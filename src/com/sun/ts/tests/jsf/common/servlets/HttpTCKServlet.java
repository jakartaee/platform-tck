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

package com.sun.ts.tests.jsf.common.servlets;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.FacesContextFactory;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Any test that would normally extend GenericServlet will instead extend this
 * class. This will provide a simple framework from invoking various tests
 * defined as methods within the servlet that extends this class.
 *
 */

public abstract class HttpTCKServlet extends HttpServlet {

  private static final String TEXT_PLAIN = "text/plain";

  /**
   * <code>TEST_HEADER</code> is the constant for the <code>testname</code>
   * header.
   */
  private static final String TEST_HEADER = "testname";

  /**
   * The {@link Application} object for this context.
   */
  private Application application;

  /**
   * The {@link FacesContext} object for this request.
   */
  private FacesContext facesContext;

  /**
   * The environment for this web application.
   */
  private ServletContext context;

  // ---------------------------------------------------------- Public Methods

  /**
   * <code>init</code> initializes the servlet.
   *
   * @param config
   *          - <code>ServletConfig</code>
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    context = config.getServletContext();
  }

  /**
   * A basic implementation of the <code>doGet</code> method which will call
   * invokeTest.
   *
   * @param req
   *          - <code>HttpServletRequest</code>
   * @param res
   *          - <code>HttpServletResponse</code>
   * @exception ServletException
   *              if an error occurs
   * @exception IOException
   *              if an IO error occurs
   */
  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    invokeTest(req, res);
  }

  /**
   * A basic implementation of the <code>doPost</code> method which will call
   * invokeTest.
   *
   * @param req
   *          - <code>HttpServletRequest</code>
   * @param res
   *          - <code>HttpServletResponse</code>
   * @exception ServletException
   *              if an error occurs
   * @exception IOException
   *              if an IO error occurs
   */
  @Override
  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    invokeTest(req, res);

  }

  // ------------------------------------------------------- Protected Methods

  /**
   * <code>invokeTest</code> uses reflection to invoke test methods in child
   * classes of this particular class.
   *
   * @param req
   *          - <code>HttpServletRequest</code>
   * @param res
   *          - <code>HttpServletResponse</code>
   * @exception ServletException
   *              if an error occurs
   */
  @SuppressWarnings("static-access")
  protected void invokeTest(HttpServletRequest req, HttpServletResponse res)
      throws ServletException {
    initFaces(context, req, res);
    res.setContentType(TEXT_PLAIN);
    char[] temp = req.getParameter(TEST_HEADER).toCharArray();
    temp[0] = Character.toLowerCase(temp[0]);
    String test = new String(temp);

    Method[] methods = this.getClass().getMethods();
    // (test, TEST_ARGS);
    Method method = null;
    for (int i = 0; i < methods.length; i++) {
      method = methods[i];
      if (method.getName().equals(test)) {
        break;
      }
    }
    if (method == null) {
      throw new ServletException("Test: " + test + " does not exist.");
    }
    try {
      method.invoke(this, new Object[] { req, res });
    } catch (InvocationTargetException ite) {
      throw new ServletException(ite.getTargetException());
    } catch (Exception e) {
      throw new ServletException("Error executing test: " + test, e);
    } finally {
      if (facesContext.getCurrentInstance() != null) {
        facesContext.release();
      }
    }
  }

  protected Application getApplication() {

    return application;

  }

  protected FacesContext getFacesContext() {

    return facesContext;

  }

  protected UIViewRoot createViewRoot(String viewId) {

    return application.getViewHandler().createView(facesContext, viewId);

  }

  protected UIViewRoot createViewRoot() {

    return createViewRoot(null);

  }

  // --------------------------------------------------------- Private Methods

  private void initFaces(ServletContext context, ServletRequest request,
      ServletResponse response) {

    // PRECONDITION: Hopefully the technology under test has this right
    FacesContextFactory facesContextFactory = (FacesContextFactory) FactoryFinder
        .getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);

    if (facesContextFactory != null) {
      LifecycleFactory lifecycleFactory = (LifecycleFactory) FactoryFinder
          .getFactory(FactoryFinder.LIFECYCLE_FACTORY);
      facesContext = facesContextFactory.getFacesContext(context, request,
          response,
          lifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE));
      if (facesContext == null) {
        throw new IllegalStateException(
            "Unable to obtain FacesContext instance");
      }

      // Set up references to the application and facesContext objects
      application = facesContext.getApplication();
      facesContext.setViewRoot(createViewRoot());
    } else {
      throw new IllegalStateException(
          "Unable to obtain FacesContextFactory instance.");
    }
  }

}// HttpTCKServlet
