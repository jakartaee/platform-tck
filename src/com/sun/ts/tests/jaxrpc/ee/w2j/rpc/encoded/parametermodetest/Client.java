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

package com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;

import javax.xml.rpc.*;

import java.util.Properties;
import java.util.GregorianCalendar;
import java.util.Calendar;

import java.math.BigInteger;
import java.math.BigDecimal;

import javax.xml.namespace.QName;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxrpc.common.*;

import com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.*;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.";

  private TSURL ctsurl = new TSURL();

  private Properties props = null;

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  String in1 = null, in1_tmp = null;

  BigInteger in2 = null, in2_tmp = null;

  int in3, in3_tmp;

  long in4, in4_tmp;

  short in5, in5_tmp;

  BigDecimal in6 = null, in6_tmp = null;

  float in7, in7_tmp;

  double in8, in8_tmp;

  boolean in9, in9_tmp;

  byte in10, in10_tmp;

  QName in11 = null, in11_tmp = null;

  Calendar in12 = null, in12_tmp = null;

  String in13 = null, in13_tmp = null;

  Boolean in14 = null, in14_tmp = null;

  Float in15 = null, in15_tmp = null;

  Double in16 = null, in16_tmp = null;

  BigDecimal in17 = null, in17_tmp = null;

  Integer in18 = null, in18_tmp = null;

  Short in19 = null, in19_tmp = null;

  Byte in20 = null, in20_tmp = null;

  byte[] in21, in21_tmp;

  byte[] in22, in22_tmp;

  byte[] in23, in23_tmp;

  javax.xml.rpc.holders.StringHolder out_inout1, out_inout1_tmp;

  javax.xml.rpc.holders.BigIntegerHolder out_inout2, out_inout2_tmp;

  javax.xml.rpc.holders.IntHolder out_inout3, out_inout3_tmp;

  javax.xml.rpc.holders.LongHolder out_inout4, out_inout4_tmp;

  javax.xml.rpc.holders.ShortHolder out_inout5, out_inout5_tmp;

  javax.xml.rpc.holders.BigDecimalHolder out_inout6, out_inout6_tmp;

  javax.xml.rpc.holders.FloatHolder out_inout7, out_inout7_tmp;

  javax.xml.rpc.holders.DoubleHolder out_inout8, out_inout8_tmp;

  javax.xml.rpc.holders.BooleanHolder out_inout9, out_inout9_tmp;

  javax.xml.rpc.holders.ByteHolder out_inout10, out_inout10_tmp;

  javax.xml.rpc.holders.QNameHolder out_inout11, out_inout11_tmp;

  javax.xml.rpc.holders.CalendarHolder out_inout12, out_inout12_tmp;

  javax.xml.rpc.holders.StringHolder out_inout13, out_inout13_tmp;

  javax.xml.rpc.holders.BooleanWrapperHolder out_inout14, out_inout14_tmp;

  javax.xml.rpc.holders.FloatWrapperHolder out_inout15, out_inout15_tmp;

  javax.xml.rpc.holders.DoubleWrapperHolder out_inout16, out_inout16_tmp;

  javax.xml.rpc.holders.BigDecimalHolder out_inout17, out_inout17_tmp;

  javax.xml.rpc.holders.IntegerWrapperHolder out_inout18, out_inout18_tmp;

  javax.xml.rpc.holders.ShortWrapperHolder out_inout19, out_inout19_tmp;

  javax.xml.rpc.holders.ByteWrapperHolder out_inout20, out_inout20_tmp;

  javax.xml.rpc.holders.ByteArrayHolder out_inout21, out_inout21_tmp;

  javax.xml.rpc.holders.ByteArrayHolder out_inout22, out_inout22_tmp;

  javax.xml.rpc.holders.ByteArrayHolder out_inout23, out_inout23_tmp;

  ArrayOfstringHolder out_inoutarray1, out_inoutarray1_tmp;

  ArrayOfintegerHolder out_inoutarray2, out_inoutarray2_tmp;

  ArrayOfintHolder out_inoutarray3, out_inoutarray3_tmp;

  ArrayOflongHolder out_inoutarray4, out_inoutarray4_tmp;

  ArrayOfshortHolder out_inoutarray5, out_inoutarray5_tmp;

  ArrayOfdecimalHolder out_inoutarray6, out_inoutarray6_tmp;

  ArrayOffloatHolder out_inoutarray7, out_inoutarray7_tmp;

  ArrayOfdoubleHolder out_inoutarray8, out_inoutarray8_tmp;

  ArrayOfbooleanHolder out_inoutarray9, out_inoutarray9_tmp;

  ArrayOfbyteHolder out_inoutarray10, out_inoutarray10_tmp;

  ArrayOfQNameHolder out_inoutarray11, out_inoutarray11_tmp;

  ArrayOfdateTimeHolder out_inoutarray12, out_inoutarray12_tmp;

  EnumString inEnum1, inEnum1_tmp;

  EnumInteger inEnum2, inEnum2_tmp;

  EnumInt inEnum3, inEnum3_tmp;

  EnumLong inEnum4, inEnum4_tmp;

  EnumShort inEnum5, inEnum5_tmp;

  EnumDecimal inEnum6, inEnum6_tmp;

  EnumFloat inEnum7, inEnum7_tmp;

  EnumDouble inEnum8, inEnum8_tmp;

  EnumByte inEnum9, inEnum9_tmp;

  EnumStringHolder out_inoutEnum1, out_inoutEnum1_tmp;

  EnumIntegerHolder out_inoutEnum2, out_inoutEnum2_tmp;

  EnumIntHolder out_inoutEnum3, out_inoutEnum3_tmp;

  EnumLongHolder out_inoutEnum4, out_inoutEnum4_tmp;

  EnumShortHolder out_inoutEnum5, out_inoutEnum5_tmp;

  EnumDecimalHolder out_inoutEnum6, out_inoutEnum6_tmp;

  EnumFloatHolder out_inoutEnum7, out_inoutEnum7_tmp;

  EnumDoubleHolder out_inoutEnum8, out_inoutEnum8_tmp;

  EnumByteHolder out_inoutEnum9, out_inoutEnum9_tmp;

  AllStruct inStruct = null;

  AllStruct inStruct_tmp = null;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "parametermodetest.endpoint.1";

  private static final String WSDLLOC_URL = "parametermodetest.wsdlloc.1";

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
  ParameterModeTest port = null;

  Stub stub = null;

  private void getStubStandalone() throws Exception {
    TestUtil.logMsg("Get stub from service implementation class"
        + " using JAXRPC porting instance");
    port = (ParameterModeTest) JAXRPC_Util.getStub(
        "com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded."
            + "parametermodetest.ParameterModeTestService",
        "getParameterModeTestPort");
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub = (javax.xml.rpc.Stub) port;
  }

  private void getStub() throws Exception {
    TestUtil.logMsg("JNDI lookup for Service1");
    InitialContext ctx = new InitialContext();
    TestUtil.logMsg("Lookup java:comp/env/service/w2jparametermodetest");
    Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/w2jparametermodetest");
    TestUtil.logMsg("Get port from Service1");
    port = (ParameterModeTest) svc.getPort(
        com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.ParameterModeTest.class);
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

  private boolean printTestStatus(boolean pass, String test) {
    if (pass)
      TestUtil.logMsg("" + test + " ... PASSED");
    else
      TestUtil.logErr("" + test + " ... FAILED");

    return pass;
  }

  private void initialize_in_SimpleType_data() {
    in1 = "String1";
    in2 = new BigInteger("3512359");
    in3 = (int) Integer.MIN_VALUE;
    in4 = (long) Long.MIN_VALUE;
    in5 = (short) Short.MIN_VALUE;
    in6 = new BigDecimal("3512359.1456");
    in7 = (float) Float.MIN_VALUE;
    in8 = (double) Double.MIN_VALUE;
    in9 = false;
    in10 = (byte) Byte.MIN_VALUE;
    in11 = new QName("String2");
    in12 = (Calendar) new GregorianCalendar(96, 5, 1);
    in13 = "String3";
    in14 = new Boolean(false);
    in15 = new Float(Float.MIN_VALUE);
    in16 = new Double(Double.MIN_VALUE);
    in17 = new BigDecimal("3512359.1111");
    in18 = new Integer(Integer.MIN_VALUE);
    in19 = new Short(Short.MIN_VALUE);
    in20 = new Byte(Byte.MIN_VALUE);
    in21 = JAXRPC_Data.byte_data;
    in22 = JAXRPC_Data.byte_data;
    in23 = JAXRPC_Data.byte_data;
  }

  private void initialize_out_inout_SimpleType_data() {
    out_inout1 = new javax.xml.rpc.holders.StringHolder("String1");
    out_inout2 = new javax.xml.rpc.holders.BigIntegerHolder(
        new BigInteger("3512359"));
    out_inout3 = new javax.xml.rpc.holders.IntHolder((int) Integer.MIN_VALUE);
    out_inout4 = new javax.xml.rpc.holders.LongHolder((long) Long.MIN_VALUE);
    out_inout5 = new javax.xml.rpc.holders.ShortHolder((short) Short.MIN_VALUE);
    out_inout6 = new javax.xml.rpc.holders.BigDecimalHolder(
        new BigDecimal("3512359.1456"));
    out_inout7 = new javax.xml.rpc.holders.FloatHolder((float) Float.MIN_VALUE);
    out_inout8 = new javax.xml.rpc.holders.DoubleHolder(
        (double) Double.MIN_VALUE);
    out_inout9 = new javax.xml.rpc.holders.BooleanHolder(false);
    out_inout10 = new javax.xml.rpc.holders.ByteHolder((byte) Byte.MIN_VALUE);
    out_inout11 = new javax.xml.rpc.holders.QNameHolder(new QName("String2"));
    out_inout12 = new javax.xml.rpc.holders.CalendarHolder(
        (Calendar) new GregorianCalendar(96, 5, 1));
    out_inout13 = new javax.xml.rpc.holders.StringHolder("String3");
    out_inout14 = new javax.xml.rpc.holders.BooleanWrapperHolder(
        new Boolean(false));
    out_inout15 = new javax.xml.rpc.holders.FloatWrapperHolder(
        new Float(Float.MIN_VALUE));
    out_inout16 = new javax.xml.rpc.holders.DoubleWrapperHolder(
        new Double(Double.MIN_VALUE));
    out_inout17 = new javax.xml.rpc.holders.BigDecimalHolder(
        new BigDecimal("3512359.1111"));
    out_inout18 = new javax.xml.rpc.holders.IntegerWrapperHolder(
        new Integer(Integer.MIN_VALUE));
    out_inout19 = new javax.xml.rpc.holders.ShortWrapperHolder(
        new Short(Short.MIN_VALUE));
    out_inout20 = new javax.xml.rpc.holders.ByteWrapperHolder(
        new Byte(Byte.MIN_VALUE));
    out_inout21 = new javax.xml.rpc.holders.ByteArrayHolder(
        JAXRPC_Data.byte_data);
    out_inout22 = new javax.xml.rpc.holders.ByteArrayHolder(
        JAXRPC_Data.byte_data);
    out_inout23 = new javax.xml.rpc.holders.ByteArrayHolder(
        JAXRPC_Data.byte_data);
  }

  private void initialize_out_inout_SimpleTypeArray_data() {
    out_inoutarray1 = new ArrayOfstringHolder(JAXRPC_Data.String_nonull_data);
    out_inoutarray2 = new ArrayOfintegerHolder(
        JAXRPC_Data.BigInteger_nonull_data);
    out_inoutarray3 = new ArrayOfintHolder(JAXRPC_Data.int_data);
    out_inoutarray4 = new ArrayOflongHolder(JAXRPC_Data.long_data);
    out_inoutarray5 = new ArrayOfshortHolder(JAXRPC_Data.short_data);
    out_inoutarray6 = new ArrayOfdecimalHolder(
        JAXRPC_Data.BigDecimal_nonull_data);
    out_inoutarray7 = new ArrayOffloatHolder(JAXRPC_Data.float_data);
    out_inoutarray8 = new ArrayOfdoubleHolder(JAXRPC_Data.double_data);
    out_inoutarray9 = new ArrayOfbooleanHolder(JAXRPC_Data.boolean_data);
    out_inoutarray10 = new ArrayOfbyteHolder(JAXRPC_Data.byte_data);
    out_inoutarray11 = new ArrayOfQNameHolder(JAXRPC_Data.QName_nonull_data);
    out_inoutarray12 = new ArrayOfdateTimeHolder(
        JAXRPC_Data.GregorianCalendar_nonull_data);
  }

  private void initialize_inEnum_data() {
    try {
      inEnum1 = EnumString.fromValue("String1");
      inEnum2 = EnumInteger.fromValue(new BigInteger("3512359"));
      inEnum3 = EnumInt.fromValue(Integer.MIN_VALUE);
      inEnum4 = EnumLong.fromValue(Long.MIN_VALUE);
      inEnum5 = EnumShort.fromValue(Short.MIN_VALUE);
      inEnum6 = EnumDecimal.fromValue(new BigDecimal("3512359.1456"));
      inEnum7 = EnumFloat.fromString("-1.00000000");
      inEnum8 = EnumDouble.fromString("-1.0000000000000");
      inEnum9 = EnumByte.fromValue(Byte.MIN_VALUE);
    } catch (IllegalArgumentException e) {
      TestUtil.logErr("Couldn't initialize enumeration data", e);
      throw e;
    }
  }

  private void initialize_out_inoutEnum_data() {
    try {
      out_inoutEnum1 = new EnumStringHolder(EnumString.fromString("String1"));
      out_inoutEnum2 = new EnumIntegerHolder(
          EnumInteger.fromValue(new BigInteger("3512359")));
      out_inoutEnum3 = new EnumIntHolder(EnumInt.fromValue(Integer.MIN_VALUE));
      out_inoutEnum4 = new EnumLongHolder(EnumLong.fromValue(Long.MIN_VALUE));
      out_inoutEnum5 = new EnumShortHolder(
          EnumShort.fromValue(Short.MIN_VALUE));
      out_inoutEnum6 = new EnumDecimalHolder(
          EnumDecimal.fromValue(new BigDecimal("3512359.1456")));
      out_inoutEnum7 = new EnumFloatHolder(EnumFloat.fromString("-1.00000000"));
      out_inoutEnum8 = new EnumDoubleHolder(
          EnumDouble.fromString("-1.0000000000000"));
      out_inoutEnum9 = new EnumByteHolder(EnumByte.fromValue(Byte.MIN_VALUE));
      /*
       * out_inoutEnum1=new EnumStringHolder(EnumString.fromString("String1"));
       * out_inoutEnum2.value=EnumInteger.fromValue(new BigInteger("3512359"));
       * out_inoutEnum3.value=EnumInt.fromValue(Integer.MIN_VALUE);
       * out_inoutEnum4.value=EnumLong.fromValue(Long.MIN_VALUE);
       * out_inoutEnum5.value=EnumShort.fromValue(Short.MIN_VALUE);
       * out_inoutEnum6.value=EnumDecimal.fromValue(new
       * BigDecimal("3512359.1456") );
       * out_inoutEnum7.value=EnumFloat.fromString("-1.00000000");
       * out_inoutEnum8.value=EnumDouble.fromString("-1.0000000000000");
       * out_inoutEnum9.value=EnumByte.fromValue(Byte.MIN_VALUE);
       */
    } catch (IllegalArgumentException e) {
      TestUtil.logErr("Couldn't initialize enumeration data", e);
      throw e;
    }
  }

  private void initialize_in_Struct_data() {
    inStruct = new AllStruct();
    inStruct.setVarString(new String("String1"));
    inStruct.setVarInteger(new BigInteger("3512359"));
    inStruct.setVarInt((int) Integer.MIN_VALUE);
    inStruct.setVarLong((long) Long.MIN_VALUE);
    inStruct.setVarShort((short) Short.MIN_VALUE);
    inStruct.setVarDecimal(new BigDecimal("3512359.1456"));
    inStruct.setVarFloat((float) Float.MIN_VALUE);
    inStruct.setVarDouble((double) Double.MIN_VALUE);
    inStruct.setVarBoolean(false);
    inStruct.setVarByte((byte) Byte.MIN_VALUE);
    inStruct.setVarQName(new QName("String2"));
    inStruct.setVarDateTime((Calendar) new GregorianCalendar(96, 5, 1));
    inStruct.setVarSoapString("String3");
    inStruct.setVarSoapBoolean(new Boolean(false));
    inStruct.setVarSoapFloat(new Float(Float.MIN_VALUE));
    inStruct.setVarSoapDouble(new Double(Double.MIN_VALUE));
    inStruct.setVarSoapDecimal(new BigDecimal("3512359.1111"));
    inStruct.setVarSoapInt(new Integer(Integer.MIN_VALUE));
    inStruct.setVarSoapShort(new Short(Short.MIN_VALUE));
    inStruct.setVarSoapByte(new Byte(Byte.MIN_VALUE));
    inStruct.setVarBase64Binary(JAXRPC_Data.byte_data);
    inStruct.setVarHexBinary(JAXRPC_Data.byte_data);
    inStruct.setVarSoapBase64(JAXRPC_Data.byte_data);
  }

  private void initialize_saved_in_SimpleType_data() {
    in1_tmp = "String4";
    in2_tmp = new BigInteger("3512360");
    in3_tmp = (int) Integer.MAX_VALUE;
    in4_tmp = (long) Long.MAX_VALUE;
    in5_tmp = (short) Short.MAX_VALUE;
    in6_tmp = new BigDecimal("3512360.1456");
    in7_tmp = (float) Float.MAX_VALUE;
    in8_tmp = (double) Double.MAX_VALUE;
    in9_tmp = true;
    in10_tmp = (byte) Byte.MAX_VALUE;
    in11_tmp = new QName("String5");
    in12_tmp = (Calendar) new GregorianCalendar(96, 5, 2);
    in13_tmp = "String6";
    in14_tmp = new Boolean(true);
    in15_tmp = new Float(Float.MAX_VALUE);
    in16_tmp = new Double(Double.MAX_VALUE);
    in17_tmp = new BigDecimal("3512360.1111");
    in18_tmp = new Integer(Integer.MAX_VALUE);
    in19_tmp = new Short(Short.MAX_VALUE);
    in20_tmp = new Byte(Byte.MAX_VALUE);
    in21_tmp = JAXRPC_Data.byte_data2;
    in22_tmp = JAXRPC_Data.byte_data2;
    in23_tmp = JAXRPC_Data.byte_data2;
  }

  private void initialize_saved_out_inout_SimpleType_data() {
    out_inout1_tmp = new javax.xml.rpc.holders.StringHolder("String4");
    out_inout2_tmp = new javax.xml.rpc.holders.BigIntegerHolder(
        new BigInteger("3512360"));
    out_inout3_tmp = new javax.xml.rpc.holders.IntHolder(
        (int) Integer.MAX_VALUE);
    out_inout4_tmp = new javax.xml.rpc.holders.LongHolder(
        (long) Long.MAX_VALUE);
    out_inout5_tmp = new javax.xml.rpc.holders.ShortHolder(
        (short) Short.MAX_VALUE);
    out_inout6_tmp = new javax.xml.rpc.holders.BigDecimalHolder(
        new BigDecimal("3512360.1456"));
    out_inout7_tmp = new javax.xml.rpc.holders.FloatHolder(
        (float) Float.MAX_VALUE);
    out_inout8_tmp = new javax.xml.rpc.holders.DoubleHolder(
        (double) Double.MAX_VALUE);
    out_inout9_tmp = new javax.xml.rpc.holders.BooleanHolder(true);
    out_inout10_tmp = new javax.xml.rpc.holders.ByteHolder(
        (byte) Byte.MAX_VALUE);
    out_inout11_tmp = new javax.xml.rpc.holders.QNameHolder(
        new QName("String5"));
    out_inout12_tmp = new javax.xml.rpc.holders.CalendarHolder(
        (Calendar) new GregorianCalendar(96, 5, 2));
    out_inout13_tmp = new javax.xml.rpc.holders.StringHolder("String6");
    out_inout14_tmp = new javax.xml.rpc.holders.BooleanWrapperHolder(
        new Boolean(true));
    out_inout15_tmp = new javax.xml.rpc.holders.FloatWrapperHolder(
        new Float(Float.MAX_VALUE));
    out_inout16_tmp = new javax.xml.rpc.holders.DoubleWrapperHolder(
        new Double(Double.MAX_VALUE));
    out_inout17_tmp = new javax.xml.rpc.holders.BigDecimalHolder(
        new BigDecimal("3512360.1111"));
    out_inout18_tmp = new javax.xml.rpc.holders.IntegerWrapperHolder(
        new Integer(Integer.MAX_VALUE));
    out_inout19_tmp = new javax.xml.rpc.holders.ShortWrapperHolder(
        new Short(Short.MAX_VALUE));
    out_inout20_tmp = new javax.xml.rpc.holders.ByteWrapperHolder(
        new Byte(Byte.MAX_VALUE));
    out_inout21_tmp = new javax.xml.rpc.holders.ByteArrayHolder(
        JAXRPC_Data.byte_data2);
    out_inout22_tmp = new javax.xml.rpc.holders.ByteArrayHolder(
        JAXRPC_Data.byte_data2);
    out_inout23_tmp = new javax.xml.rpc.holders.ByteArrayHolder(
        JAXRPC_Data.byte_data2);

  }

  private void initialize_saved_inEnum_data() {
    try {
      inEnum1_tmp = EnumString.fromValue("String4");
      inEnum2_tmp = EnumInteger.fromValue(new BigInteger("3512360"));
      inEnum3_tmp = EnumInt.fromValue(Integer.MAX_VALUE);
      inEnum4_tmp = EnumLong.fromValue(Long.MAX_VALUE);
      inEnum5_tmp = EnumShort.fromValue(Short.MAX_VALUE);
      inEnum6_tmp = EnumDecimal.fromValue(new BigDecimal("3512360.1456"));
      inEnum7_tmp = EnumFloat.fromString("3.00000000");
      inEnum8_tmp = EnumDouble.fromString("3.0000000000000");
      inEnum9_tmp = EnumByte.fromValue(Byte.MAX_VALUE);
    } catch (IllegalArgumentException e) {
      TestUtil.logErr("Couldn't initialize enumeration data", e);
      throw e;
    }
  }

  private void initialize_saved_out_inoutEnum_data() {
    try {
      out_inoutEnum1_tmp = new EnumStringHolder(
          EnumString.fromValue("String2"));
      out_inoutEnum2_tmp = new EnumIntegerHolder(
          EnumInteger.fromValue(new BigInteger("3512360")));
      out_inoutEnum3_tmp = new EnumIntHolder(
          EnumInt.fromValue(Integer.MAX_VALUE));
      out_inoutEnum4_tmp = new EnumLongHolder(
          EnumLong.fromValue(Long.MAX_VALUE));
      out_inoutEnum5_tmp = new EnumShortHolder(
          EnumShort.fromValue(Short.MAX_VALUE));
      out_inoutEnum6_tmp = new EnumDecimalHolder(
          EnumDecimal.fromValue(new BigDecimal("3512360.1456")));
      out_inoutEnum7_tmp = new EnumFloatHolder(
          EnumFloat.fromString("3.00000000"));
      out_inoutEnum8_tmp = new EnumDoubleHolder(
          EnumDouble.fromString("3.0000000000000"));
      out_inoutEnum9_tmp = new EnumByteHolder(
          EnumByte.fromValue(Byte.MAX_VALUE));
    } catch (IllegalArgumentException e) {
      TestUtil.logErr("Couldn't initialize enumeration data", e);
      throw e;
    }
  }

  private void initialize_saved_in_Struct_data() {
    inStruct = new AllStruct();
    inStruct.setVarString("String4");
    inStruct.setVarInteger(new BigInteger("3512360"));
    inStruct.setVarInt((int) Integer.MAX_VALUE);
    inStruct.setVarLong((long) Long.MAX_VALUE);
    inStruct.setVarShort((short) Short.MAX_VALUE);
    inStruct.setVarDecimal(new BigDecimal("3512360.1456"));
    inStruct.setVarFloat((float) Float.MAX_VALUE);
    inStruct.setVarDouble((double) Double.MAX_VALUE);
    inStruct.setVarBoolean(true);
    inStruct.setVarByte((byte) Byte.MAX_VALUE);
    inStruct.setVarQName(new QName("String5"));
    inStruct.setVarDateTime((Calendar) new GregorianCalendar(96, 5, 2));
    inStruct.setVarSoapString("String6");
    inStruct.setVarSoapBoolean(new Boolean(true));
    inStruct.setVarSoapFloat(new Float(Float.MAX_VALUE));
    inStruct.setVarSoapDouble(new Double(Double.MAX_VALUE));
    inStruct.setVarSoapDecimal(new BigDecimal("3512360.1111"));
    inStruct.setVarSoapInt(new Integer(Integer.MAX_VALUE));
    inStruct.setVarSoapShort(new Short(Short.MAX_VALUE));
    inStruct.setVarSoapByte(new Byte(Byte.MAX_VALUE));
    inStruct.setVarBase64Binary(JAXRPC_Data.byte_data2);
    inStruct.setVarHexBinary(JAXRPC_Data.byte_data2);
    inStruct.setVarSoapBase64(JAXRPC_Data.byte_data2);
  }

  private void save_in_SimpleType_data() {
    in1_tmp = in1;
    in2_tmp = in2;
    in3_tmp = in3;
    in4_tmp = in4;
    in5_tmp = in5;
    in6_tmp = in6;
    in7_tmp = in7;
    in8_tmp = in8;
    in9_tmp = in9;
    in10_tmp = in10;
    in11_tmp = in11;
    in12_tmp = in12;
    in13_tmp = in13;
    in14_tmp = in14;
    in15_tmp = in15;
    in16_tmp = in16;
    in17_tmp = in17;
    in18_tmp = in18;
    in19_tmp = in19;
    in20_tmp = in20;
    in21_tmp = in21;
    in22_tmp = in22;
    in23_tmp = in23;
  }

  private void save_out_inout_SimpleType_data() {
    out_inout1_tmp = out_inout1;
    out_inout2_tmp = out_inout2;
    out_inout3_tmp = out_inout3;
    out_inout4_tmp = out_inout4;
    out_inout5_tmp = out_inout5;
    out_inout6_tmp = out_inout6;
    out_inout7_tmp = out_inout7;
    out_inout8_tmp = out_inout8;
    out_inout9_tmp = out_inout9;
    out_inout10_tmp = out_inout10;
    out_inout11_tmp = out_inout11;
    out_inout12_tmp = out_inout12;
    out_inout13_tmp = out_inout13;
    out_inout14_tmp = out_inout14;
    out_inout15_tmp = out_inout15;
    out_inout16_tmp = out_inout16;
    out_inout17_tmp = out_inout17;
    out_inout18_tmp = out_inout18;
    out_inout19_tmp = out_inout19;
    out_inout20_tmp = out_inout20;
    out_inout21_tmp = out_inout21;
    out_inout22_tmp = out_inout22;
    out_inout23_tmp = out_inout23;
  }

  private void save_inEnum_data() {
    inEnum1_tmp = inEnum1;
    inEnum2_tmp = inEnum2;
    inEnum3_tmp = inEnum3;
    inEnum4_tmp = inEnum4;
    inEnum5_tmp = inEnum5;
    inEnum6_tmp = inEnum6;
    inEnum7_tmp = inEnum7;
    inEnum8_tmp = inEnum8;
    inEnum9_tmp = inEnum9;
  }

  private void save_in_Struct_data() {
    boolean bool;
    inStruct_tmp = new AllStruct();
    inStruct_tmp.setVarString(inStruct.getVarString());
    inStruct_tmp.setVarInteger(inStruct.getVarInteger());
    inStruct_tmp.setVarInt(inStruct.getVarInt());
    inStruct_tmp.setVarLong(inStruct.getVarLong());
    inStruct_tmp.setVarShort(inStruct.getVarShort());
    inStruct_tmp.setVarDecimal(inStruct.getVarDecimal());
    inStruct_tmp.setVarFloat(inStruct.getVarFloat());
    inStruct_tmp.setVarDouble(inStruct.getVarDouble());
    inStruct_tmp.setVarBoolean(inStruct.isVarBoolean());
    inStruct_tmp.setVarByte(inStruct.getVarByte());
    inStruct_tmp.setVarQName(inStruct.getVarQName());
    inStruct_tmp.setVarDateTime(inStruct.getVarDateTime());
    inStruct_tmp.setVarSoapString(inStruct.getVarSoapString());
    inStruct_tmp.setVarSoapBoolean(inStruct.getVarSoapBoolean());
    inStruct_tmp.setVarSoapFloat(inStruct.getVarSoapFloat());
    inStruct_tmp.setVarSoapDouble(inStruct.getVarSoapDouble());
    inStruct_tmp.setVarSoapDecimal(inStruct.getVarSoapDecimal());
    inStruct_tmp.setVarSoapInt(inStruct.getVarSoapInt());
    inStruct_tmp.setVarSoapShort(inStruct.getVarSoapShort());
    inStruct_tmp.setVarSoapByte(inStruct.getVarSoapByte());
    inStruct_tmp.setVarBase64Binary(inStruct.getVarBase64Binary());
    inStruct_tmp.setVarHexBinary(inStruct.getVarHexBinary());
    inStruct_tmp.setVarSoapBase64(inStruct.getVarSoapBase64());
  }

  private boolean compare_in_SimpleType_data() {
    TestUtil.logMsg("Comparing IN data");

    boolean pass = true;
    if (!in1.equals(in1_tmp)) {
      TestUtil.logErr("compare_data failed for String- expected " + in1_tmp
          + ",  received: " + in1);
      pass = false;
    }
    if (!in2.equals(in2_tmp)) {
      TestUtil.logErr("compare_data failed for BigInteger - expected " + in2_tmp
          + ",  received: " + in2);
      pass = false;
    }
    if (in3 != in3_tmp) {
      TestUtil.logErr("compare_data failed for int - expected " + in3_tmp
          + ",  received: " + in3);
      pass = false;
    }
    if (in4 != in4_tmp) {
      TestUtil.logErr("compare_data failed for long - expected " + in4_tmp
          + ",  received: " + in4);
      pass = false;
    }
    if (in5 != in5_tmp) {
      TestUtil.logErr("compare_data failed for short - expected " + in5_tmp
          + ",  received: " + in5);
      pass = false;
    }
    if (!in6.equals(in6_tmp)) {
      TestUtil.logErr("compare_data failed for BigDecimal - expected " + in6_tmp
          + ",  received: " + in6);
      pass = false;
    }
    if (in7 != in7_tmp) {
      TestUtil.logErr("compare_data failed for float - expected " + in7_tmp
          + ",  received: " + in7);
      pass = false;
    }
    if (in8 != in8_tmp) {
      TestUtil.logErr("compare_data failed for double - expected " + in8_tmp
          + ",  received: " + in8);
      pass = false;
    }
    if (in9 != in9_tmp) {
      TestUtil.logErr("compare_data failed for boolean - expected " + in9_tmp
          + ",  received: " + in9);
      pass = false;
    }
    if (in10 != in10_tmp) {
      TestUtil.logErr("compare_data failed for byte - expected " + in10_tmp
          + ",  received: " + in10);
      pass = false;
    }
    if (!in11.equals(in11_tmp)) {
      TestUtil.logErr("compare_data failed for QName - expected " + in11_tmp
          + ",  received: " + in11);
      pass = false;
    }
    if (!JAXRPC_Data.compareCalendars(in12, in12_tmp)) {
      TestUtil.logErr("compare_data failed for Calendar - expected " + in12_tmp
          + ",  received: " + in12);
      pass = false;
    }
    if (!in13.equals(in13_tmp)) {
      TestUtil.logErr("compare_data failed for Soap String - expected "
          + in13_tmp + ",  received: " + in13);
      pass = false;
    }
    if (!in14.equals(in14_tmp)) {
      TestUtil.logErr("compare_data failed for Soap Boolean - expected "
          + in14_tmp + ",  received: " + in14);
      pass = false;
    }
    if (!in15.equals(in15_tmp)) {
      TestUtil.logErr("compare_data failed for Soap Float - expected "
          + in15_tmp + ",  received: " + in15);
      pass = false;
    }
    if (!in16.equals(in16_tmp)) {
      TestUtil.logErr("compare_data failed for Soap Double - expected "
          + in16_tmp + ",  received: " + in16);
      pass = false;
    }
    if (!in17.equals(in17_tmp)) {
      TestUtil.logErr("compare_data failed for Soap Decimal - expected "
          + in17_tmp + ",  received: " + in17);
      pass = false;
    }
    if (!in18.equals(in18_tmp)) {
      TestUtil.logErr("compare_data failed Soap Int- expected " + in18_tmp
          + ",  received: " + in18);
      pass = false;
    }
    if (!in19.equals(in19_tmp)) {
      TestUtil.logErr("compare_data failed Soap Short - expected " + in19_tmp
          + ",  received: " + in19);
      pass = false;
    }
    if (!in20.equals(in20_tmp)) {
      TestUtil.logErr("compare_data failed Soap Byte - expected " + in20_tmp
          + ",  received: " + in20);
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(in21, in21_tmp, "byte")) {
      TestUtil.logErr("compare_data failed for base64binary - expected "
          + in21_tmp + ",  received: " + in21);
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(in22, in22_tmp, "byte")) {
      TestUtil.logErr("compare_data failed for hexbinary - expected " + in22_tmp
          + ",  received: " + in22);
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(in23, in23_tmp, "byte")) {
      TestUtil.logErr("compare_data failed for soap base64 - expected "
          + in23_tmp + ",  received: " + in23);
      pass = false;
    }

    return pass;
  }

  private boolean compare_out_inout_data() {
    TestUtil.logMsg("Comparing IN/INOUT data");
    boolean pass = true;
    if (!out_inout1.value.equals(out_inout1_tmp.value)) {
      TestUtil.logErr("compare_data failed for StringHolder - expected "
          + out_inout1_tmp.value + ",  received: " + out_inout1.value);
      pass = false;
    }
    if (!out_inout2.value.equals(out_inout2_tmp.value)) {
      TestUtil.logErr("compare_data failed for BigIntegerHolder - expected "
          + out_inout2_tmp.value + ",  received: " + out_inout2.value);
      pass = false;
    }
    if (out_inout3.value != out_inout3_tmp.value) {
      TestUtil.logErr("compare_data failed for IntHolder - expected "
          + out_inout3_tmp.value + ",  received: " + out_inout3.value);
      pass = false;
    }
    if (out_inout4.value != out_inout4_tmp.value) {
      TestUtil.logErr("compare_data failed for LongHolder - expected "
          + out_inout4_tmp.value + ",  received: " + out_inout4.value);
      pass = false;
    }
    if (out_inout5.value != out_inout5_tmp.value) {
      TestUtil.logErr("compare_data failed for ShortHolder - expected "
          + out_inout5_tmp.value + ",  received: " + out_inout5.value);
      pass = false;
    }
    if (!out_inout6.value.equals(out_inout6_tmp.value)) {
      TestUtil.logErr("compare_data failed for BigDecimalHolder - expected "
          + out_inout6_tmp.value + ",  received: " + out_inout6.value);
      pass = false;
    }
    if (out_inout7.value != out_inout7_tmp.value) {
      TestUtil.logErr("compare_data failed for FloatHolder - expected "
          + out_inout7_tmp.value + ",  received: " + out_inout7.value);
      pass = false;
    }
    if (out_inout8.value != out_inout8_tmp.value) {
      TestUtil.logErr("compare_data failed for DoubleHolder - expected "
          + out_inout8_tmp.value + ",  received: " + out_inout8.value);
      pass = false;
    }
    if (out_inout9.value != out_inout9_tmp.value) {
      TestUtil.logErr("compare_data failed for BooleanHolder - expected "
          + out_inout9_tmp.value + ",  received: " + out_inout9.value);
      pass = false;
    }
    if (out_inout10.value != out_inout10_tmp.value) {
      TestUtil.logErr("compare_data failed for ByteHolder - expected "
          + out_inout10_tmp.value + ",  received: " + out_inout10.value);
      pass = false;
    }
    if (!out_inout11.value.equals(out_inout11_tmp.value)) {
      TestUtil.logErr("compare_data failed for QNameHolder - expected "
          + out_inout11_tmp.value + ",  received: " + out_inout11.value);
      pass = false;
    }
    if (!JAXRPC_Data.compareCalendars(out_inout12.value,
        out_inout12_tmp.value)) {
      TestUtil.logErr("compare_data failed for CalendarHolder - expected "
          + out_inout12_tmp.value + ",  received: " + out_inout12.value);
      pass = false;
    }
    if (!out_inout13.value.equals(out_inout13_tmp.value)) {
      TestUtil.logErr("compare_data failed for StringHolder - expected "
          + out_inout13_tmp.value + ",  received: " + out_inout13.value);
      pass = false;
    }
    if (!out_inout14.value.equals(out_inout14_tmp.value)) {
      TestUtil.logErr("compare_data failed for BooleanWrapperHolder - expected "
          + out_inout14_tmp.value + ",  received: " + out_inout14.value);
      pass = false;
    }
    if (!out_inout15.value.equals(out_inout15_tmp.value)) {
      TestUtil.logErr("compare_data failed for FloatWrapperHolder - expected "
          + out_inout15_tmp.value + ",  received: " + out_inout15.value);
      pass = false;
    }
    if (!out_inout16.value.equals(out_inout16_tmp.value)) {
      TestUtil.logErr("compare_data failed for DoubleWrapperHolder - expected "
          + out_inout16_tmp.value + ",  received: " + out_inout16.value);
      pass = false;
    }
    if (!out_inout17.value.equals(out_inout17_tmp.value)) {
      TestUtil.logErr("compare_data failed for BigDecimalHolder - expected "
          + out_inout17_tmp.value + ",  received: " + out_inout17.value);
      pass = false;
    }
    if (!out_inout18.value.equals(out_inout18_tmp.value)) {
      TestUtil.logErr("compare_data failed for IntegerWrapperHolder - expected "
          + out_inout18_tmp.value + ",  received: " + out_inout18.value);
      pass = false;
    }
    if (!out_inout19.value.equals(out_inout19_tmp.value)) {
      TestUtil.logErr("compare_data failed for ShortWrapperHolder - expected "
          + out_inout19_tmp.value + ",  received: " + out_inout19.value);
      pass = false;
    }
    if (!out_inout20.value.equals(out_inout20_tmp.value)) {
      TestUtil.logErr("compare_data failed for ByteWrapperHolder - expected "
          + out_inout20_tmp.value + ",  received: " + out_inout20.value);
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inout21.value,
        out_inout21_tmp.value, "byte")) {
      TestUtil
          .logErr("compare_data failed for ByteArrayWrapperHolder - expected ");
      JAXRPC_Data.dumpArrayValues(out_inout21_tmp.value, "byte");
      TestUtil.logErr(" received: ");
      JAXRPC_Data.dumpArrayValues(out_inout21.value, "byte");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inout22.value,
        out_inout22_tmp.value, "byte")) {
      TestUtil
          .logErr("compare_data failed for ByteArrayWrapperHolder - expected ");
      JAXRPC_Data.dumpArrayValues(out_inout22_tmp.value, "byte");
      TestUtil.logErr(" received: ");
      JAXRPC_Data.dumpArrayValues(out_inout22.value, "byte");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inout23.value,
        out_inout23_tmp.value, "byte")) {
      TestUtil
          .logErr("compare_data failed for ByteArrayWrapperHolder - expected ");
      JAXRPC_Data.dumpArrayValues(out_inout23_tmp.value, "byte");
      TestUtil.logErr(" received: ");
      JAXRPC_Data.dumpArrayValues(out_inout23.value, "byte");
      pass = false;
    }

    return pass;
  }

  private boolean compare_inEnum_data() {
    TestUtil.logMsg("Comparing IN enumeration data");

    boolean pass = true;
    if (!inEnum1.equals(inEnum1_tmp)) {
      TestUtil.logErr("compare_data failed for String- expected " + inEnum1_tmp
          + ",  received: " + inEnum1);
      pass = false;
    }
    if (!inEnum2.equals(inEnum2_tmp)) {
      TestUtil.logErr("compare_data failed for BigInteger - expected "
          + inEnum2_tmp + ",  received: " + inEnum2);
      pass = false;
    }
    if (inEnum3 != inEnum3_tmp) {
      TestUtil.logErr("compare_data failed for int - expected " + inEnum3_tmp
          + ",  received: " + inEnum3);
      pass = false;
    }
    if (inEnum4 != inEnum4_tmp) {
      TestUtil.logErr("compare_data failed for long - expected " + inEnum4_tmp
          + ",  received: " + inEnum4);
      pass = false;
    }
    if (inEnum5 != inEnum5_tmp) {
      TestUtil.logErr("compare_data failed for short - expected " + inEnum5_tmp
          + ",  received: " + inEnum5);
      pass = false;
    }
    if (!inEnum6.equals(inEnum6_tmp)) {
      TestUtil.logErr("compare_data failed for BigDecimal - expected "
          + inEnum6_tmp + ",  received: " + inEnum6);
      pass = false;
    }
    if (inEnum7 != inEnum7_tmp) {
      TestUtil.logErr("compare_data failed for float - expected " + inEnum7_tmp
          + ",  received: " + inEnum7);
      pass = false;
    }
    if (inEnum8 != inEnum8_tmp) {
      TestUtil.logErr("compare_data failed for double - expected " + inEnum8_tmp
          + ",  received: " + inEnum8);
      pass = false;
    }
    if (inEnum9 != inEnum9_tmp) {
      TestUtil.logErr("compare_data failed for byte - expected " + inEnum9_tmp
          + ",  received: " + inEnum9);
      pass = false;
    }
    return pass;
  }

  private boolean compare_out_inoutEnum_data() {
    TestUtil.logMsg("Comparing IN enumeration data");

    boolean pass = true;
    if (!out_inoutEnum1.value.equals(out_inoutEnum1_tmp.value)) {
      TestUtil.logErr("compare_data failed for String- expected "
          + out_inoutEnum1_tmp.value + ",  received: " + out_inoutEnum1.value);
      pass = false;
    }
    if (!out_inoutEnum2.value.equals(out_inoutEnum2_tmp.value)) {
      TestUtil.logErr("compare_data failed for BigInteger - expected "
          + out_inoutEnum2_tmp.value + ",  received: " + out_inoutEnum2.value);
      pass = false;
    }
    if (out_inoutEnum3.value != out_inoutEnum3_tmp.value) {
      TestUtil.logErr("compare_data failed for int - expected "
          + out_inoutEnum3_tmp.value + ",  received: " + out_inoutEnum3.value);
      pass = false;
    }
    if (out_inoutEnum4.value != out_inoutEnum4_tmp.value) {
      TestUtil.logErr("compare_data failed for long - expected "
          + out_inoutEnum4_tmp.value + ",  received: " + out_inoutEnum4.value);
      pass = false;
    }
    if (out_inoutEnum5.value != out_inoutEnum5_tmp.value) {
      TestUtil.logErr("compare_data failed for short - expected "
          + out_inoutEnum5_tmp.value + ",  received: " + out_inoutEnum5.value);
      pass = false;
    }
    if (!out_inoutEnum6.value.equals(out_inoutEnum6_tmp.value)) {
      TestUtil.logErr("compare_data failed for BigDecimal - expected "
          + out_inoutEnum6_tmp.value + ",  received: " + out_inoutEnum6.value);
      pass = false;
    }
    if (out_inoutEnum7.value != out_inoutEnum7_tmp.value) {
      TestUtil.logErr("compare_data failed for float - expected "
          + out_inoutEnum7_tmp.value + ",  received: " + out_inoutEnum7.value);
      pass = false;
    }
    if (out_inoutEnum8.value != out_inoutEnum8_tmp.value) {
      TestUtil.logErr("compare_data failed for double - expected "
          + out_inoutEnum8_tmp.value + ",  received: " + out_inoutEnum8.value);
      pass = false;
    }
    if (out_inoutEnum9.value != out_inoutEnum9_tmp.value) {
      TestUtil.logErr("compare_data failed for byte - expected "
          + out_inoutEnum9_tmp.value + ",  received: " + out_inoutEnum9.value);
      pass = false;
    }
    return pass;
  }

  private boolean compare_in_Struct_data() {
    TestUtil.logMsg("Comparing IN data");

    boolean pass = true;
    if (!inStruct.getVarString().equals(inStruct_tmp.getVarString())) {
      TestUtil.logErr("compare_data failed for String- expected "
          + inStruct_tmp.getVarString() + ",  received: "
          + inStruct.getVarString());
      pass = false;
    }
    if (!inStruct.getVarInteger().equals(inStruct_tmp.getVarInteger())) {
      TestUtil.logErr("compare_data failed for BigInteger - expected "
          + inStruct_tmp.getVarInteger() + ",  received: "
          + inStruct.getVarInteger());
      pass = false;
    }
    if (inStruct.getVarInt() != inStruct_tmp.getVarInt()) {
      TestUtil.logErr("compare_data failed for int - expected "
          + inStruct_tmp.getVarInt() + ",  received: " + inStruct.getVarInt());
      pass = false;
    }
    if (inStruct.getVarLong() != inStruct_tmp.getVarLong()) {
      TestUtil.logErr(
          "compare_data failed for long - expected " + inStruct_tmp.getVarLong()
              + ",  received: " + inStruct.getVarLong());
      pass = false;
    }
    if (inStruct.getVarShort() != inStruct_tmp.getVarShort()) {
      TestUtil.logErr("compare_data failed for short - expected "
          + inStruct_tmp.getVarShort() + ",  received: "
          + inStruct.getVarShort());
      pass = false;
    }
    if (!inStruct.getVarDecimal().equals(inStruct_tmp.getVarDecimal())) {
      TestUtil.logErr("compare_data failed for BigDecimal - expected "
          + inStruct_tmp.getVarDecimal() + ",  received: "
          + inStruct.getVarDecimal());
      pass = false;
    }
    if (inStruct.getVarFloat() != inStruct_tmp.getVarFloat()) {
      TestUtil.logErr("compare_data failed for float - expected "
          + inStruct_tmp.getVarFloat() + ",  received: "
          + inStruct.getVarFloat());
      pass = false;
    }
    if (inStruct.getVarDouble() != inStruct_tmp.getVarDouble()) {
      TestUtil.logErr("compare_data failed for double - expected "
          + inStruct_tmp.getVarDouble() + ",  received: "
          + inStruct.getVarDouble());
      pass = false;
    }
    if (inStruct.isVarBoolean() != inStruct_tmp.isVarBoolean()) {
      TestUtil.logErr("compare_data failed for boolean - expected "
          + inStruct_tmp.isVarBoolean() + ",  received: "
          + inStruct.isVarBoolean());
      pass = false;
    }
    if (inStruct.getVarByte() != inStruct_tmp.getVarByte()) {
      TestUtil.logErr(
          "compare_data failed for byte - expected " + inStruct_tmp.getVarByte()
              + ",  received: " + inStruct.getVarByte());
      pass = false;
    }
    if (!inStruct.getVarQName().equals(inStruct_tmp.getVarQName())) {
      TestUtil.logErr("compare_data failed for QName - expected "
          + inStruct_tmp.getVarQName() + ",  received: "
          + inStruct.getVarQName());
      pass = false;
    }
    if (!JAXRPC_Data.compareCalendars(inStruct.getVarDateTime(),
        inStruct_tmp.getVarDateTime())) {
      TestUtil.logErr("compare_data failed for Calendar - expected "
          + inStruct_tmp.getVarDateTime() + ",  received: "
          + inStruct.getVarDateTime());
      pass = false;
    }
    if (!inStruct.getVarSoapString().equals(inStruct_tmp.getVarSoapString())) {
      TestUtil.logErr("compare_data failed for Soap String - expected "
          + inStruct_tmp.getVarSoapString() + ",  received: "
          + inStruct.getVarSoapString());
      pass = false;
    }
    if (!inStruct.getVarSoapBoolean()
        .equals(inStruct_tmp.getVarSoapBoolean())) {
      TestUtil.logErr("compare_data failed for Soap Boolean - expected "
          + inStruct_tmp.getVarSoapBoolean() + ",  received: "
          + inStruct.getVarSoapBoolean());
      pass = false;
    }
    if (!inStruct.getVarSoapFloat().equals(inStruct_tmp.getVarSoapFloat())) {
      TestUtil.logErr("compare_data failed for Soap Float - expected "
          + inStruct_tmp.getVarSoapFloat() + ",  received: "
          + inStruct.getVarSoapFloat());
      pass = false;
    }
    if (!inStruct.getVarSoapDouble().equals(inStruct_tmp.getVarSoapDouble())) {
      TestUtil.logErr("compare_data failed for Soap Double - expected "
          + inStruct_tmp.getVarSoapDouble() + ",  received: "
          + inStruct.getVarSoapDouble());
      pass = false;
    }
    if (!inStruct.getVarSoapDecimal()
        .equals(inStruct_tmp.getVarSoapDecimal())) {
      TestUtil.logErr("compare_data failed for Soap Decimal - expected "
          + inStruct_tmp.getVarSoapDecimal() + ",  received: "
          + inStruct.getVarSoapDecimal());
      pass = false;
    }
    if (!inStruct.getVarSoapInt().equals(inStruct_tmp.getVarSoapInt())) {
      TestUtil.logErr("compare_data failed Soap Int- expected "
          + inStruct_tmp.getVarSoapInt() + ",  received: "
          + inStruct.getVarSoapInt());
      pass = false;
    }
    if (!inStruct.getVarSoapShort().equals(inStruct_tmp.getVarSoapShort())) {
      TestUtil.logErr("compare_data failed Soap Short - expected "
          + inStruct_tmp.getVarSoapShort() + ",  received: "
          + inStruct.getVarSoapShort());
      pass = false;
    }
    if (!inStruct.getVarSoapByte().equals(inStruct_tmp.getVarSoapByte())) {
      TestUtil.logErr("compare_data failed Soap Byte - expected "
          + inStruct_tmp.getVarSoapByte() + ",  received: "
          + inStruct.getVarSoapByte());
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(inStruct.getVarBase64Binary(),
        inStruct_tmp.getVarBase64Binary(), "byte")) {
      TestUtil.logErr("compare_data failed for base64binary - expected "
          + inStruct_tmp.getVarBase64Binary() + ",  received: "
          + inStruct.getVarBase64Binary());
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(inStruct.getVarHexBinary(),
        inStruct_tmp.getVarHexBinary(), "byte")) {
      TestUtil.logErr("compare_data failed for hexbinary - expected "
          + inStruct_tmp.getVarHexBinary() + ",  received: "
          + inStruct.getVarHexBinary());
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(inStruct.getVarSoapBase64(),
        inStruct_tmp.getVarSoapBase64(), "byte")) {
      TestUtil.logErr("compare_data failed for soap base64 - expected "
          + inStruct_tmp.getVarSoapBase64() + ",  received: "
          + inStruct.getVarSoapBase64());
      pass = false;
    }

    return pass;
  }

  private boolean compare_out_inoutarray_data() {
    TestUtil.logMsg("Comparing IN/INOUT ARRAY data");
    boolean pass = true;
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray1.value,
        JAXRPC_Data.String_nonull_data, "String")) {
      TestUtil.logMsg("String array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray2.value,
        JAXRPC_Data.BigInteger_nonull_data, "BigInteger")) {
      TestUtil.logMsg("BigInteger array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray3.value,
        JAXRPC_Data.int_data, "int")) {
      TestUtil.logMsg("int array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray4.value,
        JAXRPC_Data.long_data, "long")) {
      TestUtil.logMsg("long array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray5.value,
        JAXRPC_Data.short_data, "short")) {
      TestUtil.logMsg("short array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray6.value,
        JAXRPC_Data.BigDecimal_nonull_data, "BigDecimal")) {
      TestUtil.logMsg("BigDecimal array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray7.value,
        JAXRPC_Data.float_data, "float")) {
      TestUtil.logMsg("float array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray8.value,
        JAXRPC_Data.double_data, "double")) {
      TestUtil.logMsg("double array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray9.value,
        JAXRPC_Data.boolean_data, "boolean")) {
      TestUtil.logMsg("boolean array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray10.value,
        JAXRPC_Data.byte_data, "byte")) {
      TestUtil.logMsg("byte array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray11.value,
        JAXRPC_Data.QName_nonull_data, "QName")) {
      TestUtil.logMsg("QNameBigDecimal array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray12.value,
        JAXRPC_Data.GregorianCalendar_nonull_data, "GregorianCalendar")) {
      TestUtil.logMsg("GregorianCalendar array data miscompare");
      pass = false;
    }
    return pass;
  }

  /*
   * @testName: InTest
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; WS4EE:SPEC:35;
   * WS4EE:SPEC:36;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void InTest() throws Fault {
    TestUtil.logTrace("InTest");
    boolean pass = true;
    try {
      TestUtil.logMsg("Marshalling input data");
      String in = "String1";
      String in_tmp = "String1";
      port.echoIn(in);
      if (!in.equals(in_tmp)) {
        TestUtil.logErr("compare_data failed for String- expected " + in_tmp
            + ",  received: " + in);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("InTest failed");
  }

  /*
   * @testName: OutTest
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; JAXRPC:SPEC:110;
   * WS4EE:SPEC:35; WS4EE:SPEC:36;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void OutTest() throws Fault {
    TestUtil.logTrace("OutTest");
    boolean pass = true;
    try {
      javax.xml.rpc.holders.StringHolder out = new javax.xml.rpc.holders.StringHolder(
          "String1");
      javax.xml.rpc.holders.StringHolder out_tmp = new javax.xml.rpc.holders.StringHolder(
          "String4");
      TestUtil.logMsg("Marshalling input data");
      port.echoOut(out);
      if (!out.value.equals(out_tmp.value)) {
        TestUtil.logErr("compare_data failed for String- expected "
            + out_tmp.value + ",  received: " + out.value);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("OutTest failed");
  }

  /*
   * @testName: InOutTest
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; JAXRPC:SPEC:110;
   * WS4EE:SPEC:35; WS4EE:SPEC:36; WS4EE:SPEC:209;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void InOutTest() throws Fault {
    TestUtil.logTrace("InOutTest");
    boolean pass = true;
    try {
      javax.xml.rpc.holders.StringHolder inout = new javax.xml.rpc.holders.StringHolder(
          "String1");
      javax.xml.rpc.holders.StringHolder inout_tmp = new javax.xml.rpc.holders.StringHolder(
          "String4");
      TestUtil.logMsg("Marshalling input data");
      port.echoInOut(inout);
      if (!inout.value.equals(inout_tmp.value)) {
        TestUtil.logErr("compare_data failed for String- expected "
            + inout_tmp.value + ",  received: " + inout.value);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("InOutTest failed");
  }

  /*
   * @testName: InOut2Test
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; JAXRPC:SPEC:110;
   * WS4EE:SPEC:35; WS4EE:SPEC:36;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   * This specific test does not use the parameterOrder attribute for the
   * operate tag
   */
  public void InOut2Test() throws Fault {
    TestUtil.logTrace("InOut2Test");
    boolean pass = true;
    try {
      javax.xml.rpc.holders.StringHolder inout = new javax.xml.rpc.holders.StringHolder(
          "String1");
      javax.xml.rpc.holders.StringHolder inout_tmp = new javax.xml.rpc.holders.StringHolder(
          "String4");
      TestUtil.logMsg("Marshalling input data");
      port.echoInOut(inout);
      if (!inout.value.equals(inout_tmp.value)) {
        TestUtil.logErr("compare_data failed for String- expected "
            + inout_tmp.value + ",  received: " + inout.value);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("InOutTest failed");
  }

  /*
   * @testName: InOut3Test
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; JAXRPC:SPEC:110;
   * WS4EE:SPEC:35; WS4EE:SPEC:36;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   * This specific test does not use the parameterOrder attribute for the
   * operate tag and it has different part names
   */
  public void InOut3Test() throws Fault {
    TestUtil.logTrace("InOut3Test");
    boolean pass = true;
    try {
      TestUtil.logMsg("Marshalling input data");
      String inout = "String1";
      String inout_tmp = "String4";
      String result = port.echoInOut3(inout);
      if (!result.equals(inout_tmp)) {
        TestUtil.logErr("compare_data failed for String- expected " + inout_tmp
            + ",  received: " + inout);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("InOut3Test failed");
  }

  /*
   * @testName: InOut4Test
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; JAXRPC:SPEC:110;
   * WS4EE:SPEC:35; WS4EE:SPEC:36;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   * This specific test uses the parameterOrder attribute for the operate tag
   * and has different part names
   */
  public void InOut4Test() throws Fault {
    TestUtil.logTrace("InOut4Test");
    boolean pass = true;
    try {
      TestUtil.logMsg("Marshalling input data");
      String inout = "String1";
      String inout_tmp = "String4";
      String result = port.echoInOut4(inout);
      if (!result.equals(inout_tmp)) {
        TestUtil.logErr("compare_data failed for String- expected " + inout_tmp
            + ",  received: " + inout);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("InOut4Test failed");
  }

  /*
   * @testName: MixTest
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; WS4EE:SPEC:35;
   * WS4EE:SPEC:36;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void MixTest() throws Fault {
    TestUtil.logTrace("MixTest");
    boolean pass = true;
    try {
      String in = "String1";
      String in_tmp = "String1";
      javax.xml.rpc.holders.StringHolder out = new javax.xml.rpc.holders.StringHolder(
          "String2");
      javax.xml.rpc.holders.StringHolder out_tmp = new javax.xml.rpc.holders.StringHolder(
          "String3");
      javax.xml.rpc.holders.StringHolder inout = new javax.xml.rpc.holders.StringHolder(
          "String4");
      javax.xml.rpc.holders.StringHolder inout_tmp = new javax.xml.rpc.holders.StringHolder(
          "String5");
      TestUtil.logMsg("Marshalling input data");
      port.echoMix(in, inout, out);
      if (!in.equals(in_tmp)) {
        TestUtil.logErr("compare_data failed for IN String- expected " + in_tmp
            + ",  received: " + in);
        pass = false;
      }
      if (!out.value.equals(out_tmp.value)) {
        TestUtil.logErr("compare_data failed for OUT String- expected "
            + out_tmp.value + ",  received: " + out.value);
        pass = false;
      }
      if (!inout.value.equals(inout_tmp.value)) {
        TestUtil.logErr("compare_data failed for INOUT String- expected "
            + inout_tmp.value + ",  received: " + inout.value);
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("MixTest failed");
  }

  /*
   * @testName: InSimpleTypesTest
   *
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; WS4EE:SPEC:35;
   * WS4EE:SPEC:36;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void InSimpleTypesTest() throws Fault {
    TestUtil.logTrace("InSimpleTypesTest");
    boolean pass = true;
    initialize_in_SimpleType_data();
    save_in_SimpleType_data();
    try {
      TestUtil.logMsg("Marshalling input data");
      port.echoInSimpleTypes(in1, in2, in3, in4, in5, in6, in7, in8, in9, in10,
          in11, in12, in13, in14, in15, in16, in17, in18, in19, in20, in21,
          in22, in23);
      if (!compare_in_SimpleType_data()) {
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("InSimpleTypesTest failed");
  }

  /*
   * @testName: OutSimpleTypesTest
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; WS4EE:SPEC:35;
   * WS4EE:SPEC:36;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void OutSimpleTypesTest() throws Fault {
    TestUtil.logTrace("OutSimpleTypesTest");
    boolean pass = true;
    initialize_out_inout_SimpleType_data();
    initialize_saved_out_inout_SimpleType_data();
    try {
      TestUtil.logMsg("Marshalling output data");
      port.echoOutSimpleTypes(out_inout1, out_inout2, out_inout3, out_inout4,
          out_inout5, out_inout6, out_inout7, out_inout8, out_inout9,
          out_inout10, out_inout11, out_inout12, out_inout13, out_inout14,
          out_inout15, out_inout16, out_inout17, out_inout18, out_inout19,
          out_inout20, out_inout21, out_inout22, out_inout23);

      if (!compare_out_inout_data()) {
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("OutSimpleTypesTest failed");
  }

  /*
   * @testName: InOutSimpleTypesTest
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; WS4EE:SPEC:35;
   * WS4EE:SPEC:36;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void InOutSimpleTypesTest() throws Fault {
    TestUtil.logTrace("InOutSimpleTypesTest");
    boolean pass = true;
    initialize_out_inout_SimpleType_data();
    initialize_saved_out_inout_SimpleType_data();
    try {
      TestUtil.logMsg("Marshalling output data");
      port.echoOutSimpleTypes(out_inout1, out_inout2, out_inout3, out_inout4,
          out_inout5, out_inout6, out_inout7, out_inout8, out_inout9,
          out_inout10, out_inout11, out_inout12, out_inout13, out_inout14,
          out_inout15, out_inout16, out_inout17, out_inout18, out_inout19,
          out_inout20, out_inout21, out_inout22, out_inout23);
      if (!compare_out_inout_data()) {
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("InOutSimpleTypesTest failed");
  }

  /*
   * @testName: InEnumerationTest
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; WS4EE:SPEC:35;
   * WS4EE:SPEC:36;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void InEnumerationTest() throws Fault {
    TestUtil.logTrace("InEnumerationTest");
    boolean pass = true;
    initialize_inEnum_data();
    save_inEnum_data();
    try {
      TestUtil.logMsg("Marshalling input data");
      port.echoInEnum(inEnum1, inEnum2, inEnum3, inEnum4, inEnum5, inEnum6,
          inEnum7, inEnum8, inEnum9);
      if (!compare_inEnum_data()) {
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("InEnumerationTest failed");
  }

  /*
   * @testName: OutEnumerationTest
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; JAXRPC:SPEC:108;
   * JAXRPC:SPEC:109; JAXRPC:SPEC:110; WS4EE:SPEC:35; WS4EE:SPEC:36;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void OutEnumerationTest() throws Fault {
    TestUtil.logTrace("OutEnumerationTest");
    initialize_out_inoutEnum_data();
    initialize_saved_out_inoutEnum_data();
    boolean pass = true;
    try {
      TestUtil.logMsg("Marshalling input data");
      port.echoOutEnum(out_inoutEnum1, out_inoutEnum2, out_inoutEnum3,
          out_inoutEnum4, out_inoutEnum5, out_inoutEnum6, out_inoutEnum7,
          out_inoutEnum8, out_inoutEnum9);
      if (!compare_out_inoutEnum_data()) {
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("OutEnumerationTest failed");
  }

  /*
   * @testName: InOutEnumerationTest
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; JAXRPC:SPEC:108;
   * JAXRPC:SPEC:109; JAXRPC:SPEC:110; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void InOutEnumerationTest() throws Fault {
    TestUtil.logTrace("InOutEnumerationTest");
    initialize_out_inoutEnum_data();
    initialize_saved_out_inoutEnum_data();
    boolean pass = true;
    try {
      TestUtil.logMsg("Marshalling input data");
      port.echoInOutEnum(out_inoutEnum1, out_inoutEnum2, out_inoutEnum3,
          out_inoutEnum4, out_inoutEnum5, out_inoutEnum6, out_inoutEnum7,
          out_inoutEnum8, out_inoutEnum9);
      if (!compare_out_inoutEnum_data()) {
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("InOutEnumerationTest failed");
  }

  /*
   * @testName: InStructTest
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; WS4EE:SPEC:35;
   * WS4EE:SPEC:36;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void InStructTest() throws Fault {
    TestUtil.logTrace("InStructTest");
    boolean pass = true;
    initialize_in_Struct_data();
    save_in_Struct_data();
    try {
      TestUtil.logMsg("Marshalling input data");
      port.echoInStruct(inStruct);
      if (!compare_in_Struct_data()) {
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("InStructTest failed");
  }

  /*
   * @testName: InOutSimpleTypesArrayTest
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; WS4EE:SPEC:35;
   * WS4EE:SPEC:36;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void InOutSimpleTypesArrayTest() throws Fault {
    TestUtil.logTrace("InOutSimpleTypesArrayTest");
    boolean pass = true;
    initialize_out_inout_SimpleTypeArray_data();
    try {
      TestUtil.logMsg("Marshalling output data");
      port.echoInOutSimpleTypesArray(out_inoutarray1, out_inoutarray2,
          out_inoutarray3, out_inoutarray4, out_inoutarray5, out_inoutarray6,
          out_inoutarray7, out_inoutarray8, out_inoutarray9, out_inoutarray10,
          out_inoutarray11, out_inoutarray12);
      if (!compare_out_inoutarray_data()) {
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("InOutSimpleTypesArrayTest failed");
  }

  /*
   * @testName: InOutUserDefinedTypeTest
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; WS4EE:SPEC:35;
   * WS4EE:SPEC:36;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void InOutUserDefinedTypeTest() throws Fault {
    TestUtil.logTrace("InOutUserDefinedTypeTest");
    boolean pass = true;
    Book b0 = new Book();
    b0.setAuthor("author0");
    b0.setTitle("title0");
    b0.setIsbn(0);
    BookHolder out_inout_bh = new BookHolder(b0);
    Book b1 = new Book();
    b1.setAuthor("author1");
    b1.setTitle("title1");
    b1.setIsbn(1);

    try {
      TestUtil.logMsg("Marshalling output data");
      TestUtil.logMsg("Data In: [author0|title0|0]");
      port.echoInOutBook(out_inout_bh);
      Book v = out_inout_bh.value;
      TestUtil.logMsg("Data Out: [" + v.getAuthor() + "|" + v.getTitle() + "|"
          + v.getIsbn() + "]");
      if (!v.getAuthor().equals(b1.getAuthor())
          || !v.getTitle().equals(b1.getTitle())
          || v.getIsbn() != b1.getIsbn()) {
        pass = false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("InOutUserDefinedTypeTest failed");
  }

  /*
   * @testName: InOutUserDefinedTypeArrayTest
   *
   * @assertion_ids: JAXRPC:SPEC:77; JAXRPC:SPEC:78; WS4EE:SPEC:35;
   * WS4EE:SPEC:36;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void InOutUserDefinedTypeArrayTest() throws Fault {
    TestUtil.logTrace("InOutUserDefinedTypeArrayTest");
    boolean pass = true;
    Book b0 = new Book();
    b0.setAuthor("author0");
    b0.setTitle("title0");
    b0.setIsbn(0);
    Book b1 = new Book();
    b1.setAuthor("author1");
    b1.setTitle("title1");
    b1.setIsbn(1);
    Book b[] = new Book[2];
    b[0] = b0;
    b[1] = b1;
    ArrayOfBookHolder out_inout_bh = new ArrayOfBookHolder(b);
    Book s[] = new Book[2];
    s[0] = b1;
    s[1] = b0;

    try {
      TestUtil.logMsg("Marshalling output data");
      TestUtil.logMsg("Data In: [author0|title0|0," + "author1|title1|1]");
      port.echoInOutBookArray(out_inout_bh);
      Book r[] = out_inout_bh.value;
      if (r.length != 2) {
        TestUtil.logErr("array size not equal 2");
        pass = false;
      } else {
        TestUtil.logMsg("Data Out: [" + r[0].getAuthor() + "|" + r[0].getTitle()
            + "|" + r[0].getIsbn() + "," + r[1].getAuthor() + "|"
            + r[1].getTitle() + "|" + r[1].getIsbn() + "]");
        for (int i = 0; i < r.length; i++) {
          if (!r[i].getAuthor().equals(s[i].getAuthor())
              || !r[i].getTitle().equals(s[i].getTitle())
              || r[i].getIsbn() != s[i].getIsbn()) {
            pass = false;
          }
        }
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      pass = false;
    }

    if (!pass)
      throw new Fault("InOutUserDefinedTypeArrayTest failed");
  }
}
