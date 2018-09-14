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

package com.sun.ts.tests.ejb.ee.tx.sessionLocal.stateless.cm.Tx_SetRollbackOnly;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txbeanLocal.*;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;
import javax.transaction.*;

public class TestBeanEJB implements SessionBean {

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
  private TxBeanHome beanHome = null;

  private TxBean beanRef = null;

  // The requiredEJB methods
  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      TestUtil.logMsg("Getting Naming Context");
      jctx = new TSNamingContext();

      this.tSize = (Integer) jctx.lookup("java:comp/env/size");
      TestUtil.logTrace("tSize: " + this.tSize);
      this.fromKey1 = (Integer) jctx.lookup("java:comp/env/fromKey1");
      TestUtil.logTrace("fromKey1: " + this.fromKey1);

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
    TestUtil.logTrace("setRollbackOnly() - Required case");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3;
    b1 = b2 = b3 = false;
    String tName = this.tName1;
    int tRng = this.fromKey1.intValue();
    int size = this.tSize.intValue();
    String txBeanRequired = "java:comp/env/ejb/TxRequired.STATELESS";

    try {
      TestUtil
          .logMsg("Looking up the TxBean Home interface of " + txBeanRequired);
      beanHome = (TxBeanHome) jctx.lookup(txBeanRequired, TxBeanHome.class);

      TestUtil.logTrace("Creating EJB instance of " + txBeanRequired);
      beanRef = (TxBean) beanHome.create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      beanRef.createData(tName);

      try {
        b1 = beanRef.delete(tName, tRng, tRng, TxBeanEJB.FLAGROLLBACK);
        if (b1) {
          TestUtil.logTrace("Tx was rolledback as expected");
          b1 = true;
        } else
          TestUtil.logTrace("Tx was NOT rolledback as expected");
      } catch (Exception rb) {
        TestUtil.logTrace("Exception rolling back the transaction");
        TestUtil.printStackTrace(rb);
      }

      TestUtil.logTrace("Getting the test results");
      dbResults = beanRef.getResults(tName);
      // beanRef.listTableData(dbResults);

      TestUtil.logTrace("Verifying the test results");
      if (dbResults.contains(new Integer(tRng)))
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

      if (b1 && b2 && b3)
        testResult = true;

    } catch (Exception e) {
      beanRef.destroyData(tName);
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.destroyData(tName);
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
    TestUtil.logTrace("setRollbackOnly() - RequiresNew case");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3;
    b1 = b2 = b3 = false;
    String tName = this.tName1;
    int tRng = this.fromKey1.intValue();
    int size = this.tSize.intValue();
    String txBeanRequiresNew = "java:comp/env/ejb/TxRequiresNew.STATELESS";

    try {
      TestUtil.logMsg(
          "Looking up the TxBean Home interface of " + txBeanRequiresNew);
      beanHome = (TxBeanHome) jctx.lookup(txBeanRequiresNew, TxBeanHome.class);

      TestUtil.logTrace("Creating EJB instance of " + txBeanRequiresNew);
      beanRef = (TxBean) beanHome.create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      beanRef.createData(tName);

      try {
        b1 = beanRef.delete(tName, tRng, tRng, TxBeanEJB.FLAGROLLBACK);
        if (b1) {
          TestUtil.logTrace("Tx was rolledback as expected");
          b1 = true;
        } else
          TestUtil.logTrace("Tx was NOT rolledback as expected");
      } catch (Exception rb) {
        TestUtil.logTrace("Exception rolling back the transaction");
        TestUtil.printStackTrace(rb);
      }

      TestUtil.logTrace("Getting the test results");
      dbResults = beanRef.getResults(tName);
      // beanRef.listTableData(dbResults);

      TestUtil.logTrace("Verifying the test results");
      if (dbResults.contains(new Integer(tRng)))
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

      if (b1 && b2 && b3)
        testResult = true;

    } catch (Exception e) {
      beanRef.destroyData(tName);
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.destroyData(tName);
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
    return testResult;
  }

  public boolean test3() {
    TestUtil.logTrace("test3");
    TestUtil.logTrace("AppException with setRollbackOnly() - Required case");

    Vector dbResults = new Vector();
    boolean testResult = false;
    boolean b1, b2, b3, b4;
    b1 = b2 = b3 = b4 = false;
    String tName = this.tName1;
    int tRng = this.fromKey1.intValue();
    int size = this.tSize.intValue();
    String txBeanRequired = "java:comp/env/ejb/TxRequired.STATELESS";

    try {
      TestUtil
          .logMsg("Looking up the TxBean Home interface of " + txBeanRequired);
      beanHome = (TxBeanHome) jctx.lookup(txBeanRequired, TxBeanHome.class);

      TestUtil.logTrace("Creating EJB instance of " + txBeanRequired);
      beanRef = (TxBean) beanHome.create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      beanRef.createData(tName);

      try {
        b2 = beanRef.delete(tName, tRng, tRng,
            TxBeanEJB.FLAGAPPEXCEPTIONWITHROLLBACK);
        TestUtil.logTrace("Expected AppException did not occur");
      } catch (AppException ae) {
        TestUtil.logTrace("AppException received as expected");
        b1 = true;
      }

      try {
        if (b1) {
          TestUtil.logTrace("Tx was rolledback as expected");
          b2 = true;
        } else
          TestUtil.logTrace("Tx was NOT rolledback as expected");
      } catch (Exception rb) {
        TestUtil.logTrace("Exception rolling back the transaction");
        TestUtil.printStackTrace(rb);
      }

      TestUtil.logTrace("Getting the test results");
      dbResults = beanRef.getResults(tName);
      // beanRef.listTableData(dbResults);

      TestUtil.logTrace("Verifying the test results");
      if (dbResults.contains(new Integer(tRng)))
        b3 = true;

      for (int i = 1; i <= size; i++) {
        if (i == tRng)
          continue;
        else {
          if (dbResults.contains(new Integer(i)))
            b4 = true;
          else {
            b4 = false;
            break;
          }
        }
      }
      beanRef.destroyData(tName);

      if (b1 && b2 && b3 && b4)
        testResult = true;

    } catch (Exception e) {
      beanRef.destroyData(tName);
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.destroyData(tName);
        beanRef.remove();
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
