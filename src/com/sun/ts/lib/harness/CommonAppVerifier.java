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

package com.sun.ts.lib.harness;

import com.sun.ts.lib.util.*;
import java.util.*;
import java.io.*;
import java.util.StringTokenizer;

/**
 * This class is used by the TS harness to figure out common archives that are
 * needed by test directories.
 *
 * A singleton class not intended for concurrent access.
 *
 */
public class CommonAppVerifier {

  public final static String COMMONARCHIVES_PROP_FILE_NAME = "commonarchives.properties";

  public final static String EXCLUDE_KEY = "exclude.dir";

  private static Properties mapping;

  private static String[] keys; // sorted ascending

  private static String[] excludes;

  private String relativeTestDir;

  private static boolean loaded;

  private static String distDir;

  // an uninitialized singleton instance
  private static CommonAppVerifier instance = new CommonAppVerifier();

  private CommonAppVerifier() {
  }

  public static CommonAppVerifier getInstance(File path) {
    if (instance == null) {
      instance = new CommonAppVerifier();
    }
    instance.init(path);
    String pathString = path.getPath();
    distDir = pathString.substring(0,
        pathString.lastIndexOf("dist" + File.separator) + 5);
    return instance;
  }

  private void loadExcludes() {
    if (mapping == null) {
      excludes = TestUtil.EMPTY_STRING_ARRAY;
    } else {
      excludes = ConfigUtil.stringToArray((String) mapping.remove(EXCLUDE_KEY));
    }
  }

  private void init(File file) {
    if (!loaded) {
      mapping = ConfigUtil.loadPropertiesFor(COMMONARCHIVES_PROP_FILE_NAME);

      if (File.separator.equals("\\")) {
        TestUtil.logHarnessDebug(
            "On Windows, so we must normalize the path returned from commonarchives.properties");

        Enumeration e = mapping.propertyNames();

        while (e.hasMoreElements()) {
          String key = (String) e.nextElement();
          String val = mapping.getProperty(key);
          mapping.setProperty(key, val.replace('/', File.separatorChar));
        }
      }

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
            "CommonAppVerifier:  This test dir is excluded from those listed in commonarchives.properties.");
        TestUtil.logHarnessDebug(
            "CommonAppVerifier:  Please check your exclude list in the commonarchives.properties file.");
        return true;
      }
    }
    return false;
  }

  /**
   * This method returns the common archives for a given test directory
   *
   * @return the common archives for a given test directory
   */
  public String[] getCommonApps() {

    String[] result;

    System.err.println("getCommonApps - relativeTestDir = " + relativeTestDir);
    if (mapping == null || keys == null || isExcluded()) {
      result = TestUtil.EMPTY_STRING_ARRAY;
    } else {
      result = getMappingValue();
    }

    return result;
  }

  private String[] getMappingValue() {

    String relativePath = "commonarchives." + relativeTestDir;
    for (int i = keys.length - 1; i >= 0; i--) { // must traverse in reverse
                                                 // order.
      if (relativePath.startsWith(keys[i])) {

        // check that that we don't have a test leaf dir thet is a subset
        // of another test leaf dir - e.g. jaxrs and jaxr
        String relativePathLeafDir = relativePath
            .substring(relativePath.lastIndexOf("/"));
        String keyLeafDir = keys[i].substring(keys[i].lastIndexOf("/"));
        // TestUtil.logHarnessDebug("relativePathLeafDir = " +
        // relativePathLeafDir);
        // TestUtil.logHarnessDebug("keyLeafDir = " + keyLeafDir);

        if (!relativePathLeafDir.startsWith(keyLeafDir)
            || relativePathLeafDir.equals(keyLeafDir)) {
          return stringToArray(mapping.getProperty(keys[i]));
        }
      }
    }
    return TestUtil.EMPTY_STRING_ARRAY;
  }

  private String[] stringToArray(String s) {
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
      result[i] = distDir + st.nextToken();
    }
    return result;
  }
}
/*
 * public class CommonAppVerifier { private static Map htValidApps; private
 * String sRelativeTestPath = "";
 * 
 * private CommonAppVerifier() {}
 * 
 * //use specific classloader if the class-to-load is not in the classpath of
 * the current vm. private static void getDeliverableTable (ClassLoader
 * classLoader) { if (htValidApps == null) { try { htValidApps =
 * DeliverableFactory.getDeliverableInstance(classLoader).getCommonApps(); }
 * catch (Exception e) { TestUtil.
 * logHarness("ERROR:  Failed to get common apps from the Deliverable instance."
 * , e); } } }
 * 
 * public static String[] getCommonApps (String sPath) { return
 * getCommonApps(sPath, null); }
 * 
 * 
 * public static String[] getCommonApps (String sPath, ClassLoader classLoader)
 * { //get the mappings from the active deliverable
 * getDeliverableTable(classLoader); String sPathCopy = sPath; String
 * sRelativeTestPath = convertSlashesToDashes(sPath); String[] sVal2 = null;
 * String sRelativeTestPathCopy = sRelativeTestPath; int iTestsIndex =
 * sPathCopy.lastIndexOf(File.separator + TestUtil.getDistString() +
 * File.separator); String sTSHome = sPathCopy.substring(0, iTestsIndex);
 * if(TestUtil.harnessDebug) TestUtil.logHarnessDebug("Try TS_HOME = " +
 * sTSHome); while (!(new File(sTSHome + File.separator + "bin")).exists()) {
 * iTestsIndex = sTSHome.lastIndexOf(File.separator + TestUtil.getDistString() +
 * File.separator); if (iTestsIndex == -1) { TestUtil.
 * logHarness("ERROR:  Failed to find the tests directory while trying to get TS_HOME from the test directory:  "
 * + sPathCopy); break; } sTSHome = sTSHome.substring(0, iTestsIndex); } int
 * iTSDirLength = sTSHome.length(); if (sPathCopy.endsWith(File.separator)) {
 * sPathCopy = sPathCopy.substring(0, sPathCopy.length() - 1); }
 * sRelativeTestPath = (sPathCopy.substring(iTSDirLength +
 * 1)).replace(File.separatorChar, '_');
 * 
 * if(TestUtil.harnessDebug) TestUtil.logHarnessDebug("sRelativeTestPath = " +
 * sRelativeTestPath);
 * 
 * if (sTSHome.endsWith(File.separator)) { sTSHome = sTSHome.substring(0,
 * sTSHome.length() - 1); }
 * 
 * if(TestUtil.harnessDebug)
 * TestUtil.logHarnessDebug("TS_HOME from backtracing=" + sTSHome); do { sVal2 =
 * (String[])htValidApps.get(sRelativeTestPathCopy + ".common_apps"); if
 * (TestUtil.harnessDebug && sVal2 != null) {
 * TestUtil.logHarnessDebug("common apps for " + sRelativeTestPathCopy + " = " +
 * Arrays.asList(sVal2).toString()); } } while ((sRelativeTestPathCopy =
 * getNextLevelUp(sRelativeTestPathCopy)) != null && sVal2 == null); if(sVal2 !=
 * null && TestUtil.harnessDebug) {
 * TestUtil.logHarnessDebug("common apps from lookup: " +
 * Arrays.asList(sVal2).toString()); } //add ctshome to each relative common app
 * path if(sVal2 == null) { return null; } String[] result = new
 * String[sVal2.length]; for (int ii = 0; ii < sVal2.length; ii++) { result[ii]
 * = sTSHome + convertDashesToSlashes(sVal2[ii]); } return result; }
 * 
 * private static String getNextLevelUp (String sDottedPath) { int index = 0;
 * String sNewPath = null; index = sDottedPath.lastIndexOf("-"); if (index !=
 * -1) sNewPath = sDottedPath.substring(0, index); return sNewPath; }
 * 
 * private static String convertSlashesToDashes (String sTestDir) { String
 * sRelativeTestPath = (sTestDir.substring(sTestDir.indexOf(File.separator +
 * "ts" + File.separator + "tests" + File.separator) +
 * 4)).replace(File.separatorChar, '-'); if(TestUtil.harnessDebug)
 * TestUtil.logHarnessDebug("convertSlashesToDashes(): sRelativeTestPath = " +
 * sRelativeTestPath); return sRelativeTestPath; }
 * 
 * private static String convertDashesToSlashes (String sTestDir) { String
 * sRelativeTestPath = sTestDir.replace('-', File.separatorChar);
 * if(TestUtil.harnessDebug)
 * TestUtil.logHarnessDebug("convertDashToSlashes(): sRelativeTestPath = " +
 * sRelativeTestPath); return sRelativeTestPath; } }
 */
