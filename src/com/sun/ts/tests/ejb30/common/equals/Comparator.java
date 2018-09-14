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

package com.sun.ts.tests.ejb30.common.equals;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;

public class Comparator {

  private Comparator() {
  }

  public static void compare(Object one, Object two, boolean expected,
      boolean logIfOk) throws TestFailedException {
    if (one.equals(two) == expected) {
      if (logIfOk) {
        TLogger.log("Comparing 2 beans returned " + expected
            + ", as expected: bean1 (" + one + "), and bean2 (" + two + ").");
      }
    } else {
      throw new TestFailedException("Expecting " + expected
          + " from comparing 2 beans, " + "but got otherwise: bean1 (" + one
          + "), and bean2 (" + two + ").");
    }
  }

  public static void compare(Object one, Object two, Object three,
      boolean expected, boolean logIfOk) throws TestFailedException {
    boolean actual = one.equals(two) && two.equals(three);
    if (actual == expected) {
      if (logIfOk) {
        TLogger.log("Comparing 3 beans returned " + expected
            + ", as expected: bean1 (" + one + "), and bean2 (" + two
            + "), and bean3 (" + three + ").");
      }
    } else {
      throw new TestFailedException("Expecting " + expected
          + " from comparing 3 beans, " + "but got otherwise: bean1 (" + one
          + "), and bean2 (" + two + "), and bean3 (" + three + ").");
    }
  }

}
