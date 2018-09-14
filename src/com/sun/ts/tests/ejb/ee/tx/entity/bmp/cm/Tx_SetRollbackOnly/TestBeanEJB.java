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
 * @(#)TestBeanEJB.java	1.18 03/05/16
 */

package com.sun.ts.tests.ejb.ee.tx.entity.bmp.cm.Tx_SetRollbackOnly;

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

  private static final String txEBeanTxRequiresNew = "java:comp/env/ejb/TxRequiresNew";

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

      // Get the table names
      this.tName1 = TestUtil
          .getTableName(TestUtil.getProperty("TxEBean_Delete"));
      TestUtil.logTrace("tName1: " + this.tName1);

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
    TestUtil
        .logMsg("Mark a transaction involving an entity EJB for rollback, in a "
            + "Required case");

    TxEBean beanRef;
    beanRef = null;

    boolean testResult = false;
    boolean b1;
    b1 = false;

    String expName = "TS";

    String tempName1, origName;
    tempName1 = null;
    origName = tName1 + "-1";

    try {
      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanRequired);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired, TxEBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEBeanRequired);
      beanRef = (TxEBean) beanHome.create(tName1, 1, origName, (float) 1,
          testProps);
      TestUtil.logTrace("Entity EJB objects created!");

      try {
        TestUtil.logTrace("Updating the Brand Name with Rollback");
        b1 = beanRef.updateBrandNameRB(expName, TxEBeanEJB.FLAGROLLBACK);
        if (b1) {
          TestUtil.logMsg("Tx was rolledback as expected");
        } else
          TestUtil.logMsg("Tx was NOT rolledback as expected");
      } catch (Exception rb) {
        TestUtil.logErr(
            "Exception rolling back the transaction" + rb.getMessage(), rb);
      }

      if (b1) {
        testResult = true;
        TestUtil.logMsg("The transaction rollback was successful");
      } else {
        TestUtil.logMsg("The transaction rollback failed");
      }
      TestUtil.logMsg("test1 completed");
      return testResult;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanRef: " + e.getMessage(), e);
      }
    }
  }

  public boolean test2() {
    TestUtil.logMsg("test2");
    TestUtil
        .logMsg("Mark a transaction involving an entity EJB for rollback, in a "
            + "Required case");

    TxEBean beanRef;
    beanRef = null;

    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;

    String expName = "TS";

    String tempName1, origName;
    tempName1 = null;
    origName = tName1 + "-1";

    try {
      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanRequired);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequired, TxEBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEBeanRequired);
      beanRef = (TxEBean) beanHome.create(tName1, 1, origName, (float) 1,
          testProps);
      TestUtil.logTrace("Entity EJB objects created!");

      try {
        beanRef.updateBrandName(expName,
            TxEBeanEJB.FLAGAPPEXCEPTIONWITHROLLBACK);
        TestUtil.logMsg("Expected AppException did not occur");
      } catch (AppException ae) {
        TestUtil.logMsg("AppException received as expected");
        b1 = true;
      }

      TestUtil
          .logTrace("Verifying the transaction is rolledback on method return");
      tempName1 = beanRef.getDbBrandName();
      if (tempName1.equals(origName))
        b2 = true;
      TestUtil.logMsg("DB Brand Name is " + tempName1);

      if (!b2) {
        TestUtil.logMsg("Brand Name DB value did not match expected value");
        TestUtil.logMsg("Expected: " + origName + ", Actual: " + tempName1);
      }

      if (b1 && b2) {
        testResult = true;
        TestUtil.logMsg("The transaction rollback was successful");
      } else {
        TestUtil.logMsg("The transaction rollback failed");
      }
      TestUtil.logMsg("test2 completed");
      return testResult;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage());
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanRef: " + e.getMessage(), e);
      }
    }
  }

  public boolean test5() {
    TestUtil.logMsg("test5");
    TestUtil
        .logMsg("Mark a transaction involving an entity EJB for rollback, in a "
            + "Mandatory case");

    TxEBean beanRef;
    beanRef = null;

    boolean testResult = false;
    boolean b1;
    b1 = false;

    String expName = "TS";

    String tempName1, origName;
    tempName1 = null;
    origName = tName1 + "-1";

    try {
      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanMandatory);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanMandatory, TxEBeanHome.class);

      try {
        TestUtil.logTrace("Creating EJB instances of " + txEBeanMandatory);
        beanRef = (TxEBean) beanHome.create(tName1, 1, origName, (float) 1,
            testProps);
        TestUtil.logTrace("Entity EJB objects created!");
      } catch (TransactionRequiredException t1) {
        TestUtil.logMsg("Caught TransactionRequiredException as expected");
        b1 = true;
      } catch (Exception e1) {
        TestUtil.logErr("Unexpected exception caught" + e1.getMessage(), e1);
      }

      if (b1) {
        testResult = true;
        TestUtil.logMsg("Exception was handled as expected.");
      } else {
        TestUtil.logMsg("Exception was not handled as expected.");
      }
      TestUtil.logMsg("test5 completed");
      return testResult;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanRef: " + e.getMessage(), e);
      }
    }
  }

  public boolean test8() {
    TestUtil.logMsg("test8");
    TestUtil
        .logMsg("Mark a transaction involving an entity EJB for rollback, in a "
            + "RequiresNew case");

    TxEBean beanRef;
    beanRef = null;

    boolean testResult = false;
    boolean b1;
    b1 = false;

    String expName = "TS";

    String tempName1, origName;
    tempName1 = null;
    origName = tName1 + "-1";

    try {
      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanTxRequiresNew);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanTxRequiresNew,
          TxEBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEBeanTxRequiresNew);
      beanRef = (TxEBean) beanHome.create(tName1, 1, origName, (float) 1,
          testProps);
      TestUtil.logTrace("Entity EJB objects created!");

      try {
        TestUtil.logTrace("Updating the Brand Name with Rollback");
        b1 = beanRef.updateBrandNameRB(expName, TxEBeanEJB.FLAGROLLBACK);
        if (b1) {
          TestUtil.logMsg("Tx was rolledback as expected");
        } else
          TestUtil.logMsg("Tx was NOT rolledback as expected");
      } catch (Exception rb) {
        TestUtil.logErr(
            "Exception rolling back the transaction" + rb.getMessage(), rb);
      }

      if (b1) {
        testResult = true;
        TestUtil.logMsg("The transaction rollback was successful");
      } else {
        TestUtil.logMsg("The transaction rollback failed");
      }
      TestUtil.logMsg("test8 completed");
      return testResult;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanRef: " + e.getMessage(), e);
      }
    }
  }

  public boolean test9() {
    TestUtil.logMsg("test9");
    TestUtil
        .logMsg("Mark a transaction involving an entity EJB for rollback, in a "
            + "RequiresNew case");

    TxEBean beanRef;
    beanRef = null;

    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;

    String expName = "TS";

    String tempName1, origName;
    tempName1 = null;
    origName = tName1 + "-1";

    try {
      TestUtil.logTrace(
          "Looking up the TxEBean Home interface of " + txEBeanTxRequiresNew);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanTxRequiresNew,
          TxEBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEBeanTxRequiresNew);
      beanRef = (TxEBean) beanHome.create(tName1, 1, origName, (float) 1,
          testProps);
      TestUtil.logTrace("Entity EJB objects created!");

      try {
        beanRef.updateBrandName(expName,
            TxEBeanEJB.FLAGAPPEXCEPTIONWITHROLLBACK);
        TestUtil.logMsg("Expected AppException did not occur");
      } catch (AppException ae) {
        TestUtil.logMsg("AppException received as expected");
        b1 = true;
      }

      TestUtil
          .logTrace("Verifying the transaction is rolledback on method return");
      tempName1 = beanRef.getDbBrandName();
      if (tempName1.equals(origName))
        b2 = true;
      TestUtil.logMsg("DB Brand Name is " + tempName1);

      if (!b2) {
        TestUtil.logMsg("Brand Name DB value did not match expected value");
        TestUtil.logMsg("Expected: " + origName + ", Actual: " + tempName1);
      }

      if (b1 && b2) {
        testResult = true;
        TestUtil.logMsg("The transaction rollback was successful");
      } else {
        TestUtil.logMsg("The transaction rollback failed");
      }
      TestUtil.logMsg("test9 completed");
      return testResult;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage());
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        if (beanRef != null) {
          beanRef.remove();
        }
      } catch (Exception e) {
        TestUtil.logErr("Exception removing beanRef: " + e.getMessage(), e);
      }
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
