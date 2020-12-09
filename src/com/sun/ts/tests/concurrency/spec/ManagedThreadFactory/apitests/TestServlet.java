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

package com.sun.ts.tests.concurrency.spec.ManagedThreadFactory.apitests;

import java.io.IOException;
import java.io.PrintWriter;

import javax.naming.InitialContext;

import com.sun.ts.tests.concurrency.api.common.Util;

import jakarta.annotation.Resource;
import jakarta.enterprise.concurrent.ManageableThread;
import jakarta.enterprise.concurrent.ManagedThreadFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/testServlet")
public class TestServlet extends HttpServlet {

  @Resource(lookup = "java:comp/DefaultManagedThreadFactory")
  private ManagedThreadFactory factory;

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    doPost(req, res);
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    PrintWriter out = null;

    try {
      res.setContentType("text/plain");
      out = res.getWriter();

      InitialContext context = new InitialContext();

      String opName = req.getParameter(Client.SERVLET_OP_ATTR_NAME);
      if (Client.SERVLET_OP_INTERRUPTTHREADAPITEST.equals(opName)) {
        CounterRunnable task = new CounterRunnable();
        Thread thread = factory.newThread(task);
        thread.start();
        thread.interrupt();
        Util.waitTillThreadFinish(thread);

        Util.assertEquals(0, task.getCount());
      } else {
        CounterRunnable task = new CounterRunnable();
        Thread thread = factory.newThread(task);
        if (!(thread instanceof ManageableThread)) {
          throw new RuntimeException(
              "The thread returned by ManagedThreadFactory should be instance of ManageableThread.");
        }
      }

      out.println(Util.SERVLET_RETURN_SUCCESS);
    } catch (Exception e) {
      if (out != null) {
        out.println(Util.SERVLET_RETURN_FAIL);
        out.println(e);
      }
    } finally {
      if (null != out) {
        out.close();
      }
    }
  }

  public static class CounterRunnable implements Runnable {
    private volatile int count = 0;

    public int getCount() {
      return count;
    }

    public void run() {
      try {
        Thread.sleep(Util.COMMON_CHECK_INTERVAL);
        count++;
      } catch (InterruptedException ignore) {
        return;
      }
    }
  }
}
