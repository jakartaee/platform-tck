/*
 * Copyright (c) 2011, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.javax_faces.component.behavior.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.behavior.ClientBehaviorBase;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public abstract class ClientBehaviorBaseTestServlet extends HttpTCKServlet {

  protected ServletContext servletContext;

  /**
   * <p>
   * Initializes this {@link javax.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws ServletException
   *           if an error occurs
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    servletContext = config.getServletContext();
  }

  /**
   * <p>
   * Creates a new {@link ClientBehaviorBase} instance.
   * </p>
   * 
   * @return a new {@link ClientBehaviorBase} instance.
   */
  protected abstract ClientBehaviorBase createBehavior();

  // --------------------------------------- Test Methods

  // ClientBehaviorBase.decode(FacesContext, UIComponent) throws
  // NullPointerException
  public void clientBehaviorDecodeNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // .decode(null, UIComponent)
    JSFTestUtil.checkForNPE(createBehavior().getClass(), "decode",
        new Class<?>[] { FacesContext.class, UIComponent.class },
        new Object[] { null, new UIInput() }, out);

    // .decode(FacesContext, null)
    JSFTestUtil.checkForNPE(createBehavior().getClass(), "decode",
        new Class<?>[] { FacesContext.class, UIComponent.class },
        new Object[] { getFacesContext(), null }, out);

  } // END behaviorBroadcastNPETest

  // ClientBehaviorBase.getScript(ClientBehaviorContext) throws
  // NullPointerException
  public void clientBehaviorGetScriptNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // .getScript(null)
    JSFTestUtil.checkForNPE(createBehavior().getClass(), "getScript",
        new Class<?>[] { ClientBehaviorContext.class }, new Object[] { null },
        out);

  } // END behaviorBroadcastNPETest

}
