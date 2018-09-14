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

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.el.common.util.ExprEval;
import com.sun.ts.tests.el.common.util.TestNum;
import com.sun.ts.tests.el.common.util.Validator;

public class ELClient extends ServiceEETest {

  Properties testProps;

  public static void main(String[] args) {
    ELClient theTests = new ELClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
  }

  public void cleanup() throws Fault {
    // does nothing at this point
  }

  /**
   * @testName: elNullOperandAddTest
   * @assertion_ids: EL:SPEC:17.1
   * @test_Strategy: Validate that if both of the operands in an EL "+"
   *                 (addition) operation are null, the result is (Long) 0.
   */
  public void elNullOperandAddTest() throws Fault {

    boolean pass = false;

    Long expectedResult = Long.valueOf("0");
    try {
      String expr = ExprEval.buildElExpr(true, "+");
      TestUtil.logTrace("expression to be evaluated is " + expr);

      Object result = ExprEval.evaluateValueExpression(expr, null,
          Object.class);
      TestUtil.logTrace("result is " + result.toString());
      pass = (ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue((Long) result, expectedResult, 0));

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED: pass = false");
  }

  /**
   * @testName: elNullOperandSubtractTest
   * @assertion_ids: EL:SPEC:17.1
   * @test_Strategy: Validate that if both of the operands in an EL "-"
   *                 (subtraction) operation are null, the result is (Long) 0.
   */
  public void elNullOperandSubtractTest() throws Fault {

    boolean pass = false;

    Long expectedResult = Long.valueOf("0");
    try {
      String expr = ExprEval.buildElExpr(false, "-");
      TestUtil.logTrace("expression to be evaluated is " + expr);

      Object result = ExprEval.evaluateValueExpression(expr, null,
          Object.class);
      TestUtil.logTrace("result is " + result.toString());
      pass = (ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue((Long) result, expectedResult, 0));

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED: pass = false");
  }

  /**
   * @testName: elNullOperandMultiplyTest
   * @assertion_ids: EL:SPEC:17.1
   * @test_Strategy: Validate that if both of the operands in an EL "*"
   *                 (multiplication) operation are null, the result is (Long)
   *                 0.
   */
  public void elNullOperandMultiplyTest() throws Fault {

    boolean pass = false;

    Long expectedResult = Long.valueOf("0");
    try {
      String expr = ExprEval.buildElExpr(true, "*");
      TestUtil.logTrace("expression to be evaluated is " + expr);

      Object result = ExprEval.evaluateValueExpression(expr, null,
          Object.class);
      TestUtil.logTrace("result is " + result.toString());
      pass = (ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue((Long) result, expectedResult, 0));

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED: pass = false");
  }

  /**
   * @testName: elNullOperandDivisionTest
   * @assertion_ids: EL:SPEC:18.1
   * @test_Strategy: Validate that if both of the operands in an EL "/"
   *                 (division) operation are null, the result is (Long) 0.
   * 
   */
  public void elNullOperandDivisionTest() throws Fault {

    boolean pass = false;
    Long expectedResult = Long.valueOf("0");

    try {
      String expr = ExprEval.buildElExpr(true, "/");
      TestUtil.logTrace("expression to be evaluated is " + expr);

      Object result = ExprEval.evaluateValueExpression(expr, null,
          Object.class);
      TestUtil.logTrace("result is " + result.toString());
      pass = (ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue((Long) result, expectedResult, 0));

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED: pass = false");
  }

  /**
   * @testName: elNullOperandModulusTest
   * @assertion_ids: EL:SPEC:19.1
   * @test_Strategy: Validate that if both of the operands in an EL "%" (mod)
   *                 operation are null, the result is (Long) 0.
   * 
   */
  public void elNullOperandModulusTest() throws Fault {

    boolean pass = false;

    Long expectedResult = Long.valueOf("0");
    try {
      String expr = ExprEval.buildElExpr(true, "%");
      TestUtil.logTrace("expression to be evaluated is " + expr);

      Object result = ExprEval.evaluateValueExpression(expr, null,
          Object.class);
      TestUtil.logTrace("result is " + result.toString());
      pass = (ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue((Long) result, expectedResult, 0));

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED: pass = false");
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
  public void elBigDecimalAddTest() throws Fault {

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
  public void elBigDecimalSubtractTest() throws Fault {

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
  public void elBigDecimalMultiplyTest() throws Fault {

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
  public void elBigDecimalDivisionTest() throws Fault {

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
  public void elBigDecimalModulusTest() throws Fault {

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
  public void elBigIntegerAddTest() throws Fault {

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
  public void elBigIntegerSubtractTest() throws Fault {

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
  public void elBigIntegerMultiplyTest() throws Fault {

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
  public void elBigIntegerDivisionTest() throws Fault {

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
  public void elBigIntegerModulusTest() throws Fault {

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
  public void elFloatAddTest() throws Fault {

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
  public void elFloatSubtractTest() throws Fault {

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
  public void elFloatMultiplyTest() throws Fault {

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
  public void elFloatDivisionTest() throws Fault {

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
  public void elFloatModulusTest() throws Fault {

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
  public void elDoubleAddTest() throws Fault {

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
  public void elDoubleSubtractTest() throws Fault {

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
  public void elDoubleMultiplyTest() throws Fault {

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
  public void elDoubleDivisionTest() throws Fault {

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
  public void elDoubleModulusTest() throws Fault {

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
  public void elNumericStringSubtractTest() throws Fault {

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
  public void elNumericStringMultiplyTest() throws Fault {

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
  public void elNumericStringDivisionTest() throws Fault {

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
  public void elNumericStringModulusTest() throws Fault {

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
  public void elLongAddTest() throws Fault {

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
  public void elLongSubtractTest() throws Fault {

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
  public void elLongMultiplyTest() throws Fault {

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
  public void elLongDivisionTest() throws Fault {

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
  public void elLongModulusTest() throws Fault {

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
  public void elIntegerAddTest() throws Fault {

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
  public void elIntegerSubtractTest() throws Fault {

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
  public void elIntegerMultiplyTest() throws Fault {

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
  public void elIntegerDivisionTest() throws Fault {

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
  public void elIntegerModulusTest() throws Fault {

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
  public void elShortAddTest() throws Fault {

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
  public void elShortSubtractTest() throws Fault {

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
  public void elShortMultiplyTest() throws Fault {

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
  public void elShortDivisionTest() throws Fault {

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
  public void elShortModulusTest() throws Fault {

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
  public void elByteAddTest() throws Fault {

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
  public void elByteSubtractTest() throws Fault {

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
  public void elByteMultiplyTest() throws Fault {

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
  public void elByteDivisionTest() throws Fault {

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
  public void elByteModulusTest() throws Fault {

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
  public void elBooleanAndTest() throws Fault {

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
  public void elBooleanOrTest() throws Fault {

    Validator.testBoolean(false, "false", false, "||");
    Validator.testBoolean(true, "false", true, "or");

    Validator.testBoolean(true, false, true, "||");
    Validator.testBoolean(true, true, true, "or");

  }

}
