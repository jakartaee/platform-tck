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

package com.sun.ts.tests.ejb30.bb.session.stateless.annotation.enventrynoat;

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

  private String myString = "XX";

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
    return "myString";
  }

  private char myChar = 'X';

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

  private int myInt = 99;

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

  private boolean myBoolean = false;

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

  private double myDouble = 999.99;

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

  private byte myByte = 99;

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

  private short myShort = 99;

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

  private long myLong = 99999;

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

  private float myFloat = 99999.9F;

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
  private String myStringDeep = "XXXX";

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

  private char myCharDeep = 'X';

  private void setMyCharDeep(char myCharDeep) {
    this.myCharDeep = myCharDeep;
  }

  protected char getCharDeep() {
    return myCharDeep;
  }

  protected char getCharDeepExpected() {
    return 'S';
  }

  protected String getCharDeepName() {
    return "a/a/a/myChar";
  }

  private int myIntDeep = 99;

  private void setMyIntDeep(Integer myIntDeep) {
    this.myIntDeep = myIntDeep;
  }

  protected int getIntDeep() {
    return myIntDeep;
  }

  protected int getIntDeepExpected() {
    return -2;
  }

  protected String getIntDeepName() {
    return "a/a/a/myInt";
  }

  private boolean myBooleanDeep = Boolean.TRUE;

  private void setMyBooleanDeep(Boolean myBooleanDeep) {
    this.myBooleanDeep = myBooleanDeep;
  }

  protected boolean getBooleanDeep() {
    return myBooleanDeep;
  }

  protected boolean getBooleanDeepExpected() {
    return false;
  }

  protected String getBooleanDeepName() {
    return "a/a/a/myBoolean";
  }

  private double myDoubleDeep = 9999999.99;

  private void setMyDoubleDeep(Double myDoubleDeep) {
    this.myDoubleDeep = myDoubleDeep;
  }

  protected double getDoubleDeep() {
    return myDoubleDeep;
  }

  protected double getDoubleDeepExpected() {
    return 2.22;
  }

  protected String getDoubleDeepName() {
    return "a/a/a/myDouble";
  }

  private byte myByteDeep = 99;

  private void setMyByteDeep(Byte myByteDeep) {
    this.myByteDeep = myByteDeep;
  }

  protected byte getByteDeep() {
    return myByteDeep;
  }

  protected byte getByteDeepExpected() {
    return 16;
  }

  protected String getByteDeepName() {
    return "a/a/a/myByte";
  }

  private short myShortDeep = 99;

  private void setMyShortDeep(Short myShortDeep) {
    this.myShortDeep = myShortDeep;
  }

  protected short getShortDeep() {
    return myShortDeep;
  }

  protected short getShortDeepExpected() {
    return 2;
  }

  protected String getShortDeepName() {
    return "a/a/a/myShort";
  }

  private long myLongDeep = 9999999L;

  private void setMyLongDeep(Long myLongDeep) {
    this.myLongDeep = myLongDeep;
  }

  protected long getLongDeep() {
    return myLongDeep;
  }

  protected long getLongDeepExpected() {
    return 200;
  }

  protected String getLongDeepName() {
    return "a/a/a/myLong";
  }

  private float myFloatDeep = 99999.9F;

  private void setMyFloatDeep(Float myFloatDeep) {
    this.myFloatDeep = myFloatDeep;
  }

  protected float getFloatDeep() {
    return myFloatDeep;
  }

  protected float getFloatDeepExpected() {
    return (float) 2.2;
  }

  protected String getFloatDeepName() {
    return "a/a/a/myFloat";
  }
}
