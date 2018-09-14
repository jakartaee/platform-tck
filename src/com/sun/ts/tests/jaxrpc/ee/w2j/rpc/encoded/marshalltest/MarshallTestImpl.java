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

package com.sun.ts.tests.jaxrpc.ee.w2j.rpc.encoded.marshalltest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxrpc.common.*;

import java.rmi.RemoteException;
import java.rmi.ServerException;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.namespace.QName;
import javax.xml.rpc.holders.*;

// Service Implementation Class - as outlined in JAX-RPC Specification

public class MarshallTestImpl implements MarshallTest {

  public String echoString(String v) throws RemoteException {
    return v;
  }

  public BigInteger echoInteger(BigInteger v) throws RemoteException {
    return v;
  }

  public int echoInt(int v) throws RemoteException {
    return v;
  }

  public long echoLong(long v) throws RemoteException {
    return v;
  }

  public short echoShort(short v) throws RemoteException {
    return v;
  }

  public BigDecimal echoDecimal(BigDecimal v) throws RemoteException {
    return v;
  }

  public float echoFloat(float v) throws RemoteException {
    return v;
  }

  public double echoDouble(double v) throws RemoteException {
    return v;
  }

  public boolean echoBoolean(boolean v) throws RemoteException {
    return v;
  }

  public byte echoByte(byte v) throws RemoteException {
    return v;
  }

  public QName echoQName(QName v) throws RemoteException {
    return v;
  }

  public Calendar echoDateTime(Calendar v) throws RemoteException {
    return v;
  }

  public String echoSoapString(String v) throws RemoteException {
    return v;
  }

  public Boolean echoSoapBoolean(Boolean v) throws RemoteException {
    return v;
  }

  public Float echoSoapFloat(Float v) throws RemoteException {
    return v;
  }

  public Double echoSoapDouble(Double v) throws RemoteException {
    return v;
  }

  public BigDecimal echoSoapDecimal(BigDecimal v) throws RemoteException {
    return v;
  }

  public Integer echoSoapInt(Integer v) throws RemoteException {
    return v;
  }

  public Short echoSoapShort(Short v) throws RemoteException {
    return v;
  }

  public Byte echoSoapByte(Byte v) throws RemoteException {
    return v;
  }

  public byte[] echoBase64Binary(byte[] v) throws RemoteException {
    return v;
  }

  public byte[] echoHexBinary(byte[] v) throws RemoteException {
    return v;
  }

  public byte[] echoSoapBase64(byte[] v) throws RemoteException {
    return v;
  }

  //
  // wsdl restricted methods
  //
  public String[] echoStringArray(String[] v) throws RemoteException {
    return v;
  }

  public BigInteger[] echoIntegerArray(BigInteger[] v) throws RemoteException {
    return v;
  }

  public int[] echoIntArray(int[] v) throws RemoteException {
    return v;
  }

  public long[] echoLongArray(long[] v) throws RemoteException {
    return v;
  }

  public short[] echoShortArray(short[] v) throws RemoteException {
    return v;
  }

  public BigDecimal[] echoDecimalArray(BigDecimal[] v) throws RemoteException {
    return v;
  }

  public float[] echoFloatArray(float[] v) throws RemoteException {
    return v;
  }

  public double[] echoDoubleArray(double[] v) throws RemoteException {
    return v;
  }

  public boolean[] echoBooleanArray(boolean[] v) throws RemoteException {
    return v;
  }

  public byte[] echoByteArray(byte[] v) throws RemoteException {
    return v;
  }

  public QName[] echoQNameArray(QName[] v) throws RemoteException {
    return v;
  }

  public Calendar[] echoDateTimeArray(Calendar[] v) throws RemoteException {
    return v;
  }

  public String[] echoSoapStringArray(String[] v) throws RemoteException {
    return v;
  }

  public Boolean[] echoSoapBooleanArray(Boolean[] v) throws RemoteException {
    return v;
  }

  public Float[] echoSoapFloatArray(Float[] v) throws RemoteException {
    return v;
  }

  public Double[] echoSoapDoubleArray(Double[] v) throws RemoteException {
    return v;
  }

  public BigDecimal[] echoSoapDecimalArray(BigDecimal[] v)
      throws RemoteException {
    return v;
  }

  public Integer[] echoSoapIntArray(Integer[] v) throws RemoteException {
    return v;
  }

  public Short[] echoSoapShortArray(Short[] v) throws RemoteException {
    return v;
  }

  public Byte[] echoSoapByteArray(Byte[] v) throws RemoteException {
    return v;
  }

  public byte[][] echoBase64BinaryArray(byte[][] v) throws RemoteException {
    return v;
  }

  public byte[][] echoHexBinaryArray(byte[][] v) throws RemoteException {
    return v;
  }

  public byte[][] echoSoapBase64Array(byte[][] v) throws RemoteException {
    return v;
  }
  //
  // soap restricted methods
  //

  public String[] echoString1Array(String[] v) throws RemoteException {
    return v;
  }

  public BigInteger[] echoInteger1Array(BigInteger[] v) throws RemoteException {
    return v;
  }

  public int[] echoInt1Array(int[] v) throws RemoteException {
    return v;
  }

  public long[] echoLong1Array(long[] v) throws RemoteException {
    return v;
  }

  public short[] echoShort1Array(short[] v) throws RemoteException {
    return v;
  }

  public BigDecimal[] echoDecimal1Array(BigDecimal[] v) throws RemoteException {
    return v;
  }

  public float[] echoFloat1Array(float[] v) throws RemoteException {
    return v;
  }

  public double[] echoDouble1Array(double[] v) throws RemoteException {
    return v;
  }

  public boolean[] echoBoolean1Array(boolean[] v) throws RemoteException {
    return v;
  }

  public byte[] echoByte1Array(byte[] v) throws RemoteException {
    return v;
  }

  public QName[] echoQName1Array(QName[] v) throws RemoteException {
    return v;
  }

  public Calendar[] echoDateTime1Array(Calendar[] v) throws RemoteException {
    return v;
  }

  public String[] echoSoapString1Array(String[] v) throws RemoteException {
    return v;
  }

  public Boolean[] echoSoapBoolean1Array(Boolean[] v) throws RemoteException {
    return v;
  }

  public Float[] echoSoapFloat1Array(Float[] v) throws RemoteException {
    return v;
  }

  public Double[] echoSoapDouble1Array(Double[] v) throws RemoteException {
    return v;
  }

  public BigDecimal[] echoSoapDecimal1Array(BigDecimal[] v)
      throws RemoteException {
    return v;
  }

  public Integer[] echoSoapInt1Array(Integer[] v) throws RemoteException {
    return v;
  }

  public Short[] echoSoapShort1Array(Short[] v) throws RemoteException {
    return v;
  }

  public Byte[] echoSoapByte1Array(Byte[] v) throws RemoteException {
    return v;
  }

  public byte[][] echoBase64Binary1Array(byte[][] v) throws RemoteException {
    return v;
  }

  public byte[][] echoHexBinary1Array(byte[][] v) throws RemoteException {
    return v;
  }

  public byte[][] echoSoapBase641Array(byte[][] v) throws RemoteException {
    return v;
  }

  public AllStruct echoAllStruct(AllStruct v) throws RemoteException {
    return v;
  }

  public AllStruct[] echoAllStructArray(AllStruct[] v) throws RemoteException {
    return v;
  }

  public AllStruct2 echoAllStruct2(AllStruct2 v) throws RemoteException {
    return v;
  }

  public AllStruct2[] echoAllStruct2Array(AllStruct2[] v)
      throws RemoteException {
    return v;
  }

  public SequenceStruct echoSequenceStruct(SequenceStruct v)
      throws RemoteException {
    return v;
  }

  public SequenceStruct[] echoSequenceStructArray(SequenceStruct[] v)
      throws RemoteException {
    return v;
  }

  public SequenceStruct2 echoSequenceStruct2(SequenceStruct2 v)
      throws RemoteException {
    return v;
  }

  public SequenceStruct2[] echoSequenceStruct2Array(SequenceStruct2[] v)
      throws RemoteException {
    return v;
  }

  public void echoVoid() throws RemoteException {
  }

  /*
   * public void echoServiceException(String v) throws RemoteException,
   * ServiceExceptionFaultMessage { throw new ServiceExceptionFaultMessage(v); }
   */
  public void echoRemoteException(String v) throws RemoteException {
    throw new RemoteException(v);
  }

  public void echoSubClassRemoteException(String v) throws RemoteException {
    throw new ServerException(v);
  }

  public EnumString echoEnumString(EnumString v) throws RemoteException {
    return v;
  }

  public EnumInteger echoEnumInteger(EnumInteger v) throws RemoteException {
    return v;
  }

  public EnumInt echoEnumInt(EnumInt v) throws RemoteException {
    return v;
  }

  public EnumLong echoEnumLong(EnumLong v) throws RemoteException {
    return v;
  }

  public EnumShort echoEnumShort(EnumShort v) throws RemoteException {
    return v;
  }

  public EnumDecimal echoEnumDecimal(EnumDecimal v) throws RemoteException {
    return v;
  }

  public EnumFloat echoEnumFloat(EnumFloat v) throws RemoteException {
    return v;
  }

  public EnumDouble echoEnumDouble(EnumDouble v) throws RemoteException {
    return v;
  }

  public EnumByte echoEnumByte(EnumByte v) throws RemoteException {
    return v;
  }

  /*
   * public void echoIn(StringHolder varString, BigIntegerHolder varInteger,
   * IntHolder varInt, LongHolder varLong, ShortHolder varShort,
   * BigDecimalHolder varDecimal, FloatHolder varFloat, DoubleHolder varDouble,
   * BooleanHolder varBoolean, ByteHolder varByte, QNameHolder varQName,
   * CalendarHolder varDateTime, StringHolder varSoapString, BooleanHolder
   * varSoapBoolean, FloatHolder varSoapFloat, DoubleHolder varSoapDouble,
   * BigDecimalHolder varSoapDecimal, IntHolder varSoapInt, ShortHolder
   * varSoapShort, ByteArrayHolder varBase64Binary, ByteArrayHolder
   * varHexBinary, ByteArrayHolder varSoapBase64) throws RemoteException { byte
   * byte_data[] = { 1, 2, 3 }; varString="String3"; varInteger=new
   * BigInteger("1111111"); varInt=1111; varLong=(long)1111;
   * varShort=(short)1111; varDecimal=new BigDecimal("1111.11");
   * varFloat=(float)1111; varDouble=(double)1111; varBoolean=false;
   * varByte=(byte)1; varQName=new QName("1111"); varDateTime=new
   * GregorianCalendar(99,11,11) varSoapString="String4"; varSoapBoolean=true;
   * varSoapFloat=(float)1111; varSoapDouble=(double)1111; varSoapDecimal=new
   * BigDecimal("1111.11"); varSoapInt=1111; varSoapShort=(short)1111;
   * varBase64Binary=byte_data; varHexBinary=byte_data; varSoapBase64=byte_data;
   * 
   * } public void echoIn2(varAllStruct2 varAllStruct2Array) throws
   * RemoteException { varAllStruct2 tempData[]={ new AllStruct2("", new
   * BigInteger("0"), (int)0, (long)0, (short)0, new BigDecimal("0.0") ,
   * (float)0, (double)0, false, (byte)0, new AllStruct("", new BigInteger("0"),
   * (int)0, (long)0, (short)0, new BigDecimal("0.0") , (float)0, (double)0,
   * false, (byte)0) ), new AllStruct2("String2", new BigInteger("3512360"),
   * (int)Integer.MAX_VALUE, (long)Long.MAX_VALUE, (short)Short.MAX_VALUE, new
   * BigDecimal("3512360.1456") , (float)Float.MAX_VALUE,
   * (double)Double.MAX_VALUE, true, (byte)Byte.MAX_VALUE, new
   * AllStruct("String2", new BigInteger("3512360"), (int)Integer.MAX_VALUE,
   * (long)Long.MAX_VALUE, (short)Short.MAX_VALUE, new
   * BigDecimal("3512360.1456") , (float)Float.MAX_VALUE,
   * (double)Double.MAX_VALUE, true, (byte)Byte.MAX_VALUE) ), new
   * AllStruct2("String1", new BigInteger("3512359"), (int)Integer.MIN_VALUE,
   * (long)Long.MIN_VALUE, (short)Short.MIN_VALUE, new
   * BigDecimal("3512359.1456") , (float)Float.MIN_VALUE,
   * (double)Double.MIN_VALUE, false, (byte)Byte.MIN_VALUE, new
   * AllStruct("String1", new BigInteger("3512359"), (int)Integer.MIN_VALUE,
   * (long)Long.MIN_VALUE, (short)Short.MIN_VALUE, new
   * BigDecimal("3512359.1456") , (float)Float.MIN_VALUE,
   * (double)Double.MIN_VALUE, false, (byte)Byte.MIN_VALUE) ) };
   * varAllStruct2Array=tempData; } public void echoOut(StringHolder varString,
   * BigIntegerHolder varInteger, IntHolder varInt, LongHolder varLong,
   * ShortHolder varShort, BigDecimalHolder varDecimal, FloatHolder varFloat,
   * DoubleHolder varDouble, BooleanHolder varBoolean, ByteHolder varByte,
   * QNameHolder varQName, CalendarHolder varDateTime, StringHolder
   * varSoapString, BooleanHolder varSoapBoolean, FloatHolder varSoapFloat,
   * DoubleHolder varSoapDouble, BigDecimalHolder varSoapDecimal, IntHolder
   * varSoapInt, ShortHolder varSoapShort, ByteArrayHolder varBase64Binary,
   * ByteArrayHolder varHexBinary, ByteArrayHolder varSoapBase64) throws
   * RemoteException {
   * 
   * byte byte_data[] = { 1, 2, 3 }; varString="String3"; varInteger=new
   * BigInteger("1111111"); varInt=1111; varLong=(long)1111;
   * varShort=(short)1111; varDecimal=new BigDecimal("1111.11");
   * varFloat=(float)1111; varDouble=(double)1111; varBoolean=false;
   * varByte=(byte)1; varQName=new QName("1111"); varDateTime=new
   * GregorianCalendar(99,11,11) varSoapString="String4"; varSoapBoolean=true;
   * varSoapFloat=(float)1111; varSoapDouble=(double)1111; varSoapDecimal=new
   * BigDecimal("1111.11"); varSoapInt=1111; varSoapShort=(short)1111;
   * varBase64Binary=byte_data; varHexBinary=byte_data; varSoapBase64=byte_data;
   * } public void echoOut2(varAllStruct2 varAllStruct2Array) throws
   * RemoteException {
   * 
   * varAllStruct2 tempData[]={ new AllStruct2("", new BigInteger("0"), (int)0,
   * (long)0, (short)0, new BigDecimal("0.0") , (float)0, (double)0, false,
   * (byte)0, new AllStruct("", new BigInteger("0"), (int)0, (long)0, (short)0,
   * new BigDecimal("0.0") , (float)0, (double)0, false, (byte)0) ), new
   * AllStruct2("String2", new BigInteger("3512360"), (int)Integer.MAX_VALUE,
   * (long)Long.MAX_VALUE, (short)Short.MAX_VALUE, new
   * BigDecimal("3512360.1456") , (float)Float.MAX_VALUE,
   * (double)Double.MAX_VALUE, true, (byte)Byte.MAX_VALUE, new
   * AllStruct("String2", new BigInteger("3512360"), (int)Integer.MAX_VALUE,
   * (long)Long.MAX_VALUE, (short)Short.MAX_VALUE, new
   * BigDecimal("3512360.1456") , (float)Float.MAX_VALUE,
   * (double)Double.MAX_VALUE, true, (byte)Byte.MAX_VALUE) ), new
   * AllStruct2("String1", new BigInteger("3512359"), (int)Integer.MIN_VALUE,
   * (long)Long.MIN_VALUE, (short)Short.MIN_VALUE, new
   * BigDecimal("3512359.1456") , (float)Float.MIN_VALUE,
   * (double)Double.MIN_VALUE, false, (byte)Byte.MIN_VALUE, new
   * AllStruct("String1", new BigInteger("3512359"), (int)Integer.MIN_VALUE,
   * (long)Long.MIN_VALUE, (short)Short.MIN_VALUE, new
   * BigDecimal("3512359.1456") , (float)Float.MIN_VALUE,
   * (double)Double.MIN_VALUE, false, (byte)Byte.MIN_VALUE) ) };
   * varAllStruct2Array=tempData; } public void echoInOut(StringHolder
   * varString, BigIntegerHolder varInteger, IntHolder varInt, LongHolder
   * varLong, ShortHolder varShort, BigDecimalHolder varDecimal, FloatHolder
   * varFloat, DoubleHolder varDouble, BooleanHolder varBoolean, ByteHolder
   * varByte, QNameHolder varQName, CalendarHolder varDateTime, StringHolder
   * varSoapString, BooleanHolder varSoapBoolean, FloatHolder varSoapFloat,
   * DoubleHolder varSoapDouble, BigDecimalHolder varSoapDecimal, IntHolder
   * varSoapInt, ShortHolder varSoapShort, ByteArrayHolder varBase64Binary,
   * ByteArrayHolder varHexBinary, ByteArrayHolder varSoapBase64) throws
   * RemoteException {
   * 
   * byte byte_data[] = { 1, 2, 3 }; varString="String3"; varInteger=new
   * BigInteger("1111111"); varInt=1111; varLong=(long)1111;
   * varShort=(short)1111; varDecimal=new BigDecimal("1111.11");
   * varFloat=(float)1111; varDouble=(double)1111; varBoolean=false;
   * varByte=(byte)1; varQName=new QName("1111"); varDateTime=new
   * GregorianCalendar(99,11,11) varSoapString="String4"; varSoapBoolean=true;
   * varSoapFloat=(float)1111; varSoapDouble=(double)1111; varSoapDecimal=new
   * BigDecimal("1111.11"); varSoapInt=1111; varSoapShort=(short)1111;
   * varBase64Binary=byte_data; varHexBinary=byte_data; varSoapBase64=byte_data;
   * } public void echoInOut2(varAllStruct2 varAllStruct2Array) throws
   * RemoteException {
   * 
   * varAllStruct2 tempData[]={ new AllStruct2("", new BigInteger("0"), (int)0,
   * (long)0, (short)0, new BigDecimal("0.0") , (float)0, (double)0, false,
   * (byte)0, new AllStruct("", new BigInteger("0"), (int)0, (long)0, (short)0,
   * new BigDecimal("0.0") , (float)0, (double)0, false, (byte)0) ), new
   * AllStruct2("String2", new BigInteger("3512360"), (int)Integer.MAX_VALUE,
   * (long)Long.MAX_VALUE, (short)Short.MAX_VALUE, new
   * BigDecimal("3512360.1456") , (float)Float.MAX_VALUE,
   * (double)Double.MAX_VALUE, true, (byte)Byte.MAX_VALUE, new
   * AllStruct("String2", new BigInteger("3512360"), (int)Integer.MAX_VALUE,
   * (long)Long.MAX_VALUE, (short)Short.MAX_VALUE, new
   * BigDecimal("3512360.1456") , (float)Float.MAX_VALUE,
   * (double)Double.MAX_VALUE, true, (byte)Byte.MAX_VALUE) ), new
   * AllStruct2("String1", new BigInteger("3512359"), (int)Integer.MIN_VALUE,
   * (long)Long.MIN_VALUE, (short)Short.MIN_VALUE, new
   * BigDecimal("3512359.1456") , (float)Float.MIN_VALUE,
   * (double)Double.MIN_VALUE, false, (byte)Byte.MIN_VALUE, new
   * AllStruct("String1", new BigInteger("3512359"), (int)Integer.MIN_VALUE,
   * (long)Long.MIN_VALUE, (short)Short.MIN_VALUE, new
   * BigDecimal("3512359.1456") , (float)Float.MIN_VALUE,
   * (double)Double.MIN_VALUE, false, (byte)Byte.MIN_VALUE) ) };
   * varAllStruct2Array=tempData; }
   */
  public NilTrueStruct echonilTrue(NilTrueStruct v) throws RemoteException {
    return v;
  }

  public NilFalseStruct echonilFalse(NilFalseStruct v) throws RemoteException {
    return v;
  }
}
