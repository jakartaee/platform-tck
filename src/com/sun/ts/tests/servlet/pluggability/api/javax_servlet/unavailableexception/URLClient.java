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
package com.sun.ts.tests.servlet.pluggability.api.javax_servlet.unavailableexception;

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

    setContextRoot("/servlet_plu_unavailableexception_web");
    setServletName("TestServlet");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run test */

  /*
   * @testName: getUnavailableSecondsTest
   * 
   * @assertion_ids: Servlet:SPEC:11; Servlet:JAVADOC:4; Servlet:JAVADOC:7;
   *
   * @test_Strategy: A test for UnavailableException.getUnavailableSeconds()
   * method.
   */
  public void getUnavailableSecondsTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getUnavailableSecondsTest");
    invoke();
  }

  /*
   * @testName: isPermanentTest
   *
   * @assertion_ids: Servlet:SPEC:11; Servlet:JAVADOC:3; Servlet:JAVADOC:4;
   * Servlet:JAVADOC:5;
   *
   * @test_Strategy: A test for UnavailableException.isPermanent() method.
   */
  public void isPermanentTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "isPermanentTest");
    invoke();
  }

  /*
   * @testName: unavailableTest
   *
   * @assertion_ids: Servlet:SPEC:11; Servlet:SPEC:11.1; Servlet:JAVADOC:3;
   * Servlet:JAVADOC:268;
   *
   * @test_Strategy: A test for Permanent Unavailable First access the Servlet,
   * and mark it unavailable Second try to access it again, 404 should be
   * returned
   */
  public void unavailableTest() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING, " ");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "");
    TEST_PROPS.setProperty(DONOTUSEServletName, "true");
    TEST_PROPS.setProperty(STATUS_CODE, "404");
    TEST_PROPS.setProperty(APITEST, "unavailableTest");
    invoke();
  }

  /*
   * @testName: unavailableException_Constructor1Test
   * 
   * @assertion_ids: Servlet:SPEC:11; Servlet:JAVADOC:3;
   *
   * @test_Strategy: A test for UnavailableException(String mesg). It construts
   * an UnavailabaleException object for the specified servlet. This constructor
   * tests for permanent unavailability
   */
  public void unavailableException_Constructor1Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "unavailableException_Constructor1Test");
    invoke();
  }

  /*
   * @testName: unavailableException_Constructor2Test
   * 
   * @assertion_ids: Servlet:SPEC:11; Servlet:JAVADOC:4;
   *
   * @test_Strategy: A test for UnavailableException(String mesg, int sec). It
   * construts an UnavailabaleException object for the specified servlet. This
   * constructor tests for temporarily unavailability
   */
  public void unavailableException_Constructor2Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "unavailableException_Constructor2Test");
    invoke();
  }
}
