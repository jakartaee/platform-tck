/*
 * Copyright (c) 2012, 2020 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package com.sun.ts.tests.el.spec.semicolonoperator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;


import com.sun.ts.tests.el.common.util.TypesBean;
import com.sun.ts.tests.el.common.util.Validator;

import jakarta.el.ELProcessor;

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
   * @testName: elSemiColonOperatorBigDecimalTest
   * 
   * @assertion_ids: EL:SPEC:49.1
   * 
   * @test_Strategy: To evaluate A;B, A is first evaluated, and its value is
   *                 discarded. B is then evaluated and its value is returned.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expression: "a = types.tckBigDecimal; a [operator] b"
   * 
   *                 Variable A - BigDecimal
   * 
   *                 Variable B - Rotating through the following types:
   *                 BigDecimal, BigInteger, Integer, Float, Long, Short,
   *                 Double, Byte
   * 
   *                 Excluded: none
   * 
   * @since: 3.0
   * 
   */
  @Test
  public void elSemiColonOperatorBigDecimalTest() throws Exception {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "BigDecimal";

    Iterator<Class<?>> iter;

    elp.defineBean("types", new TypesBean());

    iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {
      Class<?> bType = iter.next();
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();
      elp.eval(bValue);

      // (+ operator)
      Validator.testExpression(elp, "a = types.tckBigDecimal; a + b",
          BigDecimal.valueOf(2), comparitorA + " + " + bName);

      // (* operator)
      Validator.testExpression(elp, "a = types.tckBigDecimal; a * b",
          BigDecimal.valueOf(1), comparitorA + " * " + bName);

      // (- operator)
      Validator.testExpression(elp, "a = types.tckBigDecimal; a - b",
          BigDecimal.valueOf(0), comparitorA + " - " + bName);

      // (/ operator)
      Validator.testExpression(elp, "a = types.tckBigDecimal; a / b",
          BigDecimal.valueOf(1), comparitorA + " / " + bName);

      // (div operator)
      Validator.testExpression(elp, "a = types.tckBigDecimal; a div b",
          BigDecimal.valueOf(1), comparitorA + " div " + bName);

      // (% operator)
      Validator.testExpression(elp, "a = types.tckBigDecimal; a % b",
          Double.valueOf(0), comparitorA + " % " + bName);

      // (mod operator)
      Validator.testExpression(elp, "a = types.tckBigDecimal; a mod b",
          Double.valueOf(0), comparitorA + " mod " + bName);

      // Clean variables...
      elp.eval("a = null");
      elp.eval("b = null");
    }

  } // End elSemiColonOperatorBigDecimalTest

  /**
   * @testName: elSemiColonOperatorFloatTest
   * 
   * @assertion_ids: EL:SPEC:49.1
   * 
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expression: "a = types.tckFloat; a [operator] b"
   * 
   *                 Variable A - Float
   * 
   *                 Variable B - Rotating through the following types: Integer,
   *                 Float, Long, Short, Double, Byte
   * 
   *                 Exclude: BigDecimal
   * 
   * @since: 3.0
   * 
   */
  @Test
  public void elSemiColonOperatorFloatTest() throws Exception {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "Float";

    // excluded data types.
    List<String> excludeList = new ArrayList<String>();
    excludeList.add("BigDecimal");

    Iterator<Class<?>> iter;

    elp.defineBean("types", new TypesBean());

    iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {
      Class<?> bType = iter.next();
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();

      if (excludeList.contains(bName)) {
        logger.log(Logger.Level.TRACE, "*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {
        elp.eval(bValue);

        if ("BigInteger".equals(bName)) {
          // (+ operator)
          Validator.testExpression(elp, "a = types.tckFloat; a + b",
              BigDecimal.valueOf(2), comparitorA + " + " + bName);

          // (* operator)
          Validator.testExpression(elp, "a = types.tckFloat; a * b",
              BigDecimal.valueOf(1), comparitorA + " * " + bName);

          // (- operator)
          Validator.testExpression(elp, "a = types.tckFloat; a - b",
              BigDecimal.valueOf(0), comparitorA + " - " + bName);

          // (/ operator)
          Validator.testExpression(elp, "a = types.tckFloat; a / b",
              BigDecimal.valueOf(1), comparitorA + " / " + bName);

          // (div operator)
          Validator.testExpression(elp, "a = types.tckFloat; a div b",
              BigDecimal.valueOf(1), comparitorA + " div " + bName);

        } else {
          // (+ operator)
          Validator.testExpression(elp, "a = types.tckFloat; a + b",
              Double.valueOf(2), comparitorA + " + " + bName);

          // (* operator)
          Validator.testExpression(elp, "a = types.tckFloat; a * b",
              Double.valueOf(1), comparitorA + " * " + bName);

          // (- operator)
          Validator.testExpression(elp, "a = types.tckFloat; a - b",
              Double.valueOf(0), comparitorA + " - " + bName);

          // (/ operator)
          Validator.testExpression(elp, "a = types.tckFloat; a / b",
              Double.valueOf(1), comparitorA + " / " + bName);

          // (div operator)
          Validator.testExpression(elp, "a = types.tckFloat; a div b",
              Double.valueOf(1), comparitorA + " div " + bName);

        }

        // The same for all other tested data types.

        // (% operator)
        Validator.testExpression(elp, "a = types.tckFloat; a % b",
            Double.valueOf(0), comparitorA + " % " + bName);

        // (mod operator)
        Validator.testExpression(elp, "a = types.tckFloat; a mod b",
            Double.valueOf(0), comparitorA + " mod " + bName);

        // Clean variables...
        elp.eval("a = null");
        elp.eval("b = null");
      }
    }

  } // End elSemiColonOperatorFloatTest

  /**
   * @testName: elSemiColonOperatorDoubleTest
   * 
   * @assertion_ids: EL:SPEC:49.1
   * 
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expression: "a = types.tckDouble; a [operator] b"
   * 
   *                 Variable A - Double
   * 
   *                 Variable B - Rotating through the following types: Integer,
   *                 Float, Long, Short, Double, Byte
   * 
   *                 Exclude: BigDecimal, Float
   * 
   * @since: 3.0
   * 
   */
  @Test
  public void elSemiColonOperatorDoubleTest() throws Exception {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "Double";

    // excluded data types.
    List<String> excludeList = new ArrayList<String>();
    excludeList.add("BigDecimal");
    excludeList.add("Float");

    Iterator<Class<?>> iter;

    elp.defineBean("types", new TypesBean());

    iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {
      Class<?> bType = iter.next();
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();

      if (excludeList.contains(bName)) {
        logger.log(Logger.Level.TRACE, "*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {
        elp.eval(bValue);

        if ("BigInteger".equals(bName)) {
          // (+ operator)
          Validator.testExpression(elp, "a = types.tckDouble; a + b",
              BigDecimal.valueOf(2), comparitorA + " + " + bName);

          // (* operator)
          Validator.testExpression(elp, "a = types.tckDouble; a * b",
              BigDecimal.valueOf(1), comparitorA + " * " + bName);

          // (- operator)
          Validator.testExpression(elp, "a = types.tckDouble; a - b",
              BigDecimal.valueOf(0), comparitorA + " - " + bName);

          // (/ operator)
          Validator.testExpression(elp, "a = types.tckDouble; a / b",
              BigDecimal.valueOf(1), comparitorA + " / " + bName);

          // (div operator)
          Validator.testExpression(elp, "a = types.tckDouble; a div b",
              BigDecimal.valueOf(1), comparitorA + " div " + bName);

        } else {
          // (+ operator)
          Validator.testExpression(elp, "a = types.tckDouble; a + b",
              Double.valueOf(2), comparitorA + " + " + bName);

          // (* operator)
          Validator.testExpression(elp, "a = types.tckDouble; a * b",
              Double.valueOf(1), comparitorA + " * " + bName);

          // (- operator)
          Validator.testExpression(elp, "a = types.tckDouble; a - b",
              Double.valueOf(0), comparitorA + " - " + bName);

          // (/ operator)
          Validator.testExpression(elp, "a = types.tckDouble; a / b",
              Double.valueOf(1), comparitorA + " / " + bName);

          // (div operator)
          Validator.testExpression(elp, "a = types.tckDouble; a div b",
              Double.valueOf(1), comparitorA + " div " + bName);

        }

        // The same for all other tested data types.

        // (% operator)
        Validator.testExpression(elp, "a = types.tckDouble; a % b",
            Double.valueOf(0), comparitorA + " % " + bName);

        // (mod operator)
        Validator.testExpression(elp, "a = types.tckDouble; a mod b",
            Double.valueOf(0), comparitorA + " mod " + bName);

        // Clean variables...
        elp.eval("a = null");
        elp.eval("b = null");
      }
    }

  } // End elSemiColonOperatorDoubleTest

  /**
   * @testName: elSemiColonOperatorBigIntegerTest
   * 
   * @assertion_ids: EL:SPEC:49.1
   * 
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expression: "a = types.tckBigInteger; a [operator] b"
   * 
   *                 Variable A - BigInteger
   * 
   *                 Variable B - Rotating through the following types: Integer,
   *                 Float, Long, Short, Double, Byte
   * 
   *                 Exclude: BigDecimal, Float, Double
   * 
   * @since: 3.0
   * 
   */
  @Test
  public void elSemiColonOperatorBigIntegerTest() throws Exception {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "BigInteger";

    // excluded data types.
    List<String> excludeList = new ArrayList<String>();
    excludeList.add("BigDecimal");
    excludeList.add("Double");
    excludeList.add("Float");

    Iterator<Class<?>> iter;

    elp.defineBean("types", new TypesBean());

    iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {
      Class<?> bType = iter.next();
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();

      if ((excludeList.contains(bName))) {
        logger.log(Logger.Level.TRACE, "*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {
        elp.eval(bValue);

        // (+ operator)
        Validator.testExpression(elp, "a = types.tckBigInteger; a + b",
            BigInteger.valueOf(2), comparitorA + " + " + bName);

        // (* operator)
        Validator.testExpression(elp, "a = types.tckBigInteger; a * b",
            BigInteger.valueOf(1), comparitorA + " * " + bName);

        // (- operator)
        Validator.testExpression(elp, "a = types.tckBigInteger; a - b",
            BigInteger.valueOf(0), comparitorA + " - " + bName);

        // (/ operator)
        Validator.testExpression(elp, "a = types.tckBigInteger; a / b",
            BigDecimal.valueOf(1), comparitorA + " / " + bName);

        // (div operator)
        Validator.testExpression(elp, "a = types.tckBigInteger; a div b",
            BigDecimal.valueOf(1), comparitorA + " div " + bName);

        // (% operator)
        Validator.testExpression(elp, "a = types.tckBigInteger; a % b",
            BigInteger.valueOf(0), comparitorA + " % " + bName);

        // (mod operator)
        Validator.testExpression(elp, "a = types.tckBigInteger; a mod b",
            BigInteger.valueOf(0), comparitorA + " mod " + bName);

        // Clean variables...
        elp.eval("a = null");
        elp.eval("b = null");
      }
    }

  } // End elSemiColonOperatorBigIntegerTest

  /**
   * @testName: elSemiColonOperatorIntegerTest
   * 
   * @assertion_ids: EL:SPEC:49.1
   * 
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expression: "a = types.tckInteger; a [operator] b"
   * 
   *                 Variable A - Integer
   * 
   *                 Variable B - Rotating through the following types: Integer,
   *                 Float, Long, Short, Double, Byte
   * 
   *                 Exclude: BigDecimal, BigInteger, Float, Double
   * 
   * @since: 3.0
   * 
   */
  @Test
  public void elSemiColonOperatorIntegerTest() throws Exception {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "Integer";

    // excluded data types.
    List<String> excludeList = new ArrayList<String>();
    excludeList.add("BigDecimal");
    excludeList.add("BigInteger");
    excludeList.add("Double");
    excludeList.add("Float");

    Iterator<Class<?>> iter;

    elp.defineBean("types", new TypesBean());

    iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {
      Class<?> bType = iter.next();
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();

      if ((excludeList.contains(bName))) {
        logger.log(Logger.Level.TRACE, "*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {
        elp.eval(bValue);

        // (+ operator)
        Validator.testExpression(elp, "a = types.tckInteger; a + b",
            Long.valueOf(2), comparitorA + " + " + bName);

        // (* operator)
        Validator.testExpression(elp, "a = types.tckInteger; a * b",
            Long.valueOf(1), comparitorA + " * " + bName);

        // (- operator)
        Validator.testExpression(elp, "a = types.tckInteger; a - b",
            Long.valueOf(0), comparitorA + " - " + bName);

        // (/ operator)
        Validator.testExpression(elp, "a = types.tckInteger; a / b",
            Double.valueOf(1), comparitorA + " / " + bName);

        // (div operator)
        Validator.testExpression(elp, "a = types.tckInteger; a div b",
            Double.valueOf(1), comparitorA + " div " + bName);

        // (% operator)
        Validator.testExpression(elp, "a = types.tckInteger; a % b",
            Long.valueOf(0), comparitorA + " % " + bName);

        // (mod operator)
        Validator.testExpression(elp, "a = types.tckInteger; a mod b",
            Long.valueOf(0), comparitorA + " mod " + bName);

        // Clean variables...
        elp.eval("a = null");
        elp.eval("b = null");
      }
    }

  } // End elSemiColonOperatorIntegerTest

  /**
   * @testName: elSemiColonOperatorLongTest
   * 
   * @assertion_ids: EL:SPEC:49.1
   * 
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expression: "a = types.tckLong; a [operator] b"
   * 
   *                 Variable A - Long
   * 
   *                 Variable B - Rotating through the following types: Integer,
   *                 Float, Long, Short, Double, Byte
   * 
   *                 Exclude: BigDecimal, BigInteger, Float, Double, Integer
   * 
   * @since: 3.0
   * 
   */
  @Test
  public void elSemiColonOperatorLongTest() throws Exception {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "Long";

    // excluded data types.
    List<String> excludeList = new ArrayList<String>();
    excludeList.add("BigDecimal");
    excludeList.add("BigInteger");
    excludeList.add("Integer");
    excludeList.add("Double");
    excludeList.add("Float");

    Iterator<Class<?>> iter;

    elp.defineBean("types", new TypesBean());

    iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {
      Class<?> bType = iter.next();
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();

      if ((excludeList.contains(bName))) {
        logger.log(Logger.Level.TRACE, "*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {
        elp.eval(bValue);

        // (+ operator)
        Validator.testExpression(elp, "a = types.tckLong; a + b",
            Long.valueOf(2), comparitorA + " + " + bName);

        // (* operator)
        Validator.testExpression(elp, "a = types.tckLong; a * b",
            Long.valueOf(1), comparitorA + " * " + bName);

        // (- operator)
        Validator.testExpression(elp, "a = types.tckLong; a - b",
            Long.valueOf(0), comparitorA + " - " + bName);

        // (/ operator)
        Validator.testExpression(elp, "a = types.tckLong; a / b",
            Double.valueOf(1), comparitorA + " / " + bName);

        // (div operator)
        Validator.testExpression(elp, "a = types.tckLong; a div b",
            Double.valueOf(1), comparitorA + " div " + bName);

        // (% operator)
        Validator.testExpression(elp, "a = types.tckLong; a % b",
            Long.valueOf(0), comparitorA + " % " + bName);

        // (mod operator)
        Validator.testExpression(elp, "a = types.tckLong; a mod b",
            Long.valueOf(0), comparitorA + " mod " + bName);

        // Clean variables...
        elp.eval("a = null");
        elp.eval("b = null");
      }
    }

  } // End elSemiColonOperatorLongTest

  /**
   * @testName: elSemiColonOperatorShortTest
   * 
   * @assertion_ids: EL:SPEC:49.1
   * 
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expression: "a = types.tckShort; a [operator] b"
   * 
   *                 Variable A - Short
   * 
   *                 Variable B - Rotating through the following types: Integer,
   *                 Float, Long, Short, Double, Byte
   * 
   *                 Exclude: BigDecimal, BigInteger, Float, Double, Integer,
   *                 Long
   * 
   * @since: 3.0
   * 
   */
  @Test
  public void elSemiColonOperatorShortTest() throws Exception {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "Short";

    // excluded data types.
    List<String> excludeList = new ArrayList<String>();
    excludeList.add("BigDecimal");
    excludeList.add("BigInteger");
    excludeList.add("Integer");
    excludeList.add("Double");
    excludeList.add("Float");
    excludeList.add("Long");

    Iterator<Class<?>> iter;

    elp.defineBean("types", new TypesBean());

    iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {
      Class<?> bType = iter.next();
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();

      if ((excludeList.contains(bName))) {
        logger.log(Logger.Level.TRACE, "*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {
        elp.eval(bValue);

        // (+ operator)
        Validator.testExpression(elp, "a = types.tckShort; a + b",
            Long.valueOf(2), comparitorA + " + " + bName);

        // (* operator)
        Validator.testExpression(elp, "a = types.tckShort; a * b",
            Long.valueOf(1), comparitorA + " * " + bName);

        // (- operator)
        Validator.testExpression(elp, "a = types.tckShort; a - b",
            Long.valueOf(0), comparitorA + " - " + bName);

        // (/ operator)
        Validator.testExpression(elp, "a = types.tckShort; a / b",
            Double.valueOf(1), comparitorA + " / " + bName);

        // (div operator)
        Validator.testExpression(elp, "a = types.tckShort; a div b",
            Double.valueOf(1), comparitorA + " div " + bName);

        // (% operator)
        Validator.testExpression(elp, "a = types.tckShort; a % b",
            Long.valueOf(0), comparitorA + " % " + bName);

        // (mod operator)
        Validator.testExpression(elp, "a mod b", Long.valueOf(0),
            comparitorA + " mod " + bName);

        // Clean variables...
        elp.eval("a = null");
        elp.eval("b = null");
      }
    }

  } // End elSemiColonOperatorShortTest

  /**
   * @testName: elSemiColonOperatorByteTest
   * 
   * @assertion_ids: EL:SPEC:49.1
   * 
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expression: "a = types.tckByte; a [operator] b"
   * 
   *                 Variable A - Byte
   * 
   *                 Variable B - Rotating through the following types: Integer,
   *                 Float, Long, Short, Double, Byte
   * 
   *                 Exclude: BigDecimal, BigInteger, Float, Double, Integer,
   *                 Long, Short
   * 
   * @since: 3.0
   * 
   */
  @Test
  public void elSemiColonOperatorByteTest() throws Exception {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "Byte";

    // excluded data types.
    List<String> excludeList = new ArrayList<String>();
    excludeList.add("BigDecimal");
    excludeList.add("BigInteger");
    excludeList.add("Integer");
    excludeList.add("Double");
    excludeList.add("Float");
    excludeList.add("Long");
    excludeList.add("Short");

    Iterator<Class<?>> iter;

    elp.defineBean("types", new TypesBean());

    iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {
      Class<?> bType = iter.next();
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();

      if ((excludeList.contains(bName))) {
        logger.log(Logger.Level.TRACE, "*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {
        elp.eval(bValue);

        // (+ operator)
        Validator.testExpression(elp, "a = types.tckByte; a + b",
            Long.valueOf(2), comparitorA + " + " + bName);

        // (* operator)
        Validator.testExpression(elp, "a = types.tckByte; a * b",
            Long.valueOf(1), comparitorA + " * " + bName);

        // (- operator)
        Validator.testExpression(elp, "a = types.tckByte; a - b",
            Long.valueOf(0), comparitorA + " - " + bName);

        // (/ operator)
        Validator.testExpression(elp, "a = types.tckByte; a / b",
            Double.valueOf(1), comparitorA + " / " + bName);

        // (div operator)
        Validator.testExpression(elp, "a = types.tckByte; a div b",
            Double.valueOf(1), comparitorA + " div " + bName);

        // (% operator)
        Validator.testExpression(elp, "a = types.tckByte; a % b",
            Long.valueOf(0), comparitorA + " % " + bName);

        // (mod operator)
        Validator.testExpression(elp, "a = types.tckByte; a mod b",
            Long.valueOf(0), comparitorA + " mod " + bName);

        // Clean variables...
        elp.eval("a = null");
        elp.eval("b = null");
      }
    }

  } // End elSemiColonOperatorByteTest

  /**
   * @testName: elSemiColonOperatorNullTest
   * 
   * @assertion_ids: EL:SPEC:49.1
   * 
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expression: "a = null; a [operator] b"
   * 
   *                 Variable A - null
   * 
   *                 Variable B - null
   * 
   * @since: 3.0
   * 
   */
  @Test
  public void elSemiColonOperatorNullTest() throws Exception {

    ELProcessor elp = new ELProcessor();
    elp.defineBean("types", new TypesBean());

    Long expected = Long.valueOf(0);
    String bValue = "b = types.tckNull";

    elp.eval(bValue);

    // (+ operator)
    Validator.testExpression(elp, "a = null; a + b", expected, "null + null");

    // (- operator)
    Validator.testExpression(elp, "a = null; a - b", expected, "null - null");

    // (* operator)
    Validator.testExpression(elp, "a = null; a * b", expected, "null * null");

    // (/ operator)
    Validator.testExpression(elp, "a = null; a / b", expected, "null / null");

    // (div operator)
    Validator.testExpression(elp, "a = null; a div b", expected,
        "null div null");

    // (% operator)
    Validator.testExpression(elp, "a = null; a % b", expected, "null % null");

    // (mod operator)
    Validator.testExpression(elp, "a = null; a mod b", expected,
        "null mod null");

  } // End elSemiColonOperatorNullTest

}
