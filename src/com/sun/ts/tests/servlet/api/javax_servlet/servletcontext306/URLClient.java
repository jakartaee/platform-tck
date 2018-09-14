/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.servlet.api.javax_servlet.servletcontext306;

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

    setContextRoot("/servlet_js_servletcontext306_web");
    setServletName("TestServlet");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /* Run test */

  /*
   * @testName: addServletStringTest
   *
   * @assertion_ids: Servlet:JAVADOC:674.6;
   *
   * @test_Strategy: Negative test for ServletContext.addServlet(String, String)
   * Create a Servlet, call ServletContext.addServlet(String, String) Verify the
   * expected IllegalStateException is thrown.
   */
  public void addServletStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "addServletStringTest");
    invoke();
  }

  /*
   * @testName: addServletClassTest
   *
   * @assertion_ids: Servlet:JAVADOC:676.6;
   *
   * @test_Strategy: Negative test for ServletContext.addServlet(String, Class)
   * Create a Servlet, call ServletContext.addServlet(String, Class) Verify the
   * expected IllegalStateException is thrown.
   */
  public void addServletClassTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "addServletClassTest");
    invoke();
  }

  /*
   * @testName: addServletTest
   *
   * @assertion_ids: Servlet:JAVADOC:675.7;
   *
   * @test_Strategy: Negative test for ServletContext.addServlet(String,
   * Servlet) Create a Servlet, call ServletContext.addServlet(String, Servlet)
   * Verify the expected IllegalStateException is thrown.
   */
  public void addServletTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "addServletTest");
    invoke();
  }

  /*
   * @testName: addFilterStringTest
   *
   * @assertion_ids: Servlet:JAVADOC:668.5;
   *
   * @test_Strategy: Negative test for ServletContext.addFilter(String, String)
   * Create a Servlet, call ServletContext.addFilter(String, String) Verify the
   * expected IllegalStateException is thrown.
   */
  public void addFilterStringTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "addFilterStringTest");
    invoke();
  }

  /*
   * @testName: addFilterClassTest
   *
   * @assertion_ids: Servlet:JAVADOC:670.5;
   *
   * @test_Strategy: Negative test for ServletContext.addFilter(String, Class)
   * Create a Servlet, call ServletContext.addFilter(String, Class) Verify the
   * expected IllegalStateException is thrown.
   */
  public void addFilterClassTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "addFilterClassTest");
    invoke();
  }

  /*
   * @testName: addFilterTest
   *
   * @assertion_ids: Servlet:JAVADOC:669.6;
   *
   * @test_Strategy: Negative test for ServletContext.addFilter(String, Filter)
   * Create a Servlet, call ServletContext.addFilter(String, Filter) Verify the
   * expected IllegalStateException is thrown.
   */
  public void addFilterTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "addFilterTest");
    invoke();
  }

  /*
   * @testName: setInitParameterTest
   *
   * @assertion_ids: Servlet:JAVADOC:694.1;
   *
   * @test_Strategy: Negative test for ServletContext.setInitParameter(String,
   * String) Create a Servlet, call ServletContext.setInitParameter(String,
   * String) Verify the expected IllegalStateException is thrown.
   */
  public void setInitParameterTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "setInitParameterTest");
    invoke();
  }
}
