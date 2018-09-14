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
package com.sun.ts.tests.jsf.api.javax_faces.component.uiselectitems;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.BaseComponentTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.component.UIComponentBase;
import javax.faces.component.UISelectItems;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Vector;

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
    return new UISelectItems();
  }

  // ------------------------------------------- Test Methods ----
  public void uiSelectItemsGetSetValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UISelectItems command = (UISelectItems) createComponent();

    command.setValue("value");

    if (!"value".equals(command.getValue())) {
      out.println(JSFTestUtil.FAIL + " UISelectItem.getValue() didn't return"
          + " the value as set by UISelectItem.setValue().");
      out.println("Expected: value");
      out.println("Received: " + command.getValue());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiSelectItemsGetArrayValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UISelectItems command = (UISelectItems) createComponent();

    String[] colors = { "red", "green", "black", "tan" };

    command.setValue(colors);

    if (!Arrays.equals(colors, (String[]) command.getValue())) {
      out.println(JSFTestUtil.FAIL + " UISelectItem.getValue() didn't return"
          + " the value as set by UISelectItem.setValue().");
      out.println("Arrays objects were not equal!");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiSelectItemsGetCollectionValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UISelectItems command = (UISelectItems) createComponent();

    Vector<Car> cars = new Vector<Car>();
    cars.add(new Car("passenger", "green"));
    cars.add(new Car("sport", "red"));
    cars.add(new Car("sedan", "red"));

    command.setValue(cars);

    if (!Arrays.equals(cars.toArray(),
        ((Vector<?>) command.getValue()).toArray())) {
      out.println(JSFTestUtil.FAIL + " UISelectItem.getValue() didn't return"
          + " the value as set by UISelectItem.setValue().");
      out.println("Vector objects were not equal!");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiSelectItemsMapGetValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UISelectItems command = (UISelectItems) createComponent();

    HashMap<String, String> employeeLvl = new HashMap<String, String>();
    employeeLvl.put("MGR", "4");
    employeeLvl.put("VP", "6");
    employeeLvl.put("DIR", "5");
    employeeLvl.put("WB", "0");

    command.setValue(employeeLvl);

    if (!Arrays.equals(employeeLvl.entrySet().toArray(),
        ((HashMap<?, ?>) command.getValue()).entrySet().toArray())) {
      out.println(JSFTestUtil.FAIL + " UISelectItem.getValue() didn't return"
          + " the value as set by UISelectItem.setValue().");
      out.println("HashMap objects were not equal!");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // --------------------------------------------------------- private classes
  private static class Car {

    private String style;

    private String color;

    public Car(String style, String color) {
      this.style = style;
      this.color = color;
    }

    public String getColor() {
      return color;
    }

    public void setColor(String color) {
      this.color = color;
    }

    public String getStyle() {
      return style;
    }

    public void setStyle(String style) {
      this.style = style;
    }
  }
}
