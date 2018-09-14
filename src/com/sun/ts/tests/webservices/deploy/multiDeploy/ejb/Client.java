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

package com.sun.ts.tests.webservices.deploy.multiDeploy.ejb;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxrpc.common.*;
import com.sun.javatest.Status;

import java.util.Iterator;
import java.rmi.Remote;
import javax.xml.rpc.Service;
import javax.xml.rpc.Call;
import javax.xml.rpc.handler.HandlerRegistry;
import javax.xml.rpc.encoding.TypeMappingRegistry;
import javax.xml.rpc.ServiceException;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.util.Properties;

public class Client extends EETest {

  private final String NAMESPACEURI = "http://MultiDeployEjb.org";

  HelloWs port1 = null;

  Service svc1 = null;

  HelloWs port2 = null;

  Service svc2 = null;

  InitialContext ctx;

  private QName port_qname1 = null;

  private QName port_qname2 = null;

  private void getStub() throws Exception {
    TestUtil.logMsg("JNDI lookup for Service1");
    ctx = new InitialContext();
    svc1 = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/multiDeployEjb/svc1");
    TestUtil.logMsg("Get port1 from Service1");
    port_qname1 = new QName(NAMESPACEURI, "HelloWsPort");
    port1 = (HelloWs) svc1.getPort(port_qname1, HelloWs.class);
    TestUtil.logMsg("Port1 obtained");

    TestUtil.logMsg("JNDI lookup for Service2");
    svc2 = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/multiDeployEjb/svc2");
    TestUtil.logMsg("Get port2 from Service2");
    port_qname2 = new QName(NAMESPACEURI, "HelloWsPort2");
    port2 = (HelloWs) svc2.getPort(port_qname2, HelloWs.class);

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

  private void printSeperationLine() {
    TestUtil.logMsg("---------------------------");
  }

  /*
   * @testName: multiDeployEjbCall
   *
   * @assertion_ids: WS4EE:SPEC:185
   * 
   * @test_Strategy: The same WSDL file is deployed twice in the same module.
   * Use two different service-refs to access the two implementations, and make
   * sure the results are what's expected
   */
  public void multiDeployEjbCall() throws Fault {
    TestUtil.logMsg("MultiDeployEjbCall");
    try {
      String ret1 = port1.sayHello("multiDeployEjb");
      String ret2 = port2.sayHello("multiDeployEjb");
      TestUtil.logMsg("sayHello from port1: " + ret1);
      TestUtil.logMsg("sayHello from port2: " + ret2);
      if (!ret1.equals("'multiDeployEjb' to you too!")) {
        TestUtil.logMsg(
            "test MultiDeployEjbCall failed: return value from first implementationis: "
                + ret1);
        throw new Fault("MultiDeployEjbCall failed");
      } else
        TestUtil.logMsg("first call returned expected result");

      if (!ret2.equals("'multiDeployEjb' to me too!")) {
        TestUtil.logMsg(
            "test MultiDeployEjbCall failed: return value from second implementation is: "
                + ret2);
        throw new Fault("MultiDeployEjbCall failed");
      } else
        TestUtil.logMsg("second call returned expected result");

      TestUtil.logMsg("MultiDeployEjbCall passed");
    } catch (Throwable t) {
      TestUtil.logMsg(
          "test MultiDeployEjbCall failed: got exception " + t.toString());
      throw new Fault("MultiDeployEjbCall failed");
    }
    return;
  }

}
