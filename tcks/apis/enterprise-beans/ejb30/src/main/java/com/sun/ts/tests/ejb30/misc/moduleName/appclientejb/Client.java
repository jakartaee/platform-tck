/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2024 Contributors to the Eclipse Foundation
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

package com.sun.ts.tests.ejb30.misc.moduleName.appclientejb;

import static com.sun.ts.tests.ejb30.common.helper.ServiceLocator.lookupNoTry;
import static com.sun.ts.tests.ejb30.common.helper.ServiceLocator.lookupShouldFail;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.tests.common.base.EETest;
import com.sun.ts.tests.ejb30.assembly.appres.common.AppResRemoteIF;
import com.sun.ts.tests.ejb30.common.helper.Helper;

import jakarta.annotation.PostConstruct;
import jakarta.ejb.EJB;

public class Client extends EETest {
  private static StringBuilder postConstructRecords = new StringBuilder();

  @EJB(name = "moduleBean")
  private static AppResRemoteIF moduleBean;

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
    lookupShouldFail("java:module/ModuleBean", postConstructRecords);
    lookupShouldFail(
        "java:app/ejb3_misc_moduleName_appclientejb_client/ModuleBean",
        postConstructRecords);
    lookupShouldFail(
        "java:app/ejb3_misc_moduleName_appclientejb_ejb/ModuleBean",
        postConstructRecords);
    lookupShouldFail(
        "java:global/ejb3_misc_moduleName_appclientejb/ejb3_misc_moduleName_appclientejb_ejb/ModuleBean",
        postConstructRecords);

    Helper.getLogger().info(postConstructRecords.toString());

    Helper.assertNotEquals(null, null, moduleBean, postConstructRecords);

    AppResRemoteIF lookupResult = null;
    String[] names = {
        "java:app/renamed_appclientejb_ejb/ModuleBean",
        "java:global/ejb3_misc_moduleName_appclientejb/renamed_appclientejb_ejb/ModuleBean" };
    for (String name : names) {
      postConstructRecords.append("About to look up " + name);
      lookupResult = (AppResRemoteIF) lookupNoTry(name);
      Helper.assertNotEquals(null, null, lookupResult, postConstructRecords);
      lookupResult = null;
    }

    String lookup = "java:module/ModuleName";
    String moduleName = (String) lookupNoTry(lookup);
    Helper.assertEquals("Check " + lookup, "renamed_appclientejb_client",
        moduleName, postConstructRecords);
  }

  /*
   * @testName: clientPostConstruct
   * 
   * @test_Strategy: verify data sources injected and declared in descriptors
   * inside appclient
   */
  public void clientPostConstruct() {
    Helper.getLogger().info(postConstructRecords.toString());
  }

  /*
   * @testName: ejbPostConstruct
   * 
   * @test_Strategy: verify data sources injected and declared in descriptors
   * inside ejb
   */
  public void ejbPostConstruct() {
    Helper.getLogger().info(moduleBean.getPostConstructRecords().toString());
  }

}
