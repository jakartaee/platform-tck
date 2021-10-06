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

package com.sun.ts.tests.jaxrs.ee.rs.formparam.locator;

import com.sun.ts.tests.jaxrs.ee.rs.formparam.JAXRSClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSLocatorClient extends JAXRSClient {

  public JAXRSLocatorClient() {
    setContextRoot("/jaxrs_ee_formparam_locator_web/resource/locator");
  }

  private static final long serialVersionUID = 1L;

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    JAXRSLocatorClient theTests = new JAXRSLocatorClient();
    theTests.run(args);
  }

  /*
   * @testName: nonDefaultFormParamNothingSentTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.3;
   * 
   * @test_Strategy: Test sending no content;
   */
  public void nonDefaultFormParamNothingSentTest() throws Fault {
    super.nonDefaultFormParamNothingSentTest();
  }

  /*
   * @testName: nonDefaultFormParamValueOfTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.3;
   * 
   * @test_Strategy: Test creating a ParamEntityWithValueOf from sending a
   * String;
   */
  public void nonDefaultFormParamValueOfTest() throws Fault {
    super.nonDefaultFormParamValueOfTest();
  }

  /*
   * @testName: nonDefaultFormParamFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.3;
   * 
   * @test_Strategy: Test creating a ParamEntityWithFromString from sending a
   * String;
   */
  public void nonDefaultFormParamFromStringTest() throws Fault {
    _contextRoot += "encoded";
    super.nonDefaultFormParamFromStringTest();
  }

  /*
   * @testName: nonDefaultFormParamFromConstructorTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.3;
   * 
   * @test_Strategy: Test creating a ParamEntityWithConstructor from sending a
   * String;
   */
  public void nonDefaultFormParamFromConstructorTest() throws Fault {
    super.nonDefaultFormParamFromConstructorTest();
  }

  /*
   * @testName: nonDefaultFormParamFromListConstructorTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.3;
   * 
   * @test_Strategy: Test creating a ParamEntityWithConstructor from sending a
   * String;
   */
  public void nonDefaultFormParamFromListConstructorTest() throws Fault {
    super.nonDefaultFormParamFromListConstructorTest();
  }

  /*
   * @testName: nonDefaultFormParamFromSetFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.3;
   * 
   * @test_Strategy: Test creating a ParamEntityWithListConstructor from sending
   * a String;
   */
  public void nonDefaultFormParamFromSetFromStringTest() throws Fault {
    _contextRoot += "encoded";
    super.nonDefaultFormParamFromSetFromStringTest();
  }

  /*
   * @testName: nonDefaultFormParamFromSortedSetFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.3;
   * 
   * @test_Strategy: Test creating a ParamEntityWithListConstructor from sending
   * a String;
   */
  public void nonDefaultFormParamFromSortedSetFromStringTest() throws Fault {
    _contextRoot += "encoded";
    super.nonDefaultFormParamFromSortedSetFromStringTest();
  }

  /*
   * @testName: nonDefaultFormParamFromListFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.3;
   * 
   * @test_Strategy: Test creating a ParamEntityWithListConstructor from sending
   * a String;
   */
  public void nonDefaultFormParamFromListFromStringTest() throws Fault {
    _contextRoot += "encoded";
    super.nonDefaultFormParamFromListFromStringTest();
  }

  /*
   * @assertion_ids: JAXRS:SPEC:7; JAXRS:SPEC:12;JAXRS:SPEC:12.2; JAXRS:SPEC:20;
   * JAXRS:SPEC:20.3;
   * 
   * @test_Strategy: Verify that named FormParam @Encoded is handled
   */
  public void formParamEntityWithEncodedTest() throws Fault {
    _contextRoot += "encoded";
    super.paramEntityWithEncodedTest();
  }

}
