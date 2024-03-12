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



package com.sun.ts.tests.jstl.spec.xml.xtransform.transform;

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

  private static String CONTEXT_ROOT = "/jstl_xml_xform_web";

  /** Creates new JSTLClient */
  public JSTLClientIT() {
    setContextRoot(CONTEXT_ROOT);
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_xml_xform_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_xml_xform_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/xfiles/resolve.xml")), "xfiles/resolve.xml");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/xfiles/resolve.xsl")), "xfiles/resolve.xsl");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/import.xml")), "import.xml");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/import.xsl")), "import.xsl");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeTransformXmlXsltNullEmptyTest.jsp")), "negativeTransformXmlXsltNullEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/param.xsl")), "param.xsl");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTransformBodyParamsTest.jsp")), "positiveTransformBodyParamsTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTransformBodyXmlParamsTest.jsp")), "positiveTransformBodyXmlParamsTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTransformDocSystemIdTest.jsp")), "positiveTransformDocSystemIdTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTransformResultTest.jsp")), "positiveTransformResultTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTransformScopeTest.jsp")), "positiveTransformScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTransformVarTest.jsp")), "positiveTransformVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTransformXmlAndXmlSystemIdTest.jsp")), "positiveTransformXmlAndXmlSystemIdTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTransformXmlBodyTest.jsp")), "positiveTransformXmlBodyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTransformXmlInputTest.jsp")), "positiveTransformXmlInputTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTransformXsltInputTest.jsp")), "positiveTransformXsltInputTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTransformXsltSystemIdTest.jsp")), "positiveTransformXsltSystemIdTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/simple.xml")), "simple.xml");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/simple.xsl")), "simple.xsl");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/simple2.xml")), "simple2.xml");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/simple2.xsl")), "simple2.xsl");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/simple3.xsl")), "simple3.xsl");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }


  /*
   * @testName: positiveTransformXmlInputTest
   * 
   * @assertion_ids: JSTL:SPEC:73; JSTL:SPEC:73.2; JSTL:SPEC:73.2.1;
   * JSTL:SPEC:73.2.1.1; JSTL:SPEC:73.2.1.2; JSTL:SPEC:73.2.1.3;
   * JSTL:SPEC:73.2.1.4; JSTL:SPEC:73.2.1.5
   * 
   * @testStrategy: Validate the transform action is able to accept XML input
   * from the following sources: - String - Reader - javax.xml.transform.Source
   * - Objects exported by: + x:parse + x:set + x:transform
   */
  @Test
  public void positiveTransformXmlInputTest() throws Exception {
    String testName = "positiveTransformXmlInputTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Input from String:|"
        + "<h4>|Arbitrary Text|</h4>|" + "<h4>|D Text|</h4>|"
        + "Input from Reader:|" + "<h4>|Arbitrary Text|</h4>|"
        + "<h4>|D Text|</h4>|" + "Input from javax.xml.transform.Source:|"
        + "<h4>|Arbitrary Text|</h4>|" + "<h4>|D Text|</h4>|"
        + "Input from result of x:parse action:|" + "<h4>|Arbitrary Text|</h4>|"
        + "<h4>|D Text|</h4>|" + "Input from result of x:set action:|"
        + "<h4>|D Text|</h4>|" + "Input from x:transform action:|"
        + "<h4>|transform one text|</h4>");
    invoke();
  }

  /*
   * @testName: positiveTransformXsltInputTest
   * 
   * @assertion_ids: JSTL:SPEC:73; JSTL:SPEC:73.4; JSTL:SPEC:73.4.1.1;
   * JSTL:SPEC:73.4.1.2; JSTL:SPEC:73.4.1.3
   * 
   * @testStrategy: Validate the transform action is able to accept XSL input
   * from the following sources: - String - Reader - javax.xml.transform.Source.
   */
  @Test
  public void positiveTransformXsltInputTest() throws Exception {
    String testName = "positiveTransformXsltInputTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "Input from String:|" + "<h4>|Arbitrary Text|</h4>|"
            + "<h4>|D Text|</h4>|" + "Input from Reader:|"
            + "<h4>|Arbitrary Text|</h4>|" + "<h4>|D Text|</h4>|"
            + "Input from javax.xml.transform.Source:|"
            + "<h4>|Arbitrary Text|</h4>|" + "<h4>|D Text|</h4>");
    invoke();
  }

  /*
   * @testName: positiveTransformDocSystemIdTest
   * 
   * @assertion_ids: JSTL:SPEC:73; JSTL:SPEC:73.16; JSTL:SPEC:73.16.1
   * 
   * @testStrategy: Validate the transform action is able to properly resolve
   * external entity references when the docSystemId attribute is set. Validate
   * that the entities can be resolved when parsing both as as a String provided
   * to the xml attribute and as body content to the action.
   */
  @Test
  public void positiveTransformDocSystemIdTest() throws Exception {
    String testName = "positiveTransformDocSystemIdTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<h4>|Entity Resolved|</h4>|<h4>|Entity Resolved|</h4>");
    invoke();
  }

  /*
   * @testName: positiveTransformXsltSystemIdTest
   * 
   * @assertion_ids: JSTL:SPEC:73; JSTL:SPEC:73.3; JSTL:SPEC:73.3.1
   * 
   * @testStrategy: Validate the transform action is able to properly resolve
   * external references within a style sheet.
   */
  @Test
  public void positiveTransformXsltSystemIdTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTransformXsltSystemIdTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveTransformXsltSystemIdTest");
    invoke();
  }

  /*
   * @testName: positiveTransformVarTest
   * 
   * @assertion_ids: JSTL:SPEC:73; JSTL:SPEC:73.5; JSTL:SPEC:73.5.1
   * 
   * @testStrategy: Validate that if var is specified, the variable name
   * reference by var is available in the PageContext and is of type
   * org.w3c.dom.Document.
   */
  @Test
  public void positiveTransformVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTransformVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveTransformVarTest");
    invoke();
  }

  /*
   * @testName: positiveTransformScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:73; JSTL:SPEC:73.6; JSTL:SPEC:73.6.1;
   * JSTL:SPEC:73.6.2; JSTL:SPEC:73.6.3; JSTL:SPEC:73.6.4; JSTL:SPEC:73.8
   * 
   * @testStrategy: Validate that if var is provided and scope is specified,
   * that var is exported to the specified scope. If var is specified and scope
   * is not, validate that var is exported to the page scope by default.
   */
  @Test
  public void positiveTransformScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTransformScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveTransformScopeTest");
    invoke();
  }

  /*
   * @testName: positiveTransformResultTest
   * 
   * @assertion_ids: JSTL:SPEC:73
   * 
   * @testStrategy: Validate that transform action properly handles a case where
   * it is passed a javax.xml.transform.Result. The Result object is in scope
   * before calling the transform action. The Result object is passed to the
   * transform action. The Result object is obtained from scope, and is then
   * manipulated to display the results of the transformation.
   */
  @Test
  public void positiveTransformResultTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTransformResultTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveTransformResultTest");
    invoke();
  }

  /*
   * @testName: positiveTransformXmlBodyTest
   * 
   * @assertion_ids: JSTL:SPEC:73; JSTL:SPEC:73.12
   * 
   * @testStrategy: Validate the transform action is able to parse and then
   * transform an XML document provided as body content to the action.
   */
  @Test
  public void positiveTransformXmlBodyTest() throws Exception {
    String testName = "positiveTransformXmlBodyTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "<h4>|element text|</h4>");
    invoke();
  }

  /*
   * @testName: positiveTransformBodyParamsTest
   * 
   * @assertion_ids: JSTL:SPEC:73; JSTL:SPEC:73.13
   * 
   * @testStrategy: Validate that xsl parameters provided as x:param subtags to
   * the transform action are properly passed to the stylesheet.
   */
  @Test
  public void positiveTransformBodyParamsTest() throws Exception {
    // TEST_PROPS.setProperty(STANDARD, "positiveTransformBodyParamsTest");
    String testName = "positiveTransformBodyParamsTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "15pt|RT Parameter properly passed!");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "REPLACE");
    invoke();
  }

  /*
   * @testName: positiveTransformBodyXmlParamsTest
   * 
   * @assertion_ids: JSTL:SPEC:73; JSTL:SPEC:73.10
   * 
   * @testStrategy: Validate that xsl parameters provided as x:param subtags
   * along with XML provided as body content to the action, that the xml will be
   * parsed and transform with the parameters properly supplied to the
   * stylesheet.
   */
  @Test
  public void positiveTransformBodyXmlParamsTest() throws Exception {
    // TEST_PROPS.setProperty(STANDARD, "positiveTransformBodyXmlParamsTest");
    String testName = "positiveTransformBodyXmlParamsTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "11pt|Paramter properly passed!");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH,
        "THIS SHOULD BE REPLACED");
    invoke();
  }

  /*
   * @testName: negativeTransformXmlXsltNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:73; JSTL:SPEC:73.8
   * 
   * @testStrategy: Validate that if either the xml or xslt attributes are null
   * or empty, that a JspException is thrown.
   */
  @Test
  public void negativeTransformXmlXsltNullEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeTransformXmlXsltNullEmptyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeTransformXmlXsltNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positiveTransformXmlAndXmlSystemIdTest
   * 
   * @assertion_ids: JSTL:SPEC:73
   * 
   * @test_Strategy: Validate that the xml and xmlSystemId attributes can still
   * be used (deprecated and not removed).
   */
  @Test
  public void positiveTransformXmlAndXmlSystemIdTest() throws Exception {
    String testName = "positiveTransformXmlAndXmlSystemIdTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<h4>|Entity Resolved|</h4>|<h4>|Entity Resolved|</h4>");
    invoke();
  }

}
