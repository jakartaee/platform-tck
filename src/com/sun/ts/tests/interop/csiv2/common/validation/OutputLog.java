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

package com.sun.ts.tests.interop.csiv2.common.validation;

import java.util.*;
import com.sun.ts.tests.interop.csiv2.common.parser.*;
import com.sun.ts.lib.util.*;

/**
 * Utility class to organize log messages into three categories:
 * <ul>
 * <li>Informational (logInfo)</li>
 * <li>Match (logMatch)</li>
 * <li>Mismatch (logMismatch)</li>
 * </ul>
 *
 * @author Mark Roth
 */
public class OutputLog {
  /**
   * Utility method for validation to output an informational message.
   */
  public void logInfo(String message) {
    TestUtil.logMsg(message);
  }

  /**
   * Utility method for validation to output that something matches the expected
   * result.
   */
  public void logMatch(String message) {
    TestUtil.logMsg("+ " + message);
  }

  /**
   * Utility method for validation to output that something does not match the
   * expected result.
   */
  public void logMismatch(String message) {
    TestUtil.logMsg("MISMATCH: " + message);
  }
}
