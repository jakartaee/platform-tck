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
package com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.accesstimeout;

import java.util.Properties;
import java.util.Vector;

import javax.ejb.ConcurrentAccessException;
import javax.ejb.ConcurrentAccessTimeoutException;
import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;

public class Client extends EJBLiteClientBase {
  private static final int NUM_OF_THREADS = 100;

  private static final int NUM_OF_READS = 1;

  private static final int NUM_OF_WRITES = 1;

  /**
   * used to make sure the whole method invocation including the busyWait does
   * not exceed the configured timeout
   */
  private static final long LESS_THAN_TIMEOUT_DURATION_MILLIS = 500;

  private static final long READ_WRITE_DURATION_MILLIS = 2000;

  private static final Vector<Integer> readResults = new Vector<Integer>();

  private static final int READ_VAL = 1;

  private static final Vector<Throwable> writeErrors = new Vector<Throwable>();

  private static final Vector<Throwable> readErrors = new Vector<Throwable>();

  @EJB(beanName = "ClassLevelCallbackAccessTimeoutBean")
  protected ClassLevelCallbackAccessTimeoutBean classLevelCallbackAccessTimeoutBean;

  @EJB(beanName = "MethodLevelCallbackAccessTimeoutBean")
  protected MethodLevelCallbackAccessTimeoutBean methodLevelCallbackAccessTimeoutBean;

  @EJB(name = "TimeUnit", beanName = "TimeUnit")
  protected TimeUnitBean timeUnitBean;

  @EJB(beanName = "ClassLevel")
  protected AccessTimeoutIF classLevelAccessTimeoutBean;

  @EJB(beanName = "InheritAccessTimeoutBean")
  protected AccessTimeoutIF inheritAccessTimeoutBean;

  @EJB(beanName = "MethodLevelAccessTimeoutBean")
  protected AccessTimeoutIF methodLevelAccessTimeoutBean;

  @EJB(beanName = "KeepWaitOrNotAllowedBean")
  protected AccessTimeoutIF keepWaitOrNotAllowedBean;

  @Override
  public void setup(String[] args, Properties p) {
    super.setup(args, p);
    readResults.clear();
    readErrors.clear();
    writeErrors.clear();
  }

  /*
   * @testName: longReadsClassLevel
   * 
   * @test_Strategy: multiple invocations of longRead share the lock, hence no
   * timeout should occur. Each invocation does not cause time out. All these
   * concurrent longReads should not cause time out, either.
   */
  public void longReadsClassLevel() {
    longReads(classLevelAccessTimeoutBean);
  }

  /*
   * @testName: longReadsInherit
   * 
   * @test_Strategy:
   */
  public void longReadsInherit() {
    longReads(inheritAccessTimeoutBean);
  }

  /*
   * @testName: longReadsMethodLevel
   * 
   * @test_Strategy:
   */
  public void longReadsMethodLevel() {
    longReads(methodLevelAccessTimeoutBean);
  }

  /*
   * @testName: longReadsLongRead2sClassLevel
   * 
   * @test_Strategy: multiple invocations of longRead AND longRead2 share the
   * lock, hence no timeout should occur. Each invocation does not cause time
   * out. All these concurrent longReads should not cause time out, either.
   */
  public void longReadsLongRead2sClassLevel() {
    longReadsLongRead2s(classLevelAccessTimeoutBean);
  }

  /*
   * @testName: longReadsLongRead2sInherit
   * 
   * @test_Strategy:
   */
  public void longReadsLongRead2sInherit() {
    longReadsLongRead2s(inheritAccessTimeoutBean);
  }

  /*
   * @testName: longReadsLongRead2sMethodLevel
   * 
   * @test_Strategy:
   */
  public void longReadsLongRead2sMethodLevel() {
    longReadsLongRead2s(methodLevelAccessTimeoutBean);
  }

  /*
   * @testName: longWritesClassLevel
   * 
   * @test_Strategy: multiple invocations of longWrite compete for the lock,
   * hence timeout should occur. Each longWrite operation, when run
   * individually, does not cause time out. When run concurrently, should cause
   * some time out.
   */
  public void longWritesClassLevel() {
    longWrites(classLevelAccessTimeoutBean);
  }

  /*
   * @testName: concurrencyNotAllowed
   * 
   * @test_Strategy: access timeout set to -1
   */
  public void concurrencyNotAllowed() {
    // a flag to indicate the test should get ConcurrentAccessException, instead
    // of
    // ConcurrentAccessTimeoutException
    boolean expectingConcurrentAccessException = true;
    longWrites(keepWaitOrNotAllowedBean, expectingConcurrentAccessException);
  }

  /*
   * @testName: keepWaiting
   * 
   * @test_Strategy: access time out set to 0
   */
  public void keepWaiting() {
    longReads(keepWaitOrNotAllowedBean);
  }

  /*
   * @testName: longWritesInherit
   * 
   * @test_Strategy:
   */
  public void longWritesInherit() {
    longWrites(inheritAccessTimeoutBean);
  }

  /*
   * @testName: longWritesMethodLevel
   * 
   * @test_Strategy:
   */
  public void longWritesMethodLevel() {
    longWrites(methodLevelAccessTimeoutBean);
  }

  /*
   * @ testName: longWritesLongReadClassLevel
   * 
   * @test_Strategy: multiple invocations of longWrite compete for the lock,
   * hence timeout should occur. A subsequent longRead waits for the lock and
   * then times out.
   *
   * Excluded:
   * https://bug.oraclecorp.com/pls/bug/webbug_edit.edit_info_top?rptno=12804747
   * &rptno_count=1&pos=1&report_title=&query_id=-1
   *
   * public void longWritesLongReadClassLevel() {
   * longWritesLongRead(classLevelAccessTimeoutBean); }
   */
  /*
   * @ testName: longWritesLongReadInherit
   * 
   * @test_Strategy:
   *
   * Excluded:
   * https://bug.oraclecorp.com/pls/bug/webbug_edit.edit_info_top?rptno=12804747
   * &rptno_count=1&pos=1&report_title=&query_id=-1
   *
   * public void longWritesLongReadInherit() {
   * longWritesLongRead(inheritAccessTimeoutBean); }
   */
  /*
   * @ testName: longWritesLongReadMethodLevel
   * 
   * @test_Strategy:
   *
   * Excluded:
   * https://bug.oraclecorp.com/pls/bug/webbug_edit.edit_info_top?rptno=12804747
   * &rptno_count=1&pos=1&report_title=&query_id=-1
   *
   * public void longWritesLongReadMethodLevel() {
   * longWritesLongRead(methodLevelAccessTimeoutBean); }
   */

  /*
   * @testName: timeoutInterceptor
   * 
   * @test_Strategy: verify that the time taken by interceptors is also included
   * in the timeout decision.
   */
  public void timeoutInterceptor() {
    try {
      timeUnitBean.timeoutInterceptor(READ_WRITE_DURATION_MILLIS, READ_VAL);
    } catch (ConcurrentAccessTimeoutException e) {
      appendReason("Got expected " + e);
    }
  }

  /*
   * @testName: timeUnits
   * 
   * @test_Strategy: verify various TimeUnits are valid and properly processed.
   */
  public void timeUnits() {
    // time out after 1 day. Expecting no time out and correct result
    int result = timeUnitBean.days(READ_WRITE_DURATION_MILLIS, READ_VAL);
    assertEquals("days ", READ_VAL, result);

    // time out after 1 hour. Expecting no time out and correct result
    result = timeUnitBean.hours(READ_WRITE_DURATION_MILLIS, READ_VAL);
    assertEquals("hours", READ_VAL, result);

    // time out after Integer.MAX_VALUE. Expecting no time out and correct
    // result
    result = timeUnitBean.minutes(READ_WRITE_DURATION_MILLIS, READ_VAL);
    assertEquals("minutes", READ_VAL, result);

    result = timeUnitBean.defaults(READ_WRITE_DURATION_MILLIS, READ_VAL);
    assertEquals("defaults", READ_VAL, result);

    result = timeUnitBean.seconds(READ_WRITE_DURATION_MILLIS, READ_VAL);
    assertEquals("seconds", READ_VAL, result);

    result = timeUnitBean.microseconds(READ_WRITE_DURATION_MILLIS, READ_VAL);
    assertEquals("microseconds", READ_VAL, result);

    result = timeUnitBean.nanoseconds(READ_WRITE_DURATION_MILLIS, READ_VAL);
    assertEquals("nanoseconds", READ_VAL, result);

    result = timeUnitBean.zero(READ_WRITE_DURATION_MILLIS, READ_VAL);
    assertEquals("zero value", READ_VAL, result);

    result = timeUnitBean.minus1(READ_WRITE_DURATION_MILLIS, READ_VAL);
    assertEquals("-1 value", READ_VAL, result);
  }

  /*
   * @testName: classLevelCallbackAccessTimeout
   * 
   * @test_Strategy: verify the wait time in post-construct method does not
   * count towards AccessTimeout; wait time in around-invoke method is not
   * counted towards AccessTimeout; post-construct and around-invoke methods can
   * be exposed as business methods; around-invoke method is also invoked when
   * itself is invoked as a business method; preDestroy method can be exposed as
   * a business method and the class-level AccessTimeout also applies.
   */
  public void classLevelCallbackAccessTimeout() throws Exception {
    callbackAccessTimeout(classLevelCallbackAccessTimeoutBean);
  }

  /*
   * @testName: methodLevelCallbackAccessTimeout
   * 
   * @test_Strategy:
   */
  public void methodLevelCallbackAccessTimeout() throws Exception {
    callbackAccessTimeout(methodLevelCallbackAccessTimeoutBean);
  }

  private void callbackAccessTimeout(CallbackAccessTimeoutBeanBase b)
      throws Exception {
    assertEquals(null, READ_VAL, b.postConstructWait(READ_VAL));
    b.postConstruct();
    b.preDestroy();
    b.intercept(null);
  }

  private void longReads(AccessTimeoutIF b) {
    Thread[] threads = new Thread[NUM_OF_THREADS];
    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(new ReadTask(b));
      threads[i].start();
    }
    for (int i = 0; i < threads.length; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
      }
    }
    assertEquals("Count readResults ", NUM_OF_THREADS * NUM_OF_READS,
        readResults.size());
    for (int i : readResults) {
      assertEquals(null, READ_VAL, i);
    }
  }

  private void longReadsLongRead2s(AccessTimeoutIF b) {
    Thread[] threads = new Thread[NUM_OF_THREADS];
    for (int i = 0; i < threads.length; i++) {
      if (i % 2 == 0) {
        threads[i] = new Thread(new ReadTask(b));
      } else {
        threads[i] = new Thread(new Read2Task(b));
      }
      threads[i].start();
    }
    for (int i = 0; i < threads.length; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
      }
    }
    assertEquals("Count readResults ", NUM_OF_THREADS * NUM_OF_READS,
        readResults.size());
    for (int i : readResults) {
      if (i == READ_VAL || i == READ_VAL * 2) {
        // appendReason("Got expected " + i);
      } else {
        throw new RuntimeException("Expecting " + READ_VAL + ", or "
            + READ_VAL * 2 + ", but got " + i);
      }
    }
    appendReason(readResults); // if the test passed, print out all readResults
  }

  private void longWrites(AccessTimeoutIF b,
      boolean... expectingConcurrentAccessExceptions) {
    Thread[] threads = new Thread[NUM_OF_THREADS];

    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(new WriteTask(b));
      threads[i].start();
    }
    for (int i = 0; i < threads.length; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
      }
    }
    assertNotEquals("Count writeErrors ", 0, writeErrors.size());
    verifyConcurrentAccessTimeoutException(writeErrors,
        expectingConcurrentAccessExceptions);
  }

  private void longWritesLongRead(AccessTimeoutIF b) {
    Thread[] threads = new Thread[NUM_OF_THREADS + 1];
    for (int i = 0; i < NUM_OF_THREADS; i++) {
      threads[i] = new Thread(new WriteTask(b));
      threads[i].start();
    }
    threads[threads.length - 1] = new Thread(new ReadTask(b));
    threads[threads.length - 1].start();
    for (int i = 0; i < threads.length; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
      }
    }
    assertNotEquals("Count writeErrors ", 0, writeErrors.size());
    verifyConcurrentAccessTimeoutException(writeErrors);
    assertEquals("Count readErrors ", 1, readErrors.size());
    verifyConcurrentAccessTimeoutException(readErrors);
  }

  private void verifyConcurrentAccessTimeoutException(Vector<Throwable> errors,
      boolean... expectingConcurrentAccessExceptions) {
    boolean expectingConcurrentAccessException = expectingConcurrentAccessExceptions.length == 0
        ? false
        : expectingConcurrentAccessExceptions[0];
    for (Throwable th : errors) {
      if (expectingConcurrentAccessException) {
        if (th instanceof ConcurrentAccessException) {
          appendReason("Got expected " + th);
        } else {
          throw new RuntimeException(
              "Expecting ConcurrentAccessException, but got " + th);
        }
      } else {
        if (th instanceof ConcurrentAccessTimeoutException) {
          appendReason("Got expected " + th);
        } else {
          throw new RuntimeException(
              "Expecting ConcurrentAccessTimeoutException, but got " + th);
        }
      }
    }
  }

  private static class ReadTask implements Runnable {
    private AccessTimeoutIF readBean;

    public ReadTask(AccessTimeoutIF b) {
      readBean = b;
    }

    public void run() {
      for (int i = 0; i < NUM_OF_READS; i++) {
        // each invocation takes at least LESS_THAN_TIMEOUT_DURATION_MILLIS
        // The bean is configured to timeout after 1000 millis
        try {
          readResults.add(
              readBean.longRead(LESS_THAN_TIMEOUT_DURATION_MILLIS, READ_VAL));
        } catch (ConcurrentAccessTimeoutException e) {
          readErrors.add(e);
        }
      }
    }
  }

  private static class Read2Task implements Runnable {
    private AccessTimeoutIF read2Bean;

    public Read2Task(AccessTimeoutIF b) {
      read2Bean = b;
    }

    public void run() {
      for (int i = 0; i < NUM_OF_READS; i++) {
        // each invocation takes at least LESS_THAN_TIMEOUT_DURATION_MILLIS
        // The bean is configured to timeout after 1000 millis
        try {
          readResults.add(read2Bean.longRead2(LESS_THAN_TIMEOUT_DURATION_MILLIS,
              READ_VAL * 2));
        } catch (ConcurrentAccessTimeoutException e) {
          readErrors.add(e);
        }
      }
    }
  }

  private static class WriteTask implements Runnable {
    private AccessTimeoutIF writeBean;

    public WriteTask(AccessTimeoutIF b) {
      writeBean = b;
    }

    public void run() {
      for (int i = 0; i < NUM_OF_WRITES; i++) {
        // each invocation takes at least LESS_THAN_TIMEOUT_DURATION_MILLIS
        // millis
        // The bean is configured to timeout after 1000 millis
        try {
          writeBean.longWrite(LESS_THAN_TIMEOUT_DURATION_MILLIS);
        } catch (Throwable th) {
          writeErrors.add(th);
        }
      }
    }
  }
}
