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
package com.sun.ts.tests.jsf.api.javax_faces.application.resourcehandlerwrapper;

//import com.sun.ts.tests.jsf.api.javax_faces.application.application.*;
import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends HttpTCKServlet {

  private static final String RESOURCENAME = "background.css";

  private static final String LIBNAME = "style-sheets";

  private static final String MIME = "text/css";

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    config.getServletContext();
  }

  public void destroy() {
    super.destroy();
  }

  // ------------------------------------------------------------------- Tests
  // ResourceHandler.createResource(resourceName)
  public void resourceHandlerWrapperCreateResourceNTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    MyRHWrapper whandler = new MyRHWrapper();

    if (whandler != null) {
      Resource resource = whandler.createResource(RESOURCENAME);
      this.checkResourceName(resource, RESOURCENAME, out);

    }
  }

  // ResourceHandler.createResource(resourceName, libraryName)
  public void resourceHandlerWrapperCreateResourceNLTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    MyRHWrapper whandler = new MyRHWrapper();

    if (whandler != null) {
      Resource resource = whandler.createResource(RESOURCENAME, LIBNAME);
      this.checkResourceName(resource, RESOURCENAME, out);
      this.checkResourceLibName(resource, LIBNAME, out);

    }
  }

  // ResourceHandler.createResource(resourceName, libraryName, contentType)
  public void resourceHandlerWrapperCreateResourceNLTTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    MyRHWrapper whandler = new MyRHWrapper();

    if (whandler != null) {
      Resource resource = whandler.createResource(RESOURCENAME, LIBNAME, MIME);
      this.checkResourceName(resource, RESOURCENAME, out);
      this.checkResourceLibName(resource, LIBNAME, out);
      this.checkResourceType(resource, MIME, out);

    }
  }

  // ResourceHandler.createResource(resourceName, null, null)
  public void resourceHandlerWrapperCreateResourceNullTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler whandler = new MyRHWrapper();

    if (whandler != null) {
      Resource resource = whandler.createResource(RESOURCENAME, null, null);
      this.checkResourceName(resource, RESOURCENAME, out);
      this.checkResourceLibName(resource, null, out);
      this.checkResourceType(resource, MIME, out);

    }
  }

  // ResourceHandler.getRendererTypeForResourceName(resourceName)
  public void resourceHandlerWrappergetRendererTypeTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    MyRHWrapper whandler = new MyRHWrapper();
    String expected = "javax.faces.resource.Stylesheet";

    if (whandler != null) {
      whandler.createResource(RESOURCENAME);
      String result = whandler.getRendererTypeForResourceName(RESOURCENAME);

      if (expected.equals(result)) {
        out.println(JSFTestUtil.PASS);

      } else {
        out.println("Test FAILED. Unexpected renderer-type!" + JSFTestUtil.NL
            + "Expected: " + expected + JSFTestUtil.NL + "Received: " + result);
      }

    }
  }

  // ResourceHandlerWrapper.handleResourceRequest()
  public void resourceHandlerWrapperHandleResourceRequestTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = new MyRHWrapper();

    if (handler != null) {
      try {
        handler.handleResourceRequest(FacesContext.getCurrentInstance());
        out.println("Test PASSED.");

      } catch (Exception e) {
        out.println("Test FAILED.");
        out.println("Call to handleResourceRequest failed " + "see below...");
        e.printStackTrace();
      }
    }
  }

  // ResourceHandlerWrapper.libraryExists()
  public void resourceHandlerWrapperLibraryExistsTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ResourceHandler handler = new MyRHWrapper();

    boolean tp = true;
    if (handler != null) {
      if (handler.libraryExists(LIBNAME)) {
        // do nothing the test passed.
      } else {
        tp = false;
        out.println(
            "FAILURE: Expected library named  " + LIBNAME + " to exist!");
      }

      if (!(handler.libraryExists("bogus-library-name"))) {
        // do nothing the test passed.
      } else {
        tp = false;
        out.println("FAILURE: Expected library named "
            + "'bogus-library-name' NOT to exist!");
      }

      if (tp) {
        out.println("Test PASSED.");
      } else {
        out.println(JSFTestUtil.FAIL + " See above message(s)");
      }

    }
  }

  // ------------------------------------------------------- private methods
  /*
   * Check Resource Name
   */
  private void checkResourceName(Resource res, String name, PrintWriter pw) {

    if (name.equals(res.getResourceName())) {
      pw.println("Resource Name Test PASSED");
    } else {
      pw.println("Resource Name Test FAILED.");
      pw.println("Expected Resource Name: " + RESOURCENAME);
      pw.println("Received Resource Name: " + res.getResourceName());
    }

  }

  /*
   * Check Resource libraryName
   */
  private void checkResourceLibName(Resource res, String libName,
      PrintWriter pw) {

    String resource = (null == res.getLibraryName()) ? "null"
        : res.getLibraryName();

    String ln = (null == libName) ? "null" : libName;

    if (ln.equals(resource)) {
      pw.println("Library Name Test PASSED");
    } else {
      pw.println("Library Name Test FAILED.");
      pw.println("Expected Library Name: " + ln);
      pw.println("Received Library Name: " + resource);
    }

  }

  /*
   * Check Resource Type
   */
  private void checkResourceType(Resource res, String type, PrintWriter pw) {

    String resource = (null == res.getContentType()) ? "null"
        : res.getContentType();

    String tn = (null == type) ? "null" : type;

    if (tn.equals(resource)) {
      pw.println("Content Type Test PASSED");
    } else {
      pw.println("Content Type Test FAILED");
      pw.println("Expected Content Type: " + tn);
      pw.println("Received Content Type: " + resource);
    }

  }

  // ------------------------------------------------------- private classes
  /*
   * Over ride the getWrapped method on the ResourceHandlerWrapped.
   */
  private class MyRHWrapper extends ResourceHandlerWrapper {

    ResourceHandler wrapped = this.getWrapped();

    @Override
    public ResourceHandler getWrapped() {
      return getFacesContext().getApplication().getResourceHandler();
    }

    @Override
    public String getRendererTypeForResourceName(String arg0) {
      return wrapped.getRendererTypeForResourceName(arg0);
    }

    @Override
    public boolean libraryExists(String arg0) {
      return wrapped.libraryExists(arg0);
    }
  }
}
