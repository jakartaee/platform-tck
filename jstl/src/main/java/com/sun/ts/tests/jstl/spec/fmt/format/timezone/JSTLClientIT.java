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



package com.sun.ts.tests.jstl.spec.fmt.format.timezone;

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
    setContextRoot("/jstl_fmt_tz_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_fmt_tz_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_fmt_tz_web.xml"));
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTimezoneValueNullEmptyTest.jsp")), "positiveTimezoneValueNullEmptyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveTimezoneValueTest.jsp")), "positiveTimezoneValueTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveTimezoneValueTest
   * 
   * @assertion_ids: JSTL:SPEC:54; JSTL:SPEC:54.1; JSTL:SPEC:54.1.1;
   * JSTL:SPEC:54.1.2; JSTL:SPEC:54.1.3
   * 
   * @testStrategy: Validate that the value attribute can accept both static
   * values as well as three letter timezones (ex. PST) or fully qualified
   * values (ex. America/Los_Angeles).
   */
  @Test
  public void positiveTimezoneValueTest() throws Exception {
    TEST_PROPS.setProperty(STANDARD, "positiveTimezoneValueTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    InputStream gfStream;
    if (isJavaVersion20OrGreater()) {
       gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTimezoneValueTestJava20Plus.gf");
    } else {
      gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTimezoneValueTest.gf");

    }
    setGoldenFileStream(gfStream);
    invoke();
  }

  /*
   * @testName: positiveTimezoneValueNullEmptyTest
   * 
   * @assertion_ids: JSTL:SPEC:54.7
   * 
   * @testStrategy: Validate that if the value attribute is null or emtpy, the
   * GMT+0 timezone is used by the formatting actions that rely on timezone.
   */
  @Test
  public void positiveTimezoneValueNullEmptyTest() throws Exception {
    TEST_PROPS.setProperty(STANDARD, "positiveTimezoneValueNullEmptyTest");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    InputStream gfStream;
    if (isJavaVersion20OrGreater()) {
       gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTimezoneValueNullEmptyTestJava20Plus.gf");
    } else {
        gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveTimezoneValueNullEmptyTest.gf");
    }
    setGoldenFileStream(gfStream);
    invoke();
  }

}
