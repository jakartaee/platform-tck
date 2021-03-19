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

package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.requestresponse;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPException;

import com.sun.ts.tests.jaxws.wsa.common.AddressingHeaderException;

import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsa.common.*;
import com.sun.ts.tests.jaxws.sharedclients.*;

import java.net.URL;

import jakarta.xml.ws.*;
import jakarta.xml.ws.soap.*;
import jakarta.xml.soap.*;
import java.io.*;

import java.util.Properties;
import java.util.UUID;
import java.text.MessageFormat;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.wsa.j2w.document.literal.requestresponse.";

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "wsaj2wdlrequestresponsetest.endpoint.1";

  private static final String WSDLLOC_URL = "wsaj2wdlrequestresponsetest.wsdlloc.1";

  private String url = null;

  // service and port information
  private static final String NAMESPACEURI = "http://example.com";

  private static final String SERVICE_NAME = "AddNumbersService";

  private static final String PORT_NAME = "AddNumbersPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private URL wsdlurl = null;

  AddNumbersPortType port = null;

  static AddNumbersService service = null;

  String invalidCardinality = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header><To xmlns=\"http://www.w3.org/2005/08/addressing\">{0}</To><Action xmlns=\"http://www.w3.org/2005/08/addressing\">inputAction</Action><MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:{1}</MessageID><ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{2}</Address></ReplyTo><ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{3}</Address></ReplyTo></S:Header><S:Body><ns1:addNumbers2 xmlns:ns1=\"http://example.com\"><number1>10</number1><number2>10</number2><testName>invalidCardinality</testName></ns1:addNumbers2></S:Body></S:Envelope>";

  String actionMismatch = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header><To xmlns=\"http://www.w3.org/2005/08/addressing\">{0}</To><Action xmlns=\"http://www.w3.org/2005/08/addressing\">inputAction</Action><MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:{1}</MessageID><ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{2}</Address></ReplyTo></S:Header><S:Body><ns1:addNumbers2 xmlns:ns1=\"http://example.com\"><number1>10</number1><number2>10</number2><testName>actionMismatch</testName></ns1:addNumbers2></S:Body></S:Envelope>";

  String actionNotSupported = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header><To xmlns=\"http://www.w3.org/2005/08/addressing\">{0}</To><Action xmlns=\"http://www.w3.org/2005/08/addressing\">ActionNotSupported</Action><MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:{1}</MessageID><ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{2}</Address></ReplyTo></S:Header><S:Body><ns1:addNumbers2 xmlns:ns1=\"http://example.com\"><number1>10</number1><number2>10</number2><testName>actionNotSupported</testName></ns1:addNumbers2></S:Body></S:Envelope>";

  String missingActionHeader = "<?xml version=\"1.0\" ?><S:Envelope xmlns:S=\"http://schemas.xmlsoap.org/soap/envelope/\"><S:Header><To xmlns=\"http://www.w3.org/2005/08/addressing\">{0}</To><MessageID xmlns=\"http://www.w3.org/2005/08/addressing\">uuid:{1}</MessageID><ReplyTo xmlns=\"http://www.w3.org/2005/08/addressing\"><Address>{2}</Address></ReplyTo></S:Header><S:Body><ns1:addNumbers2 xmlns:ns1=\"http://example.com\"><number1>10</number1><number2>10</number2><testName>missingActionHeader</testName></ns1:addNumbers2></S:Body></S:Envelope>";

  private static AddressingFeature ENABLED_ADDRESSING_FEATURE = new AddressingFeature(
      true, true);

  private static AddressingFeature DISABLED_ADDRESSING_FEATURE = new AddressingFeature(
      false);

  private WebServiceFeature[] enabledRequiredwsf = {
      ENABLED_ADDRESSING_FEATURE };

  private Dispatch<SOAPMessage> createDispatchSOAPMessage(QName port)
      throws Exception {
    return service.createDispatch(port, SOAPMessage.class,
        jakarta.xml.ws.Service.Mode.MESSAGE, DISABLED_ADDRESSING_FEATURE);
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

  private void getPortStandalone() throws Exception {
    port = (AddNumbersPortType) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        AddNumbersService.class, PORT_QNAME, AddNumbersPortType.class,
        enabledRequiredwsf);
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
        service = (AddNumbersService) JAXWS_Util.getService(wsdlurl,
            SERVICE_QNAME, AddNumbersService.class);
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
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /*
   * @testName: testDefaultRequestResponseAction
   *
   * @assertion_ids: WSACORE:SPEC:2001; WSACORE:SPEC:3002; WSACORE:SPEC:3003;
   * WSACORE:SPEC:3003.1; WSACORE:SPEC:3003.2; WSACORE:SPEC:3003.3;
   * WSACORE:SPEC:3005; WSACORE:SPEC:3007; WSACORE:SPEC:3009; WSACORE:SPEC:3010;
   * WSACORE:SPEC:3010.1; WSACORE:SPEC:3010.2; WSACORE:SPEC:3017;
   * WSACORE:SPEC:3022; WSACORE:SPEC:3022.2; WSACORE:SPEC:3022.2.1;
   * WSACORE:SPEC:3002.2.2; WSACORE:SPEC:3023; WSACORE:SPEC:3023.1;
   * WSACORE:SPEC:3023.1.1; WSACORE:SPEC:3023.1.2; WSACORE:SPEC:3023.4;
   * WSACORE:SPEC:3023.4.1; WSASB:SPEC:5000; WSASB:SPEC:6000; WSAMD:SPEC:4001;
   * WSAMD:SPEC:4001.1; WSAMD:SPEC:5001;
   *
   * @test_Strategy: Test default action pattern for WSDL input/output
   *
   */
  public void testDefaultRequestResponseAction() throws Fault {
    TestUtil.logMsg("testDefaultRequestResponseAction");
    boolean pass = true;

    try {
      int number = port.addNumbers(10, 10,
          new Holder("testDefaultRequestResponseAction"));
      TestUtil.logMsg("number=" + number);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("testDefaultRequestResponseAction failed", e);
    }

    if (!pass)
      throw new Fault("testDefaultRequestResponseAction failed");
  }

  /*
   * @testName: testExplicitRequestResponseAction
   *
   * @assertion_ids: WSACORE:SPEC:2001; WSACORE:SPEC:3002; WSACORE:SPEC:3003;
   * WSACORE:SPEC:3003.1; WSACORE:SPEC:3003.2; WSACORE:SPEC:3003.3;
   * WSACORE:SPEC:3005; WSACORE:SPEC:3007; WSACORE:SPEC:3009; WSACORE:SPEC:3010;
   * WSACORE:SPEC:3010.1; WSACORE:SPEC:3010.2; WSACORE:SPEC:3017;
   * WSACORE:SPEC:3022; WSACORE:SPEC:3022.2; WSACORE:SPEC:3022.2.1;
   * WSACORE:SPEC:3002.2.2; WSACORE:SPEC:3023; WSACORE:SPEC:3023.1;
   * WSACORE:SPEC:3023.1.1; WSACORE:SPEC:3023.1.2; WSACORE:SPEC:3023.4;
   * WSACORE:SPEC:3023.4.1; WSASB:SPEC:5000; WSASB:SPEC:6000;
   *
   * @test_Strategy: Test explicit action pattern for WSDL input/output
   *
   */
  public void testExplicitRequestResponseAction() throws Fault {
    TestUtil.logMsg("testExplicitRequestResponseAction");
    boolean pass = true;

    try {
      int number = port.addNumbers2(10, 10,
          new Holder("testExplicitRequestResponseAction"));
      TestUtil.logMsg("number=" + number);
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("testExplicitRequestResponseAction failed", e);
    }

    if (!pass)
      throw new Fault("testExplicitRequestResponseAction failed");
  }

  /*
   * @testName: testMessageAddressingHeaderRequiredFault
   *
   * @assertion_ids: WSASB:SPEC:6005; WSASB:SPEC:6006; WSASB:SPEC:6013;
   * WSASB:SPEC:6004.4; WSASB:SPEC:6004.1; WSASB:SPEC:6004.2; WSASB:SPEC:6001;
   *
   * @test_Strategy: Send a message that doesn't contain wsa:Action header.
   * Expect MessageAddressingHeaderRequired fault. Cannot test missing wsa:To,
   * wsa:ReplyTo, or wsa:MessageID headers as these are optional in WSA Core
   * Spec.
   */
  public void testMessageAddressingHeaderRequiredFault() throws Fault {
    TestUtil.logMsg("testMessageAddressingHeaderRequiredFault");
    boolean pass = true;

    SOAPMessage response = null;
    Dispatch<SOAPMessage> dispatchSM;
    try {
      String soapmsg = MessageFormat.format(missingActionHeader, url,
          UUID.randomUUID(), WsaSOAPUtils.getAddrVerAnonUri());
      dispatchSM = createDispatchSOAPMessage(PORT_QNAME);
      SOAPMessage request = JAXWS_Util.makeSOAPMessage(soapmsg);
      TestUtil.logMsg("Dumping SOAP Request ...");
      JAXWS_Util.dumpSOAPMessage(request, false);
      response = dispatchSM.invoke(request);
      TestUtil.logMsg("Dumping SOAP Response ...");
      JAXWS_Util.dumpSOAPMessage(response, false);
    } catch (SOAPFaultException e) {
      try {
        TestUtil.logMsg("Caught SOAPFaultException");
        TestUtil.logMsg("Verify the SOAPFault faultcode");
        TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(e));
        TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(e));
        if (WsaSOAPUtils.isMessageAddressingHeaderRequiredFaultCode(e))
          TestUtil.logMsg(
              "SOAPFault contains expected faultcode MessageAddressingHeaderRequired");
        else {
          String faultcode = WsaSOAPUtils.getFaultCode(e);
          TestUtil.logErr("SOAPFault contains unexpected faultcode got: "
              + faultcode + ", expected: MessageAddressingHeaderRequired");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultString(e) == null) {
          TestUtil
              .logErr("The faultstring element MUST EXIST for SOAP 1.1 Faults");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultDetail(e) != null) {
          TestUtil.logErr("The faultdetail element MUST NOT EXIST for SOAP 1.1 "
              + "Faults related to header entries");
          pass = false;
        }
      } catch (SOAPException e2) {
        TestUtil.logErr("Caught unexpected exception: " + e2.getMessage());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("testMessageAddressingHeaderRequiredFault failed", e);
    }
    if (!pass)
      throw new Fault("testMessageAddressingHeaderRequiredFault failed");
  }

  /*
   * @testName: testInvalidCardinalityFault
   *
   * @assertion_ids: WSASB:SPEC:6005; WSASB:SPEC:6006; WSASB:SPEC:6013;
   * WSASB:SPEC:6012.3; WSASB:SPEC:6004.4; WSASB:SPEC:6004.1; WSASB:SPEC:6004.2;
   * WSASB:SPEC:6001;
   *
   * @test_Strategy: Test for InvalidCardinality fault. Send a message that
   * contains two wsa:ReplyTo headers. Expect an InvalidCardinality fault.
   */
  public void testInvalidCardinalityFault() throws Fault {
    TestUtil.logMsg("testInvalidCardinalityFault");
    boolean pass = true;
    SOAPMessage response = null;
    Dispatch<SOAPMessage> dispatchSM;
    try {
      String soapmsg = MessageFormat.format(invalidCardinality, url,
          UUID.randomUUID(), WsaSOAPUtils.getAddrVerAnonUri(),
          WsaSOAPUtils.getAddrVerAnonUri());
      dispatchSM = createDispatchSOAPMessage(PORT_QNAME);
      SOAPMessage request = JAXWS_Util.makeSOAPMessage(soapmsg);
      TestUtil.logMsg("Dumping SOAP Request ...");
      JAXWS_Util.dumpSOAPMessage(request, false);
      response = dispatchSM.invoke(request);
      TestUtil.logMsg("Dumping SOAP Response ...");
      JAXWS_Util.dumpSOAPMessage(response, false);
      TestUtil.logErr("No SOAPFaultException occurred which was expected");
      pass = false;
    } catch (SOAPFaultException s) {
      try {
        TestUtil.logMsg("Verify the SOAPFault faultcode");
        TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(s));
        TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(s));
        if (WsaSOAPUtils.isInvalidCARDINALITYFaultCode(s))
          TestUtil.logMsg(
              "SOAPFault contains expected faultcode InvalidCardinality");
        else {
          String faultcode = WsaSOAPUtils.getFaultCode(s);
          TestUtil.logErr("SOAPFault contains unexpected faultcode got: "
              + faultcode + ", expected: InvalidCardinality");
          pass = false;
        }
        String faultdetail[] = WsaSOAPUtils.getFaultDetail(s);
        if (faultdetail != null) {
          StringBuffer output = new StringBuffer("FaultDetail:");
          for (int i = 0; faultdetail[i] != null; i++) {
            output.append(" " + faultdetail[i]);
          }
          TestUtil.logErr(output.toString());

          if (WsaSOAPUtils.isProblemHeaderQNameFaultDetail(faultdetail[0]))
            TestUtil.logMsg("FaultDetail contains expected ProblemHeaderQName");
          else {
            TestUtil.logErr("FaultDetail contains unexpected value got: "
                + faultdetail[0] + ", expected: ProblemHeaderQName");
            pass = false;
          }
        }
      } catch (Exception e2) {
        TestUtil.logErr("Caught unexpected exception: " + e2.getMessage());
        throw new Fault("testInvalidCardinalityFault failed", e2);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testInvalidCardinalityFault failed", e);
    }
    if (!pass)
      throw new Fault("testInvalidCardinalityFault failed");
  }

  /*
   * @testName: testActionMismatchOrActionNotSupportedFaultCase1
   *
   * @assertion_ids: WSASB:SPEC:6005; WSASB:SPEC:6006; WSASB:SPEC:6015;
   * WSASB:SPEC:6004.4; WSASB:SPEC:6004.1; WSASB:SPEC:6004.2; WSASB:SPEC:6001;
   * JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test for ActionMismatch or ActionNotSupportedfault. Set the
   * SOAPACTIONURI to a wrong value.
   *
   */
  public void testActionMismatchOrActionNotSupportedFaultCase1() throws Fault {
    TestUtil.logMsg("testActionMismatchOrActionNotSupportedFaultCase1");
    boolean pass = true;
    SOAPMessage response = null;
    Dispatch<SOAPMessage> dispatchSM;
    try {
      String soapmsg = MessageFormat.format(actionMismatch, url,
          UUID.randomUUID(), WsaSOAPUtils.getAddrVerAnonUri());
      dispatchSM = createDispatchSOAPMessage(PORT_QNAME);
      JAXWS_Util.setSOAPACTIONURI(dispatchSM, "ActionMismatch");
      SOAPMessage request = JAXWS_Util.makeSOAPMessage(soapmsg);
      TestUtil.logMsg("Dumping SOAP Request ...");
      JAXWS_Util.dumpSOAPMessage(request, false);
      response = dispatchSM.invoke(request);
      TestUtil.logMsg("Dumping SOAP Response ...");
      JAXWS_Util.dumpSOAPMessage(response, false);
      TestUtil.logErr("No SOAPFaultException occurred which was expected");
      pass = false;
    } catch (SOAPFaultException s) {
      try {
        TestUtil.logMsg("Verify the SOAPFault faultcode");
        TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(s));
        TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(s));
        if (WsaSOAPUtils.isActionMismatchFaultCode(s)
            || WsaSOAPUtils.isActionNotSupportedFaultCode(s))
          TestUtil.logMsg(
              "SOAPFault contains expected faultcode ActionMismatch or ActionNotSupported");
        else {
          String faultcode = WsaSOAPUtils.getFaultCode(s);
          TestUtil.logErr("SOAPFault contains unexpected faultcode got: "
              + faultcode + ", expected: ActionMismatch or ActionNotSupported");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultString(s) == null) {
          TestUtil
              .logErr("The faultstring element MUST EXIST for SOAP 1.1 Faults");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultDetail(s) != null) {
          TestUtil.logErr("The faultdetail element MUST NOT EXIST for SOAP 1.1 "
              + "Faults related to header entries");
          pass = false;
        }
      } catch (Exception e2) {
        TestUtil.logErr("Caught unexpected exception: " + e2.getMessage());
        throw new Fault(
            "testActionMismatchOrActionNotSupportedFaultCase1 failed", e2);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testActionMismatchOrActionNotSupportedFaultCase1 failed",
          e);
    }
    if (!pass)
      throw new Fault(
          "testActionMismatchOrActionNotSupportedFaultCase1 failed");
  }

  /*
   * @testName: testActionMismatchOrActionNotSupportedFaultCase2
   *
   * @assertion_ids: WSASB:SPEC:6005; WSASB:SPEC:6006; WSASB:SPEC:6015;
   * WSASB:SPEC:6004.4; WSASB:SPEC:6004.1; WSASB:SPEC:6004.2; WSASB:SPEC:6001;
   * JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test for ActionMismatch or ActionNotSupportedfault. Set the
   * SOAPACTIONURI to a wrong value.
   *
   */
  public void testActionMismatchOrActionNotSupportedFaultCase2() throws Fault {
    TestUtil.logMsg("testActionMismatchOrActionNotSupportedFaultCase2");
    boolean pass = true;
    SOAPMessage response = null;
    Dispatch<SOAPMessage> dispatchSM;
    try {
      String soapmsg = MessageFormat.format(actionNotSupported, url,
          UUID.randomUUID(), WsaSOAPUtils.getAddrVerAnonUri());
      dispatchSM = createDispatchSOAPMessage(PORT_QNAME);
      JAXWS_Util.setSOAPACTIONURI(dispatchSM, "ActionNotSupported1");
      SOAPMessage request = JAXWS_Util.makeSOAPMessage(soapmsg);
      TestUtil.logMsg("Dumping SOAP Request ...");
      JAXWS_Util.dumpSOAPMessage(request, false);
      response = dispatchSM.invoke(request);
      TestUtil.logMsg("Dumping SOAP Response ...");
      JAXWS_Util.dumpSOAPMessage(response, false);
      TestUtil.logErr("No SOAPFaultException occurred which was expected");
      pass = false;
    } catch (SOAPFaultException s) {
      try {
        TestUtil.logMsg("Verify the SOAPFault faultcode");
        TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(s));
        TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(s));
        if (WsaSOAPUtils.isActionMismatchFaultCode(s)
            || WsaSOAPUtils.isActionNotSupportedFaultCode(s))
          TestUtil.logMsg(
              "SOAPFault contains expected faultcode ActionMismatch or ActionNotSupported");
        else {
          String faultcode = WsaSOAPUtils.getFaultCode(s);
          TestUtil.logErr("SOAPFault contains unexpected faultcode got: "
              + faultcode + ", expected: ActionMismatch or ActionNotSupported");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultString(s) == null) {
          TestUtil
              .logErr("The faultstring element MUST EXIST for SOAP 1.1 Faults");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultDetail(s) != null) {
          TestUtil.logErr("The faultdetail element MUST NOT EXIST for SOAP 1.1 "
              + "Faults related to header entries");
          pass = false;
        }
      } catch (Exception e2) {
        TestUtil.logErr("Caught unexpected exception: " + e2.getMessage());
        throw new Fault(
            "testActionMismatchOrActionNotSupportedFaultCase2 failed", e2);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testActionMismatchOrActionNotSupportedFaultCase2 failed",
          e);
    }
    if (!pass)
      throw new Fault(
          "testActionMismatchOrActionNotSupportedFaultCase2 failed");
  }

  /*
   * @testName: testActionMismatchOrActionNotSupportedFaultCase3
   *
   * @assertion_ids: WSASB:SPEC:6005; WSASB:SPEC:6006; WSASB:SPEC:6015;
   * WSASB:SPEC:6004.4; WSASB:SPEC:6004.1; WSASB:SPEC:6004.2; WSASB:SPEC:6001;
   * JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test for ActionMismatch or ActionNotSupportedfault. Set the
   * SOAPACTIONURI to a wrong value.
   *
   */
  public void testActionMismatchOrActionNotSupportedFaultCase3() throws Fault {
    TestUtil.logMsg("testActionMismatchOrActionNotSupportedFaultCase3");
    boolean pass = true;
    try {
      JAXWS_Util.setSOAPACTIONURI(port, "ActionNotSupported2");
      int number = port.addNumbers2(10, 10, new Holder("ActionNotSupported2"));
      TestUtil.logErr("No SOAPFaultException occurred which was expected");
      pass = false;
    } catch (SOAPFaultException s) {
      try {
        TestUtil.logMsg("Verify the SOAPFault faultcode");
        TestUtil.logMsg("FaultCode=" + WsaSOAPUtils.getFaultCode(s));
        TestUtil.logMsg("FaultString=" + WsaSOAPUtils.getFaultString(s));
        if (WsaSOAPUtils.isActionMismatchFaultCode(s)
            || WsaSOAPUtils.isActionNotSupportedFaultCode(s))
          TestUtil.logMsg(
              "SOAPFault contains expected faultcode ActionMismatch or ActionNotSupported");
        else {
          String faultcode = WsaSOAPUtils.getFaultCode(s);
          TestUtil.logErr("SOAPFault contains unexpected faultcode got: "
              + faultcode + ", expected: ActionMismatch or ActionNotSupported");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultString(s) == null) {
          TestUtil
              .logErr("The faultstring element MUST EXIST for SOAP 1.1 Faults");
          pass = false;
        }
        if (WsaSOAPUtils.getFaultDetail(s) != null) {
          TestUtil.logErr("The faultdetail element MUST NOT EXIST for SOAP 1.1 "
              + "Faults related to header entries");
          pass = false;
        }
      } catch (Exception e2) {
        TestUtil.logErr("Caught unexpected exception: " + e2.getMessage());
        throw new Fault(
            "testActionMismatchOrActionNotSupportedFaultCase3 failed", e2);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught unexpected exception: " + e.getMessage());
      throw new Fault("testActionMismatchOrActionNotSupportedFaultCase3 failed",
          e);
    }
    if (!pass)
      throw new Fault(
          "testActionMismatchOrActionNotSupportedFaultCase3 failed");
  }
}
