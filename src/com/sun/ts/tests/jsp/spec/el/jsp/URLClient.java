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

package com.sun.ts.tests.jsp.spec.el.jsp;

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

    setGeneralURI("/jsp/spec/el/jsp/");
    setContextRoot("/jsp_el_jsp_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: positiveELDeferredValueValueTest
   * 
   * @assertion_ids: JSP:SPEC:282
   * 
   * @test_Strategy: [ELDeferredValueValue] In a jsp page, pass a String literal
   * expression and an expression using the #{} syntax to a tag handler via a
   * deferred-value element in the tld file. Verify that the expressions are
   * accepted and that their values are transmitted correctly to the tag
   * handler.
   */
  public void positiveELDeferredValueValueTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/positiveELDeferredValueValue.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeELDeferredValueValueTest
   * 
   * @assertion_ids: JSP:SPEC:282
   * 
   * @test_Strategy: [ELDeferredValueValue] In a jsp page, pass an expression
   * using the ${} syntax to a tag handler via a deferred-value element in the
   * tld file. Verify that a page translation error occurs.
   */
  public void negativeELDeferredValueValueTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/negativeELDeferredValueValue.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: eLDeferredValueCoercionTest
   * 
   * @assertion_ids: JSP:SPEC:284
   * 
   * @test_Strategy: [ELDeferredValueCoercion] In a jsp page, pass a String
   * literal expression and an expression using the #{} syntax to a tag handler
   * via a deferred-value element in the tld file. Verify that the expressions
   * are accepted, that their values are coerced to the expected type and
   * evaluate as expected.
   */
  public void eLDeferredValueCoercionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/ELDeferredValueCoercion.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: positiveELDeferredMethodValueTest
   * 
   * @assertion_ids: JSP:SPEC:287
   * 
   * @test_Strategy: [ELDeferredMethodValue] In a jsp page, pass a String
   * literal expression and an expression using the #{} syntax to a tag handler
   * via a deferred-method element in the tld file. Verify that the expressions
   * are accepted and that their values are transmitted correctly to the tag
   * handler.
   */
  public void positiveELDeferredMethodValueTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/positiveELDeferredMethodValue.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeELDeferredMethodValueTest
   * 
   * @assertion_ids: JSP:SPEC:287
   * 
   * @test_Strategy: [ELDeferredMethodValue] In a jsp page, pass an expression
   * using the ${} syntax to a tag handler via a deferred-method element in the
   * tld file. Verify that a page translation error occurs.
   */
  public void negativeELDeferredMethodValueTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/negativeELDeferredMethodValue.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: eLDeferredMethodStringLiteralTest
   * 
   * @assertion_ids: JSP:SPEC:288
   * 
   * @test_Strategy: [ELDeferredMethodStringLiteral] In a jsp page, pass a
   * literal expression representing a String and a literal expression
   * representing a Double to a tag handler via a deferred-method element in the
   * tld file. Verify that the expressions are accepted, that they are coerced
   * to the proper type and have the expected values.
   */
  public void eLDeferredMethodStringLiteralTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/ELDeferredMethodStringLiteral.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: eLDeferredMethodStringLiteralErrorTest1
   * 
   * @assertion_ids: JSP:SPEC:289
   * 
   * @test_Strategy: [ELDeferredMethodStringLiteralError] In a jsp page, pass a
   * string literal to a tag handler via a deferred-method element in the tld
   * file where the method signature -element has a return value of void. Verify
   * that a page translation error occurs.
   */
  public void eLDeferredMethodStringLiteralErrorTest1() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/ELDeferredMethodStringLiteralError1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: eLDeferredMethodStringLiteralErrorTest2
   * 
   * @assertion_ids: JSP:SPEC:289
   * 
   * @test_Strategy: [ELDeferredMethodStringLiteralError] In a jsp page, pass a
   * string literal to a tag handler via a deferred-method element in the tld
   * file where the string literal cannot be coerced to the return type of the
   * method signature. Verify that a page translation error occurs.
   */
  public void eLDeferredMethodStringLiteralErrorTest2() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/ELDeferredMethodStringLiteralError2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: templateTextPoundTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:272
   * 
   * @test_Strategy: [TemplateTextPoundTranslationError] Verify that the #{
   * character sequence, when embedded in template text, triggers a translation
   * error.
   */
  public void templateTextPoundTranslationErrorTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/TemplateTextPoundTranslationError.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: tagLibraryPoundTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:273
   * 
   * @test_Strategy: [TagLibraryPoundTranslationError] Verify that the #{
   * character sequence triggers a translation error if used for a tag attribute
   * of a tag library where the jsp-version is greater than or equal to 2.1, and
   * for which the attribute is not marked as a deferred expression in the TLD.
   */
  public void tagLibraryPoundTranslationErrorTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/TagLibraryPoundTranslationError.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: allowedDynamicAttributeValueTypesTest
   * 
   * @assertion_ids: JSP:SPEC:290
   * 
   * @test_Strategy: [AllowedDynamicAttributeValueTypes] In a jsp page, pass a
   * literal expression, a scriptlet expression, an expression using the ${}
   * syntax, and deferred-value and deferred-method expressions using the #{}
   * syntax as dynamic attributes to a tag handler. Verify that the expressions
   * are accepted.
   */
  public void allowedDynamicAttributeValueTypesTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/AllowedDynamicAttributeValueTypes.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: dynamicAttributeSetterMethodTest
   * 
   * @assertion_ids: JSP:SPEC:291
   * 
   * @test_Strategy: [DynamicAttributeSetterMethod] Verify that an argument to
   * the setter method of a dynamic attribute must be of type java.lang.Object.
   * Implement a tag handler with a setter method whose argument is a primitive
   * type and see that the container generates an internal server error.
   */
  public void dynamicAttributeSetterMethodTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_el_jsp_web/DynamicAttributeSetterMethod.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: eLJspVersionTest
   * 
   * @assertion_ids: JSP:SPEC:271
   * 
   * @test_Strategy: [EvaluationOf#{expr}] Verify that an expression using the
   * #{} syntax is processed as a string literal when the jsp-version element
   * specified in the TLD has a value of less than 2.1.
   */
  public void eLJspVersionTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "ELJspVersionTest");
    invoke();
  }
}
