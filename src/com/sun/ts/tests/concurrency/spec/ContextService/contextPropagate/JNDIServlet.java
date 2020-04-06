/*
 * Copyright (c) 2013, 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.concurrency.spec.ContextService.contextPropagate;

import jakarta.ejb.EJB;
import javax.naming.NamingException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@WebServlet("/JNDIServlet")
public class JNDIServlet extends HttpServlet {

  @EJB(lookup = "java:app/ContextPropagate_ejb/ContextPropagateBean")
  private ContextPropagateInterface intf;

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    String action = req.getParameter("action");
    String result = null;
    if ("createProxyInServlet".equals(action)) {
      try {
        result = intf.executeWorker((TestWorkInterface) Util
            .lookupDefaultContextService()
            .createContextualProxy(new TestJNDIRunnableWork(), Runnable.class,
                TestWorkInterface.class, Serializable.class));

      } catch (NamingException e) {
        throw new ServletException(e);
      }
    }

    if ("createProxyInEJB".equals(action)) {
      result = intf.createWorker(
          "com.sun.ts.tests.concurrency.spec.ContextService.contextPropagate.TestJNDIRunnableWork")
          .doSomeWork();
    }

    resp.getWriter().println(result);
  }
}
