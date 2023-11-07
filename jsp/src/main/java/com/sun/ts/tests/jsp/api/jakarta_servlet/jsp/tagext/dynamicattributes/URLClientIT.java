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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.dynamicattributes;


import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for the DynamicAttributes interface.
 */
import java.io.IOException;
import java.io.InputStream;

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
    setContextRoot("/jsp_bodytagsupp_web");
    setTestJsp("BodyTagSupportApiTest");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_dynattrib_web.war");
    archive.addClasses(DynamicAttributesTag.class, SimpleExcTag.class,
            com.sun.ts.tests.jsp.common.util.JspTestUtil.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_dynattrib_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/dynamicattributes.tld", "dynamicattributes.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/SetDynamicAttributesTest.jsp")), "SetDynamicAttributesTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/DynamicAttributesExceptionTest.jsp")), "DynamicAttributesExceptionTest.jsp");

    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: dynamicAttributesTest
   * 
   * @assertion_ids: JSP:JAVADOC:372
   * 
   * @test_Strategy: Validate undeclared attributes can be set on a tag handler
   * for attributes not specified in the TLD.
   */
  @Test
  public void dynamicAttributesTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_dynattrib_web/SetDynamicAttributesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: dynamicAttributesExceptionTest
   * 
   * @assertion_ids: JSP:JAVADOC:372
   * 
   * @test_Strategy: Validate that if a JspException is thrown by
   * setDynamicAttribute(), that doStartTag() or doTag() is not called on the
   * handler.
   */
  @Test
  public void dynamicAttributesExceptionTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_dynattrib_web/DynamicAttributesExceptionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Classic - Test PASSED");
    TEST_PROPS.setProperty(SEARCH_STRING, "Simple - Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }
}
