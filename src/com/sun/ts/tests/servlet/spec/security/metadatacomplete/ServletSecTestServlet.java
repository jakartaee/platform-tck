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

package com.sun.ts.tests.servlet.spec.security.metadatacomplete;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.annotation.security.DeclareRoles;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.*;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.HttpConstraint;

/*
 * The DD is using metadata-complete=true which means that the security
 * settings in the DD will be used and the annotations security settings in
 * this servlet will be ignored.
 *
 * The annotations in this servlet specify that all http methods can be accessed
 * by role=Administrator but GET and POST are set to be denied all access.
 * REMEMBER:  these annotations will get ignored by DD since DD
 * has metadata-complete=true!
 * 
 * The DD will take precedence and sets GET accessible by role=Administrator(j2ee)
 * and sets POST accessible by role=Manager(javajoe)
 */
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {
    "Administrator" }), httpMethodConstraints = {
        @HttpMethodConstraint(value = "POST", emptyRoleSemantic = EmptyRoleSemantic.DENY),
        @HttpMethodConstraint(value = "GET", emptyRoleSemantic = EmptyRoleSemantic.DENY) })
@WebServlet(name = "ServletSecTestLogicalName", urlPatterns = {
    "/ServletSecTest" })
public class ServletSecTestServlet extends HttpServlet {

  public void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    System.out
        .println("Inside  ServletSecTestServlet.service()  ....." + "<BR>");
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, java.io.IOException {

    PrintWriter out = response.getWriter();
    System.out
        .println("Inside  ServletSecTestServlet.doTrace() ....." + "<BR>");
  }

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, java.io.IOException {

    PrintWriter out = response.getWriter();
    System.out.println("Inside  ServletSecTestServlet.doGet() ....." + "<BR>");

    if ((request != null) && (request.getUserPrincipal() != null)) {
      out.println("The user principal is: "
          + request.getUserPrincipal().getName() + "<BR>");
    } else {
      out.println("The user principal is: Null." + "<BR>");
    }
  }

}
