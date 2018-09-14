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

package com.sun.ts.tests.concurrency.api.ManagedTaskListener;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;

import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedExecutors;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.concurrency.api.common.Util;
import com.sun.ts.tests.concurrency.api.common.managedTaskListener.ListenerEvent;
import com.sun.ts.tests.concurrency.api.common.managedTaskListener.ManagedTaskListenerImpl;

public class Client extends ServiceEETest implements java.io.Serializable {

  private static final long LISTENER_MAX_WAIT_MILLIS = 3 * 1000;// (ms)

  private static final int LISTENER_POOL_INTERVAL_MILLIS = 100;// (ms)

  private ManagedExecutorService managedExecutorSvc;

  private ManagedTaskListenerImpl managedTaskListener;

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    managedExecutorSvc = Util.getManagedExecutorService();
    managedTaskListener = new ManagedTaskListenerImpl();
  }

  public void cleanup() throws Fault {
    managedTaskListener.clearEvents();
    TestUtil.logMsg("cleanup");
  }

  /*
   * @testName: TaskAborted
   * 
   * @assertion_ids:
   * CONCURRENCY:JAVADOC:38;CONCURRENCY:SPEC:7;CONCURRENCY:SPEC:7.1;CONCURRENCY:
   * SPEC:45.3;
   * 
   * @test_Strategy: taskAborted of ManagedTaskListener is Called when a task's
   * Future has been cancelled anytime during the life of a task.
   */
  public void TaskAborted() throws Fault {
    int blockTime = 3000;// (ms)
    Runnable runnableTask = new RunnableTaskWithStatus(managedTaskListener,
        blockTime);
    Runnable taskWithListener = ManagedExecutors.managedTask(runnableTask,
        managedTaskListener);
    Future<?> futureResult = managedExecutorSvc.submit(taskWithListener);
    TestUtil.sleepMsec(1000);
    futureResult.cancel(true);
    Util.waitForListenerComplete(managedTaskListener,
        blockTime + LISTENER_MAX_WAIT_MILLIS, LISTENER_POOL_INTERVAL_MILLIS);
    List<ListenerEvent> events = managedTaskListener.events();
    if (!(events.contains(ListenerEvent.ABORTED)
        && futureResult.isCancelled())) {
      throw new Fault("Listener taskAborted failed");
    }
  }

  /*
   * @testName: TaskDone
   * 
   * @assertion_ids:
   * CONCURRENCY:JAVADOC:39;CONCURRENCY:SPEC:13.3;CONCURRENCY:SPEC:45.3;
   * 
   * @test_Strategy: TaskDone is called when a submitted task has completed
   * running, either successfully or failed .
   */
  public void TaskDone() throws Fault {
    // in cancel case
    final int blockTime = 3000;// (ms)
    Runnable taskToCancelled = new RunnableTaskWithStatus(managedTaskListener,
        blockTime);
    Runnable taskToCancelledWithListener = ManagedExecutors
        .managedTask(taskToCancelled, managedTaskListener);
    Future<?> futureResult = managedExecutorSvc
        .submit(taskToCancelledWithListener);
    TestUtil.sleepMsec(1000);
    futureResult.cancel(true);
    Util.waitForListenerComplete(managedTaskListener,
        blockTime + LISTENER_MAX_WAIT_MILLIS, LISTENER_POOL_INTERVAL_MILLIS);
    List<ListenerEvent> events = managedTaskListener.events();
    if (!events.contains(ListenerEvent.DONE)) {
      throw new Fault("Listener taskDone failed in cancel case.");
    }
    managedTaskListener.clearEvents();

    // in normal case
    Runnable runTask = new RunnableTaskWithStatus(managedTaskListener);
    Runnable runtaskWithListener = ManagedExecutors.managedTask(runTask,
        managedTaskListener);
    managedExecutorSvc.submit(runtaskWithListener);
    Util.waitForListenerComplete(managedTaskListener, LISTENER_MAX_WAIT_MILLIS,
        LISTENER_POOL_INTERVAL_MILLIS);
    List<ListenerEvent> runevents = managedTaskListener.events();
    if (!runevents.contains(ListenerEvent.DONE)) {
      throw new Fault("Listener TaskDone failed");
    }
    managedTaskListener.clearEvents();

    // in exception case
    Runnable taskWithException = new RunnableTaskWithException(
        managedTaskListener);
    Runnable taskWithExceptionListener = ManagedExecutors
        .managedTask(taskWithException, managedTaskListener);
    managedExecutorSvc.submit(taskWithExceptionListener);
    Util.waitForListenerComplete(managedTaskListener, LISTENER_MAX_WAIT_MILLIS,
        LISTENER_POOL_INTERVAL_MILLIS);
    List<ListenerEvent> runeventsWithException = managedTaskListener.events();
    TestUtil.logMsg("++ runeventsWithException : " + runeventsWithException);
    if (!runeventsWithException.contains(ListenerEvent.DONE)) {
      throw new Fault("Listener TaskDone failed with exception task.");
    }

  }

  /*
   * @testName: TaskStarting
   * 
   * @assertion_ids:
   * CONCURRENCY:JAVADOC:40;CONCURRENCY:SPEC:7;CONCURRENCY:SPEC:7.3;CONCURRENCY:
   * SPEC:45.3;
   * 
   * @test_Strategy: TaskStarting is called before the task is about to start.
   * The task will not enter the starting state until the taskSubmitted listener
   * has completed.
   */
  public void TaskStarting() throws Fault {
    Runnable runnableTask = new RunnableTaskWithStatus(managedTaskListener);
    Runnable taskWithListener = ManagedExecutors.managedTask(runnableTask,
        managedTaskListener);
    managedExecutorSvc.submit(taskWithListener);
    Util.waitForListenerComplete(managedTaskListener, LISTENER_MAX_WAIT_MILLIS,
        LISTENER_POOL_INTERVAL_MILLIS);
    List<ListenerEvent> events = managedTaskListener.events();
    int submitAt = events.indexOf(ListenerEvent.SUBMITTED);
    int startAt = events.indexOf(ListenerEvent.STARTING);
    int runAt = events.indexOf(ListenerEvent.TASK_RUN);
    if (!(submitAt == 0 && startAt == 1) && runAt == 2) {
      throw new Fault("Listener TaskStarting failed to run in expected order");
    }
  }

  /*
   * @testName: TaskSubmitted
   * 
   * @assertion_ids:
   * CONCURRENCY:JAVADOC:41;CONCURRENCY:SPEC:7;CONCURRENCY:SPEC:7.2;CONCURRENCY:
   * SPEC:45.3;
   * 
   * @test_Strategy: TaskSubmitted is called after the task has been submitted
   * to the Executor. The task will not enter the starting state until the
   * taskSubmitted listener has completed.
   */
  public void TaskSubmitted() throws Fault {
    Runnable runnableTask = new RunnableTaskWithStatus(managedTaskListener);
    Runnable taskWithListener = ManagedExecutors.managedTask(runnableTask,
        managedTaskListener);
    managedExecutorSvc.submit(taskWithListener);
    Util.waitForListenerComplete(managedTaskListener, LISTENER_MAX_WAIT_MILLIS,
        LISTENER_POOL_INTERVAL_MILLIS);
    List<ListenerEvent> events = managedTaskListener.events();
    int submitAt = events.indexOf(ListenerEvent.SUBMITTED);
    if (submitAt != 0) {
      throw new Fault("Listener TaskSubmitted failed to run in expected order");
    }
  }

}
