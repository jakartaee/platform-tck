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

package com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;

import jakarta.xml.ws.*;

import java.util.Properties;
import java.util.ArrayList;
import java.util.List;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;

import java.math.BigInteger;
import java.math.BigDecimal;
import jakarta.xml.ws.Holder;
import javax.xml.namespace.QName;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.";

  // service and port information
  private static final String NAMESPACEURI = "http://ParameterModeTest.org/";

  private static final String SERVICE_NAME = "ParameterModeTestService";

  private static final String PORT_NAME = "ParameterModeTestPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  private DatatypeFactory dtfactory = null;

  ParameterModeTest port = null;

  static ParameterModeTestService service = null;

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

  XMLGregorianCalendar in12 = null, in12_tmp = null;

  byte[] in13, in13_tmp;

  byte[] in14, in14_tmp;

  Holder<String> out_inout1, out_inout1_tmp;

  Holder<BigInteger> out_inout2, out_inout2_tmp;

  Holder<Integer> out_inout3, out_inout3_tmp;

  Holder<Long> out_inout4, out_inout4_tmp;

  Holder<Short> out_inout5, out_inout5_tmp;

  Holder<BigDecimal> out_inout6, out_inout6_tmp;

  Holder<Float> out_inout7, out_inout7_tmp;

  Holder<Double> out_inout8, out_inout8_tmp;

  Holder<Boolean> out_inout9, out_inout9_tmp;

  Holder<Byte> out_inout10, out_inout10_tmp;

  Holder<QName> out_inout11, out_inout11_tmp;

  Holder<XMLGregorianCalendar> out_inout12, out_inout12_tmp;

  Holder<byte[]> out_inout13, out_inout13_tmp;

  Holder<byte[]> out_inout14, out_inout14_tmp;

  Holder<ArrayOfstring> out_inoutarray1, out_inoutarray1_tmp;

  Holder<ArrayOfinteger> out_inoutarray2, out_inoutarray2_tmp;

  Holder<ArrayOfint> out_inoutarray3, out_inoutarray3_tmp;

  Holder<ArrayOflong> out_inoutarray4, out_inoutarray4_tmp;

  Holder<ArrayOfshort> out_inoutarray5, out_inoutarray5_tmp;

  Holder<ArrayOfdecimal> out_inoutarray6, out_inoutarray6_tmp;

  Holder<ArrayOffloat> out_inoutarray7, out_inoutarray7_tmp;

  Holder<ArrayOfdouble> out_inoutarray8, out_inoutarray8_tmp;

  Holder<ArrayOfboolean> out_inoutarray9, out_inoutarray9_tmp;

  Holder<ArrayOfbyte> out_inoutarray10, out_inoutarray10_tmp;

  Holder<ArrayOfQName> out_inoutarray11, out_inoutarray11_tmp;

  Holder<ArrayOfdateTime> out_inoutarray12, out_inoutarray12_tmp;

  EnumString inEnum1, inEnum1_tmp;

  BigInteger inEnum2, inEnum2_tmp;

  Integer inEnum3, inEnum3_tmp;

  Long inEnum4, inEnum4_tmp;

  Short inEnum5, inEnum5_tmp;

  BigDecimal inEnum6, inEnum6_tmp;

  Float inEnum7, inEnum7_tmp;

  Double inEnum8, inEnum8_tmp;

  Byte inEnum9, inEnum9_tmp;

  Holder<EnumString> out_inoutEnum1, out_inoutEnum1_tmp;

  Holder<BigInteger> out_inoutEnum2, out_inoutEnum2_tmp;

  Holder<Integer> out_inoutEnum3, out_inoutEnum3_tmp;

  Holder<Long> out_inoutEnum4, out_inoutEnum4_tmp;

  Holder<Short> out_inoutEnum5, out_inoutEnum5_tmp;

  Holder<BigDecimal> out_inoutEnum6, out_inoutEnum6_tmp;

  Holder<Float> out_inoutEnum7, out_inoutEnum7_tmp;

  Holder<Double> out_inoutEnum8, out_inoutEnum8_tmp;

  Holder<Byte> out_inoutEnum9, out_inoutEnum9_tmp;

  AllStruct inStruct = null;

  AllStruct inStruct_tmp = null;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "w2jrlparametermodetest.endpoint.1";

  private static final String WSDLLOC_URL = "w2jrlparametermodetest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

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
    port = (ParameterModeTest) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        ParameterModeTestService.class, PORT_QNAME, ParameterModeTest.class);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (ParameterModeTest) service.getParameterModeTestPort();
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
      getTestURLs();
      modeProperty = p.getProperty(MODEPROP);
      if (modeProperty.equals("standalone")) {
        getPortStandalone();
      } else {
        TestUtil.logMsg(
            "WebServiceRef is not set in Client (get it from specific vehicle)");
        service = (ParameterModeTestService) getSharedObject();
        getPortJavaEE();
      }
      try {
        dtfactory = DatatypeFactory.newInstance();
      } catch (DatatypeConfigurationException e) {
        TestUtil.logErr("Could not configure DatatypeFactory object");
        TestUtil.printStackTrace(e);
        pass = false;
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
    in12 = dtfactory.newXMLGregorianCalendar(96, 5, 1, 10, 0, 0, 0, 0);
    in13 = JAXWS_Data.byte_data;
    in14 = JAXWS_Data.byte_data;
  }

  private void initialize_out_inout_SimpleType_data() {
    out_inout1 = new Holder("String1");
    out_inout2 = new Holder(new BigInteger("3512359"));
    out_inout3 = new Holder(Integer.MIN_VALUE);
    out_inout4 = new Holder(Long.MIN_VALUE);
    out_inout5 = new Holder(Short.MIN_VALUE);
    out_inout6 = new Holder(new BigDecimal("3512359.1456"));
    out_inout7 = new Holder(Float.MIN_VALUE);
    out_inout8 = new Holder(Double.MIN_VALUE);
    out_inout9 = new Holder(false);
    out_inout10 = new Holder(Byte.MIN_VALUE);
    out_inout11 = new Holder(new QName("String2"));
    out_inout12 = new Holder(
        dtfactory.newXMLGregorianCalendar(6, 5, 1, 10, 0, 0, 0, 0));
    out_inout13 = new Holder(JAXWS_Data.byte_data);
    out_inout14 = new Holder(JAXWS_Data.byte_data);
  }

  private void initialize_out_inout_SimpleTypeArray_data() {

    ArrayOfstring astring = new ArrayOfstring();
    int j = JAXWS_Data.String_nonull_data.length;
    for (int i = 0; i < JAXWS_Data.String_nonull_data.length; i++) {
      astring.getArrayOfstring().add(JAXWS_Data.String_nonull_data[--j]);
    }
    out_inoutarray1 = new Holder(astring);

    ArrayOfinteger ainteger = new ArrayOfinteger();
    j = JAXWS_Data.BigInteger_nonull_data.length;
    for (int i = 0; i < JAXWS_Data.BigInteger_nonull_data.length; i++) {
      ainteger.getArrayOfinteger().add(JAXWS_Data.BigInteger_nonull_data[--j]);
    }
    out_inoutarray2 = new Holder(ainteger);

    ArrayOfint aint = new ArrayOfint();
    j = JAXWS_Data.int_data.length;
    for (int i = 0; i < JAXWS_Data.int_data.length; i++) {
      aint.getArrayOfint().add(JAXWS_Data.int_data[--j]);
    }
    out_inoutarray3 = new Holder(aint);

    ArrayOflong along = new ArrayOflong();
    j = JAXWS_Data.long_data.length;
    for (int i = 0; i < JAXWS_Data.long_data.length; i++) {
      along.getArrayOflong().add(JAXWS_Data.long_data[--j]);
    }
    out_inoutarray4 = new Holder(along);

    ArrayOfshort ashort = new ArrayOfshort();
    j = JAXWS_Data.short_data.length;
    for (int i = 0; i < JAXWS_Data.short_data.length; i++) {
      ashort.getArrayOfshort().add(JAXWS_Data.short_data[--j]);
    }
    out_inoutarray5 = new Holder(ashort);

    ArrayOfdecimal adecimal = new ArrayOfdecimal();
    j = JAXWS_Data.BigDecimal_nonull_data.length;
    for (int i = 0; i < JAXWS_Data.BigDecimal_nonull_data.length; i++) {
      adecimal.getArrayOfdecimal().add(JAXWS_Data.BigDecimal_nonull_data[--j]);
    }
    out_inoutarray6 = new Holder(adecimal);

    ArrayOffloat afloat = new ArrayOffloat();
    j = JAXWS_Data.float_data.length;
    for (int i = 0; i < JAXWS_Data.float_data.length; i++) {
      afloat.getArrayOffloat().add(JAXWS_Data.float_data[--j]);
    }
    out_inoutarray7 = new Holder(afloat);

    ArrayOfdouble adouble = new ArrayOfdouble();
    j = JAXWS_Data.double_data.length;
    for (int i = 0; i < JAXWS_Data.double_data.length; i++) {
      adouble.getArrayOfdouble().add(JAXWS_Data.double_data[--j]);
    }
    out_inoutarray8 = new Holder(adouble);

    ArrayOfboolean abool = new ArrayOfboolean();
    j = JAXWS_Data.boolean_data.length;
    for (int i = 0; i < JAXWS_Data.boolean_data.length; i++) {
      abool.getArrayOfboolean().add(JAXWS_Data.boolean_data[--j]);
    }
    out_inoutarray9 = new Holder(abool);

    ArrayOfbyte abyte = new ArrayOfbyte();
    j = JAXWS_Data.byte_data.length;
    for (int i = 0; i < JAXWS_Data.byte_data.length; i++) {
      abyte.getArrayOfbyte().add(JAXWS_Data.byte_data[--j]);
    }
    out_inoutarray10 = new Holder(abyte);

    ArrayOfQName aqname = new ArrayOfQName();
    j = JAXWS_Data.QName_nonull_data.length;
    for (int i = 0; i < JAXWS_Data.QName_nonull_data.length; i++) {
      aqname.getArrayOfQName().add(JAXWS_Data.QName_nonull_data[--j]);
    }
    out_inoutarray11 = new Holder(aqname);

    ArrayOfdateTime adt = new ArrayOfdateTime();
    j = JAXWS_Data.XMLGregorianCalendar_nonull_data.length;
    for (int i = 0; i < JAXWS_Data.XMLGregorianCalendar_nonull_data.length; i++) {
      adt.getArrayOfdateTime()
          .add(JAXWS_Data.XMLGregorianCalendar_nonull_data[--j]);
    }
    out_inoutarray12 = new Holder(adt);
  }

  private void initialize_saved_out_inout_SimpleTypeArray_data() {

    ArrayOfstring astring = new ArrayOfstring();
    for (int i = 0; i < JAXWS_Data.String_nonull_data.length; i++) {
      astring.getArrayOfstring().add(JAXWS_Data.String_nonull_data[i]);
    }
    out_inoutarray1_tmp = new Holder(astring);

    ArrayOfinteger ainteger = new ArrayOfinteger();
    for (int i = 0; i < JAXWS_Data.BigInteger_nonull_data.length; i++) {
      ainteger.getArrayOfinteger().add(JAXWS_Data.BigInteger_nonull_data[i]);
    }
    out_inoutarray2_tmp = new Holder(ainteger);

    ArrayOfint aint = new ArrayOfint();
    for (int i = 0; i < JAXWS_Data.int_data.length; i++) {
      aint.getArrayOfint().add(JAXWS_Data.int_data[i]);
    }
    out_inoutarray3_tmp = new Holder(aint);

    ArrayOflong along = new ArrayOflong();
    for (int i = 0; i < JAXWS_Data.long_data.length; i++) {
      along.getArrayOflong().add(JAXWS_Data.long_data[i]);
    }
    out_inoutarray4_tmp = new Holder(along);

    ArrayOfshort ashort = new ArrayOfshort();
    for (int i = 0; i < JAXWS_Data.short_data.length; i++) {
      ashort.getArrayOfshort().add(JAXWS_Data.short_data[i]);
    }
    out_inoutarray5_tmp = new Holder(ashort);

    ArrayOfdecimal adecimal = new ArrayOfdecimal();
    for (int i = 0; i < JAXWS_Data.BigDecimal_nonull_data.length; i++) {
      adecimal.getArrayOfdecimal().add(JAXWS_Data.BigDecimal_nonull_data[i]);
    }
    out_inoutarray6_tmp = new Holder(adecimal);

    ArrayOffloat afloat = new ArrayOffloat();
    for (int i = 0; i < JAXWS_Data.float_data.length; i++) {
      afloat.getArrayOffloat().add(JAXWS_Data.float_data[i]);
    }
    out_inoutarray7_tmp = new Holder(afloat);

    ArrayOfdouble adouble = new ArrayOfdouble();
    for (int i = 0; i < JAXWS_Data.double_data.length; i++) {
      adouble.getArrayOfdouble().add(JAXWS_Data.double_data[i]);
    }
    out_inoutarray8_tmp = new Holder(adouble);

    ArrayOfboolean abool = new ArrayOfboolean();
    for (int i = 0; i < JAXWS_Data.boolean_data.length; i++) {
      abool.getArrayOfboolean().add(JAXWS_Data.boolean_data[i]);
    }
    out_inoutarray9_tmp = new Holder(abool);

    ArrayOfbyte abyte = new ArrayOfbyte();
    for (int i = 0; i < JAXWS_Data.byte_data.length; i++) {
      abyte.getArrayOfbyte().add(JAXWS_Data.byte_data[i]);
    }
    out_inoutarray10_tmp = new Holder(abyte);

    ArrayOfQName aqname = new ArrayOfQName();
    for (int i = 0; i < JAXWS_Data.QName_nonull_data.length; i++) {
      aqname.getArrayOfQName().add(JAXWS_Data.QName_nonull_data[i]);
    }
    out_inoutarray11_tmp = new Holder(aqname);

    ArrayOfdateTime adt = new ArrayOfdateTime();
    for (int i = 0; i < JAXWS_Data.XMLGregorianCalendar_nonull_data.length; i++) {
      adt.getArrayOfdateTime()
          .add(JAXWS_Data.XMLGregorianCalendar_nonull_data[i]);
    }
    out_inoutarray12_tmp = new Holder(adt);
  }

  private void initialize_inEnum_data() {
    try {
      inEnum1 = EnumString.STRING_1;
      inEnum2 = new BigInteger("3512359");
      inEnum3 = Integer.MIN_VALUE;
      inEnum4 = Long.MIN_VALUE;
      inEnum5 = Short.MIN_VALUE;
      inEnum6 = new BigDecimal("3512359.1456");
      inEnum7 = Float.valueOf("-1.00000000");
      inEnum8 = Double.valueOf("-1.0000000000000");
      inEnum9 = Byte.valueOf(Byte.MIN_VALUE);
    } catch (IllegalArgumentException e) {
      TestUtil.logErr("Couldn't initialize enumeration data", e);
      throw e;
    }
  }

  private void initialize_out_inoutEnum_data() {
    out_inoutEnum1 = new Holder(EnumString.STRING_1);
    out_inoutEnum2 = new Holder(new BigInteger("3512359"));
    out_inoutEnum3 = new Holder(Integer.MIN_VALUE);
    out_inoutEnum4 = new Holder(Long.MIN_VALUE);
    out_inoutEnum5 = new Holder(Short.MIN_VALUE);
    out_inoutEnum6 = new Holder(new BigDecimal("3512359.1456"));
    out_inoutEnum7 = new Holder(Float.valueOf("-1.00000000"));
    out_inoutEnum8 = new Holder(Double.valueOf("-1.0000000000000"));
    out_inoutEnum9 = new Holder(Byte.valueOf(Byte.MIN_VALUE));
  }

  private void initialize_in_Struct_data() {
    inStruct = new AllStruct();
    inStruct.setVarString("String1");
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
    inStruct.setVarDateTime(
        dtfactory.newXMLGregorianCalendar(96, 5, 1, 10, 0, 0, 0, 0));
    inStruct.setVarBase64Binary(JAXWS_Data.byte_data);
    inStruct.setVarHexBinary(JAXWS_Data.byte_data);
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
    in12_tmp = dtfactory.newXMLGregorianCalendar(96, 5, 2, 10, 0, 0, 0, 0);
    in13_tmp = JAXWS_Data.byte_data2;
    in14_tmp = JAXWS_Data.byte_data2;
  }

  private void initialize_saved_out_inout_SimpleType_data() {
    out_inout1_tmp = new Holder("String4");
    out_inout2_tmp = new Holder(new BigInteger("3512360"));
    out_inout3_tmp = new Holder(Integer.MAX_VALUE);
    out_inout4_tmp = new Holder(Long.MAX_VALUE);
    out_inout5_tmp = new Holder(Short.MAX_VALUE);
    out_inout6_tmp = new Holder(new BigDecimal("3512360.1456"));
    out_inout7_tmp = new Holder(Float.MAX_VALUE);
    out_inout8_tmp = new Holder(Double.MAX_VALUE);
    out_inout9_tmp = new Holder(true);
    out_inout10_tmp = new Holder(Byte.MAX_VALUE);
    out_inout11_tmp = new Holder(new QName("String5"));
    out_inout12_tmp = new Holder(
        dtfactory.newXMLGregorianCalendar(96, 5, 2, 10, 0, 0, 0, 0));
    out_inout13_tmp = new Holder(JAXWS_Data.byte_data2);
    out_inout14_tmp = new Holder(JAXWS_Data.byte_data2);

  }

  private void initialize_saved_out_inoutEnum_data() {
    out_inoutEnum1_tmp = new Holder(EnumString.STRING_2);
    out_inoutEnum2_tmp = new Holder(new BigInteger("3512360"));
    out_inoutEnum3_tmp = new Holder(Integer.MAX_VALUE);
    out_inoutEnum4_tmp = new Holder(Long.MAX_VALUE);
    out_inoutEnum5_tmp = new Holder(Short.MAX_VALUE);
    out_inoutEnum6_tmp = new Holder(new BigDecimal("3512360.1456"));
    out_inoutEnum7_tmp = new Holder(Float.valueOf("3.00000000"));
    out_inoutEnum8_tmp = new Holder(Double.valueOf("3.0000000000000"));
    out_inoutEnum9_tmp = new Holder(Byte.valueOf(Byte.MAX_VALUE));
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
    inStruct.setVarDateTime(
        dtfactory.newXMLGregorianCalendar(96, 5, 2, 10, 0, 0, 0, 0));
    inStruct.setVarBase64Binary(JAXWS_Data.byte_data2);
    inStruct.setVarHexBinary(JAXWS_Data.byte_data2);
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
    inStruct_tmp.setVarBase64Binary(inStruct.getVarBase64Binary());
    inStruct_tmp.setVarHexBinary(inStruct.getVarHexBinary());
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
    if (!JAXWS_Data.compareXMLGregorianCalendars(in12, in12_tmp)) {
      TestUtil.logErr("compare_data failed for XMLGregorianCalendar - expected "
          + in12_tmp + ",  received: " + in12);
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(in13, in13_tmp, "byte")) {
      TestUtil.logErr("compare_data failed for base64binary - expected "
          + in13_tmp + ",  received: " + in13);
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(in14, in14_tmp, "byte")) {
      TestUtil.logErr("compare_data failed for hexbinary - expected " + in14_tmp
          + ",  received: " + in14);
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
    if (!out_inout3.value.equals(out_inout3_tmp.value)) {
      TestUtil.logErr("compare_data failed for IntHolder - expected "
          + out_inout3_tmp.value + ",  received: " + out_inout3.value);
      pass = false;
    }
    if (!out_inout4.value.equals(out_inout4_tmp.value)) {
      TestUtil.logErr("compare_data failed for LongHolder - expected "
          + out_inout4_tmp.value + ",  received: " + out_inout4.value);
      pass = false;
    }
    if (!out_inout5.value.equals(out_inout5_tmp.value)) {
      TestUtil.logErr("compare_data failed for ShortHolder - expected "
          + out_inout5_tmp.value + ",  received: " + out_inout5.value);
      pass = false;
    }
    if (!out_inout6.value.equals(out_inout6_tmp.value)) {
      TestUtil.logErr("compare_data failed for BigDecimalHolder - expected "
          + out_inout6_tmp.value + ",  received: " + out_inout6.value);
      pass = false;
    }
    if (!out_inout7.value.equals(out_inout7_tmp.value)) {
      TestUtil.logErr("compare_data failed for FloatHolder - expected "
          + out_inout7_tmp.value + ",  received: " + out_inout7.value);
      pass = false;
    }
    if (!out_inout8.value.equals(out_inout8_tmp.value)) {
      TestUtil.logErr("compare_data failed for DoubleHolder - expected "
          + out_inout8_tmp.value + ",  received: " + out_inout8.value);
      pass = false;
    }
    if (!out_inout9.value.equals(out_inout9_tmp.value)) {
      TestUtil.logErr("compare_data failed for BooleanHolder - expected "
          + out_inout9_tmp.value + ",  received: " + out_inout9.value);
      pass = false;
    }
    if (!out_inout10.value.equals(out_inout10_tmp.value)) {
      TestUtil.logErr("compare_data failed for ByteHolder - expected "
          + out_inout10_tmp.value + ",  received: " + out_inout10.value);
      pass = false;
    }
    if (!out_inout11.value.equals(out_inout11_tmp.value)) {
      TestUtil.logErr("compare_data failed for QNameHolder - expected "
          + out_inout11_tmp.value + ",  received: " + out_inout11.value);
      pass = false;
    }
    if (!JAXWS_Data.compareXMLGregorianCalendars(out_inout12.value,
        out_inout12_tmp.value)) {
      TestUtil.logErr("compare_data failed for XMLGregorianCalendar - expected "
          + out_inout12_tmp.value + ",  received: " + out_inout12.value);
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(out_inout13.value, out_inout13_tmp.value,
        "byte")) {
      TestUtil
          .logErr("compare_data failed for ByteArrayWrapperHolder - expected ");
      JAXWS_Data.dumpArrayValues(out_inout13_tmp.value, "byte");
      TestUtil.logErr(" received: ");
      JAXWS_Data.dumpArrayValues(out_inout13.value, "byte");
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(out_inout14.value, out_inout14_tmp.value,
        "byte")) {
      TestUtil
          .logErr("compare_data failed for ByteArrayWrapperHolder - expected ");
      JAXWS_Data.dumpArrayValues(out_inout14_tmp.value, "byte");
      TestUtil.logErr(" received: ");
      JAXWS_Data.dumpArrayValues(out_inout14.value, "byte");
      pass = false;
    }

    return pass;
  }

  private boolean compare_inEnum_data() {
    TestUtil.logMsg("Comparing IN enumeration data");

    boolean pass = true;
    if (inEnum1 != inEnum1_tmp) {
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
    TestUtil.logMsg("Comparing OUT_INOUT enumeration data");

    boolean pass = true;
    if (out_inoutEnum1.value != out_inoutEnum1_tmp.value) {
      TestUtil.logErr("compare_data failed for String- expected "
          + out_inoutEnum1_tmp.value + ",  received: " + out_inoutEnum1.value);
      pass = false;
    }
    if (!out_inoutEnum2.value.equals(out_inoutEnum2_tmp.value)) {
      TestUtil.logErr("compare_data failed for BigInteger - expected "
          + out_inoutEnum2_tmp.value + ",  received: " + out_inoutEnum2.value);
      pass = false;
    }
    if (!out_inoutEnum3.value.equals(out_inoutEnum3_tmp.value)) {
      TestUtil.logErr("compare_data failed for Integer - expected "
          + out_inoutEnum3_tmp.value + ",  received: " + out_inoutEnum3.value);
      pass = false;
    }
    if (!out_inoutEnum4.value.equals(out_inoutEnum4_tmp.value)) {
      TestUtil.logErr("compare_data failed for Long - expected "
          + out_inoutEnum4_tmp.value + ",  received: " + out_inoutEnum4.value);
      pass = false;
    }
    if (!out_inoutEnum5.value.equals(out_inoutEnum5_tmp.value)) {
      TestUtil.logErr("compare_data failed for Short - expected "
          + out_inoutEnum5_tmp.value + ",  received: " + out_inoutEnum5.value);
      pass = false;
    }
    if (!out_inoutEnum6.value.equals(out_inoutEnum6_tmp.value)) {
      TestUtil.logErr("compare_data failed for BigDecimal - expected "
          + out_inoutEnum6_tmp.value + ",  received: " + out_inoutEnum6.value);
      pass = false;
    }
    if (!out_inoutEnum7.value.equals(out_inoutEnum7_tmp.value)) {
      TestUtil.logErr("compare_data failed for Float - expected "
          + out_inoutEnum7_tmp.value + ",  received: " + out_inoutEnum7.value);
      pass = false;
    }
    if (!out_inoutEnum8.value.equals(out_inoutEnum8_tmp.value)) {
      TestUtil.logErr("compare_data failed for Double - expected "
          + out_inoutEnum8_tmp.value + ",  received: " + out_inoutEnum8.value);
      pass = false;
    }
    if (!out_inoutEnum9.value.equals(out_inoutEnum9_tmp.value)) {
      TestUtil.logErr("compare_data failed for Byte - expected "
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
    if (!JAXWS_Data.compareXMLGregorianCalendars(inStruct.getVarDateTime(),
        inStruct_tmp.getVarDateTime())) {
      TestUtil.logErr("compare_data failed for XMLGregorianCalendar - expected "
          + inStruct_tmp.getVarDateTime() + ",  received: "
          + inStruct.getVarDateTime());
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(inStruct.getVarBase64Binary(),
        inStruct_tmp.getVarBase64Binary(), "byte")) {
      TestUtil.logErr("compare_data failed for base64binary - expected "
          + inStruct_tmp.getVarBase64Binary() + ",  received: "
          + inStruct.getVarBase64Binary());
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(inStruct.getVarHexBinary(),
        inStruct_tmp.getVarHexBinary(), "byte")) {
      TestUtil.logErr("compare_data failed for hexbinary - expected "
          + inStruct_tmp.getVarHexBinary() + ",  received: "
          + inStruct.getVarHexBinary());
      pass = false;
    }

    return pass;
  }

  private boolean compare_out_inoutarray_data() {
    TestUtil.logMsg("Comparing IN/INOUT ARRAY data");
    boolean pass = true;
    if (!JAXWS_Data.compareArrayValues(
        out_inoutarray1_tmp.value.getArrayOfstring(),
        out_inoutarray1.value.getArrayOfstring(), "String")) {
      TestUtil.logMsg("String array data miscompare");
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(
        out_inoutarray2_tmp.value.getArrayOfinteger(),
        out_inoutarray2.value.getArrayOfinteger(), "BigInteger")) {
      TestUtil.logMsg("BigInteger array data miscompare");
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(
        out_inoutarray3_tmp.value.getArrayOfint(),
        out_inoutarray3.value.getArrayOfint(), "int")) {
      TestUtil.logMsg("int array data miscompare");
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(
        out_inoutarray4_tmp.value.getArrayOflong(),
        out_inoutarray4.value.getArrayOflong(), "long")) {
      TestUtil.logMsg("long array data miscompare");
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(
        out_inoutarray5_tmp.value.getArrayOfshort(),
        out_inoutarray5.value.getArrayOfshort(), "short")) {
      TestUtil.logMsg("short array data miscompare");
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(
        out_inoutarray6_tmp.value.getArrayOfdecimal(),
        out_inoutarray6.value.getArrayOfdecimal(), "BigDecimal")) {
      TestUtil.logMsg("BigDecimal array data miscompare");
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(
        out_inoutarray7_tmp.value.getArrayOffloat(),
        out_inoutarray7.value.getArrayOffloat(), "float")) {
      TestUtil.logMsg("float array data miscompare");
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(
        out_inoutarray8_tmp.value.getArrayOfdouble(),
        out_inoutarray8.value.getArrayOfdouble(), "double")) {
      TestUtil.logMsg("double array data miscompare");
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(
        out_inoutarray9_tmp.value.getArrayOfboolean(),
        out_inoutarray9.value.getArrayOfboolean(), "boolean")) {
      TestUtil.logMsg("boolean array data miscompare");
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(
        out_inoutarray10_tmp.value.getArrayOfbyte(),
        out_inoutarray10.value.getArrayOfbyte(), "byte")) {
      TestUtil.logMsg("byte array data miscompare");
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(
        out_inoutarray11_tmp.value.getArrayOfQName(),
        out_inoutarray11.value.getArrayOfQName(), "QName")) {
      TestUtil.logMsg("QNameBigDecimal array data miscompare");
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(
        out_inoutarray12_tmp.value.getArrayOfdateTime(),
        out_inoutarray12.value.getArrayOfdateTime(), "XMLGregorianCalendar")) {
      TestUtil.logMsg("XMLGregorianCalendar array data miscompare");
      pass = false;
    }
    return pass;
  }

  /*
   * @testName: InTest
   *
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
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
      TestUtil.logMsg("Comparing expected data");
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
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
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
      Holder out = new Holder("String1");
      Holder out_tmp = new Holder("String4");
      TestUtil.logMsg("Marshalling output data");
      port.echoOut(out);
      TestUtil.logMsg("Comparing expected data");
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
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
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
      Holder inout = new Holder("String1");
      Holder inout_tmp = new Holder("String4");
      TestUtil.logMsg("Marshalling input/output data");
      port.echoInOut(inout);
      TestUtil.logMsg("Comparing expected data");
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
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
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
      Holder inout = new Holder("String1");
      Holder inout_tmp = new Holder("String4");
      TestUtil.logMsg("Marshalling input/output data");
      port.echoInOut(inout);
      TestUtil.logMsg("Comparing expected data");
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
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
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
      TestUtil.logMsg("Marshalling input/output data");
      String inout = "String1";
      String inout_tmp = "String4";
      String result = port.echoInOut3(inout);
      TestUtil.logMsg("Comparing expected data");
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
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
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
      TestUtil.logMsg("Marshalling input/output data");
      String inout = "String1";
      String inout_tmp = "String4";
      String result = port.echoInOut4(inout);
      TestUtil.logMsg("Comparing expected data");
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
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
   * WSI:SPEC:R2302;
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
      Holder out = new Holder("String2");
      Holder out_tmp = new Holder("String3");
      Holder inout = new Holder("String4");
      Holder inout_tmp = new Holder("String5");
      TestUtil.logMsg("Marshalling input/output data");
      port.echoMix(in, inout, out);
      TestUtil.logMsg("Comparing expected data");
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
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
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
          in11, in12, in13, in14);
      TestUtil.logMsg("Comparing expected data");
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
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
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
          out_inout10, out_inout11, out_inout12, out_inout13, out_inout14);
      TestUtil.logMsg("Comparing expected data");

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
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
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
      TestUtil.logMsg("Marshalling input/output data");
      port.echoInOutSimpleTypes(out_inout1, out_inout2, out_inout3, out_inout4,
          out_inout5, out_inout6, out_inout7, out_inout8, out_inout9,
          out_inout10, out_inout11, out_inout12, out_inout13, out_inout14);
      TestUtil.logMsg("Comparing expected data");
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
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
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
      TestUtil.logMsg("Comparing expected data");
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
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
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
      TestUtil.logMsg("Marshalling output data");
      port.echoOutEnum(out_inoutEnum1, out_inoutEnum2, out_inoutEnum3,
          out_inoutEnum4, out_inoutEnum5, out_inoutEnum6, out_inoutEnum7,
          out_inoutEnum8, out_inoutEnum9);
      TestUtil.logMsg("Comparing expected data");
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
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
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
      TestUtil.logMsg("Marshalling input/output data");
      port.echoInOutEnum(out_inoutEnum1, out_inoutEnum2, out_inoutEnum3,
          out_inoutEnum4, out_inoutEnum5, out_inoutEnum6, out_inoutEnum7,
          out_inoutEnum8, out_inoutEnum9);
      TestUtil.logMsg("Comparing expected data");
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
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
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
      TestUtil.logMsg("Comparing expected data");
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
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
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
    initialize_saved_out_inout_SimpleTypeArray_data();
    try {
      TestUtil.logMsg("Marshalling input/output data");
      port.echoInOutSimpleTypesArray(out_inoutarray1, out_inoutarray2,
          out_inoutarray3, out_inoutarray4, out_inoutarray5, out_inoutarray6,
          out_inoutarray7, out_inoutarray8, out_inoutarray9, out_inoutarray10,
          out_inoutarray11, out_inoutarray12);
      TestUtil.logMsg("Comparing expected data");
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
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
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
    Book b1 = new Book();
    b1.setAuthor("author1");
    b1.setTitle("title1");
    b1.setIsbn(1);
    Holder<Book> out_inout_b = new Holder(b0);

    try {
      TestUtil.logMsg("Marshalling input/output data");
      TestUtil.logMsg("Data In: [author0|title0|0]");
      port.echoInOutBook(out_inout_b);
      TestUtil.logMsg("Comparing expected data");
      Book b = out_inout_b.value;
      TestUtil.logMsg("Data Out: [" + b.getAuthor() + "|" + b.getTitle() + "|"
          + b.getIsbn() + "]");
      if (!b.getAuthor().equals(b1.getAuthor())
          || !b.getTitle().equals(b1.getTitle())
          || b.getIsbn() != b1.getIsbn()) {
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
   * @assertion_ids: JAXWS:SPEC:2030; WS4EE:SPEC:35; WS4EE:SPEC:36;
   * WS4EE:SPEC:4000; WS4EE:SPEC:4002; WS4EE:SPEC:5000; WS4EE:SPEC:5002;
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
    ArrayOfBook ab = new ArrayOfBook();
    ab.getArrayOfBook().add(b0);
    ab.getArrayOfBook().add(b1);
    Holder<ArrayOfBook> out_inout_ab = new Holder(ab);
    Book expected[] = new Book[2];
    expected[0] = b1;
    expected[1] = b0;

    try {
      TestUtil.logMsg("Marshalling input/output data");
      TestUtil.logMsg("Data In: [author0|title0|0," + "author1|title1|1]");
      port.echoInOutBookArray(out_inout_ab);
      TestUtil.logMsg("Comparing expected data");
      List<Book> lb = out_inout_ab.value.getArrayOfBook();
      if (lb.size() != 2) {
        TestUtil.logErr("List size are not equal ");
        TestUtil.logErr("  expected a list size of 2");
        TestUtil.logErr("  actual list size = " + lb.size());
        pass = false;
      }
      Book[] result = lb.toArray(new Book[lb.size()]);
      for (int i = 0; i < result.length; i++) {
        if (!result[i].getAuthor().equals(expected[i].getAuthor())
            || !result[i].getTitle().equals(expected[i].getTitle())
            || result[i].getIsbn() != expected[i].getIsbn()) {
          pass = false;
          TestUtil.logErr("Expected result: [" + expected[i].getAuthor() + "|"
              + expected[i].getTitle() + "|" + expected[i].getIsbn() + "]");
          TestUtil.logErr("Actual result: [" + result[i].getAuthor() + "|"
              + result[i].getTitle() + "|" + result[i].getIsbn() + "]");
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
