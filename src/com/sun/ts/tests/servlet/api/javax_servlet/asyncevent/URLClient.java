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

package com.sun.ts.tests.servlet.api.javax_servlet.asyncevent;

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

    setServletName("AsyncTestServlet");
    setContextRoot("/servlet_js_asyncevent_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run test */

  /*
   * @testName: constructorTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:842;
   * 
   * @test_Strategy: test the constructor AsyncEvent( AsyncContext )
   */
  public void constructorTest1() throws Fault {
    TEST_PROPS.setProperty(APITEST, "constructorTest1");
    invoke();
  }

  /*
   * @testName: constructorTest2
   * 
   * @assertion_ids: Servlet:JAVADOC:843;
   * 
   * @test_Strategy: test the constructor AsyncEvent(AsyncContext,
   * ServletRequest, ServletResponse)
   */
  public void constructorTest2() throws Fault {
    TEST_PROPS.setProperty(APITEST, "constructorTest2");
    invoke();
  }

  /*
   * @testName: constructorTest3
   * 
   * @assertion_ids: Servlet:JAVADOC:844;
   * 
   * @test_Strategy: test the constructor AsyncEvent(AsyncContext, Throwable)
   */
  public void constructorTest3() throws Fault {
    TEST_PROPS.setProperty(APITEST, "constructorTest3");
    invoke();
  }

  /*
   * @testName: constructorTest4
   * 
   * @assertion_ids: Servlet:JAVADOC:845;
   * 
   * @test_Strategy: test the constructor AsyncEvent(AsyncContext,
   * ServletRequest, ServletResponse, Throwable)
   */
  public void constructorTest4() throws Fault {
    TEST_PROPS.setProperty(APITEST, "constructorTest4");
    invoke();
  }

  /*
   * @testName: getSuppliedRequestTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:847;
   * 
   * @test_Strategy: test the constructor AsyncEvent(AsyncContext,
   * ServletRequest, ServletResponse) verify AsyncEvent.getSuplliedRequest()
   * works
   */
  public void getSuppliedRequestTest1() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getSuppliedRequestTest1");
    invoke();
  }

  /*
   * @testName: getSuppliedRequestTest2
   * 
   * @assertion_ids: Servlet:JAVADOC:847;
   * 
   * @test_Strategy: test the constructor AsyncEvent(AsyncContext,
   * ServletRequest, ServletResponse, Throwable) verify
   * AsyncEvent.getSuplliedRequest() works
   */
  public void getSuppliedRequestTest2() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getSuppliedRequestTest2");
    invoke();
  }

  /*
   * @testName: getSuppliedResponseTest1
   * 
   * @assertion_ids: Servlet:JAVADOC:848;
   * 
   * @test_Strategy: test the constructor AsyncEvent(AsyncContext,
   * ServletRequest, ServletResponse) verify AsyncEvent.getSuplliedResponse()
   * works
   */
  public void getSuppliedResponseTest1() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getSuppliedResponseTest1");
    invoke();
  }

  /*
   * @testName: getSuppliedResponseTest2
   * 
   * @assertion_ids: Servlet:JAVADOC:848;
   * 
   * @test_Strategy: test the constructor AsyncEvent(AsyncContext,
   * ServletRequest, ServletResponse, Throwable) verify
   * AsyncEvent.getSuplliedResponse() works
   */
  public void getSuppliedResponseTest2() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getSuppliedResponseTest2");
    invoke();
  }

  /*
   * @testName: getThrowableTest
   * 
   * @assertion_ids: Servlet:JAVADOC:849;
   * 
   * @test_Strategy: test the constructor AsyncEvent(AsyncContext,
   * ServletRequest, ServletResponse, Throwable) verify
   * AsyncEvent.getThrowable() works
   */
  public void getThrowableTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getThrowableTest");
    invoke();
  }
}
