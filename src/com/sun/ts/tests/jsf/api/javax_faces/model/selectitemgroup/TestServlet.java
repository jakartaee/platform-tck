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

package com.sun.ts.tests.jsf.api.javax_faces.model.selectitemgroup;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.faces.model.SelectItemGroup;
import javax.faces.model.SelectItem;

import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

public class TestServlet
    extends com.sun.ts.tests.jsf.api.javax_faces.model.selectitem.TestServlet {

  private static final SelectItem[] ITEMS = {
      new SelectItem("value1", "label1"), new SelectItem("value2", "label2") };

  /**
   * <p>
   * Initializes this {@link javax.servlet.Servlet}.
   * </p>
   * 
   * @param config
   *          this Servlet's configuration
   * @throws javax.servlet.ServletException
   *           if an error occurs
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ---------------------------------------------------------------- Test
  // Methods

  public void selectItemGroupCtor0Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    try {
      new SelectItemGroup();

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + " Unexpected exception creating "
          + "new SelectItemGroup: ");
      e.printStackTrace();
    }

    out.println(JSFTestUtil.PASS);
  }

  public void selectItemGroupCtor1Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    try {
      String result = new SelectItemGroup("label").getLabel();

      if (!"label".equals(result)) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Unexpected value returned by getLabel()." + JSFTestUtil.NL
            + "Expected: label" + JSFTestUtil.NL + "Received: " + result);
      }

      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected exception creating new SelectItemGroup!");
      e.printStackTrace();
    }
  }

  public void selectItemGroupCtor4Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    SelectItemGroup group = null;

    try {
      group = new SelectItemGroup("label", "description", true, ITEMS);

      if (!"label".equals(group.getLabel())) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "getLabel() didn't return the value provided to the " + "ctor."
            + JSFTestUtil.NL + "Expected: label" + JSFTestUtil.NL + "Received: "
            + group.getLabel());
        return;
      }

      if (!"description".equals(group.getDescription())) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "getDescription() didn't return the value provided "
            + "to the ctor." + JSFTestUtil.NL + "Expected: description"
            + JSFTestUtil.NL + "Received: " + group.getDescription());
        return;
      }

      if (!group.isDisabled()) {
        out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
            + "Expected isDisabled to return true as provided to "
            + "the ctor.");
        return;
      }

      if (!(this.testSelectitems(ITEMS, group, out))) {
        // test fails
        return;
      }

      out.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unexpected exception creating new SelectItemGroup: ");
      e.printStackTrace();
    }

  } // End selectItemGroupCtor4Test

  public void selectItemGroupGetSetSelectItemsTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    SelectItemGroup group = new SelectItemGroup();

    group.setSelectItems(ITEMS);

    if (!(this.testSelectitems(ITEMS, group, out))) {
      // test fails
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void selectItemSetSelectItemsNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    SelectItemGroup group = new SelectItemGroup();

    JSFTestUtil.checkForNPE(group, "setSelectItems",
        new Class<?>[] { SelectItem[].class }, new Object[] { null }, pw);

  }

  // ------------------------------------------------ private methods

  private boolean testSelectitems(SelectItem[] golden, SelectItemGroup group,
      PrintWriter out) {
    boolean result = true;
    SelectItem[] testItems = group.getSelectItems();

    if (!(Arrays.equals(golden, testItems))) {
      out.println(
          JSFTestUtil.FAIL + " Unexpected value returned by getSelectItems()");

      out.println("Expected: ");
      for (int i = 0; i < golden.length; i++) {
        out.println("SelectItem # " + i + ": value= " + golden[i].getValue()
            + ",  label= " + golden[i].getLabel());
      }

      out.println("Received: ");
      for (int i = 0; i < testItems.length; i++) {
        out.println("SelectItem # " + i + ": value= " + testItems[i].getValue()
            + ",  label= " + testItems[i].getLabel());
      }

      result = false;
    }

    return result;
  }
}
