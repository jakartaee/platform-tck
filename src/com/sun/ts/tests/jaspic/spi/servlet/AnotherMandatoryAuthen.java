/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.HttpConstraint;
import javax.annotation.security.DeclareRoles;

@DeclareRoles({ "Administrator", "Manager", "Employee" })
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {
    "Manager" }), httpMethodConstraints = {
        @HttpMethodConstraint(value = "GET", rolesAllowed = "Manager"),
        @HttpMethodConstraint(value = "POST", rolesAllowed = "Manager") })
@WebServlet(name = "AnotherMandatoryAuthen", urlPatterns = {
    "/AnotherMandatoryAuthen" })
public class AnotherMandatoryAuthen extends HttpServlet {

  /**
  * 
  */
  private static final long serialVersionUID = 1L;

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doPost(request, response);
    printOut("Enterred AnotherMandatoryAuthen->doGet()", response);
  }

  @SuppressWarnings("unused")
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    System.out.println("In AnotherMandatoryAuthen->doPost()");

    printOut("Enterred AnotherMandatoryAuthen->doPost()", response);
    printOut("request.getServletPath() = " + request.getServletPath(),
        response);
    printOut("request.getPathInfo() = " + request.getPathInfo(), response);
    printOut("request.getMethod() = " + request.getMethod(), response);

  }

  @SuppressWarnings("unused")
  public void service(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    System.out.println("In AnotherMandatoryAuthen->service()");
    printOut("Enterred AnotherMandatoryAuthen->service()", response);
  }

  private void printOut(String str, HttpServletResponse response) {
    PrintWriter out = null;
    try {
      out = response.getWriter();
    } catch (Exception ex) {
      ex.printStackTrace();
      out = null;
    }
    if (out != null) {
      out.println(str);
    } else {
      System.out.println(str);
    }
  }

}
