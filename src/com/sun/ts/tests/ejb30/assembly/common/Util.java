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

package com.sun.ts.tests.ejb30.assembly.common;

import com.sun.ts.tests.ejb30.common.helper.TLogger;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import java.net.URL;

public class Util {

  private Util() {
  }

  public static void verifyGetResource(Class cls, String name, URL expected)
      throws TestFailedException {
    URL url = cls.getResource(name);
    verifyResource(cls, name, expected, url);
  }

  public static void verifyResource(Object target, String name, URL expected,
      URL url) throws TestFailedException {
    String pass = "Got expected result " + expected + " from getResource("
        + name + ") on target " + target;
    String fail = "Expecting " + expected + " from getResource(" + name
        + "), but actual " + url + ".  The target object is " + target;
    if (expected == null) {
      if (url == null) {
        TLogger.log(pass);
      } else {
        throw new TestFailedException(fail);
      }
    } else {
      if (expected.equals(url)) {
        TLogger.log(pass);
      } else {
        throw new TestFailedException(fail);
      }
    }
  }

}
