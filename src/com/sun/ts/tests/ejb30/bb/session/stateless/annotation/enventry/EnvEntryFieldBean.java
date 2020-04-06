/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
import jakarta.ejb.Remote;
import jakarta.ejb.Stateless;
import jakarta.ejb.SessionContext;
import javax.annotation.Resource;
import jakarta.ejb.TransactionManagement;
import jakarta.ejb.TransactionManagementType;

@Stateless(name = "EnvEntryFieldBean")
@Remote({ EnvEntryIF.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
public class EnvEntryFieldBean extends EnvEntryBeanBase implements EnvEntryIF {

  @Resource(name = "sessionContext")
  private SessionContext sessionContext;

  public EnvEntryFieldBean() {
  }

  public void remove() {
  }

  protected jakarta.ejb.EJBContext getEJBContext() {
    return sessionContext;
  }

  @Resource
  private String myString;

  protected String getString() {
    return myString;
  }

  protected String getStringExpected() {
    return "${myString} @myString@ %myString% ${java.home}";
  }

  protected String getStringName() {
    return "com.sun.ts.tests.ejb30.bb.session.stateless.annotation.enventry.EnvEntryFieldBean/myString";
  }

  @Resource(name = "myChar")
  private char myChar;

  protected char getChar() {
    return myChar;
  }

  protected char getCharExpected() {
    return 'f';
  }

  protected String getCharName() {
    return "myChar";
  }

  @Resource(name = "myInt", description = "<env-entry> integer")
  private int myInt;

  protected int getInt() {
    return myInt;
  }

  protected int getIntExpected() {
    return -1;
  }

  protected String getIntName() {
    return "myInt";
  }

  @Resource(name = "myBoolean")
  private boolean myBoolean;

  protected boolean getBoolean() {
    return myBoolean;
  }

  protected boolean getBooleanExpected() {
    return true;
  }

  protected String getBooleanName() {
    return "myBoolean";
  }

  @Resource(name = "myDouble")
  private double myDouble;

  protected double getDouble() {
    return myDouble;
  }

  protected double getDoubleExpected() {
    return 1.11;
  }

  protected String getDoubleName() {
    return "myDouble";
  }

  @Resource(name = "myByte")
  private byte myByte;

  protected byte getByte() {
    return myByte;
  }

  protected byte getByteExpected() {
    return 8;
  }

  protected String getByteName() {
    return "myByte";
  }

  @Resource(name = "myShort")
  private short myShort;

  protected short getShort() {
    return myShort;
  }

  protected short getShortExpected() {
    return 1;
  }

  protected String getShortName() {
    return "myShort";
  }

  @Resource(name = "myLong")
  private long myLong;

  protected long getLong() {
    return myLong;
  }

  protected long getLongExpected() {
    return 100;
  }

  protected String getLongName() {
    return "myLong";
  }

  @Resource(name = "myFloat")
  private float myFloat;

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
  @Resource(name = "a/a/a/myString")
  private String myStringDeep;

  protected String getStringDeep() {
    return myStringDeep;
  }

  protected String getStringDeepExpected() {
    return "EnvEntryFieldBeanDeep";
  }

  protected String getStringDeepName() {
    return "a/a/a/myString";
  }

  @Resource(name = "a/a/a/myChar")
  private char myCharDeep;

  protected char getCharDeep() {
    return myCharDeep;
  }

  protected char getCharDeepExpected() {
    return 'F';
  }

  protected String getCharDeepName() {
    return "a/a/a/myChar";
  }

  @Resource(name = "a/a/a/myInt")
  private int myIntDeep;

  protected int getIntDeep() {
    return myIntDeep;
  }

  protected int getIntDeepExpected() {
    return -2;
  }

  protected String getIntDeepName() {
    return "a/a/a/myInt";
  }

  @Resource(name = "a/a/a/myBoolean")
  private boolean myBooleanDeep;

  protected boolean getBooleanDeep() {
    return myBooleanDeep;
  }

  protected boolean getBooleanDeepExpected() {
    return false;
  }

  protected String getBooleanDeepName() {
    return "a/a/a/myBoolean";
  }

  @Resource(name = "a/a/a/myDouble")
  private double myDoubleDeep;

  protected double getDoubleDeep() {
    return myDoubleDeep;
  }

  protected double getDoubleDeepExpected() {
    return 2.22;
  }

  protected String getDoubleDeepName() {
    return "a/a/a/myDouble";
  }

  @Resource(name = "a/a/a/myByte")
  private byte myByteDeep;

  protected byte getByteDeep() {
    return myByteDeep;
  }

  protected byte getByteDeepExpected() {
    return 16;
  }

  protected String getByteDeepName() {
    return "a/a/a/myByte";
  }

  @Resource(name = "a/a/a/myShort")
  private short myShortDeep;

  protected short getShortDeep() {
    return myShortDeep;
  }

  protected short getShortDeepExpected() {
    return 2;
  }

  protected String getShortDeepName() {
    return "a/a/a/myShort";
  }

  @Resource(name = "a/a/a/myLong")
  private long myLongDeep;

  protected long getLongDeep() {
    return myLongDeep;
  }

  protected long getLongDeepExpected() {
    return 200;
  }

  protected String getLongDeepName() {
    return "a/a/a/myLong";
  }

  @Resource(name = "a/a/a/myFloat")
  private float myFloatDeep;

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
