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
 * @(#)TestBeanEJB.java	1.27 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateful.sessionbeantest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import javax.transaction.*;
import java.rmi.*;

public class TestBeanEJB implements SessionBean {
  // JNDI names for Lookup
  private static final String txRequired = "java:comp/env/ejb/TxRequired";

  private static final String txRequiresNew = "java:comp/env/ejb/TxRequiresNew";

  private static final String txSupports = "java:comp/env/ejb/TxSupports";

  private static final String txMandatory = "java:comp/env/ejb/TxMandatory";

  private static final String txNotSupported = "java:comp/env/ejb/TxNotSupported";

  private static final String txNever = "java:comp/env/ejb/TxNever";

  private SessionContext sctx = null;

  private Properties harnessProps = null;

  private CallBack ref = null;

  private TSNamingContext nctx = null;

  // Proper lifecycle create call order for EJBObject creation.
  // A client invokes home.create(args ...)
  // - newInstance()
  // - setSessionContext(SessionContext ctx)
  // - ejbCreate(args ...)

  private boolean ejbNewInstanceFlag = false;

  private boolean ejbSessionContextFlag = false;

  private boolean ejbCreateFlag = false;

  private boolean createLifeCycleFlag = true;

  private int createMethodCalled; // determine ejbCreate method called

  private boolean beanManagedTransaction = false;

  private UserTransaction ut = null;

  // newInstance() invokes default no-arg constructor for class
  public TestBeanEJB() {
    TestUtil.logTrace("newInstance => default constructor called");
    ejbNewInstanceFlag = true;
    if (ejbSessionContextFlag || ejbCreateFlag)
      createLifeCycleFlag = false;
    if (ejbSessionContextFlag)
      TestUtil.logErr("newInstance() not called before setSessionContext()");
    if (ejbCreateFlag)
      TestUtil.logErr("newInstance() not called before ejbCreate()");
  }

  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
    ejbCreateFlag = true;
    if (!ejbNewInstanceFlag || !ejbSessionContextFlag)
      createLifeCycleFlag = false;
    if (!ejbNewInstanceFlag)
      TestUtil.logErr("newInstance() not called before ejbCreate()");
    if (!ejbSessionContextFlag)
      TestUtil.logErr("setSessionContext() not called before ejbCreate()");
    createMethodCalled = 1;
  }

  public void ejbCreate(CallBack r) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    ref = r;
    ejbCreateFlag = true;
    if (!ejbNewInstanceFlag || !ejbSessionContextFlag)
      createLifeCycleFlag = false;
    if (!ejbNewInstanceFlag)
      TestUtil.logErr("newInstance() not called before ejbCreate()");
    if (!ejbSessionContextFlag)
      TestUtil.logErr("setSessionContext() not called before ejbCreate()");
    createMethodCalled = 2;
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    this.sctx = sc;
    ejbSessionContextFlag = true;
    if (!ejbNewInstanceFlag || ejbCreateFlag)
      createLifeCycleFlag = false;
    if (!ejbNewInstanceFlag)
      TestUtil.logErr("newInstance() not called before setSessionContext()");
    if (ejbCreateFlag)
      TestUtil.logErr("ejbCreate() called before setSessionContext()");
    initNaming();
  }

  public void ejbRemove() {
    TestUtil.logTrace("ejbRemove");
    try {
      if (ref != null) {
        ref.setRemove(true);
      }
      reset();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }

  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public void ping() {
    TestUtil.logTrace("ping");
  }

  public void setCallBack(CallBack r) {
    TestUtil.logTrace("setCallBack");
    ref = r;
  }

  public boolean isCreateLifeCycle1() {
    TestUtil.logTrace("isCreateLifeCycle1");

    boolean status;

    if (createLifeCycleFlag && createMethodCalled == 1)
      status = true;
    else
      status = false;
    reset();
    return status;
  }

  public boolean isCreateLifeCycle2() {
    TestUtil.logTrace("isCreateLifeCycle2");
    boolean status;
    if (createLifeCycleFlag && createMethodCalled == 2)
      status = true;
    else
      status = false;
    reset();
    return status;
  }

  public boolean isSyncLifeCycle1(String tx) {
    TestUtil.logTrace("isSyncLifeCycle1");
    String jndiName = getJndiName(tx);
    try {
      // Get EJB Home ...
      TestUtil.logMsg("Looking up home interface for EJB: " + jndiName);
      TestBeanTxHome beanHome = (TestBeanTxHome) nctx.lookup(jndiName,
          TestBeanTxHome.class);
      TestUtil.logMsg("Create EJB instance");
      TestBeanTx beanRef = (TestBeanTx) beanHome.create(harnessProps);
      beanRef.syncTest(false);
      boolean result = beanRef.isSyncLifeCycle1(beanManagedTransaction);
      return result;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      return false;
    }

  }

  public boolean isSyncLifeCycle2(String tx, boolean openConn, boolean b) {
    TestUtil.logTrace("isSyncLifeCycle2");
    String jndiName = getJndiName(tx);
    try {
      // Get EJB Home ...
      TestUtil.logMsg("Looking up home interface for EJB: " + jndiName);
      TestBeanTxHome beanHome = (TestBeanTxHome) nctx.lookup(jndiName,
          TestBeanTxHome.class);
      TestUtil.logMsg("Create EJB instance");
      TestBeanTx beanRef = (TestBeanTx) beanHome.create(harnessProps);
      if (!openConn)
        beanRef.syncTest(true);
      else
        try {
          beanRef.syncTestWithDbConnection(true, b);
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        }
      ;
      boolean result = beanRef.isSyncLifeCycle2();
      if (openConn)
        beanRef.dbUnConnect();
      return result;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      return false;
    }

  }

  public boolean isSyncLifeCycle3(String tx, boolean openConn, boolean b) {
    TestUtil.logTrace("isSyncLifeCycle3");
    String jndiName = getJndiName(tx);
    try {
      // Get EJB Home ...
      TestUtil.logMsg("Looking up home interface for EJB: " + jndiName);
      TestBeanTxHome beanHome = (TestBeanTxHome) nctx.lookup(jndiName,
          TestBeanTxHome.class);
      TestUtil.logMsg("Create EJB instance");
      TestBeanTx beanRef = (TestBeanTx) beanHome.create(harnessProps);
      if (!openConn)
        beanRef.syncTest(true);
      else
        try {
          beanRef.syncTestWithDbConnection(true, b);
        } catch (Exception e) {
          TestUtil.printStackTrace(e);
        }
      ;
      boolean result = beanRef.isSyncLifeCycle3();
      if (openConn)
        beanRef.dbUnConnect();
      return result;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      return false;
    }

  }

  public boolean noSyncLifeCycle(String tx) {
    TestUtil.logTrace("noSyncLifeCycle");
    String jndiName = getJndiName(tx);
    try {
      // Get EJB Home ...
      TestUtil.logMsg("Looking up home interface for EJB: " + jndiName);
      TestBeanTxHome beanHome = (TestBeanTxHome) nctx.lookup(jndiName,
          TestBeanTxHome.class);
      TestUtil.logMsg("Create EJB instance");
      TestBeanTx beanRef = (TestBeanTx) beanHome.create(harnessProps);
      return beanRef.noSyncLifeCycle();
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean okay(String tx) {
    TestUtil.logTrace("noSyncLifeCycle");
    String jndiName = getJndiName(tx);
    try {
      // Get EJB Home ...
      TestUtil.logMsg("Looking up home interface for EJB: " + jndiName);
      TestBeanNoTxHome beanHome = (TestBeanNoTxHome) nctx.lookup(jndiName,
          TestBeanNoTxHome.class);
      TestUtil.logMsg("Create EJB instance");
      TestBeanNoTx beanRef = (TestBeanNoTx) beanHome.create(harnessProps);
      beanRef.okay();
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean not_okay(String tx) {
    TestUtil.logTrace("noSyncLifeCycle");
    String jndiName = getJndiName(tx);
    try {
      // Get EJB Home ...
      TestUtil.logMsg("Looking up home interface for EJB: " + jndiName);
      TestBeanNoTxHome beanHome = (TestBeanNoTxHome) nctx.lookup(jndiName,
          TestBeanNoTxHome.class);
      TestUtil.logMsg("Create EJB instance");
      TestBeanNoTx beanRef = (TestBeanNoTx) beanHome.create(harnessProps);
      try {
        beanRef.not_okay();
      } catch (TransactionRolledbackException e) {
        TestUtil.logMsg("TransactionRolledbackException received as expected");
        return true;
      } catch (Exception e) {
        TestUtil.logErr("Unexpected exception: " + e, e);
        return false;
      }
      TestUtil.logErr("No Exception");
      return false;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e);
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    harnessProps = p;
    try {
      TestUtil.logMsg("initialize remote logging");
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean beginTransaction() {
    TestUtil.logTrace("beginTransaction");
    try {
      ut = sctx.getUserTransaction();
      ut.begin();
      beanManagedTransaction = true;
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  public boolean commitTransaction() {
    TestUtil.logTrace("commitTransaction");
    try {
      ut.commit();
      beanManagedTransaction = false;
      return true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      return false;
    }
  }

  // ===========================================================

  private String getJndiName(String tx) {
    if (tx.equals("TxRequired"))
      return txRequired;
    else if (tx.equals("TxRequiresNew"))
      return txRequiresNew;
    else if (tx.equals("TxSupports"))
      return txSupports;
    else if (tx.equals("TxNotSupported"))
      return txNotSupported;
    else if (tx.equals("TxMandatory"))
      return txMandatory;
    else if (tx.equals("TxNever"))
      return txNever;
    else
      return null;
  }

  private void reset() {
    ejbSessionContextFlag = false;
    ejbCreateFlag = false;
    createLifeCycleFlag = true;
    createMethodCalled = 0;
  }

  private void initNaming() {
    try {
      TestUtil.logMsg("obtain naming context");
      nctx = new TSNamingContext();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("unable to obtain naming context");
    }
  }
}
