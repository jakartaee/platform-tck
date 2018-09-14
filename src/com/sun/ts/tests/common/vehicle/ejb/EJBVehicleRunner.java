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

package com.sun.ts.tests.common.vehicle.ejb;

import java.io.*;
import java.util.*;
import javax.ejb.*;
import com.sun.javatest.*;
import com.sun.ts.lib.util.*;
import com.sun.ts.tests.common.vehicle.*;
import com.sun.ts.lib.porting.TSLoginContext;

public class EJBVehicleRunner implements VehicleRunnable {
  public Status run(String[] argv, Properties p) {

    Status sTestStatus = Status.passed("");
    String username = p.getProperty("user");
    String password = p.getProperty("password");

    String isSecuredEjbClientValue = p
        .getProperty("secured.ejb.vehicle.client");
    boolean isSecuredEjbClient = (isSecuredEjbClientValue != null);
    TestUtil.logTrace("%%%%%%% isSecuredEjbClient = " + isSecuredEjbClient);

    if (isSecuredEjbClient) {
      try {
        TestUtil.logTrace("Test login in appclient for user " + username
            + " password " + password);
        TSLoginContext loginContext = new TSLoginContext();
        loginContext.login(username, password);
      } catch (Exception e) {
        TestUtil.logErr("login failed", e);
        sTestStatus = Status.failed("Test login in appclient failed for user "
            + username + " password " + password);
      }
    }

    String sVehicle = p.getProperty("vehicle");

    EJBVehicleHome home = null;
    String sEJBVehicleJndiName = "";
    EJBVehicleRemote ref = null;
    try {
      TSNamingContext jc = new TSNamingContext();
      sEJBVehicleJndiName = "java:comp/env/ejb/EJBVehicle";
      home = (EJBVehicleHome) jc.lookup(sEJBVehicleJndiName,
          EJBVehicleHome.class);
      ref = (EJBVehicleRemote) home.create(argv, p);
      TestUtil.logTrace("in ejbvehicle: home.create() ok; call runTest()");
      sTestStatus = (ref.runTest()).toStatus();
    } catch (Exception e) {
      TestUtil.logErr("Test failed", e);
      sTestStatus = Status.failed("Test run in ejb vehicle failed");
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
