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

package com.sun.ts.tests.jws.webparam.webparam1.client;

import java.net.URL;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
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

import jakarta.xml.ws.Holder;

/**
 * @test
 * @executeClass com.sun.ts.tests.jws.webmethod.webmethod1.client.Client
 * @sources Client.java
 * @keywords webservice
 */

public class Client extends ServiceEETest {

  static WebParamWebServiceService service = null;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "jwstest.webparamwebservice.endpoint.1";

  private static final String EXPECTED_DEFAULT_PART_NAME = "helloString5";

  private static final String EXPECTED_ELEMENT_TARGETNAMESPACE_2 = "helloString2/name";

  private static final String EXPECTED_ELEMENT_TARGETNAMESPACE_3 = "helloString3/name";

  private static final String EXPECTED_ELEMENT_TARGETNAMESPACE_4 = "helloString3/Name";

  private static final String EXPECTED_INOUT_PART_NAME = "Name";

  private static final String EXPECTED_OPERATION_NAME_3 = "helloString3";

  private static final String EXPECTED_OPERATION_NAME_4 = "helloString4";

  private static final String EXPECTED_OUT_PART_NAME = "Employee";

  private static final String EXPECTED_PART_ELEMENT_NAME_1 = "name";

  private static final String EXPECTED_PART_ELEMENT_NAME_2 = "name2";

  private static final String EXPECTED_PART_ELEMENT_NAME_3 = "name3";

  private static final String EXPECTED_PART_ELEMENT_NAME_4 = "name4";

  private static final String EXPECTED_PART_NAME_1 = "string1";

  private static final String EXPECTED_PART_NAME_2 = "string2";

  private static final String EXPECTED_PART_NAME_3 = "string3";

  private static final String EXPECTED_RESULT_1 = "hello : Hello jsr181 to Web Service";

  private static final String EXPECTED_RESULT_2 = "hello2 : Hello jsr181 to Web Service";

  private static final String EXPECTED_RESULT_5 = "hello5 : Hello jsr181 to Web Service";

  private static final String EXPECTED_RESULT_6 = "hello6 : Hello jsr181 30 to Web Service";

  private static final String EXPECTED_RESULT_8 = "hello8 : San Francisco to Web Service";

  private static final String HOSTNAME = "localhost";

  private static final String MODEPROP = "platform.mode";

  // service and port information
  private static final String NAMESPACEURI = "http://server.webparam1.webparam.jws.tests.ts.sun.com/";

  private static final String PKG_NAME = "com.sun.ts.tests.jws.webparam.webparam1.client.";

  private static final String PORT_NAME = "webParamWebServicePort";

  private static final int PORTNUM = 8000;

  private static final String PORTTYPE_NAME = "webParamWebService";

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String SERVICE_NAME = "webParamWebServiceService";

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String WSDLLOC_URL = "jwstest.webparamwebservice.wsdlloc.1";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  private TSURL ctsurl = new TSURL();

  // Document used to check the WSDL
  private Document doc;

  private QName EXPECTED_DEFAULT_PART_QNAME = new QName(NAMESPACEURI,
      EXPECTED_DEFAULT_PART_NAME);

  private QName EXPECTED_INOUT_PART_QNAME = new QName(
      EXPECTED_ELEMENT_TARGETNAMESPACE_4, EXPECTED_INOUT_PART_NAME);

  private QName EXPECTED_OPERATION_QNAME_3 = new QName(NAMESPACEURI,
      EXPECTED_OPERATION_NAME_3);

  private QName EXPECTED_OPERATION_QNAME_4 = new QName(NAMESPACEURI,
      EXPECTED_OPERATION_NAME_4);

  private QName EXPECTED_OUT_PART_QNAME = new QName(NAMESPACEURI,
      EXPECTED_OUT_PART_NAME);

  private QName EXPECTED_PART_ELEMENT_QNAME_2 = new QName(
      EXPECTED_ELEMENT_TARGETNAMESPACE_2, EXPECTED_PART_ELEMENT_NAME_2);

  private QName EXPECTED_PART_ELEMENT_QNAME_3 = new QName(
      EXPECTED_ELEMENT_TARGETNAMESPACE_3, EXPECTED_PART_ELEMENT_NAME_3);

  private QName EXPECTED_PART_ELEMENT_QNAME_4 = new QName(NAMESPACEURI,
      EXPECTED_PART_ELEMENT_NAME_3);

  private QName EXPECTED_PART_QNAME_1 = new QName(NAMESPACEURI,
      EXPECTED_PART_NAME_1);

  private QName EXPECTED_PART_QNAME_2 = new QName(NAMESPACEURI,
      EXPECTED_PART_NAME_2);

  private QName EXPECTED_PART_QNAME_3 = new QName(NAMESPACEURI,
      EXPECTED_PART_NAME_3);

  private String hostname = HOSTNAME;

  String modeProperty = null; // platform.mode -> (standalone|j2ee)

  private WebParamWebService port;

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private int portnum = PORTNUM;

  private QName PORTTYPE_QNAME = new QName(NAMESPACEURI, PORTTYPE_NAME);

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
    port = (WebParamWebService) service.getPort(WebParamWebService.class);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
  }

  private void getPortStandalone() throws Exception {
    port = (WebParamWebService) JWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        WebParamWebServiceService.class, PORT_QNAME, WebParamWebService.class);
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
        service = (WebParamWebServiceService) getSharedObject();
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
   * @testName: testHelloString
   * 
   * @assertion_ids: JWS:SPEC:3013
   * 
   * @test_Strategy:
   */
  public void testHelloString() throws Fault {
    TestUtil.logMsg("testHelloString");
    boolean pass = true;
    String result = "";

    try {
      TestUtil.logMsg("Invoke the helloString() method of WebParamWebService");
      result = port.helloString("jsr181");
      if (!result.equals(EXPECTED_RESULT_1))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHelloString failed", e);
    }

    if (!pass)
      throw new Fault("testHelloString failed, returned value : " + result);
    TestUtil.logMsg("Invocation of helloString() passed");
  }

  /*
   * @testName: testHelloString2
   * 
   * @assertion_ids: JWS:SPEC:6011
   * 
   * @test_Strategy:
   */
  public void testHelloString2() throws Fault {
    TestUtil.logMsg("testHelloString2");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Invoke the helloString2() method of WebMethodWebService");
      String result = port.helloString2("jsr181");
      if (!result.equals(EXPECTED_RESULT_2))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHelloString2 failed", e);
    }

    if (!pass)
      throw new Fault("testHelloString2 failed");
    TestUtil.logMsg("Invocation of helloString2() passed");
  }

  /*
   * @testName: testHelloString3
   * 
   * @assertion_ids: JWS:SPEC:6010
   * 
   * @test_Strategy:
   */
  public void testHelloString3() throws Fault {
    TestUtil.logMsg("testHelloString3");
    boolean pass = true;
    Name name = new Name();

    try {
      TestUtil
          .logMsg("Invoke the helloString3() method of WebMethodWebService");
      name.setFirstName("k");
      name.setLastName("l");

      Holder<Name> nameHolder = new Holder<Name>();

      nameHolder.value = name;

      port.helloString3("jsr181", nameHolder);

      Name output = (Name) nameHolder.value;

      if ((!output.getFirstName().equals("jsr181"))
          && (!output.getLastName().equals("jsr109")))
        pass = false;

      TestUtil.logMsg(" First Name : " + output.getFirstName()
          + "  Last Name : " + output.getLastName());

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHelloString3 failed", e);
    }

    if (!pass)
      throw new Fault("testHelloString3 failed");
    TestUtil.logMsg("Invocation of helloString3() passed");
  }

  /*
   * @testName: testHelloString4
   * 
   * @assertion_ids: JWS:SPEC:6009
   * 
   * @test_Strategy:
   */
  public void testHelloString4() throws Fault {
    TestUtil.logMsg("testHelloString3");
    boolean pass = true;

    try {
      TestUtil
          .logMsg("Invoke the helloString4() method of WebMethodWebService");

      Holder<Employee> employeeHolder = new Holder<Employee>();

      port.helloString4("jsr181", employeeHolder);

      Employee employee = (Employee) employeeHolder.value;

      Name output = employee.getName();

      if (output != null) {
        if (!output.getFirstName().equals("jsr181")
            && !output.getLastName().equals("jaxws"))
          pass = false;

        TestUtil.logMsg(" First Name : " + output.getFirstName()
            + "  Last Name : " + output.getLastName());
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHelloString4 failed", e);
    }

    if (!pass)
      throw new Fault("testHelloString4 failed");
    TestUtil.logMsg("Invocation of helloString4() passed");
  }

  /*
   * @testName: testHelloString5
   *
   * @assertion_ids: JWS:SPEC:3013
   *
   * @test_Strategy:
   */
  public void testHelloString5() throws Fault {
    TestUtil.logMsg("testHelloString5");
    boolean pass = true;
    String result = "";

    try {
      TestUtil.logMsg("Invoke the helloString5() method of WebParamWebService");
      result = port.helloString5("jsr181");
      if (!result.equals(EXPECTED_RESULT_5))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHelloString5 failed", e);
    }

    if (!pass)
      throw new Fault("testHelloString5 failed, returned value : " + result);
    TestUtil.logMsg("Invocation of helloString5() passed");
  }

  /*
   * @testName: testHelloString6
   *
   * @assertion_ids: JWS:SPEC:3013
   *
   * @test_Strategy:
   */
  public void testHelloString6() throws Fault {
    TestUtil.logMsg("testHelloString6");
    boolean pass = true;
    String result = "";

    try {
      TestUtil.logMsg("Invoke the helloString6() method of WebParamWebService");
      result = port.helloString6(30, "jsr181");
      if (!result.equals(EXPECTED_RESULT_6))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHelloString6 failed", e);
    }

    if (!pass)
      throw new Fault("testHelloString6 failed, returned value : " + result);
    TestUtil.logMsg("Invocation of helloString6() passed");
  }

  /*
   * @testName: testHelloString7
   *
   * @assertion_ids: JWS:SPEC:3013
   *
   * @test_Strategy:
   */
  public void testHelloString7() throws Fault {
    TestUtil.logMsg("testHelloString7");
    boolean pass = true;

    try {
      TestUtil
          .logMsg("Invoke the helloString7() method of WebMethodWebService");

      Holder<Employee> employeeHolder = new Holder<Employee>();

      Name name = new Name();

      name.setFirstName("jsr181");
      name.setLastName("jsr109");

      port.helloString7("jsr181", name, employeeHolder);

      Employee employee = (Employee) employeeHolder.value;

      Name output = employee.getName();

      if (output != null) {
        if (!output.getFirstName().equals("jsr181")
            && !output.getLastName().equals("jsr109"))
          pass = false;

        TestUtil.logMsg(" First Name : " + output.getFirstName()
            + "  Last Name : " + output.getLastName());
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHelloString4 failed", e);
    }

    if (!pass)
      throw new Fault("testHelloString4 failed");
    TestUtil.logMsg("Invocation of helloString4() passed");
  }

  /*
   * @testName: testHelloString8
   *
   * @assertion_ids: JWS:SPEC:4007
   *
   * @test_Strategy:
   */
  public void testHelloString8() throws Fault {
    TestUtil.logMsg("testHelloString8");
    boolean pass = true;
    String result = "";
    Address address = new Address();

    try {
      TestUtil.logMsg("Invoke the helloString8() method of WebParamWebService");
      result = port.helloString8("jsr181", address);
      if (!result.equals(EXPECTED_RESULT_8))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHelloString8 failed", e);
    }

    if (!pass)
      throw new Fault("testHelloString8 failed, returned value : " + result);
    TestUtil.logMsg("Invocation of helloString8() passed");
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
      TestUtil.logMsg("Get the WSDL of webParamWebService");
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
      throw new Fault("testPingWSDL failed");
  }

  /*
   * @testName: testWSDL1
   * 
   * @assertion_ids: JWS:SPEC:4002; JWS:SPEC:3014; JWS:SPEC:5010
   * 
   * @test_Strategy: Check for the partName
   * 
   */

  public void testWSDL1() throws Fault {
    boolean pass = false;
    Element messageElement = null;
    Element partElement = null;
    QName operationQName = new QName(NAMESPACEURI, "helloString");

    try {

      messageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          operationQName, "input");
      if (messageElement != null) {
        partElement = com.sun.ts.tests.jws.common.WsdlUtils.findPartElement(doc,
            messageElement, EXPECTED_PART_QNAME_1);
      }

      if (partElement != null)
        pass = true;

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL1 failed", ex);
    }

    if (pass) {
      TestUtil
          .logMsg(" testWSDL1() passed :  Found the part element with name : "
              + EXPECTED_PART_NAME_1 + " in the WSDL ");
    } else {
      throw new Fault(
          "testWSDL1 failed : could not find part element with name : "
              + EXPECTED_PART_NAME_1 + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL2
   * 
   * @assertion_ids: JWS:SPEC:5009; JWS:SPEC:6007; JWS:SPEC:6003; JWS:SPEC:3014;
   * JWS:SPEC:5010
   * 
   * @test_Strategy: Check for the element QName of the part
   * 
   */

  public void testWSDL2() throws Fault {
    boolean pass = false;
    String elementValue = null;
    Element messageElement = null;
    Element partElement = null;
    Attr ele = null;
    QName elementQName = null;
    QName operationQName = new QName(NAMESPACEURI, "helloString2");

    try {

      messageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          operationQName, "input");
      if (messageElement != null) {
        partElement = WsdlUtils.findPartElement(doc, messageElement,
            EXPECTED_PART_QNAME_2);
      }

      if (partElement != null) {
        // Check if we need to use the name space
        ele = partElement.getAttributeNode("element");
        if (ele != null) {

          elementValue = ele.getValue();
          elementQName = WsdlUtils.createQName(partElement, elementValue);
          if (elementQName.equals(EXPECTED_PART_ELEMENT_QNAME_2))
            pass = true;

        }
      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL1 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(
          " testWSDL2() passed :  Found the part element with element name : "
              + elementQName + " in the WSDL ");
    } else {
      throw new Fault(
          "testWSDL2 failed : could not find part element with element name : "
              + EXPECTED_PART_ELEMENT_QNAME_2 + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL3
   * 
   * @assertion_ids: JWS:SPEC:6007; JWS:SPEC:4002; JWS:SPEC:3014; JWS:SPEC:5010
   * 
   * @test_Strategy: Check for the default element QName of the part and the
   * default name of the part
   * 
   */

  public void testWSDL3() throws Fault {
    boolean pass = false;
    String elementValue = null;
    Element messageElement = null;
    Element partElement = null;
    Attr ele = null;
    QName elementQName = null;
    QName operationQName = new QName(NAMESPACEURI, "helloString5");

    try {

      messageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          operationQName, "input");
      if (messageElement != null) {
        partElement = com.sun.ts.tests.jws.common.WsdlUtils.findPartElement(doc,
            messageElement, EXPECTED_DEFAULT_PART_QNAME);
      }

      if (partElement != null) {
        // Check if we need to use the name space
        ele = partElement.getAttributeNode("element");
        if (ele != null) {

          elementValue = ele.getValue();
          elementQName = WsdlUtils.createQName(partElement, elementValue);
          if (elementQName.equals(EXPECTED_DEFAULT_PART_QNAME))
            pass = true;

        }
      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL1 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(
          " testWSDL2() passed :  Found the part element with element name : "
              + elementQName + " in the WSDL ");
    } else {
      throw new Fault(
          "testWSDL2 failed : could not find part element with element name : "
              + EXPECTED_PART_ELEMENT_QNAME_2 + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL4
   * 
   * @assertion_ids: JWS:SPEC:6012; JWS:SPEC:3014; JWS:SPEC:5010
   * 
   * @test_Strategy: Check for the header
   * 
   */

  public void testWSDL4() throws Fault {
    boolean pass = false;
    Element operationElement = null;
    Element soapHeaderElement = null;
    Element messageElement = null;
    Element partElement = null;

    try {

      operationElement = com.sun.ts.tests.jws.common.WsdlUtils
          .findOperationElement(doc, EXPECTED_OPERATION_QNAME_3, SERVICE_QNAME,
              PORT_QNAME);

      if (operationElement != null) {

        soapHeaderElement = com.sun.ts.tests.jws.common.WsdlUtils
            .findSoapHeaderElement(doc, operationElement, "input",
                EXPECTED_PART_NAME_3, PORTTYPE_QNAME,
                EXPECTED_OPERATION_QNAME_3);

      }

      messageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          EXPECTED_OPERATION_QNAME_3, "input");

      if (messageElement != null) {

        partElement = WsdlUtils.findPartElement(doc, messageElement,
            EXPECTED_PART_ELEMENT_QNAME_3, true);

      }

      if (soapHeaderElement != null && partElement != null)
        pass = true;

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL4 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(" testWSDL4() passed :  Found the soap header element : "
          + soapHeaderElement + " : " + EXPECTED_PART_ELEMENT_QNAME_3
          + " in the WSDL");
    } else {
      throw new Fault("testWSDL4 failed : could not find soap header element : "
          + EXPECTED_PART_ELEMENT_QNAME_3 + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL5
   * 
   * @assertion_ids: JWS:SPEC:6001; JWS:SPEC:4002; JWS:SPEC:3014; JWS:SPEC:5010
   * 
   * @test_Strategy: Check for the part name in both input and output messages
   * 
   */

  public void testWSDL5() throws Fault {
    boolean pass = false;
    Element inputMessageElement = null;
    Element inputPartElement = null;
    Element outputMessageElement = null;
    Element outputPartElement = null;

    try {

      inputMessageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          EXPECTED_OPERATION_QNAME_3, "input");
      if (inputMessageElement != null) {
        inputPartElement = WsdlUtils.findPartElement(doc, inputMessageElement,
            EXPECTED_INOUT_PART_QNAME, true);
      }

      outputMessageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          EXPECTED_OPERATION_QNAME_3, "output");
      if (outputMessageElement != null) {
        outputPartElement = WsdlUtils.findPartElement(doc, outputMessageElement,
            EXPECTED_INOUT_PART_QNAME, true);
      }

      if (inputPartElement != null && outputPartElement != null)
        pass = true;

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL5 failed", ex);
    }

    if (pass) {
      TestUtil
          .logMsg(" testWSDL5() passed :  Found the part element with name : "
              + EXPECTED_INOUT_PART_QNAME
              + " in the both input and output message elements in the WSDL ");
    } else {
      throw new Fault(
          "testWSDL5 failed : could not find part element with name : "
              + EXPECTED_INOUT_PART_QNAME
              + " in the both input and output message elements in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL6
   * 
   * @assertion_ids: JWS:SPEC:4002
   * 
   * @test_Strategy: Check for the name of the part set using name in the output
   * message
   * 
   */

  public void testWSDL6() throws Fault {
    boolean pass = false;
    Element messageElement = null;
    Element partElement = null;

    try {

      messageElement = com.sun.ts.tests.jws.common.WsdlUtils.findMessageElement(
          doc, PORTTYPE_QNAME, EXPECTED_OPERATION_QNAME_4, "output");
      if (messageElement != null) {
        partElement = WsdlUtils.findPartElement(doc, messageElement,
            EXPECTED_OUT_PART_QNAME, true);
      }

      if (partElement != null)
        pass = true;

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL6 failed", ex);
    }

    if (pass) {
      TestUtil
          .logMsg(" testWSDL6() passed :  Found the part element with name : "
              + EXPECTED_OUT_PART_QNAME
              + " in the output message in the WSDL ");
    } else {
      throw new Fault(
          "testWSDL6 failed : could not find part element with name : "
              + EXPECTED_OUT_PART_QNAME + "in the output message in the WSDL ");
    }

  }

}
