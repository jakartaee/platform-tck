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

package com.sun.ts.tests.webservices.deploy.CompScopedRefs;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxrpc.common.*;
import com.sun.javatest.Status;

import java.rmi.RemoteException;
import java.util.Iterator;
import java.rmi.Remote;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.util.Properties;

public class Client extends EETest {

  private HelloClientRemote helloClient;

  private ByeClientRemote byeClient;

  private void getStub() throws Exception {
    InitialContext ctx = new InitialContext();
    Object obj;

    obj = ctx.lookup("java:comp/env/ejb/HelloClientEjb");
    HelloClientHome hhome = (HelloClientHome) javax.rmi.PortableRemoteObject
        .narrow(obj, HelloClientHome.class);
    TestUtil.logMsg("*** HelloClientHome found and cast");
    helloClient = hhome.create();

    obj = ctx.lookup("java:comp/env/ejb/ByeClientEjb");
    ByeClientHome bhome = (ByeClientHome) javax.rmi.PortableRemoteObject
        .narrow(obj, ByeClientHome.class);
    TestUtil.logMsg("*** ByeClientHome found and cast");
    byeClient = bhome.create();
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
   * @testName: testCompScopedRefs
   *
   * @assertion_ids: WS4EE:SPEC:115; JavaEE:SPEC:247;
   *
   * @test_Strategy: Call two EJBs that are in the same module, both serving as
   * client to web service implementation, both using the same service-ref-name
   * for the service-ref. This tests that component-scoped-ref with the same
   * service-ref-name can be successfully deployed and used.
   */
  public void testCompScopedRefs() throws Fault {
    TestUtil.logMsg("testCompScopedRefs");
    try {
      String str = helloClient.callHello();
      int ii = byeClient.callBye();

      if (str != null && str.equals("Hello there!") && ii == 109)
        TestUtil.logMsg("testCompScopedRefs passed");
      else
        throw new RuntimeException(
            "Wrong return value(s) " + str + " and " + ii);
    } catch (Throwable t) {
      TestUtil.logMsg(
          "test testCompScopedRefs failed: got exception " + t.toString());
      throw new Fault("testCompScopedRefs failed");
    }
    return;
  }
}
