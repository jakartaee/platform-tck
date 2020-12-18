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

/*
 * $Id$
 */

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.jspidconsumer;

import java.io.IOException;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.JspIdConsumer;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

public class MultiOneTag extends SimpleTagSupport implements JspIdConsumer {

  private static String jspId;

  private JspWriter out;

  public void setJspId(String id) {
    jspId = id;
  }

  public static String getJspId() {
    return jspId;
  }

  public void doTag() throws JspException, IOException {

    out = getJspContext().getOut();

    try {
      out.println("MultiOneTag: JspId is " + jspId);
    } catch (Throwable t) {
      JspTestUtil.handleThrowable(t, out, "MultiOneTag");
    }
  }
}
