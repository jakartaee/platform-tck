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
package com.sun.ts.tests.ejb30.timer.schedule.tz;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Timer;

import org.apache.commons.lang3.time.DateUtils;

import com.sun.ts.tests.ejb30.timer.common.ClientBase;

public class Client extends ClientBase {

  @EJB(beanName = "TZScheduleBareBean")
  protected TZScheduleBareBean tzBareBean;

  @EJB(beanName = "TZScheduleBean")
  protected TZScheduleBean tzBean;

  /*
   * @testName: defaultTZ
   * 
   * @test_Strategy: default TZ on a auto timer
   */
  public void defaultTZ() {
    appendReason(tzBareBean.defaultTZ());
    appendReason(tzBean.defaultTZ());
  }

  /*
   * @testName: shanghaiAndArgentinaTZ
   * 
   * @test_Strategy: check 2 auto timers with Argentina TZ
   */
  public void shanghaiAndArgentinaTZ() {
    appendReason(tzBareBean.shanghaiAndArgentinaTZ());
    appendReason(tzBean.shanghaiAndArgentinaTZ());
  }

  /*
   * @testName: onlyForTZScheduleBareBean
   * 
   * @test_Strategy: declare a timer in ejb-jar.xml for TZScheduleBareBean only.
   * Verify this timer is not available in TZScheduleBean.
   */
  public void onlyForTZScheduleBareBean() {
    final String timerName = "TZScheduleBareBean.only";
    assertEquals(null, null, tzBean.findTimer(timerName));
    Timer t = tzBareBean.findTimer(timerName);
    ScheduleExpression schedule = tzBareBean.getSchedule(t);
    appendReason("Found the timer with schedule: " + schedule);
    assertEquals(null, true, tzBareBean.isCalendarTimer(t));
    assertEquals(null, false, tzBareBean.isPersistent(t));
  }

  /*
   * @testName: allTZ
   * 
   * @test_Strategy: create programmatic timers with all required TZ values
   */
  public void allTZ() {
    appendReason(tzBean.allTZ());
  }

  /*
   * @testName: expireInLaterTZ
   * 
   * @test_Strategy: create programmatic timers with a TZ that is later than the
   * default TZ. The timer should not expire now.
   */
  public void expireInLaterTZ() {
    Timer t = tzBean.expireInLaterTZ();
    Date nextTimeout = tzBean.getNextTimeout(t);
    long currentTimeMillis = System.currentTimeMillis();
    long diff = nextTimeout.getTime() - currentTimeMillis;

    // The diff should be more than 20 minutes
    assertGreaterThan(null, diff, DateUtils.MILLIS_PER_MINUTE * 20);
    passIfNoTimeout();
  }

}
