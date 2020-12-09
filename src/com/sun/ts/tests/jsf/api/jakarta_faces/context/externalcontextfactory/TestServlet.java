/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.jakarta_faces.context.externalcontextfactory;

import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import jakarta.faces.FactoryFinder;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.ExternalContextFactory;
import jakarta.faces.lifecycle.Lifecycle;
import jakarta.faces.lifecycle.LifecycleFactory;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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

  public void externalContextFactoryGetExternalContextTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ExternalContextFactory contextFactory = (ExternalContextFactory) FactoryFinder
        .getFactory(FactoryFinder.EXTERNAL_CONTEXT_FACTORY);

    ExternalContext context = contextFactory.getExternalContext(servletContext,
        request, response);

    if (context == null) {
      out.println(
          JSFTestUtil.FAIL + " Unable to obtain a FacesContext instance from"
              + " the FacesContextFactory.");
    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // End externalContextFactoryGetExternalContextTest

  public void externalCtxFactoryGetExternalContextNPETest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ExternalContextFactory contextFactory = (ExternalContextFactory) FactoryFinder
        .getFactory(FactoryFinder.EXTERNAL_CONTEXT_FACTORY);

    // null context
    JSFTestUtil.checkForNPE(contextFactory, "getExternalContext",
        new Class<?>[] { Object.class, Object.class, Object.class },
        new Object[] { null, request, response }, out);

    // null request
    JSFTestUtil.checkForNPE(contextFactory, "getExternalContext",
        new Class<?>[] { Object.class, Object.class, Object.class },
        new Object[] { servletContext, null, response }, out);

    // null response
    JSFTestUtil.checkForNPE(contextFactory, "getExternalContext",
        new Class<?>[] { Object.class, Object.class, Object.class },
        new Object[] { servletContext, request, null }, out);

  }// End externalCtxFactoryGetExternalContextNPETest

  public void externalContextFactoryGetWrappedTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ExternalContextFactory contextFactory = (ExternalContextFactory) FactoryFinder
        .getFactory(FactoryFinder.EXTERNAL_CONTEXT_FACTORY);

    ExternalContextFactory context = contextFactory.getWrapped();

    if (context != null) {
      out.println(
          JSFTestUtil.FAIL + " Unable to obtain a FacesContext instance from"
              + " the FacesContextFactory.");
    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // End externalContextFactoryGetWrappedTest

}
