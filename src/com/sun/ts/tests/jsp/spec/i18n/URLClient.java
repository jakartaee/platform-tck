/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;
import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {
  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setGeneralURI("/jsp/spec/i18n");
    setContextRoot("/jsp_config_encode_web");
    return super.run(args, out, err);
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
  public void i18nStandardPageResponseEncodingTest() throws Fault {
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
  public void i18nXmlPageResponseEncodingTest() throws Fault {
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
  public void i18nEncodingMismatchTest() throws Fault {
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
  public void i18nUnsupportedEncodingTest() throws Fault {
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
  public void i18nIncludedContentTest() throws Fault {
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
  public void i18nBomByteSequencesUTF8Test() throws Fault {

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
  public void i18nBomByteSequencesUTF16BETest() throws Fault {

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
  public void i18nBomByteSequencesUTF16LETest() throws Fault {

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
  public void i18nBomLegalCharacterEncodingTest1() throws Fault {

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
  public void i18nBomLegalCharacterEncodingTest2() throws Fault {

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
  public void i18nBomErrorReportingTest1() throws Fault {

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
  public void i18nBomErrorReportingTest2() throws Fault {

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
  public void i18nBomErrorReportingTest3() throws Fault {

    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_i18n_web/I18NBadTagPageEncoding.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
