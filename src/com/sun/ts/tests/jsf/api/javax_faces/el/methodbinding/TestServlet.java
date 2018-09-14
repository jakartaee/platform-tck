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

package com.sun.ts.tests.jsf.api.javax_faces.el.methodbinding;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;
import com.sun.ts.tests.jsf.common.beans.TestBean;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.faces.application.Application;
import javax.faces.el.MethodBinding;
import javax.faces.el.EvaluationException;
import javax.faces.context.FacesContext;

import java.io.IOException;
import java.io.PrintWriter;

public class TestServlet extends HttpTCKServlet {

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

  // ---------------------------------------------------------------- Test
  // Methods

  // MethodBinding.invoke()
  public void methBindingInvokeTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    TestBean testBean = new TestBean();
    request.setAttribute("TestBean", testBean);
    FacesContext context = getFacesContext();
    Application application = getApplication();

    MethodBinding binding = application
        .createMethodBinding("#{TestBean.getBoolProp}", null);

    Object ret = binding.invoke(context, null);
    if (!Boolean.FALSE.equals(ret)) {
      out.println(
          JSFTestUtil.FAIL + " Expected the return value of getBoolProp()"
              + " to be a Boolean.FALSE");
      out.println("Type returned: " + ret.getClass().getName());
      out.println("String value: " + ret.toString());
      return;
    }

    binding = application.createMethodBinding("#{TestBean.setBoolProp}",
        new Class[] { Boolean.TYPE });
    ret = binding.invoke(context, new Object[] { Boolean.TRUE });
    if (ret != null) {
      out.println(JSFTestUtil.FAIL
          + " Expected the return value of setBoolProp()" + " to be null");
      out.println("Type returned: " + ret.getClass().getName());
      out.println("String value: " + ret.toString());
      return;
    }

    binding = application.createMethodBinding("#{TestBean.getBoolProp}", null);

    ret = binding.invoke(context, null);
    if (!Boolean.TRUE.equals(ret)) {
      out.println(
          JSFTestUtil.FAIL + " Expected the return value of getBoolProp()"
              + " to be a Boolean.TRUE");
      out.println("Type returned: " + ret.getClass().getName());
      out.println("String value: " + ret.toString());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // MethodBinding.getType() throws NPE if context argument is null
  public void methBindingGetTypeNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    TestBean testBean = new TestBean();
    request.setAttribute("TestBean", testBean);

    MethodBinding binding = getApplication()
        .createMethodBinding("#{TestBean.noSuchMethod}", null);

    try {
      binding.getType(null);
      out.println(JSFTestUtil.FAIL + " No Exception thrown when MethodBinding."
          + "getType() was passed a null FacesContext argument.");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when MethodBinding"
            + ".getType() was passed a null FacesContext argument,"
            + " but it wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // MethodBinding.invoke() throws NPE if context argument is null
  public void methBindingInvokeNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    TestBean testBean = new TestBean();
    request.setAttribute("TestBean", testBean);

    MethodBinding binding = getApplication()
        .createMethodBinding("#{TestBean.noSuchMethod}", null);

    try {
      binding.invoke(null, new Class[] { String.class });
      out.println(JSFTestUtil.FAIL + " No Exception thrown when MethodBinding."
          + "invoke() was passed a null FacesContext argument.");
      return;
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when MethodBinding"
            + ".invoke() was passed a null FacesContext argument,"
            + " but it wasn't an instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // MethodBinding.getType() throws MethodNotFoundException if specified
  // method is not found
  public void methBindingGetTypeMethNotFoundExcTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    TestBean testBean = new TestBean();
    request.setAttribute("TestBean", testBean);

    MethodBinding binding = getApplication()
        .createMethodBinding("#{TestBean.noSuchMethod}", null);

    try {
      binding.getType(getFacesContext());
      out.println(JSFTestUtil.FAIL + " No Exception thrown by MethodBinding."
          + "invoke() when the method being invoked does not exist.");
      return;
    } catch (Exception e) {
      if (!(e instanceof EvaluationException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown by MethodBinding."
            + "invoke() when the method being invoked does not exist"
            + ", but it wasn't an instance of EvaluationException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

  // MethodBinding.getInvoke() throws MethodNotFoundException if specified
  // method is not found
  public void methBindingInvokeMethNotFoundExcTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    TestBean testBean = new TestBean();
    request.setAttribute("TestBean", testBean);

    MethodBinding binding = getApplication()
        .createMethodBinding("#{TestBean.noSuchMethod}", null);

    try {
      binding.invoke(getFacesContext(), null);
      out.println(JSFTestUtil.FAIL + " No Exception thrown by MethodBinding."
          + "getType() when the method being invoked does not exist.");
      return;
    } catch (Exception e) {
      if (!(e instanceof EvaluationException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown by MethodBinding."
            + "getType() when the method being invoked does not exist"
            + ", but it wasn't an instance of EvaluationException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);

  }

  // MethodBinding.getInvoke() throws EvaluationException if called method
  // throws Exception
  public void methBindingInvokeEvalExcTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    TestBean testBean = new TestBean();
    request.setAttribute("TestBean", testBean);

    MethodBinding binding = getApplication()
        .createMethodBinding("#{TestBean.getException}", null);

    try {
      binding.invoke(getFacesContext(), null);
      out.println(JSFTestUtil.FAIL + " No Exception thrown by MethodBinding."
          + "invoke() when the method being invoked throws an Exception.");
      return;
    } catch (Exception e) {
      if (!(e instanceof EvaluationException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown by MethodBinding."
            + "invoke() when the method being invoked threw an "
            + "exception, but it wasn't an instance of "
            + "EvaluationException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
      Throwable rootCause = ((EvaluationException) e).getCause();
      if (!(rootCause instanceof IllegalStateException)) {
        out.println(JSFTestUtil.FAIL + " EvaluationException didn't return"
            + " the expected root cause Exception.");
        out.println("Expected: IllegalStateException");
        out.println("Received: " + rootCause.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);

  }

}
