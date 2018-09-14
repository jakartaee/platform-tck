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

package com.sun.ts.lib.deliverable.jaxr;

import com.sun.javatest.TestEnvironment;
import com.sun.ts.lib.deliverable.AbstractPropertyManager;
import com.sun.ts.lib.deliverable.PropertyNotSetException;

import java.util.Properties;

/**
 * This class serves as a well known place for harness, util, and porting
 * classes to retrieve property values.
 *
 * @author Kyle Grucci
 */
public class JAXRPropertyManager extends AbstractPropertyManager {
  // uninitialized singleton instance
  private static JAXRPropertyManager jteMgr = new JAXRPropertyManager();

  private JAXRPropertyManager() {
  }

  /**
   * This method returns the singleton instance of TSPropertyManager which
   * provides access to all ts.jte properties. This is only called once by the
   * test harness.
   *
   * @param env - TestEnvironment object from JavaTest
   * @return TSPropertyManager - singleton property manager object
   */
  public final static JAXRPropertyManager getJAXRPropertyManager(
      TestEnvironment env) throws PropertyNotSetException {
    jteMgr.setTestEnvironment(env);
    return jteMgr;
  }

  /**
   * This method returns the singleton instance of JAXRPropertyManager which
   * provides access to all ts.jte properties. This is only called by the init()
   * method in ManualDeployment.java
   *
   * @param p - Properties object from JavaTest
   * @return JAXRPropertyManager - singleton property manager object
   */
  public final static JAXRPropertyManager getJAXRPropertyManager(Properties p)
      throws PropertyNotSetException {
    jteMgr.setJteProperties(p);
    return jteMgr;
  }

  public final static JAXRPropertyManager getJAXRPropertyManager()
      throws PropertyNotSetException {
    return jteMgr;
  }

  /**
   * This method is called by the test harness to retrieve all properties needed
   * by a particular test.
   *
   * @param sPropKeys - Properties to retrieve
   * @return Properties - property/value pairs
   */
  public Properties getTestSpecificProperties(String[] sPropKeys)
      throws PropertyNotSetException {
    Properties pTestProps = super.getTestSpecificProperties(sPropKeys);

    // if the abstract propertymanager already loaded all props, just return
    if (pTestProps.getProperty("all.props").equalsIgnoreCase("true")) {
      return pTestProps;
    }

    // add all porting class props so the factories can work in the server
    pTestProps.put("javaee.level", getProperty("javaee.level", "full"));
    pTestProps.put("platform.mode", getProperty("platform.mode", "standalone"));

    // JAXR specific properties
    pTestProps.put("jaxrConnectionFactoryLookup",
        getProperty("jaxrConnectionFactoryLookup"));
    pTestProps.put("jaxrSecurityCredentialType",
        getProperty("jaxrSecurityCredentialType"));
    pTestProps.put("jaxrWebContext", getProperty("jaxrWebContext"));
    pTestProps.put("jaxrJNDIResource", getProperty("jaxrJNDIResource"));
    pTestProps.put("jaxrAlias", getProperty("jaxrAlias"));
    pTestProps.put("jaxrAlias2", getProperty("jaxrAlias2"));
    pTestProps.put("jaxrAliasPassword", getProperty("jaxrAliasPassword"));
    pTestProps.put("jaxrAlias2Password", getProperty("jaxrAlias2Password"));
    pTestProps.put("providerCapability", getProperty("providerCapability"));
    pTestProps.put("registryURL", getProperty("registryURL"));
    pTestProps.put("queryManagerURL", getProperty("queryManagerURL"));
    pTestProps.put("jaxrUser", getProperty("jaxrUser"));
    pTestProps.put("jaxrPassword", getProperty("jaxrPassword"));
    pTestProps.put("jaxrUser2", getProperty("jaxrUser2"));
    pTestProps.put("jaxrPassword2", getProperty("jaxrPassword2"));
    pTestProps.put("authenticationMethod", getProperty("authenticationMethod"));
    // property for running sig tests
    pTestProps.put("sigTestClasspath", getProperty("sigTestClasspath"));
    return pTestProps;
  }
}
