/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.servlet.api.javax_servlet.asynccontext;

import com.sun.javatest.Status;
import com.sun.ts.tests.servlet.common.client.AbstractUrlClient;
import java.io.PrintWriter;

public class URLClient extends AbstractUrlClient {

  String CONTEXT_ROOT = "/servlet_js_asynccontext_web";

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
    setServletName("AsyncTestServlet");
    setContextRoot(CONTEXT_ROOT);

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run test */
  /*
   * Test set up:
   *
   * Define two servets AsyncTestServlet and AsyncTests that supports async in
   * web.xml; Define three AsyncListeners using annotation: ACListener,
   * ACListener1, ACListenerBad; - ACListener1 does not complete properly -
   * ACListenerBad does not instantiate; Define a ServletRequestWrapper that
   * wraps the original request; Define a ServletResponseWrapper that wraps the
   * original response;
   *
   */

  /*
   * @testName: dispatchZeroArgTest
   * 
   * @assertion_ids: Servlet:JAVADOC:639; Servlet:JAVADOC:639.2;
   * Servlet:JAVADOC:639.4; Servlet:JAVADOC:703; Servlet:JAVADOC:707;
   * Servlet:JAVADOC:708; Servlet:JAVADOC:710;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which supports async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(); call ac.dispatch(); call
   * ServletRequest.isAsyncSupported() call ServletRequest.isAsyncStarted() call
   * ServletRequest.getDispatcherType() verifies all work accordingly.
   */
  public void dispatchZeroArgTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dispatchZeroArgTest");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "ASYNC_NOT_STARTED_dispatchZeroArgTest|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=REQUEST|"
            + "ASYNC_STARTED_dispatchZeroArgTest|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=ASYNC");
    invoke();
  }

  /*
   * @testName: dispatchZeroArgTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:639; Servlet:JAVADOC:639.2;
   * Servlet:JAVADOC:639.4; Servlet:JAVADOC:703; Servlet:JAVADOC:707;
   * Servlet:JAVADOC:708; Servlet:JAVADOC:710;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which supports async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(ServletRequest, ServletResponse); call
   * ac.dispatch(); call ServletRequest.isAsyncSupported() call
   * ServletRequest.isAsyncStarted() call ServletRequest.getDispatcherType()
   * verifies all work accordingly.
   */
  public void dispatchZeroArgTest1() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dispatchZeroArgTest1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "ASYNC_NOT_STARTED_dispatchZeroArgTest1|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=REQUEST|"
            + "ASYNC_STARTED_dispatchZeroArgTest1|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=ASYNC");
    invoke();
  }

  /*
   * @testName: dispatchZeroArgTest2
   * 
   * @assertion_ids: Servlet:JAVADOC:639; Servlet:JAVADOC:639.2;
   * Servlet:JAVADOC:639.4; Servlet:JAVADOC:703; Servlet:JAVADOC:707;
   * Servlet:JAVADOC:708; Servlet:JAVADOC:710;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which supports async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(ServletRequestWrapper, ServletResponseWrapper);
   * call ac.dispatch(); call ServletRequest.isAsyncSupported() call
   * ServletRequest.isAsyncStarted() call ServletRequest.getDispatcherType()
   * verifies all work accordingly.
   */
  public void dispatchZeroArgTest2() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dispatchZeroArgTest2");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "ASYNC_NOT_STARTED_dispatchZeroArgTest2|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=REQUEST|"
            + "ASYNC_STARTED_dispatchZeroArgTest2|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=ASYNC");
    invoke();
  }

  /*
   * @testName: dispatchContextPathTest
   * 
   * @assertion_ids: Servlet:JAVADOC:641; Servlet:JAVADOC:703;
   * Servlet:JAVADOC:707; Servlet:JAVADOC:708; Servlet:JAVADOC:710;
   *
   * @test_Strategy: Create two Servlets AsyncTestServlet and AsynTest both
   * support async; Client send a request to AsyncTestServlet; StartAsync in
   * AsyncTestServlet ServletRequest.startAsync(); call
   * ac.dispatch(ServletContext, path to AsynTest); call
   * ServletRequest.isAsyncSupported() call ServletRequest.isAsyncStarted() call
   * ServletRequest.getDispatcherType() verifies all work accordingly.
   */
  public void dispatchContextPathTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dispatchContextPathTest");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "ASYNC_NOT_STARTED_dispatchContextPathTest|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=REQUEST|"
            + "ASYNC_STARTED_asyncTest|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=ASYNC");
    invoke();
  }

  /*
   * @testName: dispatchContextPathTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:641; Servlet:JAVADOC:703;
   * Servlet:JAVADOC:707; Servlet:JAVADOC:708; Servlet:JAVADOC:710;
   *
   * @test_Strategy: Create two Servlets AsyncTestServlet and AsynTest both
   * support async; Client send a request to AsyncTestServlet; StartAsync in
   * AsyncTestServlet ServletRequest.startAsync(ServletRequest,
   * ServletResponse); call ac.dispatch(ServletContext, path to AsynTest); call
   * ServletRequest.isAsyncSupported() call ServletRequest.isAsyncStarted() call
   * ServletRequest.getDispatcherType() verifies all work accordingly.
   */
  public void dispatchContextPathTest1() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dispatchContextPathTest1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "ASYNC_NOT_STARTED_dispatchContextPathTest1|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=REQUEST|"
            + "ASYNC_STARTED_asyncTest|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=ASYNC");
    invoke();
  }

  /*
   * @testName: dispatchContextPathTest2
   * 
   * @assertion_ids: Servlet:JAVADOC:641; Servlet:JAVADOC:703;
   * Servlet:JAVADOC:707; Servlet:JAVADOC:708; Servlet:JAVADOC:710;
   *
   * @test_Strategy: Create two Servlets AsyncTestServlet and AsynTest both
   * support async; Client send a request to AsyncTestServlet; StartAsync in
   * AsyncTestServlet ServletRequest.startAsync(ServletRequestWrapper,
   * ServletResponseWrapper); call ac.dispatch(ServletContext, path to
   * AsynTest); call ServletRequest.isAsyncSupported() call
   * ServletRequest.isAsyncStarted() call ServletRequest.getDispatcherType()
   * verifies all work accordingly.
   */
  public void dispatchContextPathTest2() throws Fault {
    TEST_PROPS.setProperty(APITEST, "dispatchContextPathTest2");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "ASYNC_NOT_STARTED_dispatchContextPathTest2|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=REQUEST|"
            + "ASYNC_STARTED_asyncTest|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=ASYNC");
    invoke();
  }

  /*
   * @testName: forwardTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:639; Servlet:JAVADOC:639.2;
   * Servlet:JAVADOC:639.4; Servlet:JAVADOC:703; Servlet:JAVADOC:707;
   * Servlet:JAVADOC:708; Servlet:JAVADOC:710;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which supports async;
   * Client send a request to AsyncTestServlet at
   * "/AsyncTestServlet?testname=forwardTest1";
   * getRequestDispatcher("/AsyncTestServlet?testname=forwardDummy1").forward(
   * request, response); In forwardDummy1: AsyncContext ac =
   * request.startAsync(request, response); ac.dispatch(); verifies that it
   * dispatches to "/AsyncTestServlet?testname=forwardDummy1".
   */
  public void forwardTest1() throws Fault {
    TEST_PROPS.setProperty(APITEST, "forwardTest1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "forwardDummy1|" + "ASYNC_NOT_STARTED_forwardDummy1|"
            + "IsAsyncSupported=true|" + "IsAsyncStarted=false|"
            + "DispatcherType=FORWARD|" + "forwardDummy1|"
            + "ASYNC_STARTED_forwardDummy1|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=ASYNC");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "ASYNC_STARTED_forwardTest1");
    invoke();
  }

  /*
   * @testName: getRequestTest
   * 
   * @assertion_ids: Servlet:JAVADOC:642; Servlet:JAVADOC:710;
   * Servlet:JAVADOC:710.1; Servlet:SPEC:270;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(); call AsyncContext.getRequest() verifies it
   * works.
   */
  public void getRequestTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestTest");
    invoke();
  }

  /*
   * @testName: getRequestTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:642; Servlet:JAVADOC:712;
   * Servlet:JAVADOC:712.1; Servlet:SPEC:270;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(Request, Response); call
   * AsyncContext.getRequest() verifies it works.
   */
  public void getRequestTest1() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestTest1");
    invoke();
  }

  /*
   * @testName: getRequestTest2
   * 
   * @assertion_ids: Servlet:JAVADOC:642; Servlet:JAVADOC:712;
   * Servlet:JAVADOC:712.1; Servlet:SPEC:270;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(RequestWrapper, ResponseWrapper); call
   * AsyncContext.getRequest() verifies it works.
   */
  public void getRequestTest2() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestTest2");
    invoke();
  }

  /*
   * @testName: getRequestTest3
   * 
   * @assertion_ids: Servlet:JAVADOC:642; Servlet:JAVADOC:712;
   * Servlet:JAVADOC:712.1; Servlet:SPEC:270;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(Request, ResponseWrapper); call
   * AsyncContext.getRequest() verifies it works.
   */
  public void getRequestTest3() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestTest3");
    invoke();
  }

  /*
   * @testName: getRequestTest4
   * 
   * @assertion_ids: Servlet:JAVADOC:642; Servlet:JAVADOC:712;
   * Servlet:JAVADOC:712.1; Servlet:SPEC:270;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(RequestWrapper, Response); call
   * AsyncContext.getRequest() verifies it works.
   */
  public void getRequestTest4() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestTest4");
    invoke();
  }

  /*
   * @testName: getRequestTest6
   * 
   * @assertion_ids: Servlet:JAVADOC:642; Servlet:JAVADOC:710;
   * Servlet:JAVADOC:710.1; Servlet:SPEC:270;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(); In a separate thread, call
   * AsyncContext.getRequest() verifies it works.
   */
  public void getRequestTest6() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestTest6");
    invoke();
  }

  /*
   * @testName: getRequestTest7
   * 
   * @assertion_ids: Servlet:JAVADOC:642; Servlet:JAVADOC:712;
   * Servlet:JAVADOC:712.1; Servlet:SPEC:270;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(Request, Response); In a separate thread, call
   * AsyncContext.getRequest() verifies it works.
   */
  public void getRequestTest7() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestTest7");
    invoke();
  }

  /*
   * @testName: getRequestTest8
   * 
   * @assertion_ids: Servlet:JAVADOC:642; Servlet:JAVADOC:712;
   * Servlet:JAVADOC:712.1; Servlet:SPEC:270;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(RequestWrapper, ResponseWrapper); In a separate
   * thread, call AsyncContext.getRequest() verifies it works.
   */
  public void getRequestTest8() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestTest8");
    invoke();
  }

  /*
   * @testName: getRequestTest9
   * 
   * @assertion_ids: Servlet:JAVADOC:642; Servlet:JAVADOC:712;
   * Servlet:JAVADOC:712.1; Servlet:SPEC:270;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(Request, ResponseWrapper); In a separate thread,
   * call AsyncContext.getRequest() verifies it works.
   */
  public void getRequestTest9() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestTest9");
    invoke();
  }

  /*
   * @testName: getRequestTest10
   * 
   * @assertion_ids: Servlet:JAVADOC:642; Servlet:JAVADOC:712;
   * Servlet:JAVADOC:712.1; Servlet:SPEC:270;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(RequestWrapper, Response); In a separate thread,
   * call AsyncContext.getRequest() verifies it works.
   */
  public void getRequestTest10() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestTest10");
    invoke();
  }

  /*
   * @testName: getRequestTest12
   * 
   * @assertion_ids: Servlet:JAVADOC:642; Servlet:JAVADOC:645;
   * Servlet:JAVADOC:710; Servlet:JAVADOC:710.1; Servlet:SPEC:270;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(); In a separate thread, call
   * AsyncContext.getRequest() verifies it works.
   */
  public void getRequestTest12() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestTest12");
    invoke();
  }

  /*
   * @testName: getRequestTest13
   * 
   * @assertion_ids: Servlet:JAVADOC:642; Servlet:JAVADOC:645;
   * Servlet:JAVADOC:712; Servlet:JAVADOC:712.1; Servlet:SPEC:270;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(Request, Response); In a separate thread, call
   * AsyncContext.getRequest() verifies it works.
   */
  public void getRequestTest13() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestTest13");
    invoke();
  }

  /*
   * @testName: getRequestTest14
   * 
   * @assertion_ids: Servlet:JAVADOC:642; Servlet:JAVADOC:645;
   * Servlet:JAVADOC:712; Servlet:JAVADOC:712.1; Servlet:SPEC:270;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(RequestWrapper, ResponseWrapper); In a separate
   * thread, call AsyncContext.getRequest() verifies it works.
   */
  public void getRequestTest14() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestTest14");
    invoke();
  }

  /*
   * @testName: getRequestTest15
   * 
   * @assertion_ids: Servlet:JAVADOC:642; Servlet:JAVADOC:645;
   * Servlet:JAVADOC:712; Servlet:JAVADOC:712.1; Servlet:SPEC:270;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(Request, ResponseWrapper); In a separate thread,
   * call AsyncContext.getRequest() verifies it works.
   */
  public void getRequestTest15() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestTest15");
    invoke();
  }

  /*
   * @testName: getRequestTest16
   * 
   * @assertion_ids: Servlet:JAVADOC:642; Servlet:JAVADOC:645;
   * Servlet:JAVADOC:712; Servlet:JAVADOC:712.1; Servlet:SPEC:270;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(RequestWrapper, Response); In a separate thread,
   * call AsyncContext.getRequest() verifies it works.
   */
  public void getRequestTest16() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getRequestTest16");
    invoke();
  }

  /*
   * @testName: asyncListenerTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:649; Servlet:JAVADOC:710;
   * Servlet:JAVADOC:846; Servlet:JAVADOC:866; Servlet:JAVADOC:873;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Create an AsyncListenerBad; Client send a request to AsyncTestServlet;
   * StartAsync in AsyncTestServlet; AsyncContext.createistener(clazz) verifies
   * ServletException is thrown when clazz fails to be instantiated.
   */
  public void asyncListenerTest1() throws Fault {
    TEST_PROPS.setProperty(APITEST, "asyncListenerTest1");
    invoke();
  }

  /*
   * @testName: asyncListenerTest6
   * 
   * @assertion_ids: Servlet:JAVADOC:645; Servlet:JAVADOC:649;
   * Servlet:JAVADOC:710; Servlet:JAVADOC:846; Servlet:JAVADOC:866;
   * Servlet:JAVADOC:873;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Create an AsyncListenerBad; Client send a request to AsyncTestServlet;
   * StartAsync in AsyncTestServlet; AsyncContext.createistener(clazz) In a
   * separate thread, call AsyncContext.complete(); verifies ServletException is
   * thrown when clazz fails to be instantiated.
   */
  public void asyncListenerTest6() throws Fault {
    TEST_PROPS.setProperty(APITEST, "asyncListenerTest6");
    invoke();
  }

  /*
   * @testName: timeOutTest
   * 
   * @assertion_ids: Servlet:JAVADOC:649; Servlet:JAVADOC:710;
   * Servlet:JAVADOC:846; Servlet:JAVADOC:868; Servlet:JAVADOC:869;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet;
   * AsyncContext.setTimeout(L) verifies it works using getTimeout.
   */
  public void timeOutTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "timeOutTest");
    invoke();
  }

  /*
   * @testName: timeOutTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:649; Servlet:JAVADOC:710;
   * Servlet:JAVADOC:846; Servlet:JAVADOC:868; Servlet:JAVADOC:868.1;
   * Servlet:JAVADOC:869; Servlet:JAVADOC:869.3;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet;
   * AsyncContext.setTimeout(0L) verifies it works using getTimeout.
   */
  public void timeOutTest1() throws Fault {
    TEST_PROPS.setProperty(APITEST, "timeOutTest1");
    invoke();
  }

  /*
   * @testName: timeOutTest2
   * 
   * @assertion_ids: Servlet:JAVADOC:651; Servlet:JAVADOC:710;
   * Servlet:JAVADOC:846; Servlet:JAVADOC:868; Servlet:JAVADOC:869;
   * Servlet:JAVADOC:869.1; Servlet:JAVADOC:869.2; Servlet:JAVADOC:869.5;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet;
   * AsyncContext.setTimeout(L) verifies it works by letting it timeout.
   */
  public void timeOutTest2() throws Fault {
    TEST_PROPS.setProperty(APITEST, "timeOutTest2");
    TEST_PROPS.setProperty(SEARCH_STRING, "in onTimeout method of ACListener2");
    TEST_PROPS.setProperty(STATUS_CODE, "-1");
    invoke();
  }

  /*
   * @testName: timeOutTest4
   * 
   * @assertion_ids: Servlet:JAVADOC:651; Servlet:JAVADOC:710;
   * Servlet:JAVADOC:846; Servlet:JAVADOC:868; Servlet:JAVADOC:869.4;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet;
   * verifies it times out at default timeout.
   */
  public void timeOutTest4() throws Fault {
    TEST_PROPS.setProperty(APITEST, "timeOutTest4");
    TEST_PROPS.setProperty(SEARCH_STRING, "in onTimeout method of ACListener2");
    TEST_PROPS.setProperty(STATUS_CODE, "-1");
    invoke();
  }

  /*
   * @testName: originalRequestTest
   * 
   * @assertion_ids: Servlet:JAVADOC:638; Servlet:JAVADOC:644;
   * Servlet:JAVADOC:710; Servlet:JAVADOC:710.2;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; Call ServletRequest.startAsync()
   * in AsyncTestServlet; verifies AsyncContext.hasOriginalRequestAndResponse
   * works.
   */
  public void originalRequestTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "originalRequestTest");
    invoke();
  }

  /*
   * @testName: originalRequestTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:638; Servlet:JAVADOC:644;
   * Servlet:JAVADOC:712; Servlet:JAVADOC:712.1; Servlet:JAVADOC:712.3; *
   * 
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; Call
   * ServletRequest.startAsync(ServletRequest, ServletResponse); verifies
   * AsyncContext.hasOriginalRequestAndResponse works.
   */
  public void originalRequestTest1() throws Fault {
    TEST_PROPS.setProperty(APITEST, "originalRequestTest1");
    invoke();
  }

  /*
   * @testName: originalRequestTest2
   * 
   * @assertion_ids: Servlet:JAVADOC:638; Servlet:JAVADOC:644;
   * Servlet:JAVADOC:712; Servlet:JAVADOC:712.1; Servlet:JAVADOC:712.3;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; Call
   * ServletRequest.startAsync(ServletRequestWrapper, ServletResponseWrapper);
   * verifies AsyncContext.hasOriginalRequestAndResponse works.
   */
  public void originalRequestTest2() throws Fault {
    TEST_PROPS.setProperty(APITEST, "originalRequestTest2");
    invoke();
  }

  /*
   * @testName: originalRequestTest3
   * 
   * @assertion_ids: Servlet:JAVADOC:638; Servlet:JAVADOC:644;
   * Servlet:JAVADOC:712; Servlet:JAVADOC:712.1; Servlet:JAVADOC:712.3;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; Call
   * ServletRequest.startAsync(ServletRequestWrapper, ServletResponse); verifies
   * AsyncContext.hasOriginalRequestAndResponse works.
   */
  public void originalRequestTest3() throws Fault {
    TEST_PROPS.setProperty(APITEST, "originalRequestTest3");
    invoke();
  }

  /*
   * @testName: originalRequestTest4
   * 
   * @assertion_ids: Servlet:JAVADOC:638; Servlet:JAVADOC:644;
   * Servlet:JAVADOC:712; Servlet:JAVADOC:712.1; Servlet:JAVADOC:712.3;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; Call
   * ServletRequest.startAsync(ServletRequest, ServletResponseWrapper); verifies
   * AsyncContext.hasOriginalRequestAndResponse works.
   */
  public void originalRequestTest4() throws Fault {
    TEST_PROPS.setProperty(APITEST, "originalRequestTest4");
    invoke();
  }

  /*
   * @testName: getResponseTest
   * 
   * @assertion_ids: Servlet:JAVADOC:643; Servlet:JAVADOC:710;
   * Servlet:JAVADOC:710.1; Servlet:SPEC:271;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(); call AsyncContext.getResponse() verifies it
   * works.
   */
  public void getResponseTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResponseTest");
    invoke();
  }

  /*
   * @testName: getResponseTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:643; Servlet:JAVADOC:712;
   * Servlet:JAVADOC:712.1; Servlet:SPEC:271;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(Request, Response); call
   * AsyncContext.getResponse() verifies it works.
   */
  public void getResponseTest1() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResponseTest1");
    invoke();
  }

  /*
   * @testName: getResponseTest2
   * 
   * @assertion_ids: Servlet:JAVADOC:643; Servlet:JAVADOC:712;
   * Servlet:JAVADOC:712.1; Servlet:SPEC:271;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(RequestWrapper, ResponseWrapper); call
   * AsyncContext.getResponse() verifies it works.
   */
  public void getResponseTest2() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResponseTest2");
    invoke();
  }

  /*
   * @testName: getResponseTest3
   * 
   * @assertion_ids: Servlet:JAVADOC:643; Servlet:JAVADOC:712;
   * Servlet:JAVADOC:712.1; Servlet:SPEC:271;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(Request, ResponseWrapper); call
   * AsyncContext.getResponse() verifies it works.
   */
  public void getResponseTest3() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResponseTest3");
    invoke();
  }

  /*
   * @testName: getResponseTest4
   * 
   * @assertion_ids: Servlet:JAVADOC:643; Servlet:JAVADOC:712;
   * Servlet:JAVADOC:712.1; Servlet:SPEC:271;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which support async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(RequestWrapper, Response); call
   * AsyncContext.getResponse() verifies it works.
   */
  public void getResponseTest4() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getResponseTest4");
    invoke();
  }

  /*
   * @testName: startAsyncAgainTest
   * 
   * @assertion_ids: Servlet:JAVADOC:639; Servlet:JAVADOC:639.2;
   * Servlet:JAVADOC:639.3; Servlet:JAVADOC:639.4; Servlet:JAVADOC:639.5;
   * Servlet:JAVADOC:639.9; Servlet:JAVADOC:703; Servlet:JAVADOC:707;
   * Servlet:JAVADOC:708; Servlet:JAVADOC:710;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which supports async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(); call ServletRequest.isAsyncSupported() call
   * ServletRequest.isAsyncStarted() call ServletRequest.getDispatcherType()
   * StartAsync again verifies all work accordingly.
   */
  public void startAsyncAgainTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "startAsyncAgainTest");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "ASYNC_NOT_STARTED_startAsyncAgainTest|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=REQUEST|"
            + "startAsync called|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=true|" + "DispatcherType=REQUEST|"
            + "startAsync called again|"
            + "Expected IllegalStateException thrown");
    invoke();
  }

  /*
   * @testName: startAsyncAgainTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:639; Servlet:JAVADOC:639.2;
   * Servlet:JAVADOC:639.3; Servlet:JAVADOC:639.4; Servlet:JAVADOC:639.5;
   * Servlet:JAVADOC:639.9; Servlet:JAVADOC:703; Servlet:JAVADOC:707;
   * Servlet:JAVADOC:708; Servlet:JAVADOC:712;
   *
   * @test_Strategy: Create a Servlet AsyncTestServlet which supports async;
   * Client send a request to AsyncTestServlet; StartAsync in AsyncTestServlet
   * ServletRequest.startAsync(request,response); call
   * ServletRequest.isAsyncSupported() call ServletRequest.isAsyncStarted() call
   * ServletRequest.getDispatcherType() StartAsync again verifies all work
   * accordingly.
   */
  public void startAsyncAgainTest1() throws Fault {
    TEST_PROPS.setProperty(APITEST, "startAsyncAgainTest1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "ASYNC_NOT_STARTED_startAsyncAgainTest1|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=false|" + "DispatcherType=REQUEST|"
            + "startAsync called|" + "IsAsyncSupported=true|"
            + "IsAsyncStarted=true|" + "DispatcherType=REQUEST|"
            + "startAsync called again|"
            + "Expected IllegalStateException thrown");
    invoke();
  }
}
