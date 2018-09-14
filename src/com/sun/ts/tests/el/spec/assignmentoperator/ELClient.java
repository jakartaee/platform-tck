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

package com.sun.ts.tests.el.spec.assignmentoperator;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.el.ELProcessor;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.el.common.util.TypesBean;
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
   * @testName: elAssignmentOperatorBigDecimalTest
   * @assertion_ids: EL:SPEC:48.1.1; EL:SPEC:48.1.2; EL:SPEC:48.1.3;
   *                 EL:SPEC:48.1.4
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
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
  public void elAssignmentOperatorBigDecimalTest() throws Fault {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "BigDecimal";
    Iterator<Class<?>> iter;

    elp.defineBean("types", new TypesBean());

    iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {
      Class<?> bType = iter.next();
      String aValue = "a = types.tckBigDecimal";
      String bValue = TypesBean.getNumberMap().get(bType);
      String bName = bType.getSimpleName();

      elp.eval(aValue);
      elp.eval(bValue);

      // (+ operator)
      Validator.testExpression(elp, "a + b", BigDecimal.valueOf(2),
          comparitorA + " + " + bName);

      // (* operator)
      Validator.testExpression(elp, "a * b", BigDecimal.valueOf(1),
          comparitorA + " * " + bName);

      // (- operator)
      Validator.testExpression(elp, "a - b", BigDecimal.valueOf(0),
          comparitorA + " - " + bName);

      // (/ operator)
      Validator.testExpression(elp, "a / b", BigDecimal.valueOf(1),
          comparitorA + " / " + bName);

      // (div operator)
      Validator.testExpression(elp, "a div b", BigDecimal.valueOf(1),
          comparitorA + " div " + bName);

      // (% operator)
      Validator.testExpression(elp, "a % b", Double.valueOf(0),
          comparitorA + " % " + bName);

      // (mod operator)
      Validator.testExpression(elp, "a mod b", Double.valueOf(0),
          comparitorA + " mod " + bName);

      // Clean variables...
      elp.eval("a = null");
      elp.eval("b = null");
    }

  } // End elAssignmentOperatorBigDecimalTest

  /**
   * @testName: elAssignmentOperatorFloatTest
   * @assertion_ids: EL:SPEC:48.1.1; EL:SPEC:48.1.2; EL:SPEC:48.1.3;
   *                 EL:SPEC:48.1.4
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
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
  public void elAssignmentOperatorFloatTest() throws Fault {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "Float";
    String aValue = "a = types.tckFloat";

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
        TestUtil.logTrace("*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {

        elp.eval(aValue);
        elp.eval(bValue);

        if ("BigInteger".equals(bName)) {
          // (+ operator)
          Validator.testExpression(elp, "a + b", BigDecimal.valueOf(2),
              comparitorA + " + " + bName);

          // (* operator)
          Validator.testExpression(elp, "a * b", BigDecimal.valueOf(1),
              comparitorA + " * " + bName);

          // (- operator)
          Validator.testExpression(elp, "a - b", BigDecimal.valueOf(0),
              comparitorA + " - " + bName);

          // (/ operator)
          Validator.testExpression(elp, "a / b", BigDecimal.valueOf(1),
              comparitorA + " / " + bName);

          // (div operator)
          Validator.testExpression(elp, "a div b", BigDecimal.valueOf(1),
              comparitorA + " div " + bName);

        } else {
          // (+ operator)
          Validator.testExpression(elp, "a + b", Double.valueOf(2),
              comparitorA + " + " + bName);

          // (* operator)
          Validator.testExpression(elp, "a * b", Double.valueOf(1),
              comparitorA + " * " + bName);

          // (- operator)
          Validator.testExpression(elp, "a - b", Double.valueOf(0),
              comparitorA + " - " + bName);

          // (/ operator)
          Validator.testExpression(elp, "a / b", Double.valueOf(1),
              comparitorA + " / " + bName);

          // (div operator)
          Validator.testExpression(elp, "a div b", Double.valueOf(1),
              comparitorA + " div " + bName);

        }

        // The same for all other tested data types.

        // (% operator)
        Validator.testExpression(elp, "a % b", Double.valueOf(0),
            comparitorA + " % " + bName);

        // (mod operator)
        Validator.testExpression(elp, "a mod b", Double.valueOf(0),
            comparitorA + " mod " + bName);

        // Clean variables...
        elp.eval("a = null");
        elp.eval("b = null");
      }
    }

  } // End elAssignmentOperatorFloatTest

  /**
   * @testName: elAssignmentOperatorDoubleTest
   * @assertion_ids: EL:SPEC:48.1.1; EL:SPEC:48.1.2; EL:SPEC:48.1.3;
   *                 EL:SPEC:48.1.4
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
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
  public void elAssignmentOperatorDoubleTest() throws Fault {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "Double";
    String aValue = "a = types.tckDouble";

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
        TestUtil.logTrace("*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {

        elp.eval(aValue);
        elp.eval(bValue);

        if ("BigInteger".equals(bName)) {
          // (+ operator)
          Validator.testExpression(elp, "a + b", BigDecimal.valueOf(2),
              comparitorA + " + " + bName);

          // (* operator)
          Validator.testExpression(elp, "a * b", BigDecimal.valueOf(1),
              comparitorA + " * " + bName);

          // (- operator)
          Validator.testExpression(elp, "a - b", BigDecimal.valueOf(0),
              comparitorA + " - " + bName);

          // (/ operator)
          Validator.testExpression(elp, "a / b", BigDecimal.valueOf(1),
              comparitorA + " / " + bName);

          // (div operator)
          Validator.testExpression(elp, "a div b", BigDecimal.valueOf(1),
              comparitorA + " div " + bName);

        } else {
          // (+ operator)
          Validator.testExpression(elp, "a + b", Double.valueOf(2),
              comparitorA + " + " + bName);

          // (* operator)
          Validator.testExpression(elp, "a * b", Double.valueOf(1),
              comparitorA + " * " + bName);

          // (- operator)
          Validator.testExpression(elp, "a - b", Double.valueOf(0),
              comparitorA + " - " + bName);

          // (/ operator)
          Validator.testExpression(elp, "a / b", Double.valueOf(1),
              comparitorA + " / " + bName);

          // (div operator)
          Validator.testExpression(elp, "a div b", Double.valueOf(1),
              comparitorA + " div " + bName);

        }

        // The same for all other tested data types.

        // (% operator)
        Validator.testExpression(elp, "a % b", Double.valueOf(0),
            comparitorA + " % " + bName);

        // (mod operator)
        Validator.testExpression(elp, "a mod b", Double.valueOf(0),
            comparitorA + " mod " + bName);

        // Clean variables...
        elp.eval("a = null");
        elp.eval("b = null");
      }
    }

  } // End elAssignmentOperatorDoubleTest

  /**
   * @testName: elAssignmentOperatorBigIntegerTest
   * @assertion_ids: EL:SPEC:48.1.1; EL:SPEC:48.1.2; EL:SPEC:48.1.3;
   *                 EL:SPEC:48.1.4
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
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
  public void elAssignmentOperatorBigIntegerTest() throws Fault {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "BigInteger";
    String aValue = "a = types.tckBigInteger";

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
        TestUtil.logTrace("*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {

        elp.eval(aValue);
        elp.eval(bValue);

        // (+ operator)
        Validator.testExpression(elp, "a + b", BigInteger.valueOf(2),
            comparitorA + " + " + bName);

        // (* operator)
        Validator.testExpression(elp, "a * b", BigInteger.valueOf(1),
            comparitorA + " * " + bName);

        // (- operator)
        Validator.testExpression(elp, "a - b", BigInteger.valueOf(0),
            comparitorA + " - " + bName);

        // (/ operator)
        Validator.testExpression(elp, "a / b", BigDecimal.valueOf(1),
            comparitorA + " / " + bName);

        // (div operator)
        Validator.testExpression(elp, "a div b", BigDecimal.valueOf(1),
            comparitorA + " div " + bName);

        // (% operator)
        Validator.testExpression(elp, "a % b", BigInteger.valueOf(0),
            comparitorA + " % " + bName);

        // (mod operator)
        Validator.testExpression(elp, "a mod b", BigInteger.valueOf(0),
            comparitorA + " mod " + bName);

        // Clean variables...
        elp.eval("a = null");
        elp.eval("b = null");
      }
    }

  } // End elAssignmentOperatorBigIntegerTest

  /**
   * @testName: elAssignmentOperatorIntegerTest
   * @assertion_ids: EL:SPEC:48.1.1; EL:SPEC:48.1.2; EL:SPEC:48.1.3;
   *                 EL:SPEC:48.1.4
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
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
  public void elAssignmentOperatorIntegerTest() throws Fault {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "Integer";
    String aValue = "a = types.tckInteger";

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
        TestUtil.logTrace("*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {

        elp.eval(aValue);
        elp.eval(bValue);

        // (+ operator)
        Validator.testExpression(elp, "a + b", Long.valueOf(2),
            comparitorA + " + " + bName);

        // (* operator)
        Validator.testExpression(elp, "a * b", Long.valueOf(1),
            comparitorA + " * " + bName);

        // (- operator)
        Validator.testExpression(elp, "a - b", Long.valueOf(0),
            comparitorA + " - " + bName);

        // (/ operator)
        Validator.testExpression(elp, "a / b", Double.valueOf(1),
            comparitorA + " / " + bName);

        // (div operator)
        Validator.testExpression(elp, "a div b", Double.valueOf(1),
            comparitorA + " div " + bName);

        // (% operator)
        Validator.testExpression(elp, "a % b", Long.valueOf(0),
            comparitorA + " % " + bName);

        // (mod operator)
        Validator.testExpression(elp, "a mod b", Long.valueOf(0),
            comparitorA + " mod " + bName);

        // Clean variables...
        elp.eval("a = null");
        elp.eval("b = null");
      }
    }

  } // End elAssignmentOperatorIntegerTest

  /**
   * @testName: elAssignmentOperatorLongTest
   * @assertion_ids: EL:SPEC:48.1.1; EL:SPEC:48.1.2; EL:SPEC:48.1.3;
   *                 EL:SPEC:48.1.4
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
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
  public void elAssignmentOperatorLongTest() throws Fault {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "Long";
    String aValue = "a = types.tckLong";

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
        TestUtil.logTrace("*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {

        elp.eval(aValue);
        elp.eval(bValue);

        // (+ operator)
        Validator.testExpression(elp, "a + b", Long.valueOf(2),
            comparitorA + " + " + bName);

        // (* operator)
        Validator.testExpression(elp, "a * b", Long.valueOf(1),
            comparitorA + " * " + bName);

        // (- operator)
        Validator.testExpression(elp, "a - b", Long.valueOf(0),
            comparitorA + " - " + bName);

        // (/ operator)
        Validator.testExpression(elp, "a / b", Double.valueOf(1),
            comparitorA + " / " + bName);

        // (div operator)
        Validator.testExpression(elp, "a div b", Double.valueOf(1),
            comparitorA + " div " + bName);

        // (% operator)
        Validator.testExpression(elp, "a % b", Long.valueOf(0),
            comparitorA + " % " + bName);

        // (mod operator)
        Validator.testExpression(elp, "a mod b", Long.valueOf(0),
            comparitorA + " mod " + bName);

        // Clean variables...
        elp.eval("a = null");
        elp.eval("b = null");
      }
    }

  } // End elAssignmentOperatorLongTest

  /**
   * @testName: elAssignmentOperatorShortTest
   * @assertion_ids: EL:SPEC:48.1.1; EL:SPEC:48.1.2; EL:SPEC:48.1.3;
   *                 EL:SPEC:48.1.4
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
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
  public void elAssignmentOperatorShortTest() throws Fault {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "Short";
    String aValue = "a = types.tckShort";

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
        TestUtil.logTrace("*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {

        elp.eval(aValue);
        elp.eval(bValue);

        // (+ operator)
        Validator.testExpression(elp, "a + b", Long.valueOf(2),
            comparitorA + " + " + bName);

        // (* operator)
        Validator.testExpression(elp, "a * b", Long.valueOf(1),
            comparitorA + " * " + bName);

        // (- operator)
        Validator.testExpression(elp, "a - b", Long.valueOf(0),
            comparitorA + " - " + bName);

        // (/ operator)
        Validator.testExpression(elp, "a / b", Double.valueOf(1),
            comparitorA + " / " + bName);

        // (div operator)
        Validator.testExpression(elp, "a div b", Double.valueOf(1),
            comparitorA + " div " + bName);

        // (% operator)
        Validator.testExpression(elp, "a % b", Long.valueOf(0),
            comparitorA + " % " + bName);

        // (mod operator)
        Validator.testExpression(elp, "a mod b", Long.valueOf(0),
            comparitorA + " mod " + bName);

        // Clean variables...
        elp.eval("a = null");
        elp.eval("b = null");
      }
    }

  } // End elAssignmentOperatorShortTest

  /**
   * @testName: elAssignmentOperatorByteTest
   * @assertion_ids: EL:SPEC:48.1.1; EL:SPEC:48.1.2; EL:SPEC:48.1.3;
   *                 EL:SPEC:48.1.4
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
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
  public void elAssignmentOperatorByteTest() throws Fault {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "Byte";
    String aValue = "a = types.tckByte";

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
        TestUtil.logTrace("*** Skipping " + comparitorA + " with " + bName
            + ", Already Tested in " + bName + " Test Sequence ***");

      } else {

        elp.eval(aValue);
        elp.eval(bValue);

        // (+ operator)
        Validator.testExpression(elp, "a + b", Long.valueOf(2),
            comparitorA + " + " + bName);

        // (* operator)
        Validator.testExpression(elp, "a * b", Long.valueOf(1),
            comparitorA + " * " + bName);

        // (- operator)
        Validator.testExpression(elp, "a - b", Long.valueOf(0),
            comparitorA + " - " + bName);

        // (/ operator)
        Validator.testExpression(elp, "a / b", Double.valueOf(1),
            comparitorA + " / " + bName);

        // (div operator)
        Validator.testExpression(elp, "a div b", Double.valueOf(1),
            comparitorA + " div " + bName);

        // (% operator)
        Validator.testExpression(elp, "a % b", Long.valueOf(0),
            comparitorA + " % " + bName);

        // (mod operator)
        Validator.testExpression(elp, "a mod b", Long.valueOf(0),
            comparitorA + " mod " + bName);

        // Clean variables...
        elp.eval("a = null");
        elp.eval("b = null");
      }
    }

  } // End elAssignmentOperatorByteTest

  /**
   * @testName: elAssignmentOperatorNullTest
   * @assertion_ids: EL:SPEC:48.1.1; EL:SPEC:48.1.2; EL:SPEC:48.1.3;
   *                 EL:SPEC:48.1.4
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
   * 
   *                 Variable A - null
   * 
   *                 Variable B - null
   * 
   * @since: 3.0
   * 
   */
  public void elAssignmentOperatorNullTest() throws Fault {

    ELProcessor elp = new ELProcessor();
    elp.defineBean("types", new TypesBean());

    Long expected = Long.valueOf(0);
    String aValue = "a = types.tckNull";
    String bValue = "b = types.tckNull";

    elp.eval(aValue);
    elp.eval(bValue);

    // (+ operator)
    Validator.testExpression(elp, "a + b", expected, "null + null");

    // (- operator)
    Validator.testExpression(elp, "a - b", expected, "null - null");

    // (* operator)
    Validator.testExpression(elp, "a * b", expected, "null * null");

    // (/ operator)
    Validator.testExpression(elp, "a / b", expected, "null / null");

    // (div operator)
    Validator.testExpression(elp, "a div b", expected, "null div null");

    // (% operator)
    Validator.testExpression(elp, "a % b", expected, "null % null");

    // (mod operator)
    Validator.testExpression(elp, "a mod b", expected, "null mod null");

  } // End elAssignmentOperatorNullTest

  /**
   * @testName: elAssignmentOperatorMultiTest
   * @assertion_ids: EL:SPEC:48.1.1; EL:SPEC:48.1.2; EL:SPEC:48.1.3;
   *                 EL:SPEC:48.1.4
   * @test_Strategy: Validate that when we have variable A set to a specific
   *                 data type that we coerce and receive back the correct value
   *                 and Class type.
   * 
   *                 Operators: +, -, *, /, div, %, mod
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
  public void elAssignmentOperatorMultiTest() throws Fault {

    ELProcessor elp = new ELProcessor();
    String comparitorA = "BigDecimal";
    Iterator<Class<?>> iter;

    elp.defineBean("types", new TypesBean());

    iter = TypesBean.getNumberMap().keySet().iterator();
    while (iter.hasNext()) {
      Class<?> bType = iter.next();
      String bName = bType.getSimpleName();

      String aValue = "a = types.tckBigDecimal";
      String bValue = TypesBean.getNumberMap().get(bType);
      String cValue = "c = types.tckBigDecimal";

      elp.eval(aValue);
      elp.eval(bValue);
      elp.eval(cValue);

      // (+ operator)
      Validator.testExpression(elp, "a + b + c", BigDecimal.valueOf(3),
          comparitorA + " + " + bName);

      // (* operator)
      Validator.testExpression(elp, "a * b + c", BigDecimal.valueOf(2),
          comparitorA + " * " + bName);

      // (- operator)
      Validator.testExpression(elp, "a - b + c", BigDecimal.valueOf(1),
          comparitorA + " - " + bName);

      // (/ operator)
      Validator.testExpression(elp, "a / b + c", BigDecimal.valueOf(2),
          comparitorA + " / " + bName);

      // (div operator)
      Validator.testExpression(elp, "a div b + c", BigDecimal.valueOf(2),
          comparitorA + " div " + bName);

      // (% operator)
      Validator.testExpression(elp, "a % b + c", BigDecimal.valueOf(1),
          comparitorA + " % " + bName);

      // (mod operator)
      Validator.testExpression(elp, "a mod b + c", BigDecimal.valueOf(1),
          comparitorA + " mod " + bName);

      // Clean variables...
      elp.eval("a = null");
      elp.eval("b = null");
    }

  } // End elAssignmentOperatorMultiTest

}
