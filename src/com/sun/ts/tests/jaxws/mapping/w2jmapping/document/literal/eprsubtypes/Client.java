/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: Client.java 56875 2009-02-23 21:04:59Z af70133 $
 */

package com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.eprsubtypes;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import java.util.Properties;
import java.lang.reflect.Method;

import jakarta.xml.ws.wsaddressing.W3CEndpointReference;
import jakarta.xml.ws.Holder;

public class Client extends ServiceEETest {

  private static final String EXPECTED_SEI_CLASS = "com.sun.ts.tests.jaxws.mapping.w2jmapping.document.literal.eprsubtypes.Hello";

  /*
   * Test entry point.
   * 
   */
  public static void main(String[] args) {
    Client test = new Client();
    Status status = test.run(args, System.out, System.err);
    status.exit();
  }

  /*
   * @class.setup_props: ts.home;
   */
  public void setup(String[] args, Properties properties) throws Fault {
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() {
    TestUtil.logMsg("cleanup");
  }

  /*
   * @testName: VerifyEPRSubTypesTest1
   *
   * @assertion_ids: JAXWS:SPEC:2086;
   *
   * @test_Strategy: Generate classes from a wsdl/xsd that contain
   * wsa:EndpointReference types and verify that JAXB correctly maps all
   * wsa:EndpointReference types to W3CEndpointReference. Verify that the hello
   * method on the generated sei class has the correct signatures for return
   * type and method parameters. They should all be of type
   * W3CEndpointReference. Any schema element of the type wsa:EndpointReference
   * or its subtypes MUST be mapped to
   * jakarta.xml.ws.wsaddressing.W3CEndpointReferencedefault.
   */
  public void VerifyEPRSubTypesTest1() throws Fault {
    TestUtil.logTrace("VerifyEPRSubTypesTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Load class: " + EXPECTED_SEI_CLASS);
      Class seiClass = Class.forName(EXPECTED_SEI_CLASS);
      TestUtil.logMsg("seiClass=" + seiClass);
      TestUtil.logMsg(
          "Verify that the hello method parameters map to W3CEndpointReference");
      Method m;
      try {
        Holder<W3CEndpointReference> eprHolder = new Holder<W3CEndpointReference>();
        Class eprHolderClass = eprHolder.getClass();
        m = seiClass.getDeclaredMethod("hello", W3CEndpointReference.class,
            W3CEndpointReference.class, W3CEndpointReference.class,
            eprHolderClass, eprHolderClass, eprHolderClass);
      } catch (Exception e) {
        TestUtil.logErr(
            "The hello method parameters do not map to W3CEndpointReference");
        TestUtil.logErr("Caught exception: " + e.getMessage());
        throw new Fault("VerifyEPRSubTypesTest1 failed", e);
      }
      TestUtil.logMsg("Verify that hello method return type maps to void");
      Class retType = m.getReturnType();
      TestUtil.logMsg("retType=" + retType);
      if (!retType.equals(void.class)) {
        TestUtil.logErr("The hello method return does not map to void");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("VerifyEPRSubTypesTest1 failed", e);
    }

    if (!pass)
      throw new Fault("VerifyEPRSubTypesTest1 failed");
  }

  /*
   * @testName: VerifyEPRSubTypesTest2
   *
   * @assertion_ids: JAXWS:SPEC:2086;
   *
   * @test_Strategy: Generate classes from a wsdl/xsd that contain
   * wsa:EndpointReference types and verify that JAXB correctly maps all
   * wsa:EndpointReference types to W3CEndpointReference. Verify that the hello2
   * method on the generated sei class has the correct signatures for return
   * type and method parameters. They should all be of type
   * W3CEndpointReference. Any schema element of the type wsa:EndpointReference
   * or its subtypes MUST be mapped to
   * jakarta.xml.ws.wsaddressing.W3CEndpointReferencedefault.
   */
  public void VerifyEPRSubTypesTest2() throws Fault {
    TestUtil.logTrace("VerifyEPRSubTypesTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Load class: " + EXPECTED_SEI_CLASS);
      Class seiClass = Class.forName(EXPECTED_SEI_CLASS);
      TestUtil.logMsg("seiClass=" + seiClass);
      TestUtil.logMsg(
          "Verify that the hello2 method parameters map to W3CEndpointReference");
      Method m;
      try {
        Holder<W3CEndpointReference> eprHolder = new Holder<W3CEndpointReference>();
        Class eprHolderClass = eprHolder.getClass();
        m = seiClass.getDeclaredMethod("hello2", eprHolderClass, eprHolderClass,
            eprHolderClass);
      } catch (Exception e) {
        TestUtil.logErr(
            "The hello2 method parameters do not map to W3CEndpointReference");
        TestUtil.logErr("Caught exception: " + e.getMessage());
        throw new Fault("VerifyEPRSubTypesTest2 failed", e);
      }
      TestUtil.logMsg("Verify that hello2 method return type maps to void");
      Class retType = m.getReturnType();
      TestUtil.logMsg("retType=" + retType);
      if (!retType.equals(void.class)) {
        TestUtil.logErr("The hello2 method return does not map to void");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("VerifyEPRSubTypesTest2 failed", e);
    }

    if (!pass)
      throw new Fault("VerifyEPRSubTypesTest2 failed");
  }

  /*
   * @testName: VerifyEPRSubTypesTest3
   *
   * @assertion_ids: JAXWS:SPEC:2086;
   *
   * @test_Strategy: Generate classes from a wsdl/xsd that contain
   * wsa:EndpointReference types and verify that JAXB correctly maps all
   * wsa:EndpointReference types to W3CEndpointReference. Verify that the hello3
   * method on the generated sei class has the correct signatures for return
   * type and method parameters. They should all be of type
   * W3CEndpointReference. Any schema element of the type wsa:EndpointReference
   * or its subtypes MUST be mapped to
   * jakarta.xml.ws.wsaddressing.W3CEndpointReferencedefault.
   */
  public void VerifyEPRSubTypesTest3() throws Fault {
    TestUtil.logTrace("VerifyEPRSubTypesTest3");
    boolean pass = true;
    try {
      TestUtil.logMsg("Load class: " + EXPECTED_SEI_CLASS);
      Class seiClass = Class.forName(EXPECTED_SEI_CLASS);
      TestUtil.logMsg("seiClass=" + seiClass);
      TestUtil.logMsg(
          "Verify that the hello3 method parameters map to W3CEndpointReference");
      Method m;
      try {
        m = seiClass.getDeclaredMethod("hello3", W3CEndpointReference.class);
      } catch (Exception e) {
        TestUtil.logErr(
            "The hello3 method parameters do not map to W3CEndpointReference");
        TestUtil.logErr("Caught exception: " + e.getMessage());
        throw new Fault("VerifyEPRSubTypesTest3 failed", e);
      }
      TestUtil.logMsg("Verify that hello3 method return type maps to void");
      Class retType = m.getReturnType();
      TestUtil.logMsg("retType=" + retType);
      if (!retType.equals(W3CEndpointReference.class)) {
        TestUtil.logErr(
            "The hello3 method return does not map to W3CEndpointReference");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      throw new Fault("VerifyEPRSubTypesTest3 failed", e);
    }

    if (!pass)
      throw new Fault("VerifyEPRSubTypesTest3 failed");
  }
}
