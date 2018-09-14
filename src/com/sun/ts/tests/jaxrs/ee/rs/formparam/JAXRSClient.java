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

package com.sun.ts.tests.jaxrs.ee.rs.formparam;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import com.sun.ts.tests.jaxrs.ee.rs.JaxrsParamClient;

/*
 * @class.setup_props: webServerHost;
 *                     webServerPort;
 *                     ts_home;
 */
public class JAXRSClient extends JaxrsParamClient {
  private static final String ENCODED = "_%60%27%24X+Y%40%22a+a%22";

  private static final String DECODED = "_`'$X Y@\"a a\"";

  public JAXRSClient() {
    setContextRoot("/jaxrs_ee_formparam_web/FormParamTest");
  }

  private static final long serialVersionUID = 1L;

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    JAXRSClient theTests = new JAXRSClient();
    theTests.run(args);
  }

  /*
   * @testName: nonDefaultFormParamNothingSentTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12;
   * 
   * @test_Strategy: Test sending no content;
   */
  public void nonDefaultFormParamNothingSentTest() throws Fault {
    defaultFormParamAndInvoke(Request.POST, "PostNonDefParam", null);
  }

  /*
   * @testName: defaultFormParamSentTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * 
   * @test_Strategy: Test sending override of default argument content;
   */
  public void defaultFormParamSentTest() throws Fault {
    setProperty(Property.CONTENT, "default_argument=" + ENCODED);
    defaultFormParamAndInvoke(Request.POST, "PostDefParam", ENCODED);
  }

  /*
   * @testName: defaultFormParamNoArgSentTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12;
   * 
   * @test_Strategy: Test sending no argument content, receiving default;
   */
  public void defaultFormParamNoArgSentTest() throws Fault {
    defaultFormParamAndInvoke(Request.POST, "PostDefParam", "default");
  }

  /*
   * @testName: defaultFormParamPutNoArgSentTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * 
   * @test_Strategy: Test sending no argument content, PUT, receiving default;
   */
  public void defaultFormParamPutNoArgSentTest() throws Fault {
    defaultFormParamAndInvoke(Request.PUT, "DefParam", "DefParam");
  }

  /*
   * @testName: defaultFormParamPutArgSentTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12;
   * 
   * @test_Strategy: Test sending argument content, PUT;
   */
  public void defaultFormParamPutArgSentTest() throws Fault {
    setProperty(Property.CONTENT, "default_argument=" + ENCODED);
    defaultFormParamAndInvoke(Request.PUT, "DefParam", DECODED);
  }

  /*
   * @testName: defaultFormParamValueOfTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * 
   * @test_Strategy: Test creating a ParamEntityWithValueOf from default;
   */
  public void defaultFormParamValueOfTest() throws Fault {
    defaultFormParamAndInvoke(Request.POST, "ParamEntityWithValueOf",
        "ValueOf");
  }

  /*
   * @testName: nonDefaultFormParamValueOfTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12;
   * 
   * @test_Strategy: Test creating a ParamEntityWithValueOf from sending a
   * String;
   */
  public void nonDefaultFormParamValueOfTest() throws Fault {
    setProperty(Property.CONTENT, "default_argument=" + ENCODED);
    defaultFormParamAndInvoke(Request.POST, "ParamEntityWithValueOf", DECODED);
  }

  /*
   * @testName: defaultFormParamFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * 
   * @test_Strategy: Test creating a ParamEntityWithFromString from default;
   */
  public void defaultFormParamFromStringTest() throws Fault {
    defaultFormParamAndInvoke(Request.POST, "ParamEntityWithFromString",
        "FromString");
  }

  /*
   * @testName: nonDefaultFormParamFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12;
   * 
   * @test_Strategy: Test creating a ParamEntityWithFromString from sending a
   * String;
   */
  public void nonDefaultFormParamFromStringTest() throws Fault {
    setProperty(Property.CONTENT, "default_argument=" + ENCODED);
    defaultFormParamAndInvoke(Request.POST, "ParamEntityWithFromString",
        ENCODED);
  }

  /*
   * @testName: defaultFormParamFromConstructorTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * 
   * @test_Strategy: Test creating a ParamEntityWithFromString from default;
   */
  public void defaultFormParamFromConstructorTest() throws Fault {
    defaultFormParamAndInvoke(Request.POST, "Constructor", "Constructor");
  }

  /*
   * @testName: nonDefaultFormParamFromConstructorTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12;
   * 
   * @test_Strategy: Test creating a ParamEntityWithConstructor from sending a
   * String;
   */
  public void nonDefaultFormParamFromConstructorTest() throws Fault {
    setProperty(Property.CONTENT, "default_argument=" + ENCODED);
    defaultFormParamAndInvoke(Request.POST, "Constructor", DECODED);
  }

  /*
   * @testName: defaultFormParamFromListConstructorTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * 
   * @test_Strategy: Test creating a ParamEntityWithConstructor from default;
   */
  public void defaultFormParamFromListConstructorTest() throws Fault {
    defaultFormParamAndInvoke(Request.POST, "ListConstructor",
        "ListConstructor");
  }

  /*
   * @testName: nonDefaultFormParamFromListConstructorTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12;
   * 
   * @test_Strategy: Test creating a ParamEntityWithConstructor from sending a
   * String;
   */
  public void nonDefaultFormParamFromListConstructorTest() throws Fault {
    setProperty(Property.CONTENT, "default_argument=" + ENCODED);
    defaultFormParamAndInvoke(Request.POST, "ListConstructor", DECODED);
  }

  /*
   * @testName: defaultFormParamFromSetFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * 
   * @test_Strategy: Test creating a ParamEntityWithFromString from default;
   */
  public void defaultFormParamFromSetFromStringTest() throws Fault {
    defaultFormParamAndInvoke(Request.POST, "SetFromString", "SetFromString");
  }

  /*
   * @testName: nonDefaultFormParamFromSetFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12;
   * 
   * @test_Strategy: Test creating a ParamEntityWithListConstructor from sending
   * a String;
   */
  public void nonDefaultFormParamFromSetFromStringTest() throws Fault {
    setProperty(Property.CONTENT, "default_argument=" + ENCODED);
    defaultFormParamAndInvoke(Request.POST, "SetFromString", ENCODED);
  }

  /*
   * @testName: defaultFormParamFromSortedSetFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * 
   * @test_Strategy: Test creating a ParamEntityWithFromString from default;
   */
  public void defaultFormParamFromSortedSetFromStringTest() throws Fault {
    defaultFormParamAndInvoke(Request.POST, "SortedSetFromString",
        "SortedSetFromString");
  }

  /*
   * @testName: nonDefaultFormParamFromSortedSetFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12;
   * 
   * @test_Strategy: Test creating a ParamEntityWithListConstructor from sending
   * a String;
   */
  public void nonDefaultFormParamFromSortedSetFromStringTest() throws Fault {
    setProperty(Property.CONTENT, "default_argument=" + ENCODED);
    defaultFormParamAndInvoke(Request.POST, "SortedSetFromString", ENCODED);
  }

  /*
   * @testName: defaultFormParamFromListFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12; JAXRS:SPEC:12.1;
   * 
   * @test_Strategy: Test creating a ParamEntityWithFromString from default;
   */
  public void defaultFormParamFromListFromStringTest() throws Fault {
    defaultFormParamAndInvoke(Request.POST, "ListFromString", "ListFromString");
  }

  /*
   * @testName: nonDefaultFormParamFromListFromStringTest
   * 
   * @assertion_ids: JAXRS:JAVADOC:4; JAXRS:SPEC:12;
   * 
   * @test_Strategy: Test creating a ParamEntityWithListConstructor from sending
   * a String;
   */
  public void nonDefaultFormParamFromListFromStringTest() throws Fault {
    setProperty(Property.CONTENT, "default_argument=" + ENCODED);
    defaultFormParamAndInvoke(Request.POST, "ListFromString", ENCODED);
  }

  /*
   * @testName: formParamEntityWithEncodedTest
   * 
   * @assertion_ids: JAXRS:SPEC:7; JAXRS:SPEC:12;JAXRS:SPEC:12.2;
   * 
   * @test_Strategy: Verify that named FormParam @Encoded is handled
   */
  public void formParamEntityWithEncodedTest() throws Fault {
    searchEqualsEncoded = true;
    super.paramEntityWithEncodedTest();
  }

  /*
   * @testName: formParamThrowingWebApplicationExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:12.3;
   * 
   * @test_Strategy: Exceptions thrown during construction of parameter values
   * are treated the same as exceptions thrown during construction of field or
   * bean property values, see Section 3.2.
   */
  public void formParamThrowingWebApplicationExceptionTest() throws Fault {
    super.paramThrowingWebApplicationExceptionTest();
  }

  /*
   * @testName: formParamThrowingIllegalArgumentExceptionTest
   * 
   * @assertion_ids: JAXRS:SPEC:12.3;
   * 
   * @test_Strategy: Exceptions thrown during construction of parameter values
   * are treated the same as exceptions thrown during construction of field or
   * bean property values, see section 3.2. Exceptions thrown during
   * construction of @FormParam annotated parameter values are treated the same
   * as if the parameter were annotated with @HeaderParam.
   */
  public void formParamThrowingIllegalArgumentExceptionTest() throws Fault {
    setProperty(Property.UNORDERED_SEARCH_STRING, Status.BAD_REQUEST.name());
    super.paramThrowingIllegalArgumentExceptionTest();
  }

  // ///////////////////////////////////////////////////////////////////////

  private void defaultFormParamAndInvoke(Request request, String method,
      String arg) throws Fault {
    setProperty(Property.REQUEST_HEADERS,
        buildContentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    setProperty(Property.REQUEST, buildRequest(request, method));
    setProperty(Property.SEARCH_STRING, FormParamTest.response(arg));
    invoke();
  }

  @Override
  protected String buildRequest(String param) {
    setProperty(Property.REQUEST_HEADERS,
        buildContentType(MediaType.APPLICATION_FORM_URLENCODED_TYPE));
    setProperty(Property.CONTENT,
        "default_argument=" + param.replace("=", "%3d"));
    return buildRequest(Request.POST, segmentFromParam(param));
  }

  // not used at the moment
  @Override
  protected String getDefaultValueOfParam(String param) {
    return null;
  }
}
