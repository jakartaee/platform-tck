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

package com.sun.ts.tests.securityapi.ham.form;

import java.io.IOException;
import java.io.PrintWriter;

import javax.annotation.security.DeclareRoles;
import javax.security.enterprise.authentication.mechanism.http.FormAuthenticationMechanismDefinition;
import javax.security.enterprise.authentication.mechanism.http.LoginToContinue;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@FormAuthenticationMechanismDefinition(loginToContinue = @LoginToContinue(loginPage = "/form-login-servlet", errorPage = "/form-login-error-servlet"))

@WebServlet("/servlet")
@DeclareRoles({ "Administrator", "Manager", "Employee" })
@ServletSecurity(@HttpConstraint(rolesAllowed = "Administrator"))
public class FormServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    PrintWriter out = response.getWriter();

    out.println("The user principal is: " + request.getUserPrincipal().getName()
        + "<BR>");
    out.println("getRemoteUser(): " + request.getRemoteUser() + "<BR>");
    out.println("getAuthType(): " + request.getAuthType() + "<BR>");

    // Surround these with !'s so they are easier to search for.
    // (i.e. we can search for !true! or !false!)
    out.println("isUserInRole(\"Administrator\"): !"
        + request.isUserInRole("Administrator") + "!<BR>");
    out.println("isUserInRole(\"Manager\"): !" + request.isUserInRole("Manager")
        + "!<BR>");
    out.println("isUserInRole(\"Employee\"): !"
        + request.isUserInRole("Employee") + "!<BR>");
  }

  /*
   * @Override public void doPost(HttpServletRequest request,
   * HttpServletResponse response) throws ServletException, IOException { if
   * ("true".equals(request.getParameter("logout"))) { request.logout();
   * request.getSession().invalidate(); }
   * 
   * doGet(request, response); }
   */

}
