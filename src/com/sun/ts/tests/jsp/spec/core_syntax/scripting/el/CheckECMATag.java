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

package com.sun.ts.tests.jsp.spec.core_syntax.scripting.el;

import javax.servlet.jsp.JspException;

/**
 * Tag implementation to perform validation of JSP 2.0 expression language
 * ECMA-like operators <tt>'.'</tt> and <tt>'[]'</tt>.
 */
public class CheckECMATag extends BaseCheckTag {

  /**
   * Validates ECMA treatment of the <tt>'.'</tt> and <tt>'[]'</tt> operators.
   * Validation is performed as follows:
   * <ul>
   * <li>If the control object is null, the object under test must be null as
   * well or FAIL.</li>
   * <li>If the control and test objects are not equal, fail</li>
   * </ul>
   * 
   * @throws JspException
   *           if an error occurs
   */
  protected void performCheck() throws JspException {
    String message = null;

    if (_control == null) {
      if (_object != null) {
        message = "Test FAILED.  Expected evaluation of expression to be"
            + " null when " + _name + " was null";
      }
    } else {
      if (!_control.equals(_object)) {
        message = "Test FAILED.  Expected result of evaluation to be: "
            + _control + ", received: " + _object;
      }
    }
    displayTestStatus(message);
  }

}
