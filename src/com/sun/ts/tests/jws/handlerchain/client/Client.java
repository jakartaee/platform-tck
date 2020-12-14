/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 */

/*
 * $Id$
 */

package com.sun.ts.tests.jws.handlerchain.client;

import java.net.URL;
import java.util.Properties;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jws.common.JWS_Util;
import com.sun.ts.tests.jws.handlerchain.server.HandlerChainWebServiceInterface;

public class Client extends ServiceEETest {

  static HandlerChainWebServiceService service = null;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "jwstest.handlerchainwebservice.endpoint.1";

  private static final String HOSTNAME = "localhost";

  private static final String MODEPROP = "platform.mode";

  // service and port information
  private static final String NAMESPACEURI = "http://server.handlerchain.jws.tests.ts.sun.com/";

  private static final String PKG_NAME = "com.sun.ts.tests.jws.handlerchain.client.";

  private static final String PORT_NAME = "HandlerChainWebServicePort";

  private static final int PORTNUM = 8000;

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String SERVICE_NAME = "HandlerChainWebServiceService";

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String WSDLLOC_URL = "jwstest.handlerchainwebservice.wsdlloc.1";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  String modeProperty = null; // platform.mode -> (standalone|j2ee)

  private HandlerChainWebServiceInterface port;

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private int portnum = PORTNUM;

  private Properties props = null;

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private String url = null;

  private URL wsdlurl = null;

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (HandlerChainWebServiceInterface) service
        .getPort(HandlerChainWebServiceInterface.class);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
  }

  private void getPortStandalone() throws Exception {
    port = (HandlerChainWebServiceInterface) JWS_Util.getPort(wsdlurl,
        SERVICE_QNAME, HandlerChainWebServiceService.class, PORT_QNAME,
        HandlerChainWebServiceInterface.class);
    JWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  /*
   * @class.testArgs: -ap jws-url-props.dat @class.setup_props: webServerHost;
   * webServerPort; platform.mode;
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
        getPortStandalone();
      } else {
        getTestURLs();
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (HandlerChainWebServiceService) getSharedObject();
        getPortJavaEE();
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
  }

  /*
   * @testName: testHelloRPC_WithHandler
   * 
   * @assertion_ids: JWS:SPEC:4002
   * 
   * @test_Strategy:
   */
  public void testHelloRPC_WithHandler() throws Fault {
    TestUtil.logMsg("testHelloRPC_WithHandler");
    boolean pass = true;
    try {
      String name = "Joe";
      TestUtil.logMsg("Invoke the hello() method of HandlerChainWebService");
      TestUtil.logMsg("Pass in Joe and expect return of Sam");
      String result = port.hello(name);
      if (result.equals("Sam")) {
        TestUtil.logMsg("Returned result=" + result + " (correct)");
      } else {
        TestUtil
            .logErr("Returned result=" + result + " (incorrect) expected Sam");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHelloRPC_WithHandler failed", e);
    }

    if (!pass)
      throw new Fault("testHelloRPC_WithHandler failed");
  }

  /*
   * @testName: testHelloRPC_WithoutHandler
   * 
   * @assertion_ids: JWS:SPEC:4002
   * 
   * @test_Strategy:
   */
  public void testHelloRPC_WithoutHandler() throws Fault {
    TestUtil.logMsg("testHelloRPC_WithoutHandler");
    boolean pass = true;
    try {
      String name = "Bob";
      TestUtil.logMsg("Invoke the hello() method of HandlerChainWebService");
      TestUtil.logMsg("Pass in Bob and expect return of Bob");
      String result = port.hello(name);
      if (result.equals(name)) {
        TestUtil.logMsg("Returned result=" + result + " (correct)");
      } else {
        TestUtil
            .logErr("Returned result=" + result + " (incorrect) expected Bob");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHelloRPC_WithoutHandler failed", e);
    }

    if (!pass)
      throw new Fault("testHelloRPC_WithoutHandler failed");
  }
}
