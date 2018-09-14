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
import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.behavior.BehaviorBase;
import javax.faces.event.BehaviorEvent;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.beans.TestBean;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public abstract class BaseBehaviorTestServlet
    extends ClientBehaviorBaseTestServlet {

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

    servletContext = config.getServletContext();
    super.init(config);

  } // init

  // --------------------- Test methods ------------------

  // BehaviorBase.broadast(BehaviorListener) throws NullPointerException
  public void behaviorBroadcastNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(createBehavior().getClass(), "broadcast",
        new Class<?>[] { BehaviorEvent.class }, new Object[] { null }, out);

  } // END behaviorBroadcastNPETest

  // BehaviorBase.markInitialState()
  // BehaviorBase.initialStateMarked()
  // BehaviorBase.clearInitialState()
  public void behaviorMICInitialStateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    BehaviorBase cb = createBehavior();
    boolean state;

    // state should be set to 'true' after this call is made.
    cb.markInitialState();
    state = cb.initialStateMarked();

    if (!state) {
      out.println(
          JSFTestUtil.FAIL + JSFTestUtil.NL + "Expected State to be true after "
              + "BehaviorBase.Base.markInitialState() Hed been called!");
    } else {
      cb.clearInitialState();
      state = cb.initialStateMarked();

      if (state) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Expected State to be false after "
            + "BehaviorBase.Base.clearState() Hed been called!");
      } else {
        out.println(JSFTestUtil.PASS);
      }
    }

  } // END behaviorMICInitialStateTest

  // BehaviorBase.isTransient()
  // BehaviorBase.setTransient()
  public void behaviorSITransientTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    BehaviorBase cb = createBehavior();
    boolean trans;

    cb.setTransient(true);
    trans = cb.isTransient();
    if (!trans) {
      out.println(
          JSFTestUtil.FAIL + JSFTestUtil.NL + "Unexpected value returned from "
              + "BehaviorBase.Base.isTransient()!" + JSFTestUtil.NL
              + "Expected: true" + JSFTestUtil.NL + "Received: " + trans);
    } else {
      cb.setTransient(false);
      trans = cb.isTransient();

      if (trans) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Unexpected value returned from "
            + "BehaviorBase.Base.isTransient()!" + JSFTestUtil.NL
            + "Expected: false" + JSFTestUtil.NL + "Received: " + trans);
      } else {
        out.println(JSFTestUtil.PASS);
      }
    }

  } // END behaviorMICInitialStateTest
}
