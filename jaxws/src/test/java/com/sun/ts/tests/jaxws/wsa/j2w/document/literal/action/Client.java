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

package com.sun.ts.tests.jaxws.wsa.j2w.document.literal.action;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.*;
import jakarta.xml.ws.*;
import jakarta.xml.ws.soap.*;
import jakarta.xml.soap.*;
import java.util.Properties;
import java.math.BigInteger;
import java.math.BigDecimal;
import javax.xml.namespace.QName;
import com.sun.javatest.Status;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.wsa.common.*;
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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.wsa.j2w.document.literal.action.";

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "wsaj2wdlactiontest.endpoint.1";

  private static final String WSDLLOC_URL = "wsaj2wdlactiontest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  // service and port information
  private static final String NAMESPACEURI = "http://foobar.org/";

  private static final String SERVICE_NAME = "AddNumbersService";

  private static final String PORT_NAME = "AddNumbersPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private WebServiceFeature[] enabledRequiredwsf = {
      new AddressingFeature(true, true) };

  private WebServiceFeature[] disabledNotRequiredwsf = {
      new AddressingFeature(false, false) };

  AddNumbers portEnabled = null;

  AddNumbers portDisabled = null;

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
    TestUtil.logMsg("Obtain port");
    portEnabled = (AddNumbers) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        AddNumbersService.class, PORT_QNAME, AddNumbers.class,
        enabledRequiredwsf);
    portDisabled = (AddNumbers) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        AddNumbersService.class, PORT_QNAME, AddNumbers.class,
        disabledNotRequiredwsf);
    JAXWS_Util.setTargetEndpointAddress(portEnabled, url);
    JAXWS_Util.setTargetEndpointAddress(portDisabled, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    TestUtil.logMsg("Obtain port");
    portEnabled = (AddNumbers) service.getPort(AddNumbers.class,
        enabledRequiredwsf);
    portDisabled = (AddNumbers) service.getPort(AddNumbers.class,
        disabledNotRequiredwsf);
    JAXWS_Util.dumpTargetEndpointAddress(portEnabled);
    JAXWS_Util.dumpTargetEndpointAddress(portDisabled);
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
        TestUtil.logMsg("WebServiceRef is not set in Client "
            + "(get it from specific vehicle)");
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
          "Please specify host & port of web server in " + "config properties: "
              + WEBSERVERHOSTPROP + ", " + WEBSERVERPORTPROP);
      throw new Fault("setup failed:");
    }
    TestUtil.logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    TestUtil.logMsg("cleanup ok");
  }

  /*
   * @testName: testNoActionOnInputOutput
   *
   * @assertion_ids: JAXWS:SPEC:7017; JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.2;
   * JAXWS:SPEC:7017.3; JAXWS:SPEC:10025; JAXWS:SPEC:10026; WSAMD:SPEC:4004;
   * WSAMD:SPEC:4004.1; WSAMD:SPEC:4004.2; WSAMD:SPEC:4004.4; WSAMD:SPEC:4004.5;
   * JAXWS:SPEC:3055;
   *
   * @test_Strategy: Test no action for input/output elements.
   *
   */
  public void testNoActionOnInputOutput() throws Fault {
    TestUtil.logMsg("testNoActionOnInputOutput ");
    boolean pass = true;
    try {
      int result = portEnabled.addNumbersNoAction(10, 10);
      TestUtil.logMsg("WSA:Action headers are correct");
      if (result != 20) {
        TestUtil.logErr("Expected result=20, got result=" + result);
        pass = false;
      }
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected AddNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testNoActionOnInputOutput  failed", ex);
    }
    if (!pass)
      throw new Fault("testNoActionOnInputOutput  failed");
  }

  /*
   * @testName: testEmptyActionOnInputOutput
   *
   * @assertion_ids: WSAMD:SPEC:4004; WSAMD:SPEC:4004.1; WSAMD:SPEC:4004.2;
   * WSAMD:SPEC:4004.4; WSAMD:SPEC:4004.5; JAXWS:SPEC:3055; JAXWS:SPEC:7017;
   * JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.2; JAXWS:SPEC:7017.3; JAXWS:SPEC:10025;
   * JAXWS:SPEC:10026;
   *
   * @test_Strategy: Test default action for WSDL input/output elements and no
   * explicit message names
   *
   */
  public void testEmptyActionOnInputOutput() throws Fault {
    TestUtil.logMsg("testEmptyActionOnInputOutput");
    boolean pass = true;
    try {
      int result = portEnabled.addNumbersEmptyAction(10, 10);
      TestUtil.logMsg("WSA:Action headers are correct");
      if (result != 20) {
        TestUtil.logErr("Expected result=20, got result=" + result);
        pass = false;
      }
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected AddNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testEmptyActionOnInputOutput failed", ex);
    }
    if (!pass)
      throw new Fault("testEmptyActionOnInputOutput failed");
  }

  /*
   * @testName: testExplicitInputOutputActions1
   *
   * @assertion_ids: JAXWS:SPEC:7017; JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.2;
   * JAXWS:SPEC:7017.3; JAXWS:SPEC:10025; JAXWS:SPEC:10026; WSAMD:SPEC:4003;
   * WSAMD:SPEC:4003.1; WSAMD:SPEC:4003.2; JAXWS:SPEC:3055;
   *
   * @test_Strategy: Test explicit action for input/output elements
   *
   */
  public void testExplicitInputOutputActions1() throws Fault {
    TestUtil.logMsg("testExplicitInputOutputActions1");
    boolean pass = true;
    try {
      int result = portEnabled.addNumbers(10, 10);
      TestUtil.logMsg("WSA:Action headers are correct");
      if (result != 20) {
        TestUtil.logErr("Expected result=20, got result=" + result);
        pass = false;
      }
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected AddNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testExplicitInputOutputActions1 failed", ex);
    }
    if (!pass)
      throw new Fault("testExplicitInputOutputActions1 failed");
  }

  /*
   * @testName: testExplicitInputOutputActions2
   *
   * @assertion_ids: JAXWS:SPEC:7017; JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.2;
   * JAXWS:SPEC:7017.3; JAXWS:SPEC:10025; JAXWS:SPEC:10026; WSAMD:SPEC:4003;
   * WSAMD:SPEC:4003.1; WSAMD:SPEC:4003.2; JAXWS:SPEC:3055;
   *
   * @test_Strategy: Test explicit action for input/output elements
   *
   */
  public void testExplicitInputOutputActions2() throws Fault {
    TestUtil.logMsg("testExplicitInputOutputActions2");
    boolean pass = true;
    try {
      int result = portEnabled.addNumbers2(10, 10);
      TestUtil.logMsg("WSA:Action headers are correct");
      if (result != 20) {
        TestUtil.logErr("Expected result=20, got result=" + result);
        pass = false;
      }
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected AddNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testExplicitInputOutputActions2 failed", ex);
    }
    if (!pass)
      throw new Fault("testExplicitInputOutputActions2 failed");
  }

  /*
   * @testName: testDefaultOutputActionExplicitInputAction
   *
   * @assertion_ids: WSAMD:SPEC:4004; WSAMD:SPEC:4004.2; WSAMD:SPEC:4003;
   * WSAMD:SPEC:4003.1; WSAMD:SPEC:4004.5; JAXWS:SPEC:3055; JAXWS:SPEC:7017;
   * JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.2; JAXWS:SPEC:7017.3; JAXWS:SPEC:10025;
   * JAXWS:SPEC:10026;
   *
   * @test_Strategy: Test default action for WSDL output element Test explicit
   * action for WSDL input element
   *
   */
  public void testDefaultOutputActionExplicitInputAction() throws Fault {
    TestUtil.logMsg("testDefaultOutputActionExplicitInputAction");
    boolean pass = true;
    try {
      int result = portEnabled.addNumbers3(10, 10);
      TestUtil.logMsg("WSA:Action headers are correct");
      if (result != 20) {
        TestUtil.logErr("Expected result=20, got result=" + result);
        pass = false;
      }
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected AddNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testDefaultOutputActionExplicitInputAction failed", ex);
    }
    if (!pass)
      throw new Fault("testDefaultOutputActionExplicitInputAction failed");
  }

  /*
   * @testName: testSendingWrongSOAPActionHTTPHeaderValue
   *
   * @assertion_ids: JAXWS:SPEC:7017; JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.2;
   * JAXWS:SPEC:7017.3; JAXWS:SPEC:10025; JAXWS:SPEC:10026; WSAMD:SPEC:4003;
   * WSAMD:SPEC:4003.1; WSAMD:SPEC:4003.2; JAXWS:SPEC:3055;
   *
   * @test_Strategy: Test sedning wrong SOAPAction HTTP Value for operation with
   * explicit input/output action elements
   * 
   */
  public void testSendingWrongSOAPActionHTTPHeaderValue() throws Fault {
    TestUtil.logMsg("testSendingWrongSOAPActionHTTPHeaderValue");
    boolean pass = true;
    try {
      int result = portEnabled.addNumbers4(10, 10);
      TestUtil.logMsg("WSA:Action headers are correct");
      if (result != 20) {
        TestUtil.logErr("Expected result=20, got result=" + result);
        pass = false;
      }
    } catch (SOAPFaultException ex) {
      TestUtil.logMsg("Caught expected SOAPFaultException");
      String detailName = null;
      try {
        detailName = ex.getFault().getDetail().getFirstChild().getLocalName();
      } catch (Exception e) {
      }
      if (detailName != null)
        TestUtil.logMsg("DetailName = " + detailName);
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testSendingWrongSOAPActionHTTPHeaderValue failed", ex);
    }
    if (!pass)
      throw new Fault("testSendingWrongSOAPActionHTTPHeaderValue failed");
  }

  /*
   * @testName: testOneFaultExplicitAction
   *
   * @assertion_ids: JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2;
   * JAXWS:SPEC:10025; JAXWS:SPEC:10026; JAXWS:JAVADOC:131; JAXWS:JAVADOC:132;
   * JAXWS:JAVADOC:143; JAXWS:JAVADOC:144; WSAMD:SPEC:4003; WSAMD:SPEC:4003.3;
   * JAXWS:SPEC:3055;
   *
   * @test_Strategy: Test explicit action for fault element
   *
   */
  public void testOneFaultExplicitAction() throws Fault {
    TestUtil.logMsg("testOneFaultExplicitAction");
    boolean pass = true;
    try {
      portEnabled.addNumbersFault1(-10, 10);
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testOneFaultExplicitAction failed", ex);
    }
    if (!pass)
      throw new Fault("testOneFaultExplicitAction failed");
  }

  /*
   * @testName: testTwoFaultsExplicitAction1
   *
   * @assertion_ids: JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2;
   * JAXWS:SPEC:10025; JAXWS:SPEC:10026; JAXWS:JAVADOC:131; JAXWS:JAVADOC:132;
   * JAXWS:JAVADOC:143; JAXWS:JAVADOC:144; WSAMD:SPEC:4003; WSAMD:SPEC:4003.3;
   * JAXWS:SPEC:3055;
   *
   * @test_Strategy: Test explicit action for fault element
   *
   */
  public void testTwoFaultsExplicitAction1() throws Fault {
    TestUtil.logMsg("testTwoFaultsExplicitAction1");
    boolean pass = true;
    try {
      portEnabled.addNumbersFault2(-10, 10);
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (TooBigNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected TooBigNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testTwoFaultsExplicitAction1 failed", ex);
    }
    if (!pass)
      throw new Fault("testTwoFaultsExplicitAction1 failed");
  }

  /*
   * @testName: testTwoFaultsExplicitAction2
   *
   * @assertion_ids: JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2;
   * JAXWS:SPEC:10025; JAXWS:SPEC:10026; JAXWS:JAVADOC:131; JAXWS:JAVADOC:132;
   * JAXWS:JAVADOC:143; JAXWS:JAVADOC:144; WSAMD:SPEC:4003; WSAMD:SPEC:4003.3;
   * JAXWS:SPEC:3055;
   *
   * @test_Strategy: Test explicit action for fault element
   *
   */
  public void testTwoFaultsExplicitAction2() throws Fault {
    TestUtil.logMsg("testTwoFaultsExplicitAction2");
    boolean pass = true;
    try {
      portEnabled.addNumbersFault2(20, 10);
    } catch (TooBigNumbersException_Exception ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected AddNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("WSA:Action headers are incorrect");
      throw new Fault("testTwoFaultsExplicitAction2 failed", ex);
    }
    if (!pass)
      throw new Fault("testTwoFaultsExplicitAction2 failed");
  }

  /*
   * @testName: testTwoFaultsExplicitAddNumbersFault3
   *
   * @assertion_ids: JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2;
   * JAXWS:SPEC:10025; JAXWS:SPEC:10026; JAXWS:JAVADOC:131; JAXWS:JAVADOC:132;
   * JAXWS:JAVADOC:143; JAXWS:JAVADOC:144; WSAMD:SPEC:4003; WSAMD:SPEC:4003.3;
   * JAXWS:SPEC:3055;
   *
   * @test_Strategy: Test explicit action for fault element
   *
   */
  public void testTwoFaultsExplicitAddNumbersFault3() throws Fault {
    TestUtil.logMsg("testTwoFaultsExplicitAddNumbersFault3");
    boolean pass = true;
    try {
      portEnabled.addNumbersFault3(-10, 10);
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (TooBigNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected TooBigNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testTwoFaultsExplicitAddNumbersFault3 failed", ex);
    }
    if (!pass)
      throw new Fault("testTwoFaultsExplicitAddNumbersFault3 failed");
  }

  /*
   * @testName: testTwoFaultsDefaultTooBigNumbersFault3
   *
   * @assertion_ids: JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2;
   * JAXWS:SPEC:10025; JAXWS:SPEC:10026; JAXWS:JAVADOC:131; JAXWS:JAVADOC:132;
   * JAXWS:JAVADOC:143; JAXWS:JAVADOC:144; WSAMD:SPEC:4003; WSAMD:SPEC:4003.3;
   * JAXWS:SPEC:3055;
   *
   * @test_Strategy: Test no action for fault element
   *
   */
  public void testTwoFaultsDefaultTooBigNumbersFault3() throws Fault {
    TestUtil.logMsg("testTwoFaultsDefaultTooBigNumbersFault3");
    boolean pass = true;
    try {
      portEnabled.addNumbersFault3(20, 10);
    } catch (TooBigNumbersException_Exception ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected AddNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testTwoFaultsDefaultTooBigNumbersFault3 failed", ex);
    }
    if (!pass)
      throw new Fault("testTwoFaultsDefaultTooBigNumbersFault3 failed");
  }

  /*
   * @testName: testTwoFaultsExplicitAddNumbersFault4
   *
   * @assertion_ids: WSAMD:SPEC:4004; WSAMD:SPEC:4004.3; JAXWS:SPEC:3055;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:10025;
   * JAXWS:SPEC:10026;
   *
   * @test_Strategy: Test explicit action for WSDL fault element
   *
   */
  public void testTwoFaultsExplicitAddNumbersFault4() throws Fault {
    TestUtil.logMsg("testTwoFaultsExplicitAddNumbersFault4");
    boolean pass = true;
    try {
      portEnabled.addNumbersFault4(-10, 10);
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (TooBigNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected TooBigNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testTwoFaultsExplicitAddNumbersFault4 failed", ex);
    }
    if (!pass)
      throw new Fault("testTwoFaultsExplicitAddNumbersFault4 failed");
  }

  /*
   * @testName: testTwoFaultsDefaultTooBigNumbersFault4
   *
   * @assertion_ids: WSAMD:SPEC:4004; WSAMD:SPEC:4004.3; JAXWS:SPEC:3055;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:10025;
   * JAXWS:SPEC:10026;
   *
   * @test_Strategy: Test default action for WSDL fault element
   *
   */
  public void testTwoFaultsDefaultTooBigNumbersFault4() throws Fault {
    TestUtil.logMsg("testTwoFaultsDefaultTooBigNumbersFault4");
    boolean pass = true;
    try {
      portEnabled.addNumbersFault4(20, 10);
    } catch (TooBigNumbersException_Exception ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected AddNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testTwoFaultsDefaultTooBigNumbersFault4 failed", ex);
    }
    if (!pass)
      throw new Fault("testTwoFaultsDefaultTooBigNumbersFault4 failed");
  }

  /*
   * @testName: testTwoFaultsDefaultAddNumbersFault5
   *
   * @assertion_ids: WSAMD:SPEC:4004; WSAMD:SPEC:4004.3; JAXWS:SPEC:3055;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:10025;
   * JAXWS:SPEC:10026;
   *
   * @test_Strategy: Test default action for WSDL fault element
   *
   */
  public void testTwoFaultsDefaultAddNumbersFault5() throws Fault {
    TestUtil.logMsg("testTwoFaultsDefaultAddNumbersFault5");
    boolean pass = true;
    try {
      portEnabled.addNumbersFault5(-10, 10);
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (TooBigNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected TooBigNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testTwoFaultsDefaultAddNumbersFault5 failed", ex);
    }
    if (!pass)
      throw new Fault("testTwoFaultsDefaultAddNumbersFault5 failed");
  }

  /*
   * @testName: testTwoFaultsExplicitTooBigNumbersFault5
   *
   * @assertion_ids: WSAMD:SPEC:4003; WSAMD:SPEC:4003.3; JAXWS:JAVADOC:143;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:10025;
   * JAXWS:SPEC:10026; JAXWS:SPEC:3055;
   *
   * @test_Strategy: Test explicit action for WSDL fault element
   *
   */
  public void testTwoFaultsExplicitTooBigNumbersFault5() throws Fault {
    TestUtil.logMsg("testTwoFaultsExplicitTooBigNumbersFault5");
    boolean pass = true;
    try {
      portEnabled.addNumbersFault5(20, 10);
    } catch (TooBigNumbersException_Exception ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected AddNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testTwoFaultsExplicitTooBigNumbersFault5 failed", ex);
    }
    if (!pass)
      throw new Fault("testTwoFaultsExplicitTooBigNumbersFault5 failed");
  }

  /*
   * @testName: testOnlyFaultActionsBothExplicit1
   *
   * @assertion_ids: WSAMD:SPEC:4003; WSAMD:SPEC:4003.3; JAXWS:JAVADOC:143;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:10025;
   * JAXWS:SPEC:10026; JAXWS:SPEC:3055;
   *
   * @test_Strategy: Test explicit action for WSDL fault element
   *
   */
  public void testOnlyFaultActionsBothExplicit1() throws Fault {
    TestUtil.logMsg("testOnlyFaultActionsBothExplicit1");
    boolean pass = true;
    try {
      portEnabled.addNumbersFault6(-10, 10);
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (TooBigNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected TooBigNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testOnlyFaultActionsBothExplicit1 failed", ex);
    }
    if (!pass)
      throw new Fault("testOnlyFaultActionsBothExplicit1 failed");
  }

  /*
   * @testName: testOnlyFaultActionsBothExplicit2
   *
   * @assertion_ids: WSAMD:SPEC:4003; WSAMD:SPEC:4003.3; JAXWS:JAVADOC:143;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:10025;
   * JAXWS:SPEC:10026; JAXWS:SPEC:3055;
   *
   * @test_Strategy: Test explicit action for WSDL fault element
   *
   */
  public void testOnlyFaultActionsBothExplicit2() throws Fault {
    TestUtil.logMsg("testOnlyFaultActionsBothExplicit2");
    boolean pass = true;
    try {
      portEnabled.addNumbersFault6(20, 10);
    } catch (TooBigNumbersException_Exception ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected AddNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testOnlyFaultActionsBothExplicit2 failed", ex);
    }
    if (!pass)
      throw new Fault("testOnlyFaultActionsBothExplicit2 failed");
  }

  /*
   * @testName: testOnlyFaultActionsFault7BothEmpty1
   *
   * @assertion_ids: WSAMD:SPEC:4004; WSAMD:SPEC:4004.3; JAXWS:SPEC:3055;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:10025;
   * JAXWS:SPEC:10026;
   *
   * @test_Strategy: Test default action for WSDL fault element
   *
   */
  public void testOnlyFaultActionsFault7BothEmpty1() throws Fault {
    TestUtil.logMsg("testOnlyFaultActionsFault7BothEmpty1");
    boolean pass = true;
    try {
      portEnabled.addNumbersFault7(-10, 10);
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (TooBigNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected TooBigNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testOnlyFaultActionsFault7BothEmpty1 failed", ex);
    }
    if (!pass)
      throw new Fault("testOnlyFaultActionsFault7BothEmpty1 failed");
  }

  /*
   * @testName: testOnlyFaultActionsFault7BothEmpty2
   *
   * @assertion_ids: WSAMD:SPEC:4004; WSAMD:SPEC:4004.3; JAXWS:SPEC:3055;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:10025;
   * JAXWS:SPEC:10026;
   *
   * @test_Strategy: Test default action for WSDL fault element
   *
   */
  public void testOnlyFaultActionsFault7BothEmpty2() throws Fault {
    TestUtil.logMsg("testOnlyFaultActionsFault7BothEmpty2");
    boolean pass = true;
    try {
      portEnabled.addNumbersFault7(20, 10);
    } catch (TooBigNumbersException_Exception ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (AddNumbersException_Exception ex) {
      TestUtil.logErr("Caught unexpected AddNumbersException_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testOnlyFaultActionsFault7BothEmpty2 failed", ex);
    }
    if (!pass)
      throw new Fault("testOnlyFaultActionsFault7BothEmpty2 failed");
  }
}
