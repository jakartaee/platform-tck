/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.connector.resourceDefs.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.*;

import javax.resource.AdministeredObjectDefinition;
import javax.resource.AdministeredObjectDefinitions;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.HttpConstraint;
import javax.annotation.security.DeclareRoles;

import com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory;
import com.sun.ts.lib.util.*;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.naming.InitialContext;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;

/*
 * This is testing AdministeredObjectDefinition in the different environment 
 * scopes (e.g. app-level, module-leve, component-level, and global.
 *
 */

@DeclareRoles({ "Administrator", "Manager", "Employee" })
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {
    "Administrator" }), httpMethodConstraints = {
        @HttpMethodConstraint(value = "GET", rolesAllowed = "Administrator"),
        @HttpMethodConstraint(value = "POST", rolesAllowed = "Administrator") })
@WebServlet(name = "AODTestServlet", urlPatterns = { "/AODTestServlet" })
@AdministeredObjectDefinitions({
    @AdministeredObjectDefinition(name = "java:app/env/CTSAdminObjectForAppScope", description = "application scoped AdminObjectDefinition", interfaceName = "javax.jms.Queue", className = "com.sun.ts.tests.common.connector.embedded.adapter1.CRDAdminObject", resourceAdapter = "#whitebox-rd"),

    @AdministeredObjectDefinition(name = "java:comp/env/CTSAdminObjectForCompScope", description = "component scoped AdminObjectDefinition", interfaceName = "javax.jms.Queue", className = "com.sun.ts.tests.common.connector.embedded.adapter1.CRDAdminObject", resourceAdapter = "#whitebox-rd"),

    @AdministeredObjectDefinition(name = "java:module/env/CTSAdminObjectForModuleScope", description = "module scoped AdminObjectDefinition", interfaceName = "javax.jms.Queue", className = "com.sun.ts.tests.common.connector.embedded.adapter1.CRDAdminObject", resourceAdapter = "#whitebox-rd"),

    @AdministeredObjectDefinition(name = "java:global/env/CTSAdminObjectForGlobalScope", description = "globally scoped AdminObjectDefinition", interfaceName = "javax.jms.Queue", className = "com.sun.ts.tests.common.connector.embedded.adapter1.CRDAdminObject", resourceAdapter = "#whitebox-rd") })
public class AODTestServlet extends HttpServlet {
  private String servletAppContext = null;

  private String testMethod = null;

  private String RARJndiScope = null;

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    debug("in AODTestServlet.doGet()");
    getPropsAndParams(request, response);
    doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    debug("in AODTestServlet.doPost()");
    getPropsAndParams(request, response);
    doTests(request, response);
  }

  private void doTests(HttpServletRequest request,
      HttpServletResponse response) {

    debug("in AODTestServlet.doTests()");
    PrintWriter out = null;
    try {
      out = response.getWriter();
    } catch (Exception ex) {
      debug("got exception in AODTestServlet");
      ex.printStackTrace();
    }

    // get some common props
    getPropsAndParams(request, response);

    if (testMethod.equals("ValidateGlobalAdminObj")) {
      debug("AODTestServlet.doTests(): testMethod == ValidateGlobalAdminObj");
      validateGlobalAdminObj(request, response);

    } else if (testMethod.equals("ValidateAppAdminObj")) {
      debug("AODTestServlet.doTests(): testMethod == ValidateAppAdminObj");
      validateAppAdminObj(request, response);

    } else if (testMethod.equals("ValidateCompAdminObj")) {
      debug("AODTestServlet.doTests(): testMethod == ValidateCompAdminObj");
      validateCompAdminObj(request, response);

    } else if (testMethod.equals("ValidateModuleAdminObj")) {
      debug("AODTestServlet.doTests(): testMethod == ValidateModuleAdminObj");
      validateModuleAdminObj(request, response);
    }

  }

  private void getPropsAndParams(HttpServletRequest req,
      HttpServletResponse response) {

    // set testMethod
    testMethod = req.getParameter("method.under.test");
    RARJndiScope = req.getParameter("rar.jndi.scope");

    debug("AODTestServlet.getPropsAndParams():  testMethod = " + testMethod);
    debug(
        "AODTestServlet.getPropsAndParams():  RARJndiScope = " + RARJndiScope);

    return;
  }

  public void validateGlobalAdminObj(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      String jndiName = RARJndiScope;
      PrintWriter out = response.getWriter();

      debug("checking jndiName = " + jndiName);
      if (validateConnectorResource(jndiName, true)) {
        send_output(out, "ValidateGlobalAdminObj() passed.");
      } else {
        send_output(out,
            "validateConnectorResource() failed for jndiName: " + jndiName);
      }

    } catch (Exception ex) {
      System.out.println("AODTestServlet->ValidateGlobalAdminObj() failed");
      ex.printStackTrace();
    }
  }

  public void validateAppAdminObj(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      String jndiName = RARJndiScope;
      PrintWriter out = response.getWriter();

      debug("checking jndiName = " + jndiName);
      if (validateConnectorResource(jndiName, true)) {
        send_output(out, "ValidateAppAdminObj() passed.");
      } else {
        send_output(out,
            "validateAppAdminObj() failed for jndiName: " + jndiName);
      }

    } catch (Exception ex) {
      System.out.println("AODTestServlet->ValidateAppAdminObj() failed");
      ex.printStackTrace();
    }
  }

  public void validateCompAdminObj(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      String jndiName = RARJndiScope;
      PrintWriter out = response.getWriter();

      debug("checking jndiName = " + jndiName);
      if (validateConnectorResource(jndiName, true)) {
        send_output(out, "ValidateCompAdminObj() passed.");
      } else {
        send_output(out,
            "validateCompAdminObj() failed for jndiName: " + jndiName);
      }

    } catch (Exception ex) {
      System.out.println("AODTestServlet->ValidateCompAdminObj() failed");
      ex.printStackTrace();
    }
  }

  public void validateModuleAdminObj(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      String jndiName = RARJndiScope;
      PrintWriter out = response.getWriter();

      debug("checking jndiName = " + jndiName);
      if (validateConnectorResource(jndiName, true)) {
        send_output(out, "ValidateModuleAdminObj() passed.");
      } else {
        send_output(out,
            "validateModuleAdminObj() failed for jndiName: " + jndiName);
      }

    } catch (Exception ex) {
      System.out.println("AODTestServlet->ValidateModuleAdminObj() failed");
      ex.printStackTrace();
    }
  }

  /*
   * returns true if success, else false.
   */
  private boolean validateConnectorResource(String jndiName,
      boolean expectSuccess) {
    TSConnection c = null;
    boolean rval = false;

    try {

      debug("validateConnectorResource():  calling new TSNamingContext()");
      TSNamingContext ic = new TSNamingContext();

      debug("Doing lookup of jndiName = " + jndiName);
      Object ds = ic.lookup(jndiName);
      debug(
          "validateConnectorResource(): Successfully did lookup of jndiName = "
              + jndiName);

      rval = true;
    } catch (Exception e) {
      debug("Fail to access connector resource: " + jndiName);
      e.printStackTrace();
    } finally {
      try {
        if (c != null) {
          c.close();
        }
      } catch (Exception e) {
      }
    }

    return rval;
  }

  public void send_output(PrintWriter out, String str) {
    if (out != null) {
      out.println(str);
      out.flush();
      debug(str);
    } else {
      print_err("ERROR, Null PrintWriter:  can not properly send back message: "
          + str);
    }
  }

  public void print_err(String str) {
    System.err.println(str);
    debug(str);
  }

  public void debug(String str) {
    TestUtil.logMsg(str);
    System.out.println(str);
  }

}
