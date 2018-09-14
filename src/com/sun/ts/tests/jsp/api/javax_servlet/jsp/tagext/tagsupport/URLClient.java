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
 * @(#)URLClient.java	1.2 10/09/02
 */

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagsupport;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for Container interaction with objects implementing Tag..
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

    setContextRoot("/jsp_tagsupport_web");
    setTestJsp("TagSupportApiTest");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: tagSupportTagInitializationTest
   * 
   * @assertion_ids:
   * JSP:JAVADOC:342;JSP:JAVADOC:343;JSP:JAVADOC:344;JSP:JAVADOC:345;
   * JSP:JAVADOC:202;JSP:JAVADOC:203;JSP:JAVADOC:204;JSP:JAVADOC:205;
   * JSP:JAVADOC:206
   * 
   * @test_Strategy: Validates that the container performs the proper
   * initialization steps for a new tag handler instance. The PageContext,
   * parent Tag (if any), and all attributes must be set prior to calling
   * doStartTag().
   */
  public void tagSupportTagInitializationTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagsupport_web/TagInitializationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagSupportDoStartEvalBodyIncludeTest
   * 
   * @assertion_ids: JSP:JAVADOC:345
   * 
   * @test_Strategy: Validate the when doStartTag returns EVAL_BODY_INCLUDE will
   * include the evaluation of the body in the current out. This will be
   * verified by flushing the same out the tag should be using. The evaluated
   * body shouldn't be present in the stream after the flush. This also performs
   * validation on the method sequence called by the container.
   */
  public void tagSupportDoStartEvalBodyIncludeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagsupport_web/DoStartEvalBodyIncludeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: tagSupportDoStartSkipBodyTest
   * 
   * @assertion_ids: JSP:JAVADOC:402
   * 
   * @test_Strategy: Validate the when doStartTag returns SKIP_BODY, the body of
   * the tag is not included in the current out as the body related methods are
   * not called..
   */
  public void tagSupportDoStartSkipBodyTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagsupport_web/DoStartSkipBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: tagSupportDoAfterBodySkipBodyTest
   * 
   * @assertion_ids: JSP:JAVADOC:368
   * 
   * @test_Strategy: Validate the doAfterBody() is called exactly once when
   * doStartTag() returns EVAL_BODY_INCLUDE and doAfterBody() returns SKIP_BODY.
   */
  public void tagSupportDoAfterBodySkipBodyTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagsupport_web/DoAfterBodySkipBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Body Evaluated1|Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "Test FAILED|Body Evaluated2");
    invoke();
  }

  /*
   * @testName: tagSupportDoAfterBodyEvalBodyAgainTest
   * 
   * @assertion_ids: JSP:JAVADOC:369
   * 
   * @test_Strategy: Validate the doAfterBody() is called subsequent of
   * doAfterBody() being called and returning EVAL_BODY_AGAIN.
   */
  public void tagSupportDoAfterBodyEvalBodyAgainTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagsupport_web/DoAfterBodyEvalBodyAgainTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Body Evaluated1|Body Evaluated2");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "Test FAILED|Body Evaluated3");
    invoke();
  }

  /*
   * @testName: tagSupportDoEndTagSkipPageTest
   * 
   * @assertion_ids: JSP:JAVADOC:351;JSP:JAVADOC:347
   * 
   * @test_Strategy: Validate that page evaluation ceases when doEndTagReturns
   * SKIP_PAGE. This also ensures that doEndTag will not be called in any parent
   * tags.
   */
  public void tagSupportDoEndTagSkipPageTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagsupport_web/DoEndTagSkipPageTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagsupport_web/MethodValidation.jsp?name=interaction&methods=doStartTag,doEndTag HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagsupport_web/MethodValidation.jsp?name=parent&methods=doStartTag HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagSupportDoEndTagEvalPageTest
   * 
   * @assertion_ids: JSP:JAVADOC:350
   * 
   * @test_Strategy: Validate that if doEndTag() returns EVAL_PAGE, the page
   * continues to evaluate.
   */
  public void tagSupportDoEndTagEvalPageTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagsupport_web/DoEndTagEvalPageTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: tagSupportFindAncestorWithClassTest
   * 
   * @assertion_ids: JSP:JAVADOC:194
   * 
   * @test_Strategy: Validate the behavior of findAncestorWithClass when test
   * tag is nested with multiple tag instances of the same type.
   */
  public void tagSupportFindAncestorWithClassTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagsupport_web/FindAncestorTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagSupportDoStartTagDefaultValueTest
   * 
   * @assertion_ids: JSP:JAVADOC:195
   * 
   * @test_Strategy: Validate the default return value of
   * TagSupport.doStartTag().
   */
  public void tagSupportDoStartTagDefaultValueTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "doStartTagTest");
    invoke();
  }

  /*
   * @testName: tagSupportDoEndTagDefaultValueTest
   * 
   * @assertion_ids: JSP:JAVADOC:197
   * 
   * @test_Strategy: Validate the default return value of TagSupport.doEndTag().
   */
  public void tagSupportDoEndTagDefaultValueTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "doEndTagTest");
    invoke();
  }

  /*
   * @testName: tagSupportDoAfterBodyDefaultValueTest
   * 
   * @assertion_ids: JSP:JAVADOC:199
   * 
   * @test_Strategy: Validate the default return value of
   * TagSupport.doAfterBody().
   */
  public void tagSupportDoAfterBodyDefaultValueTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "doAfterBodyTest");
    invoke();
  }

  /*
   * @testName: tagSupportGetSetValueTest
   * 
   * @assertion_ids: JSP:JAVADOC:207;JSP:JAVADOC:208
   * 
   * @test_Strategy: Validate the behavior of TagSupport.setValue() and
   * TagSupport.getValue().
   */
  public void tagSupportGetSetValueTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "setGetValue");
    invoke();
  }

  /*
   * @testName: tagSupportGetValuesTest
   * 
   * @assertion_ids: JSP:JAVADOC:210
   * 
   * @test_Strategy: Validate the behavior of TagSupport.getValues().
   */
  public void tagSupportGetValuesTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "getValues");
    invoke();
  }

  /*
   * @testName: tagSupportRemoveValueTest
   * 
   * @assertion_ids: JSP:JAVADOC:209
   * 
   * @test_Strategy: Validate the behavior of TagSupport.removeValue().
   */
  public void tagSupportRemoveValueTest() throws Fault {
    TEST_PROPS.setProperty(APITEST, "removeValue");
    invoke();
  }

  /*
   * @testName: tagSupportVariableSynchronizationTest
   * 
   * @assertion_ids: JSP:JAVADOC:348;JSP:JAVADOC:353;JSP:JAVADOC:371
   * 
   * @test_Strategy: Validate scripting variables are synchronized at the proper
   * locations by the container.
   */
  public void tagSupportVariableSynchronizationTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagsupport_web/TagSupportSynchronizationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }
}
