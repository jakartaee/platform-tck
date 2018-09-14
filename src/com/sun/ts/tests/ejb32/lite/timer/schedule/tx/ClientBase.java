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

package com.sun.ts.tests.ejb32.lite.timer.schedule.tx;

import java.util.List;

import javax.annotation.Resource;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.transaction.UserTransaction;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.timer.common.TimerInfo;

/**
 * These tests make use of UserTransaction, and therefore all the hosting
 * vehicles must support UserTransaction. OK with jsf bean, jsp, and servlet,
 * but servlet filter and webapp listeners do not.
 */
abstract public class ClientBase
    extends com.sun.ts.tests.ejb30.timer.common.ClientBase {

  protected ScheduleTxBeanBase scheduleBean; // OK to use supertype

  protected ScheduleTxBeanBase scheduleBMTBean;

  @Resource
  private UserTransaction ut;

  protected TimerConfig getTimerConfig() {
    // TimerConfig timerConfig = new TimerConfig(new TimerInfo(getTestName()),
    // true);
    TimerConfig timerConfig = new TimerConfig(new TimerInfo(getTestName()),
        false);
    return timerConfig;
  }

  /*
   * testName: createRollback
   * 
   * @test_Strategy: create a timer that is to expire in 1 second, and 1.5
   * seconds later, set the transaction to rollback only. The timer must not be
   * present, and no timeout event for this timer.
   */
  public void createRollback() {
    createRollback(scheduleBean);
  }

  /*
   * testName: createRollbackBMT
   * 
   * @test_Strategy: same as above, but using BMT
   */
  public void createRollbackBMT() {
    createRollback(scheduleBMTBean);
  }

  /*
   * testName: createRollbackTxPropagation
   * 
   * @test_Strategy: Inside propagated transaction, create a timer that is to
   * expire in 1 second. The client transaction is rolled back. The timer must
   * not be present, and no timeout event for this timer.
   */

  public void createRollbackTxPropagation() throws Exception {
    ut.begin();
    Timer timer = scheduleBean.createSecondLaterTimer(getTimerConfig());
    TestUtil.sleep(2000); // 2 seconds
    ut.rollback();
    assertEquals("contains the timer? " + timer, false,
        scheduleBean.getTimers().contains(timer));
    passIfNoTimeout();
  }

  /*
   * testName: createRollbackTxPropagationBMT
   * 
   * @test_Strategy: See above. But for BMT, no tx propagation.
   */

  public void createRollbackTxPropagationBMT() throws Exception {
    ut.begin();
    scheduleBMTBean.createSecondLaterTimer(getTimerConfig());
    TestUtil.sleep(2000); // 2 seconds
    ut.rollback();
    passIfTimeout();
  }

  /*
   * testName: cancelRollback
   * 
   * @test_Strategy: create a timer that is to expire in the far future. The
   * subsequent business method cancels the timer, and then set the tx to
   * rollback. This timer must still be present, and has not yet expired. These
   * business methods have default transaction attribute type (REQUIRED)
   */
  public void cancelRollback() {
    cancelRollback(scheduleBean);
  }

  /*
   * testName: cancelRollbackBMT
   * 
   * @test_Strategy: See above. Using BMT
   */
  public void cancelRollbackBMT() {
    cancelRollback(scheduleBMTBean);
  }

  /*
   * testName: cancelRollbackPropagation
   * 
   * @test_Strategy: create a timer that is to expire in the far future. The
   * subsequent business method cancels the timer within a client-initiated tx.
   * The client tx is rolled back. This timer must still be present, and has not
   * yet expired. These business methods have default transaction attribute type
   * (REQUIRED)
   */
  public void cancelRollbackPropagation() throws Exception {
    Timer timer = scheduleBean.createFarFutureTimer(getTimerConfig());
    String result = "This timer must still be present, since the tx "
        + "within which cancellation occurs is rolled back: " + timer;
    ut.begin();
    scheduleBean.cancelTimer(timer);
    ut.rollback();
    assertEquals(result, true, scheduleBean.getTimers().contains(timer));
    scheduleBean.cancelTimer(timer);
    assertEquals("contains the timer? " + timer, false,
        scheduleBean.getTimers().contains(timer));
  }

  /*
   * testName: cancelRollbackPropagationBMT
   * 
   * @test_Strategy: See above. No propagation for BMT
   */
  public void cancelRollbackPropagationBMT() throws Exception {
    Timer timer = scheduleBMTBean.createSecondLaterTimer(getTimerConfig());
    String result = "This timer must not be present, since the tx is not "
        + "propagated to BMT bean: " + timer;
    ut.begin();
    scheduleBMTBean.cancelTimer(timer); // really cancel it
    ut.rollback();
    assertEquals(result, false, scheduleBMTBean.getTimers().contains(timer));
  }

  /*
   * testName: timeoutRollback
   * 
   * @test_Strategy: create a timer that is to expire in 1 second. The timeout
   * method sets rollback for this timeout event. This event must be retried at
   * least once. This only applies to CMT, not BMT. The timeout method has
   * default transaction attribute type (REQUIRED)
   */
  public void timeoutRollback() {
    timeoutRollback(scheduleBean);
  }

  /*
   * testName: timeoutSystemException
   * 
   * @test_Strategy: create a timer that is to expire in 1 second The timeout
   * throws system exception. This event must be retried at least once. The
   * timeout method has default transaction attribute type (REQUIRED)
   */
  public void timeoutSystemException() {
    timeoutRollback();
  }

  /*
   * testName: timeoutSystemExceptionBMT
   * 
   * @test_Strategy: create a timer that is to expire in 1 second The timeout
   * throws system exception. This event must be retried at least once.
   */
  public void timeoutSystemExceptionBMT() {
    timeoutRollback(scheduleBMTBean);
  }

  /*
   * testName: createTimerWithoutTx
   * 
   * @test_Strategy: invoke the BMT bean to create a timer without tx.
   */
  public void createTimerWithoutTx() {
    scheduleBMTBean.createSecondLaterTimer(getTimerConfig());
    passIfTimeout();
  }

  /*
   * testName: createTimerWithoutTxHavingClientTx
   * 
   * @test_Strategy: invoke the BMT bean to create a timer without tx. The
   * client does have a UserTransaction but it is not propagated.
   */
  public void createTimerWithoutTxHavingClientTx() throws Exception {
    ut.begin();
    scheduleBMTBean.createSecondLaterTimer(getTimerConfig());
    ut.commit();
    passIfTimeout();
  }

  private void createRollback(ScheduleTxBeanBase b) {
    String result = b.createRollback(getTimerConfig());
    appendReason(result);
    passIfNoTimeout();
  }

  private void cancelRollback(ScheduleTxBeanBase b) {
    Timer timer = b.createFarFutureTimer(getTimerConfig());
    String result = b.cancelRollback(getTestName());
    assertEquals(result, true, b.getTimers().contains(timer));
    b.cancelTimer(timer); // really cancel it
    passIfNoTimeout();
  }

  private void timeoutRollback(ScheduleTxBeanBase b) {
    appendReason(
        "If the transaction rolls back in timeout method, must retry at least once.");
    Timer timer = b.createSecondLaterTimer(getTimerConfig());
    TestUtil.sleep((int) WAIT_FOR_TIMEOUT_STATUS);
    List<String> a = statusSingleton.getRecords(getTestName());
    appendReason("timeout callback result: ", a);
    assertGreaterThan(null, a.size(), 1);

    // Containers may retry failed timeout method once or multiple times.
    // The original timer may or may not exist at this point. So do not
    // check for its existence.
    // assertEquals(null, false, b.getTimers().contains(timer));
  }
}
