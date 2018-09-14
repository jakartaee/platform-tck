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
 * @(#)TestBeanEJB.java	1.21 03/05/16
 */

package com.sun.ts.tests.ejb.ee.tx.session.stateful.cm.TxM_Single;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;
import com.sun.ts.tests.ejb.ee.tx.txbean.*;

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

  private String tName1 = null;

  // The TxBean variables
  private static final String txBeanMandatory = "java:comp/env/ejb/TxMandatory";

  private TxBeanHome beanHome = null;

  private TxBean beanRef = null;

  // The requiredEJB methods
  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
    try {
      TestUtil.logMsg("Getting Naming Context");
      jctx = new TSNamingContext();

      TestUtil
          .logMsg("Looking up the TxBean Home interface of " + txBeanMandatory);
      beanHome = (TxBeanHome) jctx.lookup(txBeanMandatory, TxBeanHome.class);

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
    TestUtil.logTrace(
        "Access a method with the Mandatory attribute  and ensure that "
            + "TransactionRequiredException exception is thrown");

    boolean testResult = false;
    String tName = this.tName1;

    try {
      TestUtil.logTrace("Creating EJB instance of " + txBeanMandatory);
      beanRef = (TxBean) beanHome.create();

      TestUtil.logTrace("Logging data from server");
      beanRef.initLogging(testProps);

      TestUtil.logTrace("Getting the default Tx isolation level");
      int level = beanRef.getDefaultTxIsolationLevel(tName);
      TestUtil.logTrace("level: " + level);

      TestUtil.logTrace(
          "Error: TransactionRequiredException not thrown as expected");

    } catch (TransactionRequiredException et) {
      TestUtil.logTrace("Caught expected TransactionRequiredException");
      testResult = true;
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return testResult;
  }

  public boolean test2() {
    TestUtil.logTrace("test2");
    TestUtil.logTrace("Access a business method with a Style 3 attribute "
        + "declaration set to Supports that overrides a Style 2 "
        + "attribute declaration set to Mandatory and ensure that "
        + "the  TransactionRequiredException exception is NOT thrown.");

    boolean t1, t2;
    t1 = t2 = false;

    try {
      TestUtil.logTrace("Creating EJB instance of " + txBeanMandatory);
      beanRef = (TxBean) beanHome.create();

      TestUtil.logTrace("Logging data from server: initLogging() method with "
          + "Style 3 Supports attribute");
      beanRef.initLogging(testProps);
      TestUtil
          .logTrace("As expected, TransactionRequiredException was not thrown");
      t1 = true;

      TestUtil.logTrace(
          "Calling initLogging() method with Style 2 Mandatory attribute");
      beanRef.initLogging();

    } catch (TransactionRequiredException et) {
      if (!t1) {
        TestUtil.logErr("Caught unexpected TransactionRequiredException: "
            + et.getMessage());
        TestUtil.printStackTrace(et);
      } else {
        TestUtil.logTrace("Caught expected TransactionRequiredException");
        t2 = true;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
    } finally {
      // cleanup the bean
      try {
        beanRef.remove();
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
      }
    }
    return (t1 && t2);
  }

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    this.testProps = p;
    try {
      TestUtil.init(p);
      // Get the table names
      this.tName1 = TestUtil
          .getTableName(TestUtil.getProperty("TxBean_Tab1_Delete"));
      TestUtil.logTrace("tName1: " + this.tName1);

    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }
}
