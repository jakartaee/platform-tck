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

package com.sun.ts.tests.jsp.spec.core_syntax.directives.page;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import jakarta.el.ELContext;
import jakarta.el.ValueExpression;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class IsELIgnoredFalseActionTag extends SimpleTagSupport {

  private String dollarValue;

  private ValueExpression poundExpr;

  public void setDollarExpr(String dollarExpr) {
    this.dollarValue = dollarExpr;
  }

  public void setPoundExpr(ValueExpression poundExpr) {
    this.poundExpr = poundExpr;
  }

  public void doTag() throws JspException, IOException {
    ELContext elContext = getJspContext().getELContext();
    JspWriter out = getJspContext().getOut();

    try {
      String poundValue = (String) poundExpr.getValue(elContext);
      out.println("Non-deferred value = " + dollarValue);
      out.println("Deferred value = " + poundValue);
    } catch (Throwable t) {
      JspTestUtil.handleThrowable(t, out, "IsELIgnoredFalseActionTag");
    }
  }
}
