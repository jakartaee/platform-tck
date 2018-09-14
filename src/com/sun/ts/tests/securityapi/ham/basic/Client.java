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

package com.sun.ts.tests.securityapi.ham.basic;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.webclient.BaseUrlClient;

import java.io.PrintWriter;
import java.util.Properties;

public class Client extends BaseUrlClient {

  // Shared test variables:
  private Properties props = null;

  // Constants:
  private static final String CLASS_TRACE_HEADER = "[Client]: ";

  private String pageServletBase = "/securityapi_ham_basic_web";

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
   * @testName: testBasicHAMValidateRequest
   *
   * @assertion_ids: Security:JAVADOC:24; Security:JAVADOC:78;
   * Security:SPEC:2.2-1; Security:SPEC:2.2-5; Security:SPEC:2.4-5;
   * Security:SPEC:2.4-1;
   *
   * @test_Strategy: 1. Send request with authentication 2. Receive correct user
   * and role info.
   *
   */
  public void testBasicHAMValidateRequest() throws Fault {
    logMessage("Sending request with Authroization header...");
    String pageSec = pageServletBase + "/servlet";
    String username = "tom";
    String password = "secret1";

    StringBuffer sb = new StringBuffer(100);
    sb.append("web username: ").append(username).append("|");
    sb.append("web user has role \"Administrator\": true").append("|");
    sb.append("web user has role \"Manager\": true").append("|");
    sb.append("web user has role \"Employee\": false").append("|");

    TEST_PROPS.setProperty(TEST_NAME, "ham/basic/testBasicHAMValidateRequest");
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", pageSec));
    TEST_PROPS.setProperty(BASIC_AUTH_USER, username);
    TEST_PROPS.setProperty(BASIC_AUTH_PASSWD, password);
    TEST_PROPS.setProperty(SEARCH_STRING, sb.toString());
    invoke();

    dumpResponse(); // debug aid

    logMessage("isUserInRole() returned expected results");
  }

  /*
   * @testName: testBasicHAMValidateRequest_wrongPassword
   *
   * @assertion_ids: Security:JAVADOC:24; Security:JAVADOC:78;
   * Security:SPEC:2.2-1; Security:SPEC:2.2-5; Security:SPEC:2.4-5;
   * Security:SPEC:2.4-1;
   *
   * @test_Strategy: 1. Send request with wrong password 2. Receive unauthorized
   * status code.
   *
   */
  public void testBasicHAMValidateRequest_wrongPassword() throws Fault {
    logMessage(
        "Sending an request for a protected resource with invalid username/password...");
    String pageSec = pageServletBase + "/servlet";
    String username = "tom";
    String wrongpassword = "wrongpwd";

    TEST_PROPS.setProperty(TEST_NAME,
        "Authmech/testBasicHAMValidateRequest_wrongPassword");
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", pageSec));
    TEST_PROPS.setProperty(BASIC_AUTH_USER, username);
    TEST_PROPS.setProperty(BASIC_AUTH_PASSWD, wrongpassword);
    TEST_PROPS.setProperty(STATUS_CODE, UNAUTHORIZED);
    invoke();

    dumpResponse(); // debug aid

    logMessage("Access Denied");
  }

  /*
   * @testName: testBasicHAMHasCorrectQualifier
   *
   * @assertion_ids: Security:SPEC:2.4-4
   *
   * @test_Strategy: 1. Send request with correct password 2. Get the qualifier
   * and scope info.
   *
   */
  public void testBasicHAMHasCorrectQualifier() throws Fault {
    logMessage(
        "Sending an request for a protected resource with invalid username/password...");
    String pageSec = pageServletBase + "/servlet2";
    String username = "tom";
    String password = "secret1";

    StringBuffer sb = new StringBuffer(100);
    sb.append("Have qualifier @Default: true").append("|");
    sb.append("Have scope @ApplicationScoped: true");

    TEST_PROPS.setProperty(TEST_NAME,
        "ham/basic/testBasicHAMHasCorrectQualifier");
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", pageSec));
    TEST_PROPS.setProperty(BASIC_AUTH_USER, username);
    TEST_PROPS.setProperty(BASIC_AUTH_PASSWD, password);
    TEST_PROPS.setProperty(SEARCH_STRING, sb.toString());
    invoke();

    dumpResponse(); // debug aid
  }

  /**
   * Returns a valid HTTP/1.1 request line.
   * 
   * @param method
   *          the request method
   * @param path
   *          the request path
   * @return a valid HTTP/1.1 request line
   */
  private static String getRequestLine(String method, String path) {
    return method + " " + path + " HTTP/1.1";
  }

  /**
   * Simple wrapper around TestUtil.logMessage().
   * 
   * @param message
   *          - the message to log
   */
  private static void logMessage(String message) {
    TestUtil.logMsg(CLASS_TRACE_HEADER + message);
  }

  /**
   * Simple wrapper around TestUtil.logTrace().
   * 
   * @param message
   *          - the message to log
   */
  private static void trace(String message) {
    TestUtil.logTrace(CLASS_TRACE_HEADER + message);
  }

  private void dumpResponse() {
    try {
      if ((_testCase != null) && (_testCase.getResponse() != null)) {
        trace(_testCase.getResponse().getResponseBodyAsString());
      }
    } catch (Exception ex) {
      // must've had problem getting response so dump exception but continue on
      ex.printStackTrace();
    }
  }

}
