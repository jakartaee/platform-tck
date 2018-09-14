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
 * @(#)TestBeanEJB.java	1.27 03/05/16
 */

package com.sun.ts.tests.ejb.ee.bb.session.stateless.cm.allowedmethodstest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import javax.transaction.*;
import java.rmi.*;
import java.sql.*;
import java.security.Principal;

public class TestBeanEJB implements SessionBean, TimedObject {
  private SessionContext sctx = null;

  private TSNamingContext nctx = null;

  private Helper helperRef = null;

  private String role = "Administrator";

  private Hashtable table = new Hashtable();

  private static final String testLookup = "java:comp/env/ejb/Helper";

  private UserTransaction ut;

  // These are the method tests
  private static final String tests[] = { "ejbCreate", "setSessionContext",
      "businessMethod" };

  // This is the results of the operation tests
  private static final Properties methodList[] = { new Properties(),
      new Properties(), new Properties() };

  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
    doOperationTests("ejbCreate");
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

  public void ejbTimeout(javax.ejb.Timer timer) {
    TestUtil.logTrace("ejbTimeout");
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public Hashtable getResults() {
    TestUtil.logTrace("getResults");
    return table;
  }

  public void businessMethod(Helper ref) {
    TestUtil.logTrace("businessMethod");
    doOperationTests("businessMethod");
    try {
      ref.setData(table);
    } catch (RemoteException re) {
      TestUtil.printStackTrace(re);
      throw new EJBException(re.getMessage());
    }

  }

  public void setHelper(Helper ref) {
    TestUtil.logTrace("setHelper");
    helperRef = ref;
  }

  public void txNotSupported(Helper ref) {
    TestUtil.logTrace("txNotSupported");
    doOperationTests("businessMethod");
    try {
      ref.setData(table);
    } catch (RemoteException re) {
      TestUtil.printStackTrace(re);
      throw new EJBException(re.getMessage());
    }

  }

  public void txSupports(Helper ref) {
    TestUtil.logTrace("txSupports");
    doOperationTests("businessMethod");
    try {
      ref.setData(table);
    } catch (RemoteException re) {
      TestUtil.printStackTrace(re);
      throw new EJBException(re.getMessage());
    }

  }

  public void txNever(Helper ref) {
    TestUtil.logTrace("txNever");
    doOperationTests("businessMethod");
    try {
      ref.setData(table);
    } catch (RemoteException re) {
      TestUtil.printStackTrace(re);
      throw new EJBException(re.getMessage());
    }

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
    methodList[i].setProperty("getEJBHome", "true");
    methodList[i].setProperty("getCallerPrincipal", "true");
    methodList[i].setProperty("isCallerInRole", "true");
    methodList[i].setProperty("getEJBObject", "true");
    methodList[i].setProperty("JNDI_Access", "true");
    methodList[i].setProperty("UserTransaction", "true");
    methodList[i].setProperty("UserTransaction_Methods_Test1", "true");
    methodList[i].setProperty("UserTransaction_Methods_Test2", "true");
    methodList[i].setProperty("UserTransaction_Methods_Test3", "true");
    methodList[i].setProperty("UserTransaction_Methods_Test4", "true");
    methodList[i].setProperty("UserTransaction_Methods_Test5", "true");
    methodList[i].setProperty("UserTransaction_Methods_Test6", "true");
    methodList[i].setProperty("getEJBLocalHome", "true");
    methodList[i].setProperty("getEJBLocalObject", "true");
    methodList[i].setProperty("getTimerService", "true");
    methodList[i].setProperty("TimerService_Methods_Test1", "true");
    methodList[i].setProperty("TimerService_Methods_Test2", "true");
    methodList[i].setProperty("TimerService_Methods_Test3", "true");
    methodList[i].setProperty("TimerService_Methods_Test4", "true");
    methodList[i].setProperty("TimerService_Methods_Test5", "true");
    methodList[i].setProperty("TimerService_Methods_Test6", "true");
    methodList[i].setProperty("TimerService_Methods_Test7", "true");
    methodList[i].setProperty("getMessageContext", "true");
    methodList[i].setProperty("getRollbackOnly", "true");
    methodList[i].setProperty("setRollbackOnly", "true");
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
      TestUtil.logMsg(
          "Operations test: UserTransaction.commit - not allowed (Unexpected Exception) - "
              + e);
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
      TestUtil.logMsg(
          "Operations test: UserTransaction.getStatus - not allowed (Unexpected Exception) - "
              + e);
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
      TestUtil.logMsg(
          "Operations test: UserTransaction.rollback - not allowed (Unexpected Exception) - "
              + e);
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
      TestUtil.logMsg(
          "Operations test: UserTransaction.setRollbackOnly - not allowed (Unexpected Exception) - "
              + e);
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
      TestUtil.logMsg(
          "Operations test: UserTransaction.setTransactionTimeout - not allowed (Unexpected Exception) - "
              + e);
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

    // getTimerService test
    try {
      sctx.getTimerService();
      TestUtil.logMsg("Operations test: getTimerService() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getTimerService", "false");
      TestUtil.logMsg("Operations test: getTimerService() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getTimerService", "unexpected");
      TestUtil.logMsg(
          "Operations test: getTimerService() - not allowed (Unexpected Exception) - "
              + e);
    }

    // TimerService_Methods_Test1
    try {
      javax.ejb.TimerService timesrv = sctx.getTimerService();
      javax.ejb.Timer tt = timesrv.createTimer((long) 10000, "test1");
      tt.cancel();
      TestUtil
          .logMsg("Operations test: TimerService_Methods_Test1() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("TimerService_Methods_Test1", "false");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test1() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("TimerService_Methods_Test1", "unexpected");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test1() - not allowed (Unexpected Exception) - "
              + e);
    }

    // TimerService_Methods_Test2
    try {
      javax.ejb.TimerService timesrv2 = sctx.getTimerService();
      javax.ejb.Timer t2 = timesrv2.createTimer((long) 10000, (long) 10000,
          "test2");
      t2.cancel();
      TestUtil
          .logMsg("Operations test: TimerService_Methods_Test2() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("TimerService_Methods_Test2", "false");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test2() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("TimerService_Methods_Test2", "unexpected");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test2() - not allowed (Unexpected Exception) - "
              + e);
    }

    // TimerService_Methods_Test3
    try {
      long expiration = (System.currentTimeMillis() + (long) 900000);
      java.util.Date d = new java.util.Date(expiration);
      javax.ejb.TimerService timesrv3 = sctx.getTimerService();
      javax.ejb.Timer t3 = timesrv3.createTimer(d, "test3");
      t3.cancel();
      TestUtil
          .logMsg("Operations test: TimerService_Methods_Test3() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("TimerService_Methods_Test3", "false");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test3() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("TimerService_Methods_Test3", "unexpected");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test3() - not allowed (Unexpected Exception) - "
              + e);
    }

    // TimerService_Methods_Test4
    try {
      long expiration = (System.currentTimeMillis() + (long) 900000);
      java.util.Date d = new java.util.Date(expiration);
      javax.ejb.TimerService timesrv4 = sctx.getTimerService();
      javax.ejb.Timer t4 = timesrv4.createTimer(d, (long) 10000, "test4");
      t4.cancel();
      TestUtil
          .logMsg("Operations test: TimerService_Methods_Test4() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("TimerService_Methods_Test4", "false");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test4() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("TimerService_Methods_Test4", "unexpected");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test4() - not allowed (Unexpected Exception) - "
              + e);
    }

    // TimerService_Methods_Test5
    try {
      javax.ejb.TimerService ts = sctx.getTimerService();
      Collection ccol = ts.getTimers();
      TestUtil
          .logMsg("Operations test: TimerService_Methods_Test5() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("TimerService_Methods_Test5", "false");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test5() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("TimerService_Methods_Test5", "unexpected");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test5() - not allowed (Unexpected Exception) - "
              + e);
    }

    // TimerService_Methods_Test6
    try {
      javax.ejb.TimerService timesrv6 = sctx.getTimerService();
      javax.ejb.Timer t6 = timesrv6.createTimer((long) 10000, "test6");
      t6.getHandle();
      TestUtil
          .logMsg("Operations test: TimerService_Methods_Test6() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("TimerService_Methods_Test6", "false");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test6() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("TimerService_Methods_Test6", "unexpected");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test6() - not allowed (Unexpected Exception) - "
              + e);
    }

    // TimerService_Methods_Test7
    try {
      javax.ejb.TimerService timesrv7 = sctx.getTimerService();
      javax.ejb.Timer t7 = timesrv7.createTimer((long) 10000, "test7");
      t7.cancel();
      TestUtil
          .logMsg("Operations test: TimerService_Methods_Test7() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("TimerService_Methods_Test7", "false");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test7() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("TimerService_Methods_Test7", "unexpected");
      TestUtil.logMsg(
          "Operations test: TimerService_Methods_Test7() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getMessageContext test
    try {
      sctx.getMessageContext();
      TestUtil.logMsg("Operations test: getMessageContext() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getMessageContext", "false");
      TestUtil.logMsg("Operations test: getMessageContext() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getMessageContext", "unexpected");
      TestUtil.logMsg(
          "Operations test: getMessageContext() - not allowed (Unexpected Exception) - "
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

    // setRollbackOnly test
    try {
      sctx.setRollbackOnly();
      TestUtil.logMsg("Operations test: setRollbackOnly() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("setRollbackOnly", "false");
      TestUtil.logMsg("Operations test: setRollbackOnly() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("setRollbackOnly", "unexpected");
      TestUtil.logMsg(
          "Operations test: setRollbackOnly() - not allowed (Unexpected Exception) - "
              + e);
    }

    table.put(s, methodList[i]);
  }

  public void initLogging(Properties p) {
    TestUtil.logTrace("initLogging");
    try {
      TestUtil.init(p);
    } catch (RemoteLoggingInitException e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  public void findAndCancelTimer() {
    try {
      TestUtil.logTrace("findTimer method entered");
      javax.ejb.TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("find Timers");
      Collection ccol = ts.getTimers();
      Iterator i = ccol.iterator();
      while (i.hasNext()) {
        javax.ejb.Timer t = (javax.ejb.Timer) i.next();
        t.cancel();
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("findTimer:" + e);
    }
  }

  public boolean getCallerPrincipalTest(String s) {
    TestUtil.logTrace("getCallerPrincipalTest");
    try {
      Principal principal = sctx.getCallerPrincipal();
      if (principal != null) {
        TestUtil
            .logMsg("getCallerPrincipal() returned Principal: " + principal);
        String name = principal.getName();
        if (name.indexOf(s) < 0) {
          TestUtil.logErr("principal - expected: " + s + ", received: " + name);
          return false;
        } else
          return true;
      } else {
        TestUtil.logErr("getCallerPrincipal() returned null reference");
        return false;
      }
    } catch (Exception e) {
      TestUtil.logErr("Caught exception: " + e.getMessage());
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
  }

  // ===========================================================

}
