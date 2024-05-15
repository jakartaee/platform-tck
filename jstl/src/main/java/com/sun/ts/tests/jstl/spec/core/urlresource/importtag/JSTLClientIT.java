/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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



package com.sun.ts.tests.jstl.spec.core.urlresource.importtag;

import java.io.IOException;
import java.io.InputStream;

import com.sun.ts.tests.jstl.common.client.AbstractUrlClient;

import jakarta.servlet.ServletException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

@ExtendWith(ArquillianExtension.class)
public class JSTLClientIT extends AbstractUrlClient {

  public static String packagePath = JSTLClientIT.class.getPackageName().replace(".", "/");

  /** Creates new JSTLClient */
  public JSTLClientIT() {
    setContextRoot("/jstl_core_url_import_web");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_core_url_import_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_core_url_import_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeImportAbsResourceNon2xxTest.jsp")), "negativeImportAbsResourceNon2xxTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeImportCtxInvalidValueTest.jsp")), "negativeImportCtxInvalidValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeImportCtxUrlInvalidValueTest.jsp")), "negativeImportCtxUrlInvalidValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeImportRequestDispatcherNon2xxTest.jsp")), "negativeImportRequestDispatcherNon2xxTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeImportRequestDispatcherRTIOExceptionTest.jsp")), "negativeImportRequestDispatcherRTIOExceptionTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeImportRequestDispatcherServletExceptionTest.jsp")), "negativeImportRequestDispatcherServletExceptionTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeImportUrlEmptyTest.jsp")), "negativeImportUrlEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeImportUrlInvalidTest.jsp")), "negativeImportUrlInvalidTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeImportUrlNullTest.jsp")), "negativeImportUrlNullTest.jsp");

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImportAbsUrlEnvPropTest.jsp")), "positiveImportAbsUrlEnvPropTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImportAbsUrlTest.jsp")), "positiveImportAbsUrlTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImportBodyParamTest.jsp")), "positiveImportBodyParamTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImportCharEncodingNullEmptyTest.jsp")), "positiveImportCharEncodingNullEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImportCharEncodingTest.jsp")), "positiveImportCharEncodingTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImportCtxRelUrlTest.jsp")), "positiveImportCtxRelUrlTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImportEncodingNotSpecifiedTest.jsp")), "positiveImportEncodingNotSpecifiedTest.jsp");
    
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImportFollowRedirectTest.jsp")), "positiveImportFollowRedirectTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImportPageRelUrlTest.jsp")), "positiveImportPageRelUrlTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImportRelUrlEnvPropTest.jsp")), "positiveImportRelUrlEnvPropTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImportScopeTest.jsp")), "positiveImportScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImportUrlTest.jsp")), "positiveImportUrlTest.jsp");

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImportVarReaderTest.jsp")), "positiveImportVarReaderTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImportVarTest.jsp")), "positiveImportVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/ResponseInternalServerError.jsp")), "ResponseInternalServerError.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/ResponseScCreated.jsp")), "ResponseScCreated.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/ResponseScGone.jsp")), "ResponseScGone.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/ResponseSendRedirect.jsp")), "ResponseSendRedirect.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/RuntimeException.jsp")), "RuntimeException.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/ServletException.jsp")), "ServletException.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/ServletExceptionRootCause.jsp")), "ServletExceptionRootCause.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/encoding/Encoding.jsp")), "encoding/Encoding.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/env.jsp")), "env.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/import-encoded.txt")), "import-encoded.txt");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/import.jsp")), "import.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/import.txt")), "import.txt");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/IOException.jsp")), "IOException.jsp");


    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveImportUrlTest
   * 
   * @assertion_ids: JSTL:SPEC:23; JSTL:SPEC:23.1; JSTL:SPEC:23.1.4
   * 
   * @testStrategy: Validate that the 'url' attribute accepts both dynamic and
   * static values.
   */
  @Test
  public void positiveImportUrlTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveImportUrlTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void positiveImportAbsUrlTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveImportAbsUrlTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void positiveImportCtxRelUrlTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveImportCtxRelUrlTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void positiveImportPageRelUrlTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveImportPageRelUrlTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void positiveImportVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveImportVarTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void positiveImportScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveImportScopeTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void positiveImportRelUrlEnvPropTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveImportRelUrlEnvPropTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveImportRelUrlEnvPropTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveImportRelUrlEnvPropTest.jsp?reqpar=parm");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveImportRelUrlEnvPropTest.gf");
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
  @Test
  public void positiveImportAbsUrlEnvPropTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveImportAbsUrlEnvPropTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveImportAbsUrlEnvPropTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveImportAbsUrlEnvPropTest.jsp?reqpar=parm");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveImportAbsUrlEnvPropTest.gf");
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
  @Test
  public void positiveImportCharEncodingTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveImportCharEncodingTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void positiveImportCharEncodingNullEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveImportCharEncodingNullEmptyTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void positiveImportBodyParamTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveImportBodyParamTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void positiveImportVarReaderTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveImportVarReaderTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void positiveImportFollowRedirectTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveImportFollowRedirectTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void negativeImportCtxInvalidValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeImportCtxInvalidValueTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void negativeImportCtxUrlInvalidValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeImportCtxUrlInvalidValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeImportCtxUrlInvalidValueTest");
    invoke();
  }

  /*
   * @testName: negativeImportUrlNullTest
   * 
   * @assertion_ids: JSTL:SPEC:23.15
   * 
   * @testStrategy: Validate that if the value of url is null, a
   * jakarta.servlet.jsp.JspException is thrown.
   */
  @Test
  public void negativeImportUrlNullTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeImportUrlNullTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeImportUrlNullTest");
    invoke();
  }

  /*
   * @testName: negativeImportUrlEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:23.15
   * 
   * @testStrategy: Validate that if the value of url is empty ("") a
   * jakarta.servlet.jsp.JspException is thrown.
   */
  @Test
  public void negativeImportUrlEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeImportUrlEmptyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeImportUrlEmptyTest");
    invoke();
  }

  /*
   * @testName: negativeImportUrlInvalidTest
   * 
   * @assertion_ids: JSTL:SPEC:23.15
   * 
   * @testStrategy: Validate that if url is provided an invalid URL an instance
   * of jakarta.servlet.jsp.JspException is thrown.
   */
  @Test
  public void negativeImportUrlInvalidTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeImportUrlInvalidTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void negativeImportAbsResourceNon2xxTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeImportAbsResourceNon2xxTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void negativeImportRequestDispatcherNon2xxTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeImportRequestDispatcherNon2xxTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void negativeImportRequestDispatcherRTIOExceptionTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeImportRequestDispatcherRTIOExceptionTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void negativeImportRequestDispatcherServletExceptionTest()
      throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeImportRequestDispatcherServletExceptionTest.gf");
    setGoldenFileStream(gfStream);
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
  @Test
  public void positiveImportEncodingNotSpecifiedTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveImportEncodingNotSpecifiedTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveImportEncodingNotSpecifiedTest");
    invoke();
  }

}
