/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 *  $Id$
 */
package com.sun.ts.tests.servlet.ee.spec.security.runAs;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.ejb.EJB;

public class ServletOne extends HttpServlet {
  @EJB
  SecTest secTest;

  public void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    String webUserPrincipalName = null;
    String ejbUserPrincipalName = null;
    PrintWriter out = response.getWriter();

    if (request.getUserPrincipal() != null) {
      webUserPrincipalName = request.getUserPrincipal().getName();
    }
    out.println("Servlet accessed as User :" + webUserPrincipalName + "\n");

    try {
      ejbUserPrincipalName = secTest.getCallerPrincipalName();
      out.println("EJB accessed as User :" + ejbUserPrincipalName);
    } catch (Exception e) {
      e.printStackTrace();
      out.println(e.getMessage());
    }

  }
}
