/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.taglibraryinfo;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for TagLibraryInfo. Implementation note, all tests are performed
 * within a TagExtraInfo class. If the test fails, a translation error will be
 * generated and a ValidationMessage array will be returned.
 */
import java.io.IOException;
import java.io.InputStream;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {


  public URLClientIT() throws Exception {
    setup();
    setContextRoot("/jsp_tagvarinfo_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_taglibinfo_web.war");
    archive.addClasses(TagLibraryInfoTEI.class,
            com.sun.ts.tests.jsp.common.util.JspTestUtil.class,
            com.sun.ts.tests.jsp.common.util.JspFunctions.class,
            com.sun.ts.tests.jsp.common.util.BaseTCKExtraInfo.class,
            com.sun.ts.tests.jsp.common.tags.tck.SimpleTag.class);

    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_taglibinfo_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/taglibinfo.tld", "taglibinfo.tld");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/taglibinfo2.tld", "taglibinfo2.tld");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/taglibinfo/TagFile1.tag", "tags/taglibinfo/TagFile1.tag");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/taglibinfo/TagFile2.tag", "tags/taglibinfo/TagFile2.tag");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetFunctionsTest.jsp")), "GetFunctionsTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetFunctionTest.jsp")), "GetFunctionTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetInfoStringTest.jsp")), "GetInfoStringTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetPrefixStringTest.jsp")), "GetPrefixStringTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetReliableURNTest.jsp")), "GetReliableURNTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetRequiredVersionTest.jsp")), "GetRequiredVersionTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetShortNameTest.jsp")), "GetShortNameTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetTagFilesTest.jsp")), "GetTagFilesTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetTagFileTest.jsp")), "GetTagFileTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetTagLibraryInfosTest.jsp")), "GetTagLibraryInfosTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetTagsTest.jsp")), "GetTagsTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetTagTest.jsp")), "GetTagTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetURITest.jsp")), "GetURITest.jsp");

    return archive;
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: tagLibraryInfoGetURITest
   * 
   * @assertion_ids: JSP:JAVADOC:216
   * 
   * @test_Strategy: Validate that TagLibaryInfo.getURI() returns the URI as
   * defined by the taglib directive within a JSP page.
   */
  @Test
  public void tagLibraryInfoGetURITest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetURITest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetPrefixStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:218
   * 
   * @testStrategy: Validate that TagLibraryInfo.getPrefixString() returns the
   * prefix as specified by the prefix attribute of the taglib directive within
   * a JSP page.
   */
  @Test
  public void tagLibraryInfoGetPrefixStringTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetPrefixStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetShortNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:219
   * 
   * @testStrategy: Validate that TagLibraryInfo.getShortName() returns the
   * short name of the tag library as specified by the &lt;short-name&gt;
   * element in a TLD.
   */
  @Test
  public void tagLibraryInfoGetShortNameTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetShortNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetReliableURNTest
   * 
   * @assertion_ids: JSP:JAVADOC:220
   * 
   * @testStrategy: Validate that TagLibraryInfo.getShortName() returns the uri
   * of the tag library as specified by the &lt;uri&gt; element in a TLD.
   */
  @Test
  public void tagLibraryInfoGetReliableURNTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetReliableURNTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetInfoStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:221
   * 
   * @testStrategy: Validate that TagLibraryInfo.getInfoString() returns the
   * description of the tag library as specified by the &lt;description&gt;
   * element in a TLD.
   */
  @Test
  public void tagLibraryInfoGetInfoStringTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetInfoStringTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetRequiredVersionTest
   * 
   * @assertion_ids: JSP:JAVADOC:222
   * 
   * @testStrategy: Validate that TagLibraryInfo.getRequiredVersion() returns
   * the require version of the tag library as specified by the
   * &lt;required-version&gt; element in a TLD.
   */
  @Test
  public void tagLibraryInfoGetRequiredVersionTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetRequiredVersionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetTagsTest
   * 
   * @assertion_ids: JSP:JAVADOC:223
   * 
   * @testStrategy: Validate that TagLibraryInfo.getTags() returns an array of
   * TagInfo objects based off the tags defined by the &lt;tag&gt; elements in a
   * TLD.
   */
  @Test
  public void tagLibraryInfoGetTagsTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetTagsTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetTagFilesTest
   * 
   * @assertion_ids: JSP:JAVADOC:224
   * 
   * @testStrategy: Validate that TagLibraryInfo.getTagFiles() returns an array
   * of TagFileInfo objects based off the tags files defined by the
   * &lt;tag-file&gt; elements in a TLD.
   */
  @Test
  public void tagLibraryInfoGetTagFilesTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetTagFilesTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetTagTest
   * 
   * @assertion_ids: JSP:JAVADOC:225
   * 
   * @testStrategy: Validate that TagLibraryInfo.getTag() returns a TagInfo
   * object based off the provided discriminate.
   */
  @Test
  public void tagLibraryInfoGetTagTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetTagTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetTagFileTest
   * 
   * @assertion_ids: JSP:JAVADOC:226
   * 
   * @testStrategy: Validate that TagLibraryInfo.getTagFile() returns a
   * TagFileInfo object based off the provided discriminate.
   */
  @Test
  public void tagLibraryInfoGetTagFileTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetTagFileTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetFunctionsTest
   * 
   * @assertion_ids: JSP:JAVADOC:227
   * 
   * @testStrategy: Validate that TagLibraryInfo.getFunctions() returns an array
   * of FunctionInfo objects based off the functions defined by the
   * &lt;function&gt; elements in a TLD.
   */
  @Test
  public void tagLibraryInfoGetFunctionsTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetFunctionsTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetFunctionTest
   * 
   * @assertion_ids: JSP:JAVADOC:228
   * 
   * @testStrategy: Validate that TagLibraryInfo.getFunction() returns a
   * FunctionInfo object based off the provided discriminate.
   */
  @Test
  public void tagLibraryInfoGetFunctionTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetFunctionTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagLibraryInfoGetTagLibraryInfosTest
   * 
   * @assertion_ids: JSP:JAVADOC:440
   * 
   * @testStrategy: Validate TagLibaryInfo.getTagLibraryInfos() returns an array
   * of TagLibraryInfo objects for all taglibraries declared in the JSP.
   */
  @Test
  public void tagLibraryInfoGetTagLibraryInfosTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_taglibinfo_web/GetTagLibraryInfosTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }
}
