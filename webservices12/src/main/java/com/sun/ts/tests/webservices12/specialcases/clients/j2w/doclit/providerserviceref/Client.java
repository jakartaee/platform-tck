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

package com.sun.ts.tests.webservices12.specialcases.clients.j2w.doclit.providerserviceref;

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

import endpoint.jaxws.*;

public class Client extends ServiceEETest {
  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "specialcases.providerserviceref.endpoint";

  private static final String WSDLLOC_URL = "specialcases.providerserviceref.wsdlloc";

  private String url = null;

  private URL wsdlurl = null;

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  Hello port = null;

  HelloService service;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getPort() throws Exception {
    TestUtil.logMsg(
        "Get Service via WebServiceRef(name=\"nameattribute\") annotation");
    TestUtil.logMsg("service=" + service);
    TestUtil.logMsg("Get port from service");
    port = (Hello) service.getHelloPort();
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Port obtained");
    JAXWS_Util.dumpTargetEndpointAddress(port);
    // JAXWS_Util.setTargetEndpointAddress(port, url);
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
   * @class.setup_props: webServerHost; webServerPort; platform.mode;
   */
  public void setup(String[] args, Properties p) throws Exception {
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
      TestUtil.logMsg("Get WebServiceRef from specific vehicle");
      service = (HelloService) getSharedObject();
      getTestURLs();
      getPort();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new Exception("setup failed:", e);
    }

    if (!pass) {
      TestUtil.logErr(
          "Please specify host & port of web server " + "in config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Exception("setup failed:");
    }
    logMsg("setup ok");
  }

  public void cleanup() throws Exception {
    logMsg("cleanup ok");
  }

  /*
   * @testName: SpecialCasesJ2wProviderServiceRefTest
   *
   * @assertion_ids: WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5001;
   * WS4EE:SPEC:5002;
   *
   * @test_Strategy: Client imports wsdl from a deployed webservice endpoint,
   * builds the client-side artifacts, then uses the WebServiceRef annotation
   * with name attribute to access and communicate with the deployed webservice
   * endpoint.
   */
  public void SpecialCasesJ2wProviderServiceRefTest() throws Exception {
    TestUtil.logMsg("SpecialCasesJ2wProviderServiceRefTest");
    boolean pass = true;

    for (int i = 0; i < 10; i++) {
      String ret = port.sayHello("ProviderRef Tester loop#" + i + "!");
      if (ret.indexOf("WebSvcProviderTest-SayHello") == -1) {
        TestUtil.logErr("Unexpected greeting " + ret);
        pass = false;
        break;
      } else
        TestUtil.logMsg("Got expected greeting " + ret);
    }
    if (!pass)
      throw new Exception("SpecialCasesJ2wProviderServiceRefTest failed");
  }
}
