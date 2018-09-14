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

package com.sun.ts.tests.webservices12.deploy.warMirrorSEI;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;

import java.util.Iterator;
import javax.xml.ws.Service;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.util.Properties;

public class Client extends EETest {
  @javax.xml.ws.WebServiceRef(name = "service/WSwarMirrorSEI")
  static HelloWsService svc;

  HelloWs port = null;

  private void getStub() throws Exception {
    TestUtil.logMsg("WebServiceRef for service/WSwarMirrorSEI");
    TestUtil.logMsg("svc=" + svc);
    TestUtil.logMsg("Get port from Service");
    port = (HelloWs) svc.getPort(HelloWs.class);
    TestUtil.logMsg("Port obtained");
    TestUtil.logMsg("port=" + port);
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
   * @testName: warMirrorSEICall
   *
   * @assertion_ids: WS4EE:SPEC:110; WS4EE:SPEC:184; WS4EE:SPEC:55;
   *
   * 
   * @test_Strategy: call method on Java War that mirrors the methods of SEI,
   * but does not implement SEI directly
   */
  public void warMirrorSEICall() throws Fault {
    TestUtil.logMsg("WarMirrorSEICall");
    try {
      HelloRequest req = new HelloRequest();
      req.setArgument("warMirrorSEI");
      TestUtil.logMsg("Invoke sayHello method ...");
      HelloResponse ret = port.sayHello(req);
      if (!ret.getArgument().equals("'warMirrorSEI' to you too!")) {
        TestUtil.logMsg(
            "test WarMirrorSEICall failed: return value from server is: "
                + ret.getArgument());
        throw new Fault("WarMirrorSEICall failed");
      } else
        TestUtil.logMsg("WarMirrorSEICall passed");
    } catch (Throwable t) {
      TestUtil.logMsg(
          "test WarMirrorSEICall failed: got exception " + t.toString());
      throw new Fault("WarMirrorSEICall failed");
    }
    return;
  }

}
