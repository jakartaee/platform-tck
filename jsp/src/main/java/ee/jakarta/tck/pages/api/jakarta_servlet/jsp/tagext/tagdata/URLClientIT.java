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

package ee.jakarta.tck.pages.api.jakarta_servlet.jsp.tagext.tagdata;


import ee.jakarta.tck.pages.common.client.AbstractUrlClient;
import ee.jakarta.tck.pages.common.util.JspTestUtil;
import ee.jakarta.tck.pages.common.util.BaseTCKExtraInfo;
import ee.jakarta.tck.pages.common.tags.tck.SimpleTag;

/**
 * Test client for TagData. Implementation note, all tests are performed within
 * a TagExtraInfo class. If the test fails, a translation error will be
 * generated and a ValidationMessage array will be returned.
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

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagdata_web.war");
    archive.addClasses(TagDataTEI.class,
            JspTestUtil.class,
            BaseTCKExtraInfo.class,
            SimpleTag.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagdata_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tagdata.tld", "tagdata.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ConstructorTest.jsp")), "ConstructorTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetAttributesTest.jsp")), "GetAttributesTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetAttributeStringTest.jsp")), "GetAttributeStringTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetAttributeTest.jsp")), "GetAttributeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/SetAttributeTest.jsp")), "SetAttributeTest.jsp");

    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: tagDataGetAttributeTest
   * 
   * @assertion_ids: JSP:JAVADOC:271
   * 
   * @test_Strategy: Validate the behavior of TagData.getAttribute().
   */
  @Test
  public void tagDataGetAttributeTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagdata_web/GetAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagDataSetAttributeTest
   * 
   * @assertion_ids: JSP:JAVADOC:272
   * 
   * @test_Strategy: Validate the behavior of TagData.setAttribute().
   */
  @Test
  public void tagDataSetAttributeTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagdata_web/SetAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagDataGetAttributeStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:273;JSP:JAVADOC:274
   * 
   * @test_Strategy: Validate the behavior of TagData.getAttributeString().
   */
  @Test
  public void tagDataGetAttributeStringTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagdata_web/GetAttributeStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagDataGetAttributesTest
   * 
   * @assertion_ids: JSP:JAVADOC:275
   * 
   * @test_Strategy: Validate the behavior of TagData.getAttributes().
   */
  @Test
  public void tagDataGetAttributesTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagdata_web/GetAttributesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: tagDataConstructorTest
   * 
   * @assertion_ids: JSP:JAVADOC:268
   * 
   * @test_Strategy: validate the constructor TagData(Object[][]).
   */
  @Test
  public void tagDataConstructorTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagdata_web/ConstructorTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
