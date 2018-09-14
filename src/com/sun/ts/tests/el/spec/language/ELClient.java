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

package com.sun.ts.tests.el.spec.language;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.el.spec.Book;
import com.sun.ts.tests.el.common.util.ExprEval;
import com.sun.ts.tests.el.common.util.ResolverType;
import javax.el.ELException;
import java.util.Hashtable;
import java.util.Properties;

public class ELClient extends ServiceEETest {

  private static String NLINE = System.getProperty("line.separator", "\n");

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
  }

  // ------------------------------------------------------------- Test Methods

  /*
   * @testName: poundDollarSameMeaning1Test
   * 
   * @assertion_ids: EL:SPEC:1
   * 
   * @test_Strategy: Confirm that two EL expressions, identical except for the
   * '$' and '#' delimiters, are evaluated the same. Case 1: base is null.
   */
  public void poundDollarSameMeaning1Test() throws Fault {

    boolean pass = true;

    String testExpr = "\"foo\"";

    try {
      Object dollarResult = ExprEval
          .evaluateValueExpression("${" + testExpr + "}", null, String.class);
      Object poundResult = ExprEval
          .evaluateValueExpression("#{" + testExpr + "}", null, String.class);

      TestUtil.logTrace("Comparing  ${" + dollarResult.toString() + "} "
          + "to #{" + poundResult.toString() + "}");

      pass = (ExprEval.compareClass(poundResult, String.class)
          && ExprEval.compareClass(dollarResult, String.class)
          && ExprEval.compareValue(poundResult, dollarResult));

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED!");
  }

  /*
   * @testName: poundDollarSameMeaning2Test
   * 
   * @assertion_ids: EL:SPEC:1
   * 
   * @test_Strategy: Confirm that two EL expressions, identical except for the
   * '$' and '#' delimiters, are evaluated the same. Case 2: base is non-null.
   */
  public void poundDollarSameMeaning2Test() throws Fault {

    boolean pass = true;

    try {
      Object firstNameDollar = ExprEval.evaluateValueExpression(
          "${worker.firstName}", null, String.class,
          ResolverType.EMPLOYEE_ELRESOLVER);

      Object firstNamePound = ExprEval.evaluateValueExpression(
          "#{worker.firstName}", null, String.class,
          ResolverType.EMPLOYEE_ELRESOLVER);

      TestUtil.logTrace("Comparing  ${" + firstNameDollar.toString() + "} to #{"
          + firstNamePound.toString() + "}");

      if (!(firstNamePound.toString().equals(firstNameDollar.toString()))) {

        TestUtil.logTrace(
            "Dollar & Pound symbols return different" + "expression values!");
        pass = false;

      }

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED!");
  }

  /*
   * @testName: nestedEvalExpressionsTest
   * 
   * @assertion_ids: EL:SPEC:2
   * 
   * @test_Strategy: Verify that nested eval-expressions are illegal.
   */
  public void nestedEvalExpressionsTest() throws Fault {

    boolean pass = true;
    String[] expr = { "${worker[${worker}]}", "${worker[#{worker}]}",
        "#{worker[${worker}]}", "#{worker[#{worker}]}" };

    for (int i = 0; i < expr.length; ++i) {

      try {
        ExprEval.evaluateValueExpression(expr[i], null, String.class,
            ResolverType.EMPLOYEE_ELRESOLVER);
        pass = false;
        TestUtil.logErr("Test FAILED. No exception thrown for ");
        TestUtil.logErr(expr[i]);

      } catch (ELException ee) {
        // Test passes
        TestUtil.logErr("Expected Exception thrown.");

      } catch (Exception e) {
        pass = false;
        TestUtil.logErr("Test FAILED. " + expr[i] + " caused ");
        TestUtil.logErr("an exception, but it was not an ");
        TestUtil.logErr("ELException.");
        TestUtil.printStackTrace(e);
      }
    }

    if (!pass)
      throw new Fault("TEST FAILED!");
  }

  /*
   * @testName: mixedCompositeExpressionsTest
   * 
   * @assertion_ids: EL:SPEC:12
   * 
   * @test_Strategy: Verify that composite expressions that mix the '$' and '#'
   * delimiters are illegal.
   */
  public void mixedCompositeExpressionsTest() throws Fault {

    boolean pass = true;
    String[] expr = { "${worker}#{worker}", "#{worker}${worker}",
        "${worker}#{worker}${worker}", "#{worker}${worker}#{worker}" };

    for (int i = 0; i < expr.length; ++i) {

      try {
        ExprEval.evaluateValueExpression(expr[i], null, String.class,
            ResolverType.EMPLOYEE_ELRESOLVER);
        pass = false;
        TestUtil.logErr("Test FAILED. No exception thrown for ");
        TestUtil.logErr(expr[i]);

      } catch (ELException ee) {
        // Test passes
        TestUtil.logErr("Expected Exception thrown.");

      } catch (Exception e) {
        pass = false;
        TestUtil.logErr("Test FAILED. " + expr[i] + " caused ");
        TestUtil.logErr("an exception, but it was not an ");
        TestUtil.logErr("ELException.");
        TestUtil.printStackTrace(e);
      }

      if (!pass)
        throw new Fault("TEST FAILED!");
    }
  }

  /*
   * @testName: compositeExprEval1Test
   * 
   * @assertion_ids: EL:SPEC:11
   * 
   * @test_Strategy: Verify that in a composite expression eval-expressions are
   * coerced to Strings according to the EL type conversion rules and
   * concatenated with any intervening literal-expressions.
   */
  public void compositeExprEval1Test() throws Fault {

    boolean pass = true;

    String streetName = "${'Network Circle'}";
    String city = "${'Santa Clara'}";
    String state = "${'CA'}";

    String expected = "4140 Network Circle, Santa Clara, CA 95054";

    try {
      Object address = ExprEval.evaluateValueExpression(
          4140 + " " + streetName + ", " + city + ", " + state + " " + 95054,
          null, String.class);

      TestUtil.logTrace("Testing for Address: " + expected);

      pass = (ExprEval.compareClass(address, String.class)
          && ExprEval.compareValue(address, expected));

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED!");
  }

  /*
   * @testName: compositeExprEval2Test
   * 
   * @assertion_ids: EL:SPEC:11
   * 
   * @test_Strategy: Verify that in a composite expression eval-expressions are
   * evaluated left to right, coerced to Strings according to the EL type
   * conversion rules, and concatenated with any intervening
   * literal-expressions.
   */
  public void compositeExprEval2Test() throws Fault {

    boolean pass = true;

    int num = 2;
    String expected = "total = 3.0";

    try {
      Object div = ExprEval.evaluateValueExpression(
          "total = " + "${" + num + "+2/" + num + "}", null, String.class);

      TestUtil.logTrace("Testing for: " + expected);

      pass = (ExprEval.compareClass(div, String.class)
          && ExprEval.compareValue(div, expected));

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED!");
  }

  /*
   * @testName: dotAndIndexOperatorsSameTest
   * 
   * @assertion_ids: EL:SPEC:15
   * 
   * @test_Strategy: [DotAndIndexOperatorsSame] Verify that the dot and index
   * operators are evaluated in the same way.
   */
  public void dotAndIndexOperatorsSameTest() throws Fault {

    boolean pass = true;

    try {
      // ELResolver empResolver = new EmployeeELResolver();

      Object firstnameDot = ExprEval.evaluateValueExpression(
          "${worker.firstName}", null, String.class,
          ResolverType.EMPLOYEE_ELRESOLVER);

      Object firstnameBracket = ExprEval.evaluateValueExpression(
          "${worker['firstName']}", null, String.class,
          ResolverType.EMPLOYEE_ELRESOLVER);

      pass = firstnameDot.equals(firstnameBracket);

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED!");
  }

  /*
   * @testName: elSyntaxEscapeTest
   * 
   * @assertion_ids: EL:SPEC:8
   * 
   * @test_Strategy: [ELSyntaxEscape] Verify that the EL special characters '$'
   * and '#' are treated as literals when preceded with '\'.
   */
  public void elSyntaxEscapeTest() throws Fault {

    boolean pass = true;

    try {
      Object firstnameDollar = ExprEval.evaluateValueExpression(
          "\\${worker.firstName}", null, String.class,
          ResolverType.EMPLOYEE_ELRESOLVER);

      Object firstnamePound = ExprEval.evaluateValueExpression(
          "\\#{worker.firstName}", null, String.class,
          ResolverType.EMPLOYEE_ELRESOLVER);

      if (!(ExprEval.compareValue(firstnameDollar, "${worker.firstName}")
          && ExprEval.compareValue(firstnamePound, "#{worker.firstName}"))) {

        TestUtil.logTrace("Escape character failed to work.");
        pass = false;
      }

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED!");
  }

  /*
   * @testName: literalExprEval1Test
   * 
   * @assertion_ids: EL:SPEC:6
   * 
   * @test_Strategy: [LiteralExprEval] Set the value of a ValueExpression to a
   * literal String type. Verify that the value retrieved when the expression is
   * evaluated is a String equal to the value set.
   */
  public void literalExprEval1Test() throws Fault {

    boolean pass = true;

    String exprStr = "foo";
    String expected = exprStr;

    try {
      Object expr = ExprEval.evaluateValueExpression(exprStr, null,
          String.class);

      pass = (ExprEval.compareClass(expr, String.class)
          && ExprEval.compareValue(expr, expected));

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED!");
  }

  /*
   * @testName: literalExprEval2Test
   * 
   * @assertion_ids: EL:SPEC:6
   * 
   * @test_Strategy: [LiteralExprEval] Coerce a String literal to a Boolean in a
   * ValueExpression. Verify that the value retrieved when the expression is
   * evaluated is a Boolean of the expected value.
   */
  public void literalExprEval2Test() throws Fault {

    boolean pass = true;

    String exprStr = "true";

    try {
      Object expr = ExprEval.evaluateValueExpression(exprStr, null,
          Boolean.class);

      pass = (ExprEval.compareClass(expr, Boolean.class)
          && ExprEval.compareValue(expr, true));

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("TEST FAILED!");
  }

  /*
   * @testName: literalExprAsMethodExpr1Test
   * 
   * @assertion_ids: EL:SPEC:10
   * 
   * @test_Strategy: [LiteralExprAsMethodExpr] Verify that a literal-expression
   * can also be used as a method expression that returns a non-void value.
   */
  public void literalExprAsMethodExpr1Test() throws Fault {

    boolean pass = true;

    try {
      // literal expression returning String
      Class[] params = {};

      Object value1 = ExprEval.evaluateMethodExpression("true", params,
          String.class, ResolverType.VECT_ELRESOLVER);

      if (!("true".equals(value1))) {
        pass = false;
        TestUtil.logErr("Literal Expression, Return String Failed!");
      }

      // literal expression returning non-String value
      Object value2 = ExprEval.evaluateMethodExpression("true", params,
          Boolean.class, ResolverType.VECT_ELRESOLVER);
      if (!((Boolean) value2).booleanValue()) {
        pass = false;
        TestUtil.logErr("Literal Expression, Return non-String " + "Failed!");
      }

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("Test Failed!");
  }

  /*
   * @testName: literalExprAsMethodExpr2Test
   * 
   * @assertion_ids: EL:SPEC:10
   * 
   * @test_Strategy: [LiteralExprAsMethodExpr] Verify that a literal-expression
   * can also be used as a method expression that returns a non-void value.
   * Verify that the standard coercion rules apply if the return type is not
   * java.lang.String.
   */
  public void literalExprAsMethodExpr2Test() throws Fault {

    boolean pass = true;
    int testNum = 496;

    try {
      // literal expression returning String
      Class[] params = {};

      Object value = ExprEval.evaluateMethodExpression("496", params,
          Integer.class, ResolverType.VECT_ELRESOLVER);

      if (!(value instanceof Integer)) {
        pass = false;
        TestUtil.logErr("MethodExpression invocation does not return"
            + " instance of expected class");
      }

      else if (((Integer) value).intValue() != testNum) {
        pass = false;
        TestUtil.logErr(
            "Expected: " + testNum + NLINE + "Received: " + value.toString());
      }

    } catch (Exception e) {
      throw new Fault(e);
    }

    if (!pass)
      throw new Fault("Test Failed!");
  }

  /*
   * @testName: rValueCoercion1Test
   * 
   * @assertion_ids: EL:SPEC:3
   * 
   * @test_Strategy: [RValueCoercion] Set the value of a ValueExpression to a
   * String type and verify that the value retrieved when the expression is
   * evaluated is also a String type.
   */
  public void rValueCoercion1Test() throws Fault {
    boolean pass = false;

    String expr = "${foo}";
    String expected = "bar";

    try {
      Object value = ExprEval.evaluateCoerceValueExpression(expr, expected,
          String.class);

      pass = (ExprEval.compareClass(value, String.class)
          && ExprEval.compareValue(value, expected));

    } catch (Exception e) {
      throw new Fault(e);
    }
    if (!pass)
      throw new Fault("TEST FAILED!");
  }

  /*
   * @testName: rValueCoercion2Test
   * 
   * @assertion_ids: EL:SPEC:3
   * 
   * @test_Strategy: [RValueCoercion] Set the value of a ValueExpression to a
   * complex type and verify that the value retrieved when the expression is
   * evaluated is a String type in accordance with the coercion rules.
   */
  public void rValueCoercion2Test() throws Fault {
    boolean pass = false;

    String expr = "${javabook}";
    String expected = "The Java Programming Language";

    try {
      Book jbook = new Book(expected, "Arnold and Gosling", "Addison Wesley",
          1996);

      Object value = ExprEval.evaluateCoerceValueExpression(expr, jbook,
          String.class);

      pass = (ExprEval.compareClass(value, String.class)
          && ExprEval.compareValue(value, expected));

    } catch (Exception e) {
      throw new Fault(e);
    }
    if (!pass)
      throw new Fault("TEST FAILED!");
  }

  /*
   * @testName: parseOnceEvalManyTest
   * 
   * @assertion_ids: EL:SPEC:45
   * 
   * @test_Strategy: [ExprParsedEvalMany] Verify that once an expression is
   * parsed, it can be evaluated multiple times, and that the result of the
   * evaluation will be the same even when the EL context is modified.
   */
  public void parseOnceEvalManyTest() throws Fault {
    boolean pass = false;

    String expr = "${foo}";
    String expected = "bar";

    Hashtable contextObjects = new Hashtable();
    contextObjects.put(String.class, "string context");
    contextObjects.put(Integer.class, Integer.valueOf(1));
    contextObjects.put(Boolean.class, Boolean.TRUE);

    try {
      pass = ExprEval.evaluateManyValueExpression(expr, expected, String.class,
          contextObjects);

    } catch (Exception e) {
      throw new Fault(e);
    }
    if (!pass)
      throw new Fault("TEST FAILED!");
  }

}
