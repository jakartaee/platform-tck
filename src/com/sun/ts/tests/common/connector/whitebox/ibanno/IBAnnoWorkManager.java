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

package com.sun.ts.tests.common.connector.whitebox.ibanno;

import javax.resource.spi.BootstrapContext;
import javax.resource.spi.work.WorkManager;
import javax.resource.spi.work.Work;
import javax.resource.spi.work.ExecutionContext;
import javax.resource.spi.work.WorkException;
import javax.resource.spi.work.TransactionContext;
import com.sun.ts.tests.common.connector.util.*;
import com.sun.ts.tests.common.connector.whitebox.Debug;
import com.sun.ts.tests.common.connector.whitebox.WorkImpl;
import com.sun.ts.tests.common.connector.whitebox.WorkListenerImpl;
import com.sun.ts.tests.common.connector.whitebox.XidImpl;
import javax.transaction.xa.Xid;

public class IBAnnoWorkManager {
  private BootstrapContext bsc = null;

  private WorkManager wmgr;

  private Xid myxid;

  private Xid mynestxid;

  public IBAnnoWorkManager(BootstrapContext val) {
    debug("enterred constructor");
    this.bsc = val;
    this.wmgr = bsc.getWorkManager();

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
          .logState("IBAnnoWorkManager Work Object Submitted");
      debug("IBAnnoWorkManager Work Object Submitted");
    } catch (WorkException we) {
      System.out.println(
          "IBAnnoWorkManager WorkException thrown is " + we.getMessage());
    } catch (Exception ex) {
      System.out
          .println("IBAnnoWorkManager Exception thrown is " + ex.getMessage());
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
      WorkImpl workimpl = new WorkImpl(wmgr);
      TransactionContext tc = startTx();

      Debug.trace("Creating IBAnnoMessageListener");
      XidImpl myid = new XidImpl();
      IBAnnoMessageListener wl = new IBAnnoMessageListener(myid, this.bsc);
      wmgr.doWork(workimpl, 5000, tc, wl);
      ConnectorStatus.getConnectorStatus()
          .logState("TransactionContext Work Object Submitted");
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
    Debug.trace("IBAnnoWorkManager:  " + out);
  }

}
