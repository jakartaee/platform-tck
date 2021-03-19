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
package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.addressingfeature;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.wsa.j2w.document.literal.addressingfeature.";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL1 = "wsaj2wdladdressingfeaturetest.endpoint.1";

  private static final String ENDPOINT_URL2 = "wsaj2wdladdressingfeaturetest.endpoint.2";

  private static final String ENDPOINT_URL3 = "wsaj2wdladdressingfeaturetest.endpoint.3";

  private static final String ENDPOINT_URL4 = "wsaj2wdladdressingfeaturetest.endpoint.4";

  private static final String WSDLLOC_URL1 = "wsaj2wdladdressingfeaturetest.wsdlloc.1";

  private static final String WSDLLOC_URL2 = "wsaj2wdladdressingfeaturetest.wsdlloc.2";

  private static final String WSDLLOC_URL3 = "wsaj2wdladdressingfeaturetest.wsdlloc.3";

  private static final String WSDLLOC_URL4 = "wsaj2wdladdressingfeaturetest.wsdlloc.4";

  // service and port information
  private static final String NAMESPACEURI = "http://addressingfeatureservice.org/wsdl";

  private static final String SERVICE1_NAME = "AddressingFeatureTest1Service";

  private static final String SERVICE2_NAME = "AddressingFeatureTest2Service";

  private static final String SERVICE3_NAME = "AddressingFeatureTest3Service";

  private static final String SERVICE4_NAME = "AddressingFeatureTest4Service";

  private static final String PORT_NAME1 = "AddressingFeatureTest1Port";

  private static final String PORT_NAME2 = "AddressingFeatureTest2Port";

  private static final String PORT_NAME3 = "AddressingFeatureTest3Port";

  private static final String PORT_NAME4 = "AddressingFeatureTest4Port";

  private QName SERVICE1_QNAME = new QName(NAMESPACEURI, SERVICE1_NAME);

  private QName SERVICE2_QNAME = new QName(NAMESPACEURI, SERVICE2_NAME);

  private QName SERVICE3_QNAME = new QName(NAMESPACEURI, SERVICE3_NAME);

  private QName SERVICE4_QNAME = new QName(NAMESPACEURI, SERVICE4_NAME);

  private QName PORT_QNAME1 = new QName(NAMESPACEURI, PORT_NAME1);

  private QName PORT_QNAME2 = new QName(NAMESPACEURI, PORT_NAME2);

  private QName PORT_QNAME3 = new QName(NAMESPACEURI, PORT_NAME3);

  private QName PORT_QNAME4 = new QName(NAMESPACEURI, PORT_NAME4);

  private String url1 = null;

  private String url2 = null;

  private String url3 = null;

  private String url4 = null;

  private URL wsdlurl1 = null;

  private URL wsdlurl2 = null;

  private URL wsdlurl3 = null;

  private URL wsdlurl4 = null;

  private String ctxroot = null;

  private AddressingFeatureTest1 port1 = null;

  private AddressingFeatureTest2 port2 = null;

  private AddressingFeatureTest3 port3 = null;

  private AddressingFeatureTest4 port4 = null;

  private AddressingFeatureTest1 port5 = null;

  private AddressingFeatureTest2 port6 = null;

  private AddressingFeatureTest4 port7 = null;

  private WebServiceFeature[] enabledNotRequiredwsf = {
      new AddressingFeature(true, false) };

  private WebServiceFeature[] nonEnabledwsf = { new AddressingFeature(false) };

  private WebServiceFeature[] enabledRequiredwsf = {
      new AddressingFeature(true, true) };

  static AddressingFeatureTest1Service service1 = null;

  static AddressingFeatureTest2Service service2 = null;

  static AddressingFeatureTest3Service service3 = null;

  static AddressingFeatureTest4Service service4 = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXWS_Util.getURLFromProp(ENDPOINT_URL1);
    url1 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXWS_Util.getURLFromProp(WSDLLOC_URL1);
    wsdlurl1 = ctsurl.getURL(PROTOCOL, hostname, portnum, file);

    String file2 = JAXWS_Util.getURLFromProp(ENDPOINT_URL2);
    url2 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file2);
    file2 = JAXWS_Util.getURLFromProp(WSDLLOC_URL2);
    wsdlurl2 = ctsurl.getURL(PROTOCOL, hostname, portnum, file2);

    String file3 = JAXWS_Util.getURLFromProp(ENDPOINT_URL3);
    url3 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file3);
    file3 = JAXWS_Util.getURLFromProp(WSDLLOC_URL3);
    wsdlurl3 = ctsurl.getURL(PROTOCOL, hostname, portnum, file3);

    String file4 = JAXWS_Util.getURLFromProp(ENDPOINT_URL4);
    url4 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file4);
    file4 = JAXWS_Util.getURLFromProp(WSDLLOC_URL4);
    wsdlurl4 = ctsurl.getURL(PROTOCOL, hostname, portnum, file4);

    TestUtil.logMsg("Service Endpoint URL1: " + url1);
    TestUtil.logMsg("Service Endpoint URL2: " + url2);
    TestUtil.logMsg("Service Endpoint URL3: " + url3);
    TestUtil.logMsg("Service Endpoint URL4: " + url4);
    TestUtil.logMsg("WSDL Location URL1:    " + wsdlurl1);
    TestUtil.logMsg("WSDL Location URL2:    " + wsdlurl2);
    TestUtil.logMsg("WSDL Location URL3:    " + wsdlurl3);
    TestUtil.logMsg("WSDL Location URL4:    " + wsdlurl4);
  }

  private void getPortStandalone() throws Exception {
    TestUtil.logMsg(
        "******************************Retrieving Port 1************************\n");
    // client side Addressing enabled/NotRequired; server side
    // Addressing/NotRequired
    port1 = (AddressingFeatureTest1) JAXWS_Util.getPort(wsdlurl1,
        SERVICE1_QNAME, AddressingFeatureTest1Service.class, PORT_QNAME1,
        AddressingFeatureTest1.class, enabledNotRequiredwsf);
    JAXWS_Util.setTargetEndpointAddress(port1, url1);

    TestUtil.logMsg(
        "******************************Retrieving Port 2************************\n");
    // client side Addressing enabled/NotRequired; server side
    // Addressing/Required
    port2 = (AddressingFeatureTest2) JAXWS_Util.getPort(wsdlurl2,
        SERVICE2_QNAME, AddressingFeatureTest2Service.class, PORT_QNAME2,
        AddressingFeatureTest2.class, enabledNotRequiredwsf);
    JAXWS_Util.setTargetEndpointAddress(port2, url2);

    TestUtil.logMsg(
        "******************************Retrieving Port 3************************\n");
    // client side Addressing enabled/NotRequired; server side Addressing (using
    // default)
    port3 = (AddressingFeatureTest3) JAXWS_Util.getPort(wsdlurl3,
        SERVICE3_QNAME, AddressingFeatureTest3Service.class, PORT_QNAME3,
        AddressingFeatureTest3.class, enabledNotRequiredwsf);
    JAXWS_Util.setTargetEndpointAddress(port3, url3);

    TestUtil.logMsg(
        "******************************Retrieving Port 4************************\n");
    // client side Addressing enabled/NotRequired; server side Addressing off
    port4 = (AddressingFeatureTest4) JAXWS_Util.getPort(wsdlurl4,
        SERVICE4_QNAME, AddressingFeatureTest4Service.class, PORT_QNAME4,
        AddressingFeatureTest4.class, enabledNotRequiredwsf);
    JAXWS_Util.setTargetEndpointAddress(port4, url4);

    TestUtil.logMsg(
        "******************************Retrieving Port 5************************\n");
    // client side Addressing off; server side Addressing/NotRequired
    port5 = (AddressingFeatureTest1) JAXWS_Util.getPort(wsdlurl1,
        SERVICE1_QNAME, AddressingFeatureTest1Service.class, PORT_QNAME1,
        AddressingFeatureTest1.class, nonEnabledwsf);
    JAXWS_Util.setTargetEndpointAddress(port5, url1);

    TestUtil.logMsg(
        "******************************Retrieving Port 6************************\n");
    // client side Addressing off; server side Addressing/Required
    port6 = (AddressingFeatureTest2) JAXWS_Util.getPort(wsdlurl2,
        SERVICE2_QNAME, AddressingFeatureTest2Service.class, PORT_QNAME2,
        AddressingFeatureTest2.class, nonEnabledwsf);
    JAXWS_Util.setTargetEndpointAddress(port6, url2);

    TestUtil.logMsg(
        "******************************Retrieving Port 7************************\n");
    // client side Addressing enabled/Required; server side off
    port7 = (AddressingFeatureTest4) JAXWS_Util.getPort(wsdlurl4,
        SERVICE4_QNAME, AddressingFeatureTest4Service.class, PORT_QNAME4,
        AddressingFeatureTest4.class, enabledRequiredwsf);
    JAXWS_Util.setTargetEndpointAddress(port7, url4);
  }

  private void getPortJavaEE() throws Exception {
    javax.naming.InitialContext ic = new javax.naming.InitialContext();
    TestUtil.logMsg("Obtain service1 via WebServiceRef annotation");
    AddressingFeatureTest1Service service1 = (AddressingFeatureTest1Service) ic
        .lookup("java:comp/env/service/WSAJ2WDLAddressingFeatureTest1");
    TestUtil.logMsg("service1=" + service1);
    TestUtil.logMsg(
        "******************************Retrieving Port 1************************\n");
    port1 = (AddressingFeatureTest1) service1
        .getPort(AddressingFeatureTest1.class, enabledNotRequiredwsf);

    TestUtil.logMsg("Obtain service2 via WebServiceRef annotation");
    AddressingFeatureTest2Service service2 = (AddressingFeatureTest2Service) ic
        .lookup("java:comp/env/service/WSAJ2WDLAddressingFeatureTest2");
    TestUtil.logMsg("service2=" + service2);
    TestUtil.logMsg(
        "******************************Retrieving Port 2************************\n");
    port2 = (AddressingFeatureTest2) service2
        .getPort(AddressingFeatureTest2.class, enabledNotRequiredwsf);

    TestUtil.logMsg("Obtain service3 via WebServiceRef annotation");
    AddressingFeatureTest3Service service3 = (AddressingFeatureTest3Service) ic
        .lookup("java:comp/env/service/WSAJ2WDLAddressingFeatureTest3");
    TestUtil.logMsg("service3=" + service3);
    TestUtil.logMsg(
        "******************************Retrieving Port 3************************\n");
    port3 = (AddressingFeatureTest3) service3
        .getPort(AddressingFeatureTest3.class, enabledNotRequiredwsf);

    TestUtil.logMsg("Obtain service4 via WebServiceRef annotation");
    AddressingFeatureTest4Service service4 = (AddressingFeatureTest4Service) ic
        .lookup("java:comp/env/service/WSAJ2WDLAddressingFeatureTest4");
    TestUtil.logMsg("service4=" + service4);
    TestUtil.logMsg(
        "******************************Retrieving Port 4************************\n");
    port4 = (AddressingFeatureTest4) service4
        .getPort(AddressingFeatureTest4.class, enabledNotRequiredwsf);

    TestUtil.logMsg(
        "******************************Retrieving Port 5************************\n");
    port5 = (AddressingFeatureTest1) service1
        .getPort(AddressingFeatureTest1.class, nonEnabledwsf);

    TestUtil.logMsg(
        "******************************Retrieving Port 6************************\n");
    port6 = (AddressingFeatureTest2) service2
        .getPort(AddressingFeatureTest2.class, nonEnabledwsf);

    TestUtil.logMsg(
        "******************************Retrieving Port 7************************\n");
    port7 = (AddressingFeatureTest4) service4
        .getPort(AddressingFeatureTest4.class, enabledRequiredwsf);

    Object[] portsTodump = new Object[] { port1, port2, port3, port4, port5,
        port6, port7 };
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
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
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
   * @assertion_ids: JAXWS:SPEC:3047;
   *
   * @test_Strategy: Test Addressing Feature. Client enabled/NotRequired, Server
   * enabled/NotRequired. Addressing headers MAY be present on SOAPRequest and
   * SOAPResponse since Addressing is Optional. If addressing headers exist
   * check them otherwise don't.
   */
  public void afClientEnabledNotREQServerEnabledNotREQTest() throws Fault {
    TestUtil.logMsg("afClientEnabledNotREQServerEnabledNotREQTest");
    boolean pass = true;
    try {
      // client side and server side Addressing enabled/NotRequired
      port1.addNumbers1(new Holder("ClientEnabledNotREQServerEnabledNotREQ"),
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
   * @testName: afClientEnabledNotREQServerEnabledREQTest
   *
   * @assertion_ids: JAXWS:SPEC:3047;
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
    boolean pass = true;
    try {
      // client side Addressing enabled/NotRequired; server side
      // Addressing/Required
      port2.addNumbers2(new Holder("ClientEnabledNotREQServerEnabledREQ"), 10,
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
   * @testName: afClientEnabledNotREQServerUsingDefaultsTest
   *
   * @assertion_ids: JAXWS:SPEC:3047;
   *
   * @test_Strategy: Test Addressing Feature. Client enabled/NotRequired, Server
   * using defaults. Addressing headers MAY be present on SOAPRequest and
   * SOAPResponse since Addressing is Optional. If addressing headers exist
   * check them otherwise don't.
   */
  public void afClientEnabledNotREQServerUsingDefaultsTest() throws Fault {
    TestUtil.logMsg("afClientEnabledNotREQServerUsingDefaultsTest");
    boolean pass = true;
    try {
      // client side Addressing enabled/NotRequired; Server side using defaults
      port3.addNumbers3(new Holder("ClientEnabledNotREQServerUsingDefaults"),
          10, 10);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("afClientEnabledNotREQServerUsingDefaultsTest failed", e);
    }

    if (!pass) {
      throw new Fault("afClientEnabledNotREQServerUsingDefaultsTest failed");
    }
  }

  /*
   * @testName: afClientEnabledNotREQServerNotEnabledTest
   *
   * @assertion_ids: JAXWS:SPEC:3047;
   *
   * @test_Strategy: Test Addressing Feature. Client enabled/NotRequired, Server
   * NotEnabled. Addressing headers MAY be present on SOAPRequest but MUST NOT
   * be present on SOAPResponse.
   */
  public void afClientEnabledNotREQServerNotEnabledTest() throws Fault {
    TestUtil.logMsg("afClientEnabledNotREQServerNotEnabledTest");
    boolean pass = true;
    try {
      // client side Addressing enabled/NotRequired; Server
      // NotEnabled/NotRequired
      port4.addNumbers4(new Holder("ClientEnabledNotREQServerNotEnabled"), 10,
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
   * @testName: afClientNotEnabledServerEnabledNotREQTest
   *
   * @assertion_ids: WSAMD:SPEC:3001.1; WSAMD:SPEC:3001.3; WSAMD:SPEC:3001.4;
   * JAXWS:SPEC:6011; JAXWS:SPEC:6011.2; JAXWS:SPEC:6012; JAXWS:SPEC:6012.3;
   * JAXWS:SPEC:6012.5; JAXWS:SPEC:7020; JAXWS:SPEC:7020.2; JAXWS:SPEC:10025;
   * JAXWS:JAVADOC:190; JAXWS:SPEC:4034; JAXWS:SPEC:6012.1;
   *
   * @test_Strategy: Test Addressing Feature. Client NotEnabled, Server
   * enabled/NotRequired. Addressing headers MUST not be present on SOAPRequest
   * and SOAPResponse.
   */
  public void afClientNotEnabledServerEnabledNotREQTest() throws Fault {
    TestUtil.logMsg("afClientNotEnabledServerEnabledNotREQTest");
    TestUtil.logMsg(
        "Verify Addressing headers are NOT present on SOAPRequest and SOAPResponse");
    boolean pass = true;
    try {
      port5.addNumbers1(new Holder("ClientNotEnabledServerEnabledNotREQ"), 10,
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
      port6.addNumbers2(new Holder("ClientNotEnabledServerEnabledREQ"), 10, 10);
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
      port7.addNumbers4(new Holder("ClientEnabledREQServerNotEnabled"), 10, 10);
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
