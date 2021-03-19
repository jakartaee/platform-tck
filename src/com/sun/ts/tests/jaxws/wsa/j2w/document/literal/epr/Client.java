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
 * $Id: Client.java 51075 2003-03-27 10:44:21Z lschwenk $
 */

package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.epr;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.*;
import jakarta.xml.ws.*;
import jakarta.xml.soap.*;
import java.util.Properties;
import java.math.BigInteger;
import java.math.BigDecimal;
import javax.xml.namespace.QName;
import com.sun.javatest.Status;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsa.common.*;
import jakarta.xml.ws.wsaddressing.W3CEndpointReference;
import jakarta.xml.ws.soap.AddressingFeature;
import jakarta.xml.ws.soap.SOAPFaultException;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.JAXBElement;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.namespace.QName;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.wsa.j2w.document.literal.epr.";

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "wsaj2wdleprtest.endpoint.1";

  private static final String WSDLLOC_URL = "wsaj2wdleprtest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  // service and port information
  private static final String NAMESPACEURI = "http://foobar.org/";

  private static final String SERVICE_NAME = "AddNumbersService";

  private static final String PORT_NAME = "AddNumbersPort";

  private static final String PORT_TYPE_NAME = "AddNumbers";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private QName PORT_TYPE_QNAME = new QName(NAMESPACEURI, PORT_TYPE_NAME);

  private WebServiceFeature[] wsftrue = { new AddressingFeature(true, true) };

  private WebServiceFeature[] wsffalse = {
      new AddressingFeature(false, false) };

  AddNumbers port = null;

  BindingProvider bp = null;

  static AddNumbersService service = null;

  private Source source = null;

  private Dispatch<Object> dispatchJaxb = null;

  private Dispatch<Source> dispatchSrc = null;

  private Dispatch<SOAPMessage> dispatchSM = null;

  private String doAddNumbersRequest = "<ns1:doAddNumbers xmlns:ns1=\"http://foobar.org/\"><arg0>10</arg0><arg1>10</arg1></ns1:doAddNumbers>";

  private String doAddNumbersRequestSM = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Body><ns3:doAddNumbers xmlns:ns3=\"http://foobar.org/\"><arg0>10</arg0><arg1>10</arg1></ns3:doAddNumbers></S:Body></S:Envelope>";

  private static final Class JAXB_OBJECT_FACTORY = com.sun.ts.tests.jaxws.wsa.j2w.document.literal.epr.ObjectFactory.class;

  private JAXBContext createJAXBContext() {
    try {
      return JAXBContext.newInstance(JAXB_OBJECT_FACTORY);
    } catch (jakarta.xml.bind.JAXBException e) {
      throw new WebServiceException(e.getMessage(), e);
    }
  }

  private Dispatch<Object> createDispatchJAXB(W3CEndpointReference myepr,
      WebServiceFeature[] wsf) throws Exception {
    if (wsf == null)
      return service.createDispatch(myepr, createJAXBContext(),
          jakarta.xml.ws.Service.Mode.PAYLOAD);
    else
      return service.createDispatch(myepr, createJAXBContext(),
          jakarta.xml.ws.Service.Mode.PAYLOAD, wsf);
  }

  private Dispatch<Source> createDispatchSource(W3CEndpointReference myepr,
      WebServiceFeature[] wsf) throws Exception {
    if (wsf == null)
      return service.createDispatch(myepr, Source.class,
          jakarta.xml.ws.Service.Mode.PAYLOAD);
    else
      return service.createDispatch(myepr, Source.class,
          jakarta.xml.ws.Service.Mode.PAYLOAD, wsf);
  }

  private Dispatch<SOAPMessage> createDispatchSOAPMessage(
      W3CEndpointReference myepr, WebServiceFeature[] wsf) throws Exception {
    if (wsf == null)
      return service.createDispatch(myepr, SOAPMessage.class,
          jakarta.xml.ws.Service.Mode.MESSAGE);
    else
      return service.createDispatch(myepr, SOAPMessage.class,
          jakarta.xml.ws.Service.Mode.MESSAGE, wsf);
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

  private void getPortStandalone() throws Exception {
    TestUtil.logMsg("Obtain service via Service.create(URL, QName)");
    service = (AddNumbersService) JAXWS_Util.getService(wsdlurl, SERVICE_QNAME,
        AddNumbersService.class);
    TestUtil.logMsg("service=" + service);
    TestUtil.logMsg("Obtain port");
    port = (AddNumbers) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        AddNumbersService.class, PORT_QNAME, AddNumbers.class, wsftrue);
    bp = (BindingProvider) port;
    JAXWS_Util.setTargetEndpointAddress(port, url);
    JAXWS_Util.dumpTargetEndpointAddress(port);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("bindingProvider=" + bp);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    TestUtil.logMsg("Obtain port");
    port = (AddNumbers) service.getPort(AddNumbers.class, wsftrue);
    bp = (BindingProvider) port;
    JAXWS_Util.dumpTargetEndpointAddress(port);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("bindingProvider=" + bp);
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
        getTestURLs();
        getPortStandalone();
      } else {
        TestUtil.logMsg("WebServiceRef is not set in Client "
            + "(get it from specific vehicle)");
        service = (AddNumbersService) getSharedObject();
        getTestURLs();
        getPortJavaEE();
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("setup failed:", e);
    }

    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server in " + "config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed:");
    }
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /*
   * @testName: EPRGetEPRViaWSCTest1
   *
   * @assertion_ids: WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:JAVADOC:158; JAXWS:SPEC:4027.3; JAXWS:SPEC:4027.4; JAXWS:SPEC:4027.5;
   * WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2; WSAMD:SPEC:2001;
   * WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3; WSAMD:SPEC:2002;
   * WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3; WSAMD:SPEC:2002.4;
   * JAXWS:SPEC:5027; JAXWS:SPEC:5028;
   *
   * @test_Strategy: Retrieve EPR via WebServiceContext.getEndpointReference().
   *
   */
  public void EPRGetEPRViaWSCTest1() throws Fault {
    TestUtil.logMsg("EPRGetEPRViaWSCTest1");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Retrieve EPR via WebServiceContext.getEndpointReference()");
      W3CEndpointReference epr = port.getW3CEPR1();
      if (epr != null) {
        TestUtil.logMsg("---------------------------");
        TestUtil.logMsg("DUMP OF ENDPOINT REFERENCE");
        TestUtil.logMsg("---------------------------");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        epr.writeTo(new StreamResult(baos));
        TestUtil.logMsg(baos.toString());
        DOMResult dr = new DOMResult();
        epr.writeTo(dr);
        XMLUtils.xmlDumpDOMNodes(dr.getNode(), false);
        TestUtil.logMsg("Validate the EPR for correctness)");
        if (!EprUtil.validateEPR(epr, url, SERVICE_QNAME, PORT_QNAME,
            PORT_TYPE_QNAME, Boolean.TRUE, wsdlurl.toString()))
          pass = false;
      } else {
        TestUtil.logErr("EPR is null (unexpected)");
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRGetEPRViaWSCTest1 failed");
  }

  /*
   * @testName: EPRGetEPRViaWSCTest2
   *
   * @assertion_ids: WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:JAVADOC:159; JAXWS:SPEC:4027.3; JAXWS:SPEC:4027.4; JAXWS:SPEC:4027.5;
   * WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2; WSAMD:SPEC:2001;
   * WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3; WSAMD:SPEC:2002;
   * WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3; WSAMD:SPEC:2002.4;
   * JAXWS:SPEC:5027; JAXWS:SPEC:5028;
   *
   * @test_Strategy: Retrieve EPR via WebServiceContext.getEndpointReference(
   * java.lang.Class).
   */
  public void EPRGetEPRViaWSCTest2() throws Fault {
    TestUtil.logMsg("EPRGetEPRViaWSCTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg(
          "Retrieve EPR via WebServiceContext.getEndpointReference(java.lang.Class)");
      W3CEndpointReference epr = port.getW3CEPR2();
      if (epr != null) {
        TestUtil.logMsg("---------------------------");
        TestUtil.logMsg("DUMP OF ENDPOINT REFERENCE");
        TestUtil.logMsg("---------------------------");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        epr.writeTo(new StreamResult(baos));
        TestUtil.logMsg(baos.toString());
        DOMResult dr = new DOMResult();
        epr.writeTo(dr);
        XMLUtils.xmlDumpDOMNodes(dr.getNode(), false);
        if (!EprUtil.validateEPR(epr, url, SERVICE_QNAME, PORT_QNAME,
            PORT_TYPE_QNAME, Boolean.TRUE, wsdlurl.toString()))
          pass = false;
      } else {
        TestUtil.logErr("EPR is null (unexpected)");
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRGetEPRViaWSCTest2 failed");
  }

  /*
   * @testName: EPRGetEPRViaBPTest1
   *
   * @assertion_ids: WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:JAVADOC:186; JAXWS:SPEC:4027.3; JAXWS:SPEC:4027.4; JAXWS:SPEC:4027.5;
   * WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2; WSAMD:SPEC:2001;
   * WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3; WSAMD:SPEC:2002;
   * WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3; WSAMD:SPEC:2002.4;
   * JAXWS:SPEC:5027; JAXWS:SPEC:4022;
   *
   * @test_Strategy: Retrieve EPR via BindingProvider.getEndpointReference().
   *
   */
  public void EPRGetEPRViaBPTest1() throws Fault {
    TestUtil.logMsg("EPRGetEPRViaBPTest1");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Retrieve EPR via BindingProvider.getEndpointReference()");
      W3CEndpointReference epr = (W3CEndpointReference) bp
          .getEndpointReference();
      if (epr != null) {
        TestUtil.logMsg("---------------------------");
        TestUtil.logMsg("DUMP OF ENDPOINT REFERENCE");
        TestUtil.logMsg("---------------------------");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        epr.writeTo(new StreamResult(baos));
        TestUtil.logMsg(baos.toString());
        DOMResult dr = new DOMResult();
        epr.writeTo(dr);
        XMLUtils.xmlDumpDOMNodes(dr.getNode(), false);
        if (!EprUtil.validateEPR(epr, url, SERVICE_QNAME, PORT_QNAME,
            PORT_TYPE_QNAME, Boolean.TRUE, wsdlurl.toString()))
          pass = false;
      } else {
        TestUtil.logErr("EPR is null (unexpected)");
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRGetEPRViaBPTest1 failed");
  }

  /*
   * @testName: EPRGetEPRViaBPTest2
   *
   * @assertion_ids: WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:JAVADOC:187; JAXWS:SPEC:4027.3; JAXWS:SPEC:4027.4; JAXWS:SPEC:4027.5;
   * WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2; WSAMD:SPEC:2001;
   * WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3; WSAMD:SPEC:2002;
   * WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3; WSAMD:SPEC:2002.4;
   * JAXWS:SPEC:5027; JAXWS:SPEC:4022;
   *
   * @test_Strategy: Retrieve EPR via BindingProvider.getEndpointReference(
   * java.lang.Class).
   */
  public void EPRGetEPRViaBPTest2() throws Fault {
    TestUtil.logMsg("EPRGetEPRViaBPTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Retrieve EPR via BindingProvider.getEndpointReference("
          + "java.lang.Class)");
      W3CEndpointReference epr = (W3CEndpointReference) bp.getEndpointReference(
          jakarta.xml.ws.wsaddressing.W3CEndpointReference.class);
      if (epr != null) {
        TestUtil.logMsg("---------------------------");
        TestUtil.logMsg("DUMP OF ENDPOINT REFERENCE");
        TestUtil.logMsg("---------------------------");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        epr.writeTo(new StreamResult(baos));
        TestUtil.logMsg(baos.toString());
        DOMResult dr = new DOMResult();
        epr.writeTo(dr);
        XMLUtils.xmlDumpDOMNodes(dr.getNode(), false);
        if (!EprUtil.validateEPR(epr, url, SERVICE_QNAME, PORT_QNAME,
            PORT_TYPE_QNAME, Boolean.TRUE, wsdlurl.toString()))
          pass = false;
      } else {
        TestUtil.logErr("EPR is null (unexpected)");
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRGetEPRViaBPTest2 failed");
  }

  /*
   * @testName: EPRGetEPRViaBPWithUnsupportedEPRClassTest
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:JAVADOC:187; JAXWS:SPEC:4027.3; JAXWS:SPEC:4027.4; JAXWS:SPEC:4027.5;
   * JAXWS:SPEC:4027.6; JAXWS:SPEC:5024.3;
   *
   * @test_Strategy: Retrieve EPR via BindingProvider.getEndpointReference(
   * java.lang.Class). Pass in an invalid Class. Expect a WebServiceException to
   * be thrown.
   */
  public void EPRGetEPRViaBPWithUnsupportedEPRClassTest() throws Fault {
    TestUtil.logMsg("EPRGetEPRViaBPWithUnsupportedEPRClassTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Retrieve EPR via BindingProvider.getEndpointReference("
          + "java.lang.Class)");
      TestUtil.logMsg(
          "Pass in unsupported EPR Class and expect WebServiceException");
      MyEPR epr = (MyEPR) bp.getEndpointReference(MyEPR.class);
      TestUtil.logErr("Did not throw expected WebServiceException");
      pass = false;
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught expected WebServiceException: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRGetEPRViaBPWithUnsupportedEPRClassTest failed");
  }

  /*
   * @testName: EPRWriteToAndReadFromTest
   *
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:JAVADOC:141; JAXWS:JAVADOC:142;
   *
   * @test_Strategy: Retrieve EPR via BindingProvider.getEndpointReference().
   * Write EPR as an XML Infoset Object using writeTo() method and then read EPR
   * back from the XML Infoset Object using readfrom() method.
   */
  public void EPRWriteToAndReadFromTest() throws Fault {
    TestUtil.logMsg("EPRWriteToAndReadFromTest");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Retrieve EPR via BindingProvider.getEndpointReference()");
      W3CEndpointReference epr = (W3CEndpointReference) bp
          .getEndpointReference();
      TestUtil.logMsg("Write the EPR to a ByteArrayOutputStream Object");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      epr.writeTo(new StreamResult(baos));
      TestUtil.logMsg(baos.toString());
      TestUtil.logMsg("Read the EPR from a ByteArrayInputStream Object");
      ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
      epr = (W3CEndpointReference) EndpointReference
          .readFrom(new StreamSource(bais));
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRWriteToAndReadFromTest failed");
  }

  /*
   * @testName: ServiceGetPortViaWSCAndWSFTrueTest
   *
   * @assertion_ids: WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:SPEC:4028; JAXWS:SPEC:4028.1; JAXWS:JAVADOC:140;
   *
   * @test_Strategy: Retrieve EPR via WebServiceContext.getEndpointReference().
   * From the returned EPR get the port via Service.getPort(EPR,
   * AddNumbers.class, wsf). WebServiceFeature is passed with Addressing=true.
   * Verify invocation behavior.
   *
   */
  public void ServiceGetPortViaWSCAndWSFTrueTest() throws Fault {
    TestUtil.logMsg("ServiceGetPortViaWSCAndWSFTrueTest");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Retrieve EPR via WebServiceContext.getEndpointReference()");
      W3CEndpointReference epr = port.getW3CEPR1();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      epr.writeTo(new StreamResult(baos));
      TestUtil.logMsg(baos.toString());
      TestUtil.logMsg("Retrieve the port from the EPR via Service.getPort()");
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=true");
      AddNumbers retport = (AddNumbers) service.getPort(epr, AddNumbers.class,
          wsftrue);
      if (retport == null) {
        TestUtil.logErr(
            "Service.getPort(EPR, Class, wsftrue) returned null (unexpected)");
        pass = false;
      } else {
        TestUtil.logMsg("Verify invocation behavior on port");
        int result = retport.doAddNumbers(10, 10);
        TestUtil.logMsg("Invocation succeeded (expected) now check result");
        if (result != 20) {
          TestUtil.logErr("Expected result=20, got result=" + result);
          pass = false;
        } else
          TestUtil.logMsg("Got expected result=20");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("ServiceGetPortViaWSCAndWSFTrueTest failed");
  }

  /*
   * @testName: ServiceGetPortViaWSCAndWSFFalseTest
   *
   * @assertion_ids: WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:SPEC:4028; JAXWS:SPEC:4028.1; JAXWS:JAVADOC:140;
   *
   * @test_Strategy: Retrieve EPR via WebServiceContext.getEndpointReference().
   * From the returned EPR get the port via Service.getPort(EPR,
   * AddNumbers.class, wsf). WebServiceFeature is passed with Addressing=false.
   * Verify invocation behavior.
   *
   */
  public void ServiceGetPortViaWSCAndWSFFalseTest() throws Fault {
    TestUtil.logMsg("ServiceGetPortViaWSCAndWSFFalseTest");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Retrieve EPR via WebServiceContext.getEndpointReference()");
      W3CEndpointReference epr = port.getW3CEPR1();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      epr.writeTo(new StreamResult(baos));
      TestUtil.logMsg(baos.toString());
      TestUtil.logMsg("Retrieve the port from the EPR via Service.getPort()");
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=false");
      AddNumbers retport = (AddNumbers) service.getPort(epr, AddNumbers.class,
          wsffalse);
      if (retport == null) {
        TestUtil.logErr(
            "Service.getPort(EPR, Class, wsffalse) returned null (unexpected)");
        pass = false;
      } else {
        TestUtil.logMsg("Verify invocation behavior on port");
        int result = retport.doAddNumbers(10, 10);
        TestUtil.logErr("Did not throw expected SOAPFaultException");
        pass = false;
      }
    } catch (SOAPFaultException e) {
      TestUtil.logMsg("Caught expected SOAPFaultException: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("ServiceGetPortViaWSCAndWSFFalseTest failed");
  }

  /*
   * @testName: ServiceGetPortViaBPAndWSFTrueTest
   *
   * @assertion_ids: WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:SPEC:4028; JAXWS:SPEC:4028.1; JAXWS:JAVADOC:140;
   *
   * @test_Strategy: Retrieve EPR via BindingProvider.getEndpointReference().
   * From the returned EPR get the port via Service.getPort(EPR,
   * AddNumbers.class, wsf). WebServiceFeature is passed with Addressing=true.
   * Verify invocation behavior.
   *
   */
  public void ServiceGetPortViaBPAndWSFTrueTest() throws Fault {
    TestUtil.logMsg("ServiceGetPortViaBPAndWSFTrueTest");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Retrieve EPR via BindingProvider.getEndpointReference()");
      W3CEndpointReference epr = (W3CEndpointReference) bp
          .getEndpointReference();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      epr.writeTo(new StreamResult(baos));
      TestUtil.logMsg(baos.toString());
      TestUtil.logMsg("Retrieve the port from the EPR via Service.getPort()");
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=true");
      AddNumbers retport = (AddNumbers) service.getPort(epr, AddNumbers.class,
          wsftrue);
      if (retport == null) {
        TestUtil.logErr(
            "Service.getPort(EPR, Class, wsftrue) returned null (unexpected)");
        pass = false;
      } else {
        TestUtil.logMsg("Verify invocation behavior on port");
        int result = retport.doAddNumbers(10, 10);
        TestUtil.logMsg("Invocation succeeded (expected) now check result");
        if (result != 20) {
          TestUtil.logErr("Expected result=20, got result=" + result);
          pass = false;
        } else
          TestUtil.logMsg("Got expected result=20");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("ServiceGetPortViaBPAndWSFTrueTest failed");
  }

  /*
   * @testName: ServiceGetPortViaBPAndWSFFalseTest
   *
   * @assertion_ids: WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:SPEC:4028; JAXWS:SPEC:4028.1; JAXWS:JAVADOC:140;
   *
   * @test_Strategy: Retrieve EPR via BindingProvider.getEndpointReference().
   * From the returned EPR get the port via Service.getPort(EPR,
   * AddNumbers.class, wsf). WebServiceFeature is passed with Addressing=false.
   * Verify invocation behavior.
   *
   */
  public void ServiceGetPortViaBPAndWSFFalseTest() throws Fault {
    TestUtil.logMsg("ServiceGetPortViaBPAndWSFFalseTest");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Retrieve EPR via BindingProvider.getEndpointReference()");
      W3CEndpointReference epr = (W3CEndpointReference) bp
          .getEndpointReference();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      epr.writeTo(new StreamResult(baos));
      TestUtil.logMsg(baos.toString());
      TestUtil.logMsg("Retrieve the port from the EPR via Service.getPort()");
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=false");
      AddNumbers retport = (AddNumbers) service.getPort(epr, AddNumbers.class,
          wsffalse);
      if (retport == null) {
        TestUtil.logErr(
            "Service.getPort(EPR, Class, wsffalse) returned null (unexpected)");
        pass = false;
      } else {
        TestUtil.logMsg("Verify invocation behavior on port");
        int result = retport.doAddNumbers(10, 10);
        TestUtil.logErr("Did not throw expected SOAPFaultException");
        pass = false;
      }
    } catch (SOAPFaultException e) {
      TestUtil.logMsg("Caught expected SOAPFaultException: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("ServiceGetPortViaBPAndWSFFalseTest failed");
  }

  /*
   * @testName: EPRGetPortViaWSCAndNoWSFTest
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:SPEC:4028; JAXWS:SPEC:4028.1; JAXWS:JAVADOC:140;
   *
   * @test_Strategy: Retrieve EPR via WebServiceContext.getEndpointReference().
   * From the returned EPR get the port via EndpointReference.getPort
   * (AddNumbers.class). No WebServiceFeature is passed (DEFAULT CASE). Verify
   * invocation behavior.
   *
   */
  public void EPRGetPortViaWSCAndNoWSFTest() throws Fault {
    TestUtil.logMsg("EPRGetPortViaWSCAndNoWSFTest");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Retrieve EPR via WebServiceContext.getEndpointReference()");
      W3CEndpointReference epr = port.getW3CEPR1();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      epr.writeTo(new StreamResult(baos));
      TestUtil.logMsg(baos.toString());
      TestUtil.logMsg("Retrieve port from EPR via EndpointReference.getPort()");
      TestUtil.logMsg("Don't pass a WebServiceFeature (DEFAULT CASE)");
      AddNumbers retport = (AddNumbers) epr.getPort(AddNumbers.class);
      if (retport == null) {
        TestUtil.logErr("EPR.getPort(Class) returned null (unexpected)");
        pass = false;
      } else {
        TestUtil.logMsg("Verify invocation behavior on port");
        int result = retport.doAddNumbers(10, 10);
        TestUtil.logMsg("Invocation succeeded (expected) now check result");
        if (result != 20) {
          TestUtil.logErr("Expected result=20, got result=" + result);
          pass = false;
        } else
          TestUtil.logMsg("Got expected result=20");
      }
    } catch (SOAPFaultException e) {
      TestUtil.logMsg("Caught expected SOAPFaultException: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRGetPortViaWSCAndNoWSFTest failed");
  }

  /*
   * @testName: EPRGetPortViaWSCAndWSFTrueTest
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:SPEC:4028; JAXWS:SPEC:4028.1; JAXWS:JAVADOC:140;
   *
   * @test_Strategy: Retrieve EPR via WebServiceContext.getEndpointReference().
   * From the returned EPR get the port via EndpointReference.getPort
   * (AddNumbers.class, wsf). WebServiceFeature is passed with Addressing=true.
   * Verify invocation behavior.
   *
   */
  public void EPRGetPortViaWSCAndWSFTrueTest() throws Fault {
    TestUtil.logMsg("EPRGetPortViaWSCAndWSFTrueTest");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Retrieve EPR via WebServiceContext.getEndpointReference()");
      W3CEndpointReference epr = port.getW3CEPR1();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      epr.writeTo(new StreamResult(baos));
      TestUtil.logMsg(baos.toString());
      TestUtil.logMsg(
          "Retrieve the port from the EPR via EndpointReference.getPort()");
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=true");
      AddNumbers retport = (AddNumbers) epr.getPort(AddNumbers.class, wsftrue);
      if (retport == null) {
        TestUtil
            .logErr("EPR.getPort(Class, wsftrue) returned null (unexpected)");
        pass = false;
      } else {
        TestUtil.logMsg("Verify invocation behavior on port");
        int result = retport.doAddNumbers(10, 10);
        TestUtil.logMsg("Invocation succeeded (expected) now check result");
        if (result != 20) {
          TestUtil.logErr("Expected result=20, got result=" + result);
          pass = false;
        } else
          TestUtil.logMsg("Got expected result=20");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRGetPortViaWSCAndWSFTrueTest failed");
  }

  /*
   * @testName: EPRGetPortViaWSCAndWSFFalseTest
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:SPEC:4028; JAXWS:SPEC:4028.1; JAXWS:JAVADOC:140;
   *
   * @test_Strategy: Retrieve EPR via WebServiceContext.getEndpointReference().
   * From the returned EPR get the port via EndpointReference.getPort
   * (AddNumbers.class, wsf). WebServiceFeature is passed with Addressing=false.
   * Verify invocation behavior.
   *
   */
  public void EPRGetPortViaWSCAndWSFFalseTest() throws Fault {
    TestUtil.logMsg("EPRGetPortViaWSCAndWSFFalseTest");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Retrieve EPR via WebServiceContext.getEndpointReference()");
      W3CEndpointReference epr = port.getW3CEPR1();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      epr.writeTo(new StreamResult(baos));
      TestUtil.logMsg(baos.toString());
      TestUtil.logMsg(
          "Retrieve the port from the EPR via EndpointReference.getPort()");
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=false");
      AddNumbers retport = (AddNumbers) epr.getPort(AddNumbers.class, wsffalse);
      if (retport == null) {
        TestUtil
            .logErr("EPR.getPort(Class, wsffalse) returned null (unexpected)");
        pass = false;
      } else {
        TestUtil.logMsg("Verify invocation behavior on port");
        int result = retport.doAddNumbers(10, 10);
        TestUtil.logErr("Did not throw expected SOAPFaultException");
        pass = false;
      }
    } catch (SOAPFaultException e) {
      TestUtil.logMsg("Caught expected SOAPFaultException: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRGetPortViaWSCAndWSFFalseTest failed");
  }

  /*
   * @testName: EPRGetPortViaBPAndNoWSFTest
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:SPEC:4028; JAXWS:SPEC:4028.1; JAXWS:JAVADOC:140;
   *
   * @test_Strategy: Retrieve EPR via BindingProvider.getEndpointReference().
   * From the returned EPR get the port via EndpointReference.getPort
   * (AddNumbers.class). No WebServiceFeature is passed (DEFAULT CASE). Verify
   * invocation behavior.
   *
   */
  public void EPRGetPortViaBPAndNoWSFTest() throws Fault {
    TestUtil.logMsg("EPRGetPortViaBPAndNoWSFTest");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Retrieve EPR via BindingProvider.getEndpointReference()");
      W3CEndpointReference epr = (W3CEndpointReference) bp
          .getEndpointReference();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      epr.writeTo(new StreamResult(baos));
      TestUtil.logMsg(baos.toString());
      TestUtil.logMsg("Retrieve port from EPR via EndpointReference.getPort()");
      TestUtil.logMsg("Don't pass a WebServiceFeature (DEFAULT CASE)");
      AddNumbers retport = (AddNumbers) epr.getPort(AddNumbers.class);
      if (retport == null) {
        TestUtil.logErr("EPR.getPort(Class) returned null (unexpected)");
        pass = false;
      } else {
        TestUtil.logMsg("Verify invocation behavior on port");
        int result = retport.doAddNumbers(10, 10);
        TestUtil.logMsg("Invocation succeeded (expected) now check result");
        if (result != 20) {
          TestUtil.logErr("Expected result=20, got result=" + result);
          pass = false;
        } else
          TestUtil.logMsg("Got expected result=20");
      }
    } catch (SOAPFaultException e) {
      TestUtil.logMsg("Caught expected SOAPFaultException: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRGetPortViaBPAndNoWSFTest failed");
  }

  /*
   * @testName: EPRGetPortViaBPAndWSFTrueTest
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:SPEC:4028; JAXWS:SPEC:4028.1; JAXWS:JAVADOC:140;
   *
   * @test_Strategy: Retrieve EPR via BindingProvider.getEndpointReference().
   * From the returned EPR get the port via EndpointReference.getPort
   * (AddNumbers.class, wsf). WebServiceFeature is passed with Addressing=true.
   * Verify invocation behavior.
   *
   */
  public void EPRGetPortViaBPAndWSFTrueTest() throws Fault {
    TestUtil.logMsg("EPRGetPortViaBPAndWSFTrueTest");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Retrieve EPR via BindingProvider.getEndpointReference()");
      W3CEndpointReference epr = (W3CEndpointReference) bp
          .getEndpointReference();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      epr.writeTo(new StreamResult(baos));
      TestUtil.logMsg(baos.toString());
      TestUtil.logMsg(
          "Retrieve the port from the EPR via EndpointReference.getPort()");
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=true");
      AddNumbers retport = (AddNumbers) epr.getPort(AddNumbers.class, wsftrue);
      if (retport == null) {
        TestUtil
            .logErr("EPR.getPort(Class, wsftrue) returned null (unexpected)");
        pass = false;
      } else {
        TestUtil.logMsg("Verify invocation behavior on port");
        int result = retport.doAddNumbers(10, 10);
        TestUtil.logMsg("Invocation succeeded (expected) now check result");
        if (result != 20) {
          TestUtil.logErr("Expected result=20, got result=" + result);
          pass = false;
        } else
          TestUtil.logMsg("Got expected result=20");
      }
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRGetPortViaBPAndWSFTrueTest failed");
  }

  /*
   * @testName: EPRGetPortViaBPAndWSFFalseTest
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:SPEC:4028; JAXWS:SPEC:4028.1; JAXWS:JAVADOC:140;
   *
   * @test_Strategy: Retrieve EPR via BindingProvider.getEndpointReference().
   * From the returned EPR get the port via EndpointReference.getPort
   * (AddNumbers.class, wsf). WebServiceFeature is passed with Addressing=false.
   * Verify invocation behavior.
   *
   */
  public void EPRGetPortViaBPAndWSFFalseTest() throws Fault {
    TestUtil.logMsg("EPRGetPortViaBPAndWSFFalseTest");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Retrieve EPR via BindingProvider.getEndpointReference()");
      W3CEndpointReference epr = (W3CEndpointReference) bp
          .getEndpointReference();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      epr.writeTo(new StreamResult(baos));
      TestUtil.logMsg(baos.toString());
      TestUtil.logMsg(
          "Retrieve the port from the EPR via EndpointReference.getPort()");
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=false");
      AddNumbers retport = (AddNumbers) epr.getPort(AddNumbers.class, wsffalse);
      if (retport == null) {
        TestUtil
            .logErr("EPR.getPort(Class, wsffalse) returned null (unexpected)");
        pass = false;
      } else {
        TestUtil.logMsg("Verify invocation behavior on port");
        int result = retport.doAddNumbers(10, 10);
        TestUtil.logErr("Did not throw expected SOAPFaultException");
        pass = false;
      }
    } catch (SOAPFaultException e) {
      TestUtil.logMsg("Caught expected SOAPFaultException: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRGetPortViaBPAndWSFFalseTest failed");
  }

  /*
   * @testName: EPRViaWSCCreateDispatchWSFTrueAndInvokeTest1
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:JAVADOC:154; JAXWS:SPEC:4030;
   *
   * @test_Strategy: Retrieve EPR via WebServiceContext.getEndpointReference().
   * Create a Dispatch object via Service.createDispatch() using the returned
   * EPR, perform invocation via Dispatch.invoke() and then verify the result.
   * Pass WebServiceFeature with Addressing=true.
   *
   */
  public void EPRViaWSCCreateDispatchWSFTrueAndInvokeTest1() throws Fault {
    TestUtil.logMsg("EPRViaWSCCreateDispatchWSFTrueAndInvokeTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create a Dispatch object of type Source");
      W3CEndpointReference myepr = port.getW3CEPR1();
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=true");
      dispatchSrc = createDispatchSource(myepr, wsftrue);
      Source requestMsg = JAXWS_Util.makeSource(doAddNumbersRequest,
          "StreamSource");
      TestUtil.logMsg("Perform Dispatch invocation");
      Source responseMsg = dispatchSrc.invoke(requestMsg);
      TestUtil
          .logMsg("Dispatch invocation succeeded (expected) now check result");
      String responseStr = JAXWS_Util
          .getDOMResultAsString(JAXWS_Util.getSourceAsDOMResult(responseMsg));
      TestUtil.logMsg("responseStr=" + responseStr);
      if (responseStr.indexOf("doAddNumbersResponse") == -1
          || responseStr.indexOf("return") == -1
          || responseStr.indexOf("20") == -1) {
        TestUtil.logErr("Unexpected response results");
        pass = false;
      } else
        TestUtil.logMsg("Got expected response results");
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRViaWSCCreateDispatchWSFTrueAndInvokeTest1 failed");
  }

  /*
   * @testName: EPRViaWSCCreateDispatchWSFTrueAndInvokeTest2
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:JAVADOC:154; JAXWS:SPEC:4030;
   *
   * @test_Strategy: Retrieve EPR via WebServiceContext.getEndpointReference(
   * java.lang.Class). Create a Dispatch object via Service. createDispatch()
   * using the returned EPR, perform invocation via Dispatch.invoke() and then
   * verify the result. Pass WebServiceFeature with Addressing=true.
   *
   */
  public void EPRViaWSCCreateDispatchWSFTrueAndInvokeTest2() throws Fault {
    TestUtil.logMsg("EPRViaWSCCreateDispatchWSFTrueAndInvokeTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create a Dispatch object of type Source");
      W3CEndpointReference myepr = port.getW3CEPR2();
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=true");
      dispatchSrc = createDispatchSource(myepr, wsftrue);
      Source requestMsg = JAXWS_Util.makeSource(doAddNumbersRequest,
          "StreamSource");
      TestUtil.logMsg("Perform Dispatch invocation");
      Source responseMsg = dispatchSrc.invoke(requestMsg);
      TestUtil
          .logMsg("Dispatch invocation succeeded (expected) now check result");
      String responseStr = JAXWS_Util
          .getDOMResultAsString(JAXWS_Util.getSourceAsDOMResult(responseMsg));
      TestUtil.logMsg("responseStr=" + responseStr);
      if (responseStr.indexOf("doAddNumbersResponse") == -1
          || responseStr.indexOf("return") == -1
          || responseStr.indexOf("20") == -1) {
        TestUtil.logErr("Unexpected response results");
        pass = false;
      } else
        TestUtil.logMsg("Got expected response results");
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRViaWSCCreateDispatchWSFTrueAndInvokeTest2 failed");
  }

  /*
   * @testName: EPRViaWSCCreateDispatchWSFFalseAndInvokeTest3
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:JAVADOC:154; JAXWS:SPEC:4030;
   *
   * @test_Strategy: Retrieve EPR via WebServiceContext.getEndpointReference().
   * Create a Dispatch object via Service.createDispatch() using the returned
   * EPR. Pass WebServiceFeature with Addressing=false. Expect a
   * WebServiceException to be thrown.
   *
   */
  public void EPRViaWSCCreateDispatchWSFFalseAndInvokeTest3() throws Fault {
    TestUtil.logMsg("EPRViaWSCCreateDispatchWSFFalseAndInvokeTest3");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create a Dispatch object of type Source");
      W3CEndpointReference myepr = port.getW3CEPR1();
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=false");
      dispatchSrc = createDispatchSource(myepr, wsffalse);
      Source requestMsg = JAXWS_Util.makeSource(doAddNumbersRequest,
          "StreamSource");
      TestUtil.logMsg("Perform Dispatch invocation");
      Source responseMsg = dispatchSrc.invoke(requestMsg);
      TestUtil.logErr("Did not throw expected WebServiceException");
      pass = false;
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught expected WebServiceException: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRViaWSCCreateDispatchWSFFalseAndInvokeTest3 failed");
  }

  /*
   * @testName: EPRViaWSCCreateJAXBDispatchWSFTrueAndInvokeTest1
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:JAVADOC:154; JAXWS:SPEC:4030;
   *
   * @test_Strategy: Retrieve EPR via WebServiceContext.getEndpointReference().
   * Create a JAXB Dispatch object via Service.createDispatch() using the
   * returned EPR, perform invocation via Dispatch.invoke() and then verify the
   * result. Pass WebServiceFeature with Addressing=true.
   *
   */
  public void EPRViaWSCCreateJAXBDispatchWSFTrueAndInvokeTest1() throws Fault {
    TestUtil.logMsg("EPRViaWSCCreateJAXBDispatchWSFTrueAndInvokeTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create a Dispatch object of type JAXB");
      W3CEndpointReference myepr = port.getW3CEPR1();
      ObjectFactory of = new ObjectFactory();
      DoAddNumbers numbers = of.createDoAddNumbers();
      numbers.setArg0(10);
      numbers.setArg1(10);
      JAXBElement<DoAddNumbers> request = of.createDoAddNumbers(numbers);
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=true");
      dispatchJaxb = createDispatchJAXB(myepr, wsftrue);
      java.util.Map<String, Object> reqContext = dispatchJaxb
          .getRequestContext();
      TestUtil.logMsg("Perform Dispatch invocation");
      JAXBElement<DoAddNumbersResponse> response = (JAXBElement<DoAddNumbersResponse>) dispatchJaxb
          .invoke(request);
      TestUtil
          .logMsg("Dispatch invocation succeeded (expected) now check result");
      int result = response.getValue().getReturn();
      TestUtil.logMsg("result=" + result);
      if (result != 20) {
        TestUtil.logErr("Expected result=20, got result=" + result);
        pass = false;
      } else
        TestUtil.logMsg("Got expected response results");
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault(
          "EPRViaWSCCreateJAXBDispatchWSFTrueAndInvokeTest1 failed");
  }

  /*
   * @testName: EPRViaWSCCreateJAXBDispatchWSFFalseAndInvokeTest2
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:JAVADOC:154; JAXWS:SPEC:4030;
   *
   * @test_Strategy: Retrieve EPR via WebServiceContext.getEndpointReference().
   * Create a JAXB Dispatch object via Service.createDispatch() using the
   * returned EPR, perform invocation via Dispatch. invoke() and then verify the
   * result. Pass WebServiceFeature with Addressing=false. Expect a
   * WebServiceException to be thrown.
   *
   */
  public void EPRViaWSCCreateJAXBDispatchWSFFalseAndInvokeTest2() throws Fault {
    TestUtil.logMsg("EPRViaWSCCreateJAXBDispatchWSFFalseAndInvokeTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create a Dispatch object of type JAXB");
      W3CEndpointReference myepr = port.getW3CEPR1();
      ObjectFactory of = new ObjectFactory();
      DoAddNumbers numbers = of.createDoAddNumbers();
      numbers.setArg0(10);
      numbers.setArg1(10);
      JAXBElement<DoAddNumbers> request = of.createDoAddNumbers(numbers);
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=false");
      dispatchJaxb = createDispatchJAXB(myepr, wsffalse);
      java.util.Map<String, Object> reqContext = dispatchJaxb
          .getRequestContext();
      TestUtil.logMsg("Perform Dispatch invocation");
      JAXBElement<DoAddNumbersResponse> response = (JAXBElement<DoAddNumbersResponse>) dispatchJaxb
          .invoke(request);
      TestUtil.logErr("Did not throw expected WebServiceException");
      pass = false;
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught expected WebServiceException: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault(
          "EPRViaWSCCreateJAXBDispatchWSFFalseAndInvokeTest2 failed");
  }

  /*
   * @testName: EPRViaBPCreateDispatchWSFTrueAndInvokeTest1
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:JAVADOC:154; JAXWS:SPEC:4030;
   *
   * @test_Strategy: Retrieve EPR via BindingProvider.getEndpointReference().
   * Create a Dispatch object via Service.createDispatch() using the returned
   * EPR, perform invocation via Dispatch.invoke() and then verify the result.
   * Pass WebServiceFeature with Addressing=true.
   *
   */
  public void EPRViaBPCreateDispatchWSFTrueAndInvokeTest1() throws Fault {
    TestUtil.logMsg("EPRViaBPCreateDispatchWSFTrueAndInvokeTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create a Dispatch object of type Source");
      W3CEndpointReference myepr = (W3CEndpointReference) bp
          .getEndpointReference();
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=true");
      dispatchSrc = createDispatchSource(myepr, wsftrue);
      Source requestMsg = JAXWS_Util.makeSource(doAddNumbersRequest,
          "StreamSource");
      TestUtil.logMsg("Perform Dispatch invocation");
      Source responseMsg = dispatchSrc.invoke(requestMsg);
      TestUtil
          .logMsg("Dispatch invocation succeeded (expected) now check result");
      String responseStr = JAXWS_Util
          .getDOMResultAsString(JAXWS_Util.getSourceAsDOMResult(responseMsg));
      TestUtil.logMsg("responseStr=" + responseStr);
      if (responseStr.indexOf("doAddNumbersResponse") == -1
          || responseStr.indexOf("return") == -1
          || responseStr.indexOf("20") == -1) {
        TestUtil.logErr("Unexpected response results");
        pass = false;
      } else
        TestUtil.logMsg("Got expected response results");
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRViaBPCreateDispatchWSFTrueAndInvokeTest1 failed");
  }

  /*
   * @testName: EPRViaBPCreateDispatchWSFTrueAndInvokeTest2
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:JAVADOC:154; JAXWS:SPEC:4030;
   *
   * @test_Strategy: Retrieve EPR via BindingProvider.getEndpointReference(
   * java.lang.Class). Create a Dispatch object via Service. createDispatch()
   * using the returned EPR, perform invocation and then verify the result. Pass
   * WebServiceFeature with Addressing=true.
   *
   */
  public void EPRViaBPCreateDispatchWSFTrueAndInvokeTest2() throws Fault {
    TestUtil.logMsg("EPRViaBPCreateDispatchWSFTrueAndInvokeTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create a Dispatch object of type SOAPMessage");
      W3CEndpointReference myepr = (W3CEndpointReference) bp
          .getEndpointReference(
              jakarta.xml.ws.wsaddressing.W3CEndpointReference.class);
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=true");
      dispatchSM = createDispatchSOAPMessage(myepr, wsftrue);
      SOAPMessage requestMsg = JAXWS_Util
          .makeSOAPMessage(doAddNumbersRequestSM);
      TestUtil.logMsg("Perform Dispatch invocation");
      SOAPMessage responseMsg = dispatchSM.invoke(requestMsg);
      TestUtil
          .logMsg("Dispatch invocation succeeded (expected) now check result");
      String responseStr = JAXWS_Util.getSOAPMessageAsString(responseMsg);
      TestUtil.logMsg("responseStr=" + responseStr);
      if (responseStr.indexOf("doAddNumbersResponse") == -1
          || responseStr.indexOf("return") == -1
          || responseStr.indexOf("20") == -1) {
        TestUtil.logErr("Unexpected response results");
        pass = false;
      } else
        TestUtil.logMsg("Got expected response results");
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRViaBPCreateDispatchWSFTrueAndInvokeTest2 failed");
  }

  /*
   * @testName: EPRViaBPCreateDispatchWSFFalseAndInvokeTest3
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:JAVADOC:154; JAXWS:SPEC:4030;
   *
   * @test_Strategy: Retrieve EPR via BindingProvider.getEndpointReference(
   * java.lang.Class). Create a Dispatch object via Service. createDispatch()
   * using the returned EPR, perform invocation via Dispatch.invoke() and then
   * verify the result. Pass WebServiceFeature with Addressing=false. Expect a
   * WebServiceException to be thrown.
   *
   */
  public void EPRViaBPCreateDispatchWSFFalseAndInvokeTest3() throws Fault {
    TestUtil.logMsg("EPRViaBPCreateDispatchWSFFalseAndInvokeTest3");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create a Dispatch object of type SOAPMessage");
      W3CEndpointReference myepr = (W3CEndpointReference) bp
          .getEndpointReference(
              jakarta.xml.ws.wsaddressing.W3CEndpointReference.class);
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=false");
      dispatchSM = createDispatchSOAPMessage(myepr, wsffalse);
      SOAPMessage requestMsg = JAXWS_Util
          .makeSOAPMessage(doAddNumbersRequestSM);
      TestUtil.logMsg("Perform Dispatch invocation");
      SOAPMessage responseMsg = dispatchSM.invoke(requestMsg);
      TestUtil.logErr("Did not throw expected WebServiceException");
      pass = false;
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught expected WebServiceException: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRViaBPCreateDispatchWSFFalseAndInvokeTest3 failed");
  }

  /*
   * @testName: EPRViaBPCreateJAXBDispatchWSFTrueAndInvokeTest1
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:JAVADOC:154; JAXWS:SPEC:4030;
   *
   * @test_Strategy: Retrieve EPR via BindingProvider.getEndpointReference(
   * java.lang.Class). Create a JAXB Dispatch object via Service.
   * createDispatch() using the returned EPR, perform invocation via
   * Dispatch.invoke() and then verify the result. Pass WebServiceFeature with
   * Addressing=true.
   *
   */
  public void EPRViaBPCreateJAXBDispatchWSFTrueAndInvokeTest1() throws Fault {
    TestUtil.logMsg("EPRViaBPCreateJAXBDispatchWSFTrueAndInvokeTest1");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create a Dispatch object of type JAXB");
      W3CEndpointReference myepr = (W3CEndpointReference) bp
          .getEndpointReference(
              jakarta.xml.ws.wsaddressing.W3CEndpointReference.class);
      ObjectFactory of = new ObjectFactory();
      DoAddNumbers numbers = of.createDoAddNumbers();
      numbers.setArg0(10);
      numbers.setArg1(10);
      JAXBElement<DoAddNumbers> request = of.createDoAddNumbers(numbers);
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=true");
      dispatchJaxb = createDispatchJAXB(myepr, wsftrue);
      java.util.Map<String, Object> reqContext = dispatchJaxb
          .getRequestContext();
      TestUtil.logMsg("Perform Dispatch invocation");
      JAXBElement<DoAddNumbersResponse> response = (JAXBElement<DoAddNumbersResponse>) dispatchJaxb
          .invoke(request);
      TestUtil
          .logMsg("Dispatch invocation succeeded (expected) now check result");
      int result = response.getValue().getReturn();
      TestUtil.logMsg("result=" + result);
      if (result != 20) {
        TestUtil.logErr("Expected result=20, got result=" + result);
        pass = false;
      } else
        TestUtil.logMsg("Got expected response results");
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault("EPRViaBPCreateJAXBDispatchWSFTrueAndInvokeTest1 failed");
  }

  /*
   * @testName: EPRViaBPCreateJAXBDispatchWSFFalseAndInvokeTest2
   *
   * @assertion_ids: WSAMD:SPEC:2000; WSAMD:SPEC:2000.1; WSAMD:SPEC:2000.2;
   * WSAMD:SPEC:2001; WSAMD:SPEC:2001.1; WSAMD:SPEC:2001.2; WSAMD:SPEC:2001.3;
   * WSAMD:SPEC:2002; WSAMD:SPEC:2002.1; WSAMD:SPEC:2002.2; WSAMD:SPEC:2002.3;
   * WSAMD:SPEC:2002.4; WSACORE:SPEC:2007; WSACORE:SPEC:2008; WSACORE:SPEC:2009;
   * JAXWS:JAVADOC:154; JAXWS:SPEC:4030;
   *
   * @test_Strategy: Retrieve EPR via BindingProvider.getEndpointReference(
   * java.lang.Class). Create a JAXB Dispatch object via Service.
   * createDispatch() using the returned EPR, perform invocation via
   * Dispatch.invoke() and then verify the result. Pass WebServiceFeature with
   * Addressing=false. Expect a WebServiceException to be thrown.
   *
   */
  public void EPRViaBPCreateJAXBDispatchWSFFalseAndInvokeTest2() throws Fault {
    TestUtil.logMsg("EPRViaBPCreateJAXBDispatchWSFFalseAndInvokeTest2");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create a Dispatch object of type JAXB");
      W3CEndpointReference myepr = (W3CEndpointReference) bp
          .getEndpointReference(
              jakarta.xml.ws.wsaddressing.W3CEndpointReference.class);
      ObjectFactory of = new ObjectFactory();
      DoAddNumbers numbers = of.createDoAddNumbers();
      numbers.setArg0(10);
      numbers.setArg1(10);
      JAXBElement<DoAddNumbers> request = of.createDoAddNumbers(numbers);
      TestUtil.logMsg("Pass WebServiceFeature with Addressing=false");
      dispatchJaxb = createDispatchJAXB(myepr, wsffalse);
      java.util.Map<String, Object> reqContext = dispatchJaxb
          .getRequestContext();
      TestUtil.logMsg("Perform Dispatch invocation");
      JAXBElement<DoAddNumbersResponse> response = (JAXBElement<DoAddNumbersResponse>) dispatchJaxb
          .invoke(request);
      TestUtil.logErr("Did not throw expected WebServiceException");
      pass = false;
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught expected WebServiceException: " + e.getMessage());
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Caught unexpected exception: ", e);
    }
    if (!pass)
      throw new Fault(
          "EPRViaBPCreateJAXBDispatchWSFFalseAndInvokeTest2 failed");
  }
}
