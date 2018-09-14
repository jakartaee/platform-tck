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

package com.sun.ts.tests.ejb32.timer.service.common;

import java.util.Calendar;
import java.util.Collection;

import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;

import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.timer.common.TimerInfo;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;

abstract public class ClientBase extends EJBLiteClientBase {
  private static final long ONE_HOUR_MILLIS = 60 * 60 * 1000;

  @EJB(beanName = "TimersSingletonBean")
  protected TimerIF singletonBean;

  @EJB(beanName = "TimersStatelessBean")
  protected TimerIF statelessBean;

  @EJB(beanName = "NoTimersStatefulBean")
  protected TimerIF statefulBean;

  protected TimerIF clientBean;

  protected int autoTimerCount = 0;

  protected boolean ejbLite = false;

  private int programmaticTimerCount = 0;

  protected void createTimersProgrammatically(boolean persistent) {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.HOUR, 1); // next hour
    ScheduleExpression se = TimerUtil.getPreciseScheduleExpression(cal);

    singletonBean.createTimer(TimerUtil.getCurrentDatePlus(Calendar.HOUR, 1),
        newTimerConfig(persistent));
    singletonBean.createTimer(TimerUtil.getCurrentDatePlus(Calendar.HOUR, 1),
        ONE_HOUR_MILLIS, newTimerConfig(persistent));
    singletonBean.createTimer(ONE_HOUR_MILLIS, newTimerConfig(persistent));
    singletonBean.createTimer(ONE_HOUR_MILLIS, ONE_HOUR_MILLIS,
        newTimerConfig(persistent));
    singletonBean.createTimer(se, newTimerConfig(persistent));

    statelessBean.createTimer(TimerUtil.getCurrentDatePlus(Calendar.HOUR, 1),
        newTimerConfig(persistent));
    statelessBean.createTimer(TimerUtil.getCurrentDatePlus(Calendar.HOUR, 1),
        ONE_HOUR_MILLIS, newTimerConfig(persistent));
    statelessBean.createTimer(ONE_HOUR_MILLIS, newTimerConfig(persistent));
    statelessBean.createTimer(ONE_HOUR_MILLIS, ONE_HOUR_MILLIS,
        newTimerConfig(persistent));
    statelessBean.createTimer(se, newTimerConfig(persistent));
  }

  private TimerConfig newTimerConfig(boolean persistent) {
    TimerInfo ti = new TimerInfo(Integer.toString(programmaticTimerCount++));
    return new TimerConfig(ti, persistent);
  }

  protected void passIfAllTimersRetrieved(Collection<Timer> allTimers) {
    assertEquals("Check the count of Timer objects retrieved is correct",
        programmaticTimerCount + autoTimerCount, allTimers.size());
    checkTimersIndexedByInfo(allTimers, programmaticTimerCount, false);
    checkTimersIndexedByInfo(allTimers, autoTimerCount, true);
  }

  private void checkTimersIndexedByInfo(Collection<Timer> allTimers, int count,
      boolean auto) {
    for (int i = 0; i < count; i++) {
      Timer t = TimerUtil.findTimer(allTimers,
          new TimerInfo((auto ? "a" : "") + i));
      assertNotEquals("Check Timer object retrieved not null", null, t);
    }
  }

  public void testGetAllTimers() {
    if (!ejbLite) {
      createTimersProgrammatically(true);
    }
    createTimersProgrammatically(false);
    passIfAllTimersRetrieved(clientBean.getAllTimers());
  }
}
