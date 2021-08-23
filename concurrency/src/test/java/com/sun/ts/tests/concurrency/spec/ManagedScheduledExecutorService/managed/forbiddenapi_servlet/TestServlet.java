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

package com.sun.ts.tests.concurrency.spec.ManagedScheduledExecutorService.managed.forbiddenapi_servlet;

import java.io.PrintWriter;
import java.util.concurrent.TimeUnit;

import com.sun.ts.tests.concurrency.common.ConcurrencyTestUtils;
import com.sun.ts.tests.concurrency.common.counter.CounterServlet;
import com.sun.ts.tests.concurrency.common.counter.StaticCounter;

import jakarta.enterprise.concurrent.ManagedScheduledExecutorService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/testServlet")
public class TestServlet extends CounterServlet {

  private static final String DIDNOT_CATCH_ILLEGALSTATEEXCEPTION = "IllegalStateException expected";

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

    if (ConcurrencyTestUtils.SERVLET_OP_FORBIDDENAPI_TESTAWAITTERMINATION
        .equals(opName)) {
      try {
        service.awaitTermination(10, TimeUnit.SECONDS);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      } catch (IllegalStateException e) {
        out.println(ConcurrencyTestUtils.SERVLET_RETURN_SUCCESS);
        return;
      }
      throw new RuntimeException(DIDNOT_CATCH_ILLEGALSTATEEXCEPTION);
    } else if (ConcurrencyTestUtils.SERVLET_OP_FORBIDDENAPI_TESTISSHUTDOWN
        .equals(opName)) {
      try {
        service.isShutdown();
      } catch (IllegalStateException e) {
        out.println(ConcurrencyTestUtils.SERVLET_RETURN_SUCCESS);
        return;
      }
      throw new RuntimeException(DIDNOT_CATCH_ILLEGALSTATEEXCEPTION);
    } else if (ConcurrencyTestUtils.SERVLET_OP_FORBIDDENAPI_TESTISTERMINATED
        .equals(opName)) {
      try {
        service.isTerminated();
      } catch (IllegalStateException e) {
        out.println(ConcurrencyTestUtils.SERVLET_RETURN_SUCCESS);
        return;
      }
      throw new RuntimeException(DIDNOT_CATCH_ILLEGALSTATEEXCEPTION);
    } else if (ConcurrencyTestUtils.SERVLET_OP_FORBIDDENAPI_TESTSHUTDOWN
        .equals(opName)) {
      try {
        service.shutdown();
      } catch (IllegalStateException e) {
        out.println(ConcurrencyTestUtils.SERVLET_RETURN_SUCCESS);
        return;
      }
      throw new RuntimeException(DIDNOT_CATCH_ILLEGALSTATEEXCEPTION);
    } else if (ConcurrencyTestUtils.SERVLET_OP_FORBIDDENAPI_TESTSHUTDOWNNOW
        .equals(opName)) {
      try {
        service.shutdownNow();
      } catch (IllegalStateException e) {
        out.println(ConcurrencyTestUtils.SERVLET_RETURN_SUCCESS);
        return;
      }
      throw new RuntimeException(DIDNOT_CATCH_ILLEGALSTATEEXCEPTION);
    }
  }

}
