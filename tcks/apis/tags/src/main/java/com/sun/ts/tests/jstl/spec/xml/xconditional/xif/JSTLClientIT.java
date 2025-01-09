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



package com.sun.ts.tests.jstl.spec.xml.xconditional.xif;

import java.io.IOException;
import java.io.InputStream;

import com.sun.ts.tests.jstl.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

@Tag("jstl")
@Tag("platform")
@Tag("web")
@ExtendWith(ArquillianExtension.class)
public class JSTLClientIT extends AbstractUrlClient {

  public static String packagePath = JSTLClientIT.class.getPackageName().replace(".", "/");

  /** Creates new JSTLClient */
  public JSTLClientIT() {
    setContextRoot("/jstl_xml_xif_web");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_xml_xif_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_xml_xif_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeIfInvalidScopeTest.jsp")), "negativeIfInvalidScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeIfScopeVarTest.jsp")), "negativeIfScopeVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeIfSelectFailureTest.jsp")), "negativeIfSelectFailureTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeIfSelectReqAttrTest.jsp")), "negativeIfSelectReqAttrTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveIfScopeTest.jsp")), "positiveIfScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveIfSelectTest.jsp")), "positiveIfSelectTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveIfVarTest.jsp")), "positiveIfVarTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }


  /*
   * @testName: positiveIfSelectTest
   * 
   * @assertion_ids: JSTL:SPEC:70; JSTL:SPEC:70.1
   * 
   * @testStrategy: Validate that, providing a valid XPath expression to the
   * select attribute, will, depending on the result cause the <x:if> action to
   * process its body content.
   */
  @Test
  public void positiveIfSelectTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveIfSelectTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveIfSelectTest");
    invoke();
  }

  /*
   * @testName: positiveIfVarTest
   * 
   * @assertion_ids: JSTL:SPEC:70; JSTL:SPEC:70.2
   * 
   * @testStrategy: Validate that the following: - if var is present, the
   * Boolean result of the XPath evaluation is exported and available using the
   * name provided to var. - if var is present, and the action has a body, the
   * body is still processed.
   */
  @Test
  public void positiveIfVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveIfVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveIfVarTest");
    invoke();
  }

  /*
   * @testName: positiveIfScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:70; JSTL:SPEC:70.3; JSTL:SPEC:70.3.1;
   * JSTL:SPEC:70.4
   * 
   * @testStrategy: Validate that var is exported to the scope as specified by
   * the scope attribute. If scope is not specified, var will be exported to the
   * page scope by default.
   */
  @Test
  public void positiveIfScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveIfScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveIfScopeTest");
    invoke();
  }

  /*
   * @testName: negativeIfInvalidScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:70; JSTL:SPEC:70.3; JSTL:SPEC:70.3.5
   * 
   * @testStrategy: Validate that if scope is provided an invalid value, a fatal
   * translation error occurs.
   */
  @Test
  public void negativeIfInvalidScopeTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeIfInvalidScopeTest");
    TEST_PROPS.setProperty(REQUEST, "negativeIfInvalidScopeTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeIfSelectReqAttrTest
   * 
   * @assertion_ids: JSTL:SPEC:70; JSTL:SPEC:70.6; JSTL:SPEC:70.7
   * 
   * @testStrategy: Validate that 'select' is indeed a required attribute by
   * having an <x:if> action with no select. A fatal translation error should
   * occur.
   */
  @Test
  public void negativeIfSelectReqAttrTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeIfSelectReqAttrTest");
    TEST_PROPS.setProperty(REQUEST, "negativeIfSelectReqAttrTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeIfScopeVarTest
   * 
   * @assertion_ids: JSTL:SPEC:70; JSTL:SPEC:70.4; JSTL:SPEC:70.7
   * 
   * @testStrategy: Validate a fatal translation error occurs if scope is
   * specified by an action, but var is not.
   */
  @Test
  public void negativeIfScopeVarTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeIfScopeVarTest");
    TEST_PROPS.setProperty(REQUEST, "negativeIfScopeVarTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeIfSelectFailureTest
   * 
   * @assertion_ids: JSTL:SPEC:70; JSTL:SPEC:70.5
   * 
   * @testStrategy: Validate that an instance of jakarta.servlet.jsp.JspException
   * is thrown if the XPath expression provided to the select attribute fails to
   * evaluate.
   */
  @Test
  public void negativeIfSelectFailureTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeIfSelectFailureTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeIfSelectFailureTest");
    invoke();
  }
}
