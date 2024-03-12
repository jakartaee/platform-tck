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



package com.sun.ts.tests.jstl.spec.core.urlresource.url;

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
    setContextRoot("/jstl_core_url_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_core_url_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_core_url_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUrlDisplayUrlTest.jsp")), "positiveUrlDisplayUrlTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUrlValueVarTest.jsp")), "positiveUrlValueVarTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUrlScopeTest.jsp")), "positiveUrlScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUrlNoCharEncodingTest.jsp")), "positiveUrlNoCharEncodingTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUrlParamsBodyTest.jsp")), "positiveUrlParamsBodyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUrlAbsUrlNotRewrittenTest.jsp")), "positiveUrlAbsUrlNotRewrittenTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeUrlExcBodyContentTest.jsp")), "negativeUrlExcBodyContentTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/negativeUrlContextUrlInvalidValueTest.jsp")), "negativeUrlContextUrlInvalidValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUrlContextTest.jsp")), "positiveUrlContextTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveUrlRelUrlRewrittenTest.jsp")), "positiveUrlRelUrlRewrittenTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveUrlDisplayUrlTest
   * 
   * @assertion_ids: JSTL:SPEC:24.7
   * 
   * @testStrategy: Validate that if var is not specified, the resulting value
   * of the url action is written to the current JspWriter.
   */
  @Test
  public void positiveUrlDisplayUrlTest() throws Exception {
    TEST_PROPS.setProperty(TEST_NAME, "positiveUrlDisplayUrlTest");
    TEST_PROPS.setProperty(REQUEST, "positiveUrlDisplayUrlTest.jsp");
    TEST_PROPS.setProperty(SEARCH_STRING, "/jstl_core_url_web/jstl");
    invoke();
  }

  /*
   * @testName: positiveUrlValueVarTest
   * 
   * @assertion_ids: JSTL:SPEC:24.2; JSTL:SPEC:24.6
   * 
   * @testStrategy: Validate that the result of encoding the value of the url
   * attribute is properly associated with a variable designated by var. Compare
   * the result with that returned by response.encodeUrl().
   */
  @Test
  public void positiveUrlValueVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUrlValueVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveUrlValueVarTest");
    invoke();
  }

  /*
   * @testName: positiveUrlScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:24.3; JSTL:SPEC:24.3.1; JSTL:SPEC:24.3.1;
   * JSTL:SPEC:24.3.2; JSTL:SPEC:24.3.3; JSTL:SPEC:24.3.4; JSTL:SPEC:24.3.5
   * 
   * @testStrategy: Validate the behavior of the scope attribute with respect to
   * var, both when scope is explicitly defined and when not defined.
   */
  @Test
  public void positiveUrlScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUrlScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveUrlScopeTest");
    invoke();
  }

  /*
   * @testName: positiveUrlNoCharEncodingTest
   * 
   * @assertion_ids: JSTL:SPEC:24.12
   * 
   * @testStrategy: Validate that if the URL to be encoded contains special
   * characters, that they are not encoded by the action.
   */
  @Test
  public void positiveUrlNoCharEncodingTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUrlNoCharEncodingTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveUrlNoCharEncodingTest");
    invoke();
  }

  /*
   * @testName: positiveUrlParamsBodyTest
   * 
   * @assertion_ids: JSTL:SPEC:25
   * 
   * @testStrategy: Validate the URL action can properly interact with nested
   * param subtags.
   */
  @Test
  public void positiveUrlParamsBodyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUrlParamsBodyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveUrlParamsBodyTest");
    invoke();
  }

  /*
   * @testName: positiveUrlAbsUrlNotRewrittenTest
   * 
   * @assertion_ids: JSTL:SPEC:24.5
   * 
   * @testStrategy: Validate that if an absolute URL is provided to the URL
   * action, the result is not rewritten.
   */
  @Test
  public void positiveUrlAbsUrlNotRewrittenTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveUrlAbsUrlNotRewrittenTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveUrlAbsUrlNotRewrittenTest");
    invoke();
  }

  /*
   * @testName: negativeUrlExcBodyContentTest
   * 
   * @assertion_ids: JSTL:SPEC:24.9
   * 
   * @testStrategy: Validate that an Exception is thrown if the body content of
   * the action causes an exception.
   */
  @Test
  public void negativeUrlExcBodyContentTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeUrlExcBodyContentTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeUrlExcBodyContentTest");
    invoke();
  }

  /*
   * @testName: negativeUrlContextUrlInvalidValueTest
   * 
   * @assertion_ids: JSTL:SPEC:24.11.4; JSTL:SPEC:24.11.2
   * 
   * @testStrategy: Validate that an Exception occurs if the value provided to
   * context or url (when context is specified) doesn't begin with a leading
   * slash.
   */
  @Test
  public void negativeUrlContextUrlInvalidValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/negativeUrlContextUrlInvalidValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "negativeUrlContextUrlInvalidValueTest");
    invoke();
  }
}
