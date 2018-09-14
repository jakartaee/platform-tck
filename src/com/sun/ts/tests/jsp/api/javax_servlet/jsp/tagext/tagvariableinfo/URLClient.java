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

package com.sun.ts.tests.jsp.api.javax_servlet.jsp.tagext.tagvariableinfo;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for TagVariableInfo. Implementation note, all tests are performed
 * within a TagExtraInfo class. If the test fails, a translation error will be
 * generated and a ValidationMessage array will be returned.
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

    setContextRoot("/jsp_tagvarinfo_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: tagVariableInfoGetClassNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:189
   * 
   * @test_Strategy: Validate the JSP container properly parses the specified
   * TLD as refereneced by the taglib directive and that the TagVariableInfo
   * objects created at translation time return the expected values when calling
   * getClassName(). A translation error will occur if the test fails.
   */
  public void tagVariableInfoGetClassNameTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagvarinfo_web/GetClassNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: tagVariableInfoGetNameGivenTest
   * 
   * @assertion_ids: JSP:JAVADOC:187
   * 
   * @test_Strategy: Validate the JSP container properly parses the specified
   * TLD as refereneced by the taglib directive and that the TagVariableInfo
   * objects created at translation time return the expected values when calling
   * getNameGiven(). A translation error will occur if the test fails.
   */
  public void tagVariableInfoGetNameGivenTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagvarinfo_web/GetNameGivenTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: tagVariableInfoGetNameFromAttributeTest
   * 
   * @assertion_ids: JSP:JAVADOC:188
   * 
   * @test_Strategy: Validate the JSP container properly parses the specified
   * TLD as refereneced by the taglib directive and that the TagVariableInfo
   * objects created at translation time return the expected values when calling
   * getNameFromAttribute(). A translation error will occur if the test fails.
   */
  public void tagVariableInfoGetNameFromAttributeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagvarinfo_web/GetNameFromAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: tagVariableInfoGetDeclareTest
   * 
   * @assertion_ids: JSP:JAVADOC:190
   * 
   * @test_Strategy: Validate the JSP container properly parses the specified
   * TLD as refereneced by the taglib directive and that the TagVariableInfo
   * objects created at translation time return the expected values when calling
   * getDeclare(). A translation error will occur if the test fails.
   */
  public void tagVariableInfoGetDeclareTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagvarinfo_web/GetDeclareTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: tagVariableInfoGetScopeTest
   * 
   * @assertion_ids: JSP:JAVADOC:191
   * 
   * @test_Strategy: Validate the JSP container properly parses the specified
   * TLD as refereneced by the taglib directive and that the TagVariableInfo
   * objects created at translation time return the expected values when calling
   * getScope(). A translation error will occur if the test fails.
   */
  public void tagVariableInfoGetScopeTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagvarinfo_web/GetScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }
}
