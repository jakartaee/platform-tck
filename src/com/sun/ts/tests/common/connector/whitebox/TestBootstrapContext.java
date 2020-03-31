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

/*
 * $Id$
 */

package com.sun.ts.tests.common.connector.whitebox;

import javax.resource.spi.BootstrapContext;
import javax.resource.spi.work.WorkManager;
import com.sun.ts.tests.common.connector.util.*;
import java.util.Timer;
import javax.resource.spi.XATerminator;
import javax.transaction.xa.Xid;
import javax.resource.spi.UnavailableException;
import javax.transaction.TransactionSynchronizationRegistry;
import com.sun.ts.lib.util.TSNamingContext;

public class TestBootstrapContext {

  private BootstrapContext bsc;

  private WorkManager wrkmgr;

  private Timer timer1;

  private Timer timer2;

  public TestBootstrapContext(BootstrapContext bsc) {
    this.bsc = bsc;
  }

  public void runTests() {
    Debug.trace("Inside TestBootStrapContext.runTests");
    testTimer();
    testXATerminator();
    testIsContextSupported();
    testTransactionSynchronizationRegistry();
    testTSRLookup();
  }

  public void testTSRLookup() {
    try {
      // This lookup must work for JavaEE but is not guaranteed to
      // be supported in JCA standalone environment.
      // the only test that actually checks for this is in the
      // src/com/sun/ts/tests/xa/ee/tsr code which is NOT part of
      // standalone JCA tck.
      TSNamingContext ncxt = new TSNamingContext();
      String tsrStr = "java:comp/TransactionSynchronizationRegistry";
      Object obj = (Object) ncxt.lookup(tsrStr);
      if (obj != null) {
        ConnectorStatus.getConnectorStatus().logState("TSR Lookup Successful");
        Debug.trace("TSR Lookup Successful");
      } else {
        Debug.trace("TSR Null");
      }
    } catch (Throwable ex) {
      Debug.trace("Exception when calling testTSRLookup()");
      Debug.trace(
          "This is okay if JNDI lookup of tsr is not supported in standalone JCA");
      ex.getMessage();
    }
  }

  public void testTransactionSynchronizationRegistry() {
    try {
      // verify server supports TransactionSynchronizationRegistry
      // per spec assertion Connector:SPEC:291
      TransactionSynchronizationRegistry tr = bsc
          .getTransactionSynchronizationRegistry();
      if (tr != null) {
        String str = "getTransactionSynchronizationRegistry supported by Server";
        Debug.trace(str);
        ConnectorStatus.getConnectorStatus().logState(str);
      } else {
        Debug.trace(
            "getTransactionSynchronizationRegistry not supported by Server.");
      }

    } catch (Throwable ex) {
      Debug.trace(
          "Exception when calling getTransactionSynchronizationRegistry()");
      ex.getMessage();
    }
  }

  private void testTimer() {
    try {
      timer1 = bsc.createTimer();
      timer2 = bsc.createTimer();

      Debug.trace("Inside TestBootStrapContext.testTimer()");

      if (timer1 == null || timer2 == null) {
        ConnectorStatus.getConnectorStatus().logState("Timer is Null");
      } else {
        if (timer1.equals(timer2)) {
          ConnectorStatus.getConnectorStatus()
              .logState("Shared Timer Provided by BootstrapContext");
          Debug.trace("Timer is shared or returned the same instance");
        } else {
          ConnectorStatus.getConnectorStatus()
              .logState("New Timer Provided by BootstrapContext");
          Debug.trace("New Timer Provided by BootstrapContext");
        }
      }

    } catch (UnavailableException ex) {

      ConnectorStatus.getConnectorStatus()
          .logState("Timer UnavailableException");
    } catch (java.lang.UnsupportedOperationException uex) {

      ConnectorStatus.getConnectorStatus()
          .logState("Timer UnsupportedOperationException");
    }
  }

  private void testXATerminator() {
    try {
      XATerminator xt = bsc.getXATerminator();
      wrkmgr = bsc.getWorkManager();

      TestWorkManager twm = new TestWorkManager(bsc);

      Xid myid = twm.getXid();
      Xid nestxid = twm.getNestXid();

      if (xt != null) {
        ConnectorStatus.getConnectorStatus()
            .logState("XATerminator is not null");
        Debug.trace("TestBootStrapContext.testXATerminator XID is "
            + myid.getFormatId());
        Debug.trace("TestBootStrapContext.testXATerminator XID is "
            + nestxid.getFormatId());
        xt.commit(myid, true);
        xt.commit(nestxid, true);
        ConnectorStatus.getConnectorStatus().logState("Xid Committed");
        Debug.trace("XATerminator committed xid");
      }
    } catch (Throwable ex) {
      ex.getMessage();
    }
  }

  /*
   * This is used to assist in the verification of assertion Connector:SPEC:208
   * this will check that the server supports all 3 types of inflow context of:
   * TransactionContext, SecurityContext, and HintsContext. This is verified by
   * invoking the servers method of:
   * BootstrapContext.isContextSupported(TIC/SIC/HIC).
   *
   */
  private void testIsContextSupported() {
    try {
      // verify server supports TransactionContext
      Class tic = javax.resource.spi.work.TransactionContext.class;
      boolean b1 = bsc.isContextSupported(tic);
      if (b1) {
        Debug.trace("TransactionContext supported by Server.");
        ConnectorStatus.getConnectorStatus()
            .logState("TransactionContext supported by Server.");
      }

      Class sic = javax.resource.spi.work.SecurityContext.class;
      boolean b2 = bsc.isContextSupported(sic);
      if (b2) {
        Debug.trace("SecurityContext supported by Server.");
        ConnectorStatus.getConnectorStatus()
            .logState("SecurityContext supported by Server.");
      }

      Class hic = javax.resource.spi.work.HintsContext.class;
      boolean b3 = bsc.isContextSupported(hic);
      if (b3) {
        ConnectorStatus.getConnectorStatus()
            .logState("HintsContext supported by Server.");
      }

    } catch (Throwable ex) {
      ex.getMessage();
    }
  }

}
