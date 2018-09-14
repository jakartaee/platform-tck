/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.stateful.concurrency.common;

import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.CONCURRENT_INVOCATION_TIMES;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.ConcurrentAccessException;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

abstract public class StatefulConcurrencyClientBase extends EJBLiteClientBase {

  protected List<Exception> concurrentPing(StatefulConcurrencyIF b)
      throws InterruptedException {
    Pinger[] pingers = new Pinger[CONCURRENT_INVOCATION_TIMES];
    for (int i = 0; i < CONCURRENT_INVOCATION_TIMES; i++) {
      pingers[i] = new Pinger(b);
    }
    return concurrentPing0(pingers);
  }

  protected List<Exception> concurrentPing(Runnable runnable)
      throws InterruptedException {
    Pinger[] pingers = new Pinger[CONCURRENT_INVOCATION_TIMES];
    for (int i = 0; i < CONCURRENT_INVOCATION_TIMES; i++) {
      pingers[i] = new Pinger(runnable);
    }
    return concurrentPing0(pingers);
  }

  private List<Exception> concurrentPing0(Pinger[] pingers)
      throws InterruptedException {
    for (Pinger p : pingers) {
      p.start();
    }
    for (Pinger p : pingers) {
      try {
        p.join();
      } catch (InterruptedException e) {
        Helper.getLogger().log(Level.WARNING, null, e);
      }
    }
    return Pinger.getExceptionAsList(pingers);
  }

  protected void checkConcurrentAccessResult(List<Exception> exceptionList,
      int nullCountExpected, int concurrentAccessExceptionCountExpected) {
    int nullCount = 0;
    int concurrentAccessExceptionCount = 0;

    Helper.getLogger().fine("exceptionList: " + exceptionList.toString());
    for (Exception e : exceptionList) {
      if (e == null) {
        appendReason("Got no exception, which may be correct.");
        nullCount++;
      } else if (e instanceof ConcurrentAccessException) {
        appendReason("Got ConcurrentAccessException, which may be correct: ",
            e);
        concurrentAccessExceptionCount++;
      } else {
        throw new RuntimeException(
            "Expecting null or ConcurrentAccessException, but got ", e);
      }
    }
    assertEquals("Check nullCount", nullCountExpected, nullCount);
    assertEquals("Check concurrentAccessExceptionCount",
        concurrentAccessExceptionCountExpected, concurrentAccessExceptionCount);
  }
}
