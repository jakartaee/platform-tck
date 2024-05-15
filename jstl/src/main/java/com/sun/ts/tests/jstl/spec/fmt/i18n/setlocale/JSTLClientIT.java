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



package com.sun.ts.tests.jstl.spec.fmt.i18n.setlocale;

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
    setContextRoot("/jstl_fmt_setlocale_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_fmt_setlocale_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_fmt_setlocale_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetLocaleOverrideTest.jsp")), "positiveSetLocaleOverrideTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetLocaleScopeTest.jsp")), "positiveSetLocaleScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetLocaleValueNullEmptyTest.jsp")), "positiveSetLocaleValueNullEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetLocaleValueTest.jsp")), "positiveSetLocaleValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetLocaleVariantIgnoreTest.jsp")), "positiveSetLocaleVariantIgnoreTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetLocaleVariantTest.jsp")), "positiveSetLocaleVariantTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveSetLocaleValueTest
   * 
   * @assertion_ids: JSTL:SPEC:28; JSTL:SPEC:28.1; JSTL:SPEC:28.1.1
   * 
   * @testStrategy: Validate value can accept both String representations of
   * locales as well as instances of java.util.Locale.
   */
  @Test
  public void positiveSetLocaleValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetLocaleValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetLocaleValueTest");
    invoke();
  }

  /*
   * @testName: positiveSetLocaleVariantTest
   * 
   * @assertion_ids: JSTL:SPEC:28.2; JSTL:SPEC:28.2.1
   * 
   * @testStrategy: Validate that variant can accept both dynamic and static
   * values as well as validate that the jakarta.servlet.jsp.jstl.fmt.locale
   * scoped variable is set with the proper value.
   */
  @Test
  public void positiveSetLocaleVariantTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetLocaleVariantTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetLocaleVariantTest");
    invoke();
  }

  /*
   * @testName: positiveSetLocaleValueNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:28.6
   * 
   * @testStrategy: Validate that if value is provided with a null or empty
   * value that the runtime default locale is used.
   */
  @Test
  public void positiveSetLocaleValueNullEmptyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetLocaleValueNullEmptyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetLocaleValueNullEmptyTest");
    invoke();
  }

  /*
   * @testName: positiveSetLocaleScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:28.1.2; JSTL:SPEC:28.3; JSTL:SPEC:28.3.1;
   * JSTL:SPEC:28.3.2; JSTL:SPEC:28.3.3; JSTL:SPEC:28.3.4; JSTL:SPEC:28.4
   * 
   * @testStrategy: Validate the behvior of the action with regards to scope. If
   * scope is specified, verify the jakarta.servlet.jsp.jstl.fmt.locale
   * configuration variable is in the expected scope. If scope is not specifed,
   * verify that it is in the page scope.
   */
  @Test
  public void positiveSetLocaleScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetLocaleScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetLocaleScopeTest");
    invoke();
  }

  /*
   * @testName: positiveSetLocaleOverrideTest
   * 
   * @assertion_ids: JSTL:SPEC:108
   * 
   * @testStrategy: Validate that browser-based locales from an HTTP client are
   * not considered if the jakarta.servlet. jsp.jstl.fmt.locale attribute is
   * present. The client will send it's preferred locales of fr and sw, but the
   * page will be set to en_US. The en resources bundle should be used and not
   * the sw bundle.
   */
  @Test
  public void positiveSetLocaleOverrideTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetLocaleOverrideTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveSetLocaleOverrideTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveSetLocaleOverrideTest.jsp?res=AlgoResources5&fall=de");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: sw");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveSetLocaleOverrideTest.gf");
    invoke();
  }

  /*
   * @testName: positiveSetLocaleVariantIgnoreTest
   * 
   * @assertion_ids: JSTL:SPEC:28.7
   * 
   * @testStrategy: Validate that if the value attribute is provided a Locale
   * object, and the variant attribute is specified (using an invalid value),
   * that the variant is ignored and the expected locale of en_US is returned by
   * the test.
   */
  @Test
  public void positiveSetLocaleVariantIgnoreTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetLocaleVariantIgnoreTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetLocaleVariantIgnoreTest");
    invoke();
  }
}
