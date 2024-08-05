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

package com.sun.ts.tests.jstl.common.tags;

import java.util.ArrayList;

import jakarta.el.ValueExpression;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.PageContext;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

public class SaveTag extends SimpleTagSupport {

  private ValueExpression attr;

  public void setAttr(ValueExpression attr) {
    this.attr = attr;
  }

  public void doTag() throws JspException {

    PageContext pc = (PageContext) getJspContext();

    try {
      ArrayList list = (ArrayList) pc.getAttribute("alist",
          PageContext.APPLICATION_SCOPE);
      if (list == null) {
        list = new ArrayList();
        pc.setAttribute("alist", list, PageContext.APPLICATION_SCOPE);
      }
      list.add(attr);
    } catch (Exception ex) {
      throw new JspException(ex);
    }
  }
}
