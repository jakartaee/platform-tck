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

package com.sun.ts.tests.jaxws.api.jakarta_xml_ws_handler.PortInfo;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.*;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.*;
import jakarta.xml.ws.soap.*;
import javax.xml.namespace.QName;
import jakarta.xml.ws.handler.*;

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

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.api.jakarta_xml_ws_handler.PortInfo.";

  private static final String NAMESPACEURI = "http://helloservice.org/wsdl";

  private static final String SERVICE_NAME = "HelloService";

  private static final String PORT_NAME = "HelloPort";

  private static final QName SERVICE_QNAME = new QName(NAMESPACEURI,
      SERVICE_NAME);

  private static final QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private static final Class SERVICE_CLASS = com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.HelloService.class;

  private static final Class SEI_CLASS = com.sun.ts.tests.jaxws.sharedclients.doclithelloclient.Hello.class;

  private static final String BINDING_ID = SOAPBinding.SOAP11HTTP_BINDING;

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private static PortInfo pinfo = null;

  static HelloService service = null;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "dlhelloservice.endpoint.1";

  private static final String WSDLLOC_URL = "dlhelloservice.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

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
   * @testName: PortInfoTest
   *
   * @assertion_ids: JAXWS:JAVADOC:96; JAXWS:JAVADOC:97; JAXWS:JAVADOC:98;
   *
   * @test_Strategy:
   */
  public void PortInfoTest() throws Fault {
    TestUtil.logTrace("PortInfoTest");
    boolean pass = true;
    try {
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        service = (HelloService) JAXWS_Util.getService(wsdlurl, SERVICE_QNAME,
            SERVICE_CLASS);
      } else {
        getTestURLs();
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (HelloService) getSharedObject();
      }
      service.setHandlerResolver(new HandlerResolver() {
        public List<Handler> getHandlerChain(PortInfo info) {
          TestUtil.logMsg("BindingID=" + info.getBindingID());
          TestUtil.logMsg("ServiceName=" + info.getServiceName());
          TestUtil.logMsg("PortName=" + info.getPortName());
          pinfo = info;
          return new ArrayList<Handler>();
        }
      });
      Hello port = (Hello) service.getPort(PORT_QNAME, SEI_CLASS);
      BindingProvider bp = (BindingProvider) port;
      Binding b = bp.getBinding();
      List<Handler> hl = b.getHandlerChain();
      TestUtil.logMsg("HandlerChainList=" + hl);
      TestUtil.logMsg("HandlerChainSize = " + hl.size());
      TestUtil.logMsg("ServiceName check -> " + pinfo.getServiceName());
      if (!pinfo.getServiceName().equals(SERVICE_QNAME)) {
        TestUtil.logErr("ServiceName mismatch, expected: " + SERVICE_QNAME
            + ", received: " + pinfo.getServiceName());
        pass = false;
      }
      TestUtil.logMsg("PortName check -> " + pinfo.getPortName());
      if (!pinfo.getPortName().equals(PORT_QNAME)) {
        TestUtil.logErr("PortName mismatch, expected: " + PORT_QNAME
            + ", received: " + pinfo.getPortName());
        pass = false;
      }
      TestUtil.logMsg("BindingID check -> " + pinfo.getBindingID());
      if (!pinfo.getBindingID().equals(BINDING_ID)) {
        TestUtil.logErr("BindingID mismatch, expected: " + BINDING_ID
            + ", received: " + pinfo.getBindingID());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("PortInfoTest failed", e);
    }

    if (!pass)
      throw new Fault("PortInfoTest failed");
  }
}
