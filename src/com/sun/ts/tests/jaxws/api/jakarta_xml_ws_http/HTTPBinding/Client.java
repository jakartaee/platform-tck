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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws_http.HTTPBinding;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.sharedclients.xmlbinddlhelloproviderclient.*;

import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.net.*;
import java.util.*;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.ws.*;
import jakarta.xml.ws.http.*;
import javax.xml.transform.Source;
import jakarta.xml.ws.handler.*;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.api.jakarta_xml_ws.LogicalMessage.";

  private static final String SHARED_CLIENT_PKG = "com.sun.ts.tests.jaxws.sharedclients.xmlbinddlhelloproviderclient.";

  private static final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloService";

  private static final String PORT_NAME = "HelloPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private static final Class SERVICE_CLASS = com.sun.ts.tests.jaxws.sharedclients.xmlbinddlhelloproviderclient.HelloService.class;

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "xmlbinddlhelloproviderservice.endpoint.1";

  private String url = null;

  private String bindingID = null;

  private Dispatch<Source> dispatchSrc = null;

  private Dispatch<Object> dispatchJaxb = null;

  static jakarta.xml.ws.Service service = null;

  private Binding binding = null;

  private BindingProvider bpDispatch = null;

  private static final Class JAXB_OBJECT_FACTORY = com.sun.ts.tests.jaxws.sharedclients.xmlbinddlhelloproviderclient.ObjectFactory.class;

  private String helloReq = "<HelloRequest xmlns=\"http://helloservice.org/types\"><argument>foo</argument></HelloRequest>";

  private JAXBContext createJAXBContext() {
    try {
      return JAXBContext.newInstance(JAXB_OBJECT_FACTORY);
    } catch (jakarta.xml.bind.JAXBException e) {
      throw new WebServiceException(e.getMessage(), e);
    }
  }

  private Dispatch<Object> createDispatchJAXB() throws Exception {
    jakarta.xml.ws.Service service = jakarta.xml.ws.Service.create(SERVICE_QNAME);
    service.addPort(PORT_QNAME, bindingID, url);
    return service.createDispatch(PORT_QNAME, createJAXBContext(),
        jakarta.xml.ws.Service.Mode.PAYLOAD);
  }

  private Dispatch<Source> createDispatchSource() throws Exception {
    jakarta.xml.ws.Service service = jakarta.xml.ws.Service.create(SERVICE_QNAME);
    service.addPort(PORT_QNAME, bindingID, url);
    return service.createDispatch(PORT_QNAME, Source.class,
        jakarta.xml.ws.Service.Mode.PAYLOAD);
  }

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
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

      getTestURLs();
      service = jakarta.xml.ws.Service.create(SERVICE_QNAME);

      bindingID = HTTPBinding.HTTP_BINDING;
      dispatchSrc = createDispatchSource();
      bpDispatch = (BindingProvider) dispatchSrc;

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
   * @testName: getHTTPBindingTest
   *
   * @assertion_ids: JAXWS:SPEC:11000; WS4EE:SPEC:5005; JAXWS:SPEC:7012;
   * JAXWS:SPEC:3039;
   *
   * @test_Strategy:
   */
  public void getHTTPBindingTest() throws Fault {
    TestUtil.logTrace("getHTTPBindingTest");
    boolean pass = true;
    TestUtil.logMsg("Get Binding interface for Dispatch object");
    binding = bpDispatch.getBinding();
    if (binding == null) {
      TestUtil.logErr("getBinding() returned null");
      pass = false;
    } else {
      if (binding instanceof HTTPBinding) {
        TestUtil.logMsg("binding is a HTTPBinding instance");
      } else {
        TestUtil.logErr("binding is not a HTTPBinding instance");
        pass = false;
      }
    }
    if (!pass)
      throw new Fault("getHTTPBindingTest failed");
  }

  /*
   * @testName: HTTPBindingConstantsTest
   *
   * @assertion_ids: JAXWS:SPEC:11000; WS4EE:SPEC:5005;
   *
   * @test_Strategy:
   */
  public void HTTPBindingConstantsTest() throws Fault {
    TestUtil.logTrace("HTTPBindingConstantsTest");
    boolean pass = true;

    TestUtil.logMsg("Verify that HTTP_BINDING constant value is correct");
    if (!HTTPBinding.HTTP_BINDING.equals(Constants.EXPECTED_HTTP_BINDING)) {
      TestUtil.logErr("HTTP_BINDING is incorrect");
      TestUtil.logErr("Got: [" + HTTPBinding.HTTP_BINDING + "]");
      TestUtil.logErr("Expected: [" + Constants.EXPECTED_HTTP_BINDING + "]");
      pass = false;
    }
    if (!pass)
      throw new Fault("HTTPBindingConstantsTest failed");
  }

  /*
   * @testName: invokeTestJAXB
   *
   * @assertion_ids: JAXWS:SPEC:11000; WS4EE:SPEC:5005; JAXWS:SPEC:7012;
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
      binding = ((BindingProvider) dispatchJaxb).getBinding();
      if (binding instanceof HTTPBinding) {
        TestUtil.logMsg("binding is a HTTPBinding instance");
      } else {
        TestUtil.logErr("binding is not a HTTPBinding instance");
        pass = false;
      }
      java.util.Map<String, Object> reqContext = dispatchJaxb
          .getRequestContext();
      TestUtil.logMsg("Calling invoke ....");
      helloRes = (HelloResponse) dispatchJaxb.invoke(helloReq);
      TestUtil.logMsg("After invoke ....");
      TestUtil.logMsg("HelloRequest=" + helloReq.getArgument());
      TestUtil.logMsg("HelloResponse=" + helloRes.getArgument());
      if (!helloRes.getArgument().equals(helloReq.getArgument()))
        pass = false;
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }
    if (!pass)
      throw new Fault("invokeTestJAXB failed");
  }

  /*
   * @testName: invokeTestXML
   *
   * @assertion_ids: JAXWS:SPEC:4014; JAXWS:JAVADOC:8; WS4EE:SPEC:5005;
   * JAXWS:SPEC:7012;
   *
   * @test_Strategy:
   */
  public void invokeTestXML() throws Fault {
    TestUtil.logTrace("invokeTestXML");
    boolean pass = true;
    Source reqMsg = JAXWS_Util.makeSource(helloReq, "StreamSource");
    String resStr;
    try {
      TestUtil.logMsg("Calling invoke ....");
      Source resMsg = dispatchSrc.invoke(reqMsg);
      TestUtil.logMsg("After invoke ....");
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
   * @testName: invokeTestJAXBBad
   *
   * @assertion_ids: JAXWS:SPEC:11000; WS4EE:SPEC:5005; JAXWS:SPEC:6004;
   * JAXWS:SPEC:4012; JAXWS:SPEC:4019;
   *
   * @test_Strategy:
   */
  public void invokeTestJAXBBad() throws Fault {
    TestUtil.logTrace("invokeTestJAXBBad");
    boolean pass = true;
    String helloReq = "Hello Request";
    HelloResponse helloRes = null;
    try {
      dispatchJaxb = createDispatchJAXB();
      binding = ((BindingProvider) dispatchJaxb).getBinding();
      if (binding instanceof HTTPBinding) {
        TestUtil.logMsg("binding is a HTTPBinding instance");
      } else {
        TestUtil.logErr("binding is not a HTTPBinding instance");
        pass = false;
      }
      java.util.Map<String, Object> reqContext = dispatchJaxb
          .getRequestContext();
      TestUtil.logMsg("Calling invoke ....");
      dispatchJaxb.invoke(helloReq);
      TestUtil.logErr("No WebServiceException from bad invoke");
      pass = false;
    } catch (WebServiceException e) {
      TestUtil.logMsg("Got expected runtime exception WebServiceException" + e);
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception occurred: " + e);
      pass = false;
    }
    if (!pass)
      throw new Fault("invokeTestJAXBBad failed");
  }

  /*
   * @testName: invokeTestJAXBNull
   *
   * @assertion_ids: JAXWS:SPEC:11000; WS4EE:SPEC:5005; JAXWS:SPEC:2036;
   * JAXWS:SPEC:4013; JAXWS:SPEC:4015;
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
      binding = ((BindingProvider) dispatchJaxb).getBinding();
      if (binding instanceof HTTPBinding) {
        TestUtil.logMsg("binding is a HTTPBinding instance");
      } else {
        TestUtil.logErr("binding is not a HTTPBinding instance");
        pass = false;
      }
      java.util.Map<String, Object> reqContext = dispatchJaxb
          .getRequestContext();
      TestUtil.logMsg("Calling invoke ....");
      dispatchJaxb.invoke(helloReq);
      TestUtil.logErr("No WebServiceException from bad invoke");
      pass = false;
    } catch (WebServiceException e) {
      TestUtil.logMsg("Got expected WebServiceException" + e);
    } catch (Exception e) {
      pass = false;
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass)
      throw new Fault("invokeTestJAXBNull failed");
  }

  /*
   * @testName: incompatibleHandlerTest
   *
   * @assertion_ids: JAXWS:SPEC:10006;
   *
   * @test_Strategy:
   */
  public void incompatibleHandlerTest() throws Fault {
    TestUtil.logTrace("incompatibleHandlerTest");
    boolean pass = true;
    TestUtil.logMsg("Getting the Binding");
    binding = bpDispatch.getBinding();
    if (binding == null) {
      TestUtil.logErr("getBinding() returned null");
      pass = false;
    } else {
      if (binding instanceof HTTPBinding) {
        TestUtil.logMsg("binding is a HTTPBinding instance");
        try {
          List<Handler> handlerList = new ArrayList<Handler>();
          Handler handler = new com.sun.ts.tests.jaxws.sharedclients.xmlbinddlhelloproviderclient.SOAPHandler();
          handlerList.add(handler);
          TestUtil.logMsg("HandlerChain=" + handlerList);
          TestUtil.logMsg("HandlerChain size = " + handlerList.size());
          binding.setHandlerChain(handlerList);
          TestUtil.logErr(
              "Adding an incompatible handler did not throw a WebServiceException");
          pass = false;
        } catch (WebServiceException wse) {
          // test passed
          TestUtil.logTrace("WebServiceException was thrown");
        }
      } else {
        TestUtil.logErr("binding is not a HTTPBinding instance");
        pass = false;
      }
    }
    if (!pass)
      throw new Fault("incompatibleHandlerTest failed");
  }

  /*
   * @testName: getEndpointReferenceTest
   *
   * @assertion_ids: JAXWS:SPEC:5023.4; JAXWS:SPEC:4024; JAXWS:SPEC:5024.4;
   *
   * @test_Strategy:
   */
  public void getEndpointReferenceTest() throws Fault {
    TestUtil.logTrace("getEndpointReferenceTest");
    boolean pass = false;
    try {
      TestUtil
          .logMsg("Attempt to get EndpointReference for HTTP Binding object");
      EndpointReference epr = dispatchSrc.getEndpointReference();
      TestUtil.logErr("Did not catch expected UnsupportedOperationException");
    } catch (UnsupportedOperationException e) {
      TestUtil.logMsg("Caught expected UnsupportedOperationException");
      pass = true;
    } catch (Exception e) {
      TestUtil.logErr("Received unexpected exception", e);
    }
    if (!pass)
      throw new Fault("getEndpointReferenceTest failed");
  }
}
