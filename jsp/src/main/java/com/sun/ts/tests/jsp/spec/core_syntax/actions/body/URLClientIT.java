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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.body;


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

  public URLClientIT() throws Exception {
    setup();

    setContextRoot("/jsp_core_act_body_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_core_act_body_web.war");
    archive.addClasses(
      com.sun.ts.tests.jsp.common.util.JspTestUtil.class);
    archive.addPackages(true, Filters.exclude(URLClientIT.class),
            URLClientIT.class.getPackageName());
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_core_act_body_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/body.tld", "body.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspBodyTest.jsp")), "JspBodyTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspBodyEmptyBodyTest.jsp")), "JspBodyEmptyBodyTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspBodyUsageContextTest1.jsp")), "JspBodyUsageContextTest1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspBodyUsageContextTest2.jsp")), "JspBodyUsageContextTest2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspBodyUsageContextTest3.jsp")), "JspBodyUsageContextTest3.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspBodyUsageContextTest4.jsp")), "JspBodyUsageContextTest4.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspBodyUsageContextTest5.jsp")), "JspBodyUsageContextTest5.jsp");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspBodyTest
   * 
   * @assertion_ids: JSP:SPEC:248
   * 
   * @test_Strategy: Validate that the body of an action is properly delivered
   * to a custom action when provided through jsp:body.
   */
  @Test
  public void jspBodyTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_body_web/JspBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test PASSED|Test PASSED|Test PASSED|Test PASSED");
    invoke();
  }

  /*
   * @testName: jspBodyUsageContextTest
   * 
   * @assertion_ids: JSP:SPEC:248.2;JSP:SPEC:248.3
   * 
   * @test_Strategy: Validate that translation-time errors will occur when
   * jsp:body is used in an incorrected context. - Not nested within a standard
   * or custom action - attempting to provide a body to an action not accepting
   * a body - jsp:body nested within a jsp:body - jsp:body nested within
   * jsp:attribute
   */
  @Test
  public void jspBodyUsageContextTest() throws Exception {
    for (int i = 0; i < 5; i++) {
      TEST_PROPS.setProperty(REQUEST,
          "GET /jsp_core_act_body_web/JspBodyUsageContextTest" + (i + 1)
              + ".jsp HTTP/1.1");
      TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
      invoke();
    }
  }

  /*
   * @testName: jspBodyEmptyBodyTest
   * 
   * @assertion_ids: JSP:SPEC:248.1
   * 
   * @test_Strategy: Verify that following empty body semantics involving
   * jsp:body: - If an action has one or more jsp:attribute elements and no
   * jsp:body element, then the action is considered empty. - Empty bodies can
   * be provided via jsp:body in the forms of &lt;jsp:body/&gt; and
   * &lt;jsp:body&gt;&lt;/jsp:body&gt;
   */
  @Test
  public void jspBodyEmptyBodyTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_body_web/JspBodyEmptyBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Test PASSED|Test PASSED|Test PASSED|Test PASSED|Test PASSED|Test PASSED");
    invoke();
  }
}
