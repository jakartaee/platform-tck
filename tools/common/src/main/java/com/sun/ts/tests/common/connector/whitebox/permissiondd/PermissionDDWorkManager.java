/*
 * Copyright (c) 2014, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.connector.whitebox.permissiondd;

import java.io.FilePermission;
import java.net.SocketPermission;
import java.security.AccessControlException;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedExceptionAction;
import java.util.PropertyPermission;

import javax.transaction.xa.Xid;

import com.sun.ts.tests.common.connector.util.ConnectorStatus;
import com.sun.ts.tests.common.connector.whitebox.Debug;

import jakarta.resource.spi.BootstrapContext;
import jakarta.resource.spi.work.WorkManager;

public class PermissionDDWorkManager {
    private BootstrapContext bsc = null;

    private WorkManager wmgr;

    private Xid myxid;

    private Xid mynestxid;

    public PermissionDDWorkManager(BootstrapContext val) {
        debug("enterred constructor");
        this.bsc = val;
        this.wmgr = bsc.getWorkManager();

        debug("leaving constructor");
    }

    public void runTests() {
        debug("enterred runTests");

        validateRequiredPermSet();
        validateRestrictedLocalPerm();

        // doWork();
        // doTCWork();
        // submitNestedXidWork();
        debug("leaving runTests");
    }

    public void validateRequiredPermSet() {
        try {
            RuntimePermission rtperm = new RuntimePermission("loadLibrary.*");
            doCheckPermission(rtperm);
            debug("validateRequiredPermSet():  valid perm for: " + rtperm.toString());

            RuntimePermission rtperm2 = new RuntimePermission("queuePrintJob");
            doCheckPermission(rtperm2);
            debug("validateRequiredPermSet():  valid perm for: " + rtperm2.toString());

            SocketPermission socperm = new SocketPermission("*", "connect");
            doCheckPermission(socperm);
            debug("validateRequiredPermSet():  valid perm for: " + socperm.toString());

            FilePermission fperm = new FilePermission("*", "read");
            doCheckPermission(fperm);
            debug("validateRequiredPermSet():  valid perm for: " + fperm.toString());

            PropertyPermission pperm = new PropertyPermission("*", "read");
            doCheckPermission(pperm);
            debug("validateRequiredPermSet():  valid perm for: " + pperm.toString());

            // if we have perms we should get here
            debug("SUCCESS:  validateRequiredPermSet passed.");
            ConnectorStatus.getConnectorStatus().logState("SUCCESS:  validateRequiredPermSet passed.");
        } catch (AccessControlException ex) {
            debug("FAILURE:  validateRequiredPermSet throwing AccessControlException.");
            ConnectorStatus.getConnectorStatus().logState("FAILURE:  validateRequiredPermSet throwing AccessControlException.");
            Debug.printDebugStack(ex);
        } catch (Exception ex) {
            debug("FAILURE:  validateRequiredPermSet had unexpected Exception.");
            ConnectorStatus.getConnectorStatus().logState("FAILURE:  validateRequiredPermSet had unexpected Exception.");
            Debug.printDebugStack(ex);
        }

        debug("returning from validateRequiredPermSet()");
        return;
    }

    public void validateRestrictedLocalPerm() {
        try {
            // call a priviledged method
            PropertyPermission readPropertyPerm = new PropertyPermission("TestPropertyPerm", "read");

            try {
                doCheckPermission(readPropertyPerm);
                // should get here
                debug("SUCCESS:  validateRestrictedLocalPerm() has grant for read of TestPropertyPerm");
            } catch (AccessControlException ex) {
                // should not get here.
                debug("FAILURE:  validateRestrictedLocalPerm() threw unexpected exception for read of TestPropertyPerm.");
                ConnectorStatus.getConnectorStatus().logState("FAILURE:  validateRestrictedLocalPerm() threw AccessControlException.");
                Debug.printDebugStack(ex);
                return;
            }
            debug("SUCCESS:  validateRestrictedLocalPerm passed.");
            ConnectorStatus.getConnectorStatus().logState("SUCCESS:  validateRestrictedLocalPerm passed.");

        } catch (Exception ex) {
            debug("FAILURE:  validateRestrictedLocalPerm had unexpected exception.");
            ConnectorStatus.getConnectorStatus().logState("FAILURE:  validateRestrictedLocalPerm had unexpected exception.");
            Debug.printDebugStack(ex);
        }

        debug("returning from validateRestrictedLocalPerm()");
        return;
    }

    public void doCheckPermission(Permission pp) throws Exception {
        final Permission perm = pp;
        AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
            public Void run() throws AccessControlException {
                AccessController.checkPermission(perm);
                return null;
            }
        });
    }

    public void setXid(Xid xid) {
        this.myxid = xid;
    }

    public Xid getXid() {
        return this.myxid;
    }

    public void setNestXid(Xid xid) {
        this.mynestxid = xid;
    }

    public Xid getNestXid() {
        return this.mynestxid;
    }

    public void debug(String out) {
        Debug.trace("PermissionDDWorkManager:  " + out);
    }

}
