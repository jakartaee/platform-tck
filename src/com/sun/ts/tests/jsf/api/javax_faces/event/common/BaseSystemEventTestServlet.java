/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id:
 */
package com.sun.ts.tests.jsf.api.javax_faces.event.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.application.Application;
import javax.faces.event.FacesListener;
import javax.faces.event.SystemEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

/**
 * This class mirrors "javax.faces.event.SystemEvent"
 * 
 */
public abstract class BaseSystemEventTestServlet extends HttpTCKServlet {

  private Application app;

  private SystemEvent se = null;

  private String eventName = null;

  // --------------------------------------------------------- abstract methods
  /**
   * <p>
   * Creates a new {@link SystemEvent} instance.
   * </p>
   *
   * @return a new {@link SystemEvent} instance.
   */
  protected abstract SystemEvent createEvent(Object src);

  // ---------------------------------------------------------- private methods
  private void setupEvent() {
    app = getFacesContext().getApplication();
    se = createEvent(app);
    eventName = se.getClass().getName();
  }

  // ------------------------------------------------------------- test methods
  public void systemEventIsAppropriateListenerPostiveTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    // make sure the return value is true if SystemEventListener is
    // passed as a parameter to isAppropriateListener.

    FacesListener testListener = new TestSystemEventListener();

    this.setupEvent();

    if (se.isAppropriateListener(testListener)) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println("Test FAILED. " + eventName + ".isAppropriateListener "
          + "did not return true when ActionListener was passed in "
          + "as a parameter");
    }
  }

  public void systemEventIsAppropriateListenerNegativeTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    // make sure the return value is false if valuechangeListener is passed
    // as a parameter to isAppropriateListener.

    FacesListener testListener = new TestValueChangeListener();

    this.setupEvent();

    if (!(se.isAppropriateListener(testListener))) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println("Test FAILED. " + eventName + ".isAppropriateListener "
          + "did not return false when ValueChangeListener was "
          + "passed in as a parameter");
    }
  }

  public void systemEventProcessListenerTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    // make sure the SystemEventListener.processAction() is called from
    // ActionEvent.processListener.

    TestSystemEventListener testListener = new TestSystemEventListener();

    this.setupEvent();

    se.processListener(testListener);
    if ((testListener.getActionString()).equals("success")) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println(JSFTestUtil.FAIL + " ActionEvent.processListener"
          + " did not invoke processAction on the input listener. ");
    }
  }

}// End BaseSystemEventTestServlet
