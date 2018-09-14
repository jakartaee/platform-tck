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

public class ELDeferredValueCoercionTag extends SimpleTagSupport {

  private final static int EXPECTEDINTVAL = 8128;

  private final static String EXPECTEDBOOKVAL = "Moby Dick";

  private ValueExpression intExpr, bookExpr;

  public void setIntExpr(ValueExpression intExpr) {
    this.intExpr = intExpr;
  }

  public void setBookExpr(ValueExpression bookExpr) {
    this.bookExpr = bookExpr;
  }

  public void doTag() throws JspException, IOException {
    ELContext elContext = getJspContext().getELContext();
    JspWriter out = getJspContext().getOut();

    try {
      Class expectedIntClass = intExpr.getExpectedType();
      if (!expectedIntClass.getName().equals("int")) {
        out.println("Test FAILED. Expected type = int");
        out.println("Got type = " + expectedIntClass.getName());
        return;
      }
      Object intVal = intExpr.getValue(elContext);
      int coercedIntVal = ((Integer) intVal).intValue();
      if (coercedIntVal != EXPECTEDINTVAL) {
        out.println("Test FAILED. Wrong value for int expression.");
        out.println("Expected value: " + EXPECTEDINTVAL);
        out.println("Got value: " + coercedIntVal);
        return;
      }

      Class expectedBookClass = bookExpr.getExpectedType();
      if (!expectedBookClass.getName().equals("java.lang.String")) {
        out.println("Test FAILED. Expected type = java.lang.String");
        out.println("Got type = " + expectedBookClass.getName());
        return;
      }
      Object bookVal = bookExpr.getValue(elContext);
      String coercedBookVal = (String) bookVal;
      if (!coercedBookVal.equals(EXPECTEDBOOKVAL)) {
        out.println("Test FAILED. Wrong value for book expression.");
        out.println("Expected value: " + EXPECTEDBOOKVAL);
        out.println("Got value: " + coercedBookVal);
        return;
      }

      out.println("Test PASSED.");
    } catch (Throwable t) {
      JspTestUtil.handleThrowable(t, out, "ELDeferredValueCoercionTag");
    }
  }
}
