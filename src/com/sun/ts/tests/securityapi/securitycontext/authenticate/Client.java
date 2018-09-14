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

package com.sun.ts.tests.securityapi.securitycontext.authenticate;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.webclient.BaseUrlClient;
import com.sun.ts.tests.common.webclient.http.HttpRequest;

import java.io.PrintWriter;
import java.util.Properties;

public class Client extends BaseUrlClient {

  // Shared test variables:
  private Properties props = null;

  // Constants:
  private static final String CLASS_TRACE_HEADER = "[Client]: ";

  private String pageServletBase = "/securityapi_securitycontext_authenticate_web";

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
   * @testName: testSecurityContextAuthenticate
   *
   * @assertion_ids: Security:JAVADOC:4; Security:JAVADOC:5; Security:JAVADOC:6;
   * Security:JAVADOC:9; Security:JAVADOC:20; Security:JAVADOC:29;
   * Security:JAVADOC:37; Security:JAVADOC:41; Security:SPEC:4.2-1;
   * Security:SPEC:4.4-1
   *
   * @test_Strategy: 1. Send request without authentication 2. The application
   * will programmatically trigger the authentication process 3. Ensure
   * SecurityContext getCallerPrincipal returns the correct name 4. Ensure
   * SecurityContext isCallerInRole returns the correct value
   *
   */
  public void testSecurityContextAuthenticate() throws Fault {
    logMessage("Sending request with Authroization header...");
    String pageSec = pageServletBase + "/servlet?name=tom&password=secret1";
    String username = "tom";

    StringBuffer sb = new StringBuffer(100);
    sb.append("Authentication successed").append("|");
    sb.append("context username: ").append(username).append("|");
    sb.append("context user has role \"Administrator\": true").append("|");
    sb.append("context user has role \"Manager\": true").append("|");
    sb.append("context user has role \"Employee\": false");

    TEST_PROPS.setProperty(TEST_NAME,
        "SecurityContext/Authenticate/testSecurityContextAuthenticate");
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", pageSec));
    TEST_PROPS.setProperty(SEARCH_STRING, sb.toString());
    invoke();

    dumpResponse(); // debug aid
  }

  /*
   * @testName: testSecurityContextAuthenticate_wrongCredential
   *
   * @assertion_ids: Security:JAVADOC:5;
   *
   * @test_Strategy: 1. Send request without authentication 2. The application
   * will programmatically trigger the authentication process
   *
   */
  public void testSecurityContextAuthenticate_wrongCredential() throws Fault {
    logMessage("Sending request with Authroization header...");
    String pageSec = pageServletBase + "/servlet?name=tom&password=wrongpwd";

    StringBuffer sb = new StringBuffer(100);
    sb.append("Authentication failed");

    TEST_PROPS.setProperty(TEST_NAME,
        "SecurityContext/Authenticate/testSecurityContextAuthenticate_wrongCredential");
    TEST_PROPS.setProperty(REQUEST, getRequestLine("GET", pageSec));
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
