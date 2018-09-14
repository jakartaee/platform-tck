/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.websocket.ee.javax.websocket.coder;

public class Logger {
  private static final StringBuilder destroyLog = new StringBuilder();

  private static final StringBuilder initLog = new StringBuilder();

  private static final StringBuilder codeLog = new StringBuilder();

  private static final StringBuilder willCodeLog = new StringBuilder();

  public static void onDestroy(Class<?> clazz) {
    destroyLog.append(clazz.getName()).append(" ");
  }

  public static void onInit(Class<?> clazz) {
    initLog.append(clazz.getName()).append(" ");
  }

  public static void clearDestroyLog() {
    int len = destroyLog.length();
    destroyLog.delete(0, len);
  }

  public static void clearInitLog() {
    int len = initLog.length();
    initLog.delete(0, len);
  }

  public static String getInitLog() {
    return initLog.toString();
  }

  public static String getDestroyLog() {
    return initLog.toString();
  }

  public static void clearCodeLog() {
    int len = codeLog.length();
    codeLog.delete(0, len);
  }

  public static void clearWillCodeLog() {
    int len = willCodeLog.length();
    willCodeLog.delete(0, len);
  }

  public static void onCode(Class<?> clazz) {
    codeLog.append(clazz.getName()).append(" ");
  }

  public static void onWillCode(Class<?> clazz) {
    willCodeLog.append(clazz.getName()).append(" ");
  }

  public static String getCodeLog() {
    return codeLog.toString();
  }

  public static String getWillCodeLog() {
    return willCodeLog.toString();
  }

}
