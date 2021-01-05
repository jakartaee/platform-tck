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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.body;

import java.io.IOException;
import java.io.StringWriter;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.DynamicAttributes;
import jakarta.servlet.jsp.tagext.JspFragment;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

public class SimpleBodyTag extends SimpleTagSupport
    implements DynamicAttributes {

  public void setDynamicAttribute(String s, String s1, Object o)
      throws JspException {
    // no-op
  }

  public void doTag() throws JspException, IOException {
    JspFragment body = getJspBody();
    if (body != null) {
      StringWriter writer = new StringWriter();
      body.invoke(writer);
      String bodyValue = writer.toString().trim();
      JspWriter out = getJspContext().getOut();
      if ("testpassed".equals(bodyValue)) {
        out.println("Test PASSED");
      } else {
        out.println("Test FAILED.  Expected body to be 'testpassed'."
            + "  Received: " + bodyValue);
      }
    }
  }
}
