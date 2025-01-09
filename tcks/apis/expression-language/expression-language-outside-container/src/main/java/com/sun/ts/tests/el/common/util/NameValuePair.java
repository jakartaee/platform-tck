/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.el.common.util;

// This class holds the name of a variable in an Expression
// and the value to which the variable is to be set.
public class NameValuePair {

  private final String name;

  private final Object value;

  public NameValuePair(String name, Object value) {
    this.name = name;
    this.value = value;
  }

  public String getName() {
    return name;
  }

  public Object getValue() {
    return value;
  }

  /*
   * Builds a NameValuePair[] with values assigned to Tag Names A and B.
   */
  public static NameValuePair[] buildNameValuePair(Object a, Object b) {

    NameValuePair value1 = new NameValuePair("A", a);
    NameValuePair value2 = new NameValuePair("B", b);

    NameValuePair values[] = { value1, value2 };

    return values;
  }

  /*
   * Builds a NameValuePair[] with a value assigned to Tag Name A.
   */
  public static NameValuePair[] buildUnaryNameValue(Object a) {

    NameValuePair value1 = new NameValuePair("A", a);

    NameValuePair value[] = { value1 };

    return value;
  }

  /*
   * Builds a NameValuePair[] with values assigned to Tag Names A, B, and C.
   */
  public static NameValuePair[] buildConditionalNameValue(Object a, Object b,
      Object c) {

    NameValuePair value1 = new NameValuePair("A", a);
    NameValuePair value2 = new NameValuePair("B", b);
    NameValuePair value3 = new NameValuePair("C", c);

    NameValuePair values[] = { value1, value2, value3 };

    return values;
  }

}
