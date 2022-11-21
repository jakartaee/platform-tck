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
 * This class is used by the TS harness to figure out which keywords should be
 * associated with directories of tests. Keywords are read in by the
 * TSTestFinder and written out to the test decriptions.
 *
 * A singleton class not intended for concurrent access.
 *
 * 
 */
public class TSKeywords {
  public final static String KEYWORD_PROP_FILE_NAME = "keyword.properties";

  private Properties mapping;

  private String[] keys; // sorted ascending

  private String relativeTestDir;

  private boolean loaded;

  // an uninitialized singleton instance
  private static TSKeywords instance = new TSKeywords();

  private TSKeywords() {
  }

  public static TSKeywords getInstance(File path) {
    if (instance == null) {
      instance = new TSKeywords();
    }
    instance.init(path);
    return instance;
  }

  private void init(File file) {
    if (!loaded) {
      mapping = ConfigUtil.loadPropertiesFor(KEYWORD_PROP_FILE_NAME);
      keys = ConfigUtil.loadKeysFrom(mapping);
      loaded = true;
    }
    if (mapping != null) {
      this.relativeTestDir = TestUtil.getRelativePath(file.getPath());
    }
  }

  /**
   * This method gets the current set of keywords to be used for a given
   * directory path.
   *
   * @return a String array of the keywords that this test should be run in
   */
  public String[] getKeywordSet() {
    if (mapping == null || keys == null) {
      return TestUtil.EMPTY_STRING_ARRAY;
    }

    String[] result = ConfigUtil.getMappingValue(this.mapping, this.keys,
        this.relativeTestDir);
    return result;
  }
}
