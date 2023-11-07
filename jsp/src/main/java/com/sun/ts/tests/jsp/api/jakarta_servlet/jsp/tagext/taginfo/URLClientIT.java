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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.taginfo;


import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import com.sun.ts.tests.jsp.common.util.BaseTCKExtraInfo;
import com.sun.ts.tests.jsp.common.tags.tck.SimpleTag;
import com.sun.ts.tests.common.el.api.expression.ExpressionTest;

/**
 * Test client for TagInfo. Implementation note, all tests are performed within
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
import org.junit.jupiter.api.Disabled;


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {




  public URLClientIT() throws Exception {
    setup();
    setContextRoot("/jsp_taginfo_web");

    }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_taginfo_web.war");
    archive.addClasses(TagInfoTEI.class,
            JspTestUtil.class,
            BaseTCKExtraInfo.class,
            SimpleTag.class,
            ExpressionTest.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_taginfo_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/taginfo.tld", "taginfo.tld");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetAttributesTest.jsp")), "GetAttributesTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetBodyContentTest.jsp")), "GetBodyContentTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetDisplayNameTest.jsp")), "GetDisplayNameTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetInfoStringTest.jsp")), "GetInfoStringTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetLargeIconTest.jsp")), "GetLargeIconTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetSmallIconTest.jsp")), "GetSmallIconTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetTagClassNameTest.jsp")), "GetTagClassNameTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetTagExtraInfoTest.jsp")), "GetTagExtraInfoTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetTagLibraryTest.jsp")), "GetTagLibraryTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetTagNameTest.jsp")), "GetTagNameTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetTagVariableInfosTest.jsp")), "GetTagVariableInfosTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/HasDynamicAttributesTest.jsp")), "HasDynamicAttributesTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/SetTagExtraInfoTest.jsp")), "SetTagExtraInfoTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/SetTagLibraryTest.jsp")), "SetTagLibraryTest.jsp");

    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: tagInfoGetTagNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:232
   * 
   * @test_Strategy: Validate TagInfo.getTagName() returns the expected values
   * based on what is defined in the TLD.
   */
  @Test
  public void tagInfoGetTagNameTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetTagNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetAttributesTest
   * 
   * @assertion_ids: JSP:JAVADOC:233
   * 
   * @test_Strategy: Validate TagInfo.getAttributes() returns the expected
   * values based on what is defined in the TLD.
   */
  @Test
  public void tagInfoGetAttributesTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetAttributesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetTagExtraInfoTest
   * 
   * @assertion_ids: JSP:JAVADOC:237;JSP:JAVADOC:238
   * 
   * @test_Strategy: Validate TagInfo.getTagExtraInfo() returns the expected
   * values based on what is defined in the TLD. This implicitly tests
   * TagInfo.setTagExtraInfo().
   */
  @Test
  public void tagInfoGetTagExtraInfoTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetTagExtraInfoTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetTagClassNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:239
   * 
   * @test_Strategy: Validate TagInfo.getTagClassName() returns the expected
   * values based on what is defined in the TLD.
   */
  @Test
  public void tagInfoGetTagClassNameTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetTagClassNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetBodyContentTest
   * 
   * @assertion_ids: JSP:JAVADOC:240
   * 
   * @test_Strategy: Validate TagInfo.getBodyContent() returns the expected
   * values based on what is defined in the TLD.
   */
  @Test
  public void tagInfoGetBodyContentTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetBodyContentTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetInfoStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:241
   * 
   * @test_Strategy: Validate TagInfo.getInfoString() returns the expected
   * values based on what is defined in the TLD.
   */
  @Test
  public void tagInfoGetInfoStringTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetInfoStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetTagLibraryTest
   * 
   * @assertion_ids: JSP:JAVADOC:242;JSP:JAVADOC:243
   * 
   * @test_Strategy: Validate TagInfo.getAttributes() returns the expected
   * values based on what is defined in the TLD. This implicitly tests
   * TagInfo.setTagLibrary().
   */
  @Test
  public void tagInfoGetTagLibraryTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetTagLibraryTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetDisplayNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:244
   * 
   * @test_Strategy: Validate TagInfo.getDisplayName() returns the expected
   * values based on what is defined in the TLD.
   */
  @Test
  public void tagInfoGetDisplayNameTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetDisplayNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetSmallIconTest
   * 
   * @assertion_ids: JSP:JAVADOC:245
   * 
   * @test_Strategy: Validate TagInfo.getSmallIconName() returns the expected
   * values based on what is defined in the TLD.
   */
  @Test
  public void tagInfoGetSmallIconTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetSmallIconTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetLargeIconTest
   * 
   * @assertion_ids: JSP:JAVADOC:246
   * 
   * @test_Strategy: Validate TagInfo.getLargeIconName() returns the expected
   * values based on what is defined in the TLD.
   */
  @Test
  public void tagInfoGetLargeIconTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetLargeIconTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoGetTagVariableInfosTest
   * 
   * @assertion_ids: JSP:JAVADOC:247
   * 
   * @test_Strategy: Validate TagInfo.getTagVariableInfos() returns the expected
   * values based on what is defined in the TLD.
   */
  @Test
  public void tagInfoGetTagVariableInfosTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/GetTagVariableInfosTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoHasDynamicAttributesTest
   * 
   * @assertion_ids: JSP:JAVADOC:248
   * 
   * @test_Strategy: Validate TagInfo.hasDynamicAttributes() returns the
   * expected values based on what is defined in the TLD.
   */
  @Test
  public void tagInfoHasDynamicAttributesTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/HasDynamicAttributesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * testName: tagInfoIsValidTest assertion_ids: JSP:JAVADOC:235 test_Strategy:
   * Translation-time validation of the attributes. This is a convenience method
   * on the associated TagExtraInfo class. This method should be exercised with
   * a jsp 1.1 tld. See tagInfoConstructor11Test.
   */
  // @Test
  // @Disabled("Disabled in legacy")
  public void tagInfoIsValidTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/IsValidTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoSetTagExtraInfoTest
   * 
   * @assertion_ids: JSP:JAVADOC:237
   * 
   * @test_Strategy: Set the instance for extra tag information
   */
  @Test
  public void tagInfoSetTagExtraInfoTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/SetTagExtraInfoTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagInfoSetTagLibraryTest
   * 
   * @assertion_ids: JSP:JAVADOC:242
   * 
   * @test_Strategy: Set the TagLibraryInfo property.
   */
  @Test
  public void tagInfoSetTagLibraryTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taginfo_web/SetTagLibraryTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

}
