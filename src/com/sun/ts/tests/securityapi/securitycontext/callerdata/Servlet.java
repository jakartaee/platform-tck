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

package com.sun.ts.tests.securityapi.securitycontext.callerdata;

import java.io.IOException;
import java.util.List;

import javax.annotation.security.DeclareRoles;
import javax.inject.Inject;
import javax.security.enterprise.CallerPrincipal;
import javax.security.enterprise.SecurityContext;
import javax.security.enterprise.authentication.mechanism.http.BasicAuthenticationMechanismDefinition;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Test Servlet that prints out the name of the authenticated caller and whether
 * this caller is in any of the roles {Administrator, Manager, Employee}
 *
 */

@BasicAuthenticationMechanismDefinition(realmName = "test realm")

@DeclareRoles({ "Administrator", "Manager", "Employee" })
@WebServlet("/servlet")
@ServletSecurity(@HttpConstraint(rolesAllowed = "Administrator"))
public class Servlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Inject
  private SecurityContext securityContext;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    response.getWriter().write("This is a servlet \n");

    String contextName = null;
    if (securityContext.getCallerPrincipal() != null) {
      contextName = securityContext.getCallerPrincipal().getName();
    }

    response.getWriter().write("context username: " + contextName + "\n");

    response.getWriter().write("context user has role \"Administrator\": "
        + securityContext.isCallerInRole("Administrator") + "\n");
    response.getWriter().write("context user has role \"Manager\": "
        + securityContext.isCallerInRole("Manager") + "\n");
    response.getWriter().write("context user has role \"Employee\": "
        + securityContext.isCallerInRole("Employee") + "\n");

    response.getWriter().write(
        "isCallerInRole(Administrator) result same with request.isUserInRole(Administrator) result : "
            + (securityContext.isCallerInRole("Administrator") == request
                .isUserInRole("Administrator")));
    response.getWriter().write(
        "isCallerInRole(Manager) result same with request.isUserInRole(Manager) result : "
            + (securityContext.isCallerInRole("Manager") == request
                .isUserInRole("Manager")));
    response.getWriter().write(
        "isCallerInRole(Employee) result same with request.isUserInRole(Employee) result : "
            + (securityContext.isCallerInRole("Employee") == request
                .isUserInRole("Employee")));
  }

}
