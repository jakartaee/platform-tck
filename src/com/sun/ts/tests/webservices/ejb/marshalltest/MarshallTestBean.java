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

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import java.rmi.RemoteException;
import java.rmi.ServerException;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxrpc.common.*;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.namespace.QName;
import javax.xml.rpc.holders.*;

public class MarshallTestBean implements SessionBean {

  // public MarshallTestBean() {}
  public void ejbCreate() {
  }

  public void ejbActivate() {
  }

  public void ejbRemove() {
  }

  public void ejbPassivate() {
  }

  public void setSessionContext(SessionContext sc) {
  }

  public String echoString(String v) {
    return v;
  }

  public BigInteger echoInteger(BigInteger v) {
    return v;
  }

  public int echoInt(int v) {
    return v;
  }

  public long echoLong(long v) {
    return v;
  }

  public short echoShort(short v) {
    return v;
  }

  public BigDecimal echoDecimal(BigDecimal v) {
    return v;
  }

  public float echoFloat(float v) {
    return v;
  }

  public double echoDouble(double v) {
    return v;
  }

  public boolean echoBoolean(boolean v) {
    return v;
  }

  public byte echoByte(byte v) {
    return v;
  }

  public QName echoQName(QName v) {
    return v;
  }

  public Calendar echoDateTime(Calendar v) {
    return v;
  }

  public byte[] echoBase64Binary(byte[] v) {
    return v;
  }

  public byte[] echoHexBinary(byte[] v) {
    return v;
  }

  //
  // array methods
  //
  public String[] echoStringArray(String[] v) {
    return v;
  }

  public BigInteger[] echoIntegerArray(BigInteger[] v) {
    return v;
  }

  public int[] echoIntArray(int[] v) {
    return v;
  }

  public long[] echoLongArray(long[] v) {
    return v;
  }

  public short[] echoShortArray(short[] v) {
    return v;
  }

  public BigDecimal[] echoDecimalArray(BigDecimal[] v) {
    return v;
  }

  public float[] echoFloatArray(float[] v) {
    return v;
  }

  public double[] echoDoubleArray(double[] v) {
    return v;
  }

  public boolean[] echoBooleanArray(boolean[] v) {
    return v;
  }

  public byte[] echoByteArray(byte[] v) {
    return v;
  }

  public QName[] echoQNameArray(QName[] v) {
    return v;
  }

  public Calendar[] echoDateTimeArray(Calendar[] v) {
    return v;
  }
  /***
   * public byte[][] echoBase64BinaryArray(byte[][] v) { return v; } public
   * byte[][] echoHexBinaryArray(byte[][] v) { return v; }
   * 
   * public void echoVoid() { }
   ***/
}
