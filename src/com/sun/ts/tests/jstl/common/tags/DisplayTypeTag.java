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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jstl.common.tags;

import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.JspException;
import java.io.IOException;

public class DisplayTypeTag extends TagSupport {

  /**
   * Scoped variable name
   */
  private String _varName = null;

  /** Creates new DisplayTypeTag */
  public DisplayTypeTag() {
    super();
  }

  /*
   * public methods
   * ========================================================================
   */

  /**
   * Sets the name of the scoped variable that type information should be
   * displayed.
   *
   * @param varName
   *          scoped variable name
   */
  public void setVarName(String varName) {
    _varName = varName;
  }

  /**
   * When called, this will get the attribute from the pageContext, get the name
   * of the implementing class of the object, and write the result to the
   * current JspWriter.
   *
   * @return EVAL_PAGE
   */
  public int doEndTag() throws JspException {
    Object o = pageContext.findAttribute(_varName);
    String type = null;
    if (o != null) {
      type = o.getClass().getName();
    } else {
      type = "<strong>Error:</strong> Attribute, " + _varName
          + ", not found in any scope<br>";
    }
    try {
      pageContext.getOut().print("<strong>" + _varName + "</strong> is "
          + "of type:<strong>" + type + "</strong>");
    } catch (IOException ioe) {
      throw new JspException(ioe.toString());
    }
    return EVAL_PAGE;
  }

  public void release() {
    _varName = null;
  }
}
