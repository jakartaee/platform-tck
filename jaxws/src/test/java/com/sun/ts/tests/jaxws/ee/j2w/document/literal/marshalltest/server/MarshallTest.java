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

import java.util.*;

import java.math.BigInteger;
import java.math.BigDecimal;

// Service Defintion Interface - as outlined in JAX-WS Specification

import jakarta.jws.WebService;

@WebService(targetNamespace = "http://marshalltestservice.org/wsdl")
public interface MarshallTest {

  // ====================================================================
  // Java Primitive Types
  // ====================================================================
  public boolean booleanTest(boolean v);

  public Boolean wrapperBooleanTest(Boolean v);

  public byte byteTest(byte v);

  public Byte wrapperByteTest(Byte v);

  public short shortTest(short v);

  public Short wrapperShortTest(Short v);

  public int intTest(int v);

  public Integer wrapperIntegerTest(Integer v);

  public long longTest(long v);

  public Long wrapperLongTest(Long v);

  public float floatTest(float v);

  public Float wrapperFloatTest(Float v);

  public double doubleTest(double v);

  public Double wrapperDoubleTest(Double v);

  // ====================================================================
  // Java Primitive Type Arrays (Single and Multi Dimensional)
  // ====================================================================
  public boolean[] booleanArrayTest(boolean[] v);

  public byte[] byteArrayTest(byte[] v);

  public short[] shortArrayTest(short[] v);

  public int[] intArrayTest(int[] v);

  public long[] longArrayTest(long[] v);

  public float[] floatArrayTest(float[] v);

  public double[] doubleArrayTest(double[] v);

  public Boolean[] wrapperBooleanArrayTest(Boolean[] v);

  public Byte[] wrapperByteArrayTest(Byte[] v);

  public Short[] wrapperShortArrayTest(Short[] v);

  public Integer[] wrapperIntArrayTest(Integer[] v);

  public Long[] wrapperLongArrayTest(Long[] v);

  public Float[] wrapperFloatArrayTest(Float[] v);

  public Double[] wrapperDoubleArrayTest(Double[] v);

  // ====================================================================
  // Standard Java Classes (Scalar, Single and Multi Dimensional Arrays)
  // ====================================================================
  public String stringTest(String v);

  public String[] stringArrayTest(String[] v);

  public Calendar calendarTest(Calendar v);

  public Calendar[] calendarArrayTest(Calendar[] v);

  public BigInteger bigIntegerTest(BigInteger v);

  public BigInteger[] bigIntegerArrayTest(BigInteger[] v);

  public BigDecimal bigDecimalTest(BigDecimal v);

  public BigDecimal[] bigDecimalArrayTest(BigDecimal[] v);

  // ====================================================================
  // JavaBeans Class (Scalar, Single and Multi Dimensional Arrays)
  // ====================================================================
  public JavaBean javaBeanTest(JavaBean v);

  public JavaBean[] javaBeanArrayTest(JavaBean[] v);

  // ====================================================================
  // Service Specific Exception
  // ====================================================================
  public void myServiceExceptionTest() throws MyServiceException;

  // ====================================================================
  // The void type
  // ====================================================================
  public void voidTest();
}
