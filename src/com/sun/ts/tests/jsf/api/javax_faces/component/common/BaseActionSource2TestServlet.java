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

import java.io.IOException;
import java.io.PrintWriter;

import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.faces.component.ActionSource2;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspFactory;

import com.sun.ts.tests.jsf.common.util.JSFTestUtil;

public abstract class BaseActionSource2TestServlet
    extends BaseActionSourceTestServlet {

  private ServletContext servletContext;

  /**
   * <p>
   * Initialize this <code>Servlet</code> instance.
   * </p>
   *
   * @param config
   *          the configuration for this <code>Servlet</code>
   *
   * @throws javax.servlet.ServletException
   *           indicates initialization failure
   */
  public void init(ServletConfig config) throws ServletException {

    servletContext = config.getServletContext();
    super.init(config);

  } // init

  // ------------------------------------------------------------ Test Methods

  public void actionSource2GetSetActionExpressionTest(
      HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {

    PrintWriter out = response.getWriter();
    request.setAttribute("bean", new SimpleBean());
    ExpressionFactory factory = JspFactory.getDefaultFactory()
        .getJspApplicationContext(servletContext).getExpressionFactory();
    MethodExpression expression = factory.createMethodExpression(
        getFacesContext().getELContext(), "#{bean.action}",
        java.lang.String.class, new Class[] {});

    ActionSource2 source2 = (ActionSource2) createComponent();

    source2.setActionExpression(expression);

    if (source2.getActionExpression() != expression) {
      out.println(JSFTestUtil.FAIL + " Unexpected return value from"
          + " getActionExpression() after having just called"
          + " setActionExpression().");
      out.println("Expected: " + expression);
      out.println("Received: " + source2.getActionExpression());
      return;
    }

    out.println(JSFTestUtil.PASS);

  } // END actionSource2GetSetMethodExpressionTest

  // ----------------------------------------------------------- Inner Classes

  private static class SimpleBean {

    public String getAction() {

      return "action";

    } // END getAction

  } // END SimpleBean
}
