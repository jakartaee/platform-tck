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

package com.sun.ts.tests.ejb.ee.tx.entity.bmp.cm.Tx_Single;

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
  private Properties testProps = null;

  // The TSNamingContext abstracts away the underlying distribution protocol.
  private TSNamingContext jctx = null;

  private SessionContext sctx = null;

  // The TxEBean variables
  private static final String txEBeanRequired = "java:comp/env/ejb/TxRequired";

  private static final String txEBeanMandatory = "java:comp/env/ejb/Mandatory";

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
    TestUtil.logMsg("Commit a transaction involving entity EJBs created via "
        + "ejbCreate() - Required case");

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

    try {
      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanRequired);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired, TxEBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEBeanRequired);
      beanRef1 = (TxEBean) beanHome.create(tName1, 1, tName1 + "-1", (float) 1,
          testProps);
      beanRef2 = (TxEBean) beanHome.create(tName1, 2, tName1 + "-2", (float) 1,
          testProps);
      TestUtil.logTrace("Entity EJB objects created!");

      TestUtil.logTrace("Updating the Brand Name");
      beanRef1.updateBrandName(expName);

      TestUtil
          .logTrace("Verifying the transaction is commited on method return");
      tempName1 = beanRef1.getBrandName();
      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);
      tempName2 = beanRef1.getDbBrandName();
      if (tempName2.equals(expName))
        b2 = true;
      TestUtil.logTrace("DB Brand Name is " + tempName2);

      TestUtil.logTrace("Updating the Price");
      beanRef2.updatePrice((float) 100.00);

      TestUtil
          .logTrace("Verifying the transaction is commited on method return");
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
      try {
        if (beanRef2 != null) {
          beanRef2.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanRef2: " + e.getMessage(), e);
      }
    }
  }

  public boolean test2() {
    TestUtil.logMsg("test2");
    TestUtil.logMsg("Commit a transaction involving entity EJBs, located via "
        + "ejbFind<Method>() - Required case");

    TxEBean beanRef1, beanRef2;
    beanRef1 = beanRef2 = null;

    boolean testResult = false;
    boolean b1, b2, b3;
    b1 = b2 = b3 = false;

    String expName = "TS";

    String tempName1, tempName2;
    tempName1 = tempName2 = null;

    try {
      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanRequired);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired, TxEBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEBeanRequired);
      beanRef1 = (TxEBean) beanHome.create(tName1, 1, tName1 + "-1", (float) 1,
          testProps);
      TestUtil.logTrace("Entity EJB objects created!");

      beanRef2 = (TxEBean) beanHome.findtxEbean(tName1, new Integer(1),
          testProps);

      if (beanRef1.isIdentical(beanRef2)) {
        b1 = true;
        TestUtil.logMsg("beanRef1 and beanRef2 are IDENTICAL");
      } else {
        TestUtil.logMsg("beanRef1 and beanRef2 are NOT identical");
      }

      TestUtil.logTrace("Updating the Brand Name");
      beanRef2.updateBrandName(expName);

      TestUtil
          .logTrace("Verifying the transaction is commited on method return");
      tempName1 = beanRef2.getBrandName();
      if (tempName1.equals(expName))
        b2 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);
      tempName2 = beanRef2.getDbBrandName();
      if (tempName2.equals(expName))
        b3 = true;
      TestUtil.logTrace("DB Brand Name is " + tempName2);

      if (!b2) {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName1);
      }
      if (!b3) {
        TestUtil.logMsg("Brand Name DB value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName2);
      }

      if (b1 && b2 && b3) {
        testResult = true;
        TestUtil.logMsg("The transaction commit was successful");
      } else {
        TestUtil.logMsg("The transaction commit failed");
      }
      TestUtil.logMsg("test2 completed");
      return testResult;

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
    }
  }

  public boolean test4() {
    TestUtil.logMsg("test4");
    TestUtil
        .logMsg("Negative test case, expect TransactonRequiredException to be "
            + "thrown - Mandatory case");

    TxEBean beanRef1 = null;
    boolean testResult = false;

    try {
      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanMandatory);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanMandatory, TxEBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEBeanMandatory);
      beanRef1 = (TxEBean) beanHome.create(tName1, 1, tName1 + "-1", (float) 1,
          testProps);

      TestUtil.logTrace("Entity EJB objects created!");
      TestUtil.logMsg("TransactionRequiredException NOT thrown as expected!");

    } catch (TransactionRequiredException txe) {
      testResult = true;
      TestUtil.logMsg("TransactionRequiredException thrown as expected");
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
    }
    TestUtil.logMsg("test4 completed");
    return testResult;
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
