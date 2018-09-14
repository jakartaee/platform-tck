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

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.JspTag;
import javax.servlet.jsp.tagext.SimpleTag;
import javax.servlet.jsp.JspContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.JspFragment;
import java.io.IOException;

public class ClassicJspFragmentGetJspContext extends TagSupport {

  /**
   * JspFragment instance.
   */
  Object _fragment = null;

  /**
   * Sets _fragment. This should be an instance of JspFragment.
   * 
   * @param o
   *          - a JspFragment instance
   */
  public void setFragment(Object o) {
    _fragment = o;
  }

  public int doStartTag() throws JspException {
    JspWriter out = pageContext.getOut();
    try {
      if (_fragment != null) {
        if (_fragment instanceof JspFragment) {
          JspContext ctx = ((JspFragment) _fragment).getJspContext();
          if (ctx == null) {
            out.println(
                "Test FAILED in classic tag. jspFragment.getJspContext() returned null.");
          } else {
            ctx.getOut().println("Test PASSED in classic tag.");
          }
        } else {
          out.println(
              "Test FAILED in classic tag. fragment is not of type JspFragment.");
        }
      } else {
        out.println("Test FAILED in classic tag. fragment attribute is null.");
      }
    } catch (IOException ioex) {
      throw new JspException("Unexpected IOException", ioex);
    }
    return SKIP_BODY;
  }
}
