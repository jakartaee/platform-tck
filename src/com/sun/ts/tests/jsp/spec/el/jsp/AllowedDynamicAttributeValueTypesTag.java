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

/*
 * $Id$
 */

package com.sun.ts.tests.jsp.spec.el.jsp;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import javax.el.ValueExpression;
import javax.el.MethodExpression;
import java.io.IOException;

public class AllowedDynamicAttributeValueTypesTag extends TagSupport {

  private String litExpr;

  private String scriptExpr;

  private String dollarExpr;

  private ValueExpression valueExpr;

  private MethodExpression methodExpr;

  private boolean valExprSet;

  public void setLitExpr(String litExpr) {
    this.litExpr = litExpr;
  }

  public void setScriptExpr(String scriptExpr) {
    this.scriptExpr = scriptExpr;
  }

  public void setDollarExpr(String dollarExpr) {
    this.dollarExpr = dollarExpr;
  }

  public void setValueExpr(Object expr) {
    if (expr instanceof ValueExpression) {
      this.valueExpr = (ValueExpression) expr;
      valExprSet = true;
    } else {
      this.valueExpr = null;
      valExprSet = false;
    }
  }

  public void setMethodExpr(MethodExpression methodExpr) {
    this.methodExpr = methodExpr;
  }

  public int doStartTag() throws JspException {
    try {
      pageContext.getOut().println("litExpr is " + litExpr);
      pageContext.getOut().println("scriptExpr is " + scriptExpr);
      pageContext.getOut().println("dollarExpr is " + dollarExpr);
      pageContext.getOut()
          .println("valueExpr is " + ((valExprSet) ? valueExpr : "null"));
      pageContext.getOut().println("methodExpr is " + methodExpr);
      if (valExprSet)
        pageContext.getOut().println("Test PASSED.");
    } catch (IOException ioe) {
      throw new JspException("Unexpected Exception", ioe);
    }
    return SKIP_BODY;
  }
}
