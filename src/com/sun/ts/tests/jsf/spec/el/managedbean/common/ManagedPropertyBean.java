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

package com.sun.ts.tests.jsf.spec.el.managedbean.common;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ManagedPropertyBean implements Serializable {

  public ManagedPropertyBean() {

    System.out.println("ManagedPropertyBean created.");
    mapProperty = new HashMap();
    mapProperty.put("nullKey", "not null");
    primList = new ArrayList();
    objList = new ArrayList();

  } // END ManagedPropertyBean

  // --------------------------------------------------------- Bean Properties

  private String nullProperty = "not null";

  public String getNullProperty() {

    return nullProperty;

  } // END getNullProperty

  public void setNullProperty(String nullProperty) {

    this.nullProperty = nullProperty;

  } // END setNullProperty

  // -------------------------------------------------------------------------

  private String stringProperty;

  public String getStringProperty() {

    return stringProperty;

  } // END getStringProperty

  public void setStringProperty(String stringProperty) {

    this.stringProperty = stringProperty;

  } // END setStringProperty

  // -------------------------------------------------------------------------

  private Map mapProperty;

  public Map getMapProperty() {

    return mapProperty;

  } // END getMapProperty

  public void setMapProperty(Map mapProperty) {

    this.mapProperty = mapProperty;

  } // END setMapProperty

  // -------------------------------------------------------------------------

  private List listProperty;

  public List getListProperty() {

    return listProperty;

  } // END getListProperty

  public void setListProperty(List listProperty) {

    this.listProperty = listProperty;

  } // END setListProperty

  // -------------------------------------------------------------------------

  private Map typedMapProperty;

  public Map getByteMapProperty() {

    return typedMapProperty;

  } // END getTypedMapProperty

  public void setByteMapProperty(Map typedMapProperty) {

    this.typedMapProperty = typedMapProperty;

  } // END setTypedMapProperty

  public Map getCharMapProperty() {

    return typedMapProperty;

  } // END getCharMapProperty

  public void setCharMapProperty(Map typedMapProperty) {

    this.typedMapProperty = typedMapProperty;

  } // END getCharMapProperty

  public Map getBooleanMapProperty() {

    return typedMapProperty;

  } // END getCharMapProperty

  public void setBooleanMapProperty(Map typedMapProperty) {

    this.typedMapProperty = typedMapProperty;

  } // END getBooleanMapProperty

  public Map getShortMapProperty() {

    return typedMapProperty;

  } // END getCharMapProperty

  public void setShortMapProperty(Map typedMapProperty) {

    this.typedMapProperty = typedMapProperty;

  } // END getShortMapProperty

  public Map getIntegerMapProperty() {

    return typedMapProperty;

  } // END getCharMapProperty

  public void setIntegerMapProperty(Map typedMapProperty) {

    this.typedMapProperty = typedMapProperty;

  } // END getIntegerMapProperty

  public Map getLongMapProperty() {

    return typedMapProperty;

  } // END getCharMapProperty

  public void setLongMapProperty(Map typedMapProperty) {

    this.typedMapProperty = typedMapProperty;

  } // END getLongMapProperty

  public Map getFloatMapProperty() {

    return typedMapProperty;

  } // END getCharMapProperty

  public void setFloatMapProperty(Map typedMapProperty) {

    this.typedMapProperty = typedMapProperty;

  } // END getFloatMapProperty

  public Map getDoubleMapProperty() {

    return typedMapProperty;

  } // END getCharMapProperty

  public void setDoubleMapProperty(Map typedMapProperty) {

    this.typedMapProperty = typedMapProperty;

  } // END getDoubleMapProperty

  // -------------------------------------------------------------------------

  private List typedListProperty;

  public List getByteListProperty() {

    return typedListProperty;

  } // END getTypedListProperty

  public void setByteListProperty(List typedListProperty) {

    this.typedListProperty = typedListProperty;

  } // END setTypedListProperty

  public List getCharListProperty() {

    return typedListProperty;

  } // END getCharListProperty

  public void setCharListProperty(List typedListProperty) {

    this.typedListProperty = typedListProperty;

  } // END getCharListProperty

  public List getBooleanListProperty() {

    return typedListProperty;

  } // END getCharListProperty

  public void setBooleanListProperty(List typedListProperty) {

    this.typedListProperty = typedListProperty;

  } // END getBooleanListProperty

  public List getShortListProperty() {

    return typedListProperty;

  } // END getCharListProperty

  public void setShortListProperty(List typedListProperty) {

    this.typedListProperty = typedListProperty;

  } // END getShortListProperty

  public List getIntegerListProperty() {

    return typedListProperty;

  } // END getCharListProperty

  public void setIntegerListProperty(List typedListProperty) {

    this.typedListProperty = typedListProperty;

  } // END getIntegerListProperty

  public List getLongListProperty() {

    return typedListProperty;

  } // END getCharListProperty

  public void setLongListProperty(List typedListProperty) {

    this.typedListProperty = typedListProperty;

  } // END getLongListProperty

  public List getFloatListProperty() {

    return typedListProperty;

  } // END getCharListProperty

  public void setFloatListProperty(List typedListProperty) {

    this.typedListProperty = typedListProperty;

  } // END getFloatListProperty

  public List getDoubleListProperty() {

    return typedListProperty;

  } // END getCharListProperty

  public void setDoubleListProperty(List typedListProperty) {

    this.typedListProperty = typedListProperty;

  } // END getDoubleListProperty

  // -------------------------------------------------------------------------

  byte bytePrim;

  Byte byteObj;

  public void setBytePrim(byte bytePrim) {

    this.bytePrim = bytePrim;
    primList.add(Byte.valueOf(bytePrim));

  } // END setBytePrim

  public byte getBytePrim() {

    return bytePrim;

  } // END getBytePrim

  public void setByteObj(Byte byteObj) {

    this.byteObj = byteObj;
    objList.add(byteObj);

  } // END setByteObj

  public Byte getByteObj() {

    return byteObj;

  } // END getByteObj

  // -------------------------------------------------------------------------

  char charPrim;

  Character charObj;

  public void setCharPrim(char charPrim) {

    this.charPrim = charPrim;
    primList.add(Character.valueOf(charPrim));

  } // END setCharPrim

  public char getCharPrim() {

    return charPrim;

  } // END getCharPrim

  public void setCharObj(Character charObj) {

    this.charObj = charObj;
    objList.add(charObj);

  } // END setCharObj

  public Character getCharObj() {

    return charObj;

  } // END getCharObj

  // -------------------------------------------------------------------------

  boolean booleanPrim;

  Boolean booleanObj;

  public void setBooleanPrim(boolean booleanPrim) {

    this.booleanPrim = booleanPrim;
    primList.add(Boolean.valueOf(booleanPrim));

  } // END setBooleanPrim

  public boolean getBooleanPrim() {

    return booleanPrim;

  } // END getBooleanPrim

  public void setBooleanObj(Boolean booleanObj) {

    this.booleanObj = booleanObj;
    objList.add(booleanObj);

  } // END setBooleanObj

  public Boolean getBooleanObj() {

    return booleanObj;

  } // END getBooleanObj

  // -------------------------------------------------------------------------

  short shortPrim;

  Short shortObj;

  public void setShortPrim(short shortPrim) {

    this.shortPrim = shortPrim;
    primList.add(Short.valueOf(shortPrim));

  } // END setShortPrim

  public short getShortPrim() {

    return shortPrim;

  } // END getShortPrim

  public void setShortObj(Short shortObj) {

    this.shortObj = shortObj;
    objList.add(shortObj);

  } // END setShortObj

  public Short getShortObj() {

    return shortObj;

  } // END getShortObj

  // -------------------------------------------------------------------------

  int intPrim;

  Integer intObj;

  public void setIntegerPrim(int intPrim) {

    this.intPrim = intPrim;
    primList.add(Integer.valueOf(intPrim));

  } // END setIntegerPrim

  public int getIntegerPrim() {

    return intPrim;

  } // END getIntegerPrim

  public void setIntegerObj(Integer intObj) {

    this.intObj = intObj;
    objList.add(intObj);

  } // END setIntegerObj

  public Integer getIntegerObj() {

    return intObj;

  } // END getIntegerObj

  // -------------------------------------------------------------------------

  long longPrim;

  Long longObj;

  public void setLongPrim(long longPrim) {

    this.longPrim = longPrim;
    primList.add(Long.valueOf(longPrim));

  } // END setLongPrim

  public long getLongPrim() {

    return longPrim;

  } // END getLongPrim

  public void setLongObj(Long longObj) {

    this.longObj = longObj;
    objList.add(longObj);

  } // END setLongObj

  public Long getLongObj() {

    return longObj;

  } // END getLongObj

  // -------------------------------------------------------------------------

  float floatPrim;

  Float floatObj;

  public void setFloatPrim(float floatPrim) {

    this.floatPrim = floatPrim;
    primList.add(Float.valueOf(floatPrim));

  } // END setFloatPrim

  public float getFloatPrim() {

    return floatPrim;

  } // END getFloatPrim

  public void setFloatObj(Float floatObj) {

    this.floatObj = floatObj;
    objList.add(floatObj);

  } // END setFloatObj

  public Float getFloatObj() {

    return floatObj;

  } // END getFloatObj

  // -------------------------------------------------------------------------

  double doublePrim;

  Double doubleObj;

  public void setDoublePrim(double doublePrim) {

    this.doublePrim = doublePrim;
    primList.add(Double.valueOf(doublePrim));

  } // END setDoublePrim

  public double getDoublePrim() {

    return doublePrim;

  } // END getDoublePrim

  public void setDoubleObj(Double doubleObj) {

    this.doubleObj = doubleObj;
    objList.add(doubleObj);

  } // END setDoubleObj

  public Double getDoubleObj() {

    return doubleObj;

  } // END getDoubleObj

  // -------------------------------------------------------------------------

  List primList;

  public List getPrimList() {

    return primList;

  } // END getPrimList()

  // -------------------------------------------------------------------------

  List objList;

  public List getObjList() {

    return objList;

  } // END getObjList

  // -------------------------------------------------------------------------

  private String privString;

  private void setPrivate(String privString) {

    this.privString = privString;

  } // END setPrivate

  public String getPrivate() {

    return privString;

  } // END getPrivate();

  // -------------------------------------------------------------------------

  private Object mBean;

  public void setBean(Object mBean) {

    this.mBean = mBean;

  } // END setBean

  public Object getBean() {

    return mBean;

  } // END getBean

  // -------------------------------------------------------------------------

  private Map orderedMap;

  public Map getOrderedMap() {

    if (orderedMap == null) {
      orderedMap = new LinkedHashMap();
    }

    return orderedMap;

  } // END getOrderedMap

  public void setOrderedMap(Map orderedMap) {

    this.orderedMap = orderedMap;

  } // END setOrderedMap

  // -------------------------------------------------------------------------

  private List orderedList;

  public List getOrderedList() {

    return orderedList;

  } // END getOrderedList

  public void setOrderedList(List orderedList) {

    this.orderedList = orderedList;

  } // END setOrderedList

  // -------------------------------------------------------------------------

  private int[] arrayProperty;

  public int[] getArrayProperty() {

    if (arrayProperty == null) {
      arrayProperty = new int[1];
    }
    return arrayProperty;

  } // END getArrayProperty

  public void setArrayProperty(int[] arrayProperty) {

    this.arrayProperty = arrayProperty;

  } // END setArrayProperty

  // -------------------------------------------------------------------------

}
