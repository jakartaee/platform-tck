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

package com.sun.ts.tests.concurrency.spec.ManagedExecutorService.managed_servlet.forbiddenapi;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(Constants.SERVLET_TEST_URL)
public class TestServlet extends HttpServlet {
  @Resource
  private ManagedExecutorService mes;

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    doPost(req, res);
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    String operation = req.getParameter(Constants.OP_NAME);

    if (Constants.OP_AWAITTERMINATION.equals(operation)) {
      testAwaitTermination(res);
    } else if (Constants.OP_ISSHUTDOWN.equals(operation)) {
      testIsShutdown(res);
    } else if (Constants.OP_ISTERMINATED.equals(operation)) {
      testIsTerminated(res);
    } else if (Constants.OP_SHUTDOWN.equals(operation)) {
      testShutdown(res);
    } else if (Constants.OP_SHUTDOWNNOW.equals(operation)) {
      testShutdownNow(res);
    }

  }

  public void testAwaitTermination(HttpServletResponse res) {
    boolean passed = false;
    try {
      mes.awaitTermination(10, TimeUnit.SECONDS);
    } catch (IllegalStateException e) { // what expected.
      passed = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("+++ kick 1");
    returnMsg(res, passed);
  }

  public void testIsShutdown(HttpServletResponse res) {
    boolean passed = false;

    try {
      mes.isShutdown();
    } catch (IllegalStateException e) { // what expected
      passed = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("+++ kick 2");
    returnMsg(res, passed);
  }

  public void testIsTerminated(HttpServletResponse res) {
    boolean passed = false;
    try {
      mes.isTerminated();
    } catch (IllegalStateException e) { // what expected
      passed = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("+++ kick 3");
    returnMsg(res, passed);
  }

  public void testShutdown(HttpServletResponse res) {
    boolean passed = false;
    try {
      mes.shutdown();
    } catch (IllegalStateException e) { // what expected
      passed = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("+++ kick 4");
    returnMsg(res, passed);
  }

  public void testShutdownNow(HttpServletResponse res) {
    boolean passed = false;
    try {
      mes.shutdownNow();
    } catch (IllegalStateException e) { // what expected
      passed = true;
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("+++ kick 5");
    returnMsg(res, passed);
  }

  private void returnMsg(HttpServletResponse res, boolean passed) {
    if (passed) {
      print(res, Constants.SUCCESSMESSAGE);
    } else {
      print(res, Constants.FAILMESSAGE);
    }
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
