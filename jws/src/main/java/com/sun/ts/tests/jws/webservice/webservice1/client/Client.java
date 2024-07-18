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
package com.sun.ts.tests.jws.webservice.webservice1.client;

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
 * @executeClass com.sun.ts.tests.jws.webservice.webservice1.client.Client
 * @sources Client.java
 * @keywords webservice
 */

public class Client extends ServiceEETest {

  static DefaultWebServiceService service = null;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "jwstest.defaultwebservice.endpoint.1";

  private static final String HOSTNAME = "localhost";

  private static final String MODEPROP = "platform.mode";

  // service and port information
  private static final String NAMESPACEURI = "http://server.webservice1.webservice.jws.tests.ts.sun.com/";

  private static final String PKG_NAME = "com.sun.ts.tests.jws.webservice.webservice1.client.";

  private static final String PORT_NAME = "defaultWebServicePort";

  private static final int PORTNUM = 8000;

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String SERVICE_NAME = "defaultWebServiceService";

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String WSDLLOC_URL = "jwstest.defaultwebservice.wsdlloc.1";

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

  private DefaultWebService port;

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private int portnum = PORTNUM;

  private Properties props = null;

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private String url = null;

  private URL wsdlurl = null;

  public void cleanup() throws Exception {
    TestUtil.logMsg("cleanup ok");
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (DefaultWebService) service.getPort(DefaultWebService.class);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
  }

  private void getPortStandalone() throws Exception {
    port = (DefaultWebService) JWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        DefaultWebServiceService.class, PORT_QNAME, DefaultWebService.class);
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

  public void setup(String[] args, Properties p) throws Exception {
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
        service = (DefaultWebServiceService) getSharedObject();
        getPortJavaEE();
      }

      doc = WsdlUtils.getDocument(wsdlurl.toString());
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("setup failed:", e);
    }

    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Exception("setup failed:");
    }
  }

  /*
   * @testName: testPing
   * 
   * @assertion_ids: JWS:SPEC:3001; JWS:SPEC:3002; JWS:SPEC:3004
   * 
   * @test_Strategy:
   */
  public void testPing() throws Exception {
    TestUtil.logMsg("testPingRPC");
    boolean pass = true;
    try {
      TestUtil.logMsg("Invoke the ping() method of DefaultWebService");
      port.ping("Ping");
      TestUtil.logMsg("Invocation of ping() passed");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Exception("testPing failed", e);
    }

    if (!pass)
      throw new Exception("testPing failed");
  }

  /*
   * @testName: testPingWSDL
   * 
   * @assertion_ids: JWS:SPEC:3004
   * 
   * @test_Strategy: Ping WSDL
   * 
   */
  public void testPingWSDL() throws Exception {
    TestUtil.logMsg("testPingWSDL");
    boolean pass = true;
    try {
      TestUtil.logMsg("Get the WSDL of defaultWebService");
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
      throw new Exception("testPingWSDL failed", e);
    }

    if (!pass)
      throw new Exception("testPingWSDL failed");
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
  public void testWSDL1() throws Exception {
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

      Element elm = WsdlUtils.findServiceElement(doc, SERVICE_QNAME);

      if (elm == null) {
        throw new Exception(
            "testWSDL1 failed : could not find service element with name "
                + SERVICE_NAME + " in the WSDL");
      }
      Element portElement = WsdlUtils.findPortElement(doc, elm, PORT_QNAME);

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
      throw new Exception("testWSDL1 failed", ex);
    }

    if (!pass)
      throw new Exception(
          "testWSDL1 failed : style is not set to document in the WSDL "
              + attValue + "  " + att);
    if (pass)
      TestUtil
          .logMsg("testWSDL1 passed : style is set to document in the WSDL ");

  }

}
