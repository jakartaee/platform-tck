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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws.BindingProvider;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.*;
import jakarta.xml.ws.EndpointReference;
import jakarta.xml.ws.wsaddressing.W3CEndpointReference;
import jakarta.xml.ws.soap.*;
import javax.xml.namespace.QName;
import jakarta.xml.ws.handler.*;
import javax.xml.transform.Source;
import com.sun.ts.tests.jaxws.wsa.common.*;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.api.jakarta_xml_ws.BindingProvider.";

  private static final String SHARED_CLIENT_PKG = "com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.";

  private static final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloService";

  private static final String PORT_NAME = "HelloPort";

  private static final String PORT_TYPE = "Hello";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private QName PORT_TYPE_QNAME = new QName(NAMESPACEURI, PORT_TYPE);

  private String helloReq = "<HelloRequest xmlns=\"http://helloservice.org/types\"><argument>foo</argument></HelloRequest>";

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private static final String ENDPOINT_URL = "dlhelloservice.endpoint.1";

  private static final String WSDLLOC_URL = "dlhelloservice.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  private EndpointReference epr = null;

  private Binding binding = null;

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
      req.setArgument("foo");
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
   * @testName: getBindingForDispatchObjTest
   *
   * @assertion_ids: JAXWS:JAVADOC:4;
   *
   * @test_Strategy: Get the Binding for this binding provider.
   */
  public void getBindingForDispatchObjTest() throws Fault {
    TestUtil.logTrace("getBindingForDispatchObjTest");
    boolean pass = true;
    TestUtil.logMsg("Calling BindingProvider.getBinding() for Dispatch object");
    binding = dispatchSrc.getBinding();
    TestUtil.logMsg("Binding object=" + binding);
    if (binding == null) {
      TestUtil.logErr("getBinding() returned null");
      pass = false;
    } else
      TestUtil.logMsg("getBinding() returned Binding object: " + binding);
    if (!pass)
      throw new Fault("getBindingForDispatchObjTest failed");
  }

  /*
   * @testName: getRequestContextForDispatchObjTest
   *
   * @assertion_ids: JAXWS:JAVADOC:5;
   *
   * @test_Strategy: Get the context that is used to initialize the message
   * context for request messages.
   */
  public void getRequestContextForDispatchObjTest() throws Fault {
    TestUtil.logTrace("getRequestContextForDispatchObjTest");
    boolean pass = true;
    TestUtil.logMsg(
        "Calling BindingProvider.getRequestContext() for Dispatch object");
    java.util.Map<String, Object> requestContext = dispatchSrc
        .getRequestContext();
    if (requestContext == null) {
      TestUtil.logErr("getRequestContext() returned null");
      pass = false;
    } else {
      TestUtil.logMsg(
          "getRequestContext() returned java.util.Map<String,Object> object");
      TestUtil.logMsg("map size=" + requestContext.size());
      java.util.Iterator iterator = requestContext.keySet().iterator();
      StringBuffer names = new StringBuffer();
      while (iterator.hasNext()) {
        if (names.length() > 0)
          names.append("\n" + iterator.next());
        else
          names.append("" + iterator.next());
      }
      if (names.length() > 0)
        TestUtil.logMsg("Request property names are\n" + names.toString());
      else
        TestUtil.logMsg("There are no request properties set");
    }
    if (!pass)
      throw new Fault("getRequestContextForDispatchObjTest failed");
  }

  /*
   * @testName: getResponseContextForDispatchObjTest
   *
   * @assertion_ids: JAXWS:JAVADOC:6;
   *
   * @test_Strategy: Get the context that resulted from processing a response
   * message.
   */
  public void getResponseContextForDispatchObjTest() throws Fault {
    TestUtil.logTrace("getResponseContextForDispatchObjTest");
    boolean pass = true;
    TestUtil.logMsg(
        "Calling BindingProvider.getResponseContext() for Dispatch object");
    java.util.Map<String, Object> responseContext = dispatchSrc
        .getResponseContext();
    if (responseContext == null) {
      TestUtil.logErr("getResponseContext() returned null");
      pass = false;
    } else {
      TestUtil.logMsg(
          "getResponseContext() returned java.util.Map<String,Object> object");
      TestUtil.logMsg("map size=" + responseContext.size());
      java.util.Iterator iterator = responseContext.keySet().iterator();
      StringBuffer names = new StringBuffer();
      while (iterator.hasNext()) {
        if (names.length() > 0)
          names.append("\n" + iterator.next());
        else
          names.append("" + iterator.next());
      }
      if (names.length() > 0)
        TestUtil.logMsg("Response property names are\n" + names.toString());
      else
        TestUtil.logMsg("There are no response properties set");
    }
    if (!pass)
      throw new Fault("getResponseContextForDispatchObjTest failed");
  }

  /*
   * @testName: getBindingForStubObjTest
   *
   * @assertion_ids: JAXWS:SPEC:4009; JAXWS:SPEC:4010; JAXWS:JAVADOC:4;
   *
   * @test_Strategy: Get the Binding for this binding provider.
   */
  public void getBindingForStubObjTest() throws Fault {
    TestUtil.logTrace("getBindingForStubObjTest");
    boolean pass = true;
    TestUtil.logMsg("Calling BindingProvider.getBinding() for Stub object");
    binding = bpStub.getBinding();
    TestUtil.logMsg("Binding object=" + binding);
    if (binding == null) {
      TestUtil.logErr("getBinding() returned null");
      pass = false;
    } else
      TestUtil.logMsg("getBinding() returned Binding object: " + binding);
    if (!pass)
      throw new Fault("getBindingForStubObjTest failed");
  }

  /*
   * @testName: getRequestContextForStubObjTest
   *
   * @assertion_ids: JAXWS:SPEC:4009; JAXWS:SPEC:4010; JAXWS:JAVADOC:5;
   *
   * @test_Strategy: Get the context that is used to initialize the message
   * context for request messages.
   */
  public void getRequestContextForStubObjTest() throws Fault {
    TestUtil.logTrace("getRequestContextForStubObjTest");
    boolean pass = true;
    TestUtil
        .logMsg("Calling BindingProvider.getRequestContext() for Stub object");
    java.util.Map<String, Object> requestContext = bpStub.getRequestContext();
    if (requestContext == null) {
      TestUtil.logErr("getRequestContext() returned null");
      pass = false;
    } else {
      TestUtil.logMsg(
          "getRequestContext() returned java.util.Map<String,Object> object");
      TestUtil.logMsg("map size=" + requestContext.size());
      java.util.Iterator iterator = requestContext.keySet().iterator();
      StringBuffer names = new StringBuffer();
      while (iterator.hasNext()) {
        if (names.length() > 0)
          names.append("\n" + iterator.next());
        else
          names.append("" + iterator.next());
      }
      if (names.length() > 0)
        TestUtil.logMsg("Request property names are\n" + names.toString());
      else
        TestUtil.logMsg("There are no request properties set");
    }
    if (!pass)
      throw new Fault("getRequestContextForStubObjTest failed");
  }

  /*
   * @testName: getResponseContextForStubObjTest
   *
   * @assertion_ids: JAXWS:SPEC:4009; JAXWS:SPEC:4010; JAXWS:JAVADOC:6;
   *
   * @test_Strategy: Get the context that resulted from processing a response
   * message.
   */
  public void getResponseContextForStubObjTest() throws Fault {
    TestUtil.logTrace("getResponseContextForStubObjTest");
    boolean pass = true;
    TestUtil
        .logMsg("Calling BindingProvider.getResponseContext() for Stub object");
    java.util.Map<String, Object> responseContext = bpStub.getResponseContext();
    if (responseContext == null) {
      TestUtil.logErr("getResponseContext() returned null");
      pass = false;
    } else {
      TestUtil.logMsg(
          "getResponseContext() returned java.util.Map<String,Object> object");
      TestUtil.logMsg("map size=" + responseContext.size());
      java.util.Iterator iterator = responseContext.keySet().iterator();
      StringBuffer names = new StringBuffer();
      while (iterator.hasNext()) {
        if (names.length() > 0)
          names.append("\n" + iterator.next());
        else
          names.append("" + iterator.next());
      }
      if (names.length() > 0)
        TestUtil.logMsg("Response property names are\n" + names.toString());
      else
        TestUtil.logMsg("There are no response properties set");
    }
    if (!pass)
      throw new Fault("getResponseContextForStubObjTest failed");
  }

  /*
   * @testName: setStandardPropertiesTest
   *
   * @assertion_ids: JAXWS:SPEC:4005;
   *
   * @test_Strategy: Get the context that is used to initialize the message
   * context for request messages and set all the standard properties.
   */
  public void setStandardPropertiesTest() throws Fault {
    TestUtil.logTrace("setStandardPropertiesTest");
    boolean pass = true;
    TestUtil
        .logMsg("Calling BindingProvider.getRequestContext() for Stub object");
    java.util.Map<String, Object> requestContext = bpStub.getRequestContext();
    if (requestContext == null) {
      TestUtil.logErr("getRequestContext() returned null");
      pass = false;
    } else {
      TestUtil.logMsg(
          "getRequestContext() returned java.util.Map<String,Object> object");
      TestUtil.logMsg("Verify setting of all standard properties");
      requestContext.put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
          "endpoint-address");
      requestContext.put(BindingProvider.PASSWORD_PROPERTY, "password");
      requestContext.put(BindingProvider.USERNAME_PROPERTY, "username");
      requestContext.put(BindingProvider.SOAPACTION_URI_PROPERTY, "myuri");
      requestContext.put(BindingProvider.SOAPACTION_USE_PROPERTY,
          new Boolean("false"));
      requestContext.put(BindingProvider.SESSION_MAINTAIN_PROPERTY,
          new Boolean("false"));
      TestUtil.logMsg("map size=" + requestContext.size());
      java.util.Iterator iterator = requestContext.keySet().iterator();
      StringBuffer names = new StringBuffer();
      while (iterator.hasNext()) {
        if (names.length() > 0)
          names.append("\n" + iterator.next());
        else
          names.append("" + iterator.next());
      }
      if (names.length() > 0)
        TestUtil.logMsg("Request property names are\n" + names.toString());
      else
        TestUtil.logMsg("There are no request properties set");
    }
    if (!pass)
      throw new Fault("setStandardPropertiesTest failed");
  }

  /*
   * @testName: setNonStandardPropertiesTest
   *
   * @assertion_ids: JAXWS:SPEC:4007;
   *
   * @test_Strategy: Get the context that is used to initialize the message
   * context for request messages and set all the standard properties.
   */
  public void setNonStandardPropertiesTest() throws Fault {
    TestUtil.logTrace("setNonStandardPropertiesTest");
    boolean pass = true;
    TestUtil
        .logMsg("Calling BindingProvider.getRequestContext() for Stub object");
    java.util.Map<String, Object> requestContext = bpStub.getRequestContext();
    if (requestContext == null) {
      TestUtil.logErr("getRequestContext() returned null");
      pass = false;
    } else {
      TestUtil.logMsg(
          "getRequestContext() returned java.util.Map<String,Object> object");
      TestUtil.logMsg("Verify setting of a non standard properties");
      requestContext.put("foobar.property", "foobar");
      TestUtil.logMsg("map size=" + requestContext.size());
      java.util.Iterator iterator = requestContext.keySet().iterator();
      StringBuffer names = new StringBuffer();
      while (iterator.hasNext()) {
        if (names.length() > 0)
          names.append("\n" + iterator.next());
        else
          names.append("" + iterator.next());
      }
      if (names.length() > 0)
        TestUtil.logMsg("Request property names are\n" + names.toString());
      else
        TestUtil.logMsg("There are no request properties set");
    }
    if (!pass)
      throw new Fault("setNonStandardPropertiesTest failed");
  }

  /*
   * @testName: getEndpointReferenceForDispatchObjTest
   *
   * @assertion_ids: JAXWS:JAVADOC:186; JAXWS:SPEC:5023; JAXWS:SPEC:4022;
   * WSAMD:SPEC:2000.2; WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2;
   * WSAMD:SPEC:2001.3; WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2;
   * WSAMD:SPEC:2002.3; WSAMD:SPEC:2002.4; JAXWS:SPEC:4033;
   *
   * @test_Strategy: Get the EndpointReference for this binding provider.
   * Validate the EndpointReference (EPR) WSDL MetaData.
   */
  public void getEndpointReferenceForDispatchObjTest() throws Fault {
    TestUtil.logTrace("getEndpointReferenceForDispatchObjTest");
    boolean pass = true;
    TestUtil.logMsg(
        "Calling BindingProvider.getEndpointReference() for Dispatch object");
    epr = dispatchSrc.getEndpointReference();
    TestUtil.logMsg("EndpointReference object=" + epr);
    if (epr == null) {
      TestUtil.logErr("getEndpointReference() returned null");
      pass = false;
    } else {
      TestUtil.logMsg(
          "getEndpointReference() returned EndpointReference object: " + epr);
      pass = EprUtil.validateEPR(epr, url, SERVICE_QNAME, PORT_QNAME,
          PORT_TYPE_QNAME, Boolean.FALSE);
    }
    if (!pass)
      throw new Fault("getEndpointReferenceForDispatchObjTest failed");
  }

  /*
   * @testName: getEndpointReferenceForStubObjTest
   *
   * @assertion_ids: JAXWS:JAVADOC:186; JAXWS:SPEC:5023; JAXWS:SPEC:4022;
   * JAXWS:SPEC:4023; WSAMD:SPEC:2000.2; WSAMD:SPEC:2001; WSAMD:SPEC:2001.1;
   * WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3; WSAMD:SPEC:2002; WSAMD:SPEC:2002.1;
   * WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3; WSAMD:SPEC:2002.4; JAXWS:SPEC:4033;
   *
   * @test_Strategy: Get the EndpointReference for this binding provider.
   * Validate the EndpointReference (EPR) WSDL MetaData.
   */
  public void getEndpointReferenceForStubObjTest() throws Fault {
    TestUtil.logTrace("getEndpointReferenceForStubObjTest");
    boolean pass = true;
    TestUtil.logMsg(
        "Calling BindingProvider.getEndpointReference() for Stub object");
    epr = bpStub.getEndpointReference();
    TestUtil.logMsg("EndpointReference object=" + epr);
    if (epr == null) {
      TestUtil.logErr("getEndpointReference() returned null");
      pass = false;
    } else {
      TestUtil.logMsg(
          "getEndpointReference() returned EndpointReference object: " + epr);
      pass = EprUtil.validateEPR(epr, url, SERVICE_QNAME, PORT_QNAME,
          PORT_TYPE_QNAME, Boolean.FALSE);
    }
    if (epr instanceof W3CEndpointReference)
      TestUtil.logMsg("epr instanceof W3CEndpointReference");
    else {
      TestUtil.logErr("epr not instanceof W3CEndpointReference");
      pass = false;
    }
    if (!pass)
      throw new Fault("getEndpointReferenceForStubObjTest failed");
  }

  /*
   * @testName: getEndpointReference2ForDispatchObjTest
   *
   * @assertion_ids: JAXWS:JAVADOC:187; JAXWS:SPEC:4022; JAXWS:SPEC:4023;
   * WSAMD:SPEC:2000.2; WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2;
   * WSAMD:SPEC:2001.3; WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2;
   * WSAMD:SPEC:2002.3; WSAMD:SPEC:2002.4; JAXWS:SPEC:4033;
   *
   * @test_Strategy: Get the EndpointReference for this binding provider.
   * Validate the EndpointReference (EPR) WSDL MetaData.
   */
  public void getEndpointReference2ForDispatchObjTest() throws Fault {
    TestUtil.logTrace("getEndpointReference2ForDispatchObjTest");
    boolean pass = true;
    TestUtil.logMsg(
        "Calling BindingProvider.getEndpointReference(Class) for Dispatch object");
    epr = dispatchSrc.getEndpointReference(W3CEndpointReference.class);
    TestUtil.logMsg("EndpointReference object=" + epr);
    if (epr == null) {
      TestUtil.logErr("getEndpointReference() returned null");
      pass = false;
    } else {
      TestUtil.logMsg(
          "getEndpointReference() returned EndpointReference object: " + epr);
      pass = EprUtil.validateEPR(epr, url, SERVICE_QNAME, PORT_QNAME,
          PORT_TYPE_QNAME, Boolean.FALSE);
    }
    if (epr instanceof W3CEndpointReference)
      TestUtil.logMsg("epr instanceof W3CEndpointReference");
    else {
      TestUtil.logErr("epr not instanceof W3CEndpointReference");
      pass = false;
    }
    if (!pass)
      throw new Fault("getEndpointReference2ForDispatchObjTest failed");
  }

  /*
   * @testName: getEndpointReference2ForStubObjTest
   *
   * @assertion_ids: JAXWS:JAVADOC:187; JAXWS:SPEC:4022; JAXWS:SPEC:4023;
   * WSAMD:SPEC:2000.2; WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2;
   * WSAMD:SPEC:2001.3; WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2;
   * WSAMD:SPEC:2002.3; WSAMD:SPEC:2002.4; JAXWS:SPEC:4033;
   *
   * @test_Strategy: Get the EndpointReference for this binding provider.
   * Validate the EndpointReference (EPR) WSDL MetaData.
   */
  public void getEndpointReference2ForStubObjTest() throws Fault {
    TestUtil.logTrace("getEndpointReference2ForStubObjTest");
    boolean pass = true;
    TestUtil.logMsg(
        "Calling BindingProvider.getEndpointReference(Class) for Stub object");
    epr = bpStub.getEndpointReference(W3CEndpointReference.class);
    TestUtil.logMsg("EndpointReference object=" + epr);
    if (epr == null) {
      TestUtil.logErr("getEndpointReference() returned null");
      pass = false;
    } else {
      TestUtil.logMsg(
          "getEndpointReference() returned EndpointReference object: " + epr);
      pass = EprUtil.validateEPR(epr, url, SERVICE_QNAME, PORT_QNAME,
          PORT_TYPE_QNAME, Boolean.FALSE);
    }
    if (epr instanceof W3CEndpointReference)
      TestUtil.logMsg("epr instanceof W3CEndpointReference");
    else {
      TestUtil.logErr("epr not instanceof W3CEndpointReference");
      pass = false;
    }
    if (!pass)
      throw new Fault("getEndpointReference2ForStubObjTest failed");
  }
}
