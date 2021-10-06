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

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.deliverable.DeliverableFactory;
import com.sun.ts.lib.deliverable.PropertyNotSetException;
import com.sun.ts.lib.deliverable.PropertyManagerInterface;
import java.util.Map;
import java.io.File;

/**
 * This class is used by the TS harness to figure out which common apps are
 * required by tests in the TS.
 *
 * @author Kyle Grucci
 */
public class InteropTestManager {
  private static Map htValidRunDirections;

  private String sRelativeTestPath = "";

  private static PropertyManagerInterface jteMgr;

  /**
   * This method returns the directions that a given interop test is to be run
   * in. Possible values are forward, reverse, or both.
   *
   * @return the direction(s) to run - forward, reverse, or both.
   */
  public static String getInteropDirections(String sPath) {
    String sPathCopy = sPath;
    String sRelativeTestPath = convertSlashesToUnderscores(sPath);
    String sVal2 = null;
    String sRelativeTestPathCopy = sRelativeTestPath;

    // if we're in a rebuildable dir, then set the interop directions to both
    if (RebuildableVerifier.getInstance(new File(sPath)).isRebuildable()) {
      sVal2 = "both";
    } else {
      do {
        getDeliverableTable();
        sVal2 = (String) htValidRunDirections
            .get(sRelativeTestPathCopy + ".interop");
        if (TestUtil.harnessDebug)
          TestUtil.logHarnessDebug("getInteropDirections for "
              + sRelativeTestPathCopy + " :" + sVal2);
      } while ((sRelativeTestPathCopy = getNextLevelUp(
          sRelativeTestPathCopy)) != null && sVal2 == null);
    }

    if (TestUtil.harnessDebug)
      TestUtil.logHarnessDebug(
          "getInteropDirections - default Interop directions: " + sVal2);
    return (sVal2 == null ? "forward" : sVal2);
  }

  /**
   * This method returns the directions that a given interop test is to be run
   * in based on the javatest keywords. Possible values are forward, reverse, or
   * both.
   *
   * @return the direction(s) to run - forward, reverse, or both.
   */
  public static String getInteropDirectionsFromKeywords(String sPath) {

    String defaultDirections = getInteropDirections(sPath);
    String sDirections = defaultDirections;

    // Check keywords from the PropManager and then
    // if kewords contains all, then return default interopDirections for the
    // directory
    // if keywords contains forward but not reverse or all, and the default ==
    // forward or both, then return forward
    // if keywords contains reverse and not forward or all, and the def ==
    // reverse or both, then return reverse

    if (jteMgr == null) {
      try {
        jteMgr = DeliverableFactory.getDeliverableInstance()
            .getPropertyManager();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    if (jteMgr != null) {
      String keywords = jteMgr.getProperty("current.keywords", "all");

      if (keywords.contains("forward") && !keywords.contains("reverse")
          && !keywords.contains("all") && (defaultDirections.equals("forward")
              || defaultDirections.equals("both"))) {
        sDirections = "forward";
      } else if (keywords.contains("reverse") && !keywords.contains("forward")
          && !keywords.contains("all") && (defaultDirections.equals("reverse")
              || defaultDirections.equals("both"))) {
        sDirections = "reverse";
      }
    }
    TestUtil.logHarnessDebug(
        "getInteropDirectionsFromKeywords - directions: " + sDirections);
    return sDirections;
  }

  private static String getNextLevelUp(String sDottedPath) {
    int index = 0;
    String sNewPath = null;
    index = sDottedPath.lastIndexOf("_");
    if (index != -1)
      sNewPath = sDottedPath.substring(0, index);
    return sNewPath;
  }

  private static String convertSlashesToUnderscores(String sTestDir) {
    String sRelativeTestPath = (sTestDir.substring(sTestDir.indexOf(
        File.separator + "ts" + File.separator + "tests" + File.separator) + 4))
            .replace(File.separatorChar, '_');
    return sRelativeTestPath;
  }

  private static void getDeliverableTable() {
    if (htValidRunDirections == null) {
      try {
        htValidRunDirections = DeliverableFactory.getDeliverableInstance()
            .getInteropDirections();
      } catch (Exception e) {
        TestUtil.logHarness(
            "ERROR:  Failed to get interop directions from the Deliverable instance.");
      }
    }
  }
}
