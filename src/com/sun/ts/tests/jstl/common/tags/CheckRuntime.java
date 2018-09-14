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

/**
 * Simple tag to check the J2SE runtime.
 */

public class CheckRuntime extends TagSupport {

  /**
   * Scoped variable in which to export the result of the runtime check.
   */
  private String _var = null;

  /** Creates new CheckRuntime */
  public CheckRuntime() {
    super();
  }

  /*
   * public methods
   * ========================================================================
   */

  /**
   * Sets the name of the scoped attribute in which to export the result of the
   * runtime check
   */
  public void setVar(String var) {
    _var = var;
  }

  /**
   * Actions may behave differently based on the available runtime version. This
   * action will check to see if we're a 1.4 runtime or an earlier version.
   */
  public int doEndTag() throws JspException {
    boolean is14 = false;
    try {
      Class.forName("java.util.Currency");
      is14 = true;
    } catch (Exception e) {
      ;
    }
    pageContext.setAttribute(_var, Boolean.valueOf(is14));
    return EVAL_PAGE;
  }

  /**
   * Releases tag state.
   */
  public void release() {
    _var = null;
  }
}
