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

package com.sun.ts.tests.ejb.ee.tx.entity.pm.bm.Tx_Single;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txEPMbean.*;

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

  // The TxEPMBean variables
  private static final String txEPMBeanRequired = "java:comp/env/ejb/TxRequired";

  private static final String txEPMBeanRequiresNew = "java:comp/env/ejb/TxRequiresNew";

  private TxEPMBeanHome beanHome = null;

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

      TestUtil.logMsg(
          "Looking up the TxEPMBean Home interface of " + txEPMBeanRequired);
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequired,
          TxEPMBeanHome.class);

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
    TestUtil.logTrace("test1");
    TestUtil.logTrace("Commit a transaction involving entity EJBs");

    TxEPMBean beanRef1, beanRef2;
    beanRef1 = beanRef2 = null;

    boolean testResult = false;
    boolean b1;
    b1 = false;

    String tempName1;
    tempName1 = null;

    Integer origKey1 = new Integer(1);
    String origName1;
    origName1 = tName1 + "-1";
    float origPrice1 = (float) 1.00;

    String expName = "TS";
    float expPrice = (float) 100.00;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logTrace("Start a transaction for this client");

      // Start of UserTransaction.
      ut.begin();

      TestUtil.logTrace("Creating EJB instance of " + txEPMBeanRequired);
      TestUtil.logTrace("test1: tName1 is " + tName1);

      TestUtil.logTrace("Creating the test Entity EJBs");
      beanRef1 = (TxEPMBean) beanHome.create(tName1, origKey1, origName1,
          origPrice1, testProps);
      TestUtil.logTrace("Entity EJB objects created!");

      TestUtil.logTrace("Updating the Brand Name");
      beanRef1.updateBrandName(expName);

      // Commit changes made by creating the tables
      ut.commit();

      try {
        beanRef2 = (TxEPMBean) beanHome.findByPrimaryKey(origKey1);
      } catch (FinderException fe) {
        TestUtil.printStackTrace(fe);
        TestUtil.logMsg("Caught unexpected FinderException from trying to "
            + "access a bean.");
        // Close the transaction on an exception
        ut.commit();
      } catch (Exception ie) {
        TestUtil.printStackTrace(ie);
        TestUtil.logMsg("Caught an unexpected exception from trying to "
            + "access a bean.");
        TestUtil.logMsg("The exception that was caught : " + ie);
      }

      TestUtil.logTrace("Verify that the UserTransaction commit call"
          + "commited the transaction to the database level");

      tempName1 = beanRef2.getBrandName();

      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);

      if (b1) {
        testResult = true;
        TestUtil.logMsg("The transaction commit was successful");
      } else {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName1);
        TestUtil.logMsg("The transaction commit failed");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanRef1 != null) {
          beanRef1.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanRef1: " + e.getMessage(), e);
      }

      TestUtil.logMsg("test1 completed");
    }
    return testResult;
  }

  public boolean test2() {
    TestUtil.logMsg("test2");
    TestUtil.logMsg("rollback a transaction involving entity EJBs");

    TxEPMBean beanRef1, beanRef2;
    beanRef1 = beanRef2 = null;

    boolean testResult = false;
    boolean b1;
    b1 = false;

    String tempName1;
    tempName1 = null;

    Integer origKey1 = new Integer(1);
    String origName1;
    origName1 = tName1 + "-1";
    float origPrice1 = (float) 1.00;

    String expName = "TS";
    float expPrice = (float) 100.00;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();

      TestUtil.logMsg("Creating EJB instance of " + txEPMBeanRequired);
      TestUtil.logMsg("test2: tName1 is " + tName1);

      TestUtil.logMsg("Creating the test Entity EJBs");

      ut.begin();
      beanRef1 = (TxEPMBean) beanHome.create(tName1, origKey1, origName1,
          origPrice1, testProps);
      TestUtil.logMsg("Entity EJB objects created!");
      ut.commit();

      ut.begin();
      TestUtil.logTrace("Updating the Brand Name");
      beanRef1.updateBrandName(expName);
      ut.rollback();

      TestUtil.logMsg("The transaction was rolledback");

      TestUtil.logMsg("Verify that the UserTransaction rollbacked back");

      try {
        beanRef2 = (TxEPMBean) beanHome.findByPrimaryKey(origKey1);
        TestUtil.logMsg("Created bean via the PrimaryKey");
      } catch (Exception ie) {
        TestUtil.printStackTrace(ie);
        TestUtil.logMsg("Caught an unexpected exception from trying to "
            + "access a bean.");
        TestUtil.logMsg("The exception that was caught : " + ie);
      }

      tempName1 = beanRef2.getBrandName();

      if (tempName1.equals(origName1))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);

      if (b1) {
        testResult = true;
        TestUtil.logMsg("The transaction rollback was successful");
      } else {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + origName1 + ", Actual: " + tempName1);
        TestUtil.logMsg("The transaction rollback failed");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    } finally {
      TestUtil.logTrace("In test2 finally()");
      try {
        if (beanRef1 != null) {
          beanRef1.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanRef1: " + e.getMessage(), e);
      }

      TestUtil.logMsg("test2 completed: ");
    }
    return testResult;
  }

  public boolean test3() {
    TestUtil.logMsg("test3");
    TestUtil.logMsg("Commit a transaction involving entity EJBs, located via "
        + "ejbFind<Method>() - Required case");

    TxEPMBean beanRef1, beanRef2, beanRef3;
    beanRef1 = beanRef2 = beanRef3 = null;

    boolean testResult = false;
    boolean b1;
    b1 = false;

    String tempName1;
    tempName1 = null;

    Integer origKey1 = new Integer(1);
    String origName1;
    origName1 = tName1 + "-1";
    float origPrice1 = (float) 1.00;

    String expName = "TS";
    float expPrice = (float) 100.00;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logTrace("Start a transaction for this client");

      TestUtil.logTrace(
          "Looking up the TxEPMBean Home interface of " + txEPMBeanRequired);
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequired,
          TxEPMBeanHome.class);

      ut.begin();
      TestUtil.logMsg("Creating EJB instances of " + txEPMBeanRequired);
      beanRef1 = (TxEPMBean) beanHome.create(tName1, origKey1, origName1,
          origPrice1, testProps);
      TestUtil.logMsg("Entity EJB objects created!");
      ut.commit();

      ut.begin();
      TestUtil.logMsg("Find EJB instance");
      beanRef2 = (TxEPMBean) beanHome.findByPrimaryKey(origKey1);
      TestUtil.logMsg("Update Data");
      beanRef2.updateBrandName(expName);
      TestUtil.logMsg("Commit the updated Data");
      ut.commit();

      TestUtil.logMsg("Find EJB instance");
      beanRef3 = (TxEPMBean) beanHome.findByPrimaryKey(origKey1);

      TestUtil.logMsg("Verifying the transaction is commited.");
      tempName1 = beanRef3.getBrandName();

      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logMsg("Instance Brand Name is " + tempName1);

      if (b1) {
        testResult = true;
        TestUtil.logMsg("The transaction commit was successful");
      } else {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName1);
        TestUtil.logMsg("The transaction commit failed");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanRef1 != null) {
          beanRef1.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanRef1: " + e.getMessage(), e);
      }

      TestUtil.logMsg("test3 completed");
    }
    return testResult;
  }

  public boolean test4() {
    TestUtil.logMsg("test4");
    TestUtil.logMsg("rollback a transaction involving entity EJBs, located via "
        + "ejbFind<Method>() - Required case");

    TxEPMBean beanRef1, beanRef2, beanRef3;
    beanRef1 = beanRef2 = beanRef3 = null;

    boolean testResult = false;
    boolean b1;
    b1 = false;

    String tempName1;
    tempName1 = null;

    Integer origKey1 = new Integer(1);
    String origName1;
    origName1 = tName1 + "-1";
    float origPrice1 = (float) 1.00;

    String expName = "TS";
    float expPrice = (float) 100.00;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logTrace("Start a transaction for this client");

      TestUtil.logTrace(
          "Looking up the TxEPMBean Home interface of " + txEPMBeanRequired);
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequired,
          TxEPMBeanHome.class);

      ut.begin();
      TestUtil.logMsg("Creating EJB instances of " + txEPMBeanRequired);
      beanRef1 = (TxEPMBean) beanHome.create(tName1, origKey1, origName1,
          origPrice1, testProps);
      TestUtil.logMsg("Entity EJB objects created!");
      ut.commit();

      ut.begin();
      TestUtil.logMsg("Find EJB instance");
      beanRef2 = (TxEPMBean) beanHome.findByPrimaryKey(origKey1);
      TestUtil.logMsg("Update Data");
      beanRef2.updateBrandName(expName);
      TestUtil.logMsg("Rollback the updated Data");
      ut.rollback();

      TestUtil.logMsg("Find EJB instance");
      beanRef3 = (TxEPMBean) beanHome.findByPrimaryKey(origKey1);

      TestUtil.logMsg("Verifying the transaction is rolled back.");
      tempName1 = beanRef3.getBrandName();

      if (tempName1.equals(origName1))
        b1 = true;
      TestUtil.logMsg("Instance Brand Name is " + tempName1);

      if (b1) {
        testResult = true;
        TestUtil.logMsg("The transaction commit was successful");
      } else {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + origName1 + ", Actual: " + tempName1);
        TestUtil.logMsg("The transaction commit failed");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanRef1 != null) {
          beanRef1.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanRef1: " + e.getMessage(), e);
      }

      TestUtil.logMsg("test4 completed");
    }
    return testResult;
  }

  public boolean test5() {
    TestUtil.logMsg("test5");
    TestUtil.logMsg("Perform a simple transaction - RequiresNew case");

    TxEPMBean beanRef1, beanRef2;
    beanRef1 = beanRef2 = null;

    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;

    String tempName1;
    tempName1 = null;

    float tempPrice1;
    tempPrice1 = (float) 0.0;

    Integer origKey1 = new Integer(1);
    String origName1;
    origName1 = tName1 + "-1";
    float origPrice1 = (float) 1.00;

    String expName = "TS";
    float expPrice = (float) 100.00;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logMsg("Start a transaction for this client");

      TestUtil.logTrace(
          "Looking up the TxEPMBean Home interface of " + txEPMBeanRequiresNew);
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequiresNew,
          TxEPMBeanHome.class);

      ut.begin();
      TestUtil.logMsg("Creating EJB instance of " + txEPMBeanRequiresNew);
      beanRef1 = (TxEPMBean) beanHome.create(tName1, origKey1, origName1,
          origPrice1, testProps);
      TestUtil.logMsg("Entity EJB object created!");

      TestUtil.logMsg("Updating the Brand Name");
      beanRef1.updateBrandName(expName);
      TestUtil.logMsg("Updating the Price");
      beanRef1.updatePrice(expPrice);
      ut.commit();

      TestUtil.logMsg("Find EJB instance");
      beanRef2 = (TxEPMBean) beanHome.findByPrimaryKey(origKey1);

      tempName1 = beanRef2.getBrandName();
      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logMsg("Brand Name is " + tempName1);

      tempPrice1 = beanRef2.getPrice();
      if (tempPrice1 == expPrice)
        b2 = true;
      TestUtil.logMsg("Price is " + tempPrice1);

      if (!b1) {
        TestUtil.logMsg("Brand Name value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName1);
      }
      if (!b2) {
        TestUtil.logMsg("Price value did not match expected value");
        TestUtil.logMsg("Expected: " + expPrice + ", Actual: " + tempPrice1);
      }

      if (b1 && b2) {
        testResult = true;
        TestUtil.logMsg("The transaction was successful");
      } else {
        TestUtil.logMsg("The transaction failed");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanRef1 != null) {
          beanRef1.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanRef1: " + e.getMessage(), e);
      }

      TestUtil.logMsg("test5 completed");
    }
    return testResult;
  }

  public boolean test6() {
    TestUtil.logMsg("test6");
    TestUtil.logMsg("Perform a simple transaction - RequiresNew case");

    TxEPMBean beanRef1, beanRef2;
    beanRef1 = beanRef2 = null;

    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;

    String tempName1;
    tempName1 = null;

    float tempPrice1;
    tempPrice1 = (float) 0.0;

    Integer origKey1 = new Integer(1);
    String origName1;
    origName1 = tName1 + "-1";
    float origPrice1 = (float) 1.00;

    String expName = "TS";
    float expPrice = (float) 100.00;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logMsg("Start a transaction for this client");

      TestUtil.logTrace(
          "Looking up the TxEPMBean Home interface of " + txEPMBeanRequiresNew);
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequiresNew,
          TxEPMBeanHome.class);

      ut.begin();
      TestUtil.logTrace("Creating EJB instance of " + txEPMBeanRequiresNew);
      beanRef1 = (TxEPMBean) beanHome.create(tName1, origKey1, origName1,
          origPrice1, testProps);
      TestUtil.logTrace("Entity EJB object created!");
      ut.commit();

      ut.begin();
      TestUtil.logTrace("Updating the Brand Name");
      beanRef1.updateBrandName(expName);
      TestUtil.logTrace("Updating the Price");
      beanRef1.updatePrice(expPrice);
      ut.rollback();

      TestUtil.logMsg("Find EJB instance");
      beanRef2 = (TxEPMBean) beanHome.findByPrimaryKey(origKey1);

      tempName1 = beanRef2.getBrandName();
      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logMsg("Brand Name is " + tempName1);

      tempPrice1 = beanRef2.getPrice();
      if (tempPrice1 == expPrice)
        b2 = true;
      TestUtil.logMsg("Price is " + tempPrice1);

      if (b1 && b2) {
        testResult = true;
        TestUtil.logMsg("The transaction was successful");
      } else {
        TestUtil.logMsg("The transaction failed");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanRef1 != null) {
          beanRef1.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanRef1: " + e.getMessage(), e);
      }

      TestUtil.logMsg("test6 completed");
    }
    return testResult;
  }

  private void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    this.testProps = p;
    try {
      TestUtil.init(p);

      // Get the table name
      this.tName1 = TestUtil
          .getTableName(TestUtil.getProperty("TxEBean_Delete"));
      TestUtil.logTrace("tName1: " + this.tName1);

    } catch (RemoteLoggingInitException e) {
      TestUtil.logErr("RemoteLoggingInitException: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    }
  }

}
