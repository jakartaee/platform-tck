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

public class SetJspIdTag extends SimpleTagSupport implements JspIdConsumer {

  private String jspId;

  private JspWriter out;

  public void setJspId(String id) {
    jspId = id;
  }

  public void doTag() throws JspException, IOException {

    out = getJspContext().getOut();

    try {
      if (jspId == null) {
        out.println("No ID assigned. Test FAILED");
        return;
      }
      out.println("JspId is " + jspId);
      if (isConformant()) {
        out.println("Test PASSED");
      }
    } catch (Throwable t) {
      JspTestUtil.handleThrowable(t, out, "SetJspIdTag");
    }
  }

  private boolean isConformant() throws IOException {

    char firstChar = jspId.charAt(0);

    if (!Character.isLetter(firstChar) && firstChar != '_') {
      out.println("First character of ID is not letter or underscore");
      return false;
    }

    for (int i = 1; i < jspId.length(); ++i) {
      char ch = jspId.charAt(i);
      if (!Character.isLetter(ch) && !Character.isDigit(ch) && ch != '-'
          && ch != '_') {
        out.print("character " + i + " is neither letter, ");
        out.println("digit, dash, or underscore");
        return false;
      }
    }
    return true;
  }
}
