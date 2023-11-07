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

/*
 * @(#)URLClient.java	1.36 02/11/04
 */

package com.sun.ts.tests.jsp.spec.i18n;


import java.io.IOException;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {


  public URLClientIT() throws Exception {
    setup();

    setGeneralURI("/jsp/spec/i18n");
    setContextRoot("/jsp_config_encode_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_i18n_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_i18n_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/BadTagPageEncoding.tag", "tags/BadTagPageEncoding.tag");    

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/inclusion_utf-16LE.jspx")), "inclusion_utf-16LE.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/inclusion_iso.txt")), "inclusion_iso.txt");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/I18NXmlPrologNonMatchingPropertyGroupTest.jspx")), "I18NXmlPrologNonMatchingPropertyGroupTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/I18NXmlPrologNonMatchingDirectiveTest.jspx")), "I18NXmlPrologNonMatchingDirectiveTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/I18NUnsupportedEncodingTest.jspx")), "I18NUnsupportedEncodingTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/I18NUnsupportedEncodingTest.jsp")), "I18NUnsupportedEncodingTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/I18NPageEncTest.jsp")), "I18NPageEncTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/I18NPageEncContTypeTest.jsp")), "I18NPageEncContTypeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/I18NNoPageEncNoContTypeTest.jsp")), "I18NNoPageEncNoContTypeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/I18NIncludedContentTest.jspx")), "I18NIncludedContentTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/I18NIncludedContentTest.jsp")), "I18NIncludedContentTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/I18NContTypeTest.jsp")), "I18NContTypeTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/I18NBadTagPageEncoding.jsp")), "I18NBadTagPageEncoding.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/prolog/I18NPageEncTest.jspx")), "prolog/I18NPageEncTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/prolog/I18NPageEncContTypeTest.jspx")), "prolog/I18NPageEncContTypeTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/prolog/I18NNoPageEncNoContTypeTest.jspx")), "prolog/I18NNoPageEncNoContTypeTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/prolog/I18NContTypeTest.jspx")), "prolog/I18NContTypeTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/noprolog/I18NPageEncTest.jspx")), "noprolog/I18NPageEncTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/noprolog/I18NPageEncContTypeTest.jspx")), "noprolog/I18NPageEncContTypeTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/noprolog/I18NNoPageEncNoContTypeTest.jspx")), "noprolog/I18NNoPageEncNoContTypeTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/noprolog/I18NContTypeTest.jspx")), "noprolog/I18NContTypeTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/bom/UTF16PageEncodingAttribute.jsp")), "bom/UTF16PageEncodingAttribute.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/bom/UTF16LE.jsp")), "bom/UTF16LE.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/bom/UTF16ConfigElement.jsp")), "bom/UTF16ConfigElement.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/bom/UTF16BE.jsp")), "bom/UTF16BE.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/bom/UTF16BadPageEncodingAttribute.jsp")), "bom/UTF16BadPageEncodingAttribute.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/bom/UTF8ConfigElement.jsp")), "bom/UTF8ConfigElement.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/bom/UTF8.jsp")), "bom/UTF8.jsp");
    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: i18nStandardPageResponseEncodingTest
   * 
   * @assertion_ids: JSP:SPEC:249.4;JSP:SPEC:250.3;JSP:SPEC:250.6.2;
   * JSP:SPEC:250.2;JSP:SPEC:249.2;JSP:SPEC:250.6.1;
   * JSP:SPEC:249.3;JSP:SPEC:250.1;JSP:SPEC:250.4
   * 
   * @test_Strategy: Request 1: Using a page encoded in ISO-8859-1, with no page
   * directive including verify the page is is properly loaded and the resulting
   * output content type is the default of text/html with the charset parameter
   * set to ISO-8859-1. Request 2: Using a page encoded in ISO-8859-15, with a
   * page directive specifying only the pageEncoding (set to ISO-8859-15),
   * validate the page is properly loaded and the resulting output content is
   * the default of text/html with a charset of ISO-8859-15. Request 3: Using a
   * page encoded in ISO-8859-15, with a page directive specifying only a
   * contentType (set to text/plain charset=ISO-8859-15), validate the page is
   * properly loaded and the resulting output content is the default of
   * text/plain with a charset of ISO-8859-15.
   *
   * Request 4: Using a page encoded in ISO-8859-15, with a page directive
   * specifying both a contentType ( text/plain; charset=ISO-8859-1) and a
   * pageEncoding of ISO-8859-15, the page is properly loaded and the resulting
   * output content is set to text/plain with a charset of ISO-8859-1.
   *
   * CHALLENGES: This test has been challenged in the mistaken belief that, in
   * the case where the page encoding is ISO-8859-1, the content type need not
   * specify a charset parameter. In support of this position, the challenger
   * cited JSP 2.0, section 4.2, which states:
   *
   * "For JSP pages in standard syntax, it is the character encoding specified
   * by the pageEncoding attribute of the page directive or by a JSP
   * configuration element page-encoding whose URL pattern matches the page.
   * Only the character encoding specified for the requested page is used; the
   * encodings of files included via the include directive are not taken into
   * consideration. If there's no such specification, no initial response
   * character encoding is passed to ServletResponse.setContentType() - the
   * ServletResponse object's default, ISO-8859-1, is used."
   * 
   * However, this section omits the fact that if the default encoding is used,
   * it will be added to the response content type as soon as
   * ServletResponse.getWriter() is called. See the javadoc for
   * ServletResponse.setContentType() and ServletResponse.getWriter().
   */
  @Test
  public void i18nStandardPageResponseEncodingTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/I18NNoPageEncNoContTypeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type:text/html;charset=ISO-8859-1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "${testPassed}");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/I18NPageEncTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type:text/html;charset=ISO-8859-15");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "${testPassed}");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/I18NContTypeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type:text/plain;charset=ISO-8859-15");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "${testPassed}");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/I18NPageEncContTypeTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type:text/plain;charset=ISO-8859-1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "${testPassed}");
    invoke();
  }

  /*
   * @testName: i18nXmlPageResponseEncodingTest
   * 
   * @assertion_ids: JSP:SPEC:249.5;JSP:SPEC:249.6;JSP:SPEC:250.5;
   * JSP:SPEC:250.3;JSP:SPEC:250.1;JSP:SPEC:250.4
   * 
   * @test_Strategy: This test will exercise JSP documents with and without a
   * prolog with an encoding attribute. This will be performed over several
   * requests. This test will also validate that items such as charset
   * parameters present in a contentType attribute don't cause problems with the
   * encodings. Request 1: Validate that a page encoded in EBCDIC (cp500, or
   * IBM500) by a directive) where the resulting output content is set to
   * text/xml with a charset of UTF-8. Request 2: Validate that a page encoded
   * in cp871 (or IBM871) can properly be loaded when both the prolog and the
   * pageEncoding attribute of the page directive specify the same value. The
   * resulting output should have a content of text/xml and a charset of UTF-8.
   * Request 3: Validate that a page encoded in cp871 (or IBM871) can properly
   * be loaded when the encoding is specified by the prolog. The resulting
   * content type should be what is specified by the contentType attribute of
   * the page directive, which in this case is text/plain with a charset of
   * ISO-8859-1 Request 4: Validate that a page encoded in UTF-16 can properly
   * be loaded when the encoding is specified by the prolog as well as the
   * pageEncoding attribute of the page directive. Since the contentType
   * attribute is also present the resulting output should have a content type
   * of text/plain with a charset of ISO-8859-1. Request 5: Validate that if the
   * JSP document (encoding in UTF-16) has a prolog with no encoding attribute,
   * that the container is able to properly load the document using the encoding
   * detection semantics specified by the XML 1.0 specification. In this case
   * the JSP document has no pageEncoding or contentType attributes and is
   * encoded in UTF-16. The container should be able to load and parse the page
   * without issue. The response content type should be text/xml with a charset
   * of UTF-8. Request 6: Validate that a JSP document (encoding in UTF-16BE)
   * has a prolog with no encoding attribute, that the container is able to
   * properly load the document using the encoding detection semantics specified
   * by the XML 1.0 specification. In this case the JSP document has only a
   * pageEncoding attribute defined. The container should be able to load and
   * parse the page without issue. The response content type should be text/xml
   * with a charset of UTF-8. Request 7: Validate that a JSP document (encoded
   * in UTF-16LE) has a prolog with no encoding attribute, that the container is
   * able to properly load the document using the encoding detection semantics
   * specified by the XML 1.0 specification. In this case the JSP document has
   * only a contentType attribute defined. The container should be able to load
   * and parse the page without issue. The response content type should be based
   * off the contentType attribute: text/plain with a charset of ISO-8859-1.
   * Request 8: Validate that a JSP document (encoded in UTF-16) has a prolog
   * with no encoding attribute, that the container is able to properly load the
   * document using the encoding detection semantics specified by the XML 1.0
   * specification. In this case the JSP document has both contentType and
   * pageEncoding attributes defined. The container should be able to load and
   * parse the page without issue. The response content type should be based off
   * the contentType attribute: text/plain with a charset of ISO-8859-1.
   */
  @Test
  public void i18nXmlPageResponseEncodingTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/prolog/I18NNoPageEncNoContTypeTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type: text/xml; charset=UTF-8");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/prolog/I18NPageEncTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type: text/xml; charset=UTF-8");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/prolog/I18NContTypeTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type: text/plain; charset=ISO-8859-1");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/prolog/I18NPageEncContTypeTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type: text/plain; charset=ISO-8859-1");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/noprolog/I18NNoPageEncNoContTypeTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type: text/xml; charset=UTF-8");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/noprolog/I18NPageEncTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type: text/xml; charset=UTF-8");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/noprolog/I18NContTypeTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type: text/plain; charset=ISO-8859-1");
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/noprolog/I18NPageEncContTypeTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type: text/plain; charset=ISO-8859-1");
    invoke();
  }

  /*
   * @testName: i18nEncodingMismatchTest
   * 
   * @assertion_ids: JSP:SPEC:249.7
   * 
   * @test_Strategy: Validate that if a JSP Document specifies an encoding in
   * the XML prolog and a JSP page directive specifies a different encoding, a
   * translation error occurs. <em>NOTE: </em> The similiar translation-time
   * error with a mismatched encoding between property groups and pages in
   * standard syntax are covered in the spec/configuration test area.
   */
  @Test
  public void i18nEncodingMismatchTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/I18NXmlPrologNonMatchingDirectiveTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();

    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/I18NXmlPrologNonMatchingPropertyGroupTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: i18nUnsupportedEncodingTest
   * 
   * @assertion_ids: JSP:SPEC:249.8
   * 
   * @test_Strategy: Validate a translation error occurs if a standard JSP
   * specifies an invalid encoding in the page directive, or if a JSP document
   * has an invalid encoding in the prolog.
   */
  @Test
  public void i18nUnsupportedEncodingTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/I18NUnsupportedEncodingTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/I18NUnsupportedEncodingTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: i18nIncludedContentTest
   * 
   * @assertion_ids: JSP:SPEC:249.5
   * 
   * @test_Strategy: Validate that units that make up a single translation unit
   * all have their file encoding detected individually. In this case a JSP
   * Document encoded in UTF-16BE using the include directive includes another
   * JSP Document encoded in UTF-16LE.
   */
  @Test
  public void i18nIncludedContentTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/I18NIncludedContentTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Included Content|Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/I18NIncludedContentTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Included Content|Test PASSED");
    invoke();
  }

  /*
   * @testName: i18nBomByteSequencesUTF8Test
   * 
   * @assertion_ids: JSP:SPEC:299
   * 
   * @test_Strategy: [BOMCharacterEncodingDescription] Verify that the character
   * encoding of pages in standard syntax is determined by a BOM, and that the
   * byte sequences reserved to identify a BOM at the beginning of a page do not
   * appear in the page's output.
   */
  @Test
  public void i18nBomByteSequencesUTF8Test() throws Exception {

    String searchStr = "this is a test";
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type:text/html;charset=UTF-8");
    TEST_PROPS.setProperty(SEARCH_STRING, searchStr);
    TEST_PROPS.setProperty(REQUEST, "GET /jsp_i18n_web/bom/UTF8.jsp HTTP/1.1");
    invoke();
  }

  /*
   * @testName: i18nBomByteSequencesUTF16BETest
   * 
   * @assertion_ids: JSP:SPEC:299
   * 
   * @test_Strategy: [BOMCharacterEncodingDescription] Verify that the character
   * encoding of pages in standard syntax is determined by a BOM, and that the
   * byte sequences reserved to identify a BOM at the beginning of a page do not
   * appear in the page's output.
   */
  @Test
  public void i18nBomByteSequencesUTF16BETest() throws Exception {

    String searchStr = "this is a test";
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type:text/html;charset=UTF-16BE");
    TEST_PROPS.setProperty(SEARCH_STRING, searchStr);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/bom/UTF16BE.jsp HTTP/1.1");
    invoke();
  }

  /*
   * @testName: i18nBomByteSequencesUTF16LETest
   * 
   * @assertion_ids: JSP:SPEC:299
   * 
   * @test_Strategy: [BOMCharacterEncodingDescription] Verify that the character
   * encoding of pages in standard syntax is determined by a BOM, and that the
   * byte sequences reserved to identify a BOM at the beginning of a page do not
   * appear in the page's output.
   */
  @Test
  public void i18nBomByteSequencesUTF16LETest() throws Exception {

    String searchStr = "this is a test";
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type:text/html;charset=UTF-16LE");
    TEST_PROPS.setProperty(SEARCH_STRING, searchStr);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/bom/UTF16LE.jsp HTTP/1.1");
    invoke();
  }

  /*
   * @testName: i18nBomLegalCharacterEncodingTest1
   * 
   * @assertion_ids: JSP:SPEC:300
   * 
   * @test_Strategy: [BOMLegalCharacterEncoding] Verify that when using a BOM,
   * it is legal to describe the character encoding in a JSP page-encoding
   * configuration element.
   */
  @Test
  public void i18nBomLegalCharacterEncodingTest1() throws Exception {

    String searchStr = "this is a test";
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type:text/html;charset=UTF-16BE");
    TEST_PROPS.setProperty(SEARCH_STRING, searchStr);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/bom/UTF16ConfigElement.jsp HTTP/1.1");
    invoke();
  }

  /*
   * @testName: i18nBomLegalCharacterEncodingTest2
   * 
   * @assertion_ids: JSP:SPEC:300
   * 
   * @test_Strategy: [BOMLegalCharacterEncoding] Verify that when using a BOM,
   * it is legal to describe the character encoding in the pageEncoding
   * attribute of a page directive.
   */
  @Test
  public void i18nBomLegalCharacterEncodingTest2() throws Exception {

    String searchStr = "this is a test";
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Content-Type:text/html;charset=UTF-16LE");
    TEST_PROPS.setProperty(SEARCH_STRING, searchStr);
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/bom/UTF16PageEncodingAttribute.jsp HTTP/1.1");
    invoke();
  }

  /*
   * @testName: i18nBomErrorReportingTest1
   * 
   * @assertion_ids: JSP:SPEC:318
   * 
   * @test_Strategy: [BOMErrorReporting] Verify that when using a BOM, an error
   * results when the encoding specified by page-encoding configuration element
   * whose URL matches the page does not match the encoding indicated by the
   * BOM.
   */
  @Test
  public void i18nBomErrorReportingTest1() throws Exception {

    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/bom/UTF8ConfigElement.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: i18nBomErrorReportingTest2
   * 
   * @assertion_ids: JSP:SPEC:318
   * 
   * @test_Strategy: [BOMErrorReporting] Verify that when using a BOM, an error
   * results when the encoding specified by the pageEncoding attribute of a page
   * directive does not match the encoding indicated by the BOM.
   */
  @Test
  public void i18nBomErrorReportingTest2() throws Exception {

    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/bom/UTF16BadPageEncodingAttribute.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: i18nBomErrorReportingTest3
   * 
   * @assertion_ids: JSP:SPEC:318
   * 
   * @test_Strategy: [BOMErrorReporting] Verify that when using a BOM, an error
   * results when the encoding specified by the pageEncoding attribute of a tag
   * directive does not match the encoding indicated by the BOM.
   */
  @Test
  public void i18nBomErrorReportingTest3() throws Exception {

    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/I18NBadTagPageEncoding.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
