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
package com.sun.ts.tests.ejb30.timer.common;

import static com.sun.ts.tests.ejb30.common.helper.Helper.assertEquals;
import static com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.DAY_OF_MONTH;
import static com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.DAY_OF_WEEK;
import static com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.HOUR;
import static com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.MINUTE;
import static com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.MONTH;
import static com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.SECOND;
import static com.sun.ts.tests.ejb30.timer.common.ScheduleAttributeType.YEAR;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.logging.Level;

import javax.ejb.ScheduleExpression;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.ejb.TimerService;

import com.sun.ts.tests.ejb30.common.helper.Helper;

public final class TimerUtil {
  private TimerUtil() {
  }

  public static void cancelAllTimers(TimerService timerService,
      boolean failOnError) {
    Collection<Timer> timers = timerService.getTimers();
    for (Iterator<Timer> it = timers.iterator(); it.hasNext();) {
      Timer timer = it.next();
      try {
        timer.cancel();
      } catch (RuntimeException e) {
        if (failOnError) {
          throw e;
        }
        Helper.getLogger().warning(e.toString());
      }
    }
  }

  public static void cancelTimer(TimerService timerService, String name) {
    Collection<Timer> timers = timerService.getTimers();
    for (Iterator<Timer> it = timers.iterator(); it.hasNext();) {
      Timer timer = it.next();
      TimerInfo info = (TimerInfo) timer.getInfo();
      if (info.getTestName().equals(name)) {
        timer.cancel();
      }
    }
  }

  public static int countTimers(final TimerService timerService) {
    return timerService.getTimers().size();
  }

  public static int countTimers(final Collection<Timer> timers,
      final String testName) {
    int result = 0;
    for (Iterator<Timer> it = timers.iterator(); it.hasNext();) {
      Timer t = it.next();
      TimerInfo info = (TimerInfo) t.getInfo();
      if (info != null) {
        if (testName.equals(info.getTestName())) {
          result++;
        }
      }
    }
    return result;
  }

  public static int countTimers(Collection<Timer> timers, TimerInfo info) {
    int result = 0;
    for (Iterator<Timer> it = timers.iterator(); it.hasNext();) {
      Timer timer = it.next();
      TimerInfo fo = (TimerInfo) timer.getInfo();
      if (info == null) {
        if (fo == null) {
          result++;
        }
      } else if (info.equals(fo)) {
        result++;
      }
    }
    return result;
  }

  public static int countTimers(TimerService timerService, TimerInfo info) {
    return countTimers(timerService.getTimers(), info);
  }

  public static int countTimers(Collection<Timer> timers,
      ScheduleExpression exp) {
    int result = 0;
    for (Iterator<Timer> it = timers.iterator(); it.hasNext();) {
      Timer timer = it.next();
      if (timer.isCalendarTimer()) {
        ScheduleExpression ex = timer.getSchedule();
        if (exp == null) {
          if (ex == null) {
            result++;
          }
        } else if (exp.equals(ex)) {
          result++;
        }
      }
    }
    return result;
  }

  public static int countTimers(TimerService timerService,
      ScheduleExpression exp) {
    return countTimers(timerService.getTimers(), exp);
  }

  // can also find a timer with null info
  public static Timer findTimer(final TimerService timerService,
      final TimerInfo info) {
    return findTimer(timerService.getTimers(), info);
  }

  // can also find a timer with null info
  public static Timer findTimer(final Collection<Timer> timers,
      final TimerInfo info) {
    Timer result = null;
    if (info == null) {
      for (Iterator<Timer> it = timers.iterator(); it.hasNext();) {
        Timer t = it.next();
        if (t.getInfo() == null) {
          result = t;
          break;
        }
      }
    } else {
      result = findTimer(timers, info.getTestName());
    }
    return result;
  }

  public static Timer findTimer(final TimerService timerService,
      final ScheduleExpression exp) {
    return findTimer(timerService.getTimers(), exp);
  }

  public static Timer findTimer(final Collection<Timer> timers,
      final ScheduleExpression exp) {
    Timer result = null;
    for (Iterator<Timer> it = timers.iterator(); it.hasNext();) {
      Timer t = it.next();
      if (t.isCalendarTimer()) {
        if (exp.equals(t.getSchedule())) {
          result = t;
          break;
        }
      }
    }
    return result;
  }

  public static Timer findTimer(final TimerService timerService,
      final String testName) {
    return findTimer(timerService.getTimers(), testName);
  }

  public static Timer findTimer(final Collection<Timer> timers,
      final String testName) {
    Timer result = null;
    for (Iterator<Timer> it = timers.iterator(); it.hasNext();) {
      Timer t = it.next();
      Serializable ser = t.getInfo();
      if (ser != null) {
        if (testName.trim().equals(getTimerName(t))) {
          result = t;
          break;
        }
      }
    }
    return result;
  }

  public static Timer findTimer(final TimerService timerService,
      final TimerConfig timerConfig) {
    return findTimer(timerService.getTimers(), timerConfig);
  }

  public static Timer findTimer(final Collection<Timer> timers,
      final TimerConfig timerConfig) {
    return findTimer(timers, (TimerInfo) timerConfig.getInfo());
  }

  public static boolean containsTimer(final Collection<Timer> timers,
      final Timer timer) {
    return timers.contains(timer);
  }

  public static Timer getSingleTimer(TimerService timerService) {
    Collection<Timer> timers = timerService.getTimers();
    if (timers.size() != 1) {
      throw new IllegalStateException(
          "Expecting 1 timer, but actual " + timers.size());
    }
    Timer timer = null;
    for (Iterator<Timer> it = timers.iterator(); it.hasNext();) {
      timer = it.next();
    }
    return timer;
  }

  public static Timer createMillisecondLaterTimer(TimerService timerService,
      String timerName) {
    return createMillisecondLaterTimer(timerService, timerName, true);
  }

  public static Timer createMillisecondLaterTimer(TimerService timerService,
      String timerName, boolean persistent) {
    Date expiration = TimerUtil.getCurrentDatePlus(Calendar.MILLISECOND, 1);
    Helper.getLogger().logp(Level.FINE, "TimerUtil",
        "createMilliSecondLaterTimer", "About to create a timer expiring on "
            + expiration + ", name:" + timerName);
    Timer timer;
    if (persistent) {
      timer = timerService.createTimer(expiration, new TimerInfo(timerName));
    } else {
      timer = timerService.createSingleActionTimer(expiration,
          new TimerConfig(new TimerInfo(timerName), false));
    }
    Helper.getLogger().logp(Level.FINE, "TimerUtil",
        "createMilliSecondLaterTimer",
        "Created a timer expiring on " + expiration + ", name:" + timerName);
    return timer;
  }

  public static Timer createSecondLaterTimer(TimerService timerService) {
    return createSecondLaterTimer(timerService, (TimerInfo) null);
  }

  public static Timer createSecondLaterTimer(TimerService timerService,
      String testName) {
    return createSecondLaterTimer(timerService, new TimerInfo(testName));
  }

  public static Timer createSecondLaterTimer(TimerService timerService,
      TimerConfig timerConfig) {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.SECOND, 1); // next second
    return timerService.createCalendarTimer(getPreciseScheduleExpression(cal),
        timerConfig);
  }

  public static Timer createSecondLaterTimer(TimerService timerService,
      TimerInfo info) {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.SECOND, 1); // next second
    return timerService.createCalendarTimer(getPreciseScheduleExpression(cal),
        new TimerConfig(info, true));
  }

  public static Date getCurrentDatePlus(int calendarUnit, int amount) {
    Calendar cal = Calendar.getInstance();
    if (amount != 0) {
      cal.add(calendarUnit, amount);
    }
    return cal.getTime();
  }

  public static Calendar getCurrentCalendarPlus(int calendarUnit, int amount) {
    Calendar cal = Calendar.getInstance();
    if (amount != 0) {
      cal.add(calendarUnit, amount);
    }
    return cal;
  }

  public static ScheduleExpression getPreciseScheduleExpression(
      Calendar... cals) {
    Calendar cal = (cals.length == 0) ? Calendar.getInstance() : cals[0];
    ScheduleExpression exp = new ScheduleExpression()
        .year(cal.get(Calendar.YEAR)).month(getForSchedule(Calendar.MONTH, cal))
        .dayOfMonth(getForSchedule(Calendar.DAY_OF_MONTH, cal))
        .hour(getForSchedule(Calendar.HOUR_OF_DAY, cal))
        .minute(getForSchedule(Calendar.MINUTE, cal))
        .second(getForSchedule(Calendar.SECOND, cal));
    return exp;
  }

  public static Timer createFarFutureTimer(TimerService timerService) {
    return createFarFutureTimer(timerService, (TimerInfo) null);
  }

  public static Timer createFarFutureTimer(TimerService timerService,
      TimerInfo info) {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, 2); // next, next year
    return timerService.createCalendarTimer(getPreciseScheduleExpression(cal),
        new TimerConfig(info, true));
  }

  public static Timer createFarFutureTimer(TimerService timerService,
      TimerConfig timerConfig) {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.YEAR, 2); // next, next year
    return timerService.createCalendarTimer(getPreciseScheduleExpression(cal),
        timerConfig);
  }

  public static Timer createFarFutureTimer(TimerService timerService,
      String testName) {
    return createFarFutureTimer(timerService, new TimerInfo(testName));
  }

  /**
   * Gets the current date-related values according to the rules of
   * javax.ejb.ScheduleExpression. In java.util.Calendar, DAY_OF_WEEK is 1-7
   * (Sunday-Saturday); MONTH is 0-11 (Jan-Dec)
   * 
   * @param field
   *          a java.util.Calendar field for year, month, hour, etc
   * @param an
   *          optional Calendar that may have been manipulated
   * @return a value that can be used for javax.ejb.ScheduleExpression
   */
  public static int getForSchedule(int field, Calendar... calendars) {
    int result = 0;
    Calendar cal = null;
    if (calendars.length == 0) {
      cal = Calendar.getInstance();
    } else {
      cal = calendars[0];
    }
    result = cal.get(field);
    if (field == Calendar.DAY_OF_WEEK) {
      result--; // 0 and 7 are both Sunday
      if (result == 0) {
        result = (Math.random() < 0.5) ? 0 : 7;
      }
    } else if (field == Calendar.MONTH) {
      result++;
    }
    return result;
  }

  public static void checkScheduleDefaults(ScheduleExpression exp,
      StringBuilder sb) {
    sb.append("Check default schedule attribute values. ");
    assertEquals(SECOND.toString(), ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_0,
        exp.getSecond(), sb);
    assertEquals(MINUTE.toString(), ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_0,
        exp.getMinute(), sb);
    assertEquals(HOUR.toString(), ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_0,
        exp.getHour(), sb);

    assertEquals(MONTH.toString(), ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR,
        exp.getMonth(), sb);
    assertEquals(YEAR.toString(), ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR,
        exp.getYear(), sb);
    assertEquals(DAY_OF_WEEK.toString(),
        ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR, exp.getDayOfWeek(), sb);
    assertEquals(DAY_OF_MONTH.toString(),
        ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR, exp.getDayOfMonth(), sb);

    assertEquals("start", null, exp.getStart(), sb);
    assertEquals("end", null, exp.getEnd(), sb);
  }

  public static int getDateField(int field, Calendar... calendars) {
    Calendar cal = null;
    if (calendars.length == 0) {
      cal = Calendar.getInstance();
    } else {
      cal = calendars[0];
    }
    return cal.get(field);
  }

  public static int getDateField(int field, Date... dates) {
    Calendar cal = null;
    if (dates.length == 0) {
      cal = Calendar.getInstance();
    } else {
      cal = Calendar.getInstance();
      cal.setTime(dates[0]);
    }
    return cal.get(field);
  }

  public static String getTimerName(Timer timer) {
    if (timer == null) {
      return null;
    }
    Serializable info = timer.getInfo();
    if (info == null) {
      return null;
    }
    String result = null;
    if (info instanceof String) {
      result = (String) info;
    } else if (info instanceof TimerInfo) {
      result = ((TimerInfo) info).getTestName();
    } else {
      result = info.toString();
    }
    return (result == null) ? result : result.trim();
  }

  public static String toString(Timer timer) {
    if (timer == null) {
      return null;
    }
    String s = timer.getInfo() + "\tschedule:"
        + (timer.isCalendarTimer() ? timer.getSchedule() : null)
        + "\tnextTimeout:" + timer.getNextTimeout() + "\ttimeRemaining:"
        + timer.getTimeRemaining() + "\tisPersistent:" + timer.isPersistent();
    return s;
  }

  public static String toString(ScheduleExpression exp) {
    if (exp == null) {
      return null;
    }
    Date start = exp.getStart();
    Date end = exp.getEnd();
    return "year=" + exp.getYear() + " month=" + exp.getMonth() + " dayOfMonth="
        + exp.getDayOfMonth() + " dayOfWeek=" + exp.getDayOfWeek() + " hour="
        + exp.getHour() + " minute=" + exp.getMinute() + " second="
        + exp.getSecond() + " start=" + start + " end=" + end + " timezone="
        + exp.getTimezone();
  }

  public static int getNextLeapYear(int... baseYears) {
    GregorianCalendar cal = new GregorianCalendar();
    int baseYear = (baseYears.length == 0) ? cal.get(Calendar.YEAR)
        : baseYears[0];
    for (int i = baseYear + 1;; i++) {
      if (cal.isLeapYear(i)) {
        return i;
      }
    }
  }
}
