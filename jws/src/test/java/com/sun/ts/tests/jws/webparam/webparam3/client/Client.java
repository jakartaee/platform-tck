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

package com.sun.ts.tests.jws.webparam.webparam3.client;

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
import com.sun.ts.tests.jws.common.DescriptionConstants;
import com.sun.ts.tests.jws.common.DescriptionUtils;
import com.sun.ts.tests.jws.common.JWS_Util;
import com.sun.ts.tests.jws.common.SchemaConstants;
import com.sun.ts.tests.jws.common.WsdlUtils;
import com.sun.ts.tests.jws.common.XMLUtils;

import jakarta.xml.ws.Holder;

/**
 * @test
 * @executeClass com.sun.ts.tests.jws.webparam.webparam3.client.Client
 * @sources Client.java
 * @keywords webservice
 */

public class Client extends ServiceEETest
    implements SchemaConstants, DescriptionConstants {

  static WebParam3WebServiceService service = null;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "jwstest.webparam3webservice.endpoint.1";

  private static final String EXPECTED_DEFAULT_PART_NAME_1 = "arg1";

  private static final String EXPECTED_ELEMENT_TARGETNAMESPACE_1 = "hello3/Name";

  private static final String EXPECTED_OPERATION_NAME_1 = "hello3";

  private static final String EXPECTED_OPERATION_NAME_2 = "helloString2";

  private static final String EXPECTED_PART_ELEMENT_NAME_1 = "id";

  private static final String EXPECTED_PART_NAME_1 = "string1";

  private static final String EXPECTED_PART_NAME_2 = "string2";

  private static final String EXPECTED_PART_NAME_3 = "Employee";

  private static final String EXPECTED_PART_NAME_4 = "Name";

  private static final String EXPECTED_RESULT_1 = "Hello jsr181 to Web Service";

  private static final String EXPECTED_RESULT_2 = "Hello jsr181, jsr109 to Web Service";

  private static final String HOSTNAME = "localhost";

  private static final String MODEPROP = "platform.mode";

  // service and port information
  private static final String NAMESPACEURI = "http://server.webparam3.webparam.jws.tests.ts.sun.com/";

  private static final String PKG_NAME = "com.sun.ts.tests.jws.webparam.webparam3.client.";

  private static final String PORT_NAME = "webParam3WebServicePort";

  private static final int PORTNUM = 8000;

  private static final String PORTTYPE_NAME = "webParam3WebService";

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String SERVICE_NAME = "webParam3WebServiceService";

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String WSDLLOC_URL = "jwstest.webparam3webservice.wsdlloc.1";

  private static final String EXPECTED_INPUT_PARAMETER_ORDER = "string2 arg1 address";

  private static final String EXPECTED_OUTPUT_PARAMETER_ORDER = "return Name Employee";

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  private TSURL ctsurl = new TSURL();

  // Document used to check the WSDL
  private Document doc;

  private QName EXPECTED_DEFAULT_PART_QNAME_1 = new QName(NAMESPACEURI,
      EXPECTED_DEFAULT_PART_NAME_1);

  private QName EXPECTED_OPERATION_QNAME_1 = new QName(NAMESPACEURI,
      EXPECTED_OPERATION_NAME_1);

  private QName EXPECTED_OPERATION_QNAME_2 = new QName(NAMESPACEURI,
      EXPECTED_OPERATION_NAME_2);

  private QName EXPECTED_PART_ELEMENT_QNAME_1 = new QName(
      EXPECTED_ELEMENT_TARGETNAMESPACE_1, EXPECTED_PART_ELEMENT_NAME_1);

  private QName EXPECTED_PART_QNAME_1 = new QName(NAMESPACEURI,
      EXPECTED_PART_NAME_1);

  ;

  private QName EXPECTED_PART_QNAME_2 = new QName(NAMESPACEURI,
      EXPECTED_PART_NAME_2);

  private QName EXPECTED_PART_QNAME_3 = new QName(NAMESPACEURI,
      EXPECTED_PART_NAME_3);

  private QName EXPECTED_PART_QNAME_4 = new QName(NAMESPACEURI,
      EXPECTED_PART_NAME_4);

  private String hostname = HOSTNAME;

  String modeProperty = null; // platform.mode -> (standalone|j2ee)

  private WebParam3WebService port;

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
    port = (WebParam3WebService) service.getPort(WebParam3WebService.class);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
  }

  private void getPortStandalone() throws Exception {
    port = (WebParam3WebService) JWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        WebParam3WebServiceService.class, PORT_QNAME,
        WebParam3WebService.class);
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
        service = (WebParam3WebServiceService) getSharedObject();
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
   * @testName: testHello3
   *
   * @assertion_ids: JWS:SPEC:3013
   *
   * @test_Strategy:
   */
  public void testHello3() throws Fault {
    TestUtil.logMsg("testHello3");
    boolean pass = true;
    Name name = new Name();
    Employee employee = new Employee();
    Name output = null;

    try {
      name.setFirstName("k");
      name.setLastName("l");
      employee.setName(name);

      Holder<Name> nameHolder = new Holder<Name>();

      Holder<Employee> employeeHolder = new Holder<Employee>();

      employeeHolder.value = employee;
      TestUtil.logMsg("Invoke the hello3() method of WebParam3WebService");

      String result = port.hello3("jsr181", nameHolder, employeeHolder);

      if (result.equals(EXPECTED_RESULT_1)) {

        output = (Name) nameHolder.value;
        if ((!output.getFirstName().equals("jsr181"))
            && (!output.getLastName().equals("jsr109")))
          pass = false;
        TestUtil.logMsg(" First Name : " + output.getFirstName()
            + "  Last Name : " + output.getLastName());
      }

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHello3 failed", e);
    }

    if (!pass)
      throw new Fault("testHello3 failed, returned value : " + output);
    TestUtil.logMsg("Invocation of hello3() passed");
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
      TestUtil.logMsg("Invoke the helloString() method of WebParam3WebService");
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
   * @assertion_ids: JWS:SPEC:3013
   *
   * @test_Strategy:
   */
  public void testHelloString2() throws Fault {
    TestUtil.logMsg("testHelloString");
    boolean pass = true;
    Address address = new Address();
    String result = "";
    try {
      TestUtil
          .logMsg("Invoke the helloString2() method of WebParam3WebService");

      result = port.helloString2("jsr181", "jsr109", address);
      if (!result.equals(EXPECTED_RESULT_2))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHelloString2 failed", e);
    }

    if (!pass)
      throw new Fault("testHelloString2 failed, returned value : " + result);
    TestUtil.logMsg("Invocation of helloString2() passed");
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
      TestUtil.logMsg("Get the WSDL of webParam3WebService");
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
   * @assertion_ids: JWS:SPEC:6001; JWS:SPEC:3014; JWS:SPEC:5010
   *
   * @test_Strategy: Check for the name of the part set using name
   *
   */

  public void testWSDL1() throws Fault {
    boolean pass = false;
    String attValue = null;
    Node node = null;
    Attr att = null;
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
              + EXPECTED_PART_QNAME_1 + " in the WSDL ");
    } else {
      throw new Fault(
          "testWSDL1 failed : could not find part element with name : "
              + EXPECTED_PART_QNAME_1 + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL2
   *
   * @assertion_ids: JWS:SPEC:6001; JWS:SPEC:3014; JWS:SPEC:5010
   *
   * @test_Strategy: Check for the name of the part set using partName
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
        pass = true;

      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL1 failed", ex);
    }

    if (pass) {
      TestUtil
          .logMsg(" testWSDL2() passed :  Found the part element with name : "
              + EXPECTED_PART_QNAME_2 + " in the WSDL ");
    } else {
      throw new Fault(
          "testWSDL2 failed : could not find part element with name : "
              + EXPECTED_PART_QNAME_2 + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL3
   *
   * @assertion_ids: JWS:SPEC:6001; JWS:SPEC:3014; JWS:SPEC:5010
   *
   * @test_Strategy: Check for the default name of the part
   *
   */

  public void testWSDL3() throws Fault {
    boolean pass = false;
    String elementValue = null;
    Element messageElement = null;
    Element partElement = null;
    Attr ele = null;
    QName elementQName = null;
    QName operationQName = new QName(NAMESPACEURI, "helloString2");

    try {

      messageElement = com.sun.ts.tests.jws.common.WsdlUtils
          .findMessageElement(doc, PORTTYPE_QNAME, operationQName, "input");
      if (messageElement != null) {
        partElement = com.sun.ts.tests.jws.common.WsdlUtils.findPartElement(doc,
            messageElement, EXPECTED_DEFAULT_PART_QNAME_1);
      }

      if (partElement != null) {
        pass = true;

      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL3 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(
          " testWSDL3() passed :  Found the part element with default name : "
              + EXPECTED_DEFAULT_PART_QNAME_1 + " in the WSDL ");
    } else {
      throw new Fault(
          "testWSDL3 failed : could not find part element with default name : "
              + EXPECTED_DEFAULT_PART_QNAME_1 + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL4
   *
   * @assertion_ids: JWS:SPEC:6012
   *
   * @test_Strategy: Check for the header
   *
   */

  public void testWSDL4() throws Fault {
    boolean pass = false;
    String attValue = null;
    Node node = null;
    Attr att = null;
    Element operationElement = null;
    Element soapHeaderElement = null;
    Element messageElement = null;
    Element partElement = null;
    QName operationQName = new QName(NAMESPACEURI, "hello3");

    try {

      operationElement = com.sun.ts.tests.jws.common.WsdlUtils
          .findOperationElement(doc, EXPECTED_OPERATION_QNAME_1, SERVICE_QNAME,
              PORT_QNAME);

      if (operationElement != null) {

        soapHeaderElement = com.sun.ts.tests.jws.common.WsdlUtils
            .findSoapHeaderElement(doc, operationElement, "input", "id",
                PORTTYPE_QNAME, EXPECTED_OPERATION_QNAME_1);

      }

      messageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          operationQName, "input");

      if (messageElement != null) {

        partElement = WsdlUtils.findPartElement(doc, messageElement,
            EXPECTED_PART_ELEMENT_QNAME_1, true);

      }

      if (soapHeaderElement != null && partElement != null)
        pass = true;

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL4 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(" testWSDL4() passed :  Found the soap header element : "
          + soapHeaderElement + " : " + EXPECTED_PART_ELEMENT_QNAME_1
          + " in the WSDL");
    } else {
      throw new Fault("testWSDL4 failed : could not find soap header element : "
          + EXPECTED_PART_ELEMENT_QNAME_1 + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL5
   *
   * @assertion_ids: JWS:SPEC:5009; JWS:SPEC:6001
   *
   * @test_Strategy: Check for the part name in both input and output messages
   *
   */

  public void testWSDL5() throws Fault {
    boolean pass = false;
    String attValue = null;
    Node node = null;
    Attr att = null;
    Element inputMessageElement = null;
    Element inputPartElement = null;
    Element outputMessageElement = null;
    Element outputPartElement = null;
    QName operationQName = new QName(NAMESPACEURI, "hello3");

    try {

      inputMessageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          operationQName, "input");
      if (inputMessageElement != null) {
        inputPartElement = WsdlUtils.findPartElement(doc, inputMessageElement,
            EXPECTED_PART_QNAME_3);
      }

      outputMessageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          operationQName, "output");
      if (outputMessageElement != null) {
        outputPartElement = com.sun.ts.tests.jws.common.WsdlUtils
            .findPartElement(doc, outputMessageElement, EXPECTED_PART_QNAME_3);
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
              + EXPECTED_PART_QNAME_3
              + " in the both input and output message elements in the WSDL ");
    } else {
      throw new Fault(
          "testWSDL5 failed : could not find part element with name : "
              + EXPECTED_PART_QNAME_3
              + " in the both input and output message elements in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL6
   *
   * @assertion_ids:
   *
   * @test_Strategy: Check for the name of the part set using name in the output
   * message
   *
   */

  public void testWSDL6() throws Fault {
    boolean pass = false;
    String attValue = null;
    Node node = null;
    Attr att = null;
    Element messageElement = null;
    Element partElement = null;
    QName operationQName = new QName(NAMESPACEURI, "hello3");

    try {

      messageElement = com.sun.ts.tests.jws.common.WsdlUtils
          .findMessageElement(doc, PORTTYPE_QNAME, operationQName, "output");
      if (messageElement != null) {
        partElement = com.sun.ts.tests.jws.common.WsdlUtils.findPartElement(doc,
            messageElement, EXPECTED_PART_QNAME_4);
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
              + EXPECTED_PART_QNAME_4 + " in the output message in the WSDL ");
    } else {
      throw new Fault(
          "testWSDL6 failed : could not find part element with name : "
              + EXPECTED_PART_QNAME_4 + "in the output message in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL7
   *
   * @assertion_ids: JWS:SPEC:6002
   *
   * @test_Strategy: Check for the order of parts in input message
   *
   */

  public void testWSDL7() throws Fault {
    boolean pass = false;
    Element operationElement = null;
    Element portTypeElement = null;
    Element messageElement = null;
    String inputParameterOrder = "";
    StringBuffer outputParameterOrder = new StringBuffer(" ");

    try {

      portTypeElement = WsdlUtils.findPortTypeElement(doc, PORTTYPE_QNAME);
      operationElement = WsdlUtils.findPortTypeOperationElement(portTypeElement,
          EXPECTED_OPERATION_QNAME_2);
      if (operationElement != null) {
        inputParameterOrder = operationElement
            .getAttribute(WSDL_PARAMETERORDER_ATTR);
        TestUtil.logTrace("inputParameterOrder=" + inputParameterOrder);
        if (!inputParameterOrder.equals("")) {
          TestUtil.logMsg("input parameterOrder checking will be done.");
          if (!EXPECTED_INPUT_PARAMETER_ORDER
              .equals(inputParameterOrder.trim())) {
            throw new Fault(
                "Input Parameter Order is set to " + inputParameterOrder
                    + " instead of " + EXPECTED_INPUT_PARAMETER_ORDER);
          }
        } else
          TestUtil.logMsg("No input parameterOrder checking will be done.");
      }
      messageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          EXPECTED_OPERATION_QNAME_1, "output");
      if (messageElement != null) {
        Element[] elements = DescriptionUtils.getPartElements(doc,
            messageElement.getAttribute(WSDL_NAME_ATTR));
        for (int i = 0; i < elements.length; i++) {
          outputParameterOrder
              .append(elements[i].getAttribute(WSDL_NAME_ATTR) + " ");
        }
      }
      TestUtil
          .logTrace("outputParameterOrder=" + outputParameterOrder.toString());
      String resultParameterOrder = outputParameterOrder.toString().trim();
      if (EXPECTED_OUTPUT_PARAMETER_ORDER.equals(resultParameterOrder))
        pass = true;

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL7 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(
          " testWSDL7() passed : parameter order is set correctly in the WSDL ");
    } else {
      throw new Fault("Output Parameter Order is set to " + outputParameterOrder
          + " instead of " + EXPECTED_OUTPUT_PARAMETER_ORDER);
    }

  }

}
