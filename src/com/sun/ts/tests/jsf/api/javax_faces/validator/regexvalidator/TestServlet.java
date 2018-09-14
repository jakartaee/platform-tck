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

package com.sun.ts.tests.jsf.api.javax_faces.validator.regexvalidator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.RegexValidator;
import javax.faces.validator.Validator;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.api.javax_faces.validator.common.BaseValidatorTestServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends BaseValidatorTestServlet {

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  @Override
  protected Validator createValidator() {
    return new RegexValidator();
  }

  // ---------------------------------------------------- RegexValidator Tests

  public void regexValidatorCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      new RegexValidator();
      pw.println(JSFTestUtil.PASS);
    } catch (Exception e) {
      pw.println("The no-arg constructor for regexValidator "
          + "threw an unexpected exception ");
      e.printStackTrace();
    }
  }

  public void regexValidateNPETest(HttpServletRequest request,
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
    input.setValue("aabb");

    RegexValidator rev = new RegexValidator();
    rev.setPattern("a*b");

    // Test for null FacesContext throws NPE
    JSFTestUtil.checkForNPE(rev, "validate",
        new Class<?>[] { FacesContext.class, UIComponent.class, Object.class },
        new Object[] { null, input, input.getValue() }, pw);

    // Test for null UIComponent throws NPE
    JSFTestUtil.checkForNPE(rev, "validate",
        new Class<?>[] { FacesContext.class, UIComponent.class, Object.class },
        new Object[] { facesContext, null, input.getValue() }, pw);
  }

  // setPattern() & getPattern() test
  public void regexValidateSetGetPatternTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    String expected = "abc-xyz";

    RegexValidator rev = new RegexValidator();
    rev.setPattern(expected);

    String result = rev.getPattern();

    if (expected.equals(result)) {
      pw.println(JSFTestUtil.PASS);

    } else {
      pw.println(JSFTestUtil.FAIL + JSFTestUtil.NL + "Expected: " + expected
          + JSFTestUtil.NL + "Recieved: " + result);
    }

  }

  // StateHolder.saveState(), StateHolder.restoreState()
  public void stateHolderSaveRestoreStateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // Begin test setup
    RegexValidator preSave = new RegexValidator();
    preSave.setPattern("abc-xyz");

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

    RegexValidator postSave = new RegexValidator();
    postSave.restoreState(getFacesContext(), state);

    if (postSave.getPattern().equals(preSave.getPattern())) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "getPattern did not match after restore was called!");
    }

  }
}
