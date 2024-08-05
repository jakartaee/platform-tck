/*
 * Copyright (c) 1998, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.lib.util.sec.security.action;

/**
 * A convenience class for retrieving the string value of a system property as a
 * privileged action.
 *
 * <p>
 * An instance of this class can be used as the argument of
 * <code>AccessController.doPrivileged</code>.
 *
 * <p>
 * The following code retrieves the value of the system property named
 * <code>"prop"</code> as a privileged action:
 * <p>
 *
 * <pre>
 * String s = java.security.AccessController
 *     .doPrivileged(new GetPropertyAction("prop"));
 * </pre>
 *
 * @author Roland Schemers
 * @see java.security.PrivilegedAction
 * @see java.security.AccessController
 * @since 1.2
 */

public class GetPropertyAction
    implements java.security.PrivilegedAction<String> {
  private String theProp;

  private String defaultVal;

  /**
   * Constructor that takes the name of the system property whose string value
   * needs to be determined.
   *
   * @param theProp
   *          the name of the system property.
   */
  public GetPropertyAction(String theProp) {
    this.theProp = theProp;
  }

  /**
   * Constructor that takes the name of the system property and the default
   * value of that property.
   *
   * @param theProp
   *          the name of the system property.
   * @param defaulVal
   *          the default value.
   */
  public GetPropertyAction(String theProp, String defaultVal) {
    this.theProp = theProp;
    this.defaultVal = defaultVal;
  }

  /**
   * Determines the string value of the system property whose name was specified
   * in the constructor.
   *
   * @return the string value of the system property, or the default value if
   *         there is no property with that key.
   */
  public String run() {
    String value = System.getProperty(theProp);
    return (value == null) ? defaultVal : value;
  }
}
