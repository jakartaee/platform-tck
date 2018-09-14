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

package com.sun.ts.tests.jaxrpc.ee.wsi.rpc.literal.marshalltest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxrpc.common.*;

import java.io.ByteArrayInputStream;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.*;

import java.math.BigInteger;
import java.math.BigDecimal;
import javax.xml.namespace.QName;

// Service Implementation Class - as outlined in JAX-RPC Specification

public class MarshallTestImpl implements MarshallTest {

  // ====================================================================
  // Java Primitive Types
  // ====================================================================
  public BooleanTestResponse booleanTest(BooleanTest v) throws RemoteException {
    TestUtil.logTrace("booleanTest");
    TestUtil.logMsg("boolean=" + v.isBooleanValue());
    BooleanTestResponse r = new BooleanTestResponse();
    r.setBooleanValue(v.isBooleanValue());
    return r;
  }

  public ByteTestResponse byteTest(ByteTest v) throws RemoteException {
    TestUtil.logTrace("byteTest");
    TestUtil.logMsg("byte=" + v.getByteValue());
    ByteTestResponse r = new ByteTestResponse();
    r.setByteValue(v.getByteValue());
    return r;
  }

  public ShortTestResponse shortTest(ShortTest v) throws RemoteException {
    TestUtil.logTrace("shortTest");
    TestUtil.logMsg("short=" + v.getShortValue());
    ShortTestResponse r = new ShortTestResponse();
    r.setShortValue(v.getShortValue());
    return r;
  }

  public IntTestResponse intTest(IntTest v) throws RemoteException {
    TestUtil.logTrace("intTest");
    TestUtil.logMsg("int=" + v.getIntValue());
    IntTestResponse r = new IntTestResponse();
    r.setIntValue(v.getIntValue());
    return r;
  }

  public LongTestResponse longTest(LongTest v) throws RemoteException {
    TestUtil.logTrace("longTest");
    TestUtil.logMsg("long=" + v.getLongValue());
    LongTestResponse r = new LongTestResponse();
    r.setLongValue(v.getLongValue());
    return r;
  }

  public FloatTestResponse floatTest(FloatTest v) throws RemoteException {
    TestUtil.logTrace("floatTest");
    TestUtil.logMsg("float=" + v.getFloatValue());
    FloatTestResponse r = new FloatTestResponse();
    r.setFloatValue(v.getFloatValue());
    return r;
  }

  public DoubleTestResponse doubleTest(DoubleTest v) throws RemoteException {
    TestUtil.logTrace("doubleTest");
    TestUtil.logMsg("double=" + v.getDoubleValue());
    DoubleTestResponse r = new DoubleTestResponse();
    r.setDoubleValue(v.getDoubleValue());
    return r;
  }

  // ====================================================================
  // Java Primitive Type Arrays (Single Dimensional)
  // ====================================================================
  public BooleanArrayTestResponse booleanArrayTest(BooleanArrayTest v)
      throws RemoteException {
    TestUtil.logTrace("booleanArrayTest");
    JAXRPC_Data.dumpArrayValues(v.getBooleanArray(), "boolean");
    BooleanArrayTestResponse r = new BooleanArrayTestResponse();
    r.setBooleanArray(v.getBooleanArray());
    return r;
  }

  public ByteArrayTestResponse byteArrayTest(ByteArrayTest v)
      throws RemoteException {
    TestUtil.logTrace("byteArrayTest");
    JAXRPC_Data.dumpArrayValues(v.getByteArray(), "byte");
    ByteArrayTestResponse r = new ByteArrayTestResponse();
    r.setByteArray(v.getByteArray());
    return r;
  }

  public ShortArrayTestResponse shortArrayTest(ShortArrayTest v)
      throws RemoteException {
    TestUtil.logTrace("shortArrayTest");
    JAXRPC_Data.dumpArrayValues(v.getShortArray(), "short");
    ShortArrayTestResponse r = new ShortArrayTestResponse();
    r.setShortArray(v.getShortArray());
    return r;
  }

  public IntArrayTestResponse intArrayTest(IntArrayTest v)
      throws RemoteException {
    TestUtil.logTrace("intArrayTest");
    JAXRPC_Data.dumpArrayValues(v.getIntArray(), "int");
    IntArrayTestResponse r = new IntArrayTestResponse();
    r.setIntArray(v.getIntArray());
    return r;
  }

  public LongArrayTestResponse longArrayTest(LongArrayTest v)
      throws RemoteException {
    TestUtil.logTrace("longArrayTest");
    JAXRPC_Data.dumpArrayValues(v.getLongArray(), "long");
    LongArrayTestResponse r = new LongArrayTestResponse();
    r.setLongArray(v.getLongArray());
    return r;
  }

  public FloatArrayTestResponse floatArrayTest(FloatArrayTest v)
      throws RemoteException {
    TestUtil.logTrace("FloatArrayTest");
    JAXRPC_Data.dumpArrayValues(v.getFloatArray(), "float");
    FloatArrayTestResponse r = new FloatArrayTestResponse();
    r.setFloatArray(v.getFloatArray());
    return r;
  }

  public DoubleArrayTestResponse doubleArrayTest(DoubleArrayTest v)
      throws RemoteException {
    TestUtil.logTrace("doubleArrayTest");
    JAXRPC_Data.dumpArrayValues(v.getDoubleArray(), "double");
    DoubleArrayTestResponse r = new DoubleArrayTestResponse();
    r.setDoubleArray(v.getDoubleArray());
    return r;
  }

  public QNameArrayTestResponse qnameArrayTest(QNameArrayTest v)
      throws RemoteException {
    TestUtil.logTrace("qnameArrayTest");
    JAXRPC_Data.dumpArrayValues(v.getQnameArray_1(), "QName");
    QNameArrayTestResponse r = new QNameArrayTestResponse();
    r.setResult(v.getQnameArray_1());
    return r;
  }

  public QNameTestResponse qnameTest(QNameTest v) throws RemoteException {
    TestUtil.logTrace("qnameTest");
    TestUtil.logMsg("QName=" + v);
    QNameTestResponse res = new QNameTestResponse();
    res.setResult(v.getQname_1());
    return res;
  }

  public Base64BinaryTestResponse base64BinaryTest(Base64BinaryTest v)
      throws RemoteException {
    TestUtil.logTrace("base64BinaryTest");
    Base64BinaryTestResponse res = new Base64BinaryTestResponse();
    res.setResult(v.getBase64Binary_1());
    return res;
  }

  public HexBinaryTestResponse hexBinaryTest(HexBinaryTest v)
      throws RemoteException {
    TestUtil.logTrace("hexBinaryTest");
    HexBinaryTestResponse res = new HexBinaryTestResponse();
    res.setResult(v.getHexBinary_1());
    return res;
  }

  // ====================================================================
  // Standard Java Classes (Scalar, Single Dimensional Arrays)
  // ====================================================================
  public StringTestResponse stringTest(StringTest v) throws RemoteException {
    TestUtil.logTrace("stringTest");
    TestUtil.logMsg("String=" + v.getStringValue());
    StringTestResponse r = new StringTestResponse();
    r.setStringValue(v.getStringValue());
    return r;
  }

  public StringArrayTestResponse stringArrayTest(StringArrayTest v)
      throws RemoteException {
    TestUtil.logTrace("stringArrayTest");
    JAXRPC_Data.dumpArrayValues(v.getStringArray(), "String");
    StringArrayTestResponse r = new StringArrayTestResponse();
    r.setStringArray(v.getStringArray());
    return r;
  }

  public CalendarTestResponse calendarTest(CalendarTest v)
      throws RemoteException {
    TestUtil.logTrace("calendarTest");
    TestUtil.logMsg("Calendar=" + v.getCalendar());
    CalendarTestResponse r = new CalendarTestResponse();
    r.setCalendar(v.getCalendar());
    return r;
  }

  public CalendarArrayTestResponse calendarArrayTest(CalendarArrayTest v)
      throws RemoteException {
    TestUtil.logTrace("calendarArrayTest");
    JAXRPC_Data.dumpArrayValues(v.getCalendarArray(), "Calendar");
    CalendarArrayTestResponse r = new CalendarArrayTestResponse();
    r.setCalendarArray(v.getCalendarArray());
    return r;
  }

  public BigIntegerTestResponse bigIntegerTest(BigIntegerTest v)
      throws RemoteException {
    TestUtil.logTrace("bigIntegerTest");
    TestUtil.logMsg("BigInteger=" + v.getBigInteger());
    BigIntegerTestResponse r = new BigIntegerTestResponse();
    r.setBigInteger(v.getBigInteger());
    return r;
  }

  public BigIntegerArrayTestResponse bigIntegerArrayTest(BigIntegerArrayTest v)
      throws RemoteException {
    TestUtil.logTrace("bigIntegerArrayTest");
    JAXRPC_Data.dumpArrayValues(v.getBigIntegerArray(), "BigInteger");
    BigIntegerArrayTestResponse r = new BigIntegerArrayTestResponse();
    r.setBigIntegerArray(v.getBigIntegerArray());
    return r;
  }

  public BigDecimalTestResponse bigDecimalTest(BigDecimalTest v)
      throws RemoteException {
    TestUtil.logTrace("bigDecimalTest");
    TestUtil.logMsg("BigDecimal=" + v.getBigDecimal());
    BigDecimalTestResponse r = new BigDecimalTestResponse();
    r.setBigDecimal(v.getBigDecimal());
    return r;
  }

  public BigDecimalArrayTestResponse bigDecimalArrayTest(BigDecimalArrayTest v)
      throws RemoteException {
    TestUtil.logTrace("bigDecimalArrayTest");
    JAXRPC_Data.dumpArrayValues(v.getBigDecimalArray(), "BigDecimal");
    BigDecimalArrayTestResponse r = new BigDecimalArrayTestResponse();
    r.setBigDecimalArray(v.getBigDecimalArray());
    return r;
  }

  // ====================================================================
  // JavaBeans Class (Scalar, Single Dimensional Arrays)
  // ====================================================================
  public JavaBeanTestResponse javaBeanTest(JavaBeanTest v)
      throws RemoteException {
    TestUtil.logTrace("javaBeanTest");
    TestUtil.logMsg("JavaBean=" + v.getJavaBean());
    JavaBeanTestResponse r = new JavaBeanTestResponse();
    r.setJavaBean(v.getJavaBean());
    return r;
  }

  public JavaBeanArrayTestResponse javaBeanArrayTest(JavaBeanArrayTest v)
      throws RemoteException {
    TestUtil.logTrace("javaBeanArrayTest");
    JavaBeanArrayTestResponse r = new JavaBeanArrayTestResponse();
    r.setJavaBeanArray(v.getJavaBeanArray());
    return r;
  }

  // ====================================================================
  // The void type
  // ====================================================================
  public VoidTestResponse voidTest(VoidTest v) throws RemoteException {
    TestUtil.logTrace("voidTest");
    VoidTestResponse r = new VoidTestResponse();
    return r;
  }
}
