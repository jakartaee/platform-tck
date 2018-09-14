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
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.FacesListener;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class BaseExceptionQueuedEventTestServlet
    extends HttpTCKServlet {

  private ExceptionQueuedEventContext eqeContext;

  private ExceptionQueuedEvent eqe = null;

  private String eventName = null;

  // --------------------------------------------------------- abstract methods
  /**
   * <p>
   * Creates a new {@link ExceptionQueuedEvent} instance.
   * </p>
   *
   * @return a new {@link ExceptionQueuedEvent} instance.
   */
  protected abstract ExceptionQueuedEvent createEvent(
      ExceptionQueuedEventContext exceptionqueuedeventcontext);

  /**
   * <p>
   * Creates a new {@link ExceptionQueuedEvent} instance.
   * </p>
   *
   * @return a new {@link ExceptionQueuedEventContext} instance.
   */
  protected abstract ExceptionQueuedEventContext getExceptionQueuedEventContext();

  // ---------------------------------------------------------- private methods
  private void setupEvent() {

    eqeContext = getExceptionQueuedEventContext();
    eqe = createEvent(eqeContext);
    eventName = eqe.getClass().getName();
  }

  // ------------------------------------------------------------ tests methods
  public void exceptionQueuedEventCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    this.setupEvent();

    if (eqe == null) {
      pw.println(JSFTestUtil.FAIL + eventName
          + "(ExceptionQueuedEventContext) returned null");
    } else {
      pw.println(JSFTestUtil.PASS);
    }

  }

  public void exceptionQueuedEventGetContextTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    this.setupEvent();

    if (eqeContext.equals(eqe.getContext())) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println("Test FAILED. " + eventName + ".getContext() "
          + "didn't return the same UIComponent provided to its "
          + "constructor!" + JSFTestUtil.NL + "Expected: "
          + eqeContext.getClass().getName() + JSFTestUtil.NL + "Received: "
          + eqe.getContext().getClass().getName());
    }
  }

  public void exceptionQueuedEventIsApproiateListenerPostiveTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    // make sure the return value is true if SystemEventListener is
    // passed as a parameter to isAppropriateListener.

    FacesListener testListener = new TestSystemEventListener();

    this.setupEvent();

    if (eqe.isAppropriateListener(testListener)) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println("Test FAILED. " + eventName + ".isAppropriateListener "
          + "did not return true when ActionListener was passed in "
          + "as a parameter");
    }
  }

  public void exceptionQueuedEventIsApproiateListenerNegativeTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    // make sure the return value is false if valuechangeListener is passed
    // as a parameter to isAppropriateListener.

    FacesListener testListener = new TestValueChangeListener();

    this.setupEvent();

    if (!(eqe.isAppropriateListener(testListener))) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println("Test FAILED. " + eventName + ".isAppropriateListener "
          + "did not return false when ValueChangeListener was "
          + "passed in as a parameter");
    }
  }

  public void exceptionQueuedEventProcessListenerTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    // make sure the SystemEventListener.processAction() is called from
    // ActionEvent.processListener.

    TestSystemEventListener testListener = new TestSystemEventListener();

    this.setupEvent();

    eqe.processListener(testListener);
    if ((testListener.getActionString()).equals("success")) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println(JSFTestUtil.FAIL + " ExceptionQueuedEvent.processListener"
          + " did not invoke processAction on the input listener. ");
    }
  }
}
