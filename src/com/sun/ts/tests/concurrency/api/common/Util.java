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

package com.sun.ts.tests.concurrency.api.common;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.enterprise.concurrent.ManagedExecutorService;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.concurrency.api.common.managedTaskListener.ListenerEvent;
import com.sun.ts.tests.concurrency.api.common.managedTaskListener.ManagedTaskListenerImpl;

public class Util {

  private static final String MANAGED_EXECUTOR_SVC_JNDI_NAME = "java:comp/DefaultManagedExecutorService";

  public static final String SCHEDULED_MANAGED_EXECUTOR_SVC_JNDI_NAME = "java:comp/DefaultManagedScheduledExecutorService";

  public static final String MANAGED_THREAD_FACTORY_SVC_JNDI_NAME = "java:comp/DefaultManagedThreadFactory";

  public static final long COMMON_CHECK_INTERVAL = 5 * 1000;

  public static final long COMMON_TASK_TIMEOUT = 30 * 1000;

  public static final int COMMON_CHECK_INTERVAL_IN_SECOND = 5;

  public static final int COMMON_TASK_TIMEOUT_IN_SECOND = 30;

  public static final String SERVLET_RETURN_SUCCESS = "success";

  public static final String SERVLET_RETURN_FAIL = "fail";

  private Util() {
  }

  public static <T> T waitForTaskComplete(final Future<T> future,
      final int maxTaskWaitTime) throws Fault {
    T result = null;
    try {
      result = future.get(maxTaskWaitTime, TimeUnit.SECONDS);
    } catch (InterruptedException e) {
      throw new Fault("failed to finish task", e);
    } catch (ExecutionException e) {
      throw new Fault("failed to finish task", e);
    } catch (TimeoutException e) {
      throw new Fault(
          "failed to finish task in " + maxTaskWaitTime + " seconds.", e);
    } catch (Exception e) { // may caught the exception thrown from the task
                            // submitted.
      throw new Fault("failed to finish task ", e);
    }
    return result;
  }

  public static void waitForListenerComplete(
      ManagedTaskListenerImpl managedTaskListener, long maxListenerWaitTime,
      int poolInterval) {
    final long stopTime = System.currentTimeMillis() + maxListenerWaitTime;
    while (!managedTaskListener.eventCalled(ListenerEvent.DONE)
        && System.currentTimeMillis() < stopTime) {
      TestUtil.sleep(poolInterval);
    }
  }

  public static ManagedExecutorService getManagedExecutorService() {
    Context ctx = null;
    ManagedExecutorService managedExecutorSvc = null;
    try {
      ctx = new InitialContext();
      managedExecutorSvc = (ManagedExecutorService) ctx
          .lookup(MANAGED_EXECUTOR_SVC_JNDI_NAME);
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
    } finally {
      try {
        ctx.close();
      } catch (NamingException e) {
        TestUtil.logErr("Exception: ", e);
      }
    }
    return managedExecutorSvc;
  }

  /**
   * The difference between this method and waitForTaskComplete is that some
   * scheduled task will return values for multiple times, in this situation
   * waitForTaskComplete does not work.
   */
  public static void waitTillFutureIsDone(Future future) {
    long start = System.currentTimeMillis();

    while (!future.isDone()) {
      try {
        Thread.sleep(COMMON_CHECK_INTERVAL);
      } catch (InterruptedException ignore) {
      }

      if ((System.currentTimeMillis() - start) > COMMON_TASK_TIMEOUT) {
        throw new RuntimeException("wait task timeout");
      }
    }
  }

  public static void assertEquals(Object expected, Object actual) {
    String msg = "expected " + expected + " but you got " + actual;
    if (expected == null && actual == null) {
      return;
    }
    if (expected == null || actual == null) {
      throw new RuntimeException(msg);
    }
    if (!expected.equals(actual)) {
      throw new RuntimeException(msg);
    }
  }

  public static void assertInRange(Object[] range, Object actual) {
    String expected = "";
    for (Object each : range) {
      expected += each.toString();
      expected += ",";
    }
    expected = expected.substring(0, expected.length() - 1);
    String msg = "expected in " + expected + " but you got " + actual;
    for (Object each : range) {
      if (each.equals(actual)) {
        return;
      }
    }
    throw new RuntimeException(msg);
  }

  public static void asserIntInRange(int low, int high, int actual) {
    String msg = "expected in range " + low + " , " + high;
    msg += " but you got " + actual;
    if (actual < low || actual > high) {
      throw new RuntimeException(msg);
    }
  }

  public static void waitTillThreadFinish(Thread thread) {
    long start = System.currentTimeMillis();

    while (thread.isAlive()) {
      try {
        Thread.sleep(COMMON_CHECK_INTERVAL);
      } catch (InterruptedException ignore) {
      }

      if ((System.currentTimeMillis() - start) > COMMON_TASK_TIMEOUT) {
        throw new RuntimeException("wait task timeout");
      }
    }
  }
}
