/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.securityapi.idstore.customhandler;

import java.io.PrintWriter;
import java.util.Properties;

import com.sun.javatest.Status;
import com.sun.ts.tests.securityapi.idstore.common.BaseIDStoreClient;

public class Client extends BaseIDStoreClient {

  private String pageServletBase = "/securityapi_idstore_customhandler_web";

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
   * @testName: testIdentityStore_customHandler
   *
   * @assertion_ids: Security:SPEC:3.1-4; Security:SPEC:3.2.1-5;
   * Security:SPEC:3.2.2-3
   *
   * @test_Strategy:
   * 
   * Test applications can also supply their own implementation. And given
   * IdentityStorePermission. Implement 1 IdentityStoreHandler, if all
   * validation is true, then all the groups in which PROVIDE_GROUP is
   * specified. Meanwhile, one extra groupo "customIdentiyStoreHandler" would be
   * added in the groupset. If any validation is false, return INVALID
   * 
   * Step1: Request tom/secret1, return VALID and all groups in
   * DefaultIdentityStore and LdapIdentityStore Step2: Request emma/secret2,
   * return INVALID since validation is invalid in LdapIdentityStore
   */
  public void testIdentityStore_customHandler() throws Fault {
    String testName = "idstore/customhandler/testIdentityStore_customHandler";
    String pageSec = pageServletBase + "/ServletForIDStoreCustomhandler";
    String username = "tom";
    String password = "secret1";

    String url = pageSec + "?user=" + username;
    url += "&pwd=" + password;

    StringBuffer sb = new StringBuffer(100);
    sb.append("ValidateResultStatus=VALID").append("|");
    sb.append("ValidateResultGroups=[").append("|");
    sb.append("web username: ").append(username);

    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", url));
    TEST_PROPS.setProperty(SEARCH_STRING, sb.toString());
    invoke();
    dumpResponse();
    // The groups in Ldap server is not returned due to bug#
    // https://github.com/javaee-security-spec/soteria/issues/78
    checkGroupsSet("ValidateResultGroups",
        "customIdentiyStoreHandler, getCallerGroups, Administrator, Manager, Administrator1, Manager1");

    username = "emma";
    password = "secret2";
    url = pageSec + "?user=" + username;
    url += "&pwd=" + password;

    StringBuffer sb1 = new StringBuffer(100);
    sb1.append("ValidateResultStatus=INVALID").append("|");
    sb1.append("ValidateResultGroups=[]").append("|");
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", url));
    TEST_PROPS.setProperty(SEARCH_STRING, sb1.toString());
    invoke();
    dumpResponse();

  }

}
