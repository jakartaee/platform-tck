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
 * $Id: Client.java 53493 2007-05-22 17:06:35Z adf $
 */

package com.sun.ts.tests.jaxws.ee.j2w.document.literal.webmethod.client;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;

import jakarta.xml.ws.*;
import javax.xml.namespace.QName;

import java.util.*;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.common.*;

import javax.naming.InitialContext;

public class Client extends ServiceEETest {
  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.j2w.document.literal.webmethod.client.";

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "j2wdlwebmethod.endpoint.1";

  private static final String WSDLLOC_URL = "j2wdlwebmethod.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  // ServiceName and PortName mapping configuration going java-to-wsdl
  private static final String SERVICE_NAME = "TestService";

  private static final String PORT_NAME = "TestPort";

  private static final String NAMESPACEURI = "http://test.org/wsdl";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  Test port = null;

  static TestService service = null;

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
    port = (Test) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME, TestService.class,
        PORT_QNAME, Test.class);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (Test) service.getPort(Test.class);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
    getTargetEndpointAddress(port);
    // JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getTargetEndpointAddress(Object port) throws Exception {
    TestUtil.logMsg("Get Target Endpoint Address for port=" + port);
    String url = JAXWS_Util.getTargetEndpointAddress(port);
    TestUtil.logMsg("Target Endpoint Address=" + url);
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
        getPortStandalone();
      } else {
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (TestService) getSharedObject();
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

  private void assertEquals(String s1, String s2, String m) throws Fault {
    TestUtil.logMsg("assert method exists for " + m);
    if (!s1.equals(s2))
      throw new Fault("" + m);
    else
      TestUtil.logMsg("method exists for " + m);
  }

  private void assertMethodNonExistant(String m) throws Fault {
    TestUtil.logMsg("assert method does not exist for " + m);
    try {
      port.getClass().getMethod(m, new Class[] { String.class });
      throw new Fault("" + m + " should not be a Web Method");
    } catch (NoSuchMethodException e) {
      TestUtil.logMsg("method does not exist for " + m);
    }
  }

  /*
   * @testName: webMethodTestMapping
   *
   * @assertion_ids: JAXWS:SPEC:3010; JAXWS:SPEC:3011; JAXWS:SPEC:3006;
   * JAXWS:SPEC:3036;
   *
   * @test_Strategy:
   *
   * Description
   */
  public void webMethodTestMapping() throws Fault {
    TestUtil.logMsg("webMethodTestMapping");
    boolean pass = true;

    try {
      // Methods (method1,method3,method4,method5,overridetoString) should exist
      assertEquals("foo", port.method1("foo"),
          "webMethodTestMapping: port.method1");
      assertEquals("foo", port.method3("foo"),
          "webMethodTestMapping: port.method3");
      assertEquals("foo", port.method4("foo"),
          "webMethodTestMapping: port.method4");
      assertEquals("foo", port.method5("foo"),
          "webMethodTestMapping: port.method5");
      assertEquals("TestImplBaseBase", port.overridetoString(),
          "webMethodTestMapping: port.overridetoString");

      // Methods (method2,method6-9) should not exist
      assertMethodNonExistant("webMethodTestMapping: port.method2");
      assertMethodNonExistant("webMethodTestMapping: port.method6");
      assertMethodNonExistant("webMethodTestMapping: port.method7");
      assertMethodNonExistant("webMethodTestMapping: port.method8");
      assertMethodNonExistant("webMethodTestMapping: port.method9");
      assertMethodNonExistant("webMethodTestMapping: port.method10");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("webMethodTestMapping failed", e);
    }
  }

}
