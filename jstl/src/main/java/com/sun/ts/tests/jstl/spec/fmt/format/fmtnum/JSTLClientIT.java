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



package com.sun.ts.tests.jstl.spec.fmt.format.fmtnum;

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
    setContextRoot("/jstl_fmt_fmtnum_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_fmt_fmtnum_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_fmt_fmtnum_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNVarTest.jsp")), "positiveFNVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNValueTest.jsp")), "positiveFNValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNValueNullEmptyTest.jsp")), "positiveFNValueNullEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNTypeTest.jsp")), "positiveFNTypeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNScopeTest.jsp")), "positiveFNScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNPatternTest.jsp")), "positiveFNPatternTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNMinIntDigitsTest.jsp")), "positiveFNMinIntDigitsTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNMinFracDigitsTest.jsp")), "positiveFNMinFracDigitsTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNMaxIntDigitsTest.jsp")), "positiveFNMaxIntDigitsTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNMaxFracDigitsTest.jsp")), "positiveFNMaxFracDigitsTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNLocalizationContextTest.jsp")), "positiveFNLocalizationContextTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNGroupingUsedTest.jsp")), "positiveFNGroupingUsedTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNFallbackLocaleTest.jsp")), "positiveFNFallbackLocaleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNCurrencySymbolTest.jsp")), "positiveFNCurrencySymbolTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNCurrencyCodeTest.jsp")), "positiveFNCurrencyCodeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNCodeSymbolTest.jsp")), "positiveFNCodeSymbolTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFNBodyValueTest.jsp")), "positiveFNBodyValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeFNUnableToParseValueTest.jsp")), "negativeFNUnableToParseValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeFNScopeNoVarTest.jsp")), "negativeFNScopeNoVarTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveFNValueTest
   * 
   * @assertion_ids: JSTL:SPEC:55; JSTL:SPEC:55.1; JSTL:SPEC:55.1.1
   * 
   * @testStrategy: Validate the value attribute can accept both String and
   * Number instances as well as the acceptance of dynamic and static values.
   */
  @Test
  public void positiveFNValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFNValueTest");
    invoke();
  }

  /*
   * @testName: positiveFNTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:55.2; JSTL:SPEC:55.2.1; JSTL:SPEC:55.2.2;
   * JSTL:SPEC:55.2.3; JSTL:SPEC:55.2.4
   * 
   * @testStrategy: Validate that the inclusion of the type attribute properly
   * affects the formatting of the value. Also validate that if type is not
   * specified, that the value will be formatted as a number by default.
   */
  @Test
  public void positiveFNTypeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNTypeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFNTypeTest");
    invoke();
  }

  /*
   * @testName: positiveFNPatternTest
   * 
   * @assertion_ids: JSTL:SPEC:55.3; JSTL:SPEC:55.33
   * 
   * @testStrategy: Validate that a pattern is properly applied when formatting
   * numbers. Also validate that if pattern is specified, that type is ignored.
   */
  @Test
  public void positiveFNPatternTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNPatternTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFNPatternTest");
    invoke();
  }

  /*
   * @testName: positiveFNCurrencySymbolTest
   * 
   * @assertion_ids: JSTL:SPEC:55.5; JSTL:SPEC:55.5.1
   * 
   * @testStrategy: Validate that currencySymbol is applied to the formatted
   * value when type is currency. If type is not currency, then currencySymbol
   * should not be applied.
   */
  @Test
  public void positiveFNCurrencySymbolTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNCurrencySymbolTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFNCurrencySymbolTest");
    invoke();
  }

  /*
   * @testName: positiveFNCurrencyCodeTest
   * 
   * @assertion_ids: JSTL:SPEC:55.4; JSTL:SPEC:55.5.1
   * 
   * @testStrategy: Validate that the currency code is properly applied to the
   * value to be formatted when type is currency. If type is not currency, then
   * currencyCode should not be applied.
   */
  @Test
  public void positiveFNCurrencyCodeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNCurrencyCodeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFNCurrencyCodeTest");
    invoke();
  }

  /*
   * @testName: positiveFNGroupingUsedTest
   * 
   * @assertion_ids: JSTL:SPEC:55.6; JSTL:SPEC:56.3
   * 
   * @testStrategy: Validate the behavior of groupingUsed. When true, grouping
   * separators should be applied to all format types (number, currency,
   * percent). If groupingUsed is not specified, groupings will be applied to
   * the value by default.
   */
  @Test
  public void positiveFNGroupingUsedTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNGroupingUsedTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFNGroupingUsedTest");
    invoke();
  }

  /*
   * @testName: positiveFNMaxIntDigitsTest
   * 
   * @assertion_ids: JSTL:SPEC:55.7
   * 
   * @testStrategy: Validate the behavior of maxIntegerDigits when formatting
   * different types (number, currency, percent). The presence of the attribute
   * should appropriately reduce the number of digits in the formatted value if
   * the number of integer digits exceeds value of the maxIntegerDigits
   * attribute.
   */
  @Test
  public void positiveFNMaxIntDigitsTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNMaxIntDigitsTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFNMaxIntDigitsTest");
    invoke();
  }

  /*
   * @testName: positiveFNMinIntDigitsTest
   * 
   * @assertion_ids: JSTL:SPEC:55.8
   * 
   * @testStrategy: Validate the behavior of minIntegerDigits when formatting
   * different types (number, currency, percent). The presence of the attribute
   * should enforce the minimum number of digits in the final result of the
   * format operation.
   */
  @Test
  public void positiveFNMinIntDigitsTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNMinIntDigitsTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFNMinIntDigitsTest");
    invoke();
  }

  /*
   * @testName: positiveFNMaxFracDigitsTest
   * 
   * @assertion_ids: JSTL:SPEC:55.9
   * 
   * @testStrategy: fractional digits exceeds value of the maxIntegerDigits
   * attribute.
   */
  @Test
  public void positiveFNMaxFracDigitsTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNMaxFracDigitsTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFNMaxFracDigitsTest");
    invoke();
  }

  /*
   * @testName: positiveFNMinFracDigitsTest
   * 
   * @assertion_ids: JSTL:SPEC:55.10
   * 
   * @testStrategy: Validate the behavior of minFractionDigits when formatting
   * different types (number, currency, percent). The presence of the attribute
   * should enforce the minimum number of fractional digits in the final result
   * of the format operation.
   */
  @Test
  public void positiveFNMinFracDigitsTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNMinFracDigitsTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFNMinFracDigitsTest");
    invoke();
  }

  /*
   * @testName: positiveFNVarTest
   * 
   * @assertion_ids: JSTL:SPEC:55.10
   * 
   * @testStrategy: Validate that the formatNumber action exports the formated
   * result and can be referenced by the variable name specified by var. Also
   * validate that the type of the scoped variable is java.lang.String.
   */
  @Test
  public void positiveFNVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFNVarTest");
    invoke();
  }

  /*
   * @testName: positiveFNScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:55.12; JSTL:SPEC:55.12.1
   * 
   * @testStrategy: Validate that the action exports var to the specified scope
   * as well as validating that if scope is not specified, var will be exported
   * to the page scope by default.
   */
  @Test
  public void positiveFNScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFNScopeTest");
    invoke();
  }

  /*
   * @testName: positiveFNValueNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:55.18
   * 
   * @testStrategy: Validate that if the value attribute is provided with a null
   * or empty value, that no action is performed.
   */
  @Test
  public void positiveFNValueNullEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNValueNullEmptyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFNValueNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positiveFNBodyValueTest
   * 
   * @assertion_ids: JSTL:SPEC:55.19
   * 
   * @testStrategy: Validate that the value to be formatted can be provided as
   * body content to the action.
   */
  @Test
  public void positiveFNBodyValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNBodyValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFNBodyValueTest");
    invoke();
  }

  /*
   * @testName: positiveFNCodeSymbolTest
   * 
   * @assertion_ids: JSTL:SPEC:55.26.1; JSTL:SPEC:55.27.1
   * 
   * @testStrategy: Validate the following with both currencyCode and
   * currencySymbol defined: - Using en_US as the Locale. - If runtime is 1.4 or
   * greater currencyCode will take precedence over currencySymbol. In this case
   * a '$' will prefix the formatted value. - If runtime earlier than 1.4,
   * currencySymbol will take precedence over currencyCode. In this case USD
   * should prefix the formatted value.
   */
  @Test
  public void positiveFNCodeSymbolTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNCodeSymbolTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFNCodeSymbolTest");
    invoke();
  }

  /*
   * @testName: positiveFNLocalizationContextTest
   * 
   * @assertion_ids: JSTL:SPEC:46.2; JSTL:SPEC:92.3
   * 
   * @testStrategy: Validate that the action can properly format a date based on
   * the default I18N localization context configuration variable
   * jakarta.servlet.jsp.jstl.fmt.localizationContext.
   */
  @Test
  public void positiveFNLocalizationContextTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNLocalizationContextTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveFNLocalizationContextTest");
    TEST_PROPS.setProperty(REQUEST, "positiveFNLocalizationContextTest.jsp");
    TEST_PROPS.setProperty(GOLDENFILE, "positiveFNLocalizationContextTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-US");
    invoke();
  }

  /*
   * @testName: positiveFNFallbackLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:50.2
   * 
   * @testStrategy: Validate that if no matching locale can be found, that the
   * fallback locale will be used.
   */
  @Test
  public void positiveFNFallbackLocaleTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFNFallbackLocaleTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveFNFallbackLocaleTest");
    TEST_PROPS.setProperty(REQUEST, "positiveFNFallbackLocaleTest.jsp");
    TEST_PROPS.setProperty(GOLDENFILE, "positiveFNFallbackLocaleTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: xx-XX");
    invoke();
  }

  /*
   * @testName: negativeFNUnableToParseValueTest
   * 
   * @assertion_ids: JSTL:SPEC:55.21
   * 
   * @testStrategy: Validate that if the formatting action is unable to parse
   * the String value provided, the exception is rethrown as a JspException,
   * with the rethrown unparsable value in the exception text and the original
   * exception set as the root cause of the JspException.
   */
  @Test
  public void negativeFNUnableToParseValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeFNUnableToParseValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeFNUnableToParseValueTest");
    invoke();
  }

  /*
   * @testName: negativeFNScopeNoVarTest
   * 
   * @assertion_ids: JSTL:SPEC:55.16
   * 
   * @testStrategy: validate that if var is not specified, but scope is, that a
   * fatal translation error occurs.
   */
  @Test
  public void negativeFNScopeNoVarTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeFNScopeNoVarTest");
    TEST_PROPS.setProperty(REQUEST, "negativeFNScopeNoVarTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
