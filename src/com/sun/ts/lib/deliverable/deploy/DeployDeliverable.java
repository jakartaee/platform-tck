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

package com.sun.ts.lib.deliverable.deploy;

import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import com.sun.javatest.TestEnvironment;
import com.sun.ts.lib.deliverable.DeliverableInterface;
import com.sun.ts.lib.deliverable.PropertyManagerInterface;
import com.sun.ts.lib.deliverable.tck.TCKDeliverable;
import com.sun.ts.lib.implementation.sun.javaee.runtime.SunRIDeploymentInfo;
import com.sun.ts.lib.porting.DeploymentInfo;

/**
 * This class serves as a well known place for harness, util, and porting
 * classes to retrieve property values.
 * 
 * @author Anand Dhingra
 */
public class DeployDeliverable extends TCKDeliverable
    implements DeliverableInterface {

  public PropertyManagerInterface createPropertyManager(TestEnvironment te)
      throws Exception {
    return DeployPropertyManager.getDeployPropertyManager(te);
  }

  public PropertyManagerInterface createPropertyManager(Properties p)
      throws Exception {
    return DeployPropertyManager.getDeployPropertyManager(p);
  }

  public PropertyManagerInterface getPropertyManager() throws Exception {
    return DeployPropertyManager.getDeployPropertyManager();
  }

  public boolean supportsAutoDeployment() {
    return true;
  }

  public Map getValidVehicles() {
    if (htTSValidVehicles == null) {
      // TS hash table
      htTSValidVehicles = new Hashtable();
      // add mgmt values
      htTSValidVehicles.put("tests.service_eetest.vehicles",
          new String[] { "standalone" });
    }
    return htTSValidVehicles;
  }

  public DeploymentInfo getDeploymentInfo(String earFile,
      String[] sValidRuntimeInfoFilesArray) {
    DeploymentInfo info = null;
    try {
      info = new SunRIDeploymentInfo(earFile, sValidRuntimeInfoFilesArray);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return info;
  }

  public String getAdditionalClasspath(String distDir) {
    return null;
  }
}
