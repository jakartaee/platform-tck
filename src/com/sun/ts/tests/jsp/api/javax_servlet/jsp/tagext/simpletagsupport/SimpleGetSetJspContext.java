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

/*
 * $Id$
 */

/*
 * @(#)SimpleGetSetJspContext.java 1.1 10/31/02
 */

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.simpletagsupport;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspContext;
import java.io.IOException;

public class SimpleGetSetJspContext extends SimpleTagSupport {

  /**
   * Default constructor.
   */
  public SimpleGetSetJspContext() {
    super();
  }

  /**
   * Validates that getJspContext() returns a non null value. This indirectly
   * ensures that the container properly called setJspContext().
   *
   * @throws JspException
   *           - if an unexpected error occured.
   * @throws IOException
   *           - if an unexpected I/O error occured.
   */
  public void doTag() throws JspException, IOException {
    JspTestUtil.debug("[SimpleGetSetJspcontext] in doTag()");
    JspContext ctx = this.getJspContext();

    if (ctx != null) {
      ctx.getOut().println("Test PASSED");
    } else {
      throw new JspException(
          "Test FAILED.  PageContext.getJspContext()" + "returned null.");
    }
  }
}
