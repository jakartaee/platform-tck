/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.lib.harness;

import com.sun.ts.lib.util.*;
import java.util.*;
import java.io.*;

/**
 * This class is used by the TS harness to figure out which test directories
 * contain tests which must be rebuilt using a vendor's tools and then run
 * against the Reference Implementation.
 *
 * A singleton class not intended for concurrent access.
 *
 */
public class RebuildableVerifier {
  public final static String REBUILDABLE_PROP_FILE_NAME = "rebuildable.properties";

  public final static String EXCLUDE_KEY = "exclude.dir";

  private static Properties mapping;

  private static String[] keys; // sorted ascending

  private static String[] excludes;

  private String relativeTestDir;

  private static boolean loaded;

  // an uninitialized singleton instance
  private static RebuildableVerifier instance = new RebuildableVerifier();

  private RebuildableVerifier() {
  }

  public static RebuildableVerifier getInstance(File path) {
    if (instance == null) {
      instance = new RebuildableVerifier();
    }
    instance.init(path);
    return instance;
  }

  private void loadExcludes() {
    if (this.mapping == null) {
      excludes = TestUtil.EMPTY_STRING_ARRAY;
    } else {
      excludes = ConfigUtil.stringToArray((String) mapping.remove(EXCLUDE_KEY));
    }
  }

  private void init(File file) {
    if (!loaded) {
      mapping = ConfigUtil.loadPropertiesFor(REBUILDABLE_PROP_FILE_NAME);
      loadExcludes();
      keys = ConfigUtil.loadKeysFrom(mapping);
      loaded = true;
    }
    if (mapping != null) {
      this.relativeTestDir = TestUtil.getRelativePath(file.getPath());
    }

  }

  private boolean isExcluded() {
    for (int i = 0; i < excludes.length; i++) {
      if (relativeTestDir.startsWith(excludes[i])) {
        TestUtil.logHarnessDebug(
            "RebuildableVerifier:  This test dir is excluded from those listed in rebuildable.properties.");
        TestUtil.logHarnessDebug(
            "RebuildableVerifier:  Please check your exclude list in the rebuildable.properties file.");
        return true;
      }
    }
    return false;
  }

  /**
   * This method tells us if a given directory path is rebuildable of not
   *
   * @return true if the directory path contains rebuildable tests run in
   */
  public boolean isRebuildable() {
    boolean result = false;

    if (mapping == null || keys == null || isExcluded()) {
      result = false;
    } else {
      for (int i = keys.length - 1; i >= 0; i--) { // must traverse in reverse
                                                   // order.
        if (("rebuildable." + relativeTestDir).startsWith(keys[i])) {
          result = true;
          break;
        }
      }
    }
    return result;
  }
}
