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
 * @(#)TestBeanEJB.java	1.10 03/05/16
 */

package com.sun.ts.tests.ejb.ee.tx.sessionLocal.stateful.bm.TxM_Exceptions;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txbeanLocal.*;

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

  private Integer tSize = null;

  private Integer fromKey1 = null;

  // The TxBean variables
  private static final String txBeanMandatory = "java:comp/env/ejb/TxMandatory";

  private TxBeanHome beanHome = null;

  private TxBean beanRef = null;

  private TxBean beanRef2 = null;

  // The requiredEJB methods
  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      TestUtil.logMsg("Getting Naming Context");
      jctx = new TSNamingContext();

      // Get the table sizes
      this.tSize = (Integer) jctx.lookup("java:comp/env/size");
      TestUtil.logTrace("tSize: " + this.tSize);
      this.fromKey1 = (Integer) jctx.lookup("java:comp/env/fromKey1");
      TestUtil.logTrace("fromKey1: " + this.fromKey1);

      TestUtil
          .logMsg("Looking up the TxBean Home interface of " + txBeanMandatory);
      beanHome = (TxBeanHome) jctx.lookup(txBeanMandatory);

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
    TestUtil.logTrace("AppException from EJB");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3;
    b1 = b2 = b3 = false;
    String tName = this.tName1;
    int size = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();
    UserTransaction ut = null;

    try {
      TestUtil.logTrace("Creating EJB instance of " + txBeanMandatory);
      beanRef = (TxBean) beanHome.create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      TestUtil.logTrace("Creating the table");
      ut.begin();
      beanRef.createData(tName);
      ut.commit();

      TestUtil.logTrace("Delete a row and throw AppException");
      ut.begin();
      try {
        beanRef.delete(tName, tRng, tRng, TxBeanEJB.FLAGAPPEXCEPTION);
        TestUtil.logTrace("Expected AppException did not occur");
      } catch (AppException ae) {
        TestUtil.logTrace("AppException received as expected");
        b1 = true;
      }
      ut.commit();

      TestUtil.logTrace("Getting the test results");
      ut.begin();
      dbResults = beanRef.getResults(tName);

      TestUtil.logTrace("Verifying the test results");
      if (!dbResults.contains(new Integer(tRng)))
        b2 = true;

      for (int i = 1; i <= size; i++) {
        if (i == tRng)
          continue;
        else {
          if (dbResults.contains(new Integer(i)))
            b3 = true;
          else {
            b3 = false;
            break;
          }
        }
      }
      beanRef.destroyData(tName);
      ut.commit();

      if (b1 && b2 && b3)
        testResult = true;

    } catch (Exception e) {
      beanRef.destroyData(tName);
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
    return testResult;
  }

  public boolean test2() {
    TestUtil.logTrace("test2");
    TestUtil.logTrace("SysException from EJB");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3, b4;
    b1 = b2 = b3 = b4 = false;
    String tName = this.tName1;
    int size = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();
    UserTransaction ut = null;

    try {
      TestUtil.logTrace("Creating EJB instance of " + txBeanMandatory);
      beanRef = (TxBean) beanHome.create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      TestUtil.logTrace("Creating the table");
      ut.begin();
      beanRef.createData(tName);
      ut.commit();

      TestUtil.logTrace("Delete a row and throw SysException");
      ut.begin();
      try {
        beanRef.delete(tName, tRng, tRng, TxBeanEJB.FLAGSYSEXCEPTION);
        TestUtil.logTrace(
            "Expected javax.ejb.TransactionRolledbackLocalException did not occur");
      } catch (javax.ejb.TransactionRolledbackLocalException te) {
        TestUtil.logTrace(
            "javax.ejb.TransactionRolledbackLocalException received as expected");
        b1 = true;
      }

      TestUtil.logTrace("Check that the transaction was marked for rollback");
      int txStatus = ut.getStatus();
      if (txStatus == Status.STATUS_MARKED_ROLLBACK) {
        TestUtil.printTransactionStatus(txStatus);
        b2 = true;
      }

      TestUtil.logTrace("Check that bean instance was discarded");
      try {
        int level = beanRef.getDefaultTxIsolationLevel(tName);
        TestUtil.logTrace("Bean instance not discarded as expected!");
        b3 = false;
      } catch (javax.ejb.NoSuchObjectLocalException ne) {
        b3 = true;
        TestUtil.logTrace("Bean instance was discarded as expected");
      } catch (EJBException ee) {
        b3 = true;
        TestUtil.logTrace("Bean instance was discarded as expected");
      }
      // Tx marked for rollback, now really roll it back.
      ut.rollback();

      // beanRef is now gone
      // Check the table results for the rollback && Clean up the table
      beanRef2 = (TxBean) beanHome.create();
      beanRef2.initLogging(testProps);

      TestUtil.logTrace("Checking table results for actual rollback occurance");
      // Check the table results, did the rollback really occur?
      ut.begin();
      dbResults = beanRef2.getResults(tName);

      TestUtil.logTrace("Verifying the test results");
      for (int i = 1; i <= size; i++) {
        if (dbResults.contains(new Integer(i)))
          b4 = true;
        else {
          b4 = false;
          break;
        }
      }
      TestUtil.logTrace("Cleaning up the table");
      beanRef2.destroyData(tName);
      ut.commit();

      if (b1 && b2 && b3 && b4)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef2.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
    return testResult;
  }

  public boolean test3() {
    TestUtil.logTrace("test3");
    TestUtil.logTrace("EJBException from EJB");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3, b4;
    b1 = b2 = b3 = b4 = false;
    String tName = this.tName1;
    int size = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();
    UserTransaction ut = null;

    try {
      TestUtil.logTrace("Creating EJB instance of " + txBeanMandatory);
      beanRef = (TxBean) beanHome.create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      TestUtil.logTrace("Creating the table");
      ut.begin();
      beanRef.createData(tName);
      ut.commit();

      TestUtil.logTrace("Delete a row and throw EJBException");
      ut.begin();
      try {
        beanRef.delete(tName, tRng, tRng, TxBeanEJB.FLAGEJBEXCEPTION);
        TestUtil.logTrace(
            "Expected javax.ejb.TransactionRolledbackLocalException did not occur");
      } catch (javax.ejb.TransactionRolledbackLocalException te) {
        TestUtil.logTrace(
            "javax.ejb.TransactionRolledbackLocalException received as expected");
        b1 = true;
      }

      TestUtil.logTrace("Check that the transaction was marked for rollback");
      int txStatus = ut.getStatus();
      if (txStatus == Status.STATUS_MARKED_ROLLBACK) {
        TestUtil.printTransactionStatus(txStatus);
        b2 = true;
      }

      TestUtil.logTrace("Check that bean instance was discarded");
      try {
        int level = beanRef.getDefaultTxIsolationLevel(tName);
        TestUtil.logTrace("Bean instance not discarded as expected!");
        b3 = false;
      } catch (javax.ejb.NoSuchObjectLocalException ne) {
        b3 = true;
        TestUtil.logTrace("Bean instance was discarded as expected");
      } catch (EJBException ee) {
        b3 = true;
        TestUtil.logTrace("Bean instance was discarded as expected");
      }
      // Tx marked for rollback, now really roll it back.
      ut.rollback();

      // beanRef is now gone
      // Check the table results for the rollback && Clean up the table
      beanRef2 = (TxBean) beanHome.create();
      beanRef2.initLogging(testProps);

      TestUtil.logTrace("Checking table results for actual rollback occurance");
      // Check the table results, did the rollback really occur?
      ut.begin();
      dbResults = beanRef2.getResults(tName);

      TestUtil.logTrace("Verifying the test results");
      for (int i = 1; i <= size; i++) {
        if (dbResults.contains(new Integer(i)))
          b4 = true;
        else {
          b4 = false;
          break;
        }
      }
      TestUtil.logTrace("Cleaning up the table");
      beanRef2.destroyData(tName);
      ut.commit();

      if (b1 && b2 && b3 && b4)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef2.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
    return testResult;
  }

  public boolean test4() {
    TestUtil.logTrace("test4");
    TestUtil.logTrace("Error from EJB");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3, b4;
    b1 = b2 = b3 = b4 = false;
    String tName = this.tName1;
    int size = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();
    UserTransaction ut = null;

    try {
      TestUtil.logTrace("Creating EJB instance of " + txBeanMandatory);
      beanRef = (TxBean) beanHome.create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      TestUtil.logTrace("Creating the table");
      ut.begin();
      beanRef.createData(tName);
      ut.commit();

      TestUtil.logTrace("Delete a row and throw Error");
      ut.begin();
      try {
        beanRef.delete(tName, tRng, tRng, TxBeanEJB.FLAGERROR);
        TestUtil.logTrace(
            "Expected javax.ejb.TransactionRolledbackLocalException did not occur");
      } catch (javax.ejb.TransactionRolledbackLocalException re) {
        TestUtil.logTrace(
            "javax.ejb.TransactionRolledbackLocalException received as expected");
        b1 = true;
      }

      TestUtil.logTrace("Check that the transaction was marked for rollback");
      int txStatus = ut.getStatus();
      if (txStatus == Status.STATUS_MARKED_ROLLBACK) {
        TestUtil.printTransactionStatus(txStatus);
        b2 = true;
      }

      TestUtil.logTrace("Check that bean instance was discarded");
      try {
        int level = beanRef.getDefaultTxIsolationLevel(tName);
        TestUtil.logTrace("Bean instance not discarded as expected!");
        b3 = false;
      } catch (javax.ejb.NoSuchObjectLocalException ne) {
        b3 = true;
        TestUtil.logTrace("Bean instance was discarded as expected");
      } catch (EJBException ee) {
        b3 = true;
        TestUtil.logTrace("Bean instance was discarded as expected");
      }
      // Tx marked for rollback, now really roll it back.
      ut.rollback();

      // beanRef is now gone
      // Check the table results for the rollback && Clean up the table
      beanRef2 = (TxBean) beanHome.create();
      beanRef2.initLogging(testProps);

      TestUtil.logTrace("Checking table results for actual rollback occurance");
      // Check the table results, did the rollback really occur?
      ut.begin();
      dbResults = beanRef2.getResults(tName);

      TestUtil.logTrace("Verifying the test results");
      for (int i = 1; i <= size; i++) {
        if (dbResults.contains(new Integer(i)))
          b4 = true;
        else {
          b4 = false;
          break;
        }
      }
      TestUtil.logTrace("Cleaning up the table");
      beanRef2.destroyData(tName);
      ut.commit();

      if (b1 && b2 && b3 && b4)
        testResult = true;

    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef2.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
    return testResult;
  }

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    this.testProps = p;
    // Get the table names
    this.tName1 = TestUtil
        .getTableName(testProps.getProperty("TxBean_Tab1_Delete"));
    TestUtil.logTrace("tName1: " + this.tName1);

    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

}
