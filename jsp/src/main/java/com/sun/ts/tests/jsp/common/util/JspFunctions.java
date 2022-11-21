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

package com.sun.ts.tests.jsp.common.util;

/**
 * Defines functions to be used in EL expressions and various other tests.
 */

public class JspFunctions {

  /**
   * Private construtor as all methods are static.
   */
  private JspFunctions() {
  }

  /**
   * Returns the provided string in all lower case characters.
   * 
   * @param value
   *          - string to lower case
   * @return the provided value as lower case characters
   */
  public static String lowerCase(String value) {
    return value.toLowerCase();
  }

  /**
   * Returns the provided string in all upper case characters
   * 
   * @param value
   *          - string to upper case
   * @return the provided value as upper case characters
   */
  public static String upperCase(String value) {
    return value.toUpperCase();
  }
}
