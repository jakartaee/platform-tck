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
package com.sun.ts.tests.jsf.api.javax_faces.application.resourcewrapper;

//import com.sun.ts.tests.jsf.api.javax_faces.application.application.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.faces.application.Resource;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import java.util.Map;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceWrapper;

public class TestServlet extends HttpTCKServlet {

  /*
   * private test values. (These are the expected results!)
   */
  private static final String RESOURCE_NAME = "duke-boxer.gif";

  private static final String LIBRARY_NAME = "images";

  private static final String RESOURCE_PATH = "/jsf_appl_resourcewrapper_web/TestServlet/javax.faces.resource/"
      + RESOURCE_NAME;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    config.getServletContext();
  }

  public void destroy() {
    super.destroy();
  }

  // ------------------------------------------------------------------- Tests
  public void resourceWrapperGetInputStreamTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceWrapper wrapped = new TCKResourceWrapper();

    if (wrapped.getInputStream().read() > 0) {
      out.println("Test PASSED.");

    } else {
      out.println("Test FAILED.");
      out.println("InputSteam Empty");
    }

  }

  public void resourceWrapperGetURLTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceWrapper wrapped = new TCKResourceWrapper();

    int expected = 2947;

    InputStream is = wrapped.getURL().openStream();
    int result = 0;
    while (is.read() != -1) {
      result++;
    }

    if (expected == result) {
      out.println(JSFTestUtil.PASS);
    } else {
      out.println("Test FAILED. Unexpected URL Path!");
      out.println("Expected Resource Size: " + expected + " bits");
      out.println("Recieved Resource Size: " + result + " bits");
    }

  }

  public void resourceWrapperGetRequestPathTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceWrapper wrapped = new TCKResourceWrapper();

    if (RESOURCE_PATH.equals(wrapped.getRequestPath())) {
      out.println("Test PASSED.");

    } else {
      out.println("Test FAILED. Invalid RequestPath Returned.");
      out.println("Expected: " + RESOURCE_PATH);
      out.println("Received: " + wrapped.getRequestPath());
    }

  }

  public void resourceWrapperGetRequestPathLibTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceWrapper wrapped = new TCKResourceLibWrapper();

    String expected = RESOURCE_PATH + "?ln=" + LIBRARY_NAME;

    if (expected.equals(wrapped.getRequestPath())) {
      out.println("Test PASSED.");

    } else {
      out.println("Test FAILED. Invalid RequestPath Returned.");
      out.println("Expected: " + expected);
      out.println("Received: " + wrapped.getRequestPath());
    }

  }

  public void resourceWrapperGetResponseHeadersTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceWrapper wrapped = new TCKResourceWrapper();

    Map rMap = wrapped.getResponseHeaders();

    if (!(rMap == null)) {
      out.println("Test PASSED.");

    } else {
      out.println("Test FAILED. Zero Length ResponseHeader Returned.");
    }
  }

  // ------------------------------------------------------- private classes
  /*
   * ResourceWrapped.
   */
  private class TCKResourceWrapper extends ResourceWrapper {

    @Override
    public Resource getWrapped() {
      ResourceHandler rh = getFacesContext().getApplication()
          .getResourceHandler();

      Resource resource = rh.createResource(RESOURCE_NAME);

      return resource;
    }
  }

  private class TCKResourceLibWrapper extends ResourceWrapper {

    @Override
    public Resource getWrapped() {
      ResourceHandler rh = getFacesContext().getApplication()
          .getResourceHandler();

      Resource resource = rh.createResource(RESOURCE_NAME, LIBRARY_NAME);

      return resource;
    }
  }
}
