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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws.Dispatch;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.rmi.*;

import jakarta.xml.ws.*;
import javax.xml.namespace.QName;
import jakarta.xml.ws.soap.*;
import jakarta.xml.ws.handler.*;
import jakarta.xml.bind.*;
import jakarta.xml.soap.*;

import javax.naming.InitialContext;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import javax.xml.transform.Source;
import java.net.*;
import java.util.*;

public class Client extends ServiceEETest {
  // need to create jaxbContext
  private static final ObjectFactory of = new ObjectFactory();

  // Messages sent as straight XML as Source objects
  private String helloReq = "<HelloRequest xmlns=\"http://helloservice.org/types\"><argument>foo</argument></HelloRequest>";

  private String helloOneWayReq = "<HelloOneWayRequest xmlns=\"http://helloservice.org/types\"><argument>foo</argument></HelloOneWayRequest>";

  // Messages sent as SOAPMessage objects
  private String helloReqSM = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><HelloRequest xmlns=\"http://helloservice.org/types\"><argument>foo</argument></HelloRequest></soapenv:Body></soapenv:Envelope>";

  private String helloOneWayReqSM = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\"><soapenv:Body><HelloOneWayRequest xmlns=\"http://helloservice.org/types\"><argument>foo</argument></HelloOneWayRequest></soapenv:Body></soapenv:Envelope>";

  // Negative test case invalid messages
  private String helloBadReq1 = "<HelloRequest xmlns=\"http://helloservice.org/types\"><argument>foo</argument><HelloRequest>";

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.api.jakarta_xml_ws.Dispatch.";

  private static final String SHARED_CLIENT_PKG = "com.sun.ts.tests.jaxws.sharedclients.doclithelloclient";

  private static final Class SERVICE_CLASS = com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.HelloService.class;

  private static final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloService";

  private static final String PORT_NAME = "HelloPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "dlhelloservice.endpoint.1";

  private static final String WSDLLOC_URL = "dlhelloservice.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  private Dispatch<Object> dispatchJaxb = null;

  private Dispatch<Source> dispatchSrc = null;

  private Dispatch<SOAPMessage> dispatchSM = null;

  static HelloService service = null;

  private static final Class JAXB_OBJECT_FACTORY = com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.ObjectFactory.class;

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

  private Dispatch<Source> createDispatchSource() throws Exception {
    return service.createDispatch(PORT_QNAME, Source.class,
        jakarta.xml.ws.Service.Mode.PAYLOAD);
  }

  private Dispatch<SOAPMessage> createDispatchSOAPMessage() throws Exception {
    return service.createDispatch(PORT_QNAME, SOAPMessage.class,
        jakarta.xml.ws.Service.Mode.MESSAGE);
  }

  class MyHandlerXML implements AsyncHandler<Source> {
    private String theData;

    public String getData() {
      return theData;
    }

    public void handleResponse(Response<Source> res) {
      TestUtil.logMsg("AsyncHandler: MyHandlerXML()");
      String resStr = null;
      try {
        Source resMsg = res.get();
        resStr = JAXWS_Util
            .getDOMResultAsString(JAXWS_Util.getSourceAsDOMResult(resMsg));
        TestUtil.logMsg("resStr=" + resStr);
      } catch (Exception e) {
        e.printStackTrace();
      }
      theData = resStr;
    }
  }

  class MyHandlerSOAPMessage implements AsyncHandler<SOAPMessage> {
    private String theData;

    public String getData() {
      return theData;
    }

    public void handleResponse(Response<SOAPMessage> res) {
      TestUtil.logMsg("AsyncHandler: MyHandlerSOAPMessage()");
      String resStr = null;
      try {
        SOAPMessage resMsg = res.get();
      } catch (Exception e) {
        e.printStackTrace();
      }
      theData = resStr;
    }
  }

  class MyHandlerJAXB implements AsyncHandler<Object> {
    private HelloResponse theData;

    public HelloResponse getData() {
      return theData;
    }

    public void handleResponse(Response<Object> res) {
      TestUtil.logMsg("AsyncHandler: MyHandlerJAXB()");
      try {
        HelloResponse resMsg = (HelloResponse) res.get();
        theData = resMsg;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
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
   * @testName: invokeTestXML
   *
   * @assertion_ids: JAXWS:SPEC:4014; JAXWS:JAVADOC:8; WS4EE:SPEC:4005;
   *
   * @test_Strategy:
   */
  public void invokeTestXML() throws Fault {
    TestUtil.logTrace("invokeTestXML");
    boolean pass = true;
    Source reqMsg = JAXWS_Util.makeSource(helloReq, "StreamSource");
    String resStr;
    try {
      dispatchSrc = createDispatchSource();
      Source resMsg = dispatchSrc.invoke(reqMsg);
      try {
        resStr = JAXWS_Util
            .getDOMResultAsString(JAXWS_Util.getSourceAsDOMResult(resMsg));
        TestUtil.logMsg("resStr=" + resStr);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeTestXML failed");
  }

  /*
   * @testName: invokeAsyncTestXML
   *
   * @assertion_ids: JAXWS:SPEC:4014; JAXWS:JAVADOC:9; WS4EE:SPEC:4005;
   * WS4EE:SPEC:4006; WS4EE:SPEC:4007;
   *
   * @test_Strategy:
   */
  public void invokeAsyncTestXML() throws Fault {
    TestUtil.logTrace("invokeAsyncTestXML");
    boolean pass = true;
    Source reqMsg = JAXWS_Util.makeSource(helloReq, "StreamSource");
    String resStr;
    try {
      dispatchSrc = createDispatchSource();
      Response<Source> res = dispatchSrc.invokeAsync(reqMsg);
      TestUtil.logMsg("Polling and waiting for data ...");
      Object lock = new Object();
      while (!res.isDone()) {
        synchronized (lock) {
          try {
            lock.wait(50);
          } catch (InterruptedException e) {
            // ignore
          }
        }
      }

      try {
        Source resMsg = res.get();
        resStr = JAXWS_Util
            .getDOMResultAsString(JAXWS_Util.getSourceAsDOMResult(resMsg));
        TestUtil.logMsg("resStr=" + resStr);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeAsyncTestXML failed");
  }

  /*
   * @testName: invokeAsyncHandlerTestXML
   *
   * @assertion_ids: JAXWS:SPEC:4014; JAXWS:JAVADOC:9; WS4EE:SPEC:4005;
   * WS4EE:SPEC:4006; WS4EE:SPEC:4008;
   *
   * @test_Strategy:
   */
  public void invokeAsyncHandlerTestXML() throws Fault {
    TestUtil.logTrace("invokeAsyncHandlerTestXML");
    boolean pass = true;
    Source reqMsg = JAXWS_Util.makeSource(helloReq, "StreamSource");
    MyHandlerXML handler = new MyHandlerXML();
    Future<?> future;
    try {
      dispatchSrc = createDispatchSource();
      future = dispatchSrc.invokeAsync(reqMsg, handler);
      TestUtil.logMsg("Polling and waiting for data ...");
      Object lock = new Object();
      while (!future.isDone()) {
        synchronized (lock) {
          try {
            lock.wait(50);
          } catch (InterruptedException e) {
            // ignore
          }
        }
      }
      TestUtil
          .logMsg("Data from AsyncHandler MyHandler(): " + handler.getData());
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }

    if (!pass)
      throw new Fault("invokeAsyncHandlerTestXML failed");
  }

  /*
   * @testName: invokeOneWayTestXML
   *
   * @assertion_ids: JAXWS:SPEC:4014; JAXWS:JAVADOC:11; JAXWS:SPEC:10016;
   * JAXWS:SPEC:6006; WS4EE:SPEC:4005;
   *
   * @test_Strategy:
   */
  public void invokeOneWayTestXML() throws Fault {
    TestUtil.logTrace("invokeOneWayTestXML");
    boolean pass = true;
    Source reqMsg = JAXWS_Util.makeSource(helloOneWayReq, "StreamSource");
    try {
      dispatchSrc = createDispatchSource();
      dispatchSrc.invokeOneWay(reqMsg);
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeOneWayTestXML failed");
  }

  /*
   * @testName: invokeTestJAXB
   *
   * @assertion_ids: JAXWS:SPEC:4014; JAXWS:JAVADOC:8; WS4EE:SPEC:4005;
   *
   * @test_Strategy:
   */
  public void invokeTestJAXB() throws Fault {
    TestUtil.logTrace("invokeTestJAXB");
    boolean pass = true;
    HelloRequest helloReq = null;
    try {
      helloReq = of.createHelloRequest();
      helloReq.setArgument("foo");
    } catch (Exception e) {
      e.printStackTrace();
    }
    HelloResponse helloRes = null;
    try {
      dispatchJaxb = createDispatchJAXB();
      java.util.Map<String, Object> reqContext = dispatchJaxb
          .getRequestContext();
      helloRes = (HelloResponse) dispatchJaxb.invoke(helloReq);
      TestUtil.logMsg("HelloRequest: " + helloReq.getArgument());
      TestUtil.logMsg("HelloResponse: " + helloRes.getArgument());
      if (!helloReq.getArgument().equals(helloRes.getArgument()))
        pass = false;
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeTestJAXB failed");
  }

  /*
   * @testName: invokeAsyncTestJAXB
   *
   * @assertion_ids: JAXWS:SPEC:4014; JAXWS:JAVADOC:9; WS4EE:SPEC:4005;
   * WS4EE:SPEC:4006; WS4EE:SPEC:4007;
   *
   * @test_Strategy:
   */
  public void invokeAsyncTestJAXB() throws Fault {
    TestUtil.logTrace("invokeAsyncTestJAXB");
    boolean pass = true;
    HelloRequest helloReq = null;
    try {
      helloReq = of.createHelloRequest();
      helloReq.setArgument("foo");
    } catch (Exception e) {
      e.printStackTrace();
    }
    HelloResponse helloRes = null;
    try {
      dispatchJaxb = createDispatchJAXB();
      java.util.Map<String, Object> reqContext = dispatchJaxb
          .getRequestContext();
      Response<Object> res = dispatchJaxb.invokeAsync(helloReq);
      TestUtil.logMsg("Polling and waiting for data ...");
      Object lock = new Object();
      while (!res.isDone()) {
        synchronized (lock) {
          try {
            lock.wait(50);
          } catch (InterruptedException e) {
            // ignore
          }
        }
      }
      helloRes = (HelloResponse) res.get();
      TestUtil.logMsg("HelloRequest: " + helloReq.getArgument());
      TestUtil.logMsg("HelloResponse: " + helloRes.getArgument());
      if (!helloReq.getArgument().equals(helloRes.getArgument()))
        pass = false;
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeAsyncTestJAXB failed");
  }

  /*
   * @testName: invokeAsyncHandlerTestJAXB
   *
   * @assertion_ids: JAXWS:SPEC:4014; JAXWS:JAVADOC:10; WS4EE:SPEC:4005;
   * WS4EE:SPEC:4006; WS4EE:SPEC:4008;
   *
   * @test_Strategy:
   */
  public void invokeAsyncHandlerTestJAXB() throws Fault {
    TestUtil.logTrace("invokeAsyncHandlerTestJAXB");
    boolean pass = true;
    HelloRequest helloReq = null;
    Future<?> future;
    try {
      helloReq = of.createHelloRequest();
      helloReq.setArgument("foo");
    } catch (Exception e) {
      e.printStackTrace();
    }
    MyHandlerJAXB handler = new MyHandlerJAXB();
    HelloResponse helloRes = null;
    try {
      dispatchJaxb = createDispatchJAXB();
      java.util.Map<String, Object> reqContext = dispatchJaxb
          .getRequestContext();
      future = dispatchJaxb.invokeAsync(helloReq, handler);
      TestUtil.logMsg("Polling and waiting for data ...");
      Object lock = new Object();
      while (!future.isDone()) {
        synchronized (lock) {
          try {
            lock.wait(50);
          } catch (InterruptedException e) {
            // ignore
          }
        }
      }
      helloRes = handler.getData();
      if (helloRes != null) {
        TestUtil.logMsg("HelloRequest: " + helloReq.getArgument());
        TestUtil.logMsg("HelloResponse: " + helloRes.getArgument());
        if (!helloReq.getArgument().equals(helloRes.getArgument()))
          pass = false;
      }
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeAsyncHandlerTestJAXB failed");
  }

  /*
   * @testName: invokeOneWayTestJAXB
   *
   * @assertion_ids: JAXWS:SPEC:4014; JAXWS:JAVADOC:11; JAXWS:SPEC:6006;
   * WS4EE:SPEC:4005;
   *
   * @test_Strategy:
   */
  public void invokeOneWayTestJAXB() throws Fault {
    TestUtil.logTrace("invokeOneWayTestJAXB");
    boolean pass = true;
    HelloRequest helloReq = null;
    try {
      helloReq = of.createHelloRequest();
      helloReq.setArgument("foo");
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      dispatchJaxb = createDispatchJAXB();
      java.util.Map<String, Object> reqContext = dispatchJaxb
          .getRequestContext();
      dispatchJaxb.invokeOneWay(helloReq);
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeOneWayTestJAXB failed");
  }

  /*
   * @testName: invokeNegativeTestXML
   *
   * @assertion_ids: JAXWS:SPEC:4014; JAXWS:SPEC:4015;
   *
   * @test_Strategy:
   */
  public void invokeNegativeTestXML() throws Fault {
    TestUtil.logTrace("invokeNegativeTestXML");
    boolean pass = true;
    Source reqMsg = JAXWS_Util.makeSource(helloBadReq1, "StreamSource");
    String resStr;
    try {
      dispatchSrc = createDispatchSource();
      Source resMsg = dispatchSrc.invoke(reqMsg);
      pass = false;
      TestUtil.logErr("Did not get WebServiceException ...");
    } catch (WebServiceException e) {
      TestUtil.logMsg("Got expected WebServiceException");
      e.printStackTrace();
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass)
      throw new Fault("invokeNegativeTestXML failed");
  }

  /*
   * @testName: invokeOneWayNegTestXML
   *
   * @assertion_ids: JAXWS:SPEC:4014; JAXWS:SPEC:4017;
   *
   * @test_Strategy:
   */
  public void invokeOneWayNegTestXML() throws Fault {
    TestUtil.logTrace("invokeOneWayNegTestXML");
    boolean pass = true;
    Source reqMsg = JAXWS_Util.makeSource(helloBadReq1, "StreamSource");
    try {
      dispatchSrc = createDispatchSource();
      dispatchSrc.invokeOneWay(reqMsg);
      pass = false;
      TestUtil.logErr("Did not get WebServiceException ...");
    } catch (WebServiceException e) {
      TestUtil.logMsg("Got expected WebServiceException");
      e.printStackTrace();
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass)
      throw new Fault("invokeOneWayNegTestXML failed");
  }

  /*
   * @testName: invokeTestSOAPMessage
   *
   * @assertion_ids: JAXWS:SPEC:4014; JAXWS:JAVADOC:8;
   *
   * @test_Strategy:
   */
  public void invokeTestSOAPMessage() throws Fault {
    TestUtil.logTrace("invokeTestSOAPMessage");
    boolean pass = true;
    SOAPMessage reqMsg = JAXWS_Util.makeSOAPMessage(helloReqSM);
    String resStr;
    try {
      dispatchSM = createDispatchSOAPMessage();
      SOAPMessage resMsg = dispatchSM.invoke(reqMsg);
      try {
        resStr = JAXWS_Util.getSOAPMessageAsString(resMsg);
        TestUtil.logMsg("resStr=" + resStr);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeTestSOAPMessage failed");
  }

  /*
   * @testName: invokeAsyncTestSOAPMessage
   *
   * @assertion_ids: JAXWS:SPEC:4014; JAXWS:JAVADOC:10;
   *
   * @test_Strategy:
   */
  public void invokeAsyncTestSOAPMessage() throws Fault {
    TestUtil.logTrace("invokeAsyncTestSOAPMessage");
    boolean pass = true;
    SOAPMessage reqMsg = JAXWS_Util.makeSOAPMessage(helloReqSM);
    String resStr;
    try {
      dispatchSM = createDispatchSOAPMessage();
      Response<SOAPMessage> res = dispatchSM.invokeAsync(reqMsg);
      TestUtil.logMsg("Polling and waiting for data ...");
      Object lock = new Object();
      while (!res.isDone()) {
        synchronized (lock) {
          try {
            lock.wait(50);
          } catch (InterruptedException e) {
            // ignore
          }
        }
      }
      SOAPMessage resMsg = res.get();
      try {
        resStr = JAXWS_Util.getSOAPMessageAsString(resMsg);
        TestUtil.logMsg("resStr=" + resStr);
      } catch (Exception e) {
        e.printStackTrace();
      }
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeAsyncTestSOAPMessage failed");
  }

  /*
   * @testName: invokeAsyncHandlerTestSOAPMessage
   *
   * @assertion_ids: JAXWS:SPEC:4014; JAXWS:JAVADOC:10;
   *
   * @test_Strategy:
   */
  public void invokeAsyncHandlerTestSOAPMessage() throws Fault {
    TestUtil.logTrace("invokeAsyncHandlerTestSOAPMessage");
    boolean pass = true;
    SOAPMessage reqMsg = JAXWS_Util.makeSOAPMessage(helloReqSM);
    MyHandlerSOAPMessage handler = new MyHandlerSOAPMessage();
    Future<?> future;
    try {
      dispatchSM = createDispatchSOAPMessage();
      future = dispatchSM.invokeAsync(reqMsg, handler);
      TestUtil.logMsg("Polling and waiting for data ...");
      Object lock = new Object();
      while (!future.isDone()) {
        synchronized (lock) {
          try {
            lock.wait(50);
          } catch (InterruptedException e) {
            // ignore
          }
        }
      }
      TestUtil
          .logMsg("Data from AsyncHandler MyHandler(): " + handler.getData());
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }

    if (!pass)
      throw new Fault("invokeAsyncHandlerTestSOAPMessage failed");
  }

  /*
   * @testName: invokeOneWayTestSOAPMessage
   *
   * @assertion_ids: JAXWS:SPEC:4014; JAXWS:JAVADOC:11;
   *
   * @test_Strategy:
   */
  public void invokeOneWayTestSOAPMessage() throws Fault {
    TestUtil.logTrace("invokeOneWayTestSOAPMessage");
    boolean pass = true;
    SOAPMessage reqMsg = JAXWS_Util.makeSOAPMessage(helloOneWayReqSM);
    try {
      dispatchSM = createDispatchSOAPMessage();
      dispatchSM.invokeOneWay(reqMsg);
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeOneWayTestSOAPMessage failed");
  }

  /*
   * @testName: invokeTestJAXBNull
   *
   * @assertion_ids: JAXWS:SPEC:2036; JAXWS:SPEC:4013; JAXWS:SPEC:4015;
   *
   * @test_Strategy:
   */
  public void invokeTestJAXBNull() throws Fault {
    TestUtil.logTrace("invokeTestJAXBNull");
    boolean pass = true;
    HelloRequest helloReq = null;
    HelloResponse helloRes = null;
    try {
      dispatchJaxb = createDispatchJAXB();
      Binding binding = ((BindingProvider) dispatchJaxb).getBinding();
      if (binding instanceof SOAPBinding) {
        TestUtil.logMsg("binding is a SOAPBinding instance");
      } else {
        TestUtil.logErr("binding is not a SOAPBinding instance");
        pass = false;
      }
      java.util.Map<String, Object> reqContext = dispatchJaxb
          .getRequestContext();
      TestUtil.logMsg("Calling invoke ....");
      helloRes = (HelloResponse) dispatchJaxb.invoke(helloReq);
      TestUtil.logErr(
          "No SOAPFaultException or WebServiceException from bad invoke");
      pass = false;
    } catch (SOAPFaultException e) {
      TestUtil.logMsg("Got expected SOAPFaultException: " + e);
    } catch (WebServiceException e) {
      TestUtil.logMsg("Got expected WebServiceException: " + e);
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass)
      throw new Fault("invokeTestJAXBNull failed");
  }
}
