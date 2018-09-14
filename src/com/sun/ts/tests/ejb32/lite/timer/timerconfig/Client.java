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

package com.sun.ts.tests.ejb32.lite.timer.timerconfig;

import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;

import com.sun.ts.tests.ejb30.timer.common.ClientBase;
import com.sun.ts.tests.ejb30.timer.common.ScheduleValues;
import com.sun.ts.tests.ejb30.timer.common.TimerInfo;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;

public class Client extends ClientBase {

  @EJB(beanName = "TimerConfigBean")
  protected TimerConfigIF timerConfigBean;

  @Override
  public void setup(String[] args, Properties p) {
    super.setup(args, p);
    timerConfigBean.cancelAllTimers();
  }

  /*
   * @testName: isPersistent
   * 
   * @test_Strategy: create non-persistent timers and verify isPersistent in a
   * separate business method.
   */
  public void isPersistent() {
    String name = getTestName() + "-nonpersistent";
    ScheduleExpression exp = new ScheduleExpression();
    exp = exp.hour(ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR)
        .minute(ScheduleValues.DEFAULT_ATTRIBUTE_VALUE_STAR).second("*/2");
    TimerConfig timerConfig = new TimerConfig();
    timerConfig.setInfo(new TimerInfo(name));
    timerConfig.setPersistent(false);
    Timer timerNonPersistent = timerConfigBean.createTimer(exp, timerConfig);
    assertEquals(null, false, timerConfigBean.isPersistent(timerNonPersistent));
    passIfRecurringTimeout(name);
    timerConfigBean.cancelTimer(timerNonPersistent);
  }

  /*
   * @testName: gettersSetters
   * 
   * @test_Strategy:
   */
  public void gettersSetters() {
    timerConfigBean.gettersSetters(getReasonBuffer());
  }

  /*
   * @testName: illegalArgumentException
   * 
   * @test_Strategy:
   */
  public void illegalArgumentException() {
    TimerConfig timerConfig = new TimerConfig();
    timerConfig.setInfo(new TimerInfo(getTestName()));
    timerConfig.setPersistent(false);
    timerConfigBean.illegalArgumentException(getReasonBuffer(), timerConfig);
  }

  /*
   * @testName: resetTimerConfig
   * 
   * @test_Strategy: Any modification to TimerConfig after timer creation is not
   * reflected in the state of timer.
   */
  public void resetTimerConfig() {
    timerConfigBean.resetTimerConfig(getTestName(), getReasonBuffer());
  }

  /*
   * @testName: createTimerWithLong
   * 
   * @test_Strategy:
   */
  public void createTimerWithLong() {
    TimerConfig timerConfig = new TimerConfig();
    timerConfig.setInfo(new TimerInfo(getTestName()));
    timerConfig.setPersistent(false);
    timerConfigBean.createTimer(DEFAULT_DURATION, timerConfig);
    passIfTimeout();
  }

  /*
   * @testName: createTimerWithLongRecurring
   * 
   * @test_Strategy:
   */
  public void createTimerWithLongRecurring() {
    try {
      TimerConfig timerConfig = new TimerConfig();
      timerConfig.setInfo(new TimerInfo(getTestName()));
      timerConfig.setPersistent(false);
      timerConfigBean.createTimer(DEFAULT_DURATION, DEFAULT_INTERVAL,
          timerConfig);
      passIfRecurringTimeout();
    } finally {
      timerConfigBean.cancelAllTimers();
    }
  }

  /*
   * @testName: createTimerWithDate
   * 
   * @test_Strategy:
   */
  public void createTimerWithDate() {
    TimerConfig timerConfig = new TimerConfig();
    timerConfig.setInfo(new TimerInfo(getTestName()));
    timerConfig.setPersistent(false);
    Date expirationDate = TimerUtil.getCurrentDatePlus(Calendar.SECOND, 1);
    timerConfigBean.createTimer(expirationDate, timerConfig);
    passIfTimeout();
  }

  /*
   * @testName: createTimerWithDateRecurring
   * 
   * @test_Strategy:
   */
  public void createTimerWithDateRecurring() {
    try {
      TimerConfig timerConfig = new TimerConfig();
      timerConfig.setInfo(new TimerInfo(getTestName()));
      timerConfig.setPersistent(false);
      Date expirationDate = TimerUtil.getCurrentDatePlus(Calendar.SECOND, 1);
      timerConfigBean.createTimer(expirationDate, DEFAULT_INTERVAL,
          timerConfig);
      passIfRecurringTimeout();
    } finally {
      timerConfigBean.cancelAllTimers();
    }
  }

  /*
   * @testName: createTimerWithSchedule
   * 
   * @test_Strategy:
   */
  public void createTimerWithSchedule() {
    TimerConfig timerConfig = new TimerConfig();
    timerConfig.setInfo(new TimerInfo(getTestName()));
    timerConfig.setPersistent(false);
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.SECOND, 1); // next second
    timerConfigBean.createTimer(TimerUtil.getPreciseScheduleExpression(cal),
        timerConfig);
    passIfTimeout();
  }
}
