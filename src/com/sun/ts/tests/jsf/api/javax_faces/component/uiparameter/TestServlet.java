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

package com.sun.ts.tests.jsf.api.javax_faces.component.uiparameter;

import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.BaseComponentTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UIParameter;
import javax.faces.component.UISelectBoolean;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TestServlet extends BaseComponentTestServlet {

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    setRendererType(null);
  }

  /**
   * <p>
   * Creates a new {@link javax.faces.component.UIComponent} instance.
   * </p>
   * 
   * @return a new {@link javax.faces.component.UIComponent} instance.
   */
  @Override
  protected UIComponentBase createComponent() {
    return new UIParameter();
  }

  // ------------------------------------------- Test Methods ----

  // ------------------------------------------- UIparameter Tests

  public void uiParameterSetGetNameTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIParameter param = (UIParameter) createComponent();
    String golden = "Frodo";

    param.setName(golden);
    String result = param.getName();

    if (!result.equals(golden)) {
      out.println(JSFTestUtil.FAIL + " UIParameter.getName() didn't return"
          + " the value as set by UIParameter.setName().");
      out.println("Expected: " + golden);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // uiParameterSetGetNameTest

  public void uiParameterSetGetValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIParameter param = (UIParameter) createComponent();
    boolean golden = true;

    param.setValue(golden);
    boolean result = (Boolean) param.getValue();

    if (!result) {
      out.println(JSFTestUtil.FAIL + " UIParameter.getValue() didn't return"
          + " the value as set by UIParameter.setValue().");
      out.println("Expected: " + golden);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // uiParameterSetGetValueTest

  public void uiParameterIsSetDisableTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIParameter param = (UIParameter) createComponent();
    boolean golden = true;

    param.setDisable(golden);
    boolean result = (Boolean) param.isDisable();

    if (!result) {
      out.println(JSFTestUtil.FAIL + " UIParameter.isDisable() didn't return"
          + " the value as set by UIParameter.setDisable().");
      out.println("Expected: " + golden);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // uiParameterIsSetDisableTest
}
