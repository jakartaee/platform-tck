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

package com.sun.ts.tests.jsf.api.javax_faces.component.uicolumn;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.component.UIColumn;
import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIData;
import javax.faces.component.UIOutput;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.BaseComponentTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

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
    return new UIColumn();
  }

  // ------------------------------------------- UIColumn Methods ----

  // UIColumn.setFooter() throws NullPointerException
  public void uiColumnSetFooterNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(this.createComponent().getClass(), "setFooter",
        new Class<?>[] { UIComponent.class }, new Object[] { null }, out);
  }

  // UIColumn.{get,set}Footer()
  public void uiColumnGetSetFooterTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String golden = "Testing";
    UIColumn uic = (UIColumn) createComponent();
    UIOutput footNotes = new UIOutput();

    footNotes.setId(golden);
    uic.setFooter(footNotes);

    String result = uic.getFooter().getId();

    if (!result.equals(golden)) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected Value returned from UIColumn.getFooter()."
          + JSFTestUtil.NL + "Expected: " + golden + JSFTestUtil.NL
          + "Recieved: " + result);
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }// End uiColumnGetSetFooterTest

  // UIColumn.{get,set}Header()
  public void uiColumnGetSetHeaderTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    String golden = "Testing";
    UIColumn uic = (UIColumn) createComponent();
    UIOutput header = new UIOutput();

    header.setId(golden);
    uic.setHeader(header);

    String result = uic.getHeader().getId();

    if (!result.equals(golden)) {
      out.println(JSFTestUtil.FAIL
          + " Unexpected Value returned from UIColumn.getFooter()."
          + JSFTestUtil.NL + "Expected: " + golden + JSFTestUtil.NL
          + "Recieved: " + result);
    } else {
      out.println(JSFTestUtil.PASS);
    }

  }// End uiColumnGetSetFooterTest

  // UIColumn.setHeader() throws NullPointerException
  public void uiColumnSetHeaderNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    JSFTestUtil.checkForNPE(this.createComponent().getClass(), "setHeader",
        new Class<?>[] { UIComponent.class }, new Object[] { null }, out);
  }

}
