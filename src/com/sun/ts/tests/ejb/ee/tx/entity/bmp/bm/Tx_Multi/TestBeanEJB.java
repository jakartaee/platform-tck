/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb.ee.tx.entity.bmp.bm.Tx_Multi;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txEbean.*;

import java.util.*;
import java.rmi.*;
import jakarta.ejb.*;
import jakarta.transaction.*;

public class TestBeanEJB implements SessionBean {

  // beanProps represent the bean specific properties of the TestBean
  // testProps represent the test specific properties passed in
  // from the test harness.
  private Properties testProps = null;

  // The TSNamingContext abstracts away the underlying distribution protocol.
  private TSNamingContext jctx = null;

  private SessionContext sctx = null;

  // The TxEBean variables
  private static final String txEBeanRequired = "java:comp/env/ejb/TxRequired";

  private static final String txEBeanRequiresNew = "java:comp/env/ejb/TxRequiresNew";

  private TxEBeanHome beanHome = null;

  private TxEBean beanRef = null;

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

  public boolean doTest1(Integer pkey, String tName, int i) {
    TestUtil.logMsg("doTest1");
    TestUtil.logMsg(
        "Commit a transaction - Tx_Required synchronized multi-clients");

    TxEBean beanRef = null;

    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;

    String expName = tName + "S" + i;

    String tempName1, tempName2;
    tempName1 = tempName2 = null;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logTrace("Start a transaction for this client");

      initLogging(testProps);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired, TxEBeanHome.class);

      ut.begin();
      beanRef = (TxEBean) beanHome.findtxEbean(tName, pkey, testProps);

      TestUtil.logTrace("Updating the Brand Name");
      beanRef.updateBrandName(expName);
      ut.commit();

      TestUtil.logTrace("Verify that the UserTransaction commit call"
          + "commited the transaction to the instance and database level");

      tempName1 = beanRef.getBrandName();
      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);

      tempName2 = beanRef.getDbBrandName();
      if (tempName2.equals(expName))
        b2 = true;
      TestUtil.logTrace("DB Brand Name is " + tempName2);

      if (!b1) {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName1);
      }
      if (!b2) {
        TestUtil.logMsg("Brand Name DB value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName2);
      }

      if (b1 && b2) {
        testResult = true;
        TestUtil.logMsg("The transaction commit was successful");
      } else {
        TestUtil.logMsg("The transaction commit failed");
      }
      TestUtil.logMsg("doTest1 completed");
      return testResult;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean doTest2(Integer pkey, String tName, int i) {
    TestUtil.logMsg("doTest2");
    TestUtil.logMsg(
        "rollback a transaction - Tx_Required synchronized multi-clients");

    TxEBean beanRef = null;

    boolean testResult = false;
    boolean b1 = false;

    String expName = tName + "S" + i;

    String tempName = null;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logTrace("Start a transaction for this client");

      initLogging(testProps);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired, TxEBeanHome.class);

      ut.begin();
      beanRef = (TxEBean) beanHome.findtxEbean(tName, pkey, testProps);

      TestUtil.logTrace("Updating the Brand Name");
      beanRef.updateBrandName(expName);
      ut.rollback();

      TestUtil.logTrace("Verify that the UserTransaction commit call"
          + "commited the transaction to the database level");

      tempName = beanRef.getDbBrandName();
      if (!tempName.equals(expName))
        b1 = true;
      TestUtil.logTrace("DB Brand Name is " + tempName);

      if (!b1) {
        TestUtil.logMsg("Brand Name DB value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName);
      }

      if (b1) {
        testResult = true;
        TestUtil.logMsg("The transaction commit was successful");
      } else {
        TestUtil.logMsg("The transaction commit failed");
      }
      TestUtil.logMsg("doTest2 completed");
      return testResult;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean doTest5(Integer pkey, String tName, int i) {
    TestUtil.logMsg("doTest5");
    TestUtil.logMsg(
        "Commit a transaction - Tx_RequiresNew synchronized multi-clients");

    TxEBean beanRef = null;

    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;

    String expName = tName + "S" + i;

    String tempName1, tempName2;
    tempName1 = tempName2 = null;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logTrace("Start a transaction for this client");

      initLogging(testProps);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequiresNew,
          TxEBeanHome.class);

      ut.begin();
      beanRef = (TxEBean) beanHome.findtxEbean(tName, pkey, testProps);

      TestUtil.logTrace("Updating the Brand Name");
      beanRef.updateBrandName(expName);
      ut.commit();

      TestUtil.logTrace("Verify that the UserTransaction commit call"
          + "commited the transaction to the instance and database level");

      tempName1 = beanRef.getBrandName();
      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);

      tempName2 = beanRef.getDbBrandName();
      if (tempName2.equals(expName))
        b2 = true;
      TestUtil.logTrace("DB Brand Name is " + tempName2);

      if (!b1) {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName1);
      }
      if (!b2) {
        TestUtil.logMsg("Brand Name DB value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName2);
      }

      if (b1 && b2) {
        testResult = true;
        TestUtil.logMsg("The transaction commit was successful");
      } else {
        TestUtil.logMsg("The transaction commit failed");
      }
      TestUtil.logMsg("doTest5 completed");
      return testResult;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean doTest6(Integer pkey, String tName, int i) {
    TestUtil.logMsg("doTest6");
    TestUtil.logMsg(
        "Rollback a transaction - Tx_RequiresNew synchronized multi-clients");

    TxEBean beanRef = null;

    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;

    String expName = tName + "S" + i;

    String tempName1, tempName2;
    tempName1 = tempName2 = null;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logTrace("Start a transaction for this client");

      initLogging(testProps);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequiresNew,
          TxEBeanHome.class);

      ut.begin();
      beanRef = (TxEBean) beanHome.findtxEbean(tName, pkey, testProps);

      TestUtil.logTrace("Updating the Brand Name");
      beanRef.updateBrandName(expName);
      ut.rollback();

      TestUtil.logTrace("Verify that the UserTransaction rollback call"
          + "still commited the transaction to the instance and database level");

      tempName1 = beanRef.getBrandName();
      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);

      tempName2 = beanRef.getDbBrandName();
      if (tempName2.equals(expName))
        b2 = true;
      TestUtil.logTrace("DB Brand Name is " + tempName2);

      if (!b1) {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName1);
      }
      if (!b2) {
        TestUtil.logMsg("Brand Name DB value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName2);
      }

      if (b1 && b2) {
        testResult = true;
        TestUtil.logMsg("The transaction commit was successful");
      } else {
        TestUtil.logMsg("The transaction commit failed");
      }
      TestUtil.logMsg("doTest6 completed");
      return testResult;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    }
  }

  private void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    this.testProps = p;
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.logErr("RemoteLoggingInitException: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    }
  }

}
