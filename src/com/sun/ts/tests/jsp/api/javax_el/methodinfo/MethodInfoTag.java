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

package com.sun.ts.tests.jsp.api.javax_el.methodinfo;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import com.sun.ts.tests.common.el.api.expression.ExpressionTest;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.el.ELContext;
import javax.servlet.jsp.*;
import javax.servlet.jsp.tagext.*;
import java.io.*;

public class MethodInfoTag extends SimpleTagSupport {

  private MethodExpression mexp;

  public void setMethExpr(MethodExpression mexp) {
    this.mexp = mexp;
  }

  public void doTag() throws JspException, IOException {

    JspWriter out = getJspContext().getOut();
    StringBuffer buf = new StringBuffer("");
    ELContext elContext = getJspContext().getELContext();

    try {
      MethodInfo minfo = mexp.getMethodInfo(elContext);
      Class[] paramTypes = { Object.class };
      boolean pass = ExpressionTest.testMethodInfo(minfo, "add", boolean.class,
          1, paramTypes, buf);

      if (!pass) {
        out.println("Test FAILED: return from testMethodExpression\n");
        out.println("Buffer contents:\n" + buf.toString());
        return;
      }

      mexp.invoke(elContext, new Object[] { "latest member" });
    } catch (Throwable t) {
      out.println("Test FAILED: Exception in tag handler\n");
      out.println("Buffer contents:\n" + buf.toString());
      JspTestUtil.handleThrowable(t, out, "MethodInfoTag");
    }
  }
}
