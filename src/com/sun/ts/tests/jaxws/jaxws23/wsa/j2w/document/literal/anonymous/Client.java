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

package com.sun.ts.tests.jaxws.jaxws23.wsa.j2w.document.literal.anonymous;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsa.common.*;
import com.sun.javatest.Status;

import java.text.MessageFormat;
import java.net.*;
import java.io.*;
import java.util.UUID;
import jakarta.xml.ws.*;
import jakarta.xml.ws.soap.*;
import jakarta.xml.soap.*;
import java.util.Properties;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Exchanger;

/*
 * Test Repeatable annotation on WebServiceRef
 * @Repeatable(value=WebServiceRefs.class)
 */
public class Client extends ServiceEETest {

  private static final long serialVersionUID = 23L;

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String ENDPOINTPUBLISHPROP = "http.server.supports.endpoint.publish";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private boolean endpointPublishSupport;

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.jaxws23.wsa.j2w.document.literal.anonymous.";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private static final String MINPORT = "port.range.min";

  private int minPort = -1;

  private static final String MAXPORT = "port.range.max";

  private int maxPort = -1;

  private int javaseServerPort;

  // URL properties used by the test
  private static final String ENDPOINT_URL23001 = "wsaj2wdlanonymoustest23.endpoint.23001";

  private static final String WSDLLOC_URL23001 = "wsaj2wdlanonymoustest23.wsdlloc.23001";

  private static final String ENDPOINT_URL23002 = "wsaj2wdlanonymoustest23.endpoint.23002";

  private static final String WSDLLOC_URL23002 = "wsaj2wdlanonymoustest23.wsdlloc.23002";

  // NonAnonymousProcessor's
  private static final String NONANONYMOUSPROCESSOR = "/NonAnonymousProcessor";

  private static final String NONANONYMOUSPROCESSOR2 = "/NonAnonymousProcessor2";

  // service and port information
  private static final String NAMESPACEURI = "http://example.com/";

  private static final String TARGET_NAMESPACE = NAMESPACEURI;

  private static final String SERVICE_NAME23001 = "AddNumbersService23001";

  private static final String SERVICE_NAME23002 = "AddNumbersService23002";

  private static final String PORT_NAME23001 = "AddNumbersPort23001";

  private static final String PORT_NAME23002 = "AddNumbersPort23002";

  private static QName SERVICE_QNAME23001 = new QName(NAMESPACEURI,
      SERVICE_NAME23001);

  private static QName SERVICE_QNAME23002 = new QName(NAMESPACEURI,
      SERVICE_NAME23002);

  private static QName PORT_QNAME23001 = new QName(NAMESPACEURI,
      PORT_NAME23001);

  private static QName PORT_QNAME23002 = new QName(NAMESPACEURI,
      PORT_NAME23002);

  private static AddressingFeature ENABLED_ADDRESSING_FEATURE = new AddressingFeature(
      true, true);

  private static AddressingFeature DISABLED_ADDRESSING_FEATURE = new AddressingFeature(
      false);

  private String file23001 = null;

  private String file23002 = null;

  private String urlToNonAnonymousProcessor = null;

  private String urlToNonAnonymousProcessor2 = null;

  private String url23001 = null;

  private String url23002 = null;

  private URL wsdlurl23001 = null;

  private URL wsdlurl23002 = null;

  AddNumbersPortType23001 port23001 = null;

  AddNumbersPortType23002 port23002 = null;

  static AddNumbersService23001 service23001 = null;

  static AddNumbersService23002 service23002 = null;

  String ReplyToHeaderForAnonymousResponsesSoapMsg = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header><To xmlns=\"http://www.w3.org/2005/08/addressing\">{0}</To><MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:{1}</MessageID><ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{2}</Address></ReplyTo><Action xmlns=\"http://www.w3.org/2005/08/addressing\">http://example.com/AddNumbersPortType23001/add</Action></S:Header><S:Body><addNumbers xmlns=\"http://example.com/\"><number1>10</number1><number2>10</number2><testName>testAnonymousResponsesReplyToHeader</testName></addNumbers></S:Body></S:Envelope>";

  String FaultToHeaderForAnonymousResponsesSoapMsg = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header><To xmlns=\"http://www.w3.org/2005/08/addressing\">{0}</To><MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:{1}</MessageID><FaultTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{2}</Address></FaultTo><ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{3}</Address></ReplyTo><Action xmlns=\"http://www.w3.org/2005/08/addressing\">http://example.com/AddNumbersPortType23001/add</Action></S:Header><S:Body><addNumbers xmlns=\"http://example.com/\"><number1>-10</number1><number2>-10</number2><testName>testAnonymousResponsesFaultToHeader</testName></addNumbers></S:Body></S:Envelope>";

  String ReplyToHeaderForNonAnonymousResponsesSoapMsg = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header><To xmlns=\"http://www.w3.org/2005/08/addressing\">{0}</To><MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:{1}</MessageID><ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{2}</Address></ReplyTo><Action xmlns=\"http://www.w3.org/2005/08/addressing\">http://example.com/AddNumbersPortType23002/add</Action></S:Header><S:Body><addNumbers xmlns=\"http://example.com/\"><number1>10</number1><number2>10</number2><testName>testNonAnonymousResponsesReplyToHeader</testName></addNumbers></S:Body></S:Envelope>";

  String FaultToHeaderForNonAnonymousResponsesSoapMsg = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header><To xmlns=\"http://www.w3.org/2005/08/addressing\">{0}</To><MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:{1}</MessageID><FaultTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{2}</Address></FaultTo><ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{3}</Address></ReplyTo><Action xmlns=\"http://www.w3.org/2005/08/addressing\">http://example.com/AddNumbersPortType23002/add</Action></S:Header><S:Body><addNumbers xmlns=\"http://example.com/\"><number1>-10</number1><number2>-10</number2><testName>testNonAnonymousResponsesFaultToHeader</testName></addNumbers></S:Body></S:Envelope>";

  String TestNonAnonymousResponsesAssertionSoapMsg = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header><To xmlns=\"http://www.w3.org/2005/08/addressing\">{0}</To><MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:{1}</MessageID><ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{2}</Address></ReplyTo><Action xmlns=\"http://www.w3.org/2005/08/addressing\">http://example.com/AddNumbersPortType23002/add</Action></S:Header><S:Body><addNumbers xmlns=\"http://example.com/\"><number1>10</number1><number2>10</number2><testName>testNonAnonymousResponsesAssertion</testName></addNumbers></S:Body></S:Envelope>";

  private Dispatch<SOAPMessage> createDispatchSOAPMessage(Service service,
      QName port) throws Exception {
    return service.createDispatch(port, SOAPMessage.class,
        jakarta.xml.ws.Service.Mode.MESSAGE, DISABLED_ADDRESSING_FEATURE);
  }

  private Dispatch<SOAPMessage> createAnonymousResponsesDispatch() {
    return service23001.createDispatch(PORT_QNAME23001, SOAPMessage.class,
        Service.Mode.MESSAGE, ENABLED_ADDRESSING_FEATURE);
  }

  private Dispatch<SOAPMessage> createNonAnonymousResponsesDispatch() {
    return service23002.createDispatch(PORT_QNAME23002, SOAPMessage.class,
        Service.Mode.MESSAGE, ENABLED_ADDRESSING_FEATURE);
  }

  private Dispatch<SOAPMessage> createAnonymousResponsesDispatchWithoutAddressing() {
    return service23001.createDispatch(PORT_QNAME23001, SOAPMessage.class,
        Service.Mode.MESSAGE, DISABLED_ADDRESSING_FEATURE);
  }

  private Dispatch<SOAPMessage> createNonAnonymousResponsesDispatchWithoutAddressing() {
    return service23002.createDispatch(PORT_QNAME23002, SOAPMessage.class,
        Service.Mode.MESSAGE, DISABLED_ADDRESSING_FEATURE);
  }

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    file23001 = JAXWS_Util.getURLFromProp(ENDPOINT_URL23001);
    url23001 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file23001);
    file23002 = JAXWS_Util.getURLFromProp(ENDPOINT_URL23002);
    url23002 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file23002);
    if (endpointPublishSupport) {
      urlToNonAnonymousProcessor = ctsurl.getURLString(PROTOCOL, hostname,
          javaseServerPort, NONANONYMOUSPROCESSOR);
      urlToNonAnonymousProcessor2 = ctsurl.getURLString(PROTOCOL, hostname,
          javaseServerPort, NONANONYMOUSPROCESSOR2);
    } else {
      urlToNonAnonymousProcessor = ctsurl.getURLString(PROTOCOL, hostname,
          portnum, NONANONYMOUSPROCESSOR);
      urlToNonAnonymousProcessor2 = ctsurl.getURLString(PROTOCOL, hostname,
          portnum, NONANONYMOUSPROCESSOR2);
    }

    file23001 = JAXWS_Util.getURLFromProp(WSDLLOC_URL23001);
    wsdlurl23001 = ctsurl.getURL(PROTOCOL, hostname, portnum, file23001);
    file23002 = JAXWS_Util.getURLFromProp(WSDLLOC_URL23002);
    wsdlurl23002 = ctsurl.getURL(PROTOCOL, hostname, portnum, file23002);
    TestUtil.logMsg(
        "NonAnonymousProcessor Endpoint: " + urlToNonAnonymousProcessor);
    TestUtil.logMsg(
        "NonAnonymousProcessor2 Endpoint: " + urlToNonAnonymousProcessor2);
    TestUtil.logMsg("Service Endpoint URL23001: " + url23001);
    TestUtil.logMsg("Service Endpoint URL23002: " + url23002);
    TestUtil.logMsg("WSDL Location URL23001:    " + wsdlurl23001);
    TestUtil.logMsg("WSDL Location URL23002:    " + wsdlurl23002);
  }

  private void getPortsStandalone() throws Exception {
    port23001 = (AddNumbersPortType23001) JAXWS_Util.getPort(service23001,
        PORT_QNAME23001, AddNumbersPortType23001.class);
    port23002 = (AddNumbersPortType23002) JAXWS_Util.getPort(service23002,
        PORT_QNAME23002, AddNumbersPortType23002.class);
    TestUtil.logMsg("port3=" + port23001);
    TestUtil.logMsg("port4=" + port23002);
    JAXWS_Util.setTargetEndpointAddress(port23001, url23001);
    JAXWS_Util.setTargetEndpointAddress(port23002, url23002);
    JAXWS_Util.setSOAPLogging(port23001);
    JAXWS_Util.setSOAPLogging(port23002);
  }

  private void getPortsJavaEE() throws Exception {
    javax.naming.InitialContext ic = new javax.naming.InitialContext();

    TestUtil.logMsg("Obtain service23001 via JNDI lookup");
    service23001 = (AddNumbersService23001) ic
        .lookup("java:comp/env/service/WSAJ2WDLAnonymousTest23001");
    TestUtil.logMsg("service23001=" + service23001);
    port23001 = (AddNumbersPortType23001) service23001
        .getPort(AddNumbersPortType23001.class);

    TestUtil.logMsg("Obtain service23002 via JNDI lookup");
    service23002 = (AddNumbersService23002) ic
        .lookup("java:comp/env/service/WSAJ2WDLAnonymousTest23002");
    TestUtil.logMsg("service23002=" + service23002);
    port23002 = (AddNumbersPortType23002) service23002
        .getPort(AddNumbersPortType23002.class);

    JAXWS_Util.dumpTargetEndpointAddress(port23001);
    JAXWS_Util.dumpTargetEndpointAddress(port23002);
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
   * port.range.min; port.range.max; http.server.supports.endpoint.publish;
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

      endpointPublishSupport = Boolean
          .parseBoolean(p.getProperty(ENDPOINTPUBLISHPROP));
      modeProperty = p.getProperty(MODEPROP);

      if (endpointPublishSupport) {
        try {
          maxPort = Integer.parseInt(p.getProperty(MAXPORT));
        } catch (Exception e) {
          maxPort = -1;
        }
        try {
          minPort = Integer.parseInt(p.getProperty(MINPORT));
        } catch (Exception e) {
          minPort = -1;
        }

        TestUtil.logMsg("minPort=" + minPort);
        TestUtil.logMsg("maxPort=" + maxPort);

        javaseServerPort = JAXWS_Util.getFreePort();
        if (javaseServerPort <= 0) {
          TestUtil.logMsg("Free port not found, use standard webserver port.");
          javaseServerPort = portnum;
          pass = false;
        }

        getTestURLs();
        service23001 = (AddNumbersService23001) JAXWS_Util.getService(
            wsdlurl23001, SERVICE_QNAME23001, AddNumbersService23001.class);
        service23002 = (AddNumbersService23002) JAXWS_Util.getService(
            wsdlurl23002, SERVICE_QNAME23002, AddNumbersService23002.class);
        getPortsStandalone();
      } else {
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        getTestURLs();
        getPortsJavaEE();
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
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /*
   * @testName: testAnonymousResponsesAssertion
   *
   * @assertion_ids: JAXWS:JAVADOC:83; JAXWS:JAVADOC:84; JAXWS:JAVADOC:86;
   * WSAMD:SPEC:3003; WSAMD:SPEC:3003.1; WSAMD:SPEC:3003.2;
   *
   * @test_Strategy: Invocation on port marked with AnonymousResponses assertion
   * Verify that wsa:ReplyTo in the SOAPRequest is the anonymous URI. Verify
   * that wsa:To in the SOAPResponse is the anonymous URI.
   * 
   * Test multiple @WebServiceRef annotations can be used due
   * to @Repeatable(value=WebServiceRefs.class)
   */
  public void testAnonymousResponsesAssertion() throws Fault {
    TestUtil.logMsg("testAnonymousResponsesAssertion");
    boolean pass = true;

    try {
      port23001.addNumbers(10, 10, "testAnonymousResponsesAssertion");
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
      pass = false;
    }

    if (!pass)
      throw new Fault("testAnonymousResponsesAssertion failed");
  }

  /*
   * @testName: testNonAnonymousResponsesAssertion
   *
   * @assertion_ids: JAXWS:JAVADOC:83; JAXWS:JAVADOC:84; JAXWS:JAVADOC:86;
   * WSAMD:SPEC:3003; WSAMD:SPEC:3003.1; WSAMD:SPEC:3003.3;
   *
   * @test_Strategy: Invocation on port marked with NonAnonymousResponses
   * assertion. The <ReplyTo> header may or may not be set by default depending
   * on the implementation. The test has to account for this.
   * 
   * Test multiple @WebServiceRef annotations can be used due
   * to @Repeatable(value=WebServiceRefs.class)
   */
  public void testNonAnonymousResponsesAssertion() throws Fault {
    TestUtil.logMsg("testNonAnonymousResponsesAssertion");
    boolean pass = true;

    try {
      TestUtil.logMsg("Expect a WebServiceException on port invocation");
      port23002.addNumbers(10, 10, "testNonAnonymousResponsesAssertion");
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught WebServiceException ignore: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass)
      throw new Fault("testNonAnonymousResponsesAssertion failed");
  }
}
