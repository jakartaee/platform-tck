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

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.asynctest.client;

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

import com.sun.javatest.Status;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.asynctest.client.";

  // service and port information
  private static final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloService";

  private static final String PORT_NAME = "HelloPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "w2jasynctest.endpoint.1";

  private static final String WSDLLOC_URL = "w2jasynctest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  Hello port = null;

  static HelloService service = null;

  private class HelloCallbackHandler implements AsyncHandler<HelloResponse> {
    private HelloResponse output;

    public void handleResponse(Response<HelloResponse> response) {
      try {
        output = response.get();
      } catch (ExecutionException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    HelloResponse getResponse() {
      return output;
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

  private void getPortStandalone() throws Exception {
    port = (Hello) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        HelloService.class, PORT_QNAME, Hello.class);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtaining service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (Hello) service.getPort(Hello.class);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
    JAXWS_Util.dumpTargetEndpointAddress(port);
    // JAXWS_Util.setTargetEndpointAddress(port, url);
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
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (HelloService) getSharedObject();
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
   * @testName: invokeSynchronousTest
   *
   * @assertion_ids: JAXWS:SPEC:2032; JAXWS:SPEC:2033; JAXWS:SPEC:2034;
   * JAXWS:SPEC:2034; JAXWS:SPEC:2035; JAXWS:SPEC:2038; JAXWS:SPEC:2039;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke an RPC method using
   * synchronous method.
   *
   * Description A client can invoke an RPC method via generated stub.
   */
  public void invokeSynchronousTest() throws Fault {
    TestUtil.logTrace("invokeSynchronousTest");
    boolean pass = true;
    String reqStr = "foo";
    String resStr = "Hello, foo!";
    try {
      HelloRequest helloReq = of.createHelloRequest();
      helloReq.setString(reqStr);
      HelloResponse helloRes = port.hello(helloReq);
      String result = helloRes.getResult();
      TestUtil.logMsg("result=" + result);
      if (!result.equals(resStr)) {
        TestUtil.logErr("expected: " + resStr + ", received: " + result);
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }

    if (!pass)
      throw new Fault("invokeSynchronousTest failed");
  }

  /*
   * @testName: invokeAsyncPollTest
   *
   * @assertion_ids: JAXWS:SPEC:2032; JAXWS:SPEC:2033; JAXWS:SPEC:2034;
   * JAXWS:SPEC:2034; JAXWS:SPEC:2035; JAXWS:SPEC:2038; JAXWS:SPEC:2039;
   * WS4EE:SPEC:4006; WS4EE:SPEC:4007; WS4EE:SPEC:4008;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke an RPC method using
   * AsyncPoll method.
   *
   * Description A client can invoke an RPC method via generated stub.
   */
  public void invokeAsyncPollTest() throws Fault {
    TestUtil.logTrace("invokeAsyncPollTest");
    boolean pass = true;
    String reqStr = "foo";
    String resStr = "Hello, foo!";
    try {
      HelloRequest helloReq = of.createHelloRequest();
      helloReq.setString(reqStr);
      Response<HelloResponse> response = port.helloAsync(helloReq);
      TestUtil.logMsg("Polling and waiting for data ...");
      Object lock = new Object();
      while (!response.isDone()) {
        synchronized (lock) {
          try {
            lock.wait(50);
          } catch (InterruptedException e) {
            // ignore
          }
        }
      }
      HelloResponse helloRes = response.get();
      String result = helloRes.getResult();
      TestUtil.logMsg("result=" + result);
      if (!result.equals(resStr)) {
        TestUtil.logErr("expected: " + resStr + ", received: " + result);
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }

    if (!pass)
      throw new Fault("invokeAsyncPollTest failed");
  }

  /*
   * @testName: invokeAsyncCallbackTest
   *
   * @assertion_ids: JAXWS:SPEC:2032; JAXWS:SPEC:2033; JAXWS:SPEC:2034;
   * JAXWS:SPEC:2034; JAXWS:SPEC:2035; JAXWS:SPEC:2038; JAXWS:SPEC:2039;
   * WS4EE:SPEC:4006; WS4EE:SPEC:4007; WS4EE:SPEC:4008;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke an RPC method using
   * AsyncCallback method.
   *
   * Description A client can invoke an RPC method via generated stub.
   */
  public void invokeAsyncCallbackTest() throws Fault {
    TestUtil.logTrace("invokeAsyncCallbackTest");
    boolean pass = true;
    String reqStr = "foo";
    String resStr = "Hello, foo!";
    try {
      HelloRequest helloReq = of.createHelloRequest();
      helloReq.setString(reqStr);
      HelloCallbackHandler callbackHandler = new HelloCallbackHandler();
      Future<?> response = port.helloAsync(helloReq, callbackHandler);
      TestUtil.logMsg("Waiting for Callback to complete to obtain data ...");
      Object lock = new Object();
      while (!response.isDone()) {
        synchronized (lock) {
          try {
            lock.wait(50);
          } catch (InterruptedException e) {
            // ignore
          }
        }
      }
      HelloResponse helloRes = callbackHandler.getResponse();
      String result = helloRes.getResult();
      TestUtil.logMsg("result=" + result);
      if (!result.equals(resStr)) {
        TestUtil.logErr("expected: " + resStr + ", received: " + result);
        pass = false;
      }
    } catch (Exception e) {
      pass = false;
      e.printStackTrace();
    }

    if (!pass)
      throw new Fault("invokeAsyncCallbackTest failed");
  }
}
