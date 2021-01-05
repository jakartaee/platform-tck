/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.servlet.api.jakarta_servlet.servletcontext303;

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

    setContextRoot("/servlet_js_servletcontext303_web");
    setServletName("TestServlet");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /* Run test */

  /*
   * @testName: negativeaddSRAListenerClassTest
   *
   * @assertion_ids: Servlet:JAVADOC:672.11; Servlet:JAVADOC:673.11;
   *
   * @test_Strategy: Create a Servlet, in which, a
   * ServletRequestAttributeListener is added; Verify in servlet that
   * java.lang.IllegalStateException is thrown.
   */
  public void negativeaddSRAListenerClassTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "negativeaddSRAListenerClassTest");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "SRAttributeListener");
    invoke();
  }

  /*
   * @testName: negativeaddSRAListenerStringTest
   *
   * @assertion_ids: Servlet:JAVADOC:671.11;
   *
   * @test_Strategy: Create a Servlet, in which, a
   * ServletRequestAttributeListener is added; Verify in servlet that
   * java.lang.IllegalStateException is thrown.
   */
  public void negativeaddSRAListenerStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "negativeaddSRAListenerStringTest");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "SRAttributeListener");
    invoke();
  }

  /*
   * @testName: negativeaddSRListenerClassTest
   *
   * @assertion_ids: Servlet:JAVADOC:672.11; Servlet:JAVADOC:673.11;
   *
   * @test_Strategy: Create a Servlet, in which, a ServletRequestListener is
   * added; Verify in servlet that java.lang.IllegalStateException is thrown.
   */
  public void negativeaddSRListenerClassTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "negativeaddSRListenerClassTest");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "SRListener");
    invoke();
  }

  /*
   * @testName: negativeaddSRListenerStringTest
   *
   * @assertion_ids: Servlet:JAVADOC:671.11;
   *
   * @test_Strategy: Create a Servlet, in which, a ServletRequestListener is
   * added; Verify in servlet that java.lang.IllegalStateException is thrown.
   */
  public void negativeaddSRListenerStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "negativeaddSRListenerStringTest");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "SRListener");
    invoke();
  }

  /*
   * @testName: negativeaddSCAListenerClassTest
   *
   * @assertion_ids: Servlet:JAVADOC:672.11; Servlet:JAVADOC:673.11;
   *
   * @test_Strategy: Create a Servlet, in which, a
   * ServletContextAttributeListener is added; Verify in servlet that
   * java.lang.IllegalStateException is thrown.
   */
  public void negativeaddSCAListenerClassTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "negativeaddSCAListenerClassTest");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "SRListener");
    invoke();
  }

  /*
   * @testName: negativeaddSCAListenerStringTest
   *
   * @assertion_ids: Servlet:JAVADOC:671.11;
   *
   * @test_Strategy: Create a Servlet, in which, a
   * ServletContextAttributeListener is added; Verify in servlet that
   * java.lang.IllegalStateException is thrown.
   */
  public void negativeaddSCAListenerStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "negativeaddSCAListenerStringTest");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "SCAttributeListener");
    invoke();
  }

  /*
   * @testName: negativeaddSCListenerClassTest
   *
   * @assertion_ids: Servlet:JAVADOC:672.11; Servlet:JAVADOC:673.11;
   *
   * @test_Strategy: Create a Servlet, in which, a ServletContextListener is
   * added; Verify in servlet that java.lang.IllegalStateException is thrown.
   */
  public void negativeaddSCListenerClassTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "negativeaddSCListenerClassTest");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "SCListener");
    invoke();
  }

  /*
   * @testName: negativeaddSCListenerStringTest
   *
   * @assertion_ids: Servlet:JAVADOC:671.11;
   *
   * @test_Strategy: Create a Servlet, in which, a ServletContextListener is
   * added; Verify in servlet that java.lang.IllegalStateException is thrown.
   */
  public void negativeaddSCListenerStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "negativeaddSCListenerStringTest");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "SCListener");
    invoke();
  }
}
