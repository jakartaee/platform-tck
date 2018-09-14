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
package com.sun.ts.tests.ejb30.lite.async.common.annotated;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ejb.EJB;
import javax.ejb.EJBException;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.calc.CalculatorException;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.lite.async.common.AsyncClientBase;

abstract public class AnnotatedClientBase extends AsyncClientBase {
  protected int num1 = 10;

  protected int num2 = 20;

  protected int expectedSum = num1 + num2;

  protected final static int DESTROYED_COUNT = 50;

  @EJB(name = "annotatedMethodsIF", beanName = "AsyncAnnotatedMethodsBean")
  private AsyncAnnotatedMethodsIF annotatedMethodsIF;

  // cannot field-inject here since subdirectory may have different
  // beanInterface.
  // In stateful directory, the beanInterfaces are StatefulAsyncIF and
  // StatefulAsync2IF

  // Async2Bean, AsyncIF
  protected AsyncIF interface1;

  // Async2Bean, Async2IF
  protected Async2IF interface2;

  protected AsyncIF noInterface; // no-interface AsyncBean

  final protected AsyncIF[] beans = new AsyncIF[3];

  final protected AsyncAnnotatedMethodsCommonIF[] annotatedMethodsLocal = new AsyncAnnotatedMethodsCommonIF[1];

  abstract protected void setNoInterface(AsyncIF noInterface);

  abstract public void setInterface1(AsyncIF interface1);

  abstract public void setInterface2(Async2IF interface2);

  /**
   * Used to verify the status of bean instances after the async business method
   * throws RuntimeException or Error. For singleton, the same bean instance is
   * retained; for stateless, it should be a different bean instance; for
   * stateful, any subsequent access should result in
   * javax.ejb.NoSuchEJBException.
   * 
   * @param b
   * @throws ExecutionException
   * @throws InterruptedException
   */
  protected void assertBeanInstances(AsyncIF b)
      throws ExecutionException, InterruptedException {
  }

  @Override
  public void setup(String[] args, Properties p) {
    // For stateless and singleton test directories, it would be OK to
    // initialize the bean collections inside a
    // PostConstruct methods with the injected values. But for stateful, any
    // system exception will cause the
    // bean instance to be discarded and no longer available for the subsequent
    // test. So we will need to look
    // up a new bean reference for each test method. To be consistent, just use
    // the same approach for all
    // bean types.
    super.setup(args, p);
    beans[0] = (AsyncIF) lookup("interface1", "Async2Bean", AsyncIF.class);
    beans[1] = (AsyncIF) lookup("interface2", "Async2Bean", Async2IF.class);
    beans[2] = (AsyncIF) lookup("noInterface", "AsyncBean", null);

    annotatedMethodsLocal[0] = (AsyncAnnotatedMethodsCommonIF) lookup(
        "annotatedMethodsIF", "AsyncAnnotatedMethodsBean",
        AsyncAnnotatedMethodsIF.class);
  }

  /*
   * testName: addAway
   * 
   * @test_Strategy: asynchronous invocations on stateless, stateful, and
   * singleton. The asynchronous method returns void, and updates the result in
   * a singleton, which is retrieved by the client.
   */
  public void addAway() {
    for (int i = 0; i < beans.length; i++) {
      statusSingleton.removeResult(i);
      final int expected = expectedSum + i;
      beans[i].addAway(num1, num2, i);
      assertEquals("Check result stored in singleton.", expected,
          getAndResetResult(i));
    }
  }

  /*
   * testName: voidRuntimeException
   * 
   * @test_Strategy: asynchronous invocations on stateless, stateful, and
   * singleton. The asynchronous method throws RuntimeException, which is not
   * visible to the client, and no effect on client execution. Also verify that
   * stateless bean instance is discarded after such a RuntimeException.
   */

  public void voidRuntimeException()
      throws InterruptedException, ExecutionException {
    final Integer key = 1;
    for (final AsyncIF b : beans) {
      statusSingleton.removeResult(key);
      b.voidRuntimeException(key);
      final Integer completed = getAndResetResult(key);
      appendReason(
          "Is voidRuntimeException async method completed? " + completed);
      assertBeanInstances(b);
    }
  }

  /*
   * testName: futureRuntimeException
   * 
   * @test_Strategy: asynchronous invocations on stateless, stateful, and
   * singleton. The asynchronous method with Future return type throws
   * RuntimeException, which is retrieved with Future.get(). Also verify that
   * stateless bean instance is discarded after such a RuntimeException.
   */

  public void futureRuntimeException()
      throws InterruptedException, ExecutionException {
    for (final AsyncIF b : beans) {
      try {
        b.futureRuntimeException().get();
        throw new RuntimeException(
            "Expecting ExecutionException, but got none.");
      } catch (final ExecutionException ex) {
        appendReason("Got expected ExecutionException "
            + TestUtil.printStackTraceToString(ex));
        final EJBException ejbException = (EJBException) ex.getCause();
        assertBeanInstances(b);
      }
    }
  }

  /*
   * testName: futureError
   * 
   * @test_Strategy: asynchronous invocations on stateless, stateful, and
   * singleton. The asynchronous method with Future return type throws
   * AssertionError, which is retrieved with Future.get(). Also verify that
   * stateless bean instance is discarded after such an Error.
   */

  public void futureError() throws InterruptedException, ExecutionException {
    for (final AsyncIF b : beans) {
      try {
        b.futureError().get();
        throw new RuntimeException(
            "Expecting ExecutionException, but got none.");
      } catch (final ExecutionException ex) {
        appendReason(
            String.format("%nGot expected ExecutionException from %s%n%s", b,
                TestUtil.printStackTraceToString(ex)));
        final EJBException ejbException = (EJBException) ex.getCause();
        assertBeanInstances(b);
      }
    }
  }

  /*
   * testName: futureException
   * 
   * @test_Strategy: asynchronous invocations on stateless, stateful, and
   * singleton. The asynchronous method with Future return type throws checked
   * Exception, which is retrieved with Future.get().
   */

  public void futureException()
      throws InterruptedException, ExecutionException {
    final String expectedMsg = "futureException";
    for (final AsyncIF b : beans) {
      try {
        b.futureException().get();
        throw new RuntimeException(
            "Expecting ExecutionException, but got none.");
      } catch (final ExecutionException ex) {
        final Throwable cause = ex.getCause();
        final String actualMsg = cause.getMessage();
        if (actualMsg == null) {
          throw new RuntimeException("Unexpected exception ", cause);
        }
        assertEquals(null, expectedMsg,
            actualMsg.substring(0, expectedMsg.length()));
      } catch (final CalculatorException e) {
        throw new RuntimeException("Unexpected " + e);
      }
    }
  }

  /*
   * testName: futureValueList
   * 
   * @test_Strategy: async method returns Future<List<String>>
   */

  public void futureValueList()
      throws InterruptedException, ExecutionException {
    final List<String> la = null;
    final List<String> lb = Arrays.asList("b", "bb");
    final List<String> lc = Arrays.asList("c", "cc");

    for (final AsyncIF b : beans) {
      assertEquals(null, la, b.futureValueList(la).get());
      assertEquals(null, lb, b.futureValueList(lb).get());
      assertEquals(null, lc, b.futureValueList(lc).get());
    }
  }

  /*
   * testName: addReturn
   * 
   * @test_Strategy: asynchronous invocations on stateless, stateful, and
   * singleton. The asynchronous method returns Future.
   */

  public void addReturn() throws InterruptedException, ExecutionException {
    for (final AsyncIF bean : beans) {
      final Future<Integer> future = bean.addReturn(num1, num2);
      assertEquals("Check Future result " + future, expectedSum, future.get());
    }
  }

  /*
   * testName: addSyncThrowException
   * 
   * @test_Strategy: Some methods on the interface are annotated as async and
   * some are not. synchronous/blocking invocations on stateless, stateful, and
   * singleton. The synchronous method throws TestFailedException, which should
   * be received by client.
   */

  public void addSyncThrowException() {
    // only use local bean for this test, since the bean method
    // addSyncReturn calls addReturn, which returns a non-serializable
    // impl of Future<Integer>
    try {
      annotatedMethodsIF.addSyncThrowException(num1, num2);
      throw new RuntimeException(
          "Expecting TestFailedException, but got none.");
    } catch (final TestFailedException ex) {
      appendReason("Got expected ", ex);
    }
  }

  /*
   * testName: addSyncReturn
   * 
   * @test_Strategy: Some methods on the interface are annotated as async and
   * some are not. synchronous/blocking invocations on stateless, stateful, and
   * singleton. The synchronous method should block for the return value.
   */

  public void addSyncReturn() {
    final long waitMillis = 2000;
    final long start = System.currentTimeMillis();

    // only use local bean for this test, since the bean method addSyncReturn
    // calls addReturn, which returns a non-serializable impl of Future<Integer>
    annotatedMethodsIF.addSyncReturn(num1, num2, waitMillis);
    final long end = System.currentTimeMillis();
    final long elapsed = end - start;
    if (elapsed >= waitMillis) {
      appendReason("Time elapsed >= waitMillis: " + elapsed + " " + waitMillis);
    } else {
      throw new RuntimeException(
          "For a blocking method elapsed time should >= waitMillis: " + elapsed
              + " " + waitMillis);
    }
  }

  /*
   * testName: addReturnWaitMillis
   * 
   * @test_Strategy: Some methods on the interface are annotated as async and
   * some are not. Asynchronous invocations on stateless, stateful, and
   * singleton. The asynchronous method should return immediately.
   */

  public void addReturnWaitMillis()
      throws ExecutionException, InterruptedException, TimeoutException {
    final long waitMillis = 2000;

    for (AsyncAnnotatedMethodsCommonIF b : annotatedMethodsLocal) {
      final long start = System.currentTimeMillis();
      final Future<Integer> result = b.addReturn(num1, num2, waitMillis);
      final long end = System.currentTimeMillis();
      final long elapsed = end - start;
      if (elapsed < waitMillis) {
        appendReason(
            "Time elapsed < waitMillis: " + elapsed + " " + waitMillis);
      } else {
        throw new RuntimeException(
            "For async method elapsed time should < waitMillis: " + elapsed
                + " " + waitMillis);
      }

      assertEquals("", expectedSum, result.get());
      assertEquals("", expectedSum, result.get(10, TimeUnit.MILLISECONDS));
      assertEquals("", true, result.isDone());
      assertEquals("", false, result.isCancelled());
      assertEquals("", false, result.cancel(false));
      assertEquals("", false, result.cancel(true));
    }
  }

  /*
   * testName: cancelMayInterruptIfRunningFalse
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
  public void cancelMayInterruptIfRunningFalse()
      throws ExecutionException, InterruptedException, TimeoutException {
    cancelMayInterruptIfRunning(false);
  }

  /*
   * testName: cancelMayInterruptIfRunningTrue
   * 
   * @test_Strategy: see cancelMayInterruptIfRunningFalse
   */
  public void cancelMayInterruptIfRunningTrue()
      throws ExecutionException, InterruptedException, TimeoutException {
    cancelMayInterruptIfRunning(true);
  }

  /*
   * testName: passByValueOrReference
   * 
   * @test_Strategy:
   */
  public void passByValueOrReference() {
    final String a = "a", b = "b";
    for (int i = 0; i <= 2; i++) { // local invocations
      final String[] params = { a, b };
      beans[i].passByValueOrReference(params);
      assertNotEquals(beans[i].toString(), a, params[0]);
      assertNotEquals(beans[i].toString(), b, params[1]);
    }
  }

  /*
   * testName: passByValueOrReferenceAsync
   * 
   * @test_Strategy:
   */
  public void passByValueOrReferenceAsync()
      throws InterruptedException, ExecutionException {
    final String a = "a", b = "b";
    for (int i = 0; i <= 2; i++) { // local invocations
      final String[] params = { a, b };
      beans[i].passByValueOrReferenceAsync(params).get();
      assertNotEquals(beans[i].toString(), a, params[0]);
      assertNotEquals(beans[i].toString(), b, params[1]);
    }
  }

  private void cancelMayInterruptIfRunning(boolean mayInterruptIfRunning)
      throws ExecutionException, InterruptedException, TimeoutException {
    statusSingleton.removeResult(AsyncIF.CANCEL_IN_BEAN_KEY);
    statusSingleton.removeResult(AsyncIF.CANCEL_IN_CLIENT_KEY);
    final Future<Boolean> result = noInterface.cancelMayInterruptIfRunning();

    final long stopTime = System.currentTimeMillis() + AsyncIF.MAX_WAIT_MILLIS;

    while (!statusSingleton.isResultAvailable(AsyncIF.CANCEL_IN_BEAN_KEY)
        && System.currentTimeMillis() < stopTime) {
      Thread.sleep(AsyncIF.POLL_INTERVAL_MILLIS);
    }
    if (statusSingleton.isResultAvailable(AsyncIF.CANCEL_IN_BEAN_KEY)) {
      appendReason("The bean has started processing the async method.");
    } else {
      throw new RuntimeException(
          "The bean has not started processing the async method after waiting millis "
              + AsyncIF.MAX_WAIT_MILLIS);
    }

    result.cancel(mayInterruptIfRunning);
    // notify the statusSingleton that a cancel has been requested
    statusSingleton.addResult(AsyncIF.CANCEL_IN_CLIENT_KEY,
        AsyncIF.CANCEL_IN_CLIENT_VAL);

    assertEquals(null, mayInterruptIfRunning, result.get());
  }
}
