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
 * @(#)BeanCEJB.java	1.18 03/05/16
 */

/*
 * @(#)BeanCEJB.java	1.16 03/03/05
 */

package com.sun.ts.tests.ejb.ee.tx.entityLocal.bmp.cm.TxRN_Diamond;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txEbeanLocal.*;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;
import javax.transaction.*;

public class BeanCEJB implements SessionBean {

  // testProps represent the test specific properties passed in
  // from the test harness.
  private Properties testProps = null;

  // The TSNamingContext abstracts away the underlying distribution protocol.
  private TSNamingContext jctx = null;

  private SessionContext sctx = null;

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
      TestUtil.printStackTrace(e);
      TestUtil.logTrace("Exception from initLogging - BeanC");
    }

    try {
      TestUtil.logMsg("Getting Naming Context");
      jctx = new TSNamingContext();

    } catch (Exception e) {
      TestUtil.logErr("Create exception", e);
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
  // BeanC interface (our business methods)

  public boolean helloC() throws RemoveException {
    TestUtil.logTrace("helloC");

    boolean testResult, b1, b2;
    testResult = b1 = b2 = false;

    String txEBeanRequiresNew = "java:comp/env/ejb/TxRequiresNew";
    TxEBeanHome beanHome = null;
    TxEBean beanRef = null;

    String expName = "BeanCBrand";
    String tempName1, tempName2;
    tempName1 = tempName2 = null;

    try {
      TestUtil.logMsg(
          "Looking up the TxEBean Home interface of " + txEBeanRequiresNew);
      beanHome = (TxEBeanHome) jctx.lookup(txEBeanRequiresNew,
          TxEBeanHome.class);
      beanRef = (TxEBean) beanHome.findtxEbean(tName1, new Integer(1),
          testProps);

      beanRef.updateBrandName(expName);

      tempName1 = beanRef.getBrandName();
      TestUtil.logTrace("Instance Brand Name is " + tempName1);
      if (tempName1.equals(expName))
        b1 = true;

      tempName2 = beanRef.getDbBrandName();
      TestUtil.logTrace("DB Brand Name is " + tempName2);
      if (tempName2.equals(expName))
        b2 = true;

      if (!b1) {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName1);
      }
      // The nature of RequiresNew is that the tx is committed on the
      // method return. Thus we expect the DB value to be modified.
      if (!b2) {
        TestUtil.logMsg("Brand Name DB value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName2);
      }

      if (b1 && b2)
        testResult = true;
      return testResult;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught", e);
      throw new EJBException(e.getMessage());
    }
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
      TestUtil.logErr("RemoteLoggingInitException", e);
      throw new EJBException(e.getMessage());
    }
  }

}
