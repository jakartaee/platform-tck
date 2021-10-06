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

package com.sun.ts.tests.jws.webparam.webparam2.client;

import java.net.URL;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.javatest.Status;
import com.sun.ts.lib.harness.ServiceEETest;
import com.sun.ts.lib.porting.TSURL;
import com.sun.ts.lib.util.TestUtil;
//import com.sun.ts.tests.common.webservices.WSServiceEETest;
import com.sun.ts.tests.jws.common.DescriptionConstants;
import com.sun.ts.tests.jws.common.DescriptionUtils;
import com.sun.ts.tests.jws.common.JWS_Util;
import com.sun.ts.tests.jws.common.SchemaConstants;
import com.sun.ts.tests.jws.common.WsdlUtils;

/**
 * @test
 * @executeClass com.sun.ts.tests.jws.webparam.webparam2.client.Client
 * @sources Client.java
 * @keywords webservice
 */

public class Client extends ServiceEETest
    implements SchemaConstants, DescriptionConstants {

  static WebParam2WebServiceService service = null;

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|j2ee)

  private static final String PKG_NAME = "com.sun.ts.tests.jws.webparam.webparam2.client.";

  // service and port information
  private static final String NAMESPACEURI = "http://server.webparam2.webparam.jws.tests.ts.sun.com/";

  private static final String SERVICE_NAME = "webParam2WebServiceService";

  private static final String PORT_NAME = "webParam2WebServicePort";

  private static final String PORTTYPE_NAME = "webParam2WebService";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private QName PORTTYPE_QNAME = new QName(NAMESPACEURI, PORTTYPE_NAME);

  private static final String EXPECTED_PART_ELEMENT_NAME_1 = "string1";

  private static final String EXPECTED_PART_NAME_2 = "Address";

  private static final String EXPECTED_PART_ELEMENT_NAME_2 = "Address";

  private static final String EXPECTED_PART_ELEMENT_NAME_4 = "Name";

  private static final String EXPECTED_PART_ELEMENT_NAME_5 = "Employee";

  private static final String EXPECTED_DEFAULT_PART_ELEMENT_NAME_1 = "arg0";

  private static final String EXPECTED_DEFAULT_PART_ELEMENT_NAME_2 = "arg1";

  private static final String EXPECTED_DEFAULT_PART_ELEMENT_NAME_3 = "arg2";

  private static final String EXPECTED_HEADER_PART_ELEMENT_NAME_1 = "employee";

  private static final String EXPECTED_OPERATION_NAME_1 = "helloString";

  private static final String EXPECTED_OPERATION_NAME_2 = "helloString2";

  private static final String EXPECTED_OPERATION_NAME_3 = "hello3";

  private static final String EXPECTED_OPERATION_NAME_4 = "helloString4";

  private static final String EXPECTED_OPERATION_NAME_5 = "helloString5";

  private QName EXPECTED_PART_QNAME_2 = new QName(NAMESPACEURI,
      EXPECTED_PART_NAME_2);

  private QName EXPECTED_OPERATION_QNAME_1 = new QName(NAMESPACEURI,
      EXPECTED_OPERATION_NAME_1);

  private QName EXPECTED_OPERATION_QNAME_2 = new QName(NAMESPACEURI,
      EXPECTED_OPERATION_NAME_2);

  private QName EXPECTED_OPERATION_QNAME_3 = new QName(NAMESPACEURI,
      EXPECTED_OPERATION_NAME_3);

  private QName EXPECTED_OPERATION_QNAME_4 = new QName(NAMESPACEURI,
      EXPECTED_OPERATION_NAME_4);

  private QName EXPECTED_OPERATION_QNAME_5 = new QName(NAMESPACEURI,
      EXPECTED_OPERATION_NAME_5);

  private static final String EXPECTED_ELEMENT_TARGETNAMESPACE_2 = "helloString2/Address";

  private static final String EXPECTED_ELEMENT_TARGETNAMESPACE_4 = "helloString4/Name";

  private static final String EXPECTED_ELEMENT_TARGETNAMESPACE_5 = "helloString5/Employee";

  private QName EXPECTED_PART_ELEMENT_QNAME_1 = new QName(NAMESPACEURI,
      EXPECTED_PART_ELEMENT_NAME_1);

  private QName EXPECTED_PART_ELEMENT_QNAME_2 = new QName(
      EXPECTED_ELEMENT_TARGETNAMESPACE_2, EXPECTED_PART_ELEMENT_NAME_2);

  private QName EXPECTED_PART_ELEMENT_QNAME_4 = new QName(
      EXPECTED_ELEMENT_TARGETNAMESPACE_4, EXPECTED_PART_ELEMENT_NAME_4);

  private QName EXPECTED_PART_ELEMENT_QNAME_5 = new QName(NAMESPACEURI,
      EXPECTED_PART_ELEMENT_NAME_5);

  private QName EXPECTED_DEFAULT_PART_ELEMENT_QNAME_1 = new QName(NAMESPACEURI,
      EXPECTED_DEFAULT_PART_ELEMENT_NAME_1);

  private QName EXPECTED_DEFAULT_PART_ELEMENT_QNAME_2 = new QName(NAMESPACEURI,
      EXPECTED_DEFAULT_PART_ELEMENT_NAME_2);

  private QName EXPECTED_DEFAULT_PART_ELEMENT_QNAME_3 = new QName(NAMESPACEURI,
      EXPECTED_DEFAULT_PART_ELEMENT_NAME_3);

  private String[] DEFAULT_NAMES = { EXPECTED_DEFAULT_PART_ELEMENT_NAME_1,
      EXPECTED_DEFAULT_PART_ELEMENT_NAME_2,
      EXPECTED_DEFAULT_PART_ELEMENT_NAME_3 };

  private QName[] DEFAULT_QNAMES = { EXPECTED_DEFAULT_PART_ELEMENT_QNAME_1,
      EXPECTED_DEFAULT_PART_ELEMENT_QNAME_2,
      EXPECTED_DEFAULT_PART_ELEMENT_QNAME_3 };

  private QName EXPECTED_HEADER_PART_ELEMENT_QNAME_1 = new QName(
      EXPECTED_ELEMENT_TARGETNAMESPACE_5, EXPECTED_HEADER_PART_ELEMENT_NAME_1);

  private static final String EXPECTED_RESULT_1 = "Hello First Name:jsr181 Last Name:jsr109 to Web Service";

  private static final String EXPECTED_RESULT_2 = "Hello jsr181, jsr109 to Web Service";

  private static final String EXPECTED_RESULT_6 = "Hello First Name:jsr181 Last Name:jaxws to Web Service";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // Document used to check the WSDL
  private Document doc;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "jwstest.webparam2webservice.endpoint.1";

  private static final String WSDLLOC_URL = "jwstest.webparam2webservice.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  private WebParam2WebService port;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getPortStandalone() throws Exception {
    port = (WebParam2WebService) JWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        WebParam2WebServiceService.class, PORT_QNAME,
        WebParam2WebService.class);
    JWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (WebParam2WebService) service.getPort(WebParam2WebService.class);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
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
        /*
         * url = "file://C:/jsr181/test-wsdl/issue1.wsdl"; File myfile = new
         * File("C:/jsr181/test-wsdl/issue1.wsdl"); wsdlurl = myfile.toURL();
         */
        getTestURLs();
        getPortStandalone();
      } else {
        getTestURLs();
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (WebParam2WebServiceService) getSharedObject();
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

  private boolean findParameterElement(Document document, Element complexType,
      URL baseURL, QName qName, boolean checkList) throws Exception {

    String targetNameSpace = null;
    boolean pass = false;

    targetNameSpace = DescriptionUtils.getTargetNamespaceAttr(doc);
    TestUtil.logMsg("Got Complex Type Element : " + targetNameSpace + ":"
        + complexType.getAttribute(XSD_NAME_ATTR));
    NodeList list = complexType.getElementsByTagNameNS(XSD_NAMESPACE_URI,
        XSD_ELEMENT_LOCAL_NAME);
    Element parameterRefElement = WsdlUtils.getParameterRefElement(list, qName);
    if (parameterRefElement != null) {
      Element parameterElement = null;
      if (DescriptionUtils.isSchema(document)) {
        Node[] parameterNodes = WsdlUtils.getParameterElement(document, baseURL,
            qName);
        if (parameterNodes != null) {
          parameterElement = WsdlUtils.getElement(parameterNodes);
        }
      } else if (DescriptionUtils.isDescription(document)) {
        TestUtil.logMsg("Document is a WSDL");
        parameterElement = WsdlUtils.getSchemasElementName(document, qName,
            baseURL);
        if (parameterElement == null) {
          Document[] schemas = DescriptionUtils.getSchemaDocuments(document,
              baseURL);
          for (int i = 0; i < schemas.length; i++) {
            Node[] parameterNodes = WsdlUtils.getParameterElement(document,
                baseURL, qName);
            if (parameterNodes != null) {
              parameterElement = WsdlUtils.getElement(parameterNodes);
            }
          }
        }
      }
      if (parameterElement != null)
        pass = true;
    } else {
      if (checkList) {
        Element parameterElement = WsdlUtils.getParameterElement(doc, list,
            qName);
        if (parameterElement != null)
          pass = true;
      } else {
        throw new Fault(
            "Test failed : Cannot find a element with a reference to " + qName);
      }
    }
    return pass;
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
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
    Name name = new Name();
    name.setFirstName("jsr181");
    name.setLastName("jsr109");
    try {
      TestUtil.logMsg("Invoke the helloString() method of WebParamWebService");
      String result = port.helloString(name);
      if (!result.equals(EXPECTED_RESULT_1))
        pass = false;
      TestUtil.logMsg("Invocation of helloString() passed");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHelloString failed", e);
    }

    if (!pass)
      throw new Fault("testHelloString failed");
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
    String result;
    Address address = new Address();
    address.setCity("SF");
    try {
      TestUtil
          .logMsg("Invoke the helloString2() method of WebMethodWebService");

      result = port.helloString2("jsr181", "jsr109", address);
      if (!result.equals(EXPECTED_RESULT_2))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHelloString failed", e);
    }

    if (!pass)
      throw new Fault("testHelloString failed, got result : " + result);
    TestUtil.logMsg("Invocation of helloString2() passed");
  }

  /*
   * @testName: testHelloString5
   *
   * @assertion_ids: JWS:SPEC:3013
   *
   * @test_Strategy:
   */
  public void testHelloString5() throws Fault {
    TestUtil.logMsg("testHelloString");
    boolean pass = true;
    Name name = new Name();
    name.setFirstName("jsr181");
    name.setLastName("jsr109");
    try {
      TestUtil.logMsg("Invoke the helloString5() method of WebParamWebService");
      String result = port.helloString(name);
      if (!result.equals(EXPECTED_RESULT_1))
        pass = false;
      TestUtil.logMsg("Invocation of helloString5() passed");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHelloString5 failed", e);
    }

    if (!pass)
      throw new Fault("testHelloString5 failed");
  }

  /*
   * @testName: testHelloString6
   *
   * @assertion_ids: JWS:SPEC:3013
   *
   * @test_Strategy:
   */
  public void testHelloString6() throws Fault {
    TestUtil.logMsg("testHelloString");
    boolean pass = true;
    Name name = new Name();
    name.setFirstName("jsr181");
    name.setLastName("jaxws");
    try {
      TestUtil.logMsg("Invoke the helloString6() method of WebParamWebService");
      HelloString6Response result = port.helloString6(new HelloString6(), name);
      if (!result.getReturn().equals(EXPECTED_RESULT_6))
        pass = false;
      TestUtil.logMsg("Invocation of helloString() passed");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHelloString6 failed", e);
    }

    if (!pass)
      throw new Fault("testHelloString6 failed");
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
   * @assertion_ids: JWS:SPEC:6006; JWS:SPEC:3014; JWS:SPEC:5010
   *
   * @test_Strategy: Check for the partName of the child element.
   * 
   */

  public void testWSDL1() throws Fault {

    boolean pass = false;
    Element messageElement = null;
    Element partElement = null;
    String partElementName = null;
    String elementName = null;
    String prefix = null;
    Element wrapperElement = null;
    URL baseURL = null;
    String nameSpace = null;
    QName wrapperQName = null;

    try {
      Document currentDoc = doc;
      baseURL = new URL(url);
      WsdlUtils.setBaseUrl(url);
      messageElement = com.sun.ts.tests.jws.common.WsdlUtils.findMessageElement(
          doc, PORTTYPE_QNAME, EXPECTED_OPERATION_QNAME_1, "input");
      if (messageElement != null) {
        messageElement.getAttribute(WSDL_NAME_ATTR);
        partElement = WsdlUtils.findPartElement(doc, messageElement,
            EXPECTED_OPERATION_QNAME_1, true);
      }

      if (partElement != null) {

        elementName = partElement.getAttribute(WSDL_ELEMENT_ATTR);
        int index = elementName.indexOf(":");
        if (index != -1)
          prefix = elementName.substring(0, index);
        partElementName = elementName.substring(index + 1);

        nameSpace = WsdlUtils.getNamespaceOfPrefix(partElement, prefix);

        wrapperQName = new QName(nameSpace, partElementName);

        wrapperElement = WsdlUtils.getSchemasElementName(doc, wrapperQName,
            baseURL);

        if (wrapperElement == null) {
          Node[] nodes = WsdlUtils.getWrapperElement(doc, nameSpace, baseURL,
              partElementName);
          wrapperElement = WsdlUtils.getElement(nodes);
          currentDoc = WsdlUtils.getDocument(nodes);
        }

        if (wrapperElement != null) {
          String targetNameSpace = DescriptionUtils
              .getTargetNamespaceAttr(currentDoc);
          if (!NAMESPACEURI.equals(targetNameSpace))
            throw new Fault(" Name space of schema with wrapper element "
                + wrapperElement.getAttribute(XSD_NAME_ATTR) + " is not "
                + NAMESPACEURI);
          TestUtil.logMsg("Got wrapper Element : " + targetNameSpace + ":"
              + wrapperElement.getAttribute(XSD_NAME_ATTR));
          Element complexType = DescriptionUtils.getChildElement(wrapperElement,
              XSD_NAMESPACE_URI, XSD_COMPLEXTYPE_LOCAL_NAME);
          if (complexType == null) {
            Node[] complexTypeNodes = WsdlUtils.getComplexType(doc,
                wrapperElement, baseURL);
            complexType = WsdlUtils.getElement(complexTypeNodes);
            currentDoc = WsdlUtils.getDocument(complexTypeNodes);
            if (currentDoc.getDocumentURI() != null)
              baseURL = new URL(currentDoc.getDocumentURI());
          }
          if (complexType != null) {
            pass = findParameterElement(currentDoc, complexType, baseURL,
                EXPECTED_PART_ELEMENT_QNAME_1, true);
          } else {
            throw new Fault("Cannot Find Complex Type "
                + wrapperElement.getAttribute(XSD_TYPE_ATTR));
          }
        }

      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL1 failed", ex);
    }

    if (pass) {
      TestUtil
          .logMsg(" testWSDL1() passed :  Found the part element with name : "
              + EXPECTED_PART_ELEMENT_NAME_1 + " in the message "
              + partElementName);
    } else {
      throw new Fault(
          "testWSDL1 failed : could not find part element with name : "
              + EXPECTED_PART_ELEMENT_NAME_1 + " in the message "
              + partElementName);
    }

  }

  /*
   * @testName: testWSDL2
   *
   * @assertion_ids: JWS:SPEC:6004; JWS:SPEC:6006; JWS:SPEC:6003; JWS:SPEC:3014;
   * JWS:SPEC:5010
   *
   * @test_Strategy: Check for the element parameter Name
   *
   */

  public void testWSDL2() throws Fault {

    boolean pass = false;
    Element messageElement = null;
    Element partElement = null;
    String partElementName = null;
    String elementName = null;
    String prefix = null;
    Element wrapperElement = null;
    URL baseURL = null;
    String nameSpace = null;
    QName wrapperQName = null;

    try {
      Document currentDoc = doc;
      baseURL = new URL(url);
      WsdlUtils.setBaseUrl(url);
      messageElement = com.sun.ts.tests.jws.common.WsdlUtils.findMessageElement(
          doc, PORTTYPE_QNAME, EXPECTED_OPERATION_QNAME_2, "input");
      if (messageElement != null) {
        messageElement.getAttribute(WSDL_NAME_ATTR);
        partElement = WsdlUtils.findPartElement(doc, messageElement,
            EXPECTED_OPERATION_QNAME_2, true);
      }

      if (partElement != null) {

        elementName = partElement.getAttribute(WSDL_ELEMENT_ATTR);
        int index = elementName.indexOf(":");
        if (index != -1)
          prefix = elementName.substring(0, index);
        partElementName = elementName.substring(index + 1);

        nameSpace = WsdlUtils.getNamespaceOfPrefix(partElement, prefix);

        wrapperQName = new QName(nameSpace, partElementName);

        wrapperElement = WsdlUtils.getSchemasElementName(doc, wrapperQName,
            baseURL);

        if (wrapperElement != null)
          currentDoc = wrapperElement.getOwnerDocument();

        if (wrapperElement == null) {
          TestUtil.logMsg("Wrapper Element is NULL will search in the imports");
          Node[] nodes = WsdlUtils.getWrapperElement(doc, nameSpace, baseURL,
              partElementName);
          wrapperElement = WsdlUtils.getElement(nodes);
          currentDoc = WsdlUtils.getDocument(nodes);
        }

        if (wrapperElement != null) {
          String targetNameSpace = DescriptionUtils
              .getTargetNamespaceAttr(currentDoc);
          if (!NAMESPACEURI.equals(targetNameSpace))
            throw new Fault(" Name space of schema with wrapper element "
                + wrapperElement.getAttribute(XSD_NAME_ATTR) + " is not "
                + NAMESPACEURI);
          TestUtil.logMsg("Got wrapper Element : " + targetNameSpace + ":"
              + wrapperElement.getAttribute(XSD_NAME_ATTR));
          Element complexType = DescriptionUtils.getChildElement(wrapperElement,
              XSD_NAMESPACE_URI, XSD_COMPLEXTYPE_LOCAL_NAME);
          if (complexType == null) {
            Node[] complexTypeNodes = WsdlUtils.getComplexType(doc,
                wrapperElement, baseURL);
            complexType = WsdlUtils.getElement(complexTypeNodes);
            currentDoc = WsdlUtils.getDocument(complexTypeNodes);
            if (currentDoc.getDocumentURI() != null)
              baseURL = new URL(currentDoc.getDocumentURI());
          }
          if (complexType != null) {
            pass = findParameterElement(currentDoc, complexType, baseURL,
                EXPECTED_PART_ELEMENT_QNAME_2, false);
          } else {
            throw new Fault("Cannot Find Complex Type "
                + wrapperElement.getAttribute(XSD_TYPE_ATTR));
          }
        }

      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL2 failed", ex);
    }

    if (pass) {
      TestUtil
          .logMsg(" testWSDL2() passed :  Found the part element with name : "
              + EXPECTED_PART_ELEMENT_QNAME_2 + " in the WSDL ");
    } else {
      throw new Fault(
          "testWSDL2 failed : could not find part element with name : "
              + EXPECTED_PART_ELEMENT_QNAME_2 + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL3
   *
   * @assertion_ids: JWS:SPEC:6005; JWS:SPEC:3014; JWS:SPEC:5010
   *
   * @test_Strategy: Check for the default element parameter Name
   *
   */

  public void testWSDL3() throws Fault {

    boolean pass = false;
    Element messageElement = null;
    Element partElement = null;
    String partElementName = null;
    String elementName = null;
    String prefix = null;
    Element wrapperElement = null;
    URL baseURL = null;
    String[] result = null;
    String nameSpace = null;
    QName wrapperQName = null;

    try {
      Document currentDoc = doc;
      baseURL = new URL(url);
      WsdlUtils.setBaseUrl(url);
      messageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          EXPECTED_OPERATION_QNAME_3, "input");
      if (messageElement != null) {
        messageElement.getAttribute(WSDL_NAME_ATTR);
        partElement = WsdlUtils.findPartElement(doc, messageElement,
            EXPECTED_OPERATION_QNAME_3, true);
      }

      if (partElement != null) {
        elementName = partElement.getAttribute(WSDL_ELEMENT_ATTR);
        int index = elementName.indexOf(":");
        if (index != -1)
          prefix = elementName.substring(0, index);
        partElementName = elementName.substring(index + 1);

        nameSpace = WsdlUtils.getNamespaceOfPrefix(partElement, prefix);

        wrapperQName = new QName(nameSpace, partElementName);
        wrapperElement = WsdlUtils.getSchemasElementName(doc, wrapperQName,
            baseURL);

        if (wrapperElement == null) {
          Node[] nodes = WsdlUtils.getWrapperElement(doc, nameSpace, baseURL,
              partElementName);
          wrapperElement = WsdlUtils.getElement(nodes);
          currentDoc = WsdlUtils.getDocument(nodes);
        }

        if (wrapperElement != null) {
          String targetNameSpace = DescriptionUtils
              .getTargetNamespaceAttr(currentDoc);
          if (!NAMESPACEURI.equals(targetNameSpace))
            throw new Fault(" Name space of schema with wrapper element "
                + wrapperElement.getAttribute(XSD_NAME_ATTR) + " is not "
                + NAMESPACEURI);
          TestUtil.logMsg("Got wrapper Element : " + targetNameSpace + ":"
              + wrapperElement.getAttribute(XSD_NAME_ATTR));
          Element complexType = DescriptionUtils.getChildElement(wrapperElement,
              XSD_NAMESPACE_URI, XSD_COMPLEXTYPE_LOCAL_NAME);
          if (complexType == null) {
            Node[] complexTypeNodes = WsdlUtils.getComplexType(doc,
                wrapperElement, baseURL);
            complexType = WsdlUtils.getElement(complexTypeNodes);
            currentDoc = WsdlUtils.getDocument(complexTypeNodes);
            if (currentDoc.getDocumentURI() != null)
              baseURL = new URL(currentDoc.getDocumentURI());
          }
          if (complexType != null) {
            result = WsdlUtils.checkParameterOrder(currentDoc, baseURL,
                complexType, DEFAULT_QNAMES);
            if (WsdlUtils.checkQNamesEquals(DEFAULT_NAMES, result))
              pass = true;
          } else {
            throw new Fault("Cannot Find Complex Type "
                + wrapperElement.getAttribute(XSD_TYPE_ATTR));
          }
        }

      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL3 failed", ex);
    }

    if (pass) {
      TestUtil
          .logMsg(" testWSDL3() passed :  Found the part element with name : "
              + EXPECTED_DEFAULT_PART_ELEMENT_NAME_1 + " in the message "
              + partElementName);
    } else {
      throw new Fault(
          "testWSDL3 failed : could not find part element with name : "
              + EXPECTED_DEFAULT_PART_ELEMENT_NAME_1 + " in the message "
              + partElementName);
    }

  }

  /*
   * @testName: testWSDL4
   *
   * @assertion_ids: JWS:SPEC:5009; JWS:SPEC:6006; JWS:SPEC:3014; JWS:SPEC:5010
   *
   * @test_Strategy: Check for the partName of the child element
   *
   */

  public void testWSDL4() throws Fault {

    boolean pass = false;
    Element messageElement = null;
    String messageName = null;
    Element partElement = null;
    String partElementName = null;
    String elementName = null;
    String prefix = null;
    Element wrapperElement = null;
    URL baseURL = null;
    String nameSpace = null;
    QName wrapperQName = null;

    try {
      Document currentDoc = doc;
      baseURL = new URL(url);
      WsdlUtils.setBaseUrl(url);
      messageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          EXPECTED_OPERATION_QNAME_4, "input");
      if (messageElement != null) {
        messageName = messageElement.getAttribute(WSDL_NAME_ATTR);
        partElement = WsdlUtils.findPartElement(doc, messageElement,
            EXPECTED_OPERATION_QNAME_4, true);
      }

      if (partElement != null) {

        elementName = partElement.getAttribute(WSDL_ELEMENT_ATTR);
        int index = elementName.indexOf(":");
        if (index != -1)
          prefix = elementName.substring(0, index);
        partElementName = elementName.substring(index + 1);

        nameSpace = WsdlUtils.getNamespaceOfPrefix(partElement, prefix);

        wrapperQName = new QName(nameSpace, partElementName);
        wrapperElement = WsdlUtils.getSchemasElementName(doc, wrapperQName,
            baseURL);

        if (wrapperElement == null) {
          Node[] nodes = WsdlUtils.getWrapperElement(doc, nameSpace, baseURL,
              partElementName);
          wrapperElement = WsdlUtils.getElement(nodes);
          currentDoc = WsdlUtils.getDocument(nodes);
        }

        if (wrapperElement != null) {
          String targetNameSpace = DescriptionUtils
              .getTargetNamespaceAttr(currentDoc);
          if (!NAMESPACEURI.equals(targetNameSpace))
            throw new Fault(" Name space of schema with wrapper element "
                + wrapperElement.getAttribute(XSD_NAME_ATTR) + " is not "
                + NAMESPACEURI);
          TestUtil.logMsg("Got wrapper Element : " + targetNameSpace + ":"
              + wrapperElement.getAttribute(XSD_NAME_ATTR));
          Element complexType = DescriptionUtils.getChildElement(wrapperElement,
              XSD_NAMESPACE_URI, XSD_COMPLEXTYPE_LOCAL_NAME);
          if (complexType == null) {
            Node[] complexTypeNodes = WsdlUtils.getComplexType(doc,
                wrapperElement, baseURL);
            complexType = WsdlUtils.getElement(complexTypeNodes);
            currentDoc = WsdlUtils.getDocument(complexTypeNodes);
            if (currentDoc.getDocumentURI() != null)
              baseURL = new URL(currentDoc.getDocumentURI());
          }
          if (complexType != null) {
            pass = findParameterElement(currentDoc, complexType, baseURL,
                EXPECTED_PART_ELEMENT_QNAME_4, false);
          } else {
            throw new Fault("Cannot Find Complex Type "
                + wrapperElement.getAttribute(XSD_TYPE_ATTR));
          }
        }

      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL4 failed", ex);
    }

    if (pass) {
      TestUtil
          .logMsg(" testWSDL4() passed :  Found the part element with name : "
              + EXPECTED_PART_ELEMENT_QNAME_4 + " in the input message "
              + messageName + " in the WSDL ");
    } else {
      throw new Fault(
          "testWSDL4 failed : could not find part element with name : "
              + EXPECTED_PART_ELEMENT_QNAME_4 + " in the input message "
              + messageName + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL5
   *
   * @assertion_ids: JWS:SPEC:6006; JWS:SPEC:3014; JWS:SPEC:5010
   *
   * @test_Strategy: Check for the partName of the child element
   *
   */

  public void testWSDL5() throws Fault {

    boolean pass = false;
    Element messageElement = null;
    String messageName = null;
    Element partElement = null;
    String elementName = null;
    String partElementName = null;
    String prefix = null;
    Element wrapperElement = null;
    URL baseURL = null;
    String nameSpace = null;
    QName wrapperQName = null;

    try {
      Document currentDoc = doc;

      baseURL = new URL(url);
      WsdlUtils.setBaseUrl(url);

      messageElement = com.sun.ts.tests.jws.common.WsdlUtils.findMessageElement(
          doc, PORTTYPE_QNAME, EXPECTED_OPERATION_QNAME_4, "output");
      if (messageElement != null) {
        messageName = messageElement.getAttribute(WSDL_NAME_ATTR);
        partElement = WsdlUtils.findPartElement(doc, messageElement,
            new QName(NAMESPACEURI, "helloString4Response"), true);
      }

      if (partElement != null) {

        elementName = partElement.getAttribute(WSDL_ELEMENT_ATTR);
        int index = elementName.indexOf(":");
        if (index != -1)
          prefix = elementName.substring(0, index);
        partElementName = elementName.substring(index + 1);

        nameSpace = WsdlUtils.getNamespaceOfPrefix(partElement, prefix);

        wrapperQName = new QName(nameSpace, partElementName);
        wrapperElement = WsdlUtils.getSchemasElementName(doc, wrapperQName,
            baseURL);

        if (wrapperElement == null) {
          TestUtil.logMsg(" Trying to get the wrapper Element");
          Node[] nodes = WsdlUtils.getWrapperElement(doc, nameSpace, baseURL,
              partElementName);
          wrapperElement = WsdlUtils.getElement(nodes);
          currentDoc = WsdlUtils.getDocument(nodes);
          if (currentDoc.getDocumentURI() != null)
            baseURL = new URL(currentDoc.getDocumentURI());
        }

        if (wrapperElement != null) {
          String targetNameSpace = DescriptionUtils
              .getTargetNamespaceAttr(currentDoc);
          if (!NAMESPACEURI.equals(targetNameSpace))
            throw new Fault(" Name space of schema with wrapper element "
                + wrapperElement.getAttribute(XSD_NAME_ATTR) + " is not "
                + NAMESPACEURI);
          TestUtil.logMsg("Got wrapper Element : " + targetNameSpace + ":"
              + wrapperElement.getAttribute(XSD_NAME_ATTR));
          Element complexType = DescriptionUtils.getChildElement(wrapperElement,
              XSD_NAMESPACE_URI, XSD_COMPLEXTYPE_LOCAL_NAME);
          if (complexType == null) {
            Node[] complexTypeNodes = WsdlUtils.getComplexType(doc,
                wrapperElement, baseURL);
            complexType = WsdlUtils.getElement(complexTypeNodes);
            currentDoc = WsdlUtils.getDocument(complexTypeNodes);
            if (currentDoc.getDocumentURI() != null)
              baseURL = new URL(currentDoc.getDocumentURI());
          }
          if (complexType != null) {
            pass = findParameterElement(currentDoc, complexType, baseURL,
                EXPECTED_PART_ELEMENT_QNAME_4, false);
          } else {
            throw new Fault("Cannot Find Complex Type "
                + wrapperElement.getAttribute(XSD_TYPE_ATTR));
          }
        }

      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL5 failed", ex);
    }

    if (pass) {
      TestUtil
          .logMsg(" testWSDL5() passed :  Found the part element with name : "
              + EXPECTED_PART_ELEMENT_QNAME_4 + " in the output message "
              + messageName + " in the WSDL ");
    } else {
      throw new Fault(
          "testWSDL5 failed : could not find part element with name : "
              + EXPECTED_PART_ELEMENT_QNAME_4 + " in the output message "
              + messageName + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL6
   *
   * @assertion_ids: JWS:SPEC:6006; JWS:SPEC:3014; JWS:SPEC:5010
   *
   * @test_Strategy: Check for the partName of the child element
   *
   */

  public void testWSDL6() throws Fault {

    boolean pass = false;
    Element messageElement = null;
    String messageName = null;
    Element partElement = null;
    String partElementName = null;
    String elementName = null;
    String prefix = null;
    Element wrapperElement = null;
    URL baseURL = null;
    String nameSpace = null;
    QName wrapperQName = null;

    try {
      Document currentDoc = doc;
      baseURL = new URL(url);
      WsdlUtils.setBaseUrl(url);
      messageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          EXPECTED_OPERATION_QNAME_4, "output");
      if (messageElement != null) {
        messageName = messageElement.getAttribute(WSDL_NAME_ATTR);
        partElement = WsdlUtils.findPartElement(doc, messageElement,
            new QName(NAMESPACEURI, "helloString4Response"), true);
      }

      if (partElement != null) {

        elementName = partElement.getAttribute(WSDL_ELEMENT_ATTR);
        int index = elementName.indexOf(":");
        if (index != -1)
          prefix = elementName.substring(0, index);
        partElementName = elementName.substring(index + 1);

        nameSpace = WsdlUtils.getNamespaceOfPrefix(partElement, prefix);

        wrapperQName = new QName(nameSpace, partElementName);
        wrapperElement = WsdlUtils.getSchemasElementName(doc, wrapperQName,
            baseURL);

        if (wrapperElement == null) {
          Node[] nodes = WsdlUtils.getWrapperElement(doc, nameSpace, baseURL,
              partElementName);
          wrapperElement = WsdlUtils.getElement(nodes);
          currentDoc = WsdlUtils.getDocument(nodes);
        }

        if (wrapperElement != null) {
          String targetNameSpace = DescriptionUtils
              .getTargetNamespaceAttr(currentDoc);
          if (!NAMESPACEURI.equals(targetNameSpace))
            throw new Fault(" Name space of schema with wrapper element "
                + wrapperElement.getAttribute(XSD_NAME_ATTR) + " is not "
                + NAMESPACEURI);
          TestUtil.logMsg("Got wrapper Element : " + targetNameSpace + ":"
              + wrapperElement.getAttribute(XSD_NAME_ATTR));
          Element complexType = DescriptionUtils.getChildElement(wrapperElement,
              XSD_NAMESPACE_URI, XSD_COMPLEXTYPE_LOCAL_NAME);
          if (complexType == null) {
            Node[] complexTypeNodes = WsdlUtils.getComplexType(doc,
                wrapperElement, baseURL);
            complexType = WsdlUtils.getElement(complexTypeNodes);
            currentDoc = WsdlUtils.getDocument(complexTypeNodes);
            if (currentDoc.getDocumentURI() != null)
              baseURL = new URL(currentDoc.getDocumentURI());
          }
          if (complexType != null) {
            pass = findParameterElement(currentDoc, complexType, baseURL,
                EXPECTED_PART_ELEMENT_QNAME_5, true);
          } else {
            throw new Fault("Cannot Find Complex Type "
                + wrapperElement.getAttribute(XSD_TYPE_ATTR));
          }
        }

      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL6 failed", ex);
    }

    if (pass) {
      TestUtil
          .logMsg(" testWSDL6() passed :  Found the part element with name : "
              + EXPECTED_PART_ELEMENT_NAME_5 + " in the output message "
              + messageName + " in the WSDL ");
    } else {
      throw new Fault(
          "testWSDL6 failed : could not find part element with name : "
              + EXPECTED_PART_ELEMENT_NAME_5 + " in the output message "
              + messageName + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL7
   *
   * @assertion_ids: JWS:SPEC:6012
   *
   * @test_Strategy: Check for the header
   *
   */

  public void testWSDL7() throws Fault {

    boolean pass = false;
    Element operationElement = null;
    Element soapHeaderElement = null;
    Element messageElement = null;
    Element partElement = null;

    try {

      operationElement = WsdlUtils.findOperationElement(doc,
          EXPECTED_OPERATION_QNAME_5, SERVICE_QNAME, PORT_QNAME);

      if (operationElement != null) {

        soapHeaderElement = WsdlUtils.findSoapHeaderElement(doc,
            operationElement, "input", EXPECTED_HEADER_PART_ELEMENT_NAME_1,
            PORTTYPE_QNAME, EXPECTED_OPERATION_QNAME_5);

      }

      messageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          EXPECTED_OPERATION_QNAME_5, "input");

      if (messageElement != null) {

        partElement = com.sun.ts.tests.jws.common.WsdlUtils.findPartElement(doc,
            messageElement, EXPECTED_HEADER_PART_ELEMENT_QNAME_1, true);

      }

      if (soapHeaderElement != null && partElement != null)
        pass = true;

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL7 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(" testWSDL7() passed :  Found the soap header element : "
          + soapHeaderElement + " : " + EXPECTED_HEADER_PART_ELEMENT_QNAME_1
          + " in the WSDL");
    } else {
      throw new Fault("testWSDL7 failed : could not find soap header element : "
          + EXPECTED_HEADER_PART_ELEMENT_QNAME_1 + " in the WSDL ");
    }

  }

}
