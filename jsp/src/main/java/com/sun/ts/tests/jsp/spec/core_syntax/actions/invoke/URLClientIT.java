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
 * @(#)URLClient.java	1.1 12/09/02
 */

package com.sun.ts.tests.jsp.spec.core_syntax.actions.invoke;


import java.io.IOException;
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


  public URLClientIT() throws Exception {
    setup();

    setContextRoot("/jsp_core_act_invoke_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_core_act_invoke_web.war");
    archive.addClasses(com.sun.ts.tests.jsp.common.util.JspTestUtil.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_core_act_invoke_web.xml"));

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeVarVarReaderTest.jsp")), "JspInvokeVarVarReaderTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeVarTest.jsp")), "JspInvokeVarTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeVarReaderTest.jsp")), "JspInvokeVarReaderTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeUsageContextTest3.jsp")), "JspInvokeUsageContextTest3.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeUsageContextTest2.jspx")), "JspInvokeUsageContextTest2.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeUsageContextTest1.jsp")), "JspInvokeUsageContextTest1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeScopeTest.jsp")), "JspInvokeScopeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeNoVarVarReaderScopeTest.jsp")), "JspInvokeNoVarVarReaderScopeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeNotInSessionTest.jsp")), "JspInvokeNotInSessionTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeNonEmptyBodyTest.jsp")), "JspInvokeNonEmptyBodyTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeJspAttributeTest.jsp")), "JspInvokeJspAttributeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeInvalidSessionScopeTest.jsp")), "JspInvokeInvalidSessionScopeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeInvalidScopeTest.jsp")), "JspInvokeInvalidScopeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeInvalidRequestScopeTest.jsp")), "JspInvokeInvalidRequestScopeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeInvalidPageScopeTest.jsp")), "JspInvokeInvalidPageScopeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeInvalidApplicationScopeTest.jsp")), "JspInvokeInvalidApplicationScopeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspInvokeFragmentReqAttributeTest.jsp")), "JspInvokeFragmentReqAttributeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/jspFragmentNullTest.jsp")), "jspFragmentNullTest.jsp");

    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/VarVarReaderTag.tag", "tags/VarVarReaderTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/ScopeTag.tag", "tags/ScopeTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/NoVarVarReaderScopeTag.tag", "tags/NoVarVarReaderScopeTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/NotInSessionTag.tag", "tags/NotInSessionTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/NonEmptyBodyTag.tag", "tags/NonEmptyBodyTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspInvokeVarTag.tag", "tags/JspInvokeVarTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspInvokeVarReaderTag.tag", "tags/JspInvokeVarReaderTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspInvokeUsageContextTag3b.tagx", "tags/JspInvokeUsageContextTag3b.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspInvokeUsageContextTag3a.tag", "tags/JspInvokeUsageContextTag3a.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/jspFragmentNullTest.tag", "tags/jspFragmentNullTest.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspAttributeTag.tag", "tags/JspAttributeTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/InvalidSessionScopeTag.tag", "tags/InvalidSessionScopeTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/InvalidScopeTag.tag", "tags/InvalidScopeTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/InvalidRequestScopeTag.tag", "tags/InvalidRequestScopeTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/InvalidPageScopeTag.tag", "tags/InvalidPageScopeTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/InvalidApplicationScopeTag.tag", "tags/InvalidApplicationScopeTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/FragmentReqAttributeTag.tag", "tags/FragmentReqAttributeTag.tag");
    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspInvokeUsageContextTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the following usage contexts of jsp:invoke: -
   * jsp:invoke present in a JSP or JSP document is a translation error. -
   * jsp:invoke is valid within a tag file.
   */
  @Test
  public void jspInvokeUsageContextTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeUsageContextTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeUsageContextTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeUsageContextTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: jspInvokeVarTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the behavior of jsp:invoke when the var attribute
   * is specified. The tag file should export the result of the invocation to a
   * request-scoped variable. The type and value of the exported variable will
   * be validated by the invoking page.
   */
  @Test
  public void jspInvokeVarTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeVarTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspInvokeVarReaderTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the behavior of jsp:invoke when the varReader
   * attribute is specified. The tag file should export the result of the
   * invocation to a request-scoped variable. The type and value of the exported
   * variable will be validated by the invoking page as well as verification
   * that the exported reader is resettable.
   */
  @Test
  public void jspInvokeVarReaderTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeVarReaderTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspInvokeScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the behavior of jsp:invoke when the scope
   * attribute is and is not specified. If not specified, the result of the
   * invocation should be in the page scope of the tag file. If the scope is
   * 'page' the result of the invocation should be in the page scope of the tag
   * file. If scope is specified as 'request', 'session', or 'application', the
   * result of the invocation should be in the page context of the invoking
   * page.
   */
  @Test
  public void jspInvokeScopeTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test PASSED|Test PASSED|Test PASSED|Test PASSED|Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspInvokeVarVarReaderTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if both var and varReader are specified
   * within one particular jsp:invoke action, a translation- time error is
   * raised.
   */
  @Test
  public void jspInvokeVarVarReaderTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeVarVarReaderTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspInvokeNoVarVarReaderTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if the scope attribute of jsp:invoke is
   * specified but neither the var nor varReader are specified, a
   * translation-time error is raised.
   */
  @Test
  public void jspInvokeNoVarVarReaderTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeNoVarVarReaderScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspInvokeNotInSessionTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that jsp:invoke will cause an
   * IllegalStateException to be raised if the jsp:invoke action tries to export
   * a result into the session scope where the calling page does not participate
   * in a session.
   */
  @Test
  public void jspInvokeNotInSessionTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeNotInSessionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspInvokeJspAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the attributes of jspInvoke can all be specified
   * using the jsp:attribute action.
   */
  @Test
  public void jspInvokeJspAttributeTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeJspAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspInvokeInvalidScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that a translation-time error is generated if the
   * scope attribute of jsp:invoke is provided an invalid value (i.e. not
   * 'page', 'request', 'session', or 'application').
   */
  @Test
  public void jspInvokeInvalidScopeTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeInvalidScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeInvalidPageScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeInvalidRequestScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeInvalidSessionScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeInvalidApplicationScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspInvokeFragmentReqAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the 'fragment' attribute of jsp:invoke is
   * indeed required by the container. Validate by calling jsp:invoke without
   * the attribute and look for a translation- time error.
   */
  @Test
  public void jspInvokeFragmentReqAttributeTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeFragmentReqAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspInvokeNonEmptyBodyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate a translation-time error is raised if the
   * jsp:invoke action has a non-empty body.
   */
  @Test
  public void jspInvokeNonEmptyBodyTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/JspInvokeNonEmptyBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspFragmentNullTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: If the fragment identified by the given name is null, will
   * behave as though a fragment was passed in that produces no output.
   */
  @Test
  public void jspFragmentNullTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_invoke_web/jspFragmentNullTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "BEGINEND");
    invoke();
  }
}
