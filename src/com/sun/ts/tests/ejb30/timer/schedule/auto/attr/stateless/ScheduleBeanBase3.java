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
package com.sun.ts.tests.ejb30.timer.schedule.auto.attr.stateless;

import javax.ejb.Schedule;
import javax.ejb.Schedules;
import javax.ejb.Timer;

import com.sun.ts.tests.ejb30.timer.common.TimerUtil;

public class ScheduleBeanBase3 extends ScheduleBeanBase2 {
  @SuppressWarnings("unused")
  @Schedules({
      @Schedule(hour = "*", minute = "*", second = ODD_SECONDS, persistent = false, info = "autoTimerNonPersistent"),
      @Schedule(hour = "*", minute = "*", second = EVEN_SECONDS, info = "autoTimerPersistent") })
  private void autoTimerPersistentAndNonPersistent(Timer timer) {
    String info = (String) timer.getInfo();
    if (info.equals("autoTimerNonPersistent")) {
      checkPersistent(timer, false, "autoTimerNonPersistent");
    } else if (info.equals("autoTimerPersistent")) {
      checkPersistent(timer, true, "autoTimerPersistent");
    } else {
      throw new RuntimeException(
          "Should not reach here. timer: " + TimerUtil.toString(timer));
    }
    cancelIfTooManyExpirations(timer, info);
  }

  @SuppressWarnings("unused")
  @Schedule(hour = "*", minute = "*", second = "0/59", info = AUTO_TIMER_WITH_INFO)
  private void autoTimerWithInfo(Timer t) {
    checkInfo(t, AUTO_TIMER_WITH_INFO, "autoTimerWithInfo");
    cancelIfTooManyExpirations(t, "autoTimerWithInfo");
  }

  @SuppressWarnings("unused")
  @Schedule(hour = "0-23", minute = "0-59", second = "*/59")
  private void autoTimerWithoutInfo(Timer t) {
    checkInfo(t, null, "autoTimerWithoutInfo");
    cancelIfTooManyExpirations(t, "autoTimerWithoutInfo");
  }

  private void checkInfo(Timer t, String expected, String testName) {
    String actual = (String) t.getInfo();
    if ((expected == null && actual == null)
        || (expected != null && expected.equals(actual))) {
      statusSingleton.setStatus(testName, true);
      statusSingleton.addRecord(testName, "Got the expected info " + actual);
    } else {
      statusSingleton.setStatus(testName, false);
      statusSingleton.addRecord(testName,
          "Expecting info " + expected + ", but actual " + actual);
    }
  }

  private void checkPersistent(Timer t, boolean expected, String testName) {
    boolean actual = t.isPersistent();
    if (expected == actual) {
      statusSingleton.setStatus(testName, true);
      statusSingleton.addRecord(testName,
          "Got the expected isPersistent " + actual);
    } else {
      statusSingleton.setStatus(testName, false);
      statusSingleton.addRecord(testName,
          "Expecting isPersistent " + expected + ", but actual " + actual);
    }
  }
}
