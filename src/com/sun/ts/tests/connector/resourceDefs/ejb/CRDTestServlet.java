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

package com.sun.ts.tests.connector.resourceDefs.ejb;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.HttpConstraint;
import javax.annotation.security.DeclareRoles;

import com.sun.ts.lib.util.*;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;

import javax.resource.cci.Connection;
import javax.resource.spi.TransactionSupport;

import javax.ejb.EJB;
import javax.ejb.EJBException;

/*
 * In order for these tests to pass, we must have whitebox-tx.rar configured & deployed.
 * This is a connection resource which is typically done as part of config.vi, but since
 * it is a new anno, we want to do it here.  But like the connection resources, this also
 * will not work unless the corresponding RA for this resource is first deployed.
 * (note: whitebox-tx.rar should be deployed as part of initial config)
 *
 */

@DeclareRoles({ "Administrator", "Manager", "Employee" })
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {
    "Administrator" }), httpMethodConstraints = {
        @HttpMethodConstraint(value = "GET", rolesAllowed = "Administrator"),
        @HttpMethodConstraint(value = "POST", rolesAllowed = "Administrator") })
@WebServlet(name = "CRDTestServlet", urlPatterns = { "/CRDTestServlet" })
public class CRDTestServlet extends HttpServlet {
  @EJB
  private ITestStatelessEjb testStatelessEjb;

  private String servletAppContext = null;

  private String testMethod = null;

  private String RARJndiScope = null;

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    debug("in CRDTestServlet.doGet()");
    getPropsAndParams(request, response);
    doPost(request, response);
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    debug("in CRDTestServlet.doPost()");
    getPropsAndParams(request, response);
    doTests(request, response);
  }

  private void doTests(HttpServletRequest request,
      HttpServletResponse response) {

    debug("in CRDTestServlet.doTests()");
    PrintWriter out = null;
    try {
      out = response.getWriter();
    } catch (Exception ex) {
      debug("got exception in CRDTestServlet");
      ex.printStackTrace();
    }

    // get some common props
    getPropsAndParams(request, response);

    if (testMethod.equals("ValidateGlobalResourceDef")) {
      debug(
          "CRDTestServlet.doTests(): testMethod == ValidateGlobalResourceDef");
      validateGlobalResourceDef(request, response);

    } else if (testMethod.equals("ValidateAppResourceDef")) {
      debug("CRDTestServlet.doTests(): testMethod == ValidateAppResourceDef");
      validateAppResourceDef(request, response);

    } else if (testMethod.equals("ValidateCompResourceDef")) {
      debug("CRDTestServlet.doTests(): testMethod == ValidateCompResourceDef");
      validateCompResourceDef(request, response);

    } else if (testMethod.equals("ValidateModuleResourceDef")) {
      debug(
          "CRDTestServlet.doTests(): testMethod == ValidateModuleResourceDef");
      validateModuleResourceDef(request, response);
    }

  }

  private void getPropsAndParams(HttpServletRequest req,
      HttpServletResponse response) {

    // set testMethod
    testMethod = req.getParameter("method.under.test");
    RARJndiScope = req.getParameter("rar.jndi.scope");

    debug("CRDTestServlet.getPropsAndParams():  testMethod = " + testMethod);
    debug(
        "CRDTestServlet.getPropsAndParams():  RARJndiScope = " + RARJndiScope);

    return;
  }

  public void validateGlobalResourceDef(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out;

    try {
      out = response.getWriter();

      System.out.println(
          "TestServlet->validateGlobalResourceDef()  calling testStatefullEjb.validateGlobalResourceDef()");
      if (testStatelessEjb == null) {
        send_output(out,
            "ERROR:  test will fail since testStatelessEjb == null");
      }

      boolean bval = testStatelessEjb.validateConnectorResource(RARJndiScope);
      if (bval == true) {
        send_output(out, "SUCCESS:  validateGlobalResourceDef passed.");
      } else {
        send_output(out,
            "FAILURE:  validateGlobalResourceDef had unexpected exception.");
      }
    } catch (Exception ex) {
      System.out.println("CRDTestServlet->validateGlobalResourceDef() failed");
      ex.printStackTrace();
      System.out.println(
          "FAILURE:  validateGlobalResourceDef had unexpected exception.");
    }
  }

  public void validateAppResourceDef(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out;
    try {
      out = response.getWriter();

      if (testStatelessEjb == null) {
        send_output(out,
            "ERROR:  test will fail since testStatelessEjb == null");
      }

      boolean bval = testStatelessEjb.validateConnectorResource(RARJndiScope);
      if (bval == true) {
        send_output(out, "SUCCESS:  validateAppResourceDef passed.");
      } else {
        send_output(out,
            "FAILURE:  validateAppResourceDef had unexpected exception.");
      }
    } catch (Exception ex) {
      System.out.println("CRDTestServlet->validateAppResourceDef() failed");
      ex.printStackTrace();
      System.out.println(
          "FAILURE:  validateAppResourceDef had unexpected exception.");
    }
  }

  public void validateCompResourceDef(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out;
    try {
      out = response.getWriter();

      if (testStatelessEjb == null) {
        send_output(out,
            "ERROR:  test will fail since testStatelessEjb == null");
      }

      boolean bval = testStatelessEjb.validateConnectorResource(RARJndiScope);
      if (bval == true) {
        send_output(out, "SUCCESS:  validateCompResourceDef passed.");
      } else {
        send_output(out,
            "FAILURE:  validateCompResourceDef had unexpected exception.");
      }
    } catch (Exception ex) {
      System.out.println("CRDTestServlet->validateCompResourceDef() failed");
      ex.printStackTrace();
      System.out.println(
          "FAILURE:  validateCompResourceDef had unexpected exception.");
    }
  }

  public void validateModuleResourceDef(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out;
    try {
      out = response.getWriter();

      if (testStatelessEjb == null) {
        send_output(out,
            "ERROR:  test will fail since testStatelessEjb == null");
      }

      boolean bval = testStatelessEjb.validateConnectorResource(RARJndiScope);
      if (bval == true) {
        send_output(out, "SUCCESS:  validateModuleResourceDef passed.");
      } else {
        send_output(out,
            "FAILURE:  validateModuleResourceDef had unexpected exception.");
      }
    } catch (Exception ex) {
      System.out.println("CRDTestServlet->validateModuleResourceDef() failed");
      ex.printStackTrace();
      System.out.println(
          "FAILURE:  validateModuleResourceDef had unexpected exception.");
    }
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
