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

package com.sun.ts.tests.jaxrpc.ee.j2w.simpletest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxrpc.common.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import javax.xml.rpc.*;
import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.*;

import com.sun.javatest.Status;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.ee.j2w.simpletest.";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private static final String NAMESPACEURI = "http://simpletestservice.org/wsdl";

  // ServiceName and PortName mapping configuration going java-to-wsdl
  private static final String SERVICENAMEPROP = "j2wsimpletest.servicename.1";

  private static final String PORTNAMEPROP = "j2wsimpletest.portname.1";

  private String SERVICE_NAME;

  private String PORT_NAME;

  private QName SERVICE_QNAME;

  private QName PORT_QNAME;

  String modeProperty = null; // platform.mode -> (standalone|javaEE)

  // URL properties used by the test
  private static final String ENDPOINT_URL = "j2wsimpletest.endpoint.1";

  private static final String WSDLLOC_URL = "j2wsimpletest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  private static final String DIIENDPOINT_URL = "j2wst.endpoint.1";

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXRPC_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  // Get Port and Stub access via porting layer interface
  Hello port = null;

  Stub stub = null;

  private void getStubStandalone() throws Exception {
    port = (Hello) JAXRPC_Util.getStub(PKG_NAME + SERVICE_NAME,
        "get" + PORT_NAME);
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub = (javax.xml.rpc.Stub) port;
  }

  private void getDIITestURLs() throws Exception {
    TestUtil.logMsg("Get JAXRPC porting instance");
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXRPC_Util.getURLFromProp(DIIENDPOINT_URL);
    url = new TSURL().getURLString(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
  }

  private void getStub() throws Exception {
    InitialContext ctx = new InitialContext();
    javax.xml.rpc.Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/j2wsimpletest1");
    TestUtil.logMsg("Get stub from generic Service instance");
    port = (Hello) svc
        .getPort(com.sun.ts.tests.jaxrpc.ee.j2w.simpletest.Hello.class);
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
        SERVICE_NAME = JAXRPC_Util.getURLFromProp(SERVICENAMEPROP);
        PORT_NAME = JAXRPC_Util.getURLFromProp(PORTNAMEPROP);
        getTestURLs();
        getStubStandalone();
        TestUtil.logMsg("Setting target endpoint to " + url + " ...");
        stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url);
      } else {
        SERVICE_NAME = "SimpleTestService";
        PORT_NAME = "HelloPort";
        getStub();
        getDIITestURLs();
      }

      // Initialize QName's used in the test
      SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);
      PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);
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
   * @testName: InvokeRPCViaStubTest
   *
   * @assertion_ids: JAXRPC:SPEC:2; JAXRPC:SPEC:4; JAXRPC:SPEC:26;
   * JAXRPC:SPEC:31; JAXRPC:SPEC:8; JAXRPC:SPEC:322; JAXRPC:SPEC:16;
   * JAXRPC:SPEC:17; JAXRPC:SPEC:19; JAXRPC:SPEC:20; JAXRPC:SPEC:28;
   * JAXRPC:SPEC:208; JAXRPC:SPEC:337; JAXRPC:SPEC:340; WS4EE:SPEC:35;
   * WS4EE:SPEC:36;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke an RPC method. Verify
   * RPC method is invoked.
   *
   * Description A client can invoke an RPC method via generated stub.
   */
  public void InvokeRPCViaStubTest() throws Fault {
    TestUtil.logTrace("InvokeRPCViaStubTest");
    boolean pass = true;
    String singleLineExpected = "Hello, foo!";
    String multiLineExpected = "Hello, bar\nand\nGoodbye!";
    try {
      TestUtil.logMsg("Invoking RPC method port.hello(\"foo\") and expect "
          + "'" + singleLineExpected + "' ...");
      String response = port.hello("foo");
      if (!response.equals(singleLineExpected)) {
        TestUtil.logErr("RPC failed - expected \"" + singleLineExpected
            + "\", received: " + response);
        pass = false;
      } else {
        TestUtil.logMsg("RPC passed - received expected response: " + response);
      }

      TestUtil.logMsg(
          "Invoking RPC method port.hello(\"bar\nand\nGoodbye!\") and expect "
              + "'" + multiLineExpected + "' ...");
      response = port.hello("bar\nand\nGoodbye");
      if (!response.equals(multiLineExpected)) {
        TestUtil.logErr("RPC failed - expected \"" + multiLineExpected
            + "\", received: " + response);
        pass = false;
      } else {
        TestUtil.logMsg("RPC passed - received expected response: " + response);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("InvokeRPCViaStubTest failed", e);
    }

    if (!pass)
      throw new Fault("InvokeRPCViaStubTest failed");
  }

  /*
   * @testName: InvokeRPCViaDIITest
   *
   * @assertion_ids: JAXRPC:SPEC:2; JAXRPC:SPEC:4; JAXRPC:SPEC:26;
   * JAXRPC:SPEC:31; JAXRPC:SPEC:10; JAXRPC:SPEC:16; JAXRPC:SPEC:17;
   * JAXRPC:SPEC:19; JAXRPC:SPEC:20; JAXRPC:SPEC:28; JAXRPC:SPEC:208;
   * JAXRPC:SPEC:337; WS4EE:SPEC:35; WS4EE:SPEC:36;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and invoke an RPC method. Verify RPC method is invoked.
   *
   * Description A client can invoke an RPC method via dynamic invocation.
   */
  public void InvokeRPCViaDIITest() throws Fault {
    TestUtil.logTrace("InvokeRPCViaDIITest");
    boolean pass = true;
    String expected = "Hello, foo!";
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(SERVICE_QNAME);
      } else {
        InitialContext ctx = new InitialContext();
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/j2wsimpletest2");
      }
      Call call = service.createCall();
      call = JAXRPC_Util.setCallProperties(call, " ");
      TestUtil.logMsg("Initialize rest of Call object");
      call.setPortTypeName(PORT_QNAME);
      call.setTargetEndpointAddress(url);
      call.setOperationName(new QName(NAMESPACEURI, "hello"));
      boolean b = call
          .isParameterAndReturnSpecRequired(new QName(NAMESPACEURI, "hello"));

      if (b) {
        call.addParameter("String_1", new QName(Constants.XSD, "string"),
            ParameterMode.IN);
        call.setReturnType(new QName(Constants.XSD, "string"));
      }

      String[] params = { new String("foo") };

      TestUtil.logMsg("Invoking RPC method hello(\"foo\") and expect " + "'"
          + expected + "' ...");
      String response = (String) call.invoke(params);
      if (!response.equals(expected)) {
        TestUtil.logErr("RPC failed - expected \"" + expected + "\", received: "
            + response);
        pass = false;
      } else {
        TestUtil.logMsg("RPC passed - received expected response: " + response);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("InvokeRPCViaDIITest failed", e);
    }

    if (!pass)
      throw new Fault("InvokeRPCViaDIITest failed");
  }

  /*
   * @testName: InvokeRPCViaDIIOneWayTest
   *
   * @assertion_ids: JAXRPC:SPEC:2; JAXRPC:SPEC:4; JAXRPC:SPEC:26;
   * JAXRPC:SPEC:33; JAXRPC:SPEC:16; JAXRPC:SPEC:17; JAXRPC:SPEC:19;
   * JAXRPC:SPEC:20; JAXRPC:SPEC:28; JAXRPC:SPEC:208; WS4EE:SPEC:35;
   * WS4EE:SPEC:36;
   *
   * @test_Strategy: Create a Call object to our service definition interface
   * and invoke an RPC method. Verify RPC method is invoked.
   *
   * Description A client can invoke an RPC method via dynamic invocation.
   */
  public void InvokeRPCViaDIIOneWayTest() throws Fault {
    TestUtil.logTrace("InvokeRPCViaDIIOneWayTest");
    boolean pass = true;
    String expected = "Hello, foo!";
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(SERVICE_QNAME);
      } else {
        InitialContext ctx = new InitialContext();
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/j2wsimpletest2");
      }
      Call call = service.createCall();
      call = JAXRPC_Util.setCallProperties(call, " ");
      TestUtil.logMsg("Initialize rest of Call object");
      call.setPortTypeName(PORT_QNAME);
      call.setTargetEndpointAddress(url);
      call.setOperationName(new QName(NAMESPACEURI, "helloOneWay"));
      boolean b = call.isParameterAndReturnSpecRequired(
          new QName(NAMESPACEURI, "helloOneWay"));

      if (b) {
        call.addParameter("String_1", new QName(Constants.XSD, "string"),
            ParameterMode.IN);
      }
      String[] params = { new String("foo") };

      TestUtil.logMsg("Invoking RPC method helloOneWay(\"foo\") ...");
      call.invokeOneWay(params);
      TestUtil.logMsg("RPC passed - invokeOneWay call successful");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("InvokeRPCViaDIIOneWayTest failed", e);
    }

    if (!pass)
      throw new Fault("InvokeRPCViaDIIOneWayTest failed");
  }

  /*
   * @testName: InvokeRPCViaDynProxyTest
   *
   * @assertion_ids: JAXRPC:SPEC:2; JAXRPC:SPEC:4; JAXRPC:SPEC:26;
   * JAXRPC:SPEC:31; JAXRPC:SPEC:9; JAXRPC:SPEC:16; JAXRPC:SPEC:17;
   * JAXRPC:SPEC:19; JAXRPC:SPEC:20; JAXRPC:SPEC:28; JAXRPC:SPEC:208;
   * JAXRPC:SPEC:337; WS4EE:SPEC:35; WS4EE:SPEC:36;
   *
   * @test_Strategy: Create a proxy object to our service definition interface
   * and invoke an RPC method. Verify RPC method is invoked.
   *
   * Description A client can invoke an RPC method via dynamic proxy.
   */
  public void InvokeRPCViaDynProxyTest() throws Fault {
    TestUtil.logTrace("InvokeRPCViaDynProxyTest");
    boolean pass = true;
    String expected = "Hello, foo!";
    try {
      Service service = null;
      if (modeProperty.equals("standalone")) {
        service = JAXRPC_Util.getService(wsdlurl, SERVICE_QNAME);
      } else {
        InitialContext ctx = new InitialContext();
        service = (javax.xml.rpc.Service) ctx
            .lookup("java:comp/env/service/j2wsimpletest1");
      }
      Hello hello = (Hello) service.getPort(PORT_QNAME, Hello.class);
      if (hello != null)
        TestUtil.logMsg("Hello dynproxy = " + hello);

      TestUtil.logMsg("Invoking RPC method hello(\"foo\") and expect " + "'"
          + expected + "' ...");
      if (hello != null) {
        String response = hello.hello("foo");
        if (!response.equals(expected)) {
          TestUtil.logErr("RPC failed - expected \"" + expected
              + "\", received: " + response);
          pass = false;
        } else {
          TestUtil
              .logMsg("RPC passed - received expected response: " + response);
        }
      } else {
        TestUtil.logErr("RPC failed - proxy object returned was null");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("InvokeRPCViaDynProxyTest failed", e);
    }

    if (!pass)
      throw new Fault("InvokeRPCViaDynProxyTest failed");
  }
}
