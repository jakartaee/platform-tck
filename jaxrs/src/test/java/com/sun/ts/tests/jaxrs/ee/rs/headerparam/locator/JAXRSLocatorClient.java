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

package com.sun.ts.tests.jaxrs.ee.rs.headerparam.locator;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSLocatorClient
    extends com.sun.ts.tests.jaxrs.ee.rs.headerparam.JAXRSClient {
  private static final long serialVersionUID = 1L;

  public JAXRSLocatorClient() {
    setContextRoot("/jaxrs_ee_rs_headerparam_locator_web/resource/locator");
  }

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    new JAXRSLocatorClient().run(args);
  }

  /* Run test */
  /*
   * @testName: headerParamStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.5; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.3; JAXRS:JAVADOC:3; JAXRS:JAVADOC:5;
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
   * JAXRS:SPEC:20.3; JAXRS:JAVADOC:5;
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
   * JAXRS:SPEC:20.3; JAXRS:JAVADOC:5;
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
   * JAXRS:SPEC:20.3; JAXRS:JAVADOC:5;
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
   * JAXRS:SPEC:20.3; JAXRS:JAVADOC:5;
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
   * JAXRS:SPEC:20.3; JAXRS:JAVADOC:5;
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
   * JAXRS:SPEC:20.3; JAXRS:JAVADOC:5;
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
   * JAXRS:SPEC:20.3; JAXRS:JAVADOC:5;
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
   * JAXRS:SPEC:20.3; JAXRS:JAVADOC:5;
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
   * JAXRS:SPEC:20.3; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
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
   * JAXRS:SPEC:20.3; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
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
   * JAXRS:SPEC:20.3; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
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
   * JAXRS:SPEC:20.3; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
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
   * JAXRS:SPEC:20.3; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
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
   * JAXRS:SPEC:20.3; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named QueryParam is handled properly
   */
  public void headerParamListEntityWithFromStringTest() throws Fault {
    super.headerParamListEntityWithFromStringTest();
  }

  /*
   * @testName: headerParamThrowingWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:12.3; JAXRS:SPEC:20; JAXRS:SPEC:20.3;
   * 
   * @test_Strategy: Exceptions thrown during construction of parameter values
   * are treated the same as exceptions thrown during construction of field or
   * bean property values, see Section 3.2.
   */
  public void headerParamThrowingWebApplicationExceptionTest() throws Fault {
    super.headerParamThrowingWebApplicationExceptionTest();
  }

  /*
   * @testName: headerParamThrowingIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:12.3; JAXRS:SPEC:20; JAXRS:SPEC:20.3;
   * 
   * @test_Strategy: Exceptions thrown during construction of parameter values
   * are treated the same as exceptions thrown during construction of field or
   * bean property values, see section 3.2.
   */
  public void headerParamThrowingIllegalArgumentExceptionTest() throws Fault {
    super.headerParamThrowingIllegalArgumentExceptionTest();
  }

  /**
   * change the GET request to POST request so that the new resources will
   * handle it
   */
  @Override
  protected String buildRequest(Request type, String... path) {
    return super.buildRequest(Request.POST, path);
  }

}
