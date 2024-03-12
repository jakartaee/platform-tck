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



package com.sun.ts.tests.jstl.spec.xml.xmlcore.xset;

import java.io.IOException;
import java.io.InputStream;

import com.sun.ts.tests.jstl.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

@ExtendWith(ArquillianExtension.class)
public class JSTLClientIT extends AbstractUrlClient {

  public static String packagePath = JSTLClientIT.class.getPackageName().replace(".", "/");

  /** Creates new JSTLClient */
  public JSTLClientIT() {
    setContextRoot("/jstl_xml_xset_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_xml_xset_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_xml_xset_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeSetInvalidScopeTest.jsp")), "negativeSetInvalidScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeSetSelectFailureTest.jsp")), "negativeSetSelectFailureTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeSetSelectReqAttrTest.jsp")), "negativeSetSelectReqAttrTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeSetVarReqAttrTest.jsp")), "negativeSetVarReqAttrTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetScopeTest.jsp")), "positiveSetScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetSelectVarTest.jsp")), "positiveSetSelectVarTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }


  /*
   * @testName: positiveSetSelectVarTest
   * 
   * @assertion_ids: JSTL:SPEC:69; JSTL:SPEC:69.1; JSTL:SPEC:69.2;
   * JSTL:SPEC:69.5
   * 
   * @testStrategy: Validate the action properly sets a scoped variable when
   * select is provided a valid XPath expression and the the variable reference
   * by var is available to another action.
   */
  @Test
  public void positiveSetSelectVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetSelectVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetSelectVarTest");
    invoke();
  }

  /*
   * @testName: positiveSetScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:69; JSTL:SPEC:69.3; JSTL:SPEC:69.3.1;
   * JSTL:SPEC:69.3.2; JSTL:SPEC:69.3.3; JSTL:SPEC:69.3.4; JSTL:SPEC:69.4;
   * JSTL:SPEC:69.5
   * 
   * @testStrategy: Validate that the presense of the scope attribute properly
   * exports var to the specified scope. Also verify that if scope is not
   * specified, that var is exported to the page scope by default.
   */
  @Test
  public void positiveSetScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetScopeTest");
    invoke();
  }

  /*
   * @testName: negativeSetSelectFailureTest
   * 
   * @assertion_ids: JSTL:SPEC:69; JSTL:SPEC:69.8
   * 
   * @testStrategy: Validate that if the XPath expression fails to evaluate an
   * instance of jakarta.servet.jsp.JspException is thrown.
   */
  @Test
  public void negativeSetSelectFailureTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeSetSelectFailureTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeSetSelectFailureTest");
    invoke();
  }

  /*
   * @testName: negativeSetVarReqAttrTest
   * 
   * @assertion_ids: JSTL:SPEC:69; JSTL:SPEC:69.2.1
   * 
   * @testStrategy: Validate that a fatal translation error occurs if the var
   * attribute is not present.
   */
  @Test
  public void negativeSetVarReqAttrTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeSetVarReqAttrTest");
    TEST_PROPS.setProperty(REQUEST, "negativeSetVarReqAttrTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeSetSelectReqAttrTest
   * 
   * @assertion_ids: JSTL:SPEC:69; JSTL:SPEC:69.2.1
   * 
   * @testStrategy: Validate that a fatal translation error occurs if the select
   * attribute is not present.
   */
  @Test
  public void negativeSetSelectReqAttrTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeSetSelectReqAttrTest");
    TEST_PROPS.setProperty(REQUEST, "negativeSetSelectReqAttrTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeSetInvalidScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:69; JSTL:SPEC:69.2.1
   * 
   * @testStrategy: Validate that a fatal translation error occurs if the select
   * attribute is not present.
   */
  @Test
  public void negativeSetInvalidScopeTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeSetInvalidScopeTest");
    TEST_PROPS.setProperty(REQUEST, "negativeSetInvalidScopeTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

}
