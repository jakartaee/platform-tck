/*
* 
* The Apache Software License, Version 1.1
*
* Copyright (c) 2018 Oracle and/or its affiliates. All rights reserved.
* Copyright (c) 1999-2001 The Apache Software Foundation.  All rights 
* reserved.
*
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions
* are met:
*
* 1. Redistributions of source code must retain the above copyright
*    notice, this list of conditions and the following disclaimer. 
*
* 2. Redistributions in binary form must reproduce the above copyright
*    notice, this list of conditions and the following disclaimer in
*    the documentation and/or other materials provided with the
*    distribution.
*
* 3. The end-user documentation included with the redistribution, if
*    any, must include the following acknowlegement:  
*       "This product includes software developed by the 
*        Apache Software Foundation (http://www.apache.org/)."
*    Alternately, this acknowlegement may appear in the software itself,
*    if and wherever such third-party acknowlegements normally appear.
*
* 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
*    Foundation" must not be used to endorse or promote products derived
*    from this software without prior written permission. For written 
*    permission, please contact apache@apache.org.
*
* 5. Products derived from this software may not be called "Apache"
*    nor may "Apache" appear in their names without prior written
*    permission of the Apache Group.
*
* THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
* WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
* OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
* ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
* SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
* LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
* USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
* ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
* OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
* SUCH DAMAGE.
* ====================================================================
*
* This software consists of voluntary contributions made by many
* individuals on behalf of the Apache Software Foundation.  For more
* information on the Apache Software Foundation, please see
* <http://www.apache.org/>.
*
*/
package com.sun.ts.tests.jsp.spec.core_syntax.actions.setproperty;

import java.util.Arrays;

public class SetpropBean {

  // Declaring the variables
  private String name = "hello";

  private int num = 0;

  private String str;

  private int[] iArray = { 15, 20, 35 };

  private byte[] bArray = null;

  private char[] cArray = null;

  private short[] sArray = null;

  private float[] fArray = null;

  private long[] lArray = null;

  private double[] dArray = null;

  private boolean[] boArray = null;

  private Byte[] byteArray = null;

  private Character[] charArray = null;

  private Short[] shortArray = null;

  private Integer[] integerArray = null;

  private Float[] floatArray = null;

  private Long[] longArray = null;

  private Double[] doubleArray = null;

  private Boolean[] booleanArray = null;

  private String bar = "read-only";

  /**
   * Property 'Str'
   */

  public String getStr() {
    return str;
  }

  public void setStr(String a) {
    this.str = a;
  }

  /**
   * Property 'Name'
   */
  public String getName() {
    return name;
  }

  public void setName(String s) {
    this.name = s;
  }

  /**
   * Property 'Num'
   */
  public int getNum() {
    return num;
  }

  public void setNum(int numb) {
    this.num = numb;
  }

  /**
   * property 'intAry' This is an indexed property
   */

  public int[] getIArray() {
    return Arrays.copyOf(iArray, iArray.length);
  }

  public void setIArray(int[] i) {
    this.iArray = Arrays.copyOf(i, i.length);
  }

  public byte[] getBArray() {
    return Arrays.copyOf(bArray, bArray.length);
  }

  public void setBArray(byte[] b) {
    this.bArray = Arrays.copyOf(b, b.length);
  }

  public char[] getCArray() {
    return Arrays.copyOf(cArray, cArray.length);
  }

  public void setCArray(char[] c) {
    this.cArray = Arrays.copyOf(c, c.length);
  }

  public short[] getSArray() {
    return Arrays.copyOf(sArray, sArray.length);
  }

  public void setSArray(short[] s) {
    this.sArray = Arrays.copyOf(s, s.length);
  }

  public float[] getFArray() {
    return Arrays.copyOf(fArray, fArray.length);
  }

  public void setFArray(float[] f) {
    this.fArray = Arrays.copyOf(f, f.length);
  }

  public double[] getDArray() {
    return Arrays.copyOf(dArray, dArray.length);
  }

  public void setDArray(double[] d) {
    this.dArray = Arrays.copyOf(d, d.length);
  }

  public long[] getLArray() {
    return Arrays.copyOf(lArray, lArray.length);
  }

  public void setLArray(long[] l) {
    this.lArray = Arrays.copyOf(l, l.length);
  }

  public boolean[] getBoArray() {
    return Arrays.copyOf(boArray, boArray.length);
  }

  public void setBoArray(boolean[] b) {
    this.boArray = Arrays.copyOf(b, b.length);
  }

  public Byte[] getByteArray() {
    return Arrays.copyOf(byteArray, byteArray.length);
  }

  public void setByteArray(Byte[] b) {
    this.byteArray = Arrays.copyOf(b, b.length);
  }

  public Character[] getCharArray() {
    return Arrays.copyOf(charArray, charArray.length);
  }

  public void setCharArray(Character[] c) {
    this.charArray = Arrays.copyOf(c, c.length);
  }

  public Short[] getShortArray() {
    return Arrays.copyOf(shortArray, shortArray.length);
  }

  public void setShortArray(Short[] s) {
    this.shortArray = Arrays.copyOf(s, s.length);
  }

  public Integer[] getIntegerArray() {
    return Arrays.copyOf(integerArray, integerArray.length);
  }

  public void setIntegerArray(Integer[] i) {
    this.integerArray = Arrays.copyOf(i, i.length);
  }

  public Float[] getFloatArray() {
    return Arrays.copyOf(floatArray, floatArray.length);
  }

  public void setFloatArray(Float[] f) {
    this.floatArray = Arrays.copyOf(f, f.length);
  }

  public Long[] getLongArray() {
    return Arrays.copyOf(longArray, longArray.length);
  }

  public void setLongArray(Long[] l) {
    this.longArray = Arrays.copyOf(l, l.length);
  }

  public Double[] getDoubleArray() {
    return Arrays.copyOf(doubleArray, doubleArray.length);
  }

  public void setDoubleArray(Double[] d) {
    this.doubleArray = Arrays.copyOf(d, d.length);
  }

  public Boolean[] getBooleanArray() {
    return Arrays.copyOf(booleanArray, booleanArray.length);
  }

  public void setBooleanArray(Boolean[] b) {
    this.booleanArray = Arrays.copyOf(b, b.length);
  }

  /**
   * property 'bar' This is a read only property
   */

  public String getBar() {
    return bar;
  }

}
