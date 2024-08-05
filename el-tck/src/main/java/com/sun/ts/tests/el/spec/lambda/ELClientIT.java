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

package com.sun.ts.tests.el.spec.lambda;

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
   * @testName: elLambdaExprBigDecimalTest
   * 
   * @assertion_ids: EL:SPEC:50.1; EL:SPEC:50.2; EL:SPEC:50.3; EL:SPEC:50.4;
   *                 EL:SPEC:50.5; EL:SPEC:50.6; EL:JAVADOC:212
   * 
   * @test_Strategy: Evaluate the Lambda Expression, making sure the coercion
   *                 rules are followed.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expressions: "(((x, y)-> x [operator] y)(a, b))", "z =
   *                 (x,y)->x [operator] y" "z(a, b)", "func = (x,y)->x
   *                 [operator] y; func(a, b)", "(cond->[true/false]? a
   *                 [operator] b: a [operator] 2)(a)"
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
  public void elLambdaExprBigDecimalTest() throws Exception {
    String comparitorA = "BigDecimal";

    Iterator<Class<?>> iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {

      Class<?> bType = iter.next();
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();

      // (+ operator)
      this.runLambdaExpressions("+", BigDecimal.valueOf(2), comparitorA, bValue,
          bName);

      // (- operator)
      this.runLambdaExpressions("-", BigDecimal.valueOf(0), comparitorA, bValue,
          bName);

      // (* operator)
      this.runLambdaExpressions("*", BigDecimal.valueOf(1), comparitorA, bValue,
          bName);

      // (/ operator)
      this.runLambdaExpressions("/", BigDecimal.valueOf(1), comparitorA, bValue,
          bName);

      // (div operator)
      this.runLambdaExpressions("div", BigDecimal.valueOf(1), comparitorA,
          bValue, bName);

      // (% operator)
      this.runLambdaExpressions("%", Double.valueOf(0), comparitorA, bValue,
          bName);

      // (mod operator)
      this.runLambdaExpressions("mod", Double.valueOf(0), comparitorA, bValue,
          bName);
    }

  } // End elLambdaExprBigDecimalTest

  /**
   * @testName: elLambdaExprFloatTest
   * 
   * @assertion_ids: EL:SPEC:50.1; EL:SPEC:50.2; EL:SPEC:50.3; EL:SPEC:50.4;
   *                 EL:SPEC:50.5; EL:SPEC:50.6; EL:JAVADOC:212
   * 
   * @test_Strategy: Evaluate the Lambda Expression, making sure the coercion
   *                 rules are followed.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expressions: "(((x, y)-> x [operator] y)(a, b))", "z =
   *                 (x,y)->x [operator] y" "z(a, b)", "func = (x,y)->x
   *                 [operator] y; func(a, b)", "(cond->[true/false]? a
   *                 [operator] b: a [operator] 2)(a)"
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
  public void elLambdaExprFloatTest() throws Exception {
    String comparitorA = "Float";

    // excluded data types.
    List<String> excludeList = new ArrayList<String>();
    excludeList.add("BigDecimal");

    Iterator<Class<?>> iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {

      Class<?> bType = iter.next();
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();

      if (excludeList.contains(bName)) {
        logger.log(Logger.Level.INFO, "*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {

        if ("BigInteger".equals(bName)) {
          // (+ operator)
          this.runLambdaExpressions("+", BigDecimal.valueOf(2), comparitorA,
              bValue, bName);

          // (- operator)
          this.runLambdaExpressions("-", BigDecimal.valueOf(0), comparitorA,
              bValue, bName);

          // (* operator)
          this.runLambdaExpressions("*", BigDecimal.valueOf(1), comparitorA,
              bValue, bName);

          // (/ operator)
          this.runLambdaExpressions("/", BigDecimal.valueOf(1), comparitorA,
              bValue, bName);

          // (div operator)
          this.runLambdaExpressions("div", BigDecimal.valueOf(1), comparitorA,
              bValue, bName);

        } else {
          // (+ operator)
          this.runLambdaExpressions("+", Double.valueOf(2), comparitorA, bValue,
              bName);

          // (- operator)
          this.runLambdaExpressions("-", Double.valueOf(0), comparitorA, bValue,
              bName);

          // (* operator)
          this.runLambdaExpressions("*", Double.valueOf(1), comparitorA, bValue,
              bName);

          // (/ operator)
          this.runLambdaExpressions("/", Double.valueOf(1), comparitorA, bValue,
              bName);

          // (div operator)
          this.runLambdaExpressions("div", Double.valueOf(1), comparitorA,
              bValue, bName);

        }

        /*
         * The same for all other tested data types.
         */

        // (% operator)
        this.runLambdaExpressions("%", Double.valueOf(0), comparitorA, bValue,
            bName);

        // (mod operator)
        this.runLambdaExpressions("mod", Double.valueOf(0), comparitorA, bValue,
            bName);
      }
    }

  } // End elLambdaExprFloatTest

  /**
   * @testName: elLambdaExprDoubleTest
   * 
   * @assertion_ids: EL:SPEC:50.1; EL:SPEC:50.2; EL:SPEC:50.3; EL:SPEC:50.4;
   *                 EL:SPEC:50.5; EL:SPEC:50.6; EL:JAVADOC:212
   * 
   * @test_Strategy: Evaluate the Lambda Expression, making sure the coercion
   *                 rules are followed.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expressions: "(((x, y)-> x [operator] y)(a, b))", "z =
   *                 (x,y)->x [operator] y" "z(a, b)", "func = (x,y)->x
   *                 [operator] y; func(a, b)", "(cond->[true/false]? a
   *                 [operator] b: a [operator] 2)(a)"
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
  public void elLambdaExprDoubleTest() throws Exception {
    String comparitorA = "Double";

    // excluded data types.
    List<String> excludeList = new ArrayList<String>();
    excludeList.add("BigDecimal");
    excludeList.add("Float");

    Iterator<Class<?>> iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {

      Class<?> bType = iter.next();
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();

      if (excludeList.contains(bName)) {
        logger.log(Logger.Level.INFO, "*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {
        if ("BigInteger".equals(bName)) {
          // (+ operator)
          this.runLambdaExpressions("+", BigDecimal.valueOf(2), comparitorA,
              bValue, bName);

          // (- operator)
          this.runLambdaExpressions("-", BigDecimal.valueOf(0), comparitorA,
              bValue, bName);

          // (* operator)
          this.runLambdaExpressions("*", BigDecimal.valueOf(1), comparitorA,
              bValue, bName);

          // (/ operator)
          this.runLambdaExpressions("/", BigDecimal.valueOf(1), comparitorA,
              bValue, bName);

          // (div operator)
          this.runLambdaExpressions("div", BigDecimal.valueOf(1), comparitorA,
              bValue, bName);

        } else {
          // (+ operator)
          this.runLambdaExpressions("+", Double.valueOf(2), comparitorA, bValue,
              bName);

          // (- operator)
          this.runLambdaExpressions("-", Double.valueOf(0), comparitorA, bValue,
              bName);

          // (* operator)
          this.runLambdaExpressions("*", Double.valueOf(1), comparitorA, bValue,
              bName);

          // (/ operator)
          this.runLambdaExpressions("/", Double.valueOf(1), comparitorA, bValue,
              bName);

          // (div operator)
          this.runLambdaExpressions("div", Double.valueOf(1), comparitorA,
              bValue, bName);

        }

        /*
         * The same for all other tested data types.
         */

        // (% operator)
        this.runLambdaExpressions("%", Double.valueOf(0), comparitorA, bValue,
            bName);

        // (mod operator)
        this.runLambdaExpressions("mod", Double.valueOf(0), comparitorA, bValue,
            bName);
      }
    }

  } // End elLambdaExprDoubleTest

  /**
   * @testName: elLambdaExprBigIntegerTest
   * 
   * @assertion_ids: EL:SPEC:50.1; EL:SPEC:50.2; EL:SPEC:50.3; EL:SPEC:50.4;
   *                 EL:SPEC:50.5; EL:SPEC:50.6; EL:JAVADOC:212
   * 
   * @test_Strategy: Evaluate the Lambda Expression, making sure the coercion
   *                 rules are followed.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expressions: "(((x, y)-> x [operator] y)(a, b))", "z =
   *                 (x,y)->x [operator] y" "z(a, b)"
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
  public void elLambdaExprBigIntegerTest() throws Exception {
    String comparitorA = "BigInteger";

    // excluded data types.
    List<String> excludeList = new ArrayList<String>();
    excludeList.add("BigDecimal");
    excludeList.add("Double");
    excludeList.add("Float");

    Iterator<Class<?>> iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {
      Class<?> bType = iter.next();
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();

      if ((excludeList.contains(bName))) {
        logger.log(Logger.Level.INFO, "*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {
        // (+ operator)
        this.runLambdaExpressions("+", BigInteger.valueOf(2), comparitorA,
            bValue, bName);

        // (- operator)
        this.runLambdaExpressions("-", BigInteger.valueOf(0), comparitorA,
            bValue, bName);

        // (* operator)
        this.runLambdaExpressions("*", BigInteger.valueOf(1), comparitorA,
            bValue, bName);

        // (/ operator)
        this.runLambdaExpressions("/", BigDecimal.valueOf(1), comparitorA,
            bValue, bName);

        // (div operator)
        this.runLambdaExpressions("div", BigDecimal.valueOf(1), comparitorA,
            bValue, bName);

        // (% operator)
        this.runLambdaExpressions("%", BigInteger.valueOf(0), comparitorA,
            bValue, bName);

        // (mod operator)
        this.runLambdaExpressions("mod", BigInteger.valueOf(0), comparitorA,
            bValue, bName);
      }
    }

  } // End elLambdaExprBigIntegerTest

  /**
   * @testName: elLambdaExprIntegerTest
   * 
   * @assertion_ids: EL:SPEC:50.1; EL:SPEC:50.2; EL:SPEC:50.3; EL:SPEC:50.4;
   *                 EL:SPEC:50.5; EL:SPEC:50.6; EL:JAVADOC:212
   * 
   * @test_Strategy: Evaluate the Lambda Expression, making sure the coercion
   *                 rules are followed.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expressions: "(((x, y)-> x [operator] y)(a, b))", "z =
   *                 (x,y)->x [operator] y" "z(a, b)"
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
  public void elLambdaExprIntegerTest() throws Exception {
    String comparitorA = "Integer";

    // excluded data types.
    List<String> excludeList = new ArrayList<String>();
    excludeList.add("BigDecimal");
    excludeList.add("BigInteger");
    excludeList.add("Double");
    excludeList.add("Float");

    Iterator<Class<?>> iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {
      Class<?> bType = iter.next();
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();

      if ((excludeList.contains(bName))) {
        logger.log(Logger.Level.INFO, "*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {
        // (+ operator)
        this.runLambdaExpressions("+", Long.valueOf(2), comparitorA, bValue,
            bName);

        // (- operator)
        this.runLambdaExpressions("-", Long.valueOf(0), comparitorA, bValue,
            bName);

        // (* operator)
        this.runLambdaExpressions("*", Long.valueOf(1), comparitorA, bValue,
            bName);

        // (/ operator)
        this.runLambdaExpressions("/", Double.valueOf(1), comparitorA, bValue,
            bName);

        // (div operator)
        this.runLambdaExpressions("div", Double.valueOf(1), comparitorA, bValue,
            bName);

        // (% operator)
        this.runLambdaExpressions("%", Long.valueOf(0), comparitorA, bValue,
            bName);

        // (mod operator)
        this.runLambdaExpressions("mod", Long.valueOf(0), comparitorA, bValue,
            bName);
      }
    }

  } // End elLambdaExprIntegerTest

  /**
   * @testName: elLambdaExprLongTest
   * 
   * @assertion_ids: EL:SPEC:50.1; EL:SPEC:50.2; EL:SPEC:50.3; EL:SPEC:50.4;
   *                 EL:SPEC:50.5; EL:SPEC:50.6; EL:JAVADOC:212
   * 
   * @test_Strategy: Evaluate the Lambda Expression, making sure the coercion
   *                 rules are followed.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expressions: "(((x, y)-> x [operator] y)(a, b))", "z =
   *                 (x,y)->x [operator] y" "z(a, b)"
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
  public void elLambdaExprLongTest() throws Exception {
    String comparitorA = "Long";

    // excluded data types.
    List<String> excludeList = new ArrayList<String>();
    excludeList.add("BigDecimal");
    excludeList.add("BigInteger");
    excludeList.add("Integer");
    excludeList.add("Double");
    excludeList.add("Float");

    Iterator<Class<?>> iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {
      Class<?> bType = iter.next();
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();

      if ((excludeList.contains(bName))) {
        logger.log(Logger.Level.INFO, "*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {
        // (+ operator)
        this.runLambdaExpressions("+", Long.valueOf(2), comparitorA, bValue,
            bName);

        // (- operator)
        this.runLambdaExpressions("-", Long.valueOf(0), comparitorA, bValue,
            bName);

        // (* operator)
        this.runLambdaExpressions("*", Long.valueOf(1), comparitorA, bValue,
            bName);

        // (/ operator)
        this.runLambdaExpressions("/", Double.valueOf(1), comparitorA, bValue,
            bName);

        // (div operator)
        this.runLambdaExpressions("div", Double.valueOf(1), comparitorA, bValue,
            bName);

        // (% operator)
        this.runLambdaExpressions("%", Long.valueOf(0), comparitorA, bValue,
            bName);

        // (mod operator)
        this.runLambdaExpressions("mod", Long.valueOf(0), comparitorA, bValue,
            bName);
      }
    }

  } // End elLambdaExprLongTest

  /**
   * @testName: elLambdaExprShortTest
   * 
   * @assertion_ids: EL:SPEC:50.1; EL:SPEC:50.2; EL:SPEC:50.3; EL:SPEC:50.4;
   *                 EL:SPEC:50.5; EL:SPEC:50.6; EL:JAVADOC:212
   * 
   * @test_Strategy: Evaluate the Lambda Expression, making sure the coercion
   *                 rules are followed.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expressions: "(((x, y)-> x [operator] y)(a, b))", "z =
   *                 (x,y)->x [operator] y" "z(a, b)"
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
  public void elLambdaExprShortTest() throws Exception {
    String comparitorA = "Short";

    // excluded data types.
    List<String> excludeList = new ArrayList<String>();
    excludeList.add("BigDecimal");
    excludeList.add("BigInteger");
    excludeList.add("Integer");
    excludeList.add("Double");
    excludeList.add("Float");
    excludeList.add("Long");

    Iterator<Class<?>> iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {
      Class<?> bType = iter.next();
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();

      if ((excludeList.contains(bName))) {
        logger.log(Logger.Level.INFO, "*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {
        // (+ operator)
        this.runLambdaExpressions("+", Long.valueOf(2), comparitorA, bValue,
            bName);

        // (- operator)
        this.runLambdaExpressions("-", Long.valueOf(0), comparitorA, bValue,
            bName);

        // (* operator)
        this.runLambdaExpressions("*", Long.valueOf(1), comparitorA, bValue,
            bName);

        // (/ operator)
        this.runLambdaExpressions("/", Double.valueOf(1), comparitorA, bValue,
            bName);

        // (div operator)
        this.runLambdaExpressions("div", Double.valueOf(1), comparitorA, bValue,
            bName);

        // (% operator)
        this.runLambdaExpressions("%", Long.valueOf(0), comparitorA, bValue,
            bName);

        // (mod operator)
        this.runLambdaExpressions("mod", Long.valueOf(0), comparitorA, bValue,
            bName);
      }
    }

  } // End elLambdaExprShortTest

  /**
   * @testName: elLambdaExprByteTest
   * 
   * @assertion_ids: EL:SPEC:50.1; EL:SPEC:50.2; EL:SPEC:50.3; EL:SPEC:50.4;
   *                 EL:SPEC:50.5; EL:SPEC:50.6; EL:JAVADOC:212
   * 
   * @test_Strategy: Evaluate the Lambda Expression, making sure the coercion
   *                 rules are followed.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expressions: "(((x, y)-> x [operator] y)(a, b))", "z =
   *                 (x,y)->x [operator] y" "z(a, b)"
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
  public void elLambdaExprByteTest() throws Exception {
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

    Iterator<Class<?>> iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {
      Class<?> bType = iter.next();
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();

      if ((excludeList.contains(bName))) {
        logger.log(Logger.Level.INFO, "*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {
        // (+ operator)
        this.runLambdaExpressions("+", Long.valueOf(2), comparitorA, bValue,
            bName);

        // (- operator)
        this.runLambdaExpressions("-", Long.valueOf(0), comparitorA, bValue,
            bName);

        // (* operator)
        this.runLambdaExpressions("*", Long.valueOf(1), comparitorA, bValue,
            bName);

        // (/ operator)
        this.runLambdaExpressions("/", Double.valueOf(1), comparitorA, bValue,
            bName);

        // (div operator)
        this.runLambdaExpressions("div", Double.valueOf(1), comparitorA, bValue,
            bName);

        // (% operator)
        this.runLambdaExpressions("%", Long.valueOf(0), comparitorA, bValue,
            bName);

        // (mod operator)
        this.runLambdaExpressions("mod", Long.valueOf(0), comparitorA, bValue,
            bName);
      }
    }

  } // End elLambdaExprByteTest

  /**
   * @testName: elLambdaExprStringTest
   * 
   * @assertion_ids: EL:SPEC:49.1; EL:JAVADOC:212
   * 
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +
   * 
   *                 Expression: "(((x, y)-> x cat y)(a, b))"
   * 
   *                 Variable A - String
   * 
   *                 Variable B - String
   * 
   * @since: 3.0
   * 
   */
  @Test
  public void elLambdaExprStringTest() throws Exception {

    ELProcessor elp = new ELProcessor();

    String aValue = "a='Testing'";
    String bValue = "b='Testing'";

    elp.eval(aValue);
    elp.eval(bValue);

    // (+= operator)
    Validator.testExpression(elp, "(((x, y)-> x += y)(a, b))", "TestingTesting",
        "'Testing' += 'Testing'");

  } // End elLambdaExprStringTest

  /**
   * @testName: elLambdaExprNullTest
   * 
   * @assertion_ids: EL:SPEC:50.1; EL:SPEC:50.2; EL:SPEC:50.3; EL:SPEC:50.4;
   *                 EL:SPEC:50.5; EL:SPEC:50.6; EL:JAVADOC:212
   * 
   * @test_Strategy: Evaluate the Lambda Expression, making sure the coercion
   *                 rules are followed.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Expression: "(((x, y)-> x [operator] y)(a, b))"
   * 
   *                 Variable A - null
   * 
   *                 Variable B - null
   * 
   * @since: 3.0
   * 
   */
  @Test
  public void elLambdaExprNullTest() throws Exception {

    ELProcessor elp = new ELProcessor();
    elp.defineBean("types", new TypesBean());

    Long expected = Long.valueOf(0);
    String aValue = "a = types.tckNull";
    String bValue = "b = types.tckNull";

    elp.eval(aValue);
    elp.eval(bValue);
    String operator;

    // (+ operator)
    operator = "+";
    Validator.testExpression(elp, "(((x, y)-> x " + operator + " y)(a, b))",
        expected, "null + null");

    // (- operator)
    operator = "-";
    Validator.testExpression(elp, "(((x, y)-> x " + operator + " y)(a, b))",
        expected, "null - null");

    // (* operator)
    operator = "*";
    Validator.testExpression(elp, "(((x, y)-> x " + operator + " y)(a, b))",
        expected, "null * null");

    // (/ operator)
    operator = "/";
    Validator.testExpression(elp, "(((x, y)-> x " + operator + " y)(a, b))",
        expected, "null / null");

    // (div operator)
    operator = "div";
    Validator.testExpression(elp, "(((x, y)-> x " + operator + " y)(a, b))",
        expected, "null div null");

    // (% operator)
    operator = "%";
    Validator.testExpression(elp, "(((x, y)-> x " + operator + " y)(a, b))",
        expected, "null % null");

    // (mod operator)
    operator = "mod";
    Validator.testExpression(elp, "(((x, y)-> x " + operator + " y)(a, b))",
        expected, "null mod null");

  } // End elLambdaExprNullTest

  // ----------------------------- private methods.

  private void runLambdaExpressions(String operator, Number expectedResult,
      String aType, String comparitorB, String bName) throws Exception {

    ELProcessor elp = new ELProcessor();
    elp.defineBean("types", new TypesBean());

    String comparitorA = "a=types.tck" + aType;

    elp.eval(comparitorA);
    elp.eval(comparitorB);

    Validator.testExpression(elp, "(x->(y->x " + operator + " y)(a))(b)",
        expectedResult, aType + " " + operator + " " + bName);

    Validator.testExpression(elp, "(()->y->y " + operator + " a)()(b)",
        expectedResult, aType + " " + operator + " " + bName);

    Validator.testExpression(elp,
        "f = (x)->(tem=x; y->tem " + operator + " y); f(a)(b)", expectedResult,
        aType + " " + operator + " " + bName);

    Validator.testExpression(elp, "f = ()->y->y " + operator + " a; f()(b)",
        expectedResult, aType + " " + operator + " " + bName);

    Validator.testExpression(elp,
        "f = (x)->(tem=x; y->tem " + operator + " y); f(a)(b)", expectedResult,
        aType + " " + operator + " " + bName);

    Validator.testExpression(elp, "(((x, y)-> x " + operator + " y)(a, b))",
        expectedResult, aType + " " + operator + " " + bName);

    String zValue = "z = (x,y)->x " + operator + " y";
    elp.eval(zValue);
    Validator.testExpression(elp, "z(a, b)", expectedResult,
        aType + " " + operator + " " + bName);

    Validator.testExpression(elp,
        "func = (x,y)->x " + operator + " y; func(a, b)", expectedResult,
        aType + " " + operator + " " + bName);

    elp.eval("cond = true");
    Validator.testExpression(elp,
        "(cond->true? a " + operator + " b: a " + operator + " 2)(a)",
        expectedResult, aType + " " + operator + " " + bName);

    elp.eval("cond = false");
    Validator.testExpression(elp,
        "(cond->false? a " + operator + " 2: a " + operator + " b)(a)",
        expectedResult, aType + " " + operator + " " + bName);
  }
}
