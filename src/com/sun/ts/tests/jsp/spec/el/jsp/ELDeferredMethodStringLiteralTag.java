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
import javax.el.MethodExpression;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class ELDeferredMethodStringLiteralTag extends SimpleTagSupport {
  private static final String NL = System.getProperty("line.separator", "\n");

  private static final String EXPECTEDSTR = "hello";

  private static final double EXPECTEDDBL = 3.1415926;

  private final static double EPSILON = 0.00000001;

  private MethodExpression strExpr, dblExpr;

  public void setStrExpr(MethodExpression strExpr) {
    this.strExpr = strExpr;
  }

  public void setDblExpr(MethodExpression dblExpr) {
    this.dblExpr = dblExpr;
  }

  public void doTag() throws JspException, IOException {
    ELContext elContext = getJspContext().getELContext();
    JspWriter out = getJspContext().getOut();
    boolean pass = true;

    try {
      String strResult = (String) strExpr.invoke(elContext, null);
      if (!strResult.equals(EXPECTEDSTR)) {
        pass = false;
        out.println("Test FAILED. Incorrect return value for strResult." + NL
            + "Expected value: " + EXPECTEDSTR + NL + "Value returned: "
            + strResult);
      }

      Object objResult = dblExpr.invoke(elContext, null);
      if (!(objResult instanceof Double)) {
        pass = false;
        out.println("Test FAILED. Return value is not a Double:" + NL
            + objResult.getClass() + NL);
      }
      double dblResult = ((Double) objResult).doubleValue();
      if (!(Math.abs(dblResult - EXPECTEDDBL) < EPSILON)) {
        pass = false;
        out.println("Test FAILED. Incorrect value for dblResult." + NL
            + "Expected value: " + EXPECTEDDBL + NL + "Value returned: "
            + dblResult);
      }

      if (pass == true)
        out.println("Test PASSED.");

    } catch (Throwable t) {
      JspTestUtil.handleThrowable(t, out, "ELDeferredMethodStringLiteralTag");
    }
  }
}
