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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.setproperty;

import java.io.Serializable;

/**
 * PropertyBean.java Simple Java Bean to verify that a JSP Container will make
 * use of a Bean's PropertyEditory class if one is available.
 */

public class PropertyBean implements Serializable {

  private String _pString = null;

  private Boolean _pBoolean = null;

  private Integer _pInt = null;

  public PropertyBean() {
    _pString = "StringValue";
    _pBoolean = Boolean.FALSE;
    _pInt = new Integer("10");
  }

  /**
   * Get the value of pString.
   * 
   * @return value of pString.
   */
  public String getPString() {
    return _pString;
  }

  /**
   * Set the value of pString.
   * 
   * @param v
   *          Value to assign to pString.
   */
  public void setPString(String v) {
    this._pString = v;
  }

  /**
   * Get the value of pBoolean.
   * 
   * @return value of pBoolean.
   */
  public Boolean getPBoolean() {
    return _pBoolean;
  }

  /**
   * Set the value of pBoolean.
   * 
   * @param v
   *          Value to assign to pBoolean.
   */
  public void setPBoolean(Boolean v) {
    this._pBoolean = v;
  }

  /**
   * Get the value of pInt.
   * 
   * @return value of pInt.
   */
  public Integer getPInteger() {
    return _pInt;
  }

  /**
   * Set the value of pInt.
   * 
   * @param v
   *          Value to assign to pInt.
   */
  public void setPInteger(Integer v) {
    this._pInt = v;
  }

}// PropertyBean
