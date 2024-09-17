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

/**
 * $Id$
 */

package com.sun.ts.tests.el.spec.literal;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;


import com.sun.ts.tests.el.common.util.ExprEval;

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


  // ------------------------------------------------------------- Test
  // Methods

  /**
   * @testName: elBooleanLiteralTest
   * @assertion_ids: EL:SPEC:13.1
   * @test_Strategy: Validate that the EL Boolean literal: - 'true' is evaluated
   *                 as expected. - 'false' is evaluated as expected.
   */
  @Test
  public void elBooleanLiteralTest() throws Exception {

    boolean pass1, pass2;
    Boolean expectedResult = Boolean.TRUE;

    // Literal true.
    try {
      pass1 = false;
      expectedResult = Boolean.TRUE;

      Object result = ExprEval.evaluateValueExpression("${true}", null,
          Object.class);
      logger.log(Logger.Level.TRACE, "result is " + result.toString());
      pass1 = (ExprEval.compareClass(result, Boolean.class)
          && ExprEval.compareValue(result, expectedResult));

    } catch (Exception e) {
      throw new Exception(e);
    }

    // Literal false.
    try {
      pass2 = false;
      expectedResult = Boolean.FALSE;

      Object result = ExprEval.evaluateValueExpression("${false}", null,
          Object.class);
      logger.log(Logger.Level.TRACE, "result is " + result.toString());
      pass2 = (ExprEval.compareClass(result, Boolean.class)
          && ExprEval.compareValue(result, expectedResult));

    } catch (Exception e) {
      throw new Exception(e);
    }

    if (!pass1)
      throw new Exception("TEST FAILED: Literal true evaluated incorrectly.");
    if (!pass2)
      throw new Exception("TEST FAILED: Literal false evaluated " + "incorrectly.");
  }

  /**
   * @testName: elIntegerLiteralTest
   * @assertion_ids: EL:SPEC:13.2
   * @test_Strategy: Validate that the EL Integer literal with the: - '$' is
   *                 evaluated as expected. - '#' is evaluated as expected.
   */
  @Test
  public void elIntegerLiteralTest() throws Exception {

    boolean pass1, pass2;
    Long expectedResult;

    List ilist = this.getIntegerList();

    for (Iterator it = ilist.iterator(); it.hasNext();) {
      Integer tInteger = (Integer) it.next();
      expectedResult = Long.valueOf(tInteger);

      // test "$" symbol
      try {
        pass1 = false;

        Object result = ExprEval.evaluateValueExpression(
            "${" + tInteger.toString() + "}", null, Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass1 = (ExprEval.compareClass(result, Long.class)
            && ExprEval.compareValue((Long) result, expectedResult));

      } catch (Exception e) {
        throw new Exception(e);
      }

      // Test "#" symbol
      try {
        pass2 = false;

        Object result = ExprEval.evaluateValueExpression(
            "#{" + tInteger.toString() + "}", null, Object.class);
        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass2 = (ExprEval.compareClass(result, Long.class)
            && ExprEval.compareValue((Long) result, expectedResult));

      } catch (Exception e) {
        throw new Exception(e);
      }

      if (!pass1)
        throw new Exception(
            "TEST FAILED: Literal Integer \"$\" evaluated" + " incorrectly.");
      if (!pass2)
        throw new Exception(
            "TEST FAILED: Literal Integer \"#\" evaluated" + " incorrectly.");
    }
  }

  /**
   * @testName: elFloatingPointLiteralTest
   * @assertion_ids: EL:SPEC:13.3
   * @test_Strategy: Validate that the EL Float literal with the: - '$' is
   *                 evaluated as expected. - '#' is evaluated as expected.
   */
  @Test
  public void elFloatingPointLiteralTest() throws Exception {

    boolean pass1, pass2;
    Float expectedResult;

    List flist = this.getFloatList();

    for (Iterator it = flist.iterator(); it.hasNext();) {
      Float tFloat = (Float) it.next();
      expectedResult = Float.valueOf(tFloat);

      // test "$" symbol
      try {
        pass1 = false;

        Object result = ExprEval.evaluateValueExpression(
            "${" + tFloat.toString() + "}", null, Object.class);
        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass1 = (ExprEval.compareClass(result, Double.class)
            && ExprEval.compareValue((Double) result, expectedResult, 1));

      } catch (Exception e) {
        throw new Exception(e);
      }

      // Test "#" symbol
      try {
        pass2 = false;

        Object result = ExprEval.evaluateValueExpression(
            "#{" + tFloat.toString() + "}", null, Object.class);
        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass2 = (ExprEval.compareClass(result, Double.class)
            && ExprEval.compareValue((Double) result, expectedResult, 1));

      } catch (Exception e) {
        throw new Exception(e);
      }

      if (!pass1)
        throw new Exception(
            "TEST FAILED: Literal Float \"$\" evaluated" + " incorrectly.");
      if (!pass2)
        throw new Exception(
            "TEST FAILED: Literal Float \"#\" evaluated" + " incorrectly.");
    }
  }

  /**
   * @testName: elStringLiteralTest
   * @assertion_ids: EL:SPEC:13.4; EL:SPEC:13.5
   * @test_Strategy: Validate that the EL String literal with the: - '$' is
   *                 evaluated as expected. - '#' is evaluated as expected.
   */
  @Test
  public void elStringLiteralTest() throws Exception {

    boolean pass1, pass2;
    String expectedResult;
    String testString;

    Hashtable sMap = this.getStringTable();
    Enumeration keys = sMap.keys();

    while (keys.hasMoreElements()) {

      expectedResult = keys.nextElement().toString();
      testString = sMap.get(expectedResult).toString();

      // test "$" symbol
      try {
        pass1 = false;

        Object result = ExprEval.evaluateValueExpression(
            "${" + testString + "}", null, Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass1 = (ExprEval.compareClass(result, String.class)
            && ExprEval.compareValue((String) result, expectedResult));

      } catch (Exception e) {
        throw new Exception(e);
      }

      // Test "#" symbol
      try {
        pass2 = false;

        Object result = ExprEval.evaluateValueExpression(
            "#{" + testString + "}", null, Object.class);
        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass2 = (ExprEval.compareClass(result, String.class)
            && ExprEval.compareValue((String) result, expectedResult));

      } catch (Exception e) {
        throw new Exception(e);
      }

      if (!pass1)
        throw new Exception(
            "TEST FAILED: Literal String \"$\" evaluated" + " incorrectly.");
      if (!pass2)
        throw new Exception(
            "TEST FAILED: Literal String \"#\" evaluated" + " incorrectly.");
    }
  }

  /**
   * @testName: elNullLiteralTest
   * @assertion_ids: EL:SPEC:13.6
   * @test_Strategy: Validate that the EL 'null' literal evalutes correctly.
   */
  @Test
  public void elNullLiteralTest() throws Exception {

    boolean pass1, pass2;

    // Test "$" symbol
    try {
      Object result = ExprEval.evaluateValueExpression("${null}", null,
          Object.class);
      pass1 = (result == null) ? true : false;

    } catch (Exception e) {
      throw new Exception(e);
    }

    // Test "#" symbol

    try {
      Object result = ExprEval.evaluateValueExpression("#{null}", null,
          Object.class);
      pass2 = (result == null) ? true : false;

    } catch (Exception e) {
      throw new Exception(e);
    }

    if (!pass1)
      throw new Exception(
          "TEST FAILED: Literal 'null' \"$\" evaluated" + " incorrectly.");

    if (!pass2)
      throw new Exception(
          "TEST FAILED: Literal 'null \"#\" evaluated" + " incorrectly.");

  }

  /**
   * @testName: elSyntaxAsLiteralTest
   * @assertion_ids: EL:SPEC:7
   * @test_Strategy: [ELSyntaxAsLiteral] Verify that to generate literal values
   *                 that include the character sequence "${" or "#{" a
   *                 composite expression can be used.
   */
  @Test
  public void elSyntaxAsLiteralTest() throws Exception {
    boolean pass = false;

    // Hashtable layout.
    // key - expected value
    // value - test expression
    Hashtable testValues = new Hashtable();
    testValues.put("${foo}", "#{'${'}foo}");
    testValues.put("${foo}", "${'${'}foo}");
    testValues.put("#{foo}", "${'#{'}foo}");
    testValues.put("#{foo}", "#{'#{'}foo}");

    String exprStr;
    String expected;

    Set<String> tvalue = testValues.keySet();
    Iterator<String> itr = tvalue.iterator();

    while (itr.hasNext()) {
      expected = itr.next();
      exprStr = (String) testValues.get(expected);

      try {
        Object expr = ExprEval.evaluateValueExpression(exprStr, null,
            String.class);

        pass = (ExprEval.compareClass(expr, String.class)
            && ExprEval.compareValue(expr, expected));

      } catch (Exception e) {
        throw new Exception(e);
      }

      if (!pass)
        throw new Exception("TEST FAILED!");
    }
  }

  // ---------------------------------------------------------- Private
  // methods

  private List getFloatList() {
    List<Float> floatList = new ArrayList<Float>();
    floatList.add(new Float("8.1F"));
    floatList.add(new Float("-70.2F"));
    floatList.add(new Float("8.1e4F"));
    floatList.add(new Float("8.1E6F"));
    floatList.add(new Float("8.1e-9F"));
    floatList.add(new Float("8.1E+3F"));
    floatList.add(new Float("-.72F"));
    floatList.add(new Float(".999F"));
    floatList.add(new Float("-.1e1F"));
    floatList.add(new Float(".234E22F"));
    floatList.add(new Float("-.3444e-2F"));
    floatList.add(new Float(".5E+7F"));
    floatList.add(new Float("-1e1F"));
    floatList.add(new Float("234E2F"));
    floatList.add(new Float("-3444e-2F"));
    floatList.add(new Float("-3444e+2F"));

    return floatList;
  }

  private List getIntegerList() {
    List<Integer> integerList = new ArrayList<Integer>();
    integerList.add(1);
    integerList.add(-2);
    integerList.add(2147483647);
    integerList.add(-2147483647);

    return integerList;
  }

  /**
   * The returned Hashtable is in the form of: "Expected String" - "Test String"
   */
  private Hashtable getStringTable() {
    Hashtable testStrings = new Hashtable();
    testStrings.put("string", "'string'");
    testStrings.put("str\\ing", "'str\\\\ing'");
    testStrings.put("\"catstring\"", "'\"catstring\"'");
    testStrings.put("\'pullstring\'", "'\\\'pullstring\\\''");

    return testStrings;
  }
}
