/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.el.spec.unaryoperator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Properties;


import com.sun.ts.tests.el.common.util.ExprEval;
import com.sun.ts.tests.el.common.util.NameValuePair;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.System.Logger;

public class ELClientIT {

  private static final Logger logger = System.getLogger(ELClientIT.class.getName());

  private final boolean[] deferred = { true, false };

  @AfterEach
  public void cleanup() throws Exception {
    logger.log(Logger.Level.INFO, "Cleanup method called");
  }

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "STARTING TEST : "+testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "FINISHED TEST : "+testInfo.getDisplayName());
  }

  /*
   * @testName: elNullUnaryTest
   * 
   * @assertion_ids: EL:SPEC:20.1
   * 
   * @test_Strategy: Validate that if null is passed with the unary minus
   * operator, the result is (Long) 0. Test both ${-null} & #{-null}
   */
  @Test
  public void elNullUnaryTest() throws Exception {

    boolean pass = false;
    String[] symbols = { "$", "#" };

    Long expectedResult = Long.valueOf("0");

    try {
      for (String prefix : symbols) {
        String expr = prefix + "{-null}";
        logger.log(Logger.Level.TRACE, "expression to be evaluated is " + expr);

        Object result = ExprEval.evaluateValueExpression(expr, null,
            Object.class);
        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass = (ExprEval.compareClass(result, Long.class)
            && ExprEval.compareValue((Long) result, expectedResult, 0));
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /*
   * @testName: elBigDecimalUnaryTest
   * 
   * @assertion_ids: EL:SPEC:20.2
   * 
   * @test_Strategy: Validate that if a BigDecimal A is passed with the unary
   * minus operator (-), the result is A.negate(). Test both ${- value} & #{-
   * value}
   */
  @Test
  public void elBigDecimalUnaryTest() throws Exception {

    boolean pass = false;

    BigDecimal bd = BigDecimal.valueOf(10.0);
    BigDecimal expectedResult = BigDecimal.valueOf(-10.0);

    NameValuePair value[] = NameValuePair.buildUnaryNameValue(bd);

    try {
      for (boolean tf : deferred) {

        String expr = ExprEval.buildElExpr(tf, "unary_minus");
        Object result = ExprEval.evaluateValueExpression(expr, value,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass = (ExprEval.compareClass(result, BigDecimal.class)
            && ExprEval.compareValue((BigDecimal) result, expectedResult, 0));
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /*
   * @testName: elBigIntegerUnaryTest
   * 
   * @assertion_ids: EL:SPEC:20.2
   * 
   * @test_Strategy: Validate that if a BigInteger A is passed with the unary
   * minus operator (-), the result is A.negate(). Test both ${- value} & #{-
   * value}
   */
  @Test
  public void elBigIntegerUnaryTest() throws Exception {

    boolean pass = false;

    BigInteger bi = BigInteger.valueOf(100);
    BigInteger expectedResult = BigInteger.valueOf(-100);

    NameValuePair value[] = NameValuePair.buildUnaryNameValue(bi);

    try {
      for (boolean tf : deferred) {

        String expr = ExprEval.buildElExpr(tf, "unary_minus");
        Object result = ExprEval.evaluateValueExpression(expr, value,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass = (ExprEval.compareClass(result, BigInteger.class)
            && ExprEval.compareValue((BigInteger) result, expectedResult, 0));
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /*
   * @testName: elDoubleStringUnaryTest
   * 
   * @assertion_ids: EL:SPEC:20.3.1
   * 
   * @test_Strategy: Validate that if a String is passed with the unary minus
   * operator (-), the result is as follows: - If the String contains ".", "e",
   * or "E" coerce to Double, apply operator.
   *
   * Test both ${- value} & #{- value}
   */
  @Test
  public void elDoubleStringUnaryTest() throws Exception {

    boolean pass = false;

    String[] values = { "10.0", "10E0", "10e0" };

    Double expectedResult = Double.valueOf("-10");

    try {
      for (boolean tf : deferred) {
        for (String val : values) {
          NameValuePair value[] = NameValuePair.buildUnaryNameValue(val);

          String expr = ExprEval.buildElExpr(tf, "unary_minus");
          Object result = ExprEval.evaluateValueExpression(expr, value,
              Object.class);

          logger.log(Logger.Level.TRACE, "result is " + result.toString());
          pass = (ExprEval.compareClass(result, Double.class)
              && ExprEval.compareValue((Double) result, expectedResult));
        }
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /*
   * @testName: elLongStringUnaryTest
   * 
   * @assertion_ids: EL:SPEC:20.3.2
   * 
   * @test_Strategy: Validate that if a String is passed with the unary minus
   * operator (-), the result is as follows: - If the String does not contain
   * ".", "e", or "E" coerce to Long, apply operator.
   *
   * Test both ${- value} & #{- value}
   */
  @Test
  public void elLongStringUnaryTest() throws Exception {

    boolean pass = false;

    String val = "100";

    Long expectedResult = Long.valueOf("-100");

    try {
      for (boolean tf : deferred) {
        NameValuePair value[] = NameValuePair.buildUnaryNameValue(val);

        String expr = ExprEval.buildElExpr(tf, "unary_minus");
        Object result = ExprEval.evaluateValueExpression(expr, value,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass = (ExprEval.compareClass(result, Long.class)
            && ExprEval.compareValue((Long) result, expectedResult));
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /*
   * @testName: elByteUnaryTest
   * 
   * @assertion_ids: EL:SPEC:20.4.1
   * 
   * @test_Strategy: Validate that if a Byte is passed with the unary minus
   * operator (-), the type is retained and the operator is applied.
   *
   * Test both ${- value} & #{- value}
   */
  @Test
  public void elByteUnaryTest() throws Exception {

    boolean pass = false;

    byte num = 1;
    byte expectedResult = -1;

    NameValuePair value[] = NameValuePair.buildUnaryNameValue(num);

    try {
      for (boolean tf : deferred) {

        String expr = ExprEval.buildElExpr(tf, "unary_minus");
        Object result = ExprEval.evaluateValueExpression(expr, value,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass = (ExprEval.compareClass(result, Byte.class)
            && ExprEval.compareValue((Byte) result, expectedResult));
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /*
   * @testName: elShortUnaryTest
   * 
   * @assertion_ids: EL:SPEC:20.4.1
   * 
   * @test_Strategy: Validate that if a Short is passed with the unary minus
   * operator (-), the type is retained and the operator is applied.
   *
   * Test both ${- value} & #{- value}
   */
  @Test
  public void elShortUnaryTest() throws Exception {

    boolean pass = false;

    short num = 1;
    short expectedResult = -1;

    NameValuePair value[] = NameValuePair.buildUnaryNameValue(num);

    try {
      for (boolean tf : deferred) {

        String expr = ExprEval.buildElExpr(tf, "unary_minus");
        Object result = ExprEval.evaluateValueExpression(expr, value,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass = (ExprEval.compareClass(result, Short.class)
            && ExprEval.compareValue((Short) result, expectedResult));
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /*
   * @testName: elIntegerUnaryTest
   * 
   * @assertion_ids: EL:SPEC:20.4.1
   * 
   * @test_Strategy: Validate that if an Integer is passed with the unary minus
   * operator (-), the type is retained and the operator is applied.
   *
   * Test both ${- value} & #{- value}
   */
  @Test
  public void elIntegerUnaryTest() throws Exception {

    boolean pass = false;

    int num = 1;
    int expectedResult = -1;

    NameValuePair value[] = NameValuePair.buildUnaryNameValue(num);

    try {
      for (boolean tf : deferred) {

        String expr = ExprEval.buildElExpr(tf, "unary_minus");
        Object result = ExprEval.evaluateValueExpression(expr, value,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass = (ExprEval.compareClass(result, Integer.class)
            && ExprEval.compareValue((Integer) result, expectedResult));
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /*
   * @testName: elLongUnaryTest
   * 
   * @assertion_ids: EL:SPEC:20.4.1
   * 
   * @test_Strategy: Validate that if a Long is passed with the unary minus
   * operator (-), the type is retained and the operator is applied.
   *
   * Test both ${- value} & #{- value}
   */
  @Test
  public void elLongUnaryTest() throws Exception {

    boolean pass = false;

    long num = 10000;
    long expectedResult = -10000;

    NameValuePair value[] = NameValuePair.buildUnaryNameValue(num);

    try {
      for (boolean tf : deferred) {

        String expr = ExprEval.buildElExpr(tf, "unary_minus");
        Object result = ExprEval.evaluateValueExpression(expr, value,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass = (ExprEval.compareClass(result, Long.class)
            && ExprEval.compareValue((Long) result, expectedResult));
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /*
   * @testName: elFloatUnaryTest
   * 
   * @assertion_ids: EL:SPEC:20.4.1
   * 
   * @test_Strategy: Validate that if a Float is passed with the unary minus
   * operator (-), the type is retained and the operator is applied.
   *
   * Test both ${- value} & #{- value}
   */
  @Test
  public void elFloatUnaryTest() throws Exception {

    boolean pass = false;

    float num = 10000f;
    float expectedResult = -10000f;

    NameValuePair value[] = NameValuePair.buildUnaryNameValue(num);

    try {
      for (boolean tf : deferred) {

        String expr = ExprEval.buildElExpr(tf, "unary_minus");
        Object result = ExprEval.evaluateValueExpression(expr, value,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass = (ExprEval.compareClass(result, Float.class)
            && ExprEval.compareValue((Float) result, expectedResult, 0));
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /*
   * @testName: elDoubleUnaryTest
   * 
   * @assertion_ids: EL:SPEC:20.4.1
   * 
   * @test_Strategy: Validate that if a Double is passed with the unary minus
   * operator (-), the type is retained and the operator is applied.
   *
   * Test both ${- value} & #{- value}
   */
  @Test
  public void elDoubleUnaryTest() throws Exception {

    boolean pass = false;

    double num = 10000.0;
    double expectedResult = -10000.0;

    NameValuePair value[] = NameValuePair.buildUnaryNameValue(num);

    try {
      for (boolean tf : deferred) {

        String expr = ExprEval.buildElExpr(tf, "unary_minus");
        Object result = ExprEval.evaluateValueExpression(expr, value,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass = (ExprEval.compareClass(result, Double.class)
            && ExprEval.compareValue((Double) result, expectedResult));
      }

      if (!pass)
        throw new Exception("TEST FAILED: pass = false");

    } catch (Exception e) {
      throw new Exception(e);
    }
  }

  /*
   * @testName: elBooleanUnaryFalseTest
   * 
   * @assertion_ids: EL:SPEC:24.1.1
   * 
   * @test_Strategy: Validate that if a String("false") is passed with the unary
   * "not" operator, the type is coerced to Boolean and the operator is applied.
   *
   * Equations Tested: ${not value} & #{not value} ${! value} & #{! value}
   */
  @Test
  public void elBooleanUnaryFalseTest() throws Exception {

    this.testUnary("false", true);

  }

  /*
   * @testName: elBooleanUnaryTrueTest
   * 
   * @assertion_ids: EL:SPEC:24.1.1
   * 
   * @test_Strategy: Validate that if a String("true") is passed with the unary
   * "not" operator, the type is coerced to Boolean and the operator is applied.
   *
   * Equations Tested: ${not value} & #{not value} ${! value} & #{! value}
   */
  @Test
  public void elBooleanUnaryTrueTest() throws Exception {

    this.testUnary("true", false);

  }

  // ---------------------------------------------------------- private methods

  private void testUnary(String testVal, boolean expectedResult) throws Exception {

    boolean pass = false;

    String[] operator = { "unary_not", "unary_bang" };
    NameValuePair value[] = NameValuePair.buildUnaryNameValue(testVal);

    try {
      for (boolean tf : deferred) {
        for (String op : operator) {
          String expr = ExprEval.buildElExpr(tf, op);
          Object result = ExprEval.evaluateValueExpression(expr, value,
              Object.class);

          logger.log(Logger.Level.TRACE, "result is " + result.toString());
          pass = (ExprEval.compareClass(result, Boolean.class)
              && ExprEval.compareValue((Boolean) result, expectedResult));

          if (!pass)
            throw new Exception("TEST FAILED: pass = false");
        }
      }
    } catch (Exception e) {
      throw new Exception(e);
    }
  }
}
