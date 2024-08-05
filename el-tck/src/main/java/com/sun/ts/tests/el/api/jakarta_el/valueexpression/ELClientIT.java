/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates and others.
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

package com.sun.ts.tests.el.api.jakarta_el.valueexpression;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;



import com.sun.ts.tests.el.common.api.expression.ExpressionTest;
import com.sun.ts.tests.el.common.elcontext.BareBonesELContext;
import com.sun.ts.tests.el.common.elcontext.SimpleELContext;
import com.sun.ts.tests.el.common.elcontext.VRContext;
import com.sun.ts.tests.el.common.util.ELTestUtil;

import jakarta.el.ELContext;
import jakarta.el.ELResolver;
import jakarta.el.ExpressionFactory;
import jakarta.el.PropertyNotFoundException;
import jakarta.el.ValueExpression;
import jakarta.el.ValueReference;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.System.Logger;

public class ELClientIT {

  private static final Logger logger = System.getLogger(ELClientIT.class.getName());

  private Hashtable<Class<?>, Object> testValueTable;

  private Properties testProps;

  public ELClientIT(){
    this.initializeTable();
    this.testProps=System.getProperties();
  }

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
   * @testName: positiveValueExpressionTest
   * @assertion_ids: EL:JAVADOC:110; EL:JAVADOC:111; EL:JAVADOC:112;
   *                 EL:JAVADOC:113; EL:JAVADOC:114; EL:JAVADOC:60;
   *                 EL:JAVADOC:58
   * @test_Strategy: Validate the behavior of ValueExpression API
   *                 ValueExpression.getValue() ValueExpression.setValue()
   *                 ValueExpression.getType() ValueExpression.getExpectedType()
   *                 ValueExpression.isReadOTestUtil.NEW_LINEy()
   *                 Expression.isLiteralText() Expression.getExpressionString()
   */
  @Test
  public void positiveValueExpressionTest() throws Exception {

    StringBuffer buf = new StringBuffer();

    boolean pass1, pass2, pass3, pass4;

    try {
      ExpressionFactory expFactory = ExpressionFactory.newInstance();

      ELContext context = (new SimpleELContext()).getELContext();
      ELResolver resolver = context.getELResolver();
      resolver.setValue(context, null, "foo", "bar");

      // string variable
      // readOTestUtil.NEW_LINEy() and isLiteralText() expected to return false
      buf.append("Testing expression 1 " + ELTestUtil.NL);
      String exprStr1 = "${foo}";
      ValueExpression vexp1 = expFactory.createValueExpression(context,
          exprStr1, String.class);
      pass1 = ExpressionTest.testValueExpression(vexp1, context, exprStr1,
          String.class, "bar", false, false, buf);

      // literal expression
      // readOTestUtil.NEW_LINEy() and isLiteralText() expected to return true
      buf.append("Testing expression 2 " + ELTestUtil.NL);
      String exprStr2 = "foo";
      ValueExpression vexp2 = expFactory.createValueExpression(context,
          exprStr2, String.class);
      pass2 = ExpressionTest.testValueExpression(vexp2, context, exprStr2,
          String.class, "foo", true, true, buf);

      // expression that is not an l-value
      // readOTestUtil.NEW_LINEy() expected to return true
      // isLiteralText() expected to return false
      buf.append("Testing expression 3 " + ELTestUtil.NL);
      String exprStr3 = "#{1 + 1}";
      ValueExpression vexp3 = expFactory.createValueExpression(context,
          exprStr3, Integer.class);
      pass3 = ExpressionTest.testValueExpression(vexp3, context, exprStr3,
          Integer.class, Integer.valueOf(2), true, false, buf);

      // Expression test for ValueReference.
      buf.append("Testing expression 4 " + ELTestUtil.NL);
      pass4 = true;

      ELContext contextTwo = new VRContext(testProps);
      String exprStr4 = "#{worker.lastName}";

      ValueExpression vexp4 = expFactory.createValueExpression(contextTwo,
          exprStr4, String.class);

      ValueReference vr = vexp4.getValueReference(contextTwo);

      if (vr == null) {
        pass4 = false;
        buf.append("ValueRefernce should have return a non null " + "value.");
      } else {
        Object base = vr.getBase();

        if (base != null) {
          String baseName = base.getClass().getSimpleName();
          if (!"Worker".equals(baseName)) {
            buf.append("Unexpected Base Value!" + ELTestUtil.NL
                + "Expected: Worker" + ELTestUtil.NL + "Received: "
                + baseName);
            pass4 = false;
          }
        }
      }

    } catch (Exception ex) {
      throw new Exception(ex);
    }

    if ((pass1 && pass2 && pass3 && pass4)) {
      logger.log(Logger.Level.TRACE, buf.toString());

    } else {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }
  }

  /**
   * @testName: negativeValueExpressionTest
   * @assertion_ids: EL:JAVADOC:111; EL:JAVADOC:112; EL:JAVADOC:113;
   *                 EL:JAVADOC:114; EL:JAVADOC:368; EL:JAVADOC:371;
   *                 EL:JAVADOC:372; EL:JAVADOC:375; EL:JAVADOC:376;
   *                 EL:JAVADOC:378; EL:JAVADOC:379; EL:JAVADOC:380
   * 
   * @test_Strategy: Validate the behavior of ValueExpression API
   *                 ValueExpression.getValue() ValueExpression.setValue()
   *                 ValueExpression.getType()
   *                 ValueExpression.isReadOTestUtil.NEW_LINEy()
   * 
   *                 If the ELContext parameter for these methods is null, a
   *                 NullPointerException is thrown. If the ELContext parameter
   *                 is not the same as the one with which the ValueExpression
   *                 was created, a PropertyNotFoundException is thrown.
   */
  @Test
  public void negativeValueExpressionTest() throws Exception {

    boolean pass = true;

    ExpressionFactory expFactory = ExpressionFactory.newInstance();
    ELContext simpleContext = (new SimpleELContext()).getELContext();
    ELContext emptyContext = (new BareBonesELContext()).getELContext();

    ELResolver resolver = simpleContext.getELResolver();

    try {
      resolver.setValue(simpleContext, null, "foo", "bar");
    } catch (Exception e) {
      throw new Exception(e);
    }

    String exprStr = "${foo}";

    ValueExpression vexp = expFactory.createValueExpression(simpleContext,
        exprStr, String.class);

    // NullPointerException
    try {
      vexp.getValue(null);
      pass = false;
      logger.log(Logger.Level.ERROR, "Call to getValue() with null ELContext parameter "
          + "did not" + ELTestUtil.NL + " cause an exception to be thrown"
          + ELTestUtil.NL);

    } catch (NullPointerException npe) {
      pass = true;

    } catch (Exception e) {
      pass = false;
      logger.log(Logger.Level.ERROR, "Call to getValue() with null ELContext "
          + "parameter caused an exception to be thrown, but it was not a"
          + ELTestUtil.NL + " NullPointerException: " + e.toString()
          + ELTestUtil.NL);
    }

    try {
      vexp.setValue(null, "foo");
      pass = false;
      logger.log(Logger.Level.ERROR, "Call to setValue() with null ELContext parameter "
          + "did not" + ELTestUtil.NL + " cause an exception to be thrown"
          + ELTestUtil.NL);

    } catch (NullPointerException npe) {
      pass = true;

    } catch (Exception e) {
      pass = false;
      logger.log(Logger.Level.ERROR, "Call to setValue() with null ELContext "
          + "parameter caused" + ELTestUtil.NL
          + " an exception to be thrown, but it was not a" + ELTestUtil.NL
          + " NullPointerException: " + e.toString() + ELTestUtil.NL);
    }

    try {
      vexp.isReadOnly(null);
      pass = false;
      logger.log(Logger.Level.ERROR, "Call to isReadOTestUtil.NEW_LINEy() with null ELContext "
          + "parameter did not" + ELTestUtil.NL
          + " cause an exception to be thrown" + ELTestUtil.NL);

    } catch (NullPointerException npe) {
      pass = true;

    } catch (Exception e) {
      pass = false;
      logger.log(Logger.Level.ERROR, "Call to isReadOTestUtil.NEW_LINEy() with null ELContext "
          + "parameter caused" + ELTestUtil.NL
          + "an exception to be thrown, but it was not a" + ELTestUtil.NL
          + "NullPointerException: " + e.toString() + ELTestUtil.NL);
    }

    try {
      vexp.getType(null);
      pass = false;
      logger.log(Logger.Level.ERROR, "Call to getType() with null ELContext parameter "
          + "did not" + ELTestUtil.NL + "cause an exception to be thrown"
          + ELTestUtil.NL);

    } catch (NullPointerException npe) {
      pass = true;

    } catch (Exception e) {
      pass = false;
      logger.log(Logger.Level.ERROR, "Call to getType() with null ELContext parameter "
          + "caused" + ELTestUtil.NL
          + "an exception to be thrown, but it was not a" + ELTestUtil.NL
          + "NullPointerException: " + e.toString() + ELTestUtil.NL);
    }

    // PropertyNotFoundException
    try {
      vexp.setValue(emptyContext, "foo");
      pass = false;
      logger.log(Logger.Level.ERROR, "Call to setValue() for non-existent property did "
          + "not cause" + ELTestUtil.NL + "an exception to be thrown"
          + ELTestUtil.NL);

    } catch (PropertyNotFoundException pnfe) {
      pass = true;

    } catch (Exception e) {
      pass = false;
      logger.log(Logger.Level.ERROR, "Call to setValue() for non-existent property "
          + "caused an exception to be thrown, but it was not a"
          + ELTestUtil.NL + "PropertyNotFoundException: " + e.toString()
          + ELTestUtil.NL);
    }

    try {
      vexp.getValue(emptyContext);
      pass = false;
      logger.log(Logger.Level.ERROR, "Call to getValue() for non-existent property did "
          + "not cause an exception to be thrown" + ELTestUtil.NL);

    } catch (PropertyNotFoundException pnfe) {
      pass = true;

    } catch (Exception e) {
      pass = false;
      logger.log(Logger.Level.ERROR, "Call to getValue() for non-existent property "
          + "caused an exception to be thrown, but it was not a PropertyNotFoundException: "
          + ELTestUtil.NL + e.toString() + ELTestUtil.NL);
    }

    try {
      vexp.isReadOnly(emptyContext);
      pass = false;
      logger.log(Logger.Level.ERROR, 
          "Call to isReadOTestUtil.NEW_LINEy() for non-existent property did "
              + "not cause an exception to be thrown" + ELTestUtil.NL);

    } catch (PropertyNotFoundException pnfe) {
      pass = true;

    } catch (Exception e) {
      pass = false;
      logger.log(Logger.Level.ERROR, 
          "Call to isReadOTestUtil.NEW_LINEy() for non-existent property "
              + "caused an exception to be thrown, but it was not a"
              + " PropertyNotFoundException: " + ELTestUtil.NL
              + e.toString() + ELTestUtil.NL);
    }

    try {
      vexp.getType(emptyContext);
      pass = false;
      logger.log(Logger.Level.ERROR, "Call to getType() for non-existent property did not "
          + "cause an exception to be thrown" + ELTestUtil.NL);

    } catch (PropertyNotFoundException pnfe) {
      pass = true;

    } catch (Exception e) {
      pass = false;
      logger.log(Logger.Level.ERROR, "Call to getType() for non-existent property "
          + "caused an exception to be thrown, but it was not a"
          + ELTestUtil.NL + " PropertyNotFoundException: " + e.toString()
          + ELTestUtil.NL);
    }

    if (!pass) {
      throw new Exception(ELTestUtil.FAIL);
    }
  }

  /**
   * @testName: valueExpressionSerializableTest
   * 
   * @assertion_ids: EL:SPEC:44
   * 
   * @test_Strategy: Validate that ValueExpression implements Serializable and
   *                 that a ValueExpression can be manually serialized and
   *                 deserialized.
   */
  @Test
  public void valueExpressionSerializableTest() throws Exception {

    boolean pass;

    StringBuffer buf = new StringBuffer();
    Enumeration<?> keys = testValueTable.keys();
    Class<?> testClass;
    Object testValue;

    while (keys.hasMoreElements()) {
      pass = true;
      testClass = (Class<?>) keys.nextElement();
      testValue = testValueTable.get(testClass);

      try {
        ExpressionFactory expFactory = ExpressionFactory.newInstance();
        ELContext context = (new SimpleELContext()).getELContext();

        // Set eval-expression.
        ValueExpression evalvexp = expFactory.createValueExpression(context,
            "${" + testValue + "}", testClass);
        logger.log(Logger.Level.TRACE, "Eval Value Expression For Testing: "
            + evalvexp.toString() + ELTestUtil.NL);

        // Set literal-expression
        ValueExpression literalvexp = expFactory.createValueExpression(context,
            "\"" + testValue + "\"", testClass);
        logger.log(Logger.Level.TRACE, "Literal Value Expression For Testing: "
            + literalvexp.toString() + ELTestUtil.NL);

        // Set Composite Expression
        ValueExpression compositevexp = expFactory.createValueExpression(
            context, "#{" + testValue + "}" + " " + testValue, testClass);
        logger.log(Logger.Level.TRACE, "Composite Value Expression For Testing: "
            + compositevexp.toString() + ELTestUtil.NL);

        // Test eval, literal, & composite expressions.
        if (!(ExpressionTest.expressionSerializableTest(evalvexp, buf)
            && ExpressionTest.expressionSerializableTest(literalvexp, buf)
            && ExpressionTest.expressionSerializableTest(compositevexp, buf))) {
          pass = false;
          break;
        }

      } catch (Exception ex) {
        throw new Exception(ex);

      }

      if (pass) {
        logger.log(Logger.Level.TRACE, buf.toString());
      } else {
        throw new Exception(ELTestUtil.FAIL + buf.toString());
      }
    }
  }

  /*
   * The HashTable is of this format.
   * 
   * Key = Test Class Value = Test Value
   */
  private void initializeTable() {
    testValueTable = new Hashtable<Class<?>, Object>();

    testValueTable.put(String.class, "SERIAL");
    testValueTable.put(Integer.class, Integer.valueOf("123"));
    testValueTable.put(Boolean.class, Boolean.TRUE);
  }

  /**
   * @testName: valueExpressionEqualsTest
   * 
   * @assertion_ids: EL:JAVADOC:56
   * 
   * @test_Strategy: Validate that ValueExpression implements equals() and that
   *                 the behavior is as expected
   */
  @Test
  public void valueExpressionEqualsTest() throws Exception {

    StringBuffer buf = new StringBuffer();

    boolean pass = true;

    try {

      ExpressionFactory expFactory = ExpressionFactory.newInstance();
      SimpleELContext context = new SimpleELContext();
      ELResolver resolver = context.getELResolver();

      // case 1: Expressions of the same type and equal value, but
      // different parsed representations are NOT equal.
      String exprStr1 = "${foo}";
      String exprStr2 = "${bar}";
      resolver.setValue(context, null, "foo", "SOME VALUE");
      resolver.setValue(context, null, "bar", "SOME VALUE");

      ValueExpression vexp1 = expFactory.createValueExpression(context,
          exprStr1, String.class);
      ValueExpression vexp2 = expFactory.createValueExpression(context,
          exprStr2, String.class);

      buf.append("vexp1 has value " + (String) vexp1.getValue(context)
          + ELTestUtil.NL);
      buf.append("vexp2 has value " + (String) vexp2.getValue(context)
          + ELTestUtil.NL);

      if (ExpressionTest.equalsTest(vexp1, vexp2, buf)) {
        pass = false;
        buf.append(
            "Failed: case 1: same type and equal value" + ELTestUtil.NL);
      }

      // case 2: White space is ignored
      String exprStr3 = "${A+B+C}";
      String exprStr4 = "${ A + B	+		C	}";

      ValueExpression vexp3 = expFactory.createValueExpression(context,
          exprStr3, Object.class);
      ValueExpression vexp4 = expFactory.createValueExpression(context,
          exprStr4, Object.class);

      if (!ExpressionTest.equalsTest(vexp3, vexp4, buf)) {
        buf.append("Failed: case 2: white space" + ELTestUtil.NL);
        pass = false;
      }

      // case 3: Equivalent Operators
      String exprStr5 = "${A < B}";
      String exprStr6 = "${A lt B}";

      ValueExpression vexp5 = expFactory.createValueExpression(context,
          exprStr5, Object.class);
      ValueExpression vexp6 = expFactory.createValueExpression(context,
          exprStr6, Object.class);

      if (!ExpressionTest.equalsTest(vexp5, vexp6, buf)) {
        buf.append("Failed: case 3: equivalent operators" + ELTestUtil.NL);
        pass = false;
      }

      // case 4: Reversed operands
      String exprStr7 = "${A + B}";
      String exprStr8 = "${B + A}";

      ValueExpression vexp7 = expFactory.createValueExpression(context,
          exprStr7, Object.class);
      ValueExpression vexp8 = expFactory.createValueExpression(context,
          exprStr8, Object.class);

      if (ExpressionTest.equalsTest(vexp7, vexp8, buf)) {
        buf.append("Failed: case 4: reversed operands" + ELTestUtil.NL);
        pass = false;
      }

      // case 5: '$' and '#' delimiters are parsed the same
      String exprStr9 = "${A}";
      String exprStr10 = "#{A}";

      ValueExpression vexp9 = expFactory.createValueExpression(context,
          exprStr9, Object.class);
      ValueExpression vexp10 = expFactory.createValueExpression(context,
          exprStr10, Object.class);

      if (!ExpressionTest.equalsTest(vexp9, vexp10, buf)) {
        buf.append("Failed: case 5: delimiters" + ELTestUtil.NL);
        pass = false;
      }

    } catch (Exception ex) {
      throw new Exception(ex);

    }

    if (pass) {
      logger.log(Logger.Level.TRACE, buf.toString());
    } else {
      throw new Exception(ELTestUtil.FAIL + buf.toString());
    }

  }
}
