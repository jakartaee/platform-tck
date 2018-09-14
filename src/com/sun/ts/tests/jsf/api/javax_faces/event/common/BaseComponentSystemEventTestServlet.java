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
 * $Id$
 */
package com.sun.ts.tests.jsf.api.javax_faces.event.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.component.UIComponent;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.FacesListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

/**
 * This class mirrors "javax.faces.event.ComponentSystemEvent"
 * 
 */
public abstract class BaseComponentSystemEventTestServlet
    extends HttpTCKServlet {

  private ComponentSystemEvent cse = null;

  private String eventName = null;

  private UIComponent uic = null;

  // --------------------------------------------------------- abstract methods

  /**
   * <p>
   * Creates a new {@link ComponentSystemEvent} instance.
   * </p>
   *
   * @return a new {@link ComponentSystemEvent} instance.
   */
  protected abstract ComponentSystemEvent createEvent(UIComponent component);

  /**
   * <p>
   * Creates a new {@link UIcomponent} instance.
   * </p>
   *
   * @return a new {@link UIComponent} instance.
   */
  protected abstract UIComponent getTestComponent();

  // ---------------------------------------------------------- private methods

  private void setupEvent() {
    uic = getTestComponent();
    cse = createEvent(uic);
    eventName = cse.getClass().getName();
  }

  // ------------------------------------------------------------- test methods

  public void componentSystemEventCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    this.setupEvent();

    if (cse == null) {
      pw.println(
          JSFTestUtil.FAIL + eventName + "(UIComponent) " + "returned null");
    } else {
      pw.println(JSFTestUtil.PASS);
    }

  }

  public void componentSystemEventIAETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      createEvent(null);
      pw.println(JSFTestUtil.FAIL + " The constructor for " + eventName
          + " should have thrown an IllegalArgumentException when "
          + "provided a null component.  No Exception was thrown.");

    } catch (IllegalArgumentException iae) {
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + ": Wrong exception thrown!" + JSFTestUtil.NL
          + "Expected: IllegalArgumentException");
      e.printStackTrace();
    }
  }

  public void componentSystemEventGetComponentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    this.setupEvent();

    if (uic.equals(cse.getComponent())) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println("Test FAILED. " + eventName + ".getComponent() "
          + "didn't return the same UIComponent provided to its "
          + "constructor!" + JSFTestUtil.NL + "Expected: "
          + uic.getClass().getName() + JSFTestUtil.NL + "Received: "
          + cse.getComponent().getClass().getName());
    }
  }

  public void componentSystemEventIsApproiateListenerPostiveTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    // make sure the return value is true if ComponentSystemEventListener is
    // passed as a parameter to isAppropriateListener.

    ComponentSystemEventListener testListener = new TCKComponentSystemEventListener();

    this.setupEvent();

    if (cse.isAppropriateListener(testListener)) {
      pw.println(JSFTestUtil.PASS);

    } else {
      pw.println("Test FAILED. " + eventName + ".isAppropriateListener "
          + "did not return true when ComponentSystemEventListener was passed in "
          + "as a parameter");
    }
  }

  public void componentSystemEventIsApproiateListenerNegativeTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    // make sure the return value is false if valuechangeListener is passed
    // as a parameter to isAppropriateListener.

    FacesListener testListener = new TestValueChangeListener();

    this.setupEvent();

    if (!(cse.isAppropriateListener(testListener))) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println("Test FAILED. " + eventName + ".isAppropriateListener "
          + "did not return false when ValueChangeListener was "
          + "passed in as a parameter");
    }
  }

  public void componentSystemEventProcessListenerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    // make sure the SystemEventListener.processAction() is called from
    // ActionEvent.processListener.

    TCKComponentSystemEventListener testListener = new TCKComponentSystemEventListener();

    this.setupEvent();

    cse.processListener(testListener);
    if ((testListener.getActionString()).equals("success")) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println(JSFTestUtil.FAIL + " ActionEvent.processListener"
          + " did not invoke processAction on the input listener. ");
    }
  }
}
