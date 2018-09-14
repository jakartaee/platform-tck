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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.dynamicattributes;

import javax.servlet.jsp.tagext.DynamicAttributes;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.JspException;
import java.io.IOException;

public class SimpleExcTag extends SimpleTagSupport
    implements DynamicAttributes {

  /**
   * Default constructor.
   */
  public SimpleExcTag() {
    super();
  }

  /**
   * If called, it will fail the test.
   * 
   * @throws JspException
   *           not thrown
   * @throws IOException
   *           not thrown
   */
  public void doTag() throws JspException, IOException {
    this.getJspContext().getOut().println("Test FAILED.  Container incorrectly"
        + " called doTag() after setDynamicAttribute() threw a JspException.");
  }

  /**
   * Throws a JspException.
   * 
   * @param uri
   *          - the namespace of the attribute (if any)
   * @param localName
   *          - the name of the attribute
   * @param value
   *          - the value of the attribute
   * @throws JspException
   *           if an error occurs
   */
  public void setDynamicAttribute(String uri, String localName, Object value)
      throws JspException {
    throw new JspException("JspException");
  }
}
