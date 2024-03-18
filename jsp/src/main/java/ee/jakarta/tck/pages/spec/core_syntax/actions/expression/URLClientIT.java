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

package ee.jakarta.tck.pages.spec.core_syntax.actions.expression;


import java.io.IOException;
import ee.jakarta.tck.pages.common.client.AbstractUrlClient;

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


    setContextRoot("/jsp_core_act_decl_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_core_act_expr_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_core_act_expr_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspExpressionTag1.tag", "tags/JspExpressionTag1.tag");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspExpressionTag2.tagx", "tags/JspExpressionTag2.tagx");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspExpressionUsageContextTest1.jsp")), "JspExpressionUsageContextTest1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspExpressionUsageContextTest2.jspx")), "JspExpressionUsageContextTest2.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspExpressionUsageContextTest3.jsp")), "JspExpressionUsageContextTest3.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspExpressionUsageContextTest4.jspx")), "JspExpressionUsageContextTest4.jspx");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspExpressionUsageContextTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the container's acceptance of the use of
   * jsp:expression in standard JSP pages, JSP documents and Tag files in both
   * standard and XML syntax.
   */
  @Test
  public void jspExpressionUsageContextTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_expr_web/JspExpressionUsageContextTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_expr_web/JspExpressionUsageContextTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<root>Test PASSED</root>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_expr_web/JspExpressionUsageContextTest3.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_expr_web/JspExpressionUsageContextTest4.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<root><subroot>Test PASSED</subroot></root>");
    invoke();
  }

}
