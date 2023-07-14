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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.dobody;


import java.io.IOException;
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

  private static final String CONTEXT_ROOT = "/jsp_core_act_dobody_web";

  public URLClientIT() throws Exception {
    setup();

    setGeneralURI("/jsp/spec/core_syntax/action/dobody");
    setContextRoot(CONTEXT_ROOT);

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_core_act_dobody_web.war");
    archive.addClasses(com.sun.ts.tests.jsp.common.util.JspTestUtil.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_core_act_dobody_web.xml"));

    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DoBodyVarVarReaderTag.tag", "tags/DoBodyVarVarReaderTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DoBodyVarTag.tag", "tags/DoBodyVarTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DoBodyVarReaderTag.tag", "tags/DoBodyVarReaderTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DoBodyUsageContextTagB.tagx", "tags/DoBodyUsageContextTagB.tagx");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DoBodyUsageContextTagA.tag", "tags/DoBodyUsageContextTagA.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DoBodyScopeTag.tag", "tags/DoBodyScopeTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DoBodyNoVarVarReaderScopeTag.tag", "tags/DoBodyNoVarVarReaderScopeTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DoBodyNonEmptyBodyTag2.tag", "tags/DoBodyNonEmptyBodyTag2.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DoBodyNonEmptyBodyTag1.tag", "tags/DoBodyNonEmptyBodyTag1.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DoBodyInvalidSessionScopeTag.tag", "tags/DoBodyNonEmptyBodyTag1.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DoBodyInvalidScopeTag.tag", "tags/DoBodyInvalidScopeTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DoBodyInvalidRequestScopeTag.tag", "tags/DoBodyInvalidRequestScopeTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DoBodyInvalidPageScopeTag.tag", "tags/DoBodyInvalidPageScopeTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DoBodyInvalidApplicationScopeTag.tag", "tags/DoBodyInvalidApplicationScopeTag.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/DoBodyAttributeTag.tag", "tags/DoBodyAttributeTag.tag");

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspDoBodyVarVarReaderTest.jsp")), "JspDoBodyVarVarReaderTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspDoBodyVarTest.jsp")), "JspDoBodyVarTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspDoBodyVarReaderTest.jsp")), "JspDoBodyVarReaderTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspDoBodyUsageContextTest3.jsp")), "JspDoBodyUsageContextTest3.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspDoBodyUsageContextTest2.jspx")), "JspDoBodyUsageContextTest2.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspDoBodyUsageContextTest1.jsp")), "JspDoBodyUsageContextTest1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspDoBodyScopeTest.jsp")), "JspDoBodyScopeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspDoBodyNoVarVarReaderScopeTest.jsp")), "JspDoBodyNoVarVarReaderScopeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspDoBodyNonEmptyBodyTest2.jsp")), "JspDoBodyNonEmptyBodyTest2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspDoBodyNonEmptyBodyTest1.jsp")), "JspDoBodyNonEmptyBodyTest1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspDoBodyJspAttributeTest.jsp")), "JspDoBodyJspAttributeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspDoBodyInvalidSessionScopeTest.jsp")), "JspDoBodyInvalidSessionScopeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspDoBodyInvalidScopeTest.jsp")), "JspDoBodyInvalidScopeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspDoBodyInvalidRequestScopeTest.jsp")), "JspDoBodyInvalidRequestScopeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspDoBodyInvalidPageScopeTest.jsp")), "JspDoBodyInvalidPageScopeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspDoBodyInvalidApplicationScopeTest.jsp")), "JspDoBodyInvalidApplicationScopeTest.jsp");
    
    return archive;

  }

  
  /*
   * @testName: jspDoBodyNonEmptyBodyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void jspDoBodyNonEmptyBodyTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT + "/"
        + "JspDoBodyNonEmptyBodyTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT + "/"
        + "JspDoBodyNonEmptyBodyTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspDoBodyInvalidPageScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void jspDoBodyInvalidPageScopeTest() throws Exception {
    String testName = "JspDoBodyInvalidPageScopeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspDoBodyInvalidRequestScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void jspDoBodyInvalidRequestScopeTest() throws Exception {
    String testName = "JspDoBodyInvalidRequestScopeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspDoBodyInvalidApplicationScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void jspDoBodyInvalidApplicationScopeTest() throws Exception {
    String testName = "JspDoBodyInvalidApplicationScopeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
  /*
   * @testName: jspDoBodyInvalidSessionScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void jspDoBodyInvalidSessionScopeTest() throws Exception {
    String testName = "JspDoBodyInvalidSessionScopeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
  /*
   * @testName: jspDoBodyInvalidScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void jspDoBodyInvalidScopeTest() throws Exception {
    String testName = "JspDoBodyInvalidScopeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspDoBodyJspAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void jspDoBodyJspAttributeTest() throws Exception {
    String testName = "JspDoBodyJspAttributeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspDoBodyVarTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void jspDoBodyVarTest() throws Exception {
    String testName = "JspDoBodyVarTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspDoBodyVarReaderTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void jspDoBodyVarReaderTest() throws Exception {
    String testName = "JspDoBodyVarReaderTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspDoBodyScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void jspDoBodyScopeTest() throws Exception {
    String testName = "JspDoBodyScopeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: jspDoBodyNoVarVarReaderScopeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void jspDoBodyNoVarVarReaderScopeTest() throws Exception {
    String testName = "JspDoBodyNoVarVarReaderScopeTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspDoBodyVarVarReaderTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void jspDoBodyVarVarReaderTest() throws Exception {
    String testName = "JspDoBodyVarVarReaderTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeJspDoBodyUsageContextTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void negativeJspDoBodyUsageContextTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT + "/"
        + "JspDoBodyUsageContextTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT + "/"
        + "JspDoBodyUsageContextTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveJspDoBodyUsageContextTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy:
   */

  @Test
  public void positiveJspDoBodyUsageContextTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT + "/"
        + "JspDoBodyUsageContextTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
