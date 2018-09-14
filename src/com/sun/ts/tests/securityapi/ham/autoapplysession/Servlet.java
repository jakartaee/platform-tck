/*
 * Copyright (c) 2017, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.securityapi.ham.autoapplysession;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.security.DeclareRoles;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/servlet")
@DeclareRoles({ "Administrator", "Manager", "Employee" })
@ServletSecurity(@HttpConstraint(rolesAllowed = "Administrator"))
public class Servlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    if (request.getParameter("logout") != null) {
      request.logout();
      request.getSession().invalidate();
    }

    PrintWriter out = response.getWriter();

    if (request.getUserPrincipal() != null) {
      out.println("The user principal is: "
          + request.getUserPrincipal().getName() + "<BR>");
    }

    // Surround these with !'s so they are easier to search for.
    // (i.e. we can search for !true! or !false!)
    out.println("isUserInRole(\"Administrator\"): !"
        + request.isUserInRole("Administrator") + "!<BR>");
    out.println("isUserInRole(\"Manager\"): !" + request.isUserInRole("Manager")
        + "!<BR>");
    out.println("isUserInRole(\"Employee\"): !"
        + request.isUserInRole("Employee") + "!<BR>");
  }
}
