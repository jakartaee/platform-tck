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
package com.sun.ts.tests.jsf.api.javax_faces.component.uioutcometarget;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIOutcomeTarget;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.BaseComponentTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends BaseComponentTestServlet {

  private List<String> rendererType;

  /**
   * <p>
   * Initializes this {@link javax.servlet.Servlet}.
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
    rendererType = new ArrayList<String>();
    rendererType.add("javax.faces.Link");
    rendererType.add("javax.faces.Button");

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
    return new UIOutcomeTarget();
  }

  // --------------------------------------------------- overridden test cases
  // UIComponent.{get,set}RendererType
  @Override
  public void uiComponentGetSetRendererTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIComponent comp = createComponent();

    // First check the default return value for the component
    // under test base on the rendererType variable. If null,
    // then expect the return type to be null, if non null,
    // make sure the values are equal.
    if (this.rendererType == null) {
      if (comp.getRendererType() != null) {
        out.println(JSFTestUtil.FAIL + " Expected getRendererType() to"
            + " return null for this component type.");
        out.println("Value received: " + comp.getRendererType());
        return;
      }
    } else {
      if (!this.rendererType.contains(comp.getRendererType())) {
        out.println(JSFTestUtil.FAIL + " Unexpected renderType returned "
            + "from getRendererType()." + JSFTestUtil.NL + "Expected"
            + rendererType.get(0) + " or " + rendererType.get(1)
            + JSFTestUtil.NL + "Received: " + comp.getRendererType());
        return;
      }
    }

    // now ensure that the value can be overridden
    comp.setRendererType("string");

    if (!"string".equals(comp.getRendererType())) {
      out.println(JSFTestUtil.FAIL + " Expected getRendererType() to"
          + " return 'string' for this component "
          + "after having explicitly set it via setRendererType()");
      out.println("Value received: " + comp.getRendererType());
      return;
    }

    // ensure we can set null
    comp.setRendererType(null);
    if (comp.getRendererType() != null) {
      out.println(JSFTestUtil.FAIL + " Expected getRendererType() to return"
          + " null after having explicitly set it via setRendererType().");
      return;
    }

    // reset the renderer type
    // comp.setRendererType(rendererType);

    out.println(JSFTestUtil.PASS);
  }

  // --------------------------------------------------------------
  // UIOutcomeTarget

  public void uiOutcomeTargetGetSetOutcomeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIOutcomeTarget outcome = (UIOutcomeTarget) createComponent();

    String golden = "frodo";
    outcome.setOutcome(golden);
    String result = outcome.getOutcome();

    if (!result.equals(golden)) {
      out.println(JSFTestUtil.FAIL + " Unexpected value for outcome property!"
          + JSFTestUtil.NL + "Expected: " + golden + JSFTestUtil.NL
          + "Received: " + result);

    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // end uiOutcomeTargetGetSetOutcomeTest

  public void uiOutcomeTargetIsSetIncludeViewParamsTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIOutcomeTarget outcome = (UIOutcomeTarget) createComponent();

    boolean golden = true;
    outcome.setIncludeViewParams(golden);
    boolean result = outcome.isIncludeViewParams();

    if (!result) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected value for for view parameters setting!"
          + JSFTestUtil.NL + "Expected: " + golden + JSFTestUtil.NL
          + "Received: " + result);

    } else {
      out.println(JSFTestUtil.PASS);
    }

  } // end uiOutcomeTargetIsSetIncludeViewParamsTest
}
