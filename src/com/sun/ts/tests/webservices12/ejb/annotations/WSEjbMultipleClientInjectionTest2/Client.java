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

package com.sun.ts.tests.webservices12.ejb.annotations.WSEjbMultipleClientInjectionTest2;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;

import javax.xml.ws.*;
import javax.xml.namespace.QName;
import javax.naming.InitialContext;
import java.net.*;
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

  private URL url = null;

  private URLConnection urlConn = null;

  private String SERVLET = "/WSEjbMultipleClientInjectionTest2Clnt2_web/ServletTest";

  private Hello port;

  @WebServiceRef(name = "service/wsejbmultipleclientinjectiontest2")
  static HelloService service = null;

  private void getPort() throws Exception {
    TestUtil.logMsg(
        "Get wsejbmultipleclientinjectiontest2 Service via @WebServiceRef annotation");
    TestUtil.logMsg(
        "Uses name attribute @WebServiceRef(name=\"service/wsejbmultipleclientinjectiontest2\")");
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

  /*
   * @testName: MultipleClientInjectionMultipleClntEarsTest
   *
   * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42;
   * WS4EE:SPEC:43; WS4EE:SPEC:44; WS4EE:SPEC:51; WS4EE:SPEC:109;
   * WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155;
   * WS4EE:SPEC:171; WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4002;
   * WS4EE:SPEC:115; WS4EE:SPEC:213; WS4EE:SPEC:187;
   *
   * @test_Strategy: Test that injection works for multiple clients packaged in
   * separate client ear files. An appclient and servletclient is packaged in
   * separate client ear files. Injection should happen in both clients.
   */
  public void MultipleClientInjectionMultipleClntEarsTest() throws Fault {
    boolean pass = true;

    TestUtil.logMsg("MultipleClientInjectionMultipleClntEarsTest");
    TestUtil.logMsg("Test injection in both appclient and servletclient");
    try {
      TestUtil.logMsg("Test appclient injection .....");
      if (service == null) {
        TestUtil.logErr("appclient injection failed");
        pass = false;
      } else
        TestUtil.logMsg("appclient injection passed");
      TestUtil.logMsg("Test servletclient injection .....");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      props.setProperty("TEST", "test2");
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail")) {
        TestUtil.logErr("servletclient injection failed");
        pass = false;
      } else
        TestUtil.logMsg("servletclient injection passed");
    } catch (Throwable t) {
      throw new Fault("MultipleClientInjectionMultipleClntEarsTest failed");
    }
    if (!pass)
      throw new Fault("MultipleClientInjectionMultipleClntEarsTest failed");
  }

  /*
   * @testName: MultipleClientInjectionMultipleClntEarsInvokeHelloBye
   *
   * @assertion_ids: WS4EE:SPEC:37; WS4EE:SPEC:39; WS4EE:SPEC:41; WS4EE:SPEC:42;
   * WS4EE:SPEC:43; WS4EE:SPEC:44; WS4EE:SPEC:51; WS4EE:SPEC:109;
   * WS4EE:SPEC:145; WS4EE:SPEC:148; WS4EE:SPEC:149; WS4EE:SPEC:155;
   * WS4EE:SPEC:171; WS4EE:SPEC:184; WS4EE:SPEC:4000; WS4EE:SPEC:4002;
   * WS4EE:SPEC:115; WS4EE:SPEC:213; WS4EE:SPEC:187;
   *
   * @test_Strategy: Test that invocation works for multiple injection. Call
   * methods hello and bye.
   */
  public void MultipleClientInjectionMultipleClntEarsInvokeHelloBye()
      throws Fault {
    boolean pass = true;

    TestUtil.logMsg("MultipleClientInjectionMultipleClntEarsInvokeHelloBye");
    TestUtil.logMsg("Test injection in both appclient and servletclient");
    try {
      TestUtil.logMsg("Test appclient invocation .....");
      TestUtil.logMsg("Invoke hello method");
      String txt = port.hello("Hello there");
      if (txt.equals("Hello there to you too!"))
        TestUtil.logMsg("Invoke of hello passed");
      else {
        pass = false;
        TestUtil.logErr("Invoke of hello failed");
      }
      TestUtil.logMsg("Invoke bye method");
      txt = port.bye("Bye");
      if (txt.equals("Bye and take care!"))
        TestUtil.logMsg("Invoke of bye passed");
      else {
        pass = false;
        TestUtil.logErr("Invoke of bye failed");
      }
      TestUtil.logMsg("Test servletclient invocation .....");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      props.setProperty("TEST", "test2");
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail"))
        pass = false;
    } catch (Throwable t) {
      throw new Fault(
          "MultipleClientInjectionMultipleClntEarsInvokeHelloBye failed");
    }
    if (!pass)
      throw new Fault(
          "MultipleClientInjectionMultipleClntEarsInvokeHelloBye failed");
  }
}
