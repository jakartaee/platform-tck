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
 * @(#)TestBeanEJB.java	1.10 03/05/16
 */

package com.sun.ts.tests.ejb.ee.tx.entityLocal.pm.cm.TxRN_Exceptions;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txEPMbeanLocal.*;

import java.util.*;
import java.rmi.*;
import javax.ejb.*;
import javax.transaction.*;

public class TestBeanEJB implements SessionBean {

  // testProps represent the test specific properties passed in
  // from the test harness.
  private Properties testProps = new Properties();

  // The TSNamingContext abstracts away the underlying distribution protocol.
  private TSNamingContext jctx = null;

  private SessionContext sctx = null;

  // The TxEPMBean variables
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
      TestUtil.logMsg("Getting Naming Context");
      jctx = new TSNamingContext();
      initLogging(p);

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
    TestUtil.logMsg("test1");
    TestUtil.logMsg("Cause an AppException");

    TxEPMBean beanref = null;
    boolean testResult = false;

    String brand1 = "First brand";
    String brand2 = "Second brand";
    String tempName1, tempName2;

    try {
      TestUtil.logTrace(
          "Looking up the TxEPMBean Home interface of " + txEPMBeanRequiresNew);
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequiresNew);

      TestUtil.logTrace("Creating EJB instances of " + txEPMBeanRequiresNew);
      beanref = (TxEPMBean) beanHome.create(tName1, new Integer(1), brand1,
          (float) 1, testProps);

      TestUtil.logTrace("Update brand name and catch AppException");
      try {
        beanref.updateBrandName(brand2, TxEPMBeanEJB.FLAGAPPEXCEPTION);
        TestUtil.logTrace("Did not receive expected AppException");
      } catch (AppException ae) {
        TestUtil.logTrace("AppException received as expected.");
        testResult = true;
      }

      return (testResult);

    } catch (Exception e) {
      TestUtil.logMsg("Unexpected exception caught");
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        beanref.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  public boolean test2() {
    TestUtil.logMsg("test2");
    TestUtil.logMsg("Cause a SystemException");

    TxEPMBean beanref = null;
    boolean t1, t2;
    t1 = t2 = false;
    boolean testResult = false;

    String brand1 = "First brand";
    String brand2 = "Second brand";
    String tempName1, tempName2;

    try {
      TestUtil.logTrace(
          "Looking up the TxEPMBean Home interface of " + txEPMBeanRequiresNew);
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequiresNew);

      TestUtil.logTrace("Creating EJB instances of " + txEPMBeanRequiresNew);
      beanref = (TxEPMBean) beanHome.create(tName1, new Integer(1), brand1,
          (float) 1, testProps);

      // Let's first check that we get our exception thrown
      TestUtil.logTrace("Update brand name and catch RemoteException");
      try {
        beanref.updateBrandName(brand2, TxEPMBeanEJB.FLAGSYSEXCEPTION);
        TestUtil.logTrace("Did not receive expected EJBException");
      } catch (EJBException ee) {
        TestUtil.logTrace("EJBException received as expected.");
        t1 = true;
      }

      if (t1)
        testResult = true;

      return (testResult);

    } catch (Exception e) {
      TestUtil.logMsg("Unexpected exception caught");
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        TestUtil.logTrace("Removing beanref in finally clause!");
        beanref.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  public boolean test3() {
    TestUtil.logMsg("test3");
    TestUtil.logMsg("Cause an EJBException");

    TxEPMBean beanref = null;
    boolean t1, t2;
    t1 = t2 = false;
    boolean testResult = false;

    String brand1 = "First brand";
    String brand2 = "Second brand";
    String tempName1, tempName2;

    try {
      TestUtil.logTrace(
          "Looking up the TxEPMBean Home interface of " + txEPMBeanRequiresNew);
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequiresNew);

      TestUtil.logTrace("Creating EJB instances of " + txEPMBeanRequiresNew);
      beanref = (TxEPMBean) beanHome.create(tName1, new Integer(1), brand1,
          (float) 1, testProps);

      // Let's first check that we get our exception thrown
      TestUtil.logTrace("Update brand name and catch EJBException");
      try {
        beanref.updateBrandName(brand2, TxEPMBeanEJB.FLAGEJBEXCEPTION);
        TestUtil.logTrace("Did not receive expected EJBException");
      } catch (EJBException ee) {
        TestUtil.logTrace("EJBException received as expected.");
        t1 = true;
      }

      if (t1)
        testResult = true;

      return (testResult);

    } catch (Exception e) {
      TestUtil.logMsg("Unexpected exception caught");
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        beanref.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
      ;
    }
  }

  public boolean test4() {
    TestUtil.logMsg("test4");
    TestUtil.logMsg("Cause an Error");

    TxEPMBean beanref = null;
    boolean t1, t2;
    t1 = t2 = false;
    boolean testResult = false;

    String brand1 = "First brand";
    String brand2 = "Second brand";
    String tempName1, tempName2;

    try {
      TestUtil.logTrace(
          "Looking up the TxEPMBean Home interface of " + txEPMBeanRequiresNew);
      beanHome = (TxEPMBeanHome) jctx.lookup(txEPMBeanRequiresNew);

      TestUtil.logTrace("Creating EJB instances of " + txEPMBeanRequiresNew);
      beanref = (TxEPMBean) beanHome.create(tName1, new Integer(1), brand1,
          (float) 1, testProps);

      // Let's first check that we get our exception thrown
      TestUtil.logTrace("Update brand name and catch EJBException");
      try {
        beanref.updateBrandName(brand2, TxEPMBeanEJB.FLAGERROR);
        TestUtil.logTrace("Did not receive expected EJBException");
      } catch (EJBException ee) {
        TestUtil.logTrace("EJBException received as expected.");
        t1 = true;
      }

      if (t1)
        testResult = true;

      return (testResult);

    } catch (Exception e) {
      TestUtil.logMsg("Unexpected exception caught");
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    } finally {
      // cleanup the bean (will remove the DB row entry!)
      try {
        beanref.remove();
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
