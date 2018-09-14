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

import java.util.List;
import java.util.concurrent.Future;
import java.util.logging.Level;

import javax.annotation.Resource;
import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.SessionContext;

import com.sun.ts.tests.ejb30.common.calc.CalculatorException;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.statussingleton.StatusSingletonBean;

public class AsyncBeanBase implements AsyncIF {
  private boolean errorOccurredInInstance = false;

  @Resource
  protected SessionContext sessionContext;

  @EJB
  private StatusSingletonBean statusSingleton;

  @Asynchronous()
  public void addAway(int a, int b, int key) {
    statusSingleton.addResult(key, a + b + key);
  }

  @Asynchronous()
  public void voidRuntimeException(Integer key) throws RuntimeException {
    errorOccurredInInstance = true;
    statusSingleton.addResult(key, 1);
    throw new RuntimeException("voidRuntimeException in instance " + this);
  }

  @Asynchronous()
  public Future<Integer> addReturn(int a, int b) {
    return new AsyncResult<Integer>(a + b);
  }

  @Asynchronous
  public Future<Integer> futureRuntimeException() throws RuntimeException {
    errorOccurredInInstance = true;
    throw new RuntimeException("futureRuntimeException in instance " + this);
  }

  @Asynchronous
  public Future<Integer> futureError() throws AssertionError {
    errorOccurredInInstance = true;
    throw new AssertionError("futureError in instance " + this);
  }

  @Asynchronous
  public Future<Integer> futureException() throws CalculatorException {
    throw new CalculatorException("futureException in instance " + this);
  }

  @Asynchronous
  public Future<Integer> identityHashCode() {
    return new AsyncResult<Integer>(System.identityHashCode(this));
  }

  @Asynchronous
  public Future<Boolean> isErrorOccurredInInstance() {
    Helper.getLogger().logp(Level.FINE, "AsyncBeanBase", "isDestroyed",
        "instance: " + this + ", identityHashCode: "
            + System.identityHashCode(this) + "errorOccurredInInstance: "
            + errorOccurredInInstance);
    return new AsyncResult<Boolean>(errorOccurredInInstance);
  }

  @Asynchronous
  public Future<Boolean> cancelMayInterruptIfRunning() {
    Helper.getLogger()
        .info(Helper.assertEquals(
            "Check wasCancelCalled before any client cancel request", false,
            sessionContext.wasCancelCalled()));

    // to tell the client that the async method is being processed
    statusSingleton.addResult(AsyncIF.CANCEL_IN_BEAN_KEY,
        AsyncIF.CANCEL_IN_BEAN_VAL);

    // wait for the client to send the cancel request
    final long stopTime = System.currentTimeMillis() + AsyncIF.MAX_WAIT_MILLIS;
    while (!statusSingleton.isResultAvailable(AsyncIF.CANCEL_IN_CLIENT_KEY)
        && System.currentTimeMillis() < stopTime) {
      Helper.busyWait(AsyncIF.POLL_INTERVAL_MILLIS);
    }
    if (statusSingleton.isResultAvailable(AsyncIF.CANCEL_IN_CLIENT_KEY)) {
      Helper.getLogger()
          .info("The client has requested to cancel the async method.");
    } else {
      throw new RuntimeException(
          "The client has not requested to cancel the async method after waiting millis "
              + AsyncIF.MAX_WAIT_MILLIS);
    }

    return new AsyncResult<Boolean>(sessionContext.wasCancelCalled());
  }

  @Asynchronous
  public Future<List<String>> futureValueList(List<String> vals) {
    return new AsyncResult<List<String>>(vals);
  }

  public void passByValueOrReference(String[] ss) {
    for (int i = 0; i < ss.length; i++) {
      ss[i] = this.toString();
    }
  }

  @Asynchronous
  public Future<String> passByValueOrReferenceAsync(String[] ss) {
    passByValueOrReference(ss);
    return new AsyncResult<String>(null);
  }
}
