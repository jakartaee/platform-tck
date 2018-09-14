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

package com.sun.ts.tests.jaxrs.api.client.invocationcallback;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javax.ws.rs.ProcessingException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.InvocationCallback;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

import com.sun.ts.tests.jaxrs.common.JAXRSCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JAXRSCommonClient {

  private static final long serialVersionUID = 8164327856558890177L;

  private static final int WAIT_SECONDS = 5;

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  /* Run test */

  /*
   * @testName: completedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:539;
   * 
   * @test_Strategy: Called when the invocation was successfully completed.
   */
  public void completedTest() throws Fault {
    AtomicInteger ai = new AtomicInteger(0);
    CountDownLatch countDownLatch = new CountDownLatch(1);
    InvocationCallback<String> callback = createCallback(ai, countDownLatch);
    Invocation.Builder builder = createInvocationBuilder();
    Invocation invocation = builder.buildGet();
    Future<String> future = invocation.submit(callback);
    try {
      countDownLatch.await(WAIT_SECONDS, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      fault(e);
    }
    assertContains(future, "get");
    assertEqualsInt(10, ai.get(), "Unexpected result from InvocationCallback",
        ai.get(), "expected was 10");
    logMsg("InvocationCallback#completed has been called as expected");
  }

  /*
   * @testName: failedTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:979;
   * 
   * @test_Strategy: Called when the invocation has failed for any reason.
   */
  public void failedTest() throws Fault {
    AtomicInteger ai = new AtomicInteger(0);
    CountDownLatch countDownLatch = new CountDownLatch(1);
    InvocationCallback<String> callback = createCallback(ai, countDownLatch);
    Invocation.Builder builder = createInvocationBuilder(null);
    Invocation invocation = builder.buildGet();
    Future<String> future = invocation.submit(callback);
    try {
      countDownLatch.await(WAIT_SECONDS, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      fault(e);
    }
    assertExceptionWithProcessingExceptionIsThrownAndLog(future);
    assertEqualsInt(100, ai.get(), "Unexpected result from InvocationCallback",
        ai.get(), "expected was 100");
    logMsg("InvocationCallback#failed has been called as expected");
  }

  // ///////////////////////////////////////////////////////////////////////

  /**
   * Simulates server side
   * 
   * @return Response containing request method and entity
   */
  private static ClientRequestFilter createRequestFilter() {
    ClientRequestFilter filter = new ClientRequestFilter() {
      @Override
      public void filter(ClientRequestContext ctx) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append(ctx.getMethod()).append(";");
        if (ctx.hasEntity())
          sb.append(ctx.getEntity()).append(";");
        Response r = Response.ok(sb.toString()).build();
        ctx.abortWith(r);
      }
    };
    return filter;
  }

  private static Invocation.Builder createInvocationBuilder(
      ClientRequestFilter filter) {
    Client client = ClientBuilder.newClient();
    if (filter != null)
      client.register(filter);
    WebTarget target = client.target("http://cts.tck:888");
    Invocation.Builder builder = target.request();
    return builder;
  }

  private static Invocation.Builder createInvocationBuilder() {
    return createInvocationBuilder(createRequestFilter());
  }

  private static InvocationCallback<String> createCallback(
      final AtomicInteger ai, final CountDownLatch latch) {
    InvocationCallback<String> callback = null;
    callback = new InvocationCallback<String>() {
      @Override
      public void completed(String arg0) {
        ai.set(ai.get() + 10);
        latch.countDown();
      }

      @Override
      public void failed(Throwable throwable) {
        ai.set(ai.get() + 100);
        latch.countDown();
      }
    };
    return callback;
  }

  private static void assertContains(String responseEntity, String what)
      throws Fault {
    assertTrue(responseEntity.toLowerCase().contains(what.toLowerCase()),
        responseEntity, "does not contain expected", what);
    logMsg("Found expected", what);
  }

  private static void assertContains(Future<String> future, String what)
      throws Fault {
    String responseEntity = null;
    try {
      responseEntity = future.get();
    } catch (Exception e) {
      throw new Fault(e);
    }
    assertContains(responseEntity, what);
  }

  private void assertExceptionWithProcessingExceptionIsThrownAndLog(
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

  private void //
      assertProcessingExceptionIsCauseAndLog(ExecutionException e)
          throws Fault {
    logMsg("ExecutionException has been thrown as expected", e);
    assertTrue(hasWrapped(e, ProcessingException.class),
        "ExecutionException wrapped", e.getCause(),
        "rather then ProcessingException");
    logMsg("ExecutionException.getCause is ProcessingException as expected");
  }

  private static boolean //
      hasWrapped(Throwable parent, Class<? extends Throwable> wrapped) {
    while (parent.getCause() != null) {
      if (wrapped.isInstance(parent.getCause()))
        return true;
      parent = parent.getCause();
    }
    return false;
  }
}
