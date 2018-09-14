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

package com.sun.ts.tests.jaxrpc.ee.w2j.document.literal.stockquote;

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

  String modeProperty = null; // platform.mode -> (standalone|javaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.ee.w2j.document.literal.stockquote.";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private final String NAMESPACEURI = "http://example.com/stockquote/service";

  private QName SERVICE_QNAME;

  private QName PORT_QNAME;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "stockquote.endpoint.1";

  private static final String WSDLLOC_URL = "stockquote.wsdlloc.1";

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
  StockQuotePortType port = null;

  Stub stub = null;

  private void getStubStandalone() throws Exception {
    TestUtil.logMsg("Get stub from service implementation class"
        + " using JAXRPC porting instance");
    port = (StockQuotePortType) JAXRPC_Util
        .getStub("com.sun.ts.tests.jaxrpc.ee.w2j.document.literal."
            + "stockquote.StockQuoteService", "getStockQuotePort");
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub = (javax.xml.rpc.Stub) port;
  }

  private void getStub() throws Exception {
    TestUtil.logMsg("JNDI lookup for Service1");
    InitialContext ctx = new InitialContext();
    TestUtil.logMsg("java:comp/env/service/w2jdlstockquote");
    Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/w2jdlstockquote");
    TestUtil.logMsg("Get port from Service1");
    port = (StockQuotePortType) svc.getPort(
        com.sun.ts.tests.jaxrpc.ee.w2j.document.literal.stockquote.StockQuotePortType.class);
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
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   */

  public void setup(String[] args, Properties p) throws Fault {
    props = p;
    boolean pass = true;

    // Initialize QName's used in the test
    SERVICE_QNAME = new QName(NAMESPACEURI, "StockQuoteService");
    PORT_QNAME = new QName(NAMESPACEURI, "StockQuotePort");
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
   * @testName: invokeStockQuoteService
   *
   * @assertion_ids: JAXRPC:SPEC:13; JAXRPC:SPEC:261; JAXRPC:SPEC:280;
   * JAXRPC:SPEC:442; JAXRPC:SPEC:300; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:187; WS4EE:SPEC:204; WS4EE:SPEC:170; WS4EE:SPEC:202;
   * WS4EE:SPEC:203;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke an RPC method. Verify
   * RPC method is invoked.
   *
   * Description A client can invoke an RPC method via generated stub.
   */
  public void invokeStockQuoteService() throws Fault {
    TestUtil.logTrace("invokeStockQuoteService");
    boolean pass = true;
    try {
      TestUtil.logMsg("Invoke getLastTradePrice(\"GTE\") and expect 24.25");
      float response = port.getLastTradePrice("GTE");
      if (response != 24.25f) {
        TestUtil.logErr(
            "RPC failed - expected: \"24.25" + "\", received: " + response);
        pass = false;
      } else {
        TestUtil.logMsg("RPC passed - received expected response: " + response);
      }
      TestUtil.logMsg("Invoke getLastTradePrice(\"GE\") and expect 45.5");
      response = port.getLastTradePrice("GE");
      if (response != 45.5f) {
        TestUtil.logErr(
            "RPC failed - expected: \"45.5" + "\", received: " + response);
        pass = false;
      } else {
        TestUtil.logMsg("RPC passed - received expected response: " + response);
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("invokeStockQuoteService");
    }

    if (!pass)
      throw new Fault("invokeStockQuoteService failed");
  }
}
