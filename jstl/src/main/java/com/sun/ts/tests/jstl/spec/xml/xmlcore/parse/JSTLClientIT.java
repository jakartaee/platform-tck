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



package com.sun.ts.tests.jstl.spec.xml.xmlcore.parse;

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
    setContextRoot("/jstl_xml_parse_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_xml_parse_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_xml_parse_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/xfiles/resolve.xml")), "xfiles/resolve.xml");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/import.xml")), "import.xml");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeParseDocNullEmptyTest.jsp")), "negativeParseDocNullEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeParseInvalidScopeTest.jsp")), "negativeParseInvalidScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeParseScopeVarDomSyntaxTest.jsp")), "negativeParseScopeVarDomSyntaxTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeParseScopeVarSyntaxTest.jsp")), "negativeParseScopeVarSyntaxTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParseFilterNullTest.jsp")), "positiveParseFilterNullTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParseFilterTest.jsp")), "positiveParseFilterTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParseNoDTDValidationTest.jsp")), "positiveParseNoDTDValidationTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParseScopeDomTest.jsp")), "positiveParseScopeDomTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParseScopeTest.jsp")), "positiveParseScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParseSystemIdTest.jsp")), "positiveParseSystemIdTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParseVarDomTest.jsp")), "positiveParseVarDomTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParseVarTest.jsp")), "positiveParseVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParseXmlAttrTest.jsp")), "positiveParseXmlAttrTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParseXmlBodyTest.jsp")), "positiveParseXmlBodyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParseXmlInputTest.jsp")), "positiveParseXmlInputTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }


  /*
   * @testName: positiveParseXmlInputTest
   * 
   * @assertion_ids: JSTL:SPEC:67; JSTL:SPEC:67.3
   * 
   * @testStrategy: Validate that parse action is able to parse various input
   * sources as provided by the xml attribute: - Strings - Readers - Instances
   * of javax.xml.transform.Source - Objects exported by: + x:parse + x:set +
   * x:transform No validation will be performed against the result. The test
   * will be considered a success if no parse exceptions are thrown.
   */
  @Test
  public void positiveParseXmlInputTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParseXmlInputTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParseXmlInputTest");
    invoke();
  }

  /*
   * @testName: positiveParseFilterTest
   * 
   * @assertion_ids: JSTL:SPEC:67; JSTL:SPEC:67.3
   * 
   * @testStrategy: Validate that if an instance of org.xml.sax.XMLFilter is
   * provided to the filter attribute, that it is properly applied by the
   * action. This will be verified using a simple XML filter that will add an
   * attribute ('test') to the provided elements. XPath will be used to verify
   * that the attribute exists.
   */
  @Test
  public void positiveParseFilterTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParseFilterTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParseFilterTest");
    invoke();
  }

  /*
   * @testName: positiveParseVarTest
   * 
   * @assertion_ids: JSTL:SPEC:67; JSTL:SPEC:67.4; JSTL:SPEC:67.4.1
   * 
   * @testStrategy: Validate that if the parse operation is successfull, and var
   * is specified, the result is available via the PageContext and is of type
   * java.lang.Object.
   */
  @Test
  public void positiveParseVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParseVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParseVarTest");
    invoke();
  }

  /*
   * @testName: positiveParseVarDomTest
   * 
   * @assertion_ids: JSTL:SPEC:67; JSTL:SPEC:67.6; JSTL:SPEC:67.6.1
   * 
   * @testStrategy: Validate that if the parse operation is successfull, and
   * varDom is specified, the result is available via the PageContext and is of
   * type org.w3c.dom.Document.
   */
  @Test
  public void positiveParseVarDomTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParseVarDomTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParseVarDomTest");
    invoke();
  }

  /*
   * @testName: positiveParseScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:67; JSTL:SPEC:67.5; JSTL:SPEC:67.5.1;
   * JSTL:SPEC:67.5.2; JSTL:SPEC:67.5.3; JSTL:SPEC:67.5.4; JSTL:SPEC:67.11
   * 
   * @testStrategy: Validate that the presence of the 'scope' property exports
   * var to the specified scope. If var is provided, but scope is not, verify
   * that var is exported to the page scope by default.
   */
  @Test
  public void positiveParseScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParseScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParseScopeTest");
    invoke();
  }

  /*
   * @testName: positiveParseScopeDomTest
   * 
   * @assertion_ids: JSTL:SPEC:67; JSTL:SPEC:67.7; JSTL:SPEC:67.7.1;
   * JSTL:SPEC:67.7.2; JSTL:SPEC:67.7.4; JSTL:SPEC:67.7.4; JSTL:SPEC:67.12
   * 
   * @testStrategy: Validate that the presence of the 'scopeDom' property
   * exports varDom to the specified scope. If varDom is provided, but scopeDom
   * is not, verify that varDom is exported to the page scope by default.
   */
  @Test
  public void positiveParseScopeDomTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParseScopeDomTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParseScopeDomTest");
    invoke();
  }

  /*
   * @testName: positiveParseXmlBodyTest
   * 
   * @assertion_ids: JSTL:SPEC:67; JSTL:SPEC:67.14
   * 
   * @testStrategy: Validate that parse action can properly parse XML provided
   * as body content to the action. No exception should occur, and the result
   * should be available via an XPath expression.
   */
  @Test
  public void positiveParseXmlBodyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParseXmlBodyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParseXmlBodyTest");
    invoke();
  }

  /*
   * @testName: positiveParseFilterNullTest
   * 
   * @assertion_ids: JSTL:SPEC:67; JSTL:SPEC:67.3; JSTL:SPEC:67.3.3
   * 
   * @testStrategy: Validate that if the 'filter' attribute is null, no
   * filtering takes place and the parse operation proceed normally.
   */
  @Test
  public void positiveParseFilterNullTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParseFilterNullTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParseFilterNullTest");
    invoke();
  }

  /*
   * @testName: positiveParseSystemIdTest
   * 
   * @assertion_ids: JSTL:SPEC:67; JSTL:SPEC:67.1; JSTL:SPEC:67.1.1
   * 
   * @testStrategy: Validate that when the SystemID attribute is used, it's able
   * to resolve external Entities referenced by the provided XML document.
   */
  @Test
  public void positiveParseSystemIdTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParseSystemIdTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParseSystemIdTest");
    invoke();
  }

  /*
   * @testName: positiveParseNoDTDValidationTest
   * 
   * @assertion_ids: JSTL:SPEC:67; JSTL:SPEC:67.1; JSTL:SPEC:67.1.1
   * 
   * @testStrategy: Validate that no DTD validation is performed against a
   * document provided with a DTD. Confirm by providing an XML document that
   * goes against the provided DTD. No parsing exception should occur.
   */
  @Test
  public void positiveParseNoDTDValidationTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParseNoDTDValidationTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveParseNoDTDValidationTest");
    invoke();
  }

  /*
   * @testName: negativeParseScopeVarSyntaxTest
   * 
   * @assertion_ids: JSTL:SPEC:67; JSTL:SPEC:67.5; JSTL:SPEC:67.5.5
   * 
   * @testStrategy: Validate that if scope is specified, but var is not, that a
   * fatal translation error occurs.
   */
  @Test
  public void negativeParseScopeVarSyntaxTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeParseScopeVarSyntaxTest");
    TEST_PROPS.setProperty(REQUEST, "negativeParseScopeVarSyntaxTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeParseScopeVarDomSyntaxTest
   * 
   * @assertion_ids: JSTL:SPEC:67; JSTL:SPEC:67.7; JSTL:SPEC:67.7.5
   * 
   * @testStrategy: Validate that if scopeDom is specified, but varDom is not,
   * that a fatal translation error occurs.
   */
  @Test
  public void negativeParseScopeVarDomSyntaxTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeParseScopeVarDomSyntaxTest");
    TEST_PROPS.setProperty(REQUEST, "negativeParseScopeVarSyntaxTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeParseInvalidScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:67; JSTL:SPEC:67.16
   * 
   * @testStrategy: Validate that a fatal translation error occurs if the scope
   * attribute is provided an invalid value.
   */
  @Test
  public void negativeParseInvalidScopeTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeParseInvalidScopeTest");
    TEST_PROPS.setProperty(REQUEST, "negativeParseInvalidScopeTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeParseDocNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:67; JSTL:SPEC:67.19.2
   * 
   * @testStrategy: Validate that if doc is null or empty, that an instance of
   * jakarta.servlet.jsp.JspException is thrown.
   */
  @Test
  public void negativeParseDocNullEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeParseDocNullEmptyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeParseDocNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positiveParseXmlAttrTest
   * 
   * @assertion_ids: JSTL:SPEC:67
   * 
   * @test_Strategy: Validate that the xml attribute is still available for use
   * (deprecated and not removed).
   */
  @Test
  public void positiveParseXmlAttrTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParseXmlAttrTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_xml_parse_web/positiveParseXmlAttrTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
