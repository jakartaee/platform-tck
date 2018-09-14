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
 * literals.
 */
public class CheckLiteralTag extends BaseCheckTag {

  /**
   * Performs validation of expression language literals using the following
   * algorithm:
   * <ul>
   * <li>If the control object is null, and the expression yields a non null
   * object, FAIL</li>
   * <li>If the control object is an instance of <tt>java.lang.Number</tt>,
   * convert both the control and test objects to Strings. If they are not
   * equal, FAIL.</li>
   * <li>Otherwise, if both the control and test objects are not equal,
   * FAIL.</li>
   * </ul>
   * 
   * @throws JspException
   *           if an error occurs
   */
  protected void performCheck() throws JspException {
    String message = null;
    if (_control == null) {
      if (_object != null) {
        message = "Test FAILED.  Test of Null literal failed.  Expected"
            + " EL evaluation of Null literal to be null.  Received: "
            + _object;
      }
    } else {
      if (_control instanceof Number) {
        if (!_control.toString().equals(_object.toString())) {
          message = "Test FAILED.  Test of " + _name + " literal failed."
              + " Expected: " + _control + ", Received: " + _object;
        }
      } else if (!_control.equals(_object)) {
        message = "Test FAILED.  Test of " + _name + " literal failed."
            + " Expected: " + _control + ", Received: " + _object;
      }
    }
    displayTestStatus(message);
  }
}
