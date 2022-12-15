/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsp.api.jakarta_el.valexpression;

import java.io.IOException;

import com.sun.ts.tests.common.el.api.expression.ExpressionTest;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import jakarta.el.ELContext;
import jakarta.el.ValueExpression;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

public class ValueExpressionTag extends SimpleTagSupport {

  private ValueExpression vexp;

  private static final String VALUE = "bar";

  private static final boolean READONLY = false;

  private static final boolean LITERAL_TEXT = false;

  public void setValExpr(ValueExpression vexp) {
    this.vexp = vexp;
  }

  public void doTag() throws JspException, IOException {

    ELContext elContext = getJspContext().getELContext();
    StringBuffer buf = new StringBuffer("");
    JspWriter out = getJspContext().getOut();

    try {
      boolean pass = ExpressionTest.testValueExpression(vexp, elContext,
          "#{foo}", String.class, VALUE, READONLY, LITERAL_TEXT, buf);
      out.println(buf.toString());
      if (pass)
        out.println("Test PASSED.");
      else
        out.println("Test FAILED.");
    } catch (Throwable t) {
      out.println(buf.toString());
      JspTestUtil.handleThrowable(t, out, "ValueExpressionTag");
    }
  }
}
