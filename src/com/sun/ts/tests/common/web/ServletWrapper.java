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

package com.sun.ts.tests.common.web;

import java.util.Properties;
import java.io.PrintWriter;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.tests.common.web.WebUtil;

/**
 * Provide a testing framework for a Servlet test.
 *
 * This class is intended to be extended by the actual Servlet test class that
 * will define one or more test methods. This is why this class is tagged
 * "abstract".
 *
 * This class shield the final Servlet class from Servlet life cycle and
 * specific testing framework.
 *
 * @see com.sun.ts.tests.common.web.WebServer
 */
public abstract class ServletWrapper extends HttpServlet {

  /** Name of property used to send back test result to client */
  public static final String RESULT_PROP = "ctsWebTestResult";

  public static final String TEST_NAME_PROP = "ctsWebTestName";

  protected static TSNamingContext nctx = null;

  private Properties servletProps = null;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);

    try {
      WebUtil.logTrace("[ServletWrapper] init()");
      WebUtil.logTrace("[ServletWrapper] Getting naming ctx...");
      nctx = new TSNamingContext();
    } catch (Exception e) {
      WebUtil.logErr("[ServletWrapper] Cannot get Naming ctx", e);
      throw new ServletException("Cannot initialize Servlet");
    }
  }

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    PrintWriter out = null;

    try {
      WebUtil.logTrace("[ServletWrapper] doGet()");
      res.setContentType("text/plain");
      out = res.getWriter();
      servletProps.list(out);
    } catch (Exception e) {
      WebUtil.logErr("[ServletWrapper] Exception in doGet()", e);
      if (null != out) {
        e.printStackTrace(out);
      }
    } finally {
      if (null != out) {
        out.close();
      }
    }
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    WebUtil.logTrace("[ServletWrapper] doPost()");
    servletProps = WebUtil.executeTest(this, nctx, req);
    doGet(req, res);
    servletProps = null;
  }

}
