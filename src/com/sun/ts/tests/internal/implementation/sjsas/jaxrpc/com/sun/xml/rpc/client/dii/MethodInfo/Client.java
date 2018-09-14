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

package com.sun.ts.tests.internal.implementation.sjsas.jaxrpc.com.sun.xml.rpc.client.dii.MethodInfo;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import java.lang.reflect.*;

import com.sun.javatest.Status;

// Import implementation specific classes to test
import com.sun.xml.rpc.client.dii.*;

public class Client extends EETest {
  private Properties props = null;

  private Method[] methods;

  private Method method;

  String name;

  int modifiers;

  Class[] parameterTypes;

  Class returnType;

  int parameterCount;

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.setup_props:
   */

  public void setup(String[] args, Properties p) throws Fault {
    Class c = Client.class;
    try {
      logMsg("Get declared methods of this class object");
      methods = c.getDeclaredMethods();
      method = methods[0];
      name = method.getName();
      logMsg("name=" + name);
      modifiers = method.getModifiers();
      logMsg("modifiers=" + modifiers);
      parameterTypes = method.getParameterTypes();
      logMsg("parameterTypes=" + parameterTypes);
      parameterCount = parameterTypes.length;
      logMsg("parameterCount=" + parameterCount);
      returnType = method.getReturnType();
      logMsg("returnType=" + returnType);
    } catch (Exception e) {
      throw new Fault("setup failed", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: MethodInfoTest
   *
   * @assertion_ids:
   *
   * @test_Strategy:
   */
  public void MethodInfoTest() throws Fault {
    TestUtil.logTrace("MethodInfoTest");
    boolean pass = true;
    MethodInfo mi;
    try {
      TestUtil.logMsg("Call MethodInfo() constructor");
      mi = new MethodInfo();
      if (mi == null) {
        TestUtil.logErr("MethodInfo() returned null");
        pass = false;
      }
      mi = new MethodInfo(method);
      if (mi == null) {
        TestUtil.logErr("MethodInfo(Method) returned null");
        pass = false;
      }
      TestUtil.logMsg("Verify MethodInfo.getName() method");
      String n = mi.getName();
      if (!n.equals(name)) {
        TestUtil.logErr("MethodInfo.getName() yields different value");
        pass = false;
      }
      TestUtil.logMsg("Verify MethodInfo.getParameterTypes() method");
      Class[] carr = mi.getParameterTypes();
      TestUtil.logMsg("Verify MethodInfo.getReturnType() method");
      Class c = mi.getReturnType();
      if (!c.getName().equals(returnType.getName())) {
        TestUtil.logErr("MethodInfo.getReturnType() yields different value");
        pass = false;
      }
      TestUtil.logMsg("Verify MethodInfo.getModifiers() method");
      int m = mi.getModifiers();
      if (m != modifiers) {
        TestUtil.logErr("MethodInfo.getModifierrs() yields different value");
        pass = false;
      }
      TestUtil.logMsg("Verify MethodInfo.getMethodObject() method");
      Method mo = mi.getMethodObject();
      if (!mo.equals(method)) {
        TestUtil.logErr("MethodInfo.getMethodObject() yields different value");
        pass = false;
      }
      TestUtil.logMsg("Verify MethodInfo.getParameterCount() method");
      int pc = mi.getParameterCount();
      if (pc != parameterCount) {
        TestUtil
            .logErr("MethodInfo.getParameterCount() yields different value");
        pass = false;
      }
    } catch (Exception e) {
      throw new Fault("MethodInfoTest failed", e);
    }

    if (!pass)
      throw new Fault("MethodInfoTest failed");
  }
}
