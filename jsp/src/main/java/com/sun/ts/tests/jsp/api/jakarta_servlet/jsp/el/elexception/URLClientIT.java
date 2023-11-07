/*
 * Copyright (c) 2007, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.el.elexception;


import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {




  public URLClientIT() throws Exception {
    setup();
    setContextRoot("/jsp_elexc_web");
    setTestJsp("ELExceptionTest");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_elexc_web.war");
    archive.addClasses(
            com.sun.ts.tests.jsp.common.util.JspTestUtil.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_elexc_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/ELExceptionTest.jsp")), "ELExceptionTest.jsp");


    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: elExceptionDefaultCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:159
   * 
   * @test_Strategy: Validate default constructor of ELException
   */
  @Test
  public void elExceptionDefaultCtorTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "elExceptionDefaultCtorTest");
    invoke();
  }

  /*
   * @testName: elExceptionMessageCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:160
   * 
   * @test_Strategy: Validate contructor taking single string argument as the
   * message of the Exception.
   */
  @Test
  public void elExceptionMessageCtorTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "elExceptionMessageCtorTest");
    invoke();
  }

  /*
   * @testName: elExceptionCauseCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:161
   * 
   * @test_Strategy: Validate constructor taking a Throwable signifying the root
   * cause of the this ELException.
   */
  @Test
  public void elExceptionCauseCtorTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "elExceptionCauseCtorTest");
    invoke();
  }

  /*
   * @testName: elExceptionCauseMessageCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:162
   * 
   * @test_Strategy: Validate constructor taking both a message and a Throwable
   * signifying the root cause of the ELException.
   */
  @Test
  public void elExceptionCauseMessageCtorTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "elExceptionCauseMessageCtorTest");
    invoke();
  }

  /*
   * @testName: elExceptionGetRootCauseTest
   * 
   * @assertion_ids: JSP:JAVADOC:163
   * 
   * @test_Strategy: Validate the behavior of ELException.getRootCause().
   */
  @Test
  public void elExceptionGetRootCauseTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "elExceptionGetRootCauseTest");
    invoke();
  }

  /*
   * @testName: elExceptionToStringTest
   * 
   * @assertion_ids: JSP:JAVADOC:164
   * 
   * @test_Strategy: Validate the behavior of ELException.toString().
   */
  @Test
  public void elExceptionToStringTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "elExceptionToStringTest");
    invoke();
  }
}
