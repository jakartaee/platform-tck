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
 * $Id$
 */

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws.Provider;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.sharedclients.dlhelloproviderclient.*;

import java.rmi.*;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.net.*;
import java.util.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.ws.*;
import jakarta.xml.ws.soap.*;
import jakarta.xml.soap.*;
import javax.xml.transform.*;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

public class Client extends ServiceEETest {
  // need to create jaxbContext
  private static final ObjectFactory of = new ObjectFactory();

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String SHARED_CLIENT_PKG = "com.sun.ts.tests.jaxws.sharedclients.dlhelloproviderclient.";

  private static final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloService";

  private static final String PORT_NAME = "HelloPort";

  private static final String PORT_NAME2 = "Hello2Port";

  private static final String PORT_NAME3 = "Hello3Port";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private QName PORT_QNAME2 = new QName(NAMESPACEURI, PORT_NAME2);

  private QName PORT_QNAME3 = new QName(NAMESPACEURI, PORT_NAME3);

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "dlhelloproviderservice.endpoint.1";

  private static final String ENDPOINT_URL2 = "dlhelloproviderservice.endpoint.2";

  private static final String ENDPOINT_URL3 = "dlhelloproviderservice.endpoint.3";

  private static final String WSDLLOC_URL = "dlhelloproviderservice.wsdlloc.1";

  private String url = null;

  private String url2 = null;

  private String url3 = null;

  private URL wsdlurl = null;

  private Dispatch<Object> dispatchJaxb = null;

  private Dispatch<SOAPMessage> dispatchSM = null;

  private Dispatch<Source> dispatchSrc = null;

  String bindingID = null;

  private static final Class JAXB_OBJECT_FACTORY = com.sun.ts.tests.jaxws.sharedclients.dlhelloproviderclient.ObjectFactory.class;

  private static final Class SERVICE_CLASS = com.sun.ts.tests.jaxws.sharedclients.dlhelloproviderclient.HelloService.class;

  static HelloService service = null;

  private JAXBContext createJAXBContext() {
    try {
      return JAXBContext.newInstance(JAXB_OBJECT_FACTORY);
    } catch (jakarta.xml.bind.JAXBException e) {
      throw new WebServiceException(e.getMessage(), e);
    }
  }

  private Dispatch<Object> createDispatchJAXB() throws Exception {
    return service.createDispatch(PORT_QNAME, createJAXBContext(),
        jakarta.xml.ws.Service.Mode.PAYLOAD);
  }

  private Dispatch<SOAPMessage> createDispatchSOAPMessage() throws Exception {
    return service.createDispatch(PORT_QNAME, SOAPMessage.class,
        jakarta.xml.ws.Service.Mode.MESSAGE);
  }

  private Dispatch<Source> createDispatchSourcePayLoad() throws Exception {
    return service.createDispatch(PORT_QNAME, Source.class,
        jakarta.xml.ws.Service.Mode.PAYLOAD);
  }

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(ENDPOINT_URL2);
    url2 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(ENDPOINT_URL3);
    url3 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("Service Endpoint URL2: " + url2);
    TestUtil.logMsg("Service Endpoint URL3: " + url3);
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
        TestUtil.logMsg("Create Service object");
        getTestURLs();
        service = (HelloService) JAXWS_Util.getService(wsdlurl, SERVICE_QNAME,
            SERVICE_CLASS);
      } else {
        getTestURLs();
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (HelloService) getSharedObject();
      }

      bindingID = new String(SOAPBinding.SOAP11HTTP_BINDING);

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
    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  /*
   * @testName: invokeTest1
   *
   * @assertion_ids: JAXWS:SPEC:7002; JAXWS:JAVADOC:38; JAXWS:SPEC:7008;
   * WS4EE:SPEC:5001; JAXWS:JAVADOC:78; JAXWS:JAVADOC:79; JAXWS:JAVADOC:80;
   * JAXWS:JAVADOC:81; JAXWS:SPEC:5000; JAXWS:SPEC:5001; JAXWS:SPEC:5002;
   * JAXWS:SPEC:5003; JAXWS:SPEC:7000; JAXWS:SPEC:7009; JAXWS:SPEC:6003;
   * JAXWS:JAVADOC:117; JAXWS:JAVADOC:7; WS4EE:SPEC:5005; JAXWS:SPEC:6001;
   * JAXWS:SPEC:7012;
   *
   * @test_Strategy:
   */
  public void invokeTest1() throws Fault {
    TestUtil.logTrace("invokeTest1");
    boolean pass = true;
    HelloRequest helloReq = null;
    try {
      helloReq = of.createHelloRequest();
      helloReq.setArgument("sendSource");
    } catch (Exception e) {
      e.printStackTrace();
    }
    HelloResponse helloRes = null;
    try {
      dispatchJaxb = createDispatchJAXB();
      helloRes = (HelloResponse) dispatchJaxb.invoke(helloReq);
      TestUtil.logMsg("HelloRequest=" + helloReq.getArgument());
      TestUtil.logMsg("HelloResponse=" + helloRes.getArgument());
      if (!helloRes.getArgument().equals(helloReq.getArgument()))
        pass = false;
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeTest1 failed");
  }

  /*
   * @testName: invokeTest2
   *
   * @assertion_ids: JAXWS:SPEC:7002; JAXWS:JAVADOC:38; JAXWS:SPEC:7008;
   * WS4EE:SPEC:5001; JAXWS:JAVADOC:78; JAXWS:JAVADOC:79; JAXWS:JAVADOC:80;
   * JAXWS:JAVADOC:81; JAXWS:SPEC:5000; JAXWS:SPEC:5001; JAXWS:SPEC:5002;
   * JAXWS:SPEC:5003; JAXWS:SPEC:7000; JAXWS:SPEC:7009; JAXWS:SPEC:6003;
   * JAXWS:JAVADOC:117; JAXWS:JAVADOC:7; WS4EE:SPEC:5005; JAXWS:SPEC:6001;
   * JAXWS:SPEC:7012;
   *
   * @test_Strategy:
   */
  public void invokeTest2() throws Fault {
    TestUtil.logTrace("invokeTest2");
    boolean pass = true;
    HelloRequest helloReq = null;
    try {
      helloReq = of.createHelloRequest();
      helloReq.setArgument("sendBean");
    } catch (Exception e) {
      e.printStackTrace();
    }
    HelloResponse helloRes = null;
    try {
      // Using generic Service object
      jakarta.xml.ws.Service service = jakarta.xml.ws.Service.create(SERVICE_QNAME);
      service.addPort(PORT_QNAME, bindingID, url);
      dispatchJaxb = service.createDispatch(PORT_QNAME, createJAXBContext(),
          jakarta.xml.ws.Service.Mode.PAYLOAD);
      helloRes = (HelloResponse) dispatchJaxb.invoke(helloReq);
      TestUtil.logMsg("HelloRequest=" + helloReq.getArgument());
      TestUtil.logMsg("HelloResponse=" + helloRes.getArgument());
      if (!helloRes.getArgument().equals(helloReq.getArgument()))
        pass = false;
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeTest2 failed");
  }

  /*
   * @testName: invokeTest3
   *
   * @assertion_ids: JAXWS:SPEC:7002; JAXWS:JAVADOC:38; JAXWS:SPEC:7008;
   * WS4EE:SPEC:5001; JAXWS:JAVADOC:78; JAXWS:JAVADOC:79; JAXWS:JAVADOC:80;
   * JAXWS:JAVADOC:81; JAXWS:SPEC:5000; JAXWS:SPEC:5001; JAXWS:SPEC:5002;
   * JAXWS:SPEC:5003; JAXWS:SPEC:7000; JAXWS:SPEC:7009; JAXWS:SPEC:6003;
   * JAXWS:JAVADOC:117; JAXWS:JAVADOC:7; WS4EE:SPEC:5005; JAXWS:SPEC:6001;
   * JAXWS:SPEC:7012;
   *
   * @test_Strategy: do an invoke with the payload set to payload and a source
   * being sent over the wire
   */
  public void invokeTest3() throws Fault {
    TestUtil.logTrace("invokeTest3");
    boolean pass = true;
    HelloRequest helloReq = null;
    try {
      helloReq = of.createHelloRequest();
      helloReq.setArgument("sendBean");
    } catch (Exception e) {
      e.printStackTrace();
    }
    HelloResponse helloRes = null;
    try {
      // Using generic Service object
      jakarta.xml.ws.Service service = jakarta.xml.ws.Service.create(SERVICE_QNAME);
      service.addPort(PORT_QNAME2, bindingID, url2);
      dispatchJaxb = service.createDispatch(PORT_QNAME2, createJAXBContext(),
          jakarta.xml.ws.Service.Mode.PAYLOAD);
      helloRes = (HelloResponse) dispatchJaxb.invoke(helloReq);
      TestUtil.logMsg("HelloRequest=" + helloReq.getArgument());
      TestUtil.logMsg("HelloResponse=" + helloRes.getArgument());
      if (!helloRes.getArgument().equals("responseBean")) {
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeTest3 failed");
  }

  /*
   * @testName: invokeTest4
   *
   * @assertion_ids: JAXWS:SPEC:7002; JAXWS:JAVADOC:38; JAXWS:SPEC:7008;
   * WS4EE:SPEC:5001; JAXWS:JAVADOC:78; JAXWS:JAVADOC:79; JAXWS:JAVADOC:80;
   * JAXWS:JAVADOC:81; JAXWS:SPEC:5000; JAXWS:SPEC:5001; JAXWS:SPEC:5002;
   * JAXWS:SPEC:5003; JAXWS:SPEC:7000; JAXWS:SPEC:7009; JAXWS:SPEC:6003;
   * JAXWS:JAVADOC:117; JAXWS:JAVADOC:7; WS4EE:SPEC:5005; JAXWS:SPEC:6001;
   * JAXWS:SPEC:7012;
   *
   * @test_Strategy: do an invoke with the payload set to message and a
   * SOAPMessage being sent over the wire
   */
  public void invokeTest4() throws Fault {
    TestUtil.logTrace("invokeTest4");
    boolean pass = true;
    HelloResponse helloRes = null;
    try {
      // Using generic Service object
      jakarta.xml.ws.Service service = jakarta.xml.ws.Service.create(SERVICE_QNAME);
      service.addPort(PORT_QNAME2, bindingID, url2);
      dispatchSM = service.createDispatch(PORT_QNAME2, SOAPMessage.class,
          jakarta.xml.ws.Service.Mode.MESSAGE);

      String helloReq = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><HelloRequest xmlns=\"http://helloservice.org/types\"><argument>sendBean</argument></HelloRequest></soapenv:Body></soapenv:Envelope>";

      SOAPMessage reqMsg = JAXWS_Util.makeSOAPMessage(helloReq);
      TestUtil.logTrace("sending the following SOAPMessage:");
      JAXWS_Util.dumpSOAPMessage(reqMsg);

      SOAPMessage respMsg = (SOAPMessage) dispatchSM.invoke(reqMsg);
      TestUtil.logTrace("The following SOAPMessage was received:");
      JAXWS_Util.dumpSOAPMessage(respMsg);

      String resStr = JAXWS_Util.getSOAPMessageAsString(respMsg);
      if (resStr.indexOf("responseBean") == -1) {
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeTest4 failed");
  }

  /*
   * @testName: invokeTest5
   *
   * @assertion_ids: JAXWS:SPEC:7002; JAXWS:JAVADOC:38; JAXWS:SPEC:7008;
   * WS4EE:SPEC:5001; JAXWS:JAVADOC:78; JAXWS:JAVADOC:79; JAXWS:JAVADOC:80;
   * JAXWS:JAVADOC:81; JAXWS:SPEC:5000; JAXWS:SPEC:5001; JAXWS:SPEC:5002;
   * JAXWS:SPEC:5003; JAXWS:SPEC:7000; JAXWS:SPEC:7009; JAXWS:SPEC:6003;
   * JAXWS:JAVADOC:117; JAXWS:JAVADOC:7; WS4EE:SPEC:5005; JAXWS:SPEC:6001;
   * JAXWS:SPEC:7012;
   *
   * @test_Strategy: do an invoke with the payload set to payload and a Source
   * being sent over the wire
   */
  public void invokeTest5() throws Fault {
    TestUtil.logTrace("invokeTest5");
    boolean pass = true;
    HelloResponse helloRes = null;
    try {
      // Using generic Service object
      jakarta.xml.ws.Service service = jakarta.xml.ws.Service.create(SERVICE_QNAME);
      service.addPort(PORT_QNAME3, bindingID, url3);
      dispatchSrc = service.createDispatch(PORT_QNAME3, Source.class,
          jakarta.xml.ws.Service.Mode.PAYLOAD);

      String helloReq = "<HelloRequest xmlns=\"http://helloservice.org/types\"><argument>sendSource</argument></HelloRequest>";

      Source reqMsg = JAXWS_Util.makeSource(helloReq, "StreamSource");

      Source ds = dispatchSrc.invoke(reqMsg);
      TestUtil.logMsg("ds=" + ds);
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeTest5 failed");
  }

  /*
   * @testName: invokeTest6
   *
   * @assertion_ids: JAXWS:SPEC:7002; JAXWS:JAVADOC:38; JAXWS:SPEC:7008;
   * WS4EE:SPEC:6001; JAXWS:JAVADOC:78; JAXWS:JAVADOC:79; JAXWS:JAVADOC:80;
   * JAXWS:JAVADOC:81; JAXWS:SPEC:6000; JAXWS:SPEC:6001; JAXWS:SPEC:6002;
   * JAXWS:SPEC:6003; JAXWS:SPEC:7000; JAXWS:SPEC:7009; JAXWS:SPEC:6003;
   * JAXWS:JAVADOC:117; JAXWS:JAVADOC:7; WS4EE:SPEC:6006; JAXWS:SPEC:6001;
   * JAXWS:SPEC:7012; JAXWS:SPEC:5000;
   *
   * @test_Strategy: Test provider endpoint using JAXB request object which
   * sends Empty Payload response back
   */
  public void invokeTest6() throws Fault {
    TestUtil.logTrace("invokeTest6");
    boolean pass = true;
    HelloRequest helloReq = null;
    try {
      helloReq = of.createHelloRequest();
      helloReq.setArgument("sendEmptyStreamSource");
    } catch (Exception e) {
      e.printStackTrace();
    }
    Object o = null;
    try {
      dispatchJaxb = createDispatchJAXB();
      o = dispatchJaxb.invoke(helloReq);
      TestUtil.logMsg("o=" + o);
      if (o != null)
        pass = false;
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeTest6 failed");
  }

  /*
   * @testName: invokeTest7
   *
   * @assertion_ids: JAXWS:SPEC:7002; JAXWS:JAVADOC:38; JAXWS:SPEC:7008;
   * WS4EE:SPEC:6001; JAXWS:JAVADOC:78; JAXWS:JAVADOC:79; JAXWS:JAVADOC:80;
   * JAXWS:JAVADOC:81; JAXWS:SPEC:6000; JAXWS:SPEC:6001; JAXWS:SPEC:6002;
   * JAXWS:SPEC:6003; JAXWS:SPEC:7000; JAXWS:SPEC:7009; JAXWS:SPEC:6003;
   * JAXWS:JAVADOC:117; JAXWS:JAVADOC:7; WS4EE:SPEC:6006; JAXWS:SPEC:6001;
   * JAXWS:SPEC:7012; JAXWS:SPEC:5000;
   *
   * @test_Strategy: Test provider endpoint using Source request object and
   * sends Empty Payload response back as an Empty StreamSource, DOMSource or
   * SAXSource.
   */
  public void invokeTest7() throws Fault {
    TestUtil.logTrace("invokeTest7");
    boolean pass = true;
    String[] helloReq = {
        "<HelloRequest xmlns=\"http://helloservice.org/types\"><argument>sendEmptyStreamSource</argument></HelloRequest>",
        "<HelloRequest xmlns=\"http://helloservice.org/types\"><argument>sendEmptyDOMSource</argument></HelloRequest>",
        "<HelloRequest xmlns=\"http://helloservice.org/types\"><argument>sendEmptySAXSource</argument></HelloRequest>" };
    String[] streamAs = { "StreamSource", "DOMSource", "SAXSource" };
    try {
      // Using generic Service object
      jakarta.xml.ws.Service service = jakarta.xml.ws.Service.create(SERVICE_QNAME);
      service.addPort(PORT_QNAME3, bindingID, url3);
      dispatchSrc = service.createDispatch(PORT_QNAME3, Source.class,
          jakarta.xml.ws.Service.Mode.PAYLOAD);
      for (int i = 0; i < streamAs.length; i++) {
        TestUtil.logMsg("Send request as (" + streamAs[i]
            + ") and check for empty payload response as a " + streamAs[i]);
        Source reqMsg = JAXWS_Util.makeSource(helloReq[i], streamAs[i]);
        Source ds = dispatchSrc.invoke(reqMsg);
        TestUtil.logMsg("ds=" + ds);
        if (ds != null)
          pass = false;
      }
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeTest7 failed");
  }

  /*
   * @testName: invokeTest8
   *
   * @assertion_ids: JAXWS:SPEC:7002; JAXWS:JAVADOC:38; JAXWS:SPEC:7008;
   * WS4EE:SPEC:6001; JAXWS:JAVADOC:78; JAXWS:JAVADOC:79; JAXWS:JAVADOC:80;
   * JAXWS:JAVADOC:81; JAXWS:SPEC:6000; JAXWS:SPEC:6001; JAXWS:SPEC:6002;
   * JAXWS:SPEC:6003; JAXWS:SPEC:7000; JAXWS:SPEC:7009; JAXWS:SPEC:6003;
   * JAXWS:JAVADOC:117; JAXWS:JAVADOC:7; WS4EE:SPEC:6006; JAXWS:SPEC:6001;
   * JAXWS:SPEC:7012; JAXWS:SPEC:5000;
   *
   * @test_Strategy: Test provider endpoint using JAXB request object for oneway
   * op and sends Empty Payload null back
   */
  public void invokeTest8() throws Fault {
    TestUtil.logTrace("invokeTest8");
    boolean pass = true;
    HelloOneWayRequest helloOneWayReq = null;
    try {
      helloOneWayReq = of.createHelloOneWayRequest();
      helloOneWayReq.setArgument("sendEmptyStreamSource");
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      dispatchJaxb = createDispatchJAXB();
      dispatchJaxb.invokeOneWay(helloOneWayReq);
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeTest8 failed");
  }
}
