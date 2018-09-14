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

package com.sun.ts.tests.jsp.spec.tagfiles.semantics;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import java.rmi.UnexpectedException;

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

    setContextRoot("/jsp_tagfile_semantics_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspTagSemanticsJspContextWrapperTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the container properly creates a JSP Context
   * wrapper, an instance of PageContext, for the tag file. Validate that this
   * wrapper is not the same JspContext as that of the invoking page (this
   * includes validate of the jspContext scripting variable).
   */
  public void jspTagSemanticsJspContextWrapperTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_semantics_web/JspTagSemanticsJspContextWrapperTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspTagSemanticsJspContextWrapperScopesTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the following: - the container presents the Tag
   * file with a clean page context. - the container provides the Tag file
   * access with to the same request, session, and application scope as that of
   * the invoking context. - Any changes to the page scope in the wrapper
   * context are not reflected in the invoking context. - Any changes to the
   * request, session, or application scopes of the wrapping context are
   * synchronized with the invoking context.
   */
  public void jspTagSemanticsJspContextWrapperScopesTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_semantics_web/JspTagSemanticsScopesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Wrapper Test PASSED|Test PASSED|Wrapper Test PASSED|Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspTagSemanticsDeclaredAttributesTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that a page scoped variable is created for each
   * declared and specified attribute defined by the tag. The variable name must
   * be the same as the attribute name and the variable value must be the same
   * as provided at invocation time.
   */
  public void jspTagSemanticsDeclaredAttributesTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_semantics_web/JspTagSemanticsDeclaredAttributesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspTagSemanticsAttributeNotSpecifiedTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if an attribute is declared as optional, and
   * that attribute is not specified at invocation time, no page scoped variable
   * is created.
   */
  public void jspTagSemanticsAttributeNotSpecifiedTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_semantics_web/JspTagSemanticsAttributeNotSpecifiedTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspTagSemanticsDynamicAttributesTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate dynamic attributes.
   */
  public void jspTagSemanticsDynamicAttributesTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfile_semantics_web/JspTagSemanticsDynamicAttributesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: semanticsJspForwardTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Upon return from the RequestDispather.forward method, the
   * generated tag handler must stop processing of the tag file and throw
   * java.servlet.jsp.SkipPageException.
   */

  public void semanticsJspForwardTest() throws Fault {
    String testName = "semanticsJspForward";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + "/jsp_tagfile_semantics_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: semanticsInvokeSimpleTagTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: invokes a simple tag handler which throws SkipPageException
   * in the doTag method, the generated tag handler must terminate and
   * SkipPageException must be thrown.
   */

  public void semanticsInvokeSimpleTagTest() throws Fault {
    String testName = "semanticsInvokeSimpleTag";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + "/jsp_tagfile_semantics_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: semanticsInvokeClassicTagTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: must allow such an invocation to occur.
   */

  public void semanticsInvokeClassicTagTest() throws Fault {
    String testName = "semanticsInvokeClassicTag";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + "/jsp_tagfile_semantics_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "MyClassicTag:Test PASSED|endOfTagFile|endOfCallingPage");
    invoke();
  }

}
