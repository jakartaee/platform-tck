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



package com.sun.ts.tests.jstl.spec.fmt.format.fmtdate;

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
    setContextRoot("/jstl_fmt_fmtdate_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_fmt_fmtdate_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_fmt_fmtdate_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeFDScopeNoVarTest.jsp")), "negativeFDScopeNoVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFDDateStyleTest.jsp")), "positiveFDDateStyleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFDFallbackLocaleTest.jsp")), "positiveFDFallbackLocaleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFDLocalizationContextTest.jsp")), "positiveFDLocalizationContextTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFDPatternTest.jsp")), "positiveFDPatternTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFDScopeTest.jsp")), "positiveFDScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFDTimeStyleTest.jsp")), "positiveFDTimeStyleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFDTimeZoneNullEmptyTest.jsp")), "positiveFDTimeZoneNullEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFDTimeZonePrecedenceTest.jsp")), "positiveFDTimeZonePrecedenceTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFDTimeZoneTest.jsp")), "positiveFDTimeZoneTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFDTypeTest.jsp")), "positiveFDTypeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFDValueNullTest.jsp")), "positiveFDValueNullTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFDValueTest.jsp")), "positiveFDValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFDVarTest.jsp")), "positiveFDVarTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }


  /*
   * @testName: positiveFDValueTest
   * 
   * @assertion_ids: JSTL:SPEC:57; JSTL:SPEC:57.1; JSTL:SPEC:57.1.1;
   * JSTL:SPEC:57.22
   * 
   * @testStrategy: Validate that formatDate action can properly format dates
   * provided as String literals or as java.util.Date objects.
   */
  @Test
  public void positiveFDValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFDValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFDValueTest");
    invoke();
  }

  /*
   * @testName: positiveFDTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:57.2; JSTL:SPEC:57.2.1; JSTL:SPEC:57.2.2;
   * JSTL:SPEC:57.2.3; JSTL:SPEC:57.19
   * 
   * @testStrategy: Validate the following: - If type is not specified, only the
   * date will be formatted. - The value is properly formatted when explicity
   * using time, date, or both.
   */
  @Test
  public void positiveFDTypeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFDTypeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFDTypeTest");
    invoke();
  }

  /*
   * @testName: positiveFDDateStyleTest
   * 
   * @assertion_ids: JSTL:SPEC:57.3; JSTL:SPEC:57.3.1; JSTL:SPEC:57.20
   * 
   * @testStrategy: Validate the following: - If dateStyle is present it will
   * only be applied when type is not specified, or is set to date or both. - If
   * dateStyle is not present, the default value for dateStyle, default, will be
   * applied when type is not specified, or is set to date or both.
   */
  @Test
  public void positiveFDDateStyleTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFDDateStyleTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFDDateStyleTest");
    invoke();
  }

  /*
   * @testName: positiveFDTimeStyleTest
   * 
   * @assertion_ids: JSTL:SPEC:57.4; JSTL:SPEC:57.4.1; JSTL:SPEC:57.21
   * 
   * @testStrategy: Validate the following: - If timeStyle is present is will
   * only be applied when type is set to time or both. - If timeStyle is not
   * present, the default value for timeStyle, default, will be applied when
   * type is set to time or both.
   */
  @Test
  public void positiveFDTimeStyleTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFDTimeStyleTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFDTimeStyleTest");
    invoke();
  }

  /*
   * @testName: positiveFDPatternTest
   * 
   * @assertion_ids: JSTL:SPEC:57.5; JSTL:SPEC:57.18
   * 
   * @testStragegy: Validate that if pattern is present, it is properly applied
   * to the date value provided.
   */
  @Test
  public void positiveFDPatternTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFDPatternTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFDPatternTest");
    invoke();
  }

  /*
   * @testName: positiveFDTimeZoneTest
   * 
   * @assertion_ids: JSTL:SPEC:57.6; JSTL:SPEC:57.6.1
   * 
   * @testStrategy: Validate that the timeZone attribute is able to accept
   * String literals representing a time zone, as well as java.util.TimeZone
   * objects.
   */
  @Test
  public void positiveFDTimeZoneTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFDTimeZoneTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFDTimeZoneTest");
    invoke();
  }

  /*
   * @testName: positiveFDVarTest
   * 
   * @assertion_ids: JSTL:SPEC:57.6; JSTL:SPEC:57.6.1
   * 
   * @testStrategy: Validate that the if var is specified, the formatted value
   * is not written to the current JspWriter, but is instead availabe via the
   * PageContext and is of type String (validation performed via custom action).
   */
  @Test
  public void positiveFDVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFDVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFDVarTest");
    invoke();
  }

  /*
   * @testName: positiveFDScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:57.7; JSTL:SPEC:57.8; JSTL:SPEC:57.8.1;
   * JSTL:SPEC:57.8.2; JSTL:SPEC:57.8.3; JSTL:SPEC:57.8.4; JSTL:SPEC:57.8.5
   * 
   * @testStrategy: Validate that var is properly exported to the scopes as
   * specified by the scope attribute. Also verify that if scope is not present,
   * that var is exported to the page scope by default.
   */
  @Test
  public void positiveFDScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFDScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFDScopeTest");
    invoke();
  }

  /*
   * @testName: positiveFDValueNullTest
   * 
   * @assertion_ids: JSTL:SPEC:57.10
   * 
   * @testStrategy: Validate that if value is null and var, or scope and var is
   * specified, the scoped variable referenced by var is removed. If var, or
   * scope and var is not specified, no action occurs.
   */
  @Test
  public void positiveFDValueNullTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFDValueNullTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFDValueNullTest");
    invoke();
  }

  /*
   * @testName: positiveFDTimeZoneNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:57.11
   * 
   * @testStrategy: Validate that if timeZone is null or empty, the value will
   * be formatted as if it was present. In this case, the time will be formatted
   * using the page's time zone of EST.
   */
  @Test
  public void positiveFDTimeZoneNullEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFDTimeZoneNullEmptyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFDTimeZoneNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positiveFDTimeZonePrecedenceTest
   * 
   * @assertion_ids: JSTL:SPEC:57.6; JSTL:SPEC:57.6.1; JSTL:SPEC:57.6.2
   * 
   * @testStrategy: Validate that following order for determining the time zone
   * to be used when formatting date/times. In order of precedence: - If
   * present, use the value of the timeZone attribute. - If wrapped in a
   * <fmt:setTimeZone> action, use the timeZone of that action. - If no timeZone
   * attribute present, and not wrapped by the fmt:setTimeZone action, use the
   * value of the scoped attribute jakarta.servlet.jsp.jstl.fmt.timeZone
   */
  @Test
  public void positiveFDTimeZonePrecedenceTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFDTimeZonePrecedenceTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveFDTimeZonePrecedenceTest");
    invoke();
  }

  /*
   * @testName: positiveFDLocalizationContextTest
   * 
   * @assertion_ids: JSTL:SPEC:46.1; JSTL:SPEC:92; JSTL:SPEC:92.5
   * 
   * @testStrategy: Validate that the action can properly format a date based on
   * the default I18N localization context configuration variable
   * jakarta.servlet.jsp.jstl.fmt.localizationContext.
   */
  @Test
  public void positiveFDLocalizationContextTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFDLocalizationContextTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveFDLocalizationContextTest");
    TEST_PROPS.setProperty(REQUEST, "positiveFDLocalizationContextTest.jsp");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveFDLocalizationContextTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-US");
    invoke();
  }

  /*
   * @testName: positiveFDFallbackLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:50.2
   * 
   * @testStrategy: Validate that if no matching locale can be found, that the
   * fallback locale will be used.
   */
  @Test
  public void positiveFDFallbackLocaleTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFDFallbackLocaleTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveFDFallbackLocaleTest");
    TEST_PROPS.setProperty(REQUEST, "positiveFDFallbackLocaleTest.jsp");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveFDFallbackLocaleTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: xx-XX");
    invoke();
  }

  /*
   * @testName: negativeFDScopeNoVarTest
   * 
   * @assertion_ids: JSTL:SPEC:57.9
   * 
   * @testStrategy: validate that if var is not specified, but scope is, that a
   * fatal translation error occurs.
   */
  @Test
  public void negativeFDScopeNoVarTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativeFDScopeNoVarTest");
    TEST_PROPS.setProperty(REQUEST, "negativeFDScopeNoVarTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
