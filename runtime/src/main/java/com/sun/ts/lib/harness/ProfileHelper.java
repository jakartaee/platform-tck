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

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import com.sun.ts.lib.deliverable.DeliverableFactory;
import com.sun.ts.lib.deliverable.PropertyManagerInterface;
import com.sun.ts.lib.util.TestUtil;

public final class ProfileHelper {

  private static PropertyManagerInterface jteMgr;

  public static String[] getArchives(String sDir, String sInteropDirections) {
    try {
      jteMgr = DeliverableFactory.getDeliverableInstance().getPropertyManager();
    } catch (Exception e) {
      e.printStackTrace();
    }

    String javaeeLevel = jteMgr.getProperty("javaee.level", "full");
    ArrayList<String> sFilteredAppJarsArray = new ArrayList<String>();
    boolean bEarPresent = false;

    File fTestDir = new File(sDir);
    String[] sAppJarsArray = fTestDir.list(ArchiveFilter.getInstance());

    if (sInteropDirections.equals("forward") && !isInteropDir(sDir)) {
      // if forward and not an interop dir, then only deploy the non vibuilt
      // apps
      sAppJarsArray = fTestDir.list(NonVIBuiltArchiveFilter.getInstance());
    } else if (sInteropDirections.equals("reverse") && !isInteropDir(sDir)) {
      // if reverse and not an interop dir, then only deploy the vibuilt apps
      sAppJarsArray = fTestDir.list(VIBuiltArchiveFilter.getInstance());
    }

    if (sAppJarsArray != null) {
      // first, see if we have any ears
      bEarPresent = isEarPresent(sAppJarsArray);

      if (javaeeLevel.contains("full")) {
        if (bEarPresent) {
          for (int ii = 0; ii < sAppJarsArray.length; ii++) {
            String name = sAppJarsArray[ii];
            if ((name.endsWith(".ear") || (name.indexOf("_component") != -1
                && (name.endsWith(".jar") || name.endsWith(".war")
                    || name.endsWith(".rar") || name.endsWith(".car"))))) {
              // if any ears exist, return only ears and _component
              sFilteredAppJarsArray.add(name);
            }
          }
          sAppJarsArray = sFilteredAppJarsArray.toArray(new String[0]);
        }
      } else {
        // Must be in a subset of EE so strip out ears ro basicaslly
        // anything that is not a war
        for (int ii = 0; ii < sAppJarsArray.length; ii++) {
          String name = sAppJarsArray[ii];
          if (name.endsWith(".war")) {
            // if any ears exist, return all non-ears
            sFilteredAppJarsArray.add(name);
          }
        }
        sAppJarsArray = sFilteredAppJarsArray.toArray(new String[0]);
      }
    }
    return sAppJarsArray;
  }

  private static boolean isEarPresent(String[] archives) {

    boolean bFoundEar = false;

    for (int ii = 0; ii < archives.length; ii++) {
      if (archives[ii].endsWith(".ear")) {
        bFoundEar = true;
        break;
      }
    }
    return bFoundEar;
  }

  private static boolean isInteropDir(String sDir) {
    return (sDir.indexOf("interop") != -1);
  }

  public static class VIBuiltArchiveFilter extends ArchiveFilter {
    private static VIBuiltArchiveFilter instance = new VIBuiltArchiveFilter();

    private VIBuiltArchiveFilter() {
    }

    public static VIBuiltArchiveFilter getInstance() {
      return instance;
    }

    public boolean accept(File dir, String name) {
      return (name.endsWith(".ear") || name.endsWith(".war")
          || name.endsWith(".rar") || name.endsWith(".jar"))
          && name.startsWith("vi_built_") && deployThisVehicleApp(dir, name);
    }
  }

  public static class NonVIBuiltArchiveFilter extends ArchiveFilter {
    private static NonVIBuiltArchiveFilter instance = new NonVIBuiltArchiveFilter();

    private NonVIBuiltArchiveFilter() {
    }

    public static NonVIBuiltArchiveFilter getInstance() {
      return instance;
    }

    public boolean accept(File dir, String name) {
      return (name.endsWith(".ear") || name.endsWith(".war")
          || name.endsWith(".rar") || name.endsWith(".jar"))
          && !name.startsWith("vi_built_") && deployThisVehicleApp(dir, name);
    }
  }

  public static class ArchiveFilter implements FilenameFilter {
    private static ArchiveFilter instance = new ArchiveFilter();

    private ArchiveFilter() {
    }

    public static ArchiveFilter getInstance() {
      return instance;
    }

    public boolean accept(File dir, String name) {
      return (name.endsWith(".ear") || name.endsWith(".war")
          || name.endsWith(".rar") || name.endsWith(".jar"))
          && deployThisVehicleApp(dir, name) && appIsNotExcluded(dir, name);
    }

    /*
     * This method is used to exclude certain apps containg rars from being
     * deployed given that we deploy them ahead of time as part of the TCK
     * configuration.
     */
    protected boolean appIsNotExcluded(File file, String sName) {
      boolean bVal = true;

      // Return true if we don't have one of these specific apps that
      // we don't want deployed
      if (sName.indexOf("ejb_Tsr.ear") != -1
          || sName.indexOf("ejb_Deployment.ear") != -1) {
        // never deploy any ejbembed vehicle jars
        bVal = false;
      }

      return bVal;

    }

    protected boolean deployThisVehicleApp(File file, String sName) {
      boolean bVal = false;
      // test whether we should deploy this vehicle ear file
      if (sName.indexOf("ejbembed") != -1) {
        // never deploy any ejbembed vehicle jars
        bVal = false;
      } else if (sName.indexOf("_vehicle") == -1
          || sName.indexOf("_vehicles") != -1) {
        bVal = true;
      } else {
        // check that this vehicle archive should be deployed
        VehicleVerifier vehicleVerifier = VehicleVerifier.getInstance(file);
        String[] sVal = vehicleVerifier.getVehicleSet();
        if (sVal != null) {

          // check keywords to see if the user has subsetted...
          String keywords = jteMgr.getProperty("current.keywords", "all");
          TestUtil.logHarnessDebug(
              "ProfileHelper - current.keywords = " + keywords);
          ArrayList<String> filtered = new ArrayList<String>();

          for (int ii = 0; ii < sVal.length; ii++) {
            if ((keywords.indexOf(sVal[ii] + "_vehicle") != -1
                && keywords.indexOf("!" + sVal[ii] + "_vehicle") == -1)
                || keywords.indexOf("!" + sVal[ii] + "_vehicle") == -1) {
              filtered.add(sVal[ii]);

            }
          }

          // if keywords refer to specific veicle(s), then subset the list of
          // valid vehicles
          if (!filtered.isEmpty()) {
            sVal = filtered.toArray(new String[0]);
          }

          for (int ii = 0; ii < sVal.length; ii++) {
            if (sName.indexOf(sVal[ii] + "_vehicle") != -1)
              bVal = true;
          }
        }
      }
      return bVal;
    }
  }
}
