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

package com.sun.ts.tests.ejb.ee.tx.entityLocal.bmp.bm.Tx_Single;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txEbeanLocal.*;

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

  // The TxEBean variables
  private static final String txEBeanRequired = "java:comp/env/ejb/TxRequired";

  private static final String txEBeanRequiresNew = "java:comp/env/ejb/TxRequiresNew";

  private TxEBeanHome beanHome = null;

  // Table Name variable
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
      TestUtil.printStackTrace(e);
      TestUtil.logTrace("Exception from initLogging - TestBean");
    }

    try {
      TestUtil.logMsg("Getting Naming Context");
      jctx = new TSNamingContext();

      TestUtil.logMsg(
          "Looking up the TxEBean Home interface of " + txEBeanRequired);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired);

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
    TestUtil.logTrace("Commit a transaction involving entity EJBs");

    TxEBean beanRef1, beanRef2;
    beanRef1 = beanRef2 = null;

    boolean testResult = false;
    boolean b1, b2, b3, b4;
    b1 = b2 = b3 = b4 = false;

    String expName = "TS";
    float expPrice = (float) 100.00;

    String tempName1, tempName2;
    tempName1 = tempName2 = null;
    float tempPrice1, tempPrice2;
    tempPrice1 = tempPrice2 = (float) 0.0;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logTrace("Start a transaction for this client");

      // Start of UserTransaction.
      ut.begin();

      TestUtil.logTrace("Creating EJB instance of " + txEBeanRequired);
      TestUtil.logTrace("test1: tName1 is " + tName1);

      TestUtil.logTrace("Creating the test Entity EJBs");
      beanRef1 = (TxEBean) beanHome.create(tName1, 1, tName1 + "-1", (float) 1,
          testProps);
      beanRef2 = (TxEBean) beanHome.create(tName1, 2, tName1 + "-2", (float) 2,
          testProps);
      TestUtil.logTrace("Entity EJB objects created!");

      // Commit changes made by creating the tables
      ut.commit();

      ut.begin();
      TestUtil.logTrace("Updating the Brand Name");
      beanRef1.updateBrandName(expName);
      ut.commit();

      TestUtil.logTrace("Verify that the UserTransaction commit call"
          + "commited the transaction to the database level");

      tempName1 = beanRef1.getBrandName();
      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);
      tempName2 = beanRef1.getDbBrandName();
      if (tempName2.equals(expName))
        b2 = true;
      TestUtil.logTrace("DB Brand Name is " + tempName2);

      ut.begin();
      TestUtil.logTrace("Updating the Price");
      beanRef2.updatePrice((float) 100.00);
      ut.commit();

      TestUtil.logTrace("Verify that the UserTransaction commit call"
          + "commited the transaction to the database level");

      tempPrice1 = beanRef2.getPrice();
      if (tempPrice1 == expPrice)
        b3 = true;
      TestUtil.logTrace("Instance Price is " + tempPrice1);
      tempPrice2 = beanRef2.getDbPrice();
      if (tempPrice2 == expPrice)
        b4 = true;
      TestUtil.logTrace("DB Price is " + tempPrice2);

      if (!b1) {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName1);
      }
      if (!b2) {
        TestUtil.logMsg("Brand Name DB value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName2);
      }
      if (!b3) {
        TestUtil.logMsg("Price instance value did not match expected value");
        TestUtil.logMsg("Expected: " + expPrice + ", Actual: " + tempPrice1);
      }
      if (!b4) {
        TestUtil.logMsg("Price DB value did not match expected value");
        TestUtil.logMsg("Expected: " + expPrice + ", Actual: " + tempPrice2);
      }

      if (b1 && b2 && b3 && b4) {
        testResult = true;
        TestUtil.logMsg("The transaction commit was successful");
      } else {
        TestUtil.logMsg("The transaction commit failed");
      }
      TestUtil.logMsg("test1 completed");
      return testResult;

    } catch (Exception e) {
      TestUtil.logMsg("Unexpected exception caught");
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        beanRef1.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;

      try {
        beanRef2.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;

    }
  }

  public boolean test2() {
    TestUtil.logMsg("test2");
    TestUtil.logMsg("rollback a transaction involving entity EJBs");

    TxEBean beanRef1, beanRef2;
    beanRef1 = beanRef2 = null;

    boolean testResult = false;
    boolean b1;
    b1 = false;

    String expName = "TS";
    float expPrice = (float) 100.00;

    String tempName1, tempName2;
    tempName1 = tempName2 = null;
    float tempPrice1, tempPrice2;
    tempPrice1 = tempPrice2 = (float) 0.0;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logMsg("Start a transaction for this client");

      // Start of UserTransaction.
      ut.begin();

      TestUtil.logMsg("Creating EJB instance of " + txEBeanRequired);
      TestUtil.logMsg("test2: tName1 is " + tName1);

      TestUtil.logMsg("Creating the test Entity EJBs");
      beanRef1 = (TxEBean) beanHome.create(tName1, 1, tName1 + "-1", (float) 1,
          testProps);
      TestUtil.logMsg("Entity EJB objects created!");

      // Rollback changes made by creating the tables
      ut.rollback();
      TestUtil.logMsg("The transaction was rolledback");

      TestUtil.logMsg("Verify that the UserTransaction rollback call"
          + "rollback on create the transaction to the database level");

      try {
        ut.begin();
        beanRef2 = (TxEBean) beanHome.findtxEbean(tName1, new Integer(1),
            testProps);
        ut.commit();
      } catch (FinderException fe) {
        TestUtil.logMsg("Caught expected FinderException from trying to "
            + "access a bean that has bean created then rolledback");
        b1 = true;
        // Close the transaction on an exception
        ut.commit();
      } catch (Exception ie) {
        TestUtil.printStackTrace(ie);
        TestUtil.logMsg("Caught an unexpected exception from trying to "
            + "access a bean that has bean created then rolledback");
        TestUtil.logMsg("The exception that was caught : " + ie);
      }

      if (b1) {
        testResult = true;
        TestUtil.logMsg("The transaction rollback was successful");
      } else {
        TestUtil.logMsg("The transaction rollback failed");
      }
      TestUtil.logMsg("test2 completed: " + testResult);
      return testResult;

    } catch (Exception e) {
      TestUtil.logMsg("Unexpected exception caught");
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    } finally {
      // There is no need to remove the bean because it was removed
      // Automatically because it was rolled back.
      TestUtil.logTrace("In test2 finally()");
    }
  }

  public boolean test3() {
    TestUtil.logMsg("test3");
    TestUtil.logMsg("Commit a transaction involving entity EJBs, located via "
        + "ejbFind<Method>() - Required case");

    TxEBean beanRef1, beanRef2;
    beanRef1 = beanRef2 = null;

    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;

    String expName = "TS";

    String tempName1, tempName2;
    tempName1 = tempName2 = null;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logTrace("Start a transaction for this client");

      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanRequired);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired);

      // Start of UserTransaction.
      ut.begin();
      TestUtil.logMsg("Creating EJB instances of " + txEBeanRequired);
      beanRef1 = (TxEBean) beanHome.create(tName1, 1, tName1 + "-1", (float) 1,
          testProps);
      TestUtil.logMsg("Entity EJB objects created!");
      ut.commit();

      ut.begin();
      TestUtil.logMsg("Find EJB instance");
      beanRef2 = (TxEBean) beanHome.findtxEbean(tName1, new Integer(1),
          testProps);
      TestUtil.logMsg("Update Data");
      beanRef2.updateBrandName(expName);
      TestUtil.logMsg("Commit the updated Data");
      ut.commit();

      TestUtil.logMsg("Verifying the transaction is commited on method return");
      tempName1 = beanRef2.getBrandName();
      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logMsg("Instance Brand Name is " + tempName1);
      tempName2 = beanRef2.getDbBrandName();
      if (tempName2.equals(expName))
        b2 = true;
      TestUtil.logMsg("DB Brand Name is " + tempName2);

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
      TestUtil.logMsg("test3 completed");
      return testResult;

    } catch (Exception e) {
      TestUtil.logMsg("Unexpected exception caught");
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        beanRef1.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;

      try {
        beanRef2.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;

    }
  }

  public boolean test4() {
    TestUtil.logMsg("test4");
    TestUtil.logMsg("rollback a transaction involving entity EJBs, located via "
        + "ejbFind<Method>() - Required case");

    TxEBean beanRef1, beanRef2;
    beanRef1 = beanRef2 = null;

    boolean testResult = false;
    boolean b1;
    b1 = false;

    String expName = "TS";

    String tempName1;
    tempName1 = null;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logTrace("Start a transaction for this client");

      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanRequired);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired);

      // Start of UserTransaction.
      ut.begin();
      TestUtil.logMsg("Creating EJB instances of " + txEBeanRequired);
      beanRef1 = (TxEBean) beanHome.create(tName1, 1, tName1 + "-1", (float) 1,
          testProps);
      TestUtil.logMsg("Entity EJB objects created!");
      ut.commit();

      ut.begin();
      TestUtil.logMsg("Find EJB instance.");
      beanRef2 = (TxEBean) beanHome.findtxEbean(tName1, new Integer(1),
          testProps);
      TestUtil.logMsg("Update Data.");
      beanRef2.updateBrandName(expName);
      TestUtil.logMsg("Rollback Data");
      ut.rollback();

      TestUtil.logMsg("Verifying the transaction is rolled back.");

      tempName1 = beanRef2.getDbBrandName();
      if (tempName1.equals(tName1 + "-1"))
        b1 = true;
      TestUtil.logTrace("DB Brand Name is " + tempName1);

      if (b1) {
        testResult = true;
        TestUtil.logMsg("The transaction rollback was successful");
      } else {
        TestUtil.logMsg("Brand Name DB value did not match expected value");
        TestUtil
            .logMsg("Expected: " + tName1 + "-1" + ", Actual: " + tempName1);
        TestUtil.logMsg("The transaction rollback failed");
      }
      TestUtil.logMsg("test4 completed");
      return testResult;

    } catch (Exception e) {
      TestUtil.logMsg("Unexpected exception caught");
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        beanRef1.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;

      try {
        beanRef2.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;

    }
  }

  public boolean test7() {
    TestUtil.logMsg("test7");
    TestUtil.logMsg("Perform a simple transaction - RequiresNew case");

    TxEBean beanRef1 = null;

    boolean testResult = false;
    boolean b1, b2, b3, b4;
    b1 = b2 = b3 = b4 = false;

    String expName = "TS";
    float expPrice = (float) 100.00;

    String tempName1, tempName2;
    tempName1 = tempName2 = null;
    float tempPrice1, tempPrice2;
    tempPrice1 = tempPrice2 = (float) 0.0;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logMsg("Start a transaction for this client");

      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanRequiresNew);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequiresNew);

      ut.begin();
      TestUtil.logMsg("Creating EJB instance of " + txEBeanRequiresNew);
      beanRef1 = (TxEBean) beanHome.create(tName1, 1, tName1 + "-1", (float) 1,
          testProps);
      TestUtil.logMsg("Entity EJB object created!");
      ut.commit();

      ut.begin();
      TestUtil.logMsg("Updating the Brand Name");
      beanRef1.updateBrandName(expName);
      TestUtil.logMsg("Updating the Price");
      beanRef1.updatePrice((float) 100.00);
      ut.commit();

      tempName1 = beanRef1.getBrandName();
      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logMsg("Brand Name is " + tempName1);

      tempPrice1 = beanRef1.getPrice();
      if (tempPrice1 == (float) 100.00)
        b2 = true;
      TestUtil.logMsg("Price is " + tempPrice1);

      if (!b1) {
        TestUtil.logMsg("Brand Name DB value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName1);
      }
      if (!b2) {
        TestUtil.logMsg("Price DB value did not match expected value");
        TestUtil
            .logMsg("Expected: " + (float) 100.00 + ", Actual: " + tempPrice1);
      }

      tempName2 = beanRef1.getDbBrandName();
      if (tempName2.equals(expName))
        b3 = true;
      TestUtil.logMsg("DB Brand Name is " + tempName2);

      tempPrice2 = beanRef1.getDbPrice();
      if (tempPrice2 == expPrice)
        b4 = true;
      TestUtil.logMsg("DB Price is " + tempPrice2);

      if (!b3) {
        TestUtil.logMsg("Brand Name DB value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName2);
      }
      if (!b4) {
        TestUtil.logMsg("Price DB value did not match expected value");
        TestUtil.logMsg("Expected: " + expPrice + ", Actual: " + tempPrice2);
      }

      if (b1 && b2 && b3 && b4) {
        testResult = true;
        TestUtil.logMsg("The transaction was successful");
      } else {
        TestUtil.logMsg("The transaction failed");
      }
      TestUtil.logMsg("test7 completed");
      return testResult;

    } catch (Exception e) {
      TestUtil.logMsg("Unexpected exception caught");
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        beanRef1.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  public boolean test8() {
    TestUtil.logMsg("test8");
    TestUtil.logMsg("Perform a simple transaction - RequiresNew case");

    TxEBean beanRef1 = null;

    boolean testResult = false;
    boolean b1, b2, b3, b4;
    b1 = b2 = b3 = b4 = false;

    String expName = "TS";
    float expPrice = (float) 100.00;

    String tempName1, tempName2;
    tempName1 = tempName2 = null;
    float tempPrice1, tempPrice2;
    tempPrice1 = tempPrice2 = (float) 0.0;

    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logMsg("Start a transaction for this client");

      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanRequiresNew);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequiresNew);

      ut.begin();
      TestUtil.logTrace("Creating EJB instance of " + txEBeanRequiresNew);
      beanRef1 = (TxEBean) beanHome.create(tName1, 1, tName1 + "-1", (float) 1,
          testProps);
      TestUtil.logTrace("Entity EJB object created!");
      ut.commit();

      ut.begin();
      TestUtil.logTrace("Updating the Brand Name");
      beanRef1.updateBrandName(expName);
      TestUtil.logTrace("Updating the Price");
      beanRef1.updatePrice((float) 100.00);
      ut.rollback();

      tempName1 = beanRef1.getBrandName();
      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logMsg("Brand Name is " + tempName1);

      tempPrice1 = beanRef1.getPrice();
      if (tempPrice1 == (float) 100.00)
        b2 = true;
      TestUtil.logMsg("Price is " + tempPrice1);

      if (!b1) {
        TestUtil.logMsg("Brand Name DB value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName1);
      }
      if (!b2) {
        TestUtil.logMsg("Price DB value did not match expected value");
        TestUtil
            .logMsg("Expected: " + (float) 100.00 + ", Actual: " + tempPrice1);
      }

      tempName2 = beanRef1.getDbBrandName();
      if (tempName2.equals(expName))
        b3 = true;
      TestUtil.logMsg("DB Brand Name is " + tempName2);

      tempPrice2 = beanRef1.getDbPrice();
      if (tempPrice2 == expPrice)
        b4 = true;
      TestUtil.logMsg("DB Price is " + tempPrice2);

      if (!b3) {
        TestUtil.logMsg("Brand Name DB value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName2);
      }
      if (!b4) {
        TestUtil.logMsg("Price DB value did not match expected value");
        TestUtil.logMsg("Expected: " + expPrice + ", Actual: " + tempPrice2);
      }

      if (b1 && b2 && b3 && b4) {
        testResult = true;
        TestUtil.logMsg("The transaction was successful");
      } else {
        TestUtil.logMsg("The transaction failed");
      }
      TestUtil.logMsg("test8 completed");
      return testResult;

    } catch (Exception e) {
      TestUtil.logMsg("Unexpected exception caught");
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        beanRef1.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  private void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    this.testProps = p;
    try {
      TestUtil.init(p);
      // Get the table name
      this.tName1 = TestUtil
          .getTableName(testProps.getProperty("TxEBean_Delete"));
      TestUtil.logTrace("tName1: " + this.tName1);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

}
