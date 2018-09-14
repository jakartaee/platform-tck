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

/*
 * @(#)PropertyUtils.java	1.2 03/05/16
 */

package com.sun.ts.tests.jaxrpc.wsi.utils;

import java.util.Properties;

import com.sun.ts.lib.harness.EETest;

/**
 */
public class PropertyUtils {
  /**
   * Returns the property with the specified name. If the property does not
   * exist, an EETest.Fault exception is thrown.
   * 
   * @param properties
   *          the properties.
   * @param key
   *          the key.
   * 
   * @return the value.
   * 
   * @throws EETest.Fault
   */
  public static String getProperty(Properties properties, String key)
      throws EETest.Fault {
    String value = properties.getProperty(key);
    if (value != null) {
      return value;
    }
    throw new EETest.Fault("Required property '" + key + "' not present.");
  }

  /**
   * Returns the property, with the specified name, as an integer. If the
   * property does not exist or cannot be converted to an integer, an
   * EETest.Fault exception is thrown.
   * 
   * @param properties
   *          the properties.
   * @param key
   *          the key.
   * 
   * @return the value.
   * 
   * @throws EETest.Fault
   */
  public static int getIntegerProperty(Properties properties, String key)
      throws EETest.Fault {
    String value = getProperty(properties, key);
    try {
      int i = Integer.parseInt(value);
      return i;
    } catch (NumberFormatException e) {
      throw new EETest.Fault("Property '" + key + "' value '" + value
          + "' is not a valid integer.", e);
    }
  }

  /**
   * Returns the property, with the specified name, as a boolean. If the
   * property does not exist or cannot be converted to a boolean, an
   * EETest.Fault exception is thrown.
   * 
   * @param properties
   *          the properties.
   * @param key
   *          the key.
   * 
   * @return the value.
   * 
   * @throws EETest.Fault
   */
  public static boolean getBooleanProperty(Properties properties, String key)
      throws EETest.Fault {
    String value = getProperty(properties, key);
    if (value.equalsIgnoreCase("true")) {
      return true;
    }
    if (value.equalsIgnoreCase("false")) {
      return false;
    }
    throw new EETest.Fault(
        "Property '" + key + "' value '" + value + "' is not a valid boolean.");
  }

  /**
   * Private to prevent instantiation.
   */
  private PropertyUtils() {
    super();
  }
}
