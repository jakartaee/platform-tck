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

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.wrapperstyle.marshalltest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;

import com.sun.ts.tests.jaxws.ee.j2w.document.literal.marshalltest.server.MarshallTest;

import jakarta.jws.WebService;

@WebService(portName = "MarshallTestPort", serviceName = "MarshallTestService", targetNamespace = "http://MarshallTest.org/", wsdlLocation = "WEB-INF/wsdl/WSW2JDLWMarshallTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.wrapperstyle.marshalltest.MarshallTest")

public class MarshallTestImpl implements MarshallTest {

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

  public XMLGregorianCalendar echoDateTime(XMLGregorianCalendar v) {
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
  public List<String> echoStringArray(List<String> v) {
    return v;
  }

  public List<BigInteger> echoIntegerArray(List<BigInteger> v) {
    return v;
  }

  public List<Integer> echoIntArray(List<Integer> v) {
    return v;
  }

  public List<Long> echoLongArray(List<Long> v) {
    return v;
  }

  public List<Short> echoShortArray(List<Short> v) {
    return v;
  }

  public List<BigDecimal> echoDecimalArray(List<BigDecimal> v) {
    return v;
  }

  public List<Float> echoFloatArray(List<Float> v) {
    return v;
  }

  public List<Double> echoDoubleArray(List<Double> v) {
    return v;
  }

  public List<Boolean> echoBooleanArray(List<Boolean> v) {
    return v;
  }

  public List<Byte> echoByteArray(List<Byte> v) {
    return v;
  }

  public List<QName> echoQNameArray(List<QName> v) {
    return v;
  }

  public List<XMLGregorianCalendar> echoDateTimeArray(
      List<XMLGregorianCalendar> v) {
    return v;
  }
}
