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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.simpletagsupport;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for the default behavior of SimpleTagSupport.
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

    setContextRoot("/jsp_tagadapter_web");
    setTestJsp("TagAdapterTest");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: simpleTagSupportDoTagDefaultTest
   * 
   * @assertion_ids: JSP:JAVADOC:301;JSP:JAVADOC:356
   * 
   * @test_Strategy: This validates that the default behavior of
   * SimpleTagSupport.doTag() does nothing. If this is indeed the case, no
   * output will be displayed by the tag nested within the SimpleTagSupport
   * instance.
   */
  public void simpleTagSupportDoTagDefaultTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_simtagsupport_web/SimpleTagSupportDoTagDefault.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: simpleTagSupportSkipPageExceptionTest
   * 
   * @assertion_ids: JSP:JAVADOC:304;JSP:JAVADOC:359
   * 
   * @test_Strategy: Validate the containers behavior with regards to: - Simple
   * Tag Handler generated from a tag file throws a SkipPageException if an
   * invoked Classic Tag Handler returns SKIP_PAGE. - Simple Tag Handler
   * generated from a tag file throws a SkipPageException if an invoked Simple
   * Tag Handler throws a SkipPageException.
   */
  public void simpleTagSupportSkipPageExceptionTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_simtagsupport_web/SimpleTagSupportSkipPageClassicTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_simtagsupport_web/SimpleTagSupportSkipPageSimpleTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: simpleTagSupportGetSetJspContextTest
   * 
   * @assertion_ids: JSP:JAVADOC:307;JSP:JAVADOC:308;JSP:JAVADOC:362
   * 
   * @test_Strategy: Validate that getJspContext() returnes a non-null value as
   * the container called setJspContext() prior to invoking doGet().
   */
  public void simpleTagSupportGetSetJspContextTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_simtagsupport_web/SimpleTagSupportJspContextTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: simpleTagSupportGetSetJspBodyTest
   * 
   * @assertion_ids: JSP:JAVADOC:309;JSP:JAVADOC:310;JSP:JAVADOC:363
   * 
   * @test_Strategy: Validate that getJspBody() returnes a non-null value as the
   * container called setJspBody() prior to invoking doGet().
   */
  public void simpleTagSupportGetSetJspBodyTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_simtagsupport_web/SimpleTagSupportJspBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: simpleTagSupportGetSetParentTest
   * 
   * @assertion_ids: JSP:JAVADOC:305;JSP:JAVADOC:306;JSP:JAVADOC:360;
   * JSP:JAVADOC:361
   * 
   * @test_Strategy: Validate that getParent() returnes a non-null value as the
   * container called setParent() prior to invoking doGet().
   */
  public void simpleTagSupportGetSetParentTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_simtagsupport_web/SimpleTagSupportParentTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: simpleTagSupportFindAncestorTest
   * 
   * @assertion_ids: JSP:JAVADOC:311
   * 
   * @test_Strategy: Validate that findAncestorWithClass() where the validation
   * is preformed nested within a SimpleTag handler as well as a Classic Tag
   * handler.
   */
  public void simpleTagSupportFindAncestorTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_simtagsupport_web/SimpleTagSupportFindAncestorTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: simpleTagSupportVariableSynchronizationTest
   * 
   * @assertion_ids: JSP:JAVADOC:400
   * 
   * @testStrategy: Validate variable synchronization for AT_END and and
   * AT_BEGIN variables occurs after doTag() has been called. This should occur
   * for SimpleTags declared as Tags in the TLD using either TEI or through
   * variable elements, or for Tag files.
   */
  public void simpleTagSupportVariableSynchronizationTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_simtagsupport_web/SimpleTagSupportVariableSynchronizationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: tagHandlerCacheTest
   * 
   * @assertion_ids: JSP:JAVADOC:300
   * 
   * @test_Strategy: compare instances of a simple tag handler class across
   * different invocations.
   */

  public void tagHandlerCacheTest() throws Fault {
    String testName = "tagHandlerCacheTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_simtagsupport_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: emptySetJspBodyTest
   * 
   * @assertion_ids: JSP:JAVADOC:300
   * 
   * @test_Strategy: If the action element is empty in the page, setJpsBody
   * method is not called at all.
   */

  public void emptySetJspBodyTest() throws Fault {
    String testName = "emptySetJspBodyTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_simtagsupport_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: noParentTest
   * 
   * @assertion_ids: JSP:JAVADOC:300
   * 
   * @test_Strategy: The container invokes setParent() method only if this tag
   * invocation is nested within another tag invocation.
   */

  public void noParentTest() throws Fault {
    String testName = "noParentTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_simtagsupport_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspFragmentGetJspContextTest
   * 
   * @assertion_ids: JSP:JAVADOC:300
   * 
   * @test_Strategy: jspFragment.getJspContext() returns the JspContext that is
   * bound to this JspFragment.
   */

  public void jspFragmentGetJspContextTest() throws Fault {
    String testName = "jspFragmentGetJspContextTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_simtagsupport_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test PASSED in simple tag.|Test PASSED in classic tag.");
    invoke();
  }
}
