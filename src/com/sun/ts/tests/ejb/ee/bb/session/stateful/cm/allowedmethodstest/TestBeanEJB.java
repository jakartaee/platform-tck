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

package com.sun.ts.tests.ejb.ee.bb.session.stateful.cm.allowedmethodstest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import javax.transaction.*;
import java.rmi.*;
import java.sql.*;

public class TestBeanEJB implements SessionBean, SessionSynchronization {
  private SessionContext sctx = null;

  private Properties harnessProps = null;

  private TSNamingContext nctx = null;

  private Helper helperRef = null;

  private TimerHandle th;

  private String role = "Administrator";

  private Hashtable table = new Hashtable();

  private Hashtable table1 = new Hashtable();

  private static final String testLookup = "java:comp/env/ejb/Helper";

  private static final String timerLookup = "java:comp/env/ejb/TimerLocal";

  private UserTransaction ut;

  // These are the method tests
  private static final String tests[] = { "ejbCreate", "ejbRemove",
      "ejbActivate", "ejbPassivate", "afterBegin", "beforeCompletion",
      "afterCompletion", "setSessionContext", "businessMethod" };

  // This is the results of the operation tests
  private static final Properties methodList[] = { new Properties(),
      new Properties(), new Properties(), new Properties(), new Properties(),
      new Properties(), new Properties(), new Properties(), new Properties() };

  private int doTest = 0;

  private static final Properties setRollbackList = new Properties();

  public void ejbCreate(Properties p, int doTest) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    this.doTest = doTest;
    harnessProps = p;
    try {
      TestUtil.logMsg("Initialize remote logging");
      TestUtil.init(p);

      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();

      TestUtil.logMsg("Do timer in ejbCreate");
      th = doTimer();

    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException(e.getMessage());
    } catch (NamingException e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Unable to obtain naming context");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    if (doTest == 0) {
      doOperationTests("ejbCreate");
    } else {
      if (doTest == 5)
        doSetRollbackOnlyTest("ejbCreate");
    }
  }

  public void setSessionContext(SessionContext sc) {
    TestUtil.logTrace("setSessionContext");
    this.sctx = sc;
    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("Unable to obtain NamingContext");
    }
    doOperationTests("setSessionContext");
    doSetRollbackOnlyTest("setSessionContext");

  }

  public void ejbRemove() {
    TestUtil.logTrace("ejbRemove");
  }

  public void ejbActivate() {
    TestUtil.logTrace("ejbActivate");
    if (doTest == 6) {
      doSetRollbackOnlyTest("ejbActivate");
    } else {
      doOperationTests("ejbActivate");
    }
  }

  public void ejbPassivate() {
    TestUtil.logTrace("ejbPassivate");
    if (doTest == 7) {
      doSetRollbackOnlyTest("ejbActivate");
    } else {
      doOperationTests("ejbPassivate");
    }
  }

  public void afterBegin() {
    TestUtil.logTrace("afterBegin");
    if (doTest == 1) {
      doOperationTests("afterBegin");
      doTest = 0;
    } else {
      if (doTest == 8) {
        doSetRollbackOnlyTest("afterBegin");
        doTest = 0;
      }
    }
  }

  public void beforeCompletion() {
    TestUtil.logTrace("beforeCompletion");
    if (doTest == 2) {
      doOperationTests("beforeCompletion");
      doTest = 0;
    } else {
      if (doTest == 9) {
        doSetRollbackOnlyTest("beforeCompletion");
        doTest = 0;
      }
    }
  }

  public void afterCompletion(boolean status) {
    TestUtil.logTrace("afterCompletion");
    if (doTest == 3) {
      doOperationTests("afterCompletion");
      doTest = 0;
    } else {
      if (doTest == 10) {
        doSetRollbackOnlyTest("afterCompletion");
        doTest = 0;
      }
    }
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public Hashtable getResults() {
    TestUtil.logTrace("getResults");
    return table;
  }

  public Hashtable getResults1() {
    TestUtil.logTrace("getResults1");
    return table1;
  }

  public void businessMethod() {
    TestUtil.logTrace("businessMethod");
    if (doTest == 4) {
      doOperationTests("businessMethod");
    } else {
      if (doTest == 11) {
        doSetRollbackOnlyTest("businessMethod");
      }
    }
  }

  public void setHelper(Helper ref) {
    TestUtil.logTrace("setHelper");
    helperRef = ref;
  }

  // ===========================================================
  // Private methods

  private int testIndex(String s) {
    TestUtil.logTrace("testIndex");
    for (int i = 0; i < tests.length; i++)
      if (s.equals(tests[i]))
        return i;
    return -1;
  }

  private void setTestList(int i) {
    TestUtil.logTrace("setTestList");
    methodList[i].setProperty("JNDI_Access", "true");
    methodList[i].setProperty("getEJBHome", "true");
    methodList[i].setProperty("getCallerPrincipal", "true");
    methodList[i].setProperty("getRollbackOnly", "true");
    methodList[i].setProperty("isCallerInRole", "true");
    methodList[i].setProperty("getEJBObject", "true");
    methodList[i].setProperty("UserTransaction", "true");
    methodList[i].setProperty("UserTransaction_Methods_Test1", "true");
    methodList[i].setProperty("UserTransaction_Methods_Test2", "true");
    methodList[i].setProperty("UserTransaction_Methods_Test3", "true");
    methodList[i].setProperty("UserTransaction_Methods_Test4", "true");
    methodList[i].setProperty("UserTransaction_Methods_Test5", "true");
    methodList[i].setProperty("UserTransaction_Methods_Test6", "true");
    methodList[i].setProperty("getEJBLocalHome", "true");
    methodList[i].setProperty("getEJBLocalObject", "true");
    methodList[i].setProperty("Timer_Methods", "true");
  }

  private void doOperationTests(String s) {
    TestUtil.logTrace("doOperationTests");
    int i = testIndex(s);
    TestUtil.logMsg("index for " + s + " is " + i);
    TestUtil.logMsg("methodList length=" + methodList.length);
    TestUtil.logMsg("tests length=" + tests.length);
    setTestList(i);
    TestUtil.logMsg("Operations testing for " + s + " method ...");

    // getEJBHome test
    try {
      sctx.getEJBHome();
      TestUtil.logMsg("Operations test: getEJBHome() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getEJBHome", "false");
      TestUtil.logMsg("Operations test: getEJBHome() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getEJBHome", "unexpected");
      TestUtil.logMsg(
          "Operations test: getEJBHome() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getCallerPrincipal test
    try {
      sctx.getCallerPrincipal();
      TestUtil.logMsg("Operations test: getCallerPrincipal() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getCallerPrincipal", "false");
      TestUtil.logMsg("Operations test: getCallerPrincipal() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getCallerPrincipal", "unexpected");
      TestUtil.logMsg(
          "Operations test: getCallerPrincipal() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getRollbackOnly test
    try {
      sctx.getRollbackOnly();
      TestUtil.logMsg("Operations test: getRollbackOnly() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getRollbackOnly", "false");
      TestUtil.logMsg("Operations test: getRollbackOnly() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getRollbackOnly", "unexpected");
      TestUtil.logMsg(
          "Operations test: getRollbackOnly() - not allowed (Unexpected Exception) - "
              + e);
    }

    // isCallerInRole test
    try {
      sctx.isCallerInRole(role);
      TestUtil.logMsg("Operations test: isCallerInRole() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("isCallerInRole", "false");
      TestUtil.logMsg("Operations test: isCallerInRole() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("isCallerInRole", "unexpected");
      TestUtil.logMsg(
          "Operations test: isCallerInRole() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getEJBObject test
    try {
      sctx.getEJBObject();
      TestUtil.logMsg("Operations test: getEJBObject() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getEJBObject", "false");
      TestUtil.logMsg("Operations test: getEJBObject() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getEJBObject", "unexpected");
      TestUtil.logMsg(
          "Operations test: getEJBObject() - not allowed (Unexpected Exception) - "
              + e);
    }

    // JNDI Access test
    try {
      Context ctx = (Context) nctx.lookup("java:comp/env");
      TestUtil.logMsg("Operations test: JNDI_Access - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("JNDI_Access", "false");
      TestUtil.logMsg("Operations test: JNDI_Access - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("JNDI_Access", "unexpected");
      TestUtil.logMsg(
          "Operations test: JNDI_Access - not allowed (Unexpected Exception) - "
              + e);
    }

    // UserTransaction Access test
    try {
      ut = sctx.getUserTransaction();
      TestUtil.logMsg("Operations test: UserTransaction - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("UserTransaction", "false");
      TestUtil.logMsg("Operations test: UserTransaction - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("UserTransaction", "unexpected");
      TestUtil.logMsg(
          "Operations test: UserTransaction - not allowed (Unexpected Exception) - "
              + e);
    }

    // UserTransaction Methods Test1
    try {
      sctx.getUserTransaction().begin();
      TestUtil.logMsg("Operations test: UserTransaction.begin - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("UserTransaction_Methods_Test1", "false");
      TestUtil.logMsg("Operations test: UserTransaction.begin - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("UserTransaction_Methods_Test1", "unexpected");
      TestUtil.logMsg(
          "Operations test: UserTransaction.begin - not allowed (Unexpected Exception) - "
              + e);
    }

    // UserTransaction Methods Test2
    try {
      sctx.getUserTransaction().commit();
      TestUtil.logMsg("Operations test: UserTransaction.commit - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("UserTransaction_Methods_Test2", "false");
      TestUtil.logMsg("Operations test: UserTransaction.commit - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("UserTransaction_Methods_Test2", "unexpected");
      TestUtil.logMsg("Operations test: UserTransaction.commit"
          + " - not allowed (Unexpected Exception) - " + e);
    }

    // UserTransaction Methods Test3
    try {
      sctx.getUserTransaction().getStatus();
      TestUtil.logMsg("Operations test: UserTransaction.getStatus - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("UserTransaction_Methods_Test3", "false");
      TestUtil
          .logMsg("Operations test: UserTransaction.getStatus - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("UserTransaction_Methods_Test3", "unexpected");
      TestUtil.logMsg("Operations test: UserTransaction.getStatus"
          + " - not allowed (Unexpected Exception) - " + e);
    }

    // UserTransaction Methods Test4
    try {
      sctx.getUserTransaction().rollback();
      TestUtil.logMsg("Operations test: UserTransaction.rollback - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("UserTransaction_Methods_Test4", "false");
      TestUtil
          .logMsg("Operations test: UserTransaction.rollback - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("UserTransaction_Methods_Test4", "unexpected");
      TestUtil.logMsg("Operations test: UserTransaction.rollback"
          + " - not allowed (Unexpected Exception) - " + e);
    }

    // UserTransaction Methods Test5
    try {
      sctx.getUserTransaction().setRollbackOnly();
      TestUtil
          .logMsg("Operations test: UserTransaction.setRollbackOnly - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("UserTransaction_Methods_Test5", "false");
      TestUtil.logMsg(
          "Operations test: UserTransaction.setRollbackOnly - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("UserTransaction_Methods_Test5", "unexpected");
      TestUtil.logMsg("Operations test: UserTransaction.setRollbackOnly"
          + " - not allowed (Unexpected Exception) - " + e);
    }

    // UserTransaction Methods Test6
    try {
      sctx.getUserTransaction().setTransactionTimeout(5);
      TestUtil.logMsg(
          "Operations test: UserTransaction.setTransactionTimeout - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("UserTransaction_Methods_Test6", "false");
      TestUtil.logMsg(
          "Operations test: UserTransaction.setTransactionTimeout - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("UserTransaction_Methods_Test6", "unexpected");
      TestUtil.logMsg("Operations test: UserTransaction.setTransactionTimeout"
          + " - not allowed (Unexpected Exception) - " + e);
    }

    // getEJBLocalHome test
    try {
      sctx.getEJBLocalHome();
      TestUtil.logMsg("Operations test: getEJBLocalHome() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getEJBLocalHome", "false");
      TestUtil.logMsg("Operations test: getEJBLocalHome() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getEJBLocalHome", "unexpected");
      TestUtil.logMsg(
          "Operations test: getEJBLocalHome() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getEJBLocalObject test
    try {
      sctx.getEJBLocalObject();
      TestUtil.logMsg("Operations test: getEJBLocalObject() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getEJBLocalObject", "false");
      TestUtil.logMsg("Operations test: getEJBLocalObject() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getEJBLocalObject", "unexpected");
      TestUtil.logMsg(
          "Operations test: getEJBLocalObject() - not allowed (Unexpected Exception) - "
              + e);
    }

    // Timer Method test
    try {
      if (th != null) {
        javax.ejb.Timer t = th.getTimer();
        TestUtil.logMsg("Got timer");
        th = t.getHandle();
        TestUtil.logMsg("Got timer handle");
        long tRemaining = t.getTimeRemaining();
        TestUtil.logMsg("Time remaining is " + tRemaining);
        java.util.Date tNextTimeout = t.getNextTimeout();
        TestUtil.logMsg("Next timeout is " + tNextTimeout);
        String tInfo = (String) t.getInfo();
        TestUtil.logMsg("Timer info is " + tInfo);
        TestUtil.logMsg("Operations test: Timer_Methods() - allowed");
      } else {
        TestUtil.logMsg("Variable th is null - not set in setSessionContext");
        methodList[i].setProperty("Timer_Methods", "false");
      }
    } catch (IllegalStateException e) {
      methodList[i].setProperty("Timer_Methods", "false");
      TestUtil.logMsg("Operations test: Timer_Methods() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("Timer_Methods", "unexpected");
      TestUtil.logMsg(
          "Operations test: Timer_Methods() - not allowed (Unexpected Exception) - "
              + e);
    }

    table.put(s, methodList[i]);
  }

  private void doSetRollbackOnlyTest(String s) {
    TestUtil.logTrace("doSetRollbackOnlyTest");
    TestUtil.logMsg("Operations testing for " + s + " method ...");

    try {
      sctx.setRollbackOnly();
      setRollbackList.setProperty("setRollbackOnly", "true");
      TestUtil.logMsg("Operations test: setRollbackOnly() - allowed");
    } catch (IllegalStateException e) {
      setRollbackList.setProperty("setRollbackOnly", "false");
      TestUtil.logMsg("Operations test: setRollbackOnly() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      setRollbackList.setProperty("setRollbackOnly", "unexpected");
      TestUtil.logMsg(
          "Operations test: setRollbackOnly() - not allowed (Unexpected Exception) - "
              + e);
    }
    table1.put(s, setRollbackList);
  }

  // ===========================================================

  public void stopTestTimer() {
    try {
      TestUtil.logMsg("Obtain naming context");
      TSNamingContext nctx = new TSNamingContext();
      TestUtil.logMsg("Looking up home interface for "
          + "stateless bean creating timer: " + timerLookup);
      TimerLocalHome timerHome = (TimerLocalHome) nctx.lookup(timerLookup);
      TimerLocal timerRef = timerHome.create();
      timerRef.findAndCancelTimer();
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("stopTestTimer:" + e);
    }
  }

  private TimerHandle doTimer() {
    try {
      TestUtil.logMsg("Obtain naming context");
      TSNamingContext nctx = new TSNamingContext();
      TestUtil.logMsg("Looking up home interface for "
          + "stateless bean creating timer: " + timerLookup);
      TimerLocalHome timerHome = (TimerLocalHome) nctx.lookup(timerLookup);
      TimerLocal timerRef = timerHome.create();
      return timerRef.startTimer(60000, "info");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("startTimer:" + e);
    }
  }
}
