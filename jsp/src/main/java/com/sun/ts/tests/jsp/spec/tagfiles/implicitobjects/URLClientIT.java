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

package com.sun.ts.tests.jsp.spec.tagfiles.implicitobjects;


import java.io.IOException;
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


  private static final String CONTEXT_ROOT = "/jsp_tagfiles_implicitobjects_web";

  public URLClientIT() throws Exception {


    setContextRoot(CONTEXT_ROOT);

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_tagfiles_implicitobjects_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_tagfiles_implicitobjects_web.xml"));

    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/checkSession.tag", "tags/checkSession.tag");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/checkRequest.tag", "tags/checkRequest.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/checkResponse.tag", "tags/checkResponse.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/checkOut.tag", "tags/checkOut.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/checkJspContext.tag", "tags/checkJspContext.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/checkConfig.tag", "tags/checkConfig.tag");
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/checkApplication.tag", "tags/checkApplication.tag");
    
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/checkSession.jsp")), "checkSession.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/checkResponse.jsp")), "checkResponse.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/checkRequest.jsp")), "checkRequest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/checkOut.jsp")), "checkOut.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/checkJspContext.jsp")), "checkJspContext.jsp");
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
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the object associated with the session
   * scripting variable is of type jakarta.servlet.http.HttpSession and that a
   * method can be called against it.
   */

  @Test
  public void checkSessionTest() throws Exception {
    String testName = "checkSession";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "true|checkSession");
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
    String testName = "checkConfig";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/checkConfig" + " HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "true|param1 is:|hobbit|param2 is:|gollum");
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
    String testName = "checkOut";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "true");
    invoke();
  }

  /*
   * @testName: checkJspContextTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the object associated with the jspContext
   * scripting variable is of type jakarta.servlet.jsp.JspContext and that a
   * method can be called against it.
   */

  @Test
  public void checkJspContextTest() throws Exception {
    String testName = "checkJspContext";
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_tagfiles_implicitobjects_web/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "true|true");
    invoke();
  }

  /*
   * @testName: checkRequestTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the object associated with the request
   * scripting variable is of type jakarta.servlet.Request (parent class of
   * HttpServletRequest) and that a method can be called against it.
   */

  @Test
  public void checkRequestTest() throws Exception {
    String testName = "checkRequest";
    TEST_PROPS.setProperty(REQUEST, "GET /jsp_tagfiles_implicitobjects_web/"
        + testName + ".jsp?Years=2 HTTP/1.0");
    TEST_PROPS.setProperty(SEARCH_STRING, "true|HTTP|1.0|2");
    invoke();
  }

  /*
   * @testName: checkResponseTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the object associated with the response
   * scripting variable is of type jakarta.servlet.Response (parent class of
   * HttpServletResponse) and that a method can be called against it.
   */

  @Test
  public void checkResponseTest() throws Exception {
    String testName = "checkResponse";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "true");
    TEST_PROPS.setProperty(EXPECTED_HEADERS, "TestHeader:Method call OK");
    invoke();
  }

  /*
   * @testName: checkApplicationTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that the object associated with the application
   * scripting variable is of type jakarta.servlet.ServletContext that a method
   * can be called against it.
   */
  @Test
  public void checkApplicationTest() throws Exception {
    String testName = "checkApplication";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "true|param1|bilbo");
    invoke();
  }

}
