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
import com.sun.ts.tests.jaxws.common.*;

import jakarta.xml.ws.WebServiceException;
import java.rmi.AccessException;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.DatatypeConfigurationException;

import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;
import java.math.BigDecimal;
import javax.xml.namespace.QName;

import jakarta.xml.ws.Holder;

// Service Implementation Class - as outlined in JAX-WS Specification

import jakarta.jws.WebService;

@WebService(portName = "ParameterModeTestPort", serviceName = "ParameterModeTestService", targetNamespace = "http://ParameterModeTest.org/", wsdlLocation = "WEB-INF/wsdl/WSW2JRLParameterModeTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.ParameterModeTest")
public class ParameterModeTestImpl implements ParameterModeTest {
  private static DatatypeFactory dtfactory = null;

  static {
    System.out.println("Configure DatatypeFactory object");
    try {
      dtfactory = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) {
      System.out.println("Could not configure DatatypeFactory object" + e);
    }
  }

  public void echoIn(java.lang.String varString) {
    System.out.println("in echoIn");
    String result = "";
    boolean pass = true;
    String v1 = "String1";
    if (!v1.equals(varString)) {
      result = "compare of data failed for String - received " + varString
          + ",  expected: " + v1;
      pass = false;
    }
    if (!pass) {
      throw new WebServiceException(result);
    }
    varString = "String4";
  }

  public void echoOut(Holder<java.lang.String> varString) {
    System.out.println("in echoOut");
    varString.value = "String4";
  }

  public void echoInOut(Holder<java.lang.String> varString) {
    System.out.println("in echoInOut");
    String result = "";
    boolean pass = true;
    String v1 = "String1";
    if (!v1.equals(varString.value)) {
      result = "compare of data failed for String - received " + varString.value
          + ",  expected: " + v1;
      pass = false;
    }
    if (!pass) {
      throw new WebServiceException(result);
    }
    varString.value = "String4";
  }

  public void echoInOut2(Holder<java.lang.String> varString) {
    System.out.println("in echoInOut2");
    String result = "";
    boolean pass = true;
    String v1 = "String1";
    if (!v1.equals(varString.value)) {
      result = "compare of data failed for String - received " + varString.value
          + ",  expected: " + v1;
      pass = false;
    }
    if (!pass) {
      throw new WebServiceException(result);
    }
    varString.value = "String4";
  }

  public String echoInOut3(java.lang.String param) {
    System.out.println("in echoInOut3");
    String result = "";
    boolean pass = true;
    String v1 = "String1";
    if (!v1.equals(param)) {
      result = "compare of data failed for String - received " + param
          + ",  expected: " + v1;
      pass = false;
    }
    if (!pass) {
      throw new WebServiceException(result);
    }
    param = "String4";
    return param;
  }

  public String echoInOut4(java.lang.String param) {
    System.out.println("in echoInOut4");
    String result = "";
    boolean pass = true;
    String v1 = "String1";
    if (!v1.equals(param)) {
      result = "compare of data failed for String - received " + param
          + ",  expected: " + v1;
      pass = false;
    }
    if (!pass) {
      throw new WebServiceException(result);
    }
    param = "String4";
    return param;
  }

  public void echoMix(java.lang.String varInString,
      Holder<java.lang.String> varInOutString,
      Holder<java.lang.String> varOutString) {
    System.out.println("in echoMix");
    String result = "";
    boolean pass = true;
    String v1 = "String1";
    if (!v1.equals(varInString)) {
      result = "compare of data failed for String - received " + varInString
          + ",  expected: " + v1;
      pass = false;
    }
    v1 = "String4";
    if (!v1.equals(varInOutString.value)) {
      result = "compare of data failed for String - received "
          + varInOutString.value + ",  expected: " + v1;
      pass = false;
    }
    if (!pass) {
      throw new WebServiceException(result);
    }
    varOutString.value = "String3";
    varInOutString.value = "String5";
  }

  public void echoInSimpleTypes(java.lang.String varString,
      java.math.BigInteger varInteger, int varInt, long varLong, short varShort,
      java.math.BigDecimal varDecimal, float varFloat, double varDouble,
      boolean varBoolean, byte varByte, javax.xml.namespace.QName varQName,
      XMLGregorianCalendar varDateTime, byte[] varBase64Binary,
      byte[] varHexBinary) {
    System.out.println("in echoInSimpleTypes");
    boolean pass = true;
    String v1 = "String1";
    BigInteger v2 = new BigInteger("3512359");
    int v3 = (int) Integer.MIN_VALUE;
    long v4 = (long) Long.MIN_VALUE;
    short v5 = (short) Short.MIN_VALUE;
    BigDecimal v6 = new BigDecimal("3512359.1456");
    float v7 = (float) Float.MIN_VALUE;
    double v8 = (double) Double.MIN_VALUE;
    boolean v9 = false;
    byte v10 = (byte) Byte.MIN_VALUE;
    QName v11 = new QName("String2");
    XMLGregorianCalendar v12 = dtfactory.newXMLGregorianCalendar(96, 5, 1, 10,
        0, 0, 0, 0);
    byte v13[] = JAXWS_Data.byte_data;
    byte v14[] = JAXWS_Data.byte_data;
    StringBuffer result = new StringBuffer();
    if (!v1.equals(varString)) {
      result.append("compare of data failed for String - received " + varString
          + ",  expected: " + v1);
      pass = false;
    }
    if (!v2.equals(varInteger)) {
      result.append("\ncompare of data failed for BigInteger - received "
          + varInteger + ",  expected: " + v2);
      pass = false;
    }
    if (v3 != varInt) {
      result.append("\ncompare of data failed for int - received " + varInt
          + ",  expected: " + v3);
      pass = false;
    }
    if (v4 != varLong) {
      result.append("\ncompare of data failed for long - received " + varLong
          + ",  expected: " + v4);
      pass = false;
    }
    if (v5 != varShort) {
      result.append("\ncompare of data failed for short - received " + varShort
          + ",  expected: " + v5);
      pass = false;
    }
    if (!v6.equals(varDecimal)) {
      result.append("\ncompare of data failed for decimal - received "
          + varDecimal + ",  expected: " + v6);
      pass = false;
    }
    if (v7 != varFloat) {
      result.append("\ncompare of data failed for float - received " + varFloat
          + ",  expected: " + v7);
      pass = false;
    }
    if (v8 != varDouble) {
      result.append("\ncompare of data failed for double - received "
          + varDouble + ",  expected: " + v8);
      pass = false;
    }
    if (v9 != varBoolean) {
      result.append("\ncompare of data failed for boolean - received "
          + varBoolean + ",  expected: " + v9);
      pass = false;
    }
    if (v10 != varByte) {
      result.append("\ncompare of data failed for byte - received " + varByte
          + ",  expected: " + v10);
      pass = false;
    }
    if (!v11.equals(varQName)) {
      result.append("\ncompare of data failed for QName - received " + varQName
          + ",  expected: " + v11);
      pass = false;
    }
    if (!JAXWS_Data.compareXMLGregorianCalendars(v12, varDateTime)) {
      result.append(
          "\ncompare of data failed for XMLGregorianCalendar - received "
              + varDateTime + ",  expected: " + v12);
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(v13, varBase64Binary, "byte")) {
      result.append("\ncompare of data failed for Base64Binary - received [");
      for (int i = 0; i < varBase64Binary.length; i++) {
        result.append(varBase64Binary[i]);
        if (i + 1 != varBase64Binary.length)
          result.append(",");
        else
          result.append("]");
      }
      result.append(",  expected: [");
      for (int i = 0; i < v13.length; i++) {
        result.append(v13[i]);
        if (i + 1 != v13.length)
          result.append(",");
        else
          result.append("]");
      }
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(v14, varHexBinary, "byte")) {
      result.append("\ncompare of data failed for HexBinary - received [");
      for (int i = 0; i < varHexBinary.length; i++) {
        result.append(varHexBinary[i]);
        if (i + 1 != varHexBinary.length)
          result.append(",");
        else
          result.append("]");
      }
      result.append(",  expected: [");
      for (int i = 0; i < v14.length; i++) {
        result.append(v14[i]);
        if (i + 1 != v14.length)
          result.append(",");
        else
          result.append("]");
      }
      pass = false;
    }

    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    varString = "String4";
    varInteger = new BigInteger("3512360");
    varInt = (int) Integer.MAX_VALUE;
    varLong = (long) Long.MAX_VALUE;
    varShort = (short) Short.MAX_VALUE;
    varDecimal = new BigDecimal("3512360.1456");
    varFloat = (float) Float.MAX_VALUE;
    varDouble = (double) Double.MAX_VALUE;
    varBoolean = true;
    varByte = (byte) Byte.MAX_VALUE;
    varQName = new QName("String5");
    varDateTime = dtfactory.newXMLGregorianCalendar(96, 5, 2, 10, 0, 0, 0, 0);
    varBase64Binary = JAXWS_Data.byte_data2;
    varHexBinary = JAXWS_Data.byte_data2;
  }

  public void echoOutSimpleTypes(Holder<String> varString,
      Holder<BigInteger> varInteger, Holder<Integer> varInt,
      Holder<Long> varLong, Holder<Short> varShort,
      Holder<BigDecimal> varDecimal, Holder<Float> varFloat,
      Holder<Double> varDouble, Holder<Boolean> varBoolean,
      Holder<Byte> varByte, Holder<QName> varQName,
      Holder<XMLGregorianCalendar> varDateTime, Holder<byte[]> varBase64Binary,
      Holder<byte[]> varHexBinary) {
    System.out.println("in echoOutSimpleTypes");
    varString.value = "String4";
    varInteger.value = new BigInteger("3512360");
    varInt.value = Integer.valueOf(Integer.MAX_VALUE);
    varLong.value = Long.valueOf(Long.MAX_VALUE);
    varShort.value = Short.valueOf(Short.MAX_VALUE);
    varDecimal.value = new BigDecimal("3512360.1456");
    varFloat.value = Float.valueOf(Float.MAX_VALUE);
    varDouble.value = Double.valueOf(Double.MAX_VALUE);
    varBoolean.value = Boolean.valueOf(Boolean.TRUE);
    varByte.value = Byte.valueOf(Byte.MAX_VALUE);
    varQName.value = new QName("String5");
    varDateTime.value = dtfactory.newXMLGregorianCalendar(96, 5, 2, 10, 0, 0, 0,
        0);
    varBase64Binary.value = JAXWS_Data.byte_data2;
    varHexBinary.value = JAXWS_Data.byte_data2;
  }

  public void echoInOutSimpleTypes(Holder<String> varString,
      Holder<BigInteger> varInteger, Holder<Integer> varInt,
      Holder<Long> varLong, Holder<Short> varShort,
      Holder<BigDecimal> varDecimal, Holder<Float> varFloat,
      Holder<Double> varDouble, Holder<Boolean> varBoolean,
      Holder<Byte> varByte, Holder<QName> varQName,
      Holder<XMLGregorianCalendar> varDateTime, Holder<byte[]> varBase64Binary,
      Holder<byte[]> varHexBinary) {

    System.out.println("in echoInOutSimpleTypes");
    String v1 = "String1";
    BigInteger v2 = new BigInteger("3512359");
    Integer v3 = Integer.MIN_VALUE;
    Long v4 = Long.MIN_VALUE;
    Short v5 = Short.MIN_VALUE;
    BigDecimal v6 = new BigDecimal("3512359.1456");
    Float v7 = Float.MIN_VALUE;
    Double v8 = Double.MIN_VALUE;
    Boolean v9 = Boolean.FALSE;
    Byte v10 = Byte.MIN_VALUE;
    QName v11 = new QName("String2");
    XMLGregorianCalendar v12 = dtfactory.newXMLGregorianCalendar(6, 5, 1, 10, 0,
        0, 0, 0);
    byte[] v13 = JAXWS_Data.byte_data;
    byte[] v14 = JAXWS_Data.byte_data;
    boolean pass = true;
    StringBuffer result = new StringBuffer();
    if (!v1.equals(varString.value)) {
      result.append("compare of data failed for String - received "
          + varString.value + ",  expected: " + v1);
      pass = false;
    }
    if (!v2.equals(varInteger.value)) {
      result.append("\ncompare of data failed for BigInteger - received "
          + varInteger.value + ",  expected: " + v2);
      pass = false;
    }
    if (!v3.equals(varInt.value)) {
      result.append("\ncompare of data failed for int - received "
          + varInt.value + ",  expected: " + v3);
      pass = false;
    }
    if (!v4.equals(varLong.value)) {
      result.append("\ncompare of data failed for long - received "
          + varLong.value + ",  expected: " + v4);
      pass = false;
    }
    if (!v5.equals(varShort.value)) {
      result.append("\ncompare of data failed for short - received "
          + varShort.value + ",  expected: " + v5);
      pass = false;
    }
    if (!v6.equals(varDecimal.value)) {
      result.append("\ncompare of data failed for decimal - received "
          + varDecimal.value + ",  expected: " + v6);
      pass = false;
    }
    if (!v7.equals(varFloat.value)) {
      result.append("\ncompare of data failed for float - received "
          + varFloat.value + ",  expected: " + v7);
      pass = false;
    }
    if (!v8.equals(varDouble.value)) {
      result.append("\ncompare of data failed for double - received "
          + varDouble.value + ",  expected: " + v8);
      pass = false;
    }
    if (!v9.equals(varBoolean.value)) {
      result.append("\ncompare of data failed for boolean - received "
          + varBoolean.value + ",  expected: " + v9);
      pass = false;
    }
    if (!v10.equals(varByte.value)) {
      result.append("\ncompare of data failed for byte - received "
          + varByte.value + ",  expected: " + v10);
      pass = false;
    }
    if (!v11.equals(varQName.value)) {
      result.append("\ncompare of data failed for QName - received "
          + varQName.value + ",  expected: " + v11);
      pass = false;
    }
    if (!JAXWS_Data.compareXMLGregorianCalendars(v12, varDateTime.value)) {

      result.append(
          "\ncompare of data failed for XMLGregorianCalendar - received "
              + varDateTime.value + ",  expected: " + v12);
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(v13, varBase64Binary.value, "byte")) {
      result.append("\ncompare of data failed for Base64Binary - received [");
      for (int i = 0; i < varBase64Binary.value.length; i++) {
        result.append(varBase64Binary.value[i]);
        if (i + 1 != varBase64Binary.value.length)
          result.append(",");
        else
          result.append("]");
      }
      result.append(",  expected: [");
      for (int i = 0; i < v13.length; i++) {
        result.append(v13[i]);
        if (i + 1 != v13.length)
          result.append(",");
        else
          result.append("]");
      }
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(v14, varHexBinary.value, "byte")) {
      result.append("\ncompare of data failed for HexBinary - received [");
      for (int i = 0; i < varHexBinary.value.length; i++) {
        result.append(varHexBinary.value[i]);
        if (i + 1 != varHexBinary.value.length)
          result.append(",");
        else
          result.append("]");
      }
      result.append(",  expected: [");
      for (int i = 0; i < v14.length; i++) {
        result.append(v14[i]);
        if (i + 1 != v14.length)
          result.append(",");
        else
          result.append("]");
      }
      pass = false;
    }

    if (!pass) {
      throw new WebServiceException(result.toString());
    }

    varString.value = "String4";
    varInteger.value = new BigInteger("3512360");
    varInt.value = Integer.MAX_VALUE;
    varLong.value = Long.MAX_VALUE;
    varShort.value = Short.MAX_VALUE;
    varDecimal.value = new BigDecimal("3512360.1456");
    varFloat.value = Float.MAX_VALUE;
    varDouble.value = Double.MAX_VALUE;
    varBoolean.value = Boolean.TRUE;
    varByte.value = Byte.MAX_VALUE;
    varQName.value = new QName("String5");
    varDateTime.value = dtfactory.newXMLGregorianCalendar(96, 5, 2, 10, 0, 0, 0,
        0);
    varBase64Binary.value = JAXWS_Data.byte_data2;
    varHexBinary.value = JAXWS_Data.byte_data2;
  }

  public void echoInEnum(EnumString varEnumString, BigInteger varEnumInteger,
      int varEnumInt, long varEnumLong, short varEnumShort,
      java.math.BigDecimal varEnumDecimal, float varEnumFloat,
      double varEnumDouble, byte varEnumByte) {
    System.out.println("in echoInEnum");
    boolean pass = true;
    EnumString v1 = EnumString.STRING_1;
    BigInteger v2 = new BigInteger("3512359");
    int v3 = (int) Integer.MIN_VALUE;
    long v4 = (long) Long.MIN_VALUE;
    short v5 = (short) Short.MIN_VALUE;
    BigDecimal v6 = new BigDecimal("3512359.1456");
    float v7 = (float) -1.00000000;
    double v8 = (double) -1.00000000;
    byte v9 = (byte) Byte.MIN_VALUE;
    StringBuffer result = new StringBuffer();
    if (v1 != varEnumString) {
      result.append("compare of data failed for String - received "
          + varEnumString + ",  expected: " + v1);
      pass = false;
    }
    if (!v2.equals(varEnumInteger)) {
      result.append("\ncompare of data failed for BigInteger - received "
          + varEnumInteger + ",  expected: " + v2);
      pass = false;
    }
    if (v3 != varEnumInt) {
      result.append("\ncompare of data failed for int - received " + varEnumInt
          + ",  expected: " + v3);
      pass = false;
    }
    if (v4 != varEnumLong) {
      result.append("\ncompare of data failed for long - received "
          + varEnumLong + ",  expected: " + v4);
      pass = false;
    }
    if (v5 != varEnumShort) {
      result.append("\ncompare of data failed for short - received "
          + varEnumShort + ",  expected: " + v5);
      pass = false;
    }
    if (!v6.equals(varEnumDecimal)) {
      result.append("\ncompare of data failed for decimal - received "
          + varEnumDecimal + ",  expected: " + v6);
      pass = false;
    }
    if (v7 != varEnumFloat) {
      result.append("\ncompare of data failed for float - received "
          + varEnumFloat + ",  expected: " + v7);
      pass = false;
    }
    if (v8 != varEnumDouble) {
      result.append("\ncompare of data failed for double - received "
          + varEnumDouble + ",  expected: " + v8);
      pass = false;
    }
    if (v9 != varEnumByte) {
      result.append("\ncompare of data failed for byte - received "
          + varEnumByte + ",  expected: " + v9);
      pass = false;
    }

    if (!pass) {
      throw new WebServiceException(result.toString());
    }

    varEnumString = EnumString.STRING_2;
    varEnumInteger = new BigInteger("3512360");
    varEnumInt = (int) Integer.MAX_VALUE;
    varEnumLong = (long) Long.MAX_VALUE;
    varEnumShort = (short) Short.MAX_VALUE;
    varEnumDecimal = new BigDecimal("3512360.1456");
    varEnumFloat = (float) 3.00000000;
    varEnumDouble = (double) 3.0000000000000;
    varEnumByte = (byte) Byte.MAX_VALUE;
  }

  public void echoOutEnum(Holder<EnumString> varEnumString,
      Holder<BigInteger> varEnumInteger, Holder<Integer> varEnumInt,
      Holder<Long> varEnumLong, Holder<Short> varEnumShort,
      Holder<BigDecimal> varEnumDecimal, Holder<Float> varEnumFloat,
      Holder<Double> varEnumDouble, Holder<Byte> varEnumByte) {

    System.out.println("in echoOutEnum");
    varEnumString.value = EnumString.STRING_2;
    varEnumInteger.value = new BigInteger("3512360");
    varEnumInt.value = Integer.MAX_VALUE;
    varEnumLong.value = Long.MAX_VALUE;
    varEnumShort.value = Short.MAX_VALUE;
    varEnumDecimal.value = new BigDecimal("3512360.1456");
    varEnumFloat.value = new Float(3.00000000);
    varEnumDouble.value = Double.valueOf(3.0000000000000);
    varEnumByte.value = Byte.MAX_VALUE;
  }

  public void echoInOutEnum(Holder<EnumString> varEnumString,
      Holder<BigInteger> varEnumInteger, Holder<Integer> varEnumInt,
      Holder<Long> varEnumLong, Holder<Short> varEnumShort,
      Holder<BigDecimal> varEnumDecimal, Holder<Float> varEnumFloat,
      Holder<Double> varEnumDouble, Holder<Byte> varEnumByte) {

    System.out.println("in echoInOutEnum");
    boolean pass = true;
    EnumString v1 = EnumString.STRING_1;
    BigInteger v2 = new BigInteger("3512359");
    Integer v3 = Integer.valueOf(Integer.MIN_VALUE);
    Long v4 = Long.valueOf(Long.MIN_VALUE);
    Short v5 = Short.valueOf(Short.MIN_VALUE);
    BigDecimal v6 = new BigDecimal("3512359.1456");
    Float v7 = new Float("-1.00000000");
    Double v8 = Double.valueOf("-1.00000000");
    Byte v9 = (byte) Byte.MIN_VALUE;
    StringBuffer result = new StringBuffer();
    if (v1 != varEnumString.value) {
      result.append("compare of data failed for String - received "
          + varEnumString.value + ",  expected: " + v1);
      pass = false;
    }
    if (!v2.equals(varEnumInteger.value)) {
      result.append("\ncompare of data failed for BigInteger - received "
          + varEnumInteger.value + ",  expected: " + v2);
      pass = false;
    }
    if (!v3.equals(varEnumInt.value)) {
      result.append("\ncompare of data failed for Integer - received "
          + varEnumInt.value + ",  expected: " + v3);
      pass = false;
    }
    if (!v4.equals(varEnumLong.value)) {
      result.append("\ncompare of data failed for Long - received "
          + varEnumLong.value + ",  expected: " + v4);
      pass = false;
    }
    if (!v5.equals(varEnumShort.value)) {
      result.append("\ncompare of data failed for Short - received "
          + varEnumShort.value + ",  expected: " + v5);
      pass = false;
    }
    if (!v6.equals(varEnumDecimal.value)) {
      result.append("\ncompare of data failed for Decimal - received "
          + varEnumDecimal.value + ",  expected: " + v6);
      pass = false;
    }
    if (!v7.equals(varEnumFloat.value)) {
      result.append("\ncompare of data failed for Float - received "
          + varEnumFloat.value + ",  expected: " + v7);
      pass = false;
    }
    if (!v8.equals(varEnumDouble.value)) {
      result.append("\ncompare of data failed for Double - received "
          + varEnumDouble.value + ",  expected: " + v8);
      pass = false;
    }
    if (!v9.equals(varEnumByte.value)) {
      result.append("\ncompare of data failed for Byte - received "
          + varEnumByte.value + ",  expected: " + v9);
      pass = false;
    }

    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    varEnumString.value = EnumString.STRING_2;
    varEnumInteger.value = new BigInteger("3512360");
    varEnumInt.value = Integer.MAX_VALUE;
    varEnumLong.value = Long.MAX_VALUE;
    varEnumShort.value = Short.MAX_VALUE;
    varEnumDecimal.value = new BigDecimal("3512360.1456");
    varEnumFloat.value = new Float(3.00000000);
    varEnumDouble.value = Double.valueOf(3.0000000000000);
    varEnumByte.value = Byte.MAX_VALUE;
  }

  public void echoInStruct(
      com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.AllStruct varStruct) {

    System.out.println("in echoInStruct");
    boolean pass = true;
    String v1 = "String1";
    BigInteger v2 = new BigInteger("3512359");
    int v3 = (int) Integer.MIN_VALUE;
    long v4 = (long) Long.MIN_VALUE;
    short v5 = (short) Short.MIN_VALUE;
    BigDecimal v6 = new BigDecimal("3512359.1456");
    float v7 = (float) Float.MIN_VALUE;
    double v8 = (double) Double.MIN_VALUE;
    boolean v9 = false;
    byte v10 = (byte) Byte.MIN_VALUE;
    QName v11 = new QName("String2");
    XMLGregorianCalendar v12 = dtfactory.newXMLGregorianCalendar(96, 5, 1, 10,
        0, 0, 0, 0);
    byte v13[] = JAXWS_Data.byte_data;
    byte v14[] = JAXWS_Data.byte_data;
    StringBuffer result = new StringBuffer();
    if (!v1.equals(varStruct.getVarString())) {
      result.append("compare of data failed for String - received "
          + varStruct.getVarString() + ",  expected: " + v1);
      pass = false;
    }
    if (!v2.equals(varStruct.getVarInteger())) {
      result.append("\ncompare of data failed for BigInteger - received "
          + varStruct.getVarInteger() + ",  expected: " + v2);
      pass = false;
    }
    if (v3 != varStruct.getVarInt()) {
      result.append("\ncompare of data failed for int - received "
          + varStruct.getVarInt() + ",  expected: " + v3);
      pass = false;
    }
    if (v4 != varStruct.getVarLong()) {
      result.append("\ncompare of data failed for long - received "
          + varStruct.getVarLong() + ",  expected: " + v4);
      pass = false;
    }
    if (v5 != varStruct.getVarShort()) {
      result.append("\ncompare of data failed for short - received "
          + varStruct.getVarShort() + ",  expected: " + v5);
      pass = false;
    }
    if (!v6.equals(varStruct.getVarDecimal())) {
      result.append("\ncompare of data failed for decimal - received "
          + varStruct.getVarDecimal() + ",  expected: " + v6);
      pass = false;
    }
    if (v7 != varStruct.getVarFloat()) {
      result.append("\ncompare of data failed for float - received "
          + varStruct.getVarFloat() + ",  expected: " + v7);
      pass = false;
    }
    if (v8 != varStruct.getVarDouble()) {
      result.append("\ncompare of data failed for double - received "
          + varStruct.getVarDouble() + ",  expected: " + v8);
      pass = false;
    }
    if (v9 != varStruct.isVarBoolean()) {
      result.append("\ncompare of data failed for boolean - received "
          + varStruct.isVarBoolean() + ",  expected: " + v9);
      pass = false;
    }
    if (v10 != varStruct.getVarByte()) {
      result.append("\ncompare of data failed for byte - received "
          + varStruct.getVarByte() + ",  expected: " + v10);
      pass = false;
    }
    if (!v11.equals(varStruct.getVarQName())) {
      result.append("\ncompare of data failed for QName - received "
          + varStruct.getVarQName() + ",  expected: " + v11);
      pass = false;
    }
    if (!JAXWS_Data.compareXMLGregorianCalendars(v12,
        varStruct.getVarDateTime())) {
      result.append(
          "\ncompare of data failed for XMLGregorianCalendar - received "
              + varStruct.getVarDateTime() + ",  expected: " + v12);
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(v13, varStruct.getVarBase64Binary(),
        "byte")) {
      result.append("\ncompare of data failed for Base64Binary - received [");
      byte thedata[] = varStruct.getVarBase64Binary();
      for (int i = 0; i < thedata.length; i++) {
        result.append(thedata[i]);
        if (i + 1 != thedata.length)
          result.append(",");
        else
          result.append("]");
      }
      result.append(",  expected: [");
      for (int i = 0; i < v13.length; i++) {
        result.append(v13[i]);
        if (i + 1 != v13.length)
          result.append(",");
        else
          result.append("]");
      }
      pass = false;
    }
    if (!JAXWS_Data.compareArrayValues(v14, varStruct.getVarHexBinary(),
        "byte")) {
      result.append("\ncompare of data failed for HexBinary - received [");
      byte thedata[] = varStruct.getVarHexBinary();
      for (int i = 0; i < thedata.length; i++) {
        result.append(thedata[i]);
        if (i + 1 != thedata.length)
          result.append(",");
        else
          result.append("]");
      }
      result.append(",  expected: [");
      for (int i = 0; i < v14.length; i++) {
        result.append(v14[i]);
        if (i + 1 != v14.length)
          result.append(",");
        else
          result.append("]");
      }
      pass = false;
    }

    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    varStruct.setVarString("String4");
    varStruct.setVarInteger(new BigInteger("3512360"));
    varStruct.setVarInt((int) Integer.MAX_VALUE);
    varStruct.setVarLong((long) Long.MAX_VALUE);
    varStruct.setVarShort((short) Short.MAX_VALUE);
    varStruct.setVarDecimal(new BigDecimal("3512360.1456"));
    varStruct.setVarFloat((float) Float.MAX_VALUE);
    varStruct.setVarDouble((double) Double.MAX_VALUE);
    varStruct.setVarBoolean(true);
    varStruct.setVarByte((byte) Byte.MAX_VALUE);
    varStruct.setVarQName(new QName("String5"));
    varStruct.setVarDateTime(
        dtfactory.newXMLGregorianCalendar(6, 5, 2, 10, 0, 0, 0, 0));
    varStruct.setVarBase64Binary(JAXWS_Data.byte_data2);
    varStruct.setVarHexBinary(JAXWS_Data.byte_data2);
  }

  public void echoInOutSimpleTypesArray(
      Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.ArrayOfstring> varString,
      Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.ArrayOfinteger> varInteger,
      Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.ArrayOfint> varInt,
      Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.ArrayOflong> varLong,
      Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.ArrayOfshort> varShort,
      Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.ArrayOfdecimal> varDecimal,
      Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.ArrayOffloat> varFloat,
      Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.ArrayOfdouble> varDouble,
      Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.ArrayOfboolean> varBoolean,
      Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.ArrayOfbyte> varByte,
      Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.ArrayOfQName> varQName,
      Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.ArrayOfdateTime> varDateTime) {

    System.out.println("in echoInOutSimpleTypesArray");

    ArrayOfstring astring = new ArrayOfstring();
    for (int i = 0; i < JAXWS_Data.String_nonull_data.length; i++) {
      astring.getArrayOfstring().add(JAXWS_Data.String_nonull_data[i]);
    }
    varString.value = astring;

    ArrayOfinteger ainteger = new ArrayOfinteger();
    for (int i = 0; i < JAXWS_Data.BigInteger_nonull_data.length; i++) {
      ainteger.getArrayOfinteger().add(JAXWS_Data.BigInteger_nonull_data[i]);
    }
    varInteger.value = ainteger;

    ArrayOfint aint = new ArrayOfint();
    for (int i = 0; i < JAXWS_Data.int_data.length; i++) {
      aint.getArrayOfint().add(JAXWS_Data.int_data[i]);
    }
    varInt.value = aint;

    ArrayOflong along = new ArrayOflong();
    for (int i = 0; i < JAXWS_Data.long_data.length; i++) {
      along.getArrayOflong().add(JAXWS_Data.long_data[i]);
    }
    varLong.value = along;

    ArrayOfshort ashort = new ArrayOfshort();
    for (int i = 0; i < JAXWS_Data.short_data.length; i++) {
      ashort.getArrayOfshort().add(JAXWS_Data.short_data[i]);
    }
    varShort.value = ashort;

    ArrayOfdecimal adecimal = new ArrayOfdecimal();
    for (int i = 0; i < JAXWS_Data.BigDecimal_nonull_data.length; i++) {
      adecimal.getArrayOfdecimal().add(JAXWS_Data.BigDecimal_nonull_data[i]);
    }
    varDecimal.value = adecimal;

    ArrayOffloat afloat = new ArrayOffloat();
    for (int i = 0; i < JAXWS_Data.float_data.length; i++) {
      afloat.getArrayOffloat().add(JAXWS_Data.float_data[i]);
    }
    varFloat.value = afloat;

    ArrayOfdouble adouble = new ArrayOfdouble();
    for (int i = 0; i < JAXWS_Data.double_data.length; i++) {
      adouble.getArrayOfdouble().add(JAXWS_Data.double_data[i]);
    }
    varDouble.value = adouble;

    ArrayOfboolean abool = new ArrayOfboolean();
    for (int i = 0; i < JAXWS_Data.boolean_data.length; i++) {
      abool.getArrayOfboolean().add(JAXWS_Data.boolean_data[i]);
    }
    varBoolean.value = abool;

    ArrayOfbyte abyte = new ArrayOfbyte();
    for (int i = 0; i < JAXWS_Data.byte_data.length; i++) {
      abyte.getArrayOfbyte().add(JAXWS_Data.byte_data[i]);
    }
    varByte.value = abyte;

    ArrayOfQName aqname = new ArrayOfQName();
    for (int i = 0; i < JAXWS_Data.QName_nonull_data.length; i++) {
      aqname.getArrayOfQName().add(JAXWS_Data.QName_nonull_data[i]);
    }
    varQName.value = aqname;

    ArrayOfdateTime adt = new ArrayOfdateTime();
    for (int i = 0; i < JAXWS_Data.XMLGregorianCalendar_nonull_data.length; i++) {
      adt.getArrayOfdateTime()
          .add(JAXWS_Data.XMLGregorianCalendar_nonull_data[i]);
    }
    varDateTime.value = adt;

  }

  public void echoInOutBook(
      Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.Book> varBook) {
    System.out.println("in echoInOutBook");
    Book b = varBook.value;
    b.setAuthor("author1");
    b.setTitle("title1");
    b.setIsbn(1);
    varBook.value = b;
  }

  public void echoInOutBookArray(
      Holder<com.sun.ts.tests.jaxws.ee.w2j.rpc.literal.parametermodetest.ArrayOfBook> varBook) {

    System.out.println("in echoInOutBookArray");
    Book[] b = varBook.value.getArrayOfBook()
        .toArray(new Book[varBook.value.getArrayOfBook().size()]);
    if (b.length != 2)
      throw new WebServiceException("array size not 2");
    String author = b[0].getAuthor();
    String title = b[0].getTitle();
    int isbn = b[0].getIsbn();
    b[0].setAuthor(b[1].getAuthor());
    b[0].setTitle(b[1].getTitle());
    b[0].setIsbn(b[1].getIsbn());
    b[1].setAuthor(author);
    b[1].setTitle(title);
    b[1].setIsbn(isbn);
    ArrayOfBook ab = new ArrayOfBook();
    for (int i = 0; i < b.length; i++) {
      ab.getArrayOfBook().add(b[i]);
    }
    varBook.value = ab;

  }

}
