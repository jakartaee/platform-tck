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

/*
 * @(#)URLClient.java	1.1 12/09/02
 */

package com.sun.ts.tests.jsp.spec.core_syntax.actions.attribute;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
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

    setContextRoot("/jsp_core_act_attribute_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspAttributeFragmentTest
   * 
   * @assertion_ids: JSP:SPEC:168.3;JSP:SPEC:168.4
   * 
   * @test_Strategy: Validate that container passes an instance of JspFragment
   * when the target attribute is being set by jsp:attribute. This test will
   * validate the above statement using both Classic and Simple tag handlers.
   */
  public void jspAttributeFragmentTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeFragmentTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspAttributeUsageContextTest
   * 
   * @assertion_ids: JSP:SPEC:168.2;JSP:SPEC:168.11
   * 
   * @test_Strategy: Validate a translation-time error occurs for the following
   * use cases: - jsp:attribute is not a child of a standard or custom action. -
   * jsp:attribute is used to provide the attribute of another jsp:attribute
   * action.
   */
  public void jspAttributeUsageContextTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeUsageContextTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeUsageContextTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeUsageContextTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();

  }

  /*
   * @testName: jspAttributeFragmentNonScriptingBodyTest
   * 
   * @assertion_ids: JSP:SPEC:168.5
   * 
   * @test_Strategy: Validate a translation error occurs if providing an
   * attribute value using jsp:attribute to an attribute accepting JspFragment
   * values and the body of the jsp:attribute action contains scripting elements
   * (<%=, <%, <%!).
   */
  public void jspAttributeFragmentNonScriptingBodyTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeFragmentNonScrBodyTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeFragmentNonScrBodyTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeFragmentNonScrBodyTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeFragmentNonScrBodyTest4.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeFragmentNonScrBodyTest5.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeFragmentNonScrBodyTest6.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspAttributeDynamicAttributesTest
   * 
   * @assertion_ids: JSP:SPEC:168.6
   * 
   * @test_Strategy: Validate that dynamic attribute values provided via
   * jsp:attribute are provided to the tag handler as java.lang.String objects.
   */
  public void jspAttributeDynamicAttributesTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeDynamicTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeDynamicTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspAttributeRtExprTest
   * 
   * @assertion_ids: JSP:SPEC:168.7
   * 
   * @test_Strategy: Validate that if a tag handler (either Classic or Simple)
   * accepts RT expressions and the attribute values are provided via
   * jsp:attribute actions, type conversions are properly performed and the
   * attributes are set.
   */
  public void jspAttributeRtExprTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeRtTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED|"
        + "Test PASSED|Test PASSED|Test PASSED|Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspAttributeElementInteractionTest
   * 
   * @assertion_ids: JSP:SPEC:168.9
   * 
   * @test_Strategy: Validate that when a jsp:attribute element is nexted within
   * a jsp:element element within a JSP document, that the element is
   * constructed with element name being the value of the name attribute of
   * jsp:attribute and the body of the element being the body of the
   * jsp:attribute element.
   *
   */
  public void jspAttributeElementInteractionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeElementInteractionTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<test name=|passed|Test PASSED|</test>");
    invoke();
  }

  /*
   * @testName: jspAttributeTrimTest
   * 
   * @assertion_ids: JSP:SPEC:168.13;JSP:SPEC:168.14;JSP:SPEC:168.15
   * 
   * @test_Strategy: Validate the behavior of the trim attribute when set to
   * true or false, or when not specified when handling static values at
   * translation time vs. runtime.
   */
  public void jspAttributeTrimTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeTrimTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test PASSED|Test PASSED|Test PASSED|Test PASSED|Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: jspAttributeDuplicatedAttributeTest
   * 
   * @assertion_ids: JSP:SPEC:168.1.2
   * 
   * @test_Strategy: Validate that if an attribute is already specified for an
   * action (using the xml style) and a jsp:attribute action is used to define
   * the value for that same attribute, a translation-time error occurs.
   */
  public void jspAttributeDuplicatedAttributeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeDuplicatedAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspAttributeQNameTest
   * 
   * @assertion_ids: JSP:SPEC:168.1.4;JSP:SPEC:168.12
   * 
   * @test_Strategy: Validate the following: - qname attribute names can be
   * provided to the name attribute of jsp:attribute an work the same as a
   * non-qname value, as long as the qname prefix matches the prefix of the
   * surrounding action. - If the qname prefix doesn't match the prefix of the
   * surrounding action, a translation error must occur.
   */
  public void jspAttributeQNameTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeQNameTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeQNameTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeQNameTest3.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<test test:status=|passed|Test PASSED|</test>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeQNameTest4.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "dynamic|dynamic value");
    invoke();
  }

  /*
   * @testName: jspAttributeEmptyBodyTest
   * 
   * @assertion_ids: JSP:SPEC:168.10
   * 
   * @test_Strategy: Validate that if the body of jsp:attribute is empty, it
   * sets the value of the attribute to "".
   */
  public void jspAttributeEmptyBodyTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeEmptyBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: jspAttributeNoAttributeFoundTest
   * 
   * @assertion_ids: JSP:SPEC:168.1.1
   * 
   * @test_Strategy: Validate that if jsp:attribute specifies an attribute of an
   * action that does not exist, and that action does not accept dynamic
   * attributes, a translation-time error occurs.
   */
  public void jspAttributeNoAttributeFoundTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeNoAttributeFoundTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspAttributeNameRequiredAttributeTest
   * 
   * @assertion_ids: JSP:SPEC:168.1.3
   * 
   * @test_Strategy: Validate the name attribute is indeed a required attribute
   * of the jsp:attribute standard action by omitting it and checking for a
   * translation-time error.
   */
  public void jspAttributeNameRequiredAttributeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_attribute_web/JspAttributeNameReqAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
