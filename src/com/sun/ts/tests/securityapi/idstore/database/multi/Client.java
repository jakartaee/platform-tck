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

package com.sun.ts.tests.securityapi.idstore.database.multi;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.securityapi.idstore.common.BaseIDStoreClient;
import com.sun.ts.tests.common.webclient.http.HttpRequest;

import java.io.PrintWriter;
import java.util.Properties;

public class Client extends BaseIDStoreClient {

  private String pageServletBase = "/securityapi_idstore_database_multi_web";

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
   * @testName: testAnnotationDBIDStore_multi_withLdap
   *
   * @assertion_ids:
   * Security:SPEC:3.1-2;Security:JAVADOC:126;Security:JAVADOC:117;
   *
   * @test_Strategy:
   * 
   * Test the properties of DB IDStore annotation and default authn&authz.
   * DBIDStore supports both authentication and authorization. And below
   * properties is specified: callerQuery,dataSourceLookup,groupsQuery Step1.
   * request with valid user/pwd, return VALID and groups Step2. request with
   * valid user / invalid pwd, return INVALID Step3. request with invalid user,
   * return INVALID
   * 
   * 
   */
  public void testAnnotationDBIDStore_multi_withLdap() throws Fault {
    String testName = "idstore/database/testAnnotationDBIDStore_multi_withLdap";
    String pageSec = pageServletBase + "/ServletForDatabaseIDStore";
    String username = "tom";
    String password = "secret1";

    // Step1. request with valid user/pwd, return VALID and groups
    String url = pageSec + "?user=" + username + "&pwd=" + password;
    StringBuffer sb = new StringBuffer(100);
    sb.append("ValidateResultStatus=VALID").append("|");
    sb.append("ValidateResultGroups=[Administrator, Manager]").append("|");
    sb.append("web username: ").append(username);

    TEST_PROPS.setProperty(TEST_NAME, testName + "_tom");
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", url));
    TEST_PROPS.setProperty(SEARCH_STRING, sb.toString());
    invoke();
    dumpResponse();

    // Step2. request with valid user/pwd, return VALID and groups
    username = "tomx";
    url = pageSec + "?user=" + username + "&pwd=" + password;
    StringBuffer sb1 = new StringBuffer(100);
    sb1.append("ValidateResultStatus=VALID").append("|");
    sb1.append("ValidateResultGroups=[Administratorx, Managerx]").append("|");
    sb1.append("web username: ").append(username);

    TEST_PROPS.setProperty(TEST_NAME, testName + "_tomx");
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", url));
    TEST_PROPS.setProperty(SEARCH_STRING, sb1.toString());
    invoke();
    dumpResponse();

  }

}
