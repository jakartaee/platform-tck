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

package com.sun.ts.lib.util;

/**
 * This class acts as an adapter between the logging API used in the API check
 * utility and the logginf API available in the CTS harness. The API check
 * utility uses a PrintWriter to log messages. The parts of the PrintWriter API
 * used by the API check utility will be translated to TestUtil calls in this
 * class. The SigLogIntf will capture the parts of the PrintWriter API that need
 * to be reimplemented so API check can fit into the CTS test framework.
 */
public class SigLogAdapter implements SigLogIntf {

  private static final String NL = System.getProperty("line.separator", "\n");

  private boolean dumpMessagesToStdErr;

  public SigLogAdapter() {
    String useStdErr = System.getProperty("dump.api.check.stderr", "false");
    if (useStdErr.equalsIgnoreCase("true")) {
      dumpMessagesToStdErr = true;
    }
  }

  public void println(String msg) {
    print(msg + NL);
  }

  public void println(Object obj) {
    print(obj.toString() + NL);
  }

  public void println(char c) {
    print(c);
    println();
  }

  public void println() {
    print(NL);
  }

  public void print(String msg) {
    if (dumpMessagesToStdErr) {
      System.err.println(msg);
    } else {
      TestUtil.logMsg(msg);
    }
  }

  public void print(Object obj) {
    print(obj.toString());
  }

  public void print(char c) {
    char[] chars = new char[] { c };
    print(new String(chars));
  }

  public void flush() {
    // do nothing unless there is an equivalent call in TestUtil
    // to flush the output stream
  }

  public void close() {
    // do nothing unless there is an equivalent call in TestUtil
    // to close the output stream
  }

}
