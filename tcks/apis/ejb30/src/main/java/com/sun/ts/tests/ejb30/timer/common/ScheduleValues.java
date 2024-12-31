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

import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumSet;

public class ScheduleValues {
  private static final String NL = System.getProperty("line.separator");

  private static final String WILD_CARD = " * ";

  private static final String[] INVALID_COMMON_STRING = { null, "", " ", "0.1",
      "1d", "1.0", "?", "%", "$", "!", "&", "-", "/", ",", ".", "1-", "1-2-3",
      "1+2", "3/4-5", "1-*", "5-6*", "*-*", "**", "*-4", "*-", "*,1", "1,*",
      "5/*", "1, 2/2", "1-2/2" }; // let's allow numbers in the form of 01, 02,
                                  // 03, etc

  // string values are case insensitive. Whitespaces are ignored
  public static final String[] VALID_FEB_29_4000 = new String[] { " FEb  ",
      " 29 ", "  4000  " };

  public static final String DEFAULT_ATTRIBUTE_VALUE_0 = "0";

  public static final String DEFAULT_ATTRIBUTE_VALUE_STAR = "*";

  /////////////////////////////////////////////////////////////////////
  // second [0,59]
  /////////////////////////////////////////////////////////////////////
  private static final String[] SECOND_LIST_RANGE_INCREMENT = { "0, 59",
      "0,10,  55", "58,47,3,   33", " 0 -59", "58-   59",
      " \t \t  2 \t \t - \t \t 3 \t \t ", "*/5", "30/19", " * \t/\t 5 ",
      " 10\t/\t19 ", "2-1", "1, 8 - 9, 1", "2-3, 3-4, 4-5, 4", "59-0",
      "1, 59-1, 3-5" };

  private static final int[] INVALID_SECOND_INT = { -1, 60, Integer.MAX_VALUE,
      Integer.MIN_VALUE };

  private static final String[] INVALID_SECOND_STRING = { "0,60", "2,*",
      "50-60", "1--2", "-2-1", "60/2", "-1/2" };

  /////////////////////////////////////////////////////////////////////
  // minute [0,59]
  /////////////////////////////////////////////////////////////////////
  private static final String[] MINUTE_LIST_RANGE = SECOND_LIST_RANGE_INCREMENT;

  private static final int[] INVALID_MINUTE_INT = INVALID_SECOND_INT;

  private static final String[] INVALID_MINUTE_STRING = INVALID_SECOND_STRING;

  /////////////////////////////////////////////////////////////////////
  // hour [0,23]
  /////////////////////////////////////////////////////////////////////
  private static final String[] HOUR_LIST_RANGE_INCREMENT = { "  0 ,  23 ",
      "2,11,22", "23,15,4,9", "0-23", "22-23", "7-2", "2-1", "1, 3-2, 2-3",
      "0\t\t,\t\t2\t,\t4\t", "*/5", "0/12", " * \t/\t 5 ", " 0\t/\t12 " };

  private static final int[] INVALID_HOUR_INT = { -1, 24, Integer.MAX_VALUE,
      Integer.MIN_VALUE };

  private static final String[] INVALID_HOUR_STRING = { "0,24", "22-24", "1--2",
      "24/1", "-1/1" };

  /////////////////////////////////////////////////////////////////////
  // dayOfMonth [1,31]
  /////////////////////////////////////////////////////////////////////
  private static final String[] DAY_OF_MONTH_LIST_RANGE = { "1, 31", "2,11,22",
      "31,15,4,9", "-3,-7, -2", "3rd Wed, 1st Tue, Last", "Last, 1, 2nd Mon",
      " 1 - 31 ", "30-31", "20-last", "1st   Mon-last   Fri", "lasT",
      "last  Tue", "last \twed", "last " + NL + "thu", "-7--1", "1--2",
      "-7-last", "1st Mon - Last", "-1--7", "2-1", "17-Last, Last-5",
      "Last Fri - 1st Mon, Last Thu - 2nd Wed, 3rd Sat" };

  private static final int[] INVALID_DAY_OF_MONTH_INT = { -8, 0, 32,
      Integer.MAX_VALUE, Integer.MIN_VALUE };

  private static final String[] INVALID_DAY_OF_MONTH_STRING = { "1st", "2nd",
      "3rd", "4th", "5th", "6th", "First", "6th Mon", "Last-", "-2, *",
      "1st Sun, *, 2nd Mon", "\"Last\"", "\"1\"", "1,32", "0,8,30", "0-23",
      "8-32", "1/2" };

  /////////////////////////////////////////////////////////////////////
  // month [1,12] or {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
  // "Sep", "Oct", "Nov", "Dec"}
  /////////////////////////////////////////////////////////////////////
  private static final String[] MONTH_VALUE_LIST_RANGE = { "JAN", "Feb", "maR",
      "apr", "may", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec", "2,9,11",
      "maR,jUn,Nov", "9,11,8", "Jun,Nov,Oct", "11-12", "NOV-dec", "1-12",
      "Sep - auG", "9-1, 5-6", "12-3, 4-6, Sep, Oct-Nov", "2-1" };

  private static final int[] INVALID_MONTH_INT = { -1, 0, 13, Integer.MAX_VALUE,
      Integer.MIN_VALUE };

  private static final String[] INVALID_MONTH_STRING = { "1,13", "0,8,9",
      "5-13", "0-5", "January", "February", "March", "April", "June", "July",
      "August", "September", "October", "November", "December", "Jan-*",
      "*-Jan", "January-May", "Jan,February,May", "1--2", "1/2" };

  /////////////////////////////////////////////////////////////////////
  // dayOfWeek [0,7] or {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}
  /////////////////////////////////////////////////////////////////////
  private static final String[] DAY_OF_WEEK_VALUE_LIST_RANGE = { "Sun", "Mon",
      "Tue", "Wed", "Thu", "Fri", "Sat", "0,3,6", "Sun,Wed,Sat, Sun", "7,4,2",
      "Sun,Thu,Tue", "0-6", "2-1", "1-7", "Sun-Sat", "Sat-Sun", "Sun  -  Mon",
      "6-5", "Fri-Tue, Mon, Mon-Fri", "0, 6-5, 5-4", "Mon, 4, 4-6", "1-Wed",
      "7-Tue" };

  private static final int[] INVALID_DAY_OF_WEEK_INT = { -1, 8,
      Integer.MAX_VALUE, Integer.MIN_VALUE };

  private static final String[] INVALID_DAY_OF_WEEK_STRING = { "0,8", "-1,3,4",
      "0,4,8", "Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday",
      "Saturday", "Mon-*", "*-Fri", "Mon-Friday", "Mon,Wed,Friday", "*,Mon",
      "1--2", "1/2" };

  /////////////////////////////////////////////////////////////////////
  // year a 4-digit calendar year
  /////////////////////////////////////////////////////////////////////
  private static final String[] YEAR_LIST_RANGE = { "3000,9999",
      "8080,9000,9009", "9009,9000,9050", "1970-9999", "9998-9999",
      "2050, 2000-2010, 2020-2030, 2050" };

  private static final int[] INVALID_YEAR_INT = { 0, 99, -2009, 10000 };

  private static final String[] INVALID_YEAR_STRING = { "200*", "200?",
      "2008,09", "08-09", "1--2", "2020/2", "10/2" };

  /////////////////////////////////////////////////////////////////////
  // public methods
  /////////////////////////////////////////////////////////////////////
  public static int[] validSecondValuesInt() {
    return getSequenceIntArray(0, 59);
  }

  public static int[] invalidSecondValuesInt() {
    return INVALID_SECOND_INT;
  }

  public static String[] validSecondValuesString() {
    return combineIntAndStringValues(validSecondValuesInt(),
        SECOND_LIST_RANGE_INCREMENT, WILD_CARD);
  }

  public static String[] invalidSecondValuesString() {
    return combineIntAndStringValues(invalidSecondValuesInt(),
        INVALID_SECOND_STRING, INVALID_COMMON_STRING);
  }

  public static int[] validMinuteValuesInt() {
    return getSequenceIntArray(0, 59);
  }

  public static int[] invalidMinuteValuesInt() {
    return INVALID_MINUTE_INT;
  }

  public static String[] validMinuteValuesString() {
    return combineIntAndStringValues(validMinuteValuesInt(), MINUTE_LIST_RANGE,
        WILD_CARD);
  }

  public static String[] invalidMinuteValuesString() {
    return combineIntAndStringValues(invalidMinuteValuesInt(),
        INVALID_MINUTE_STRING, INVALID_COMMON_STRING);
  }

  public static int[] validHourValuesInt() {
    return getSequenceIntArray(0, 23);
  }

  public static int[] invalidHourValuesInt() {
    return INVALID_HOUR_INT;
  }

  public static String[] validHourValuesString() {
    return combineIntAndStringValues(validHourValuesInt(),
        HOUR_LIST_RANGE_INCREMENT, WILD_CARD);
  }

  public static String[] invalidHourValuesString() {
    return combineIntAndStringValues(invalidHourValuesInt(),
        INVALID_HOUR_STRING, INVALID_COMMON_STRING);
  }

  public static int[] validDayOfMonthValuesInt() {
    int[] daysBefore = getSequenceIntArray(-7, -1);
    int[] daysRange = getSequenceIntArray(1, 31);
    int[] allValid = new int[daysBefore.length + daysRange.length];
    System.arraycopy(daysBefore, 0, allValid, 0, daysBefore.length);
    System.arraycopy(daysRange, 0, allValid, daysBefore.length,
        daysRange.length);
    return allValid;
  }

  public static int[] invalidDayOfMonthValuesInt() {
    return INVALID_DAY_OF_MONTH_INT;
  }

  public static String[] validDayOfMonthValuesString() {
    String[] sequences = { "1st", "2nd", "3rd", "4th", "5th", "Last" };
    String[] weekDays = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat" };
    String[] products = new String[sequences.length * weekDays.length];
    for (int i = 0; i < products.length;) {
      for (String seq : sequences) {
        for (String wkd : weekDays) {
          products[i] = seq + " " + wkd;
          i++;
        }
      }
    }
    String[] valid1 = combineIntAndStringValues(validDayOfMonthValuesInt(),
        DAY_OF_MONTH_LIST_RANGE, WILD_CARD);
    String[] allValid = new String[valid1.length + products.length];
    System.arraycopy(valid1, 0, allValid, 0, valid1.length);
    System.arraycopy(products, 0, allValid, valid1.length, products.length);
    return allValid;
  }

  public static String[] invalidDayOfMonthValuesString() {
    return combineIntAndStringValues(invalidDayOfMonthValuesInt(),
        INVALID_DAY_OF_MONTH_STRING, INVALID_COMMON_STRING);
  }

  public static int[] validMonthValuesInt() {
    return getSequenceIntArray(1, 12);
  }

  public static int[] invalidMonthValuesInt() {
    return INVALID_MONTH_INT;
  }

  public static String[] validMonthValuesString() {
    return combineIntAndStringValues(validMonthValuesInt(),
        MONTH_VALUE_LIST_RANGE, WILD_CARD);
  }

  public static String[] invalidMonthValuesString() {
    return combineIntAndStringValues(invalidMonthValuesInt(),
        INVALID_MONTH_STRING, INVALID_COMMON_STRING);
  }

  public static int[] validDayOfWeekValuesInt() {
    return getSequenceIntArray(0, 7); // 0 and 7 are both Sunday
  }

  public static int[] invalidDayOfWeekValuesInt() {
    return INVALID_DAY_OF_WEEK_INT;
  }

  public static String[] validDayOfWeekValuesString() {
    return combineIntAndStringValues(validDayOfWeekValuesInt(),
        DAY_OF_WEEK_VALUE_LIST_RANGE, WILD_CARD);
  }

  public static String[] invalidDayOfWeekValuesString() {
    return combineIntAndStringValues(invalidDayOfWeekValuesInt(),
        INVALID_DAY_OF_WEEK_STRING, INVALID_COMMON_STRING);
  }

  public static int[] validYearValuesInt() {
    return getSequenceIntArray(2999, 3010);
  }

  public static int[] invalidYearValuesInt() {
    return INVALID_YEAR_INT;
  }

  public static String[] validYearValuesString() {
    return combineIntAndStringValues(validYearValuesInt(), YEAR_LIST_RANGE,
        WILD_CARD);
  }

  public static String[] invalidYearValuesString() {
    return combineIntAndStringValues(invalidYearValuesInt(),
        INVALID_YEAR_STRING, INVALID_COMMON_STRING);
  }

  /////////////////////////////////////////////////////////////////////
  // utility methods
  /////////////////////////////////////////////////////////////////////
  public static String[] combineIntAndStringValues(final int[] a1,
      final String[] a2, String... additional) {
    int len = a1.length + a2.length + additional.length;
    String[] values = new String[len];
    for (int i = 0; i < a1.length; i++) {
      values[i] = Integer.toString(a1[i]);
    }
    System.arraycopy(a2, 0, values, a1.length, a2.length);
    if (additional.length > 0) {
      System.arraycopy(additional, 0, values, a1.length + a2.length,
          additional.length);
    }
    return values;
  }

  public static int[] getSequenceIntArray(int start, int end) {
    int[] values = new int[end - start + 1];
    for (int i = 0, j = values.length; i < j; i++) {
      values[i] = start++;
    }
    return values;
  }

  // dayOfWeek is based on ScheduleExpression rules, not java Calendar rules
  public static String dayOfWeekIntToString(int dayOfWeek) {
    if (dayOfWeek < 0 || dayOfWeek > 7) {
      throw new RuntimeException("dayOfWeek out of range [0, 7]: " + dayOfWeek);
    }
    String[] dayOfWeekStrings = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fri",
        "Sat", "Sun" };
    return dayOfWeekStrings[dayOfWeek];
  }

  public static String[] dayOfWeekIntToString(int[] dayOfWeeks) {
    String[] dayOfWeeksAsText = new String[dayOfWeeks.length];
    for (int i = 0; i < dayOfWeeks.length; i++) {
      dayOfWeeksAsText[i] = dayOfWeekIntToString(dayOfWeeks[i]);
    }
    return dayOfWeeksAsText;
  }

  // month is based on ScheduleExpression rules, not java Calendar rules
  public static String monthIntToString(int month) {
    if (month < 1 || month > 12) {
      throw new RuntimeException("month out of range [1, 12]: " + month);
    }
    String[] monthStrings = { "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
        "Aug", "Sep", "Oct", "Nov", "Dec" };
    return monthStrings[month - 1];
  }

  public static String[] monthIntToString(int[] months) {
    String[] monthsAsText = new String[months.length];
    for (int i = 0; i < months.length; i++) {
      monthsAsText[i] = monthIntToString(months[i]);
    }
    return monthsAsText;
  }

  public static String[] intArrayToStringArray(int[] ints) {
    String[] strings = new String[ints.length];
    for (int i = 0; i < ints.length; i++) {
      strings[i] = String.valueOf(ints[i]);
    }
    return strings;
  }

  /* For testing only */
  public static void main(String[] args) {
    StringBuilder sb = new StringBuilder();
    sb.append("\n validSecondValuesInt:\n");
    sb.append(Arrays.toString(validSecondValuesInt()));
    sb.append("\n validSecondValuesString:\n");
    sb.append(Arrays.toString(validSecondValuesString()));

    sb.append("\n validMinuteValuesInt:\n");
    sb.append(Arrays.toString(validMinuteValuesInt()));
    sb.append("\n validMinuteValuesString:\n");
    sb.append(Arrays.toString(validMinuteValuesString()));

    sb.append("\n validHourValuesInt:\n");
    sb.append(Arrays.toString(validHourValuesInt()));
    sb.append("\n validHourValuesString:\n");
    sb.append(Arrays.toString(validHourValuesString()));

    sb.append("\n validDayOfMonthValuesInt:\n");
    sb.append(Arrays.toString(validDayOfMonthValuesInt()));
    sb.append("\n validDayOfMonthValuesString:\n");
    sb.append(Arrays.toString(validDayOfMonthValuesString()));

    sb.append("\n validMonthValuesInt:\n");
    sb.append(Arrays.toString(validMonthValuesInt()));
    sb.append("\n validMonthValuesString:\n");
    sb.append(Arrays.toString(validMonthValuesString()));

    sb.append("\n validDayOfWeekValuesInt:\n");
    sb.append(Arrays.toString(validDayOfWeekValuesInt()));
    sb.append("\n validDayOfWeekValuesString:\n");
    sb.append(Arrays.toString(validDayOfWeekValuesString()));

    sb.append("\n validYearValuesInt:\n");
    sb.append(Arrays.toString(validYearValuesInt()));
    sb.append("\n validYearValuesString:\n");
    sb.append(Arrays.toString(validYearValuesString()));

    /////////////////////////////////////////////////////////////////////

    sb.append("\n invalidSecondValuesInt:\n");
    sb.append(Arrays.toString(invalidSecondValuesInt()));
    sb.append("\n invalidSecondValuesString:\n");
    sb.append(Arrays.toString(invalidSecondValuesString()));

    sb.append("\n invalidMinuteValuesInt:\n");
    sb.append(Arrays.toString(invalidMinuteValuesInt()));
    sb.append("\n invalidMinuteValuesString:\n");
    sb.append(Arrays.toString(invalidMinuteValuesString()));

    sb.append("\n invalidHourValuesInt:\n");
    sb.append(Arrays.toString(invalidHourValuesInt()));
    sb.append("\n invalidHourValuesString:\n");
    sb.append(Arrays.toString(invalidHourValuesString()));

    sb.append("\n invalidDayOfMonthValuesInt:\n");
    sb.append(Arrays.toString(invalidDayOfMonthValuesInt()));
    sb.append("\n invalidDayOfMonthValuesString:\n");
    sb.append(Arrays.toString(invalidDayOfMonthValuesString()));

    sb.append("\n invalidMonthValuesInt:\n");
    sb.append(Arrays.toString(invalidMonthValuesInt()));
    sb.append("\n invalidMonthValuesString:\n");
    sb.append(Arrays.toString(invalidMonthValuesString()));

    sb.append("\n invalidDayOfWeekValuesInt:\n");
    sb.append(Arrays.toString(invalidDayOfWeekValuesInt()));
    sb.append("\n invalidDayOfWeekValuesString:\n");
    sb.append(Arrays.toString(invalidDayOfWeekValuesString()));

    sb.append("\n invalidYearValuesInt:\n");
    sb.append(Arrays.toString(invalidYearValuesInt()));
    sb.append("\n invalidYearValuesString:\n");
    sb.append(Arrays.toString(invalidYearValuesString()));

    System.out.println(sb.toString());

    for (ScheduleAttributeType t : EnumSet.allOf(ScheduleAttributeType.class)) {
      System.out.println(t);
    }
    System.out.println("YEAR=" + TimerUtil.getForSchedule(Calendar.YEAR));
    System.out.println("MONTH=" + TimerUtil.getForSchedule(Calendar.MONTH));
    System.out.println(
        "DAY_OF_MONTH=" + TimerUtil.getForSchedule(Calendar.DAY_OF_MONTH));
    System.out.println(
        "DAY_OF_WEEK=" + TimerUtil.getForSchedule(Calendar.DAY_OF_WEEK));
    System.out.println(
        "HOUR_OF_DAY=" + TimerUtil.getForSchedule(Calendar.HOUR_OF_DAY));

    Calendar calNextHour = Calendar.getInstance();
    calNextHour.add(Calendar.HOUR_OF_DAY, 1);
    System.out.println("Next hour="
        + TimerUtil.getForSchedule(Calendar.HOUR_OF_DAY, calNextHour));

    Calendar calNextMonth = Calendar.getInstance();
    calNextMonth.add(Calendar.MONTH, 1);
    System.out.println(
        "Next month=" + TimerUtil.getForSchedule(Calendar.MONTH, calNextMonth));

  }
}
