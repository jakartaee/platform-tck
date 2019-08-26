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

package com.sun.ts.lib.deliverable.jaxrpc;

import com.sun.javatest.TestEnvironment;
import com.sun.ts.lib.deliverable.AbstractPropertyManager;
import com.sun.ts.lib.deliverable.PropertyNotSetException;

import java.io.File;
import java.util.Properties;

/**
 * This class serves as a well known place for harness, util, and porting
 * classes to retrieve property values.
 *
 * @author Kyle Grucci
 */
public class JAXRPCPropertyManager extends AbstractPropertyManager {
  // uninitialized singleton instance
  private static JAXRPCPropertyManager jteMgr = new JAXRPCPropertyManager();

  private JAXRPCPropertyManager() {
  }

  /**
   * This method returns the singleton instance of TSPropertyManager which
   * provides access to all ts.jte properties. This is only called once by the
   * test harness.
   *
   * @param env
   *          - TestEnvironment object from JavaTest
   * @return TSPropertyManager - singleton property manager object
   */
  public final static JAXRPCPropertyManager getJAXRPCPropertyManager(
      TestEnvironment env) throws PropertyNotSetException {
    jteMgr.setTestEnvironment(env);
    return jteMgr;
  }

  /**
   * This method returns the singleton instance of JAXRPropertyManager which
   * provides access to all ts.jte properties. This is only called by the init()
   * method in ManualDeployment.java
   *
   * @param p
   *          - Properties object from JavaTest
   * @return JAXRPropertyManager - singleton property manager object
   */
  public final static JAXRPCPropertyManager getJAXRPCPropertyManager(
      Properties p) throws PropertyNotSetException {
    jteMgr.setJteProperties(p);
    return jteMgr;
  }

  public final static JAXRPCPropertyManager getJAXRPCPropertyManager()
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
    pTestProps.put("porting.ts.url.class.1",
        getProperty("porting.ts.url.class.1"));
    pTestProps.put("porting.ts.jaxrpc.class.1",
        getProperty("porting.ts.jaxrpc.class.1"));

    // add all porting class props so the factories can work in the server
    pTestProps.put("javaee.level", getProperty("javaee.level", "full"));
    pTestProps.put("platform.mode", getProperty("platform.mode", "standalone"));

    pTestProps.put("jaxrpc.mode", "jaxrpc");
    pTestProps.put("user", "j2ee");
    pTestProps.put("password", "j2ee");
    pTestProps.put("authuser", "javajoe");
    pTestProps.put("authpassword", "javajoe");

    String tsHome = getProperty("TS_HOME", null);
    if (tsHome == null)
      tsHome = getProperty("cts_home", null);
    if (tsHome != null)
      pTestProps.put("cts_home", tsHome);
    if (tsHome != null)
      pTestProps.put("bin.dir", tsHome + File.separator + "bin");
    // JAXRPC specific properties
    // property for running sig tests
    pTestProps.put("sigTestClasspath", getProperty("sigTestClasspath"));
    return pTestProps;
  }
}
