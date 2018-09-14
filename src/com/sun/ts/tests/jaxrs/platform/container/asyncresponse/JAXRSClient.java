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

package com.sun.ts.tests.jaxrs.platform.container.asyncresponse;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Future;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.ts.tests.jaxrs.common.client.JaxrsCommonClient;
import com.sun.ts.tests.jaxrs.common.client.JdkLoggingFilter;
import com.sun.ts.tests.jaxrs.common.util.JaxrsUtil;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
/*
 * These test are in the platform package since async is not available in 
 * Servlet 2.5 spec by default
 */
public class JAXRSClient extends JaxrsCommonClient {

  private static final long serialVersionUID = 8496602126019947248L;

  public JAXRSClient() {
    setContextRoot("/jaxrs_platform_container_asyncresponse_web/resource");
  }

  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  /*
   * @testName: cancelVoidTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:980;
   * 
   * @test_Strategy: Cancel the suspended request processing. When a request
   * processing is cancelled using this method, the JAX-RS implementation MUST
   * indicate to the client that the request processing has been cancelled by
   * sending back a HTTP 503 (Service unavailable) error response.
   */
  public void cancelVoidTest() throws Fault {
    invokeClear();
    Future<Response> suspend = invokeRequest("suspend");
    Future<Response> cancel = invokeRequest("cancelvoid?stage=0");
    assertStatus(getResponse(suspend), Status.SERVICE_UNAVAILABLE);
    assertString(cancel, Resource.TRUE);
  }

  /*
   * @testName: cancelVoidOnResumedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:980;
   * 
   * @test_Strategy: Cancel the suspended request processing. Invoking a
   * cancel(...) method on an asynchronous response instance that has already
   * been resumed has no effect and the method call is ignored while returning
   * false when resumed.
   */
  public void cancelVoidOnResumedTest() throws Fault {
    suspendResumeTest();
    Future<Response> cancel = invokeRequest("cancelvoid?stage=1");
    assertString(cancel, Resource.FALSE);
  }

  /*
   * @testName: cancelVoidOnCanceledTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:980;
   * 
   * @test_Strategy: Cancel the suspended request processing. Invoking a
   * cancel(...) method on an asynchronous response instance that has already
   * been resumed has no effect and the method call is ignored while returning
   * true when canceled.
   */
  public void cancelVoidOnCanceledTest() throws Fault {
    cancelVoidTest();
    Future<Response> cancel = invokeRequest("cancelvoid?stage=1");
    assertString(cancel, Resource.TRUE);
  }

  /*
   * @testName: resumeCanceledTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:980;
   * 
   * @test_Strategy: returns false in case the request processing is not
   * suspended and could not be resumed.
   */
  public void resumeCanceledTest() throws Fault {
    cancelVoidTest();
    Future<Response> resumeCanceled = invokeRequest("resume?stage=1", "");
    assertString(resumeCanceled, Resource.FALSE);
  }

  /*
   * @testName: cancelIntTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:980;
   * 
   * @test_Strategy: Cancel the suspended request processing. When a request
   * processing is cancelled using this method, the JAX-RS implementation MUST
   * indicate to the client that the request processing has been cancelled by
   * sending back a HTTP 503 (Service unavailable) error response with a
   * Retry-After header set to the value provided by the method parameter.
   */
  public void cancelIntTest() throws Fault {
    String seconds = "20";
    invokeClear();

    Future<Response> suspend = invokeRequest("suspend");
    Future<Response> cancel = invokeRequest("cancelretry?stage=0", seconds);
    Response response = getResponse(suspend);
    assertStatus(response, Status.SERVICE_UNAVAILABLE);
    assertString(cancel, Resource.TRUE);
    String retry = response.getHeaderString(HttpHeaders.RETRY_AFTER);
    assertEquals(seconds, retry, "Unexpected", HttpHeaders.RETRY_AFTER,
        "header value received", retry, "expected", seconds);
    logMsg("Found expected", HttpHeaders.RETRY_AFTER, "=", retry);
  }

  /*
   * @testName: cancelIntOnResumedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:981;
   * 
   * @test_Strategy: Cancel the suspended request processing. Invoking a
   * cancel(...) method on an asynchronous response instance that has already
   * been resumed has no effect and the method call is ignored while returning
   * false when resumed
   */
  public void cancelIntOnResumedTest() throws Fault {
    suspendResumeTest();
    Future<Response> cancel = invokeRequest("cancelretry?stage=1", "20");
    assertString(cancel, Resource.FALSE);
  }

  /*
   * @testName: cancelIntOnCanceledTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:981;
   * 
   * @test_Strategy: Cancel the suspended request processing. Invoking a
   * cancel(...) method on an asynchronous response instance that has already
   * been resumed has no effect and the method call is ignored while returning
   * true when canceled
   */
  public void cancelIntOnCanceledTest() throws Fault {
    cancelVoidTest();
    Future<Response> cancel = invokeRequest("cancelretry?stage=1", "20");
    assertString(cancel, Resource.TRUE);
  }

  /*
   * @testName: resumeCanceledIntTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:981;
   * 
   * @test_Strategy: returns false in case the request processing is not
   * suspended and could not be resumed.
   */
  public void resumeCanceledIntTest() throws Fault {
    cancelIntTest();
    Future<Response> resume = invokeRequest("resume?stage=1", "");
    assertString(resume, Resource.FALSE);
  }

  /*
   * @testName: cancelDateTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:982;
   * 
   * @test_Strategy: Cancel the suspended request processing. When a request
   * processing is cancelled using this method, the JAX-RS implementation MUST
   * indicate to the client that the request processing has been cancelled by
   * sending back a HTTP 503 (Service unavailable) error response with a
   * Retry-After header set to the value provided by the method parameter.
   */
  public void cancelDateTest() throws Fault {
    long milis = (System.currentTimeMillis() / 1000) * 1000 + 20000;
    invokeClear();

    Future<Response> suspend = invokeRequest("suspend");
    Future<Response> cancel = invokeRequest("canceldate?stage=0", milis);
    Response response = getResponse(suspend);
    assertStatus(response, Status.SERVICE_UNAVAILABLE);
    assertString(cancel, Resource.TRUE);
    String header = response.getHeaderString(HttpHeaders.RETRY_AFTER);
    TimeZone timezone = JaxrsUtil.findTimeZoneInDate(header);
    Date retry = null;
    try {
      retry = JaxrsUtil.createDateFormat(timezone).parse(header);
    } catch (ParseException e) {
      throw new Fault(e);
    }
    assertEquals(new Date(milis), retry, "Unexpected", HttpHeaders.RETRY_AFTER,
        "header value received", retry.getTime(), "expected", milis);
    logMsg("Found expected", HttpHeaders.RETRY_AFTER, "=", header);
  }

  /*
   * @testName: cancelDateOnResumedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:982;
   * 
   * @test_Strategy: Cancel the suspended request processing. Invoking a
   * cancel(...) method on an asynchronous response instance that has already
   * been resumed has no effect and the method call is ignored while returning
   * false when resumed
   */
  public void cancelDateOnResumedTest() throws Fault {
    suspendResumeTest();
    Future<Response> cancel = invokeRequest("canceldate?stage=1",
        System.currentTimeMillis());
    assertString(cancel, Resource.FALSE);
  }

  /*
   * @testName: cancelDateOnCanceledTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:982;
   * 
   * @test_Strategy: Cancel the suspended request processing. Invoking a
   * cancel(...) method on an asynchronous response instance that has already
   * been resumed has no effect and the method call is ignored while returning
   * true when canceled
   */
  public void cancelDateOnCanceledTest() throws Fault {
    cancelVoidTest();
    Future<Response> cancel = invokeRequest("canceldate?stage=1",
        System.currentTimeMillis());
    assertString(cancel, Resource.TRUE);
  }

  /*
   * @testName: resumeCanceledDateTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:982;
   * 
   * @test_Strategy: returns false in case the request processing is not
   * suspended and could not be resumed.
   */
  public void resumeCanceledDateTest() throws Fault {
    cancelDateTest();
    Future<Response> resumeResumed = invokeRequest("resume?stage=1", "");
    assertString(resumeResumed, Resource.FALSE);
  }

  /*
   * @testName: isCanceledWhenCanceledTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:628;
   * 
   * @test_Strategy: Check if the asynchronous response instance has been
   * cancelled. Method returns true if this asynchronous response has been
   * canceled before completion.
   */
  public void isCanceledWhenCanceledTest() throws Fault {
    cancelVoidTest();
    Future<Response> is = invokeRequest("iscanceled?stage=1");
    assertString(is, Resource.TRUE);
  }

  /*
   * @testName: isCanceledWhenSuspendedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:628;
   * 
   * @test_Strategy: Check if the asynchronous response instance has been
   * cancelled. Method returns true if this asynchronous response has been
   * canceled before completion.
   */
  public void isCanceledWhenSuspendedTest() throws Fault {
    invokeClear();
    invokeRequest("suspend");
    Future<Response> is = invokeRequest("iscanceled?stage=0");
    assertString(is, Resource.FALSE);
  }

  /*
   * @testName: isCanceledWhenResumedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:628;
   * 
   * @test_Strategy: Check if the asynchronous response instance has been
   * cancelled. Method returns true if this asynchronous response has been
   * canceled before completion.
   */
  public void isCanceledWhenResumedTest() throws Fault {
    suspendResumeTest();
    Future<Response> is = invokeRequest("iscanceled?stage=1");
    assertString(is, Resource.FALSE);
  }

  /*
   * @testName: isDoneWhenResumedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:629;
   * 
   * @test_Strategy: Check if the processing of a request this asynchronous
   * response instance belongs to has finished. Method returns true if the
   * processing of a request this asynchronous response is bound to is finished.
   * The request processing may be finished due to a normal termination, a
   * suspend timeout, or cancellation -- in all of these cases, this method will
   * return true.
   */
  public void isDoneWhenResumedTest() throws Fault {
    suspendResumeTest();
    Future<Response> is = invokeRequest("isdone?stage=1");
    assertString(is, Resource.TRUE);
  }

  /*
   * @testName: isDoneWhenSuspendedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:629;
   * 
   * @test_Strategy: Check if the processing of a request this asynchronous
   * response instance belongs to has finished. Method returns true if the
   * processing of a request this asynchronous response is bound to is finished.
   * The request processing may be finished due to a normal termination, a
   * suspend timeout, or cancellation -- in all of these cases, this method will
   * return true.
   */
  public void isDoneWhenSuspendedTest() throws Fault {
    invokeClear();
    invokeRequest("suspend");
    Future<Response> is = invokeRequest("isdone?stage=0");
    assertString(is, Resource.FALSE);
  }

  /*
   * @testName: isDoneWhenCanceledTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:629;
   * 
   * @test_Strategy: Check if the processing of a request this asynchronous
   * response instance belongs to has finished. Method returns true if the
   * processing of a request this asynchronous response is bound to is finished.
   * The request processing may be finished due to a normal termination, a
   * suspend timeout, or cancellation -- in all of these cases, this method will
   * return true.
   */
  public void isDoneWhenCanceledTest() throws Fault {
    cancelVoidTest();
    Future<Response> is = invokeRequest("isdone?stage=1");
    assertString(is, Resource.TRUE);
  }

  /*
   * @testName: isDoneWhenTimedOutTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:629;
   * 
   * @test_Strategy: Check if the processing of a request this asynchronous
   * response instance belongs to has finished. Method returns true if the
   * processing of a request this asynchronous response is bound to is finished.
   * The request processing may be finished due to a normal termination, a
   * suspend timeout, or cancellation -- in all of these cases, this method will
   * return true.
   */
  public void isDoneWhenTimedOutTest() throws Fault {
    setTimeoutTest();
    Future<Response> is = invokeRequest("isdone?stage=1");
    assertString(is, Resource.TRUE);
  }

  /*
   * @testName: isSuspendedWhenSuspendedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:630;
   * 
   * @test_Strategy: Check if the asynchronous response instance is in a
   * suspended state. Method returns true if this asynchronous response is still
   * suspended and has not finished processing yet (either by resuming or
   * canceling the response).
   */
  public void isSuspendedWhenSuspendedTest() throws Fault {
    invokeClear();
    invokeRequest("suspend");
    Future<Response> is = invokeRequest("issuspended?stage=0");
    assertString(is, Resource.TRUE);
  }

  /*
   * @testName: isSuspendedWhenCanceledTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:630;
   * 
   * @test_Strategy: Check if the asynchronous response instance is in a
   * suspended state. Method returns true if this asynchronous response is still
   * suspended and has not finished processing yet (either by resuming or
   * canceling the response).
   */
  public void isSuspendedWhenCanceledTest() throws Fault {
    cancelVoidTest();
    Future<Response> is = invokeRequest("issuspended?stage=1");
    assertString(is, Resource.FALSE);
  }

  /*
   * @testName: isSuspendedWhenResumedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:630;
   * 
   * @test_Strategy: Check if the asynchronous response instance is in a
   * suspended state. Method returns true if this asynchronous response is still
   * suspended and has not finished processing yet (either by resuming or
   * canceling the response).
   */
  public void isSuspendedWhenResumedTest() throws Fault {
    suspendResumeTest();
    Future<Response> is = invokeRequest("issuspended?stage=1");
    assertString(is, Resource.FALSE);
  }

  /*
   * @testName: suspendResumeTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:983;
   * 
   * @test_Strategy: Resume the suspended request processing using the provided
   * response data.
   */
  public void suspendResumeTest() throws Fault {
    invokeClear();
    String expectedResponse = "Expected response";
    Future<Response> suspend = invokeRequest("suspend");
    Future<Response> resume = invokeRequest("resume?stage=0", expectedResponse);
    assertString(resume, Resource.TRUE);
    assertString(suspend, expectedResponse);
  }

  /*
   * @testName: resumeAnyJavaObjectInputStreamTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:983;
   * 
   * @test_Strategy: Resume the suspended request processing using the provided
   * response data. The provided response data can be of any Java type that can
   * be returned from a JAX-RS resource method.
   */
  public void resumeAnyJavaObjectInputStreamTest() throws Fault {
    invokeClear();
    String expectedResponse = "Expected response";
    Future<Response> suspend = invokeRequest("suspend");
    Future<Response> resume = invokeRequest("resume?stage=0",
        new ByteArrayInputStream(expectedResponse.getBytes()));
    assertString(resume, Resource.TRUE);
    assertString(suspend, expectedResponse);
  }

  /*
   * @testName: resumeResumedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:983;
   * 
   * @test_Strategy: returns false in case the request processing is not
   * suspended and could not be resumed.
   */
  public void resumeResumedTest() throws Fault {
    suspendResumeTest(); // resume & store
    Future<Response> resumeResumed = invokeRequest("resume?stage=1", "");
    assertString(resumeResumed, Resource.FALSE);
  }

  /*
   * @testName: resumeWithCheckedExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:641;
   * 
   * @test_Strategy: Resume the suspended request processing using the provided
   * throwable. For the provided throwable same rules apply as for an exception
   * thrown by a JAX-RS resource method.
   */
  public void resumeWithCheckedExceptionTest() throws Fault {
    invokeClear();
    Future<Response> suspend = invokeRequest("suspend");
    Future<Response> resume = invokeRequest("resumechecked?stage=0");
    assertString(resume, Resource.TRUE);
    assertException(suspend, IOException.class);
  }

  /*
   * @testName: resumeWithRuntimeExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:984;
   * 
   * @test_Strategy: Resume the suspended request processing using the provided
   * throwable. For the provided throwable same rules apply as for an exception
   * thrown by a JAX-RS resource method.
   */
  public void resumeWithRuntimeExceptionTest() throws Fault {
    invokeClear();
    Future<Response> suspend = invokeRequest("suspend");
    Future<Response> resume = invokeRequest("resumeruntime?stage=0");
    assertString(resume, Resource.TRUE);
    assertException(suspend, RuntimeException.class);
  }

  /*
   * @testName: resumeWithExceptionReturnsFalseWhenResumedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:984;
   * 
   * @test_Strategy: returns false in case the request processing is not
   * suspended and could not be resumed.
   */
  public void resumeWithExceptionReturnsFalseWhenResumedTest() throws Fault {
    suspendResumeTest();
    Future<Response> resume = invokeRequest("resumechecked?stage=1");
    assertString(resume, Resource.FALSE);
  }

  /*
   * @testName: setTimeoutTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1034; JAXRS:SPEC:103; JAXRS:SPEC:104;
   * 
   * @test_Strategy: The new suspend timeout values override any timeout value
   * previously specified.
   * 
   * JAX-RS implementations are REQUIRED to generate a
   * ServiceUnavailableException, a subclass of WebApplicationException with its
   * status set to 503, if the timeout value is reached and no timeout handler
   * is registered.
   * 
   * The exception MUST be processed as described in section 3.3.4.
   */
  public void setTimeoutTest() throws Fault {
    invokeClear();
    Future<Response> suspend = invokeRequest("suspend");
    Future<Response> setTimeout = invokeRequest("settimeout?stage=0", 200);
    assertStatus(getResponse(setTimeout), Status.NO_CONTENT);
    // WebApplication exception with 503 is caught by
    // ServiceUnavailableExceptionMapper
    Response fromMapper = getResponse(suspend);
    assertStatus(fromMapper, Status.REQUEST_TIMEOUT);
    String entity = fromMapper.readEntity(String.class);
    assertContains(entity, 503);
    logMsg("Found expected status 503");
  }

  /*
   * @testName: updateTimeoutTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1034; JAXRS:SPEC:103; JAXRS:SPEC:104;
   * 
   * @test_Strategy: Set/update the suspend timeout.
   * 
   * JAX-RS implementations are REQUIRED to generate a
   * ServiceUnavailableException, a subclass of WebApplicationException with its
   * status set to 503, if the timeout value is reached and no timeout handler
   * is registered.
   * 
   * The exception MUST be processed as described in section 3.3.4.
   */
  public void updateTimeoutTest() throws Fault {
    invokeClear();
    Future<Response> suspend = invokeRequest("suspend");
    Future<Response> setTimeout = invokeRequest("settimeout?stage=0", 600000);
    assertStatus(getResponse(setTimeout), Status.NO_CONTENT);
    assertFalse(suspend.isDone(), "Suspended AsyncResponse already received");
    setTimeout = invokeRequest("settimeout?stage=1", 200);
    assertStatus(getResponse(setTimeout), Status.NO_CONTENT);
    // WebApplication exception with 503 is caught by
    // ServiceUnavailableExceptionMapper
    Response fromMapper = getResponse(suspend);
    assertStatus(fromMapper, Status.REQUEST_TIMEOUT);
    String entity = fromMapper.readEntity(String.class);
    assertContains(entity, 503);
    logMsg("Found expected status 503");
  }

  /*
   * @testName: handleTimeOutWaitsForeverTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:725; JAXRS:JAVADOC:645; JAXRS:SPEC:105;
   * 
   * @test_Strategy: Invoked when the suspended asynchronous response is about
   * to time out.
   * 
   * Set/replace a time-out handler for the suspended asynchronous response.
   * 
   * If a registered timeout handler resets the timeout value or resumes the
   * connection and returns a response, JAX-RS implementations MUST NOT generate
   * an exception.
   */
  public void handleTimeOutWaitsForeverTest() throws Fault {
    String responseMsg = "handleTimeOutWaitsForeverTest";
    invokeClear();
    Future<Response> suspend = invokeRequest("suspend");
    Future<Response> setTimeout = invokeRequest("timeouthandler?stage=0", 1);
    Future<Response> resume = invokeRequest("resume?stage=1", responseMsg);
    assertStatus(getResponse(setTimeout), Status.NO_CONTENT);
    assertString(resume, Resource.TRUE);
    assertString(suspend, responseMsg);
  }

  /*
   * @testName: handleTimeoutCancelsTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:725; JAXRS:JAVADOC:645;
   * 
   * @test_Strategy: Invoked when the suspended asynchronous response is about
   * to time out.
   * 
   * Set/replace a time-out handler for the suspended asynchronous response.
   */
  public void handleTimeoutCancelsTest() throws Fault {
    invokeClear();
    Future<Response> suspend = invokeRequest("suspend");
    Future<Response> setTimeout = invokeRequest("timeouthandler?stage=0", 2);
    assertStatus(getResponse(setTimeout), Status.NO_CONTENT);
    assertStatus(getResponse(suspend), Status.SERVICE_UNAVAILABLE);
    Future<Response> resume = invokeRequest("issuspended?stage=1");
    assertString(resume, Resource.FALSE);

  }

  /*
   * @testName: handleTimeoutResumesTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:725; JAXRS:JAVADOC:645; JAXRS:SPEC:105;
   * 
   * @test_Strategy: Invoked when the suspended asynchronous response is about
   * to time out.
   * 
   * Set/replace a time-out handler for the suspended asynchronous response.
   * 
   * If a registered timeout handler resets the timeout value or resumes the
   * connection and returns a response, JAX-RS implementations MUST NOT generate
   * an exception.
   */
  public void handleTimeoutResumesTest() throws Fault {
    invokeClear();
    Future<Response> suspend = invokeRequest("suspend");
    Future<Response> setTimeout = invokeRequest("timeouthandler?stage=0", 3);
    assertStatus(getResponse(setTimeout), Status.NO_CONTENT);
    assertString(suspend, Resource.RESUMED);
    Future<Response> resume = invokeRequest("issuspended?stage=1");
    assertString(resume, Resource.FALSE);

  }

  // ////////////////////////////////////////////////////////////////////////////

  private void invokeClear() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "clear"));
    setProperty(Property.STATUS_CODE, getStatusCode(Status.NO_CONTENT));
    invoke();
  }

  private Future<Response> invokeRequest(String resource) {
    AsyncInvoker async = createAsyncInvoker(resource);
    Future<Response> future = async.get();
    return future;
  }

  private <T> Future<Response> invokeRequest(String resource, T entity) {
    AsyncInvoker async = createAsyncInvoker(resource);
    Future<Response> future = async
        .post(Entity.entity(entity, MediaType.TEXT_PLAIN_TYPE));
    return future;
  }

  private WebTarget createWebTarget(String resource) {
    Client client = ClientBuilder.newClient();
    client.register(new JdkLoggingFilter(true));
    WebTarget target = client.target(getAbsoluteUrl() + "/" + resource);
    return target;
  }

  private AsyncInvoker createAsyncInvoker(String resource) {
    WebTarget target = createWebTarget(resource);
    AsyncInvoker async = target.request().async();
    return async;
  }

  private static Response getResponse(Future<Response> future) throws Fault {
    Response response = null;
    try {
      response = future.get();
    } catch (Exception e) {
      throw new Fault(e);
    }
    return response;
  }

  private static void assertStatus(Response response, Status status)
      throws Fault {
    assertEqualsInt(response.getStatus(), status.getStatusCode(),
        "Unexpected status code received", response.getStatus(), "expected was",
        status);
    logMsg("Found expected status", status);
  }

  private static void assertString(Future<Response> future, String check)
      throws Fault {
    Response response = getResponse(future);
    assertStatus(response, Status.OK);
    String content = response.readEntity(String.class);
    assertEquals(check, content, "Unexpected response content", content);
    logMsg("Found expected string", check);
  }

  private static void assertException(Future<Response> future,
      Class<? extends Throwable> e) throws Fault {
    String clazz = e.getName();
    Response response = getResponse(future);
    assertStatus(response, Status.NOT_ACCEPTABLE);
    assertContains(response.readEntity(String.class), clazz, clazz,
        "not thrown");
    logMsg(clazz, "has been thrown as expected");
  }
}
