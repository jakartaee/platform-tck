/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jaxrs.ee.rs.matrixparam;

import javax.ws.rs.core.Response.Status;

import com.sun.ts.tests.jaxrs.ee.rs.JaxrsParamClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JaxrsParamClient {

  private static final long serialVersionUID = 1L;

  public JAXRSClient() {
    setContextRoot("/jaxrs_ee_rs_matrixparam_web/MatrixParamTest");
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
   * @testName: matrixParamStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.1; JAXRS:JAVADOC:7;
   * 
   * @test_Strategy: Client invokes GET on root resource at /MatrixParamTest;
   * Verify that named MatrixParam is handled properly
   */
  public void matrixParamStringTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest("stringtest=cts"));
    setProperty(Property.SEARCH_STRING, "stringtest=cts");
    invoke();

    setProperty(Property.REQUEST,
        buildRequest("stringtest1=cts;stringtest=ri_impl"));
    setProperty(Property.SEARCH_STRING, "stringtest=ri_impl|stringtest1=cts");
    invoke();
  }

  /*
   * @testName: matrixParamIntTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.1; JAXRS:JAVADOC:7;
   * 
   * @test_Strategy: Client invokes GET on a resource at /MatrixParamTest;
   * Verify that named MatrixParam is handled properly
   */
  public void matrixParamIntTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest("inttest1=2147483647"));
    setProperty(Property.SEARCH_STRING, "inttest1=2147483647");
    invoke();

    setProperty(Property.REQUEST,
        buildRequest("inttest1=2147483647;inttest2=-2147483648"));
    setProperty(Property.SEARCH_STRING,
        "inttest1=2147483647|inttest2=-2147483648");
    invoke();

  }

  /*
   * @testName: matrixParamDoubleTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.1; JAXRS:JAVADOC:7;
   * 
   * @test_Strategy: Client invokes GET on a resource at /MatrixParamTest;
   * Verify that named MatrixParam is handled properly
   */
  public void matrixParamDoubleTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest("doubletest1=123"));
    setProperty(Property.SEARCH_STRING, "doubletest1=123.0");
    invoke();

    setProperty(Property.REQUEST,
        buildRequest("doubletest1=12.345;doubletest2=34.567"));
    setProperty(Property.SEARCH_STRING,
        "doubletest1=12.345|doubletest2=34.567");
    invoke();

  }

  /*
   * @testName: matrixParamFloatTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.1; JAXRS:JAVADOC:7;
   * 
   * @test_Strategy: Client invokes GET on a resource at /MatrixParamTest;
   * Verify that named MatrixParam is handled properly
   */
  public void matrixParamFloatTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest("floattest1=123"));
    setProperty(Property.SEARCH_STRING, "floattest1=123.0");
    invoke();

    setProperty(Property.REQUEST,
        buildRequest("floattest1=12.345;floattest2=34.567"));
    setProperty(Property.SEARCH_STRING, "floattest1=12.345|floattest2=34.567");
    invoke();

  }

  /*
   * @testName: matrixParamLongTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.1; JAXRS:JAVADOC:7;
   * 
   * @test_Strategy: Client invokes GET on a resource at /MatrixParamTest;
   * Verify that named MatrixParam is handled properly
   */
  public void matrixParamLongTest() throws Fault {
    setProperty(Property.REQUEST,
        buildRequest("longtest=-9223372036854775808"));
    setProperty(Property.SEARCH_STRING, "longtest=-9223372036854775808");
    invoke();

    setProperty(Property.REQUEST, buildRequest(
        "longtest1=-9223372036854775808;longtest2=9223372036854775807"));
    setProperty(Property.SEARCH_STRING,
        "longtest1=-9223372036854775808|longtest2=9223372036854775807");
    invoke();
  }

  /*
   * @testName: matrixParamShortTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.1; JAXRS:JAVADOC:7;
   * 
   * @test_Strategy: Client invokes GET on a resource at /MatrixParamTest;
   * Verify that named MatrixParam is handled properly
   */
  public void matrixParamShortTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest("shorttest=-32768"));
    setProperty(Property.SEARCH_STRING, "shorttest=-32768");
    invoke();

    setProperty(Property.REQUEST,
        buildRequest("shorttest1=32767;shorttest2=-32768"));
    setProperty(Property.SEARCH_STRING, "shorttest1=32767|shorttest2=-32768");
    invoke();
  }

  /*
   * @testName: matrixParamByteTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.1; JAXRS:JAVADOC:7;
   * 
   * @test_Strategy: Client invokes GET on a resource at /MatrixParamTest;
   * Verify that named MatrixParam is handled properly
   */
  public void matrixParamByteTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest("bytetest=127"));
    setProperty(Property.SEARCH_STRING, "bytetest=127");
    invoke();

    setProperty(Property.REQUEST, buildRequest("bytetest1=123;bytetest2=-128"));
    setProperty(Property.SEARCH_STRING, "bytetest1=123|bytetest2=-128");
    invoke();
  }

  /*
   * @testName: matrixParamBooleanTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.1; JAXRS:JAVADOC:7;
   * 
   * @test_Strategy: Client invokes GET on a resource at /MatrixParamTest;
   * Verify that named MatrixParam is handled properly
   */
  public void matrixParamBooleanTest() throws Fault {
    setProperty(Property.REQUEST, buildRequest("booleantest=true"));
    setProperty(Property.SEARCH_STRING, "booleantest=true");
    invoke();

    setProperty(Property.REQUEST,
        buildRequest("booleantest1=false;booleantest2=true"));
    setProperty(Property.SEARCH_STRING, "booleantest1=false|booleantest2=true");
    invoke();
  }

  /*
   * @testName: matrixParamEntityWithConstructorTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.2; JAXRS:JAVADOC:6;
   * JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixParamEntityWithConstructorTest() throws Fault {
    paramEntityWithConstructorTest();
  }

  /*
   * @testName: matrixParamEntityWithValueOfTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.3; JAXRS:JAVADOC:6;
   * JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixParamEntityWithValueOfTest() throws Fault {
    paramEntityWithValueOfTest();
  }

  /*
   * @testName: matrixParamEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.3; JAXRS:JAVADOC:6;
   * JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixParamEntityWithFromStringTest() throws Fault {
    paramEntityWithFromStringTest();
  }

  /*
   * @testName: matrixParamSetEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.4; JAXRS:JAVADOC:6;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixParamSetEntityWithFromStringTest() throws Fault {
    paramCollectionEntityWithFromStringTest(CollectionName.SET);
  }

  /*
   * @testName: matrixParamSortedSetEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.4; JAXRS:JAVADOC:6;
   * JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixParamSortedSetEntityWithFromStringTest() throws Fault {
    paramCollectionEntityWithFromStringTest(CollectionName.SORTED_SET);
  }

  /*
   * @testName: matrixParamListEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.4; JAXRS:JAVADOC:6;
   * JAXRS:JAVADOC:12; JAXRS:JAVADOC:12.1;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixParamListEntityWithFromStringTest() throws Fault {
    paramCollectionEntityWithFromStringTest(CollectionName.LIST);
  }

  /*
   * @testName: matrixFieldParamEntityWithConstructorTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.2; JAXRS:JAVADOC:6;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixFieldParamEntityWithConstructorTest() throws Fault {
    fieldEntityWithConstructorTest();
  }

  /*
   * @testName: matrixFieldParamEntityWithValueOfTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.3; JAXRS:JAVADOC:6;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixFieldParamEntityWithValueOfTest() throws Fault {
    fieldEntityWithValueOfTest();
  }

  /*
   * @testName: matrixFieldParamEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.3; JAXRS:JAVADOC:6;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixFieldParamEntityWithFromStringTest() throws Fault {
    fieldEntityWithFromStringTest();
  }

  /*
   * @testName: matrixFieldParamSetEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.4; JAXRS:JAVADOC:6;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixFieldParamSetEntityWithFromStringTest() throws Fault {
    fieldCollectionEntityWithFromStringTest(CollectionName.SET);
  }

  /*
   * @testName: matrixFieldParamSortedSetEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.4; JAXRS:JAVADOC:6;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixFieldParamSortedSetEntityWithFromStringTest() throws Fault {
    fieldCollectionEntityWithFromStringTest(CollectionName.SORTED_SET);
  }

  /*
   * @testName: matrixFieldParamListEntityWithFromStringTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:5.4; JAXRS:JAVADOC:6;
   * 
   * @test_Strategy: Verify that named MatrixParam is handled properly
   */
  public void matrixFieldParamListEntityWithFromStringTest() throws Fault {
    fieldCollectionEntityWithFromStringTest(CollectionName.LIST);
  }

  /*
   * @testName: matrixParamEntityWithEncodedTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:7; JAXRS:SPEC:12.2;
   * 
   * @test_Strategy: Verify that named MatrixParam @Encoded is handled
   */
  public void matrixParamEntityWithEncodedTest() throws Fault {
    super.paramEntityWithEncodedTest();
  }

  /*
   * @testName: matrixFieldParamEntityWithEncodedTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:7;
   * 
   * @test_Strategy: Verify that named MatrixParam @Encoded is handled
   */
  public void matrixFieldParamEntityWithEncodedTest() throws Fault {
    super.fieldEntityWithEncodedTest();
  }

  /*
   * @testName: matrixParamThrowingWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:12.3;
   * 
   * @test_Strategy: Exceptions thrown during construction of parameter values
   * are treated the same as exceptions thrown during construction of field or
   * bean property values, see Section 3.2.
   */
  public void matrixParamThrowingWebApplicationExceptionTest() throws Fault {
    super.paramThrowingWebApplicationExceptionTest();
  }

  /*
   * @testName: matrixFieldThrowingWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:3.1; JAXRS:SPEC:8;
   * 
   * @test_Strategy: A WebApplicationException thrown during construction of
   * field or property values using 2 or 3 above is processed directly as
   * described in section 3.3.4.
   */
  public void matrixFieldThrowingWebApplicationExceptionTest() throws Fault {
    super.fieldThrowingWebApplicationExceptionTest();
  }

  /*
   * @testName: matrixParamThrowingIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:12.3;
   * 
   * @test_Strategy: Exceptions thrown during construction of parameter values
   * are treated the same as exceptions thrown during construction of field or
   * bean property values, see section 3.2.
   */
  public void matrixParamThrowingIllegalArgumentExceptionTest() throws Fault {
    setProperty(Property.UNORDERED_SEARCH_STRING, Status.NOT_FOUND.name());
    super.paramThrowingIllegalArgumentExceptionTest();
  }

  /*
   * @testName: matrixFieldThrowingIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:9; JAXRS:SPEC:9.1; JAXRS:SPEC:10;
   * 
   * @test_Strategy: Other exceptions thrown during construction of field or
   * property values using 2 or 3 above are treated as client errors:
   * 
   * if the field or property is annotated with @MatrixParam,
   * 
   * @QueryParam or @PathParam then an implementation MUST generate a
   * WebApplicationException that wraps the thrown exception with a not found
   * response (404 status) and no entity;
   */
  public void matrixFieldThrowingIllegalArgumentExceptionTest() throws Fault {
    setProperty(Property.UNORDERED_SEARCH_STRING, Status.NOT_FOUND.name());
    super.fieldThrowingIllegalArgumentExceptionTest();
  }

  @Override
  protected String buildRequest(String param) {
    StringBuilder sb = new StringBuilder();
    sb.append(Request.GET.name()).append(" ").append(_contextRoot);
    sb.append(";").append(param).append(HTTP11);
    return sb.toString();
  }

  @Override
  protected String getDefaultValueOfParam(String param) {
    StringBuilder sb = new StringBuilder();
    sb.append(param).append("=");
    sb.append(MatrixParamTest.class.getSimpleName());
    return sb.toString();
  }
}
