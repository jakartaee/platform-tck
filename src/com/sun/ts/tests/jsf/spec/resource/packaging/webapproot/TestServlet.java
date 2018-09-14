/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

/*
 * $Id$
 */
package com.sun.ts.tests.jsf.spec.resource.packaging.webapproot;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.spec.resource.common.util.ResourceChecker;
import javax.faces.application.ResourceHandler;

public class TestServlet extends HttpTCKServlet {

  // private indicators that a test has previously passed.
  // this is necessary if a test is run multiple times
  // without reloading the application
  private boolean setGetLocaleTestPassed = false;

  private ServletContext servletContext;

  // When testing Resource using image.
  private static final int IMAGE_SIZE = 2947;

  // When testing Resource using stylesheet.
  private static final int CSS_SIZE = 947;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    servletContext = config.getServletContext();
  }

  public void destroy() {
    super.destroy();
  }

  // ------------------------------------------------------------------- Tests
  public void resourceResPkgTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceChecker.checkIndentifier(this.getTCKHandler(), "duke-boxer.gif",
        IMAGE_SIZE, out);

  }

  public void resourceLibResPkgTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceChecker.checkIndentifier(this.getTCKHandler(), "duke-boxer-1.gif",
        IMAGE_SIZE, "images", out);
  }

  public void resourceLibVerResPkgTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceChecker.checkIndentifier(this.getTCKHandler(), "gun.sm.gif",
        IMAGE_SIZE, "lib", out);
  }

  public void resourceResVerPkgTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceChecker.checkIndentifier(this.getTCKHandler(), "time.css", CSS_SIZE,
        out);
  }

  public void resourceLibVerResVerPkgTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceChecker.checkIndentifier(this.getTCKHandler(), "foreground.css",
        CSS_SIZE, "styles", out);
  }

  public void reourceNoFileExtPkgTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceChecker.checkIndentifier(this.getTCKHandler(),
        "negative_test_image", IMAGE_SIZE, null, out);
  }

  public void resourceLocaleDEPkgTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceChecker.checkIndentifier(this.getTCKHandler(), "duke-de.gif",
        IMAGE_SIZE, out);
  }

  public void resourceLocaleENPkgTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceChecker.checkIndentifier(this.getTCKHandler(), "duke-en.gif",
        IMAGE_SIZE, "locLib", out);
  }

  public void resourceLocaleFRPkgTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceChecker.checkIndentifier(this.getTCKHandler(), "duke-fr.gif",
        IMAGE_SIZE, out);
  }

  // ------------------------------------------------------ negative test cases
  public void resourceLibVerResVerNegativePkgTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceChecker.checkIndentifier(this.getTCKHandler(), "background.css",
        CSS_SIZE, "styles", true, out);
  }

  public void reourceLibVerResNegativePkgTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceChecker.checkIndentifier(this.getTCKHandler(), "never-found.css", 0,
        "styles", true, out);
  }

  public void reourceTrailingUSNegativePkgTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceChecker.checkIndentifier(this.getTCKHandler(), "trailing.css", 0,
        "styles", true, out);
  }

  public void reourceLeadingUSNegativePkgTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceChecker.checkIndentifier(this.getTCKHandler(), "leading.css", 0,
        "styles", true, out);
  }

  public void reourceNoUSNegetivePkgTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceChecker.checkIndentifier(this.getTCKHandler(), "noUnderscore.css",
        0, "styles", true, out);
  }

  public void reourceNoFileExtVerNegetivePkgTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceChecker.checkIndentifier(this.getTCKHandler(), "doug.css", 0,
        "styles", true, out);
  }

  // ---------------------------------------------------------- private methods
  private ResourceHandler getTCKHandler() {
    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    return handler;
  }
}
