/*
 * Copyright (c) 2012, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.el.common.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;

/**
 * This is a simple bean with different type values for testing Expressions.
 * 
 */
public class TypesBean {
  private final static String COMPARATOR = "1";

  private static HashMap<Class<?>, String> numberMap;

  private BigDecimal tckBigDecimal = BigDecimal
      .valueOf(Long.valueOf(COMPARATOR));

  private Double tckDouble = Double.valueOf(COMPARATOR);

  private Float tckFloat = Float.valueOf(COMPARATOR);

  private BigInteger tckBigInteger = BigInteger
      .valueOf(Long.valueOf(COMPARATOR));

  private Long tckLong = Long.valueOf(COMPARATOR);

  private Integer tckInteger = Integer.valueOf(COMPARATOR);

  private Short tckShort = Short.valueOf(COMPARATOR);

  private Byte tckByte = Byte.valueOf(COMPARATOR);

  private Boolean tckBoolean = true;

  private Byte tckNull = null;

  // Getter Setter for Type BigDeciaml
  public BigDecimal getTckBigDecimal() {
    return tckBigDecimal;
  }

  public void setTckBigDecimal(BigDecimal tckBigDecimal) {
    this.tckBigDecimal = tckBigDecimal;
  }

  // Getter Setter for Type Double
  public Double getTckDouble() {
    return tckDouble;
  }

  public void setTckDouble(Double tckDouble) {
    this.tckDouble = tckDouble;
  }

  // Getter Setter for Type Float
  public Float getTckFloat() {
    return tckFloat;
  }

  public void setTckFloat(Float tckFloat) {
    this.tckFloat = tckFloat;
  }

  // Getter Setter for Type Integer
  public BigInteger getTckBigInteger() {
    return tckBigInteger;
  }

  public void setTckBigInteger(BigInteger tckBigInteger) {
    this.tckBigInteger = tckBigInteger;
  }

  // Getter Setter for Type Long
  public Long getTckLong() {
    return tckLong;
  }

  public void setTckLong(Long tckLong) {
    this.tckLong = tckLong;
  }

  // Getter Setter for Type Integer
  public Integer getTckInteger() {
    return tckInteger;
  }

  public void setTckInteger(Integer tckInt) {
    this.tckInteger = tckInt;
  }

  // Getter Setter for Type Short
  public Short getTckShort() {
    return tckShort;
  }

  public void setTckShort(Short tchShort) {
    this.tckShort = tchShort;
  }

  // Getter Setter for Type Byte
  public Byte getTckByte() {
    return tckByte;
  }

  public void setTckByte(Byte tckByte) {
    this.tckByte = tckByte;
  }

  // Getter Setter for Type Boolean
  public Boolean getTckBoolean() {
    return tckBoolean;
  }

  public void setTckBoolean(Boolean tckBoolean) {
    this.tckBoolean = tckBoolean;
  }

  public Byte getTckNull() {
    return tckNull;
  }

  public void setTckNullString(Byte tckNull) {
    this.tckNull = tckNull;
  }

  /**
   * This method will return an HaashMap of Key = Class, Value = String for the
   * purpose of using them in an ElProcessor expression.
   * 
   * 
   * @return - A common List of Number types with a constant value.
   */
  public static HashMap<Class<?>, String> getNumberMap() {

    numberMap = new HashMap<Class<?>, String>();

    numberMap.put(BigDecimal.class, "b = types.tckBigDecimal");
    numberMap.put(Double.class, "b = types.tckDouble");
    numberMap.put(Float.class, "b = types.tckFloat");
    numberMap.put(BigInteger.class, "b = types.tckBigInteger");
    numberMap.put(Long.class, "b = types.tckLong");
    numberMap.put(Integer.class, "b = types.tckInteger");
    numberMap.put(Short.class, "b = types.tckShort");
    numberMap.put(Byte.class, "b = types.tckByte");

    return numberMap;

  }

}
