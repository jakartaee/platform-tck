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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.bodytagsupport;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for BodyTagSupport.
 */
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

    setContextRoot("/jsp_bodytagsupp_web");
    setTestJsp("BodyTagSupportApiTest");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: bodyTagSupportCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:318
   * 
   * @test_Strategy: Validate the constructor of BodyTagSupport
   */
  public void bodyTagSupportCtorTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "bodyTagSupportCtorTest");
    invoke();
  }

  /*
   * @testName: bodyTagSupportDoStartTagTest
   * 
   * @assertion_ids: JSP:JAVADOC:319
   * 
   * @test_Strategy: Validate the default return value of
   * BodyTagSupport.doStartTag() is EVAL_BODY_BUFFERED.
   */
  public void bodyTagSupportDoStartTagTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "bodyTagSupportDoStartTagTest");
    invoke();
  }

  /*
   * @testName: bodyTagSupportDoEndTagTest
   * 
   * @assertion_ids: JSP:JAVADOC:321
   * 
   * @test_Strategy: Validate the default return value of
   * BodyTagSupport.doEndTag() is EVAL_PAGE.
   */
  public void bodyTagSupportDoEndTagTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "bodyTagSupportDoEndTagTest");
    invoke();
  }

  /*
   * @testName: bodyTagSupportDoAfterBodyTest
   * 
   * @assertion_ids: JSP:JAVADOC:326
   * 
   * @test_Strategy: Validate the default return value of
   * BodyTagSupport.doAfterBody() is SKIP_BODY.
   */
  public void bodyTagSupportDoAfterBodyTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "bodyTagSupportDoAfterBodyTest");
    invoke();
  }

  /*
   * @testName: bodyTagSupportGetBodyContentTest
   * 
   * @assertion_ids: JSP:JAVADOC:323;JSP:JAVADOC:329
   * 
   * @test_Strategy: Validate the behavior of getBodyContent(). This indirectly
   * ensures that the container properly called setBodyContent().
   */
  public void bodyTagSupportGetBodyContentTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodytagsupp_web/GetBodyContentTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: bodyTagSupportGetPreviousOutTest
   * 
   * @assertion_ids: JSP:JAVADOC:330
   * 
   * @test_Strategy: Validate the behavior of getPreviousOut.
   */
  public void bodyTagSupportGetPreviousOutTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodytagsupp_web/GetPreviousOutTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: bodyTagSupportEvalBodyBufferedTest
   * 
   * @assertion_ids:
   * JSP:JAVADOC:346;JSP:JAVADOC:374;JSP:JAVADOC:375;JSP:JAVADOC:324
   * 
   * @test_Strategy: Validate that the container properly calls setInitBody()
   * then doInitBody() after doStartTag() is called, prior to evaluating the
   * body.
   */
  public void bodyTagSupportEvalBodyBufferedTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodytagsupp_web/BodyTagEvalBodyBufferedTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: bodyTagSupportEvalBodyIncludeTest
   * 
   * @assertion_ids: JSP:JAVADOC:376
   * 
   * @test_Strategy: Validate that the container doesn't call setBodyContent()
   * and doInitBody() if doStartTag() returns EVAL_BODY_INCLUDE.
   */
  public void bodyTagSupportEvalBodyIncludeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodytagsupp_web/BodyTagEvalBodyIncludeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: bodyTagSupportSkipBodyTest
   * 
   * @assertion_ids: JSP:JAVADOC:402
   * 
   * @test_Strategy: Validate that the container doesn't call setInitBody() and
   * doInitBody() after doStartTag() returns SKIP_BODY.
   */
  public void bodyTagSupportSkipBodyTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodytagsupp_web/BodyTagSkipBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: bodyTagSupportEmptyTagTest
   *
   * @assertion_ids: JSP:JAVADOC:376;
   *
   * @test_Strategy: Validate that the container only calls setInitBody() and
   * doInitBody() if the tag is empty. 1. Empty Tag1: <foo></foo> 2. Empty Tag2:
   * <foo/> 3. Non-Empty Tag2: <foo> </foo>
   */
  public void bodyTagSupportEmptyTagTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodytagsupp_web/BodyTagEmptyTagTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Empty Tag1: Test PASSED|Empty Tag2: Test PASSED|Non-Empty Tag3: Test PASSED");
    invoke();
  }

  /*
   * @testName: bodyTagSupportVariableSynchronizationTest
   * 
   * @assertion_ids: JSP:JAVADOC:377
   * 
   * @test_Strategy: Validate scripting variables are properly synchornized.
   */
  public void bodyTagSupportVariableSynchronizationTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodytagsupp_web/BodyTagSupportSynchronizationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }
}
