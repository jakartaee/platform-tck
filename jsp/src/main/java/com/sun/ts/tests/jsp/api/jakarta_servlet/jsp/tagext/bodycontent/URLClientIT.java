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
 * @(#)URLClient.java	1.2 10/09/02
 */

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.bodycontent;


import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;

import java.io.IOException;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

/**
 * Test client for BodyContent
 */

@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {




  public URLClientIT() throws Exception {
    setup();
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_bodycontent_web.war");
    archive.addClasses(BodyContentClearBodyTag.class,
            BodyContentFlushTag.class, BodyContentGetStringTag.class,
            BodyContentReadWriteTag.class, BodyContentWriteOutTag.class,
            JspTestUtil.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_bodycontent_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/bodycontent.tld", "bodycontent.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/BodyContentClearBodyTest.jsp")), "BodyContentClearBodyTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/BodyContentFlushTest.jsp")), "BodyContentFlushTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/BodyContentGetStringTest.jsp")), "BodyContentGetStringTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/BodyContentReadWriteTest.jsp")), "BodyContentReadWriteTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/BodyContentWriteOutTest.jsp")), "BodyContentWriteOutTest.jsp");

    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: bodyContentFlushTest
   * 
   * @assertion_ids: JSP:JAVADOC:332
   * 
   * @test_Strategy: Validates that an IOException is thrown when
   * BodyContent.flush() is called.
   */
  @Test
  public void bodyContentFlushTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodycontent_web/BodyContentFlushTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: bodyContentClearBodyTest
   * 
   * @assertion_ids: JSP:JAVADOC:333
   * 
   * @test_Strategy: Validate that clearBuffer() works as expected.
   */
  @Test
  public void bodyContentClearBodyTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodycontent_web/BodyContentClearBodyTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: bodyContentReadWriteTest
   * 
   * @assertion_ids: JSP:JAVADOC:334;JSP:JAVADOC:338
   * 
   * @test_Strategy: Validate that a reader can be obtained containing the
   * bodycontent of the tag. Using the content that is read in, obtain a writer
   * and write the content to that writer.
   */
  @Test
  public void bodyContentReadWriteTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodycontent_web/BodyContentReadWriteTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "#TestPASSED#");
    invoke();
  }

  /*
   * @testName: bodyContentWriteOutTest
   * 
   * @assertion_ids: JSP:JAVADOC:336
   * 
   * @test_Strategy: Validate the behavior of BodyContent.writeOut().
   */
  @Test
  public void bodyContentWriteOutTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodycontent_web/BodyContentWriteOutTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: bodyContentGetStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:335
   * 
   * @test_Strategy: Validate the behavior of BodyContent.getString().
   */
  @Test
  public void bodyContentGetStringTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_bodycontent_web/BodyContentGetStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
