/*
 * Copyright (c) 2007, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.jaxrpc.ee.wsi.document.literal.marshalltest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import com.sun.ts.tests.jaxrpc.ee.wsi.document.literal.marshalltest.*;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.*;

import javax.xml.rpc.*;
import javax.xml.soap.*;

import java.util.Properties;
import java.util.GregorianCalendar;
import java.util.Calendar;

import java.math.BigInteger;
import java.math.BigDecimal;

import javax.xml.namespace.QName;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.ee.wsi.document.literal.marshalltest.";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "wsidlmarshalltest.endpoint.1";

  private static final String ENDPOINT2_URL = "wsidlmarshalltest.endpoint.2";

  private static final String ENDPOINT3_URL = "wsidlmarshalltest.endpoint.3";

  private static final String ENDPOINT4_URL = "wsidlmarshalltest.endpoint.4";

  private static final String WSDLLOC_URL = "wsidlmarshalltest.wsdlloc.1";

  private String url = null;

  private String url2 = null;

  private String url3 = null;

  private String url4 = null;

  private URL wsdlurl = null;

  private void getTestURLs() throws Exception {
    TestUtil.logMsg("Get URL's used by the test");
    String file = JAXRPC_Util.getURLFromProp(ENDPOINT_URL);
    url = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(ENDPOINT2_URL);
    url2 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);

    file = JAXRPC_Util.getURLFromProp(ENDPOINT3_URL);
    url3 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);
    file = JAXRPC_Util.getURLFromProp(ENDPOINT4_URL);
    url4 = ctsurl.getURLString(PROTOCOL, hostname, portnum, file);

    file = JAXRPC_Util.getURLFromProp(WSDLLOC_URL);
    wsdlurl = ctsurl.getURL(PROTOCOL, hostname, portnum, file);
    TestUtil.logMsg("Service Endpoint URL: " + url);
    TestUtil.logMsg("WSDL Location URL:    " + wsdlurl);
  }

  // Get Port and Stub access via porting layer interface
  MarshallTest port = null;

  NewSchemaTest port2 = null;

  CompoundTest port3 = null;

  OneWayTest port4 = null;

  Stub stub = null;

  Stub stub2 = null;

  Stub stub3 = null;

  Stub stub4 = null;

  private void getStubStandalone() throws Exception {
    TestUtil.logMsg("Get stub from service implementation class"
        + " using JAXRPC porting instance");
    port = (MarshallTest) JAXRPC_Util
        .getStub("com.sun.ts.tests.jaxrpc.ee.wsi.document.literal."
            + "marshalltest.MarshallTestService", "getMarshallTestPort1");

    port2 = (NewSchemaTest) JAXRPC_Util
        .getStub("com.sun.ts.tests.jaxrpc.ee.wsi.document.literal."
            + "marshalltest.MarshallTestService", "getMarshallTestPort2");

    port3 = (CompoundTest) JAXRPC_Util
        .getStub("com.sun.ts.tests.jaxrpc.ee.wsi.document.literal."
            + "marshalltest.MarshallTestService", "getMarshallTestPort3");

    port4 = (OneWayTest) JAXRPC_Util
        .getStub("com.sun.ts.tests.jaxrpc.ee.wsi.document.literal."
            + "marshalltest.MarshallTestService", "getMarshallTestPort4");

    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub = (javax.xml.rpc.Stub) port;
    stub2 = (javax.xml.rpc.Stub) port2;
    stub3 = (javax.xml.rpc.Stub) port3;
    stub4 = (javax.xml.rpc.Stub) port4;
  }

  private void getStub() throws Exception {
    TestUtil.logMsg("JNDI lookup for Service1");
    InitialContext ctx = new InitialContext();
    TestUtil.logMsg("Lookup java:comp/env/service/wsidlmarshalltest");
    Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/wsidlmarshalltest");
    TestUtil.logMsg("Get port1 from Service1");
    port = (MarshallTest) svc.getPort(
        com.sun.ts.tests.jaxrpc.ee.wsi.document.literal.marshalltest.MarshallTest.class);
    TestUtil.logMsg("Get port2 from Service1");
    port2 = (NewSchemaTest) svc.getPort(
        com.sun.ts.tests.jaxrpc.ee.wsi.document.literal.marshalltest.NewSchemaTest.class);

    port3 = (CompoundTest) svc.getPort(
        com.sun.ts.tests.jaxrpc.ee.wsi.document.literal.marshalltest.CompoundTest.class);

    port4 = (OneWayTest) svc.getPort(
        com.sun.ts.tests.jaxrpc.ee.wsi.document.literal.marshalltest.OneWayTest.class);

    TestUtil.logMsg("Port obtained");
  }

  private JavaBean JavaBean_data[] = null;

  private JavaBean2 JavaBean2_data[] = null;

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
        stub2._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url2);
        stub3._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url3);
        stub4._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, url4);
        JAXRPC_Util.setSOAPLogging(stub2, System.out);
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
    JavaBean_data = new JavaBean[2];
    JavaBean_data[0] = new JavaBean();
    JavaBean_data[1] = new JavaBean();
    JavaBean2_data = new JavaBean2[2];
    JavaBean2_data[0] = new JavaBean2();
    JavaBean2_data[1] = new JavaBean2();
    logMsg("setup ok");
  }

  private void printSeperationLine() {
    TestUtil.logMsg("---------------------------");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  // ====================================================================
  // Java Primitive Types
  // ====================================================================

  /*
   * @testName: MarshallPrimitiveTest
   *
   * @assertion_ids: JAXRPC:SPEC:52; JAXRPC:SPEC:295; JAXRPC:SPEC:296;
   * JAXRPC:SPEC:280; JAXRPC:SPEC:502; WS4EE:SPEC:177; WS4EE:SPEC:180;
   * WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224; WS4EE:SPEC:226;
   * WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232; WS4EE:SPEC:233;
   * WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237; WS4EE:SPEC:238;
   * WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244; WS4EE:SPEC:246;
   * WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each primitive type pass its value as input to the
   * corresponding RPC method and receive it back as the return value. Compare
   * results of each value/type of what was sent and what was returned. Verify
   * they are equal.
   *
   * Description
   */
  public void MarshallPrimitiveTest() throws Fault {
    TestUtil.logMsg("MarshallPrimitiveTest");
    boolean pass = true;

    if (!booleanTest())
      pass = false;
    if (!byteTest())
      pass = false;
    if (!shortTest())
      pass = false;
    if (!intTest())
      pass = false;
    if (!longTest())
      pass = false;
    if (!floatTest())
      pass = false;
    if (!doubleTest())
      pass = false;

    if (!pass)
      throw new Fault("MarshallPrimitiveTest failed");
  }

  // ====================================================================
  // Standard Java Classes
  // ====================================================================

  /*
   * @testName: MarshallStandardJavaClassesTest
   *
   * @assertion_ids: JAXRPC:SPEC:52; JAXRPC:SPEC:295; JAXRPC:SPEC:296;
   * JAXRPC:SPEC:280; WS4EE:SPEC:177; WS4EE:SPEC:179; WS4EE:SPEC:180;
   * WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224; WS4EE:SPEC:226;
   * WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232; WS4EE:SPEC:233;
   * WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237; WS4EE:SPEC:238;
   * WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244; WS4EE:SPEC:246;
   * WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * standard java class type. For each standard java class above pass its value
   * as input to the corresponding RPC method and receive it back as the return
   * value. Compare results of each value/type of what was sent sent and what
   * was returned. Verify they are equal.
   *
   * Description
   */
  public void MarshallStandardJavaClassesTest() throws Fault {
    TestUtil.logMsg("MarshallStandardJavaClassesTest");
    boolean pass = true;

    if (!StringTest())
      pass = false;
    if (!CalendarTest())
      pass = false;
    if (!BigIntegerTest())
      pass = false;
    if (!BigDecimalTest())
      pass = false;

    if (!pass)
      throw new Fault("MarshallStandardJavaClassesTest failed");
  }

  // ====================================================================
  // JavaBeans Class
  // ====================================================================

  /*
   * @testName: MarshallJavaBeanTest
   *
   * @assertion_ids: JAXRPC:SPEC:59; JAXRPC:SPEC:60; JAXRPC:SPEC:63;
   * JAXRPC:SPEC:295; JAXRPC:SPEC:296; JAXRPC:SPEC:280; WS4EE:SPEC:177;
   * WS4EE:SPEC:179; WS4EE:SPEC:180; WS4EE:SPEC:182; WS4EE:SPEC:207;
   * WS4EE:SPEC:224; WS4EE:SPEC:226; WS4EE:SPEC:228; WS4EE:SPEC:231;
   * WS4EE:SPEC:232; WS4EE:SPEC:233; WS4EE:SPEC:235; WS4EE:SPEC:236;
   * WS4EE:SPEC:237; WS4EE:SPEC:238; WS4EE:SPEC:240; WS4EE:SPEC:241;
   * WS4EE:SPEC:244; WS4EE:SPEC:246; WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method
   * JavaBeanTest. Pass a JavaBean value to the RPC method and receive it back
   * as the return value. Compare results of JavaBean value from what was sent
   * and what was returned. Verify they are equal.
   *
   * Description
   */
  public void MarshallJavaBeanTest() throws Fault {
    TestUtil.logMsg("MarshallJavaBeanTest");
    boolean pass = true;

    init_JavaBean_Data();
    JavaBean values[] = JavaBean_data;
    JavaBeanTestResponse response;
    JavaBeanTest request;
    TestUtil.logMsg(
        "Passing/Returning JavaBean JavaBean class to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        request = new JavaBeanTest();
        request.setJavaBean(values[i]);
        response = port.javaBeanTest(request);
        if (!compareJavaBeans(values[i], response.getJavaBean()))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallJavaBeanTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallJavaBeanTest failed");
  }

  // ====================================================================
  // Java Array Single-Dimensional for all supported JAX-RPC types
  // ====================================================================

  /*
   * @testName: MarshallJavaArrayTest
   *
   * @assertion_ids: JAXRPC:SPEC:59; JAXRPC:SPEC:63; JAXRPC:SPEC:295;
   * JAXRPC:SPEC:280; JAXRPC:SPEC:296; WS4EE:SPEC:177; WS4EE:SPEC:179;
   * WS4EE:SPEC:180; WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224;
   * WS4EE:SPEC:226; WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232;
   * WS4EE:SPEC:233; WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237;
   * WS4EE:SPEC:238; WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244;
   * WS4EE:SPEC:246; WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, invoke the RPC methods for each
   * java type supported. For each java type supported pass an arrary of values
   * as input to the corresponding RPC method and receive it back as the return
   * value. Compare results of each array type of what was sent and what was
   * returned. Verify they are equal.
   *
   * Description
   */
  public void MarshallJavaArrayTest() throws Fault {
    TestUtil.logMsg("MarshallJavaArrayTest");
    boolean pass = true;

    if (!booleanArrayTest())
      pass = false;
    if (!byteArrayTest())
      pass = false;
    if (!shortArrayTest())
      pass = false;
    if (!intArrayTest())
      pass = false;
    if (!longArrayTest())
      pass = false;
    if (!floatArrayTest())
      pass = false;
    if (!doubleArrayTest())
      pass = false;
    if (!StringArrayTest())
      pass = false;
    if (!CalendarArrayTest())
      pass = false;
    if (!BigIntegerArrayTest())
      pass = false;
    if (!BigDecimalArrayTest())
      pass = false;
    if (!JavaBeanArrayTest())
      pass = false;
    if (!QNameArrayTest())
      pass = false;

    if (!pass)
      throw new Fault("MarshallJavaArrayTest failed");
  }
  // ====================================================================
  // The void type
  // ====================================================================

  /*
   * @testName: MarshallVoidTest
   *
   * @assertion_ids: JAXRPC:SPEC:31; JAXRPC:SPEC:59; JAXRPC:SPEC:63;
   * JAXRPC:SPEC:295; JAXRPC:SPEC:296; JAXRPC:SPEC:280; WS4EE:SPEC:177;
   * WS4EE:SPEC:179; WS4EE:SPEC:180; WS4EE:SPEC:182; WS4EE:SPEC:207;
   * WS4EE:SPEC:224; WS4EE:SPEC:226; WS4EE:SPEC:228; WS4EE:SPEC:231;
   * WS4EE:SPEC:232; WS4EE:SPEC:233; WS4EE:SPEC:235; WS4EE:SPEC:236;
   * WS4EE:SPEC:237; WS4EE:SPEC:238; WS4EE:SPEC:240; WS4EE:SPEC:241;
   * WS4EE:SPEC:244; WS4EE:SPEC:246; WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method voidTest.
   * Verify normal invocation and return.
   *
   * Description
   */
  public void MarshallVoidTest() throws Fault {
    TestUtil.logMsg("MarshallVoidTest");
    boolean pass = true;
    VoidTestResponse response;
    TestUtil.logMsg("Handling a void type to/from JAXRPC Service");
    try {
      response = port.voidTest(new VoidTest());
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallVoidTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallVoidTest failed");
  }

  // =======================================================================
  // Marshall Other XML Schema Types (Any, List, Union, Anonymous, ...)
  // =======================================================================

  /*
   * @testName: MarshallNormalizedStringTypeTest
   *
   * @assertion_ids: JAXRPC:SPEC:70; JAXRPC:SPEC:295; JAXRPC:SPEC:296;
   * JAXRPC:SPEC:280; JAXRPC:SPEC:64; JAXRPC:SPEC:502; WS4EE:SPEC:177;
   * WS4EE:SPEC:179; WS4EE:SPEC:180; WS4EE:SPEC:182; WS4EE:SPEC:207;
   * WS4EE:SPEC:224; WS4EE:SPEC:226; WS4EE:SPEC:228; WS4EE:SPEC:231;
   * WS4EE:SPEC:232; WS4EE:SPEC:233; WS4EE:SPEC:235; WS4EE:SPEC:236;
   * WS4EE:SPEC:237; WS4EE:SPEC:238; WS4EE:SPEC:240; WS4EE:SPEC:241;
   * WS4EE:SPEC:244; WS4EE:SPEC:246; WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method. Verify
   * normal invocation and return.
   *
   * Description
   */
  public void MarshallNormalizedStringTypeTest() throws Fault {
    TestUtil.logMsg("MarshallNormalizedStringTypeTest");
    boolean pass = true;

    String request = new String("123-ABC12");

    try {
      String response = port2.echoNormalizedStringTypeTest(request);
      TestUtil.logMsg("response=" + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallNormalizedStringTypeTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallNormalizedStringTypeTest failed");
  }

  /*
   * @testName: MarshallIntegerRangeTypeTest
   *
   * @assertion_ids: JAXRPC:SPEC:70; JAXRPC:SPEC:295; JAXRPC:SPEC:296;
   * JAXRPC:SPEC:280; WS4EE:SPEC:177; WS4EE:SPEC:179; WS4EE:SPEC:180;
   * WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224; WS4EE:SPEC:226;
   * WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232; WS4EE:SPEC:233;
   * WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237; WS4EE:SPEC:238;
   * WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244; WS4EE:SPEC:246;
   * WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method. Verify
   * normal invocation and return.
   *
   * Description
   */
  public void MarshallIntegerRangeTypeTest() throws Fault {
    TestUtil.logMsg("MarshallIntegerRangeTypeTest");
    boolean pass = true;

    BigInteger request = new BigInteger("101");

    try {
      TestUtil.logMsg("Sending request: " + request);
      BigInteger response = port2.echoIntegerRangeTypeTest(request);
      TestUtil.logMsg("response=" + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallIntegerRangeTypeTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallIntegerRangeTypeTest failed");
  }

  /*
   * @testName: MarshallEnumTypesTest
   *
   * @assertion_ids: JAXRPC:SPEC:68; JAXRPC:SPEC:69; JAXRPC:SPEC:295;
   * JAXRPC:SPEC:296; JAXRPC:SPEC:280; JAXRPC:SPEC:503; WS4EE:SPEC:177;
   * WS4EE:SPEC:179; WS4EE:SPEC:180; WS4EE:SPEC:182; WS4EE:SPEC:207;
   * WS4EE:SPEC:224; WS4EE:SPEC:226; WS4EE:SPEC:228; WS4EE:SPEC:231;
   * WS4EE:SPEC:232; WS4EE:SPEC:233; WS4EE:SPEC:235; WS4EE:SPEC:236;
   * WS4EE:SPEC:237; WS4EE:SPEC:238; WS4EE:SPEC:240; WS4EE:SPEC:241;
   * WS4EE:SPEC:244; WS4EE:SPEC:246; WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method. Verify
   * normal invocation and return.
   *
   * Description
   */
  public void MarshallEnumTypesTest() throws Fault {
    TestUtil.logMsg("MarshallEnumTypesTest");
    boolean pass = true;

    if (!StringEnumTypeTest())
      pass = false;
    if (!ByteEnumTypeTest())
      pass = false;
    if (!ShortEnumTypeTest())
      pass = false;
    if (!IntegerEnumTypeTest())
      pass = false;
    if (!IntEnumTypeTest())
      pass = false;
    if (!LongEnumTypeTest())
      pass = false;
    if (!DecimalEnumTypeTest())
      pass = false;
    if (!FloatEnumTypeTest())
      pass = false;
    if (!DoubleEnumTypeTest())
      pass = false;

    if (!pass)
      throw new Fault("MarshallEnumTypesTest failed");
  }

  /*
   * @testName: MarshallAnonymousTypeTest
   *
   * @assertion_ids: JAXRPC:SPEC:65; JAXRPC:SPEC:295; JAXRPC:SPEC:296;
   * JAXRPC:SPEC:280; WS4EE:SPEC:177; WS4EE:SPEC:179; WS4EE:SPEC:180;
   * WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224; WS4EE:SPEC:226;
   * WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232; WS4EE:SPEC:233;
   * WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237; WS4EE:SPEC:238;
   * WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244; WS4EE:SPEC:246;
   * WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method. Verify
   * normal invocation and return.
   *
   * Description
   */
  public void MarshallAnonymousTypeTest() throws Fault {
    TestUtil.logMsg("MarshallAnonymousTypeTest");
    boolean pass = true;

    init_FooAnonymousType_Data();
    FooAnonymousType request = FooAnonymousType_data;

    try {
      FooAnonymousType response = port2.echoAnonymousTypeTest(request);
      if (!compareFooAnonymousTypeData(request, response))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallAnonymousTypeTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallAnonymousTypeTest failed");
  }

  /*
   * @testName: MarshallVariousSchemaTypesTest
   *
   * @assertion_ids: JAXRPC:SPEC:52; JAXRPC:SPEC:64; JAXRPC:SPEC:295;
   * JAXRPC:SPEC:296; JAXRPC:SPEC:280; JAXRPC:SPEC:502; WS4EE:SPEC:177;
   * WS4EE:SPEC:179; WS4EE:SPEC:180; WS4EE:SPEC:182; WS4EE:SPEC:207;
   * WS4EE:SPEC:224; WS4EE:SPEC:226; WS4EE:SPEC:228; WS4EE:SPEC:231;
   * WS4EE:SPEC:232; WS4EE:SPEC:233; WS4EE:SPEC:235; WS4EE:SPEC:236;
   * WS4EE:SPEC:237; WS4EE:SPEC:238; WS4EE:SPEC:240; WS4EE:SPEC:241;
   * WS4EE:SPEC:244; WS4EE:SPEC:246; WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method. Verify
   * normal invocation and return.
   *
   * Description
   */
  public void MarshallVariousSchemaTypesTest() throws Fault {
    TestUtil.logMsg("MarshallVariousSchemaTypesTest");
    boolean pass = true;

    init_FooVariousSchemaTypes_Data();
    FooVariousSchemaTypes request = FooVariousSchemaTypes_data;

    try {
      TestUtil.logMsg("Send: " + request.getFooA() + "|" + request.getFooB()
          + "|" + request.getFooC() + "|" + request.getFooD() + "|"
          + request.getFooE() + "|" + request.getFooF());
      FooVariousSchemaTypes response = port2
          .echoVariousSchemaTypesTest(request);
      TestUtil.logMsg("Recv: " + response.getFooA() + "|" + response.getFooB()
          + "|" + response.getFooC() + "|" + response.getFooD() + "|"
          + response.getFooE() + "|" + response.getFooF());
      if (response.getFooA() == request.getFooA()
          && response.getFooB().equals(request.getFooB())
          && response.getFooC().equals(request.getFooC())
          && response.getFooD().equals(request.getFooD())
          && response.getFooE() == request.getFooE()
          && response.getFooF() == request.getFooF()) {
        TestUtil.logMsg("Result match");
      } else {
        TestUtil.logErr("Result mismatch");
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallVariousSchemaTypesTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallVariousSchemaTypesTest failed");
  }

  /*
   * @testName: MarshallVariousSchemaTypesListTypeTest
   *
   * @assertion_ids: JAXRPC:SPEC:52; JAXRPC:SPEC:63; JAXRPC:SPEC:64;
   * JAXRPC:SPEC:295; JAXRPC:SPEC:296; JAXRPC:SPEC:280; JAXRPC:SPEC:502;
   * WS4EE:SPEC:177; WS4EE:SPEC:179; WS4EE:SPEC:180; WS4EE:SPEC:182;
   * WS4EE:SPEC:207; WS4EE:SPEC:224; WS4EE:SPEC:226; WS4EE:SPEC:228;
   * WS4EE:SPEC:231; WS4EE:SPEC:232; WS4EE:SPEC:233; WS4EE:SPEC:235;
   * WS4EE:SPEC:236; WS4EE:SPEC:237; WS4EE:SPEC:238; WS4EE:SPEC:240;
   * WS4EE:SPEC:241; WS4EE:SPEC:244; WS4EE:SPEC:246; WS4EE:SPEC:247;
   * WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method. Verify
   * normal invocation and return.
   *
   * Description
   */
  public void MarshallVariousSchemaTypesListTypeTest() throws Fault {
    TestUtil.logMsg("MarshallVariousSchemaTypesListTypeTest");
    boolean pass = true;

    init_FooVariousSchemaTypes_Data();
    FooVariousSchemaTypesListType request = FooVariousSchemaTypesListType_data;

    try {
      FooVariousSchemaTypes[] fooSend = request.getFooA();
      TestUtil
          .logMsg("Send: " + fooSend[0].getFooA() + "|" + fooSend[0].getFooB()
              + "|" + fooSend[0].getFooC() + "|" + fooSend[0].getFooD() + "|"
              + fooSend[0].getFooE() + "|" + fooSend[0].getFooF() + ","
              + fooSend[1].getFooA() + "|" + fooSend[1].getFooB() + "|"
              + fooSend[1].getFooC() + "|" + fooSend[1].getFooD() + "|"
              + fooSend[1].getFooE() + "|" + fooSend[1].getFooF());
      FooVariousSchemaTypesListType response = port2
          .echoVariousSchemaTypesListTypeTest(request);
      FooVariousSchemaTypes[] fooRecv = response.getFooA();
      TestUtil
          .logMsg("Recv: " + fooRecv[0].getFooA() + "|" + fooRecv[0].getFooB()
              + "|" + fooRecv[0].getFooC() + "|" + fooRecv[0].getFooD() + "|"
              + fooRecv[0].getFooE() + "|" + fooRecv[0].getFooF() + ","
              + fooRecv[1].getFooA() + "|" + fooRecv[1].getFooB() + "|"
              + fooRecv[1].getFooC() + "|" + fooRecv[1].getFooD() + "|"
              + fooRecv[1].getFooE() + "|" + fooRecv[1].getFooF());
      if (fooRecv.length == fooSend.length) {
        for (int i = 0; i < fooSend.length; i++) {
          if (fooRecv[i].getFooA() == fooSend[i].getFooA()
              && fooRecv[i].getFooB().equals(fooSend[i].getFooB())
              && fooRecv[i].getFooC().equals(fooSend[i].getFooC())
              && fooRecv[i].getFooD().equals(fooSend[i].getFooD())) {
            TestUtil.logMsg("Result match");
          } else {
            TestUtil.logErr("Result mismatch");
            pass = false;
          }
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallVariousSchemaTypesListTypeTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallVariousSchemaTypesListTypeTest failed");
  }

  /*
   * @testName: MarshallAnnotationTypeTest
   *
   * @assertion_ids: JAXRPC:SPEC:64; JAXRPC:SPEC:502; JAXRPC:SPEC:295;
   * JAXRPC:SPEC:296; JAXRPC:SPEC:280; WS4EE:SPEC:177; WS4EE:SPEC:179;
   * WS4EE:SPEC:180; WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224;
   * WS4EE:SPEC:226; WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232;
   * WS4EE:SPEC:233; WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237;
   * WS4EE:SPEC:238; WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244;
   * WS4EE:SPEC:246; WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method. Verify
   * normal invocation and return.
   *
   * Description
   */
  public void MarshallAnnotationTypeTest() throws Fault {
    TestUtil.logMsg("MarshallAnnotationTypeTest");
    boolean pass = true;

    FooAnnotationType request = FooAnnotationType.fromValue("Unknown");
    try {
      FooAnnotationType response = port2.echoAnnotationTypeTest(request);
      if (!response.getValue().equals(request.getValue())) {
        TestUtil.logErr("Wrong response expected: <" + request.getValue()
            + ">, received: <" + response.getValue() + ">");
        pass = false;
      } else {
        TestUtil.logMsg("Correct response expected: <" + request.getValue()
            + ">, received: <" + response.getValue() + ">");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallAnnotationTypeTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallAnnotationTypeTest failed");
  }

  /*
   * @testName: MarshallAnySimpleTypeTest
   *
   * @assertion_ids: JAXRPC:SPEC:52; JAXRPC:SPEC:295; JAXRPC:SPEC:296;
   * JAXRPC:SPEC:280; JAXRPC:SPEC:502; WS4EE:SPEC:177; WS4EE:SPEC:179;
   * WS4EE:SPEC:180; WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224;
   * WS4EE:SPEC:226; WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232;
   * WS4EE:SPEC:233; WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237;
   * WS4EE:SPEC:238; WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244;
   * WS4EE:SPEC:246; WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method. Verify
   * normal invocation and return.
   *
   * Description
   */
  public void MarshallAnySimpleTypeTest() throws Fault {
    TestUtil.logMsg("MarshallAnySimpleTypeTest");
    boolean pass = true;
    String request = "hello,there";
    try {
      String response = port2.echoAnySimpleTypeTest(request);
      if (!response.equals(request)) {
        TestUtil.logErr("Wrong response expected: <" + request
            + ">, received: <" + response + ">");
        pass = false;
      } else {
        TestUtil.logMsg("Correct response expected: <" + request
            + ">, received: <" + response + ">");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallAnySimpleTypeTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallAnySimpleTypeTest failed");
  }

  /*
   * @testName: MarshallAnyURITypeTest
   *
   * @assertion_ids: JAXRPC:SPEC:52; JAXRPC:SPEC:295; JAXRPC:SPEC:296;
   * JAXRPC:SPEC:280; WS4EE:SPEC:177; WS4EE:SPEC:179; WS4EE:SPEC:180;
   * WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224; WS4EE:SPEC:226;
   * WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232; WS4EE:SPEC:233;
   * WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237; WS4EE:SPEC:238;
   * WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244; WS4EE:SPEC:246;
   * WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method. Verify
   * normal invocation and return.
   *
   * Description
   */
  public void MarshallAnyURITypeTest() throws Fault {
    TestUtil.logMsg("MarshallAnyURITypeTest");
    boolean pass = true;

    try {
      URI uriReq = new URI("http://example.com/myURI");
      FooAnyURIType request = new FooAnyURIType();
      request.setFooAnyURIType(uriReq);
      FooAnyURIType response = port2.echoAnyURITypeTest(request);
      URI uriRes = response.getFooAnyURIType();
      if (!uriRes.equals(uriReq)) {
        TestUtil.logErr("Wrong response expected: <" + uriReq + ">, received: <"
            + uriRes + ">");
        pass = false;
      } else {
        TestUtil.logMsg("Correct response expected: <" + uriReq
            + ">, received: <" + uriRes + ">");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallAnyURITypeTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallAnyURITypeTest failed");
  }

  /*
   * @testName: MarshallLanguageTypeTest
   *
   * @assertion_ids: JAXRPC:SPEC:64; JAXRPC:SPEC:502; JAXRPC:SPEC:295;
   * JAXRPC:SPEC:296; JAXRPC:SPEC:280; WS4EE:SPEC:177; WS4EE:SPEC:179;
   * WS4EE:SPEC:180; WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224;
   * WS4EE:SPEC:226; WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232;
   * WS4EE:SPEC:233; WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237;
   * WS4EE:SPEC:238; WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244;
   * WS4EE:SPEC:246; WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method. Verify
   * normal invocation and return.
   *
   * Description
   */
  public void MarshallLanguageTypeTest() throws Fault {
    TestUtil.logMsg("MarshallLanguageTypeTest");
    boolean pass = true;

    String request = "english";
    try {
      String response = port2.echoLanguageTypeTest(request);
      if (!response.equals(request)) {
        TestUtil.logErr("Wrong response expected: <" + request
            + ">, received: <" + response + ">");
        pass = false;
      } else {
        TestUtil.logMsg("Correct response expected: <" + request
            + ">, received: <" + response + ">");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallLanguageTypeTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallLanguageTypeTest failed");
  }

  /*
   * @testName: MarshallTokenTypeTest
   *
   * @assertion_ids: JAXRPC:SPEC:64; JAXRPC:SPEC:502; JAXRPC:SPEC:295;
   * JAXRPC:SPEC:296; JAXRPC:SPEC:280; WS4EE:SPEC:177; WS4EE:SPEC:179;
   * WS4EE:SPEC:180; WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224;
   * WS4EE:SPEC:226; WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232;
   * WS4EE:SPEC:233; WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237;
   * WS4EE:SPEC:238; WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244;
   * WS4EE:SPEC:246; WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method. Verify
   * normal invocation and return.
   *
   * Description
   */
  public void MarshallTokenTypeTest() throws Fault {
    TestUtil.logMsg("MarshallTokenTypeTest");
    boolean pass = true;

    String request = "token";
    try {
      String response = port2.echoTokenTypeTest(request);
      if (!response.equals(request)) {
        TestUtil.logErr("Wrong response expected: <" + request
            + ">, received: <" + response + ">");
        pass = false;
      } else {
        TestUtil.logMsg("Correct response expected: <" + request
            + ">, received: <" + response + ">");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallTokenTypeTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallTokenTypeTest failed");
  }

  /*
   * @testName: MarshallNameTypeTest
   *
   * @assertion_ids: JAXRPC:SPEC:64; JAXRPC:SPEC:502; JAXRPC:SPEC:295;
   * JAXRPC:SPEC:296; JAXRPC:SPEC:280; WS4EE:SPEC:177; WS4EE:SPEC:179;
   * WS4EE:SPEC:180; WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224;
   * WS4EE:SPEC:226; WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232;
   * WS4EE:SPEC:233; WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237;
   * WS4EE:SPEC:238; WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244;
   * WS4EE:SPEC:246; WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method. Verify
   * normal invocation and return.
   *
   * Description
   */
  public void MarshallNameTypeTest() throws Fault {
    TestUtil.logMsg("MarshallNameTypeTest");
    boolean pass = true;

    String request = "name";
    try {
      String response = port2.echoNameTypeTest(request);
      if (!response.equals(request)) {
        TestUtil.logErr("Wrong response expected: <" + request
            + ">, received: <" + response + ">");
        pass = false;
      } else {
        TestUtil.logMsg("Correct response expected: <" + request
            + ">, received: <" + response + ">");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallNameTypeTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallNameTypeTest failed");
  }

  /*
   * @testName: MarshallNCNameTypeTest
   *
   * @assertion_ids: JAXRPC:SPEC:64; JAXRPC:SPEC:502; JAXRPC:SPEC:295;
   * JAXRPC:SPEC:296; JAXRPC:SPEC:280; WS4EE:SPEC:177; WS4EE:SPEC:179;
   * WS4EE:SPEC:180; WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224;
   * WS4EE:SPEC:226; WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232;
   * WS4EE:SPEC:233; WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237;
   * WS4EE:SPEC:238; WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244;
   * WS4EE:SPEC:246; WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method. Verify
   * normal invocation and return.
   *
   * Description
   */
  public void MarshallNCNameTypeTest() throws Fault {
    TestUtil.logMsg("MarshallNCNameTypeTest");
    boolean pass = true;

    String request = "ncname";
    try {
      String response = port2.echoNCNameTypeTest(request);
      if (!response.equals(request)) {
        TestUtil.logErr("Wrong response expected: <" + request
            + ">, received: <" + response + ">");
        pass = false;
      } else {
        TestUtil.logMsg("Correct response expected: <" + request
            + ">, received: <" + response + ">");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallNCNameTypeTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallNCNameTypeTest failed");
  }

  /*
   * @testName: MarshallIDTypeTest
   *
   * @assertion_ids: JAXRPC:SPEC:64; JAXRPC:SPEC:502; JAXRPC:SPEC:295;
   * JAXRPC:SPEC:296; JAXRPC:SPEC:280; WS4EE:SPEC:177; WS4EE:SPEC:179;
   * WS4EE:SPEC:180; WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224;
   * WS4EE:SPEC:226; WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232;
   * WS4EE:SPEC:233; WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237;
   * WS4EE:SPEC:238; WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244;
   * WS4EE:SPEC:246; WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method. Verify
   * normal invocation and return.
   *
   * Description
   */
  public void MarshallIDTypeTest() throws Fault {
    TestUtil.logMsg("MarshallIDTypeTest");
    boolean pass = true;

    String request = "id";
    try {
      String response = port2.echoIDTypeTest(request);
      if (!response.equals(request)) {
        TestUtil.logErr("Wrong response expected: <" + request
            + ">, received: <" + response + ">");
        pass = false;
      } else {
        TestUtil.logMsg("Correct response expected: <" + request
            + ">, received: <" + response + ">");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallIDTypeTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallIDTypeTest failed");
  }

  /*
   * @testName: MarshallStructXMLSchemaTypesTest
   *
   * @assertion_ids: JAXRPC:SPEC:52; JAXRPC:SPEC:63; JAXRPC:SPEC:64;
   * JAXRPC:SPEC:502; JAXRPC:SPEC:295; JAXRPC:SPEC:296; JAXRPC:SPEC:280;
   * WS4EE:SPEC:177; WS4EE:SPEC:179; WS4EE:SPEC:180; WS4EE:SPEC:182;
   * WS4EE:SPEC:207; WS4EE:SPEC:224; WS4EE:SPEC:226; WS4EE:SPEC:228;
   * WS4EE:SPEC:231; WS4EE:SPEC:232; WS4EE:SPEC:233; WS4EE:SPEC:235;
   * WS4EE:SPEC:236; WS4EE:SPEC:237; WS4EE:SPEC:238; WS4EE:SPEC:240;
   * WS4EE:SPEC:241; WS4EE:SPEC:244; WS4EE:SPEC:246; WS4EE:SPEC:247;
   * WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method voidTest.
   * Verify normal invocation and return.
   *
   * Description
   */
  public void MarshallStructXMLSchemaTypesTest() throws Fault {
    TestUtil.logMsg("MarshallStructXMLSchemaTypesTest");
    boolean pass = true;

    InitExpectedFooTypeData();
    if (!sendFoo1Test())
      pass = false;
    if (!sendFoo2Test())
      pass = false;

    if (!pass)
      throw new Fault("MarshallStructXMLSchemaTypesTest failed");
  }

  // =======================================================================
  // Marshall Literal Faults (Foobad1 ... FooBad5)
  // =======================================================================
  /*
   * @testName: MarshallLiteralFaultsTest
   *
   * @assertion_ids: JAXRPC:SPEC:111; JAXRPC:SPEC:298; JAXRPC:SPEC:295;
   * JAXRPC:SPEC:296; JAXRPC:SPEC:280; WS4EE:SPEC:177; WS4EE:SPEC:179;
   * WS4EE:SPEC:180; WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224;
   * WS4EE:SPEC:226; WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232;
   * WS4EE:SPEC:233; WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237;
   * WS4EE:SPEC:238; WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244;
   * WS4EE:SPEC:246; WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method voidTest.
   * Verify literal faults.
   *
   * Description
   */
  public void MarshallLiteralFaultsTest() throws Fault {
    TestUtil.logMsg("MarshallLiteralFaults");
    boolean pass = true;

    if (!fooFaultTest())
      pass = false;

    if (!pass)
      throw new Fault("MarshallLiteralFaults failed");
  }

  // =======================================================================
  // Marshall XML Schema Complex Types (Person, Employee, Document)
  // =======================================================================
  /*
   * @testName: MarshallComplexTypesTest
   *
   * @assertion_ids: JAXRPC:SPEC:13; JAXRPC:SPEC:280; JAXRPC:SPEC:442;
   * JAXRPC:SPEC:295; JAXRPC:SPEC:296; WS4EE:SPEC:177; WS4EE:SPEC:179;
   * WS4EE:SPEC:180; WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224;
   * WS4EE:SPEC:226; WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232;
   * WS4EE:SPEC:233; WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237;
   * WS4EE:SPEC:238; WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244;
   * WS4EE:SPEC:246; WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods. Test
   * with complex types. For each type pass its value as input to the
   * corresponding RPC method and receive it back as the return value. Compare
   * results of each value/type of what was sent and what was returned. Verify
   * they are equal.
   *
   */
  public void MarshallComplexTypesTest() throws Fault {
    String testname = "MarshallComplexTypesTest";
    TestUtil.logTrace(testname);
    boolean pass = true;
    try {
      boolean b1 = doPersonTest();
      if (b1)
        TestUtil.logMsg("Person echo success!");
      boolean b2 = doEmployeeTest();
      if (b2)
        TestUtil.logMsg("Employee echo success!");
      boolean b3 = doDocumentTest();
      if (b3)
        TestUtil.logMsg("Document echo success!");
      if (!b1 || !b2 || !b3)
        pass = false;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault(testname);
    }

    if (!pass)
      throw new Fault(testname + " failed");
  }

  // ====================================================================
  // One Way Request
  // ====================================================================

  /*
   * @testName: MarshallOneWayTest
   *
   * @assertion_ids: JAXRPC:SPEC:33; JAXRPC:SPEC:295; JAXRPC:SPEC:296;
   * JAXRPC:SPEC:280; WS4EE:SPEC:177; WS4EE:SPEC:179; WS4EE:SPEC:180;
   * WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224; WS4EE:SPEC:226;
   * WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232; WS4EE:SPEC:233;
   * WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237; WS4EE:SPEC:238;
   * WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244; WS4EE:SPEC:246;
   * WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method voidTest.
   * Verify normal invocation and return.
   *
   * Description
   */
  public void MarshallOneWayTest() throws Fault {
    TestUtil.logMsg("MarshallOneWayTest");
    boolean pass = true;
    TestUtil.logMsg("Testing oneway operation to JAXRPC Service");
    try {
      OneWayMessage v = new OneWayMessage();
      v.setStringValue("A One Way Test");
      port4.oneWayMethod(v);
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallOneWayTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallOneWayTest failed");
  }

  // ====================================================================
  // Other Simple Types
  // ====================================================================

  /*
   * @testName: MarshallOtherSimpleTypesTest
   *
   * @assertion_ids: JAXRPC:SPEC:13; JAXRPC:SPEC:280; JAXRPC:SPEC:295;
   * JAXRPC:SPEC:296; JAXRPC:SPEC:52; JAXRPC:SPEC:442; JAXRPC:SPEC:284;
   * WS4EE:SPEC:177; WS4EE:SPEC:179; WS4EE:SPEC:180; WS4EE:SPEC:182;
   * WS4EE:SPEC:207; WS4EE:SPEC:224; WS4EE:SPEC:226; WS4EE:SPEC:228;
   * WS4EE:SPEC:231; WS4EE:SPEC:232; WS4EE:SPEC:233; WS4EE:SPEC:235;
   * WS4EE:SPEC:236; WS4EE:SPEC:237; WS4EE:SPEC:238; WS4EE:SPEC:240;
   * WS4EE:SPEC:241; WS4EE:SPEC:244; WS4EE:SPEC:246; WS4EE:SPEC:247;
   * WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * standard java class type. For each other simple type above (QName,
   * Base64Binary, and HexBinary) pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent sent and what was returned. Verify they are
   * equal.
   */
  public void MarshallOtherSimpleTypesTest() throws Fault {
    TestUtil.logMsg("MarshallOtherSimpleTypesTest");
    boolean pass = true;

    if (!QNameTest())
      pass = false;
    if (!Base64BinaryTest())
      pass = false;
    if (!HexBinaryTest())
      pass = false;

    if (!pass)
      throw new Fault("MarshallOtherSimpleTypesTest failed");
  }

  /*
   * @testName: MarshallUnsignedTypesTest
   *
   * @assertion_ids: JAXRPC:SPEC:52; JAXRPC:SPEC:295; JAXRPC:SPEC:296;
   * JAXRPC:SPEC:280; WS4EE:SPEC:177; WS4EE:SPEC:179; WS4EE:SPEC:180;
   * WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224; WS4EE:SPEC:226;
   * WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232; WS4EE:SPEC:233;
   * WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237; WS4EE:SPEC:238;
   * WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244; WS4EE:SPEC:246;
   * WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * standard java class type. For each supported unsigned simple type pass its
   * value as input to the corresponding RPC method and receive it back as the
   * return value. Compare results of each value/type of what was sent sent and
   * what was returned. Verify they are equal.
   */
  public void MarshallUnsignedTypesTest() throws Fault {
    TestUtil.logMsg("MarshallUnsignedTypesTest");
    boolean pass = true;

    if (!UnsignedShortTest())
      pass = false;
    if (!UnsignedIntTest())
      pass = false;
    if (!UnsignedByteTest())
      pass = false;
    if (!UnsignedLongTest())
      pass = false;

    if (!pass)
      throw new Fault("MarshallUnsignedTypesTest failed");
  }

  /*
   * @testName: MarshallBigIntegerTypesTest
   *
   * @assertion_ids: JAXRPC:SPEC:52; JAXRPC:SPEC:295; JAXRPC:SPEC:296;
   * JAXRPC:SPEC:280; WS4EE:SPEC:177; WS4EE:SPEC:179; WS4EE:SPEC:180;
   * WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224; WS4EE:SPEC:226;
   * WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232; WS4EE:SPEC:233;
   * WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237; WS4EE:SPEC:238;
   * WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244; WS4EE:SPEC:246;
   * WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * standard java class type. For each supported unsigned simple type pass its
   * value as input to the corresponding RPC method and receive it back as the
   * return value. Compare results of each value/type of what was sent sent and
   * what was returned. Verify they are equal.
   */
  public void MarshallBigIntegerTypesTest() throws Fault {
    TestUtil.logMsg("MarshallBigIntegerTypesTest");
    boolean pass = true;

    if (!NonPositiveIntegerTest())
      pass = false;
    if (!NonNegativeIntegerTest())
      pass = false;
    if (!PositiveIntegerTest())
      pass = false;
    if (!NegativeIntegerTest())
      pass = false;

    if (!pass)
      throw new Fault("MarshallBigIntegerTypesTest failed");
  }

  /*
   * @testName: MarshallDateTimeTest
   *
   * @assertion_ids: JAXRPC:SPEC:52; JAXRPC:SPEC:295; JAXRPC:SPEC:296;
   * JAXRPC:SPEC:280; WS4EE:SPEC:177; WS4EE:SPEC:179; WS4EE:SPEC:180;
   * WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224; WS4EE:SPEC:226;
   * WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232; WS4EE:SPEC:233;
   * WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237; WS4EE:SPEC:238;
   * WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244; WS4EE:SPEC:246;
   * WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method. Verify
   * normal invocation and return.
   *
   * Description
   */
  public void MarshallDateTimeTest() throws Fault {
    TestUtil.logMsg("MarshallDateTimeTest");
    boolean pass = true;

    if (!TimeTest())
      pass = false;
    if (!DateTest())
      pass = false;

    if (!pass)
      throw new Fault("MarshallDateTimeTest failed");
  }

  /*
   * @testName: MarshallListTypesTest
   *
   * @assertion_ids: JAXRPC:SPEC:71; JAXRPC:SPEC:295; JAXRPC:SPEC:296;
   * JAXRPC:SPEC:280; WS4EE:SPEC:177; WS4EE:SPEC:179; WS4EE:SPEC:180;
   * WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224; WS4EE:SPEC:226;
   * WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232; WS4EE:SPEC:233;
   * WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237; WS4EE:SPEC:238;
   * WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244; WS4EE:SPEC:246;
   * WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * standard java class type. For each supported unsigned simple type pass its
   * value as input to the corresponding RPC method and receive it back as the
   * return value. Compare results of each value/type of what was sent sent and
   * what was returned. Verify they are equal.
   */
  public void MarshallListTypesTest() throws Fault {
    TestUtil.logMsg("MarshallListTypesTest");
    boolean pass = true;

    if (!StringListTest())
      pass = false;
    if (!IntListTest())
      pass = false;
    if (!FloatListTest())
      pass = false;
    if (!DecimalListTest())
      pass = false;

    if (!DoubleListTest())
      pass = false;
    if (!IntegerListTest())
      pass = false;
    if (!LongListTest())
      pass = false;
    if (!ShortListTest())
      pass = false;
    if (!ByteListTest())
      pass = false;

    if (!pass)
      throw new Fault("MarshallListTypesTest failed");
  }

  /*
   * @testName: MarshallMapSimpleTypesTest
   *
   * @assertion_ids: JAXRPC:SPEC:70; WS4EE:SPEC:177; WS4EE:SPEC:179;
   * WS4EE:SPEC:180; WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224;
   * WS4EE:SPEC:226; WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232;
   * WS4EE:SPEC:233; WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237;
   * WS4EE:SPEC:238; WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244;
   * WS4EE:SPEC:246; WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: In NewSchemaDefs.xsd create a schema of simpletypes derived
   * via restriction for string, integer, int, long, short, decimal, float,
   * double, boolean, byte and qname. Verify that what was sent to the service
   * endpoint is echoed back.
   *
   * Description
   */
  public void MarshallMapSimpleTypesTest() throws Fault {
    TestUtil.logMsg("MarshallMapSimpleTypesTest");
    boolean pass = true;
    String request = "This is from MarshallMapSimpleTypesTest";
    BigDecimal values[] = JAXRPC_Data.BigDecimal_data;
    BigDecimalTestResponse bdResponse;
    BigDecimalTest bdRequest;
    TestUtil.logMsg("Top of  MarshallMapSimpleTypesTest");
    try {
      String response = port2.echoFooStringTypeTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Simple String passes");

      // -----------------------------------------------
      BigInteger birequest = new BigInteger("5");
      BigInteger biresponse = port2.echoFooIntegerTypeTest(birequest);
      if (!birequest.toString().equals(biresponse.toString())) {
        TestUtil.logErr("Result mismatch - expected: " + birequest.toString()
            + ", received: " + biresponse.toString());
        TestUtil.logErr("Integer failed");
        pass = false;

      } else
        TestUtil.logMsg("Simple Integer passes");

      // -----------------------------------------------
      int irequest = 10;
      int iresponse = port2.echoFooIntTypeTest(irequest);
      if (irequest != iresponse) {
        TestUtil.logErr("Result mismatch - expected: " + irequest
            + ", received: " + iresponse);
        TestUtil.logErr("Simple int failed");
        pass = false;
      } else
        TestUtil.logMsg("Simple int passes");

      // -----------------------------------------------

      long lrequest = 1000000000000000000L;
      long lresponse = port2.echoFooLongTypeTest(lrequest);
      if (lrequest != lresponse) {
        TestUtil.logErr("Result mismatch - expected: " + lrequest
            + ", received: " + lresponse);
        TestUtil.logErr("Simple long failed");
        pass = false;

      } else
        TestUtil.logMsg("Simple long passes");

      // -----------------------------------------------
      short srequest = 32765;
      short sresponse = port2.echoFooShortTypeTest(srequest);
      if (srequest != sresponse) {
        TestUtil.logErr("Result mismatch - expected: " + srequest
            + ", received: " + sresponse);
        TestUtil.logErr("Simple short failed");
        pass = false;

      } else
        TestUtil.logMsg("Simple short passes");

      // -----------------------------------------------
      TestUtil.logMsg("Testing BigDecimal");
      BigDecimal bdrequest = new BigDecimal("3512359.1456");
      BigDecimal bdresponse = port2.echoFooDecimalTypeTest(bdrequest);
      if (!bdrequest.toString().equals(bdresponse.toString())) {
        TestUtil.logErr("Result mismatch - expected: " + bdrequest.toString()
            + ", received: " + bdresponse.toString());
        TestUtil.logErr("decimal failed");
        pass = false;

      } else
        TestUtil.logMsg("Simple decimal passes");

      // -----------------------------------------------
      TestUtil.logMsg("Testing float");
      float frequest = 489.57f;
      float fresponse = port2.echoFooFloatTypeTest(frequest);
      if (frequest != fresponse) {
        TestUtil.logErr("Result mismatch - expected: " + frequest
            + ", received: " + fresponse);
        TestUtil.logErr("float failed");
        pass = false;

      } else
        TestUtil.logMsg("Simple float passes");

      // -----------------------------------------------
      TestUtil.logMsg("Testing double ");
      double drequest = 5;
      double dresponse = port2.echoFooDoubleTypeTest(drequest);
      if (drequest != dresponse) {
        TestUtil.logErr("Result mismatch - expected: " + drequest
            + ", received: " + dresponse);
        TestUtil.logErr("double failed");
        pass = false;

      } else
        TestUtil.logMsg("Simple double passes");

      // -----------------------------------------------
      TestUtil.logMsg("Testing boolean");
      boolean brequest = true;
      boolean bresponse = port2.echoFooBooleanTypeTest(brequest);
      if (brequest != bresponse) {
        TestUtil.logErr("Result mismatch - expected: " + brequest
            + ", received: " + bresponse);
        TestUtil.logErr("boolean failed");
        pass = false;

      } else
        TestUtil.logMsg("Simple boolean passes");
      // ------------------------------
      TestUtil.logMsg("Testing byte");
      byte btrequest = 127;
      byte btresponse = port2.echoFooByteTypeTest(btrequest);
      if (btrequest != btresponse) {
        TestUtil.logErr("Result mismatch - expected: " + btrequest
            + ", received: " + btresponse);
        TestUtil.logErr("byte failed");
        pass = false;

      } else
        TestUtil.logMsg("Simple byte passes");

      // -----------------------------------------------
      TestUtil.logMsg("Testing QName");
      QName qnrequest = new QName("myLocalPart");
      QName qnresponse = port2.echoFooQNameTypeTest(qnrequest);
      if (!qnresponse.toString().equals(qnrequest.toString())) {
        TestUtil.logErr("Result mismatch - expected: " + qnrequest.toString()
            + ", received: " + qnresponse.toString());
        TestUtil.logErr("QName failed");
        pass = false;

      } else
        TestUtil.logMsg("Simple QName passes");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    if (!pass)
      throw new Fault("MarshallMapSimpleTypesTest failed");

  }

  /*
   * @testName: MarshallIncludedStringTypeTest
   *
   * @assertion_ids: JAXRPC:SPEC:TODO; JAXRPC:SPEC:295; JAXRPC:SPEC:300;
   * WS4EE:SPEC:177; WS4EE:SPEC:179; JAXRPC:SPEC:280; WS4EE:SPEC:180;
   * WS4EE:SPEC:182; WS4EE:SPEC:207; WS4EE:SPEC:224; WS4EE:SPEC:226;
   * WS4EE:SPEC:228; WS4EE:SPEC:231; WS4EE:SPEC:232; WS4EE:SPEC:233;
   * WS4EE:SPEC:235; WS4EE:SPEC:236; WS4EE:SPEC:237; WS4EE:SPEC:238;
   * WS4EE:SPEC:240; WS4EE:SPEC:241; WS4EE:SPEC:244; WS4EE:SPEC:246;
   * WS4EE:SPEC:247; WS4EE:SPEC:248;
   *
   * @test_Strategy: include another schema in NewSchemaDefs.xsd. Verify that
   * the schema is available.
   *
   * Description
   */

  public void MarshallIncludedStringTypeTest() throws Fault {
    TestUtil.logMsg("MarshallIncludedStringTypeTest");
    boolean pass = true;
    IncludedStringRequest request = null;
    String myString = "Please echo this back to me ";
    IncludedStringResponse response = null;
    try {
      request = new IncludedStringRequest();
      request.setMyString(myString);
      response = port2.echoIncludedStringTest(request);
      String resp = response.getMyString();
      if (!myString.equals(resp)) {
        pass = false;
        TestUtil.logMsg("Expected: " + myString + " but returned " + resp);
      } else
        TestUtil.logMsg("Good String echoed back as expected");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallNOTATIONTypeTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallIncludedStringTypeTest failed");
  }

  private boolean printTestStatus(boolean pass, String test) {
    if (pass)
      TestUtil.logMsg("" + test + " ... PASSED");
    else
      TestUtil.logErr("" + test + " ... FAILED");

    return pass;
  }

  private boolean sendFoo1Test() {
    TestUtil.logMsg("MarshallStructXMLSchemaTypesTest:(sendFoo1Test)");
    boolean pass = true;
    TestUtil.logMsg(
        "Passing/Returning StructXMLSchemaDataTypes to/from JAXRPC Service");
    try {
      FooStatusType response = port2.sendFoo1Test(FooType_data);
      if (!response.isFooA())
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallStructXMLSchemaTypesTest:(sendFoo1Test)");
    return pass;
  }

  private boolean sendFoo2Test() {
    TestUtil.logMsg("MarshallStructXMLSchemaTypesTest:(sendFoo2Test)");
    boolean pass = true;
    TestUtil.logMsg(
        "Passing/Returning StructXMLSchemaDataTypes to/from JAXRPC Service");
    try {
      FooType response = port2.sendFoo2Test(FooType_data);
      if (!CompareWithExpectedFooTypeData(response))
        pass = false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallStructXMLSchemaTypesTest:(sendFoo2Test)");
    return pass;
  }

  private boolean fooFaultTest() {
    TestUtil.logMsg("MarshallLiteralFaultsTest:(fooFaultTest)");
    boolean pass = true;
    TestUtil.logMsg("Testing Literal Faults");
    try {
      TestUtil.logMsg("Throw a FooFaultException with reason - FooBad1");
      FooStringRequest f = new FooStringRequest();
      f.setVarString("FooBad1");
      FooStringResponse response = port2.fooFaultTest(f);
      TestUtil.logErr("Did not throw a FooFaultException");
      pass = false;
    } catch (FooFaultException e) {
      TestUtil.logMsg("Did thow a FooFaultException");
      TestUtil.logMsg("Reason=" + e.getWhyTheFault().getValue());
      if (e.getWhyTheFault() == WhyTheFault.FooBad1)
        TestUtil.logMsg("Reason for exception - expected");
      else {
        pass = false;
        TestUtil.logErr("Reason for exception - unexpected", e);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    try {
      TestUtil.logMsg("Throw a FooFaultException with reason - FooBad5");
      FooStringRequest f = new FooStringRequest();
      f.setVarString("FooBad5");
      FooStringResponse response = port2.fooFaultTest(f);
      TestUtil.logErr("Did not throw a FooFaultException");
      pass = false;
    } catch (FooFaultException e) {
      TestUtil.logMsg("Did thow a FooFaultException");
      TestUtil.logMsg("Reason=" + e.getWhyTheFault().getValue());
      if (e.getWhyTheFault() == WhyTheFault.FooBad5)
        TestUtil.logMsg("Reason for exception - expected");
      else {
        pass = false;
        TestUtil.logErr("Reason for exception - unexpected", e);
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallLiteralFaultsTest:(fooFaultTest)");
    return pass;
  }

  private boolean booleanTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(booleanTest)");
    boolean pass = true;
    boolean values[] = JAXRPC_Data.boolean_data;
    BooleanTestResponse response;
    BooleanTest request;
    TestUtil.logMsg("Passing/Returning boolean data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        request = new BooleanTest();
        request.setBooleanValue(values[i]);
        response = port.booleanTest(request);
        if (!JAXRPC_Data.compareValues(values[i], response.isBooleanValue()))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallPrimitiveTest:(booleanTest)");
    return pass;
  }

  private boolean byteTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(byteTest)");
    boolean pass = true;
    byte values[] = JAXRPC_Data.byte_data;
    ByteTestResponse response;
    ByteTest request;
    TestUtil.logMsg("Passing/Returning byte data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        request = new ByteTest();
        request.setByteValue(values[i]);
        response = port.byteTest(request);
        if (!JAXRPC_Data.compareValues(values[i], response.getByteValue()))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallPrimitiveTest:(byteTest)");
    return pass;
  }

  private boolean shortTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(shortTest)");
    boolean pass = true;
    short values[] = JAXRPC_Data.short_data;
    ShortTestResponse response;
    ShortTest request;
    TestUtil.logMsg("Passing/Returning short data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        request = new ShortTest();
        request.setShortValue(values[i]);
        response = port.shortTest(request);
        if (!JAXRPC_Data.compareValues(values[i], response.getShortValue()))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallPrimitiveTest:(shortTest)");
    return pass;
  }

  private boolean intTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(intTest)");
    boolean pass = true;
    int values[] = JAXRPC_Data.int_data;
    IntTestResponse response;
    IntTest request;
    TestUtil.logMsg("Passing/Returning int data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        request = new IntTest();
        request.setIntValue(values[i]);
        response = port.intTest(request);
        if (!JAXRPC_Data.compareValues(values[i], response.getIntValue()))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallPrimitiveTest:(intTest)");
    return pass;
  }

  private boolean longTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(longTest)");
    boolean pass = true;
    long values[] = JAXRPC_Data.long_data;
    LongTestResponse response;
    LongTest request;
    TestUtil.logMsg("Passing/Returning long data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        request = new LongTest();
        request.setLongValue(values[i]);
        response = port.longTest(request);
        if (!JAXRPC_Data.compareValues(values[i], response.getLongValue()))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallPrimitiveTest:(longTest)");
    return pass;
  }

  private boolean floatTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(floatTest)");
    boolean pass = true;
    float values[] = JAXRPC_Data.float_data;
    FloatTestResponse response;
    FloatTest request;
    TestUtil.logMsg("Passing/Returning float data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        request = new FloatTest();
        request.setFloatValue(values[i]);
        response = port.floatTest(request);
        if (!JAXRPC_Data.compareValues(values[i], response.getFloatValue()))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallPrimitiveTest:(floatTest)");
    return pass;
  }

  private boolean doubleTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(doubleTest)");
    boolean pass = true;
    double values[] = JAXRPC_Data.double_data;
    DoubleTestResponse response;
    DoubleTest request;
    TestUtil.logMsg("Passing/Returning double data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        request = new DoubleTest();
        request.setDoubleValue(values[i]);
        response = port.doubleTest(request);
        if (!JAXRPC_Data.compareValues(values[i], response.getDoubleValue()))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallPrimitiveTest:(doubleTest)");
    return pass;
  }

  private boolean StringTest() {
    TestUtil.logMsg("MarshallStandardJavaClassesTest:(StringTest)");
    boolean pass = true;
    String values[] = JAXRPC_Data.String_data;
    StringTestResponse response;
    StringTest request;
    try {
      TestUtil.logMsg("Passing/Returning String class to/from JAXRPC Service");
      for (int i = 0; i < values.length; i++) {
        request = new StringTest();
        request.setStringValue(values[i]);
        response = port.stringTest(request);
        if (!JAXRPC_Data.compareValues(values[i], response.getStringValue(),
            "String"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallStandardJavaClassesTest:(StringTest)");
    return pass;
  }

  private boolean CalendarTest() {
    TestUtil.logMsg("MarshallStandardJavaClassesTest:(CalendarTest)");
    boolean pass = true;
    Calendar values[] = JAXRPC_Data.GregorianCalendar_data;
    CalendarTestResponse response;
    CalendarTest request;
    TestUtil.logMsg("Passing/Returning Calendar class to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        request = new CalendarTest();
        request.setCalendar(values[i]);
        response = port.calendarTest(request);
        if (!JAXRPC_Data.compareValues(values[i], response.getCalendar(),
            "Calendar"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallStandardJavaClassesTest:(CalendarTest)");
    return pass;
  }

  private boolean BigIntegerTest() {
    TestUtil.logMsg("MarshallStandardJavaClassesTest:(BigIntegerTest)");
    boolean pass = true;
    BigInteger values[] = JAXRPC_Data.BigInteger_data;
    BigIntegerTestResponse response;
    BigIntegerTest request;
    TestUtil
        .logMsg("Passing/Returning BigInteger class to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        request = new BigIntegerTest();
        request.setBigInteger(values[i]);
        response = port.bigIntegerTest(request);
        if (!JAXRPC_Data.compareValues(values[i], response.getBigInteger(),
            "BigInteger"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallStandardJavaClassesTest:(BigIntegerTest)");
    return pass;
  }

  private boolean BigDecimalTest() {
    TestUtil.logMsg("MarshallStandardJavaClassesTest:(BigDecimalTest)");
    boolean pass = true;
    BigDecimal values[] = JAXRPC_Data.BigDecimal_data;
    BigDecimalTestResponse response;
    BigDecimalTest request;
    TestUtil
        .logMsg("Passing/Returning BigDecimal class to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        request = new BigDecimalTest();
        request.setBigDecimal(values[i]);
        response = port.bigDecimalTest(request);
        if (!JAXRPC_Data.compareValues(values[i], response.getBigDecimal(),
            "BigDecimal"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallStandardJavaClassesTest:(BigDecimalTest)");
    return pass;
  }

  private boolean booleanArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(booleanArrayTest)");
    boolean pass = true;
    boolean values[] = JAXRPC_Data.boolean_data;
    BooleanArrayTestResponse response;
    BooleanArrayTest request;
    TestUtil.logMsg("Passing/Returning boolean array to/from JAXRPC Service");
    try {
      request = new BooleanArrayTest();
      request.setBooleanArray(values);
      response = port.booleanArrayTest(request);
      pass = JAXRPC_Data.compareArrayValues(values, response.getBooleanArray(),
          "boolean");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(booleanArrayTest)");
    return pass;
  }

  private boolean byteArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(byteArrayTest)");
    boolean pass = true;
    byte values[] = JAXRPC_Data.byte_data;
    ByteArrayTestResponse response;
    ByteArrayTest request;
    try {
      request = new ByteArrayTest();
      request.setByteArray(values);
      response = port.byteArrayTest(request);
      pass = JAXRPC_Data.compareArrayValues(values, response.getByteArray(),
          "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(byteArrayTest)");
    return pass;
  }

  private boolean shortArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(shortArrayTest)");
    boolean pass = true;
    short values[] = JAXRPC_Data.short_data;
    ShortArrayTestResponse response;
    ShortArrayTest request;
    try {
      request = new ShortArrayTest();
      request.setShortArray(values);
      response = port.shortArrayTest(request);
      pass = JAXRPC_Data.compareArrayValues(values, response.getShortArray(),
          "short");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(shortArrayTest)");
    return pass;
  }

  private boolean intArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(intArrayTest)");
    boolean pass = true;
    int values[] = JAXRPC_Data.int_data;
    IntArrayTestResponse response;
    IntArrayTest request;
    try {
      request = new IntArrayTest();
      request.setIntArray(values);
      response = port.intArrayTest(request);
      pass = JAXRPC_Data.compareArrayValues(values, response.getIntArray(),
          "int");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(intArrayTest)");
    return pass;
  }

  private boolean longArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(longArrayTest)");
    boolean pass = true;
    long values[] = JAXRPC_Data.long_data;
    LongArrayTestResponse response;
    LongArrayTest request;
    try {
      request = new LongArrayTest();
      request.setLongArray(values);
      response = port.longArrayTest(request);
      pass = JAXRPC_Data.compareArrayValues(values, response.getLongArray(),
          "long");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(longArrayTest)");
    return pass;
  }

  private boolean floatArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(floatArrayTest)");
    boolean pass = true;
    float values[] = JAXRPC_Data.float_data;
    FloatArrayTestResponse response;
    FloatArrayTest request;
    try {
      request = new FloatArrayTest();
      request.setFloatArray(values);
      response = port.floatArrayTest(request);
      pass = JAXRPC_Data.compareArrayValues(values, response.getFloatArray(),
          "float");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(floatArrayTest)");
    return pass;
  }

  private boolean doubleArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(doubleArrayTest)");
    boolean pass = true;
    double values[] = JAXRPC_Data.double_data;
    DoubleArrayTestResponse response;
    DoubleArrayTest request;
    try {
      request = new DoubleArrayTest();
      request.setDoubleArray(values);
      response = port.doubleArrayTest(request);
      pass = JAXRPC_Data.compareArrayValues(values, response.getDoubleArray(),
          "double");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(doubleArrayTest)");
    return pass;
  }

  private boolean StringArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(StringArrayTest)");
    boolean pass = true;
    String values[] = JAXRPC_Data.String_data;
    StringArrayTestResponse response;
    StringArrayTest request;
    try {
      request = new StringArrayTest();
      request.setStringArray(values);
      response = port.stringArrayTest(request);
      pass = JAXRPC_Data.compareArrayValues(values, response.getStringArray(),
          "String");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(StringArrayTest)");
    return pass;
  }

  private boolean CalendarArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(CalendarArrayTest)");
    boolean pass = true;
    Calendar values[] = JAXRPC_Data.GregorianCalendar_data;
    CalendarArrayTestResponse response;
    CalendarArrayTest request;
    try {
      request = new CalendarArrayTest();
      request.setCalendarArray(values);
      response = port.calendarArrayTest(request);
      pass = JAXRPC_Data.compareArrayValues(values, response.getCalendarArray(),
          "Calendar");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
      ;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(CalendarArrayTest)");
    return pass;
  }

  private boolean BigIntegerArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(BigIntegerArrayTest)");
    boolean pass = true;
    BigInteger values[] = JAXRPC_Data.BigInteger_data;
    BigIntegerArrayTestResponse response;
    BigIntegerArrayTest request;
    try {
      request = new BigIntegerArrayTest();
      request.setBigIntegerArray(values);
      response = port.bigIntegerArrayTest(request);
      pass = JAXRPC_Data.compareArrayValues(values,
          response.getBigIntegerArray(), "BigInteger");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(BigIntegerArrayTest)");
    return pass;
  }

  private boolean BigDecimalArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(BigDecimalArrayTest)");
    boolean pass = true;
    BigDecimal values[] = JAXRPC_Data.BigDecimal_data;
    BigDecimalArrayTestResponse response;
    BigDecimalArrayTest request;
    try {
      request = new BigDecimalArrayTest();
      request.setBigDecimalArray(values);
      response = port.bigDecimalArrayTest(request);
      pass = JAXRPC_Data.compareArrayValues(values,
          response.getBigDecimalArray(), "BigDecimal");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(BigDecimalArrayTest)");
    return pass;
  }

  private boolean JavaBeanArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(JavaBeanArrayTest)");
    boolean pass = true;

    init_JavaBean_Data();
    JavaBean values[] = JavaBean_data;
    JavaBeanArrayTestResponse response;
    JavaBeanArrayTest request;
    try {
      request = new JavaBeanArrayTest();
      request.setJavaBeanArray(values);
      response = port.javaBeanArrayTest(request);
      pass = compareArrayValues(values, response.getJavaBeanArray(),
          "JavaBean");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(JavaBeanArrayTest)");
    return pass;
  }

  private void init_JavaBean_Data() {
    JavaBean_data[0].setMyBoolean(false);
    JavaBean_data[0].setMyByte(Byte.MIN_VALUE);
    JavaBean_data[0].setMyShort(Short.MIN_VALUE);
    JavaBean_data[0].setMyInt(Integer.MIN_VALUE);
    JavaBean_data[0].setMyLong(Long.MIN_VALUE);
    JavaBean_data[0].setMyFloat(Float.MIN_VALUE);
    JavaBean_data[0].setMyDouble(Double.MIN_VALUE);
    JavaBean_data[0].setMyString("");
    JavaBean_data[0].setMyBigInteger(JAXRPC_Data.BigInteger_data[0]);
    JavaBean_data[0].setMyBigDecimal(JAXRPC_Data.BigDecimal_data[0]);
    JavaBean_data[0].setMyCalendar(JAXRPC_Data.GregorianCalendar_data[0]);

    JavaBean_data[1].setMyBoolean(true);
    JavaBean_data[1].setMyByte(Byte.MAX_VALUE);
    JavaBean_data[1].setMyShort(Short.MAX_VALUE);
    JavaBean_data[1].setMyInt(Integer.MAX_VALUE);
    JavaBean_data[1].setMyLong(Long.MAX_VALUE);
    JavaBean_data[1].setMyFloat(Float.MAX_VALUE);
    JavaBean_data[1].setMyDouble(Double.MAX_VALUE);
    JavaBean_data[1].setMyString("");
    JavaBean_data[1].setMyBigInteger(JAXRPC_Data.BigInteger_data[1]);
    JavaBean_data[1].setMyBigDecimal(JAXRPC_Data.BigDecimal_data[1]);
    JavaBean_data[1].setMyCalendar(JAXRPC_Data.GregorianCalendar_data[1]);

    JavaBean2_data[0].setMyBoolean(false);
    JavaBean2_data[0].setMyByte(Byte.MIN_VALUE);
    JavaBean2_data[0].setMyShort(Short.MIN_VALUE);
    JavaBean2_data[0].setMyInt(Integer.MIN_VALUE);
    JavaBean2_data[0].setMyLong(Long.MIN_VALUE);
    JavaBean2_data[0].setMyFloat(Float.MIN_VALUE);
    JavaBean2_data[0].setMyDouble(Double.MIN_VALUE);
    JavaBean2_data[0].setMyString("");
    JavaBean2_data[0].setMyBigInteger(JAXRPC_Data.BigInteger_data[0]);
    JavaBean2_data[0].setMyBigDecimal(JAXRPC_Data.BigDecimal_data[0]);
    JavaBean2_data[0].setMyCalendar(JAXRPC_Data.GregorianCalendar_data[0]);

    JavaBean2_data[1].setMyBoolean(true);
    JavaBean2_data[1].setMyByte(Byte.MAX_VALUE);
    JavaBean2_data[1].setMyShort(Short.MAX_VALUE);
    JavaBean2_data[1].setMyInt(Integer.MAX_VALUE);
    JavaBean2_data[1].setMyLong(Long.MAX_VALUE);
    JavaBean2_data[1].setMyFloat(Float.MAX_VALUE);
    JavaBean2_data[1].setMyDouble(Double.MAX_VALUE);
    JavaBean2_data[1].setMyString("");
    JavaBean2_data[1].setMyBigInteger(JAXRPC_Data.BigInteger_data[1]);
    JavaBean2_data[1].setMyBigDecimal(JAXRPC_Data.BigDecimal_data[1]);
    JavaBean2_data[1].setMyCalendar(JAXRPC_Data.GregorianCalendar_data[1]);

    JavaBean_data[0].setMyJavaBean(JavaBean2_data[0]);
    JavaBean_data[1].setMyJavaBean(JavaBean2_data[1]);

  }

  public String toStringJavaBean(JavaBean v) {
    return "myBoolean: " + v.isMyBoolean() + ", myByte: " + v.getMyByte()
        + ", myShort: " + v.getMyShort() + ", myInt: " + v.getMyInt()
        + ", myLong: " + v.getMyLong() + ", myFloat: " + v.getMyFloat()
        + ", myDouble: " + v.getMyDouble() + ", myString: " + v.getMyString()
        + ", myBigInteger: " + v.getMyBigInteger() + ", myBigDecimal: "
        + v.getMyBigDecimal() + ", myJavaBean: "
        + toStringJavaBean2(v.getMyJavaBean()) + ", myCalendar: "
        + v.getMyCalendar();
  }

  public String toStringJavaBean2(JavaBean2 v) {
    return "myBoolean: " + v.isMyBoolean() + ", myByte: " + v.getMyByte()
        + ", myShort: " + v.getMyShort() + ", myInt: " + v.getMyInt()
        + ", myLong: " + v.getMyLong() + ", myFloat: " + v.getMyFloat()
        + ", myDouble: " + v.getMyDouble() + ", myString: " + v.getMyString()
        + ", myBigInteger: " + v.getMyBigInteger() + ", myBigDecimal: "
        + v.getMyBigDecimal() + ", myCalendar: " + v.getMyCalendar();
  }

  public boolean compareJavaBeans(JavaBean e, JavaBean r) {
    return e.isMyBoolean() == r.isMyBoolean() && e.getMyByte() == r.getMyByte()
        && e.getMyShort() == r.getMyShort() && e.getMyInt() == r.getMyInt()
        && e.getMyLong() == r.getMyLong() && e.getMyFloat() == r.getMyFloat()
        && e.getMyDouble() == r.getMyDouble()
        && e.getMyString().equals(r.getMyString())
        && e.getMyBigInteger().equals(r.getMyBigInteger())
        && e.getMyBigDecimal().equals(r.getMyBigDecimal())
        && compareJavaBean2(e.getMyJavaBean(), r.getMyJavaBean())
        && JAXRPC_Data.compareCalendars(e.getMyCalendar(), r.getMyCalendar());
  }

  public boolean compareJavaBean2(JavaBean2 e, JavaBean2 r) {
    return e.isMyBoolean() == r.isMyBoolean() && e.getMyByte() == r.getMyByte()
        && e.getMyShort() == r.getMyShort() && e.getMyInt() == r.getMyInt()
        && e.getMyLong() == r.getMyLong() && e.getMyFloat() == r.getMyFloat()
        && e.getMyDouble() == r.getMyDouble()
        && e.getMyString().equals(r.getMyString())
        && e.getMyBigInteger().equals(r.getMyBigInteger())
        && e.getMyBigDecimal().equals(r.getMyBigDecimal())
        && JAXRPC_Data.compareCalendars(e.getMyCalendar(), r.getMyCalendar());
  }

  // ==================================================================
  // Various utility classes used for dumping/comparing data
  // ==================================================================

  private void dumpArrayValues(Object o, String t) {
    System.out.println("JAXRPC_Data:dumpArrayValues");
    System.out.println("Dumping " + t + " array, size=" + getArraySize(o, t));
    if (t.equals("JavaBean")) {
      JavaBean[] v = (JavaBean[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + toStringJavaBean(v[i]));
    }
  }

  private int getArraySize(Object o, String t) {
    System.out.println("JAXRPC_Data:getArraySize");
    if (t.equals("JavaBean")) {
      return ((JavaBean[]) o).length;
    }
    return -1;
  }

  private boolean compareValues(Object e, Object r, String t) {
    boolean pass = true;

    if (t.equals("JavaBean")) {
      JavaBean exp = (JavaBean) e;
      JavaBean rec = (JavaBean) r;
      if (rec == exp)
        return true;
      if ((rec == null && exp != null) || (rec != null && exp == null)) {
        pass = false;
      } else if (!compareJavaBeans(exp, rec)) {
        System.out.println("Value Mismatch: expected " + toStringJavaBean(exp)
            + ", received " + toStringJavaBean(rec));
        pass = false;
      }
    }
    return pass;
  }

  private boolean compareArrayValues(Object e, Object r, String t) {
    System.out.println("JAXRPC_Data:compareArrayValues");
    boolean pass = true;

    if (t.equals("JavaBean")) {
      JavaBean[] exp = (JavaBean[]) e;
      JavaBean[] rec = (JavaBean[]) r;
      if (rec.length != exp.length) {
        System.out.println("Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i] == exp[i])
          continue;
        if ((rec[i] == null && exp[i] != null)
            || (rec[i] != null && exp[i] == null)) {
          pass = false;
        } else if (!compareJavaBeans(exp[i], rec[i])) {
          System.out
              .println("Array Mismatch: expected " + toStringJavaBean(exp[i])
                  + ", received " + toStringJavaBean(rec[i]));
          pass = false;
        }
      }
    }
    return pass;
  }

  private String returnArrayValues(Object o, String t) {
    String values = null;
    if (t.equals("JavaBean")) {
      JavaBean[] v = (JavaBean[]) o;
      for (int i = 0; i < v.length; i++)
        values += ", " + toStringJavaBean(v[i]);
    }
    return values;
  }

  private boolean doPersonTest() {
    TestUtil.logMsg("doPersonTest");
    boolean pass = true;
    // Person arguments
    String name = "ChildPerson";
    String sex = "F";
    int age = 10;
    float id = (float) Short.MAX_VALUE;
    boolean adult = false;
    Person person = null;
    try {
      person = new Person();
      person.setName(name);
      person.setAge(age);
      person.setSex(sex);
      person.setAdult(adult);
      person.setId(id);
      EchoPersonRequest request = new EchoPersonRequest();
      request.setPerson(person);
      EchoPersonResponse response = port3.echoPerson(request);
      person = response.getPerson();
      String resultName = person.getName();
      String resultSex = person.getSex();
      int resultAge = person.getAge();
      boolean resultAdult = person.isAdult();
      float resultId = person.getId();
      if (!resultName.equals(name) || !resultSex.equals(sex)
          || (resultAge != age) || (resultId != id) || resultAdult) {
        TestUtil.logErr("Result mismatch in Person");
        pass = false;
        TestUtil.logErr("Expected: <" + name + "," + sex + "," + age + "," + id
            + "," + adult + ">, Got: <" + resultName + "," + resultSex + ","
            + resultAge + "," + resultId + "," + resultAdult + ">");
      } else {
        TestUtil.logMsg(
            "Results match!\nExpected: <" + name + "," + sex + "," + age + ","
                + id + "," + adult + ">, Got: <" + resultName + "," + resultSex
                + "," + resultAge + "," + resultId + "," + resultAdult + ">");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean doEmployeeTest() {
    TestUtil.logMsg("doEmployeeTest");
    boolean pass = true;
    // Person arguments
    String name = "AdultPerson";
    String sex = "M";
    int age = 30;
    boolean adult = true;
    float id = (float) Short.MAX_VALUE;
    // Employee other arguments
    double salary = 200000;
    int empId = 1;
    try {
      Person person = new Person();
      person.setName(name);
      person.setSex(sex);
      person.setAge(age);
      person.setAdult(adult);
      person.setId(id);
      Employee employee = new Employee();
      employee.setPerson(person);
      employee.setSalary(salary);
      employee.setId(empId);
      EchoEmployeeRequest request = new EchoEmployeeRequest();
      request.setEmployee(employee);
      EchoEmployeeResponse response = port3.echoEmployee(request);
      employee = response.getEmployee();
      // verify results
      person = employee.getPerson();
      String resultName = person.getName();
      String resultSex = person.getSex();
      int resultAge = person.getAge();
      float resultId = person.getId();
      boolean resultAdult = person.isAdult();
      if (!resultName.equals(name) || !resultSex.equals(sex)
          || (resultAdult != adult) || (resultAge != age) || (resultId != id)
          || !resultAdult) {
        TestUtil.logErr("Result mismatch in Person");
        pass = false;
        TestUtil.logErr("Expected: <" + name + "," + sex + "," + age + "," + id
            + "," + adult + ">, Got: <" + resultName + "," + resultSex + ","
            + resultAge + "," + resultId + "," + resultAdult + ">");

      }
      float resultSalary = (float) employee.getSalary();
      int resultEmpId = employee.getId();
      if (!pass || (resultSalary != salary) || (resultEmpId != empId)) {
        TestUtil.logErr("Result mismatch in Employee");
        pass = false;
        TestUtil.logErr("Expected: <" + salary + ", " + empId + ">, Got: <"
            + resultSalary + "," + resultEmpId + ">");
      } else {
        TestUtil.logMsg("Results match\nExpected: <" + name + "," + sex + ","
            + age + "," + id + "," + adult + ">, Got: <" + resultName + ","
            + resultSex + "," + resultAge + "," + resultId + "," + resultAdult
            + ">\n" + "Expected: <" + salary + ", " + empId + ">, Got: <"
            + resultSalary + "," + resultEmpId + ">");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean doDocumentTest() {
    String stringID = "myID";
    String stringValue = "myValue";
    TestUtil.logMsg("doDocumentTest");
    boolean pass = true;
    try {
      Document request = new Document();
      request.setID(stringID);
      request.set_value(stringValue);
      Document response = port3.echoDocument(request);
      if (!response.getID().equals(stringID)) {
        TestUtil.logErr("Result mismatch in Document ID");
        TestUtil.logErr(
            "Expected: <" + stringID + ">, Got: <" + response.getID() + ">");
      }
      if (!response.get_value().equals(stringValue)) {
        TestUtil.logErr("Result mismatch in Document Value");
        TestUtil.logErr("Expected: <" + stringValue + ">, Got: <"
            + response.get_value() + ">");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean QNameTest() {
    TestUtil.logMsg("QNameTest");
    boolean pass = true;
    QName arg = new QName("someLocalPart");
    QNameTestResponse response;
    QNameTest request;
    TestUtil.logMsg("Passing Returning data to/from JAXRPC Service");
    request = new QNameTest();
    request.setQname_1(arg);
    try {
      response = port.qnameTest(request);
      if (!response.getResult().equals(arg)) {
        TestUtil.logErr("QNameTest failed - expected " + arg + ",  received: "
            + response.getResult());
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "QNameTest");
    return pass;
  }

  private boolean Base64BinaryTest() {
    TestUtil.logMsg("Base64BinaryTest");
    boolean pass = false;
    byte values[] = JAXRPC_Data.byte_data;
    Base64BinaryTest request;
    Base64BinaryTestResponse response;
    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      request = new Base64BinaryTest();
      request.setBase64Binary_1(values);
      response = port.base64BinaryTest(request);
      byte ret[] = response.getResult();

      TestUtil.logTrace(
          "base64 returned : " + ret[0] + "," + ret[1] + "," + ret[2]);
      pass = JAXRPC_Data.compareArrayValues(values, response.getResult(),
          "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "Base64BinaryTest");
    return pass;
  }

  private boolean HexBinaryTest() {
    TestUtil.logMsg("HexBinaryTest");
    boolean pass = false;
    byte values[] = JAXRPC_Data.byte_data;
    HexBinaryTest request;
    HexBinaryTestResponse response;
    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      request = new HexBinaryTest();
      request.setHexBinary_1(values);

      response = port.hexBinaryTest(request);
      byte ret[] = response.getResult();
      TestUtil.logTrace(
          "HexBinary returned : " + ret[0] + "," + ret[1] + "," + ret[2]);
      pass = JAXRPC_Data.compareArrayValues(values, response.getResult(),
          "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "HexBinaryTest");
    return pass;
  }

  private FooType FooType_data = null;

  private FooType FooType_array_data[] = null;

  private FooVariousSchemaTypes FooVariousSchemaTypes_data = null;

  private FooVariousSchemaTypes FooVariousSchemaTypes_array_data[] = null;

  private FooVariousSchemaTypesListType FooVariousSchemaTypesListType_data = null;

  private FooAnonymous FooAnonymous_array_data[] = null;

  private FooAnonymousType FooAnonymousType_data = null;

  private int getIteratorCount(Iterator i) {
    int count = 0;
    while (i.hasNext() && count < 100) {
      ++count;
    }
    return count;
  }

  private void InitExpectedFooTypeData() {
    init_FooVariousSchemaTypes_Data();
    init_FooAnonymousType_Data();
    init_FooType_Data();
  }

  private boolean CompareWithExpectedFooTypeData(FooType f) {
    boolean valid = true;

    if (f.isFooA() != true) {
      TestUtil.logErr(
          "isFooA() returned " + f.isFooA() + ", expected " + !f.isFooA());
      valid = false;
    }
    if (f.getFooB() != Byte.MAX_VALUE) {
      TestUtil.logErr(
          "getFooB() returned " + f.getFooB() + ", expected " + Byte.MAX_VALUE);
      valid = false;
    }
    if (f.getFooC() != Short.MAX_VALUE) {
      TestUtil.logErr("getFooC() returned " + f.getFooC() + ", expected "
          + Short.MAX_VALUE);
      valid = false;
    }
    if (f.getFooD() != Integer.MAX_VALUE) {
      TestUtil.logErr("getFooD() returned " + f.getFooD() + ", expected "
          + Integer.MAX_VALUE);
      valid = false;
    }
    if (f.getFooE() != Long.MAX_VALUE) {
      TestUtil.logErr(
          "getFooE() returned " + f.getFooE() + ", expected " + Long.MAX_VALUE);
      valid = false;
    }
    if (f.getFooF() != Float.MAX_VALUE) {
      TestUtil.logErr("getFooF() returned " + f.getFooF() + ", expected "
          + Float.MAX_VALUE);
      valid = false;
    }
    if (f.getFooG() != Double.MAX_VALUE) {
      TestUtil.logErr("getFooG() returned " + f.getFooG() + ", expected "
          + Double.MAX_VALUE);
      valid = false;
    }
    if (!f.getFooH().equals("foostringH")) {
      TestUtil.logErr(
          "getFooH() returned " + f.getFooH() + ", expected foostringH");
      valid = false;
    }
    if (!f.getFooI().equals("123-ABC12")) {
      TestUtil
          .logErr("getFooI() returned " + f.getFooI() + ", expected 123-ABC12");
      valid = false;
    }
    FooVariousSchemaTypes fnst = f.getFooJ();
    if (fnst == null) {
      TestUtil.logErr(
          "getFooJ() returned null, " + "expected FooVariousSchemaTypes");
      valid = false;
    }
    if (fnst != null) {
      TestUtil.logMsg("Send: " + FooVariousSchemaTypes_data.getFooA() + "|"
          + FooVariousSchemaTypes_data.getFooB() + "|"
          + FooVariousSchemaTypes_data.getFooC() + "|"
          + FooVariousSchemaTypes_data.getFooD() + "|"
          + FooVariousSchemaTypes_data.getFooE() + "|"
          + FooVariousSchemaTypes_data.getFooF());
      TestUtil.logMsg("Recv: " + fnst.getFooA() + "|" + fnst.getFooB() + "|"
          + fnst.getFooC() + "|" + fnst.getFooD() + "|" + fnst.getFooE() + "|"
          + fnst.getFooF());
      if (fnst.getFooA() == FooVariousSchemaTypes_data.getFooA()
          && fnst.getFooB().equals(FooVariousSchemaTypes_data.getFooB())
          && fnst.getFooC().equals(FooVariousSchemaTypes_data.getFooC())
          && fnst.getFooD().equals(FooVariousSchemaTypes_data.getFooD())
          && fnst.getFooE() == FooVariousSchemaTypes_data.getFooE()
          && fnst.getFooF() == FooVariousSchemaTypes_data.getFooF()) {
        TestUtil.logMsg("Result match");
      } else {
        TestUtil.logErr("Result mismatch");
        valid = false;
      }
    }
    if (!f.getFooK().equals(new BigInteger("101"))) {
      TestUtil.logErr("getFooK() returned " + f.getFooK() + ", expected 101");
      valid = false;
    }
    if (!(f.getFooM().equals("hello,there"))) {
      TestUtil.logErr(
          "getFooM() returned " + f.getFooM() + ", expected hello,there");
      valid = false;
    }
    if (!compareFooAnonymousTypeData(f.getFooN(), FooAnonymousType_data))
      valid = false;
    return valid;
  }

  private void init_FooVariousSchemaTypes_Data() {
    FooVariousSchemaTypes_data = new FooVariousSchemaTypes();
    FooVariousSchemaTypes_data.setFooA(1);
    FooVariousSchemaTypes_data.setFooB(new BigInteger("1000"));
    FooVariousSchemaTypes_data.setFooC("NORMALIZEDSTRING");
    FooVariousSchemaTypes_data.setFooD("NMTOKEN");
    FooVariousSchemaTypes_data.setFooE(1);
    FooVariousSchemaTypes_data.setFooF((short) 1);

    FooVariousSchemaTypes_array_data = new FooVariousSchemaTypes[2];
    FooVariousSchemaTypesListType_data = new FooVariousSchemaTypesListType();

    FooVariousSchemaTypes_array_data[0] = new FooVariousSchemaTypes();
    FooVariousSchemaTypes_array_data[1] = new FooVariousSchemaTypes();
    FooVariousSchemaTypes_array_data[0].setFooA(256);
    FooVariousSchemaTypes_array_data[1].setFooA(0);
    FooVariousSchemaTypes_array_data[0].setFooB(JAXRPC_Data.BigInteger_data[0]);
    FooVariousSchemaTypes_array_data[1].setFooB(JAXRPC_Data.BigInteger_data[1]);
    FooVariousSchemaTypes_array_data[0].setFooC("NORMALIZEDSTRING1");
    FooVariousSchemaTypes_array_data[1].setFooC("NORMALIZEDSTRING2");
    FooVariousSchemaTypes_array_data[0].setFooD("NMTOKEN1");
    FooVariousSchemaTypes_array_data[1].setFooD("NMTOKEN2");
    FooVariousSchemaTypes_array_data[0].setFooE(0);
    FooVariousSchemaTypes_array_data[1].setFooE(1);
    FooVariousSchemaTypes_array_data[0].setFooF((short) 0);
    FooVariousSchemaTypes_array_data[1].setFooF((short) 1);
    FooVariousSchemaTypesListType_data
        .setFooA(FooVariousSchemaTypes_array_data);
  }

  private void init_FooAnonymousType_Data() {
    FooAnonymous_array_data = new FooAnonymous[2];
    FooAnonymousType_data = new FooAnonymousType();

    FooAnonymous_array_data[0] = new FooAnonymous();
    FooAnonymous_array_data[1] = new FooAnonymous();
    FooAnonymous_array_data[0].setFooA("foo");
    FooAnonymous_array_data[1].setFooA("bar");
    FooAnonymous_array_data[0].setFooB(1);
    FooAnonymous_array_data[1].setFooB(0);
    FooAnonymous_array_data[0].setFooC(true);
    FooAnonymous_array_data[1].setFooC(false);
    FooAnonymousType_data.setFooAnonymous(FooAnonymous_array_data);
  }

  private boolean compareFooAnonymousTypeData(FooAnonymousType request,
      FooAnonymousType response) {
    boolean valid = true;

    FooAnonymous[] req = request.getFooAnonymous();
    FooAnonymous[] res = response.getFooAnonymous();
    if (req.length == res.length) {
      TestUtil.logMsg("Array length match - checking array elements");
      for (int i = 0; i < req.length; i++) {
        FooAnonymous exp = req[i];
        FooAnonymous rec = res[i];
        TestUtil.logMsg("Request: " + exp.getFooA() + "|" + exp.getFooB() + "|"
            + exp.isFooC());
        TestUtil.logMsg("Response: " + rec.getFooA() + "|" + rec.getFooB() + "|"
            + rec.isFooC());
        if (!exp.getFooA().equals(rec.getFooA())
            || exp.getFooB() != rec.getFooB() || exp.isFooC() != rec.isFooC()) {
          valid = false;
          TestUtil.logErr("Element results mismatch ...");
          break;
        } else
          TestUtil.logMsg("Element results match ...");
      }
    } else {
      TestUtil.logErr("Array length mismatch - expected: " + req.length
          + ", received: " + res.length);
    }
    return valid;
  }

  private void init_FooType_Data() {
    FooType_data = new FooType();

    FooType_data.setFooA(true);
    FooType_data.setFooB(Byte.MAX_VALUE);
    FooType_data.setFooC(Short.MAX_VALUE);
    FooType_data.setFooD(Integer.MAX_VALUE);
    FooType_data.setFooE(Long.MAX_VALUE);
    FooType_data.setFooF(Float.MAX_VALUE);
    FooType_data.setFooG(Double.MAX_VALUE);
    FooType_data.setFooH("foostringH");
    FooType_data.setFooI("123-ABC12");
    FooType_data.setFooJ(FooVariousSchemaTypes_data);
    FooType_data.setFooK(new BigInteger("101"));
    FooType_data.setFooM("hello,there");
    FooType_data.setFooN(FooAnonymousType_data);
  }

  private boolean QNameArrayTest() {
    TestUtil.logMsg("QNameArrayTest");
    boolean pass = false;
    QName values[] = JAXRPC_Data.QName_data;
    QNameArrayTest request;
    QNameArrayTestResponse response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      request = new QNameArrayTest();
      request.setQnameArray_1(values);
      response = port.qnameArrayTest(request);
      pass = JAXRPC_Data.compareArrayValues(values, response.getResult(),
          "QName");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "QNameArrayTest");
    return pass;
  }

  private boolean StringEnumTypeTest() {
    boolean pass = true;
    FooStringEnumType request = FooStringEnumType.fromValue("MA");
    try {
      TestUtil.logMsg("Sending request: " + request);
      FooStringEnumType response = port2.echoStringEnumTypeTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    try {
      TestUtil.logMsg("Testing for IllegalArgumentException ... for value CA");
      request = FooStringEnumType.fromString("CA");
      TestUtil.logErr("Expected exception for illegal value");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught IllegalArgumentException as expected");
    }
    return pass;
  }

  private boolean ByteEnumTypeTest() {
    boolean pass = true;
    FooByteEnumType request = FooByteEnumType.fromString("1");
    try {
      TestUtil.logMsg("Sending request: " + request);
      FooByteEnumType response = port2.echoByteEnumTypeTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    try {
      TestUtil.logMsg("Testing for IllegalArgumentException ... for value 4");
      request = FooByteEnumType.fromString("4");
      TestUtil.logErr("Expected exception for illegal value");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught IllegalArgumentException as expected");
    }
    return pass;
  }

  private boolean ShortEnumTypeTest() {
    boolean pass = true;
    FooShortEnumType request = FooShortEnumType.fromString("1");
    try {
      TestUtil.logMsg("Sending request: " + request);
      FooShortEnumType response = port2.echoShortEnumTypeTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    try {
      TestUtil.logMsg("Testing for IllegalArgumentException ... for value 4");
      request = FooShortEnumType.fromString("4");
      TestUtil.logErr("Expected exception for illegal value");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught IllegalArgumentException as expected");
    }
    return pass;
  }

  private boolean IntegerEnumTypeTest() {
    boolean pass = true;
    FooIntegerEnumType request = FooIntegerEnumType.fromString("1");
    try {
      TestUtil.logMsg("Sending request: " + request);
      FooIntegerEnumType response = port2.echoIntegerEnumTypeTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    try {
      TestUtil.logMsg("Testing for IllegalArgumentException ... for value 4");
      request = FooIntegerEnumType.fromString("4");
      TestUtil.logErr("Expected exception for illegal value");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught IllegalArgumentException as expected");
    }
    return pass;
  }

  private boolean IntEnumTypeTest() {
    boolean pass = true;
    FooIntEnumType request = FooIntEnumType.fromString("1");
    try {
      TestUtil.logMsg("Sending request: " + request);
      FooIntEnumType response = port2.echoIntEnumTypeTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    try {
      request = FooIntEnumType.fromString("4");
      TestUtil.logErr("Expected exception for illegal value");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught IllegalArgumentException as expected");
    }
    return pass;
  }

  private boolean LongEnumTypeTest() {
    boolean pass = true;
    FooLongEnumType request = FooLongEnumType.fromString("1");
    try {
      TestUtil.logMsg("Sending request: " + request);
      FooLongEnumType response = port2.echoLongEnumTypeTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    try {
      TestUtil.logMsg("Testing for IllegalArgumentException ... for value 4");
      request = FooLongEnumType.fromString("4");
      TestUtil.logErr("Expected exception for illegal value");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught IllegalArgumentException as expected");
    }
    return pass;
  }

  private boolean DecimalEnumTypeTest() {
    boolean pass = true;
    FooDecimalEnumType request = FooDecimalEnumType.fromString("1.1");
    try {
      TestUtil.logMsg("Sending request: " + request);
      FooDecimalEnumType response = port2.echoDecimalEnumTypeTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    try {
      TestUtil.logMsg("Testing for IllegalArgumentException ... for value 4.4");
      request = FooDecimalEnumType.fromString("4.4");
      TestUtil.logErr("Expected exception for illegal value");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught IllegalArgumentException as expected");
    }
    return pass;
  }

  private boolean FloatEnumTypeTest() {
    boolean pass = true;
    FooFloatEnumType request = FooFloatEnumType.fromString("1.1");
    try {
      TestUtil.logMsg("Sending request: " + request);
      FooFloatEnumType response = port2.echoFloatEnumTypeTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    try {
      TestUtil.logMsg("Testing for IllegalArgumentException ... for value 4.4");
      request = FooFloatEnumType.fromString("4.4");
      TestUtil.logErr("Expected exception for illegal value");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught IllegalArgumentException as expected");
    }
    return pass;
  }

  private boolean DoubleEnumTypeTest() {
    boolean pass = true;
    FooDoubleEnumType request = FooDoubleEnumType.fromString("1.1");
    try {
      TestUtil.logMsg("Sending request: " + request);
      FooDoubleEnumType response = port2.echoDoubleEnumTypeTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    try {
      TestUtil.logMsg("Testing for IllegalArgumentException ... for value 4.4");
      request = FooDoubleEnumType.fromString("4.4");
      TestUtil.logErr("Expected exception for illegal value");
      pass = false;
    } catch (IllegalArgumentException e) {
      TestUtil.logMsg("Caught IllegalArgumentException as expected");
    }
    return pass;
  }

  private boolean UnsignedShortTest() {
    TestUtil.logTrace("UnsignedShortTest ...");
    boolean pass = true;
    int request = 100;
    try {
      TestUtil.logMsg("Sending request: " + request);
      int response = port2.echoUnsignedShortTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (request != response) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean UnsignedIntTest() {
    TestUtil.logTrace("UnsignedIntTest ...");
    boolean pass = true;
    long request = 100;
    try {
      TestUtil.logMsg("Sending request: " + request);
      long response = port2.echoUnsignedIntTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (request != response) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean UnsignedByteTest() {
    TestUtil.logTrace("UnsignedByteTest ...");
    boolean pass = true;
    short request = 100;
    try {
      TestUtil.logMsg("Sending request: " + request);
      int response = port2.echoUnsignedByteTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (request != response) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean UnsignedLongTest() {
    TestUtil.logTrace("UnsignedLongTest ...");
    boolean pass = true;
    BigInteger request = new BigInteger("100");
    try {
      TestUtil.logMsg("Sending request: " + request);
      BigInteger response = port2.echoUnsignedLongTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean NonPositiveIntegerTest() {
    TestUtil.logTrace("NonPositiveIntegerTest ...");
    boolean pass = true;
    BigInteger request = new BigInteger("-100");
    try {
      TestUtil.logMsg("Sending request: " + request);
      BigInteger response = port2.echoNonPositiveIntegerTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean NonNegativeIntegerTest() {
    TestUtil.logTrace("NonNegativeIntegerTest ...");
    boolean pass = true;
    BigInteger request = new BigInteger("100");
    try {
      TestUtil.logMsg("Sending request: " + request);
      BigInteger response = port2.echoNonNegativeIntegerTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean NegativeIntegerTest() {
    TestUtil.logTrace("NegativeIntegerTest ...");
    boolean pass = true;
    BigInteger request = new BigInteger("-100");
    try {
      TestUtil.logMsg("Sending request: " + request);
      BigInteger response = port2.echoNegativeIntegerTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean PositiveIntegerTest() {
    TestUtil.logTrace("PositiveIntegerTest ...");
    boolean pass = true;
    BigInteger request = new BigInteger("100");
    try {
      TestUtil.logMsg("Sending request: " + request);
      BigInteger response = port2.echoPositiveIntegerTest(request);
      TestUtil.logMsg("Received response: " + response);
      if (!request.equals(response)) {
        TestUtil.logErr("Result mismatch - expected: " + request
            + ", received: " + response);
        pass = false;

      } else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean TimeTest() {
    TestUtil.logMsg("(TimeTest)");
    boolean pass = true;
    Calendar values[] = JAXRPC_Data.GregorianCalendar_data;
    Calendar request;
    Calendar response;
    TestUtil.logMsg("Passing/Returning Time class to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        request = values[i];
        response = port2.echoTimeTest(request);
        if (!JAXRPC_Data.compareValues(values[i], response, "calendar"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "(TimeTest)");
    return pass;
  }

  private boolean DateTest() {
    TestUtil.logMsg("(DateTest)");
    boolean pass = true;
    Calendar values[] = JAXRPC_Data.GregorianCalendar_data;
    Calendar request = null;
    Calendar response = null;
    TestUtil.logMsg("Passing/Returning Time class to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        request = values[i];
        response = port2.echoDateTest(request);
        if (!JAXRPC_Data.compareValues(values[i], response, "calendar"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "(DateTest)");
    return pass;
  }

  private boolean StringListTest() {
    TestUtil.logTrace("StringListTest ...");
    boolean pass = true;
    String[] request = new String[] { "foo", "bar", "foobar" };
    try {
      String[] response = port2.echoStringListTypeTest(request);
      if (!JAXRPC_Data.compareArrayValues(request, response, "String"))
        pass = false;
      else
        TestUtil.logMsg("Result match");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean DecimalListTest() {
    TestUtil.logTrace("DecimalListTest...");
    boolean pass = true;
    BigDecimal[] request = new BigDecimal[] { new BigDecimal("3512359.1456"),
        new BigDecimal("1"), new BigDecimal("2") };

    try {
      BigDecimal[] response = port2.echoDecimalListTypeTest(request);
      if (!JAXRPC_Data.compareArrayValues(request, response, "BigDecimal"))
        pass = false;
      else
        TestUtil.logMsg("Result match");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean FloatListTest() {
    TestUtil.logTrace("FloatListTest...");
    boolean pass = true;
    float[] request = new float[] { Float.MIN_VALUE, Float.MAX_VALUE, 1f };
    try {
      float[] response = port2.echoFloatListTypeTest(request);
      if (!JAXRPC_Data.compareArrayValues(request, response, "float"))
        pass = false;
      else
        TestUtil.logMsg("Result match");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean IntListTest() {
    TestUtil.logTrace("IntListTest...");
    boolean pass = true;
    int[] request = new int[] { Integer.MIN_VALUE, Integer.MAX_VALUE, 1 };
    try {
      int[] response = port2.echoIntListTypeTest(request);
      if (!JAXRPC_Data.compareArrayValues(request, response, "int"))
        pass = false;
      else
        TestUtil.logMsg("Result match");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean IntegerListTest() {
    TestUtil.logTrace("IntegerListTest...");
    boolean pass = true;
    BigInteger[] request = new BigInteger[] { new BigInteger("0"),
        new BigInteger("1"), new BigInteger("2") };
    try {
      BigInteger[] response = port2.echoIntegerListTypeTest(request);
      if (!JAXRPC_Data.compareArrayValues(request, response, "BigInteger"))
        pass = false;
      else
        TestUtil.logMsg("Result match");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean DoubleListTest() {
    TestUtil.logTrace("DoubleListTest...");
    boolean pass = true;
    double[] request = new double[] { Double.MIN_VALUE, Double.MAX_VALUE, 1d };
    try {
      double[] response = port2.echoDoubleListTypeTest(request);
      if (!JAXRPC_Data.compareArrayValues(request, response, "double"))
        pass = false;
      else
        TestUtil.logMsg("Result match");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean ByteListTest() {
    TestUtil.logTrace("ByteListTest...");
    boolean pass = true;
    byte[] request = new byte[] { Byte.MIN_VALUE, Byte.MAX_VALUE, 1 };
    try {
      byte[] response = port2.echoByteListTypeTest(request);
      if (!JAXRPC_Data.compareArrayValues(request, response, "byte"))
        pass = false;
      else
        TestUtil.logMsg("Result match");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean LongListTest() {
    TestUtil.logTrace("LongListTest...");
    boolean pass = true;
    long[] request = new long[] { Long.MIN_VALUE, Long.MAX_VALUE, 1 };
    try {
      long[] response = port2.echoLongListTypeTest(request);
      if (!JAXRPC_Data.compareArrayValues(request, response, "long"))
        pass = false;
      else
        TestUtil.logMsg("Result match");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

  private boolean ShortListTest() {
    TestUtil.logTrace("ShortListTest...");
    boolean pass = true;
    short[] request = new short[] { Short.MIN_VALUE, Short.MAX_VALUE, 1 };
    try {
      short[] response = port2.echoShortListTypeTest(request);
      if (!JAXRPC_Data.compareArrayValues(request, response, "short"))
        pass = false;
      else
        TestUtil.logMsg("Result match");

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    return pass;
  }

}
