/*
 * Copyright (c) 2015, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrs.platform.beanvalidation.constraintviolationexceptionmapper;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.Response.Status;

import com.sun.ts.tests.jaxrs.common.client.JaxrsCommonClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
/**
 * Test the exception mapper is used
 * 
 * @since 2.1.0
 */
public class JAXRSClient extends JaxrsCommonClient {

  private static final long serialVersionUID = 210L;

  public JAXRSClient() {
    setContextRoot(
        "/jaxrs_platform_beanvalidation_constraintviolationexceptionmapper_web/resource");
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
    setProperty(Property.SEARCH_STRING,
        ConstraintViolationException.class.getName());
    invoke();
    logMsg("ExceptionMapper works as expected");
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
    setProperty(Property.SEARCH_STRING,
        ConstraintViolationException.class.getName());
    invoke();
    logMsg("ExceptionMapper works as expected");

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
    setProperty(Property.SEARCH_STRING,
        ConstraintViolationException.class.getName());
    invoke();
    logMsg("ExceptionMapper works as expected");

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
    setProperty(Property.SEARCH_STRING,
        ConstraintViolationException.class.getName());
    invoke();
    logMsg("ExceptionMapper works as expected");
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
    logMsg("ExceptionMapper works as expected");

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
    logMsg("ExceptionMapper works as expected");

  }

  /*
   * @testName: validateExecutableIsInvalidForBeingShortTest
   * 
   * @assertion_ids: JAXRS:SPEC:101; JAXRS:SPEC:102; JAXRS:SPEC:102.2;
   * 
   * @test_Strategy: JAX-RS implementations MUST follow the constraint
   * annotation rules defined in Bean Validation 1.1. JSR
   * 
   * Due to validation of whole bean status 400 is returned
   */
  public void validateExecutableIsInvalidForBeingShortTest() throws Fault {
    setProperty(Property.REQUEST,
        buildRequest(Request.GET, "executable/nogetter"));
    setProperty(Property.SEARCH_STRING,
        ConstraintViolationException.class.getName());
    invoke();
    logMsg("ExceptionMapper works as expected");
  }
}
