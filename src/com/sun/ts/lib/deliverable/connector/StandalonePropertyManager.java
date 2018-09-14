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

package com.sun.ts.lib.deliverable.connector;

import com.sun.ts.lib.deliverable.*;
import com.sun.ts.lib.deliverable.tck.*;
import com.sun.javatest.TestEnvironment;

import java.util.Map;
import java.util.Properties;

/**
 * This class serves as a well known place for harness, util, and porting
 * classes to retrieve property values.
 */
public class StandalonePropertyManager extends TCKPropertyManager {

  // uninitialized singleton instance
  private static StandalonePropertyManager jteMgr = new StandalonePropertyManager();

  /**
   * This method returns the singleton instance of TSPropertyManager which
   * provides access to all ts.jte properties. This is only called once by the
   * test harness.
   *
   * @param env
   *          - TestEnvironment object from JavaTest
   * @return TSPropertyManager - singleton property manager object
   */
  public final static StandalonePropertyManager getStandalonePropertyManager(
      TestEnvironment env) throws PropertyNotSetException {
    jteMgr.setTestEnvironment(env);
    return jteMgr;
  }

  /**
   * This method returns the singleton instance of StandalonePropertyManager
   * which provides access to all ts.jte properties. This is only called by the
   * init() method in ManualDeployment.java
   *
   * @param p
   *          - Properties object from JavaTest
   * @return StandalonePropertyManager - singleton property manager object
   */
  public final static StandalonePropertyManager getStandalonePropertyManager(
      Properties p) throws PropertyNotSetException {
    jteMgr.setJteProperties(p);
    return jteMgr;
  }

  public final static StandalonePropertyManager getStandalonePropertyManager()
      throws PropertyNotSetException {
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

    // if the abstract propertymanager already loaded all props, just return
    // if (pTestProps.getProperty("all.props").equalsIgnoreCase("true")) {
    // return pTestProps;
    // }

    String sJtePropVal = "";
    // add all porting class props so the factories can work in the server
    pTestProps.put("porting.ts.login.class.1",
        getProperty("porting.ts.login.class.1"));
    pTestProps.put("porting.ts.HttpsURLConnection.class.1",
        getProperty("porting.ts.HttpsURLConnection.class.1"));
    pTestProps.put("porting.ts.url.class.1",
        getProperty("porting.ts.url.class.1"));
    pTestProps.put("namingServiceHost2", getProperty("namingServiceHost2"));
    pTestProps.put("namingServicePort2", getProperty("namingServicePort2"));
    pTestProps.put("namingServiceHost1", getProperty("namingServiceHost1"));
    pTestProps.put("namingServicePort1", getProperty("namingServicePort1"));

    return pTestProps;
  }
}
