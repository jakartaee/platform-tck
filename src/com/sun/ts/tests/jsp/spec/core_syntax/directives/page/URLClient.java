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

package com.sun.ts.tests.jsp.spec.core_syntax.directives.page;

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

    setGeneralURI("/jsp/spec/core_syntax/directives/page");
    setContextRoot("/jsp_coresyntx_directive_page_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: positiveBuffAutoflushTest
   * 
   * @assertion_ids: JSP:SPEC:44; JSP:SPEC:42
   * 
   * @test_Strategy: Leaving the defaults for autoFlush and buffer, validate
   * that the buffer is automatically flushed once the buffer is full.
   */

  public void positiveBuffAutoflushTest() throws Fault {
    String testName = "positiveBuffAutoflush";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(RESPONSE_MATCH, "5999");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: negativeBuffAutoflushTest
   * 
   * @assertion_ids: JSP:SPEC:45
   * 
   * @test_Strategy: set autoFlash to false when buffer=none, resulting in a
   * translation error.
   */

  public void negativeBuffAutoflushTest() throws Fault {
    String testName = "negativeBuffAutoflush";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveBuffCreateTest
   * 
   * @assertion_ids: JSP:SPEC:41
   * 
   * @test_Strategy: Validate that the page can configure a buffer and set the
   * autoFlush attribute to false. Write data to the output stream and manually
   * flush the content
   */

  public void positiveBuffCreateTest() throws Fault {
    String testName = "positiveBuffCreate";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(RESPONSE_MATCH, "999");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: negativeDuplicateBufferFatalTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two buffer attributes of
   * different values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateBufferFatalTranslationErrorTest() throws Fault {
    String testName = "negativeDuplicateBufferFatalTranslationError";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateBufferFatalTranslationError2Test
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare two page directives with buffer attributes of
   * different values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateBufferFatalTranslationError2Test() throws Fault {
    String testName = "negativeDuplicateBufferFatalTranslationError2";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateAutoFlushFatalTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two autoFlush attributes of
   * different values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateAutoFlushFatalTranslationErrorTest()
      throws Fault {
    String testName = "negativeDuplicateAutoFlushFatalTranslationError";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateAutoFlushFatalTranslationError2Test
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare two page directives with autoflush attributes of
   * different values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateAutoFlushFatalTranslationError2Test()
      throws Fault {
    String testName = "negativeDuplicateAutoFlushFatalTranslationError2";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateIsThreadSafeFatalTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two isThreadSafe attributes
   * of different values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateIsThreadSafeFatalTranslationErrorTest()
      throws Fault {
    String testName = "negativeDuplicateIsThreadSafeFatalTranslationError";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateIsThreadSafeFatalTranslationError2Test
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare two page directives with isThreadSafe attributes of
   * different values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateIsThreadSafeFatalTranslationError2Test()
      throws Fault {
    String testName = "negativeDuplicateIsThreadSafeFatalTranslationError2";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateIsErrorPageFatalTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two isErrorPage attributes of
   * different values Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateIsErrorPageFatalTranslationErrorTest()
      throws Fault {
    String testName = "negativeDuplicateIsErrorPageFatalTranslationError";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateIsErrorPageFatalTranslationError2Test
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare two page directives with isErrorPage attributes of
   * different values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateIsErrorPageFatalTranslationError2Test()
      throws Fault {
    String testName = "negativeDuplicateIsErrorPageFatalTranslationError2";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateIsELIgnoredFatalTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two isELIgnored attributes of
   * different values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateIsELIgnoredFatalTranslationErrorTest()
      throws Fault {
    String testName = "negativeDuplicateIsELIgnoredFatalTranslationError";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateIsELIgnoredFatalTranslationError2Test
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare two page directives with isELIgnored attributes of
   * different values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateIsELIgnoredFatalTranslationError2Test()
      throws Fault {
    String testName = "negativeDuplicateIsELIgnoredFatalTranslationError2";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeBufferOverflowExceptionTest
   * 
   * @assertion_ids: JSP:SPEC:44
   * 
   * @test_Strategy: Declare a page directive with autoFlush set to false.
   * Overflow the buffer and verify the Exception is caught.
   */

  public void negativeBufferOverflowExceptionTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "negativeBufferOverflowException");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positiveContentTypeTest
   * 
   * @assertion_ids: JSP:SPEC:52
   * 
   * @test_Strategy: Using the page directive, set the contentType attribute to
   * "text/plain;charset=ISO-8859-1". Verify on the client side that the
   * Content-Type header was properly set in the response.
   */
  public void positiveContentTypeTest() throws Fault {
    String testName = "positiveContenttype";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type:text/plain;charset=ISO-8859-1");
    invoke();

  }

  /*
   * @testName: negativeDuplicateContentFatalTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with contentType attributes
   * specified of different values. Validate that a fatal translation error
   * occurs.
   *
   */

  public void negativeDuplicateContentFatalTranslationErrorTest() throws Fault {
    String testName = "negativeDuplicateContentFatalTranslationError";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateContentFatalTranslationError2Test
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare two page directives with contentType attributes of
   * different values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateContentFatalTranslationError2Test()
      throws Fault {
    String testName = "negativeDuplicateContentFatalTranslationError2";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveDefaultIsErrorPageTest
   * 
   * @assertion_ids: JSP:SPEC:49
   * 
   * @test_Strategy: Verify that the 'isErrorPage' attribute is false by
   * generating an exception in the called page and then have the error page
   * attempt to access the implicit exception object.
   */

  public void positiveDefaultIsErrorPageTest() throws Fault {
    String testName = "positiveDefaultIsErrorPage";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();

  }

  /*
   * @testName: positiveErrorPageTest
   * 
   * @assertion_ids: JSP:SPEC:48
   * 
   * @test_Strategy: In the initial JSP page, generate a java.lang.Arithmetic
   * Exception by dividing an int value by 0. Validate the following: The
   * errorPage attributes value must be used instead of the error-page defined
   * in web.xml
   */

  public void positiveErrorPageTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveErrorPage");
    TEST_PROPS.setProperty(IGNORE_STATUS_CODE, "true");
    invoke();
  }

  /*
   * @testName: negativeFatalTranslationErrorTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Generate an exception from the requested page with the
   * errorPage attribute set. The Error page has isErrorPage set to false and
   * will attempt to access the exception object.
   * 
   * A fatal translation error shall result if a JSP error page has the
   * isErrorPage attribute set to false and an attempt is made to access the
   * implicit exception object. JavaServer Pages Specification v1.2, Sec 2.10.1
   */
  public void negativeFatalTranslationErrorTest() throws Fault {
    String testName = "negativeFatalTranslationError";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();

  }

  /*
   * @testName: negativeDuplicateErrorPageFatalTranslationErrorTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Declare a page directive with two errorPage attributes.
   * Validate that a fatal translation error occurs.
   * 
   * Duplicate errorPage attributes/values within a given translation unit shall
   * result in a fatal translation error. JavaServer Pages Specification v1.2,
   * Sec 2.10.1
   */

  public void negativeDuplicateErrorPageFatalTranslationErrorTest()
      throws Fault {
    String testName = "negativeDuplicateErrorPageFatalTranslationError";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateErrorPageFatalTranslationError2Test
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare two page directives with errorPage attributes of
   * different values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateErrorPageFatalTranslationError2Test()
      throws Fault {
    String testName = "negativeDuplicateErrorPageFatalTranslationError2";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveExtendsTest
   * 
   * @assertion_ids: JSP:SPEC:33
   * 
   * @test_Strategy: Provide the extends attribute with a fully qualified class.
   * The resulting JSP implementation class will use instanceof to validate that
   * this page instance is an instance of the class that it extends.
   */

  public void positiveExtendsTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveExtends");
    invoke();
  }

  /*
   * @testName: negativeDuplicateExtendsFatalTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @assertion: Duplicate extends attributes with distinct values within a
   * given translation unit shall result in a fatal translation error.
   * JavaServer Pages Specification v1.2, Sec. 2.10.1
   * 
   * @test_Strategy: Declare a page directive with two extends attributes with
   * distinct values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateExtendsFatalTranslationErrorTest() throws Fault {
    String testName = "negativeDuplicateExtendsFatalTranslationError";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateExtendsFatalTranslationError2Test
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @assertion: Duplicate extends attributes with distinct values within a
   * given translation unit
   * 
   * @test_Strategy: Declare two page directives with extends attributes with
   * distinct values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateExtendsFatalTranslationError2Test()
      throws Fault {
    String testName = "negativeDuplicateExtendsFatalTranslationError2";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveImportTest
   * 
   * @assertion_ids: JSP:SPEC:34
   * 
   * @test_Strategy: Use the import attribute to import 'java.util.Properties'.
   * Validated that a Properties object can be created and used.
   */

  public void positiveImportTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveImport");
    invoke();
  }

  /*
   * @testName: implicitImportLangTest
   * 
   * @assertion_ids: JSP:SPEC:35
   * 
   * @test_Strategy: Validate that classes from the java.lang package are
   * implicitly imported by creating and using a java.lang.Integer object.
   * PENDING Merge with exising import tests.
   */

  public void implicitImportLangTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "implicitImportLang");
    invoke();
  }

  /*
   * @testName: implicitImportJspTest
   * 
   * @assertion_ids: JSP:SPEC:35
   * 
   * @test_Strategy: Validate that classes from the javax.servlet.jsp package
   * are implicitly imported by calling JspFactory.getDefaultFactory() method.
   */

  public void implicitImportJspTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "implicitImportJsp");
    invoke();
  }

  /*
   * @testName: implicitImportServletTest
   * 
   * @assertion_ids: JSP:SPEC:35
   * 
   * @test_Strategy: Validate that classes from the javax.servlet package are
   * implicitly imported by creating and using an instance of RequestDispatcher.
   */

  public void implicitImportServletTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "implicitImportServlet");
    invoke();
  }

  /*
   * @testName: implicitImportHttpTest
   * 
   * @assertion_ids: JSP:SPEC:35
   * 
   * @test_Strategy: Validate that classes from the javax.servlet.http package
   * are implicitly imported by creating and using an instance of HttpUtils.
   */

  public void implicitImportHttpTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "implicitImportHttp");
    invoke();
  }

  /*
   * @testName: positiveMultipleImportTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two import attributes.
   *
   */

  public void positiveMultipleImportTest() throws Fault {
    String testName = "positiveMultipleImport";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: negativeMultiplePageEncodingTest
   * 
   * @assertion_ids: JSP:SPEC:229.21
   * 
   * @test_Strategy: Declare a page directive with two pageEncoding attributes.
   *
   */

  public void negativeMultiplePageEncodingTest() throws Fault {
    String testName = "negativeMultiplePageEncoding";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveInfoTest
   * 
   * @assertion_ids: JSP:SPEC:47
   * 
   * @test_Strategy: Set the info attribute of the page directive. Call
   * getServletInfo().
   */

  public void positiveInfoTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveInfo");
    invoke();
  }

  /*
   * @testName: negativeDuplicateInfoFatalTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two info attributes of
   * different values and Validate that a fatal translation error occurs.
   *
   */

  public void negativeDuplicateInfoFatalTranslationErrorTest() throws Fault {
    String testName = "negativeDuplicateInfoFatalTranslationError";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateInfoFatalTranslationError2Test
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare two page directives with info attributes of
   * different values and Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateInfoFatalTranslationError2Test() throws Fault {
    String testName = "negativeDuplicateInfoFatalTranslationError2";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveLangTest
   * 
   * @assertion_ids: JSP:SPEC:32
   * 
   * @test_Strategy: Validate that the language attribute can be set to "java"
   * without an error.
   */

  public void positiveLangTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveLang");
    invoke();
  }

  /*
   * @testName: negativeDuplicateLanguageFatalTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two language attributes of
   * different values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateLanguageFatalTranslationErrorTest()
      throws Fault {
    String testName = "negativeDuplicateLanguageFatalTranslationError";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateLanguageFatalTranslationError2Test
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare two page directives with language attributes of
   * different values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateLanguageFatalTranslationError2Test()
      throws Fault {
    String testName = "negativeDuplicateLanguageFatalTranslationError2";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveSessionTest
   * 
   * @assertion_ids: JSP:SPEC:36
   * 
   * @test_Strategy: Set the session attribute to 'true' and validate that the
   * implicit session variable can be accessed and used.
   */

  public void positiveSessionTest() throws Fault {
    String testName = "positiveSession";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(RESPONSE_MATCH, "got true");
    invoke();
  }

  /*
   * @testName: positiveSessionDefaultTest
   * 
   * @assertion_ids: JSP:SPEC:38
   * 
   * @test_Strategy: Do not set the session attribute in the page. Validate that
   * the implicit session variable can be accessed and used.
   */

  public void positiveSessionDefaultTest() throws Fault {
    String testName = "positiveSessionDefault";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(RESPONSE_MATCH, "got true");
    invoke();
  }

  /*
   * @testName: negativeSessionFatalTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:37
   * 
   * @test_Strategy: Validate that setting the session attribute to false will
   * result in a fatal translation error if the implicit session variable is
   * referenced.
   */

  public void negativeSessionFatalTranslationErrorTest() throws Fault {
    String testName = "negativeSessionFatalTranslationError";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateSessionFatalTranslationErrorTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two session attributes of
   * different values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateSessionFatalTranslationErrorTest() throws Fault {
    String testName = "negativeDuplicateSessionFatalTranslationError";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDuplicateSessionFatalTranslationError2Test
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare two page directives with session attributes of
   * different values. Validate that a fatal translation error occurs.
   */

  public void negativeDuplicateSessionFatalTranslationError2Test()
      throws Fault {
    String testName = "negativeDuplicateSessionFatalTranslationError2";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveDuplicateBufferTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two identical buffer
   * attributes.
   */

  public void positiveDuplicateBufferTest() throws Fault {
    String testName = "positiveDuplicateBuffer";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: positiveDuplicateAutoFlushTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two identical autoFlush
   * attributes.
   */

  public void positiveDuplicateAutoFlushTest() throws Fault {
    String testName = "positiveDuplicateAutoFlush";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: positiveDuplicateIsThreadSafeTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two identical isThreadSafe
   * attributes.
   */

  public void positiveDuplicateIsThreadSafeTest() throws Fault {
    String testName = "positiveDuplicateIsThreadSafe";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: positiveDuplicateIsErrorPageTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two identical isErrorPage
   * attributes.
   */

  public void positiveDuplicateIsErrorPageTest() throws Fault {
    String testName = "positiveDuplicateIsErrorPage";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: positiveDuplicateIsELIgnoredTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two identical isELIgnored
   * attributes.
   */

  public void positiveDuplicateIsELIgnoredTest() throws Fault {
    String testName = "positiveDuplicateIsELIgnored";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: positiveDuplicateContentTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two identical contentType
   * attributes specified.
   *
   */

  public void positiveDuplicateContentTest() throws Fault {
    String testName = "positiveDuplicateContent";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: positiveDuplicateErrorPageTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two identical errorPage
   * attributes.
   */

  public void positiveDuplicateErrorPageTest() throws Fault {
    String testName = "positiveDuplicateErrorPage";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: positiveDuplicateExtendsTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two identical extends
   * attributes.
   */

  public void positiveDuplicateExtendsTest() throws Fault {
    String testName = "positiveDuplicateExtends";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: positiveDuplicateInfoTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two identical info
   * attributes.
   *
   */

  public void positiveDuplicateInfoTest() throws Fault {
    String testName = "positiveDuplicateInfo";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: positiveDuplicateLanguageTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two identical language
   * attributes.
   */

  public void positiveDuplicateLanguageTest() throws Fault {
    String testName = "positiveDuplicateLanguage";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: positiveDuplicateSessionTest
   * 
   * @assertion_ids: JSP:SPEC:21
   * 
   * @test_Strategy: Declare a page directive with two identical session
   * attributes.
   */

  public void positiveDuplicateSessionTest() throws Fault {
    String testName = "positiveDuplicateSession";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: negativeBufferSuffixTest
   * 
   * @assertion_ids: JSP:SPEC:40
   * 
   * @test_Strategy: The suffix kb is mandatory or a translation error must
   * occur.
   */

  public void negativeBufferSuffixTest() throws Fault {
    String testName = "negativeBufferSuffix";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + "2.jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: errorPageExceptionAttributeTest
   * 
   * @assertion_ids: JSP:SPEC:50
   * 
   * @test_Strategy: Throwable object is transferred by the throwing page
   * implementation to the error page implementation by saving the object
   * reference on the common ServletRequest object using the setAttribute
   * method, with a name of javax.servlet.jsp.jspException (for
   * backwards-compatibility) and also javax.servlet.error.exception (for
   * compatibility with the servlet specification).
   */

  public void errorPageExceptionAttributeTest() throws Fault {
    String testName = "errorPageExceptionAttributeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test PASSED|javax.servlet.error.exception and javax.servlet.jsp.jspException are the same");
    TEST_PROPS.setProperty(IGNORE_STATUS_CODE, "true");
    invoke();
  }

  /*
   * @testName: negativeImportUtilTest
   * 
   * @assertion_ids: JSP:SPEC:267
   * 
   * @test_Strategy: Verify that a jsp page does not import java.util.* by
   * default.
   */
  public void negativeImportUtilTest() throws Fault {
    String testName = "negativeImportUtil";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeImportIoTest
   * 
   * @assertion_ids: JSP:SPEC:267
   * 
   * @test_Strategy: Verify that a jsp page does not import java.io.* by
   * default.
   */
  public void negativeImportIoTest() throws Fault {
    String testName = "negativeImportIo";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: isELIgnoredTrueTemplateTextTest
   * 
   * @assertion_ids: JSP:SPEC:268
   * 
   * @test_Strategy: [IsELIgnored] Verify that EL expressions are ignored by the
   * container in template text when the IsELIgnored page directive attribute is
   * set to true.
   */
  public void isELIgnoredTrueTemplateTextTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "IsELIgnoredTrueTemplateTextTest");
    invoke();
  }

  /*
   * @testName: isELIgnoredFalseTemplateTextDollarTest
   * 
   * @assertion_ids: JSP:SPEC:268
   * 
   * @test_Strategy: [IsELIgnored] Verify that EL expressions are recognized by
   * the container in template text when the IsELIgnored page directive
   * attribute is set to false and the '$' character is used.
   */
  public void isELIgnoredFalseTemplateTextDollarTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "IsELIgnoredFalseTemplateTextDollarTest");
    invoke();
  }

  /*
   * @testName: isELIgnoredFalseTemplateTextPoundTest
   * 
   * @assertion_ids: JSP:SPEC:268
   * 
   * @test_Strategy: [IsELIgnored] Verify that EL expressions are recognized by
   * the container in template text when the IsELIgnored page directive
   * attribute is set to false and the '#' character is used. Since the use of
   * the '#' character is illegal in template text, a translation error is
   * expected.
   */
  public void isELIgnoredFalseTemplateTextPoundTest() throws Fault {
    String testName = "IsELIgnoredFalseTemplateTextPoundTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: isELIgnoredFalseActionTest
   * 
   * @assertion_ids: JSP:SPEC:268
   * 
   * @test_Strategy: [IsELIgnored] Verify that EL expressions are recognized by
   * the container in actions when the IsELIgnored page directive attribute is
   * set to false.
   */
  public void isELIgnoredFalseActionTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "IsELIgnoredFalseActionTest");
    invoke();
  }

  /*
   * @testName: isELIgnoredTrueActionTest
   * 
   * @assertion_ids: JSP:SPEC:268
   * 
   * @test_Strategy: [IsELIgnored] Verify that EL expressions are ignored by the
   * container in actions when the IsELIgnored page directive attribute is set
   * to true.
   */
  public void isELIgnoredTrueActionTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "IsELIgnoredTrueActionTest");
    invoke();
  }

  /*
   * @testName: deferredSyntaxAllowedAsLiteralFalseTemplateTextTest
   * 
   * @assertion_ids: JSP:SPEC:269
   * 
   * @test_Strategy: [DeferredSyntaxAllowedAsLiteralPageDirective] Verify that
   * when the DeferredSyntaxAllowedAsLiteral page directive attribute is set to
   * false, a translation error occurs when the '{#' character sequence is used
   * in template text.
   */
  public void deferredSyntaxAllowedAsLiteralFalseTemplateTextTest()
      throws Fault {
    String testName = "DeferredSyntaxAllowedAsLiteralFalseTemplateTextTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: deferredSyntaxAllowedAsLiteralTrueTemplateTextTest
   * 
   * @assertion_ids: JSP:SPEC:269
   * 
   * @test_Strategy: [DeferredSyntaxAllowedAsLiteralPageDirective] Verify that
   * when the DeferredSyntaxAllowedAsLiteral page directive attribute is set to
   * true, the '{#' character sequence is allowed in template text.
   */
  public void deferredSyntaxAllowedAsLiteralTrueTemplateTextTest()
      throws Fault {
    TEST_PROPS.setProperty(STANDARD,
        "DeferredSyntaxAllowedAsLiteralTrueTemplateTextTest");
    invoke();
  }

  /*
   * @testName: deferredSyntaxAllowedAsLiteralFalseActionTest
   * 
   * @assertion_ids: JSP:SPEC:269
   * 
   * @test_Strategy: [DeferredSyntaxAllowedAsLiteralPageDirective] Verify that
   * when the DeferredSyntaxAllowedAsLiteral page directive attribute is set to
   * false, a translation error occurs when the '{#' character sequence is used
   * in an action.
   */
  public void deferredSyntaxAllowedAsLiteralFalseActionTest() throws Fault {
    String testName = "DeferredSyntaxAllowedAsLiteralFalseActionTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: deferredSyntaxAllowedAsLiteralTrueActionTest
   * 
   * @assertion_ids: JSP:SPEC:269
   * 
   * @test_Strategy: [DeferredSyntaxAllowedAsLiteralPageDirective] Verify that
   * when the DeferredSyntaxAllowedAsLiteral page directive attribute is set to
   * true, the '{#' character sequence is allowed in an action.
   */
  public void deferredSyntaxAllowedAsLiteralTrueActionTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD,
        "DeferredSyntaxAllowedAsLiteralTrueActionTest");
    invoke();
  }
}
