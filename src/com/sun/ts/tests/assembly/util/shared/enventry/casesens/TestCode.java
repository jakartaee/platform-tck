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

package com.sun.ts.tests.assembly.util.shared.enventry.casesens;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.util.TSNamingContext;

public class TestCode {

  /** Prefix for JNDI lookups */
  public static final String prefix = "java:comp/env/";

  /*
   * Names we use to lookup the two env. entries - differ by case
   */
  public static final String entryName1 = prefix + "aloha";

  public static final String entryName2 = prefix + "Aloha";

  /*
   * Values specified in DD for these env. entries (must be distinct)
   */
  public static final String ddValue1 = "Windsurf";

  public static final String ddValue2 = "windsurf";

  /**
   * Check that two environment entries whose names differ only by case are
   * associated with different runtime values (as specified in DD).
   */
  public static boolean testCaseSensitivity(TSNamingContext nctx) {
    /* Runtime values */
    String value1;
    String value2;

    boolean pass;

    try {
      TestUtil.logTrace("Looking up '" + entryName1 + "' ...");
      value1 = (String) nctx.lookup(entryName1);
      TestUtil.logTrace("Runtime value is '" + value1 + "'");

      TestUtil.logTrace("Looking up '" + entryName2 + "' ...");
      value2 = (String) nctx.lookup(entryName2);
      TestUtil.logTrace("Runtime value is '" + value2 + "'");

      pass = ddValue1.equals(value1) && ddValue2.equals(value2);
      if (!pass) {
        TestUtil.logErr(entryName1 + " value should be '" + ddValue1 + "' and "
            + entryName2 + " value should " + "be '" + ddValue2 + "' !");
      }
    } catch (Exception e) {
      TestUtil.logErr("caught exception: " + e, e);
      pass = false;
    }

    return pass;
  }
}
