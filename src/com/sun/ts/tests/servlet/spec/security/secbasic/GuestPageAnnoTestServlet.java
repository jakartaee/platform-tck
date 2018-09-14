/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.servlet.spec.security.secbasic;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.*;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.HttpConstraint;
import javax.annotation.security.DeclareRoles;

/* 
 * This servlet is the annotation based equivalent of GuestPageTestServlet.
 * As such this declares roles, and specifies that the GET and POST methods
 * can be accessed by all of the declared roles.
 * 
 * This annotation based version of GuestPageTestServlet varies a bit from
 * it because GuestPageTestServlet specifies that GET and POST can be accessed
 * by "Administrator", "Manager", "Employee"  *but* all other http methods
 * are left unprotected and so can be accessed by anyone.
 * This servlet specifies that *all* http methods can be accessed by 
 * "Administrator", "Manager", "Employee" (including GET and POST) and NO
 * http methods are left unprotected.  Thus, this servlet is trying to 
 * be more secure yet it still leaves GET & POST accessible to all declared
 * roles just as is done with GuestPageTestServlet.
 *
 */
@DeclareRoles({ "Administrator", "Manager", "Employee" })
@ServletSecurity(@HttpConstraint(rolesAllowed = { "Administrator", "Manager",
    "Employee" }))
@WebServlet(name = "GuestPageAnnoTest", urlPatterns = { "/GuestPageAnnoTest" })
public class GuestPageAnnoTestServlet extends HttpServlet {

  public void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    out.println("The user principal is: " + request.getUserPrincipal().getName()
        + "<BR>");
  }
}
