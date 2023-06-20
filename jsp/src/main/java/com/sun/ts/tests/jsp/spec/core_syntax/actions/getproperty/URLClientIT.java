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

/*
 * $Id$
 */

package com.sun.ts.tests.jsp.spec.core_syntax.actions.getproperty;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import java.io.IOException;
import java.io.InputStream;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;

@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {

  private static final String CONTEXT_ROOT = "/jsp_coresyntx_act_getproperty_web";

  public static String packagePath = URLClientIT.class.getPackageName().replace(".", "/");

  public URLClientIT() throws Exception {
    setup();

    setGeneralURI("/jsp/spec/core_syntax/actions/getproperty");
    setContextRoot("/jsp_coresyntx_act_getproperty_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_coresyntx_act_getproperty_web.war");
    archive.addClasses(StringBean.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_coresyntx_act_getproperty_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/positiveGetProps.jsp")), "positiveGetProps.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/nullPropertyValueTest.jsp")), "nullPropertyValueTest.jsp");

    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: positiveGetPropsTest
   * 
   * @assertion_ids: JSP:SPEC:163.1;JSP:SPEC:163.2;JSP:SPEC:163.4
   * 
   * @test_Strategy: Create a bean using jsp:useBean tag, use jsp:getProperty to
   * access and validate the property.
   */

  @Test
  public void positiveGetPropsTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/positiveGetProps.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "positiveGetProps");
    invoke();
  }

  /*
   * @testName: nullPropertyValueTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: if the target property value is null, "null" rather than ""
   * must be outputed, done as in println method.
   */

  @Test
  public void nullPropertyValueTest() throws Exception {
    String testName = "nullPropertyValueTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "null");
    invoke();
  }

}
