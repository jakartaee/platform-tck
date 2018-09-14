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

package com.sun.ts.tests.webservices.ejb.marshalltest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.tests.jaxrpc.common.*;
import com.sun.javatest.Status;

import javax.xml.rpc.Service;
import javax.xml.namespace.QName;

import javax.naming.InitialContext;

import java.io.*;
import java.net.*;
import java.rmi.*;

import java.util.Properties;
import java.util.Calendar;

import java.math.BigInteger;
import java.math.BigDecimal;

public class Client extends EETest {
  MarshallTest port = null;

  private void getStub() throws Exception {
    TestUtil.logMsg("JNDI lookup for Service1");
    InitialContext ctx = new InitialContext();
    Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/ejbmarshalltest");
    TestUtil.logMsg("Get port from Service1");
    port = (MarshallTest) svc.getPort(MarshallTest.class);
    TestUtil.logMsg("Port obtained");
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
   * @class.setup_props: webServerHost; webServerPort;
   */

  public void setup(String[] args, Properties p) throws Fault {
    try {
      getStub();
    } catch (Exception e) {
      throw new Fault("setup failed:", e);
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
   * @assertion_ids: WS4EE:SPEC:43; WS4EE:SPEC:51; WS4EE:SPEC:145;
   * WS4EE:SPEC:39; WS4EE:SPEC:109; WS4EE:SPEC:184;
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
   * @assertion_ids: WS4EE:SPEC:43; WS4EE:SPEC:51; WS4EE:SPEC:145;
   * WS4EE:SPEC:39; WS4EE:SPEC:109; WS4EE:SPEC:184;
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
    /***
     * if ( !Base64BinaryArrayTest() ) pass = false; printSeperationLine(); if (
     * !HexBinaryArrayTest() ) pass = false; printSeperationLine();
     ***/

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
    String values[] = JAXRPC_Data.String_data;
    String response;
    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
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
    BigInteger values[] = JAXRPC_Data.BigInteger_data;
    BigInteger response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
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
    int values[] = JAXRPC_Data.int_data;
    int response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
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
    long values[] = JAXRPC_Data.long_data;
    long response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
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
    short values[] = JAXRPC_Data.short_data;
    short response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
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
    BigDecimal values[] = JAXRPC_Data.BigDecimal_data;
    BigDecimal response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
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
    float values[] = JAXRPC_Data.float_data;
    float response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
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
    double values[] = JAXRPC_Data.double_data;
    double response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
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
    boolean values[] = JAXRPC_Data.boolean_data;
    boolean response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
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
    byte values[] = JAXRPC_Data.byte_data;
    byte response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
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
    QName values[] = JAXRPC_Data.QName_data;
    QName response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
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
    Calendar values[] = JAXRPC_Data.GregorianCalendar_data;
    Calendar response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      for (int i = 0; i < values.length; i++) {
        response = port.echoDateTime(values[i]);

        if (!JAXRPC_Data.compareValues(values[i], response, "Calendar"))
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
    byte values[] = JAXRPC_Data.byte_data;
    byte[] response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      response = port.echoBase64Binary(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "byte");
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
    byte[] response;

    TestUtil.logMsg("Passing/Returning data to/from JAXRPC Service");
    try {
      response = port.echoHexBinary(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "byte");
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
    String values[] = JAXRPC_Data.String_data;
    String[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      JAXRPC_Data.dumpArrayValues(values, "String");
      response = port.echoStringArray(values);
      TestUtil.logMsg("Compare response with input ....");
      pass = JAXRPC_Data.compareArrayValues(values, response, "String");
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
    BigInteger values[] = JAXRPC_Data.BigInteger_data;
    BigInteger[] response;
    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoIntegerArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "BigInteger");
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
    int values[] = JAXRPC_Data.int_data;
    int[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoIntArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "int");
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
    long values[] = JAXRPC_Data.long_data;
    long[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoLongArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "long");
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
    short values[] = JAXRPC_Data.short_data;
    short[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoShortArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "short");
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
    float values[] = JAXRPC_Data.float_data;
    float[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoFloatArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "float");
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
    double values[] = JAXRPC_Data.double_data;
    double[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoDoubleArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "double");
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
    BigDecimal values[] = JAXRPC_Data.BigDecimal_data;
    BigDecimal[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoDecimalArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "BigDecimal");
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
    boolean values[] = JAXRPC_Data.boolean_data;
    boolean[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoBooleanArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "boolean");
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
    byte values[] = JAXRPC_Data.byte_data;
    byte[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoByteArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "byte");
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
    QName values[] = JAXRPC_Data.QName_data;
    QName[] response;

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoQNameArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "QName");
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
    Calendar values[] = JAXRPC_Data.GregorianCalendar_data;
    Calendar response[];

    TestUtil.logMsg("Passing/Returning array data to/from JAXRPC Service");
    try {
      response = port.echoDateTimeArray(values);
      pass = JAXRPC_Data.compareArrayValues(values, response, "Calendar");
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    printTestStatus(pass, "DateTimeArrayTest");
    return pass;
  }

  /***
   * private boolean Base64BinaryArrayTest() { TestUtil.logMsg(
   * "Base64BinaryArrayTest" ); boolean pass = false; byte values[][] =
   * JAXRPC_Data.byte_multi_data; byte[][] response;
   * 
   * TestUtil.logMsg( "Passing/Returning array data to/from JAXRPC Service");
   * try { response = port.echoBase64BinaryArray( values ); pass =
   * JAXRPC_Data.compareMultiArrayValues( values, response, "byte" ); } catch (
   * Exception e ) { TestUtil.logErr( "Caught exception: " + e.getMessage() );
   * TestUtil.printStackTrace(e); pass = false; }
   * 
   * printTestStatus( pass, "Base64BinaryArrayTest" ); return pass; }
   * 
   * private boolean HexBinaryArrayTest() { TestUtil.logMsg(
   * "HexBinaryArrayTest" ); boolean pass = false; byte values[][] =
   * JAXRPC_Data.byte_multi_data; byte[][] response;
   * 
   * TestUtil.logMsg( "Passing/Returning array data to/from JAXRPC Service");
   * try { response = port.echoHexBinaryArray( values ); pass =
   * JAXRPC_Data.compareMultiArrayValues( values, response, "byte" ); } catch (
   * Exception e ) { TestUtil.logErr( "Caught exception: " + e.getMessage() );
   * TestUtil.printStackTrace(e); pass = false; }
   * 
   * printTestStatus( pass, "HexBinaryArrayTest" ); return pass; }
   ***/
}
