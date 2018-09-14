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

package com.sun.ts.tests.common.vehicle;

import java.util.*;
import com.sun.ts.lib.harness.*;
import com.sun.ts.lib.util.TestUtil;
import com.sun.javatest.*;

public class EmptyVehicleRunner implements VehicleRunnable {

  public Status run(String[] argv, Properties p) {

    ServiceEETest theTestClient;
    Status sTestStatus = Status.passed("");

    // create an instance of the test client and run here
    try {
      Class c = Class.forName(p.getProperty("test_classname"));
      theTestClient = (ServiceEETest) c.newInstance();
      theTestClient.setSharedObject(VehicleClient.getClientSharedObject());
      sTestStatus = theTestClient.run(argv, p);
    } catch (ClassNotFoundException cnfe) {
      TestUtil.logErr("Failed to create the EETest instance", cnfe);
      sTestStatus = Status.failed("Failed to create the EETest instance");
    } catch (InstantiationException ie) {
      TestUtil.logErr("Failed to create the EETest instance", ie);
      sTestStatus = Status.failed("Failed to create the EETest instance");
    } catch (Exception e) {
      TestUtil.logErr("Failed running in a client side vehicle", e);
      sTestStatus = Status.failed("Failed running in a client side vehicle");
    }

    return sTestStatus;
  }
}
