/*
 * Copyright (c) 2013, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.concurrency.spec.ManagedScheduledExecutorService.inheritedapi_servlet;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import com.sun.ts.tests.concurrency.common.CommonTasks;
import com.sun.ts.tests.concurrency.common.ConcurrencyTestUtils;
import com.sun.ts.tests.concurrency.common.counter.CounterRunnableTask;
import com.sun.ts.tests.concurrency.common.counter.CounterServlet;
import com.sun.ts.tests.concurrency.common.counter.StaticCounter;

import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/testServlet")
public class TestServlet extends CounterServlet {

  protected void setupTest(HttpServletRequest req, HttpServletResponse res)
      throws Exception {
    StaticCounter.reset();
  }

  protected void doTest(HttpServletRequest req, HttpServletResponse res)
      throws Exception {

    res.setContentType("text/plain");
    PrintWriter out = res.getWriter();
    String opName = req.getParameter(ConcurrencyTestUtils.SERVLET_OP_ATTR_NAME);
    ManagedScheduledExecutorService service = ConcurrencyTestUtils
        .getManagedScheduledExecutorService();

    if (ConcurrencyTestUtils.SERVLET_OP_INHERITEDAPI_TESTAPISUBMIT
        .equals(opName)) {
      Future result = service.submit(new CommonTasks.SimpleCallable());
      ConcurrencyTestUtils.waitTillFutureIsDone(result);
      ConcurrencyTestUtils.assertEquals(CommonTasks.SIMPLE_RETURN_STRING,
          result.get());

      result = service.submit(new CommonTasks.SimpleRunnable());
      ConcurrencyTestUtils.waitTillFutureIsDone(result);
      result.get();

      result = service.submit(new CommonTasks.SimpleRunnable(),
          CommonTasks.SIMPLE_RETURN_STRING);
      ConcurrencyTestUtils.waitTillFutureIsDone(result);
      ConcurrencyTestUtils.assertEquals(CommonTasks.SIMPLE_RETURN_STRING,
          result.get());
      out.println(ConcurrencyTestUtils.SERVLET_RETURN_SUCCESS);
    } else if (ConcurrencyTestUtils.SERVLET_OP_INHERITEDAPI_TESTAPIEXECUTE
        .equals(opName)) {
      service.execute(new CounterRunnableTask());
      Thread.sleep(ConcurrencyTestUtils.COMMON_TASK_TIMEOUT);
      ConcurrencyTestUtils.assertEquals(1, StaticCounter.getCount());
      out.println(ConcurrencyTestUtils.SERVLET_RETURN_SUCCESS);
    } else if (ConcurrencyTestUtils.SERVLET_OP_INHERITEDAPI_TESTAPIINVOKEALL
        .equals(opName)) {
      List taskList = new ArrayList();
      taskList.add(new CommonTasks.SimpleArgCallable(1));
      taskList.add(new CommonTasks.SimpleArgCallable(2));
      taskList.add(new CommonTasks.SimpleArgCallable(3));
      List<Future> resultList = service.invokeAll(taskList);
      for (Future each : resultList) {
        ConcurrencyTestUtils.waitTillFutureIsDone(each);
      }
      ConcurrencyTestUtils.assertEquals(1, resultList.get(0).get());
      ConcurrencyTestUtils.assertEquals(2, resultList.get(1).get());
      ConcurrencyTestUtils.assertEquals(3, resultList.get(2).get());
      resultList = service.invokeAll(taskList,
          ConcurrencyTestUtils.COMMON_TASK_TIMEOUT_IN_SECOND, TimeUnit.SECONDS);
      for (Future each : resultList) {
        ConcurrencyTestUtils.waitTillFutureIsDone(each);
      }
      ConcurrencyTestUtils.assertEquals(1, resultList.get(0).get());
      ConcurrencyTestUtils.assertEquals(2, resultList.get(1).get());
      ConcurrencyTestUtils.assertEquals(3, resultList.get(2).get());

      try {
        taskList = new ArrayList();
        taskList.add(new CommonTasks.SimpleCallable(
            ConcurrencyTestUtils.COMMON_TASK_TIMEOUT));
        taskList.add(new CommonTasks.SimpleCallable(
            ConcurrencyTestUtils.COMMON_TASK_TIMEOUT));
        resultList = service.invokeAll(taskList,
            ConcurrencyTestUtils.COMMON_CHECK_INTERVAL_IN_SECOND,
            TimeUnit.SECONDS);
        Thread.sleep(ConcurrencyTestUtils.COMMON_CHECK_INTERVAL);
        for (Future each : resultList) {
          each.get();
        }
      } catch (CancellationException e) {
        out.println(ConcurrencyTestUtils.SERVLET_RETURN_SUCCESS);
        return;
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
      throw new RuntimeException("Task should be cancelled because of timeout");
    } else if (ConcurrencyTestUtils.SERVLET_OP_INHERITEDAPI_TESTAPIINVOKEANY
        .equals(opName)) {
      List taskList = new ArrayList();
      taskList.add(new CommonTasks.SimpleArgCallable(1));
      taskList.add(new CommonTasks.SimpleArgCallable(2));
      taskList.add(new CommonTasks.SimpleArgCallable(3));
      Object result = service.invokeAny(taskList);
      ConcurrencyTestUtils.assertInRange(new Integer[] { 1, 2, 3 }, result);
      result = service.invokeAny(taskList,
          ConcurrencyTestUtils.COMMON_TASK_TIMEOUT_IN_SECOND, TimeUnit.SECONDS);
      ConcurrencyTestUtils.assertInRange(new Integer[] { 1, 2, 3 }, result);

      try {
        taskList = new ArrayList();
        taskList.add(new CommonTasks.SimpleCallable(
            ConcurrencyTestUtils.COMMON_TASK_TIMEOUT));
        taskList.add(new CommonTasks.SimpleCallable(
            ConcurrencyTestUtils.COMMON_TASK_TIMEOUT));
        result = service.invokeAny(taskList,
            ConcurrencyTestUtils.COMMON_CHECK_INTERVAL_IN_SECOND,
            TimeUnit.SECONDS);
      } catch (TimeoutException e) {
        out.println(ConcurrencyTestUtils.SERVLET_RETURN_SUCCESS);
        return;
      } catch (Exception ex) {
        throw new RuntimeException(ex);
      }
      throw new RuntimeException("Task should be cancelled because of timeout");
    } else if (ConcurrencyTestUtils.SERVLET_OP_INHERITEDAPI_TESTAPISCHEDULE
        .equals(opName)) {
      Future result = service.schedule(new CommonTasks.SimpleCallable(),
          ConcurrencyTestUtils.COMMON_CHECK_INTERVAL_IN_SECOND,
          TimeUnit.SECONDS);
      ConcurrencyTestUtils.waitTillFutureIsDone(result);
      ConcurrencyTestUtils.assertEquals(CommonTasks.SIMPLE_RETURN_STRING,
          result.get());

      result = service.schedule(new CommonTasks.SimpleRunnable(),
          ConcurrencyTestUtils.COMMON_CHECK_INTERVAL_IN_SECOND,
          TimeUnit.SECONDS);
      ConcurrencyTestUtils.waitTillFutureIsDone(result);
      ConcurrencyTestUtils.assertEquals(null, result.get());
      out.println(ConcurrencyTestUtils.SERVLET_RETURN_SUCCESS);
    } else if (ConcurrencyTestUtils.SERVLET_OP_INHERITEDAPI_TESTAPISCHEDULEATFIXEDRATE
        .equals(opName)) {
      ScheduledFuture result = null;
      try {
        result = service.scheduleAtFixedRate(new CounterRunnableTask(),
            ConcurrencyTestUtils.COMMON_CHECK_INTERVAL_IN_SECOND,
            ConcurrencyTestUtils.COMMON_CHECK_INTERVAL_IN_SECOND,
            TimeUnit.SECONDS);
        Thread.sleep(ConcurrencyTestUtils.COMMON_TASK_TIMEOUT);
        ConcurrencyTestUtils.asserIntInRange(3, 7, StaticCounter.getCount());
      } catch (Exception e) {
        throw new RuntimeException(e);
      } finally {
        if (result != null) {
          result.cancel(true);
          // Sleep to ensure cancel take effect.
          try {
            Thread.sleep(ConcurrencyTestUtils.COMMON_CHECK_INTERVAL);
          } catch (Exception e) {
          }
        }
      }
      out.println(ConcurrencyTestUtils.SERVLET_RETURN_SUCCESS);
    } else if (ConcurrencyTestUtils.SERVLET_OP_INHERITEDAPI_TESTAPISCHEDULEWITHFIXEDDELAY
        .equals(opName)) {
      ScheduledFuture result = null;
      try {
        result = service.scheduleWithFixedDelay(
            new CounterRunnableTask(ConcurrencyTestUtils.COMMON_CHECK_INTERVAL),
            ConcurrencyTestUtils.COMMON_CHECK_INTERVAL_IN_SECOND,
            ConcurrencyTestUtils.COMMON_CHECK_INTERVAL_IN_SECOND,
            TimeUnit.SECONDS);
        Thread.sleep(ConcurrencyTestUtils.COMMON_TASK_TIMEOUT);
        ConcurrencyTestUtils.asserIntInRange(1, 3, StaticCounter.getCount());
      } catch (Exception e) {
        throw new RuntimeException(e);
      } finally {
        if (result != null) {
          result.cancel(true);
          // Sleep to ensure cancel take effect.
          try {
            Thread.sleep(ConcurrencyTestUtils.COMMON_CHECK_INTERVAL);
          } catch (Exception e) {
          }
        }
      }
      out.println(ConcurrencyTestUtils.SERVLET_RETURN_SUCCESS);
    }
  }

}
