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

package com.sun.ts.tests.jaxrpc.ee.j2w.marshalltest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;

import javax.xml.rpc.*;

import java.util.*;

import java.math.BigInteger;
import java.math.BigDecimal;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.ee.j2w.marshalltest.";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "j2wmarshalltest.endpoint.1";

  private static final String WSDLLOC_URL = "j2wmarshalltest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  // ServiceName and PortName mapping configuration going java-to-wsdl
  private static final String SERVICE_NAME = "j2wmarshalltest.servicename.1";

  private static final String PORT_NAME = "j2wmarshalltest.portname.1";

  private String serviceName = null;

  private String portName = null;

  String modeProperty = null; // platform.mode -> (standalone|javaEE)

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
  MarshallTest port = null;

  Stub stub = null;

  private void getStubStandalone() throws Exception {
    serviceName = PKG_NAME + JAXRPC_Util.getURLFromProp(SERVICE_NAME);
    portName = JAXRPC_Util.getURLFromProp(PORT_NAME);
    port = (MarshallTest) JAXRPC_Util.getStub(serviceName, "get" + portName);
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub = (javax.xml.rpc.Stub) port;
  }

  private void getStub() throws Exception {
    try {
      InitialContext ic = new InitialContext();
      javax.xml.rpc.Service svc = (javax.xml.rpc.Service) ic
          .lookup("java:comp/env/service/j2wmarshalltest");

      port = (MarshallTest) svc.getPort(MarshallTest.class);
    } catch (Throwable t) {
      TestUtil.printStackTrace(t);
      throw new Fault(t.toString());
    }
  }

  private JavaBean JavaBean_data[] = null;

  private JavaBean JavaBean_multi_data[][] = null;

  private ValueType ValueType_data[] = null;

  private ValueType ValueType_multi_data[][] = null;

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
    JavaBean_data = new JavaBean[2];
    JavaBean_data[0] = new JavaBean();
    JavaBean_data[1] = new JavaBean();
    JavaBean_multi_data = new JavaBean[2][];
    JavaBean_multi_data[0] = JavaBean_data;
    JavaBean_multi_data[1] = JavaBean_data;
    ValueType_data = new ValueType[2];
    ValueType_data[0] = new ValueType();
    ValueType_data[1] = new ValueType();
    ValueType_multi_data = new ValueType[2][];
    ValueType_multi_data[0] = ValueType_data;
    ValueType_multi_data[1] = ValueType_data;
    logMsg("setup ok");
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
   * @assertion_ids: JAXRPC:SPEC:202; JAXRPC:SPEC:203; JAXRPC:SPEC:208;
   * JAXRPC:SPEC:212; JAXRPC:SPEC:214; JAXRPC:SPEC:220; JAXRPC:SPEC:221;
   * JAXRPC:SPEC:11; JAXRPC:SPEC:258; JAXRPC:SPEC:259; JAXRPC:SPEC:260;
   * JAXRPC:SPEC:258; JAXRPC:SPEC:259; JAXRPC:SPEC:260; JAXRPC:SPEC:261;
   * JAXRPC:SPEC:262; JAXRPC:SPEC:263; JAXRPC:SPEC:264; JAXRPC:SPEC:265;
   * JAXRPC:SPEC:266; JAXRPC:SPEC:267; JAXRPC:SPEC:268; JAXRPC:SPEC:279;
   * JAXRPC:SPEC:283; JAXRPC:SPEC:218; JAXRPC:SPEC:222; JAXRPC:SPEC:281;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:70; WS4EE:SPEC:137;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each primitive type pass its value as input to the
   * corresponding RPC method and receive it back as the return value. Compare
   * results of each value/type of what was sent and what was returned. Verify
   * they are equal.
   *
   * Description JAX-RPC 1.0 specification supports the following Java
   * primititive types and the corresponding Java classes:
   * boolean,byte,short,int,long,float,double as well as:
   * Boolean,Byte,Short,Integer,Long,Float,Double
   */
  public void MarshallPrimitiveTest() throws Fault {
    TestUtil.logMsg("MarshallPrimitiveTest");
    boolean pass = true;

    if (!booleanTest())
      pass = false;
    if (!BooleanTest())
      pass = false;
    if (!byteTest())
      pass = false;
    if (!ByteTest())
      pass = false;
    if (!shortTest())
      pass = false;
    if (!ShortTest())
      pass = false;
    if (!intTest())
      pass = false;
    if (!IntegerTest())
      pass = false;
    if (!longTest())
      pass = false;
    if (!LongTest())
      pass = false;
    if (!floatTest())
      pass = false;
    if (!FloatTest())
      pass = false;
    if (!doubleTest())
      pass = false;
    if (!DoubleTest())
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
   * @assertion_ids: JAXRPC:SPEC:202; JAXRPC:SPEC:206; JAXRPC:SPEC:208;
   * JAXRPC:SPEC:212; JAXRPC:SPEC:214; JAXRPC:SPEC:220; JAXRPC:SPEC:221;
   * JAXRPC:SPEC:11; JAXRPC:SPEC:258; JAXRPC:SPEC:259; JAXRPC:SPEC:260;
   * JAXRPC:SPEC:261; JAXRPC:SPEC:262; JAXRPC:SPEC:263; JAXRPC:SPEC:264;
   * JAXRPC:SPEC:265; JAXRPC:SPEC:266; JAXRPC:SPEC:267; JAXRPC:SPEC:268;
   * JAXRPC:SPEC:279; JAXRPC:SPEC:283; JAXRPC:SPEC:223; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:70; WS4EE:SPEC:137;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * standard java class type. For each standard java class above pass its value
   * as input to the corresponding RPC method and receive it back as the return
   * value. Compare results of each value/type of what was sent sent and what
   * was returned. Verify they are equal.
   *
   * Description JAX-RPC 1.0 specification supports the following Standard Java
   * Classes: o java.lang.String o java.util.Calendar o java.math.BigInteger o
   * java.math.BigDecimal
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
   * @assertion_ids: JAXRPC:SPEC:202; JAXRPC:SPEC:228; JAXRPC:SPEC:240;
   * JAXRPC:SPEC:208; JAXRPC:SPEC:212; JAXRPC:SPEC:214; JAXRPC:SPEC:220;
   * JAXRPC:SPEC:221; JAXRPC:SPEC:256; JAXRPC:SPEC:11; JAXRPC:SPEC:258;
   * JAXRPC:SPEC:259; JAXRPC:SPEC:260; JAXRPC:SPEC:261; JAXRPC:SPEC:262;
   * JAXRPC:SPEC:263; JAXRPC:SPEC:264; JAXRPC:SPEC:265; JAXRPC:SPEC:266;
   * JAXRPC:SPEC:267; JAXRPC:SPEC:268; JAXRPC:SPEC:279; JAXRPC:SPEC:283;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:70; WS4EE:SPEC:137;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method
   * JavaBeanTest. Pass a JavaBean value to the RPC method and receive it back
   * as the return value. Compare results of JavaBean value from what was sent
   * and what was returned. Verify they are equal.
   *
   * Description A client can invoke an RPC method via generated stub passing a
   * JavaBean value as input argument and receiving a JavaBean value as a return
   * value. This also falls under the category of JAXRPC Value Types.
   */
  public void MarshallJavaBeanTest() throws Fault {
    TestUtil.logMsg("MarshallJavaBeanTest");
    boolean pass = true;

    init_JavaBean_Data();
    JavaBean values[] = JavaBean_data;
    JavaBean response;
    TestUtil.logMsg(
        "Passing/Returning JavaBean JavaBean class to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.javaBeanTest(values[i]);
        if (!compareJavaBeans(values[i], response))
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
   * @assertion_ids: JAXRPC:SPEC:202; JAXRPC:SPEC:204; JAXRPC:SPEC:208;
   * JAXRPC:SPEC:212; JAXRPC:SPEC:214; JAXRPC:SPEC:220; JAXRPC:SPEC:221;
   * JAXRPC:SPEC:11; JAXRPC:SPEC:258; JAXRPC:SPEC:259; JAXRPC:SPEC:260;
   * JAXRPC:SPEC:261; JAXRPC:SPEC:262; JAXRPC:SPEC:263; JAXRPC:SPEC:264;
   * JAXRPC:SPEC:265; JAXRPC:SPEC:266; JAXRPC:SPEC:267; JAXRPC:SPEC:268;
   * JAXRPC:SPEC:279; JAXRPC:SPEC:283; JAXRPC:SPEC:219; JAXRPC:SPEC:224;
   * JAXRPC:SPEC:225; JAXRPC:SPEC:227; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:70; WS4EE:SPEC:137;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, invoke the RPC methods for each
   * java type supported. For each java type supported pass an arrary of values
   * as input to the corresponding RPC method and receive it back as the return
   * value. Compare results of each array type of what was sent and what was
   * returned. Verify they are equal.
   *
   * Description JAX-RPC 1.0 specification supports a Java array with members of
   * a supported JAX-RPC Java type. Single dimensional Java arrays are tested.
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

    if (!pass)
      throw new Fault("MarshallJavaArrayTest failed");
  }

  // ====================================================================
  // Java Array Multi-Dimensional for all supported JAX-RPC types
  // ====================================================================

  /*
   * @testName: MarshallJavaMultiArrayTest
   *
   * @assertion_ids: JAXRPC:SPEC:202; JAXRPC:SPEC:205; JAXRPC:SPEC:208;
   * JAXRPC:SPEC:212; JAXRPC:SPEC:214; JAXRPC:SPEC:220; JAXRPC:SPEC:221;
   * JAXRPC:SPEC:11; JAXRPC:SPEC:258; JAXRPC:SPEC:259; JAXRPC:SPEC:260;
   * JAXRPC:SPEC:261; JAXRPC:SPEC:262; JAXRPC:SPEC:263; JAXRPC:SPEC:264;
   * JAXRPC:SPEC:265; JAXRPC:SPEC:266; JAXRPC:SPEC:267; JAXRPC:SPEC:268;
   * JAXRPC:SPEC:226; JAXRPC:SPEC:279; JAXRPC:SPEC:283; JAXRPC:SPEC:58;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:70; WS4EE:SPEC:137;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, invoke the RPC methods for each
   * java type supported. For each java type supported pass an arrary of values
   * as input to the corresponding RPC method and receive it back as the return
   * value. Compare results of each array type of what was sent and what was
   * returned. Verify they are equal.
   *
   * Description JAX-RPC 1.0 specification supports a Java array with members of
   * a supported JAX-RPC Java type. Multidimensional Java arrays are tested.
   */
  public void MarshallJavaMultiArrayTest() throws Fault {
    TestUtil.logMsg("MarshallJavaMultiArrayTest");
    boolean pass = true;

    if (!booleanMultiArrayTest())
      pass = false;
    if (!byteMultiArrayTest())
      pass = false;
    if (!shortMultiArrayTest())
      pass = false;
    if (!intMultiArrayTest())
      pass = false;
    if (!longMultiArrayTest())
      pass = false;
    if (!floatMultiArrayTest())
      pass = false;
    if (!doubleMultiArrayTest())
      pass = false;
    if (!StringMultiArrayTest())
      pass = false;
    if (!CalendarMultiArrayTest())
      pass = false;
    if (!BigIntegerMultiArrayTest())
      pass = false;
    if (!BigDecimalMultiArrayTest())
      pass = false;
    if (!JavaBeanMultiArrayTest())
      pass = false;

    if (!pass)
      throw new Fault("MarshallJavaMultiArrayTest failed");
  }

  // ====================================================================
  // Service Specific Exception
  // ====================================================================

  /*
   * @testName: MarshallServiceExceptionTest
   *
   * @assertion_ids: JAXRPC:SPEC:215; JAXRPC:SPEC:220; JAXRPC:SPEC:258;
   * JAXRPC:SPEC:259; JAXRPC:SPEC:260; JAXRPC:SPEC:261; JAXRPC:SPEC:262;
   * JAXRPC:SPEC:263; JAXRPC:SPEC:264; JAXRPC:SPEC:265; JAXRPC:SPEC:266;
   * JAXRPC:SPEC:267; JAXRPC:SPEC:268; JAXRPC:SPEC:279; JAXRPC:SPEC:283;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:70; WS4EE:SPEC:137;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method
   * ServiceExceptionTest that generates a fault condition. Verify a fault
   * condition occurs.
   *
   * Description A client can invoke an RPC method via generated stub and
   * receive a fault.
   */
  public void MarshallServiceExceptionTest() throws Fault {
    TestUtil.logMsg("MarshallServiceExceptionTest");
    boolean pass = true;
    TestUtil.logMsg("Throwing a ServiceException from JAXRPC Service");
    try {
      try {
        port.myServiceExceptionTest();
        TestUtil.logErr("no fault occurred");
        pass = false;
      } catch (Exception response) {
        TestUtil.logMsg("a fault occurred: " + response);
        TestUtil.logMsg("response=" + response);
        if (response instanceof MyServiceException) {
          TestUtil.logMsg("Is an instance of MyServiceException");
        } else {
          TestUtil.logErr("Not an instance of MyServiceException", response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallServiceExceptionTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallServiceExceptionTest failed");
  }

  // ====================================================================
  // The void type
  // ====================================================================

  /*
   * @testName: MarshallVoidTest
   *
   * @assertion_ids: JAXRPC:JAVADOC:212; JAXRPC:SPEC:258; JAXRPC:SPEC:259;
   * JAXRPC:SPEC:260; JAXRPC:SPEC:261; JAXRPC:SPEC:262; JAXRPC:SPEC:263;
   * JAXRPC:SPEC:264; JAXRPC:SPEC:265; JAXRPC:SPEC:266; JAXRPC:SPEC:267;
   * JAXRPC:SPEC:268; JAXRPC:SPEC:279; JAXRPC:SPEC:283; WS4EE:SPEC:35;
   * WS4EE:SPEC:36; WS4EE:SPEC:70; WS4EE:SPEC:137;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method voidTest.
   * Verify normal invocation and return.
   *
   * Description A client can invoke an RPC method via generated stub and handle
   * void type.
   */
  public void MarshallVoidTest() throws Fault {
    TestUtil.logMsg("MarshallVoidTest");
    boolean pass = true;
    TestUtil.logMsg("Handling a void type to/from JAXRPC Service");
    try {
      port.voidTest();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new Fault("MarshallVoidTest failed", e);
    }

    if (!pass)
      throw new Fault("MarshallVoidTest failed");
  }

  private boolean printTestStatus(boolean pass, String test) {
    if (pass)
      TestUtil.logMsg("" + test + " ... PASSED");
    else
      TestUtil.logErr("" + test + " ... FAILED");

    return pass;
  }

  private boolean booleanTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(booleanTest)");
    boolean pass = true;
    boolean values[] = JAXRPC_Data.boolean_data;
    boolean response;
    TestUtil.logMsg("Passing/Returning boolean data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.booleanTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response))
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

  private boolean BooleanTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(BooleanTest)");
    boolean pass = true;
    Boolean values[] = JAXRPC_Data.Boolean_data;
    Boolean response;
    try {
      TestUtil.logMsg("Passing/Returning Boolean class to/from JAXRPC Service");
      for (int i = 0; i < values.length; i++) {
        response = port.wrapperBooleanTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response, "Boolean"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallPrimitiveTest:(BooleanTest)");
    return pass;
  }

  private boolean byteTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(byteTest)");
    boolean pass = true;
    byte values[] = JAXRPC_Data.byte_data;
    byte response;
    TestUtil.logMsg("Passing/Returning byte data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.byteTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response))
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

  private boolean ByteTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(ByteTest)");
    boolean pass = true;
    Byte values[] = JAXRPC_Data.Byte_data;
    Byte response;
    TestUtil.logMsg("Passing/Returning Byte class to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.wrapperByteTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response, "Byte"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallPrimitiveTest:(ByteTest)");
    return pass;
  }

  private boolean shortTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(shortTest)");
    boolean pass = true;
    short values[] = JAXRPC_Data.short_data;
    short response;
    TestUtil.logMsg("Passing/Returning short data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.shortTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response))
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

  private boolean ShortTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(ShortTest)");
    boolean pass = true;
    Short values[] = JAXRPC_Data.Short_data;
    Short response;
    TestUtil.logMsg("Passing/Returning Short class to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.wrapperShortTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response, "Short"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallPrimitiveTest:(ShortTest)");
    return pass;
  }

  private boolean intTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(intTest)");
    boolean pass = true;
    int values[] = JAXRPC_Data.int_data;
    int response;
    TestUtil.logMsg("Passing/Returning int data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.intTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response))
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

  private boolean IntegerTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(IntegerTest)");
    boolean pass = true;
    Integer values[] = JAXRPC_Data.Integer_data;
    Integer response;
    TestUtil.logMsg("Passing/Returning Integer class to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.wrapperIntegerTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response, "Integer"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallPrimitiveTest:(IntegerTest)");
    return pass;
  }

  private boolean longTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(longTest)");
    boolean pass = true;
    long values[] = JAXRPC_Data.long_data;
    long response;
    TestUtil.logMsg("Passing/Returning long data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.longTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response))
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

  private boolean LongTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(LongTest)");
    boolean pass = true;
    Long values[] = JAXRPC_Data.Long_data;
    Long response;
    TestUtil.logMsg("Passing/Returning Long class to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.wrapperLongTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response, "Long"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallPrimitiveTest:(LongTest)");
    return pass;
  }

  private boolean floatTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(floatTest)");
    boolean pass = true;
    float values[] = JAXRPC_Data.float_data;
    float response;
    TestUtil.logMsg("Passing/Returning float data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.floatTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response))
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

  private boolean FloatTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(FloatTest)");
    boolean pass = true;
    Float values[] = JAXRPC_Data.Float_data;
    Float response;
    TestUtil.logMsg("Passing/Returning Float class to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.wrapperFloatTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response, "Float"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallPrimitiveTest:(FloatTest)");
    return pass;
  }

  private boolean doubleTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(doubleTest)");
    boolean pass = true;
    double values[] = JAXRPC_Data.double_data;
    double response;
    TestUtil.logMsg("Passing/Returning double data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.doubleTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response))
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

  private boolean DoubleTest() {
    TestUtil.logMsg("MarshallPrimitiveTest:(DoubleTest)");
    boolean pass = true;
    Double values[] = JAXRPC_Data.Double_data;
    Double response;
    TestUtil.logMsg("Passing/Returning Double class to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.wrapperDoubleTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response, "Double"))
          pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallPrimitiveTest:(DoubleTest)");
    return pass;
  }

  private boolean StringTest() {
    TestUtil.logMsg("MarshallStandardJavaClassesTest:(StringTest)");
    boolean pass = true;
    String values[] = JAXRPC_Data.String_data;
    String response;
    try {
      TestUtil.logMsg("Passing/Returning String class to/from JAXRPC Service");
      for (int i = 0; i < values.length; i++) {
        response = port.stringTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response, "String"))
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
    Calendar response;
    TestUtil.logMsg("Passing/Returning Calendar class to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.calendarTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response, "Calendar"))
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
    BigInteger response;
    TestUtil
        .logMsg("Passing/Returning BigInteger class to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.bigIntegerTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response, "BigInteger"))
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
    BigDecimal response;
    TestUtil
        .logMsg("Passing/Returning BigDecimal class to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.bigDecimalTest(values[i]);
        if (!JAXRPC_Data.compareValues(values[i], response, "BigDecimal"))
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
    boolean[] response;
    TestUtil.logMsg("Passing/Returning boolean array to/from JAXRPC Service");
    try {
      response = port.booleanArrayTest(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "boolean");
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
    byte[] response;
    try {
      response = port.byteArrayTest(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "byte");
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
    short[] response;
    try {
      response = port.shortArrayTest(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "short");
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
    int[] response;
    try {
      response = port.intArrayTest(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "int");
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
    long[] response;
    try {
      response = port.longArrayTest(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "long");
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
    float[] response;
    try {
      response = port.floatArrayTest(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "float");
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
    double[] response;
    try {
      response = port.doubleArrayTest(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "double");
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
    String[] response;
    try {
      response = port.stringArrayTest(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "String");
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
    Calendar[] response;
    try {
      response = port.calendarArrayTest(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "Calendar");
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
    BigInteger[] response;
    try {
      response = port.bigIntegerArrayTest(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "BigInteger");
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
    BigDecimal[] response;
    try {
      response = port.bigDecimalArrayTest(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "BigDecimal");
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
    JavaBean[] response;
    try {
      response = port.javaBeanArrayTest(values);
      pass = compareArrayValues(values, response, "JavaBean");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(JavaBeanArrayTest)");
    return pass;
  }

  private boolean booleanMultiArrayTest() {
    TestUtil.logMsg("MarshallJavaMultiArrayTest:(booleanMultiArrayTest)");
    boolean pass = true;
    boolean values[][] = JAXRPC_Data.boolean_multi_data;
    boolean[][] response;
    TestUtil
        .logMsg("Passing/Returning boolean multi array to/from JAXRPC Service");
    try {
      response = port.booleanMultiArrayTest(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response, "boolean");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaMultiArrayTest:(booleanMultiArrayTest)");
    return pass;
  }

  private boolean byteMultiArrayTest() {
    TestUtil.logMsg("MarshallJavaMultiArrayTest:(byteMultiArrayTest)");
    boolean pass = true;
    byte values[][] = JAXRPC_Data.byte_multi_data;
    byte[][] response;
    TestUtil
        .logMsg("Passing/Returning byte multi array to/from JAXRPC Service");
    try {
      response = port.byteMultiArrayTest(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response, "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaMultiArrayTest:(byteMultiArrayTest)");
    return pass;
  }

  private boolean shortMultiArrayTest() {
    TestUtil.logMsg("MarshallJavaMultiArrayTest:(shortMultiArrayTest)");
    boolean pass = true;
    short values[][] = JAXRPC_Data.short_multi_data;
    short[][] response;
    TestUtil
        .logMsg("Passing/Returning short multi array to/from JAXRPC Service");
    try {
      response = port.shortMultiArrayTest(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response, "short");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaMultiArrayTest:(shortMultiArrayTest)");
    return pass;
  }

  private boolean intMultiArrayTest() {
    TestUtil.logMsg("MarshallJavaMultiArrayTest:(intMultiArrayTest)");
    boolean pass = true;
    int values[][] = JAXRPC_Data.int_multi_data;
    int[][] response;
    TestUtil.logMsg("Passing/Returning int multi array to/from JAXRPC Service");
    try {
      response = port.intMultiArrayTest(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response, "int");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaMultiArrayTest:(intMultiArrayTest)");
    return pass;
  }

  private boolean longMultiArrayTest() {
    TestUtil.logMsg("MarshallJavaMultiArrayTest:(longMultiArrayTest)");
    boolean pass = true;
    long values[][] = JAXRPC_Data.long_multi_data;
    long[][] response;
    TestUtil
        .logMsg("Passing/Returning long multi array to/from JAXRPC Service");
    try {
      response = port.longMultiArrayTest(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response, "long");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaMultiArrayTest:(longMultiArrayTest)");
    return pass;
  }

  private boolean floatMultiArrayTest() {
    TestUtil.logMsg("MarshallJavaMultiArrayTest:(floatMultiArrayTest)");
    boolean pass = true;
    float values[][] = JAXRPC_Data.float_multi_data;
    float[][] response;
    TestUtil
        .logMsg("Passing/Returning float multi array to/from JAXRPC Service");
    try {
      response = port.floatMultiArrayTest(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response, "float");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaMultiArrayTest:(floatMultiArrayTest)");
    return pass;
  }

  private boolean doubleMultiArrayTest() {
    TestUtil.logMsg("MarshallJavaMultiArrayTest:(doubleMultiArrayTest)");
    boolean pass = true;
    double values[][] = JAXRPC_Data.double_multi_data;
    double[][] response;
    TestUtil
        .logMsg("Passing/Returning double multi array to/from JAXRPC Service");
    try {
      response = port.doubleMultiArrayTest(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response, "double");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaMultiArrayTest:(doubleMultiArrayTest)");
    return pass;
  }

  private boolean StringMultiArrayTest() {
    TestUtil.logMsg("MarshallJavaMultiArrayTest:(StringMultiArrayTest)");
    boolean pass = true;
    String values[][] = JAXRPC_Data.String_multi_data;
    String[][] response;
    TestUtil
        .logMsg("Passing/Returning String multi array to/from JAXRPC Service");
    try {
      response = port.stringMultiArrayTest(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response, "String");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaMultiArrayTest:(StringMultiArrayTest)");
    return pass;
  }

  private boolean CalendarMultiArrayTest() {
    TestUtil.logMsg("MarshallJavaMultiArrayTest:(CalendarMultiArrayTest)");
    boolean pass = true;
    Calendar values[][] = JAXRPC_Data.GregorianCalendar_multi_data;
    Calendar[][] response;
    TestUtil.logMsg(
        "Passing/Returning Calendar multi array to/from JAXRPC Service");
    try {
      response = port.calendarMultiArrayTest(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response, "Calendar");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
      ;
    }
    printTestStatus(pass,
        "MarshallJavaMultiArrayTest:(CalendarMultiArrayTest)");
    return pass;
  }

  private boolean BigIntegerMultiArrayTest() {
    TestUtil.logMsg("MarshallJavaMultiArrayTest:(BigIntegerMultiArrayTest)");
    boolean pass = true;
    BigInteger values[][] = JAXRPC_Data.BigInteger_multi_data;
    BigInteger[][] response;
    TestUtil.logMsg(
        "Passing/Returning BigInteger multi array to/from JAXRPC Service");
    try {
      response = port.bigIntegerMultiArrayTest(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response,
          "BigInteger");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass,
        "MarshallJavaMultiArrayTest:(BigIntegerMultiArrayTest)");
    return pass;
  }

  private boolean BigDecimalMultiArrayTest() {
    TestUtil.logMsg("MarshallJavaMultiArrayTest:(BigDecimalMultiArrayTest)");
    boolean pass = true;
    BigDecimal values[][] = JAXRPC_Data.BigDecimal_multi_data;
    BigDecimal[][] response;
    TestUtil.logMsg(
        "Passing/Returning BigDecimal multi array to/from JAXRPC Service");
    try {
      response = port.bigDecimalMultiArrayTest(values);
      pass = JAXRPC_Data.compareMultiArrayValues(values, response,
          "BigDecimal");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass,
        "MarshallJavaMultiArrayTest:(BigDecimalMultiArrayTest)");
    return pass;
  }

  private boolean JavaBeanMultiArrayTest() {
    TestUtil.logMsg("MarshallJavaMultiArrayTest:(JavaBeanMultiArrayTest)");
    boolean pass = true;

    init_JavaBean_Data();
    JavaBean values[][] = JavaBean_multi_data;
    JavaBean[][] response;
    TestUtil.logMsg(
        "Passing/Returning JavaBean multi array to/from JAXRPC Service");
    try {
      response = port.javaBeanMultiArrayTest(values);
      pass = compareMultiArrayValues(values, response, "JavaBean");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass,
        "MarshallJavaMultiArrayTest:(JavaBeanMultiArrayTest)");
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
    JavaBean_data[0].setMyBoolean1(new Boolean("false"));
    JavaBean_data[0].setMyByte1(new Byte(Byte.MIN_VALUE));
    JavaBean_data[0].setMyShort1(new Short(Short.MIN_VALUE));
    JavaBean_data[0].setMyInt1(new Integer(Integer.MIN_VALUE));
    JavaBean_data[0].setMyLong1(new Long(Long.MIN_VALUE));
    JavaBean_data[0].setMyFloat1(new Float(Float.MIN_VALUE));
    JavaBean_data[0].setMyDouble1(new Double(Double.MIN_VALUE));
    JavaBean_data[0].setMyString("");
    JavaBean_data[0].setMyBigInteger(JAXRPC_Data.BigInteger_data[0]);
    JavaBean_data[0].setMyBigDecimal(JAXRPC_Data.BigDecimal_data[0]);
    JavaBean_data[0].setMyCalendar(JAXRPC_Data.GregorianCalendar_data[0]);
    JavaBean_data[0].setMyBooleanArray(JAXRPC_Data.boolean_data);
    JavaBean_data[0].setMyByteArray(JAXRPC_Data.byte_data);
    JavaBean_data[0].setMyShortArray(JAXRPC_Data.short_data);
    JavaBean_data[0].setMyIntArray(JAXRPC_Data.int_data);
    JavaBean_data[0].setMyLongArray(JAXRPC_Data.long_data);
    JavaBean_data[0].setMyFloatArray(JAXRPC_Data.float_data);
    JavaBean_data[0].setMyDoubleArray(JAXRPC_Data.double_data);
    JavaBean_data[0].setMyBoolean1Array(JAXRPC_Data.Boolean_data);
    JavaBean_data[0].setMyShort1Array(JAXRPC_Data.Short_data);
    JavaBean_data[0].setMyInt1Array(JAXRPC_Data.Integer_data);
    JavaBean_data[0].setMyLong1Array(JAXRPC_Data.Long_data);
    JavaBean_data[0].setMyFloat1Array(JAXRPC_Data.Float_data);
    JavaBean_data[0].setMyDouble1Array(JAXRPC_Data.Double_data);
    JavaBean_data[0].setMyStringArray(JAXRPC_Data.String_data);
    JavaBean_data[0].setMyStringArray(JAXRPC_Data.String_data);
    JavaBean_data[0].setMyBigIntegerArray(JAXRPC_Data.BigInteger_data);
    JavaBean_data[0].setMyBigDecimalArray(JAXRPC_Data.BigDecimal_data);
    JavaBean_data[0].setMyCalendarArray(JAXRPC_Data.GregorianCalendar_data);

    JavaBean_data[1].setMyBoolean(true);
    JavaBean_data[1].setMyByte(Byte.MAX_VALUE);
    JavaBean_data[1].setMyShort(Short.MAX_VALUE);
    JavaBean_data[1].setMyInt(Integer.MAX_VALUE);
    JavaBean_data[1].setMyLong(Long.MAX_VALUE);
    JavaBean_data[1].setMyFloat(Float.MAX_VALUE);
    JavaBean_data[1].setMyDouble(Double.MAX_VALUE);
    JavaBean_data[1].setMyBoolean1(new Boolean("true"));
    JavaBean_data[1].setMyByte1(new Byte(Byte.MAX_VALUE));
    JavaBean_data[1].setMyShort1(new Short(Short.MAX_VALUE));
    JavaBean_data[1].setMyInt1(new Integer(Integer.MAX_VALUE));
    JavaBean_data[1].setMyLong1(new Long(Long.MAX_VALUE));
    JavaBean_data[1].setMyFloat1(new Float(Float.MAX_VALUE));
    JavaBean_data[1].setMyDouble1(new Double(Double.MAX_VALUE));
    JavaBean_data[1].setMyString("");
    JavaBean_data[1].setMyBigInteger(JAXRPC_Data.BigInteger_data[1]);
    JavaBean_data[1].setMyBigDecimal(JAXRPC_Data.BigDecimal_data[1]);
    JavaBean_data[1].setMyCalendar(JAXRPC_Data.GregorianCalendar_data[1]);
    JavaBean_data[1].setMyBooleanArray(JAXRPC_Data.boolean_data);
    JavaBean_data[1].setMyByteArray(JAXRPC_Data.byte_data);
    JavaBean_data[1].setMyShortArray(JAXRPC_Data.short_data);
    JavaBean_data[1].setMyIntArray(JAXRPC_Data.int_data);
    JavaBean_data[1].setMyLongArray(JAXRPC_Data.long_data);
    JavaBean_data[1].setMyFloatArray(JAXRPC_Data.float_data);
    JavaBean_data[1].setMyDoubleArray(JAXRPC_Data.double_data);
    JavaBean_data[1].setMyBoolean1Array(JAXRPC_Data.Boolean_data);
    JavaBean_data[1].setMyShort1Array(JAXRPC_Data.Short_data);
    JavaBean_data[1].setMyInt1Array(JAXRPC_Data.Integer_data);
    JavaBean_data[1].setMyLong1Array(JAXRPC_Data.Long_data);
    JavaBean_data[1].setMyFloat1Array(JAXRPC_Data.Float_data);
    JavaBean_data[1].setMyDouble1Array(JAXRPC_Data.Double_data);
    JavaBean_data[1].setMyStringArray(JAXRPC_Data.String_data);
    JavaBean_data[1].setMyStringArray(JAXRPC_Data.String_data);
    JavaBean_data[1].setMyBigIntegerArray(JAXRPC_Data.BigInteger_data);
    JavaBean_data[1].setMyBigDecimalArray(JAXRPC_Data.BigDecimal_data);
    JavaBean_data[1].setMyCalendarArray(JAXRPC_Data.GregorianCalendar_data);

  }

  public String toStringJavaBean(JavaBean v) {
    return "myBoolean: " + v.isMyBoolean() + ", myByte: " + v.getMyByte()
        + ", myShort: " + v.getMyShort() + ", myInt: " + v.getMyInt()
        + ", myLong: " + v.getMyLong() + ", myFloat: " + v.getMyFloat()
        + ", myDouble: " + v.getMyDouble() + ", myBoolean1: "
        + v.getMyBoolean1() + ", myByte1: " + v.getMyByte1() + ", myShort1: "
        + v.getMyShort1() + ", myInt1: " + v.getMyInt1() + ", myLong1: "
        + v.getMyLong1() + ", myFloat1: " + v.getMyFloat1() + ", myDouble1: "
        + v.getMyDouble1() + ", myString: " + v.getMyString()
        + ", myBigInteger: " + v.getMyBigInteger() + ", myBigDecimal: "
        + v.getMyBigDecimal() + ", myCalendar: " + v.getMyCalendar()
        + ", myBooleanArray: " + v.getMyBooleanArray() + ", myByteArray: "
        + v.getMyByteArray() + ", myShortArray: " + v.getMyShortArray()
        + ", myIntArray: " + v.getMyIntArray() + ", myLongArray: "
        + v.getMyLongArray() + ", myFloatArray: " + v.getMyFloatArray()
        + ", myDoubleArray: " + v.getMyDoubleArray() + ", myBoolean1Array: "
        + v.getMyBoolean1Array() + ", myShort1Array: " + v.getMyShort1Array()
        + ", myInt1Array: " + v.getMyInt1Array() + ", myLong1Array: "
        + v.getMyLong1Array() + ", myFloat1Array: " + v.getMyFloat1Array()
        + ", myDouble1Array: " + v.getMyDouble1Array() + ", myStringArray: "
        + v.getMyStringArray() + ", myBigIntegerArray: "
        + v.getMyBigIntegerArray() + ", myBigDecimalArray: "
        + v.getMyBigDecimalArray() + ", myCalendarArray: "
        + v.getMyCalendarArray();
  }

  public boolean compareJavaBeans(JavaBean e, JavaBean r) {
    return e.isMyBoolean() == r.isMyBoolean() && e.getMyByte() == r.getMyByte()
        && e.getMyShort() == r.getMyShort() && e.getMyInt() == r.getMyInt()
        && e.getMyLong() == r.getMyLong() && e.getMyFloat() == r.getMyFloat()
        && e.getMyDouble() == r.getMyDouble()
        && e.getMyBoolean1().equals(r.getMyBoolean1())
        && e.getMyByte1().equals(r.getMyByte1())
        && e.getMyShort1().equals(r.getMyShort1())
        && e.getMyInt1().equals(r.getMyInt1())
        && e.getMyLong1().equals(r.getMyLong1())
        && e.getMyFloat1().equals(r.getMyFloat1())
        && e.getMyDouble1().equals(r.getMyDouble1())
        && e.getMyString().equals(r.getMyString())
        && e.getMyBigInteger().equals(r.getMyBigInteger())
        && e.getMyBigDecimal().equals(r.getMyBigDecimal())
        && JAXRPC_Data.compareCalendars(e.getMyCalendar(), r.getMyCalendar())
        && JAXRPC_Data.compareArrayValues(e.getMyBooleanArray(),
            r.getMyBooleanArray(), "boolean")
        && JAXRPC_Data.compareArrayValues(e.getMyByteArray(),
            r.getMyByteArray(), "byte")
        && JAXRPC_Data.compareArrayValues(e.getMyShortArray(),
            r.getMyShortArray(), "short")
        && JAXRPC_Data.compareArrayValues(e.getMyIntArray(), r.getMyIntArray(),
            "int")
        && JAXRPC_Data.compareArrayValues(e.getMyLongArray(),
            r.getMyLongArray(), "long")
        && JAXRPC_Data.compareArrayValues(e.getMyFloatArray(),
            r.getMyFloatArray(), "float")
        && JAXRPC_Data.compareArrayValues(e.getMyDoubleArray(),
            r.getMyDoubleArray(), "double")
        && JAXRPC_Data.compareArrayValues(e.getMyBoolean1Array(),
            r.getMyBoolean1Array(), "Boolean")
        && JAXRPC_Data.compareArrayValues(e.getMyShort1Array(),
            r.getMyShort1Array(), "Short")
        && JAXRPC_Data.compareArrayValues(e.getMyInt1Array(),
            r.getMyInt1Array(), "Integer")
        && JAXRPC_Data.compareArrayValues(e.getMyLong1Array(),
            r.getMyLong1Array(), "Long")
        && JAXRPC_Data.compareArrayValues(e.getMyFloat1Array(),
            r.getMyFloat1Array(), "Float")
        && JAXRPC_Data.compareArrayValues(e.getMyDouble1Array(),
            r.getMyDouble1Array(), "Double")
        && JAXRPC_Data.compareArrayValues(e.getMyStringArray(),
            r.getMyStringArray(), "String")
        && JAXRPC_Data.compareArrayValues(e.getMyBigIntegerArray(),
            r.getMyBigIntegerArray(), "BigInteger")
        && JAXRPC_Data.compareArrayValues(e.getMyBigDecimalArray(),
            r.getMyBigDecimalArray(), "BigDecimal")
        && JAXRPC_Data.compareArrayValues(e.getMyCalendarArray(),
            r.getMyCalendarArray(), "Calendar");
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

  private void dumpMultiArrayValues(Object o, String t) {
    System.out.println("JAXRPC_Data:dumpMultiArrayValues");
    System.out.println(
        "Dumping " + t + " multiarray, size=" + getMultiArraySize(o, t));
    if (t.equals("JavaBean")) {
      JavaBean[][] v = (JavaBean[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          System.out.println("- " + toStringJavaBean(v[i][k]));
      }
    }
  }

  private int getArraySize(Object o, String t) {
    System.out.println("JAXRPC_Data:getArraySize");
    if (t.equals("JavaBean")) {
      return ((JavaBean[]) o).length;
    }
    return -1;
  }

  private String getMultiArraySize(Object o, String t) {
    System.out.println("JAXRPC_Data:getMultiArraySize");
    if (t.equals("JavaBean")) {
      JavaBean[][] m = (JavaBean[][]) o;
      return ("[" + m.length + "][" + m[0].length + "]");
    }
    return "unknown";
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

  private boolean compareMultiArrayValues(Object e, Object r, String t) {
    System.out.println("JAXRPC_Data:compareMultiArrayValues");
    boolean pass = true;

    if (t.equals("JavaBean")) {
      JavaBean[][] exp = (JavaBean[][]) e;
      JavaBean[][] rec = (JavaBean[][]) r;
      if (rec.length != exp.length) {
        System.out.println("Multi Array Size MisMatch: expected " + exp.length
            + ", received " + rec.length);
        pass = false;
      }
      for (int i = 0; i < rec.length; i++) {
        if (rec[i].length != exp[i].length) {
          System.out.println("Multi Array Size MisMatch: expected "
              + exp[i].length + ", received " + rec[i].length);
          pass = false;
        } else {
          for (int k = 0; k < rec[i].length; k++) {
            if (rec[i][k] == exp[i][k])
              continue;
            if ((rec[i][k] == null && exp[i][k] != null)
                && (rec[i][k] != null && exp[i][k] == null)) {
              pass = false;
            } else if (!compareJavaBeans(exp[i][k], exp[i][k])) {
              System.out.println(
                  "Array Mismatch: expected " + toStringJavaBean(exp[i][k])
                      + ", received " + toStringJavaBean(rec[i][k]));
              pass = false;
            }
          }
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

  private String returnMultiArrayValues(Object o, String t) {
    String values = null;
    if (t.equals("JavaBean")) {
      JavaBean[][] v = (JavaBean[][]) o;
      for (int i = 0; i < v.length; i++) {
        for (int k = 0; k < v[i].length; k++)
          values += ", " + toStringJavaBean(v[i][k]);
      }
    }
    return values;
  }
}
