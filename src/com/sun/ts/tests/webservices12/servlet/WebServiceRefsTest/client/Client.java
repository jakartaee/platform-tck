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

package com.sun.ts.tests.webservices12.servlet.WebServiceRefsTest.client;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;

import javax.xml.ws.*;
import javax.xml.namespace.QName;

import java.util.*;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.common.*;

import javax.naming.InitialContext;

public class Client extends ServiceEETest {
  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  String modeProperty = null; // platform.mode -> (standalone|javaEE)

  WSHello1 port1 = null;

  WSHello2 port2 = null;

  private void getPort() throws Exception {
    TestUtil.logMsg("Get JNDI InitialContext");
    javax.naming.InitialContext ic = new javax.naming.InitialContext();
    TestUtil.logMsg(
        "Do JNDI lookup of service1 java:comp/env/service/wshello1service");
    WSHello1Service service1 = (WSHello1Service) ic
        .lookup("java:comp/env/service/wshello1service");
    TestUtil.logMsg("service1=" + service1);
    TestUtil.logMsg(
        "Do JNDI lookup of service2 java:comp/env/service/wshello2service");
    WSHello2Service service2 = (WSHello2Service) ic
        .lookup("java:comp/env/service/wshello2service");
    TestUtil.logMsg("service2=" + service2);
    TestUtil.logMsg("Services obtained");
    TestUtil.logMsg("Get port from service1");
    port1 = (WSHello1) service1.getWSHello1Port();
    TestUtil.logMsg("port1=" + port1);
    TestUtil.logMsg("Get port from service2");
    port2 = (WSHello2) service2.getWSHello2Port();
    TestUtil.logMsg("port2=" + port2);
    TestUtil.logMsg("Ports obtained");
    JAXWS_Util.dumpTargetEndpointAddress(port1);
    JAXWS_Util.dumpTargetEndpointAddress(port2);
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
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   */
  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;

    try {
      hostname = p.getProperty(WEBSERVERHOSTPROP);
      if (hostname == null)
        pass = false;
      else if (hostname.equals(""))
        pass = false;
      try {
        portnum = Integer.parseInt(p.getProperty(WEBSERVERPORTPROP));
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        pass = false;
      }
      modeProperty = p.getProperty(MODEPROP);
      getPort();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("setup failed:", e);
    }

    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed:");
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: DoInvokeWebService1Test
   *
   * @assertion_ids: WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4003;
   * WS4EE:SPEC:4004; WS4EE:SPEC:4010; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
   *
   * @test_Strategy: Invoke WebService1 endpoint.
   */
  public void DoInvokeWebService1Test() throws Fault {
    TestUtil.logMsg("DoInvokeWebService1Test");
    boolean pass = true;

    try {
      String ret = port1.sayServletHello("Hello Hello!!!");
      if (!ret.equals("WSHello1Servlet: Hello Hello!!!"))
        throw new Fault("DoInvokeWebService1Test failed");
    } catch (Exception e) {
      throw new Fault("DoInvokeWebService1Test failed", e);
    }
  }

  /*
   * @testName: DoInvokeWebService2Test
   *
   * @assertion_ids: WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4003;
   * WS4EE:SPEC:4004; WS4EE:SPEC:4010; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
   *
   * @test_Strategy: Invoke WebService2 endpoint.
   */
  public void DoInvokeWebService2Test() throws Fault {
    TestUtil.logMsg("DoInvokeWebService2Test");
    boolean pass = true;

    try {
      String ret = port2.sayServletHello("Hello Hello!!!");
      if (!ret.equals("WSHello2Servlet: Hello Hello!!!"))
        throw new Fault("DoInvokeWebService2Test failed");
    } catch (Exception e) {
      throw new Fault("DoInvokeWebService2Test failed", e);
    }
  }

  /*
   * @testName: VerifyTargetEndpointAddressForBothWebServices
   *
   * @assertion_ids: WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:4003;
   * WS4EE:SPEC:4004; WS4EE:SPEC:4010; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
   *
   * @test_Strategy: Verify target endpoint address for WebService1 and
   * WebService2.
   *
   */
  public void VerifyTargetEndpointAddressForBothWebServices() throws Fault {
    TestUtil.logMsg("VerifyTargetEndpointAddressForBothWebServices");
    try {
      String endpointaddr1 = JAXWS_Util.getTargetEndpointAddress(port1);
      String endpointaddr2 = JAXWS_Util.getTargetEndpointAddress(port2);
      TestUtil.logMsg(
          "Verify that target endpoint address for WebService1 ends with [jaxws/WSHello1]");
      TestUtil.logMsg(
          "Verify that target endpoint address for WebService2 ends with [jaxws/WSHello2]");
      if (endpointaddr1.endsWith("jaxws/WSHello1")
          && endpointaddr2.endsWith("jaxws/WSHello2"))
        TestUtil.logMsg("VerifyTargetEndpointAddressForBothWebServices passed");
      else {
        TestUtil.logErr(
            "Target Endpoint Address for WebService1: " + endpointaddr1);
        TestUtil.logErr(
            "Target Endpoint Address for WebService2: " + endpointaddr2);
        throw new Fault("VerifyTargetEndpointAddressForBothWebServices failed");
      }
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault("VerifyTargetEndpointAddressForBothWebServices failed");
    }
    return;
  }
}
