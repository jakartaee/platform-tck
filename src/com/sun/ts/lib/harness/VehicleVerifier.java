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

package com.sun.ts.lib.harness;

import com.sun.ts.lib.util.*;
import java.util.*;
import java.io.*;

/**
 * This class is used by the TS harness to figure out which vehicles are to be
 * used by the Service tests in the TS. These defaults can be overridden by
 * editing appropriate properties file. However, this override mechanism is only
 * to be used for debugging purposes. When testing for J2EE certification, the
 * defaults specified in this class must be used.
 *
 * A singleton class not intended for concurrent access.
 *
 * @author Kyle Grucci
 */
public class VehicleVerifier {
  public final static String VEHICLE_PROP_FILE_NAME = "vehicle.properties";

  public final static String EXCLUDE_KEY = "exclude.dir";

  private static Properties mapping;

  private static String[] keys; // sorted ascending

  private static String[] excludes;

  private String relativeTestDir;

  private String testName;

  private static boolean loaded;

  // an uninitialized singleton instance
  private static VehicleVerifier instance = new VehicleVerifier();

  private VehicleVerifier() {
  }

  public static VehicleVerifier getInstance(File path) {
    if (instance == null) {
      instance = new VehicleVerifier();
    }
    instance.init(path, null);
    return instance;
  }

  public static VehicleVerifier getInstance(File path, String sTestName) {
    if (instance == null) {
      instance = new VehicleVerifier();
    }
    instance.init(path, sTestName);
    return instance;
  }

  private void loadExcludes() {
    if (this.mapping == null) {
      excludes = TestUtil.EMPTY_STRING_ARRAY;
    } else {
      excludes = ConfigUtil.stringToArray((String) mapping.remove(EXCLUDE_KEY));
    }
  }

  private void init(File file, String sTest) {
    if (!loaded) {
      mapping = ConfigUtil.loadPropertiesFor(VEHICLE_PROP_FILE_NAME);
      loadExcludes();
      keys = ConfigUtil.loadKeysFrom(mapping);
      loaded = true;
    }
    testName = sTest;
    if (mapping != null) {
      this.relativeTestDir = TestUtil.getRelativePath(file.getPath());
      if (testName != null) {
        this.relativeTestDir += "#" + testName;
        TestUtil.logHarnessDebug(
            "VehicleVerifier.init:  relative dir = " + this.relativeTestDir);

      }
    }
    // if mapping is null, it means this tck uses no vehicles and
    // vehicle.properties
    // does not exist. So don't bother to convert testDir to relative path.
  }

  private boolean isExcluded() {
    for (int i = 0; i < excludes.length; i++) {
      if (relativeTestDir.startsWith(excludes[i])) {
        TestUtil.logHarnessDebug(
            "VehicleVerifier:  This test dir is excluded from those listed in vehicle.properties.");
        TestUtil.logHarnessDebug(
            "VehicleVerifier:  Please check your exclude list in the vehicle.properties file.");
        return true;
      }
    }
    return false;
  }

  /**
   * This method gets the current set of vehicles to be used for a given
   * directory path.
   *
   * @return a String array of the vehicles that this test should be run in
   */
  public String[] getVehicleSet() {
    if (mapping == null || keys == null) {
      return TestUtil.EMPTY_STRING_ARRAY;
    }
    if (isExcluded()) {
      return TestUtil.EMPTY_STRING_ARRAY;
    }
    String[] result = ConfigUtil.getMappingValue(this.mapping, this.keys,
        this.relativeTestDir);
    return result;
  }

  public static void main(String args[]) {
    File testDir = null;
    if (args.length == 0) {
      testDir = new File(System.getProperty("user.dir"));
    } else {
      testDir = new File(args[0]);
    }
    VehicleVerifier ver = VehicleVerifier.getInstance(testDir);
    String[] result = ver.getVehicleSet();
    System.out
        .println(testDir.getPath() + " : " + Arrays.asList(result).toString());
  }
}
