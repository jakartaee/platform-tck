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

package com.sun.ts.tests.webservices.deploy.portcomplink.war;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxrpc.common.*;
import com.sun.javatest.Status;

import javax.xml.rpc.Service;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.util.Properties;

public class Client extends EETest {
  InterModuleSei port = null;

  private void getStub() throws Exception {
    TestUtil.logMsg("JNDI lookup for Service1");
    InitialContext ctx = new InitialContext();
    Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/portcomplink/inter");
    TestUtil.logMsg("Get port from Service1");
    port = (InterModuleSei) svc.getPort(InterModuleSei.class);
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
   * @testName: PortCompLinkWarTest
   *
   * @assertion_ids: WS4EE:SPEC:175; WS4EE:SPEC:71; WS4EE:SPEC:72;
   * WS4EE:SPEC:73; WS4EE:SPEC:74; WS4EE:SPEC:76; WS4EE:SPEC:77; WS4EE:SPEC:78;
   * WS4EE:SPEC:79; WS4EE:SPEC:111; WS4EE:SPEC:221;
   *
   * 
   * @test_Strategy: Call InterModuleSei implementatin, which will in turn call
   * IntraModuleSei implementation. Deployment does not require any binding
   * information.
   */
  public void PortCompLinkWarTest() throws Fault {
    TestUtil.logMsg("PortCompLinkWarTest");
    try {
      String ret = port.interModCall("PortCompLinkWar");
      if (!ret.equals("inter intra PortCompLinkWar")) {
        TestUtil.logMsg(
            "test PortCompLinkWar failed: return value from server is: " + ret);
        throw new Fault("PortCompLinkTest failed");
      }
    } catch (Throwable t) {
      TestUtil
          .logMsg("test PortCompLinkWar failed: got exception " + t.toString());
      throw new Fault("PortCompLinkTest failed");
    }
    return;
  }
}
