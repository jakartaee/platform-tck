/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v. 2.0, which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 *
 * This Source Code may also be made available under the following Secondary
 * Licenses when the conditions for such availability set forth in the
 * Eclipse Public License v. 2.0 are satisfied: GNU General Public License,
 * version 2 with the GNU Classpath Exception, which is available at
 * https://www.gnu.org/software/classpath/license.html.
 *
 * SPDX-License-Identifier: EPL-2.0 OR GPL-2.0 WITH Classpath-exception-2.0
 */

/*
 * $Id$
 */

package com.sun.ts.tests.ejb30.assembly.initorder.appclientejb;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.annotation.Resource;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.assembly.initorder.common.InitOrderRemoteIF;
import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;

public class Client extends EETest {
  @Resource(lookup = "java:app/AppName")
  private static String appNameInjected;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) {
  }

  public void cleanup() {
  }

  private static HelloRemoteIF getHelloBean() {
    return (HelloRemoteIF) ServiceLocator
        .lookupNoTry(HelloRemoteIF.GLOBAL_JNDI_NAME);
  }

  /*
   * @testName: initOrder
   * 
   * @test_Strategy: verify <initialize-in-order> set to true.
   */
  public void initOrder() {
    List<String> expected = Arrays.asList("InitOrder2Bean", "InitOrderBean");
    List<String> records = getHelloBean().getAndClearRecords();
    Helper.getLogger().info(Helper.assertEquals(null, expected, records));
  }

  /*
   * @testName: appName
   * 
   * @test_Strategy: verify <application-name> value is used instead of the
   * default app-name.
   */
  public void appName() {
    String expected = "renamed2";
    String lookup = "java:app/AppName";
    String actual = (String) ServiceLocator.lookupNoTry(lookup);
    Helper.assertEquals("Check " + lookup, expected, actual);
    Helper.assertEquals("Check appNameInjected ", expected, appNameInjected);
    Helper.getLogger().info("Got expected " + lookup + ": " + actual);

    String[] moduleAndBeanNames = { "one_ejb/InitOrderBean",
        "two_ejb/InitOrder2Bean" };

    for (String m : moduleAndBeanNames) {
      StringBuilder reason = new StringBuilder();
      String s = "java:global/renamed2/" + m;
      InitOrderRemoteIF b = (InitOrderRemoteIF) ServiceLocator.lookupNoTry(s);
      reason.append(
          String.format("Looked up by global jndi name %s, and got %s", s, b));

      s = "java:global/ejb3_assembly_initorder_appclientejb/" + m;
      try {
        b = (InitOrderRemoteIF) ServiceLocator.lookup(s);
        throw new RuntimeException("Expecting NamingException when looking up "
            + s + ", but got " + b);
      } catch (javax.naming.NamingException e) {
        reason.append(" Got expected ").append(e).append(" when looking up ")
            .append(s);
      }
      Helper.getLogger().info(reason.toString());
    }
  }

}
