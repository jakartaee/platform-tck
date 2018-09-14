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

package com.sun.ts.tests.jsp.api.javax_el.beanelresolver;

import com.sun.ts.tests.common.el.api.resolver.ResolverTest;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import javax.el.ELContext;
import javax.el.BeanELResolver;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

public class BeanELResolverTag extends SimpleTagSupport {

  public void doTag() throws JspException, IOException {

    StringBuffer buf = new StringBuffer();
    String property = "intention";

    SimpleBean bean = new SimpleBean();
    bean.setIntention("bad");

    JspWriter out = getJspContext().getOut();
    ELContext context = getJspContext().getELContext();
    BeanELResolver beanResolver = new BeanELResolver();

    try {
      boolean pass = ResolverTest.testELResolver(context, beanResolver, bean,
          property, "good", buf, false);
      out.println(buf.toString());
      if (pass == true)
        out.println("Test PASSED");
    } catch (Throwable t) {
      out.println("contents of buffer:\n" + buf.toString());
      JspTestUtil.handleThrowable(t, out, "BeanELResolverTag");
    }
  }
}
