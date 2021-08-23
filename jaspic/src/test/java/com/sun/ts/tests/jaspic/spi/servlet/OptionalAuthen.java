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

package com.sun.ts.tests.jaspic.spi.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import jakarta.annotation.security.DeclareRoles;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HttpMethodConstraint;
import jakarta.servlet.annotation.ServletSecurity;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@DeclareRoles({ "Administrator", "Manager", "Employee" })
@ServletSecurity(httpMethodConstraints = { @HttpMethodConstraint(value = "GET"),
    @HttpMethodConstraint(value = "POST") })
@WebServlet(urlPatterns = { "/OptionalAuthen",
    "/ModuleAuthStatusThrowExNoDispatch" })
public class OptionalAuthen extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    doPost(request, response);
    out.println("Enterred OptionalAuthen->doGet()");
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    System.out.println("In OptionalAuthen->doPost()");

    PrintWriter out = response.getWriter();
    out.println("Enterred OptionalAuthen->doPost()");

    out.println("request.getServletPath() = " + request.getServletPath());
    out.println("request.getPathInfo() = " + request.getPathInfo());
    out.println("request.getMethod() = " + request.getMethod());
  }

  public void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    System.out.println("In OptionalAuthen->service()");
    PrintWriter out = response.getWriter();
    out.println("Enterred OptionalAuthen->service()");
  }

}
