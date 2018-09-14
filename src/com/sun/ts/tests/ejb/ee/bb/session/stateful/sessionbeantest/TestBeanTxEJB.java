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
 * @(#)TestBeanTxEJB.java	1.21 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateful.sessionbeantest;

import com.sun.ts.tests.ejb.ee.bb.session.util.DBSupport;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import java.rmi.*;
import java.sql.*;
import javax.sql.*;

public class TestBeanTxEJB implements SessionBean, SessionSynchronization {
  private SessionContext sctx = null;

  private Properties harnessProps = null;

  private boolean afterBeginFlag = false;

  private boolean beforeCompletionFlag = false;

  private boolean afterCompletionFlag = false;

  private boolean commitFlag;

  private boolean rollBack;

  private boolean syncLifeCycleFlag = true;

  private boolean syncTestComplete = false;

  private DBSupport db = null;

  private TSNamingContext nctx = null;

  public void ejbCreate(Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    harnessProps = p;
    try {
      TestUtil.logMsg("initialize remote logging");
      TestUtil.init(p);
      TestUtil.logMsg("obtaining naming context");
      nctx = new TSNamingContext();

      TestUtil.logMsg("Initialize DBSupport");
      db = new DBSupport(sctx, harnessProps);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e);
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
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

  public void afterBegin() {
    TestUtil.logTrace("afterBegin");
    if (afterBeginFlag || syncTestComplete)
      return;
    afterBeginFlag = true;
    if (beforeCompletionFlag || afterCompletionFlag)
      syncLifeCycleFlag = false;
  }

  public void beforeCompletion() {
    TestUtil.logTrace("beforeCompletion");
    if (beforeCompletionFlag || syncTestComplete)
      return;
    beforeCompletionFlag = true;
    if (!afterBeginFlag || afterCompletionFlag)
      syncLifeCycleFlag = false;
    if (rollBack) {
      TestUtil.logMsg("Marking transaction for rollback only.");
      sctx.setRollbackOnly();
      rollBack = false;
    }
  }

  public void afterCompletion(boolean committed) {
    TestUtil.logTrace("afterCompletion");
    if (afterCompletionFlag || syncTestComplete)
      return;
    afterCompletionFlag = true;
    if (!afterBeginFlag || !beforeCompletionFlag)
      syncLifeCycleFlag = false;
    commitFlag = committed;
    TestUtil.logMsg("committed=" + committed);
  }

  // ===========================================================
  // TestBeanTx interface (our business methods)

  public void syncTest(boolean b) {
    TestUtil.logTrace("syncTest");
    rollBack = b;
  }

  public void syncTestWithDbConnection(boolean b1, boolean b2) {
    TestUtil.logTrace("syncTestWithDbConnection");
    try {
      dbConnect();
    } catch (Exception e) {
      TestUtil.logErr("Exception on database connection: " + e, e);
      throw new EJBException("database connection failed");
    }
    rollBack = b1;
    if (rollBack && b2) {
      TestUtil.logMsg("Marking transaction for rollback only.");
      sctx.setRollbackOnly();
      rollBack = false;
    }
  }

  public boolean isSyncLifeCycle1(boolean b) {
    TestUtil.logTrace("isSyncLifeCycle1");
    syncTestComplete = true;
    boolean result;
    if (!b) // Container-Managed, the transaction should be committed
      return syncLifeCycleFlag && commitFlag;
    else // Bean-Managed, the transaction should not be committed
      return syncLifeCycleFlag && !commitFlag;
  }

  public boolean isSyncLifeCycle2() {
    TestUtil.logTrace("isSyncLifeCycle2");
    syncTestComplete = true;
    TestUtil.logMsg("syncLifeCycleFlag=" + syncLifeCycleFlag);
    TestUtil.logMsg("commitFlag=" + commitFlag);
    return syncLifeCycleFlag && !commitFlag;
  }

  public boolean isSyncLifeCycle3() {
    TestUtil.logTrace("isSyncLifeCycle3");
    syncTestComplete = true;
    TestUtil.logMsg("syncLifeCycleFlag=" + syncLifeCycleFlag);
    TestUtil.logMsg("commitFlag=" + commitFlag);
    return !syncLifeCycleFlag && !commitFlag;
  }

  public boolean noSyncLifeCycle() {
    TestUtil.logTrace("noSyncLifeCycle");
    if (!afterBeginFlag && !beforeCompletionFlag && !afterCompletionFlag)
      return true;
    else
      return false;
  }

  public void dbUnConnect() {
    TestUtil.logTrace("dbUnConnect");

    try {
      db.closeDBConnection();
      TestUtil.logTrace("closed connection");
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred trying to close connection: " + e, e);
      throw new EJBException(e.getMessage());
    }
  }

  // ===========================================================

  private void dbConnect() {
    TestUtil.logTrace("dbConnect");
    try {
      db.getDBConnection();
    } catch (SQLException e) {
      TestUtil.logErr("SQLException connection to DB");
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    } catch (Exception e) {
      TestUtil.logErr("Exception occurred: " + e, e);
      throw new EJBException(e.getMessage());
    }
  }
}
