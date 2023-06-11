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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.tagext.tagvariableinfo;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

/**
 * Test client for TagVariableInfo. Implementation note, all tests are performed
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
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagvarinfo_web.war");
    archive.addClasses(TagVarInfoTEI.class,
            com.sun.ts.tests.jsp.common.util.JspTestUtil.class,
            com.sun.ts.tests.jsp.common.util.BaseTCKExtraInfo.class,
            com.sun.ts.tests.jsp.common.tags.tck.SimpleTag.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagvarinfo_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tagvarinfo.tld", "tagvarinfo.tld");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetClassNameTest.jsp")), "GetClassNameTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetDeclareTest.jsp")), "GetDeclareTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetNameFromAttributeTest.jsp")), "GetNameFromAttributeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetNameGivenTest.jsp")), "GetNameGivenTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/GetScopeTest.jsp")), "GetScopeTest.jsp");

    return archive;
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: tagVariableInfoGetClassNameTest
   * 
   * @assertion_ids: JSP:JAVADOC:189
   * 
   * @test_Strategy: Validate the JSP container properly parses the specified
   * TLD as refereneced by the taglib directive and that the TagVariableInfo
   * objects created at translation time return the expected values when calling
   * getClassName(). A translation error will occur if the test fails.
   */
  @Test
  public void tagVariableInfoGetClassNameTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagvarinfo_web/GetClassNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: tagVariableInfoGetNameGivenTest
   * 
   * @assertion_ids: JSP:JAVADOC:187
   * 
   * @test_Strategy: Validate the JSP container properly parses the specified
   * TLD as refereneced by the taglib directive and that the TagVariableInfo
   * objects created at translation time return the expected values when calling
   * getNameGiven(). A translation error will occur if the test fails.
   */
  @Test
  public void tagVariableInfoGetNameGivenTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagvarinfo_web/GetNameGivenTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: tagVariableInfoGetNameFromAttributeTest
   * 
   * @assertion_ids: JSP:JAVADOC:188
   * 
   * @test_Strategy: Validate the JSP container properly parses the specified
   * TLD as refereneced by the taglib directive and that the TagVariableInfo
   * objects created at translation time return the expected values when calling
   * getNameFromAttribute(). A translation error will occur if the test fails.
   */
  @Test
  public void tagVariableInfoGetNameFromAttributeTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagvarinfo_web/GetNameFromAttributeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: tagVariableInfoGetDeclareTest
   * 
   * @assertion_ids: JSP:JAVADOC:190
   * 
   * @test_Strategy: Validate the JSP container properly parses the specified
   * TLD as refereneced by the taglib directive and that the TagVariableInfo
   * objects created at translation time return the expected values when calling
   * getDeclare(). A translation error will occur if the test fails.
   */
  @Test
  public void tagVariableInfoGetDeclareTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagvarinfo_web/GetDeclareTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }

  /*
   * @testName: tagVariableInfoGetScopeTest
   * 
   * @assertion_ids: JSP:JAVADOC:191
   * 
   * @test_Strategy: Validate the JSP container properly parses the specified
   * TLD as refereneced by the taglib directive and that the TagVariableInfo
   * objects created at translation time return the expected values when calling
   * getScope(). A translation error will occur if the test fails.
   */
  @Test
  public void tagVariableInfoGetScopeTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagvarinfo_web/GetScopeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }
}
