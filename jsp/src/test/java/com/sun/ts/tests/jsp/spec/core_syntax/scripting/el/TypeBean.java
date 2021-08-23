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

package com.sun.ts.tests.jsp.spec.core_syntax.scripting.el;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;

/**
 * Simple bean to return all primitive and their respective boxed types.
 */
public class TypeBean implements Serializable {

  /**
   * Various types to use in testing
   */
  private byte b = (byte) 0;

  private char c = '1';

  private short s = (short) 2;

  private int i = 3;

  private long l = 4L;

  private float f = 5.5f;

  private double d = 6.5d;

  private boolean bool = false;

  private Byte bb = Byte.valueOf((byte) 30);

  private Character cc = Character.valueOf((char) 31);

  private Short ss = Short.valueOf((short) 32);

  private Integer ii = Integer.valueOf(33);

  private Long ll = Long.valueOf(34L);

  private Float ff = Float.valueOf(35.5f);

  private Double dd = Double.valueOf(36.5d);

  private Boolean bbool = Boolean.TRUE;

  private BigDecimal bigDec = BigDecimal.valueOf(100.5d);

  private BigInteger bigInt = BigInteger.valueOf(125);

  /**
   * Returns a BigInteger.
   * 
   * @return a BigInteger
   */
  public BigInteger getBigInt() {
    return bigInt;
  }

  /**
   * Returns a BigDecimal
   * 
   * @return a BigDecimal
   */
  public BigDecimal getBigDec() {
    return bigDec;
  }

  /**
   * Returns a byte primitive.
   * 
   * @return a byte primitive
   */
  public byte getBytePrim() {
    return b;
  }

  /**
   * Sets a byte primitive
   * 
   * @param b
   *          - a byte primitive
   */
  public void setBytePrim(byte b) {
    this.b = b;
  }

  /**
   * Returns a char primitive.
   * 
   * @return a char primitive
   */
  public char getCharPrim() {
    return c;
  }

  /**
   * Sets a char primitive.
   * 
   * @param c
   *          - a char primitive
   */
  public void setCharPrim(char c) {
    this.c = c;
  }

  /**
   * Returns a short primitive.
   * 
   * @return a short primitive
   */
  public short getShortPrim() {
    return s;
  }

  /**
   * Sets a short primitive.
   * 
   * @param s
   *          - a short primitive
   */
  public void setShortPrim(short s) {
    this.s = s;
  }

  /**
   * Returns an int primitive
   * 
   * @return an int primitive
   */
  public int getIntPrim() {
    return i;
  }

  /**
   * Sets an int primitive
   * 
   * @param i
   *          - an int primitive
   */
  public void setIntPrim(int i) {
    this.i = i;
  }

  /**
   * Returns a long primitive.
   * 
   * @return a long primitive
   */
  public long getLongPrim() {
    return l;
  }

  /**
   * Sets a long primitive.
   * 
   * @param l
   *          - a long primitive
   */
  public void setLongPrim(long l) {
    this.l = l;
  }

  /**
   * Returns a float primitive.
   * 
   * @return a float primtitive
   */
  public float getFloatPrim() {
    return f;
  }

  /**
   * Sets a float primitive.
   * 
   * @param f
   *          - a float primitive
   */
  public void setFloatPrim(float f) {
    this.f = f;
  }

  /**
   * Returns a double primitive.
   * 
   * @return a double primitive
   */
  public double getDoublePrim() {
    return d;
  }

  /**
   * Sets a double primitive.
   * 
   * @param d
   *          - a double primitive
   */
  public void setDoublePrim(double d) {
    this.d = d;
  }

  /**
   * Sets a boolean primitive.
   * 
   * @param b
   *          - a boolean primitive
   */
  public void setBooleanPrim(boolean b) {
    this.bool = b;
  }

  /**
   * Returns a boolean primitive.
   * 
   * @return a boolean primitive.
   */
  public boolean getBooleanPrim() {
    return bool;
  }

  /**
   * Returns a Byte.
   * 
   * @return a Byte
   */
  public Byte getBite() {
    return bb;
  }

  /**
   * Sets a Byte.
   * 
   * @param bb
   *          - a Byte
   */
  public void setBite(Byte bb) {
    this.bb = bb;
  }

  /**
   * Returns a Character.
   * 
   * @return a Character
   */
  public Character getChr() {
    return cc;
  }

  /**
   * Sets a Character.
   * 
   * @param cc
   *          - a Character
   */
  public void setChr(Character cc) {
    this.cc = cc;
  }

  /**
   * Returns a Short.
   * 
   * @return a Short
   */
  public Short getShrt() {
    return ss;
  }

  /**
   * Sets a Short.
   * 
   * @param ss
   *          - a Short
   */
  public void setShrt(Short ss) {
    this.ss = ss;
  }

  /**
   * Returns an Integer.
   * 
   * @return an Integer
   */
  public Integer getInti() {
    return ii;
  }

  /**
   * Sets an Integer.
   * 
   * @param ii
   *          - an Integer
   */
  public void setInti(Integer ii) {
    this.ii = ii;
  }

  /**
   * Returns a Long.
   * 
   * @return a Long
   */
  public Long getLng() {
    return ll;
  }

  /**
   * Sets a Long.
   * 
   * @param ll
   *          - a Long
   */
  public void setLng(Long ll) {
    this.ll = ll;
  }

  /**
   * Returns a Float.
   * 
   * @return a Float
   */
  public Float getFlote() {
    return ff;
  }

  /**
   * Sets a Float.
   * 
   * @param ff
   *          - a Float
   */
  public void setFlote(Float ff) {
    this.ff = ff;
  }

  /**
   * Returns a Double.
   * 
   * @return - a Double
   */
  public Double getDble() {
    return dd;
  }

  /**
   * Sets a Double.
   * 
   * @param dd
   *          - a Double
   */
  public void setDble(Double dd) {
    this.dd = dd;
  }

  /**
   * Sets a Boolean.
   * 
   * @param b
   *          - a Boolean
   */
  public void setBooln(Boolean b) {
    this.bbool = b;
  }

  /**
   * Returns a Boolean.
   * 
   * @return a Boolean
   */
  public Boolean getBooln() {
    return bbool;
  }

  /**
   * Returns a Boolean (of value true).
   * 
   * @return a true Boolean
   */
  public boolean isCallable() {
    return true;
  }

  /**
   * Returns information about this simple bean.
   * 
   * @return a String
   */
  public String toString() {
    return "TypeBean;v,1.0";
  }
}
