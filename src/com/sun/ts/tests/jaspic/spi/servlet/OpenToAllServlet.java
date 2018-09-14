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
import java.security.Principal;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.annotation.security.DeclareRoles;

import com.sun.ts.tests.jaspic.tssv.util.JASPICData;
import com.sun.ts.tests.jaspic.tssv.util.IdUtil;

@ServletSecurity
@WebServlet(name = "OpenToAllServlet", urlPatterns = { "/OpenToAllServlet" })
public class OpenToAllServlet extends HttpServlet {
  private String logFileLocation;

  private String servletAppContext = null;

  private String providerConfigFileLocation;

  private String testMethod = null;

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    doPost(request, response);
    debug("Enterred OpenToAllServlet->doGet()");
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    debug("Enterred OpenToAllServlet->doPost()");

    doTests(request, response);

  }

  public void doTests(HttpServletRequest request,
      HttpServletResponse response) {

    PrintWriter out = null;
    try {
      out = response.getWriter();
    } catch (Exception ex) {
      debug("got exception in ModTestServlet");
      ex.printStackTrace();
    }

    // get some common props that are passed into our servlet request
    getPropsAndParams(request, response);

    if (testMethod.equals("testGPCWithNoRequiredAuth")) {
      TestGPCWithNoRequiredAuth(request, response);
    } else if (testMethod.equals("testAuthenAfterLogout")) {
      TestAuthenAfterLogout(request, response);
    } else {
      // if here, it most likely means that we ran a test from within
      // Client.java which
      // invoked this servlet with the only intent being to check if access
      // could be
      // made. Some tests only care that they can access this servlet and do NOT
      // need to run any particular testMethod in here.
      debug("WARNING:  OpenToAllServlet.doTests()  testMethod not recognized: "
          + testMethod);
    }
  }

  public void TestAuthenAfterLogout(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();
      // out.println("Enterred OpenToAllServlet->TestAuthenAfterLogout()");
      System.out.println("In OpenToAllServlet->TestAuthenAfterLogout()");

      Principal pp = request.getUserPrincipal();
      if (pp != null) {
        // ohoh - when no authN required for this servlet, we are detecting
        // that some authentication occurred -OR- that we are pre-logged in
        // either way, thats a problem so force a logout.
        out.println(
            "OpenToAllServlet:  we should not be authenticated so forcing logout");
        request.logout();
      } else {
        out.println(
            "OpenToAllServlet:  pp == null so we were not pre-logged in.");
        out.println(
            "no need to force a logout since we are (correctly) not logged in.");
      }
    } catch (Exception ex) {
      System.out.println("OpenToAllServlet->TestAuthenAfterLogout() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This will get called after we have authenticated with our user j2ee in role
   * Administrator. When we get into this test method, a call to isUserInRole
   * better return true. if we have not authenticated, we expect to get false.
   */
  public void TestGPCWithNoRequiredAuth(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();
      out.println("Enterred OpenToAllServlet->TestGPCWithNoRequiredAuth()");
      System.out.println("In OpenToAllServlet->TestGPCWithNoRequiredAuth()");

      // after successful login, we should see true value for isUserInRole
      boolean bval = request.isUserInRole("Administrator");

      if (bval == true) {
        debug(out, "isUserInRole() returns true.");
        debug(
            "OpenToAllServlet->testAuthenIsUserInRole() isUserInRole() returns true");
      } else {
        debug(
            "OpenToAllServlet->TestGPCWithNoRequiredAuth() isUserInRole() returns false");
      }
      out.println("OpenToAllServlet->testAuthenIsUserInRole() passed");

    } catch (Exception ex) {
      System.out
          .println("OpenToAllServlet->TestGPCWithNoRequiredAuth() failed");
      ex.printStackTrace();
    }

    return;
  }

  // this pulls out some params that are passed in on our servlet req
  // that is made from within the client code (e.g. Client.java).
  private void getPropsAndParams(HttpServletRequest req,
      HttpServletResponse response) {

    // set logfile location
    logFileLocation = req.getParameter("log.file.location");
    if ((logFileLocation != null)
        && (-1 < logFileLocation.indexOf(JASPICData.DEFAULT_LOG_FILE))) {
      // if here, we have logfile location value which contains
      // JASPICData.DEFAULT_LOG_FILE
      debug("logFileLocation already set");
    } else if (logFileLocation != null) {
      debug("logFileLocation NOT set completely");
      System.setProperty("log.file.location", logFileLocation);
    } else {
      debug("ModTestServlet: logFileLocation null");
    }
    debug("logFileLocation = " + logFileLocation);

    // set provider config file
    providerConfigFileLocation = req
        .getParameter("provider.configuration.file");
    debug("TS Provider ConfigFile = " + providerConfigFileLocation);
    if (providerConfigFileLocation == null) {
      debug("ERROR:  getPropsAndParams(): providerConfigFileLocation = null");
    } else {
      debug("getPropsAndParams(): providerConfigFileLocation = "
          + providerConfigFileLocation);
    }

    // set testMethod
    testMethod = req.getParameter("method.under.test");

    // Reusing some old code that is passing vendorACFClass in - so while
    // we dont currently use it in this servlet, - it IS being passed in
    // so just note it is here.
    // String vendorACFClass = req.getParameter("vendor.authconfig.factory");

    servletAppContext = IdUtil.getAppContextId(JASPICData.LAYER_SERVLET);

    return;
  }

  public void debug(PrintWriter out, String str) {
    out.println(str);
    System.out.println(str);
  }

  public void debug(String str) {
    System.out.println(str);
  }

}
