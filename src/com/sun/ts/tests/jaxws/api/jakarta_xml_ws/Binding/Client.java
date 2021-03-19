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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws.Binding;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.*;
import jakarta.xml.ws.soap.*;
import javax.xml.namespace.QName;
import jakarta.xml.ws.handler.*;
import javax.xml.transform.Source;

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

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.api.jakarta_xml_ws.Binding.";

  private static final String SHARED_CLIENT_PKG = "com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.";

  private static final String NAMESPACEURI = "http://dlhandlerservice.org/wsdl";

  private static final String SERVICE_NAME = "DLHandlerService";

  private static final String PORT_NAME = "HelloPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private static final Class SERVICE_CLASS = com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.DLHandlerService.class;

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "dlhandlerservice.endpoint.1";

  private static final String WSDLLOC_URL = "dlhandlerservice.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  private Binding binding = null;

  private BindingProvider bp = null;

  private Dispatch dispatch = null;

  private Hello port = null;

  static DLHandlerService service = null;

  private void getPorts() throws Exception {
    TestUtil.logMsg("Get port  = " + PORT_NAME);
    port = (Hello) service.getPort(Hello.class);
    TestUtil.logMsg("port=" + port);
  }

  private void getPortsStandalone() throws Exception {
    getPorts();
    bp = (BindingProvider) port;
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortsJavaEE() throws Exception {
    TestUtil.logMsg("Obtaining service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    getPorts();
    TestUtil.logMsg("Get Target Endpoint Address for port=" + port);
    String url = JAXWS_Util.getTargetEndpointAddress(port);
    TestUtil.logMsg("Target Endpoint Address=" + url);
    bp = (BindingProvider) port;
  }

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
      Binding binding = null;
      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        TestUtil.logMsg("Create Service object");
        getTestURLs();
        service = (DLHandlerService) JAXWS_Util.getService(wsdlurl,
            SERVICE_QNAME, SERVICE_CLASS);
        getPortsStandalone();
      } else {
        getTestURLs();
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (DLHandlerService) getSharedObject();
        getPortsJavaEE();
      }
      TestUtil
          .logMsg("Create a Dispatch object for SOAP 1.1 over HTTP binding");
      dispatch = service.createDispatch(PORT_QNAME, Source.class,
          jakarta.xml.ws.Service.Mode.PAYLOAD);
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
   * @testName: getBindingIDTest
   *
   * @assertion_ids: JAXWS:JAVADOC:133;
   *
   * @test_Strategy:
   * 
   */
  public void getBindingIDTest() throws Fault {
    TestUtil.logTrace("getBindingIDTest");
    boolean pass = true;
    try {
      binding = dispatch.getBinding();
      TestUtil.logMsg("Dispatch object = " + dispatch);
      TestUtil.logMsg("Binding object = " + binding);
      String bindingID = binding.getBindingID();
      TestUtil.logMsg("bindingID=" + bindingID);
      if (!bindingID.equals(SOAPBinding.SOAP11HTTP_BINDING)) {
        TestUtil.logErr("bindingID is not expected SOAP11HTTP_BINDING");
        pass = false;
      } else
        TestUtil.logMsg("bindingID is expected SOAP11HTTP_BINDING");
    } catch (WebServiceException e) {
      TestUtil.logErr("Caught unexpected WebServiceException", e);
      pass = false;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("getBindingIDTest failed", e);
    }

    if (!pass)
      throw new Fault("getBindingIDTest failed");
  }

  /*
   * @testName: SetAndGetHandlerChainForDispatchObjTest
   *
   * @assertion_ids: JAXWS:JAVADOC:2; JAXWS:JAVADOC:3;
   *
   * @test_Strategy: Sets the handler chain for a protocol binding instance.
   * Gets the handler chain for a protocol binding instance.
   */
  public void SetAndGetHandlerChainForDispatchObjTest() throws Fault {
    TestUtil.logTrace("SetAndGetHandlerChainForDispatchObjTest");
    boolean pass = true;
    try {
      binding = dispatch.getBinding();
      TestUtil.logMsg("Dispatch object = " + dispatch);
      TestUtil.logMsg("Binding object = " + binding);
      TestUtil.logMsg(
          "Test setHandlerChain()/getHandlerChain() for Dispatch object");
      TestUtil.logMsg(
          "Create a handler chain for SOAP 1.1 over HTTP protocol binding");
      TestUtil.logMsg("List<Handler> hc = new ArrayList<Handler>()");
      List<Handler> hc = new ArrayList<Handler>();
      TestUtil
          .logMsg("Construct ClientLogicalHandler1 and add to HandlerChain");
      hc.add(
          new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler1());
      TestUtil
          .logMsg("Construct ClientLogicalHandler2 and add to HandlerChain");
      hc.add(
          new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler2());
      TestUtil
          .logMsg("Construct ClientLogicalHandler3 and add to HandlerChain");
      hc.add(
          new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler3());
      TestUtil.logMsg("Set handler chain for protocol binding instance");
      TestUtil
          .logMsg("Calling Binding.setHandlerChain(java.util.List<Handler>)");
      binding.setHandlerChain(hc);
      TestUtil
          .logMsg("Now get the handler chain for protocol binding instance");
      TestUtil
          .logMsg("Calling java.util.List<Handler> Binding.getHandlerChain()");
      List<Handler> hl = binding.getHandlerChain();
      TestUtil.logMsg("HandlerChainList=" + hl);
      TestUtil.logMsg("HandlerChainSize = " + hl.size());
      if (hl.size() != 3) {
        TestUtil.logErr("Wrong size returned for HandlerChain");
        TestUtil.logErr(
            "handlerchain1 size=" + hl.size() + ", handlerchain1 size=3");
        pass = false;
      }
      Class c1 = Class.forName(
          "com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler1");
      Class c2 = Class.forName(
          "com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler2");
      Class c3 = Class.forName(
          "com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler3");
      TestUtil.logMsg("Walk through HandlerChain and verify contents");
      for (Handler hi : hl) {
        Class c = (Class) hi.getClass();
        TestUtil.logMsg("Handler object = " + hi);
        TestUtil.logMsg("Class object = " + c);
        if (!c.equals(c1) && !c.equals(c2) && !c.equals(c3)) {
          TestUtil.logErr("Expected object1: " + c1);
          TestUtil.logErr("Expected object2: " + c2);
          TestUtil.logErr("Expected object3: " + c3);
          TestUtil.logErr("Unexpected object in chain: " + c);
          pass = false;
        }
      }
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Caught UnsupportedOperationException");
    } catch (WebServiceException e) {
      TestUtil.logErr("Caught unexpected WebServiceException", e);
      pass = false;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("SetAndGetHandlerChainForDispatchObjTest failed", e);
    }

    if (!pass)
      throw new Fault("SetAndGetHandlerChainForDispatchObjTest failed");
  }

  /*
   * @testName: SetAndGetHandlerChainForStubObjTest
   *
   * @assertion_ids: JAXWS:JAVADOC:2; JAXWS:JAVADOC:3;
   *
   * @test_Strategy: Sets the handler chain for a protocol binding instance.
   * Gets the handler chain for a protocol binding instance.
   */
  public void SetAndGetHandlerChainForStubObjTest() throws Fault {
    TestUtil.logTrace("SetAndGetHandlerChainForStubObjTest");
    boolean pass = true;
    try {
      binding = bp.getBinding();
      TestUtil.logMsg("Stub object = " + port);
      TestUtil.logMsg("Binding object = " + binding);
      TestUtil
          .logMsg("Test setHandlerChain()/getHandlerChain() for Stub object");
      TestUtil.logMsg(
          "Create a handler chain for SOAP 1.1 over HTTP protocol binding");
      TestUtil.logMsg("List<Handler> hc = new ArrayList<Handler>()");
      List<Handler> hc = new ArrayList<Handler>();
      TestUtil
          .logMsg("Construct ClientLogicalHandler1 and add to HandlerChain");
      hc.add(
          new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler1());
      TestUtil
          .logMsg("Construct ClientLogicalHandler2 and add to HandlerChain");
      hc.add(
          new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler2());
      TestUtil
          .logMsg("Construct ClientLogicalHandler3 and add to HandlerChain");
      hc.add(
          new com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler3());
      TestUtil.logMsg("Set handler chain for protocol binding instance");
      TestUtil
          .logMsg("Calling Binding.setHandlerChain(java.util.List<Handler>)");
      binding.setHandlerChain(hc);
      TestUtil
          .logMsg("Now get the handler chain for protocol binding instance");
      TestUtil
          .logMsg("Calling java.util.List<Handler> Binding.getHandlerChain()");
      List<Handler> hl = binding.getHandlerChain();
      TestUtil.logMsg("HandlerChainList=" + hl);
      TestUtil.logMsg("HandlerChainSize = " + hl.size());
      if (hl.size() != 3) {
        TestUtil.logErr("Wrong size returned for HandlerChain");
        TestUtil.logErr(
            "handlerchain1 size=" + hl.size() + ", handlerchain1 size=3");
        pass = false;
      }
      Class c1 = Class.forName(
          "com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler1");
      Class c2 = Class.forName(
          "com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler2");
      Class c3 = Class.forName(
          "com.sun.ts.tests.jaxws.sharedclients.dlhandlerclient.ClientLogicalHandler3");
      TestUtil.logMsg("Walk through HandlerChain and verify contents");
      for (Handler hi : hl) {
        Class c = (Class) hi.getClass();
        TestUtil.logMsg("Handler object = " + hi);
        TestUtil.logMsg("Class object = " + c);
        if (!c.equals(c1) && !c.equals(c2) && !c.equals(c3)) {
          TestUtil.logErr("Expected object1: " + c1);
          TestUtil.logErr("Expected object2: " + c2);
          TestUtil.logErr("Expected object3: " + c3);
          TestUtil.logErr("Unexpected object in chain: " + c);
          pass = false;
        }
      }
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Caught UnsupportedOperationException");
    } catch (WebServiceException e) {
      TestUtil.logErr("Caught unexpected WebServiceException", e);
      pass = false;
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("SetAndGetHandlerChainForStubObjTest failed", e);
    }

    if (!pass)
      throw new Fault("SetAndGetHandlerChainForStubObjTest failed");
  }
}
