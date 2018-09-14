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
package com.sun.ts.tests.jsf.api.javax_faces.event.actionevent;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.event.ActionListener;
import javax.faces.event.FacesListener;
import javax.faces.event.ValueChangeListener;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet extends HttpTCKServlet {

  public void actionEventCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    try {
      new ActionEvent(new UICommand());
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + ": Unexpected Exception thrown.");
      e.printStackTrace();
    }
  }

  public void actionEventCtorIllegalArgumentExceptionTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      new ActionEvent(null);
      pw.println(JSFTestUtil.FAIL + " The constructor of ActionEvent should "
          + "have thrown an IllegalArgumentException when provided a "
          + "null component.  No Exception was thrown.");

    } catch (IllegalArgumentException iae) {
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + ": Exception thrown, but was not an "
          + "instance of IllegalArgumentException.");
      e.printStackTrace();
    }
  }

  public void actionEventGetComponentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    UIComponent uic = new UICommand();
    ActionEvent ae = new ActionEvent(uic);
    if (uic.equals(ae.getComponent())) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println(JSFTestUtil.FAIL + " ActionEvent.getComponent() didn't "
          + "return the same UIComponent provided to its constructor.");
    }
  }

  public void actionEventIsApproiateListenerPostiveTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    // make sure the return value is true if ActionListener is passed as
    // a parameter to isAppropriateListener.
    UIComponent uic = new UICommand();
    FacesListener testListener = new TestActionListener();
    ActionEvent ae = new ActionEvent(uic);
    if (ae.isAppropriateListener(testListener)) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println(JSFTestUtil.FAIL + " ActionEvent.isAppropriateListener"
          + " did not return true when ActionListener was passed in "
          + "as a parameter");
    }
  }

  public void actionEventIsApproiateListenerNegativeTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    // make sure the return value is false if valuechangeListener is passed as
    // a parameter to isAppropriateListener.
    UIComponent uic = new UICommand();
    FacesListener testListener = new TestValueChangeListener();
    ActionEvent ae = new ActionEvent(uic);
    if (!(ae.isAppropriateListener(testListener))) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println(JSFTestUtil.FAIL + " ActionEvent.isAppropriateListener"
          + " did not return false when ValueChangeListener was "
          + "passed in as a parameter");
    }
  }

  public void actionEventProcessListenerTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    // make sure the ActionListener.processAction() is called from
    // ActionEvent.processListener.
    UIComponent uic = new UICommand();
    TestActionListener testListener = new TestActionListener();
    ActionEvent ae = new ActionEvent(uic);
    ae.processListener(testListener);

    if (("success".equals(testListener.getActionString()))) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println(JSFTestUtil.FAIL + " ActionEvent.processListener"
          + " did not invoke processAction on the input listener. ");
    }
  }

  private static class TestActionListener implements ActionListener {

    String action = null;

    public TestActionListener() {
      super();
    }

    public void processAction(ActionEvent event) {
      action = "success";
      System.out.println("Processed Action");
    }

    public String getActionString() {
      return action;
    }
  }

  private static class TestValueChangeListener implements ValueChangeListener {

    public TestValueChangeListener() {
      super();
    }

    public void processValueChange(ValueChangeEvent event) {
      System.out.println("Processed Value Change");
    }
  }
}
