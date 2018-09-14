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
 * @(#)BeanAEJB.java	1.13 03/05/16
 */

package com.sun.ts.tests.ejb.ee.tx.entityLocal.pm.cm.TxRN_Diamond;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txEPMbeanLocal.*;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;

public class BeanAEJB implements SessionBean {

  // testProps represent the test specific properties passed in
  // from the test harness.
  private Properties testProps = null;

  // The TSNamingContext abstracts away the underlying distribution protocol.
  private TSNamingContext jctx = null;

  private SessionContext sctx = null;

  // Bean variables
  private TxEPMBeanHome beanHome = null;

  private TxEPMBean RefTxEPMBean = null;

  private BeanCHome HomeC = null;

  private BeanC RefC = null;

  private BeanBHome HomeB = null;

  private BeanB RefB = null;

  private static final String txEPMBeanRequiresNew = "java:comp/env/ejb/TxRequiresNew";

  private static final String lookupBeanC = "java:comp/env/ejb/BeanC";

  private static final String lookupBeanB = "java:comp/env/ejb/BeanB";

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
      TestUtil.logTrace("Exception from initLogging - BeanA");
    }

    try {
      TestUtil.logMsg("Getting Naming Context");
      jctx = new TSNamingContext();

      // TxEPMBean - BeanD
      TestUtil
          .logMsg("Getting the EJB Home interface for " + txEPMBeanRequiresNew);
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequiresNew,
          TxEPMBeanHome.class);
      TestUtil.logMsg("Creating EJB TxEPMBean(RequiresNew) instance");
      RefTxEPMBean = (TxEPMBean) beanHome.create(tName1, new Integer(1),
          tName1 + "-1", (float) 1, testProps);

      // BeanC
      TestUtil.logMsg("Getting the EJB Home interface for " + lookupBeanC);
      HomeC = (BeanCHome) jctx.lookup(lookupBeanC, BeanCHome.class);
      TestUtil.logMsg("Creating EJB BeanC instance");
      RefC = (BeanC) HomeC.create(testProps);

      // BeanB
      TestUtil.logMsg("Getting the EJB Home interface for " + lookupBeanB);
      HomeB = (BeanBHome) jctx.lookup(lookupBeanB, BeanBHome.class);
      TestUtil.logMsg("Creating EJB BeanB instance");
      RefB = (BeanB) HomeB.create(testProps);

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
  // BeanA interface (our business methods)

  public boolean test1() {
    TestUtil.logTrace("test1");
    TestUtil
        .logTrace("Commit a transaction involving a Tx Diamond - RequiresNew");

    boolean testResult, b1, b2, b3, b4;
    testResult = b1 = b2 = b3 = b4 = false;

    String expNameB = "BeanBBrand";
    String expNameC = "BeanCBrand";

    String tempName1, tempName2, tempName3, tempName4;
    tempName1 = tempName2 = tempName3 = tempName4 = null;

    try {
      b1 = RefB.helloB();

      TestUtil.logMsg(
          "Verify the tx commit occured immediately via the Container - BeanB");
      tempName1 = RefTxEPMBean.getBrandName();
      TestUtil.logTrace("Instance Brand Name is " + tempName1);
      if (tempName1.equals(expNameB))
        b3 = true;

      b2 = RefC.helloC();

      TestUtil.logMsg(
          "Verify the tx commit occured immediately via the Container - BeanC");
      tempName3 = RefTxEPMBean.getBrandName();
      TestUtil.logTrace("Instance Brand Name is " + tempName3);
      if (tempName3.equals(expNameC))
        b4 = true;

      // Verify the results
      if (!b3) {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + expNameB + ", Actual: " + tempName1);
      }

      if (!b4) {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + expNameC + ", Actual: " + tempName3);
      }

      if (b1 && b2 && b3 && b4)
        testResult = true;
      return testResult;

    } catch (Exception e) {
      TestUtil.logErr("Unexpected exception caught: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the beans
      if (RefB != null)
        try {
          RefB.remove();
        } catch (Exception e) {
          TestUtil.logErr("Exception removing Bean B: " + e.getMessage(), e);
        }
      ;

      if (RefC != null)
        try {
          RefC.remove();
        } catch (Exception e) {
          TestUtil.logErr("Exception removing Bean C: " + e.getMessage(), e);
        }
      ;

      if (RefTxEPMBean != null)
        try {
          RefTxEPMBean.remove();
        } catch (Exception e) {
          TestUtil.logErr(
              "Exception removing Bean TxEPMBean: " + e.getMessage(), e);
        }
      ;
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
      TestUtil.logErr("RemoteLoggingInitException: " + e.getMessage(), e);
      throw new EJBException(e.getMessage());
    }
  }

}
