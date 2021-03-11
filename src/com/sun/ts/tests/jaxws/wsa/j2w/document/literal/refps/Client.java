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
 * $Id: Client.java 51837 2006-11-09 16:48:53Z adf $
 */

package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.refps;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPException;

import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsa.common.*;
import com.sun.ts.tests.jaxws.sharedclients.*;

import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.w3c.dom.Document;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.transform.stream.StreamResult;
import java.util.Iterator;

import java.net.URL;

import jakarta.xml.ws.*;
import jakarta.xml.ws.soap.*;
import jakarta.xml.ws.wsaddressing.*;
import jakarta.xml.soap.*;
import java.io.*;

import java.util.Properties;
import java.util.UUID;
import java.text.MessageFormat;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.wsa.j2w.document.literal.refps.";

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "wsaj2wdlreferenceparamstest.endpoint.1";

  private static final String WSDLLOC_URL = "wsaj2wdlreferenceparamstest.wsdlloc.1";

  private String url = null;

  // service and port information
  private static final String NAMESPACEURI = "http://example.com";

  private static final String SERVICE_NAME = "AddNumbersService";

  private static final String PORT_NAME = "AddNumbersPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private static final String CORRECT_ACTION = "addInAction";

  private Dispatch<SOAPMessage> dispatchSM;

  private URL wsdlurl = null;

  AddNumbersPortType port = null;

  static AddNumbersService service = null;

  private static String xmlInterfaceName = "<wsam:InterfaceName xmlns:wsam=\"http://www.w3.org/2007/05/addressing/metadata\" xmlns:myns=\"http://example.com\">myns:AddNumbersPortType</wsam:InterfaceName>";

  private static String xmlRefParam1 = "<myns1:MyParam1 IsReferenceParameter='true' xmlns:myns1=\"http://example.com/myparam1\">Hello</myns1:MyParam1>";

  private static String xmlRefParam2 = "<myns2:MyParam2 IsReferenceParameter='true' xmlns:myns2=\"http://example.com/myparam2\">There</myns2:MyParam2>";

  public static final String REQUEST_REFERENCE_PARAMETERS = "<?xml version=\"1.0\"?><S:Envelope "
      + "xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" "
      + "xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">" + "<S:Header>"
      + "<wsa:To>{0}</wsa:To>\n" + "<wsa:MessageID>uuid:{1}</wsa:MessageID>\n"
      + "<wsa:ReplyTo>\n" + "  <wsa:Address>{2}</wsa:Address>\n"
      + "</wsa:ReplyTo>\n" + "<wsa:Action>{3}</wsa:Action>\n" + "</S:Header>\n"
      + "<S:Body>\n"
      + "<addNumbersAndPassString xmlns=\"http://example.com\">\n"
      + "  <number1>10</number1>\n" + "  <number2>10</number2>\n"
      + "  <testString>RequestReferenceParamaters</testString>\n"
      + "</addNumbersAndPassString>\n" + "</S:Body></S:Envelope>";

  public static final String REPLY_TO_REFPS_MESSAGE = "<?xml version=\"1.0\"?><S:Envelope "
      + "xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" "
      + "xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">" + "<S:Header>"
      + "<wsa:To>{0}</wsa:To>\n" + "<wsa:MessageID>uuid:{1}</wsa:MessageID>\n"
      + "<wsa:ReplyTo>\n" + "  <wsa:Address>{2}</wsa:Address>\n"
      + "  <wsa:ReferenceParameters>\n"
      + "    <ck:CustomerKey xmlns:ck=\"http://example.org/customer\">Key#123456789</ck:CustomerKey>\n"
      + "  </wsa:ReferenceParameters>" + "</wsa:ReplyTo>\n"
      + "<wsa:Action>{3}</wsa:Action>\n" + "</S:Header>\n" + "<S:Body>\n"
      + "<addNumbers xmlns=\"http://example.com\">\n"
      + "  <number1>10</number1>\n" + "  <number2>10</number2>\n"
      + "</addNumbers>\n" + "</S:Body></S:Envelope>";

  public static final String FAULT_TO_REFPS_MESSAGE = "<S:Envelope "
      + "xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\" "
      + "xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">" + "<S:Header>\n"
      + "<wsa:To>{0}</wsa:To>\n" + "<wsa:MessageID>uuid:{1}</wsa:MessageID>\n"
      + "<wsa:ReplyTo>\n" + "  <wsa:Address>{2}</wsa:Address>\n"
      + "  <wsa:ReferenceParameters>\n"
      + "    <ck:CustomerKey xmlns:ck=\"http://example.org/customer\">Key#123456789</ck:CustomerKey>\n"
      + "  </wsa:ReferenceParameters>" + "</wsa:ReplyTo>\n" + "<wsa:FaultTo>\n"
      + "  <wsa:Address>{3}</wsa:Address>\n" + "  <wsa:ReferenceParameters>\n"
      + "    <ck:CustomerKey xmlns:ck=\"http://example.org/customer\">Fault#123456789</ck:CustomerKey>\n"
      + "  </wsa:ReferenceParameters>" + "</wsa:FaultTo>\n"
      + "<wsa:Action>{4}</wsa:Action>\n" + "</S:Header>\n" + "<S:Body>\n"
      + "<addNumbers xmlns=\"http://example.com\">\n"
      + "  <number1>-10</number1>\n" + "  <number2>10</number2>\n"
      + "</addNumbers>\n" + "</S:Body></S:Envelope>";

  private static AddressingFeature ENABLED_ADDRESSING_FEATURE = new AddressingFeature(
      true, true);

  private static AddressingFeature DISABLED_ADDRESSING_FEATURE = new AddressingFeature(
      false);

  private static WebServiceFeature[] enabledRequiredwsf = {
      new AddressingFeature(true, true) };

  // Reference parameter constants
  private QName CUSTOMER_KEY = new QName("http://example.org/customer",
      "CustomerKey");

  private static final String CUSTOMER_KEY_VALUE = "Key#123456789";

  private QName IS_REF_PARAM = new QName("http://www.w3.org/2005/08/addressing",
      "IsReferenceParameter");

  private static final String IS_REF_PARAM_VALUE = "1";

  private boolean validate(SOAPMessage sm, QName refp, String refp_value,
      QName isRefParam, String isRefParam_value) {
    try {
      TestUtil.logMsg("get SOAPHeader");
      SOAPHeader header = sm.getSOAPHeader();
      TestUtil.logMsg("get Child Element for " + refp);
      Iterator iter = header.getChildElements(refp);
      if (!iter.hasNext()) {
        TestUtil.logErr("SOAPMessage response does not contain" + refp);
        return false;
      }
      Element element = (Element) iter.next();
      TestUtil.logMsg("get Attribute node for " + isRefParam);
      Attr attr = element.getAttributeNodeNS(isRefParam.getNamespaceURI(),
          isRefParam.getLocalPart());
      if (attr == null) {
        TestUtil.logErr("attribute not found: " + isRefParam);
        return false;
      }
      TestUtil.logMsg("get all Child Nodes");
      NodeList nodes = element.getChildNodes();
      boolean found = false;
      for (int i = 0; i < nodes.getLength(); i++) {
        Node node = (Node) nodes.item(i);
        if (node instanceof Text) {
          TestUtil.logMsg("got TEXT node");
          String value = node.getNodeValue();
          TestUtil.logMsg("value=[" + value + "]");
          if (!value.equals(CUSTOMER_KEY_VALUE)) {
            TestUtil.logErr("CUSTOMER_KEY_VALUE: expected: "
                + CUSTOMER_KEY_VALUE + ", received: " + value);
            return false;
          } else
            found = true;
        }
      }
      if (!found) {
        TestUtil
            .logErr("Did not found CUSTOMER_KEY_VALUE: " + CUSTOMER_KEY_VALUE);
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  private Dispatch<SOAPMessage> createDispatchSOAPMessage(QName port,
      boolean enabled) throws Exception {
    if (enabled)
      return service.createDispatch(port, SOAPMessage.class,
          jakarta.xml.ws.Service.Mode.MESSAGE, ENABLED_ADDRESSING_FEATURE);
    else
      return service.createDispatch(port, SOAPMessage.class,
          jakarta.xml.ws.Service.Mode.MESSAGE, DISABLED_ADDRESSING_FEATURE);
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
    port = (AddNumbersPortType) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        AddNumbersService.class, PORT_QNAME, AddNumbersPortType.class,
        enabledRequiredwsf);
    TestUtil.logMsg("port=" + port);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (AddNumbersPortType) service.getAddNumbersPort();
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
    JAXWS_Util.dumpTargetEndpointAddress(port);
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
        service = (AddNumbersService) JAXWS_Util.getService(wsdlurl,
            SERVICE_QNAME, AddNumbersService.class);
        getPortStandalone();
      } else {
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
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
   * @testName: testReplyToRefps
   *
   * @assertion_ids: WSACORE:SPEC:3012; WSACORE:SPEC:3012.1;
   * WSACORE:SPEC:3012.2; WSACORE:SPEC:2004; WSACORE:SPEC:2004.3;
   * WSACORE:SPEC:3021;
   *
   * @test_Strategy:
   *
   */
  public void testReplyToRefps() throws Fault {
    boolean pass = true;
    try {

      String soapmsg = MessageFormat.format(REPLY_TO_REFPS_MESSAGE, url,
          UUID.randomUUID(), WsaSOAPUtils.getAddrVerAnonUri(), CORRECT_ACTION);
      dispatchSM = createDispatchSOAPMessage(PORT_QNAME, false);
      SOAPMessage request = JAXWS_Util.makeSOAPMessage(soapmsg);
      TestUtil.logMsg("Dumping SOAP Request ...");
      JAXWS_Util.dumpSOAPMessage(request, false);
      SOAPMessage response = dispatchSM.invoke(request);
      TestUtil.logMsg("Dumping SOAP Response ...");
      JAXWS_Util.dumpSOAPMessage(response, false);
      pass = validate(response, CUSTOMER_KEY, CUSTOMER_KEY_VALUE, IS_REF_PARAM,
          IS_REF_PARAM_VALUE);
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("testReplyToRefps failed", e);
    }

    if (!pass)
      throw new Fault("testReplyToRefps failed");
  }

  /*
   * @testName: testFaultToRefps
   *
   * @assertion_ids: WSACORE:SPEC:3012; WSACORE:SPEC:3012.1;
   * WSACORE:SPEC:3012.2; WSACORE:SPEC:2004; WSACORE:SPEC:2004.3;
   * WSACORE:SPEC:3021;
   *
   * @test_Strategy:
   *
   */
  public void testFaultToRefps() throws Fault {
    boolean pass = true;

    try {
      String soapmsg = MessageFormat.format(FAULT_TO_REFPS_MESSAGE, url,
          UUID.randomUUID(), WsaSOAPUtils.getAddrVerAnonUri(),
          WsaSOAPUtils.getAddrVerAnonUri(), CORRECT_ACTION);
      dispatchSM = createDispatchSOAPMessage(PORT_QNAME, false);
      SOAPMessage request = JAXWS_Util.makeSOAPMessage(soapmsg);
      TestUtil.logMsg("Dumping SOAP Request ...");
      JAXWS_Util.dumpSOAPMessage(request, false);
      SOAPMessage response = dispatchSM.invoke(request);
      JAXWS_Util.dumpSOAPMessage(response, false);
      pass = false;
      TestUtil.logErr("SOAPFaultException must be thrown");
    } catch (SOAPFaultException e) {
      TestUtil.logMsg("Caught expected SOAPFaultException: " + e.getMessage());
      try {
        TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(e));
        TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(e));
        String faultdetail[] = WsaSOAPUtils.getFaultDetail(e);
        if (faultdetail != null) {
          StringBuffer output = new StringBuffer("FaultDetail:");
          for (int i = 0; faultdetail[i] != null; i++) {
            output.append(" " + faultdetail[i]);
          }
          TestUtil.logMsg(output.toString());
        }
      } catch (Exception e2) {
        TestUtil.logErr("Caught unexpected exception: " + e2.getMessage());
        throw new Fault("testFaultToRefps failed", e2);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("testFaultToRefps failed", e);
    }

    if (!pass)
      throw new Fault("testFaultToRefps failed");
  }

  /*
   * @testName: RequestReferenceParametersTest
   *
   * @assertion_ids: JAXWS:SPEC:9042; JAXWS:SPEC:9042.1;
   *
   * @test_Strategy: Send reference paramaters on soap request
   */
  public void RequestReferenceParametersTest() throws Fault {
    TestUtil.logMsg("RequestReferenceParametersTest");
    boolean pass = true;

    try {
      W3CEndpointReferenceBuilder builder = new W3CEndpointReferenceBuilder();
      builder = builder.address(url);
      DOMSource domsrc = (DOMSource) JAXWS_Util.makeSource(xmlInterfaceName,
          "DOMSource");
      Document document = (Document) domsrc.getNode();
      builder = builder.metadata(document.getDocumentElement());
      builder = builder.serviceName(SERVICE_QNAME);
      builder = builder.endpointName(PORT_QNAME);
      builder = builder.wsdlDocumentLocation(wsdlurl.toString());
      domsrc = (DOMSource) JAXWS_Util.makeSource(xmlRefParam1, "DOMSource");
      document = (Document) domsrc.getNode();
      builder = builder.referenceParameter(document.getDocumentElement());
      domsrc = (DOMSource) JAXWS_Util.makeSource(xmlRefParam2, "DOMSource");
      document = (Document) domsrc.getNode();
      builder = builder.referenceParameter(document.getDocumentElement());
      W3CEndpointReference epr = builder.build();
      TestUtil.logMsg("---------------------------");
      TestUtil.logMsg("DUMP OF ENDPOINT REFERENCE");
      TestUtil.logMsg("---------------------------");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      epr.writeTo(new StreamResult(baos));
      TestUtil.logMsg(baos.toString());
      AddNumbersPortType myport = (AddNumbersPortType) service.getPort(epr,
          AddNumbersPortType.class, ENABLED_ADDRESSING_FEATURE);
      myport.addNumbersAndPassString(10, 10, "RequestReferenceParametersTest");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred");
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("RequestReferenceParametersTest failed");
  }
}
