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

package com.sun.ts.tests.ejb30.timer.basic.sharing;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.timer.common.ClientBase;
import com.sun.ts.tests.ejb30.timer.common.TimerInfo;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.Vector;
import javax.ejb.EJB;
import javax.ejb.Timer;

public class Client extends ClientBase {
  private static final int THREAD_COUNT = 100;

  private static final long TIMER_DURATION = 1000 * 60 * 5;

  @EJB(beanName = "StatelessTimerBean")
  private TimerIF statelessTimerBean;

  @EJB(beanName = "SingletonTimerBean")
  private TimerIF singletonTimerBean;

  @Override
  public void setup(String[] args, Properties p) {
    super.setup(args, p);
    // warm up the timer service to avoid possible startup delay, and clean up
    statelessTimerBean.cancelAllTimers();
    singletonTimerBean.cancelAllTimers();
  }

  /*
   * @testName: createTimerRollbackStateless
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: create a timer, and roll back the tx.
   */
  public void createTimerRollbackStateless() {
    statelessTimerBean.createTimerRollback(DEFAULT_DURATION,
        new TimerInfo(getTestName()));
    passIfNoTimeout();
  }

  /*
   * @testName: createTimerRollbackSingleton
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: create a timer, and roll back the tx.
   */
  public void createTimerRollbackSingleton() {
    singletonTimerBean.createTimerRollback(DEFAULT_DURATION,
        new TimerInfo(getTestName()));
    passIfNoTimeout();
  }

  /*
   * @testName: createVerifyRecurringTimerStateless
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: create a timer, find it, and verify its expiration
   */
  public void createVerifyRecurringTimerStateless() {
    Date expiration = TimerUtil.getCurrentDatePlus(Calendar.SECOND, 1);
    Timer timer = statelessTimerBean.createTimer(expiration, DEFAULT_INTERVAL,
        new TimerInfo(getTestName()));
    passIfRecurringTimeout();
    statelessTimerBean.cancelTimer(timer);
  }

  /*
   * @testName: createVerifyRecurringTimerSingleton
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: create a timer, find it, and verify its expiration
   */
  public void createVerifyRecurringTimerSingleton() {
    Date expiration = TimerUtil.getCurrentDatePlus(Calendar.SECOND, 1);
    Timer timer = singletonTimerBean.createTimer(expiration, DEFAULT_INTERVAL,
        new TimerInfo(getTestName()));
    passIfRecurringTimeout();
    singletonTimerBean.cancelTimer(timer);
  }

  /*
   * @testName: accessTimersStateless
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: verify all bean instances of the same EJB share the same
   * set of timers, invoking stateless bean
   */
  public void accessTimersStateless() {
    accessTimers0(statelessTimerBean);
  }

  /*
   * @testName: accessTimersSingleton
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: verify all bean instances of the same EJB share the same
   * set of timers, invoking singleton bean
   */
  public void accessTimersSingleton() {
    accessTimers0(singletonTimerBean);
  }

  private void accessTimers0(final TimerIF bean) {
    final Vector<String> results = new Vector<String>();
    final Vector<String> errors = new Vector<String>();

    bean.createTimer(TIMER_DURATION, null);
    Runnable runnable = new Runnable() {
      public void run() {
        try {
          String result = bean.accessTimers();
          results.add(result);
        } catch (TestFailedException e) {
          errors.add(TestUtil.printStackTraceToString(e));
        }
      }
    };
    Thread[] threads = new Thread[THREAD_COUNT];
    for (int i = 0; i < THREAD_COUNT; i++) {
      threads[i] = new Thread(runnable);
      threads[i].start();
    }
    for (int i = 0; i < threads.length; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException ex) {
        // ignore
      }
    }
    assertEquals(null, 0, errors.size());
    assertEquals(null, THREAD_COUNT, results.size());
    appendReason(results);
    bean.cancelAllTimers();
  }
}
