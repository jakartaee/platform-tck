/*
 * Copyright (c) 2007 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jstl.compat.onedotzero;

import java.io.IOException;
import java.io.InputStream;

import com.sun.ts.tests.jstl.common.client.CompatAbstractUrlClient;

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
public class JSTLClientIT extends CompatAbstractUrlClient {

  public static String packagePath = JSTLClientIT.class.getPackageName().replace(".", "/");

  /** Creates new JSTLClient */
  public JSTLClientIT() {
    setGeneralURI("/jstl/compat/onedotzero");
    setContextRoot("/jstl_1_0_compat_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_1_0_compat_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_1_0_compat_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/import.jsp")), "import.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/import.txt")), "import.txt");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeScriptFreeTlvNoDeclTest.jsp")), "negativeScriptFreeTlvNoDeclTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/param.xsl")), "param.xsl");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveBundleLocalizationScopeTest.jsp")), "positiveBundleLocalizationScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveCatchVarTest.jsp")), "positiveCatchVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveCWOTest.jsp")), "positiveCWOTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDateParamQueryTimestampTest.jsp")), "positiveDateParamQueryTimestampTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDefaultEncodingTest.jsp")), "positiveDefaultEncodingTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFDValueTest.jsp")), "positiveFDValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNScopeTest.jsp")), "positiveFNScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFormatLocalizationContextI18NTest.jsp")), "positiveFormatLocalizationContextI18NTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFormatLocalizationContextI18NTestJava20Plus.jsp")), "positiveFormatLocalizationContextI18NTestJava20Plus.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveForTokensTest.jsp")), "positiveForTokensTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveIfScopeTest.jsp")), "positiveIfScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveImportAbsUrlTest.jsp")), "positiveImportAbsUrlTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveItemsObjArrayTest.jsp")), "positiveItemsObjArrayTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveJSTLURITest.jsp")), "positiveJSTLURITest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMessageKeyTest.jsp")), "positiveMessageKeyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveOutEscXmlTest.jsp")), "positiveOutEscXmlTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamNameValueTest.jsp")), "positiveParamNameValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamValueTest.jsp")), "positiveParamValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParseVarTest.jsp")), "positiveParseVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDValueTest.jsp")), "positivePDValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePermittedTlvTest.jsp")), "positivePermittedTlvTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePNValueTest.jsp")), "positivePNValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveQueryScopeAttributeTest.jsp")), "positiveQueryScopeAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveRedirectTest.jsp")), "positiveRedirectTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveRemoveScopeVarTest.jsp")), "positiveRemoveScopeVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveResultGetRowsCountTest.jsp")), "positiveResultGetRowsCountTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetBundleScopeVarTest.jsp")), "positiveSetBundleScopeVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetDataSourceScopeAttributeTest.jsp")), "positiveSetDataSourceScopeAttributeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetLocaleValueTest.jsp")), "positiveSetLocaleValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetScopeTest.jsp")), "positiveSetScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetSelectVarTest.jsp")), "positiveSetSelectVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetTimezoneValueTest.jsp")), "positiveSetTimezoneValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTimezoneValueTest.jsp")), "positiveTimezoneValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTransformVarTest.jsp")), "positiveTransformVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTxQueryCommitTest.jsp")), "positiveTxQueryCommitTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUpdateBodyContentTest.jsp")), "positiveUpdateBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUrlScopeTest.jsp")), "positiveUrlScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveXParamNameTest.jsp")), "positiveXParamNameTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/simple2.xml")), "simple2.xml");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/simple2.xsl")), "simple2.xsl");

    archive.addAsWebInfResource(JSTLClientIT.class.getPackage(), "tssql.stmt", "jstl-sql.properties");    

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }



  /*
   * @testName: positiveBundleLocalizationScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:106.3.1
   * 
   * @testStrategy: Validate that if a LocalizationContext was established via a
   * bundle action, that any messages within the body of the bundle action are
   * properly localized and any message actions outside the body are not.
   */
  @Test
  public void positiveBundleLocalizationScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveBundleLocalizationScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveBundleLocalizationScopeTest");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_1_0_compat_web/positiveBundleLocalizationScopeTest.jsp HTTP/1.1");
    // TEST_PROPS.setProperty(GOLDENFILE,
    //     "positiveBundleLocalizationScopeTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveCatchVarTest
   * 
   * @assertion_ids: JSTL:SPEC:42.1
   * 
   * @testStrategy: Validate that the catch action properly stores the Throable
   * into the variable name designated by the var attribute and validate the
   * type of var as it should be the type of the Throwable.
   */
  @Test
  public void positiveCatchVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveCatchVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveCatchVarTest");
    invoke();
  }

  /*
   * @testName: positiveCWOTest
   * 
   * @assertion_ids: JSTL:SPEC:15.1; JSTL:SPEC:15.1.1; JSTL:SPEC:15.1.1.1;
   * JSTL:SPEC:15.1.1.2; JSTL:SPEC:15.2.1.1; JSTL:SPEC:15.2.1
   * 
   * @testStrategy: Validate the behavior/interaction of 'choose', 'when'
   * 'otherwise' actions. - single 'when' action evaluating to true - one 'when'
   * action evaluating to false and two 'when' actions evaluating to true. Only
   * the first when that evaluates to true should process it's body content -
   * single 'when' evaluating to false which should cause the 'otherwise' action
   * to process its body
   */
  @Test
  public void positiveCWOTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveCWOTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveCWOTest");
    invoke();
  }

  /*
   * @testName: positiveDateParamQueryTimestampTest
   * 
   * @assertion_ids: JSTL:SPEC:94.2
   * 
   * @testStrategy: Validate the behavior of the sql:query action - That a query
   * can be executed using sql:dateParam action specifying a type of 'timestamp'
   * - Validate that you get the expected number of rows back.
   */
  @Test
  public void positiveDateParamQueryTimestampTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveDateParamQueryTimestampTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT,
        "positiveDateParamQueryTimestampTest");
    invoke();
  }

  /*
   * @testName: positiveDefaultEncodingTest
   * 
   * @assertion_ids: JSTL:SPEC:45.4.2
   * 
   * @testStrategy: Validate that if no Content-Type header is sent by the
   * client, and the scoped variable,
   * jakarta.servlet.jsp.jstl.fmt.request.charset, is not present, the default
   * encoding of ISO-8859-1 is used. Validation will be by calling
   * getCharacterEncoding() against the request object after the action has been
   * called.
   */
  @Test
  public void positiveDefaultEncodingTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveDefaultEncodingTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveDefaultEncodingTest");
    invoke();
  }

  /*
   * @testName: positiveFDValueTest
   * 
   * @assertion_ids: JSTL:SPEC:48; JSTL:SPEC:57
   * 
   * @testStrategy: Validate that formatDate action can properly format dates
   * provided as String literals or as java.util.Date objects.
   */
  @Test
  public void positiveFDValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFDValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveFDValueTest");
    invoke();
  }

  /*
   * @testName: positiveFNScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:58.9.1
   * 
   * @testStrategy: Validate that the action exports var to the specified scope
   * as well as validating that if scope is not specified, var will be exported
   * to the page scope by default.
   */
  @Test
  public void positiveFNScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveFNScopeTest");
    invoke();
  }

  /*
   * @testName: positiveFormatLocalizationContextI18NTest
   * 
   * @assertion_ids: JSTL:SPEC:46.3
   * 
   * @testStrategy: If the jakarta.servlet.jsp.jstl.fmt.localizationContext
   * attribute is present, and the formatting action is not nested in a
   * <fmt:bundle> action, the base name attribute will take precedence over the
   * jakarta.servlet.jsp.jstl.fmt.locale scoped attribute. This will be verified
   * by setting the localizationContext attribute so that it will resolve to an
   * en_US bundle, and the set the locale attribute to de_DE. If the formatting
   * action correctly uses the locale from the base name attribute, then no
   * parse exception will occur.
   */
  @Test
  public void positiveFormatLocalizationContextI18NTest() throws Exception {
    InputStream gfStream;
    TEST_PROPS.setProperty(TEST_NAME,
        "positiveFormatLocalizationContextI18NTest");
    if (isJavaVersion20OrGreater()) {
          TEST_PROPS.setProperty(REQUEST,
              "positiveFormatLocalizationContextI18NTestJava20Plus.jsp");
          gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFormatLocalizationContextI18NTestJava20Plus.gf");
    } else {
          TEST_PROPS.setProperty(REQUEST,
              "positiveFormatLocalizationContextI18NTest.jsp");
          gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFormatLocalizationContextI18NTest.gf");
    }
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-US");
    invoke();
  }

  /*
   * @testName: positiveForTokensTest
   * 
   * @assertion_ids: JSTL:SPEC:22
   * 
   * @testStrategy: Validate that forTokens can properly Iterate over a String
   * provided with specified delimiters.
   */
  @Test
  public void positiveForTokensTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveForTokensTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveForTokensTest");
    invoke();
  }

  /*
   * @testName: positiveIfScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:14.3.1; JSTL:SPEC:14.3.2; JSTL:SPEC:14.3.3;
   * JSTL:SPEC:14.3.4; JSTL:SPEC:14.3.5
   * 
   * @testStrategy: Verify the behavior of the 'if' action when using the scope
   * attribute. If scope is not specified, the exported var should be in the
   * page scope, otherwise the exported var should be in the designated scope.
   */
  @Test
  public void positiveIfScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveIfScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveIfScopeTest");
    invoke();
  }

  /*
   * @testName: positiveImportAbsUrlTest
   * 
   * @assertion_ids: JSTL:SPEC:23.1.3.1; JSTL:SPEC:23.1.3.2
   * 
   * @testStrategy: Validate that resources identified by an absolute URL can be
   * imported and displayed. This will test absolute URLs for HTTP and FTP
   */
  @Test
  public void positiveImportAbsUrlTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveImportAbsUrlTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveImportAbsUrlTest");
    invoke();
  }

  /*
   * @testName: positiveItemsObjArrayTest
   * 
   * @assertion_ids: JSTL:SPEC:21.2.1
   * 
   * @testStrategy: Validate that arrays of Object types can be handled by
   * 'forEach'.
   */
  @Test
  public void positiveItemsObjArrayTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveItemsObjArrayTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveItemsObjArrayTest");
    invoke();
  }

  /*
   * @testName: positiveJSTLURITest
   * 
   * @assertion_ids: JSTL:SPEC:1; JSTL:SPEC:2; JSTL:SPEC:3; JSTL:SPEC:4;
   * JSTL:SPEC:16; JSTL:SPEC:17; JSTL:SPEC:18; JSTL:SPEC:19
   * 
   * @testStrategy: Import all defined taglib URI definitions for both EL and RT
   * tags. If defined correctly, a fatal translation error should not occur (
   * per section 7.3.6.2 of the JavaServer Pages 1.2 Specification.
   */
  @Test
  public void positiveJSTLURITest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "positiveJSTLURITest");
    TEST_PROPS.setProperty(REQUEST, "positiveJSTLURITest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positiveMessageKeyTest
   * 
   * @assertion_ids: JSTL:SPEC:44.1
   * 
   * @testStrategy: Validate that the message action is able to properly lookup
   * and display the message associated with the provided key. This will also
   * establish that the key attribute can accept both static and dynamic values.
   */
  @Test
  public void positiveMessageKeyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveMessageKeyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveMessageKeyTest");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveMessageKeyTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveMessageKeyTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveOutEscXmlTest
   * 
   * @assertion_ids: JSTL:SPEC:68.3
   * 
   * @testStrategy: Validate that the escaping of XML entities (<,>,&,',") will
   * occur if the escapeXml is not present, or the value is true. Also validate
   * that no escaping occurs if the value is false.
   */
  @Test
  public void positiveOutEscXmlTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveOutEscXmlTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveOutEscXmlTest");
    invoke();
  }

  /*
   * @testName: positiveParamNameValueTest
   * 
   * @assertion_ids: JSTL:SPEC:74.1; JSTL:SPEC:74.2
   * 
   * @testStrategy: Validate the the name and value attributes can accept both
   * dynamic and static values.
   */
  @Test
  public void positiveParamNameValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamNameValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveParamNameValueTest");
    invoke();
  }

  /*
   * @testName: positiveParamValueTest
   * 
   * @assertion_ids: JSTL:SPEC:75; JSTL:SPEC:75.1; JSTL:SPEC:75.1.1;
   * JSTL:SPEC:75.1.2
   * 
   * @testStrategy: Validate that parametric replacement occurs when param
   * subtags are specified with either dynamic or static values.
   */
  @Test
  public void positiveParamValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveParamValueTest");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveParamValueTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveParamValueTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveParseVarTest
   * 
   * @assertion_ids: JSTL:SPEC:67; JSTL:SPEC:73.2.1.4
   * 
   * @testStrategy: Validate that if the parse operation is successfull, and var
   * is specified, the result is available via the PageContext and is of type
   * java.lang.Object.
   */
  @Test
  public void positiveParseVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParseVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveParseVarTest");
    invoke();
  }

  /*
   * @testName: positivePDValueTest
   * 
   * @assertion_ids: JSTL:SPEC:57.22
   * 
   * @testStrategy: Validate that parseDate action can properly parse dates
   * provided as dynamic or static String values. Pass the parsed value to
   * formatDate to display (due to possible timezone difference).
   */
  @Test
  public void positivePDValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positivePDValueTest");
    invoke();
  }

  /*
   * @testName: positivePermittedTlvTest
   * 
   * @assertion_ids: JSTL:SPEC:104.2; JSTL:SPEC:104.3
   * 
   * @testStrategy: Validate that if a URI that refers to a specific set of
   * libraries is specified as a paramter to the PermittedTaglibsTLV, that the
   * use of this library doesn't generate a translation error.
   */
  @Test
  public void positivePermittedTlvTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "positivePermittedTlvTest");
    TEST_PROPS.setProperty(REQUEST, "positivePermittedTlvTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positivePNValueTest
   * 
   * @assertion_ids: JSTL:SPEC:58
   * 
   * @testStrategy: Validate the action can correctly parse dynamic and static
   * values provided to the value attribute.
   */
  @Test
  public void positivePNValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePNValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positivePNValueTest");
    invoke();
  }

  /*
   * @testName: positiveQueryScopeAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:59.6; JSTL:SPEC:59.6.1; JSTL:SPEC:59.6.2;
   * JSTL:SPEC:59.6.3; JSTL:SPEC:59.6.4; JSTL:SPEC:59.14
   * 
   * @testStrategy: Validate that the action exports var to the specified scope
   * as well as validating that if scope is not specified, var will be exported
   * to the page scope by default.
   */
  @Test
  public void positiveQueryScopeAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveQueryScopeAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveQueryScopeAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveRedirectTest
   * 
   * @assertion_ids: JSTL:SPEC:43; JSTL:SPEC:43.1; JSTL:SPEC:43.1.1;
   * JSTL:SPEC:43.1.2
   * 
   * @testStrategy: Validate that the action can properly redirect when the url
   * attribute is provided either a dynamic or static values.
   */
  @Test
  public void positiveRedirectTest() throws Exception {
    /* EL */
    // abs
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?el1=true");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_1_0_compat_web/jstl/core/urlresource/param/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?el2=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_1_0_compat_web/jstl/core/urlresource/param/import.jsp");
    invoke();

    // rel
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?el3=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port + "/jstl_1_0_compat_web/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?el4=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port + "/jstl_1_0_compat_web/import.jsp");
    invoke();

    // foreign context
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?el5=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_1_0_compat_web/jstl/core/urlresource/param/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?el6=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_1_0_compat_web/jstl/core/urlresource/param/import.jsp");
    invoke();

    /* RT */
    // abs
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt1=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_1_0_compat_web/jstl/core/urlresource/param/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt2=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_1_0_compat_web/jstl/core/urlresource/param/import.jsp");
    invoke();

    // rel
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt3=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port + "/jstl_1_0_compat_web/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt4=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "Location:  http://" + _hostname
        + ":" + _port + "/jstl_1_0_compat_web/import.jsp");
    invoke();

    // foreign context
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt5=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_1_0_compat_web/jstl/core/urlresource/param/import.jsp");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveRedirectTest");
    TEST_PROPS.setProperty(IGNORE_BODY, "true");
    TEST_PROPS.setProperty(REQUEST, "positiveRedirectTest.jsp?rt6=true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "Location:  http://" + _hostname + ":" + _port
            + "/jstl_1_0_compat_web/jstl/core/urlresource/param/import.jsp");
    invoke();

  }

  /*
   * @testName: positiveRemoveScopeVarTest
   * 
   * @assertion_ids: JSTL:SPEC:41.1; JSTL:SPEC:41.2; JSTL:SPEC:41.3
   * 
   * @testStrategy: Validate that the remove action can properly remove a scoped
   * attribute by providing only a value to the var attribute. This should
   * remove the attribute no matter the scope in which it exists. Also validate
   * that if scope is specified, the var is properly removed.
   */
  @Test
  public void positiveRemoveScopeVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveRemoveScopeVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveRemoveScopeVarTest");
    invoke();
  }

  /*
   * @testName: positiveResultGetRowsCountTest
   * 
   * @assertion_ids: JSTL:SPEC:59
   * 
   * @testStrategy: Validate that the correct number of rows is returned by
   * Result.getRows().
   */
  @Test
  public void positiveResultGetRowsCountTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveResultGetRowsCountTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveResultGetRowsCountTest");
    invoke();
  }

  /*
   * @testName: negativeScriptFreeTlvNoDeclTest
   * 
   * @assertion_ids: JSTL:SPEC:22.1
   * 
   * @testStrategy: Validate that if declarations aren't allowed per the
   * configured validator, a translation error occurs if a declaration is
   * encountered.
   */
  @Test
  public void negativeScriptFreeTlvNoDeclTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeScriptFreeTlvNoDeclTest");
    TEST_PROPS.setProperty(REQUEST, "negativeScriptFreeTlvNoDeclTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: positiveSetBundleScopeVarTest
   * 
   * @assertion_ids: JSTL:SPEC:22.1; JSTL:SPEC:22.1.1
   * 
   * @testStrategy: Validate the behavior of the action when both scope and var
   * are specified. Var should be exported to the scope as specified by the
   * scope attribute. If scope is not present and var is, var will be exported
   * to the page scope by default.
   */
  @Test
  public void positiveSetBundleScopeVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetBundleScopeVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveSetBundleScopeVarTest");
    TEST_PROPS.setProperty(REQUEST, "positiveSetBundleScopeVarTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveSetBundleScopeVarTest.gf");
    invoke();
  }

  /*
   * @testName: positiveSetDataSourceScopeAttributeTest
   * 
   * @assertion_ids: JSTL:SPEC:62; JSTL:SPEC:62.6; JSTL:SPEC:62.6.1
   * 
   * @testStrategy: Validate sql:setDataSource exports the var attribute to the
   * correct scope.
   */
  @Test
  public void positiveSetDataSourceScopeAttributeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetDataSourceScopeAttributeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT,
        "positiveSetDataSourceScopeAttributeTest");
    invoke();
  }

  /*
   * @testName: positiveSetLocaleValueTest
   * 
   * @assertion_ids: JSTL:SPEC:27; JSTL:SPEC:27.1; JSTL:SPEC:28
   * 
   * @testStrategy: Validate value can accept both String representations or
   * locales as well as instances of java.util.Locale.
   */
  @Test
  public void positiveSetLocaleValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetLocaleValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveSetLocaleValueTest");
    invoke();
  }

  /*
   * @testName: positiveSetScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:13.4.1; JSTL:SPEC:13.4.2; JSTL:SPEC:13.4.3;
   * JSTL:SPEC:13.4.4; JSTL:SPEC:13.5
   * 
   * @testStrategy: Validated the different scope behaviors (default and
   * explicitly set scopes) by exporting different vairables to the assorted
   * scopes and then print the values using PageContext.getAttribute(String,
   * int).
   */
  @Test
  public void positiveSetScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveSetScopeTest");
    invoke();
  }

  /*
   * @testName: positiveSetSelectVarTest
   * 
   * @assertion_ids: JSTL:SPEC:69; JSTL:SPEC:69.2
   * 
   * @testStrategy: Validate the action properly sets a scoped variable when
   * select is provided a valid XPath expression and the the variable reference
   * by var is available to another action.
   */
  @Test
  public void positiveSetSelectVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetSelectVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveSetSelectVarTest");
    invoke();
  }

  /*
   * @testName: positiveSetTimezoneValueTest
   * 
   * @assertion_ids: JSTL:SPEC:57.1; JSTL:SPEC:57.1.1; JSTL:SPEC:57.1.2;
   * JSTL:SPEC:57.1.3
   * 
   * @testStrategy: Validate that the value attribute can accept both static
   * values as well as three letter timezones (ex. PST) or fully qualified
   * values (ex. America/Los_Angeles).
   */
  @Test
  public void positiveSetTimezoneValueTest() throws Exception {
    InputStream gfStream;
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveSetTimezoneValueTest");
    if (isJavaVersion20OrGreater()) {
      gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetTimezoneValueTestJava20Plus.gf");
    } else {
      gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetTimezoneValueTest.gf");
    }
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveTimezoneValueTest
   * 
   * @assertion_ids: JSTL:SPEC:57.1; JSTL:SPEC:57.1.1; JSTL:SPEC:57.1.2;
   * JSTL:SPEC:57.1.3
   * 
   * @testStrategy: Validate that the value attribute can accept both static
   * values as well as three letter timezones (ex. PST) or fully qualified
   * values (ex. America/Los_Angeles).
   */
  @Test
  public void positiveTimezoneValueTest() throws Exception {
    InputStream gfStream;
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveTimezoneValueTest");
    if (isJavaVersion20OrGreater()) {
      gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTimezoneValueTestJava20Plus.gf");
    } else {
      gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTimezoneValueTest.gf");
    }
    setGoldenFileStream(gfStream);

    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveTransformVarTest
   * 
   * @assertion_ids: JSTL:SPEC:73; JSTL:SPEC:73.2; JSTL:SPEC:73.2.1.1;
   * JSTL:SPEC:73.2.1.2; JSTL:SPEC:73.2.1.3; JSTL:SPEC:73.2.1.4;
   * JSTL:SPEC:73.2.1.5; JSTL:SPEC:73.2.1.6
   * 
   * @testStrategy: Validate that if var is specified, the variable name
   * reference by var is available in the PageContext and is of type
   * org.w3c.dom.Document.
   */
  @Test
  public void positiveTransformVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTransformVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveTransformVarTest");
    invoke();
  }

  /*
   * @testName: positiveTxQueryCommitTest
   * 
   * @assertion_ids: JSTL:SPEC:59.7; JSTL:SPEC:59.9
   * 
   * @testStrategy: Validate sql:transaction and sql:query actions allow a query
   * to be successfully executed.
   */
  @Test
  public void positiveTxQueryCommitTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTxQueryCommitTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveTxQueryCommitTest");
    invoke();
  }

  /*
   * @testName: positiveUpdateBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:60; JSTL:SPEC:60.1
   * 
   * @testStrategy: Validate the sql:update action - That a SQL query can be
   * passed as body content.
   */
  @Test
  public void positiveUpdateBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUpdateBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveUpdateBodyContentTest");
    invoke();
  }

  /*
   * @testName: positiveUrlScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:24; JSTL:SPEC:24.2
   * 
   * @testStrategy: Validate the behavior of the scope attribute with respect to
   * var, both when scope is explicitly defined and when not defined.
   */
  @Test
  public void positiveUrlScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUrlScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveUrlScopeTest");
    invoke();
  }

  /*
   * @testName: positiveXParamNameTest
   * 
   * @assertion_ids: JSTL:SPEC:73.13
   * 
   * @testStrategy: Validate the name attribute of the x:param action is able to
   * accept both static and dynamic values.
   */
  @Test
  public void positiveXParamNameTest() throws Exception {
    // TEST_PROPS.setProperty(STANDARD_COMPAT, "positiveXParamNameTest");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_1_0_compat_web/positiveXParamNameTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "10pt|Param properly used|10pt|Param properly used");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "REPLACE");
    invoke();
  }
}
