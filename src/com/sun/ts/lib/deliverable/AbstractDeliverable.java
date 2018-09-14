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

package com.sun.ts.lib.deliverable;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.lib.porting.DeploymentInfo;
import java.util.Map;
import java.util.Hashtable;

/**
 * This class serves as an abstract implementation of the DeliverableInterface.
 * It can be extended to customize values for a particular deliverable.
 *
 * @author Kyle Grucci
 */
public abstract class AbstractDeliverable implements DeliverableInterface {
  protected Map htTSValidVehicles;

  protected Map htValidApps;

  protected Map htValidRunDirections;

  public boolean supportsAutoDeployment() {
    return true;
  }

  public boolean supportsAutoJMSAdmin() {
    return true;
  }

  public Map getValidVehicles() {
    if (htTSValidVehicles == null) {
      // TS hash table
      htTSValidVehicles = new Hashtable();
      // add default values
      htTSValidVehicles.put("tests.service_eetest.vehicles",
          new String[] { "ejb", "servlet", "jsp" });
    }
    return htTSValidVehicles;
  }

  public Map getInteropDirections() {
    if (htValidRunDirections == null) {
      htValidRunDirections = new Hashtable();
      // default for all tests
      htValidRunDirections.put("tests.interop", "forward");
    }
    return htValidRunDirections;
  }

  public boolean supportsInterop() {
    return true;
  }

  public String getAdditionalClasspath(String distDir) {
    return null;
  }

  public DeploymentInfo getDeploymentInfo(String earFile,
      String[] sValidRuntimeInfoFilesArray) {
    return null;
  }
}
