/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import jakarta.ejb.AccessTimeout;
import jakarta.ejb.AsyncResult;
import jakarta.ejb.Asynchronous;
import jakarta.ejb.ConcurrencyManagement;
import jakarta.ejb.ConcurrencyManagementType;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.NoMoreTimeoutsException;
import jakarta.ejb.NoSuchObjectLocalException;
import jakarta.ejb.SessionContext;
import jakarta.ejb.Timeout;
import jakarta.ejb.Timer;
import jakarta.ejb.TimerService;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.timer.common.TimerInfo;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;

@TransactionManagement(TransactionManagementType.BEAN)
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
public class LockSingletonTimerBeanBase implements TimerIF {
  protected int increment;

  protected int sum;

  @Resource(name = "timerService")
  protected TimerService timerService;

  private Map<String, TimerService> postConstructTimerServices = new HashMap<String, TimerService>();

  private Map<String, TimerService> preDestroyTimerServices = new HashMap<String, TimerService>();

  private Map<String, TimerService> asyncMethodTimerServices = new HashMap<String, TimerService>();

  private static final String KEY_TIMER_SERVICE_FROM_GLOBAL_LOOKUP = "KEY_TIMER_SERVICE_FROM_GLOBAL_LOOKUP";

  private static final String KEY_TIMER_SERVICE_FROM_COMPONENT_LOOKUP = "KEY_TIMER_SERVICE_FROM_COMPONENT_LOOKUP";

  private static final String KEY_TIMER_SERVICE_SESSION_CONTEXT = "KEY_TIMER_SERVICE_SESSION_CONTEXT";

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    lookupTimerService(postConstructTimerServices);
  }

  @SuppressWarnings("unused")
  @PreDestroy
  private void preDestroy() {
    lookupTimerService(preDestroyTimerServices);
    Helper.getLogger().info("TimerService lookup results in preDestroy: "
        + preDestroyTimerServices);
  }

  @Timeout
  protected void timeout(Timer timer) {
    Helper.getLogger().finer("Timeout method for timer: " + timer.getInfo()
        + ". sum=" + sum + ", increment=" + increment);
    busyAdd0();
  }

  @Lock(LockType.READ)
  @Asynchronous
  @AccessTimeout(unit = TimeUnit.NANOSECONDS, value = 9000000000L) // 9 seconds
  public void readLockBusyAdd() {
    busyAdd0();
  }

  public int getAndResetResult() {
    int result = sum;
    sum = 0;
    Helper.getLogger().fine("getAndResetSum about to return " + result);
    return result;
  }

  @Lock(LockType.READ)
  public boolean isResultComplete(int expected) {
    return sum == expected;
  }

  public void resetResult() {
    sum = 0;
  }

  @Lock(LockType.READ)
  public void setIncrement(int i) {
    this.increment = i;
  }

  @Asynchronous
  @AccessTimeout(unit = TimeUnit.MINUTES, value = 10)
  @Lock(LockType.READ)
  public Future<Timer> createTimer(String name, Date d) {
    Timer timer = timerService.createTimer(d, new TimerInfo(name));
    return new AsyncResult<Timer>(timer);
  }

  @Lock(LockType.READ)
  public void cancelAllTimers() {
    TimerUtil.cancelAllTimers(timerService, false);
  }

  public Future<String> lookupTimerService() {
    asyncMethodTimerServices.clear();
    lookupTimerService(asyncMethodTimerServices);
    return new AsyncResult<String>(
        "postConstructTimerServices :" + postConstructTimerServices
            + ", asyncMethodTimerServices: " + asyncMethodTimerServices);
  }

  private void busyAdd0() {
    for (int i = 0; i < increment; i++) {
      sum++;
    }
  }

  private void lookupTimerService(Map<String, TimerService> timerServiceMap) {
    timerServiceMap.put(KEY_TIMER_SERVICE_FROM_GLOBAL_LOOKUP,
        (TimerService) ServiceLocator.lookupNoTry("java:comp/TimerService"));
    SessionContext sessionContext = (SessionContext) ServiceLocator
        .lookupNoTry("java:comp/EJBContext");
    timerServiceMap.put(KEY_TIMER_SERVICE_FROM_COMPONENT_LOOKUP,
        (TimerService) sessionContext.lookup("timerService"));
    timerServiceMap.put(KEY_TIMER_SERVICE_SESSION_CONTEXT,
        sessionContext.getTimerService());
    for (Iterator<String> it = timerServiceMap.keySet().iterator(); it
        .hasNext();) {
      String key = it.next();
      Helper.getLogger().fine("About to getTimers from TimerService " + key);
      TimerService ts = timerServiceMap.get(key);
      TimerUtil.createSecondLaterTimer(ts);
      for (Object o : ts.getTimers()) {
        Timer t = (Timer) o;
        if (t.isPersistent()) {
          t.getHandle();
        }
        t.getInfo();

        try {
          t.getNextTimeout();
        } catch (NoMoreTimeoutsException nmto) {
          // ignore
        }
        if (t.isCalendarTimer()) {
          t.getSchedule();
        }
        try {
          t.getTimeRemaining();
        } catch (NoMoreTimeoutsException e) {
          // ignore
        }
        try {
          t.cancel();
        } catch (NoSuchObjectLocalException ignore) {
        }
      }
    }
  }
}
