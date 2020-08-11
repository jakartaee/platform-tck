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
 *  @(#)Client.java	1.16 06/02/11
 */

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.*;
import jakarta.xml.ws.handler.*;
import jakarta.xml.ws.handler.soap.*;
import jakarta.xml.ws.soap.*;
import javax.xml.namespace.QName;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.common.*;

import javax.naming.InitialContext;
import jakarta.xml.ws.WebServiceRef;
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

  private static final String CLIENTDELAY = "client.delay";

  private String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private int clientDelay = 1;

  private static final String HARNESSHOST = "harness.host";

  private String harnessHost = null;

  private static final String HARNESSLOGPORT = "harness.log.port";

  private String harnessLogPort = null;

  private static final String TRACEFLAG = "harness.log.traceflag";

  private String harnessLogTraceFlag = "false"; // false or true

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String WSDLLOC_URL = "dlowhandlertest.wsdlloc.1";

  private static final String ENDPOINT1_URL = "dlowhandlertest.endpoint.1";

  private static final String ENDPOINT4_URL = "dlowhandlertest.endpoint.2";

  private String url1 = null;

  private String url4 = null;

  private URL wsdlurl = null;

  // service and port information
  private static final String NAMESPACEURI = "http://dlowhandlertestservice.org/wsdl";

  private static final String SERVICE_NAME = "DLOWHandlerTestService";

  private static final String PORT_NAME1 = "HelloPort";

  private static final String PORT_NAME4 = "GetTrackerDataPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME1 = new QName(NAMESPACEURI, PORT_NAME1);

  private static final Class SERVICE_CLASS = com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.DLOWHandlerTestService.class;

  private static final String THEBINDINGPROTOCOL = jakarta.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING;

  private static final String LOGICAL = "Logical";

  private static final String SOAP = "SOAP";

  private static final String TEST_TYPE = LOGICAL + "Test";

  private Handler handler = null;

  Hello port1 = null;

  GetTrackerData port4 = null;

  static DLOWHandlerTestService service = null;

  BindingProvider bp1 = null;

  BindingProvider bp4 = null;

  Binding binding1 = null;

  Binding binding4 = null;

  List<Binding> listOfBindings = new ArrayList<Binding>();

  List<Handler> port1HandlerChain = null;

  List<Handler> port4HandlerChain = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT1_URL);
    url1 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(ENDPOINT4_URL);
    url4 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint1 URL: " + url1);
    TestUtil.logMsg("Service Endpoint4 URL: " + url4);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
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
    JAXWS_Util.setTargetEndpointAddress(port1, url1);
    JAXWS_Util.setTargetEndpointAddress(port4, url4);
  }

  private void getPortsJavaEE() throws Exception {
    TestUtil.logMsg("Obtaining service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    getPorts();
    getTargetEndpointAddress(port1, port4);
  }

  private void getTargetEndpointAddress(Object port1, Object port4)
      throws Exception {
    TestUtil.logMsg("Get Target Endpoint Address for port1=" + port1);
    String url1 = JAXWS_Util.getTargetEndpointAddress(port1);
    TestUtil.logMsg("Target Endpoint Address=" + url1);
    TestUtil.logMsg("Get Target Endpoint Address for port4=" + port4);
    String url4 = JAXWS_Util.getTargetEndpointAddress(port4);
    TestUtil.logMsg("Target Endpoint Address=" + url4);
  }

  private void getPorts() throws Exception {
    TestUtil.logTrace("entering getPorts");

    TestUtil.logMsg("Get port 1 = " + PORT_NAME1);
    port1 = (Hello) service.getPort(Hello.class);
    TestUtil.logMsg("port1=" + port1);

    TestUtil.logMsg("Get port 4 = " + PORT_NAME4);
    port4 = (GetTrackerData) service.getPort(GetTrackerData.class);
    TestUtil.logMsg("port4=" + port4);

    TestUtil.logMsg("Get binding for port 1 = " + PORT_NAME1);
    bp1 = (BindingProvider) port1;
    binding1 = bp1.getBinding();
    port1HandlerChain = binding1.getHandlerChain();
    TestUtil.logMsg("Port1 HandlerChain =" + port1HandlerChain);
    TestUtil.logMsg("Port1 HandlerChain size = " + port1HandlerChain.size());

    TestUtil.logMsg("------------------------------------------------------");

    TestUtil.logMsg("Get binding for port 4 = " + PORT_NAME4);
    bp4 = (BindingProvider) port4;
    binding4 = bp4.getBinding();
    port4HandlerChain = binding4.getHandlerChain();
    TestUtil.logMsg("Port4 HandlerChain=" + port4HandlerChain);
    TestUtil.logMsg("Port4 HandlerChain size = " + port4HandlerChain.size());

    listOfBindings.add(binding1);
    listOfBindings.add(binding4);

    TestUtil.logTrace("leaving getPorts");
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
   * client.delay;
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

      try {
        harnessHost = p.getProperty(HARNESSHOST);
      } catch (Exception e) {
        harnessHost = null;
      }
      try {
        harnessLogPort = p.getProperty(HARNESSLOGPORT);
      } catch (Exception e) {
        harnessLogPort = null;
      }
      try {
        harnessLogTraceFlag = p.getProperty(TRACEFLAG);
      } catch (Exception e) {
        harnessLogTraceFlag = "false";
      }
      try {
        clientDelay = Integer.parseInt(p.getProperty(CLIENTDELAY));
      } catch (Exception e) {
        TestUtil.logMsg("An ERROR occurred for the property " + CLIENTDELAY
            + ", using default value of " + clientDelay + " second");
        TestUtil.printStackTrace(e);
      }

      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        TestUtil.logMsg("Create Service object");
        getTestURLs();
        service = (DLOWHandlerTestService) JAXWS_Util.getService(wsdlurl,
            SERVICE_QNAME, SERVICE_CLASS);
      } else {
        getTestURLs();
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (DLOWHandlerTestService) getSharedObject();
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
   * @testName: ClientOneWayHandlerTest
   *
   * @assertion_ids: JAXWS:SPEC:9002; JAXWS:SPEC:9007; JAXWS:SPEC:9012;
   * JAXWS:SPEC:9014; JAXWS:SPEC:9015.1; JAXWS:SPEC:9017; JAXWS:SPEC:9018;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the client-side
   * logical message handler callbacks are called.
   */
  public void ClientOneWayHandlerTest() throws Fault {
    TestUtil.logTrace("ClientOneWayHandlerTest");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Programatically registering the client side handlers by creating new HandlerResolver.");
      service.setHandlerResolver(new HandlerResolver() {
        public List<Handler> getHandlerChain(PortInfo info) {
          List<Handler> handlerList = new ArrayList<Handler>();
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following service based handlers through the binding: \n"
                  + "ClientSOAPHandler1, ClientLogicalHandler1");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler1 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientSOAPHandler1();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler1 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientLogicalHandler1();
          handlerList.add(handler);
          if (info.getBindingID().equals(THEBINDINGPROTOCOL)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Programmatically registering the following protocol based handlers through the binding: \n"
                    + "ClientSOAPHandler2, ClientLogicalHandler2");
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler2 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientSOAPHandler2();
            handlerList.add(handler);
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler2 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientLogicalHandler2();
            handlerList.add(handler);
          }
          if (info.getPortName().equals(PORT_QNAME1)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME1);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler3 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientSOAPHandler3();
            handlerList.add(handler);
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler3 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientLogicalHandler3();
            handlerList.add(handler);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });

      if (!setupPorts()) {
        pass = false;
      }
      if (pass) {

        TestUtil.logMsg("Invoking RPC method port1.doHandlerTest1()");
        MyAction ma = new MyAction();
        ma.setAction("ClientLogicalTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          port1.doHandlerTest1(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        Handler_Util.clearHandlers(listOfBindings);

        TestUtil.logMsg("Get client side results back from Tracker");
        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        // verify client-side callbacks

        if (!Handler_Util.VerifyOneWayCallbacks("Client", Constants.OUTBOUND,
            clientSideMsgs)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault("ClientOneWayHandlerTest failed");
  }

  /*
   * @testName: ClientLogicalOutboundHandleMessageThrowsSOAPFaultTest
   *
   * @assertion_ids: JAXWS:SPEC:9002; JAXWS:SPEC:9007; JAXWS:SPEC:9014;
   * JAXWS:SPEC:9015.3.2; JAXWS:SPEC:9017; JAXWS:SPEC:9018;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side soap
   * message handler callbacks are called.
   */
  public void ClientLogicalOutboundHandleMessageThrowsSOAPFaultTest()
      throws Fault {
    TestUtil.logTrace("ClientLogicalOutboundHandleMessageThrowsSOAPFaultTest");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Programatically registering the client side handlers by creating new HandlerResolver.");
      service.setHandlerResolver(new HandlerResolver() {
        public List<Handler> getHandlerChain(PortInfo info) {
          List<Handler> handlerList = new ArrayList<Handler>();
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following service based handlers through the binding: \n"
                  + "ClientSOAPHandler1, ClientLogicalHandler1");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler1 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientSOAPHandler1();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler1 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientLogicalHandler1();
          handlerList.add(handler);
          if (info.getBindingID().equals(THEBINDINGPROTOCOL)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Programmatically registering the following protocol based handlers through the binding: \n"
                    + "ClientSOAPHandler2, ClientLogicalHandler2");
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler2 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientSOAPHandler2();
            handlerList.add(handler);
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler2 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientLogicalHandler2();
            handlerList.add(handler);
          }
          if (info.getPortName().equals(PORT_QNAME1)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME1);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler3 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientSOAPHandler3();
            handlerList.add(handler);
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler3 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientLogicalHandler3();
            handlerList.add(handler);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });

      if (!setupPorts()) {
        pass = false;
      }
      if (pass) {

        TestUtil.logMsg("Invoking RPC method port1.doHandlerTest1()");
        MyAction ma = new MyAction();
        ma.setAction("ClientLogicalOutboundHandleMessageThrowsSOAPFaultTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          port1.doHandlerTest1(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        Handler_Util.clearHandlers(listOfBindings);

        TestUtil.logMsg("Get client side results back from Tracker");
        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        // verify client-side callbacks

        if (!Handler_Util.VerifyOneWaySOAPFaultCallbacks("Client",
            Constants.OUTBOUND, LOGICAL, clientSideMsgs)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "ClientLogicalOutboundHandleMessageThrowsSOAPFaultTest failed");
  }

  /*
   * @testName: ClientSOAPOutboundHandleMessageThrowsSOAPFaultTest
   *
   * @assertion_ids: JAXWS:SPEC:9002; JAXWS:SPEC:9007; JAXWS:SPEC:9014;
   * JAXWS:SPEC:9015.3.2; JAXWS:SPEC:9017; JAXWS:SPEC:9018;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side soap
   * message handler callbacks are called.
   */
  public void ClientSOAPOutboundHandleMessageThrowsSOAPFaultTest()
      throws Fault {
    TestUtil.logTrace("ClientSOAPOutboundHandleMessageThrowsSOAPFaultTest");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Programatically registering the client side handlers by creating new HandlerResolver.");
      service.setHandlerResolver(new HandlerResolver() {
        public List<Handler> getHandlerChain(PortInfo info) {
          List<Handler> handlerList = new ArrayList<Handler>();
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following service based handlers through the binding: \n"
                  + "ClientLogicalHandler1, ClientSOAPHandler1");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler1 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientLogicalHandler1();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler1 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientSOAPHandler1();
          handlerList.add(handler);
          if (info.getBindingID().equals(THEBINDINGPROTOCOL)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Programmatically registering the following protocol based handlers through the binding: \n"
                    + "ClientLogicalHandler2, ClientSOAPHandler2");
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler2 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientLogicalHandler2();
            handlerList.add(handler);
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler2 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientSOAPHandler2();
            handlerList.add(handler);
          }
          if (info.getPortName().equals(PORT_QNAME1)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME1);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler3 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientLogicalHandler3();
            handlerList.add(handler);
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler3 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientSOAPHandler3();
            handlerList.add(handler);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });

      if (!setupPorts()) {
        pass = false;
      }
      if (pass) {

        TestUtil.logMsg("Invoking RPC method port1.doHandlerTest1()");
        MyAction ma = new MyAction();
        ma.setAction("ClientSOAPOutboundHandleMessageThrowsSOAPFaultTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          port1.doHandlerTest1(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        Handler_Util.clearHandlers(listOfBindings);

        TestUtil.logMsg("Get client side results back from Tracker");
        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        // verify client-side callbacks

        if (!Handler_Util.VerifyOneWaySOAPFaultCallbacks("Client",
            Constants.OUTBOUND, SOAP, clientSideMsgs)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "ClientSOAPOutboundHandleMessageThrowsSOAPFaultTest failed");
  }

  /*
   * @testName: ClientLogicalOutboundHandleMessageReturnsFalseTest
   *
   * @assertion_ids: JAXWS:SPEC:9002; JAXWS:SPEC:9007; JAXWS:SPEC:9014;
   * JAXWS:SPEC:9015.2.2; JAXWS:SPEC:9017; JAXWS:SPEC:9018;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side soap
   * message handler callbacks are called.
   */
  public void ClientLogicalOutboundHandleMessageReturnsFalseTest()
      throws Fault {
    TestUtil.logTrace("ClientLogicalOutboundHandleMessageReturnsFalseTest");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Programatically registering the client side handlers by creating new HandlerResolver.");
      service.setHandlerResolver(new HandlerResolver() {
        public List<Handler> getHandlerChain(PortInfo info) {
          List<Handler> handlerList = new ArrayList<Handler>();
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following service based handlers through the binding: \n"
                  + "ClientSOAPHandler1, ClientLogicalHandler1");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler1 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientSOAPHandler1();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler1 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientLogicalHandler1();
          handlerList.add(handler);
          if (info.getBindingID().equals(THEBINDINGPROTOCOL)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Programmatically registering the following protocol based handlers through the binding: \n"
                    + "ClientSOAPHandler2, ClientLogicalHandler2");
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler2 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientSOAPHandler2();
            handlerList.add(handler);
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler2 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientLogicalHandler2();
            handlerList.add(handler);
          }
          if (info.getPortName().equals(PORT_QNAME1)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME1);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler3 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientSOAPHandler3();
            handlerList.add(handler);
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler3 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientLogicalHandler3();
            handlerList.add(handler);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });

      if (!setupPorts()) {
        pass = false;
      }
      if (pass) {

        TestUtil.logMsg("Invoking RPC method port1.doHandlerTest1()");
        MyAction ma = new MyAction();
        ma.setAction("ClientLogicalOutboundHandleMessageReturnsFalseTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          port1.doHandlerTest1(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        Handler_Util.clearHandlers(listOfBindings);

        TestUtil.logMsg("Get client side results back from Tracker");
        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        // verify client-side callbacks

        if (!Handler_Util.VerifyOneWayHandleMessageFalseCallbacks("Client",
            Constants.OUTBOUND, LOGICAL, clientSideMsgs)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "ClientLogicalOutboundHandleMessageReturnsFalseTest failed");
  }

  /*
   * @testName: ClientSOAPOutboundHandleMessageReturnsFalseTest
   *
   * @assertion_ids: JAXWS:SPEC:9002; JAXWS:SPEC:9007; JAXWS:SPEC:9014;
   * JAXWS:SPEC:9015.2.2; JAXWS:SPEC:9017; JAXWS:SPEC:9018;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side soap
   * message handler callbacks are called.
   */
  public void ClientSOAPOutboundHandleMessageReturnsFalseTest() throws Fault {
    TestUtil.logTrace("ClientSOAPOutboundHandleMessageReturnsFalseTest");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Programatically registering the client side handlers by creating new HandlerResolver.");
      service.setHandlerResolver(new HandlerResolver() {
        public List<Handler> getHandlerChain(PortInfo info) {
          List<Handler> handlerList = new ArrayList<Handler>();
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following service based handlers through the binding: \n"
                  + "ClientLogicalHandler1, ClientSOAPHandler1");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler1 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientLogicalHandler1();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler1 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientSOAPHandler1();
          handlerList.add(handler);
          if (info.getBindingID().equals(THEBINDINGPROTOCOL)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Programmatically registering the following protocol based handlers through the binding: \n"
                    + "ClientLogicalHandler2, ClientSOAPHandler2");
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler2 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientLogicalHandler2();
            handlerList.add(handler);
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler2 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientSOAPHandler2();
            handlerList.add(handler);
          }
          if (info.getPortName().equals(PORT_QNAME1)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME1);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler3 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientLogicalHandler3();
            handlerList.add(handler);
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler3 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.ee.w2j.document.literal.onewayhandlertest.client.ClientSOAPHandler3();
            handlerList.add(handler);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });

      if (!setupPorts()) {
        pass = false;
      }
      if (pass) {

        TestUtil.logMsg("Invoking RPC method port1.doHandlerTest1()");
        MyAction ma = new MyAction();
        ma.setAction("ClientSOAPOutboundHandleMessageReturnsFalseTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          port1.doHandlerTest1(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        Handler_Util.clearHandlers(listOfBindings);

        TestUtil.logMsg("Get client side results back from Tracker");
        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        // verify client-side callbacks

        if (!Handler_Util.VerifyOneWayHandleMessageFalseCallbacks("Client",
            Constants.OUTBOUND, SOAP, clientSideMsgs)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault("ClientSOAPOutboundHandleMessageReturnsFalseTest failed");
  }

  /*
   * @testName: ServerLogicalHandlerTest
   *
   * @assertion_ids: JAXWS:SPEC:9002; JAXWS:SPEC:9007; JAXWS:SPEC:9014;
   * JAXWS:SPEC:9015.1; JAXWS:SPEC:9017; JAXWS:SPEC:9018;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side soap
   * message handler callbacks are called.
   */
  public void ServerLogicalHandlerTest() throws Fault {
    TestUtil.logTrace("ServerLogicalHandlerTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    }
    if (pass) {
      Handler_Util.clearHandlers(listOfBindings);
      try {
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        TestUtil.logMsg("Invoking RPC method port1.doHandlerTest1()");
        MyAction ma = new MyAction();
        ma.setAction("ServerLogicalTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        // MyResultType mr = null;
        try {
          // mr = port1.doHandlerTest1(ma);
          port1.doHandlerTest1(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(
            "\n-------------------------------------------------------------------\n");
        sb.append("Sleeping: " + clientDelay
            + " second(s) before getting results from server\n");
        sb.append(
            "-------------------------------------------------------------------\n");
        TestUtil.logMsg(sb.toString());
        TestUtil.sleepSec(clientDelay);
        List<String> serverSideMsgs = null;

        TestUtil.logMsg("Get server side result back from endpoint");
        GetTrackerDataAction gtda = new GetTrackerDataAction();
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        TestUtil.logMsg("Verifying Server-Side Handler callbacks");
        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);

        if (!Handler_Util.VerifyOneWayCallbacks("Server", Constants.INBOUND,
            serverSideMsgs)) {
          TestUtil.logErr("Server-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side Callbacks are (correct)");
        }

        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();
      } catch (Exception e) {
        TestUtil.logErr("Exception occurred: " + e);
        pass = false;
      }
    }
    if (!pass)
      throw new Fault("ServerLogicalHandlerTest failed");
  }

  /*
   * @testName: ServerLogicalInboundHandleMessageThrowsSOAPFaultTest
   *
   * @assertion_ids: JAXWS:SPEC:9002; JAXWS:SPEC:9007; JAXWS:SPEC:9014;
   * JAXWS:SPEC:9015.3.2; JAXWS:SPEC:9017; JAXWS:SPEC:9018;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side soap
   * message handler callbacks are called.
   */
  public void ServerLogicalInboundHandleMessageThrowsSOAPFaultTest()
      throws Fault {
    TestUtil.logTrace("ServerLogicalInboundHandleMessageThrowsSOAPFaultTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    }
    if (pass) {
      Handler_Util.clearHandlers(listOfBindings);
      try {
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        TestUtil.logMsg("Invoking RPC method port1.doHandlerTest1()");
        MyAction ma = new MyAction();
        ma.setAction("ServerLogicalInboundHandleMessageThrowsSOAPFaultTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        // MyResultType mr = null;
        try {
          // mr = port1.doHandlerTest1(ma);
          port1.doHandlerTest1(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(
            "\n-------------------------------------------------------------------\n");
        sb.append("Sleeping: " + clientDelay
            + " second(s) before getting results from server\n");
        sb.append(
            "-------------------------------------------------------------------\n");
        TestUtil.logMsg(sb.toString());
        TestUtil.sleepSec(clientDelay);
        List<String> serverSideMsgs = null;

        TestUtil.logMsg("Get server side result back from endpoint");
        GetTrackerDataAction gtda = new GetTrackerDataAction();
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        TestUtil.logMsg("Verifying Server-Side Handler callbacks");
        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);

        if (!Handler_Util.VerifyOneWaySOAPFaultCallbacks("Server",
            Constants.INBOUND, LOGICAL, serverSideMsgs)) {
          TestUtil.logErr("Server-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side Callbacks are (correct)");
        }

        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();
      } catch (Exception e) {
        TestUtil.logErr("Exception occurred: " + e);
        pass = false;
      }
    }
    if (!pass)
      throw new Fault(
          "ServerLogicalInboundHandleMessageThrowsSOAPFaultTest failed");
  }

  /*
   * @testName: ServerSOAPInboundHandleMessageThrowsSOAPFaultTest
   *
   * @assertion_ids: JAXWS:SPEC:9002; JAXWS:SPEC:9007; JAXWS:SPEC:9014;
   * JAXWS:SPEC:9015.3.2; JAXWS:SPEC:9017; JAXWS:SPEC:9018;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side soap
   * message handler callbacks are called.
   */
  public void ServerSOAPInboundHandleMessageThrowsSOAPFaultTest() throws Fault {
    TestUtil.logTrace("ServerSOAPInboundHandleMessageThrowsSOAPFaultTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    }
    if (pass) {
      Handler_Util.clearHandlers(listOfBindings);
      try {
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        TestUtil.logMsg("Invoking RPC method port1.doHandlerTest1()");
        MyAction ma = new MyAction();
        ma.setAction("ServerSOAPInboundHandleMessageThrowsSOAPFaultTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        // MyResultType mr = null;
        try {
          // mr = port1.doHandlerTest1(ma);
          port1.doHandlerTest1(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(
            "\n-------------------------------------------------------------------\n");
        sb.append("Sleeping: " + clientDelay
            + " second(s) before getting results from server\n");
        sb.append(
            "-------------------------------------------------------------------\n");
        TestUtil.logMsg(sb.toString());
        TestUtil.sleepSec(clientDelay);
        List<String> serverSideMsgs = null;

        TestUtil.logMsg("Get server side result back from endpoint");
        GetTrackerDataAction gtda = new GetTrackerDataAction();
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        TestUtil.logMsg("Verifying Server-Side Handler callbacks");
        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);

        if (!Handler_Util.VerifyOneWaySOAPFaultCallbacks("Server",
            Constants.INBOUND, SOAP, serverSideMsgs)) {
          TestUtil.logErr("Server-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side Callbacks are (correct)");
        }

        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();
      } catch (Exception e) {
        TestUtil.logErr("Exception occurred: " + e);
        pass = false;
      }
    }
    if (!pass)
      throw new Fault(
          "ServerSOAPInboundHandleMessageThrowsSOAPFaultTest failed");
  }

  /*
   * @testName: ServerLogicalInboundHandleMessageReturnsFalseTest
   *
   * @assertion_ids: JAXWS:SPEC:9002; JAXWS:SPEC:9007; JAXWS:SPEC:9014;
   * JAXWS:SPEC:9015.2.2; JAXWS:SPEC:9017; JAXWS:SPEC:9018;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side soap
   * message handler callbacks are called.
   */
  public void ServerLogicalInboundHandleMessageReturnsFalseTest() throws Fault {
    TestUtil.logTrace("ServerLogicalInboundHandleMessageReturnsFalseTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    }
    if (pass) {
      Handler_Util.clearHandlers(listOfBindings);
      try {
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        TestUtil.logMsg("Invoking RPC method port1.doHandlerTest1()");
        MyAction ma = new MyAction();
        ma.setAction("ServerLogicalInboundHandleMessageReturnsFalseTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        // MyResultType mr = null;
        try {
          // mr = port1.doHandlerTest1(ma);
          port1.doHandlerTest1(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(
            "\n-------------------------------------------------------------------\n");
        sb.append("Sleeping: " + clientDelay
            + " second(s) before getting results from server\n");
        sb.append(
            "-------------------------------------------------------------------\n");
        TestUtil.logMsg(sb.toString());
        TestUtil.sleepSec(clientDelay);
        List<String> serverSideMsgs = null;

        TestUtil.logMsg("Get server side result back from endpoint");
        GetTrackerDataAction gtda = new GetTrackerDataAction();
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        TestUtil.logMsg("Verifying Server-Side Handler callbacks");
        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);

        if (!Handler_Util.VerifyOneWayHandleMessageFalseCallbacks("Server",
            Constants.INBOUND, LOGICAL, serverSideMsgs)) {
          TestUtil.logErr("Server-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side Callbacks are (correct)");
        }

        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();
      } catch (Exception e) {
        TestUtil.logErr("Exception occurred: " + e);
        pass = false;
      }
    }
    if (!pass)
      throw new Fault(
          "ServerLogicalInboundHandleMessageReturnsFalseTest failed");
  }

  /*
   * @testName: ServerSOAPInboundHandleMessageReturnsFalseTest
   *
   * @assertion_ids: JAXWS:SPEC:9002; JAXWS:SPEC:9007; JAXWS:SPEC:9014;
   * JAXWS:SPEC:9015.2.2; JAXWS:SPEC:9017; JAXWS:SPEC:9018;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side soap
   * message handler callbacks are called.
   */
  public void ServerSOAPInboundHandleMessageReturnsFalseTest() throws Fault {
    TestUtil.logTrace("ServerSOAPInboundHandleMessageReturnsFalseTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    }
    if (pass) {
      Handler_Util.clearHandlers(listOfBindings);
      try {
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        TestUtil.logMsg("Invoking RPC method port1.doHandlerTest1()");
        MyAction ma = new MyAction();
        ma.setAction("ServerSOAPInboundHandleMessageReturnsFalseTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        // MyResultType mr = null;
        try {
          // mr = port1.doHandlerTest1(ma);
          port1.doHandlerTest1(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        StringBuffer sb = new StringBuffer();
        sb.append(
            "\n-------------------------------------------------------------------\n");
        sb.append("Sleeping: " + clientDelay
            + " second(s) before getting results from server\n");
        sb.append(
            "-------------------------------------------------------------------\n");
        TestUtil.logMsg(sb.toString());
        TestUtil.sleepSec(clientDelay);
        List<String> serverSideMsgs = null;

        TestUtil.logMsg("Get server side result back from endpoint");
        GetTrackerDataAction gtda = new GetTrackerDataAction();
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        TestUtil.logMsg("Verifying Server-Side Handler callbacks");
        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);

        if (!Handler_Util.VerifyOneWayHandleMessageFalseCallbacks("Server",
            Constants.INBOUND, SOAP, serverSideMsgs)) {
          TestUtil.logErr("Server-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side Callbacks are (correct)");
        }

        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();
      } catch (Exception e) {
        TestUtil.logErr("Exception occurred: " + e);
        pass = false;
      }
    }
    if (!pass)
      throw new Fault("ServerSOAPInboundHandleMessageReturnsFalseTest failed");
  }

  private void purgeServerSideTrackerData() {
    try {
      GetTrackerDataAction gtda = new GetTrackerDataAction();
      gtda.setAction("purge");
      gtda.setHarnessloghost(harnessHost);
      gtda.setHarnesslogport(harnessLogPort);
      gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
      port4.getTrackerData(gtda);
    } catch (Exception e) {
      TestUtil.logErr("Call to purge server-side tracker data failed:" + e);
    }
  }

  static class MyStatus {
    private boolean status = true;

    public void setStatus(boolean b) {
      status = b;
    }

    public boolean getStatus() {
      return status;
    }
  }

}
