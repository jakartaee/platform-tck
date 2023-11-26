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



package com.sun.ts.tests.jstl.spec.core.urlresource.param;

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
    setContextRoot("/jstl_core_url_param_web");
  }


  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_core_url_param_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_core_url_param_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/import.jsp")), "import.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeParamExcBodyContentTest.jsp")), "negativeParamExcBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamAggregationTest.jsp")), "positiveParamAggregationTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamBodyValueTest.jsp")), "positiveParamBodyValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamEncodingTest.jsp")), "positiveParamEncodingTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamNameNullEmptyTest.jsp")), "positiveParamNameNullEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamNameValueTest.jsp")), "positiveParamNameValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamValueNullTest.jsp")), "positiveParamValueNullTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveParamNameValueTest
   * 
   * @assertion_ids: JSTL:SPEC:25; JSTL:SPEC:25.1; JSTL:SPEC:25.1.1;
   * JSTL:SPEC:25.2; JSTL:SPEC:25.2.1
   * 
   * @testStrategy: Validate the the name and value attributes can accept both
   * dynamic and static values.
   */
  @Test
  public void positiveParamNameValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamNameValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParamNameValueTest");
    invoke();
  }

  /*
   * @testName: positiveParamEncodingTest
   * 
   * @assertion_ids: JSTL:SPEC:25.4
   * 
   * @testStrategy: Validate that the action properly encodes parameter names
   * and values if it contains characters that require URL encoding.
   */
  @Test
  public void positiveParamEncodingTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamEncodingTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParamEncodingTest");
    invoke();
  }

  /*
   * @testName: positiveParamBodyValueTest
   * 
   * @assertion_ids: JSTL:SPEC:25.5
   * 
   * @testStrategy: Validate the the param tag can accept a parameter value as
   * body content.
   */
  @Test
  public void positiveParamBodyValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamBodyValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParamBodyValueTest");
    invoke();
  }

  /*
   * @testName: positiveParamAggregationTest
   * 
   * @assertion_ids: JSTL:SPEC:25.7.1
   * 
   * @testStrategy: Validate that request parameters are properly aggregated if
   * the parent action's URL contains the same parameter name as the name
   * specified in the param tag. The value in the param tag should take
   * precedence.
   */
  @Test
  public void positiveParamAggregationTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamAggregationTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParamAggregationTest");
    invoke();
  }

  /*
   * @testName: positiveParamNameNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:25.8
   * 
   * @testStrategy: Validate that if name is null or empty, that the action is
   * effectively ignored.
   */
  @Test
  public void positiveParamNameNullEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamNameNullEmptyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParamNameNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positiveParamValueNullTest
   * 
   * @assertion_ids: JSTL:SPEC:25.9
   * 
   * @testStrategy: Validate that if value is null, that the value of the param
   * is an empty string ("").
   */
  @Test
  public void positiveParamValueNullTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamValueNullTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParamValueNullTest");
    invoke();
  }

  /*
   * @testName: negativeParamExcBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:25.10
   * 
   * @testStrategy: Validate that if the body content of the action causes an
   * exception, that it is properly propagated.
   */
  @Test
  public void negativeParamExcBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeParamExcBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeParamExcBodyContentTest");
    invoke();
  }
}
