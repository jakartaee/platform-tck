/*
 * Copyright (c) 2010, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.timer.timerconfig;

import java.util.Collection;
import java.util.Date;

import javax.ejb.ScheduleExpression;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;

import com.sun.ts.tests.ejb30.timer.common.TimerInfo;

/**
 * This interface is extracted from TimerConfigBean and its superclasses. It
 * serves as the local business interface for TimerConfigBean to avoid exposing
 * ejbTimeout callback method as business method when no-interface view is used.
 * The name of a business method cannot start with "ejb".
 */
public interface TimerConfigIF {

  public abstract void resetTimerConfig(String testName, StringBuilder sb);

  public abstract void illegalArgumentException(StringBuilder sb,
      TimerConfig timerConfig);

  public abstract void gettersSetters(StringBuilder sb);

  Collection<Timer> getTimers();

  void cancelTimer(Timer... timers);

  void cancelAllTimers();

  Timer createTimer(ScheduleExpression exp, String name);

  boolean isPersistent(Timer timer);

  boolean isCalendarTimer(Timer timer);

  ScheduleExpression getSchedule(Timer timer);

  Timer createTimer(Date expiration, TimerConfig timerConfig);

  Timer createTimer(Date initialExpiration, long intervalDuration,
      TimerConfig timerConfig);

  Timer createTimer(long duration, TimerConfig timerConfig);

  Timer createTimer(long InitialDuration, long intervalDuration,
      TimerConfig timerConfig);

  Timer createTimer(ScheduleExpression exp, TimerConfig timerConfig);

  Timer createTimer(ScheduleExpression exp, TimerInfo info);

  Timer createTimer(long duration, java.io.Serializable timerInfo);

  Timer createTimer(Date expiration, long duration,
      java.io.Serializable timerInfo);

  Timer createFarFutureTimer(String name);

  Timer createFarFutureTimer(TimerConfig timerConfig);

  Timer createSecondLaterTimer(String name);

  Timer createSecondLaterTimer(TimerConfig timerConfig);

  Timer createMillisecondLaterTimer(String name);

  Timer findTimer(TimerInfo info);

  long getTimeRemaining(Timer timer);

  Date getNextTimeout(Timer timer);

  Timer findTimer(String info);

  Timer findTimer(TimerConfig timerConfig);

  String passIfNoMoreTimeouts(Timer t);

  String passIfNoSuchObjectLocalException(Timer t);
}
