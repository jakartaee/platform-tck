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

package com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.xmlnamemappingtest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;

import jakarta.xml.ws.*;
import javax.xml.namespace.QName;

import java.util.*;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.common.*;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.xmlnamemappingtest.";

  // service and port information
  private static final String NAMESPACEURI = "http://XMLNameMappingTest.org/wsdl";

  private static final String SERVICE_NAME = "xMLNameMappingTest";

  private static final String PORT_NAME = "XMLNameMappingTestPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  // EyeColor data
  private static EyeColor eyeColor_data = null;

  // XMLNameMappingTest_Type struct data
  private static XMLNameMappingTest_Type xmlNameMapping_data = null;

  static {
    try {
      xmlNameMapping_data = new XMLNameMappingTest_Type();
      xmlNameMapping_data.setVarString("string1");
      xmlNameMapping_data.setVarInt(Integer.MIN_VALUE);
      xmlNameMapping_data.setVarFloat(Float.MIN_VALUE);

      eyeColor_data = new EyeColor();
      eyeColor_data.setColor("blue");
    } catch (Exception e) {
      TestUtil.logErr("exception on data initialization." + e);
      e.printStackTrace();
    }
  }

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "xmlnamemappingtest.endpoint.1";

  private static final String WSDLLOC_URL = "xmlnamemappingtest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  XMLNameMappingTest port = null;

  static XMLNameMappingTest_Service service = null;

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
    port = (XMLNameMappingTest) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        XMLNameMappingTest_Service.class, PORT_QNAME, XMLNameMappingTest.class);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (XMLNameMappingTest) service.getXMLNameMappingTestPort();
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
    JAXWS_Util.dumpTargetEndpointAddress(port);
    // JAXWS_Util.setSOAPLogging(port);
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
        service = (XMLNameMappingTest_Service) getSharedObject();
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

  private void printSeperationLine() {
    TestUtil.logMsg("---------------------------");
  }

  /*
   * @testName: JavaKeywordsTest
   *
   * @assertion_ids: JAXWS:SPEC:2057; JAXWS:SPEC:2066; JAXWS:SPEC:2067;
   * JAXWS:SPEC:2068;
   * 
   * @test_Strategy:
   */
  public void JavaKeywordsTest() throws Fault {
    TestUtil.logTrace("JavaKeywordsTest");
    boolean pass = true;

    if (!nullTest())
      pass = false;
    printSeperationLine();
    if (!trueTest())
      pass = false;
    printSeperationLine();
    if (!falseTest())
      pass = false;
    printSeperationLine();
    if (!abstractTest())
      pass = false;
    printSeperationLine();
    if (!booleanTest())
      pass = false;
    printSeperationLine();
    if (!breakTest())
      pass = false;
    printSeperationLine();
    if (!byteTest())
      pass = false;
    printSeperationLine();
    if (!caseTest())
      pass = false;
    printSeperationLine();
    if (!catchTest())
      pass = false;
    printSeperationLine();
    if (!charTest())
      pass = false;
    printSeperationLine();
    if (!classTest())
      pass = false;
    printSeperationLine();
    if (!constTest())
      pass = false;
    printSeperationLine();
    if (!continueTest())
      pass = false;
    printSeperationLine();
    if (!defaultTest())
      pass = false;
    printSeperationLine();
    if (!doTest())
      pass = false;
    printSeperationLine();
    if (!doubleTest())
      pass = false;
    printSeperationLine();
    if (!elseTest())
      pass = false;
    printSeperationLine();
    if (!extendsTest())
      pass = false;
    printSeperationLine();
    if (!finalTest())
      pass = false;
    printSeperationLine();
    if (!finallyTest())
      pass = false;
    printSeperationLine();
    if (!floatTest())
      pass = false;
    printSeperationLine();
    if (!forTest())
      pass = false;
    printSeperationLine();
    if (!gotoTest())
      pass = false;
    printSeperationLine();
    if (!ifTest())
      pass = false;
    printSeperationLine();
    if (!implementsTest())
      pass = false;
    printSeperationLine();
    if (!importTest())
      pass = false;
    printSeperationLine();
    if (!instanceofTest())
      pass = false;
    printSeperationLine();
    if (!intTest())
      pass = false;
    printSeperationLine();
    if (!interfaceTest())
      pass = false;
    printSeperationLine();
    if (!longTest())
      pass = false;
    printSeperationLine();
    if (!nativeTest())
      pass = false;
    printSeperationLine();
    if (!newTest())
      pass = false;
    printSeperationLine();
    if (!packageTest())
      pass = false;
    printSeperationLine();
    if (!privateTest())
      pass = false;
    printSeperationLine();
    if (!protectedTest())
      pass = false;
    printSeperationLine();
    if (!publicTest())
      pass = false;
    printSeperationLine();
    if (!returnTest())
      pass = false;
    printSeperationLine();
    if (!shortTest())
      pass = false;
    printSeperationLine();
    if (!staticTest())
      pass = false;
    printSeperationLine();
    if (!superTest())
      pass = false;
    printSeperationLine();
    if (!switchTest())
      pass = false;
    printSeperationLine();
    if (!synchronizedTest())
      pass = false;
    printSeperationLine();
    if (!thisTest())
      pass = false;
    printSeperationLine();
    if (!throwTest())
      pass = false;
    printSeperationLine();
    if (!throwsTest())
      pass = false;
    printSeperationLine();
    if (!transientTest())
      pass = false;
    printSeperationLine();
    if (!tryTest())
      pass = false;
    printSeperationLine();
    if (!voidTest())
      pass = false;
    printSeperationLine();
    if (!volatileTest())
      pass = false;
    printSeperationLine();
    if (!whileTest())
      pass = false;
    printSeperationLine();

    if (!pass)
      throw new Fault("JavaKeywordsTest failed");
  }

  /*
   * @testName: JavaNamingConventionsTest
   *
   * @assertion_ids: JAXWS:SPEC:2057; JAXWS:SPEC:2066; JAXWS:SPEC:2067;
   * JAXWS:SPEC:2068;
   * 
   * @test_Strategy:
   */
  public void JavaNamingConventionsTest() throws Fault {
    TestUtil.logTrace("JavaNamingConventionsTest");
    boolean pass = true;

    if (!eyeColorTest())
      pass = false;
    printSeperationLine();
    if (!structTest())
      pass = false;
    printSeperationLine();

    if (!pass)
      throw new Fault("JavaNamingConventionsTest failed");
  }

  private boolean printTestStatus(boolean pass, String test) {
    if (pass)
      TestUtil.logMsg("" + test + " ... PASSED");
    else
      TestUtil.logErr("" + test + " ... FAILED");

    return pass;
  }

  private boolean nullTest() {
    boolean pass = true;
    try {
      port.nullTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "nullTest");
    return pass;
  }

  private boolean trueTest() {
    boolean pass = true;
    try {
      port.trueTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "trueTest");
    return pass;
  }

  private boolean falseTest() {
    boolean pass = true;
    try {
      port.falseTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "falseTest");
    return pass;
  }

  private boolean abstractTest() {
    boolean pass = true;
    try {
      port.abstractTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "abstractTest");
    return pass;
  }

  private boolean booleanTest() {
    boolean pass = true;
    try {
      port.booleanTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "booleanTest");
    return pass;
  }

  private boolean breakTest() {
    boolean pass = true;
    try {
      port.breakTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "breakTest");
    return pass;
  }

  private boolean byteTest() {
    boolean pass = true;
    try {
      port.byteTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "byteTest");
    return pass;
  }

  private boolean caseTest() {
    boolean pass = true;
    try {
      port.caseTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "caseTest");
    return pass;
  }

  private boolean catchTest() {
    boolean pass = true;
    try {
      port.catchTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "catchTest");
    return pass;
  }

  private boolean charTest() {
    boolean pass = true;
    try {
      port.charTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "charTest");
    return pass;
  }

  private boolean classTest() {
    boolean pass = true;
    try {
      port.classTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "classTest");
    return pass;
  }

  private boolean constTest() {
    boolean pass = true;
    try {
      port.constTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "constTest");
    return pass;
  }

  private boolean continueTest() {
    boolean pass = true;
    try {
      port.continueTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "continueTest");
    return pass;
  }

  private boolean defaultTest() {
    boolean pass = true;
    try {
      port.defaultTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "defaultTest");
    return pass;
  }

  private boolean doTest() {
    boolean pass = true;
    try {
      port.doTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "doTest");
    return pass;
  }

  private boolean doubleTest() {
    boolean pass = true;
    try {
      port.doubleTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "doubleTest");
    return pass;
  }

  private boolean elseTest() {
    boolean pass = true;
    try {
      port.elseTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "elseTest");
    return pass;
  }

  private boolean extendsTest() {
    boolean pass = true;
    try {
      port.extendsTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "extendsTest");
    return pass;
  }

  private boolean finalTest() {
    boolean pass = true;
    try {
      port.finalTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "finalTest");
    return pass;
  }

  private boolean finallyTest() {
    boolean pass = true;
    try {
      port.finallyTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "finallyTest");
    return pass;
  }

  private boolean floatTest() {
    boolean pass = true;
    try {
      port.floatTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "floatTest");
    return pass;
  }

  private boolean forTest() {
    boolean pass = true;
    try {
      port.forTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "forTest");
    return pass;
  }

  private boolean gotoTest() {
    boolean pass = true;
    try {
      port.gotoTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "gotoTest");
    return pass;
  }

  private boolean ifTest() {
    boolean pass = true;
    try {
      port.ifTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "ifTest");
    return pass;
  }

  private boolean implementsTest() {
    boolean pass = true;
    try {
      port.implementsTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "implementsTest");
    return pass;
  }

  private boolean importTest() {
    boolean pass = true;
    try {
      port.importTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "importTest");
    return pass;
  }

  private boolean instanceofTest() {
    boolean pass = true;
    try {
      port.instanceofTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "instanceofTest");
    return pass;
  }

  private boolean intTest() {
    boolean pass = true;
    try {
      port.intTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "intTest");
    return pass;
  }

  private boolean interfaceTest() {
    boolean pass = true;
    try {
      port.interfaceTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "interfaceTest");
    return pass;
  }

  private boolean longTest() {
    boolean pass = true;
    try {
      port.longTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "longTest");
    return pass;
  }

  private boolean nativeTest() {
    boolean pass = true;
    try {
      port.nativeTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "nativeTest");
    return pass;
  }

  private boolean newTest() {
    boolean pass = true;
    try {
      port.newTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "newTest");
    return pass;
  }

  private boolean packageTest() {
    boolean pass = true;
    try {
      port.packageTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "packageTest");
    return pass;
  }

  private boolean privateTest() {
    boolean pass = true;
    try {
      port.privateTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "privateTest");
    return pass;
  }

  private boolean protectedTest() {
    boolean pass = true;
    try {
      port.protectedTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "protectedTest");
    return pass;
  }

  private boolean publicTest() {
    boolean pass = true;
    try {
      port.publicTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "publicTest");
    return pass;
  }

  private boolean returnTest() {
    boolean pass = true;
    try {
      port.returnTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "returnTest");
    return pass;
  }

  private boolean shortTest() {
    boolean pass = true;
    try {
      port.shortTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "shortTest");
    return pass;
  }

  private boolean staticTest() {
    boolean pass = true;
    try {
      port.staticTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "staticTest");
    return pass;
  }

  private boolean superTest() {
    boolean pass = true;
    try {
      port.superTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "superTest");
    return pass;
  }

  private boolean switchTest() {
    boolean pass = true;
    try {
      port.switchTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "switchTest");
    return pass;
  }

  private boolean synchronizedTest() {
    boolean pass = true;
    try {
      port.synchronizedTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "synchronizeTest");
    return pass;
  }

  private boolean thisTest() {
    boolean pass = true;
    try {
      port.thisTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "thisTest");
    return pass;
  }

  private boolean throwTest() {
    boolean pass = true;
    try {
      port.throwTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "throwTest");
    return pass;
  }

  private boolean throwsTest() {
    boolean pass = true;
    try {
      port.throwsTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "throwsTest");
    return pass;
  }

  private boolean transientTest() {
    boolean pass = true;
    try {
      port.transientTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "transientTest");
    return pass;
  }

  private boolean tryTest() {
    boolean pass = true;
    try {
      port.tryTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "tryTest");
    return pass;
  }

  private boolean voidTest() {
    boolean pass = true;
    try {
      port.voidTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "voidTest");
    return pass;
  }

  private boolean volatileTest() {
    boolean pass = true;
    try {
      port.volatileTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "volatileTest");
    return pass;
  }

  private boolean whileTest() {
    boolean pass = true;
    try {
      port.whileTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "whileTest");
    return pass;
  }

  private boolean eyeColorTest() {
    boolean pass = true;
    try {
      EyeColor o = port.echoEyeColor(eyeColor_data);
      TestUtil.logMsg("EyeColor=" + o);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "eyeColorTest");
    return pass;
  }

  private boolean structTest() {
    boolean pass = true;
    try {
      XMLNameMappingTest_Type o = port.echoXMLNameMapping(xmlNameMapping_data);
      TestUtil.logMsg("XMLNameMappingTest_Type=" + o);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "structTest");
    return pass;
  }
}
