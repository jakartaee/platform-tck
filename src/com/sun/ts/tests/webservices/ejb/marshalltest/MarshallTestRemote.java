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

package com.sun.ts.tests.webservices.ejb.marshalltest;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

public interface MarshallTestRemote extends EJBObject {
  public java.lang.String echoString(java.lang.String inputString)
      throws java.rmi.RemoteException;

  public java.math.BigInteger echoInteger(java.math.BigInteger inputInteger)
      throws java.rmi.RemoteException;

  public int echoInt(int inputInt) throws java.rmi.RemoteException;

  public long echoLong(long inputLong) throws java.rmi.RemoteException;

  public short echoShort(short inputShort) throws java.rmi.RemoteException;

  public java.math.BigDecimal echoDecimal(java.math.BigDecimal inputDecimal)
      throws java.rmi.RemoteException;

  public float echoFloat(float inputFloat) throws java.rmi.RemoteException;

  public double echoDouble(double inputDouble) throws java.rmi.RemoteException;

  public boolean echoBoolean(boolean inputBoolean)
      throws java.rmi.RemoteException;

  public byte echoByte(byte inputByte) throws java.rmi.RemoteException;

  public javax.xml.namespace.QName echoQName(
      javax.xml.namespace.QName inputQName) throws java.rmi.RemoteException;

  public java.util.Calendar echoDateTime(java.util.Calendar inputDateTime)
      throws java.rmi.RemoteException;

  public byte[] echoBase64Binary(byte[] inputBase64Binary)
      throws java.rmi.RemoteException;

  public byte[] echoHexBinary(byte[] inputHexBinary)
      throws java.rmi.RemoteException;

  public java.lang.String[] echoStringArray(java.lang.String[] aVal)
      throws java.rmi.RemoteException;

  public java.math.BigInteger[] echoIntegerArray(java.math.BigInteger[] aVal)
      throws java.rmi.RemoteException;

  public int[] echoIntArray(int[] aVal) throws java.rmi.RemoteException;

  public long[] echoLongArray(long[] aVal) throws java.rmi.RemoteException;

  public short[] echoShortArray(short[] aVal) throws java.rmi.RemoteException;

  public java.math.BigDecimal[] echoDecimalArray(java.math.BigDecimal[] aVal)
      throws java.rmi.RemoteException;

  public float[] echoFloatArray(float[] aVal) throws java.rmi.RemoteException;

  public double[] echoDoubleArray(double[] aVal)
      throws java.rmi.RemoteException;

  public boolean[] echoBooleanArray(boolean[] aVal)
      throws java.rmi.RemoteException;

  public byte[] echoByteArray(byte[] aVal) throws java.rmi.RemoteException;

  public javax.xml.namespace.QName[] echoQNameArray(
      javax.xml.namespace.QName[] aVal) throws java.rmi.RemoteException;

  public java.util.Calendar[] echoDateTimeArray(java.util.Calendar[] aVal)
      throws java.rmi.RemoteException;
}
