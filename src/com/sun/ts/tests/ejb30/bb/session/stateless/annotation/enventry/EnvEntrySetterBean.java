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

package com.sun.ts.tests.ejb30.bb.session.stateless.annotation.enventry;

import com.sun.ts.tests.ejb30.common.annotation.enventry.EnvEntryBeanBase;
import com.sun.ts.tests.ejb30.common.annotation.enventry.EnvEntryIF;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.SessionContext;
import javax.annotation.Resource;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name = "EnvEntrySetterBean")
@Remote({ EnvEntryIF.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnvEntrySetterBean extends EnvEntryBeanBase implements EnvEntryIF {

  @Resource(name = "sessionContext")
  private SessionContext sessionContext;

  public EnvEntrySetterBean() {
  }

  public void remove() {
  }

  protected javax.ejb.EJBContext getEJBContext() {
    return sessionContext;
  }

  private String myString;

  @Resource
  private void setMyString(String myString) {
    this.myString = myString;
  }

  protected String getString() {
    return myString;
  }

  protected String getStringExpected() {
    return "EnvEntrySetterBean";
  }

  protected String getStringName() {
    return "com.sun.ts.tests.ejb30.bb.session.stateless.annotation.enventry.EnvEntrySetterBean/myString";
  }

  private char myChar;

  @Resource(name = "myChar")
  private void setMyChar(char myChar) {
    this.myChar = myChar;
  }

  protected char getChar() {
    return myChar;
  }

  protected char getCharExpected() {
    return 's';
  }

  protected String getCharName() {
    return "myChar";
  }

  private int myInt;

  @Resource(name = "myInt")
  private void setMyInt(int myInt) {
    this.myInt = myInt;
  }

  protected int getInt() {
    return myInt;
  }

  protected int getIntExpected() {
    return -1;
  }

  protected String getIntName() {
    return "myInt";
  }

  private boolean myBoolean;

  @Resource(name = "myBoolean")
  private void setMyBoolean(boolean myBoolean) {
    this.myBoolean = myBoolean;
  }

  protected boolean getBoolean() {
    return myBoolean;
  }

  protected boolean getBooleanExpected() {
    return true;
  }

  protected String getBooleanName() {
    return "myBoolean";
  }

  private double myDouble;

  @Resource(name = "myDouble")
  private void setMyDouble(double myDouble) {
    this.myDouble = myDouble;
  }

  protected double getDouble() {
    return myDouble;
  }

  protected double getDoubleExpected() {
    return 1.11;
  }

  protected String getDoubleName() {
    return "myDouble";
  }

  private byte myByte;

  @Resource(name = "myByte")
  private void setMyByte(byte myByte) {
    this.myByte = myByte;
  }

  protected byte getByte() {
    return myByte;
  }

  protected byte getByteExpected() {
    return 8;
  }

  protected String getByteName() {
    return "myByte";
  }

  private short myShort;

  @Resource(name = "myShort")
  private void setMyShort(short myShort) {
    this.myShort = myShort;
  }

  protected short getShort() {
    return myShort;
  }

  protected short getShortExpected() {
    return 1;
  }

  protected String getShortName() {
    return "myShort";
  }

  private long myLong;

  @Resource(name = "myLong")
  private void setMyLong(long myLong) {
    this.myLong = myLong;
  }

  protected long getLong() {
    return myLong;
  }

  protected long getLongExpected() {
    return 100;
  }

  protected String getLongName() {
    return "myLong";
  }

  private float myFloat;

  @Resource(name = "myFloat")
  private void setMyFloat(float myFloat) {
    this.myFloat = myFloat;
  }

  protected float getFloat() {
    return myFloat;
  }

  protected float getFloatExpected() {
    return (float) 1.1;
  }

  protected String getFloatName() {
    return "myFloat";
  }

  // env-entries with subcontexts
  private String myStringDeep;

  @Resource(name = "a/a/a/myString")
  private void setMyStringDeep(String myStringDeep) {
    this.myStringDeep = myStringDeep;
  }

  protected String getStringDeep() {
    return myStringDeep;
  }

  protected String getStringDeepExpected() {
    return "EnvEntrySetterBeanDeep";
  }

  protected String getStringDeepName() {
    return "a/a/a/myString";
  }

  private Character myCharDeep;

  @Resource(name = "a/a/a/myChar")
  private void setMyCharDeep(char myCharDeep) {
    this.myCharDeep = myCharDeep;
  }

  protected char getCharDeep() {
    return myCharDeep.charValue();
  }

  protected char getCharDeepExpected() {
    return 'S';
  }

  protected String getCharDeepName() {
    return "a/a/a/myChar";
  }

  private Integer myIntDeep;

  @Resource(name = "a/a/a/myInt")
  private void setMyIntDeep(int myIntDeep) {
    this.myIntDeep = myIntDeep;
  }

  protected int getIntDeep() {
    return myIntDeep.intValue();
  }

  protected int getIntDeepExpected() {
    return -2;
  }

  protected String getIntDeepName() {
    return "a/a/a/myInt";
  }

  private Boolean myBooleanDeep;

  @Resource(name = "a/a/a/myBoolean", description = "<env-entry> boolean")
  private void setMyBooleanDeep(boolean myBooleanDeep) {
    this.myBooleanDeep = myBooleanDeep;
  }

  protected boolean getBooleanDeep() {
    return myBooleanDeep.booleanValue();
  }

  protected boolean getBooleanDeepExpected() {
    return false;
  }

  protected String getBooleanDeepName() {
    return "a/a/a/myBoolean";
  }

  private Double myDoubleDeep;

  @Resource(name = "a/a/a/myDouble")
  private void setMyDoubleDeep(double myDoubleDeep) {
    this.myDoubleDeep = myDoubleDeep;
  }

  protected double getDoubleDeep() {
    return myDoubleDeep.doubleValue();
  }

  protected double getDoubleDeepExpected() {
    return 2.22;
  }

  protected String getDoubleDeepName() {
    return "a/a/a/myDouble";
  }

  private Byte myByteDeep;

  @Resource(name = "a/a/a/myByte")
  private void setMyByteDeep(byte myByteDeep) {
    this.myByteDeep = myByteDeep;
  }

  protected byte getByteDeep() {
    return myByteDeep.byteValue();
  }

  protected byte getByteDeepExpected() {
    return 16;
  }

  protected String getByteDeepName() {
    return "a/a/a/myByte";
  }

  private Short myShortDeep;

  @Resource(name = "a/a/a/myShort")
  private void setMyShortDeep(short myShortDeep) {
    this.myShortDeep = myShortDeep;
  }

  protected short getShortDeep() {
    return myShortDeep.shortValue();
  }

  protected short getShortDeepExpected() {
    return 2;
  }

  protected String getShortDeepName() {
    return "a/a/a/myShort";
  }

  private Long myLongDeep;

  @Resource(name = "a/a/a/myLong")
  private void setMyLongDeep(long myLongDeep) {
    this.myLongDeep = myLongDeep;
  }

  protected long getLongDeep() {
    return myLongDeep.longValue();
  }

  protected long getLongDeepExpected() {
    return 200;
  }

  protected String getLongDeepName() {
    return "a/a/a/myLong";
  }

  private Float myFloatDeep;

  @Resource(name = "a/a/a/myFloat")
  private void setMyFloatDeep(float myFloatDeep) {
    this.myFloatDeep = myFloatDeep;
  }

  protected float getFloatDeep() {
    return myFloatDeep.floatValue();
  }

  protected float getFloatDeepExpected() {
    return (float) 2.2;
  }

  protected String getFloatDeepName() {
    return "a/a/a/myFloat";
  }
}
