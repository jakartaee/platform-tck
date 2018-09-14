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

package com.sun.ts.tests.servlet.spec.rdspecialchar;

import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TestServlet extends HttpTCKServlet {

  public void querySemicolonInclude(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    String path = "/include/IncludedServlet?querySemicolonIncludeQuebec;libre";
    RequestDispatcher rd = getServletContext().getRequestDispatcher(path);

    if (rd == null)
      pw.println("Null RequestDispatcher got for path=" + path);
    else
      rd.include(request, response);
  }

  public void querySemicolonForward(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    String path = "/include/IncludedServlet?querySemicolonForwardQuebec;libre";
    RequestDispatcher rd = getServletContext().getRequestDispatcher(path);

    if (rd == null)
      pw.println("Null RequestDispatcher got for path=" + path);
    else
      rd.forward(request, response);
  }

}
