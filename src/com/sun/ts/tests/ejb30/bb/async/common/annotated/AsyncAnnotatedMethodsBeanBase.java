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

package com.sun.ts.tests.ejb30.bb.async.common.annotated;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ejb.Asynchronous;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

public class AsyncAnnotatedMethodsBeanBase implements AsyncAnnotatedMethodsIF {

  // blocking method
  public Future<Integer> addSyncThrowException(int a, int b)
      throws TestFailedException {
    throw new TestFailedException(
        "Client should receive this exception, since this method is synchronous.");
  }

  // blocking method
  public Future<Integer> addSyncReturn(int a, int b, long waitMillis) {
    return addReturn(a, b, waitMillis);
  }

  @Asynchronous
  public Future<Integer> addReturn(final int a, final int b, long waitMillis) {
    Helper.busyWait(waitMillis);
    return new Future<Integer>() { // not serializable and not for remote
      public boolean cancel(boolean arg0) {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      public boolean isCancelled() {
        return false;
      }

      public boolean isDone() {
        return true;
      }

      public Integer get() throws InterruptedException, ExecutionException {
        return a + b;
      }

      public Integer get(long arg0, TimeUnit arg1)
          throws InterruptedException, ExecutionException, TimeoutException {
        return a + b;
      }
    };
  }

}
