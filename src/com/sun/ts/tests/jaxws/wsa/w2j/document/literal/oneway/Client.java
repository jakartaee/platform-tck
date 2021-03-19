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
 * $Id: Client.java 52501 2007-01-24 02:29:49Z lschwenk $
 */

package com.sun.ts.tests.jaxws.wsa.w2j.document.literal.oneway;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPException;
import java.text.MessageFormat;

import com.sun.ts.tests.jaxws.wsa.common.ActionNotSupportedException;

import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.sharedclients.*;

import java.net.URL;

import jakarta.xml.ws.*;
import jakarta.xml.ws.soap.*;
import jakarta.xml.soap.*;

import java.util.Properties;
import java.util.UUID;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.wsa.w2j.document.literal.oneway.";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "wsaw2jdlonewaytest.endpoint.1";

  private static final String WSDLLOC_URL = "wsaw2jdlonewaytest.wsdlloc.1";

  private String url = null;

  // service and port information
  private static final String NAMESPACEURI = "http://example.com";

  private static final String SERVICE_NAME = "AddNumbersService";

  private static final String PORT_NAME = "AddNumbersPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private URL wsdlurl = null;

  private AddNumbersClient1 client1;

  AddNumbersPortType port = null;

  String noToHeaderSoapmsg = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header><Action xmlns=\"http://www.w3.org/2005/08/addressing\">http://example.com/AddNumbersPortType/add</Action></S:Header><S:Body><addNumbers xmlns=\"http://example.com\"><number1>10</number1><number2>10</number2></addNumbers></S:Body></S:Envelope>";

  String noActionHeaderSoapmsg = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header><To xmlns=\"http://www.w3.org/2005/08/addressing\">{0}</To></S:Header><S:Body><addNumbers xmlns=\"http://example.com\"><number1>10</number1><number2>10</number2></addNumbers></S:Body></S:Envelope>";

  static AddNumbersService service = null;

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
    port = (AddNumbersPortType) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        AddNumbersService.class, PORT_QNAME, AddNumbersPortType.class);
    TestUtil.logMsg("port=" + port);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (AddNumbersPortType) service.getAddNumbersPort();
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
    JAXWS_Util.dumpTargetEndpointAddress(port);
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
      client1 = (AddNumbersClient1) ClientFactory
          .getClient(AddNumbersClient1.class, p, this, service);
      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getTestURLs();
        getPortStandalone();
      } else {
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (AddNumbersService) getSharedObject();
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
   * @testName: testDefaultOneWayAction
   *
   * @assertion_ids: WSACORE:SPEC:3001; WSACORE:SPEC:3005; WSACORE:SPEC:3017;
   * WSACORE:SPEC:3022.2; WSACORE:SPEC:3022.2.1; WSACORE:SPEC:3022.2.2;
   * WSAMD:SPEC:5000
   *
   * @test_Strategy: Test default action pattern for WSDL input
   *
   */
  public void testDefaultOneWayAction() throws Fault {
    TestUtil.logMsg("testDefaultOneWayAction");
    boolean pass = true;

    try {
      port.addNumbers(10, 10);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("testDefaultOneWayAction failed", e);
    }

    if (!pass)
      throw new Fault("testDefaultOneWayAction failed");
  }

  /*
   * @testName: testExplicitOneWayAction
   *
   * @assertion_ids: WSACORE:SPEC:3001; WSACORE:SPEC:3005; WSACORE:SPEC:3009;
   * WSACORE:SPEC:3017; WSACORE:SPEC:3022.2; WSACORE:SPEC:3022.2.1;
   * WSACORE:SPEC:3022.2.2; WSAMD:SPEC:5000
   *
   * @test_Strategy: Test default action pattern for WSDL input
   *
   */
  public void testExplicitOneWayAction() throws Fault {
    TestUtil.logMsg("testExplicitOneWayAction");
    boolean pass = true;

    try {
      port.addNumbers2(10, 10);
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("testExplicitOneWayAction failed", e);
    }

    if (!pass)
      throw new Fault("testExplicitOneWayAction failed");
  }

  /*
   * @testName: noToHeaderOneWayTest
   *
   * @assertion_ids: WSASB:SPEC:6005; WSASB:SPEC:6006; WSASB:SPEC:6013;
   *
   * @test_Strategy: Send a message that doesn't contain wsa:To
   *
   */
  public void noToHeaderOneWayTest() throws Fault {
    TestUtil.logMsg("noToHeaderOneWayTest");
    boolean pass = true;

    SOAPMessage response = null;
    try {
      String soapmsg = noToHeaderSoapmsg;
      response = client1.makeSaajRequest(soapmsg);
      JAXWS_Util.dumpSOAPMessage(response, false);
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("noToHeaderOneWayTest failed", e);
    }
    if (!pass)
      throw new Fault("noToHeaderOneWayTest failed");
  }

  /*
   * @testName: noActionHeaderOneWayTest
   *
   * @assertion_ids: WSASB:SPEC:6005; WSASB:SPEC:6006; WSASB:SPEC:6013;
   *
   * @test_Strategy: Send a message that doesn't contain wsa:Action
   *
   */
  public void noActionHeaderOneWayTest() throws Fault {
    TestUtil.logMsg("noActionHeaderOneWayTest");
    boolean pass = true;

    String soapmsg = MessageFormat.format(noActionHeaderSoapmsg, url);
    SOAPMessage response = null;
    try {
      response = client1.makeSaajRequest(soapmsg);
      JAXWS_Util.dumpSOAPMessage(response, false);
    } catch (SOAPFaultException e) {
      TestUtil.logMsg("Caught expected SOAPFaultException: " + e.getMessage());
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("noActionHeaderOneWayTest failed", e);
    }
    if (!pass)
      throw new Fault("noActionHeaderOneWayTest failed");
  }

}
