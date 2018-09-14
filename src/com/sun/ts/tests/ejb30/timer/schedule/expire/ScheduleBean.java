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
package com.sun.ts.tests.ejb30.timer.schedule.expire;

import java.util.Collection;

import javax.ejb.NoMoreTimeoutsException;
import javax.ejb.Singleton;
import javax.ejb.Timeout;
import javax.ejb.Timer;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod;
import com.sun.ts.tests.ejb30.timer.common.TimerInfo;

@Singleton
public class ScheduleBean extends TimerBeanBaseWithoutTimeOutMethod {
  @Override
  @Timeout
  protected void timeout(Timer timer) {
    super.timeout(timer);
    TimerInfo info = (TimerInfo) timer.getInfo();
    String testName = info.getTestName();
    if (testName.equals("timerAccessInTimeoutMethod")) {
      timerAccessInTimeoutMethod(timer, testName);
    } else if (testName.equals("cancelInTimeoutMethod")) {
      timer.cancel();
    } else if (testName.startsWith("incrementSecond")) {
      final long margin = 10 * 1000; // generous 10 seconds
      long timeRemaining = timer.getTimeRemaining();
      long expectedTimeRemaining = info.getLongVar();
      if (Math.abs(timeRemaining - expectedTimeRemaining) <= margin) {
        Helper.getLogger()
            .info("Expected timeRemaining: " + expectedTimeRemaining
                + ", and actual " + timeRemaining
                + " are close enough for test " + testName);
      } else {
        statusSingleton.setStatus(testName, false);
        statusSingleton.addRecord(testName, "Expecting timeRemaining "
            + expectedTimeRemaining + ", actual " + timeRemaining);
      }
    }
  }

  private void timerAccessInTimeoutMethod(Timer t, String testName) {
    Collection<Timer> timers = timerService.getTimers();
    if (timers.size() == 1) {
      statusSingleton.addRecord(testName, "Found 1 timer, as expected.");
    } else {
      statusSingleton.setStatus(testName, false);
      statusSingleton.addRecord(testName,
          "Expecting 1 timer, but got " + timers.size());
    }
    if (t.isPersistent()) {
      t.getHandle();
    }
    if (t.isCalendarTimer()) {
      t.getSchedule();
    }
    t.isPersistent();
    try {
      t.getNextTimeout();
      statusSingleton.setStatus(testName, false);
      statusSingleton.addRecord(testName,
          "Expecting NoMoreTimeoutsException, but got none.");
    } catch (NoMoreTimeoutsException e) {
      statusSingleton.addRecord(testName, "Got the expected " + e);
    }

    try {
      t.getTimeRemaining();
      statusSingleton.setStatus(testName, false);
      statusSingleton.addRecord(testName,
          "Expecting NoMoreTimeoutsException, but got none.");
    } catch (NoMoreTimeoutsException e) {
      statusSingleton.addRecord(testName, "Got the expected " + e);
    }

    t.cancel();
    statusSingleton.addRecord(testName,
        "Called various timer methods in timeout method.");
  }
}
