/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
 * @(#)BeanBEJB.java	1.12 03/05/16
 */

package com.sun.ts.tests.ejb.ee.tx.entityLocal.pm.bm.TxR_Diamond;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txEPMbeanLocal.*;

import java.util.*;
import java.rmi.*;
import jakarta.ejb.*;
import jakarta.transaction.*;

public class BeanBEJB implements SessionBean {

  // testProps represent the test specific properties passed in
  // from the test harness.
  private Properties testProps = null;

  // The TSNamingContext abstracts away the underlying distribution protocol.
  private TSNamingContext jctx = null;

  private SessionContext sctx = null;

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
      TestUtil.logTrace("Exception from initLogging - BeanB");
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
  // BeanB interface (our business methods)

  public boolean helloB() throws RemoveException {
    TestUtil.logTrace("helloB");

    boolean testResult, b1;
    testResult = b1 = false;

    String txEPMBeanRequired = "java:comp/env/ejb/TxRequired";
    TxEPMBeanHome beanHome = null;
    TxEPMBean beanRef = null;

    String expName = "BeanBBrand";

    String tempName = null;

    try {
      TestUtil.logMsg(
          "Looking up the TxEPMBean Home interface of " + txEPMBeanRequired);
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequired,
          TxEPMBeanHome.class);
      beanRef = (TxEPMBean) beanHome.findByPrimaryKey(new Integer(1));

      beanRef.updateBrandName(expName);

      tempName = beanRef.getBrandName();
      TestUtil.logTrace("Instance Brand Name is " + tempName);
      if (tempName.equals(expName))
        b1 = true;

      if (!b1) {
        TestUtil
            .logMsg("Brand Name instance value did not match expected value");
        TestUtil.logMsg("Expected: " + expName + ", Actual: " + tempName);
      }

      if (b1)
        testResult = true;
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
