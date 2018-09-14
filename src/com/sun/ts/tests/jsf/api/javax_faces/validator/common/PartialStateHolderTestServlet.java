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
package com.sun.ts.tests.jsf.api.javax_faces.validator.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.component.PartialStateHolder;
import javax.faces.component.StateHolder;
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
public abstract class PartialStateHolderTestServlet
    extends BaseStateHolderTestServlet {

  // ----------------------------------------------------------- Public
  // Methods
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
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ----------------------------------------------------------- Test Methods
  public void validatorPartialStateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    PartialStateHolder psh;
    if (createValidator() instanceof PartialStateHolder) {
      psh = (PartialStateHolder) createValidator();
    } else {
      out.println("The Specific Validator that you are trying to test "
          + "does not implement the PartialStateHolder interface!");
      return;
    }

    boolean result;

    // Expect state is 'true'.
    psh.markInitialState();
    result = psh.initialStateMarked();
    if (result) {
      // do nothing test status still true.
    } else {
      out.println("Test FAILED." + JSFTestUtil.NL
          + "Unexpected state returned when calling "
          + "initialStateMarked after markInitialState() was " + "called"
          + JSFTestUtil.NL + "Expected: true" + JSFTestUtil.NL + "Received: "
          + result);
      return;
    }

    // Expect state to be 'false'
    psh.clearInitialState();
    result = psh.initialStateMarked();
    if (!result) {
      // do nothing test status still true.
    } else {
      out.println("Test FAILED." + JSFTestUtil.NL
          + "Unexpected state returned when calling "
          + "IntialStateMarked() after clearInitialState() was " + "called"
          + JSFTestUtil.NL + "Expected: false" + JSFTestUtil.NL + "Received: "
          + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }
}
