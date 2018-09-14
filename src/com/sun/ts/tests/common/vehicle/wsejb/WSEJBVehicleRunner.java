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

/*
 * $Id$
 */

package com.sun.ts.tests.common.vehicle.wsejb;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import com.sun.ts.lib.harness.*;
import com.sun.javatest.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.tests.common.vehicle.*;

public class WSEJBVehicleRunner implements VehicleRunnable {
  public Status run(String[] argv, Properties p) {
    String sVehicle = p.getProperty("vehicle");
    Status sTestStatus = Status.passed("");

    WSEJBVehicleHome home = null;
    String sEJBVehicleJndiName = "";
    WSEJBVehicleRemote ref = null;
    try {
      TSNamingContext jc = new TSNamingContext();
      sEJBVehicleJndiName = "java:comp/env/ejb/WSEJBVehicle";
      home = (WSEJBVehicleHome) jc.lookup(sEJBVehicleJndiName,
          WSEJBVehicleHome.class);
      ref = (WSEJBVehicleRemote) home.create(argv, p);
      TestUtil.logTrace("in wsejbvehicle: home.create() ok; call runTest()");
      sTestStatus = (ref.runTest()).toStatus();
    } catch (Exception e) {
      TestUtil.logErr("Test failed", e);
      sTestStatus = Status.failed("Test run in wsejb vehicle failed");
    } finally {
      if (ref != null) {
        try {
          ref.remove();
        } catch (Exception e2) {
          TestUtil.logHarnessDebug(
              "Exception while trying to remove the EJB Vehicle bean.");
        }
      }
    }
    return sTestStatus;
  }
}
