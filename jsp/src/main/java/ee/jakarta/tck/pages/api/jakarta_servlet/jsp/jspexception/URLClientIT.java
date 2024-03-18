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

package ee.jakarta.tck.pages.api.jakarta_servlet.jsp.jspexception;


import ee.jakarta.tck.pages.common.client.AbstractUrlClient;

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

    setContextRoot("/jsp_jspexc_web");
    setTestJsp("JspExceptionTest");

    }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_jspexc_web.war");
    archive.addClasses(ee.jakarta.tck.pages.common.util.JspTestUtil.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_jspexc_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspExceptionTest.jsp")), "JspExceptionTest.jsp");


    return archive;
  }

  
  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: jspExceptionDefaultCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:125
   * 
   * @test_Strategy: Validate default constructor of JspException
   */
  @Test
  public void jspExceptionDefaultCtorTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "jspExceptionDefaultCtorTest");
    invoke();
  }

  /*
   * @testName: jspExceptionMessageCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:126
   * 
   * @test_Strategy: Validate contructor taking single string argument as the
   * message of the Exception.
   */
  @Test
  public void jspExceptionMessageCtorTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "jspExceptionMessageCtorTest");
    invoke();
  }

  /*
   * @testName: jspExceptionCauseCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:128
   * 
   * @test_Strategy: Validate constructor taking a Throwable signifying the root
   * cause of the this JspException.
   */
  @Test
  public void jspExceptionCauseCtorTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "jspExceptionCauseCtorTest");
    invoke();
  }

  /*
   * @testName: jspExceptionCauseMessageCtorTest
   * 
   * @assertion_ids: JSP:JAVADOC:127
   * 
   * @test_Strategy: Validate constructor taking both a message and a Throwable
   * signifying the root cause of the JspException.
   */
  @Test
  public void jspExceptionCauseMessageCtorTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "jspExceptionCauseMessageCtorTest");
    invoke();
  }

  /*
   * @testName: jspExceptionGetRootCauseTest
   * 
   * @assertion_ids: JSP:JAVADOC:129
   * 
   * @test_Strategy: Validate the behavior of JspException.getRootCause().
   */
  // @Test
  public void jspExceptionGetRootCauseTest() throws Exception {
    TEST_PROPS.setProperty(APITEST, "jspExceptionGetRootCauseTest");
    invoke();
  }
}
