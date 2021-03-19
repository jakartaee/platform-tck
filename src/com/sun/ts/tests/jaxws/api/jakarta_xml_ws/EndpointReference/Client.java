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
 * $Id: Client.java 51361 2006-10-10 21:21:31Z jbenoit $
 */

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws.EndpointReference;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import java.text.MessageFormat;

import jakarta.xml.ws.EndpointReference;
import jakarta.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import com.sun.javatest.Status;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsa.common.EprUtil;

import jakarta.xml.ws.*;
import jakarta.xml.ws.wsaddressing.W3CEndpointReference;
import jakarta.xml.ws.soap.*;
import javax.xml.namespace.QName;
import jakarta.xml.ws.handler.*;

import javax.naming.InitialContext;

import javax.xml.transform.dom.*;
import org.w3c.dom.Node;

public class Client extends ServiceEETest {

  private static String xmlSource = "<EndpointReference xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{0}</Address><Metadata xmlns:wsdli=\"http://www.w3.org/ns/wsdl-instance\" wsdli:wsdlLocation=\"http://helloservice.org/wsdl {1}\"><wsam:InterfaceName xmlns:wsam=\"http://www.w3.org/2007/05/addressing/metadata\" xmlns:wsns=\"http://helloservice.org/wsdl\">wsns:Hello</wsam:InterfaceName><wsam:ServiceName xmlns:wsam=\"http://www.w3.org/2007/05/addressing/metadata\" xmlns:ns3=\"http://www.w3.org/2005/08/addressing\" xmlns=\"\" xmlns:wsns=\"http://helloservice.org/wsdl\" EndpointName=\"HelloPort\">wsns:HelloService</wsam:ServiceName></Metadata></EndpointReference>";

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.api.jakarta_xml_ws.BindingProvider.";

  private static final String SHARED_CLIENT_PKG = "com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.";

  private static final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloService";

  private static final String PORT_NAME = "HelloPort";

  private static final String PORT_TYPE_NAME = "Hello";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private QName PORT_TYPE_QNAME = new QName(NAMESPACEURI, PORT_TYPE_NAME);

  private String helloReq = "<HelloRequest xmlns=\"http://helloservice.org/types\"><argument>foo</argument></HelloRequest>";

  private String foo_arg = "foo";

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private static final String ENDPOINT_URL = "dlhelloservice.endpoint.1";

  private static final String WSDLLOC_URL = "dlhelloservice.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  private EndpointReference epr = null;

  private BindingProvider bpStub = null;

  private Dispatch<Source> dispatchSrc = null;

  private Hello port = null;

  private static final Class SERVICE_CLASS = com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.HelloService.class;

  static HelloService service = null;

  private Dispatch<Source> createDispatchSrc(QName port, Class type,
      jakarta.xml.ws.Service.Mode mode) {
    TestUtil.logMsg("Create a Dispatch object for SOAP 1.1 over HTTP binding");
    return service.createDispatch(port, type, mode);
  }

  private void getPorts() throws Exception {
    TestUtil.logMsg("Get port  = " + PORT_NAME);
    port = (Hello) service.getPort(Hello.class);
    TestUtil.logMsg("port=" + port);
  }

  private void getPortsStandalone() throws Exception {
    getPorts();
    bpStub = (BindingProvider) port;
    dispatchSrc = createDispatchSrc(PORT_QNAME, Source.class,
        jakarta.xml.ws.Service.Mode.PAYLOAD);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortsJavaEE() throws Exception {
    TestUtil.logMsg("Obtaining service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    getPorts();
    bpStub = (BindingProvider) port;
    TestUtil.logMsg("Get Target Endpoint Address for port=" + port);
    String url = JAXWS_Util.getTargetEndpointAddress(port);
    TestUtil.logMsg("Target Endpoint Address=" + url);
    dispatchSrc = service.createDispatch(PORT_QNAME, Source.class,
        jakarta.xml.ws.Service.Mode.PAYLOAD);
  }

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
    xmlSource = MessageFormat.format(xmlSource, url, wsdlurl.toString());
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
        service = (HelloService) JAXWS_Util.getService(wsdlurl, SERVICE_QNAME,
            SERVICE_CLASS);
        getPortsStandalone();
      } else {
        getTestURLs();
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (HelloService) getSharedObject();
        getPortsJavaEE();
      }
      HelloRequest req = new HelloRequest();
      req.setArgument(foo_arg);
      TestUtil.logMsg("invoking hello through stub");
      port.hello(req);
      Source reqMsg = JAXWS_Util.makeSource(helloReq, "StreamSource");
      TestUtil.logMsg("invoking hello through dispatch");
      dispatchSrc.invoke(reqMsg);
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
   * @testName: readFromTest
   *
   * @assertion_ids: JAXWS:JAVADOC:141; JAXWS:JAVADOC:178;
   *
   * @test_Strategy: Test factory method to read an EndpointReference from the
   * infoset contained in eprInfoset.
   */
  public void readFromTest() throws Fault {
    TestUtil.logTrace("readFromTest");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Create instance via EndpointReference.readFrom(source) ...");
      EndpointReference result = EndpointReference
          .readFrom(JAXWS_Util.makeSource(xmlSource, "StreamSource"));
      if (result != null) {
        if (!(result instanceof W3CEndpointReference)) {
          TestUtil.logErr(
              "W3CEndpointReference created, but not instanceof W3CEndpointReference");
          pass = false;
        } else {
          TestUtil.logMsg("W3CEndpointReference object created successfully");
        }
      } else {
        TestUtil.logErr("W3CEndpointReference object not created");
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("readFromTest failed", e);
    }

    if (!pass) {
      throw new Fault("readFromTest failed");
    }
  }

  /*
   * @testName: toStringTest
   *
   * @assertion_ids: JAXWS:JAVADOC:188;
   *
   * @test_Strategy:
   */
  public void toStringTest() throws Fault {
    TestUtil.logTrace("toStringTest");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Create instance via EndpointReference.readFrom(source) ...");
      EndpointReference epr = EndpointReference
          .readFrom(JAXWS_Util.makeSource(xmlSource, "StreamSource"));
      if (epr != null) {
        if (!(epr instanceof W3CEndpointReference)) {
          TestUtil.logErr(
              "W3CEndpointReference created, but not instanceof W3CEndpointReference");
          pass = false;
        } else {
          TestUtil.logMsg("epr.toString(): " + epr.toString());
          TestUtil.logMsg(
              "Now perform an epr.readFrom() of the results from epr.toString()");
          epr = EndpointReference
              .readFrom(JAXWS_Util.makeSource(epr.toString(), "StreamSource"));
          TestUtil.logMsg("Validate the EPR for correctness (Verify MetaData)");
          if (!EprUtil.validateEPR(epr, url, SERVICE_QNAME, PORT_QNAME,
              PORT_TYPE_QNAME, Boolean.TRUE)) {
            pass = false;
            TestUtil
                .logErr("toString failed to write out xml source as expected");
          } else
            TestUtil
                .logMsg("toString passed to write out xml source as expected");
        }
      } else {
        TestUtil.logErr("W3CEndpointReference object not created");
        pass = false;
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("toStringTest failed", e);
    }

    if (!pass) {
      throw new Fault("toStringTest failed");
    }
  }

  /*
   * @testName: getPortFromEndpointReferenceForDispatchObjTest
   *
   * @assertion_ids: JAXWS:JAVADOC:140;
   *
   * @test_Strategy: Get the port from the EndpointReference for this binding
   * provider. Verify method invocation works from the port obtained from the
   * EPR.
   */
  public void getPortFromEndpointReferenceForDispatchObjTest() throws Fault {
    TestUtil.logTrace("getPortFromEndpointReferenceForDispatchObjTest");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Calling BindingProvider.getEndpointReference() for Dispatch object");
      epr = dispatchSrc.getEndpointReference();
      pass = processEprFromDispatch(epr);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getPortFromEndpointReferenceForDispatchObjTest failed",
          e);
    }
    if (!pass) {
      throw new Fault("getPortFromEndpointReferenceForDispatchObjTest failed");
    }
  }

  /*
   * @testName: getPortFromEndpointReferenceForStubObjTest
   *
   * @assertion_ids: JAXWS:JAVADOC:140;
   *
   * @test_Strategy: Get the port from the EndpointReference for this binding
   * provider. Verify method invocation works from the port obtained from the
   * EPR.
   */
  public void getPortFromEndpointReferenceForStubObjTest() throws Fault {
    TestUtil.logTrace("getPortFromEndpointReferenceForStubObjTest");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Calling BindingProvider.getEndpointReference() for Stub object");
      epr = bpStub.getEndpointReference();
      pass = processEprFromStub(epr);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getPortFromEndpointReferenceForDispatchObjTest failed",
          e);
    }
    if (!pass) {
      throw new Fault("getPortFromEndpointReferenceForStubObjTest failed");
    }
  }

  /*
   * @testName: getPortFromEndpointReference2ForDispatchObjTest
   *
   * @assertion_ids: JAXWS:JAVADOC:140; JAXWS:SPEC:4028; JAXWS:SPEC:4028.1;
   *
   * @test_Strategy: Get the port from the EndpointReference for this binding
   * provider. Verify method invocation works from the port obtained from the
   * EPR.
   */
  public void getPortFromEndpointReference2ForDispatchObjTest() throws Fault {
    TestUtil.logTrace("getPortFromEndpointReference2ForDispatchObjTest");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Calling BindingProvider.getEndpointReference(Class) for Dispatch object");
      epr = dispatchSrc.getEndpointReference(W3CEndpointReference.class);
      pass = processEprFromDispatch(epr);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getPortFromEndpointReference2ForDispatchObjTest failed",
          e);
    }
    if (!pass) {
      throw new Fault("getPortFromEndpointReference2ForDispatchObjTest failed");
    }
  }

  /*
   * @testName: getPortFromEndpointReference2ForStubObjTest
   *
   * @assertion_ids: JAXWS:JAVADOC:140; JAXWS:SPEC:4028; JAXWS:SPEC:4028.1;
   *
   * @test_Strategy: Get the port from the EndpointReference for this binding
   * provider. Verify method invocation works from the port obtained from the
   * EPR.
   */
  public void getPortFromEndpointReference2ForStubObjTest() throws Fault {
    TestUtil.logTrace("getPortFromEndpointReference2ForStubObjTest");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Calling BindingProvider.getEndpointReference(Class) for Stub object");
      epr = bpStub.getEndpointReference(W3CEndpointReference.class);
      TestUtil.logMsg("EndpointReference object=" + epr);
      pass = processEprFromStub(epr);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("getPortFromEndpointReference2ForStubObjTest failed", e);
    }
    if (!pass) {
      throw new Fault("getPortFromEndpointReference2ForStubObjTest failed");
    }
  }

  private boolean processEprFromStub(EndpointReference epr) {
    TestUtil.logTrace("processEprFromStub");
    boolean pass = true;
    try {
      TestUtil.logMsg("EndpointReference object=" + epr);
      if (epr == null) {
        TestUtil.logErr("getEndpointReference() returned null");
        pass = false;
      } else {
        TestUtil.logMsg(
            "getEndpointReference() returned EndpointReference object: " + epr);
        if (!(epr instanceof W3CEndpointReference)) {
          TestUtil.logErr(
              "W3CEndpointReference created, but not instanceof W3CEndpointReference");
          pass = false;
        } else {
          port = (Hello) epr.getPort(Hello.class);
          if (port == null) {
            TestUtil.logErr("EndpointReference.getPort(Class) returned null");
            pass = false;
          } else {
            // INVOKE A METHOD AND VERIFY RESULT
            TestUtil.logMsg("getPort() returned Hello port as expected");
            HelloRequest req = new HelloRequest();
            req.setArgument(foo_arg);
            TestUtil.logMsg("invoking hello through stub");
            HelloResponse res = port.hello(req);
            if (res != null) {
              // TEST PASSES if expected RESULT otherwise FAIL
              TestUtil.logMsg("HelloRequest: " + req.getArgument());
              TestUtil.logMsg("HelloResponse: " + res.getArgument());
              // TEST PASSES if expected RESULT otherwise FAIL
              if (!req.getArgument().equals(res.getArgument())) {
                TestUtil.logErr("Expected [" + req.getArgument()
                    + "] Received response [" + res.getArgument() + "]");
                pass = false;
              } else {
                TestUtil.logMsg("Port method invocation successful, Expected ["
                    + req.getArgument()
                    + "] Received response Received response ["
                    + res.getArgument() + "]");
              }
            } else {
              TestUtil.logErr("HelloResponse is null from stub");
              pass = false;
            }
          }
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean processEprFromDispatch(EndpointReference epr) {
    TestUtil.logTrace("processEprFromDispatch");
    boolean pass = true;
    try {
      TestUtil.logMsg("EndpointReference object=" + epr);
      if (epr == null) {
        TestUtil.logErr("getEndpointReference() returned null");
        pass = false;
      } else {
        TestUtil.logMsg(
            "getEndpointReference() returned EndpointReference object: " + epr);
        if (!(epr instanceof W3CEndpointReference)) {
          TestUtil.logErr(
              "W3CEndpointReference created, but not instanceof W3CEndpointReference");
          pass = false;
        } else {
          port = (Hello) epr.getPort(Hello.class);
          if (port == null) {
            TestUtil.logErr("EndpointReference.getPort(Class) returned null");
            pass = false;
          } else {
            // INVOKE A METHOD AND VERIFY RESULT
            TestUtil.logMsg("getPort() returned Hello port as expected");
            Source reqMsg = JAXWS_Util.makeSource(helloReq, "StreamSource");
            TestUtil.logMsg("invoking hello through dispatch");
            Source resMsg = dispatchSrc.invoke(reqMsg);
            // TEST PASSES if expected RESULT otherwise FAIL
            /*
             * expected RESULT=<HelloResponse
             * xmlns="http://helloservice.org/types"><argument>foo</{http://
             * helloservice.org/types}argument></{http://helloservice.org/types}
             * HelloResponse>
             */
            try {
              DOMResult dr = JAXWS_Util.getSourceAsDOMResult(resMsg);
              TestUtil.logMsg("Returned Response Source="
                  + JAXWS_Util.getDOMResultAsString(dr));
              Node documentNode = dr.getNode();
              Node requestResponseNode = documentNode.getFirstChild();
              TestUtil.logMsg("received requestResponseNode localname ["
                  + requestResponseNode.getLocalName() + "]");
              if (!requestResponseNode.getLocalName()
                  .startsWith("HelloResponse")) {
                TestUtil
                    .logMsg("The expected response messages were not received");
                pass = false;
              }
              // The first child is the test name the second(last) is the
              // argument
              Node argumentNode = requestResponseNode.getLastChild();
              // check for argument element
              String argumentElement = argumentNode.getLocalName();
              if (argumentElement.equals("argument")) {
                TestUtil.logMsg(
                    "Method invoked and returned with correct argument value ["
                        + argumentElement + "]");
              } else {
                TestUtil.logMsg(
                    "Method invoked and returned with incorrect param value, Expected [argument] but received ["
                        + argumentElement + "]");
                pass = false;
              }
              // check for "foo" argument string as value of argument element
              // node
              Node textNode = argumentNode.getFirstChild();
              String item = textNode.getNodeValue();
              if (foo_arg.equals(item)) {
                TestUtil.logMsg(
                    "Method invoked and returned with correct param value ["
                        + item + "]");
              } else {
                TestUtil.logMsg(
                    "Method invoked and returned with incorrect param value, Expected ["
                        + foo_arg + "] but received [" + item + "]");
                pass = false;
              }
            } catch (Exception e) {
              e.printStackTrace();
              pass = false;
            }
          }
        }
      }

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }
}
