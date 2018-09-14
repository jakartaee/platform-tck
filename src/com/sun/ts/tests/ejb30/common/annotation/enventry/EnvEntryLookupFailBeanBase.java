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
import javax.naming.NamingException;
import static com.sun.ts.tests.ejb30.common.annotation.enventry.Constants.PREFIX;

abstract public class EnvEntryLookupFailBeanBase extends EnvEntryBeanBase
    implements EnvEntryIF {

  /////////////////////////////////////////////////////////////////////////
  // business methods
  /////////////////////////////////////////////////////////////////////////

  public void stringTest() throws TestFailedException {
    String expected = getStringExpected();
    String actual = getString();
    verify(expected, actual, "injected String env-entry");

    failEJBContextLookup(getStringName());
    failJndiLookup(getStringName());
  }

  public void charTest() throws TestFailedException {
    char expected = getCharExpected();
    char actual = getChar();
    verify(expected, actual, "injected char env-entry");

    failEJBContextLookup(getCharName());
    failJndiLookup(getCharName());
  }

  public void intTest() throws TestFailedException {
    int expected = getIntExpected();
    int actual = getInt();
    verify(expected, actual, "injected int env-entry");

    failEJBContextLookup(getIntName());
    failJndiLookup(getIntName());
  }

  public void booleanTest() throws TestFailedException {
    boolean expected = getBooleanExpected();
    boolean actual = getBoolean();
    verify(expected, actual, "injected boolean env-entry");
    failEJBContextLookup(getBooleanName());
    failJndiLookup(getBooleanName());
  }

  public void doubleTest() throws TestFailedException {
    double expected = getDoubleExpected();
    double actual = getDouble();
    verify(expected, actual, "injected double env-entry");
    failEJBContextLookup(getDoubleName());
    failJndiLookup(getDoubleName());
  }

  public void byteTest() throws TestFailedException {
    byte expected = getByteExpected();
    byte actual = getByte();
    verify(expected, actual, "injected byte env-entry");
    failEJBContextLookup(getByteName());
    failJndiLookup(getByteName());
  }

  public void shortTest() throws TestFailedException {
    short expected = getShortExpected();
    short actual = getShort();
    verify(expected, actual, "injected short env-entry");
    failEJBContextLookup(getShortName());
    failJndiLookup(getShortName());
  }

  public void longTest() throws TestFailedException {
    long expected = getLongExpected();
    long actual = getLong();
    verify(expected, actual, "injected long env-entry");
    failEJBContextLookup(getLongName());
    failJndiLookup(getLongName());
  }

  public void floatTest() throws TestFailedException {
    float expected = getFloatExpected();
    float actual = getFloat();
    verify(expected, actual, "injected float env-entry");
    failEJBContextLookup(getFloatName());
    failJndiLookup(getFloatName());
  }

  // env-entries with subcontext

  public void stringDeepTest() throws TestFailedException {
    String expected = getStringDeepExpected();
    String actual = getStringDeep();
    verify(expected, actual, "injected String env-entry with subcontexts");
    failEJBContextLookup(getStringDeepName());
    failJndiLookup(getStringDeepName());
  }

  public void charDeepTest() throws TestFailedException {
    char expected = getCharDeepExpected();
    char actual = getCharDeep();
    verify(expected, actual, "injected char env-entry with subcontexts");
    failEJBContextLookup(getCharDeepName());
    failJndiLookup(getCharDeepName());
  }

  public void intDeepTest() throws TestFailedException {
    int expected = getIntDeepExpected();
    int actual = getIntDeep();
    verify(expected, actual, "injected int env-entry with subcontexts");
    failEJBContextLookup(getIntDeepName());
    failJndiLookup(getIntDeepName());
  }

  public void booleanDeepTest() throws TestFailedException {
    boolean expected = getBooleanDeepExpected();
    boolean actual = getBooleanDeep();
    verify(expected, actual, "injected boolean env-entry with subcontexts");
    failEJBContextLookup(getBooleanDeepName());
    failJndiLookup(getBooleanDeepName());
  }

  public void doubleDeepTest() throws TestFailedException {
    double expected = getDoubleDeepExpected();
    double actual = getDoubleDeep();
    verify(expected, actual, "injected double env-entry with subcontexts");
    failEJBContextLookup(getDoubleName());
    failJndiLookup(getDoubleDeepName());
  }

  public void byteDeepTest() throws TestFailedException {
    byte expected = getByteDeepExpected();
    byte actual = getByteDeep();
    verify(expected, actual, "injected byte env-entry with subcontexts");
    failEJBContextLookup(getByteDeepName());
    failJndiLookup(getByteDeepName());
  }

  public void shortDeepTest() throws TestFailedException {
    short expected = getShortDeepExpected();
    short actual = getShortDeep();
    verify(expected, actual, "injected short env-entry with subcontexts");
    failEJBContextLookup(getShortDeepName());
    failJndiLookup(getShortDeepName());
  }

  public void longDeepTest() throws TestFailedException {
    long expected = getLongDeepExpected();
    long actual = getLongDeep();
    verify(expected, actual, "injected long env-entry with subcontexts");
    failEJBContextLookup(getLongDeepName());
    failJndiLookup(getLongDeepName());
  }

  public void floatDeepTest() throws TestFailedException {
    float expected = getFloatDeepExpected();
    float actual = getFloatDeep();
    verify(expected, actual, "injected float env-entry with subcontexts");
    failEJBContextLookup(getFloatDeepName());
    failJndiLookup(getFloatDeepName());
  }

  //////////////////////////////////////////////////////////////////////

  protected void failEJBContextLookup(String name) throws TestFailedException {
    Object actual = null;
    try {
      actual = getEJBContext().lookup(name);
    } catch (Exception e) {
      // ignore
    }
    if (actual != null) {
      throw new TestFailedException(
          "EJBContext lookup of env-entry unexpectedly succedded " + name + "="
              + actual + " in class " + this);
    }
  }

  protected void failJndiLookup(String name) throws TestFailedException {
    Object actual = null;
    try {
      actual = ServiceLocator.lookup(PREFIX + name);
    } catch (Exception e) {
      // ignore
    }
    if (actual != null) {
      throw new TestFailedException(
          "Naming Context lookup of env-entry unexpectedly succedded " + name
              + "=" + actual + " in class " + this);
    }
  }
}
