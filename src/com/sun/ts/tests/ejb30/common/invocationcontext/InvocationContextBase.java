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
import javax.interceptor.ExcludeClassInterceptors;

public abstract class InvocationContextBase implements InvocationContextIF {
  protected InvocationContextBase() {
  }

  // ===================== business methods ===========================
  public Number[] setParametersIllegalArgumentExceptionForNumber(Number m,
      Number n) throws TestFailedException {
    return new Number[] { m, n };
  }

  public String[] setParametersIllegalArgumentExceptionForStringArray(
      String[] ss) throws TestFailedException {
    return ss;
  }

  public String[] setParametersIllegalArgumentExceptionForString(String m,
      String n) throws TestFailedException {
    return new String[] { m, n };
  }

  public char[] setParametersIllegalArgumentExceptionForChar(char m, char n)
      throws TestFailedException {
    return new char[] { m, n };
  }

  public Number[] setParametersIllegalArgumentExceptionForShortLong(short s,
      long l) throws TestFailedException {
    return new Number[] { s, l };
  }

  @ExcludeClassInterceptors()
  public String[] setParametersIllegalArgumentExceptionForString(Object o,
      Object p) throws TestFailedException {
    return null;
  }

  public boolean proceedAgain(int count) throws TestFailedException {
    if (count == 0) {
      throw new TestFailedException(
          "Throw TestFailedException if it's the 1st invocation/proceed call.");
    }
    return true;
  }

  public int getTarget() throws TestFailedException {
    return System.identityHashCode(this);
  }

  public void getSetParametersEmpty() throws TestFailedException {

  }

  public String getSetParameters(String a, String b)
      throws TestFailedException {
    return a + b;
  }

  public void getContextData() throws TestFailedException {
  }

  public void getTimer() throws TestFailedException {
  }

}
