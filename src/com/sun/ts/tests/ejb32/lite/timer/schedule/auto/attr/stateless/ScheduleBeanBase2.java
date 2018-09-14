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

package com.sun.ts.tests.ejb32.lite.timer.schedule.auto.attr.stateless;

import javax.ejb.Schedule;
import javax.ejb.Timer;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.timer.common.TimerBeanBase;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;

public class ScheduleBeanBase2 extends TimerBeanBase {

  public static final String AUTO_TIMER_WITH_INFO = "  < autoTimerWithInfo >  ";

  public static final String ODD_SECONDS = "0/4";

  public static final String EVEN_SECONDS = "1/4";

  public static final String ALL_MONTH = "6-5";

  public static final String ODD_MONTH = "1,3,5,7,9,11";

  public static final String EVEN_MONTH = "2,4,6,8,10,12";

  public static final String ALL_DAY_OF_WEEK = "Fri-Thu";

  public static final String ODD_DAY_OF_WEEK = "Sun,Mon,Wed,Fri";

  public static final String EVEN_DAY_OF_WEEK = "Tue,Thu,Sat";

  private static final int MAX_NUM_OF_EXPIRATIONS = 50;

  @Schedule(hour = "*", minute = "*", second = ODD_SECONDS, dayOfWeek = ALL_DAY_OF_WEEK, dayOfMonth = "Last", persistent = false, info = "autoTimerInSuperClassNoParam")
  public void autoTimerInSuperClassNoParam() {
    Timer t = TimerUtil.findTimer(timerService, "autoTimerInSuperClassNoParam");
    timeout(t);
    cancelIfTooManyExpirations(t, "autoTimerInSuperClassNoParam");
  }

  protected void cancelIfTooManyExpirations(Timer timer, String name,
      int... maxOfExpirations) {
    int limit = (maxOfExpirations.length == 0) ? MAX_NUM_OF_EXPIRATIONS
        : maxOfExpirations[0];
    int actualNum = statusSingleton.getRecords(name).size();
    if (actualNum >= limit) {
      Helper.getLogger()
          .fine("This timer has expired approximately " + actualNum
              + " times, and will be removed: " + TimerUtil.toString(timer));
      timer.cancel();
    }
  }
}
