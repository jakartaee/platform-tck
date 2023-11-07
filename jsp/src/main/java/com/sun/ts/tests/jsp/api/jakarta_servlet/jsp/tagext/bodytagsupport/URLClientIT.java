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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.bodytagsupport;


import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for BodyTagSupport.
 */
import java.io.IOException;
import java.io.InputStream;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {




  public URLClientIT() throws Exception {
    setup();
    setContextRoot("/jsp_bodytagsupp_web");
    setTestJsp("BodyTagSupportApiTest");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_bodytagsupp_web.war");
    archive.addClasses(BodyContainerInteractionTag.class,
            BodySynchronizationTag.class, GetBodyContentTestTag.class,
            GetPreviousOutTestTag.class, SyncTEI.class,
            com.sun.ts.tests.jsp.common.util.JspTestUtil.class,
            com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.tagsupport.ContainerInteractionTag.class,
            com.sun.ts.tests.jsp.common.util.MethodValidatorBean.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_bodytagsupp_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/bodytagsupport.tld", "bodytagsupport.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/BodyTagEmptyTagTest.jsp")), "BodyTagEmptyTagTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/BodyTagEvalBodyBufferedTest.jsp")), "BodyTagEvalBodyBufferedTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/BodyTagEvalBodyIncludeTest.jsp")), "BodyTagEvalBodyIncludeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/BodyTagSkipBodyTest.jsp")), "BodyTagSkipBodyTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/BodyTagSupportApiTest.jsp")), "BodyTagSupportApiTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/BodyTagSupportSynchronizationTest.jsp")), "BodyTagSupportSynchronizationTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetBodyContentTest.jsp")), "GetBodyContentTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetPreviousOutTest.jsp")), "GetPreviousOutTest.jsp");

    return archive;

  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: bodyTagSupportCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:318
   * 
   * @test_Strategy: Validate the constructor of BodyTagSupport
   */
  @Test
  public void bodyTagSupportCtorTest() throws Exception {
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
  @Test
  public void bodyTagSupportDoStartTagTest() throws Exception {
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
  @Test
  public void bodyTagSupportDoEndTagTest() throws Exception {
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
  @Test
  public void bodyTagSupportDoAfterBodyTest() throws Exception {
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
  @Test
  public void bodyTagSupportGetBodyContentTest() throws Exception {
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
  @Test
  public void bodyTagSupportGetPreviousOutTest() throws Exception {
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
  @Test
  public void bodyTagSupportEvalBodyBufferedTest() throws Exception {
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
  @Test
  public void bodyTagSupportEvalBodyIncludeTest() throws Exception {
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
  @Test
  public void bodyTagSupportSkipBodyTest() throws Exception {
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
  @Test
  public void bodyTagSupportEmptyTagTest() throws Exception {
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
  @Test
  public void bodyTagSupportVariableSynchronizationTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodytagsupp_web/BodyTagSupportSynchronizationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }
}
