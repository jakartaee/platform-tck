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

package com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest;

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

import com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.holders.*;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.";

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

  Object in13 = null, in13_tmp = null;

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

  // URL properties used by the test
  private static final String ENDPOINT_URL = "wsirlholdertest.endpoint.1";

  private static final String WSDLLOC_URL = "wsirlholdertest.wsdlloc.1";

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
  HolderTest port = null;

  Stub stub = null;

  private void getStubStandalone() throws Exception {
    TestUtil.logMsg("Get stub from service implementation class"
        + " using JAXRPC porting instance");
    port = (HolderTest) JAXRPC_Util
        .getStub("com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal."
            + "holdertest.HolderTestService", "getHolderTestPort");
    TestUtil.logMsg("Cast stub to base Stub class ...");
    stub = (javax.xml.rpc.Stub) port;
  }

  private void getStub() throws Exception {
    TestUtil.logMsg("JNDI lookup for Service1");
    InitialContext ctx = new InitialContext();
    TestUtil.logMsg("Lookup java:comp/env/service/wsirlholdertest");
    Service svc = (javax.xml.rpc.Service) ctx
        .lookup("java:comp/env/service/wsirlholdertest");
    TestUtil.logMsg("Get port from Service1");
    port = (HolderTest) svc.getPort(
        com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.holdertest.HolderTest.class);
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
  }

  private void initialize_out_inout_SimpleTypeArray_data() {
    ArrayOfstring o1 = new ArrayOfstring();
    o1.setArrayOfstring(JAXRPC_Data.String_nonull_data);
    out_inoutarray1 = new ArrayOfstringHolder(o1);

    ArrayOfinteger o2 = new ArrayOfinteger();
    o2.setArrayOfinteger(JAXRPC_Data.BigInteger_nonull_data);
    out_inoutarray2 = new ArrayOfintegerHolder(o2);

    ArrayOfint o3 = new ArrayOfint();
    o3.setArrayOfint(JAXRPC_Data.int_data);
    out_inoutarray3 = new ArrayOfintHolder(o3);

    ArrayOflong o4 = new ArrayOflong();
    o4.setArrayOflong(JAXRPC_Data.long_data);
    out_inoutarray4 = new ArrayOflongHolder(o4);

    ArrayOfshort o5 = new ArrayOfshort();
    o5.setArrayOfshort(JAXRPC_Data.short_data);
    out_inoutarray5 = new ArrayOfshortHolder(o5);

    ArrayOfdecimal o6 = new ArrayOfdecimal();
    o6.setArrayOfdecimal(JAXRPC_Data.BigDecimal_nonull_data);
    out_inoutarray6 = new ArrayOfdecimalHolder(o6);

    ArrayOffloat o7 = new ArrayOffloat();
    o7.setArrayOffloat(JAXRPC_Data.float_data);
    out_inoutarray7 = new ArrayOffloatHolder(o7);

    ArrayOfdouble o8 = new ArrayOfdouble();
    o8.setArrayOfdouble(JAXRPC_Data.double_data);
    out_inoutarray8 = new ArrayOfdoubleHolder(o8);

    ArrayOfboolean o9 = new ArrayOfboolean();
    o9.setArrayOfboolean(JAXRPC_Data.boolean_data);
    out_inoutarray9 = new ArrayOfbooleanHolder(o9);

    ArrayOfbyte o10 = new ArrayOfbyte();
    o10.setArrayOfbyte(JAXRPC_Data.byte_data);
    out_inoutarray10 = new ArrayOfbyteHolder(o10);

    ArrayOfQName o11 = new ArrayOfQName();
    o11.setArrayOfQName(JAXRPC_Data.QName_nonull_data);
    out_inoutarray11 = new ArrayOfQNameHolder(o11);

    ArrayOfdateTime o12 = new ArrayOfdateTime();
    o12.setArrayOfdateTime(JAXRPC_Data.GregorianCalendar_nonull_data);
    out_inoutarray12 = new ArrayOfdateTimeHolder(o12);
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
    return pass;
  }

  private boolean compare_out_inoutarray_data() {
    TestUtil.logMsg("Comparing IN/INOUT ARRAY data");
    boolean pass = true;
    if (!JAXRPC_Data.compareArrayValues(
        out_inoutarray1.value.getArrayOfstring(),
        JAXRPC_Data.String_nonull_data, "String")) {
      TestUtil.logMsg("String array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(
        out_inoutarray2.value.getArrayOfinteger(),
        JAXRPC_Data.BigInteger_nonull_data, "BigInteger")) {
      TestUtil.logMsg("BigInteger array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray3.value.getArrayOfint(),
        JAXRPC_Data.int_data, "int")) {
      TestUtil.logMsg("int array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray4.value.getArrayOflong(),
        JAXRPC_Data.long_data, "long")) {
      TestUtil.logMsg("long array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray5.value.getArrayOfshort(),
        JAXRPC_Data.short_data, "short")) {
      TestUtil.logMsg("short array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(
        out_inoutarray6.value.getArrayOfdecimal(),
        JAXRPC_Data.BigDecimal_nonull_data, "BigDecimal")) {
      TestUtil.logMsg("BigDecimal array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray7.value.getArrayOffloat(),
        JAXRPC_Data.float_data, "float")) {
      TestUtil.logMsg("float array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(
        out_inoutarray8.value.getArrayOfdouble(), JAXRPC_Data.double_data,
        "double")) {
      TestUtil.logMsg("double array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(
        out_inoutarray9.value.getArrayOfboolean(), JAXRPC_Data.boolean_data,
        "boolean")) {
      TestUtil.logMsg("boolean array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(out_inoutarray10.value.getArrayOfbyte(),
        JAXRPC_Data.byte_data, "byte")) {
      TestUtil.logMsg("byte array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(
        out_inoutarray11.value.getArrayOfQName(), JAXRPC_Data.QName_nonull_data,
        "QName")) {
      TestUtil.logMsg("QNameBigDecimal array data miscompare");
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(
        out_inoutarray12.value.getArrayOfdateTime(),
        JAXRPC_Data.GregorianCalendar_nonull_data, "GregorianCalendar")) {
      TestUtil.logMsg("GregorianCalendar array data miscompare");
      pass = false;
    }
    return pass;
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
      port.echoInOutSimpleTypes(out_inout1, out_inout2, out_inout3, out_inout4,
          out_inout5, out_inout6, out_inout7, out_inout8, out_inout9,
          out_inout10, out_inout11, out_inout12);
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
    ArrayOfBook aob = new ArrayOfBook();
    aob.setArrayOfBook(b);
    ArrayOfBookHolder out_inout_bh = new ArrayOfBookHolder(aob);
    Book s[] = new Book[2];
    s[0] = b1;
    s[1] = b0;

    try {
      TestUtil.logMsg("Marshalling output data");
      TestUtil.logMsg("Data In: [author0|title0|0," + "author1|title1|1]");
      port.echoInOutBookArray(out_inout_bh);
      ArrayOfBook v = out_inout_bh.value;
      Book r[] = v.getArrayOfBook();
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
