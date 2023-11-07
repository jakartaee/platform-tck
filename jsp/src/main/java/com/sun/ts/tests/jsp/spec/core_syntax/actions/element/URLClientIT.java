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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.element;


import java.io.IOException;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

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

    setContextRoot("/jsp_core_act_element_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_core_act_element_web.war");
    archive.addClasses(com.sun.ts.tests.jsp.common.util.JspTestUtil.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_core_act_element_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/NameAttrTag.tag", "tags/NameAttrTag.tag");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspElementTag.tag", "tags/JspElementTag.tag");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspElementJspBodyTag.tag", "tags/JspElementJspBodyTag.tag");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspElementJspAttributeTag.tag", "tags/JspElementJspAttributeTag.tag");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspElementDynAttrTag.tag", "tags/JspElementDynAttrTag.tag");    

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspElementTest3.jsp")), "JspElementTest3.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspElementTest2.jspx")), "JspElementTest2.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspElementTest1.jsp")), "JspElementTest1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspElementNameReqAttributeTest3.jsp")), "JspElementNameReqAttributeTest3.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspElementNameReqAttributeTest2.jspx")), "JspElementNameReqAttributeTest2.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspElementNameReqAttributeTest1.jsp")), "JspElementNameReqAttributeTest1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspElementJspBodyTest3.jsp")), "JspElementJspBodyTest3.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspElementJspBodyTest2.jspx")), "JspElementJspBodyTest2.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspElementJspBodyTest1.jsp")), "JspElementJspBodyTest1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspElementJspAttributeTest3.jsp")), "JspElementJspAttributeTest3.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspElementJspAttributeTest2.jspx")), "JspElementJspAttributeTest2.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspElementJspAttributeTest1.jsp")), "JspElementJspAttributeTest1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspElementDynamicAttributeTest3.jsp")), "JspElementDynamicAttributeTest3.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspElementDynamicAttributeTest2.jspx")), "JspElementDynamicAttributeTest2.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspElementDynamicAttributeTest1.jsp")), "JspElementDynamicAttributeTest1.jsp");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspElementTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the use of jsp:element with a simple body (not
   * using jsp:body).
   */
  @Test
  public void jspElementTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1>|body1|</element1>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1>|body1|</element1>");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "xmlns:jsp|http://java.sun.com/JSP/Page");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1>|body1|</element1>");
    invoke();
  }

  /*
   * @testName: jspElementJspAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the use of jsp:attribute as a child of
   * jsp:element. The attributes specified by jsp:attribute should be translated
   * into the attributes of the new XML element.
   */
  @Test
  public void jspElementJspAttributeTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementJspAttributeTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1 attr1=|value1");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementJspAttributeTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1 attr1=|value1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "http://java.sun.com/JSP/Page");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementJspAttributeTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1 attr1=|value1");
    invoke();
  }

  /*
   * @testName: jspElementJspBodyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the use of jsp:body as a chile of jsp:element. The
   * body of the generated element should be that specified by jsp:body.
   */
  @Test
  public void jspElementJspBodyTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementJspBodyTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1>|body1|</element1>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementJspBodyTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1>|body1|</element1>");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "http://java.sun.com/JSP/Page");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementJspBodyTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<element1>|body1|</element1>");
    invoke();
  }

  /*
   * @testName: jspElementNameReqAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the name attribute of the jsp:element action
   * is indeed required by the container.
   */
  @Test
  public void jspElementNameReqAttributeTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementNameReqAttributeTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementNameReqAttributeTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementNameReqAttributeTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();

  }

  /*
   * @testName: jspElementDynamicAttributeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the name attribute of jsp:element can accept both
   * EL and RT expressions.
   */
  @Test
  public void jspElementDynamicAttributeTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementDynamicAttributeTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<element1>|body1|</element1>|<element2>|body2|</element2>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementDynamicAttributeTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<element1>|body1|</element1>|<element2>|body2|</element2>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_element_web/JspElementDynamicAttributeTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<element1>|body1|</element1>|<element2>|body2|</element2>");
    invoke();
  }
}
