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
 * @(#)TestBeanEJB.java	1.16 03/05/27
 */

package com.sun.ts.tests.ejb.ee.bb.entity.cmp20.allowedmethodstest;

import com.sun.ts.lib.util.*;
import com.sun.ts.lib.porting.*;

import java.util.*;
import javax.ejb.*;
import javax.naming.*;
import java.rmi.*;
import java.sql.*;
import java.security.Principal;

public abstract class TestBeanEJB implements EntityBean, TimedObject {
  private EntityContext ectx = null;

  private Properties harnessProps = null;

  private TSNamingContext nctx = null;

  private String role = "Administrator";

  private Hashtable table = new Hashtable();

  private static final String testLookup = "java:comp/env/ejb/Helper";

  // These are the method tests
  private static final String tests[] = { "ejbCreate", "ejbPostCreate",
      "setEntityContext", "businessMethod", "ejbLoad", "ejbStore",
      "ejbHomeDoTest", "unsetEntityContext" };

  // This is the results of the operation tests
  private static final Properties methodList[] = { new Properties(),
      new Properties(), new Properties(), new Properties(), new Properties(),
      new Properties(), new Properties(), new Properties() };

  private boolean ejbLoadCalled = false;

  private boolean ejbStoreCalled = false;

  // ===========================================================
  // getters and setters for cmp fields

  public abstract Integer getId();

  public abstract void setId(Integer i);

  public abstract String getBrandName();

  public abstract void setBrandName(String s);

  public abstract float getPrice();

  public abstract void setPrice(float p);

  // ===========================================================

  public Integer ejbCreate(Properties p, Helper ref, int id, String brandName,
      float price, int flag) throws CreateException {
    TestUtil.logTrace("ejbCreate");
    Integer pk = new Integer(id);
    try {
      setId(pk);
      setBrandName(brandName);
      setPrice(price);
      TestUtil.init(p);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    if (flag == 1)
      doOperationTests("ejbCreate");
    try {
      ref.setData(table);
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception: " + e, e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(Properties p, Helper ref, int id, String brandName,
      float price, int flag) throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
    if (flag == 2)
      doOperationTests("ejbPostCreate");
    try {
      ref.setData(table);
    } catch (Exception e) {
      TestUtil.logErr("Caught Exception: " + e, e);
      throw new CreateException("Exception occurred: " + e);
    }
  }

  public Integer ejbCreate(Properties p, int id, String brandName, float price)
      throws CreateException {
    TestUtil.logTrace("ejbCreate");
    Integer pk = new Integer(id);
    try {
      setId(pk);
      setBrandName(brandName);
      setPrice(price);
      TestUtil.init(p);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new CreateException("Exception occurred: " + e);
    }
    return null;
  }

  public void ejbPostCreate(Properties p, int id, String brandName, float price)
      throws CreateException {
    TestUtil.logTrace("ejbPostCreate");
  }

  public void setEntityContext(EntityContext c) {
    TestUtil.logTrace("setEntityContext");
    ectx = c;
    try {
      TestUtil.logMsg("Obtain naming context");
      nctx = new TSNamingContext();
    } catch (Exception e) {
      TestUtil.logErr("ERROR: Exception Caught ... " + e, e);
      throw new EJBException("unable to obtain naming context");
    }
    doOperationTests("setEntityContext");
  }

  public void unsetEntityContext() {
    TestUtil.logTrace("unsetEntityContext");
    doOperationTests("unsetEntityContext");
  }

  public void ejbLoad() {
    TestUtil.logTrace("ejbLoad");
    if (ejbLoadCalled)
      return;
    doOperationTests("ejbLoad");
    ejbLoadCalled = true;
    ejbStoreCalled = false;
  }

  public void ejbStore() {
    TestUtil.logTrace("ejbStore");
    if (ejbStoreCalled)
      return;
    doOperationTests("ejbStore");
    ejbStoreCalled = true;
    ejbLoadCalled = false;
  }

  public void ejbRemove() throws RemoveException {
    TestUtil.logTrace("ejbRemove");
    TestUtil.logMsg("PrimaryKey=" + ectx.getPrimaryKey());
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

  public TestBean ejbHomeDoTest(Helper ref) {
    TestUtil.logTrace("ejbHomeDoTest");
    try {
      doOperationTests("ejbHomeDoTest");
      ref.setData(table);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
    return null;
  }

  // ===========================================================
  // TestBean interface (our business methods)

  public Hashtable getResults() {
    TestUtil.logTrace("getResults");
    return table;
  }

  public void businessMethod(Helper ref) {
    TestUtil.logTrace("businessMethod");
    try {
      doOperationTests("businessMethod");
      ref.setData(table);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
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
    methodList[i].setProperty("getPrimaryKey", "true");
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
    methodList[i].setProperty("getRollbackOnly", "true");
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
      ectx.getEJBHome();
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
      ectx.getCallerPrincipal();
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
      ectx.isCallerInRole(role);
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
      ectx.getEJBObject();
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
      TestUtil.logMsg("Operations test: JNDI_Access() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("JNDI_Access", "unexpected");
      TestUtil.logMsg(
          "Operations test: JNDI_Access() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getPrimaryKey test
    try {
      ectx.getPrimaryKey();
      TestUtil.logMsg("Operations test: getPrimaryKey() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("getPrimaryKey", "false");
      TestUtil.logMsg("Operations test: getPrimaryKey() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("getPrimaryKey", "unexpected");
      TestUtil.logMsg(
          "Operations test: getPrimaryKey() - not allowed (Unexpected Exception) - "
              + e);
    }

    // getEJBLocalHome test
    try {
      ectx.getEJBLocalHome();
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
      ectx.getEJBLocalObject();
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
      ectx.getTimerService();
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
      javax.ejb.TimerService timesrv = ectx.getTimerService();
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
      javax.ejb.TimerService timesrv2 = ectx.getTimerService();
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
      javax.ejb.TimerService timesrv3 = ectx.getTimerService();
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
      javax.ejb.TimerService timesrv4 = ectx.getTimerService();
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
      javax.ejb.TimerService ts = ectx.getTimerService();
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
      javax.ejb.TimerService timesrv6 = ectx.getTimerService();
      javax.ejb.Timer t6 = timesrv6.createTimer((long) 10000, "test6");
      t6.getHandle();
      t6.cancel();
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
      javax.ejb.TimerService timesrv7 = ectx.getTimerService();
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

    // getRollbackOnly test
    try {
      ectx.getRollbackOnly();
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

    table.put(s, methodList[i]);
  }

  // ===========================================================

  public void findAndCancelTimer() {
    Collection ccol = null;
    try {
      TestUtil.logTrace("findAndCancelTimer method entered");
      javax.ejb.TimerService ts = ectx.getTimerService();
      TestUtil.logTrace("Get Timers");
      ccol = ts.getTimers();
      if (ccol.size() != 0) {
        TestUtil.logTrace("Collection size is: " + ccol.size());
        Iterator i = ccol.iterator();
        while (i.hasNext()) {
          TestUtil.logTrace("Get next timer");
          javax.ejb.Timer t = (javax.ejb.Timer) i.next();
          TestUtil.logTrace("Next timer to Cancel: " + t.getInfo());
          t.cancel();
        }
      } else {
        TestUtil.logTrace("Timer Collection is null");
      }
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException("findAndCancelTimer: " + e);
    }
  }

  public boolean getCallerPrincipalTest(String s) {
    TestUtil.logTrace("getCallerPrincipalTest");
    try {
      Principal principal = ectx.getCallerPrincipal();
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
}
