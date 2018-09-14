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
 * $Id$
 */

package com.sun.ts.tests.jsp.spec.core_syntax.scripting.el;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

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
   * @testName: elInvocationAndUsageTest
   * 
   * @assertion_ids: JSP:SPEC:97;JSP:SPEC:99;JSP:SPEC:100
   * 
   * @test_Strategy: Validate EL expression will be evaluated by the container
   * when provided as attribute values for actions that accept dynamic values,
   * as body content when body content type is not tagdependent, as template
   * text, and that the expression is only evaluated using the construct
   * ${expr}.
   */
  public void elInvocationAndUsageTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElInvocationAndUsageTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Expression in template text: Evaluated|"
            + "These should not be evaluated: {pageScope.eval}, pageScope.eval}, {pageScope.eval|"
            + "Expression from attribute: Evaluated|"
            + "Expression in body of action: Evaluated|"
            + "Expression in body of tagdependent body content tag: ${pageScope.eval}");
    invoke();
  }

  /*
   * @testName: elStaticAttributeTest
   * 
   * @assertion_ids: JSP:SPEC:98
   * 
   * @test_Strategy: Validate EL expressions provided to an action that does not
   * accept dynamic attributes will result in a translation error.
   */
  public void elStaticAttributeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElStaticAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: elAttributeSearchTest
   * 
   * @assertion_ids: JSP:SPEC:118
   * 
   * @test_Strategy: Validate that if no scope qualifier is provided, attributes
   * are located in the PageContext using the semantics of
   * PageContext.findAttribute(String). If no attribute can be found, the
   * expression evaluation must be null.
   */
  public void elAttributeSearchTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElAttributeSearchTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elBooleanLiteralTest
   * 
   * @assertion_ids: JSP:SPEC:101
   * 
   * @test_Strategy: Validate that the EL Boolean literal evaluated as expected.
   */
  public void elBooleanLiteralTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElBooleanLiteralTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elNullLiteralTest
   * 
   * @assertion_ids: JSP:SPEC:105
   * 
   * @test_Strategy: Validate that EL Null literal evaluated as expected.
   */
  public void elNullLiteralTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElNullLiteralTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elIntegerLiteralTest
   * 
   * @assertion_ids: JSP:SPEC:102
   * 
   * @test_Strategy: Validate that EL Integer literal evaluated as expected.
   */
  public void elIntegerLiteralTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElIntegerLiteralTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elFloatingPointLiteralTest
   * 
   * @assertion_ids: JSP:SPEC:103
   * 
   * @test_Strategy: Validate that EL Floating Point literal evaluated as
   * expected.
   */
  public void elFloatingPointLiteralTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElFloatingPointLiteralTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elStringLiteralTest
   * 
   * @assertion_ids: JSP:SPEC:104
   * 
   * @test_Strategy: Validate that EL String literal evaluated as expected.
   */
  public void elStringLiteralTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElStringLiteralTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elBinaryOperators1Test
   * 
   * @assertion_ids: JSP:SPEC:107.1;JSP:SPEC:107.2;JSP:SPEC:107.3;JSP:SPEC:107.4
   * 
   * @test_Strategy: Validate the binary operators, '+', '-', and '*' when using
   * Doubles, Floats, and Strings.
   */
  public void elBinaryOperators1Test() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElBinaryOperators1Test.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elBinaryOperators2Test
   * 
   * @assertion_ids: JSP:SPEC:108.1;JSP:SPEC:108.2;JSP:SPEC:108.3
   * 
   * @test_Strategy: Validate the binary operators, '/' and 'div' when using
   * different types.
   */
  public void elBinaryOperators2Test() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElBinaryOperators2Test.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elBinaryOperators3Test
   * 
   * @assertion_ids: JSP:SPEC:109.1;JSP:SPEC:109.2;JSP:SPEC:109.3;JSP:SPEC:109.4
   * 
   * @test_Strategy: Validate the binary operator '%' and 'mod' when using
   * different types.
   */
  public void elBinaryOperators3Test() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElBinaryOperators3Test.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elBinaryOperators4Test
   * 
   * @assertion_ids: JSP:SPEC:113.1
   * 
   * @test_Strategy: Validate the binary operator '&&', 'and', '||', and "or'
   * when using different types.
   */
  public void elBinaryOperators4Test() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElBinaryOperators4Test.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elUnaryOperators1Test
   * 
   * @assertion_ids: JSP:SPEC:110.1;JSP:SPEC:110.2.1;JSP:SPEC:110.2.2;
   * JSP:SPEC:110.2.3;JSP:SPEC:110.3
   * 
   * @test_Strategy: Validate the unary minus ('-') operator with variaous types
   * and validate the evaluation of the expression returns the expected value.
   */
  public void elUnaryOperators1Test() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElUnaryOperators1Test.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elUnaryOperators2Test
   * 
   * @assertion_ids: JSP:SPEC:114.1
   * 
   * @test_Strategy: Validate the unary minus ('!' and 'not') returns the
   * expected value.
   */
  public void elUnaryOperators2Test() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElUnaryOperators2Test.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elRelationalOperators1Test
   * 
   * @assertion_ids: JSP:SPEC:111.1;JSP:SPEC:111.2;JSP:SPEC:111.3;
   * JSP:SPEC:111.4;JSP:SPEC:111.8;JSP:SPEC:111.5; JSP:SPEC:111.6
   * 
   * @test_Strategy: Validate the relational operators '<', '>', '<=', '>=',
   * 'lt', 'gt', 'le', 'ge' when one value is null, as well as the various types
   * (Double, Float, Short, etc). Verify the relational operation behaves as
   * expected.
   */
  public void elRelationalOperators1Test() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElRelationalOperators1Test.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elRelationalOperators2Test
   * 
   * @assertion_ids: JSP:SPEC:112.1;JSP:SPEC:112.2;JSP:SPEC:112.3;
   * JSP:SPEC:112.4;JSP:SPEC:112.5;JSP:SPEC:112.6; JSP:SPEC:112.6.2
   * 
   * @test_Strategy: Validate the relational operators '==', '!=', 'ne', 'eq',
   * when one value is null or both values are null, as well as the various
   * types (Double, Float, Short, etc). Verify the relational operation behaves
   * as expected.
   */
  public void elRelationalOperators2Test() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElRelationalOperators2Test.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elEmptyOperatorTest
   * 
   * @assertion_ids: JSP:SPEC:115.1;JSP:SPEC:115.2;JSP:SPEC:115.3;
   * JSP:SPEC:115.4;JSP:SPEC:115.5;JSP:SPEC:115.6
   * 
   * @test_Strategy: Validate the behavior of the EL 'empty' operator when used
   * to evaluate null, String, array, Map, and Collection values (on the case of
   * the array, map, and Collection, provide emtpy and non-empty values)
   */
  public void elEmptyOperatorTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElEmptyOperatorTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elStringCoercionTest
   * 
   * @assertion_ids: JSP:SPEC:135.1;JSP:SPEC:135.2;JSP:SPEC:135.4;
   * JSP:SPEC:134.5
   * 
   * @testStrategy: Validate that various can be successfully corerced to a
   * String using the EL string coercion rules. Also validate any error
   * conditions that can occur during the coercion.
   */
  public void elStringCoercionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElStringCoercionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elNumberCoercionTest
   * 
   * @assertion_ids: JSP:SPEC:136.1;JSP:SPEC:136.2;JSP:SPEC:136.3;
   * JSP:SPEC:136.4;JSP:SPEC:136.5;JSP:SPEC:136.6; JSP:SPEC:136.7;JSP:SPEC:136.8
   * 
   * @testStrategy: Validate the EL is able to convert between the various
   * number types successfully based on varying input and target values. Also
   * validate any error conditions that can occur during the coercion.
   */
  public void elNumberCoercionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElNumberCoercionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elCharacterCoercionTest
   * 
   * @assertion_ids: JSP:SPEC:137.1;JSP:SPEC:137.2;JSP:SPEC:137.3;
   * JSP:SPEC:137.4;JSP:SPEC:137.5;JSP:SPEC:137.6
   * 
   * @testStrategy: Validate that the EL is able to convert various types to a
   * Character. Also validate any error conditions that can occur during the
   * coercion.
   */
  public void elCharacterCoercionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElCharacterCoercionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elBooleanCoercionTest
   * 
   * @assertion_ids: JSP:SPEC:138.1;JSP:SPEC:138.2;JSP:SPEC:138.3;
   * JSP:SPEC:138.4;JSP:SPEC:138.5
   * 
   * @testStrategy: Validate that the EL is able to convert various types to a
   * Boolean. Also validate any error conditions that can occur during the
   * coercion.
   */
  public void elBooleanCoercionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElBooleanCoercionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elOtherTypeCoercionTest
   * 
   * @assertion_ids: JSP:SPEC:139.1;JSP:SPEC:139.2;JSP:SPEC:139.3;
   * JSP:SPEC:139.6
   * 
   * @testStrategy: Validate that the EL is able to convert various types
   * including invoking propery editors if target type has PropertyEditory.
   * Additionally validate any specified error conditions.
   */
  public void elOtherTypeCoercionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElOtherTypeCoercionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: elPrimitiveBoxedTypeConversionTest
   * 
   * @assertion_ids: JSP:SPEC:134.1;JSP:SPEC:134.2;JSP:SPEC:134.4
   * 
   * @testStrategy: Validate that if the target type is primitive and the
   * resulting EL evaluation is a boxed type, the result is 'unboxed' and passed
   * to the attribute. If a runtime error doens't occur due to a type problem,
   * the test passes.
   */
  public void elPrimitiveBoxedTypeConversionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElPrimitiveBoxedTypeConversionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: elFunctionInvocationTest
   * 
   * @assertion_ids: JSP:SPEC:122.5;JSP:SPEC:122.6
   * 
   * @test_Strategy: Validation an EL Function can be successfully invoked.
   */
  public void elFunctionInvocationTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElFunctionInvocationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: elNonExistingFunctionNameTest
   * 
   * @assertion_ids: JSP:SPEC:122.3
   * 
   * @test_Strategy: Validate a translation error occurs if the specified
   * function name doesn't exist in the imported taglibrary.
   */
  public void elNonExistingFunctionNameTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElNonExistingFunctionNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: elNonMatchingFunctionSignatureTest
   * 
   * @assertion_ids: JSP:SPEC:122.4
   * 
   * @test_Strategy: Validate a translation error occurs if the specified
   * function find a matching name, but the signature doesn't match.
   */
  public void elNonMatchingFunctionSignatureTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElNonMatchingFunctionSignatureTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: elNonStaticPublicFunctionTest
   * 
   * @assertion_ids: JSP:SPEC:120
   * 
   * @test_Strategy: Validate a translation error occurs if the specified
   * function is not defined a public static.
   */
  public void elNonStaticPublicFunctionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ElNonStaticPublicFunctionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: elTernaryOperatorTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the operation/evaluation of the container when
   * presented with ternary operator expressions.
   */
  public void elTernaryOperatorTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/ELTernaryOperatorTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test PASSED|Test PASSED|Test PASSED|Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: elBinaryOperatorParenthesesTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the operation/evaluation of the container when
   * presented with binary operator expressions with parentheses.
   */
  public void elBinaryOperatorParenthesesTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_script_el_web/elBinaryOperatorParenthesesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "true|true|true|true|2|1.4");
    invoke();
  }

}
