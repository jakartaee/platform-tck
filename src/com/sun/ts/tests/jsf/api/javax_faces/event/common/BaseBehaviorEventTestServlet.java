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
package com.sun.ts.tests.jsf.api.javax_faces.event.common;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import java.io.IOException;
import java.io.PrintWriter;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.Behavior;
import javax.faces.event.BehaviorEvent;
import javax.faces.event.FacesListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseBehaviorEventTestServlet extends HttpTCKServlet {

  private BehaviorEvent be = null;

  private String eventName = null;

  // --------------------------------------------------------- abstract methods
  /**
   * <p>
   * Creates a new {@link BehaviorEvent} instance.
   * </p>
   *
   * @return a new {@link BehaviorEvent} instance.
   */
  protected abstract BehaviorEvent createEvent(UIComponent component,
      Behavior behavior);

  /**
   * <p>
   * Creates a new {@link Behavior} instance.
   * </p>
   *
   * @return a new {@link Behavior} instance.
   */
  protected abstract Behavior getTestBehavior();

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
    be = createEvent(getTestComponent(), getTestBehavior());
    eventName = be.getClass().getName();
  }

  // ------------------------------------------------------------- test methods
  public void behaviorEventCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    this.setupEvent();

    if (be == null) {
      pw.println("Test FAILED. " + eventName + "(UIComponent, Behavior) "
          + "returned null");
    } else {
      pw.println(JSFTestUtil.PASS);
    }
  }// -- End behaviorEventCtorTest

  public void behaviorEventIAETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    String pass = "no";
    // UIComponent null
    try {
      createEvent(null, getTestBehavior());
      pw.println(JSFTestUtil.FAIL + " The constructor for " + eventName
          + " should have thrown an IllegalArgumentException when "
          + "provided a null UIComponent.  No Exception was thrown.");
    } catch (IllegalArgumentException iae) {
      pass = "yes";

    } catch (Exception e) {
      pw.println("Test FAILED. Wrong exception thrown!" + JSFTestUtil.NL
          + "Expected: IllegalArgumentException");
      e.printStackTrace();
    }

    // Behavior null
    try {
      createEvent(this.getTestComponent(), null);
      pw.println(JSFTestUtil.FAIL + " The constructor for " + eventName
          + " should have thrown an IllegalArgumentException when "
          + "provided a null behavior.  No Exception was thrown.");
    } catch (IllegalArgumentException iae) {
      pass = pass + "yes";

    } catch (Exception e) {
      pw.println("Test FAILED. Wrong exception thrown!" + JSFTestUtil.NL
          + "Expected: IllegalArgumentException");
      e.printStackTrace();
    }

    if ("yesyes".equals(pass)) {
      pw.println("Test PASSED.");
    }
  }// -- End behaviorEventIAETest

  public void behaviorEventGetBehaviorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    this.setupEvent();
    Behavior exp = getTestBehavior();
    Behavior result = be.getBehavior();
    if (!exp.equals(result)) {
      pw.println("Test FAILED. Unexpected result when calling " + eventName
          + ".getBehavior()." + JSFTestUtil.NL + "Expected: " + exp
          + JSFTestUtil.NL + "Received: " + result);
    } else {
      pw.println(JSFTestUtil.PASS);
    }
  }// -- End behaviorEventGetBehaviorTest

  public void behaviorEventIsApproiateListenerPosTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    // make sure the return value is true if BahaviorListener is
    // passed as a parameter to isAppropriateListener.

    FacesListener testListener = new TestAjaxBehaviorListener();

    this.setupEvent();

    if (be.isAppropriateListener(testListener)) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println("Test FAILED. " + eventName + ".isAppropriateListener "
          + "did not return true when BehaviorListener was passed in "
          + "as a parameter");
    }
  }

  public void behaviorEventIsApproiateListenerNegTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    // make sure the return value is false if valuechangeListener is passed
    // as a parameter to isAppropriateListener.

    FacesListener testListener = new TestValueChangeListener();

    this.setupEvent();

    if (!(be.isAppropriateListener(testListener))) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println("Test FAILED. " + eventName + ".isAppropriateListener "
          + "did not return false when ValueChangeListener was "
          + "passed in as a parameter");
    }
  }

  public void behaviorEventProcessListenerTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    // make sure the SystemEventListener.processAction() is called from
    // ActionEvent.processListener.

    TestAjaxBehaviorListener testListener = new TestAjaxBehaviorListener();

    this.setupEvent();

    be.processListener(testListener);
    if ((testListener.getActionString()).equals("success")) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println(JSFTestUtil.FAIL + " FacesEvent.processListener"
          + " did not invoke processAction on the input listener. ");
    }
  }
}
