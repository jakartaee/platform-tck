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
 * $Id$
 */

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws.LogicalMessage;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.*;

import java.rmi.*;
import jakarta.xml.ws.*;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.net.*;
import java.util.*;
import jakarta.xml.ws.*;
import jakarta.xml.ws.handler.*;
import jakarta.xml.ws.soap.*;

import jakarta.jws.HandlerChain;

public class Client extends ServiceEETest {
  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.api.jakarta_xml_ws.LogicalMessage.";

  private static final String SHARED_CLIENT_PKG = "com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.";

  private static final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloService";

  private static final String PORT_NAME = "Hello3Port";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "dlhelloservice.endpoint.3";

  private static final String WSDLLOC_URL = "dlhelloservice.wsdlloc.3";

  private String url = null;

  private URL wsdlurl = null;

  private Hello3 port = null;

  private BindingProvider bp = null;

  private Binding binding = null;

  private List<Binding> listOfBindings = new ArrayList<Binding>();

  private List<Handler> portHandlerChain = null;

  private static final Class SERVICE_CLASS = com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.HelloService.class;

  static HelloService service = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  private boolean setupPorts() {
    boolean result = true;
    TestUtil.logTrace("entering setupPorts");
    try {
      if (modeProperty.equals("standalone")) {
        getPortsStandalone();
      } else {
        getPortsJavaEE();
      }
    } catch (Exception e) {
      TestUtil.logErr("Could not setup stubs properly");
      TestUtil.printStackTrace(e);
      result = false;
    }
    TestUtil.logTrace("leaving setupPorts");
    return result;

  }

  private void getPortsStandalone() throws Exception {
    getPorts();
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortsJavaEE() throws Exception {
    TestUtil.logMsg("Obtaining service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    getPorts();
    getTargetEndpointAddress(port);
  }

  private void getTargetEndpointAddress(Object port) throws Exception {
    TestUtil.logMsg("Get Target Endpoint Address for port=" + port);
    String url = JAXWS_Util.getTargetEndpointAddress(port);
    TestUtil.logMsg("Target Endpoint Address=" + url);
  }

  private void getPorts() throws Exception {
    TestUtil.logTrace("entering getPorts");

    TestUtil.logMsg("Get port = " + PORT_NAME);
    port = (Hello3) service.getPort(Hello3.class);
    TestUtil.logMsg("port=" + port);

    TestUtil.logMsg("Get binding for port = " + PORT_NAME);
    bp = (BindingProvider) port;
    binding = bp.getBinding();
    portHandlerChain = binding.getHandlerChain();
    TestUtil.logMsg("Port HandlerChain =" + portHandlerChain);
    TestUtil.logMsg("Port HandlerChain size = " + portHandlerChain.size());

    listOfBindings.add(binding);

    TestUtil.logTrace("leaving getPorts");
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
        TestUtil.logMsg("Create Service object");
        getTestURLs();
        service = (HelloService) JAXWS_Util.getService(wsdlurl, SERVICE_QNAME,
            SERVICE_CLASS);
      } else {
        getTestURLs();
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (HelloService) getSharedObject();

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
   * @testName: SetGetPayloadSourceTest
   *
   * @assertion_ids: JAXWS:JAVADOC:30; JAXWS:JAVADOC:32; JAXWS:SPEC:6002;
   * WS4EE:SPEC:5005; JAXWS:SPEC:5000; JAXWS:SPEC:5001; JAXWS:SPEC:5002;
   * JAXWS:SPEC:5003; JAXWS:SPEC:7000; JAXWS:SPEC:7002; JAXWS:SPEC:7008;
   * JAXWS:SPEC:7009;
   *
   * @test_Strategy: Test the various getPayload and setPayload methods in the
   * handlers
   */
  public void SetGetPayloadSourceTest() throws Fault {
    TestUtil.logTrace("SetGetPayloadSourceTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    } else {
      try {
        String expected = "client:OutboundClientLogicalHandler_getsetPayloadSource:InboundServerLogicalHandler_getsetPayloadSource:Hello3Impl:OutboundServerLogicalHandler_getsetPayloadSource:InboundClientLogicalHandler_getsetPayloadSource";

        Hello3Request hello3Req = new Hello3Request();
        hello3Req.setTestname("setgetPayloadSourceTest");
        hello3Req.setArgument("client");

        Hello3Response hello3Res = port.hello(hello3Req);
        String actual = hello3Res.getArgument();
        TestUtil.logMsg("Hello3Response=" + actual);
        if (!actual.equals(expected)) {
          TestUtil.logErr("Error: Did not get expected result:");
          TestUtil.logErr("expected=" + expected);
          TestUtil.logErr("actual=" + actual);
          pass = false;
        }
      } catch (Exception e) {
        pass = false;
        e.printStackTrace();
      }
    }
    if (!pass)
      throw new Fault("SetGetPayloadSourceTest failed");
  }

  /*
   * @testName: SetGetPayloadJAXBContextTest
   *
   * @assertion_ids: JAXWS:JAVADOC:31; JAXWS:JAVADOC:33; JAXWS:SPEC:6002;
   * WS4EE:SPEC:5005; JAXWS:SPEC:5000; JAXWS:SPEC:5001; JAXWS:SPEC:5002;
   * JAXWS:SPEC:5003; JAXWS:SPEC:7000; JAXWS:SPEC:7002; JAXWS:SPEC:7008;
   * JAXWS:SPEC:7009;
   *
   * @test_Strategy: Test the various getPayload and setPayload methods in the
   * handlers
   */
  public void SetGetPayloadJAXBContextTest() throws Fault {
    TestUtil.logTrace("SetGetPayloadJAXBContextTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    } else {
      try {
        String expected = "client:OutboundClientLogicalHandler_getsetPayloadJAXBContext:InboundServerLogicalHandler_getsetPayloadJAXBContext:Hello3Impl:OutboundServerLogicalHandler_getsetPayloadJAXBContext:InboundClientLogicalHandler_getsetPayloadJAXBContext";

        Hello3Request hello3Req = new Hello3Request();
        hello3Req.setTestname("setgetPayloadJAXBContextTest");
        hello3Req.setArgument("client");

        Hello3Response hello3Res = port.hello(hello3Req);
        String actual = hello3Res.getArgument();
        TestUtil.logMsg("Hello3Response=" + actual);
        if (!actual.equals(expected)) {
          TestUtil.logErr("Error: Did not get expected result:");
          TestUtil.logErr("expected=" + expected);
          TestUtil.logErr("actual=" + actual);
          pass = false;
        }
      } catch (Exception e) {
        pass = false;
        e.printStackTrace();
      }
    }
    if (!pass)
      throw new Fault("SetGetPayloadJAXBContextTest failed");
  }

}
