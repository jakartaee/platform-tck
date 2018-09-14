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
package com.sun.ts.tests.ejb30.timer.schedule.lifecycle;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;

import javax.ejb.EJB;
import javax.ejb.ScheduleExpression;
import javax.ejb.Timer;
import javax.ejb.TimerHandle;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.timer.common.ClientBase;

public class Client extends ClientBase {

  public static final String TIMER_STORE_DIR = System
      .getProperty("java.io.tmpdir");

  public static final String TIMER_STORE_NAME = "ts-ejb30-timer.ser";

  public static final File TIMER_STORE = new File(TIMER_STORE_DIR,
      TIMER_STORE_NAME);

  @EJB(beanName = "ScheduleBean")
  private ScheduleBean scheduleBean;

  private void deleteTimerStore() {
    if (TIMER_STORE.exists()) {
      boolean deleted = TIMER_STORE.delete();
      if (deleted) {
        Helper.getLogger().info("Deleted existing TIMER_STORE file: "
            + TIMER_STORE.getAbsolutePath());
      } else {
        Helper.getLogger().info(
            "Failed to delete TIMER_STORE: " + TIMER_STORE.getAbsolutePath());
      }
    }
  }

  private void writeTimerHandle(TimerHandle handle) throws IOException {
    ObjectOutputStream out = null;
    try {
      out = new ObjectOutputStream(new FileOutputStream(TIMER_STORE));
      out.writeObject(handle);
    } finally {
      if (out != null) {
        out.close();
      }
    }
  }

  private TimerHandle readTimerHandle()
      throws IOException, ClassNotFoundException {
    ObjectInputStream in = null;
    Object obj;
    try {
      in = new ObjectInputStream(new FileInputStream(TIMER_STORE));
      obj = in.readObject();
    } finally {
      if (in != null) {
        in.close();
      }
    }
    return (TimerHandle) obj;
  }

  /*
   * @testName: timerHandle
   * 
   * @test_Strategy: verify Timer.getHandler(), pass it locally to web
   * component, write it to disk, read it back, and compare it to the original
   * one with Timer.equals(Object). Also verifies that calling Handle.getTimer()
   * after the associated timer is cancelled will result in
   * NoSuchObjectLocalException.
   */
  public void timerHandle() throws IOException, ClassNotFoundException {
    deleteTimerStore();
    Timer t = scheduleBean.findTimer(ScheduleBean.YEAR_5000);
    TimerHandle handle = scheduleBean.getTimerHandle(t);
    appendReason(" Got TimerHandle: " + handle);
    writeTimerHandle(handle);
    appendReason(" Serialized it to " + TIMER_STORE.getAbsolutePath());
    handle = readTimerHandle();
    appendReason(" Read it from TIMER_STORE ");
    appendReason(scheduleBean.compareTimer(handle));

    // TimerHandle.getTimer() in web tier is currently not allowed
    // appendReason(scheduleBean.compareTimer(handle.getTimer()));

    scheduleBean.cancelTimer(handle);
    appendReason(" Auto timer cancelled.");
    assertEquals("Confirm the timer no long exists", null,
        scheduleBean.findTimer(ScheduleBean.YEAR_5000));

    // call handle.getTimer() after the associated timer has been cancelled,
    // expecting NoSuchObjectLocalException
    appendReason(scheduleBean.getTimerExpired(handle));
  }

  /*
   * @testName: timerHandleIllegalStateException
   * 
   * @test_Strategy: verify Timer.getHandler() throws IllegalStateException for
   * non-persistent timers.
   */
  public void timerHandleIllegalStateException() {
    appendReason(scheduleBean.timerHandleIllegalStateException());
  }

  /*
   * @testName: isCalendarTimerAndGetSchedule
   * 
   * @test_Strategy: test isCalendarTimer for auto and programmatic timer, and
   * getSchedule returns the schedule expression for calendar timer and throws
   * IllegalStateException for non-calendar timer.
   */
  public void isCalendarTimerAndGetSchedule() {
    appendReason(scheduleBean.isCalendarTimerAndGetSchedule());
  }

  /*
   * @testName: timerEquals
   * 
   * @test_Strategy: test Timer.equals(Object)
   */
  public void timerEquals() {
    ScheduleExpression exp = new ScheduleExpression();
    String t1Name = getTestName() + "t1";
    Timer t1 = scheduleBean.createSecondLaterTimer(t1Name);
    Timer t2 = scheduleBean.createFarFutureTimer(getTestName());
    Timer t3 = scheduleBean.createTimer(exp, getTestName());
    Timer t4 = scheduleBean.createTimer(exp, getTestName());
    Timer t1Found = scheduleBean.findTimer(t1Name);
    assertEquals("Compare timer to itself.", t1, t1);
    assertEquals("Compare timer to t1Found.", t1, t1Found);
    assertNotEquals("Compare timer to null.", t1, null);
    assertNotEquals("Compare timer to 1.", t1, 1);
    assertNotEquals("Compare timer to true.", t1, true);
    assertNotEquals("Compare timer to TimerHandle.", t1,
        scheduleBean.getTimerHandle(t1));
    assertNotEquals("Compare TimerHandle to timer.",
        scheduleBean.getTimerHandle(t1), t1);
    assertNotEquals("Compare 2 timers.", t1, t2);
    assertNotEquals("Compare timer 3 to timer 4.", t3, t4);
    scheduleBean.cancelTimer(t1, t2, t3, t4);
  }

  /*
   * @testName: timerHash
   * 
   * @test_Strategy: serialize a TimerHandle, deserialize it to get the timer,
   * verify it with a HashMap.
   */
  public void timerHash() throws IOException, ClassNotFoundException {
    deleteTimerStore();
    Timer t = scheduleBean.createSecondLaterTimer(getTestName());
    TimerHandle handle = scheduleBean.getTimerHandle(t);
    HashSet<Timer> h = new HashSet<Timer>();
    h.add(t);
    appendReason(" Got TimerHandle: " + handle);
    writeTimerHandle(handle);
    appendReason(" Serialized it to " + TIMER_STORE.getAbsolutePath());
    handle = readTimerHandle();
    appendReason(" Read it from TIMER_STORE ");

    if (h.contains(scheduleBean.getTimer(handle))) {
      appendReason("Found the deserialize timer in the HashSet.");
    } else {
      throw new RuntimeException(
          "Failed to find the deserialize timer in the HashSet.");
    }
    scheduleBean.cancelTimer(t);
  }

  /*
   * @testName: createAndComplete
   * 
   * @test_Strategy: create a timer and wait for its completion
   */
  public void createAndComplete() {
    scheduleBean.createSecondLaterTimer(getTestName());
    appendReason("Created a timer and wait for its completion.");
    passIfTimeoutOnce();
  }

  /*
   * @testName: completeAndNoSuchObjectLocalException
   * 
   * @test_Strategy: after a timer completes, further access will result in
   * javax.ejb.NoSuchObjectLocalException
   */
  public void completeAndNoSuchObjectLocalException() {
    Timer t = scheduleBean.createSecondLaterTimer(getTestName());
    passIfTimeoutOnce();
    appendReason(scheduleBean.passIfNoSuchObjectLocalException(t));
  }

  /*
   * @testName: cancelAndNoSuchObjectLocalException
   * 
   * @test_Strategy: after a timer cancellation, further access will result in
   * javax.ejb.NoSuchObjectLocalException
   */
  public void cancelAndNoSuchObjectLocalException() {
    Timer t = scheduleBean.createFarFutureTimer(getTestName());
    scheduleBean.cancelTimerWithNoTransaction(t);
    appendReason(scheduleBean.passIfNoSuchObjectLocalException(t));
  }

  /*
   * @testName: cancelWithTxAndNoSuchObjectLocalException
   * 
   * @test_Strategy: after a timer cancellation within a tx context, further
   * access will result in javax.ejb.NoSuchObjectLocalException
   */
  public void cancelWithTxAndNoSuchObjectLocalException() {
    Timer t = scheduleBean.createFarFutureTimer(getTestName());
    scheduleBean.cancelTimer(t); // cancel with default tx context in EJB
    appendReason(scheduleBean.passIfNoSuchObjectLocalException(t));
  }
}
