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

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.timer.common.ClientBase;

public class Client extends ClientBase {
  @EJB(beanName = "ScheduleBean")
  private ScheduleBean scheduleBean;

  /*
   * @testName: validSecondValuesInt
   * 
   * @test_Strategy: verify valid int values are correctly processed.
   */
  public void validSecondValuesInt() {
    appendReason(scheduleBean.validSecondValuesInt());
  }

  /*
   * @testName: validSecondValuesString
   * 
   * @test_Strategy: verify valid String values are correctly processed.
   */
  public void validSecondValuesString() {
    appendReason(scheduleBean.validSecondValuesString());
  }

  /*
   * @testName: validMinuteValuesInt
   */
  public void validMinuteValuesInt() {
    appendReason(scheduleBean.validMinuteValuesInt());
  }

  /*
   * @testName: validMinuteValuesString
   */
  public void validMinuteValuesString() {
    appendReason(scheduleBean.validMinuteValuesString());
  }

  /*
   * @testName: validHourValuesInt
   */
  public void validHourValuesInt() {
    appendReason(scheduleBean.validHourValuesInt());
  }

  /*
   * @testName: validHourValuesString
   */
  public void validHourValuesString() {
    appendReason(scheduleBean.validHourValuesString());
  }

  /*
   * @testName: validMonthValuesInt
   */
  public void validMonthValuesInt() {
    appendReason(scheduleBean.validMonthValuesInt());
  }

  /*
   * @testName: validMonthValuesString
   */
  public void validMonthValuesString() {
    appendReason(scheduleBean.validMonthValuesString());
  }

  /*
   * @testName: validYearValuesInt
   */
  public void validYearValuesInt() {
    appendReason(scheduleBean.validYearValuesInt());
  }

  /*
   * @testName: validYearValuesString
   */
  public void validYearValuesString() {
    appendReason(scheduleBean.validYearValuesString());
  }

  /*
   * @testName: validDayOfMonthValuesInt
   */
  public void validDayOfMonthValuesInt() {
    appendReason(scheduleBean.validDayOfMonthValuesInt());
  }

  /*
   * @testName: validDayOfMonthValuesString
   */
  public void validDayOfMonthValuesString() {
    appendReason(scheduleBean.validDayOfMonthValuesString());
  }

  /*
   * @testName: validDayOfWeekValuesInt
   */
  public void validDayOfWeekValuesInt() {
    appendReason(scheduleBean.validDayOfWeekValuesInt());
  }

  /*
   * @testName: validDayOfWeekValuesString
   */
  public void validDayOfWeekValuesString() {
    appendReason(scheduleBean.validDayOfWeekValuesString());
  }

  /*
   * @testName: invalidSecondValuesInt
   */
  public void invalidSecondValuesInt() {
    appendReason(scheduleBean.invalidSecondValuesInt());
  }

  /*
   * @testName: invalidSecondValuesString
   */
  public void invalidSecondValuesString() {
    appendReason(scheduleBean.invalidSecondValuesString());
  }

  /*
   * @testName: invalidMinuteValuesInt
   */
  public void invalidMinuteValuesInt() {
    appendReason(scheduleBean.invalidMinuteValuesInt());
  }

  /*
   * @testName: invalidMinuteValuesString
   */
  public void invalidMinuteValuesString() {
    appendReason(scheduleBean.invalidMinuteValuesString());
  }

  /*
   * @testName: invalidHourValuesInt
   */
  public void invalidHourValuesInt() {
    appendReason(scheduleBean.invalidHourValuesInt());
  }

  /*
   * @testName: invalidHourValuesString
   */
  public void invalidHourValuesString() {
    appendReason(scheduleBean.invalidHourValuesString());
  }

  /*
   * @testName: invalidMonthValuesInt
   */
  public void invalidMonthValuesInt() {
    appendReason(scheduleBean.invalidMonthValuesInt());
  }

  /*
   * @testName: invalidMonthValuesString
   */
  public void invalidMonthValuesString() {
    appendReason(scheduleBean.invalidMonthValuesString());
  }

  /*
   * @testName: invalidYearValuesInt
   */
  public void invalidYearValuesInt() {
    appendReason(scheduleBean.invalidYearValuesInt());
  }

  /*
   * @testName: invalidYearValuesString
   */
  public void invalidYearValuesString() {
    appendReason(scheduleBean.invalidYearValuesString());
  }

  /*
   * @testName: invalidDayOfMonthValuesInt
   */
  public void invalidDayOfMonthValuesInt() {
    appendReason(scheduleBean.invalidDayOfMonthValuesInt());
  }

  /*
   * @testName: invalidDayOfMonthValuesString
   */
  public void invalidDayOfMonthValuesString() {
    appendReason(scheduleBean.invalidDayOfMonthValuesString());
  }

  /*
   * @testName: invalidDayOfWeekValuesInt
   */
  public void invalidDayOfWeekValuesInt() {
    appendReason(scheduleBean.invalidDayOfWeekValuesInt());
  }

  /*
   * @testName: invalidDayOfWeekValuesString
   */
  public void invalidDayOfWeekValuesString() {
    appendReason(scheduleBean.invalidDayOfWeekValuesString());
  }

  /*
   * @testName: leapYear
   * 
   * @test_Strategy: Create a timer with a valid leap year date, and cancel it.
   */
  public void leapYear() {
    appendReason(scheduleBean.leapYear());
  }

  /*
   * @testName: attributeDefaults
   * 
   * @test_Strategy: verify the default values 0 for second, minute, hour, and
   * '*' for dayOfMonth, month, year, and dayOfWeek
   */
  public void attributeDefaults() {
    appendReason(scheduleBean.attributeDefaults());
  }

  /*
   * @testName: dayOfMonthOverDayOfWeek
   * 
   * @test_Strategy: If dayOfWeek has a wildcard value and dayOfMonth does not,
   * the dayOfMonth attribute takes precedence and the dayOfWeek attribute is
   * ignored. If dayOfMonth has a non-wildcard value and dayOfWeek has a
   * non-wildcard value, both attributes apply. Typically, at most one of these
   * two attributes will be specified to contain a non-wildcard value.
   */
  public void dayOfMonthOverDayOfWeek() {
    appendReason(scheduleBean.dayOfMonthOverDayOfWeek());
    passIfNoTimeout();
  }

  /*
   * @testName: dayOfWeekOverDayOfMonth
   * 
   * @test_Strategy: If dayOfMonth has a wildcard value and dayOfWeek does not,
   * the dayOfWeek attribute takes precedence and the dayOfMonth attribute is
   * ignored.
   */
  public void dayOfWeekOverDayOfMonth() {
    appendReason(scheduleBean.dayOfWeekOverDayOfMonth());
    passIfNoTimeout();
  }

  /*
   * @testName: dayOfMonthAndDayOfWeek
   * 
   * @test_Strategy: If dayOfMonth has a non-wildcard value and dayOfWeek has a
   * non-wildcard value, both attributes apply. Typically, at most one of these
   * two attributes will be specified to contain a non-wildcard value.
   */
  public void dayOfMonthAndDayOfWeek() {
    scheduleBean.cancelAllTimers();
    appendReason(scheduleBean.dayOfMonthAndDayOfWeek());
    passIfTimeout();
  }

  /*
   * @testName: validStart
   * 
   * @test_Strategy: verify various valid ScheduleExpression start value.
   */
  public void validStart() {
    scheduleBean.validStart(getReasonBuffer());
  }

  /*
   * @testName: validEnd
   * 
   * @test_Strategy:verify various valid ScheduleExpression end value.
   */
  public void validEnd() {
    scheduleBean.validEnd(getReasonBuffer());
  }

  /*
   * @testName: validStartEnd
   * 
   * @test_Strategy:verify various valid ScheduleExpression start and end value.
   */
  public void validStartEnd() {
    scheduleBean.validStartEnd(getReasonBuffer());
    scheduleBean.cancelAllTimers();
  }
}
