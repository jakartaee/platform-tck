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
package com.sun.ts.tests.jsf.api.javax_faces.view.statemanagementstrategy;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import javax.faces.view.StateManagementStrategy;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.api.javax_faces.view.common.TCKViewRoot;
import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends HttpTCKServlet {
  private static final String JSP_VIEWID = "/root.jsp";

  private static final String FACELETS_VIEWID = "/root.xhtml";

  /**
   * <code>init</code> initializes the servlet.
   * 
   * @param config
   *          - <code>ServletConfig</code>
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ------------------------------------------- Test Methods

  public void stateMgmtStrategyNonNullTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    StateManagementStrategy statestrategy = this.getStateMgtmStrat(context,
        FACELETS_VIEWID);

    if (statestrategy == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "StateManagementStrategy Must be non-null for Facelet Views!");

    } else {
      out.println(JSFTestUtil.PASS);

    }

  }// End stateMgmtStrategyNonNullTest

  public void stateMgmtStratNullForJSPTest(HttpServletRequest request,
      HttpServletResponse response) throws IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    StateManagementStrategy statestrategy = this.getStateMgtmStrat(context,
        JSP_VIEWID);

    if (statestrategy == null) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "StateManagementStrategy Must be null for JSP Views!");
    }

  }// End stateMgmtStratNullForJSPTest

  // --------------------------------------------- private methods

  private StateManagementStrategy getStateMgtmStrat(FacesContext context,
      String viewId) {

    StateManagementStrategy stmgt = context.getApplication().getViewHandler()
        .getViewDeclarationLanguage(context, viewId)
        .getStateManagementStrategy(context, viewId);

    return stmgt;
  }

}
