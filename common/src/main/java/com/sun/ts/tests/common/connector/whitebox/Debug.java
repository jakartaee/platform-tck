/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.connector.whitebox;

public class Debug {

  private static String debuggingProp = null;

  private static boolean bDebug = true; // debugging on by default

  private static int iDebugLevel = 0; // no debugging by default

  /**
   * Constructor
   */
  public Debug(String str) {
    debuggingProp = System.getProperty(str);
    if ((debuggingProp != null) && (debuggingProp.equals("ON"))) {
      setDebugStatus(true); // turn tracing on
      trace("Debugging enabled");
    } else if ((debuggingProp != null) && (debuggingProp.equals("OFF"))) {
      trace("Debugging messages being disabled");
      setDebugStatus(false);
    }
  }

  public static void trace(String str) {
    if (getDebugStatus()) {
      // debugging is enabled so print info out
      System.out.println(str);
    }
  }

  /**
   * This is used to turn debugging off or on. This is off by default and must
   * be explicitly set to true in order to turn on debugging.
   *
   * @exception none.
   * @param boolean:
   *          True means turn debugging on.
   * @return none.
   */
  public static void setDebugStatus(boolean bVal) {
    bDebug = bVal;
  }

  /*
   * This is a convenience method used to test if we are running in debug mode.
   * If so, we will print the strack trace. If not, we do nothing.
   *
   * @exception none.
   * 
   * @param none
   * 
   * @version 1.32 07/31/00
   * 
   * @return void
   */
  public static void printDebugStack(Exception ex) {
    if (getDebugStatus() == true) {
      ex.printStackTrace();
    }
  }

  /**
   * This gets the status of the debugging functionality. false means that
   * debugging is disabled, true means it is enabled.
   *
   * @exception none.
   * @param none.
   * @return boolean: True means turn debugging on.
   */
  public static boolean getDebugStatus() {
    return bDebug;
  }

  /**
   * This gets the current level of debugging we are using.
   *
   * @exception none.
   * @param none.
   * @return int: 0=none, 1=errors, 2=errors+warnings, 3=all
   */

  public static int getDebugLevel() {
    return iDebugLevel;
  }

  /**
   * This sets the current level of debugging we are using.
   *
   * @exception none.
   * @param int:
   *          0=none, 1=errors, 2=errors+warnings, 3=all
   * @return none.
   */
  public static void setDebugLevel(int val) {
    iDebugLevel = val;
  }

}
