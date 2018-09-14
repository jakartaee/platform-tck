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
package com.sun.ts.tests.jsf.api.javax_faces.event.exceptionqueuedeventcontext;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.event.PhaseId;

public class TestServlet extends HttpTCKServlet {

  private FacesContext fcontext;

  private UIComponent uic = null;

  private PhaseId pid;

  // --------------------------------------------------------- private methods
  private void setupEvent() {
    fcontext = getFacesContext();
    uic = new UIMessage();
    pid = PhaseId.ANY_PHASE;
  }

  // ------------------------------------------------------------- test
  // methods

  // public ExceptionQueuedEventContext(FacesContext context, Throwable
  // thrown)
  public void exceptionQueuedEventContextCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    this.setupEvent();

    try {
      new ExceptionQueuedEventContext(fcontext, new IllegalArgumentException());
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + " Exception Thrown! when calling: "
          + JSFTestUtil.NL
          + "ExceptionQueuedEventContext(FacesContext, Throwable)");
      e.printStackTrace();
    }

  } // End exceptionQueuedEventContextCtorTest

  // ExceptionQueuedEventContext(FacesContext , Throwable, UIComponent )
  public void exceptionQueuedEventContextCtorOneTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    this.setupEvent();

    try {
      new ExceptionQueuedEventContext(fcontext, new IllegalArgumentException(),
          uic);
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + " Exception Thrown! when calling: "
          + JSFTestUtil.NL
          + "ExceptionQueuedEventContext(FacesContext, Throwable, UIComponent)");
      e.printStackTrace();
    }

  } // End exceptionQueuedEventContextCtorOneTest

  // ExceptionQueuedEventContext(FacesContext, Throwable,UIComponent, PhaseId)
  public void exceptionQueuedEventContextCtorTwoTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    this.setupEvent();

    try {
      new ExceptionQueuedEventContext(fcontext, new IllegalArgumentException(),
          uic, pid);
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + " Exception Thrown! when calling: "
          + JSFTestUtil.NL
          + "ExceptionQueuedEventContext(FacesContext, Throwable, UIComponent, PhaseId)");
      e.printStackTrace();
    }

  } // End exceptionQueuedEventContextCtorTwoTest

  // public ExceptionQueuedEventContext.getAttributes()
  public void exceptionQueuedEventContextgetAttributesTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    this.setupEvent();

    ExceptionQueuedEventContext eec = new ExceptionQueuedEventContext(fcontext,
        new IllegalArgumentException());

    if (eec == null) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ExceptionQueuedEventContext(FacesContext context, "
          + "Throwable thrown) is null");
    } else if (!(eec.getAttributes() instanceof java.util.Map)) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL + "Expected: java.util.Map"
          + JSFTestUtil.NL + "Recieved: "
          + eec.getAttributes().getClass().getName());
    } else {
      pw.println(JSFTestUtil.PASS);
    }

  }

  // public ExceptionQueuedEventContext.getContext()
  public void exceptionQueuedEventContextgetContextTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    this.setupEvent();

    ExceptionQueuedEventContext eec = new ExceptionQueuedEventContext(fcontext,
        new IllegalArgumentException());

    if (eec == null) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ExceptionQueuedEventContext(FacesContext context, "
          + "Throwable thrown) is null");
    } else if (eec.getContext() != fcontext) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "getContext returned wrong FacesContext!");
    } else {
      pw.println(JSFTestUtil.PASS);
    }

  }

  // public ExceptionQueuedEventContext.getException()
  public void exceptionQueuedEventContextgetExceptionTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    this.setupEvent();
    IllegalArgumentException iae = new IllegalArgumentException();

    ExceptionQueuedEventContext eec = new ExceptionQueuedEventContext(fcontext,
        iae);

    if (eec == null) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ExceptionQueuedEventContext(FacesContext context, "
          + "Throwable thrown) is null");
    } else if (eec.getException() != iae) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "getContext returned wrong FacesContext!");
    } else {
      pw.println(JSFTestUtil.PASS);
    }

  }

  // public ExceptionQueuedEventContext.getComponent()
  public void exceptionQueuedEventContextgetComponentTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    this.setupEvent();

    ExceptionQueuedEventContext eec = new ExceptionQueuedEventContext(fcontext,
        new IllegalArgumentException(), uic);

    if (eec == null) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ExceptionQueuedEventContext(FacesContext context, "
          + "Throwable thrown, UIComponent component) is null");
    } else if (eec.getComponent() != uic) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL + "Expected: "
          + uic.getClass().getName() + JSFTestUtil.NL + "Receivved: "
          + eec.getComponent().getClass().getName());
    } else {
      pw.println(JSFTestUtil.PASS);
    }

  }

  // public ExceptionQueuedEventContext.getComponent()
  public void exceptionQueuedEventContextgetComponentNullTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    this.setupEvent();

    ExceptionQueuedEventContext eec = new ExceptionQueuedEventContext(fcontext,
        new IllegalArgumentException());

    if (eec == null) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ExceptionQueuedEventContext(FacesContext context, "
          + "Throwable thrown, UIComponent component) is null");
    } else if (eec.getComponent() != null) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Expected getComponent to return null" + JSFTestUtil.NL
          + "Receeivved: " + eec.getComponent());
    } else {
      pw.println(JSFTestUtil.PASS);
    }

  }

  // public ExceptionQueuedEventContext.getPhaseId()
  public void exceptionQueuedEventContextgetPhaseIdNullTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    this.setupEvent();

    ExceptionQueuedEventContext eec = new ExceptionQueuedEventContext(fcontext,
        new IllegalArgumentException(), uic);

    if (eec == null) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ExceptionQueuedEventContext(FacesContext context, "
          + "Throwable thrown, UIcomponent component) is null");
    } else if (eec.getPhaseId() != null) {
      pw.println(JSFTestUtil.FAIL + ": getComponent returned incorrect "
          + "component!" + JSFTestUtil.NL + "Expected: null" + JSFTestUtil.NL
          + "Received: " + eec.getPhaseId().toString());
    } else {
      pw.println(JSFTestUtil.PASS);
    }

  }

  // public ExceptionQueuedEventContext.getPhaseId()
  public void exceptionQueuedEventContextgetPhaseIdTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    this.setupEvent();

    ExceptionQueuedEventContext eec = new ExceptionQueuedEventContext(fcontext,
        new IllegalArgumentException(), uic, pid);

    if (eec == null) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "ExceptionQueuedEventContext(FacesContext context, "
          + "Throwable thrown, UIComponent component, "
          + "PhaseId phaseid) is null");
    } else if (eec.getPhaseId() != pid) {
      pw.println(JSFTestUtil.FAIL + ": getComponent returned incorrect "
          + "component!" + JSFTestUtil.NL + "Expected: " + pid.toString()
          + JSFTestUtil.NL + "Received: " + eec.getPhaseId().toString());
    } else {
      pw.println(JSFTestUtil.PASS);
    }

  }
}
