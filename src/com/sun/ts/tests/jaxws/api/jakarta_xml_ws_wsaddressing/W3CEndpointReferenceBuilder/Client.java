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
 * $Id: Client.java 51088 2003-12-03 17:00:09Z af70133 $
 */

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws_wsaddressing.W3CEndpointReferenceBuilder;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.wsa.common.EprUtil;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;
import java.math.*;
import java.text.MessageFormat;

import jakarta.xml.ws.*;
import jakarta.xml.ws.wsaddressing.W3CEndpointReferenceBuilder;
import jakarta.xml.ws.wsaddressing.W3CEndpointReference;
import javax.xml.namespace.QName;
import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.w3c.dom.*;

import com.sun.javatest.Status;

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

  private static final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloService";

  private static final String PORT_NAME = "HelloPort";

  private static final String PORT_TYPE_NAME = "Hello";

  private static final QName SERVICE_QNAME = new QName(NAMESPACEURI,
      SERVICE_NAME);

  private static final QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private static final QName PORT_TYPE_QNAME = new QName(NAMESPACEURI,
      PORT_TYPE_NAME);

  private static final QName MyExtensionAttr = new QName(
      "http://extensions.org/ext", "MyExtensionAttr");

  private static final String MyExtensionAttrValue = "Hello";

  private static final Class SERVICE_CLASS = com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.HelloService.class;

  private static String xmlRefParam1 = "<myns1:MyParam1 wsa:IsReferenceParameter='true' xmlns:myns1=\"http://helloservice.org/myparam1\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">Hello</myns1:MyParam1>";

  private static String xmlRefParam2 = "<myns2:MyParam2 wsa:IsReferenceParameter='true' xmlns:myns2=\"http://helloservice.org/myparam2\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\">There</myns2:MyParam2>";

  private static String xmlMyExtensionElement = "<myext:MyExtensionElement xmlns:myext=\"http://extension.org/ext\">MyExtensionElementValue</myext:MyExtensionElement>";

  private static String xmlInterfaceName = "<wsam:InterfaceName xmlns:wsam=\"http://www.w3.org/2007/05/addressing/metadata\" xmlns:wsns=\"http://helloservice.org/wsdl\">wsns:Hello</wsam:InterfaceName>";

  private static String xmlServiceName = "<wsam:ServiceName xmlns:wsam=\"http://www.w3.org/2007/05/addressing/metadata\" xmlns:wsa=\"http://www.w3.org/2005/08/addressing\" xmlns:wsns=\"http://helloservice.org/wsdl\" EndpointName=\"HelloPort\">wsns:HelloService</wsam:ServiceName>";

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private static final String ENDPOINT_URL = "dlhelloservice.endpoint.1";

  private static final String WSDLLOC_URL = "dlhelloservice.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  private Hello port = null;

  W3CEndpointReferenceBuilder builder = null;

  static HelloService service = null;

  private void getPorts() throws Exception {
    TestUtil.logMsg("Get port  = " + PORT_NAME);
    port = (Hello) service.getPort(Hello.class);
    TestUtil.logMsg("port=" + port);
  }

  private void getPortsStandalone() throws Exception {
    getPorts();
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortsJavaEE() throws Exception {
    TestUtil.logMsg("Obtaining service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    getPorts();
    TestUtil.logMsg("Get Target Endpoint Address for port=" + port);
    String url = JAXWS_Util.getTargetEndpointAddress(port);
    TestUtil.logMsg("Target Endpoint Address=" + url);
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
        TestUtil.logMsg("Create Service object");
        service = (HelloService) JAXWS_Util.getService(wsdlurl, SERVICE_QNAME,
            SERVICE_CLASS);
        getPortsStandalone();
      } else {
        getTestURLs();
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (HelloService) getSharedObject();
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
    builder = new W3CEndpointReferenceBuilder();
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: W3CEndpointReferenceBuilderConstructorTest
   *
   * @assertion_ids: JAXWS:JAVADOC:204;
   *
   * @test_Strategy: Create instance via W3CEndpointReferenceBuilder()
   * constructor. Verify W3CEndpointReferenceBuilder object created
   * successfully.
   */
  public void W3CEndpointReferenceBuilderConstructorTest() throws Fault {
    TestUtil.logTrace("W3CEndpointReferenceBuilderConstructorTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Create instance via W3CEndpointReferenceBuilder() ...");
      W3CEndpointReferenceBuilder b = new W3CEndpointReferenceBuilder();
      if (b != null) {
        TestUtil
            .logMsg("W3CEndpointReferenceBuilder object created successfully");
      } else {
        TestUtil.logErr("W3CEndpointReferenceBuilder object not created");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("W3CEndpointReferenceBuilderConstructorTest failed", e);
    }

    if (!pass)
      throw new Fault("W3CEndpointReferenceBuilderConstructorTest failed");
  }

  /*
   * @testName: addressNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:198;
   *
   * @test_Strategy: Call address() api.
   * 
   */
  public void addressNULLTest() throws Fault {
    TestUtil.logTrace("addressNULLTest");
    boolean pass = true;
    try {
      builder = builder.address(null);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("addressNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("addressNULLTest failed");
  }

  /*
   * @testName: addressNonNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:198;
   *
   * @test_Strategy: Call address() api.
   * 
   */
  public void addressNonNULLTest() throws Fault {
    TestUtil.logTrace("addressNonNULLTest");
    boolean pass = true;
    try {
      builder = builder.address(url);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("addressNonNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("addressNonNULLTest failed");
  }

  /*
   * @testName: serviceNameNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:203;
   *
   * @test_Strategy: Call serviceName() api.
   * 
   */
  public void serviceNameNULLTest() throws Fault {
    TestUtil.logTrace("serviceNameNULLTest");
    boolean pass = true;
    try {
      builder = builder.serviceName(null);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("serviceNameNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("serviceNameNULLTest failed");
  }

  /*
   * @testName: serviceNameNonNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:203;
   *
   * @test_Strategy: Call serviceName() api.
   * 
   */
  public void serviceNameNonNULLTest() throws Fault {
    TestUtil.logTrace("serviceNameNonNULLTest");
    boolean pass = true;
    try {
      builder = builder.serviceName(SERVICE_QNAME);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("serviceNameNonNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("serviceNameNonNULLTest failed");
  }

  /*
   * @testName: interfaceNameNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:280;
   *
   * @test_Strategy: Call interfaceName() api.
   * 
   */
  public void interfaceNameNULLTest() throws Fault {
    TestUtil.logTrace("interfaceNameNULLTest");
    boolean pass = true;
    try {
      builder = builder.interfaceName(null);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("interfaceNameNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("interfaceNameNULLTest failed");
  }

  /*
   * @testName: interfaceNameNonNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:280;
   *
   * @test_Strategy: Call interfaceName() api.
   * 
   */
  public void interfaceNameNonNULLTest() throws Fault {
    TestUtil.logTrace("interfaceNameNonNULLTest");
    boolean pass = true;
    try {
      builder = builder.interfaceName(PORT_TYPE_QNAME);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("interfaceNameNonNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("interfaceNameNonNULLTest failed");
  }

  /*
   * @testName: endpointNameNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:200;
   *
   * @test_Strategy: Call endpointName() api.
   * 
   */
  public void endpointNameNULLTest() throws Fault {
    TestUtil.logTrace("endpointNameNULLTest");
    boolean pass = true;
    try {
      builder = builder.serviceName(SERVICE_QNAME);
      builder = builder.endpointName(null);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("endpointNameNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("endpointNameNULLTest failed");
  }

  /*
   * @testName: endpointNameNonNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:200;
   *
   * @test_Strategy: Call endpointName() api.
   * 
   */
  public void endpointNameNonNULLTest() throws Fault {
    TestUtil.logTrace("endpointNameNonNULLTest");
    boolean pass = true;
    try {
      builder = builder.serviceName(SERVICE_QNAME);
      builder = builder.endpointName(PORT_QNAME);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("endpointNameNonNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("endpointNameNonNULLTest failed");
  }

  /*
   * @testName: endpointNameIllegalStateExceptionTest
   *
   * @assertion_ids: JAXWS:JAVADOC:200;
   *
   * @test_Strategy: Call endpointName() api. Test for IllegalStateException.
   * 
   */
  public void endpointNameIllegalStateExceptionTest() throws Fault {
    TestUtil.logTrace("endpointNameIllegalStateExceptionTest");
    boolean pass = true;
    try {
      builder = builder.endpointName(PORT_QNAME);
      TestUtil.logErr("Did not throw expected IllegalStateException");
      pass = false;
    } catch (IllegalStateException e) {
      TestUtil.logMsg("Caught expected IllegalStateException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("endpointNameIllegalStateExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("endpointNameIllegalStateExceptionTest failed");
  }

  /*
   * @testName: wsdlDocumentLocationNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:205;
   *
   * @test_Strategy: Call wsdlDocumentLocation() api.
   * 
   */
  public void wsdlDocumentLocationNULLTest() throws Fault {
    TestUtil.logTrace("wsdlDocumentLocationNULLTest");
    boolean pass = true;
    try {
      builder = builder.wsdlDocumentLocation(null);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("wsdlDocumentLocationNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("wsdlDocumentLocationNULLTest failed");
  }

  /*
   * @testName: wsdlDocumentLocationNonNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:205;
   *
   * @test_Strategy: Call wsdlDocumentLocation() api.
   * 
   */
  public void wsdlDocumentLocationNonNULLTest() throws Fault {
    TestUtil.logTrace("wsdlDocumentLocationNonNULLTest");
    boolean pass = true;
    try {
      builder = builder.wsdlDocumentLocation(wsdlurl.toString());
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("wsdlDocumentLocationNonNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("wsdlDocumentLocationNonNULLTest failed");
  }

  /*
   * @testName: metadataNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:201;
   *
   * @test_Strategy: Call metadata() api. Test for IllegalArgumentException.
   * 
   */
  public void metadataNULLTest() throws Fault {
    TestUtil.logTrace("metadataNULLTest");
    boolean pass = true;
    try {
      builder = builder.metadata(null);
      TestUtil.logErr("Passing NULL metadata should have thrown exception");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught expected IllegalArgumentException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("metadataNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("metadataNULLTest failed");
  }

  /*
   * @testName: metadataNonNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:201;
   *
   * @test_Strategy: Call metadata() api.
   * 
   */
  public void metadataNonNULLTest() throws Fault {
    TestUtil.logTrace("metadataNonNULLTest");
    boolean pass = true;
    try {
      DOMSource domsrc = (DOMSource) JAXWS_Util.makeSource(xmlServiceName,
          "DOMSource");
      Document document = (Document) domsrc.getNode();
      XMLUtils.xmlDumpDOMNodes(document, false);
      builder = builder.metadata(document.getDocumentElement());
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("metadataNonNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("metadataNonNULLTest failed");
  }

  /*
   * @testName: attributeNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:278;
   *
   * @test_Strategy: Call attribute() api. Test for IllegalArgumentException.
   * 
   */
  public void attributeNULLTest() throws Fault {
    TestUtil.logTrace("attributeNULLTest");
    boolean pass = true;
    try {
      builder = builder.attribute(null, null);
      TestUtil.logErr("Passing NULL should have thrown exception");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught expected IllegalArgumentException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("attributeNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("attributeNULLTest failed");
  }

  /*
   * @testName: attributeNonNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:278;
   *
   * @test_Strategy: Call attribute() api.
   * 
   */
  public void attributeNonNULLTest() throws Fault {
    TestUtil.logTrace("attributeNonNULLTest");
    boolean pass = true;
    try {
      builder = builder.attribute(MyExtensionAttr, MyExtensionAttrValue);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("attributeNonNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("attributeNonNULLTest failed");
  }

  /*
   * @testName: elementNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:279;
   *
   * @test_Strategy: Call element() api. Test for IllegalArgumentException.
   * 
   */
  public void elementNULLTest() throws Fault {
    TestUtil.logTrace("elementNULLTest");
    boolean pass = true;
    try {
      builder = builder.element(null);
      TestUtil.logErr("Passing NULL should have thrown exception");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught expected IllegalArgumentException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("elementNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("elementNULLTest failed");
  }

  /*
   * @testName: elementNonNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:279;
   *
   * @test_Strategy: Call element() api.
   * 
   */
  public void elementNonNULLTest() throws Fault {
    TestUtil.logTrace("elementNonNULLTest");
    boolean pass = true;
    try {
      DOMSource domsrc = (DOMSource) JAXWS_Util
          .makeSource(xmlMyExtensionElement, "DOMSource");
      Document document = (Document) domsrc.getNode();
      builder = builder.element(document.getDocumentElement());
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("elementNonNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("elementNonNULLTest failed");
  }

  /*
   * @testName: referenceParameterNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:202;
   *
   * @test_Strategy: Call referenceParameter() api. Test for
   * IllegalArgumentException.
   * 
   */
  public void referenceParameterNULLTest() throws Fault {
    TestUtil.logTrace("referenceParameterNULLTest");
    boolean pass = true;
    try {
      builder = builder.referenceParameter(null);
      TestUtil.logErr(
          "Passing NULL referenceParameter should have thrown exception");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught expected IllegalArgumentException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("referenceParameterNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("referenceParameterNULLTest failed");
  }

  /*
   * @testName: referenceParameterNonNULLTest
   *
   * @assertion_ids: JAXWS:JAVADOC:202;
   *
   * @test_Strategy: Call referenceParameter() api.
   * 
   */
  public void referenceParameterNonNULLTest() throws Fault {
    TestUtil.logTrace("referenceParameterNonNULLTest");
    boolean pass = true;
    try {
      DOMSource domsrc = (DOMSource) JAXWS_Util.makeSource(xmlRefParam1,
          "DOMSource");
      Document document = (Document) domsrc.getNode();
      builder = builder.referenceParameter(document.getDocumentElement());
      domsrc = (DOMSource) JAXWS_Util.makeSource(xmlRefParam2, "DOMSource");
      document = (Document) domsrc.getNode();
      builder = builder.referenceParameter(document.getDocumentElement());
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("referenceParameterNonNULLTest failed", e);
    }

    if (!pass)
      throw new Fault("referenceParameterNonNULLTest failed");
  }

  /*
   * @testName: buildTest1
   *
   * @assertion_ids: JAXWS:JAVADOC:206;
   *
   * @test_Strategy: Call build() api. Use calls to address() and metadata() to
   * build the EndpointReference.
   * 
   */
  public void buildTest1() throws Fault {
    TestUtil.logTrace("buildTest1");
    boolean pass = true;
    try {
      builder = builder.address(url);
      DOMSource domsrc = (DOMSource) JAXWS_Util.makeSource(xmlInterfaceName,
          "DOMSource");
      Document document = (Document) domsrc.getNode();
      builder = builder.metadata(document.getDocumentElement());
      domsrc = (DOMSource) JAXWS_Util.makeSource(xmlServiceName, "DOMSource");
      document = (Document) domsrc.getNode();
      builder = builder.metadata(document.getDocumentElement());
      W3CEndpointReference epr = builder.build();
      DOMResult dr = new DOMResult();
      epr.writeTo(dr);
      XMLUtils.xmlDumpDOMNodes(dr.getNode(), false);
      if (!EprUtil.validateEPR(epr, url, SERVICE_QNAME, PORT_QNAME,
          PORT_TYPE_QNAME, Boolean.TRUE))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("buildTest1 failed", e);
    }

    if (!pass)
      throw new Fault("buildTest1 failed");
  }

  /*
   * @testName: buildTest2
   *
   * @assertion_ids: JAXWS:JAVADOC:206;
   *
   * @test_Strategy: Call build() api. Use calls to address(), metadata(),
   * element() and attribute() and to build the EndpointReference.
   * 
   */
  public void buildTest2() throws Fault {
    TestUtil.logTrace("buildTest2");
    boolean pass = true;
    try {
      builder = builder.address(url);
      builder = builder.attribute(MyExtensionAttr, MyExtensionAttrValue);
      DOMSource domsrc = (DOMSource) JAXWS_Util
          .makeSource(xmlMyExtensionElement, "DOMSource");
      Document document = (Document) domsrc.getNode();
      builder = builder.element(document.getDocumentElement());
      domsrc = (DOMSource) JAXWS_Util.makeSource(xmlInterfaceName, "DOMSource");
      document = (Document) domsrc.getNode();
      builder = builder.metadata(document.getDocumentElement());
      domsrc = (DOMSource) JAXWS_Util.makeSource(xmlServiceName, "DOMSource");
      document = (Document) domsrc.getNode();
      builder = builder.metadata(document.getDocumentElement());
      W3CEndpointReference epr = builder.build();
      DOMResult dr = new DOMResult();
      epr.writeTo(dr);
      XMLUtils.xmlDumpDOMNodes(dr.getNode(), false);
      if (!EprUtil.validateEPR(epr, url, SERVICE_QNAME, PORT_QNAME,
          PORT_TYPE_QNAME, Boolean.TRUE))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("buildTest2 failed", e);
    }

    if (!pass)
      throw new Fault("buildTest2 failed");
  }

  /*
   * @testName: buildTest3
   *
   * @assertion_ids: JAXWS:JAVADOC:206;
   *
   * @test_Strategy: Call build() api. Call all the api's to build an
   * EndpointReference from scratch.
   * 
   */
  public void buildTest3() throws Fault {
    TestUtil.logTrace("buildTest3");
    boolean pass = true;
    try {
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
      DOMResult dr = new DOMResult();
      epr.writeTo(dr);
      XMLUtils.xmlDumpDOMNodes(dr.getNode(), false);
      if (!EprUtil.validateEPR(epr, url, SERVICE_QNAME, PORT_QNAME,
          PORT_TYPE_QNAME, Boolean.TRUE, wsdlurl.toString()))
        pass = false;
      if (!EprUtil.validateReferenceParameter(dr.getNode(), "MyParam1",
          "Hello"))
        pass = false;
      if (!EprUtil.validateReferenceParameter(dr.getNode(), "MyParam2",
          "There"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("buildTest3 failed", e);
    }

    if (!pass)
      throw new Fault("buildTest3 failed");
  }

  /*
   * @testName: buildTest4
   *
   * @assertion_ids: JAXWS:JAVADOC:206;
   *
   * @test_Strategy: Call build() api. Call all the api's to build an
   * EndpointReference from scratch.
   * 
   */
  public void buildTest4() throws Fault {
    TestUtil.logTrace("buildTest4");
    boolean pass = true;
    try {
      builder = builder.address(url);
      builder = builder.serviceName(SERVICE_QNAME);
      builder = builder.endpointName(PORT_QNAME);
      builder = builder.interfaceName(PORT_TYPE_QNAME);
      builder = builder.wsdlDocumentLocation(wsdlurl.toString());
      DOMSource domsrc = (DOMSource) JAXWS_Util.makeSource(xmlRefParam1,
          "DOMSource");
      Document document = (Document) domsrc.getNode();
      builder = builder.referenceParameter(document.getDocumentElement());
      domsrc = (DOMSource) JAXWS_Util.makeSource(xmlRefParam2, "DOMSource");
      document = (Document) domsrc.getNode();
      builder = builder.referenceParameter(document.getDocumentElement());
      W3CEndpointReference epr = builder.build();
      DOMResult dr = new DOMResult();
      epr.writeTo(dr);
      XMLUtils.xmlDumpDOMNodes(dr.getNode(), false);
      if (!EprUtil.validateEPR(epr, url, SERVICE_QNAME, PORT_QNAME,
          PORT_TYPE_QNAME, Boolean.TRUE, wsdlurl.toString()))
        pass = false;
      if (!EprUtil.validateReferenceParameter(dr.getNode(), "MyParam1",
          "Hello"))
        pass = false;
      if (!EprUtil.validateReferenceParameter(dr.getNode(), "MyParam2",
          "There"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("buildTest4 failed", e);
    }

    if (!pass)
      throw new Fault("buildTest4 failed");
  }

  /*
   * @testName: buildIllegalStateExceptionTest1
   *
   * @assertion_ids: JAXWS:JAVADOC:206;
   *
   * @test_Strategy: Call build() api. Test for IllegalStateException.
   * 
   */
  public void buildIllegalStateExceptionTest1() throws Fault {
    TestUtil.logTrace("buildIllegalStateExceptionTest1");
    boolean pass = true;
    try {
      W3CEndpointReference epr = builder.build();
      TestUtil.logErr("Did not throw expected IllegalStateException");
      pass = false;
    } catch (IllegalStateException e) {
      TestUtil.logMsg("Caught expected IllegalStateException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("buildIllegalStateExceptionTest1 failed", e);
    }

    if (!pass)
      throw new Fault("buildIllegalStateExceptionTest1 failed");
  }

  /*
   * @testName: buildIllegalStateExceptionTest2
   *
   * @assertion_ids: JAXWS:JAVADOC:206;
   *
   * @test_Strategy: Call build() api. Test for IllegalStateException.
   * 
   */
  public void buildIllegalStateExceptionTest2() throws Fault {
    TestUtil.logTrace("buildIllegalStateExceptionTest2");
    boolean pass = true;
    try {
      builder = builder.endpointName(PORT_QNAME);
      W3CEndpointReference epr = builder.build();
      TestUtil.logErr("Did not throw expected IllegalStateException");
      pass = false;
    } catch (IllegalStateException e) {
      TestUtil.logMsg("Caught expected IllegalStateException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("buildIllegalStateExceptionTest2 failed", e);
    }

    if (!pass)
      throw new Fault("buildIllegalStateExceptionTest2 failed");
  }

  /*
   * @testName: buildIllegalStateExceptionTest3
   *
   * @assertion_ids: JAXWS:JAVADOC:206;
   *
   * @test_Strategy: Call build() api. Test for IllegalStateException.
   * 
   */
  public void buildIllegalStateExceptionTest3() throws Fault {
    TestUtil.logTrace("buildIllegalStateExceptionTest3");
    boolean pass = true;
    try {
      builder = builder.wsdlDocumentLocation("http://bogus.org/bogus");
      W3CEndpointReference epr = builder.build();
      TestUtil.logErr("Did not throw expected IllegalStateException");
      pass = false;
    } catch (IllegalStateException e) {
      TestUtil.logMsg("Caught expected IllegalStateException");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("buildIllegalStateExceptionTest3 failed", e);
    }

    if (!pass)
      throw new Fault("buildIllegalStateExceptionTest3 failed");
  }
}
