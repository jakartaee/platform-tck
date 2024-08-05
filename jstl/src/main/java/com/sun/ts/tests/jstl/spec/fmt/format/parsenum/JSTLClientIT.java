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



package com.sun.ts.tests.jstl.spec.fmt.format.parsenum;

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
    setContextRoot("/jstl_fmt_psnum_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_fmt_psnum_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_fmt_psnum_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativePNScopeNoVarTest.jsp")), "negativePNScopeNoVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativePNUnableToParseValueTest.jsp")), "negativePNUnableToParseValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePNBodyValueTest.jsp")), "positivePNBodyValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePNFallbackLocaleTest.jsp")), "positivePNFallbackLocaleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePNIntegerOnlyTest.jsp")), "positivePNIntegerOnlyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePNLocalizationContextTest.jsp")), "positivePNLocalizationContextTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePNParseLocaleNullEmptyTest.jsp")), "positivePNParseLocaleNullEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePNParseLocaleTest.jsp")), "positivePNParseLocaleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePNPatternTest.jsp")), "positivePNPatternTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePNScopeTest.jsp")), "positivePNScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePNTypeTest.jsp")), "positivePNTypeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePNValueNullEmptyTest.jsp")), "positivePNValueNullEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePNValueTest.jsp")), "positivePNValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positivePNVarTest.jsp")), "positivePNVarTest.jsp");
    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positivePNValueTest
   * 
   * @assertion_ids: JSTL:SPEC:56; JSTL:SPEC:56.1; JSTL:SPEC:56.1.1
   * 
   * @testStrategy: Validate the action can correctly parse dynamic and static
   * values provided to the value attribute.
   */
  @Test
  public void positivePNValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePNValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positivePNValueTest");
    invoke();
  }

  /*
   * @testName: positivePNTypeTest
   * 
   * @assertion_ids: JSTL:SPEC:56.2; JSTL:SPEC:56.2.1; JSTL:SPEC:56.2.2;
   * JSTL:SPEC:56.2.3; JSTL:SPEC:56.2.4; JSTL:SPEC:56.2.6
   * 
   * @testStrategy: Validate the action can properly parse numbers provided in
   * the three supported types (number, currency, percent). Also verify that if
   * type is not provided, that the value is parsed as a number.
   */
  @Test
  public void positivePNTypeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePNTypeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positivePNTypeTest");
    invoke();
  }

  /*
   * @testName: positivePNPatternTest
   * 
   * @assertion_ids: JSTL:SPEC:56.3; JSTL:SPEC:56.3.1; JSTL:SPEC:56.20
   * 
   * @testStrategy: Validate that if a pattern is specified that it will be
   * applied to the parsed value no matter the value of type.
   */
  @Test
  public void positivePNPatternTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePNPatternTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positivePNPatternTest");
    invoke();
  }

  /*
   * @testName: positivePNParseLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:56.4; JSTL:SPEC:56.4.1
   * 
   * @testStrategy: Validate that the action is properly able to parse the
   * provided value based of the parseLocale attribute. Also validate that the
   * attribute can accept either Strings or Locale objects. Additionally
   * validate that the presence of the parseLocale attribute will override the
   * locale of the page.
   */
  @Test
  public void positivePNParseLocaleTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePNParseLocaleTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positivePNParseLocaleTest");
    invoke();
  }

  /*
   * @testName: positivePNIntegerOnlyTest
   * 
   * @assertion_ids: JSTL:SPEC:56.5; JSTL:SPEC:56.5.1; JSTL:SPEC:56.5.3
   * 
   * @testStrategy: Validate that if integerOnly is true, only the integer
   * portion of the value is returned parsed. Also validate that if integerOnly
   * is not specified, that the entire value will be returned parsed (default
   * value of false).
   */
  @Test
  public void positivePNIntegerOnlyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePNIntegerOnlyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positivePNIntegerOnlyTest");
    invoke();
  }

  /*
   * @testName: positivePNVarTest
   * 
   * @assertion_ids: JSTL:SPEC:56.6; JSTL:SPEC:56.6.1
   * 
   * @testStrategy: Validate that if var is specifed the parsed result is
   * exported and is of type java.lang.Number.
   */
  @Test
  public void positivePNVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePNVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positivePNVarTest");
    invoke();
  }

  /*
   * @testName: positivePNScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:56.7; JSTL:SPEC:56.7.1; JSTL:SPEC:56.7.2;
   * JSTL:SPEC:56.7.3; JSTL:SPEC:56.7.4; JSTL:SPEC:56.10
   * 
   * @testStrategy: Validate that var is exported to the proper scope as defined
   * by the value of the scope attribute. Also verify that scope is not
   * specified, that var is exported to the page scope.
   */
  @Test
  public void positivePNScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePNScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positivePNScopeTest");
    invoke();
  }

  /*
   * @testName: positivePNBodyValueTest
   * 
   * @assertion_ids: JSTL:SPEC:56.11
   * 
   * @testStrategy: Validate that the value to be parsed can be provided as body
   * content to the action.
   */
  @Test
  public void positivePNBodyValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePNBodyValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positivePNBodyValueTest");
    invoke();
  }

  /*
   * @testName: positivePNValueNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:56.9
   * 
   * @testStrategy: Validate that if value is null or empty, the variable
   * referenced by var will be removed from the scoped specified in the action.
   * This will be verfied by setting an exported variable to the 4 given scopes,
   * and calling the action specifying these scopes with value null or empty.
   * After the action completes, use the checkScope tag to validate the variable
   * no longer exists.
   */
  @Test
  public void positivePNValueNullEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePNValueNullEmptyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positivePNValueNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positivePNParseLocaleNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:56.13
   * 
   * @testStrategy: Validate that if the parseLocale attribute is null or empty,
   * the action behaves as of the attribute was not specified. Since setLocale
   * was used, the parse action should use the locale from the locale
   * configuration variable.
   */
  @Test
  public void positivePNParseLocaleNullEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePNParseLocaleNullEmptyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positivePNParseLocaleNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positivePNLocalizationContextTest
   * 
   * @assertion_ids: JSTL:SPEC:46.2
   * 
   * @testStrategy: Validate that the action can properly format a date based on
   * the default I18N localization context configuration variable
   * jakarta.servlet.jsp.jstl.fmt.localizationContext.
   */
  @Test
  public void positivePNLocalizationContextTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePNLocalizationContextTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positivePNLocalizationContextTest");
    TEST_PROPS.setProperty(REQUEST, "positivePNLocalizationContextTest.jsp");
    // TEST_PROPS.setProperty(GOLDENFILE, "positivePNLocalizationContextTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-US");
    invoke();
  }

  /*
   * @testName: positivePNFallbackLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:50.2
   * 
   * @testStrategy: Validate that if no matching locale can be found, that the
   * fallback locale will be used.
   */
  @Test
  public void positivePNFallbackLocaleTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positivePNFallbackLocaleTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positivePNFallbackLocaleTest");
    TEST_PROPS.setProperty(REQUEST, "positivePNFallbackLocaleTest.jsp");
    // TEST_PROPS.setProperty(GOLDENFILE, "positivePNFallbackLocaleTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: xx-XX");
    invoke();
  }

  /*
   * @testName: negativePNUnableToParseValueTest
   * 
   * @assertion_ids: JSTL:SPEC:56.14; JSTL:SPEC:56.14.1
   * 
   * @testStrategy: Validate that if the formatting action is unable to parse
   * the String value provided, the exception is rethrown as a JspException,
   * with the rethrown unparsable value in the exception text and the original
   * exception set as the root cause of the JspException.
   */
  @Test
  public void negativePNUnableToParseValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativePNUnableToParseValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativePNUnableToParseValueTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: negativePNScopeNoVarTest
   * 
   * @assertion_ids: JSTL:SPEC:56.8
   * 
   * @testStrategy: validate that if var is not specified, but scope is, that a
   * fatal translation error occurs.
   */
  @Test
  public void negativePNScopeNoVarTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "negativePNScopeNoVarTest");
    TEST_PROPS.setProperty(REQUEST, "negativePNScopeNoVarTest.jsp");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
