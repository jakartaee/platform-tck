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

/**
 * Simple single 'el functioned' class for EL testing.
 */
public class ElFunctions {

  /**
   * EL Function that provides substring functionality
   * 
   * @param startIndex
   *          - the start index for the substring operation
   * @param endIndex
   *          - the end index for the substring operation
   * @param value
   *          - the String value to perform the operation against
   * @return the substringed value
   */
  public static final String substring(int startIndex, int endIndex,
      String value) {
    return value.substring(startIndex, endIndex);
  }

  public String toString() {
    return super.toString();
  }
}
