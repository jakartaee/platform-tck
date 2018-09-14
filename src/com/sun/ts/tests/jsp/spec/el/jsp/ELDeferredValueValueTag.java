/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsp.spec.el.jsp;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class ELDeferredValueValueTag extends SimpleTagSupport {

  private final static String LITVAL = "foo";

  private final static String POUNDVAL = "bar";

  private ValueExpression litExpr;

  private ValueExpression poundExpr;

  public void setLitExpr(ValueExpression litExpr) {
    this.litExpr = litExpr;
  }

  public void setPoundExpr(ValueExpression poundExpr) {
    this.poundExpr = poundExpr;
  }

  public void doTag() throws JspException, IOException {
    ELContext elContext = getJspContext().getELContext();
    JspWriter out = getJspContext().getOut();

    try {
      String litVal = (String) litExpr.getValue(elContext);
      if (!litVal.equals(LITVAL)) {
        out.println("Test FAILED. Wrong value for literal expression.");
        out.println("Expected value: " + LITVAL);
        out.println("Retrieved value: " + litVal);
        return;
      }
      String poundVal = (String) poundExpr.getValue(elContext);
      if (!poundVal.equals(POUNDVAL)) {
        out.println("Test FAILED. Wrong value for pound expression.");
        out.println("Expected value: " + POUNDVAL);
        out.println("Retrieved value: " + poundVal);
        return;
      }
      out.println("Test PASSED.");
    } catch (Throwable t) {
      JspTestUtil.handleThrowable(t, out, "ELDeferredValueValueTag");
    }
  }
}
