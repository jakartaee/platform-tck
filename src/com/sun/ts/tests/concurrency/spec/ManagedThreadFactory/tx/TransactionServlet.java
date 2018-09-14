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

package com.sun.ts.tests.concurrency.spec.ManagedThreadFactory.tx;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import javax.annotation.Resource;
import javax.enterprise.concurrent.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = Constants.TX_SERVLET_NAME, urlPatterns = {
    Constants.TX_SERVLET_URI })
public class TransactionServlet extends HttpServlet {

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
    Map<String, String> params = null;
    try {
      params = formatMap(req);
    } catch (UnsupportedEncodingException e1) {
      e1.printStackTrace();
    }

    boolean passed = false;
    String param = params.get(Constants.PARAM_COMMIT);
    if (Constants.PARAM_VALUE_CANCEL.equals(param)) {
      passed = cancelTest(res, params);
    } else {
      boolean isCommit = Boolean.parseBoolean(param);
      ManagedThreadFactory factory = Util.getManagedThreadFactory();
      Thread thread = factory.newThread(new TransactedTask(isCommit,
          params.get(Constants.USERNAME), params.get(Constants.PASSWORD),
          params.get(Constants.SQL_TEMPLATE)));
      thread.start();
      try {
        Util.waitTillThreadFinish(thread);
      } catch (Exception e) {
        e.printStackTrace();
        print(res, Message.FAILMESSAGE);
        return;
      }
      passed = true;
    }
    if (passed) {
      print(res, Message.SUCCESSMESSAGE);
    } else {
      print(res, Message.FAILMESSAGE);
    }
  }

  private boolean cancelTest(HttpServletResponse res,
      Map<String, String> params) {
    String username = params.get(Constants.USERNAME);
    String password = params.get(Constants.PASSWORD);
    String tablename = params.get(Constants.TABLE_P);
    String sqlTemplate = params.get(Constants.SQL_TEMPLATE);
    int originTableCount = Util.getCount(tablename, username, password);
    CancelledTransactedTask cancelledTask = new CancelledTransactedTask(
        username, password, sqlTemplate);
    ManagedThreadFactory factory = Util.getManagedThreadFactory();
    Thread thread = factory.newThread(cancelledTask);
    thread.start();
    // then cancel it after transaction begin and
    Util.waitForTransactionBegan(cancelledTask, 3000, 100);
    // before it commit.
    cancelledTask.cancelTask();
    // continue to run if possible.
    cancelledTask.resume();
    int afterTransacted = Util.getCount(tablename, username, password);
    return originTableCount == afterTransacted;
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

  private Map<String, String> formatMap(HttpServletRequest req)
      throws UnsupportedEncodingException {
    Map<String, String> props = new HashMap<String, String>();
    Enumeration<String> params = req.getParameterNames();
    while (params.hasMoreElements()) {
      String name = (String) params.nextElement();
      String value = req.getParameter(name);
      props.put(name, URLDecoder.decode(value, "utf8"));
    }
    return props;
  }
}
