/*
 * Copyright (c) 2000, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.lib.util.sec.security.util;

/**
 */
public class ResourcesMgr {

  // intended for java.security, javax.security and sun.security resources
  private static java.util.ResourceBundle bundle;

  // intended for com.sun.security resources
  private static java.util.ResourceBundle altBundle;

  public static String getString(String s) {

    if (bundle == null) {

      // only load if/when needed
      bundle = java.security.AccessController.doPrivileged(
          new java.security.PrivilegedAction<java.util.ResourceBundle>() {
            public java.util.ResourceBundle run() {
              return (java.util.ResourceBundle
                  .getBundle("sun.security.util.Resources"));
            }
          });
    }

    return bundle.getString(s);
  }

  public static String getString(String s, final String altBundleName) {

    if (altBundle == null) {

      // only load if/when needed
      altBundle = java.security.AccessController.doPrivileged(
          new java.security.PrivilegedAction<java.util.ResourceBundle>() {
            public java.util.ResourceBundle run() {
              return (java.util.ResourceBundle.getBundle(altBundleName));
            }
          });
    }

    return altBundle.getString(s);
  }
}
