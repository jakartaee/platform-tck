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

package com.sun.ts.tests.jsf.api.javax_faces.el.variableresolver;

import com.sun.ts.tests.jsf.common.servlets.HttpTCKServlet;
import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

import javax.faces.el.VariableResolver;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

public final class TestServlet extends HttpTCKServlet {

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }

  // ------------------------------------------- Test Methods ----

  // VariableResolver.resolveVariable(FacesContext, String)
  public void varResolverResolveVariableTest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    VariableResolver resolver = getApplication().getVariableResolver();

    if (resolver == null) {
      out.println(JSFTestUtil.FAIL + " Unable to obtain VariableResolver"
          + " instance from Application object.");
      return;
    }

    Object result = resolver.resolveVariable(getFacesContext(), "param");

    if (result == null) {
      out.println(JSFTestUtil.FAIL + " VariableResolver failed to resolve "
          + "implicit 'param' object.");
      return;
    }

    result = resolver.resolveVariable(getFacesContext(), "tckNoSuchObject");

    if (result != null) {
      out.println(JSFTestUtil.FAIL + " VariableResolver returned a non null "
          + "result for variable 'tckNoSuchObject' when null "
          + "was expected.");
      return;
    }

    out.println(JSFTestUtil.PASS);
  }

  // VariableResolver.resolveVariable(FacesContext, String) throws NPE
  // if either arg is null
  public void varResolverResolveVariableNPETest(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    PrintWriter out = response.getWriter();
    VariableResolver resolver = getApplication().getVariableResolver();

    if (resolver == null) {
      out.println(JSFTestUtil.FAIL + " Unable to obtain VariableResolver"
          + " instance from Application object.");
      return;
    }

    try {
      resolver.resolveVariable(null, "param");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when null "
          + "FacesContext was passed to VariableResolver."
          + "resolverVariable().");
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when "
            + "VariableResolver.resolveVariable() was"
            + " passed a null FacesContext, but it wasn't an"
            + "instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    try {
      resolver.resolveVariable(null, "param");
      out.println(JSFTestUtil.FAIL + " No Exception thrown when null "
          + "var name was passed to VariableResolver." + "resolverVariable().");
    } catch (Exception e) {
      if (!(e instanceof NullPointerException)) {
        out.println(JSFTestUtil.FAIL + " Exception thrown when "
            + "VariableResolver.resolveVariable() was"
            + " passed a null var name, but it wasn't an"
            + "instance of NullPointerException.");
        out.println("Exception received: " + e.getClass().getName());
        return;
      }
    }

    out.println(JSFTestUtil.PASS);
  }

}
