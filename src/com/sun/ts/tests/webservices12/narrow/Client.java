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

package com.sun.ts.tests.webservices12.narrow;

import com.sun.ts.lib.harness.ServiceEETest;

import com.sun.ts.lib.util.TestUtil;

import com.sun.javatest.Status;

import javax.rmi.PortableRemoteObject;

import javax.naming.InitialContext;

import javax.xml.ws.Service;

import java.util.Properties;

public class Client extends ServiceEETest {
  // Get Port and Stub access via InitialContext
  InterfaceTest1 port1 = null;

  InterfaceTest2 port2 = null;

  private void getPort() throws Exception {
    try {
      InitialContext ic = new InitialContext();
      Service svc = (Service) ic
          .lookup("java:comp/env/service/wsinterfacetest");

      Object ret = svc.getPort(InterfaceTest1.class);
      port1 = (InterfaceTest1) PortableRemoteObject.narrow(ret,
          InterfaceTest1.class);

      ret = svc.getPort(InterfaceTest2.class);
      port2 = (InterfaceTest2) PortableRemoteObject.narrow(ret,
          InterfaceTest2.class);

    } catch (Throwable t) {
      t.printStackTrace();
      throw new Fault(t.toString());
    }
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap webservices-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    boolean pass = true;

    try {
      getPort();
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
    if (!pass) {
      throw new Fault("setup failed:");
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /**
   * @testName: narrowTest
   *
   * @assertion_ids: WS4EE:SPEC:31; WS4EE:SPEC:56
   *
   * @test_Strategy:
   *
   * @test_Strategy: Get ports using PortableRemoteObject.narrow to cast the
   *                 SEI's and call a method on each to verify it.
   *
   *                 port1 - Interface1 - is narrowed from service.getPort.
   *                 port2 - Interface2 - is narrowed from service.getPort.
   *
   */
  public void narrowTest() throws Fault {
    TestUtil.logTrace("narrowTest");
    boolean pass = true;

    TestUtil.logMsg("Invoking methods on interface 1 ...");
    if (interface1Test()) {
      TestUtil.logMsg("interface 1 test PASSED ...");
    } else {
      pass = false;
      TestUtil.logErr("interface 1 test FAILED ...");
    }

    TestUtil.logMsg("Invoking methods on interface 2 ...");
    if (interface2Test()) {
      TestUtil.logMsg("interface 2 test PASSED ...");
    } else {
      pass = false;
      TestUtil.logErr("interface 2 test FAILED ...");
    }

    if (!pass)
      throw new Fault("narrowTest failed");
  }

  private boolean interface1Test() throws Fault {
    TestUtil.logTrace("interface1Test");
    boolean pass = true;
    String exp = "interface1:hello, world";

    try {
      String rec = port1.hello1("hello, world");
      TestUtil
          .logMsg("Invoking RPC method port1.hello1(\"hello, world\")=" + rec);
      if (rec.equals(exp)) {
        TestUtil.logMsg("Result match - " + exp);
      } else {
        TestUtil.logErr(
            "Result mismatch - expected: " + exp + ", received: " + rec);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean interface2Test() throws Fault {
    TestUtil.logTrace("interface2Test");
    boolean pass = true;
    String exp1 = "interface2:hello, world";
    String exp2 = "interface2:hello, world";
    try {
      String rec = port2.hello1("hello, world");
      TestUtil
          .logMsg("Invoking RPC method port2.hello1(\"hello, world\")=" + rec);
      if (rec.equals(exp1)) {
        TestUtil.logMsg("Result match - " + exp1);
      } else {
        TestUtil.logErr(
            "Result mismatch - expected: " + exp1 + ", received: " + rec);
        pass = false;
      }
      rec = port2.hello2("hello, world");
      TestUtil
          .logMsg("Invoking RPC method port2.hello2(\"hello, world\")=" + rec);
      if (rec.equals(exp2)) {
        TestUtil.logMsg("Result match - " + exp2);
      } else {
        TestUtil.logErr(
            "Result mismatch - expected: " + exp2 + ", received: " + rec);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }
}
