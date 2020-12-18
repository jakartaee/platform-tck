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
 */
/*
 * $Id$
 */

package com.sun.ts.tests.jws.webservice.webservice4.client;

import java.net.URL;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.jws.common.JWS_Util;
import com.sun.ts.tests.jws.common.WsdlUtils;
import com.sun.ts.tests.jws.common.XMLUtils;

/**
 * @test
 * @executeClass com.sun.ts.tests.webservice.webservice4.client.Client
 * @sources Client.java
 * @keywords webservice
 */

public class Client extends ServiceEETest {

  static EndpointInterface2WebServiceService service = null;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "jwstest.endpointinterface2webservice.endpoint.1";

  private static final String EXPECTED_RESULT_1 = "hello jsr181 to Web Service";

  private static final String EXPECTED_RESULT_2 = "hello1 jsr181 to Web Service";

  private static final String EXPECTED_RESULT_3 = "hello2 jsr181 to Web Service";

  private static final String HOSTNAME = "localhost";

  private static final String MODEPROP = "platform.mode";

  // service and port information
  private static final String NAMESPACEURI = "http://server.webservice4.webservice.jws.tests.ts.sun.com/";

  private static final String PKG_NAME = "com.sun.ts.tests.jws.webservice.webservice4.client.";

  private static final String PORT_NAME = "endpointInterface2WebServicePort";

  private static final int PORTNUM = 8000;

  private static final String PORTTYPE_NAME = "endpointInterface2WebServiceEI";

  private static final String PORTTYPE_NAMESPACEURI = "http://bea/jsr181/tck/portType";

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String SERVICE_NAME = "endpointInterface2WebServiceService";

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String WSDLLOC_URL = "jwstest.endpointinterface2webservice.wsdlloc.1";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  private TSURL ctsurl = new TSURL();

  // Document used to check the WSDL
  private Document doc;

  private String hostname = HOSTNAME;

  String modeProperty = null; // platform.mode -> (standalone|j2ee)

  private EndpointInterface2WebServiceEI port;

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private int portnum = PORTNUM;

  private QName PORTTYPE_QNAME = new QName(PORTTYPE_NAMESPACEURI,
      PORTTYPE_NAME);

  private Properties props = null;

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private String url = null;

  private URL wsdlurl = null;

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (EndpointInterface2WebServiceEI) service
        .getPort(EndpointInterface2WebServiceEI.class);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
  }

  private void getPortStandalone() throws Exception {
    port = (EndpointInterface2WebServiceEI) JWS_Util.getPort(wsdlurl,
        SERVICE_QNAME, EndpointInterface2WebServiceService.class, PORT_QNAME,
        EndpointInterface2WebServiceEI.class);
    JWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  /*
   * @class.testArgs: -ap jws-url-props.dat @class.setup_props: webServerHost;
   * webServerPort; platform.mode;
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
      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        getPortStandalone();
      } else {
        getTestURLs();
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (EndpointInterface2WebServiceService) getSharedObject();
        getPortJavaEE();
      }

      doc = WsdlUtils.getDocument(wsdlurl.toString());
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
  }

  /*
   * @testName: testHello
   *
   * @assertion_ids: JWS:SPEC:3007; JWS:SPEC:3009; JWS:SPEC:3010; JWS:SPEC:3012;
   * JWS:SPEC:3013
   *
   * @test_Strategy:
   */
  public void testHello() throws Fault {
    TestUtil.logMsg("testPingRPC");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Invoke the hello() method of endpointInterface2WebService");
      String result = port.hello("jsr181");
      if (!result.equals(EXPECTED_RESULT_1))
        pass = false;
      TestUtil.logMsg("Invocation of hello() passed");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHello failed", e);
    }

    if (!pass)
      throw new Fault("testHello failed");
    TestUtil.logMsg("Invocation of hello() passed");
  }

  /*
   * @testName: testHello1
   *
   * @assertion_ids: JWS:SPEC:3007; JWS:SPEC:3009; JWS:SPEC:3010; JWS:SPEC:3012;
   * JWS:SPEC:3013
   *
   * @test_Strategy:
   */
  public void testHello1() throws Fault {
    TestUtil.logMsg("testPingRPC");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Invoke the hello1() method of endpointInterface2WebService");
      String result = port.hello1("jsr181");
      if (!result.equals(EXPECTED_RESULT_2))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHello1 failed", e);
    }

    if (!pass)
      throw new Fault("testHello1 failed");
    TestUtil.logMsg("Invocation of hello1() passed");
  }

  /*
   * @testName: testHello2
   *
   * @assertion_ids: JWS:SPEC:3007; JWS:SPEC:3008; JWS:SPEC:3009; JWS:SPEC:3010
   *
   * @test_Strategy:
   */
  public void testHello2() throws Fault {
    TestUtil.logMsg("testPingRPC");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Invoke the hello2() method of endpointInterface2WebService");
      String result = port.hello2("jsr181");
      if (!result.equals(EXPECTED_RESULT_3))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHello2 failed", e);
    }

    if (!pass)
      throw new Fault("testHello2 failed");
    TestUtil.logMsg("Invocation of hello2() passed");
  }

  /*
   * @testName: testHelloWSDL
   *
   * @assertion_ids: JWS:SPEC:3007; JWS:SPEC:3008
   *
   * @test_Strategy: Hello WSDL
   *
   */
  public void testHelloWSDL() throws Fault {
    TestUtil.logMsg("testPingWSDL");
    boolean pass = true;
    try {
      TestUtil.logMsg("Get the WSDL of endpointInterface2WebService");
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      TestUtil.logMsg("Parse WSDL from location " + wsdlurl.toString());
      Document document = builder.parse(wsdlurl.toString());
      XMLUtils.xmlDumpDocument(document);
      NodeList nodeList = document.getElementsByTagName("*");
      if (nodeList.getLength() <= 0) {
        TestUtil.logErr("WSDL did not parse successfully");
        pass = false;
      }
      TestUtil.logMsg("WSDL parsed successfully");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testPingWSDL failed", e);
    }

    if (!pass)
      throw new Fault("testHelloWSDL failed");
  }

  /*
   * @testName: testWSDL1
   *
   * @assertion_ids: JWS:SPEC:6003
   *
   * @test_Strategy: Check for the style attribute in SOAP Binding
   *
   */

  // Need to clean up this method
  public void testWSDL1() throws Fault {
    boolean pass = false;
    String attValue = null;
    Node node = null;
    Attr att = null;
    Element soapBindingElement;
    Attr bindingAttr = null;
    String bindingValue = null;
    QName bindingQName = null;

    try {

      // Fix me find the binding element from the service/port element.
      WsdlUtils.setBaseUrl(wsdlurl.toString());
      Element elm = com.sun.ts.tests.jws.common.WsdlUtils
          .findServiceElement(doc, SERVICE_QNAME);

      if (elm == null) {
        throw new Fault(
            "testWSDL1 failed : could not find service element with name "
                + SERVICE_NAME + " in the WSDL");
      }
      Element portElement = com.sun.ts.tests.jws.common.WsdlUtils
          .findPortElement(doc, elm, PORT_QNAME);

      if (portElement != null) {

        att = portElement.getAttributeNode("name");

        if (att != null && att.getValue().equals(PORT_NAME)) {

          // check if we need to use the QName here while getting the
          // Attribute

          bindingAttr = portElement.getAttributeNode("binding");

          if (bindingAttr != null && bindingAttr.getValue() != null) {

            bindingValue = bindingAttr.getValue();

            TestUtil.logMsg(" Binding Value : " + bindingValue);

            bindingQName = com.sun.ts.tests.jws.common.WsdlUtils
                .createQName(portElement, bindingValue);

            if (bindingQName != null) {

              Element bindingElement = com.sun.ts.tests.jws.common.WsdlUtils
                  .findSoapBindingElement(doc, bindingQName);

              if (bindingElement != null) {

                NodeList soapBindingElementList = bindingElement
                    .getChildNodes();

                for (int i = 0; i < soapBindingElementList.getLength(); i++) {

                  node = soapBindingElementList.item(i);

                  if (!(node instanceof Element))
                    continue;

                  soapBindingElement = (Element) node;

                  TestUtil.logMsg(node.toString());
                  att = soapBindingElement.getAttributeNode("style");
                  if (att != null && att.getValue().equals("document")) {

                    pass = true;
                  }
                }
              }

            }
          }
        }
      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL1 failed", ex);
    }

    if (!pass)
      throw new Fault(
          "testWSDL1 failed : style is not set to document in the WSDL "
              + attValue + "  " + att);
    if (pass)
      TestUtil
          .logMsg("testWSDL1 passed : style is set to document in the WSDL ");

  }

  /*
   * @testName: testWSDL2
   *
   * @assertion_ids: JWS:SPEC:5003; JWS:SPEC:5005
   *
   * @test_Strategy: Check for the ServiceName and the PortName in the WSDL
   *
   */

  public void testWSDL2() throws Fault {
    boolean pass = false;

    Attr att = null;
    try {

      WsdlUtils.setBaseUrl(wsdlurl.toString());
      Element elm = com.sun.ts.tests.jws.common.WsdlUtils
          .findServiceElement(doc, SERVICE_QNAME);
      if (elm == null)
        throw new Fault(
            "testWSDL2 failed : could not find service element with name "
                + SERVICE_NAME + " in the WSDL");
      Element portElement = WsdlUtils.findPortElement(doc, elm, PORT_QNAME);
      if (portElement != null) {

        att = portElement.getAttributeNode("name");
        if (att != null && att.getValue().equals(PORT_NAME))
          pass = true;

      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL2 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(
          " testWSDL2() passed :  Found the service element " + SERVICE_QNAME
              + " and the port element " + PORT_QNAME + " in the WSDL ");
    } else {
      throw new Fault(
          "testWSDL2 failed : could not find port element with name "
              + PORT_NAME + " in the WSDL");
    }

  }

  /*
   * @testName: testWSDL3
   *
   * @assertion_ids: JWS:SPEC:5002
   *
   * @test_Strategy: Check for the PortType in the WSDL
   *
   */

  public void testWSDL3() throws Fault {
    boolean pass = false;

    try {
      WsdlUtils.setBaseUrl(wsdlurl.toString());
      Element elm = WsdlUtils.findPortTypeElement(doc, PORTTYPE_QNAME);
      if (elm != null)
        pass = true;

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL2 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(" testWSDL3() passed :  Found the portType element "
          + PORTTYPE_QNAME + " in the WSDL ");
    } else {
      throw new Fault(
          "testWSDL3 failed : could not find portType element with name "
              + PORTTYPE_QNAME + " in the WSDL");
    }

  }

}
