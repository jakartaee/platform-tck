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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws_handler.LogicalHandler;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.*;
import jakarta.xml.ws.ProtocolException;
import jakarta.xml.ws.WebServiceException;
import jakarta.xml.ws.handler.*;
import jakarta.xml.ws.handler.soap.*;
import jakarta.xml.ws.soap.*;
import javax.xml.namespace.QName;

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

  private String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String HARNESSHOST = "harness.host";

  private String harnessHost = null;

  private static final String HARNESSLOGPORT = "harness.log.port";

  private String harnessLogPort = null;

  private static final String TRACEFLAG = "harness.log.traceflag";

  private String harnessLogTraceFlag = "false"; // false or true

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String WSDLLOC_URL = "dlhandlerservice.wsdlloc.1";

  private static final String ENDPOINT1_URL = "dlhandlerservice.endpoint.1";

  private static final String ENDPOINT2_URL = "dlhandlerservice.endpoint.2";

  private static final String ENDPOINT4_URL = "dlhandlerservice.endpoint.4";

  private String url1 = null;

  private String url2 = null;

  private String url4 = null;

  private URL wsdlurl = null;

  // service and port information
  private static final String NAMESPACEURI = "http://dlhandlerservice.org/wsdl";

  private static final String SERVICE_NAME = "DLHandlerService";

  private static final String PORT_NAME1 = "HelloPort";

  private static final String PORT_NAME2 = "Hello2Port";

  private static final String PORT_NAME4 = "GetTrackerDataPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME1 = new QName(NAMESPACEURI, PORT_NAME1);

  private QName PORT_QNAME2 = new QName(NAMESPACEURI, PORT_NAME2);

  private static final Class SERVICE_CLASS = com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.DLHandlerService.class;

  private static final String THEBINDINGPROTOCOL = jakarta.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING;

  private static final String LOGICAL = "Logical";

  private static final String TEST_TYPE = LOGICAL + "Test";

  private Handler handler = null;

  Hello port1 = null;

  Hello2 port2 = null;

  GetTrackerData port4 = null;

  static DLHandlerService service = null;

  BindingProvider bp1 = null;

  BindingProvider bp2 = null;

  BindingProvider bp4 = null;

  Binding binding1 = null;

  Binding binding2 = null;

  Binding binding4 = null;

  List<Binding> listOfBindings = new ArrayList<Binding>();

  List<Handler> port1HandlerChain = null;

  List<Handler> port2HandlerChain = null;

  List<Handler> port4HandlerChain = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT1_URL);
    url1 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(ENDPOINT2_URL);
    url2 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(ENDPOINT4_URL);
    url4 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint1 URL: " + url1);
    TestUtil.logMsg("Service Endpoint2 URL: " + url2);
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
    JAXWS_Util.setTargetEndpointAddress(port2, url2);
    JAXWS_Util.setTargetEndpointAddress(port4, url4);
  }

  private void getPortsJavaEE() throws Exception {
    TestUtil.logMsg("Obtaining service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    getPorts();
    getTargetEndpointAddress(port1, port2, port4);
  }

  private void getTargetEndpointAddress(Object port1, Object port2,
      Object port4) throws Exception {
    TestUtil.logMsg("Get Target Endpoint Address for port1=" + port1);
    String url1 = JAXWS_Util.getTargetEndpointAddress(port1);
    TestUtil.logMsg("Target Endpoint Address=" + url1);
    TestUtil.logMsg("Get Target Endpoint Address for port2=" + port2);
    String url2 = JAXWS_Util.getTargetEndpointAddress(port2);
    TestUtil.logMsg("Target Endpoint Address=" + url2);
    TestUtil.logMsg("Get Target Endpoint Address for port4=" + port4);
    String url4 = JAXWS_Util.getTargetEndpointAddress(port4);
    TestUtil.logMsg("Target Endpoint Address=" + url4);
  }

  private void getPorts() throws Exception {
    TestUtil.logTrace("entering getPorts");

    TestUtil.logMsg("Get port 1 = " + PORT_NAME1);
    port1 = (Hello) service.getPort(Hello.class);
    TestUtil.logMsg("port1=" + port1);

    TestUtil.logMsg("Get port 2 = " + PORT_NAME2);
    port2 = (Hello2) service.getPort(Hello2.class);
    TestUtil.logMsg("port2=" + port2);

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

    TestUtil.logMsg("Get binding for port 2 = " + PORT_NAME2);
    bp2 = (BindingProvider) port2;
    binding2 = bp2.getBinding();
    port2HandlerChain = binding2.getHandlerChain();
    TestUtil.logMsg("Port2 HandlerChain=" + port2HandlerChain);
    TestUtil.logMsg("Port2 HandlerChain size = " + port2HandlerChain.size());

    TestUtil.logMsg("------------------------------------------------------");

    TestUtil.logMsg("Get binding for port 4 = " + PORT_NAME4);
    bp4 = (BindingProvider) port4;
    binding4 = bp4.getBinding();
    port4HandlerChain = binding4.getHandlerChain();
    TestUtil.logMsg("Port4 HandlerChain=" + port4HandlerChain);
    TestUtil.logMsg("Port4 HandlerChain size = " + port4HandlerChain.size());

    listOfBindings.add(binding1);
    listOfBindings.add(binding2);
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
        service = (DLHandlerService) JAXWS_Util.getService(wsdlurl,
            SERVICE_QNAME, SERVICE_CLASS);
      } else {
        getTestURLs();
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (DLHandlerService) getSharedObject();
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
   * @testName: ClientLogicalHandlerTest
   *
   * @assertion_ids: JAXWS:SPEC:9002; JAXWS:SPEC:9007; JAXWS:SPEC:9012;
   * JAXWS:SPEC:9014; JAXWS:SPEC:9015.1; JAXWS:SPEC:9017; JAXWS:SPEC:9018;
   * WS4EE:SPEC:6010; WS4EE:SPEC:6015.1; WS4EE:SPEC:6015.2; WS4EE:SPEC:6015.3;
   * WS4EE:SPEC:6008; WS4EE:SPEC:6028; WS4EE:SPEC:6005; WS4EE:SPEC:6051;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the client-side
   * logical message handler callbacks are called.
   */
  public void ClientLogicalHandlerTest() throws Fault {
    TestUtil.logTrace("ClientLogicalHandlerTest");
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
                  + "ClientSOAPHandler5, ClientLogicalHandler5");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler5();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler5();
          handlerList.add(handler);
          if (info.getBindingID().equals(THEBINDINGPROTOCOL)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Programmatically registering the following protocol based handlers through the binding: \n"
                    + "ClientSOAPHandler1, ClientLogicalHandler1");
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler1 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler1();
            handlerList.add(handler);
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler1 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler1();
            handlerList.add(handler);
          }
          if (info.getPortName().equals(PORT_QNAME1)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME1);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler2 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler2();
            handlerList.add(handler);
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler2 and add to HandlerChain");
            handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler2();
            handlerList.add(handler);
            if (info.getPortName().equals(PORT_QNAME1)) {
              TestUtil.logMsg(
                  "Construct HandleInfo for ClientSOAPHandler3 and add to HandlerChain");
              handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler3();
              handlerList.add(handler);
              TestUtil.logMsg(
                  "Construct HandleInfo for ClientLogicalHandler3 and add to HandlerChain");
              handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler3();
              handlerList.add(handler);
            }
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });

      if (!setupPorts()) {
        pass = false;
      } else {
        try {
          TestUtil.logMsg("Getting existing Handlers for Port1");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding1.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientLogicalHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding1.setHandlerChain(handlerList);
        } catch (Exception e) {
          TestUtil.logErr(
              "ERROR: Adding handlers to the binding failed with the following exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
      }
      if (pass) {

        TestUtil.logMsg("Invoking RPC method port1.doHandlerTest1()");
        MyActionType ma = new MyActionType();
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

        TestUtil.logMsg("Verify handleMessage()/init callbacks");
        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandlerCallBacks("Client", LOGICAL,
            clientSideMsgs)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg(
            "Verifying callbacks where LogicalHandlers were called before SOAPHandlers on Client-Side");
        if (!Handler_Util.VerifyLogicalVerseSOAPHandlerOrder(clientSideMsgs)) {
          TestUtil.logErr(
              "Client-Side Logical verses SOAP Handler Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg(
              "Client-Side Logical verses SOAP Handler Callbacks are (correct)");
        }
        TestUtil.logMsg("Get client side throwables back from Tracker");
        String[] clientSideThrowables = HandlerTracker.getArrayThrowables();
        int len = clientSideThrowables.length;
        if (len > 0) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          for (int i = 0; i <= len - 1; i++) {
            TestUtil.logErr(clientSideThrowables[i]);
            pass = false;
          }
        } else {
          TestUtil.logMsg("There were no Client Handler exceptions");
        }

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault("ClientLogicalHandlerTest failed");
  }

  /*
   * @testName: ServerLogicalHandlerTest
   *
   * @assertion_ids: JAXWS:SPEC:9002; JAXWS:SPEC:9007; JAXWS:SPEC:9014;
   * JAXWS:SPEC:9015.1; JAXWS:SPEC:9017; JAXWS:SPEC:9018; WS4EE:SPEC:6010;
   * WS4EE:SPEC:6008; WS4EE:SPEC:6028; WS4EE:SPEC:6005; WS4EE:SPEC:6051;
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
        MyActionType ma = new MyActionType();
        ma.setAction("ServerLogicalTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        MyResultType mr = null;
        try {
          mr = port1.doHandlerTest1(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        if (mr != null) {
          String result = mr.getErrors();
          if (!result.equals("")) {
            pass = false;
            TestUtil
                .logErr("The serverside tests for MessageContext.Scope failed:"
                    + result);
          }
        }

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

        if (!Handler_Util.VerifyHandlerCallBacks("Server", LOGICAL,
            serverSideMsgs)) {
          TestUtil.logErr("Server-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side Callbacks are (correct)");
        }

        TestUtil.logMsg(
            "Verifying callbacks where LogicalHandlers are called before SOAPHandlers on Server-Side");
        if (!Handler_Util.VerifyLogicalVerseSOAPHandlerOrder(serverSideMsgs)) {
          TestUtil.logErr(
              "Server-Side Logical verses SOAP Handler Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg(
              "Server-Side Logical verses SOAP Handler Callbacks are (correct)");
        }
        gtda = new GetTrackerDataAction();
        TestUtil.logMsg("Get server side throwables back from endpoint");
        gtda.setAction("getArrayThrowables");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideThrowables = port4.getTrackerData(gtda)
            .getResult();
        if (serverSideThrowables.size() >= 1) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          Iterator iterator = serverSideThrowables.iterator();
          while (iterator.hasNext()) {
            TestUtil.logErr((String) iterator.next());
          }
          pass = false;
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
   * @testName: ClientLogicalInboundHandleMessageThrowsRuntimeExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:9015.4.2; JAXWS:SPEC:9016.1; JAXWS:SPEC:9017;
   * JAXWS:SPEC:9018; WS4EE:SPEC:6008; WS4EE:SPEC:6028; WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ClientLogicalHandler4 throws a RuntimeException in handleMessage method
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientLogicalHandler5.handleMessage().doOutbound()
   * ClientLogicalHandler4.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleMessage().doInbound()
   * ClientLogicalHandler4.handleMessage().doInbound() ClientLogicalHandler4
   * Throwing an inbound RuntimeException ClientLogicalHandler6.close()
   * ClientLogicalHandler4.close() ClientLogicalHandler5.close()
   */
  public void ClientLogicalInboundHandleMessageThrowsRuntimeExceptionTest()
      throws Fault {
    TestUtil.logTrace(
        "ClientLogicalInboundHandleMessageThrowsRuntimeExceptionTest");
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
                  + "ClientSOAPHandler5, ClientLogicalHandler5");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler5();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler5();
          handlerList.add(handler);

          if (info.getPortName().equals(PORT_QNAME2)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME2);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler4();
            handlerList.add(h4);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });
      if (!setupPorts()) {
        pass = false;
      } else {
        try {
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg("Getting existing Handlers for Port2");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding2.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientLogicalHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding2.setHandlerChain(handlerList);
        } catch (Exception e) {
          TestUtil.logErr(
              "ERROR: Adding handlers to the binding failed with the following exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
      }

      if (pass) {

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ClientLogicalInboundHandleMessageThrowsRuntimeExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting RuntimeException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected RuntimeException");
          pass = false;
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did get expected RuntimeException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientLogicalHandler4.handleMessage throwing an inbound RuntimeException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected RuntimeException text");
          else {
            TestUtil.logErr("Did not get expected RuntimeException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Client", LOGICAL,
            false, Constants.INBOUND, clientSideMsgs)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Get client side throwables back from Tracker");
        String[] clientSideThrowables = HandlerTracker.getArrayThrowables();
        int len = clientSideThrowables.length;
        if (len > 0) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          for (int i = 0; i <= len - 1; i++) {
            TestUtil.logErr(clientSideThrowables[i]);
            pass = false;
          }
        } else {
          TestUtil.logMsg("There were no Client Handler exceptions");
        }

        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "ClientLogicalInboundHandleMessageThrowsRuntimeExceptionTest failed");
  }

  /*
   * @testName: ClientLogicalInboundHandleMessageThrowsSOAPFaultTest
   *
   * @assertion_ids: JAXWS:SPEC:9015.3.2; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ClientLogicalHandler4 throws a SOAPFaultException in handleMessage method
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientLogicalHandler5.handleMessage().doOutbound()
   * ClientLogicalHandler4.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleMessage().doInbound()
   * ClientLogicalHandler4.handleMessage().doInbound() ClientLogicalHandler4
   * Throwing an inbound SOAPFaultException ClientLogicalHandler6.close()
   * ClientLogicalHandler4.close() ClientLogicalHandler5.close()
   */
  public void ClientLogicalInboundHandleMessageThrowsSOAPFaultTest()
      throws Fault {
    TestUtil.logTrace("ClientLogicalInboundHandleMessageThrowsSOAPFaultTest");
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
                  + "ClientSOAPHandler5, ClientLogicalHandler5");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler5();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler5();
          handlerList.add(handler);

          if (info.getPortName().equals(PORT_QNAME2)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME2);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler4();
            handlerList.add(h4);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });
      if (!setupPorts()) {
        pass = false;
      } else {
        try {
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg("Getting existing Handlers for Port2");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding2.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientLogicalHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding2.setHandlerChain(handlerList);
        } catch (Exception e) {
          TestUtil.logErr(
              "ERROR: Adding handlers to the binding failed with the following exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
      }

      if (pass) {

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

        MyActionType ma = new MyActionType();
        ma.setAction("ClientLogicalInboundHandleMessageThrowsSOAPFaultTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg(
              "Expecting WebServiceException that wraps a SOAPFaultException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected WebServiceException");
          pass = false;
        } catch (WebServiceException e) {
          TestUtil.logMsg("Did get expected WebServiceException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "SOAPFaultException: ClientLogicalHandler4.handleMessage throwing an inbound SOAPFaultException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected WebServiceException text");
          else {
            TestUtil.logErr("Did not get expected WebServiceException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandleMessageExceptionCallBacks("Client",
            LOGICAL, clientSideMsgs, Constants.INBOUND)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Get client side throwables back from Tracker");
        String[] clientSideThrowables = HandlerTracker.getArrayThrowables();
        int len = clientSideThrowables.length;
        if (len > 0) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          for (int i = 0; i <= len - 1; i++) {
            TestUtil.logErr(clientSideThrowables[i]);
            pass = false;
          }
        } else {
          TestUtil.logMsg("There were no Client Handler exceptions");
        }

        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "ClientLogicalInboundHandleMessageThrowsSOAPFaultTest failed");
  }

  /*
   * @testName: ClientLogicalOutboundHandleMessageThrowsSOAPFaultTest
   *
   * @assertion_ids: JAXWS:SPEC:9015.3.1; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ClientLogicalHandler4 throws a SOAPFaultException in handleMessage method
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientLogicalHandler5.handleMessage().doOutbound()
   * ClientLogicalHandler4.handleMessage().doOutbound() ClientLogicalHandler4
   * Throwing an inbound SOAPFaultException ClientLogicalHandler4.close()
   * ClientLogicalHandler5.close()
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
                  + "ClientSOAPHandler5, ClientLogicalHandler5");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler5();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler5();
          handlerList.add(handler);

          if (info.getPortName().equals(PORT_QNAME2)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME2);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler4();
            handlerList.add(h4);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });
      if (!setupPorts()) {
        pass = false;
      } else {
        try {
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg("Getting existing Handlers for Port2");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding2.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientLogicalHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding2.setHandlerChain(handlerList);
        } catch (Exception e) {
          TestUtil.logErr(
              "ERROR: Adding handlers to the binding failed with the following exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
      }

      if (pass) {

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

        MyActionType ma = new MyActionType();
        ma.setAction("ClientLogicalOutboundHandleMessageThrowsSOAPFaultTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting SOAPFaultException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected SOAPFaultException");
          pass = false;
        } catch (SOAPFaultException e) {
          TestUtil.logMsg("Did get expected SOAPFaultException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientLogicalHandler4.handleMessage throwing an outbound SOAPFaultException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected SOAPFaultException text");
          else {
            TestUtil.logErr("Did not get expected SOAPFaultException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandleMessageExceptionCallBacks("Client",
            LOGICAL, clientSideMsgs, Constants.OUTBOUND)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Get client side throwables back from Tracker");
        String[] clientSideThrowables = HandlerTracker.getArrayThrowables();
        int len = clientSideThrowables.length;
        if (len > 0) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          for (int i = 0; i <= len - 1; i++) {
            TestUtil.logErr(clientSideThrowables[i]);
            pass = false;
          }
        } else {
          TestUtil.logMsg("There were no Client Handler exceptions");
        }

        Handler_Util.clearHandlers(listOfBindings);
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
   * @testName: ServerLogicalInboundHandleMessageThrowsRuntimeExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:9015.4.1; JAXWS:SPEC:9016.1; JAXWS:SPEC:9017;
   * JAXWS:SPEC:9018; WS4EE:SPEC:6008; WS4EE:SPEC:6028; WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the server-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ServerLogicalHandler4 throws a RuntimeException in handleMessage method
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ServerLogicalHandler6.handleMessage().doInbound()
   * ServerLogicalHandler4.handleMessage().doInbound() ServerLogicalHandler4
   * Throwing an inbound RuntimeException ServerLogicalHandler4.close()
   * ServerLogicalHandler6.close()
   */
  public void ServerLogicalInboundHandleMessageThrowsRuntimeExceptionTest()
      throws Fault {
    TestUtil.logTrace(
        "ServerLogicalInboundHandleMessageThrowsRuntimeExceptionTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    }
    if (pass) {
      Handler_Util.clearHandlers(listOfBindings);
      try {
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ServerLogicalInboundHandleMessageThrowsRuntimeExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg(
              "Expecting SOAPFaultException wrapped in a WebServiceException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected WebServiceException");
          pass = false;
        } catch (WebServiceException e) {
          TestUtil.logMsg("Did get expected WebServiceException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          if (e instanceof jakarta.xml.ws.soap.SOAPFaultException)
            TestUtil.logMsg("Did get expected nested SOAPFaultException");
          else {
            TestUtil.logErr("Did not get expected nested SOAPFaultException");
            TestUtil.printStackTrace(e);
            pass = false;
          }
          String tmp = "ServerLogicalHandler4.handleMessage throwing an inbound RuntimeException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected nested SOAPFaultException text");
          else {
            TestUtil
                .logErr("Did not get expected nested SOAPFaultException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        GetTrackerDataAction gtda = new GetTrackerDataAction();
        TestUtil.logMsg("Get server side result back from endpoint");
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideMsgs = null;
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Server", LOGICAL,
            false, Constants.INBOUND, serverSideMsgs)) {
          TestUtil.logErr("Server-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side Callbacks are (correct)");
        }
        gtda = new GetTrackerDataAction();
        TestUtil.logMsg("Get server side throwables back from endpoint");
        gtda.setAction("getArrayThrowables");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideThrowables = port4.getTrackerData(gtda)
            .getResult();
        if (serverSideThrowables.size() >= 1) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          Iterator iterator = serverSideThrowables.iterator();
          while (iterator.hasNext()) {
            TestUtil.logErr((String) iterator.next());
          }
          pass = false;
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
          "ServerLogicalInboundHandleMessageThrowsRuntimeExceptionTest failed");
  }

  /*
   * @testName: ServerLogicalInboundHandleMessageThrowsSOAPFaultTest
   *
   * @assertion_ids: JAXWS:SPEC:9015.3.1; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the server-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ServerLogicalHandler4 throws a RuntimeException in handleMessage method
   * while processing an inbound message.
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ServerLogicalHandler6.handleMessage().doInbound()
   * ServerLogicalHandler4.handleMessage().doInbound() ServerLogicalHandler4
   * Throwing an inbound SOAPFaultException ServerLogicalHandler6.handleFault()
   * ServerLogicalHandler4.close() ServerLogicalHandler6.close()
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

        MyActionType ma = new MyActionType();
        ma.setAction("ServerLogicalInboundHandleMessageThrowsSOAPFaultTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg(
              "Expecting SOAPFaultException wrapped by a WebServiceException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected WebServiceException");
          pass = false;
        } catch (WebServiceException e) {
          TestUtil.logMsg("Did get expected WebServiceException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          if (e instanceof jakarta.xml.ws.soap.SOAPFaultException)
            TestUtil.logMsg("Did get expected nested SOAPFaultException");
          else {
            TestUtil.logErr("Did not get expected nested SOAPFaultException");
            TestUtil.printStackTrace(e);
            pass = false;
          }
          String tmp = "ServerLogicalHandler4.handleMessage throwing an inbound SOAPFaultException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected nested SOAPFaultException text");
          else {
            TestUtil
                .logErr("Did not get expected nested SOAPFaultException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        GetTrackerDataAction gtda = new GetTrackerDataAction();
        TestUtil.logMsg("Get server side result back from endpoint");
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideMsgs = null;
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);
        if (!Handler_Util.VerifyHandleMessageExceptionCallBacks("Server",
            LOGICAL, serverSideMsgs, Constants.INBOUND)) {
          TestUtil.logErr("Server-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side Callbacks are (correct)");
        }
        gtda = new GetTrackerDataAction();
        TestUtil.logMsg("Get server side throwables back from endpoint");
        gtda.setAction("getArrayThrowables");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideThrowables = port4.getTrackerData(gtda)
            .getResult();
        if (serverSideThrowables.size() >= 1) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          Iterator iterator = serverSideThrowables.iterator();
          while (iterator.hasNext()) {
            TestUtil.logErr((String) iterator.next());
          }
          pass = false;
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
   * @testName: ServerLogicalOutboundHandleMessageThrowsSOAPFaultTest
   *
   * @assertion_ids: JAXWS:SPEC:9015.3.2; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the server-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ServerLogicalHandler4 throws a SOAPFaultException in handleMessage method
   * while processing an outbound message.
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ServerLogicalHandler6.handleMessage().doInbound()
   * ServerLogicalHandler4.handleMessage().doInbound()
   * ServerLogicalHandler5.handleMessage().doInbound()
   * ServerLogicalHandler5.handleMessage().doOutbound()
   * ServerLogicalHandler4.handleMessage().doOutbound() ServerLogicalHandler4
   * Throwing an outbound SOAPFaultException ServerLogicalHandler5.close()
   * ServerLogicalHandler4.close() ServerLogicalHandler6.close()
   */
  public void ServerLogicalOutboundHandleMessageThrowsSOAPFaultTest()
      throws Fault {
    TestUtil.logTrace("ServerLogicalOutboundHandleMessageThrowsSOAPFaultTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    }
    if (pass) {
      Handler_Util.clearHandlers(listOfBindings);
      try {
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        MyActionType ma = new MyActionType();
        ma.setAction("ServerLogicalOutboundHandleMessageThrowsSOAPFaultTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg(
              "Expecting SOAPFaultException wrapped by a WebServiceException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected WebServiceException");
          pass = false;
        } catch (WebServiceException e) {
          TestUtil.logMsg("Did get expected WebServiceException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          if (e instanceof jakarta.xml.ws.soap.SOAPFaultException)
            TestUtil.logMsg("Did get expected nested SOAPFaultException");
          else {
            TestUtil.logErr("Did not get expected nested SOAPFaultException");
            TestUtil.printStackTrace(e);
            pass = false;
          }
          String tmp = "ServerLogicalHandler4.handleMessage throwing an outbound SOAPFaultException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected nested SOAPFaultException text");
          else {
            TestUtil
                .logErr("Did not get expected nested SOAPFaultException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        GetTrackerDataAction gtda = new GetTrackerDataAction();
        TestUtil.logMsg("Get server side result back from endpoint");
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideMsgs = null;
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);
        if (!Handler_Util.VerifyHandleMessageExceptionCallBacks("Server",
            LOGICAL, serverSideMsgs, Constants.OUTBOUND)) {
          TestUtil.logErr("Server-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side Callbacks are (correct)");
        }
        gtda = new GetTrackerDataAction();
        TestUtil.logMsg("Get server side throwables back from endpoint");
        gtda.setAction("getArrayThrowables");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideThrowables = port4.getTrackerData(gtda)
            .getResult();
        if (serverSideThrowables.size() >= 1) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          Iterator iterator = serverSideThrowables.iterator();
          while (iterator.hasNext()) {
            TestUtil.logErr((String) iterator.next());
          }
          pass = false;
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
          "ServerLogicalOutboundHandleMessageThrowsSOAPFaultTest failed");
  }

  /*
   * @testName: ClientLogicalOutboundHandleMessageThrowsRuntimeExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:9015.4.1; JAXWS:SPEC:9016.1; JAXWS:SPEC:9017;
   * JAXWS:SPEC:9018; WS4EE:SPEC:6008; WS4EE:SPEC:6028; WS4EE:SPEC:6005;
   *
   * 
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ClientLogicalHandler4 throws a RuntimeException in handleMessage method
   * while processing an outbound message.
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientLogicalHandler5.handleMessage().doOutbound()
   * ClientLogicalHandler4.handleMessage().doOutbound() ClientLogicalHandler4
   * Throwing an inbound RuntimeException ClientLogicalHandler4.close()
   * ClientLogicalHandler5.close()
   */
  public void ClientLogicalOutboundHandleMessageThrowsRuntimeExceptionTest()
      throws Fault {
    TestUtil.logTrace(
        "ClientLogicalOutboundHandleMessageThrowsRuntimeExceptionTest");
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
                  + "ClientSOAPHandler5, ClientLogicalHandler5");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler5();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler5();
          handlerList.add(handler);

          if (info.getPortName().equals(PORT_QNAME2)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME2);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler4();
            handlerList.add(h4);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }
      });
      if (!setupPorts()) {
        pass = false;
      } else {
        try {
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg("Getting existing Handlers for Port2");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding2.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientLogicalHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding2.setHandlerChain(handlerList);
        } catch (Exception e) {
          TestUtil.logErr(
              "ERROR: Adding handlers to the binding failed with the following exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
      }

      if (pass) {

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ClientLogicalOutboundHandleMessageThrowsRuntimeExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting RuntimeException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected RuntimeException");
          pass = false;
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did get expected RuntimeException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientLogicalHandler4.handleMessage throwing an outbound RuntimeException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected RuntimeException text");
          else {
            TestUtil.logErr("Did not get expected RuntimeException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Client", LOGICAL,
            false, Constants.OUTBOUND, clientSideMsgs)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Get client side throwables back from Tracker");
        String[] clientSideThrowables = HandlerTracker.getArrayThrowables();
        int len = clientSideThrowables.length;
        if (len > 0) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          for (int i = 0; i <= len - 1; i++) {
            TestUtil.logErr(clientSideThrowables[i]);
            pass = false;
          }
        } else {
          TestUtil.logMsg("There were no Client Handler exceptions");
        }

        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "ClientLogicalOutboundHandleMessageThrowsRuntimeExceptionTest failed");
  }

  /*
   * @testName: ServerLogicalOutboundHandleMessageThrowsRuntimeExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:9015.4.2; JAXWS:SPEC:9016.1; JAXWS:SPEC:9017;
   * JAXWS:SPEC:9018; WS4EE:SPEC:6008; WS4EE:SPEC:6028; WS4EE:SPEC:6005;
   *
   * 
   * @test_Strategy: Invoke an RPC method. Verify that the server-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ServerLogicalHandler4 throws a RuntimeException in handleMessage method
   * while processing an outbound message.
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ServerLogicalHandler6.handleMessage().doInbound()
   * ServerLogicalHandler4.handleMessage().doInbound()
   * ServerLogicalHandler5.handleMessage().doInbound()
   * ServerLogicalHandler5.handleMessage().doOutbound()
   * ServerLogicalHandler4.handleMessage().doOutbound() ServerLogicalHandler4
   * Throwing an outbound RuntimeException ServerLogicalHandler5.close()
   * ServerLogicalHandler4.close() ServerLogicalHandler6.close()
   */
  public void ServerLogicalOutboundHandleMessageThrowsRuntimeExceptionTest()
      throws Fault {
    TestUtil.logTrace(
        "ServerLogicalOutboundHandleMessageThrowsRuntimeExceptionTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    }
    if (pass) {
      Handler_Util.clearHandlers(listOfBindings);
      try {
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ServerLogicalOutboundHandleMessageThrowsRuntimeExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg(
              "Expecting RuntimeException wrapped by a WebServiceException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected WebServiceException");
          pass = false;
        } catch (WebServiceException e) {
          TestUtil.logMsg("Did get expected WebServiceException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          if (e instanceof jakarta.xml.ws.soap.SOAPFaultException)
            TestUtil.logMsg("Did get expected nested SOAPFaultException");
          else {
            TestUtil.logErr("Did not get expected nested SOAPFaultException");
            TestUtil.printStackTrace(e);
            pass = false;
          }
          String tmp = "ServerLogicalHandler4.handleMessage throwing an outbound RuntimeException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected nested RuntimeException text");
          else {
            TestUtil
                .logErr("Did not get expected nested RuntimeException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        GetTrackerDataAction gtda = new GetTrackerDataAction();
        TestUtil.logMsg("Get server side result back from endpoint");
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideMsgs = null;
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Server", LOGICAL,
            false, Constants.OUTBOUND, serverSideMsgs)) {
          TestUtil.logErr("Server-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side Callbacks are (correct)");
        }
        gtda = new GetTrackerDataAction();
        TestUtil.logMsg("Get server side throwables back from endpoint");
        gtda.setAction("getArrayThrowables");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideThrowables = port4.getTrackerData(gtda)
            .getResult();
        if (serverSideThrowables.size() >= 1) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          Iterator iterator = serverSideThrowables.iterator();
          while (iterator.hasNext()) {
            TestUtil.logErr((String) iterator.next());
          }
          pass = false;
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
          "ServerLogicalOutboundHandleMessageThrowsRuntimeExceptionTest failed");
  }

  /*
   * @testName: ServerEndpointRemoteRuntimeExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:9016.4; JAXWS:SPEC:9017; JAXWS:SPEC:9018;
   * WS4EE:SPEC:6008; WS4EE:SPEC:6028; WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side
   * handlers callbacks are called. Endpoint throws a RuntimeException that is
   * wrapped by a WebServiceException
   *
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ServerLogicalHandler6.handleMessage().doInbound()
   * ServerLogicalHandler4.handleMessage().doInbound()
   * ServerLogicalHandler5.handleMessage().doInbound()
   * ServerLogicalHandler5.handleFault() ServerLogicalHandler4.handleFault()
   * ServerLogicalHandler6.handleFault() ServerLogicalHandler5.close()
   * ServerLogicalHandler4.close() ServerLogicalHandler6.close()
   * 
   */
  public void ServerEndpointRemoteRuntimeExceptionTest() throws Fault {
    TestUtil.logTrace("ServerEndpointRemoteRuntimeExceptionTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    }
    if (pass) {
      Handler_Util.clearHandlers(listOfBindings);
      try {
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        MyActionType ma = new MyActionType();
        ma.setAction("EndpointRemoteRuntimeExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg(
              "Expecting RuntimeException wrapped by a WebServiceException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected WebServiceException");
          pass = false;
        } catch (WebServiceException e) {
          TestUtil.logMsg("Did get expected WebServiceException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          if (baos.toString().indexOf("RuntimeException") > -1)
            TestUtil.logMsg("Did get expected nested RuntimeException");
          else {
            TestUtil.logErr("Did not get expected nested RuntimeException");
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        GetTrackerDataAction gtda = new GetTrackerDataAction();
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideMsgs = null;
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Server", LOGICAL,
            true, "", serverSideMsgs)) {
          TestUtil.logErr("Server-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side Callbacks are (correct)");
        }
        gtda = new GetTrackerDataAction();
        TestUtil.logMsg("Get server side throwables back from endpoint");
        gtda.setAction("getArrayThrowables");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideThrowables = port4.getTrackerData(gtda)
            .getResult();
        if (serverSideThrowables.size() >= 1) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          Iterator iterator = serverSideThrowables.iterator();
          while (iterator.hasNext()) {
            TestUtil.logErr((String) iterator.next());
          }
          pass = false;
        }

        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();
      } catch (Exception e) {
        TestUtil.logErr("Exception occurred: " + e);
        pass = false;
      }
    }
    if (!pass)
      throw new Fault("ServerEndpointRemoteRuntimeExceptionTest failed");
  }

  /*
   * @testName: ServerEndpointRemoteSOAPFaultExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:9016.3; JAXWS:SPEC:9017; JAXWS:SPEC:9018;
   * WS4EE:SPEC:6008; WS4EE:SPEC:6028; WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side
   * handler callbacks are called. Endpoint throws a SOAPFaultException that is
   * wrapped by a WebServiceException
   *
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ServerLogicalHandler6.handleMessage().doInbound()
   * ServerLogicalHandler4.handleMessage().doInbound()
   * ServerLogicalHandler5.handleMessage().doInbound()
   * ServerLogicalHandler5.handleFault() ServerLogicalHandler4.handleFault()
   * ServerLogicalHandler6.handleFault() ServerLogicalHandler5.close()
   * ServerLogicalHandler4.close() ServerLogicalHandler6.close()
   * 
   */
  public void ServerEndpointRemoteSOAPFaultExceptionTest() throws Fault {
    TestUtil.logTrace("ServerEndpointRemoteSOAPFaultExceptionTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    }
    if (pass) {
      Handler_Util.clearHandlers(listOfBindings);
      try {
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        MyActionType ma = new MyActionType();
        ma.setAction("EndpointRemoteSOAPFaultExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg(
              "Expecting SOAPFaultException wrapped by a WebServiceException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected WebServiceException");
          pass = false;
        } catch (WebServiceException e) {
          TestUtil.logMsg("Did get expected WebServiceException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          if (baos.toString().indexOf("SOAPFaultException") > -1)
            TestUtil.logMsg("Did get expected nested SOAPFaultException");
          else {
            TestUtil.logErr("Did not get expected nested SOAPFaultException");
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        GetTrackerDataAction gtda = new GetTrackerDataAction();
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideMsgs = null;
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Server", LOGICAL,
            true, "", serverSideMsgs)) {
          TestUtil.logErr("Server-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side Callbacks are (correct)");
        }
        gtda = new GetTrackerDataAction();
        TestUtil.logMsg("Get server side throwables back from endpoint");
        gtda.setAction("getArrayThrowables");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideThrowables = port4.getTrackerData(gtda)
            .getResult();
        if (serverSideThrowables.size() >= 1) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          Iterator iterator = serverSideThrowables.iterator();
          while (iterator.hasNext()) {
            TestUtil.logErr((String) iterator.next());
          }
          pass = false;
        }

        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();
      } catch (Exception e) {
        TestUtil.logErr("Exception occurred: " + e);
        pass = false;
      }
    }
    if (!pass)
      throw new Fault("ServerEndpointRemoteSOAPFaultExceptionTest failed");
  }

  /*
   * @testName: ServerLogicalInboundHandleMessageFalseTest
   *
   * @assertion_ids: JAXWS:SPEC:9015.2.1; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005; JAXWS:SPEC:11003; JAXWS:SPEC:10007
   *
   * @test_Strategy: Invoke an RPC method. Verify that the server-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ServerLogicalHandler4 returns false in the handleMessage method while
   * processing an inbound message.
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ServerLogicalHandler6.handleMessage().doInbound()
   * ServerLogicalHandler4.handleMessage().doInbound()
   * ServerLogicalHandler6.handleMessage().doOutbound()
   * ServerLogicalHandler4.close() ServerLogicalHandler6.close()
   */
  public void ServerLogicalInboundHandleMessageFalseTest() throws Fault {
    TestUtil.logTrace("ServerLogicalInboundHandleMessageFalseTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    }
    if (pass) {
      Handler_Util.clearHandlers(listOfBindings);
      try {
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        MyActionType ma = new MyActionType();
        ma.setAction("ServerLogicalInboundHandleMessageFalseTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          port2.doHandlerTest2(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        GetTrackerDataAction gtda = new GetTrackerDataAction();
        TestUtil.logMsg("Get server side result back from endpoint");
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideMsgs = null;
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);
        if (!Handler_Util.VerifyHandleMessageFalseCallBacks("Server", LOGICAL,
            serverSideMsgs, Constants.INBOUND)) {
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
      throw new Fault("ServerLogicalInboundHandleMessageFalseTest failed");
  }

  /*
   * @testName: ServerLogicalOutboundHandleMessageFalseTest
   *
   * @assertion_ids: JAXWS:SPEC:9015.2.2; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the server-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ServerLogicalHandler4 returns false in the handleMessage method while
   * processing an outbound message.
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ServerLogicalHandler6.handleMessage().doInbound()
   * ServerLogicalHandler4.handleMessage().doInbound()
   * ServerLogicalHandler5.handleMessage().doInbound()
   * ServerLogicalHandler5.handleMessage().doOutbound()
   * ServerLogicalHandler4.handleMessage().doOutbound()
   * ServerLogicalHandler5.close() ServerLogicalHandler4.close()
   * ServerLogicalHandler6.close()
   */
  public void ServerLogicalOutboundHandleMessageFalseTest() throws Fault {
    TestUtil.logTrace("ServerLogicalOutboundHandleMessageFalseTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    }
    if (pass) {
      Handler_Util.clearHandlers(listOfBindings);
      try {
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        MyActionType ma = new MyActionType();
        ma.setAction("ServerLogicalOutboundHandleMessageFalseTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          port2.doHandlerTest2(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        GetTrackerDataAction gtda = new GetTrackerDataAction();
        TestUtil.logMsg("Get server side result back from endpoint");
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideMsgs = null;
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);
        if (!Handler_Util.VerifyHandleMessageFalseCallBacks("Server", LOGICAL,
            serverSideMsgs, Constants.OUTBOUND)) {
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
      throw new Fault("ServerLogicalOutboundHandleMessageFalseTest failed");
  }

  /*
   * @testName: ClientLogicalInboundHandleMessageFalseTest
   *
   * @assertion_ids: JAXWS:SPEC:9015.2.2; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ClientLogicalHandler4 returns false in the handleMessage method while
   * processing an inbound message.
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientLogicalHandler5.handleMessage().doOutbound()
   * ClientLogicalHandler4.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleMessage().doInbound()
   * ClientLogicalHandler4.handleMessage().doInbound()
   * ClientLogicalHandler6.close() ClientLogicalHandler4.close()
   * ClientLogicalHandler5.close()
   */
  public void ClientLogicalInboundHandleMessageFalseTest() throws Fault {
    TestUtil.logTrace("ClientLogicalInboundHandleMessageFalseTest");
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
                  + "ClientSOAPHandler5, ClientLogicalHandler5");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler5();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler5();
          handlerList.add(handler);

          if (info.getPortName().equals(PORT_QNAME2)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME2);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler4();
            handlerList.add(h4);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });
      if (!setupPorts()) {
        pass = false;
      } else {
        try {
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg("Getting existing Handlers for Port2");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding2.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientLogicalHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding2.setHandlerChain(handlerList);
        } catch (Exception e) {
          TestUtil.logErr(
              "ERROR: Adding handlers to the binding failed with the following exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
      }

      if (pass) {

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

        MyActionType ma = new MyActionType();
        ma.setAction("ClientLogicalInboundHandleMessageFalseTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          port2.doHandlerTest2(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandleMessageFalseCallBacks("Client", LOGICAL,
            clientSideMsgs, Constants.INBOUND)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Get client side throwables back from Tracker");
        String[] clientSideThrowables = HandlerTracker.getArrayThrowables();
        int len = clientSideThrowables.length;
        if (len > 0) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          for (int i = 0; i <= len - 1; i++) {
            TestUtil.logErr(clientSideThrowables[i]);
            pass = false;
          }
        } else {
          TestUtil.logMsg("There were no Client Handler exceptions");
        }

        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault("ClientLogicalInboundHandleMessageFalseTest failed");
  }

  /*
   * @testName: ClientLogicalOutboundHandleMessageFalseTest
   *
   * @assertion_ids: JAXWS:SPEC:9015.2.1; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ClientLogicalHandler4 returns false in the handleMessage method while
   * processing an outbound message.
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientLogicalHandler5.handleMessage().doOutbound()
   * ClientLogicalHandler4.handleMessage().doOutbound()
   * ClientLogicalHandler5.handleMessage().doInbound()
   * ClientLogicalHandler4.close() ClientLogicalHandler5.close()
   */
  public void ClientLogicalOutboundHandleMessageFalseTest() throws Fault {
    TestUtil.logTrace("ClientLogicalOutboundHandleMessageFalseTest");
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
                  + "ClientSOAPHandler5, ClientLogicalHandler5");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler5();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler5();
          handlerList.add(handler);

          if (info.getPortName().equals(PORT_QNAME2)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME2);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler4();
            handlerList.add(h4);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });
      if (!setupPorts()) {
        pass = false;
      } else {
        try {
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg("Getting existing Handlers for Port2");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding2.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientLogicalHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding2.setHandlerChain(handlerList);
        } catch (Exception e) {
          TestUtil.logErr(
              "ERROR: Adding handlers to the binding failed with the following exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
      }

      if (pass) {

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

        MyActionType ma = new MyActionType();
        ma.setAction("ClientLogicalOutboundHandleMessageFalseTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          port2.doHandlerTest2(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandleMessageFalseCallBacks("Client", LOGICAL,
            clientSideMsgs, Constants.OUTBOUND)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Get client side throwables back from Tracker");
        String[] clientSideThrowables = HandlerTracker.getArrayThrowables();
        int len = clientSideThrowables.length;
        if (len > 0) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          for (int i = 0; i <= len - 1; i++) {
            TestUtil.logErr(clientSideThrowables[i]);
            pass = false;
          }
        } else {
          TestUtil.logMsg("There were no Client Handler exceptions");
        }

        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault("ClientLogicalOutboundHandleMessageFalseTest failed");
  }

  /*
   * @testName: ClientLogicalOutboundHandleFaultFalseTest
   *
   * @assertion_ids: JAXWS:SPEC:9016.2; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleFault callbacks are called by the JAXWS RUNTIME.
   * ClientLogicalHandler6 throws a SOAPFaultException ClientLogicalHandler4
   * returns a false for handleFault method while processing an outbound
   * message. ------------------------------------------------------- This is
   * the expected order -------------------------------------------------------
   * ClientLogicalHandler5.handleMessage().doOutbound()
   * ClientLogicalHandler4.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleMessage().doOutbound() ClientLogicalHandler6
   * Throwing an outbound SOAPFaultException ClientLogicalHandler4.handleFault()
   * ClientLogicalHandler6.close() ClientLogicalHandler4.close()
   * ClientLogicalHandler5.close()
   * 
   */
  public void ClientLogicalOutboundHandleFaultFalseTest() throws Fault {
    TestUtil.logTrace("ClientLogicalOutboundHandleFaultFalseTest");
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
                  + "ClientSOAPHandler5, ClientLogicalHandler5");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler5();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler5();
          handlerList.add(handler);

          if (info.getPortName().equals(PORT_QNAME2)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME2);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler4();
            handlerList.add(h4);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });
      if (!setupPorts()) {
        pass = false;
      } else {
        try {
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg("Getting existing Handlers for Port2");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding2.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientLogicalHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding2.setHandlerChain(handlerList);
        } catch (Exception e) {
          TestUtil.logErr(
              "ERROR: Adding handlers to the binding failed with the following exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
      }

      if (pass) {

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

        MyActionType ma = new MyActionType();
        ma.setAction("ClientLogicalOutboundHandleFaultFalseTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting RuntimeException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected RuntimeException");
          pass = false;
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did get expected RuntimeException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientLogicalHandler6.handleMessage throws SOAPFaultException for ClientLogicalOutboundHandleFaultFalseTest";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected RuntimeException text");
          else {
            TestUtil.logErr("Did not get expected RuntimeException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandleFaultFalseCallBacks("Client", LOGICAL,
            clientSideMsgs, Constants.OUTBOUND)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Get client side throwables back from Tracker");
        String[] clientSideThrowables = HandlerTracker.getArrayThrowables();
        int len = clientSideThrowables.length;
        if (len > 0) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          for (int i = 0; i <= len - 1; i++) {
            TestUtil.logErr(clientSideThrowables[i]);
            pass = false;
          }
        } else {
          TestUtil.logMsg("There were no Client Handler exceptions");
        }

        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault("ClientLogicalOutboundHandleFaultFalseTest failed");
  }

  /*
   * @testName: ServerLogicalInboundHandleFaultFalseTest
   *
   * @assertion_ids: JAXWS:SPEC:9016.2; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the server-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ServerLogicalHandler5 throws a SOAPFaultException ServerLogicalHandler4
   * returns a false for handleFault method while processing an outbound
   * message. ------------------------------------------------------- This is
   * the expected order -------------------------------------------------------
   * ServerLogicalHandler6.handleMessage().doInbound()
   * ServerLogicalHandler4.handleMessage().doInbound()
   * ServerLogicalHandler5.handleMessage().doInbound() ServerLogicalHandler5
   * Throwing an inbound SOAPFaultException ServerLogicalHandler4.handleFault()
   * ServerLogicalHandler5.close() ServerLogicalHandler4.close()
   * ServerLogicalHandler6.close()
   */
  public void ServerLogicalInboundHandleFaultFalseTest() throws Fault {
    TestUtil.logTrace("ServerLogicalInboundHandleFaultFalseTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    }
    if (pass) {
      Handler_Util.clearHandlers(listOfBindings);
      try {
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        MyActionType ma = new MyActionType();
        ma.setAction("ServerLogicalInboundHandleFaultFalseTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting RuntimeException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected RuntimeException");
          pass = false;
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did get expected RuntimeException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ServerLogicalHandler5.handleMessage throws SOAPFaultException for ServerLogicalInboundHandleFaultFalseTest";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected RuntimeException text");
          else {
            TestUtil.logErr("Did not get expected RuntimeException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        GetTrackerDataAction gtda = new GetTrackerDataAction();
        TestUtil.logMsg("Get server side result back from endpoint");
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideMsgs = null;
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);
        if (!Handler_Util.VerifyHandleFaultFalseCallBacks("Server", LOGICAL,
            serverSideMsgs, Constants.INBOUND)) {
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
      throw new Fault("ServerLogicalInboundHandleFaultFalseTest failed");
  }

  /*
   * @testName: ClientLogicalOutboundHandleFaultThrowsRuntimeExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:9016.4; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ClientLogicalHandler6 throws a SOAPFaultException ClientLogicalHandler4
   * throws a RuntimeException in handleFault method processing an inbound
   * message. ------------------------------------------------------- This is
   * the expected order -------------------------------------------------------
   * ClientLogicalHandler5.handleMessage().doOutbound()
   * ClientLogicalHandler4.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleMessage().doOutbound() ClientLogicalHandler6
   * Throwing an outbound SOAPFaultException ClientLogicalHandler4.handleFault()
   * ClientLogicalHandler4 Throwing an inbound RuntimeException
   * ClientLogicalHandler6.close() ClientLogicalHandler4.close()
   * ClientLogicalHandler5.close()
   */
  public void ClientLogicalOutboundHandleFaultThrowsRuntimeExceptionTest()
      throws Fault {
    TestUtil
        .logTrace("ClientLogicalOutboundHandleFaultThrowsRuntimeExceptionTest");
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
                  + "ClientSOAPHandler5, ClientLogicalHandler5");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler5();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler5();
          handlerList.add(handler);

          if (info.getPortName().equals(PORT_QNAME2)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME2);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler4();
            handlerList.add(h4);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });
      if (!setupPorts()) {
        pass = false;
      } else {
        try {
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg("Getting existing Handlers for Port2");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding2.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientLogicalHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding2.setHandlerChain(handlerList);
        } catch (Exception e) {
          TestUtil.logErr(
              "ERROR: Adding handlers to the binding failed with the following exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
      }

      if (pass) {

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ClientLogicalOutboundHandleFaultThrowsRuntimeExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);

        try {
          TestUtil.logMsg("Expecting RuntimeException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected RuntimeException");
          pass = false;
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did get expected RuntimeException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientLogicalHandler4.handleFault throwing an inbound RuntimeException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected RuntimeException text");
          else {
            TestUtil.logErr("Did not get expected RuntimeException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandleFaultRuntimeExceptionCallBacks("Client",
            LOGICAL, clientSideMsgs, Constants.OUTBOUND)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Get client side throwables back from Tracker");
        String[] clientSideThrowables = HandlerTracker.getArrayThrowables();
        int len = clientSideThrowables.length;
        if (len > 0) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          for (int i = 0; i <= len - 1; i++) {
            TestUtil.logErr(clientSideThrowables[i]);
            pass = false;
          }
        } else {
          TestUtil.logMsg("There were no Client Handler exceptions");
        }

        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "ClientLogicalOutboundHandleFaultThrowsRuntimeExceptionTest failed");
  }

  /*
   * @testName: ServerLogicalInboundHandleFaultThrowsRuntimeExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:9016.4; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the server-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ServerLogicalHandler5 throws a SOAPFaultException ServerLogicalHandler4
   * throws a RuntimeException in the handleFault method processing an outbound
   * message. ------------------------------------------------------- This is
   * the expected order -------------------------------------------------------
   * ServerLogicalHandler6.handleMessage().doInbound()
   * ServerLogicalHandler4.handleMessage().doInbound()
   * ServerLogicalHandler5.handleMessage().doInbound() ServerLogicalHandler5
   * Throwing an inbound SOAPFaultException ServerLogicalHandler4.handleFault()
   * ServerLogicalHandler4 Throwing an outbound RuntimeException
   * ServerLogicalHandler5.close() ServerLogicalHandler4.close()
   * ServerLogicalHandler6.close()
   */
  public void ServerLogicalInboundHandleFaultThrowsRuntimeExceptionTest()
      throws Fault {
    TestUtil
        .logTrace("ServerLogicalInboundHandleFaultThrowsRuntimeExceptionTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    }
    if (pass) {
      Handler_Util.clearHandlers(listOfBindings);
      try {
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ServerLogicalInboundHandleFaultThrowsRuntimeExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting RuntimeException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected RuntimeException");
          pass = false;
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did get expected RuntimeException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ServerLogicalHandler4.handleFault throwing an outbound RuntimeException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected RuntimeException text");
          else {
            TestUtil.logErr("Did not get expected RuntimeException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        GetTrackerDataAction gtda = new GetTrackerDataAction();
        TestUtil.logMsg("Get server side result back from endpoint");
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideMsgs = null;
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);
        if (!Handler_Util.VerifyHandleFaultRuntimeExceptionCallBacks("Server",
            LOGICAL, serverSideMsgs, Constants.INBOUND)) {
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
          "ServerLogicalInboundHandleFaultThrowsRuntimeExceptionTest failed");
  }

  /*
   * @testName: ClientLogicalOutboundHandleFaultThrowsSOAPFaultExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:9016.3; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ClientLogicalHandler6 throws a SOAPFaultException ClientLogicalHandler4
   * throws a SOAPFaultException in the handleFault method processing an inbound
   * message. ------------------------------------------------------- This is
   * the expected order -------------------------------------------------------
   * ClientLogicalHandler5.handleMessage().doOutbound()
   * ClientLogicalHandler4.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleMessage().doOutbound() ClientLogicalHandler6
   * Throwing an outbound SOAPFaultException ClientLogicalHandler4.handleFault()
   * ClientLogicalHandler4 Throwing an inbound SOAPFaultException
   * ClientLogicalHandler6.close() ClientLogicalHandler4.close()
   * ClientLogicalHandler5.close()
   */
  public void ClientLogicalOutboundHandleFaultThrowsSOAPFaultExceptionTest()
      throws Fault {
    TestUtil.logTrace(
        "ClientLogicalOutboundHandleFaultThrowsSOAPFaultExceptionTest");
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
                  + "ClientSOAPHandler5, ClientLogicalHandler5");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler5();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler5();
          handlerList.add(handler);

          if (info.getPortName().equals(PORT_QNAME2)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME2);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler4();
            handlerList.add(h4);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });
      if (!setupPorts()) {
        pass = false;
      } else {
        try {
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg("Getting existing Handlers for Port2");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding2.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientLogicalHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding2.setHandlerChain(handlerList);
        } catch (Exception e) {
          TestUtil.logErr(
              "ERROR: Adding handlers to the binding failed with the following exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
      }

      if (pass) {

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ClientLogicalOutboundHandleFaultThrowsSOAPFaultExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);

        try {
          TestUtil.logMsg(
              "Expecting WebServiceException that wraps a SOAPFaultException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected WebServiceException");
          pass = false;
        } catch (WebServiceException e) {
          TestUtil.logMsg("Did get expected WebServiceException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "SOAPFaultException: ClientLogicalHandler4.handleFault throwing an inbound SOAPFaultException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected WebServiceException text");
          else {
            TestUtil.logErr("Did not get expected WebServiceException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandleFaultSOAPFaultExceptionCallBacks("Client",
            LOGICAL, clientSideMsgs, Constants.OUTBOUND)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Get client side throwables back from Tracker");
        String[] clientSideThrowables = HandlerTracker.getArrayThrowables();
        int len = clientSideThrowables.length;
        if (len > 0) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          for (int i = 0; i <= len - 1; i++) {
            TestUtil.logErr(clientSideThrowables[i]);
            pass = false;
          }
        } else {
          TestUtil.logMsg("There were no Client Handler exceptions");
        }

        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "ClientLogicalOutboundHandleFaultThrowsSOAPFaultExceptionTest failed");
  }

  /*
   * @testName: ServerLogicalInboundHandleFaultThrowsSOAPFaultExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:9016.3; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the server-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ServerLogicalHandler5 throws a SOAPFaultException ServerLogicalHandler4
   * throws a SOAPFaultException in handleFailt method processing an outbound
   * message. ------------------------------------------------------- This is
   * the expected order -------------------------------------------------------
   * ServerLogicalHandler6.handleMessage().doInbound()
   * ServerLogicalHandler4.handleMessage().doInbound()
   * ServerLogicalHandler5.handleMessage().doInbound() ServerLogicalHandler5
   * Throwing an inbound SOAPFaultException ServerLogicalHandler4.handleFault()
   * ServerLogicalHandler4 Throwing an outbound SOAPFaultException
   * ServerLogicalHandler5.close() ServerLogicalHandler4.close()
   * ServerLogicalHandler6.close()
   */
  public void ServerLogicalInboundHandleFaultThrowsSOAPFaultExceptionTest()
      throws Fault {
    TestUtil.logTrace(
        "ServerLogicalInboundHandleFaultThrowsSOAPFaultExceptionTest");
    boolean pass = true;
    if (!setupPorts()) {
      pass = false;
    }
    if (pass) {
      Handler_Util.clearHandlers(listOfBindings);
      try {
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ServerLogicalInboundHandleFaultThrowsSOAPFaultExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting SOAPFaultException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected SOAPFaultException");
          pass = false;
        } catch (SOAPFaultException e) {
          TestUtil.logMsg("Did get expected SOAPFaultException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ServerLogicalHandler4.handleFault throwing an outbound SOAPFaultException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected SOAPFaultException text");
          else {
            TestUtil.logErr("Did not get expected SOAPFaultException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        GetTrackerDataAction gtda = new GetTrackerDataAction();
        TestUtil.logMsg("Get server side result back from endpoint");
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideMsgs = null;
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);
        if (!Handler_Util.VerifyHandleFaultSOAPFaultExceptionCallBacks("Server",
            LOGICAL, serverSideMsgs, Constants.INBOUND)) {
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
          "ServerLogicalInboundHandleFaultThrowsSOAPFaultExceptionTest failed");
  }

  /*
   * @testName: ServerLogicalInboundHandlerThrowsSOAPFaultToClientHandlersTest
   *
   * @assertion_ids: JAXWS:SPEC:9016; JAXWS:SPEC:9016.2;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side handler
   * callbacks are called by the JAXWS RUNTIME. Server handler throws a
   * SOAPFaultException while processing an inbound message and Client should
   * properly process exception.
   *
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientLogicalHandler5.handleMessage().doOutbound()
   * ClientLogicalHandler4.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleFault() ClientLogicalHandler6 received
   * SOAPFault from Inbound ServerLogicalHandler6 ClientLogicalHandler6.close()
   * ClientLogicalHandler4.close() ClientLogicalHandler5.close()
   *
   * ServerLogicalHandler6.handleMessage().doInbound() ServerLogicalHandler6
   * Throwing an inbound SOAPFaultException ServerLogicalHandler6.close()
   *
   */
  public void ServerLogicalInboundHandlerThrowsSOAPFaultToClientHandlersTest()
      throws Fault {
    TestUtil.logTrace(
        "ServerLogicalInboundHandlerThrowsSOAPFaultToClientHandlersTest");
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
                  + "ClientSOAPHandler5, ClientLogicalHandler5");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler5();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler5();
          handlerList.add(handler);
          if (info.getPortName().equals(PORT_QNAME2)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME2);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler4();
            handlerList.add(h4);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }
      });

      if (!setupPorts()) {
        pass = false;
      } else {
        try {
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg("Getting existing Handlers for Port2");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding2.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientLogicalHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding2.setHandlerChain(handlerList);
        } catch (Exception e) {
          TestUtil.logErr(
              "ERROR: Adding handlers to the binding failed with the following exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
      }

      if (pass) {
        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ServerLogicalInboundHandlerThrowsSOAPFaultToClientHandlersTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);

        try {
          TestUtil.logMsg("Expecting RuntimeException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected RuntimeException");
          pass = false;
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did get expected RuntimeException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ServerLogicalHandler6.handleMessage throws SOAPFaultException for ServerLogicalInboundHandlerThrowsSOAPFaultToClientHandlersTest";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected RuntimeException text");
          else {
            TestUtil.logErr("Did not get expected RuntimeException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyServerToClientHandlerExceptionCallBacks(
            "Client", LOGICAL, Constants.OUTBOUND, clientSideMsgs)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Get client side throwables back from Tracker");
        String[] clientSideThrowables = HandlerTracker.getArrayThrowables();
        int len = clientSideThrowables.length;
        if (len > 0) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          for (int i = 0; i <= len - 1; i++) {
            TestUtil.logErr(clientSideThrowables[i]);
            pass = false;
          }
        } else {
          TestUtil.logMsg("There were no Client Handler exceptions");
        }

        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();
        GetTrackerDataAction gtda = new GetTrackerDataAction();

        TestUtil.logMsg("Get server side result back from endpoint");
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideMsgs = null;
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);
        if (!Handler_Util.VerifyServerToClientHandlerExceptionCallBacks(
            "Server", LOGICAL, Constants.INBOUND, serverSideMsgs)) {
          TestUtil.logErr("Server-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "ServerLogicalInboundHandlerThrowsSOAPFaultToClientHandlersTest failed");
  }

  /*
   * @testName: ServerLogicalOutboundHandlerThrowsSOAPFaultToClientHandlersTest
   *
   * @assertion_ids: JAXWS:SPEC:9016; JAXWS:SPEC:9016.2;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side handler
   * callbacks are called by the JAXWS RUNTIME. Server handler throws a
   * SOAPFaultException while processing an inbound message and Client should
   * properly process exception.
   *
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientLogicalHandler5.handleMessage().doOutbound()
   * ClientLogicalHandler4.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleFault() ClientLogicalHandler6 received
   * SOAPFault from Outbound ServerLogicalHandler6 ClientLogicalHandler6.close()
   * ClientLogicalHandler4.close() ClientLogicalHandler5.close()
   *
   * ServerLogicalHandler6.handleMessage().doInbound()
   * ServerLogicalHandler4.handleMessage().doInbound()
   * ServerLogicalHandler5.handleMessage().doInbound()
   * ServerLogicalHandler5.handleMessage().doOutbound()
   * ServerLogicalHandler4.handleMessage().doOutbound()
   * ServerLogicalHandler6.handleMessage().doOutbound() ServerLogicalHandler6
   * Throwing an inbound SOAPFaultException ServerLogicalHandler6.close()
   *
   */
  public void ServerLogicalOutboundHandlerThrowsSOAPFaultToClientHandlersTest()
      throws Fault {
    TestUtil.logTrace(
        "ServerLogicalOutboundHandlerThrowsSOAPFaultToClientHandlersTest");
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
                  + "ClientSOAPHandler5, ClientLogicalHandler5");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler5();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler5();
          handlerList.add(handler);
          if (info.getPortName().equals(PORT_QNAME2)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME2);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler4();
            handlerList.add(h4);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }
      });

      if (!setupPorts()) {
        pass = false;
      } else {
        try {
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg("Getting existing Handlers for Port2");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding2.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientLogicalHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding2.setHandlerChain(handlerList);
        } catch (Exception e) {
          TestUtil.logErr(
              "ERROR: Adding handlers to the binding failed with the following exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
      }

      if (pass) {
        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ServerLogicalOutboundHandlerThrowsSOAPFaultToClientHandlersTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);

        try {
          TestUtil.logMsg("Expecting RuntimeException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected RuntimeException");
          pass = false;
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did get expected RuntimeException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ServerLogicalHandler6.handleMessage throws SOAPFaultException for ServerLogicalOutboundHandlerThrowsSOAPFaultToClientHandlersTest";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected RuntimeException text");
          else {
            TestUtil.logErr("Did not get expected RuntimeException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyServerToClientHandlerExceptionCallBacks(
            "Client", LOGICAL, Constants.INBOUND, clientSideMsgs)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Get client side throwables back from Tracker");
        String[] clientSideThrowables = HandlerTracker.getArrayThrowables();
        int len = clientSideThrowables.length;
        if (len > 0) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          for (int i = 0; i <= len - 1; i++) {
            TestUtil.logErr(clientSideThrowables[i]);
            pass = false;
          }
        } else {
          TestUtil.logMsg("There were no Client Handler exceptions");
        }

        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();
        GetTrackerDataAction gtda = new GetTrackerDataAction();

        TestUtil.logMsg("Get server side result back from endpoint");
        gtda.setAction("getArrayMessages1");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        List<String> serverSideMsgs = null;
        try {
          serverSideMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        serverSideMsgs = JAXWS_Util.getMessagesStartingFrom(serverSideMsgs,
            Constants.INBOUND);
        if (!Handler_Util.VerifyServerToClientHandlerExceptionCallBacks(
            "Server", LOGICAL, Constants.OUTBOUND, serverSideMsgs)) {
          TestUtil.logErr("Server-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "ServerLogicalOutboundHandlerThrowsSOAPFaultToClientHandlersTest failed");
  }

  /*
   * @testName: ClientLogicalInboundHandleMessageThrowsWebServiceExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:4021;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ClientLogicalHandler4 throws a WebServiceException in handleMessage method
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientLogicalHandler5.handleMessage().doOutbound()
   * ClientLogicalHandler4.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleMessage().doInbound()
   * ClientLogicalHandler4.handleMessage().doInbound() ClientLogicalHandler4
   * Throwing an inbound WebServiceException ClientLogicalHandler6.close()
   * ClientLogicalHandler4.close() ClientLogicalHandler5.close()
   */
  public void ClientLogicalInboundHandleMessageThrowsWebServiceExceptionTest()
      throws Fault {
    TestUtil.logTrace(
        "ClientLogicalInboundHandleMessageThrowsWebServiceExceptionTest");
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
                  + "ClientSOAPHandler5, ClientLogicalHandler5");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler5();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler5();
          handlerList.add(handler);

          if (info.getPortName().equals(PORT_QNAME2)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME2);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler4();
            handlerList.add(h4);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });
      if (!setupPorts()) {
        pass = false;
      } else {
        try {
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg("Getting existing Handlers for Port2");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding2.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientLogicalHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding2.setHandlerChain(handlerList);
        } catch (Exception e) {
          TestUtil.logErr(
              "ERROR: Adding handlers to the binding failed with the following exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
      }

      if (pass) {

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ClientLogicalInboundHandleMessageThrowsWebServiceExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting WebServiceException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected WebServiceException");
          pass = false;
        } catch (WebServiceException e) {
          TestUtil.logMsg("Did get expected WebServiceException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientLogicalHandler4.handleMessage throwing an inbound WebServiceException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected WebServiceException text");
          else {
            TestUtil.logErr("Did not get expected WebServiceException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Client", LOGICAL,
            false, Constants.INBOUND, clientSideMsgs)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Get client side throwables back from Tracker");
        String[] clientSideThrowables = HandlerTracker.getArrayThrowables();
        int len = clientSideThrowables.length;
        if (len > 0) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          for (int i = 0; i <= len - 1; i++) {
            TestUtil.logErr(clientSideThrowables[i]);
            pass = false;
          }
        } else {
          TestUtil.logMsg("There were no Client Handler exceptions");
        }

        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "ClientLogicalInboundHandleMessageThrowsWebServiceExceptionTest failed");
  }

  /*
   * @testName: ClientLogicalOutboundHandleMessageThrowsWebServiceExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:4021;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ClientLogicalHandler4 throws a WebServiceException in handleMessage method
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientLogicalHandler5.handleMessage().doOutbound()
   * ClientLogicalHandler4.handleMessage().doOutbound() ClientLogicalHandler4
   * Throwing an outbound WebServiceException ClientLogicalHandler4.close()
   * ClientLogicalHandler5.close()
   */
  public void ClientLogicalOutboundHandleMessageThrowsWebServiceExceptionTest()
      throws Fault {
    TestUtil.logTrace(
        "ClientLogicalOutboundHandleMessageThrowsWebServiceExceptionTest");
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
                  + "ClientSOAPHandler5, ClientLogicalHandler5");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler5();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler5();
          handlerList.add(handler);

          if (info.getPortName().equals(PORT_QNAME2)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME2);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler4();
            handlerList.add(h4);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });
      if (!setupPorts()) {
        pass = false;
      } else {
        try {
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg("Getting existing Handlers for Port2");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding2.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientLogicalHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding2.setHandlerChain(handlerList);
        } catch (Exception e) {
          TestUtil.logErr(
              "ERROR: Adding handlers to the binding failed with the following exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
      }

      if (pass) {

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ClientLogicalOutboundHandleMessageThrowsWebServiceExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting WebServiceException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected WebServiceException");
          pass = false;
        } catch (WebServiceException e) {
          TestUtil.logMsg("Did get expected WebServiceException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientLogicalHandler4.handleMessage throwing an outbound WebServiceException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected WebServiceException text");
          else {
            TestUtil.logErr("Did not get expected WebServiceException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Client", LOGICAL,
            false, Constants.OUTBOUND, clientSideMsgs)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Get client side throwables back from Tracker");
        String[] clientSideThrowables = HandlerTracker.getArrayThrowables();
        int len = clientSideThrowables.length;
        if (len > 0) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          for (int i = 0; i <= len - 1; i++) {
            TestUtil.logErr(clientSideThrowables[i]);
            pass = false;
          }
        } else {
          TestUtil.logMsg("There were no Client Handler exceptions");
        }

        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "ClientLogicalOutboundHandleMessageThrowsWebServiceExceptionTest failed");
  }

  /*
   * @testName: ClientLogicalInboundHandleMessageThrowsProtocolExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:4021;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ClientLogicalHandler4 throws a ProtocolException in handleMessage method
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientLogicalHandler5.handleMessage().doOutbound()
   * ClientLogicalHandler4.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleMessage().doOutbound()
   * ClientLogicalHandler6.handleMessage().doInbound()
   * ClientLogicalHandler4.handleMessage().doInbound() ClientLogicalHandler4
   * Throwing an inbound ProtocolException ClientLogicalHandler6.close()
   * ClientLogicalHandler4.close() ClientLogicalHandler5.close()
   */
  public void ClientLogicalInboundHandleMessageThrowsProtocolExceptionTest()
      throws Fault {
    TestUtil.logTrace(
        "ClientLogicalInboundHandleMessageThrowsProtocolExceptionTest");
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
                  + "ClientSOAPHandler5, ClientLogicalHandler5");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler5();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler5();
          handlerList.add(handler);

          if (info.getPortName().equals(PORT_QNAME2)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME2);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler4();
            handlerList.add(h4);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });
      if (!setupPorts()) {
        pass = false;
      } else {
        try {
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg("Getting existing Handlers for Port2");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding2.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientLogicalHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding2.setHandlerChain(handlerList);
        } catch (Exception e) {
          TestUtil.logErr(
              "ERROR: Adding handlers to the binding failed with the following exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
      }

      if (pass) {

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ClientLogicalInboundHandleMessageThrowsProtocolExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting ProtocolException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected ProtocolException");
          pass = false;
        } catch (ProtocolException e) {
          TestUtil.logMsg("Did get expected ProtocolException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientLogicalHandler4.handleMessage throwing an inbound ProtocolException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected ProtocolException text");
          else {
            TestUtil.logErr("Did not get expected ProtocolException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Client", LOGICAL,
            false, Constants.INBOUND, clientSideMsgs)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Get client side throwables back from Tracker");
        String[] clientSideThrowables = HandlerTracker.getArrayThrowables();
        int len = clientSideThrowables.length;
        if (len > 0) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          for (int i = 0; i <= len - 1; i++) {
            TestUtil.logErr(clientSideThrowables[i]);
            pass = false;
          }
        } else {
          TestUtil.logMsg("There were no Client Handler exceptions");
        }

        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "ClientLogicalInboundHandleMessageThrowsProtocolExceptionTest failed");
  }

  /*
   * @testName: ClientLogicalOutboundHandleMessageThrowsProtocolExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:4021;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME.
   * ClientLogicalHandler4 throws a ProtocolException in handleMessage method
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientLogicalHandler5.handleMessage().doOutbound()
   * ClientLogicalHandler4.handleMessage().doOutbound() ClientLogicalHandler4
   * Throwing an outbound ProtocolException ClientLogicalHandler4.close()
   * ClientLogicalHandler5.close()
   */
  public void ClientLogicalOutboundHandleMessageThrowsProtocolExceptionTest()
      throws Fault {
    TestUtil.logTrace(
        "ClientLogicalOutboundHandleMessageThrowsProtocolExceptionTest");
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
                  + "ClientSOAPHandler5, ClientLogicalHandler5");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler5();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler5 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler5();
          handlerList.add(handler);

          if (info.getPortName().equals(PORT_QNAME2)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME2);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientLogicalHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler4();
            handlerList.add(h4);
          }
          TestUtil.logMsg("HandlerChainList=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          return handlerList;
        }

      });
      if (!setupPorts()) {
        pass = false;
      } else {
        try {
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg("Getting existing Handlers for Port2");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding2.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientLogicalHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientLogicalHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding2.setHandlerChain(handlerList);
        } catch (Exception e) {
          TestUtil.logErr(
              "ERROR: Adding handlers to the binding failed with the following exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
      }

      if (pass) {

        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ClientLogicalOutboundHandleMessageThrowsProtocolExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting ProtocolException");
          port2.doHandlerTest2(ma);
          TestUtil.logErr("Did not get expected ProtocolException");
          pass = false;
        } catch (ProtocolException e) {
          TestUtil.logMsg("Did get expected ProtocolException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientLogicalHandler4.handleMessage throwing an outbound ProtocolException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected ProtocolException text");
          else {
            TestUtil.logErr("Did not get expected ProtocolException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Client", LOGICAL,
            false, Constants.OUTBOUND, clientSideMsgs)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg("Get client side throwables back from Tracker");
        String[] clientSideThrowables = HandlerTracker.getArrayThrowables();
        int len = clientSideThrowables.length;
        if (len > 0) {
          TestUtil
              .logErr("There were exceptions thrown in the Client Handlers");
          for (int i = 0; i <= len - 1; i++) {
            TestUtil.logErr(clientSideThrowables[i]);
            pass = false;
          }
        } else {
          TestUtil.logMsg("There were no Client Handler exceptions");
        }

        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging client-side tracker data");
        HandlerTracker.purge();
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "ClientLogicalOutboundHandleMessageThrowsProtocolExceptionTest failed");
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

}
