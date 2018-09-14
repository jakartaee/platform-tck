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
 * @(#)TestCode.java	1.8 03/05/16
 */

package com.sun.ts.tests.assembly.util.shared.enventry.scope;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;

public class TestCode {

  /**
   * Lookup a String env entry and compare its runtime value with a reference
   * value.
   *
   * @param name
   *          Name of the env entry to lookup.
   * @param ref
   *          Reference value for this env entry (the one in DD).
   *
   * @return true if runtime value and reference matches. False otherwise.
   */
  public static boolean checkEntry(TSNamingContext nctx, String name,
      String ref) {

    String runtimeVal;
    boolean pass;

    try {
      TestUtil.logTrace("Looking up '" + name + "'");
      runtimeVal = (String) nctx.lookup("java:comp/env/" + name);
      TestUtil.logTrace("Runtime value is '" + runtimeVal + "'");

      pass = runtimeVal.equals(ref);
      if (!pass) {
        TestUtil.logErr("Expected value was '" + ref + "'");
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e, e);
      pass = false;
    }

    return pass;
  }
}
