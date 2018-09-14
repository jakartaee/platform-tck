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

package com.sun.ts.tests.jsf.api.javax_faces.validator.doublerangevalidator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.DoubleRangeValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.api.javax_faces.validator.common.BaseValidatorTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

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
    return new DoubleRangeValidator();
  }

  public void doubleValidatorCtor1Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    try {
      new DoubleRangeValidator();
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println("The no-arg constructor for DoubleRangeValidator  "
          + "threw an unexpected exception");
      e.printStackTrace();
    }
  }

  public void doubleValidatorCtor2Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    try {
      new DoubleRangeValidator(10.0);
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println("The DoubleRangeValidator(max) constructor"
          + "threw an unexpected exception");
      e.printStackTrace();
    }
  }

  public void doubleValidatorCtor3Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    try {
      new DoubleRangeValidator(10.0, 1.0);
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println("The DoubleRangeValidator(max, min) constructor "
          + "threw an unexpected exception");
      e.printStackTrace();
    }
  }

  public void doubleValidatorGetSetMaximumTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    DoubleRangeValidator drv = new DoubleRangeValidator();
    drv.setMaximum(10.0);

    if (drv.getMaximum() == 10.0) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "DoubleRangeValidator.getMaximum didn't "
          + "return the save value as provided to " + "DoubleRangeValidator."
          + "setValue()." + JSFTestUtil.NL + "Expected: 10.0" + JSFTestUtil.NL
          + "Received: " + drv.getMaximum());
    }
  }

  public void doubleValidatorGetSetMinimumTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    DoubleRangeValidator drv = new DoubleRangeValidator();
    drv.setMinimum(10.0);

    if (drv.getMinimum() == 10.0) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "DoubleRangeValidator.getMaximum didn't "
          + "return the save value as provided to "
          + "DoubleRangeValidator.setValue()." + JSFTestUtil.NL
          + "Expected: 10.0" + JSFTestUtil.NL + "Received: "
          + drv.getMinimum());
    }
  }

  public void doubleValidatorValidateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FacesContext facesContext = getFacesContext();

    if (facesContext == null) {
      pw.println(JSFTestUtil.FAIL + " Unable to obtain FacesContext instance.");
      return;
    }

    UIViewRoot root = new UIViewRoot();
    root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
    UIInput input = (UIInput) getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    input.setId("C" + Long.valueOf(System.currentTimeMillis()).toString());

    // Max value / no-arg DoubleRangeValidator
    input.setValue(Double.valueOf(Double.MAX_VALUE));
    DoubleRangeValidator drv = new DoubleRangeValidator();

    testValidation(input, drv, facesContext, pw);

    // Min value / no-arg DoubleRangeValidator
    input.setValue(Double.valueOf(Double.MAX_VALUE));
    drv = new DoubleRangeValidator();

    testValidation(input, drv, facesContext, pw);

    // Value set below configured maximum
    input.setValue(Double.valueOf(300.0));
    drv = new DoubleRangeValidator(300.00001);

    testValidation(input, drv, facesContext, pw);

    // value set between configured maximum and minimum
    input.setValue(Double.valueOf(300.0));
    drv = new DoubleRangeValidator(300.00000, 299.99999);

    testValidation(input, drv, facesContext, pw);

    // value is null
    input.setValue(null);
    drv = new DoubleRangeValidator(300.0, 299.0);

    testValidation(input, drv, facesContext, pw);

    pw.println(JSFTestUtil.PASS);
  }

  public void doubleValidatorValidateInvalidTypeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FacesContext facesContext = getFacesContext();
    if (facesContext == null) {
      pw.println(JSFTestUtil.FAIL + " Unable to obtain FacesContext instance.");
      return;
    }
    UIViewRoot root = new UIViewRoot();
    root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
    UIInput input = (UIInput) getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    input.setId("C" + Long.valueOf(System.currentTimeMillis()).toString());
    input.setValue(new ServletException());
    input.setId("input1");
    DoubleRangeValidator drv = new DoubleRangeValidator();
    try {
      drv.validate(facesContext, input, input.getValue());
      pw.println(JSFTestUtil.FAIL + " No Exception thrown when attempting to "
          + "validate invalid type for validator.");
      return;
    } catch (Exception e) {
      if (!(e instanceof ValidatorException)) {
        pw.println(JSFTestUtil.FAIL + " Exception thrown when attempting to"
            + " validate invalid type, but it wasn't an instance of"
            + " ValidatorException.");
        pw.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    input = (UIInput) getApplication().createComponent(UIInput.COMPONENT_TYPE);
    input.setValue("invalid");
    input.setId("input2");
    drv = new DoubleRangeValidator(100);
    try {
      drv.validate(facesContext, input, input.getValue());
      pw.println(JSFTestUtil.FAIL + " No Exception thrown when attempting to "
          + "validate a String that cannot be converted to the "
          + "expected type.");
      return;
    } catch (Exception e) {
      if (!(e instanceof ValidatorException)) {
        pw.println(JSFTestUtil.FAIL + " Exception thrown when attempting to"
            + " validate a String that cannot be converted to the expected"
            + " type but it wasn't an instance of" + " ValidatorException.");
        pw.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    pw.println(JSFTestUtil.PASS);
  }

  public void doubleValidatorValidateMaxViolationTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FacesContext facesContext = getFacesContext();
    if (facesContext == null) {
      pw.println(JSFTestUtil.FAIL + " Unable to obtain FacesContext instance.");
      return;
    }
    UIViewRoot root = new UIViewRoot();
    root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
    facesContext.setViewRoot(root);
    UIInput input = (UIInput) getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    input.setId("C" + Long.valueOf(System.currentTimeMillis()).toString());
    input.setValue(Double.valueOf(100.1));
    DoubleRangeValidator drv = new DoubleRangeValidator(100.0);
    try {
      drv.validate(facesContext, input, input.getValue());
      pw.println(
          JSFTestUtil.FAIL + " No Exception thrown when value was greater than "
              + "allowable maximum.");
      return;
    } catch (Exception e) {
      if (!(e instanceof ValidatorException)) {
        pw.println(JSFTestUtil.FAIL + " Exception thrown when value was "
            + "greater than allowable maximum, but it wasn't an "
            + "instance of ValidatorException");
        pw.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    pw.println(JSFTestUtil.PASS);
  }

  public void doubleValidatorValidateMinViolationTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FacesContext facesContext = getFacesContext();
    if (facesContext == null) {
      pw.println(JSFTestUtil.FAIL + " Unable to obtain FacesContext instance.");
      return;
    }
    UIViewRoot root = new UIViewRoot();
    root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);
    UIInput input = (UIInput) getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    input.setId("C" + Long.valueOf(System.currentTimeMillis()).toString());
    input.setValue(Double.valueOf(89.99999));
    DoubleRangeValidator drv = new DoubleRangeValidator(100.0, 99.0);
    try {
      drv.validate(facesContext, input, input.getValue());
      pw.println(JSFTestUtil.FAIL + " No Exception thrown when value "
          + "was less than allowable minimum.");
      return;
    } catch (Exception e) {
      if (!(e instanceof ValidatorException)) {
        pw.println(JSFTestUtil.FAIL + " Exception thrown when value was "
            + "less than allowable minimum, but it wasn't an "
            + "instance of ValidatorException");
        pw.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    pw.println(JSFTestUtil.PASS);
  }

  public void doubleValidatorValidateNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FacesContext facesContext = getFacesContext();

    if (facesContext == null) {
      pw.println(JSFTestUtil.FAIL + " Unable to obtain FacesContext instance.");
      return;
    }

    UIInput input = (UIInput) getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);

    DoubleRangeValidator drv = new DoubleRangeValidator();

    // Test for FacesContext throws NPE
    JSFTestUtil.checkForNPE(drv, "validate",
        new Class<?>[] { FacesContext.class, UIComponent.class, Object.class },
        new Object[] { null, input, Double.valueOf(10) }, pw);

    // Test for UIComponent throws NPE
    JSFTestUtil.checkForNPE(drv, "validate",
        new Class<?>[] { FacesContext.class, UIComponent.class, Object.class },
        new Object[] { facesContext, null, Double.valueOf(10) }, pw);
  }

  // StateHolder.saveState(), StateHolder.restoreState()
  public void stateHolderSaveRestoreStateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // Begin test setup
    DoubleRangeValidator preSave = new DoubleRangeValidator(100, 5000);

    // Save and restore state and compare the results
    Object state = preSave.saveState(getFacesContext());

    if (state == null) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "saveState() failed to returned null");
      return;
    }

    if (!(state instanceof Serializable)) {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "The Object returned by saveState() was "
          + "not an instance of java.io.Serializable.");
      return;
    }

    DoubleRangeValidator postSave = new DoubleRangeValidator();
    postSave.restoreState(getFacesContext(), state);

    if (postSave.getMaximum() == preSave.getMaximum()) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "getMaximum did not match after restore was called!");
    }

  }

  // --------------------------------------------------- private methods

  private static void testValidation(UIInput input, DoubleRangeValidator drv,
      FacesContext context, PrintWriter pw) {

    try {
      pw.println("Test Validation Range: " + drv.getMinimum() + "-"
          + drv.getMaximum() + JSFTestUtil.NL + "Test Input Value: "
          + input.getValue() + JSFTestUtil.NL);

      drv.validate(context, input, input.getValue());

    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Exception thrown during validation of value." + JSFTestUtil.NL
          + "Exception: " + e);
      return;
    }
  }
}
