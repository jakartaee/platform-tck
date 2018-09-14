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
package com.sun.ts.tests.jsf.api.javax_faces.validator.beanvalidator;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;

import javax.faces.component.StateHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.validator.BeanValidator;
import javax.faces.validator.Validator;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.api.javax_faces.validator.common.BaseValidatorTestServlet;
import com.sun.ts.tests.jsf.common.beans.TestBean;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public class TestServlet extends BaseValidatorTestServlet {

  /**
   * <code>init</code> initializes the servlet.
   * 
   * @param config
   *          - <code>ServletConfig</code>
   */
  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  @Override
  protected Validator createValidator() {
    return new BeanValidator();
  }

  public void beanValidatorCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    try {
      new BeanValidator();
      pw.println(JSFTestUtil.PASS);

    } catch (Exception e) {
      pw.println("The no-arg constructor for BeanValidator "
          + "threw an unexpected exception ");
      e.printStackTrace();
    }
  }

  public void beanvalidatorClearInitialStateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter pw = response.getWriter();
    BeanValidator bv = new BeanValidator();
    bv.markInitialState();

    if (bv.initialStateMarked()) {
      bv.clearInitialState();
    }

    if (!bv.initialStateMarked()) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println(JSFTestUtil.FAIL + " BeanValidator.clearInitialState didn't "
          + "Reset the PartialStateHolder to a non-delta tracking " + "state.");
    }
  }

  public void beanvalidatorgetsetValidationGroupsTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
    PrintWriter pw = response.getWriter();

    BeanValidator bv = new BeanValidator();
    String vgs = "group1, group2, group3";
    bv.setValidationGroups(vgs);

    if (bv.getValidationGroups().equals(vgs)) {
      pw.println(JSFTestUtil.PASS);
    } else {
      pw.println("Test FAILED. BeanValidator.set/getValidationGroups() "
          + "didn't set or get the correct information." + JSFTestUtil.NL
          + "Expected: " + vgs + JSFTestUtil.NL + "Received: "
          + bv.getValidationGroups());
    }
  }

  // StateHolder.saveState(), StateHolder.restoreState()
  public void stateHolderSaveRestoreStateTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    // Begin test setup
    BeanValidator preSave = new BeanValidator();
    preSave.setValidationGroups("abc, efg, xyx");

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

    BeanValidator postSave = new BeanValidator();
    postSave.restoreState(getFacesContext(), state);

    if (postSave.getValidationGroups().equals(preSave.getValidationGroups())) {
      out.println(JSFTestUtil.PASS);

    } else {
      out.println(JSFTestUtil.FAIL + JSFTestUtil.NL
          + "getValidationGroups did not match after restore was " + "called!");
    }

  }

}
