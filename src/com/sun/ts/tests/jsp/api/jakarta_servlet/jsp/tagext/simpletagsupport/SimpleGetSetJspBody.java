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

/*
 * @(#)SimpleGetSetJspBody.java 1.1 10/31/02
 */

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.simpletagsupport;

import java.io.IOException;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.JspFragment;
import jakarta.servlet.jsp.tagext.SimpleTagSupport;

public class SimpleGetSetJspBody extends SimpleTagSupport {

  /**
   * Default constructor.
   */
  public SimpleGetSetJspBody() {
    super();
  }

  /**
   * Validate that getJspBody() returns a non null value. This indirectly
   * ensures that the container properly called setJspBody().
   * 
   * @throws JspException
   *           - if an unexpected error occurs
   * @throws IOException
   *           - if an unexpected I/O error occurs
   */
  public void doTag() throws JspException, IOException {
    JspTestUtil.debug("[SimpleGetSetJspBody] in doGet()");
    JspFragment fragment = this.getJspBody();
    if (fragment != null) {
      this.getJspContext().getOut().println("Test PASSED");
    } else {
      this.getJspContext().getOut().println(
          "Test FAILED.  JspContext.getJspBody()" + " returned a null value.");
    }
  }
}
