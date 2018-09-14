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
 * $URL$ $LastChangedDate$
 */

package com.sun.ts.tests.jstl.spec.core.urlresource.importtag;

import java.io.PrintWriter;
import com.sun.javatest.Status;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.ts.tests.jstl.common.client.AbstractUrlClient;

public class JSTLClient extends AbstractUrlClient {

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /** Creates new JSTLClient */
  public JSTLClient() {
  }

  /*
   * public methods
   * ========================================================================
   */

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    JSTLClient theTests = new JSTLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setContextRoot("/jstl_core_url_import_web");
    setGoldenFileDir("/jstl/spec/core/urlresource/importtag");

    return super.run(args, out, err);
  }

  /*
   * @testName: positiveImportUrlTest
   * 
   * @assertion_ids: JSTL:SPEC:23; JSTL:SPEC:23.1; JSTL:SPEC:23.1.4
   * 
   * @testStrategy: Validate that the 'url' attribute accepts both dynamic and
   * static values.
   */
  public void positiveImportUrlTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveImportUrlTest");
    invoke();
  }

  /*
   * @testName: positiveImportAbsUrlTest
   * 
   * @assertion_ids: JSTL:SPEC:23.1.3; JSTL:SPEC:23.1.3.1;
   * 
   * @testStrategy: Validate that resources identified by an absolute URL can be
   * imported and displayed. This will test absolute URLs for HTTP and FTP
   */
  public void positiveImportAbsUrlTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveImportAbsUrlTest");
    invoke();
  }

  /*
   * @testName: positiveImportCtxRelUrlTest
   * 
   * @assertion_ids: JSTL:SPEC:23.1.1; JSTL:SPEC:23.1.1.1; JSTL:SPEC:23.20.2
   * 
   * @testStrategy: Validate that resources identified by a context-relative
   * path can be imported and displayed. Also validate that an imported resource
   * that sets the status to some 2xx value doesn't cause the page to throw an
   * Exception.
   */
  public void positiveImportCtxRelUrlTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveImportCtxRelUrlTest");
    invoke();
  }

  /*
   * @testName: positiveImportPageRelUrlTest
   * 
   * @assertion_ids: JSTL:SPEC:23.1.1; JSTL:SPEC:23.1.1.2; JSTL:SPEC:23.20.2
   * 
   * @testStrategy: Validate that resources identified by a page-relative path
   * can be imported and displayed. Also validate that an imported resource that
   * sets the status to some 2xx value doesn't cause the page to throw an
   * Exception.
   */
  public void positiveImportPageRelUrlTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveImportPageRelUrlTest");
    invoke();
  }

  /*
   * @testName: positiveImportVarTest
   * 
   * @assertion_ids: JSTL:SPEC:23.3
   * 
   * @testStrategy: Validate that content of the imported resource is associated
   * with the variable name defined by the 'var' attribute.
   */
  public void positiveImportVarTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveImportVarTest");
    invoke();
  }

  /*
   * @testName: positiveImportScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:23.4; JSTL:SPEC:23.4.1; JSTL:SPEC:23.4.2;
   * JSTL:SPEC:23.4.3; JSTL:SPEC:23.4.4
   * 
   * @testStrategy: Validate that the variables defined by 'var' are exported to
   * the appropriate scope based on the presence or absence of the 'scope'
   * attribute.
   */
  public void positiveImportScopeTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveImportScopeTest");
    invoke();
  }

  /*
   * @testName: positiveImportRelUrlEnvPropTest
   * 
   * @assertion_ids: JSTL:SPEC:23.1.1.3.1
   * 
   * @testStrategy: Import the content of a JSP via a relative URL. Validate
   * that Application, Session, Request, and Request Parameters are available to
   * the target resource.
   */
  public void positiveImportRelUrlEnvPropTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveImportRelUrlEnvPropTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveImportRelUrlEnvPropTest.jsp?reqpar=parm");
    TEST_PROPS.setProperty(GOLDENFILE, "positiveImportRelUrlEnvPropTest.gf");
    invoke();
  }

  /*
   * @testName: positiveImportAbsUrlEnvPropTest
   * 
   * @assertion_ids: JSTL:SPEC:23.1.3.2
   * 
   * @testStrategy: Import the content of a JSP via an absolute URL. Validate
   * that only the Application (Context) information is available to the target
   * resource.
   */
  public void positiveImportAbsUrlEnvPropTest() throws Fault {
    TEST_PROPS.setProperty(TEST_NAME, "positiveImportAbsUrlEnvPropTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveImportAbsUrlEnvPropTest.jsp?reqpar=parm");
    TEST_PROPS.setProperty(GOLDENFILE, "positiveImportAbsUrlEnvPropTest.gf");
    invoke();
  }

  /*
   * @testName: positiveImportCharEncodingTest
   * 
   * @assertion_ids: JSTL:SPEC:23.5
   * 
   * @testStrategy: Validate that if the charEncoding attribute is set that the
   * encoding is indeed applied to the imported resource.
   */
  public void positiveImportCharEncodingTest() throws Fault {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_core_url_import_web/positiveImportCharEncodingTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test PASSED|Test PASSED|Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: positiveImportCharEncodingNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:23.18
   * 
   * @testStrategy: Validate that if the charEncoding attribute is null or
   * empty, the action behaves as if it was not specified.
   */
  public void positiveImportCharEncodingNullEmptyTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveImportCharEncodingNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positiveImportBodyParamTest
   * 
   * @assertion_ids: JSTL:SPEC:25
   * 
   * @testStrategy: Validate the import action can properly handle nested param
   * subtags within the body content.
   */
  public void positiveImportBodyParamTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveImportBodyParamTest");
    invoke();
  }

  /*
   * @testName: positiveImportVarReaderTest
   * 
   * @assertion_ids: JSTL:SPEC:23.6; JSTL:SPEC:23.6.1; JSTL:SPEC:23.6.2
   * 
   * @testStrategy: Validate the following with varReader: - the type of the
   * exported variable (java.io.Reader). - the visibility of the exported
   * variable during - and after the import action completed (variable is not
   * visible after action completes).
   */
  public void positiveImportVarReaderTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveImportVarReaderTest");
    invoke();
  }

  /*
   * @testName: positiveImportFollowRedirectTest
   * 
   * @assertion_ids: JSTL:SPEC:23.1.3.3
   * 
   * @testStrategy: Validate that if the imported absolute resource sends a
   * redirect, the action properly follows the redirect and imports the target.
   */
  public void positiveImportFollowRedirectTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveImportFollowRedirectTest");
    invoke();
  }

  /*
   * @testName: negativeImportCtxInvalidValueTest
   * 
   * @assertion_ids: JSTL:SPEC:23.2.4
   * 
   * @testStrategy: Validate that if context is provided an invalid value
   * (content root without a leading slash) an Exception is thrown.
   */
  public void negativeImportCtxInvalidValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeImportCtxInvalidValueTest");
    invoke();
  }

  /*
   * @testName: negativeImportCtxUrlInvalidValueTest
   * 
   * @assertion_ids: JSTL:SPEC:23.2.3
   * 
   * @testStrategy: Validate that if context is specified, and the url attribute
   * is provided an incorrect value (a path with no leading slash), an Exception
   * is thrown.
   */
  public void negativeImportCtxUrlInvalidValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeImportCtxUrlInvalidValueTest");
    invoke();
  }

  /*
   * @testName: negativeImportUrlNullTest
   * 
   * @assertion_ids: JSTL:SPEC:23.15
   * 
   * @testStrategy: Validate that if the value of url is null, a
   * javax.servlet.jsp.JspException is thrown.
   */
  public void negativeImportUrlNullTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeImportUrlNullTest");
    invoke();
  }

  /*
   * @testName: negativeImportUrlEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:23.15
   * 
   * @testStrategy: Validate that if the value of url is empty ("") a
   * javax.servlet.jsp.JspException is thrown.
   */
  public void negativeImportUrlEmptyTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeImportUrlEmptyTest");
    invoke();
  }

  /*
   * @testName: negativeImportUrlInvalidTest
   * 
   * @assertion_ids: JSTL:SPEC:23.15
   * 
   * @testStrategy: Validate that if url is provided an invalid URL an instance
   * of javax.servlet.jsp.JspException is thrown.
   */
  public void negativeImportUrlInvalidTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeImportUrlInvalidTest");
    invoke();
  }

  /*
   * @testName: negativeImportAbsResourceNon2xxTest
   * 
   * @assertion_ids: JSTL:SPEC:23.20.2
   * 
   * @testStrategy: Validate that if an imported absolute resource has a return
   * code other than 2xx, a JspException is thrown with the path and status code
   * included in the Exception message. In this case, the test will import a JSP
   * via an absolute URL that sets the response to 410.
   */
  public void negativeImportAbsResourceNon2xxTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeImportAbsResourceNon2xxTest");
    invoke();
  }

  /*
   * @testName: negativeImportRequestDispatcherNon2xxTest
   * 
   * @assertion_ids: JSTL:SPEC:23.19.4
   * 
   * @testStrategy: Validate that if an imported local resource sets a response
   * not in the 2xx range, a JspException is thrown with the path and status
   * code included in the Exception message. In this case, the test will import
   * a JSP via a relative URL that sets the response to 410.
   */
  public void negativeImportRequestDispatcherNon2xxTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD,
        "negativeImportRequestDispatcherNon2xxTest");
    invoke();
  }

  /*
   * @testName: negativeImportRequestDispatcherRTIOExceptionTest
   * 
   * @assertion_ids: JSTL:SPEC:23.19.2
   * 
   * @testStrategy: Validate that if the included resource throws either an
   * IOException or RuntimeException, a JspException is thrown, with the message
   * of original exception included in the message and the original exception as
   * the root cause.
   */
  public void negativeImportRequestDispatcherRTIOExceptionTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD,
        "negativeImportRequestDispatcherRTIOExceptionTest");
    invoke();
  }

  /*
   * @testName: negativeImportRequestDispatcherServletExceptionTest
   * 
   * @assertion_ids: JSTL:SPEC:23.19.3.1; JSTL:SPEC:23.19.3.2
   * 
   * @testStrategy: Validate that if the included resource throws a
   * ServletException and a root cause is present, the message of the exception
   * text is that of the original root cause, and the root cause of the
   * JspException is set to be the root cause of the ServletException, otherwise
   * if no root cause is present, the message of the ServletException is used
   * and the ServletException is the root cause of the JspException.
   */
  public void negativeImportRequestDispatcherServletExceptionTest()
      throws Fault {
    TEST_PROPS.setProperty(STANDARD,
        "negativeImportRequestDispatcherServletExceptionTest");
    invoke();
  }

  /*
   * @testName: positiveImportEncodingNotSpecifiedTest
   * 
   * @assertion_ids: JSTL:SPEC:23.21.1; JSTL:SPEC:23.21.2
   * 
   * @test_Strategy: Validate that the import action will use the encoding
   * specified by the response of the imported resource. If the imported
   * resource specifies no charset, then the default charset of ISO-8859-1 will
   * be used.
   */
  public void positiveImportEncodingNotSpecifiedTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveImportEncodingNotSpecifiedTest");
    invoke();
  }

}
