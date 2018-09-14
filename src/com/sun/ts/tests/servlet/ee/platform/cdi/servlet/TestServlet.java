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

package com.sun.ts.tests.servlet.ee.platform.cdi.servlet;

import com.sun.ts.tests.servlet.ee.platform.cdi.TCKTestBean;
import java.io.IOException;
import java.io.PrintWriter;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "TCKTestServlet", urlPatterns = { "/TCKTestServletURL" })
public class TestServlet extends HttpServlet {

  @Inject
  TCKTestBean ttb;

  @Inject
  BeanManager bm;

  public void service(HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException {
    PrintWriter pw = response.getWriter();
    if (ttb == null) {
      throw new ServletException(
          "Injection of TCKTestBean in TestServlet failed");
    }
    if (bm == null) {
      throw new ServletException(
          "Injection of BeanManager in TestServlet failed");
    }
    pw.print("Test PASSED from TestServlet");
  }
}
