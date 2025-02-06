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



package com.sun.ts.tests.jstl.spec.fmt.i18n.requestencoding;

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
    setContextRoot("/jstl_fmt_reqenc_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_fmt_reqenc_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_fmt_reqenc_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveContentTypeEncodingTest.jsp")), "positiveContentTypeEncodingTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveDefaultEncodingTest.jsp")), "positiveDefaultEncodingTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveReqEncodingValueTest.jsp")), "positiveReqEncodingValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveScopedAttrEncodingTest.jsp")), "positiveScopedAttrEncodingTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveReqEncodingValueTest
   * 
   * @assertion_ids: JSTL:SPEC:45; JSTL:SPEC:45.1; JSTL:SPEC:45.1.1
   * 
   * @testStrategy: Validate that setting the value of the requestEncoding
   * action correctly sets the encoding of the page.
   */
  @Test
  public void positiveReqEncodingValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveReqEncodingValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveReqEncodingValueTest");
    invoke();
  }

  /*
   * @testName: positiveContentTypeEncodingTest
   * 
   * @assertion_ids: JSTL:SPEC:45.4.1
   * 
   * @testStrategy : Validate that if a Content-Type header is present in the
   * client's request, the request encoding is properly set to the value
   * provided by the header. Validation will be by calling
   * getCharacterEncoding() against the request object after the action has been
   * called.
   */
  @Test
  public void positiveContentTypeEncodingTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveContentTypeEncodingTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveContentTypeEncodingTest");
    TEST_PROPS.setProperty(REQUEST, "positiveContentTypeEncodingTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS,
        "Content-Type: text/plain; charset=UTF-8");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveContentTypeEncodingTest.gf");
    invoke();
  }

  /*
   * @testName: positiveScopedAttrEncodingTest
   * 
   * @assertion_ids: JSTL:SPEC:45.4.2
   * 
   * @testStrategy: Validate that if no Content-Type header is sent by the
   * client, that the value of the scoped variable,
   * jakarta.servlet.jsp.jstl.fmt.request.charset, is used to set the character
   * encoding of the request. Validation will be by calling
   * getCharacterEncoding() against the request object after the action has been
   * called.
   */
  @Test
  public void positiveScopedAttrEncodingTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveScopedAttrEncodingTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveScopedAttrEncodingTest");
    invoke();
  }

  /*
   * @testName: positiveDefaultEncodingTest
   * 
   * @assertion_ids: JSTL:SPEC:45.4.3
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
    TEST_PROPS.setProperty(STANDARD, "positiveDefaultEncodingTest");
    invoke();
  }
}
