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

package com.sun.ts.tests.ejb30.common.invocationcontext;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

public interface InvocationContextIF {
  public Number[] setParametersIllegalArgumentExceptionForNumber(Number m,
      Number n) throws TestFailedException;

  public char[] setParametersIllegalArgumentExceptionForChar(char m, char n)
      throws TestFailedException;

  public String[] setParametersIllegalArgumentExceptionForString(String m,
      String n) throws TestFailedException;

  // This overloaded method is for confusion purpose, not used by tests.
  // After resetting params from (String, String) to (null, null),
  // setParametersIllegalArgumentExceptionForString(String, String) must still
  // be called, not
  // setParametersIllegalArgumentExceptionForString(Object, Object)
  public String[] setParametersIllegalArgumentExceptionForString(Object o,
      Object p) throws TestFailedException;

  public String[] setParametersIllegalArgumentExceptionForStringArray(
      String[] ss) throws TestFailedException;

  public Number[] setParametersIllegalArgumentExceptionForShortLong(short s,
      long l) throws TestFailedException;

  public boolean proceedAgain(int count) throws TestFailedException;

  public int getTarget() throws TestFailedException;

  public void getSetParametersEmpty() throws TestFailedException;

  public String getSetParameters(String a, String b) throws TestFailedException;

  public void getContextData() throws TestFailedException;

  public void getTimer() throws TestFailedException;
}
