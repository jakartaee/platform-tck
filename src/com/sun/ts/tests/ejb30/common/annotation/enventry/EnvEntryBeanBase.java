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

package com.sun.ts.tests.ejb30.common.annotation.enventry;

import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.ejb.EJBContext;
import javax.naming.NamingException;
import static com.sun.ts.tests.ejb30.common.annotation.enventry.Constants.PREFIX;

abstract public class EnvEntryBeanBase implements EnvEntryIF {
  abstract protected EJBContext getEJBContext();

  abstract protected String getString();

  abstract protected String getStringExpected();

  abstract protected String getStringName();

  abstract protected char getChar();

  abstract protected char getCharExpected();

  abstract protected String getCharName();

  abstract protected int getInt();

  abstract protected int getIntExpected();

  abstract protected String getIntName();

  abstract protected boolean getBoolean();

  abstract protected boolean getBooleanExpected();

  abstract protected String getBooleanName();

  abstract protected double getDouble();

  abstract protected double getDoubleExpected();

  abstract protected String getDoubleName();

  abstract protected byte getByte();

  abstract protected byte getByteExpected();

  abstract protected String getByteName();

  abstract protected short getShort();

  abstract protected short getShortExpected();

  abstract protected String getShortName();

  abstract protected long getLong();

  abstract protected long getLongExpected();

  abstract protected String getLongName();

  abstract protected float getFloat();

  abstract protected float getFloatExpected();

  abstract protected String getFloatName();

  abstract protected String getStringDeep();

  abstract protected String getStringDeepExpected();

  abstract protected String getStringDeepName();

  abstract protected char getCharDeep();

  abstract protected char getCharDeepExpected();

  abstract protected String getCharDeepName();

  abstract protected int getIntDeep();

  abstract protected int getIntDeepExpected();

  abstract protected String getIntDeepName();

  abstract protected boolean getBooleanDeep();

  abstract protected boolean getBooleanDeepExpected();

  abstract protected String getBooleanDeepName();

  abstract protected double getDoubleDeep();

  abstract protected double getDoubleDeepExpected();

  abstract protected String getDoubleDeepName();

  abstract protected byte getByteDeep();

  abstract protected byte getByteDeepExpected();

  abstract protected String getByteDeepName();

  abstract protected short getShortDeep();

  abstract protected short getShortDeepExpected();

  abstract protected String getShortDeepName();

  abstract protected long getLongDeep();

  abstract protected long getLongDeepExpected();

  abstract protected String getLongDeepName();

  abstract protected float getFloatDeep();

  abstract protected float getFloatDeepExpected();

  abstract protected String getFloatDeepName();

  /////////////////////////////////////////////////////////////////////////
  // business methods
  /////////////////////////////////////////////////////////////////////////

  public void stringTest() throws TestFailedException {
    String expected = getStringExpected();
    String actual = getString();
    verify(expected, actual, "injected String env-entry");
    actual = null;

    actual = (String) getEJBContext().lookup(getStringName());
    verify(expected, actual,
        "EJBContext lookup of String env-entry " + getStringName());
    actual = null;

    try {
      actual = (String) ServiceLocator.lookup(PREFIX + getStringName());
      verify(expected, actual,
          "Naming Context lookup of String env-entry " + getStringName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void charTest() throws TestFailedException {
    char expected = getCharExpected();
    char actual = getChar();
    verify(expected, actual, "injected char env-entry");

    Character big = (Character) getEJBContext().lookup(getCharName());
    actual = big.charValue();
    verify(expected, actual,
        "EJBContext lookup of char env-entry " + getCharName());

    try {
      big = (Character) ServiceLocator.lookup(PREFIX + getCharName());
      actual = big.charValue();
      verify(expected, actual,
          "Naming Context lookup of char env-entry " + getCharName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void intTest() throws TestFailedException {
    int expected = getIntExpected();
    int actual = getInt();
    verify(expected, actual, "injected int env-entry");

    Integer big = (Integer) getEJBContext().lookup(getIntName());
    actual = big.intValue();
    verify(expected, actual,
        "EJBContext lookup of int env-entry " + getIntName());

    try {
      big = (Integer) ServiceLocator.lookup(PREFIX + getIntName());
      actual = big.intValue();
      verify(expected, actual,
          "Naming Context lookup of int env-entry " + getIntName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void booleanTest() throws TestFailedException {
    boolean expected = getBooleanExpected();
    boolean actual = getBoolean();
    verify(expected, actual, "injected boolean env-entry");

    Boolean big = (Boolean) getEJBContext().lookup(getBooleanName());
    actual = big.booleanValue();
    verify(expected, actual,
        "EJBContext lookup of boolean env-entry " + getBooleanName());

    try {
      big = (Boolean) ServiceLocator.lookup(PREFIX + getBooleanName());
      actual = big.booleanValue();
      verify(expected, actual,
          "Naming Context lookup of boolean env-entry " + getBooleanName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void doubleTest() throws TestFailedException {
    double expected = getDoubleExpected();
    double actual = getDouble();
    verify(expected, actual, "injected double env-entry");

    Double big = (Double) getEJBContext().lookup(getDoubleName());
    actual = big.doubleValue();
    verify(expected, actual,
        "EJBContext lookup of double env-entry " + getDoubleName());

    try {
      big = (Double) ServiceLocator.lookup(PREFIX + getDoubleName());
      actual = big.doubleValue();
      verify(expected, actual,
          "Naming Context lookup of double env-entry " + getDoubleName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void byteTest() throws TestFailedException {
    byte expected = getByteExpected();
    byte actual = getByte();
    verify(expected, actual, "injected byte env-entry");

    Byte big = (Byte) getEJBContext().lookup(getByteName());
    actual = big.byteValue();
    verify(expected, actual,
        "EJBContext lookup of byte env-entry " + getByteName());

    try {
      big = (Byte) ServiceLocator.lookup(PREFIX + getByteName());
      actual = big.byteValue();
      verify(expected, actual,
          "Naming Context lookup of byte env-entry " + getByteName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void shortTest() throws TestFailedException {
    short expected = getShortExpected();
    short actual = getShort();
    verify(expected, actual, "injected short env-entry");

    Short big = (Short) getEJBContext().lookup(getShortName());
    actual = big.shortValue();
    verify(expected, actual,
        "EJBContext lookup of short env-entry " + getShortName());

    try {
      big = (Short) ServiceLocator.lookup(PREFIX + getShortName());
      actual = big.shortValue();
      verify(expected, actual,
          "Naming Context lookup of short env-entry " + getShortName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void longTest() throws TestFailedException {
    long expected = getLongExpected();
    long actual = getLong();
    verify(expected, actual, "injected long env-entry");

    Long big = (Long) getEJBContext().lookup(getLongName());
    actual = big.longValue();
    verify(expected, actual,
        "EJBContext lookup of long env-entry " + getLongName());

    try {
      big = (Long) ServiceLocator.lookup(PREFIX + getLongName());
      actual = big.longValue();
      verify(expected, actual,
          "Naming Context lookup of long env-entry " + getLongName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void floatTest() throws TestFailedException {
    float expected = getFloatExpected();
    float actual = getFloat();
    verify(expected, actual, "injected float env-entry");

    Float big = (Float) getEJBContext().lookup(getFloatName());
    actual = big.floatValue();
    verify(expected, actual,
        "EJBContext lookup of float env-entry " + getFloatName());

    try {
      big = (Float) ServiceLocator.lookup(PREFIX + getFloatName());
      actual = big.floatValue();
      verify(expected, actual,
          "Naming Context lookup of float env-entry " + getFloatName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  // env-entries with subcontext

  public void stringDeepTest() throws TestFailedException {
    String expected = getStringDeepExpected();
    String actual = getStringDeep();
    verify(expected, actual, "injected String env-entry with subcontexts");
    actual = null;

    actual = (String) getEJBContext().lookup(getStringDeepName());
    verify(expected, actual,
        "EJBContext lookup of String env-entry with subcontexts "
            + getStringDeepName());
    actual = null;

    try {
      actual = (String) ServiceLocator.lookup(PREFIX + getStringDeepName());
      verify(expected, actual,
          "Naming Context lookup of String env-entry with subcontexts "
              + getStringDeepName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void charDeepTest() throws TestFailedException {
    char expected = getCharDeepExpected();
    char actual = getCharDeep();
    verify(expected, actual, "injected char env-entry with subcontexts");

    Character big = (Character) getEJBContext().lookup(getCharDeepName());
    actual = big.charValue();
    verify(expected, actual,
        "EJBContext lookup of char env-entry with subcontexts "
            + getCharDeepName());

    try {
      big = (Character) ServiceLocator.lookup(PREFIX + getCharDeepName());
      actual = big.charValue();
      verify(expected, actual,
          "Naming Context lookup of char env-entry with subcontexts "
              + getCharDeepName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void intDeepTest() throws TestFailedException {
    int expected = getIntDeepExpected();
    int actual = getIntDeep();
    verify(expected, actual, "injected int env-entry with subcontexts");

    Integer big = (Integer) getEJBContext().lookup(getIntDeepName());
    actual = big.intValue();
    verify(expected, actual,
        "EJBContext lookup of int env-entry with subcontexts "
            + getIntDeepName());

    try {
      big = (Integer) ServiceLocator.lookup(PREFIX + getIntDeepName());
      actual = big.intValue();
      verify(expected, actual,
          "Naming Context lookup of int env-entry with subcontexts "
              + getIntDeepName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void booleanDeepTest() throws TestFailedException {
    boolean expected = getBooleanDeepExpected();
    boolean actual = getBooleanDeep();
    verify(expected, actual, "injected boolean env-entry with subcontexts");

    Boolean big = (Boolean) getEJBContext().lookup(getBooleanDeepName());
    actual = big.booleanValue();
    verify(expected, actual,
        "EJBContext lookup of boolean env-entry with subcontexts "
            + getBooleanDeepName());

    try {
      big = (Boolean) ServiceLocator.lookup(PREFIX + getBooleanDeepName());
      actual = big.booleanValue();
      verify(expected, actual,
          "Naming Context lookup of boolean env-entry with subcontexts "
              + getBooleanDeepName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void doubleDeepTest() throws TestFailedException {
    double expected = getDoubleDeepExpected();
    double actual = getDoubleDeep();
    verify(expected, actual, "injected double env-entry with subcontexts");

    Double big = (Double) getEJBContext().lookup(getDoubleDeepName());
    actual = big.doubleValue();
    verify(expected, actual,
        "EJBContext lookup of double env-entry with subcontexts "
            + getDoubleDeepName());

    try {
      big = (Double) ServiceLocator.lookup(PREFIX + getDoubleDeepName());
      actual = big.doubleValue();
      verify(expected, actual,
          "Naming Context lookup of double env-entry with subcontexts "
              + getDoubleDeepName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void byteDeepTest() throws TestFailedException {
    byte expected = getByteDeepExpected();
    byte actual = getByteDeep();
    verify(expected, actual, "injected byte env-entry with subcontexts");

    Byte big = (Byte) getEJBContext().lookup(getByteDeepName());
    actual = big.byteValue();
    verify(expected, actual,
        "EJBContext lookup of byte env-entry with subcontexts "
            + getByteDeepName());

    try {
      big = (Byte) ServiceLocator.lookup(PREFIX + getByteDeepName());
      actual = big.byteValue();
      verify(expected, actual,
          "Naming Context lookup of byte env-entry with subcontexts "
              + getByteDeepName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void shortDeepTest() throws TestFailedException {
    short expected = getShortDeepExpected();
    short actual = getShortDeep();
    verify(expected, actual, "injected short env-entry with subcontexts");

    Short big = (Short) getEJBContext().lookup(getShortDeepName());
    actual = big.shortValue();
    verify(expected, actual,
        "EJBContext lookup of short env-entry with subcontexts "
            + getShortDeepName());

    try {
      big = (Short) ServiceLocator.lookup(PREFIX + getShortDeepName());
      actual = big.shortValue();
      verify(expected, actual,
          "Naming Context lookup of short env-entry with subcontexts "
              + getShortDeepName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void longDeepTest() throws TestFailedException {
    long expected = getLongDeepExpected();
    long actual = getLongDeep();
    verify(expected, actual, "injected long env-entry with subcontexts");

    Long big = (Long) getEJBContext().lookup(getLongDeepName());
    actual = big.longValue();
    verify(expected, actual,
        "EJBContext lookup of long env-entry with subcontexts "
            + getLongDeepName());

    try {
      big = (Long) ServiceLocator.lookup(PREFIX + getLongDeepName());
      actual = big.longValue();
      verify(expected, actual,
          "Naming Context lookup of long env-entry with subcontexts "
              + getLongDeepName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }

  public void floatDeepTest() throws TestFailedException {
    float expected = getFloatDeepExpected();
    float actual = getFloatDeep();
    verify(expected, actual, "injected float env-entry with subcontexts");

    Float big = (Float) getEJBContext().lookup(getFloatDeepName());
    actual = big.floatValue();
    verify(expected, actual,
        "EJBContext lookup of float env-entry with subcontexts "
            + getFloatDeepName());

    try {
      big = (Float) ServiceLocator.lookup(PREFIX + getFloatDeepName());
      actual = big.floatValue();
      verify(expected, actual,
          "Naming Context lookup of float env-entry with subcontexts "
              + getFloatDeepName());
    } catch (NamingException e) {
      throw new TestFailedException(e);
    }
  }
  //////////////////////////////////////////////////////////////////////////

  protected void verify(String expected, String actual, String description)
      throws TestFailedException {
    if (!expected.equals(actual)) {
      if (description == null) {
        description = "Obtaiing env-entry of type java.lang.String ";
      }
      throw new TestFailedException(description + ": expected " + expected
          + ", actual " + actual + ". " + this);
    }
  }

  protected void verify(char expected, char actual, String description)
      throws TestFailedException {
    if (expected != actual) {
      if (description == null) {
        description = "Obtaining env-entry of type char ";
      }
      throw new TestFailedException(description + ": expected " + expected
          + ", actual " + actual + ". " + this);
    }
  }

  protected void verify(int expected, int actual, String description)
      throws TestFailedException {
    if (expected != actual) {
      if (description == null) {
        description = "Obtaining env-entry of type int ";
      }
      throw new TestFailedException(description + ": expected " + expected
          + ", actual " + actual + ". " + this);
    }
  }

  protected void verify(boolean expected, boolean actual, String description)
      throws TestFailedException {
    if (expected != actual) {
      if (description == null) {
        description = "Obtaining env-entry of type boolean ";
      }
      throw new TestFailedException(description + ": expected " + expected
          + ", actual " + actual + ". " + this);
    }
  }

  protected void verify(double expected, double actual, String description)
      throws TestFailedException {
    if (expected != actual) {
      if (description == null) {
        description = "Obtaining env-entry of type double ";
      }
      throw new TestFailedException(description + ": expected " + expected
          + ", actual " + actual + ". " + this);
    }
  }

  protected void verify(byte expected, byte actual, String description)
      throws TestFailedException {
    if (expected != actual) {
      if (description == null) {
        description = "Obtaining env-entry of type byte ";
      }
      throw new TestFailedException(description + ": expected " + expected
          + ", actual " + actual + ". " + this);
    }
  }

  protected void verify(short expected, short actual, String description)
      throws TestFailedException {
    if (expected != actual) {
      if (description == null) {
        description = "Obtaining env-entry of type short ";
      }
      throw new TestFailedException(description + ": expected " + expected
          + ", actual " + actual + ". " + this);
    }
  }

  protected void verify(long expected, long actual, String description)
      throws TestFailedException {
    if (expected != actual) {
      if (description == null) {
        description = "Obtaining env-entry of type long ";
      }
      throw new TestFailedException(description + ": expected " + expected
          + ", actual " + actual + ". " + this);
    }
  }

  protected void verify(float expected, float actual, String description)
      throws TestFailedException {
    if (expected != actual) {
      if (description == null) {
        description = "Obtaining env-entry of type float ";
      }
      throw new TestFailedException(description + ": expected " + expected
          + ", actual " + actual + ". " + this);
    }
  }

}
