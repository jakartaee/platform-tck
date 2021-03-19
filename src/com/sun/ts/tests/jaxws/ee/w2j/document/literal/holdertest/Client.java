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

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.Status;
import com.sun.ts.tests.jaxws.common.*;
import com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.*;

import java.io.*;
import java.net.*;
import java.rmi.*;
import java.util.Properties;
import java.util.List;

import java.math.BigInteger;
import java.math.BigDecimal;

import jakarta.xml.ws.*;
import jakarta.xml.ws.Holder;

import javax.xml.namespace.QName;
import javax.xml.datatype.*;
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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.";

  // service and port information
  private static final String NAMESPACEURI = "http://holdertest.org/wsdl";

  private static final String SERVICE_NAME = "HolderTestService";

  private static final String PORT_NAME = "HolderTestPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  Holder<StringElement> inout1, inout1_tmp;

  Holder<IntegerElement> inout2, inout2_tmp;

  Holder<IntElement> inout3, inout3_tmp;

  Holder<LongElement> inout4, inout4_tmp;

  Holder<ShortElement> inout5, inout5_tmp;

  Holder<DecimalElement> inout6, inout6_tmp;

  Holder<FloatElement> inout7, inout7_tmp;

  Holder<DoubleElement> inout8, inout8_tmp;

  Holder<BooleanElement> inout9, inout9_tmp;

  Holder<ByteElement> inout10, inout10_tmp;

  Holder<QNameElement> inout11, inout11_tmp;

  Holder<DateTimeElement> inout12, inout12_tmp;

  Holder<ArrayOfString> inoutarray1;

  Holder<ArrayOfInteger> inoutarray2;

  Holder<ArrayOfInt> inoutarray3;

  Holder<ArrayOfLong> inoutarray4;

  Holder<ArrayOfShort> inoutarray5;

  Holder<ArrayOfDecimal> inoutarray6;

  Holder<ArrayOfFloat> inoutarray7;

  Holder<ArrayOfDouble> inoutarray8;

  Holder<ArrayOfBoolean> inoutarray9;

  Holder<ArrayOfByte> inoutarray10;

  Holder<ArrayOfQName> inoutarray11;

  Holder<ArrayOfDateTime> inoutarray12;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "w2jdlholdertest.endpoint.1";

  private static final String WSDLLOC_URL = "w2jdlholdertest.wsdlloc.1";

  private String url = null;

  private URL wsdlurl = null;

  private static DatatypeFactory dtfactory = null;

  HolderTest port = null;

  static HolderTestService service = null;

  static {
    try {
      dtfactory = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) {
      TestUtil.logMsg("Could not configure DatatypeFactory object");
      TestUtil.printStackTrace(e);
    }
  }

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
    port = (HolderTest) JAXWS_Util.getPort(wsdlurl, SERVICE_QNAME,
        HolderTestService.class, PORT_QNAME, HolderTest.class);
    JAXWS_Util.setTargetEndpointAddress(port, url);
  }

  private void getPortJavaEE() throws Exception {
    TestUtil.logMsg("Obtain service via WebServiceRef annotation");
    TestUtil.logMsg("service=" + service);
    port = (HolderTest) service.getHolderTestPort();
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
        service = (HolderTestService) getSharedObject();
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

  private boolean printTestStatus(boolean pass, String test) {
    if (pass)
      TestUtil.logMsg("" + test + " ... PASSED");
    else
      TestUtil.logErr("" + test + " ... FAILED");

    return pass;
  }

  private void initialize_inout_SimpleType_data() {
    inout1 = new Holder<StringElement>();
    inout2 = new Holder<IntegerElement>();
    inout3 = new Holder<IntElement>();
    inout4 = new Holder<LongElement>();
    inout5 = new Holder<ShortElement>();
    inout6 = new Holder<DecimalElement>();
    inout7 = new Holder<FloatElement>();
    inout8 = new Holder<DoubleElement>();
    inout9 = new Holder<BooleanElement>();
    inout10 = new Holder<ByteElement>();
    inout11 = new Holder<QNameElement>();
    inout12 = new Holder<DateTimeElement>();

    inout1.value = new StringElement();
    inout2.value = new IntegerElement();
    inout3.value = new IntElement();
    inout4.value = new LongElement();
    inout5.value = new ShortElement();
    inout6.value = new DecimalElement();
    inout7.value = new FloatElement();
    inout8.value = new DoubleElement();
    inout9.value = new BooleanElement();
    inout10.value = new ByteElement();
    inout11.value = new QNameElement();
    inout12.value = new DateTimeElement();

    inout1.value.setString("String1");
    inout2.value.setInteger(new BigInteger("3512359"));
    inout3.value.setInt(Integer.MIN_VALUE);
    inout4.value.setLong(Long.MIN_VALUE);
    inout5.value.setShort(Short.MIN_VALUE);
    inout6.value.setDecimal(new BigDecimal("3512359.1456"));
    inout7.value.setFloat(Float.MIN_VALUE);
    inout8.value.setDouble(Double.MIN_VALUE);
    inout9.value.setBoolean(false);
    inout10.value.setByte(Byte.MIN_VALUE);
    inout11.value.setQName(new QName("String2"));
    inout12.value.setDateTime(
        dtfactory.newXMLGregorianCalendar(96, 5, 1, 0, 30, 0, 0, 0));

  }

  private void initialize_inout_SimpleTypeArray_data() throws Exception {
    inoutarray1 = new Holder<ArrayOfString>();
    inoutarray2 = new Holder<ArrayOfInteger>();
    inoutarray3 = new Holder<ArrayOfInt>();
    inoutarray4 = new Holder<ArrayOfLong>();
    inoutarray5 = new Holder<ArrayOfShort>();
    inoutarray6 = new Holder<ArrayOfDecimal>();
    inoutarray7 = new Holder<ArrayOfFloat>();
    inoutarray8 = new Holder<ArrayOfDouble>();
    inoutarray9 = new Holder<ArrayOfBoolean>();
    inoutarray10 = new Holder<ArrayOfByte>();
    inoutarray11 = new Holder<ArrayOfQName>();
    inoutarray12 = new Holder<ArrayOfDateTime>();

    inoutarray1.value = new ArrayOfString();
    inoutarray2.value = new ArrayOfInteger();
    inoutarray3.value = new ArrayOfInt();
    inoutarray4.value = new ArrayOfLong();
    inoutarray5.value = new ArrayOfShort();
    inoutarray6.value = new ArrayOfDecimal();
    inoutarray7.value = new ArrayOfFloat();
    inoutarray8.value = new ArrayOfDouble();
    inoutarray9.value = new ArrayOfBoolean();
    inoutarray10.value = new ArrayOfByte();
    inoutarray11.value = new ArrayOfQName();
    inoutarray12.value = new ArrayOfDateTime();

    for (int i = 0; i < JAXWS_Data.String_nonull_data.length; i++)
      inoutarray1.value.getArrayOfString()
          .add(JAXWS_Data.String_nonull_data[i]);
    for (int i = 0; i < JAXWS_Data.BigInteger_nonull_data.length; i++)
      inoutarray2.value.getArrayOfInteger()
          .add(JAXWS_Data.BigInteger_nonull_data[i]);
    for (int i = 0; i < JAXWS_Data.int_data.length; i++)
      inoutarray3.value.getArrayOfInt().add(JAXWS_Data.Integer_nonull_data[i]);
    for (int i = 0; i < JAXWS_Data.long_data.length; i++)
      inoutarray4.value.getArrayOfLong().add(JAXWS_Data.Long_nonull_data[i]);
    for (int i = 0; i < JAXWS_Data.short_data.length; i++)
      inoutarray5.value.getArrayOfShort().add(JAXWS_Data.Short_nonull_data[i]);
    for (int i = 0; i < JAXWS_Data.BigDecimal_nonull_data.length; i++)
      inoutarray6.value.getArrayOfDecimal()
          .add(JAXWS_Data.BigDecimal_nonull_data[i]);
    for (int i = 0; i < JAXWS_Data.float_data.length; i++)
      inoutarray7.value.getArrayOfFloat().add(JAXWS_Data.Float_nonull_data[i]);
    for (int i = 0; i < JAXWS_Data.double_data.length; i++)
      inoutarray8.value.getArrayOfDouble()
          .add(JAXWS_Data.Double_nonull_data[i]);
    for (int i = 0; i < JAXWS_Data.boolean_data.length; i++)
      inoutarray9.value.getArrayOfBoolean()
          .add(JAXWS_Data.Boolean_nonull_data[i]);
    for (int i = 0; i < JAXWS_Data.byte_data.length; i++)
      inoutarray10.value.getArrayOfByte().add(JAXWS_Data.Byte_nonull_data[i]);
    for (int i = 0; i < JAXWS_Data.QName_nonull_data.length; i++)
      inoutarray11.value.getArrayOfQName().add(JAXWS_Data.QName_nonull_data[i]);
    for (int i = 0; i < JAXWS_Data.XMLGregorianCalendar_nonull_data.length; i++)
      inoutarray12.value.getArrayOfDateTime()
          .add(JAXWS_Data.XMLGregorianCalendar_nonull_data[i]);

    TestUtil.logTrace("inoutarray1.value.getArrayOfString().size()="
        + inoutarray1.value.getArrayOfString().size());
    TestUtil.logTrace("inoutarray2.value.getArrayOfInteger().size()="
        + inoutarray2.value.getArrayOfInteger().size());
    TestUtil.logTrace("inoutarray3.value.getArrayOfInt().size()="
        + inoutarray3.value.getArrayOfInt().size());
    TestUtil.logTrace("inoutarray4.value.getArrayOfLong().size()="
        + inoutarray4.value.getArrayOfLong().size());
    TestUtil.logTrace("inoutarray5.value.getArrayOfShort().size()="
        + inoutarray5.value.getArrayOfShort().size());
    TestUtil.logTrace("inoutarray6.value.getArrayOfDecimal().size()="
        + inoutarray6.value.getArrayOfDecimal().size());
    TestUtil.logTrace("inoutarray7.value.getArrayOfFloat().size()="
        + inoutarray7.value.getArrayOfFloat().size());
    TestUtil.logTrace("inoutarray8.value.getArrayOfDouble().size()="
        + inoutarray8.value.getArrayOfDouble().size());
    TestUtil.logTrace("inoutarray9.value.getArrayOfBoolean().size()="
        + inoutarray9.value.getArrayOfBoolean().size());
    TestUtil.logTrace("inoutarray10.value.getArrayOfByte().size()="
        + inoutarray10.value.getArrayOfByte().size());
    TestUtil.logTrace("inoutarray11.value.getArrayOfQName().size()="
        + inoutarray11.value.getArrayOfQName().size());
    TestUtil.logTrace("inoutarray12.value.getArrayOfDateTime().size()="
        + inoutarray12.value.getArrayOfDateTime().size());
  }

  private void initialize_saved_inout_SimpleType_data() {
    inout1_tmp = new Holder<StringElement>();
    inout2_tmp = new Holder<IntegerElement>();
    inout3_tmp = new Holder<IntElement>();
    inout4_tmp = new Holder<LongElement>();
    inout5_tmp = new Holder<ShortElement>();
    inout6_tmp = new Holder<DecimalElement>();
    inout7_tmp = new Holder<FloatElement>();
    inout8_tmp = new Holder<DoubleElement>();
    inout9_tmp = new Holder<BooleanElement>();
    inout10_tmp = new Holder<ByteElement>();
    inout11_tmp = new Holder<QNameElement>();
    inout12_tmp = new Holder<DateTimeElement>();

    inout1_tmp.value = new StringElement();
    inout2_tmp.value = new IntegerElement();
    inout3_tmp.value = new IntElement();
    inout4_tmp.value = new LongElement();
    inout5_tmp.value = new ShortElement();
    inout6_tmp.value = new DecimalElement();
    inout7_tmp.value = new FloatElement();
    inout8_tmp.value = new DoubleElement();
    inout9_tmp.value = new BooleanElement();
    inout10_tmp.value = new ByteElement();
    inout11_tmp.value = new QNameElement();
    inout12_tmp.value = new DateTimeElement();

    inout1_tmp.value.setString("String4");
    inout2_tmp.value.setInteger(new BigInteger("3512360"));
    inout3_tmp.value.setInt(Integer.MAX_VALUE);
    inout4_tmp.value.setLong(Long.MAX_VALUE);
    inout5_tmp.value.setShort(Short.MAX_VALUE);
    inout6_tmp.value.setDecimal(new BigDecimal("3512360.1456"));
    inout7_tmp.value.setFloat(Float.MAX_VALUE);
    inout8_tmp.value.setDouble(Double.MAX_VALUE);
    inout9_tmp.value.setBoolean(true);
    inout10_tmp.value.setByte(Byte.MAX_VALUE);
    inout11_tmp.value.setQName(new QName("String5"));
    inout12_tmp.value.setDateTime(
        dtfactory.newXMLGregorianCalendar(96, 5, 2, 0, 30, 0, 0, 0));
  }

  private boolean compare_inout_data() {
    TestUtil.logMsg("Comparing IN/INOUT data");
    boolean pass = true;
    if (!inout1.value.getString().equals(inout1_tmp.value.getString())) {
      TestUtil.logErr("compare_data failed for StringHolder - expected "
          + inout1_tmp.value.getString() + ",  received: "
          + inout1.value.getString());
      pass = false;
    }
    if (!inout2.value.getInteger().equals(inout2_tmp.value.getInteger())) {
      TestUtil.logErr("compare_data failed for BigIntegerHolder - expected "
          + inout2_tmp.value.getInteger() + ",  received: "
          + inout2.value.getInteger());
      pass = false;
    }
    if (inout3.value.getInt() != inout3_tmp.value.getInt()) {
      TestUtil.logErr("compare_data failed for IntHolder - expected "
          + inout3_tmp.value.getInt() + ",  received: "
          + inout3.value.getInt());
      pass = false;
    }
    if (inout4.value.getLong() != inout4_tmp.value.getLong()) {
      TestUtil.logErr("compare_data failed for LongHolder - expected "
          + inout4_tmp.value.getLong() + ",  received: "
          + inout4.value.getLong());
      pass = false;
    }
    if (inout5.value.getShort() != inout5_tmp.value.getShort()) {
      TestUtil.logErr("compare_data failed for ShortHolder - expected "
          + inout5_tmp.value.getShort() + ",  received: "
          + inout5.value.getShort());
      pass = false;
    }
    if (!inout6.value.getDecimal().equals(inout6_tmp.value.getDecimal())) {
      TestUtil.logErr("compare_data failed for BigDecimalHolder - expected "
          + inout6_tmp.value.getDecimal() + ",  received: "
          + inout6.value.getDecimal());
      pass = false;
    }
    if (inout7.value.getFloat() != inout7_tmp.value.getFloat()) {
      TestUtil.logErr("compare_data failed for FloatHolder - expected "
          + inout7_tmp.value.getFloat() + ",  received: "
          + inout7.value.getFloat());
      pass = false;
    }
    if (inout8.value.getDouble() != inout8_tmp.value.getDouble()) {
      TestUtil.logErr("compare_data failed for DoubleHolder - expected "
          + inout8_tmp.value.getDouble() + ",  received: "
          + inout8.value.getDouble());
      pass = false;
    }
    if (inout9.value.isBoolean() != inout9_tmp.value.isBoolean()) {
      TestUtil.logErr("compare_data failed for BooleanHolder - expected "
          + inout9_tmp.value.isBoolean() + ",  received: "
          + inout9.value.isBoolean());
      pass = false;
    }
    if (inout10.value.getByte() != inout10_tmp.value.getByte()) {
      TestUtil.logErr("compare_data failed for ByteHolder - expected "
          + inout10_tmp.value.getByte() + ",  received: "
          + inout10.value.getByte());
      pass = false;
    }
    if (!inout11.value.getQName().equals(inout11_tmp.value.getQName())) {
      TestUtil.logErr("compare_data failed for QNameHolder - expected "
          + inout11_tmp.value.getQName() + ",  received: "
          + inout11.value.getQName());
      pass = false;
    }
    if (!JAXWS_Data.compareXMLGregorianCalendars(inout12.value.getDateTime(),
        inout12_tmp.value.getDateTime())) {
      TestUtil.logErr(
          "compare_data failed for XMLGregorianCalendarHolder - expected "
              + inout12_tmp.value.getDateTime() + ",  received: "
              + inout12.value.getDateTime());
      pass = false;
    }
    return pass;
  }

  private boolean compare_inoutarray_data() {
    TestUtil.logMsg("Comparing IN/INOUT ARRAY data");
    boolean pass = true;

    TestUtil.logTrace("inoutarray1.value.getArrayOfString().size()="
        + inoutarray1.value.getArrayOfString().size());
    TestUtil.logTrace("inoutarray2.value.getArrayOfInteger().size()="
        + inoutarray2.value.getArrayOfInteger().size());
    TestUtil.logTrace("inoutarray3.value.getArrayOfInt().size()="
        + inoutarray3.value.getArrayOfInt().size());
    TestUtil.logTrace("inoutarray4.value.getArrayOfLong().size()="
        + inoutarray4.value.getArrayOfLong().size());
    TestUtil.logTrace("inoutarray5.value.getArrayOfShort().size()="
        + inoutarray5.value.getArrayOfShort().size());
    TestUtil.logTrace("inoutarray6.value.getArrayOfDecimal().size()="
        + inoutarray6.value.getArrayOfDecimal().size());
    TestUtil.logTrace("inoutarray7.value.getArrayOfFloat().size()="
        + inoutarray7.value.getArrayOfFloat().size());
    TestUtil.logTrace("inoutarray8.value.getArrayOfDouble().size()="
        + inoutarray8.value.getArrayOfDouble().size());
    TestUtil.logTrace("inoutarray9.value.getArrayOfBoolean().size()="
        + inoutarray9.value.getArrayOfBoolean().size());
    TestUtil.logTrace("inoutarray10.value.getArrayOfByte().size()="
        + inoutarray10.value.getArrayOfByte().size());
    TestUtil.logTrace("inoutarray11.value.getArrayOfQName().size()="
        + inoutarray11.value.getArrayOfQName().size());
    TestUtil.logTrace("inoutarray12.value.getArrayOfDateTime().size()="
        + inoutarray12.value.getArrayOfDateTime().size());

    String[] strArray = new String[inoutarray1.value.getArrayOfString().size()];
    for (int i = 0, j = inoutarray1.value.getArrayOfString().size()
        - 1; j >= 0; i++, j--)
      strArray[i] = inoutarray1.value.getArrayOfString().get(j);
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.String_nonull_data, strArray,
        "String")) {
      TestUtil.logErr("String array data miscompare");
      pass = false;
    }

    BigInteger[] bigintArray = new BigInteger[inoutarray2.value
        .getArrayOfInteger().size()];
    for (int i = 0, j = inoutarray2.value.getArrayOfInteger().size()
        - 1; j >= 0; i++, j--)
      bigintArray[i] = inoutarray2.value.getArrayOfInteger().get(j);
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.BigInteger_nonull_data,
        bigintArray, "BigInteger")) {
      TestUtil.logErr("BigInteger array data miscompare");
      pass = false;
    }

    int[] intArray = new int[inoutarray3.value.getArrayOfInt().size()];
    for (int i = 0, j = inoutarray3.value.getArrayOfInt().size()
        - 1; j >= 0; i++, j--)
      intArray[i] = inoutarray3.value.getArrayOfInt().get(j).intValue();
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.int_data, intArray, "int")) {
      TestUtil.logErr("int array data miscompare");
      pass = false;
    }

    long[] longArray = new long[inoutarray4.value.getArrayOfLong().size()];
    for (int i = 0, j = inoutarray4.value.getArrayOfLong().size()
        - 1; j >= 0; i++, j--)
      longArray[i] = inoutarray4.value.getArrayOfLong().get(j).longValue();
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.long_data, longArray,
        "long")) {
      TestUtil.logErr("long array data miscompare");
      pass = false;
    }

    short[] shortArray = new short[inoutarray5.value.getArrayOfShort().size()];
    for (int i = 0, j = inoutarray5.value.getArrayOfShort().size()
        - 1; j >= 0; i++, j--)
      shortArray[i] = inoutarray5.value.getArrayOfShort().get(j).shortValue();
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.short_data, shortArray,
        "short")) {
      TestUtil.logErr("short array data miscompare");
      pass = false;
    }

    BigDecimal[] bigdecArray = new BigDecimal[inoutarray6.value
        .getArrayOfDecimal().size()];
    for (int i = 0, j = inoutarray6.value.getArrayOfDecimal().size()
        - 1; j >= 0; i++, j--)
      bigdecArray[i] = inoutarray6.value.getArrayOfDecimal().get(j);
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.BigDecimal_nonull_data,
        bigdecArray, "BigDecimal")) {
      TestUtil.logErr("BigDecimal array data miscompare");
      pass = false;
    }

    float[] floatArray = new float[inoutarray7.value.getArrayOfFloat().size()];
    for (int i = 0, j = inoutarray7.value.getArrayOfFloat().size()
        - 1; j >= 0; i++, j--)
      floatArray[i] = inoutarray7.value.getArrayOfFloat().get(j).floatValue();
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.float_data, floatArray,
        "float")) {
      TestUtil.logErr("float array data miscompare");
      pass = false;
    }

    double[] doubleArray = new double[inoutarray8.value.getArrayOfDouble()
        .size()];
    for (int i = 0, j = inoutarray8.value.getArrayOfDouble().size()
        - 1; j >= 0; i++, j--)
      doubleArray[i] = inoutarray8.value.getArrayOfDouble().get(j)
          .doubleValue();
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.double_data, doubleArray,
        "double")) {
      TestUtil.logErr("double array data miscompare");
      pass = false;
    }

    boolean[] booleanArray = new boolean[inoutarray9.value.getArrayOfBoolean()
        .size()];
    for (int i = 0, j = inoutarray9.value.getArrayOfBoolean().size()
        - 1; j >= 0; i++, j--)
      booleanArray[i] = inoutarray9.value.getArrayOfBoolean().get(j)
          .booleanValue();
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.boolean_data, booleanArray,
        "boolean")) {
      TestUtil.logErr("boolean array data miscompare");
      pass = false;
    }

    byte[] byteArray = new byte[inoutarray10.value.getArrayOfByte().size()];
    for (int i = 0, j = inoutarray10.value.getArrayOfByte().size()
        - 1; j >= 0; i++, j--)
      byteArray[i] = inoutarray10.value.getArrayOfByte().get(j).byteValue();
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.byte_data, byteArray,
        "byte")) {
      TestUtil.logErr("byte array data miscompare");
      pass = false;
    }

    QName[] qnameArray = new QName[inoutarray11.value.getArrayOfQName().size()];
    for (int i = 0, j = inoutarray11.value.getArrayOfQName().size()
        - 1; j >= 0; i++, j--)
      qnameArray[i] = inoutarray11.value.getArrayOfQName().get(j);
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.QName_nonull_data, qnameArray,
        "QName")) {
      TestUtil.logErr("QNameBigDecimal array data miscompare");
      pass = false;
    }

    XMLGregorianCalendar[] dateTimeArray = new XMLGregorianCalendar[inoutarray12.value
        .getArrayOfDateTime().size()];
    for (int i = 0, j = inoutarray12.value.getArrayOfDateTime().size()
        - 1; j >= 0; i++, j--)
      dateTimeArray[i] = inoutarray12.value.getArrayOfDateTime().get(j);
    if (!JAXWS_Data.compareArrayValues(
        JAXWS_Data.XMLGregorianCalendar_nonull_data, dateTimeArray,
        "XMLGregorianCalendar")) {
      TestUtil.logErr("XMLGregorianCalendar array data miscompare");
      pass = false;
    }
    return pass;
  }

  /*
   * @testName: InOutSimpleTypesTest
   *
   * @assertion_ids: JAXWS:SPEC:2031;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * type. For each type pass its value as input to the corresponding RPC method
   * and receive it back as the return value. Compare results of each value/type
   * of what was sent and what was returned. Verify they are equal.
   */
  public void InOutSimpleTypesTest() throws Fault {
    TestUtil.logTrace("InOutSimpleTypesTest");
    boolean pass = true;
    initialize_inout_SimpleType_data();
    initialize_saved_inout_SimpleType_data();
    try {
      TestUtil.logMsg("Marshalling input/output data");
      port.echoInOutStringTypes(inout1);
      port.echoInOutIntegerTypes(inout2);
      port.echoInOutIntTypes(inout3);
      port.echoInOutLongTypes(inout4);
      port.echoInOutShortTypes(inout5);
      port.echoInOutDecimalTypes(inout6);
      port.echoInOutFloatTypes(inout7);
      port.echoInOutDoubleTypes(inout8);
      port.echoInOutBooleanTypes(inout9);
      port.echoInOutByteTypes(inout10);
      port.echoInOutQNameTypes(inout11);
      port.echoInOutDateTimeTypes(inout12);
      if (!compare_inout_data()) {
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
   * @assertion_ids: JAXWS:SPEC:2031;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * array type. For each type pass its value as input to the corresponding RPC
   * method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void InOutSimpleTypesArrayTest() throws Fault {
    TestUtil.logTrace("InOutSimpleTypesArrayTest");
    boolean pass = true;
    try {
      initialize_inout_SimpleTypeArray_data();
      TestUtil.logMsg("Marshalling input/output data");
      port.echoInOutArrayStringTypes(inoutarray1);
      port.echoInOutArrayIntegerTypes(inoutarray2);
      port.echoInOutArrayIntTypes(inoutarray3);
      port.echoInOutArrayLongTypes(inoutarray4);
      port.echoInOutArrayShortTypes(inoutarray5);
      port.echoInOutArrayDecimalTypes(inoutarray6);
      port.echoInOutArrayFloatTypes(inoutarray7);
      port.echoInOutArrayDoubleTypes(inoutarray8);
      port.echoInOutArrayBooleanTypes(inoutarray9);
      port.echoInOutArrayByteTypes(inoutarray10);
      port.echoInOutArrayQNameTypes(inoutarray11);
      port.echoInOutArrayDateTimeTypes(inoutarray12);
      if (!compare_inoutarray_data()) {
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
   * @assertion_ids: JAXWS:SPEC:2031;
   *
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * user defined type. For each type pass its value as input to the
   * corresponding RPC method and receive it back as the return value. Compare
   * results of each value/type of what was sent and what was returned. Verify
   * they are equal.
   */
  public void InOutUserDefinedTypeTest() throws Fault {
    TestUtil.logTrace("InOutUserDefinedTypeTest");
    boolean pass = true;
    try {
      Book b0 = new Book();
      b0.setAuthor("author0");
      b0.setTitle("title0");
      b0.setIsbn(0);
      Holder<Book> inout_bh = new Holder<Book>();
      inout_bh.value = b0;
      Book expected = new Book();
      expected.setAuthor("author1");
      expected.setTitle("title1");
      expected.setIsbn(1);
      TestUtil.logMsg("Marshalling input/output data");
      TestUtil.logMsg("Data In: [author0|title0|0]");
      port.echoInOutBook(inout_bh);
      Book actual = inout_bh.value;
      TestUtil.logMsg("Data Out: [" + actual.getAuthor() + "|"
          + actual.getTitle() + "|" + actual.getIsbn() + "]");
      if (!actual.getAuthor().equals(expected.getAuthor())
          || !actual.getTitle().equals(expected.getTitle())
          || actual.getIsbn() != expected.getIsbn()) {
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
   * @assertion_ids: JAXWS:SPEC:2031;
   * 
   * @test_Strategy: Create a stub instance to our service definition interface,
   * set the target endpoint to the servlet, and invoke the RPC methods for each
   * array of user defined type. For each type pass its value as input to the
   * corresponding RPC method and receive it back as the return value. Compare
   * results of each value/type of what was sent and what was returned. Verify
   * they are equal.
   */
  public void InOutUserDefinedTypeArrayTest() throws Fault {
    TestUtil.logTrace("InOutUserDefinedTypeArrayTest");
    boolean pass = true;
    try {
      Book b0 = new Book();
      b0.setAuthor("author0");
      b0.setTitle("title0");
      b0.setIsbn(0);
      Book b1 = new Book();
      b1.setAuthor("author1");
      b1.setTitle("title1");
      b1.setIsbn(1);
      ArrayOfBook aob = new ArrayOfBook();
      aob.getBook().add(b0);
      aob.getBook().add(b1);
      Holder<ArrayOfBook> inout_bh = new Holder<ArrayOfBook>();
      inout_bh.value = aob;
      Book s[] = new Book[2];
      s[0] = b1;
      s[1] = b0;
      TestUtil.logMsg("Marshalling input/output data");
      TestUtil.logMsg("Data sent: [" + b0.getAuthor() + "|" + b0.getTitle()
          + "|" + b0.getIsbn() + "," + b1.getAuthor() + "|" + b1.getTitle()
          + "|" + b1.getIsbn() + "]");
      port.echoInOutBookArray(inout_bh);
      Book r[] = new Book[2];
      List<Book> bList = inout_bh.value.getBook();
      if (bList.size() != 2) {
        TestUtil.logErr("list size not equal 2");
        pass = false;
      } else {
        r[0] = bList.get(0);
        r[1] = bList.get(1);
        TestUtil.logMsg("Data received: [" + r[0].getAuthor() + "|"
            + r[0].getTitle() + "|" + r[0].getIsbn() + "," + r[1].getAuthor()
            + "|" + r[1].getTitle() + "|" + r[1].getIsbn() + "]");
        for (int i = 0; i < r.length; i++) {
          if (!r[i].getAuthor().equals(s[i].getAuthor())
              || !r[i].getTitle().equals(s[i].getTitle())
              || r[i].getIsbn() != s[i].getIsbn()) {
            TestUtil
                .logMsg("Expected: [" + s[i].getAuthor() + "|" + s[i].getTitle()
                    + "|" + s[i].getIsbn() + ", result:" + r[i].getAuthor()
                    + "|" + r[i].getTitle() + "|" + r[i].getIsbn());
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
