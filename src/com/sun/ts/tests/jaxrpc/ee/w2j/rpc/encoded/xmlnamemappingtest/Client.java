/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
 * Copyright (c) 2002 International Business Machines Corp. All rights reserved.
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

package com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.xmlnamemappingtest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;

import javax.xml.rpc.*;

import java.util.*;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxrpc.common.*;

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

  String modeProperty = null; // platform.mode -> (standalone|javaEE)

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.xmlnamemappingtest.";

  // EyeColor enumeration data
  public final static EyeColor eyeColor_data = EyeColor.Brown;

  // SoapStruct struct data
  public final static SoapStruct soapStruct_data = new SoapStruct();

  static {
    soapStruct_data.setVarString("string1");
    soapStruct_data.setVarInt(Integer.MIN_VALUE);
    soapStruct_data.setVarFloat(Float.MIN_VALUE);
  }

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "xmlnamemappingtest.endpoint.1";

  private static final String WSDLLOC_URL = "xmlnamemappingtest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXRPC_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  // Get Port and Stub access via porting layer interface
  XMLNameMappingTest port = null;

  Stub stub = null;

  private void getStubStandalone() throws Exception {
    TestUtil.logMsg("Get stub from service implementation class"
        + " using JAXRPC porting instance");
    port = (XMLNameMappingTest) JAXRPC_Util.getStub(
        "com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded."
            + "xmlnamemappingtest.XMLNameMappingTestService",
        "getXMLNameMappingTestService");
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub = (javax.xml.rpc.Stub) port;
  }

  private void getStub() throws Exception {
    try {
      InitialContext ic = new InitialContext();
      TestUtil.logMsg("Lookup java:comp/env/service/w2jxmlnamemappingtest");
      javax.xml.rpc.Service svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/w2jxmlnamemappingtest");
      port = (XMLNameMappingTest) svc.getPort(XMLNameMappingTest.class);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
  }

  public static void main(String[] args) {
    Client theTests = new Client();
    Status s = theTests.run(args, System.out, System.err);
    s.exit();
  }

  /* Test setup */

  /*
   * @class.testArgs: -ap jaxrpc-url-props.dat
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
        getStubStandalone();
        TestUtil.logMsg("Setting target endpoint to " + url + " ...");
        stub._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url);
      } else {
        getStub();
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
   * @assertion_ids: JAXRPC:SPEC:51; JAXRPC:SPEC:113; JAXRPC:SPEC:124;
   * JAXRPC:SPEC:526; JAXRPC:SPEC:527; JAXRPC:SPEC:528; JAXRPC:SPEC:529;
   * WS4EE:SPEC:35; WS4EE:SPEC:36;
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
   * @assertion_ids: JAXRPC:SPEC:51; JAXRPC:SPEC:113; JAXRPC:SPEC:124;
   * JAXRPC:SPEC:526; JAXRPC:SPEC:527; JAXRPC:SPEC:528; JAXRPC:SPEC:529;
   * WS4EE:SPEC:35; WS4EE:SPEC:36;
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
      port._null();
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
      port._true();
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
      port._false();
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
      port._abstract();
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
      port._boolean();
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
      port._break();
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
      port._byte();
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
      port._case();
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
      port._catch();
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
      port._char();
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
      port._class();
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
      port._const();
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
      port._continue();
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
      port._default();
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
      port._do();
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
      port._double();
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
      port._else();
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
      port._extends();
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
      port._final();
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
      port._finally();
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
      port._float();
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
      port._for();
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
      port._goto();
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
      port._if();
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
      port._implements();
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
      port._import();
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
      port._instanceof();
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
      port._int();
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
      port._interface();
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
      port._long();
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
      port._native();
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
      port._new();
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
      port._package();
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
      port._private();
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
      port._protected();
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
      port._public();
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
      port._return();
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
      port._short();
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
      port._static();
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
      port._super();
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
      port._switch();
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
      port._synchronized();
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
      port._this();
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
      port._throw();
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
      port._throws();
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
      port._transient();
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
      port._try();
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
      port._void();
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
      port._volatile();
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
      port._while();
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
      SoapStruct o = port.echoSoapStruct(soapStruct_data);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "structTest");
    return pass;
  }
}
