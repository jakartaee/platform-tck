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

package com.sun.ts.tests.jaxrs.jaxrs21.ee.client.rxinvoker;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.CompletionStageRxInvoker;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;
import com.sun.ts.tests.jaxrs.common.client.JdkLoggingFilter;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
/**
 * @since 2.1
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = 21L;

  protected long millis;

  protected int callbackResult = 0;

  protected Throwable callbackException = null;

  private final static String NONEXISTING_SITE = "somenonexisting.domain-site";

  public JAXRSClient() {
    setContextRoot("/jaxrs_jaxrs21_ee_client_rxinvoker_web/resource");
  }

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  static final String[] METHODS = { "DELETE", "GET", "OPTIONS" };

  static final String[] ENTITY_METHODS = { "PUT", "POST" };

  /* Run test */
  // --------------------------------------------------------------------
  // ---------------------- DELETE --------------------------------------
  // --------------------------------------------------------------------
  /*
   * @testName: deleteTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1134; JAXRS:JAVADOC:1162;
   * 
   * @test_Strategy: Invoke HTTP DELETE method for the current request.
   */
  public Future<Response> deleteTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("delete");
    Future<Response> future = rx.delete().toCompletableFuture();
    checkFutureResponseStatus(future, Status.OK);
    return future;
  }

  /*
   * @testName: deleteThrowsExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1134; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1164;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void deleteThrowsExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    CompletionStageRxInvoker rx = startRxInvokerForMethod("delete");
    Future<Response> future = rx.delete().toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: deleteWithStringClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1135; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1164;
   * 
   * @test_Strategy: Invoke HTTP DELETE method for the current request
   * 
   */
  public Future<String> deleteWithStringClassTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("delete");
    Future<String> future = rx.delete(String.class).toCompletableFuture();
    checkFutureString(future, "delete");
    return future;
  }

  /*
   * @testName: deleteWithResponseClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1135; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1165;
   * 
   * @test_Strategy: Invoke HTTP DELETE method for the current request
   * 
   */
  public Future<Response> deleteWithResponseClassTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("delete");
    Future<Response> future = rx.delete(Response.class).toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: deleteWithClassThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1135; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1165;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void deleteWithClassThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    CompletionStageRxInvoker rx = startRxInvokerForMethod("delete");
    Future<String> future = rx.delete(String.class).toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: deleteWithClassThrowsWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1135; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1165;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void deleteWithClassThrowsWebApplicationExceptionTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("deletenotok");
    Future<String> future = rx.delete(String.class).toCompletableFuture();
    assertExceptionWithWebApplicationExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: deleteWithClassThrowsNoWebApplicationExceptionForResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1135; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1165;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void deleteWithClassThrowsNoWebApplicationExceptionForResponseTest()
      throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("deletenotok");
    Future<Response> future = rx.delete(Response.class).toCompletableFuture();
    checkFutureResponseStatus(future, Status.NOT_ACCEPTABLE);
  }

  /*
   * @testName: deleteWithGenericTypeStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1136; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1166;
   * 
   * @test_Strategy: Invoke HTTP DELETE method for the current request
   */
  public Future<String> deleteWithGenericTypeStringTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("delete");
    GenericType<String> generic = createGeneric(String.class);
    Future<String> future = rx.delete(generic).toCompletableFuture();
    checkFutureString(future, "delete");
    return future;
  }

  /*
   * @testName: deleteWithGenericTypeResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1136; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1166;
   * 
   * @test_Strategy: Invoke HTTP DELETE method for the current request
   */
  public Future<Response> deleteWithGenericTypeResponseTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("delete");
    GenericType<Response> generic = createGeneric(Response.class);
    Future<Response> future = rx.delete(generic).toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: deleteWithGenericTypeThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1136; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1166;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void deleteWithGenericTypeThrowsProcessingExceptionTest()
      throws Fault {
    _hostname = NONEXISTING_SITE;
    CompletionStageRxInvoker rx = startRxInvokerForMethod("delete");
    GenericType<String> generic = createGeneric(String.class);
    Future<String> future = rx.delete(generic).toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: deleteWithGenericTypeThrowsWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1136; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1166;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void deleteWithGenericTypeThrowsWebApplicationExceptionTest()
      throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("deletenotok");
    GenericType<String> generic = createGeneric(String.class);
    Future<String> future = rx.delete(generic).toCompletableFuture();
    assertExceptionWithWebApplicationExceptionIsThrownAndLog(future);
  }

  /*
   * @testName:
   * deleteWithGenericTypeThrowsNoWebApplicationExceptionForResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1136; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1166;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void deleteWithGenericTypeThrowsNoWebApplicationExceptionForResponseTest()
      throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("deletenotok");
    GenericType<Response> generic = createGeneric(Response.class);
    Future<Response> future = rx.delete(generic).toCompletableFuture();
    checkFutureResponseStatus(future, Status.NOT_ACCEPTABLE);
  }

  // ------------------------------------------------------------------
  // ---------------------------GET------------------------------------
  // ------------------------------------------------------------------

  /*
   * @testName: getTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1137; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1167;
   * 
   * @test_Strategy: Invoke HTTP GET method for the current request
   */
  public Future<Response> getTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("get");
    Future<Response> future = rx.get().toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: getThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1137; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1167;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void getThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    CompletionStageRxInvoker rx = startRxInvokerForMethod("get");
    Future<Response> future = rx.get().toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: getWithStringClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1138; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1168;
   * 
   * @test_Strategy: Invoke HTTP GET method for the current request
   */
  public Future<String> getWithStringClassTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("get");
    Future<String> future = rx.get(String.class).toCompletableFuture();
    checkFutureString(future, "get");
    return future;
  }

  /*
   * @testName: getWithResponseClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1138; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1168;
   * 
   * @test_Strategy: Invoke HTTP GET method for the current request
   */
  public Future<Response> getWithResponseClassTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("get");
    Future<Response> future = rx.get(Response.class).toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: getWithClassThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1138; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1168;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void getWithClassThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    CompletionStageRxInvoker rx = startRxInvokerForMethod("get");
    Future<String> future = rx.get(String.class).toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: getWithClassThrowsWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1138; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1168;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void getWithClassThrowsWebApplicationExceptionTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("getnotok");
    Future<String> future = rx.get(String.class).toCompletableFuture();
    assertExceptionWithWebApplicationExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: getWithClassThrowsNoWebApplicationExceptionForResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1138; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1168;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void getWithClassThrowsNoWebApplicationExceptionForResponseTest()
      throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("getnotok");
    Future<Response> future = rx.get(Response.class).toCompletableFuture();
    checkFutureResponseStatus(future, Status.NOT_ACCEPTABLE);
  }

  /*
   * @testName: getWithGenericTypeStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1139; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1169;
   * 
   * @test_Strategy: Invoke HTTP GET method for the current request
   */
  public Future<String> getWithGenericTypeStringTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("get");
    GenericType<String> generic = createGeneric(String.class);
    Future<String> future = rx.get(generic).toCompletableFuture();
    checkFutureString(future, "get");
    return future;
  }

  /*
   * @testName: getWithGenericTypeResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1139; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1169;
   * 
   * @test_Strategy: Invoke HTTP GET method for the current request
   */
  public Future<Response> getWithGenericTypeResponseTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("get");
    GenericType<Response> generic = createGeneric(Response.class);
    Future<Response> future = rx.get(generic).toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: getWithGenericTypeThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1139; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1169;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void getWithGenericTypeThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    CompletionStageRxInvoker rx = startRxInvokerForMethod("get");
    GenericType<String> generic = createGeneric(String.class);
    Future<String> future = rx.get(generic).toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: getWithGenericTypeThrowsWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1139; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1169;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void getWithGenericTypeThrowsWebApplicationExceptionTest()
      throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("getnotok");
    GenericType<String> generic = createGeneric(String.class);
    Future<String> future = rx.get(generic).toCompletableFuture();
    assertExceptionWithWebApplicationExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: getWithGenericTypeThrowsNoWebApplicationExceptionForResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1139; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1169;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void getWithGenericTypeThrowsNoWebApplicationExceptionForResponseTest()
      throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("getnotok");
    GenericType<Response> generic = createGeneric(Response.class);
    Future<Response> future = rx.get(generic).toCompletableFuture();
    checkFutureResponseStatus(future, Status.NOT_ACCEPTABLE);
  }

  // ------------------------------------------------------------------
  // ---------------------------HEAD-----------------------------------
  // ------------------------------------------------------------------

  /*
   * @testName: headTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1140; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1170;
   * 
   * @test_Strategy: Invoke HTTP HEAD method for the current request
   */
  public Future<Response> headTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("head");
    Future<Response> future = rx.head().toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: headThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1140; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1170;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void headThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    CompletionStageRxInvoker rx = startRxInvokerForMethod("head");
    Future<Response> future = rx.head().toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  // ------------------------------------------------------------------
  // ---------------------------METHOD-----------------------------------
  // ------------------------------------------------------------------

  /*
   * @testName: methodTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1141; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1171;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   */
  public Future<Response> methodTest() throws Fault {
    Future<Response> future = null;
    for (String method : METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "");
      future = rx.method(method).toCompletableFuture();
      checkFutureOkResponse(future);
    }
    return future;
  }

  /*
   * @testName: methodThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1141; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1171;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void methodThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    Future<Response> future = null;
    for (String method : METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase());
      future = rx.method(method).toCompletableFuture();
      assertExceptionWithProcessingExceptionIsThrownAndLog(future);
    }
  }

  /*
   * @testName: methodWithStringClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1142; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1172;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   */
  public Future<String> methodWithStringClassTest() throws Fault {
    Future<String> future = null;
    for (String method : METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "");
      future = rx.method(method, String.class).toCompletableFuture();
      checkFutureString(future, method);
    }
    return future;
  }

  /*
   * @testName: methodWithResponseClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1142; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1172;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   */
  public Future<Response> methodWithResponseClassTest() throws Fault {
    Future<Response> future = null;
    for (String method : METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "");
      future = rx.method(method, Response.class).toCompletableFuture();
      checkFutureOkResponse(future);
    }
    return future;
  }

  /*
   * @testName: methodWithClassThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1142; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1172;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void methodWithClassThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    Future<String> future = null;
    for (String method : METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase());
      future = rx.method(method, String.class).toCompletableFuture();
      assertExceptionWithProcessingExceptionIsThrownAndLog(future);
    }
  }

  /*
   * @testName: methodWithClassThrowsWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1142; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1172;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void methodWithClassThrowsWebApplicationExceptionTest() throws Fault {
    Future<String> future = null;
    for (String method : METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "notok");
      future = rx.method(method, String.class).toCompletableFuture();
      assertExceptionWithWebApplicationExceptionIsThrownAndLog(future);
    }
  }

  /*
   * @testName: methodWithClassThrowsNoWebApplicationExceptionForResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1142; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1172;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void methodWithClassThrowsNoWebApplicationExceptionForResponseTest()
      throws Fault {
    Future<Response> future = null;
    for (String method : METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "notok");
      future = rx.method(method, Response.class).toCompletableFuture();
      checkFutureResponseStatus(future, Status.NOT_ACCEPTABLE);
    }
  }

  /*
   * @testName: methodWithGenericTypeStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1143; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1173;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   */
  public Future<String> methodWithGenericTypeStringTest() throws Fault {
    GenericType<String> generic = createGeneric(String.class);
    Future<String> future = null;
    for (String method : METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "");
      future = rx.method(method, generic).toCompletableFuture();
      checkFutureString(future, method);
    }
    return future;
  }

  /*
   * @testName: methodWithGenericTypeResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1143; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1173;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   */
  public Future<Response> methodWithGenericTypeResponseTest() throws Fault {
    GenericType<Response> generic = createGeneric(Response.class);
    Future<Response> future = null;
    for (String method : METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "");
      future = rx.method(method, generic).toCompletableFuture();
      checkFutureOkResponse(future);
    }
    return future;
  }

  /*
   * @testName: methodWithGenericTypeThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1143; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1173;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void methodWithGenericTypeThrowsProcessingExceptionTest()
      throws Fault {
    _hostname = NONEXISTING_SITE;
    Future<Response> future = null;
    GenericType<Response> generic = createGeneric(Response.class);
    for (String method : METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase());
      future = rx.method(method, generic).toCompletableFuture();
      assertExceptionWithProcessingExceptionIsThrownAndLog(future);
    }
  }

  /*
   * @testName: methodWithGenericTypeThrowsWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1143; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1173;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void methodWithGenericTypeThrowsWebApplicationExceptionTest()
      throws Fault {
    Future<String> future = null;
    GenericType<String> generic = createGeneric(String.class);
    for (String method : METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "notok");
      future = rx.method(method, generic).toCompletableFuture();
      assertExceptionWithWebApplicationExceptionIsThrownAndLog(future);
    }
  }

  /*
   * @testName:
   * methodWithGenericTypeThrowsNoWebApplicationExceptionForResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1143; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1173;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void methodWithGenericTypeThrowsNoWebApplicationExceptionForResponseTest()
      throws Fault {
    Future<Response> future = null;
    GenericType<Response> generic = createGeneric(Response.class);
    for (String method : METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "notok");
      future = rx.method(method, generic).toCompletableFuture();
      checkFutureResponseStatus(future, Status.NOT_ACCEPTABLE);
    }
  }

  /*
   * @testName: methodWithEntityTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1144; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1174;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   */
  public Future<Response> methodWithEntityTest() throws Fault {
    Future<Response> future = null;
    for (String method : ENTITY_METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "");
      Entity<String> entity = Entity.entity(method, MediaType.WILDCARD_TYPE);
      future = rx.method(method, entity).toCompletableFuture();
      checkFutureOkResponse(future);
    }
    return future;
  }

  /*
   * @testName: methodWithEntityThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1144; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1174;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void methodWithEntityThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    Future<Response> future = null;
    for (String method : ENTITY_METHODS) {
      Entity<String> entity = Entity.entity(method, MediaType.WILDCARD_TYPE);
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase());
      future = rx.method(method, entity).toCompletableFuture();
      assertExceptionWithProcessingExceptionIsThrownAndLog(future);
    }
  }

  /*
   * @testName: methodWithStringClassWithEntityTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1145; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1175;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   */
  public Future<String> methodWithStringClassWithEntityTest() throws Fault {
    Future<String> future = null;
    for (String method : ENTITY_METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "");
      Entity<String> entity = Entity.entity(method, MediaType.WILDCARD_TYPE);
      future = rx.method(method, entity, String.class).toCompletableFuture();
      checkFutureString(future, method);
    }
    return future;
  }

  /*
   * @testName: methodWithResponseClassWithEntityTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1145; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1175;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   */
  public Future<Response> methodWithResponseClassWithEntityTest() throws Fault {
    Future<Response> future = null;
    for (String method : ENTITY_METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "");
      Entity<String> entity = Entity.entity(method, MediaType.WILDCARD_TYPE);
      future = rx.method(method, entity, Response.class).toCompletableFuture();
      checkFutureOkResponse(future);
    }
    return future;
  }

  /*
   * @testName: methodWithClassWithEntityThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1145; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1175;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void methodWithClassWithEntityThrowsProcessingExceptionTest()
      throws Fault {
    _hostname = NONEXISTING_SITE;
    Future<String> future = null;
    for (String method : ENTITY_METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase());
      Entity<String> entity = Entity.entity(method, MediaType.WILDCARD_TYPE);
      future = rx.method(method, entity, String.class).toCompletableFuture();
      assertExceptionWithProcessingExceptionIsThrownAndLog(future);
    }
  }

  /*
   * @testName: methodWithClassWithEntityThrowsWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1145; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1175;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void methodWithClassWithEntityThrowsWebApplicationExceptionTest()
      throws Fault {
    Future<String> future = null;
    for (String method : ENTITY_METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "notok");
      Entity<String> entity = Entity.entity(method, MediaType.WILDCARD_TYPE);
      future = rx.method(method, entity, String.class).toCompletableFuture();
      assertExceptionWithWebApplicationExceptionIsThrownAndLog(future);
    }
  }

  /*
   * @testName:
   * methodWithClassWithEntityThrowsNoWebApplicationExceptionForResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1145; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1175;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void methodWithClassWithEntityThrowsNoWebApplicationExceptionForResponseTest()
      throws Fault {
    Future<Response> future = null;
    for (String method : ENTITY_METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "notok");
      Entity<String> entity = Entity.entity(method, MediaType.WILDCARD_TYPE);
      future = rx.method(method, entity, Response.class).toCompletableFuture();
      checkFutureResponseStatus(future, Status.NOT_ACCEPTABLE);
    }
  }

  /*
   * @testName: methodWithGenericTypeStringWithEntityTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1146; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1176;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   */
  public Future<String> methodWithGenericTypeStringWithEntityTest()
      throws Fault {
    Future<String> future = null;
    GenericType<String> generic = createGeneric(String.class);
    for (String method : ENTITY_METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "");
      Entity<String> entity = Entity.entity(method, MediaType.WILDCARD_TYPE);
      future = rx.method(method, entity, generic).toCompletableFuture();
      checkFutureString(future, method);
    }
    return future;
  }

  /*
   * @testName: methodWithGenericTypeResponseWithEntityTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1146; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1176;
   * 
   * @test_Strategy: Invoke an arbitrary method for the current request
   */
  public Future<Response> methodWithGenericTypeResponseWithEntityTest()
      throws Fault {
    Future<Response> future = null;
    GenericType<Response> generic = createGeneric(Response.class);
    for (String method : ENTITY_METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "");
      Entity<String> entity = Entity.entity(method, MediaType.WILDCARD_TYPE);
      future = rx.method(method, entity, generic).toCompletableFuture();
      checkFutureOkResponse(future);
    }
    return future;
  }

  /*
   * @testName: methodWithGenericTypeWithEntityThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1146; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1176;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void methodWithGenericTypeWithEntityThrowsProcessingExceptionTest()
      throws Fault {
    _hostname = NONEXISTING_SITE;
    GenericType<String> generic = createGeneric(String.class);
    Future<String> future = null;
    for (String method : ENTITY_METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase());
      Entity<String> entity = Entity.entity(method, MediaType.WILDCARD_TYPE);
      future = rx.method(method, entity, generic).toCompletableFuture();
      assertExceptionWithProcessingExceptionIsThrownAndLog(future);
    }
  }

  /*
   * @testName: methodWithGenericTypeWithEntityThrowsWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1146; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1176;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void methodWithGenericTypeWithEntityThrowsWebApplicationExceptionTest()
      throws Fault {
    Future<String> future = null;
    GenericType<String> generic = createGeneric(String.class);
    for (String method : ENTITY_METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "notok");
      Entity<String> entity = Entity.entity(method, MediaType.WILDCARD_TYPE);
      future = rx.method(method, entity, generic).toCompletableFuture();
      assertExceptionWithWebApplicationExceptionIsThrownAndLog(future);
    }
  }

  /*
   * @testName:
   * methodWithGenericTypeWithEntityThrowsNoWebApplicationExceptionForResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1146; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1176;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void methodWithGenericTypeWithEntityThrowsNoWebApplicationExceptionForResponseTest()
      throws Fault {
    Future<Response> future = null;
    GenericType<Response> generic = createGeneric(Response.class);
    for (String method : ENTITY_METHODS) {
      CompletionStageRxInvoker rx = startRxInvokerForMethod(
          method.toLowerCase() + "notok");
      Entity<String> entity = Entity.entity(method, MediaType.WILDCARD_TYPE);
      future = rx.method(method, entity, generic).toCompletableFuture();
      checkFutureResponseStatus(future, Status.NOT_ACCEPTABLE);
    }
  }

  // ------------------------------------------------------------------
  // ---------------------------OPTIONS--------------------------------
  // ------------------------------------------------------------------

  /*
   * @testName: optionsTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1147; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1177;
   * 
   * @test_Strategy: Invoke HTTP options method for the current request
   */
  public Future<Response> optionsTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("options");
    Future<Response> future = rx.options().toCompletableFuture();
    checkFutureResponseStatus(future, Status.OK);
    return future;
  }

  /*
   * @testName: optionsThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1147; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1177;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void optionsThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    CompletionStageRxInvoker rx = startRxInvokerForMethod("options");
    Future<Response> future = rx.options().toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: optionsWithStringClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1148; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1178;
   * 
   * @test_Strategy: Invoke HTTP options method for the current request
   */
  public Future<String> optionsWithStringClassTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("options");
    Future<String> future = rx.options(String.class).toCompletableFuture();
    checkFutureString(future, "options");
    return future;
  }

  /*
   * @testName: optionsWithResponseClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1148; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1178;
   * 
   * @test_Strategy: Invoke HTTP options method for the current request
   */
  public Future<Response> optionsWithResponseClassTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("options");
    Future<Response> future = rx.options(Response.class).toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: optionsWithClassThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1148; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1178;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void optionsWithClassThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    CompletionStageRxInvoker rx = startRxInvokerForMethod("options");
    Future<String> future = rx.options(String.class).toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: optionsWithClassThrowsWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1148; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1178;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void optionsWithClassThrowsWebApplicationExceptionTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("optionsnotok");
    Future<String> future = rx.options(String.class).toCompletableFuture();
    assertExceptionWithWebApplicationExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: optionsWithClassThrowsNoWebApplicationExceptionForResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1148; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1178;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void optionsWithClassThrowsNoWebApplicationExceptionForResponseTest()
      throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("optionsnotok");
    Future<Response> future = rx.options(Response.class).toCompletableFuture();
    checkFutureResponseStatus(future, Status.NOT_ACCEPTABLE);
  }

  /*
   * @testName: optionsWithGenericTypeStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1149; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1179;
   * 
   * @test_Strategy: Invoke HTTP options method for the current request
   */
  public Future<String> optionsWithGenericTypeStringTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("options");
    GenericType<String> generic = createGeneric(String.class);
    Future<String> future = rx.options(generic).toCompletableFuture();
    checkFutureString(future, "options");
    return future;
  }

  /*
   * @testName: optionsWithGenericTypeResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1149; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1179;
   * 
   * @test_Strategy: Invoke HTTP options method for the current request
   */
  public Future<Response> optionsWithGenericTypeResponseTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("options");
    GenericType<Response> generic = createGeneric(Response.class);
    Future<Response> future = rx.options(generic).toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: optionsWithGenericTypeThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1149; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1179;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void optionsWithGenericTypeThrowsProcessingExceptionTest()
      throws Fault {
    _hostname = NONEXISTING_SITE;
    CompletionStageRxInvoker rx = startRxInvokerForMethod("options");
    GenericType<String> generic = createGeneric(String.class);
    Future<String> future = rx.options(generic).toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: optionsWithGenericTypeThrowsWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1149; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1179;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void optionsWithGenericTypeThrowsWebApplicationExceptionTest()
      throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("optionsnotok");
    GenericType<String> generic = createGeneric(String.class);
    Future<String> future = rx.options(generic).toCompletableFuture();
    assertExceptionWithWebApplicationExceptionIsThrownAndLog(future);
  }

  /*
   * @testName:
   * optionsWithGenericTypeThrowsNoWebApplicationExceptionForResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1149; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1179;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void optionsWithGenericTypeThrowsNoWebApplicationExceptionForResponseTest()
      throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("optionsnotok");
    GenericType<Response> generic = createGeneric(Response.class);
    Future<Response> future = rx.options(generic).toCompletableFuture();
    checkFutureResponseStatus(future, Status.NOT_ACCEPTABLE);
  }

  // ------------------------------------------------------------------
  // ---------------------------POST-----------------------------------
  // ------------------------------------------------------------------

  /*
   * @testName: postTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1150; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1180;
   * 
   * @test_Strategy: Invoke HTTP post method for the current request
   */
  public Future<Response> postTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("post");
    Entity<String> entity = Entity.entity("post", MediaType.WILDCARD_TYPE);
    Future<Response> future = rx.post(entity).toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: postThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1150; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1180;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void postThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    CompletionStageRxInvoker rx = startRxInvokerForMethod("post");
    Entity<String> entity = Entity.entity("post", MediaType.WILDCARD_TYPE);
    Future<Response> future = rx.post(entity).toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: postWithStringClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1151; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1181;
   * 
   * @test_Strategy: Invoke HTTP post method for the current request
   */
  public Future<String> postWithStringClassTest() throws Fault {
    Entity<String> entity = Entity.entity("post", MediaType.WILDCARD_TYPE);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("post");
    Future<String> future = rx.post(entity, String.class).toCompletableFuture();
    checkFutureString(future, "post");
    return future;
  }

  /*
   * @testName: postWithResponseClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1151; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1181;
   * 
   * @test_Strategy: Invoke HTTP post method for the current request
   */
  public Future<Response> postWithResponseClassTest() throws Fault {
    Entity<String> entity = Entity.entity("post", MediaType.WILDCARD_TYPE);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("post");
    Future<Response> future = rx.post(entity, Response.class)
        .toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: postWithClassThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1151; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1181;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void postWithClassThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    Entity<String> entity = Entity.entity("post", MediaType.WILDCARD_TYPE);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("post");
    Future<String> future = rx.post(entity, String.class).toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: postWithClassThrowsWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1151; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1181;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void postWithClassThrowsWebApplicationExceptionTest() throws Fault {
    Entity<String> entity = Entity.entity("post", MediaType.WILDCARD_TYPE);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("postnotok");
    Future<String> future = rx.post(entity, String.class).toCompletableFuture();
    assertExceptionWithWebApplicationExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: postWithClassThrowsNoWebApplicationExceptionForResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1151; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1181;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void postWithClassThrowsNoWebApplicationExceptionForResponseTest()
      throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("postnotok");
    Entity<String> entity = Entity.entity("post", MediaType.WILDCARD_TYPE);
    Future<Response> future = rx.post(entity, Response.class)
        .toCompletableFuture();
    checkFutureResponseStatus(future, Status.NOT_ACCEPTABLE);
  }

  /*
   * @testName: postWithGenericTypeStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1152; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1182;
   * 
   * @test_Strategy: Invoke HTTP post method for the current request
   */
  public Future<String> postWithGenericTypeStringTest() throws Fault {
    GenericType<String> generic = createGeneric(String.class);
    Entity<String> entity = Entity.entity("post", MediaType.WILDCARD_TYPE);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("post");
    Future<String> future = rx.post(entity, generic).toCompletableFuture();
    checkFutureString(future, "post");
    return future;
  }

  /*
   * @testName: postWithGenericTypeResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1152; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1182;
   * 
   * @test_Strategy: Invoke HTTP post method for the current request
   */
  public Future<Response> postWithGenericTypeResponseTest() throws Fault {
    GenericType<Response> generic = createGeneric(Response.class);
    Entity<String> entity = Entity.entity("post", MediaType.WILDCARD_TYPE);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("post");
    Future<Response> future = rx.post(entity, generic).toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: postWithGenericTypeThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1152; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1182;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void postWithGenericTypeThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    Entity<String> entity = Entity.entity("post", MediaType.WILDCARD_TYPE);
    GenericType<String> generic = createGeneric(String.class);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("post");
    Future<String> future = rx.post(entity, generic).toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: postWithGenericTypeThrowsWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1152; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1182;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void postWithGenericTypeThrowsWebApplicationExceptionTest()
      throws Fault {
    Entity<String> entity = Entity.entity("post", MediaType.WILDCARD_TYPE);
    GenericType<String> generic = createGeneric(String.class);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("postnotok");
    Future<String> future = rx.post(entity, generic).toCompletableFuture();
    assertExceptionWithWebApplicationExceptionIsThrownAndLog(future);
  }

  /*
   * @testName:
   * postWithGenericTypeThrowsNoWebApplicationExceptionForResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1152; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1182;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void postWithGenericTypeThrowsNoWebApplicationExceptionForResponseTest()
      throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("postnotok");
    GenericType<Response> generic = createGeneric(Response.class);
    Entity<String> entity = Entity.entity("post", MediaType.WILDCARD_TYPE);
    Future<Response> future = rx.post(entity, generic).toCompletableFuture();
    checkFutureResponseStatus(future, Status.NOT_ACCEPTABLE);
  }

  // ------------------------------------------------------------------
  // ---------------------------PUT -----------------------------------
  // ------------------------------------------------------------------

  /*
   * @testName: putTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1153; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1183;
   * 
   * @test_Strategy: Invoke HTTP PUT method for the current request
   */
  public Future<Response> putTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("put");
    Entity<String> entity = Entity.entity("put", MediaType.WILDCARD_TYPE);
    Future<Response> future = rx.put(entity).toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: putThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1153; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1183;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void putThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    CompletionStageRxInvoker rx = startRxInvokerForMethod("put");
    Entity<String> entity = Entity.entity("put", MediaType.WILDCARD_TYPE);
    Future<Response> future = rx.put(entity).toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: putWithStringClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1154; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1184;
   * 
   * @test_Strategy: Invoke HTTP put method for the current request
   */
  public Future<String> putWithStringClassTest() throws Fault {
    Entity<String> entity = Entity.entity("put", MediaType.WILDCARD_TYPE);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("put");
    Future<String> future = rx.put(entity, String.class).toCompletableFuture();
    checkFutureString(future, "put");
    return future;
  }

  /*
   * @testName: putWithResponseClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1154; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1184;
   * 
   * @test_Strategy: Invoke HTTP put method for the current request
   */
  public Future<Response> putWithResponseClassTest() throws Fault {
    Entity<String> entity = Entity.entity("put", MediaType.WILDCARD_TYPE);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("put");
    Future<Response> future = rx.put(entity, Response.class)
        .toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: putWithClassThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1154; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1184;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void putWithClassThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    Entity<String> entity = Entity.entity("put", MediaType.WILDCARD_TYPE);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("put");
    Future<String> future = rx.put(entity, String.class).toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: putWithClassThrowsWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1154; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1184;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void putWithClassThrowsWebApplicationExceptionTest() throws Fault {
    Entity<String> entity = Entity.entity("put", MediaType.WILDCARD_TYPE);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("putnotok");
    Future<String> future = rx.put(entity, String.class).toCompletableFuture();
    assertExceptionWithWebApplicationExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: putWithClassThrowsNoWebApplicationExceptionForResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1154; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1184;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void putWithClassThrowsNoWebApplicationExceptionForResponseTest()
      throws Fault {
    Entity<String> entity = Entity.entity("put", MediaType.WILDCARD_TYPE);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("putnotok");
    Future<Response> future = rx.put(entity, Response.class)
        .toCompletableFuture();
    checkFutureResponseStatus(future, Status.NOT_ACCEPTABLE);
  }

  /*
   * @testName: putWithGenericTypeStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1155; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1185;
   * 
   * @test_Strategy: Invoke HTTP put method for the current request
   */
  public Future<String> putWithGenericTypeStringTest() throws Fault {
    GenericType<String> generic = createGeneric(String.class);
    Entity<String> entity = Entity.entity("put", MediaType.WILDCARD_TYPE);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("put");
    Future<String> future = rx.put(entity, generic).toCompletableFuture();
    checkFutureString(future, "put");
    return future;
  }

  /*
   * @testName: putWithGenericTypeResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1155; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1185;
   * 
   * @test_Strategy: Invoke HTTP put method for the current request
   */
  public Future<Response> putWithGenericTypeResponseTest() throws Fault {
    GenericType<Response> generic = createGeneric(Response.class);
    Entity<String> entity = Entity.entity("put", MediaType.WILDCARD_TYPE);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("put");
    Future<Response> future = rx.put(entity, generic).toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: putWithGenericTypeThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1155; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1185;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void putWithGenericTypeThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    GenericType<String> generic = createGeneric(String.class);
    Entity<String> entity = Entity.entity("put", MediaType.WILDCARD_TYPE);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("put");
    Future<String> future = rx.put(entity, generic).toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: putWithGenericTypeThrowsWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1155; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1185;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void putWithGenericTypeThrowsWebApplicationExceptionTest()
      throws Fault {
    GenericType<String> generic = createGeneric(String.class);
    Entity<String> entity = Entity.entity("put", MediaType.WILDCARD_TYPE);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("putnotok");
    Future<String> future = rx.put(entity, generic).toCompletableFuture();
    assertExceptionWithWebApplicationExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: putWithGenericTypeThrowsNoWebApplicationExceptionForResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1155; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1185;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void putWithGenericTypeThrowsNoWebApplicationExceptionForResponseTest()
      throws Fault {
    Entity<String> entity = Entity.entity("put", MediaType.WILDCARD_TYPE);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("putnotok");
    GenericType<Response> generic = createGeneric(Response.class);
    Future<Response> future = rx.put(entity, generic).toCompletableFuture();
    checkFutureResponseStatus(future, Status.NOT_ACCEPTABLE);
  }

  // ------------------------------------------------------------------
  // ---------------------------TRACE -----------------------------------
  // ------------------------------------------------------------------

  /*
   * @testName: traceTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1156; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1186;
   * 
   * @test_Strategy: Invoke HTTP trace method for the current request
   */
  public Future<Response> traceTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("trace");
    Future<Response> future = rx.trace().toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: traceThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1156; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1186;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void traceThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    CompletionStageRxInvoker rx = startRxInvokerForMethod("trace");
    Future<Response> future = rx.trace().toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: traceWithStringClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1157; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1187;
   * 
   * @test_Strategy: Invoke HTTP trace method for the current request
   */
  public Future<String> traceWithStringClassTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("trace");
    Future<String> future = rx.trace(String.class).toCompletableFuture();
    checkFutureString(future, "trace");
    return future;
  }

  /*
   * @testName: traceWithResponseClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1157; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1187;
   * 
   * @test_Strategy: Invoke HTTP trace method for the current request
   */
  public Future<Response> traceWithResponseClassTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("trace");
    Future<Response> future = rx.trace(Response.class).toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: traceWithClassThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1157; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1187;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void traceWithClassThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    CompletionStageRxInvoker rx = startRxInvokerForMethod("trace");
    Future<String> future = rx.trace(String.class).toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: traceWithClassThrowsWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1157; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1187;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void traceWithClassThrowsWebApplicationExceptionTest() throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("tracenotok");
    Future<String> future = rx.trace(String.class).toCompletableFuture();
    assertExceptionWithWebApplicationExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: traceWithClassThrowsNoWebApplicationExceptionForResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1157; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1187;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void traceWithClassThrowsNoWebApplicationExceptionForResponseTest()
      throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("tracenotok");
    Future<Response> future = rx.trace(Response.class).toCompletableFuture();
    checkFutureResponseStatus(future, Status.NOT_ACCEPTABLE);
  }

  /*
   * @testName: traceWithGenericTypeStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1158; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1188;
   * 
   * @test_Strategy: Invoke HTTP trace method for the current request
   */
  public Future<String> traceWithGenericTypeStringTest() throws Fault {
    GenericType<String> generic = createGeneric(String.class);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("trace");
    Future<String> future = rx.trace(generic).toCompletableFuture();
    checkFutureString(future, "trace");
    return future;
  }

  /*
   * @testName: traceWithGenericTypeResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1158; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1188;
   * 
   * @test_Strategy: Invoke HTTP trace method for the current request
   */
  public Future<Response> traceWithGenericTypeResponseTest() throws Fault {
    GenericType<Response> generic = createGeneric(Response.class);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("trace");
    Future<Response> future = rx.trace(generic).toCompletableFuture();
    checkFutureOkResponse(future);
    return future;
  }

  /*
   * @testName: traceWithGenericTypeThrowsProcessingExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1158; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1188;
   * 
   * @test_Strategy: ResponseProcessingException - in case processing of a
   * received HTTP response fails (e.g. in a filter or during conversion of the
   * response entity data to an instance of a particular Java type).
   */
  public void traceWithGenericTypeThrowsProcessingExceptionTest() throws Fault {
    _hostname = NONEXISTING_SITE;
    GenericType<String> generic = createGeneric(String.class);
    CompletionStageRxInvoker rx = startRxInvokerForMethod("trace");
    Future<String> future = rx.trace(generic).toCompletableFuture();
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
  }

  /*
   * @testName: traceWithGenericTypeThrowsWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1158; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1188;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void traceWithGenericTypeThrowsWebApplicationExceptionTest()
      throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("tracenotok");
    GenericType<String> generic = createGeneric(String.class);
    Future<String> future = rx.trace(generic).toCompletableFuture();
    assertExceptionWithWebApplicationExceptionIsThrownAndLog(future);
  }

  /*
   * @testName:
   * traceWithGenericTypeThrowsNoWebApplicationExceptionForResponseTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1158; JAXRS:JAVADOC:1162; JAXRS:JAVADOC:1188;
   * 
   * @test_Strategy: WebApplicationException - in case the response status code
   * of the response returned by the server is not successful and the specified
   * response type is not Response.
   */
  public void traceWithGenericTypeThrowsNoWebApplicationExceptionForResponseTest()
      throws Fault {
    CompletionStageRxInvoker rx = startRxInvokerForMethod("tracenotok");
    GenericType<Response> generic = createGeneric(Response.class);
    Future<Response> future = rx.trace(generic).toCompletableFuture();
    checkFutureResponseStatus(future, Status.NOT_ACCEPTABLE);
  }

  // ///////////////////////////////////////////////////////////////////////
  // utility methods

  /**
   * Create CompletionStageRxInvoker for given resource method and start time
   */
  protected CompletionStageRxInvoker startRxInvokerForMethod(
      String methodName) {
    return startBuilderForMethod(methodName).rx();
  }

  protected Invocation.Builder startBuilderForMethod(String methodName) {
    Client client = ClientBuilder.newClient();
    client.register(new JdkLoggingFilter(false));
    WebTarget target = client.target(getAbsoluteUrl(methodName));
    Invocation.Builder ib = target.request();
    return ib;
  }

  protected void assertOkAndLog(Response response, Status status) throws Fault {
    assertFault(response.getStatus() == status.getStatusCode(),
        "Returned unexpected status", response.getStatus());
    String msg = new StringBuilder().append("Returned status ")
        .append(status.getStatusCode()).append(" (").append(status.name())
        .append(")").toString();
    TestUtil.logMsg(msg);
  }

  protected void checkFutureResponseStatus(Future<Response> future,
      Status status) throws Fault {
    Response response = null;
    try {
      response = future.get();
    } catch (Exception e) {
      throw new Fault(e);
    }
    assertOkAndLog(response, status);
  }

  protected void checkFutureOkResponse(Future<Response> future) throws Fault {
    assertFault(!future.isDone(), "Future cannot be done, yet!");
    checkFutureResponseStatus(future, Status.OK);
  }

  protected void checkFutureString(Future<String> future, String expectedValue)
      throws Fault {
    assertFault(!future.isDone(), "Future cannot be done, yet!");
    String value = null;
    try {
      value = future.get();
    } catch (Exception e) {
      throw new Fault(e);
    }
    assertFault(expectedValue.equalsIgnoreCase(value), "expected value",
        expectedValue, "differes from acquired value", value);
  }

  protected void //
      assertExceptionWithWebApplicationExceptionIsThrownAndLog(Future<?> future)
          throws Fault {
    try {
      future.get();
      throw new Fault("ExecutionException has not been thrown");
    } catch (ExecutionException e) {
      assertWebApplicationExceptionIsCauseAndLog(e);
    } catch (InterruptedException e) {
      throw new Fault("Unexpected exception thrown", e);
    }
  }

  protected void assertExceptionWithProcessingExceptionIsThrownAndLog(
      Future<?> future) throws Fault {
    try {
      future.get();
      throw new Fault("ExecutionException has not been thrown");
    } catch (ExecutionException e) {
      assertProcessingExceptionIsCauseAndLog(e);
    } catch (InterruptedException e) {
      throw new Fault("Unexpected exception thrown", e);
    }
  }

  protected void //
      assertProcessingExceptionIsCauseAndLog(ExecutionException e)
          throws Fault {
    logMsg("ExecutionException has been thrown as expected", e);
    assertCause(e, javax.ws.rs.ProcessingException.class,
        "ExecutionException wrapped", e.getCause(),
        "rather then ProcessingException");
    logMsg("ExecutionException.getCause is ProcessingException as expected");
  }

  protected void //
      assertWebApplicationExceptionIsCauseAndLog(ExecutionException e)
          throws Fault {
    logMsg("ExecutionException has been thrown as expected", e);
    assertCause(e, WebApplicationException.class, "ExecutionException wrapped",
        e.getCause(), "rather then WebApplicationException");
    logMsg(
        "ExecutionException.getCause is WebApplicationException as expected");
  }

  protected <T> GenericType<T> createGeneric(Class<T> clazz) {
    return new GenericType<T>(clazz);
  }

}
