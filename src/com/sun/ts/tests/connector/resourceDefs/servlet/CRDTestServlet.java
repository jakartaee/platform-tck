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

package com.sun.ts.tests.connector.resourceDefs.servlet;

import java.io.*;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.HttpConstraint;
import javax.annotation.security.DeclareRoles;
import javax.naming.InitialContext;

import com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory;
import com.sun.ts.lib.util.*;
import com.sun.ts.tests.common.connector.whitebox.TSDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSEISDataSource;
import com.sun.ts.tests.common.connector.whitebox.TSConnection;

import javax.resource.ConnectionFactoryDefinition;
import javax.resource.ConnectionFactoryDefinitions;
import javax.resource.cci.Connection;
import javax.resource.cci.ConnectionFactory;
import javax.resource.spi.TransactionSupport;

/*
 * In order for these tests to pass, we must have whitebox-tx.rar configured & deployed.
 * This is a connection resource which is typically done as part of config.vi, but since
 * it is a new anno, we want to do it here.  But like the connection resources, this also
 * will not work unless the corresponding RA for this resource is first deployed.
 * (note: whitebox-tx.rar should be deployed as part of initial config)
 *
 * Also, be sure to specify the correct className that matches our connection factory
 * type (which for cts rar's is com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory)
 *
 * WARNING:  XXXX:  Current issue: 
 *           @ConnectionFactoryDefinition seems to currently (1/9/2013) depend
 *           on proprietary properties to tie the definition to a rar.
 *
 */

//
//  XXXX:  try using transactionSupport=TransactionSupport.TransactionSupportLevel.XATransaction),
//

@DeclareRoles({ "Administrator", "Manager", "Employee" })
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {
    "Administrator" }), httpMethodConstraints = {
        @HttpMethodConstraint(value = "GET", rolesAllowed = "Administrator"),
        @HttpMethodConstraint(value = "POST", rolesAllowed = "Administrator") })
@WebServlet(name = "CRDTestServlet", urlPatterns = { "/CRDTestServlet" })
@ConnectionFactoryDefinitions({
    @ConnectionFactoryDefinition(name = "java:app/env/CRDTestServlet_App_ConnectorResource", description = "application scoped connector resource definition", interfaceName = "com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory", resourceAdapter = "whitebox-tx", transactionSupport = TransactionSupport.TransactionSupportLevel.NoTransaction),

    @ConnectionFactoryDefinition(name = "java:comp/env/CRDTestServlet_Comp_ConnectorResource", description = "component scoped connector resource definition", interfaceName = "com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory", resourceAdapter = "whitebox-tx", transactionSupport = TransactionSupport.TransactionSupportLevel.LocalTransaction),

    @ConnectionFactoryDefinition(name = "java:module/env/CRDTestServlet_Module_ConnectorResource", description = "module scoped connector resource definition", interfaceName = "com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory", resourceAdapter = "whitebox-tx", transactionSupport = TransactionSupport.TransactionSupportLevel.NoTransaction),

    @ConnectionFactoryDefinition(name = "java:global/env/CRDTestServlet_Global_ConnectorResource", description = "globally scoped connector resource definition", interfaceName = "com.sun.ts.tests.common.connector.whitebox.TSConnectionFactory", resourceAdapter = "whitebox-xa", transactionSupport = TransactionSupport.TransactionSupportLevel.XATransaction) })
public class CRDTestServlet extends HttpServlet {
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
    try {
      String jndiName = RARJndiScope;
      PrintWriter out = response.getWriter();

      debug("checking jndiName = " + jndiName);
      if (validateConnectorResource(jndiName, true)) {
        send_output(out,
            "CRDTestServlet->ValidateGlobalResourceDef() passed for jndiName="
                + jndiName);
      } else {
        send_output(out,
            "validateConnectorResource() failed for jndiName: " + jndiName);
      }

    } catch (Exception ex) {
      System.out.println("CRDTestServlet->ValidateGlobalResourceDef() failed");
      ex.printStackTrace();
    }
  }

  public void validateAppResourceDef(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      String jndiName = RARJndiScope;
      PrintWriter out = response.getWriter();

      debug("checking jndiName = " + jndiName);
      if (validateConnectorResource(jndiName, true)) {
        send_output(out,
            "CRDTestServlet->ValidateAppResourceDef() passed for jndiName="
                + jndiName);
      } else {
        send_output(out,
            "validateAppResourceDef() failed for jndiName: " + jndiName);
      }

    } catch (Exception ex) {
      System.out.println("CRDTestServlet->ValidateAppResourceDef() failed");
      ex.printStackTrace();
    }
  }

  public void validateCompResourceDef(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      String jndiName = RARJndiScope;
      PrintWriter out = response.getWriter();

      debug("checking jndiName = " + jndiName);
      if (validateConnectorResource(jndiName, true)) {
        send_output(out,
            "CRDTestServlet->ValidateCompResourceDef() passed for jndiName="
                + jndiName);
      } else {
        send_output(out,
            "validateCompResourceDef() failed for jndiName: " + jndiName);
      }

    } catch (Exception ex) {
      System.out.println("CRDTestServlet->ValidateCompResourceDef() failed");
      ex.printStackTrace();
    }
  }

  public void validateModuleResourceDef(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      String jndiName = RARJndiScope;
      PrintWriter out = response.getWriter();

      debug("checking jndiName = " + jndiName);
      if (validateConnectorResource(jndiName, true)) {
        send_output(out,
            "CRDTestServlet->ValidateModuleResourceDef() passed for jndiName="
                + jndiName);
      } else {
        send_output(out,
            "validateModuleResourceDef() failed for jndiName: " + jndiName);
      }

    } catch (Exception ex) {
      System.out.println("CRDTestServlet->ValidateModuleResourceDef() failed");
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
      TSDataSource ds = (TSDataSource) (ic.lookup(jndiName));
      debug(
          "validateConnectorResource(): Successfully did lookup of jndiName = "
              + jndiName);

      rval = true;
    } catch (Exception e) {
      debug("Fail to access connector resource: " + jndiName);
      e.printStackTrace();
    } finally {
      debug("finally:  Fail to access connector resource: " + jndiName);
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
