/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.connector.whitebox.annotated;

import javax.transaction.xa.XAException;
import javax.transaction.xa.Xid;

import com.sun.ts.tests.common.connector.util.ConnectorStatus;
import com.sun.ts.tests.common.connector.whitebox.Debug;
import com.sun.ts.tests.common.connector.whitebox.WorkImpl;
import com.sun.ts.tests.common.connector.whitebox.WorkListenerImpl;
import com.sun.ts.tests.common.connector.whitebox.XidImpl;

import jakarta.resource.spi.BootstrapContext;
import jakarta.resource.spi.XATerminator;
import jakarta.resource.spi.work.ExecutionContext;
import jakarta.resource.spi.work.TransactionContext;
import jakarta.resource.spi.work.WorkException;
import jakarta.resource.spi.work.WorkManager;

/**
 * AnnoWorkManager is a class that manages work submissions and transactions using the Jakarta Resource SPI WorkManager
 * and XATerminator.
 */
public class AnnoWorkManager {
    private BootstrapContext bsc = null;
    private WorkManager wmgr;
    private Xid myxid;
    private Xid mynestxid;
    private XATerminator xa;

    /**
     * Constructor for AnnoWorkManager.
     *
     * @param val the BootstrapContext to initialize the WorkManager and XATerminator
     */
    public AnnoWorkManager(BootstrapContext val) {
        debug("entered constructor");
        this.bsc = val;
        this.wmgr = bsc.getWorkManager();
        this.xa = bsc.getXATerminator();
        debug("leaving constructor");
    }

    /**
     * Runs a series of tests including work submission and transaction context work.
     */
    public void runTests() {
        debug("entered runTests");
        doWork();
        doTCWork();
        submitNestedXidWork();
        debug("leaving runTests");
    }

    /**
     * Submits a work object to the WorkManager.
     */
    public void doWork() {
        debug("entered doWork");

        try {
            WorkImpl workimpl = new WorkImpl(wmgr);
            ExecutionContext ec = new ExecutionContext();
            WorkListenerImpl wl = new WorkListenerImpl();
            wmgr.doWork(workimpl, 5000, ec, wl);
            ConnectorStatus.getConnectorStatus().logState("AnnoWorkManager Work Object Submitted");
            debug("AnnoWorkManager Work Object Submitted");
        } catch (WorkException we) {
            System.out.println("AnnoWorkManager WorkException thrown is " + we.getMessage());
        } catch (Exception ex) {
            System.out.println("AnnoWorkManager Exception thrown is " + ex.getMessage());
        }

        debug("leaving doWork");
    }

    /**
     * Starts a new transaction context.
     *
     * @return the initialized TransactionContext
     */
    private TransactionContext startTx() {
        TransactionContext tc = new TransactionContext();
        try {
            Xid xid = new XidImpl();
            tc.setXid(xid);
            tc.setTransactionTimeout(5 * 1000); // 5 seconds
        } catch (Exception ex) {
            Debug.printDebugStack(ex);
        }
        return tc;
    }

    /**
     * Submits a work object with a transaction context to the WorkManager.
     */
    public void doTCWork() {
        try {
            XidImpl myid = new XidImpl();
            WorkImpl workimpl = new WorkImpl(wmgr);
            TransactionContext tc = startTx();
            tc.setXid(myid);

            Debug.trace("Creating WorkListener");
            WorkListenerImpl wl = new WorkListenerImpl();
            wmgr.doWork(workimpl, 5000, tc, wl);
            ConnectorStatus.getConnectorStatus().logState("TransactionContext Work Object Submitted");

            xa.commit(tc.getXid(), true);
        } catch (XAException xe) {
            Debug.trace("AnnoWorkManager.doTCWork():  XAException" + xe.getMessage());
            Debug.trace("AnnoWorkManager XAException.toString() = " + xe.toString());
            Debug.printDebugStack(xe);
        } catch (WorkException we) {
            Debug.trace("TestWorkManager Exception thrown is " + we.getMessage());
        }
    }

    /**
     * Sets the Xid for this AnnoWorkManager.
     *
     * @param xid the Xid to set
     */
    public void setXid(Xid xid) {
        this.myxid = xid;
    }

    /**
     * Gets the Xid for this AnnoWorkManager.
     *
     * @return the current Xid
     */
    public Xid getXid() {
        return this.myxid;
    }

    /**
     * Sets the nested Xid for this AnnoWorkManager.
     *
     * @param xid the nested Xid to set
     */
    public void setNestXid(Xid xid) {
        this.mynestxid = xid;
    }

    /**
     * Gets the nested Xid for this AnnoWorkManager.
     *
     * @return the current nested Xid
     */
    public Xid getNestXid() {
        return this.mynestxid;
    }

    /**
     * Submits nested work objects where only one of the work objects has a transaction context.
     */
    public void submitNestedXidWork() {
        try {
            XidImpl myid = new XidImpl();
            NestedWorkXid1 workid = new NestedWorkXid1(wmgr, myid, NestedWorkXid1.ContextType.EXECUTION_CONTEXT);

            // setting up the xid so it can be committed later.
            setNestXid(myid);

            TransactionContext tc = startTx();
            tc.setXid(myid);
            wmgr.doWork(workid, wmgr.INDEFINITE, tc, null);

            Debug.trace("Anno based NestedWorkXid1 Submitted");
            ConnectorStatus.getConnectorStatus().logState("anno based NestedWorkXid1 parent context submitted");
            xa.commit(tc.getXid(), true);

        } catch (XAException xe) {
            Debug.trace("AnnoWorkManager.submitNestedXidWork():  XAException" + xe.getMessage());
            Debug.trace("AnnoWorkManager XAException.toString() = " + xe.toString());
            Debug.printDebugStack(xe);
        } catch (WorkException we) {
            Debug.printDebugStack(we);
        } catch (Exception ex) {
            Debug.printDebugStack(ex);
        }
    }

    /**
     * Logs debug messages.
     *
     * @param out the debug message to log
     */
    public void debug(String out) {
        Debug.trace("AnnoWorkManager:  " + out);
    }
}