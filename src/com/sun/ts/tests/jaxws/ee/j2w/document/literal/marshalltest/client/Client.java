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

package com.sun.ts.tests.jaxws.ee.j2w.document.literal.marshalltest.client;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;

import jakarta.xml.ws.*;
import javax.xml.namespace.QName;

import java.util.*;

import java.math.BigInteger;
import java.math.BigDecimal;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.common.*;

import javax.naming.InitialContext;

import javax.xml.datatype.*;

public class Client extends ServiceEETest {
  // The webserver defaults (overidden by harness properties)
  private static final String PROTOCOL = "http";

  private static final String HOSTNAME = "localhost";

  private static final int PORTNUM = 8000;

  // The webserver host and port property names (harness properties)
  private static final String WEBSERVERHOSTPROP = "webServerHost";

  private static final String WEBSERVERPORTPROP = "webServerPort";

  private static final String MODEPROP = "platform.mode";

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.j2w.document.literal.marshalltest.client.";

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "j2wdlmarshalltest.endpoint.1";

  private static final String WSDLLOC_URL = "j2wdlmarshalltest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  // ServiceName and PortName mapping configuration going java-to-wsdl
  private static final String SERVICE_NAME = "MarshallTestService";

  private static final String PORT_NAME = "MarshallTestPort";

  private static final String NAMESPACEURI = "http://marshalltestservice.org/wsdl";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  String modeProperty = null; // platform.mode -> (standalone|jakartaEE)

  MarshallTest port = null;

  static MarshallTestService service = null;

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
    port = (MarshallTest) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        MarshallTestService.class, PORT_QNAME, MarshallTest.class);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (MarshallTest) service.getPort(MarshallTest.class);
    TestUtil.logMsg("port=" + port);
    TestUtil.logMsg("Obtained port");
    getTargetEndpointAddress(port);
    // JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getTargetEndpointAddress(Object port) throws Exception {
    TestUtil.logMsg("Get Target Endpoint Address for port=" + port);
    String url = JAXWS_Util.getTargetEndpointAddress(port);
    TestUtil.logMsg("Target Endpoint Address=" + url);
  }

  private JavaBean JavaBean_data[] = null;

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
        service = (MarshallTestService) getSharedObject();
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
    JavaBean_data = new JavaBean[2];
    JavaBean_data[0] = new JavaBean();
    JavaBean_data[1] = new JavaBean();
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
   * @assertion_ids: JAXWS:SPEC:2017; JAXWS:SPEC:2040; JAXWS:SPEC:10011;
   * JAXWS:SPEC:3000; JAXWS:SPEC:3012; JAXWS:SPEC:3057; JAXWS:SPEC:7000;
   * JAXWS:SPEC:3058;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each primitive type pass its value as input to the
   * corresponding RPC method and receive it back as the return value. Compare
   * results of each value/type of what was sent and what was returned. Verify
   * they are equal.
   *
   * Description Java primititive types and the corresponding Java classes:
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
   * @assertion_ids: JAXWS:SPEC:2017; JAXWS:SPEC:2040; JAXWS:SPEC:10011;
   * JAXWS:SPEC:3000; JAXWS:SPEC:3012; JAXWS:SPEC:3057; JAXWS:SPEC:7000;
   * JAXWS:SPEC:3058;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * standard java class type. For each standard java class above pass its value
   * as input to the corresponding RPC method and receive it back as the return
   * value. Compare results of each value/type of what was sent sent and what
   * was returned. Verify they are equal.
   *
   * Description Standard Java Classes: o java.lang.String o java.util.Calendar
   * o java.math.BigInteger o java.math.BigDecimal
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
  // Java Array Single-Dimensional for all supported JAX-WS types
  // ====================================================================

  /*
   * @testName: MarshallJavaArrayTest
   *
   * @assertion_ids: JAXWS:SPEC:2017; JAXWS:SPEC:2040; JAXWS:SPEC:10011;
   * JAXWS:SPEC:3000; JAXWS:SPEC:3012; JAXWS:SPEC:3057; JAXWS:SPEC:7000;
   * JAXWS:SPEC:3058;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, invoke the RPC methods for each
   * java type supported. For each java type supported pass an arrary of values
   * as input to the corresponding RPC method and receive it back as the return
   * value. Compare results of each array type of what was sent and what was
   * returned. Verify they are equal.
   *
   * Description Single dimensional Java arrays are tested.
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
    if (!wrapperBooleanArrayTest())
      pass = false;
    if (!wrapperByteArrayTest())
      pass = false;
    if (!wrapperShortArrayTest())
      pass = false;
    if (!wrapperIntArrayTest())
      pass = false;
    if (!wrapperLongArrayTest())
      pass = false;
    if (!wrapperFloatArrayTest())
      pass = false;
    if (!wrapperDoubleArrayTest())
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
  // Service Specific Exception
  // ====================================================================

  /*
   * @testName: MarshallServiceExceptionTest
   *
   * @assertion_ids: JAXWS:SPEC:2017; JAXWS:SPEC:2040; JAXWS:SPEC:10011;
   * JAXWS:SPEC:3000; JAXWS:SPEC:3012; JAXWS:SPEC:3057; JAXWS:SPEC:7000;
   * JAXWS:SPEC:3058;
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
    TestUtil.logMsg("Throwing a ServiceException from JAXWS Service");
    try {
      try {
        port.myServiceExceptionTest();
        TestUtil.logErr("no fault occurred");
        pass = false;
      } catch (Throwable response) {
        TestUtil.logMsg("a fault occurred: " + response);
        TestUtil.logMsg("response=" + response);
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
  // JavaBeans Class
  // ====================================================================

  /*
   * @testName: MarshallJavaBeanTest
   *
   * @assertion_ids: JAXWS:SPEC:2017; JAXWS:SPEC:2040; JAXWS:SPEC:10011;
   * JAXWS:SPEC:3000; JAXWS:SPEC:3012; JAXWS:SPEC:3057; JAXWS:SPEC:7000;
   * JAXWS:SPEC:3058;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC method
   * JavaBeanTest. Pass a JavaBean value to the RPC method and receive it back
   * as the return value. Compare results of JavaBean value from what was sent
   * and what was returned. Verify they are equal.
   *
   * Description A client can invoke an RPC method via generated stub passing a
   * JavaBean value as input argument and receiving a JavaBean value as a return
   * value.
   */
  public void MarshallJavaBeanTest() throws Fault {
    TestUtil.logMsg("MarshallJavaBeanTest");
    boolean pass = true;

    init_JavaBean_Data();
    JavaBean values[] = JavaBean_data;
    JavaBean response;
    TestUtil.logMsg(
        "Passing/Returning JavaBean JavaBean class to/from JAXWS Service");
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
  // The void type
  // ====================================================================

  /*
   * @testName: MarshallVoidTest
   *
   * @assertion_ids: JAXWS:SPEC:2017; JAXWS:SPEC:2040; JAXWS:SPEC:10011;
   * JAXWS:SPEC:3000; JAXWS:SPEC:3012; JAXWS:SPEC:3057; JAXWS:SPEC:7000;
   * JAXWS:SPEC:3058;
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
    TestUtil.logMsg("Handling a void type to/from JAXWS Service");
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
    Boolean values[] = JAXWS_Data.Boolean_nonull_data;
    Boolean response;
    TestUtil.logMsg("Passing/Returning Boolean data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.booleanTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "Boolean"))
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
    Boolean values[] = JAXWS_Data.Boolean_data;
    Boolean response;
    try {
      TestUtil.logMsg("Passing/Returning Boolean class to/from JAXWS Service");
      for (int i = 0; i < values.length; i++) {
        response = port.wrapperBooleanTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "Boolean"))
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
    Byte values[] = JAXWS_Data.Byte_nonull_data;
    Byte response;
    TestUtil.logMsg("Passing/Returning Byte data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.byteTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "Byte"))
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
    Byte values[] = JAXWS_Data.Byte_data;
    Byte response;
    TestUtil.logMsg("Passing/Returning Byte class to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.wrapperByteTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "Byte"))
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
    Short values[] = JAXWS_Data.Short_nonull_data;
    Short response;
    TestUtil.logMsg("Passing/Returning Short data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.shortTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "Short"))
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
    Short values[] = JAXWS_Data.Short_data;
    Short response;
    TestUtil.logMsg("Passing/Returning Short class to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        if (values[i] == null)
          continue;
        response = port.wrapperShortTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "Short"))
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
    Integer values[] = JAXWS_Data.Integer_nonull_data;
    Integer response;
    TestUtil.logMsg("Passing/Returning Integer data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.intTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "Integer"))
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
    Integer values[] = JAXWS_Data.Integer_data;
    Integer response;
    TestUtil.logMsg("Passing/Returning Integer class to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.wrapperIntegerTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "Integer"))
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
    Long values[] = JAXWS_Data.Long_nonull_data;
    Long response;
    TestUtil.logMsg("Passing/Returning Long data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.longTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "Long"))
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
    Long values[] = JAXWS_Data.Long_data;
    Long response;
    TestUtil.logMsg("Passing/Returning Long class to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.wrapperLongTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "Long"))
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
    Float values[] = JAXWS_Data.Float_nonull_data;
    Float response;
    TestUtil.logMsg("Passing/Returning Float data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.floatTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "Float"))
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
    Float values[] = JAXWS_Data.Float_data;
    Float response;
    TestUtil.logMsg("Passing/Returning Float class to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.wrapperFloatTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "Float"))
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
    Double values[] = JAXWS_Data.Double_nonull_data;
    Double response;
    TestUtil.logMsg("Passing/Returning Double data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.doubleTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "Double"))
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
    Double values[] = JAXWS_Data.Double_data;
    Double response;
    TestUtil.logMsg("Passing/Returning Double class to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.wrapperDoubleTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "Double"))
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
    String values[] = JAXWS_Data.String_data;
    String response;
    try {
      TestUtil.logMsg("Passing/Returning String class to/from JAXWS Service");
      for (int i = 0; i < values.length; i++) {
        response = port.stringTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "String"))
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
    XMLGregorianCalendar values[] = JAXWS_Data.XMLGregorianCalendar_data;
    XMLGregorianCalendar response;
    TestUtil.logMsg("Passing/Returning Calendar class to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.calendarTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response,
            "XMLGregorianCalendar"))
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
    BigInteger values[] = JAXWS_Data.BigInteger_data;
    BigInteger response;
    TestUtil.logMsg("Passing/Returning BigInteger class to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.bigIntegerTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "BigInteger"))
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
    BigDecimal values[] = JAXWS_Data.BigDecimal_data;
    BigDecimal response;
    TestUtil.logMsg("Passing/Returning BigDecimal class to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.bigDecimalTest(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response, "BigDecimal"))
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
    List<Boolean> values = JAXWS_Data.list_Boolean_nonull_data;
    List<Boolean> response;
    TestUtil.logMsg("Passing/Returning boolean array to/from JAXWS Service");
    try {
      response = port.booleanArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Boolean");
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
    byte[] values = JAXWS_Data.byte_data;
    byte[] response;
    try {
      response = port.byteArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "byte");
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
    List<Short> values = JAXWS_Data.list_Short_nonull_data;
    List<Short> response;
    try {
      response = port.shortArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Short");
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
    List<Integer> values = JAXWS_Data.list_Integer_nonull_data;
    List<Integer> response;
    try {
      response = port.intArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Integer");
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
    List<Long> values = JAXWS_Data.list_Long_nonull_data;
    List<Long> response;
    try {
      response = port.longArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Long");
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
    List<Float> values = JAXWS_Data.list_Float_nonull_data;
    List<Float> response;
    try {
      response = port.floatArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Float");
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
    List<Double> values = JAXWS_Data.list_Double_nonull_data;
    List<Double> response;
    try {
      response = port.doubleArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Double");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(doubleArrayTest)");
    return pass;
  }

  private boolean wrapperBooleanArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(wrapperBooleanArrayTest)");
    boolean pass = true;
    List<Boolean> values = JAXWS_Data.list_Boolean_nonull_data;
    List<Boolean> response;
    TestUtil.logMsg("Passing/Returning boolean array to/from JAXWS Service");
    try {
      response = port.wrapperBooleanArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Boolean");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(wrapperBooleanArrayTest)");
    return pass;
  }

  private boolean wrapperByteArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(wrapperByteArrayTest)");
    boolean pass = true;
    List<Byte> values = JAXWS_Data.list_Byte_nonull_data;
    List<Byte> response;
    try {
      response = port.wrapperByteArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(wrapperByteArrayTest)");
    return pass;
  }

  private boolean wrapperShortArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(wrapperShortArrayTest)");
    boolean pass = true;
    List<Short> values = JAXWS_Data.list_Short_nonull_data;
    List<Short> response;
    try {
      response = port.wrapperShortArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Short");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(wrapperShortArrayTest)");
    return pass;
  }

  private boolean wrapperIntArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(wrapperIntArrayTest)");
    boolean pass = true;
    List<Integer> values = JAXWS_Data.list_Integer_nonull_data;
    List<Integer> response;
    try {
      response = port.wrapperIntArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Integer");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(wrapperIntArrayTest)");
    return pass;
  }

  private boolean wrapperLongArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(wrapperLongArrayTest)");
    boolean pass = true;
    List<Long> values = JAXWS_Data.list_Long_nonull_data;
    List<Long> response;
    try {
      response = port.wrapperLongArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Long");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(wrapperLongArrayTest)");
    return pass;
  }

  private boolean wrapperFloatArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(wrapperFloatArrayTest)");
    boolean pass = true;
    List<Float> values = JAXWS_Data.list_Float_nonull_data;
    List<Float> response;
    try {
      response = port.wrapperFloatArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Float");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(wrapperFloatArrayTest)");
    return pass;
  }

  private boolean wrapperDoubleArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(wrapperDoubleArrayTest)");
    boolean pass = true;
    List<Double> values = JAXWS_Data.list_Double_nonull_data;
    List<Double> response;
    try {
      response = port.wrapperDoubleArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Double");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }
    printTestStatus(pass, "MarshallJavaArrayTest:(wrapperDoubleArrayTest)");
    return pass;
  }

  private boolean StringArrayTest() {
    TestUtil.logMsg("MarshallJavaArrayTest:(StringArrayTest)");
    boolean pass = true;
    List<String> values = JAXWS_Data.list_String_nonull_data;
    List<String> response;
    try {
      response = port.stringArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "String");
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
    List<XMLGregorianCalendar> values = JAXWS_Data.list_XMLGregorianCalendar_nonull_data;
    List<XMLGregorianCalendar> response;
    try {
      response = port.calendarArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response,
          "XMLGregorianCalendar");
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
    List<BigInteger> values = JAXWS_Data.list_BigInteger_nonull_data;
    List<BigInteger> response;
    try {
      response = port.bigIntegerArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "BigInteger");
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
    List<BigDecimal> values = JAXWS_Data.list_BigDecimal_nonull_data;
    List<BigDecimal> response;
    try {
      response = port.bigDecimalArrayTest(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "BigDecimal");
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
    List<JavaBean> list_JavaBean_data = (List<JavaBean>) Arrays
        .asList(JavaBean_data);
    List<JavaBean> values = list_JavaBean_data;
    List<JavaBean> response;
    try {
      JAXWS_Data.dumpListValues(values);
      response = port.javaBeanArrayTest(values);
      JAXWS_Data.dumpListValues(response);
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
    JavaBean_data[0].setMyBoolean1(Boolean.valueOf("false"));
    JavaBean_data[0].setMyByte1(Byte.valueOf(Byte.MIN_VALUE));
    JavaBean_data[0].setMyShort1(Short.valueOf(Short.MIN_VALUE));
    JavaBean_data[0].setMyInt1(Integer.valueOf(Integer.MIN_VALUE));
    JavaBean_data[0].setMyLong1(Long.valueOf(Long.MIN_VALUE));
    JavaBean_data[0].setMyFloat1(Float.valueOf(Float.MIN_VALUE));
    JavaBean_data[0].setMyDouble1(Double.valueOf(Double.MIN_VALUE));
    JavaBean_data[0].setMyString("");
    JavaBean_data[0].setMyBigInteger(JAXWS_Data.BigInteger_data[0]);
    JavaBean_data[0].setMyBigDecimal(JAXWS_Data.BigDecimal_data[0]);
    JavaBean_data[0].setMyCalendar(JAXWS_Data.XMLGregorianCalendar_data[0]);

    JavaBean_data[1].setMyBoolean(true);
    JavaBean_data[1].setMyByte(Byte.MAX_VALUE);
    JavaBean_data[1].setMyShort(Short.MAX_VALUE);
    JavaBean_data[1].setMyInt(Integer.MAX_VALUE);
    JavaBean_data[1].setMyLong(Long.MAX_VALUE);
    JavaBean_data[1].setMyFloat(Float.MAX_VALUE);
    JavaBean_data[1].setMyDouble(Double.MAX_VALUE);
    JavaBean_data[1].setMyBoolean1(Boolean.valueOf("true"));
    JavaBean_data[1].setMyByte1(Byte.valueOf(Byte.MAX_VALUE));
    JavaBean_data[1].setMyShort1(Short.valueOf(Short.MAX_VALUE));
    JavaBean_data[1].setMyInt1(Integer.valueOf(Integer.MAX_VALUE));
    JavaBean_data[1].setMyLong1(Long.valueOf(Long.MAX_VALUE));
    JavaBean_data[1].setMyFloat1(Float.valueOf(Float.MAX_VALUE));
    JavaBean_data[1].setMyDouble1(Double.valueOf(Double.MAX_VALUE));
    JavaBean_data[1].setMyString("");
    JavaBean_data[1].setMyBigInteger(JAXWS_Data.BigInteger_data[1]);
    JavaBean_data[1].setMyBigDecimal(JAXWS_Data.BigDecimal_data[1]);
    JavaBean_data[1].setMyCalendar(JAXWS_Data.XMLGregorianCalendar_data[1]);

  }

  public String toStringJavaBean(JavaBean v) {
    return "myBoolean: " + v.isMyBoolean() + ", myByte: " + v.getMyByte()
        + ", myShort: " + v.getMyShort() + ", myInt: " + v.getMyInt()
        + ", myLong: " + v.getMyLong() + ", myFloat: " + v.getMyFloat()
        + ", myDouble: " + v.getMyDouble() + ", myBoolean1: " + v.isMyBoolean1()
        + ", myByte1: " + v.getMyByte1() + ", myShort1: " + v.getMyShort1()
        + ", myInt1: " + v.getMyInt1() + ", myLong1: " + v.getMyLong1()
        + ", myFloat1: " + v.getMyFloat1() + ", myDouble1: " + v.getMyDouble1()
        + ", myString: " + v.getMyString() + ", myBigInteger: "
        + v.getMyBigInteger() + ", myBigDecimal: " + v.getMyBigDecimal()
        + ", myCalendar: " + v.getMyCalendar();
  }

  public boolean compareJavaBeans(JavaBean e, JavaBean r) {
    return e.isMyBoolean() == r.isMyBoolean() && e.getMyByte() == r.getMyByte()
        && e.getMyShort() == r.getMyShort() && e.getMyInt() == r.getMyInt()
        && e.getMyLong() == r.getMyLong() && e.getMyFloat() == r.getMyFloat()
        && e.getMyDouble() == r.getMyDouble()
        && e.isMyBoolean1().equals(r.isMyBoolean1())
        && e.getMyByte1().equals(r.getMyByte1())
        && e.getMyShort1().equals(r.getMyShort1())
        && e.getMyInt1().equals(r.getMyInt1())
        && e.getMyLong1().equals(r.getMyLong1())
        && e.getMyFloat1().equals(r.getMyFloat1())
        && e.getMyDouble1().equals(r.getMyDouble1())
        && e.getMyString().equals(r.getMyString())
        && e.getMyBigInteger().equals(r.getMyBigInteger())
        && e.getMyBigDecimal().equals(r.getMyBigDecimal()) && JAXWS_Data
            .compareXMLGregorianCalendars(e.getMyCalendar(), r.getMyCalendar());
  }

  // ==================================================================
  // Various utility classes used for dumping/comparing data
  // ==================================================================

  private void dumpArrayValues(Object o, String t) {
    System.out.println("JAXWS_Data:dumpArrayValues");
    System.out.println("Dumping " + t + " array, size=" + getArraySize(o, t));
    if (t.equals("JavaBean")) {
      JavaBean[] v = (JavaBean[]) o;
      for (int i = 0; i < v.length; i++)
        System.out.println("- " + toStringJavaBean(v[i]));
    }
  }

  private int getArraySize(Object o, String t) {
    System.out.println("JAXWS_Data:getArraySize");
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
    System.out.println("compareArrayValues");
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
}
