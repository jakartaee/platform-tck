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

package com.sun.ts.tests.jaxws.ee.j2w.document.literal.marshalltest.server;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxws.common.*;

import java.io.ByteArrayInputStream;

import jakarta.xml.ws.WebServiceException;

import java.util.*;

import java.math.BigInteger;
import java.math.BigDecimal;

// Service Implementation Class - as outlined in JAX-WS Specification

import jakarta.jws.WebService;

@WebService(portName = "MarshallTestPort", serviceName = "MarshallTestService", targetNamespace = "http://marshalltestservice.org/wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.j2w.document.literal.marshalltest.server.MarshallTest")
public class MarshallTestImpl implements MarshallTest {

  // ====================================================================
  // Java Primitive Types
  // ====================================================================
  public boolean booleanTest(boolean v) {
    TestUtil.logTrace("booleanTest");
    TestUtil.logMsg("boolean=" + v);
    return v;
  }

  public Boolean wrapperBooleanTest(Boolean v) {
    TestUtil.logTrace("wrapperBooleanTest");
    TestUtil.logMsg("Boolean=" + v);
    return v;
  }

  public byte byteTest(byte v) {
    TestUtil.logTrace("byteTest");
    TestUtil.logMsg("byte=" + v);
    return v;
  }

  public Byte wrapperByteTest(Byte v) {
    TestUtil.logTrace("wrapperByteTest");
    TestUtil.logMsg("Byte=" + v);
    return v;
  }

  public short shortTest(short v) {
    TestUtil.logTrace("shortTest");
    TestUtil.logMsg("short=" + v);
    return v;
  }

  public Short wrapperShortTest(Short v) {
    TestUtil.logTrace("wrapperShortTest");
    TestUtil.logMsg("Short=" + v);
    return v;
  }

  public int intTest(int v) {
    TestUtil.logTrace("intTest");
    TestUtil.logMsg("int=" + v);
    return v;
  }

  public Integer wrapperIntegerTest(Integer v) {
    TestUtil.logTrace("wrapperIntegerTest");
    TestUtil.logMsg("Integer=" + v);
    return v;
  }

  public long longTest(long v) {
    TestUtil.logTrace("longTest");
    TestUtil.logMsg("long=" + v);
    return v;
  }

  public Long wrapperLongTest(Long v) {
    TestUtil.logTrace("wrapperLongTest");
    TestUtil.logMsg("Long=" + v);
    return v;
  }

  public float floatTest(float v) {
    TestUtil.logTrace("floatTest");
    TestUtil.logMsg("float=" + v);
    return v;
  }

  public Float wrapperFloatTest(Float v) {
    TestUtil.logTrace("wrapperFloatTest");
    TestUtil.logMsg("Float=" + v);
    return v;
  }

  public double doubleTest(double v) {
    TestUtil.logTrace("doubleTest");
    TestUtil.logMsg("double=" + v);
    return v;
  }

  public Double wrapperDoubleTest(Double v) {
    TestUtil.logTrace("wrapperDoubleTest");
    TestUtil.logMsg("Double=" + v);
    return v;
  }

  // ====================================================================
  // Java Primitive Type Arrays (Single and Multi Dimensional)
  // ====================================================================
  public boolean[] booleanArrayTest(boolean[] v) {
    TestUtil.logTrace("booleanArrayTest");
    JAXWS_Data.dumpArrayValues(v, "boolean");
    return v;
  }

  public byte[] byteArrayTest(byte[] v) {
    TestUtil.logTrace("byteArrayTest");
    JAXWS_Data.dumpArrayValues(v, "byte");
    return v;
  }

  public short[] shortArrayTest(short[] v) {
    TestUtil.logTrace("shortArrayTest");
    JAXWS_Data.dumpArrayValues(v, "short");
    return v;
  }

  public int[] intArrayTest(int[] v) {
    TestUtil.logTrace("intArrayTest");
    JAXWS_Data.dumpArrayValues(v, "int");
    return v;
  }

  public long[] longArrayTest(long[] v) {
    TestUtil.logTrace("longArrayTest");
    JAXWS_Data.dumpArrayValues(v, "long");
    return v;
  }

  public float[] floatArrayTest(float[] v) {
    TestUtil.logTrace("floatArrayTest");
    JAXWS_Data.dumpArrayValues(v, "float");
    return v;
  }

  public double[] doubleArrayTest(double[] v) {
    TestUtil.logTrace("doubleArrayTest");
    JAXWS_Data.dumpArrayValues(v, "double");
    return v;
  }

  public Boolean[] wrapperBooleanArrayTest(Boolean[] v) {
    TestUtil.logTrace("wrapperBooleanArrayTest");
    JAXWS_Data.dumpArrayValues(v, "Boolean");
    return v;
  }

  public Byte[] wrapperByteArrayTest(Byte[] v) {
    TestUtil.logTrace("wrapperByteArrayTest");
    JAXWS_Data.dumpArrayValues(v, "Byte");
    return v;
  }

  public Short[] wrapperShortArrayTest(Short[] v) {
    TestUtil.logTrace("wrapperShortArrayTest");
    JAXWS_Data.dumpArrayValues(v, "Short");
    return v;
  }

  public Integer[] wrapperIntArrayTest(Integer[] v) {
    TestUtil.logTrace("wrapperIntArrayTest");
    JAXWS_Data.dumpArrayValues(v, "Integer");
    return v;
  }

  public Long[] wrapperLongArrayTest(Long[] v) {
    TestUtil.logTrace("wrapperLongArrayTest");
    JAXWS_Data.dumpArrayValues(v, "Long");
    return v;
  }

  public Float[] wrapperFloatArrayTest(Float[] v) {
    TestUtil.logTrace("wrapperFloatArrayTest");
    JAXWS_Data.dumpArrayValues(v, "Float");
    return v;
  }

  public Double[] wrapperDoubleArrayTest(Double[] v) {
    TestUtil.logTrace("wrapperDoubleArrayTest");
    JAXWS_Data.dumpArrayValues(v, "Double");
    return v;
  }

  // ====================================================================
  // Standard Java Classes (Scalar, Single and Multi Dimensional Arrays)
  // ====================================================================
  public String stringTest(String v) {
    TestUtil.logTrace("stringTest");
    TestUtil.logMsg("String=" + v);
    return v;
  }

  public String[] stringArrayTest(String[] v) {
    TestUtil.logTrace("stringArrayTest");
    JAXWS_Data.dumpArrayValues(v, "String");
    return v;
  }

  public Calendar calendarTest(Calendar v) {
    TestUtil.logTrace("calendarTest");
    TestUtil.logMsg("Calendar=" + v);
    return v;
  }

  public Calendar[] calendarArrayTest(Calendar[] v) {
    TestUtil.logTrace("calendarArrayTest");
    JAXWS_Data.dumpArrayValues(v, "Calendar");
    return v;
  }

  public BigInteger bigIntegerTest(BigInteger v) {
    TestUtil.logTrace("bigIntegerTest");
    TestUtil.logMsg("BigInteger=" + v);
    return v;
  }

  public BigInteger[] bigIntegerArrayTest(BigInteger[] v) {
    TestUtil.logTrace("bigIntegerArrayTest");
    JAXWS_Data.dumpArrayValues(v, "BigInteger");
    return v;
  }

  public BigDecimal bigDecimalTest(BigDecimal v) {
    TestUtil.logTrace("bigDecimalTest");
    TestUtil.logMsg("BigDecimal=" + v);
    return v;
  }

  public BigDecimal[] bigDecimalArrayTest(BigDecimal[] v) {
    TestUtil.logTrace("bigDecimalArrayTest");
    JAXWS_Data.dumpArrayValues(v, "BigDecimal");
    return v;
  }

  // ====================================================================
  // JavaBeans Class (Scalar, Single and Multi Dimensional Arrays)
  // ====================================================================
  public JavaBean javaBeanTest(JavaBean v) {
    TestUtil.logTrace("javaBeanTest");
    TestUtil.logMsg("JavaBean=" + v);
    return v;
  }

  public JavaBean[] javaBeanArrayTest(JavaBean[] v) {
    TestUtil.logTrace("javaBeanArrayTest");
    return v;
  }

  // ====================================================================
  // Service Specific Exception
  // ====================================================================
  public void myServiceExceptionTest() throws MyServiceException {
    TestUtil.logTrace("myServiceExceptionTest");
    throw new MyServiceException("My ServiceException");
  }

  // ====================================================================
  // The void type
  // ====================================================================
  public void voidTest() {
    TestUtil.logTrace("voidTest");
  }
}
