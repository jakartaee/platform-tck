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

package com.sun.ts.tests.ejb.ee.tx.entity.cmp.bm.TxRN_Exceptions;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txECMPbean.*;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;
import javax.transaction.*;

public class TestBeanEJB implements SessionBean {

  // testProps represent the test specific properties passed in
  // from the test harness.
  private Properties testProps = new Properties();

  // The TSNamingContext abstracts away the underlying distribution protocol.
  private TSNamingContext jctx = null;

  private SessionContext sctx = null;

  // The TxECMPBean variables
  private static final String txECMPBeanRequiresNew = "java:comp/env/ejb/TxRequiresNew";

  private TxECMPBeanHome beanHome = null;

  // Table Name variables
  private String tName1 = null;

  // The requiredEJB methods
  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
  }

  public void ejbCreate(Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate w/Properties");

    try {
      TestUtil.logMsg("Getting Naming Context");
      jctx = new TSNamingContext();

      initLogging(p);

    } catch (Exception e) {
      TestUtil.logErr("Create exception: " + e.getMessage(), e);
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
    TestUtil.logMsg("test1");
    TestUtil.logMsg("Cause an AppException");

    TxECMPBean beanref = null;
    boolean testResult = false;
    UserTransaction ut = null;

    String brand1 = "First brand";
    String brand2 = "Second brand";
    String tempName1, tempName2;

    try {
      TestUtil.logTrace("Looking up the TxECMPBean Home interface of "
          + txECMPBeanRequiresNew);
      beanHome = (TxECMPBeanHome) jctx.lookup(txECMPBeanRequiresNew,
          TxECMPBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txECMPBeanRequiresNew);
      beanref = (TxECMPBean) beanHome.create(tName1, new Integer(1), brand1,
          (float) 1, testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      TestUtil.logTrace("Update brand name and catch AppException");
      ut.begin();
      try {
        beanref.updateBrandName(brand2, TxECMPBeanEJB.FLAGAPPEXCEPTION);
        TestUtil.logTrace("Did not receive expected AppException");
      } catch (AppException ae) {
        TestUtil.logTrace("AppException received as expected.");
        testResult = true;
      }
      ut.commit();
      TestUtil.logTrace("Transaction commited");

      return (testResult);

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanref != null) {
          beanref.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanref: " + e.getMessage(), e);
      }
    }
  }

  public boolean test2() {
    TestUtil.logMsg("test2");
    TestUtil.logMsg("Cause a SystemException");

    TxECMPBean beanref = null;
    boolean t1, t2;
    t1 = t2 = false;
    boolean testResult = false;
    UserTransaction ut = null;

    String brand1 = "First brand";
    String brand2 = "Second brand";
    String tempName1, tempName2;
    Integer key = null;

    try {
      TestUtil.logTrace("Looking up the TxECMPBean Home interface of "
          + txECMPBeanRequiresNew);
      beanHome = (TxECMPBeanHome) jctx.lookup(txECMPBeanRequiresNew,
          TxECMPBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txECMPBeanRequiresNew);
      beanref = (TxECMPBean) beanHome.create(tName1, new Integer(1), brand1,
          (float) 1, testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      // Let's first check that we get our exception thrown
      TestUtil.logTrace("Update brand name and catch RemoteException");
      ut.begin();
      try {
        beanref.updateBrandName(brand2, TxECMPBeanEJB.FLAGSYSEXCEPTION);
        TestUtil.logTrace("Did not receive expected RemoteException");
      } catch (RemoteException re) {
        TestUtil.logTrace("RemoteException received as expected.");
        t1 = true;
      }

      TestUtil.logTrace("Check the transaction status");
      int txStatus = ut.getStatus();
      TestUtil.printTransactionStatus(txStatus);

      if (txStatus == Status.STATUS_ACTIVE) {
        TestUtil.logTrace("Transaction is active");
        t2 = true;
      } else if (txStatus == Status.STATUS_MARKED_ROLLBACK) {
        TestUtil.logTrace("Transaction is marked for rollback");
        t2 = true;
      } else {
        TestUtil.logTrace("Transaction status neither ACTIVE nor "
            + "MARKED FOR ROLLBACK as expected");
      }

      // OK, let's rollback
      if (txStatus != Status.STATUS_NO_TRANSACTION) {
        TestUtil.logTrace("Starting rollback");
        ut.rollback();
        TestUtil.logTrace("Rollback finished");
      } else
        TestUtil.logTrace("No transaction to rollback");

      if (t1 && t2)
        testResult = true;

      return (testResult);

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanref != null) {
          beanref.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanref: " + e.getMessage(), e);
      }
    }
  }

  public boolean test3() {
    TestUtil.logMsg("test3");
    TestUtil.logMsg("Cause an EJBException");

    TxECMPBean beanref = null;
    boolean t1, t2;
    t1 = t2 = false;
    boolean testResult = false;
    UserTransaction ut = null;

    String brand1 = "First brand";
    String brand2 = "Second brand";
    String tempName1, tempName2;
    Integer key = null;

    try {
      TestUtil.logTrace("Looking up the TxECMPBean Home interface of "
          + txECMPBeanRequiresNew);
      beanHome = (TxECMPBeanHome) jctx.lookup(txECMPBeanRequiresNew,
          TxECMPBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txECMPBeanRequiresNew);
      beanref = (TxECMPBean) beanHome.create(tName1, new Integer(1), brand1,
          (float) 1, testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      // Let's first check that we get our exception thrown
      TestUtil.logTrace("Update brand name and catch RemoteException");
      ut.begin();
      try {
        beanref.updateBrandName(brand2, TxECMPBeanEJB.FLAGEJBEXCEPTION);
        TestUtil.logTrace("Did not receive expected RemoteException");
      } catch (RemoteException re) {
        TestUtil.logTrace("RemoteException received as expected.");
        t1 = true;
      }

      // Make sure tx1 is still active. Because the transaction attribute
      // for this TestBean is RequiresNew, a new transaction (t2) is started
      // when a business method of TxECMPBean is called. So it is t2 that is
      // rolled back, not t1.
      TestUtil.logTrace("Check that the transaction is active");
      int txStatus = ut.getStatus();
      TestUtil.printTransactionStatus(txStatus);

      if (txStatus == Status.STATUS_ACTIVE) {
        TestUtil.logTrace("Transaction is active");
        t2 = true;
      } else if (txStatus == Status.STATUS_MARKED_ROLLBACK) {
        TestUtil.logTrace("Transaction is marked for rollback");
        t2 = true;
      } else {
        TestUtil.logTrace("Transaction status neither ACTIVE nor "
            + "MARKED FOR ROLLBACK as expected");
      }

      // OK, let's rollback
      if (txStatus != Status.STATUS_NO_TRANSACTION) {
        TestUtil.logTrace("Starting rollback");
        ut.rollback();
        TestUtil.logTrace("Rollback finished");
      } else
        TestUtil.logTrace("No transaction to rollback");

      if (t1 && t2)
        testResult = true;

      return (testResult);

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanref != null) {
          beanref.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanref: " + e.getMessage(), e);
      }
    }
  }

  public boolean test4() {
    TestUtil.logMsg("test4");
    TestUtil.logMsg("Cause an Error");

    TxECMPBean beanref = null;
    boolean t1, t2;
    t1 = t2 = false;
    boolean testResult = false;
    UserTransaction ut = null;

    String brand1 = "First brand";
    String brand2 = "Second brand";
    String tempName1, tempName2;
    Integer key = null;

    try {
      TestUtil.logTrace("Looking up the TxECMPBean Home interface of "
          + txECMPBeanRequiresNew);
      beanHome = (TxECMPBeanHome) jctx.lookup(txECMPBeanRequiresNew,
          TxECMPBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txECMPBeanRequiresNew);
      beanref = (TxECMPBean) beanHome.create(tName1, new Integer(1), brand1,
          (float) 1, testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      // Let's first check that we get our exception thrown
      TestUtil.logTrace("Update brand name and catch RemoteException");
      ut.begin();
      try {
        beanref.updateBrandName(brand2, TxECMPBeanEJB.FLAGERROR);
        TestUtil.logTrace("Did not receive expected RemoteException");
      } catch (RemoteException re) {
        TestUtil.logTrace("RemoteException received as expected.");
        t1 = true;
      }

      // Make sure tx1 is still active. Because the transaction attribute
      // for this TestBean is RequiresNew, a new transaction (t2) is started
      // when a business method of TxECMPBean is called. So it is t2 that is
      // rolled back, not t1.
      TestUtil.logTrace("Check that the transaction is active");
      int txStatus = ut.getStatus();
      TestUtil.printTransactionStatus(txStatus);

      if (txStatus == Status.STATUS_ACTIVE) {
        TestUtil.logTrace("Transaction is active");
        t2 = true;
      } else if (txStatus == Status.STATUS_MARKED_ROLLBACK) {
        TestUtil.logTrace("Transaction is marked for rollback");
        t2 = true;
      } else {
        TestUtil.logTrace("Transaction status neither ACTIVE nor "
            + "MARKED FOR ROLLBACK as expected");
      }

      // OK, let's rollback
      if (txStatus != Status.STATUS_NO_TRANSACTION) {
        TestUtil.logTrace("Starting rollback");
        ut.rollback();
        TestUtil.logTrace("Rollback finished");
      } else
        TestUtil.logTrace("No transaction to rollback");

      if (t1 && t2)
        testResult = true;

      return (testResult);

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanref != null) {
          beanref.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanref: " + e.getMessage(), e);
      }
    }
  }

  private void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    this.testProps = p;
    try {
      TestUtil.init(p);

      // Get the table names
      this.tName1 = TestUtil
          .getTableName(TestUtil.getProperty("TxEBean_Delete"));
      TestUtil.logTrace("tName1: " + this.tName1);

    } catch (RemoteLoggingInitException e) {
      TestUtil.logErr("RemoteLoggingInitException: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    }
  }

}
