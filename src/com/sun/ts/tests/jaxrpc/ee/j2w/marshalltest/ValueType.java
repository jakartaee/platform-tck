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

import java.util.*;
import java.math.*;

public class ValueType implements java.io.Serializable {

  // Primitive Data Types
  public boolean myBoolean;

  public byte myByte;

  public short myShort;

  public int myInt;

  public long myLong;

  public float myFloat;

  public double myDouble;

  // Primitive Data Type Wrappers
  public Boolean myBoolean1;

  public Byte myByte1;

  public Short myShort1;

  public Integer myInt1;

  public Long myLong1;

  public Float myFloat1;

  public Double myDouble1;

  // Standard Java Classes
  public String myString;

  public BigInteger myBigInteger;

  public BigDecimal myBigDecimal;

  public Calendar myCalendar;

  // Arrays of Primitive Data Types
  public boolean[] myBooleanArray;

  public byte[] myByteArray;

  public short[] myShortArray;

  public int[] myIntArray;

  public long[] myLongArray;

  public float[] myFloatArray;

  public double[] myDoubleArray;

  // Arrays of Primitive Data Type Wrappers
  public Boolean[] myBoolean1Array;

  public Short[] myShort1Array;

  public Integer[] myInt1Array;

  public Long[] myLong1Array;

  public Float[] myFloat1Array;

  public Double[] myDouble1Array;

  // Arrays of Standard Java Classes
  public String[] myStringArray;

  public BigInteger[] myBigIntegerArray;

  public BigDecimal[] myBigDecimalArray;

  public Calendar[] myCalendarArray;

  // Private stuff which should not be mapped
  private boolean myBooleanNotMapped;

  private byte myByteNotMapped;

  private short myShortNotMapped;

  private int myIntNotMapped;

  private long myLongNotMapped;

  private float myFloatNotMapped;

  private double myDoubleNotMapped;

  public void notMethodNotMapped() {
  };

  // Default constructor
  public ValueType() {
  }
}
