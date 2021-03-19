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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws_handler_soap.SOAPHandler;

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

  private static final String ENDPOINT3_URL = "dlhandlerservice.endpoint.3";

  private static final String ENDPOINT4_URL = "dlhandlerservice.endpoint.4";

  private String url1 = null;

  private String url3 = null;

  private String url4 = null;

  private URL wsdlurl = null;

  // service and port information
  private static final String NAMESPACEURI = "http://dlhandlerservice.org/wsdl";

  private static final String SERVICE_NAME = "DLHandlerService";

  private static final String PORT_NAME1 = "HelloPort";

  private static final String PORT_NAME3 = "Hello3Port";

  private static final String PORT_NAME4 = "GetTrackerDataPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME1 = new QName(NAMESPACEURI, PORT_NAME1);

  private QName PORT_QNAME3 = new QName(NAMESPACEURI, PORT_NAME3);

  private static final Class SERVICE_CLASS = com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.DLHandlerService.class;

  private static final String THEBINDINGPROTOCOL = jakarta.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING;

  private static final String SOAP = "SOAP";

  private static final String TEST_TYPE = SOAP + "Test";

  private Handler handler = null;

  Hello port1 = null;

  Hello3 port3 = null;

  GetTrackerData port4 = null;

  static DLHandlerService service = null;

  BindingProvider bp1 = null;

  BindingProvider bp3 = null;

  BindingProvider bp4 = null;

  Binding binding1 = null;

  Binding binding3 = null;

  Binding binding4 = null;

  List<Binding> listOfBindings = new ArrayList<Binding>();

  List<Handler> port1HandlerChain = null;

  List<Handler> port3HandlerChain = null;

  List<Handler> port4HandlerChain = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT1_URL);
    url1 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(ENDPOINT3_URL);
    url3 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(ENDPOINT4_URL);
    url4 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint1 URL: " + url1);
    TestUtil.logMsg("Service Endpoint3 URL: " + url3);
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
    JAXWS_Util.setTargetEndpointAddress(port3, url3);
    JAXWS_Util.setTargetEndpointAddress(port4, url4);
  }

  private void getPortsJavaEE() throws Exception {
    TestUtil.logMsg("Obtaining service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    getPorts();
    getTargetEndpointAddress(port1, port3, port4);
  }

  private void getTargetEndpointAddress(Object port1, Object port3,
      Object port4) throws Exception {
    TestUtil.logMsg("Get Target Endpoint Address for port1=" + port1);
    String url1 = JAXWS_Util.getTargetEndpointAddress(port1);
    TestUtil.logMsg("Target Endpoint Address=" + url1);
    TestUtil.logMsg("Get Target Endpoint Address for port3=" + port3);
    String url3 = JAXWS_Util.getTargetEndpointAddress(port3);
    TestUtil.logMsg("Target Endpoint Address=" + url3);
    TestUtil.logMsg("Get Target Endpoint Address for port4=" + port4);
    String url4 = JAXWS_Util.getTargetEndpointAddress(port4);
    TestUtil.logMsg("Target Endpoint Address=" + url4);
  }

  private void getPorts() throws Exception {
    TestUtil.logTrace("entering getPorts");

    TestUtil.logMsg("Get port 1 = " + PORT_NAME1);
    port1 = (Hello) service.getPort(Hello.class);
    TestUtil.logMsg("port1=" + port1);

    TestUtil.logMsg("Get port 3 = " + PORT_NAME3);
    port3 = (Hello3) service.getPort(Hello3.class);
    TestUtil.logMsg("port3=" + port3);

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

    TestUtil.logMsg("Get binding for port 3 = " + PORT_NAME3);
    bp3 = (BindingProvider) port3;
    binding3 = bp3.getBinding();
    port3HandlerChain = binding3.getHandlerChain();
    TestUtil.logMsg("Port3 HandlerChain=" + port3HandlerChain);
    TestUtil.logMsg("Port3 HandlerChain size = " + port3HandlerChain.size());

    TestUtil.logMsg("------------------------------------------------------");

    TestUtil.logMsg("Get binding for port 4 = " + PORT_NAME4);
    bp4 = (BindingProvider) port4;
    binding4 = bp4.getBinding();
    port4HandlerChain = binding4.getHandlerChain();
    TestUtil.logMsg("Port4 HandlerChain=" + port4HandlerChain);
    TestUtil.logMsg("Port4 HandlerChain size = " + port4HandlerChain.size());

    listOfBindings.add(binding1);
    listOfBindings.add(binding3);
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
   * harness.log.traceflag;
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
   * @testName: ClientSOAPHandlerTest
   *
   * @assertion_ids: JAXWS:JAVADOC:87; JAXWS:JAVADOC:88; JAXWS:JAVADOC:89;
   * JAXWS:JAVADOC:90; JAXWS:JAVADOC:99; JAXWS:SPEC:9007; JAXWS:SPEC:9012;
   * JAXWS:SPEC:9014; JAXWS:SPEC:9015.1; JAXWS:SPEC:9018; WS4EE:SPEC:6010;
   * WS4EE:SPEC:6013; WS4EE:SPEC:6015.1; WS4EE:SPEC:6015.2; WS4EE:SPEC:6015.3;
   * WS4EE:SPEC:6008; WS4EE:SPEC:6028; WS4EE:SPEC:6005; WS4EE:SPEC:6051;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the client-side soap
   * message handler callbacks are called.
   *
   */
  public void ClientSOAPHandlerTest() throws Fault {
    TestUtil.logTrace("ClientSOAPHandlerTest");
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
        ma.setAction("ClientSOAPTest");
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
        TestUtil.logMsg("Verify handleMessage()/init() callbacks");
        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandlerCallBacks("Client", SOAP,
            clientSideMsgs)) {
          TestUtil.logErr("Client-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side Callbacks are (correct)");
        }
        TestUtil.logMsg(
            "Verifying callbacks where SOAPHandlers were called after SOAPHandlers on Client-Side");
        if (!Handler_Util.VerifySOAPVerseLogicalHandlerOrder(clientSideMsgs)) {
          TestUtil.logErr(
              "Client-Side SOAP verses Logical Handler Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg(
              "Client-Side SOAP verses Logical Handler Callbacks are (correct)");
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
      throw new Fault("ClientSOAPHandlerTest failed");
  }

  /*
   * @testName: ServerSOAPHandlerTest
   *
   * @assertion_ids: JAXWS:JAVADOC:87; JAXWS:JAVADOC:88; JAXWS:JAVADOC:89;
   * JAXWS:JAVADOC:90; JAXWS:JAVADOC:99; JAXWS:SPEC:9007; JAXWS:SPEC:9014;
   * JAXWS:SPEC:9015.1; JAXWS:SPEC:9018; WS4EE:SPEC:6010; WS4EE:SPEC:6013;
   * WS4EE:SPEC:6008; WS4EE:SPEC:6028; WS4EE:SPEC:6005; WS4EE:SPEC:6051;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side soap
   * message handler callbacks are called.
   */
  public void ServerSOAPHandlerTest() throws Fault {
    TestUtil.logTrace("ServerSOAPHandlerTest");
    boolean pass = true;
    try {
      if (!setupPorts()) {
        pass = false;
      }
      if (pass) {
        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        TestUtil.logMsg("Invoking RPC method port1.doHandlerTest1()");
        MyActionType ma = new MyActionType();
        ma.setAction("ServerSOAPTest");
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
          if (result != null && !result.equals("")) {
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
        if (!Handler_Util.VerifyHandlerCallBacks("Server", SOAP,
            serverSideMsgs)) {
          TestUtil.logErr("Server-Side Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side Callbacks are (correct)");
        }

        TestUtil.logMsg(
            "Verifying callbacks where SOAPHandlers are called after LogicalHandlers on Server-Side");
        if (!Handler_Util.VerifySOAPVerseLogicalHandlerOrder(serverSideMsgs)) {
          TestUtil.logErr(
              "Server-Side SOAP verses Logical Handler Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg(
              "Server-Side SOAP verses Logical Handler Callbacks are (correct)");
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
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault("ServerSOAPHandlerTest failed");
  }

  /*
   * @testName: ClientSOAPInboundHandleMessageThrowsRuntimeExceptionTest
   *
   * @assertion_ids: JAXWS:JAVADOC:87; JAXWS:JAVADOC:88; JAXWS:JAVADOC:89;
   * JAXWS:SPEC:9015.4.2; JAXWS:SPEC:9016.1; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side handler
   * callbacks are called by the JAXWS RUNTIME. Client handler throws a
   * RuntimeException while processing an inbound message.
   *
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientSOAPHandler5.handleMessage().doOutbound()
   * ClientSOAPHandler4.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleMessage().doInbound()
   * ClientSOAPHandler4.handleMessage().doInbound() ClientSOAPHandler4 Throwing
   * an inbound RuntimeException ClientSOAPHandler6.close()
   * ClientSOAPHandler4.close() ClientSOAPHandler5.close()
   */
  public void ClientSOAPInboundHandleMessageThrowsRuntimeExceptionTest()
      throws Fault {
    TestUtil
        .logTrace("ClientSOAPInboundHandleMessageThrowsRuntimeExceptionTest");
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
          if (info.getPortName().equals(PORT_QNAME3)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME3);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler4();
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
          TestUtil.logMsg("Getting existing Handlers for Port3");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding3.getHandlerChain();

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
          binding3.setHandlerChain(handlerList);
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
            "ClientSOAPInboundHandleMessageThrowsRuntimeExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);

        try {
          TestUtil.logMsg("Expecting RuntimeException");
          port3.doHandlerTest3(ma);
          TestUtil.logErr("Did not get expected RuntimeException");
          pass = false;
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did get expected RuntimeException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientSOAPHandler4.handleMessage throwing an inbound RuntimeException";
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
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Client", SOAP, false,
            Constants.INBOUND, clientSideMsgs)) {
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
          "ClientSOAPInboundHandleMessageThrowsRuntimeExceptionTest failed");
  }

  /*
   * @testName: ServerSOAPInboundHandleMessageThrowsRuntimeExceptionTest
   *
   * @assertion_ids: JAXWS:JAVADOC:87; JAXWS:JAVADOC:88; JAXWS:JAVADOC:89;
   * JAXWS:SPEC:9015.4.1; JAXWS:SPEC:9016.1; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side
   * handler callbacks are called. Server handler throws a RuntimeException
   * while processing an inbound message.
   *
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * 
   * ServerSOAPHandler6.handleMessage().doInbound()
   * ServerSOAPHandler4.handleMessage().doInbound() ServerSOAPHandler4 Throwing
   * an inbound RuntimeException ServerSOAPHandler4.close()
   * ServerSOAPHandler6.close()
   */
  public void ServerSOAPInboundHandleMessageThrowsRuntimeExceptionTest()
      throws Fault {
    TestUtil
        .logTrace("ServerSOAPInboundHandleMessageThrowsRuntimeExceptionTest");
    boolean pass = true;
    try {
      if (!setupPorts()) {
        pass = false;
      }
      if (pass) {
        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ServerSOAPInboundHandleMessageThrowsRuntimeExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting WebServiceException");
          port3.doHandlerTest3(ma);
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
          String tmp = "ServerSOAPHandler4.handleMessage throwing an inbound RuntimeException";
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
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Server", SOAP, false,
            Constants.INBOUND, serverSideMsgs)) {
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
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "ServerSOAPInboundHandleMessageThrowsRuntimeExceptionTest failed");
  }

  /*
   * @testName: ClientSOAPOutboundHandleMessageThrowsRuntimeExceptionTest
   *
   * @assertion_ids: JAXWS:JAVADOC:87; JAXWS:JAVADOC:88; JAXWS:JAVADOC:89;
   * JAXWS:SPEC:9015.4.1; JAXWS:SPEC:9016.1; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side handler
   * callbacks are called. Client handler throws a RuntimeException while
   * processing an outbound message.
   *
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientSOAPHandler5.handleMessage().doOutbound()
   * ClientSOAPHandler4.handleMessage().doOutbound() ClientSOAPHandler4 Throwing
   * an outbound RuntimeException ClientSOAPHandler4.close()
   * ClientSOAPHandler5.close()
   */
  public void ClientSOAPOutboundHandleMessageThrowsRuntimeExceptionTest()
      throws Fault {
    TestUtil
        .logTrace("ClientSOAPOutboundHandleMessageThrowsRuntimeExceptionTest");
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
          if (info.getPortName().equals(PORT_QNAME3)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME3);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler4();
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
          TestUtil.logMsg("Getting existing Handlers for Port3");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding3.getHandlerChain();

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
          binding3.setHandlerChain(handlerList);
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
            "ClientSOAPOutboundHandleMessageThrowsRuntimeExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting RuntimeException");
          port3.doHandlerTest3(ma);
          TestUtil.logErr("Did not get expected RuntimeException");
          pass = false;
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did get expected RuntimeException");
        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }
        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Client", SOAP, false,
            Constants.OUTBOUND, clientSideMsgs)) {
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
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "ClientSOAPOutboundHandleMessageThrowsRuntimeExceptionTest failed");
  }

  /*
   * @testName: ServerSOAPOutboundHandleMessageThrowsRuntimeExceptionTest
   *
   * @assertion_ids: JAXWS:JAVADOC:87; JAXWS:JAVADOC:88; JAXWS:JAVADOC:89;
   * JAXWS:SPEC:9015.4.2; JAXWS:SPEC:9016.1; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side
   * handler callbacks are called. Server handler throws a RuntimeException
   * while processing an outbound message.
   *
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ServerSOAPHandler6.handleMessage().doInbound()
   * ServerSOAPHandler4.handleMessage().doInbound()
   * ServerSOAPHandler5.handleMessage().doInbound()
   * ServerSOAPHandler5.handleMessage().doOutbound()
   * ServerSOAPHandler4.handleMessage().doOutbound() ServerSOAPHandler4 Throwing
   * an outbound RuntimeException ServerSOAPHandler5.close()
   * ServerSOAPHandler4.close() ServerSOAPHandler6.close()
   */
  public void ServerSOAPOutboundHandleMessageThrowsRuntimeExceptionTest()
      throws Fault {
    TestUtil
        .logTrace("ServerSOAPOutboundHandleMessageThrowsRuntimeExceptionTest");
    boolean pass = true;
    try {
      if (!setupPorts()) {
        pass = false;
      }
      if (pass) {
        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ServerSOAPOutboundHandleMessageThrowsRuntimeExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting WebServiceException");
          port3.doHandlerTest3(ma);
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
          String tmp = "ServerSOAPHandler4.handleMessage throwing an outbound RuntimeException";
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
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Server", SOAP, false,
            Constants.OUTBOUND, serverSideMsgs)) {
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
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }
    if (!pass)
      throw new Fault(
          "ServerSOAPOutboundHandleMessageThrowsRuntimeExceptionTest failed");
  }

  /*
   * @testName: ClientSOAPInboundHandleMessageThrowsSOAPFaultExceptionTest
   *
   * @assertion_ids: JAXWS:JAVADOC:87; JAXWS:JAVADOC:88; JAXWS:JAVADOC:89;
   * JAXWS:SPEC:9015.3.1; JAXWS:SPEC:9015.4.2; JAXWS:SPEC:9016.1;
   * WS4EE:SPEC:6008; WS4EE:SPEC:6028; WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side handler
   * callbacks are called. Client handler throws a SOAPFaultException while
   * processing an inbound message.
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientSOAPHandler5.handleMessage().doOutbound()
   * ClientSOAPHandler4.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleMessage().doInbound()
   * ClientSOAPHandler4.handleMessage().doInbound() ClientSOAPHandler4 Throwing
   * an inbound SOAPFaultException ClientSOAPHandler6.close()
   * ClientSOAPHandler4.close() ClientSOAPHandler5.close()
   */
  public void ClientSOAPInboundHandleMessageThrowsSOAPFaultExceptionTest()
      throws Fault {
    TestUtil
        .logTrace("ClientSOAPInboundHandleMessageThrowsSOAPFaultExceptionTest");
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
          if (info.getPortName().equals(PORT_QNAME3)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME3);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler4();
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
          TestUtil.logMsg("Getting existing Handlers for Port3");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding3.getHandlerChain();

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
          binding3.setHandlerChain(handlerList);
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
            "ClientSOAPInboundHandleMessageThrowsSOAPFaultExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);

        try {
          TestUtil.logTrace(
              "Expecting WebServiceException that wraps a SOAPFaultException");
          port3.doHandlerTest3(ma);
          TestUtil.logErr("Did not get expected WebServiceException");
          pass = false;
        } catch (WebServiceException e) {
          TestUtil.logMsg("Did get expected WebServiceException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "SOAPFaultException: ClientSOAPHandler4.handleMessage throwing an inbound SOAPFaultException";
          if (baos.toString().indexOf(tmp) > -1)
            TestUtil.logMsg("Did get expected nested WebServiceException text");
          else {
            TestUtil
                .logErr("Did not get expected nested WebServiceException text");
            TestUtil.logErr("expected:" + tmp);
            TestUtil.printStackTrace(e);
            pass = false;
          }

        } catch (Exception e) {
          TestUtil.logMsg("Got unexpected exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        Handler_Util.clearHandlers(listOfBindings);
        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Client", SOAP, false,
            Constants.INBOUND, clientSideMsgs)) {
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
          "ClientSOAPInboundHandleMessageThrowsSOAPFaultExceptionTest failed");
  }

  /*
   * @testName: ServerSOAPInboundHandleMessageThrowsSOAPFaultExceptionTest
   *
   * @assertion_ids: JAXWS:JAVADOC:87; JAXWS:JAVADOC:88; JAXWS:JAVADOC:89;
   * JAXWS:SPEC:9015.3.1; JAXWS:SPEC:9015.4.1; JAXWS:SPEC:9016.1;
   * WS4EE:SPEC:6008; WS4EE:SPEC:6028; WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side
   * handler callbacks are called. Server handler throws a SOAPFaultException
   * while processing an inbound message.
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ServerSOAPHandler6.handleMessage().doInbound()
   * ServerSOAPHandler4.handleMessage().doInbound() ServerSOAPHandler4 Throwing
   * an inbound SOAPFaultException ServerSOAPHandler6.handleFault()
   * ServerSOAPHandler4.close() ServerSOAPHandler6.close()
   */
  public void ServerSOAPInboundHandleMessageThrowsSOAPFaultExceptionTest()
      throws Fault {
    TestUtil
        .logTrace("ServerSOAPInboundHandleMessageThrowsSOAPFaultExceptionTest");
    boolean pass = true;
    try {
      if (!setupPorts()) {
        pass = false;
      }
      if (pass) {
        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ServerSOAPInboundHandleMessageThrowsSOAPFaultExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting WebServiceException");
          port3.doHandlerTest3(ma);
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
          String tmp = "ServerSOAPHandler4.handleMessage throwing an inbound SOAPFaultException";
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
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Server", SOAP, false,
            Constants.INBOUND, serverSideMsgs)) {
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
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }
    if (!pass)
      throw new Fault(
          "ServerSOAPInboundHandleMessageThrowsSOAPFaultExceptionTest failed");
  }

  /*
   * @testName: ClientSOAPOutboundHandleMessageThrowsSOAPFaultExceptionTest
   *
   * @assertion_ids: JAXWS:JAVADOC:87; JAXWS:JAVADOC:88; JAXWS:JAVADOC:89;
   * JAXWS:SPEC:9015.3.1; JAXWS:SPEC:9015.4.1; JAXWS:SPEC:9016.1;
   * WS4EE:SPEC:6008; WS4EE:SPEC:6028; WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side handler
   * callbacks are called. Client handler throws a SOAPFaultException while
   * processing an outbound message.
   *
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientSOAPHandler5.handleMessage().doOutbound()
   * ClientSOAPHandler4.handleMessage().doOutbound() ClientSOAPHandler4 Throwing
   * an outbound SOAPFaultException ClientSOAPHandler5.handleFault()
   * ClientSOAPHandler4.close() ClientSOAPHandler5.close()
   */
  public void ClientSOAPOutboundHandleMessageThrowsSOAPFaultExceptionTest()
      throws Fault {
    TestUtil.logTrace(
        "ClientSOAPOutboundHandleMessageThrowsSOAPFaultExceptionTest");
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
          if (info.getPortName().equals(PORT_QNAME3)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME3);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler4();
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
          TestUtil.logMsg("Getting existing Handlers for Port3");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding3.getHandlerChain();

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
          binding3.setHandlerChain(handlerList);
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
            "ClientSOAPOutboundHandleMessageThrowsSOAPFaultExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting SOAPFaultException");
          port3.doHandlerTest3(ma);
          TestUtil.logErr("Did not get expected SOAPFaultException");
          pass = false;
        } catch (SOAPFaultException e) {
          TestUtil.logMsg("Did get expected SOAPFaultException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientSOAPHandler4.handleMessage throwing an outbound SOAPFaultException";
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
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Client", SOAP, false,
            Constants.OUTBOUND, clientSideMsgs)) {
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
          "ClientSOAPOutboundHandleMessageThrowsSOAPFaultExceptionTest failed");
  }

  /*
   * @testName: ServerSOAPOutboundHandleMessageThrowsSOAPFaultExceptionTest
   *
   * @assertion_ids: JAXWS:JAVADOC:87; JAXWS:JAVADOC:88; JAXWS:JAVADOC:89;
   * JAXWS:SPEC:9015.3.1; JAXWS:SPEC:9015.4.2; JAXWS:SPEC:9016.1;
   * WS4EE:SPEC:6008; WS4EE:SPEC:6028; WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side
   * handler callbacks are called. Server handler throws a SOAPFaultException
   * while processing an outbound message.
   *
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ServerSOAPHandler6.handleMessage().doInbound()
   * ServerSOAPHandler4.handleMessage().doInbound()
   * ServerSOAPHandler5.handleMessage().doInbound()
   * ServerSOAPHandler5.handleMessage().doOutbound()
   * ServerSOAPHandler4.handleMessage().doOutbound() ServerSOAPHandler4 Throwing
   * an outbound SOAPFaultException ServerSOAPHandler5.close()
   * ServerSOAPHandler4.close() ServerSOAPHandler6.close()
   */
  public void ServerSOAPOutboundHandleMessageThrowsSOAPFaultExceptionTest()
      throws Fault {
    TestUtil.logTrace(
        "ServerSOAPOutboundHandleMessageThrowsSOAPFaultExceptionTest");
    boolean pass = true;
    try {
      if (!setupPorts()) {
        pass = false;
      }
      if (pass) {
        Handler_Util.clearHandlers(listOfBindings);
        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        MyActionType ma = new MyActionType();
        ma.setAction(
            "ServerSOAPOutboundHandleMessageThrowsSOAPFaultExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg(
              "Expecting SOAPFaultException wrapped by a WebServiceException");
          port3.doHandlerTest3(ma);
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
          String tmp = "ServerSOAPHandler4.handleMessage throwing an outbound SOAPFaultException";
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

        Handler_Util.clearHandlers(listOfBindings);
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
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Server", SOAP, false,
            Constants.OUTBOUND, serverSideMsgs)) {
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
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occured: " + e);
      pass = false;
    }
    if (!pass)
      throw new Fault(
          "ServerSOAPOutboundHandleMessageThrowsSOAPFaultExceptionTest failed");
  }

  /*
   * @testName: ServerEndpointRemoteRuntimeExceptionTest
   *
   * @assertion_ids: JAXWS:JAVADOC:87; JAXWS:JAVADOC:88; JAXWS:JAVADOC:89;
   * JAXWS:SPEC:9016.4; JAXWS:SPEC:9017; JAXWS:SPEC:9018; WS4EE:SPEC:6008;
   * WS4EE:SPEC:6028; WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side
   * handler callbacks are called. Endpoint throws a RuntimeException that is
   * wrapped by a WebServiceException
   *
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ServerSOAPHandler6.handleMessage().doInbound()
   * ServerSOAPHandler4.handleMessage().doInbound()
   * ServerSOAPHandler5.handleMessage().doInbound()
   * ServerSOAPHandler5.handleFault() ServerSOAPHandler4.handleFault()
   * ServerSOAPHandler6.handleFault() ServerSOAPHandler5.close()
   * ServerSOAPHandler4.close() ServerSOAPHandler6.close()
   */
  public void ServerEndpointRemoteRuntimeExceptionTest() throws Fault {
    TestUtil.logTrace("ServerEndpointRemoteRuntimeExceptionTest");
    boolean pass = true;
    try {
      if (!setupPorts()) {
        pass = false;
      }
      if (pass) {
        Handler_Util.clearHandlers(listOfBindings);
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
          port3.doHandlerTest3(ma);
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
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Server", SOAP, true,
            "", serverSideMsgs)) {
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
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ServerEndpointRemoteRuntimeExceptionTest failed");
  }

  /*
   * @testName: ServerEndpointRemoteSOAPFaultExceptionTest
   *
   * @assertion_ids: JAXWS:JAVADOC:87; JAXWS:JAVADOC:88; JAXWS:JAVADOC:89;
   * JAXWS:SPEC:9016.3; JAXWS:SPEC:9017; JAXWS:SPEC:9018; WS4EE:SPEC:6008;
   * WS4EE:SPEC:6028; WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side
   * handler callbacks are called. Endpoint throws a SOAPFaultException that is
   * wrapped by a WebServiceException
   *
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ServerSOAPHandler6.handleMessage().doInbound()
   * ServerSOAPHandler4.handleMessage().doInbound()
   * ServerSOAPHandler5.handleMessage().doInbound()
   * ServerSOAPHandler5.handleFault() ServerSOAPHandler4.handleFault()
   * ServerSOAPHandler6.handleFault() ServerSOAPHandler5.close()
   * ServerSOAPHandler4.close() ServerSOAPHandler6.close()
   */
  public void ServerEndpointRemoteSOAPFaultExceptionTest() throws Fault {
    TestUtil.logTrace("ServerEndpointRemoteSOAPFaultExceptionTest");
    boolean pass = true;
    try {
      if (!setupPorts()) {
        pass = false;
      }
      if (pass) {
        Handler_Util.clearHandlers(listOfBindings);
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
          port3.doHandlerTest3(ma);
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
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Server", SOAP, true,
            "", serverSideMsgs)) {
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
      }
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      pass = false;
    }
    if (!pass)
      throw new Fault("ServerEndpointRemoteSOAPFaultExceptionTest failed");
  }

  /*
   * @testName: ServerSOAPInboundHandleMessageFalseTest
   *
   * @assertion_ids: JAXWS:SPEC:9015.2.1; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the server-side
   * handleMessage callbacks are called by the JAXWS RUNTIME. ServerSOAPHandler4
   * returns false in the handleMessage method while processing an inbound
   * message. ------------------------------------------------------- This is
   * the expected order -------------------------------------------------------
   * ServerSOAPHandler6.handleMessage().doInbound()
   * ServerSOAPHandler4.handleMessage().doInbound()
   * ServerSOAPHandler6.handleMessage().doInbound() ServerSOAPHandler4.close()
   * ServerSOAPHandler6.close()
   */
  public void ServerSOAPInboundHandleMessageFalseTest() throws Fault {
    TestUtil.logTrace("ServerSOAPInboundHandleMessageFalseTest");
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
        ma.setAction("ServerSOAPInboundHandleMessageFalseTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          port3.doHandlerTest3(ma);
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
        if (!Handler_Util.VerifyHandleMessageFalseCallBacks("Server", SOAP,
            serverSideMsgs, Constants.INBOUND)) {
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
      throw new Fault("ServerSOAPInboundHandleMessageFalseTest failed");
  }

  /*
   * @testName: ServerSOAPOutboundHandleMessageFalseTest
   *
   * @assertion_ids: JAXWS:SPEC:9015.2.2; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the server-side
   * handleMessage callbacks are called by the JAXWS RUNTIME. ServerSOAPHandler4
   * returns false in the handleMessage method while processing an outbound
   * message. ------------------------------------------------------- This is
   * the expected order -------------------------------------------------------
   * ServerSOAPHandler6.handleMessage().doInbound()
   * ServerSOAPHandler4.handleMessage().doInbound()
   * ServerSOAPHandler5.handleMessage().doInbound()
   * ServerSOAPHandler5.handleMessage().doOutbound()
   * ServerSOAPHandler4.handleMessage().doOutbound() ServerSOAPHandler5.close()
   * ServerSOAPHandler4.close() ServerSOAPHandler6.close()
   */
  public void ServerSOAPOutboundHandleMessageFalseTest() throws Fault {
    TestUtil.logTrace("ServerSOAPOutboundHandleMessageFalseTest");
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
        ma.setAction("ServerSOAPOutboundHandleMessageFalseTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          port3.doHandlerTest3(ma);
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
        if (!Handler_Util.VerifyHandleMessageFalseCallBacks("Server", SOAP,
            serverSideMsgs, Constants.OUTBOUND)) {
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
      throw new Fault("ServerSOAPOutboundHandleMessageFalseTest failed");
  }

  /*
   * @testName: ClientSOAPInboundHandleMessageFalseTest
   *
   * @assertion_ids: JAXWS:SPEC:9015.2.2; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME. ClientSOAPHandler4
   * returns false in the handleMessage method while processing an inbound
   * message. ------------------------------------------------------- This is
   * the expected order -------------------------------------------------------
   * ClientSOAPHandler5.handleMessage().doOutbound()
   * ClientSOAPHandler4.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleMessage().doInbound()
   * ClientSOAPHandler4.handleMessage().doInbound() ClientSOAPHandler6.close()
   * ClientSOAPHandler4.close() ClientSOAPHandler5.close()
   */
  public void ClientSOAPInboundHandleMessageFalseTest() throws Fault {
    TestUtil.logTrace("ClientSOAPInboundHandleMessageFalseTest");
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
          if (info.getPortName().equals(PORT_QNAME3)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME3);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler4();
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
          TestUtil.logMsg("Getting existing Handlers for Port3");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding3.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding3.setHandlerChain(handlerList);
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
        ma.setAction("ClientSOAPInboundHandleMessageFalseTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          port3.doHandlerTest3(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandleMessageFalseCallBacks("Client", SOAP,
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
      throw new Fault("ClientSOAPInboundHandleMessageFalseTest failed");
  }

  /*
   * @testName: ClientSOAPOutboundHandleMessageFalseTest
   *
   * @assertion_ids: JAXWS:SPEC:9015.2.1; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME. ClientSOAPHandler4
   * returns false in the handleMessage method while processing an outbound
   * message. ------------------------------------------------------- This is
   * the expected order -------------------------------------------------------
   * ClientSOAPHandler5.handleMessage().doOutbound()
   * ClientSOAPHandler4.handleMessage().doOutbound()
   * ClientSOAPHandler5.handleMessage().doOutbound() ClientSOAPHandler4.close()
   * ClientSOAPHandler5.close()
   */
  public void ClientSOAPOutboundHandleMessageFalseTest() throws Fault {
    TestUtil.logTrace("ClientSOAPOutboundHandleMessageFalseTest");
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
          if (info.getPortName().equals(PORT_QNAME3)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME3);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler4();
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
          TestUtil.logMsg("Getting existing Handlers for Port3");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding3.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientSOAPHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding3.setHandlerChain(handlerList);
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
        ma.setAction("ClientSOAPOutboundHandleMessageFalseTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          port3.doHandlerTest3(ma);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        List<String> clientSideMsgs = HandlerTracker.getListMessages1();

        TestUtil.logMsg("Verifying Client-Side JAXWS-RUNTIME Callbacks");
        if (!Handler_Util.VerifyHandleMessageFalseCallBacks("Client", SOAP,
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
      throw new Fault("ClientSOAPOutboundHandleMessageFalseTest failed");
  }

  /*
   * @testName: ClientSOAPOutboundHandleFaultFalseTest
   *
   * @assertion_ids: JAXWS:SPEC:9016.2; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleFault callbacks are called by the JAXWS RUNTIME. ClientSOAPHandler6
   * throws a SOAPFaultException ClientSOAPHandler4 returns a false for
   * handleFault method while processing an outbound message.
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientSOAPHandler5.handleMessage().doOutbound()
   * ClientSOAPHandler4.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleMessage().doOutbound() ClientSOAPHandler6 Throwing
   * an outbound SOAPFaultException ClientSOAPHandler4.handleFault()
   * ClientSOAPHandler6.close() ClientSOAPHandler4.close()
   * ClientSOAPHandler5.close()
   * 
   */
  public void ClientSOAPOutboundHandleFaultFalseTest() throws Fault {
    TestUtil.logTrace("ClientSOAPOutboundHandleFaultFalseTest");
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
          if (info.getPortName().equals(PORT_QNAME3)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME3);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler4();
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
          TestUtil.logMsg("Getting existing Handlers for Port3");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding3.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientSOAPHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding3.setHandlerChain(handlerList);
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
        ma.setAction("ClientSOAPOutboundHandleFaultFalseTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting RuntimeException");
          port3.doHandlerTest3(ma);
          TestUtil.logErr("Did not get expected RuntimeException");
          pass = false;
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did get expected RuntimeException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientSOAPHandler6.handleMessage throws SOAPFaultException for ClientSOAPOutboundHandleFaultFalseTest";
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
        if (!Handler_Util.VerifyHandleFaultFalseCallBacks("Client", SOAP,
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
      throw new Fault("ClientSOAPOutboundHandleFaultFalseTest failed");
  }

  /*
   * @testName: ServerSOAPInboundHandleFaultFalseTest
   *
   * @assertion_ids: JAXWS:SPEC:9016.2; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the server-side
   * handleMessage callbacks are called by the JAXWS RUNTIME. ServerSOAPHandler5
   * throws a SOAPFaultException ServerSOAPHandler4 returns a false for
   * handleFault method while processing an outbound message.
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ServerSOAPHandler6.handleMessage().doInbound()
   * ServerSOAPHandler4.handleMessage().doInbound()
   * ServerSOAPHandler5.handleMessage().doInbound() ServerSOAPHandler5 Throwing
   * an inbound SOAPFaultException ServerSOAPHandler4.handleFault()
   * ServerSOAPHandler5.close() ServerSOAPHandler4.close()
   * ServerSOAPHandler6.close()
   */
  public void ServerSOAPInboundHandleFaultFalseTest() throws Fault {
    TestUtil.logTrace("ServerSOAPInboundHandleFaultFalseTest");
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
        ma.setAction("ServerSOAPInboundHandleFaultFalseTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting RuntimeException");
          port3.doHandlerTest3(ma);
          TestUtil.logErr("Did not get expected RuntimeException");
          pass = false;
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did get expected RuntimeException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ServerSOAPHandler5.handleMessage throws SOAPFaultException for ServerSOAPInboundHandleFaultFalseTest";
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
        if (!Handler_Util.VerifyHandleFaultFalseCallBacks("Server", SOAP,
            serverSideMsgs, Constants.INBOUND)) {
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
      throw new Fault("ServerSOAPInboundHandleFaultFalseTest failed");
  }

  /*
   * @testName: ClientSOAPOutboundHandleFaultThrowsRuntimeExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:9016.4; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME. ClientSOAPHandler6
   * throws a SOAPFaultException ClientSOAPHandler4 throws a RuntimeException in
   * handleFault method processing an inbound message.
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientSOAPHandler5.handleMessage().doOutbound()
   * ClientSOAPHandler4.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleMessage().doOutbound() ClientSOAPHandler6 Throwing
   * an outbound SOAPFaultException ClientSOAPHandler4.handleFault()
   * ClientSOAPHandler4 Throwing an inbound RuntimeException
   * ClientSOAPHandler6.close() ClientSOAPHandler4.close()
   * ClientSOAPHandler5.close()
   */
  public void ClientSOAPOutboundHandleFaultThrowsRuntimeExceptionTest()
      throws Fault {
    TestUtil
        .logTrace("ClientSOAPOutboundHandleFaultThrowsRuntimeExceptionTest");
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
          if (info.getPortName().equals(PORT_QNAME3)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME3);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler4();
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
          TestUtil.logMsg("Getting existing Handlers for Port3");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding3.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientSOAPHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding3.setHandlerChain(handlerList);
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
        ma.setAction("ClientSOAPOutboundHandleFaultThrowsRuntimeExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);

        try {
          TestUtil.logMsg("Expecting RuntimeException");
          port3.doHandlerTest3(ma);
          TestUtil.logErr("Did not get expected RuntimeException");
          pass = false;
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did get expected RuntimeException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientSOAPHandler4.handleFault throwing an inbound RuntimeException";
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
            SOAP, clientSideMsgs, Constants.OUTBOUND)) {
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
          "ClientSOAPOutboundHandleFaultThrowsRuntimeExceptionTest failed");
  }

  /*
   * @testName: ServerSOAPInboundHandleFaultThrowsRuntimeExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:9016.4; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the server-side
   * handleMessage callbacks are called by the JAXWS RUNTIME. ServerSOAPHandler5
   * throws a SOAPFaultException ServerSOAPHandler4 throws a RuntimeException in
   * the handleFault method processing an outbound message.
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ServerSOAPHandler6.handleMessage().doInbound()
   * ServerSOAPHandler4.handleMessage().doInbound()
   * ServerSOAPHandler5.handleMessage().doInbound() ServerSOAPHandler5 Throwing
   * an inbound SOAPFaultException ServerSOAPHandler4.handleFault()
   * ServerSOAPHandler4 Throwing an outbound RuntimeException
   * ServerSOAPHandler5.close() ServerSOAPHandler4.close()
   * ServerSOAPHandler6.close()
   */
  public void ServerSOAPInboundHandleFaultThrowsRuntimeExceptionTest()
      throws Fault {
    TestUtil.logTrace("ServerSOAPInboundHandleFaultThrowsRuntimeExceptionTest");
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
        ma.setAction("ServerSOAPInboundHandleFaultThrowsRuntimeExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting RuntimeException");
          port3.doHandlerTest3(ma);
          TestUtil.logErr("Did not get expected RuntimeException");
          pass = false;
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did get expected RuntimeException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ServerSOAPHandler4.handleFault throwing an outbound RuntimeException";
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
            SOAP, serverSideMsgs, Constants.INBOUND)) {
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
          "ServerSOAPInboundHandleFaultThrowsRuntimeExceptionTest failed");
  }

  /*
   * @testName: ClientSOAPOutboundHandleFaultThrowsSOAPFaultExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:9016.3; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME. ClientSOAPHandler6
   * throws a SOAPFaultException ClientSOAPHandler4 throws a SOAPFaultException
   * in the handleFault method processing an inbound message.
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientSOAPHandler5.handleMessage().doOutbound()
   * ClientSOAPHandler4.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleMessage().doOutbound() ClientSOAPHandler6 Throwing
   * an outbound SOAPFaultException ClientSOAPHandler4.handleFault()
   * ClientSOAPHandler4 Throwing an inbound SOAPFaultException
   * ClientSOAPHandler6.close() ClientSOAPHandler4.close()
   * ClientSOAPHandler5.close()
   */
  public void ClientSOAPOutboundHandleFaultThrowsSOAPFaultExceptionTest()
      throws Fault {
    TestUtil
        .logTrace("ClientSOAPOutboundHandleFaultThrowsSOAPFaultExceptionTest");
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
          if (info.getPortName().equals(PORT_QNAME3)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME3);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler4();
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
          TestUtil.logMsg("Getting existing Handlers for Port3");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding3.getHandlerChain();

          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Programmatically registering the following handlers through the binding: \n"
                  + "ClientSOAPHandler6, ClientSOAPHandler6");
          TestUtil.logMsg("----------------------------------------------");
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg(
              "Construct HandleInfo for ClientSOAPHandler6 and add to HandlerChain");
          handler = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler6();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding3.setHandlerChain(handlerList);
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
            "ClientSOAPOutboundHandleFaultThrowsSOAPFaultExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);

        try {
          TestUtil.logMsg(
              "Expecting WebServiceException that wraps a SOAPFaultException");
          port3.doHandlerTest3(ma);
          TestUtil.logErr("Did not get expected WebServiceException");
          pass = false;
        } catch (WebServiceException e) {
          TestUtil.logMsg("Did get expected WebServiceException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "SOAPFaultException: ClientSOAPHandler4.handleFault throwing an inbound SOAPFaultException";
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
            SOAP, clientSideMsgs, Constants.OUTBOUND)) {
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
          "ClientSOAPOutboundHandleFaultThrowsSOAPFaultExceptionTest failed");
  }

  /*
   * @testName: ServerSOAPInboundHandleFaultThrowsSOAPFaultExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:9016.3; WS4EE:SPEC:6008; WS4EE:SPEC:6028;
   * WS4EE:SPEC:6005;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the server-side
   * handleMessage callbacks are called by the JAXWS RUNTIME. ServerSOAPHandler5
   * throws a SOAPFaultException ServerSOAPHandler4 throws a SOAPFaultException
   * in handleFailt method processing an outbound message.
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ServerSOAPHandler6.handleMessage().doInbound()
   * ServerSOAPHandler4.handleMessage().doInbound()
   * ServerSOAPHandler5.handleMessage().doInbound() ServerSOAPHandler5 Throwing
   * an inbound SOAPFaultException ServerSOAPHandler4.handleFault()
   * ServerSOAPHandler4 Throwing an outbound SOAPFaultException
   * ServerSOAPHandler5.close() ServerSOAPHandler4.close()
   * ServerSOAPHandler6.close()
   */
  public void ServerSOAPInboundHandleFaultThrowsSOAPFaultExceptionTest()
      throws Fault {
    TestUtil
        .logTrace("ServerSOAPInboundHandleFaultThrowsSOAPFaultExceptionTest");
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
            "ServerSOAPInboundHandleFaultThrowsSOAPFaultExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting SOAPFaultException");
          port3.doHandlerTest3(ma);
          TestUtil.logErr("Did not get expected SOAPFaultException");
          pass = false;
        } catch (SOAPFaultException e) {
          TestUtil.logMsg("Did get expected SOAPFaultException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ServerSOAPHandler4.handleFault throwing an outbound SOAPFaultException";
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
            SOAP, serverSideMsgs, Constants.INBOUND)) {
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
          "ServerSOAPInboundHandleFaultThrowsSOAPFaultExceptionTest failed");
  }

  /*
   * @testName: ServerSOAPInboundHandlerThrowsSOAPFaultToClientHandlersTest
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
   * ClientSOAPHandler5.handleMessage().doOutbound()
   * ClientSOAPHandler4.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleFault() ClientSOAPHandler6 received SOAPFault from
   * Inbound ServerSOAPHandler6 ClientSOAPHandler6.close()
   * ClientSOAPHandler4.close() ClientSOAPHandler5.close()
   *
   * ServerSOAPHandler6.handleMessage().doInbound() ServerSOAPHandler6 Throwing
   * an inbound SOAPFaultException ServerSOAPHandler6.close()
   *
   */
  public void ServerSOAPInboundHandlerThrowsSOAPFaultToClientHandlersTest()
      throws Fault {
    TestUtil.logTrace(
        "ServerSOAPInboundHandlerThrowsSOAPFaultToClientHandlersTest");
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
          if (info.getPortName().equals(PORT_QNAME3)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME3);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler4();
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
          TestUtil.logMsg("Getting existing Handlers for Port3");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding3.getHandlerChain();

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
          binding3.setHandlerChain(handlerList);
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
            "ServerSOAPInboundHandlerThrowsSOAPFaultToClientHandlersTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);

        try {
          TestUtil.logMsg("Expecting RuntimeException");
          port3.doHandlerTest3(ma);
          TestUtil.logErr("Did not get expected RuntimeException");
          pass = false;
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did get expected RuntimeException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ServerSOAPHandler6.handleMessage throws SOAPFaultException for ServerSOAPInboundHandlerThrowsSOAPFaultToClientHandlersTest";
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
            "Client", SOAP, Constants.OUTBOUND, clientSideMsgs)) {
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
            "Server", SOAP, Constants.INBOUND, serverSideMsgs)) {
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
          "ServerSOAPInboundHandlerThrowsSOAPFaultToClientHandlersTest failed");
  }

  /*
   * @testName: ServerSOAPOutboundHandlerThrowsSOAPFaultToClientHandlersTest
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
   * ClientSOAPHandler5.handleMessage().doOutbound()
   * ClientSOAPHandler4.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleFault() ClientSOAPHandler6 received SOAPFault from
   * Outbound ServerSOAPHandler6 ClientSOAPHandler6.close()
   * ClientSOAPHandler4.close() ClientSOAPHandler5.close()
   *
   * ServerSOAPHandler6.handleMessage().doInbound()
   * ServerSOAPHandler4.handleMessage().doInbound()
   * ServerSOAPHandler5.handleMessage().doInbound()
   * ServerSOAPHandler5.handleMessage().doOutbound()
   * ServerSOAPHandler4.handleMessage().doOutbound()
   * ServerSOAPHandler6.handleMessage().doOutbound() ServerSOAPHandler6 Throwing
   * an inbound SOAPFaultException ServerSOAPHandler6.close()
   *
   */
  public void ServerSOAPOutboundHandlerThrowsSOAPFaultToClientHandlersTest()
      throws Fault {
    TestUtil.logTrace(
        "ServerSOAPOutboundHandlerThrowsSOAPFaultToClientHandlersTest");
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
          if (info.getPortName().equals(PORT_QNAME3)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME3);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler4();
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
          TestUtil.logMsg("Getting existing Handlers for Port3");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding3.getHandlerChain();

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
          binding3.setHandlerChain(handlerList);
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
            "ServerSOAPOutboundHandlerThrowsSOAPFaultToClientHandlersTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);

        try {
          TestUtil.logMsg("Expecting RuntimeException");
          port3.doHandlerTest3(ma);
          TestUtil.logErr("Did not get expected RuntimeException");
          pass = false;
        } catch (RuntimeException e) {
          TestUtil.logMsg("Did get expected RuntimeException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ServerSOAPHandler6.handleMessage throws SOAPFaultException for ServerSOAPOutboundHandlerThrowsSOAPFaultToClientHandlersTest";
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
            "Client", SOAP, Constants.INBOUND, clientSideMsgs)) {
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
            "Server", SOAP, Constants.OUTBOUND, serverSideMsgs)) {
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
          "ServerSOAPOutboundHandlerThrowsSOAPFaultToClientHandlersTest failed");
  }

  /*
   * @testName: ClientSOAPInboundHandleMessageThrowsWebServiceExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:4021;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME. ClientSOAPHandler4
   * throws a WebServiceException in handleMessage method
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientSOAPHandler5.handleMessage().doOutbound()
   * ClientSOAPHandler4.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleMessage().doInbound()
   * ClientSOAPHandler4.handleMessage().doInbound() ClientSOAPHandler4 Throwing
   * an inbound WebServiceException ClientSOAPHandler6.close()
   * ClientSOAPHandler4.close() ClientSOAPHandler5.close()
   */
  public void ClientSOAPInboundHandleMessageThrowsWebServiceExceptionTest()
      throws Fault {
    TestUtil.logTrace(
        "ClientSOAPInboundHandleMessageThrowsWebServiceExceptionTest");
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

          if (info.getPortName().equals(PORT_QNAME3)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME3);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler4();
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
          TestUtil.logMsg("Getting existing Handlers for Port3");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding3.getHandlerChain();

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
          binding3.setHandlerChain(handlerList);
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
            "ClientSOAPInboundHandleMessageThrowsWebServiceExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting WebServiceException");
          port3.doHandlerTest3(ma);
          TestUtil.logErr("Did not get expected WebServiceException");
          pass = false;
        } catch (WebServiceException e) {
          TestUtil.logMsg("Did get expected WebServiceException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientSOAPHandler4.handleMessage throwing an inbound WebServiceException";
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
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Client", SOAP, false,
            Constants.INBOUND, clientSideMsgs)) {
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
          "ClientSOAPInboundHandleMessageThrowsWebServiceExceptionTest failed");
  }

  /*
   * @testName: ClientSOAPOutboundHandleMessageThrowsWebServiceExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:4021;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME. ClientSOAPHandler4
   * throws a WebServiceException in handleMessage method
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientSOAPHandler5.handleMessage().doOutbound()
   * ClientSOAPHandler4.handleMessage().doOutbound() ClientSOAPHandler4 Throwing
   * an outbound WebServiceException ClientSOAPHandler4.close()
   * ClientSOAPHandler5.close()
   */
  public void ClientSOAPOutboundHandleMessageThrowsWebServiceExceptionTest()
      throws Fault {
    TestUtil.logTrace(
        "ClientSOAPOutboundHandleMessageThrowsWebServiceExceptionTest");
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

          if (info.getPortName().equals(PORT_QNAME3)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME3);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler4();
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
          TestUtil.logMsg("Getting existing Handlers for Port3");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding3.getHandlerChain();

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
          binding3.setHandlerChain(handlerList);
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
            "ClientSOAPOutboundHandleMessageThrowsWebServiceExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting WebServiceException");
          port3.doHandlerTest3(ma);
          TestUtil.logErr("Did not get expected WebServiceException");
          pass = false;
        } catch (WebServiceException e) {
          TestUtil.logMsg("Did get expected WebServiceException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientSOAPHandler4.handleMessage throwing an outbound WebServiceException";
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
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Client", SOAP, false,
            Constants.OUTBOUND, clientSideMsgs)) {
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
          "ClientSOAPOutboundHandleMessageThrowsWebServiceExceptionTest failed");
  }

  /*
   * @testName: ClientSOAPInboundHandleMessageThrowsProtocolExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:4021;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME. ClientSOAPHandler4
   * throws a ProtocolException in handleMessage method
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientSOAPHandler5.handleMessage().doOutbound()
   * ClientSOAPHandler4.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleMessage().doOutbound()
   * ClientSOAPHandler6.handleMessage().doInbound()
   * ClientSOAPHandler4.handleMessage().doInbound() ClientSOAPHandler4 Throwing
   * an inbound ProtocolException ClientSOAPHandler6.close()
   * ClientSOAPHandler4.close() ClientSOAPHandler5.close()
   */
  public void ClientSOAPInboundHandleMessageThrowsProtocolExceptionTest()
      throws Fault {
    TestUtil
        .logTrace("ClientSOAPInboundHandleMessageThrowsProtocolExceptionTest");
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

          if (info.getPortName().equals(PORT_QNAME3)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME3);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler4();
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
          TestUtil.logMsg("Getting existing Handlers for Port3");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding3.getHandlerChain();

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
          binding3.setHandlerChain(handlerList);
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
            "ClientSOAPInboundHandleMessageThrowsProtocolExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting ProtocolException");
          port3.doHandlerTest3(ma);
          TestUtil.logErr("Did not get expected ProtocolException");
          pass = false;
        } catch (ProtocolException e) {
          TestUtil.logMsg("Did get expected ProtocolException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientSOAPHandler4.handleMessage throwing an inbound ProtocolException";
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
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Client", SOAP, false,
            Constants.INBOUND, clientSideMsgs)) {
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
          "ClientSOAPInboundHandleMessageThrowsProtocolExceptionTest failed");
  }

  /*
   * @testName: ClientSOAPOutboundHandleMessageThrowsProtocolExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:4021;
   *
   * @test_Strategy: Invoke an RPC method. Verify that the client-side
   * handleMessage callbacks are called by the JAXWS RUNTIME. ClientSOAPHandler4
   * throws a ProtocolException in handleMessage method
   * ------------------------------------------------------- This is the
   * expected order -------------------------------------------------------
   * ClientSOAPHandler5.handleMessage().doOutbound()
   * ClientSOAPHandler4.handleMessage().doOutbound() ClientSOAPHandler4 Throwing
   * an outbound ProtocolException ClientSOAPHandler4.close()
   * ClientSOAPHandler5.close()
   */
  public void ClientSOAPOutboundHandleMessageThrowsProtocolExceptionTest()
      throws Fault {
    TestUtil
        .logTrace("ClientSOAPOutboundHandleMessageThrowsProtocolExceptionTest");
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

          if (info.getPortName().equals(PORT_QNAME3)) {
            TestUtil.logMsg("----------------------------------------------");
            TestUtil
                .logMsg("Create port based handlers for port: " + PORT_QNAME3);
            TestUtil.logMsg("----------------------------------------------");
            TestUtil.logMsg(
                "Construct HandleInfo for ClientSOAPHandler4 and add to HandlerChain");
            Handler h4 = new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientSOAPHandler4();
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
          TestUtil.logMsg("Getting existing Handlers for Port3");
          TestUtil.logMsg("----------------------------------------------");
          List<Handler> handlerList = binding3.getHandlerChain();

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
          binding3.setHandlerChain(handlerList);
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
            "ClientSOAPOutboundHandleMessageThrowsProtocolExceptionTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          TestUtil.logMsg("Expecting ProtocolException");
          port3.doHandlerTest3(ma);
          TestUtil.logErr("Did not get expected ProtocolException");
          pass = false;
        } catch (ProtocolException e) {
          TestUtil.logMsg("Did get expected ProtocolException");
          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          PrintStream ps = new PrintStream(baos, true);
          e.printStackTrace(ps);
          String tmp = "ClientSOAPHandler4.handleMessage throwing an outbound ProtocolException";
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
        if (!Handler_Util.VerifyHandlerExceptionCallBacks("Client", SOAP, false,
            Constants.OUTBOUND, clientSideMsgs)) {
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
          "ClientSOAPOutboundHandleMessageThrowsProtocolExceptionTest failed");
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
      TestUtil.logErr("Call to purge server-side tracker data failed" + e);
    }

  }

}
