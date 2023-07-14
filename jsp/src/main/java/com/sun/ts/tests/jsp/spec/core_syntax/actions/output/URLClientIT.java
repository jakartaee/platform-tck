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

/*
 * @(#)URLClient.java	1.1 12/09/02
 */

package com.sun.ts.tests.jsp.spec.core_syntax.actions.output;


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

  private static final String CONTEXT_ROOT = "/jsp_core_act_output_web";

  public URLClientIT() throws Exception {
    setup();

    setContextRoot("/jsp_core_act_output_web");

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_core_act_output_web.war");
    archive.addClasses(com.sun.ts.tests.jsp.common.util.JspTestUtil.class);
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_core_act_output_web.xml"));

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/simpleDefaultTest.jspx")), "simpleDefaultTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeMultipleDoctypeTest.jspx")), "negativeMultipleDoctypeTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDoctypeSystemNoRoot.jspx")), "negativeDoctypeSystemNoRoot.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDoctypeRootNoSystem.jspx")), "negativeDoctypeRootNoSystem.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/negativeDoctypePublicNoSystemTest.jspx")), "negativeDoctypePublicNoSystemTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputUsageContextTest4.jsp")), "JspOutputUsageContextTest4.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputUsageContextTest3.jspx")), "JspOutputUsageContextTest3.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputUsageContextTest2.jsp")), "JspOutputUsageContextTest2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputUsageContextTest1.jsp")), "JspOutputUsageContextTest1.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputOmitDeclValidValuesTest8.jsp")), "JspOutputOmitDeclValidValuesTest8.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputOmitDeclValidValuesTest7.jsp")), "JspOutputOmitDeclValidValuesTest7.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputOmitDeclValidValuesTest6.jsp")), "JspOutputOmitDeclValidValuesTest6.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputOmitDeclValidValuesTest5.jsp")), "JspOutputOmitDeclValidValuesTest5.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputOmitDeclValidValuesTest4.jspx")), "JspOutputOmitDeclValidValuesTest4.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputOmitDeclValidValuesTest3.jspx")), "JspOutputOmitDeclValidValuesTest3.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputOmitDeclValidValuesTest2.jspx")), "JspOutputOmitDeclValidValuesTest2.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputOmitDeclValidValuesTest1.jspx")), "JspOutputOmitDeclValidValuesTest1.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputOmitDeclDefaultTest.jspx")), "JspOutputOmitDeclDefaultTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputOmitDeclDefaultTagTest.jsp")), "JspOutputOmitDeclDefaultTagTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputJspRootOmitDeclDefaultTest.jspx")), "JspOutputJspRootOmitDeclDefaultTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputBodyTest2.jsp")), "JspOutputBodyTest2.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/JspOutputBodyTest1.jspx")), "JspOutputBodyTest1.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/doctypeSystemTest.jspx")), "doctypeSystemTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/doctypeSystemPublicTest.jspx")), "doctypeSystemPublicTest.jspx");

    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspOutputUsageXMLTag.tagx", "tags/JspOutputUsageXMLTag.tagx");    
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspOutputUsageTag.tag", "tags/JspOutputUsageTag.tag");  
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspOutputOmitDeclYesTag.tagx", "tags/JspOutputOmitDeclYesTag.tagx");  
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspOutputOmitDeclTrueTag.tagx", "tags/JspOutputOmitDeclTrueTag.tagx");  
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspOutputOmitDeclNoTag.tagx", "tags/JspOutputOmitDeclNoTag.tagx");  
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspOutputOmitDeclFalseTag.tagx", "tags/JspOutputOmitDeclFalseTag.tagx");  
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspOutputDefaultTag.tagx", "tags/JspOutputDefaultTag.tagx");  
    archive.addAsWebInfResource(URLClientIT.class.getPackage(), "WEB-INF/tags/JspOutputBodyTag.tagx", "tags/JspOutputBodyTag.tagx");  
    
    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: jspOutputUsageContextTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that jsp:output can be used within JSP documents
   * and Tag files in XML syntax and that a translation-time error is raised if
   * used within the context of a standard syntax JSP or Tag file.
   */
  @Test
  public void jspOutputUsageContextTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputUsageContextTest1.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputUsageContextTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputUsageContextTest3.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>|<root></root>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputUsageContextTest4.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>|<root></root>");
    invoke();
  }

  /*
   * @testName: jspOutputOmitDeclValidValuesTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate the valid values for the omit-xml-declaration are
   * 'true', 'false', 'yes', and 'no'. If the attribute values are 'false' or
   * 'no', then the xml declaration will be generated. If 'true' or 'yes' no
   * declaration will be generated.
   */
  @Test
  public void jspOutputOmitDeclValidValuesTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclValidValuesTest1.jspx HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "<?xml");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclValidValuesTest2.jspx HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "<?xml");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclValidValuesTest3.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclValidValuesTest4.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclValidValuesTest5.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "<?xml");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclValidValuesTest6.jsp HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "<?xml");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclValidValuesTest7.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclValidValuesTest8.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>");
    invoke();
  }

  /*
   * @testName: jspOutputJspRootOmitDeclDefaultTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if a JSP document contains jsp:root, and a
   * jsp:output action is present without the omit-xml-declaration attribute,
   * the xml declaration will not be generated.
   */
  @Test
  public void jspOutputJspRootOmitDeclDefaultTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputJspRootOmitDeclDefaultTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(UNEXPECTED_RESPONSE_MATCH, "<?xml");
    invoke();
  }

  /*
   * @testName: jspOutputOmitDeclDefaultTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if jsp:output is present in a JSP document
   * without a jsp:root element, and the omit-xml-declaration attribute is not
   * present, the default behavior is that an xml declaration is generated.
   */
  @Test
  public void jspOutputOmitDeclDefaultTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclDefaultTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>");
    invoke();
  }

  /*
   * @testName: jspOutputOmitDeclDefaultTagTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that if jsp:output is present in a Tag file in XML
   * syntax, and the omit-xml-declaration attribute is not present, the default
   * behavior will be the generation of an XML declaration.
   */
  @Test
  public void jspOutputOmitDeclDefaultTagTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputOmitDeclDefaultTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>");
    invoke();
  }

  /*
   * @testName: jspOutputBodyTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Validate that a translation error occurs if the body of
   * jsp:output is not empty.
   */
  @Test
  public void jspOutputBodyTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputBodyTest1.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_core_act_output_web/JspOutputBodyTest2.jsp HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: simpleDefaultTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: use a jsp document without jsp:output. response encoding is
   * not set so use default for jsp document
   */

  @Test
  public void simpleDefaultTest() throws Exception {
    String testName = "simpleDefaultTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<?xml version=\"1.0\" encoding=\"UTF-8\"|?>");
    invoke();
  }

  /*
   * @testName: doctypeSystemTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: A DOCTYPE must be automatically output if and only if the
   * doctype-system element appears in the translation unit as part of a
   * <jsp:output> action. The format of the DOCTYPE: <!DOCTYPE nameOfRootElement
   * SYSTEM "doctypeSystem">
   */

  @Test
  public void doctypeSystemTest() throws Exception {
    String testName = "doctypeSystemTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<!DOCTYPE html SYSTEM |http://www.w3.org/TR/xhtml-basic/xhtml-basic10.dtd|Example XHTML Document");
    invoke();
  }

  /*
   * @testName: doctypeSystemPublicTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: A DOCTYPE must be automatically output if and only if the
   * doctype-system element appears in the translation unit as part of a
   * <jsp:output> action. The format of the DOCTYPE: <!DOCTYPE nameOfRootElement
   * PUBLIC "doctypePublic" "doctypeSystem">
   */

  @Test
  public void doctypeSystemPublicTest() throws Exception {
    String testName = "doctypeSystemPublicTest";
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/" + testName + ".jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING,
        "<!DOCTYPE html PUBLIC |-//W3C//DTD XHTML Basic 1.0//EN|http://www.w3.org/TR/xhtml-basic/xhtml-basic10.dtd|Example XHTML Document");
    invoke();
  }

  /*
   * @testName: negativeDoctypeRootTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: The doctype- root-element must appear and must only appear
   * if the doctype-system property appears, or a translation error must occur.
   */

  @Test
  public void negativeDoctypeRootTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/negativeDoctypeRootNoSystem.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/negativeDoctypeSystemNoRoot.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeDoctypePublicNoSystemTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: The doctype-public property is optional, but must not
   * appear unless the doctype-system property appears, or a translation error
   * must occur.
   */

  @Test
  public void negativeDoctypePublicNoSystemTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST, "GET " + CONTEXT_ROOT
        + "/negativeDoctypePublicNoSystemTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }

  /*
   * @testName: negativeMultipleDoctypeTest
   * 
   * @assertion_ids: PENDING
   * 
   * @test_Strategy: Multiple occurrences of the doctype-root-element,
   * doctype-system or doctype-public properties will cause a translation error
   * if the values for the properties differ from the previous occurrence.
   */

  @Test
  public void negativeMultipleDoctypeTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET " + CONTEXT_ROOT + "/negativeMultipleDoctypeTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(STATUS_CODE, INTERNAL_SERVER_ERROR);
    invoke();
  }
}
