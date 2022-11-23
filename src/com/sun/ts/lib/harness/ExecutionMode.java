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

package com.sun.ts.lib.harness;

import com.sun.ts.lib.deliverable.PropertyManagerInterface;
import com.sun.ts.lib.util.TestUtil;
import java.io.*;
import java.util.*;

public class ExecutionMode {
    public static final int DEPLOY_RUN_UNDEPLOY = 0;

    public static final int DEPLOY = 1;

    public static final int RUN = 2;

    public static final int UNDEPLOY = 3;

    public static final int DEPLOY_RUN = 4;

    public static final int LIST = 5;

    public static final int DEFAULT = DEPLOY_RUN_UNDEPLOY;

    private ExecutionMode() {
    }

    /**
     * gets the current execution mode from PropertyManagerInterface or from a system property if overridden on the
     * commandline. Note that current execution mode is not cached since harness.executeMode property may change between
     * test executions.
     *
     * @param propMgr an implementation of PropertyManagerInterface.
     * @return an int representing one of the 5 modes
     */
    public static int getExecutionMode(PropertyManagerInterface propMgr) {
        int mode = (Integer.getInteger("harness.executeMode", -1)).intValue();

        if (mode == -1) {
            if (propMgr != null) {
                String modeS = propMgr.getProperty("harness.executeMode", "");
                try {
                    mode = Integer.parseInt(modeS);
                } catch (Exception e) {
                    mode = DEFAULT;
                }
            } else {
                throw new Error("PropertyManager is null.  Please pass in a valid PropertyManager");
            }
        }

        if (mode < DEPLOY_RUN_UNDEPLOY || mode > LIST) {
            TestUtil.logHarness(
                    "harness.executeMode in ts.jte: " + mode + " is not valid. Will use default " + DEFAULT + ".");
            mode = DEFAULT;
        }

        TestUtil.logHarness("harness.executeMode is set to \"" + mode + "\"");
        return mode;
    }
}
