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
import java.math.BigInteger;
import java.math.BigDecimal;

/**
 * Tag implementation to validate type coercions for the JSP 2.0 expression
 * language.
 */
public class CheckCoercionTag extends BaseCheckTag {

  /**
   * Sets a Byte object.
   * 
   * @param _byte
   *          a Byte object
   */
  public void setByte(Byte _byte) {
    _object = _byte;
  }

  /**
   * Sets a Character object.
   * 
   * @param _char
   *          a Character object
   */
  public void setChar(Character _char) {
    _object = _char;
  }

  /**
   * Sets a Short object.
   * 
   * @param _short
   *          a Short object
   */
  public void setShort(Short _short) {
    _object = _short;
  }

  /**
   * Sets an Integer object.
   * 
   * @param _int
   *          an Integer object
   */
  public void setInt(Integer _int) {
    _object = _int;
  }

  /**
   * Sets a Long object.
   * 
   * @param _long
   *          a Long object
   */
  public void setLong(Long _long) {
    _object = _long;
  }

  /**
   * Sets a Float object.
   * 
   * @param _float
   *          a Float object
   */
  public void setFloat(Float _float) {
    _object = _float;
  }

  /**
   * Sets a Double object.
   * 
   * @param _double
   *          a Double object
   */
  public void setDouble(Double _double) {
    _object = _double;
  }

  /**
   * Sets a String object.
   * 
   * @param _string
   *          a String object
   */
  public void setString(String _string) {
    _object = _string;
  }

  /**
   * Sets a Boolean object.
   * 
   * @param _boolean
   *          a Boolean object
   */
  public void setBoolean(Boolean _boolean) {
    _object = _boolean;
  }

  public void setBigInteger(BigInteger _bigInteger) {
    _object = _bigInteger;
  }

  public void setBigDecimal(BigDecimal _bigDecimal) {
    _object = _bigDecimal;
  }

  public void setTypeBean(TypeBean _bean) {
    _object = _bean;
  }

  /**
   * Validates the to and from various types by taking the String value of the
   * expected result and the resulting evaluation and checking for equality.
   * 
   * @throws JspException
   *           if an error occurs.
   */
  protected void performCheck() throws JspException {
    String message = null;
    if (_control == null) {
      if (_object != null) {
        message = "Test FAILED(" + _name + ") Expected the EL expression"
            + "to evaluate to null.  Received: " + _object;
      }
    } else if (!_control.toString().equals(_object.toString())) {
      message = "Test Failed(" + _name + ") Expected: " + _control
          + ", received: " + _object;
    }
    displayTestStatus(message);
  }
}
