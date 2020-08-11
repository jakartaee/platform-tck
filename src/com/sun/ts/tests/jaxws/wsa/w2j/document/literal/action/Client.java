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

package com.sun.ts.tests.jaxws.wsa.w2j.document.literal.action;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.*;
import jakarta.xml.ws.*;
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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.wsa.w2j.document.literal.action.";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "wsaw2jdlactiontest.endpoint.1";

  private static final String WSDLLOC_URL = "wsaw2jdlactiontest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  // service and port information
  private static final String NAMESPACEURI = "http://example.com/";

  private static final String SERVICE_NAME = "AddNumbersService";

  private static final String PORT_NAME = "AddNumbersPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  AddNumbersPortType port = null;

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
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (AddNumbersPortType) service.getPort(AddNumbersPortType.class);
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
   * @testName: testAddNumbersDefaultAddNumbersFaultAction
   *
   * @assertion_ids: WSAMD:SPEC:4004; WSAMD:SPEC:4004.3; JAXWS:SPEC:2089;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:7017;
   * JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.3; JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test default action pattern for WSDL fault element
   *
   */
  public void testAddNumbersDefaultAddNumbersFaultAction() throws Fault {
    TestUtil.logMsg("testAddNumbersDefaultAddNumbersFaultAction");
    boolean pass = true;
    try {
      port.addNumbers(-10, 10);
      TestUtil.logErr("AddNumbersFault_Exception must be thrown");
      pass = false;
    } catch (AddNumbersFault_Exception ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (TooBigNumbersFault_Exception ex) {
      TestUtil.logErr("Caught unexpected TooBigNumbersFault_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testAddNumbersDefaultAddNumbersFaultAction failed", ex);
    }
    if (!pass)
      throw new Fault("testAddNumbersDefaultAddNumbersFaultAction failed");
  }

  /*
   * @testName: testAddNumbersDefaultTooBigNumbersFaultAction
   *
   * @assertion_ids: WSAMD:SPEC:4004; WSAMD:SPEC:4004.3; JAXWS:SPEC:2089;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:7017;
   * JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.3; JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test default action pattern for WSDL fault element
   *
   */
  public void testAddNumbersDefaultTooBigNumbersFaultAction() throws Fault {
    TestUtil.logMsg("testAddNumbersDefaultTooBigNumbersFaultAction");
    boolean pass = true;
    try {
      port.addNumbers(20, 20);
      TestUtil.logErr("TooBigNumbersFault_Exception must be thrown");
      pass = false;
    } catch (TooBigNumbersFault_Exception ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (AddNumbersFault_Exception ex) {
      TestUtil.logErr("Caught unexpected AddNumbersFault_Exception");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testAddNumbersDefaultTooBigNumbersFaultAction failed",
          ex);
    }
    if (!pass)
      throw new Fault("testAddNumbersDefaultTooBigNumbersFaultAction failed");
  }

  /*
   * @testName: testAddNumbers2ExplicitAddNumbers2FaultAction
   *
   * @assertion_ids: WSAMD:SPEC:4003; WSAMD:SPEC:4003.3; JAXWS:SPEC:2089;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:7017;
   * JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.3; JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test explicit association for WSDL fault element
   *
   */
  public void testAddNumbers2ExplicitAddNumbers2FaultAction() throws Fault {
    TestUtil.logMsg("testAddNumbers2ExplicitAddNumbers2FaultAction");
    boolean pass = true;
    try {
      port.addNumbers2(-10, 10);
      TestUtil.logErr("AddNumbers2Fault must be thrown");
      pass = false;
    } catch (AddNumbers2Fault ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (TooBigNumbers2Fault ex) {
      TestUtil.logErr("Caught unexpected TooBigNumbers2Fault");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testAddNumbers2ExplicitAddNumbers2FaultAction failed",
          ex);
    }
    if (!pass)
      throw new Fault("testAddNumbers2ExplicitAddNumbers2FaultAction failed");
  }

  /*
   * @testName: testAddNumbers2ExplicitTooBigNumbers2FaultAction
   *
   * @assertion_ids: WSAMD:SPEC:4003; WSAMD:SPEC:4003.3; JAXWS:SPEC:2089;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:7017;
   * JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.3; JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test explicit association for WSDL fault element
   *
   */
  public void testAddNumbers2ExplicitTooBigNumbers2FaultAction() throws Fault {
    TestUtil.logMsg("testAddNumbers2ExplicitTooBigNumbers2FaultAction");
    boolean pass = true;
    try {
      port.addNumbers2(20, 20);
      TestUtil.logErr("TooBigNumbers2Fault must be thrown");
      pass = false;
    } catch (TooBigNumbers2Fault ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (AddNumbers2Fault ex) {
      TestUtil.logErr("Caught unexpected AddNumbers2Fault");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testAddNumbers2ExplicitTooBigNumbers2FaultAction failed",
          ex);
    }
    if (!pass)
      throw new Fault(
          "testAddNumbers2ExplicitTooBigNumbers2FaultAction failed");
  }

  /*
   * @testName: testAddNumbers3ExplicitAddNumbers3FaultAction
   *
   * @assertion_ids: WSAMD:SPEC:4003; WSAMD:SPEC:4003.3; JAXWS:SPEC:2089;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:7017;
   * JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.3; JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test explicit association for WSDL fault element
   *
   */
  public void testAddNumbers3ExplicitAddNumbers3FaultAction() throws Fault {
    TestUtil.logMsg("testAddNumbers3ExplicitAddNumbers3FaultAction");
    boolean pass = true;
    try {
      port.addNumbers3(-10, 10);
      TestUtil.logErr("AddNumbers3Fault must be thrown");
      pass = false;
    } catch (AddNumbers3Fault ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (TooBigNumbers3Fault ex) {
      TestUtil.logErr("Caught unexpected TooBigNumbers3Fault");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testAddNumbers3ExplicitAddNumbers3FaultAction failed",
          ex);
    }
    if (!pass)
      throw new Fault("testAddNumbers3ExplicitAddNumbers3FaultAction failed");
  }

  /*
   * @testName: testAddNumbers3DefaultTooBigNumbers3FaultAction
   *
   * @assertion_ids: WSAMD:SPEC:4004; WSAMD:SPEC:4004.3; JAXWS:SPEC:2089;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:7017;
   * JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.3; JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test default action pattern for WSDL fault element
   *
   */
  public void testAddNumbers3DefaultTooBigNumbers3FaultAction() throws Fault {
    TestUtil.logMsg("testAddNumbers3DefaultTooBigNumbers3FaultAction");
    boolean pass = true;
    try {
      port.addNumbers3(20, 20);
      TestUtil.logErr("TooBigNumbers3Fault must be thrown");
      pass = false;
    } catch (TooBigNumbers3Fault ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (AddNumbers3Fault ex) {
      TestUtil.logErr("Caught unexpected AddNumbers3Fault");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testAddNumbers3DefaultTooBigNumbers3FaultAction failed",
          ex);
    }
    if (!pass)
      throw new Fault("testAddNumbers3DefaultTooBigNumbers3FaultAction failed");
  }

  /*
   * @testName: testAddNumbers4DefaultAddNumbers4FaultAction
   *
   * @assertion_ids: WSAMD:SPEC:4004; WSAMD:SPEC:4004.3; JAXWS:SPEC:2089;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:7017;
   * JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.3; JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test default action pattern for WSDL fault element
   *
   */
  public void testAddNumbers4DefaultAddNumbers4FaultAction() throws Fault {
    TestUtil.logMsg("testAddNumbers4DefaultAddNumbers4FaultAction");
    boolean pass = true;
    try {
      port.addNumbers4(-10, 10);
      TestUtil.logErr("AddNumbers4Fault must be thrown");
      pass = false;
    } catch (AddNumbers4Fault ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (TooBigNumbers4Fault ex) {
      TestUtil.logErr("Caught unexpected TooBigNumbers4Fault");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testAddNumbers4DefaultAddNumbers4FaultAction failed",
          ex);
    }
    if (!pass)
      throw new Fault("testAddNumbers4DefaultAddNumbers4FaultAction failed");
  }

  /*
   * @testName: testAddNumbers4ExplicitTooBigNumbers4FaultAction
   *
   * @assertion_ids: WSAMD:SPEC:4003; WSAMD:SPEC:4003.3; JAXWS:SPEC:2089;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:7017;
   * JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.3; JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test explicit association for WSDL fault element
   *
   */
  public void testAddNumbers4ExplicitTooBigNumbers4FaultAction() throws Fault {
    TestUtil.logMsg("testAddNumbers4ExplicitTooBigNumbers4FaultAction");
    boolean pass = true;
    try {
      port.addNumbers4(20, 20);
      TestUtil.logErr("TooBigNumbers4Fault must be thrown");
      pass = false;
    } catch (TooBigNumbers4Fault ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (AddNumbers4Fault ex) {
      TestUtil.logErr("Caught unexpected AddNumbers4Fault");
      pass = false;
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testAddNumbers4ExplicitTooBigNumbers4FaultAction failed",
          ex);
    }
    if (!pass)
      throw new Fault(
          "testAddNumbers4ExplicitTooBigNumbers4FaultAction failed");
  }

  /*
   * @testName: testAddNumbers5ExplicitAddNumbers5FaultAction
   *
   * @assertion_ids: WSAMD:SPEC:4003; WSAMD:SPEC:4003.3; JAXWS:SPEC:2089;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:7017;
   * JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.3; JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test explicit association for WSDL fault element
   *
   */
  public void testAddNumbers5ExplicitAddNumbers5FaultAction() throws Fault {
    TestUtil.logMsg("testAddNumbers5ExplicitAddNumbers5FaultAction");
    boolean pass = true;
    try {
      port.addNumbers5(-10, 20);
      TestUtil.logErr("AddNumbers5Fault must be thrown");
      pass = false;
    } catch (AddNumbers5Fault ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testAddNumbers5ExplicitAddNumbers5FaultAction failed",
          ex);
    }
    if (!pass)
      throw new Fault("testAddNumbers5ExplicitAddNumbers5FaultAction failed");
  }

  /*
   * @testName: testAddNumbers6EmptyAddNumbers6FaultAction
   *
   * @assertion_ids: WSAMD:SPEC:4004; WSAMD:SPEC:4004.3; JAXWS:SPEC:2089;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:7017;
   * JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.3; JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test default association for WSDL fault element when an the
   * Action value is empty string ""
   *
   */
  public void testAddNumbers6EmptyAddNumbers6FaultAction() throws Fault {
    TestUtil.logMsg("testAddNumbers6EmptyAddNumbers6FaultAction");
    boolean pass = true;
    try {
      port.addNumbers6(-10, 20);
      TestUtil.logErr("AddNumbers6Fault must be thrown");
      pass = false;
    } catch (AddNumbers6Fault ex) {
      TestUtil.logMsg("WSA:Action headers are correct");
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testAddNumbers6EmptyAddNumbers6FaultAction failed", ex);
    }
    if (!pass)
      throw new Fault("testAddNumbers6EmptyAddNumbers6FaultAction failed");
  }

  /*
   * @testName: testDefaultInputOutputActionExplicitMessageNames
   *
   * @assertion_ids: WSAMD:SPEC:4004; WSAMD:SPEC:4004.1; WSAMD:SPEC:4004.2;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:7017;
   * JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.3; JAXWS:SPEC:2089; JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test default association for WSDL input/output elements and
   * explicit message names specified
   *
   */
  public void testDefaultInputOutputActionExplicitMessageNames() throws Fault {
    TestUtil.logMsg("testDefaultInputOutputActionExplicitMessageNames");
    boolean pass = true;
    try {
      port.addNumbers2(10, 10);
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testDefaultInputOutputActionExplicitMessageNames failed",
          ex);
    }
    if (!pass)
      throw new Fault(
          "testDefaultInputOutputActionExplicitMessageNames failed");
  }

  /*
   * @testName: testDefaultInputOutputAction
   *
   * @assertion_ids: WSAMD:SPEC:4004; WSAMD:SPEC:4004.3; JAXWS:SPEC:2089;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:7017;
   * JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.3; JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test default association for WSDL input/output elements and
   * no message names specified
   *
   */
  public void testDefaultInputOutputAction() throws Fault {
    TestUtil.logMsg("testDefaultInputOutputAction");
    boolean pass = true;
    try {
      port.addNumbers3(10, 10);
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testDefaultInputOutputAction failed", ex);
    }
    if (!pass)
      throw new Fault("testDefaultInputOutputAction failed");
  }

  /*
   * @testName: testEmptyInputOutputAction
   *
   * @assertion_ids: WSAMD:SPEC:4004; WSAMD:SPEC:4004.3; JAXWS:SPEC:2089;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:7017;
   * JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.3; JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test default association for WSDL input/output elements
   * when the Action value is empty string ""
   *
   */
  public void testEmptyInputOutputAction() throws Fault {
    TestUtil.logMsg("testEmptyInputOutputAction");
    boolean pass = true;
    try {
      port.addNumbers4(10, 10);
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testEmptyInputOutputAction failed", ex);
    }
    if (!pass)
      throw new Fault("testEmptyInputOutputAction failed");
  }

  /*
   * @testName: testExplicitInputOutputActions
   *
   * @assertion_ids: WSAMD:SPEC:4003; WSAMD:SPEC:4003.1; WSAMD:SPEC:4003.2;
   * JAXWS:SPEC:7018; JAXWS:SPEC:7018.1; JAXWS:SPEC:7018.2; JAXWS:SPEC:7017;
   * JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.3; JAXWS:SPEC:2089; JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test explicit association for WSDL input/output elements
   *
   */
  public void testExplicitInputOutputActions() throws Fault {
    TestUtil.logMsg("testExplicitInputOutputActions");
    boolean pass = true;
    try {
      port.addNumbers5(10, 10);
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testExplicitInputOutputActions failed", ex);
    }
    if (!pass)
      throw new Fault("testExplicitInputOutputActions failed");
  }

  /*
   * @testName: testExplicitInputDefaultOutputAction
   *
   * @assertion_ids: WSAMD:SPEC:4003; WSAMD:SPEC:4003.1; WSAMD:SPEC:4004;
   * WSAMD:SPEC:4004.2; JAXWS:SPEC:2089; JAXWS:SPEC:7018; JAXWS:SPEC:7018.1;
   * JAXWS:SPEC:7018.2; JAXWS:SPEC:7017; JAXWS:SPEC:7017.1; JAXWS:SPEC:7017.3;
   * JAXWS:SPEC:10027;
   *
   * @test_Strategy: Test explicit association for WSDL input element and
   * default association for WSDL output element
   *
   */
  public void testExplicitInputDefaultOutputAction() throws Fault {
    TestUtil.logMsg("testExplicitInputDefaultOutputAction");
    boolean pass = true;
    try {
      port.addNumbers6(10, 10);
    } catch (Exception ex) {
      TestUtil.logErr("Caught unexpected Exception " + ex.getMessage());
      throw new Fault("testExplicitInputDefaultOutputAction failed", ex);
    }
    if (!pass)
      throw new Fault("testExplicitInputDefaultOutputAction failed");
  }
}
