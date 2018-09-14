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

package com.sun.ts.tests.ejb.ee.tx.entity.bmp.bm.TxR_Exceptions;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txEbean.*;

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

  // The TxEBean variables
  private static final String txEBeanRequired = "java:comp/env/ejb/TxRequired";

  private TxEBeanHome beanHome = null;

  // Table Name variables
  private String tName1 = null;

  // The requiredEJB methods
  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
  }

  public void ejbCreate(Properties p) throws CreateException {
    TestUtil.logTrace("ejbCreate w/Properties");

    try {
      initLogging(p);
      TestUtil.logTrace("Call to initLogging DONE");
    } catch (Exception e) {
      TestUtil.logErr("Exception from initLogging - TestBean:" + e.getMessage(),
          e);
    }

    try {
      TestUtil.logMsg("Getting Naming Context");
      jctx = new TSNamingContext();

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

    TxEBean beanref = null;
    boolean testResult = false;
    UserTransaction ut = null;

    String brand1 = "First brand";
    String brand2 = "Second brand";
    String tempName1, tempName2;

    try {
      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanRequired);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired, TxEBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEBeanRequired);
      beanref = (TxEBean) beanHome.create(tName1, 1, brand1, (float) 1,
          testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      TestUtil.logTrace("Update brand name and catch AppException");
      ut.begin();
      try {
        beanref.updateBrandName(brand2, TxEBeanEJB.FLAGAPPEXCEPTION);
        TestUtil.logTrace("Did not receive AppException as expected");
      } catch (AppException ae) {
        TestUtil.logTrace("AppException received as expected.");
        testResult = true;
      }
      ut.commit();
      TestUtil.logTrace("Transaction commited");

      return (testResult);

    } catch (Exception e) {
      try {
        if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
          ut.rollback();
      } catch (SystemException se) {
        TestUtil.logErr(
            "Exception checking transaction status: " + se.getMessage(), se);
      }

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

    TxEBean beanref = null;
    boolean t1, t2;
    t1 = t2 = false;
    boolean testResult = false;
    UserTransaction ut = null;

    String brand1 = "First brand";
    String brand2 = "Second brand";
    String tempName1, tempName2;
    Integer key = null;

    try {
      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanRequired);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired, TxEBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEBeanRequired);
      beanref = (TxEBean) beanHome.create(tName1, 1, brand1, (float) 1,
          testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      // Let's first check that we get our exception thrown
      TestUtil.logTrace(
          "Update brand name and catch TransactionRolledbackException");
      ut.begin();
      try {
        beanref.updateBrandName(brand2, TxEBeanEJB.FLAGSYSEXCEPTION);
        TestUtil.logTrace(
            "Did not receive TransactionRolledbackException as expected");
      } catch (TransactionRolledbackException re) {
        TestUtil
            .logTrace("TransactionRolledbackException received as expected.");
        t1 = true;
      }

      // Make sure tx was marked for rollback
      TestUtil.logTrace("Check that the transaction was marked for rollback");
      int txStatus = ut.getStatus();
      if (txStatus == Status.STATUS_MARKED_ROLLBACK) {
        TestUtil.logTrace("Transaction is marked for rollback");
        TestUtil.printTransactionStatus(txStatus);
        t2 = true;
      } else {
        TestUtil.logTrace("Transaction not marked for rollback as expected");
      }

      // OK, let's rollback
      TestUtil.logTrace("Starting rollback");
      ut.rollback();
      TestUtil.logTrace("Rollback finished");

      if (t1 && t2)
        testResult = true;

      return (testResult);

    } catch (Exception e) {
      try {
        if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
          ut.rollback();
      } catch (SystemException se) {
        TestUtil.logErr(
            "Exception checking transaction status: " + se.getMessage(), se);
      }

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
    TestUtil.logMsg("Cause a CreateException");

    UserTransaction ut = null;
    TxEBean beanref1, beanref2, beanref3;
    beanref1 = beanref2 = beanref3 = null;
    boolean testResult = false;

    String brand1 = "First brand";
    String brand2 = "Second brand";
    int key1 = 1;
    int key2 = 2;

    try {
      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanRequired);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired, TxEBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEBeanRequired);
      beanref1 = (TxEBean) beanHome.create(tName1, key1, brand1, (float) 1,
          testProps);

      beanref2 = (TxEBean) beanHome.create(tName1, key2, brand2, (float) 1,
          testProps);

      // Now let's try to create the third bean instance with the same key and
      // force a CreateException

      ut = sctx.getUserTransaction();
      TestUtil.logTrace("Starting User Transaction");
      ut.begin();
      try {
        beanref3 = (TxEBean) beanHome.create(tName1, key2, brand2, (float) 1,
            testProps);
        TestUtil.logTrace("Did not get CreateException as expected");
      } catch (CreateException re) {
        TestUtil.logTrace("Got CreateException as expected");
        testResult = true;
      }

      // OK, let's commit to force the exception
      TestUtil.logTrace("Starting commit");
      ut.commit();
      TestUtil.logTrace("Commit finished");

      return (testResult);

    } catch (Exception e) {
      try {
        if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
          ut.rollback();
      } catch (SystemException se) {
        TestUtil.logErr(
            "Exception checking transaction status: " + se.getMessage(), se);
      }

      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanref1 != null) {
          beanref1.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanref1: " + e.getMessage(), e);
      }
      try {
        if (beanref2 != null) {
          beanref2.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanref2: " + e.getMessage(), e);
      }
      try {
        if (beanref3 != null) {
          beanref3.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanref3: " + e.getMessage(), e);
      }
    }
  }

  public boolean test5() {
    TestUtil.logMsg("test5");
    TestUtil.logMsg("Cause a FinderException");

    TxEBean beanref1, beanref2;
    UserTransaction ut = null;
    beanref1 = beanref2 = null;
    boolean testResult = false;
    ArrayList al = new ArrayList();

    String brand1 = "First brand";
    String brand2 = "Second brand";
    int key1 = 1;
    int key2 = 2;

    try {
      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanRequired);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired, TxEBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEBeanRequired);

      // Create two instances, delete the second instance, then try and
      // find it with a Finder method
      beanref1 = (TxEBean) beanHome.create(tName1, key1, brand1, (float) 1,
          testProps);

      // Start a transaction
      ut = sctx.getUserTransaction();
      TestUtil.logTrace("Starting User Transaction");
      ut.begin();

      beanref2 = (TxEBean) beanHome.create(tName1, key2, brand1, (float) 1,
          testProps);
      try {
        if (beanref2 != null) {
          beanref2.remove();
          TestUtil.logTrace("beanRef2 sucessfully removed");
        }
      } catch (RemoveException re) {
        TestUtil.logErr("Could not remove beanref2: " + re.getMessage(), re);
      }
      beanref2 = null;

      try {
        TestUtil.logTrace("Trying to find by brand name");
        al = (ArrayList) beanHome.findByBrandName(tName1, brand2, testProps);
        TestUtil.logTrace("Did not receive expected FinderException");
      } catch (FinderException fe) {
        TestUtil.logTrace("Caught expected FinderException: " + fe.getMessage(),
            fe);
        testResult = true;
      }

      // OK, let's rollback
      TestUtil.logTrace("Starting rollback");
      ut.rollback();
      TestUtil.logTrace("Rollback finished");

      return (testResult);

    } catch (Exception e) {
      try {
        if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
          ut.rollback();
      } catch (SystemException se) {
        TestUtil.logErr(
            "Exception checking transaction status: " + se.getMessage(), se);
      }

      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanref1 != null) {
          beanref1.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanref1: " + e.getMessage(), e);
      }
    }
  }

  public boolean test6() {
    TestUtil.logMsg("test6");
    TestUtil.logMsg("Cause an ObjectNotFoundException");

    TxEBean beanref, beanref2;
    UserTransaction ut = null;
    beanref = beanref2 = null;
    boolean testResult = false;

    String brand1 = "First brand";
    int key1 = 1;

    try {
      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanRequired);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired, TxEBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEBeanRequired);

      beanref = (TxEBean) beanHome.create(tName1, key1, brand1, (float) 1,
          testProps);

      // Start a transaction
      ut = sctx.getUserTransaction();
      TestUtil.logTrace("Starting User Transaction");
      ut.begin();

      try {
        beanref2 = beanHome.findtxEbean(tName1, new Integer(26130), testProps);
        TestUtil.logTrace("Did not recieve expected ObjectNotFoundException");
      } catch (ObjectNotFoundException onf) {
        TestUtil.logTrace("Received Expected ObjectNotFoundException");
        testResult = true;
      }

      // OK, let's rollback
      TestUtil.logTrace("Starting rollback");
      ut.rollback();
      TestUtil.logTrace("Rollback finished");

      return (testResult);

    } catch (Exception e) {
      try {
        if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
          ut.rollback();
      } catch (SystemException se) {
        TestUtil.logErr(
            "Exception checking transaction status: " + se.getMessage(), se);
      }

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

  public boolean test7() {
    TestUtil.logMsg("test7");
    TestUtil.logMsg("Cause a RemoveException");

    UserTransaction ut = null;
    TxEBean beanref;
    beanref = null;
    boolean testResult = false;

    String brand1 = "First brand";
    int key1 = 1;

    try {
      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanRequired);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired, TxEBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEBeanRequired);

      beanref = (TxEBean) beanHome.create(tName1, key1, brand1, (float) 1,
          testProps);
      // Start a transaction
      ut = sctx.getUserTransaction();
      TestUtil.logTrace("Starting User Transaction");
      ut.begin();
      TestUtil.logTrace("Forcing a RemoveException");
      beanref.throwRemoveException();
      TestUtil.logTrace("Did not receive expected RemoveException");
    } catch (RemoveException re) {
      testResult = true;
      TestUtil.logTrace("RemoveException received as expected");
    } catch (Exception e) {
      try {
        if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
          ut.rollback();
      } catch (SystemException se) {
        TestUtil.logErr(
            "Exception checking transaction status: " + se.getMessage(), se);
      }

      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    }

    try {
      // OK, let's rollback
      TestUtil.logTrace("Starting rollback");
      ut.rollback();
      TestUtil.logTrace("Rollback finished");
    } catch (Exception er) {
      TestUtil.logErr(
          "Exception caught while trying to rollback" + er.getMessage(), er);
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
    return (testResult);
  }

  public boolean test8() {
    TestUtil.logMsg("test8");
    TestUtil.logMsg("Cause an EJBException");

    TxEBean beanref = null;
    boolean t1, t2;
    t1 = t2 = false;
    boolean testResult = false;
    UserTransaction ut = null;

    String brand1 = "First brand";
    String brand2 = "Second brand";
    String tempName1, tempName2;
    Integer key = null;

    try {
      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanRequired);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired, TxEBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEBeanRequired);
      beanref = (TxEBean) beanHome.create(tName1, 1, brand1, (float) 1,
          testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      // Let's first check that we get our exception thrown
      TestUtil.logTrace(
          "Update brand name and catch TransactionRolledbackException");
      ut.begin();
      try {
        beanref.updateBrandName(brand2, TxEBeanEJB.FLAGEJBEXCEPTION);
        TestUtil.logTrace(
            "Did not receive TransactionRolledbackException as expected");
      } catch (TransactionRolledbackException re) {
        TestUtil
            .logTrace("TransactionRolledbackException received as expected.");
        t1 = true;
      }

      // Make sure tx was marked for rollback
      TestUtil.logTrace("Check that the transaction was marked for rollback");
      int txStatus = ut.getStatus();
      if (txStatus == Status.STATUS_MARKED_ROLLBACK) {
        TestUtil.logTrace("Transaction is marked for rollback");
        TestUtil.printTransactionStatus(txStatus);
        t2 = true;
      } else {
        TestUtil.logTrace(
            "Did not receive transaction marked for rollback as expected");
      }

      // OK, let's rollback
      TestUtil.logTrace("Starting rollback");
      ut.rollback();
      TestUtil.logTrace("Rollback finished");

      if (t1 && t2)
        testResult = true;

      return (testResult);

    } catch (Exception e) {
      try {
        if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
          ut.rollback();
      } catch (SystemException se) {
        TestUtil.logErr(
            "Exception checking transaction status: " + se.getMessage(), se);
      }

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

  public boolean test9() {
    TestUtil.logMsg("test9");
    TestUtil.logMsg("Cause an Error");

    TxEBean beanref = null;
    boolean t1, t2;
    t1 = t2 = false;
    boolean testResult = false;
    UserTransaction ut = null;

    String brand1 = "First brand";
    String brand2 = "Second brand";
    String tempName1, tempName2;
    Integer key = null;

    try {
      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanRequired);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired, TxEBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEBeanRequired);
      beanref = (TxEBean) beanHome.create(tName1, 1, brand1, (float) 1,
          testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      // Let's first check that we get our exception thrown
      TestUtil.logTrace(
          "Update brand name and catch TransactionRolledbackException");
      ut.begin();
      try {
        beanref.updateBrandName(brand2, TxEBeanEJB.FLAGERROR);
        TestUtil.logTrace(
            "Did not receive TransactionRolledbackException as expected");
      } catch (TransactionRolledbackException re) {
        TestUtil
            .logTrace("TransactionRolledbackException received as expected.");
        t1 = true;
      }

      // Make sure tx was marked for rollback
      TestUtil.logTrace("Check that the transaction was marked for rollback");
      int txStatus = ut.getStatus();
      if (txStatus == Status.STATUS_MARKED_ROLLBACK) {
        TestUtil.logTrace("Transaction is marked for rollback");
        TestUtil.printTransactionStatus(txStatus);
        t2 = true;
      } else {
        TestUtil.logTrace(
            "Did not receive transaction marked for rollback as expected");
      }

      // OK, let's rollback
      TestUtil.logTrace("Starting rollback");
      ut.rollback();
      TestUtil.logTrace("Rollback finished");

      if (t1 && t2)
        testResult = true;

      return (testResult);

    } catch (Exception e) {
      try {
        if (ut.getStatus() != Status.STATUS_NO_TRANSACTION)
          ut.rollback();
      } catch (SystemException se) {
        TestUtil.logErr(
            "Exception checking transaction status: " + se.getMessage(), se);
      }

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
