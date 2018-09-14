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
package com.sun.ts.tests.servlet.pluggability.api.javax_servlet_http.httpservlet;

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
    setContextRoot("/servlet_pluh_httpservlet_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: destroyTest
   * 
   * @assertion_ids: Servlet:JAVADOC:416
   * 
   * @test_Strategy: Create a GenericServlet and take out of service using
   * destroy method
   *
   */
  public void destroyTest() throws Fault {
    String testName = "destroyTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + testName + " HTTP/1.1");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    invoke();
  }

  /*
   * @testName: getServletConfigTest
   * 
   * @assertion_ids: Servlet:JAVADOC:421
   * 
   * @test_Strategy: Create a GenericServlet and check for its ServletConfig
   * object existence
   *
   */
  public void getServletConfigTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getServletConfigTest");
    invoke();
  }

  /*
   * @testName: getServletContextTest
   * 
   * @assertion_ids: Servlet:JAVADOC:422
   * 
   * @test_Strategy: Create a GenericServlet and check for its ServletContext
   * object existence
   *
   */
  public void getServletContextTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getServletContextTest");
    invoke();
  }

  /*
   * @testName: getServletInfoTest
   * 
   * @assertion_ids: Servlet:JAVADOC:423
   * 
   * @test_Strategy: Create a GenericServlet and check for its ServletInfo
   * object values
   *
   */
  public void getServletInfoTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getServletInfoTest");
    invoke();
  }

  /*
   * @testName: getInitParameterTest
   * 
   * @assertion_ids: Servlet:JAVADOC:417
   * 
   * @test_Strategy: Servlet tries to access a parameter that exists
   */
  public void getInitParameterTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getInitParameterTest");
    invoke();
  }

  /*
   * @testName: getInitParameterTestNull
   * 
   * @assertion_ids: Servlet:JAVADOC:418
   * 
   * @test_Strategy: Servlet tries to access a parameter that doesnot exist
   */
  public void getInitParameterTestNull() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getInitParameterTestNull");
    invoke();
  }

  /*
   * @testName: getInitParameterNamesTest
   * 
   * @assertion_ids: Servlet:JAVADOC:419
   * 
   * @test_Strategy: Servlet tries to get all parameter names
   */
  public void getInitParameterNamesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getInitParameterNamesTest");
    invoke();
  }

  /*
   * @testName: getServletNameTest
   * 
   * @assertion_ids: Servlet:JAVADOC:433
   * 
   * @test_Strategy: Servlet gets name of servlet
   */
  public void getServletNameTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getServletNameTest");
    invoke();
  }

  /*
   * @testName: serviceTest
   * 
   * @assertion_ids: Servlet:JAVADOC:430
   * 
   * @test_Strategy: Servlet which has a service method that is called
   */
  public void serviceTest() throws Fault {
    String testName = "serviceTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + testName + " HTTP/1.1");
    invoke();
  }

  /*
   * @testName: initTest
   * 
   * @assertion_ids: Servlet:JAVADOC:426
   * 
   * @test_Strategy: Servlet has init method that puts a value into the context.
   * Servlet when called reads value from context
   */
  public void initTest() throws Fault {
    String testName = "initTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + testName + " HTTP/1.1");
    invoke();
  }

  /*
   * @testName: init_ServletConfigTest
   * 
   * @assertion_ids: Servlet:JAVADOC:424
   * 
   * @test_Strategy: Servlet has init method that puts a value into the context.
   * Servlet when called reads value from context
   */
  public void init_ServletConfigTest() throws Fault {
    String testName = "init_ServletConfigTest";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + testName + " HTTP/1.1");
    invoke();
  }
}
