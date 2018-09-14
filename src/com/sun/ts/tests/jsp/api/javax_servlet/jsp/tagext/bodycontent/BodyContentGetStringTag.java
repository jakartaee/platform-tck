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

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.JspException;
import java.io.IOException;

public class BodyContentGetStringTag extends BodyTagSupport {

  /**
   * Default constructor.
   */
  public BodyContentGetStringTag() {
    super();
  }

  /**
   * Validates BodyContent.getString()
   * 
   * @return SKIP_BODY
   * @throws JspException
   *           if an error occurs
   */
  public int doAfterBody() throws JspException {
    BodyContent content = this.getBodyContent();

    try {
      String body = content.getString().trim();

      if (body.equals("body content")) {
        content.getEnclosingWriter().println("Test PASSED");

      } else {
        content.getEnclosingWriter()
            .println("Test FAILED.  Expected "
                + "BodyContent.getString() to return 'body content'.  "
                + "Received: " + body);
      }

    } catch (Exception e) {
      throw new JspException("Test FAILED. Unexpected Exception", e);
    }

    return SKIP_BODY;
  }
}
