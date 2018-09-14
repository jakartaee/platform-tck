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

package com.sun.ts.tests.jaxrs.ee.rs.matrixparam.sub;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSSubClient
    extends com.sun.ts.tests.jaxrs.ee.rs.matrixparam.JAXRSClient {

  private static final long serialVersionUID = 1L;

  public JAXRSSubClient() {
    setContextRoot("/jaxrs_ee_rs_matrixparam_sub_web/resource/subresource");
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
   * @testName: matrixParamStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:7;
   * 
   * @test_Strategy: Client invokes GET on root resource at /MatrixParamTest;
   * Verify that named MatrixParam is handled properly
   */
  public void matrixParamStringTest() throws Fault {
    super.matrixParamStringTest();
  }

  /*
   * @testName: matrixParamIntTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:7;
   * 
   * @test_Strategy: Client invokes GET on a resource at /MatrixParamTest;
   * Verify that named MatrixParam is handled properly
   */
  public void matrixParamIntTest() throws Fault {
    super.matrixParamIntTest();
  }

  /*
   * @testName: matrixParamDoubleTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:7;
   * 
   * @test_Strategy: Client invokes GET on a resource at /MatrixParamTest;
   * Verify that named MatrixParam is handled properly
   */
  public void matrixParamDoubleTest() throws Fault {
    super.matrixParamDoubleTest();
  }

  /*
   * @testName: matrixParamFloatTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:7;
   * 
   * @test_Strategy: Client invokes GET on a resource at /MatrixParamTest;
   * Verify that named MatrixParam is handled properly
   */
  public void matrixParamFloatTest() throws Fault {
    super.matrixParamFloatTest();
  }

  /*
   * @testName: matrixParamLongTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:7;
   * 
   * @test_Strategy: Client invokes GET on a resource at /MatrixParamTest;
   * Verify that named MatrixParam is handled properly
   */
  public void matrixParamLongTest() throws Fault {
    super.matrixParamLongTest();
  }

  /*
   * @testName: matrixParamShortTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:7;
   * 
   * @test_Strategy: Client invokes GET on a resource at /MatrixParamTest;
   * Verify that named MatrixParam is handled properly
   */
  public void matrixParamShortTest() throws Fault {
    super.matrixParamShortTest();
  }

  /*
   * @testName: matrixParamByteTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:7;
   * 
   * @test_Strategy: Client invokes GET on a resource at /MatrixParamTest;
   * Verify that named MatrixParam is handled properly
   */
  public void matrixParamByteTest() throws Fault {
    super.matrixParamByteTest();
  }

  /*
   * @testName: matrixParamBooleanTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.1; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:7;
   * 
   * @test_Strategy: Client invokes GET on a resource at /MatrixParamTest;
   * Verify that named MatrixParam is handled properly
   */
  public void matrixParamBooleanTest() throws Fault {
    super.matrixParamBooleanTest();
  }

  /*
   * @testName: matrixParamEntityWithConstructorTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.2; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixParamEntityWithConstructorTest() throws Fault {
    super.matrixParamEntityWithConstructorTest();
  }

  /*
   * @testName: matrixParamEntityWithValueOfTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.3; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixParamEntityWithValueOfTest() throws Fault {
    super.matrixParamEntityWithValueOfTest();
  }

  /*
   * @testName: matrixParamEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.3; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixParamEntityWithFromStringTest() throws Fault {
    super.matrixParamEntityWithFromStringTest();
  }

  /*
   * @testName: matrixParamSetEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.4; JAXRS:JAVADOC:6;
   * JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixParamSetEntityWithFromStringTest() throws Fault {
    super.matrixParamSetEntityWithFromStringTest();
  }

  /*
   * @testName: matrixParamSortedSetEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.4; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixParamSortedSetEntityWithFromStringTest() throws Fault {
    super.matrixParamSortedSetEntityWithFromStringTest();
  }

  /*
   * @testName: matrixParamListEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.4; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:JAVADOC:6; JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixParamListEntityWithFromStringTest() throws Fault {
    super.matrixParamListEntityWithFromStringTest();
  }

  /*
   * @testName: matrixFieldParamEntityWithConstructorTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.2; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:SPEC:4; JAXRS:JAVADOC:6;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   * 
   * An implementation is only required to set the annotated field and bean
   * property values of instances created by the implementation runtime. (Check
   * the resource with resource locator is injected field properties)
   */
  public void matrixFieldParamEntityWithConstructorTest() throws Fault {
    fieldEntityWithConstructorTest();
  }

  /*
   * @testName: matrixFieldParamEntityWithValueOfTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.3; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:SPEC:4; JAXRS:JAVADOC:6;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   * 
   * An implementation is only required to set the annotated field and bean
   * property values of instances created by the implementation runtime. (Check
   * the resource with resource locator is injected field properties)
   */
  public void matrixFieldParamEntityWithValueOfTest() throws Fault {
    super.matrixFieldParamEntityWithValueOfTest();
  }

  /*
   * @testName: matrixFieldParamEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.3; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:SPEC:4; JAXRS:JAVADOC:6;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   * 
   * An implementation is only required to set the annotated field and bean
   * property values of instances created by the implementation runtime. (Check
   * the resource with resource locator is injected field properties)
   */
  public void matrixFieldParamEntityWithFromStringTest() throws Fault {
    super.matrixFieldParamEntityWithFromStringTest();
  }

  /*
   * @testName: matrixFieldParamSetEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.4; JAXRS:JAVADOC:6;
   * JAXRS:SPEC:20; JAXRS:SPEC:20.2; JAXRS:SPEC:4;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   * 
   * An implementation is only required to set the annotated field and bean
   * property values of instances created by the implementation runtime. (Check
   * the resource with resource locator is injected field properties)
   */
  public void matrixFieldParamSetEntityWithFromStringTest() throws Fault {
    super.matrixFieldParamSetEntityWithFromStringTest();
  }

  /*
   * @testName: matrixFieldParamSortedSetEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.4; JAXRS:JAVADOC:6;
   * JAXRS:SPEC:20; JAXRS:SPEC:20.2; JAXRS:SPEC:4;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   * 
   * An implementation is only required to set the annotated field and bean
   * property values of instances created by the implementation runtime. (Check
   * the resource with resource locator is injected field properties)
   */
  public void matrixFieldParamSortedSetEntityWithFromStringTest() throws Fault {
    super.matrixFieldParamSortedSetEntityWithFromStringTest();
  }

  /*
   * @testName: matrixFieldParamListEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.4; JAXRS:JAVADOC:6;
   * JAXRS:SPEC:20; JAXRS:SPEC:20.2; JAXRS:SPEC:4;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   * 
   * An implementation is only required to set the annotated field and bean
   * property values of instances created by the implementation runtime. (Check
   * the resource with resource locator is injected field properties)
   */
  public void matrixFieldParamListEntityWithFromStringTest() throws Fault {
    super.matrixFieldParamListEntityWithFromStringTest();
  }

  /*
   * @testName: matrixParamEntityWithEncodedTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:7; JAXRS:SPEC:12.2;
   * JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Verify that named MatrixParam @Encoded is handled
   */
  public void matrixParamEntityWithEncodedTest() throws Fault {
    super.matrixParamEntityWithEncodedTest();
  }

  /*
   * @testName: matrixFieldParamEntityWithEncodedTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:7; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Verify that named MatrixParam @Encoded is handled
   */
  public void matrixFieldParamEntityWithEncodedTest() throws Fault {
    super.matrixFieldParamEntityWithEncodedTest();
  }

  /*
   * @testName: matrixParamThrowingWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:12.3; JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Exceptions thrown during construction of parameter values
   * are treated the same as exceptions thrown during construction of field or
   * bean property values, see Section 3.2.
   */
  public void matrixParamThrowingWebApplicationExceptionTest() throws Fault {
    super.matrixParamThrowingWebApplicationExceptionTest();
  }

  /*
   * @testName: matrixFieldThrowingWebApplicationExceptionTest
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
  public void matrixFieldThrowingWebApplicationExceptionTest() throws Fault {
    super.matrixFieldThrowingWebApplicationExceptionTest();
  }

  /*
   * @testName: matrixParamThrowingIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:12.3; JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Exceptions thrown during construction of parameter values
   * are treated the same as exceptions thrown during construction of field or
   * bean property values, see section 3.2.
   */
  public void matrixParamThrowingIllegalArgumentExceptionTest() throws Fault {
    super.matrixParamThrowingIllegalArgumentExceptionTest();
  }

  /*
   * @testName: matrixFieldThrowingIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:9; JAXRS:SPEC:9.1; JAXRS:SPEC:10; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2; JAXRS:SPEC:4;
   * 
   * @test_Strategy: Other exceptions thrown during construction of field or
   * property values using 2 or 3 above are treated as client errors:
   * 
   * if the field or property is annotated with @MatrixParam,
   * 
   * @QueryParam or @PathParam then an implementation MUST generate a
   * WebApplicationException that wraps the thrown exception with a not found
   * response (404 status) and no entity;
   *
   * An implementation is only required to set the annotated field and bean
   * property values of instances created by the implementation runtime. (Check
   * the resource with resource locator is injected field properties)
   */
  public void matrixFieldThrowingIllegalArgumentExceptionTest() throws Fault {
    super.matrixFieldThrowingIllegalArgumentExceptionTest();
  }
}
