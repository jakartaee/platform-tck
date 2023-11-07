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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.tagfileinfo;


import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;
import com.sun.ts.tests.jsp.common.util.JspTestUtil;
import com.sun.ts.tests.jsp.common.util.BaseTCKExtraInfo;
import com.sun.ts.tests.jsp.common.tags.tck.SimpleTag;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

/**
 * Test client for TagFileInfo. Implementation note, all tests are performed
 * within a TagExtraInfo class. If the test fails, a translation error will be
 * generated and a ValidationMessage array will be returned.
 */


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {




  public URLClientIT() throws Exception {
    setup();
    setContextRoot("/jsp_taginfo_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagfileinfo_web.war");
    archive.addClasses(TagFileInfoTEI.class,
            JspTestUtil.class,
            BaseTCKExtraInfo.class,
            SimpleTag.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagfileinfo_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tagfileinfo.tld", "tagfileinfo.tld");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/tagfileinfo/TagFile1.tag", "tags/tagfileinfo/TagFile1.tag");    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetNameTest.jsp")), "GetNameTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetPathTest.jsp")), "GetPathTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetTagInfoTest.jsp")), "GetTagInfoTest.jsp");

    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: tagFileInfoGetNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:259
   * 
   * @test_Strategy: Validate TagFileInfo.getName returns the expected values
   * based on what is defined in the TLD.
   */
  @Test
  public void tagFileInfoGetNameTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfileinfo_web/GetNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagFileInfoGetPathTest
   * 
   * @assertion_ids: JSP:JAVADOC:260
   * 
   * @test_Strategy: Validate TagFileInfo.getPath() returns the expected values
   * based on what is defined in the TLD.
   */
  @Test
  public void tagFileInfoGetPathTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfileinfo_web/GetPathTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }

  /*
   * @testName: tagFileInfoGetTagInfoTest
   * 
   * @assertion_ids: JSP:JAVADOC:261
   * 
   * @test_Strategy: Validate TagFileInfo.getTagInfo() returns the expected
   * values based on what is defined in the tag file.
   */
  @Test
  public void tagFileInfoGetTagInfoTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfileinfo_web/GetTagInfoTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED.");
    invoke();
  }
}
