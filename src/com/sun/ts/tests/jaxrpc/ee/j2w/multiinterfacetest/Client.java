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

package com.sun.ts.tests.jaxrpc.ee.j2w.multiinterfacetest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;

import javax.xml.rpc.*;

import java.util.Properties;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxrpc.common.*;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.ee.j2w.multiinterfacetest.";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT1_URL = "multiinterfacetest.endpoint.1";

  private static final String ENDPOINT2_URL = "multiinterfacetest.endpoint.2";

  private static final String WSDLLOC_URL = "multiinterfacetest.wsdlloc.1";

  // ServiceName and PortName mapping configuration going java-to-wsdl
  private static final String SERVICE_NAME = "multiinterfacetest.servicename.1";

  private static final String PORT_NAME1 = "multiinterfacetest.portname.1";

  private static final String PORT_NAME2 = "multiinterfacetest.portname.2";

  private String serviceName = null;

  private String portName1 = null;

  private String portName2 = null;

  private String url1 = null;

  private String url2 = null;

  private URL wsdlurl = null;

  String modeProperty = null; // platform.mode -> (standalone|javaEE)

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXRPC_Util.getURLFromProp(ENDPOINT1_URL);
    url1 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(ENDPOINT2_URL);
    url2 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint1 URL: " + url1);
    TestUtil.logMsg("Service Endpoint2 URL: " + url2);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  // Get Port and Stub access via porting layer interface
  MultiInterfaceTest1 port1 = null;

  MultiInterfaceTest2 port2 = null;

  Stub stub1 = null;

  Stub stub2 = null;

  private void getStubStandalone() throws Exception {
    serviceName = PKG_NAME + JAXRPC_Util.getURLFromProp(SERVICE_NAME);
    portName1 = JAXRPC_Util.getURLFromProp(PORT_NAME1);
    portName2 = JAXRPC_Util.getURLFromProp(PORT_NAME2);
    port1 = (MultiInterfaceTest1) JAXRPC_Util.getStub(serviceName,
        "get" + portName1);
    port2 = (MultiInterfaceTest2) JAXRPC_Util.getStub(serviceName,
        "get" + portName2);
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub1 = (javax.xml.rpc.Stub) port1;
    stub2 = (javax.xml.rpc.Stub) port2;
  }

  private void getStub() throws Exception {
    try {
      InitialContext ic = new InitialContext();
      javax.xml.rpc.Service svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/multiinterfacetest");

      port1 = (MultiInterfaceTest1) svc.getPort(MultiInterfaceTest1.class);
      port2 = (MultiInterfaceTest2) svc.getPort(MultiInterfaceTest2.class);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
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
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        getStubStandalone();
        TestUtil.logMsg("Setting target endpoint to " + url1 + " ...");
        stub1._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url1);
        TestUtil.logMsg("Setting target endpoint to " + url2 + " ...");
        stub2._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url2);
      } else {
        getStub();
      }
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
   * @testName: multiinterfaceTest
   *
   * @assertion_ids: JAXRPC:SPEC:262; JAXRPC:SPEC:263; JAXRPC:SPEC:264;
   * JAXRPC:SPEC:265; JAXRPC:SPEC:266; JAXRPC:SPEC:267; WS4EE:SPEC:197;
   * WS4EE:SPEC:35; WS4EE:SPEC:36;
   *
   * @test_Strategy: Invoke a method on interface 1 of the service and verify
   * correct service method is called. Invoke a method on interface 2 of the
   * service and verify correct service method is called.
   *
   * Description A service can have multiple service definition interfaces or
   * ports defined.
   */
  public void multiinterfaceTest() throws Fault {
    TestUtil.logTrace("multiinterfaceTest");
    boolean pass = true;

    TestUtil.logMsg("Invoking method on interface 1 ...");
    if (multiinterface1Test()) {
      TestUtil.logMsg("interface 1 test PASSED ...");
    } else {
      pass = false;
      TestUtil.logErr("interface 1 test FAILED ...");
    }

    TestUtil.logMsg("Invoking method on interface 2 ...");
    if (multiinterface2Test()) {
      TestUtil.logMsg("interface 2 test PASSED ...");
    } else {
      pass = false;
      TestUtil.logErr("interface 2 test FAILED ...");
    }

    if (!pass)
      throw new Fault("multiinterfaceTest failed");
  }

  /*
   * @testName: inheritedinterfaceTest
   *
   * @assertion_ids: JAXRPC:SPEC:262; JAXRPC:SPEC:263; JAXRPC:SPEC:264;
   * JAXRPC:SPEC:265; JAXRPC:SPEC:266; JAXRPC:SPEC:267; WS4EE:SPEC:35;
   * WS4EE:SPEC:36;
   *
   * @test_Strategy: Invoke methods on interface 2 of the service and verify
   * correct service method(s) are called. Interface2 extends and inherits from
   * Interface1.
   *
   * Description JAX-RPC supports the mapping of inheritance of service
   * definition interfaces.
   */
  public boolean inheritedinterfaceTest() throws Fault {
    TestUtil.logTrace("inheritedinterfaceTest");
    boolean pass = true;
    String exp1 = "interface2:hello1:hello, world";
    String exp2 = "interface2:hello2:hello, world";
    try {
      String rec = port2.hello1("hello, world");
      TestUtil
          .logMsg("Invoking RPC method port2.hello1(\"hello, world\")=" + rec);
      if (rec.equals(exp1)) {
        TestUtil.logMsg("Result match - " + exp1);
      } else {
        TestUtil.logErr(
            "Result mismatch - expected: " + exp1 + ", received: " + rec);
        pass = false;
      }
      rec = port2.hello2("hello, world");
      TestUtil
          .logMsg("Invoking RPC method port2.hello2(\"hello, world\")=" + rec);
      if (rec.equals(exp2)) {
        TestUtil.logMsg("Result match - " + exp2);
      } else {
        TestUtil.logErr(
            "Result mismatch - expected: " + exp2 + ", received: " + rec);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("inheritedinterfaceTest failed");
    return pass;
  }

  private boolean multiinterface1Test() throws Fault {
    TestUtil.logTrace("multiinterface1Test");
    boolean pass = true;
    String exp = "interface1:hello1:hello, world";
    try {
      String rec = port1.hello1("hello, world");
      TestUtil
          .logMsg("Invoking RPC method port1.hello1(\"hello, world\")=" + rec);
      if (rec.equals(exp)) {
        TestUtil.logMsg("Result match - " + exp);
      } else {
        TestUtil.logErr(
            "Result mismatch - expected: " + exp + ", received: " + rec);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean multiinterface2Test() throws Fault {
    TestUtil.logTrace("multiinterface2Test");
    boolean pass = true;
    String exp = "interface2:hello2:hello, world";
    try {
      String rec = port2.hello2("hello, world");
      TestUtil
          .logMsg("Invoking RPC method port2.hello2(\"hello, world\")=" + rec);
      if (rec.equals(exp)) {
        TestUtil.logMsg("Result match - " + exp);
      } else {
        TestUtil.logErr(
            "Result mismatch - expected: " + exp + ", received: " + rec);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }
}
