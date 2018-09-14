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
package com.sun.ts.tests.jsf.api.javax_faces.application.resourcehandler;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.context.FacesContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import java.util.Enumeration;
import java.util.Hashtable;
import javax.faces.application.ResourceHandler;

public class TestServlet extends HttpTCKServlet {

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    config.getServletContext();
  }

  // ------------------------------------------------------------------- Tests
  // ResourceHandler.createResource(resourceName)
  public void resourceHandlerCreateResourceNTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    try {
      handler.createResource("background.css");
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Resource not created by Handler");
      e.printStackTrace();
    }
  }

  // ResourceHandler.createResource(null)
  // ResourceHandler.createResource(null, libraryName)
  // ResourceHandler.createResource(null, libraryName, contentType)
  public void resourceHandlerCreateResourceNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    // ResourceHandler.createResource(null)
    JSFTestUtil.checkForNPE(handler, "createResource",
        new Class<?>[] { String.class }, new Object[] { null }, out);

    // ResourceHandler.createResource(null, libraryName)
    JSFTestUtil.checkForNPE(handler, "createResource",
        new Class<?>[] { String.class, String.class },
        new Object[] { null, "style-sheets" }, out);

    // ResourceHandler.createResource(null, libraryName, contentType)
    JSFTestUtil.checkForNPE(handler, "createResource",
        new Class<?>[] { String.class, String.class, String.class },
        new Object[] { null, "style-sheets", "text/css" }, out);
  }

  // ResourceHandler.createResource(resourceName libraryName)
  public void resourceHandlerCreateResourceNLTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    try {
      handler.createResource("background.css", "style-sheets");
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "library/resource not created by Handler");
      e.printStackTrace();
    }

  }

  // ResourceHandler.createResource(resourceName libraryName contentType)
  public void resourceHandlerCreateResourceNLCTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    try {
      handler.createResource("background.css", "style-sheets", "text/css");
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "library/resource not created by Handler");
      e.printStackTrace();
    }

  }

  // ResourceHandler.createResource(resourceName libraryName null)
  public void resourceHandlerCreateResourceContentNullTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    try {
      handler.createResource("background.css", "style-sheets", null);
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ResourceHandler did not except 'null' value for " + "contentType");
      e.printStackTrace();
    }
  }

  // ResourceHandler.createResource(resourceName null contentType)
  public void resourceHandlerCreateResourceLibNullTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    try {
      handler.createResource("background.css", null, "text/css");
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ResourceHandler did not except 'null' value for " + "libraryName");
      e.printStackTrace();
    }
  }

  // ResourceHandler.createResource(resourceName null null)
  public void resourceHandlerCreateResourceBothNullTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    try {
      handler.createResource("background.css", null, null);
      out.println("Test PASSED.");

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ResourceHandler did not except 'null' value for "
          + "libraryName & contentType");
      e.printStackTrace();
    }

  }

  // ResourceHandler.handleResourceRequest()
  public void resourceHandlerHandleResourceRequestTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    try {
      handler.handleResourceRequest(FacesContext.getCurrentInstance());
      out.println("Test PASSED.");

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Call to handleResourceRequest failed see below...");
      e.printStackTrace();
    }
  }

  // ResourceHandler.getRendererTypeForResourceName()
  public void resourceHandlergetRendererTypeForResourceNameTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    // Expected values in the form of: <Resource, RendererType>
    Hashtable<String, String> expectedValues = new Hashtable<String, String>();
    expectedValues.put("background.css", "javax.faces.resource.Stylesheet");
    expectedValues.put("hello.js", "javax.faces.resource.Script");

    Enumeration<String> myKeys = expectedValues.keys();
    while (myKeys.hasMoreElements()) {
      String testValue = (String) myKeys.nextElement();
      String expected = expectedValues.get(testValue);

      try {
        String result = handler.getRendererTypeForResourceName(testValue);

        if (!expected.equals(result)) {
          out.println(JSFTestUtil.FAIL + JSFTestUtil.NL + "Expected: "
              + expected + JSFTestUtil.NL + "Received: " + result);
          return;
        }

        out.println("Test PASSED.");

      } catch (Exception e) {
        out.println(JSFTestUtil.FAIL + "Unexpectedly!");
        e.printStackTrace();
      }
    }
  }

  // ResourceHandler.libraryExists()
  public void resourceHandlerlibraryExistsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    if (!handler.libraryExists("style-sheets")) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected library named 'style-sheets' to exist!");
      return;
    }

    if (handler.libraryExists("bogus-library-name")) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected library named 'bogus-library-name' NOT " + "to exist!");
      return;
    }

    out.println("Test PASSED.");

  }

  // ResourceHandler.isResourceRendered()
  public void resourceHandlerIsResourceRenderedTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    if (handler.isResourceRendered(getFacesContext(), "notrendered",
        "notrendered")) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected library 'notrendered' not to be rendered!");
    }

    out.println("Test PASSED.");

  }

  // ResourceHandler.markResourceRendered
  public void resourceHandlerMarkResourceRenderedTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = getFacesContext().getApplication()
        .getResourceHandler();

    if (handler == null) {
      out.println(JSFTestUtil.RESHANDLER_NULL_MSG);
      return;
    }

    try {
      handler.createResource("background.css", "style-sheets", "text/css");
      handler.isResourceRendered(getFacesContext(), "background.css",
          "style-sheets");
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "library/resource not created by Handler");
      e.printStackTrace();
    }

  }
}
