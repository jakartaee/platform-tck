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
package com.sun.ts.tests.servlet.pluggability.api.javax_servlet.servletexception;

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

    setContextRoot("/servlet_plu_servletexception_web");
    setServletName("TestServlet");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: getRootCauseTest
   *
   * @assertion_ids: Servlet:SPEC:83; Servlet:JAVADOC:108; Servlet:JAVADOC:109;
   * Servlet:JAVADOC:8;
   *
   * @test_Strategy: A Test for getRootCause method
   */
  public void getRootCauseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRootCause");
    invoke();
  }

  /*
   * @testName: servletExceptionConstructor1Test
   *
   * @assertion_ids: Servlet:SPEC:83; Servlet:JAVADOC:105;
   *
   * @test_Strategy: A Test for ServletException() constructor method
   */
  public void servletExceptionConstructor1Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "servletExceptionConstructor1");
    invoke();
  }

  /*
   * @testName: servletExceptionConstructor2Test
   *
   * @assertion_ids: Servlet:SPEC:83; Servlet:JAVADOC:106;
   *
   * @test_Strategy: A Test for ServletException(String) constructor method
   */
  public void servletExceptionConstructor2Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "servletExceptionConstructor2");
    invoke();
  }

  /*
   * @testName: servletExceptionConstructor3Test
   *
   * @assertion_ids: Servlet:SPEC:83; Servlet:JAVADOC:108; Servlet:JAVADOC:109;
   * Servlet:JAVADOC:8;
   *
   * @test_Strategy: A Test for ServletException(Throwable) constructor method
   */
  public void servletExceptionConstructor3Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "servletExceptionConstructor3");
    invoke();

  }

  /*
   * @testName: servletExceptionConstructor4Test
   *
   * @assertion_ids: Servlet:SPEC:83; Servlet:JAVADOC:107; Servlet:JAVADOC:109;
   * Servlet:JAVADOC:8;
   *
   * @test_Strategy: A Test for ServletException(String,Throwable) constructor
   * method
   */
  public void servletExceptionConstructor4Test() throws Fault {
    TEST_PROPS.setProperty(APITEST, "servletExceptionConstructor4");
    invoke();
  }
}
