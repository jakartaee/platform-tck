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
 * @(#)FailingTag.java 1.1 10/31/02
 */

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.simpletagsupport;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import java.io.IOException;

public class FailingTag extends TagSupport {

  /**
   * Default constructor.
   */
  public FailingTag() {
    super();
  }

  /**
   * This method shouldn't be invoked.
   * 
   * @return Tag.SKIP_BODY
   * @throws JspException
   *           - if an unexpected error occurs
   */
  public int doEndTag() throws JspException {
    JspTestUtil.debug("[FailingTag] in doEndTag()");
    try {
      pageContext.getOut().println("Test FAILED.  Default behavior of "
          + "SimpleTagSupport.doTag() is to do nothing.  This handler should"
          + " not have been invoked.");
    } catch (IOException ioe) {
      throw new JspException("Test FAILED. Unexpected IOException thrown.",
          ioe);
    }
    return SKIP_BODY;
  }
}
