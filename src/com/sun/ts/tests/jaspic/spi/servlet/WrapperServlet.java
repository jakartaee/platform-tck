/*
 * Copyright (c)  2014, 2018 Oracle and/or its affiliates. All rights reserved.
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
import java.util.logging.Level;

import com.sun.ts.tests.jaspic.spi.common.CommonTests;
import com.sun.ts.tests.jaspic.spi.common.CommonUtils;
import com.sun.ts.tests.jaspic.tssv.util.IdUtil;
import com.sun.ts.tests.jaspic.tssv.util.JASPICData;
import com.sun.ts.tests.jaspic.tssv.util.TSLogger;
import com.sun.ts.tests.jaspic.tssv.util.TSXMLFormatter;
import com.sun.ts.tests.jaspic.tssv.util.TSFileHandler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.annotation.security.DeclareRoles;
import javax.servlet.annotation.HttpMethodConstraint;
import javax.servlet.annotation.HttpConstraint;

@DeclareRoles({ "Administrator", "Manager", "Employee" })
@ServletSecurity(value = @HttpConstraint(rolesAllowed = {
    "Administrator" }), httpMethodConstraints = {
        @HttpMethodConstraint(value = "GET", rolesAllowed = "Administrator"),
        @HttpMethodConstraint(value = "POST", rolesAllowed = "Administrator") })
@WebServlet(name = "WrapperServlet", urlPatterns = { "/WrapperServlet" })
public class WrapperServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  private String logFileLocation;

  private String servletAppContext = null;

  private String providerConfigFileLocation;

  private String vendorACFClass;

  private String testMethod = null;

  private transient CommonTests commonTests;

  private TSLogger logger = null;

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doTests(request, response);
  }

  @SuppressWarnings("unused")
  public void doPost(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    doTests(request, response);
  }

  public void doTests(HttpServletRequest request,
      HttpServletResponse response) {

    debug("in WrapperServlet.doTests");

    logger = TSLogger.getTSLogger(JASPICData.LOGGER_NAME);

    PrintWriter out = null;
    try {
      out = response.getWriter();
    } catch (Exception ex) {
      debug("got exception in WrapperServlet.doTests");
      ex.printStackTrace();
    }

    // get some common props
    getPropsAndParams(request, response);

    //
    // now send string out to logger so we can verify that the content
    // gets processed BEFORE secureResponse.
    debug("in WrapperServlet.doTests:  calling logger.log()");
    String outStr = "WrapperServlet.doTests() content processed for requestURI";
    logger.log(Level.INFO, outStr);

    if (testMethod.equals("testRequestWrapper")) {
      testRequestWrapper(request, response);
    } else if (testMethod.equals("testResponseWrapper")) {
      testResponseWrapper(request, response);
    }

    // restore original (CTS) factory class
    // note: this should be done in many of the calls but its a safety measure
    // to ensure we are resetting things back to expected defaults
    try {
      CommonUtils.resetDefaultACF();
    } catch (Exception ex) {
      debug("ACFTestServlet:  error calling CommonUtils.resetDefaultACF(): "
          + ex.getMessage());
      ex.printStackTrace();
    }
  }

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

    // initialize TSLogger once we have logFileLocation set
    initializeTSLogger();

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

  public void testRequestWrapper(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out = null;
    try {
      out = response.getWriter();
      out.println("WrapperServlet->testRequestWrapper()");
      out.println(
          "isRequestWrapped = " + request.getAttribute("isRequestWrapped"));
      out.flush();
    } catch (Exception ex) {
      System.out.println("WrapperServlet->testRequestWrapper() failed");
      ex.printStackTrace();
    }
  }

  public void testResponseWrapper(HttpServletRequest request,
      HttpServletResponse response) {
    PrintWriter out = null;
    try {
      out = response.getWriter();
      out.println("WrapperServlet->testResponseWrapper()");
      out.println(
          "isResponseWrapped = " + response.getHeader("isResponseWrapped"));
      out.flush();
    } catch (Exception ex) {
      System.out.println("WrapperServlet->testResponseWrapper() failed");
      ex.printStackTrace();
    }
  }

  /*
   * This is used to initialize our logger so that we can log from this servlet
   * into the TSSVLog.txt file. This will allow us to test
   * verifyRuntimeCallOrder() (from in Client.java)
   */
  private void initializeTSLogger() {
    if (logger == null) {
      logger = TSLogger.getTSLogger(JASPICData.LOGGER_NAME);
    }

    try {
      System.out.println("logFileLocation = " + logFileLocation);
      if (logFileLocation != null) {
        logger = TSLogger.getTSLogger(JASPICData.LOGGER_NAME);
        boolean appendMode = true;

        // create a new file
        TSFileHandler fileHandler = new TSFileHandler(
            logFileLocation + "/" + JASPICData.DEFAULT_LOG_FILE, appendMode);
        fileHandler.setFormatter(new TSXMLFormatter());
        logger.addHandler(fileHandler);
      } else {
        throw new RuntimeException("log.file.location not set");
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("TSLogger Initialization failed", e);
    }
  }

  public void debug(String str) {
    System.out.println(str);
  }

}
