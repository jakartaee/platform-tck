/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.javax_faces.render.clientbehaviorrenderer;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.behavior.AjaxBehavior;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.context.FacesContext;
import javax.faces.render.ClientBehaviorRenderer;
import javax.faces.render.RenderKit;
import javax.faces.render.RenderKitFactory;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends HttpTCKServlet {

  /**
   * <p>
   * Initializes this {@link javax.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws javax.servlet.ServletException
   *           if an error occurs
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  public RenderKit getRenderKit() {
    RenderKitFactory factory = (RenderKitFactory) FactoryFinder
        .getFactory(FactoryFinder.RENDER_KIT_FACTORY);

    return factory.getRenderKit(getFacesContext(),
        RenderKitFactory.HTML_BASIC_RENDER_KIT);
  }

  // --------------------------------------------------------- Test Methods

  public void clientBehaviorRendererDecodeNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    FacesContext context = getFacesContext();
    ClientBehavior cb = new AjaxBehavior();
    UIComponent comp = new UIForm();
    ClientBehaviorRenderer cbr = new TCKcbr();

    // null FacesContext
    JSFTestUtil
        .checkForNPE(
            cbr, "decode", new Class<?>[] { FacesContext.class,
                UIComponent.class, ClientBehavior.class },
            new Object[] { null, comp, cb }, pw);

    // null UIComponent
    JSFTestUtil
        .checkForNPE(
            cbr, "decode", new Class<?>[] { FacesContext.class,
                UIComponent.class, ClientBehavior.class },
            new Object[] { context, null, cb }, pw);

    // null ClientBehavior
    JSFTestUtil
        .checkForNPE(cbr, "decode",
            new Class<?>[] { FacesContext.class, UIComponent.class,
                ClientBehavior.class },
            new Object[] { context, comp, null }, pw);

  }

  private class TCKcbr extends ClientBehaviorRenderer {

  }
}
