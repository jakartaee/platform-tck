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
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class ELJspVersionTag extends SimpleTagSupport {

  private String poundExpr;

  public void setPoundExpr(String poundExpr) {
    this.poundExpr = poundExpr;
  }

  public void doTag() throws JspException, IOException {
    JspWriter out = getJspContext().getOut();

    try {
      out.println(poundExpr);
    } catch (Throwable t) {
      JspTestUtil.handleThrowable(t, out, "ELJspVersionTag");
    }
  }
}
