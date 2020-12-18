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

package com.sun.ts.tests.jws.webresult.webresult2.client;

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
import com.sun.ts.tests.jws.common.DescriptionConstants;
import com.sun.ts.tests.jws.common.DescriptionUtils;
import com.sun.ts.tests.jws.common.JWS_Util;
import com.sun.ts.tests.jws.common.SchemaConstants;
import com.sun.ts.tests.jws.common.WsdlUtils;

/**
 * @test
 * @executeClass com.sun.ts.tests.jws.webresult.webresult1.client.Client
 * @sources Client.java
 * @keywords webservice
 */

public class Client extends ServiceEETest
    implements DescriptionConstants, SchemaConstants {

  static WebResult2WebServiceService service = null;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "jwstest.webresult2webservice.endpoint.1";

  private static final String EXPECTED_DEFAULT_PART_NAME_1 = "helloStringResponse";

  private static final String EXPECTED_DEFAULT_PART_NAME_2 = "helloString2Response";

  private static final String EXPECTED_ELEMENT_TARGETNAMESPACE_4 = "hello4/employee";

  private static final String EXPECTED_ELEMENT_TARGETNAMESPACE_5 = "hello5/name";

  private static final String EXPECTED_OPERATION_NAME_1 = "helloString";

  private static final String EXPECTED_OPERATION_NAME_2 = "helloString2";

  private static final String EXPECTED_OPERATION_NAME_3 = "helloString3";

  private static final String EXPECTED_OPERATION_NAME_4 = "helloString4";

  private static final String EXPECTED_OPERATION_NAME_5 = "helloString5";

  private static final String EXPECTED_PART_ELEMENT_NAME_1 = "return";

  private static final String EXPECTED_PART_ELEMENT_NAME_2 = "return";

  private static final String EXPECTED_PART_ELEMENT_NAME_3 = "name3";

  private static final String EXPECTED_PART_ELEMENT_NAME_4 = "employee";

  private static final String EXPECTED_PART_ELEMENT_NAME_5 = "name5";

  private static final String EXPECTED_PART_NAME_3 = "name3";

  private static final String EXPECTED_PART_NAME_4 = "Employee";

  private static final String EXPECTED_HEADER_PART_NAME_4 = "employee";

  private static final String EXPECTED_RESULT_1 = "hello1 : Hello jsr181 to Web Service";

  private static final String EXPECTED_RESULT_2 = "hello2 : Hello First Name:jsr181 Last Name:jaxws to Web Service";

  private static final String HOSTNAME = "localhost";

  private static final String MODEPROP = "platform.mode";

  // service and port information
  private static final String NAMESPACEURI = "http://server.webresult2.webresult.jws.tests.ts.sun.com/";

  private static final String PKG_NAME = "com.sun.ts.tests.jws.webresult.webresult2.client.";

  private static final String PORT_NAME = "webResult2WebServicePort";

  private static final int PORTNUM = 8000;

  private static final String PORTTYPE_NAME = "webResult2WebService";

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String SERVICE_NAME = "webResult2WebServiceService";

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String WSDLLOC_URL = "jwstest.webresult2webservice.wsdlloc.1";

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

  private QName EXPECTED_DEFAULT_PART_QNAME_2 = new QName(NAMESPACEURI,
      EXPECTED_DEFAULT_PART_NAME_2);

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

  private QName EXPECTED_PART_ELEMENT_QNAME_1 = new QName(NAMESPACEURI,
      EXPECTED_PART_ELEMENT_NAME_1);

  private QName EXPECTED_PART_ELEMENT_QNAME_2 = new QName(NAMESPACEURI,
      EXPECTED_PART_ELEMENT_NAME_2);

  private QName EXPECTED_PART_ELEMENT_QNAME_3 = new QName(NAMESPACEURI,
      EXPECTED_PART_ELEMENT_NAME_3);

  private QName EXPECTED_PART_ELEMENT_QNAME_4 = new QName(
      EXPECTED_ELEMENT_TARGETNAMESPACE_4, EXPECTED_PART_ELEMENT_NAME_4);

  private QName EXPECTED_PART_ELEMENT_QNAME_5 = new QName(
      EXPECTED_ELEMENT_TARGETNAMESPACE_5, EXPECTED_PART_ELEMENT_NAME_5);

  private QName EXPECTED_PART_QNAME_3 = new QName(NAMESPACEURI,
      EXPECTED_PART_NAME_3);

  private QName EXPECTED_PART_QNAME_4 = new QName(NAMESPACEURI,
      EXPECTED_PART_NAME_4);

  private QName EXPECTED_HEADER_PART_QNAME_4 = new QName(
      EXPECTED_ELEMENT_TARGETNAMESPACE_4, EXPECTED_HEADER_PART_NAME_4);

  private String hostname = HOSTNAME;

  String modeProperty = null; // platform.mode -> (standalone|j2ee)

  private WebResult2WebService port;

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
    port = (WebResult2WebService) service.getPort(WebResult2WebService.class);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
  }

  private void getPortStandalone() throws Exception {
    port = (WebResult2WebService) JWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        WebResult2WebServiceService.class, PORT_QNAME,
        WebResult2WebService.class);
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
        /*
         * url = "file:/C:/jsr181/test-wsdl/"; File myfile = new
         * File("C:/jsr181/test-wsdl/issue2.wsdl"); wsdlurl = myfile.toURL();
         * TestUtil.logMsg("URL " + wsdlurl.toString());
         */
        getTestURLs();
        getPortStandalone();
      } else {
        getTestURLs();
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (WebResult2WebServiceService) getSharedObject();
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

  private Element getGlobalSchemaElement(Document doc, URL baseURL,
      Element partElement) throws Exception {
    String elementAtt = partElement.getAttribute(WSDL_ELEMENT_ATTR);
    Node[] nodes = null;
    Element element = null;
    if (elementAtt != null) {
      QName partQName = WsdlUtils.createQName(partElement, elementAtt);
      String namespace = partQName.getNamespaceURI();
      String localName = partQName.getLocalPart();

      if (DescriptionUtils.isSchema(doc)) {
        nodes = WsdlUtils.getWrapperElement(doc, namespace, baseURL, localName);
        if (nodes != null) {
          TestUtil.logMsg(" Got Global Schema Element : " + partQName);
        }
        element = (nodes == null ? null : WsdlUtils.getElement(nodes));
      } else if (DescriptionUtils.isDescription(doc)) {
        element = WsdlUtils.getSchemasElementName(doc, partQName, baseURL);
        if (element == null) {
          TestUtil.logMsg(" parameterElement is null ");
          Document[] schemas = DescriptionUtils.getSchemaDocuments(doc,
              baseURL);
          for (int i = 0; i < schemas.length; i++) {
            Node[] parameterNodes = WsdlUtils.getParameterElement(doc, baseURL,
                partQName);
            if (parameterNodes != null) {
              element = WsdlUtils.getElement(parameterNodes);
            }
          }
        }
      }
    }

    return element;
  }

  private Node[] getGlobalComplexTypeElement(Document doc, URL baseURL,
      Element headerElement) throws Exception {
    Element complexTypeElement = null;
    Node[] complexTypeNodes = WsdlUtils.getComplexType(doc, headerElement,
        baseURL);
    if (complexTypeNodes != null) {
      complexTypeElement = WsdlUtils.getElement(complexTypeNodes);
      Document document = WsdlUtils.getDocument(complexTypeNodes);
      TestUtil.logMsg("Got the Complex Type Element : "
          + complexTypeElement.getAttribute(WSDL_NAME_ATTR)
          + " in Schema with TargetNamespace "
          + DescriptionUtils.getTargetNamespaceAttr(document));
    }
    return complexTypeNodes;
  }

  private boolean findParameterElement(Document document, Element element,
      URL baseURL, QName qName, boolean checkList) throws Exception {

    boolean pass = false;

    NodeList list = element.getElementsByTagNameNS(XSD_NAMESPACE_URI,
        XSD_ELEMENT_LOCAL_NAME);
    Element parameterRefElement = WsdlUtils.getParameterRefElement(list, qName);
    if (parameterRefElement != null) {
      Element parameterElement = null;
      if (DescriptionUtils.isSchema(document)) {
        Node[] parameterNodes = WsdlUtils.getParameterElement(document, baseURL,
            qName);
        if (parameterNodes != null) {
          parameterElement = WsdlUtils.getElement(parameterNodes);
          if (parameterElement != null)
            pass = true;
        }
      } else if (DescriptionUtils.isDescription(document)) {
        TestUtil.logMsg("Document is a WSDL");
        parameterElement = WsdlUtils.getSchemasElementName(document, qName,
            baseURL);
        if (parameterElement == null) {
          TestUtil.logMsg(" parameterElement is null ");
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
      TestUtil.logMsg("Invoke the helloString() method of WebResultWebService");
      result = port.helloString("jsr181");
      if (!result.equals(EXPECTED_RESULT_1))
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e.getMessage());
      throw new Fault("testHelloString failed", e);
    }

    if (!pass)
      throw new Fault("testHelloString failed returned value : " + result);
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
    TestUtil.logMsg("testHelloString2");
    boolean pass = true;
    String result = "";
    try {
      TestUtil
          .logMsg("Invoke the helloString2() method of WebMethodWebService");
      Name name = new Name();
      name.setFirstName("jsr181");
      name.setLastName("jaxws");
      result = port.helloString2(name);
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
   * @testName: testHelloString3
   *
   * @assertion_ids: JWS:SPEC:3013
   *
   * @test_Strategy:
   */
  public void testHelloString3() throws Fault {
    TestUtil.logMsg("testHelloString3");
    boolean pass = true;
    Name output = null;
    try {
      TestUtil
          .logMsg("Invoke the helloString3() method of WebMethodWebService");

      output = port.helloString3(true);

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
      throw new Fault("testHelloString3 failed, returned value : " + output);
    TestUtil.logMsg("Invocation of helloString3() passed");
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
      TestUtil.logMsg("Get the WSDL of WebResultWebService");
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware(true);
      DocumentBuilder builder = factory.newDocumentBuilder();
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
   * @assertion_ids: JWS:SPEC:6007; JWS:SPEC:4003
   *
   * @test_Strategy: Check for the default element QName of the part and the
   * default name of the part
   *
   */

  public void testWSDL1() throws Fault {

    boolean pass = false;
    Element messageElement = null;
    Element partElement = null;
    String partElementName = null;
    String elementName = null;
    Element wrapperElement = null;
    Element complexTypeElement = null;
    URL baseURL = null;
    String prefix = null;
    String nameSpace = null;
    QName wrapperQName = null;

    try {
      Document currentDoc = doc;
      baseURL = new URL(url);
      WsdlUtils.setBaseUrl(url);
      messageElement = com.sun.ts.tests.jws.common.WsdlUtils.findMessageElement(
          doc, PORTTYPE_QNAME, EXPECTED_OPERATION_QNAME_1, "output");
      if (messageElement != null) {
        partElement = WsdlUtils.findPartElement(doc, messageElement,
            new QName(NAMESPACEURI, "helloStringResponse"), true);
      }

      if (partElement != null) {

        elementName = partElement.getAttribute(WSDL_ELEMENT_ATTR);
        int index = elementName.indexOf(":");
        partElementName = elementName.substring(index + 1);
        prefix = elementName.substring(0, index);
        nameSpace = WsdlUtils.getNamespaceOfPrefix(partElement, prefix);

        wrapperQName = new QName(nameSpace, partElementName);

        wrapperElement = WsdlUtils.getSchemasElementName(doc, wrapperQName,
            baseURL);

        if (wrapperElement == null) {
          wrapperElement = getGlobalSchemaElement(doc, baseURL, partElement);
        }

        if (wrapperElement != null) {
          Node[] nodes = getGlobalComplexTypeElement(doc, baseURL,
              wrapperElement);
          complexTypeElement = WsdlUtils.getElement(nodes);
          currentDoc = WsdlUtils.getDocument(nodes);
          if (currentDoc.getDocumentURI() != null)
            baseURL = new URL(currentDoc.getDocumentURI());
        }

        if (complexTypeElement != null) {
          pass = findParameterElement(currentDoc, complexTypeElement, baseURL,
              EXPECTED_PART_ELEMENT_QNAME_1, true);
        } else {
          throw new Fault("Cannot Find Complex Type "
              + wrapperElement.getAttribute(XSD_TYPE_ATTR));
        }
      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL1 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(
          " testWSDL1() passed :  Found the part element with element name : "
              + EXPECTED_PART_ELEMENT_NAME_1 + " in the message "
              + partElementName);
    } else {
      throw new Fault(
          "testWSDL1 failed : could not find part element with element name : "
              + EXPECTED_PART_ELEMENT_NAME_1 + " in the message "
              + partElementName);
    }

  }

  /*
   * @testName: testWSDL2
   *
   * @assertion_ids: JWS:SPEC:6007
   *
   * @test_Strategy: Check for the default element QName of the part
   *
   */

  public void testWSDL2() throws Fault {

    boolean pass = false;
    Element messageElement = null;
    Element partElement = null;
    String partElementName = null;
    String elementName = null;
    Element wrapperElement = null;
    Element complexTypeElement = null;
    URL baseURL = null;
    String prefix = null;
    String nameSpace = null;
    QName wrapperQName = null;

    try {
      Document currentDoc = null;
      baseURL = new URL(url);
      WsdlUtils.setBaseUrl(url);
      messageElement = com.sun.ts.tests.jws.common.WsdlUtils.findMessageElement(
          doc, PORTTYPE_QNAME, EXPECTED_OPERATION_QNAME_2, "output");
      if (messageElement != null) {
        partElement = WsdlUtils.findPartElement(doc, messageElement,
            new QName(NAMESPACEURI, "helloString2Response"), true);
      }

      if (partElement != null) {

        elementName = partElement.getAttribute(WSDL_ELEMENT_ATTR);
        int index = elementName.indexOf(":");
        partElementName = elementName.substring(index + 1);

        partElementName = elementName.substring(index + 1);
        prefix = elementName.substring(0, index);
        nameSpace = WsdlUtils.getNamespaceOfPrefix(partElement, prefix);

        wrapperQName = new QName(nameSpace, partElementName);

        wrapperElement = WsdlUtils.getSchemasElementName(doc, wrapperQName,
            baseURL);

        if (wrapperElement == null) {
          wrapperElement = getGlobalSchemaElement(doc, baseURL, partElement);
        }

        if (wrapperElement != null) {
          Node[] nodes = getGlobalComplexTypeElement(doc, baseURL,
              wrapperElement);
          complexTypeElement = WsdlUtils.getElement(nodes);
          currentDoc = WsdlUtils.getDocument(nodes);
          if (currentDoc.getDocumentURI() != null)
            baseURL = new URL(currentDoc.getDocumentURI());
        }

        if (complexTypeElement != null) {
          pass = findParameterElement(currentDoc, complexTypeElement, baseURL,
              EXPECTED_PART_ELEMENT_QNAME_2, true);
        } else {
          throw new Fault("Cannot Find Complex Type "
              + wrapperElement.getAttribute(XSD_TYPE_ATTR));
        }
      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL2 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(
          " testWSDL2() passed :  Found the part element with element name : "
              + EXPECTED_PART_ELEMENT_NAME_2 + " in the message "
              + partElementName);
    } else {
      throw new Fault(
          "testWSDL2 failed : could not find part element with element name : "
              + EXPECTED_PART_ELEMENT_NAME_2 + " in the message "
              + partElementName);
    }

  }

  /*
   * @testName: testWSDL3
   *
   * @assertion_ids: JWS:SPEC:6007;
   *
   * @test_Strategy: Check for the part element name and parameter element when
   * "name" attribute of WebResult set
   *
   */

  public void testWSDL3() throws Fault {

    boolean pass = false;
    Element messageElement = null;
    Element partElement = null;
    String partElementName = null;
    String elementName = null;
    Element wrapperElement = null;
    Element complexTypeElement = null;
    URL baseURL = null;
    String prefix = null;
    String nameSpace = null;
    QName wrapperQName = null;

    try {
      Document currentDoc = null;
      baseURL = new URL(url);
      WsdlUtils.setBaseUrl(url);
      messageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          EXPECTED_OPERATION_QNAME_3, "output");
      if (messageElement != null) {
        partElement = WsdlUtils.findPartElement(doc, messageElement,
            new QName(NAMESPACEURI, "helloString3Response"), true);
      }

      if (partElement != null) {

        elementName = partElement.getAttribute(WSDL_ELEMENT_ATTR);
        int index = elementName.indexOf(":");
        partElementName = elementName.substring(index + 1);
        partElementName = elementName.substring(index + 1);
        prefix = elementName.substring(0, index);
        nameSpace = WsdlUtils.getNamespaceOfPrefix(partElement, prefix);

        wrapperQName = new QName(nameSpace, partElementName);

        wrapperElement = WsdlUtils.getSchemasElementName(doc, wrapperQName,
            baseURL);

        if (wrapperElement == null) {
          wrapperElement = getGlobalSchemaElement(doc, baseURL, partElement);
        }

        if (wrapperElement != null) {
          Node[] nodes = getGlobalComplexTypeElement(doc, baseURL,
              wrapperElement);
          complexTypeElement = WsdlUtils.getElement(nodes);
          currentDoc = WsdlUtils.getDocument(nodes);
          if (currentDoc.getDocumentURI() != null)
            baseURL = new URL(currentDoc.getDocumentURI());
        }

        if (complexTypeElement != null) {
          pass = findParameterElement(currentDoc, complexTypeElement, baseURL,
              EXPECTED_PART_ELEMENT_QNAME_3, true);
        } else {
          throw new Fault("Cannot Find Complex Type "
              + wrapperElement.getAttribute(XSD_TYPE_ATTR));
        }
      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL3 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(
          " testWSDL3() passed :  Found the part element with element name : "
              + EXPECTED_PART_ELEMENT_NAME_3 + " in the message "
              + partElementName);
    } else {
      throw new Fault(
          "testWSDL3 failed : could not find part element with element name : "
              + EXPECTED_PART_ELEMENT_NAME_3 + " in the message "
              + partElementName);
    }

  }

  /*
   * @testName: testWSDL4
   *
   * @assertion_ids: JWS:SPEC:6013
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
    Element headerElement = null;
    Element complexTypeElement = null;
    URL baseURL = null;

    try {
      Document currentDoc = null;
      baseURL = new URL(url);
      WsdlUtils.setBaseUrl(url);
      operationElement = com.sun.ts.tests.jws.common.WsdlUtils
          .findOperationElement(doc, EXPECTED_OPERATION_QNAME_4, SERVICE_QNAME,
              PORT_QNAME);

      if (operationElement != null) {
        soapHeaderElement = com.sun.ts.tests.jws.common.WsdlUtils
            .findSoapHeaderElement(doc, operationElement, "output",
                EXPECTED_HEADER_PART_NAME_4, PORTTYPE_QNAME,
                EXPECTED_OPERATION_QNAME_4);
        TestUtil.logMsg("SOAPHeaderElement is " + soapHeaderElement);
      }

      messageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          EXPECTED_OPERATION_QNAME_4, "output");

      if (messageElement != null) {
        partElement = WsdlUtils.findPartElement(doc, messageElement,
            EXPECTED_PART_ELEMENT_QNAME_4, true);
      }

      if (partElement != null) {
        headerElement = getGlobalSchemaElement(doc, baseURL, partElement);
      }

      if (headerElement != null) {
        Node[] nodes = getGlobalComplexTypeElement(doc, baseURL, headerElement);
        complexTypeElement = WsdlUtils.getElement(nodes);
      }

      if (complexTypeElement != null && soapHeaderElement != null)
        pass = true;

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL4 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(" testWSDL4() passed :  Found the soap header element : "
          + soapHeaderElement + " : " + EXPECTED_HEADER_PART_NAME_4
          + " in the WSDL");
    } else {
      throw new Fault("testWSDL4 failed : could not find soap header element : "
          + EXPECTED_HEADER_PART_NAME_4 + " in the WSDL ");
    }

  }

  /*
   * @testName: testWSDL5
   *
   * @assertion_ids: JWS:SPEC:6007;
   *
   * @test_Strategy: Check for the part element name and partName when "name"
   * and "partName" attributes of WebResult set
   *
   */

  public void testWSDL5() throws Fault {

    boolean pass = false;
    Element messageElement = null;
    Element partElement = null;
    String partElementName = null;
    String elementName = null;
    Element wrapperElement = null;
    Element complexTypeElement = null;
    URL baseURL = null;
    String prefix = null;
    String nameSpace = null;
    QName wrapperQName = null;

    try {
      Document currentDoc = null;
      baseURL = new URL(url);
      WsdlUtils.setBaseUrl(url);

      messageElement = WsdlUtils.findMessageElement(doc, PORTTYPE_QNAME,
          EXPECTED_OPERATION_QNAME_5, "output");
      if (messageElement != null) {
        TestUtil.logMsg("Found Message Element");
        WsdlUtils.setBaseUrl(url);
        partElement = WsdlUtils.findPartElement(doc, messageElement,
            new QName(NAMESPACEURI, "helloString5Response"), true);
      }

      if (partElement != null) {

        elementName = partElement.getAttribute(WSDL_ELEMENT_ATTR);
        int index = elementName.indexOf(":");
        partElementName = elementName.substring(index + 1);
        prefix = elementName.substring(0, index);
        nameSpace = WsdlUtils.getNamespaceOfPrefix(partElement, prefix);

        wrapperQName = new QName(nameSpace, partElementName);

        wrapperElement = WsdlUtils.getSchemasElementName(doc, wrapperQName,
            baseURL);

        if (wrapperElement == null) {
          wrapperElement = getGlobalSchemaElement(doc, baseURL, partElement);
        }

        if (wrapperElement != null) {
          Node[] nodes = getGlobalComplexTypeElement(doc, baseURL,
              wrapperElement);
          complexTypeElement = WsdlUtils.getElement(nodes);
          currentDoc = WsdlUtils.getDocument(nodes);
          if (currentDoc.getDocumentURI() != null)
            baseURL = new URL(currentDoc.getDocumentURI());
        }

        if (complexTypeElement != null) {
          pass = findParameterElement(currentDoc, complexTypeElement, baseURL,
              EXPECTED_PART_ELEMENT_QNAME_5, false);
        } else {
          throw new Fault("Cannot Find Complex Type "
              + wrapperElement.getAttribute(XSD_TYPE_ATTR));
        }
      }

    } catch (Exception ex) {
      TestUtil.logErr("Exception occurred: " + ex.getMessage());
      throw new Fault("testWSDL5 failed", ex);
    }

    if (pass) {
      TestUtil.logMsg(
          " testWSDL5() passed :  Found the part element with element name : "
              + EXPECTED_PART_ELEMENT_QNAME_5 + " in the Schema");
    } else {
      throw new Fault(
          "testWSDL5 failed : could not find part element with element name : "
              + EXPECTED_PART_ELEMENT_QNAME_5 + " in Service : " + SERVICE_QNAME
              + " in the Schema");
    }

  }
}
