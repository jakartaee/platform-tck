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

package com.sun.ts.tests.jsf.api.javax_faces.event.valuechangeevent;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.event.ValueChangeEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet extends HttpTCKServlet {

  public void valueChangeEventCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    if (uic != null) {
      ValueChangeEvent ae = new ValueChangeEvent(uic, "oldvalue", "newvalue");
      if (ae == null) {
        pw.println(JSFTestUtil.FAIL + " ValueChangeEvent(UIComponent, String, "
            + "String) returned null.");
      } else {
        pw.println(JSFTestUtil.PASS);
      }
    }
  }

  public void valueChangeEventCtorIllegalArgumentExceptionTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      new ValueChangeEvent(null, "oldvalue", "newvalue");
      pw.println("Error:  The constructor of ValueChangeEvent should "
          + "have thrown an IllegalArgumentException when the "
          + "provided component was null.  No exception was thrown.");
    } catch (IllegalArgumentException iae) {
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println("Exception thrown, but was not an instance of "
          + "IllegalArgumentException.");
      e.printStackTrace();
    }
  }

  public void valueChangeEventGetOldValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    if (uic != null) {
      String expected = "oldvalue";
      ValueChangeEvent vce = new ValueChangeEvent(uic, expected, "newvalue");
      String val = (String) vce.getOldValue();
      if (!expected.equals(val)) {
        pw.println(
            JSFTestUtil.FAIL + ": ValueChangeEvent.getOldValue() did not "
                + "return the correct result");
        pw.println("Expected: " + expected);
        pw.println("actual: " + val);
      } else {
        pw.println(JSFTestUtil.PASS);
      }
    } else {
      pw.println(JSFTestUtil.FAIL + " Unable to obtain UIComponent instance.");
    }
  }

  public void valueChangeEventGetNewValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    if (uic != null) {
      String expected = "newvalue";
      ValueChangeEvent vce = new ValueChangeEvent(uic, "oldvalue", expected);
      String val = (String) vce.getNewValue();
      if (!expected.equals(val)) {
        pw.println(
            JSFTestUtil.FAIL + ": ValueChangeEvent.getNewValue() did not "
                + "return the correct result");
        pw.println("Expected: " + expected);
        pw.println("actual: " + val);
      } else {
        pw.println(JSFTestUtil.PASS);
      }
    } else {
      pw.println(JSFTestUtil.FAIL + " Unable to obtain UIComponent instance.");
    }
  }

  public void valueChangeEventGetComponentTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    UIComponent uic = getApplication().createComponent(UIOutput.COMPONENT_TYPE);
    ValueChangeEvent vce = new ValueChangeEvent(uic, "old", "new");
    if (uic == vce.getComponent()) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println(JSFTestUtil.FAIL + " ValueChangeEvent.getComponent() didn't "
          + "return the same UIComponent provided to its constructor.");
    }
  }
}
