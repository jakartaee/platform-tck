/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.securityapi.idstore.multiauthz;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.securityapi.idstore.common.BaseIDStoreClient;
import com.sun.ts.tests.common.webclient.http.HttpRequest;

import java.io.PrintWriter;
import java.util.Properties;

public class Client extends BaseIDStoreClient {

  private String pageServletBase = "/securityapi_idstore_multiauthz_web/ServletForMultiAuthzIDStore";

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    Client theTests = new Client();

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   *
   */
  // Note:Based on the input argument setup will intialize JSP or servlet pages

  public void setup(String[] args, Properties p) throws Fault {
    super.setup(args, p);

    props = p;
  }

  /*
   * @testName: testIdentityStore_getGroups_multiGroupStore_highPriority_valid
   *
   * @assertion_ids:
   * Security:SPEC:3.2.2-1;Security:SPEC:3.2.3-1;Security:SPEC:3.2.4-1;Security:
   * SPEC:3.2.4-7;Security:SPEC:3.2.4-8
   *
   * @test_Strategy:
   * 
   * Test validate within multiauthz IDStore and return valid result in the
   * IDStore with Higer priority
   * 
   * 1 ID store (IdentityStore1, ValidationType=BOTH) implemented. IDStore1
   * (priority=400): user/pwd/group = tom/secret1/Administrator1:Manager1
   * 
   * 1 ID store (IdentityStore2, ValidationType=Validation) implemented.
   * IDStore2 (priority=300): user/pwd/group =
   * tom/secret1/Administrator2:Manager2
   * 
   * 2 Authz only store (IdentityStoreAuthz1,2, ValidateionType=PROVIDE_GROUPS
   * implemented IDStoreAuthz1 (priority=200): user/group =
   * tom/AdminAuthz1:ManagerAuthz1 IDStoreAuthz2 (priority=100): user/group =
   * tom/AdminAuthz2:ManagerAuthz2
   *
   * Request with tom/sercret1, the validate in IDStore2 is called since it is
   * has the higher proirty And returns the groups in IDStore2, IDStoreAuthz1,
   * IDStoreAuthz2
   * 
   */
  public void testIdentityStore_getGroups_multiGroupStore_highPriority_valid()
      throws Fault {
    String testName = "idstore/multiauthz/testIdentityStoreValidateGetGroup_multiauthzIDStore";

    StringBuffer sb = new StringBuffer(1000);
    sb.append("ValidateResultStatus=VALID").append("|");
    sb.append("ValidateResultGroups=[").append("|");
    sb.append("web username: ").append("tom");
    String groups = "IDStore2:validate, IDStoreAuthz1:getCallerGroups, AdminAuthz1";
    groups += ", ManagerAuthz1, IDStoreAuthz2:getCallerGroups, AdminAuthz2, ManagerAuthz2";
    sendRequestAndVerify(testName, pageServletBase, "tom", "secret1",
        sb.toString(), groups);
  }

  /*
   * @testName: testIdentityStore_getGroups_multiGroupStore_lowerPriority_valid
   *
   * @assertion_ids:
   * Security:SPEC:3.2.2-1;Security:SPEC:3.2.3-1;Security:SPEC:3.2.4-1;Security:
   * SPEC:3.2.4-7;Security:SPEC:3.2.4-8
   * 
   * @test_Strategy:
   * 
   * Test validate within multiauthz IDStore and return valid result in the
   * IDStore with lower priority
   * 
   * 1 ID store (IdentityStore1, ValidationType=BOTH) implemented. IDStore1
   * (priority=400): user/pwd/group = emma/secret2/Administrator1:Manager1
   *
   * 1 ID store (IdentityStore2, ValidationType=Validation) implemented.
   * IDStore2 (priority=300): user/pwd/group =
   * emma/secret3/Administrator2:Manager2
   *
   * 2 Authz only store (ValidateionType=Authorization implemented IDStoreAuthz1
   * (priority=200): user/group = emma/AdminAuthz1:EmployeeAuthz1 IDStoreAuthz2
   * (priority=100): user/group = emma/AdminAuthz2:EmployeeAuthz1
   *
   * Request with emma/sercret2, the validate in IDStore1 is called since the
   * higher proirty IDStore2 returns INVALID And returns the groups in IDStore1,
   * IDStoreAuthz1, IDStoreAuthz2
   * 
   */
  public void testIdentityStore_getGroups_multiGroupStore_lowerPriority_valid()
      throws Fault {
    String testName = "idstore/multiauthz/testIdentityStoreValidateGetGroup_multiauthzIDStore";

    StringBuffer sb = new StringBuffer(1000);
    sb.append("ValidateResultStatus=VALID").append("|");
    sb.append("ValidateResultGroups=[").append("|");
    sb.append("web username: ").append("emma");
    String groups = "IDStore1:validate, IDStoreAuthz2:getCallerGroups, AdminAuthz2, EmployeeAuthz2,";
    groups += "IDStoreAuthz1:getCallerGroups, AdminAuthz1, EmployeeAuthz1";
    sendRequestAndVerify(testName, pageServletBase, "emma", "secret2",
        sb.toString(), groups);
  }

}
