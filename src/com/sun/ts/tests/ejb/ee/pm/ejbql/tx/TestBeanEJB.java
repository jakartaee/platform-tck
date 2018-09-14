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

package com.sun.ts.tests.ejb.ee.pm.ejbql.tx;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;
import javax.transaction.*;

public class TestBeanEJB implements SessionBean {

  // testProps represent the test specific properties passed in
  // from the test harness.
  private Properties testProps = null;

  private SessionContext sctx = null;

  // The tx.common variables
  private static final String txCommonRequired = "java:comp/env/ejb/TxRequired";

  private static final String txCommonRequiresNew = "java:comp/env/ejb/TxRequiresNew";

  private static final String txCommonMandatory = "java:comp/env/ejb/TxMandatory";

  private TxCommonBeanHome beanHome = null;

  private TxCommonBeanHome beanHome1 = null;

  private TxCommonBeanHome beanHome2 = null;

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
      TSNamingContext jctx = new TSNamingContext();

      TestUtil.logMsg(
          "Looking up the TxCommonBean Home interface of " + txCommonRequired);
      beanHome = (TxCommonBeanHome) jctx.lookup(txCommonRequired,
          TxCommonBeanHome.class);

      TestUtil.logMsg("Looking up the TxCommonBean Home interface of "
          + txCommonRequiresNew);
      beanHome1 = (TxCommonBeanHome) jctx.lookup(txCommonRequiresNew,
          TxCommonBeanHome.class);

      TestUtil.logMsg(
          "Looking up the TxCommonBean Home interface of " + txCommonMandatory);
      beanHome2 = (TxCommonBeanHome) jctx.lookup(txCommonMandatory,
          TxCommonBeanHome.class);

    } catch (Exception e) {
      TestUtil.logErr("Create exception:", e);
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

  public boolean txTest1() {
    TestUtil.logTrace("txTest1");

    TxCommonBean beanRef1, beanRef2;
    beanRef1 = beanRef2 = null;

    boolean testResult = false;
    boolean a1;
    boolean b1;
    a1 = false;
    b1 = false;

    String tempName1;
    tempName1 = null;

    Integer origKey1 = new Integer(1);
    String origName1 = "ORIG-1";
    float origPrice1 = (float) 1.00;

    String expName = "TS";
    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();
      TestUtil.logTrace("Start a transaction for this client");

      // Start of UserTransaction.
      ut.begin();

      TestUtil.logTrace(
          "Creating the entity bean instance for txTest1:" + txCommonRequired);
      beanRef1 = (TxCommonBean) beanHome.create(origKey1, origName1, origPrice1,
          testProps);
      TestUtil.logTrace("Entity EJB object created!");

      TestUtil.logTrace("Updating the Brand Name");
      beanRef1.updateBrandName(expName);

      TestUtil.logTrace("Checking the update is successful");
      try {
        beanHome.findByBrandName("TS");
        a1 = true;
      } catch (FinderException e) {
        TestUtil.logErr(
            "Exception occurred: Could not access updated brand name", e);
        a1 = false;
      }
      TestUtil.logTrace("Commit the transaction.");
      ut.commit();
      try {
        beanRef2 = (TxCommonBean) beanHome.findByPrimaryKey(origKey1);
      } catch (FinderException fe) {
        TestUtil
            .logErr("Caught unexpected FinderException from findByPrimaryKey:"
                + origKey1, fe);
        // Close the transaction on an exception
        ut.commit();
      } catch (Exception ie) {
        TestUtil.logErr(
            "Caught unexpected Exception from findByPrimaryKey:" + origKey1,
            ie);
      }

      TestUtil.logTrace("Verify the transaction was committed.");

      tempName1 = beanRef2.getBrandName();

      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);

      if (a1 && b1) {
        testResult = true;
        TestUtil.logMsg("The transaction commit was successful");
      } else {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName1);
        TestUtil.logMsg("The transaction commit failed");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught", e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanRef1 != null) {
          beanRef1.remove();
        } else {
          TestUtil.logTrace("beanRef1 in txTest1 is null");
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception caught while removing beanRef1 in txTest1",
            e);
      }

    }
    TestUtil.logMsg("txTest1 completed");
    return testResult;
  }

  public boolean txTest2() {
    TestUtil.logMsg("txTest2");

    TxCommonBean beanRef1, beanRef2;
    beanRef1 = beanRef2 = null;

    boolean testResult = false;
    boolean a1;
    boolean b1;
    a1 = false;
    b1 = false;

    String tempName1;
    tempName1 = null;

    Integer origKey2 = new Integer(2);
    String origName1 = "ORIG-2";
    float origPrice1 = (float) 1.00;
    String expName = "TS";
    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();

      TestUtil.logMsg(
          "Creating the entity bean instance for txTest2" + txCommonRequired);

      ut.begin();
      beanRef1 = (TxCommonBean) beanHome.create(origKey2, origName1, origPrice1,
          testProps);
      TestUtil.logMsg("Entity EJB object created!");
      ut.commit();

      ut.begin();
      TestUtil.logTrace("Updating the Brand Name");
      beanRef1.updateBrandName(expName);
      TestUtil.logTrace("Checking the update is successful");
      try {
        beanHome.findByBrandName("TS");
        a1 = true;
      } catch (FinderException e) {
        TestUtil.logErr(
            "Exception occurred: Could not access updated brand name", e);
        a1 = false;
      }
      TestUtil.logTrace("Roll back the transaction.");
      ut.rollback();

      TestUtil.logMsg("The transaction was rolled back");

      TestUtil.logMsg("Verify the user transaction was rolled back");

      try {
        beanRef2 = (TxCommonBean) beanHome.findByPrimaryKey(origKey2);
      } catch (Exception ie) {
        TestUtil.logErr(
            "Caught unexpected exception from findByPrimaryKey:" + origKey2,
            ie);
      }

      tempName1 = beanRef2.getBrandName();

      if (tempName1.equals(origName1))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);

      if (a1 && b1) {
        testResult = true;
        TestUtil.logMsg("The transaction rollback was successful");
      } else {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + origName1 + ", Actual: " + tempName1);
        TestUtil.logMsg("The transaction rollback failed");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught", e);
      throw new EJBException(e.getMessage());
    } finally {
      // There is no need to remove the bean because it was removed
      // Automatically because it was rolled back.
      TestUtil.logTrace("In txTest2 finally()");
      try {
        if (beanRef1 != null) {
          beanRef1.remove();
        } else {
          TestUtil.logTrace("beanRef1 in txTest2 is null");
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception caught while removing beanRef1 in txTest2",
            e);
      }

    }
    TestUtil.logMsg("txTest2 completed: ");
    return testResult;
  }

  public boolean txTest3() {
    TestUtil.logTrace("txTest3");

    TxCommonBean beanRef1, beanRef2;
    beanRef1 = beanRef2 = null;

    boolean testResult = false;
    boolean a1;
    boolean b1;
    a1 = false;
    b1 = false;

    String tempName1;
    tempName1 = null;
    Integer origKey3 = new Integer(3);
    String origName1 = "ORIG-3";
    float origPrice1 = (float) 1.00;
    String expName = "TS1";
    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();

      // Start of UserTransaction.
      ut.begin();

      TestUtil.logTrace(
          "Creating entity bean instance for txTest3" + txCommonRequiresNew);
      beanRef1 = (TxCommonBean) beanHome1.create(origKey3, origName1,
          origPrice1, testProps);
      TestUtil.logTrace("Entity EJB object created!");

      TestUtil.logTrace("Updating the Brand Name");
      beanRef1.updateBrandName(expName);

      TestUtil.logTrace("Checking the update is successful");
      try {
        beanHome1.findByBrandName1("TS1");
        a1 = true;
      } catch (FinderException e) {
        TestUtil.logErr(
            "Exception occurred: Could not access updated brand name", e);
        a1 = false;
      }
      // Commit changes made by creating the tables
      ut.commit();

      try {
        beanRef2 = (TxCommonBean) beanHome1.findByPrimaryKey(origKey3);
      } catch (FinderException fe) {
        TestUtil
            .logErr("Caught unexpected FinderException from findByPrimaryKey:"
                + origKey3, fe);
        // Close the transaction on an exception
        ut.commit();
      } catch (Exception ie) {
        TestUtil.logErr(
            "Caught unexpected Exception from findByPrimaryKey:" + origKey3,
            ie);
      }

      TestUtil.logTrace("Verify the user transaction was committed.");

      tempName1 = beanRef2.getBrandName();

      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);

      if (a1 && b1) {
        testResult = true;
        TestUtil.logMsg("The transaction commit was successful");
      } else {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName1);
        TestUtil.logMsg("The transaction commit failed");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught", e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanRef1 != null) {
          beanRef1.remove();
        } else {
          TestUtil.logTrace("beanRef1 in txTest3 is null");
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception caught while removing beanRef1 in txTest3",
            e);
      }
    }
    TestUtil.logMsg("txTest3 completed");
    return testResult;
  }

  public boolean txTest4() {
    TestUtil.logMsg("txTest4");

    TxCommonBean beanRef1, beanRef2;
    beanRef1 = beanRef2 = null;

    boolean testResult = false;
    boolean a1;
    boolean b1;
    a1 = false;
    b1 = false;

    String tempName1;
    tempName1 = null;
    Integer origKey4 = new Integer(4);
    String origName1 = "ORIG-4";
    float origPrice1 = (float) 1.00;
    String expName = "TS1";
    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();

      TestUtil.logMsg(
          "Creating entity bean instance for txTest4 " + txCommonRequiresNew);

      ut.begin();
      beanRef1 = (TxCommonBean) beanHome1.create(origKey4, origName1,
          origPrice1, testProps);
      TestUtil.logMsg("Entity EJB object created!");
      ut.commit();

      ut.begin();
      TestUtil.logTrace("Updating the Brand Name");
      beanRef1.updateBrandName(expName);
      TestUtil.logTrace("Checking the update is successful");
      try {
        beanHome1.findByBrandName1("TS1");
        a1 = true;
      } catch (FinderException e) {
        TestUtil.logErr(
            "Exception occurred: Could not access updated brand name", e);
        a1 = false;
      }
      ut.rollback();

      TestUtil.logMsg("The transaction was rolled back");

      TestUtil.logMsg("Verify the user transaction rolled back");

      try {
        beanRef2 = (TxCommonBean) beanHome1.findByPrimaryKey(origKey4);
      } catch (Exception ie) {
        TestUtil.logErr(
            "Caught an unexpected Exception from findByPrimaryKey" + origKey4,
            ie);
      }

      tempName1 = beanRef2.getBrandName();

      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);

      if (a1 && b1) {
        testResult = true;
        TestUtil.logMsg("The transaction rollback was successful");
      } else {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName1);
        TestUtil.logMsg("The transaction rollback failed");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught", e);
      throw new EJBException(e.getMessage());
    } finally {
      // The bean should have been rolled back, just checking . . .
      TestUtil.logTrace("In txTest4 finally()");
      try {
        if (beanRef1 != null) {
          beanRef1.remove();
        } else {
          TestUtil.logTrace("beanRef1 in txTest4 is null");
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception caught while removing beanRef1 in txTest4",
            e);
      }

    }
    TestUtil.logMsg("txTest4 completed: ");
    return testResult;
  }

  public boolean txTest5() {
    TestUtil.logTrace("txTest5");

    TxCommonBean beanRef1, beanRef2;
    beanRef1 = beanRef2 = null;

    boolean testResult = false;
    boolean a1;
    boolean b1;
    a1 = false;
    b1 = false;

    String tempName1;
    tempName1 = null;

    Integer origKey5 = new Integer(5);
    String origName1 = "ORIG-5";
    float origPrice1 = (float) 1.00;
    String expName = "TS2";
    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();

      // Start of UserTransaction.
      ut.begin();

      TestUtil.logTrace(
          "Creating the Entity Bean Instance for txTest5" + txCommonMandatory);
      beanRef1 = (TxCommonBean) beanHome2.create(origKey5, origName1,
          origPrice1, testProps);
      TestUtil.logTrace("Entity EJB objects created!");

      ut.commit();

      ut.begin();
      TestUtil.logTrace("Updating the Brand Name");
      beanRef1.updateBrandName(expName);

      TestUtil.logTrace("Checking the update is successful");
      try {
        beanHome2.findByBrandName2("TS2");
        a1 = true;
      } catch (FinderException e) {
        TestUtil.logErr(
            "Exception occurred: Could not access updated brand name", e);
        a1 = false;
      }
      // Commit changes made by creating the tables
      ut.commit();

      // Begin another tx
      ut.begin();

      try {
        beanRef2 = (TxCommonBean) beanHome2.findByPrimaryKey(origKey5);
      } catch (FinderException fe) {
        TestUtil
            .logErr("Caught unexpected FinderException from findByPrimaryKey:"
                + origKey5, fe);
        // Close the transaction on an exception
      } catch (Exception ie) {
        TestUtil.logErr(
            "Caught unexpected Exception from findByPrimaryKey:" + origKey5,
            ie);
      }

      TestUtil.logTrace("Verify the user transaction was committed.");

      tempName1 = beanRef2.getBrandName();
      ut.commit();

      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);

      if (a1 && b1) {
        testResult = true;
        TestUtil.logMsg("The transaction commit was successful");
      } else {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName1);
        TestUtil.logMsg("The transaction commit failed");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught", e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        ut.begin();
        if (beanRef1 != null) {
          beanRef1.remove();
        } else {
          TestUtil.logTrace("beanRef1 in txTest5 is null");
        }
        ut.commit();
      } catch (Exception e) {
        TestUtil.logErr(
            "Unexpected exception caught while removing beanRef1 in txTest5",
            e);
      }
    }
    TestUtil.logMsg("txTest5 completed");
    return testResult;
  }

  public boolean txTest6() {
    TestUtil.logMsg("txTest6");

    TxCommonBean beanRef1, beanRef2;
    beanRef1 = beanRef2 = null;

    boolean testResult = false;
    boolean a1;
    boolean b1;
    a1 = false;
    b1 = false;

    String tempName1;
    tempName1 = null;
    Integer origKey6 = new Integer(6);
    String origName1 = "ORIG-6";
    float origPrice1 = (float) 1.00;

    String expName = "TS2";
    UserTransaction ut = null;

    try {
      ut = sctx.getUserTransaction();

      TestUtil.logMsg(
          "Creating entity bean instance for txTest6 " + txCommonMandatory);

      ut.begin();
      beanRef1 = (TxCommonBean) beanHome2.create(origKey6, origName1,
          origPrice1, testProps);
      TestUtil.logMsg("Entity EJB objects created!");
      ut.commit();

      ut.begin();
      TestUtil.logTrace("Updating the Brand Name");
      beanRef1.updateBrandName(expName);
      TestUtil.logTrace("Checking the update is successful");
      try {
        beanHome2.findByBrandName2("TS2");
        a1 = true;
      } catch (FinderException e) {
        TestUtil.logErr(
            "Exception occurred: Could not access updated brand name", e);
        a1 = false;
      }
      ut.rollback();
      TestUtil.logMsg("The transaction was rolledback");

      TestUtil.logMsg("Verify the user transaction rolled back");

      ut.begin();
      try {
        beanRef2 = (TxCommonBean) beanHome2.findByPrimaryKey(origKey6);
      } catch (Exception ie) {
        TestUtil.logErr(
            "Caught unexpected exception from findByPrimaryKey:" + origKey6,
            ie);
      }

      tempName1 = beanRef2.getBrandName();
      ut.commit();

      if (tempName1.equals(origName1))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);

      if (a1 && b1) {
        testResult = true;
        TestUtil.logMsg("The transaction rollback was successful");
      } else {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + origName1 + ", Actual: " + tempName1);
        TestUtil.logMsg("The transaction rollback failed");
      }

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught", e);
      throw new EJBException(e.getMessage());
    } finally {
      try {
        ut.begin();
        if (beanRef1 != null) {
          beanRef1.remove();
        } else {
          TestUtil.logTrace("beanRef1 in txTest6 is null");
        }
        ut.commit();
      } catch (Exception e) {
        TestUtil.logErr(
            "Unexpected exception caught while removing beanRef1 in txTest6"
                + e);
      }
    }
    TestUtil.logMsg("txTest6 completed: ");
    return testResult;
  }

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    this.testProps = p;
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

}
