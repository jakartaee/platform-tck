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

package com.sun.ts.tests.concurrency.api.ManagedTask;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.enterprise.concurrent.ManagedExecutors;
import javax.enterprise.concurrent.ManagedTask;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.concurrency.api.common.RunnableTask;
import com.sun.ts.tests.concurrency.api.common.managedTaskListener.ManagedTaskListenerImpl;

public class Client extends ServiceEETest implements java.io.Serializable {

  private static final String ENV_ENTRY_JNDI_NAME = "java:comp/env/StringValue";

  private static final String ENV_ENTRY_VALUE = "something";

  private ManagedTaskListenerImpl managedTaskListener;

  public Client() {
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("setup");
    managedTaskListener = new ManagedTaskListenerImpl();
  }

  public void cleanup() throws Fault {
    TestUtil.logTrace("cleanup");
  }

  /*
   * @testName: GetExecutionProperties
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:36
   * 
   * @test_Strategy: Get ManagedTask to provides additional information to the
   * ManagedExecutorService or ManagedScheduledExecutorService when executing
   * this task.
   */
  public void GetExecutionProperties() throws Fault {
    Map<String, String> properties = new HashMap<String, String>();
    properties.put("key", "value");
    Runnable runnableTask = createRunnableTask();
    Runnable task = ManagedExecutors.managedTask(runnableTask, properties,
        managedTaskListener);
    boolean pass = false;

    if (task instanceof ManagedTask) {
      ManagedTask managedTask = (ManagedTask) task;
      if (managedTask.getExecutionProperties().get("key") == "value")
        pass = true;
    }
    if (!pass) {
      throw new Fault("GetExecutionProperties failed to get expected property");
    }

  }

  /*
   * @testName: GetManagedTaskListener
   * 
   * @assertion_ids: CONCURRENCY:JAVADOC:37
   * 
   * @test_Strategy: Get ManagedTask with ManagedTaskListener to receive
   * notification of life cycle events of this task.
   */
  public void GetManagedTaskListener() throws Fault {
    Map<String, String> properties = new HashMap<String, String>();
    properties.put("key", "value");
    RunnableTask runnableTask = createRunnableTask();
    Runnable task = ManagedExecutors.managedTask(runnableTask, properties,
        managedTaskListener);
    boolean pass = false;

    if (task instanceof ManagedTask) {
      ManagedTask managedTask = (ManagedTask) task;
      if (managedTask.getManagedTaskListener() == managedTaskListener) {
        pass = true;
      }
    }
    if (!pass) {
      throw new Fault(
          "GetManagedTaskListener failed to get expected managedTaskListener");
    }

  }

  private RunnableTask createRunnableTask() {
    return new RunnableTask(ENV_ENTRY_JNDI_NAME, ENV_ENTRY_VALUE,
        this.getClass().getName());
  }

}
