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

import com.sun.ts.lib.deliverable.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.DeploymentInfo;
import com.sun.javatest.*;
import java.util.*;
import java.io.*;

/**
 * This class serves as a place for JAXR Deliverable specific info.
 *
 * @author Kyle Grucci
 */
public class JAXRDeliverable extends AbstractDeliverable {

  public PropertyManagerInterface createPropertyManager(TestEnvironment te)
      throws Exception {
    return JAXRPropertyManager.getJAXRPropertyManager(te);
  }

  public PropertyManagerInterface createPropertyManager(Properties p)
      throws Exception {
    return JAXRPropertyManager.getJAXRPropertyManager(p);
  }

  public PropertyManagerInterface getPropertyManager() throws Exception {
    return JAXRPropertyManager.getJAXRPropertyManager();
  }

  public Map getValidVehicles() {
    super.getValidVehicles();
    htTSValidVehicles.put("tests_jaxr.service_eetest.vehicles",
        new String[] { "ejb", "servlet", "jsp", "appclient" });
    return htTSValidVehicles;
  }

  public Map getInteropDirections() {
    super.getInteropDirections();
    return htValidRunDirections;
  }

  public boolean supportsInterop() {
    return false;
  }

  public boolean supportsAutoDeployment() {
    return false;
  }
}
