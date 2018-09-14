/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setContextRoot("/jsp_core_act_plugin_web");

    return super.run(args, out, err);
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
  public void jspPluginTest() throws Fault {
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginAppletTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "http://www.nowaythiswebsitecouldpossiblyexist.com|"
            + "fallback_text|vspace=1|hspace=1|"
            + "width=10|height=10|test=testvalue|applet|"
            + "code=foo.class|archive=test.jar|name=test|"
            + "align=middle|codebase=/");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginBeanTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "http://www.nowaythiswebsitecouldpossiblyexist.com|"
            + "fallback_text|vspace=1|hspace=1|"
            + "width=10|height=10|test=testvalue|bean|"
            + "code=foo.class|archive=test.jar|name=test|"
            + "align=middle|codebase=/");
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
  public void jspPluginDynamicAttributesTest() throws Fault {
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginHeightElAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "height=10");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginHeightRtAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "height=10");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginWidthElAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "width=10");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginWidthRtAttributeValueTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "width=10");
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
  public void jspParamsUsageContextTest() throws Fault {
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
  public void jspFallbackUsageContextTest() throws Fault {
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
  public void jspFallbackBodyTest() throws Fault {
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspFallbackBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "fallback_text");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "<jsp:body>|</jsp:body>");
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
  public void jspParamsBodyTest() throws Fault {
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspParamsBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "param1=value1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "<jsp:body>|</jsp:body>");
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
  public void jspPluginInvalidTypeTest() throws Fault {
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
  public void jspPluginJspAttributeTest() throws Fault {
    TEST_PROPS.setProperty(STRATEGY,
        "com.sun.ts.tests.jsp.spec.core_syntax.actions.plugin.JspPluginValidator");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_plugin_web/JspPluginJspAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "http://www.nowaythiswebsitecouldpossiblyexist.com|"
            + "fallback_text|vspace=1|hspace=1|"
            + "width=10|height=10|test=testvalue|bean|"
            + "code=foo.class|archive=test.jar|name=test|"
            + "align=middle|codebase=/");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "<jsp:attribute>|</jsp:attribute>");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "User-Agent: Mozilla/5.0 (Windows; U; Windows NT 5.0; en-US; rv:1.4) Gecko/20030624 Netscape/7.1 (ax)");
    invoke();
  }
}
