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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.simpletagsupport;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.JspFragment;
import java.io.IOException;

public class SimpleJspFragmentGetJspContext extends SimpleTagSupport {
  public SimpleJspFragmentGetJspContext() {
    super();
  }

  public void doTag() throws JspException, IOException {
    JspTestUtil.debug("[SimpleJspFragmentGetJspContext] in doTag()");
    JspFragment aBody = getJspBody();
    JspContext ctx = aBody.getJspContext();
    if (ctx == null) {
      this.getJspContext().getOut().println(
          "Test FAILED in simple tag. jspFragment.getJspContext() returned null.");
    } else {
      ctx.getOut().println("Test PASSED in simple tag.");
    }
  }
}
