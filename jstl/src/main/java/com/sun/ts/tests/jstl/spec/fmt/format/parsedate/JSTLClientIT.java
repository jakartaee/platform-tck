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

/*
 * @(#)JSTLClient.java	1.1 04/06/02
 */

package com.sun.ts.tests.jstl.spec.fmt.format.parsedate;

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
    setContextRoot("/jstl_fmt_psdate_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_fmt_psdate_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_fmt_psdate_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativePDScopeNoVarTest.jsp")), "negativePDScopeNoVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativePDUnableToParseValueTest.jsp")), "negativePDUnableToParseValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDBodyValueTest.jsp")), "positivePDBodyValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDBodyValueTestJava20Plus.jsp")), "positivePDBodyValueTestJava20Plus.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDDateStyleTest.jsp")), "positivePDDateStyleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDDateStyleTestJava20Plus.jsp")), "positivePDDateStyleTestJava20Plus.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDFallbackLocaleTest.jsp")), "positivePDFallbackLocaleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDFallbackLocaleTestJava20Plus.jsp")), "positivePDFallbackLocaleTestJava20Plus.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDLocalizationContextTest.jsp")), "positivePDLocalizationContextTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDLocalizationContextTestJava20Plus.jsp")), "positivePDLocalizationContextTestJava20Plus.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDParseLocaleNullEmptyTest.jsp")), "positivePDParseLocaleNullEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDParseLocaleTest.jsp")), "positivePDParseLocaleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDParseLocaleTestJava20Plus.jsp")), "positivePDParseLocaleTestJava20Plus.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDPatternTest.jsp")), "positivePDPatternTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDScopeTest.jsp")), "positivePDScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDTimeStyleTest.jsp")), "positivePDTimeStyleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDTimeStyleTestJava20Plus.jsp")), "positivePDTimeStyleTestJava20Plus.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDTimeZoneNullEmptyTest.jsp")), "positivePDTimeZoneNullEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDTimeZoneNullEmptyTestJava20Plus.jsp")), "positivePDTimeZoneNullEmptyTestJava20Plus.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDTimeZonePrecedenceTest.jsp")), "positivePDTimeZonePrecedenceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDTimeZonePrecedenceTestJava20Plus.jsp")), "positivePDTimeZonePrecedenceTestJava20Plus.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDTimeZoneTest.jsp")), "positivePDTimeZoneTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDTimeZoneTestJava20Plus.jsp")), "positivePDTimeZoneTestJava20Plus.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDTypeTest.jsp")), "positivePDTypeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDTypeTestJava20Plus.jsp")), "positivePDTypeTestJava20Plus.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDValueNullEmptyTest.jsp")), "positivePDValueNullEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDValueTest.jsp")), "positivePDValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePDVarTest.jsp")), "positivePDVarTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positivePDValueTest
   * 
   * @assertion_ids: JSTL:SPEC:58; JSTL:SPEC:58.1; JSTL:SPEC:58.1.1
   * 
   * @testStrategy: Validate that parseDate action can properly parse dates
   * provided as dynamic or static String values. Pass the parsed value to
   * formatDate to display (due to possible timezone difference).
   */
  @Test
  public void positivePDValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positivePDValueTest");
    invoke();
  }

  /*
   * @testName: positivePDTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:58.2; JSTL:SPEC:58.2.1; JSTL:SPEC:58.2.2;
   * JSTL:SPEC:58.2.3; JSTL:SPEC:58.2.4; JSTL:SPEC:58.15
   * 
   * @testStrategy: Validate the following: - If type is not specified, the
   * action properly parses a String with the only the date component. - The
   * value is properly parsed when explicity using time, date, or both. Pass the
   * parsed value to formatDate to display (due to possible timezone
   * difference).
   */
  @Test
  public void positivePDTypeTest() throws Exception {
    InputStream gfStream;
    if (isJavaVersion20OrGreater()) {
      TEST_PROPS.setProperty(REQUEST,
          "positivePDTypeTestJava20Plus.jsp");
      gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDTypeTestJava20Plus.gf");
    } else {
        TEST_PROPS.setProperty(REQUEST,
            "positivePDTypeTest.jsp");
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDTypeTest.gf");
    }
    setGoldenFileStream(gfStream);
    // TEST_PROPS.setProperty(STANDARD, "positivePDTypeTest");
    TEST_PROPS.setProperty(TEST_NAME, "positivePDTypeTest");
    invoke();
  }

  /*
   * @testName: positivePDDateStyleTest
   * 
   * @assertion_ids: JSTL:SPEC:58.3; JSTL:SPEC:58.3.1; JSTL:SPEC:58.3.2;
   * JSTL:SPEC:58.3.3; JSTL:SPEC:58.3.4; JSTL:SPEC:58.3.5; JSTL:SPEC:58.3.6;
   * JSTL:SPEC:58.3.7; JSTL:SPEC:58.16
   * 
   * @testStrategy: Validate the following: - Dates are parsed correctly for all
   * values of the dateStyle attribute. - If dateStyle is present it will only
   * be applied when type is not specified, or is set to date or both. - If
   * dateStyle is not present, the action will assume the date to be parsed is
   * in the default style.
   */
  @Test
  public void positivePDDateStyleTest() throws Exception {
    InputStream gfStream;
    TEST_PROPS.setProperty(TEST_NAME, "positivePDDateStyleTest");
    if (isJavaVersion20OrGreater()) {
      TEST_PROPS.setProperty(REQUEST,
          "positivePDDateStyleTestJava20Plus.jsp");
      gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDDateStyleTestJava20Plus.gf");
    } else {
        TEST_PROPS.setProperty(REQUEST,
            "positivePDDateStyleTest.jsp");
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDDateStyleTest.gf");
    }
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positivePDTimeStyleTest
   * 
   * @assertion_ids: JSTL:SPEC:58.4; JSTL:SPEC:58.4.1; JSTL:SPEC:58.4.2;
   * JSTL:SPEC:58.4.3; JSTL:SPEC:58.4.4; JSTL:SPEC:58.4.5; JSTL:SPEC:58.4.6;
   * JSTL:SPEC:58.17
   * 
   * @testStrategy: Validate the following: - Times are parsed correctly for all
   * values of the timeStyle attribute. - If timeStyle is present is will only
   * be applied when type is set to time or both. - If timeStyle is not present,
   * the action will assume the time to be parsed is in the default style.
   */
  @Test
  public void positivePDTimeStyleTest() throws Exception {
    InputStream gfStream;
    TEST_PROPS.setProperty(TEST_NAME, "positivePDTimeStyleTest");
    
    if (isJavaVersion20OrGreater()) {
      TEST_PROPS.setProperty(REQUEST,
          "positivePDTimeStyleTestJava20Plus.jsp");
      gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDTimeStyleTestJava20Plus.gf");
    } else {
        TEST_PROPS.setProperty(REQUEST,
            "positivePDTimeStyleTest.jsp");
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDTimeStyleTest.gf");
    }
    setGoldenFileStream(gfStream);

    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positivePDParseLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:58.7; JSTL:SPEC:58.7.1
   * 
   * @testStrategy: Validate the action will properly parse a date using the
   * locale provided via the parseLocale attribute. This will be confirmed by
   * setting the locale of the page to de_DE, and pass en_US to the action
   * itself. No parsing exceptions should occur.
   */
  @Test
  public void positivePDParseLocaleTest() throws Exception {
    InputStream gfStream;
    TEST_PROPS.setProperty(TEST_NAME, "positivePDParseLocaleTest");
    if (isJavaVersion20OrGreater()) {
      TEST_PROPS.setProperty(REQUEST,
          "positivePDParseLocaleTestJava20Plus.jsp");
      gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDParseLocaleTestJava20Plus.gf");
    } else {
        TEST_PROPS.setProperty(REQUEST,
            "positivePDParseLocaleTest.jsp");
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDParseLocaleTest.gf");
    }
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: positivePDPatternTest
   * 
   * @assertion_ids: JSTL:SPEC:58.5; JSTL:SPEC:58.5.1
   * 
   * @testStragegy: Validate that if pattern is present, it is properly applied
   * when parsing the date value provided. Pass the parsed value to formatDate
   * to display (due to possible timezone difference).
   */
  @Test
  public void positivePDPatternTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDPatternTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positivePDPatternTest");
    invoke();
  }

  /*
   * @testName: positivePDVarTest
   * 
   * @assertion_ids: JSTL:SPEC:58.8; JSTL:SPEC:58.8.1; JSTL:SPEC:58.14
   * 
   * @testStrategy: Validate that the if var is specified and scope is not, the
   * parsed value is not written to the current JspWriter, but is instead
   * available via the PageContext and is of type java.util.Date (validation
   * performed via custom action).
   */
  @Test
  public void positivePDVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positivePDVarTest");
    invoke();
  }

  /*
   * @testName: positivePDScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:58.9; JSTL:SPEC:58.9.1; JSTL:SPEC:58.9.2;
   * JSTL:SPEC:58.9.3; JSTL:SPEC:58.9.4; JSTL:SPEC:58.14
   * 
   * @testStrategy: Validate that var is properly exported to the scopes as
   * specified by the scope attribute. Also verify that if scope is not present,
   * that var is exported to the page scope by default.
   */
  @Test
  public void positivePDScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positivePDScopeTest");
    invoke();
  }

  /*
   * @testName: positivePDValueNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:58.11
   * 
   * @testStrategy: Validate that if value is null or empty, that the scoped
   * variable referenced by var is indeed removed. This will be validated by
   * setting a scoped variable before calling the action with a null or empty
   * value, and then verifying that the var is no longer available. Pass the
   * parsed value to formatDate to display (due to possible timezone
   * difference).
   */
  @Test
  public void positivePDValueNullEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDValueNullEmptyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positivePDValueNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positivePDBodyValueTest
   * 
   * @assertion_ids: JSTL:SPEC:58.12
   * 
   * @testStrategy: Validate the action can properly parse a date string
   * provided as body content to the action.
   */
  @Test
  public void positivePDBodyValueTest() throws Exception {
    InputStream gfStream;
    // TEST_PROPS.setProperty(STANDARD, "positivePDBodyValueTest");
    TEST_PROPS.setProperty(TEST_NAME, "positivePDBodyValueTest");
    if (isJavaVersion20OrGreater()) {
      TEST_PROPS.setProperty(REQUEST,
          "positivePDBodyValueTestJava20Plus.jsp");
      gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDBodyValueTestJava20Plus.gf");
    } else {
        TEST_PROPS.setProperty(REQUEST,
            "positivePDBodyValueTest.jsp");
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDBodyValueTest.gf");
    }
    setGoldenFileStream(gfStream);
    invoke();
  }

  /*
   * @testName: positivePDParseLocaleNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:58.26
   * 
   * @testStrategy: Validate that if the parseLocale attribute is null or empty,
   * the action behaves as of the attribute was not specified. Since setLocale
   * was used, the parse action should use the locale from the locale
   * configuration variable. Pass the parsed value to formatDate to display (due
   * to possible timezone difference).
   */
  @Test
  public void positivePDParseLocaleNullEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDParseLocaleNullEmptyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positivePDParseLocaleNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positivePDLocalizationContextTest
   * 
   * @assertion_ids: JSTL:SPEC:46.2
   * 
   * @testStrategy: Validate that the action can properly format a date based on
   * the default I18N localization context configuration variable
   * jakarta.servlet.jsp.jstl.fmt.localizationContext. Pass the parsed value to
   * formatDate to display (due to possible timezone difference).
   */
  @Test
  public void positivePDLocalizationContextTest() throws Exception {
    InputStream gfStream;
    TEST_PROPS.setProperty(TEST_NAME, "positivePDLocalizationContextTest");
    if (isJavaVersion20OrGreater()) {
      TEST_PROPS.setProperty(REQUEST,
          "positivePDLocalizationContextTestJava20Plus.jsp");
      gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDLocalizationContextTestJava20Plus.gf");
    } else {
        TEST_PROPS.setProperty(REQUEST,
            "positivePDLocalizationContextTest.jsp");
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDLocalizationContextTest.gf");
    }
    setGoldenFileStream(gfStream);
    // TEST_PROPS.setProperty(GOLDENFILE, "positivePDLocalizationContextTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-US");
    invoke();
  }

  /*
   * @testName: positivePDFallbackLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:50.2
   * 
   * @testStrategy: Validate that if no matching locale can be found, that the
   * fallback locale will be used. Pass the parsed value to formatDate to
   * display (due to possible timezone difference).
   */
  @Test
  public void positivePDFallbackLocaleTest() throws Exception {
    InputStream gfStream;
    TEST_PROPS.setProperty(TEST_NAME, "positivePDFallbackLocaleTest");
    if (isJavaVersion20OrGreater()) {
      TEST_PROPS.setProperty(REQUEST,
          "positivePDFallbackLocaleTestJava20Plus.jsp");
      gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDFallbackLocaleTestJava20Plus.gf");
    } else {
        TEST_PROPS.setProperty(REQUEST,
            "positivePDFallbackLocaleTest.jsp");
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDFallbackLocaleTest.gf");
    }

    setGoldenFileStream(gfStream);
    // TEST_PROPS.setProperty(GOLDENFILE, "positivePDFallbackLocaleTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: xx-XX");
    invoke();
  }

  /*
   * @testName: negativePDUnableToParseValueTest
   * 
   * @assertion_ids: JSTL:SPEC:58.23; JSTL:SPEC:58.23.1
   * 
   * @testStrategy: Validate that if the formatting action is unable to parse
   * the String value provided, the exception is rethrown as a JspException,
   * with the rethrown unparsable value in the exception text and the original
   * exception set as the root cause of the JspException.
   */
  @Test
  public void negativePDUnableToParseValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativePDUnableToParseValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativePDUnableToParseValueTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positivePDTimeZoneTest
   * 
   * @assertion_ids: JSTL:SPEC:58.6; JSTL:SPEC:58.6.1
   * 
   * @testStrategy: Validate that the timeZone attribute is able to accept both
   * String literals representing a time zone, as well as java.util.TimeZone
   * objects. Pass the parsed value to formatDate to display (due to possible
   * timezone difference).
   */
  @Test
  public void positivePDTimeZoneTest() throws Exception {
    InputStream gfStream;
    // TEST_PROPS.setProperty(STANDARD, "positivePDTimeZoneTest");
    TEST_PROPS.setProperty(TEST_NAME, "positivePDTimeZoneTest");
    if (isJavaVersion20OrGreater()) {
      TEST_PROPS.setProperty(REQUEST,
          "positivePDTimeZoneTestJava20Plus.jsp");
      gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDTimeZoneTestJava20Plus.gf");
    } else {
        TEST_PROPS.setProperty(REQUEST,
            "positivePDTimeZoneTest.jsp");
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDTimeZoneTest.gf");

    }
    setGoldenFileStream(gfStream);

    invoke();
  }

  /*
   * @testName: positivePDTimeZoneNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:58.25
   * 
   * @testStrategy: Validate that if timeZone is null or empty, the value will
   * be formatted as if it was missing. In this case, the time will be formatted
   * using the page's time zone of EST. Pass the parsed value to formatDate to
   * display (due to possible timezone difference).
   */
  @Test
  public void positivePDTimeZoneNullEmptyTest() throws Exception {
    InputStream gfStream;
    // TEST_PROPS.setProperty(STANDARD, "positivePDTimeZoneNullEmptyTest");
    TEST_PROPS.setProperty(TEST_NAME, "positivePDTimeZoneNullEmptyTest");
    if (isJavaVersion20OrGreater()) {
        TEST_PROPS.setProperty(REQUEST,
            "positivePDTimeZoneNullEmptyTestJava20Plus.jsp");
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDTimeZoneNullEmptyTestJava20Plus.gf");
    } else {
        TEST_PROPS.setProperty(REQUEST,
            "positivePDTimeZoneNullEmptyTest.jsp");
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDTimeZoneNullEmptyTest.gf");
    }
    setGoldenFileStream(gfStream);

    invoke();
  }

  /*
   * @testName: positivePDTimeZonePrecedenceTest
   * 
   * @assertion_ids: JSTL:SPEC:58.19; JSTL:SPEC:58.19; JSTL:SPEC:58.21
   * 
   * @testStrategy: Validate that following order for determining the time zone
   * to be used when formatting date/times. In order of precedence: - If
   * present, use the value of the timeZone attribute. - If wrapped in a
   * <fmt:setTimeZone> action, use the timeZone of that action. - If no timeZone
   * attribute present, and not wrapped by the fmt:setTimeZone action, use the
   * value of the scoped attribute jakarta.servlet.jsp.jstl.fmt.timeZone Pass the
   * parsed value to formatDate to display (due to possible timezone
   * difference).
   */
  @Test
  public void positivePDTimeZonePrecedenceTest() throws Exception {
    InputStream gfStream;
    // TEST_PROPS.setProperty(STANDARD, "positivePDTimeZonePrecedenceTest");
    TEST_PROPS.setProperty(TEST_NAME, "positivePDTimeZonePrecedenceTest");
    if (isJavaVersion20OrGreater()) {
      TEST_PROPS.setProperty(REQUEST,
          "positivePDTimeZonePrecedenceTestJava20Plus.jsp");
      gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDTimeZonePrecedenceTestJava20Plus.gf");
    } else {
        TEST_PROPS.setProperty(REQUEST,
            "positivePDTimeZonePrecedenceTest.jsp");
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePDTimeZonePrecedenceTest.gf");
    }
    setGoldenFileStream(gfStream);
    invoke();
  }

  /*
   * @testName: negativePDScopeNoVarTest
   * 
   * @assertion_ids: JSTL:SPEC:58.10
   * 
   * @testStrategy: validate that if var is not specified, but scope is, that a
   * fatal translation error occurs.
   */
  @Test
  public void negativePDScopeNoVarTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativePDScopeNoVarTest");
    TEST_PROPS.setProperty(REQUEST, "negativePDScopeNoVarTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

}
