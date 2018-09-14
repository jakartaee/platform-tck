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

package com.sun.ts.tests.ejb.ee.tx.entity.cmp.cm.Tx_Multi;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txECMPbean.*;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;
import javax.transaction.*;

public class S1TestBeanEJB implements SessionBean {

  // testProps represent the test specific properties passed in
  // from the test harness.
  private Properties testProps = null;

  // The TSNamingContext abstracts away the underlying distribution protocol.
  private TSNamingContext jctx = null;

  private SessionContext sctx = null;

  // The TxECMPBean variables
  private static final String txECMPBeanRequired = "java:comp/env/ejb/TxRequired";

  private static final String txECMPBeanMandatory = "java:comp/env/ejb/TxMandatory";

  private TxECMPBeanHome beanHome = null;

  private TxECMPBean beanRef = null;

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
  // S1TestBean interface (our business methods)

  public boolean doTest1(Integer pkey, String tName, int i) {
    TestUtil.logMsg("doTest1");
    TestUtil.logMsg(
        "Commit a transaction - synchronized multi-clients - Required Tx");

    TxECMPBean beanRef, beanRef2;
    beanRef = beanRef2 = null;

    boolean testResult = false;
    boolean b1, b2;
    b1 = b2 = false;

    String expName = tName + "S" + i;

    String tempName1, tempName2;
    tempName1 = tempName2 = null;

    try {
      initLogging(testProps);
      TestUtil.logTrace("pkey is:" + pkey.toString());
      TestUtil.logTrace("Looking up the txECMPBeanRequired EJB");
      beanHome = (TxECMPBeanHome) jctx.lookup(txECMPBeanRequired,
          TxECMPBeanHome.class);
      beanRef = (TxECMPBean) beanHome.findByPrimaryKey(pkey);

      TestUtil.logTrace("Updating the Brand Name");
      beanRef.updateBrandName(expName);

      TestUtil.logTrace("Verify that the UserTransaction commit call"
          + "commited the transaction to the database level");

      tempName1 = beanRef.getBrandName();
      if (tempName1.equals(expName))
        b1 = true;
      TestUtil.logTrace("Instance Brand Name is " + tempName1);

      beanRef2 = (TxECMPBean) beanHome.findByPrimaryKey(pkey);

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

  public boolean doTest3(Integer pkey, String tName, int i) {
    TestUtil.logMsg("doTest3");
    TestUtil
        .logMsg("Negative test case, expect TransactonRequiredException to be "
            + "thrown - Mandatory case");

    TxECMPBeanHome beanHome = null;
    TxECMPBean beanRef = null;

    boolean testResult = false;
    boolean b1 = false;

    try {
      initLogging(testProps);
      // Get the Entity EJB Home and create an instance - Mandatory
      TestUtil
          .logMsg("Looking up home interface for EJB: " + txECMPBeanMandatory);
      beanHome = (TxECMPBeanHome) jctx.lookup(txECMPBeanMandatory,
          TxECMPBeanHome.class);
      TestUtil.logMsg("Creating entity EJB = " + pkey.toString());
      beanRef = (TxECMPBean) beanHome.create(tName, pkey,
          tName + "-" + pkey.intValue(), (float) 1.00, testProps);

      TestUtil.logTrace("Entity EJB objects created!");
      TestUtil.logMsg("TransactionRequiredException NOT thrown as expected!");

    } catch (TransactionRequiredException txe) {
      testResult = true;
      TestUtil.logMsg("TransactionRequiredException thrown as expected");
    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    }
    TestUtil.logMsg("doTest3 completed");
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
