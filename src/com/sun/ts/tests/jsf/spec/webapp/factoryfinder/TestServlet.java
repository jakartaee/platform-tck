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
package com.sun.ts.tests.jsf.spec.webapp.factoryfinder;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.faces.FactoryFinder;
import javax.faces.render.RenderKitFactory;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.context.FacesContextFactory;
import javax.faces.application.ApplicationFactory;

import java.io.IOException;
import java.io.PrintWriter;

public final class TestServlet extends HttpTCKServlet {

  /**
   * <p>
   * Initialize this <code>Servlet</code> instance.
   * </p>
   * 
   * @param config
   *          the configuration for this <code>Servlet</code>
   * 
   * @throws javax.servlet.ServletException
   *           indicates initialization failure
   */
  public void init(ServletConfig config) throws ServletException {

    super.init(config);

  } // init

  // ------------------------------------------------------------ Test Methods

  public void factoryFinderConfig1Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // validate FactoryFinder can be configured via faces-config in
    // /WEB-INF
    PrintWriter out = response.getWriter();
    ApplicationFactory factory = (ApplicationFactory) FactoryFinder
        .getFactory(FactoryFinder.APPLICATION_FACTORY);

    if (factory == null) {
      out.println(
          "Test FAILED.  Unable to obtain ApplicationFactory" + " instance.");
      return;
    }

    if (!(factory.getWrapped() instanceof TCKApplicationFactory)) {
      out.println("Test FAILED.  Unexpected ApplicationFactory returned"
          + " by the FactoryFinder.");
      out.println("Expected: " + TCKApplicationFactory.class);
      out.println("Received: " + factory.getWrapped().getClass());
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // END factorFinderConfig1Test

  public void factoryFinderConfig2Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // validate FactoryFinder can be configured via javax.faces.CONFIG_FILES
    // context parameter
    PrintWriter out = response.getWriter();
    FacesContextFactory factory = (FacesContextFactory) FactoryFinder
        .getFactory(FactoryFinder.FACES_CONTEXT_FACTORY);

    if (factory == null) {
      out.println(
          "Test FAILED.  Unable to obtain FacesContextFactory" + " instance.");
      return;
    }

    if (!(factory.getWrapped() instanceof TCKFacesContextFactory)) {
      out.println("Test FAILED.  Unexpected FacesContextFactory returned"
          + " by the FactoryFinder.");
      out.println("Expected: " + TCKFacesContextFactory.class);
      out.println("Received: " + factory.getWrapped().getClass());
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // END factorFinderConfig2Test

  public void factoryFinderConfig3Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // validate FactoryFinder can be configured via META-INF/faces-config.xml
    PrintWriter out = response.getWriter();
    LifecycleFactory factory = (LifecycleFactory) FactoryFinder
        .getFactory(FactoryFinder.LIFECYCLE_FACTORY);

    if (factory == null) {
      out.println(
          "Test FAILED.  Unable to obtain LifecycleFactory" + " instance.");
      return;
    }

    if (!(factory instanceof TCKLifecycleFactory)) {
      out.println("Test FAILED.  Unexpected LifecycleFactory returned"
          + " by the FactoryFinder.");
      out.println("Expected: " + TCKLifecycleFactory.class);
      out.println("Received: " + factory.getClass());
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // END factorFinderConfig3Test

  public void factoryFinderConfig4Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    // validate FactoryFinder can be configured via META-INF/faces-config.xml
    PrintWriter out = response.getWriter();
    RenderKitFactory factory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);

    if (factory == null) {
      out.println(
          "Test FAILED.  Unable to obtain RenderKitFactory" + " instance.");
      return;
    }

    if (!(factory instanceof TCKRenderKitFactory)) {
      out.println("Test FAILED.  Unexpected RenderKitFactory returned"
          + " by the FactoryFinder.");
      out.println("Expected: " + TCKRenderKitFactory.class);
      out.println("Received: " + factory.getClass());
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // END factorFinderConfig4Test

}
