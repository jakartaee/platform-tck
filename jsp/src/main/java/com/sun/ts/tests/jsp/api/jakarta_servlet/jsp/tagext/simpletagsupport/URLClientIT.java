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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.simpletagsupport;


import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for the default behavior of SimpleTagSupport.
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
    setContextRoot("/jsp_tagadapter_web");
    setTestJsp("TagAdapterTest");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_simtagsupport_web.war");
    archive.addClasses(CheckInstanceSimpleTag.class,
            ClassicJspFragmentGetJspContext.class,
            ClassicParent.class,
            ClassicSkipPage.class,
            DefaultSimpleTag.class,
            FailingTag.class,
            SimpleAncestor.class,
            SimpleEmptyBody.class,
            SimpleGetSetJspBody.class,
            SimpleGetSetJspContext.class,
            SimpleGetSetParent.class,
            SimpleJspFragmentGetJspContext.class,
            SimpleNoParent.class,
            SimpleParentTag.class,
            SimpleSkipPage.class,
            SimpleSyncTag.class,
            SyncTEI.class,
            com.sun.ts.tests.jsp.common.util.JspTestUtil.class,
            com.sun.ts.tests.jsp.common.tags.tck.SetTag.class,
            com.sun.ts.tests.common.el.api.expression.ExpressionTest.class);

    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_simtagsupport_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/simpletagsupport/ClassicSkipPageTag.tag", "tags/simpletagsupport/ClassicSkipPageTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/simpletagsupport/SimpleSkipPageTag.tag", "tags/simpletagsupport/SimpleSkipPageTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/simpletagsupport/Sync.tag", "tags/simpletagsupport/Sync.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/simpletagsupport.tld", "simpletagsupport.tld");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/emptySetJspBodyTest.jsp")), "emptySetJspBodyTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/jspFragmentGetJspContextTest.jsp")), "jspFragmentGetJspContextTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/noParentTest.jsp")), "noParentTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/SimpleTagSupportDoTagDefault.jsp")), "SimpleTagSupportDoTagDefault.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/SimpleTagSupportFindAncestorTest.jsp")), "SimpleTagSupportFindAncestorTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/SimpleTagSupportJspBodyTest.jsp")), "SimpleTagSupportJspBodyTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/SimpleTagSupportJspContextTest.jsp")), "SimpleTagSupportJspContextTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/SimpleTagSupportParentTest.jsp")), "SimpleTagSupportParentTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/SimpleTagSupportSkipPageClassicTest.jsp")), "SimpleTagSupportSkipPageClassicTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/SimpleTagSupportSkipPageSimpleTest.jsp")), "SimpleTagSupportSkipPageSimpleTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/SimpleTagSupportVariableSynchronizationTest.jsp")), "SimpleTagSupportVariableSynchronizationTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/tagHandlerCacheTest.jsp")), "tagHandlerCacheTest.jsp");

    return archive;
  }

  
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
  @Test
  public void simpleTagSupportDoTagDefaultTest() throws Exception {
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
  @Test
  public void simpleTagSupportSkipPageExceptionTest() throws Exception {
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
  @Test
  public void simpleTagSupportGetSetJspContextTest() throws Exception {
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
  @Test
  public void simpleTagSupportGetSetJspBodyTest() throws Exception {
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
  @Test
  public void simpleTagSupportGetSetParentTest() throws Exception {
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
  @Test
  public void simpleTagSupportFindAncestorTest() throws Exception {
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
  @Test
  public void simpleTagSupportVariableSynchronizationTest() throws Exception {
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
  @Test
  public void tagHandlerCacheTest() throws Exception {
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
  @Test
  public void emptySetJspBodyTest() throws Exception {
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
  @Test
  public void noParentTest() throws Exception {
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
  @Test
  public void jspFragmentGetJspContextTest() throws Exception {
    String testName = "jspFragmentGetJspContextTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_simtagsupport_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test PASSED in simple tag.|Test PASSED in classic tag.");
    invoke();
  }
}
