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

package com.sun.ts.tests.concurrency.spec.ManagedScheduledExecutorService.security;

import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.ejb.EJB;
import com.sun.ts.tests.concurrency.api.common.*;
import javax.enterprise.concurrent.*;
import java.util.concurrent.*;
import javax.naming.*;
import com.sun.ts.lib.util.TestUtil;

@WebServlet("/testServlet")
public class TestServlet extends HttpServlet {

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    doPost(req, res);
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    PrintWriter out = null;
    req.login("javajoe", "javajoe");

    try {
      res.setContentType("text/plain");
      out = res.getWriter();

      InitialContext context = new InitialContext();
      ManagedScheduledExecutorService executorService = (ManagedScheduledExecutorService) context
          .lookup(Util.SCHEDULED_MANAGED_EXECUTOR_SVC_JNDI_NAME);
      ScheduledFuture future = executorService.schedule(new SecurityTestTask(),
          new CommonTriggers.OnceTrigger());

      Object result = Util.waitForTaskComplete(future,
          Util.COMMON_TASK_TIMEOUT_IN_SECOND);
      Util.assertEquals(SecurityTestRemote.MANAGERMETHOD1_RETURN_STR, result);
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

}
