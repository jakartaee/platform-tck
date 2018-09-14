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
package com.sun.ts.tests.ejb30.timer.schedule.lifecycle;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.NoSuchObjectLocalException;
import javax.ejb.Schedule;
import javax.ejb.ScheduleExpression;
import javax.ejb.Schedules;
import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerHandle;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import org.apache.commons.lang3.time.DateUtils;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.timer.common.TimerBeanBase;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;

@Stateless
public class ScheduleBean extends TimerBeanBase {
  protected static final String YEAR_5000 = "year5000";

  protected static final String NON_PERSISTENT_TIMER = "non-persistent";

  @SuppressWarnings("unused")
  @Schedules({ @Schedule(year = "5000", info = YEAR_5000),
      @Schedule(year = "5000", info = NON_PERSISTENT_TIMER, persistent = false) })
  private void year5000() {
    throw new IllegalStateException("We will not see this in our life.");
  }

  public String compareTimer(Timer timer) {
    // This timer is an auto-timer and its info is "year5000"
    Timer expected = TimerUtil.findTimer(timerService,
        (String) timer.getInfo());
    return Helper.assertEquals(
        "Compare timer passed from web component to that in TimerService",
        expected, timer);
  }

  public String compareTimer(TimerHandle timerHandle) {
    return compareTimer(timerHandle.getTimer());
  }

  public TimerHandle getTimerHandle(Timer timer) {
    return timer.isPersistent() ? timer.getHandle() : null;
  }

  public Timer getTimer(TimerHandle handle) {
    return handle.getTimer();
  }

  public String getTimerExpired(TimerHandle handle) {
    String result = null;
    try {
      Timer t = handle.getTimer();
      result = "Expecting NoSuchObjectLocalException but got " + t;
      throw new RuntimeException(result);
    } catch (NoSuchObjectLocalException e) {
      result = "Got expected " + e;
    }
    return result;
  }

  public void cancelTimer(TimerHandle timerHandle) {
    timerHandle.getTimer().cancel();
  }

  @TransactionAttribute(TransactionAttributeType.NEVER)
  public void cancelTimerWithNoTransaction(Timer timer) {
    timer.cancel();
  }

  public String timerHandleIllegalStateException() {
    StringBuilder sb = new StringBuilder();
    ScheduleExpression exp = new ScheduleExpression().year(5000);
    TimerConfig config = new TimerConfig("timerHandleIllegalStateException",
        false); // non-persistent
    Date initial = TimerUtil.getCurrentDatePlus(Calendar.YEAR, 1);
    long interval = DateUtils.MILLIS_PER_DAY;

    timerHandleIllegalStateException0(findTimer(NON_PERSISTENT_TIMER), sb);
    timerHandleIllegalStateException0(
        timerService.createCalendarTimer(exp, config), sb);
    timerHandleIllegalStateException0(
        timerService.createIntervalTimer(initial, interval, config), sb);
    timerHandleIllegalStateException0(
        timerService.createIntervalTimer(initial, interval, config), sb);
    timerHandleIllegalStateException0(
        timerService.createSingleActionTimer(initial, config), sb);
    timerHandleIllegalStateException0(
        timerService.createSingleActionTimer(initial.getTime(), config), sb);

    return sb.toString();
  }

  public String isCalendarTimerAndGetSchedule() {
    StringBuilder sb = new StringBuilder();
    ScheduleExpression exp = new ScheduleExpression().year(5000);
    TimerConfig config = new TimerConfig("isCalendarTimer", false); // non-persistent
    Date initial = TimerUtil.getCurrentDatePlus(Calendar.YEAR, 1);
    long interval = DateUtils.MILLIS_PER_DAY;
    Serializable info = "isCalendarTimer";

    isCalendarTimerAndGetSchedule0(findTimer(YEAR_5000), true, sb);
    isCalendarTimerAndGetSchedule0(timerService.createCalendarTimer(exp), true,
        sb);
    isCalendarTimerAndGetSchedule0(
        timerService.createCalendarTimer(exp, config), true, sb);
    isCalendarTimerAndGetSchedule0(
        timerService.createIntervalTimer(initial, interval, config), false, sb);
    isCalendarTimerAndGetSchedule0(
        timerService.createIntervalTimer(initial, interval, config), false, sb);
    isCalendarTimerAndGetSchedule0(
        timerService.createSingleActionTimer(initial, config), false, sb);
    isCalendarTimerAndGetSchedule0(
        timerService.createSingleActionTimer(initial.getTime(), config), false,
        sb);

    isCalendarTimerAndGetSchedule0(timerService.createTimer(initial, info),
        false, sb);
    isCalendarTimerAndGetSchedule0(
        timerService.createTimer(initial.getTime(), info), false, sb);
    isCalendarTimerAndGetSchedule0(
        timerService.createTimer(initial, interval, info), false, sb);
    isCalendarTimerAndGetSchedule0(
        timerService.createTimer(initial.getTime(), interval, info), false, sb);

    return sb.toString();
  }

  // Also verifies Timer.getSchedule returns normal value or
  // IllegalStateException
  private void isCalendarTimerAndGetSchedule0(Timer t, boolean expected,
      StringBuilder sb) {
    Helper.assertEquals(null, expected, t.isCalendarTimer(), sb);
    ScheduleExpression exp = null;
    if (expected) {
      exp = t.getSchedule();
      sb.append(
          " Got expected ScheduleExpression when calling getSchedule: " + exp);
    } else {
      try {
        exp = t.getSchedule();
        sb.append(
            "Expecting IllegalStateException when calling getSchedule on a non-schedule timer, but got "
                + exp);
        throw new RuntimeException(sb.toString());
      } catch (IllegalStateException e) {
        sb.append(
            " Got expected IllegalStateException when calling getSchedule on a non-schedule timer.");
      }
    }
  }

  private void timerHandleIllegalStateException0(Timer t, StringBuilder sb) {
    TimerHandle h = null;
    try {
      h = t.getHandle();
      sb.append(
          "Expecting IllegalStateException when calling getHandle on a non-persistent timer, but got "
              + h);
      throw new RuntimeException(sb.toString());
    } catch (IllegalStateException e) {
      sb.append(
          " Got expected IllegalStateException when calling getHandle on a non-persistent timer");
    }
  }
}
