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

package com.sun.ts.tests.ejb30.lite.async.common.metadata;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.statussingleton.StatusSingletonBean;

@Asynchronous
abstract public class BeanClassLevel2BeanBase extends BeanClassLevel1BeanBase {
  @EJB
  protected StatusSingletonBean statusSingleton;

  @Override
  public Future<Boolean> futureReturnType() {
    return new AsyncResult<Boolean>(true);
  }

  public Future<TimeUnit> customFutureImpl(final TimeUnit timeUnit) {
    return new Future<TimeUnit>() {

      public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      public boolean isCancelled() {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      public boolean isDone() {
        throw new UnsupportedOperationException("Not supported yet.");
      }

      public TimeUnit get() throws InterruptedException, ExecutionException {
        return timeUnit;
      }

      public TimeUnit get(long timeout, TimeUnit unit)
          throws InterruptedException, ExecutionException, TimeoutException {
        return timeUnit;
      }
    };
  }
}
