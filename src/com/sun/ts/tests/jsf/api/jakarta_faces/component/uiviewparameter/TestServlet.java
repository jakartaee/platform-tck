/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.api.jakarta_faces.component.uiviewparameter;

import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.jsf.api.jakarta_faces.component.common.BaseComponentTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.component.UIViewParameter;
import jakarta.faces.context.FacesContext;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestServlet extends BaseComponentTestServlet {

  /**
   * <p>
   * Initializes this {@link jakarta.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws ServletException
   *           if an error occurs
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    setRendererType(null);
  }

  /**
   * <p>
   * Creates a new {@link UIComponent} instance.
   * </p>
   * 
   * @return a new {@link UIComponent} instance.
   */
  @Override
  protected UIComponentBase createComponent() {
    return new UIViewParameter();
  }

  // ----------------------------------------- UIViewParameter

  public void uiViewParameterIsImmediateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIViewParameter viewparam = (UIViewParameter) createComponent();

    if (viewparam.isImmediate()) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Default setting for 'isImmediate() " + " MUST be false!");

    } else {
      // UIViewparameter.isImmediate() should always return false.
      // (according to API doc.)
      viewparam.setImmediate(true);
      if (viewparam.isImmediate()) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Unxpected value returned from isImmediate()" + JSFTestUtil.NL
            + "Expected: false" + JSFTestUtil.NL + "Received: true");

      } else {
        out.println(JSFTestUtil.PASS);
      }
    }
  } // End uiViewParameterIsImmediateTest

  public void uiViewParameterGetSetNameTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    String golden = "Bytor";
    UIViewParameter viewparam = (UIViewParameter) createComponent();

    viewparam.setName(golden);

    if (golden.equals(viewparam.getName())) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(
          JSFTestUtil.FAIL + " Unexpected Value returned from getName()!"
              + JSFTestUtil.NL + "Expected: " + golden + JSFTestUtil.NL
              + "Received: " + viewparam.getName());
    }

  } // End uiViewParameterGetSetNameTest

  public void uiViewParameterGetSetSubmittedValueTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIViewParameter viewparam = (UIViewParameter) createComponent();
    String golden = "true";

    viewparam.setSubmittedValue(golden);
    String result = (String) viewparam.getSubmittedValue();

    if (!(golden.equals(result))) {
      out.println(JSFTestUtil.FAIL
          + " Value returned by getSubmittedValue() was not the value set by "
          + "setSubmittedValue()." + JSFTestUtil.NL + "Expected: " + golden
          + JSFTestUtil.NL + "Received: " + result);
    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // End uiViewParameterGetSetSubmittedValueTest

  public void uiViewParameterGetStringValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    FacesContext context = getFacesContext();
    UIViewParameter viewparam = (UIViewParameter) createComponent();
    String golden = "true";

    viewparam.setValue(golden);
    String result = viewparam.getStringValue(context);

    if (!(golden.equals(result))) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Wrong value returned by getStringValue()!" + JSFTestUtil.NL
          + JSFTestUtil.NL + "Expected: " + golden + JSFTestUtil.NL
          + "Received: " + result);
    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // End uiViewParameterGetStringValueTest

} // End TestServlet
