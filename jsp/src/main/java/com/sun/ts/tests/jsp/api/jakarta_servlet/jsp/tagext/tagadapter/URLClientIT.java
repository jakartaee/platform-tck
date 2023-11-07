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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.tagadapter;


import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for TagAdapter. There isn't much that can be done to really test
 * the runtime functionality of TagAdapter as the container will provide the
 * code to cause the wrapping to occur. This set of tests will perform basic API
 * tests to make sure that methods that should not be called by the container
 * throw an UnsupportedOperationException. The last test will make sure that a
 * tag nested within a SimpleTag will be passed an instance of the TagAdapter.
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
    setContextRoot("/jsp_tagadapter_web");
    setTestJsp("TagAdapterTest");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagadapter_web.war");
    archive.addClasses(TagAdapterVerifierTag.class,
            TASimpleTag.class,
            com.sun.ts.tests.jsp.common.util.JspTestUtil.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagadapter_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tagadapter.tld", "tagadapter.tld");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TagAdapterValidationTest.jsp")), "TagAdapterValidationTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/TagAdapterTest.jsp")), "TagAdapterTest.jsp");

    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: tagAdapterCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:285
   * 
   * @test_Strategy: Validates the constructor of the TagAdapter class.
   */
  @Test
  public void tagAdapterCtorTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "tagAdapterCtorTest");
    invoke();
  }

  /*
   * @testName: tagAdapterSetPageContextTest
   * 
   * @assertion_ids: JSP:JAVADOC:287
   * 
   * @test_Strategy: Validates that an UnsupportedOperationException is thrown
   * if a call to TagAdapter.setPageContext() is made.
   */
  @Test
  public void tagAdapterSetPageContextTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "tagAdapterSetPageContextTest");
    invoke();
  }

  /*
   * @testName: tagAdapterSetParentTest
   * 
   * @assertion_ids: JSP:JAVADOC:289
   * 
   * @test_Strategy: Validates that an UnsupportedOperationException is thrown
   * if a call to TagAdapter.setParent() is made.
   */
  @Test
  public void tagAdapterSetParentTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "tagAdapterSetParentTest");
    invoke();
  }

  /*
   * @testName: tagAdapterGetParentTest
   * 
   * @assertion_ids: JSP:JAVADOC:290
   * 
   * @test_Strategy: Validates that getParent always returns
   * getAdaptee().getParent()
   */
  @Test
  public void tagAdapterGetParentTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "tagAdapterGetParentTest");
    invoke();
  }

  /*
   * @testName: tagAdapterDoStartTagTest
   * 
   * @assertion_ids: JSP:JAVADOC:293;JSP:JAVADOC:294
   * 
   * @test_Strategy: Validates that an UnsupportedOperationException is thrown
   * if a call to TagAdapter.doStartTag() is made.
   */
  @Test
  public void tagAdapterDoStartTagTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "tagAdapterDoStartTagTest");
    invoke();
  }

  /*
   * @testName: tagAdapterDoEndTagTest
   * 
   * @assertion_ids: JSP:JAVADOC:296;JSP:JAVADOC:297
   * 
   * @test_Strategy: Validates that an UnsupportedOperationException is thrown
   * if a call to TagAdapter.doEndTag() is made.
   */
  @Test
  public void tagAdapterDoEndTagTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "tagAdapterDoEndTagTest");
    invoke();
  }

  /*
   * @testName: tagAdapterReleaseTest
   * 
   * @assertion_ids: JSP:JAVADOC:299
   * 
   * @test_Strategy: Validates that an UnsupportedOperationException is thrown
   * if a call to TagAdapter.release() is made.
   */
  @Test
  public void tagAdapterReleaseTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "tagAdapterReleaseTest");
    invoke();
  }

  /*
   * @testName: tagAdapterValidationTest
   * 
   * @assertion_ids: JSP:JAVADOC:286;JSP:JAVADOC:288;JSP:JAVADOC:291;
   * JSP:JAVADOC:292;JSP:JAVADOC:295;JSP:JAVADOC:298; JSP:JAVADOC:290
   * 
   * @test_Strategy: This validates that the container properly wraps a
   * SimpleTag instance with a TagAdapter when a Classic tag handler is a child
   * of the SimpleTag handler within the JSP Page. This also makes the
   * assumption, that all of the previous tests passed as it expects an
   * UnsupportedOperationException to be thrown if an illegal method call is
   * made on the TagAdapter.
   */
  @Test
  public void tagAdapterValidationTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagadapter_web/TagAdapterValidationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
