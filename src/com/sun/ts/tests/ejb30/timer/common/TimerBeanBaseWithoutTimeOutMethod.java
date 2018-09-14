/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.timer.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBContext;
import javax.ejb.NoMoreTimeoutsException;
import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.ScheduleExpression;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import com.sun.ts.tests.ejb30.common.helper.Helper;

/**
 * Since most bean classes have no business interface, all public methods in
 * this class are considered as business methods. In this class, method timeout
 * is not annotated with @TimeOut so that subclass may configure its own timeout
 * method.
 */
public class TimerBeanBaseWithoutTimeOutMethod {

  @Resource
  protected EJBContext ejbContext;

  @Resource
  protected TimerService timerService;

  @EJB
  protected TimeoutStatusBean statusSingleton;

  protected void timeout(Timer timer) {
    timeoutAlias(timer);
  }

  // this alias method is useful when a subclass overrides timeout(Timer), but
  // still wish to call super.timeout in another method. (see
  // schedule/descriptor)
  protected void timeoutAlias(Timer timer) {
    ScheduleExpression exp = timer.isCalendarTimer() ? timer.getSchedule()
        : null;
    Serializable ser = timer.getInfo();
    String testName = null;
    if (ser instanceof TimerInfo) {
      testName = ((TimerInfo) ser).getTestName();
    } else if (ser instanceof String) {
      testName = (String) ser;
    } else {
      throw new RuntimeException("Unrecognizable type for timer info: " + ser);
    }
    String timeoutRecord = "Invoking Timeout method for timer with schedule: "
        + TimerUtil.toString(exp) + ", info: " + ser + ", and isPersistent: "
        + timer.isPersistent();
    Helper.getLogger().fine(timeoutRecord);
    statusSingleton.addRecord(testName, timeoutRecord);
    statusSingleton.setStatus(testName, true);
  }

  public Collection<Timer> getTimers() {
    return timerService.getTimers();
  }

  public Collection<Timer> getAllTimers() {
    return timerService.getAllTimers();
  }

  public void cancelTimer(Timer... timers) {
    for (Timer timer : timers) {
      timer.cancel();
    }
  }

  public void cancelAllTimers() {
    TimerUtil.cancelAllTimers(timerService, false);
  }

  public Timer createTimer(ScheduleExpression exp, String name) {
    return createTimer(exp, new TimerInfo(name));
  }

  public boolean isPersistent(Timer timer) {
    return timer.isPersistent();
  }

  public boolean isCalendarTimer(Timer timer) {
    return timer.isCalendarTimer();
  }

  public ScheduleExpression getSchedule(Timer timer) {
    return timer.isCalendarTimer() ? timer.getSchedule() : null;
  }

  public Timer createTimer(Date expiration, TimerConfig timerConfig) {
    return timerService.createSingleActionTimer(expiration, timerConfig);
  }

  public Timer createTimer(Date initialExpiration, long intervalDuration,
      TimerConfig timerConfig) {
    return timerService.createIntervalTimer(initialExpiration, intervalDuration,
        timerConfig);
  }

  public Timer createTimer(long duration, TimerConfig timerConfig) {
    return timerService.createSingleActionTimer(duration, timerConfig);
  }

  public Timer createTimer(long InitialDuration, long intervalDuration,
      TimerConfig timerConfig) {
    return timerService.createIntervalTimer(InitialDuration, intervalDuration,
        timerConfig);
  }

  public Timer createTimer(ScheduleExpression exp, TimerConfig timerConfig) {
    return timerService.createCalendarTimer(exp, timerConfig);
  }

  public Timer createTimer(ScheduleExpression exp, TimerInfo info) {
    return timerService.createCalendarTimer(exp, new TimerConfig(info, true));
  }

  public Timer createTimer(long duration, java.io.Serializable timerInfo) {
    return timerService.createTimer(duration, timerInfo);
  }

  public Timer createTimer(Date expiration, long duration,
      java.io.Serializable timerInfo) {
    return timerService.createTimer(expiration, duration, timerInfo);
  }

  public Timer createFarFutureTimer(String name) {
    return TimerUtil.createFarFutureTimer(timerService, name);
  }

  public Timer createFarFutureTimer(TimerConfig timerConfig) {
    return TimerUtil.createFarFutureTimer(timerService, timerConfig);
  }

  public Timer createSecondLaterTimer(String name) {
    return TimerUtil.createSecondLaterTimer(timerService, name);
  }

  public Timer createSecondLaterTimer(TimerConfig timerConfig) {
    return TimerUtil.createSecondLaterTimer(timerService, timerConfig);
  }

  public Timer createMillisecondLaterTimer(String name) {
    return TimerUtil.createMillisecondLaterTimer(timerService, name);
  }

  public Timer createMillisecondLaterTimer(String name, boolean persistent) {
    return TimerUtil.createMillisecondLaterTimer(timerService, name,
        persistent);
  }

  public Timer findTimer(TimerInfo info) {
    return TimerUtil.findTimer(timerService, info);
  }

  public long getTimeRemaining(Timer timer) {
    return timer.getTimeRemaining();
  }

  public Date getNextTimeout(Timer timer) {
    return timer.getNextTimeout();
  }

  public Timer findTimer(String info) {
    return TimerUtil.findTimer(timerService, info);
  }

  public Timer findTimer(TimerConfig timerConfig) {
    return TimerUtil.findTimer(timerService, timerConfig);
  }

  public String passIfNoMoreTimeouts(Timer t) {
    String result = "";
    try {
      Date nextTimeout = t.getNextTimeout();
      throw new RuntimeException(
          "Expecting NoSuchObjectLocalException or NoMoreTimeoutsException "
              + "when accessing getNextTimeout, but actual " + nextTimeout);
    } catch (NoMoreTimeoutsException e) {
      result += " Got expected " + e;
    } catch (NoSuchObjectLocalException e) {
      result += " Got expected " + e;
    }
    try {
      long timeRemaining = t.getTimeRemaining();
      throw new RuntimeException(
          "Expecting NoSuchObjectLocalException or NoMoreTimeoutsException "
              + "when accessing getTimeRemaining, but actual " + timeRemaining);
    } catch (NoMoreTimeoutsException e) {
      result += " Got expected " + e;
    } catch (NoSuchObjectLocalException e) {
      result += " Got expected " + e;
    }
    return result;
  }

  public String passIfNoSuchObjectLocalException(Timer t) {
    List<String> expected = Arrays.asList("getInfo", "isCalendarTimer",
        "getHandle", "getNextTimeout", "getSchedule", "getTimeRemaining",
        "isPersistent", "cancel");
    List<String> calls = new ArrayList<String>();

    try {
      Serializable info = t.getInfo();
      Helper.getLogger()
          .warning("getInfo on an expired timer returned " + info);
    } catch (NoSuchObjectLocalException e) {
      calls.add("getInfo");
    }
    try {
      t.isCalendarTimer();
    } catch (NoSuchObjectLocalException e) {
      calls.add("isCalendarTimer");
    }
    try {
      t.getHandle();
    } catch (NoSuchObjectLocalException e) {
      calls.add("getHandle");
    } catch (IllegalStateException e) {
      // IllegalStateException for non-persistent timers.
      // NoSuchObjectLocalException
      // should occur even for non-persistent timers. To be safe, also allow
      // IllegalStateException to pass the test.
      calls.add("getHandle");
    }
    try {
      t.getNextTimeout();
    } catch (NoSuchObjectLocalException e) {
      calls.add("getNextTimeout");
    }
    try {
      t.getSchedule();
    } catch (NoSuchObjectLocalException e) {
      calls.add("getSchedule");
    } catch (IllegalStateException e) {
      // IllegalStateException for non-calendar timers.
      // NoSuchObjectLocalException
      // should occur even for non-calendar timers. To be safe, also allow
      // IllegalStateException to pass the test.
      calls.add("getSchedule");
    }

    try {
      t.getTimeRemaining();
    } catch (NoSuchObjectLocalException e) {
      calls.add("getTimeRemaining");
    }
    try {
      t.isPersistent();
    } catch (NoSuchObjectLocalException e) {
      calls.add("isPersistent");
    }
    try {
      t.cancel();
    } catch (NoSuchObjectLocalException e) {
      calls.add("cancel");
    }
    if (expected.equals(calls)) {
      return "Got expected NoSuchObjectLocalException for timer methods:"
          + expected.toString();
    }
    throw new RuntimeException(
        "Expecting NoSuchObjectLocalException for timer methods:"
            + expected.toString() + ", but actual " + calls.toString());
  }
}
