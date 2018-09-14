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

package com.sun.ts.tests.jsf.api.javax_faces.context.facescontextfactory;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContextFactory;
import javax.faces.context.FacesContext;

import java.io.IOException;
import java.io.PrintWriter;

public final class TestServlet extends HttpTCKServlet {

  ServletContext servletContext;

  Lifecycle lifecycle;

  public void init(ServletConfig config) throws ServletException {
    servletContext = config.getServletContext();
    LifecycleFactory lifeFactory = (LifecycleFactory) FactoryFinder
        .getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    lifecycle = lifeFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    super.init(config);
  }

  // ------------------------------------------- Test Methods ----

  // FacesContextFactory.getFacesContext()
  public void facesCtxFactoryGetFacesContextTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesContextFactory contextFactory = (FacesContextFactory) FactoryFinder
        .getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);
    FacesContext context = contextFactory.getFacesContext(servletContext,
        request, response, lifecycle);

    if (context == null) {
      out.println(
          JSFTestUtil.FAIL + " Unable to obtain a FacesContext instance from"
              + " the FacesContextFactory.");
    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // End facesCtxFactoryGetFacesContextTest

  // FacesContextFactory.getFacesContext() throws NPE if any arguments are
  // null.
  public void facesCtxFactoryGetFacesContextNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    FacesContextFactory contextFactory = (FacesContextFactory) FactoryFinder
        .getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);

    // null context
    JSFTestUtil.checkForNPE(contextFactory, "getFacesContext",
        new Class<?>[] { Object.class, Object.class, Object.class,
            Lifecycle.class },
        new Object[] { null, request, response, lifecycle }, out);

    // null request
    JSFTestUtil.checkForNPE(contextFactory, "getFacesContext",
        new Class<?>[] { Object.class, Object.class, Object.class,
            Lifecycle.class },
        new Object[] { servletContext, null, response, lifecycle }, out);

    // null response
    JSFTestUtil.checkForNPE(contextFactory, "getFacesContext",
        new Class<?>[] { Object.class, Object.class, Object.class,
            Lifecycle.class },
        new Object[] { servletContext, request, null, lifecycle }, out);

    // null lifecycle
    JSFTestUtil.checkForNPE(contextFactory, "getFacesContext",
        new Class<?>[] { Object.class, Object.class, Object.class,
            Lifecycle.class },
        new Object[] { servletContext, request, response, null }, out);
  }
}
