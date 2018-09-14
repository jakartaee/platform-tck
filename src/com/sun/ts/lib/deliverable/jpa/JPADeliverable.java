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

package com.sun.ts.lib.deliverable.jpa;

import com.sun.ts.lib.deliverable.AbstractDeliverable;
import com.sun.ts.lib.deliverable.PropertyManagerInterface;
import com.sun.javatest.TestEnvironment;
import com.sun.ts.lib.util.TestUtil;
import java.io.File;
import java.util.Map;
import java.util.Properties;

public class JPADeliverable extends AbstractDeliverable {

  private static String PERSISTENCE_ARCHIVE_FILE_EXT = ".jar";

  public PropertyManagerInterface createPropertyManager(TestEnvironment te)
      throws Exception {
    return JPAPropertyManager.getJPAPropertyManager(te);
  }

  public PropertyManagerInterface createPropertyManager(Properties p)
      throws Exception {
    return JPAPropertyManager.getJPAPropertyManager(p);
  }

  public PropertyManagerInterface getPropertyManager() throws Exception {
    return JPAPropertyManager.getJPAPropertyManager();
  }

  public boolean supportsAutoDeployment() {
    return false;
  }

  public boolean supportsAutoJMSAdmin() {
    return false;
  }

  public boolean supportsInterop() {
    return false;
  }

  public Map getValidVehicles() {
    super.getValidVehicles();

    // add default values
    htTSValidVehicles.put("tests.service_eetest.vehicles",
        new String[] { "standalone" });

    return htTSValidVehicles;
  }

  public String getAdditionalClasspath(String distDir) {
    String result = null;
    TestUtil
        .logMsg("Search for persistence archives under the current dist dir:"
            + distDir);
    File currentDistDirAsFile = new File(distDir);
    for (File aFile : currentDistDirAsFile.listFiles()) {
      if (!aFile.isDirectory()
          && aFile.getName().endsWith(PERSISTENCE_ARCHIVE_FILE_EXT)) {
        TestUtil.logMsg("Found persistence archive file: " + aFile.getName());
        if (result == null) {
          // the first persistence archive file in the current dist dir
          result = aFile.getAbsolutePath();
        } else {
          // we already have found other persistence archives in the current
          // dist dir
          result += File.pathSeparator + aFile.getAbsolutePath();
        }
      }
    }
    TestUtil.logMsg(
        "Will need to append the following to test run classpath: " + result);
    return result;
  }
}
