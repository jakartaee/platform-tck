/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
 * $Id: Client.java 53493 2009-05-22 17:06:35Z adf $
 */

package com.sun.ts.tests.webservices13.servlet.WSWebServiceRefLookup.client;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;

import javax.ejb.EJB;
import javax.xml.ws.*;
import javax.xml.namespace.QName;

import java.util.*;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.common.*;

import javax.naming.InitialContext;

public class Client extends EETest {
  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  private static final String PKG_NAME = "com.sun.ts.tests.webservices13.servlet.WSWebServiceRefLookup.client.";

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "wswsreflookup.endpoint.1";

  private static final String WSDLLOC_URL = "wswsreflookup.wsdlloc.1";

  private String urlString = null;

  private URL wsdlurl = null;

  // ServiceName and PortName mapping configuration going java-to-wsdl
  private static final String SERVICE_NAME = "EchoService";

  private static final String PORT_NAME = "EchoPort";

  private static final String NAMESPACEURI = "http://echo.org/wsdl";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  // URL properties used by the test
  private URL url = null;

  private URLConnection urlConn = null;

  private Properties props = null;

  private String SERVLET = "/WSWSRefLookupClnt2_web/ServletTest";

  @WebServiceRef(name = "service/wswsreflookupservice")
  static EchoService service = null;

  Echo port = null;

  @WebServiceRef(lookup = "java:comp/env/service/wswsreflookupservice")
  static EchoService service2 = null;

  Echo port2 = null;

  @EJB(name = "ejb/WSWebServiceRefLookupClntBean")
  static EjbClientIF ejbclient;

  private void getPort() throws Exception {
    getTestURLs();
    TestUtil.logMsg("AppClient DEBUG: service=" + service);
    port = (Echo) service.getPort(Echo.class);
    TestUtil.logMsg("AppClient DEBUG: Obtained port");
    TestUtil.logMsg("AppClient DEBUG: port=" + port);
    getTargetEndpointAddress(port);
    TestUtil.logMsg("AppClient DEBUG: service2=" + service2);
    TestUtil.logMsg("AppClient DEBUG: Obtained port");
    port2 = (Echo) service2.getPort(Echo.class);
    TestUtil.logMsg("AppClient DEBUG: port2=" + port2);
    JAXWS_Util.setTargetEndpointAddress(port2, urlString);
    getTargetEndpointAddress(port2);
  }

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    urlString = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + urlString);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getTargetEndpointAddress(Object port) throws Exception {
    TestUtil.logMsg("Get Target Endpoint Address for port=" + port);
    String urlString = JAXWS_Util.getTargetEndpointAddress(port);
    TestUtil.logMsg("Target Endpoint Address=" + urlString);
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
    boolean pass = true;

    props = p;

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

      getPort();

      TestUtil.logMsg("AppClient DEBUG: ejbclient=" + ejbclient);
      TestUtil.logMsg("AppClient DEBUG: service=" + service);
      TestUtil.logMsg("AppClient DEBUG: port=" + port);
      TestUtil.logMsg("AppClient DEBUG: service2=" + service2);
      TestUtil.logMsg("AppClient DEBUG: port2=" + port2);

    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Fault("setup failed:", e);
    }

    if (service == null || service2 == null || port == null || port2 == null) {
      throw new Fault("setup failed: injection failure");
    }

    TestUtil.logMsg("setup() Endpoint urlString=" + urlString);
    p.setProperty("ENDPOINTURL", urlString);
    ejbclient.init(p);

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
   * @testName: testwsreflookup
   *
   * @assertion_ids: WS4EE:SPEC:4022;
   *
   * @test_Strategy: Test @WebServiceRef(lookup) method
   *
   * Description
   */
  public void testwsreflookup() throws Fault {
    TestUtil.logMsg("testwsreflookup");
    boolean pass = true;

    TestUtil.logMsg("-------------------------------");
    TestUtil.logMsg("Test appclient invocation .....");
    TestUtil.logMsg("-------------------------------");
    TestUtil.logMsg("AppClient invoking EchoService stringTest() method");
    if (!stringTest()) {
      pass = false;
      TestUtil.logErr("Failed in appclient");
    } else
      TestUtil.logMsg("Passed in appclient");
    try {
      TestUtil.logMsg("-----------------------------------");
      TestUtil.logMsg("Test servletclient invocation .....");
      TestUtil.logMsg("-----------------------------------");
      url = ctsurl.getURL("http", hostname, portnum, SERVLET);
      TestUtil.logMsg("Servlet URL: " + url);
      props.setProperty("TEST", "testwsreflookup");
      TestUtil.logMsg("Endpoint urlString=" + urlString);
      props.setProperty("ENDPOINTURL", urlString);
      urlConn = TestUtil.sendPostData(props, url);
      Properties p = TestUtil.getResponseProperties(urlConn);
      String passStr = p.getProperty("TESTRESULT");
      if (passStr.equals("fail")) {
        pass = false;
        TestUtil.logErr("Failed in servletclient");
      } else
        TestUtil.logMsg("Passed in servletclient");
    } catch (Exception e) {
      // e.printStackTrace();
      TestUtil.logErr("Failed on servletclient invocation");
      pass = false;
    }
    try {
      TestUtil.logMsg("-------------------------------");
      TestUtil.logMsg("Test ejbclient invocation .....");
      TestUtil.logMsg("-------------------------------");
      boolean passEjb = ejbclient.testwsreflookup();
      if (!passEjb) {
        pass = false;
        TestUtil.logErr("Failed in ejbclient");
      } else {
        TestUtil.logMsg("Passed in ejbclient");
      }
    } catch (Exception e) {
      // TestUtil.printStackTrace(e);
      TestUtil.logErr("Failed on ejbclient invocation");
      pass = false;
    }

    if (!pass)
      throw new Fault("testwsreflookup failed");
  }

  public boolean stringTest() throws Fault {
    TestUtil.logMsg("stringTest");
    boolean pass = true;
    String request = "Mary";

    try {
      String response = port2.echoString(request);
      if (!JAXWS_Data.compareValues(request, response, "String"))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("stringTest failed", e);
    }
    return pass;
  }
}
