/*
 * Copyright (c) 2007, 2021 Oracle and/or its affiliates and others.
 * All rights reserved.
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


import java.io.IOException;
import java.io.InputStream;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {



  public static String packagePath = URLClientIT.class.getPackageName().replace(".", "/");

  public URLClientIT() throws Exception {
    setup();

    setGeneralURI("/jsp/spec/core_syntax/directives/page");
    setContextRoot("/jsp_coresyntx_directive_page_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_coresyntx_directive_page_web.war");
    archive.addClasses(com.sun.ts.tests.jsp.common.util.JspTestUtil.class);
    archive.addPackages(false, Filters.exclude(URLClientIT.class),
            URLClientIT.class.getPackageName());
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_coresyntx_directive_page_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/iselignoredaction.tld", "iselignoredaction.tld");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/deferredsyntaxaction.tld", "deferredsyntaxaction.tld");    

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/willNotSee.jsp")), "willNotSee.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSessionDefault.jsp")), "positiveSessionDefault.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSession.jsp")), "positiveSession.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMultipleImport.jsp")), "positiveMultipleImport.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveLang.jsp")), "positiveLang.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveInfo.jsp")), "positiveInfo.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImport.jsp")), "positiveImport.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveExtends.jsp")), "positiveExtends.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveErrorPage.jsp")), "positiveErrorPage.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDuplicateSession.jsp")), "positiveDuplicateSession.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDuplicateLanguage.jsp")), "positiveDuplicateLanguage.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDuplicateIsThreadSafe.jsp")), "positiveDuplicateIsThreadSafe.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDuplicateIsErrorPage.jsp")), "positiveDuplicateIsErrorPage.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDuplicateIsELIgnored.jsp")), "positiveDuplicateIsELIgnored.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDuplicateInfo.jsp")), "positiveDuplicateInfo.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDuplicateExtends.jsp")), "positiveDuplicateExtends.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDuplicateErrorPage.jsp")), "positiveDuplicateErrorPage.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDuplicateContent.jsp")), "positiveDuplicateContent.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDuplicateBuffer.jsp")), "positiveDuplicateBuffer.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDuplicateAutoFlush.jsp")), "positiveDuplicateAutoFlush.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDefaultIsErrorPage.jsp")), "positiveDefaultIsErrorPage.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveContenttype.jsp")), "positiveContenttype.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveBuffCreate.jsp")), "positiveBuffCreate.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveBuffAutoflush.jsp")), "positiveBuffAutoflush.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeSessionFatalTranslationError.jsp")), "negativeSessionFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeMultiplePageEncoding.jsp")), "negativeMultiplePageEncoding.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeImportUtil.jsp")), "negativeImportUtil.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeImportIo.jsp")), "negativeImportIo.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeFatalTranslationError.jsp")), "negativeFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateSessionFatalTranslationError2.jsp")), "negativeDuplicateSessionFatalTranslationError2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateSessionFatalTranslationError.jsp")), "negativeDuplicateSessionFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateLanguageFatalTranslationError2.jsp")), "negativeDuplicateLanguageFatalTranslationError2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateLanguageFatalTranslationError.jsp")), "negativeDuplicateLanguageFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateIsThreadSafeFatalTranslationError2.jsp")), "negativeDuplicateIsThreadSafeFatalTranslationError2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateIsThreadSafeFatalTranslationError.jsp")), "negativeDuplicateIsThreadSafeFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateIsErrorPageFatalTranslationError2.jsp")), "negativeDuplicateIsErrorPageFatalTranslationError2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateIsErrorPageFatalTranslationError.jsp")), "negativeDuplicateIsErrorPageFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateIsELIgnoredFatalTranslationError2.jsp")), "negativeDuplicateIsELIgnoredFatalTranslationError2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateIsELIgnoredFatalTranslationError.jsp")), "negativeDuplicateIsELIgnoredFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateInfoFatalTranslationError2.jsp")), "negativeDuplicateInfoFatalTranslationError2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateInfoFatalTranslationError.jsp")), "negativeDuplicateInfoFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateExtendsFatalTranslationError2.jsp")), "negativeDuplicateExtendsFatalTranslationError2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateExtendsFatalTranslationError.jsp")), "negativeDuplicateExtendsFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateErrorPageFatalTranslationError2.jsp")), "negativeDuplicateErrorPageFatalTranslationError2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateErrorPageFatalTranslationError.jsp")), "negativeDuplicateErrorPageFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateContentFatalTranslationError2.jsp")), "negativeDuplicateContentFatalTranslationError2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateContentFatalTranslationError.jsp")), "negativeDuplicateContentFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateBufferFatalTranslationError2.jsp")), "negativeDuplicateBufferFatalTranslationError2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateBufferFatalTranslationError.jsp")), "negativeDuplicateBufferFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateAutoFlushFatalTranslationError2.jsp")), "negativeDuplicateAutoFlushFatalTranslationError2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDuplicateAutoFlushFatalTranslationError.jsp")), "negativeDuplicateAutoFlushFatalTranslationError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeBufferSuffix2.jsp")), "negativeBufferSuffix2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeBufferSuffix.jsp")), "negativeBufferSuffix.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeBufferOverflowException.jsp")), "negativeBufferOverflowException.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeBuffAutoflush.jsp")), "negativeBuffAutoflush.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/myerrorpage.jsp")), "myerrorpage.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/IsELIgnoredTrueTemplateTextTest.jsp")), "IsELIgnoredTrueTemplateTextTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/IsELIgnoredTrueActionTest.jsp")), "IsELIgnoredTrueActionTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/IsELIgnoredFalseTemplateTextPoundTest.jsp")), "IsELIgnoredFalseTemplateTextPoundTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/IsELIgnoredFalseTemplateTextDollarTest.jsp")), "IsELIgnoredFalseTemplateTextDollarTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/IsELIgnoredFalseActionTest.jsp")), "IsELIgnoredFalseActionTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/implicitImportServlet.jsp")), "implicitImportServlet.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/implicitImportLang.jsp")), "implicitImportLang.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/implicitImportJsp.jsp")), "implicitImportJsp.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/implicitImportHttp.jsp")), "implicitImportHttp.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/implicit.jsp")), "implicit.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/FatalTranslationErrorPage.jsp")), "FatalTranslationErrorPage.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/errorPageExceptionAttributeTestError.jsp")), "errorPageExceptionAttributeTestError.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/errorPageExceptionAttributeTest.jsp")), "errorPageExceptionAttributeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/errorpagedefault.jsp")), "errorpagedefault.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ErrorOnELNotFoundTrueTest.jsp")), "ErrorOnELNotFoundTrueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ErrorOnELNotFoundFalseTest.jsp")), "ErrorOnELNotFoundFalseTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/duplicateErrorPage.jsp")), "duplicateErrorPage.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/DeferredSyntaxAllowedAsLiteralTrueTemplateTextTest.jsp")), "DeferredSyntaxAllowedAsLiteralTrueTemplateTextTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/DeferredSyntaxAllowedAsLiteralTrueActionTest.jsp")), "DeferredSyntaxAllowedAsLiteralTrueActionTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/DeferredSyntaxAllowedAsLiteralFalseTemplateTextTest.jsp")), "DeferredSyntaxAllowedAsLiteralFalseTemplateTextTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/DeferredSyntaxAllowedAsLiteralFalseActionTest.jsp")), "DeferredSyntaxAllowedAsLiteralFalseActionTest.jsp");

    return archive;
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

  @Test
  public void positiveBuffAutoflushTest() throws Exception {
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

  @Test
  public void negativeBuffAutoflushTest() throws Exception {
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

  @Test
  public void positiveBuffCreateTest() throws Exception {
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

  @Test
  public void negativeDuplicateBufferFatalTranslationErrorTest() throws Exception {
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

  @Test
  public void negativeDuplicateBufferFatalTranslationError2Test() throws Exception {
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

  @Test
  public void negativeDuplicateAutoFlushFatalTranslationErrorTest()
      throws Exception {
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

  @Test
  public void negativeDuplicateAutoFlushFatalTranslationError2Test()
      throws Exception {
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

  @Test
  public void negativeDuplicateIsThreadSafeFatalTranslationErrorTest()
      throws Exception {
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

  @Test
  public void negativeDuplicateIsThreadSafeFatalTranslationError2Test()
      throws Exception {
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

  @Test
  public void negativeDuplicateIsErrorPageFatalTranslationErrorTest()
      throws Exception {
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

  @Test
  public void negativeDuplicateIsErrorPageFatalTranslationError2Test()
      throws Exception {
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

  @Test
  public void negativeDuplicateIsELIgnoredFatalTranslationErrorTest()
      throws Exception {
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

  @Test
  public void negativeDuplicateIsELIgnoredFatalTranslationError2Test()
      throws Exception {
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

  @Test
  public void negativeBufferOverflowExceptionTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeBufferOverflowException.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void positiveContentTypeTest() throws Exception {
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

  @Test
  public void negativeDuplicateContentFatalTranslationErrorTest() throws Exception {
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

  @Test
  public void negativeDuplicateContentFatalTranslationError2Test()
      throws Exception {
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

  @Test
  public void positiveDefaultIsErrorPageTest() throws Exception {
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

  @Test
  public void positiveErrorPageTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveErrorPage.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void negativeFatalTranslationErrorTest() throws Exception {
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

  @Test
  public void negativeDuplicateErrorPageFatalTranslationErrorTest()
      throws Exception {
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

  @Test
  public void negativeDuplicateErrorPageFatalTranslationError2Test()
      throws Exception {
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

  @Test
  public void positiveExtendsTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveExtends.gf");
    setGoldenFileStream(gfStream);
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

  @Test
  public void negativeDuplicateExtendsFatalTranslationErrorTest() throws Exception {
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

  @Test
  public void negativeDuplicateExtendsFatalTranslationError2Test()
      throws Exception {
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

  @Test
  public void positiveImportTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveImport.gf");
    setGoldenFileStream(gfStream);
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

  @Test
  public void implicitImportLangTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/implicitImportLang.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "implicitImportLang");
    invoke();
  }

  /*
   * @testName: implicitImportJspTest
   * 
   * @assertion_ids: JSP:SPEC:35
   * 
   * @test_Strategy: Validate that classes from the jakarta.servlet.jsp package
   * are implicitly imported by calling JspFactory.getDefaultFactory() method.
   */

  @Test
  public void implicitImportJspTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/implicitImportJsp.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "implicitImportJsp");
    invoke();
  }

  /*
   * @testName: implicitImportServletTest
   * 
   * @assertion_ids: JSP:SPEC:35
   * 
   * @test_Strategy: Validate that classes from the jakarta.servlet package are
   * implicitly imported by creating and using an instance of RequestDispatcher.
   */

  @Test
  public void implicitImportServletTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/implicitImportServlet.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "implicitImportServlet");
    invoke();
  }

  /*
   * @testName: implicitImportHttpTest
   * 
   * @assertion_ids: JSP:SPEC:35
   * 
   * @test_Strategy: Validate that classes from the jakarta.servlet.http package
   * are implicitly imported by creating and using an instance of Cookie.
   */

  @Test
  public void implicitImportHttpTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/implicitImportHttp.gf");
    setGoldenFileStream(gfStream);
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

  @Test
  public void positiveMultipleImportTest() throws Exception {
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

  @Test
  public void negativeMultiplePageEncodingTest() throws Exception {
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

  @Test
  public void positiveInfoTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveInfo.gf");
    setGoldenFileStream(gfStream);
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

  @Test
  public void negativeDuplicateInfoFatalTranslationErrorTest() throws Exception {
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

  @Test
  public void negativeDuplicateInfoFatalTranslationError2Test() throws Exception {
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

  @Test
  public void positiveLangTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveLang.gf");
    setGoldenFileStream(gfStream);
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

  @Test
  public void negativeDuplicateLanguageFatalTranslationErrorTest()
      throws Exception {
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

  @Test
  public void negativeDuplicateLanguageFatalTranslationError2Test()
      throws Exception {
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

  @Test
  public void positiveSessionTest() throws Exception {
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

  @Test
  public void positiveSessionDefaultTest() throws Exception {
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

  @Test
  public void negativeSessionFatalTranslationErrorTest() throws Exception {
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

  @Test
  public void negativeDuplicateSessionFatalTranslationErrorTest() throws Exception {
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

  @Test
  public void negativeDuplicateSessionFatalTranslationError2Test()
      throws Exception {
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

  @Test
  public void positiveDuplicateBufferTest() throws Exception {
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

  @Test
  public void positiveDuplicateAutoFlushTest() throws Exception {
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

  @Test
  public void positiveDuplicateIsThreadSafeTest() throws Exception {
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

  @Test
  public void positiveDuplicateIsErrorPageTest() throws Exception {
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

  @Test
  public void positiveDuplicateIsELIgnoredTest() throws Exception {
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

  @Test
  public void positiveDuplicateContentTest() throws Exception {
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

  @Test
  public void positiveDuplicateErrorPageTest() throws Exception {
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

  @Test
  public void positiveDuplicateExtendsTest() throws Exception {
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

  @Test
  public void positiveDuplicateInfoTest() throws Exception {
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

  @Test
  public void positiveDuplicateLanguageTest() throws Exception {
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

  @Test
  public void positiveDuplicateSessionTest() throws Exception {
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

  @Test
  public void negativeBufferSuffixTest() throws Exception {
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
   * method, with a name of jakarta.servlet.jsp.jspException (for
   * backwards-compatibility) and also jakarta.servlet.error.exception (for
   * compatibility with the servlet specification).
   */

  @Test
  public void errorPageExceptionAttributeTest() throws Exception {
    String testName = "errorPageExceptionAttributeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test PASSED|jakarta.servlet.error.exception and jakarta.servlet.jsp.jspException are the same");
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
  @Test
  public void negativeImportUtilTest() throws Exception {
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
  @Test
  public void negativeImportIoTest() throws Exception {
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
  @Test
  public void isELIgnoredTrueTemplateTextTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/IsELIgnoredTrueTemplateTextTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void isELIgnoredFalseTemplateTextDollarTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/IsELIgnoredFalseTemplateTextDollarTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void isELIgnoredFalseTemplateTextPoundTest() throws Exception {
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
  @Test
  public void isELIgnoredFalseActionTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/IsELIgnoredFalseActionTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void isELIgnoredTrueActionTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/IsELIgnoredTrueActionTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void deferredSyntaxAllowedAsLiteralFalseTemplateTextTest()
      throws Exception {
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
  @Test
  public void deferredSyntaxAllowedAsLiteralTrueTemplateTextTest()
      throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/DeferredSyntaxAllowedAsLiteralTrueTemplateTextTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void deferredSyntaxAllowedAsLiteralFalseActionTest() throws Exception {
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
  @Test
  public void deferredSyntaxAllowedAsLiteralTrueActionTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/DeferredSyntaxAllowedAsLiteralTrueActionTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "DeferredSyntaxAllowedAsLiteralTrueActionTest");
    invoke();
  }
  
  /*
   * @testName: errorOnELNotFoundFalseTest
   * 
   * @assertion_ids: JSP:SPEC:319
   * 
   * @test_Strategy: [ErrorOnELNotFoundPageDirective] Verify that when the
   * ErrorOnELNotFound page directive attribute is set to false, a reference
   * to an unresolved identifier results in the empty string being used.
   */
  @Test
  public void errorOnELNotFoundFalseTest()
      throws Exception {
    String testName = "ErrorOnELNotFoundFalseTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    invoke();
  }
  
  /*
   * @testName: errorOnELNotFoundTrueTest
   * 
   * @assertion_ids: JSP:SPEC:319
   * 
   * @test_Strategy: [ErrorOnELNotFoundPageDirective] Verify that when the
   * ErrorOnELNotFound page directive attribute is set to false, a reference
   * to an unresolved identifier results in the empty string being used.
   */
  @Test
  public void errorOnELNotFoundTrueTest()
      throws Exception {
    String testName = "ErrorOnELNotFoundTrueTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_directive_page_web/" + testName + ".jsp HTTP/1.0");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
