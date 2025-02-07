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



package com.sun.ts.tests.jstl.spec.xml.xmlcore.xout;

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
    setContextRoot("/jstl_xml_xout_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_xml_xout_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_xml_xout_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeOutSelectFailureTest.jsp")), "negativeOutSelectFailureTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeOutSelectReqAttrTest.jsp")), "negativeOutSelectReqAttrTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveOutEscXmlTest.jsp")), "positiveOutEscXmlTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveOutSelectTest.jsp")), "positiveOutSelectTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }


  /*
   * @testName: positiveOutSelectTest
   * 
   * @assertion_ids: JSTL:SPEC:68; JSTL:SPEC:68.1; JSTL:SPEC:68.6
   * 
   * @testStrategy: Validate that the action properly displays the result of an
   * XPath expression provided to the select attribute.
   */
  @Test
  public void positiveOutSelectTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveOutSelectTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveOutSelectTest");
    invoke();
  }

  /*
   * @testName: positiveOutEscXmlTest
   * 
   * @assertion_ids: JSTL:SPEC:68; JSTL:SPEC:68.2; JSTL:SPEC:68.2.1;
   * JSTL:SPEC:68.2.2; JSTL:SPEC:68.2.3; JSTL:SPEC:68.2.4; JSTL:SPEC:68.2.5;
   * JSTL:SPEC:68.2.6
   * 
   * @testStrategy: Validate that the escaping of XML entities (<,>,&,',") will
   * occur if the escapeXml is not present, or the value is true. Also validate
   * that no escaping occurs if the value is false.
   */
  @Test
  public void positiveOutEscXmlTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveOutEscXmlTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveOutEscXmlTest");
    invoke();
  }

  /*
   * @testName: negativeOutSelectFailureTest
   * 
   * @assertion_ids: JSTL:SPEC:68; JSTL:SPEC:68.5; JSTL:SPEC:68.7
   * 
   * @testStrategy: Validate that a jakarta.servlet.jsp.JspException is thrown if
   * the expression language fails to evaluate the provided XPath expression.
   */
  @Test
  public void negativeOutSelectFailureTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeOutSelectFailureTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeOutSelectFailureTest");
    invoke();
  }

  /*
   * @testName: negativeOutSelectReqAttrTest
   * 
   * @assertion_ids: JSTL:SPEC:68; JSTL:SPEC:68.6
   * 
   * @testStrategy: Validate that a fatal translation error is generated if the
   * select attribute is not present in the out action.
   */
  @Test
  public void negativeOutSelectReqAttrTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeOutSelectReqAttrTest");
    TEST_PROPS.setProperty(REQUEST, "negativeOutSelectReqAttrTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
