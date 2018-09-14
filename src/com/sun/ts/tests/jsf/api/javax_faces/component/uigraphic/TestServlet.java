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

package com.sun.ts.tests.jsf.api.javax_faces.component.uigraphic;

import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIComponentBase;
import javax.faces.component.UIGraphic;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.el.ValueExpression;
import javax.el.ExpressionFactory;

import com.sun.ts.tests.jsf.api.javax_faces.component.common.BaseComponentTestServlet;
import com.sun.ts.tests.jsf.api.javax_faces.component.common.BufferedResponseWrapper;
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
    setRendererType("javax.faces.Image");
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
    return new UIGraphic();
  }

  // ------------------------------------------- Test Methods ----

  @Override
  public void uiComponentGetSetValueExpressionTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    BufferedResponseWrapper wrapper = new BufferedResponseWrapper(response);

    super.uiComponentGetSetValueExpressionTest(request, wrapper);
    String result = wrapper.getBufferedWriter().toString();

    PrintWriter out = response.getWriter();

    if (result.indexOf(JSFTestUtil.PASS) == -1) {
      out.println(result);
      return;
    }

    // default processing of get,setValueExpression is ok, now validate
    // processing specific to UIGraphic
    request.setAttribute("tckUrl", "http://java.sun.com");

    ExpressionFactory factory = JSFTestUtil
        .getExpressionFactory(servletContext);
    ValueExpression expression = factory.createValueExpression(
        getFacesContext().getELContext(), "#{requestScope.tckUrl}",
        java.lang.String.class);
    ValueExpression literalExpr = factory.createValueExpression(
        getFacesContext().getELContext(), "http://java.net",
        java.lang.String.class);

    UIGraphic graphic = (UIGraphic) createComponent();

    graphic.setValueExpression("url", expression);

    if (!"http://java.sun.com".equals(graphic.getValue())) {
      out.println(JSFTestUtil.FAIL + " setValueExpression() with a key of 'url'"
          + " didn't set the ValueExpression provided as the value"
          + " of the component.");
      out.println("Expected: http://java.net");
      out.println("Received: " + graphic.getValue());
      return;
    }

    // ensure there is no special processing if the ValueExpression
    // happens to be literal
    graphic.setValueExpression("url", literalExpr);
    if (!"http://java.net".equals(graphic.getValue())) {
      out.println(JSFTestUtil.FAIL + " setValueExpression() with a key of 'url'"
          + " didn't set the literal ValueExpression provided as the "
          + "value of the component.");
      out.println("Expected: " + expression);
      out.println("Received: " + graphic.getValue());
      return;
    }

    out.println(JSFTestUtil.PASS);

  }

  public void uiGraphicSetGetValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    UIGraphic command = (UIGraphic) createComponent();

    command.setValue("value");

    if (!"value".equals(command.getValue())) {
      out.println(JSFTestUtil.FAIL + " UIGraphic.getValue() didn't return"
          + " the value as set by UIGraphic.setValue().");
      out.println("Expected: value");
      out.println("Received: " + command.getValue());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void uiGraphicGetSetURLTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    UIGraphic graphic = (UIGraphic) createComponent();

    String url = "http://java.sun.com";

    graphic.setUrl(url);
    String result = graphic.getUrl();
    if (!url.equals(result)) {
      out.println(JSFTestUtil.FAIL + " Expected UIGraphic.getURL() to return"
          + " the value as set by UIGraphic.setURL().");
      out.println("Expected: " + url);
      out.println("Received: " + result);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

}
