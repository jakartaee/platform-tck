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

package com.sun.ts.tests.webservices.ejb.txattributes;

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

  private Tx port;

  private void getStub() throws Exception {
    TestUtil.logMsg("JNDI lookup for txattributes Service");
    InitialContext ctx = new InitialContext();
    Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/txattributes");
    TestUtil.logMsg("Service found");
    TestUtil.logMsg("Get port from Service");
    port = (Tx) svc.getPort(Tx.class);
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
   * @testName: call_txRequired
   *
   * @assertion_ids: WS4EE:SPEC:69; WS4EE:SPEC:39; WS4EE:SPEC:109;
   * WS4EE:SPEC:184; WS4EE:SPEC:43;
   *
   * @test_Strategy: Call txRequired, should succeed
   */
  public void call_txRequired() throws Fault {
    TestUtil.logMsg("call_txRequired");
    try {
      port.txRequired();
      TestUtil.logMsg("call_txRequired passed");
    } catch (Throwable t) {
      TestUtil
          .logMsg("test call_txRequired failed: got exception " + t.toString());
      throw new Fault("call_txRequired failed");
    }
    return;
  }

  /*
   * @testName: call_txSupports
   *
   * @assertion_ids: WS4EE:SPEC:69; WS4EE:SPEC:39; WS4EE:SPEC:109;
   * WS4EE:SPEC:184; WS4EE:SPEC:43;
   *
   * @test_Strategy: Call txSupports, should succeed
   */
  public void call_txSupports() throws Fault {
    TestUtil.logMsg("call_txSupports");
    try {
      port.txSupports();
      TestUtil.logMsg("call_txSupports passed");
    } catch (Throwable t) {
      TestUtil
          .logMsg("test call_txSupports failed: got exception " + t.toString());
      throw new Fault("call_txSupports failed");
    }
    return;
  }

  /*
   * @testName: call_txRequiresNew
   *
   * @assertion_ids: WS4EE:SPEC:69; WS4EE:SPEC:39; WS4EE:SPEC:109;
   * WS4EE:SPEC:184; WS4EE:SPEC:43;
   *
   * @test_Strategy: Call txRequiresNew, should succeed
   */
  public void call_txRequiresNew() throws Fault {
    TestUtil.logMsg("call_txRequiresNew");
    try {
      port.txRequiresNew();
      TestUtil.logMsg("call_txRequiresNew passed");
    } catch (Throwable t) {
      TestUtil.logMsg(
          "test call_txRequiresNew failed: got exception " + t.toString());
      throw new Fault("call_txRequiresNew failed");
    }
    return;
  }

  /*
   * @testName: call_txNotSupported
   *
   * @assertion_ids: WS4EE:SPEC:69; WS4EE:SPEC:39; WS4EE:SPEC:109;
   * WS4EE:SPEC:184; WS4EE:SPEC:43;
   *
   * @test_Strategy: Call txNotSupported, should succeed
   */
  public void call_txNotSupported() throws Fault {
    TestUtil.logMsg("call_txNotSupported");
    try {
      port.txNotSupported();
      TestUtil.logMsg("call_txNotSupported passed");
    } catch (Throwable t) {
      TestUtil.logMsg(
          "test call_txNotSupported failed: got exception " + t.toString());
      throw new Fault("call_txNotSupported failed");
    }
    return;
  }

  /*
   * @testName: call_txNever
   *
   * @assertion_ids: WS4EE:SPEC:69; WS4EE:SPEC:39; WS4EE:SPEC:109;
   * WS4EE:SPEC:184; WS4EE:SPEC:43;
   *
   * @test_Strategy: Call txNever, should succeed
   */
  public void call_txNever() throws Fault {
    TestUtil.logMsg("call_txNever");
    try {
      port.txNever();
      TestUtil.logMsg("call_txNever passed");
    } catch (Throwable t) {
      TestUtil
          .logMsg("test call_txNever failed: got exception " + t.toString());
      throw new Fault("call_txNever failed");
    }
    return;
  }
}
