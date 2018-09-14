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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.servlet.spec.errorpage;

import com.sun.ts.tests.servlet.common.servlets.HttpTCKServlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TestServlet extends HttpTCKServlet {

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  public void destroy() {
    super.destroy();
  }

  // ------------------------------------------------- Test Methods

  public void htmlErrorPageTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IllegalAccessException {
    throw new IllegalAccessException("error page invoked");
  }

  public void servletErrorPageTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, InstantiationException {
    throw new IllegalStateException("error page invoked");
  }

  public void statusCodeErrorPageTest(HttpServletRequest req,
      HttpServletResponse res) throws ServletException, IOException {
    res.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED, "error page invoked");
  }

  public void heirarchyErrorMatchTest(HttpServletRequest req,
      HttpServletResponse res)
      throws ServletException, IllegalThreadStateException {
    throw new IllegalThreadStateException("error page invoked");
  }

}// ErrorPageTestServlet
