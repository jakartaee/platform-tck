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

package com.sun.ts.tests.jsf.api.javax_faces.component.common;

import javax.faces.component.ValueHolder;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * <p>
 * Base tests for methods defined in {@link ValueHolder}.
 * </p>
 */
public abstract class BaseValueHolderTestServlet
    extends BaseUIComponentTestServlet {

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
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ------------------------------------------- Test Methods ----

  // ValueHolder.getValue(), ValueHolder.setValue()
  public void valueHolderGetSetValueTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();

    ValueHolder holder = (ValueHolder) createComponent();
    Object value = createComponent();
    holder.setValue(value);
    if (!value.equals(holder.getValue())) {
      out.println(JSFTestUtil.FAIL + " getValue() failed to return the "
          + "expected value as set by setValue().");
      out.println("Expected: " + value);
      out.println("Received: " + holder.getValue());
      return;
    }

    // make sure we can set null
    holder.setValue(null);
    if (holder.getValue() != null) {
      out.println(JSFTestUtil.FAIL + " getValue() failed to return null"
          + " as set by setValue().");
      out.println("Value received: " + holder.getValue());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ValueHolder.getValueRef(), ValueHolder.setValueRef()
  // public void valueHolderGetSetValueRefTest(HttpServletRequest request,
  // HttpServletResponse response)
  // throws ServletException, IOException {
  // PrintWriter out = response.getWriter();
  //
  // ValueHolder holder = (ValueHolder) createComponent();
  //
  // holder.setValueRef("requestScope");
  // if (!"requestScope".equals(holder.getValueRef())) {
  // out.println(FAIL + " getValueRef() failed to return the " +
  // "expected value as set by setValueRef().");
  // out.println("Expected: requestScope");
  // out.println("Received: " + holder.getValueRef());
  // return;
  // }
  //
  // // make sure we can set null
  // holder.setValueRef(null);
  // if (holder.getValueRef() != null) {
  // out.println(FAIL + " getValueRef() failed to return null" +
  // " as set by setValueRef().");
  // out.println("Value received: " + holder.getValueRef());
  // return;
  // }
  //
  // out.println(PASS);
  // }
  //
  //
  // // ValueHolder.getCurrentValue()
  // public void valueHolderGetCurrentValueTest(HttpServletRequest request,
  // HttpServletResponse response)
  // throws ServletException, IOException {
  // PrintWriter out = response.getWriter();
  //
  // ValueHolder holder = (ValueHolder) createComponent();
  // FacesContext context = getFacesContext();
  //
  // holder.setValueRef(null);
  // holder.setValue(null);
  //
  // request.setAttribute("valueRef", "valueRef value");
  //
  // Object result = holder.currentValue(context);
  //
  // if (result != null) {
  // out.println(FAIL + " Expected currentValue() to return null" +
  // " if no local value or valueRef were set on the" +
  // " ValueHolder.");
  // out.println("Value received: " + result);
  // return;
  // }
  //
  // // now set a local value and valueRef. The local value should be
  // // returned
  // holder.setValue("local value");
  // holder.setValueRef("requestScope.valueRef");
  //
  // result = holder.currentValue(context);
  // if (!"local value".equals(result)) {
  // out.println(FAIL + " Expected currentValue() to return the" +
  // " local value of the component when local value has been" +
  // " specified.");
  // out.println("Value expected: local value");
  // out.println("Value received: " + result);
  // return;
  // }
  //
  // holder.setValue(null);
  //
  // result = holder.currentValue(context);
  // if (!"valueRef value".equals(result)) {
  // out.println(FAIL + " Expected currentValue to return the" +
  // " result of the value reference expression when no local" +
  // " value has been set on the ValueHolder.");
  // out.println("Value expected: valueRef value");
  // out.println("Value received: " + result);
  // return;
  // }
  //
  // out.println(PASS);
  // }
  //
  //
  // // ValueHolder.currentValue() throws NPE if context argument is null
  // public void valueHolderCurrentValueNPETest(HttpServletRequest request,
  // HttpServletResponse response)
  // throws ServletException, IOException {
  // PrintWriter out = response.getWriter();
  //
  // ValueHolder holder = (ValueHolder) createComponent();
  // try {
  // holder.currentValue(null);
  // out.println(FAIL + " No Exception thrown when a null " +
  // "FacesContext argument was passed to currentValue()");
  // return;
  // } catch (Exception e) {
  // if (!(e instanceof NullPointerException)) {
  // out.println(FAIL + " Test FAILED. Exception thrown when" +
  // " a null FacesContext argument was passed to " +
  // "currentValue() but it wasn't an instance of" +
  // " NullPointerException.");
  // out.println("Exeception received: " + e.getClass().getName());
  // return;
  // }
  // }
  //
  // out.println(PASS);
  // }
  //
  //
  // // ValueHolder.currentValue() throws EvaluationException if valueRef cannot
  // // be evaluated.
  // public void valueHolderCurrentValueEvalExcTest(HttpServletRequest request,
  // HttpServletResponse response)
  // throws ServletException, IOException {
  // PrintWriter out = response.getWriter();
  //
  // ValueHolder holder = (ValueHolder) createComponent();
  // holder.setValue(null);
  // holder.setValueRef("requestScope.bean.invalidProperty");
  //
  // request.setAttribute("bean", new Bean());
  //
  // try {
  // holder.currentValue(getFacesContext());
  // out.println(FAIL + " No Exception thrown when currentValue() " +
  // "was called with a valueRef that should fail to evaluate" +
  // " at runtime.");
  // return;
  // } catch (Exception e) {
  // if (!(e instanceof EvaluationException)) {
  // out.println(FAIL + " Test FAILED. Exception thrown when" +
  // " currentValue() was called with a valueRef that should" +
  // " fail to evaluate at runtime, but it wasn't an" +
  // " instance of EvaluationException.");
  // out.println("Exeception received: " + e.getClass().getName());
  // return;
  // }
  // }
  //
  // out.println(PASS);
  // }

  // ------------------------------------------------------------ PrivateClasses

  private static class Bean {
    private String value;

    public void setValue(String value) {
      this.value = value;
    }

    public String getValue() {
      return value;
    }
  }

}
