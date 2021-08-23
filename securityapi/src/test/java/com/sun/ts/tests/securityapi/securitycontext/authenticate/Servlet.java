/*
 * Copyright (c) 2017, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.securityapi.securitycontext.authenticate;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.annotation.security.DeclareRoles;
import jakarta.inject.Inject;
import jakarta.security.enterprise.AuthenticationStatus;
import jakarta.security.enterprise.SecurityContext;
import jakarta.security.enterprise.authentication.mechanism.http.AuthenticationParameters;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.Password;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Test Servlet that prints out the name of the authenticated caller and whether
 * this caller is in any of the roles {Administrator, Manager, Employee}
 *
 */

@DeclareRoles({ "Administrator", "Manager", "Employee" })
@WebServlet("/servlet")
public class Servlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Inject
  private SecurityContext securityContext;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    PrintWriter writer = response.getWriter();
    HttpSession session = request.getSession();

    // String username = "tom";
    // String password = "secret1";
    String username = request.getParameter("name");
    String password = request.getParameter("password");

    if (username != null && password != null) {
      Credential credential = new UsernamePasswordCredential(username,
          new Password(password));
      AuthenticationStatus status = securityContext.authenticate(request,
          response,
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
    if (securityContext.getCallerPrincipal() != null) {
      contextName = securityContext.getCallerPrincipal().getName();
    }

    writer.write("context username: " + contextName + "\n");

    writer.write("context user has role \"Administrator\": "
        + securityContext.isCallerInRole("Administrator") + "\n");
    writer.write("context user has role \"Manager\": "
        + securityContext.isCallerInRole("Manager") + "\n");
    writer.write("context user has role \"Employee\": "
        + securityContext.isCallerInRole("Employee") + "\n");
  }

}
