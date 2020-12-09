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

package com.sun.ts.tests.jsf.api.jakarta_faces.validator.methodexpressionvalidator;

import java.io.IOException;
import java.io.PrintWriter;

import com.sun.ts.tests.jsf.api.jakarta_faces.validator.common.BaseValidatorTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import jakarta.el.ExpressionFactory;
import jakarta.el.MethodExpression;
import jakarta.faces.application.Application;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.validator.MethodExpressionValidator;
import jakarta.faces.validator.Validator;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TestServlet extends BaseValidatorTestServlet {

  /**
   * <code>init</code> initializes the servlet.
   * 
   * @param config
   *          - <code>ServletConfig</code>
   */
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  @Override
  protected Validator createValidator() {
    return new MethodExpressionValidator();
  }

  public void methodExpressionValidatorCtor1Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    try {
      new MethodExpressionValidator();
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println("The no-arg constructor for MethodExpressionValidator "
          + "threw an unexpected exception");
      e.printStackTrace();
    }
  }

  public void methodExpressionValidatorCtor2Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {

    PrintWriter pw = response.getWriter();
    FacesContext facesContext = getFacesContext();
    Application app = facesContext.getApplication();
    ExpressionFactory ef = app.getExpressionFactory();

    MethodExpression me = ef.createMethodExpression(facesContext.getELContext(),
        "test.nothing", null, new Class[] {});

    try {
      new MethodExpressionValidator(me);
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println("MethodExpressionValidator(MethodExpression) " + "constructor"
          + "threw an unexpected exception ");
      e.printStackTrace();
    }
  }

  public void methodExpressionValidateNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FacesContext facesContext = getFacesContext();

    if (facesContext == null) {
      pw.println(JSFTestUtil.FAIL + " Unable to obtain FacesContext instance.");
      return;
    }

    UIInput input = (UIInput) getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    input.setId("input1");

    input.setValue(new ServletException());
    MethodExpressionValidator mev = new MethodExpressionValidator();

    // Test for null FacesContext throws NPE
    JSFTestUtil.checkForNPE(mev, "validate",
        new Class<?>[] { FacesContext.class, UIComponent.class, Object.class },
        new Object[] { null, input, input.getValue() }, pw);

    // Test for null UIComponent throws NPE
    JSFTestUtil.checkForNPE(mev, "validate",
        new Class<?>[] { FacesContext.class, UIComponent.class, Object.class },
        new Object[] { facesContext, null, input.getValue() }, pw);

  }
}
