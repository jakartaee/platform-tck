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
 *  $Id$
 */
package com.sun.ts.tests.jaxws.ee.j2w.rpc.literal.handlerchaintest2;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.*;
import java.util.Properties;
import com.sun.ts.tests.jaxws.common.*;

import javax.xml.namespace.QName;

public class Client extends ServiceEETest {

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  // ServiceName and PortName mapping configuration going java-to-wsdl
  private static final String SERVICE_NAME = "HandlerChainTestService";

  private static final String PORT_NAME = "HandlerChainTestPort";

  private static final String NAMESPACEURI = "http://handlerchaintestservice.org/wsdl";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  // URL properties used by the test
  private static final String ENDPOINT_URL = "j2wrlhandlerchaintest2.endpoint.1";

  private static final String WSDLLOC_URL = "j2wrlhandlerchaintest2.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  HandlerChainTest port = null;

  static HandlerChainTestService service = null;

  private TSURL ctsurl = new TSURL();

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getPortJavaEE() throws Exception {
    port = (HandlerChainTest) service.getHandlerChainTestPort();
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
    JAXWS_Util.dumpTargetEndpointAddress(port);
  }

  private void getPortStandalone() throws Exception {
    port = (HandlerChainTest) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        HandlerChainTestService.class, PORT_QNAME, HandlerChainTest.class);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getTargetEndpointAddress(Object port) throws Exception {
    TestUtil.logMsg("Get Target Endpoint Address for port=" + port);
    String url = JAXWS_Util.getTargetEndpointAddress(port);
    TestUtil.logMsg("Target Endpoint Address=" + url);
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
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   */

  public void setup(String[] args, Properties p) throws Fault {
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
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (HandlerChainTestService) getSharedObject();
        getTestURLs();
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

    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: HandlerChainOnSEITest
   *
   * @assertion_ids: JAXWS:SPEC:9009.1
   *
   * @test_Strategy: Use a handler that is specified on the SEI and see that it
   * transforms the body
   */
  public void HandlerChainOnSEITest() throws Fault {
    TestUtil.logMsg("HandlerChainOnSEITest");
    boolean pass = true;
    String expected1 = "OutboundServerLogicalHandler";
    String expected2 = "InboundServerLogicalHandler";
    String expected3 = "InboundServerSOAPHandler";
    String expected4 = "OutboundServerSOAPHandler";
    try {
      String result = port.helloWorld("HandlerChainOnSEITest");
      TestUtil.logMsg("Return value = " + result);
      if (result.indexOf(expected1) == -1) {
        pass = false;
        TestUtil.logErr("The value:" + expected1 + " was not found ");
        TestUtil.logErr("in the result:" + result);
      }
      if (result.indexOf(expected2) == -1) {
        pass = false;
        TestUtil.logErr("The value:" + expected2 + " was not found ");
        TestUtil.logErr("in the result:" + result);
      }
      if (result.indexOf(expected3) == -1) {
        pass = false;
        TestUtil.logErr("The value:" + expected3 + " was not found ");
        TestUtil.logErr("in the result:" + result);
      }
      if (result.indexOf(expected4) == -1) {
        pass = false;
        TestUtil.logErr("The value:" + expected4 + " was not found ");
        TestUtil.logErr("in the result:" + result);
      }
    } catch (Throwable t) {
      t.printStackTrace();
      throw new Fault(t.toString());
    }

    if (!pass)
      throw new Fault("HandlerChainOnSEITest failed");
  }

}
