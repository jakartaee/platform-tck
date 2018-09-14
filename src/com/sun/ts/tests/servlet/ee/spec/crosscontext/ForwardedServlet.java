/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */

package com.sun.ts.tests.servlet.ee.spec.crosscontext;

import javax.servlet.http.HttpServlet;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ForwardedServlet extends HttpServlet {

  private static final String TEST_HEADER = "testname";

  private static final Class[] TEST_ARGS = { HttpServletRequest.class,
      HttpServletResponse.class };

  public void service(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    String test = (String) req.getParameter(TEST_HEADER);
    if (test == null)
      test = (String) req.getAttribute(TEST_HEADER);

    try {
      Method method = this.getClass().getMethod(test, TEST_ARGS);
      method.invoke(this, new Object[] { req, res });
    } catch (InvocationTargetException ite) {
      throw new ServletException(ite.getTargetException());
    } catch (NoSuchMethodException nsme) {
      throw new ServletException("Test: " + test + " does not exist");
    } catch (Throwable t) {
      throw new ServletException("Error executing test: " + test, t);
    }
  }

  public void session(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    PrintWriter pw = response.getWriter();
    String aname = "crosscontext_cts";
    String status = "FAIL";

    HttpSession ses = request.getSession();

    Object o = ses.getAttribute(aname);

    if (o != null) {
      if (o instanceof String) {
        String attr = (String) o;
        pw.println("attribute " + aname + " set with incorrect value=" + attr);
      } else {
        pw.println("attribute " + aname + " set to non-String type");
      }
      pw.println("Forward Test FAILED");
    } else {
      pw.println("attribute " + aname + " not set");
      pw.println("Forward Test PASSED");
    }
  }
}
