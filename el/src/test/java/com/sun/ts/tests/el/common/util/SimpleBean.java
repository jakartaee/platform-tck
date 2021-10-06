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

package com.sun.ts.tests.el.common.util;

import java.io.Serializable;

public class SimpleBean implements Serializable {

  private String fullName = "Doug Donahue"; // Default Setting

  private String intention;

  public void setIntention(String s) {
    intention = s;
  }

  public String getIntention() {
    return intention;
  }

  /**
   * Set full name using the pattern firstName + " " + lastName.
   * 
   * @param firstName
   * @param lastName
   */
  public void setFullName(String firstName, String lastName) {
    this.fullName = firstName + " " + lastName;
  }

  /**
   *
   * @param firstName
   *          - first name @String
   * @param lastName
   *          - last Name
   * @return true is full name matches the given firstName + " " + lastName
   *         pattern.
   */
  public boolean isName(String firstName, String lastName) {
    boolean result = Boolean.FALSE;
    String testName = firstName + " " + lastName;

    if (testName.equals(fullName)) {
      result = true;
    }

    return result;
  }
}
