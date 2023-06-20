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

/*
 * $Id$
 */

/*
 * @(#)URLClient.java	1.1 12/09/02
 */

package com.sun.ts.tests.jsp.spec.core_syntax.actions.root;

import java.io.PrintWriter;

import com.sun.javatest.Status;
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

@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {
  public URLClientIT() throws Exception {
    setup();

    setContextRoot("/jsp_core_act_root_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_core_act_root_web.war");
    archive.addClasses(com.sun.ts.tests.jsp.common.util.JspTestUtil.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_core_act_root_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspRootJspBodyTag.tagx", "tags/JspRootJspBodyTag.tagx");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspRootUsageContextTag1.tagx", "tags/JspRootUsageContextTag1.tagx");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspRootUsageContextTag2.tag", "tags/JspRootUsageContextTag2.tag");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspRootVersionInvalidValueTag.tagx", "tags/JspRootVersionInvalidValueTag.tagx");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspRootVersionReqAttrTag.tagx", "tags/JspRootVersionReqAttrTag.tagx");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspRootVersionReqAttrTest2.jsp")), "JspRootVersionReqAttrTest2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspRootVersionReqAttrTest1.jspx")), "JspRootVersionReqAttrTest1.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspRootVersionAttrInvalidValueTest2.jsp")), "JspRootVersionAttrInvalidValueTest2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspRootVersionAttrInvalidValueTest1.jspx")), "JspRootVersionAttrInvalidValueTest1.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspRootVersionAttrAllowableValuesTest2.jspx")), "JspRootVersionAttrAllowableValuesTest2.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspRootVersionAttrAllowableValuesTest1.jspx")), "JspRootVersionAttrAllowableValuesTest1.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspRootUsageContextTest4.jsp")), "JspRootUsageContextTest4.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspRootUsageContextTest3.jsp")), "JspRootUsageContextTest3.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspRootUsageContextTest2.jspx")), "JspRootUsageContextTest2.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspRootUsageContextTest1.jsp")), "JspRootUsageContextTest1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspRootJspBodyTest2.jsp")), "JspRootJspBodyTest2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspRootJspBodyTest1.jspx")), "JspRootJspBodyTest1.jspx");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspRootUsageContextTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that a translation time error is raised when
   * jsp:root is used in JSP or Tag files in standard syntax but not in JSP
   * Documents, or Tag files in XML syntax.
   */
  @Test
  public void jspRootUsageContextTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootUsageContextTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootUsageContextTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<status>Test PASSED</status>");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "jsp:root");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootUsageContextTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<status>Test PASSED</status>");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "jsp:root");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootUsageContextTest4.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspRootVersionAttrAllowableValuesTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the container accepts both "1.2" and "2.0" as
   * allowable values for the version attribute of the jsp:root element.
   */
  @Test
  public void jspRootVersionAttrAllowableValuesTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootVersionAttrAllowableValuesTest1.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<status>Test PASSED</status>");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "jsp:root");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootVersionAttrAllowableValuesTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<status>Test PASSED</status>");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "jsp:root");
    invoke();
  }

  /*
   * @testName: jspRootVersionAttrInvalidValueTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the container generates a translation-time error
   * when the version attribute of jsp:root is provided a value other than "1.2"
   * or "2.0"
   */
  @Test
  public void jspRootVersionAttrInvalidValueTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootVersionAttrInvalidValueTest1.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootVersionAttrInvalidValueTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspRootVersionReqAttrTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the version attribute of jsp:root is indeed
   * required by looking for a translation error from the container when the
   * attribute is not present.
   */
  @Test
  public void jspRootVersionReqAttrTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootVersionReqAttrTest1.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootVersionReqAttrTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspRootJspBodyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that jsp:body can be used to specify the body of
   * the jsp:root action.
   */
  @Test
  public void jspRootJspBodyTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootJspBodyTest1.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<status>Test PASSED</status>");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "jsp:root");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_root_web/JspRootJspBodyTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<status>Test PASSED</status>");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "jsp:root");
    invoke();
  }
}
