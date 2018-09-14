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
import javax.servlet.jsp.JspWriter;
import java.io.IOException;
import java.io.BufferedReader;

public class BodyContentReadWriteTag extends BodyTagSupport {

  /**
   * Default constructor.
   */
  public BodyContentReadWriteTag() {
    super();
  }

  /**
   * Validates the behavior of BodyContent.getReader() and
   * BodyContent.getEnclosingWriter();
   * 
   * @return SKIP_BODY
   * @throws JspException
   *           - if an error occurs
   */
  public int doAfterBody() throws JspException {
    JspTestUtil.debug("[BodyContentReadWriteTag] in doAfterBody()");

    try {
      BodyContent content = this.getBodyContent();
      BufferedReader reader = new BufferedReader(content.getReader());
      StringBuffer sb = new StringBuffer();
      JspWriter writer = content.getEnclosingWriter();

      try {
        // add a little something to the body to show that it has
        // been modified...
        sb.append("#");

        for (int i = reader.read(); i != -1; i = reader.read()) {
          if (Character.isWhitespace((char) i)) {
            continue;
          }
          sb.append((char) i);
        }
        sb.append("#");

        writer = content.getEnclosingWriter();

        if (writer != null) {
          // validate that the enclosing writer is not the same
          // JspWriter used at the page level.
          if (writer == pageContext.getOut()) {
            writer.println("Test FAILED.  Writer returned"
                + " by BodyContent has the same address as the writer returned"
                + " by pageContext.get().");
          } else {
            writer.println(sb.toString());
          }
        }
      } finally {
        writer.close();
        reader.close();
      }

    } catch (IOException ioe) {
      throw new JspException("Unexpected IOException!", ioe);
    }

    return SKIP_BODY;
  }
}
