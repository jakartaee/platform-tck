/*
 * Copyright (c) 2003, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.lib.util;

/**
 * This class contains additional constants documenting limits of the
 * <code>double</code> type.
 *
 * @author Joseph D. Darcy
 */

public class DoubleConsts {
  /**
   * Don't let anyone instantiate this class.
   */
  private DoubleConsts() {
  }

  public static final double POSITIVE_INFINITY = java.lang.Double.POSITIVE_INFINITY;

  public static final double NEGATIVE_INFINITY = java.lang.Double.NEGATIVE_INFINITY;

  public static final double NaN = java.lang.Double.NaN;

  public static final double MAX_VALUE = java.lang.Double.MAX_VALUE;

  public static final double MIN_VALUE = java.lang.Double.MIN_VALUE;

  /**
   * A constant holding the smallest positive normal value of type
   * <code>double</code>, 2<sup>-1022</sup>. It is equal to the value returned
   * by <code>Double.longBitsToDouble(0x0010000000000000L)</code>.
   *
   * @since 1.5
   */
  public static final double MIN_NORMAL = 2.2250738585072014E-308;

  /**
   * The number of logical bits in the significand of a <code>double</code>
   * number, including the implicit bit.
   */
  public static final int SIGNIFICAND_WIDTH = 53;

  /**
   * Maximum exponent a finite <code>double</code> number may have. It is equal
   * to the value returned by <code>Math.ilogb(Double.MAX_VALUE)</code>.
   */
  public static final int MAX_EXPONENT = 1023;

  /**
   * Minimum exponent a normalized <code>double</code> number may have. It is
   * equal to the value returned by <code>Math.ilogb(Double.MIN_NORMAL)</code>.
   */
  public static final int MIN_EXPONENT = -1022;

  /**
   * The exponent the smallest positive <code>double</code> subnormal value
   * would have if it could be normalized. It is equal to the value returned by
   * <code>FpUtils.ilogb(Double.MIN_VALUE)</code>.
   */
  public static final int MIN_SUB_EXPONENT = MIN_EXPONENT
      - (SIGNIFICAND_WIDTH - 1);

  /**
   * Bias used in representing a <code>double</code> exponent.
   */
  public static final int EXP_BIAS = 1023;

  /**
   * Bit mask to isolate the sign bit of a <code>double</code>.
   */
  public static final long SIGN_BIT_MASK = 0x8000000000000000L;

  /**
   * Bit mask to isolate the exponent field of a <code>double</code>.
   */
  public static final long EXP_BIT_MASK = 0x7FF0000000000000L;

  /**
   * Bit mask to isolate the significand field of a <code>double</code>.
   */
  public static final long SIGNIF_BIT_MASK = 0x000FFFFFFFFFFFFFL;

  static {
    // verify bit masks cover all bit positions and that the bit
    // masks are non-overlapping
    assert (((SIGN_BIT_MASK | EXP_BIT_MASK | SIGNIF_BIT_MASK) == ~0L)
        && (((SIGN_BIT_MASK & EXP_BIT_MASK) == 0L)
            && ((SIGN_BIT_MASK & SIGNIF_BIT_MASK) == 0L)
            && ((EXP_BIT_MASK & SIGNIF_BIT_MASK) == 0L)));
  }
}
