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

package com.sun.ts.tests.common.testlogic.ejb.bb.argsemantics;

import java.util.Properties;

import com.sun.ts.lib.util.TSNamingContext;
import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.common.ejb.calleebeans.SimpleArgument;
import com.sun.ts.tests.common.ejb.calleebeans.StatefulCallee;
import com.sun.ts.tests.common.ejb.calleebeans.StatefulCalleeHome;
import com.sun.ts.tests.common.ejb.calleebeans.StatefulCalleeLocal;
import com.sun.ts.tests.common.ejb.calleebeans.StatefulCalleeLocalHome;

public class TestLogic {

    /*
     * Names used for Callee beans lookups.
     */
    public static final String prefix = "java:comp/env/";

    public static final String statefulRemoteLookup = prefix + "ejb/StatefulCalleeRemote";

    public static final String statefulLocalLookup = prefix + "ejb/StatefulCalleeLocal";

    public static final String statefulBiRemoteLookup = prefix + "ejb/StatefulCalleeBothRemote";

    public static final String statefulBiLocalLookup = prefix + "ejb/StatefulCalleeBothLocal";

    /*
     * Expected values for our custom argument.
     */
    public static final int initialValue = 12;

    public static final int modifiedValue = 24;

    public static final int modifiedValue2 = 48;

    private static StatefulCallee ssfCalleeBean = null;

    private static StatefulCalleeLocal ssfCalleeLocalBean = null;

    public static boolean testStatefulRemote(TSNamingContext nctx, Properties props) {

        return testStatefulRemote(statefulRemoteLookup, nctx, props);
    }

    public static boolean testStatefulLocal(TSNamingContext nctx, Properties props) {

        return testStatefulLocal(statefulLocalLookup, nctx, props);
    }

    public static boolean testStatefulBoth(TSNamingContext nctx, Properties props) {

        boolean pass;

        pass = testStatefulRemote(statefulBiRemoteLookup, nctx, props);
        pass &= testStatefulLocal(statefulBiLocalLookup, nctx, props);

        return pass;
    }

    protected static boolean testStatefulRemote(String lookupName, TSNamingContext nctx, Properties props) {

        StatefulCalleeHome home;
        ssfCalleeBean = null;
        SimpleArgument arg;
        boolean pass;

        try {
            arg = new SimpleArgument(initialValue);
            TestUtil.logTrace("[TestLogic] Initial value is " + arg.getValue());

            TestUtil.logTrace("[TestLogic] Looking up Callee " + lookupName + " ...");
            home = (StatefulCalleeHome) nctx.lookup(lookupName, StatefulCalleeHome.class);

            ssfCalleeBean = home.create(props, arg);
            TestUtil.logTrace("[TestLogic] Value after create is " + arg.getValue());

            ssfCalleeBean.call(props, arg);
            TestUtil.logTrace("[TestLogic] Value after business " + "method is " + arg.getValue());

            pass = (arg.getValue() == initialValue);
            if (!pass) {
                TestUtil.logErr("[TestLogic] Argument has been " + "modified to " + arg.getValue());
            }
        } catch (Exception e) {
            pass = false;
            TestUtil.logErr("[TestLogic] Unexpected exception", e);
        }

        return pass;
    }

    protected static boolean testStatefulLocal(String lookupName, TSNamingContext nctx, Properties props) {

        StatefulCalleeLocalHome home;
        ssfCalleeLocalBean = null;
        SimpleArgument arg;
        String msg;
        boolean pass;

        try {
            arg = new SimpleArgument(initialValue);
            TestUtil.logTrace("[TestLogic] Initial value is " + arg.getValue());

            TestUtil.logTrace("[TestLogic] Looking up Callee " + lookupName + " ...");
            home = (StatefulCalleeLocalHome) nctx.lookup(lookupName);

            ssfCalleeLocalBean = home.create(props, arg);
            TestUtil.logTrace("[TestLogic] Value after create is " + arg.getValue());
            pass = (arg.getValue() == modifiedValue);
            if (!pass) {
                msg = "Expected Argument to be set to " + modifiedValue;
                TestUtil.logErr("[TestLogic] " + msg);
                throw new Exception(msg);
            }

            ssfCalleeLocalBean.call(props, arg);
            TestUtil.logTrace("[TestLogic] Value after business " + "method is " + arg.getValue());

            pass = (arg.getValue() == modifiedValue2);
            if (!pass) {
                TestUtil.logErr("[TestLogic] Expected argument to be " + "set to " + modifiedValue2);
            }
        } catch (Exception e) {
            pass = false;
            TestUtil.logErr("[TestLogic] Unexpected exception", e);
        }

        return pass;
    }

    public static void cleanUpStatefulBean() {
        TestUtil.logTrace("cleanUpStatefulBean");
        try {
            if (ssfCalleeBean != null) {
                TestUtil.logTrace("cleanUp Session Stateful Remote Callee Bean");
                ssfCalleeBean.remove();
                ssfCalleeBean = null;
            }

            if (ssfCalleeLocalBean != null) {
                TestUtil.logTrace("cleanUp Session Stateful Local Callee Bean");
                ssfCalleeLocalBean.remove();
                ssfCalleeLocalBean = null;
            }
        } catch (Exception e) {
            TestUtil.logErr("Exception caught trying to remove Stateful Session beans", e);
        }
    }

}
