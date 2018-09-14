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

package com.sun.ts.tests.jsf.api.javax_faces.validator.longrangevalidator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.render.RenderKitFactory;
import javax.faces.validator.LongRangeValidator;
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
    return new LongRangeValidator();
  }

  public void longValidatorCtor1Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      new LongRangeValidator();
      pw.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      pw.println("The no-arg constructor for LongRangeValidator ");
      e.printStackTrace();
    }
  }

  public void longValidatorCtor2Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      new LongRangeValidator(10);
      pw.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      pw.println("The LongRangeValidator(max) constructor");
      e.printStackTrace();
    }
  }

  public void longValidatorCtor3Test(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      new LongRangeValidator(10, 1);
      pw.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      pw.println("The LongRangeValidator(max, min) constructor ");
      e.printStackTrace();
    }
  }

  public void longValidatorGetSetMaximumTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    LongRangeValidator drv = new LongRangeValidator();
    drv.setMaximum(10);
    if (drv.getMaximum() == 10.0) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println(JSFTestUtil.FAIL + " LongRangeValidator.getMaximum didn't"
          + " return the save value as provided to LongRangeValidator."
          + "setValue().");
      pw.println("Expected: 10.0");
      pw.println("Received: " + drv.getMaximum());
    }
  }

  public void longValidatorGetSetMinimumTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    LongRangeValidator drv = new LongRangeValidator();
    drv.setMinimum(10);
    if (drv.getMinimum() == 10.0) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println(JSFTestUtil.FAIL + " LongRangeValidator.getMaximum didn't"
          + " return the save value as provided to LongRangeValidator."
          + "setValue().");
      pw.println("Expected: 10.0");
      pw.println("Received: " + drv.getMinimum());
    }
  }

  public void longValidatorValidateTest(HttpServletRequest request,
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

    // Max value / no-arg LongRangeValidator
    input.setValue(Long.valueOf(Long.MAX_VALUE));
    LongRangeValidator lrv = new LongRangeValidator();
    testValidation(input, lrv, facesContext, pw);

    // Min value / no-arg LongRangeValidator
    input.setValue(Long.valueOf(Long.MIN_VALUE));
    lrv = new LongRangeValidator();
    testValidation(input, lrv, facesContext, pw);

    // Value set below configured maximum
    input.setValue(Long.valueOf(300));
    lrv = new LongRangeValidator(300);
    testValidation(input, lrv, facesContext, pw);

    // value set between configured maximum and minimum
    input.setValue(Long.valueOf(299));
    lrv = new LongRangeValidator(300, 299);
    testValidation(input, lrv, facesContext, pw);

    // value is null
    input.setValue(null);
    lrv = new LongRangeValidator(300, 299);
    testValidation(input, lrv, facesContext, pw);

    pw.println(JSFTestUtil.PASS);
  }

  public void longValidatorValidateNPETest(HttpServletRequest request,
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

    LongRangeValidator lrv = new LongRangeValidator();

    // Test for FacesContext throws NPE
    JSFTestUtil.checkForNPE(lrv, "validate",
        new Class<?>[] { FacesContext.class, UIComponent.class, Object.class },
        new Object[] { null, input, Long.valueOf(10) }, pw);

    // Test for UIComponent throws NPE
    JSFTestUtil.checkForNPE(lrv, "validate",
        new Class<?>[] { FacesContext.class, UIComponent.class, Object.class },
        new Object[] { facesContext, null, Long.valueOf(10) }, pw);

  }

  public void longValidatorValidateInvalidTypeTest(HttpServletRequest request,
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
    input.setId("input1");
    input.setId("C" + Long.valueOf(System.currentTimeMillis()).toString());
    input.setValue(new ServletException());
    LongRangeValidator drv = new LongRangeValidator();
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
    input.setId("input2");
    input.setValue("invalid");
    drv = new LongRangeValidator(100);
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

  public void longValidatorValidateMaxViolationTest(HttpServletRequest request,
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
    input.setValue(Long.valueOf(101));
    LongRangeValidator drv = new LongRangeValidator(100);
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

  public void longValidatorValidateMinViolationTest(HttpServletRequest request,
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
    input.setValue(Long.valueOf(98));
    LongRangeValidator drv = new LongRangeValidator(100, 99);

    try {
      drv.validate(facesContext, input, input.getValue());
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

  // StateHolder.saveState(), StateHolder.restoreState()
  public void stateHolderSaveRestoreStateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // Begin test setup
    LongRangeValidator preSave = new LongRangeValidator(Long.valueOf(1000));

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

    LongRangeValidator postSave = new LongRangeValidator();
    postSave.restoreState(getFacesContext(), state);

    if (postSave.getMaximum() == preSave.getMaximum()) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "getMaximum did not match after restore was called!");
    }

  }

  // --------------------------------------------------- private methods

  private static void testValidation(UIInput input, LongRangeValidator lrv,
      FacesContext context, PrintWriter pw) {

    try {
      pw.println("Test Validation min-max range: " + lrv.getMinimum() + "-"
          + lrv.getMaximum() + JSFTestUtil.NL + "Test Input Value: "
          + input.getValue() + JSFTestUtil.NL);

      lrv.validate(context, input, input.getValue());

    } catch (Exception e) {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "Exception thrown during validation of value." + JSFTestUtil.NL
          + "Exception: " + e);
      return;
    }
  }
}
