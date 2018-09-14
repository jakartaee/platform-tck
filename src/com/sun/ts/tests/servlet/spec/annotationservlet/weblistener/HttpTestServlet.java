/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id:$
 */
package com.sun.ts.tests.servlet.spec.annotationservlet.weblistener;

import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class HttpTestServlet extends HttpTCKServlet {

  public void httpSessionListenerTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    HttpSession session = request.getSession(true);
    session.invalidate();

    response.getWriter().println(getServletContext().getAttribute("HSList"));
    getServletContext().removeAttribute("SRList");
    getServletContext().removeAttribute("SRAList");
    getServletContext().removeAttribute("HSList");
  }
}
