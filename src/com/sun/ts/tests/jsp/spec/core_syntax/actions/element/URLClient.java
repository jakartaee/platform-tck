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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.element;

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

    setContextRoot("/jsp_core_act_element_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspElementTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the use of jsp:element with a simple body (not
   * using jsp:body).
   */
  public void jspElementTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1>|body1|</element1>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1>|body1|</element1>");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "xmlns:jsp|http://java.sun.com/JSP/Page");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1>|body1|</element1>");
    invoke();
  }

  /*
   * @testName: jspElementJspAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the use of jsp:attribute as a child of
   * jsp:element. The attributes specified by jsp:attribute should be translated
   * into the attributes of the new XML element.
   */
  public void jspElementJspAttributeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementJspAttributeTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1 attr1=|value1");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementJspAttributeTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1 attr1=|value1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "http://java.sun.com/JSP/Page");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementJspAttributeTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1 attr1=|value1");
    invoke();
  }

  /*
   * @testName: jspElementJspBodyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the use of jsp:body as a chile of jsp:element. The
   * body of the generated element should be that specified by jsp:body.
   */
  public void jspElementJspBodyTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementJspBodyTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1>|body1|</element1>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementJspBodyTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1>|body1|</element1>");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "http://java.sun.com/JSP/Page");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementJspBodyTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1>|body1|</element1>");
    invoke();
  }

  /*
   * @testName: jspElementNameReqAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the name attribute of the jsp:element action
   * is indeed required by the container.
   */
  public void jspElementNameReqAttributeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementNameReqAttributeTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementNameReqAttributeTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementNameReqAttributeTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();

  }

  /*
   * @testName: jspElementDynamicAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the name attribute of jsp:element can accept both
   * EL and RT expressions.
   */
  public void jspElementDynamicAttributeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementDynamicAttributeTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<element1>|body1|</element1>|<element2>|body2|</element2>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementDynamicAttributeTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<element1>|body1|</element1>|<element2>|body2|</element2>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementDynamicAttributeTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<element1>|body1|</element1>|<element2>|body2|</element2>");
    invoke();
  }
}
