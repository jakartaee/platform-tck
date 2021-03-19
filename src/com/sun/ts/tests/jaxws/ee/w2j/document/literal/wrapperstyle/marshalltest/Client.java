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

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.wrapperstyle.marshalltest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;

import jakarta.xml.ws.*;

import java.util.Properties;
import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.List;

import java.math.BigInteger;
import java.math.BigDecimal;

import javax.xml.namespace.QName;
import javax.xml.datatype.XMLGregorianCalendar;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.wrapperstyle.marshalltest.";

  // service and port information
  private static final String NAMESPACEURI = "http://MarshallTest.org/";

  private static final String SERVICE_NAME = "MarshallTestService";

  private static final String PORT_NAME = "MarshallTestPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "w2jdlwmarshalltest.endpoint.1";

  private static final String WSDLLOC_URL = "w2jdlwmarshalltest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

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
    TestUtil.logMsg("Obtaining service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (MarshallTest) service.getMarshallTestPort();
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

    logMsg("setup ok");
  }

  public void cleanup() throws Fault {
    logMsg("cleanup ok");
  }

  private void printSeperationLine() {
    TestUtil.logMsg("---------------------------");
  }

  /*
   * @testName: MarshallSimpleTypesTest
   *
   * @assertion_ids: JAXWS:SPEC:2001; JAXWS:SPEC:2017; JAXWS:SPEC:2040;
   * JAXWS:SPEC:10011; JAXWS:SPEC:2024; JAXWS:SPEC:2027;
   * 
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void MarshallSimpleTypesTest() throws Fault {
    TestUtil.logMsg("MarshallSimpleTypesTest");
    boolean pass = true;

    if (!StringTest())
      pass = false;
    printSeperationLine();
    if (!IntegerTest())
      pass = false;
    printSeperationLine();
    if (!IntTest())
      pass = false;
    printSeperationLine();
    if (!LongTest())
      pass = false;
    printSeperationLine();
    if (!ShortTest())
      pass = false;
    printSeperationLine();
    if (!DecimalTest())
      pass = false;
    printSeperationLine();
    if (!FloatTest())
      pass = false;
    printSeperationLine();
    if (!DoubleTest())
      pass = false;
    printSeperationLine();
    if (!BooleanTest())
      pass = false;
    printSeperationLine();
    if (!ByteTest())
      pass = false;
    printSeperationLine();
    if (!QNameTest())
      pass = false;
    printSeperationLine();
    if (!DateTimeTest())
      pass = false;
    printSeperationLine();
    if (!Base64BinaryTest())
      pass = false;
    printSeperationLine();
    if (!HexBinaryTest())
      pass = false;
    printSeperationLine();

    if (!pass)
      throw new Fault("MarshallSimpleTypesTest failed");
  }

  /*
   * @testName: MarshallArraysOfSimpleTypesTest
   *
   * @assertion_ids: JAXWS:SPEC:2001; JAXWS:SPEC:2017; JAXWS:SPEC:2040;
   * JAXWS:SPEC:10011; JAXWS:SPEC:2024; JAXWS:SPEC:2027;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each primitive type pass its value as input to the
   * corresponding RPC method and receive it back as the return value. Compare
   * results of each value/type of what was sent and what was returned. Verify
   * they are equal.
   */
  public void MarshallArraysOfSimpleTypesTest() throws Fault {
    TestUtil.logMsg("MarshallArraysOfSimpleTypesTest");
    boolean pass = true;

    if (!StringArrayTest())
      pass = false;
    printSeperationLine();
    if (!IntegerArrayTest())
      pass = false;
    printSeperationLine();
    if (!IntArrayTest())
      pass = false;
    printSeperationLine();
    if (!LongArrayTest())
      pass = false;
    printSeperationLine();
    if (!ShortArrayTest())
      pass = false;
    printSeperationLine();
    if (!DecimalArrayTest())
      pass = false;
    printSeperationLine();
    if (!FloatArrayTest())
      pass = false;
    printSeperationLine();
    if (!DoubleArrayTest())
      pass = false;
    printSeperationLine();
    if (!BooleanArrayTest())
      pass = false;
    printSeperationLine();
    if (!ByteArrayTest())
      pass = false;
    printSeperationLine();
    if (!QNameArrayTest())
      pass = false;
    printSeperationLine();
    if (!DateTimeArrayTest())
      pass = false;
    printSeperationLine();

    if (!pass)
      throw new Fault("MarshallArraysOfSimpleTypesTest failed");
  }

  private boolean printTestStatus(boolean pass, String test) {
    if (pass)
      TestUtil.logMsg("" + test + " ... PASSED");
    else
      TestUtil.logErr("" + test + " ... FAILED");

    return pass;
  }

  private boolean StringTest() {
    TestUtil.logMsg("StringTest");
    boolean pass = true;
    String values[] = JAXWS_Data.String_data;
    String response;
    TestUtil.logMsg("Passing/Returning data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoString(values[i]);

        if (values[i] == null && response == null) {
          continue;
        } else if (!response.equals(values[i])) {
          TestUtil.logErr("StringTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "StringTest");
    return pass;
  }

  private boolean IntegerTest() {
    TestUtil.logMsg("IntegerTest");
    boolean pass = true;
    BigInteger values[] = JAXWS_Data.BigInteger_data;
    BigInteger response;

    TestUtil.logMsg("Passing/Returning data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoInteger(values[i]);

        if (values[i] == null && response == null) {
          continue;
        }

        if (!response.equals(values[i])) {
          TestUtil.logErr("IntegerTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "IntegerTest");
    return pass;
  }

  private boolean IntTest() {
    TestUtil.logMsg("IntTest");
    boolean pass = true;
    int values[] = JAXWS_Data.int_data;
    int response;

    TestUtil.logMsg("Passing/Returning data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoInt(values[i]);

        if (response != values[i]) {
          TestUtil.logErr("IntTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "IntTest");
    return pass;
  }

  private boolean LongTest() {
    TestUtil.logMsg("LongTest");
    boolean pass = true;
    long values[] = JAXWS_Data.long_data;
    long response;

    TestUtil.logMsg("Passing/Returning data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoLong(values[i]);

        if (response != values[i]) {
          TestUtil.logErr("LongTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "LongTest");
    return pass;
  }

  private boolean ShortTest() {
    TestUtil.logMsg("ShortTest");
    boolean pass = true;
    short values[] = JAXWS_Data.short_data;
    short response;

    TestUtil.logMsg("Passing/Returning data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoShort(values[i]);

        if (response != values[i]) {
          TestUtil.logErr("ShortTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "ShortTest");
    return pass;
  }

  private boolean DecimalTest() {
    TestUtil.logMsg("DecimalTest");
    boolean pass = true;
    BigDecimal values[] = JAXWS_Data.BigDecimal_data;
    BigDecimal response;

    TestUtil.logMsg("Passing/Returning data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoDecimal(values[i]);

        if (values[i] == null && response == null) {
          continue;
        } else if (!response.equals(values[i])) {
          TestUtil.logErr("DecimalTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "DecimalTest");
    return pass;
  }

  private boolean FloatTest() {
    TestUtil.logMsg("FloatTest");
    boolean pass = true;
    float values[] = JAXWS_Data.float_data;
    float response;

    TestUtil.logMsg("Passing/Returning data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoFloat(values[i]);

        if (response != values[i]) {
          TestUtil.logErr("FloatTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "FloatTest");
    return pass;
  }

  private boolean DoubleTest() {
    TestUtil.logMsg("DoubleTest");
    boolean pass = true;
    double values[] = JAXWS_Data.double_data;
    double response;

    TestUtil.logMsg("Passing/Returning data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoDouble(values[i]);

        if (response != values[i]) {
          TestUtil.logErr("DoubleTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "DoubleTest");
    return pass;
  }

  private boolean BooleanTest() {
    TestUtil.logMsg("BooleanTest");
    boolean pass = true;
    boolean values[] = JAXWS_Data.boolean_data;
    boolean response;

    TestUtil.logMsg("Passing/Returning data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoBoolean(values[i]);

        if (!response == values[i]) {
          TestUtil.logErr("BooleanTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "BooleanTest");
    return pass;
  }

  private boolean ByteTest() {
    TestUtil.logMsg("ByteTest");
    boolean pass = true;
    byte values[] = JAXWS_Data.byte_data;
    byte response;

    TestUtil.logMsg("Passing/Returning data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoByte(values[i]);

        if (response != values[i]) {
          TestUtil.logErr("ByteTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "ByteTest");
    return pass;
  }

  private boolean QNameTest() {
    TestUtil.logMsg("QNameTest");
    boolean pass = true;
    QName values[] = JAXWS_Data.QName_data;
    QName response;

    TestUtil.logMsg("Passing/Returning data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoQName(values[i]);
        if (values[i] == null && response == null) {
          continue;
        } else if (!response.equals(values[i])) {
          TestUtil.logErr("QNameTest failed - expected " + values[i]
              + ",  received: " + response);
          pass = false;
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "QNameTest");
    return pass;
  }

  private boolean DateTimeTest() {
    TestUtil.logMsg("DateTimeTest");
    boolean pass = true;
    XMLGregorianCalendar values[] = JAXWS_Data.XMLGregorianCalendar_data;
    XMLGregorianCalendar response;

    TestUtil.logMsg("Passing/Returning data to/from JAXWS Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoDateTime(values[i]);
        if (!JAXWS_Data.compareValues(values[i], response,
            "XMLGregorianCalendar"))
          pass = false;

      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "DateTimeTest");
    return pass;
  }

  private boolean Base64BinaryTest() {
    TestUtil.logMsg("Base64BinaryTest");
    boolean pass = false;
    byte values[] = JAXWS_Data.byte_data;
    byte[] response;

    TestUtil.logMsg("Passing/Returning data to/from JAXWS Service");
    try {
      response = port.echoBase64Binary(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "byte");
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
    byte values[] = JAXWS_Data.byte_data;
    byte[] response;

    TestUtil.logMsg("Passing/Returning data to/from JAXWS Service");
    try {
      response = port.echoHexBinary(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "HexBinaryTest");
    return pass;
  }

  private boolean StringArrayTest() {
    TestUtil.logMsg("StringArrayTest");
    boolean pass = false;
    List<String> values = JAXWS_Data.list_String_nonull_data;
    List<String> response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXWS Service");
    try {
      response = port.echoStringArray(values);
      TestUtil.logMsg("Compare response with input ....");
      pass = JAXWS_Data.compareArrayValues(values, response, "String");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "StringArrayTest");
    return pass;
  }

  private boolean IntegerArrayTest() {
    TestUtil.logMsg("IntegerArrayTest");
    boolean pass = false;
    List<BigInteger> values = JAXWS_Data.list_BigInteger_nonull_data;
    List<BigInteger> response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXWS Service");
    try {
      response = port.echoIntegerArray(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "BigInteger");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "IntegerArrayTest");
    return pass;
  }

  private boolean IntArrayTest() {
    TestUtil.logMsg("IntArrayTest");
    boolean pass = false;
    List<Integer> values = JAXWS_Data.list_Integer_nonull_data;
    List<Integer> response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXWS Service");
    try {
      response = port.echoIntArray(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Integer");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "IntArrayTest");
    return pass;
  }

  private boolean LongArrayTest() {
    TestUtil.logMsg("LongArrayTest");
    boolean pass = false;
    List<Long> values = JAXWS_Data.list_Long_nonull_data;
    List<Long> response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXWS Service");
    try {
      response = port.echoLongArray(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Long");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "LongArrayTest");
    return pass;
  }

  private boolean ShortArrayTest() {
    TestUtil.logMsg("ShortArrayTest");
    boolean pass = false;
    List<Short> values = JAXWS_Data.list_Short_nonull_data;
    List<Short> response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXWS Service");
    try {
      response = port.echoShortArray(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Short");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "ShortArrayTest");
    return pass;
  }

  private boolean FloatArrayTest() {
    TestUtil.logMsg("FloatArrayTest");
    boolean pass = false;
    List<Float> values = JAXWS_Data.list_Float_nonull_data;
    List<Float> response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXWS Service");
    try {
      response = port.echoFloatArray(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Float");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "FloatArrayTest");
    return pass;
  }

  private boolean DoubleArrayTest() {
    TestUtil.logMsg("DoubleArrayTest");
    boolean pass = false;
    List<Double> values = JAXWS_Data.list_Double_nonull_data;
    List<Double> response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXWS Service");
    try {
      response = port.echoDoubleArray(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Double");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "DoubleArrayTest");
    return pass;
  }

  private boolean DecimalArrayTest() {
    TestUtil.logMsg("DecimalArrayTest");
    boolean pass = false;
    List<BigDecimal> values = JAXWS_Data.list_BigDecimal_nonull_data;
    List<BigDecimal> response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXWS Service");
    try {
      response = port.echoDecimalArray(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "BigDecimal");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "DecimalArrayTest");
    return pass;
  }

  private boolean BooleanArrayTest() {
    TestUtil.logMsg("BooleanArrayTest");
    boolean pass = false;
    List<Boolean> values = JAXWS_Data.list_Boolean_nonull_data;
    List<Boolean> response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXWS Service");
    try {
      response = port.echoBooleanArray(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "Boolean");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "BooleanArrayTest");
    return pass;
  }

  private boolean ByteArrayTest() {
    TestUtil.logMsg("ByteArrayTest");
    boolean pass = false;
    List<Byte> values = JAXWS_Data.list_Byte_nonull_data;
    List<Byte> response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXWS Service");
    try {
      response = port.echoByteArray(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "byte");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "ByteArrayTest");
    return pass;
  }

  private boolean QNameArrayTest() {
    TestUtil.logMsg("QNameArrayTest");
    boolean pass = false;
    List<QName> values = JAXWS_Data.list_QName_nonull_data;
    List<QName> response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXWS Service");
    try {
      response = port.echoQNameArray(values);
      pass = JAXWS_Data.compareArrayValues(values, response, "QName");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "QNameArrayTest");
    return pass;
  }

  private boolean DateTimeArrayTest() {
    TestUtil.logMsg("DateTimeArrayTest");
    boolean pass = false;
    List<XMLGregorianCalendar> values = JAXWS_Data.list_XMLGregorianCalendar_nonull_data;
    List<XMLGregorianCalendar> response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXWS Service");
    try {
      response = port.echoDateTimeArray(values);
      pass = JAXWS_Data.compareArrayValues(values, response,
          "XMLGregorianCalendar");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "DateTimeArrayTest");
    return pass;
  }
}
