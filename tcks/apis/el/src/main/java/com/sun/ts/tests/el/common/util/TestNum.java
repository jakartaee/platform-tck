/*
 * Copyright (c) 2006, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Used to store lists that will be utilized accross the board for a common
 * point of reference when testing.
 */
public final class TestNum {

  private static final String COMPARATOR = "1";

  private static ArrayList<Float> floatList;

  private static ArrayList<Object> numberList;

  /**
   * Private as this class will only have static methods and members.
   */
  private TestNum() {
  }

  /**
   * Used for a common list of Float values when testing.
   * 
   * @return - A set list of common Floats that we use as test values.
   */
  public static ArrayList getFloatList() {

    floatList = new ArrayList<Float>();

    floatList.add(Float.valueOf("1.00005f"));
    floatList.add(Float.valueOf("1.5E-4d"));
    floatList.add(Float.valueOf("1.5E+4"));
    floatList.add(Float.valueOf("1.5e+4"));

    return floatList;

  }

  /**
   * Used a common reference point for Number types and a common value is
   * assigned (1).
   *
   * @return - A common List of Number types with a constant value.
   */
  public static ArrayList getNumberList() {

    numberList = new ArrayList<Object>();

    numberList.add(BigDecimal.valueOf(Long.valueOf(COMPARATOR)));
    numberList.add(Double.valueOf(COMPARATOR));
    numberList.add(Float.valueOf(COMPARATOR));
    numberList.add(COMPARATOR + ".0");
    numberList.add(COMPARATOR + "e0");
    numberList.add(COMPARATOR + "E0");
    numberList.add(BigInteger.valueOf(Long.valueOf(COMPARATOR)));
    numberList.add(Long.valueOf(COMPARATOR));
    numberList.add(Integer.valueOf(COMPARATOR));
    numberList.add(Short.valueOf(COMPARATOR));
    numberList.add(Byte.valueOf(COMPARATOR));

    return numberList;

  }
}
