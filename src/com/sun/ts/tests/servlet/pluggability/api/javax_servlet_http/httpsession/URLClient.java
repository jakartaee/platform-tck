/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id:$
 */
package com.sun.ts.tests.servlet.pluggability.api.javax_servlet_http.httpsession;

import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import java.io.PrintWriter;

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
    setContextRoot("/servlet_pluh_httpsession_web");

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
