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

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;

public class CheckInstanceSimpleTag extends SimpleTagSupport {
  public CheckInstanceSimpleTag() {
    super();
  }

  public void doTag() throws JspException, IOException {
    JspTestUtil.debug("[CheckInstanceSimpleTag] in doTag()");
    JspContext ctx = this.getJspContext();
    JspWriter out = ctx.getOut();

    Object obj = ctx.getAttribute("handlers", PageContext.REQUEST_SCOPE);
    List handlers = null;
    if (obj == null) {
      handlers = new ArrayList();
      ctx.setAttribute("handlers", handlers, PageContext.REQUEST_SCOPE);
    } else {
      handlers = (List) obj;
    }
    int numHandlers = handlers.size();
    out.println("Comparing with prior " + numHandlers
        + " instances of CheckInstanceSimpleTag");
    for (int i = 0; i < numHandlers; i++) {
      Object o = handlers.get(i);
      if (this == o) {
        throw new JspException(
            "Test FAILED. The same instance of CheckInstanceSimpleTag is used in this invocation: "
                + this);
      }
    }
    handlers.add(this);
  }
}
