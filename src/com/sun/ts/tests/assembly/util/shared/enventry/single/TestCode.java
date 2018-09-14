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
 * @(#)TestCode.java	1.9 03/05/16
 */

package com.sun.ts.tests.assembly.util.shared.enventry.single;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;

public class TestCode {
  /** Prefix for JNDI lookups */
  protected static final String prefix = "java:comp/env/";

  /*
   * Names of the env. entries.
   */
  protected static final String charEntryName = prefix + "myChar";

  protected static final String stringEntryName = prefix + "myString";

  protected static final String booleanEntryName = prefix + "myBoolean";

  protected static final String byteEntryName = prefix + "myByte";

  protected static final String shortEntryName = prefix + "myShort";

  protected static final String integerEntryName = prefix + "myInteger";

  protected static final String longEntryName = prefix + "myLong";

  protected static final String floatEntryName = prefix + "myFloat";

  protected static final String doubleEntryName = prefix + "myDouble";

  /*
   * Reference values for the environment entries.
   */
  protected static final Character charValue = new Character('J');

  protected static final String stringValue = new String("In vino veritas");

  protected static final Boolean boolValue = new Boolean("true");

  protected static final Byte byteValue = new Byte("22");

  protected static final Short shortValue = new Short("1789");

  protected static final Integer intValue = new Integer("-1");

  protected static final Long longValue = new Long("55000000");

  protected static final Float floatValue = new Float("37.2");

  protected static final Double doubleValue = new Double("5.5");

  /**
   * Check that runtime value for Character env entry.
   */
  public static boolean testCharacterEntry(TSNamingContext nctx) {
    boolean pass;
    Character value = null;

    try {
      TestUtil.logTrace("Looking up env entry '" + charEntryName + "'");
      value = (Character) nctx.lookup(charEntryName);
      TestUtil.logTrace("Runtime value is '" + value + "'");

      pass = charValue.equals(value);
      if (!pass) {
        TestUtil.logErr("Value should be '" + charValue + "'");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e, e);
      pass = false;
    }

    return pass;
  }

  /**
   * Check that runtime value for String env entry.
   */
  public static boolean testStringEntry(TSNamingContext nctx) {
    boolean pass;
    String value = null;

    try {
      TestUtil.logTrace("Looking up env entry '" + stringEntryName + "'");
      value = (String) nctx.lookup(stringEntryName);
      TestUtil.logTrace("Runtime value is '" + value + "'");

      pass = stringValue.equals(value);
      if (!pass) {
        TestUtil.logErr("Value should be '" + stringValue + "'");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e, e);
      pass = false;
    }

    return pass;
  }

  /**
   * Check that runtime value for Boolean env entry.
   */
  public static boolean testBooleanEntry(TSNamingContext nctx) {
    boolean pass;
    Boolean value;

    try {
      TestUtil.logTrace("Looking up env entry '" + booleanEntryName + "'");
      value = (Boolean) nctx.lookup(booleanEntryName);
      TestUtil.logTrace("Runtime value is '" + value + "'");

      pass = boolValue.equals(value);
      if (!pass) {
        TestUtil.logErr("Value should be '" + boolValue + "'");
      }
    } catch (Exception e) {
      TestUtil.logErr("caught exception: " + e, e);
      pass = false;
    }

    return pass;
  }

  /**
   * Check that runtime value for Byte env entry.
   */
  public static boolean testByteEntry(TSNamingContext nctx) {
    boolean pass;
    Byte value;

    try {
      TestUtil.logTrace("Looking up env entry '" + byteEntryName + "'");
      value = (Byte) nctx.lookup(byteEntryName);
      TestUtil.logTrace("Runtime value is '" + value + "'");

      pass = byteValue.equals(value);
      if (!pass) {
        TestUtil.logErr("Value should be '" + byteValue + "'");
      }
    } catch (Exception e) {
      TestUtil.logErr("caught exception: " + e, e);
      pass = false;
    }

    return pass;
  }

  /**
   * Check that runtime value for Short env entry.
   */
  public static boolean testShortEntry(TSNamingContext nctx) {
    boolean pass;
    Short value;

    try {
      TestUtil.logTrace("Looking up env entry '" + shortEntryName + "'");
      value = (Short) nctx.lookup(shortEntryName);
      TestUtil.logTrace("Runtime value is '" + value + "'");

      pass = shortValue.equals(value);
      if (!pass) {
        TestUtil.logErr("Value should be '" + shortValue + "'");
      }
    } catch (Exception e) {
      TestUtil.logErr("caught exception: " + e, e);
      pass = false;
    }

    return pass;
  }

  /**
   * Check that runtime value for Integer env entry.
   */
  public static boolean testIntegerEntry(TSNamingContext nctx) {
    boolean pass;
    Integer value;

    try {
      TestUtil.logTrace("Looking up env entry '" + integerEntryName + "'");
      value = (Integer) nctx.lookup(integerEntryName);
      TestUtil.logTrace("Runtime value is '" + value + "'");

      pass = intValue.equals(value);
      if (!pass) {
        TestUtil.logErr("Value should be '" + intValue + "'");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e, e);
      pass = false;
    }

    return pass;
  }

  /**
   * Check that runtime value for Long env entry.
   */
  public static boolean testLongEntry(TSNamingContext nctx) {
    boolean pass;
    Long value;

    try {
      TestUtil.logTrace("Looking up env entry '" + longEntryName + "'");
      value = (Long) nctx.lookup(longEntryName);
      TestUtil.logTrace("Runtime value is '" + value + "'");

      pass = longValue.equals(value);
      if (!pass) {
        TestUtil.logErr("Value should be '" + longValue + "'");
      }
    } catch (Exception e) {
      TestUtil.logErr("caught exception: " + e, e);
      pass = false;
    }

    return pass;
  }

  /**
   * Check that runtime value for Float env entry.
   */
  public static boolean testFloatEntry(TSNamingContext nctx) {
    boolean pass;
    Float value;

    try {
      TestUtil.logTrace("Looking up env entry '" + floatEntryName + "'");
      value = (Float) nctx.lookup(floatEntryName);
      TestUtil.logTrace("Runtime value is '" + value + "'");

      pass = floatValue.equals(value);
      if (!pass) {
        TestUtil.logErr("Value should be '" + floatValue + "'");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e, e);
      pass = false;
    }

    return pass;
  }

  /**
   * Check that runtime value for Double env entry.
   */
  public static boolean testDoubleEntry(TSNamingContext nctx) {
    boolean pass;
    Double value;

    try {
      TestUtil.logTrace("Looking up env entry '" + doubleEntryName + "'");
      value = (Double) nctx.lookup(doubleEntryName);
      TestUtil.logTrace("Runtime value is '" + value + "'");

      pass = doubleValue.equals(value);
      if (!pass) {
        TestUtil.logErr("Value should be '" + doubleValue + "'");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e, e);
      pass = false;
    }

    return pass;
  }

}
