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
 * $Id: Client.java 52501 2007-01-24 02:29:49Z af70133 $
 */

package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.anonymous;

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

public class Client extends ServiceEETest {

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.wsa.j2w.document.literal.anonymous.";

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
  private static final String ENDPOINT_URL3 = "wsaj2wdlanonymoustest.endpoint.3";

  private static final String WSDLLOC_URL3 = "wsaj2wdlanonymoustest.wsdlloc.3";

  private static final String ENDPOINT_URL4 = "wsaj2wdlanonymoustest.endpoint.4";

  private static final String WSDLLOC_URL4 = "wsaj2wdlanonymoustest.wsdlloc.4";

  // NonAnonymousProcessor's
  private static final String NONANONYMOUSPROCESSOR = "/NonAnonymousProcessor";

  private static final String NONANONYMOUSPROCESSOR2 = "/NonAnonymousProcessor2";

  // service and port information
  private static final String NAMESPACEURI = "http://example.com/";

  private static final String TARGET_NAMESPACE = NAMESPACEURI;

  private static final String SERVICE_NAME3 = "AddNumbersService3";

  private static final String SERVICE_NAME4 = "AddNumbersService4";

  private static final String PORT_NAME3 = "AddNumbersPort3";

  private static final String PORT_NAME4 = "AddNumbersPort4";

  private static QName SERVICE_QNAME3 = new QName(NAMESPACEURI, SERVICE_NAME3);

  private static QName SERVICE_QNAME4 = new QName(NAMESPACEURI, SERVICE_NAME4);

  private static QName PORT_QNAME3 = new QName(NAMESPACEURI, PORT_NAME3);

  private static QName PORT_QNAME4 = new QName(NAMESPACEURI, PORT_NAME4);

  private static AddressingFeature ENABLED_ADDRESSING_FEATURE = new AddressingFeature(
      true, true);

  private static AddressingFeature DISABLED_ADDRESSING_FEATURE = new AddressingFeature(
      false);

  private String file3 = null;

  private String file4 = null;

  private String urlToNonAnonymousProcessor = null;

  private String urlToNonAnonymousProcessor2 = null;

  private String url3 = null;

  private String url4 = null;

  private URL wsdlurl3 = null;

  private URL wsdlurl4 = null;

  AddNumbersPortType3 port3 = null;

  AddNumbersPortType4 port4 = null;

  static AddNumbersService3 service3 = null;

  static AddNumbersService4 service4 = null;

  String ReplyToHeaderForAnonymousResponsesSoapMsg = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header><To xmlns=\"http://www.w3.org/2005/08/addressing\">{0}</To><MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:{1}</MessageID><ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{2}</Address></ReplyTo><Action xmlns=\"http://www.w3.org/2005/08/addressing\">http://example.com/AddNumbersPortType3/add</Action></S:Header><S:Body><addNumbers xmlns=\"http://example.com/\"><number1>10</number1><number2>10</number2><testName>testAnonymousResponsesReplyToHeader</testName></addNumbers></S:Body></S:Envelope>";

  String FaultToHeaderForAnonymousResponsesSoapMsg = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header><To xmlns=\"http://www.w3.org/2005/08/addressing\">{0}</To><MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:{1}</MessageID><FaultTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{2}</Address></FaultTo><ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{3}</Address></ReplyTo><Action xmlns=\"http://www.w3.org/2005/08/addressing\">http://example.com/AddNumbersPortType3/add</Action></S:Header><S:Body><addNumbers xmlns=\"http://example.com/\"><number1>-10</number1><number2>-10</number2><testName>testAnonymousResponsesFaultToHeader</testName></addNumbers></S:Body></S:Envelope>";

  String ReplyToHeaderForNonAnonymousResponsesSoapMsg = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header><To xmlns=\"http://www.w3.org/2005/08/addressing\">{0}</To><MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:{1}</MessageID><ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{2}</Address></ReplyTo><Action xmlns=\"http://www.w3.org/2005/08/addressing\">http://example.com/AddNumbersPortType4/add</Action></S:Header><S:Body><addNumbers xmlns=\"http://example.com/\"><number1>10</number1><number2>10</number2><testName>testNonAnonymousResponsesReplyToHeader</testName></addNumbers></S:Body></S:Envelope>";

  String FaultToHeaderForNonAnonymousResponsesSoapMsg = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header><To xmlns=\"http://www.w3.org/2005/08/addressing\">{0}</To><MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:{1}</MessageID><FaultTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{2}</Address></FaultTo><ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{3}</Address></ReplyTo><Action xmlns=\"http://www.w3.org/2005/08/addressing\">http://example.com/AddNumbersPortType4/add</Action></S:Header><S:Body><addNumbers xmlns=\"http://example.com/\"><number1>-10</number1><number2>-10</number2><testName>testNonAnonymousResponsesFaultToHeader</testName></addNumbers></S:Body></S:Envelope>";

  String TestNonAnonymousResponsesAssertionSoapMsg = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header><To xmlns=\"http://www.w3.org/2005/08/addressing\">{0}</To><MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:{1}</MessageID><ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{2}</Address></ReplyTo><Action xmlns=\"http://www.w3.org/2005/08/addressing\">http://example.com/AddNumbersPortType4/add</Action></S:Header><S:Body><addNumbers xmlns=\"http://example.com/\"><number1>10</number1><number2>10</number2><testName>testNonAnonymousResponsesAssertion</testName></addNumbers></S:Body></S:Envelope>";

  private Dispatch<SOAPMessage> createDispatchSOAPMessage(Service service,
      QName port) throws Exception {
    return service.createDispatch(port, SOAPMessage.class,
        jakarta.xml.ws.Service.Mode.MESSAGE, DISABLED_ADDRESSING_FEATURE);
  }

  private Dispatch<SOAPMessage> createAnonymousResponsesDispatch() {
    return service3.createDispatch(PORT_QNAME3, SOAPMessage.class,
        Service.Mode.MESSAGE, ENABLED_ADDRESSING_FEATURE);
  }

  private Dispatch<SOAPMessage> createNonAnonymousResponsesDispatch() {
    return service4.createDispatch(PORT_QNAME4, SOAPMessage.class,
        Service.Mode.MESSAGE, ENABLED_ADDRESSING_FEATURE);
  }

  private Dispatch<SOAPMessage> createAnonymousResponsesDispatchWithoutAddressing() {
    return service3.createDispatch(PORT_QNAME3, SOAPMessage.class,
        Service.Mode.MESSAGE, DISABLED_ADDRESSING_FEATURE);
  }

  private Dispatch<SOAPMessage> createNonAnonymousResponsesDispatchWithoutAddressing() {
    return service4.createDispatch(PORT_QNAME4, SOAPMessage.class,
        Service.Mode.MESSAGE, DISABLED_ADDRESSING_FEATURE);
  }

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    file3 = JAXWS_Util.getURLFromProp(ENDPOINT_URL3);
    url3 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file3);
    file4 = JAXWS_Util.getURLFromProp(ENDPOINT_URL4);
    url4 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file4);
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

    file3 = JAXWS_Util.getURLFromProp(WSDLLOC_URL3);
    wsdlurl3 = ctsurl.getURL(PROTOCOL, hostname, portnum, file3);
    file4 = JAXWS_Util.getURLFromProp(WSDLLOC_URL4);
    wsdlurl4 = ctsurl.getURL(PROTOCOL, hostname, portnum, file4);
    TestUtil.logMsg(
        "NonAnonymousProcessor Endpoint: " + urlToNonAnonymousProcessor);
    TestUtil.logMsg(
        "NonAnonymousProcessor2 Endpoint: " + urlToNonAnonymousProcessor2);
    TestUtil.logMsg("Service Endpoint URL3: " + url3);
    TestUtil.logMsg("Service Endpoint URL4: " + url4);
    TestUtil.logMsg("WSDL Location URL3:    " + wsdlurl3);
    TestUtil.logMsg("WSDL Location URL4:    " + wsdlurl4);
  }

  private void getPortsStandalone() throws Exception {
    port3 = (AddNumbersPortType3) JAXWS_Util.getPort(service3, PORT_QNAME3,
        AddNumbersPortType3.class);
    port4 = (AddNumbersPortType4) JAXWS_Util.getPort(service4, PORT_QNAME4,
        AddNumbersPortType4.class);
    TestUtil.logMsg("port3=" + port3);
    TestUtil.logMsg("port4=" + port4);
    JAXWS_Util.setTargetEndpointAddress(port3, url3);
    JAXWS_Util.setTargetEndpointAddress(port4, url4);
    JAXWS_Util.setSOAPLogging(port3);
    JAXWS_Util.setSOAPLogging(port4);
  }

  private void getPortsJavaEE() throws Exception {
    javax.naming.InitialContext ic = new javax.naming.InitialContext();

    TestUtil.logMsg("Obtain service3 via JNDI lookup");
    service3 = (AddNumbersService3) ic
        .lookup("java:comp/env/service/WSAJ2WDLAnonymousTest3");
    TestUtil.logMsg("service3=" + service3);
    port3 = (AddNumbersPortType3) service3.getPort(AddNumbersPortType3.class);

    TestUtil.logMsg("Obtain service4 via JNDI lookup");
    service4 = (AddNumbersService4) ic
        .lookup("java:comp/env/service/WSAJ2WDLAnonymousTest4");
    TestUtil.logMsg("service4=" + service4);
    port4 = (AddNumbersPortType4) service4.getPort(AddNumbersPortType4.class);

    JAXWS_Util.dumpTargetEndpointAddress(port3);
    JAXWS_Util.dumpTargetEndpointAddress(port4);
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
        service3 = (AddNumbersService3) JAXWS_Util.getService(wsdlurl3,
            SERVICE_QNAME3, AddNumbersService3.class);
        service4 = (AddNumbersService4) JAXWS_Util.getService(wsdlurl4,
            SERVICE_QNAME4, AddNumbersService4.class);
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
   * @assertion_ids: WSAMD:SPEC:3003; WSAMD:SPEC:3003.1; WSAMD:SPEC:3003.2;
   *
   * @test_Strategy: Invocation on port marked with AnonymousResponses assertion
   * Verify that wsa:ReplyTo in the SOAPRequest is the anonymous URI. Verify
   * that wsa:To in the SOAPResponse is the anonymous URI.
   */
  public void testAnonymousResponsesAssertion() throws Fault {
    TestUtil.logMsg("testAnonymousResponsesAssertion");
    boolean pass = true;

    try {
      port3.addNumbers(10, 10, "testAnonymousResponsesAssertion");
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
   * @assertion_ids: WSAMD:SPEC:3003; WSAMD:SPEC:3003.1; WSAMD:SPEC:3003.3;
   *
   * @test_Strategy: Invocation on port marked with NonAnonymousResponses
   * assertion. The <ReplyTo> header may or may not be set by default depending
   * on the implementation. The test has to account for this.
   */
  public void testNonAnonymousResponsesAssertion() throws Fault {
    TestUtil.logMsg("testNonAnonymousResponsesAssertion");
    boolean pass = true;

    try {
      TestUtil.logMsg("Expect a WebServiceException on port invocation");
      port4.addNumbers(10, 10, "testNonAnonymousResponsesAssertion");
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught WebServiceException ignore: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass)
      throw new Fault("testNonAnonymousResponsesAssertion failed");
  }

  /*
   * @testName: testNonAnonymousResponsesWithReplyToSetToValidProviderEndpoint
   *
   * @assertion_ids: WSAMD:SPEC:3003; WSAMD:SPEC:3003.1; WSAMD:SPEC:3003.2;
   * WSASB:SPEC:6012; WSASB:SPEC:6012.7; WSASB:SPEC:6013;
   *
   * @test_Strategy: Test for NonAnonymousResponses assertion where <ReplyTo>
   * header is set to a NonAnonymousProvider which will process the SOAP
   * response. Verify that the NonAnonymousProvider recieved the SOAP response.
   */
  public void testNonAnonymousResponsesWithReplyToSetToValidProviderEndpoint()
      throws Fault {
    TestUtil.logMsg(
        "testNonAnonymousResponsesWithReplyToSetToValidProviderEndpoint");
    boolean pass = true;

    SOAPMessage request = null, response = null;
    Dispatch<SOAPMessage> dispatchSM;
    Endpoint responseProcessor = null;
    Exchanger<SOAPMessage> respMsgExchanger = new Exchanger<SOAPMessage>();

    try {
      if (modeProperty.equals("standalone") && endpointPublishSupport) {
        responseProcessor = Endpoint
            .create(new NonAnonymousRespProcessor(respMsgExchanger));
        responseProcessor.publish(urlToNonAnonymousProcessor);
        String soapmsg = MessageFormat.format(
            ReplyToHeaderForNonAnonymousResponsesSoapMsg, url4,
            UUID.randomUUID(), urlToNonAnonymousProcessor);
        dispatchSM = createDispatchSOAPMessage(service4, PORT_QNAME4);
        request = JAXWS_Util.makeSOAPMessage(soapmsg);
        TestUtil.logMsg("Dumping SOAP Request ...");
        JAXWS_Util.dumpSOAPMessage(request, false);
        dispatchSM.invokeAsync(request);
        response = respMsgExchanger.exchange(null, 30L, TimeUnit.SECONDS);
        if (response != null) {
          System.out.println("****************************");
          response.writeTo(System.out);
          System.out.println("\n****************************");
        } else {
          pass = false;
        }
      }
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught WebServiceException ignore: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    } finally {
      if (responseProcessor != null)
        responseProcessor.stop();
    }
    if (!pass)
      throw new Fault(
          "testNonAnonymousResponsesWithReplyToSetToValidProviderEndpoint failed");
  }

  /*
   * @testName: testNonAnonymousResponsesWithFaultToSetToValidProviderEndpoint
   *
   * @assertion_ids: WSAMD:SPEC:3003; WSAMD:SPEC:3003.1; WSAMD:SPEC:3003.2;
   * WSASB:SPEC:6012; WSASB:SPEC:6012.7; WSASB:SPEC:6013;
   *
   * @test_Strategy: Test for NonAnonymousResponses assertion where <FaultTo>
   * header is set to a NonAnonymousProvider which will process the SOAP
   * response. Verify that the NonAnonymousProvider received the SOAPFault
   * message.
   */
  public void testNonAnonymousResponsesWithFaultToSetToValidProviderEndpoint()
      throws Fault {
    TestUtil.logMsg(
        "testNonAnonymousResponsesWithFaultToSetToValidProviderEndpoint");
    boolean pass = true;

    SOAPMessage request = null, response = null;
    Dispatch<SOAPMessage> dispatchSM;
    Endpoint responseProcessor = null;
    Endpoint responseProcessor2 = null;
    Exchanger<SOAPMessage> respMsgExchanger = new Exchanger<SOAPMessage>();
    Exchanger<SOAPMessage> respMsgExchanger2 = new Exchanger<SOAPMessage>();

    try {
      if (modeProperty.equals("standalone") && endpointPublishSupport) {
        responseProcessor = Endpoint
            .create(new NonAnonymousRespProcessor(respMsgExchanger));
        responseProcessor.publish(urlToNonAnonymousProcessor);
        responseProcessor2 = Endpoint
            .create(new NonAnonymousRespProcessor2(respMsgExchanger2));
        responseProcessor2.publish(urlToNonAnonymousProcessor2);
        String soapmsg = MessageFormat.format(
            FaultToHeaderForNonAnonymousResponsesSoapMsg, url4,
            UUID.randomUUID(), urlToNonAnonymousProcessor2,
            urlToNonAnonymousProcessor);
        dispatchSM = createDispatchSOAPMessage(service4, PORT_QNAME4);
        request = JAXWS_Util.makeSOAPMessage(soapmsg);
        TestUtil.logMsg("Dumping SOAP Request ...");
        JAXWS_Util.dumpSOAPMessage(request, false);
        dispatchSM.invokeAsync(request);
        response = respMsgExchanger2.exchange(null, 30L, TimeUnit.SECONDS);
        if (response != null) {
          System.out.println("****************************");
          response.writeTo(System.out);
          System.out.println("\n****************************");
        } else {
          pass = false;
        }
      }
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught WebServiceException ignore: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    } finally {
      if (responseProcessor != null)
        responseProcessor.stop();
      if (responseProcessor2 != null)
        responseProcessor2.stop();
    }
    if (!pass)
      throw new Fault(
          "testNonAnonymousResponsesWithFaultToSetToValidProviderEndpoint failed");
  }

  /*
   * @testName: testAnonymousResponsesWithReplyToSetToNone
   *
   * @assertion_ids: WSAMD:SPEC:3003; WSAMD:SPEC:3003.1; WSAMD:SPEC:3003.2;
   * WSASB:SPEC:6012; WSASB:SPEC:6012.7; WSASB:SPEC:6013;
   *
   * @test_Strategy: Test for AnonymousResponses assertion where <ReplyTo>
   * header is set to the None URI. This value must be accepted.
   *
   */
  public void testAnonymousResponsesWithReplyToSetToNone() throws Fault {
    TestUtil.logMsg("testAnonymousResponsesWithReplyToSetToNone");
    boolean pass = true;
    boolean done = false;

    SOAPMessage response = null;
    Dispatch<SOAPMessage> dispatchSM;
    try {
      String soapmsg = MessageFormat.format(
          ReplyToHeaderForAnonymousResponsesSoapMsg, url3, UUID.randomUUID(),
          W3CAddressingConstants.WSA_NONE_ADDRESS);
      dispatchSM = createDispatchSOAPMessage(service3, PORT_QNAME3);
      SOAPMessage request = JAXWS_Util.makeSOAPMessage(soapmsg);
      TestUtil.logMsg("Dumping SOAP Request ...");
      JAXWS_Util.dumpSOAPMessage(request, false);
      response = dispatchSM.invoke(request);
      TestUtil.logMsg("Dumping SOAP Response ...");
      JAXWS_Util.dumpSOAPMessage(response, false);
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught WebServiceException ignore: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("testAnonymousResponsesWithReplyToSetToNone failed");
  }

  /*
   * @testName: testAnonymousResponsesWithFaultToSetToNone
   *
   * @assertion_ids: WSAMD:SPEC:3003; WSAMD:SPEC:3003.1; WSAMD:SPEC:3003.2;
   * WSASB:SPEC:6012; WSASB:SPEC:6012.7; WSASB:SPEC:6013;
   *
   * @test_Strategy: Test for AnonymousResponses assertion where <FaultTo>
   * header is set to the None URI. This value must be accepted.
   *
   */
  public void testAnonymousResponsesWithFaultToSetToNone() throws Fault {
    TestUtil.logMsg("testAnonymousResponsesWithFaultToSetToNone");
    boolean pass = true;
    boolean done = false;

    SOAPMessage response = null;
    Dispatch<SOAPMessage> dispatchSM;
    try {
      String soapmsg = MessageFormat.format(
          FaultToHeaderForAnonymousResponsesSoapMsg, url3, UUID.randomUUID(),
          W3CAddressingConstants.WSA_NONE_ADDRESS,
          W3CAddressingConstants.WSA_ANONYMOUS_ADDRESS);
      dispatchSM = createDispatchSOAPMessage(service3, PORT_QNAME3);
      SOAPMessage request = JAXWS_Util.makeSOAPMessage(soapmsg);
      TestUtil.logMsg("Dumping SOAP Request ...");
      JAXWS_Util.dumpSOAPMessage(request, false);
      response = dispatchSM.invoke(request);
      TestUtil.logMsg("Dumping SOAP Response ...");
      JAXWS_Util.dumpSOAPMessage(response, false);
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught WebServiceException ignore: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("testAnonymousResponsesWithFaultToSetToNone failed");
  }

  /*
   * @testName: testNonAnonymousResponsesWithReplyToSetToNone
   *
   * @assertion_ids: WSAMD:SPEC:3003; WSAMD:SPEC:3003.1; WSAMD:SPEC:3003.2;
   * WSASB:SPEC:6012; WSASB:SPEC:6012.7; WSASB:SPEC:6013;
   *
   * @test_Strategy: Test for NonAnonymousResponses assertion where <ReplyTo>
   * header is set to the None URI. This value must be accepted.
   */
  public void testNonAnonymousResponsesWithReplyToSetToNone() throws Fault {
    TestUtil.logMsg("testNonAnonymousResponsesWithReplyToSetToNone");
    boolean pass = true;

    SOAPMessage response = null;
    Dispatch<SOAPMessage> dispatchSM;
    try {
      String soapmsg = MessageFormat.format(
          ReplyToHeaderForNonAnonymousResponsesSoapMsg, url4, UUID.randomUUID(),
          W3CAddressingConstants.WSA_NONE_ADDRESS);
      dispatchSM = createDispatchSOAPMessage(service4, PORT_QNAME4);
      SOAPMessage request = JAXWS_Util.makeSOAPMessage(soapmsg);
      TestUtil.logMsg("Dumping SOAP Request ...");
      JAXWS_Util.dumpSOAPMessage(request, false);
      response = dispatchSM.invoke(request);
      TestUtil.logMsg("Dumping SOAP Response ...");
      JAXWS_Util.dumpSOAPMessage(response, false);
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught WebServiceException ignore: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("testNonAnonymousResponsesWithReplyToSetToNone failed");
  }

  /*
   * @testName: testNonAnonymousResponsesWithFaultToSetToNone
   *
   * @assertion_ids: WSAMD:SPEC:3003; WSAMD:SPEC:3003.1; WSAMD:SPEC:3003.2;
   * WSASB:SPEC:6012; WSASB:SPEC:6012.7; WSASB:SPEC:6013;
   *
   * @test_Strategy: Test for NonAnonymousResponses assertion where <FaultTo>
   * header is set to the None URI. This value must be accepted.
   */
  public void testNonAnonymousResponsesWithFaultToSetToNone() throws Fault {
    TestUtil.logMsg("testNonAnonymousResponsesWithFaultToSetToNone");
    boolean pass = true;

    SOAPMessage response = null;
    Dispatch<SOAPMessage> dispatchSM;
    try {
      String soapmsg = MessageFormat.format(
          FaultToHeaderForNonAnonymousResponsesSoapMsg, url4, UUID.randomUUID(),
          W3CAddressingConstants.WSA_NONE_ADDRESS,
          W3CAddressingConstants.WSA_ANONYMOUS_ADDRESS);
      dispatchSM = createDispatchSOAPMessage(service4, PORT_QNAME4);
      SOAPMessage request = JAXWS_Util.makeSOAPMessage(soapmsg);
      TestUtil.logMsg("Dumping SOAP Request ...");
      JAXWS_Util.dumpSOAPMessage(request, false);
      response = dispatchSM.invoke(request);
      TestUtil.logMsg("Dumping SOAP Response ...");
      JAXWS_Util.dumpSOAPMessage(response, false);
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught WebServiceException ignore: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("testNonAnonymousResponsesWithFaultToSetToNone failed");
  }

  /*
   * @testName: testOnlyAnonymousAddressSupportedFaultBadReplyTo
   *
   * @assertion_ids: WSAMD:SPEC:3003; WSAMD:SPEC:3003.1; WSAMD:SPEC:3003.2;
   * WSASB:SPEC:6012; WSASB:SPEC:6012.7; WSASB:SPEC:6013;
   *
   * @test_Strategy: Test for OnlyAnonymousAddressSupported fault from client
   * runtime. Pass in soap message with <ReplyTo> header not equal to Anonymous
   * URI. Expect SOAPFault.
   *
   */
  public void testOnlyAnonymousAddressSupportedFaultBadReplyTo() throws Fault {
    TestUtil.logMsg("testOnlyAnonymousAddressSupportedFaultBadReplyTo");
    boolean pass = true;
    boolean done = false;

    SOAPMessage response = null;
    Dispatch<SOAPMessage> dispatchSM;
    try {
      String soapmsg = MessageFormat.format(
          ReplyToHeaderForAnonymousResponsesSoapMsg, url3, UUID.randomUUID(),
          url3 + "/badurl");
      dispatchSM = createDispatchSOAPMessage(service3, PORT_QNAME3);
      SOAPMessage request = JAXWS_Util.makeSOAPMessage(soapmsg);
      TestUtil.logMsg("Dumping SOAP Request ...");
      JAXWS_Util.dumpSOAPMessage(request, false);
      response = dispatchSM.invoke(request);
      TestUtil.logMsg("Dumping SOAP Response ...");
      JAXWS_Util.dumpSOAPMessage(response, false);
    } catch (SOAPFaultException e) {
      try {
        TestUtil.logMsg("Verify the SOAPFault faultcode");
        TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(e));
        TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(e));
        if (WsaSOAPUtils.isOnlyAnonymousAddressSupportedFaultCode(e))
          TestUtil.logMsg(
              "SOAPFault contains expected faultcode OnlyAnonymousAddressSupported");
        else {
          String faultcode = WsaSOAPUtils.getFaultCode(e);
          TestUtil.logErr("SOAPFault contains unexpected faultcode got: "
              + faultcode + ", expected: OnlyAnonymousAddressSupported");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultString(e) == null) {
          TestUtil
              .logErr("The faultstring element MUST EXIST for SOAP 1.1 Faults");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultDetail(e) != null) {
          TestUtil.logErr("The faultdetail element MUST NOT EXIST for SOAP 1.1 "
              + "Faults related to header entries");
          pass = false;
        }
        done = true;
      } catch (SOAPException e2) {
        TestUtil.logErr("Caught unexpected exception: ", e2);
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault(
          "testOnlyAnonymousAddressSupportedFaultBadReplyTo failed");
    if (done)
      return;

    try {
      if (response == null)
        throw new Fault("Expected a SOAPFault to be returned in SOAPResponse");
      if (!response.getSOAPPart().getEnvelope().getBody().hasFault())
        throw new Fault("Expected a SOAPFault to be returned in SOAPResponse");
    } catch (SOAPException e) {
      throw new Fault("Expected a SOAPFault to be returned in SOAPResponse");
    }
    try {
      TestUtil.logMsg("Verify the SOAPFault faultcode");
      TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(response));
      TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(response));
      if (WsaSOAPUtils.isOnlyAnonymousAddressSupportedFaultCode(response))
        TestUtil.logMsg(
            "SOAPFault contains expected faultcode OnlyAnonymousAddressSupported");
      else {
        String faultcode = WsaSOAPUtils.getFaultCode(response);
        TestUtil.logErr("SOAPFault contains unexpected faultcode got: "
            + faultcode + ", expected: OnlyAnonymousAddressSupported");
        pass = false;
      }
      if (WsaSOAPUtils.getFaultString(response) == null) {
        TestUtil
            .logErr("The faultstring element MUST EXIST for SOAP 1.1 Faults");
        pass = false;
      }
      if (WsaSOAPUtils.getFaultDetail(response) != null) {
        TestUtil.logErr("The faultdetail element MUST NOT EXIST for SOAP 1.1 "
            + "Faults related to header entries");
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass)
      throw new Fault(
          "testOnlyAnonymousAddressSupportedFaultBadReplyTo failed");
  }

  /*
   * @testName: testOnlyAnonymousAddressSupportedFaultBadFaultTo
   *
   * @assertion_ids: WSAMD:SPEC:3003; WSAMD:SPEC:3003.1; WSAMD:SPEC:3003.2;
   * WSASB:SPEC:6012; WSASB:SPEC:6012.7; WSASB:SPEC:6013;
   *
   * @test_Strategy: Test for OnlyAnonymousAddressSupported fault from client
   * runtime. Pass in soap message with <FaultTo> header not equal to Anonymous
   * URI. Expect SOAPFault.
   *
   */
  public void testOnlyAnonymousAddressSupportedFaultBadFaultTo() throws Fault {
    TestUtil.logMsg("testOnlyAnonymousAddressSupportedFaultBadFaultTo");
    boolean pass = true;
    boolean done = false;

    SOAPMessage response = null;
    Dispatch<SOAPMessage> dispatchSM;
    try {
      String soapmsg = MessageFormat.format(
          FaultToHeaderForAnonymousResponsesSoapMsg, url3, UUID.randomUUID(),
          url3 + "/badurl", W3CAddressingConstants.WSA_ANONYMOUS_ADDRESS);
      dispatchSM = createDispatchSOAPMessage(service3, PORT_QNAME3);
      SOAPMessage request = JAXWS_Util.makeSOAPMessage(soapmsg);
      TestUtil.logMsg("Dumping SOAP Request ...");
      JAXWS_Util.dumpSOAPMessage(request, false);
      response = dispatchSM.invoke(request);
      TestUtil.logMsg("Dumping SOAP Response ...");
      JAXWS_Util.dumpSOAPMessage(response, false);
    } catch (SOAPFaultException e) {
      try {
        TestUtil.logMsg("Verify the SOAPFault faultcode");
        TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(e));
        TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(e));
        if (WsaSOAPUtils.isOnlyAnonymousAddressSupportedFaultCode(e))
          TestUtil.logMsg(
              "SOAPFault contains expected faultcode OnlyAnonymousAddressSupported");
        else {
          String faultcode = WsaSOAPUtils.getFaultCode(e);
          TestUtil.logErr("SOAPFault contains unexpected faultcode got: "
              + faultcode + ", expected: OnlyAnonymousAddressSupported");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultString(e) == null) {
          TestUtil
              .logErr("The faultstring element MUST EXIST for SOAP 1.1 Faults");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultDetail(e) != null) {
          TestUtil.logErr("The faultdetail element MUST NOT EXIST for SOAP 1.1 "
              + "Faults related to header entries");
          pass = false;
        }
        done = true;
      } catch (SOAPException e2) {
        TestUtil.logErr("Caught unexpected exception: ", e2);
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault(
          "testOnlyAnonymousAddressSupportedFaultBadFaultTo failed");
    if (done)
      return;

    try {
      if (response == null)
        throw new Fault("Expected a SOAPFault to be returned in SOAPResponse");
      if (!response.getSOAPPart().getEnvelope().getBody().hasFault())
        throw new Fault("Expected a SOAPFault to be returned in SOAPResponse");
    } catch (SOAPException e) {
      throw new Fault("Expected a SOAPFault to be returned in SOAPResponse");
    }

    try {
      TestUtil.logMsg("Verify the SOAPFault faultcode");
      TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(response));
      TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(response));
      if (WsaSOAPUtils.isOnlyAnonymousAddressSupportedFaultCode(response))
        TestUtil.logMsg(
            "SOAPFault contains expected faultcode OnlyAnonymousAddressSupported");
      else {
        String faultcode = WsaSOAPUtils.getFaultCode(response);
        TestUtil.logErr("SOAPFault contains unexpected faultcode got: "
            + faultcode + ", expected: OnlyAnonymousAddressSupported");
        pass = false;
      }
      if (WsaSOAPUtils.getFaultString(response) == null) {
        TestUtil
            .logErr("The faultstring element MUST EXIST for SOAP 1.1 Faults");
        pass = false;
      }
      if (WsaSOAPUtils.getFaultDetail(response) != null) {
        TestUtil.logErr("The faultdetail element MUST NOT EXIST for SOAP 1.1 "
            + "Faults related to header entries");
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass)
      throw new Fault(
          "testOnlyAnonymousAddressSupportedFaultBadFaultTo failed");
  }

  /*
   * @testName: testOnlyNonAnonymousAddressSupportedFaultBadReplyTo
   *
   * @assertion_ids: WSAMD:SPEC:3003; WSAMD:SPEC:3003.1; WSAMD:SPEC:3003.3;
   * WSASB:SPEC:6012; WSASB:SPEC:6012.8; WSASB:SPEC:6013;
   *
   * @test_Strategy: Test for OnlyNonAnonymousAddressSupported fault from client
   * runtime. Pass in soap message with <ReplyTo> header equal to Anonymous URI.
   * Expect SOAPFault.
   *
   */
  public void testOnlyNonAnonymousAddressSupportedFaultBadReplyTo()
      throws Fault {
    TestUtil.logMsg("testOnlyNonAnonymousAddressSupportedFaultBadReplyTo");
    boolean pass = true;
    boolean done = false;

    SOAPMessage response = null;
    Dispatch<SOAPMessage> dispatchSM;
    try {
      String soapmsg = MessageFormat.format(
          ReplyToHeaderForNonAnonymousResponsesSoapMsg, url4, UUID.randomUUID(),
          W3CAddressingConstants.WSA_ANONYMOUS_ADDRESS);
      dispatchSM = createDispatchSOAPMessage(service4, PORT_QNAME4);
      SOAPMessage request = JAXWS_Util.makeSOAPMessage(soapmsg);
      TestUtil.logMsg("Dumping SOAP Request ...");
      JAXWS_Util.dumpSOAPMessage(request, false);
      response = dispatchSM.invoke(request);
      TestUtil.logMsg("Dumping SOAP Response ...");
      JAXWS_Util.dumpSOAPMessage(response, false);
    } catch (SOAPFaultException e) {
      try {
        TestUtil.logMsg("Verify the SOAPFault faultcode");
        TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(e));
        TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(e));
        if (WsaSOAPUtils.isOnlyNonAnonymousAddressSupportedFaultCode(e))
          TestUtil.logMsg(
              "SOAPFault contains expected faultcode OnlyNonAnonymousAddressSupported");
        else {
          String faultcode = WsaSOAPUtils.getFaultCode(e);
          TestUtil.logErr("SOAPFault contains unexpected faultcode got: "
              + faultcode + ", expected: OnlyNonAnonymousAddressSupported");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultString(e) == null) {
          TestUtil
              .logErr("The faultstring element MUST EXIST for SOAP 1.1 Faults");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultDetail(e) != null) {
          TestUtil.logErr("The faultdetail element MUST NOT EXIST for SOAP 1.1 "
              + "Faults related to header entries");
          pass = false;
        }
        done = true;
      } catch (SOAPException e2) {
        TestUtil.logErr("Caught unexpected exception: ", e2);
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass)
      throw new Fault(
          "testOnlyNonAnonymousAddressSupportedFaultBadReplyTo failed");

    if (done)
      return;

    try {
      if (response == null)
        throw new Fault("Expected a SOAPFault to be returned in SOAPResponse");
      if (!response.getSOAPPart().getEnvelope().getBody().hasFault())
        throw new Fault("Expected a SOAPFault to be returned in SOAPResponse");
    } catch (SOAPException e) {
      throw new Fault("Expected a SOAPFault to be returned in SOAPResponse");
    }
    try {
      TestUtil.logMsg("Verify the SOAPFault faultcode");
      TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(response));
      TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(response));
      if (WsaSOAPUtils.isOnlyNonAnonymousAddressSupportedFaultCode(response))
        TestUtil.logMsg(
            "SOAPFault contains expected faultcode OnlyNonAnonymousAddressSupported");
      else {
        String faultcode = WsaSOAPUtils.getFaultCode(response);
        TestUtil.logErr("SOAPFault contains unexpected faultcode got: "
            + faultcode + ", expected: OnlyNonAnonymousAddressSupported");
        pass = false;
      }
      if (WsaSOAPUtils.getFaultString(response) == null) {
        TestUtil
            .logErr("The faultstring element MUST EXIST for SOAP 1.1 Faults");
        pass = false;
      }
      if (WsaSOAPUtils.getFaultDetail(response) != null) {
        TestUtil.logErr("The faultdetail element MUST NOT EXIST for SOAP 1.1 "
            + "Faults related to header entries");
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass)
      throw new Fault(
          "testOnlyNonAnonymousAddressSupportedFaultBadReplyTo failed");
  }

  /*
   * @testName: testOnlyNonAnonymousAddressSupportedFaultBadFaultTo
   *
   * @assertion_ids: WSAMD:SPEC:3003; WSAMD:SPEC:3003.1; WSAMD:SPEC:3003.3;
   * WSASB:SPEC:6012; WSASB:SPEC:6012.8; WSASB:SPEC:6013;
   *
   * @test_Strategy: Test for OnlyNonAnonymousAddressSupported fault from client
   * runtime. Pass in soap message with <FaultTo> header equal to Anonymous URI.
   * Expect SOAPFault.
   *
   */
  public void testOnlyNonAnonymousAddressSupportedFaultBadFaultTo()
      throws Fault {
    TestUtil.logMsg("testOnlyNonAnonymousAddressSupportedFaultBadFaultTo");
    boolean pass = true;
    boolean done = false;

    SOAPMessage response = null;
    Dispatch<SOAPMessage> dispatchSM;
    try {
      String soapmsg = MessageFormat.format(
          FaultToHeaderForNonAnonymousResponsesSoapMsg, url4, UUID.randomUUID(),
          W3CAddressingConstants.WSA_ANONYMOUS_ADDRESS,
          W3CAddressingConstants.WSA_ANONYMOUS_ADDRESS);
      dispatchSM = createDispatchSOAPMessage(service4, PORT_QNAME4);
      SOAPMessage request = JAXWS_Util.makeSOAPMessage(soapmsg);
      TestUtil.logMsg("Dumping SOAP Request ...");
      JAXWS_Util.dumpSOAPMessage(request, false);
      response = dispatchSM.invoke(request);
      TestUtil.logMsg("Dumping SOAP Response ...");
      JAXWS_Util.dumpSOAPMessage(response, false);
    } catch (SOAPFaultException e) {
      try {
        TestUtil.logMsg("Verify the SOAPFault faultcode");
        TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(e));
        TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(e));
        if (WsaSOAPUtils.isOnlyNonAnonymousAddressSupportedFaultCode(e))
          TestUtil.logMsg(
              "SOAPFault contains expected faultcode OnlyNonAnonymousAddressSupported");
        else {
          String faultcode = WsaSOAPUtils.getFaultCode(e);
          TestUtil.logErr("SOAPFault contains unexpected faultcode got: "
              + faultcode + ", expected: OnlyNonAnonymousAddressSupported");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultString(e) == null) {
          TestUtil
              .logErr("The faultstring element MUST EXIST for SOAP 1.1 Faults");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultDetail(e) != null) {
          TestUtil.logErr("The faultdetail element MUST NOT EXIST for SOAP 1.1 "
              + "Faults related to header entries");
          pass = false;
        }
        done = true;
      } catch (SOAPException e2) {
        TestUtil.logErr("Caught unexpected exception: ", e2);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: ", e);
      pass = false;
    }

    if (!pass)
      throw new Fault(
          "testOnlyNonAnonymousAddressSupportedFaultBadFaultTo failed");

    if (done)
      return;

    try {
      if (response == null)
        throw new Fault("Expected a SOAPFault to be returned in SOAPResponse");
      if (!response.getSOAPPart().getEnvelope().getBody().hasFault())
        throw new Fault("Expected a SOAPFault to be returned in SOAPResponse");
    } catch (SOAPException e) {
      throw new Fault("Expected a SOAPFault to be returned in SOAPResponse");
    }
    try {
      TestUtil.logMsg("Verify the SOAPFault faultcode");
      TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(response));
      TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(response));
      if (WsaSOAPUtils.isOnlyNonAnonymousAddressSupportedFaultCode(response))
        TestUtil.logMsg(
            "SOAPFault contains expected faultcode OnlyNonAnonymousAddressSupported");
      else {
        String faultcode = WsaSOAPUtils.getFaultCode(response);
        TestUtil.logErr("SOAPFault contains unexpected faultcode got: "
            + faultcode + ", expected: OnlyNonAnonymousAddressSupported");
        pass = false;
      }
      if (WsaSOAPUtils.getFaultString(response) == null) {
        TestUtil
            .logErr("The faultstring element MUST EXIST for SOAP 1.1 Faults");
        pass = false;
      }
      if (WsaSOAPUtils.getFaultDetail(response) != null) {
        TestUtil.logErr("The faultdetail element MUST NOT EXIST for SOAP 1.1 "
            + "Faults related to header entries");
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }

    if (!pass)
      throw new Fault(
          "testOnlyNonAnonymousAddressSupportedFaultBadFaultTo failed");
  }
}
