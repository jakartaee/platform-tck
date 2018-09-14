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

/**
 * $Id$
 */

package com.sun.ts.tests.el.spec.relationaloperator;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.el.common.util.ExprEval;
import com.sun.ts.tests.el.common.util.NameValuePair;
import com.sun.ts.tests.el.common.util.TestNum;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Properties;

public class ELClient extends ServiceEETest {

  Properties testProps;

  private List numberList;

  private enum TestEnum {
    APPLE, PEAR
  };

  // Data Type to test String Coercion.
  private static final DougType DT = new DougType();

  private static final NickType NT = new NickType();

  public static void main(String[] args) {
    ELClient theTests = new ELClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
    numberList = TestNum.getNumberList();
  }

  public void cleanup() throws Fault {
    // does nothing at this point
  }

  /**
   * @testName: elEqualOperandLessThanOrEqualTest
   * @assertion_ids: EL:SPEC:21.1
   * @test_Strategy: Validate that if the operands in an EL <= or le operation
   *                 are equal, the result is true.
   */
  public void elEqualOperandLessThanOrEqualTest() throws Fault {

    boolean pass = false;

    try {
      // Expression One
      String expr1 = ExprEval.buildElExpr(false, "<=");
      TestUtil.logTrace("first expression to be evaluated is " + expr1);

      NameValuePair values1[] = NameValuePair
          .buildNameValuePair(new Float(-1.0f), new Float(-1.0));

      Object result1 = ExprEval.evaluateValueExpression(expr1, values1,
          Boolean.class);
      TestUtil.logTrace("first result is " + result1.toString());

      // Expression Two
      String expr2 = ExprEval.buildElExpr(true, "le");
      TestUtil.logTrace("second expression to be evaluated is " + expr2);

      NameValuePair values2[] = NameValuePair
          .buildNameValuePair(new BigDecimal("1.0"), BigDecimal.ONE);
      Object result2 = ExprEval.evaluateValueExpression(expr2, values2,
          Boolean.class);

      TestUtil.logTrace("second result is " + result2.toString());

      pass = (ExprEval.compareClass(result1, Boolean.class)
          && ExprEval.compareValue((Boolean) result1, Boolean.TRUE)
          && ExprEval.compareClass(result2, Boolean.class)
          && ExprEval.compareValue((Boolean) result2, Boolean.TRUE));

    } catch (Exception e) {
      throw new Fault(e);
    } finally {
      ExprEval.cleanup();
    }

    if (!pass)
      throw new Fault("TEST FAILED: pass = false");
  }

  /**
   * @testName: elEqualOperandGreaterThanOrEqualTest
   * @assertion_ids: EL:SPEC:21.1
   * @test_Strategy: Validate that if the operands in an EL >= or ge operation
   *                 are equal, the result is true.
   */
  public void elEqualOperandGreaterThanOrEqualTest() throws Fault {

    boolean pass = false;

    try {
      // Expression One
      String expr1 = ExprEval.buildElExpr(false, ">=");
      TestUtil.logTrace("first expression to be evaluated is " + expr1);

      NameValuePair values1[] = NameValuePair
          .buildNameValuePair(new Float(-1.0f), new Float(-1.0));

      Object result1 = ExprEval.evaluateValueExpression(expr1, values1,
          Boolean.class);
      TestUtil.logTrace("first result is " + result1.toString());

      // Expression Two
      String expr2 = ExprEval.buildElExpr(true, "ge");
      TestUtil.logTrace("second expression to be evaluated is " + expr2);

      NameValuePair values2[] = NameValuePair
          .buildNameValuePair(new BigInteger("1010"), BigInteger.TEN);

      Object result2 = ExprEval.evaluateValueExpression(expr2, values2,
          Boolean.class);
      TestUtil.logTrace("second result is " + result2.toString());

      pass = (ExprEval.compareClass(result1, Boolean.class)
          && ExprEval.compareValue((Boolean) result1, Boolean.TRUE)
          && ExprEval.compareClass(result2, Boolean.class)
          && ExprEval.compareValue((Boolean) result2, Boolean.TRUE));

    } catch (Exception e) {
      throw new Fault(e);
    } finally {
      ExprEval.cleanup();
    }

    if (!pass)
      throw new Fault("TEST FAILED: pass = false");
  }

  /**
   * @testName: elNullOperandLessThanOrEqualTest
   * @assertion_ids: EL:SPEC:21.2
   * @test_Strategy: Validate that if one of the operands in an EL <= or le
   *                 operation is null, the result is false.
   */
  public void elNullOperandLessThanOrEqualTest() throws Fault {

    boolean pass = false;

    try {

      Object result1 = ExprEval.evaluateValueExpression("${1 <= nullValue}",
          null, Object.class);
      TestUtil.logTrace("first result is " + result1.toString());

      Object result2 = ExprEval.evaluateValueExpression("#{2 le nullValue}",
          null, Object.class);
      TestUtil.logTrace("second result is " + result2.toString());

      pass = (ExprEval.compareClass(result1, Boolean.class)
          && ExprEval.compareValue((Boolean) result1, Boolean.FALSE)
          && ExprEval.compareClass(result2, Boolean.class)
          && ExprEval.compareValue((Boolean) result2, Boolean.FALSE));

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED: pass = false");
  }

  /**
   * @testName: elNullOperandGreaterThanOrEqualTest
   * @assertion_ids: EL:SPEC:21.2
   * @test_Strategy: Validate that if one of the operands in an EL >= or ge
   *                 operation is null, the result is false.
   */
  public void elNullOperandGreaterThanOrEqualTest() throws Fault {

    boolean pass = false;

    try {

      Object result1 = ExprEval.evaluateValueExpression("${1 >= nullValue}",
          null, Object.class);
      TestUtil.logTrace("first result is " + result1.toString());

      Object result2 = ExprEval.evaluateValueExpression("#{2 ge nullValue}",
          null, Object.class);
      TestUtil.logTrace("second result is " + result2.toString());

      pass = (ExprEval.compareClass(result1, Boolean.class)
          && ExprEval.compareValue((Boolean) result1, Boolean.FALSE)
          && ExprEval.compareClass(result2, Boolean.class)
          && ExprEval.compareValue((Boolean) result2, Boolean.FALSE));

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED: pass = false");
  }

  /**
   * @testName: elNullOperandNotEqualTest
   * @assertion_ids: EL:SPEC:22.2
   * @test_Strategy: Validate that if one of the operands is null in an EL !=,
   *                 ne operation return true.
   */
  public void elNullOperandNotEqualTest() throws Fault {

    boolean pass = false;

    try {

      Object result1 = ExprEval.evaluateValueExpression("${1 != nullValue}",
          null, Object.class);
      TestUtil.logTrace("first result is " + result1.toString());

      Object result2 = ExprEval.evaluateValueExpression("#{2 ne nullValue}",
          null, Object.class);
      TestUtil.logTrace("second result is " + result2.toString());

      pass = (ExprEval.compareClass(result1, Boolean.class)
          && ExprEval.compareValue((Boolean) result1, Boolean.TRUE)
          && ExprEval.compareClass(result2, Boolean.class)
          && ExprEval.compareValue((Boolean) result2, Boolean.TRUE));

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED: pass = false");

  } // End elNullOperandNotEqualTest

  /**
   * @testName: elNullOperandEqualTest
   * @assertion_ids: EL:SPEC:22.2
   * @test_Strategy: Validate that if one of the operands is null in an EL =, eq
   *                 operation return false.
   */
  public void elNullOperandEqualTest() throws Fault {

    boolean pass = false;

    try {

      Object result1 = ExprEval.evaluateValueExpression("${1 == nullValue}",
          null, Object.class);
      TestUtil.logTrace("first result is " + result1.toString());

      Object result2 = ExprEval.evaluateValueExpression("#{2 eq nullValue}",
          null, Object.class);
      TestUtil.logTrace("second result is " + result2.toString());

      pass = (ExprEval.compareClass(result1, Boolean.class)
          && ExprEval.compareValue((Boolean) result1, Boolean.FALSE)
          && ExprEval.compareClass(result2, Boolean.class)
          && ExprEval.compareValue((Boolean) result2, Boolean.FALSE));

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED: pass = false");

  } // End elNullOperandEqualTest

  /**
   * @testName: elBigDecimalLessThanTest
   * @assertion_ids: EL:SPEC:21.3
   * @test_Strategy: Validate that if one of the operands in an EL "<" or "lt"
   *                 operation is a BigDecimal, the result is coerced to
   *                 BigDecimal and the correct boolean value is returned.
   * 
   *                 Equations tested: BigDecimal "<" & "lt" BigDecimal
   *                 BigDecimal "<" & "lt" Double BigDecimal "<" & "lt" Float
   *                 BigDecimal "<" & "lt" BigInteger BigDecimal "<" & "lt"
   *                 Integer BigDecimal "<" & "lt" Long BigDecimal "<" & "lt"
   *                 Short BigDecimal "<" & "lt" Byte
   */
  public void elBigDecimalLessThanTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(10.531), false, "<");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(1.0000), false, "lt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(0.531), true, "lt");

  }

  /**
   * @testName: elBigDecimalLessThanEqualTest
   * @assertion_ids: EL:SPEC:21.1; EL:SPEC:21.3
   * @test_Strategy: Validate that if one of the operands in an EL "<=" or "le"
   *                 operation is a BigDecimal, the result is coerced to
   *                 BigDecimal and the correct boolean value is returned.
   * 
   *                 Equations tested: BigDecimal "<=" & "le" BigDecimal
   *                 BigDecimal "<=" & "le" Double BigDecimal "<=" & "le" Float
   *                 BigDecimal "<=" & "le" BigInteger BigDecimal "<=" & "le"
   *                 Integer BigDecimal "<=" & "le" Long BigDecimal "<=" & "le"
   *                 Short BigDecimal "<=" & "le" Byte
   */
  public void elBigDecimalLessThanEqualTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(10.531), false, "<=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(1.0000), true, "le");

    // Value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(-10.531), true, "<=");
  }

  /**
   * @testName: elBigDecimalGreaterThanTest
   * @assertion_ids: EL:SPEC:21.3
   * @test_Strategy: Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is a BigDecimal, the result is coerced to
   *                 BigDecimal and the correct boolean value is returned.
   * 
   *                 Equations tested: BigDecimal ">" & "gt" BigDecimal
   *                 BigDecimal ">" & "gt" Double BigDecimal ">" & "gt" Float
   *                 BigDecimal ">" & "gt" BigInteger BigDecimal ">" & "gt"
   *                 Integer BigDecimal ">" & "gt" Long BigDecimal ">" & "gt"
   *                 Short BigDecimal ">" & "gt" Byte
   */
  public void elBigDecimalGreaterThanTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(10.531), true, ">");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(1.0000), false, ">");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(0.531), false, "gt");

  }

  /**
   * @testName: elBigDecimalGreaterThanEqualTest
   * @assertion_ids: EL:SPEC:21.1; EL:SPEC:21.3
   * @test_Strategy: Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is a BigDecimal, the result is coerced to
   *                 BigDecimal and the correct boolean value is returned.
   * 
   *                 Equations tested: BigDecimal ">=" & "ge" BigDecimal
   *                 BigDecimal ">=" & "ge" Double BigDecimal ">=" & "ge" Float
   *                 BigDecimal ">=" & "ge" BigInteger BigDecimal ">=" & "ge"
   *                 Integer BigDecimal ">=" & "ge" Long BigDecimal ">=" & "ge"
   *                 Short BigDecimal ">=" & "ge" Byte
   */
  public void elBigDecimalGreaterThanEqualTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(10.531), true, ">=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(1.0000), true, "ge");

    // value passed in is smaller than the COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(-1.0000), false, "ge");

  }

  /**
   * @testName: elBigDecimalEqualToTest
   * @assertion_ids: EL:SPEC:22.1; EL:SPEC:22.3.1
   * @test_Strategy: Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is a BigDecimal, the result is coerced to
   *                 BigDecimal and the correct boolean value is returned.
   * 
   *                 Equations tested: BigDecimal "==" & "eq" BigDecimal
   *                 BigDecimal "==" & "eq" Double BigDecimal "==" & "eq" Float
   *                 BigDecimal "==" & "eq" BigInteger BigDecimal "==" & "eq"
   *                 Integer BigDecimal "==" & "eq" Long BigDecimal "==" & "eq"
   *                 Short BigDecimal "==" & "eq" Byte
   */
  public void elBigDecimalEqualToTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(10.531), false, "==");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(1), true, "eq");

  }

  /**
   * @testName: elBigDecimalNotEqualToTest
   * @assertion_ids: EL:SPEC:22.3.2
   * @test_Strategy: Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is a BigDecimal, the result is coerced to
   *                 BigDecimal and the correct boolean value is returned.
   * 
   *                 Equations tested: BigDecimal "!=" & "ne" BigDecimal
   *                 BigDecimal "!=" & "ne" Double BigDecimal "!=" & "ne" Float
   *                 BigDecimal "!=" & "ne" BigInteger BigDecimal "!=" & "ne"
   *                 Integer BigDecimal "!=" & "ne" Long BigDecimal "!=" & "ne"
   *                 Short BigDecimal "!=" & "ne" Byte
   */
  public void elBigDecimalNotEqualToTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(10.531), true, "!=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigDecimal.valueOf(1), false, "ne");

  }

  /**
   * @testName: elFloatLessThanTest
   * @assertion_ids: EL:SPEC:21.4
   * @test_Strategy: Validate that if one of the operands in an EL "<" or "lt"
   *                 operation is a Float, the result is coerced to Double and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Float "<" & "lt" Double Float "<" & "lt"
   *                 Float Float "<" & "lt" BigInteger Float "<" & "lt" Integer
   *                 Float "<" & "lt" Long Float "<" & "lt" Short Float "<" &
   *                 "lt" Byte
   */
  public void elFloatLessThanTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(10f), false, "<");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(1f), false, "<");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(-10f), true, "lt");

  }

  /**
   * @testName: elFloatLessThanEqualTest
   * @assertion_ids: EL:SPEC:21.1; EL:SPEC:21.4
   * @test_Strategy: Validate that if one of the operands in an EL "<=" or "le"
   *                 operation is a Float, the result is coerced to Double and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Float "<=" & "le" Double Float "<=" &
   *                 "le" Float Float "<=" & "le" BigInteger Float "<=" & "le"
   *                 Integer Float "<=" & "le" Long Float "<=" & "le" Short
   *                 Float "<=" & "le" Byte
   */
  public void elFloatLessThanEqualTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(10f), false, "<=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(1f), true, "le");

    // Value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(-10f), true, "<=");
  }

  /**
   * @testName: elFloatGreaterThanTest
   * @assertion_ids: EL:SPEC:21.4
   * @test_Strategy: Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is a Float, the result is coerced to Double and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Float ">" & "gt" Double Float ">" & "gt"
   *                 Float Float ">" & "gt" BigInteger Float ">" & "gt" Integer
   *                 Float ">" & "gt" Long Float ">" & "gt" Short Float ">" &
   *                 "gt" Byte
   */
  public void elFloatGreaterThanTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(10531f), true, ">");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(1f), false, ">");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(-531f), false, "gt");

  }

  /**
   * @testName: elFloatGreaterThanEqualTest
   * @assertion_ids: EL:SPEC:21.1; EL:SPEC:21.4
   * @test_Strategy: Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is a Float, the result is coerced to Double and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Float ">=" & "ge" Double Float ">=" &
   *                 "ge" Float Float ">=" & "ge" BigInteger Float ">=" & "ge"
   *                 Integer Float ">=" & "ge" Long Float ">=" & "ge" Short
   *                 Float ">=" & "ge" Byte
   */
  public void elFloatGreaterThanEqualTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(10531f), true, ">=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(1f), true, "ge");

    // value passed in is smaller than the COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(-1f), false, "ge");

  }

  /**
   * @testName: elFloatEqualToTest
   * @assertion_ids: EL:SPEC:22.1; EL:SPEC:22.4
   * @test_Strategy: Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is a Float, the result is coerced to Double and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Float "==" & "eq" Double Float "==" &
   *                 "eq" Float Float "==" & "eq" BigInteger Float "==" & "eq"
   *                 Integer Float "==" & "eq" Long Float "==" & "eq" Short
   *                 Float "==" & "eq" Byte
   */
  public void elFloatEqualToTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(10531), false, "==");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(1), true, "eq");

  }

  /**
   * @testName: elFloatNotEqualToTest
   * @assertion_ids: EL:SPEC:22.4
   * @test_Strategy: Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is a Float, the result is coerced to Double and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Float "!=" & "ne" Double Float "!=" &
   *                 "ne" Float Float "!=" & "ne" BigInteger Float "!=" & "ne"
   *                 Integer Float "!=" & "ne" Long Float "!=" & "ne" Short
   *                 Float "!=" & "ne" Byte
   */
  public void elFloatNotEqualToTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(10531), true, "!=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Float.valueOf(1), false, "ne");

  }

  /**
   * @testName: elDoubleLessThanTest
   * @assertion_ids: EL:SPEC:21.4
   * @test_Strategy: Validate that if one of the operands in an EL "<" or "lt"
   *                 operation is a Double, the result is coerced to Double and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Double "<" & "lt" Double Double "<" &
   *                 "lt" BigInteger Double "<" & "lt" Integer Double "<" & "lt"
   *                 Long Double "<" & "lt" Short Double "<" & "lt" Byte
   */
  public void elDoubleLessThanTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(2.5), false, "<");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(1.0), false, "lt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(-2.5), true, "lt");

  }

  /**
   * @testName: elDoubleLessThanEqualTest
   * @assertion_ids: EL:SPEC:21.1; EL:SPEC:21.4
   * @test_Strategy: Validate that if one of the operands in an EL "<=" or "le"
   *                 operation is a Double, the result is coerced to Double and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Double "<=" & "le" Double Double "<=" &
   *                 "le" BigInteger Double "<=" & "le" Integer Double "<=" &
   *                 "le" Long Double "<=" & "le" Short Double "<=" & "le" Byte
   */
  public void elDoubleLessThanEqualTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(2.5), false, "<=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(1.0), true, "le");

    // Value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(-1.5), true, "<=");
  }

  /**
   * @testName: elDoubleGreaterThanTest
   * @assertion_ids: EL:SPEC:21.4
   * @test_Strategy: Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is a Double, the result is coerced to Double and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Double ">" & "gt" Double Double ">" &
   *                 "gt" BigInteger Double ">" & "gt" Integer Double ">" & "gt"
   *                 Long Double ">" & "gt" Short Double ">" & "gt" Byte
   */
  public void elDoubleGreaterThanTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(10.5), true, ">");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(1.0), false, "gt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(-10.5), false, "gt");

  }

  /**
   * @testName: elDoubleGreaterThanEqualTest
   * @assertion_ids: EL:SPEC:21.1; EL:SPEC:21.4
   * @test_Strategy: Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is a Double, the result is coerced to Double and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Double ">=" & "ge" Double Double ">=" &
   *                 "ge" BigInteger Double ">=" & "ge" Integer Double ">=" &
   *                 "ge" Long Double ">=" & "ge" Short Double ">=" & "ge" Byte
   */
  public void elDoubleGreaterThanEqualTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(10.0), true, ">=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(1.0), true, "ge");

    // value passed in is smaller than the COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(-10.0), false, "ge");

  }

  /**
   * @testName: elDoubleEqualToTest
   * @assertion_ids: EL:SPEC:22.1; EL:SPEC:22.4
   * @test_Strategy: Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is a Double, the result is coerced to Double and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Double "==" & "eq" Double Double "==" &
   *                 "eq" BigInteger Double "==" & "eq" Integer Double "==" &
   *                 "eq" Long Double "==" & "eq" Short Double "==" & "eq" Byte
   */
  public void elDoubleEqualToTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(10531), false, "==");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(1.00), true, "eq");

  }

  /**
   * @testName: elDoubleNotEqualToTest
   * @assertion_ids: EL:SPEC:22.4
   * @test_Strategy: Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is a Double, the result is coerced to Double and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Double "!=" & "ne" Double Double "!=" &
   *                 "ne" BigInteger Double "!=" & "ne" Integer Double "!=" &
   *                 "ne" Long Double "!=" & "ne" Short Double "!=" & "ne" Byte
   */
  public void elDoubleNotEqualToTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(10531), true, "!=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Double.valueOf(1), false, "ne");

  }

  /**
   * @testName: elBigIntegerLessThanTest
   * @assertion_ids: EL:SPEC:21.5
   * @test_Strategy: Validate that if one of the operands in an EL "<" or "lt"
   *                 operation is a BigInteger, the result is coerced to
   *                 BigInteger and the correct boolean value is returned.
   * 
   *                 Equations tested: BigInteger "<" & "lt" BigInteger
   *                 BigInteger "<" & "lt" Integer BigInteger "<" & "lt" Long
   *                 BigInteger "<" & "lt" Short BigInteger "<" & "lt" Byte
   */
  public void elBigIntegerLessThanTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(10531), false, "<");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(1), false, "lt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(-10531), true, "lt");

  }

  /**
   * @testName: elBigIntegerLessThanEqualTest
   * @assertion_ids: EL:SPEC:21.1; EL:SPEC:21.5
   * @test_Strategy: Validate that if one of the operands in an EL "<=" or "le"
   *                 operation is a BigInteger, the result is coerced to
   *                 BigInteger and the correct boolean value is returned.
   * 
   *                 Equations tested: BigInteger "<=" & "le" BigInteger
   *                 BigInteger "<=" & "le" Integer BigInteger "<=" & "le" Long
   *                 BigInteger "<=" & "le" Short BigInteger "<=" & "le" Byte
   */
  public void elBigIntegerLessThanEqualTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(10531), false, "<=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(1), true, "le");

    // Value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(-10531), true, "<=");
  }

  /**
   * @testName: elBigIntegerGreaterThanTest
   * @assertion_ids: EL:SPEC:21.5
   * @test_Strategy: Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is a BigInteger, the result is coerced to
   *                 BigInteger and the correct boolean value is returned.
   * 
   *                 Equations tested: BigInteger ">" & "gt" BigInteger
   *                 BigInteger ">" & "gt" Integer BigInteger ">" & "gt" Long
   *                 BigInteger ">" & "gt" Short BigInteger ">" & "gt" Byte
   */
  public void elBigIntegerGreaterThanTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(10531), true, ">");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(1), false, "gt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(-10531), false, "gt");

  }

  /**
   * @testName: elBigIntegerGreaterThanEqualTest
   * @assertion_ids: EL:SPEC:21.1; EL:SPEC:21.5
   * @test_Strategy: Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is a BigInteger, the result is coerced to
   *                 BigInteger and the correct boolean value is returned.
   * 
   *                 Equations tested: BigInteger ">=" & "ge" BigInteger
   *                 BigInteger ">=" & "ge" Integer BigInteger ">=" & "ge" Long
   *                 BigInteger ">=" & "ge" Short BigInteger ">=" & "ge" Byte
   */
  public void elBigIntegerGreaterThanEqualTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(10531), true, ">=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(1), true, "ge");

    // value passed in is smaller than the COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(-10531), false, "ge");

  }

  /**
   * @testName: elBigIntegerEqualToTest
   * @assertion_ids: EL:SPEC:22.1; EL:SPEC:22.5.1
   * @test_Strategy: Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is a BigInteger, the result is coerced to
   *                 BigInteger and the correct boolean value is returned.
   * 
   *                 Equations tested: BigInteger "==" & "eq" BigInteger
   *                 BigInteger "==" & "eq" Integer BigInteger "==" & "eq" Long
   *                 BigInteger "==" & "eq" Short BigInteger "==" & "eq" Byte
   */
  public void elBigIntegerEqualToTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(10531), false, "==");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(1), true, "eq");

  }

  /**
   * @testName: elBigIntegerNotEqualToTest
   * @assertion_ids: EL:SPEC:22.5.2
   * @test_Strategy: Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is a BigInteger, the result is coerced to
   *                 BigInteger and the correct boolean value is returned.
   * 
   *                 Equations tested: BigInteger "!=" & "ne" BigInteger
   *                 BigInteger "!=" & "ne" Integer BigInteger "!=" & "ne" Long
   *                 BigInteger "!=" & "ne" Short BigInteger "!=" & "ne" Byte
   */
  public void elBigIntegerNotEqualToTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(10531), true, "!=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(BigInteger.valueOf(1), false, "ne");

  }

  /**
   * @testName: elLongLessThanTest
   * @assertion_ids: EL:SPEC:21.6
   * @test_Strategy: Validate that if one of the operands in an EL "<" or "lt"
   *                 operation is a Long, the result is coerced to Long and the
   *                 correct boolean value is returned.
   * 
   *                 Equations tested: Long "<" & "lt" Integer Long "<" & "lt"
   *                 Long Long "<" & "lt" Short Long "<" & "lt" Byte
   */
  public void elLongLessThanTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(25000), false, "<");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(1), false, "<");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(-25000), true, "lt");

  }

  /**
   * @testName: elLongLessThanEqualTest
   * @assertion_ids: EL:SPEC:21.1; EL:SPEC:21.6
   * @test_Strategy: Validate that if one of the operands in an EL "<=" or "le"
   *                 operation is a Long, the result is coerced to Long and the
   *                 correct boolean value is returned.
   * 
   *                 Equations tested: Long "<=" & "le" Integer Long "<=" & "le"
   *                 Long Long "<=" & "le" Short Long "<=" & "le" Byte
   */
  public void elLongLessThanEqualTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(25000), false, "<=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(1), true, "le");

    // Value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(-25000), true, "<=");
  }

  /**
   * @testName: elLongGreaterThanTest
   * @assertion_ids: EL:SPEC:21.6
   * @test_Strategy: Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is a Long, the result is coerced to Long and the
   *                 correct boolean value is returned.
   * 
   *                 Equations tested: Long ">" & "gt" Integer Long ">" & "gt"
   *                 Long Long ">" & "gt" Short Long ">" & "gt" Byte
   */
  public void elLongGreaterThanTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(10531), true, ">");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(1), false, ">");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(-10531), false, "gt");

  }

  /**
   * @testName: elLongGreaterThanEqualTest
   * @assertion_ids: EL:SPEC:21.1; EL:SPEC:21.6
   * @test_Strategy: Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is a Long, the result is coerced to Long and the
   *                 correct boolean value is returned.
   * 
   *                 Equations tested: Long ">=" & "ge" Integer Long ">=" & "ge"
   *                 Long Long ">=" & "ge" Short Long ">=" & "ge" Byte
   */
  public void elLongGreaterThanEqualTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(25000), true, ">=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(1), true, "ge");

    // value passed in is smaller than the COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(-25000), false, "ge");

  }

  /**
   * @testName: elLongEqualToTest
   * @assertion_ids: EL:SPEC:22.1; EL:SPEC:22.6
   * @test_Strategy: Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is a Long, the result is coerced to Long and the
   *                 correct boolean value is returned.
   * 
   *                 Equations tested: Long "==" & "eq" Integer Long "==" & "eq"
   *                 Long Long "==" & "eq" Short Long "==" & "eq" Byte
   */
  public void elLongEqualToTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(25000), false, "==");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(1), true, "eq");

  }

  /**
   * @testName: elLongNotEqualToTest
   * @assertion_ids: EL:SPEC:22.6
   * @test_Strategy: Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is a Long, the result is coerced to Long and the
   *                 correct boolean value is returned.
   * 
   *                 Equations tested: Long "!=" & "ne" Integer Long "!=" & "ne"
   *                 Long Long "!=" & "ne" Short Long "!=" & "ne" Byte
   */
  public void elLongNotEqualToTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(25000), true, "!=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Long.valueOf(1), false, "ne");

  }

  /**
   * @testName: elIntegerLessThanTest
   * @assertion_ids: EL:SPEC:21.6
   * @test_Strategy: Validate that if one of the operands in an EL "<" or "lt"
   *                 operation is an Integer, the result is coerced to Long and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Integer "<" & "lt" Integer Integer "<" &
   *                 "lt" Short Integer "<" & "lt" Byte
   */
  public void elIntegerLessThanTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(25), false, "<");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(1), false, "lt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(-25), true, "lt");

  }

  /**
   * @testName: elIntegerLessThanEqualTest
   * @assertion_ids: EL:SPEC:21.1; EL:SPEC:21.6
   * @test_Strategy: Validate that if one of the operands in an EL "<=" or "le"
   *                 operation is an Integer, the result is coerced to Long and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Integer "<=" & "le" Integer Integer "<="
   *                 & "le" Short Integer "<=" & "le" Byte
   */
  public void elIntegerLessThanEqualTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(25), false, "<=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(1), true, "le");

    // Value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(-25), true, "<=");
  }

  /**
   * @testName: elIntegerGreaterThanTest
   * @assertion_ids: EL:SPEC:21.6
   * @test_Strategy: Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is an Integer, the result is coerced to Long and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Integer ">" & "gt" Integer Integer ">" &
   *                 "gt" Short Integer ">" & "gt" Byte
   */
  public void elIntegerGreaterThanTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(105), true, ">");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(1), false, "gt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(-105), false, "gt");

  }

  /**
   * @testName: elIntegerGreaterThanEqualTest
   * @assertion_ids: EL:SPEC:21.1; EL:SPEC:21.6
   * @test_Strategy: Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is an Integer, the result is coerced to Long and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Integer ">=" & "ge" Integer Integer ">="
   *                 & "ge" Short Integer ">=" & "ge" Byte
   */
  public void elIntegerGreaterThanEqualTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(250), true, ">=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(1), true, "ge");

    // value passed in is smaller than the COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(-250), false, "ge");

  }

  /**
   * @testName: elIntegerEqualToTest
   * @assertion_ids: EL:SPEC:22.1; EL:SPEC:22.6
   * @test_Strategy: Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is an Integer, the result is coerced to Long and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Integer "==" & "eq" Integer Integer "=="
   *                 & "eq" Short Integer "==" & "eq" Byte
   */
  public void elIntegerEqualToTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(25), false, "==");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(1), true, "eq");

  }

  /**
   * @testName: elIntegerNotEqualToTest
   * @assertion_ids: EL:SPEC:22.6
   * @test_Strategy: Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is an Integer, the result is coerced to Long and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Integer "!=" & "ne" Integer Integer "!="
   *                 & "ne" Short Integer "!=" & "ne" Byte
   */
  public void elIntegerNotEqualToTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(25), true, "!=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Integer.valueOf(1), false, "ne");

  }

  /**
   * @testName: elShortLessThanTest
   * @assertion_ids: EL:SPEC:21.6
   * @test_Strategy: Validate that if one of the operands in an EL "<" or "lt"
   *                 operation is an Short, the result is coerced to Long and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Short "<" & "lt" Short Short "<" & "lt"
   *                 Byte
   */
  public void elShortLessThanTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("2"), false, "<");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("1"), false, "<");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("-2"), true, "lt");

  }

  /**
   * @testName: elShortLessThanEqualTest
   * @assertion_ids: EL:SPEC:21.1; EL:SPEC:21.6
   * @test_Strategy: Validate that if one of the operands in an EL "<=" or "le"
   *                 operation is an Short, the result is coerced to Long and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Short "<=" & "le" Short Short "<=" & "le"
   *                 Byte
   */
  public void elShortLessThanEqualTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("2"), false, "<=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("1"), true, "le");

    // Value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("-2"), true, "<=");
  }

  /**
   * @testName: elShortGreaterThanTest
   * @assertion_ids: EL:SPEC:21.6
   * @test_Strategy: Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is an Short, the result is coerced to Long and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Short ">" & "gt" Short Short ">" & "gt"
   *                 Byte
   */
  public void elShortGreaterThanTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("2"), true, ">");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("1"), false, ">");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("-2"), false, "gt");

  }

  /**
   * @testName: elShortGreaterThanEqualTest
   * @assertion_ids: EL:SPEC:21.1; EL:SPEC:21.6
   * @test_Strategy: Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is an Short, the result is coerced to Long and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Short ">=" & "ge" Short Short ">=" & "ge"
   *                 Byte
   */
  public void elShortGreaterThanEqualTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("2"), true, ">=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("1"), true, "ge");

    // value passed in is smaller than the COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("-2"), false, "ge");

  }

  /**
   * @testName: elShortEqualToTest
   * @assertion_ids: EL:SPEC:22.1; EL:SPEC:22.6
   * @test_Strategy: Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is an Short, the result is coerced to Long and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Short "==" & "eq" Short Short "==" & "eq"
   *                 Byte
   */
  public void elShortEqualToTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("2"), false, "==");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("1"), true, "eq");

  }

  /**
   * @testName: elShortNotEqualToTest
   * @assertion_ids: EL:SPEC:22.6
   * @test_Strategy: Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is an Short, the result is coerced to Long and
   *                 the correct boolean value is returned.
   * 
   *                 Equations tested: Short "!=" & "ne" Short Short "!=" & "ne"
   *                 Byte
   */
  public void elShortNotEqualToTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("2"), true, "!=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Short.valueOf("1"), false, "ne");

  }

  /**
   * @testName: elByteLessThanTest
   * @assertion_ids: EL:SPEC:21.6
   * @test_Strategy: Validate that if one of the operands in an EL "<" or "lt"
   *                 operation is an Byte, the result is coerced to Long and the
   *                 correct boolean value is returned.
   * 
   *                 Equations tested: Byte "<" & "lt" Byte
   */
  public void elByteLessThanTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("2"), false, "<");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("1"), false, "lt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("-2"), true, "lt");

  }

  /**
   * @testName: elByteLessThanEqualTest
   * @assertion_ids: EL:SPEC:21.1; EL:SPEC:21.6
   * @test_Strategy: Validate that if one of the operands in an EL "<=" or "le"
   *                 operation is an Byte, the result is coerced to Long and the
   *                 correct boolean value is returned.
   * 
   *                 Equations tested: Byte "<=" & "le" Byte
   */
  public void elByteLessThanEqualTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("2"), false, "<=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("1"), true, "le");

    // Value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("-2"), true, "<=");
  }

  /**
   * @testName: elByteGreaterThanTest
   * @assertion_ids: EL:SPEC:21.1
   * @test_Strategy: Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is an Byte, the result is coerced to Long and the
   *                 correct boolean value is returned.
   * 
   *                 Equations tested: Byte ">" & "gt" Byte
   */
  public void elByteGreaterThanTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("2"), true, ">");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("1"), false, "gt");

    // value passed in is smaller than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("-2"), false, "gt");

  }

  /**
   * @testName: elByteGreaterThanEqualTest
   * @assertion_ids: EL:SPEC:21.1; EL:SPEC:21.6
   * @test_Strategy: Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is an Byte, the result is coerced to Long and the
   *                 correct boolean value is returned.
   * 
   *                 Equations tested: Byte ">=" & "ge" Byte
   */
  public void elByteGreaterThanEqualTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("2"), true, ">=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("1"), true, "ge");

    // value passed in is smaller than the COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("-2"), false, "ge");

  }

  /**
   * @testName: elByteEqualToTest
   * @assertion_ids: EL:SPEC:22.1; EL:SPEC:22.6
   * @test_Strategy: Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is an Byte, the result is coerced to Long and the
   *                 correct boolean value is returned.
   * 
   *                 Equations tested: Byte "==" & "eq" Byte
   */
  public void elByteEqualToTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("2"), false, "==");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("1"), true, "eq");

  }

  /**
   * @testName: elByteNotEqualToTest
   * @assertion_ids: EL:SPEC:22.6
   * @test_Strategy: Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is an Byte, the result is coerced to Long and the
   *                 correct boolean value is returned.
   * 
   *                 Equations tested: Byte "!=" & "ne" Byte
   */
  public void elByteNotEqualToTest() throws Fault {

    // Value passed in is larger than COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("2"), true, "!=");

    // value passed in is equal to the COMPARATOR.
    this.testOperatorBoolean(Byte.valueOf("1"), false, "ne");

  }

  /**
   * @testName: elStringLessThanTest
   * @assertion_ids: EL:SPEC:21.7
   * @test_Strategy: Validate that if one of the operands in an EL "<" or "lt"
   *                 operation is an String, that both operands are coerced to
   *                 type String and the correct boolean value is returned.
   * 
   *                 Equations tested: String "<" & "lt" String
   */
  public void elStringLessThanTest() throws Fault {

    // Value A is less than value B. (true)
    this.testOperatorBoolean("Alpha", DT, true, "<");

    // Value A is less than value B. (false)
    this.testOperatorBoolean("Beta", DT, false, "lt");

    // Value A is less than value B. (true)
    this.testOperatorBoolean("Gamma", DT, false, "lt");

  }

  /**
   * @testName: elStringLessThanEqualTest
   * @assertion_ids: EL:SPEC:21.7
   * @test_Strategy: Validate that if one of the operands in an EL "<=" or "le"
   *                 operation is a String, that both operands are coerced to
   *                 type String and the correct boolean value is returned.
   * 
   *                 Equations tested: String "<=" & "le" String
   */
  public void elStringLessThanEqualTest() throws Fault {

    // Value A is less than or equal to value B. (false)
    this.testOperatorBoolean("Gamma", DT, false, "<=");

    // Value A is less than or equal to value B. (true)
    this.testOperatorBoolean("Beta", DT, true, "le");

    // Value A is less than or equal to value B. (true)
    this.testOperatorBoolean("Alpha", DT, true, "<=");
  }

  /**
   * @testName: elStringGreaterThanTest
   * @assertion_ids: EL:SPEC:21.7
   * @test_Strategy: Validate that if one of the operands in an EL ">" or "gt"
   *                 operation is an String, that both operands are coerced to
   *                 type String and the correct boolean value is returned.
   * 
   *                 Equations tested: String ">" & "gt" String
   */
  public void elStringGreaterThanTest() throws Fault {

    // Value A is greater than value B. (false)
    this.testOperatorBoolean("Gamma", DT, true, ">");

    // Value A greater than value B. (false)
    this.testOperatorBoolean("Beta", DT, false, "gt");

    // Value A is greater than value B. (false)
    this.testOperatorBoolean("Alpha", DT, false, "gt");

  }

  /**
   * @testName: elStringGreaterThanEqualTest
   * @assertion_ids: EL:SPEC:21.7
   * @test_Strategy: Validate that if one of the operands in an EL ">=" or "ge"
   *                 operation is an String, that both operands are coerced to
   *                 type String and the correct boolean value is returned.
   * 
   *                 Equations tested: String ">=" & "ge" String
   */
  public void elStringGreaterThanEqualTest() throws Fault {

    // Value A is greater than or equal to value B. (true)
    this.testOperatorBoolean("Gamma", DT, true, ">=");

    // Value A is greater than or equal to value B. (true)
    this.testOperatorBoolean("Beta", DT, true, "ge");

    // Value A is greater than or equal to value B. (false)
    this.testOperatorBoolean("Alpha", DT, false, "ge");

  }

  /**
   * @testName: elStringEqualToTest
   * @assertion_ids: EL:SPEC:22.9
   * @test_Strategy: Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is an String, that both operands are coerced to
   *                 type String and the correct boolean value is returned.
   * 
   *                 Equations tested: String "==" & "eq" String
   */
  public void elStringEqualToTest() throws Fault {

    // Value A is equal to value B. (true)
    this.testOperatorBoolean("Beta", DT, true, "==");

    // Value A is smaller than value B. (false)
    this.testOperatorBoolean("Alpha", DT, false, "eq");

  }

  /**
   * @testName: elStringNotEqualToTest
   * @assertion_ids: EL:SPEC:22.9
   * @test_Strategy: Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is an String, that both operands are coerced to
   *                 type String and the correct boolean value is returned.
   * 
   *                 Equations tested: String "!=" & "ne" String
   */
  public void elStringNotEqualToTest() throws Fault {

    // Value A is not equal to value B. (true)
    this.testOperatorBoolean("Alpha", DT, true, "!=");

    // Value A is not equal to value B. (false)
    this.testOperatorBoolean("Beta", DT, false, "ne");

  }

  /**
   * @testName: elOtherLessThanTest
   * @assertion_ids: EL:SPEC:21.8.2
   * @test_Strategy: Validate that if operand A in an EL "<" or "lt" operation
   *                 is comparable, the result A.compareTo(B) is returned.
   * 
   *                 Equation example: DougType "<" & "lt" NickType
   */
  public void elOtherLessThanTest() throws Fault {

    // Value A is less than value B. (true)
    this.testOperatorBoolean(DT, NT, true, "<");

    // Value A is less than value B. (false)
    this.testOperatorBoolean(DT, DT, false, "lt");

    // Value A is less than value B. (false)
    this.testOperatorBoolean(NT, DT, false, "lt");

  }

  /**
   * @testName: elOtherLessThanEqualTest
   * @assertion_ids: EL:SPEC:21.8.2
   * @test_Strategy: Validate that if operand A in an EL "<=" or "le" operation
   *                 is comparable, the result A.compareTo(B) is returned.
   * 
   *                 Equation example: DougType "<=" & "le" NickType
   */
  public void elOtherLessThanEqualTest() throws Fault {

    // Value A is less than or equal to value B. (false)
    this.testOperatorBoolean(NT, DT, false, "<=");

    // Value A is less than or equal to value B. (true)
    this.testOperatorBoolean(DT, DT, true, "le");

    // Value A is less than or equal to value B. (true)
    this.testOperatorBoolean(DT, NT, true, "<=");
  }

  /**
   * @testName: elOtherGreaterThanTest
   * @assertion_ids: EL:SPEC:21.8.2
   * @test_Strategy: Validate that if operand A in an EL ">" or "gt" operation
   *                 is comparable, the result A.compareTo(B) is returned.
   * 
   *                 Equation example: DougType "<" & "gt" NickType
   */
  public void elOtherGreaterThanTest() throws Fault {

    // Value A is greater than value B. (true)
    this.testOperatorBoolean(NT, DT, true, ">");

    // Value A greater than value B. (false)
    this.testOperatorBoolean(DT, DT, false, "gt");

    // Value A is greater than value B. (false)
    this.testOperatorBoolean(DT, NT, false, "gt");

  }

  /**
   * @testName: elOtherGreaterThanEqualTest
   * @assertion_ids: EL:SPEC:21.8.2
   * @test_Strategy: Validate that if operand A in an EL ">=" or "ge" operation
   *                 is comparable, the result A.compareTo(B) is returned.
   * 
   *                 Equations tested: DougType ">=" & "ge" NickType
   */
  public void elOtherGreaterThanEqualTest() throws Fault {

    // Value A is greater than or equal to value B. (true)
    this.testOperatorBoolean(NT, DT, true, ">=");

    // Value A is greater than or equal to value B. (true)
    this.testOperatorBoolean(DT, DT, true, "ge");

    // Value A is greater than or equal to value B. (false)
    this.testOperatorBoolean(DT, NT, false, "ge");

  }

  /**
   * @testName: elOtherEqualToTest
   * @assertion_ids: EL:SPEC:22.11
   * @test_Strategy: Validate that if operand A in an EL "==" or "eq" operation
   *                 is comparable, the result A.equals(B) is returned.
   * 
   *                 Equations Example: DougType "==" & "eq" NickType
   */
  public void elOtherEqualToTest() throws Fault {

    // Value A is equal to value B. (true)
    this.testOperatorBoolean(DT, DT, true, "==");

    // Value A is smaller than value B. (false)
    this.testOperatorBoolean(DT, NT, false, "eq");

  }

  /**
   * @testName: elOtherNotEqualToTest
   * @assertion_ids: EL:SPEC:22.11
   * @test_Strategy: Validate that if operand A in an EL "!=" or "ne" operation
   *                 is comparable, the result A.equals(B) is returned.
   * 
   *                 Equation Example: DougType "!=" & "ne" NickType
   */
  public void elOtherNotEqualToTest() throws Fault {

    // Value A is not equal to value B. (true)
    this.testOperatorBoolean(DT, NT, true, "!=");

    // Value A is not equal to value B. (false)
    this.testOperatorBoolean(DT, DT, false, "ne");

  }

  /**
   * @testName: elBooleanEqualToTest
   * @assertion_ids: EL:SPEC:22.7
   * @test_Strategy: Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is an Boolean, that both operands are coerced to
   *                 type Boolean and the correct boolean value is returned.
   * 
   *                 Equations tested: Boolean "==" & "eq" String
   */
  public void elBooleanEqualToTest() throws Fault {

    // Value A is equal to value B. (true)
    this.testOperatorBoolean("true", true, true, "==");

    // Value A is smaller than value B. (false)
    this.testOperatorBoolean("false", true, false, "eq");

  }

  /**
   * @testName: elBooleanNotEqualToTest
   * @assertion_ids: EL:SPEC:22.7
   * @test_Strategy: Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is an Boolean, that both operands are coerced to
   *                 type Boolean and the correct boolean value is returned.
   * 
   *                 Equations tested: String "!=" & "ne" String
   */
  public void elBooleanNotEqualToTest() throws Fault {

    // Value A is not equal to value B. (true)
    this.testOperatorBoolean("false", true, true, "!=");

    // Value A is not equal to value B. (false)
    this.testOperatorBoolean("false", false, false, "ne");

  }

  /**
   * @testName: elEnumEqualToTest
   * @assertion_ids: EL:SPEC:22.8
   * @test_Strategy: Validate that if one of the operands in an EL "==" or "eq"
   *                 operation is an Enum, that both operands are coerced to
   *                 type Enum and the correct boolean value is returned.
   * 
   *                 Example Equation: Enum "==" String or Integer
   */
  public void elEnumEqualToTest() throws Fault {

    // Value A is equal to value B. (true)
    this.testOperatorBoolean(TestEnum.APPLE, "APPLE", true, "==");
    this.testOperatorBoolean(TestEnum.PEAR, "PEAR", true, "eq");

    // Value A is not equal to value B. (false)
    this.testOperatorBoolean(TestEnum.PEAR, "APPLE", false, "==");
    this.testOperatorBoolean(TestEnum.APPLE, "PEAR", false, "eq");
  }

  /**
   * @testName: elEnumNotEqualToTest
   * @assertion_ids: EL:SPEC:22.8
   * @test_Strategy: Validate that if one of the operands in an EL "!=" or "ne"
   *                 operation is an Enum, that both operands are coerced to
   *                 type String and the correct boolean value is returned.
   * 
   *                 Example Equation: Enum "!=" & "ne" Enum
   */
  public void elEnumNotEqualToTest() throws Fault {

    // Value A is not equal to value B. (true)
    this.testOperatorBoolean(TestEnum.APPLE, "PEAR", true, "!=");
    this.testOperatorBoolean(TestEnum.PEAR, "APPLE", true, "ne");

    // Value A is not equal to value B. (false)
    this.testOperatorBoolean(TestEnum.APPLE, "APPLE", false, "!=");
    this.testOperatorBoolean(TestEnum.PEAR, "PEAR", false, "ne");
  }

  // ---------------------------------------------------------- private
  // methods

  /**
   * This method is used to validate an expression that has at least one
   * BigDecimal in it. We pass in one of the operands(testVal), the other
   * operand is automatically picked up from the numberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. ">, >=,
   *          <, <=, gt, ge, lt, le, ==, !=, eq, ne)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  private void testOperatorBoolean(BigDecimal testVal, Boolean expectedVal,
      String booleanOperator) throws Fault {

    boolean pass;

    for (int i = 0; numberList.size() > i; i++) {
      TestUtil.logTrace(
          "*** Start " + "\"" + "BigDecimal" + "\"" + " Test Sequence ***");

      Object testNum = numberList.get(i);

      // We don't test numeric strings
      if (!(testNum instanceof Number))
        continue;

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(false, booleanOperator);
        TestUtil.logTrace("expression to be evaluated is " + expr);
        TestUtil.logTrace("types are " + "BigDecimal" + " and "
            + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logTrace("result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && (((Boolean) result).equals(expectedVal)));

      } catch (Exception e) {
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil.logTrace(
            "*** End " + "\"" + "BigDecimal" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one
   * BigInteger in it. We pass in one of the operands(testVal), the other
   * operand is automatically picked up from the numberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. >, >=, <,
   *          <=, gt, ge, lt, le, ==, !=, eq, ne)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  private void testOperatorBoolean(BigInteger testVal, Boolean expectedVal,
      String booleanOperator) throws Fault {

    boolean pass;

    for (int i = 0; numberList.size() > i; i++) {
      TestUtil.logTrace(
          "*** Start " + "\"" + "BigInteger" + "\"" + " Test Sequence ***");

      Object testNum = numberList.get(i);

      // We don't test numeric strings
      if (!(testNum instanceof Number))
        continue;

      if ((testNum instanceof BigDecimal) || (testNum instanceof Float)
          || (testNum instanceof Double)) {
        String skipType = testNum.getClass().getSimpleName();
        TestUtil.logTrace("Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(false, booleanOperator);
        TestUtil.logTrace("expression to be evaluated is " + expr);
        TestUtil.logTrace("types are " + "BigInteger" + " and "
            + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logTrace("result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && (((Boolean) result).equals(expectedVal)));

      } catch (Exception e) {
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil.logTrace(
            "*** End " + "\"" + "BigInteger" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one Float
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. ">, >=,
   *          <, <=, gt, ge, lt, le, ==, !=, eq, ne)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  private void testOperatorBoolean(Float testVal, Boolean expectedVal,
      String booleanOperator) throws Fault {

    boolean pass;

    for (int i = 0; numberList.size() > i; i++) {
      TestUtil.logTrace(
          "*** Start " + "\"" + "Float" + "\"" + " Test Sequence ***");

      Object testNum = numberList.get(i);

      // We don't test numeric strings
      if (!(testNum instanceof Number))
        continue;

      if ((testNum instanceof BigDecimal)) {
        String skipType = testNum.getClass().getSimpleName();
        TestUtil.logTrace("Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(false, booleanOperator);
        TestUtil.logTrace("expression to be evaluated is " + expr);
        TestUtil.logTrace(
            "types are " + "Float" + " and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logTrace("result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && (((Boolean) result).equals(expectedVal)));

      } catch (Exception e) {
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil.logTrace(
            "*** End " + "\"" + "Float" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one Double
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. ">, >=,
   *          <, <=, gt, ge, lt, le, ==, !=, eq, ne)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  private void testOperatorBoolean(Double testVal, Boolean expectedVal,
      String booleanOperator) throws Fault {

    boolean pass;

    for (int i = 0; numberList.size() > i; i++) {
      TestUtil.logTrace(
          "*** Start " + "\"" + "Double" + "\"" + " Test Sequence ***");

      Object testNum = numberList.get(i);

      // We don't test numeric strings
      if (!(testNum instanceof Number))
        continue;

      if ((testNum instanceof BigDecimal || testNum instanceof Float)) {
        String skipType = testNum.getClass().getSimpleName();
        TestUtil.logTrace("Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(false, booleanOperator);
        TestUtil.logTrace("expression to be evaluated is " + expr);
        TestUtil.logTrace(
            "types are " + "Double" + " and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logTrace("result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && (((Boolean) result).equals(expectedVal)));

      } catch (Exception e) {
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil.logTrace(
            "*** End " + "\"" + "Double" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one Long in
   * it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. ">, >=,
   *          <, <=, gt, ge, lt, le, ==, !=, eq, ne)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  private void testOperatorBoolean(Long testVal, Boolean expectedVal,
      String booleanOperator) throws Fault {

    boolean pass;

    for (int i = 0; numberList.size() > i; i++) {
      TestUtil
          .logTrace("*** Start " + "\"" + "Long" + "\"" + " Test Sequence ***");

      Object testNum = numberList.get(i);

      // We don't test numeric strings
      if (!(testNum instanceof Number))
        continue;

      if ((testNum instanceof BigDecimal || testNum instanceof Float
          || testNum instanceof BigInteger || testNum instanceof Double)) {
        String skipType = testNum.getClass().getSimpleName();
        TestUtil.logTrace("Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(false, booleanOperator);
        TestUtil.logTrace("expression to be evaluated is " + expr);
        TestUtil.logTrace(
            "types are " + "Long" + " and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logTrace("result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && (((Boolean) result).equals(expectedVal)));

      } catch (Exception e) {
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil
            .logTrace("*** End " + "\"" + "Long" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one Integer
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. ">, >=,
   *          <, <=, gt, ge, lt, le, ==, !=, eq, ne)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  private void testOperatorBoolean(Integer testVal, Boolean expectedVal,
      String booleanOperator) throws Fault {

    boolean pass;

    for (int i = 0; numberList.size() > i; i++) {
      TestUtil.logTrace(
          "*** Start " + "\"" + "Integer" + "\"" + " Test Sequence ***");

      Object testNum = numberList.get(i);

      // We don't test numeric strings
      if (!(testNum instanceof Number))
        continue;

      if ((testNum instanceof BigDecimal || testNum instanceof Float
          || testNum instanceof BigInteger || testNum instanceof Long
          || testNum instanceof Double)) {
        String skipType = testNum.getClass().getSimpleName();
        TestUtil.logTrace("Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(false, booleanOperator);
        TestUtil.logTrace("expression to be evaluated is " + expr);
        TestUtil.logTrace(
            "types are " + "Integer" + " and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logTrace("result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && (((Boolean) result).equals(expectedVal)));

      } catch (Exception e) {
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil.logTrace(
            "*** End " + "\"" + "Integer" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one Short
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. ">, >=,
   *          <, <=, gt, ge, lt, le, ==, !=, eq, ne)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  private void testOperatorBoolean(Short testVal, Boolean expectedVal,
      String booleanOperator) throws Fault {

    boolean pass;

    for (int i = 0; numberList.size() > i; i++) {
      TestUtil.logTrace(
          "*** Start " + "\"" + "Short" + "\"" + " Test Sequence ***");

      Object testNum = numberList.get(i);

      if (!(testNum instanceof Short || testNum instanceof Byte)) {
        String skipType = testNum.getClass().getSimpleName();
        TestUtil.logTrace("Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(false, booleanOperator);
        TestUtil.logTrace("expression to be evaluated is " + expr);
        TestUtil.logTrace(
            "types are " + "Short" + " and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logTrace("result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && (((Boolean) result).equals(expectedVal)));

      } catch (Exception e) {
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil.logTrace(
            "*** End " + "\"" + "Short" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one Byte in
   * it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. >, >=, <,
   *          <=, gt, ge, lt, le, ==, !=, eq, ne)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  private void testOperatorBoolean(Byte testVal, Boolean expectedVal,
      String booleanOperator) throws Fault {

    boolean pass;

    for (int i = 0; numberList.size() > i; i++) {
      TestUtil
          .logTrace("*** Start " + "\"" + "Byte" + "\"" + " Test Sequence ***");

      Object testNum = numberList.get(i);

      if (!(testNum instanceof Byte)) {
        String skipType = testNum.getClass().getSimpleName();
        TestUtil.logTrace("Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(false, booleanOperator);
        TestUtil.logTrace("expression to be evaluated is " + expr);
        TestUtil.logTrace(
            "types are " + "Byte" + " and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logTrace("result is " + result.toString());

        pass = (ExprEval.compareClass(result, Boolean.class)
            && (((Boolean) result).equals(expectedVal)));

      } catch (Exception e) {
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil
            .logTrace("*** End " + "\"" + "Byte" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one String
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - expected result.
   * @param booleanOperator
   *          - The operator in which the operands are compared. (i.e. ">, >=,
   *          <, <=, gt, ge, lt, le, ==, !=, eq, ne)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  private void testOperatorBoolean(Object testValOne, Object testValTwo,
      Boolean expectedVal, String booleanOperator) throws Fault {

    boolean pass;

    NameValuePair values[] = NameValuePair.buildNameValuePair(testValOne,
        testValTwo);

    try {
      TestUtil.logTrace(
          "*** Start " + "\"" + "String" + "\"" + " Test Sequence ***");

      String expr = ExprEval.buildElExpr(false, booleanOperator);
      TestUtil.logTrace("expression to be evaluated is " + expr);
      TestUtil.logTrace("types are " + "String and String");

      Object result = ExprEval.evaluateValueExpression(expr, values,
          Object.class);

      TestUtil.logTrace("result is " + result.toString());

      pass = (ExprEval.compareClass(result, Boolean.class)
          && (((Boolean) result).equals(expectedVal)));

    } catch (Exception e) {
      throw new Fault(e);

    } finally {
      ExprEval.cleanup();
      TestUtil
          .logTrace("*** End " + "\"" + "String" + "\"" + " Test Sequence ***");
    }

    if (!pass)
      throw new Fault("TEST FAILED: pass = false");

  }

  // ---------------------------------------------------- Inner Classes

  private static class DougType implements Comparable {

    @Override
    public String toString() {

      return "Beta";
    }

    public int compareTo(Object o) {

      if (o == null)
        return -1;
      return this.toString().compareTo(o.toString());
    }

    @Override
    public boolean equals(Object o) {

      // test for null.
      if (o == null)
        return false;

      /*
       * Since all DougType are staticly set to "Beta" All DougTypes are
       * considered equal, and any other object is not.
       */
      return (o instanceof DougType);

    }

    @Override
    public int hashCode() {
      return 42;
    }

  }

  private static class NickType implements Comparable {

    @Override
    public String toString() {
      return "Gamma";
    }

    public int compareTo(Object o) {

      if (o == null)
        return -1;
      return this.toString().compareTo(o.toString());
    }

    @Override
    public boolean equals(Object o) {

      // test for null.
      if (o == null)
        return false;

      /*
       * Since all NickType are statically set to "Gamma" All NickTypes are
       * considered equal, and any other object is not.
       */
      return (o instanceof NickType);

    }

    @Override
    public int hashCode() {
      return 42;
    }

  }

}
