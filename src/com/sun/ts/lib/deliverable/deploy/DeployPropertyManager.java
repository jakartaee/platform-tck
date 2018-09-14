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

import com.sun.ts.lib.deliverable.*;
import com.sun.ts.lib.util.*;
import com.sun.javatest.*;
import java.util.*;
import java.io.*;

/**
 * This class serves as a well known place for harness, util, and porting
 * classes to retrieve property values.
 *
 * @author Kyle Grucci
 */
public class DeployPropertyManager extends AbstractPropertyManager {
  private static DeployPropertyManager jteMgr = new DeployPropertyManager();

  private DeployPropertyManager() {
  }

  /**
   * This method returns the singleton instance of DeployPropertyManager which
   * provides access to all ts.jte properties. This is only called once by the
   * test harness.
   *
   * @param env
   *          - TestEnvironment object from JavaTest
   * @return DeployPropertyManager - singleton property manager object
   */
  public final static DeployPropertyManager getDeployPropertyManager(
      TestEnvironment env) throws Exception {
    jteMgr.setTestEnvironment(env);
    return jteMgr;
  }

  /**
   * This method returns the singleton instance of DeployPropertyManager which
   * provides access to all ts.jte properties. This is only called by the init()
   * method in ManualDeployment.java
   *
   * @param p
   *          - Properties object from JavaTest
   * @return DeployPropertyManager - singleton property manager object
   */
  public final static DeployPropertyManager getDeployPropertyManager(
      Properties p) throws Exception {
    jteMgr.setJteProperties(p);
    return jteMgr;
  }

  public final static DeployPropertyManager getDeployPropertyManager()
      throws Exception {
    return jteMgr;
  }

  /**
   * This method is called by the test harness to retrieve all properties needed
   * by a particular test.
   *
   * @param sPropKeys
   *          - Properties to retrieve
   * @return Properties - property/value pairs
   */
  public Properties getTestSpecificProperties(String[] sPropKeys)
      throws PropertyNotSetException {
    Properties pTestProps = super.getTestSpecificProperties(sPropKeys);
    // Deployment needs four additional properties to load a vendors
    // DeploymentManager implementation
    pTestProps.put("deployManagerJarFile", getProperty("deployManagerJarFile"));
    pTestProps.put("deployManageruri", getProperty("deployManageruri"));
    pTestProps.put("deployManageruname", getProperty("deployManageruname"));
    pTestProps.put("deployManagerpasswd", getProperty("deployManagerpasswd"));
    // Modules and deploymentplans to deploy for testing
    pTestProps.put("deploytestsEarFile", getProperty("deploytestsEarFile"));
    pTestProps.put("deploytestsEarPlan", getProperty("deploytestsEarPlan"));
    pTestProps.put("deploytestsEjbJarFile",
        getProperty("deploytestsEjbJarFile"));
    pTestProps.put("deploytestsEjbJarPlan",
        getProperty("deploytestsEjbJarPlan"));
    pTestProps.put("deploytestsWARFile", getProperty("deploytestsWARFile"));
    pTestProps.put("deploytestsWARPlan", getProperty("deploytestsWARPlan"));
    pTestProps.put("deploytestsCARFile", getProperty("deploytestsCARFile"));
    pTestProps.put("deploytestsCARPlan", getProperty("deploytestsCARPlan"));
    pTestProps.put("deploytestsRARFile", getProperty("deploytestsRARFile"));
    pTestProps.put("deploytestsRARPlan", getProperty("deploytestsRARPlan"));
    // Tools sig file properties
    pTestProps.put("ToolsSigTestClasspath",
        getProperty("ToolsSigTestClasspath"));
    pTestProps.put("ToolsSigTestUrl", getProperty("ToolsSigTestUrl"));

    String ctsHome = getProperty("cts_home", null);
    if (ctsHome != null) {
      pTestProps.put("cts_home", ctsHome);
    }
    return pTestProps;
  }
}
