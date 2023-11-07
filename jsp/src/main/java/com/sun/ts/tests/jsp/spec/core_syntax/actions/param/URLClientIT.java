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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.param;


import java.io.IOException;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import com.sun.ts.tests.jsp.common.tags.tck.SimpleTag;

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

    setContextRoot("/jsp_core_act_param_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_core_act_param_web.war");
    archive.addClasses(JspTestUtil.class, SimpleTag.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_core_act_param_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/simple.tld", "simple.tld");    

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/Resource.jsp")), "Resource.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspParamUsageContextTest4.jsp")), "JspParamUsageContextTest4.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspParamUsageContextTest3.jsp")), "JspParamUsageContextTest3.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspParamUsageContextTest2.jsp")), "JspParamUsageContextTest2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspParamUsageContextTest1.jsp")), "JspParamUsageContextTest1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspParamNoValueAttributeTest.jsp")), "JspParamNoValueAttributeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspParamNoNameAttributeTest.jsp")), "JspParamNoNameAttributeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspParamNameRtValuesTest.jsp")), "JspParamNameRtValuesTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspParamNameElValuesTest.jsp")), "JspParamNameElValuesTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspParamIncludeTest.jsp")), "JspParamIncludeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspParamForwardTest.jsp")), "JspParamForwardTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspParamAttributesTest.jsp")), "JspParamAttributesTest.jsp");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspParamUsageContextTest
   * 
   * @assertion_ids: JSP:SPEC:166.3
   * 
   * @test_Strategy: Validate a translation-time error is raised if the
   * jsp:param element is not a child of jsp:forward, jsp:include, or
   * jsp:params.
   */
  @Test
  public void jspParamUsageContextTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_param_web/JspParamUsageContextTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_param_web/JspParamUsageContextTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_param_web/JspParamUsageContextTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_param_web/JspParamUsageContextTest4.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspParamAttributesTest
   * 
   * @assertion_ids: JSP:SPEC:166.1;JSP:SPEC:166.2;JSP:SPEC:166.6;
   * JSP:SPEC:166.7;JSP:SPEC:166.8;JSP:SPEC:166.9
   * 
   * @test_Strategy: Validate the following: - Confirm name and values
   * attributes are required by checking for a translation-time error if they
   * are not present. - Confirm that the value attribute can accept both EL and
   * RT expressions. - Confirm that the name attribute cannot accept either RT
   * or EL expressions by looking for a translation-time error if the attribute
   * is provided with either. - The attributes can be set using jsp:attribute
   */
  @Test
  public void jspParamAttributesTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_param_web/JspParamAttributesTest.jsp?param1=value1 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED|Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_param_web/JspParamNoNameAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_param_web/JspParamNoValueAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_param_web/JspParamNameElValuesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_param_web/JspParamNameRtValuesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspParamForwardTest
   * 
   * @assertion_ids: JSP:SPEC:166.4
   * 
   * @test_Strategy: Validate that params added to a jsp:forward are properly
   * aggregated and available to the target resource.
   */
  @Test
  public void jspParamForwardTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_param_web/JspParamForwardTest.jsp?param1=value1 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: jspParamIncludeTest
   * 
   * @assertion_ids: JSP:SPEC:166.5;JSP:SPEC:166.10
   * 
   * @test_Strategy: Validate that params added to a jsp:included are properly
   * aggregated and available to the target resource, and are not available to
   * the calling page after the include completes.
   */
  @Test
  public void jspParamIncludeTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_param_web/JspParamIncludeTest.jsp?param1=value1 HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }
}
