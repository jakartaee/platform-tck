/*
 * Copyright (c) 2013, 2023 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.beanvalidation.annotation;

import com.sun.ts.tests.jaxrs.common.client.JaxrsCommonClient;
import com.sun.ts.tests.jaxrs.common.util.JaxrsUtil;
import com.sun.ts.tests.jaxrs.common.provider.StringBean;
import com.sun.ts.tests.jaxrs.common.provider.StringBeanEntityProvider;
import java.io.IOException;
import java.io.InputStream;
import jakarta.ws.rs.core.Response.Status;

import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;


/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
/**
 * Test the interceptor is called when any entity provider is called
 */
@ExtendWith(ArquillianExtension.class)
public class JAXRSClientIT extends JaxrsCommonClient {

  private static final long serialVersionUID = 6890833876230368627L;

  public JAXRSClientIT() {
    setup();
    setContextRoot("/jaxrs_platform_beanvalidation_annotation_web/resource");
  }

  @Deployment(testable = false)
  public static WebArchive createDeployment() throws IOException {

    InputStream inStream = JAXRSClientIT.class.getClassLoader().getResourceAsStream("com/sun/ts/tests/jaxrs/platform/beanvalidation/annotation/web.xml.template");
    // Replace the servlet_adaptor in web.xml.template with the System variable set as servlet adaptor
    String webXml = editWebXmlString(inStream);
    inStream = JAXRSClientIT.class.getClassLoader().getResourceAsStream("com/sun/ts/tests/jaxrs/platform/beanvalidation/annotation/beans.xml");
    String beansXml = toString(inStream);

    WebArchive archive = ShrinkWrap.create(WebArchive.class, "jaxrs_platform_beanvalidation_annotation_web.war");
    archive.addClasses(
        TSAppConfig.class, 
        Resource.class, 
        ConstraintDeclarationAnnotation.class, 
        ConstraintDeclarationResource.class, 
        ConstraintDeclarationValidator.class, 
        ConstraintDefinitionAnnotation.class,
        ConstraintDefinitionResource.class,
        ConstraintDefinitionValidator.class,
        NotFiveNorShort.class,
        NotFiveNorShortStringBeanValidator.class,
        NotNullOrOne.class,
        NotNullOrOneStringBean.class,
        NotNullOrOneStringBeanValidator.class,
        NotShortNorFiveEntityProvider.class,
        NotShortNorFiveStringBean.class,
        ValidateExecutableResource.class,
        JaxrsUtil.class,
        StringBean.class,
        StringBeanEntityProvider.class
    );
    archive.addAsWebInfResource(new StringAsset(beansXml), "beans.xml");
    archive.setWebXML(new StringAsset(webXml));
    //archive.addAsWebInfResource(JAXRSClientIT.class.getPackage(), "web.xml.template", "web.xml"); //can use if the web.xml.template doesn't need to be modified.    
    
    return archive;
  }

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  // public static void main(String[] args) {
  //   new JAXRSClient().run(args);
  // }

  /* Run test */

  /*
   * @testName: beanIsValidTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  @Test
  public void beanIsValidTest() throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.POST, "notshortnorfive"));
    setProperty(Property.CONTENT, "1234");
    setProperty(Property.SEARCH_STRING, "1234");
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: beanIsInvalidForBeingFiveCharsLongTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.3;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  @Test
  public void beanIsInvalidForBeingFiveCharsLongTest() throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.POST, "notshortnorfive"));
    setProperty(Property.CONTENT, "12345");
    setProperty(Property.STATUS_CODE, getStatusCode(Status.BAD_REQUEST));
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: beanIsInvalidForBeingOneCharLongTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.3;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  @Test
  public void beanIsInvalidForBeingOneCharLongTest() throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.POST, "notshortnorfive"));
    setProperty(Property.CONTENT, "1");
    setProperty(Property.STATUS_CODE, getStatusCode(Status.BAD_REQUEST));
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: returnIsValidTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  @Test
  public void returnIsValidTest() throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.POST, "notshortnorfive"));
    setProperty(Property.CONTENT, "1234");
    setProperty(Property.SEARCH_STRING, "1234");
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: returnIsInvalidForBeingFiveCharsLongTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.2;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  @Test
  public void returnIsInvalidForBeingFiveCharsLongTest() throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.POST, "returnnotshortnorfive"));
    setProperty(Property.CONTENT, "12345");
    setProperty(Property.STATUS_CODE,
        getStatusCode(Status.INTERNAL_SERVER_ERROR));
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: returnIsInvalidForBeingOneCharLongTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.2;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  @Test
  public void returnIsInvalidForBeingOneCharLongTest() throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.POST, "returnnotshortnorfive"));
    setProperty(Property.CONTENT, "1");
    setProperty(Property.STATUS_CODE,
        getStatusCode(Status.INTERNAL_SERVER_ERROR));
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: returnIsInvalidForBeingNullTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.2;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  @Test
  public void returnIsInvalidForBeingNullTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.GET, "returnnull"));
    setProperty(Property.STATUS_CODE,
        getStatusCode(Status.INTERNAL_SERVER_ERROR));
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: beanAnnotatedIsValidTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  @Test
  public void beanAnnotatedIsValidTest() throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.POST, "directannotatedarg"));
    setProperty(Property.CONTENT, "1234");
    setProperty(Property.SEARCH_STRING, "1234");
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: beanAnnotatedIsInvalidForBeingFiveCharsLongTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.3;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  @Test
  public void beanAnnotatedIsInvalidForBeingFiveCharsLongTest() throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.POST, "directannotatedarg"));
    setProperty(Property.CONTENT, "12345");
    setProperty(Property.STATUS_CODE, getStatusCode(Status.BAD_REQUEST));
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: beanAnnotatedIsValidEvenBeingOneCharLongTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  @Test
  public void beanAnnotatedIsValidEvenBeingOneCharLongTest() throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.POST, "directannotatedarg"));
    setProperty(Property.CONTENT, "1");
    setProperty(Property.SEARCH_STRING, "1");
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: beanAnnotatedReturnIsValidTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  @Test
  public void beanAnnotatedReturnIsValidTest() throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.POST, "directannotatedreturn"));
    setProperty(Property.CONTENT, "1234");
    setProperty(Property.SEARCH_STRING, "1234");
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: beanAnnotatedReturnIsInvalidForBeingFiveCharsLongTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.2;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  @Test
  public void beanAnnotatedReturnIsInvalidForBeingFiveCharsLongTest()
      throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.POST, "directannotatedreturn"));
    setProperty(Property.CONTENT, "12345");
    setProperty(Property.STATUS_CODE,
        getStatusCode(Status.INTERNAL_SERVER_ERROR));
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: beanAnnotatedReturnIsValidEvenBeingOneCharLongTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  @Test
  public void beanAnnotatedReturnIsValidEvenBeingOneCharLongTest()
      throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.POST, "directannotatedreturn"));
    setProperty(Property.CONTENT, "1");
    setProperty(Property.SEARCH_STRING, "1");
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: beanAnnotatedReturnIsInvalidForBeingNullTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.2;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  @Test
  public void beanAnnotatedReturnIsInvalidForBeingNullTest() throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.GET, "directannotatedreturnnull"));
    setProperty(Property.STATUS_CODE,
        getStatusCode(Status.INTERNAL_SERVER_ERROR));
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: constraintDeclarationExceptionThrownTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.1;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  @Test
  public void constraintDeclarationExceptionThrownTest() throws Exception {
    setProperty(Property.REQUEST, buildRequest(Request.POST,
        "declaration/constraintdeclarationexception"));
    setProperty(Property.CONTENT, "throw ConstraintDeclarationException()");
    setProperty(Property.STATUS_CODE,
        getStatusCode(Status.INTERNAL_SERVER_ERROR));
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: constraintDefinitionExceptionThrownTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.1;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  @Test
  public void constraintDefinitionExceptionThrownTest() throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.POST, "definition/constraintdefinitionexception"));
    setProperty(Property.CONTENT, "throw ConstraintDefinitionException()");
    setProperty(Property.STATUS_CODE,
        getStatusCode(Status.INTERNAL_SERVER_ERROR));
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: validateExecutableIsInvalidForBeingShortTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.3;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   * 
   * Due to validation of whole bean status 400 is returned
   */
  @Test
  public void validateExecutableIsInvalidForBeingShortTest() throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.GET, "executable/nogetter"));
    setProperty(Property.STATUS_CODE, getStatusCode(Status.BAD_REQUEST));
    invoke();
    logMsg("Bean is validated as expected");
  }

  /*
   * @testName: validateExecutableIsNotValidatedTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.1;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   * 
   * Due to validation of whole bean status 400 is returned Make sure the
   * resource is not validated in Step 4, i.e. it would return status 500 then
   * (the exception was thrown while validating a method return type) or is not
   * forgotten to be validated at all.
   */
  @Test
  public void validateExecutableIsNotValidatedTest() throws Exception {
    setProperty(Property.REQUEST,
        buildRequest(Request.GET, "executable/getter"));
    setProperty(Property.STATUS_CODE, getStatusCode(Status.BAD_REQUEST));
    invoke();
    logMsg("Bean is validated as expected");
  }

}
