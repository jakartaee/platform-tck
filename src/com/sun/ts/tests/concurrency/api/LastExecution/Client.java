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

package com.sun.ts.tests.concurrency.api.LastExecution;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import javax.naming.*;
import javax.enterprise.concurrent.*;
import java.util.*;
import java.util.concurrent.*;
import com.sun.ts.tests.concurrency.api.common.*;
import com.sun.ts.tests.concurrency.common.counter.*;

public class Client extends ServiceEETest implements java.io.Serializable {

  InitialContext context;

  ManagedScheduledExecutorService executorService;

  public static final String IDENTITY_NAME_TEST_ID = "lastExecutionGetIdentityNameTest";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    try {
      context = new InitialContext();
      executorService = (ManagedScheduledExecutorService) context
          .lookup(Util.SCHEDULED_MANAGED_EXECUTOR_SVC_JNDI_NAME);
      StaticCounter.reset();
    } catch (Exception e) {
      TestUtil.logErr("Exception: ", e);
      throw new Fault("Setup failed:", e);
    }
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
  }

  /*
   * @testName: lastExecutionGetIdentityNameTest
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:15
   * 
   * @test_Strategy: The name or ID of the identifiable object, as specified in
   * the ManagedTask#IDENTITY_NAME execution property of the task if it also
   * implements the ManagedTask interface.
   */
  public void lastExecutionGetIdentityNameTest() throws Fault {

    Map<String, String> executionProperties = new HashMap<String, String>();
    executionProperties.put(ManagedTask.IDENTITY_NAME, IDENTITY_NAME_TEST_ID);

    ScheduledFuture sf = executorService.schedule(
        ManagedExecutors.managedTask(new CounterRunnableTask(),
            executionProperties, null),
        new LogicDrivenTrigger(Util.COMMON_CHECK_INTERVAL,
            LogicDrivenTrigger.TEST_NAME_LASTEXECUTIONGETIDENTITYNAMETEST));
    Util.waitTillFutureIsDone(sf);

    if (StaticCounter.getCount() != LogicDrivenTrigger.RIGHT_COUNT) {
      throw new Fault(
          "Got wrong identity name. See server log for more details.");
    }
  }

  /*
   * @testName: lastExecutionGetResultTest
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:16
   * 
   * @test_Strategy: Result of the last execution.
   */
  public void lastExecutionGetResultTest() throws Fault {
    // test with runnable, LastExecution should return null
    ScheduledFuture sf = executorService.schedule(
        ManagedExecutors.managedTask(new CounterRunnableTask(), null, null),
        new LogicDrivenTrigger(Util.COMMON_CHECK_INTERVAL,
            LogicDrivenTrigger.TEST_NAME_LASTEXECUTIONGETRESULTTEST_RUNNABLE));
    Util.waitTillFutureIsDone(sf);
    if (StaticCounter.getCount() != LogicDrivenTrigger.RIGHT_COUNT) {
      throw new Fault(
          "Got wrong last execution result. See server log for more details.");
    }

    StaticCounter.reset();
    // test with callable, LastExecution should return 1
    sf = executorService.schedule(
        ManagedExecutors.managedTask(new CounterCallableTask(), null, null),
        new LogicDrivenTrigger(Util.COMMON_CHECK_INTERVAL,
            LogicDrivenTrigger.TEST_NAME_LASTEXECUTIONGETRESULTTEST_CALLABLE));
    Util.waitTillFutureIsDone(sf);
    if (StaticCounter.getCount() != LogicDrivenTrigger.RIGHT_COUNT) {
      throw new Fault(
          "Got wrong last execution result. See server log for more details.");
    }
  }

  /*
   * @testName: lastExecutionGetRunningTimeTest
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:17; CONCURRENCY:JAVADOC:18;
   * CONCURRENCY:JAVADOC:19
   * 
   * @test_Strategy: The last time in which the task was completed.
   */
  public void lastExecutionGetRunningTimeTest() throws Fault {
    ScheduledFuture sf = executorService.schedule(
        ManagedExecutors.managedTask(
            new CounterRunnableTask(
                LogicDrivenTrigger.LASTEXECUTIONGETRUNNINGTIMETEST_SLEEP_TIME),
            null, null),
        new LogicDrivenTrigger(Util.COMMON_CHECK_INTERVAL,
            LogicDrivenTrigger.TEST_NAME_LASTEXECUTIONGETRUNNINGTIMETEST));
    Util.waitTillFutureIsDone(sf);

    if (StaticCounter.getCount() != LogicDrivenTrigger.RIGHT_COUNT) {
      throw new Fault("Got wrong last execution result.");
    }
  }

}
