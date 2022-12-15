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

package com.sun.ts.lib.deliverable.jms;

import com.sun.ts.lib.deliverable.AbstractDeliverable;
import com.sun.ts.lib.deliverable.PropertyManagerInterface;
import com.sun.javatest.TestEnvironment;

import java.util.Map;
import java.util.Properties;
import java.util.Hashtable;

/**
 * This class serves as a well known place for harness, util, and porting
 * classes to retrieve property values.
 * 
 * @author Dianne Jiao
 */
public class JMSDeliverable extends AbstractDeliverable {
  public PropertyManagerInterface createPropertyManager(TestEnvironment te)
      throws Exception {
    return JMSPropertyManager.getJMSPropertyManager(te);
  }

  public PropertyManagerInterface createPropertyManager(Properties p)
      throws Exception {
    return JMSPropertyManager.getJMSPropertyManager(p);
  }

  public PropertyManagerInterface getPropertyManager() throws Exception {
    return JMSPropertyManager.getJMSPropertyManager();
  }

  public boolean supportsAutoDeployment() {
    return false;
  }

  public boolean supportsInterop() {
    return false;
  }

  public Map getValidVehicles() {

    if (htTSValidVehicles == null) {
      // TS hash table
      htTSValidVehicles = new Hashtable();

      // add default values
      htTSValidVehicles.put("tests.service_eetest.vehicles",
          new String[] { "standalone" });

    }
    return htTSValidVehicles;
  }

}
