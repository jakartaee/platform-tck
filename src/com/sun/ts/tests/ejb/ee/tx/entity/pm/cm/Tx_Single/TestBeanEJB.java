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

package com.sun.ts.tests.ejb.ee.tx.entity.pm.cm.Tx_Single;

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

  private static final String txEPMBeanMandatory = "java:comp/env/ejb/TxMandatory";

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

    TxEPMBean beanRef1, beanRef2;
    beanRef1 = beanRef2 = null;

    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;

    String expName = "TS";
    float expPrice = (float) 100.00;

    String tempName1, tempName2;
    tempName1 = tempName2 = null;
    float tempPrice1, tempPrice2;
    tempPrice1 = tempPrice2 = (float) 0.0;

    try {
      TestUtil.logTrace(
          "Looking up the TxEPMBean Home interface of " + txEPMBeanRequired);
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequired,
          TxEPMBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEPMBeanRequired);
      beanRef1 = (TxEPMBean) beanHome.create(tName1, new Integer(1),
          tName1 + "-1", (float) 1, testProps);
      TestUtil.logTrace("Entity EJB objects created!");

      TestUtil.logTrace("Updating the Brand Name");
      beanRef1.updateBrandName(expName);

      TestUtil
          .logTrace("Verifying the transaction is commited on method return");
      tempName1 = beanRef1.getBrandName();
      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);

      beanRef2 = (TxEPMBean) beanHome.findByPrimaryKey(new Integer(1));

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
    }
  }

  public boolean test2() {
    TestUtil.logMsg("test2");
    TestUtil.logMsg("Commit a transaction involving entity EJBs, located via "
        + "ejbFind<Method>() - Required case");

    TxEPMBean beanRef1, beanRef2, beanRef3;
    beanRef1 = beanRef2 = beanRef3 = null;

    boolean testResult = false;
    boolean b1, b2, b3;
    b1 = b2 = b3 = false;

    String expName = "TS";

    String tempName1, tempName2;
    tempName1 = tempName2 = null;

    try {
      TestUtil.logTrace(
          "Looking up the TxEPMBean Home interface of " + txEPMBeanRequired);
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequired,
          TxEPMBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEPMBeanRequired);
      beanRef1 = (TxEPMBean) beanHome.create(tName1, new Integer(1),
          tName1 + "-1", (float) 1, testProps);
      TestUtil.logTrace("Entity EJB objects created!");

      beanRef2 = (TxEPMBean) beanHome.findByPrimaryKey(new Integer(1));
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

      beanRef3 = (TxEPMBean) beanHome.findByPrimaryKey(new Integer(1));

      TestUtil.logTrace("Instance Brand Name is " + tempName1);
      tempName2 = beanRef3.getBrandName();
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

  public boolean test3() {
    TestUtil.logMsg("test3");
    TestUtil
        .logMsg("Negative test case, expect TransactonRequiredException to be "
            + "thrown - Mandatory case");

    TxEPMBean beanRef1 = null;
    boolean testResult = false;

    try {
      TestUtil.logTrace(
          "Looking up the TxEPMBean Home interface of " + txEPMBeanMandatory);
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanMandatory,
          TxEPMBeanHome.class);

      TestUtil.logTrace("Creating EJB instances of " + txEPMBeanMandatory);
      beanRef1 = (TxEPMBean) beanHome.create(tName1, new Integer(1),
          tName1 + "-1", (float) 1, testProps);

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
    TestUtil.logMsg("test3 completed");
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
