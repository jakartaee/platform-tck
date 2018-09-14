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
package com.sun.ts.tests.ejb30.timer.schedule.descriptor.stateless;

import static com.sun.ts.tests.ejb30.timer.schedule.descriptor.common.TimeoutParamIF.AUTO_TIMER_SUFFIX;
import static com.sun.ts.tests.ejb30.timer.schedule.descriptor.common.TimeoutParamIF.EmptyParamTimeoutBean;
import static com.sun.ts.tests.ejb30.timer.schedule.descriptor.common.TimeoutParamIF.NoParamTimeoutBean;
import static com.sun.ts.tests.ejb30.timer.schedule.descriptor.common.TimeoutParamIF.PROGRAMMATIC_TIMER_SUFFIX;
import static com.sun.ts.tests.ejb30.timer.schedule.descriptor.common.TimeoutParamIF.WithParamTimeoutBean;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.ejb.Timer;

import com.sun.ts.tests.ejb30.timer.common.ClientBase;
import com.sun.ts.tests.ejb30.timer.schedule.descriptor.common.TimeoutParamIF;

public class Client extends ClientBase {

  @EJB(beanName = NoParamTimeoutBean)
  private TimeoutParamIF noParamTimeoutBean;

  @EJB(beanName = EmptyParamTimeoutBean)
  private TimeoutParamIF emptyParamTimeoutBean;

  @EJB(beanName = WithParamTimeoutBean)
  private TimeoutParamIF withParamTimeoutBean;

  private List<TimeoutParamIF> beans = new ArrayList<TimeoutParamIF>();

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    beans.add(noParamTimeoutBean);
    beans.add(emptyParamTimeoutBean);
    beans.add(withParamTimeoutBean);
  }

  /*
   * @testName: programmatic
   * 
   * @test_Strategy:
   */
  public void programmatic() {
    for (TimeoutParamIF b : beans) {
      String timerName = b.getBeanName() + PROGRAMMATIC_TIMER_SUFFIX;
      Timer t = b.createSecondLaterTimer(timerName);
      appendReason("Created a timer with name " + timerName + "; " + t);
      passIfTimeout(timerName);
      removeStatusAndRecords(timerName);
    }
  }

  /*
   * @testName: autoNoParamTimeoutBean
   * 
   * @test_Strategy:
   */
  public void autoNoParamTimeoutBean() {
    auto(noParamTimeoutBean);
  }

  /*
   * @testName: autoEmptyParamTimeoutBean
   * 
   * @test_Strategy:
   */
  public void autoEmptyParamTimeoutBean() {
    auto(emptyParamTimeoutBean);
  }

  /*
   * @testName: autoWithParamTimeoutBean
   * 
   * @test_Strategy:
   */
  public void autoWithParamTimeoutBean() {
    auto(withParamTimeoutBean);
  }

  private void auto(TimeoutParamIF b) {
    passIfRecurringTimeout(b.getBeanName() + AUTO_TIMER_SUFFIX);

    // If cancel timers, then the subsequent test run won't pass without a
    // redeploy.
    // b.cancelAllTimers();
  }

}
