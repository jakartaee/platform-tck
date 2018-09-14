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

package com.sun.ts.tests.el.spec.coercion;

import com.sun.ts.tests.el.common.util.ExprEval;
import com.sun.ts.tests.el.common.util.NameValuePair;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;

import javax.el.ELException;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Date;
import java.sql.Time;
import java.util.Properties;

public class ELClient extends ServiceEETest {

  private Properties testProps;

  private static final String NLINE = System.getProperty("line.separator",
      "\n");

  private static final String BYTE = "30";

  private static final String SHORT = "32";

  private static final String INTEGER = "33";

  private static final String LONG = "34";

  // NOTE: The elStringToNumberCoercionTest assumes a non-numeric
  // ending character for Float and Double types
  private static final String FLOAT = "35.5f";

  private static final String DOUBLE = "36.5d";

  private static final String BIGINT = "125";

  private static final String BIGDEC = "100.5";

  private static enum greeting {
    hello, goodbye
  };

  private Hashtable numberTable;

  private enum planets {
    MERCURY, VENUS, EARTH, MARS, JUPITER, SATURN, URANUS, NEPTUNE
    /** ,PLUTO */
  };

  public static void main(String[] args) {
    ELClient theTests = new ELClient();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  public void setup(String[] args, Properties p) throws Fault {
    TestUtil.logTrace("Setup method called");
    this.testProps = p;
    initializeNumberTable();
  }

  public void cleanup() throws Fault {
    // does nothing at this point
  }

  /**
   * @testName: positivePrimitiveToBoxedTest
   * @assertion_ids: EL:SPEC:37.3; EL:SPEC:37.5
   * @test_Strategy: Validate that the primitive types - boolean - char - byte -
   *                 short - int - long - float - double when found in an
   *                 expression are converted to the appropriate 'boxed' types.
   */
  public void positivePrimitiveToBoxedTest() throws Fault {

    boolean fail = false;
    boolean[] pass = { false, false, false, false, false, false, false, false };
    Object result = null;

    try {
      result = ExprEval.evaluateValueExpression("${true}", null, Boolean.class);
      pass[0] = ExprEval.compareClass(result, Boolean.class)
          && ExprEval.compareValue(result, Boolean.TRUE);

      result = ExprEval.evaluateValueExpression("#{'x'}", null,
          Character.class);
      pass[1] = ExprEval.compareClass(result, Character.class)
          && ExprEval.compareValue(result, Character.valueOf('x'));

      result = ExprEval.evaluateValueExpression("${2}", null, Byte.class);
      pass[2] = ExprEval.compareClass(result, Byte.class)
          && ExprEval.compareValue(result, Byte.valueOf("2"));

      result = ExprEval.evaluateValueExpression("#{20}", null, Short.class);
      pass[3] = ExprEval.compareClass(result, Short.class)
          && ExprEval.compareValue(result, Short.valueOf("20"));

      result = ExprEval.evaluateValueExpression("${2000}", null, Integer.class);
      pass[4] = ExprEval.compareClass(result, Integer.class)
          && ExprEval.compareValue(result, Integer.valueOf(2000));

      result = ExprEval.evaluateValueExpression("#{2000}", null, Long.class);
      pass[5] = ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue(result, Long.valueOf(2000L));

      result = ExprEval.evaluateValueExpression("${2e+3}", null, Float.class);
      pass[6] = ExprEval.compareClass(result, Float.class)
          && ExprEval.compareValue(result, Float.valueOf(2000f));

      result = ExprEval.evaluateValueExpression("#{2000.00}", null,
          Double.class);
      pass[7] = ExprEval.compareClass(result, Double.class)
          && ExprEval.compareValue(result, Double.valueOf(2000));

    } catch (RuntimeException re) {
      throw new Fault(re);
    } catch (Exception e) {
      throw new Fault(e);
    }

    for (int i = 0; i < pass.length; ++i) {
      if (!pass[i]) {
        fail = true;
        TestUtil.logErr("Unexpected result for test case " + i);
      }
    }

    if (fail)
      throw new Fault("TEST FAILED");
  }

  /**
   * @testName: positiveBoxedToPrimitiveTest
   * @assertion_ids: EL:SPEC:37.3; EL:SPEC:37.4
   * @test_Strategy: Validate that the 'boxed' types - Boolean - Character -
   *                 Byte - Short - Integer - Long - Float - Double when found
   *                 in an expression are converted to the appropriate primitive
   *                 types. Note that the conversion takes place in
   *                 ExprEval.evaluateValueExpression() when
   *                 ExpressionFactory.createValueExpression() is called. When
   *                 ValueExpression.getValue() is subsequently called, the
   *                 primitive type is converted back to its 'boxed' type.
   */
  public void positiveBoxedToPrimitiveTest() throws Fault {

    boolean fail = false;
    boolean[] pass = { false, false, false, false, false, false, false, false };
    Object result = null;

    String immExpr = "${A}";
    String defExpr = "#{A}";

    Boolean boolVal = Boolean.FALSE;
    NameValuePair[] boolNV = NameValuePair.buildUnaryNameValue(boolVal);

    Character charVal = Character.valueOf('x');
    NameValuePair[] charNV = NameValuePair.buildUnaryNameValue(charVal);

    Byte byteVal = Byte.valueOf("2");
    NameValuePair[] byteNV = NameValuePair.buildUnaryNameValue(byteVal);

    Short shortVal = Short.valueOf("20");
    NameValuePair[] shortNV = NameValuePair.buildUnaryNameValue(shortVal);

    Integer intVal = Integer.valueOf(2000);
    NameValuePair[] intNV = NameValuePair.buildUnaryNameValue(intVal);

    Long longVal = Long.valueOf(2000);
    NameValuePair[] longNV = NameValuePair.buildUnaryNameValue(longVal);

    Float floatVal = Float.valueOf("2e+3");
    NameValuePair[] floatNV = NameValuePair.buildUnaryNameValue(floatVal);

    Double doubleVal = Double.valueOf(2000.0);
    NameValuePair[] doubleNV = NameValuePair.buildUnaryNameValue(doubleVal);

    try {
      result = ExprEval.evaluateValueExpression(defExpr, boolNV, boolean.class);
      pass[0] = ExprEval.compareClass(result, Boolean.class)
          && ExprEval.compareValue(result, boolVal);

      result = ExprEval.evaluateValueExpression(immExpr, charNV, char.class);
      pass[1] = ExprEval.compareClass(result, Character.class)
          && ExprEval.compareValue(result, charVal);

      result = ExprEval.evaluateValueExpression(defExpr, byteNV, byte.class);
      pass[2] = ExprEval.compareClass(result, Byte.class)
          && ExprEval.compareValue(result, byteVal);

      result = ExprEval.evaluateValueExpression(immExpr, shortNV, short.class);
      pass[3] = ExprEval.compareClass(result, Short.class)
          && ExprEval.compareValue(result, shortVal);

      result = ExprEval.evaluateValueExpression(defExpr, intNV, int.class);
      pass[4] = ExprEval.compareClass(result, Integer.class)
          && ExprEval.compareValue(result, intVal);

      result = ExprEval.evaluateValueExpression(immExpr, longNV, long.class);
      pass[5] = ExprEval.compareClass(result, Long.class)
          && ExprEval.compareValue(result, longVal);

      result = ExprEval.evaluateValueExpression(defExpr, floatNV, float.class);
      pass[6] = ExprEval.compareClass(result, Float.class)
          && ExprEval.compareValue(result, floatVal);

      result = ExprEval.evaluateValueExpression(immExpr, doubleNV,
          double.class);
      pass[7] = ExprEval.compareClass(result, Double.class)
          && ExprEval.compareValue(result, doubleVal);

    } catch (RuntimeException re) {
      throw new Fault(re);
    } catch (Exception e) {
      throw new Fault(e);
    } finally {
      ExprEval.cleanup();
    }

    for (int i = 0; i < pass.length; ++i) {
      if (!pass[i]) {
        fail = true;
        TestUtil.logErr("Unexpected result for test case " + i);
      }
    }

    if (fail)
      throw new Fault("TEST FAILED");
  }

  /**
   * @testName: positiveElBooleanCoercionTest
   * @assertion_ids: EL:SPEC:41.1; EL:SPEC:41.2; EL:SPEC:41.3
   * @test_Strategy: Validate that null, the empty String, a Boolean, and a
   *                 proper String argument to Boolean.valueOf() are coerced to
   *                 the expected Boolean values.
   */
  public void positiveElBooleanCoercionTest() throws Fault {

    Class expectedClass = Boolean.class;
    boolean pass = false;

    try {
      Object result2 = ExprEval.evaluateValueExpression("#{''}", null,
          expectedClass);
      Object result3 = ExprEval.evaluateValueExpression(
          "${" + Boolean.TRUE + "}", null, expectedClass);
      Object result4 = ExprEval.evaluateValueExpression("#{'true'}", null,
          expectedClass);

      TestUtil.logTrace("result2 is " + result2.toString());
      TestUtil.logTrace("result3 is " + result3.toString());
      TestUtil.logTrace("result4 is " + result4.toString());

      pass = (ExprEval.compareClass(result2, expectedClass)
          && ExprEval.compareValue(result2, Boolean.FALSE)
          && ExprEval.compareClass(result3, expectedClass)
          && ExprEval.compareValue(result3, Boolean.TRUE)
          && ExprEval.compareClass(result4, expectedClass)
          && ExprEval.compareValue(result4, Boolean.TRUE));

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED: pass = false");
  }

  /**
   * @testName: negativeElBooleanCoercionTest
   * @assertion_ids: EL:SPEC:41.4; EL:JAVADOC:112
   * @test_Strategy: Validate that an error occurs when an attempt is made to
   *                 coerce an invalid class to a Boolean. The coercion is
   *                 performed with a call to ValueExpression.getValue(), which
   *                 must throw an ELException.
   */
  public void negativeElBooleanCoercionTest() throws Fault {

    boolean pass = false;

    try {
      ExprEval.evaluateValueExpression("${1}", null, Boolean.class);
      TestUtil.logErr(
          "No exception thrown when coercing invalid class " + "to Boolean");

    } catch (ELException ee) {
      pass = true;

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED: pass = false");
  }

  /**
   * @testName: elPrimitiveToStringCoercionTest
   * @assertion_ids: EL:SPEC:38.1; EL:SPEC:38.2; EL:SPEC:38.3; EL:SPEC:38.5
   * @test_Strategy: Validate that the following types coerce to type of String
   *                 and the expected String value is returned.
   * 
   *                 Types: String(), boolean, null, byte, char, short, int,
   *                 long, float, double, enum.
   * 
   */
  public void elPrimitiveToStringCoercionTest() throws Fault {

    boolean pass1, pass2, pass4, pass5, pass6, pass7, pass8, pass9, pass10,
        pass11 = false;

    Object result1, result2, result4, result5, result6, result7, result8,
        result9, result10, result11;

    Class expectedClass = String.class;

    String primString = "\"STRING\""; // result1
    boolean primBoolean = false; // result2
    // String nothing = null; // result3
    byte primByte = 0; // result4
    char primChar = '1'; // result5
    short primShort = 2; // result6
    int primInt = 3; // result7
    long primLong = 4L; // result8
    float primFloat = 5.5f; // result9
    double primDouble = 6.5d; // result10
    greeting morning; // result11
    morning = greeting.hello;

    try {
      // String to String
      result1 = ExprEval.evaluateValueExpression("${" + primString + "}", null,
          expectedClass);
      pass1 = (ExprEval.compareClass(result1, expectedClass)
          && ExprEval.compareValue(result1, "STRING"));

      // boolean to String
      result2 = ExprEval.evaluateValueExpression("#{" + primBoolean + "}", null,
          expectedClass);
      pass2 = (ExprEval.compareClass(result2, expectedClass)
          && ExprEval.compareValue(result2, "false"));

      // byte to String
      result4 = ExprEval.evaluateValueExpression("#{" + primByte + "}", null,
          expectedClass);
      pass4 = (ExprEval.compareClass(result4, expectedClass)
          && ExprEval.compareValue(result4, "0"));

      // char to String
      result5 = ExprEval.evaluateValueExpression("#{" + primChar + "}", null,
          expectedClass);
      pass5 = (ExprEval.compareClass(result5, expectedClass)
          && ExprEval.compareValue(result5, "1"));

      // short to String
      result6 = ExprEval.evaluateValueExpression("#{" + primShort + "}", null,
          expectedClass);
      pass6 = (ExprEval.compareClass(result6, expectedClass)
          && ExprEval.compareValue(result6, "2"));

      // int to String
      result7 = ExprEval.evaluateValueExpression("#{" + primInt + "}", null,
          expectedClass);
      pass7 = (ExprEval.compareClass(result7, expectedClass)
          && ExprEval.compareValue(result7, "3"));

      // long to String
      result8 = ExprEval.evaluateValueExpression("#{" + primLong + "}", null,
          expectedClass);
      pass8 = (ExprEval.compareClass(result8, expectedClass)
          && ExprEval.compareValue(result8, "4"));

      // float to String
      result9 = ExprEval.evaluateValueExpression("#{" + primFloat + "}", null,
          expectedClass);
      pass9 = (ExprEval.compareClass(result9, expectedClass)
          && ExprEval.compareValue(result9, "5.5"));

      // double to String
      result10 = ExprEval.evaluateValueExpression("#{" + primDouble + "}", null,
          expectedClass);
      pass10 = (ExprEval.compareClass(result10, expectedClass)
          && ExprEval.compareValue(result10, "6.5"));

      // enum to String
      result11 = ExprEval.evaluateValueExpression("#{'" + morning + "'}", null,
          expectedClass);

      pass11 = (ExprEval.compareClass(result11, expectedClass)
          && ExprEval.compareValue(result11, morning.name()));

    } catch (RuntimeException re) {
      throw new Fault(re);
    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass1)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "String Coerced to String Failed!" + NLINE
          + "See above for expected value." + NLINE);
    if (!pass2)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "boolean Coerced to String Failed!" + NLINE
          + "See above for expected value." + NLINE);
    if (!pass4)
      throw new Fault(
          "TEST FAILED: pass = false" + NLINE + "byte Coerced to String Failed!"
              + NLINE + "See above for expected value." + NLINE);
    if (!pass5)
      throw new Fault(
          "TEST FAILED: pass = false" + NLINE + "char Coerced to String Failed!"
              + NLINE + "See above for expected value." + NLINE);
    if (!pass6)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "short Coerced to String Failed!" + NLINE
          + "See above for expected value." + NLINE);
    if (!pass7)
      throw new Fault(
          "TEST FAILED: pass = false" + NLINE + "int Coerced to String Failed!"
              + NLINE + "See above for expected value." + NLINE);
    if (!pass8)
      throw new Fault(
          "TEST FAILED: pass = false" + NLINE + "long Coerced to String Failed!"
              + NLINE + "See above for expected value." + NLINE);
    if (!pass9)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "float Coerced to String Failed!" + NLINE
          + "See above for expected value." + NLINE);
    if (!pass10)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "double Coerced to String Failed!" + NLINE
          + "See above for expected value." + NLINE);
    if (!pass11)
      throw new Fault(
          "TEST FAILED: pass = false" + NLINE + "enum Coerced to String Failed!"
              + NLINE + "See above for expected value." + NLINE);
  }

  /**
   * @testName: elWrapperToStringCoercionTest
   * @assertion_ids: EL:SPEC:38.5
   * @test_Strategy: Validate that the following types coerce to type of String
   *                 and the expected String value is returned.
   * 
   *                 Types: Boolean, Byte, Character, Short, Integer, Long,
   *                 Float, Double.
   * 
   */
  public void elWrapperToStringCoercionTest() throws Fault {

    boolean pass1, pass2, pass3, pass4, pass5, pass6, pass7, pass8 = false;

    Object result1, result2, result3, result4, result5, result6, result7,
        result8;

    Class expectedClass = String.class;

    Boolean boo = false; // result1
    Byte bite = 0; // result2
    Character funny = '1'; // result3
    Short tee = 2; // result4
    Integer i = 3; // result5
    Long rope = 4L; // result6
    Float ter = 5.5f; // result7
    Double down = 6.5d; // result8

    try {
      // Boolean to String
      result1 = ExprEval.evaluateValueExpression("${" + boo + "}", null,
          expectedClass);
      pass1 = (ExprEval.compareClass(result1, expectedClass)
          && ExprEval.compareValue(result1, "false"));

      // Byte to String
      result2 = ExprEval.evaluateValueExpression("#{" + bite + "}", null,
          expectedClass);
      pass2 = (ExprEval.compareClass(result2, expectedClass)
          && ExprEval.compareValue(result2, "0"));

      // Character to String
      result3 = ExprEval.evaluateValueExpression("#{" + funny + "}", null,
          expectedClass);
      pass3 = (ExprEval.compareClass(result3, expectedClass)
          && ExprEval.compareValue(result3, "1"));

      // Short to String
      result4 = ExprEval.evaluateValueExpression("#{" + tee + "}", null,
          expectedClass);
      pass4 = (ExprEval.compareClass(result4, expectedClass)
          && ExprEval.compareValue(result4, "2"));

      // Integer to String
      result5 = ExprEval.evaluateValueExpression("#{" + i + "}", null,
          expectedClass);
      pass5 = (ExprEval.compareClass(result5, expectedClass)
          && ExprEval.compareValue(result5, "3"));

      // Long to String
      result6 = ExprEval.evaluateValueExpression("#{" + rope + "}", null,
          expectedClass);
      pass6 = (ExprEval.compareClass(result6, expectedClass)
          && ExprEval.compareValue(result6, "4"));

      // Float to String
      result7 = ExprEval.evaluateValueExpression("#{" + ter + "}", null,
          expectedClass);
      pass7 = (ExprEval.compareClass(result7, expectedClass)
          && ExprEval.compareValue(result7, "5.5"));

      // long to String
      result8 = ExprEval.evaluateValueExpression("#{" + down + "}", null,
          expectedClass);
      pass8 = (ExprEval.compareClass(result8, expectedClass)
          && ExprEval.compareValue(result8, "6.5"));

    } catch (RuntimeException re) {
      throw new Fault(re);
    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass1)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "String Coerced to String Failed!" + NLINE
          + "See above for expected value." + NLINE);
    if (!pass2)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "boolean Coerced to String Failed!" + NLINE
          + "See above for expected value." + NLINE);
    if (!pass3)
      throw new Fault(
          "TEST FAILED: pass = false" + NLINE + "null Coerced to String Failed!"
              + NLINE + "See above for expected value." + NLINE);
    if (!pass4)
      throw new Fault(
          "TEST FAILED: pass = false" + NLINE + "byte Coerced to String Failed!"
              + NLINE + "See above for expected value." + NLINE);
    if (!pass5)
      throw new Fault(
          "TEST FAILED: pass = false" + NLINE + "char Coerced to String Failed!"
              + NLINE + "See above for expected value." + NLINE);
    if (!pass6)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "short Coerced to String Failed!" + NLINE
          + "See above for expected value." + NLINE);
    if (!pass7)
      throw new Fault(
          "TEST FAILED: pass = false" + NLINE + "int Coerced to String Failed!"
              + NLINE + "See above for expected value." + NLINE);
    if (!pass8)
      throw new Fault(
          "TEST FAILED: pass = false" + NLINE + "long Coerced to String Failed!"
              + NLINE + "See above for expected value." + NLINE);
  }

  /**
   * @testName: positiveElCharacterCoercionTest
   * @assertion_ids: EL:SPEC:40.1; EL:SPEC:40.2; EL:SPEC:40.4; EL:SPEC:40.5
   * @test_Strategy: Validate that the following types coerce to type of
   *                 Character and the expected Character value is returned.
   * 
   *                 Types: String, Byte, Character, Short, Integer, Long,
   *                 Float, Double, null, empty String.
   * 
   */
  public void positiveElCharacterCoercionTest() throws Fault {

    boolean pass1, pass2, pass3, pass4, pass5, pass6, pass7, pass8,
        pass10 = false;

    Object result1, result2, result3, result4, result5, result6, result7,
        result8, result10;

    Class expectedClass = Character.class;

    Byte bite = 7;
    Character funny = '1';
    Short tee = 2;
    Integer i = 3;
    Long rope = 4L;
    Float ter = 5f;
    Double down = 6.5d;

    try {
      // String to Character
      result1 = ExprEval.evaluateValueExpression("${'STRING'}", null,
          expectedClass);
      pass1 = (ExprEval.compareClass(result1, expectedClass)
          && ExprEval.compareValue(result1, 'S'));

      // Byte to Character
      result2 = ExprEval.evaluateValueExpression("${" + bite + "}", null,
          expectedClass);
      pass2 = (ExprEval.compareClass(result2, expectedClass)
          && ExprEval.compareValue(result2, Character.valueOf((char) 7)));

      // Character to Character
      result3 = ExprEval.evaluateValueExpression("${" + funny + "}", null,
          expectedClass);
      pass3 = (ExprEval.compareClass(result3, expectedClass)
          && ExprEval.compareValue(result3, Character.valueOf((char) 1)));

      // Short to Character
      result4 = ExprEval.evaluateValueExpression("${" + tee + "}", null,
          expectedClass);
      pass4 = (ExprEval.compareClass(result4, expectedClass)
          && ExprEval.compareValue(result4, Character.valueOf((char) 2)));

      // Integer to Character
      result5 = ExprEval.evaluateValueExpression("${" + i + "}", null,
          expectedClass);
      pass5 = (ExprEval.compareClass(result5, expectedClass)
          && ExprEval.compareValue(result5, Character.valueOf((char) 3)));

      // Long to Character
      result6 = ExprEval.evaluateValueExpression("${" + rope + "}", null,
          expectedClass);
      pass6 = (ExprEval.compareClass(result6, expectedClass)
          && ExprEval.compareValue(result6, Character.valueOf((char) 4)));

      // Float to Character
      result7 = ExprEval.evaluateValueExpression("${" + ter + "}", null,
          expectedClass);
      pass7 = (ExprEval.compareClass(result7, expectedClass)
          && ExprEval.compareValue(result7, Character.valueOf((char) 5)));

      // Double to Character
      result8 = ExprEval.evaluateValueExpression("${" + down + "}", null,
          expectedClass);
      pass8 = (ExprEval.compareClass(result8, expectedClass)
          && ExprEval.compareValue(result8, Character.valueOf((char) 6)));

      // empty String to Character
      result10 = ExprEval.evaluateValueExpression("${''}", null, expectedClass);
      pass10 = (ExprEval.compareClass(result10, expectedClass)
          && ExprEval.compareValue(result10, Character.valueOf((char) 0)));

    } catch (RuntimeException re) {
      throw new Fault(re);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(e);
    }

    if (!pass1)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "String Coerced to Character Failed!" + NLINE
          + "See above for expected value." + NLINE);
    if (!pass2)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "Byte Coerced to Character Failed!" + NLINE
          + "See above for expected value." + NLINE);
    if (!pass3)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "Character Coerced to Character Failed!" + NLINE
          + "See above for expected value." + NLINE);
    if (!pass4)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "Short Coerced to Character Failed!" + NLINE
          + "See above for expected value." + NLINE);
    if (!pass5)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "Integer Coerced to Character Failed!" + NLINE
          + "See above for expected value." + NLINE);
    if (!pass6)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "Long Coerced to Character Failed!" + NLINE
          + "See above for expected value." + NLINE);
    if (!pass7)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "Float Coerced to Character Failed!" + NLINE
          + "See above for expected value." + NLINE);
    if (!pass8)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "Double Coerced to Character Failed!" + NLINE
          + "See above for expected value." + NLINE);
    if (!pass10)
      throw new Fault("TEST FAILED: pass = false" + NLINE
          + "Empty String Coerced to Character Failed!" + NLINE
          + "See above for expected value." + NLINE);

  }

  /**
   * @testName: negativeElCharacterCoercionTest
   * @assertion_ids: EL:SPEC:40.3; EL:JAVADOC:112
   * @test_Strategy: Validate that an error occurs when an attempt is made to
   *                 coerce a boolean to Character. The coercion is performed
   *                 with a call to ValueExpression.getValue(), which must throw
   *                 an ELException.
   */
  public void negativeElCharacterCoercionTest() throws Fault {

    boolean pass = false;

    try {
      ExprEval.evaluateValueExpression("${" + pass + "}", null,
          Character.class);
      TestUtil.logErr(
          "No exception thrown when coercing Boolean " + "to Character!");

    } catch (ELException ee) {
      pass = true;

    } catch (Exception e) {
      TestUtil.logErr("Exception thrown, but it was not an ELException");
      TestUtil.printStackTrace(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED: pass = false");
  }

  /**
   * @testName: negativeElNumberCoercionTest
   * @assertion_ids: EL:SPEC:39.3; EL:SPEC:39.6.1.1; EL:SPEC:39.7.3;
   *                 EL:SPEC:39.7.1; EL:SPEC:39.6.1.1; EL:JAVADOC:112
   * @test_Strategy: Validate that an error occurs when an attempt is made to
   *                 coerce a: -Boolean to Number. -String to a Number. The
   *                 coercion is performed with a call to
   *                 ValueExpression.getValue(), which must throw an
   *                 ELException.
   */
  public void negativeElNumberCoercionTest() throws Fault {

    boolean pass;
    Enumeration keys = numberTable.keys();
    String name;
    Class testClass;

    while (keys.hasMoreElements()) {
      pass = false;
      testClass = (Class) keys.nextElement();
      name = testClass.getSimpleName();

      // Coercing Boolean to Number type.
      try {
        ExprEval.evaluateValueExpression("${" + pass + "}", null, testClass);
        TestUtil.logErr("No exception thrown when coercing Boolean " + "to "
            + name + "!" + NLINE);

      } catch (ELException ee) {
        pass = true;

      } catch (Exception e) {
        TestUtil.logErr("Exception thrown, but it was not an ELException");
        TestUtil.printStackTrace(e);
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");

      // Coercing String to Number type.
      try {
        ExprEval.evaluateValueExpression("${'A'}", null, testClass);
        TestUtil.logErr("No exception thrown when coercing String " + "to "
            + name + "!" + NLINE);

      } catch (ELException ee) {
        pass = true;

      } catch (Exception e) {
        TestUtil.logErr("Exception thrown, but it was not an ELException");
        TestUtil.printStackTrace(e);
      }

      if (!pass)
        throw new Fault("TEST FAILED: pass = false");
    }

  }

  /**
   * @testName: elNullToNumberCoercionTest
   * @assertion_ids: EL:SPEC:39.1
   * @test_Strategy: Validate that when null or empty String is given. That the
   *                 returned value is 0. Test this for the Following types.
   * 
   *                 Types: Byte, Short, Integer, Long, Float, Double,
   *                 BigDecimal, BigInteger.
   */
  public void elNullToNumberCoercionTest() throws Fault {

    boolean pass1;
    Object result1;
    Enumeration keys = numberTable.keys();
    String name;
    Class testClass;
    Object expectedValue;

    while (keys.hasMoreElements()) {
      pass1 = false;
      testClass = (Class) keys.nextElement();
      name = testClass.getSimpleName();

      if ("Byte".equals(name)) {
        expectedValue = Byte.valueOf((byte) 0);
      } else if ("Short".equals(name)) {
        expectedValue = Short.valueOf((short) 0);
      } else if ("Integer".equals(name)) {
        expectedValue = Integer.valueOf(0);
      } else if ("Long".equals(name)) {
        expectedValue = Long.valueOf(0L);
      } else if ("Float".equals(name)) {
        expectedValue = Float.valueOf(0f);
      } else if ("Double".equals(name)) {
        expectedValue = Double.valueOf(0d);
      } else if ("BigInteger".equals(name)) {
        expectedValue = BigInteger.valueOf(0);
      } else if ("BigDecimal".equals(name)) {
        expectedValue = BigDecimal.valueOf(0);
      } else {
        // Default Value, this should never been utilized.
        expectedValue = "0";
      }

      try {
        // Coercing Empty String to Number type.
        result1 = ExprEval.evaluateValueExpression("${''}", null, testClass);
        pass1 = (ExprEval.compareClass(result1, testClass)
            && ExprEval.compareValue(result1, expectedValue));
        if (!pass1)
          throw new Fault("TEST FAILED: pass = false" + NLINE
              + "Empty String Coerced to " + name + " Failed!" + NLINE
              + "See above for expected value." + NLINE);

      } catch (Exception e) {
        throw new Fault(e);
      }
    }
  }

  /**
   * @testName: elNumberToByteCoercionTest
   * @assertion_ids: EL:SPEC:39.5.3
   * @test_Strategy: Validate that following Number types coerce to Byte and the
   *                 expected Byte value is returned.
   * 
   *                 Types: Byte, Short, Integer, Long, Float, Double,
   *                 BigDecimal, BigInteger.
   * 
   */
  public void elNumberToByteCoercionTest() throws Fault {

    boolean pass;
    Object result, testValue;
    Enumeration keys = numberTable.keys();
    Class expectedClass = Byte.class;
    Byte expectedValue;
    Class testClass;
    String name;

    while (keys.hasMoreElements()) {
      pass = false;
      testClass = (Class) keys.nextElement();
      name = testClass.getSimpleName();

      if ("Byte".equals(name)) {
        testValue = (Byte) numberTable.get(testClass);
        expectedValue = Byte.valueOf(BYTE);
      } else if ("Short".equals(name)) {
        testValue = (Short) numberTable.get(testClass);
        expectedValue = Byte.valueOf(SHORT);
      } else if ("Integer".equals(name)) {
        testValue = (Integer) numberTable.get(testClass);
        expectedValue = Byte.valueOf(INTEGER);
      } else if ("Long".equals(name)) {
        testValue = (Long) numberTable.get(testClass);
        expectedValue = Byte.valueOf(LONG);
      } else if ("Float".equals(name)) {
        testValue = (Float) numberTable.get(testClass);
        expectedValue = Byte.valueOf((Float.valueOf(FLOAT)).byteValue());
      } else if ("Double".equals(name)) {
        testValue = (Double) numberTable.get(testClass);
        expectedValue = Byte.valueOf((Double.valueOf(DOUBLE)).byteValue());
      } else if ("BigInteger".equals(name)) {
        testValue = (BigInteger) numberTable.get(testClass);
        expectedValue = Byte.valueOf(BIGINT);
      } else if ("BigDecimal".equals(name)) {
        testValue = (BigDecimal) numberTable.get(testClass);
        expectedValue = Byte.valueOf((Double.valueOf(BIGDEC)).byteValue());
      } else {
        // Default Values, this should never been utilized.
        testValue = "0";
        expectedValue = Byte.valueOf("1");
      }

      try {
        // Coercing Numbers to Byte type.
        result = ExprEval.evaluateValueExpression("${" + testValue + "}", null,
            expectedClass);
        pass = (ExprEval.compareClass(result, expectedClass)
            && ExprEval.compareValue(result, expectedValue));
        if (!pass)
          throw new Fault("TEST FAILED: pass = false" + NLINE + name
              + " Coerced to Byte Failed!" + NLINE
              + "See above for expected value." + NLINE);

      } catch (Exception e) {
        throw new Fault(e);
      }
    }

  }

  /**
   * @testName: elNumberToShortCoercionTest
   * @assertion_ids: EL:SPEC:39.5.4
   * @test_Strategy: Validate that following Number types coerce to Short and
   *                 the expected Short value is returned.
   * 
   *                 Types: Byte, Short, Integer, Long, Float, Double,
   *                 BigDecimal, BigInteger.
   * 
   */
  public void elNumberToShortCoercionTest() throws Fault {

    boolean pass;
    Object result, testValue;
    Enumeration keys = numberTable.keys();
    Class expectedClass = Short.class;
    Short expectedValue;
    Class testClass;
    String name;

    while (keys.hasMoreElements()) {
      pass = false;
      testClass = (Class) keys.nextElement();
      name = testClass.getSimpleName();

      if ("Byte".equals(name)) {
        testValue = (Byte) numberTable.get(testClass);
        expectedValue = Short.valueOf(BYTE);
      } else if ("Short".equals(name)) {
        testValue = (Short) numberTable.get(testClass);
        expectedValue = Short.valueOf(SHORT);
      } else if ("Integer".equals(name)) {
        testValue = (Integer) numberTable.get(testClass);
        expectedValue = Short.valueOf(INTEGER);
      } else if ("Long".equals(name)) {
        testValue = (Long) numberTable.get(testClass);
        expectedValue = Short.valueOf(LONG);
      } else if ("Float".equals(name)) {
        testValue = (Float) numberTable.get(testClass);
        expectedValue = Short.valueOf((Float.valueOf(FLOAT)).shortValue());
      } else if ("Double".equals(name)) {
        testValue = (Double) numberTable.get(testClass);
        expectedValue = Short.valueOf((Double.valueOf(DOUBLE)).shortValue());
      } else if ("BigInteger".equals(name)) {
        testValue = (BigInteger) numberTable.get(testClass);
        expectedValue = Short.valueOf(BIGINT);
      } else if ("BigDecimal".equals(name)) {
        testValue = (BigDecimal) numberTable.get(testClass);
        expectedValue = Short.valueOf((Double.valueOf(BIGDEC)).shortValue());
      } else {
        // Default Values, this should never been utilized.
        testValue = "0";
        expectedValue = Short.valueOf("1");
      }

      try {
        // Coercing Number to Short type.
        result = ExprEval.evaluateValueExpression("${" + testValue + "}", null,
            expectedClass);
        pass = (ExprEval.compareClass(result, expectedClass)
            && ExprEval.compareValue(result, expectedValue));
        if (!pass)
          throw new Fault("TEST FAILED: pass = false" + NLINE + name
              + " Coerced to Short Failed!" + NLINE
              + "See above for expected value." + NLINE);

      } catch (Exception e) {
        throw new Fault(e);
      }
    }

  }

  /**
   * @testName: elNumberToIntegerCoercionTest
   * @assertion_ids: EL:SPEC:39.5.5
   * @test_Strategy: Validate that following Number types coerce to Integer and
   *                 the expected Integer value is returned.
   * 
   *                 Types: Byte, Short, Integer, Long, Float, Double,
   *                 BigDecimal, BigInteger.
   * 
   */
  public void elNumberToIntegerCoercionTest() throws Fault {

    boolean pass;
    Object result, testValue;
    Enumeration keys = numberTable.keys();
    Class expectedClass = Integer.class;
    Integer expectedValue;
    Class testClass;
    String name;

    while (keys.hasMoreElements()) {
      pass = false;
      testClass = (Class) keys.nextElement();
      name = testClass.getSimpleName();

      if ("Byte".equals(name)) {
        testValue = (Byte) numberTable.get(testClass);
        expectedValue = Integer.valueOf(BYTE);
      } else if ("Short".equals(name)) {
        testValue = (Short) numberTable.get(testClass);
        expectedValue = Integer.valueOf(SHORT);
      } else if ("Integer".equals(name)) {
        testValue = (Integer) numberTable.get(testClass);
        expectedValue = Integer.valueOf(INTEGER);
      } else if ("Long".equals(name)) {
        testValue = (Long) numberTable.get(testClass);
        expectedValue = Integer.valueOf(LONG);
      } else if ("Float".equals(name)) {
        testValue = (Float) numberTable.get(testClass);
        expectedValue = Integer.valueOf(Float.valueOf(FLOAT).intValue());
      } else if ("Double".equals(name)) {
        testValue = (Double) numberTable.get(testClass);
        expectedValue = Integer.valueOf(Double.valueOf(DOUBLE).intValue());
      } else if ("BigInteger".equals(name)) {
        testValue = (BigInteger) numberTable.get(testClass);
        expectedValue = Integer.valueOf(BIGINT);
      } else if ("BigDecimal".equals(name)) {
        testValue = (BigDecimal) numberTable.get(testClass);
        expectedValue = Integer.valueOf(Double.valueOf(BIGDEC).intValue());
      } else {
        // Default Values, this should never been utilized.
        testValue = "0";
        expectedValue = Integer.valueOf("1");
      }

      try {
        // Coercing Number to Integer type.
        result = ExprEval.evaluateValueExpression("${" + testValue + "}", null,
            expectedClass);
        pass = (ExprEval.compareClass(result, expectedClass)
            && ExprEval.compareValue(result, expectedValue));
        if (!pass)
          throw new Fault("TEST FAILED: pass = false" + NLINE + name
              + " Coerced to Integer Failed!" + NLINE
              + "See above for expected value." + NLINE);

      } catch (Exception e) {
        throw new Fault(e);
      }
    }
  }

  /**
   * @testName: elNumberToLongCoercionTest
   * @assertion_ids: EL:SPEC:39.5.6
   * @test_Strategy: Validate that following Number types coerce to Long and the
   *                 expected Long value is returned.
   * 
   *                 Types: Byte, Short, Integer, Long, Float, Double,
   *                 BigDecimal, BigInteger.
   * 
   */
  public void elNumberToLongCoercionTest() throws Fault {

    boolean pass;
    Object result, testValue;
    Enumeration keys = numberTable.keys();
    Class expectedClass = Long.class;
    Long expectedValue;
    Class testClass;
    String name;

    while (keys.hasMoreElements()) {
      pass = false;
      testClass = (Class) keys.nextElement();
      name = testClass.getSimpleName();

      if ("Byte".equals(name)) {
        testValue = (Byte) numberTable.get(testClass);
        expectedValue = Long.valueOf(BYTE);
      } else if ("Short".equals(name)) {
        testValue = (Short) numberTable.get(testClass);
        expectedValue = Long.valueOf(SHORT);
      } else if ("Integer".equals(name)) {
        testValue = (Integer) numberTable.get(testClass);
        expectedValue = Long.valueOf(INTEGER);
      } else if ("Long".equals(name)) {
        testValue = (Long) numberTable.get(testClass);
        expectedValue = Long.valueOf(LONG);
      } else if ("Float".equals(name)) {
        testValue = (Float) numberTable.get(testClass);
        expectedValue = Long.valueOf(Float.valueOf(FLOAT).longValue());
      } else if ("Double".equals(name)) {
        testValue = (Double) numberTable.get(testClass);
        expectedValue = Long.valueOf(Double.valueOf(DOUBLE).longValue());
      } else if ("BigInteger".equals(name)) {
        testValue = (BigInteger) numberTable.get(testClass);
        expectedValue = Long.valueOf(BIGINT);
      } else if ("BigDecimal".equals(name)) {
        testValue = (BigDecimal) numberTable.get(testClass);
        expectedValue = Long.valueOf(Double.valueOf(BIGDEC).longValue());
      } else {
        // Default Values, this should never been utilized.
        testValue = "0";
        expectedValue = Long.valueOf("1");
      }

      try {
        // Coercing Number to Long type.
        result = ExprEval.evaluateValueExpression("${" + testValue + "}", null,
            expectedClass);
        pass = (ExprEval.compareClass(result, expectedClass)
            && ExprEval.compareValue(result, expectedValue));
        if (!pass)
          throw new Fault("TEST FAILED: pass = false" + NLINE + name
              + " Coerced to Long Failed!" + NLINE
              + "See above for expected value." + NLINE);

      } catch (Exception e) {
        throw new Fault(e);
      }
    }

  }

  /**
   * @testName: elNumberToFloatCoercionTest
   * @assertion_ids: EL:SPEC:39.5.7
   * @test_Strategy: Validate that following Number types coerce to Float and
   *                 the expected Float value is returned.
   * 
   *                 Types: Byte, Short, Integer, Long, Float, Double,
   *                 BigDecimal, BigInteger.
   * 
   */
  public void elNumberToFloatCoercionTest() throws Fault {

    boolean pass;
    Object result, testValue;
    Enumeration keys = numberTable.keys();
    Class expectedClass = Float.class;
    Float expectedValue;
    Class testClass;
    String name;

    while (keys.hasMoreElements()) {
      pass = false;
      testClass = (Class) keys.nextElement();
      name = testClass.getSimpleName();

      if ("Byte".equals(name)) {
        testValue = (Byte) numberTable.get(testClass);
        expectedValue = Float.valueOf(BYTE);
      } else if ("Short".equals(name)) {
        testValue = (Short) numberTable.get(testClass);
        expectedValue = Float.valueOf(SHORT);
      } else if ("Integer".equals(name)) {
        testValue = (Integer) numberTable.get(testClass);
        expectedValue = Float.valueOf(INTEGER);
      } else if ("Long".equals(name)) {
        testValue = (Long) numberTable.get(testClass);
        expectedValue = Float.valueOf(LONG);
      } else if ("Float".equals(name)) {
        testValue = (Float) numberTable.get(testClass);
        expectedValue = Float.valueOf(FLOAT);
      } else if ("Double".equals(name)) {
        testValue = (Double) numberTable.get(testClass);
        expectedValue = Float.valueOf(DOUBLE);
      } else if ("BigInteger".equals(name)) {
        testValue = (BigInteger) numberTable.get(testClass);
        expectedValue = Float.valueOf(BIGINT);
      } else if ("BigDecimal".equals(name)) {
        testValue = (BigDecimal) numberTable.get(testClass);
        expectedValue = Float.valueOf(BIGDEC);
      } else {
        // Default Values, this should never been utilized.
        testValue = "0";
        expectedValue = Float.valueOf("1");
      }

      try {
        // Coercing Number to Float type.
        result = ExprEval.evaluateValueExpression("${" + testValue + "}", null,
            expectedClass);
        pass = (ExprEval.compareClass(result, expectedClass)
            && ExprEval.compareValue((Float) result, expectedValue, 2));
        if (!pass)
          throw new Fault("TEST FAILED: pass = false" + NLINE + name
              + " Coerced to Float Failed!" + NLINE
              + "See above for expected value." + NLINE);

      } catch (Exception e) {
        throw new Fault(e);
      }
    }

  }

  /**
   * @testName: elNumberToDoubleCoercionTest
   * @assertion_ids: EL:SPEC:39.5.8
   * @test_Strategy: Validate that following Number types coerce to Double and
   *                 the expected Double value is returned.
   * 
   *                 Types: Byte, Short, Integer, Long, Float, Double,
   *                 BigDecimal, BigInteger.
   * 
   */
  public void elNumberToDoubleCoercionTest() throws Fault {

    boolean pass;
    Object result, testValue;
    Enumeration keys = numberTable.keys();
    Class expectedClass = Double.class;
    Double expectedValue;
    Class testClass;
    String name;

    while (keys.hasMoreElements()) {
      pass = false;
      testClass = (Class) keys.nextElement();
      name = testClass.getSimpleName();

      if ("Byte".equals(name)) {
        testValue = (Byte) numberTable.get(testClass);
        expectedValue = Double.valueOf(BYTE);
      } else if ("Short".equals(name)) {
        testValue = (Short) numberTable.get(testClass);
        expectedValue = Double.valueOf(SHORT);
      } else if ("Integer".equals(name)) {
        testValue = (Integer) numberTable.get(testClass);
        expectedValue = Double.valueOf(INTEGER);
      } else if ("Long".equals(name)) {
        testValue = (Long) numberTable.get(testClass);
        expectedValue = Double.valueOf(LONG);
      } else if ("Float".equals(name)) {
        testValue = (Float) numberTable.get(testClass);
        expectedValue = Double.valueOf(FLOAT);
      } else if ("Double".equals(name)) {
        testValue = (Double) numberTable.get(testClass);
        expectedValue = Double.valueOf(DOUBLE);
      } else if ("BigInteger".equals(name)) {
        testValue = (BigInteger) numberTable.get(testClass);
        expectedValue = Double.valueOf(BIGINT);
      } else if ("BigDecimal".equals(name)) {
        testValue = (BigDecimal) numberTable.get(testClass);
        expectedValue = Double.valueOf(BIGDEC);
      } else {
        // Default Values, this should never been utilized.
        testValue = "0";
        expectedValue = Double.valueOf("1");
      }

      try {
        // Coercing Number to Double type.
        result = ExprEval.evaluateValueExpression("${" + testValue + "}", null,
            expectedClass);
        pass = (ExprEval.compareClass(result, expectedClass)
            && ExprEval.compareValue(result, expectedValue));
        if (!pass)
          throw new Fault("TEST FAILED: pass = false" + NLINE + name
              + " Coerced to Double Failed!" + NLINE
              + "See above for expected value." + NLINE);

      } catch (Exception e) {
        throw new Fault(e);
      }
    }

  }

  /**
   * @testName: elStringToNumberCoercionTest
   * @assertion_ids: EL:SPEC:39.6.1.2; EL:SPEC:39.7.2; EL:SPEC:39.7.4
   * @test_Strategy: Validate that String types coerce to the following types
   *                 and the expected value is returned.
   * 
   *                 Types: Byte, Short, Integer, Long, Float, Double,
   *                 BigDecimal, BigInteger.
   * 
   */
  public void elStringToNumberCoercionTest() throws Fault {

    boolean pass;
    Object result, expectedValue;
    Enumeration keys = numberTable.keys();
    Class expectedClass;
    String name, testValue;

    while (keys.hasMoreElements()) {
      pass = false;
      expectedClass = (Class) keys.nextElement();
      name = expectedClass.getSimpleName();

      if ("Byte".equals(name)) {
        testValue = BYTE;
        expectedValue = Byte.valueOf(BYTE);
      } else if ("Short".equals(name)) {
        testValue = SHORT;
        expectedValue = Short.valueOf(SHORT);
      } else if ("Integer".equals(name)) {
        testValue = INTEGER;
        expectedValue = Integer.valueOf(INTEGER);
      } else if ("Long".equals(name)) {
        testValue = LONG;
        expectedValue = Long.valueOf(LONG);
      } else if ("Float".equals(name)) {

        // remove non-numeric char
        testValue = FLOAT.substring(0, FLOAT.length() - 1);
        expectedValue = Float.valueOf(FLOAT);
      } else if ("Double".equals(name)) {
        testValue = DOUBLE.substring(0, DOUBLE.length() - 1);
        expectedValue = Double.valueOf(DOUBLE);
      } else if ("BigInteger".equals(name)) {
        testValue = BIGINT;
        expectedValue = BigInteger.valueOf(Long.valueOf(BIGINT));
      } else if ("BigDecimal".equals(name)) {
        testValue = BIGDEC;
        expectedValue = BigDecimal.valueOf(Double.valueOf(BIGDEC));
      } else {
        // Default Values, this should never been utilized.
        testValue = "0";
        expectedValue = "1";
      }

      try {
        // Coercing String to Number types.
        result = ExprEval.evaluateValueExpression("${'" + testValue + "'}",
            null, expectedClass);
        pass = (ExprEval.compareClass(result, expectedClass)
            && ExprEval.compareValue(result, expectedValue));

        if (!pass)
          throw new Fault("TEST FAILED: pass = false" + NLINE + "Coerced to "
              + name + " Failed!" + NLINE + "See above for expected value."
              + NLINE);

      } catch (Exception e) {
        throw new Fault(e);
      }
    }

  }

  /**
   * @testName: elCharacterToNumberCoercionTest
   * @assertion_ids: EL:SPEC:39.2
   * @test_Strategy: Validate that Character types coerce to the following types
   *                 and the expected value is returned.
   * 
   *                 Types: Byte, Short, Integer, Long, Float, Double,
   *                 BigDecimal, BigInteger.
   * 
   */
  public void elCharacterToNumberCoercionTest() throws Fault {

    boolean pass;
    Object result, expectedValue;
    Enumeration keys = numberTable.keys();
    Class expectedClass;
    Character testValue;
    String name;

    while (keys.hasMoreElements()) {
      pass = false;
      expectedClass = (Class) keys.nextElement();
      name = expectedClass.getSimpleName();

      if ("Byte".equals(name)) {
        testValue = '1';
        expectedValue = Byte.valueOf("1");
      } else if ("Short".equals(name)) {
        testValue = '2';
        expectedValue = Short.valueOf("2");
      } else if ("Integer".equals(name)) {
        testValue = '3';
        expectedValue = Integer.valueOf(3);
      } else if ("Long".equals(name)) {
        testValue = '4';
        expectedValue = Long.valueOf(4L);
      } else if ("Float".equals(name)) {
        testValue = '5';
        expectedValue = Float.valueOf(5f);
      } else if ("Double".equals(name)) {
        testValue = '6';
        expectedValue = Double.valueOf(6d);
      } else if ("BigInteger".equals(name)) {
        testValue = '7';
        expectedValue = BigInteger.valueOf(7);
      } else if ("BigDecimal".equals(name)) {
        testValue = '8';
        expectedValue = BigDecimal.valueOf(8);
      } else {
        // Default Values, this should never been utilized.
        testValue = '0';
        expectedValue = '1';
      }

      try {
        // Coercing Character to Number types.
        result = ExprEval.evaluateValueExpression("${'" + testValue + "'}",
            null, expectedClass);
        pass = (ExprEval.compareClass(result, expectedClass)
            && ExprEval.compareValue(result, expectedValue));

        if (!pass)
          throw new Fault("TEST FAILED: pass = false" + NLINE + "Coerced to "
              + name + " Failed!" + NLINE + "See above for expected value."
              + NLINE);

      } catch (Exception e) {
        throw new Fault(e);
      }
    }

  }

  /**
   * @testName: elNumberToBigIntegerCoercionTest
   * @assertion_ids: EL:SPEC:39.5.1.1; EL:SPEC:39.5.1.2
   * @test_Strategy: Validate that following Number types coerce to BigInteger
   *                 and the expected value is returned.
   * 
   *                 Types: Byte, Short, Integer, Long, Float, Double,
   *                 BigDecimal, BigInteger.
   * 
   */
  public void elNumberToBigIntegerCoercionTest() throws Fault {

    boolean pass;
    Object result, testValue;
    Enumeration keys = numberTable.keys();
    Class expectedClass = BigInteger.class;
    BigInteger expectedValue;
    Class testClass;
    String name;

    while (keys.hasMoreElements()) {
      pass = false;
      testClass = (Class) keys.nextElement();
      name = testClass.getSimpleName();

      if ("Byte".equals(name)) {
        testValue = (Byte) numberTable.get(testClass);
        expectedValue = BigInteger.valueOf(Long.valueOf(BYTE));
      } else if ("Short".equals(name)) {
        testValue = (Short) numberTable.get(testClass);
        expectedValue = BigInteger.valueOf(Long.valueOf(SHORT));
      } else if ("Integer".equals(name)) {
        testValue = (Integer) numberTable.get(testClass);
        expectedValue = BigInteger.valueOf(Long.valueOf(INTEGER));
      } else if ("Long".equals(name)) {
        testValue = (Long) numberTable.get(testClass);
        expectedValue = BigInteger.valueOf(Long.valueOf(LONG));
      } else if ("Float".equals(name)) {
        testValue = (Float) numberTable.get(testClass);
        expectedValue = BigInteger.valueOf(Float.valueOf(FLOAT).longValue());
      } else if ("Double".equals(name)) {
        testValue = (Double) numberTable.get(testClass);
        expectedValue = BigInteger.valueOf(Double.valueOf(DOUBLE).longValue());
      } else if ("BigInteger".equals(name)) {
        testValue = (BigInteger) numberTable.get(testClass);
        expectedValue = BigInteger.valueOf(Long.valueOf(BIGINT));
      } else if ("BigDecimal".equals(name)) {
        testValue = (BigDecimal) numberTable.get(testClass);
        expectedValue = BigDecimal.valueOf(Double.valueOf(BIGDEC))
            .toBigInteger();
      } else {
        // Default Values, this should never been utilized.
        testValue = "0";
        expectedValue = BigInteger.valueOf(Long.valueOf("1"));
      }

      try {
        // Coercing Number to BigInteger type.
        result = ExprEval.evaluateValueExpression("${" + testValue + "}", null,
            expectedClass);
        pass = (ExprEval.compareClass(result, expectedClass)
            && ExprEval.compareValue((BigInteger) result, expectedValue, 1));
        if (!pass)
          throw new Fault("TEST FAILED: pass = false" + NLINE + name
              + " Coerced to BigInteger Failed!" + NLINE
              + "See above for expected value." + NLINE);

      } catch (Exception e) {
        throw new Fault(e);
      }
    }

  }

  /**
   * @testName: elNumberToBigDecimalCoercionTest
   * @assertion_ids: EL:SPEC:39.5.2.1; EL:SPEC:39.5.2.2
   * @test_Strategy: Validate that following Number types coerce to BigDecimal
   *                 and the expected value is returned.
   * 
   *                 Types: Byte, Short, Integer, Long, Float, Double,
   *                 BigDecimal, BigInteger.
   * 
   */
  public void elNumberToBigDecimalCoercionTest() throws Fault {

    boolean pass;
    Object result, testValue;
    Enumeration keys = numberTable.keys();
    Class expectedClass = BigDecimal.class;
    BigDecimal expectedValue;
    Class testClass;
    String name;

    while (keys.hasMoreElements()) {
      pass = false;
      testClass = (Class) keys.nextElement();
      name = testClass.getSimpleName();

      if ("Byte".equals(name)) {
        testValue = (Byte) numberTable.get(testClass);
        expectedValue = BigDecimal.valueOf(Double.valueOf(BYTE));
      } else if ("Short".equals(name)) {
        testValue = (Short) numberTable.get(testClass);
        expectedValue = BigDecimal.valueOf(Double.valueOf(SHORT));
      } else if ("Integer".equals(name)) {
        testValue = (Integer) numberTable.get(testClass);
        expectedValue = BigDecimal.valueOf(Double.valueOf(INTEGER));
      } else if ("Long".equals(name)) {
        testValue = (Long) numberTable.get(testClass);
        expectedValue = BigDecimal.valueOf(Double.valueOf(LONG));
      } else if ("Float".equals(name)) {
        testValue = (Float) numberTable.get(testClass);
        expectedValue = BigDecimal.valueOf(Float.valueOf(FLOAT).doubleValue());
      } else if ("Double".equals(name)) {
        testValue = (Double) numberTable.get(testClass);
        expectedValue = BigDecimal.valueOf(Double.valueOf(DOUBLE));
      } else if ("BigInteger".equals(name)) {
        testValue = (BigInteger) numberTable.get(testClass);
        expectedValue = BigDecimal.valueOf(Double.valueOf(BIGINT));
      } else if ("BigDecimal".equals(name)) {
        testValue = (BigDecimal) numberTable.get(testClass);
        expectedValue = BigDecimal.valueOf(Double.valueOf(BIGDEC));
      } else {
        // Default Values, this should never been utilized.
        testValue = "0";
        expectedValue = BigDecimal.valueOf(Double.valueOf("1"));
      }

      try {
        // Coercing Number to BigInteger type.
        result = ExprEval.evaluateValueExpression("${" + testValue + "}", null,
            expectedClass);
        pass = (ExprEval.compareClass(result, expectedClass)
            && ExprEval.compareValue((BigDecimal) result, expectedValue, 1));
        if (!pass)
          throw new Fault("TEST FAILED: pass = false" + NLINE + name
              + " Coerced to BigDecimal Failed!" + NLINE
              + "See above for expected value." + NLINE);

      } catch (Exception e) {
        throw new Fault(e);
      }
    }

  }

  /**
   * @testName: elCoerceToEnumTypeTest
   * @assertion_ids: EL:SPEC:42.1; EL:SPEC:42.2; EL:SPEC:42.3; EL:SPEC:42.4
   * @test_Strategy: Validate that - an assignable enum type can be coerced to
   *                 an enum - coercing a null value to an enum type returns a
   *                 null value - coercing an empty string to an enum type
   *                 returns a null value - coercing a string to an enum is
   *                 successful if the string is identical to one of the enum
   *                 values. If not, an ELException is thrown.
   */
  public void elCoerceToEnumTypeTest() throws Fault {

    boolean fail = false;
    boolean[] pass = { false, false, false, false, false };
    Object result = null;

    try {
      // assignable types
      NameValuePair[] earthNV = NameValuePair
          .buildUnaryNameValue(planets.EARTH);
      result = ExprEval.evaluateValueExpression("${A}", earthNV, planets.class);
      pass[0] = ExprEval.compareClass(result, planets.class)
          && ExprEval.compareValue(result, planets.EARTH);

      // type is null
      result = ExprEval.evaluateValueExpression("${null}", null, planets.class);
      pass[1] = (result == null) ? true : false;

      // type is empty string
      NameValuePair[] emptyNV = NameValuePair.buildUnaryNameValue("");
      result = ExprEval.evaluateValueExpression("#{A}", emptyNV, planets.class);
      pass[2] = (result == null) ? true : false;

      // String value that matches enum identifier
      NameValuePair[] marsNV = NameValuePair.buildUnaryNameValue("MARS");
      result = ExprEval.evaluateValueExpression("${A}", marsNV, planets.class);
      pass[3] = ExprEval.compareClass(result, planets.class)
          && ExprEval.compareValue(result, planets.MARS);

      // String value that does not match
      NameValuePair[] plutoNV = NameValuePair.buildUnaryNameValue("PLUTO");
      try {
        result = ExprEval.evaluateValueExpression("${A}", plutoNV,
            planets.class);
      } catch (ELException e) {
        pass[4] = true;
      }

    } catch (Exception e) {
      throw new Fault(e);
    } finally {
      ExprEval.cleanup();
    }

    for (int i = 0; i < pass.length; ++i) {
      if (!pass[i]) {
        fail = true;
        TestUtil.logErr("Unexpected result for test case " + i);
      }
    }

    if (fail)
      throw new Fault("TEST FAILED");
  }

  /**
   * @testName: elCoerceToOtherTypeTest
   * @assertion_ids: EL:SPEC:43.1; EL:SPEC:43.2
   * @test_Strategy: Validate that - an assignable "other" type can be coerced
   *                 to an "other" type. We coerce an instance of the class
   *                 java.sql.Time to its parent class java.util.Date. -
   *                 coercing a null value to an "other" type returns a null
   *                 value
   * 
   */
  public void elCoerceToOtherTypeTest() throws Fault {

    Time time = new Time(1000000000000L);
    boolean pass1, pass2 = false;

    try {
      NameValuePair[] timeNV = NameValuePair.buildUnaryNameValue(time);
      Object result = ExprEval.evaluateValueExpression("${A}", timeNV,
          Date.class);
      pass1 = ExprEval.compareClass(result, Date.class)
          && ExprEval.compareValue(result, (Date) time);

      result = ExprEval.evaluateValueExpression("#{null}", null, Date.class);
      pass2 = (result == null) ? true : false;

    } catch (Exception e) {
      throw new Fault(e);
    } finally {
      ExprEval.cleanup();
    }

    if (!pass1 || !pass2)
      throw new Fault("TEST FAILED");

  }

  // ------------------------------------------------- private methods

  /**
   * The HashTable is of this format.
   * 
   * Key = Test Class Value = Test Value
   */
  private void initializeNumberTable() {
    numberTable = new Hashtable();

    numberTable.put(Byte.class, Byte.valueOf(BYTE));
    numberTable.put(Short.class, Short.valueOf(SHORT));
    numberTable.put(Integer.class, Integer.valueOf(INTEGER));
    numberTable.put(Long.class, Long.valueOf(LONG));
    numberTable.put(Float.class, Float.valueOf(FLOAT));
    numberTable.put(Double.class, Double.valueOf(DOUBLE));
    numberTable.put(BigInteger.class, BigInteger.valueOf(Long.valueOf(BIGINT)));
    numberTable.put(BigDecimal.class,
        BigDecimal.valueOf(Double.valueOf(BIGDEC)));
  }
}
