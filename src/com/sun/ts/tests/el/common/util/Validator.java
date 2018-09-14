/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

import javax.el.ELProcessor;

import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.util.TestUtil;

public class Validator {
  private static Validator instance = null;

  protected Validator() {
    // Exists only to defeat instantiation.
  }

  private Validator getInstance() {
    if (instance == null) {
      instance = new Validator();
    }
    return instance;
  }

  /**
   * This method is used to validate an expression that have at least one
   * BigDecimal in it. We pass in one of the operands(testVal), the other
   * operand is automatically picked up from the NumberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public static void testBigDecimal(BigDecimal testVal, Object expectedVal,
      String operator) throws Fault {
    boolean pass = false;
    Class<?> returnType;

    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      TestUtil.logMsg(
          "*** Start " + "\"" + "BigDecimal" + "\"" + " Test Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        TestUtil.logMsg("expression to be evaluated is " + expr);
        TestUtil
            .logMsg("types are BigDecimal and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logMsg("result is " + result.toString());

        /*
         * If operator is "+=" (concatenation) then coerce both operands to
         * String and concatenate them. (NEW to EL 3.0)
         */
        if ("+=".equals(operator)) {

          pass = Validator.runConcatenationTest(testVal, result, testNum);

          // If the Operator is "%" then the return type is Double.
        } else if ("%".equals(operator)) {

          returnType = Double.class;
          TestUtil.logMsg("Setting Expected Type: " + returnType.getName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue((Double) result,
                  Double.valueOf(((BigDecimal) expectedVal).doubleValue())));
        } else {
          returnType = BigDecimal.class;
          TestUtil.logMsg("Setting Expected Type: " + returnType.getName());

          pass = (ExprEval.compareClass(result, returnType) && ExprEval
              .compareValue((BigDecimal) result, (BigDecimal) expectedVal, 5));
        }

      } catch (RuntimeException re) {
        TestUtil.printStackTrace(re);
        throw new Fault(re);

      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil.logMsg(
            "*** End " + "\"" + "BigDecimal" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that have at least one Float
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the NumberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public static void testFloat(Float testVal, Object expectedVal,
      String operator) throws Fault {

    boolean pass = false;
    Class<?> returnType;

    // For each NumberType in this list.
    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      TestUtil
          .logMsg("*** Start " + "\"" + "Float" + "\"" + " Test Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      // If Test value from numberList is BigDecimal skip it.
      if (testNum instanceof BigDecimal) {
        TestUtil.logMsg("Skip " + testNum.getClass().getSimpleName()
            + " for Float tests we already tested for this in the"
            + " BigDecimal tests.");
        continue;
      }

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        TestUtil.logMsg("expression to be evaluated is " + expr);
        TestUtil.logMsg("types are Float and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logMsg("result is " + result.toString());

        /*
         * If operator is "+="concatenation then coerce both operands to String
         * and concatenate them. (NEW to EL 3.0)
         */
        if ("+=".equals(operator)) {
          pass = Validator.runConcatenationTest(testVal, result, testNum);

          // If the Operator is "%" then the return type is Double.
        } else if ("%".equals(operator)) {
          returnType = Double.class;
          TestUtil.logMsg(
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue((Double) result,
                  Double.valueOf(((Float) expectedVal).doubleValue())));
        } else {
          if (testNum instanceof BigInteger) {
            returnType = BigDecimal.class;
            TestUtil.logMsg(
                "Setting Expected Type: " + returnType.getCanonicalName());

            pass = (ExprEval.compareClass(result, returnType)
                && ExprEval.compareValue(((BigDecimal) result).floatValue(),
                    (Float) expectedVal, 3));
          } else {
            returnType = Double.class;
            TestUtil.logMsg(
                "Setting Expected Type: " + returnType.getCanonicalName());

            pass = (ExprEval.compareClass(result, returnType) && ExprEval
                .compareValue((Double) result, (Float) expectedVal, 3));
          }
        }

      } catch (RuntimeException re) {
        TestUtil.printStackTrace(re);
        throw new Fault(re);
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil
            .logMsg("*** End " + "\"" + "Float" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that have at least one Double
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the NumberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public static void testDouble(Double testVal, Object expectedVal,
      String operator) throws Fault {

    boolean pass = false;
    Class<?> returnType;

    // For each NumberType in this list.
    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      TestUtil.logMsg(
          "*** Start " + "\"" + "Double" + "\"" + "Test " + "Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      // If Test value from numberList is BigDecimal, Float skip it.
      if ((testNum instanceof BigDecimal) || (testNum instanceof Float)) {
        String skipType = testNum.getClass().getSimpleName();
        TestUtil.logMsg("Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        TestUtil.logMsg("expression to be evaluated is " + expr);
        TestUtil.logMsg("types are Double and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logMsg("result is " + result.toString());

        /*
         * If operator is "+=" (concatenation) then coerce both operands to
         * String and concatenate them. (NEW to EL 3.0)
         */
        if ("+=".equals(operator)) {
          pass = Validator.runConcatenationTest(testVal, result, testNum);

          // If the Operator is "%" then the return type is Double.
        } else if ("%".equals(operator)) {
          returnType = Double.class;
          TestUtil.logMsg(
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue((Double) result, expectedVal));
        } else {
          if (testNum instanceof BigInteger) {
            returnType = BigDecimal.class;
            TestUtil.logMsg(
                "Setting Expected Type: " + returnType.getCanonicalName());

            pass = (ExprEval.compareClass(result, returnType)
                && ExprEval.compareValue(((BigDecimal) result).doubleValue(),
                    expectedVal));
          } else {
            returnType = Double.class;
            TestUtil.logMsg(
                "Setting Expected Type: " + returnType.getCanonicalName());

            pass = (ExprEval.compareClass(result, returnType)
                && ExprEval.compareValue((Double) result, expectedVal));
          }
        }

      } catch (RuntimeException re) {
        TestUtil.printStackTrace(re);
        throw new Fault(re);

      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil
            .logMsg("*** End " + "\"" + "Double" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that have at least one
   * NumericString in it (numeric String containing ".", "e", or "E". We pass in
   * one of the operands(testVal), the other operand is automatically picked up
   * from the NumberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public static void testNumericString(String testVal, Double expectedVal,
      String operator) throws Fault {

    boolean pass = false;
    Class<?> returnType;

    // For each NumberType in this list.
    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      TestUtil.logMsg("*** Start " + "\"" + "NumericString" + "\"" + "Test "
          + "Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      // If Test value from numberList is BigDecimal, Float, Double skip
      // it.
      if ((testNum instanceof BigDecimal) || (testNum instanceof Float)
          || (testNum instanceof Double)) {
        String skipType = testNum.getClass().getSimpleName();
        TestUtil.logMsg("Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        TestUtil.logMsg("expression to be evaluated is " + expr);
        TestUtil.logMsg("types are String and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logMsg("result is " + result.toString());

        if ("%".equals(operator)) {
          returnType = Double.class;
          TestUtil.logMsg(
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue((Double) result, expectedVal));
        } else {
          if (testNum instanceof BigInteger) {
            returnType = BigDecimal.class;
            TestUtil.logMsg(
                "Setting Expected Type: " + returnType.getCanonicalName());

            pass = (ExprEval.compareClass(result, returnType)
                && ExprEval.compareValue(((BigDecimal) result).doubleValue(),
                    expectedVal));
          } else {
            returnType = Double.class;
            TestUtil.logMsg(
                "Setting Expected Type: " + returnType.getCanonicalName());

            pass = (ExprEval.compareClass(result, returnType)
                && ExprEval.compareValue((Double) result, expectedVal));
          }
        }

      } catch (RuntimeException re) {
        TestUtil.printStackTrace(re);
        throw new Fault(re);

      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil.logMsg(
            "*** End " + "\"" + "NumericString" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that have at least one
   * BigInteger in it. We pass in one of the operands(testVal), the other
   * operand is automatically picked up from the numberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public static void testBigInteger(BigInteger testVal, Object expectedVal,
      String operator) throws Fault {

    boolean pass = false;
    Class<?> returnType;

    // For each NumberType in this list.
    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      TestUtil.logMsg(
          "*** Start " + "\"" + "BigInteger" + "\"" + " Test Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      // If Test value from numberList BigDecimal, Float, Double, or
      // String skip it.
      if ((testNum instanceof BigDecimal) || (testNum instanceof Float)
          || (testNum instanceof Double) || (testNum instanceof String)) {
        String skipType = testNum.getClass().getSimpleName();
        TestUtil.logMsg("Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        TestUtil.logMsg("expression to be evaluated is " + expr);
        TestUtil
            .logMsg("types are BigInteger and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logMsg("result is " + result.toString());

        /*
         * If operator is "+=" then coerce both operands to String and
         * concatenate them. (NEW to EL 3.0)
         */
        if ("+=".equals(operator)) {
          pass = Validator.runConcatenationTest(testVal, result, testNum);

          // If the Operator is "/" then the return type is
          // BigDecimal.
        } else if ("/".equals(operator)) {
          returnType = BigDecimal.class;
          TestUtil.logMsg(
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue((BigDecimal) result,
                  BigDecimal.valueOf(((BigInteger) expectedVal).doubleValue()),
                  0));
        } else {
          returnType = BigInteger.class;
          TestUtil.logMsg(
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType) && ExprEval
              .compareValue((BigInteger) result, (BigInteger) expectedVal, 0));
        }

      } catch (RuntimeException re) {
        TestUtil.printStackTrace(re);
        throw new Fault(re);

      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil.logMsg(
            "*** End " + "\"" + "BigInteger" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that have at least one Long
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public static void testLong(Long testVal, Object expectedVal, String operator)
      throws Fault {

    boolean pass = false;
    Class<?> returnType;

    // For each NumberType in this list.
    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      TestUtil
          .logMsg("*** Start " + "\"" + "Long" + "\"" + " Test Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      // If Test value from numberList BigDecimal, Float, Double,
      // String or BigInteger skip it.
      if ((testNum instanceof BigDecimal) || (testNum instanceof Float)
          || (testNum instanceof Double) || (testNum instanceof String)
          || (testNum instanceof BigInteger)) {
        String skipType = testNum.getClass().getSimpleName();
        TestUtil.logMsg("Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        TestUtil.logMsg("expression to be evaluated is " + expr);
        TestUtil.logMsg("types are  Long and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logMsg("result is " + result.toString());

        /*
         * If operator is "+=" then coerce both operands to String and
         * concatenate them. (NEW to EL 3.0)
         */
        if ("+=".equals(operator)) {
          pass = Validator.runConcatenationTest(testVal, result, testNum);

          // If the Operator is "/" then the return type is Double.
        } else if ("/".equals(operator)) {
          returnType = Double.class;
          TestUtil.logMsg(
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue((Double) result,
                  ((Long) expectedVal).doubleValue()));
        } else {
          returnType = Long.class;
          TestUtil.logMsg(
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue((Long) result, (Long) expectedVal, 0));
        }

      } catch (RuntimeException re) {
        TestUtil.printStackTrace(re);
        throw new Fault(re);

      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil
            .logMsg("*** End " + "\"" + "Long" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that have at least one
   * Integer in it. We pass in one of the operands(testVal), the other operand
   * is automatically picked up from the numberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public static void testInteger(Integer testVal, Object expectedVal,
      String operator) throws Fault {

    boolean pass = false;
    Class<?> returnType;

    // For each NumberType in this list.
    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      TestUtil.logMsg(
          "*** Start " + "\"" + "Integer" + "\"" + " Test Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      // If Test value from numberList BigDecimal, Float, Double, String,
      // Long, or BigInteger skip it.
      if ((testNum instanceof BigDecimal) || (testNum instanceof Float)
          || (testNum instanceof Double) || (testNum instanceof String)
          || (testNum instanceof Long) || (testNum instanceof BigInteger)) {
        String skipType = testNum.getClass().getSimpleName();
        TestUtil.logMsg("Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        TestUtil.logMsg("expression to be evaluated is " + expr);
        TestUtil
            .logMsg("types are  Integer and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logMsg("result is " + result.toString());

        /*
         * If operator is "+=" then coerce both operands to String and
         * concatenate them. (NEW to EL 3.0)
         */
        if ("+=".equals(operator)) {
          pass = Validator.runConcatenationTest(testVal, result, testNum);

          // If the Operator is "/" then the return type is Double.
        } else if ("/".equals(operator)) {
          returnType = Double.class;
          TestUtil.logMsg(
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue((Double) result,
                  ((Integer) expectedVal).doubleValue()));
        } else {
          returnType = Long.class;
          TestUtil.logMsg(
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue((Long) result,
                  ((Integer) expectedVal).longValue()));
        }

      } catch (RuntimeException re) {
        TestUtil.printStackTrace(re);
        throw new Fault(re);

      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil.logMsg(
            "*** End " + "\"" + "Integer" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that have at least one Short
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public static void testShort(Short testVal, Object expectedVal,
      String operator) throws Fault {

    boolean pass = false;
    Class<?> returnType;

    // For each NumberType in this list.
    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      TestUtil
          .logMsg("*** Start " + "\"" + "Short" + "\"" + " Test Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      // If Test value from numberList BigDecimal, Float, Double, String,
      // Long, BigInteger, Integer skip it.
      if (!(testNum instanceof Short || testNum instanceof Byte)) {
        String skipType = testNum.getClass().getSimpleName();
        TestUtil.logMsg("Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        TestUtil.logMsg("expression to be evaluated is " + expr);
        TestUtil.logMsg("types are  Short and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logMsg("result is " + result.toString());

        /*
         * If operator is "+=" then coerce both operands to String and
         * concatenate them. (NEW to EL 3.0)
         */
        if ("+=".equals(operator)) {
          pass = Validator.runConcatenationTest(testVal, result, testNum);

          // If the Operator is "/" then the return type is Double.
        } else if ("/".equals(operator)) {
          returnType = Double.class;
          TestUtil.logMsg(
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue((Double) result,
                  ((Short) expectedVal).doubleValue()));
        } else {
          returnType = Long.class;
          TestUtil.logMsg(
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType) && ExprEval
              .compareValue((Long) result, ((Short) expectedVal).longValue()));
        }

      } catch (RuntimeException re) {
        TestUtil.printStackTrace(re);
        throw new Fault(re);

      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil
            .logMsg("*** End " + "\"" + "Short" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that have at least one Byte
   * in it. We pass in one of the operands(testVal), the other operand is
   * automatically picked up from the numberList.
   * 
   * @param testVal
   *          - One of the operands used in the expression.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public static void testByte(Byte testVal, Object expectedVal, String operator)
      throws Fault {

    boolean pass = false;
    Class<?> returnType;

    // For each NumberType in this list.
    for (int i = 0; TestNum.getNumberList().size() > i; i++) {
      TestUtil
          .logMsg("*** Start " + "\"" + "Byte" + "\"" + " Test Sequence ***");

      Object testNum = TestNum.getNumberList().get(i);

      NameValuePair values[] = NameValuePair.buildNameValuePair(testVal,
          testNum);

      // If Test value from numberList BigDecimal, Float, Double, String,
      // Long, BigInteger, Integer, Short skip it.
      if (!(testNum instanceof Byte)) {
        String skipType = testNum.getClass().getSimpleName();
        TestUtil.logMsg("Skip " + skipType + " Data type already "
            + "tested for this in the " + skipType + " tests.");
        continue;
      }

      try {
        String expr = ExprEval.buildElExpr(true, operator);
        TestUtil.logMsg("expression to be evaluated is " + expr);
        TestUtil.logMsg("types are  Byte and " + testNum.getClass().getName());

        Object result = ExprEval.evaluateValueExpression(expr, values,
            Object.class);

        TestUtil.logMsg("result is " + result.toString());

        /*
         * If operator is "+=" then coerce both operands to String and
         * concatenate them. (NEW to EL 3.0)
         */
        if ("+=".equals(operator)) {
          pass = Validator.runConcatenationTest(testVal, result, testNum);

          // If the Operator is "/" then the return type is Double.
        } else if ("/".equals(operator)) {
          returnType = Double.class;
          TestUtil.logMsg(
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType)
              && ExprEval.compareValue((Double) result,
                  ((Byte) expectedVal).doubleValue()));
        } else {
          returnType = Long.class;
          TestUtil.logMsg(
              "Setting Expected Type: " + returnType.getCanonicalName());

          pass = (ExprEval.compareClass(result, returnType) && ExprEval
              .compareValue((Long) result, ((Byte) expectedVal).longValue()));
        }

      } catch (RuntimeException re) {
        TestUtil.printStackTrace(re);
        throw new Fault(re);

      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        throw new Fault(e);

      } finally {
        ExprEval.cleanup();
        TestUtil
            .logMsg("*** End " + "\"" + "Byte" + "\"" + " Test Sequence ***");
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    }
  }

  /**
   * This method is used to validate an expression that has at least one boolean
   * in it.
   * 
   * @param testValOne
   *          - The boolean operand.
   * @param testValTwo
   *          - The second operand that will be coerced to a boolean.
   * @param expectedVal
   *          - The expected value returned from the Expression evaluation.
   * @param operator
   *          - The operator in which the operands are compared. (i.e. "+", "-",
   *          etc...)
   * @throws com.sun.ts.lib.harness.EETest.Fault
   */
  public static void testBoolean(boolean testValOne, Object testValTwo,
      Object expectedVal, String operator) throws Fault {

    boolean pass = false;

    NameValuePair values[] = NameValuePair.buildNameValuePair(testValOne,
        testValTwo);

    try {
      TestUtil.logMsg(
          "*** Start " + "\"" + "Boolean" + "\"" + " Test Sequence ***");
      String expr = ExprEval.buildElExpr(true, operator);
      TestUtil.logMsg("expression to be evaluated is " + expr);
      TestUtil
          .logMsg("types are  Boolean and " + testValTwo.getClass().getName());

      Object result = ExprEval.evaluateValueExpression(expr, values,
          Object.class);

      /*
       * If operator is "+=" then coerce both operands to String and concatenate
       * them. (NEW to EL 3.0)
       */
      if ("+=".equals(operator)) {
        pass = Validator.runConcatenationTest(testValOne, result, testValTwo);

      } else {
        TestUtil.logMsg("result is " + result.toString());
        pass = (ExprEval.compareClass(result, Boolean.class)
            && ExprEval.compareValue((Boolean) result, expectedVal));
      }

    } catch (RuntimeException re) {
      TestUtil.printStackTrace(re);
      throw new Fault(re);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(e);

    } finally {
      ExprEval.cleanup();
      TestUtil
          .logMsg("*** End " + "\"" + "Boolean" + "\"" + " Test Sequence ***");
    }

    if (!pass)
      throw new Fault("TEST FAILED: pass = false");

  }

  public static void testExpression(ELProcessor elp, String expr,
      Object expected, String testName) throws Fault {
    boolean pass = false;

    try {
      TestUtil.logMsg("*** Start " + testName + " Test Sequence ***");
      TestUtil.logMsg("expression to be evaluated is " + expr);
      Object result = elp.eval(expr);

      pass = ExprEval.compareClass(result, expected.getClass())
          && ExprEval.compareValue(result, expected);

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

    } catch (RuntimeException re) {
      TestUtil.printStackTrace(re);
      throw new Fault(re);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(e);

    } finally {
      ExprEval.cleanup();
      TestUtil.logMsg("*** End " + testName + " Test Sequence ***");
    }
  }

  // ------------------------- private methods

  private static Boolean runConcatenationTest(Object testVal, Object result,
      Object testNum) {

    Class<String> returnType = String.class;
    String expectedResult = testVal.toString() + testNum.toString();

    TestUtil.logMsg("Setting Expected Type: " + returnType.getName());

    return (ExprEval.compareClass(result, returnType)
        && ExprEval.compareValue(result, expectedResult));
  }
}
