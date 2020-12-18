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

package com.sun.ts.tests.jws.webmethod.webmethod1.client;

import java.net.URL;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
 * @executeClass com.sun.ts.tests.jws.webmethod.webmethod1.client.Client
 * @sources Client.java
 * @keywords webservice
 */

public class Client extends ServiceEETest {

  static DefaultWebMethodWebServiceService service = null;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "jwstest.defaultwebmethodwebservice.endpoint.1";

  private static final String EXPECTED_OPERATION_NAME = "hello";

  private static final String EXPECTED_RESULT_1 = "Hello jsr181 to Web Service";

  private static final String EXPECTED_RESULT_2 = "Hello jsr181, jaxws to Web Service";

  private static final String EXPECTED_RESULT_3 = "Hello jsr181, jaxws, jsr109 to Web Service";

  private static final String EXPECTED_SOAP_ACTION = "";

  private static final String HOSTNAME = "localhost";

  private static final String MODEPROP = "platform.mode";

  // service and port information
  private static final String NAMESPACEURI = "http://server.webmethod1.webmethod.jws.tests.ts.sun.com/";

  private static final String PKG_NAME = "com.sun.ts.tests.jws.webmethod.webmethod1.client.";

  private static final String PORT_NAME = "defaultWebMethodWebServicePort";

  private static final int PORTNUM = 8000;

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String SERVICE_NAME = "defaultWebMethodWebServiceService";

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String WSDLLOC_URL = "jwstest.defaultwebmethodwebservice.wsdlloc.1";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  private TSURL ctsurl = new TSURL();

  // Document used to check the WSDL
  private Document doc;

  private QName EXPECTED_OPERATION_QNAME = new QName(NAMESPACEURI,
      EXPECTED_OPERATION_NAME);

  private String hostname = HOSTNAME;

  String modeProperty = null; // platform.mode -> (standalone|j2ee)

  private DefaultWebMethodWebService port;

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private int portnum = PORTNUM;

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
    port = (DefaultWebMethodWebService) service
        .getPort(DefaultWebMethodWebService.class);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
  }

  private void getPortStandalone() throws Exception {
    port = (DefaultWebMethodWebService) JWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        DefaultWebMethodWebServiceService.class, PORT_QNAME,
        DefaultWebMethodWebService.class);
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
        service = (DefaultWebMethodWebServiceService) getSharedObject();
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
   * @assertion_ids: JWS:SPEC:3013
   * 
   * @test_Strategy:
   */
  public void testHello() throws Fault {
    TestUtil.logMsg("testHello");
    boolean pass = true;
    String result = null;

    try {
      TestUtil
          .logMsg("Invoke the hello() method of DefaultWebMethodWebService");
      result = port.hello("jsr181");

      if (!result.equals(EXPECTED_RESULT_1))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHello failed", e);
    }

    if (!pass)
      throw new Fault("testHello failed invoking hello returned " + result);
    TestUtil.logMsg("Invocation of hello(String) passed");
  }

  /*
   * @testName: testHello2
   * 
   * @assertion_ids: JWS:SPEC:3013
   * 
   * @test_Strategy:
   */
  public void testHello2() throws Fault {
    TestUtil.logMsg("testHello2");
    boolean pass = true;
    String result = null;

    try {
      TestUtil
          .logMsg("Invoke the hello2() method of DefaultWebMethodWebService");
      result = port.hello2("jsr181", "jaxws");

      if (!result.equals(EXPECTED_RESULT_2))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHello2 failed", e);
    }

    if (!pass)
      throw new Fault("testHello2 failed invoking hello2 returned " + result);
    TestUtil.logMsg("Invocation of hello2(String, String) passed");
  }

  /*
   * @testName: testHello3
   * 
   * @assertion_ids: JWS:SPEC:3013
   * 
   * @test_Strategy:
   */
  public void testHello3() throws Fault {
    TestUtil.logMsg("testHello3");
    boolean pass = true;
    String result = null;

    try {
      TestUtil
          .logMsg("Invoke the hello3() method of DefaultWebMethodWebService");
      result = port.hello3("jsr181", "jaxws", "jsr109");

      if (!result.equals(EXPECTED_RESULT_3))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHello3 failed", e);
    }

    if (!pass)
      throw new Fault("testHello3 failed invoking hello2 returned " + result);
    TestUtil.logMsg("Invocation of hello3(String, String, String) passed");
  }

  /*
   * @testName: testPingWSDL
   * 
   * @assertion_ids: JWS:SPEC:3004
   * 
   * @test_Strategy: Ping WSDL
   * 
   */
  public void testPingWSDL() throws Fault {
    TestUtil.logMsg("testPingWSDL");
    boolean pass = true;
    try {
      TestUtil.logMsg("Get the WSDL of defaultWebMethodWebService");
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
      // TestUtil.logMsg("Parse WSDL from location " +
      // wsdlurl.toString());
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
      throw new Fault("testPingWSDL failed");
  }

  // Method to check the operation Name and the action value in the WSDL

  /*
   * @testName: testWSDL1
   * 
   * @assertion_ids: JWS:SPEC:5001; JWS:SPEC:5008
   * 
   * @test_Strategy: Check for the operation Name and Action URI
   * 
   */

  public void testWSDL1() throws Fault {
    boolean pass = false;
    Element operationElement = null;
    Element soapOperationElement = null;

    try {

      operationElement = com.sun.ts.tests.jws.common.WsdlUtils
          .findOperationElement(doc, EXPECTED_OPERATION_QNAME, SERVICE_QNAME,
              PORT_QNAME);

      if (operationElement != null) {

        soapOperationElement = WsdlUtils.findSoapOperationElement(doc,
            EXPECTED_SOAP_ACTION, operationElement);

        if (soapOperationElement != null)
          pass = true;

      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL1 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(" testWSDL1() passed :  Found the operation element "
          + EXPECTED_OPERATION_NAME + " and the soap action uri "
          + EXPECTED_SOAP_ACTION + " for service : " + SERVICE_QNAME
          + " in the WSDL ");
    } else {
      throw new Fault("testWSDL1 failed : could not find operation element "
          + EXPECTED_OPERATION_NAME + " and the soap action uri "
          + EXPECTED_SOAP_ACTION + " for service : " + SERVICE_QNAME
          + " in the WSDL ");
    }

  }
}
