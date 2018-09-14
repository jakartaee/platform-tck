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

package com.sun.ts.tests.jsf.api.javax_faces.el.valuebinding;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;
import com.sun.ts.tests.jsf.api.javax_faces.el.common.TestBean;

import javax.faces.el.PropertyNotFoundException;
import javax.faces.el.ValueBinding;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.beans.Beans;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public final class TestServlet extends HttpTCKServlet {

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ------------------------------------------- Test Methods ----

  // ValueBinding.getValue(FacesContext)
  public void valBindingGetValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ValueBinding binding = getApplication()
        .createValueBinding("#{param.testname}");

    try {
      String result = (String) binding.getValue(getFacesContext());
      if (!"valBindingGetValueTest".equals(result)) {
        out.println(JSFTestUtil.FAIL + " Unexpected value returned from"
            + " ValueBinding.getValue() for value reference "
            + "'param.testname'");
        out.println("Expected: valBindingGetValueTest");
        out.println("Received: " + result);
        return;
      }
    } catch (Exception e) {
      e.printStackTrace();
      out.println(JSFTestUtil.FAIL + " Unexpected Exception!");
      out.println(e);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ValueBinding.getValue(FacesContext) throws NPE if argument is null
  public void valBindingGetValueNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ValueBinding binding = getApplication()
        .createValueBinding("#{param.testname}");

    try {
      binding.getValue(null);
      out.println(JSFTestUtil.FAIL + " No Exception thrown when ValueBinding"
          + ".getValue(FacesContext) was passed a null argument.");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when ValueBinding."
            + "getValue(FacesContext) was passed a null argument,"
            + " but it wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // ValueBinding.getValue(FacesContext) throws PNFE if property cannot be
  // found or is not readable.
  public void valBindingGetValuePNFETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    request.setAttribute("obj", request);
    ValueBinding binding = getApplication()
        .createValueBinding("#{requestScope.obj.foo}");

    try {
      binding.getValue(getFacesContext());
      out.println(JSFTestUtil.FAIL + " No Exception thrown when ValueBinding."
          + "getValue(FacesContext) with a non-existent property.");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown with ValueBinding"
            + " referenced a non-existent property, but was not"
            + " an instance of PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    // Now check for the PNFE if the property is not readable
    TestBean bean = getTestBean();
    request.setAttribute("tckBean", bean);
    binding = getApplication()
        .createValueBinding("#{requestScope.tckBean.onlyWritable}");

    try {
      binding.getValue(getFacesContext());
      out.println(JSFTestUtil.FAIL + " No Exception thrown when ValueBinding."
          + "getValue(FacesContext) with a non-readable property.");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown with ValueBinding"
            + " referenced a non-readable property, but was not"
            + " an instance of PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  public void valBindingGetExpressionStringTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ValueBinding binding = getApplication()
        .createValueBinding("#{param.testname}");

    if (binding == null) {
      out.println(
          JSFTestUtil.FAIL + " Unable to obtain ValueBinding instance.");
      return;
    }

    if (!"#{param.testname}".equals(binding.getExpressionString())) {
      out.println(JSFTestUtil.FAIL + " Unexpected result returned from "
          + "getExpressionString().");
      out.println("Value expected: '#{param.testname}");
      out.println("Value returned: " + binding.getExpressionString());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ValueBinding.getType(FacesContext) throws NPE if arg is null
  public void valBindingGetTypeNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ValueBinding binding = getApplication()
        .createValueBinding("#{param.testname}");

    try {
      binding.getType(null);
      out.println(JSFTestUtil.FAIL + " No Exception thrown when ValueBinding"
          + ".getType(FacesContext) was passed a null argument.");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when ValueBinding."
            + "getType(FacesContext) was passed a null argument,"
            + " but it wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // ValueBinding.getType(FacesContext) throws PNFE if property cannot be
  // found
  public void valBindingGetTypePNFETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    request.setAttribute("obj", request);
    ValueBinding binding = getApplication()
        .createValueBinding("#{requestScope.obj.foo}");

    try {
      binding.getType(getFacesContext());
      out.println(JSFTestUtil.FAIL + " No Exception thrown when ValueBinding."
          + "getType(FacesContext) with a non-existent property.");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown with ValueBinding"
            + " referenced a non-existent property, but was not"
            + " an instance of PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    // Now check for the PNFE if the property is not readable
    TestBean bean = getTestBean();
    request.setAttribute("tckBean", bean);
    binding = getApplication()
        .createValueBinding("#{requestScope.tckBean.onlyWritable}");

    try {
      binding.getValue(getFacesContext());
      out.println(JSFTestUtil.FAIL + " No Exception thrown when ValueBinding."
          + "getType(FacesContext) with a non-readable property.");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown with ValueBinding"
            + " referenced a non-readable property, but was not"
            + " an instance of PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // ValueBinding.isReadOnly(FacesContext)
  public void valBindingIsReadOnlyTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    TestBean bean = getTestBean();

    request.setAttribute("bean", bean);

    ValueBinding binding = getApplication()
        .createValueBinding("#{requestScope.bean.readOnly}");

    if (binding == null) {
      out.println(JSFTestUtil.FAIL + " Unable to obtain ValueBinding for "
          + "'requestScope.bean.readOnly'");
      return;
    }

    if (!binding.isReadOnly(getFacesContext())) {
      out.println(JSFTestUtil.FAIL + " Expected ValueBinding.isReadOnly("
          + "FacesContext) to return true for a Bean that had "
          + "only a getter for the specified property.");
      return;
    }

    binding = getApplication()
        .createValueBinding("#{requestScope.bean.writable}");

    if (binding == null) {
      out.println(JSFTestUtil.FAIL + " Unable to obtain ValueBinding for "
          + "'requestScope.bean.writable'");
      return;
    }

    if (binding.isReadOnly(getFacesContext())) {
      out.println(JSFTestUtil.FAIL + " Expected ValueBinding.isReadOnly("
          + "FacesContext) to return false for a Bean that had "
          + "both a getter and setter for a property.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ValueBinding.isReadOnly(FacesContext) throws NPE if arg is null
  public void valBindingIsReadOnlyNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ValueBinding binding = getApplication()
        .createValueBinding("#{param.testname}");

    try {
      binding.isReadOnly(null);
      out.println(JSFTestUtil.FAIL + " No Exception thrown when ValueBinding"
          + ".isReadOnly(FacesContext) was passed a null argument.");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when ValueBinding."
            + "isReadOnly(FacesContext) was passed a null argument,"
            + " but it wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // ValueBinding.setValue(FacesContext, Object)
  public void valBindingSetValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ValueBinding binding = getApplication()
        .createValueBinding("#{requestScope.tckAttr}");

    binding.setValue(getFacesContext(), "tckAttrValue");
    String value = (String) request.getAttribute("tckAttr");
    if (!"tckAttrValue".equals(value)) {
      out.println(JSFTestUtil.FAIL + " ValueBinding.setValue(FacesContext, "
          + "Object) failed to set the requested value into the"
          + "request scope.");
      out.println("Expected: tckAttrValue");
      out.println("Received: " + value);
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ValueBinding.setValue(FacesContext, Object) throws NPE if FacesContext
  // arg is null
  public void valBindingSetValueNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    ValueBinding binding = getApplication()
        .createValueBinding("#{requestScope.tckattr}");

    try {
      binding.setValue(null, "value");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when ValueBinding"
          + ".setValue(FacesContext, Object) was passed a null "
          + "FacesContext argument.");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when ValueBinding."
            + "setValue(FacesContext, Object) was passed a null "
            + "FacesContext argument but it wasn't an instance of "
            + "NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      binding.setValue(getFacesContext(), null);
    } catch (Exception e) {
      e.printStackTrace();
      out.println(JSFTestUtil.FAIL + " Exception thrown when a null value was"
          + " passed to ValueBinding.setValue(FacesContext, Object).");
      out.println(e.toString());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ValueBinding.setValue(FacesContext, Object) throws PNFE if property
  // doesn't exist or is read-only
  public void valBindingSetValuePNFETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    request.setAttribute("obj", request);
    ValueBinding binding = getApplication()
        .createValueBinding("#{requestScope.obj.foo}");

    try {
      binding.setValue(getFacesContext(), null);
      out.println(JSFTestUtil.FAIL + " No Exception thrown when ValueBinding."
          + "setValue(FacesContext, Object) with a non-existent property.");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown with ValueBinding"
            + " referenced a non-existent property, but was not"
            + " an instance of PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    // Now check for the PNFE if the property is not readable
    TestBean bean = getTestBean();
    request.setAttribute("tckBean", bean);
    binding = getApplication()
        .createValueBinding("#{requestScope.tckBean.readonly}");

    try {
      binding.setValue(getFacesContext(), null);
      out.println(JSFTestUtil.FAIL + " No Exception thrown when ValueBinding."
          + "setValue(FacesContext, Object) with a non-writable property.");
      return;
    } catch (Exception e) {
      if (!(e instanceof PropertyNotFoundException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown with ValueBinding"
            + " referenced a non-writable property, but was not"
            + " an instance of PropertyNotFoundException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // ------------------------------------------------------- Private Methods -

  private TestBean getTestBean() throws ServletException {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    TestBean bean;
    try {
      bean = (TestBean) Beans.instantiate(loader,
          "com.sun.ts.tests.jsf.api.javax_faces.el.common.TestBean");
    } catch (Exception e) {
      throw new ServletException(
          JSFTestUtil.FAIL + " Unable to instantiate TestBean", e);

    }
    return bean;
  }

}
