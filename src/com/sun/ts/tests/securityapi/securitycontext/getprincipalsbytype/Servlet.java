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

package com.sun.ts.tests.securityapi.securitycontext.getprincipalsbytype;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

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
@ServletSecurity(@HttpConstraint(rolesAllowed = "Administrator"))
public class Servlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  @Inject
  private SecurityContext securityContext;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    PrintWriter writer = response.getWriter();

    String contextName = null;
    if (securityContext.getCallerPrincipal() != null) {
      contextName = securityContext.getCallerPrincipal().getName();
    }

    writer.write("context username: " + contextName + "\n");

    Set<TestPrincipal> principalSet = securityContext
        .getPrincipalsByType(TestPrincipal.class);

    writer.write("PrincipalsSet size should be one: "
        + (principalSet.size() == 1) + "\n");
    for (TestPrincipal principal : principalSet) {
      writer.write("PrincipalsSet contains correct principal: "
          + principal.getName().equalsIgnoreCase("tom") + "\n");
    }
  }

}
