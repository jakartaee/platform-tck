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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.invoke;

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

    setContextRoot("/jsp_core_act_invoke_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspInvokeUsageContextTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the following usage contexts of jsp:invoke: -
   * jsp:invoke present in a JSP or JSP document is a translation error. -
   * jsp:invoke is valid within a tag file.
   */
  public void jspInvokeUsageContextTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeUsageContextTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeUsageContextTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeUsageContextTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: jspInvokeVarTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the behavior of jsp:invoke when the var attribute
   * is specified. The tag file should export the result of the invocation to a
   * request-scoped variable. The type and value of the exported variable will
   * be validated by the invoking page.
   */
  public void jspInvokeVarTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeVarTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspInvokeVarReaderTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the behavior of jsp:invoke when the varReader
   * attribute is specified. The tag file should export the result of the
   * invocation to a request-scoped variable. The type and value of the exported
   * variable will be validated by the invoking page as well as verification
   * that the exported reader is resettable.
   */
  public void jspInvokeVarReaderTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeVarReaderTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspInvokeScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the behavior of jsp:invoke when the scope
   * attribute is and is not specified. If not specified, the result of the
   * invocation should be in the page scope of the tag file. If the scope is
   * 'page' the result of the invocation should be in the page scope of the tag
   * file. If scope is specified as 'request', 'session', or 'application', the
   * result of the invocation should be in the page context of the invoking
   * page.
   */
  public void jspInvokeScopeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test PASSED|Test PASSED|Test PASSED|Test PASSED|Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspInvokeVarVarReaderTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if both var and varReader are specified
   * within one particular jsp:invoke action, a translation- time error is
   * raised.
   */
  public void jspInvokeVarVarReaderTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeVarVarReaderTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspInvokeNoVarVarReaderTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if the scope attribute of jsp:invoke is
   * specified but neither the var nor varReader are specified, a
   * translation-time error is raised.
   */
  public void jspInvokeNoVarVarReaderTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeNoVarVarReaderScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspInvokeNotInSessionTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that jsp:invoke will cause an
   * IllegalStateException to be raised if the jsp:invoke action tries to export
   * a result into the session scope where the calling page does not participate
   * in a session.
   */
  public void jspInvokeNotInSessionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeNotInSessionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspInvokeJspAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the attributes of jspInvoke can all be specified
   * using the jsp:attribute action.
   */
  public void jspInvokeJspAttributeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeJspAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspInvokeInvalidScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that a translation-time error is generated if the
   * scope attribute of jsp:invoke is provided an invalid value (i.e. not
   * 'page', 'request', 'session', or 'application').
   */
  public void jspInvokeInvalidScopeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeInvalidScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeInvalidPageScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeInvalidRequestScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeInvalidSessionScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeInvalidApplicationScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspInvokeFragmentReqAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the 'fragment' attribute of jsp:invoke is
   * indeed required by the container. Validate by calling jsp:invoke without
   * the attribute and look for a translation- time error.
   */
  public void jspInvokeFragmentReqAttributeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeFragmentReqAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspInvokeNonEmptyBodyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate a translation-time error is raised if the
   * jsp:invoke action has a non-empty body.
   */
  public void jspInvokeNonEmptyBodyTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeNonEmptyBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspFragmentNullTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: If the fragment identified by the given name is null, will
   * behave as though a fragment was passed in that produces no output.
   */
  public void jspFragmentNullTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/jspFragmentNullTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "BEGINEND");
    invoke();
  }
}
