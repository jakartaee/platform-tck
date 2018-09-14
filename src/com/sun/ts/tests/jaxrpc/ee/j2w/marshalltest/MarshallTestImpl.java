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

package com.sun.ts.tests.jaxrpc.ee.j2w.marshalltest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxrpc.common.*;

import java.io.ByteArrayInputStream;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.*;

import java.math.BigInteger;
import java.math.BigDecimal;

// Service Implementation Class - as outlined in JAX-RPC Specification

public class MarshallTestImpl implements MarshallTest {

  // ====================================================================
  // Java Primitive Types
  // ====================================================================
  public boolean booleanTest(boolean v) throws RemoteException {
    TestUtil.logTrace("booleanTest");
    TestUtil.logMsg("boolean=" + v);
    return v;
  }

  public Boolean wrapperBooleanTest(Boolean v) throws RemoteException {
    TestUtil.logTrace("wrapperBooleanTest");
    TestUtil.logMsg("Boolean=" + v);
    return v;
  }

  public byte byteTest(byte v) throws RemoteException {
    TestUtil.logTrace("byteTest");
    TestUtil.logMsg("byte=" + v);
    return v;
  }

  public Byte wrapperByteTest(Byte v) throws RemoteException {
    TestUtil.logTrace("wrapperByteTest");
    TestUtil.logMsg("Byte=" + v);
    return v;
  }

  public short shortTest(short v) throws RemoteException {
    TestUtil.logTrace("shortTest");
    TestUtil.logMsg("short=" + v);
    return v;
  }

  public Short wrapperShortTest(Short v) throws RemoteException {
    TestUtil.logTrace("wrapperShortTest");
    TestUtil.logMsg("Short=" + v);
    return v;
  }

  public int intTest(int v) throws RemoteException {
    TestUtil.logTrace("intTest");
    TestUtil.logMsg("int=" + v);
    return v;
  }

  public Integer wrapperIntegerTest(Integer v) throws RemoteException {
    TestUtil.logTrace("wrapperIntegerTest");
    TestUtil.logMsg("Integer=" + v);
    return v;
  }

  public long longTest(long v) throws RemoteException {
    TestUtil.logTrace("longTest");
    TestUtil.logMsg("long=" + v);
    return v;
  }

  public Long wrapperLongTest(Long v) throws RemoteException {
    TestUtil.logTrace("wrapperLongTest");
    TestUtil.logMsg("Long=" + v);
    return v;
  }

  public float floatTest(float v) throws RemoteException {
    TestUtil.logTrace("floatTest");
    TestUtil.logMsg("float=" + v);
    return v;
  }

  public Float wrapperFloatTest(Float v) throws RemoteException {
    TestUtil.logTrace("wrapperFloatTest");
    TestUtil.logMsg("Float=" + v);
    return v;
  }

  public double doubleTest(double v) throws RemoteException {
    TestUtil.logTrace("doubleTest");
    TestUtil.logMsg("double=" + v);
    return v;
  }

  public Double wrapperDoubleTest(Double v) throws RemoteException {
    TestUtil.logTrace("wrapperDoubleTest");
    TestUtil.logMsg("Double=" + v);
    return v;
  }

  // ====================================================================
  // Java Primitive Type Arrays (Single and Multi Dimensional)
  // ====================================================================
  public boolean[] booleanArrayTest(boolean[] v) throws RemoteException {
    TestUtil.logTrace("booleanArrayTest");
    JAXRPC_Data.dumpArrayValues(v, "boolean");
    return v;
  }

  public byte[] byteArrayTest(byte[] v) throws RemoteException {
    TestUtil.logTrace("byteArrayTest");
    JAXRPC_Data.dumpArrayValues(v, "byte");
    return v;
  }

  public short[] shortArrayTest(short[] v) throws RemoteException {
    TestUtil.logTrace("shortArrayTest");
    JAXRPC_Data.dumpArrayValues(v, "short");
    return v;
  }

  public int[] intArrayTest(int[] v) throws RemoteException {
    TestUtil.logTrace("intArrayTest");
    JAXRPC_Data.dumpArrayValues(v, "int");
    return v;
  }

  public long[] longArrayTest(long[] v) throws RemoteException {
    TestUtil.logTrace("longArrayTest");
    JAXRPC_Data.dumpArrayValues(v, "long");
    return v;
  }

  public float[] floatArrayTest(float[] v) throws RemoteException {
    TestUtil.logTrace("floatArrayTest");
    JAXRPC_Data.dumpArrayValues(v, "float");
    return v;
  }

  public double[] doubleArrayTest(double[] v) throws RemoteException {
    TestUtil.logTrace("doubleArrayTest");
    JAXRPC_Data.dumpArrayValues(v, "double");
    return v;
  }

  public boolean[][] booleanMultiArrayTest(boolean[][] v)
      throws RemoteException {
    TestUtil.logTrace("booleanMultiArrayTest");
    JAXRPC_Data.dumpMultiArrayValues(v, "boolean");
    return v;
  }

  public byte[][] byteMultiArrayTest(byte[][] v) throws RemoteException {
    TestUtil.logTrace("byteMultiArrayTest");
    JAXRPC_Data.dumpMultiArrayValues(v, "byte");
    return v;
  }

  public short[][] shortMultiArrayTest(short[][] v) throws RemoteException {
    TestUtil.logTrace("shortMultiArrayTest");
    JAXRPC_Data.dumpMultiArrayValues(v, "short");
    return v;
  }

  public int[][] intMultiArrayTest(int[][] v) throws RemoteException {
    TestUtil.logTrace("intMultiArrayTest");
    JAXRPC_Data.dumpMultiArrayValues(v, "int");
    return v;
  }

  public long[][] longMultiArrayTest(long[][] v) throws RemoteException {
    TestUtil.logTrace("longMultiArrayTest");
    JAXRPC_Data.dumpMultiArrayValues(v, "long");
    return v;
  }

  public float[][] floatMultiArrayTest(float[][] v) throws RemoteException {
    TestUtil.logTrace("floatMultiArrayTest");
    JAXRPC_Data.dumpMultiArrayValues(v, "float");
    return v;
  }

  public double[][] doubleMultiArrayTest(double[][] v) throws RemoteException {
    TestUtil.logTrace("doubleMultiArrayTest");
    JAXRPC_Data.dumpMultiArrayValues(v, "double");
    return v;
  }

  // ====================================================================
  // Standard Java Classes (Scalar, Single and Multi Dimensional Arrays)
  // ====================================================================
  public String stringTest(String v) throws RemoteException {
    TestUtil.logTrace("stringTest");
    TestUtil.logMsg("String=" + v);
    return v;
  }

  public String[] stringArrayTest(String[] v) throws RemoteException {
    TestUtil.logTrace("stringArrayTest");
    JAXRPC_Data.dumpArrayValues(v, "String");
    return v;
  }

  public String[][] stringMultiArrayTest(String[][] v) throws RemoteException {
    TestUtil.logTrace("stringMultiArrayTest");
    JAXRPC_Data.dumpMultiArrayValues(v, "String");
    return v;
  }

  public Calendar calendarTest(Calendar v) throws RemoteException {
    TestUtil.logTrace("calendarTest");
    TestUtil.logMsg("Calendar=" + v);
    return v;
  }

  public Calendar[] calendarArrayTest(Calendar[] v) throws RemoteException {
    TestUtil.logTrace("calendarArrayTest");
    JAXRPC_Data.dumpArrayValues(v, "Calendar");
    return v;
  }

  public Calendar[][] calendarMultiArrayTest(Calendar[][] v)
      throws RemoteException {
    TestUtil.logTrace("calendarMultiArrayTest");
    JAXRPC_Data.dumpMultiArrayValues(v, "Calendar");
    return v;
  }

  public BigInteger bigIntegerTest(BigInteger v) throws RemoteException {
    TestUtil.logTrace("bigIntegerTest");
    TestUtil.logMsg("BigInteger=" + v);
    return v;
  }

  public BigInteger[] bigIntegerArrayTest(BigInteger[] v)
      throws RemoteException {
    TestUtil.logTrace("bigIntegerArrayTest");
    JAXRPC_Data.dumpArrayValues(v, "BigInteger");
    return v;
  }

  public BigInteger[][] bigIntegerMultiArrayTest(BigInteger[][] v)
      throws RemoteException {
    TestUtil.logTrace("bigIntegerMultiArrayTest");
    JAXRPC_Data.dumpMultiArrayValues(v, "BigInteger");
    return v;
  }

  public BigDecimal bigDecimalTest(BigDecimal v) throws RemoteException {
    TestUtil.logTrace("bigDecimalTest");
    TestUtil.logMsg("BigDecimal=" + v);
    return v;
  }

  public BigDecimal[] bigDecimalArrayTest(BigDecimal[] v)
      throws RemoteException {
    TestUtil.logTrace("bigDecimalArrayTest");
    JAXRPC_Data.dumpArrayValues(v, "BigDecimal");
    return v;
  }

  public BigDecimal[][] bigDecimalMultiArrayTest(BigDecimal[][] v)
      throws RemoteException {
    TestUtil.logTrace("bigDecimalMultiArrayTest");
    JAXRPC_Data.dumpMultiArrayValues(v, "BigDecimal");
    return v;
  }

  // ====================================================================
  // JavaBeans Class (Scalar, Single and Multi Dimensional Arrays)
  // ====================================================================
  public JavaBean javaBeanTest(JavaBean v) throws RemoteException {
    TestUtil.logTrace("javaBeanTest");
    TestUtil.logMsg("JavaBean=" + v);
    return v;
  }

  public JavaBean[] javaBeanArrayTest(JavaBean[] v) throws RemoteException {
    TestUtil.logTrace("javaBeanArrayTest");
    return v;
  }

  public JavaBean[][] javaBeanMultiArrayTest(JavaBean[][] v)
      throws RemoteException {
    TestUtil.logTrace("javaBeanMultiArrayTest");
    return v;
  }

  // ====================================================================
  // Value Type Class (Scalar, Single and Multi Dimensional Arrays)
  // ====================================================================
  public ValueType valueTypeTest(ValueType v) throws RemoteException {
    TestUtil.logTrace("valueTypeTest");
    TestUtil.logMsg("ValueType=" + v);
    return v;
  }

  public ValueType[] valueTypeArrayTest(ValueType[] v) throws RemoteException {
    TestUtil.logTrace("valueTypeArrayTest");
    return v;
  }

  public ValueType[][] valueTypeMultiArrayTest(ValueType[][] v)
      throws RemoteException {
    TestUtil.logTrace("valueTypeMultiArrayTest");
    return v;
  }

  // ====================================================================
  // Service Specific Exception
  // ====================================================================
  public void myServiceExceptionTest()
      throws RemoteException, MyServiceException {
    TestUtil.logTrace("myServiceExceptionTest");
    throw new MyServiceException("My ServiceException");
  }

  // ====================================================================
  // The void type
  // ====================================================================
  public void voidTest() throws RemoteException {
    TestUtil.logTrace("voidTest");
  }
}
