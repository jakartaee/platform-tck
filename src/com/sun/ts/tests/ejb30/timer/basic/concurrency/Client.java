/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.timer.basic.concurrency;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ejb.EJB;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;

public class Client extends EJBLiteClientBase {
  private static final int THREAD_COUNT = 100;

  private static final int INCREMENT = 999;

  private static final int EXPECTED_SUM = THREAD_COUNT * INCREMENT;

  private static final int CREATE_TIMER_AFTER_MILLIS = 1000;

  private static final long DEFAULT_MAX_WAIT_MILLIS = 1000 * 60 * 5;

  private static final long POLL_INTERVAL_MILLIS = 1000 * 60;

  @EJB(beanName = "WriteSingletonTimerBean")
  private TimerIF writeSingletonTimerBean;

  @EJB(beanName = "ReadSingletonTimerBean")
  private TimerIF readSingletonTimerBean;

  /*
   * @testName: lookupTimerService
   * 
   * @test_Strategy: lookup TimerService in lifecycle methods and async methods;
   * verify that TimerService and Timer methods can be invoked in these
   * invocation contexts.
   */
  public void lookupTimerService()
      throws InterruptedException, ExecutionException {
    Future<String> f = readSingletonTimerBean.lookupTimerService();
    appendReason(f.get());
  }

  public void readLockTimeout() {
    lockTimeout(readSingletonTimerBean);
    assertNotEquals(null, EXPECTED_SUM,
        getAndResetResult(readSingletonTimerBean, EXPECTED_SUM));
  }

  public void readLockBusyAdd() {
    readSingletonTimerBean.cancelAllTimers();
    readSingletonTimerBean.resetResult();
    readSingletonTimerBean.setIncrement(INCREMENT);
    for (int i = 0; i < THREAD_COUNT; i++) {
      readSingletonTimerBean.readLockBusyAdd();
    }
    assertNotEquals(null, EXPECTED_SUM,
        getAndResetResult(readSingletonTimerBean, EXPECTED_SUM));
  }

  /*
   * @testName: writeLockTimeout
   * 
   * @test_Strategy: @Timeout method has LockType.WRITE
   */
  public void writeLockTimeout() {
    lockTimeout(writeSingletonTimerBean);
    assertEquals(null, EXPECTED_SUM,
        getAndResetResult(writeSingletonTimerBean, EXPECTED_SUM));
  }

  private void lockTimeout(TimerIF b) {
    b.cancelAllTimers();
    b.resetResult();
    b.setIncrement(INCREMENT);
    Date d = TimerUtil.getCurrentDatePlus(Calendar.MILLISECOND,
        CREATE_TIMER_AFTER_MILLIS);
    for (int i = 0; i < THREAD_COUNT; i++) {
      String name = getTestName() + i;
      b.createTimer(name, d);
    }
  }

  // keep polling the singleton bean till the expected result is reached, or the
  // polling
  // times out. The expected param is needed since singleton timeout method or
  // business
  // method keeps updating the sum result.
  protected Integer getAndResetResult(TimerIF b, int expected,
      long... maxWaitMillis) {
    long waitFor = maxWaitMillis.length == 0 ? DEFAULT_MAX_WAIT_MILLIS
        : maxWaitMillis[0];
    long stopTime = System.currentTimeMillis() + waitFor;
    boolean avail = b.isResultComplete(expected);
    while (!avail && System.currentTimeMillis() < stopTime) {
      TestUtil.sleep((int) POLL_INTERVAL_MILLIS);
      avail = b.isResultComplete(expected);
    }
    return b.getAndResetResult();
  }
}
