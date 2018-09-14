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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.root;

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

    setContextRoot("/jsp_core_act_root_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspRootUsageContextTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that a translation time error is raised when
   * jsp:root is used in JSP or Tag files in standard syntax but not in JSP
   * Documents, or Tag files in XML syntax.
   */
  public void jspRootUsageContextTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootUsageContextTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootUsageContextTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<status>Test PASSED</status>");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "jsp:root");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootUsageContextTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<status>Test PASSED</status>");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "jsp:root");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootUsageContextTest4.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspRootVersionAttrAllowableValuesTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the container accepts both "1.2" and "2.0" as
   * allowable values for the version attribute of the jsp:root element.
   */
  public void jspRootVersionAttrAllowableValuesTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootVersionAttrAllowableValuesTest1.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<status>Test PASSED</status>");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "jsp:root");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootVersionAttrAllowableValuesTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<status>Test PASSED</status>");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "jsp:root");
    invoke();
  }

  /*
   * @testName: jspRootVersionAttrInvalidValueTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the container generates a translation-time error
   * when the version attribute of jsp:root is provided a value other than "1.2"
   * or "2.0"
   */
  public void jspRootVersionAttrInvalidValueTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootVersionAttrInvalidValueTest1.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootVersionAttrInvalidValueTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspRootVersionReqAttrTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the version attribute of jsp:root is indeed
   * required by looking for a translation error from the container when the
   * attribute is not present.
   */
  public void jspRootVersionReqAttrTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootVersionReqAttrTest1.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootVersionReqAttrTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspRootJspBodyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that jsp:body can be used to specify the body of
   * the jsp:root action.
   */
  public void jspRootJspBodyTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootJspBodyTest1.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<status>Test PASSED</status>");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "jsp:root");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootJspBodyTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<status>Test PASSED</status>");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "jsp:root");
    invoke();
  }
}
