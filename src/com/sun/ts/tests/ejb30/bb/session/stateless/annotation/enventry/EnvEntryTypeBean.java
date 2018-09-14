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
import javax.annotation.Resources;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

@Stateless(name = "EnvEntryTypeBean")
@Remote({ EnvEntryIF.class })
@TransactionManagement(TransactionManagementType.CONTAINER)
@Resources({
    @Resource(name = "myString", type = String.class, description = "<env-entry> string"),
    @Resource(name = "myChar", type = char.class, description = "<env-entry> Character"),
    @Resource(name = "myInt", type = int.class, description = "<env-entry> Integer"),
    @Resource(name = "myBoolean", type = boolean.class, description = "<env-entry> Boolean"),
    @Resource(name = "myDouble", type = double.class, description = "<env-entry> Double"),
    @Resource(name = "myByte", type = byte.class, description = "<env-entry> Byte"),
    @Resource(name = "myShort", type = short.class, description = "<env-entry> Short"),
    @Resource(name = "myLong", type = long.class, description = "<env-entry> Long"),
    @Resource(name = "myFloat", type = float.class, description = "<env-entry> Float"),

    @Resource(name = "a/a/a/myString", type = String.class, description = "<env-entry> string"),
    @Resource(name = "a/a/a/myChar", type = char.class, description = "<env-entry> Character"),
    @Resource(name = "a/a/a/myInt", type = int.class, description = "<env-entry> Integer"),
    @Resource(name = "a/a/a/myBoolean", type = boolean.class, description = "<env-entry> Boolean"),
    @Resource(name = "a/a/a/myDouble", type = double.class, description = "<env-entry> Double"),
    @Resource(name = "a/a/a/myByte", type = byte.class, description = "<env-entry> Byte"),
    @Resource(name = "a/a/a/myShort", type = short.class, description = "<env-entry> Short"),
    @Resource(name = "a/a/a/myLong", type = long.class, description = "<env-entry> Long"),
    @Resource(name = "a/a/a/myFloat", type = float.class, description = "<env-entry> Float") })
public class EnvEntryTypeBean extends EnvEntryBeanBase implements EnvEntryIF {

  @Resource(name = "sessionContext")
  private SessionContext sessionContext;

  public EnvEntryTypeBean() {
  }

  public void remove() {
  }

  protected javax.ejb.EJBContext getEJBContext() {
    return sessionContext;
  }

  protected String getString() {
    return (String) getEJBContext().lookup(getStringName());
  }

  protected String getStringExpected() {
    return "EnvEntryTypeBean";
  }

  protected String getStringName() {
    return "myString";
  }

  protected char getChar() {
    Character big = (Character) getEJBContext().lookup(getCharName());
    return big.charValue();
  }

  protected char getCharExpected() {
    return 't';
  }

  protected String getCharName() {
    return "myChar";
  }

  protected int getInt() {
    Integer big = (Integer) getEJBContext().lookup(getIntName());
    return big.intValue();
  }

  protected int getIntExpected() {
    return -1;
  }

  protected String getIntName() {
    return "myInt";
  }

  protected boolean getBoolean() {
    Boolean big = (Boolean) getEJBContext().lookup(getBooleanName());
    return big.booleanValue();
  }

  protected boolean getBooleanExpected() {
    return true;
  }

  protected String getBooleanName() {
    return "myBoolean";
  }

  protected double getDouble() {
    Double big = (Double) getEJBContext().lookup(getDoubleName());
    return big.doubleValue();
  }

  protected double getDoubleExpected() {
    return 1.11;
  }

  protected String getDoubleName() {
    return "myDouble";
  }

  protected byte getByte() {
    Byte big = (Byte) getEJBContext().lookup(getByteName());
    return big.byteValue();
  }

  protected byte getByteExpected() {
    return 8;
  }

  protected String getByteName() {
    return "myByte";
  }

  protected short getShort() {
    Short big = (Short) getEJBContext().lookup(getShortName());
    return big.shortValue();
  }

  protected short getShortExpected() {
    return 1;
  }

  protected String getShortName() {
    return "myShort";
  }

  protected long getLong() {
    Long big = (Long) getEJBContext().lookup(getLongName());
    return big.longValue();
  }

  protected long getLongExpected() {
    return 100;
  }

  protected String getLongName() {
    return "myLong";
  }

  protected float getFloat() {
    Float big = (Float) getEJBContext().lookup(getFloatName());
    return big.floatValue();
  }

  protected float getFloatExpected() {
    return (float) 1.1;
  }

  protected String getFloatName() {
    return "myFloat";
  }

  // env-entries with subcontexts
  protected String getStringDeep() {
    String s = (String) getEJBContext().lookup(getStringDeepName());
    return s;
  }

  // protected String getStringDeepExpected() {return "EnvEntryTypeBeanDeep";}
  protected String getStringDeepExpected() {
    return " ";
  }

  protected String getStringDeepName() {
    return "a/a/a/myString";
  }

  protected char getCharDeep() {
    Character big = (Character) getEJBContext().lookup(getCharDeepName());
    return big.charValue();
  }

  protected char getCharDeepExpected() {
    return ' ';
  }

  protected String getCharDeepName() {
    return "a/a/a/myChar";
  }

  protected int getIntDeep() {
    Integer big = (Integer) getEJBContext().lookup(getIntDeepName());
    return big.intValue();
  }

  protected int getIntDeepExpected() {
    return -2;
  }

  protected String getIntDeepName() {
    return "a/a/a/myInt";
  }

  protected boolean getBooleanDeep() {
    Boolean big = (Boolean) getEJBContext().lookup(getBooleanDeepName());
    return big.booleanValue();
  }

  protected boolean getBooleanDeepExpected() {
    return false;
  }

  protected String getBooleanDeepName() {
    return "a/a/a/myBoolean";
  }

  protected double getDoubleDeep() {
    Double big = (Double) getEJBContext().lookup(getDoubleDeepName());
    return big.doubleValue();
  }

  protected double getDoubleDeepExpected() {
    return 2.22;
  }

  protected String getDoubleDeepName() {
    return "a/a/a/myDouble";
  }

  protected byte getByteDeep() {
    Byte big = (Byte) getEJBContext().lookup(getByteDeepName());
    return big.byteValue();
  }

  protected byte getByteDeepExpected() {
    return 16;
  }

  protected String getByteDeepName() {
    return "a/a/a/myByte";
  }

  protected short getShortDeep() {
    Short big = (Short) getEJBContext().lookup(getShortDeepName());
    return big.shortValue();
  }

  protected short getShortDeepExpected() {
    return 2;
  }

  protected String getShortDeepName() {
    return "a/a/a/myShort";
  }

  protected long getLongDeep() {
    Long big = (Long) getEJBContext().lookup(getLongDeepName());
    return big.longValue();
  }

  protected long getLongDeepExpected() {
    return 200;
  }

  protected String getLongDeepName() {
    return "a/a/a/myLong";
  }

  protected float getFloatDeep() {
    Float big = (Float) getEJBContext().lookup(getFloatDeepName());
    return big.floatValue();
  }

  protected float getFloatDeepExpected() {
    return (float) 2.2;
  }

  protected String getFloatDeepName() {
    return "a/a/a/myFloat";
  }
}
