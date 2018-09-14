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
 * @(#)TestBeanEJB.java	1.12 03/05/16
 */

package com.sun.ts.tests.interop.tx.session.stateful.bm.TxN_GlobalSingle;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txbean.*;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;
import javax.transaction.*;

public class TestBeanEJB implements SessionBean {

  // beanProps represent the bean specific properties of the TestBean
  // testProps represent the test specific properties passed in
  // from the test harness.
  private Properties testProps = null;

  // The TSNamingContext abstracts away the underlying distribution protocol.
  private TSNamingContext jctx = null;

  private SessionContext sctx = null;

  private String tName1 = null;

  // The TxBean variables
  private static final String txBeanNever = "java:comp/env/ejb/TxNever";

  private TxBeanHome beanHome = null;

  private TxBean beanRef = null;

  // The requiredEJB methods
  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      TestUtil.logMsg("Getting Naming Context");
      jctx = new TSNamingContext();

      TestUtil.logMsg("Looking up the TxBean Home interface of " + txBeanNever);
      beanHome = (TxBeanHome) jctx.lookup(txBeanNever, TxBeanHome.class);

    } catch (Exception e) {
      TestUtil.logErr("Create exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    }
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    this.sctx = sc;
  }

  public void ejbRemove() {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public boolean test1() {
    TestUtil.logTrace("test1");
    TestUtil.logTrace("Ensure java.rmi.RemoteException is thrown for TX_NEVER");

    boolean testResult = false;
    String tName = this.tName1;
    UserTransaction ut = null;

    try {
      TestUtil.logTrace("Creating EJB instance of " + txBeanNever);
      beanRef = (TxBean) beanHome.create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      TestUtil.logTrace("BEGIN transaction");
      ut.begin();

      int level = beanRef.getDefaultTxIsolationLevel(tName);

      TestUtil.logErr("Did not get expected RemoteException");
      testResult = false;
      ut.rollback();
    } catch (java.rmi.RemoteException re) {
      TestUtil.logTrace("Got expected RemoteException");
      testResult = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    this.testProps = p;
    try {
      TestUtil.init(p);
      // Get the table names
      this.tName1 = TestUtil
          .getTableName(TestUtil.getProperty("TxBean_Tab1_Delete"));
      TestUtil.logTrace("tName1: " + this.tName1);

    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

}
