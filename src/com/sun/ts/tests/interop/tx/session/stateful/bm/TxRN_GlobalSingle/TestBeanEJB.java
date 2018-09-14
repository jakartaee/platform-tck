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

package com.sun.ts.tests.interop.tx.session.stateful.bm.TxRN_GlobalSingle;

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

  private String tName2 = null;

  private Integer tSize = null;

  private Integer fromKey1 = null;

  private Integer fromKey2 = null;

  private Integer toKey2 = null;

  // The TxBean variables
  private static final String txBeanRequiresNew = "java:comp/env/ejb/TxRequiresNew";

  private TxBeanHome beanHome = null;

  private TxBean beanRef = null;

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

      this.fromKey2 = (Integer) jctx.lookup("java:comp/env/fromKey2");
      TestUtil.logTrace("fromKey2: " + this.fromKey2);

      this.toKey2 = (Integer) jctx.lookup("java:comp/env/toKey2");
      TestUtil.logTrace("toKey2: " + this.toKey2);

      TestUtil.logMsg(
          "Looking up the TxBean Home interface of " + txBeanRequiresNew);
      beanHome = (TxBeanHome) jctx.lookup(txBeanRequiresNew, TxBeanHome.class);

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
    TestUtil.logTrace("Insert/Delete followed by a commit to a single table");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3;
    b1 = b2 = b3 = false;
    String tName = this.tName1;
    int size = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();
    UserTransaction ut = null;

    try {
      TestUtil.logTrace("Creating EJB instance of " + txBeanRequiresNew);
      beanRef = (TxBean) beanHome.create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      TestUtil.logTrace("Creating the table");
      ut.begin();
      beanRef.createData(tName);

      TestUtil.logTrace("Insert and delete some rows");
      TestUtil.logTrace("Inserting 2 new rows");
      if (beanRef.insert(tName, size + 1))
        size++;
      if (beanRef.insert(tName, size + 1))
        size++;
      TestUtil.logTrace("Deleting a row");
      beanRef.delete(tName, tRng, tRng);

      TestUtil.logTrace("Get test results");
      dbResults = beanRef.getResults(tName);

      TestUtil.logTrace("Verifying the test results");
      if (!dbResults.contains(new Integer(tRng)))
        b1 = true;

      for (int i = 1; i <= size; i++) {
        if (i == tRng)
          continue;
        else {
          if (dbResults.contains(new Integer(i)))
            b2 = true;
          else {
            b2 = false;
            break;
          }
        }
      }
      beanRef.destroyData(tName);
      ut.commit();

      dbResults = beanRef.getResults(tName);
      if (dbResults.isEmpty())
        b3 = true;
      else
        TestUtil.logMsg("Error:dbResults size should be 0 but returned:"
            + dbResults.size());

      /*
       * try { dbResults = beanRef.getResults(tName); } catch
       * (java.rmi.RemoteException re) { // Table or view should not exist
       * TestUtil.logTrace("Caught expected RemoteException"); b3 = true; }
       */

      if (b1 && b2 && b3)
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

  public boolean test2() {
    TestUtil.logTrace("test2");
    TestUtil.logTrace("Insert/Delete followed by a rollback to a single table");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3;
    b1 = b2 = b3 = false;
    String tName = this.tName1;
    int size = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();
    UserTransaction ut = null;

    try {
      TestUtil.logTrace("Creating EJB instance of " + txBeanRequiresNew);
      beanRef = (TxBean) beanHome.create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      TestUtil.logTrace("Creating the table");
      ut.begin();
      beanRef.createData(tName);

      TestUtil.logTrace("Insert and delete some rows");
      TestUtil.logTrace("Inserting 2 new rows");
      if (beanRef.insert(tName, size + 1))
        size++;
      if (beanRef.insert(tName, size + 1))
        size++;
      TestUtil.logTrace("Deleting a row");
      beanRef.delete(tName, tRng, tRng);

      TestUtil.logTrace("Get test results");
      dbResults = beanRef.getResults(tName);

      TestUtil.logTrace("Verifying the test results");
      if (!dbResults.contains(new Integer(tRng)))
        b1 = true;

      for (int i = 1; i <= size; i++) {
        if (i == tRng)
          continue;
        else {
          if (dbResults.contains(new Integer(i)))
            b2 = true;
          else {
            b2 = false;
            break;
          }
        }
      }
      beanRef.destroyData(tName);
      ut.rollback();
      // See exception tests for RequiresNew auto-rollback.

      dbResults = beanRef.getResults(tName);
      if (dbResults.isEmpty())
        b3 = true;
      else
        TestUtil.logMsg("Error:dbResults size should be 0 but returned:"
            + dbResults.size());
      /*
       * try { dbResults = beanRef.getResults(tName); } catch
       * (java.rmi.RemoteException re) { // Table or view should not exist
       * TestUtil.logTrace("Caught expected RemoteException"); b3 = true; }
       */

      if (b1 && b2 && b3)
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

  public boolean test3() {
    TestUtil.logTrace("test3");
    TestUtil
        .logTrace(" Insert/Delete followed by a commit, and checking TxStatus");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3;
    b1 = b2 = b3 = false;
    String tName = this.tName2;
    int size = this.tSize.intValue();
    int tRng = this.fromKey1.intValue();
    int txStatus1, txStatus2, txStatus3;
    UserTransaction ut = null;

    try {
      TestUtil.logTrace("Creating EJB instance of " + txBeanRequiresNew);
      beanRef = (TxBean) beanHome.create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Getting the UserTransaction interface");
      ut = sctx.getUserTransaction();

      txStatus1 = ut.getStatus();
      TestUtil.logTrace("Tx Status: " + txStatus1);

      TestUtil.logTrace("BEGIN transaction");
      ut.begin();

      txStatus2 = ut.getStatus();
      TestUtil.logTrace("Tx Status: " + txStatus2);

      beanRef.createData(tName);

      TestUtil.logTrace("Inserting 2 new rows");
      if (beanRef.insert(tName, size + 1))
        size++;
      if (beanRef.insert(tName, size + 1))
        size++;

      TestUtil.logTrace("Deleting a row");
      beanRef.delete(tName, tRng, tRng);

      beanRef.destroyData(tName);
      TestUtil.logTrace("COMMIT transaction");
      ut.commit();

      txStatus3 = ut.getStatus();
      TestUtil.logTrace("Tx Status: " + txStatus3);

      // Verify the test results
      TestUtil.logTrace("Verifying the test results");
      if (txStatus1 == Status.STATUS_NO_TRANSACTION)
        b1 = true;
      if (txStatus2 == Status.STATUS_ACTIVE)
        b2 = true;
      if (txStatus3 == Status.STATUS_NO_TRANSACTION)
        b3 = true;

      if (b1 && b2 && b3)
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
      this.tName2 = TestUtil
          .getTableName(TestUtil.getProperty("TxBean_Tab2_Delete"));
      TestUtil.logTrace("tName2: " + this.tName2);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

}
