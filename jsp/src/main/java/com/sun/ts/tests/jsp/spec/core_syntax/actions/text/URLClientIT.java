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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.text;


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
    setup();

    setContextRoot("/jsp_core_act_text_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_core_act_text_web.war");
    archive.addClasses(JspTestUtil.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_core_act_text_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspTextTag.tag", "tags/JspTextTag.tag");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTextBodyRestrictionsTest1.jsp")), "JspTextBodyRestrictionsTest1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTextBodyRestrictionsTest2.jsp")), "JspTextBodyRestrictionsTest2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTextBodyRestrictionsTest3.jsp")), "JspTextBodyRestrictionsTest3.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTextBodyRestrictionsTest4.jsp")), "JspTextBodyRestrictionsTest4.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTextBodyRestrictionsTest5.jspx")), "JspTextBodyRestrictionsTest5.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTextUsageContextTest1.jsp")), "JspTextUsageContextTest1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTextUsageContextTest2.jspx")), "JspTextUsageContextTest2.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspTextUsageContextTest3.jsp")), "JspTextUsageContextTest3.jsp");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspTextUsageContextTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the behavior of jsp:text.
   */
  @Test
  public void jspTextUsageContextTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_text_web/JspTextUsageContextTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Template\nText|Template Text2");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_text_web/JspTextUsageContextTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Template\nText|Template Text2");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_text_web/JspTextUsageContextTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Template Text|Template\nText2");
    invoke();
  }

  /*
   * @testName: jspTextBodyRestrictionsTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that neiter nested actions nor scripting elements
   * are allowed in the body of jsp:text.
   */
  @Test
  public void jspTextBodyRestrictionsTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_text_web/JspTextBodyRestrictionsTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_text_web/JspTextBodyRestrictionsTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_text_web/JspTextBodyRestrictionsTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_text_web/JspTextBodyRestrictionsTest4.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_text_web/JspTextBodyRestrictionsTest5.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
