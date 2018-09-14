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

package com.sun.ts.tests.concurrency.common.counter;

import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sun.ts.tests.concurrency.common.*;

public class CounterServlet extends HttpServlet {

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

      String opName = req
          .getParameter(ConcurrencyTestUtils.SERVLET_OP_ATTR_NAME);
      if (ConcurrencyTestUtils.SERVLET_OP_COUNTER_GETCOUNT.equals(opName)) {
        out.println(StaticCounter.getCount());
      } else if (ConcurrencyTestUtils.SERVLET_OP_COUNTER_INC.equals(opName)) {
        StaticCounter.inc();
        out.println(ConcurrencyTestUtils.SERVLET_RETURN_SUCCESS);
      } else if (ConcurrencyTestUtils.SERVLET_OP_COUNTER_RESET.equals(opName)) {
        StaticCounter.reset();
        out.println(ConcurrencyTestUtils.SERVLET_RETURN_SUCCESS);
      } else {
        setupTest(req, res);
        doTest(req, res);
      }
    } catch (Exception e) {
      if (out != null) {
        out.println(ConcurrencyTestUtils.SERVLET_RETURN_FAIL);
        out.println(e);
      }
    } finally {
      if (null != out) {
        out.close();
      }
    }
  }

  protected void doTest(HttpServletRequest req, HttpServletResponse res)
      throws Exception {
  }

  protected void setupTest(HttpServletRequest req, HttpServletResponse res)
      throws Exception {
  }

}
