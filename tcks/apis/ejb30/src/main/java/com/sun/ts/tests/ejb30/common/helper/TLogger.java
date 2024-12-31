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

package com.sun.ts.tests.ejb30.common.helper;

/**
 * A convenience class to use different logging, e.g., cts TestUtil.log, or
 * System.out.println.
 */
public class TLogger {
  public static final String NL = System.getProperty("line.separator");

  private TLogger() {
  }

  public static void log(String... args) {
    String msg = null;
    if (args.length == 0) {
      return;
    } else if (args.length == 1) {
      msg = args[0];
      if (msg == null) {
        return;
      }
    } else {
      StringBuffer sb = new StringBuffer();
      for (String s : args) {
        sb.append(s).append(' ');
      }
      msg = sb.toString();
    }
    System.out.println(msg);
  }

  public static void log(String arg, Throwable thr) {
    if (arg != null) {
      System.out.println(arg);
    }
    if (thr != null) {
      thr.printStackTrace();
    }
  }

  public static void log(boolean status, String arg) {
    if (arg != null) {
      String s = status ? "PASSED: " : "FAILED: ";
      System.out.println(s + arg);
    }
  }

  public static void logMsg(String s) {
    log(s);
  }

  public static void logErr(String s) {
    log(s);
  }

  public static void logTrace(String s) {
    log(s);
  }

  public static void printStackTrace(Throwable th) {
    th.printStackTrace();
  }

  public static void main(String[] args) {
    TLogger.log("####");
  }
}
