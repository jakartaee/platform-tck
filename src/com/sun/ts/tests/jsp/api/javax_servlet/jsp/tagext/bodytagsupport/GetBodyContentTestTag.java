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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.bodytagsupport;

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.BodyContent;
import javax.servlet.jsp.JspException;
import java.io.IOException;

public class GetBodyContentTestTag extends BodyTagSupport {

  /**
   * Default constructor.
   */
  public GetBodyContentTestTag() {
    super();
  }

  /**
   * Validate the that get/setBodyContent() work as expected.
   * 
   * @return Tag.EVAL_PAGE
   * @throws JspException
   *           if an unexpected error occurs
   */
  public int doEndTag() throws JspException {
    BodyContent bc = this.getBodyContent();
    try {
      if (bc != null) {
        if ("body content".equals(bc.getString().trim())) {
          pageContext.getOut().println("Test PASSED");
        } else {
          pageContext.getOut()
              .println("Test FAILED.  Unexpected body"
                  + " content returned.  Expected: 'body content', received: "
                  + bc.getString());
        }
      } else {
        pageContext.getOut().println(
            "Test FAILED.  BodyTagSupport.getBodyContent()" + "returned null.");
      }
    } catch (IOException ioe) {
      throw new JspException("Unexpected IOException!", ioe);
    }
    return EVAL_PAGE;
  }
}
