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

package com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxrpc.common.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.AccessException;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.math.BigInteger;
import java.math.BigDecimal;
import javax.xml.namespace.QName;

import javax.xml.rpc.holders.*;
import com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.*;

// Service Implementation Class - as outlined in JAX-RPC Specification

public class ParameterModeTestImpl implements ParameterModeTest {

  public void echoIn(java.lang.String varString) throws RemoteException {
    String result = "";
    boolean pass = true;
    String v1 = "String1";
    if (!v1.equals(varString)) {
      result = "compare of data failed for String - received " + varString
          + ",  expected: " + v1;
      pass = false;
    }
    if (!pass) {
      throw new RemoteException(result);
    }
    varString = "String4";
  }

  public void echoOut(javax.xml.rpc.holders.StringHolder varString)
      throws RemoteException {
    varString.value = new String("String4");
  }

  public void echoInOut(javax.xml.rpc.holders.StringHolder varString)
      throws RemoteException {
    String result = "";
    boolean pass = true;
    String v1 = "String1";
    if (!v1.equals(varString.value)) {
      result = "compare of data failed for String - received " + varString.value
          + ",  expected: " + v1;
      pass = false;
    }
    if (!pass) {
      throw new RemoteException(result);
    }
    varString.value = new String("String4");
  }

  public void echoInOut2(javax.xml.rpc.holders.StringHolder varString)
      throws RemoteException {
    String result = "";
    boolean pass = true;
    String v1 = "String1";
    if (!v1.equals(varString.value)) {
      result = "compare of data failed for String - received " + varString.value
          + ",  expected: " + v1;
      pass = false;
    }
    if (!pass) {
      throw new RemoteException(result);
    }
    varString.value = new String("String4");
  }

  public String echoInOut3(java.lang.String param) throws RemoteException {
    String result = "";
    boolean pass = true;
    String v1 = "String1";
    if (!v1.equals(param)) {
      result = "compare of data failed for String - received " + param
          + ",  expected: " + v1;
      pass = false;
    }
    if (!pass) {
      throw new RemoteException(result);
    }
    param = "String4";
    return param;
  }

  public String echoInOut4(java.lang.String param) throws RemoteException {
    String result = "";
    boolean pass = true;
    String v1 = "String1";
    if (!v1.equals(param)) {
      result = "compare of data failed for String - received " + param
          + ",  expected: " + v1;
      pass = false;
    }
    if (!pass) {
      throw new RemoteException(result);
    }
    param = "String4";
    return param;
  }

  public void echoMix(java.lang.String varInString,
      javax.xml.rpc.holders.StringHolder varInOutString,
      javax.xml.rpc.holders.StringHolder varOutString) throws RemoteException {
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
      throw new RemoteException(result);
    }
    varOutString.value = new String("String3");
    varInOutString.value = new String("String5");
  }

  public void echoInSimpleTypes(java.lang.String varString,
      java.math.BigInteger varInteger, int varInt, long varLong, short varShort,
      java.math.BigDecimal varDecimal, float varFloat, double varDouble,
      boolean varBoolean, byte varByte, javax.xml.namespace.QName varQName,
      java.util.Calendar varDateTime, java.lang.String varSoapString,
      java.lang.Boolean varSoapBoolean, java.lang.Float varSoapFloat,
      java.lang.Double varSoapDouble, java.math.BigDecimal varSoapDecimal,
      java.lang.Integer varSoapInt, java.lang.Short varSoapShort,
      java.lang.Byte varSoapByte, byte[] varBase64Binary, byte[] varHexBinary,
      byte[] varSoapBase64) throws RemoteException

  {
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
    Calendar v12 = (Calendar) new GregorianCalendar(96, 5, 1);
    String v13 = "String3";
    Boolean v14 = new Boolean("false");
    Float v15 = new Float(Float.MIN_VALUE);
    Double v16 = new Double(Double.MIN_VALUE);
    BigDecimal v17 = new BigDecimal("3512359.1111");
    Integer v18 = new Integer(Integer.MIN_VALUE);
    Short v19 = new Short(Short.MIN_VALUE);
    Byte v20 = new Byte(Byte.MIN_VALUE);
    byte v21[] = JAXRPC_Data.byte_data;
    byte v22[] = JAXRPC_Data.byte_data;
    byte v23[] = JAXRPC_Data.byte_data;
    String result = "";
    if (!v1.equals(varString)) {
      result = "compare of data failed for String - received " + varString
          + ",  expected: " + v1;
      pass = false;
    }
    if (!v2.equals(varInteger)) {
      result = result + "\ncompare of data failed for BigInteger - received "
          + varInteger + ",  expected: " + v2;
      pass = false;
    }
    if (v3 != varInt) {
      result = result + "\ncompare of data failed for int - received " + varInt
          + ",  expected: " + v3;
      pass = false;
    }
    if (v4 != varLong) {
      result = result + "\ncompare of data failed for long - received "
          + varLong + ",  expected: " + v4;
      pass = false;
    }
    if (v5 != varShort) {
      result = result + "\ncompare of data failed for short - received "
          + varShort + ",  expected: " + v5;
      pass = false;
    }
    if (!v6.equals(varDecimal)) {
      result = result + "\ncompare of data failed for decimal - received "
          + varDecimal + ",  expected: " + v6;
      pass = false;
    }
    if (v7 != varFloat) {
      result = result + "\ncompare of data failed for float - received "
          + varFloat + ",  expected: " + v7;
      pass = false;
    }
    if (v8 != varDouble) {
      result = result + "\ncompare of data failed for double - received "
          + varDouble + ",  expected: " + v8;
      pass = false;
    }
    if (v9 != varBoolean) {
      result = result + "\ncompare of data failed for boolean - received "
          + varBoolean + ",  expected: " + v9;
      pass = false;
    }
    if (v10 != varByte) {
      result = result + "\ncompare of data failed for byte - received "
          + varByte + ",  expected: " + v10;
      pass = false;
    }
    if (!v11.equals(varQName)) {
      result = result + "\ncompare of data failed for QName - received "
          + varQName + ",  expected: " + v11;
      pass = false;
    }
    if (!JAXRPC_Data.compareCalendars(v12, varDateTime)) {
      result = result + "\ncompare of data failed for Calendar - received "
          + varDateTime + ",  expected: " + v12;
      pass = false;
    }
    if (!v13.equals(varSoapString)) {
      result = result + "\ncompare of data failed for SoapString - received "
          + varSoapString + ",  expected: " + v13;
      pass = false;
    }
    if (!v14.equals(varSoapBoolean)) {
      result = result + "\ncompare of data failed for SoapBoolean - received "
          + varSoapBoolean + ",  expected: " + v14;
      pass = false;
    }
    if (!v15.equals(varSoapFloat)) {
      result = result + "\ncompare of data failed for SoapFloat - received "
          + varSoapFloat + ",  expected: " + v15;
      pass = false;
    }
    if (!v16.equals(varSoapDouble)) {
      result = result + "\ncompare of data failed for SoapDouble - received "
          + varSoapDouble + ",  expected: " + v16;
      pass = false;
    }
    if (!v17.equals(varSoapDecimal)) {
      result = result + "\ncompare of data failed for SoapDecimal - received "
          + varSoapDecimal + ",  expected: " + v17;
      pass = false;
    }
    if (!v18.equals(varSoapInt)) {
      result = result + "\ncompare of data failed for SoapInt - received "
          + varSoapInt + ",  expected: " + v18;
      pass = false;
    }
    if (!v19.equals(varSoapShort)) {
      result = result + "\ncompare of data failed for SoapShort - received "
          + varSoapShort + ",  expected: " + v19;
      pass = false;
    }
    if (!v20.equals(varSoapByte)) {
      result = result + "\ncompare of data failed for SoapByte - received "
          + varSoapByte + ",  expected: " + v20;
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(v21, varBase64Binary, "byte")) {
      result = result + "\ncompare of data failed for Base64Binary - received "
          + varBase64Binary + ",  expected: " + v21;
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(v22, varHexBinary, "byte")) {
      result = result + "\ncompare of data failed for HexBinary - received "
          + varHexBinary + ",  expected: " + v22;
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(v23, varSoapBase64, "byte")) {
      result = result + "\ncompare of data failed for SoapBase64 - received "
          + varSoapBase64 + ",  expected: " + v23;
      pass = false;
    }

    if (!pass) {
      throw new RemoteException(result);
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
    varDateTime = (Calendar) new GregorianCalendar(96, 5, 2);
    varSoapString = "String6";
    varSoapBoolean = new Boolean("true");
    varSoapFloat = new Float(Float.MAX_VALUE);
    varSoapDouble = new Double(Double.MAX_VALUE);
    varSoapDecimal = new BigDecimal("3512360.1111");
    varSoapInt = new Integer(Integer.MAX_VALUE);
    varSoapShort = new Short(Short.MAX_VALUE);
    varSoapByte = new Byte(Byte.MAX_VALUE);
    varBase64Binary = JAXRPC_Data.byte_data2;
    varHexBinary = JAXRPC_Data.byte_data2;
    varSoapBase64 = JAXRPC_Data.byte_data2;
  }

  public void echoOutSimpleTypes(javax.xml.rpc.holders.StringHolder varString,
      javax.xml.rpc.holders.BigIntegerHolder varInteger,
      javax.xml.rpc.holders.IntHolder varInt,
      javax.xml.rpc.holders.LongHolder varLong,
      javax.xml.rpc.holders.ShortHolder varShort,
      javax.xml.rpc.holders.BigDecimalHolder varDecimal,
      javax.xml.rpc.holders.FloatHolder varFloat,
      javax.xml.rpc.holders.DoubleHolder varDouble,
      javax.xml.rpc.holders.BooleanHolder varBoolean,
      javax.xml.rpc.holders.ByteHolder varByte,
      javax.xml.rpc.holders.QNameHolder varQName,
      javax.xml.rpc.holders.CalendarHolder varDateTime,
      javax.xml.rpc.holders.StringHolder varSoapString,
      javax.xml.rpc.holders.BooleanWrapperHolder varSoapBoolean,
      javax.xml.rpc.holders.FloatWrapperHolder varSoapFloat,
      javax.xml.rpc.holders.DoubleWrapperHolder varSoapDouble,
      javax.xml.rpc.holders.BigDecimalHolder varSoapDecimal,
      javax.xml.rpc.holders.IntegerWrapperHolder varSoapInt,
      javax.xml.rpc.holders.ShortWrapperHolder varSoapShort,
      javax.xml.rpc.holders.ByteWrapperHolder varSoapByte,
      javax.xml.rpc.holders.ByteArrayHolder varBase64Binary,
      javax.xml.rpc.holders.ByteArrayHolder varHexBinary,
      javax.xml.rpc.holders.ByteArrayHolder varSoapBase64)
      throws RemoteException {
    varString.value = new String("String4");
    varInteger.value = new BigInteger("3512360");
    varInt.value = Integer.MAX_VALUE;
    varLong.value = Long.MAX_VALUE;
    varShort.value = Short.MAX_VALUE;
    varDecimal.value = new BigDecimal("3512360.1456");
    varFloat.value = Float.MAX_VALUE;
    varDouble.value = Double.MAX_VALUE;
    varBoolean.value = true;
    varByte.value = Byte.MAX_VALUE;
    varQName.value = new QName("String5");
    varDateTime.value = new GregorianCalendar(96, 5, 2);
    varSoapString.value = new String("String6");
    varSoapBoolean.value = new Boolean(true);
    varSoapFloat.value = new Float(Float.MAX_VALUE);
    varSoapDouble.value = new Double(Double.MAX_VALUE);
    varSoapDecimal.value = new BigDecimal("3512360.1111");
    varSoapInt.value = new Integer(Integer.MAX_VALUE);
    varSoapShort.value = new Short(Short.MAX_VALUE);
    varSoapByte.value = new Byte(Byte.MAX_VALUE);
    varBase64Binary.value = JAXRPC_Data.byte_data2;
    varHexBinary.value = JAXRPC_Data.byte_data2;
    varSoapBase64.value = JAXRPC_Data.byte_data2;
  }

  public void echoInOutSimpleTypes(javax.xml.rpc.holders.StringHolder varString,
      javax.xml.rpc.holders.BigIntegerHolder varInteger,
      javax.xml.rpc.holders.IntHolder varInt,
      javax.xml.rpc.holders.LongHolder varLong,
      javax.xml.rpc.holders.ShortHolder varShort,
      javax.xml.rpc.holders.BigDecimalHolder varDecimal,
      javax.xml.rpc.holders.FloatHolder varFloat,
      javax.xml.rpc.holders.DoubleHolder varDouble,
      javax.xml.rpc.holders.BooleanHolder varBoolean,
      javax.xml.rpc.holders.ByteHolder varByte,
      javax.xml.rpc.holders.QNameHolder varQName,
      javax.xml.rpc.holders.CalendarHolder varDateTime,
      javax.xml.rpc.holders.StringHolder varSoapString,
      javax.xml.rpc.holders.BooleanWrapperHolder varSoapBoolean,
      javax.xml.rpc.holders.FloatWrapperHolder varSoapFloat,
      javax.xml.rpc.holders.DoubleWrapperHolder varSoapDouble,
      javax.xml.rpc.holders.BigDecimalHolder varSoapDecimal,
      javax.xml.rpc.holders.IntegerWrapperHolder varSoapInt,
      javax.xml.rpc.holders.ShortWrapperHolder varSoapShort,
      javax.xml.rpc.holders.ByteWrapperHolder varSoapByte,
      javax.xml.rpc.holders.ByteArrayHolder varBase64Binary,
      javax.xml.rpc.holders.ByteArrayHolder varHexBinary,
      javax.xml.rpc.holders.ByteArrayHolder varSoapBase64)
      throws RemoteException {

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
    Calendar v12 = (Calendar) new GregorianCalendar(96, 5, 1);
    String v13 = "String3";
    Boolean v14 = new Boolean(false);
    Float v15 = new Float(Float.MIN_VALUE);
    Double v16 = new Double(Double.MIN_VALUE);
    BigDecimal v17 = new BigDecimal("3512359.1111");
    Integer v18 = new Integer(Integer.MIN_VALUE);
    Short v19 = new Short(Short.MIN_VALUE);
    Byte v20 = new Byte(Byte.MIN_VALUE);
    byte[] v21 = JAXRPC_Data.byte_data;
    byte[] v22 = JAXRPC_Data.byte_data;
    byte[] v23 = JAXRPC_Data.byte_data;
    boolean pass = true;
    String result = "";
    if (!v1.equals(varString.value)) {
      result = "compare of data failed - received " + varString.value
          + ",  expected: " + v1;
      pass = false;
    }
    if (!v2.equals(varInteger.value)) {
      result = result + "\ncompare of data failed - received "
          + varInteger.value + ",  expected: " + v2;
      pass = false;
    }
    if (v3 != varInt.value) {
      result = result + "\ncompare of data failed - received " + varInt.value
          + ",  expected: " + v3;
      pass = false;
    }
    if (v4 != varLong.value) {
      result = result + "\ncompare of data failed - received " + varLong.value
          + ",  expected: " + v4;
      pass = false;
    }
    if (v5 != varShort.value) {
      result = result + "\ncompare of data failed - received " + varShort.value
          + ",  expected: " + v5;
      pass = false;
    }
    if (!v6.equals(varDecimal.value)) {
      result = result + "\ncompare of data failed - received "
          + varDecimal.value + ",  expected: " + v6;
      pass = false;
    }
    if (v7 != varFloat.value) {
      result = result + "\ncompare of data failed - received " + varFloat.value
          + ",  expected: " + v7;
      pass = false;
    }
    if (v8 != varDouble.value) {
      result = result + "\ncompare of data failed - received " + varDouble.value
          + ",  expected: " + v8;
      pass = false;
    }
    if (v9 != varBoolean.value) {
      result = result + "\ncompare of data failed - received "
          + varBoolean.value + ",  expected: " + v9;
      pass = false;
    }
    if (v10 != varByte.value) {
      result = result + "\ncompare of data failed - received " + varByte.value
          + ",  expected: " + v10;
      pass = false;
    }
    if (!v11.equals(varQName.value)) {
      result = result + "\ncompare of data failed - received " + varQName.value
          + ",  expected: " + v11;
      pass = false;
    }
    if (!JAXRPC_Data.compareCalendars(v12, varDateTime.value)) {

      result = result + "\ncompare of data failed - received "
          + varDateTime.value + ",  expected: " + v12;
      pass = false;
    }
    if (!v13.equals(varSoapString.value)) {
      result = result + "\ncompare of data failed - received "
          + varSoapString.value + ",  expected: " + v13;
      pass = false;
    }
    if (!v14.equals(varSoapBoolean.value)) {
      result = result + "\ncompare of data failed - received "
          + varSoapBoolean.value + ",  expected: " + v14;
      pass = false;
    }
    if (!v15.equals(varSoapFloat.value)) {
      result = result + "\ncompare of data failed - received "
          + varSoapFloat.value + ",  expected: " + v15;
      pass = false;
    }
    if (!v16.equals(varSoapDouble.value)) {
      result = result + "\ncompare of data failed - received "
          + varSoapDouble.value + ",  expected: " + v16;
      pass = false;
    }
    if (!v17.equals(varSoapDecimal.value)) {
      result = result + "\ncompare of data failed - received "
          + varSoapDecimal.value + ",  expected: " + v17;
      pass = false;
    }
    if (!v18.equals(varSoapInt.value)) {
      result = result + "\ncompare of data failed - received "
          + varSoapInt.value + ",  expected: " + v18;
      pass = false;
    }
    if (!v19.equals(varSoapShort.value)) {
      result = result + "\ncompare of data failed - received "
          + varSoapShort.value + ",  expected: " + v19;
      pass = false;
    }
    if (!v20.equals(varSoapByte.value)) {
      result = result + "\ncompare of data failed - received "
          + varSoapByte.value + ",  expected: " + v20;
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(v21, varBase64Binary.value, "byte")) {
      result = result + "\ncompare of data failed - received "
          + varBase64Binary.value + ",  expected: " + v21;
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(v22, varHexBinary.value, "byte")) {
      result = result + "\ncompare of data failed - received "
          + varHexBinary.value + ",  expected: " + v22;
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(v23, varSoapBase64.value, "byte")) {
      result = result + "\ncompare of data failed - received "
          + varSoapBase64.value + ",  expected: " + v23;
      pass = false;
    }

    if (!pass) {
      throw new RemoteException(result);
    }

    varString.value = new String("String4");
    varInteger.value = new BigInteger("3512360");
    varInt.value = Integer.MAX_VALUE;
    varLong.value = Long.MAX_VALUE;
    varShort.value = Short.MAX_VALUE;
    varDecimal.value = new BigDecimal("3512360.1456");
    varFloat.value = Float.MAX_VALUE;
    varDouble.value = Double.MAX_VALUE;
    varBoolean.value = true;
    varByte.value = Byte.MAX_VALUE;
    varQName.value = new QName("String5");
    varDateTime.value = new GregorianCalendar(96, 5, 2);
    varSoapString.value = new String("String6");
    varSoapBoolean.value = new Boolean(true);
    varSoapFloat.value = new Float(Float.MAX_VALUE);
    varSoapDouble.value = new Double(Double.MAX_VALUE);
    varSoapDecimal.value = new BigDecimal("3512360.1111");
    varSoapInt.value = new Integer(Integer.MAX_VALUE);
    varSoapShort.value = new Short(Short.MAX_VALUE);
    varSoapByte.value = new Byte(Byte.MAX_VALUE);
    varBase64Binary.value = JAXRPC_Data.byte_data2;
    varHexBinary.value = JAXRPC_Data.byte_data2;
    varSoapBase64.value = JAXRPC_Data.byte_data2;
  }

  public void echoInEnum(
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.EnumString varEnumString,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.EnumInteger varEnumInteger,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.EnumInt varEnumInt,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.EnumLong varEnumLong,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.EnumShort varEnumShort,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.EnumDecimal varEnumDecimal,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.EnumFloat varEnumFloat,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.EnumDouble varEnumDouble,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.EnumByte varEnumByte)
      throws java.rmi.RemoteException {
    boolean pass = true;
    String v1 = "String1";
    BigInteger v2 = new BigInteger("3512359");
    int v3 = (int) Integer.MIN_VALUE;
    long v4 = (long) Long.MIN_VALUE;
    short v5 = (short) Short.MIN_VALUE;
    BigDecimal v6 = new BigDecimal("3512359.1456");
    float v7 = (float) -1.00000000;
    double v8 = (double) -1.00000000;
    byte v9 = (byte) Byte.MIN_VALUE;
    String result = "";
    if (!v1.equals(varEnumString.getValue())) {
      result = "compare of data failed for String - received "
          + varEnumString.getValue() + ",  expected: " + v1;
      pass = false;
    }
    if (!v2.equals(varEnumInteger.getValue())) {
      result = result + "\ncompare of data failed for BigInteger - received "
          + varEnumInteger.getValue() + ",  expected: " + v2;
      pass = false;
    }
    if (v3 != varEnumInt.getValue()) {
      result = result + "\ncompare of data failed for int - received "
          + varEnumInt.getValue() + ",  expected: " + v3;
      pass = false;
    }
    if (v4 != varEnumLong.getValue()) {
      result = result + "\ncompare of data failed for long - received "
          + varEnumLong.getValue() + ",  expected: " + v4;
      pass = false;
    }
    if (v5 != varEnumShort.getValue()) {
      result = result + "\ncompare of data failed for short - received "
          + varEnumShort.getValue() + ",  expected: " + v5;
      pass = false;
    }
    if (!v6.equals(varEnumDecimal.getValue())) {
      result = result + "\ncompare of data failed for decimal - received "
          + varEnumDecimal.getValue() + ",  expected: " + v6;
      pass = false;
    }
    if (v7 != varEnumFloat.getValue()) {
      result = result + "\ncompare of data failed for float - received "
          + varEnumFloat.getValue() + ",  expected: " + v7;
      pass = false;
    }
    if (v8 != varEnumDouble.getValue()) {
      result = result + "\ncompare of data failed for double - received "
          + varEnumDouble.getValue() + ",  expected: " + v8;
      pass = false;
    }
    if (v9 != varEnumByte.getValue()) {
      result = result + "\ncompare of data failed for byte - received "
          + varEnumByte.getValue() + ",  expected: " + v9;
      pass = false;
    }

    if (!pass) {
      throw new RemoteException(result);
    }

    varEnumString.fromValue("String2");
    varEnumInteger.fromValue(new BigInteger("3512360"));
    varEnumInt.fromValue(Integer.MAX_VALUE);
    varEnumLong.fromValue(Long.MAX_VALUE);
    varEnumShort.fromValue(Short.MAX_VALUE);
    varEnumDecimal.fromValue(new BigDecimal("3512360.1456"));
    varEnumFloat.fromString("3.00000000");
    varEnumDouble.fromString("3.0000000000000");
    varEnumByte.fromValue(Byte.MAX_VALUE);
  }

  public void echoOutEnum(
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumStringHolder varEnumString,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumIntegerHolder varEnumInteger,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumIntHolder varEnumInt,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumLongHolder varEnumLong,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumShortHolder varEnumShort,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumDecimalHolder varEnumDecimal,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumFloatHolder varEnumFloat,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumDoubleHolder varEnumDouble,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumByteHolder varEnumByte)
      throws RemoteException {

    varEnumString.value = EnumString.fromValue("String2");
    varEnumInteger.value = EnumInteger.fromValue(new BigInteger("3512360"));
    varEnumInt.value = EnumInt.fromValue(Integer.MAX_VALUE);
    varEnumLong.value = EnumLong.fromValue(Long.MAX_VALUE);
    varEnumShort.value = EnumShort.fromValue(Short.MAX_VALUE);
    varEnumDecimal.value = EnumDecimal
        .fromValue(new BigDecimal("3512360.1456"));
    varEnumFloat.value = EnumFloat.fromString("3.00000000");
    varEnumDouble.value = EnumDouble.fromString("3.0000000000000");
    varEnumByte.value = EnumByte.fromValue(Byte.MAX_VALUE);
  }

  public void echoInOutEnum(
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumStringHolder varEnumString,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumIntegerHolder varEnumInteger,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumIntHolder varEnumInt,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumLongHolder varEnumLong,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumShortHolder varEnumShort,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumDecimalHolder varEnumDecimal,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumFloatHolder varEnumFloat,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumDoubleHolder varEnumDouble,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.EnumByteHolder varEnumByte)
      throws RemoteException {

    boolean pass = true;
    String v1 = "String1";
    BigInteger v2 = new BigInteger("3512359");
    int v3 = (int) Integer.MIN_VALUE;
    long v4 = (long) Long.MIN_VALUE;
    short v5 = (short) Short.MIN_VALUE;
    BigDecimal v6 = new BigDecimal("3512359.1456");
    float v7 = (float) -1.00000000;
    double v8 = (double) -1.00000000;
    byte v9 = (byte) Byte.MIN_VALUE;
    String result = "";
    if (!v1.equals(varEnumString.value.getValue())) {
      result = "compare of data failed for String - received "
          + varEnumString.value.getValue() + ",  expected: " + v1;
      pass = false;
    }
    if (!v2.equals(varEnumInteger.value.getValue())) {
      result = result + "\ncompare of data failed for BigInteger - received "
          + varEnumInteger.value.getValue() + ",  expected: " + v2;
      pass = false;
    }
    if (v3 != varEnumInt.value.getValue()) {
      result = result + "\ncompare of data failed for int - received "
          + varEnumInt.value.getValue() + ",  expected: " + v3;
      pass = false;
    }
    if (v4 != varEnumLong.value.getValue()) {
      result = result + "\ncompare of data failed for long - received "
          + varEnumLong.value.getValue() + ",  expected: " + v4;
      pass = false;
    }
    if (v5 != varEnumShort.value.getValue()) {
      result = result + "\ncompare of data failed for short - received "
          + varEnumShort.value.getValue() + ",  expected: " + v5;
      pass = false;
    }
    if (!v6.equals(varEnumDecimal.value.getValue())) {
      result = result + "\ncompare of data failed for decimal - received "
          + varEnumDecimal.value.getValue() + ",  expected: " + v6;
      pass = false;
    }
    if (v7 != varEnumFloat.value.getValue()) {
      result = result + "\ncompare of data failed for float - received "
          + varEnumFloat.value.getValue() + ",  expected: " + v7;
      pass = false;
    }
    if (v8 != varEnumDouble.value.getValue()) {
      result = result + "\ncompare of data failed for double - received "
          + varEnumDouble.value.getValue() + ",  expected: " + v8;
      pass = false;
    }
    if (v9 != varEnumByte.value.getValue()) {
      result = result + "\ncompare of data failed for byte - received "
          + varEnumByte.value.getValue() + ",  expected: " + v9;
      pass = false;
    }

    if (!pass) {
      throw new RemoteException(result);
    }
    varEnumString.value = EnumString.fromValue("String2");
    varEnumInteger.value = EnumInteger.fromValue(new BigInteger("3512360"));
    varEnumInt.value = EnumInt.fromValue(Integer.MAX_VALUE);
    varEnumLong.value = EnumLong.fromValue(Long.MAX_VALUE);
    varEnumShort.value = EnumShort.fromValue(Short.MAX_VALUE);
    varEnumDecimal.value = EnumDecimal
        .fromValue(new BigDecimal("3512360.1456"));
    varEnumFloat.value = EnumFloat.fromString("3.00000000");
    varEnumDouble.value = EnumDouble.fromString("3.0000000000000");
    varEnumByte.value = EnumByte.fromValue(Byte.MAX_VALUE);
  }

  public void echoInStruct(
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.AllStruct varStruct)
      throws java.rmi.RemoteException {

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
    Calendar v12 = (Calendar) new GregorianCalendar(96, 5, 1);
    String v13 = "String3";
    Boolean v14 = new Boolean("false");
    Float v15 = new Float(Float.MIN_VALUE);
    Double v16 = new Double(Double.MIN_VALUE);
    BigDecimal v17 = new BigDecimal("3512359.1111");
    Integer v18 = new Integer(Integer.MIN_VALUE);
    Short v19 = new Short(Short.MIN_VALUE);
    Byte v20 = new Byte(Byte.MIN_VALUE);
    byte v21[] = JAXRPC_Data.byte_data;
    byte v22[] = JAXRPC_Data.byte_data;
    byte v23[] = JAXRPC_Data.byte_data;
    String result = "";
    if (!v1.equals(varStruct.getVarString())) {
      result = "compare of data failed for String - received "
          + varStruct.getVarString() + ",  expected: " + v1;
      pass = false;
    }
    if (!v2.equals(varStruct.getVarInteger())) {
      result = result + "\ncompare of data failed for BigInteger - received "
          + varStruct.getVarInteger() + ",  expected: " + v2;
      pass = false;
    }
    if (v3 != varStruct.getVarInt()) {
      result = result + "\ncompare of data failed for int - received "
          + varStruct.getVarInt() + ",  expected: " + v3;
      pass = false;
    }
    if (v4 != varStruct.getVarLong()) {
      result = result + "\ncompare of data failed for long - received "
          + varStruct.getVarLong() + ",  expected: " + v4;
      pass = false;
    }
    if (v5 != varStruct.getVarShort()) {
      result = result + "\ncompare of data failed for short - received "
          + varStruct.getVarShort() + ",  expected: " + v5;
      pass = false;
    }
    if (!v6.equals(varStruct.getVarDecimal())) {
      result = result + "\ncompare of data failed for decimal - received "
          + varStruct.getVarDecimal() + ",  expected: " + v6;
      pass = false;
    }
    if (v7 != varStruct.getVarFloat()) {
      result = result + "\ncompare of data failed for float - received "
          + varStruct.getVarFloat() + ",  expected: " + v7;
      pass = false;
    }
    if (v8 != varStruct.getVarDouble()) {
      result = result + "\ncompare of data failed for double - received "
          + varStruct.getVarDouble() + ",  expected: " + v8;
      pass = false;
    }
    if (v9 != varStruct.isVarBoolean()) {
      result = result + "\ncompare of data failed for boolean - received "
          + varStruct.isVarBoolean() + ",  expected: " + v9;
      pass = false;
    }
    if (v10 != varStruct.getVarByte()) {
      result = result + "\ncompare of data failed for byte - received "
          + varStruct.getVarByte() + ",  expected: " + v10;
      pass = false;
    }
    if (!v11.equals(varStruct.getVarQName())) {
      result = result + "\ncompare of data failed for QName - received "
          + varStruct.getVarQName() + ",  expected: " + v11;
      pass = false;
    }
    if (!JAXRPC_Data.compareCalendars(v12, varStruct.getVarDateTime())) {
      result = result + "\ncompare of data failed for Calendar - received "
          + varStruct.getVarDateTime() + ",  expected: " + v12;
      pass = false;
    }
    if (!v13.equals(varStruct.getVarSoapString())) {
      result = result + "\ncompare of data failed for SoapString - received "
          + varStruct.getVarSoapString() + ",  expected: " + v13;
      pass = false;
    }
    if (!v14.equals(varStruct.getVarSoapBoolean())) {
      result = result + "\ncompare of data failed for SoapBoolean - received "
          + varStruct.getVarSoapBoolean() + ",  expected: " + v14;
      pass = false;
    }
    if (!v15.equals(varStruct.getVarSoapFloat())) {
      result = result + "\ncompare of data failed for SoapFloat - received "
          + varStruct.getVarSoapFloat() + ",  expected: " + v15;
      pass = false;
    }
    if (!v16.equals(varStruct.getVarSoapDouble())) {
      result = result + "\ncompare of data failed for SoapDouble - received "
          + varStruct.getVarSoapDouble() + ",  expected: " + v16;
      pass = false;
    }
    if (!v17.equals(varStruct.getVarSoapDecimal())) {
      result = result + "\ncompare of data failed for SoapDecimal - received "
          + varStruct.getVarSoapDecimal() + ",  expected: " + v17;
      pass = false;
    }
    if (!v18.equals(varStruct.getVarSoapInt())) {
      result = result + "\ncompare of data failed for SoapInt - received "
          + varStruct.getVarSoapInt() + ",  expected: " + v18;
      pass = false;
    }
    if (!v19.equals(varStruct.getVarSoapShort())) {
      result = result + "\ncompare of data failed for SoapShort - received "
          + varStruct.getVarSoapShort() + ",  expected: " + v19;
      pass = false;
    }
    if (!v20.equals(varStruct.getVarSoapByte())) {
      result = result + "\ncompare of data failed for SoapByte - received "
          + varStruct.getVarSoapByte() + ",  expected: " + v20;
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(v21, varStruct.getVarBase64Binary(),
        "byte")) {
      result = result + "\ncompare of data failed for Base64Binary - received "
          + varStruct.getVarBase64Binary() + ",  expected: " + v21;
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(v22, varStruct.getVarHexBinary(),
        "byte")) {
      result = result + "\ncompare of data failed for HexBinary - received "
          + varStruct.getVarHexBinary() + ",  expected: " + v22;
      pass = false;
    }
    if (!JAXRPC_Data.compareArrayValues(v23, varStruct.getVarSoapBase64(),
        "byte")) {
      result = result + "\ncompare of data failed for SoapBase64 - received "
          + varStruct.getVarSoapBase64() + ",  expected: " + v23;
      pass = false;
    }

    if (!pass) {
      throw new RemoteException(result);
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
    varStruct.setVarDateTime((Calendar) new GregorianCalendar(96, 5, 2));
    varStruct.setVarSoapString("String6");
    varStruct.setVarSoapBoolean(new Boolean("true"));
    varStruct.setVarSoapFloat(new Float(Float.MAX_VALUE));
    varStruct.setVarSoapDouble(new Double(Double.MAX_VALUE));
    varStruct.setVarSoapDecimal(new BigDecimal("3512360.1111"));
    varStruct.setVarSoapInt(new Integer(Integer.MAX_VALUE));
    varStruct.setVarSoapShort(new Short(Short.MAX_VALUE));
    varStruct.setVarSoapByte(new Byte(Byte.MAX_VALUE));
    varStruct.setVarBase64Binary(JAXRPC_Data.byte_data2);
    varStruct.setVarHexBinary(JAXRPC_Data.byte_data2);
    varStruct.setVarSoapBase64(JAXRPC_Data.byte_data2);
  }

  public void echoInOutSimpleTypesArray(
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.ArrayOfstringHolder varString,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.ArrayOfintegerHolder varInteger,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.ArrayOfintHolder varInt,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.ArrayOflongHolder varLong,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.ArrayOfshortHolder varShort,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.ArrayOfdecimalHolder varDecimal,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.ArrayOffloatHolder varFloat,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.ArrayOfdoubleHolder varDouble,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.ArrayOfbooleanHolder varBoolean,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.ArrayOfbyteHolder varByte,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.ArrayOfQNameHolder varQName,
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.ArrayOfdateTimeHolder varDateTime)
      throws java.rmi.RemoteException {

    varString.value = null;
    varInteger.value = null;
    varInt.value = null;
    varLong.value = null;
    varShort.value = null;
    varDecimal.value = null;
    varFloat.value = null;
    varDouble.value = null;
    varBoolean.value = null;
    varByte.value = null;
    varQName.value = null;
    varDateTime.value = null;

    varString.value = JAXRPC_Data.String_nonull_data;
    varInteger.value = JAXRPC_Data.BigInteger_nonull_data;
    varInt.value = JAXRPC_Data.int_data;
    varLong.value = JAXRPC_Data.long_data;
    varShort.value = JAXRPC_Data.short_data;
    varDecimal.value = JAXRPC_Data.BigDecimal_nonull_data;
    varFloat.value = JAXRPC_Data.float_data;
    varDouble.value = JAXRPC_Data.double_data;
    varBoolean.value = JAXRPC_Data.boolean_data;
    varByte.value = JAXRPC_Data.byte_data;
    varQName.value = JAXRPC_Data.QName_nonull_data;
    varDateTime.value = JAXRPC_Data.GregorianCalendar_nonull_data;

  }

  public void echoInOutBook(
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.BookHolder varBook)
      throws java.rmi.RemoteException {
    Book b = varBook.value;
    b.setAuthor("author1");
    b.setTitle("title1");
    b.setIsbn(1);
    varBook.value = b;
  }

  public void echoInOutBookArray(
      com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.parametermodetest.holders.ArrayOfBookHolder varBook)
      throws java.rmi.RemoteException {
    Book[] b = varBook.value;
    if (b.length != 2)
      throw new RemoteException("array size not 2");
    String author = b[0].getAuthor();
    String title = b[0].getTitle();
    int isbn = b[0].getIsbn();
    b[0].setAuthor(b[1].getAuthor());
    b[0].setTitle(b[1].getTitle());
    b[0].setIsbn(b[1].getIsbn());
    b[1].setAuthor(author);
    b[1].setTitle(title);
    b[1].setIsbn(isbn);
    varBook.value = b;
  }

}
