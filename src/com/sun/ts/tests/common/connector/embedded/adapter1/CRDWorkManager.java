/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.common.connector.embedded.adapter1;

import javax.resource.spi.BootstrapContext;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.ExecutionContext;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.TransactionContext;
import javax.resource.spi.XATerminator;
import javax.resource.spi.work.SecurityContext;
import com.sun.ts.tests.common.connector.util.*;
import com.sun.ts.tests.common.connector.whitebox.Debug;
import com.sun.ts.tests.common.connector.whitebox.WorkImpl;
import com.sun.ts.tests.common.connector.whitebox.WorkListenerImpl;
import com.sun.ts.tests.common.connector.whitebox.XidImpl;
import javax.transaction.xa.Xid;
import javax.transaction.xa.XAException;

public class CRDWorkManager {
  private BootstrapContext bsc = null;

  private WorkManager wmgr;

  private Xid myxid;

  private Xid mynestxid;

  private XATerminator xa;

  public CRDWorkManager(BootstrapContext val) {
    debug("enterred constructor");
    this.bsc = val;
    this.wmgr = bsc.getWorkManager();
    this.xa = bsc.getXATerminator();

    debug("leaving constructor");
  }

  public void runTests() {
    debug("enterred runTests");
    doWork();
    doTCWork();
    debug("leaving runTests");
  }

  public void doWork() {
    debug("enterred doWork");

    try {
      WorkImpl workimpl = new WorkImpl(wmgr);

      ExecutionContext ec = new ExecutionContext();
      WorkListenerImpl wl = new WorkListenerImpl();
      wmgr.doWork(workimpl, 5000, ec, wl);
      ConnectorStatus.getConnectorStatus()
          .logState("CRDWorkManager Work Object Submitted");
      debug("CRDWorkManager Work Object Submitted");
    } catch (WorkException we) {
      System.out
          .println("CRDWorkManager WorkException thrown is " + we.getMessage());
    } catch (Exception ex) {
      System.out
          .println("CRDWorkManager Exception thrown is " + ex.getMessage());
    }

    debug("leaving doWork");
  }

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

  public void doTCWork() {
    try {
      XidImpl myid = new XidImpl();
      WorkImpl workimpl = new WorkImpl(wmgr);
      TransactionContext tc = startTx();
      tc.setXid(myid);

      // submit a transactional work object
      Debug.trace("Creating WorkListener");
      WorkListenerImpl wl = new WorkListenerImpl();
      wmgr.doWork(workimpl, 5000, tc, wl);

      xa.commit(tc.getXid(), true);
    } catch (XAException xe) {
      Debug.trace("CRDWorkManager.doTCWork():  XAException" + xe.getMessage());
      Debug.printDebugStack(xe);
    } catch (WorkException we) {
      Debug.trace("TestWorkManager Exception thrown is " + we.getMessage());
    }
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
    Debug.trace("CRDWorkManager:  " + out);
  }

}
