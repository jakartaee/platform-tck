/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.vehicle.stateful3;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.vehicle.VehicleRunnable;
import com.sun.ts.tests.common.vehicle.web.AltWebVehicleRunner;

public class Stateful3VehicleRunner extends AltWebVehicleRunner {

    public Status run(String[] args, Properties props) {
        Status sTestStatus = null;
        try {
            TestUtil.logTrace("stateful3 runner using AltWebVehicleRunner");
            sTestStatus = super.run(args, props);
        } catch (Exception e) {
            TestUtil.logErr("Test failed.", e);
            sTestStatus = Status.failed("Test run in stateful3 vehicle failed.");
        }
        return sTestStatus;
    }
}
