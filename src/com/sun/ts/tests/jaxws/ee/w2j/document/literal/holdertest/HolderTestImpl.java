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
import com.sun.ts.tests.jaxws.common.*;

import jakarta.xml.ws.WebServiceException;
import java.rmi.AccessException;

import java.util.Calendar;
import java.math.BigInteger;
import java.math.BigDecimal;
import javax.xml.namespace.QName;
import javax.xml.datatype.*;
import jakarta.xml.ws.Holder;

import javax.imageio.metadata.IIOMetadataNode;

import com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.*;

// Service Implementation Class - as outlined in JAX-WS Specification

import jakarta.jws.WebService;

@WebService(portName = "HolderTestPort", serviceName = "HolderTestService", targetNamespace = "http://holdertest.org/wsdl", wsdlLocation = "WEB-INF/wsdl/WSW2JDLHolderTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.HolderTest")
public class HolderTestImpl implements HolderTest {
  private static DatatypeFactory dtfactory = null;

  private static final boolean debug = true;
  static {
    try {
      dtfactory = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) {
      TestUtil.logMsg("Could not configure DatatypeFactory object");
      TestUtil.printStackTrace(e);
    }
  }

  public void echoInOutStringTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.StringElement> param) {
    System.out.println("Entering echoInOutStringTypes()");
    String expected = "String1";
    String actual = param.value.getString();
    if (!expected.equals(actual)) {
      String result = "compare of data failed - received " + actual
          + ",  expected: " + expected;
      throw new WebServiceException(result);
    }
    param.value.setString("String4");
    System.out.println("Leaving echoInOutStringTypes()");
  }

  public void echoInOutIntegerTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.IntegerElement> param) {
    System.out.println("Entering echoInOutIntegerTypes()");
    BigInteger expected = new BigInteger("3512359");
    BigInteger actual = param.value.getInteger();
    if (!expected.equals(actual)) {
      String result = "compare of data failed - received " + actual
          + ",  expected: " + expected;
      throw new WebServiceException(result);
    }
    param.value.setInteger(new BigInteger("3512360"));
    System.out.println("Leaving echoInOutIntegerTypes()");
  }

  public void echoInOutIntTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.IntElement> param) {
    System.out.println("Entering echoInOutIntTypes()");
    int expected = (int) Integer.MIN_VALUE;
    int actual = param.value.getInt();
    if (expected != actual) {
      String result = "compare of data failed - received " + actual
          + ",  expected: " + expected;
      throw new WebServiceException(result);
    }
    param.value.setInt(Integer.MAX_VALUE);
    System.out.println("Leaving echoInOutIntTypes()");
  }

  public void echoInOutLongTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.LongElement> param) {
    System.out.println("Entering echoInOutLongTypes()");
    long expected = (long) Long.MIN_VALUE;
    long actual = param.value.getLong();
    if (expected != actual) {
      String result = "compare of data failed - received " + actual
          + ",  expected: " + expected;
      throw new WebServiceException(result);
    }
    param.value.setLong(Long.MAX_VALUE);
    System.out.println("Leaving echoInOutLongTypes()");
  }

  public void echoInOutShortTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.ShortElement> param) {
    System.out.println("Entering echoInOutShortTypes()");
    short expected = (short) Short.MIN_VALUE;
    short actual = param.value.getShort();
    if (expected != actual) {
      String result = "compare of data failed - received " + actual
          + ",  expected: " + expected;
      throw new WebServiceException(result);
    }
    param.value.setShort(Short.MAX_VALUE);
    System.out.println("Leaving echoInOutShortTypes()");
  }

  public void echoInOutDecimalTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.DecimalElement> param) {
    System.out.println("Entering echoInOutDecimalTypes()");
    BigDecimal expected = new BigDecimal("3512359.1456");
    BigDecimal actual = param.value.getDecimal();
    if (!expected.equals(actual)) {
      String result = "compare of data failed - received " + actual
          + ",  expected: " + expected;
      throw new WebServiceException(result);
    }
    param.value.setDecimal(new BigDecimal("3512360.1456"));
    System.out.println("Leaving echoInOutDecimalTypes()");
  }

  public void echoInOutFloatTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.FloatElement> param) {
    System.out.println("Entering echoInOutFloatTypes()");
    float expected = (float) Float.MIN_VALUE;
    float actual = param.value.getFloat();
    if (expected != actual) {
      String result = "compare of data failed - received " + actual
          + ",  expected: " + expected;
      throw new WebServiceException(result);
    }
    param.value.setFloat(Float.MAX_VALUE);
    System.out.println("Leaving echoInOutFloatTypes()");
  }

  public void echoInOutDoubleTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.DoubleElement> param) {
    System.out.println("Entering echoInOutDoubleTypes()");
    double expected = (double) Double.MIN_VALUE;
    double actual = param.value.getDouble();
    if (expected != actual) {
      String result = "compare of data failed - received " + actual
          + ",  expected: " + expected;
      throw new WebServiceException(result);
    }
    param.value.setDouble(Double.MAX_VALUE);
    System.out.println("Leaving echoInOutDoubleTypes()");
  }

  public void echoInOutBooleanTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.BooleanElement> param) {
    System.out.println("Entering echoInOutBooleanTypes()");
    boolean expected = false;
    boolean actual = param.value.isBoolean();
    if (expected != actual) {
      String result = "compare of data failed - received " + actual
          + ",  expected: " + expected;
      throw new WebServiceException(result);
    }
    param.value.setBoolean(true);
    System.out.println("Leaving echoInOutBooleanTypes()");
  }

  public void echoInOutByteTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.ByteElement> param) {
    System.out.println("Entering echoInOutByteTypes()");
    byte expected = (byte) Byte.MIN_VALUE;
    byte actual = param.value.getByte();
    if (expected != actual) {
      String result = "compare of data failed - received " + actual
          + ",  expected: " + expected;
      throw new WebServiceException(result);
    }
    param.value.setByte(Byte.MAX_VALUE);
    System.out.println("Leaving echoInOutByteTypes()");
  }

  public void echoInOutQNameTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.QNameElement> param) {
    System.out.println("Entering echoInOutQNameTypes()");
    QName expected = new QName("String2");
    ;
    QName actual = param.value.getQName();
    if (!expected.equals(actual)) {
      String result = "compare of data failed - received " + actual
          + ",  expected: " + expected;
      throw new WebServiceException(result);
    }
    param.value.setQName(new QName("String5"));
    System.out.println("Leaving echoInOutQNameTypes()");
  }

  public void echoInOutDateTimeTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.DateTimeElement> param) {
    System.out.println("Entering echoInOutDateTimeTypes()");
    XMLGregorianCalendar expected = dtfactory.newXMLGregorianCalendar(96, 5, 1,
        0, 30, 0, 0, 0);
    XMLGregorianCalendar actual = param.value.getDateTime();
    if (!expected.equals(actual)) {
      String result = "compare of data failed - received " + actual
          + ",  expected: " + expected;
      throw new WebServiceException(result);
    }
    param.value.setDateTime(
        dtfactory.newXMLGregorianCalendar(96, 5, 2, 0, 30, 0, 0, 0));
    System.out.println("Leaving echoInOutDateTimeTypes()");
  }

  public void echoInOutArrayStringTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.ArrayOfString> param) {
    boolean pass = true;
    StringBuffer result = new StringBuffer();
    System.out.println("Entering echoInOutArrayStringTypes()");
    int size = param.value.getArrayOfString().size();
    if (size != JAXWS_Data.String_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfString>, expected: "
          + JAXWS_Data.String_nonull_data.length + " got: " + size);
    } else {
      for (int i = 0; i < JAXWS_Data.String_nonull_data.length; i++) {
        if (!param.value.getArrayOfString().get(i)
            .equals(JAXWS_Data.String_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOfString Value mismatch - expected:"
              + JAXWS_Data.String_nonull_data[i]);
          result.append("actual:" + param.value.getArrayOfString().get(i));
        }
      }

    }
    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    param.value.getArrayOfString().clear();
    for (int i = JAXWS_Data.String_nonull_data.length
        - 1, j = 0; i >= 0; i--, j++) {
      param.value.getArrayOfString().add(JAXWS_Data.String_nonull_data[i]);
      if (debug)
        System.out.println(
            "array[" + j + "]=" + param.value.getArrayOfString().get(j));
    }
    System.out.println("Leaving echoInOutArrayStringTypes()");
  }

  public void echoInOutArrayIntegerTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.ArrayOfInteger> param) {
    boolean pass = true;
    StringBuffer result = new StringBuffer();
    System.out.println("Entering echoInOutArrayIntegerTypes()");
    int size = param.value.getArrayOfInteger().size();
    if (size != JAXWS_Data.BigInteger_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfInteger>, expected: "
          + JAXWS_Data.BigInteger_nonull_data.length + " got: " + size);
    } else {
      for (int i = 0; i < JAXWS_Data.BigInteger_nonull_data.length; i++) {
        if (!param.value.getArrayOfInteger().get(i)
            .equals(JAXWS_Data.BigInteger_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOfInteger Value mismatch - expected:"
              + JAXWS_Data.BigInteger_nonull_data[i]);
          result.append("actual:" + param.value.getArrayOfInteger().get(i));
        }
      }
    }
    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    param.value.getArrayOfInteger().clear();
    for (int i = JAXWS_Data.BigInteger_nonull_data.length
        - 1, j = 0; i >= 0; i--, j++) {
      param.value.getArrayOfInteger().add(JAXWS_Data.BigInteger_nonull_data[i]);
      if (debug)
        System.out.println(
            "array[" + j + "]=" + param.value.getArrayOfInteger().get(j));
    }
    System.out.println("Leaving echoInOutArrayIntegerTypes()");
  }

  public void echoInOutArrayIntTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.ArrayOfInt> param) {
    boolean pass = true;
    StringBuffer result = new StringBuffer();
    System.out.println("Entering echoInOutArrayIntTypes()");
    int size = param.value.getArrayOfInt().size();
    if (size != JAXWS_Data.int_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfInt>, expected: "
          + JAXWS_Data.int_data.length + " got: " + size);
    } else {
      for (int i = 0; i < JAXWS_Data.int_data.length; i++) {
        if (param.value.getArrayOfInt().get(i) != JAXWS_Data.int_data[i]) {
          pass = false;
          result.append("\nArrayOfInt Value mismatch - expected:"
              + JAXWS_Data.int_data[i]);
          result.append("actual:" + param.value.getArrayOfInt().get(i));
        }
      }
    }
    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    param.value.getArrayOfInt().clear();
    for (int i = JAXWS_Data.int_data.length - 1, j = 0; i >= 0; i--, j++) {
      param.value.getArrayOfInt().add(JAXWS_Data.int_data[i]);
      if (debug)
        System.out
            .println("array[" + j + "]=" + param.value.getArrayOfInt().get(j));
    }
    System.out.println("Leaving echoInOutArrayIntTypes()");
  }

  public void echoInOutArrayLongTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.ArrayOfLong> param) {
    boolean pass = true;
    StringBuffer result = new StringBuffer();
    System.out.println("Entering echoInOutArrayLongTypes()");
    int size = param.value.getArrayOfLong().size();
    if (size != JAXWS_Data.long_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfLong>, expected: "
          + JAXWS_Data.long_data.length + " got: " + size);
    } else {
      for (int i = 0; i < JAXWS_Data.long_data.length; i++) {
        if (param.value.getArrayOfLong().get(i) != JAXWS_Data.long_data[i]) {
          pass = false;
          result.append("\nArrayOfLong Value mismatch - expected:"
              + JAXWS_Data.long_data[i]);
          result.append("actual:" + param.value.getArrayOfLong().get(i));
        }
      }

    }
    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    param.value.getArrayOfLong().clear();
    for (int i = JAXWS_Data.long_data.length - 1, j = 0; i >= 0; i--, j++) {
      param.value.getArrayOfLong().add(JAXWS_Data.long_data[i]);
      if (debug)
        System.out
            .println("array[" + j + "]=" + param.value.getArrayOfLong().get(j));
    }
    System.out.println("Leaving echoInOutArrayLongTypes()");
  }

  public void echoInOutArrayShortTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.ArrayOfShort> param) {
    boolean pass = true;
    StringBuffer result = new StringBuffer();
    System.out.println("Entering echoInOutArrayShortTypes()");
    int size = param.value.getArrayOfShort().size();
    if (size != JAXWS_Data.short_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfShort>, expected: "
          + JAXWS_Data.short_data.length + " got: " + size);
    } else {
      for (int i = 0; i < JAXWS_Data.short_data.length; i++) {
        if (param.value.getArrayOfShort().get(i) != JAXWS_Data.short_data[i]) {
          pass = false;
          result.append("\nArrayOfShort Value mismatch - expected:"
              + JAXWS_Data.short_data[i]);
          result.append("actual:" + param.value.getArrayOfShort().get(i));
        }
      }
    }
    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    param.value.getArrayOfShort().clear();
    for (int i = JAXWS_Data.short_data.length - 1, j = 0; i >= 0; i--, j++) {
      param.value.getArrayOfShort().add(JAXWS_Data.short_data[i]);
      if (debug)
        System.out.println(
            "array[" + j + "]=" + param.value.getArrayOfShort().get(j));
    }
    System.out.println("Leaving echoInOutArrayShortTypes()");
  }

  public void echoInOutArrayDecimalTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.ArrayOfDecimal> param) {
    boolean pass = true;
    StringBuffer result = new StringBuffer();
    System.out.println("Entering echoInOutArrayDecimalTypes()");
    int size = param.value.getArrayOfDecimal().size();
    if (size != JAXWS_Data.BigDecimal_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfDecimal>, expected: "
          + JAXWS_Data.BigDecimal_nonull_data.length + " got: " + size);
    } else {
      for (int i = 0; i < JAXWS_Data.BigDecimal_nonull_data.length; i++) {
        if (!param.value.getArrayOfDecimal().get(i)
            .equals(JAXWS_Data.BigDecimal_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOfDecimal Value mismatch - expected:"
              + JAXWS_Data.BigDecimal_nonull_data[i]);
          result.append("actual:" + param.value.getArrayOfDecimal().get(i));
        }
      }
    }
    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    param.value.getArrayOfDecimal().clear();
    for (int i = JAXWS_Data.BigDecimal_nonull_data.length
        - 1, j = 0; i >= 0; i--, j++) {
      param.value.getArrayOfDecimal().add(JAXWS_Data.BigDecimal_nonull_data[i]);
      if (debug)
        System.out.println(
            "array[" + j + "]=" + param.value.getArrayOfDecimal().get(j));
    }
    System.out.println("Leaving echoInOutArrayDecimalTypes()");
  }

  public void echoInOutArrayFloatTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.ArrayOfFloat> param) {
    boolean pass = true;
    StringBuffer result = new StringBuffer();
    System.out.println("Entering echoInOutArrayFloatTypes()");
    int size = param.value.getArrayOfFloat().size();
    if (size != JAXWS_Data.float_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfFloat>, expected: "
          + JAXWS_Data.float_data.length + " got: " + size);
    } else {
      for (int i = 0; i < JAXWS_Data.float_data.length; i++) {
        if (param.value.getArrayOfFloat().get(i) != JAXWS_Data.float_data[i]) {
          pass = false;
          result.append("\nArrayOfFloat Value mismatch - expected:"
              + JAXWS_Data.float_data[i]);
          result.append("actual:" + param.value.getArrayOfFloat().get(i));
        }
      }

    }
    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    param.value.getArrayOfFloat().clear();
    for (int i = JAXWS_Data.float_data.length - 1, j = 0; i >= 0; i--, j++) {
      param.value.getArrayOfFloat().add(JAXWS_Data.float_data[i]);
      if (debug)
        System.out.println(
            "array[" + j + "]=" + param.value.getArrayOfFloat().get(j));
    }
    System.out.println("Leaving echoInOutArrayFloatTypes()");
  }

  public void echoInOutArrayDoubleTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.ArrayOfDouble> param) {
    boolean pass = true;
    StringBuffer result = new StringBuffer();
    System.out.println("Entering echoInOutArrayDoubleTypes()");
    int size = param.value.getArrayOfDouble().size();
    if (size != JAXWS_Data.double_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfDouble>, expected: "
          + JAXWS_Data.double_data.length + " got: " + size);
    } else {
      for (int i = 0; i < JAXWS_Data.double_data.length; i++) {
        if (param.value.getArrayOfDouble()
            .get(i) != JAXWS_Data.double_data[i]) {
          pass = false;
          result.append("\nArrayOfDouble Value mismatch - expected:"
              + JAXWS_Data.double_data[i]);
          result.append("actual:" + param.value.getArrayOfDouble().get(i));
        }
      }
    }
    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    param.value.getArrayOfDouble().clear();
    for (int i = JAXWS_Data.double_data.length - 1, j = 0; i >= 0; i--, j++) {
      param.value.getArrayOfDouble().add(JAXWS_Data.double_data[i]);
      if (debug)
        System.out.println(
            "array[" + j + "]=" + param.value.getArrayOfDouble().get(j));
    }
    System.out.println("Leaving echoInOutArrayDoubleTypes()");
  }

  public void echoInOutArrayBooleanTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.ArrayOfBoolean> param) {
    boolean pass = true;
    StringBuffer result = new StringBuffer();
    System.out.println("Entering echoInOutArrayBooleanTypes()");
    int size = param.value.getArrayOfBoolean().size();
    if (size != JAXWS_Data.boolean_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfBoolean>, expected: "
          + JAXWS_Data.boolean_data.length + " got: " + size);
    } else {
      for (int i = 0; i < JAXWS_Data.boolean_data.length; i++) {
        if (param.value.getArrayOfBoolean()
            .get(i) != JAXWS_Data.boolean_data[i]) {
          pass = false;
          result.append("\nArrayOfBoolean Value mismatch - expected:"
              + JAXWS_Data.boolean_data[i]);
          result.append("actual:" + param.value.getArrayOfBoolean().get(i));
        }
      }
    }
    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    param.value.getArrayOfBoolean().clear();
    for (int i = JAXWS_Data.boolean_data.length - 1, j = 0; i >= 0; i--, j++) {
      param.value.getArrayOfBoolean().add(JAXWS_Data.boolean_data[i]);
      if (debug)
        System.out.println(
            "array[" + j + "]=" + param.value.getArrayOfBoolean().get(j));
    }
    System.out.println("Leaving echoInOutArrayBooleanTypes()");
  }

  public void echoInOutArrayByteTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.ArrayOfByte> param) {
    boolean pass = true;
    StringBuffer result = new StringBuffer();
    System.out.println("Entering echoInOutArrayByteTypes()");
    int size = param.value.getArrayOfByte().size();
    if (size != JAXWS_Data.byte_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfByte>, expected: "
          + JAXWS_Data.byte_data.length + " got: " + size);
    } else {
      for (int i = 0; i < JAXWS_Data.byte_data.length; i++) {
        if (param.value.getArrayOfByte().get(i) != JAXWS_Data.byte_data[i]) {
          pass = false;
          result.append("\nArrayOfByte Value mismatch - expected:"
              + JAXWS_Data.byte_data[i]);
          result.append("actual:" + param.value.getArrayOfByte().get(i));
        }
      }
    }
    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    param.value.getArrayOfByte().clear();
    for (int i = JAXWS_Data.byte_data.length - 1, j = 0; i >= 0; i--, j++) {
      param.value.getArrayOfByte().add(JAXWS_Data.byte_data[i]);
      if (debug)
        System.out
            .println("array[" + j + "]=" + param.value.getArrayOfByte().get(j));
    }
    System.out.println("Leaving echoInOutArrayByteTypes()");
  }

  public void echoInOutArrayQNameTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.ArrayOfQName> param) {
    boolean pass = true;
    StringBuffer result = new StringBuffer();
    System.out.println("Entering echoInOutArrayQNameTypes()");
    int size = param.value.getArrayOfQName().size();
    if (size != JAXWS_Data.QName_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfQName>, expected: "
          + JAXWS_Data.QName_nonull_data.length + " got: " + size);
    } else {
      for (int i = 0; i < JAXWS_Data.QName_nonull_data.length; i++) {
        if (!param.value.getArrayOfQName().get(i)
            .equals(JAXWS_Data.QName_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOfQName Value mismatch - expected:"
              + JAXWS_Data.QName_nonull_data[i]);
          result.append("actual:" + param.value.getArrayOfQName().get(i));
        }
      }
    }
    if (!pass) {
      throw new WebServiceException(result.toString());
    }

    param.value.getArrayOfQName().clear();
    for (int i = JAXWS_Data.QName_nonull_data.length
        - 1, j = 0; i >= 0; i--, j++) {
      param.value.getArrayOfQName().add(JAXWS_Data.QName_nonull_data[i]);
      if (debug)
        System.out.println(
            "array[" + j + "]=" + param.value.getArrayOfQName().get(j));
    }
    System.out.println("Leaving echoInOutArrayQNameTypes()");
  }

  public void echoInOutArrayDateTimeTypes(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.ArrayOfDateTime> param) {
    boolean pass = true;
    StringBuffer result = new StringBuffer();
    System.out.println("Entering echoInOutArrayDateTimeTypes()");
    int size = param.value.getArrayOfDateTime().size();
    if (size != JAXWS_Data.XMLGregorianCalendar_nonull_data.length) {
      pass = false;
      result.append("\nUnexpected size for Holder<ArrayOfDateTime>, expected: "
          + JAXWS_Data.XMLGregorianCalendar_nonull_data.length + " got: "
          + size);
    } else {
      for (int i = 0; i < JAXWS_Data.XMLGregorianCalendar_nonull_data.length; i++) {
        if (!JAXWS_Data.compareXMLGregorianCalendars(
            param.value.getArrayOfDateTime().get(i),
            JAXWS_Data.XMLGregorianCalendar_nonull_data[i])) {
          pass = false;
          result.append("\nArrayOfQName Value mismatch - expected:"
              + JAXWS_Data.XMLGregorianCalendar_nonull_data[i]);
          result.append("actual:" + param.value.getArrayOfDateTime().get(i));
        }
      }
    }
    if (!pass) {
      throw new WebServiceException(result.toString());
    }
    param.value.getArrayOfDateTime().clear();
    for (int i = JAXWS_Data.XMLGregorianCalendar_nonull_data.length
        - 1, j = 0; i >= 0; i--, j++) {
      param.value.getArrayOfDateTime()
          .add(JAXWS_Data.XMLGregorianCalendar_nonull_data[i]);
      if (debug)
        System.out.println(
            "array[" + j + "]=" + param.value.getArrayOfDateTime().get(j));
    }
    System.out.println("Leaving echoInOutArrayDateTimeTypes()");
  }

  public void echoInOutBook(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.Book> param) {
    System.out.println("Entering echoInOutBookTypes()");
    Book expected = new Book();
    expected.setAuthor("author0");
    expected.setTitle("title0");
    expected.setIsbn(0);
    Book actual = param.value;
    if (!actual.getAuthor().equals(expected.getAuthor())
        || !actual.getTitle().equals(expected.getTitle())
        || actual.getIsbn() != expected.getIsbn()) {
      StringBuffer result = new StringBuffer();
      result.append("compare of data failed:");

      result.append("received[" + actual.getAuthor() + "," + actual.getTitle()
          + "," + actual.getIsbn() + "] - ");
      result.append("expected[" + expected.getAuthor() + ","
          + expected.getTitle() + "," + expected.getIsbn() + "]");
      throw new WebServiceException(result.toString());
    }

    actual.setAuthor("author1");
    actual.setTitle("title1");
    actual.setIsbn(1);
    param.value = actual;
    System.out.println("Leaving echoInOutBookTypes()");

  }

  public void echoInOutBookArray(
      jakarta.xml.ws.Holder<com.sun.ts.tests.jaxws.ee.w2j.document.literal.holdertest.ArrayOfBook> param) {
    System.out.println("Entering echoInOutArrayBookTypes()");
    int size = param.value.getBook().size();
    if (size != 2) {
      System.out.println(
          "Unexpected size for Holder<ArrayOfBook>, expected: 2, got: " + size);
    }

    Book b0 = new Book();
    b0.setAuthor("author1");
    b0.setTitle("title1");
    b0.setIsbn(1);
    Book b1 = new Book();
    b1.setAuthor("author0");
    b1.setTitle("title0");
    b1.setIsbn(0);
    param.value.getBook().clear();
    param.value.getBook().add(b0);
    param.value.getBook().add(b1);
    System.out.println("Leaving echoInOutArrayBookTypes()");
  }
}
