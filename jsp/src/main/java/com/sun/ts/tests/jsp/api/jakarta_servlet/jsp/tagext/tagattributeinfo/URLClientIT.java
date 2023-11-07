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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.tagattributeinfo;


import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for TagAttributeInfo. Implementation note, all tests are
 * performed within a TagExtraInfo class. If the test fails, a translation error
 * will be generated and a ValidationMessage array will be returned.
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
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagattrinfo_web.war");
    archive.addClasses(TagAttributeInfoTEI.class,
            com.sun.ts.tests.jsp.common.util.JspTestUtil.class,
            com.sun.ts.tests.jsp.common.util.BaseTCKExtraInfo.class,
            com.sun.ts.tests.jsp.common.tags.tck.SimpleTag.class);

    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagattrinfo_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tagattributeinfo.tld", "tagattributeinfo.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/CanBeRequestTimeTest.jsp")), "CanBeRequestTimeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetIdAttribute.jsp")), "GetIdAttribute.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetNameTest.jsp")), "GetNameTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetTypeNameTest.jsp")), "GetTypeNameTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/IsFragmentTest.jsp")), "IsFragmentTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/IsRequiredTest.jsp")), "IsRequiredTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ToStringTest.jsp")), "ToStringTest.jsp");
    
    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: tagAttributeInfoGetNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:278
   * 
   * @test_Strategy: Validate the behavior of TagAttributeInfo.getName().
   */
  @Test
  public void tagAttributeInfoGetNameTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagattrinfo_web/GetNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagAttributeInfoGetTypeNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:279
   * 
   * @test_Strategy: Validate the behavior of TagAttributeInfo.getTypeName().
   */
  @Test
  public void tagAttributeInfoGetTypeNameTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagattrinfo_web/GetTypeNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagAttributeInfoCanBeRequestTimeTest
   * 
   * @assertion_ids: JSP:JAVADOC:280
   * 
   * @test_Strategy: Validate the behavior of
   * TagAttributeInfo.catBeRequestTime().
   */
  @Test
  public void tagAttributeInfoCanBeRequestTimeTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagattrinfo_web/CanBeRequestTimeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagAttributeInfoIsRequiredTest
   * 
   * @assertion_ids: JSP:JAVADOC:281
   * 
   * @test_Strategy: Validate the behavior of TagAttributeInfo.isRequired().
   */
  @Test
  public void tagAttributeInfoIsRequiredTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagattrinfo_web/IsRequiredTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagAttributeInfoIsFragmentTest
   * 
   * @assertion_ids: JSP:JAVADOC:283
   * 
   * @test_Strategy: Validate the behavior of TagAttributeInfo.siFragment().
   */
  @Test
  public void tagAttributeInfoIsFragmentTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagattrinfo_web/IsFragmentTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagAttributeInfoToStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:284
   * 
   * @test_Strategy: Validate the behavior of TagAttributeInfo.toString().
   */
  @Test
  public void tagAttributeInfoToStringTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagattrinfo_web/ToStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagAttributeInfoGetIdAttribute
   * 
   * @assertion_ids: JSP:JAVADOC:282
   * 
   * @test_Strategy: Convenience static method that goes through an array of
   * TagAttributeInfo objects and looks for "id".
   */
  @Test
  public void tagAttributeInfoGetIdAttribute() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagattrinfo_web/GetIdAttribute.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
