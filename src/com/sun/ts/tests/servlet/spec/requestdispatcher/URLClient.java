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
package com.sun.ts.tests.servlet.spec.requestdispatcher;

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

    setContextRoot("/servlet_spec_requestdispatcher_web");
    setServletName("TestServlet");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */
  /*
   * @testName: getRequestAttributes
   *
   * @assertion_ids: Servlet:SPEC:76.1; Servlet:SPEC:76.2; Servlet:SPEC:76.3;
   * Servlet:SPEC:76.4; Servlet:SPEC:76.5;
   *
   * @test_Strategy: 1. Create servlets TestServlet and IncludedServlet; 2. In
   * TestServlet, get RequestDispatcher by using
   * ServletContext.getRequestDispatcher(String), and access IncludedServlet
   * using RequestDispatcher.include. 3. Verify in IncludedServlet the following
   * request attributes set correctly required by Servlet 2.4 Spec:
   * javax.servlet.include.request_uri; javax.servlet.include.context_path;
   * javax.servlet.include.servlet_path; javax.servlet.include.path_info;
   * javax.servlet.include.query_string;
   */
  public void getRequestAttributes() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING,
        "javax.servlet.include.request_uri=SET_GOOD;javax.servlet.include.context_path=SET_GOOD;javax.servlet.include.servlet_path=SET_GOOD;javax.servlet.include.path_info=SET_NO;javax.servlet.include.query_string=SET_GOOD;");

    TEST_PROPS.setProperty(APITEST, "includeAttributes");
    invoke();
  }

  /*
   * @testName: getRequestAttributes1
   *
   * @assertion_ids: Servlet:SPEC:76.1; Servlet:SPEC:76.2; Servlet:SPEC:76.3;
   * Servlet:SPEC:76.4; Servlet:SPEC:76.5;
   *
   * @test_Strategy: 1. Create servlets TestServlet and IncludedServlet; 2. In
   * TestServlet, get RequestDispatcher by using
   * ServletRequest.getRequestDispatcher(String), and access IncludedServlet
   * using RequestDispatcher.include. 3. Verify in IncludedServlet the following
   * request attributes set correctly required by Servlet 2.4 Spec:
   * javax.servlet.include.request_uri; javax.servlet.include.context_path;
   * javax.servlet.include.servlet_path; javax.servlet.include.path_info;
   * javax.servlet.include.query_string;
   */
  public void getRequestAttributes1() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING,
        "javax.servlet.include.request_uri=SET_GOOD;javax.servlet.include.context_path=SET_GOOD;javax.servlet.include.servlet_path=SET_GOOD;javax.servlet.include.path_info=SET_NO;javax.servlet.include.query_string=SET_GOOD;");

    TEST_PROPS.setProperty(APITEST, "includeAttributes1");
    invoke();
  }

  /*
   * @testName: getRequestAttributes2
   *
   * @assertion_ids: Servlet:SPEC:76.1; Servlet:SPEC:76.2; Servlet:SPEC:76.3;
   * Servlet:SPEC:76.4; Servlet:SPEC:76.5;
   *
   * @test_Strategy: 1. Create servlets TestServlet and IncludedServlet; 2. In
   * TestServlet, get RequestDispatcher by using
   * ServletContext.getNamedDispatcher(String), and access IncludedServlet using
   * RequestDispatcher.include. 3. Verify in IncludedServlet the following
   * request attributes not set required by Servlet 2.4 Spec:
   * javax.servlet.include.request_uri; javax.servlet.include.context_path;
   * javax.servlet.include.servlet_path; javax.servlet.include.path_info;
   * javax.servlet.include.query_string;
   */
  public void getRequestAttributes2() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING,
        "javax.servlet.include.request_uri=SET_NO;javax.servlet.include.context_path=SET_NO;javax.servlet.include.servlet_path=SET_NO;javax.servlet.include.path_info=SET_NO;javax.servlet.include.query_string=SET_NO;");

    TEST_PROPS.setProperty(APITEST, "includeAttributes2");
    invoke();
  }

  /*
   * @testName: requestDispatcherIncludeIOAndServletExceptionTest
   * 
   * @assertion_ids: Servlet:SPEC:82; Servlet:JAVADOC:279; Servlet:JAVADOC:280;
   * 
   * @test_Strategy: Validate an exception thrown during a
   * RequestDispatcher.include() operation results in an IOException or
   * ServletException being thrown, the Servlet or IOException will be
   * propagated back to the caller and will not be wrapped by a
   * ServletException.
   */
  public void requestDispatcherIncludeIOAndServletExceptionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /servlet_spec_requestdispatcher_web/TestServlet?testname=includeIOAndServletException HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: requestDispatcherIncludeRuntimeExceptionTest
   * 
   * @assertion_ids: Servlet:SPEC:82
   * 
   * @test_Strategy: Validate a RuntimeException thrown during a
   * RequestDispacher.include() operation results in the RuntimeException being
   * propagated back to the caller and will not be wrapped by a
   * ServletException.
   */
  public void requestDispatcherIncludeRuntimeExceptionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /servlet_spec_requestdispatcher_web/TestServlet?testname=includeUnCheckedException HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: requestDispatcherIncludeCheckedExceptionTest
   * 
   * @assertion_ids: Servlet:SPEC:82
   * 
   * @test_Strategy: Validate a checked exception that is thrown during a
   * RequetDispatcher.include() operation and is not an instance of
   * ServletException or IOException is returned to the caller wrapped by
   * ServletException.
   */
  public void requestDispatcherIncludeCheckedExceptionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /servlet_spec_requestdispatcher_web/TestServlet?testname=includeCheckedException HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: requestDispatcherForwardIOAndServletExceptionTest
   * 
   * @assertion_ids: Servlet:SPEC:82; Servlet:JAVADOC:275; Servlet:JAVADOC:276;
   * 
   * @test_Strategy: Validate an exception thrown during a
   * RequestDispatcher.forward() operation results in an IOException or
   * ServletException being thrown, the Servlet or IOException will be
   * propagated back to the caller and will not be wrapped by a
   * ServletException.
   */
  public void requestDispatcherForwardIOAndServletExceptionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /servlet_spec_requestdispatcher_web/TestServlet?testname=forwardIOAndServletException HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: requestDispatcherForwardRuntimeExceptionTest
   * 
   * @assertion_ids: Servlet:SPEC:82
   * 
   * @test_Strategy: Validate a RuntimeException thrown during a
   * RequestDispacher.forward() operation results in the RuntimeException being
   * propagated back to the caller and will not be wrapped by a
   * ServletException.
   */
  public void requestDispatcherForwardRuntimeExceptionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /servlet_spec_requestdispatcher_web/TestServlet?testname=forwardUnCheckedException HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: requestDispatcherForwardCheckedExceptionTest
   * 
   * @assertion_ids: Servlet:SPEC:82
   * 
   * @test_Strategy: Validate a checked exception that is thrown during a
   * RequetDispatcher.forward() operation and is not an instance of
   * ServletException or IOException is returned to the caller wrapped by
   * ServletException.
   */
  public void requestDispatcherForwardCheckedExceptionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /servlet_spec_requestdispatcher_web/TestServlet?testname=forwardCheckedException HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: getRequestAttributes3
   *
   * @assertion_ids: Servlet:SPEC:180.1; Servlet:SPEC:180.2; Servlet:SPEC:180.3;
   * Servlet:SPEC:180.4; Servlet:SPEC:180.5;
   *
   * @test_Strategy: 1. Create servlets TestServlet and ForwardedServlet; 2. In
   * TestServlet, get RequestDispatcher by using
   * ServletContext.getRequestDispatcher(String), and access ForwardedServlet
   * using RequestDispatcher.forward. 3. Verify in ForwardedServlet the
   * following request attributes set correctly required by Servlet 2.4 Spec:
   * javax.servlet.forward.request_uri; javax.servlet.forward.context_path;
   * javax.servlet.forward.servlet_path; javax.servlet.forward.path_info;
   * javax.servlet.forward.query_string;
   */
  public void getRequestAttributes3() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING,
        "javax.servlet.forward.request_uri=SET_GOOD;"
            + "javax.servlet.forward.context_path=SET_GOOD;"
            + "javax.servlet.forward.servlet_path=SET_GOOD;"
            + "javax.servlet.forward.path_info=SET_NO;"
            + "javax.servlet.forward.query_string=SET_GOOD;");

    TEST_PROPS.setProperty(APITEST, "forwardAttributes");
    invoke();
  }

  /*
   * @testName: getRequestAttributes4
   *
   * @assertion_ids: Servlet:SPEC:180.1; Servlet:SPEC:180.2; Servlet:SPEC:180.3;
   * Servlet:SPEC:180.4; Servlet:SPEC:180.5;
   *
   * @test_Strategy: 1. Create servlets TestServlet and ForwardedServlet; 2. In
   * TestServlet, get RequestDispatcher by using
   * ServletRequest.getRequestDispatcher(String), and access ForwardedServlet
   * using RequestDispatcher.forward. 3. Verify in ForwardedServlet the
   * following request attributes set correctly required by Servlet 2.4 Spec:
   * javax.servlet.forward.request_uri; javax.servlet.forward.context_path;
   * javax.servlet.forward.servlet_path; javax.servlet.forward.path_info;
   * javax.servlet.forward.query_string;
   */
  public void getRequestAttributes4() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING,
        "javax.servlet.forward.request_uri=SET_GOOD;"
            + "javax.servlet.forward.context_path=SET_GOOD;"
            + "javax.servlet.forward.servlet_path=SET_GOOD;"
            + "javax.servlet.forward.path_info=SET_NO;"
            + "javax.servlet.forward.query_string=SET_GOOD;");

    TEST_PROPS.setProperty(APITEST, "forwardAttributes1");
    invoke();
  }

  /*
   * @testName: getRequestAttributes5
   *
   * @assertion_ids: Servlet:SPEC:181.1; Servlet:SPEC:181.2; Servlet:SPEC:181.3;
   * Servlet:SPEC:181.4; Servlet:SPEC:181.5;
   *
   * @test_Strategy: 1. Create servlets TestServlet and ForwardedServlet; 2. In
   * TestServlet, get RequestDispatcher by using
   * ServletContext.getNamedDispatcher(String), and access ForwardedServlet
   * using RequestDispatcher.forward. 3. Verify in ForwardedServlet the
   * following request attributes are not set required by Servlet 2.4 Spec:
   * javax.servlet.forward.request_uri; javax.servlet.forward.context_path;
   * javax.servlet.forward.servlet_path; javax.servlet.forward.path_info;
   * javax.servlet.forward.query_string;
   */
  public void getRequestAttributes5() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING,
        "javax.servlet.forward.request_uri=SET_NO;"
            + "javax.servlet.forward.context_path=SET_NO;"
            + "javax.servlet.forward.servlet_path=SET_NO;"
            + "javax.servlet.forward.path_info=SET_NO;"
            + "javax.servlet.forward.query_string=SET_NO;");

    TEST_PROPS.setProperty(APITEST, "forwardAttributes2");
    invoke();
  }

  /*
   * @testName: getRequestAttributes6
   *
   * @assertion_ids: Servlet:SPEC:181.1; Servlet:SPEC:181.2; Servlet:SPEC:181.3;
   * Servlet:SPEC:181.4; Servlet:SPEC:181.5;
   *
   * @test_Strategy: 1. Create servlets TestServlet, ForwardedServlet and
   * MultiForwardedServlet; 2. In TestServlet, get RequestDispatcher by using
   * ServletContext.getRequestDispatcher(String), and access
   * MultiForwardedServlet using RequestDispatcher.forward. 3. In
   * MultiForwardedServlet, get RequestDispatcher by using
   * ServletContext.getRequestDispatcher(String), and access ForwardedServlet
   * using RequestDispatcher.forward. 4. Verify in ForwardedServlet the
   * following request attributes are set required by Servlet 2.4 Spec:
   * javax.servlet.forward.request_uri; javax.servlet.forward.context_path;
   * javax.servlet.forward.servlet_path; javax.servlet.forward.path_info;
   * javax.servlet.forward.query_string;
   */
  public void getRequestAttributes6() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING,
        "javax.servlet.forward.request_uri=SET_GOOD;"
            + "javax.servlet.forward.context_path=SET_GOOD;"
            + "javax.servlet.forward.servlet_path=SET_GOOD;"
            + "javax.servlet.forward.path_info=SET_NO;"
            + "javax.servlet.forward.query_string=SET_GOOD;");

    TEST_PROPS.setProperty(APITEST, "forwardAttributes6");
    invoke();
  }

  /*
   * @testName: bufferContent
   *
   * @assertion_ids: Servlet:SPEC:77;
   *
   * @test_Strategy: 1. Create servlets TestServlet and ForwardedServlet; 2. In
   * TestServlet, first write "Test FAILED" to ServletResponse; then access
   * ForwardedServlet using RequestDispatcher.forward. 3. Verify that the
   * message "Test FAILED" wrote to ServletResponse is cleared and not sent to
   * Client as required by Servlet 2.4 Spec.
   */
  public void bufferContent() throws Fault {
    TEST_PROPS.setProperty(SEARCH_STRING,
        "bufferContent_in_ForwardedServlet_invoked");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(APITEST, "bufferContent");
    invoke();
  }

  /*
   * @testName: requestDispatcherNoWrappingTest
   * 
   * @assertion_ids: Servlet:SPEC:50;
   * 
   * @test_Strategy: Validate the container passes the same objects from a
   * RequestDispatcher operation to the target entity. The container should not
   * wrap the object at any point.
   */
  public void requestDispatcherNoWrappingTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /servlet_spec_requestdispatcher_web/TestServlet?testname=rdNoWrappingTest&operation=0 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /servlet_spec_requestdispatcher_web/TestServlet?testname=rdNoWrappingTest&operation=1 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: getRequestURIIncludeTest
   * 
   * @assertion_ids: Servlet:SPEC:76; Servlet:JAVADOC:561;
   * 
   * @test_Strategy: 1. Create servlets TestServlet and HttpTestServlet; 2. In
   * TestServlet;, access HttpTestServlet using RequestDispatcher.include. 3.
   * Verify in HttpTestServlet, that getRequestURI returns correct URI according
   * to 8.3
   */
  public void getRequestURIIncludeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestURIIncludeTest");
    invoke();
  }

  /*
   * @testName: getRequestURLIncludeTest
   * 
   * @assertion_ids: Servlet:SPEC:76; Servlet:JAVADOC:562;
   * 
   * @test_Strategy: 1. Create servlets TestServlet and HttpTestServlet; 2. In
   * TestServlet;, access HttpTestServlet using RequestDispatcher.include. 3.
   * Verify in HttpTestServlet, that getRequestURL returns correct URI according
   * to 8.3
   */
  public void getRequestURLIncludeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestURLIncludeTest");
    invoke();
  }

  /*
   * @testName: getRequestURIForwardTest
   * 
   * @assertion_ids: Servlet:SPEC:78;; Servlet:JAVADOC:561;
   * 
   * @test_Strategy: 1. Create servlets TestServlet and HttpTestServlet; 2. In
   * TestServlet;, access HttpTestServlet using RequestDispatcher.forward. 3.
   * Verify in HttpTestServlet, that getRequestURI returns correct URI according
   * to 8.4
   */
  public void getRequestURIForwardTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestURIForwardTest");
    invoke();
  }

  /*
   * @testName: getRequestURLForwardTest
   * 
   * @assertion_ids: Servlet:SPEC:78; Servlet:JAVADOC:562;
   * 
   * @test_Strategy: 1. Create servlets TestServlet and HttpTestServlet; 2. In
   * TestServlet;, access HttpTestServlet using RequestDispatcher.forward. 3.
   * Verify in HttpTestServlet, that getRequestURL returns correct URL according
   * to 8.4
   */
  public void getRequestURLForwardTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestURLForwardTest");
    invoke();
  }

  /*
   * @testName: getQueryStringIncludeTest
   * 
   * @assertion_ids: Servlet:SPEC:192; Servlet:JAVADOC:552;
   * 
   * @test_Strategy: 1. Create servlets TestServlet and HttpTestServlet; 2. Send
   * request to TestServlet with ?testname=getQueryStringIncludeTest; 3. In
   * TestServlet, access HttpTestServlet using RequestDispatcher.include, with
   * ?testname=getQueryStringTestInclude; 4. Verify in HttpTestServlet, that
   * getQueryString returns correct QueryString
   * testname=getQueryStringIncludeTest according to 8.3
   */
  public void getQueryStringIncludeTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getQueryStringIncludeTest");
    invoke();
  }

  /*
   * @testName: getQueryStringForwardTest
   * 
   * @assertion_ids: Servlet:SPEC:78; Servlet:JAVADOC:552;
   * 
   * @test_Strategy: 1. Create servlets TestServlet and HttpTestServlet; 2. Send
   * request to TestServlet with ?testname=getQueryStringForwardTest; 3. In
   * TestServlet, access HttpTestServlet using RequestDispatcher.forward, with
   * ?testname=getQueryStringTestForward; 4. Verify in HttpTestServlet, that
   * getQueryString returns correct QueryString
   * testname=getQueryStringTestForward according to 8.4.2
   */
  public void getQueryStringForwardTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getQueryStringForwardTest");
    invoke();
  }
}
