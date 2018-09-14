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
 * @(#)ClassicParent.java 1.1 10/31/02
 */

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.simpletagsupport;

import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import javax.servlet.jsp.tagext.TagSupport;

/**
 * A simple tag to help verify nested tag behavior.
 */
public class ClassicParent extends TagSupport {

  /**
   * The nested level.
   */
  private String _level = null;

  /**
   * Default constructor.
   */
  public ClassicParent() {
    super();
  }

  /**
   * Sets the nesting level.
   * 
   * @param level
   *          - the nesting level
   */
  public void setLevel(String level) {
    _level = level;
  }

  /**
   * Gets the nesting level of this tag.
   * 
   * @return the defined nesting level
   */
  public String getLevel() {
    return _level;
  }

  /**
   * Returns EVAL_BODY_INCLUDE.
   * 
   * @return Tag.EVAL_BODY_INCLUDE
   */
  public int doStartTag() {
    JspTestUtil.debug("[ClassicParent] in doStartTag()");
    return EVAL_BODY_INCLUDE;
  }
}
