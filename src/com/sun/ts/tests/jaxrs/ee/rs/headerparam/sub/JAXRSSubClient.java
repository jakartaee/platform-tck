/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jaxrs.ee.rs.headerparam.sub;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSSubClient
    extends com.sun.ts.tests.jaxrs.ee.rs.headerparam.JAXRSClient {

  private static final long serialVersionUID = -7534318281215084279L;

  public JAXRSSubClient() {
    setContextRoot("/jaxrs_ee_rs_headerparam_sub_web/resource/subresource");
  }

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    new JAXRSSubClient().run(args);
  }

  /* Run test */
  /*
   * @testName: headerParamStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:3; JAXRS:JAVADOC:5;
   * 
   * @test_Strategy: Client invokes HEAD on root resource at /HeaderParamTest;
   * Verify that right Method is invoked.
   */
  public void headerParamStringTest() throws Fault {
    super.headerParamStringTest();
  }

  /*
   * @testName: headerParamNoQueryTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:5;
   * 
   * @test_Strategy: Client invokes GET on a resource at /HeaderParamTest;
   * Verify that right Method is invoked.
   */
  public void headerParamNoQueryTest() throws Fault {
    super.headerParamNoQueryTest();
  }

  /*
   * @testName: headerParamIntTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:5;
   * 
   * @test_Strategy: Client invokes GET on a resource at /HeaderParamTest;
   * Verify that right Method is invoked.
   */
  public void headerParamIntTest() throws Fault {
    super.headerParamIntTest();
  }

  /*
   * @testName: headerParamDoubleTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:5;
   * 
   * @test_Strategy: Client invokes GET on a resource at /HeaderParamTest;
   * Verify that right Method is invoked.
   */
  public void headerParamDoubleTest() throws Fault {
    super.headerParamDoubleTest();
  }

  /*
   * @testName: headerParamFloatTest
   *
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:5;
   *
   * @test_Strategy: Client invokes GET on a resource at /HeaderParamTest;
   * Verify that right Method is invoked.
   */
  public void headerParamFloatTest() throws Fault {
    super.headerParamFloatTest();
  }

  /*
   * @testName: headerParamLongTest
   *
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:5;
   *
   * @test_Strategy: Client invokes GET on a sub resource at /HeaderParamTest;
   * Verify that right Method is invoked.
   */
  public void headerParamLongTest() throws Fault {
    super.headerParamLongTest();
  }

  /*
   * @testName: headerParamShortTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:5;
   * 
   * @test_Strategy: Client invokes GET on a sub resource at /HeaderParamTest;
   * Verify that right Method is invoked.
   */
  public void headerParamShortTest() throws Fault {
    super.headerParamShortTest();
  }

  /*
   * @testName: headerParamByteTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:5;
   * 
   * @test_Strategy: Client invokes GET on a sub resource at /HeaderParamTest;
   * Verify that right Method is invoked.
   */
  public void headerParamByteTest() throws Fault {
    super.headerParamByteTest();
  }

  /*
   * @testName: headerParamBooleanTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:5;
   * 
   * @test_Strategy: Client invokes GET on a sub resource at /HeaderParamTest;
   * Verify that right Method is invoked.
   */
  public void headerParamBooleanTest() throws Fault {
    super.headerParamBooleanTest();
  }

  /*
   * @testName: headerParamEntityWithConstructorTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.2; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named QueryParam is handled properly
   */
  public void headerParamEntityWithConstructorTest() throws Fault {
    super.headerParamEntityWithConstructorTest();
  }

  /*
   * @testName: headerParamEntityWithValueOfTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.3; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named QueryParam is handled properly
   */
  public void headerParamEntityWithValueOfTest() throws Fault {
    super.headerParamEntityWithValueOfTest();
  }

  /*
   * @testName: headerParamEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.3; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named QueryParam is handled properly
   */
  public void headerParamEntityWithFromStringTest() throws Fault {
    super.headerParamEntityWithFromStringTest();
  }

  /*
   * @testName: headerParamSetEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.4; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named QueryParam is handled properly
   */
  public void headerParamSetEntityWithFromStringTest() throws Fault {
    super.headerParamSetEntityWithFromStringTest();
  }

  /*
   * @testName: headerParamSortedSetEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.4; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named QueryParam is handled properly
   */
  public void headerParamSortedSetEntityWithFromStringTest() throws Fault {
    super.headerParamSortedSetEntityWithFromStringTest();
  }

  /*
   * @testName: headerParamListEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.4; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named QueryParam is handled properly
   */
  public void headerParamListEntityWithFromStringTest() throws Fault {
    super.headerParamListEntityWithFromStringTest();
  }

  /*
   * @testName: headerFieldParamEntityWithConstructorTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.2; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:SPEC:4; JAXRS:JAVADOC:6;
   * 
   * @test_Strategy: Verify that named QueryParam is handled properly
   */
  public void headerFieldParamEntityWithConstructorTest() throws Fault {
    super.headerFieldParamEntityWithConstructorTest();
  }

  /*
   * @testName: headerFieldParamEntityWithValueOfTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.3; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:SPEC:4; JAXRS:JAVADOC:6;
   * 
   * @test_Strategy: Verify that named QueryParam is handled properly
   * 
   * An implementation is only required to set the annotated field and bean
   * property values of instances created by the implementation runtime. (Check
   * the resource with resource locator is injected field properties)
   */
  public void headerFieldParamEntityWithValueOfTest() throws Fault {
    super.headerFieldParamEntityWithValueOfTest();
  }

  /*
   * @testName: headerFieldParamEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.3; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:SPEC:4; JAXRS:JAVADOC:6;
   * 
   * @test_Strategy: Verify that named QueryParam is handled properly
   * 
   * An implementation is only required to set the annotated field and bean
   * property values of instances created by the implementation runtime. (Check
   * the resource with resource locator is injected field properties)
   */
  public void headerFieldParamEntityWithFromStringTest() throws Fault {
    super.headerFieldParamEntityWithFromStringTest();
  }

  /*
   * @testName: headerFieldParamSetEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.4; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:SPEC:4; JAXRS:JAVADOC:6;
   * 
   * @test_Strategy: Verify that named QueryParam is handled properly
   * 
   * An implementation is only required to set the annotated field and bean
   * property values of instances created by the implementation runtime. (Check
   * the resource with resource locator is injected field properties)
   */
  public void headerFieldParamSetEntityWithFromStringTest() throws Fault {
    super.headerFieldParamSetEntityWithFromStringTest();
  }

  /*
   * @testName: headerFieldParamSortedSetEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.4; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:SPEC:4; JAXRS:JAVADOC:6;
   * 
   * @test_Strategy: Verify that named QueryParam is handled properly
   * 
   * An implementation is only required to set the annotated field and bean
   * property values of instances created by the implementation runtime. (Check
   * the resource with resource locator is injected field properties)
   */
  public void headerFieldParamSortedSetEntityWithFromStringTest() throws Fault {
    super.headerFieldParamSortedSetEntityWithFromStringTest();
  }

  /*
   * @testName: headerFieldParamListEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.4; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:SPEC:4; JAXRS:JAVADOC:6;
   * 
   * @test_Strategy: Verify that named QueryParam is handled properly
   * 
   * An implementation is only required to set the annotated field and bean
   * property values of instances created by the implementation runtime. (Check
   * the resource with resource locator is injected field properties)
   */
  public void headerFieldParamListEntityWithFromStringTest() throws Fault {
    super.headerFieldParamListEntityWithFromStringTest();
  }

  /*
   * @testName: headerParamThrowingWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:12.3; JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Exceptions thrown during construction of parameter values
   * are treated the same as exceptions thrown during construction of field or
   * bean property values, see Section 3.2.
   */
  public void headerParamThrowingWebApplicationExceptionTest() throws Fault {
    super.headerParamThrowingWebApplicationExceptionTest();
  }

  /*
   * @testName: headerFieldThrowingWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:8; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:SPEC:4;
   * 
   * @test_Strategy: A WebApplicationException thrown during construction of
   * field or property values using 2 or 3 above is processed directly as
   * described in section 3.3.4.
   * 
   * An implementation is only required to set the annotated field and bean
   * property values of instances created by the implementation runtime. (Check
   * the resource with resource locator is injected field properties)
   */
  public void headerFieldThrowingWebApplicationExceptionTest() throws Fault {
    super.headerFieldThrowingWebApplicationExceptionTest();
  }

  /*
   * @testName: headerParamThrowingIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:12.3; JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Exceptions thrown during construction of parameter values
   * are treated the same as exceptions thrown during construction of field or
   * bean property values, see section 3.2.
   */
  public void headerParamThrowingIllegalArgumentExceptionTest() throws Fault {
    super.headerParamThrowingIllegalArgumentExceptionTest();
  }

  /*
   * @testName: headerFieldThrowingIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:9; JAXRS:SPEC:9.2; JAXRS:SPEC:10; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:SPEC:4;
   * 
   * @test_Strategy: Other exceptions thrown during construction of field or
   * property values using 2 or 3 above are treated as client errors:
   *
   * if the field or property is annotated with @HeaderParam or @CookieParam
   * then an implementation MUST generate a WebApplicationException that wraps
   * the thrown exception with a client error response (400 status) and no
   * entity.
   *
   * An implementation is only required to set the annotated field and bean
   * property values of instances created by the implementation runtime. (Check
   * the resource with resource locator is injected field properties)
   */
  public void headerFieldThrowingIllegalArgumentExceptionTest() throws Fault {
    super.headerFieldThrowingIllegalArgumentExceptionTest();
  }
}
