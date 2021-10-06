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
 * $Id: Client.java 52695 2007-02-13 14:24:45Z sdimilla $
 */
package com.sun.ts.tests.jaxws.wsa.w2j.document.literal.addressingfeature;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPException;
import jakarta.xml.ws.soap.SOAPFaultException;

import com.sun.ts.tests.jaxws.wsa.common.ActionNotSupportedException;

import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsa.common.W3CAddressingConstants;
import com.sun.ts.tests.jaxws.wsa.common.WsaSOAPUtils;

import java.net.URL;

import jakarta.xml.ws.*;
import jakarta.xml.soap.*;
import jakarta.xml.ws.soap.AddressingFeature;

import java.util.Properties;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.wsa.w2j.document.literal.addressingfeature.";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL1 = "wsaw2jdladdressingfeature.endpoint.1";

  private static final String ENDPOINT_URL2 = "wsaw2jdladdressingfeature.endpoint.2";

  private static final String ENDPOINT_URL3 = "wsaw2jdladdressingfeature.endpoint.3";

  private static final String WSDLLOC_URL = "wsaw2jdladdressingfeature.wsdlloc.1";

  // service and port information
  private static final String NAMESPACEURI = "http://addressingfeatureservice.org/wsdl";

  private static final String SERVICE_NAME = "AddressingFeatureTestService";

  private static final String PORT_NAME1 = "AddressingFeatureTest1Port";

  private static final String PORT_NAME2 = "AddressingFeatureTest2Port";

  private static final String PORT_NAME3 = "AddressingFeatureTest3Port";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME1 = new QName(NAMESPACEURI, PORT_NAME1);

  private QName PORT_QNAME2 = new QName(NAMESPACEURI, PORT_NAME2);

  private QName PORT_QNAME3 = new QName(NAMESPACEURI, PORT_NAME3);

  private String url1 = null;

  private String url2 = null;

  private String url3 = null;

  private URL wsdlurl = null;

  private String ctxroot = null;

  private AddressingFeatureTest1 port1a = null;

  private AddressingFeatureTest1 port1b = null;

  private AddressingFeatureTest1 port1c = null;

  private AddressingFeatureTest2 port2a = null;

  private AddressingFeatureTest2 port2b = null;

  private AddressingFeatureTest2 port2c = null;

  private AddressingFeatureTest3 port3a = null;

  private AddressingFeatureTest3 port3b = null;

  private WebServiceFeature[] nonEnabledwsf = { new AddressingFeature(false) };

  private WebServiceFeature[] enabledRequiredwsf = {
      new AddressingFeature(true, true) };

  private WebServiceFeature[] enabledNotRequiredwsf = {
      new AddressingFeature(true, false) };

  static AddressingFeatureTestService service = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL1);
    url1 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);

    file = JAXWS_Util.getURLFromProp(ENDPOINT_URL2);
    url2 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(ENDPOINT_URL3);
    url3 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL1: " + url1);
    TestUtil.logMsg("Service Endpoint URL2: " + url2);
    TestUtil.logMsg("Service Endpoint URL3: " + url3);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  private void getPortStandalone() throws Exception {
    TestUtil.logMsg(
        "******************************Retrieving Port 1************************\n");
    // client side Addressing enabled/NotRequired; server side
    // Addressing/NotRequired
    port1a = (AddressingFeatureTest1) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        AddressingFeatureTestService.class, PORT_QNAME1,
        AddressingFeatureTest1.class, enabledNotRequiredwsf);
    JAXWS_Util.setTargetEndpointAddress(port1a, url1);

    TestUtil.logMsg(
        "******************************Retrieving Port 2************************\n");
    // client side Addressing enabled/Required; server side
    // Addressing/NotRequired
    port1b = (AddressingFeatureTest1) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        AddressingFeatureTestService.class, PORT_QNAME1,
        AddressingFeatureTest1.class, enabledRequiredwsf);
    JAXWS_Util.setTargetEndpointAddress(port1b, url1);

    TestUtil.logMsg(
        "******************************Retrieving Port 3************************\n");
    // client side Addressing off; server side Addressing/NotRequired
    port1c = (AddressingFeatureTest1) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        AddressingFeatureTestService.class, PORT_QNAME1,
        AddressingFeatureTest1.class, nonEnabledwsf);
    JAXWS_Util.setTargetEndpointAddress(port1c, url1);

    TestUtil.logMsg(
        "******************************Retrieving Port 4************************\n");
    // client side Addressing enabled/NotRequired; server side
    // Addressing/Required
    port2a = (AddressingFeatureTest2) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        AddressingFeatureTestService.class, PORT_QNAME2,
        AddressingFeatureTest2.class, enabledNotRequiredwsf);
    JAXWS_Util.setTargetEndpointAddress(port2a, url2);

    TestUtil.logMsg(
        "******************************Retrieving Port 5************************\n");
    // client side Addressing enabled/Required; server side Addressing/Required
    port2b = (AddressingFeatureTest2) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        AddressingFeatureTestService.class, PORT_QNAME2,
        AddressingFeatureTest2.class, enabledRequiredwsf);
    JAXWS_Util.setTargetEndpointAddress(port2b, url2);

    TestUtil.logMsg(
        "******************************Retrieving Port 6************************\n");
    // client side Addressing off; server side Addressing/Required
    port2c = (AddressingFeatureTest2) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        AddressingFeatureTestService.class, PORT_QNAME2,
        AddressingFeatureTest2.class, nonEnabledwsf);
    JAXWS_Util.setTargetEndpointAddress(port2c, url2);

    TestUtil.logMsg(
        "******************************Retrieving Port 7************************\n");
    // client side Addressing enabled/NotRequired; server side off (in WSDL
    // enabled, but overridden to false in IMPL to turn it off)
    port3a = (AddressingFeatureTest3) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        AddressingFeatureTestService.class, PORT_QNAME3,
        AddressingFeatureTest3.class, enabledNotRequiredwsf);
    JAXWS_Util.setTargetEndpointAddress(port3a, url3);

    TestUtil.logMsg(
        "******************************Retrieving Port 8************************\n");
    // client side Addressing enabled/Required; server side off (in WSDL
    // enabled, but overridden to false in IMPL to turn it off)
    port3b = (AddressingFeatureTest3) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        AddressingFeatureTestService.class, PORT_QNAME3,
        AddressingFeatureTest3.class, enabledRequiredwsf);
    JAXWS_Util.setTargetEndpointAddress(port3b, url3);
  }

  private void getPortJavaEE() throws Exception {
    try {
      TestUtil.logMsg("Obtain service via WebServiceRef annotation");
      TestUtil.logMsg("service=" + service);

      port1a = (AddressingFeatureTest1) service
          .getPort(AddressingFeatureTest1.class, enabledNotRequiredwsf);
      JAXWS_Util.dumpTargetEndpointAddress(port1a);
      JAXWS_Util.setSOAPLogging(port1a);
      port1b = (AddressingFeatureTest1) service
          .getPort(AddressingFeatureTest1.class, enabledRequiredwsf);
      JAXWS_Util.dumpTargetEndpointAddress(port1b);
      JAXWS_Util.setSOAPLogging(port1b);
      port1c = (AddressingFeatureTest1) service
          .getPort(AddressingFeatureTest1.class, nonEnabledwsf);
      JAXWS_Util.dumpTargetEndpointAddress(port1c);
      JAXWS_Util.setSOAPLogging(port1c);

      port2a = (AddressingFeatureTest2) service
          .getPort(AddressingFeatureTest2.class, enabledNotRequiredwsf);
      JAXWS_Util.dumpTargetEndpointAddress(port2a);
      JAXWS_Util.setSOAPLogging(port2a);
      port2b = (AddressingFeatureTest2) service
          .getPort(AddressingFeatureTest2.class, enabledRequiredwsf);
      JAXWS_Util.dumpTargetEndpointAddress(port2b);
      JAXWS_Util.setSOAPLogging(port2b);
      port2c = (AddressingFeatureTest2) service
          .getPort(AddressingFeatureTest2.class, nonEnabledwsf);
      JAXWS_Util.dumpTargetEndpointAddress(port2c);
      JAXWS_Util.setSOAPLogging(port2c);

      port3a = (AddressingFeatureTest3) service
          .getPort(AddressingFeatureTest3.class, enabledNotRequiredwsf);
      JAXWS_Util.dumpTargetEndpointAddress(port3a);
      JAXWS_Util.setSOAPLogging(port3a);
      port3b = (AddressingFeatureTest3) service
          .getPort(AddressingFeatureTest3.class, enabledRequiredwsf);
      JAXWS_Util.dumpTargetEndpointAddress(port3b);
      JAXWS_Util.setSOAPLogging(port3b);

    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }

    // debug dumping of ports
    Object[] portsTodump = new Object[] { port1a, port1b, port1c, port2a,
        port2b, port2c, port3a, port3b };
    dumpTargetEndpointAddressForPort(portsTodump);
  }

  private void dumpTargetEndpointAddressForPort(Object[] portsTodump) {
    try {
      for (int i = 0; i < portsTodump.length; i++) {
        TestUtil.logMsg("port=" + portsTodump[i]);
        TestUtil.logMsg("Obtained port" + i);
        JAXWS_Util.dumpTargetEndpointAddress(portsTodump[i]);
      }
    } catch (java.lang.Exception e) {
      TestUtil.printStackTrace(e);
      TestUtil.logErr("Error dumping EndpointAddress for port");
    }
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
      if (hostname == null) {
        pass = false;
      } else if (hostname.equals("")) {
        pass = false;
      }

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
        TestUtil.logMsg("WebServiceRef is not set in Client "
            + "(get it from specific vehicle)");
        service = (AddressingFeatureTestService) getSharedObject();
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
   * @testName: afClientEnabledNotREQServerEnabledNotREQTest
   *
   * @assertion_ids: WSAMD:SPEC:3001.1; WSAMD:SPEC:3001.2; WSAMD:SPEC:3001.4;
   * JAXWS:SPEC:6011; JAXWS:SPEC:6011.2; JAXWS:SPEC:6012; JAXWS:SPEC:6012.1;
   * JAXWS:SPEC:6012.2; JAXWS:SPEC:6012.4; JAXWS:SPEC:6012.5; JAXWS:SPEC:7020;
   * JAXWS:SPEC:7020.2; JAXWS:SPEC:10025; JAXWS:JAVADOC:190;
   *
   * @test_Strategy: Test Addressing Feature. Client enabled/NotRequired, Server
   * enabled/NotRequired. Addressing headers MAY be present on SOAPRequest and
   * SOAPResponse since Addressing is Optional. If addressing headers exist
   * check them otherwise don't.
   */
  public void afClientEnabledNotREQServerEnabledNotREQTest() throws Fault {
    TestUtil.logMsg("afClientEnabledNotREQServerEnabledNotREQTest");
    TestUtil.logMsg(
        "Verify Addressing headers may be present on SOAPRequest and SOAPResponse");
    boolean pass = true;
    try {
      port1a.addNumbers(new Holder("ClientEnabledNotREQServerEnabledNotREQ"),
          10, 10);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("afClientEnabledNotREQServerEnabledNotREQTest failed", e);
    }

    if (!pass) {
      throw new Fault("afClientEnabledNotREQServerEnabledNotREQTest failed");
    }
  }

  /*
   * @testName: afClientEnabledREQServerEnabledNotREQTest
   *
   * @assertion_ids: WSAMD:SPEC:3001.1; WSAMD:SPEC:3001.2; WSAMD:SPEC:3001.4;
   * JAXWS:SPEC:6011; JAXWS:SPEC:6011.2; JAXWS:SPEC:6012; JAXWS:SPEC:6012.1;
   * JAXWS:SPEC:6012.2; JAXWS:SPEC:6012.4; JAXWS:SPEC:6012.5; JAXWS:SPEC:7020;
   * JAXWS:SPEC:7020.2; JAXWS:SPEC:10025; JAXWS:SPEC:4031; JAXWS:JAVADOC:190;
   *
   * @test_Strategy: Test Addressing Feature. Client enabled/Required, Server
   * enabled/NotRequired. Addressing headers MUST be present on SOAPRequest and
   * SOAPResponse.
   */
  public void afClientEnabledREQServerEnabledNotREQTest() throws Fault {
    TestUtil.logMsg("afClientEnabledREQServerEnabledNotREQTest");
    TestUtil.logMsg(
        "Verify Addressing headers are present on SOAPRequest and SOAPResponse");
    boolean pass = true;
    try {
      port1b.addNumbers(new Holder("ClientEnabledREQServerEnabledNotREQ"), 10,
          10);
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("afClientEnabledREQServerEnabledNotREQTest failed", e);
    }

    if (!pass) {
      throw new Fault("afClientEnabledREQServerEnabledNotREQTest failed");
    }
  }

  /*
   * @testName: afClientNotEnabledServerEnabledNotREQTest
   *
   * @assertion_ids: WSAMD:SPEC:3001.1; WSAMD:SPEC:3001.3; WSAMD:SPEC:3001.4;
   * JAXWS:SPEC:6011; JAXWS:SPEC:6011.2; JAXWS:SPEC:6012; JAXWS:SPEC:6012.1;
   * JAXWS:SPEC:6012.3; JAXWS:SPEC:6012.5; JAXWS:SPEC:7020; JAXWS:SPEC:4031;
   * JAXWS:SPEC:7020.2; JAXWS:SPEC:10025; JAXWS:JAVADOC:190;
   *
   * @test_Strategy: Test Addressing Feature. Client Not Enabled, Server
   * enabled/NotRequired. Addressing headers MUST not be present on SOAPRequest
   * and SOAPResponse.
   */
  public void afClientNotEnabledServerEnabledNotREQTest() throws Fault {
    TestUtil.logMsg("afClientNotEnabledServerEnabledNotREQTest");
    TestUtil.logMsg(
        "Verify Addressing headers are not present on SOAPRequest and SOAPResponse");
    boolean pass = true;
    try {
      port1c.addNumbers(new Holder("ClientNotEnabledServerEnabledNotREQ"), 10,
          10);
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("afClientNotEnabledServerEnabledNotREQTest failed", e);
    }

    if (!pass) {
      throw new Fault("afClientNotEnabledServerEnabledNotREQTest failed");
    }
  }

  /*
   * @testName: afClientEnabledNotREQServerEnabledREQTest
   *
   * @assertion_ids: WSAMD:SPEC:3001.1; WSAMD:SPEC:3001.2; WSAMD:SPEC:3001.4;
   * JAXWS:SPEC:6011; JAXWS:SPEC:6011.2; JAXWS:SPEC:6012; JAXWS:SPEC:6012.1;
   * JAXWS:SPEC:6012.2; JAXWS:SPEC:6012.4; JAXWS:SPEC:6012.5; JAXWS:SPEC:3046;
   *
   * @test_Strategy: Test Addressing Feature. Client enabled/NotRequired, Server
   * enabled/Required. If the Client does not send Addressing headers then the
   * Server MUST throw back a SOAP Fault with a MessageAddressingHeaderRequired
   * fault code since the Server mandates Addressing Required. If the Client
   * does send Addressing headers then they MUST be present on SOAPRequest and
   * SOAPResponse since the Server mandates requires addressing.
   */
  public void afClientEnabledNotREQServerEnabledREQTest() throws Fault {
    TestUtil.logMsg("afClientEnabledNotREQServerEnabledREQTest");
    TestUtil.logMsg(
        "Verify Addressing headers may be present on SOAPRequest and SOAPResponse");
    TestUtil.logMsg(
        "or a MessageAddressingHeaderRequired soap fault is thrown by endpoint");
    boolean pass = true;
    try {
      port2a.addNumbers(new Holder("ClientEnabledNotREQServerEnabledREQ"), 10,
          10);
    } catch (SOAPFaultException sfe) {
      try {
        TestUtil
            .logMsg("Caught expected SOAPFaultException: " + sfe.getMessage());
        TestUtil.logMsg("Verify the SOAPFault faultcode");
        TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(sfe));
        TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(sfe));
        if (WsaSOAPUtils.isMessageAddressingHeaderRequiredFaultCode(sfe)) {
          TestUtil.logMsg(
              "SOAPFault contains expected faultcode MessageAddressingHeaderRequired");
        } else {
          String faultcode = WsaSOAPUtils.getFaultCode(sfe);
          TestUtil.logErr("SOAPFault contains unexpected faultcode got: "
              + faultcode + ", expected: MessageAddressingHeaderRequired");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultString(sfe) == null) {
          TestUtil
              .logErr("The faultstring element MUST EXIST for SOAP 1.1 Faults");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultDetail(sfe) != null) {
          TestUtil.logErr("The faultdetail element MUST NOT EXIST for SOAP 1.1 "
              + "Faults related to header entries");
          pass = false;
        }
      } catch (Exception e2) {
        TestUtil.logErr("Caught unexpected exception: " + e2.getMessage());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("afClientEnabledNotREQServerEnabledREQTest failed", e);
    }

    if (!pass) {
      throw new Fault("afClientEnabledNotREQServerEnabledREQTest failed");
    }
  }

  /*
   * @testName: afClientEnabledREQServerEnabledREQTest
   *
   * @assertion_ids: WSAMD:SPEC:3001.1; WSAMD:SPEC:3001.2; WSAMD:SPEC:3001.4;
   * JAXWS:SPEC:6011; JAXWS:SPEC:6011.2; JAXWS:SPEC:6012; JAXWS:SPEC:6012.1;
   * JAXWS:SPEC:6012.2; JAXWS:SPEC:6012.4; JAXWS:SPEC:6012.5; JAXWS:SPEC:3046;
   *
   * @test_Strategy: Test Addressing Feature. Client enabled/Required, Server
   * enabled/Required. Addressing headers MUST be present on SOAPRequest and
   * SOAPResponse.
   */
  public void afClientEnabledREQServerEnabledREQTest() throws Fault {
    TestUtil.logMsg("afClientEnabledREQServerEnabledREQTest");
    TestUtil.logMsg(
        "Verify Addressing headers are present on SOAPRequest and SOAPResponse");
    boolean pass = true;
    try {
      port2b.addNumbers(new Holder("ClientEnabledREQServerEnabledREQ"), 10, 10);
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("afClientEnabledREQServerEnabledREQTest failed", e);
    }

    if (!pass) {
      throw new Fault("afClientEnabledREQServerEnabledREQTest failed");
    }
  }

  /*
   * @testName: afClientNotEnabledServerEnabledREQTest
   *
   * @assertion_ids: WSAMD:SPEC:3001.1; WSAMD:SPEC:3001.3; WSAMD:SPEC:3001.4;
   * JAXWS:SPEC:4031; JAXWS:SPEC:6011; JAXWS:SPEC:6011.2; JAXWS:SPEC:6012;
   * JAXWS:SPEC:6012.1; JAXWS:SPEC:6012.3; JAXWS:SPEC:6012.5; JAXWS:SPEC:3046;
   * WSASB:SPEC:6004.3;
   *
   * @test_Strategy: Test Addressing Feature. Client Not Enabled, Server
   * enabled/Required. This scenario MUST throw back a SOAP Fault. Make sure the
   * SOAP Fault has the correct information in it. The SOAP Fault faultcode must
   * be: MessageAddressingHeaderRequired.
   */
  public void afClientNotEnabledServerEnabledREQTest() throws Fault {
    TestUtil.logMsg("afClientNotEnabledServerEnabledREQTest");
    TestUtil.logMsg(
        "Verify MessageAddressingHeaderRequired soap fault is thrown by endpoint");
    boolean pass = true;
    try {
      port2c.addNumbers(new Holder("ClientNotEnabledServerEnabledREQ"), 10, 10);
      TestUtil.logErr("SOAPFaultException was not thrown back");
      pass = false;
    } catch (SOAPFaultException sfe) {
      try {
        TestUtil
            .logMsg("Caught expected SOAPFaultException: " + sfe.getMessage());
        TestUtil.logMsg("Verify the SOAPFault faultcode");
        TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(sfe));
        TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(sfe));
        if (WsaSOAPUtils.isMessageAddressingHeaderRequiredFaultCode(sfe)) {
          TestUtil.logMsg(
              "SOAPFault contains expected faultcode MessageAddressingHeaderRequired");
        } else {
          String faultcode = WsaSOAPUtils.getFaultCode(sfe);
          TestUtil.logErr("SOAPFault contains unexpected faultcode got: "
              + faultcode + ", expected: MessageAddressingHeaderRequired");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultString(sfe) == null) {
          TestUtil
              .logErr("The faultstring element MUST EXIST for SOAP 1.1 Faults");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultDetail(sfe) != null) {
          TestUtil.logErr("The faultdetail element MUST NOT EXIST for SOAP 1.1 "
              + "Faults related to header entries");
          pass = false;
        }
      } catch (Exception e2) {
        TestUtil.logErr("Caught unexpected exception: " + e2.getMessage());
        throw new Fault("afClientNotEnabledServerEnabledREQTest failed", e2);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("afClientNotEnabledServerEnabledREQTest failed", e);
    }

    if (!pass) {
      throw new Fault("afClientNotEnabledServerEnabledREQTest failed");
    }
  }

  /*
   * @testName: afClientEnabledNotREQServerNotEnabledTest
   *
   * @assertion_ids: WSAMD:SPEC:3001.1; WSAMD:SPEC:3001.2; WSAMD:SPEC:3001.4;
   * JAXWS:SPEC:6012.2; JAXWS:SPEC:6012.3; JAXWS:SPEC:6012.4; JAXWS:SPEC:6012.6;
   * JAXWS:SPEC:6016.1; JAXWS:SPEC:7020; JAXWS:SPEC:7020.1; JAXWS:JAVADOC:191;
   *
   * @test_Strategy: Test Addressing Feature. Client enabled/NotRequired, Server
   * notenabled. Addressing headers MAY be present in the SOAPRequest but MUST
   * not be present in the SOAPResponse.
   */
  public void afClientEnabledNotREQServerNotEnabledTest() throws Fault {
    TestUtil.logMsg("afClientEnabledNotREQServerEnableNotREQTest");
    TestUtil
        .logMsg("Verify Addressing headers are NOT present on SOAPResponse");
    boolean pass = true;
    try {
      port3a.addNumbers(new Holder("ClientEnabledNotREQServerNotEnabled"), 10,
          10);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("afClientEnabledNotREQServerNotEnabledTest failed", e);
    }

    if (!pass) {
      throw new Fault("afClientEnabledNotREQServerNotEnabledTest failed");
    }
  }

  /*
   * @testName: afClientEnabledREQServerNotEnabledTest
   *
   * @assertion_ids: WSAMD:SPEC:3001.1; WSAMD:SPEC:3001.2; WSAMD:SPEC:3001.4;
   * JAXWS:SPEC:6012.2; JAXWS:SPEC:6012.3; JAXWS:SPEC:6012.4; JAXWS:SPEC:6012.6;
   * JAXWS:SPEC:6016.1; JAXWS:SPEC:7020; JAXWS:SPEC:7020.1; JAXWS:JAVADOC:191;
   *
   * @test_Strategy: Test Addressing Feature. Client enabled/Required, Server
   * not enabled. This scenario MUST throw back a WebServiceException.
   */
  public void afClientEnabledREQServerNotEnabledTest() throws Fault {
    TestUtil.logMsg("afClientEnabledREQServerNotEnabledTest");
    TestUtil.logMsg("Verify WebServiceException is thrown");
    boolean pass = true;
    try {
      port3b.addNumbers(new Holder("ClientEnabledREQServerNotEnabled"), 10, 10);
      TestUtil.logErr("WebServiceException was not thrown back");
      pass = false;
    } catch (WebServiceException e) {
      TestUtil.logMsg("Caught expected WebServiceException: " + e.getMessage());
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("afClientEnabledREQServerNotEnabledTest failed", e);
    }

    if (!pass) {
      throw new Fault("afClientEnabledREQServerNotEnabledTest failed");
    }
  }

}
