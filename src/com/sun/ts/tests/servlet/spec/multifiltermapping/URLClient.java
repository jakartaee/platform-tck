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

package com.sun.ts.tests.servlet.spec.multifiltermapping;

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
  @Override
  public Status run(String[] args, PrintWriter out, PrintWriter err) {
    setContextRoot("/servlet_spec_multifiltermapping_web");
    setServletName("TestServlet");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /* Run test */
  /*
   * @testName: requestTest
   *
   * @assertion_ids: Servlet:SPEC:54; Servlet:SPEC:59;
   *
   * @test_Strategy: 1. Create a web page - /dummy.html. 2. Create servlets
   * TestServlet1 with URL /foo/bar/*, TestServlet2 with URL /foo/baR/*,
   * TestServlet3 with URL /TestServlet3, TestServlet4 with URL *.bop,
   * TestServlet5 with URL /foo/bar/TestServlet5, TestServlet6 with servlet-name
   * TestServlet6, 2. Map a filter Test_RequestFilter on all above one web page
   * and 6 servlets with dispatcher value set to REQUEST using url-pattern and
   * servlet name respectively. 3. Client try to access all of them directly. 4.
   * Verify that Test_RequestFilter is properly invoked.
   */
  public void requestTest() throws Fault {
    TEST_PROPS.setProperty(DONOTUSEServletName, "true");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_RequestFilter|TestServlet1");
    TEST_PROPS.setProperty(APITEST, "foo/bar/index.html");
    invoke();

    TEST_PROPS.setProperty(DONOTUSEServletName, "true");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_RequestFilter|TestServlet1");
    TEST_PROPS.setProperty(APITEST, "foo/bar");
    invoke();

    TEST_PROPS.setProperty(APITEST, "foo/baR/TestServlet5");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_RequestFilter|TestServlet2");
    TEST_PROPS.setProperty(DONOTUSEServletName, "true");
    invoke();

    TEST_PROPS.setProperty(APITEST, "TestServlet3");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_RequestFilter|TestServlet3");
    TEST_PROPS.setProperty(DONOTUSEServletName, "true");
    invoke();

    TEST_PROPS.setProperty(APITEST, "foo/bar/TestServlet5");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_RequestFilter|TestServlet5");
    TEST_PROPS.setProperty(DONOTUSEServletName, "true");
    invoke();

    TEST_PROPS.setProperty(APITEST, "test/servletbyname");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_RequestFilter|TestServlet6");
    TEST_PROPS.setProperty(DONOTUSEServletName, "true");
    invoke();

    TEST_PROPS.setProperty(APITEST, "dummy.html");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test_RequestFilter|Test FAILED from dummy html page");
    TEST_PROPS.setProperty(DONOTUSEServletName, "true");
    invoke();

    TEST_PROPS.setProperty(APITEST, "index.bop");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_RequestFilter|TestServlet4");
    TEST_PROPS.setProperty(DONOTUSEServletName, "true");
    invoke();

    TEST_PROPS.setProperty(APITEST, "TestServlet3/racecar.bop");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_RequestFilter|TestServlet4");
    TEST_PROPS.setProperty(DONOTUSEServletName, "true");
    invoke();
  }

  /*
   * @testName: forwardTest
   *
   * @assertion_ids: Servlet:SPEC:54;
   *
   * @test_Strategy: 1. Create a web page - /dummy.html. 2. Create servlets
   * TestServlet1 with URL /foo/bar/*, TestServlet2 with URL /foo/baR/*,
   * TestServlet3 with URL /TestServlet3, TestServlet4 with URL *.bop,
   * TestServlet5 with URL /foo/bar/TestServlet5, TestServlet6 with servlet-name
   * TestServlet6; 3. Map a filter Test_ForwardFilter on all above one web page
   * and 6 servlets with dispatcher value set to FORWARD using url-pattern and
   * servlet name respectively. 4. Create a servlet TestServlet. 5. Client send
   * request to TestServlet, which access all resources list in 1 and 2 using
   * forward. 6. Verify that the filter is invoked properly
   */
  public void forwardTest() throws Fault {
    String testName = "forwardTest";

    TEST_PROPS.setProperty(SEARCH_STRING, "Test_ForwardFilter|TestServlet1");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/foo/bar/index.html HTTP/1.1");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/foo/bar HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_ForwardFilter|TestServlet1");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/foo/baR/TestServlet5 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_ForwardFilter|TestServlet2");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/TestServlet3 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_ForwardFilter|TestServlet3");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/foo/bar/TestServlet5 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_ForwardFilter|TestServlet5");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/test/servletbyname HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_ForwardFilter|TestServlet6");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/dummy.html HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test_ForwardFilter|Test FAILED from dummy html page");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/index.bop HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_ForwardFilter|TestServlet4");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/TestServlet3/racecar.bop HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_ForwardFilter|TestServlet4");
    invoke();
  }

  /*
   * @testName: includeTest
   *
   * @assertion_ids: Servlet:SPEC:54;
   *
   * @test_Strategy: 1. Create a web page - /dummy.html. 2. Create servlets
   * TestServlet1 with URL /foo/bar/*, TestServlet2 with URL /foo/baR/*,
   * TestServlet3 with URL /TestServlet3, TestServlet4 with URL *.bop,
   * TestServlet5 with URL /foo/bar/TestServlet5, TestServlet6 with servlet-name
   * TestServlet6; 3. Map a filter Test_IncludeFilter on all above one web page
   * and 6 servlets with dispatcher value set to INCLUDE using url-pattern and
   * servlet name respectively. 4. Create a servlet TestServlet. 5. Client send
   * request to TestServlet, which access all resources list in 1 and 2 using
   * include. 6. Verify that the filter is invoked properly
   */
  public void includeTest() throws Fault {
    String testName = "includeTest";
    String filterString = "Test_IncludeFilter";

    TEST_PROPS.setProperty(SEARCH_STRING, filterString + "|TestServlet1");
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/foo/bar/index.html HTTP/1.1");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/foo/bar HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, filterString + "|TestServlet1");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/foo/baR/TestServlet5 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, filterString + "|TestServlet2");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/TestServlet3 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, filterString + "|TestServlet3");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/foo/bar/TestServlet5 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, filterString + "|TestServlet5");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/test/servletbyname HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, filterString + "|TestServlet6");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/dummy.html HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        filterString + "|Test FAILED from dummy html page");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/index.bop HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, filterString + "|TestServlet4");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName() + "?testname="
            + testName + "&parameter1=/TestServlet3/racecar.bop HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, filterString + "|TestServlet4");
    invoke();
  }

  /*
   * @testName: errorTest
   *
   * @assertion_ids: Servlet:SPEC:57; Servlet:SPEC:59;
   *
   * @test_Strategy: 1. Create a web page - /dummy.html. 2. Create servlets
   * TestServlet1 with URL /foo/bar/*, TestServlet2 with URL /foo/baR/*,
   * TestServlet3 with URL /TestServlet3, TestServlet4 with URL *.bop,
   * TestServlet5 with URL /foo/bar/TestServlet5, TestServlet6 with servlet-name
   * TestServlet6, 3. Map a filter Test_RequestFilter on all above one web page
   * and six servlets with dispatcher value set to REQUEST, using url-pattern
   * and servlet name respectively. 4. Map a filter Test_ForwardFilter on all
   * above one web page and six servlets with dispatcher value set to FORWARD
   * using url-pattern and servlet name respectively. 5. Map a filter
   * Test_ErrorFilter to an servlet ErrorPage with dispatcher value set to ERROR
   * 6. access a resource that dose not match any resource listed in 1 and 2, 7.
   * verify that Test_ErrorFilter is properly invoked 8. Create a servlet
   * TestServlet. 9. Client send request to TestServlet, which access a resource
   * that dose not match any resource listed in 1 and 2 using include and
   * forward respectively. 10. Verify that Test_ErrorFilter is properly invoked.
   */
  public void errorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "test/foo/bar/xyz");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_ErrorFilter|ErrorPage");
    TEST_PROPS.setProperty(DONOTUSEServletName, "true");
    TEST_PROPS.setProperty(STATUS_CODE, NOT_FOUND);
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/" + getServletName()
            + "?testname=forwardTest"
            + "&parameter1=/test/foo/bar/xyz HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test_ErrorFilter|ErrorPage");
    TEST_PROPS.setProperty(STATUS_CODE, NOT_FOUND);
    invoke();
  }
}
