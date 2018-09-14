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

import java.util.*;
import java.io.*;

public class ConfigUtil {
  public final static File PROPS_DIR = new File(
      System.getProperty("TS_HOME", System.getProperty("ts.home"))
          + File.separator + "src" + File.separator + "com" + File.separator
          + "sun" + File.separator + "ts" + File.separator + "lib",
      "harness");

  public final static File SRC_DIR = new File(
      System.getProperty("TS_HOME", System.getProperty("ts.home")), "src");

  public final static String INTEROP_DIRECTION_PROP_FILE_NAME = "interop-direction.properties";

  public static String[] getMappingValue(Properties mapping, String[] keys,
      String relativePath) {
    String forwardSlashRelativePath = relativePath.replace(File.separator, "/");

    for (int i = keys.length - 1; i >= 0; i--) { // must traverse in reverse
                                                 // order.
      if (forwardSlashRelativePath.startsWith(keys[i])) {
        return stringToArray(mapping.getProperty(keys[i]));
      }
    }
    return TestUtil.EMPTY_STRING_ARRAY;
  }

  public static String[] loadKeysFrom(Properties mapping) {
    String[] keys = null;
    if (mapping != null) {
      keys = new String[mapping.size()];
      int i = 0;
      for (Enumeration enum1 = mapping.keys(); enum1.hasMoreElements(); i++) {
        keys[i] = (String) enum1.nextElement();
      }
      Arrays.sort(keys);
    }
    return keys;
  }

  public static String[] stringToArray(String s) {
    if (s == null) {
      return TestUtil.EMPTY_STRING_ARRAY;
    }
    StringTokenizer st = new StringTokenizer(s, " ,;\t\r\n\f");
    int tokenCount = st.countTokens();
    if (tokenCount == 0) {
      return TestUtil.EMPTY_STRING_ARRAY;
    }
    String[] result = new String[tokenCount];
    for (int i = 0; st.hasMoreTokens(); i++) {
      result[i] = st.nextToken();
    }
    return result;
  }

  public static Properties loadPropertiesFor(String propFileName) {
    File propFile;

    // always load vehicle.properties from under src
    if (propFileName.equals("vehicle.properties")) {
      propFile = new File(SRC_DIR, propFileName);
    } else {
      propFile = new File(PROPS_DIR, propFileName);
    }

    Properties props = null;
    String propPath = propFile.getPath();
    if (propFile.exists()) {
      props = new Properties();
      try {
        System.out.println("Loading " + propPath);
        props.load(new FileInputStream(propFile));
      } catch (IOException ioe) {
        ioe.printStackTrace();
      }
    }
    return props;
  }
}
