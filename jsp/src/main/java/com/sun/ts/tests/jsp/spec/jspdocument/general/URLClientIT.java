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

package com.sun.ts.tests.jsp.spec.jspdocument.general;


import java.io.IOException;
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

import java.lang.System.Logger;

@ExtendWith(ArquillianExtension.class)
public class URLClientIT extends AbstractUrlClient {

  private static final Logger logger = System.getLogger(URLClientIT.class.getName());

  @BeforeEach
  void logStartTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "STARTING TEST : "+testInfo.getDisplayName());
  }

  @AfterEach
  void logFinishTest(TestInfo testInfo) {
    logger.log(Logger.Level.INFO, "FINISHED TEST : "+testInfo.getDisplayName());
  }

  private static final String CONTEXT_ROOT = "/jsp_jspdocument_general_web";

  public URLClientIT() throws Exception {
    setup();

    setContextRoot(CONTEXT_ROOT);

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_jspdocument_general_web.war");
    archive.addClasses(EchoTag.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_jspdocument_general_web.xml"));
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tagdep.tld", "tagdep.tld");    

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/tagDependentTest.jspx")), "tagDependentTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeWellFormedness.jspx")), "negativeWellFormedness.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDTDValidation.jspx")), "negativeDTDValidation.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/invalidPlainURI.jsp")), "invalidPlainURI.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/identifyByJspRoot.jsp")), "identifyByJspRoot.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/identifyByExtension.jspx")), "identifyByExtension.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/identifyByConfig.jsp")), "identifyByConfig.jsp");
    
    return archive;

  }

  
  /*
   * @testName: negativeWellFormednessTest
   * 
   * @assertion_ids: JSP:SPEC:173.4
   * 
   * @test_Strategy: access a jsp document that is not well-formed.
   */

  @Test
  public void negativeWellFormednessTest() throws Exception {
    String testName = "negativeWellFormedness";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: identifyByJspRootTest
   * 
   * @assertion_ids: JSP:SPEC:173; JSP:SPEC:173.3
   * 
   * @test_Strategy: access a jsp page that has a jsp:root as top element
   */

  @Test
  public void identifyByJspRootTest() throws Exception {
    String testName = "identifyByJspRoot";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: identifyByExtensionTest
   * 
   * @assertion_ids: JSP:SPEC:173; JSP:SPEC:173.2; JSP:SPEC:176
   * 
   * @test_Strategy: identify a jsp document by .jspx extension a jsp document
   * does not need to have jsp:root
   */

  @Test
  public void identifyByExtensionTest() throws Exception {
    String testName = "identifyByExtension";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: identifyByConfigTest
   * 
   * @assertion_ids: JSP:SPEC:173; JSP:SPEC:173.1; JSP:SPEC:176
   * 
   * @test_Strategy: identify a jsp document by jsp-property-group via is-xml a
   * jsp document does not need to have jsp:root
   */

  @Test
  public void identifyByConfigTest() throws Exception {
    String testName = "identifyByConfig";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: negativeDTDValidationTest
   * 
   * @assertion_ids: JSP:SPEC:260
   * 
   * @test_Strategy: access an invalid jsp document and expect translation
   * error.
   */

  @Test
  public void negativeDTDValidationTest() throws Exception {
    String testName = "negativeDTDValidation";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: invalidPlainURITest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: a translation error must not be generated if the given
   * plain uri is not found in the taglib map.
   */

  @Test
  public void invalidPlainURITest() throws Exception {
    String testName = "invalidPlainURI";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, OK);
    invoke();
  }

  /*
   * @testName: tagDependentTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: validate that tagdependent body should be passed verbatim,
   * tag handles inside body must not be invoked.
   */

  @Test
  public void tagDependentTest() throws Exception {
    String testName = "tagDependentTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "START ${pageScope.eval}|"
            + "jsp:scriptlet|out.println(\"scriptlet\");|jsp:scriptlet|"
            + "jsp:useBean|java.util.Date|" + "jsp:getProperty|property|" +

            "use jsp:body|" +

            "START ${pageScope.eval}|"
            + "jsp:scriptlet|out.println(\"scriptlet\");|jsp:scriptlet|"
            + "jsp:useBean|java.util.Date|" + "jsp:getProperty|property|"
            + "Expression from attribute: 18|" +

            "END 72");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "evaluated");
    invoke();
  }

}
