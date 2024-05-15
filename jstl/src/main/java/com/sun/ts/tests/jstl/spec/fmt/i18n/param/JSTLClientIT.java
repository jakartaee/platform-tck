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



package com.sun.ts.tests.jstl.spec.fmt.i18n.param;

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
    setContextRoot("/jstl_fmt_param_web");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jstl_fmt_param_web.war");
    archive.setWebXML(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/jstl_fmt_param_web.xml"));
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamValueBodyTest.jsp")), "positiveParamValueBodyTest.jsp");
    archive.add(new UrlAsset(JSTLClientIT.class.getClassLoader().getResource(packagePath+"/positiveParamValueTest.jsp")), "positiveParamValueTest.jsp");

    archive.addAsLibrary(getCommonJarArchive());

    return archive;
  }

  /*
   * @testName: positiveParamValueTest
   * 
   * @assertion_ids: JSTL:SPEC:25; JSTL:SPEC:25.2; JSTL:SPEC:25.2.1
   * 
   * @testStrategy: Validate that parametric replacement occurs when param
   * subtags are specified with either dynamic or static values.
   */
  @Test
  public void positiveParamValueTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamValueTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveParamValueTest");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveParamValueTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveParamValueTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }

  /*
   * @testName: positiveParamValueBodyTest
   * 
   * @assertion_ids: JSTL:SPEC:25.5
   * 
   * @testStrategy: Validate that the value of the parameter can be provided as
   * body content to the action.
   */
  @Test
  public void positiveParamValueBodyTest() throws Exception {
    InputStream gfStream = JSTLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveParamValueBodyTest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, "positiveParamValueBodyTest");
    // TEST_PROPS.setProperty(GOLDENFILE, "positiveParamValueBodyTest.gf");
    TEST_PROPS.setProperty(REQUEST, "positiveParamValueBodyTest.jsp");
    TEST_PROPS.setProperty(REQUEST_HEADERS, "Accept-Language: en");
    invoke();
  }
}
