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
 * @(#)CSIv2Servlet.java	1.13 03/05/16
 */

package com.sun.ts.tests.interop.csiv2.common;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;

import javax.servlet.http.*;
import javax.servlet.*;
import java.io.*;
import java.util.*;

/**
 * CSIv2 Servlet
 */
public class CSIv2Servlet extends HttpServlet
    implements CSIv2TestLogicInterface {

  private CSIv2TestLogicImpl csiv2TestLogicImpl;

  private Properties harnessProps = null;

  public CSIv2Servlet() {
    csiv2TestLogicImpl = new CSIv2TestLogicImpl();
  }

  public void init() {
  }

  public void destroy() {
  }

  public String getServletInfo() {
    return "Servlet for CSIv2 Testing.";
  }

  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    Properties p = new Properties();
    resp.setContentType("text/html");
    PrintWriter out = resp.getWriter();
    try {
      TestUtil.logMsg("CSIv2Servlet.doGet()");
      TestUtil
          .logMsg("CSIv2Servlet caller principal : " + req.getUserPrincipal());
      ArrayList chain = new ArrayList();
      chain.add(harnessProps.getProperty("CHAIN"));
      invoke(chain, harnessProps);
      p.setProperty("TESTRESULT", "pass"); // Allways pass?
      p.list(out);
    } catch (Exception e) {
      TestUtil.logErr("doGet Exception: " + e);
      TestUtil.printStackTrace(e);
      p.setProperty("TESTRESULT", "pass"); // Allways pass?
      p.setProperty("EXCEPTION_MESSAGE", e.getMessage());
      p.list(out);
    }
    out.close();
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    harnessProps = new Properties();
    Enumeration pEnum = req.getParameterNames();
    while (pEnum.hasMoreElements()) {
      String name = (String) pEnum.nextElement();
      String value = req.getParameter(name);
      harnessProps.setProperty(name, value);
    }
    try {
      TestUtil.init(harnessProps);
      TestUtil.logTrace("Initialize remote logging");
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new ServletException("unable to initialize remote logging");
    }
    doGet(req, res);
    harnessProps = null;
  }

  public void service(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    doPost(req, res);

  }

  /**
   * @see CSIv2TestLogicInterface for javadocs.
   */
  public void invoke(ArrayList chain, Properties p) {
    TestUtil.logTrace("CSIv2Servlet.invoke()");
    csiv2TestLogicImpl.invoke(chain, p);
  }
}
