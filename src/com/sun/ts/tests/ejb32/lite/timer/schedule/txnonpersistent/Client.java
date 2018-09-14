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

package com.sun.ts.tests.ejb32.lite.timer.schedule.txnonpersistent;

import javax.ejb.EJB;
import javax.ejb.TimerConfig;

import com.sun.ts.tests.ejb32.lite.timer.schedule.tx.ClientBase;
import com.sun.ts.tests.ejb32.lite.timer.schedule.tx.ScheduleTxBeanBase;

public class Client extends ClientBase {

  @EJB(beanInterface = ScheduleBean.class, beanName = "txnonpersistent-ScheduleBean")
  protected void setScheduleBean(ScheduleTxBeanBase b) {
    scheduleBean = b;
  }

  @EJB(beanInterface = ScheduleBMTBean.class, beanName = "txnonpersistent-ScheduleBMTBean")
  protected void setScheduleBMTBean(ScheduleTxBeanBase b) {
    scheduleBMTBean = b;
  }

  @Override
  protected TimerConfig getTimerConfig() {
    TimerConfig timerConfig = super.getTimerConfig();
    timerConfig.setPersistent(false);
    return timerConfig;
  }

  /*
   * @testName: createRollback
   * 
   * @test_Strategy: create a timer that is to expire in 1 second, and 1.5
   * seconds later, set the transaction to rollback only. The timer must not be
   * present, and no timeout event for this timer.
   */
  /*
   * @testName: createRollbackBMT
   * 
   * @test_Strategy: same as above, but using BMT
   */
  /*
   * @testName: createRollbackTxPropagation
   * 
   * @test_Strategy: Inside propagated transaction, create a timer that is to
   * expire in 1 second. The client transaction is rolled back. The timer must
   * not be present, and no timeout event for this timer.
   */
  /*
   * @testName: createRollbackTxPropagationBMT
   * 
   * @test_Strategy: See above. But for BMT, no tx propagation.
   */
  /*
   * @testName: cancelRollback
   * 
   * @test_Strategy: create a timer that is to expire in the far future. The
   * subsequent business method cancels the timer, and then set the tx to
   * rollback. This timer must still be present, and has not yet expired. These
   * business methods have default transaction attribute type (REQUIRED)
   */
  /*
   * @testName: cancelRollbackBMT
   * 
   * @test_Strategy: See above. Using BMT
   */
  /*
   * @testName: cancelRollbackPropagation
   * 
   * @test_Strategy: create a timer that is to expire in the far future. The
   * subsequent business method cancels the timer within a client-initiated tx.
   * The client tx is rolled back. This timer must still be present, and has not
   * yet expired. These business methods have default transaction attribute type
   * (REQUIRED)
   */
  /*
   * @testName: cancelRollbackPropagationBMT
   * 
   * @test_Strategy: See above. No propagation for BMT
   */
  /*
   * @testName: timeoutRollback
   * 
   * @test_Strategy: create a timer that is to expire in 1 second. The timeout
   * method sets rollback for this timeout event. This event must be retried at
   * least once. The timeout method has default transaction attribute type
   * (REQUIRED)
   */
  /*
   * @testName: timeoutSystemException
   * 
   * @test_Strategy: create a timer that is to expire in 1 second The timeout
   * throws system exception. This event must be retried at least once. The
   * timeout method has default transaction attribute type (REQUIRED)
   */
  /*
   * @testName: timeoutSystemExceptionBMT
   * 
   * @test_Strategy: create a timer that is to expire in 1 second The timeout
   * throws system exception. This event must be retried at least once.
   */
  /*
   * @testName: createTimerWithoutTx
   * 
   * @test_Strategy: invoke the BMT bean to create a timer without tx.
   */
  /*
   * @testName: createTimerWithoutTxHavingClientTx
   * 
   * @test_Strategy: invoke the BMT bean to create a timer without tx. The
   * client does have a UserTransaction but it is not propagated.
   */
}
