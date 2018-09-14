/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.webservices.handler.uniqueness;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxrpc.common.*;
import com.sun.javatest.Status;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.rmi.Remote;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.util.Properties;

public class Client extends EETest {

  private Hello port1;

  private Bye port2;

  private void getStub() throws Exception {
    InitialContext ctx = new InitialContext();
    Service svc;
    TestUtil.logMsg("JNDI lookup for uniquenesshello Service");
    svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/uniquenesshello");
    TestUtil.logMsg("Service found");
    TestUtil.logMsg("Get port from Service");
    port1 = (Hello) svc.getPort(Hello.class);
    TestUtil.logMsg("Port obtained");

    TestUtil.logMsg("JNDI lookup for uniquenessbye Service");
    svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/uniquenessbye");
    TestUtil.logMsg("Service found");
    TestUtil.logMsg("Get port from Service");
    port2 = (Bye) svc.getPort(Bye.class);
    TestUtil.logMsg("Port obtained");
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxrpc-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      getStub();
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  private void printSeperationLine() {
    TestUtil.logMsg("---------------------------");
  }

  /*
   * @testName: checkUniqueness
   *
   * @assertion_ids: WS4EE:SPEC:103; WS4EE:SPEC:104; WS4EE:SPEC:92;
   * WS4EE:SPEC:200;
   *
   * @test_Strategy: Call txRequired, should succeed
   */
  public void checkUniqueness() throws Fault {
    TestUtil.logMsg("checkUniqueness");
    try {
      port1.hello();
      port2.bye();
      TestUtil.logMsg("checkUniqueness passed");
    } catch (Throwable t) {
      TestUtil
          .logMsg("test checkUniqueness failed: got exception " + t.toString());
      throw new Fault("checkUniqueness failed");
    }
    return;
  }

  /*
   * @testName: seriousCheck
   *
   * @assertion_ids: WS4EE:SPEC:103; WS4EE:SPEC:104; WS4EE:SPEC:92;
   * WS4EE:SPEC:200;
   *
   * @test_Strategy: Call txRequired, should succeed
   */
  public void seriousCheck() throws Fault {
    TestUtil.logMsg("seriousCheck");
    try {
      port2.bye();
      port1.hello();
      port1.hello();
      port2.bye();
      TestUtil.logMsg("seriousCheck passed");
    } catch (Throwable t) {
      TestUtil
          .logMsg("test seriousCheck failed: got exception " + t.toString());
      throw new Fault("seriousCheck failed");
    }
    return;
  }

  /*
   * @testName: checkPerPCParameters
   *
   * @assertion_ids: WS4EE:SPEC:160; WS4EE:SPEC:200;
   *
   * @test_Strategy: Call txRequired, should succeed
   */
  public void checkPerPCParameters() throws Fault {
    TestUtil.logMsg("checkPerPCParameters");
    try {
      String v1 = port1.hello();
      int v2 = port2.bye();

      // This is assuming the client side MessageContext is forwarded to
      // the server side, which turns out not to be the case.
      // if (v1.equals("HelloPCHelloPC") && v2 == 22)

      // As such, this test is only about server side handlers

      if (v1.indexOf("Null MessageContext obj!") != -1)
        TestUtil.logMsg(
            "RPC call to hello endpoint had unexpected Null MessageContext obj!");
      if (v2 == -1111)
        TestUtil.logMsg(
            "RPC call to bye endpoint had unexpected Null MessageContext obj!");

      if (v1.endsWith("HelloPC") && v2 % 10 == 2) {
        TestUtil.logMsg("checkPerPCParameters passed");
        TestUtil.logMsg("with return value(s): v1 = " + v1 + ", v2 = " + v2);
      } else {
        TestUtil.logMsg("***Bad return value(s): v1 = " + v1 + ", v2 = " + v2);
        throw new RuntimeException("did not get expected init data");
      }
    } catch (Throwable t) {
      TestUtil.logMsg(
          "test checkPerPCParameters failed: got exception " + t.toString());
      throw new Fault("checkPerPCParameters failed");
    }
    return;
  }
}
