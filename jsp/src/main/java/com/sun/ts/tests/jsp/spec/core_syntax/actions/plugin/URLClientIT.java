/*
 * Copyright (c) 2007, 2022 Oracle and/or its affiliates and others.
 * All rights reserved.
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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin;


import java.io.IOException;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;

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


    setContextRoot("/jsp_core_act_plugin_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_core_act_plugin_web.war");
    archive.addClasses(JspPluginValidator.class, JspTestUtil.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_core_act_plugin_web.xml"));
    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginWidthRtAttributeValueTest.jsp")), "JspPluginWidthRtAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginWidthElAttributeValueTest.jsp")), "JspPluginWidthElAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginVspaceRtAttributeValueTest.jsp")), "JspPluginVspaceRtAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginVspaceElAttributeValueTest.jsp")), "JspPluginVspaceElAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginTypeRtAttributeValueTest.jsp")), "JspPluginTypeRtAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginTypeElAttributeValueTest.jsp")), "JspPluginTypeElAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginNsPluginUrlRtAttributeValueTest.jsp")), "JspPluginNsPluginUrlRtAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginNsPluginUrlElAttributeValueTest.jsp")), "JspPluginNsPluginUrlElAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginNameRtAttributeValueTest.jsp")), "JspPluginNameRtAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginNameElAttributeValueTest.jsp")), "JspPluginNameElAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginJspParamsNoParametersTest.jsp")), "JspPluginJspParamsNoParametersTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginJspAttributeTest.jsp")), "JspPluginJspAttributeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginJreversionRtAttributeValueTest.jsp")), "JspPluginJreversionRtAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginJreversionElAttributeValueTest.jsp")), "JspPluginJreversionElAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginInvalidTypeTest.jsp")), "JspPluginInvalidTypeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginIePluginUrlRtAttributeValueTest.jsp")), "JspPluginIePluginUrlRtAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginIePluginUrlElAttributeValueTest.jsp")), "JspPluginIePluginUrlElAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginHspaceRtAttributeValueTest.jsp")), "JspPluginHspaceRtAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginHspaceElAttributeValueTest.jsp")), "JspPluginHspaceElAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginHeightRtAttributeValueTest.jsp")), "JspPluginHeightRtAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginHeightElAttributeValueTest.jsp")), "JspPluginHeightElAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginCodeRtAttributeValueTest.jsp")), "JspPluginCodeRtAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginCodeReqAttributeTest.jsp")), "JspPluginCodeReqAttributeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginCodeElAttributeValueTest.jsp")), "JspPluginCodeElAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginCodeBaseRtAttributeValueTest.jsp")), "JspPluginCodeBaseRtAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginCodeBaseReqAttributeTest.jsp")), "JspPluginCodeBaseReqAttributeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginCodeBaseElAttributeValueTest.jsp")), "JspPluginCodeBaseElAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginBeanTest.jsp")), "JspPluginBeanTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginArchiveRtAttributeValueTest.jsp")), "JspPluginArchiveRtAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginArchiveElAttributeValueTest.jsp")), "JspPluginArchiveElAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginAppletTest.jsp")), "JspPluginAppletTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginAlignRtAttributeValueTest.jsp")), "JspPluginAlignRtAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspPluginAlignElAttributeValueTest.jsp")), "JspPluginAlignElAttributeValueTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspParamsUsageContextTest.jsp")), "JspParamsUsageContextTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspParamsBodyTest.jsp")), "JspParamsBodyTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspFallbackUsageContextTest.jsp")), "JspFallbackUsageContextTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspFallbackBodyTest.jsp")), "JspFallbackBodyTest.jsp");
    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspPluginTest
   * 
   * @assertion_ids: JSP:SPEC:167.4;JSP:SPEC:167.5;JSP:SPEC:167.6;
   * JSP:SPEC:167.8;JSP:SPEC:167.9;JSP:SPEC:167.10;
   * JSP:SPEC:167.11;JSP:SPEC:167.12;JSP:SPEC:167.13;
   * JSP:SPEC:167.15;JSP:SPEC:167.16;JSP:SPEC:167.17; JSP:SPEC:167.18
   * 
   * @test_Strategy: Validate, rather loosely as the output will be
   * implementation dependent, the output of a jsp:plugin action.
   */
  @Test
  public void jspPluginTest() throws Exception {
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginAppletTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "expected_text");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "http://www.nowaythiswebsitecouldpossiblyexist.com|" +
        "fallback_text|vspace=1|hspace=1|" +
        "width=10|height=10|test=testvalue|applet|" +
        "code=foo.class|archive=test.jar|name=test|" +
        "align=middle|codebase=/");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginBeanTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "expected_text");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "http://www.nowaythiswebsitecouldpossiblyexist.com|" +
        "fallback_text|vspace=1|hspace=1|" +
        "width=10|height=10|test=testvalue|bean|" +
        "code=foo.class|archive=test.jar|name=test|" +
        "align=middle|codebase=/");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
  }

  /*
   * @testName: jspPluginDynamicAttributesTest
   * 
   * @assertion_ids: JSP:SPEC:167.15.1;JSP:SPEC:167.9.1
   * 
   * @test_Strategy: Validate that the only the height and width attributes of
   * jsp:plugin accept RT and EL Expressions.
   */
  @Test
  public void jspPluginDynamicAttributesTest() throws Exception {
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginHeightElAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "height=10");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginHeightRtAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "height=10");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginWidthElAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "width=10");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginWidthRtAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "width=10");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginAlignElAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginAlignRtAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginArchiveElAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginArchiveRtAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginCodeBaseElAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginCodeBaseRtAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginCodeElAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginCodeRtAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginHspaceElAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginHspaceRtAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginIePluginUrlElAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginIePluginUrlRtAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginJreversionElAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginJreversionRtAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginNameElAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginNameRtAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginNsPluginUrlElAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginNsPluginUrlRtAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginTypeElAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginTypeRtAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginVspaceElAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginVspaceRtAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
  }

  /*
   * @testName: jspParamsUsageContextTest
   * 
   * @assertion_ids: JSP:SPEC:167.2.2
   * 
   * @test_Strategy: Validate that if the jsp:params action is not nested within
   * a jsp:plugin action, a translation-time error will occur.
   */
  @Test
  public void jspParamsUsageContextTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspParamsUsageContextTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
  }

  /*
   * @testName: jspFallbackUsageContextTest
   * 
   * @assertion_ids: JSP:SPEC:167.3.2
   * 
   * @test_Strategy: Validate that if the jsp:fallback action is used in a
   * context other than a nested child of the jsp:plugin action, a
   * translation-time error is generated.
   */
  @Test
  public void jspFallbackUsageContextTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspFallbackUsageContextTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
  }

  /*
   * @testName: jspFallbackBodyTest
   * 
   * @assertion_ids: JSP:SPEC:167.3.2
   * 
   * @test_Strategy: Validate the body of the jsp:fallback action can be
   * supplied using the jsp:body action.
   */
  @Test
  public void jspFallbackBodyTest() throws Exception {
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspFallbackBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "expected_text");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "<jsp:body>|fallback_text|</jsp:body>");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
  }

  /*
   * @testName: jspParamsBodyTest
   * 
   * @assertion_ids: JSP:SPEC:167.21
   * 
   * @test_Strategy: Validate the body of the jsp:params action can be supplied
   * using the jsp:body action.
   */
  @Test
  public void jspParamsBodyTest() throws Exception {
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspParamsBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "expected_text");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "<jsp:body>|param1=value1|</jsp:body>");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
  }

  /*
   * @testName: jspPluginInvalidTypeTest
   * 
   * @assertion_ids: JSP:SPEC:167.4.1
   * 
   * @test_Strategy: Validate that if the type attribute is provided a value
   * other than 'bean', or 'applet' that a translation error occurs.
   */
  @Test
  public void jspPluginInvalidTypeTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginInvalidTypeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
  }

  /*
   * @testName: jspPluginJspAttributeTest
   * 
   * @assertion_ids: JSP:SPEC:167.20
   * 
   * @test_Strategy: Validate the attributes of the jsp:plugin action can be
   * described by the jsp:attribute action.
   */
  @Test
  public void jspPluginJspAttributeTest() throws Exception {
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginJspAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "expected_text");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "<jsp:attribute>|</jsp:attribute>|" +
        "http://www.nowaythiswebsitecouldpossiblyexist.com|" +
        "fallback_text|vspace=1|hspace=1|" +
        "width=10|height=10|test=testvalue|bean|" +
        "code=foo.class|archive=test.jar|name=test|" +
        "align=middle|codebase=/");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
  }
}
