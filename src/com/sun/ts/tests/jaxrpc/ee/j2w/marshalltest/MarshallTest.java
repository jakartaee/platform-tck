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

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.*;

import java.math.BigInteger;
import java.math.BigDecimal;

// Service Defintion Interface - as outlined in JAX-RPC Specification

public interface MarshallTest extends Remote {

  // ====================================================================
  // Java Primitive Types
  // ====================================================================
  public boolean booleanTest(boolean v) throws RemoteException;

  public Boolean wrapperBooleanTest(Boolean v) throws RemoteException;

  public byte byteTest(byte v) throws RemoteException;

  public Byte wrapperByteTest(Byte v) throws RemoteException;

  public short shortTest(short v) throws RemoteException;

  public Short wrapperShortTest(Short v) throws RemoteException;

  public int intTest(int v) throws RemoteException;

  public Integer wrapperIntegerTest(Integer v) throws RemoteException;

  public long longTest(long v) throws RemoteException;

  public Long wrapperLongTest(Long v) throws RemoteException;

  public float floatTest(float v) throws RemoteException;

  public Float wrapperFloatTest(Float v) throws RemoteException;

  public double doubleTest(double v) throws RemoteException;

  public Double wrapperDoubleTest(Double v) throws RemoteException;

  // ====================================================================
  // Java Primitive Type Arrays (Single and Multi Dimensional)
  // ====================================================================
  public boolean[] booleanArrayTest(boolean[] v) throws RemoteException;

  public byte[] byteArrayTest(byte[] v) throws RemoteException;

  public short[] shortArrayTest(short[] v) throws RemoteException;

  public int[] intArrayTest(int[] v) throws RemoteException;

  public long[] longArrayTest(long[] v) throws RemoteException;

  public float[] floatArrayTest(float[] v) throws RemoteException;

  public double[] doubleArrayTest(double[] v) throws RemoteException;

  public boolean[][] booleanMultiArrayTest(boolean[][] v)
      throws RemoteException;

  public byte[][] byteMultiArrayTest(byte[][] v) throws RemoteException;

  public short[][] shortMultiArrayTest(short[][] v) throws RemoteException;

  public int[][] intMultiArrayTest(int[][] v) throws RemoteException;

  public long[][] longMultiArrayTest(long[][] v) throws RemoteException;

  public float[][] floatMultiArrayTest(float[][] v) throws RemoteException;

  public double[][] doubleMultiArrayTest(double[][] v) throws RemoteException;

  // ====================================================================
  // Standard Java Classes (Scalar, Single and Multi Dimensional Arrays)
  // ====================================================================
  public String stringTest(String v) throws RemoteException;

  public String[] stringArrayTest(String[] v) throws RemoteException;

  public String[][] stringMultiArrayTest(String[][] v) throws RemoteException;

  public Calendar calendarTest(Calendar v) throws RemoteException;

  public Calendar[] calendarArrayTest(Calendar[] v) throws RemoteException;

  public Calendar[][] calendarMultiArrayTest(Calendar[][] v)
      throws RemoteException;

  public BigInteger bigIntegerTest(BigInteger v) throws RemoteException;

  public BigInteger[] bigIntegerArrayTest(BigInteger[] v)
      throws RemoteException;

  public BigInteger[][] bigIntegerMultiArrayTest(BigInteger[][] v)
      throws RemoteException;

  public BigDecimal bigDecimalTest(BigDecimal v) throws RemoteException;

  public BigDecimal[] bigDecimalArrayTest(BigDecimal[] v)
      throws RemoteException;

  public BigDecimal[][] bigDecimalMultiArrayTest(BigDecimal[][] v)
      throws RemoteException;

  // ====================================================================
  // Value Type Class (Scalar, Single and Multi Dimensional Arrays)
  // ====================================================================
  public ValueType valueTypeTest(ValueType v) throws RemoteException;

  public ValueType[] valueTypeArrayTest(ValueType[] v) throws RemoteException;

  public ValueType[][] valueTypeMultiArrayTest(ValueType[][] v)
      throws RemoteException;

  // ====================================================================
  // Service Specific Exception
  // ====================================================================
  public void myServiceExceptionTest()
      throws RemoteException, MyServiceException;

  // ====================================================================
  // JavaBeans Class (Scalar, Single and Multi Dimensional Arrays)
  // ====================================================================
  public JavaBean javaBeanTest(JavaBean v) throws RemoteException;

  public JavaBean[] javaBeanArrayTest(JavaBean[] v) throws RemoteException;

  public JavaBean[][] javaBeanMultiArrayTest(JavaBean[][] v)
      throws RemoteException;

  // ====================================================================
  // The void type
  // ====================================================================
  public void voidTest() throws RemoteException;
}
