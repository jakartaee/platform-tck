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

package com.sun.ts.tests.jstl.common.beans;

/**
 * Simple single-valued bean for testing purposes.
 */
public class SimpleBean {

  /** Creates new NullBean */
  public SimpleBean() {
  }

  private String _value = null;

  /*
   * public methods
   * ========================================================================
   */

  /**
   * Sets a non-descriptive value
   * 
   * @param value
   *          some value
   */
  public void setValue(String value) {
    _value = value;
  }

  /**
   * Returns the current value of the bean
   * 
   * @return the current value
   */
  public String getValue() {
    return _value;
  }

  /**
   * Causes a RuntimeException to be thrown when called.
   */
  public String toString() {
    throw new RuntimeException();
  }
}
