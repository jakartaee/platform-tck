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
package com.sun.ts.tests.ejb30.timer.timerconfig;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.ejb.Singleton;
import javax.ejb.TimedObject;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod;
import com.sun.ts.tests.ejb30.timer.common.TimerInfo;

@Singleton
public class TimerConfigBean extends TimerBeanBaseWithoutTimeOutMethod
    implements TimedObject, TimerConfigIF {

  // implements TimedObject AND use @Timeout, the same ejbTimeout method
  @Timeout
  public void ejbTimeout(Timer timer) {
    timeout(timer);
  }

  public void resetTimerConfig(String testName, StringBuilder sb) {
    long durationLong = 1000 * 60 * 10; // time out after 10 min
    TimerInfo info1 = new TimerInfo(testName);
    boolean isPersistent1 = false;
    TimerConfig timerConfig = new TimerConfig();
    timerConfig.setInfo(info1);
    timerConfig.setPersistent(isPersistent1);
    Timer timer = createTimer(durationLong, timerConfig);

    TimerInfo info2 = new TimerInfo(testName + testName);
    boolean isPersistent2 = true;
    timerConfig.setInfo(info2);
    timerConfig.setPersistent(isPersistent2);

    Helper.assertEquals(null, isPersistent1, timer.isPersistent(), sb);
    Helper.assertEquals(null, info1, timer.getInfo(), sb);
    timer.cancel();

    timerConfig.setInfo(null);
    timer = createTimer(durationLong, timerConfig);
    timerConfig.setInfo(info1);
    timerConfig.setPersistent(isPersistent1);
    Helper.assertEquals(null, isPersistent2, timer.isPersistent(), sb);
    Helper.assertEquals(null, null, timer.getInfo(), sb);
    timer.cancel();
  }

  public void illegalArgumentException(StringBuilder sb,
      TimerConfig timerConfig) {
    try {
      timerService.createSingleActionTimer((Date) null, timerConfig);
      throw new RuntimeException(
          "Expecting IllegalArgumentException, but got none.");
    } catch (IllegalArgumentException e) {
      sb.append("Got expected: " + e);
    }

    try {
      timerService.createIntervalTimer(Calendar.getInstance().getTime(), -1L,
          timerConfig);
      throw new RuntimeException(
          "Expecting IllegalArgumentException, but got none.");
    } catch (IllegalArgumentException e) {
      sb.append("Got expected: " + e);
    }

    try {
      Calendar cal = Calendar.getInstance();
      cal.set(Calendar.YEAR, 1968);
      Date d = cal.getTime();
      timerService.createSingleActionTimer(d, timerConfig);
      throw new RuntimeException(
          "Expecting IllegalArgumentException for negative date, but got none: "
              + d.getTime() + "; " + d);
    } catch (IllegalArgumentException e) {
      sb.append("Got expected: " + e);
    }

    try {
      timerService.createIntervalTimer((Date) null, 1L, timerConfig);
      throw new RuntimeException(
          "Expecting IllegalArgumentException, but got none.");
    } catch (IllegalArgumentException e) {
      sb.append("Got expected: " + e);
    }

    try {
      timerService.createSingleActionTimer(-1L, timerConfig);
      throw new RuntimeException(
          "Expecting IllegalArgumentException, but got none.");
    } catch (IllegalArgumentException e) {
      sb.append("Got expected: " + e);
    }

    try {
      timerService.createIntervalTimer(-1L, 1L, timerConfig);
      throw new RuntimeException(
          "Expecting IllegalArgumentException, but got none.");
    } catch (IllegalArgumentException e) {
      sb.append("Got expected: " + e);
    }
    try {
      timerService.createIntervalTimer(1L, -1L, timerConfig);
      throw new RuntimeException(
          "Expecting IllegalArgumentException, but got none.");
    } catch (IllegalArgumentException e) {
      sb.append("Got expected: " + e);
    }
    // invalid ScheduleExpression is tested under schedule directory
  }

  public void gettersSetters(StringBuilder sb) {
    TimerConfig timerConfig = new TimerConfig();
    Helper.assertEquals("Check default value of info ", null,
        timerConfig.getInfo(), sb);
    Helper.assertEquals("Check default value of isPersistent ", true,
        timerConfig.isPersistent(), sb);

    TimerInfo timerInfo = new TimerInfo();
    gettersSetters0(timerConfig, timerInfo, true, sb);
    gettersSetters0(timerConfig, timerInfo, false, sb);
    gettersSetters0(timerConfig, null, false, sb);
  }

  private void gettersSetters0(TimerConfig timerConfig, Serializable timerInfo,
      boolean isPersistent, StringBuilder sb) {
    timerConfig.setPersistent(isPersistent);
    timerConfig.setInfo(timerInfo);
    Helper.assertEquals("Check value of info ", timerInfo,
        timerConfig.getInfo(), sb);
    Helper.assertEquals("Check value of isPersistent ", isPersistent,
        timerConfig.isPersistent(), sb);
  }
}
