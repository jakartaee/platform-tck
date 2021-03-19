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

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.catalogtest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.*;
import javax.xml.namespace.QName;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.common.*;

import javax.naming.InitialContext;

public class Client extends ServiceEETest {
  private static final long serialVersionUID = 1L;

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.catalogtest.";

  // service and port information
  private static final String NAMESPACEURI = "http://catalogtestservice.org/wsdl";

  private static final String SERVICE_NAME = "CatalogTestService";

  private static final String PORT_NAME = "HelloPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "w2jdlcatalogtest.endpoint.1";

  private static final String WSDLLOC_URL = "w2jdlcatalogtest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  transient Hello port = null;

  static CatalogTestService service = null;

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
        getTestURLs();
      } else {
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (CatalogTestService) getSharedObject();
        getTestURLs();
      }
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
   * @testName: TestCatalogWithValidSystemIdAndValidURIValidWSDL
   *
   * @assertion_ids: JAXWS:SPEC:4020; WS4EE:SPEC:5007; WS4EE:SPEC:35;
   * WS4EE:SPEC:4014;
   *
   * @test_Strategy: Positive test case for oasis catalogs. Valid SystemId and
   * valid URI pointing to a valid WSDL. Must pass.
   */
  public void TestCatalogWithValidSystemIdAndValidURIValidWSDL() throws Fault {
    boolean pass = true;
    Iterator iterator = null;
    try {
      TestUtil.logMsg("TestCatalogWithValidSystemIdAndValidURIValidWSDL");
      TestUtil.logMsg("Get port via wsdl catalog with Valid URI/Valid WSDL");
      if (modeProperty.equals("standalone")) {
        port = (Hello) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
            CatalogTestService.class, PORT_QNAME, Hello.class);
        TestUtil.logMsg("port=" + port);
        JAXWS_Util.setTargetEndpointAddress(port, url);
      } else {
        TestUtil.logMsg("service=" + service);
        port = (Hello) service.getPort(Hello.class);
        TestUtil.logMsg("port=" + port);
        JAXWS_Util.dumpTargetEndpointAddress(port);
      }
      HelloRequest request = new HelloRequest();
      request.setString("Hello There!");
      HelloResponse response = port.hello(request);
      TestUtil.logMsg("response=" + response.getString());
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: test failed");
      TestUtil.logErr(
          "Invocation should have succeeded (Valid URI/Valid WSDL in catalog)");
      throw new Fault("TestCatalogWithValidSystemIdAndValidURIValidWSDL failed",
          e);
    }

    if (!pass)
      throw new Fault(
          "TestCatalogWithValidSystemIdAndValidURIValidWSDL failed");
  }
}
