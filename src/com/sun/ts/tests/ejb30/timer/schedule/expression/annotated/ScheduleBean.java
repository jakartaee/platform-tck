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
package com.sun.ts.tests.ejb30.timer.schedule.expression.annotated;

import static com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.DAY_OF_MONTH;
import static com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.DAY_OF_WEEK;
import static com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.HOUR;
import static com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.MINUTE;
import static com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.MONTH;
import static com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.SECOND;
import static com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.YEAR;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.ScheduleExpression;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType;
import com.sun.ts.tests.ejb30.timer.common.ScheduleValues;
import com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod;
import com.sun.ts.tests.ejb30.timer.common.TimerInfo;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;

@Stateless
public class ScheduleBean extends TimerBeanBaseWithoutTimeOutMethod {
  @Override
  @Timeout
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  protected void timeout(Timer timer) {
    super.timeout(timer);
    TimerInfo info = (TimerInfo) timer.getInfo();
    String testName = info.getTestName();
    if ("dayOfMonthAndDayOfWeek".equals(testName)) {
      long expectedNext = 24 * 60 * 60 * 1000;
      long actualNext = timer.getTimeRemaining();
      String s = "Expecting approximate timeRemaining " + expectedNext
          + ", actual timeRemaining " + actualNext;
      statusSingleton.addRecord(testName, s);
      if (Math.abs(actualNext - expectedNext) < 60000) {
        // pass, the next timeout is the next day +/- 60 seconds
        statusSingleton.setStatus(testName, true);
      } else {
        statusSingleton.setStatus(testName, false);
      }
    }
  }

  public String validSecondValuesInt() {
    return validValuesInt(ScheduleValues.validSecondValuesInt(), SECOND);
  }

  public String validMinuteValuesInt() {
    return validValuesInt(ScheduleValues.validMinuteValuesInt(), MINUTE);
  }

  public String validHourValuesInt() {
    return validValuesInt(ScheduleValues.validHourValuesInt(), HOUR);
  }

  public String validMonthValuesInt() {
    return validValuesInt(ScheduleValues.validMonthValuesInt(), MONTH);
  }

  public String validYearValuesInt() {
    return validValuesInt(ScheduleValues.validYearValuesInt(), YEAR);
  }

  public String validDayOfMonthValuesInt() {
    return validValuesInt(ScheduleValues.validDayOfMonthValuesInt(),
        DAY_OF_MONTH);
  }

  public String validDayOfWeekValuesInt() {
    return validValuesInt(ScheduleValues.validDayOfWeekValuesInt(),
        DAY_OF_WEEK);
  }

  public String invalidSecondValuesInt() {
    return invalidValuesInt(ScheduleValues.invalidSecondValuesInt(), SECOND);
  }

  public String invalidMinuteValuesInt() {
    return invalidValuesInt(ScheduleValues.invalidMinuteValuesInt(), MINUTE);
  }

  public String invalidHourValuesInt() {
    return invalidValuesInt(ScheduleValues.invalidHourValuesInt(), HOUR);
  }

  public String invalidMonthValuesInt() {
    return invalidValuesInt(ScheduleValues.invalidMonthValuesInt(), MONTH);
  }

  public String invalidYearValuesInt() {
    return invalidValuesInt(ScheduleValues.invalidYearValuesInt(), YEAR);
  }

  public String invalidDayOfMonthValuesInt() {
    return invalidValuesInt(ScheduleValues.invalidDayOfMonthValuesInt(),
        DAY_OF_MONTH);
  }

  public String invalidDayOfWeekValuesInt() {
    return invalidValuesInt(ScheduleValues.invalidDayOfWeekValuesInt(),
        DAY_OF_WEEK);
  }

  public String validSecondValuesString() {
    return validValuesString(ScheduleValues.validSecondValuesString(), SECOND);
  }

  public String validMinuteValuesString() {
    return validValuesString(ScheduleValues.validMinuteValuesString(), MINUTE);
  }

  public String validHourValuesString() {
    return validValuesString(ScheduleValues.validHourValuesString(), HOUR);
  }

  public String validMonthValuesString() {
    return validValuesString(ScheduleValues.validMonthValuesString(), MONTH);
  }

  public String validYearValuesString() {
    return validValuesString(ScheduleValues.validYearValuesString(),
        ScheduleAttributeType.YEAR);
  }

  public String validDayOfMonthValuesString() {
    return validValuesString(ScheduleValues.validDayOfMonthValuesString(),
        ScheduleAttributeType.DAY_OF_MONTH);
  }

  public String validDayOfWeekValuesString() {
    return validValuesString(ScheduleValues.validDayOfWeekValuesString(),
        ScheduleAttributeType.DAY_OF_WEEK);
  }

  public String invalidSecondValuesString() {
    return invalidValuesString(ScheduleValues.invalidSecondValuesString(),
        ScheduleAttributeType.SECOND);
  }

  public String invalidMinuteValuesString() {
    return invalidValuesString(ScheduleValues.invalidMinuteValuesString(),
        MINUTE);
  }

  public String invalidHourValuesString() {
    return invalidValuesString(ScheduleValues.invalidHourValuesString(), HOUR);
  }

  public String invalidMonthValuesString() {
    return invalidValuesString(ScheduleValues.invalidMonthValuesString(),
        MONTH);
  }

  public String invalidYearValuesString() {
    return invalidValuesString(ScheduleValues.invalidYearValuesString(), YEAR);
  }

  public String invalidDayOfMonthValuesString() {
    return invalidValuesString(ScheduleValues.invalidDayOfMonthValuesString(),
        DAY_OF_MONTH);
  }

  public String invalidDayOfWeekValuesString() {
    return invalidValuesString(ScheduleValues.invalidDayOfWeekValuesString(),
        DAY_OF_WEEK);
  }

  public String leapYear() {
    ScheduleExpression exp = new ScheduleExpression();
    exp.month(ScheduleValues.VALID_FEB_29_4000[0]);
    exp.dayOfMonth(ScheduleValues.VALID_FEB_29_4000[1]);
    exp.year(ScheduleValues.VALID_FEB_29_4000[2]);
    Timer timer = timerService.createCalendarTimer(exp);
    timer.cancel();
    return "Scheduled and canceled a timer with "
        + Arrays.toString(ScheduleValues.VALID_FEB_29_4000);
  }

  public String attributeDefaults() {
    StringBuilder sb = new StringBuilder();
    Timer t = timerService.createCalendarTimer(new ScheduleExpression());
    ScheduleExpression exp = t.getSchedule();
    TimerUtil.checkScheduleDefaults(exp, sb);
    return sb.toString();
  }

  public String dayOfWeekOverDayOfMonth() {
    TimerInfo info = new TimerInfo("dayOfWeekOverDayOfMonth");
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_WEEK, 1);
    int nextDayOfWeek = TimerUtil.getForSchedule(Calendar.DAY_OF_WEEK, cal);
    // every year, every month, every hour, every minute, every second, but not
    // every day. Only for the next day of week. So the timeout method for
    // this test must not be invoked during test run.
    // There are chances of false positive; if a container does not fire
    // any timeout event, this test will also pass.
    ScheduleExpression exp = new ScheduleExpression().year("*").month("*")
        .dayOfMonth("*").dayOfWeek(nextDayOfWeek).second("*").minute("*")
        .hour("*");
    Timer timer = timerService.createCalendarTimer(exp,
        new TimerConfig(info, true));
    return "Created timer: " + TimerUtil.toString(timer);
  }

  public String dayOfMonthOverDayOfWeek() {
    TimerInfo info = new TimerInfo("dayOfMonthOverDayOfWeek");
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_MONTH, 1);
    int nextDayOfMonth = TimerUtil.getForSchedule(Calendar.DAY_OF_MONTH, cal);
    ScheduleExpression exp = new ScheduleExpression().year("*").month("*")
        .dayOfWeek("*").dayOfMonth(nextDayOfMonth).second("*").minute("*")
        .hour("*");
    Timer timer = timerService.createCalendarTimer(exp,
        new TimerConfig(info, true));
    return "Created timer: " + TimerUtil.toString(timer);
  }

  public String dayOfMonthAndDayOfWeek() {
    // The timer expires this DAY_OF_WEEK and next DAY_OF_MONTH, only once
    // during a day
    TimerInfo info = new TimerInfo("dayOfMonthAndDayOfWeek");
    int currentDayOfWeek = TimerUtil.getForSchedule(Calendar.DAY_OF_WEEK);
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.DAY_OF_MONTH, 1);
    int nextDayOfMonth = TimerUtil.getForSchedule(Calendar.DAY_OF_MONTH, cal);

    Calendar cal2 = Calendar.getInstance();
    cal2.add(Calendar.SECOND, 1); // times out after 1 second

    ScheduleExpression exp = new ScheduleExpression().year("*").month("*")
        .dayOfWeek(currentDayOfWeek).dayOfMonth(nextDayOfMonth)
        .second(TimerUtil.getForSchedule(Calendar.SECOND, cal2))
        .minute(TimerUtil.getForSchedule(Calendar.MINUTE, cal2))
        .hour(TimerUtil.getForSchedule(Calendar.HOUR_OF_DAY, cal2));
    Timer timer = timerService.createCalendarTimer(exp,
        new TimerConfig(info, true));
    return "Created timer: " + TimerUtil.toString(timer);
  }

  public void validStart(StringBuilder sb) {
    ScheduleExpression exp = new ScheduleExpression();
    Date start = null;
    for (int i = 1; i < 10; i++) {
      start = TimerUtil.getCurrentDatePlus(Calendar.YEAR, i);
      exp.start(start);
      Helper.assertEquals("Check getStart()", start, exp.getStart(), sb);
    }
    Date[] starts = { start, null };
    for (Date aStart : starts) {
      Timer timer = timerService.createCalendarTimer(exp.start(aStart),
          (TimerConfig) null);
      Helper.assertEquals("Check getStart()", aStart,
          timer.getSchedule().getStart(), sb);
      timer.cancel();
    }
  }

  public void validEnd(StringBuilder sb) {
    ScheduleExpression exp = new ScheduleExpression();
    Date end = null;
    for (int i = 1; i < 10; i++) {
      end = TimerUtil.getCurrentDatePlus(Calendar.YEAR, i);
      exp.end(end);
      Helper.assertEquals("Check getEnd()", end, exp.getEnd(), sb);
    }
    // verify that start can be set to the same value as end, and that
    // start and end can be null
    Date[] dates = { end, null };
    TimerConfig timerConfig = null;
    for (Date d : dates) {
      // set hour minute second to DEFAULT_ATTRIBUTE_VALUE_STAR so the timer
      // expires every second instead of the default beginning of day.
      // This is needed to make sure there is always at least one match,
      // especially
      // when the start is the same as end and the time window is narrow.
      exp = exp.hour(ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR)
          .minute(ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR)
          .second(ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR).start(d).end(d);
      Timer timer = timerService.createCalendarTimer(exp, timerConfig);
      Helper.assertEquals("Check getStart()", d, timer.getSchedule().getStart(),
          sb);
      Helper.assertEquals("  Check getEnd()", d, timer.getSchedule().getEnd(),
          sb);
      timer.cancel();
    }
  }

  public void validStartEnd(StringBuilder sb) {
    final int count = 3;
    ScheduleExpression exp = new ScheduleExpression();
    Calendar cal = Calendar.getInstance();
    Date d = cal.getTime();
    Date[] starts = new Date[count];
    Date[] ends = new Date[count];

    // start and end are the same
    starts[0] = d;
    ends[0] = d;

    // start is later than end
    starts[1] = TimerUtil.getCurrentDatePlus(Calendar.YEAR, 3);
    ends[1] = TimerUtil.getCurrentDatePlus(Calendar.YEAR, 2);

    // start in the past
    cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 2);
    starts[2] = cal.getTime();
    ends[2] = d;

    for (int i = 0; i < count; i++) {
      exp.start(starts[i]).end(ends[i]);
      @SuppressWarnings("unused")
      Timer timer = timerService.createCalendarTimer(exp);
      sb.append(" Created a timer with schedule ")
          .append(TimerUtil.toString(exp)).append(TestUtil.NEW_LINE);
    }
  }

  private String validValuesInt(int[] values,
      ScheduleAttributeType attributeType) {
    try {
      for (int i : values) {
        dispatch(i, attributeType);
      }
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
    String result = "Succeeded in creating ScheduleExpression and Timer with all valid int values for "
        + attributeType + ": " + Arrays.toString(values);
    TimerUtil.cancelAllTimers(timerService, false);
    return result;
  }

  private String validValuesString(String[] values,
      ScheduleAttributeType attributeType) {
    try {
      for (String i : values) {
        dispatch(i, attributeType);
      }
    } catch (IllegalArgumentException e) {
      throw new RuntimeException(e);
    }
    String result = "Succeeded in creating ScheduleExpression and Timer with all valid String values for "
        + attributeType + ": " + Arrays.toString(values);
    TimerUtil.cancelAllTimers(timerService, false);
    return result;
  }

  private String invalidValuesInt(int[] values,
      ScheduleAttributeType attributeType) {
    String result = null;
    for (int i : values) {
      try {
        dispatch(i, attributeType);
        result = "Expecting IllegalArgumentException, but got none, attributeType="
            + attributeType + ", value=" + i;
        throw new RuntimeException(result);
      } catch (IllegalArgumentException expected) {
        Helper.getLogger().fine(
            "Got expected IllegalArgumentException when creating timer with "
                + attributeType + ":" + i);
      }
    }
    if (result == null) {
      result = "Got expected IllegalArgumentException when creating ScheduleExpression and Timer with all invalid int values for "
          + attributeType + ": " + Arrays.toString(values);
    }
    return result;
  }

  private String invalidValuesString(String[] values,
      ScheduleAttributeType attributeType) {
    String result = null;
    for (String i : values) {
      try {
        dispatch(i, attributeType);
        result = "Expecting IllegalArgumentException, but got none, attributeType="
            + attributeType + ", value=" + i;
        throw new RuntimeException(result);
      } catch (IllegalArgumentException expected) {
        Helper.getLogger().fine(
            "Got expected IllegalArgumentException when creating timer with "
                + attributeType + ":" + i);
      }
    }
    if (result == null) {
      result = "Got expected IllegalArgumentException when creating ScheduleExpression and Timer with all invalid int values for "
          + attributeType + ": " + Arrays.toString(values);
    }
    return result;
  }

  private void dispatch(int i, ScheduleAttributeType attributeType) {
    ScheduleExpression exp = new ScheduleExpression();
    if (attributeType.equals(SECOND)) {
      exp = exp.second(i);
    } else if (attributeType.equals(MINUTE)) {
      exp = exp.minute(i);
    } else if (attributeType.equals(HOUR)) {
      exp = exp.hour(i);
    } else if (attributeType.equals(MONTH)) {
      exp = exp.month(i);
    } else if (attributeType.equals(DAY_OF_MONTH)) {
      exp = exp.dayOfMonth(i);
    } else if (attributeType.equals(DAY_OF_WEEK)) {
      exp = exp.dayOfWeek(i);
    } else if (attributeType.equals(YEAR)) {
      exp = exp.year(i);
    } else {
      throw new IllegalStateException(
          "Invalid ScheduleAttributeType:" + attributeType);
    }
    Helper.getLogger()
        .fine("About to create timer with " + attributeType + ": " + i);
    timerService.createCalendarTimer(exp);
    Helper.getLogger().fine("Created timer with attributeType=" + attributeType
        + " and value=" + i);
  }

  private void dispatch(String i, ScheduleAttributeType attributeType) {
    ScheduleExpression exp = new ScheduleExpression();
    if (attributeType.equals(SECOND)) {
      exp = exp.second(i);
    } else if (attributeType.equals(MINUTE)) {
      exp = exp.minute(i);
    } else if (attributeType.equals(HOUR)) {
      exp = exp.hour(i);
    } else if (attributeType.equals(MONTH)) {
      exp = exp.month(i);
    } else if (attributeType.equals(DAY_OF_MONTH)) {
      exp = exp.dayOfMonth(i);
    } else if (attributeType.equals(DAY_OF_WEEK)) {
      exp = exp.dayOfWeek(i);
    } else if (attributeType.equals(YEAR)) {
      exp = exp.year(i);
    } else {
      throw new IllegalStateException(
          "Invalid ScheduleAttributeType:" + attributeType);
    }
    Helper.getLogger()
        .fine("About to create timer with " + attributeType + ": " + i);
    timerService.createCalendarTimer(exp);
    Helper.getLogger().fine("Created timer with attributeType=" + attributeType
        + " and value=" + i);
  }
}
