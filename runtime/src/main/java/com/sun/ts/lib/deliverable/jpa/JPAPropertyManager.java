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

import com.sun.javatest.TestEnvironment;
import com.sun.ts.lib.deliverable.AbstractPropertyManager;
import com.sun.ts.lib.deliverable.PropertyNotSetException;
import com.sun.ts.tests.jpa.common.PMClientBase;

import java.util.Properties;

public class JPAPropertyManager extends AbstractPropertyManager {

  private static JPAPropertyManager jteMgr = new JPAPropertyManager();

  /**
   * This method returns the singleton instance of JPAPropertyManager which
   * provides access to all ts.jte properties. This is only called once by the
   * test harness.
   *
   * @param env
   *          - TestEnvironment object from JavaTest
   * @return JPAPropertyManager - singleton property manager object
   */
  public final static JPAPropertyManager getJPAPropertyManager(
      TestEnvironment env) throws Exception {
    jteMgr.setTestEnvironment(env);
    return jteMgr;
  }

  /**
   * This method returns the singleton instance of JPAPropertyManager which
   * provides access to all ts.jte properties. This is only called by the init()
   * method in ManualDeployment.java
   *
   * @param p
   *          - Properties object from JavaTest
   * @return JPAPropertyManager - singleton property manager object
   */
  public final static JPAPropertyManager getJPAPropertyManager(Properties p)
      throws Exception {
    jteMgr.setJteProperties(p);
    return jteMgr;
  }

  public final static JPAPropertyManager getJPAPropertyManager()
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
    String tsHome = getProperty("TS_HOME", null);
    if (tsHome == null) {
      tsHome = getProperty("cts_home", null);
    }
    if (tsHome != null) {
      pTestProps.put("cts_home", tsHome);
    }

    // get persistence tck specific properties
    String mode = getProperty(PMClientBase.MODE_PROP, "standalone");
    String puName = getProperty(PMClientBase.PERSISTENCE_UNIT_NAME_PROP);
    String secondPUName = getProperty(
        PMClientBase.SECOND_PERSISTENCE_UNIT_NAME_PROP);
    String provider = getProperty(PMClientBase.JAVAX_PERSISTENCE_PROVIDER);
    String driver = getProperty(PMClientBase.JAVAX_PERSISTENCE_JDBC_DRIVER);
    String url = getProperty(PMClientBase.JAVAX_PERSISTENCE_JDBC_URL);
    String user = getProperty(PMClientBase.JAVAX_PERSISTENCE_JDBC_USER);
    String password = getProperty(PMClientBase.JAVAX_PERSISTENCE_JDBC_PASSWORD);
    String provider_specific = getProperty(
        PMClientBase.JPA_PROVIDER_IMPLEMENTATION_SPECIFIC_PROPERTIES);
    String errorMsg = "";
    if (mode == null) {
      errorMsg += PMClientBase.MODE_PROP + " is not set in ts.jte. ";
    }
    if (puName == null) {
      errorMsg += PMClientBase.PERSISTENCE_UNIT_NAME_PROP
          + " is not set in ts.jte. ";
    }
    if (secondPUName == null) {
      errorMsg += PMClientBase.SECOND_PERSISTENCE_UNIT_NAME_PROP
          + " is not set in ts.jte. ";
    }
    if (provider == null) {
      errorMsg += PMClientBase.JAVAX_PERSISTENCE_PROVIDER
          + " is not set in ts.jte. ";
    }
    if (driver == null) {
      errorMsg += PMClientBase.JAVAX_PERSISTENCE_JDBC_DRIVER
          + " is not set in ts.jte. ";
    }
    if (url == null) {
      errorMsg += PMClientBase.JAVAX_PERSISTENCE_JDBC_URL
          + " is not set in ts.jte. ";
    }
    if (user == null) {
      errorMsg += PMClientBase.JAVAX_PERSISTENCE_JDBC_USER
          + " is not set in ts.jte. ";
    }
    if (password == null) {
      errorMsg += PMClientBase.JAVAX_PERSISTENCE_JDBC_PASSWORD
          + " is not set in ts.jte. ";
    }
    if (provider_specific == null) {
      errorMsg += PMClientBase.JPA_PROVIDER_IMPLEMENTATION_SPECIFIC_PROPERTIES
          + " is not set in ts.jte. ";
    }
    if (errorMsg.length() > 0) {
      throw new PropertyNotSetException(errorMsg);
    } else {
      pTestProps.put(PMClientBase.MODE_PROP, mode);
      pTestProps.put(PMClientBase.PERSISTENCE_UNIT_NAME_PROP, puName);
      pTestProps.put(PMClientBase.SECOND_PERSISTENCE_UNIT_NAME_PROP,
          secondPUName);
      pTestProps.put(PMClientBase.JAVAX_PERSISTENCE_PROVIDER, provider);
      pTestProps.put(PMClientBase.JAVAX_PERSISTENCE_JDBC_DRIVER, driver);
      pTestProps.put(PMClientBase.JAVAX_PERSISTENCE_JDBC_URL, url);
      pTestProps.put(PMClientBase.JAVAX_PERSISTENCE_JDBC_USER, user);
      pTestProps.put(PMClientBase.JAVAX_PERSISTENCE_JDBC_PASSWORD, password);
      pTestProps.put(
          PMClientBase.JPA_PROVIDER_IMPLEMENTATION_SPECIFIC_PROPERTIES,
          provider_specific);
    }
    pTestProps.put("persistence.second.level.caching.supported",
        getProperty("persistence.second.level.caching.supported", "true"));
    return pTestProps;
  }
}
