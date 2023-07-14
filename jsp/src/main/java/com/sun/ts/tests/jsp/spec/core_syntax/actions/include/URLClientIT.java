/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.include;


import java.io.IOException;
import java.io.InputStream;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

import java.lang.System.Logger;

@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {

  private static final Logger logger = System.getLogger(URLClientIT.class.getName());

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "STARTING TEST : "+testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "FINISHED TEST : "+testInfo.getDisplayName());
  }

  private static final String CONTEXT_ROOT = "/jsp_coresyntx_act_include_web";

  public static String packagePath = URLClientIT.class.getPackageName().replace(".", "/");

  public URLClientIT() throws Exception {
    setup();

    setGeneralURI("/jsp/spec/core_syntax/actions/include");
    setContextRoot("/jsp_coresyntx_act_include_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_coresyntx_act_include_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_coresyntx_act_include_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/staticStatic_A.jsp")), "staticStatic_A.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/staticDynamic_A.jsp")), "staticDynamic_A.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveRequestAttrPageRelative.jsp")), "positiveRequestAttrPageRelative.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveRequestAttrCtxRelative.jsp")), "positiveRequestAttrCtxRelative.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveIncludePageRelativeHtml.jsp")), "positiveIncludePageRelativeHtml.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveIncludePageRelative2.jsp")), "positiveIncludePageRelative2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveIncludePageRelative.jsp")), "positiveIncludePageRelative.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveIncludeForward.jsp")), "positiveIncludeForward.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveIncludeCtxRelativeHtml.jsp")), "positiveIncludeCtxRelativeHtml.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveIncludeCtxRelative.jsp")), "positiveIncludeCtxRelative.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/includeMappedServletTest.jsp")), "includeMappedServletTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/includeforward.jsp")), "includeforward.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/includecommon.jsp")), "includecommon.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/includecommon.html")), "includecommon.html");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/forwardtarget.html")), "forwardtarget.html");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/dynamicStatic_A.jsp")), "dynamicStatic_A.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/dynamicDynamic_A.jsp")), "dynamicDynamic_A.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/C.jsp")), "C.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/include/static_B.jsp")), "include/static_B.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/include/includeMappedServlet.jsp")), "include/includeMappedServlet.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/include/includeMappedServlet.html")), "include/includeMappedServlet.html");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/include/include2.jsp")), "include/include2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/include/dynamic_B.jsp")), "include/dynamic_B.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/include/C.jsp")), "include/C.jsp");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: positiveIncludeCtxRelativeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Include content, using a context-relative path, from JSP
   * page into the current JSP page.
   * 
   * jsp:include provides for the inclusion of dynamic resources, within the
   * same context, using a context-relative path. JavaServer Pages Specification
   * v1.2, Sec. 4.4
   */

  @Test
  public void positiveIncludeCtxRelativeTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveIncludeCtxRelative.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveIncludeCtxRelative");
    invoke();
  }

  /*
   * @testName: positiveIncludeCtxRelativeHtmlTest
   * 
   * @assertion_ids: JSP:SPEC:164.1
   * 
   * @test_Strategy: Include content, using a context-relative path, from a
   * static HTML page into the current JSP page. PENDING Merge existing tests
   * into one test.
   */

  @Test
  public void positiveIncludeCtxRelativeHtmlTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveIncludeCtxRelativeHtml.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveIncludeCtxRelativeHtml");
    invoke();
  }

  /*
   * @testName: positiveIncludePageRelativeTest
   * 
   * @assertion_ids: JSP:SPEC:164.1
   * 
   * @test_Strategy: Include content, using a page-relative path, from a JSP
   * page into the current JSP page.
   */
  @Test
  public void positiveIncludePageRelativeTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveIncludePageRelative.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveIncludePageRelative");
    invoke();
  }

  /*
   * @testName: positiveRequestAttrCtxRelativeTest
   * 
   * @assertion_ids: JSP:SPEC:164.1;JSP:SPEC:164.9
   * 
   * @test_Strategy: Validate the page attribute of jsp:include can correctly
   * accept request-time attribute values which contain context-relative paths.
   */

  @Test
  public void positiveRequestAttrCtxRelativeTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveRequestAttrCtxRelative.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveRequestAttrCtxRelative");
    invoke();
  }

  /*
   * @testName: positiveRequestAttrPageRelativeTest
   * 
   * @assertion_ids: JSP:SPEC:164.1
   * 
   * @test_Strategy: Validate the page attribute of jsp:include can correctly
   * accept request-time attribute values which contain page-relative paths.
   */

  @Test
  public void positiveRequestAttrPageRelativeTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveRequestAttrPageRelative.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveRequestAttrPageRelative");
    invoke();
  }

  /*
   * @testName: positiveIncludePageRelative2Test
   * 
   * @assertion_ids: JSP:SPEC:164.1
   * 
   * @test_Strategy: Include content, using a page-relative path, from a JSP
   * page in a different dir into the current JSP page, with a jsp-config
   * url-pattern in web.xml
   */
  @Test
  public void positiveIncludePageRelative2Test() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveIncludePageRelative2.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveIncludePageRelative2");
    invoke();
  }

  /*
   * @testName: positiveIncludeForwardTest
   * 
   * @assertion_ids: JSP:SPEC:164.1
   * 
   * @test_Strategy: Include a jsp, which forwards to a html file Only the
   * output from the forwarded target should be sent to client, because the test
   * jsp and included jsp shared the same out (JSP 5.4), and a forwarding clears
   * the response buffer (SVR 8.4).
   * 
   */
  @Test
  public void positiveIncludeForwardTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveIncludeForward.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveIncludeForward");
    invoke();
  }

  /*
   * @testName: includeMappedServletTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: set page attribute to a mapped servlet.
   */

  @Test
  public void includeMappedServletTest() throws Exception {
    String testName = "includeMappedServletTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "shoule not be served");
    invoke();
  }

  /*
   * @testName: staticStaticTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:complex set of inclusions in JSP.5.4
   */

  @Test
  public void staticStaticTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/staticStatic_A.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "In /include/C.jsp");
    invoke();
  }

  /*
   * @testName: dynamicDynamicTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: complex set of inclusions in JSP.5.4
   */

  @Test
  public void dynamicDynamicTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/dynamicDynamic_A.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "In /include/C.jsp");
    invoke();
  }

  /*
   * @testName: dynamicStaticTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: complex set of inclusions in JSP.5.4
   */

  @Test
  public void dynamicStaticTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/dynamicStatic_A.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "In /include/C.jsp");
    invoke();
  }

  /*
   * @testName: staticDynamicTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: complex set of inclusions in JSP.5.4
   */

  @Test
  public void staticDynamicTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/staticDynamic_A.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "In /C.jsp");
    invoke();
  }
}
