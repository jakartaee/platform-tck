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

package com.sun.ts.tests.rmiiiop.ee.marshaltests;

import java.io.*;
import java.util.*;
import com.sun.ts.lib.util.*;

public final class MyUtil {
  static boolean testPass = true;

  static int index = 0;

  static final String arrayr[] = new String[100];

  static final String arrayt[] = new String[100];

  static final int arrayf[] = new int[100];

  public static void logTrace(String s) {
    TestUtil.logTrace(s);
  }

  public static void logMsg(String s) {
    TestUtil.logMsg(s);
  }

  public static void logErr(String s) {
    TestUtil.logErr(s);
  }

  public static void logErr(String s, Throwable t) {
    TestUtil.logErr(s, t);
  }

  public static void printStackTrace(Throwable e) {
    TestUtil.printStackTrace(e);
  }

  public static void init(Properties p) throws RemoteLoggingInitException {
    TestUtil.init(p);
  }

  public static void pass(String s) {
    TestUtil.logMsg(s + " ..... PASSED");
    arrayr[index] = "PASSED";
    arrayt[index++] = s;
  }

  public static void fail(String s) {
    TestUtil.logMsg(s + " ..... FAILED");
    testPass = false;
    arrayr[index] = "FAILED";
    arrayf[index] = 0;
    arrayt[index++] = s;
  }

  public static void fail(String s, int e) {
    TestUtil.logMsg(s + " ..... FAILED with (" + e + " failures)");
    testPass = false;
    arrayr[index] = "FAILED";
    arrayf[index] = e;
    arrayt[index++] = s;
  }

  public static void logBeginSubTest(String t) {
    TestUtil.logMsg("--------------------------------------------------"
        + "------------------------------");
    TestUtil.logMsg("Begin executing subtest " + t + " .....");
  }

  public static void logDoneSubTest(String t) {
    TestUtil.logMsg("Done executing subtest " + t + " .....");
  }

  public static void displayError(String s1, String expected, String result) {
    TestUtil.logErr(s1);
    TestUtil.logErr("expected|" + expected + "|");
    TestUtil.logErr("got     |" + result + "|");
  }

  public static void displayError(String s1) {
    TestUtil.logErr(s1);
  }

  public static void logSubTestResultSummary() {
    TestUtil.logMsg("--------------------------------------------------"
        + "------------------------------");
    if (index <= 1)
      return;
    for (int i = 0; i < index; i++) {
      if (!arrayr[i].equals("FAILED")) {
        TestUtil.logMsg("" + arrayt[i] + "  . . . . .  " + arrayr[i]);
      } else {
        if (arrayf[i] > 1)
          TestUtil.logMsg("" + arrayt[i] + "  . . . . .  " + arrayr[i]
              + " with (" + arrayf[i] + " failures)");
        else
          TestUtil.logMsg("" + arrayt[i] + "  . . . . .  " + arrayr[i]);
      }
    }
    index = 0;
    TestUtil.logMsg("--------------------------------------------------"
        + "------------------------------");
  }
}
