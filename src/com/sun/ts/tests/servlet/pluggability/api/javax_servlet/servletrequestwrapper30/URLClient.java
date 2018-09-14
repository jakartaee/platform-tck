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
package com.sun.ts.tests.servlet.pluggability.api.javax_servlet.servletrequestwrapper30;

import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import java.io.PrintWriter;

public class URLClient extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/servlet_plu_servletrequestwrapper30_web";

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
    setContextRoot(CONTEXT_ROOT);

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */
  /*
   * @testName: getServletContextTest
   *
   * @assertion_ids: Servlet:JAVADOC:721;
   *
   * @test_Strategy: Create a ServletRequestWrapper that wrap another servlet;
   * calls ServletRequestWrapper.getServletContext(); verifies that returned
   * ServletContext instance is consistent with the one stored in
   * ServletConfigs.
   */
  public void getServletContextTest() throws Fault {
    setServletName("TestServletWrapper");
    TEST_PROPS.setProperty(APITEST, "getServletContextTest");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "TEST FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: getDispatcherTypeTestRequest
   *
   * @assertion_ids: Servlet:JAVADOC:718;
   *
   * @test_Strategy: Create a ServletRequestWrapper that wraps TestServlet;
   * Client send a request the ServletRequestWrapper; call
   * ServletRequestWrapper.getDispatcherType() verifies that
   * DispatcherType.REQUEST is returned.
   */
  public void getDispatcherTypeTestRequest() throws Fault {
    setServletName("TestServletWrapper");
    TEST_PROPS.setProperty(APITEST, "getDispatcherTypeTestRequest");
    TEST_PROPS.setProperty(SEARCH_STRING, "DispatcherType=REQUEST");
    invoke();
  }

  /*
   * @testName: getDispatcherTypeTestForward
   *
   * @assertion_ids: Servlet:JAVADOC:718;
   *
   * @test_Strategy: Create a ServletRequestWrapper that wraps TestServlet;
   * Client send a request the ServletRequestWrapper; Forward to SecondServlet
   * call ServletRequestWrapper.getDispatcherType() verifies that
   * DispatcherType.FORWARD is returned.
   */
  public void getDispatcherTypeTestForward() throws Fault {
    setServletName("TestServletWrapper");
    TEST_PROPS.setProperty(APITEST, "getDispatcherTypeTestForward");
    TEST_PROPS.setProperty(SEARCH_STRING, "DispatcherType=FORWARD");
    invoke();
  }

  /*
   * @testName: getDispatcherTypeTestInclude
   *
   * @assertion_ids: Servlet:JAVADOC:718;
   *
   * @test_Strategy: Create a ServletRequestWrapper that wraps TestServlet;
   * Client send a request the ServletRequestWrapper; Include to SecondServlet
   * call ServletRequestWrapper.getDispatcherType() verifies that
   * DispatcherType.INCLUDE is returned.
   */
  public void getDispatcherTypeTestInclude() throws Fault {
    setServletName("TestServletWrapper");
    TEST_PROPS.setProperty(APITEST, "getDispatcherTypeTestInclude");
    TEST_PROPS.setProperty(SEARCH_STRING, "DispatcherType=INCLUDE");
    invoke();
  }

  /*
   * @testName: getDispatcherTypeTestError
   *
   * @assertion_ids: Servlet:JAVADOC:718;
   *
   * @test_Strategy: Create a ServletRequestWrapper that wraps SecondServlet;
   * Maps 404 to the ServletRequestWrapper; Client send a request to
   * nowhereland; In SecondServlet, call
   * ServletRequestWrapper.getDispatcherType() verifies that
   * DispatcherType.ERROR is returned.
   */
  public void getDispatcherTypeTestError() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + getContextRoot() + "/nowheretobefound/  HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "DispatcherType=ERROR");
    TEST_PROPS.setProperty(STATUS_CODE, NOT_FOUND);
    invoke();
  }

  /*
   * @testName: getDispatcherTypeTestAsync
   *
   * @assertion_ids: Servlet:JAVADOC:640; Servlet:JAVADOC:703;
   * Servlet:JAVADOC:710;
   *
   * @test_Strategy: Create three Servlets AsyncTestServletWrapper,
   * AsyncTestServlet and AsyncTests, all support async; AsyncTestServletWrapper
   * wraps AsyncTestServlet; Client send a request to AsyncTestServletWrapper;
   * StartAsync in AsyncTestServlet; Invoke AsyncTests using
   * AsyncContext.dispatch(String) call
   * ServletRequestWrapper.getDispatcherType() verifies that
   * DispatcherType.ASYNC is returned.
   */
  public void getDispatcherTypeTestAsync() throws Fault {
    setServletName("AsyncTestServletWrapper");
    TEST_PROPS.setProperty(APITEST, "getDispatcherTypeTestAsync");
    TEST_PROPS.setProperty(SEARCH_STRING, "DispatcherType=ASYNC");
    invoke();
  }

  /*
   * @testName: asyncStartedTest1
   *
   * @assertion_ids: Servlet:JAVADOC:722; Servlet:JAVADOC:727;
   *
   * @test_Strategy: Create three Servlets AsyncTestServletWrapper,
   * AsyncTestServlet and AsyncTests, all support async; AsyncTestServletWrapper
   * wraps AsyncTestServlet; Client send a request to AsyncTestServletWrapper;
   * StartAsync in AsyncTestServlet ServletRequestWrapper.startAsync(); call
   * ServletRequestWrapper.isAsyncStarted() verifies that true is returned.
   */
  public void asyncStartedTest1() throws Fault {
    setServletName("AsyncTestServletWrapper");
    TEST_PROPS.setProperty(APITEST, "asyncStartedTest1");
    TEST_PROPS.setProperty(SEARCH_STRING, "IsAsyncStarted=true");
    invoke();
  }

  /*
   * @testName: asyncStartedTest2
   *
   * @assertion_ids: Servlet:JAVADOC:722;
   *
   * @test_Strategy: Create three Servlets AsyncTestServletWrapper,
   * AsyncTestServlet and AsyncTests, all support async; AsyncTestServletWrapper
   * wraps AsyncTestServlet; Client send a request to AsyncTestServletWrapper;
   * call ServletRequestWrapper.isAsyncStarted() without start async mode
   * verifies that false is returned.
   */
  public void asyncStartedTest2() throws Fault {
    setServletName("AsyncTestServletWrapper");
    TEST_PROPS.setProperty(APITEST, "asyncStartedTest2");
    TEST_PROPS.setProperty(SEARCH_STRING, "IsAsyncStarted=false");
    invoke();
  }

  /*
   * @testName: asyncStartedTest3
   *
   * @assertion_ids: Servlet:JAVADOC:638; Servlet:JAVADOC:722;
   * Servlet:JAVADOC:727;
   *
   * @test_Strategy: Create three Servlets AsyncTestServletWrapper,
   * AsyncTestServlet and AsyncTests, all support async; AsyncTestServletWrapper
   * wraps AsyncTestServlet; Client send a request to AsyncTestServletWrapper;
   * StartAsync in AsyncTestServlet ServletRequestWrapper.startAsync(); call
   * ServletRequestWrapper.isAsyncStarted() after AsyncContext.complete() called
   * verifies that true is returned before it dispatch return to the container
   */
  public void asyncStartedTest3() throws Fault {
    setServletName("AsyncTestServletWrapper");
    TEST_PROPS.setProperty(APITEST, "asyncStartedTest3");
    TEST_PROPS.setProperty(SEARCH_STRING, "IsAsyncStarted=true");
    invoke();
  }

  /*
   * @testName: asyncStartedTest4
   *
   * @assertion_ids: Servlet:JAVADOC:640; Servlet:JAVADOC:722;
   * Servlet:JAVADOC:727;
   *
   * @test_Strategy: Create three Servlets AsyncTestServletWrapper,
   * AsyncTestServlet and AsyncTests, all support async; AsyncTestServletWrapper
   * wraps AsyncTestServlet; Client send a request to AsyncTestServletWrapper;
   * StartAsync in AsyncTestServlet ServletRequestWrapper.startAsync(); Invoke
   * AsyncTests using AsyncContext.dispatch(String) call
   * ServletRequestWrapper.isAsyncStarted() verifies that false is returned.
   */
  public void asyncStartedTest4() throws Fault {
    setServletName("AsyncTestServletWrapper");
    TEST_PROPS.setProperty(APITEST, "asyncStartedTest4");
    TEST_PROPS.setProperty(SEARCH_STRING, "IsAsyncStarted=false");
    invoke();
  }

  /*
   * @testName: isAsyncSupportedTest1
   *
   * @assertion_ids: Servlet:JAVADOC:723;
   *
   * @test_Strategy: Create two Servlets AsyncTestServletWrapper,
   * AsyncTestServlet all support async; AsyncTestServletWrapper wraps
   * AsyncTestServlet; Client send a request to AsyncTestServletWrapper; call
   * ServletRequestWrapper.isAsyncSupported() verifies that true is returned.
   */
  public void isAsyncSupportedTest1() throws Fault {
    setServletName("AsyncTestServletWrapper");
    TEST_PROPS.setProperty(APITEST, "isAsyncSupportedTest");
    TEST_PROPS.setProperty(SEARCH_STRING, "isAsyncSupported=true");
    invoke();
  }

  /*
   * @testName: isAsyncSupportedTest2
   *
   * @assertion_ids: Servlet:JAVADOC:723;
   *
   * @test_Strategy: Create two Servlets TestServletWrapper, TestServlet all
   * does not support async; TestServletWrapper wraps TestServlet; Client send a
   * request to TestServletWrapper; call
   * ServletRequestWrapper.isAsyncSupported() verifies that false is returned.
   */
  public void isAsyncSupportedTest2() throws Fault {
    setServletName("TestServletWrapper");
    TEST_PROPS.setProperty(APITEST, "isAsyncSupportedTest");
    TEST_PROPS.setProperty(SEARCH_STRING, "isAsyncSupported=false");
    invoke();
  }

  /*
   * @testName: startAsyncTest1
   *
   * @assertion_ids: Servlet:JAVADOC:728;
   *
   * @test_Strategy: Create two Servlets TestServletWrapper, TestServlet all
   * does not support async; TestServletWrapper wraps TestServlet; Client send a
   * request to TestServletWrapper; call ServletRequestWrapper.startAsyncTest()
   * verifies that IllegalStateException is thrown.
   */
  public void startAsyncTest1() throws Fault {
    setServletName("TestServletWrapper");
    TEST_PROPS.setProperty(APITEST, "startAsyncTest");
    invoke();
  }

  /*
   * @testName: startAsyncTest2
   *
   * @assertion_ids: Servlet:JAVADOC:640; Servlet:JAVADOC:722;
   * Servlet:JAVADOC:727; Servlet:JAVADOC:728;
   *
   * @test_Strategy: Create three Servlets AsyncTestServletWrapper,
   * AsyncTestServlet, AsyncTest all support async; AsyncTestServletWrapper
   * wraps AsyncTestServlet; Client send a request to AsyncTestServletWrapper;
   * Start Async in AsyncTestServlet; Invoke AsyncTests using
   * AsyncContext.dispatch(String) Call ServletRequestWrapper.startAsyncTest()
   * outside of dispatch verifies that IllegalStateException is thrown.
   */
  public void startAsyncTest2() throws Fault {
    setServletName("AsyncTestServletWrapper");
    TEST_PROPS.setProperty(APITEST, "startAsyncTest");
    TEST_PROPS.setProperty(SEARCH_STRING, "IllegalStateException thrown");
    invoke();
  }

  /*
   * @testName: getAsyncContextTest
   *
   * @assertion_ids: Servlet:JAVADOC:638; Servlet:JAVADOC:716;
   * Servlet:JAVADOC:727;
   *
   * @test_Strategy: Create two Servlets AsyncTestServletWrapper,
   * AsyncTestServlet all support async; AsyncTestServletWrapper wraps
   * AsyncTestServlet; Client send a request to AsyncTestServletWrapper; Start
   * Async in AsyncTestServlet; call ServletRequestWrapper.getAsyncContext()
   * verifies it works.
   */
  public void getAsyncContextTest() throws Fault {
    setServletName("AsyncTestServletWrapper");
    TEST_PROPS.setProperty(APITEST, "getAsyncContextTest");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
