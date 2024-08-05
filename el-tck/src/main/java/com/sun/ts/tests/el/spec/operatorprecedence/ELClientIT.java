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

/*
 * $Id$
 */

package com.sun.ts.tests.el.spec.operatorprecedence;

import java.util.Properties;


import com.sun.ts.tests.el.common.elcontext.FuncMapperELContext;
import com.sun.ts.tests.el.common.util.ExprEval;

import jakarta.el.ELException;
import jakarta.el.ExpressionFactory;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;

import java.lang.System.Logger;

public class ELClientIT {

  private static final Logger logger = System.getLogger(ELClientIT.class.getName());

  private static final String[] MODOPER = { "%", "mod" };

  private static final String[] DIVOPER = { "/", "div" };

  private static final String[] ANDOPER = { "&&", "and" };

  private static final String[] OROPER = { "||", "or" };

  private final boolean[] deferred = { true, false };

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


  /*
   * @testName: elMultiPreBinaryTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "*" is evaluated before "+" "*" is evaluated before "-"
   */
  @Test
  public void elMultiPreBinaryTest() throws Exception {

    this.testOrderPrecedence("{1 + 5 * 2}", Long.valueOf(11));
    this.testOrderPrecedence("{1 - 5 * 2}", Long.valueOf(-9));

  }

  /*
   * @testName: elDivPreBinaryTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "div, /" is evaluated before "+" "div, /" is evaluated before
   * "-"
   */
  @Test
  public void elDivPreBinaryTest() throws Exception {

    for (String s : DIVOPER) {
      this.testOrderPrecedence("{1 + 4 " + s + " 2}", Double.valueOf(3));
      this.testOrderPrecedence("{1 - 4 " + s + " 2}", Double.valueOf(-1));
    }

  }

  /*
   * @testName: elModPreBinaryTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "mod, %" is evaluated before "+" "mod, %" is evaluated before
   * "-"
   */
  @Test
  public void elModPreBinaryTest() throws Exception {

    for (String s : MODOPER) {
      this.testOrderPrecedence("{1 + 7 " + s + " 2}", Long.valueOf(2));
      this.testOrderPrecedence("{1 - 7 " + s + " 2}", Long.valueOf(0));
    }

  }

  /*
   * @testName: elMultiPreRelationalTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "*" is evaluated before ">" "*" is evaluated before "<" "*"
   * is evaluated before ">=" "*" is evaluated before "<=" "*" is evaluated
   * before "lt" "*" is evaluated before "gt" "*" is evaluated before "le" "*"
   * is evaluated before "ge"
   *
   * "*" is evaluated before "==" "*" is evaluated before "!=" "*" is evaluated
   * before "eq" "*" is evaluated before "ne"
   */
  @Test
  public void elMultiPreRelationalTest() throws Exception {

    this.testOrderPrecedence("{6 > 5 * 2}", false);
    this.testOrderPrecedence("{3 * 2 < 8}", true);
    this.testOrderPrecedence("{6 >= 5 * 2}", false);
    this.testOrderPrecedence("{6 * 2 <= 12}", true);
    this.testOrderPrecedence("{5 * 1 gt 6}", false);
    this.testOrderPrecedence("{6 lt 5 * 2}", true);
    this.testOrderPrecedence("{5 * 1 ge 6}", false);
    this.testOrderPrecedence("{6 le 5 * 2}", true);

    this.testOrderPrecedence("{5 == 5 * 2}", false);
    this.testOrderPrecedence("{5 * 2 != 10}", false);
    this.testOrderPrecedence("{10 eq 5 * 2}", true);
    this.testOrderPrecedence("{15 * 1 ne 1}", true);

  }

  /*
   * @testName: elDivPreRelationalTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "div, /" is evaluated before ">" "div, /" is evaluated before
   * "<" "div, /" is evaluated before ">=" "div, /" is evaluated before "<="
   * "div, /" is evaluated before "lt" "div, /" is evaluated before "gt"
   * "div, /" is evaluated before "le" "div, /" is evaluated before "ge"
   *
   * "div, /" is evaluated before "==" "div, /" is evaluated before "!="
   * "div, /" is evaluated before "eq" "div, /" is evaluated before "ne"
   */
  @Test
  public void elDivPreRelationalTest() throws Exception {

    for (String s : DIVOPER) {
      this.testOrderPrecedence("{3 > 4  " + s + " 2}", true);
      this.testOrderPrecedence("{12 " + s + " 2 < 5}", false);
      this.testOrderPrecedence("{4 >= 6 " + s + " 24}", true);
      this.testOrderPrecedence("{16 " + s + " 2 <= 5}", false);
      this.testOrderPrecedence("{6 gt 5 " + s + " 2}", true);
      this.testOrderPrecedence("{12 " + s + " 1 lt 5}", false);
      this.testOrderPrecedence("{6 ge 5 " + s + " 2}", true);
      this.testOrderPrecedence("{50 " + s + " 2 le 5}", false);

      this.testOrderPrecedence("{1 == 2 " + s + " 2}", true);
      this.testOrderPrecedence("{10 " + s + " 5 != 5}", true);
      this.testOrderPrecedence("{5 eq 5 " + s + " 2}", false);
      this.testOrderPrecedence("{2 ne 4 " + s + " 2}", false);
    }

  }

  /*
   * @testName: elModPreRelationalTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "mod, %" is evaluated before ">" "mod, %" is evaluated before
   * "<" "mod, %" is evaluated before ">=" "mod, %" is evaluated before "<="
   * "mod, %" is evaluated before "lt" "mod, %" is evaluated before "gt"
   * "mod, %" is evaluated before "le" "mod, %" is evaluated before "ge"
   *
   * "mod, %" is evaluated before "==" "mod, %" is evaluated before "!="
   * "mod, %" is evaluated before "eq" "mod, %" is evaluated before "ne"
   */
  @Test
  public void elModPreRelationalTest() throws Exception {

    for (String s : MODOPER) {
      this.testOrderPrecedence("{4 " + s + " 15 > 1}", true);
      this.testOrderPrecedence("{5 < 6 " + s + " 2}", false);
      this.testOrderPrecedence("{6 " + s + " 29 >= 5}", true);
      this.testOrderPrecedence("{6 <= 5 " + s + " 2}", false);
      this.testOrderPrecedence("{3 " + s + " 8 gt 1}", true);
      this.testOrderPrecedence("{6 lt 5 " + s + " 2}", false);
      this.testOrderPrecedence("{8 " + s + " 5 ge 2}", true);
      this.testOrderPrecedence("{6 le 5 " + s + " 2}", false);

      this.testOrderPrecedence("{3 " + s + " 2 == 1}", true);
      this.testOrderPrecedence("{5 != 5 " + s + " 2}", true);
      this.testOrderPrecedence("{6 " + s + " 2 eq 5}", false);
      this.testOrderPrecedence("{2 ne 5 " + s + " 3}", false);
    }

  }

  /*
   * @testName: elMultiEqualPreAndTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "*, ==" is evaluated before "&&, and"
   */
  @Test
  public void elMultiEqualPreAndTest() throws Exception {

    for (String a : ANDOPER) {
      this.testOrderPrecedence("{10 == 5 * 2 " + a + " 6 * 2 == 15}", false);
      this.testOrderPrecedence("{10 == 5 * 2 " + a + " 6 * 2 == 12}", true);
    }

  }

  /*
   * @testName: elDivEqualPreAndTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "div, /, ==" is evaluated before "&&, and"
   */
  @Test
  public void elDivEqualPreAndTest() throws Exception {

    for (String d : DIVOPER) {
      for (String a : ANDOPER) {
        this.testOrderPrecedence(
            "{12 " + d + " 2 == 6 " + a + " 10 " + d + " 2  == 5}", true);
        this.testOrderPrecedence(
            "{12 " + d + " 3 == 6 " + a + " 10 " + d + " 2  == 5}", false);
      }
    }

  }

  /*
   * @testName: elModEqualPreAndTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "mod, %, ==" is evaluated before "&&, and"
   */
  @Test
  public void elModEqualPreAndTest() throws Exception {

    for (String m : MODOPER) {
      for (String a : ANDOPER) {
        this.testOrderPrecedence(
            "{15 " + m + " 4 == 3 " + a + " 3 " + m + " 3 == 0}", true);
        this.testOrderPrecedence(
            "{15 " + m + " 3 == 3 " + a + " 3 " + m + " 3 == 0}", false);
      }
    }

  }

  /*
   * @testName: elMultiEqualOrCondTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "*, ==" is evaluated before "||, or"
   */
  @Test
  public void elMultiEqualOrCondTest() throws Exception {

    for (String o : OROPER) {
      this.testOrderPrecedence("{10 == 5 * 2 " + o + " 6 * 2 == 15}", true);
      this.testOrderPrecedence("{10 == 5 * 5 " + o + " 6 * 2 == 12}", true);
      this.testOrderPrecedence("{10 == 5 * 5 " + o + " 6 * 6 == 12}", false);
    }

  }

  /*
   * @testName: elDivEqualPreOrTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "div, /, ==" is evaluated before "||, or"
   */
  @Test
  public void elDivEqualPreOrTest() throws Exception {

    for (String d : DIVOPER) {
      for (String o : OROPER) {
        this.testOrderPrecedence(
            "{12 " + d + " 2 == 6 " + o + " 10 " + d + " 5  == 5}", true);
        this.testOrderPrecedence(
            "{12 " + d + " 3 == 6 " + o + " 10 " + d + " 2  == 5}", true);
        this.testOrderPrecedence(
            "{12 " + d + " 3 == 6 " + o + " 10 " + d + " 5  == 5}", false);
      }
    }

  }

  /*
   * @testName: elModEqualPreOrTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "mod, %, ==" is evaluated before "||, or"
   */
  @Test
  public void elModEqualPreOrTest() throws Exception {

    for (String m : MODOPER) {
      for (String o : OROPER) {
        this.testOrderPrecedence(
            "{15 " + m + " 4 == 3 " + o + " 4 " + m + " 3 == 0}", true);
        this.testOrderPrecedence(
            "{15 " + m + " 3 == 3 " + o + " 3 " + m + " 3 == 0}", true);
        this.testOrderPrecedence(
            "{15 " + m + " 3 == 3 " + o + " 8 " + m + " 3 == 0}", false);
      }
    }
  }

  /*
   * @testName: elMultiEqualPreCondTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "*" is evaluated before "? :"
   */
  @Test
  public void elMultiEqualPreCondTest() throws Exception {

    // These tests are designed to return the false if correct.
    this.testOrderPrecedence("{5 * 2 == 10 ? false : true}", false);
    this.testOrderPrecedence("{5 * 5 == 10 ? false : true}", true);

  }

  /*
   * @testName: elDivEqualPreCondTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "div, /, ==" is evaluated before "? :"
   */
  @Test
  public void elDivEqualPreCondTest() throws Exception {

    // These tests are designed to return the false if correct.
    for (String d : DIVOPER) {
      this.testOrderPrecedence("{20 " + d + " 2 == 10 ? false : true}", false);
      this.testOrderPrecedence("{24 " + d + " 2 == 10 ? false : true}", true);
    }
  }

  /*
   * @testName: elModEqualPreCondTest
   * 
   * @assertion_ids: EL:SPEC:28
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "mod, %, ==" is evaluated before "||, or"
   */
  @Test
  public void elModEqualPreCondTest() throws Exception {

    for (String m : MODOPER) {
      this.testOrderPrecedence("{21 " + m + " 2 == 1 ? false : true}", false);
      this.testOrderPrecedence("{15 " + m + " 3 == 3 ? false : true}", true);
    }
  }

  /*
   * @testName: elParenPreBinaryTest
   * 
   * @assertion_ids: EL:SPEC:28; EL:SPEC:27
   * 
   * @test_Strategy: Validate that the order of precedence is followed when an
   * EL Expression is evaluated.
   *
   * Rules tested: "( )" is evaluated before "+" "( )" is evaluated before "-"
   * "( )" is evaluated before "*" "( )" is evaluated before "/" "( )" is
   * evaluated before "%"
   */
  @Test
  public void elParenPreBinaryTest() throws Exception {

    // "+" tests
    this.testOrderPrecedence("{(2 + 3) - 10}", Long.valueOf(-5));
    this.testOrderPrecedence("{10 - (2 + 3)}", Long.valueOf(5));

    // "-" tests
    this.testOrderPrecedence("{(1 - 5) + 2}", Long.valueOf(-2));
    this.testOrderPrecedence("{2 + (5 - 1)}", Long.valueOf(6));

    // "*" tests
    this.testOrderPrecedence("{(1 + 5) * 2}", Long.valueOf(12));
    this.testOrderPrecedence("{2 * (1 + 5)}", Long.valueOf(12));

    // "/" tests
    this.testOrderPrecedence("{(4 + 4) / 2}", Double.valueOf(4));
    this.testOrderPrecedence("{2 / (4 + 4)}", Double.valueOf(0.25));

    // "%" tests
    this.testOrderPrecedence("{(2 + 7) % 2}", Long.valueOf(1));
    this.testOrderPrecedence("{18 % (8 + 7)}", Long.valueOf(3));

  }

  /*
   * @testName: functionPrecedenceTest
   * 
   * @assertion_ids: EL:SPEC:29
   * 
   * @test_Strategy: Validate that qualified functions with a namespace prefix
   * have precedence over the operators by constructing an expression which
   * cannot be parsed due to this rule.
   */
  @Test
  public void functionPrecedenceTest() throws Exception {

    boolean pass = false;

    ExpressionFactory expFactory = ExpressionFactory.newInstance();
    FuncMapperELContext context = new FuncMapperELContext();
    expFactory.createValueExpression(context, "${Int:val(10)}", Object.class);
    try {
      expFactory.createValueExpression(context, "${a?Int:val(10)}",
          Object.class);
    } catch (ELException ex) {
      pass = true;
    }

    if (!pass)
      throw new Exception("function precedence failed");

  }

  // ---------------------------------------------------------- private methods

  private void testOrderPrecedence(String testExpr, Object expectedResult)
      throws Exception {

    boolean pass = false;

    String[] symbol = { "$", "#" };
    String expr;

    try {
      for (String prefix : symbol) {
        expr = prefix + testExpr;

        logger.log(Logger.Level.TRACE, "Expression to test: " + expr);

        Object result = ExprEval.evaluateValueExpression(expr, null,
            Object.class);

        logger.log(Logger.Level.TRACE, "result is " + result.toString());
        pass = (ExprEval.compareValue(result, expectedResult));

        if (!pass)
          throw new Exception("TEST FAILED: pass = false");
      }
    } catch (Exception e) {
      throw new Exception(e);
    }
  }
}
