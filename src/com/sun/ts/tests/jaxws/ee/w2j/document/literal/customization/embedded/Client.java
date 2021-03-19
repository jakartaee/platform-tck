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

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.customization.embedded;

import com.sun.ts.tests.jaxws.ee.w2j.document.literal.customization.embedded.custom.pkg.*;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxws.common.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.*;
import javax.xml.namespace.QName;
import jakarta.xml.ws.soap.*;
import javax.xml.transform.Source;

import com.sun.javatest.Status;

import javax.naming.InitialContext;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.customization.embedded.";

  // service and port information
  private static final String NAMESPACEURI = "http://customizationembeddedtest.org/wsdl";

  private static final String SERVICE_NAME = "myService";

  private static final String PORT_NAME = "HelloPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "w2jcustomizationembeddedtest.endpoint.1";

  private static final String WSDLLOC_URL = "w2jcustomizationembeddedtest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  Hello port = null;

  static CustomizationEmbeddedTestService service = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getPortStandalone() throws Exception {
    port = (Hello) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        CustomizationEmbeddedTestService.class, PORT_QNAME, Hello.class);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtaining service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (Hello) JAXWS_Util.getPort(service, PORT_QNAME, Hello.class);
    // port = (Hello) service.getMyHelloPort();
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
    JAXWS_Util.dumpTargetEndpointAddress(port);
    // JAXWS_Util.setSOAPLogging(port);
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

    // Initialize QName's used in the test
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
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (CustomizationEmbeddedTestService) getSharedObject();
        getTestURLs();
        getPortJavaEE();
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
   * @testName: CustomizationEmbeddedTest
   *
   * @assertion_ids: JAXWS:SPEC:8000; JAXWS:SPEC:8001; JAXWS:SPEC:8002;
   * JAXWS:SPEC:8005; JAXWS:SPEC:8006; JAXWS:SPEC:8007; JAXWS:SPEC:8010;
   * JAXWS:SPEC:8012; JAXWS:SPEC:8013; JAXWS:SPEC:2064; JAXWS:SPEC:2023;
   * JAXWS:SPEC:2028; JAXWS:SPEC:7000; JAXWS:SPEC:8009;
   *
   * @test_Strategy: Embedded annotations in the wsdl are used to change aspects
   * of the wsdl file. If the endpoint is reachable then the customization
   * worked.
   *
   *
   * 
   */
  public void CustomizationEmbeddedTest() throws Fault {
    TestUtil.logTrace("CustomizationEmbeddedTest");
    boolean pass = true;
    String reqStr = "Hello";
    String reqStr2 = "World";
    String resStr = "Hello, World!";
    try {
      // wrapper style
      TestUtil.logMsg("Testing wrapper style enableWrapperStyle=true ...");
      String result = port.hello1(reqStr);
      TestUtil.logMsg("result=" + result);
      if (!result.equals(resStr)) {
        TestUtil.logErr("expected: " + resStr + ", received: " + result);
        pass = false;
      }

      // non-wrapper style
      TestUtil.logMsg("Testing non-wrapper style enableWrapperStyle=false ...");
      Hello2 hello2 = of.createHello2();
      hello2.setArgument(reqStr);
      HelloResponse h = port.hello2(hello2);
      result = h.getResponse();
      TestUtil.logMsg("result=" + result);
      if (!result.equals(resStr)) {
        TestUtil.logErr("expected: " + resStr + ", received: " + result);
        pass = false;
      }

      // non-wrapper style
      TestUtil.logMsg("Testing non-wrapper style enableWrapperStyle=false ...");
      HelloRequest3 hello3 = of.createHelloRequest3();
      hello3.setHelloRequest1(reqStr);
      hello3.setHelloRequest2(reqStr2);
      HelloResponse3 h3 = port.hello3(hello3);
      result = h3.getResponse();
      TestUtil.logMsg("result=" + result);
      if (!result.equals(resStr)) {
        TestUtil.logErr("expected: " + resStr + ", received: " + result);
        pass = false;
      }

      TestUtil.logMsg("Testing Fault Exception Case ...");
      hello3 = of.createHelloRequest3();
      hello3.setHelloRequest1("HelloException");
      hello3.setHelloRequest2(reqStr2);
      try {
        port.hello3(hello3);
        TestUtil.logErr("HelloException expected but not thrown");
        pass = false;
      } catch (HelloException e) {
        TestUtil.logMsg("Got expected HelloException");
      }
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }

    if (!pass)
      throw new Fault("CustomizationEmbeddedTest failed");
  }

  /*
   * @testName: jaxbCustomizationTest
   *
   * @assertion_ids: JAXWS:SPEC:8005;
   *
   * @test_Strategy: A jaxb customization test
   *
   */
  public void jaxbCustomizationTest() throws Fault {
    TestUtil.logTrace("jaxbCustomizationTest");
    boolean pass = true;
    String resStr = "FooBarPopeyeOlive";
    EchoRequest echoRequest = new EchoRequest();
    Name[] names = { new Name(), new Name() };
    names[0].setFirst("Foo");
    names[0].setLast("Bar");
    names[1].setFirst("Popeye");
    names[1].setLast("Olive");
    echoRequest.setName(names);
    try {
      TestUtil.logMsg("Testing jaxb customization test ...");
      EchoResponse echoResponse = port.echo(echoRequest);
      String result = echoResponse.getReturn();
      if (!result.equals(resStr)) {
        TestUtil.logErr("expected: " + resStr + ", received: " + result);
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }

    if (!pass)
      throw new Fault("jaxbCustomizationTest failed");
  }

}
