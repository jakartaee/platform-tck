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

/*
 * @(#)URLClient.java	1.36 02/11/04
 */

package com.sun.ts.tests.jsp.spec.configuration.elevaluation;

import java.io.PrintWriter;
import java.net.URL;

import com.sun.javatest.Status;
import java.io.IOException;
import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
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
  public URLClientIT() throws Exception {
    setup();
    setGeneralURI("/jsp/spec/configuration/elevaluation");
    setContextRoot("/jsp_config_eleval_web");

  }

  public void setup() throws Exception {

    String hostname = System.getProperty(SERVLETHOSTPROP).trim();
    String portnum = System.getProperty(SERVLETPORTPROP).trim();

    if (isNullOrEmpty(hostname)) {
      hostname = url2.getHost(); 
    }
    if (isNullOrEmpty(portnum)) {
      portnum = Integer.toString(url2.getPort()); 
    }
    
    if (!isNullOrEmpty(hostname)) {
      _hostname = hostname;
    } else {
      throw new Exception(
          "[elevaluation.URLClientIT] 'webServerHost' was not set");
    }

    if (!isNullOrEmpty(portnum)) {
      _port = Integer.parseInt(portnum);
    } else {
      throw new Exception(
          "[elevaluation.URLClientIT] 'webServerPort' was not set");
    }

  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_config_eleval_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_config_eleval_web.xml"));

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/compat13/ElCompatTest.jsp")), "compat13/ElCompatTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/elconffalse/ElEvaluationTest.jsp")), "elconffalse/ElEvaluationTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/elconffalsex/ElEvaluationTest.jspx")), "elconffalsex/ElEvaluationTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/elconftrue/ElEvaluationTest.jsp")), "elconftrue/ElEvaluationTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/elconftruex/ElEvaluationTest.jspx")), "elconftruex/ElEvaluationTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/elpagefalse/ElEvaluationTest.jsp")), "elpagefalse/ElEvaluationTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/elpagefalsex/ElEvaluationTest.jspx")), "elpagefalsex/ElEvaluationTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/elpagetrue/ElEvaluationTest.jsp")), "elpagetrue/ElEvaluationTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/elpagetruex/ElEvaluationTest.jspx")), "elpagetruex/ElEvaluationTest.jspx");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/elunspec/ElEvaluationTest.jsp")), "elunspec/ElEvaluationTest.jsp");
    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/elunspecx/ElEvaluationTest.jspx")), "elunspecx/ElEvaluationTest.jspx");
  
    return archive;

  }

  @ArquillianResource
  @OperateOnDeployment("jsp_config_eleval23_web") 
  public URL url2;


  @Deployment(testable = false, name="jsp_config_eleval23_web")
  public static WebArchive createDeployment23() throws IOException {
    
    String packagePath = URLClientIT.class.getPackageName().replace(".", "/");
    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jsp_config_eleval23_web.war");
    archive.setWebXML(URLClientIT.class.getClassLoader().getResource(packagePath+"/jsp_config_eleval23_web.xml"));

    archive.add(new UrlAsset(URLClientIT.class.getClassLoader().getResource(packagePath+"/compat13/ElCompatTest.jsp")), "ElCompatTest.jsp");
  
    return archive;

  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: elEvaluationUnspecifiedTest
   * 
   * @assertion_ids: JSP:SPEC:254
   * 
   * @test_Strategy: Validate that if the web application uses a 2.4 deployment
   * descriptor and the jsp-property-group element nor the JSP identified by the
   * jsp-property-group specifies no EL evaluation information, EL will be
   * evaluated by the container. This validates both JSPs in standard syntax and
   * JSP documents.
   */
  @Test
  public void elEvaluationUnspecifiedTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elunspec/ElEvaluationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elunspecx/ElEvaluationTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: elEvaluationConfigurationFalseTest
   * 
   * @assertion_ids: JSP:SPEC:142
   * 
   * @test_Strategy: Validate that if the web application uses a 2.4 deployment
   * descriptor and the jsp-property-group element sets the el-ignored element
   * to false, and the JSP page specifies no special EL handling, that EL
   * expressions will be evaluated. This validates both JSPs in standard syntax
   * and JSP documents.
   */
  @Test
  public void elEvaluationConfigurationFalseTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elconffalse/ElEvaluationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elconffalsex/ElEvaluationTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: elEvaluationConfigurationTrueTest
   * 
   * @assertion_ids: JSP:SPEC:141
   * 
   * @test_Strategy: Validate that if the web application uses a 2.4 deployment
   * descriptor and the jsp-property-group element sets the el-ignored element
   * to true, and the JSP page specifies no special EL handling, that EL
   * expressions will not be evaluated. This validates both JSPs in standard
   * syntax and JSP documents.
   */
  @Test
  public void elEvaluationConfigurationTrueTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elconftrue/ElEvaluationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "${testPassed}");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elconftruex/ElEvaluationTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "${testPassed}");
    invoke();
  }

  /*
   * @testName: elEvaluationPageDirectiveOverrideTest
   * 
   * @assertion_ids: JSP:SPEC:255
   * 
   * @test_Strategy: Validate that if the web application uses a 2.4 deployment
   * descriptor, that the page directive attribute isELIgnored takes precedence
   * over the configuration of the JSP property group.
   */
  @Test
  public void elEvaluationPageDirectiveOverrideTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elpagetrue/ElEvaluationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "${testPassed}");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elpagetruex/ElEvaluationTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "${testPassed}");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elpagefalse/ElEvaluationTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval_web/elpagefalsex/ElEvaluationTest.jspx HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "Test PASSED");
    invoke();
  }

  /*
   * @testName: elEvaluation23WebApplicationTest
   * 
   * @assertion_ids: JSP:SPEC:252
   * 
   * @test_Strategy: Validate a JSP 2.0 container when presented with a 2.3
   * based web application, and it encounters a JSP with an EL-like construct
   * (i.e. ${expr}), that EL Evaluation is not performed.
   */
  @Test
  public void elEvaluation23WebApplicationTest() throws Exception {
    TEST_PROPS.setProperty(REQUEST,
        "GET /jsp_config_eleval23_web/ElCompatTest.jsp HTTP/1.1");
    TEST_PROPS.setProperty(SEARCH_STRING, "${testPassed}");
    invoke();
  }
}
