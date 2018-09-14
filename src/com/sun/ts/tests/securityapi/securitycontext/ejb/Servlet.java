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

package com.sun.ts.tests.securityapi.securitycontext.ejb;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.inject.Inject;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@DeclareRoles({ "Administrator", "Manager", "Employee" })
@WebServlet("/servlet")
public class Servlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Inject
  private TestEJB testEJB;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    PrintWriter writer = response.getWriter();
    HttpSession session = request.getSession();

    String username = request.getParameter("name");
    String password = request.getParameter("password");

    if (username != null && password != null) {
      Credential credential = new UsernamePasswordCredential(username,
          new Password(password));
      AuthenticationStatus status = testEJB.authenticate(request, response,
          AuthenticationParameters.withParams().credential(credential));

      response.getWriter()
          .write("Authenticated with status: " + status.name() + "\n");

      if (status.equals(AuthenticationStatus.SUCCESS)) {
        response.getWriter().write("Authentication successed");

      } else if (status.equals(AuthenticationStatus.SEND_FAILURE)) {
        response.getWriter().write("Authentication failed");

        return;
      }
    }

    String contextName = null;
    if (testEJB.getCallerPrincipal() != null) {
      contextName = testEJB.getCallerPrincipal().getName();
    }

    writer.write("context username: " + contextName + "\n");

    writer.write("context user has role \"Administrator\": "
        + testEJB.isCallerInRole("Administrator") + "\n");
    writer.write("context user has role \"Manager\": "
        + testEJB.isCallerInRole("Manager") + "\n");
    writer.write("context user has role \"Employee\": "
        + testEJB.isCallerInRole("Employee") + "\n");
  }

}
