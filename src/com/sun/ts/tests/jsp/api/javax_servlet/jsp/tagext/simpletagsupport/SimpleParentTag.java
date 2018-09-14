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
 * @(#)SimpleParentTag.java 1.2 11/07/02
 */

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.simpletagsupport;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.JspException;
import java.io.IOException;

/**
 * SimpleTag to do nothing more than invoke its body.
 */
public class SimpleParentTag extends SimpleTagSupport {

  /**
   * Nesting level
   */
  private String _level = null;

  /**
   * Default constructor
   */
  public SimpleParentTag() {
    super();
  }

  /**
   * Returns the level set for this tag.
   * 
   * @return the nesting level defined by the tag
   */
  public String getLevel() {
    return _level;
  }

  /**
   * Sets the level for this tag.
   * 
   * @param level
   *          - the nesting level of the tag
   */
  public void setLevel(String level) {
    _level = level;
  }

  /**
   * Does nothing more than invokes the body.
   *
   * @throws javax.servlet.jsp.JspException
   *           Subclasses can throw JspException to indicate an error occurred
   *           while processing this tag.
   * @throws javax.servlet.jsp.SkipPageException
   *           If the page that (either directly or indirectly) invoked this tag
   *           is to cease evaluation. A Simple Tag Handler generated from a tag
   *           file must throw this exception if an invoked Classic Tag Handler
   *           returned SKIP_PAGE or if an invoked Simple Tag Handler threw
   *           SkipPageException or if an invoked Jsp Fragment threw a
   *           SkipPageException.
   * @throws java.io.IOException
   *           Subclasses can throw IOException if there was an error writing to
   *           the output stream
   */
  public void doTag() throws JspException, IOException {
    this.getJspBody().invoke(this.getJspContext().getOut());
  }
}
