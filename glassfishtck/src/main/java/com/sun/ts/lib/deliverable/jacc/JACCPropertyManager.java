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

package com.sun.ts.lib.deliverable.jacc;

import com.sun.ts.lib.deliverable.*;
import com.sun.javatest.TestEnvironment;
import java.util.Properties;

public class JACCPropertyManager extends AbstractPropertyManager {

  private static JACCPropertyManager jteMgr = new JACCPropertyManager();

  /**
   * This method returns the singleton instance of JACCPropertyManager which
   * provides access to all ts.jte properties. This is only called once by the
   * test harness.
   *
   * @param env
   *          - TestEnvironment object from JavaTest
   * @return JACCPropertyManager - singleton property manager object
   */

  public final static JACCPropertyManager getJACCPropertyManager(
      TestEnvironment env) throws Exception {
    jteMgr.setTestEnvironment(env);
    return jteMgr;
  }

  /**
   * This method returns the singleton instance of JACCPropertyManager which
   * provides access to all ts.jte properties. This is only called by the init()
   * method in ManualDeployment.java
   *
   * @param p
   *          - Properties object from JavaTest
   * @return JACCPropertyManager - singleton property manager object
   */
  public final static JACCPropertyManager getJACCPropertyManager(Properties p)
      throws Exception {
    jteMgr.setJteProperties(p);
    return jteMgr;
  }

  public final static JACCPropertyManager getJACCPropertyManager()
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
    if (tsHome == null)
      tsHome = getProperty("ts_home", null);
    if (tsHome != null)
      pTestProps.put("ts_home", tsHome);

    return pTestProps;
  }
}
