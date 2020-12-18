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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.bodycontent;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.tagext.BodyContent;
import jakarta.servlet.jsp.tagext.BodyTagSupport;

public class BodyContentClearBodyTag extends BodyTagSupport {

  /**
   * Default constructor.
   */
  public BodyContentClearBodyTag() {
    super();
  }

  /**
   * Validates the behavior of clearBody().
   * 
   * @return SKIP_BODY
   * @throws JspException
   *           - if an error occurs
   */
  public int doAfterBody() throws JspException {
    JspTestUtil.debug("[BodyContentClearBodyTag] in doAfterBody()");
    BodyContent content = this.getBodyContent();

    try {
      content.clearBody();
      // write the body content -- should be empty.
      content.writeOut(content.getEnclosingWriter());

    } catch (Exception e) {
      throw new JspException("Test FAILED. Unexpected Exception!", e);
    }

    return SKIP_BODY;
  }
}
