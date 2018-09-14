/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)URLClient.java	1.2 05/05/03
 */

package com.sun.ts.tests.jsp.spec.el.language;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

// these imports are invoked in jsps; declared here only to force 
// compilation so classes will be included in packaging
import com.sun.ts.tests.common.el.spec.Book;
import com.sun.ts.tests.jsp.common.tags.tck.SetTag;

public class URLClient extends AbstractUrlClient {

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: poundDollarSameMeaningTest
   * 
   * @assertion_ids: EL:SPEC:1
   * 
   * @test_Strategy: [PoundDollarSameMeaning] In a jsp page, set an EL variable,
   * then pass it to a tag handler as both ${expr} and #{expr}. Verify that the
   * tag handler's evaluation of both forms is identical.
   */
  public void poundDollarSameMeaningTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_language_web/PoundDollarSameMeaning.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: parseOnceEvalManyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: [ExprParsedEvalMany] In a jsp page, verify that once an
   * expression is parsed, it can be evaluated multiple times.
   */
  public void parseOnceEvalManyTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_language_web/ParseOnceEvalMany.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
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
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_language_web/RValueCoercion1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
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
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_language_web/RValueCoercion2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
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
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_language_web/LiteralExprEval1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
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
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_language_web/LiteralExprEval2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: eLSyntaxEscapeTest
   * 
   * @assertion_ids: EL:SPEC:8
   * 
   * @test_Strategy: [ELSyntaxEscape] Verify that the EL special characters '&'
   * and '#' are treated as literals when preceded with '\'.
   */
  public void eLSyntaxEscapeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_language_web/ELSyntaxEscape.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
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
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_language_web/LiteralExprAsMethodExpr1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
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
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_language_web/LiteralExprAsMethodExpr2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: compositeExprEval1Test
   * 
   * @assertion_ids: EL:SPEC:11
   * 
   * @test_Strategy: [CompositeExprEval] Verify that in a composite expression
   * eval-expressions are coerced to Strings according to the EL type conversion
   * rules and concatenated with any intervening literal-expressions.
   */
  public void compositeExprEval1Test() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_language_web/CompositeExprEval1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: compositeExprEval2Test
   * 
   * @assertion_ids: EL:SPEC:11
   * 
   * @test_Strategy: [CompositeExprEval] Verify that in a composite expression
   * eval-expressions are evaluated left to right, coerced to Strings according
   * to the EL type conversion rules, and concatenated with any intervening
   * literal-expressions.
   */
  public void compositeExprEval2Test() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_language_web/CompositeExprEval2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
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
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_language_web/DotAndIndexOperatorsSame.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
