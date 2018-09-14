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
import java.util.Properties;
import java.util.Enumeration;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.HttpConstraint;
import javax.annotation.security.DeclareRoles;

import com.sun.ts.tests.jaspic.tssv.util.JASPICData;
import com.sun.ts.tests.jaspic.tssv.util.IdUtil;

@DeclareRoles({ "Administrator", "Manager", "Employee" })
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {
    "Administrator" }), httpMethodConstraints = {
        @HttpMethodConstraint(value = "GET", rolesAllowed = "Administrator"),
        @HttpMethodConstraint(value = "POST", rolesAllowed = "Administrator") })
@WebServlet(name = "AuthStatusMandatorySuccess", urlPatterns = {
    "/AuthStatusMandatorySuccess" })
public class AuthStatusMandatorySuccess extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private String logFileLocation;

  private String servletAppContext = null;

  private String providerConfigFileLocation;

  private String vendorACFClass;

  private String testMethod = null;

  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    doPost(request, response);
    out.println("Enterred AuthStatusMandatorySuccess->doGet()");
  }

  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    System.out.println("In AuthStatusMandatorySuccess->doPost()");
    doTests(request, response);
  }

  public void doTests(HttpServletRequest request,
      HttpServletResponse response) {

    System.out.println("In AuthStatusMandatorySuccess->doTests()");

    PrintWriter out = null;
    try {
      out = response.getWriter();

      // get some common props
      getPropsAndParams(request, response);

      if (testMethod == null) {

        out.println("request.getServletPath() = " + request.getServletPath());
        out.println("request.getPathInfo() = " + request.getPathInfo());
        out.println("request.getMethod() = " + request.getMethod());

      } else if (testMethod.equals("testSecRespCalledAfterSvcInvoc")) {
        _testSecRespCalledAfterSvcInvoc(request, response);
      }
    } catch (Exception ex) {
      System.out.println("WrapperServlet->testRequestWrapper() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This should be satisfying assertion JASPIC:SPEC:108.
   */
  public void _testSecRespCalledAfterSvcInvoc(HttpServletRequest request,
      HttpServletResponse response) {
    try {
      PrintWriter out = response.getWriter();

      out.println(
          "Enterred AuthStatusMandatorySuccess->_testSecRespCalledAfterSvcInvoc()");
      System.out.println(
          "In AuthStatusMandatorySuccess->_testSecRespCalledAfterSvcInvoc()");

      // see if a cts proprietary requestAttribute was set in the secureResponse
      // call and if so, we have a problem. The secure response should be called
      // AFTER this servlet invocation thus we should not see this attribute set
      // yet. If set, it means our secureResponse was called BEFOR the service
      // invocation and thats violation of jaspic 1.1. spec (section 3.8.3.2)
      // also
      // see https://java.net/jira/browse/JASPIC_SPEC-9
      String wasSecureResponseInvokedYet = (String) request
          .getAttribute("secureRespCalled");
      if ((wasSecureResponseInvokedYet != null)
          && (wasSecureResponseInvokedYet.equalsIgnoreCase("true"))) {
        // error - the "secureRespCalled" attribute was set and that should
        // only occur in secureResponse which indicates a problem since the
        // secureResponse should not get called before service invocation.
        System.out
            .println("ERROR - secureResponse called before service invocation");
        out.println("testSecRespCalledAfterSvcInvoc() failed");
        return;
      } else if (wasSecureResponseInvokedYet == null) {
        // This is good and validates assertion: JASPIC:SPEC:108
        // The attribue was NOT set in secureResponse, so it must not
        // have been called before the service invocation of this servlet
        System.out.println(
            "_testSecRespCalledAfterSvcInvoc() secureResponse NOT called before service invocation - which is correct!");
        out.println("testSecRespCalledAfterSvcInvoc() passed");
      }

      String wasValidateRequestInvokedYet = (String) request
          .getAttribute("validateReqCalled");
      if ((wasValidateRequestInvokedYet == null)
          || (!wasValidateRequestInvokedYet.equalsIgnoreCase("true"))) {
        // ERROR - the only reason we should be here is if there was a Multi-msg
        // dialog
        // (per spec 3.8.3) but that should NOT be the case thus we should NOT
        // get in here as caller identity should've been established in
        // validateRequest already.
        System.out.println(
            "ERROR - validateRequest NOT called before service invocation");
        out.println("testSecRespCalledAfterSvcInvoc() failed");
        return;
      } else {
        // good - caller identity was established in validateRequest already,
        // which
        // is why we correctly see "validateReqCalled" request attribute was
        // set.
        System.out.println(
            "_testSecRespCalledAfterSvcInvoc() validateRequest was correctly called BEFORE service invocation");
        out.println("testSecRespCalledAfterSvcInvoc() passed");
      }

      out.flush();
    } catch (Exception ex) {
      System.out.println(
          "AuthStatusMandatorySuccess->_testSecRespCalledAfterSvcInvoc() failed");
      ex.printStackTrace();
    }

    return;
  } // _testSecRespCalledAfterSvcInvoc()

  private void getPropsAndParams(HttpServletRequest req,
      HttpServletResponse response) {

    // set logfile location
    logFileLocation = req.getParameter("log.file.location");
    if ((logFileLocation != null)
        && (-1 < logFileLocation.indexOf(JASPICData.DEFAULT_LOG_FILE))) {
      // if here, we have logfile location value which contains
      // JASPICData.DEFAULT_LOG_FILE
      debug("logFileLocation already set");
    } else {
      debug("logFileLocation NOT set completely");
      System.setProperty("log.file.location", logFileLocation);
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

    // set vendor class
    vendorACFClass = req.getParameter("vendor.authconfig.factory");
    if (vendorACFClass == null) {
      debug("ERROR:  getPropsAndParams(): vendorACFClass = null");
    } else {
      debug("getPropsAndParams(): vendorACFClass = " + vendorACFClass);
    }

    servletAppContext = IdUtil.getAppContextId(JASPICData.LAYER_SERVLET);

    return;
  }

  public void debug(String str) {
    System.out.println(str);
  }

}
