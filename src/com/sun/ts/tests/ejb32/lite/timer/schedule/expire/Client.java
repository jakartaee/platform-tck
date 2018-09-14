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

package com.sun.ts.tests.ejb32.lite.timer.schedule.expire;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.timer.common.ClientBase;
import com.sun.ts.tests.ejb30.timer.common.ScheduleValues;
import com.sun.ts.tests.ejb30.timer.common.TimerInfo;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;

import javax.ejb.*;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

/**
 * Some tests use 2100 as a test calendar: January February Su Mo Tu We Th Fr Sa
 * Su Mo Tu We Th Fr Sa 1 2 1 2 3 4 5 6 3 4 5 6 7 8 9 7 8 9 10 11 12 13 10 11 12
 * 13 14 15 16 14 15 16 17 18 19 20 17 18 19 20 21 22 23 21 22 23 24 25 26 27 24
 * 25 26 27 28 29 30 28 31
 * 
 */
public class Client extends ClientBase {
  private static final int WAIT_YEARS = 6;

  @EJB(beanName = "ScheduleBean")
  private ScheduleBean scheduleBean;

  @Override
  public void setup(String[] args, Properties p) {
    super.setup(args, p);
    scheduleBean.cancelAllTimers();
  }

  private void incrementSecond0(String increment,
      long timeRemainingTillNextTimeout) {
    ScheduleExpression exp = new ScheduleExpression()
        .hour(ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR)
        .minute(ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR).second(increment);
    TimerInfo info = new TimerInfo(getTestName());
    info.setLongVar(timeRemainingTillNextTimeout);
    Timer timer = createTimer(exp, info);
    passIfRecurringTimeout();
    scheduleBean.cancelTimer(timer);
  }

  private void incrementMinute0(String increment, Date expectedTimeout,
      Date... starts) {
    ScheduleExpression exp = new ScheduleExpression()
        .hour(ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR).minute(increment);
    if (starts.length > 0) {
      exp = exp.start(starts[0]);
    }
    Timer timer = createTimer(exp);
    verifyNextTimeout(expectedTimeout, timer);
    scheduleBean.cancelTimer(timer);
  }

  private void incrementHour0(String increment, Date expectedTimeout,
      Date... starts) {
    ScheduleExpression exp = new ScheduleExpression().hour(increment);
    if (starts.length > 0) {
      exp = exp.start(starts[0]);
    }
    Timer timer = createTimer(exp);
    verifyNextTimeout(expectedTimeout, timer);
    scheduleBean.cancelTimer(timer);
  }

  private void dayOfWeek0(ScheduleExpression exp, Date expectedNextTimeout,
      String... dayOfWeeks) {
    // all timers created with each dayOfWeek expire at the same
    // expectedNextTimeout
    if (exp == null) {
      exp = new ScheduleExpression();
    }
    // may fall to next month, or even next year. So set month and year to *
    for (String day : dayOfWeeks) {
      exp = exp.year(ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR)
          .month(ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR)
          .dayOfMonth(ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR)
          .dayOfWeek(day);
      Timer timer = createTimer(exp);
      verifyNextTimeout(expectedNextTimeout, timer);
    }
  }

  private void month0(ScheduleExpression exp, Date expectedNextTimeout,
      String... months) {
    // all timers created with each month expire at the same expectedNextTimeout
    if (exp == null) {
      exp = new ScheduleExpression();
    }
    // may fall to next year. So set year to *
    for (String month : months) {
      exp = exp.year(ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR).month(month);
      Timer timer = createTimer(exp);
      verifyNextTimeout(expectedNextTimeout, timer);
    }
  }

  private void year0(ScheduleExpression exp, Date expectedNextTimeout,
      String... years) {
    if (exp == null) {
      exp = new ScheduleExpression();
    }
    for (String year : years) {
      exp = exp.year(year);
      Timer timer = createTimer(exp);
      verifyNextTimeout(expectedNextTimeout, timer);
    }
  }

  private void dayOfMonth0(String[] dayOfMonths, int[] expectedDayOfMonths,
      Calendar cal) {
    for (int i = 0; i < dayOfMonths.length; i++) {
      ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal)
          .dayOfMonth(dayOfMonths[i]);
      Date expectedNextTimeout = cal.getTime();
      expectedNextTimeout = DateUtils.setDays(expectedNextTimeout,
          expectedDayOfMonths[i]);
      Timer timer = createTimer(exp);
      verifyNextTimeout(expectedNextTimeout, timer);
    }
  }

  protected void verifyNextTimeout(Date expected, Timer timer)
      throws RuntimeException {
    final long ignoreableMillis = DateUtils.MILLIS_PER_MINUTE;
    Date actual = scheduleBean.getNextTimeout(timer);
    long actualTimeRemaining = scheduleBean.getTimeRemaining(timer);
    Helper.getLogger().fine(
        "About to compare expected and actual nextTimeout and actualTimeRemaining:"
            + expected + "; " + actual + "; " + actualTimeRemaining);

    appendReason("Compare expected nextTimeout " + expected
        + ", and actual nextTimeout " + actual);
    try {
      assertEquals(null, true,
          DateUtils.isSameInstant(DateUtils.round(expected, Calendar.MINUTE),
              DateUtils.round(actual, Calendar.MINUTE)));
    } catch (RuntimeException e) {
      appendReason(
          "Rounded dates are not equal; next check if they are close.");
      long dif = Math.abs(expected.getTime() - actual.getTime());
      if (dif <= ignoreableMillis) {
        appendReason("The time diff " + dif + " <= ignoreableMillis "
            + ignoreableMillis);
      } else {
        throw new RuntimeException(
            "The time diff " + dif + " > " + ignoreableMillis);
      }
    }
    Helper.assertCloseEnough("Check timeRemaining",
        expected.getTime() - System.currentTimeMillis(), actualTimeRemaining,
        ignoreableMillis, getReasonBuffer());
  }

  protected Timer createTimer(ScheduleExpression exp, TimerInfo... infos) {
    Timer timer = null;
    if (infos.length == 0) {
      // timer = scheduleBean.createTimer(exp, getTestName());
      timer = scheduleBean.createTimer(exp,
          new TimerConfig(new TimerInfo(getTestName()), false));
    } else {
      // timer = scheduleBean.createTimer(exp, infos[0]);
      timer = scheduleBean.createTimer(exp, new TimerConfig(infos[0], false));
    }
    appendReason(TestUtil.NEW_LINE + "Created a timer with expression "
        + TimerUtil.toString(exp));
    return timer;
  }

  /*
   * @testName: startNeverExpires
   * 
   * @test_Strategy: create a timer with year="currentYear - currentYear+1", and
   * start="currentYear+WAIT_YEARS". The start value is beyond the year values,
   * and this timer will never expire. Creating this timer will succeed, but any
   * timer method access in a subsequent business method will result in
   * NoSuchObjectLocalException.
   */
  public void startNeverExpires() {
    Calendar cal = Calendar.getInstance();
    int currentYear = TimerUtil.getForSchedule(Calendar.YEAR, cal);
    cal.add(Calendar.SECOND, 10);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    Date start = TimerUtil.getCurrentDatePlus(Calendar.YEAR, WAIT_YEARS);
    exp.start(start);
    exp.year((currentYear) + " - " + (currentYear + 1));

    Timer timer = createTimer(exp);
    appendReason(scheduleBean.passIfNoMoreTimeouts(timer));
    passIfNoTimeout();
  }

  /*
   * @testName: endNeverExpires
   * 
   * @test_Strategy: create a timer with year="currentYear - currentYear+1", and
   * end="currentYear-1". The end value is prior to the year values, and this
   * timer will never expire. Creating this timer will succeed, but any timer
   * method access in a subsequent business method will result in
   * NoSuchObjectLocalException.
   */

  public void endNeverExpires() {
    Calendar cal = Calendar.getInstance();
    int currentYear = TimerUtil.getForSchedule(Calendar.YEAR, cal);
    cal.add(Calendar.SECOND, 10);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    Date end = DateUtils.setYears(cal.getTime(), cal.get(Calendar.YEAR) - 1);
    exp.end(end);
    exp.year((currentYear) + " - " + (currentYear + 1));

    Timer timer = createTimer(exp);
    appendReason(scheduleBean.passIfNoMoreTimeouts(timer));
    passIfNoTimeout();
  }

  /*
   * @testName: endBeforeActualValues
   * 
   * @test_Strategy: the end date is before the actual values. So the timer
   * should be dead.
   */
  public void endBeforeActualValues() {
    int plusMinutes = 5;
    int currentYear = TimerUtil.getForSchedule(Calendar.YEAR);
    Date end = TimerUtil.getCurrentDatePlus(Calendar.YEAR, plusMinutes);
    end = DateUtils.setYears(end, currentYear + WAIT_YEARS);

    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.SECOND,
        plusMinutes * 2);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    exp.year((currentYear + WAIT_YEARS) + "-" + (currentYear + WAIT_YEARS * 2));
    exp.end(end);

    Timer timer = createTimer(exp);
    appendReason(scheduleBean.passIfNoMoreTimeouts(timer));
  }

  /*
   * @testName: startBeforeActualValues
   * 
   * @test_Strategy: the start attr of the expression represents a time before
   * the first expiration. Verify that the timer does not expire at the start
   * date. If the current date is 1/13/2009 17:46:47, then the schedule is
   * year=2015 - 2021 month=1 dayOfMonth=13 dayOfWeek=* hour=17 minute=46
   * second=47 start Tue Jan 13 17:46:52 EST 2009 end null
   */
  public void startBeforeActualValues() {
    Date start = TimerUtil.getCurrentDatePlus(Calendar.SECOND, 5);
    int currentYear = TimerUtil.getForSchedule(Calendar.YEAR);
    Calendar cal = Calendar.getInstance();
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    exp.year(
        (currentYear + WAIT_YEARS) + " - " + (currentYear + WAIT_YEARS * 2));
    exp.start(start);

    Timer timer = createTimer(exp);
    Date expectedNextTimeout = DateUtils.setYears(cal.getTime(),
        currentYear + WAIT_YEARS);
    verifyNextTimeout(expectedNextTimeout, timer);
    passIfNoTimeout();
  }

  /*
   * @testName: startInTheFuture
   * 
   * @test_Strategy: the start attr of the expression represents a time in the
   * future. Other attrs take defaults. If the current year is 2009, then the
   * schedule is: year=* month=* dayOfMonth=* dayOfWeek=* hour=0 minute=0
   * second=0 start Thu Jan 01 00:00:00 EST 2015 end null
   * 
   * ScheduleExpression is mutable but any modification should not affect timers
   * that were created previously with this schedule.
   */

  public void startInTheFuture() {
    Calendar cal = Calendar.getInstance();
    Date currentDate = cal.getTime();
    Date start = DateUtils.truncate(currentDate, Calendar.YEAR);
    start = DateUtils.setYears(start, cal.get(Calendar.YEAR) + WAIT_YEARS);

    ScheduleExpression exp = new ScheduleExpression().start(start);
    Timer timer = createTimer(exp);
    verifyNextTimeout(start, timer);

    exp.start(currentDate);
    verifyNextTimeout(start, timer);
    passIfNoTimeout();
  }

  /*
   * @testName: startAfterActualValues
   * 
   * @test_Strategy: the start attr of the expression represents a time after
   * the first expiration. Verify that the timer does not start at the current
   * year; it does not start at the start date; it should start at start + 1
   * year. For example,
   * 
   * year=2009 - 2021 month=1 dayOfMonth=14 dayOfWeek=* hour=13 minute=51
   * second=50 start Wed Jan 14 13:51:51 EST 2015
   */
  public void startAfterActualValues() {
    Calendar cal = Calendar.getInstance();
    int currentYear = TimerUtil.getForSchedule(Calendar.YEAR, cal);
    cal.add(Calendar.SECOND, 10);
    Date currentDatePlus10Sec = cal.getTime();
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    Date start = DateUtils.addYears(currentDatePlus10Sec, WAIT_YEARS);
    start = DateUtils.addSeconds(start, 1);
    exp.start(start);
    exp.year((currentYear) + " - " + (currentYear + WAIT_YEARS * 2));

    Timer timer = createTimer(exp);
    int startYear = TimerUtil.getDateField(Calendar.YEAR, start);
    Date expectedNextTimeout = DateUtils.setYears(currentDatePlus10Sec,
        startYear + 1);
    verifyNextTimeout(expectedNextTimeout, timer);
    passIfNoTimeout();
  }

  /*
   * @testName: startAfterActualValues2
   * 
   * @test_Strategy: the start attr of the expression represents a time after
   * the first expiration. Verify that the timer does not start at the current
   * year; it should start a little later after the start date. For example,
   * 
   * year=2009 - 2021 month=1 dayOfMonth=20 dayOfWeek=* hour=10 minute=33
   * second=19 start Tue Jan 20 10:23:19 EST 2015 end null
   */

  public void startAfterActualValues2() {
    int plusMinutes = 10;
    Calendar cal = Calendar.getInstance();
    int currentYear = TimerUtil.getForSchedule(Calendar.YEAR, cal);
    Date currentDate = cal.getTime();
    cal.add(Calendar.MINUTE, plusMinutes);
    Date currentDatePlus = cal.getTime();
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);

    // the start time (excluding year) is before the scheduled time
    Date start = DateUtils.addYears(currentDate, WAIT_YEARS);
    exp.start(start);
    exp.year((currentYear) + " - " + (currentYear + WAIT_YEARS * 2));

    Timer timer = createTimer(exp);
    int startYear = TimerUtil.getDateField(Calendar.YEAR, start);
    Date expectedNextTimeout = DateUtils.setYears(currentDatePlus, startYear);
    verifyNextTimeout(expectedNextTimeout, timer);
    passIfNoTimeout();
  }

  /*
   * @testName: allDefaults
   * 
   * @test_Strategy: create a no-arg ScheduleExpression, which should expire
   * every year, every month, everyday, at 0:0:0 (midnight)
   */
  public void allDefaults() {
    ScheduleExpression exp = new ScheduleExpression();
    Timer timer = createTimer(exp);

    // the next timeout is tomorrow at 0:0:0
    Date expectedNextTimeout = Calendar.getInstance().getTime();
    expectedNextTimeout = DateUtils.addDays(expectedNextTimeout, 1);
    expectedNextTimeout = DateUtils.truncate(expectedNextTimeout,
        Calendar.DAY_OF_MONTH);

    verifyNextTimeout(expectedNextTimeout, timer);
    scheduleBean.cancelTimer(timer);
  }

  /*
   * @testName: timerAccessInTimeoutMethod
   * 
   * @test_Strategy: create a single-event scheduled timer, access timer methods
   * in timeout method.
   */
  public void timerAccessInTimeoutMethod() {
    scheduleBean.cancelAllTimers();

    @SuppressWarnings("unused")
    // Timer timer = scheduleBean.createSecondLaterTimer(getTestName());
    Timer timer = scheduleBean.createSecondLaterTimer(
        new TimerConfig(new TimerInfo(getTestName()), false));
    // ScheduleBean's superclass' timeout method may add a positive timeout
    // record. But ScheduleBean's timeout method does additional verification,
    // which may turn out to be negative.
    TestUtil.sleepMsec((int) (WAIT_FOR_TIMEOUT_STATUS / 2));
    passIfTimeout();
  }

  /*
   * @testName: cancelInTimeoutMethod
   * 
   * @test_Strategy: create a recurring timer, cancel it in timeout method.
   * There should be no more timeout event for this timer. There is only one
   * timeout record.
   */
  public void cancelInTimeoutMethod() {
    Date end = TimerUtil.getCurrentDatePlus(Calendar.MINUTE, 5);
    ScheduleExpression exp = new ScheduleExpression()
        .second(ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR)
        .minute(ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR)
        .hour(ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR).end(end);
    // Timer timer = scheduleBean.createTimer(exp, getTestName());
    Timer timer = scheduleBean.createTimer(exp,
        new TimerConfig(new TimerInfo(getTestName()), false));
    passIfTimeout();
    removeStatusAndRecords();
    appendReason(scheduleBean.passIfNoSuchObjectLocalException(timer));
  }

  /*
   * @testName: leapYears
   * 
   * @test_Strategy: Test 2/29 in 3 leap years (next next leapYear, 2104/2/29,
   * 2104/2/last. For example, year=2013-2016 month=2 dayOfMonth=29 dayOfWeek=*
   * hour=21 minute=15 second=31 start null end null expected nextTimeout Mon
   * Feb 29 21:15:15 EST 2016
   */
  public void leapYears() {
    scheduleBean.cancelAllTimers();
    int nextNextLeapYear = TimerUtil
        .getNextLeapYear(TimerUtil.getNextLeapYear());
    int[] leapYears = { nextNextLeapYear, 2104, 2104 };
    String[] yearRange = { (leapYears[0] - 3) + "-" + leapYears[0],
        (leapYears[1] - 6) + "-" + leapYears[1], String.valueOf(leapYears[2]) };
    String[] dayOfMonths = { "29, 29", "29, 29", "last, last" };

    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.DAY_OF_MONTH, 1);

    for (int i = 0; i < leapYears.length; i++) {
      ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal)
          .dayOfMonth(dayOfMonths[i]).month(2).year(yearRange[i]);

      Date expectedNextTimeout = TimerUtil.getCurrentDatePlus(0, 0);
      expectedNextTimeout = DateUtils.setYears(expectedNextTimeout,
          leapYears[i]);
      expectedNextTimeout = DateUtils.setDays(expectedNextTimeout, 29);
      expectedNextTimeout = DateUtils.setMonths(expectedNextTimeout,
          Calendar.FEBRUARY);
      Timer timer = createTimer(exp);
      verifyNextTimeout(expectedNextTimeout, timer);
    }
    assertEquals("Check # of timers", 3, scheduleBean.getTimers().size());
  }

  /*
   * @testName: dayOfMonth
   * 
   * @test_Strategy: test dayOfMonth (1, <last>, and "last") for the next month
   */
  public void dayOfMonth() {
    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.MONTH, 1);
    int lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    String[] dayOfMonths = { String.valueOf(1), String.valueOf(lastDayOfMonth),
        "last, last" };
    int[] expectedDayOfMonths = { 1, lastDayOfMonth, lastDayOfMonth };
    dayOfMonth0(dayOfMonths, expectedDayOfMonths, cal);
  }

  /*
   * @testName: dayOfMonthNegative
   * 
   * @test_Strategy: test dayOfMonth [-7, -1]
   */
  public void dayOfMonthNegative() {
    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.MONTH, 1);
    int lastDayOfMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    int[] dayOfMonthsInt = ScheduleValues.getSequenceIntArray(-7, -1);
    String[] dayOfMonths = ScheduleValues.intArrayToStringArray(dayOfMonthsInt);
    int[] expectedDayOfMonths = new int[dayOfMonths.length];
    for (int i = 0; i < expectedDayOfMonths.length; i++) {
      expectedDayOfMonths[i] = lastDayOfMonth + dayOfMonthsInt[i];
    }
    dayOfMonth0(dayOfMonths, expectedDayOfMonths, cal);
  }

  /*
   * @testName: dayOfMonthNthDayFeb
   * 
   * @test_Strategy: test 1st Sun, 3rd Sat, etc, using Feb 2100 as a sample.
   */
  public void dayOfMonthNthDayFeb() {

    // Calendar.getInstance() returns a lenient calendar based on the current
    // date.
    // When we modify it (setting month and year), also need to reset the
    // day-of-month to a February-safe value. For example, when running this
    // test
    // on 2010-07-31, the modified calendar will be 2100-02-31 before resetting
    // day-of-month, and Calendar.get(MONTH) will wrap the month value to March.
    // Although the test sets the dayOfMonth field on schedule, the schedule
    // month
    // value is already set to an unexpected value.
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, 2100);
    cal.set(Calendar.DAY_OF_MONTH, 1);
    cal.set(Calendar.MONTH, Calendar.FEBRUARY);

    String[] dayOfMonths = { "1st Mon", "1st Tue", "1st Wed", "1st Thu",
        "1st Fri", "1st Sat", "1st Sun", "2nd Mon", "2nd Tue", "2nd Wed",
        "2nd Thu", "2nd Fri", "2nd Sat", "2nd Sun", "3rd Mon", "3rd Tue",
        "3rd Wed", "3rd Thu", "3rd Fri", "3rd Sat", "3rd Sun", "4th Mon",
        "4th Tue", "4th Wed", "4th Thu", "4th Fri", "4th Sat", "4th Sun" };
    int[] expectedDayOfMonths = ScheduleValues.getSequenceIntArray(1, 28);
    dayOfMonth0(dayOfMonths, expectedDayOfMonths, cal);
    dayOfMonths = null;
    expectedDayOfMonths = null;

    String[] dayOfMonths2 = { "Last Mon", "Last Tue", "Last Wed", "Last Thu",
        "Last Fri", "Last Sat", "Last Sun" };
    int[] expectedDayOfMonths2 = ScheduleValues.getSequenceIntArray(22, 28);
    dayOfMonth0(dayOfMonths2, expectedDayOfMonths2, cal);
  }

  /*
   * @testName: dayOfMonthNthDayJan
   * 
   * @test_Strategy: test 1st Sun, 3rd Sat, etc, using Jan 2100 as a sample.
   */
  public void dayOfMonthNthDayJan() {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, 2100);
    cal.set(Calendar.MONTH, Calendar.JANUARY);
    String[] dayOfMonths = { "1st Fri-1st Mon", "1st Sat", "1st Sun", "1st Mon",
        "1st Tue", "1st Wed", "1st Thu", "2nd Fri", "2nd Sat", "2nd Sun",
        "2nd Mon", "2nd Tue", "2nd Wed", "2nd Thu", "3rd Fri", "3rd Sat",
        "3rd Sun", "3rd Mon", "3rd Tue", "3rd Wed", "3rd Thu", "4th Fri",
        "4th Sat", "4th Sun", "4th Mon", "4th Tue", "4th Wed", "4th Thu",
        "5th Fri", "5th Sat-5th Sat", "5th Sun-Last Sun" };
    int[] expectedDayOfMonths = ScheduleValues.getSequenceIntArray(1, 31);
    dayOfMonth0(dayOfMonths, expectedDayOfMonths, cal);
    dayOfMonths = null;
    expectedDayOfMonths = null;

    String[] dayOfMonths2 = { "Last Mon", "Last Tue", "Last Wed", "Last Thu",
        "Last Fri", "Last Sat", "Last Sun, 5th Sun" };
    int[] expectedDayOfMonths2 = ScheduleValues.getSequenceIntArray(25, 31);
    dayOfMonth0(dayOfMonths2, expectedDayOfMonths2, cal);
  }

  /*
   * @testName: dayOfWeekAll
   * 
   * @test_Strategy: test dayOfWeek [0, 7], and ["Sun", "Sat"] The timer may
   * expire once, twice, 3, or 4times. Check the first expiration
   */
  public void dayOfWeekAll() {
    Calendar cal = Calendar.getInstance();
    for (int i = 0; i < 7; i++) {
      cal.add(Calendar.DAY_OF_WEEK, 1);
      int targetDayOfWeek = TimerUtil.getForSchedule(Calendar.DAY_OF_WEEK, cal);
      String[] targetDayOfWeeks = { String.valueOf(targetDayOfWeek),
          ScheduleValues.dayOfWeekIntToString(targetDayOfWeek) };
      Date expectedNextTimeout = cal.getTime();
      dayOfWeek0(TimerUtil.getPreciseScheduleExpression(cal),
          expectedNextTimeout, targetDayOfWeeks);
    }
  }

  /*
   * @testName: dayOfWeekSunday
   * 
   * @test_Strategy: test dayOfWeek 0, 7, and Sun are refers to Sunday
   */
  public void dayOfWeekSunday() {
    Calendar cal = Calendar.getInstance();
    int currentDayOfWeek = 0;
    do {
      cal.add(Calendar.DAY_OF_WEEK, 1);
      currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    } while (currentDayOfWeek != Calendar.SUNDAY);

    Date nextSun = cal.getTime();
    dayOfWeek0(TimerUtil.getPreciseScheduleExpression(cal), nextSun, "0", "7",
        "SUn");
  }

  /*
   * @testName: dayOfWeekSunday0To7
   * 
   * @test_Strategy: create a schedule timer using dayOfWeek="0-7" to denote all
   * dayOfWeeks.
   */
  public void dayOfWeekSunday0To7() {
    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.SECOND, 5);
    Date expectedNextTimeout = cal.getTime();
    dayOfWeek0(TimerUtil.getPreciseScheduleExpression(cal), expectedNextTimeout,
        "0-7");
  }

  /*
   * @testName: dayOfWeekSunday2
   * 
   * @test_Strategy: test dayOfWeek Sun and start. start is set to the date
   * after the next Sun. So the first expiration should be the next next Sun.
   */
  public void dayOfWeekSunday2() {
    Calendar cal = Calendar.getInstance();
    int currentDayOfWeek = 0;
    do {
      cal.add(Calendar.DAY_OF_WEEK, 1);
      currentDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
    } while (currentDayOfWeek != Calendar.SUNDAY);

    Date nextSun = cal.getTime();
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    cal.add(Calendar.DAY_OF_WEEK, 1);
    exp = exp.start(cal.getTime());
    dayOfWeek0(exp, DateUtils.addDays(nextSun, 7), "7");
  }

  /*
   * @testName: dayOfWeekNow
   * 
   * @test_Strategy: test today's dayOfWeek, create a recurring timer that
   * expires on this year, this month, and this dayOfWeek.
   */
  public void dayOfWeekNow() {
    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.SECOND, 10);
    int targetDayOfWeek = TimerUtil.getForSchedule(Calendar.DAY_OF_WEEK, cal);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    exp = exp.dayOfMonth(ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR)
        .dayOfWeek(targetDayOfWeek);

    Timer timer = createTimer(exp);
    int currentMonth = cal.get(Calendar.MONTH);
    cal.add(Calendar.DAY_OF_WEEK, 7);
    int nextWeekMonth = cal.get(Calendar.MONTH);
    appendReason(
        "currentMonth=" + currentMonth + ", nextWeekMonth=" + nextWeekMonth);
    passIfTimeout();
    if (currentMonth == nextWeekMonth) {
      // still in the same month 7 days later. So a 2nd expiration
      Date expectedNextTimeout = cal.getTime();
      verifyNextTimeout(expectedNextTimeout, timer);
    } else {
      try {
        Date nextTimeout = scheduleBean.getNextTimeout(timer);
        throw new RuntimeException(
            "Expecting EJBException, but got " + nextTimeout);
      } catch (NoSuchObjectLocalException e) {
        appendReason("Got the expected " + e);
      } catch (NoMoreTimeoutsException e) {
        appendReason("Got the expected " + e);
      } catch (EJBException e) {
        // after the first expiration, this timer may or may not have been
        // removed
        Throwable c = e.getCause();
        if (c instanceof NoSuchObjectLocalException
            || c instanceof NoMoreTimeoutsException) {
          appendReason("Got the (wrapped) expected " + c);
        } else {
          throw new RuntimeException(
              "Expecting (wrapped) NoSuchObjectLocalException or NoMoreTimeoutsException, but actual "
                  + c);
        }
      }
    }
  }

  /*
   * @testName: dayOfWeekList
   * 
   * @test_Strategy: create a schedule timer using dayOfWeek=
   * "dayAfter, dayBefore, today". It should expire in a few minutes. The order
   * of the list elements is insignificant (the implementation needs to sort out
   * the earliest possible element, rather than the first element).
   */
  public void dayOfWeekList() {
    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.MINUTE, 5);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    Date expectedTimeout = cal.getTime();

    int currentDayOfWeek = TimerUtil.getForSchedule(Calendar.DAY_OF_WEEK, cal);
    cal.add(Calendar.DAY_OF_WEEK, 1);
    int nextDayOfWeek = TimerUtil.getForSchedule(Calendar.DAY_OF_WEEK, cal);
    cal.add(Calendar.DAY_OF_WEEK, -2);
    int previosDayOfWeek = TimerUtil.getForSchedule(Calendar.DAY_OF_WEEK, cal);
    int[] dayOfWeeks = { nextDayOfWeek, previosDayOfWeek, currentDayOfWeek };
    String[] dayOfWeeksAsNumber = ScheduleValues
        .intArrayToStringArray(dayOfWeeks);
    String[] dayOfWeeksAsText = ScheduleValues.dayOfWeekIntToString(dayOfWeeks);

    dayOfWeek0(exp, expectedTimeout, StringUtils.join(dayOfWeeksAsNumber, ','),
        StringUtils.join(dayOfWeeksAsText, ','));
  }

  /*
   * @testName: dayOfWeekListComplex
   * 
   * @test_Strategy: create a schedule timer using dayOfWeek=
   * "nextDay-2DaysLater, 2DaysBack-1DayBack, today". It should expire in a few
   * minutes.
   */
  public void dayOfWeekListComplex() {
    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.MINUTE, 5);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    Date expectedTimeout = cal.getTime();

    cal.add(Calendar.DAY_OF_WEEK, -3); // rewind to 3 days back
    int[] dayOfWeeks = new int[5];
    for (int i = 0; i < dayOfWeeks.length; i++) {
      cal.add(Calendar.DAY_OF_WEEK, 1);
      dayOfWeeks[i] = TimerUtil.getForSchedule(Calendar.DAY_OF_WEEK, cal);
    }
    String[] dayOfWeeksAsNumber = ScheduleValues
        .intArrayToStringArray(dayOfWeeks);
    String[] dayOfWeeksAsText = ScheduleValues.dayOfWeekIntToString(dayOfWeeks);
    String[][] combinedDayOfWeeks = { dayOfWeeksAsNumber, dayOfWeeksAsText };

    for (String[] days : combinedDayOfWeeks) {
      // array index 0 1 2 3 4
      // dayOfWeek today-2 today-1 today today+1 today+2

      String dayOfWeekVal = days[3] + "-" + days[4] + ", " + days[0] + "-"
          + days[1] + ", " + days[2];
      dayOfWeek0(exp, expectedTimeout, dayOfWeekVal);
    }
  }

  /*
   * @testName: dayOfWeekListOverlap
   * 
   * @test_Strategy: create a schedule timer using dayOfWeek=
   * "2DayBack-1DaysLater, 1DaysBack-2DaysLater". It should expire in a few
   * minutes. This test is similar to dayOfWeekListComplex
   */
  public void dayOfWeekListOverlap() {
    // The following code is the same as in dayOfWeekListComplex
    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.MINUTE, 5);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    Date expectedTimeout = cal.getTime();

    cal.add(Calendar.DAY_OF_WEEK, -3); // rewind to 3 days back
    int[] dayOfWeeks = new int[5];
    for (int i = 0; i < dayOfWeeks.length; i++) {
      cal.add(Calendar.DAY_OF_WEEK, 1);
      dayOfWeeks[i] = TimerUtil.getForSchedule(Calendar.DAY_OF_WEEK, cal);
    }
    String[] dayOfWeeksAsNumber = ScheduleValues
        .intArrayToStringArray(dayOfWeeks);
    String[] dayOfWeeksAsText = ScheduleValues.dayOfWeekIntToString(dayOfWeeks);
    String[][] combinedDayOfWeeks = { dayOfWeeksAsNumber, dayOfWeeksAsText };

    // The following code is different than dayOfWeekListComplex
    for (String[] days : combinedDayOfWeeks) {
      // array index 0 1 2 3 4
      // dayOfWeek today-2 today-1 today today+1 today+2

      String dayOfWeekVal = days[0] + "-" + days[3] + ", " + days[1] + "-"
          + days[4];
      dayOfWeek0(exp, expectedTimeout, dayOfWeekVal);
    }
  }

  /*
   * @testName: dayOfWeekRange
   * 
   * @test_Strategy: create a schedule timer using dayOfWeek=
   * "prevPrevDay-today". It should expire in a few minutes. The range may wrap
   * around (e.g., Sat-Mon)
   */
  public void dayOfWeekRange() {
    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.MINUTE, 5);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    Date expectedTimeout = cal.getTime();

    int thisDayOfWeek = TimerUtil.getForSchedule(Calendar.DAY_OF_WEEK, cal);
    cal.add(Calendar.DAY_OF_WEEK, -2);
    int prevPrevDayOfWeek = TimerUtil.getForSchedule(Calendar.DAY_OF_WEEK, cal);

    String dayOfWeeksAsNumber = prevPrevDayOfWeek + "-" + thisDayOfWeek;
    String dayOfWeeksAsText = ScheduleValues
        .dayOfWeekIntToString(prevPrevDayOfWeek) + "-"
        + ScheduleValues.dayOfWeekIntToString(thisDayOfWeek);

    dayOfWeek0(exp, expectedTimeout, dayOfWeeksAsNumber, dayOfWeeksAsText);
  }

  /*
   * @testName: monthList
   * 
   * @test_Strategy: create a schedule timer using month=
   * "monthAfter, monthBefore, thisMonth". It should expire in a few minutes.
   * The order of the list elements is insignificant (the implementation needs
   * to sort out the earliest possible element, rather than the first element).
   */
  public void monthList() {
    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.MINUTE, 5);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    Date expectedTimeout = cal.getTime();

    int currentMonth = TimerUtil.getForSchedule(Calendar.MONTH, cal);
    cal.add(Calendar.MONTH, 1);
    int nextMonth = TimerUtil.getForSchedule(Calendar.MONTH, cal);
    cal.add(Calendar.MONTH, -2);
    int previosMonth = TimerUtil.getForSchedule(Calendar.MONTH, cal);
    int[] months = { nextMonth, previosMonth, currentMonth };
    String[] monthsAsNumber = ScheduleValues.intArrayToStringArray(months);
    String[] monthsAsText = ScheduleValues.monthIntToString(months);

    month0(exp, expectedTimeout, StringUtils.join(monthsAsNumber, ','),
        StringUtils.join(monthsAsText, ','));
  }

  /*
   * @testName: monthRange
   * 
   * @test_Strategy: create a schedule timer using month=
   * "prevPrevMonth-thisMonth". It should expire in a few minutes. The range may
   * wrap around (e.g., Dec-Feb)
   */
  public void monthRange() {
    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.MINUTE, 5);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    Date expectedTimeout = cal.getTime();

    int thisMonth = TimerUtil.getForSchedule(Calendar.MONTH, cal);
    cal.add(Calendar.MONTH, -2);
    int prevPrevMonth = TimerUtil.getForSchedule(Calendar.MONTH, cal);

    String monthsAsNumber = prevPrevMonth + "-" + thisMonth;
    String monthsAsText = ScheduleValues.monthIntToString(prevPrevMonth) + "-"
        + ScheduleValues.monthIntToString(thisMonth);
    month0(exp, expectedTimeout, monthsAsNumber, monthsAsText);

    monthsAsNumber = thisMonth + "-" + thisMonth;
    monthsAsText = ScheduleValues.monthIntToString(thisMonth) + "-"
        + ScheduleValues.monthIntToString(thisMonth);
    month0(exp, expectedTimeout, monthsAsNumber, monthsAsText);
  }

  /*
   * @testName: monthListComplex
   * 
   * @test_Strategy: create a schedule timer using month=
   * "nextMonth-2MonthsLater, 2MonthsBack-1MonthBack, thisMonth". It should
   * expire in a few minutes.
   */
  public void monthListComplex() {
    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.MINUTE, 5);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    Date expectedTimeout = cal.getTime();

    cal.add(Calendar.MONTH, -3); // rewind to 3 months back
    int[] months = new int[5];
    for (int i = 0; i < months.length; i++) {
      cal.add(Calendar.MONTH, 1);
      months[i] = TimerUtil.getForSchedule(Calendar.MONTH, cal);
    }
    String[] monthsAsNumber = ScheduleValues.intArrayToStringArray(months);
    String[] monthsAsText = ScheduleValues.monthIntToString(months);

    String[][] combinedMonths = { monthsAsNumber, monthsAsText };
    for (String[] m : combinedMonths) {
      // array index 0 1 2 3 4
      // month -2 -1 current +1 +2

      String monthVal = m[3] + "-" + m[4] + ", " + m[0] + "-" + m[1] + ", "
          + m[2];
      month0(exp, expectedTimeout, monthVal);
    }
  }

  /*
   * @testName: monthListOverlap
   * 
   * @test_Strategy: create a schedule timer using month=
   * "2MonthsBack-1MonthsLater, 1MonthsBack-2MonthsLater". It should expire in a
   * few minutes. This test is similar to monthListComplex
   */
  public void monthListOverlap() {
    // The following code is the same as in dayOfWeekListComplex
    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.MINUTE, 5);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    Date expectedTimeout = cal.getTime();

    cal.add(Calendar.MONTH, -3); // rewind to 3 months back
    int[] months = new int[5];
    for (int i = 0; i < months.length; i++) {
      cal.add(Calendar.MONTH, 1);
      months[i] = TimerUtil.getForSchedule(Calendar.MONTH, cal);
    }
    String[] monthsAsNumber = ScheduleValues.intArrayToStringArray(months);
    String[] monthsAsText = ScheduleValues.monthIntToString(months);
    String[][] combinedMonths = { monthsAsNumber, monthsAsText };

    // The following code is different than dayOfWeekListComplex
    for (String[] m : combinedMonths) {
      // array index 0 1 2 3 4
      // month -2 -1 current +1 +2

      String monthVal = m[0] + "-" + m[3] + ", " + m[1] + "-" + m[4];
      month0(exp, expectedTimeout, monthVal);
    }
  }

  /*
   * @testName: monthAll
   * 
   * @test_Strategy: test the last day of each month [1,12; Jan-Dec]
   */
  public void monthAll() {
    Calendar cal = Calendar.getInstance();
    for (int i = 0; i < 24; i++) {
      cal.add(Calendar.MONTH, 1);
      int targetMonth = TimerUtil.getForSchedule(Calendar.MONTH, cal);
      String[] targetMonths = { String.valueOf(targetMonth),
          ScheduleValues.monthIntToString(targetMonth) };
      cal.set(Calendar.DAY_OF_MONTH,
          cal.getActualMaximum(Calendar.DAY_OF_MONTH));
      Date expectedNextTimeout = cal.getTime();
      for (String month : targetMonths) {
        ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal)
            .month(month).dayOfMonth("last");
        Timer timer = createTimer(exp);
        verifyNextTimeout(expectedNextTimeout, timer);
      }
    }
  }

  /*
   * @testName: yearList
   * 
   * @test_Strategy: create a schedule timer using year=
   * "yearAfter, yearBefore, thisYear". It should expire in a few minutes. The
   * order of the list elements is insignificant (the implementation needs to
   * sort out the earliest possible element, rather than the first element).
   */
  public void yearList() {
    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.MINUTE, 5);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    Date expectedTimeout = cal.getTime();

    int currentYear = TimerUtil.getForSchedule(Calendar.YEAR, cal);
    cal.add(Calendar.YEAR, 1);
    int nextYear = TimerUtil.getForSchedule(Calendar.YEAR, cal);
    cal.add(Calendar.YEAR, -2);
    int previosYear = TimerUtil.getForSchedule(Calendar.YEAR, cal);
    int[] years = { nextYear, previosYear, currentYear };
    String[] yearsAsNumber = ScheduleValues.intArrayToStringArray(years);
    year0(exp, expectedTimeout, StringUtils.join(yearsAsNumber, ','));
  }

  /*
   * @testName: yearRange
   * 
   * @test_Strategy: create a schedule timer using year=
   * "prevPrevYear-thisYear". It should expire in a few minutes. The range may
   * wrap around (e.g., Dec-Feb)
   */
  public void yearRange() {
    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.MINUTE, 5);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    Date expectedTimeout = cal.getTime();

    int thisYear = TimerUtil.getForSchedule(Calendar.YEAR, cal);
    cal.add(Calendar.YEAR, -2);
    int prevPrevYear = TimerUtil.getForSchedule(Calendar.YEAR, cal);

    String yearsAsNumber = prevPrevYear + "-" + thisYear;
    year0(exp, expectedTimeout, yearsAsNumber);

    yearsAsNumber = thisYear + "-" + thisYear;
    year0(exp, expectedTimeout, yearsAsNumber);
  }

  /*
   * @testName: yearListComplex
   * 
   * @test_Strategy: create a schedule timer using year=
   * "nextYear-2YearsLater, 2YearsBack-1YearBack, thisYear". It should expire in
   * a few minutes.
   */
  public void yearListComplex() {
    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.MINUTE, 5);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    Date expectedTimeout = cal.getTime();

    cal.add(Calendar.YEAR, -3); // rewind to 3 years back
    int[] years = new int[5];
    for (int i = 0; i < years.length; i++) {
      cal.add(Calendar.YEAR, 1);
      years[i] = TimerUtil.getForSchedule(Calendar.YEAR, cal);
    }
    String[] yearsAsNumber = ScheduleValues.intArrayToStringArray(years);
    // array index 0 1 2 3 4
    // year -2 -1 current +1 +2

    String yearVal = yearsAsNumber[3] + "-" + yearsAsNumber[4] + ", "
        + yearsAsNumber[0] + "-" + yearsAsNumber[1] + ", " + yearsAsNumber[2];
    year0(exp, expectedTimeout, yearVal);
  }

  /*
   * @testName: yearListOverlap
   * 
   * @test_Strategy: create a schedule timer using year=
   * "2YearsBack-1YearsLater, 1YearsBack-2YearsLater". It should expire in a few
   * minutes. This test is similar to yearListComplex
   */
  public void yearListOverlap() {
    Calendar cal = TimerUtil.getCurrentCalendarPlus(Calendar.MINUTE, 5);
    ScheduleExpression exp = TimerUtil.getPreciseScheduleExpression(cal);
    Date expectedTimeout = cal.getTime();

    cal.add(Calendar.YEAR, -3); // rewind to 3 years back
    int[] years = new int[5];
    for (int i = 0; i < years.length; i++) {
      cal.add(Calendar.YEAR, 1);
      years[i] = TimerUtil.getForSchedule(Calendar.YEAR, cal);
    }
    String[] yearsAsNumber = ScheduleValues.intArrayToStringArray(years);

    // array index 0 1 2 3 4
    // year -2 -1 current +1 +2

    String yearVal = yearsAsNumber[0] + "-" + yearsAsNumber[3] + ", "
        + yearsAsNumber[1] + "-" + yearsAsNumber[4];
    year0(exp, expectedTimeout, yearVal);
  }

  /*
   * @testName: incrementSecond1
   * 
   * @test_Strategy:
   */
  public void incrementSecond1() {
    incrementSecond0("*/10", 10 * 1000);
  }

  /*
   * @testName: incrementSecond2
   * 
   * @test_Strategy:
   */
  public void incrementSecond2() {
    incrementSecond0("0/10", 10 * 1000);
  }

  /*
   * @testName: incrementSecond3
   * 
   * @test_Strategy:
   */
  public void incrementSecond3() {
    incrementSecond0("25/35", 60 * 1000);
  }

  /*
   * @testName: incrementMinute1
   * 
   * @test_Strategy: start the incremental timer with start set to 1 minute
   * before {10, 20, 30, etc}.
   */
  public void incrementMinute1() {
    String[] increments = { "*/10", "0/10" };
    for (String increment : increments) {
      Calendar cal = Calendar.getInstance();
      int currentMinute = cal.get(Calendar.MINUTE);
      do {
        cal.add(Calendar.MINUTE, 1);
        currentMinute = cal.get(Calendar.MINUTE);
      } while (currentMinute % 10 != 0);

      cal.set(Calendar.SECOND, 0);
      Date expectedTimeout = cal.getTime();
      cal.add(Calendar.MINUTE, -1);
      cal.set(Calendar.SECOND, 1);
      Date start = cal.getTime();
      incrementMinute0(increment, expectedTimeout, start);
    }
  }

  /*
   * @testName: incrementMinute2
   * 
   * @test_Strategy: start the incremental timer with start set to 1 minute
   * after 25 minute. Verify the first timeout is about 1 hour later, and minute
   * 60 is not included (there is no minute 60).
   */
  public void incrementMinute2() {
    String increment = "25/35";
    Calendar cal = Calendar.getInstance();
    int currentMinute = cal.get(Calendar.MINUTE);
    do {
      cal.add(Calendar.MINUTE, 1);
      currentMinute = cal.get(Calendar.MINUTE);
    } while (currentMinute != 25);

    cal.add(Calendar.HOUR, 1);
    cal.set(Calendar.SECOND, 0);
    Date expectedTimeout = cal.getTime();

    cal.add(Calendar.HOUR, -1);
    cal.add(Calendar.MINUTE, 1);
    cal.set(Calendar.SECOND, 1);
    Date start = cal.getTime();
    incrementMinute0(increment, expectedTimeout, start);
  }

  /*
   * @testName: incrementHour1
   * 
   * @test_Strategy:
   */
  public void incrementHour1() {
    String[] increments = { "*/3", "0/3" };
    for (String increment : increments) {
      Calendar cal = Calendar.getInstance();
      int currentHour = cal.get(Calendar.HOUR);
      do {
        cal.add(Calendar.HOUR, 1);
        currentHour = cal.get(Calendar.HOUR);
      } while (currentHour % 3 != 0);

      cal.set(Calendar.SECOND, 0);
      cal.set(Calendar.MINUTE, 0);
      Date expectedTimeout = cal.getTime();
      cal.add(Calendar.MINUTE, -1);
      cal.set(Calendar.SECOND, 1);
      Date start = cal.getTime();
      incrementHour0(increment, expectedTimeout, start);
    }
  }

  /*
   * @testName: incrementHour2
   * 
   * @test_Strategy:
   */
  public void incrementHour2() {
    String increment = "22/2";
    Calendar cal = Calendar.getInstance();
    int currentHour = cal.get(Calendar.HOUR_OF_DAY);
    do {
      cal.add(Calendar.HOUR_OF_DAY, 1);
      currentHour = cal.get(Calendar.HOUR_OF_DAY);
    } while (currentHour != 22);

    cal.set(Calendar.MINUTE, 0);
    cal.set(Calendar.SECOND, 0);
    Date expectedTimeout = cal.getTime();

    cal.add(Calendar.MINUTE, -1);
    cal.set(Calendar.SECOND, 1);
    Date start = cal.getTime();
    incrementHour0(increment, expectedTimeout, start);
  }

}
