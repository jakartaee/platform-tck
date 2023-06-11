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

package com.sun.ts.tests.jsp.api.jakarta_servlet.jsp.jspapplicationcontext;

import java.io.PrintWriter;

import com.sun.javatest.Status;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import java.io.IOException;
import java.io.InputStream;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
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


  public URLClientIT() throws Exception {
    setup();
    setContextRoot("/jsp_jspapplicationcontext_web");
    setTestJsp("JspApplicationContextTest");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_jspapplicationcontext_web.war");
    archive.addClasses(FooELResolver.class, InstallFooListener.class,
            com.sun.ts.tests.jsp.common.util.JspTestUtil.class);    
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_jspapplicationcontext_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/AddELResolverTest.jsp")), "AddELResolverTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/IllegalStateExceptionTest.jsp")), "IllegalStateExceptionTest.jsp");

    return archive;
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /* Run tests */

  // ============================================ Tests ======

  /*
   * @testName: addELResolverTest
   * 
   * @assertion_ids: JSP:JAVADOC:410
   * 
   * @test_Strategy: Validate the behavior of
   * JspApplicationContext.addELResolver() Verify that once an ELResolver has
   * been registered with the JSP container it performs as expected.
   */
  @Test
  public void addELResolverTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_jspapplicationcontext_web/AddELResolverTest.jsp HTTP 1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: invokeIllegalStateExceptionTest
   * 
   * @assertion_ids: JSP:JAVADOC:410
   * 
   * @test_Strategy: Validate the behavior of
   * JspApplicationContext.addELResolver() throws IllegalStateException Verify
   * that once an application has received a request from the clienT, A call to
   * JspApplicationContext.addELResolver() will cause the container to throw an
   * IllegalStateException.
   */
  @Test
  public void invokeIllegalStateExceptionTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_jspapplicationcontext_web/IllegalStateExceptionTest.jsp HTTP 1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }
}
