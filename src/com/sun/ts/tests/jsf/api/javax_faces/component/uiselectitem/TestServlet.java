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

package com.sun.ts.tests.jsf.api.javax_faces.component.uiselectitem;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UISelectItem;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.BaseComponentTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends BaseComponentTestServlet {

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
    return new UISelectItem();
  }

  // ------------------------------------------- Test Methods ----

  public void uiSelectItemGetSetValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UISelectItem command = (UISelectItem) createComponent();
    String test = "value";
    command.setValue(test);
    String result = (String) command.getValue();

    if (!test.equals(result)) {
      out.println(JSFTestUtil.FAIL + " UISelectItem.getValue() didn't return"
          + " the value as set by UISelectItem.setValue().");
      out.println("Expected: " + test);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // End uiSelectItemGetSetValueTest

  public void uiSelectItemGetSetItemDescriptionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UISelectItem command = (UISelectItem) createComponent();
    String test = "Description";
    command.setItemDescription(test);
    String result = command.getItemDescription();

    if (!test.equals(result)) {
      out.println(
          JSFTestUtil.FAIL + " UISelectItem.getItemDescription() didn't return"
              + " the value as set by UISelectItem.setItemDescription().");
      out.println("Expected: " + test);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // uiSelectItemGetSetItemDescriptionTest

  public void uiSelectItemGetSetItemLabelTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UISelectItem command = (UISelectItem) createComponent();
    String test = "Label";
    command.setItemLabel(test);
    String result = command.getItemLabel();

    if (!test.equals(result)) {
      out.println(
          JSFTestUtil.FAIL + " UISelectItem.getItemLabel() didn't return"
              + " the value as set by UISelectItem.setItemLabel().");
      out.println("Expected: " + test);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // uiSelectItemGetSetItemLabelTest

  public void uiSelectItemIsSetItemDisabledTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UISelectItem command = (UISelectItem) createComponent();
    boolean value = true;
    command.setItemDisabled(value);
    boolean result = command.isItemDisabled();

    if (!result) {
      out.println(
          JSFTestUtil.FAIL + " UISelectItem.isItemDisabled() didn't return"
              + " the value as set by UISelectItem.setItemDisabled().");
      out.println("Expected: " + value);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // uiSelectItemIsSetItemDisabledTest

  public void uiSelectItemIsSetItemEscapedTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UISelectItem command = (UISelectItem) createComponent();
    boolean value = true;
    command.setItemEscaped(value);
    boolean result = command.isItemEscaped();

    if (!result) {
      out.println(
          JSFTestUtil.FAIL + " UISelectItem.isItemEscaped() didn't return"
              + " the value as set by UISelectItem.setItemEscaped().");
      out.println("Expected: " + value);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // uiSelectItemIsSetItemEscapedTest

  public void uiSelectItemIsSetNoSelectionOptionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UISelectItem command = (UISelectItem) createComponent();
    boolean value = true;
    command.setNoSelectionOption(value);
    boolean result = command.isNoSelectionOption();

    if (!result) {
      out.println(JSFTestUtil.FAIL
          + " UISelectItem.isNoSelectionOptionTest() didn't return"
          + " the value as set by UISelectItem.setNoSelectionOptionTest().");
      out.println("Expected: " + value);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // uiSelectItemIsSetNoSelectionOptionTest

  public void uiSelectItemGetSetItemValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UISelectItem command = (UISelectItem) createComponent();
    boolean test = true;

    command.setItemValue(test);
    boolean result = (Boolean) command.getItemValue();

    if (!result) {
      out.println(
          JSFTestUtil.FAIL + " UISelectItem.getItemValue() didn't return"
              + " the value as set by UISelectItem.setItemValue().");
      out.println("Expected: " + test);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // End uiSelectItemGetSetValueTest

}
