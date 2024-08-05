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



package com.sun.ts.tests.jstl.spec.fmt.format.localecontext;

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
    setContextRoot("/jstl_fmt_locctx_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_fmt_locctx_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_fmt_locctx_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFormatLocalizationContextBrowserLocaleTest.jsp")), "positiveFormatLocalizationContextBrowserLocaleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFormatLocalizationContextBrowserLocaleTestJava20Plus.jsp")), "positiveFormatLocalizationContextBrowserLocaleTestJava20Plus.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFormatLocalizationContextBundleTest.jsp")), "positiveFormatLocalizationContextBundleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFormatLocalizationContextBundleTestJava20Plus.jsp")), "positiveFormatLocalizationContextBundleTestJava20Plus.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFormatLocalizationContextI18NTest.jsp")), "positiveFormatLocalizationContextI18NTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFormatLocalizationContextI18NTestJava20Plus.jsp")), "positiveFormatLocalizationContextI18NTestJava20Plus.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFormatLocalizationContextLocaleTest.jsp")), "positiveFormatLocalizationContextLocaleTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveFormatLocalizationContextLocaleTestJava20Plus.jsp")), "positiveFormatLocalizationContextLocaleTestJava20Plus.jsp");
    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveFormatLocalizationContextBundleTest
   * 
   * @assertion_ids: JSTL:SPEC:46.1
   * 
   * @testStrategy: If a formatting action is nested within a <fmt:bundle>
   * action, the formatting action will use the locale of the parent action.
   * This will be verified by Setting the
   * jakarta.servlet.jsp.jstl.fmt.localizationContext scoped variable. The
   * basename attribute will refer to an fr_FR bundle. The fmt:setBundle action
   * that encloses a formatting action will have a single en_US resource bundle,
   * so the resulting locale will be en_US. If the action chooses the proper
   * locale, no parse exception will occur.
   */
  @Test
  public void positiveFormatLocalizationContextBundleTest() throws Exception {
    InputStream gfStream;
    TEST_PROPS.setProperty(TEST_NAME,
        "positiveFormatLocalizationContextBundleTest");
    if (isJavaVersion20OrGreater()) {
        TEST_PROPS.setProperty(REQUEST,
            "positiveFormatLocalizationContextBundleTestJava20Plus.jsp");
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFormatLocalizationContextBundleTestJava20Plus.gf");
    } else {
        TEST_PROPS.setProperty(REQUEST,
            "positiveFormatLocalizationContextBundleTest.jsp");
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFormatLocalizationContextBundleTest.gf");    
    }
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-US");
    invoke();
  }

  /*
   * @testName: positiveFormatLocalizationContextI18NTest
   * 
   * @assertion_ids: JSTL:SPEC:46.2
   * 
   * @testStrategy: If the jakarta.servlet.jsp.jstl.fmt.localizationContext
   * attribute is present, and the formatting action is not nested in a
   * <fmt:bundle> action, the basename attribute will take precedence over the
   * jakarta.servlet.jsp.jstl.fmt.locale scoped attribute. This will be verified
   * by setting the localizationContext attribute so that it will resolve to an
   * en_US bundle, and the set the locale attribute to de_DE. If the formatting
   * action correctly uses the locale from the basename attribute, then no parse
   * exception will occur.
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
  
    // TEST_PROPS.setProperty(GOLDENFILE,
    //     "positiveFormatLocalizationContextI18NTest.gf");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-US");
    invoke();
  }

  /*
   * @testName: positiveFormatLocalizationContextLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:105.1
   * 
   * @testStrategy: If the jakarta.servlet.jsp.jstl.fmt.locale attribute is set,
   * the locale specified by this attribute will be used vs. those provided by
   * the browser (i.e. preferred locales from an Accept-Language header). This
   * will be verified by setting the locale attribute to en_US and the client's
   * preferred locale to de_DE. If the locale attribute is used, no parse
   * exception will occur.
   */
  @Test
  public void positiveFormatLocalizationContextLocaleTest() throws Exception {
    InputStream gfStream;
    if (isJavaVersion20OrGreater()) {
      TEST_PROPS.setProperty(REQUEST,
          "positiveFormatLocalizationContextLocaleTestJava20Plus.jsp");
      gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFormatLocalizationContextLocaleTestJava20Plus.gf");
    } else {
        TEST_PROPS.setProperty(REQUEST,
            "positiveFormatLocalizationContextLocaleTest.jsp");
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFormatLocalizationContextLocaleTest.gf");
    }
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME,
        "positiveFormatLocalizationContextLocaleTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: de-DE");
    invoke();
  }

  /*
   * @testName: positiveFormatLocalizationContextBrowserLocaleTest
   * 
   * @assertion_ids: JSTL:SPEC:46.4
   * 
   * @testStrategy: If the formatting action is not wrapped in a <fmt:bundle>
   * action, nor are the jakarta.servlet.jsp.jstl.fmt.localizationContext or
   * jakarta.servlet.jsp.jstl.fmt.locale attributes set, the formatting locale
   * will be based on the preferred locales provided by the client (via the
   * Accept-Language request header).
   */
  @Test
  public void positiveFormatLocalizationContextBrowserLocaleTest()
      throws Exception {
    InputStream gfStream ;
    TEST_PROPS.setProperty(TEST_NAME,
        "positiveFormatLocalizationContextBrowserLocaleTest");
    // TEST_PROPS.setProperty(GOLDENFILE,
    //     "positiveFormatLocalizationContextBrowserLocaleTest.gf");
    if (isJavaVersion20OrGreater()) {
      TEST_PROPS.setProperty(REQUEST,
          "positiveFormatLocalizationContextBrowserLocaleTestJava20Plus.jsp");
      gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFormatLocalizationContextBrowserLocaleTestJava20Plus.gf");
    } else {
        TEST_PROPS.setProperty(REQUEST,
            "positiveFormatLocalizationContextBrowserLocaleTest.jsp");
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveFormatLocalizationContextBrowserLocaleTest.gf");
    }
    setGoldenFileStream(gfStream);

    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en-US");
    invoke();
  }

}
