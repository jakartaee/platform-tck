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

package com.sun.ts.lib.deliverable.el;

import com.sun.javatest.TestEnvironment;
import com.sun.ts.lib.deliverable.AbstractPropertyManager;
import com.sun.ts.lib.deliverable.PropertyNotSetException;

import java.util.Properties;

/**
 * This class serves as a well known place for harness, util, and porting
 * classes to retrieve property values.
 *
 * @author Kyle Grucci, Nick Rodin
 */
public class ELPropertyManager extends AbstractPropertyManager {

  private static ELPropertyManager jteMgr = new ELPropertyManager();

  private ELPropertyManager() {
  }

  /**
   * This method returns the singleton instance of TCKPropertyManager which
   * provides access to all ts.jte properties. This is only called once by the
   * test harness.
   *
   * @param env
   *          - TestEnvironment object from JavaTest
   * @return TCKPropertyManager - singleton property manager object
   */
  public final static ELPropertyManager getTCKPropertyManager(
      TestEnvironment env) throws Exception {
    jteMgr.setTestEnvironment(env);
    return jteMgr;
  }

  /**
   * This method returns the singleton instance of TCKPropertyManager which
   * provides access to all ts.jte properties. This is only called by the init()
   * method in ManualDeployment.java
   *
   * @param p
   *          - Properties object from JavaTest
   * @return TCKPropertyManager - singleton property manager object
   */
  public final static ELPropertyManager getTCKPropertyManager(Properties p)
      throws Exception {
    jteMgr.setJteProperties(p);
    return jteMgr;
  }

  public final static ELPropertyManager getTCKPropertyManager()
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
    String sJtePropVal = "";
    pTestProps.put("porting.ts.url.class.1",
        getProperty("porting.ts.url.class.1"));
    String tsHome = getProperty("TS_HOME", null);
    pTestProps.put("variable.mapper", getProperty("variable.mapper"));

    return pTestProps;
  }
}
