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

package com.sun.ts.tests.concurrency.spec.ManagedExecutorService.inheritedapi;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = Constants.COMMON_SERVLET_NAME, urlPatterns = {
    Constants.COMMON_SERVLET_URI })
public class CommonServlet extends HttpServlet {
  // try the inject
  @Resource
  ManagedExecutorService mes;

  /**
   * A basic implementation of the <code>doGet</code> method.
   * 
   * @param req
   *          - <code>HttpServletRequest</code>
   * @param res
   *          - <code>HttpServletResponse</code>
   * @exception ServletException
   *              if an error occurs
   * @exception IOException
   *              if an IO error occurs
   */
  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    invokeTest(req, res);
  }

  /**
   * A basic implementation of the <code>doPost</code> method.
   * 
   * @param req
   *          - <code>HttpServletRequest</code>
   * @param res
   *          - <code>HttpServletResponse</code>
   * @exception ServletException
   *              if an error occurs
   * @exception IOException
   *              if an IO error occurs
   */
  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    invokeTest(req, res);
  }

  private void invokeTest(HttpServletRequest req, HttpServletResponse res) {
    // make sure resource injection is ok.
    if (null != mes && submitTaskTest() && testAtMostOnce()) {
      print(res, Message.SUCCESSMESSAGE);
    } else {
      print(res, Message.FAILMESSAGE);
    }
  }

  private boolean submitTaskTest() {
    return testExecute() && testSubmit() && testInvokeAny() && testInvokeAll();
  }

  private boolean testExecute() {
    Task<?> commonTask = new Task.CommonTask(0);
    mes.execute(commonTask);
    // wait for a while.
    try {
      TimeUnit.SECONDS.sleep(3);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    return commonTask.isRan();
  }

  private boolean testSubmit() {
    Task<?> commonTask = new Task.CommonTask(0);
    Future<?> noRes = mes.submit((Runnable) commonTask);
    try {
      Util.waitForTaskComplete(noRes, 3 * 1000);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return commonTask.isRan();
  }

  private boolean testInvokeAny() {
    boolean passed = false;
    Task.CommonTask commonTask0 = new Task.CommonTask(0);
    Task.CommonTask commonTask1 = new Task.CommonTask(1);
    List<Task.CommonTask> tasks = new ArrayList<Task.CommonTask>();
    tasks.add(commonTask0);
    tasks.add(commonTask1);
    int res = -1;
    try {
      res = mes.invokeAny(tasks);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
    if (tasks.get(res).isRan()) {
      passed = true;
    }
    return passed;
  }

  private boolean testInvokeAll() {
    boolean passed = false;
    Task.CommonTask commonTask0 = new Task.CommonTask(0);
    Task.CommonTask commonTask1 = new Task.CommonTask(1);
    List<Task.CommonTask> tasks = new ArrayList<Task.CommonTask>();
    tasks.add(commonTask0);
    tasks.add(commonTask1);
    List<Future<Integer>> res = null;
    try {
      res = mes.invokeAll(tasks);
      Util.waitForTaskComplete(res.get(0), Constants.MAX_WAIT_TIME);
      Util.waitForTaskComplete(res.get(1), Constants.MAX_WAIT_TIME);
    } catch (Exception e) {
      e.printStackTrace();
    }
    if (commonTask0.isRan() && commonTask1.isRan()) {
      passed = true;
    }
    return passed;
  }

  private boolean testAtMostOnce() {
    Task.CommonTask commonTask = new Task.CommonTask(0);
    Future<?> future = mes.submit((Runnable) commonTask);
    try {
      Util.waitForTaskComplete(future, Constants.MAX_WAIT_TIME);
      // check number.
      return (commonTask.runCount() == 1);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return false;
  }

  private void print(HttpServletResponse res, String msg) {
    PrintWriter pw = null;
    try {
      pw = res.getWriter();
      pw.print(msg);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      pw.close();
    }
  }

}
