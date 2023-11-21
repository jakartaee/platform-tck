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

/*
 * @(#)URLClient.java	1.2 10/09/02
 */

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.tagsupport;


import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import com.sun.ts.tests.jsp.common.util.MethodValidatorBean;

/**
 * Test client for Container interaction with objects implementing Tag..
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

    setContextRoot("/jsp_tagsupport_web");
    setTestJsp("TagSupportApiTest");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagsupport_web.war");
    archive.addClasses(ContainerInteractionTag.class,
            FindAncestorTag.class, InitializationTag.class,
            ParentTag.class, SynchronizationTag.class,
            JspTestUtil.class,
            MethodValidatorBean.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagsupport_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tagsupport.tld", "tagsupport.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/DoAfterBodyEvalBodyAgainTest.jsp")), "DoAfterBodyEvalBodyAgainTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/DoAfterBodySkipBodyTest.jsp")), "DoAfterBodySkipBodyTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/DoEndTagEvalPageTest.jsp")), "DoEndTagEvalPageTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/DoEndTagSkipPageTest.jsp")), "DoEndTagSkipPageTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/DoStartEvalBodyIncludeTest.jsp")), "DoStartEvalBodyIncludeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/DoStartSkipBodyTest.jsp")), "DoStartSkipBodyTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/FindAncestorTest.jsp")), "FindAncestorTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/MethodValidation.jsp")), "MethodValidation.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TagInitializationTest.jsp")), "TagInitializationTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TagSupportApiTest.jsp")), "TagSupportApiTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TagSupportSynchronizationTest.jsp")), "TagSupportSynchronizationTest.jsp");

    return archive;
  }

  
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
  @Test
  public void tagSupportTagInitializationTest() throws Exception {
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
  @Test
  public void tagSupportDoStartEvalBodyIncludeTest() throws Exception {
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
  @Test
  public void tagSupportDoStartSkipBodyTest() throws Exception {
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
  @Test
  public void tagSupportDoAfterBodySkipBodyTest() throws Exception {
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
  @Test
  public void tagSupportDoAfterBodyEvalBodyAgainTest() throws Exception {
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
  @Test
  public void tagSupportDoEndTagSkipPageTest() throws Exception {
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
  @Test
  public void tagSupportDoEndTagEvalPageTest() throws Exception {
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
  @Test
  public void tagSupportFindAncestorWithClassTest() throws Exception {
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
  @Test
  public void tagSupportDoStartTagDefaultValueTest() throws Exception {
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
  @Test
  public void tagSupportDoEndTagDefaultValueTest() throws Exception {
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
  @Test
  public void tagSupportDoAfterBodyDefaultValueTest() throws Exception {
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
  @Test
  public void tagSupportGetSetValueTest() throws Exception {
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
  @Test
  public void tagSupportGetValuesTest() throws Exception {
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
  @Test
  public void tagSupportRemoveValueTest() throws Exception {
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
  @Test
  public void tagSupportVariableSynchronizationTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagsupport_web/TagSupportSynchronizationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }
}
