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
 *  $Id$
 */
package com.sun.ts.tests.jaxws.wsa.w2j.document.literal.providertest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.*;

import jakarta.xml.ws.*;
import java.util.Properties;
import com.sun.ts.tests.jaxws.common.*;

import javax.xml.namespace.QName;
import jakarta.xml.bind.JAXBContext;

import jakarta.xml.ws.soap.AddressingFeature;
import jakarta.xml.ws.soap.MTOMFeature;
import jakarta.xml.ws.RespectBindingFeature;
import jakarta.xml.ws.WebServiceFeature;

public class Client extends ServiceEETest {

  private static final ObjectFactory of = new ObjectFactory();

  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  // ServiceName and PortName mapping configuration going java-to-wsdl
  private static final String SERVICE_NAME = "ProviderTestService";

  private static final String PORT_NAME = "ProviderTestPort";

  private static final String PORT_TYPE_NAME = "ProviderTest";

  private static final String INPUT_MSG_NAME = "helloRequest";

  private static final String NAMESPACEURI = "http://providertestservice.org/wsdl";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  // URL properties used by the test
  private static final String ENDPOINT_URL = "providertest.endpoint.1";

  private static final String WSDLLOC_URL = "providertest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  ProviderTest port = null;

  private WebServiceFeature[] wsf = { new AddressingFeature(true),
      new MTOMFeature(true), new RespectBindingFeature(true) };

  static ProviderTestService service = null;

  private TSURL ctsurl = new TSURL();

  private Dispatch<Object> dispatchJaxb = null;

  private static final Class SERVICE_CLASS = com.sun.ts.tests.jaxws.wsa.w2j.document.literal.providertest.ProviderTestService.class;

  private static final Class JAXB_OBJECT_FACTORY = com.sun.ts.tests.jaxws.wsa.w2j.document.literal.providertest.ObjectFactory.class;

  private JAXBContext createJAXBContext() {
    try {
      return JAXBContext.newInstance(JAXB_OBJECT_FACTORY);
    } catch (jakarta.xml.bind.JAXBException e) {
      throw new WebServiceException(e.getMessage(), e);
    }
  }

  private Dispatch<Object> createDispatchJAXB() throws Exception {
    return service.createDispatch(PORT_QNAME, createJAXBContext(),
        jakarta.xml.ws.Service.Mode.PAYLOAD, wsf);
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

  private void getPortJavaEE() throws Exception {
    port = (ProviderTest) service.getPort(ProviderTest.class, wsf);
    TestUtil.logMsg("port=" + port);
  }

  private void getPortStandalone() throws Exception {
    port = (ProviderTest) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        ProviderTestService.class, PORT_QNAME, ProviderTest.class, wsf);
    JAXWS_Util.setTargetEndpointAddress(port, url);
    service = (ProviderTestService) JAXWS_Util.getService(wsdlurl,
        SERVICE_QNAME, SERVICE_CLASS);
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
        service = (ProviderTestService) getSharedObject();
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
   * @testName: WebServiceFeaturesOnProviderTest
   *
   * @assertion_ids: JAXWS:SPEC:6017; JAXWS:SPEC:5025; JAXWS:JAVADOC:189
   *
   * @test_Strategy: enable the webservice features on the impl then ensure the
   * endpoint can be reached
   */
  public void WebServiceFeaturesOnProviderTest() throws Fault {
    TestUtil.logMsg("WebServiceFeaturesOnProviderTest");
    boolean pass = true;
    HelloRequest helloReq = null;
    try {
      helloReq = of.createHelloRequest();
      helloReq.setArgument("WebServiceFeaturesOnProviderTest");
    } catch (Exception e) {
      e.printStackTrace();
    }
    HelloResponse helloRes = null;
    try {
      dispatchJaxb = createDispatchJAXB();
      dispatchJaxb.invoke(helloReq);
    } catch (Throwable t) {
      t.printStackTrace();
      throw new Fault(t.toString());
    }

    if (!pass)
      throw new Fault("WebServiceFeaturesOnProviderTest failed");
  }

}
