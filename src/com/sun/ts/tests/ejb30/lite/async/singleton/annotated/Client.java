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

/*
 * $Id$
 */
package com.sun.ts.tests.ejb30.lite.async.singleton.annotated;

import java.util.concurrent.ExecutionException;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.lite.async.common.annotated.Async2IF;
import com.sun.ts.tests.ejb30.lite.async.common.annotated.AsyncIF;

public class Client extends
    com.sun.ts.tests.ejb30.lite.async.common.annotated.AnnotatedClientBase {

  @Override
  @EJB(beanInterface = AsyncBean.class, name = "noInterface", beanName = "AsyncBean")
  protected void setNoInterface(AsyncIF noInterface) {
    this.noInterface = noInterface;
  }

  @Override
  @EJB(name = "interface1", beanName = "Async2Bean")
  public void setInterface1(AsyncIF interface1) {
    this.interface1 = interface1;
  }

  @Override
  @EJB(name = "interface2", beanName = "Async2Bean")
  public void setInterface2(Async2IF interface2) {
    this.interface2 = interface2;
  }

  @Override
  protected void assertBeanInstances(AsyncIF b)
      throws ExecutionException, InterruptedException {
    for (int i = 0; i < DESTROYED_COUNT; i++) {
      assertEquals("verify still the same instance ", true,
          b.isErrorOccurredInInstance().get());
    }
  }

  /*
   * @testName: addAway
   * 
   * @test_Strategy: asynchronous invocations on stateless, stateful, and
   * singleton. The asynchronous method returns void, and updates the result in
   * a singleton, which is retrieved by the client.
   */
  /*
   * @testName: voidRuntimeException
   * 
   * @test_Strategy: asynchronous invocations on stateless, stateful, and
   * singleton. The asynchronous method throws RuntimeException, which is not
   * visible to the client, and no effect on client execution. Also verify that
   * stateless bean instance is discarded after such a RuntimeException.
   */
  /*
   * @testName: futureRuntimeException
   * 
   * @test_Strategy: asynchronous invocations on stateless, stateful, and
   * singleton. The asynchronous method with Future return type throws
   * RuntimeException, which is retrieved with Future.get(). Also verify that
   * stateless bean instance is discarded after such a RuntimeException.
   */
  /*
   * @testName: futureError
   * 
   * @test_Strategy: asynchronous invocations on stateless, stateful, and
   * singleton. The asynchronous method with Future return type throws
   * AssertionError, which is retrieved with Future.get(). Also verify that
   * stateless bean instance is discarded after such an Error.
   */
  /*
   * @testName: futureException
   * 
   * @test_Strategy: asynchronous invocations on stateless, stateful, and
   * singleton. The asynchronous method with Future return type throws checked
   * Exception, which is retrieved with Future.get().
   */
  /*
   * @testName: futureValueList
   * 
   * @test_Strategy: async method returns Future<List<String>>
   */
  /*
   * @testName: addReturn
   * 
   * @test_Strategy: asynchronous invocations on stateless, stateful, and
   * singleton. The asynchronous method returns Future.
   */
  /*
   * @testName: addSyncThrowException
   * 
   * @test_Strategy: Some methods on the interface are annotated as async and
   * some are not. synchronous/blocking invocations on stateless, stateful, and
   * singleton. The synchronous method throws TestFailedException, which should
   * be received by client.
   */
  /*
   * @testName: addSyncReturn
   * 
   * @test_Strategy: Some methods on the interface are annotated as async and
   * some are not. synchronous/blocking invocations on stateless, stateful, and
   * singleton. The synchronous method should block for the return value.
   */
  /*
   * @testName: addReturnWaitMillis
   * 
   * @test_Strategy: Some methods on the interface are annotated as async and
   * some are not. Asynchronous invocations on stateless, stateful, and
   * singleton. The asynchronous method should return immediately.
   */

  /*
   * @testName: cancelMayInterruptIfRunningFalse
   * 
   * @test_Strategy: cancel an async invocation with mayInterruptIfRunning set
   * to true or false. If the client's cancel request is sent before the
   * previous async method is dispatched, the async method will not be executed.
   * So need to make sure the cancel request is not sent until the bean has
   * started processing the first async method.
   * 
   * The bean method also needs to wait for the client's cancel request, and
   * then call SessionContext.wasCancelCalled.
   */
  /*
   * @testName: cancelMayInterruptIfRunningTrue
   * 
   * @test_Strategy: see cancelMayInterruptIfRunningFalse
   */
  /*
   * @testName: passByValueOrReference
   * 
   * @test_Strategy:
   */
  /*
   * @testName: passByValueOrReferenceAsync
   * 
   * @test_Strategy:
   */
}
