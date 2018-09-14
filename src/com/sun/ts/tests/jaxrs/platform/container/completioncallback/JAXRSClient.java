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

package com.sun.ts.tests.jaxrs.platform.container.completioncallback;

import java.io.IOException;
import java.util.concurrent.Future;

import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.sun.ts.tests.jaxrs.common.client.JaxrsCommonClient;
import com.sun.ts.tests.jaxrs.common.client.JdkLoggingFilter;
import com.sun.ts.tests.jaxrs.platform.container.asyncresponse.Resource;

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

  private static final long serialVersionUID = -234268814434213796L;

  public JAXRSClient() {
    setContextRoot("/jaxrs_platform_container_completioncallback_web/resource");
  }

  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  /*
   * @testName: argumentIsNullTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:985; JAXRS:JAVADOC:1032;
   * 
   * @test_Strategy: the argument is null when success
   * 
   * Register an asynchronous processing lifecycle callback instance
   */
  public void argumentIsNullTest() throws Fault {
    invokeClear();
    invokeReset();
    String expectedResponse = "Expected response";
    Future<Response> suspend = invokeRequest("suspend");
    Future<Response> register = invokeRequest("register?stage=0");
    Future<Response> resume = invokeRequest("resume?stage=1", expectedResponse);
    assertString(resume, Resource.TRUE);
    assertString(register, Resource.FALSE);
    assertString(suspend, expectedResponse);
    Future<Response> error = invokeRequest("error");
    assertString(error, SettingCompletionCallback.NULL);
  }

  /*
   * @testName: argumentContainsExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:985; JAXRS:JAVADOC:1032;
   * 
   * @test_Strategy: the argument is the thrown exception
   * 
   * Register an asynchronous processing lifecycle callback instance
   */
  public void argumentContainsExceptionTest() throws Fault {
    invokeClear();
    invokeReset();
    Future<Response> suspend = invokeRequest("suspend");

    Future<Response> register = invokeRequest("register?stage=0");
    logMsg("Register response test");
    assertString(register, Resource.FALSE);

    Future<Response> exception = invokeRequest("exception?stage=1");
    logMsg("Exception response test");
    Response response = getResponse(exception);
    logMsg("Gotten response status", response.getStatus());
    // assertException(exception, RuntimeException.class);

    logMsg("Suspend response test");
    assertStatus(getResponse(suspend), Status.INTERNAL_SERVER_ERROR);

    Future<Response> error = invokeRequest("error");
    assertString(error, RuntimeException.class.getName());
  }

  /*
   * @testName: argumentContainsExceptionWhenSendingIoExceptionTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:985; JAXRS:JAVADOC:1032;
   * 
   * @test_Strategy: the argument is the exception the AsyncResponse is resumed
   * with
   * 
   * Register an asynchronous processing lifecycle callback instance
   */
  public void argumentContainsExceptionWhenSendingIoExceptionTest()
      throws Fault {
    invokeClear();
    invokeReset();
    Future<Response> suspend = invokeRequest("suspend");

    Future<Response> register = invokeRequest("register?stage=0");
    logMsg("Register response test");
    assertString(register, Resource.FALSE);

    Future<Response> exception = invokeRequest("resumechecked?stage=1");
    logMsg("Exception response test");
    Response response = getResponse(exception);
    logMsg("Gotten response status", response.getStatus());
    // assertException(exception, RuntimeException.class);

    logMsg("Suspend response test");
    assertStatus(getResponse(suspend), Status.INTERNAL_SERVER_ERROR);

    Future<Response> error = invokeRequest("error");
    assertString(error, IOException.class.getName());
  }

  /*
   * @testName: argumentIsNullWhenRegistredClassTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:985; JAXRS:JAVADOC:1030;
   * 
   * @test_Strategy: the argument is the exception the AsyncResponse is resumed
   * with
   * 
   * Register an asynchronous processing lifecycle callback class
   */
  public void argumentIsNullWhenRegistredClassTest() throws Fault {
    invokeClear();
    invokeReset();
    String expectedResponse = "Expected response";
    Future<Response> suspend = invokeRequest("suspend");
    Future<Response> register = invokeRequest("registerclass?stage=0");
    Future<Response> resume = invokeRequest("resume?stage=1", expectedResponse);
    assertString(resume, Resource.TRUE);
    assertString(register, Resource.FALSE);
    assertString(suspend, expectedResponse);
    Future<Response> error = invokeRequest("error");
    assertString(error, SettingCompletionCallback.NULL);
  }

  /*
   * @testName: registerClassThrowsNpeTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1030;
   * 
   * @test_Strategy: Throws NPE in case the callback class is {@code null}.
   */
  public void registerClassThrowsNpeTest() throws Fault {
    invokeClear();
    invokeReset();
    invokeRequest("suspend");
    Future<Response> register = invokeRequest("registerclassthrows?stage=0");
    assertString(register, Resource.TRUE);
    logMsg("Register threw NullPointerException as expected");
  }

  /*
   * @testName: argumentContainsExceptionInTwoCallbackClassesTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:985; JAXRS:JAVADOC:1031;
   * 
   * @test_Strategy: the argument is the thrown exception
   * 
   * Register asynchronous processing lifecycle callback classes
   */
  public void argumentContainsExceptionInTwoCallbackClassesTest() throws Fault {
    invokeClear();
    invokeReset();
    Future<Response> suspend = invokeRequest("suspend");

    Future<Response> register = invokeRequest("registerclasses?stage=0");
    logMsg("Register response test");
    assertString(register, Resource.FALSE);

    Future<Response> exception = invokeRequest("exception?stage=1");
    logMsg("Exception response test");
    Response response = getResponse(exception);
    logMsg("Gotten response status", response.getStatus());
    // assertException(exception, RuntimeException.class);

    logMsg("Suspend response test");
    assertStatus(getResponse(suspend), Status.INTERNAL_SERVER_ERROR);

    Future<Response> error = invokeRequest("error");
    assertString(error, RuntimeException.class.getName());
    // Check the second callback contains the same exception
    // moreover, as assertString contains assertEquals, it checks
    // for the correct ordering of callback executions
    error = invokeRequest("seconderror");
    assertString(error, RuntimeException.class.getName());
  }

  /*
   * @testName: registerClassesThrowsNpeFirstArgNullTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1031;
   * 
   * @test_Strategy: Throws NPE in case any of the callback classes is {@code
   * null}. check first argument is null
   */
  public void registerClassesThrowsNpeFirstArgNullTest() throws Fault {
    invokeClear();
    invokeReset();
    invokeRequest("suspend");
    Future<Response> register = invokeRequest("registerclassesthrows1?stage=0");
    assertString(register, Resource.TRUE);
    logMsg("Register threw NullPointerException as expected");
  }

  /*
   * @testName: registerClassesThrowsNpeSecondArgNullTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1031;
   * 
   * @test_Strategy: Throws NPE in case any of the callback classes is {@code
   * null}. check second argument is null
   */
  public void registerClassesThrowsNpeSecondArgNullTest() throws Fault {
    invokeClear();
    invokeReset();
    invokeRequest("suspend");
    Future<Response> register = invokeRequest("registerclassesthrows2?stage=0");
    assertString(register, Resource.TRUE);
    logMsg("Register threw NullPointerException as expected");
  }

  /*
   * @testName: registerObjectThrowsNpeTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1032;
   * 
   * @test_Strategy: Throws NPE in case the callback instance is {@code null}.
   */
  public void registerObjectThrowsNpeTest() throws Fault {
    invokeClear();
    invokeReset();
    invokeRequest("suspend");
    Future<Response> register = invokeRequest("registerthrows?stage=0");
    assertString(register, Resource.TRUE);
    logMsg("Register threw NullPointerException as expected");
  }

  /*
   * @testName: argumentContainsExceptionInTwoCallbackInstancesTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:985; JAXRS:JAVADOC:1033;
   * 
   * @test_Strategy: the argument is the thrown exception
   * 
   * Register asynchronous processing lifecycle callback classes
   */
  public void argumentContainsExceptionInTwoCallbackInstancesTest()
      throws Fault {
    invokeClear();
    invokeReset();
    Future<Response> suspend = invokeRequest("suspend");

    Future<Response> register = invokeRequest("registerobjects?stage=0");
    logMsg("Register response test");
    assertString(register, Resource.FALSE);

    Future<Response> exception = invokeRequest("exception?stage=1");
    logMsg("Exception response test");
    Response response = getResponse(exception);
    logMsg("Gotten response status", response.getStatus());
    // assertException(exception, RuntimeException.class);

    logMsg("Suspend response test");
    assertStatus(getResponse(suspend), Status.INTERNAL_SERVER_ERROR);

    Future<Response> error = invokeRequest("error");
    assertString(error, RuntimeException.class.getName());
    // Check the second callback contains the same exception
    // moreover, as assertString contains assertEquals, it checks
    // for the correct ordering of callback executions
    error = invokeRequest("seconderror");
    assertString(error, RuntimeException.class.getName());
  }

  /*
   * @testName: registerInstancesThrowsNpeFirstArgNullTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1033;
   * 
   * @test_Strategy: Throws NPE in case any of the callback instances is {@code
   * null}. check first argument is null
   */
  public void registerInstancesThrowsNpeFirstArgNullTest() throws Fault {
    invokeClear();
    invokeReset();
    invokeRequest("suspend");
    Future<Response> register = invokeRequest("registerobjectsthrows1?stage=0");
    assertString(register, Resource.TRUE);
    logMsg("Register threw NullPointerException as expected");
  }

  /*
   * @testName: registerInstancesThrowsNpeSecondArgNullTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:1033;
   * 
   * @test_Strategy: Throws NPE in case any of the callback instances is {@code
   * null}. check second argument is null
   */
  public void registerInstancesThrowsNpeSecondArgNullTest() throws Fault {
    invokeClear();
    invokeReset();
    invokeRequest("suspend");
    Future<Response> register = invokeRequest("registerobjectsthrows2?stage=0");
    assertString(register, Resource.TRUE);
    logMsg("Register threw NullPointerException as expected");
  }

  // ////////////////////////////////////////////////////////////////////////////

  private void invokeClear() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "clear"));
    setProperty(Property.STATUS_CODE, getStatusCode(Status.NO_CONTENT));
    invoke();
  }

  private void invokeReset() throws Fault {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "reset"));
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
    assertEquals(check, content, "Unexpected response content", content,
        "expecting", check);
    logMsg("Found expected string", check);
  }

  @SuppressWarnings("unused")
  // should be used once the behaviour of exception throwing is verified.
  private static void assertException(Future<Response> future,
      Class<? extends Throwable> e) throws Fault {
    String clazz = e.getName();
    Response response = getResponse(future);
    assertStatus(response, Status.INTERNAL_SERVER_ERROR);
    assertContains(response.readEntity(String.class), clazz, clazz,
        "not thrown");
    logMsg(clazz, "has been thrown as expected");
  }
}
