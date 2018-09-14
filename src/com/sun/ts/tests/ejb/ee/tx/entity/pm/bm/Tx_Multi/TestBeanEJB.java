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

package com.sun.ts.tests.ejb.ee.tx.entity.pm.bm.Tx_Multi;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txEPMbean.*;

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

  // The TxEPMBean variables
  private static final String txEPMBeanRequired = "java:comp/env/ejb/TxRequired";

  private static final String txEPMBeanRequiresNew = "java:comp/env/ejb/TxRequiresNew";

  private TxEPMBeanHome beanHome = null;

  private TxEPMBean beanRef = null;

  // Table Name variables
  private String tName1 = null;

  private String tName2 = null;

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

    TxEPMBean beanRef = null;
    TxEPMBean beanRef2 = null;

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
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequired,
          TxEPMBeanHome.class);

      ut.begin();
      beanRef = (TxEPMBean) beanHome.findByPrimaryKey(pkey);

      TestUtil.logTrace("Updating the Brand Name");
      beanRef.updateBrandName(expName);
      ut.commit();

      TestUtil.logTrace("Verify that the UserTransaction commit call"
          + "commited the transaction to the instance and database level");

      tempName1 = beanRef.getBrandName();
      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);

      beanRef2 = (TxEPMBean) beanHome.findByPrimaryKey(pkey);

      tempName2 = beanRef2.getBrandName();
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

    TxEPMBean beanRef = null;
    TxEPMBean beanRef2 = null;

    boolean testResult = false;
    boolean b1 = false;

    String expName = tName + "S" + i;

    String tempName = null;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logTrace("Start a transaction for this client");

      initLogging(testProps);
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequired,
          TxEPMBeanHome.class);

      ut.begin();
      beanRef = (TxEPMBean) beanHome.findByPrimaryKey(pkey);

      TestUtil.logTrace("Updating the Brand Name");
      beanRef.updateBrandName(expName);
      ut.rollback();

      TestUtil.logTrace("Verify that the UserTransaction commit call"
          + "commited the transaction to the database level");

      beanRef2 = (TxEPMBean) beanHome.findByPrimaryKey(pkey);

      tempName = beanRef2.getBrandName();
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

  public boolean doTest3(Integer pkey, String tName, int i) {
    TestUtil.logMsg("doTest3");
    TestUtil.logMsg(
        "Commit a transaction - Tx_RequiresNew synchronized multi-clients");

    TxEPMBean beanRef = null;
    TxEPMBean beanRef2 = null;

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
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequiresNew,
          TxEPMBeanHome.class);

      ut.begin();
      beanRef = (TxEPMBean) beanHome.findByPrimaryKey(pkey);

      TestUtil.logTrace("Updating the Brand Name");
      beanRef.updateBrandName(expName);
      ut.commit();

      TestUtil.logTrace("Verify that the UserTransaction commit call"
          + "commited the transaction to the instance and database level");

      tempName1 = beanRef.getBrandName();
      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);

      beanRef2 = (TxEPMBean) beanHome.findByPrimaryKey(pkey);

      tempName2 = beanRef2.getBrandName();
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
      TestUtil.logMsg("doTest3 completed");
      return testResult;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    }
  }

  public boolean doTest4(Integer pkey, String tName, int i) {
    TestUtil.logMsg("doTest4");
    TestUtil.logMsg(
        "Rollback a transaction - Tx_RequiresNew synchronized multi-clients");

    TxEPMBean beanRef = null;
    TxEPMBean beanRef2 = null;

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
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequiresNew,
          TxEPMBeanHome.class);

      ut.begin();
      beanRef = (TxEPMBean) beanHome.findByPrimaryKey(pkey);

      TestUtil.logTrace("Updating the Brand Name");
      beanRef.updateBrandName(expName);
      ut.rollback();

      TestUtil.logTrace("Verify that the UserTransaction rollback call"
          + "still commited the transaction to the instance and database level");

      tempName1 = beanRef.getBrandName();
      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);

      beanRef2 = (TxEPMBean) beanHome.findByPrimaryKey(pkey);

      tempName2 = beanRef2.getBrandName();
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
      TestUtil.logMsg("doTest4 completed");
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
