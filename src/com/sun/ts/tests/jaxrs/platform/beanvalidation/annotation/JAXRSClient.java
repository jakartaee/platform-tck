/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

import javax.ws.rs.core.Response.Status;

import com.sun.ts.tests.jaxrs.common.client.JaxrsCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
/**
 * Test the interceptor is called when any entity provider is called
 */
public class JAXRSClient extends JaxrsCommonClient {

  private static final long serialVersionUID = 6890833876230368627L;

  public JAXRSClient() {
    setContextRoot("/jaxrs_platform_beanvalidation_annotation_web/resource");
  }

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    new JAXRSClient().run(args);
  }

  /* Run test */

  /*
   * @testName: beanIsValidTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   */
  public void beanIsValidTest() throws Fault {
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
  public void beanIsInvalidForBeingFiveCharsLongTest() throws Fault {
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
  public void beanIsInvalidForBeingOneCharLongTest() throws Fault {
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
  public void returnIsValidTest() throws Fault {
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
  public void returnIsInvalidForBeingFiveCharsLongTest() throws Fault {
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
  public void returnIsInvalidForBeingOneCharLongTest() throws Fault {
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
  public void returnIsInvalidForBeingNullTest() throws Fault {
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
  public void beanAnnotatedIsValidTest() throws Fault {
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
  public void beanAnnotatedIsInvalidForBeingFiveCharsLongTest() throws Fault {
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
  public void beanAnnotatedIsValidEvenBeingOneCharLongTest() throws Fault {
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
  public void beanAnnotatedReturnIsValidTest() throws Fault {
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
  public void beanAnnotatedReturnIsInvalidForBeingFiveCharsLongTest()
      throws Fault {
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
  public void beanAnnotatedReturnIsValidEvenBeingOneCharLongTest()
      throws Fault {
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
  public void beanAnnotatedReturnIsInvalidForBeingNullTest() throws Fault {
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
  public void constraintDeclarationExceptionThrownTest() throws Fault {
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
  public void constraintDefinitionExceptionThrownTest() throws Fault {
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
  public void validateExecutableIsInvalidForBeingShortTest() throws Fault {
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
  public void validateExecutableIsNotValidatedTest() throws Fault {
    setProperty(Property.REQUEST,
        buildRequest(Request.GET, "executable/getter"));
    setProperty(Property.STATUS_CODE, getStatusCode(Status.BAD_REQUEST));
    invoke();
    logMsg("Bean is validated as expected");
  }

}
