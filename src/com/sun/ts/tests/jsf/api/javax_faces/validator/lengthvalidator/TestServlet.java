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

package com.sun.ts.tests.jsf.api.javax_faces.validator.lengthvalidator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.LengthValidator;
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
    return new LengthValidator();
  }

  public void lengthValidatorCtor1Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      new LengthValidator();
      pw.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + " Default constructor of LengthValidator");
      e.printStackTrace();
    }
  }

  public void lengthValidatorCtor2Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      new LengthValidator(Integer.MAX_VALUE);
      pw.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + " LengthValidator(int maximum)");
      e.printStackTrace();
    }
  }

  public void lengthValidatorCtor3Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      new LengthValidator(Integer.MAX_VALUE, Integer.MIN_VALUE);
      pw.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      pw.println(
          JSFTestUtil.FAIL + " LengthValidator(int maximum, int minimum)");
      e.printStackTrace();
    }
  }

  public void lengthValidatorGetSetMaximumTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    LengthValidator lVal = new LengthValidator();
    lVal.setMaximum(Integer.MAX_VALUE);
    if (lVal.getMaximum() == Integer.MAX_VALUE) {
      lVal = new LengthValidator(Integer.MIN_VALUE);
      lVal.setMaximum(Integer.MAX_VALUE);
      if (lVal.getMaximum() == Integer.MAX_VALUE) {
        pw.println(JSFTestUtil.PASS);
      } else {
        pw.println(
            JSFTestUtil.FAIL + " Original maximum value was not overwritten"
                + " when LengthValidator.setMaximum(int) was called.");
        pw.println("Expected: " + Integer.MAX_VALUE);
        pw.println("Received: " + lVal.getMaximum());
      }
    } else {
      pw.println(
          JSFTestUtil.FAIL + " LengthValidator.getMaximum() didn't not return"
              + " the same value passed to LengthValidator.setMaximum()");
      pw.println("Expected: " + Integer.MAX_VALUE);
      pw.println("Received: " + lVal.getMaximum());
    }
  }

  public void lengthValidatorGetSetMinimumTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    LengthValidator lVal = new LengthValidator();
    lVal.setMinimum(Integer.MIN_VALUE);
    if (lVal.getMinimum() == Integer.MIN_VALUE) {
      lVal = new LengthValidator(Integer.MAX_VALUE, Integer.MIN_VALUE);
      lVal.setMinimum(Integer.MAX_VALUE);
      if (lVal.getMinimum() == Integer.MAX_VALUE) {
        pw.println(JSFTestUtil.PASS);
      } else {
        pw.println(
            JSFTestUtil.FAIL + " Original minimum value was not overwritten"
                + " when LengthValidator.setMinimum(int) was called.");
        pw.println("Expected: " + Integer.MAX_VALUE);
        pw.println("Received: " + lVal.getMinimum());
      }
    } else {
      pw.println(
          JSFTestUtil.FAIL + " LengthValidator.getMinimum() didn't not return"
              + " the same value passed to LengthValidator.setMinimum()");
      pw.println("Expected: " + Integer.MAX_VALUE);
      pw.println("Received: " + lVal.getMinimum());
    }
  }

  public void lengthValidatorValidateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    UIInput input = (UIInput) getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    FacesContext facesContext = getFacesContext();

    if (facesContext == null) {
      pw.println(JSFTestUtil.FAIL + " Unable to obtain FacesContext instance.");
      return;
    }

    LengthValidator lVal = new LengthValidator();
    input.setValue("arbitrary value");
    testValidation(input, lVal, facesContext, pw);

    input.setValue(null);
    lVal = new LengthValidator(50);
    testValidation(input, lVal, facesContext, pw);

    input.setValue("arbitrarty value");
    lVal = new LengthValidator(50, 1);
    testValidation(input, lVal, facesContext, pw);

    // value is null
    input.setValue(null);
    lVal = new LengthValidator(300, 299);
    testValidation(input, lVal, facesContext, pw);

    pw.println(JSFTestUtil.PASS);
  }

  public void lengthValidatorValidateMaxViolationTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    UIInput input = (UIInput) getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    input.setId("C" + Long.valueOf(System.currentTimeMillis()).toString());
    FacesContext facesContext = getFacesContext();

    if (facesContext == null) {
      pw.println(JSFTestUtil.FAIL + " Unable to obtain FacesContext instance.");
      return;
    }

    UIViewRoot root = new UIViewRoot();
    root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);

    LengthValidator lVal = new LengthValidator(2);

    input.setValue(Double.valueOf(100.0));
    try {
      lVal.validate(facesContext, input, input.getValue());
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

  public void lengthValidatorValidateMinViolationTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    UIInput input = (UIInput) getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);
    input.setId("C" + Long.valueOf(System.currentTimeMillis()).toString());
    FacesContext facesContext = getFacesContext();

    if (facesContext == null) {
      pw.println(JSFTestUtil.FAIL + " Unable to obtain FacesContext instance.");
      return;
    }

    UIViewRoot root = new UIViewRoot();
    root.setRenderKitId(RenderKitFactory.HTML_BASIC_RENDER_KIT);

    LengthValidator lVal = new LengthValidator(Integer.MAX_VALUE, 50);

    input.setValue("arbitrary value");
    try {
      lVal.validate(facesContext, input, input.getValue());
      pw.println(
          JSFTestUtil.FAIL + " No Exception thrown when value was less than "
              + "allowable minimum.");
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

  public void lengthValidatorValidateNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    FacesContext facesContext = getFacesContext();
    UIInput input = (UIInput) getApplication()
        .createComponent(UIInput.COMPONENT_TYPE);

    if (facesContext == null) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Unable to obtain FacesContext instance.");
      return;
    }

    LengthValidator lv = new LengthValidator(Integer.MAX_VALUE, 50);

    // Test for FacesContext throws NPE
    JSFTestUtil.checkForNPE(lv, "validate",
        new Class<?>[] { FacesContext.class, UIComponent.class, Object.class },
        new Object[] { null, input, 20 }, pw);

    // Test for UIComponent throws NPE
    JSFTestUtil.checkForNPE(lv, "validate",
        new Class<?>[] { FacesContext.class, UIComponent.class, Object.class },
        new Object[] { facesContext, null, 20 }, pw);

  }

  // StateHolder.saveState(), StateHolder.restoreState()
  public void stateHolderSaveRestoreStateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // Begin test setup
    LengthValidator preSave = new LengthValidator(1000);

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

    LengthValidator postSave = new LengthValidator();
    postSave.restoreState(getFacesContext(), state);

    if (postSave.getMaximum() == preSave.getMaximum()) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "getMaximum did not match after restore was called!");
    }

  }

  // --------------------------------------------------- private methods

  private static void testValidation(UIInput input, LengthValidator lv,
      FacesContext context, PrintWriter pw) {

    try {
      pw.println("Test Validation min-max length: " + lv.getMinimum() + "-"
          + lv.getMaximum() + JSFTestUtil.NL + "Test Input Value: "
          + input.getValue() + JSFTestUtil.NL);

      lv.validate(context, input, input.getValue());

    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Exception thrown during validation of value." + JSFTestUtil.NL
          + "Exception: " + e);
      return;
    }
  }
}
