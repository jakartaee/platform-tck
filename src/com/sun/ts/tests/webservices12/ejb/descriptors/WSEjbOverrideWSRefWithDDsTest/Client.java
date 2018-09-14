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

package com.sun.ts.tests.webservices12.ejb.descriptors.WSEjbOverrideWSRefWithDDsTest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.rmi.Remote;
import javax.xml.ws.WebServiceRef;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.util.Properties;

import javax.jws.*;

public class Client extends EETest {

  private Tx port;

  @WebServiceRef(name = "service/wsejboverridewsrefwithddstest", type = java.lang.Object.class, value = javax.xml.ws.Service.class)
  static TxService service;

  private void getStub() throws Exception {
    TestUtil.logMsg(
        "Get wsejboverridewsrefwithddstest Service via DDs (ignore @WebServiceRef annotation)");
    TestUtil.logMsg("service=" + service);
    TestUtil.logMsg("Get port from Service");
    port = (Tx) service.getPort(Tx.class);
    TestUtil.logMsg("Port obtained");
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxws-url-props.dat
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
   * @assertion_ids: WS4EE:SPEC:39; WS4EE:SPEC:69; WS4EE:SPEC:109;
   * WS4EE:SPEC:112; WS4EE:SPEC:43; WS4EE:SPEC:115; WS4EE:SPEC:213;
   * WS4EE:SPEC:219; WS4EE:SPEC:183; WS4EE:SPEC:184; WS4EE:SPEC:185;
   * WS4EE:SPEC:186; WS4EE:SPEC:187; WS4EE:SPEC:113; WS4EE:SPEC:114;
   * WS4EE:SPEC:117; WS4EE:SPEC:221; WS4EE:SPEC:224; WS4EE:SPEC:228;
   * WS4EE:SPEC:248; WS4EE:SPEC:249; WS4EE:SPEC:4000; WS4EE:SPEC:4001;
   * WS4EE:SPEC:5000;
   *
   * @test_Strategy: Deploy via full 109 deployment descriptors.
   * The @WebserviceRef annotation on the client must be overridden by the
   * deployment descriptors.
   */
  public void call_txRequired() throws Fault {
    TestUtil.logMsg("call_txRequired");
    try {
      String response = port.txRequired("Hello there!");
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
   * @assertion_ids: WS4EE:SPEC:39; WS4EE:SPEC:69; WS4EE:SPEC:109;
   * WS4EE:SPEC:112; WS4EE:SPEC:43; WS4EE:SPEC:115; WS4EE:SPEC:213;
   * WS4EE:SPEC:219; WS4EE:SPEC:183; WS4EE:SPEC:184; WS4EE:SPEC:185;
   * WS4EE:SPEC:186; WS4EE:SPEC:187; WS4EE:SPEC:113; WS4EE:SPEC:114;
   * WS4EE:SPEC:117; WS4EE:SPEC:221; WS4EE:SPEC:224; WS4EE:SPEC:228;
   * WS4EE:SPEC:248; WS4EE:SPEC:249; WS4EE:SPEC:4000; WS4EE:SPEC:4001;
   * WS4EE:SPEC:5000;
   *
   * @test_Strategy: Deploy via full 109 deployment descriptors.
   * The @WebserviceRef annotation on the client must be overridden by the
   * deployment descriptors.
   */
  public void call_txSupports() throws Fault {
    TestUtil.logMsg("call_txSupports");
    try {
      String response = port.txSupports("Hello there!");
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
   * @assertion_ids: WS4EE:SPEC:39; WS4EE:SPEC:69; WS4EE:SPEC:109;
   * WS4EE:SPEC:112; WS4EE:SPEC:43; WS4EE:SPEC:115; WS4EE:SPEC:213;
   * WS4EE:SPEC:219; WS4EE:SPEC:183; WS4EE:SPEC:184; WS4EE:SPEC:185;
   * WS4EE:SPEC:186; WS4EE:SPEC:187; WS4EE:SPEC:113; WS4EE:SPEC:114;
   * WS4EE:SPEC:117; WS4EE:SPEC:221; WS4EE:SPEC:224; WS4EE:SPEC:228;
   * WS4EE:SPEC:248; WS4EE:SPEC:249; WS4EE:SPEC:4000; WS4EE:SPEC:4001;
   * WS4EE:SPEC:5000;
   *
   * @test_Strategy: Deploy via full 109 deployment descriptors.
   * The @WebserviceRef annotation on the client must be overridden by the
   * deployment descriptors.
   */
  public void call_txRequiresNew() throws Fault {
    TestUtil.logMsg("call_txRequiresNew");
    try {
      String response = port.txRequiresNew("Hello there!");
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
   * @assertion_ids: WS4EE:SPEC:39; WS4EE:SPEC:69; WS4EE:SPEC:109;
   * WS4EE:SPEC:112; WS4EE:SPEC:43; WS4EE:SPEC:115; WS4EE:SPEC:213;
   * WS4EE:SPEC:219; WS4EE:SPEC:183; WS4EE:SPEC:184; WS4EE:SPEC:185;
   * WS4EE:SPEC:186; WS4EE:SPEC:187; WS4EE:SPEC:113; WS4EE:SPEC:114;
   * WS4EE:SPEC:117; WS4EE:SPEC:221; WS4EE:SPEC:224; WS4EE:SPEC:228;
   * WS4EE:SPEC:248; WS4EE:SPEC:249; WS4EE:SPEC:4000; WS4EE:SPEC:4001;
   * WS4EE:SPEC:5000;
   *
   * @test_Strategy: Deploy via full 109 deployment descriptors.
   * The @WebserviceRef annotation on the client must be overridden by the
   * deployment descriptors.
   */
  public void call_txNotSupported() throws Fault {
    TestUtil.logMsg("call_txNotSupported");
    try {
      String response = port.txNotSupported("Hello there!");
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
   * @assertion_ids: WS4EE:SPEC:39; WS4EE:SPEC:69; WS4EE:SPEC:109;
   * WS4EE:SPEC:112; WS4EE:SPEC:43; WS4EE:SPEC:115; WS4EE:SPEC:213;
   * WS4EE:SPEC:219; WS4EE:SPEC:183; WS4EE:SPEC:184; WS4EE:SPEC:185;
   * WS4EE:SPEC:186; WS4EE:SPEC:187; WS4EE:SPEC:113; WS4EE:SPEC:114;
   * WS4EE:SPEC:117; WS4EE:SPEC:221; WS4EE:SPEC:224; WS4EE:SPEC:228;
   * WS4EE:SPEC:248; WS4EE:SPEC:249; WS4EE:SPEC:4000; WS4EE:SPEC:4001;
   * WS4EE:SPEC:5000;
   *
   * @test_Strategy: Deploy via full 109 deployment descriptors.
   * The @WebserviceRef annotation on the client must be overridden by the
   * deployment descriptors.
   */
  public void call_txNever() throws Fault {
    TestUtil.logMsg("call_txNever");
    try {
      String response = port.txNever("Hello there!");
      TestUtil.logMsg("call_txNever passed");
    } catch (Throwable t) {
      TestUtil
          .logMsg("test call_txNever failed: got exception " + t.toString());
      throw new Fault("call_txNever failed");
    }
    return;
  }
}
