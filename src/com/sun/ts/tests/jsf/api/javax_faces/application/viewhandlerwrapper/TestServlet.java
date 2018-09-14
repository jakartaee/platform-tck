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

package com.sun.ts.tests.jsf.api.javax_faces.application.viewhandlerwrapper;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.FactoryFinder;
import javax.faces.component.UIViewRoot;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;
import java.util.Enumeration;

public final class TestServlet extends HttpTCKServlet {

  ServletContext servletContext;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    servletContext = config.getServletContext();
  }

  // ------------------------------------------- Test Methods ----

  // ViewHandler.calculateLocale(FacesContext)
  public void viewHandlerCalculateLocaleTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String result = doTestCaseOne(request, response);
    out.println("Running test_case one.");
    if (result != null) {
      out.println(result);
      return;
    }

    result = doTestCaseTwo(request, response);
    if (result != null) {
      out.println(result);
      return;
    }

    result = doTestCaseThree(request, response);
    if (result != null) {
      out.println(result);
      return;
    }

    result = doTestCaseFour(request, response);
    if (result != null) {
      out.println(result);
      return;
    }

    result = doTestCaseFive(request, response);
    if (result != null) {
      out.println(result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ViewRoot.createView(FacesContext, String)
  public void viewHandlerCreateViewTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String viewId = "/viewId";
    UIViewRoot root = new SimpleViewHandlerWrapper(
        getApplication().getViewHandler()).createView(getFacesContext(),
            viewId);
    if (root == null) {
      out.println(
          JSFTestUtil.FAIL + " ViewHandler.createView() returned null.");
      return;
    }

    if (!viewId.equals(root.getViewId())) {
      out.println(JSFTestUtil.FAIL + " Expected the UIViewRoot created using"
          + " ViewHandler.createView() to have the same view ID"
          + " as that passed to createView.");
      out.println("Execpted: " + viewId);
      out.println("Received: " + root.getViewId());
      return;
    }

    Locale locale = root.getLocale();
    String renderKitId = root.getRenderKitId();

    getFacesContext().setViewRoot(root);

    String newViewId = "/newViewId";

    UIViewRoot root2 = new SimpleViewHandlerWrapper(
        getApplication().getViewHandler()).createView(getFacesContext(),
            newViewId);
    if (root2 == null) {
      out.println(
          JSFTestUtil.FAIL + " ViewHandler.createView() returned null.");
      return;
    }

    if (!newViewId.equals(root2.getViewId())) {
      out.println(JSFTestUtil.FAIL + " Expected the UIViewRoot created using"
          + " ViewHandler.createView() to have the same view ID"
          + " as that passed to createView.");
      out.println("Execpted: " + newViewId);
      out.println("Received: " + root2.getViewId());
      return;
    }

    if (root2 == root) {
      out.println(JSFTestUtil.FAIL + " createView() returned same view instance"
          + " previously created and stored within the FacesContext");
      out.println("Original view root: " + root);
      out.println("New view root: " + root2);
      return;
    }

    if (locale != root2.getLocale()) {
      out.println(JSFTestUtil.FAIL + " Expected the Locale from the original"
          + " view root to be copied to the new view root instance.");
      return;
    }

    if (renderKitId != root.getRenderKitId()) {
      out.println(
          JSFTestUtil.FAIL + " Expected the RenderKitID from the original"
              + " view root to be copied to the new view root instance.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ViewRoot.renderView(FacesContext, UIViewRoot) throws NPE if either
  // argument is null
  public void viewHandlerRenderViewNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    try {
      new SimpleViewHandlerWrapper(getApplication().getViewHandler())
          .renderView(null, new UIViewRoot());
      out.println(
          JSFTestUtil.FAIL + " No Exception thrown when a null FacesContext"
              + "was passed to ViewHandler.renderView().");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when a null "
            + "FacesContext was provided to ViewHandler.render"
            + "View(), but it was not an instance of "
            + "NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      new SimpleViewHandlerWrapper(getApplication().getViewHandler())
          .renderView(getFacesContext(), null);
      out.println(
          JSFTestUtil.FAIL + " No Exception thrown when a null view root"
              + "was passed to ViewHandler.renderView().");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when a null "
            + "view root was provided to ViewHandler.render"
            + "View(), but it was not an instance of "
            + "NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // COMMENTED OUT DUE TO CHANGE IN VIEW HANDLER. Basic logic still
  // holds for future specification test.
  // ViewRoot.getViewIdPath()
  // public void viewHandlerGetViewIdPathTest(HttpServletRequest request,
  // HttpServletResponse response)
  // throws ServletException, IOException {
  // PrintWriter out = response.getWriter();
  // TCKServletPathWrapper wrapper =
  // new TCKServletPathWrapper(request);
  // FacesContext context = initFacesContext(wrapper, response);
  //
  // ViewHandler handler = getApplication().getViewHandler();
  //
  // if (handler == null) {
  // out.println(FAIL + " Unable to obtain ViewHandler instance.");
  // return;
  // }
  //
  // String result;
  //
  // if ("prefix".equals(request.getParameter("mapping"))) {
  // // Simulate an incoming prefix path mapped request
  // wrapper.setServletPath("/mapping");
  // wrapper.setPathInfo("/myapp/test.jsp");
  //
  // result = handler.getViewIdPath(context, "/view.jsp");
  //
  // if (!"/mapping/view.jsp".equals(result)) {
  // out.println(FAIL + " Unexpected result returned when using" +
  // " a simulated prefix path mapped request.");
  // out.println("Expected: /mapping/view.jsp");
  // out.println("Received: " + result);
  // return;
  // }
  // }
  //
  // if ("suffix1".equals(request.getParameter("mapping"))) {
  // // Simulate an incoming extension mapped request -- NOTE:
  // // the view ID has an extension, it should be replaced
  // // with the extension mapping defined in the deployment
  // // descriptor.
  // wrapper.setServletPath("/myapp/view.faces");
  // wrapper.setPathInfo(null);
  //
  // result = handler.getViewIdPath(context, "/view.jsp");
  //
  // if (!"/view.faces".equals(result)) {
  // out.println("Test FAILED[1]. Unexpected result returned when using" +
  // " a simulated extension mapped request.");
  // out.println("Expected: /view.faces");
  // out.println("Received: " + result);
  // return;
  // }
  // }
  //
  // if ("suffix2".equals(request.getParameter("mapping"))) {
  // // Simulate an incoming extension mapped request -- NOTE:
  // // the view ID has no extension, so it should be appended.
  // wrapper.setServletPath("/myapp/view.faces");
  // wrapper.setPathInfo(null);
  //
  // result = handler.getViewIdPath(context, "/view");
  //
  // if (!"/view.faces".equals(result)) {
  // out.println("Test FAILED[2]. Unexpected result returned when using" +
  // " a simulated extension mapped request.");
  // out.println("Expected: /view.faces");
  // out.println("Received: " + result);
  // return;
  // }
  // }
  //
  // out.println(PASS);
  // }

  // ViewHandler.calculateLocale(FacesContext) throws NPE if FacesContext
  // is null.
  public void viewHandlerCalculateLocaleNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    try {
      new SimpleViewHandlerWrapper(getApplication().getViewHandler())
          .calculateLocale(null);
      out.println(JSFTestUtil.FAIL + " No Exception thrown when a null argument"
          + "was passed to ViewHandler.calculateLocale().");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when a null "
            + "argument was provided to ViewHandler.calculate"
            + "Locale(), but it was not an instance of "
            + "NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // ViewHandler.createView(FacesContext, String) throws NPE if FacesContext
  // is null.
  public void viewHandlerCreateViewNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    try {
      new SimpleViewHandlerWrapper(getApplication().getViewHandler())
          .createView(null, "/viewId");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when a null argument"
          + "was passed to ViewHandler.calculateLocale().");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when a null "
            + "argument was provided to ViewHandler.calculate"
            + "Locale(), but it was not an instance of "
            + "NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // ------------------------------------------------ Private Methods --------

  private String doTestCaseFive(HttpServletRequest request,
      HttpServletResponse response) {
    Collection localeCollection = new ArrayList();
    localeCollection.add(Locale.ENGLISH);
    localeCollection.add(new Locale("fr", "CA"));
    Locale[] prefLocales = { new Locale("en", "GB"), new Locale("fr", "CA") };

    FacesContext context = initTestEnvironment(localeCollection,
        new Locale("fr", "CA"), "en-GB, fr-CA", prefLocales, request, response);
    if (context == null) {
      return "Test FAILED[1].  Unable to initialize FacesContext.";
    }

    ViewHandler handler = new SimpleViewHandlerWrapper(
        getApplication().getViewHandler());

    Locale result = handler.calculateLocale(context);
    if (!result.equals(Locale.ENGLISH)) {
      StringBuffer buff = new StringBuffer(50);
      buff.append("Test FAILED[1].  Expected the result of ");
      buff.append("ViewHandler.calculateLocale() to return ");
      buff.append("en given the following conditions:\n");
      buff.append("Supported Locales: en, fr_CA\n");
      buff.append("Default Locale: fr_CA\n");
      buff.append("Client preferred Locales: en-GB, fr-CA\n");
      buff.append("Locale returned: " + result);
      return buff.toString();
    }

    return null;
  }

  private String doTestCaseTwo(HttpServletRequest request,
      HttpServletResponse response) {
    Collection localeCollection = new ArrayList();
    localeCollection.add(Locale.ENGLISH);
    Locale[] prefLocales = { new Locale("de", ""), new Locale("fr", "") };

    FacesContext context = initTestEnvironment(localeCollection, Locale.ENGLISH,
        "de, fr", prefLocales, request, response);
    if (context == null) {
      return "Test FAILED[2].  Unable to initialize FacesContext.";
    }

    ViewHandler handler = new SimpleViewHandlerWrapper(
        getApplication().getViewHandler());

    Locale result = handler.calculateLocale(context);
    if (!result.equals(Locale.ENGLISH)) {
      StringBuffer buff = new StringBuffer(50);
      buff.append("Test FAILED[2].  Expected the result of ");
      buff.append("ViewHandler.calculateLocale() to return ");
      buff.append("en given the following conditions:\n");
      buff.append("Supported Locales: en\n");
      buff.append("Default Locale: en\n");
      buff.append("Client preferred Locales: de, fr\n");
      buff.append("Locale returned: " + result);
      return buff.toString();
    }

    return null;
  }

  private String doTestCaseThree(HttpServletRequest request,
      HttpServletResponse response) {
    Collection localeCollection = new ArrayList();
    localeCollection.add(Locale.ENGLISH);
    localeCollection.add(new Locale("fr", ""));
    localeCollection.add(new Locale("en", "US"));
    Locale[] prefLocales = { new Locale("ja", ""), new Locale("en", "GB"),
        new Locale("en", "US"), new Locale("en", "CA"), new Locale("fr", "") };

    FacesContext context = initTestEnvironment(localeCollection, Locale.CHINESE,
        "ja, en-GB, en-US, en-CA, fr", prefLocales, request, response);
    if (context == null) {
      return "Test FAILED[3].  Unable to initialize FacesContext.";
    }

    ViewHandler handler = new SimpleViewHandlerWrapper(
        getApplication().getViewHandler());

    Locale result = handler.calculateLocale(context);
    if (!result.equals(Locale.ENGLISH)) {
      StringBuffer buff = new StringBuffer(50);
      buff.append("Test FAILED[3].  Expected the result of ");
      buff.append("ViewHandler.calculateLocale() to return ");
      buff.append("en given the following conditions:\n");
      buff.append("Supported Locales: en, fr, en_US\n");
      buff.append("Default Locale: en\n");
      buff.append("Client preferred Locales: ja, en-GB, en-US, en-CA, fr\n");
      buff.append("Locale returned: " + result);
      return buff.toString();
    }

    return null;
  }

  private String doTestCaseFour(HttpServletRequest request,
      HttpServletResponse response) {
    Collection localeCollection = new ArrayList();
    localeCollection.add(new Locale("fr", "CA"));
    localeCollection.add(new Locale("sv", ""));
    localeCollection.add(Locale.ENGLISH);
    Locale[] prefLocales = { new Locale("fr", ""), new Locale("sv", ""), };

    FacesContext context = initTestEnvironment(localeCollection, Locale.GERMAN,
        "fr, sv", prefLocales, request, response);
    if (context == null) {
      return "Test FAILED[4].  Unable to initialize FacesContext.";
    }

    ViewHandler handler = new SimpleViewHandlerWrapper(
        getApplication().getViewHandler());

    Locale result = handler.calculateLocale(context);
    if (!result.equals(new Locale("sv", ""))) {
      StringBuffer buff = new StringBuffer(50);
      buff.append("Test FAILED[4].  Expected the result of ");
      buff.append("ViewHandler.calculateLocale() to return ");
      buff.append("sv given the following conditions:\n");
      buff.append("Supported Locales: fr_CA, sv, en\n");
      buff.append("Default Locale: de\n");
      buff.append("Client preferred Locales: fr, sv\n");
      buff.append("Locale returned: " + result);
      return buff.toString();
    }

    return null;
  }

  private String doTestCaseOne(HttpServletRequest request,
      HttpServletResponse response) {
    Collection localeCollection = new ArrayList();
    localeCollection.add(new Locale("ja", ""));
    Locale[] prefLocales = { new Locale("en", ""), };

    FacesContext context = initTestEnvironment(localeCollection, null, "en",
        prefLocales, request, response);
    if (context == null) {
      return "Test FAILED[5].  Unable to initialize FacesContext.";
    }

    ViewHandler handler = new SimpleViewHandlerWrapper(
        getApplication().getViewHandler());

    Locale result = handler.calculateLocale(context);
    if (!result.equals(Locale.getDefault())) {
      StringBuffer buff = new StringBuffer(50);
      buff.append("Test FAILED[5].  Expected the result of ");
      buff.append("ViewHandler.calculateLocale() to return ");
      buff.append(Locale.getDefault());
      buff.append(" given the following conditions:\n");
      buff.append("Supported Locales: ja\n");
      buff.append("Default Locale: null\n");
      buff.append("Client preferred Locales: en\n");
      buff.append("Locale returned: " + result);
      return buff.toString();
    }

    return null;
  }

  private FacesContext initTestEnvironment(Collection locales,
      Locale defaultLocale, String preferredLocales, Locale[] lPreferredLocales,
      HttpServletRequest request, HttpServletResponse response) {
    Application application = getApplication();
    application.setSupportedLocales(locales);
    if (defaultLocale != null) {
      application.setDefaultLocale(defaultLocale);
    }

    // Create a new FacesContext using the TCKHttpServletRequestWrapper
    TCKHttpServletRequestWrapper wrapper = new TCKHttpServletRequestWrapper(
        request);
    wrapper.setAcceptHeaderValues(preferredLocales);
    wrapper.setLocales(lPreferredLocales);
    return initFacesContext(wrapper, response);
  }

  private FacesContext initFacesContext(HttpServletRequest request,
      HttpServletResponse response) {
    LifecycleFactory lifeFactory = (LifecycleFactory) FactoryFinder
        .getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    Lifecycle lifecycle = lifeFactory
        .getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);

    FacesContextFactory facesFactory = (FacesContextFactory) FactoryFinder
        .getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    return facesFactory.getFacesContext(servletContext, request, response,
        lifecycle);
  }

  // -------------------------------------------------- Private Classes ------

  private static class TCKHttpServletRequestWrapper
      extends HttpServletRequestWrapper {

    private String values;

    private Locale[] locales;

    public TCKHttpServletRequestWrapper(HttpServletRequest request) {
      super(request);
    }

    public String getHeader(String name) {
      if ("accept-language".equalsIgnoreCase(name)) {
        return values;
      }
      return super.getHeader(name);
    }

    public Enumeration getHeaders(String name) {
      if ("accept-language".equalsIgnoreCase(name)) {
        return new Enumeration() {
          boolean hasMore = true;

          public boolean hasMoreElements() {
            return hasMore;
          }

          public Object nextElement() {
            hasMore = false;
            return values;
          }
        };
      }
      return super.getHeaders(name);
    }

    public Enumeration getLocales() {

      return new Enumeration() {
        int length = locales.length;

        int count = 0;

        public boolean hasMoreElements() {
          return (count < length);
        }

        public Object nextElement() {
          return locales[count++];
        }
      };
    }

    public void setAcceptHeaderValues(String values) {
      this.values = values;
    }

    public void setLocales(Locale[] locales) {
      this.locales = locales;
    }

  }

  private static class TCKServletPathWrapper extends HttpServletRequestWrapper {

    private String servletPath;

    private String pathInfo;

    public TCKServletPathWrapper(HttpServletRequest request) {
      super(request);
    }

    public String getServletPath() {
      return servletPath;
    }

    public String getPathInfo() {
      return pathInfo;
    }

    public void setServletPath(String servletPath) {
      this.servletPath = servletPath;
    }

    public void setPathInfo(String pathInfo) {
      this.pathInfo = pathInfo;
    }
  }

  private static class SimpleViewHandlerWrapper extends ViewHandlerWrapper {

    ViewHandler handler;

    // -------------------------------------------------------- Constructors

    SimpleViewHandlerWrapper(ViewHandler handler) {

      if (handler == null) {
        throw new IllegalArgumentException(
            "ViewHandler argument cannot be null");
      }
      this.handler = handler;

    } // SimpleViewHandlerWrapper

    // ------------------------------------- Methods from ViewHandlerWrapper

    public ViewHandler getWrapped() {

      return handler;

    } // END getWrapped

  } // END SimpleViewHandlerWrapper
}
