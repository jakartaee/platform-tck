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

package com.sun.ts.tests.jsp.spec.core_syntax.actions.setproperty;

import java.io.PrintWriter;
import com.sun.ts.lib.harness.EETest.Fault;

import com.sun.javatest.Status;

import com.sun.ts.tests.jsp.common.client.AbstractUrlClient;

public class URLClient extends AbstractUrlClient {

  /**
   * Entry point for different-VM execution. It should delegate to method
   * run(String[], PrintWriter, PrintWriter), and this method should not contain
   * any test configuration.
   */
  public static void main(String[] args) {
    URLClient theTests = new URLClient();
    Status s = theTests.run(args, new PrintWriter(System.out),
        new PrintWriter(System.err));
    s.exit();
  }

  /**
   * Entry point for same-VM execution. In different-VM execution, the main
   * method delegates to this method.
   */
  public Status run(String args[], PrintWriter out, PrintWriter err) {

    setGeneralURI("/jsp/spec/core_syntax/actions/setproperty");
    setContextRoot("/jsp_coresyntx_act_setproperty_web");

    return super.run(args, out, err);
  }

  /*
   * @class.setup_props: webServerHost; webServerPort; ts_home;
   *
   */

  /* Run test */

  /*
   * @testName: positiveSetBooleanObjTest
   * 
   * @assertion_ids: JSP:SPEC:87; JSP:SPEC:162.10
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set a
   * Boolean property of the bean using a String constant. PENDING Merge with
   * prim test
   */

  public void positiveSetBooleanObjTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetBooleanObj");
    invoke();
  }

  /*
   * @testName: positiveSetBooleanPrimTest
   * 
   * @assertion_ids: JSP:SPEC:87
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set a
   * boolean property of the bean using a String constant.
   *
   */

  public void positiveSetBooleanPrimTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetBooleanPrim");
    invoke();
  }

  /*
   * @testName: positiveSetByteObjTest
   * 
   * @assertion_ids: JSP:SPEC:88;JSP:SPEC:162.10
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set a
   * Byte property of the bean using a String constant. PENDING Merge with prim
   * test
   */

  public void positiveSetByteObjTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetByteObj");
    invoke();
  }

  /*
   * @testName: positiveSetBytePrimTest
   * 
   * @assertion_ids: JSP:SPEC:88
   * 
   * @test_Strategy: Set a byte property of the bean using a String constant.
   *
   */

  public void positiveSetBytePrimTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetBytePrim");
    invoke();
  }

  /*
   * @testName: positiveSetCharObjTest
   * 
   * @assertion_ids: JSP:SPEC:89;JSP:SPEC:162.10
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set a
   * Character property of the bean using a String constant. PENDING Merge with
   * prim test
   */

  public void positiveSetCharObjTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetCharObj");
    invoke();
  }

  /*
   * @testName: positiveSetCharPrimTest
   * 
   * @assertion_ids: JSP:SPEC:89
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set a
   * char property of the bean using a String constant.
   *
   */

  public void positiveSetCharPrimTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetCharPrim");
    invoke();
  }

  /*
   * @testName: positiveSetDoubleObjTest
   * 
   * @assertion_ids: JSP:SPEC:90; JSP:SPEC:162.10
   * 
   * @test_Strategy: and set a Double property of the bean using a String
   * constant. PENDING Merge with prim test
   */

  public void positiveSetDoubleObjTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetDoubleObj");
    invoke();
  }

  /*
   * @testName: positiveSetDoublePrimTest
   * 
   * @assertion_ids: JSP:SPEC:90
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set a
   * double property of the bean using a String constant.
   *
   */

  public void positiveSetDoublePrimTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetDoublePrim");
    invoke();
  }

  /*
   * @testName: positiveSetFloatObjTest
   * 
   * @assertion_ids: JSP:SPEC:92;JSP:SPEC:162.10
   * 
   * @test_Strategy: and set a Float property of the bean using a String
   * constant. PENDING Merge with prim test
   */

  public void positiveSetFloatObjTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetFloatObj");
    invoke();
  }

  /*
   * @testName: positiveSetFloatPrimTest
   * 
   * @assertion_ids: JSP:SPEC:92
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set a
   * float property of the bean using a String constant.
   *
   */

  public void positiveSetFloatPrimTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetFloatPrim");
    invoke();
  }

  /*
   * @testName: positiveSetIndexedPropTest
   * 
   * @assertion_ids: JSP:SPEC:162.12
   * 
   * @test_Strategy: Create a bean using useBean tag, use setProperty and set
   * properties using the following array types: <ul> <li> byte <li> char <li>
   * short <li> int <li> float <li> long <li> double <li> boolean <li> Byte <li>
   * Character <li> Short <li> Integer <li> Float <li> Long <li> Double <li>
   * Boolean <ul> Access each of the properties via scripting, iterate through
   * the array, and display the values.
   */

  public void positiveSetIndexedPropTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetIndexedProp");
    invoke();
  }

  /*
   * @testName: positiveSetIntObjTest
   * 
   * @assertion_ids: JSP:SPEC:91;JSP:SPEC:162.10
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set an
   * Integer property of the bean using a String constant. PENDING Merge with
   * prim test
   */

  public void positiveSetIntObjTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetIntObj");
    invoke();
  }

  /*
   * @testName: positiveSetIntPrimTest
   * 
   * @assertion_ids: JSP:SPEC:91
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set an
   * int property of the bean using a String constant.
   */

  public void positiveSetIntPrimTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetIntPrim");
    invoke();
  }

  /*
   * @testName: positiveSetLongObjTest
   * 
   * @assertion_ids: JSP:SPEC:93;JSP:SPEC:162.10
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set an
   * Long property of the bean using a String constant.
   */

  public void positiveSetLongObjTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetLongObj");
    invoke();
  }

  /*
   * @testName: positiveSetLongPrimTest
   * 
   * @assertion_ids: JSP:SPEC:93
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set a
   * long property of the bean using a String constant. PENDING Merge with prim
   * test
   */
  public void positiveSetLongPrimTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetLongPrim");
    invoke();
  }

  /*
   * @testName: positiveSetPropAllTest
   * 
   * @assertion_ids: JSP:SPEC:162.2.1
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance and set the
   * property attribute to '*'. The following properties should be set by the
   * tag: name, num, str.
   */

  public void positiveSetPropAllTest() throws Fault {
    String testName = "positiveSetPropAll";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(GOLDENFILE, testName + ".gf");
    TEST_PROPS.setProperty(REQUEST, "GET /jsp_coresyntx_act_setproperty_web/"
        + testName + ".jsp?name=Frodo&num=116165&str=Validated HTTP/1.0");

    invoke();
  }

  /*
   * @testName: positiveSetPropNoParamTest
   * 
   * @assertion_ids: JSP:SPEC:162.4
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance.
   * jsp:setProperty only specifies the name and property properties. The
   * container should set the value of the Bean's property to the value of the
   * request parameter that has the same name as specified by the property
   * attribute.
   */

  public void positiveSetPropNoParamTest() throws Fault {
    String testName = "positiveSetPropNoParam";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(GOLDENFILE, testName + ".gf");
    TEST_PROPS.setProperty(REQUEST, "GET /jsp_coresyntx_act_setproperty_web/"
        + testName + ".jsp?str=SAPPOTA HTTP/1.0");

    invoke();
  }

  /*
   * @testName: positiveSetPropParamTest
   * 
   * @assertion_ids: JSP:SPEC:162.3
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance.
   * jsp:setProperty only specifies the param property. The container should set
   * the value of the Bean's property to the value of the request parameter that
   * has the same name as specified by the param attribute.
   */

  public void positiveSetPropParamTest() throws Fault {
    String testName = "positiveSetPropParam";
    TEST_PROPS.setProperty(TEST_NAME, testName);
    TEST_PROPS.setProperty(GOLDENFILE, testName + ".gf");
    TEST_PROPS.setProperty(REQUEST, "GET /jsp_coresyntx_act_setproperty_web/"
        + testName + ".jsp?Name=MANGO HTTP/1.0");

    invoke();
  }

  /*
   * @testName: positiveSetPropReqTimeSingleQuotesTest
   * 
   * @assertion_ids: JSP:SPEC:162.7.1
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance. Set the
   * value of a bean property using a request-time attribute expression
   * delimited by single quotes. PENDING Merge with
   * positiveSetPropReqTimeDoubleQuotesTest
   */

  public void positiveSetPropReqTimeSingleQuotesTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetPropReqTimeSingleQuotes");
    invoke();
  }

  /*
   * @testName: positiveSetPropReqTimeDoubleQuotesTest
   * 
   * @assertion_ids: JSP:SPEC:162.7.1
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance. Set the
   * value of a bean property using a request-time attribute expression
   * delimited by double quotes.
   *
   */

  public void positiveSetPropReqTimeDoubleQuotesTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetPropReqTimeDoubleQuotes");
    invoke();
  }

  /*
   * @testName: positiveSetPropValueTest
   * 
   * @assertion_ids: JSP:SPEC:162.1; JSP:SPEC:162.2; JSP:SPEC:162.7
   * 
   * @test_Strategy: Using jsp:useBean, create a new bean instance. Set the
   * value of a bean property using the value attribute.
   */

  public void positiveSetPropValueTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveSetPropValue");
    invoke();
  }

  /*
   * @testName: positiveBeanPropertyEditorTest
   * 
   * @assertion_ids: JSP:SPEC:86
   * 
   * @test_Strategy: Create a bean using useBean tag, use setProperty and and
   * verfiy results using getProperty.
   */

  public void positiveBeanPropertyEditorTest() throws Fault {
    TEST_PROPS.setProperty(STANDARD, "positiveBeanPropertyEditor");
    invoke();
  }

}
