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

package com.sun.ts.tests.webservices.wsdlImport.http.shared4;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;

import javax.xml.rpc.Service;

import java.util.Properties;
import javax.naming.InitialContext;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxrpc.common.*;

public class Client extends ServiceEETest {

  // Get Port and Stub access via porting layer interface
  SharedTest1 port1 = null;

  SharedTest2 port2 = null;

  private void getStub() throws Exception {
    TestUtil.logMsg("JNDI lookup for Service1");
    InitialContext ctx = new InitialContext();
    Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/svc1");
    TestUtil.logMsg("Get port from Service1");
    port1 = (SharedTest1) svc.getPort(
        com.sun.ts.tests.webservices.wsdlImport.http.shared4.SharedTest1.class);
    TestUtil.logMsg("Port1 obtained");

    TestUtil.logMsg("JNDI lookup for Service2");
    Service svc2 = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/svc2");
    TestUtil.logMsg("Get port from Service2");
    port2 = (SharedTest2) svc2.getPort(
        com.sun.ts.tests.webservices.wsdlImport.http.shared4.SharedTest2.class);
    TestUtil.logMsg("Port2 obtained");
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

  /*
   * @testName: SharedImportTest
   *
   * @assertion_ids: WS4EE:SPEC:65; WS4EE:SPEC:214; WS4EE:SPEC:227;
   * WS4EE:SPEC:218
   *
   * @test_Strategy: Two different .wsdl files shared the same .xsd import. Make
   * sure we can call the server side implementations.
   * 
   */
  public void SharedImportTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("SharedImportTest");
      AllStruct orig = new AllStruct();
      orig.setVarString("SharedImportTest");
      orig.setVarInt(11);
      TestUtil.logMsg("Calling first port ");
      AllStruct ret1 = port1.echoAllStruct(orig);
      TestUtil.logMsg("Calling second port ");
      AllStruct ret2 = port2.echoAllStruct(orig);

      String expectedString1 = orig.getVarString() + "_SharedTestImpl1";
      int expectedInt1 = orig.getVarInt() + 1;
      String expectedString2 = orig.getVarString() + "_SharedTestImpl2";
      int expectedInt2 = orig.getVarInt() + 2;

      if (!ret1.getVarString().equals(expectedString1)) {
        TestUtil.logErr(
            "unexpected string from returned structure while calling svc1: expecting "
                + expectedString1 + ", but got: " + ret1.getVarString());
        pass = false;
      }
      if (ret1.getVarInt() != expectedInt1) {
        TestUtil.logErr(
            "unexpected int from returned structure while calling svc1: expecting "
                + expectedInt1 + ", but got: " + ret1.getVarInt());
        pass = false;
      }
      if (!ret2.getVarString().equals(expectedString2)) {
        TestUtil.logErr(
            "unexpected string from returned structure while calling svc2: expecting "
                + expectedString2 + ", but got: " + ret2.getVarString());
        pass = false;
      }
      if (ret2.getVarInt() != expectedInt2) {
        TestUtil.logErr(
            "unexpected int from returned structure while calling svc2: expecting "
                + expectedInt2 + ", but got: " + ret2.getVarInt());
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("did not pass");
  }
}
