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

package com.sun.ts.tests.common.vehicle.stateless3;

import java.util.Properties;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.vehicle.web.AltWebVehicleRunner;

public class Stateless3VehicleRunner extends AltWebVehicleRunner {

    public Status run(String[] args, Properties props) {
        Status sTestStatus = null;
        try {
            TestUtil.logTrace("stateless3 runner using AltWebVehicleRunner");
            props.put("persistence.unit.name", "CTS-EM");
            sTestStatus = super.run(args, props);
        } catch (Exception e) {
            TestUtil.logErr("Test failed.", e);
            sTestStatus = Status.failed("Test run in stateless3 vehicle failed.");
        }

        return sTestStatus;
    }
}
