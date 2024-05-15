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



package com.sun.ts.tests.jstl.spec.fmt.i18n.message;

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
    setContextRoot("/jstl_fmt_message_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_fmt_message_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_fmt_message_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/localeSupportTest.jsp")), "localeSupportTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeLocaleSupportTest.jsp")), "negativeLocaleSupportTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMessageBundleOverrideTest.jsp")), "positiveMessageBundleOverrideTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMessageBundleTest.jsp")), "positiveMessageBundleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMessageCompoundNoParamTest.jsp")), "positiveMessageCompoundNoParamTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMessageFallbackLocaleTest.jsp")), "positiveMessageFallbackLocaleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMessageKeyBodyTest.jsp")), "positiveMessageKeyBodyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMessageKeyNotFoundTest.jsp")), "positiveMessageKeyNotFoundTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMessageKeyNullEmptyTest.jsp")), "positiveMessageKeyNullEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMessageKeyParamBodyTest.jsp")), "positiveMessageKeyParamBodyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMessageKeyTest.jsp")), "positiveMessageKeyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMessageLocaleConfigurationTest.jsp")), "positiveMessageLocaleConfigurationTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMessageNoLocalizationContextTest.jsp")), "positiveMessageNoLocalizationContextTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMessageParamBodyTest.jsp")), "positiveMessageParamBodyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMessagePrefixTest.jsp")), "positiveMessagePrefixTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMessageScopeTest.jsp")), "positiveMessageScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveMessageVarTest.jsp")), "positiveMessageVarTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveMessageKeyTest
   * 
   * @assertion_ids: JSTL:SPEC:44; JSTL:SPEC:44.1; JSTL:SPEC:44.1.1
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
   * @testName: positiveMessageBundleTest
   * 
   * @assertion_ids: JSTL:SPEC:44.2; JSTL:SPEC:44.2.1
   * 
   * @testStrategy: Validate that the message action can lookup a localized
   * message from a ResourceBundle specified via the bundle attribute.
   */
  @Test
  public void positiveMessageBundleTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveMessageBundleTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveMessageBundleTest");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveMessageBundleTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveMessageBundleTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveMessageVarTest
   * 
   * @assertion_ids: JSTL:SPEC:44.3; JSTL:SPEC:44.4.6
   * 
   * @testStrategy: Validate that if var is present, the message is not written
   * to the current JspWriter, but is exported to the PageContext and associated
   * with the name specified by var.
   */
  @Test
  public void positiveMessageVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveMessageVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveMessageVarTest");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveMessageVarTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveMessageVarTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveMessageScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:44.4; JSTL:SPEC:44.4.1; JSTL:SPEC:44.4.2;
   * JSTL:SPEC:44.4.3; JSTL:SPEC:44.4.4; JSTL:SPEC:44.4.6
   * 
   * @testStrategy: Validate that the scope attribute affects the scope to which
   * var is exported. This will also validate that if var is specified, but
   * scope is not, var is exported to the page scope by default.
   */
  @Test
  public void positiveMessageScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveMessageScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveMessageScopeTest");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveMessageScopeTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveMessageScopeTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveMessageKeyNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:44.4; JSTL:SPEC:44.4.1; JSTL:SPEC:44.4.2
   * 
   * @testStrategy: Validate that if the key attribute evaluates to null, or is
   * empty (""), that '??????' is written to the current JspWriter.
   */
  @Test
  public void positiveMessageKeyNullEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveMessageKeyNullEmptyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveMessageKeyNullEmptyTest");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveMessageKeyNullEmptyTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveMessageKeyNullEmptyTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveMessageKeyNotFoundTest
   * 
   * @assertion_ids: JSTL:SPEC:44.11
   * 
   * @testStrategy: Validate that if the key specified by the message action
   * does not exist in the ResourceBundle being used, the '???<key>???' is
   * written to the current JspWriter (<key> is the unknown key).
   */
  @Test
  public void positiveMessageKeyNotFoundTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveMessageKeyNotFoundTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveMessageKeyNotFoundTest");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveMessageKeyNotFoundTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveMessageKeyNotFoundTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveMessageKeyBodyTest
   * 
   * @assertion_ids: JSTL:SPEC:44.5
   * 
   * @testStrategy: Validate that the message action can properly look up a
   * localized message when the key is provided as body content to the action.
   */
  @Test
  public void positiveMessageKeyBodyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveMessageKeyBodyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveMessageKeyBodyTest");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveMessageKeyBodyTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveMessageKeyBodyTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveMessageKeyParamBodyTest
   * 
   * @assertion_ids: JSTL:SPEC:44.8
   * 
   * @testStrategy: Validate that the message action can properly lookup and
   * display compound messages with the key and <fmt:param> subtags provided as
   * body content to the action.
   */
  @Test
  public void positiveMessageKeyParamBodyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveMessageKeyParamBodyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveMessageKeyParamBodyTest");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveMessageKeyParamBodyTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveMessageKeyParamBodyTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveMessageParamBodyTest
   * 
   * @assertion_ids: JSTL:SPEC:44.8
   * 
   * @testStrategy: Validate the the action can properly perform parametric
   * replacement of parameters provided as body content against a compound
   * message.
   */
  @Test
  public void positiveMessageParamBodyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveMessageParamBodyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveMessageParamBodyTest");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveMessageParamBodyTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveMessageParamBodyTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveMessageCompoundNoParamTest
   * 
   * @assertion_ids: JSTL:SPEC:44.9
   * 
   * @testStrategy: Validate that if a message is compound and no param subtags
   * are provided, the message is returned as is.
   */
  @Test
  public void positiveMessageCompoundNoParamTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveMessageCompoundNoParamTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveMessageCompoundNoParamTest");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveMessageCompoundNoParamTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveMessageCompoundNoParamTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveMessagePrefixTest
   * 
   * @assertion_ids: JSTL:SPEC:44.6
   * 
   * @testStrategy: Validate that a message action will use the prefix specified
   * by a parent bundle action when looking up messsage keys.
   */
  @Test
  public void positiveMessagePrefixTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveMessagePrefixTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveMessagePrefixTest");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveMessagePrefixTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveMessagePrefixTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveMessageBundleOverrideTest
   * 
   * @assertion_ids: JSTL:SPEC:106.1
   * 
   * @testStrategy: Validate that if a message action has a bundle as a parent
   * tag and the same message action has the bundle attribute specified, that
   * the key look up will be performed against the resource bundle specified by
   * the bundle attribute.
   */
  @Test
  public void positiveMessageBundleOverrideTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveMessageBundleOverrideTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveMessageBundleOverrideTest");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveMessageBundleOverrideTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveMessageBundleOverrideTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveMessageNoLocalizationContextTest
   * 
   * @assertion_ids: JSTL:SPEC:44.14
   * 
   * @testStrategy: Validate that if a LocalizationContext, provided to the
   * message action has a null resource bundle, the message displayed is
   * ???<key>???.
   */
  @Test
  public void positiveMessageNoLocalizationContextTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveMessageNoLocalizationContextTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD,
        "positiveMessageNoLocalizationContextTest");
    invoke();
  }

  /*
   * @testName: positiveMessageLocaleConfigurationTest
   * 
   * @assertion_ids: JSTL:SPEC:106.3; JSTL:SPEC:106.3.1
   * 
   * @testStrategy: Validate that if the configuration variables
   * jakarta.servlet.jsp.jstl.fmt.locale and
   * jakarta.servlet.jsp.jstl.fmt.localizationContext are available, and the
   * message action is not nested within a bundle action, the message can
   * properly be localized. To try to throw a wrench in things, the client will
   * send a preferred locale across the wire that, if used, will not resolve to
   * any ResourceBundle (no fallback defined).
   */
  @Test
  public void positiveMessageLocaleConfigurationTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveMessageLocaleConfigurationTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveMessageLocaleConfigurationTest");
    // TEST_PROPS.setProperty(GOLDENFILE,
    //     "positiveMessageLocaleConfigurationTest.gf");
    TEST_PROPS.setProperty(REQUEST,
        "positiveMessageLocaleConfigurationTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: ja");
    invoke();
  }

  /*
   * @testName: positiveMessageFallbackLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:106.3; JSTL:SPEC:106.3.2; JSTL:SPEC:26.2.2
   * 
   * @testStrategy: Validate that if the default localization context cannot
   * determine a locale, that the fallback locale will be used and allow the
   * message action to properly localize a message. To try to throw a wrench in
   * things, the client will send a preferred locale across the wire that, if
   * used, will not resolve to any resource bundle (no fallback defined).
   * Additionally verify that the fallbackLocale variable can be configured
   * using a String representation of a locale as well as an instance of Locale.
   */
  @Test
  public void positiveMessageFallbackLocaleTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveMessageFallbackLocaleTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveMessageFallbackLocaleTest");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveMessageFallbackLocaleTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveMessageFallbackLocaleTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: ja");
    invoke();
  }

  /*
   * @testName: localeSupportTest
   * 
   * @assertion_ids: JSTL:SPEC:107.1; JSTL:SPEC:107.1.1; JSTL:SPEC:107.2;
   * JSTL:SPEC:107.2.1; JSTL:SPEC:107.3; JSTL:SPEC:107.4
   * 
   * @test_Strategy: validates jakarta.servlet.jsp.jstl.fmt.LocaleSupport for
   * static getLocalizedMessage() methods.
   */

  public void localeSupportTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_fmt_message_web/localeSupportTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    TEST_PROPS.setProperty(SEARCH_STRING, "(pageContext,key):|en message|"
        + "(pageContext,key,basename):|en message|"
        + "(pageContext,key,args):|param1: Monday, param2: Tuesday|"
        + "(pageContext,key,args,basename):|param1: Monday, param2: Tuesday");
    invoke();
  }

  /*
   * @testName: negativeLocaleSupportTest
   * 
   * @assertion_ids: JSTL:SPEC:107.1.2; JSTL:SPEC:107.2.2
   * 
   * @test_Strategy: validates jakarta.servlet.jsp.jstl.fmt.LocaleSupport for
   * static getLocalizedMessage() methods.
   */

  public void negativeLocaleSupportTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jstl_fmt_message_web/negativeLocaleSupportTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "(pageContext,key):|???nkey???|"
            + "(pageContext,key,basename):|???mkey???|"
            + "(pageContext,key,args):|???nkey???|"
            + "(pageContext,key,args,basename):|???pkey???");
    invoke();
  }

}
