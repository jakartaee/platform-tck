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

package com.sun.ts.tests.securityapi.idstore.multi;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.securityapi.idstore.common.BaseIDStoreClient;
import com.sun.ts.tests.common.webclient.http.HttpRequest;

import java.io.PrintWriter;
import java.util.Properties;

public class Client extends BaseIDStoreClient {

  private String pageServletBase = "/securityapi_idstore_multi_web/ServletForMultiIDStore";

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
   * @testName: testIdentityStoreValidate_multiIDStore
   *
   * @assertion_ids:
   * Security:JAVADOC:103;Security:JAVADOC:104;Security:JAVADOC:105;Security:
   * SPEC:3.2.4-3;Security:SPEC:3.2.4-4;Security:SPEC:3.3-2;Security:SPEC:3.2.3-
   * 1
   *
   * @test_Strategy:
   * 
   * Test validate within multi IDStore and return valid result.
   * 
   * 3 ID store are implemented. IDStore1 (priority=100, ValidationType=BOTH):
   * user/pwd/group = tom/secret1/Administrator1:Manager1 user/pwd/group =
   * emma/secret2/Administrator1:Employee1 IDStore2 (priority=200,
   * ValidationType=VALIDATION): user/pwd/group =
   * tom/secret2/Administrator2:Manager2 user/pwd/group =
   * emma/secret2/Administrator1:Employee1 IDStore3
   * (priority=300,ValidationType=BOTH): user/pwd/group =
   * tom/secret2/Administrator3:Manager3 user/pwd/group =
   * emma/secret2/Administrator1:Employee1 Request with tom/sercret2, the
   * validate in IDStore3 is not called since validate in IDStore2 is VALID. And
   * groups in IDStore2 will be returned.
   * 
   * BTW, check the the validate is called or not by adding one additional group
   * in CrendentialValidationResult
   */
  public void testIdentityStoreValidate_multiIDStore() throws Fault {
    String testName = "idstore/multi/testIdentityStoreValidateGetGroup_multiIDStore";

    // return VALID in IDStore2 with user=tom/secret2
    StringBuffer sb = new StringBuffer(100);
    sb.append("ValidateResultStatus=VALID").append("|");
    sb.append("ValidateResultGroups=[]").append("|");
    sb.append("web username: ").append("tom");
    sendRequestAndVerify(testName, pageServletBase, "tom", "secret2",
        sb.toString());

    // return VALID in IDStore1 with user=tom/secret1
    StringBuffer sb1 = new StringBuffer(100);
    sb1.append("ValidateResultStatus=VALID").append("|");
    sb1.append("ValidateResultGroups=[IDStore1:validate]").append("|");
    sb1.append("web username: ").append("tom");
    sendRequestAndVerify(testName, pageServletBase, "tom", "secret1",
        sb1.toString());

    // return VALID in IDStore1 with user=emma/secret2
    StringBuffer sb2 = new StringBuffer(100);
    sb2.append("ValidateResultStatus=VALID").append("|");
    sb2.append("ValidateResultGroups=[IDStore1:validate]").append("|");
    sb2.append("web username: ").append("emma");
    sendRequestAndVerify(testName, pageServletBase, "emma", "secret2",
        sb2.toString());
  }

  /*
   * @testName: testIdentityStoreValidate_multiIDStore_INVALID
   *
   * @assertion_ids:
   * Security:SPEC:3.2.2-2;Security:SPEC:3.2.3-2;Security:SPEC:3.2.4-5;Security:
   * SPEC:3.2.2-1
   *
   * @test_Strategy:
   * 
   * Test validate within multi IDStore and return valid result.
   * 
   * 3 ID store (ValidationType=BOTH) implemented. IDStore1 (priority=100):
   * user/pwd/group = tom/secret1/Administrator1:Manager1 IDStore2
   * (priority=200): user/pwd/group = tom/secret2/Administrator2:Manager2
   * IDStore3 (priority=300): user/pwd/group =
   * tom/secret2/Administrator3:Manager3 Request with tom/sercret_invalid, the
   * validate in all of 3 IDStore will be INVALID. The final result is INVALID
   * 
   */
  public void testIdentityStoreValidate_multiIDStore_INVALID() throws Fault {
    String testName = "idstore/multi/testIdentityStoreValidate_multiIDStore_INVALID";

    StringBuffer sb = new StringBuffer(100);
    sb.append("ValidateResultStatus=INVALID").append("|");
    sb.append("ValidateResultGroups=[]");
    sendRequestAndVerify(testName, pageServletBase, "tom", "secret_invalid",
        sb.toString());

  }

  /*
   * @testName: testIdentityStoreValidate_multiIDStore_INVALIDWithNOTVALIDATED
   *
   * @assertion_ids:
   * Security:SPEC:3.2.2-2;Security:SPEC:3.2.3-2;Security:SPEC:3.2.4-6;Security:
   * SPEC:3.2.2-1
   *
   * @test_Strategy:
   * 
   * Test validate within multi IDStore and return valid result.
   * 
   * 3 ID store (ValidationType=BOTH) implemented. IDStore1 (priority=100):
   * notvalidated/secret4 always return NOT_VALIDATED
   * notvalidated_invalid1/secret4 alawys return INVALID
   * notvalidated_invalid2/secret4 alawys return NOT_VALIDATED
   * notvalidated_invalid3/secret4 alawys return NOT_VALIDATED IDStore2
   * (priority=200): notvalidated/secret4 always return NOT_VALIDATED
   * notvalidated_invalid1/secret4 alawys return NOT_VALIDATED
   * notvalidated_invalid2/secret4 alawys return INVALID
   * notvalidated_invalid3/secret4 alawys return NOT_VALIDATED IDStore3
   * (priority=300): notvalidated/secret4 always return NOT_VALIDATED
   * notvalidated_invalid1/secret4 alawys return NOT_VALIDATED
   * notvalidated_invalid2/secret4 alawys return NOT_VALIDATED
   * notvalidated_invalid3/secret4 alawys return INVALID
   * 
   * Steps: 1.Request with notValidated_invalid1/secret4, result from IDtore1 is
   * INVALID, from IDStore2,3 is NOT_VALIDATED, then the finial result is
   * INVALID 2.Request with notValidated_invalid2/secret4, result from IDtore2
   * is INVALID, from IDStore1,3 is NOT_VALIDATED, then the finial result is
   * INVALID 3.Request with notValidated_invalid2/secret4, result from IDtore3
   * is INVALID, from IDStore1,3 is NOT_VALIDATED, then the finial result is
   * INVALID *
   */
  public void testIdentityStoreValidate_multiIDStore_INVALIDWithNOTVALIDATED()
      throws Fault {
    String testName = "idstore/multi/testIdentityStoreValidate_multiIDStore_INVALIDWithNOTVALIDATED";
    String userName_tom = "tom";
    String userName_notValidated = "notValidated";

    logMessage("===============Step1:========================");
    StringBuffer sb1 = new StringBuffer(100);
    sb1.append("ValidateResultStatus=INVALID").append("|");
    sb1.append("ValidateResultGroups=[]");
    sendRequestAndVerify(testName, pageServletBase, "notValidated_invalid1",
        "secret11", sb1.toString());

    logMessage("===============Step2:========================");
    StringBuffer sb2 = new StringBuffer(100);
    sb1.append("ValidateResultStatus=INVALID").append("|");
    sb1.append("ValidateResultGroups=[]");
    sendRequestAndVerify(testName, pageServletBase, "notValidated_invalid2",
        "secret11", sb2.toString());

    logMessage("===============Step3:========================");
    StringBuffer sb3 = new StringBuffer(100);
    sb3.append("ValidateResultStatus=INVALID").append("|");
    sb3.append("ValidateResultGroups=[]");
    sendRequestAndVerify(testName, pageServletBase, "notValidated_invalid3",
        "secret11", sb3.toString());

  }

  /*
   * @testName: testIdentityStoreValidate_multiIDStore_NOTVALIDATED
   *
   * @assertion_ids:
   * Security:SPEC:3.2.2-2;Security:SPEC:3.2.3-2;Security:SPEC:3.2.4-6;Security:
   * SPEC:3.2.2-1
   *
   * @test_Strategy:
   * 
   * Test validate within multi IDStore and return valid result.
   * 
   * 3 ID store (ValidationType=BOTH) implemented. IDStore1 (priority=100):
   * notvalidated/secret4 always return NOT_VALIDATED
   * notvalidated_invalid1/secret4 alawys return INVALID
   * notvalidated_invalid2/secret4 alawys return NOT_VALIDATED
   * notvalidated_invalid3/secret4 alawys return NOT_VALIDATED IDStore2
   * (priority=200): notvalidated/secret4 always return NOT_VALIDATED
   * notvalidated_invalid1/secret4 alawys return NOT_VALIDATED
   * notvalidated_invalid2/secret4 alawys return INVALID
   * notvalidated_invalid3/secret4 alawys return NOT_VALIDATED IDStore3
   * (priority=300): notvalidated/secret4 always return NOT_VALIDATED
   * notvalidated_invalid1/secret4 alawys return NOT_VALIDATED
   * notvalidated_invalid2/secret4 alawys return NOT_VALIDATED
   * notvalidated_invalid3/secret4 alawys return INVALID
   * 
   * Steps: 1.Request with notValidated/secret1, all validate results are
   * NOT_VALIDATED, the the final result is NOT_VALIDATED
   *
   */
  public void testIdentityStoreValidate_multiIDStore_NOTVALIDATED()
      throws Fault {
    String testName = "idstore/multi/testIdentityStoreValidate_multiIDStore_NOTVALIDATED";

    StringBuffer sb3 = new StringBuffer(100);
    sb3.append("ValidateResultStatus=NOT_VALIDATED").append("|");
    sb3.append("ValidateResultGroups=[]");
    sendRequestAndVerify(testName, pageServletBase, "notValidated", "secret4",
        sb3.toString());

  }

}
