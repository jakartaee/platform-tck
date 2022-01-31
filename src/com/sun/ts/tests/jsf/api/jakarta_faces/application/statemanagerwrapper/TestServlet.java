/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsf.api.jakarta_faces.application.statemanagerwrapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Locale;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import jakarta.faces.FactoryFinder;
import jakarta.faces.application.StateManager;
import jakarta.faces.application.StateManagerWrapper;
import jakarta.faces.component.UIInput;
import jakarta.faces.component.UIOutput;
import jakarta.faces.component.UIViewRoot;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import jakarta.faces.render.RenderKit;
import jakarta.faces.render.RenderKitFactory;
import jakarta.faces.validator.LengthValidator;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestServlet extends HttpTCKServlet {

  /**
   * <p>
   * Initializes this {@link jakarta.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws jakarta.servlet.ServletException
   *           if an error occurs
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ---------------------------------------------------------------- Test
  // Methods

  // Validation of return value will be performed on the client side.
  public void stateManagerIsSavingStateInClientTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    StateManager manager = new SimpleStateManagerWrapper(
        getApplication().getStateManager());
    out.println(manager.isSavingStateInClient(getFacesContext()));
  }


  // ----------------------------------------------------------- Inner Classes

  private static class SimpleStateManagerWrapper extends StateManagerWrapper {

    StateManager manager;

    // -------------------------------------------------------- Constructors

    SimpleStateManagerWrapper(StateManager manager) {

      super(manager);
      if (manager == null) {
        throw new IllegalArgumentException(
            "StateManager argument cannot be null.");
      }
      this.manager = manager;

    } // END SimpleStateManagerWrapper

    // ------------------------------------ Methods from StateManagerWrapper

    public StateManager getWrapped() {

      return manager;

    } // END getWrapped

  } // END SimpleStateManagerWrapper

}
