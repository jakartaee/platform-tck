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

package com.sun.ts.tests.common.vehicle.appmanaged;

import java.util.Properties;

import javax.naming.InitialContext;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;

import com.sun.ts.lib.harness.Status;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.vehicle.web.AltWebVehicleRunner;

/**
 * The {@link com.sun.ts.tests.common.vehicle.VehicleRunnable} implementation for the
 * {@link com.sun.ts.tests.common.vehicle.VehicleType#appmanaged} vehicle. This uses the
 * {@link com.sun.ts.tests.common.vehicle.web.AltWebVehicleRunner} to run the tests in the server using the
 * {@link AppManagedServletVehicle}.
 */
public class AppManagedVehicleRunner extends AltWebVehicleRunner {
    public static final String APPMANAGED_REF_NAME = "java:comp/env/ejb/AppManagedVehicleBean";

    public Status run(String[] args, Properties props) {
        Status sTestStatus = null;
        try {
            TestUtil.logTrace("application-managed JTA runner using AltWebVehicleRunner");
            sTestStatus = super.run(args, props);
        } catch (Exception e) {
            TestUtil.logErr("Test failed.", e);
            sTestStatus = Status.failed("Test run in application-managed JTA vehicle failed.");
        }
        return sTestStatus;
    }

    private void dumpJndi(String s, InitialContext jc) {
        try {
            dumpTreeEntry(jc, jc.list(s), s);
        } catch (Exception ignore) {
        }
    }

    private void dumpTreeEntry(InitialContext jc, NamingEnumeration<NameClassPair> list, String s) throws NamingException {
        System.out.println("\n1. AppManagedVehicleRunner jndi dump walking down tree branch name = " + s);
        while (list.hasMore()) {
            NameClassPair ncp = list.next();
            System.out.println("2. AppManagedVehicleRunner jndi dump (show name + classname pair): " + ncp.toString());
            if (s.length() == 0) {
                dumpJndi(ncp.getName(), jc);
            } else {
                dumpJndi(s + "/" + ncp.getName(), jc);
            }
        }
    }

}
