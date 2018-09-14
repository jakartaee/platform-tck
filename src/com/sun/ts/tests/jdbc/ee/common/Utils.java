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
 * %W% %E%
 */

/*
 * @(#)Utils.java	1.15 02/04/22
 */
package com.sun.ts.tests.jdbc.ee.common;

import java.io.*;
import java.util.*;
import java.sql.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.FpUtils;

import java.rmi.RemoteException;

/**
 * The Utils class is used to hold common methods that can be used by various
 * JDBC tests.
 *
 * @author
 * @version 1
 */
public class Utils extends ServiceEETest {
  private Properties props = null;

  /**
   * This method is used to compare two floating point values for equality where
   * equality does not mean EXACT equality, but instead means equal within a
   * range where the range is specified by the passed in expectedUlp variable.
   * 
   * @param expected
   *          - the 'golden' value we want to match
   * @param obtained
   *          - the value we are testing to see if it matches our expected value
   * @return boolean True if expected and obtained are considered equal within a
   *         range of some ulp value (expectedUlp).
   */
  public static boolean isMatchingFloatingPointVal(Float expected,
      Double obtained) {
    return isMatchingFloatingPointVal(new Double(expected.doubleValue()),
        obtained, Math.ulp(expected.floatValue()));
  }

  /**
   * This method is used to compare two floating point values for equality where
   * equality does not mean EXACT equality, but instead means equal within a
   * range where the range is specified by the passed in expectedUlp variable.
   * 
   * @param expected
   *          - the 'golden' value we want to match
   * @param obtained
   *          - the value we are testing to see if it matches our expected value
   * @return boolean True if expected and obtained are considered equal within a
   *         range of some ulp value (expectedUlp).
   */
  public static boolean isMatchingFloatingPointVal(Double expected,
      Float obtained) {
    return isMatchingFloatingPointVal(expected,
        new Double(obtained.doubleValue()), Math.ulp(expected.floatValue()));
  }

  /**
   * This method is used to compare two floating point values for equality where
   * equality does not mean EXACT equality, but instead means equal within a
   * range where the range is specified by the passed in expectedUlp variable.
   * 
   * @param expected
   *          - the 'golden' value we want to match
   * @param obtained
   *          - the value we are testing to see if it matches our expected value
   * @return boolean True if expected and obtained are considered equal within a
   *         range of some ulp value (expectedUlp).
   */
  public static boolean isMatchingFloatingPointVal(Float expected,
      Float obtained) {

    return isMatchingFloatingPointVal(new Double(expected.doubleValue()),
        new Double(obtained.doubleValue()), Math.ulp(expected.floatValue()));
  }

  /**
   * This method is used to compare two floating point values for equality where
   * equality does not mean EXACT equality, but instead means equal within a
   * range where the range is specified by the passed in expectedUlp variable.
   * 
   * @param expected
   *          - the 'golden' value we want to match
   * @param obtained
   *          - the value we are testing to see if it matches our expected value
   * @return boolean True if expected and obtained are considered equal within a
   *         range of some ulp value (expectedUlp).
   */
  public static boolean isMatchingFloatingPointVal(Double expected,
      Double obtained) {

    return isMatchingFloatingPointVal(new Double(expected.doubleValue()),
        new Double(obtained.doubleValue()), Math.ulp(expected.doubleValue()));
  }

  /**
   * This method is used to compare two floating point values for equality where
   * equality does not mean EXACT equality, but instead means equal within a
   * range where the range is specified by the passed in expectedUlp variable.
   * 
   * @param expected
   *          - the 'golden' value we want to match
   * @param obtained
   *          - the value we are testing to see if it matches our expected value
   * @param expectedUlp
   *          - the 'ulp' (or margin of round off error) that we are allowed to
   *          use in our comparisons such that if expected and obtained are
   *          equal OR fall within a range of (+) or (-) expectedUlp of each
   *          other then we are considered equal.
   * @return boolean True if expected and obtained are considered equal within a
   *         range of some ulp value (expectedUlp).
   */
  public static boolean isMatchingFloatingPointVal(Double expected,
      Double obtained, double expectedUlp) {
    boolean rval = false;

    String tracestr = "testing if (golden) expected value=" + expected;
    tracestr += " matches obtained value=" + obtained;
    TestUtil.logTrace(tracestr);

    double ulpToUse = expectedUlp;
    if (expected.doubleValue() != obtained.doubleValue()) {
      // it is possible for a real number to be calculated out perfectly
      // and when that happens, we want to use an ulp of the other floating
      // point value, so use the ulp from the obtained value
      if (expected.doubleValue() == expectedUlp) {
        ulpToUse = Math.ulp(obtained.doubleValue());
      }
    }

    if (Double.compare(expected, obtained) == 0) {
      TestUtil.logTrace(
          "Expected and obtained values matched using Double.compare()");
      rval = true;
    } else if (0 == testUlpCore(obtained.doubleValue(), expected.doubleValue(),
        ulpToUse)) {
      TestUtil.logTrace(
          "values matched based on level of ulp/roundoff being within expected range.");
      rval = true;
    } else {
      TestUtil.logTrace(
          "Doubles do not matched using Double.compare() or calculated roundoff error.");
    }

    return rval;
  }

  /**
   * This method is taken from Joe Darcys floating point work in:
   * /java/re/jdk/7/latest/ws/j2se/test/closed/java/lang/Math/Tests.java This
   * method is doing the actual comparisons using an ulp value.
   * 
   * @param expected
   *          - the 'golden' value we want to match
   * @param result
   *          - the value we are testing to see if it matches our expected value
   * @param ulps
   *          - the 'ulp' (or margin of round off error) that we are allowed to
   *          use in our comparisons such that if expected and obtained are
   *          equal OR fall within a range of (+) or (-) expectedUlp of each
   *          other then we are considered equal.
   * @return 0 if the result and expected values are considered equal.
   */
  public static int testUlpCore(double result, double expected, double ulps) {
    String msg = "testUlpCore:  (double) result = " + result;
    msg += "\n              (double) expected = " + expected;
    msg += "\n              (double) ulps = " + ulps;
    TestUtil.logTrace(msg);

    if (Double.compare(expected, result) == 0) {
      return 0; // result and expected are equivalent
    } else {
      if (ulps == 0.0) {
        // Equivalent results required but not found
        return 1;
      } else {
        double difference = expected - result;
        TestUtil.logTrace(" (double) difference = " + difference);
        TestUtil.logTrace("Math.abs(difference/Math.ulp(expected)) = "
            + Math.abs(difference / Math.ulp(expected)));

        if (FpUtils.isUnordered(expected, result) ||
        // fail if unordered
            Double.isNaN(difference)) {
          TestUtil.logTrace("Unordered values - floating points do not match");
          return 1;
        } else if (!(Math.abs(difference / Math.ulp(expected)) <= Math
            .abs(ulps))) {
          // fail if ulp is out of range
          TestUtil.logTrace("invalid rounding error detected via ulp");
          return 1;
        } else {
          TestUtil.logTrace("roundoff was in acceptable range ");
          return 0;
        }
      }
    }
  }

}
