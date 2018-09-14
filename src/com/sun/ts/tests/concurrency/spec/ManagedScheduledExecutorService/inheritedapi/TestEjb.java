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

package com.sun.ts.tests.concurrency.spec.ManagedScheduledExecutorService.inheritedapi;

import javax.ejb.Stateless;
import javax.enterprise.concurrent.*;
import java.util.concurrent.*;
import javax.naming.*;
import com.sun.ts.tests.concurrency.api.common.*;
import com.sun.ts.tests.concurrency.api.common.counter.*;
import javax.ejb.EJB;
import java.util.*;

@Stateless
public class TestEjb implements TestEjbRemote {

  @EJB(beanName = "CounterSingleton")
  private CounterRemote counter;

  private String COUNTER_SINGLETON_JNDI = "java:global/inheritedapi/counter_ejb/CounterSingleton";

  private ManagedScheduledExecutorService getService() {
    try {
      InitialContext context = new InitialContext();
      ManagedScheduledExecutorService executorService = (ManagedScheduledExecutorService) context
          .lookup(Util.SCHEDULED_MANAGED_EXECUTOR_SVC_JNDI_NAME);
      return executorService;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void testApiSubmit() {
    try {
      Future result = getService().submit(new CommonTasks.SimpleCallable());
      Util.waitTillFutureIsDone(result);
      Util.assertEquals(CommonTasks.SIMPLE_RETURN_STRING, result.get());

      result = getService().submit(new CommonTasks.SimpleRunnable());
      Util.waitTillFutureIsDone(result);
      result.get();

      result = getService().submit(new CommonTasks.SimpleRunnable(),
          CommonTasks.SIMPLE_RETURN_STRING);
      Util.waitTillFutureIsDone(result);
      Util.assertEquals(CommonTasks.SIMPLE_RETURN_STRING, result.get());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void testApiExecute() {
    try {
      getService().execute(new CounterRunnableTask(COUNTER_SINGLETON_JNDI));
      Thread.sleep(Util.COMMON_TASK_TIMEOUT);
      Util.assertEquals(1, counter.getCount());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void testApiInvokeAll() {
    try {
      List taskList = new ArrayList();
      taskList.add(new CommonTasks.SimpleArgCallable(1));
      taskList.add(new CommonTasks.SimpleArgCallable(2));
      taskList.add(new CommonTasks.SimpleArgCallable(3));
      List<Future> resultList = getService().invokeAll(taskList);
      for (Future each : resultList) {
        Util.waitTillFutureIsDone(each);
      }
      Util.assertEquals(1, resultList.get(0).get());
      Util.assertEquals(2, resultList.get(1).get());
      Util.assertEquals(3, resultList.get(2).get());

      resultList = getService().invokeAll(taskList,
          Util.COMMON_TASK_TIMEOUT_IN_SECOND, TimeUnit.SECONDS);
      for (Future each : resultList) {
        Util.waitTillFutureIsDone(each);
      }
      Util.assertEquals(1, resultList.get(0).get());
      Util.assertEquals(2, resultList.get(1).get());
      Util.assertEquals(3, resultList.get(2).get());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    try {
      List taskList = new ArrayList();
      taskList.add(new CommonTasks.SimpleCallable(Util.COMMON_TASK_TIMEOUT));
      taskList.add(new CommonTasks.SimpleCallable(Util.COMMON_TASK_TIMEOUT));
      List<Future> resultList = getService().invokeAll(taskList,
          Util.COMMON_CHECK_INTERVAL_IN_SECOND, TimeUnit.SECONDS);
      Thread.sleep(Util.COMMON_CHECK_INTERVAL);
      for (Future each : resultList) {
        each.get();
      }
    } catch (CancellationException e) {
      return;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    throw new RuntimeException("Task should be cancelled because of timeout");
  }

  public void testApiInvokeAny() {
    try {
      List taskList = new ArrayList();
      taskList.add(new CommonTasks.SimpleArgCallable(1));
      taskList.add(new CommonTasks.SimpleArgCallable(2));
      taskList.add(new CommonTasks.SimpleArgCallable(3));
      Object result = getService().invokeAny(taskList);
      Util.assertInRange(new Integer[] { 1, 2, 3 }, result);

      result = getService().invokeAny(taskList,
          Util.COMMON_TASK_TIMEOUT_IN_SECOND, TimeUnit.SECONDS);
      Util.assertInRange(new Integer[] { 1, 2, 3 }, result);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    try {
      List taskList = new ArrayList();
      taskList.add(new CommonTasks.SimpleCallable(Util.COMMON_TASK_TIMEOUT));
      taskList.add(new CommonTasks.SimpleCallable(Util.COMMON_TASK_TIMEOUT));
      Object result = getService().invokeAny(taskList,
          Util.COMMON_CHECK_INTERVAL_IN_SECOND, TimeUnit.SECONDS);
    } catch (TimeoutException e) {
      return;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
    throw new RuntimeException("Task should be cancelled because of timeout");
  }

  public void testApiSchedule() {
    try {
      Future result = getService().schedule(new CommonTasks.SimpleCallable(),
          Util.COMMON_CHECK_INTERVAL_IN_SECOND, TimeUnit.SECONDS);
      Util.waitTillFutureIsDone(result);
      Util.assertEquals(CommonTasks.SIMPLE_RETURN_STRING, result.get());

      result = getService().schedule(new CommonTasks.SimpleRunnable(),
          Util.COMMON_CHECK_INTERVAL_IN_SECOND, TimeUnit.SECONDS);
      Util.waitTillFutureIsDone(result);
      Util.assertEquals(null, result.get());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public void testApiScheduleAtFixedRate() {
    ScheduledFuture result = null;
    try {
      result = getService().scheduleAtFixedRate(
          new CounterRunnableTask(COUNTER_SINGLETON_JNDI),
          Util.COMMON_CHECK_INTERVAL_IN_SECOND,
          Util.COMMON_CHECK_INTERVAL_IN_SECOND, TimeUnit.SECONDS);
      Thread.sleep(Util.COMMON_TASK_TIMEOUT);
      Util.asserIntInRange(3, 7, counter.getCount());
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (result != null) {
        result.cancel(true);
        // Sleep to ensure cancel take effect.
        try {
          Thread.sleep(Util.COMMON_CHECK_INTERVAL);
        } catch (Exception e) {
        }
      }
    }
  }

  public void testApiScheduleWithFixedDelay() {
    ScheduledFuture result = null;
    try {
      result = getService().scheduleWithFixedDelay(
          new CounterRunnableTask(COUNTER_SINGLETON_JNDI,
              Util.COMMON_CHECK_INTERVAL),
          Util.COMMON_CHECK_INTERVAL_IN_SECOND,
          Util.COMMON_CHECK_INTERVAL_IN_SECOND, TimeUnit.SECONDS);
      Thread.sleep(Util.COMMON_TASK_TIMEOUT);
      Util.asserIntInRange(1, 3, counter.getCount());
    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      if (result != null) {
        result.cancel(true);
        // Sleep to ensure cancel take effect.
        try {
          Thread.sleep(Util.COMMON_CHECK_INTERVAL);
        } catch (Exception e) {
        }
      }
    }
  }

}
