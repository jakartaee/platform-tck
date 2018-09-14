/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

public class AnnoWorkManager {
  private BootstrapContext bsc = null;

  private WorkManager wmgr;

  private Xid myxid;

  private Xid mynestxid;

  private XATerminator xa;

  public AnnoWorkManager(BootstrapContext val) {
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
    submitNestedXidWork();
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
          .logState("AnnoWorkManager Work Object Submitted");
      debug("AnnoWorkManager Work Object Submitted");
    } catch (WorkException we) {
      System.out.println(
          "AnnoWorkManager WorkException thrown is " + we.getMessage());
    } catch (Exception ex) {
      System.out
          .println("AnnoWorkManager Exception thrown is " + ex.getMessage());
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

  /*
   * This will be used to help verify assertion Connector:SPEC:55 from the
   * annotation point of view.
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
      ConnectorStatus.getConnectorStatus()
          .logState("TransactionContext Work Object Submitted");

      xa.commit(tc.getXid(), true);
    } catch (XAException xe) {
      Debug.trace("AnnoWorkManager.doTCWork():  XAException" + xe.getMessage());
      Debug.trace("AnnoWorkManager XAException.toString() = " + xe.toString());
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

  /*
   * This is used to help verify assertion: Connector:SPEC:210 While the spec is
   * clear to state that nested work contexts are to be supported, it appears
   * there may be some grey area wrt nested work objects with transaction
   * contexts. It may be the case that nested transaction contexts may not be
   * clearly defined in the connector 1.6 spec - so we will test nested work
   * objs where only 1 of the work objs has transactioncontext.
   */
  public void submitNestedXidWork() {
    try {
      XidImpl myid = new XidImpl();
      NestedWorkXid1 workid = new NestedWorkXid1(wmgr, myid,
          NestedWorkXid1.ContextType.EXECUTION_CONTEXT);

      // setting up the xid so it can be commited later.
      setNestXid(myid);

      TransactionContext tc = startTx();
      tc.setXid(myid);
      wmgr.doWork(workid, wmgr.INDEFINITE, tc, null);

      Debug.trace("Anno based NestedWorkXid1 Submitted");
      ConnectorStatus.getConnectorStatus()
          .logState("anno based NestedWorkXid1 parent context submitted");
      xa.commit(tc.getXid(), true);

    } catch (XAException xe) {
      Debug.trace("AnnoWorkManager.submitNestedXidWork():  XAException"
          + xe.getMessage());
      Debug.trace("AnnoWorkManager XAException.toString() = " + xe.toString());
      Debug.printDebugStack(xe);
    } catch (WorkException we) {
      Debug.printDebugStack(we);
    } catch (Exception ex) {
      Debug.printDebugStack(ex);
    }
  }

  public void debug(String out) {
    Debug.trace("AnnoWorkManager:  " + out);
  }

}
