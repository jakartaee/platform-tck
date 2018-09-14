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

package com.sun.ts.tests.rmiiiop.ee.standalone;

import java.io.*;
import java.util.*;

public final class PrintUtil {
  static boolean testPass = true;

  static int index = 0;

  static final String arrayr[] = new String[500];

  static final String arrayt[] = new String[500];

  static final int arrayf[] = new int[500];

  public static void logMsg(String s) {
    System.out.println(s);
  }

  public static void logErr(String s) {
    System.err.println("ERROR: " + s);
  }

  public static void logTrace(String s) {
    System.err.println("Trace: " + s);
  }

  public static void pass(String s) {
    logMsg(s + " ..... PASSED");
    arrayr[index] = "PASSED";
    arrayt[index++] = s;
  }

  public static void fail(String s) {
    logMsg(s + " ..... FAILED");
    testPass = false;
    arrayr[index] = "FAILED";
    arrayf[index] = 0;
    arrayt[index++] = s;
  }

  public static void fail(String s, int e) {
    logMsg(s + " ..... FAILED with (" + e + " failures)");
    testPass = false;
    arrayr[index] = "FAILED";
    arrayf[index] = e;
    arrayt[index++] = s;
  }

  public static void logBeginSubTest(String t) {
    logMsg("--------------------------------------------------"
        + "------------------------------");
    logMsg("Begin executing subtest " + t + " .....");
  }

  public static void logDoneSubTest(String t) {
    logMsg("Done executing subtest " + t + " .....");
  }

  public static void displayError(String s1, String expected, String result) {
    logErr(s1);
    logErr("expected|" + expected + "|");
    logErr("got     |" + result + "|");
  }

  public static void displayError(String s1) {
    logErr(s1);
  }

  public static void logSubTestResultSummary() {
    logMsg("--------------------------------------------------"
        + "------------------------------");
    if (index <= 1)
      return;
    for (int i = 0; i < index; i++) {
      if (!arrayr[i].equals("FAILED")) {
        logMsg("" + arrayt[i] + "  . . . . .  " + arrayr[i]);
      } else {
        if (arrayf[i] > 1)
          logMsg("" + arrayt[i] + "  . . . . .  " + arrayr[i] + " with ("
              + arrayf[i] + " failures)");
        else
          logMsg("" + arrayt[i] + "  . . . . .  " + arrayr[i]);
      }
    }
    index = 0;
    logMsg("--------------------------------------------------"
        + "------------------------------");
  }

  public static void printStackTrace(Throwable e) {
    if (e == null)
      return;
    logMsg("--- Begin Print Stack Trace ---");
    try {
      StringWriter sw = new StringWriter();
      PrintWriter writer = new PrintWriter(sw);
      e.printStackTrace(writer);
      BufferedReader reader = new BufferedReader(
          new StringReader(sw.toString()));
      String line = null;
      while ((line = reader.readLine()) != null) {
        logErr(line);
      }
      reader.close();
      writer.close();
    } catch (Exception E) {
    }
    logMsg("--- End Print Stack Trace ---");
  }
}
