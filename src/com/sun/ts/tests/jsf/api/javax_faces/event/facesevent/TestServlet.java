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

package com.sun.ts.tests.jsf.api.javax_faces.event.facesevent;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends HttpTCKServlet {

  public void facesEventCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    if (uic != null) {

      FacesEvent fe = new TCKFacesEvent(uic);
      if (fe == null) {
        pw.println(
            JSFTestUtil.FAIL + ": FacesEvent(UIComponent) returned null");
      } else {
        pw.println(JSFTestUtil.PASS);
      }
    } else {
      pw.println(JSFTestUtil.FAIL + " Unable to obtain UIComponent instance.");
    }
  }

  public void facesEventCtorIllegalArgumentExceptionTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      new TCKFacesEvent(null);
      pw.println(JSFTestUtil.FAIL + " The constructor of FacesEvent should "
          + "have thrown an IllegalArgumentException when provided a null "
          + "component.  No Exception was thrown.");
    } catch (IllegalArgumentException iae) {
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + ": Exception thrown, but was not an "
          + "instance of IllegalArgumentException.");
      e.printStackTrace();
    }
  }

  public void facesEventGetComponentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    UIViewRoot root = new UIViewRoot();
    FacesEvent fe = new TCKFacesEvent(uic);
    root.getChildren().add(uic);
    if (uic == fe.getComponent()) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println(JSFTestUtil.FAIL + " FacesEvent.getComponent() didn't return"
          + " the same UIComponent provided to its constructor.");
    }
  }

  public void facesEventQueueISETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter pw = response.getWriter();
    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    UIComponent root = getApplication()
        .createComponent(UIOutput.COMPONENT_TYPE);
    FacesEvent fe = new TCKFacesEvent(uic);
    root.getChildren().add(uic);

    JSFTestUtil.checkForISE(fe, "queue", new Class<?>[] {}, new Object[] {},
        pw);

  } // END facesEventQueueISETest

  public void facesEventGetSetPhaseIdTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    FacesEvent fe = new TCKFacesEvent(uic);

    fe.setPhaseId(PhaseId.ANY_PHASE);

    if (!fe.getPhaseId().equals(PhaseId.ANY_PHASE)) {
      out.println(JSFTestUtil.FAIL);

    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // END facesEventGetPhaseIdTest

  public void facesEventSetPhaseIdIAETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    FacesEvent fe = new TCKFacesEvent(uic);

    try {
      fe.setPhaseId(null);
      out.println(
          JSFTestUtil.FAIL + "Expected IllegalArgumentException to be thrown!");

    } catch (IllegalArgumentException iae) {
      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(
          JSFTestUtil.FAIL + "Exception thrown, but not the correct one!"
              + JSFTestUtil.NL + " Expected: IllegalArgumentException"
              + JSFTestUtil.NL + "Received: ");
      e.printStackTrace();
    }

  } // END facesEventSetPhaseIdIAETest

  // ------------------------------------ Private Classes

  private static class TCKFacesEvent extends FacesEvent {

    public TCKFacesEvent(UIComponent component) {
      super(component);
    }

    public boolean isAppropriateListener(FacesListener listener) {
      return false;
    }

    public void processListener(FacesListener listener) {

    }
  }

}
