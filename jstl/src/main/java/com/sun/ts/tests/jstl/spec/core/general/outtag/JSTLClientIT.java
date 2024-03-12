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



package com.sun.ts.tests.jstl.spec.core.general.outtag;

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
    setContextRoot("/jstl_core_gen_out_web");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_core_gen_out_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_core_gen_out_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveOutValueAttributeTest.jsp")), "positiveOutValueAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveOutDefaultAttributeTest.jsp")), "positiveOutDefaultAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveOutBodyBehaviorTest.jsp")), "positiveOutBodyBehaviorTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveOutEscXmlDefaultTest.jsp")), "positiveOutEscXmlDefaultTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveOutEscXmlTest.jsp")), "positiveOutEscXmlTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeOutBodyContentExcTest.jsp")), "negativeOutBodyContentExcTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveOutReaderTest.jsp")), "positiveOutReaderTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveOutValueAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:12.1; JSTL:SPEC:12.1.1
   * 
   * @testStrategy: Validate the the 'value' attribute of the out action can
   * accept both EL and static values.
   */
  @Test
  public void positiveOutValueAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveOutValueAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveOutValueAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveOutDefaultAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:12.3; JSTL:SPEC:12.3.1
   * 
   * @testStrategy: Validate that the 'default' attribute of the out action can
   * accept both EL and static values.
   */
  @Test
  public void positiveOutDefaultAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveOutDefaultAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveOutDefaultAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveOutBodyBehaviorTest
   * 
   * @assertion_ids: JSTL:SPEC:12.5
   * 
   * @testStrategy: Validate the default value returned in case of an expression
   * failure, or a null value returned, can be specified in the body of the
   * action.
   *
   */
  @Test
  public void positiveOutBodyBehaviorTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveOutBodyBehaviorTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveOutBodyBehaviorTest");
    invoke();
  }

  /*
   * @testName: positiveOutEscXmlDefaultTest
   * 
   * @assertion_ids: JSTL:SPEC:12.2.1; JSTL:SPEC:12.2.10
   * 
   * @testStrategy: Validate that if escapeXml is not specified, the escaping of
   * <, >, ', ", & will be performed by default.
   */
  @Test
  public void positiveOutEscXmlDefaultTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveOutEscXmlDefaultTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveOutEscXmlDefaultTest");
    invoke();
  }

  /*
   * @testName: positiveOutEscXmlTest
   * 
   * @assertion_ids: JSTL:SPEC:12.2.2; JSTL:SPEC:12.2.3; JSTL:SPEC:12.2.5;
   * JSTL:SPEC:12.2.6; JSTL:SPEC:12.2.7; JSTL:SPEC:12.2.8; JSTL:SPEC:12.2.9
   * 
   * @testStrategy: Validate that escapeXml behaves as specified when the
   * setting is true or false. If true, <, >, ', ", and & will be escaped, and
   * if false, no escaping is performed.
   */
  @Test
  public void positiveOutEscXmlTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveOutEscXmlTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveOutEscXmlTest");
    invoke();
  }

  /*
   * @testName: negativeOutBodyContentExcTest
   * 
   * @assertion_ids: JSTL:SPEC:12.7
   * 
   * @testStrategy: Validate that an exception caused by the body content is
   * properly propagated and not handled by the action.
   */
  @Test
  public void negativeOutBodyContentExcTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeOutBodyContentExcTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeOutBodyContentExcTest");
    invoke();
  }

  /*
   * @testName: positiveOutReaderTest
   * 
   * @assertion_ids: JSTL:SPEC:12.1.4
   * 
   * @test_Strategy: Validate that if a java.io.Reader object is provided to the
   * value attribute of the out tag, that the contents of the reader are emitted
   * to the current JspWriter object.
   */
  @Test
  public void positiveOutReaderTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_core_gen_out_web/positiveOutReaderTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
