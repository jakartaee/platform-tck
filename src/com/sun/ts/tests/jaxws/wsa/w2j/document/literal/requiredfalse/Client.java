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

package com.sun.ts.tests.jaxws.wsa.w2j.document.literal.requiredfalse;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import jakarta.xml.soap.SOAPBody;
import jakarta.xml.soap.SOAPException;

import com.sun.ts.tests.jaxws.wsa.common.ActionNotSupportedException;

import com.sun.ts.tests.jaxws.common.*;

import java.net.URL;

import jakarta.xml.ws.*;
import jakarta.xml.soap.*;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.wsa.w2j.document.literal.requiredfalse.";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "wsaw2jdlrequiredfalsetest.endpoint.1";

  private static final String WSDLLOC_URL = "wsaw2jdlrequiredfalsetest.wsdlloc.1";

  private String url = null;

  // service and port information
  private static final String NAMESPACEURI = "http://example.com/";

  private static final String SERVICE_NAME = "AddNumbersService";

  private static final String PORT_NAME = "AddNumbersPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private URL wsdlurl = null;

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
   * @testName: testDefaultActions
   *
   * @assertion_ids: WSAMD:SPEC:3000; WSAMD:SPEC:3000.1; WSAMD:SPEC:3000.2;
   * WSAMD:SPEC:3000.3; WSAMD:SPEC:3000.4; WSAMD:SPEC:4004; WSAMD:SPEC:4004.1;
   * WSAMD:SPEC:4004.2;
   *
   * @test_Strategy:
   *
   */
  public void testDefaultActions() throws Fault {
    TestUtil.logMsg("testDefaultActions");
    boolean pass = true;

    try {
      int result = port.addNumbers(10, 10);
      if (result != 20) {
        TestUtil.logErr("result mismatch, expected 20, received " + result);
        pass = false;
      } else
        TestUtil.logMsg("result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("testDefaultActions failed", e);
    }

    if (!pass)
      throw new Fault("testDefaultActions failed");
  }

  /*
   * @testName: testActionWithExplicitNames
   *
   * @assertion_ids: WSAMD:SPEC:3000; WSAMD:SPEC:3000.1; WSAMD:SPEC:3000.2;
   * WSAMD:SPEC:3000.3; WSAMD:SPEC:3000.4; WSAMD:SPEC:4004;
   *
   * @test_Strategy:
   *
   */
  public void testActionWithExplicitNames() throws Fault {
    TestUtil.logMsg("testActionWithExplicitNames");
    boolean pass = true;

    try {
      int result = port.addNumbers2(10, 10);
      if (result != 20) {
        TestUtil.logErr("result mismatch, expected 20, received " + result);
        pass = false;
      } else
        TestUtil.logMsg("result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("testActionWithExplicitNames failed", e);
    }

    if (!pass)
      throw new Fault("testActionWithExplicitNames failed");
  }

  /*
   * @testName: testActionWithInputNameOnly
   *
   * @assertion_ids: WSAMD:SPEC:3000; WSAMD:SPEC:3000.1; WSAMD:SPEC:3000.2;
   * WSAMD:SPEC:3000.3; WSAMD:SPEC:3000.4; WSAMD:SPEC:4004; WSAMD:SPEC:4004.2;
   *
   * @test_Strategy:
   *
   */
  public void testActionWithInputNameOnly() throws Fault {
    TestUtil.logMsg("testActionWithInputNameOnly");
    boolean pass = true;

    try {
      int result = port.addNumbers3(10, 10);
      if (result != 20) {
        TestUtil.logErr("result mismatch, expected 20, received " + result);
        pass = false;
      } else
        TestUtil.logMsg("result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("testActionWithInputNameOnly failed", e);
    }

    if (!pass)
      throw new Fault("testActionWithInputNameOnly failed");
  }

  /*
   * @testName: testActionWithOutputNameOnly
   *
   * @assertion_ids: WSAMD:SPEC:3000; WSAMD:SPEC:3000.1; WSAMD:SPEC:3000.2;
   * WSAMD:SPEC:3000.3; WSAMD:SPEC:3000.4; WSAMD:SPEC:4004; WSAMD:SPEC:4004.1;
   *
   * @test_Strategy:
   *
   */
  public void testActionWithOutputNameOnly() throws Fault {
    TestUtil.logMsg("testActionWithOutputNameOnly");
    boolean pass = true;

    try {
      int result = port.addNumbers4(10, 10);
      if (result != 20) {
        TestUtil.logErr("result mismatch, expected 20, received " + result);
        pass = false;
      } else
        TestUtil.logMsg("result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("testActionWithOutputNameOnly failed", e);
    }

    if (!pass)
      throw new Fault("testActionWithOutputNameOnly failed");
  }

  /*
   * @testName: testExplicitActionsBoth
   *
   * @assertion_ids: WSAMD:SPEC:3000; WSAMD:SPEC:3000.1; WSAMD:SPEC:3000.2;
   * WSAMD:SPEC:3000.3; WSAMD:SPEC:3000.4; WSAMD:SPEC:4003; WSAMD:SPEC:4003.1;
   * WSAMD:SPEC:4003.2;
   *
   * @test_Strategy:
   *
   */
  public void testExplicitActionsBoth() throws Fault {
    TestUtil.logMsg("testExplicitActionsBoth");
    boolean pass = true;

    try {
      int result = port.addNumbers5(10, 10);
      if (result != 20) {
        TestUtil.logErr("result mismatch, expected 20, received " + result);
        pass = false;
      } else
        TestUtil.logMsg("result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("testExplicitActionsBoth failed", e);
    }

    if (!pass)
      throw new Fault("testExplicitActionsBoth failed");
  }

  /*
   * @testName: testExplicitActionsInputOnly
   *
   * @assertion_ids: WSAMD:SPEC:3000; WSAMD:SPEC:3000.1; WSAMD:SPEC:3000.2;
   * WSAMD:SPEC:3000.3; WSAMD:SPEC:3000.4; WSAMD:SPEC:4000; WSAMD:SPEC:4003;
   * WSAMD:SPEC:4003.1;
   *
   * @test_Strategy:
   *
   */
  public void testExplicitActionsInputOnly() throws Fault {
    TestUtil.logMsg("testExplicitActionsInputOnly");
    boolean pass = true;

    try {
      int result = port.addNumbers6(10, 10);
      if (result != 20) {
        TestUtil.logErr("result mismatch, expected 20, received " + result);
        pass = false;
      } else
        TestUtil.logMsg("result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("testExplicitActionsInputOnly failed", e);
    }

    if (!pass)
      throw new Fault("testExplicitActionsInputOnly failed");
  }

  /*
   * @testName: testExplicitActionsOutputOnly
   *
   * @assertion_ids: WSAMD:SPEC:3000; WSAMD:SPEC:3000.1; WSAMD:SPEC:3000.2;
   * WSAMD:SPEC:3000.3; WSAMD:SPEC:3000.4; WSAMD:SPEC:4000; WSAMD:SPEC:4003;
   * WSAMD:SPEC:4003.2;
   *
   * @test_Strategy:
   *
   */
  public void testExplicitActionsOutputOnly() throws Fault {
    TestUtil.logMsg("testExplicitActionsOutputOnly");
    boolean pass = true;

    try {
      int result = port.addNumbers7(10, 10);
      if (result != 20) {
        TestUtil.logErr("result mismatch, expected 20, received " + result);
        pass = false;
      } else
        TestUtil.logMsg("result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("testExplicitActionsOutputOnly failed", e);
    }

    if (!pass)
      throw new Fault("testExplicitActionsOutputOnly failed");
  }

  /*
   * @testName: TestEmptyActions
   *
   * @assertion_ids: WSAMD:SPEC:3000; WSAMD:SPEC:3000.1; WSAMD:SPEC:3000.2;
   * WSAMD:SPEC:3000.3; WSAMD:SPEC:3000.4; WSAMD:SPEC:4004; WSAMD:SPEC:4003;
   * WSAMD:SPEC:4003.1; WSAMD:SPEC:4003.2;
   *
   * @test_Strategy:
   *
   */
  public void TestEmptyActions() throws Fault {
    TestUtil.logMsg("TestEmptyActions");
    boolean pass = true;

    try {
      int result = port.addNumbers8(10, 10);
      if (result != 20) {
        TestUtil.logErr("result mismatch, expected 20, received " + result);
        pass = false;
      } else
        TestUtil.logMsg("result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("TestEmptyActions failed", e);
    }

    if (!pass)
      throw new Fault("TestEmptyActions failed");
  }
}
