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

package com.sun.ts.tests.jsf.api.javax_faces.el.common;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.FacesException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;

public abstract class BaseELExceptionTestServlet extends HttpTCKServlet {

  private static final String EXCEPTION_MESSAGE = "exception message";

  private Class exceptionClass;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ------------------------------------------- Test Methods ----

  public void noArgCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Constructor ctor;
    try {
      ctor = exceptionClass.getDeclaredConstructor(null);
    } catch (NoSuchMethodException nsme) {
      out.println(JSFTestUtil.FAIL + " Unable to locate no-arg ctor"
          + " for Exception class: " + exceptionClass.getName());
      out.println(nsme);
      return;
    }

    try {
      ctor.newInstance(null);
    } catch (Exception e) {
      out.println("Unable to create instance of Exception class '"
          + exceptionClass.getName() + "' using no-arg ctor.");
      out.println(e.toString());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void messageArgCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Constructor ctor;
    try {
      ctor = exceptionClass
          .getDeclaredConstructor(new Class[] { String.class });
    } catch (NoSuchMethodException nsme) {
      out.println(JSFTestUtil.FAIL + " Unable to located message arg ctor"
          + " for Exception class: " + exceptionClass.getName());
      out.println(nsme);
      return;
    }

    try {
      FacesException fe = (FacesException) ctor
          .newInstance(new Object[] { EXCEPTION_MESSAGE });
      String message = fe.getMessage().trim();
      if (!EXCEPTION_MESSAGE.equals(message)) {
        out.println(JSFTestUtil.FAIL + " Unexpected message received"
            + " when getMessage() was called.");
        out.println("Expected: " + EXCEPTION_MESSAGE);
        out.println("Received: " + message);
      }
    } catch (Exception e) {
      out.println("Unable to create instance of Exception class '"
          + exceptionClass.getName() + "' using String arg ctor.");
      out.println(e.toString());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void rootCauseArgCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Constructor ctor;
    try {
      ctor = exceptionClass
          .getDeclaredConstructor(new Class[] { Throwable.class });
    } catch (NoSuchMethodException nsme) {
      out.println(JSFTestUtil.FAIL + " Unable to located root cause arg ctor"
          + " for Exception class: " + exceptionClass.getName());
      out.println(nsme);
      return;
    }

    try {
      ServletException se = new ServletException();
      FacesException fe = (FacesException) ctor
          .newInstance(new Object[] { se });
      Throwable t = fe.getCause();
      if (!se.equals(t)) {
        out.println("Unexpected exception type received when calling"
            + " getCause on newly created instance.");
        out.println("Received: " + t.getClass().getName());
      }
    } catch (Exception e) {
      out.println("Unable to create instance of Exception class '"
          + exceptionClass.getName() + "' using Throwable" + " arg ctor.");
      out.println(e.toString());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  public void rootCauseMessageArgsCtorTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    Constructor ctor;
    try {
      ctor = exceptionClass.getDeclaredConstructor(
          new Class[] { String.class, Throwable.class });
    } catch (NoSuchMethodException nsme) {
      out.println(JSFTestUtil.FAIL + " Unable to located message/root cause "
          + "arg ctor for Exception class: " + exceptionClass.getName());
      out.println(nsme);
      return;
    }

    try {
      FacesException fe = (FacesException) ctor.newInstance(
          new Object[] { EXCEPTION_MESSAGE, new ServletException() });

      String message = fe.getMessage().trim();
      if (!EXCEPTION_MESSAGE.equals(message)) {
        out.println(JSFTestUtil.FAIL + " Unexpected message received"
            + " when getMessage() was called.");
        out.println("Expected: " + EXCEPTION_MESSAGE);
        out.println("Received: " + message);
      }

      ServletException se = new ServletException();
      Throwable t = fe.getCause();
      if (!se.equals(t)) {
        out.println("Unexpected exception type received when calling"
            + " getCause on newly created instance.");
        out.println("Received: " + t.getClass().getName());
      }
    } catch (Exception e) {
      out.println("Unable to create instance of Exception class '"
          + exceptionClass.getName() + "' using "
          + "String/Throwable arg ctor.");
      out.println(e.toString());
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // ------------------------------------------- Protected Methods
  // ---------------

  protected void setExceptionClass(Class exceptionClass) {
    this.exceptionClass = exceptionClass;
  }

}
