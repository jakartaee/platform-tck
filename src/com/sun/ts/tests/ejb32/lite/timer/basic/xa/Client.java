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

package com.sun.ts.tests.ejb32.lite.timer.basic.xa;

import java.util.Calendar;
import java.util.Date;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.timer.common.ClientBase;
import com.sun.ts.tests.ejb30.timer.common.TimerInfo;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;

public class Client extends ClientBase {

  @EJB(beanName = "StatelessXATimerBean")
  private StatelessXATimerBean statelessXaTimerBean;

  @EJB(beanName = "SingletonXATimerBean")
  private SingletonXATimerBean singletonXaTimerBean;

  /*
   * @testName: persistCoffeeCreateTimerRollbackStateless
   * 
   * @test_Strategy: persist a coffee in the first business method. In the
   * second business method, create a timer and try to persist the same coffee.
   * It will cause the tx and timer creation to rollback.
   */
  public void persistCoffeeCreateTimerRollbackStateless() {
    persistCoffeeCreateTimerRollback(statelessXaTimerBean, "RollbackStateless");
  }

  /*
   * @testName: persistCoffeeCreateTimerRollbackSingleton
   * 
   * @test_Strategy: see persistCoffeeCreateTimerRollbackStateless
   */
  public void persistCoffeeCreateTimerRollbackSingleton() {
    persistCoffeeCreateTimerRollback(singletonXaTimerBean, "RollbackSingleton");
  }

  private void persistCoffeeCreateTimerRollback(XATimerBeanBase b,
      String brandName) {
    int id = 1;
    Date expireation = TimerUtil.getCurrentDatePlus(Calendar.HOUR, 5);
    TimerInfo info = new TimerInfo(getTestName());
    b.persistCoffee(id, brandName);
    boolean result = b.persistCoffeeCreateTimerRollback(id, brandName,
        expireation, info);
    assertEquals(null, true, result);
    assertEquals(null, 0, b.getTimers().size());
    passIfNoTimeout();
  }
}
