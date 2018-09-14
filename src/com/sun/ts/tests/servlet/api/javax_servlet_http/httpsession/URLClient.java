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

package com.sun.ts.tests.servlet.api.javax_servlet_http.httpsession;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setServletName("TestServlet");
    setContextRoot("/servlet_jsh_httpsession_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   * servlet_waittime;
   */

  /* Run test */

  /*
   * @testName: getCreationTimeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:465
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void getCreationTimeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getCreationTimeTest");
    invoke();
  }

  /*
   * @testName: getCreationTimeIllegalStateExceptionTest
   * 
   * @assertion_ids: Servlet:JAVADOC:466
   * 
   * @test_Strategy: Servlet starts session, invalidates it then calls method
   */
  public void getCreationTimeIllegalStateExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getCreationTimeIllegalStateExceptionTest");
    invoke();
  }

  /*
   * @testName: getIdTestServlet
   * 
   * @assertion_ids: Servlet:JAVADOC:467
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void getIdTestServlet() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getIdTestServlet");
    invoke();
  }

  /*
   * @testName: getIdIllegalStateExceptionTest
   * 
   * @assertion_ids: Servlet:JAVADOC:467
   * 
   * @test_Strategy: Create a HttpSession; invalidate it; Verify that no
   * IllegalStateException is thrown when getId is called.
   */
  public void getIdIllegalStateExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getIdIllegalStateExceptionTest");
    invoke();
  }

  /*
   * @testName: getLastAccessedTimeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:469
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void getLastAccessedTimeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getLastAccessedTimeTest");
    invoke();
  }

  /*
   * @testName: getLastAccessedTimeSetGetTest
   * 
   * @assertion_ids: Servlet:JAVADOC:470
   * 
   * @test_Strategy: Servlet does a get/set operation
   */
  public void getLastAccessedTimeSetGetTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getLastAccessedTimeSetGetTest");
    invoke();
  }

  /*
   * @testName: expireHttpSessionTest
   *
   * @assertion_ids: Servlet:SPEC:202; Servlet:SPEC:203; Servlet:SPEC:66;
   * Servlet:JAVADOC:565; Servlet:JAVADOC:473; Servlet:JAVADOC:469;
   * Servlet:JAVADOC:470; Servlet:JAVADOC:566;
   *
   * @test_Strategy: 1. Client sends request to TestServlet which - creates a
   * session using request.getSession() - and sets the session's
   * maxInactiveInterval to 10 secs. Client captures the JSESSIONID returned in
   * the response. Client records the current system time (t1),
   * 
   * 2. Within maxInactiveInterval, client sends a request that contains the
   * JSESSIONID in a request cookie to file index.html Client receives the
   * response and records the current system time (t2).
   * 
   * 3. Client makes request with JSESSIONID to servlet getLastAccessedTime,
   * including both t1 and t2 as request params. Servlet resumes session by
   * calling request.getSession(false), verify t2 >
   * session.getLastAccessedTime() > t1
   * 
   * 4. Client waits for longer than maxInactiveInterval, and sends a
   * request(including JSESSIONID) to the third servlet. The servlet tries to
   * resume the session by calling request.getSession(false). Verify that no
   * session is returned this time
   */

  public void expireHttpSessionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getSessionMax");
    TEST_PROPS.setProperty(SAVE_STATE, "true");
    invoke();

    Long t1 = System.currentTimeMillis();
    int tmp = 0;
    while (System.currentTimeMillis() - t1 < 10) {
      tmp++;
    }

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/index.html HTTP/1.1");
    TEST_PROPS.setProperty(SAVE_STATE, "true");
    TEST_PROPS.setProperty(USE_SAVED_STATE, "true");
    TEST_PROPS.setProperty(SEARCH_STRING, "INDEX from index.html");
    invoke();
    Long t2 = System.currentTimeMillis();

    while (System.currentTimeMillis() - t2 < 10) {
      tmp++;
    }

    TEST_PROPS.setProperty(REQUEST, "GET " + getContextRoot()
        + "/getLastAccessedTime?t1=" + t1 + "&t2=" + t2 + " HTTP/1.1");
    TEST_PROPS.setProperty(SAVE_STATE, "true");
    TEST_PROPS.setProperty(USE_SAVED_STATE, "true");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test passed: session.getLastAccessedTime()");
    invoke();

    Long t3 = System.currentTimeMillis();
    int wait = Integer.parseInt(_props.getProperty("servlet_waittime").trim());
    while (System.currentTimeMillis() - t3 < (10000 + wait)) {
      tmp++;
    }

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/expireHttpSession HTTP/1.1");
    TEST_PROPS.setProperty(USE_SAVED_STATE, "true");
    TEST_PROPS.setProperty(SEARCH_STRING, "Session expired as expected.");
    invoke();
  }

  /*
   * @testName: getLastAccessedTimeIllegalStateExceptionTest
   * 
   * @assertion_ids: Servlet:JAVADOC:471
   * 
   * @test_Strategy: Servlet verifies exception is generated
   */
  public void getLastAccessedTimeIllegalStateExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getSession");
    TEST_PROPS.setProperty(SAVE_STATE, "true");
    invoke();
    TEST_PROPS.setProperty(APITEST,
        "getLastAccessedTimeIllegalStateExceptionTest");
    TEST_PROPS.setProperty(USE_SAVED_STATE, "true");
    invoke();
  }

  /*
   * @testName: getMaxInactiveIntervalTest
   * 
   * @assertion_ids: Servlet:JAVADOC:474
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void getMaxInactiveIntervalTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getMaxInactiveIntervalTest");
    invoke();
  }

  /*
   * @testName: getAttributeNamesTest
   * 
   * @assertion_ids: Servlet:JAVADOC:480
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void getAttributeNamesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getAttributeNamesTest");
    invoke();
  }

  /*
   * @testName: getAttributeNamesIllegalStateExceptionTest
   * 
   * @assertion_ids: Servlet:JAVADOC:481
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void getAttributeNamesIllegalStateExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST,
        "getAttributeNamesIllegalStateExceptionTest");
    invoke();
  }

  /*
   * @testName: getAttributeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:476
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void getAttributeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getAttributeTest");
    invoke();
  }

  /*
   * @testName: getAttributeIllegalStateExceptionTest
   * 
   * @assertion_ids: Servlet:JAVADOC:477
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void getAttributeIllegalStateExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getAttributeIllegalStateExceptionTest");
    invoke();
  }

  /*
   * @testName: getServletContextTest
   * 
   * @assertion_ids: Servlet:JAVADOC:472
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void getServletContextTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getServletContextTest");
    invoke();
  }

  /*
   * @testName: invalidateTest
   * 
   * @assertion_ids: Servlet:JAVADOC:496
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void invalidateTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "invalidateTest");
    invoke();
  }

  /*
   * @testName: invalidateIllegalStateExceptionTest
   * 
   * @assertion_ids: Servlet:JAVADOC:497
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void invalidateIllegalStateExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "invalidateIllegalStateExceptionTest");
    invoke();
  }

  /*
   * @testName: isNewTest
   * 
   * @assertion_ids: Servlet:JAVADOC:498
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void isNewTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "isNewTest");
    invoke();
  }

  /*
   * @testName: isNewIllegalStateExceptionTest
   * 
   * @assertion_ids: Servlet:JAVADOC:499
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void isNewIllegalStateExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "isNewIllegalStateExceptionTest");
    invoke();
  }

  /*
   * @testName: removeAttributeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:492
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void removeAttributeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "removeAttributeTest");
    invoke();
  }

  /*
   * @testName: removeAttributeDoNothingTest
   * 
   * @assertion_ids: Servlet:JAVADOC:491
   * 
   * @test_Strategy: Servlet removes non-existant attribute then tries to tries
   * to get it.
   */
  public void removeAttributeDoNothingTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "removeAttributeDoNothingTest");
    invoke();
  }

  /*
   * @testName: removeAttributeIllegalStateExceptionTest
   * 
   * @assertion_ids: Servlet:JAVADOC:493
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void removeAttributeIllegalStateExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "removeAttributeIllegalStateExceptionTest");
    invoke();
  }

  /*
   * @testName: setAttributeTest
   * 
   * @assertion_ids: Servlet:JAVADOC:484
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void setAttributeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "setAttributeTest");
    invoke();
  }

  /*
   * @testName: setAttributeNullTest
   * 
   * @assertion_ids: Servlet:JAVADOC:487
   * 
   * @test_Strategy: Servlet passes null to setAttribute
   */
  public void setAttributeNullTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "setAttributeNullTest");
    invoke();
  }

  /*
   * @testName: setAttributeIllegalStateExceptionTest
   * 
   * @assertion_ids: Servlet:JAVADOC:488
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void setAttributeIllegalStateExceptionTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "setAttributeIllegalStateExceptionTest");
    invoke();
  }

  /*
   * @testName: setMaxInactiveIntervalTest
   * 
   * @assertion_ids: Servlet:JAVADOC:473
   * 
   * @test_Strategy: Servlet tests method and returns result to client
   */
  public void setMaxInactiveIntervalTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "setMaxInactiveIntervalTest");
    invoke();
  }
}
