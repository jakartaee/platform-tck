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



package com.sun.ts.tests.jstl.spec.xml.xconditional.xforeach;

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
    setContextRoot("/jstl_xml_xforeach_web");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_xml_xforeach_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_xml_xforeach_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForEachVarTest.jsp")), "positiveForEachVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForEachVarStatusTest.jsp")), "positiveForEachVarStatusTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForEachStepTest.jsp")), "positiveForEachStepTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForEachSelectTest.jsp")), "positiveForEachSelectTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForEachSelectEmptyTest.jsp")), "positiveForEachSelectEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForEachEndTest.jsp")), "positiveForEachEndTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForEachEndLTBeginTest.jsp")), "positiveForEachEndLTBeginTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForEachBeginTest.jsp")), "positiveForEachBeginTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeForEachSelectReqAttrTest.jsp")), "negativeForEachSelectReqAttrTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeForEachSelectFailureTest.jsp")), "negativeForEachSelectFailureTest.jsp");
    
    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }


  /*
   * @testName: positiveForEachSelectTest
   * 
   * @assertion_ids: STL:SPEC:72; JSTL:SPEC:72.1; JSTL:SPEC:72.10
   * 
   * @testStrategy: Validate that the forEach action can properly iterate
   * through a node-set returned by the evaluated of the XPath expression
   * provided.
   */
  @Test
  public void positiveForEachSelectTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveForEachSelectTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveForEachSelectTest");
    invoke();
  }

  /*
   * @testName: positiveForEachVarTest
   * 
   * @assertion_ids: STL:SPEC:72; JSTL:SPEC:72.1; STL:SPEC:72.2
   * 
   * @testStrategy: Validate the following: - The variable specified by var is
   * treated as nested. - The type of var is a node-set therfore validate that
   * it is an instance of java.lang.Object. - Validate that var is available
   * within the body of the forEach action.
   */
  @Test
  public void positiveForEachVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveForEachVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveForEachVarTest");
    invoke();
  }

  /*
   * @testName: negativeForEachSelectReqAttrTest
   * 
   * @assertion_ids: JSTL:SPEC:72; JSTL:SPEC:72.2.2
   * 
   * @testStrategy: Validate that a fatal translation error occurs if the select
   * attribute is not present.
   */
  @Test
  public void negativeForEachSelectReqAttrTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeForEachSelectReqAttrTest");
    TEST_PROPS.setProperty(REQUEST, "negativeForEachSelectReqAttrTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeForEachSelectFailureTest
   * 
   * @assertion_ids: JSTL:SPEC:72; JSTL:SPEC:72.3
   * 
   * @testStrategy: Validate that an instance of jakarta.servlet.jsp.JspException
   * is thrown if the XPath expression provided to the select attribute fails to
   * evaluated.
   */
  @Test
  public void negativeForEachSelectFailureTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeForEachSelectFailureTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeForEachSelectFailureTest");
    invoke();
  }

  /*
   * @testName: positiveForEachBeginTest
   * 
   * @assertion_ids: JSTL:SPEC:72; JSTL:SPEC:72.7; JSTL:SPEC:72.7.1;
   * JSTL:SPEC:72.7.1.1; JSTL:SPEC:72.7.3; JSTL:SPEC:72.7.4
   * 
   * @test_Strategy: Validate the behavior of x:forEach when the begin attribute
   * is specified.
   */
  @Test
  public void positiveForEachBeginTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveForEachBeginTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveForEachBeginTest");
    invoke();
  }

  /*
   * @testName: positiveForEachEndTest
   * 
   * @assertion_ids: JSTL:SPEC:72; JSTL:SPEC:72.8; JSTL:SPEC:72.8.1;
   * JSTL:SPEC:72.8.2; JSTL:SPEC:72.8.3
   * 
   * @test_Strategy: Validate the behavior of x:forEach when the end attribute
   * is specified.
   */
  @Test
  public void positiveForEachEndTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveForEachEndTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveForEachEndTest");
    invoke();
  }

  /*
   * @testName: positiveForEachStepTest
   * 
   * @assertion_ids: JSTL:SPEC:72; JSTL:SPEC:72.7; JSTL:SPEC:72.7.2
   * 
   * @test_Strategy: Validate the behavior of the x:forEach action when the step
   * attribute is not specified.
   */
  @Test
  public void positiveForEachStepTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveForEachStepTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveForEachStepTest");
    invoke();
  }

  /*
   * @testName: positiveForEachVarStatusTest
   * 
   * @assertion_ids: JSTL:SPEC:72; JSTL:SPEC:72.6
   * 
   * @test_Strategy: Validate the action properly exports a VarStatus object on
   * each iteration when the varStatus attribute is specified.
   */
  @Test
  public void positiveForEachVarStatusTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveForEachVarStatusTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveForEachVarStatusTest");
    invoke();
  }

  /*
   * @testName: positiveForEachEndLTBeginTest
   * 
   * @assertion_ids: JSTL:SPEC:72; JSTL:SPEC:72.13
   * 
   * @test_Strategy: Validate that no iteration occurs of the end attribute
   * value is less than the value of the begin attribute.
   */
  @Test
  public void positiveForEachEndLTBeginTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_xml_xforeach_web/positiveForEachEndLTBeginTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "Test FAILED");
    invoke();
  }
}
