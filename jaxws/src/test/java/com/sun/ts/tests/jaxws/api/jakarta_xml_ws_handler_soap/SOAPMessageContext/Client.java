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
 * @(#)Client.java	1.42 05/08/29
 */

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws_handler_soap.SOAPMessageContext;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.*;

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
import jakarta.activation.DataHandler;

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

  private static final String ENDPOINT4_URL = "dlhandlerservice.endpoint.4";

  private static final String CTXROOT = "dlhandlerservice.ctxroot.1";

  private String url1 = null;

  private String url4 = null;

  private URL wsdlurl = null;

  private String ctxroot = null;

  // service and port information
  private static final String NAMESPACEURI = "http://dlhandlerservice.org/wsdl";

  private static final String SERVICE_NAME = "DLHandlerService";

  private static final String PORT_NAME1 = "HelloPort";

  private static final String PORT_NAME4 = "GetTrackerDataPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME1 = new QName(NAMESPACEURI, PORT_NAME1);

  private static final Class SERVICE_CLASS = com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.DLHandlerService.class;

  private static final String THEBINDINGPROTOCOL = jakarta.xml.ws.soap.SOAPBinding.SOAP11HTTP_BINDING;

  private static final String SOAP = "SOAP";

  private static final String TEST_TYPE = SOAP + "Test";

  private Handler handler = null;

  Hello port1 = null;

  GetTrackerData port4 = null;

  static DLHandlerService service = null;

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
    ctxroot = JAXWS_Util.getURLFromProp(CTXROOT);
    TestUtil.logMsg("Service Endpoint1 URL: " + url1);
    TestUtil.logMsg("Service Endpoint4 URL: " + url4);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
    TestUtil.logMsg("Context Root:         " + ctxroot);

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
   * @testName: ClientMessageContextTest
   *
   * @assertion_ids: JAXWS:JAVADOC:100; JAXWS:JAVADOC:101; JAXWS:JAVADOC:102;
   * JAXWS:JAVADOC:103; JAXWS:SPEC:9022; JAXWS:SPEC:9023; JAXWS:SPEC:9024;
   * JAXWS:SPEC:9025; JAXWS:SPEC:9026; JAXWS:SPEC:9041; WS4EE:SPEC:6012;
   * WS4EE:SPEC:6013; WS4EE:SPEC:6014; WS4EE:SPEC:6008; WS4EE:SPEC:6002;
   * WS4EE:SPEC:6039; WS4EE:SPEC:6047;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the client-side soap
   * message handler callbacks are called.
   *
   */
  public void ClientMessageContextTest() throws Fault {
    TestUtil.logTrace("ClientMessageContextTest");
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

        TestUtil.logTrace("Getting RequestContext to set a property");
        java.util.Map<String, Object> rc = bp1.getRequestContext();
        if (rc != null) {
          rc.put("ClientToClientProp", "client");
        } else {
          TestUtil.logErr(
              "The request context returned from BindingProvider.getRequestContext() was null");
          pass = false;
        }

        TestUtil.logMsg("Invoking RPC method port1.doHandlerTest1()");
        MyActionType ma = new MyActionType();
        ma.setAction("ClientMessageContextTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);

        MyHeaderType mht = new MyHeaderType();
        mht.setHeader("this is the header");
        Holder<MyHeaderType> hmht = new Holder<MyHeaderType>(mht);

        try {
          port1.doHandlerHeaderTest1(ma, hmht);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        boolean clientSOAPMessageScopeAppProp = false;
        boolean clientSOAPMessageScopeHandlerProp = false;
        boolean clientToClientProp = false;

        TestUtil.logTrace("Getting ResponseContext");
        rc = bp1.getResponseContext();
        if (rc == null) {
          TestUtil.logErr(
              "The response context returned from BindingProvider.getResponseContext() was null");
          pass = false;
        } else {
          Iterator i = rc.keySet().iterator();
          while (i.hasNext()) {
            Object o = i.next();
            TestUtil.logTrace("Object Property=" + o);
            if (o instanceof String) {
              String key = (String) o;
              if (key.equals(
                  "INBOUNDClientSOAPMessageScopeAppPropSetByHandler3")) {
                clientSOAPMessageScopeAppProp = true;
                TestUtil.logTrace(
                    "Found INBOUNDClientSOAPMessageScopeAppPropSetByHandler3");
              }
              if (key.equals(
                  "INBOUNDClientSOAPMessageScopeHandlerPropSetByHandler3")) {
                clientSOAPMessageScopeHandlerProp = true;
                TestUtil.logTrace(
                    "Found INBOUNDClientSOAPMessageScopeHandlerPropSetByHandler3");
              }
              if (key.equals("ClientToClientProp")) {
                clientToClientProp = true;
                TestUtil.logTrace("Found ClientToClientProp");
                Object o1 = rc.get("ClientToClientProp");
                if (o1 instanceof String) {
                  String value = (String) o1;
                  String expected = "clientOUTBOUNDClientSOAPHandler2INBOUNDClientSOAPHandler2";
                  if (!value.equals(expected)) {
                    TestUtil
                        .logErr("The value of ClientToClientProp was wrong");
                    TestUtil.logErr("Expected = " + expected);
                    TestUtil.logErr("Actual = " + value);
                    pass = false;
                  }
                } else {
                  TestUtil.logErr(
                      "The value of ClientToClientProp was not a String");
                  pass = false;

                }
              }

            }

          }
          if (!clientSOAPMessageScopeAppProp) {
            TestUtil.logErr(
                "The property INBOUNDClientSOAPMessageScopeAppPropSetByHandler3 was not accessible by the client");
            pass = false;
          }
          if (!clientToClientProp) {
            TestUtil.logErr(
                "The property ClientToClientProp was not accessible by the client");
            pass = false;
          }
          if (clientSOAPMessageScopeHandlerProp) {
            TestUtil.logErr(
                "The property INBOUNDClientSOAPMessageScopeHandlerPropSetByHandler3 was accessible by the client");
            pass = false;
          }
        }

        TestUtil.logMsg("Get client side results back from Tracker");
        List<String> clientSideMCMsgs = HandlerTracker.getListMessages2();
        List<String> clientSideSMCMsgs = HandlerTracker.getListMessages3();

        // verify client-side callbacks
        TestUtil.logMsg("Verifying MessageContext callbacks on Client-Side");
        if (!Handler_Util.VerifyMessageContextCallBacks("Client", SOAP,
            clientSideMCMsgs)) {
          TestUtil
              .logErr("Client-Side MessageContext Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side MessageContext Callbacks are (correct)");
        }

        TestUtil
            .logMsg("Verifying LogicalMessageContext callbacks on Client-Side");
        if (!Handler_Util.VerifyLogicalOrSOAPMessageContextCallBacks("Client",
            SOAP, clientSideSMCMsgs)) {
          TestUtil.logErr(
              "Client-Side LogicalMessageContext Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg(
              "Client-Side LogicalMessageContext Callbacks are (correct)");
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
      throw new Fault("ClientMessageContextTest failed");
  }

  /*
   * @testName: ServerMessageContextTest
   *
   * @assertion_ids: JAXWS:JAVADOC:100; JAXWS:JAVADOC:101; JAXWS:JAVADOC:102;
   * JAXWS:JAVADOC:103; JAXWS:SPEC:9022; JAXWS:SPEC:9023; JAXWS:SPEC:9024;
   * JAXWS:SPEC:9025; JAXWS:SPEC:9026; JAXWS:SPEC:9041; WS4EE:SPEC:6012;
   * WS4EE:SPEC:6013; WS4EE:SPEC:6014; WS4EE:SPEC:6002; WS4EE:SPEC:6039;
   * WS4EE:SPEC:6047;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the server-side soap
   * message handler callbacks are called.
   */
  public void ServerMessageContextTest() throws Fault {
    TestUtil.logTrace("ServerMessageContextTest");
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
        ma.setAction("ServerMessageContextTest");
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

        List<String> serverSideMCMsgs = null;
        List<String> serverSideSMCMsgs = null;

        TestUtil.logMsg("Get server side result back from endpoint");
        GetTrackerDataAction gtda = new GetTrackerDataAction();
        gtda.setAction("getArrayMessages2");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          serverSideMCMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        gtda.setAction("getArrayMessages3");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          serverSideSMCMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        TestUtil.logMsg("Verifying MessageContext callbacks on Server-Side");
        if (!Handler_Util.VerifyMessageContextCallBacks("Server", SOAP,
            serverSideMCMsgs)) {
          TestUtil
              .logErr("Server-Side MessageContext Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side MessageContext Callbacks are (correct)");
        }
        TestUtil
            .logMsg("Verifying SOAPMessageContext callbacks on Server-Side");
        if (!Handler_Util.VerifyLogicalOrSOAPMessageContextCallBacks("Server",
            SOAP, serverSideSMCMsgs)) {
          TestUtil.logErr(
              "Server-Side SOAPMessageContext Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil
              .logMsg("Server-Side SOAPMessageContext Callbacks are (correct)");
        }
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
      throw new Fault("ServerMessageContextTest failed");
  }

  /*
   * @testName: ContextPropertiesTest
   *
   * @assertion_ids: JAXWS:SPEC:9026; JAXWS:SPEC:9027; JAXWS:SPEC:9033;
   * JAXWS:SPEC:9034; JAXWS:SPEC:9035; JAXWS:SPEC:9036; JAXWS:SPEC:9037;
   * JAXWS:SPEC:9038; JAXWS:SPEC:9039; JAXWS:SPEC:9040; WS4EE:SPEC:6012;
   * WS4EE:SPEC:6013; WS4EE:SPEC:6014; WS4EE:SPEC:6008; WS4EE:SPEC:6002;
   * WS4EE:SPEC:6047;
   *
   * @test_Strategy: Invoke an RPC method and ensure that the various
   * MessageContext and SOAPMessageContext properties are accessible
   */
  public void ContextPropertiesTest() throws Fault {
    TestUtil.logTrace("ContextPropertiesTest");
    boolean pass = true;
    TestUtil.logMsg(
        "Programatically registering the client side handlers by creating new HandlerResolver.");
    service.setHandlerResolver(new HandlerResolver() {
      public List<Handler> getHandlerChain(PortInfo info) {
        List<Handler> handlerList = new ArrayList<Handler>();
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

        TestUtil.logMsg("Purging server-side tracker data");
        purgeServerSideTrackerData();

        TestUtil.logMsg("Invoking RPC method port1.doHandlerTest1()");
        MyActionType ma = new MyActionType();

        ma.setAction("ContextPropertiesTest");
        ma.setTestType(TEST_TYPE);
        ma.setHarnessloghost(harnessHost);
        ma.setHarnesslogport(harnessLogPort);
        ma.setHarnesslogtraceflag(harnessLogTraceFlag);

        URL url1;
        url1 = ctsurl.getURL("http", hostname, portnum,
            ctxroot + "/attach.text");
        TestUtil.logMsg("url1=" + url1);
        DataHandler dh1 = new DataHandler(url1);
        Holder<jakarta.activation.DataHandler> attach1 = new Holder<jakarta.activation.DataHandler>();
        attach1.value = dh1;
        MyResult2 mr = null;
        try {
          mr = port1.doHandlerAttachmentTest(ma, attach1);
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        if (mr != null) {
          String errors = mr.getErrors();
          if (errors != null && !errors.equals("")) {
            TestUtil.logErr(
                "ERROR: The following errors were reported by the endpoint:"
                    + errors);
            pass = false;
          }
        }

        List<String> lResults = null;
        if (mr != null) {
          lResults = mr.getResult();
          JAXWS_Util.dumpList(mr.getResult());
        }

        if (lResults != null) {

          if (JAXWS_Util.looseIndexOf(lResults,
              "Endpoint:MessageContext.INBOUND_MESSAGE_ATTACHMENTS=key[0]") == -1) {
            TestUtil.logErr(
                "The property MessageContext.INBOUND_MESSAGE_ATTACHMENTS did not contain an attachment in the endpoint");
            pass = false;
          } else {
            TestUtil.logTrace(
                "Found Endpoint:MessageContext.INBOUND_MESSAGE_ATTACHMENTS=key[0]");
          }
          if (JAXWS_Util.looseIndexOf(lResults,
              "Endpoint:MessageContext.INBOUND_MESSAGE_ATTACHMENTS=key[1]") >= 0) {
            TestUtil.logErr(
                "The property MessageContext.INBOUND_MESSAGE_ATTACHMENTS contained more than one attachment in the endpoint");
            pass = false;
          } else {
            TestUtil.logTrace(
                "Found Endpoint:MessageContext.INBOUND_MESSAGE_ATTACHMENTS=key[1]");
          }
          if (lResults.indexOf(
              "Endpoint:MessageContext.HTTP_REQUEST_METHOD=POST") == -1) {
            TestUtil.logErr(
                "The property MessageContext.HTTP_REQUEST_METHOD was not POST in the endpoint");
            pass = false;
          } else {
            TestUtil.logTrace(
                "Found Endpoint:MessageContext.HTTP_REQUEST_METHOD=POST");
          }
          if (JAXWS_Util.looseIndexOf(lResults,
              "Endpoint:MessageContext.HTTP_REQUEST_HEADERS=value[0]=") == -1) {
            TestUtil.logErr(
                "The property MessageContext.HTTP_REQUEST_HEADERS did not contain any headers in the endpoint");
            pass = false;
          } else {
            TestUtil.logTrace(
                "The property MessageContext.HTTP_REQUEST_HEADERS did contain headers in the endpoint");
          }
          if (lResults.indexOf(
              "Endpoint:MessageContext.HTTP_RESPONSE_HEADERS=null") == -1) {
            TestUtil.logErr(
                "The property MessageContext.HTTP_RESPONSE_HEADERS was not null in the endpoint");
            pass = false;
          } else {
            TestUtil.logTrace(
                "The property MessageContext.HTTP_RESPONSE_HEADERS was null in the endpoint");
          }
          if (lResults
              .indexOf("Endpoint:MessageContext.SERVLET_REQUEST=null") >= 0) {
            TestUtil.logErr(
                "The property MessageContext.SERVLET_REQUEST was null in the endpoint");
            pass = false;
          } else {
            TestUtil.logTrace(
                "The property MessageContext.SERVLET_REQUEST was not null in the endpoint");
          }
          if (lResults
              .indexOf("Endpoint:MessageContext.SERVLET_RESPONSE=null") >= 0) {
            TestUtil.logErr(
                "The property MessageContext.SERVLET_RESPONSE was null in the endpoint");
            pass = false;
          } else {
            TestUtil.logTrace(
                "The property MessageContext.SERVLET_RESPONSE was not null in the endpoint");
          }
          if (lResults
              .indexOf("Endpoint:MessageContext.SERVLET_CONTEXT=null") >= 0) {
            TestUtil.logErr(
                "The property MessageContext.SERVLET_CONTEXT was null in the endpoint");
            pass = false;
          } else {
            TestUtil.logTrace(
                "The property MessageContext.SERVLET_CONTEXT was not null in the endpoint");
          }
        }

        if (mr != null) {
          String endpointErrors = mr.getErrors();
          if (endpointErrors != null && !endpointErrors.equals("")) {
            TestUtil.logErr("Erors:" + endpointErrors);
            pass = false;
          }
        }

        TestUtil.logMsg("Get client side results back from Tracker");
        List<String> clientSideMCMsgs = HandlerTracker.getListMessages2();

        List<String> serverSideMCMsgs = null;

        TestUtil.logMsg("Get server side result back from endpoint");
        GetTrackerDataAction gtda = new GetTrackerDataAction();
        gtda.setAction("getArrayMessages2");
        gtda.setHarnessloghost(harnessHost);
        gtda.setHarnesslogport(harnessLogPort);
        gtda.setHarnesslogtraceflag(harnessLogTraceFlag);
        try {
          serverSideMCMsgs = port4.getTrackerData(gtda).getResult();
        } catch (Exception e) {
          TestUtil.logErr("Endpoint threw an exception:");
          TestUtil.printStackTrace(e);
          pass = false;
        }

        // verify client-side callbacks
        TestUtil.logMsg(
            "Verifying MessageContext and SOAPMessageContext propterty callbacks on Client-Side");
        if (!Handler_Util.VerifyStandardMessageContextPropertiesCallBacks(
            "Client", SOAP, clientSideMCMsgs)) {
          TestUtil
              .logErr("Client-Side MessageContext Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Client-Side MessageContext Callbacks are (correct)");
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

        TestUtil.logMsg(
            "Verifying MessageContext and SOAPMessageContext propterty callbacks on Server-Side");
        if (!Handler_Util.VerifyStandardMessageContextPropertiesCallBacks(
            "Server", SOAP, serverSideMCMsgs)) {
          TestUtil
              .logErr("Server-Side MessageContext Callbacks are (incorrect)");
          pass = false;
        } else {
          TestUtil.logMsg("Server-Side MessageContext Callbacks are (correct)");
        }

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
      throw new Fault("ContextPropertiesTest failed");
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
