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

package com.sun.ts.tests.jaxws.ee.j2w.document.literal.marshalltest.server;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import java.math.*;

public class JavaBean implements java.io.Serializable {

  // Primitive Data Types
  private boolean myBoolean;

  private byte myByte;

  private short myShort;

  private int myInt;

  private long myLong;

  private float myFloat;

  private double myDouble;

  // Primitive Data Type Wrappers
  private Boolean myBoolean1;

  private Byte myByte1;

  private Short myShort1;

  private Integer myInt1;

  private Long myLong1;

  private Float myFloat1;

  private Double myDouble1;

  // Standard Java Classes
  private String myString;

  private BigInteger myBigInteger;

  private BigDecimal myBigDecimal;

  private Calendar myCalendar;

  // Default constructor
  public JavaBean() {
  }

  public void setMyBoolean(boolean v) {
    myBoolean = v;
  }

  public boolean getMyBoolean() {
    return myBoolean;
  }

  public boolean isMyBoolean() {
    return myBoolean;
  }

  public void setMyByte(byte v) {
    myByte = v;
  }

  public byte getMyByte() {
    return myByte;
  }

  public void setMyShort(short v) {
    myShort = v;
  }

  public short getMyShort() {
    return myShort;
  }

  public void setMyInt(int v) {
    myInt = v;
  }

  public int getMyInt() {
    return myInt;
  }

  public void setMyLong(long v) {
    myLong = v;
  }

  public long getMyLong() {
    return myLong;
  }

  public void setMyFloat(float v) {
    myFloat = v;
  }

  public float getMyFloat() {
    return myFloat;
  }

  public void setMyDouble(double v) {
    myDouble = v;
  }

  public double getMyDouble() {
    return myDouble;
  }

  public void setMyBoolean1(Boolean v) {
    myBoolean1 = v;
  }

  public Boolean getMyBoolean1() {
    return myBoolean1;
  }

  public Boolean isMyBoolean1() {
    return myBoolean1;
  }

  public void setMyByte1(Byte v) {
    myByte1 = v;
  }

  public Byte getMyByte1() {
    return myByte1;
  }

  public void setMyShort1(Short v) {
    myShort1 = v;
  }

  public Short getMyShort1() {
    return myShort1;
  }

  public void setMyInt1(Integer v) {
    myInt1 = v;
  }

  public Integer getMyInt1() {
    return myInt1;
  }

  public void setMyLong1(Long v) {
    myLong1 = v;
  }

  public Long getMyLong1() {
    return myLong1;
  }

  public void setMyFloat1(Float v) {
    myFloat1 = v;
  }

  public Float getMyFloat1() {
    return myFloat1;
  }

  public void setMyDouble1(Double v) {
    myDouble1 = v;
  }

  public Double getMyDouble1() {
    return myDouble1;
  }

  public void setMyString(String v) {
    myString = v;
  }

  public String getMyString() {
    return myString;
  }

  public void setMyBigInteger(BigInteger v) {
    myBigInteger = v;
  }

  public BigInteger getMyBigInteger() {
    return myBigInteger;
  }

  public void setMyBigDecimal(BigDecimal v) {
    myBigDecimal = v;
  }

  public BigDecimal getMyBigDecimal() {
    return myBigDecimal;
  }

  public void setMyCalendar(Calendar v) {
    myCalendar = v;
  }

  public Calendar getMyCalendar() {
    return myCalendar;
  }
}
