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
package com.sun.ts.tests.jsf.api.javax_faces.application.resource;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Map;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends HttpTCKServlet {
  private static final String RESOURCE_NAME = "duke-boxer.gif";

  private static final String LIBRARY_NAME = "images";

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    config.getServletContext();
  }

  // ------------------------------------------------------------------- Tests
  // Test for Resource.getContentType()
  public void resourceGetContentTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    Resource resource = handler.createResource(RESOURCE_NAME);

    if (!"image/gif".equals(resource.getContentType())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL + "Expected: image/gif"
          + JSFTestUtil.NL + "Received: " + resource.getContentType());
      return;
    }

    out.println(JSFTestUtil.PASS);

  }

  // Test for Resource.setContentType()
  public void resourceSetContentTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    Resource resource = handler.createResource(RESOURCE_NAME);
    resource.setContentType("plain/text");

    if (!"plain/text".equals(resource.getContentType())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL + "Expected: plain/text"
          + JSFTestUtil.NL + "Received: " + resource.getContentType());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test for Resource.getInputStream
  public void resourceGetInputStreamTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    Resource resource = handler.createResource(RESOURCE_NAME);

    if (!(resource.getInputStream().read() > 0)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Resource.getInputStream returned zero bits!");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test for Resource.getLibraryName
  public void resourceGetLibraryNameTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    // create the resource
    Resource resource = handler.createResource(RESOURCE_NAME, LIBRARY_NAME);

    if (!LIBRARY_NAME.equals(resource.getLibraryName())) {
      out.println(
          JSFTestUtil.FAIL + JSFTestUtil.NL + "Expected: " + LIBRARY_NAME
              + JSFTestUtil.NL + "Recieved: " + resource.getLibraryName());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test for Resource.getLibraryName
  public void resourceGetLibraryNameNullTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    // create the resource
    Resource resource = handler.createResource(RESOURCE_NAME, null);

    if (!(null == resource.getLibraryName())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL + "Expected: null"
          + JSFTestUtil.NL + "Recieved: " + resource.getLibraryName());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test for Resource.setLibraryName
  public void resourceSetLibraryNameTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    // create the resource
    Resource resource = handler.createResource(RESOURCE_NAME, LIBRARY_NAME);

    resource.setLibraryName("Sun");

    if (!"Sun".equals(resource.getLibraryName())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL + "Unexpected Library Name!"
          + JSFTestUtil.NL + "Expected: Sun" + JSFTestUtil.NL + "Recieved: "
          + resource.getLibraryName());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test for Resource.getRequestPath
  public void resourceGetRequestPathTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    // create the resource
    Resource resource = handler.createResource(RESOURCE_NAME);

    String expected = "/jsf_appl_resource_web/TestServlet/"
        + "javax.faces.resource/" + RESOURCE_NAME;

    if (!expected.equals(resource.getRequestPath())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL + "Expected: " + expected
          + JSFTestUtil.NL + "Recieved: " + resource.getRequestPath());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test for Resource.getRequestPath with Library
  public void resourceGetRequestPathLibTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    // create the resource.
    Resource resource = handler.createResource(RESOURCE_NAME, LIBRARY_NAME);

    String expected = "/jsf_appl_resource_web/TestServlet/"
        + "javax.faces.resource/" + RESOURCE_NAME + "?ln=" + LIBRARY_NAME;

    if (!expected.equals(resource.getRequestPath())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL + "Expected: " + expected
          + JSFTestUtil.NL + "Recieved: " + resource.getRequestPath());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test for Resource.getURL
  public void resourceGetURLTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    // create the resource & set expected test result.
    Resource resource = handler.createResource(RESOURCE_NAME);
    int expected = 2947;

    InputStream is = resource.getURL().openStream();
    int result = 0;
    while (is.read() != -1) {
      result++;
    }

    if (!(expected == result)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL + "Expected: " + expected
          + JSFTestUtil.NL + "Recieved: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test for Resource.toString
  public void resourceToStringTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    // create the resource & set expected test result.
    Resource resource = handler.createResource(RESOURCE_NAME);
    String expected = "/jsf_appl_resource_web/TestServlet/"
        + "javax.faces.resource/" + RESOURCE_NAME;

    if (!expected.equals(resource.toString())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected Conversions '.toString'!" + JSFTestUtil.NL
          + "Expected: " + expected + JSFTestUtil.NL + "Recieved: "
          + resource.toString());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test for Resource.userAgentNeedsUpdate
  public void resourceUserAgentNeedsUpdateTrueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    // create the resource
    Resource resource = handler.createResource(RESOURCE_NAME);
    boolean result = resource.userAgentNeedsUpdate(getFacesContext());

    if (!result) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected value for 'userAgentNeedsUpdate'!" + JSFTestUtil.NL
          + "Expected: " + true + JSFTestUtil.NL + "Recieved: " + result);

      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test for Resource.getResponseHeaders
  public void resourceGetResponseHeadersTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    // create the resource
    Resource resource = handler.createResource(RESOURCE_NAME);

    try {
      Object result = resource.getResponseHeaders();

      if (!(result instanceof Map)) {
        out.println(
            JSFTestUtil.FAIL + JSFTestUtil.NL + "Expected: Class Type Map"
                + JSFTestUtil.NL + "Recieved: " + result.getClass().getName());
        return;
      }

      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println("Test FAILED. Unexpected Exception thrown!");
      e.printStackTrace();
    }

  }

  // Test for Resource.getResourceName
  public void resourceGetResourceNameTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    // create the resource &
    Resource resource = handler.createResource(RESOURCE_NAME);

    if (!RESOURCE_NAME.equals(resource.getResourceName())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected value for resource name" + JSFTestUtil.NL + "Expected: "
          + RESOURCE_NAME + JSFTestUtil.NL + "Recieved: "
          + resource.getResourceName());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test for Resource.setResourceName
  public void resourceSetResourceNameTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    // create the resource & reset the resource name.
    Resource resource = handler.createResource(RESOURCE_NAME);
    resource.setResourceName("Dukie");

    if (!"Dukie".equals(resource.getResourceName())) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected value for resource name" + JSFTestUtil.NL
          + "Expected: Dukie" + JSFTestUtil.NL + "Recieved: "
          + resource.getResourceName());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // Test for Resource.setResourceName NPE
  public void resourceSetResourceNameNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    // create the resource & reset the resource name.
    Resource resource = handler.createResource(RESOURCE_NAME);

    JSFTestUtil.checkForNPE(resource, "setResourceName",
        new Class<?>[] { String.class }, new Object[] { null }, out);
  }

}
