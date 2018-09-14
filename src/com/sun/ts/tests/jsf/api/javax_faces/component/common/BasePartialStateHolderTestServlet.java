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
package com.sun.ts.tests.jsf.api.javax_faces.component.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.component.PartialStateHolder;
import javax.faces.component.UIComponent;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

/**
 * <p>
 * Base test Servlet for the {@link PartialStateHolder} interface.
 * </p>
 */
public abstract class BasePartialStateHolderTestServlet
    extends BaseStateHolderTestServlet {

  // ------------------------------------------- Public Methods
  /**
   * <p>
   * Initialize this <code>Servlet</code>.
   * </p>
   * 
   * @param config
   *          this <code>Servlet</code>'s configuration
   * @throws ServletException
   *           if initialization fails
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ------------------------------------------ PartialStateHolder Test Methods

  // .markInitialState()
  // .initialStateMarked()
  // .clearInitialState()
  public void partialStateHolderMICStateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();

    UIComponent comp = createComponent();
    boolean state;

    // state should be set to 'true' after this call is made.
    comp.markInitialState();
    state = comp.initialStateMarked();

    if (!state) {
      out.println(JSFTestUtil.FAIL
          + " Expected State to be true after BehaviorBase.Base.markInitialState()"
          + " Hed been called!");
    } else {
      comp.clearInitialState();
      state = comp.initialStateMarked();

      if (state) {
        out.println(JSFTestUtil.FAIL
            + " Expected State to be false after BehaviorBase.Base.clearState()"
            + " Hed been called!");
      } else {
        out.println(JSFTestUtil.PASS);
      }
    }

  } // END behaviorMICInitialStateTest

}
