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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.bodycontent;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.JspException;
import java.io.IOException;

public class BodyContentFlushTag extends BodyTagSupport {

  /**
   * Default constructor.
   */
  public BodyContentFlushTag() {
    super();
  }

  /**
   * Validates that an IOException is thrown if BodyContent.flush() is called.
   * 
   * @return SKIP_BODY
   * @throws JspException
   *           - if an error occurs
   */
  public int doAfterBody() throws JspException {
    JspTestUtil.debug("[BodyContentFlushTag] in doAfterBody()");
    BodyContent content = this.getBodyContent();

    try {
      content.flush();
    } catch (IOException ioe) {
      try {
        content.getEnclosingWriter().println("Test PASSED");
      } catch (IOException eio) {
        throw new JspException("Test FAILED. Unexpected IOException!", eio);
      }
    } catch (Exception e) {
      throw new JspException("Test FAILED. Unexpected Exception!", e);
    }

    return SKIP_BODY;
  }
}
