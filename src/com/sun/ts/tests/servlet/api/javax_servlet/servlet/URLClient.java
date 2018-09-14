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

package com.sun.ts.tests.servlet.api.javax_servlet.servlet;

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

    setContextRoot("/servlet_js_servlet_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run test */

  /*
   * @testName: DoDestroyedTest
   *
   * @assertion_ids: Servlet:SPEC:5; Servlet:SPEC:6;
   *
   * @test_Strategy: Testing that destroy method is not called during service
   * method execution
   */

  public void DoDestroyedTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "DoDestroyedTest");
    invoke();
  }

  /*
   * @testName: DoInit1Test
   *
   * @assertion_ids: Servlet:SPEC:5; Servlet:SPEC:6; Servlet:SPEC:8;
   * Servlet:JAVADOC:265; Servlet:SPEC:11; Servlet:SPEC:11.1;
   *
   * @test_Strategy: Validate a 404 is returned to the client if a permanent
   * UnavailableException is thrown during servlet initialization.
   */

  public void DoInit1Test() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "DoInit1Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /servlet_js_servlet_web/DoInit1Test HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, NOT_FOUND);
    invoke();
  }

  /*
   * @testName: DoInit2Test
   *
   * @assertion_ids: Servlet:SPEC:5; Servlet:SPEC:6; Servlet:JAVADOC:263;
   *
   * @test_Strategy: Inside CoreServletTest, which is the parent servlet, we are
   * implementing init() and setting a boolean variable to true. We'll check for
   * the variables here in the DoInit2Test
   */

  public void DoInit2Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "DoInit2Test");
    invoke();
  }

  /*
   * @testName: DoServletConfigTest
   *
   * @assertion_ids: Servlet:SPEC:5; Servlet:SPEC:6; Servlet:JAVADOC:266;
   *
   * @test_Strategy: Create a servlet and test for the getServletConfig() method
   * to be a non-null value and an initial paramter can be retrieved
   */

  public void DoServletConfigTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "DoServletConfigTest");
    invoke();
  }

  /*
   * @testName: DoServletInfoTest
   *
   * @assertion_ids: Servlet:SPEC:5; Servlet:SPEC:6; Servlet:JAVADOC:270;
   *
   * @test_Strategy: Create a servlet and test that information is returned
   */

  public void DoServletInfoTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "DoServletInfoTest");
    invoke();
  }

  /*
   * @testName: PUTest
   *
   * @assertion_ids: Servlet:SPEC:5; Servlet:SPEC:6; Servlet:JAVADOC:5;
   *
   * @test_Strategy: Create a servlet, throw UnavailableException and test if
   * isPermanent() method is true
   */

  public void PUTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "PUTest");
    invoke();
  }

  /*
   * @testName: DoServiceTest
   *
   * @assertion_ids: Servlet:SPEC:5; Servlet:SPEC:6; Servlet:JAVADOC:263;
   * Servlet:JAVADOC:267;
   *
   * @test_Strategy: Inside CoreServletTest, which is the parent servlet, we
   * will override init method and assign some value to the String. We'll check
   * for that value in the service method
   */

  public void DoServiceTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "DoServiceTest");
    invoke();
  }
}
