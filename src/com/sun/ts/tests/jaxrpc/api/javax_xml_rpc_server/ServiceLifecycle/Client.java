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

package com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_server.ServiceLifecycle;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxrpc.common.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import java.math.BigInteger;
import java.math.BigDecimal;

import javax.xml.rpc.*;
import javax.xml.namespace.QName;
import javax.xml.rpc.encoding.*;
import javax.xml.rpc.handler.*;

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

  String modeProperty = null; // platform.mode -> (standalone|javaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_server.ServiceLifecycle.";

  private final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private QName SERVICE_QNAME;

  private QName PORT_QNAME;

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "helloservice.endpoint.1";

  private static final String WSDLLOC_URL = "helloservice.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

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

  private void getStubJaxrpc() throws Exception {
    TestUtil.logMsg("Get stub from service implementation class"
        + " using JAXRPC porting instance");
    port = (Hello) JAXRPC_Util
        .getStub("com.sun.ts.tests.jaxrpc.api.javax_xml_rpc_server."
            + "ServiceLifecycle.HelloService", "getHelloPort");
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub = (javax.xml.rpc.Stub) port;
  }

  private void getStub() throws Exception {
    /* Lookup service then obtain port */
    InitialContext ctx = new InitialContext();
    TestUtil.logMsg("Obtained InitialContext");
    TestUtil.logMsg("Lookup java:comp/env/service/servicelifecycle");
    javax.xml.rpc.Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/servicelifecycle");
    TestUtil.logMsg("Obtained service");
    port = (Hello) svc.getPort(Hello.class);
    TestUtil.logMsg("Obtained port");
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

    // Initialize QName's used in the test
    SERVICE_QNAME = new QName(NAMESPACEURI, "HelloService");
    PORT_QNAME = new QName(NAMESPACEURI, "HelloPort");

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
        getStubJaxrpc();
        TestUtil.logMsg("Setting target endpoint to " + url + " ...");
        stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url);
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
   * @testName: initTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:239; WS4EE:SPEC:60; WS4EE:SPEC:150;
   * WS4EE:SPEC:151; WS4EE:SPEC:152;
   *
   * @test_Strategy: Deploy and start a service endpoint class and make sure
   * ServiceLifecycle.init() gets called.
   *
   * Description After a service endpoint object (an instance of a service
   * endpoint class) is instantiated, the JAX-RPC runtime system invokes the
   * init method. The service endpoint class uses the init method to initialize
   * its configuration and setup access to any external resources.
   */
  public void initTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("initTest: test if ServiceLifecycle.init() called");
      TestUtil.logMsg(
          "Invoking RPC method wasInitCalled() and expect " + "true ...");
      boolean yes = port.wasInitCalled();
      if (!yes) {
        TestUtil.logErr("ServiceLifecycle.init() was not called");
        pass = false;
      } else {
        TestUtil.logMsg("ServiceLifecycle.init() was called");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
      throw new Fault("initTest failed", e);
    }

    if (!pass)
      throw new Fault("initTest failed");
  }

  /*
   * @testName: destroyTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:241; WS4EE:SPEC:60; WS4EE:SPEC:150;
   * WS4EE:SPEC:154; WS4EE:SPEC:151; WS4EE:SPEC:152;
   *
   * @test_Strategy: Deploy and end a service endpoint class and make sure
   * ServiceLifecycle.destroy() gets called.
   *
   * Description JAX-RPC runtime system ends the lifecycle of a service endpoint
   * object by invoking the destroy method.
   */
  public void destroyTest() throws Fault {
    boolean pass = true;
    try {
      TestUtil.logMsg("destroyTest: test if ServiceLifecycle.destroy() called");
      TestUtil.logMsg(
          "Invoking RPC method wasDestroyCalled() and " + "expect true ...");
      boolean yes = port.wasDestroyCalled();
      if (!yes) {
        TestUtil.logErr("ServiceLifecycle.destroy() was not called");
        pass = false;
      } else {
        TestUtil.logMsg("ServiceLifecycle.destroy() was called");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: ", e);
      throw new Fault("destroyTest failed", e);
    }

    if (!pass)
      throw new Fault("destroyTest failed");
  }
}
