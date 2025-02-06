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
 * @(#)JSTLClient.java	1.1 04/02/02
 */

package com.sun.ts.tests.jstl.spec.fmt.format.settimezone;

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
    setContextRoot("/jstl_fmt_stz_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_fmt_stz_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_fmt_stz_web.xml"));

    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetTimezoneAttrScopeTest.jsp")), "positiveSetTimezoneAttrScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetTimezoneScopeTest.jsp")), "positiveSetTimezoneScopeTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetTimezoneSetAttrTest.jsp")), "positiveSetTimezoneSetAttrTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetTimezoneValueNullEmptyTest.jsp")), "positiveSetTimezoneValueNullEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetTimezoneValueTest.jsp")), "positiveSetTimezoneValueTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveSetTimezoneVarTest.jsp")), "positiveSetTimezoneVarTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveSetTimezoneValueTest
   * 
   * @assertion_ids: JSTL:SPEC:93; JSTL:SPEC:93.1; JSTL:SPEC:93.1.1;
   * JSTL:SPEC:93.1.2; JSTL:SPEC:93.1.3; JSTL:SPEC:93.1.4; JSTL:SPEC:93.1.5;
   * JSTL:SPEC:93.1.6
   * 
   * @testStrategy: Validate that the value attribute can accept dynamic values
   * as well as three letter timezones (ex. PST) or fully qualified values (ex.
   * America/Los_Angeles).
   */
  @Test
  public void positiveSetTimezoneValueTest() throws Exception {
    InputStream gfStream;
    TEST_PROPS.setProperty(STANDARD, "positiveSetTimezoneValueTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    if (isJavaVersion20OrGreater()) {
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetTimezoneValueTestJava20Plus.gf");
    } else {
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetTimezoneValueTest.gf");
    }
    setGoldenFileStream(gfStream);
    invoke();
  }

  /*
   * @testName: positiveSetTimezoneVarTest
   * 
   * @assertion_ids: JSTL:SPEC:93.2; JSTL:SPEC:93.2.1
   * 
   * @testStrategy: Validate that a scoped variable of type java.util.TimeZone
   * is properly set and associated with the variable name specified by var.
   */
  @Test
  public void positiveSetTimezoneVarTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetTimezoneVarTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetTimezoneVarTest");
    invoke();
  }

  /*
   * @testName: positiveSetTimezoneScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:93.3; JSTL:SPEC:93.3.1; JSTL:SPEC:93.3.2;
   * JSTL:SPEC:93.3.3; JSTL:SPEC:93.3.4; JSTL:SPEC:93.3.5
   * 
   * @testStrategy: Validate that the through explicit use of the scope
   * attribute, var is exported to the appropriate scope. Additionally, validate
   * that if var is specified and scope is not, that var is exported to the page
   * scope by default.
   */
  @Test
  public void positiveSetTimezoneScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetTimezoneScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetTimezoneScopeTest");
    invoke();
  }

  /*
   * @testName: positiveSetTimezoneValueNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:93.7
   * 
   * @testStrategy: Validate that if the value attribute is null or empty, the
   * GMT+0 timezone is used by the formatting actions that rely on timezone.
   */
  @Test
  public void positiveSetTimezoneValueNullEmptyTest() throws Exception {
    InputStream gfStream;
    TEST_PROPS.setProperty(STANDARD, "positiveSetTimezoneValueNullEmptyTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    if (isJavaVersion20OrGreater()) {
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetTimezoneValueNullEmptyTestJava20Plus.gf");
    } else {
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetTimezoneValueNullEmptyTest.gf");
    }
    setGoldenFileStream(gfStream);
    invoke();
  }

  /*
   * @testName: positiveSetTimezoneSetAttrTest
   * 
   * @assertion_ids: JSTL:SPEC:93.4
   * 
   * @testStrategy: Validate that if var is not set, the scoped variable
   * jakarta.servlet.jsp.jstl.fmt.timeZone is properly set.
   */
  @Test
  public void positiveSetTimezoneSetAttrTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetTimezoneSetAttrTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetTimezoneSetAttrTest");
    invoke();
  }

  /*
   * @testName: positiveSetTimezoneAttrScopeTest
   * 
   * @assertion_ids: JSTL:SPEC:93.3; JSTL:SPEC:93.3.1; JSTL:SPEC:93.3.2;
   * JSTL:SPEC:93.3.3; JSTL:SPEC:93.3.4; JSTL:SPEC:93.3.5; JSTL:SPEC:93.4
   * 
   * @testStrategy: Validate that if var is not specified, but scope is, that
   * the scoped variable, jakarta.servlet.jsp.jstl.fmt.timeZone is exported to the
   * appropriate scope.
   */
  @Test
  public void positiveSetTimezoneAttrScopeTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveSetTimezoneAttrScopeTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveSetTimezoneAttrScopeTest");
    invoke();
  }

}
