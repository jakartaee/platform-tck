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

package com.sun.ts.tests.jsp.spec.core_syntax.implicitobjects;

import java.io.IOException;
import java.io.InputStream;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.jboss.shrinkwrap.api.asset.UrlAsset;


@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {



  public static String packagePath = URLClientIT.class.getPackageName().replace(".", "/");

  public URLClientIT() throws Exception {
    setup();

    setGeneralURI("/jsp/spec/core_syntax/implicitobjects");
    setContextRoot("/jsp_coresyntx_implicitobjects_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_coresyntx_implicitobjects_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_coresyntx_implicitobjects_web.xml"));
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/Errorpage.jsp")), "Errorpage.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/checkSession.jsp")), "checkSession.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/checkResponse.jsp")), "checkResponse.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/checkRequest.jsp")), "checkRequest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/checkPageContext.jsp")), "checkPageContext.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/checkPage.jsp")), "checkPage.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/checkOut.jsp")), "checkOut.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/checkException.jsp")), "checkException.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/checkConfig.jsp")), "checkConfig.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/checkApplication.jsp")), "checkApplication.jsp");
  
    return archive;
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: checkSessionTest
   * 
   * @assertion_ids: JSP:SPEC:15
   * 
   * @test_Strategy: Validate that the object associated with the session
   * scripting variable is of type jakarta.servlet.http.HttpSession and that a
   * method can be called against it.
   */

  @Test
  public void checkSessionTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/checkSession.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "checkSession");
    invoke();
  }

  /*
   * @testName: checkConfigTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the object associated with the config
   * scripting variable is of type jakarta.servlet.ServletConfig and that a method
   * can be called against it.
   */

  @Test
  public void checkConfigTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/checkConfig.gf");
    setGoldenFileStream(gfStream);
    String testName = "checkConfig";
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(GOLDENFILE, testName + ".gf");
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_coresyntx_implicitobjects_web/" + testName + " HTTP/1.0");
    invoke();
  }

  /*
   * @testName: checkExceptionTest
   * 
   * @assertion_ids: JSP:SPEC:17
   * 
   * @test_Strategy: Validate that the object associated with the exception
   * scripting variable is of an instance of the exception type thrown (a
   * subclass of java.lang.Throwable).
   */

  @Test
  public void checkExceptionTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/checkException.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "checkException");
    TEST_PROPS.setProperty(IGNORE_STATUS_CODE, "true");
    invoke();
  }

  /*
   * @testName: checkOutTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the object associated with the out scripting
   * variable is of type jakarta.servlet.jsp.JspWriter.
   */

  @Test
  public void checkOutTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/checkOut.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "checkOut");
    invoke();
  }

  /*
   * @testName: checkPageTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the object associated with the page scripting
   * variable is of type java.lang.Object.
   */

  @Test
  public void checkPageTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/checkPage.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "checkPage");
    invoke();
  }

  /*
   * @testName: checkPageContextTest
   * 
   * @assertion_ids: JSP:SPEC:14
   * 
   * @test_Strategy: Validate that the object associated with the pageContext
   * scripting variable is of type jakarta.servlet.jsp.PageContext and that a
   * method can be called against it.
   */

  @Test
  public void checkPageContextTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/checkPageContext.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "checkPageContext");
    invoke();
  }

  /*
   * @testName: checkRequestTest
   * 
   * @assertion_ids: JSP:SPEC:12
   * 
   * @test_Strategy: Validate that the object associated with the request
   * scripting variable is of type jakarta.servlet.Request (parent class of
   * HttpServletRequest) and that a method can be called against it.
   */

  @Test
  public void checkRequestTest() throws Exception {
    String testName = "checkRequest";
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/checkRequest.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(TEST_NAME, testName + "Test");
    TEST_PROPS.setProperty(GOLDENFILE, testName + ".gf");
    TEST_PROPS.setProperty(REQUEST, "GET /jsp_coresyntx_implicitobjects_web/"
        + testName + ".jsp?Years=2 HTTP/1.0");
    invoke();
  }

  /*
   * @testName: checkResponseTest
   * 
   * @assertion_ids: JSP:SPEC:13
   * 
   * @test_Strategy: Validate that the object associated with the response
   * scripting variable is of type jakarta.servlet.Response (parent class of
   * HttpServletResponse) and that a method can be called against it.
   */

  @Test
  public void checkResponseTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/checkResponse.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "checkResponse");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "TestHeader:Method call OK");
    invoke();
  }

  /*
   * @testName: checkApplicationTest
   * 
   * @assertion_ids: JSP:SPEC:16
   * 
   * @test_Strategy: Validate that the object associated with the application
   * scripting variable is of type jakarta.servlet.ServletContext that a method
   * can be called against it.
   */

  @Test
  public void checkApplicationTest() throws Exception {
    InputStream gfStream = URLClientIT.class.getClassLoader().getResourceAsStream(packagePath+"/checkApplication.gf");
    setGoldenFileStream(gfStream);
    TEST_PROPS.setProperty(STANDARD, "checkApplication");
    invoke();
  }

}
