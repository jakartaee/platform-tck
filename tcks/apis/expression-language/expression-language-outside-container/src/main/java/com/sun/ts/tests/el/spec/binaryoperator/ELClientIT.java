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

package com.sun.ts.tests.el.spec.binaryoperator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Properties;


import com.sun.ts.tests.el.common.util.ExprEval;
import com.sun.ts.tests.el.common.util.TestNum;
import com.sun.ts.tests.el.common.util.Validator;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.System.Logger;

public class ELClientIT {

  private static final Logger logger = System.getLogger(ELClientIT.class.getName());

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


  /**
   * @testName: elNullOperandAddTest
   * @assertion_ids: EL:SPEC:17.1
   * @test_Strategy: Validate that if both of the operands in an EL "+"
   *                 (addition) operation are null, the result is (Long) 0.
   */
  @Test
  public void elNullOperandAddTest() throws Exception {

    boolean pass = false;

    Long expectedResult = Long.valueOf("0");
    try {
      String expr = ExprEval.buildElExpr(true, "+");
      logger.log(Logger.Level.TRACE, "expression to be evaluated is " + expr);

      Object result = ExprEval.evaluateValueExpression(expr, null,
          Object.class);
      logger.log(Logger.Level.TRACE, "result is " + result.toString());
      pass = (ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue((Long) result, expectedResult, 0));

    } catch (Exception e) {
      throw new Exception(e);
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");
  }

  /**
   * @testName: elNullOperandSubtractTest
   * @assertion_ids: EL:SPEC:17.1
   * @test_Strategy: Validate that if both of the operands in an EL "-"
   *                 (subtraction) operation are null, the result is (Long) 0.
   */
  @Test
  public void elNullOperandSubtractTest() throws Exception {

    boolean pass = false;

    Long expectedResult = Long.valueOf("0");
    try {
      String expr = ExprEval.buildElExpr(false, "-");
      logger.log(Logger.Level.TRACE, "expression to be evaluated is " + expr);

      Object result = ExprEval.evaluateValueExpression(expr, null,
          Object.class);
      logger.log(Logger.Level.TRACE, "result is " + result.toString());
      pass = (ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue((Long) result, expectedResult, 0));

    } catch (Exception e) {
      throw new Exception(e);
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");
  }

  /**
   * @testName: elNullOperandMultiplyTest
   * @assertion_ids: EL:SPEC:17.1
   * @test_Strategy: Validate that if both of the operands in an EL "*"
   *                 (multiplication) operation are null, the result is (Long)
   *                 0.
   */
  @Test
  public void elNullOperandMultiplyTest() throws Exception {

    boolean pass = false;

    Long expectedResult = Long.valueOf("0");
    try {
      String expr = ExprEval.buildElExpr(true, "*");
      logger.log(Logger.Level.TRACE, "expression to be evaluated is " + expr);

      Object result = ExprEval.evaluateValueExpression(expr, null,
          Object.class);
      logger.log(Logger.Level.TRACE, "result is " + result.toString());
      pass = (ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue((Long) result, expectedResult, 0));

    } catch (Exception e) {
      throw new Exception(e);
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");
  }

  /**
   * @testName: elNullOperandDivisionTest
   * @assertion_ids: EL:SPEC:18.1
   * @test_Strategy: Validate that if both of the operands in an EL "/"
   *                 (division) operation are null, the result is (Long) 0.
   * 
   */
  @Test
  public void elNullOperandDivisionTest() throws Exception {

    boolean pass = false;
    Long expectedResult = Long.valueOf("0");

    try {
      String expr = ExprEval.buildElExpr(true, "/");
      logger.log(Logger.Level.TRACE, "expression to be evaluated is " + expr);

      Object result = ExprEval.evaluateValueExpression(expr, null,
          Object.class);
      logger.log(Logger.Level.TRACE, "result is " + result.toString());
      pass = (ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue((Long) result, expectedResult, 0));

    } catch (Exception e) {
      throw new Exception(e);
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");
  }

  /**
   * @testName: elNullOperandModulusTest
   * @assertion_ids: EL:SPEC:19.1
   * @test_Strategy: Validate that if both of the operands in an EL "%" (mod)
   *                 operation are null, the result is (Long) 0.
   * 
   */
  @Test
  public void elNullOperandModulusTest() throws Exception {

    boolean pass = false;

    Long expectedResult = Long.valueOf("0");
    try {
      String expr = ExprEval.buildElExpr(true, "%");
      logger.log(Logger.Level.TRACE, "expression to be evaluated is " + expr);

      Object result = ExprEval.evaluateValueExpression(expr, null,
          Object.class);
      logger.log(Logger.Level.TRACE, "result is " + result.toString());
      pass = (ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue((Long) result, expectedResult, 0));

    } catch (Exception e) {
      throw new Exception(e);
    }

    if (!pass)
      throw new Exception("TEST FAILED: pass = false");
  }

  /**
   * @testName: elBigDecimalAddTest
   * @assertion_ids: EL:SPEC:17.2.1
   * @test_Strategy: Validate that if one of the operands in an EL "+"
   *                 (addition) operation is a BigDecimal, the result is coerced
   *                 to BigDecimal and is the sum of the operands.
   * 
   *                 Equations tested: BigDecimal + BigDecimal BigDecimal +
   *                 Double BigDecimal + Float BigDecimal + String containing
   *                 ".", "e", or "E" BigDecimal + BigInteger BigDecimal +
   *                 Integer BigDecimal + Long BigDecimal + Short BigDecimal +
   *                 Byte
   */
  @Test
  public void elBigDecimalAddTest() throws Exception {

    BigDecimal testValue = BigDecimal.valueOf(10.531);
    BigDecimal expectedResult = BigDecimal.valueOf(11.531);

    Validator.testBigDecimal(testValue, expectedResult, "+");

  }

  /**
   * @testName: elBigDecimalSubtractTest
   * @assertion_ids: EL:SPEC:17.2.2
   * @test_Strategy: Validate that if one of the operands in an EL "-"
   *                 (subtraction) operation is a BigDecimal, the result is
   *                 coerced to BigDecimal and is the difference of the
   *                 operands.
   * 
   *                 Equations tested: BigDecimal - BigDecimal BigDecimal -
   *                 Double BigDecimal - Float BigDecimal - String containing
   *                 ".", "e", or "E" BigDecimal - BigInteger BigDecimal -
   *                 Integer BigDecimal - Long BigDecimal - Short BigDecimal -
   *                 Byte
   */
  @Test
  public void elBigDecimalSubtractTest() throws Exception {

    BigDecimal testValue = BigDecimal.valueOf(10.531);
    BigDecimal expectedResult = BigDecimal.valueOf(9.531);

    Validator.testBigDecimal(testValue, expectedResult, "-");

  }

  /**
   * @testName: elBigDecimalMultiplyTest
   * @assertion_ids: EL:SPEC:17.2.3
   * @test_Strategy: Validate that if one of the operands in an EL "*"
   *                 (multiplication) operation is a BigDecimal, the result is
   *                 coerced to BigDecimal and is the product of the operands.
   * 
   *                 Equations tested: BigDecimal * BigDecimal BigDecimal *
   *                 Double BigDecimal * Float BigDecimal * String containing
   *                 ".", "e", or "E" BigDecimal * BigInteger BigDecimal *
   *                 Integer BigDecimal * Long BigDecimal * Short BigDecimal *
   *                 Byte
   */
  @Test
  public void elBigDecimalMultiplyTest() throws Exception {

    BigDecimal testValue = BigDecimal.valueOf(1.5);
    BigDecimal expectedResult = BigDecimal.valueOf(1.5);

    Validator.testBigDecimal(testValue, expectedResult, "*");

  }

  /**
   * @testName: elBigDecimalDivisionTest
   * @assertion_ids: EL:SPEC:18.2
   * @test_Strategy: Validate that if one of the operands in an EL "/" (div)
   *                 operation is a BigDecimal, the result is coerced to
   *                 BigDecimal and is the quotient of the operands.
   * 
   *                 Equations tested: BigDecimal / BigDecimal BigDecimal /
   *                 Double BigDecimal / Float BigDecimal / String containing
   *                 ".", "e", or "E" BigDecimal / BigInteger BigDecimal /
   *                 Integer BigDecimal / Long BigDecimal / Short BigDecimal /
   *                 Byte
   */
  @Test
  public void elBigDecimalDivisionTest() throws Exception {

    BigDecimal testValue = BigDecimal.valueOf(3.0);
    BigDecimal expectedResult = BigDecimal.valueOf(3.0);

    Validator.testBigDecimal(testValue, expectedResult, "/");

  }

  /**
   * @testName: elBigDecimalModulusTest
   * @assertion_ids: EL:SPEC:19.2
   * @test_Strategy: Validate that if one of the operands in an EL "%" (mod)
   *                 operation is a BigDecimal, the result is coerced to Double
   *                 and is the remainder of the quotient of the operands.
   * 
   *                 Equations tested: BigDecimal % BigDecimal BigDecimal %
   *                 Double BigDecimal % Float BigDecimal % String containing
   *                 ".", "e", or "E" BigDecimal % BigInteger BigDecimal %
   *                 Integer BigDecimal % Long BigDecimal % Short BigDecimal %
   *                 Byte
   */
  @Test
  public void elBigDecimalModulusTest() throws Exception {

    BigDecimal testValue = BigDecimal.valueOf(2.5);
    BigDecimal expectedResult = BigDecimal.valueOf(0.5);

    Validator.testBigDecimal(testValue, expectedResult, "%");

  }

  /**
   * @testName: elBigIntegerAddTest
   * @assertion_ids: EL:SPEC:17.4.1
   * @test_Strategy: Validate that if one of the operands in an EL "+"
   *                 (addition) operation is a BigInteger, the result is coerced
   *                 to BigInteger and is the sum of the operands.
   * 
   *                 Equations tested: BigInteger + BigInteger BigInteger +
   *                 Integer BigInteger + Long BigInteger + Short BigInteger +
   *                 Byte
   */
  @Test
  public void elBigIntegerAddTest() throws Exception {

    BigInteger testValue = BigInteger.valueOf(10531);
    BigInteger expectedResult = BigInteger.valueOf(10532);

    Validator.testBigInteger(testValue, expectedResult, "+");

  }

  /**
   * @testName: elBigIntegerSubtractTest
   * @assertion_ids: EL:SPEC:17.4.2
   * @test_Strategy: Validate that if one of the operands in an EL "-"
   *                 (subtraction) operation is a BigInteger, the result is
   *                 coerced to BigInteger and is the difference of the
   *                 operands.
   * 
   *                 Equations tested: BigInteger - BigInteger BigInteger -
   *                 Integer BigInteger - Long BigInteger - Short BigInteger -
   *                 Byte
   */
  @Test
  public void elBigIntegerSubtractTest() throws Exception {

    BigInteger testValue = BigInteger.valueOf(10531);
    BigInteger expectedResult = BigInteger.valueOf(10530);

    Validator.testBigInteger(testValue, expectedResult, "-");

  }

  /**
   * @testName: elBigIntegerMultiplyTest
   * @assertion_ids: EL:SPEC:17.4.3
   * @test_Strategy: Validate that if one of the operands in an EL "*" operation
   *                 is a BigInteger, the result is coerced to BigInteger and is
   *                 the product of the operands.
   * 
   *                 BigInteger * BigInteger BigInteger * Integer BigInteger *
   *                 Long BigInteger * Short BigInteger * Byte
   */
  @Test
  public void elBigIntegerMultiplyTest() throws Exception {

    BigInteger testValue = BigInteger.valueOf(10531);
    BigInteger expectedResult = BigInteger.valueOf(10531);

    Validator.testBigInteger(testValue, expectedResult, "*");

  }

  /**
   * @testName: elBigIntegerDivisionTest
   * @assertion_ids: EL:SPEC:18.2
   * @test_Strategy: Validate that if one of the operands in an EL "/" (div)
   *                 operation is a BigInteger, the result is coerced to
   *                 BigDecimal and is the quotient of the operands.
   * 
   *                 BigInteger / BigInteger BigInteger / Integer BigInteger /
   *                 Long BigInteger / Short BigInteger / Byte
   */
  @Test
  public void elBigIntegerDivisionTest() throws Exception {

    BigInteger testValue = BigInteger.valueOf(10531);
    BigInteger expectedResult = BigInteger.valueOf(10531);

    Validator.testBigInteger(testValue, expectedResult, "/");

  }

  /**
   * @testName: elBigIntegerModulusTest
   * @assertion_ids: EL:SPEC:19.3
   * @test_Strategy: Validate that if one of the operands in an EL "%" (mod)
   *                 operation is a BigInteger, the result is coerced to
   *                 BigInteger and is the remainder of the quotient of the
   *                 operands.
   * 
   *                 BigInteger % BigInteger BigInteger % Integer BigInteger %
   *                 Long BigInteger % Short BigInteger % Byte
   */
  @Test
  public void elBigIntegerModulusTest() throws Exception {

    BigInteger testValue = BigInteger.valueOf(10531);
    BigInteger expectedResult = BigInteger.valueOf(0);

    Validator.testBigInteger(testValue, expectedResult, "%");

  }

  /**
   * @testName: elFloatAddTest
   * @assertion_ids: EL:SPEC:17.3.1; EL:SPEC:17.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "+"
   *                 (addition) operation is a Float, the result is coerced to
   *                 Double and is the sum of the operands.
   * 
   *                 Equations tested: Float + Double Float + Float Float +
   *                 String containing ".", "e", or "E" Float + BigInteger Float
   *                 + Integer Float + Long Float + Short Float + Byte
   */
  @Test
  public void elFloatAddTest() throws Exception {

    Float expectedResult;
    // For each float in validate List.
    for (Iterator<?> it = TestNum.getFloatList().iterator(); it.hasNext();) {
      Float testValue = (Float) it.next();
      expectedResult = testValue + Float.valueOf("1.0");
      Validator.testFloat(testValue, expectedResult, "+");
    }

  }

  /**
   * @testName: elFloatSubtractTest
   * @assertion_ids: EL:SPEC:17.3.1; EL:SPEC:17.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "-"
   *                 (subtraction) operation is a Float, the result is coerced
   *                 to Double and is the difference of the operands.
   * 
   *                 Equations tested: Float - Double Float - Float Float -
   *                 String containing ".", "e", or "E" Float - BigInteger Float
   *                 - Integer Float - Long Float - Short Float - Byte
   */
  @Test
  public void elFloatSubtractTest() throws Exception {

    Float expectedResult;
    // For each float in validate List.
    for (Iterator<?> it = TestNum.getFloatList().iterator(); it.hasNext();) {
      Float testValue = (Float) it.next();
      expectedResult = testValue - Float.valueOf("1.0");
      Validator.testFloat(testValue, expectedResult, "-");
    }

  }

  /**
   * @testName: elFloatMultiplyTest
   * @assertion_ids: EL:SPEC:17.3.1; EL:SPEC:17.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "*"
   *                 (multiplication) operation is a Float, the result is
   *                 coerced to Double and is the product of the operands.
   * 
   *                 Equations tested: Float * Double Float * Float Float *
   *                 String containing ".", "e", or "E" Float * BigInteger Float
   *                 * Integer Float * Long Float * Short Float * Byte
   */
  @Test
  public void elFloatMultiplyTest() throws Exception {

    Float expectedResult;
    // For each float in validate List.
    for (Iterator<?> it = TestNum.getFloatList().iterator(); it.hasNext();) {
      Float testValue = (Float) it.next();
      expectedResult = testValue * Float.valueOf("1.0");
      Validator.testFloat(testValue, expectedResult, "*");
    }

  }

  /**
   * @testName: elFloatDivisionTest
   * @assertion_ids: EL:SPEC:18.2; EL:SPEC:18.3
   * @test_Strategy: Validate that if one of the operands in an EL "/" (div)
   *                 operation is a Float, the result is coerced to Double and
   *                 is the quotient of the operands.
   * 
   *                 Equations tested: Float / Double Float / Float Float /
   *                 String containing ".", "e", or "E" Float / BigInteger Float
   *                 / Integer Float / Long Float / Short Float / Byte
   */
  @Test
  public void elFloatDivisionTest() throws Exception {

    Float expectedResult;
    // For each float in validate List.
    for (Iterator<?> it = TestNum.getFloatList().iterator(); it.hasNext();) {
      Float testValue = (Float) it.next();
      expectedResult = testValue / Float.valueOf("1.0");
      Validator.testFloat(testValue, expectedResult, "/");
    }

  }

  /**
   * @testName: elFloatModulusTest
   * @assertion_ids: EL:SPEC:19.2
   * @test_Strategy: Validate that if one of the operands in an EL "%" (mod)
   *                 operation is a Float, the result is coerced to Double and
   *                 is the remainder of the quotient of the operands.
   * 
   *                 Equations tested: Float % Double Float % Float Float %
   *                 String containing ".", "e", or "E" Float % BigInteger Float
   *                 % Integer Float % Long Float % Short Float % Byte
   */
  @Test
  public void elFloatModulusTest() throws Exception {

    Float expectedResult;
    // For each float in validate List.
    for (Iterator<?> it = TestNum.getFloatList().iterator(); it.hasNext();) {
      Float testValue = (Float) it.next();
      expectedResult = testValue % Float.valueOf("1.0");
      Validator.testFloat(testValue, expectedResult, "%");
    }

  }

  /**
   * @testName: elDoubleAddTest
   * @assertion_ids: EL:SPEC:17.3.1; EL:SPEC:17.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "+"
   *                 (addition) operation is a Double, the result is coerced to
   *                 Double and is the sum of the operands.
   * 
   *                 Equations tested: Double + Double Double + String
   *                 containing ".", "e", or "E" Double + BigInteger Double +
   *                 Integer Double + Long Double + Short Double + Byte
   */
  @Test
  public void elDoubleAddTest() throws Exception {

    Double testValue = Double.valueOf(2.5);
    Double expectedResult = Double.valueOf(3.5);

    Validator.testDouble(testValue, expectedResult, "+");

  }

  /**
   * @testName: elDoubleSubtractTest
   * @assertion_ids: EL:SPEC:17.3.1; EL:SPEC:17.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "-"
   *                 (subtraction) operation is a Double, the result is coerced
   *                 to Double and is the difference of the operands.
   * 
   *                 Equations tested: Double - Double Double - String
   *                 containing ".", "e", or "E" Double - BigInteger Double -
   *                 Integer Double - Long Double - Short Double - Byte
   */
  @Test
  public void elDoubleSubtractTest() throws Exception {

    Double testValue = Double.valueOf(2.5);
    Double expectedResult = Double.valueOf(1.5);

    Validator.testDouble(testValue, expectedResult, "-");

  }

  /**
   * @testName: elDoubleMultiplyTest
   * @assertion_ids: EL:SPEC:17.3.1; EL:SPEC:17.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "*"
   *                 (multiplication) operation is a Double, the result is
   *                 coerced to Double and is the product of the operands.
   * 
   *                 Equations tested: Double * Double Double * String
   *                 containing ".", "e", or "E" Double * BigInteger Double *
   *                 Integer Double * Long Double * Short Double * Byte
   */
  @Test
  public void elDoubleMultiplyTest() throws Exception {

    Double testValue = Double.valueOf(2.5);
    Double expectedResult = Double.valueOf(2.5);

    Validator.testDouble(testValue, expectedResult, "*");

  }

  /**
   * @testName: elDoubleDivisionTest
   * @assertion_ids: EL:SPEC:18.2; EL:SPEC:18.3
   * @test_Strategy: Validate that if one of the operands in an EL "/" (div)
   *                 operation is a Double, the result is coerced to Double and
   *                 is the quotient of the operands.
   * 
   *                 Equations tested: Double / Double Double / String
   *                 containing ".", "e", or "E" Double / BigInteger Double /
   *                 Integer Double / Long Double / Short Double / Byte
   */
  @Test
  public void elDoubleDivisionTest() throws Exception {

    Double testValue = Double.valueOf(2.5);
    Double expectedResult = Double.valueOf(2.5);

    Validator.testDouble(testValue, expectedResult, "/");

  }

  /**
   * @testName: elDoubleModulusTest
   * @assertion_ids: EL:SPEC:19.2
   * @test_Strategy: Validate that if one of the operands in an EL "%" (mod)
   *                 operation is a Double, the result is coerced to Double and
   *                 is the remainder of the quotient of the operands.
   * 
   *                 Equations tested: Double % Double Double % String
   *                 containing ".", "e", or "E" Double % BigInteger Double %
   *                 Integer Double % Long Double % Short Double % Byte
   */
  @Test
  public void elDoubleModulusTest() throws Exception {

    Double testValue = Double.valueOf(2.5);
    Double expectedResult = Double.valueOf(0.5);

    Validator.testDouble(testValue, expectedResult, "%");
  }

  /**
   * @testName: elNumericStringSubtractTest
   * @assertion_ids: EL:SPEC:17.3.1; EL:SPEC:17.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "-"
   *                 (subtraction) operation is a numeric string, the result is
   *                 coerced to Double and is the difference of the operands.
   * 
   *                 Equations tested: Numeric String - String containing ".",
   *                 "e", or "E" Numeric String - BigInteger Numeric String -
   *                 Integer Numeric String - Long Numeric String - Short
   *                 Numeric String - Byte
   */
  @Test
  public void elNumericStringSubtractTest() throws Exception {

    String testValue = "25e-1";
    Double expectedResult = Double.valueOf(1.5);

    Validator.testNumericString(testValue, expectedResult, "-");

  }

  /**
   * @testName: elNumericStringMultiplyTest
   * @assertion_ids: EL:SPEC:17.3.1; EL:SPEC:17.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "*"
   *                 (multiplication) operation is a numeric string, the result
   *                 is coerced to Double and is the product of the operands.
   * 
   *                 Equations tested: Numeric String * String containing ".",
   *                 "e", or "E" Numeric String * BigInteger Numeric String *
   *                 Integer Numeric String * Long Numeric String * Short
   *                 Numeric String * Byte
   */
  @Test
  public void elNumericStringMultiplyTest() throws Exception {

    String testValue = "25E-1";
    Double expectedResult = Double.valueOf(2.5);

    Validator.testNumericString(testValue, expectedResult, "*");

  }

  /**
   * @testName: elNumericStringDivisionTest
   * @assertion_ids: EL:SPEC:18.2; EL:SPEC:18.3
   * @test_Strategy: Validate that if one of the operands in an EL "/" (div)
   *                 operation is a numeric string, the result is coerced to
   *                 Double and is the quotient of the operands.
   * 
   *                 Equations tested: Numeric String / String containing ".",
   *                 "e", or "E" Numeric String / BigInteger Numeric String /
   *                 Integer Numeric String / Long Numeric String / Short
   *                 Numeric String / Byte
   */
  @Test
  public void elNumericStringDivisionTest() throws Exception {

    String testValue = "2.5";
    Double expectedResult = Double.valueOf(2.5);

    Validator.testNumericString(testValue, expectedResult, "/");

  }

  /**
   * @testName: elNumericStringModulusTest
   * @assertion_ids: EL:SPEC:19.2
   * @test_Strategy: Validate that if one of the operands in an EL "%" (mod)
   *                 operation is a numeric string, the result is coerced to
   *                 Double and is the remainder of the quotient of the
   *                 operands.
   * 
   *                 Equations tested: Numeric String % String containing ".",
   *                 "e", or "E" Numeric String % BigInteger Numeric String %
   *                 Integer Numeric String % Long Numeric String % Short
   *                 Numeric String % Byte
   */
  @Test
  public void elNumericStringModulusTest() throws Exception {

    String testValue = "2.5e0";
    Double expectedResult = Double.valueOf(0.5);

    Validator.testNumericString(testValue, expectedResult, "%");

  }

  /**
   * @testName: elLongAddTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "+"
   *                 (addition) operation is a Long, the result is coerced to
   *                 Long and is the sum of the operands.
   * 
   *                 Equations tested: Long + Integer Long + Long Long + Short
   *                 Long + Byte
   */
  @Test
  public void elLongAddTest() throws Exception {

    Long testValue = Long.valueOf(25000);
    Long expectedResult = Long.valueOf(25001);

    Validator.testLong(testValue, expectedResult, "+");

  }

  /**
   * @testName: elLongSubtractTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "-"
   *                 (subtraction) operation is a Long, the result is coerced to
   *                 Long and is the difference of the operands.
   * 
   *                 Equations tested: Long - Integer Long - Long Long - Short
   *                 Long - Byte
   */
  @Test
  public void elLongSubtractTest() throws Exception {

    Long testValue = Long.valueOf(25000);
    Long expectedResult = Long.valueOf(24999);

    Validator.testLong(testValue, expectedResult, "-");

  }

  /**
   * @testName: elLongMultiplyTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "*"
   *                 (multiplication) operation is a Long, the result is coerced
   *                 to Long and is the product of the operands.
   * 
   *                 Equations tested: Long * Integer Long * Long Long * Short
   *                 Long * Byte
   */
  @Test
  public void elLongMultiplyTest() throws Exception {

    Long testValue = Long.valueOf(25000);
    Long expectedResult = Long.valueOf(25000);

    Validator.testLong(testValue, expectedResult, "*");

  }

  /**
   * @testName: elLongDivisionTest
   * @assertion_ids: EL:SPEC:18.3
   * @test_Strategy: Validate that if one of the operands in an EL "/" (div)
   *                 operation is a Long, the result is coerced to Double and is
   *                 the quotient of the operands.
   * 
   *                 Equations tested: Long / Integer Long / Long Long / Short
   *                 Long / Byte
   */
  @Test
  public void elLongDivisionTest() throws Exception {

    Long testValue = Long.valueOf(25000);
    Long expectedResult = Long.valueOf(25000);

    Validator.testLong(testValue, expectedResult, "/");

  }

  /**
   * @testName: elLongModulusTest
   * @assertion_ids: EL:SPEC:19.4
   * @test_Strategy: Validate that if one of the operands in an EL "%" (mod)
   *                 operation is a Long, the result is coerced to Long and is
   *                 the remainder of the quotient of the operands.
   * 
   *                 Equations tested: Long % Integer Long % Long Long % Short
   *                 Long % Byte
   */
  @Test
  public void elLongModulusTest() throws Exception {

    Long testValue = Long.valueOf(25000);
    Long expectedResult = Long.valueOf(0);

    Validator.testLong(testValue, expectedResult, "%");

  }

  /**
   * @testName: elIntegerAddTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "+"
   *                 (addition) operation is a Integer, the result is coerced to
   *                 Long and is the sum of the operands.
   * 
   *                 Equations tested: Integer + Integer Integer + Short Integer
   *                 + Byte
   */
  @Test
  public void elIntegerAddTest() throws Exception {

    Integer testValue = Integer.valueOf(25);
    Integer expectedResult = Integer.valueOf(26);

    Validator.testInteger(testValue, expectedResult, "+");

  }

  /**
   * @testName: elIntegerSubtractTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "-"
   *                 (subtraction) operation is a Integer, the result is coerced
   *                 to Long and is the difference of the operands.
   * 
   *                 Equations tested: Long - Integer Long - Short Long - Byte
   */
  @Test
  public void elIntegerSubtractTest() throws Exception {

    Integer testValue = Integer.valueOf(25);
    Integer expectedResult = Integer.valueOf(24);

    Validator.testInteger(testValue, expectedResult, "-");

  }

  /**
   * @testName: elIntegerMultiplyTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "*"
   *                 (multiplication) operation is a Integer, the result is
   *                 coerced to Long and is the product of the operands.
   * 
   *                 Equations tested: Integer * Integer Integer * Short Integer
   *                 * Byte
   */
  @Test
  public void elIntegerMultiplyTest() throws Exception {

    Integer testValue = Integer.valueOf(25);
    Integer expectedResult = Integer.valueOf(25);

    Validator.testInteger(testValue, expectedResult, "*");

  }

  /**
   * @testName: elIntegerDivisionTest
   * @assertion_ids: EL:SPEC:18.3
   * @test_Strategy: Validate that if one of the operands in an EL "/" (div)
   *                 operation is a Integer, the result is coerced to Double and
   *                 is the quotient of the operands.
   * 
   *                 Equations tested: Integer / Integer Integer / Short Integer
   *                 / Byte
   */
  @Test
  public void elIntegerDivisionTest() throws Exception {

    Integer testValue = Integer.valueOf(25);
    Integer expectedResult = Integer.valueOf(25);

    Validator.testInteger(testValue, expectedResult, "/");

  }

  /**
   * @testName: elIntegerModulusTest
   * @assertion_ids: EL:SPEC:19.4
   * @test_Strategy: Validate that if one of the operands in an EL "%" (mod)
   *                 operation is a Integer, the result is coerced to Long and
   *                 is the remainder of the quotient of the operands.
   * 
   *                 Equations tested: Integer % Integer Integer % Short Integer
   *                 % Byte
   */
  @Test
  public void elIntegerModulusTest() throws Exception {

    Integer testValue = Integer.valueOf(25);
    Integer expectedResult = Integer.valueOf(0);

    Validator.testInteger(testValue, expectedResult, "%");

  }

  /**
   * @testName: elShortAddTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "+"
   *                 (addition) operation is a Short, the result is coerced to
   *                 Long and is the sum of the operands.
   * 
   *                 Equations tested: Short + Short Short + Byte
   */
  @Test
  public void elShortAddTest() throws Exception {

    Short testValue = Short.valueOf("2");
    Short expectedResult = Short.valueOf("3");

    Validator.testShort(testValue, expectedResult, "+");

  }

  /**
   * @testName: elShortSubtractTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "-"
   *                 (subtraction) operation is a Short, the result is coerced
   *                 to Long and is the difference of the operands.
   * 
   *                 Equations tested: Short - Short Short - Byte
   */
  @Test
  public void elShortSubtractTest() throws Exception {

    Short testValue = Short.valueOf("2");
    Short expectedResult = Short.valueOf("1");

    Validator.testShort(testValue, expectedResult, "-");

  }

  /**
   * @testName: elShortMultiplyTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if one of the operands in an EL "*"
   *                 (multiplication) operation is a Short, the result is
   *                 coerced to Long and is the product of the operands.
   * 
   *                 Equations tested: Short * Short Short * Byte
   */
  @Test
  public void elShortMultiplyTest() throws Exception {

    Short testValue = Short.valueOf("2");
    Short expectedResult = Short.valueOf("2");

    Validator.testShort(testValue, expectedResult, "*");

  }

  /**
   * @testName: elShortDivisionTest
   * @assertion_ids: EL:SPEC:18.3
   * @test_Strategy: Validate that if one of the operands in an EL "/" (div)
   *                 operation is a Short, the result is coerced to Double and
   *                 is the quotient of the operands.
   * 
   *                 Equations tested: Short / Short Short / Byte
   */
  @Test
  public void elShortDivisionTest() throws Exception {

    Short testValue = Short.valueOf("2");
    Short expectedResult = Short.valueOf("2");

    Validator.testShort(testValue, expectedResult, "/");

  }

  /**
   * @testName: elShortModulusTest
   * @assertion_ids: EL:SPEC:19.4
   * @test_Strategy: Validate that if one of the operands in an EL "%" (mod)
   *                 operation is a Short, the result is coerced to Long and is
   *                 the remainder of the quotient of the operands.
   * 
   *                 Equations tested: Short % Short Short % Byte
   */
  @Test
  public void elShortModulusTest() throws Exception {

    Short testValue = Short.valueOf("2");
    Short expectedResult = Short.valueOf("0");

    Validator.testShort(testValue, expectedResult, "%");

  }

  /**
   * @testName: elByteAddTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if both operands in an EL "+" (addition)
   *                 operation are Bytes, the result is coerced to Long and is
   *                 the sum of the operands.
   * 
   *                 Equations tested: Byte + Byte
   */
  @Test
  public void elByteAddTest() throws Exception {

    Byte testValue = Byte.valueOf("2");
    Byte expectedResult = Byte.valueOf("3");

    Validator.testByte(testValue, expectedResult, "+");

  }

  /**
   * @testName: elByteSubtractTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if both operands in an EL "-" (subtraction)
   *                 operation are Bytes, the result is coerced to Long and is
   *                 the difference of the operands.
   * 
   *                 Equations tested: Byte - Byte
   */
  @Test
  public void elByteSubtractTest() throws Exception {

    Byte testValue = Byte.valueOf("2");
    Byte expectedResult = Byte.valueOf("1");

    Validator.testByte(testValue, expectedResult, "-");

  }

  /**
   * @testName: elByteMultiplyTest
   * @assertion_ids: EL:SPEC:17.5
   * @test_Strategy: Validate that if both operands in an EL "*"
   *                 (multiplication) operation are Bytes, the result is coerced
   *                 to Long and is the product of the operands.
   * 
   *                 Equations tested: Byte * Byte
   */
  @Test
  public void elByteMultiplyTest() throws Exception {

    Byte testValue = Byte.valueOf("2");
    Byte expectedResult = Byte.valueOf("2");

    Validator.testByte(testValue, expectedResult, "*");

  }

  /**
   * @testName: elByteDivisionTest
   * @assertion_ids: EL:SPEC:18.3
   * @test_Strategy: Validate that if both operands in an EL "/" (div) operation
   *                 are Bytes, the result is coerced to Double and is the
   *                 quotient of the operands.
   * 
   *                 Equations tested: Byte / Byte
   */
  @Test
  public void elByteDivisionTest() throws Exception {

    Byte testValue = Byte.valueOf("2");
    Byte expectedResult = Byte.valueOf("2");

    Validator.testByte(testValue, expectedResult, "/");

  }

  /**
   * @testName: elByteModulusTest
   * @assertion_ids: EL:SPEC:19.4
   * @test_Strategy: Validate that if both operands in an EL "%" (mod) operation
   *                 are Bytes, the result is coerced to Long and is the
   *                 remainder of the quotient of the operands.
   * 
   *                 Equations tested: Byte % Byte
   */
  @Test
  public void elByteModulusTest() throws Exception {

    Byte testValue = Byte.valueOf("2");
    Byte expectedResult = Byte.valueOf("0");

    Validator.testByte(testValue, expectedResult, "%");

  }

  /**
   * @testName: elBooleanAndTest
   * @assertion_ids: EL:SPEC:23.1; EL:SPEC:24.2.1
   * @test_Strategy: Validate that if one of the operands in an EL "&&", "and"
   *                 operation is a Boolean, the result is coerced to Boolean.
   * 
   *                 Equations tested: Boolean && String Boolean && Boolean
   *                 Boolean and String Boolean and Boolean
   * 
   */
  @Test
  public void elBooleanAndTest() throws Exception {

    Validator.testBoolean(true, "true", true, "&&");
    Validator.testBoolean(true, true, true, "&&");

    Validator.testBoolean(true, "false", false, "and");
    Validator.testBoolean(true, false, false, "and");

  }

  /**
   * @testName: elBooleanOrTest
   * @assertion_ids: EL:SPEC:23.1; EL:SPEC:24.2.1
   * @test_Strategy: Validate that if one of the operands in an EL "||", "or"
   *                 operation is a Boolean, the result is coerced to Boolean.
   * 
   *                 Equations tested: Boolean || String Boolean || Boolean
   *                 Boolean or String Boolean or Boolean
   * 
   */
  @Test
  public void elBooleanOrTest() throws Exception {

    Validator.testBoolean(false, "false", false, "||");
    Validator.testBoolean(true, "false", true, "or");

    Validator.testBoolean(true, false, true, "||");
    Validator.testBoolean(true, true, true, "or");

  }

}
