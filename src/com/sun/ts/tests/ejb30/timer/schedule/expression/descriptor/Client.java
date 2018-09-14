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
package com.sun.ts.tests.ejb30.timer.schedule.expression.descriptor;

import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Timer;

import com.sun.ts.tests.ejb30.timer.common.ClientBase;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;

public class Client extends ClientBase {
  @EJB(beanName = "ScheduleBean")
  private ScheduleBean scheduleBean;

  private void schedule0() {
    ScheduleExpression exp = scheduleBean
        .getSchedule(scheduleBean.findTimer(getTestName()));
    appendReason("Found auto timer with schedule " + exp);
  }

  /*
   * @testName: defaultSchedule
   * 
   * @test_Strategy: verify a auto timer declared in ejb-jar.xml with default
   * values in schedule
   */
  public void defaultSchedule() {
    Timer t = scheduleBean.findTimer(getTestName());
    ScheduleExpression exp = scheduleBean.getSchedule(t);
    TimerUtil.checkScheduleDefaults(exp, getReasonBuffer());
  }

  /*
   * @testName: schedule1
   * 
   * @test_Strategy: verify a auto timer declared in ejb-jar.xml with various
   * values in schedule
   */
  public void schedule1() {
    schedule0();
  }

  /*
   * @testName: schedule2
   */
  public void schedule2() {
    schedule0();
  }

  /*
   * @testName: schedule3
   */
  public void schedule3() {
    schedule0();
  }

  /*
   * @testName: schedule4
   */
  public void schedule4() {
    schedule0();
  }

}
