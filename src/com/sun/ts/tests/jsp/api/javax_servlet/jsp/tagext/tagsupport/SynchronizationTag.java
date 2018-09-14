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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagsupport;

import javax.servlet.jsp.JspException;

public class SynchronizationTag extends ContainerInteractionTag {

  /**
   * Default constructor.
   */
  public SynchronizationTag() {
    super();
  }

  /**
   * Adds attributes for begin and nested scripting variables and returns what
   * ever value is configured by the tag.
   * 
   * @return an int value based on what the tag is configured to return
   * @throws JspException
   *           if an error occurs
   */
  public int doStartTag() throws JspException {
    pageContext.setAttribute("begin", Integer.valueOf(1));
    pageContext.setAttribute("nested", Integer.valueOf(1));
    return super.doStartTag();
  }

  /**
   * Adds attributes for begin and end scripting variables as well as removing
   * nested from the PageContext.
   * 
   * @return an int value based on what the tag is configured to return
   * @throws JspException
   */
  public int doEndTag() throws JspException {
    pageContext.setAttribute("begin",
        Integer.valueOf(getIntValue("begin") + 1));
    pageContext.setAttribute("end", Integer.valueOf(getIntValue("nested") + 1));
    pageContext.removeAttribute("nested");
    return super.doEndTag();
  }

  /**
   * Adds attributes for begin and nested scripting variables.
   * 
   * @return an int value based on what the tag is configured to return
   * @throws JspException
   */
  public int doAfterBody() throws JspException {
    pageContext.setAttribute("begin",
        Integer.valueOf(getIntValue("begin") + 1));
    pageContext.setAttribute("nested",
        Integer.valueOf(getIntValue("nested") + 1));
    return super.doAfterBody();
  }

}
