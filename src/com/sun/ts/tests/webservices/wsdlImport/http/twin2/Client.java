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

package com.sun.ts.tests.webservices.wsdlImport.http.twin2;

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
    TestUtil.logMsg("JNDI lookup for twinimporthello Service");
    svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/twinimporthello");
    TestUtil.logMsg("Service found");
    TestUtil.logMsg("Get port from Service");
    port1 = (Hello) svc.getPort(Hello.class);
    TestUtil.logMsg("Port obtained");

    TestUtil.logMsg("JNDI lookup for twinimportbye Service");
    svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/twinimportbye");
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
   * @testName: invokeTwin2Svcs
   *
   * @assertion_ids: WS4EE:SPEC:214;WS4EE:SPEC:66
   *
   * @test_Strategy:
   */
  public void invokeTwin2Svcs() throws Fault {
    TestUtil.logMsg("invokeTwin2Svcs");
    try {
      port1.hello();
      port2.bye();
      TestUtil.logMsg("invokeTwin2Svcs passed");
    } catch (Throwable t) {
      TestUtil
          .logMsg("test invokeTwin2Svcs failed: got exception " + t.toString());
      throw new Fault("invokeTwin2Svcs failed");
    }
    return;
  }
}
