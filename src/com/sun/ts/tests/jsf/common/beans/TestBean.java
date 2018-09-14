/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.jsf.common.beans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;

public class TestBean {

  public static final String PROP = "oneSet";

  public static final String TRUE = "true";

  public static final String FALSE = "false";

  // --------------------------------------------------------------------------
  // private instance variables
  private String stkTrace = "false";

  private String writeOnlyProperty = "Write Only String Property";

  // -----------------------------------------------------------------------
  // protected instance variables
  protected String one = null;

  protected String prop = null;

  protected boolean boolProp = false;

  protected Boolean boolWrapProp = Boolean.FALSE;

  protected byte byteProp = Byte.MAX_VALUE;

  protected char charProp = 'a';

  protected double doubleProp = 37.266D;

  protected float floatProp = 87.363F;

  protected int intProp = Integer.MAX_VALUE;

  protected long longProp = Long.MAX_VALUE;

  protected short shortProp = Short.MAX_VALUE;

  protected String imagePath = null;

  protected InnerBean inner = null;

  protected ArrayList<String> indexProperties = null;

  protected ArrayList<Integer> indexIntegerProperties = null;

  protected Map mapProperty = new HashMap();

  protected String modelLabel = "model label";

  protected UIComponent component = new UIInput();

  public TestBean() {
    indexProperties = new ArrayList<String>();
    indexProperties.add("Justyna");
    indexProperties.add("Roger");
    indexProperties.add("Ed");
    indexProperties.add("Jayashri");
    indexProperties.add("Craig");

    Integer integer;
    indexIntegerProperties = new ArrayList<Integer>();
    integer = Integer.valueOf("5");
    indexIntegerProperties.add(integer);
    integer = Integer.valueOf("10");
    indexIntegerProperties.add(integer);
  }

  public void setOne(String newOne) {
    one = newOne;
    if (!newOne.equals("one")) {
      throw new RuntimeException("Value must be 'one'");
    }
  }

  public String getOne() {
    return one;
  }

  public void setProp(String newProp) {
    prop = newProp;
  }

  public String getProp() {
    return prop;
  }

  public void setBoolProp(boolean newBoolProp) {
    boolProp = newBoolProp;
  }

  public boolean getBoolProp() {
    return boolProp;
  }

  public Boolean getBoolWrapProp() {
    return boolWrapProp;
  }

  public void setBoolWrapProp(Boolean boolWrapProp) {
    this.boolWrapProp = boolWrapProp;
  }

  public void setByteProp(byte newByteProp) {
    byteProp = newByteProp;
  }

  public byte getByteProp() {
    return byteProp;
  }

  public void setCharProp(char newCharProp) {
    charProp = newCharProp;
  }

  public char getCharProp() {
    return charProp;
  }

  public void setDoubleProp(double newDoubleProp) {
    doubleProp = newDoubleProp;
  }

  public double getDoubleProp() {
    return doubleProp;
  }

  public void setFloatProp(float newFloatProp) {
    floatProp = newFloatProp;
  }

  public float getFloatProp() {
    return floatProp;
  }

  public void setIntProp(int newIntProp) {
    intProp = newIntProp;
  }

  public int getIntProp() {
    return intProp;
  }

  public void setLongProp(long newLongProp) {
    longProp = newLongProp;
  }

  public long getLongProp() {
    return longProp;
  }

  public void setShortProp(short newShortProp) {
    shortProp = newShortProp;
  }

  public short getShortProp() {
    return shortProp;
  }

  public void setInner(InnerBean newInner) {
    inner = newInner;
  }

  public InnerBean getInner() {
    return inner;
  }

  public String getImagePath() {
    return imagePath;
  }

  public void setImagePath(String newImagePath) {
    imagePath = newImagePath;
  }

  public ArrayList<String> getIndexProperties() {
    return indexProperties;
  }

  public void setIndexProperties(String property) {
    indexProperties.add(property);
  }

  public ArrayList<Integer> getIndexIntegerProperties() {
    return indexIntegerProperties;
  }

  public void setIndexIntegerProperties(Integer property) {
    indexIntegerProperties.add(property);
  }

  public Map getMapProperty() {
    return mapProperty;
  }

  public void setMapProperty(Map mapProperty) {
    this.mapProperty = mapProperty;
  }

  public void setModelLabel(String modelLabel) {
    this.modelLabel = modelLabel;
  }

  public String getModelLabel() {
    return modelLabel;
  }

  public UIComponent getComponent() {
    return component;
  }

  public void setComponent(UIComponent component) {
    this.component = component;
  }

  public void setException(String exceptionClass) {
    // no-op
  }

  public String getException() {
    throw new IllegalStateException();
  }

  public String getWriteOnlyPropertyValue() {
    return (this.writeOnlyProperty);
  }

  public void setWriteOnlyProperty(String writeOnlyProperty) {
    this.writeOnlyProperty = writeOnlyProperty;
  }

  /**
   * @return the stkTrace
   */
  public String getStkTrace() {
    return stkTrace;
  }

  /**
   * @param stkTrace
   *          the stkTrace to set
   */
  public void setStkTrace(String stkTrace) throws TckException {
    throw new TckException();
  }

  public String getReadOnly() {
    return "readOnly";
  }

  // ------------------------------------------------------------------------------------------
  // public classes
  public static class InnerBean {

    protected String two = null;

    protected Integer pin = null;

    protected Boolean result = null;

    protected ArrayList<String> customers = null;

    public void setTwo(String newTwo) {
      two = newTwo;
      if (!newTwo.equals("two")) {
        throw new RuntimeException("Value must be 'two'");
      }
    }

    public String getTwo() {
      return two;
    }

    public void setPin(Integer newPin) {
      pin = newPin;
    }

    public Integer getPin() {
      return pin;
    }

    public Collection<String> getCustomers() {
      if (null == customers) {
        customers = new ArrayList<String>();
        customers.add("Mickey");
        customers.add("Jerry");
        customers.add("Phil");
        customers.add("Bill");
        customers.add("Bob");
      }
      return customers;
    }

    public void setCustomers(String customer) {
      if (null == customers) {
        customers = new ArrayList<String>();
      }
      customers.add(customer);
    }

    public void setResult(Boolean newResult) {
      result = newResult;
    }

    public Boolean getResult() {
      return result;
    }

    protected Inner2Bean inner2 = null;

    public void setInner2(Inner2Bean newInner2) {
      inner2 = newInner2;
    }

    public Inner2Bean getInner2() {
      return inner2;
    }
  }

  public static class Inner2Bean {
    protected Map nicknames = new HashMap();

    protected String three = null;

    public void setThree(String newThree) {
      three = newThree;
      if (!newThree.equals("three")) {
        throw new RuntimeException("Value must be 'three'");
      }
    }

    public String getThree() {
      return three;
    }

    public Map getNicknames() {
      return nicknames;
    }

    public void setNicknames(Map newNicknames) {
      nicknames = newNicknames;
    }
  }

  // ----------------------------------------------------------------------------------------
  // private classes
  private static class TckException extends Exception {

    String mistake = null;

    public TckException() {
      super();
      mistake = "BROKEN";
    }

    public String getError() {
      return mistake;
    }
  }
}
