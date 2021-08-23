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

package com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.lib.harness.*;

import java.io.*;
import java.net.*;
import java.rmi.*;

import jakarta.xml.ws.*;

import java.util.Properties;

import java.math.BigInteger;
import java.math.BigDecimal;
import jakarta.xml.ws.Holder;

import javax.xml.namespace.QName;
import javax.xml.datatype.*;

import com.sun.javatest.Status;

import com.sun.ts.tests.jaxws.common.*;

import com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.*;

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

  private static final String PKG_NAME = "com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.holdertest.";

  // service and port information
  private static final String NAMESPACEURI = "http://holdertest.org/wsdl";

  private static final String SERVICE_NAME = "HolderTestService";

  private static final String PORT_NAME = "HolderTestPort";

  private QName SERVICE_QNAME = new QName(NAMESPACEURI, SERVICE_NAME);

  private QName PORT_QNAME = new QName(NAMESPACEURI, PORT_NAME);

  private TSURL ctsurl = new TSURL();

  private String hostname = HOSTNAME;

  private int portnum = PORTNUM;

  Holder<java.lang.String> inout1, inout1_tmp;

  Holder<java.math.BigInteger> inout2, inout2_tmp;

  Holder<java.lang.Integer> inout3, inout3_tmp;

  Holder<java.lang.Long> inout4, inout4_tmp;

  Holder<java.lang.Short> inout5, inout5_tmp;

  Holder<java.math.BigDecimal> inout6, inout6_tmp;

  Holder<java.lang.Float> inout7, inout7_tmp;

  Holder<java.lang.Double> inout8, inout8_tmp;

  Holder<java.lang.Boolean> inout9, inout9_tmp;

  Holder<java.lang.Byte> inout10, inout10_tmp;

  Holder<javax.xml.namespace.QName> inout11, inout11_tmp;

  Holder<javax.xml.datatype.XMLGregorianCalendar> inout12, inout12_tmp;

  Holder<ArrayOfstring> inoutarray1, inoutarray1_tmp;

  Holder<ArrayOfinteger> inoutarray2, inoutarray2_tmp;

  Holder<ArrayOfint> inoutarray3, inoutarray3_tmp;

  Holder<ArrayOflong> inoutarray4, inoutarray4_tmp;

  Holder<ArrayOfshort> inoutarray5, inoutarray5_tmp;

  Holder<ArrayOfdecimal> inoutarray6, inoutarray6_tmp;

  Holder<ArrayOffloat> inoutarray7, inoutarray7_tmp;

  Holder<ArrayOfdouble> inoutarray8, inoutarray8_tmp;

  Holder<ArrayOfboolean> inoutarray9, inoutarray9_tmp;

  Holder<ArrayOfbyte> inoutarray10, inoutarray10_tmp;

  Holder<ArrayOfQName> inoutarray11, inoutarray11_tmp;

  Holder<ArrayOfdateTime> inoutarray12, inoutarray12_tmp;

  // URL properties used by the test
  private static final String ENDPOINT_URL = "w2jrlholdertest.endpoint.1";

  private static final String WSDLLOC_URL = "w2jrlholdertest.wsdlloc.1";

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
    inout1 = new Holder<java.lang.String>();
    inout1_tmp = new Holder<java.lang.String>();
    inout2 = new Holder<java.math.BigInteger>();
    inout2_tmp = new Holder<java.math.BigInteger>();
    inout3 = new Holder<java.lang.Integer>();
    inout3_tmp = new Holder<java.lang.Integer>();
    inout4 = new Holder<java.lang.Long>();
    inout4_tmp = new Holder<java.lang.Long>();
    inout5 = new Holder<java.lang.Short>();
    inout5_tmp = new Holder<java.lang.Short>();
    inout6 = new Holder<java.math.BigDecimal>();
    inout6_tmp = new Holder<java.math.BigDecimal>();
    inout7 = new Holder<java.lang.Float>();
    inout7_tmp = new Holder<java.lang.Float>();
    inout8 = new Holder<java.lang.Double>();
    inout8_tmp = new Holder<java.lang.Double>();
    inout9 = new Holder<java.lang.Boolean>();
    inout9_tmp = new Holder<java.lang.Boolean>();
    inout10 = new Holder<java.lang.Byte>();
    inout10_tmp = new Holder<java.lang.Byte>();
    inout11 = new Holder<javax.xml.namespace.QName>();
    inout11_tmp = new Holder<javax.xml.namespace.QName>();
    inout12 = new Holder<javax.xml.datatype.XMLGregorianCalendar>();
    inout12_tmp = new Holder<javax.xml.datatype.XMLGregorianCalendar>();

    inout1.value = "String1";
    inout2.value = new BigInteger("3512359");
    inout3.value = new Integer(Integer.MIN_VALUE);
    inout4.value = new Long(Long.MIN_VALUE);
    inout5.value = new Short(Short.MIN_VALUE);
    inout6.value = new BigDecimal("3512359.1456");
    inout7.value = new Float(Float.MIN_VALUE);
    inout8.value = new Double(Double.MIN_VALUE);
    inout9.value = Boolean.FALSE;
    inout10.value = Byte.valueOf(Byte.MIN_VALUE);
    inout11.value = new QName("String2");
    inout12.value = dtfactory.newXMLGregorianCalendar(96, 5, 1, 0, 30, 0, 0, 0);
  }

  private void initialize_inout_SimpleTypeArray_data() throws Exception {
    inoutarray1 = new Holder<ArrayOfstring>();
    inoutarray2 = new Holder<ArrayOfinteger>();
    inoutarray3 = new Holder<ArrayOfint>();
    inoutarray4 = new Holder<ArrayOflong>();
    inoutarray5 = new Holder<ArrayOfshort>();
    inoutarray6 = new Holder<ArrayOfdecimal>();
    inoutarray7 = new Holder<ArrayOffloat>();
    inoutarray8 = new Holder<ArrayOfdouble>();
    inoutarray9 = new Holder<ArrayOfboolean>();
    inoutarray10 = new Holder<ArrayOfbyte>();
    inoutarray11 = new Holder<ArrayOfQName>();
    inoutarray12 = new Holder<ArrayOfdateTime>();

    inoutarray1.value = new ArrayOfstring();
    inoutarray2.value = new ArrayOfinteger();
    inoutarray3.value = new ArrayOfint();
    inoutarray4.value = new ArrayOflong();
    inoutarray5.value = new ArrayOfshort();
    inoutarray6.value = new ArrayOfdecimal();
    inoutarray7.value = new ArrayOffloat();
    inoutarray8.value = new ArrayOfdouble();
    inoutarray9.value = new ArrayOfboolean();
    inoutarray10.value = new ArrayOfbyte();
    inoutarray11.value = new ArrayOfQName();
    inoutarray12.value = new ArrayOfdateTime();

    for (int i = 0; i < JAXWS_Data.String_nonull_data.length; i++)
      inoutarray1.value.getArrayOfstring()
          .add(JAXWS_Data.String_nonull_data[i]);

    for (int i = 0; i < JAXWS_Data.BigInteger_nonull_data.length; i++)
      inoutarray2.value.getArrayOfinteger()
          .add(JAXWS_Data.BigInteger_nonull_data[i]);

    for (int i = 0; i < JAXWS_Data.Integer_nonull_data.length; i++)
      inoutarray3.value.getArrayOfint().add(JAXWS_Data.Integer_nonull_data[i]);

    for (int i = 0; i < JAXWS_Data.Long_nonull_data.length; i++)
      inoutarray4.value.getArrayOflong().add(JAXWS_Data.Long_nonull_data[i]);

    for (int i = 0; i < JAXWS_Data.Short_nonull_data.length; i++)
      inoutarray5.value.getArrayOfshort().add(JAXWS_Data.Short_nonull_data[i]);

    for (int i = 0; i < JAXWS_Data.BigDecimal_nonull_data.length; i++)
      inoutarray6.value.getArrayOfdecimal()
          .add(JAXWS_Data.BigDecimal_nonull_data[i]);

    for (int i = 0; i < JAXWS_Data.Float_nonull_data.length; i++)
      inoutarray7.value.getArrayOffloat().add(JAXWS_Data.Float_nonull_data[i]);

    for (int i = 0; i < JAXWS_Data.Double_nonull_data.length; i++)
      inoutarray8.value.getArrayOfdouble()
          .add(JAXWS_Data.Double_nonull_data[i]);

    for (int i = 0; i < JAXWS_Data.Boolean_nonull_data.length; i++)
      inoutarray9.value.getArrayOfboolean()
          .add(JAXWS_Data.Boolean_nonull_data[i]);

    for (int i = 0; i < JAXWS_Data.Byte_nonull_data.length; i++)
      inoutarray10.value.getArrayOfbyte().add(JAXWS_Data.Byte_nonull_data[i]);

    for (int i = 0; i < JAXWS_Data.QName_nonull_data.length; i++)
      inoutarray11.value.getArrayOfQName().add(JAXWS_Data.QName_nonull_data[i]);

    for (int i = 0; i < JAXWS_Data.XMLGregorianCalendar_nonull_data.length; i++)
      inoutarray12.value.getArrayOfdateTime()
          .add(JAXWS_Data.XMLGregorianCalendar_nonull_data[i]);

  }

  private void initialize_saved_inout_SimpleType_data() {
    inout1_tmp.value = "String4";
    inout2_tmp.value = new BigInteger("3512360");
    inout3_tmp.value = new Integer(Integer.MAX_VALUE);
    inout4_tmp.value = new Long(Long.MAX_VALUE);
    inout5_tmp.value = new Short(Short.MAX_VALUE);
    inout6_tmp.value = new BigDecimal("3512360.1456");
    inout7_tmp.value = new Float(Float.MAX_VALUE);
    inout8_tmp.value = new Double(Double.MAX_VALUE);
    inout9_tmp.value = Boolean.TRUE;
    inout10_tmp.value = Byte.valueOf(Byte.MAX_VALUE);
    inout11_tmp.value = new QName("String5");
    inout12_tmp.value = dtfactory.newXMLGregorianCalendar(96, 5, 2, 0, 30, 0, 0,
        0);

  }

  private void save_inout_SimpleType_data() {
    inout1_tmp = inout1;
    inout2_tmp = inout2;
    inout3_tmp = inout3;
    inout4_tmp = inout4;
    inout5_tmp = inout5;
    inout6_tmp = inout6;
    inout7_tmp = inout7;
    inout8_tmp = inout8;
    inout9_tmp = inout9;
    inout10_tmp = inout10;
    inout11_tmp = inout11;
    inout12_tmp = inout12;
  }

  private boolean compare_inout_data() {
    TestUtil.logMsg("Comparing IN/INOUT data");
    boolean pass = true;
    if (!inout1.value.equals(inout1_tmp.value)) {
      TestUtil.logErr("compare_data failed for StringHolder - expected "
          + inout1_tmp.value + ",  received: " + inout1.value);
      pass = false;
    }
    if (!inout2.value.equals(inout2_tmp.value)) {
      TestUtil.logErr("compare_data failed for BigIntegerHolder - expected "
          + inout2_tmp.value + ",  received: " + inout2.value);
      pass = false;
    }
    if (!inout3.value.equals(inout3_tmp.value)) {
      TestUtil.logErr("compare_data failed for IntHolder - expected "
          + inout3_tmp.value + ",  received: " + inout3.value);
      pass = false;
    }
    if (!inout4.value.equals(inout4_tmp.value)) {
      TestUtil.logErr("compare_data failed for LongHolder - expected "
          + inout4_tmp.value + ",  received: " + inout4.value);
      pass = false;
    }
    if (!inout5.value.equals(inout5_tmp.value)) {
      TestUtil.logErr("compare_data failed for ShortHolder - expected "
          + inout5_tmp.value + ",  received: " + inout5.value);
      pass = false;
    }
    if (!inout6.value.equals(inout6_tmp.value)) {
      TestUtil.logErr("compare_data failed for BigDecimalHolder - expected "
          + inout6_tmp.value + ",  received: " + inout6.value);
      pass = false;
    }
    if (!inout7.value.equals(inout7_tmp.value)) {
      TestUtil.logErr("compare_data failed for FloatHolder - expected "
          + inout7_tmp.value + ",  received: " + inout7.value);
      pass = false;
    }
    if (!inout8.value.equals(inout8_tmp.value)) {
      TestUtil.logErr("compare_data failed for DoubleHolder - expected "
          + inout8_tmp.value + ",  received: " + inout8.value);
      pass = false;
    }
    if (!inout9.value.equals(inout9_tmp.value)) {
      TestUtil.logErr("compare_data failed for BooleanHolder - expected "
          + inout9_tmp.value + ",  received: " + inout9.value);
      pass = false;
    }
    if (!inout10.value.equals(inout10_tmp.value)) {
      TestUtil.logErr("compare_data failed for ByteHolder - expected "
          + inout10_tmp.value + ",  received: " + inout10.value);
      pass = false;
    }
    if (!inout11.value.equals(inout11_tmp.value)) {
      TestUtil.logErr("compare_data failed for QNameHolder - expected "
          + inout11_tmp.value + ",  received: " + inout11.value);
      pass = false;
    }
    if (!JAXWS_Data.compareXMLGregorianCalendars(inout12.value,
        inout12_tmp.value)) {
      TestUtil.logErr(
          "compare_data failed for XMLGregorianCalendarHolder - expected "
              + inout12_tmp.value + ",  received: " + inout12.value);
      pass = false;
    }
    return pass;
  }

  private boolean compare_inoutarray_data() {
    TestUtil.logMsg("Comparing IN/INOUT ARRAY data");
    boolean pass = true;

    String[] strArray = new String[inoutarray1.value.getArrayOfstring().size()];
    for (int i = 0; i < inoutarray1.value.getArrayOfstring().size(); i++)
      strArray[i] = inoutarray1.value.getArrayOfstring().get(i);
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.String_nonull_data, strArray,
        "String")) {
      TestUtil.logErr("String array data miscompare");
      pass = false;
    }

    BigInteger[] bigintArray = new BigInteger[inoutarray2.value
        .getArrayOfinteger().size()];
    for (int i = 0; i < inoutarray2.value.getArrayOfinteger().size(); i++)
      bigintArray[i] = inoutarray2.value.getArrayOfinteger().get(i);
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.BigInteger_nonull_data,
        bigintArray, "BigInteger")) {
      TestUtil.logErr("BigInteger array data miscompare");
      pass = false;
    }

    int[] intArray = new int[inoutarray3.value.getArrayOfint().size()];
    for (int i = 0; i < inoutarray3.value.getArrayOfint().size(); i++)
      intArray[i] = inoutarray3.value.getArrayOfint().get(i).intValue();
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.int_data, intArray, "int")) {
      TestUtil.logErr("int array data miscompare");
      pass = false;
    }

    long[] longArray = new long[inoutarray4.value.getArrayOflong().size()];
    for (int i = 0; i < inoutarray4.value.getArrayOflong().size(); i++)
      longArray[i] = inoutarray4.value.getArrayOflong().get(i).longValue();
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.long_data, longArray,
        "long")) {
      TestUtil.logErr("long array data miscompare");
      pass = false;
    }

    short[] shortArray = new short[inoutarray5.value.getArrayOfshort().size()];
    for (int i = 0; i < inoutarray5.value.getArrayOfshort().size(); i++)
      shortArray[i] = inoutarray5.value.getArrayOfshort().get(i).shortValue();
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.short_data, shortArray,
        "short")) {
      TestUtil.logErr("short array data miscompare");
      pass = false;
    }

    BigDecimal[] bigdecArray = new BigDecimal[inoutarray6.value
        .getArrayOfdecimal().size()];
    for (int i = 0; i < inoutarray6.value.getArrayOfdecimal().size(); i++)
      bigdecArray[i] = inoutarray6.value.getArrayOfdecimal().get(i);
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.BigDecimal_nonull_data,
        bigdecArray, "BigDecimal")) {
      TestUtil.logErr("BigDecimal array data miscompare");
      pass = false;
    }

    float[] floatArray = new float[inoutarray7.value.getArrayOffloat().size()];
    for (int i = 0; i < inoutarray7.value.getArrayOffloat().size(); i++)
      floatArray[i] = inoutarray7.value.getArrayOffloat().get(i).floatValue();
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.float_data, floatArray,
        "float")) {
      TestUtil.logErr("float array data miscompare");
      pass = false;
    }

    double[] doubleArray = new double[inoutarray8.value.getArrayOfdouble()
        .size()];
    for (int i = 0; i < inoutarray8.value.getArrayOfdouble().size(); i++)
      doubleArray[i] = inoutarray8.value.getArrayOfdouble().get(i)
          .doubleValue();
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.double_data, doubleArray,
        "double")) {
      TestUtil.logErr("double array data miscompare");
      pass = false;
    }

    boolean[] booleanArray = new boolean[inoutarray9.value.getArrayOfboolean()
        .size()];
    for (int i = 0; i < inoutarray9.value.getArrayOfboolean().size(); i++)
      booleanArray[i] = inoutarray9.value.getArrayOfboolean().get(i)
          .booleanValue();
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.boolean_data, booleanArray,
        "boolean")) {
      TestUtil.logErr("boolean array data miscompare");
      pass = false;
    }

    byte[] byteArray = new byte[inoutarray10.value.getArrayOfbyte().size()];
    for (int i = 0; i < inoutarray10.value.getArrayOfbyte().size(); i++)
      byteArray[i] = inoutarray10.value.getArrayOfbyte().get(i).byteValue();
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.byte_data, byteArray,
        "byte")) {
      TestUtil.logErr("byte array data miscompare");
      pass = false;
    }

    QName[] qnameArray = new QName[inoutarray11.value.getArrayOfQName().size()];
    for (int i = 0; i < inoutarray11.value.getArrayOfQName().size(); i++)
      qnameArray[i] = inoutarray11.value.getArrayOfQName().get(i);
    if (!JAXWS_Data.compareArrayValues(JAXWS_Data.QName_nonull_data, qnameArray,
        "QName")) {
      TestUtil.logErr("QNameBigDecimal array data miscompare");
      pass = false;
    }

    XMLGregorianCalendar[] dateTimeArray = new XMLGregorianCalendar[inoutarray12.value
        .getArrayOfdateTime().size()];
    for (int i = 0; i < inoutarray12.value.getArrayOfdateTime().size(); i++)
      dateTimeArray[i] = inoutarray12.value.getArrayOfdateTime().get(i);
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
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void InOutSimpleTypesTest() throws Fault {
    TestUtil.logTrace("InOutSimpleTypesTest");
    boolean pass = true;
    initialize_inout_SimpleType_data();
    initialize_saved_inout_SimpleType_data();
    try {
      TestUtil.logMsg("Marshalling input/output data");
      port.echoInOutSimpleTypes(inout1, inout2, inout3, inout4, inout5, inout6,
          inout7, inout8, inout9, inout10, inout11, inout12);
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
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
   */
  public void InOutSimpleTypesArrayTest() throws Fault {
    TestUtil.logTrace("InOutSimpleTypesArrayTest");
    boolean pass = true;
    try {
      initialize_inout_SimpleTypeArray_data();
      TestUtil.logMsg("Marshalling input/output data");
      port.echoInOutSimpleTypesArray(inoutarray1, inoutarray2, inoutarray3,
          inoutarray4, inoutarray5, inoutarray6, inoutarray7, inoutarray8,
          inoutarray9, inoutarray10, inoutarray11, inoutarray12);
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
   * primitive type. For each type pass its value as input to the corresponding
   * RPC method and receive it back as the return value. Compare results of each
   * value/type of what was sent and what was returned. Verify they are equal.
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
      Book b1 = new Book();
      b1.setAuthor("author1");
      b1.setTitle("title1");
      b1.setIsbn(1);
      TestUtil.logMsg("Marshalling input/output data");
      TestUtil.logMsg("Data In: [author0|title0|0]");
      port.echoInOutBook(inout_bh);
      Book v = inout_bh.value;
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
   * @assertion_ids: JAXWS:SPEC:2031;
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
      aob.getArrayOfBook().add(b0);
      aob.getArrayOfBook().add(b1);
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
      java.util.List<Book> blist = inout_bh.value.getArrayOfBook();
      if (blist.size() != 2) {
        TestUtil.logErr("list size not equal 2");
        pass = false;
      } else {
        r[0] = blist.get(0);
        r[1] = blist.get(1);
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
