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

package com.sun.ts.tests.jsp.common.tags.tck;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

/**
 * This tag is a simplified version of the jstl c:set tag. It assigns a String
 * value to a variable.
 */
public class SetTag extends SimpleTagSupport {

  private String var, value;

  public void setVar(String var) {
    this.var = var;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public void doTag() throws JspException {
    getJspContext().setAttribute(var, value, PageContext.PAGE_SCOPE);
  }
}
