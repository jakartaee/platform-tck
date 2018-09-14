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

package com.sun.ts.tests.ejb30.assembly.mbean.appclient;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResCommonIF;
import com.sun.ts.tests.ejb30.common.helloejbjar.HelloRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.Helper;

public class Client extends EETest {
  private static StringBuilder postConstructRecords = new StringBuilder();

  @Resource(type = OneManagedBean.class)
  private static AppResCommonIF one;

  @Resource(lookup = "java:module/one-managed-bean")
  private static OneManagedBean oneWithLookup;

  @Resource(name = "java:comp/env/two-managed-bean")
  private static TwoManagedBean two;

  @Resource(lookup = "java:comp/env/two-managed-bean", type = TwoManagedBean.class)
  private static AppResCommonIF twoWithLookup;

  @EJB(lookup = "java:module/env/hello")
  private static HelloRemoteIF hello;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) {
  }

  public void cleanup() {
  }

  @SuppressWarnings("unused")
  @PostConstruct
  private static void postConstruct() {
    Helper.assertEquals(null, OneManagedBean.NAME, one.getName(),
        postConstructRecords);
    Helper.assertEquals(null, OneManagedBean.NAME, oneWithLookup.getName(),
        postConstructRecords);
    Helper.assertEquals(null, TwoManagedBean.NAME, two.getName(),
        postConstructRecords);

    Helper.assertEquals("Check injected hello ejb", 1 + 1, hello.add(1, 1),
        postConstructRecords);
  }

  /*
   * @testName: clientPostConstruct
   * 
   * @test_Strategy: managed beans packaged inside application client jar
   */
  public void clientPostConstruct() {
    Helper.getLogger().info(postConstructRecords.toString());
  }

  /*
   * @testName: mbeanPostConstruct
   * 
   * @test_Strategy: managed beans packaged inside application client jar
   */
  public void mbeanPostConstruct() {
    AppResCommonIF[] beans = { one, oneWithLookup, two, twoWithLookup };
    for (AppResCommonIF b : beans) {
      Helper.getLogger().info(b.getPostConstructRecords().toString());
    }
  }

}
