/*
 * Copyright (c) 2007, 2024 Oracle and/or its affiliates. All rights reserved.
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

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.vehicle.VehicleRunnable;

public class WSEJBVehicleRunner implements VehicleRunnable {
    public Status run(String[] argv, Properties p) {
        Status sTestStatus = Status.passed("");

        String sEJBVehicleJndiName = "";
        WSEJBVehicleRemote ref = null;
        try {
            TSNamingContext jc = new TSNamingContext();
            sEJBVehicleJndiName = "java:comp/env/ejb/WSEJBVehicle";
            ref = (WSEJBVehicleRemote) jc.lookup(sEJBVehicleJndiName, WSEJBVehicleRemote.class);
            ref.initialize(argv, p);
            TestUtil.logTrace("in wsejbvehicle: initialize ok; call runTest()");
            sTestStatus = (ref.runTest()).toStatus();
        } catch (Exception e) {
            TestUtil.logErr("Test failed", e);
            sTestStatus = Status.failed("Test run in wsejb vehicle failed");
        }
        return sTestStatus;
    }
}
