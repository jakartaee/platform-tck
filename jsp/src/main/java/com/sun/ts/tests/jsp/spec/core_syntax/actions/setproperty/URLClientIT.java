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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.setproperty;


import java.io.IOException;
import java.io.InputStream;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

import java.lang.System.Logger;

@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {

  private static final Logger logger = System.getLogger(URLClientIT.class.getName());

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "STARTING TEST : "+testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "FINISHED TEST : "+testInfo.getDisplayName());
  }


  public static String packagePath = URLClientIT.class.getPackageName().replace(".", "/");

  public URLClientIT() throws Exception {
    setup();

    setGeneralURI("/jsp/spec/core_syntax/actions/setproperty");
    setContextRoot("/jsp_coresyntx_act_setproperty_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_coresyntx_act_setproperty_web.war");
    // archive.addClasses(SetpropBean.class, MiscBean.class);
    archive.addPackages(true, Filters.exclude(URLClientIT.class),
            URLClientIT.class.getPackageName());
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_coresyntx_act_setproperty_web.xml"));

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetPropValue.jsp")), "positiveSetPropValue.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetPropReqTimeSingleQuotes.jsp")), "positiveSetPropReqTimeSingleQuotes.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetPropReqTimeDoubleQuotes.jsp")), "positiveSetPropReqTimeDoubleQuotes.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetPropParam.jsp")), "positiveSetPropParam.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetPropNoParam.jsp")), "positiveSetPropNoParam.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetPropAll.jsp")), "positiveSetPropAll.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetLongPrim.jsp")), "positiveSetLongPrim.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetLongObj.jsp")), "positiveSetLongObj.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetIntPrim.jsp")), "positiveSetIntPrim.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetIntObj.jsp")), "positiveSetIntObj.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetIndexedProp.jsp")), "positiveSetIndexedProp.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetFloatPrim.jsp")), "positiveSetFloatPrim.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetFloatObj.jsp")), "positiveSetFloatObj.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDoublePrim.jsp")), "positiveSetDoublePrim.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDoubleObj.jsp")), "positiveSetDoubleObj.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetCharPrim.jsp")), "positiveSetCharPrim.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetCharObj.jsp")), "positiveSetCharObj.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetBytePrim.jsp")), "positiveSetBytePrim.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetByteObj.jsp")), "positiveSetByteObj.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetBooleanPrim.jsp")), "positiveSetBooleanPrim.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetBooleanObj.jsp")), "positiveSetBooleanObj.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveBeanPropertyEditor.jsp")), "positiveBeanPropertyEditor.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/Errorpage.jsp")), "Errorpage.jsp");
    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: positiveSetBooleanObjTest
   * 
   * @assertion_ids: JSP:SPEC:87; JSP:SPEC:162.10
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set a
   * Boolean property of the bean using a String constant. PENDING Merge with
   * prim test
   */

  @Test
  public void positiveSetBooleanObjTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetBooleanObj.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetBooleanObj");
    invoke();
  }

  /*
   * @testName: positiveSetBooleanPrimTest
   * 
   * @assertion_ids: JSP:SPEC:87
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set a
   * boolean property of the bean using a String constant.
   *
   */

  @Test
  public void positiveSetBooleanPrimTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetBooleanPrim.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetBooleanPrim");
    invoke();
  }

  /*
   * @testName: positiveSetByteObjTest
   * 
   * @assertion_ids: JSP:SPEC:88;JSP:SPEC:162.10
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set a
   * Byte property of the bean using a String constant. PENDING Merge with prim
   * test
   */

  @Test
  public void positiveSetByteObjTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetByteObj.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetByteObj");
    invoke();
  }

  /*
   * @testName: positiveSetBytePrimTest
   * 
   * @assertion_ids: JSP:SPEC:88
   * 
   * @test_Strategy: Set a byte property of the bean using a String constant.
   *
   */

  @Test
  public void positiveSetBytePrimTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetBytePrim.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetBytePrim");
    invoke();
  }

  /*
   * @testName: positiveSetCharObjTest
   * 
   * @assertion_ids: JSP:SPEC:89;JSP:SPEC:162.10
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set a
   * Character property of the bean using a String constant. PENDING Merge with
   * prim test
   */

  @Test
  public void positiveSetCharObjTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetCharObj.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetCharObj");
    invoke();
  }

  /*
   * @testName: positiveSetCharPrimTest
   * 
   * @assertion_ids: JSP:SPEC:89
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set a
   * char property of the bean using a String constant.
   *
   */

  @Test
  public void positiveSetCharPrimTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetCharPrim.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetCharPrim");
    invoke();
  }

  /*
   * @testName: positiveSetDoubleObjTest
   * 
   * @assertion_ids: JSP:SPEC:90; JSP:SPEC:162.10
   * 
   * @test_Strategy: and set a Double property of the bean using a String
   * constant. PENDING Merge with prim test
   */

  @Test
  public void positiveSetDoubleObjTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDoubleObj.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetDoubleObj");
    invoke();
  }

  /*
   * @testName: positiveSetDoublePrimTest
   * 
   * @assertion_ids: JSP:SPEC:90
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set a
   * double property of the bean using a String constant.
   *
   */

  @Test
  public void positiveSetDoublePrimTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDoublePrim.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetDoublePrim");
    invoke();
  }

  /*
   * @testName: positiveSetFloatObjTest
   * 
   * @assertion_ids: JSP:SPEC:92;JSP:SPEC:162.10
   * 
   * @test_Strategy: and set a Float property of the bean using a String
   * constant. PENDING Merge with prim test
   */

  @Test
  public void positiveSetFloatObjTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetFloatObj.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetFloatObj");
    invoke();
  }

  /*
   * @testName: positiveSetFloatPrimTest
   * 
   * @assertion_ids: JSP:SPEC:92
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set a
   * float property of the bean using a String constant.
   *
   */

  @Test
  public void positiveSetFloatPrimTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetFloatPrim.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetFloatPrim");
    invoke();
  }

  /*
   * @testName: positiveSetIndexedPropTest
   * 
   * @assertion_ids: JSP:SPEC:162.12
   * 
   * @test_Strategy: Create a bean using useBean tag, use setProperty and set
   * properties using the following array types: <ul> <li> byte <li> char <li>
   * short <li> int <li> float <li> long <li> double <li> boolean <li> Byte <li>
   * Character <li> Short <li> Integer <li> Float <li> Long <li> Double <li>
   * Boolean <ul> Access each of the properties via scripting, iterate through
   * the array, and display the values.
   */

  @Test
  public void positiveSetIndexedPropTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetIndexedProp.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetIndexedProp");
    invoke();
  }

  /*
   * @testName: positiveSetIntObjTest
   * 
   * @assertion_ids: JSP:SPEC:91;JSP:SPEC:162.10
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set an
   * Integer property of the bean using a String constant. PENDING Merge with
   * prim test
   */

  @Test
  public void positiveSetIntObjTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetIntObj.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetIntObj");
    invoke();
  }

  /*
   * @testName: positiveSetIntPrimTest
   * 
   * @assertion_ids: JSP:SPEC:91
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set an
   * int property of the bean using a String constant.
   */

  @Test
  public void positiveSetIntPrimTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetIntPrim.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetIntPrim");
    invoke();
  }

  /*
   * @testName: positiveSetLongObjTest
   * 
   * @assertion_ids: JSP:SPEC:93;JSP:SPEC:162.10
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set an
   * Long property of the bean using a String constant.
   */

  @Test
  public void positiveSetLongObjTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetLongObj.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetLongObj");
    invoke();
  }

  /*
   * @testName: positiveSetLongPrimTest
   * 
   * @assertion_ids: JSP:SPEC:93
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set a
   * long property of the bean using a String constant. PENDING Merge with prim
   * test
   */
  @Test
  public void positiveSetLongPrimTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetLongPrim.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetLongPrim");
    invoke();
  }

  /*
   * @testName: positiveSetPropAllTest
   * 
   * @assertion_ids: JSP:SPEC:162.2.1
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set the
   * property attribute to '*'. The following properties should be set by the
   * tag: name, num, str.
   */

  @Test
  public void positiveSetPropAllTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetPropAll.gf");
    setGoldenFileStream(gfStream);
    String testName = "positiveSetPropAll";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(GOLDENFILE, testName + ".gf");
    TEST_PROPS.setProperty(REQUEST, "GET /jsp_coresyntx_act_setproperty_web/"
        + testName + ".jsp?name=Frodo&num=116165&str=Validated HTTP/1.0");

    invoke();
  }

  /*
   * @testName: positiveSetPropNoParamTest
   * 
   * @assertion_ids: JSP:SPEC:162.4
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance.
   * jsp:setProperty only specifies the name and property properties. The
   * container should set the value of the Bean's property to the value of the
   * request parameter that has the same name as specified by the property
   * attribute.
   */

  @Test
  public void positiveSetPropNoParamTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetPropNoParam.gf");
    setGoldenFileStream(gfStream);
    String testName = "positiveSetPropNoParam";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(GOLDENFILE, testName + ".gf");
    TEST_PROPS.setProperty(REQUEST, "GET /jsp_coresyntx_act_setproperty_web/"
        + testName + ".jsp?str=SAPPOTA HTTP/1.0");

    invoke();
  }

  /*
   * @testName: positiveSetPropParamTest
   * 
   * @assertion_ids: JSP:SPEC:162.3
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance.
   * jsp:setProperty only specifies the param property. The container should set
   * the value of the Bean's property to the value of the request parameter that
   * has the same name as specified by the param attribute.
   */

  @Test
  public void positiveSetPropParamTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetPropParam.gf");
    setGoldenFileStream(gfStream);
    String testName = "positiveSetPropParam";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(GOLDENFILE, testName + ".gf");
    TEST_PROPS.setProperty(REQUEST, "GET /jsp_coresyntx_act_setproperty_web/"
        + testName + ".jsp?Name=MANGO HTTP/1.0");

    invoke();
  }

  /*
   * @testName: positiveSetPropReqTimeSingleQuotesTest
   * 
   * @assertion_ids: JSP:SPEC:162.7.1
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance. Set the
   * value of a bean property using a request-time attribute expression
   * delimited by single quotes. PENDING Merge with
   * positiveSetPropReqTimeDoubleQuotesTest
   */

  @Test
  public void positiveSetPropReqTimeSingleQuotesTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetPropReqTimeSingleQuotes.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetPropReqTimeSingleQuotes");
    invoke();
  }

  /*
   * @testName: positiveSetPropReqTimeDoubleQuotesTest
   * 
   * @assertion_ids: JSP:SPEC:162.7.1
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance. Set the
   * value of a bean property using a request-time attribute expression
   * delimited by double quotes.
   *
   */

  @Test
  public void positiveSetPropReqTimeDoubleQuotesTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetPropReqTimeDoubleQuotes.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetPropReqTimeDoubleQuotes");
    invoke();
  }

  /*
   * @testName: positiveSetPropValueTest
   * 
   * @assertion_ids: JSP:SPEC:162.1; JSP:SPEC:162.2; JSP:SPEC:162.7
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance. Set the
   * value of a bean property using the value attribute.
   */

  @Test
  public void positiveSetPropValueTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetPropValue.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetPropValue");
    invoke();
  }

  /*
   * @testName: positiveBeanPropertyEditorTest
   * 
   * @assertion_ids: JSP:SPEC:86
   * 
   * @test_Strategy: Create a bean using useBean tag, use setProperty and and
   * verfiy results using getProperty.
   */

  @Test
  public void positiveBeanPropertyEditorTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveBeanPropertyEditor.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveBeanPropertyEditor");
    invoke();
  }

}
