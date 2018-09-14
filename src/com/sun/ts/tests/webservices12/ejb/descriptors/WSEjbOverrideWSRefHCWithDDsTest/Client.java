/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.webservices12.ejb.descriptors.WSEjbOverrideWSRefHCWithDDsTest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;

import javax.jws.*;
import javax.xml.ws.*;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.net.URL;
import java.net.URLConnection;
import java.io.*;
import java.util.Properties;
import java.util.Iterator;

public class Client extends EETest {

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "wsejboverridewsrefhcwithddstest.endpoint.1";

  private static final String WSDLLOC_URL = "wsejboverridewsrefhcwithddstest.wsdlloc.1";

  private URL url = null;

  private URLConnection urlConn = null;

  private String SERVLET = "/WSEjbOverrideWSRefHCWithDDsTestClnt_web/ServletTest";

  private Hello port;

  @WebServiceRef(name = "service/wsejboverridewsrefhcwithddstest", type = java.lang.Object.class, value = javax.xml.ws.Service.class)
  static HelloService service;

  private void getPort() throws Exception {
    TestUtil.logMsg(
        "Get wsejboverridewsrefhcwithddstest Service via @WebServiceRef annotation");
    TestUtil.logMsg(
        "Uses name attribute @WebServiceRef(name=\"service/wsejboverridewsrefhcwithddstest\")");
    TestUtil.logMsg("service=" + service);
    TestUtil.logMsg("Get port from service");
    port = (Hello) service.getHello();
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Port obtained");
    JAXWS_Util.dumpTargetEndpointAddress(port);
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap webservices-url-props.dat
   * 
   * @class.setup_props: webServerHost; webServerPort;
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
      if (pass)
        getPort();
    } catch (Exception e) {
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

  private void printSeperationLine() {
    TestUtil.logMsg("---------------------------");
  }

  /*
   * @testName: WSEjbSOAPHandlersApplicationClientTest
   *
   * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42;
   * WS4EE:SPEC:43; WS4EE:SPEC:44; WS4EE:SPEC:51; WS4EE:SPEC:109;
   * WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155;
   * WS4EE:SPEC:171; WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4002;
   * WS4EE:SPEC:115; WS4EE:SPEC:213; WS4EE:SPEC:187; WS4EE:SPEC:6021;
   * WS4EE:SPEC:6022; WS4EE:SPEC:6024; WS4EE:SPEC:6026; WS4EE:SPEC:6029;
   * WS4EE:SPEC:6035; WS4EE:SPEC:6038; WS4EE:SPEC:6048; WS4EE:SPEC:6049;
   * WS4EE:SPEC:6009; WS4EE:SPEC:6010; WS4EE:SPEC:6011; WS4EE:SPEC:6023;
   *
   * @test_Strategy: This is a prebuilt client and prebuilt webservice using EJB
   * endpoint. Tests @WebServiceRef, @WebService, and @HandlerChain annotations.
   * Tests client-side and server-side handlers and callbacks. Deploy via full
   * 109 deployment descriptors. The
   * 
   * @WebserviceRef annotation on the client must be overridden by the
   * deployment descriptors. The @HandleChain annotation does not have to be
   * specified. The @WebserviceRef and if present the @HandlerChain annotations
   * are ignored. Everything is obtained from the deployment descriptors so the
   * annotations are ignored. Verifies that override via Deployment Descriptors
   * works. Tests the appclient container.
   */
  public void WSEjbSOAPHandlersApplicationClientTest() throws Fault {
    TestUtil.logMsg("WSEjbSOAPHandlersApplicationClientTest");
    boolean pass = true;
    try {
      TestUtil
          .logMsg("Invoke the webservice endpoint from AppClient container");
      String serverSideMsgs = port.helloEcho("firstcall");
      serverSideMsgs = port.helloEcho("secondcall");
      String clientSideMsgs = HandlerTracker.getMessages1();
      HandlerTracker.purge();
      TestUtil.logMsg("-----------------------------------");
      TestUtil.logMsg("Dumping ClientSide Handler messages");
      TestUtil.logMsg("-----------------------------------");
      TestUtil.logMsg(clientSideMsgs);
      TestUtil.logMsg("Verify client side handler callbacks");
      if (!VerifyHandlerCallBacks("client", clientSideMsgs)) {
        TestUtil.logErr("ClientSide Handler CallBacks (incorrect)");
        pass = false;
      } else {
        TestUtil.logMsg("ClientSide Handler CallBacks (correct)");
      }

      TestUtil.logMsg("-----------------------------------");
      TestUtil.logMsg("Dumping ServerSide Handler messages");
      TestUtil.logMsg("-----------------------------------");
      TestUtil.logMsg(serverSideMsgs);
      TestUtil.logMsg("Verify server side handler callbacks");
      if (!VerifyHandlerCallBacks("server", serverSideMsgs)) {
        TestUtil.logErr("ServerSide Handler CallBacks (incorrect)");
        pass = false;
      } else {
        TestUtil.logMsg("ServerSide Handler CallBacks (correct)");
      }

      if (!pass)
        throw new Fault("WSEjbSOAPHandlersApplicationClientTest failed");
    } catch (Throwable t) {
      throw new Fault("WSEjbSOAPHandlersApplicationClientTest failed");
    }
  }

  /*
   * @testName: WSEjbSOAPHandlersServletClientTest
   *
   * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42;
   * WS4EE:SPEC:43; WS4EE:SPEC:44; WS4EE:SPEC:51; WS4EE:SPEC:109;
   * WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155;
   * WS4EE:SPEC:171; WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4002;
   * WS4EE:SPEC:115; WS4EE:SPEC:213; WS4EE:SPEC:187; WS4EE:SPEC:6021;
   * WS4EE:SPEC:6022; WS4EE:SPEC:6024; WS4EE:SPEC:6026; WS4EE:SPEC:6029;
   * WS4EE:SPEC:6035; WS4EE:SPEC:6038; WS4EE:SPEC:6048; WS4EE:SPEC:6049;
   * WS4EE:SPEC:6009; WS4EE:SPEC:6010; WS4EE:SPEC:6011; WS4EE:SPEC:6023;
   *
   * @test_Strategy: This is a prebuilt client and prebuilt webservice using EJB
   * endpoint. Tests @WebServiceRef, @WebService, and @HandlerChain annotations.
   * Tests client-side and server-side handlers and callbacks. Deploy via full
   * 109 deployment descriptors. The
   * 
   * @WebserviceRef annotation on the client must be overridden by the
   * deployment descriptors. The @HandleChain annotation does not have to be
   * specified. The @WebserviceRef and if present the @HandlerChain annotations
   * are ignored. Everything is obtained from the deployment descriptors so the
   * annotations are ignored. Verifies that override via Deployment Descriptors
   * works. Tests the web container.
   */
  public void WSEjbSOAPHandlersServletClientTest() throws Fault {
    TestUtil.logMsg("WSEjbSOAPHandlersServletClientTest");
    try {
      TestUtil.logMsg("Invoke the webservice endpoint from Web container");
      TestUtil.logMsg("Create URL to WebClient (ServletClient)");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      TestUtil.logMsg("Invoke the WebClient (ServletClient)");
      TestUtil.logMsg("Get test result from WebClient (ServletClient)");
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail"))
        throw new Fault("WSEjbSOAPHandlersServletClientTest failed");
    } catch (Throwable t) {
      throw new Fault("WSEjbSOAPHandlersServletClientTest failed");
    }
  }

  private boolean VerifyHandlerCallBacks(String who, String calls) {
    int startIdx = 0;
    int endIdx = 0;
    boolean pass = true;

    if (who.equals("client")) {
      if (calls == null) {
        TestUtil.logErr("Callback string is null (unexpected)");
        return false;
      }
      if (calls.indexOf("ClientHandler1.handleMessage().doInbound()") == -1) {
        TestUtil.logErr(
            "ClientHandler1.handleMessage().doInbound() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler2.handleMessage().doInbound()") == -1) {
        TestUtil.logErr(
            "ClientHandler2.handleMessage().doInbound() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler1.handleMessage().doOutbound()") == -1) {
        TestUtil.logErr(
            "ClientHandler1.handleMessage().doOutbound() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler2.handleMessage().doOutbound()") == -1) {
        TestUtil.logErr(
            "ClientHandler2.handleMessage().doOutbound() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler1.close()") == -1) {
        TestUtil.logErr("ClientHandler1.close() was not called");
        pass = false;
      }
      if (calls.indexOf("ClientHandler2.close()") == -1) {
        TestUtil.logErr("ClientHandler2.close() was not called");
        pass = false;
      }
    } else {
      if (calls == null) {
        TestUtil.logErr("Callback string is null (unexpected)");
        return false;
      }
      if (calls.indexOf("ServerHandler1.handleMessage().doInbound()") == -1) {
        TestUtil.logErr(
            "ServerHandler1.handleMessage().doInbound() was not called");
        pass = false;
      }
      if (calls.indexOf("ServerHandler2.handleMessage().doInbound()") == -1) {
        TestUtil.logErr(
            "ServerHandler2.handleMessage().doInbound() was not called");
        pass = false;
      }
      if (calls.indexOf("ServerHandler1.handleMessage().doOutbound()") == -1) {
        TestUtil.logErr(
            "ServerHandler1.handleMessage().doOutbound() was not called");
        pass = false;
      }
      if (calls.indexOf("ServerHandler2.handleMessage().doOutbound()") == -1) {
        TestUtil.logErr(
            "ServerHandler2.handleMessage().doOutbound() was not called");
        pass = false;
      }
      if (calls.indexOf("ServerHandler1.close()") == -1) {
        TestUtil.logErr("ServerHandler1.close() was not called");
        pass = false;
      }
      if (calls.indexOf("ServerHandler2.close()") == -1) {
        TestUtil.logErr("ServerHandler2.close() was not called");
        pass = false;
      }
    }
    return pass;
  }
}
