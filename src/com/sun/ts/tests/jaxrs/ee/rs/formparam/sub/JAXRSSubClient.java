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

package com.sun.ts.tests.jaxrs.ee.rs.formparam.sub;

import com.sun.ts.tests.jaxrs.ee.rs.formparam.JAXRSClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSSubClient extends JAXRSClient {

  public JAXRSSubClient() {
    setContextRoot("/jaxrs_ee_formparam_sub_web/resource/sub");
  }

  private static final long serialVersionUID = 1L;

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    JAXRSSubClient theTests = new JAXRSSubClient();
    theTests.run(args);
  }

  /*
   * @testName: nonDefaultFormParamNothingSentTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test sending no content;
   */
  public void nonDefaultFormParamNothingSentTest() throws Fault {
    super.nonDefaultFormParamNothingSentTest();
  }

  /*
   * @testName: defaultFormParamSentTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test sending override of default argument content;
   */
  public void defaultFormParamSentTest() throws Fault {
    super.defaultFormParamSentTest();
  }

  /*
   * @testName: defaultFormParamNoArgSentTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test sending no argument content, receiving default;
   */
  public void defaultFormParamNoArgSentTest() throws Fault {
    super.defaultFormParamNoArgSentTest();
  }

  /*
   * @testName: defaultFormParamPutNoArgSentTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test sending no argument content, PUT, receiving default;
   */
  public void defaultFormParamPutNoArgSentTest() throws Fault {
    super.defaultFormParamPutNoArgSentTest();
  }

  /*
   * @testName: defaultFormParamPutArgSentTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test sending argument content, PUT;
   */
  public void defaultFormParamPutArgSentTest() throws Fault {
    super.defaultFormParamPutArgSentTest();
  }

  /*
   * @testName: defaultFormParamValueOfTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test creating a ParamEntityWithValueOf from default;
   */
  public void defaultFormParamValueOfTest() throws Fault {
    super.defaultFormParamValueOfTest();
  }

  /*
   * @testName: nonDefaultFormParamValueOfTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test creating a ParamEntityWithValueOf from sending a
   * String;
   */
  public void nonDefaultFormParamValueOfTest() throws Fault {
    super.nonDefaultFormParamValueOfTest();
  }

  /*
   * @testName: defaultFormParamFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test creating a ParamEntityWithFromString from default;
   */
  public void defaultFormParamFromStringTest() throws Fault {
    super.defaultFormParamFromStringTest();
  }

  /*
   * @testName: nonDefaultFormParamFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test creating a ParamEntityWithFromString from sending a
   * String;
   */
  public void nonDefaultFormParamFromStringTest() throws Fault {
    super.nonDefaultFormParamFromStringTest();
  }

  /*
   * @testName: defaultFormParamFromConstructorTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test creating a ParamEntityWithFromString from default;
   */
  public void defaultFormParamFromConstructorTest() throws Fault {
    super.defaultFormParamFromConstructorTest();
  }

  /*
   * @testName: nonDefaultFormParamFromConstructorTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test creating a ParamEntityWithConstructor from sending a
   * String;
   */
  public void nonDefaultFormParamFromConstructorTest() throws Fault {
    super.nonDefaultFormParamFromConstructorTest();
  }

  /*
   * @testName: defaultFormParamFromListConstructorTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test creating a ParamEntityWithConstructor from default;
   */
  public void defaultFormParamFromListConstructorTest() throws Fault {
    super.defaultFormParamFromListConstructorTest();
  }

  /*
   * @testName: nonDefaultFormParamFromListConstructorTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test creating a ParamEntityWithConstructor from sending a
   * String;
   */
  public void nonDefaultFormParamFromListConstructorTest() throws Fault {
    super.nonDefaultFormParamFromListConstructorTest();
  }

  /*
   * @testName: defaultFormParamFromSetFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test creating a ParamEntityWithFromString from default;
   */
  public void defaultFormParamFromSetFromStringTest() throws Fault {
    super.defaultFormParamFromSetFromStringTest();
  }

  /*
   * @testName: nonDefaultFormParamFromSetFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test creating a ParamEntityWithListConstructor from sending
   * a String;
   */
  public void nonDefaultFormParamFromSetFromStringTest() throws Fault {
    super.nonDefaultFormParamFromSetFromStringTest();
  }

  /*
   * @testName: defaultFormParamFromSortedSetFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test creating a ParamEntityWithFromString from default;
   */
  public void defaultFormParamFromSortedSetFromStringTest() throws Fault {
    super.defaultFormParamFromSortedSetFromStringTest();
  }

  /*
   * @testName: nonDefaultFormParamFromSortedSetFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test creating a ParamEntityWithListConstructor from sending
   * a String;
   */
  public void nonDefaultFormParamFromSortedSetFromStringTest() throws Fault {
    super.nonDefaultFormParamFromSortedSetFromStringTest();
  }

  /*
   * @testName: defaultFormParamFromListFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test creating a ParamEntityWithFromString from default;
   * 
   */
  public void defaultFormParamFromListFromStringTest() throws Fault {
    super.defaultFormParamFromListFromStringTest();
  }

  /*
   * @testName: nonDefaultFormParamFromListFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Test creating a ParamEntityWithListConstructor from sending
   * a String;
   */
  public void nonDefaultFormParamFromListFromStringTest() throws Fault {
    super.nonDefaultFormParamFromListFromStringTest();
  }

  /*
   * @assertion_ids: JAXRS:SPEC:7; JAXRS:SPEC:12;JAXRS:SPEC:12.2; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Verify that named FormParam @Encoded is handled
   */
  public void formParamEntityWithEncodedTest() throws Fault {
    super.paramEntityWithEncodedTest();
  }

  /*
   * @testName: formParamThrowingWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:12.3; JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Exceptions thrown during construction of parameter values
   * are treated the same as exceptions thrown during construction of field or
   * bean property values, see Section 3.2.
   */
  public void formParamThrowingWebApplicationExceptionTest() throws Fault {
    super.formParamThrowingIllegalArgumentExceptionTest();
  }

  /*
   * @testName: formParamThrowingIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:12.3; JAXRS:SPEC:20; JAXRS:SPEC:20.2;
   * 
   * @test_Strategy: Exceptions thrown during construction of parameter values
   * are treated the same as exceptions thrown during construction of field or
   * bean property values, see section 3.2. Exceptions thrown during
   * construction of @FormParam annotated parameter values are treated the same
   * as if the parameter were annotated with @HeaderParam.
   */
  public void formParamThrowingIllegalArgumentExceptionTest() throws Fault {
    super.formParamThrowingIllegalArgumentExceptionTest();
  }
}
