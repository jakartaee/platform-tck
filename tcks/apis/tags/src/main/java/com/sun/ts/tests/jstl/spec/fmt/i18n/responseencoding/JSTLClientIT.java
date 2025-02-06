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



package com.sun.ts.tests.jstl.spec.fmt.i18n.responseencoding;

import java.io.IOException;
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
    setContextRoot("/jstl_fmt_resenc_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_fmt_resenc_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_fmt_resenc_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveResponseEncodingTest.jsp")), "positiveResponseEncodingTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveResponseSetCharEncodingAttrTest.jsp")), "positiveResponseSetCharEncodingAttrTest.jsp");
    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveResponseEncodingTest
   * 
   * @assertion_ids: JSTL:SPEC:27
   * 
   * @testStrategy: Validate that actions that establish an I18N localization
   * context properly call ServletResponse.setLocale(). Actions that do this
   * are: <fmt:setLocale> <fmt:message> <fmt:bundle> <fmt:setBundle>
   */
  @Test
  public void positiveResponseEncodingTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "positiveResponseEncodingTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveResponseEncodingTest.jsp?action=locale&type=rt");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "setlocale: en");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveResponseEncodingTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveResponseEncodingTest.jsp?action=bundle&type=rt");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "setlocale: en");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveResponseEncodingTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveResponseEncodingTest.jsp?action=setbundle&type=rt");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "setlocale: en");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME, "positiveResponseEncodingTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveResponseEncodingTest.jsp?action=message&type=rt");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "setlocale: en");
    invoke();
  }

  /*
   * @testName: positiveResponseSetCharEncodingAttrTest
   * 
   * @assertion_ids: JSTL:SPEC:27.1
   * 
   * @testStrategy: Validate that the actions that initialize an I18N
   * localization context properly sets
   * jakarta.servlet.jsp.jstl.fmt.request.charset session attribute. Actions that
   * do this are: <fmt:setLocale> <fmt:message> <fmt:bundle> <fmt:setBundle>
   */
  @Test
  public void positiveResponseSetCharEncodingAttrTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME,
        "positiveResponseSetCharEncodingAttrTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveResponseSetCharEncodingAttrTest.jsp?action=locale");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "charset: attribute set|charencoding: called");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME,
        "positiveResponseSetCharEncodingAttrTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveResponseSetCharEncodingAttrTest.jsp?action=bundle");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "charset: attribute set|charencoding: called");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME,
        "positiveResponseSetCharEncodingAttrTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveResponseSetCharEncodingAttrTest.jsp?action=setbundle");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "charset: attribute set|charencoding: called");
    invoke();
    TEST_PROPS.setProperty(TEST_NAME,
        "positiveResponseSetCharEncodingAttrTest");
    TEST_PROPS.setProperty(REQUEST,
        "positiveResponseSetCharEncodingAttrTest.jsp?action=message");
    TEST_PROPS.setProperty(EXPECTED_HEADERS,
        "charset: attribute set|charencoding: called");
    invoke();
  }
}
