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
 * Tag implementation to validate the various operators provided by the JSP 2.0
 * expression language.
 */
public class CheckOperatorTag extends BaseCheckTag {

  /**
   * Validate the control and test objects using the following algorithm:
   * <ul>
   * <li>If either the control or test objects are null, FAIL.</li>
   * <li>If both objects, converted to a String, are not equal, FAIL.</li>
   * </ul>
   * 
   * @throws JspException
   *           if an error occurs
   */
  protected void performCheck() throws JspException {
    String message = null;

    if (_control == null || _object == null) {
      message = "Test FAILED (" + _name + ").  Either the control or"
          + " the object under test was null.";
    } else {
      if (!_control.toString().equals(_object.toString())) {
        message = "Test FAILED (" + _name + ").  " + " Expected: " + _control
            + ", Received: " + _object;
      }
    }
    displayTestStatus(message);
  }
}
