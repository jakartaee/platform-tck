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

public class MiscBean {

  // Declaring the variables
  private boolean primitiveBoolean;

  private Boolean objectBoolean;

  private byte primitiveByte;

  private Byte objectByte;

  private char primitiveChar;

  private Character objectChar;

  private double primitiveDouble;

  private Double objectDouble;

  private int primitiveInt;

  private Integer objectInt;

  private float primitiveFloat;

  private Float objectFloat;

  private long primitiveLong;

  private Long objectLong;

  private String path;

  /**
   * we define set and get methods for boolean variable
   */

  public void setPrimitiveBoolean(boolean b) {
    this.primitiveBoolean = b;
  }

  public boolean getPrimitiveBoolean() {
    return primitiveBoolean;
  }

  /**
   * we define set and get methods for Boolean variable
   */
  public void setObjectBoolean(Boolean b) {
    this.objectBoolean = b;
  }

  public Boolean getObjectBoolean() {
    return objectBoolean;
  }

  /**
   * we define set and get methods for byte variable
   */
  public void setPrimitiveByte(byte b) {
    this.primitiveByte = b;
  }

  public byte getPrimitiveByte() {
    return primitiveByte;
  }

  /**
   * we define set and get methods for Byte variable
   */
  public void setObjectByte(Byte b) {
    this.objectByte = b;
  }

  public Byte getObjectByte() {
    return objectByte;
  }

  /**
   * we define set and get methods for char variable
   */
  public void setPrimitiveChar(char b) {
    this.primitiveChar = b;
  }

  public char getPrimitiveChar() {
    return primitiveChar;
  }

  /**
   * we define set and get methods for Character variable
   */
  public void setObjectChar(Character b) {
    this.objectChar = b;
  }

  public Character getObjectChar() {
    return objectChar;
  }

  /**
   * we define set and get methods for double variable
   */
  public void setPrimitiveDouble(double b) {
    this.primitiveDouble = b;
  }

  public double getPrimitiveDouble() {
    return primitiveDouble;
  }

  /**
   * we define set and get methods for Double variable
   */
  public void setObjectDouble(Double b) {
    this.objectDouble = b;
  }

  public Double getObjectDouble() {
    return objectDouble;
  }

  /**
   * we define set and get methods for int variable
   */
  public void setPrimitiveInt(int i) {
    primitiveInt = i;
  }

  public int getPrimitiveInt() {
    return primitiveInt;
  }

  /**
   * we define set and get methods for Integer variable
   */
  public void setObjectInt(Integer in) {
    this.objectInt = in;
  }

  public Integer getObjectInt() {
    return objectInt;
  }

  /**
   * we define set and get methods for float variable
   */
  public void setPrimitiveFloat(float f) {
    primitiveFloat = f;
  }

  public float getPrimitiveFloat() {
    return primitiveFloat;
  }

  /**
   * we define set and get methods for Float variable
   */
  public void setObjectFloat(Float fl) {
    this.objectFloat = fl;
  }

  public Float getObjectFloat() {
    return objectFloat;
  }

  /**
   * we define set and get methods for long variable
   */
  public void setPrimitiveLong(long l) {
    primitiveLong = l;
  }

  public long getPrimitiveLong() {
    return primitiveLong;
  }

  /**
   * we define set and get methods for Long variable
   */
  public void setObjectLong(Long ln) {
    this.objectLong = ln;
  }

  public Long getObjectLong() {
    return objectLong;
  }

  /**
   * we define set and get methods for path variable which is a String
   */

  public void setPath(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

}
