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
package com.sun.ts.tests.jsf.api.jakarta_faces.validator.common;

import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import jakarta.faces.component.StateHolder;
import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.Validator;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * <p>
 * Base test Servlet for the {@link StateHolder} interface.
 * </p>
 */
public abstract class BaseStateHolderTestServlet extends HttpTCKServlet {

  // ----------------------------------------------- Public Methods
  /**
   * <p>
   * Initializes this {@link jakarta.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws ServletException
   *           if an error occurs
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ----------------------------------------------- Abstract Methods
  /**
   * <p>
   * Creates a new {@link UIComponent} instance.
   * </p>
   * 
   * @return a new {@link UIComponent} instance.
   */
  protected abstract Validator createValidator();

  // ----------------------------------------------- Test Methods
  // StateHolder.isTransient(), StateHolder.setTransient();
  public void stateHolderIsSetTransientTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    StateHolder holder;
    if (createValidator() instanceof StateHolder) {
      holder = (StateHolder) createValidator();
    } else {
      out.println("The Specific Validator that you are trying to test "
          + "does not implement the StateHolder interface!");
      return;
    }

    holder.setTransient(false);

    if (holder.isTransient()) {
      out.println(JSFTestUtil.FAIL + " Expected isTransient() to return"
          + " false after having explicitly setting it as such via"
          + " setTransient().");
      return;
    }

    holder.setTransient(true);

    if (!holder.isTransient()) {
      out.println(JSFTestUtil.FAIL + " Expected isTransient() to return true"
          + " after having explicitly setting it as such via"
          + " setTransient().");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void stateHolderRestoreStateNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    StateHolder holder;
    if (createValidator() instanceof StateHolder) {
      holder = (StateHolder) createValidator();
    } else {
      pw.println("The Specific Validator that you are trying to test "
          + "does not implement the StateHolder interface!");
      return;
    }

    // Test for null FacesContext throws NPE
    JSFTestUtil.checkForNPE(holder, "restoreState",
        new Class<?>[] { FacesContext.class, Object.class },
        new Object[] { null, "abc" }, pw);

  }

  public void stateHolderSaveStateNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    StateHolder holder;
    if (createValidator() instanceof StateHolder) {
      holder = (StateHolder) createValidator();
    } else {
      pw.println("The Specific Validator that you are trying to test "
          + "does not implement the StateHolder interface!");
      return;
    }

    // Test for null FacesContext throws NPE
    JSFTestUtil.checkForNPE(holder, "saveState",
        new Class<?>[] { FacesContext.class }, new Object[] { null }, pw);

  }
}
