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

package com.sun.ts.tests.concurrency.api.ManagedExecutors;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.concurrent.ManagedExecutors;
import javax.enterprise.concurrent.ManagedTask;
import javax.enterprise.concurrent.ManagedThreadFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.concurrency.api.common.CallableTask;
import com.sun.ts.tests.concurrency.api.common.RunnableTask;
import com.sun.ts.tests.concurrency.api.common.managedTaskListener.ListenerEvent;
import com.sun.ts.tests.concurrency.api.common.managedTaskListener.ManagedTaskListenerImpl;
import com.sun.ts.tests.concurrency.api.common.Util;

public class Client extends ServiceEETest implements java.io.Serializable {

  private static final String MANAGED_EXECUTOR_SVC_JNDI_NAME = "java:comp/DefaultManagedExecutorService";

  private static final String MANAGED_THREAD_FACTORY_JNDI_NAME = "java:comp/DefaultManagedThreadFactory";

  private static final int TASK_MAX_WAIT_SECONDS = 10;// (s)

  private static final long LISTENER_MAX_WAIT_MILLIS = 3 * 1000;// (ms)

  private static final int LISTENER_POOL_INTERVAL_MILLIS = 100;// (ms)

  private static final String ENV_ENTRY_JNDI_NAME = "java:comp/env/StringValue";

  private static final String ENV_ENTRY_VALUE = "something";

  private ManagedExecutorService managedExecutorSvc;

  private ManagedThreadFactory managedThreadFactory;

  private ManagedTaskListenerImpl managedTaskListener;

  private boolean shutdown = true;

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    Context ctx = null;
    try {
      ctx = new InitialContext();
      managedExecutorSvc = (ManagedExecutorService) ctx
          .lookup(MANAGED_EXECUTOR_SVC_JNDI_NAME);
      managedThreadFactory = (ManagedThreadFactory) ctx
          .lookup(MANAGED_THREAD_FACTORY_JNDI_NAME);
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    } finally {
      try {
        ctx.close();
      } catch (NamingException e) {
        TestUtil.logErr("Exception: ", e);
        throw new Fault("Setup failed:", e);
      }
    }
    managedTaskListener = new ManagedTaskListenerImpl();
  }

  public void cleanup() throws Fault {
    managedTaskListener.clearEvents();
    TestUtil.logTrace("cleanup");
  }

  /*
   * @testName: IsCurrentThreadShutdown
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:21
   * 
   * @test_Strategy: Use a regular thread(non-Manageable thread) and verify
   * isCurrentThreadShutdown() returns false
   * 
   */
  public void IsCurrentThreadShutdown() throws Fault {
    Thread createdThread = managedThreadFactory.newThread(new Runnable() {
      @Override
      public void run() {
        shutdown = ManagedExecutors.isCurrentThreadShutdown();
      }
    });
    // Executors.newSingleThreadExecutor() uses Executors.defaultThreadFactory()
    // to create new thread. So the thread used in this test is a non Manageable
    // Thread.
    Future<?> future = Executors.newSingleThreadExecutor()
        .submit(createdThread);
    waitForTaskComplete(future);
    if (shutdown) {
      throw new Fault(
          "IsCurrentThreadShutdown failed because shutdown is set to be true when running job");
    }
  }

  /*
   * @testName: IsCurrentThreadShutdown_ManageableThread
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:21
   * 
   * @test_Strategy: Create a ManageableThread from ManagedThreadFactory and
   * check the shutdown status.
   */
  public void IsCurrentThreadShutdown_ManageableThread() throws Fault {
    Thread createdThread = managedThreadFactory.newThread(new Runnable() {
      @Override
      public void run() {
        shutdown = ManagedExecutors.isCurrentThreadShutdown();
      }
    });
    // Executors.newSingleThreadExecutor(managedThreadFactory) uses
    // ManagedThreadFactory
    // to create new (Manageable) thread.
    Future<?> future = Executors.newSingleThreadExecutor(managedThreadFactory)
        .submit(createdThread);
    waitForTaskComplete(future);
    if (shutdown) {
      throw new Fault(
          "IsCurrentThreadShutdown failed because shutdown is set to be true when running job");
    }
  }

  /*
   * @testName: ManageRunnableTaskWithTaskListener
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:22;CONCURRENCY:SPEC:7;
   * CONCURRENCY:SPEC:7.1;CONCURRENCY:SPEC:7.2;
   * CONCURRENCY:SPEC:4;CONCURRENCY:SPEC:4.2; CONCURRENCY:SPEC:18;
   *
   * @test_Strategy: Returns a Runnable object that also implements ManagedTask
   * interface so it can receive notification of life cycle events with the
   * provided ManagedTaskListener when the task is submitted to a
   * ManagedExecutorService or a ManagedScheduledExecutorService.
   */
  public void ManageRunnableTaskWithTaskListener() throws Fault {
    RunnableTask runnableTask = createRunnableTask();
    Runnable taskWithListener = ManagedExecutors.managedTask(runnableTask,
        managedTaskListener);
    Future<?> futureResult = managedExecutorSvc.submit(taskWithListener);
    assertTaskAndListenerComplete(futureResult, runnableTask);
  }

  /*
   * @testName: ManageRunnableTaskWithNullArg
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:23
   * 
   * @test_Strategy: Catch IllegalArgumentException when get the manage task
   * with null runnable task.
   */
  public void ManageRunnableTaskWithNullArg() throws Fault {
    boolean pass = false;
    Runnable nullTask = null;
    try {
      ManagedExecutors.managedTask(nullTask, managedTaskListener);
    } catch (IllegalArgumentException e) {
      // this is what expected
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass) {
      throw new Fault(
          "ManageRunnableTaskWithNullArg failed to get expected exception");
    }
  }

  /*
   * @testName: ManageRunnableTaskWithTaskListenerAndMap
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:24;CONCURRENCY:SPEC:13;
   * 
   * @test_Strategy: Returns a Runnable object that also implements ManagedTask
   * interface so it can receive notification of life cycle events with the
   * provided ManagedTaskListener and to provide additional execution properties
   * when the task is submitted to a ManagedExecutorService
   */
  public void ManageRunnableTaskWithTaskListenerAndMap() throws Fault {
    Map<String, String> properties = new HashMap<String, String>();
    properties.put("key", "value");
    RunnableTask runnableTask = createRunnableTask();
    Runnable task = ManagedExecutors.managedTask(runnableTask, properties,
        managedTaskListener);
    boolean pass = false;
    if (task instanceof ManagedTask) {
      ManagedTask managedTask = (ManagedTask) task;
      if (managedTask.getExecutionProperties().get("key") == "value")
        pass = true;
    }
    if (!pass) {
      throw new Fault(
          "ManageRunnableTaskWithTaskListenerAndMap failed to get expected property");
    }
    assertTaskAndListenerComplete(managedExecutorSvc.submit(task),
        runnableTask);
  }

  /*
   * @testName: ManageRunnableTaskWithMapAndNullArg
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:25
   * 
   * @test_Strategy: Catch IllegalArgumentException when get the manage task
   * with null runnable task and additional execution properties.
   */
  public void ManageRunnableTaskWithMapAndNullArg() throws Fault {
    boolean pass = false;
    Runnable nullTask = null;
    Map<String, String> properties = new HashMap<String, String>();
    try {
      ManagedExecutors.managedTask(nullTask, properties, managedTaskListener);
    } catch (IllegalArgumentException e) {
      // this is what expected
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass) {
      throw new Fault(
          "ManageRunnableTaskWithMapAndNullArg failed to get expected exception");
    }
  }

  /*
   * @testName: ManageCallableTaskWithTaskListener
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:26
   * 
   * @test_Strategy: Returns a Callable object that also implements ManagedTask
   * interface so it can receive notification of life cycle events with the
   * provided ManagedTaskListener when the task is submitted to a
   * ManagedExecutorService
   */
  public void ManageCallableTaskWithTaskListener() throws Fault {
    String expectedResultStr = "expected something";
    CallableTask<String> callableTask = createCallableTask(expectedResultStr);
    Callable<String> taskWithListener = ManagedExecutors
        .managedTask((Callable<String>) callableTask, managedTaskListener);
    Future<String> futureResult = managedExecutorSvc.submit(taskWithListener);
    assertTaskAndListenerComplete(expectedResultStr, futureResult,
        callableTask);
  }

  /*
   * @testName: ManageCallableTaskWithNullArg
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:27
   * 
   * @test_Strategy: Catch IllegalArgumentException when get the manage task
   * with null Callable task.
   */
  public void ManageCallableTaskWithNullArg() throws Fault {
    boolean pass = false;
    Callable<?> nullTask = null;
    try {
      ManagedExecutors.managedTask(nullTask, managedTaskListener);
    } catch (IllegalArgumentException e) {
      // this is what expected
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass) {
      throw new Fault(
          "ManageCallableTaskWithNullArg failed to get expected exception");
    }
  }

  /*
   * @testName: ManageCallableTaskWithTaskListenerAndMap
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:28;CONCURRENCY:SPEC:13.1;
   * CONCURRENCY:SPEC:45;CONCURRENCY:SPEC:45.1;
   * 
   * @test_Strategy: Returns a Callable object that also implements ManagedTask
   * interface so it can receive notification of life cycle events with the
   * provided ManagedTaskListener and to provide additional execution properties
   * when the task is submitted to a ManagedExecutorService
   */
  public void ManageCallableTaskWithTaskListenerAndMap() throws Fault {
    Map<String, String> properties = new HashMap<String, String>();
    properties.put("key", "value");
    properties.put(ManagedTask.IDENTITY_NAME, "id");
    String expectedResultStr = "expected something";

    CallableTask<String> callableTask = createCallableTask(expectedResultStr);
    Callable<String> task = ManagedExecutors.managedTask(
        (Callable<String>) callableTask, properties, managedTaskListener);

    boolean pass = false;
    if (task instanceof ManagedTask) {
      ManagedTask managedTask = (ManagedTask) task;
      if (managedTask.getExecutionProperties().get("key") == "value")
        pass = true;
    }
    if (!pass) {
      throw new Fault(
          "ManageCallableTaskWithTaskListenerAndMap failed to get expected property");
    }
    assertTaskAndListenerComplete(expectedResultStr,
        managedExecutorSvc.submit(task), callableTask);
  }

  /*
   * @testName: ManageCallableTaskWithMapAndNullArg
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:29
   * 
   * @test_Strategy: Catch IllegalArgumentException when get the manage task
   * with null Callable task and additional execution properties.
   */
  public void ManageCallableTaskWithMapAndNullArg() throws Fault {
    boolean pass = false;
    Callable<?> nullTask = null;
    Map<String, String> properties = new HashMap<String, String>();
    try {
      ManagedExecutors.managedTask(nullTask, properties, managedTaskListener);
    } catch (IllegalArgumentException e) {
      // this is what expected
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Unexpected Exception Caught", e);
    }
    if (!pass) {
      throw new Fault("ManageTaskWithMapAndNullArg failed");
    }
  }

  private void assertTaskAndListenerComplete(Future<?> future,
      RunnableTask runnableTask) throws Fault {
    waitForTaskComplete(future);
    assertListenerComplete(runnableTask);
  }

  private void assertTaskAndListenerComplete(String expectedResult,
      Future<String> future, CallableTask<?> callableTask) throws Fault {
    String result = waitForTaskComplete(future);
    if (!expectedResult.endsWith(result)) {
      throw new Fault("Task return different value with expected one.");
    }
    assertListenerComplete(callableTask);
  }

  private <T> T waitForTaskComplete(Future<T> future) throws Fault {
    return Util.waitForTaskComplete(future, TASK_MAX_WAIT_SECONDS);
  }

  private void assertListenerComplete(RunnableTask task) throws Fault {
    // wait for the listener run done.
    Util.waitForListenerComplete(managedTaskListener, LISTENER_MAX_WAIT_MILLIS,
        LISTENER_POOL_INTERVAL_MILLIS);
    // check listener status.
    if (!(managedTaskListener.eventCalled(ListenerEvent.SUBMITTED)
        && managedTaskListener.eventCalled(ListenerEvent.STARTING)
        && managedTaskListener.eventCalled(ListenerEvent.DONE))) {
      throw new Fault("TaskListener is not completely executed.");
    }
  }

  private RunnableTask createRunnableTask() {
    return new RunnableTask(ENV_ENTRY_JNDI_NAME, ENV_ENTRY_VALUE,
        this.getClass().getName());
  }

  private CallableTask<String> createCallableTask(String expectedReturnValue) {
    return new CallableTask<String>(ENV_ENTRY_JNDI_NAME, ENV_ENTRY_VALUE,
        this.getClass().getName(), expectedReturnValue);
  }
}
