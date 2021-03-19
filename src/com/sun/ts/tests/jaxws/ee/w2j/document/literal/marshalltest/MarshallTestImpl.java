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

package com.sun.ts.tests.jaxws.ee.w2j.document.literal.marshalltest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.jaxws.common.*;

import java.io.ByteArrayInputStream;

import jakarta.xml.ws.WebServiceException;

import java.util.*;

import java.math.BigInteger;
import java.math.BigDecimal;
import javax.xml.namespace.QName;

// Service Implementation Class - as outlined in JAX-WS Specification

import jakarta.jws.WebService;

@WebService(targetNamespace = "http://marshalltestservice.org/MarshallTestService.wsdl", portName = "MarshallTestPort1", serviceName = "MarshallTestService", wsdlLocation = "WEB-INF/wsdl/WSW2JDLMarshallTestService.wsdl", endpointInterface = "com.sun.ts.tests.jaxws.ee.w2j.document.literal.marshalltest.MarshallTest")
public class MarshallTestImpl implements MarshallTest {

  private static final ObjectFactory of = new ObjectFactory();

  // ====================================================================
  // Java Primitive Types
  // ====================================================================
  public BooleanTestResponse booleanTest(BooleanTest v) {
    TestUtil.logTrace("booleanTest");
    TestUtil.logMsg("boolean=" + v.isBooleanValue());
    BooleanTestResponse r;
    try {
      r = of.createBooleanTestResponse();
      r.setBooleanValue(v.isBooleanValue());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public ByteTestResponse byteTest(ByteTest v) {
    TestUtil.logTrace("byteTest");
    TestUtil.logMsg("byte=" + v.getByteValue());
    ByteTestResponse r;
    try {
      r = of.createByteTestResponse();
      r.setByteValue(v.getByteValue());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public ShortTestResponse shortTest(ShortTest v) {
    TestUtil.logTrace("shortTest");
    TestUtil.logMsg("short=" + v.getShortValue());
    ShortTestResponse r;
    try {
      r = of.createShortTestResponse();
      r.setShortValue(v.getShortValue());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public IntTestResponse intTest(IntTest v) {
    TestUtil.logTrace("intTest");
    TestUtil.logMsg("int=" + v.getIntValue());
    IntTestResponse r;
    try {
      r = of.createIntTestResponse();
      r.setIntValue(v.getIntValue());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public LongTestResponse longTest(LongTest v) {
    TestUtil.logTrace("longTest");
    TestUtil.logMsg("long=" + v.getLongValue());
    LongTestResponse r;
    try {
      r = of.createLongTestResponse();
      r.setLongValue(v.getLongValue());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public FloatTestResponse floatTest(FloatTest v) {
    TestUtil.logTrace("floatTest");
    TestUtil.logMsg("float=" + v.getFloatValue());
    FloatTestResponse r;
    try {
      r = of.createFloatTestResponse();
      r.setFloatValue(v.getFloatValue());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public DoubleTestResponse doubleTest(DoubleTest v) {
    TestUtil.logTrace("doubleTest");
    TestUtil.logMsg("double=" + v.getDoubleValue());
    DoubleTestResponse r;
    try {
      r = of.createDoubleTestResponse();
      r.setDoubleValue(v.getDoubleValue());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  // ====================================================================
  // Java Primitive Type Arrays (Single Dimensional)
  // ====================================================================
  public BooleanArrayTestResponse booleanArrayTest(BooleanArrayTest v) {
    TestUtil.logTrace("booleanArrayTest");
    BooleanArrayTestResponse r;
    try {
      JAXWS_Data.dumpListValues(v.getBooleanArray());
      r = of.createBooleanArrayTestResponse();
      r.getBooleanArray().addAll(v.getBooleanArray());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public ByteArrayTestResponse byteArrayTest(ByteArrayTest v) {
    TestUtil.logTrace("byteArrayTest");
    ByteArrayTestResponse r;
    try {
      JAXWS_Data.dumpArrayValues(v.getByteArray(), "byte");
      r = of.createByteArrayTestResponse();
      r.setByteArray(v.getByteArray());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public ShortArrayTestResponse shortArrayTest(ShortArrayTest v) {
    TestUtil.logTrace("shortArrayTest");
    ShortArrayTestResponse r;
    try {
      // JAXWS_Data.dumpListValues(v.getShortArray());
      r = of.createShortArrayTestResponse();
      r.getShortArray().addAll(v.getShortArray());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public IntArrayTestResponse intArrayTest(IntArrayTest v) {
    TestUtil.logTrace("intArrayTest");
    IntArrayTestResponse r;
    try {
      JAXWS_Data.dumpListValues(v.getIntArray());
      r = of.createIntArrayTestResponse();
      r.getIntArray().addAll(v.getIntArray());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public LongArrayTestResponse longArrayTest(LongArrayTest v) {
    TestUtil.logTrace("longArrayTest");
    LongArrayTestResponse r;
    try {
      JAXWS_Data.dumpListValues(v.getLongArray());
      r = of.createLongArrayTestResponse();
      r.getLongArray().addAll(v.getLongArray());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public FloatArrayTestResponse floatArrayTest(FloatArrayTest v) {
    TestUtil.logTrace("FloatArrayTest");
    FloatArrayTestResponse r;
    try {
      JAXWS_Data.dumpListValues(v.getFloatArray());
      r = of.createFloatArrayTestResponse();
      r.getFloatArray().addAll(v.getFloatArray());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public DoubleArrayTestResponse doubleArrayTest(DoubleArrayTest v) {
    TestUtil.logTrace("doubleArrayTest");
    DoubleArrayTestResponse r;
    try {
      JAXWS_Data.dumpListValues(v.getDoubleArray());
      r = of.createDoubleArrayTestResponse();
      r.getDoubleArray().addAll(v.getDoubleArray());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public QNameArrayTestResponse qnameArrayTest(QNameArrayTest v) {
    TestUtil.logTrace("qnameArrayTest");
    QNameArrayTestResponse r;
    try {
      JAXWS_Data.dumpListValues(v.getQnameArray1());
      r = of.createQNameArrayTestResponse();
      r.getResult().addAll(v.getQnameArray1());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public QNameTestResponse qnameTest(QNameTest v) {
    TestUtil.logTrace("qnameTest");
    TestUtil.logMsg("QName=" + v.getQname1());
    System.out.println("QName=" + v.getQname1());
    QNameTestResponse res;
    try {
      res = of.createQNameTestResponse();
      res.setResult(v.getQname1());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return res;
  }

  public Base64BinaryTestResponse base64BinaryTest(Base64BinaryTest v) {
    TestUtil.logTrace("base64BinaryTest");
    Base64BinaryTestResponse res;
    try {
      res = of.createBase64BinaryTestResponse();
      res.setResult(v.getBase64Binary1());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return res;
  }

  public HexBinaryTestResponse hexBinaryTest(HexBinaryTest v) {
    TestUtil.logTrace("hexBinaryTest");
    HexBinaryTestResponse res;
    try {
      res = of.createHexBinaryTestResponse();
      res.setResult(v.getHexBinary1());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return res;
  }

  // ====================================================================
  // Standard Java Classes (Scalar, Single Dimensional Arrays)
  // ====================================================================
  public StringTestResponse stringTest(StringTest v) {
    TestUtil.logTrace("stringTest");
    StringTestResponse r;
    try {
      TestUtil.logMsg("String=" + v.getStringValue());
      r = of.createStringTestResponse();
      r.setStringValue(v.getStringValue());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public StringArrayTestResponse stringArrayTest(StringArrayTest v) {
    TestUtil.logTrace("stringArrayTest");
    StringArrayTestResponse r;
    try {
      JAXWS_Data.dumpListValues(v.getStringArray());
      r = of.createStringArrayTestResponse();
      r.getStringArray().addAll(v.getStringArray());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public CalendarTestResponse calendarTest(CalendarTest v) {
    TestUtil.logTrace("calendarTest");
    TestUtil.logMsg("Calendar=" + v.getCalendar());
    CalendarTestResponse r;
    try {
      r = of.createCalendarTestResponse();
      r.setCalendar(v.getCalendar());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public CalendarArrayTestResponse calendarArrayTest(CalendarArrayTest v) {
    TestUtil.logTrace("calendarArrayTest");
    CalendarArrayTestResponse r;
    try {
      JAXWS_Data.dumpListValues(v.getCalendarArray());
      r = of.createCalendarArrayTestResponse();
      r.getCalendarArray().addAll(v.getCalendarArray());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public BigIntegerTestResponse bigIntegerTest(BigIntegerTest v) {
    TestUtil.logTrace("bigIntegerTest");
    TestUtil.logMsg("BigInteger=" + v.getBigInteger());
    BigIntegerTestResponse r;
    try {
      r = of.createBigIntegerTestResponse();
      r.setBigInteger(v.getBigInteger());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public BigIntegerArrayTestResponse bigIntegerArrayTest(
      BigIntegerArrayTest v) {
    TestUtil.logTrace("bigIntegerArrayTest");
    BigIntegerArrayTestResponse r;
    try {
      JAXWS_Data.dumpListValues(v.getBigIntegerArray());
      r = of.createBigIntegerArrayTestResponse();
      r.getBigIntegerArray().addAll(v.getBigIntegerArray());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public BigDecimalTestResponse bigDecimalTest(BigDecimalTest v) {
    TestUtil.logTrace("bigDecimalTest");
    TestUtil.logMsg("BigDecimal=" + v.getBigDecimal());
    BigDecimalTestResponse r;
    try {
      r = of.createBigDecimalTestResponse();
      r.setBigDecimal(v.getBigDecimal());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public BigDecimalArrayTestResponse bigDecimalArrayTest(
      BigDecimalArrayTest v) {
    TestUtil.logTrace("bigDecimalArrayTest");
    BigDecimalArrayTestResponse r;
    try {
      JAXWS_Data.dumpListValues(v.getBigDecimalArray());
      r = of.createBigDecimalArrayTestResponse();
      r.getBigDecimalArray().addAll(v.getBigDecimalArray());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  // ====================================================================
  // JavaBeans Class (Scalar, Single Dimensional Arrays)
  // ====================================================================
  public JavaBeanTestResponse javaBeanTest(JavaBeanTest v) {
    TestUtil.logTrace("javaBeanTest");
    TestUtil.logMsg("JavaBean=" + v.getJavaBean());
    JavaBeanTestResponse r;
    try {
      r = of.createJavaBeanTestResponse();
      r.setJavaBean(v.getJavaBean());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  public JavaBeanArrayTestResponse javaBeanArrayTest(JavaBeanArrayTest v) {
    TestUtil.logTrace("javaBeanArrayTest");
    JavaBeanArrayTestResponse r;
    try {
      r = of.createJavaBeanArrayTestResponse();
      for (JavaBean e : v.getJavaBeanArray())
        r.getJavaBeanArray().add(e);
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  // ====================================================================
  // The void type
  // ====================================================================
  public VoidTestResponse voidTest(VoidTest v) {
    TestUtil.logTrace("voidTest");
    VoidTestResponse r;
    try {
      r = of.createVoidTestResponse();
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

  // ====================================================================
  // other types
  // ====================================================================

  public GYearMonthTestResponse gYearMonthTest(GYearMonthTest v) {
    TestUtil.logTrace("gYearMonthTest");
    GYearMonthTestResponse g;
    try {
      g = of.createGYearMonthTestResponse();
      g.setResult(v.getValue());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return g;
  }

  public GYearTestResponse gYearTest(GYearTest v) {
    TestUtil.logTrace("gYearTest");
    GYearTestResponse g;
    try {
      g = of.createGYearTestResponse();
      g.setResult(v.getValue());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return g;
  }

  public GMonthDayTestResponse gMonthDayTest(GMonthDayTest v) {
    TestUtil.logTrace("gMonthDayTest");
    GMonthDayTestResponse g;
    try {
      g = of.createGMonthDayTestResponse();
      g.setResult(v.getValue());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return g;
  }

  public GDayTestResponse gDayTest(GDayTest v) {
    TestUtil.logTrace("gDayTest");
    GDayTestResponse g;
    try {
      g = of.createGDayTestResponse();
      g.setResult(v.getValue());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return g;
  }

  public GMonthTestResponse gMonthTest(GMonthTest v) {
    TestUtil.logTrace("gMonthTest");
    GMonthTestResponse g;
    try {
      g = of.createGMonthTestResponse();
      g.setResult(v.getValue());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return g;
  }

  public DurationTestResponse durationTest(DurationTest v) {
    TestUtil.logTrace("durationTest");
    DurationTestResponse r;
    try {
      TestUtil.logMsg("Duration=" + v.getDurationValue());
      r = of.createDurationTestResponse();
      r.setDurationValue(v.getDurationValue());
    } catch (Exception e) {
      throw new WebServiceException("Failed on object creation: " + e);
    }
    return r;
  }

}
