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

package com.sun.ts.tests.securityapi.idstore.database.basic;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.securityapi.idstore.common.BaseIDStoreClient;
import com.sun.ts.tests.common.webclient.http.HttpRequest;

import java.io.PrintWriter;
import java.util.Properties;

public class Client extends BaseIDStoreClient {

  private String pageServletBase = "/securityapi_idstore_database_basic_web";

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
   * @testName: testAnnotationDBIDStore_Basic
   *
   * @assertion_ids:
   * Security:JAVADOC:118;Security:JAVADOC:112;Security:JAVADOC:113;Security:
   * JAVADOC:114;Security:JAVADOC:170;Security:SPEC:3.2-1;Security:SPEC:3.4-2;
   * Security:JAVADOC:104;Security:JAVADOC:108;Security:SPEC:3.1-1;
   * Security:SPEC:3.2.1-2;Security:SPEC:3.2.1-3
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
  public void testAnnotationDBIDStore_Basic() throws Fault {
    String testName = "idstore/database/testAnnotationDBIDStore_Basic";
    String pageSec = pageServletBase + "/ServletForDatabaseIDStore";
    String username = "tom";
    String password = "secret1";

    // Step1. request with valid user/pwd, return VALID and groups
    String url = pageSec + "?user=" + username + "&pwd=" + password;
    StringBuffer sb = new StringBuffer(100);
    sb.append("ValidateResultStatus=VALID").append("|");
    sb.append("ValidateResultGroups=[Administrator, Manager]").append("|");
    sb.append("ValidateCallerDN=null").append("|");
    sb.append("web username: ").append(username);

    TEST_PROPS.setProperty(TEST_NAME, testName + "_valid");
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", url));
    TEST_PROPS.setProperty(SEARCH_STRING, sb.toString());
    invoke();
    dumpResponse();

    // Step2. request with valid user / invalid pwd, return INVALID
    url = pageSec + "?user=" + username + "&pwd=invalid_pwd";
    StringBuffer sb1 = new StringBuffer(100);
    sb1.append("ValidateResultStatus=INVALID").append("|");
    sb1.append("ValidateResultGroups=[]").append("|");

    TEST_PROPS.setProperty(TEST_NAME, testName + "_pwd_invalid");
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", url));
    TEST_PROPS.setProperty(SEARCH_STRING, sb1.toString());
    invoke();
    dumpResponse();

    // Step3. request with invalid user, return INVALID
    url = pageSec + "?user=invalid_user&pwd=" + password;
    StringBuffer sb2 = new StringBuffer(100);
    sb2.append("ValidateResultStatus=INVALID").append("|");
    sb2.append("ValidateResultGroups=[]").append("|");

    TEST_PROPS.setProperty(TEST_NAME, testName + "_user_invalid");
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", url));
    TEST_PROPS.setProperty(SEARCH_STRING, sb2.toString());
    invoke();
    dumpResponse();

  }

}
