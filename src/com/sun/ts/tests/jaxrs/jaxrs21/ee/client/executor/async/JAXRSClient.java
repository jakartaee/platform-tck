/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.jaxrs21.ee.client.executor.async;

import java.io.IOException;
import java.util.concurrent.Future;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.sun.ts.tests.jaxrs.common.client.JdkLoggingFilter;
import com.sun.ts.tests.jaxrs.jaxrs21.ee.client.executor.ExecutorServiceChecker;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient
    extends com.sun.ts.tests.jaxrs.ee.rs.client.asyncinvoker.JAXRSClient
    implements ExecutorServiceChecker {

  private static final long serialVersionUID = 21L;

  public JAXRSClient() {
    setContextRoot("/jaxrs_jaxrs21_ee_client_executor_async_web/resource");
  }

  public static void main(String[] args) {
    JAXRSClient client = new JAXRSClient();
    client.run(args);
    try {
      client.close();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /* Run test */
  // --------------------------------------------------------------------
  // ---------------------- DELETE --------------------------------------
  // --------------------------------------------------------------------
  /*
   * @testName: deleteTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP DELETE method for the current request
   * asynchronously.
   */
  public Future<Response> deleteTest() throws Fault {
    return super.deleteTest();
  }

  /*
   * @testName: deleteWithStringClassWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP DELETE method for the current request
   * asynchronously.
   */
  public Future<String> deleteWithStringClassWhileServerWaitTest()
      throws Fault {
    return super.deleteWithStringClassWhileServerWaitTest();
  }

  /*
   * @testName: deleteWithResponseClassWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP DELETE method for the current request
   * asynchronously.
   */
  public Future<Response> deleteWithResponseClassWhileServerWaitTest()
      throws Fault {
    return super.deleteWithResponseClassWhileServerWaitTest();
  }

  /*
   * @testName: deleteWithGenericTypeStringWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP DELETE method for the current request
   * asynchronously.
   */
  public Future<String> deleteWithGenericTypeStringWhileServerWaitTest()
      throws Fault {
    return super.deleteWithGenericTypeStringWhileServerWaitTest();
  }

  /*
   * @testName: deleteWithGenericTypeResponseWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP DELETE method for the current request
   * asynchronously.
   */
  public Future<Response> deleteWithGenericTypeResponseWhileServerWaitTest()
      throws Fault {
    return super.deleteWithGenericTypeResponseWhileServerWaitTest();
  }

  /*
   * @testName: deleteWithCallbackWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP DELETE method for the current request
   * asynchronously.
   */
  public Future<Response> deleteWithCallbackWhileServerWaitTest() throws Fault {
    return super.deleteWithCallbackWhileServerWaitTest();
  }

  /*
   * @testName: deleteWithCallbackStringWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP DELETE method for the current request
   * asynchronously.
   */
  public Future<String> deleteWithCallbackStringWhileServerWaitTest()
      throws Fault {
    return super.deleteWithCallbackStringWhileServerWaitTest();
  }

  // ------------------------------------------------------------------
  // ---------------------------GET------------------------------------
  // ------------------------------------------------------------------
  /*
   * @testName: getTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP GET method for the current request
   * asynchronously.
   */
  public Future<Response> getTest() throws Fault {
    return super.getTest();
  }

  /*
   * @testName: getWithStringClassWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP GET method for the current request
   * asynchronously.
   */
  public Future<String> getWithStringClassWhileServerWaitTest() throws Fault {
    return super.getWithStringClassWhileServerWaitTest();
  }

  /*
   * @testName: getWithResponseClassWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP GET method for the current request
   * asynchronously.
   */
  public Future<Response> getWithResponseClassWhileServerWaitTest()
      throws Fault {
    return super.deleteWithResponseClassWhileServerWaitTest();
  }

  /*
   * @testName: getWithGenericTypeStringWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP GET method for the current request
   * asynchronously.
   */
  public Future<String> getWithGenericTypeStringWhileServerWaitTest()
      throws Fault {
    return super.getWithGenericTypeStringWhileServerWaitTest();
  }

  /*
   * @testName: getWithGenericTypeResponseWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP GET method for the current request
   * asynchronously.
   */
  public Future<Response> getWithGenericTypeResponseWhileServerWaitTest()
      throws Fault {
    return super.getWithGenericTypeResponseWhileServerWaitTest();
  }

  /*
   * @testName: getWithCallbackWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP GET method for the current request
   * asynchronously.
   */
  public Future<Response> getWithCallbackWhileServerWaitTest() throws Fault {
    return super.getWithCallbackWhileServerWaitTest();
  }

  /*
   * @testName: getWithCallbackStringWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP GET method for the current request
   * asynchronously.
   */
  public Future<String> getWithCallbackStringWhileServerWaitTest()
      throws Fault {
    return super.getWithCallbackStringWhileServerWaitTest();
  }

  // ------------------------------------------------------------------
  // ---------------------------HEAD-----------------------------------
  // ------------------------------------------------------------------

  /*
   * @testName: headTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP HEAD method for the current request
   * asynchronously.
   */
  public Future<Response> headTest() throws Fault {
    return super.headTest();
  }

  /*
   * @testName: headWithCallbackWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP HEAD method for the current request
   * asynchronously.
   */
  public Future<Response> headWithCallbackWhileServerWaitTest() throws Fault {
    return super.headWithCallbackWhileServerWaitTest();
  }

  // ------------------------------------------------------------------
  // ---------------------------METHOD-----------------------------------
  // ------------------------------------------------------------------

  /*
   * @testName: methodTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   * asynchronously.
   */
  public Future<Response> methodTest() throws Fault {
    return super.methodTest();
  }

  /*
   * @testName: methodWithStringClassWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   * asynchronously.
   */
  public Future<String> methodWithStringClassWhileServerWaitTest()
      throws Fault {
    return super.methodWithStringClassWhileServerWaitTest();
  }

  /*
   * @testName: methodWithResponseClassWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   * asynchronously.
   */
  public Future<Response> methodWithResponseClassWhileServerWaitTest()
      throws Fault {
    return super.methodWithResponseClassWhileServerWaitTest();
  }

  /*
   * @testName: methodWithGenericTypeStringWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   * asynchronously.
   */
  public Future<String> methodWithGenericTypeStringWhileServerWaitTest()
      throws Fault {
    return super.methodWithGenericTypeStringWhileServerWaitTest();
  }

  /*
   * @testName: methodWithGenericTypeResponseWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   * asynchronously.
   */
  public Future<Response> methodWithGenericTypeResponseWhileServerWaitTest()
      throws Fault {
    return super.methodWithGenericTypeResponseWhileServerWaitTest();
  }

  /*
   * @testName: methodWithCallbackWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   * asynchronously.
   */
  public Future<Response> methodWithCallbackWhileServerWaitTest() throws Fault {
    return super.methodWithCallbackWhileServerWaitTest();
  }

  /*
   * @testName: methodWithCallbackStringWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   * asynchronously.
   */
  public Future<String> methodWithCallbackStringWhileServerWaitTest()
      throws Fault {
    return super.methodWithCallbackStringWhileServerWaitTest();
  }

  /*
   * @testName: methodWithEntityWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   * asynchronously.
   */
  public Future<Response> methodWithEntityWhileServerWaitTest() throws Fault {
    return super.methodWithEntityWhileServerWaitTest();
  }

  /*
   * @testName: methodWithStringClassWithEntityWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   * asynchronously.
   */
  public Future<String> methodWithStringClassWithEntityWhileServerWaitTest()
      throws Fault {
    return super.methodWithStringClassWithEntityWhileServerWaitTest();
  }

  /*
   * @testName: methodWithResponseClassWithEntityWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   * asynchronously.
   */
  public Future<Response> methodWithResponseClassWithEntityWhileServerWaitTest()
      throws Fault {
    return super.methodWithResponseClassWithEntityWhileServerWaitTest();
  }

  /*
   * @testName: methodWithGenericTypeStringWithEntityWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   * asynchronously.
   */
  public Future<String> methodWithGenericTypeStringWithEntityWhileServerWaitTest()
      throws Fault {
    return super.methodWithGenericTypeStringWithEntityWhileServerWaitTest();
  }

  /*
   * @testName: methodWithGenericTypeResponseWithEntityWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   * asynchronously.
   */
  public Future<Response> methodWithGenericTypeResponseWithEntityWhileServerWaitTest()
      throws Fault {
    return super.methodWithGenericTypeResponseWithEntityWhileServerWaitTest();
  }

  /*
   * @testName: methodWithCallbackWithEntityWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   * asynchronously.
   */
  public Future<Response> methodWithCallbackWithEntityWhileServerWaitTest()
      throws Fault {
    return super.methodWithCallbackWithEntityWhileServerWaitTest();
  }

  /*
   * @testName: methodWithCallbackStringWithEntityWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   * asynchronously.
   */
  public Future<String> methodWithCallbackStringWithEntityWhileServerWaitTest()
      throws Fault {
    return super.methodWithCallbackStringWithEntityWhileServerWaitTest();
  }

  // ------------------------------------------------------------------
  // ---------------------------OPTIONS--------------------------------
  // ------------------------------------------------------------------

  /*
   * @testName: optionsTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP options method for the current request
   * asynchronously.
   */
  public Future<Response> optionsTest() throws Fault {
    return super.optionsTest();
  }

  /*
   * @testName: optionsWithStringClassWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP options method for the current request
   * asynchronously.
   */
  public Future<String> optionsWithStringClassWhileServerWaitTest()
      throws Fault {
    return super.optionsWithStringClassWhileServerWaitTest();
  }

  /*
   * @testName: optionsWithResponseClassWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP options method for the current request
   * asynchronously.
   */
  public Future<Response> optionsWithResponseClassWhileServerWaitTest()
      throws Fault {
    return super.optionsWithResponseClassWhileServerWaitTest();
  }

  /*
   * @testName: optionsWithGenericTypeStringWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP options method for the current request
   * asynchronously.
   */
  public Future<String> optionsWithGenericTypeStringWhileServerWaitTest()
      throws Fault {
    return super.optionsWithGenericTypeStringWhileServerWaitTest();
  }

  /*
   * @testName: optionsWithGenericTypeResponseWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP options method for the current request
   * asynchronously.
   */
  public Future<Response> optionsWithGenericTypeResponseWhileServerWaitTest()
      throws Fault {
    return super.optionsWithGenericTypeResponseWhileServerWaitTest();
  }

  /*
   * @testName: optionsWithCallbackWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP options method for the current request
   * asynchronously.
   */
  public Future<Response> optionsWithCallbackWhileServerWaitTest()
      throws Fault {
    return super.optionsWithCallbackWhileServerWaitTest();
  }

  /*
   * @testName: optionsWithStringCallbackWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP options method for the current request
   * asynchronously.
   */
  public Future<String> optionsWithStringCallbackWhileServerWaitTest()
      throws Fault {
    return super.optionsWithStringCallbackWhileServerWaitTest();
  }

  // ------------------------------------------------------------------
  // ---------------------------POST-----------------------------------
  // ------------------------------------------------------------------

  /*
   * @testName: postTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP post method for the current request
   * asynchronously.
   */
  public Future<Response> postTest() throws Fault {
    return super.postTest();
  }

  /*
   * @testName: postWithStringClassWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP post method for the current request
   * asynchronously.
   */
  public Future<String> postWithStringClassWhileServerWaitTest() throws Fault {
    return super.postWithStringClassWhileServerWaitTest();
  }

  /*
   * @testName: postWithResponseClassWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP post method for the current request
   * asynchronously.
   */
  public Future<Response> postWithResponseClassWhileServerWaitTest()
      throws Fault {
    return super.postWithResponseClassWhileServerWaitTest();
  }

  /*
   * @testName: postWithGenericTypeStringWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP post method for the current request
   * asynchronously.
   */
  public Future<String> postWithGenericTypeStringWhileServerWaitTest()
      throws Fault {
    return super.postWithGenericTypeStringWhileServerWaitTest();
  }

  /*
   * @testName: postWithGenericTypeResponseWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP post method for the current request
   * asynchronously.
   */
  public Future<Response> postWithGenericTypeResponseWhileServerWaitTest()
      throws Fault {
    return super.postWithGenericTypeResponseWhileServerWaitTest();
  }

  /*
   * @testName: postWithCallbackWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP post method for the current request
   * asynchronously.
   */
  public Future<Response> postWithCallbackWhileServerWaitTest() throws Fault {
    return super.postWithCallbackWhileServerWaitTest();
  }

  // ------------------------------------------------------------------
  // ---------------------------PUT -----------------------------------
  // ------------------------------------------------------------------

  /*
   * @testName: putTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP PUT method for the current request
   * asynchronously.
   */
  public Future<Response> putTest() throws Fault {
    return super.putTest();
  }

  /*
   * @testName: putWithStringClassWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP put method for the current request
   * asynchronously.
   */
  public Future<String> putWithStringClassWhileServerWaitTest() throws Fault {
    return super.putWithStringClassWhileServerWaitTest();
  }

  /*
   * @testName: putWithResponseClassWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP put method for the current request
   * asynchronously.
   */
  public Future<Response> putWithResponseClassWhileServerWaitTest()
      throws Fault {
    return super.putWithResponseClassWhileServerWaitTest();
  }

  /*
   * @testName: putWithGenericTypeStringWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP put method for the current request
   * asynchronously.
   */
  public Future<String> putWithGenericTypeStringWhileServerWaitTest()
      throws Fault {
    return super.putWithGenericTypeStringWhileServerWaitTest();
  }

  /*
   * @testName: putWithGenericTypeResponseWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP put method for the current request
   * asynchronously.
   */
  public Future<Response> putWithGenericTypeResponseWhileServerWaitTest()
      throws Fault {
    return super.putWithGenericTypeResponseWhileServerWaitTest();
  }

  /*
   * @testName: putWithCallbackWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP put method for the current request
   * asynchronously.
   */
  public Future<Response> putWithCallbackWhileServerWaitTest() throws Fault {
    return super.putWithCallbackWhileServerWaitTest();
  }

  /*
   * @testName: putWithStringCallbackWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP put method for the current request
   * asynchronously.
   */
  public Future<String> putWithStringCallbackWhileServerWaitTest()
      throws Fault {
    return super.putWithStringCallbackWhileServerWaitTest();
  }

  // ------------------------------------------------------------------
  // ---------------------------TRACE -----------------------------------
  // ------------------------------------------------------------------

  /*
   * @testName: traceTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP trace method for the current request
   * asynchronously.
   */
  public Future<Response> traceTest() throws Fault {
    return super.traceTest();
  }

  /*
   * @testName: traceWithStringClassWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP trace method for the current request
   * asynchronously.
   */
  public Future<String> traceWithStringClassWhileServerWaitTest() throws Fault {
    return super.traceWithStringClassWhileServerWaitTest();
  }

  /*
   * @testName: traceWithResponseClassWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP trace method for the current request
   * asynchronously.
   */
  public Future<Response> traceWithResponseClassWhileServerWaitTest()
      throws Fault {
    return super.traceWithResponseClassWhileServerWaitTest();
  }

  /*
   * @testName: traceWithGenericTypeStringWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP trace method for the current request
   * asynchronously.
   */
  public Future<String> traceWithGenericTypeStringWhileServerWaitTest()
      throws Fault {
    return super.traceWithGenericTypeStringWhileServerWaitTest();
  }

  /*
   * @testName: traceWithGenericTypeResponseWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP trace method for the current request
   * asynchronously.
   */
  public Future<Response> traceWithGenericTypeResponseWhileServerWaitTest()
      throws Fault {
    return super.traceWithGenericTypeResponseWhileServerWaitTest();
  }

  /*
   * @testName: traceWithCallbackWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP trace method for the current request
   * asynchronously.
   */
  public Future<Response> traceWithCallbackWhileServerWaitTest() throws Fault {
    return super.traceWithCallbackWhileServerWaitTest();
  }

  /*
   * @testName: traceWithStringCallbackWhileServerWaitTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1131;
   * 
   * @test_Strategy: Invoke HTTP trace method for the current request
   * asynchronously.
   */
  public Future<String> traceWithStringCallbackWhileServerWaitTest()
      throws Fault {
    return super.traceWithStringCallbackWhileServerWaitTest();
  }

  // ///////////////////////////////////////////////////////////////////////
  // utility methods

  /**
   * Create AsyncInvoker for given resource method and start time
   */
  protected AsyncInvoker startAsyncInvokerForMethod(String methodName) {
    Client client = createClient();
    client.register(new JdkLoggingFilter(false));
    WebTarget target = client.target(getAbsoluteUrl(methodName));
    AsyncInvoker async = target.request().async();
    setStartTime();
    return async;
  }
}
