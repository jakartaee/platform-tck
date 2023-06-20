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

package com.sun.ts.tests.jsp.spec.jspdocument.elements;

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
  private static final String CONTEXT_ROOT = "/jsp_jspdocument_elements_web";

  public URLClientIT() throws Exception {
    setup();

    setContextRoot(CONTEXT_ROOT);

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_jspdocument_elements_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_jspdocument_elements_web.xml"));

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scriptingInJspPage.jsp")), "scriptingInJspPage.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/scripting.jspx")), "scripting.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/notJspDocument.jspx")), "notJspDocument.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeJspRoot.jspx")), "negativeJspRoot.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/jspRootPrefix.jspx")), "jspRootPrefix.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/included.jspx")), "included.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/included.jsp")), "included.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/directivePageInJspPage.jsp")), "directivePageInJspPage.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/directiveIncludeLocation.jspx")), "directiveIncludeLocation.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/directiveIncludeInJspPage.jsp")), "directiveIncludeInJspPage.jsp");
    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   */

  /*
   * @testName: negativeJspRootTest
   * 
   * @assertion_ids: JSP:SPEC:177; JSP:SPEC:173.1
   * 
   * @test_Strategy: use jsp:root not as the top element of a jsp document.
   * jsp-property-group config overrides other determiniations.
   */

  @Test
  public void negativeJspRootTest() throws Exception {
    String testName = "negativeJspRoot";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + "notJspDocument.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: jspRootPrefixTest
   * 
   * @assertion_ids: JSP:SPEC:175
   * 
   * @test_Strategy: use a different prefix abc
   */

  @Test
  public void jspRootPrefixTest() throws Exception {
    String testName = "jspRootPrefix";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: directiveIncludeLocationTest
   * 
   * @assertion_ids: JSP:SPEC:179.1; JSP:SPEC:179; JSP:SPEC:179.3
   * 
   * @test_Strategy: use jsp:directive.include anywhere within a jsp document.
   */

  @Test
  public void directiveIncludeLocationTest() throws Exception {
    String testName = "directiveIncludeLocation";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "HELLO|ONE|HELLO|TWO");
    invoke();
  }

  /*
   * @testName: directiveIncludeInJspPageTest
   * 
   * @assertion_ids: JSP:SPEC:179.5
   * 
   * @test_Strategy: use jsp:directive.include in a jsp page
   */

  @Test
  public void directiveIncludeInJspPageTest() throws Exception {
    String testName = "directiveIncludeInJspPage";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "HELLO|ONE");
    invoke();
  }

  /*
   * @testName: directivePageInJspPageTest
   * 
   * @assertion_ids: JSP:SPEC:178.39
   * 
   * @test_Strategy: use jsp:directive.page in a jsp page
   */

  @Test
  public void directivePageInJspPageTest() throws Exception {
    String testName = "directivePageInJspPage";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "listSize2");
    invoke();
  }

  /*
   * @testName: scriptingTest
   * 
   * @assertion_ids: JSP:SPEC:180; JSP:SPEC:181; JSP:SPEC:182
   * 
   * @test_Strategy: use jsp:declaration, jsp:scriptlet, and jsp:expression in a
   * jsp document.
   */

  @Test
  public void scriptingTest() throws Exception {
    String testName = "scripting";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "JSP637|JSP637|99");
    invoke();
  }

  /*
   * @testName: scriptingInJspPageTest
   * 
   * @assertion_ids: JSP:SPEC:180.4; JSP:SPEC:181.2; JSP:SPEC:182.5
   * 
   * @test_Strategy: use jsp:declaration, jsp:scriptlet, and jsp:expression in a
   * jsp page.
   */

  @Test
  public void scriptingInJspPageTest() throws Exception {
    String testName = "scriptingInJspPage";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    TEST_PROPS.setProperty(SEARCH_STRING, "JSP637|99");
    invoke();
  }

}
