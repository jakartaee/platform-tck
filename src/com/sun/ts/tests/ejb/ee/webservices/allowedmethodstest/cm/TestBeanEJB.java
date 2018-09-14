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
 * @(#)TestBeanEJB.java	1.2 03/05/16
 */

package com.sun.ts.tests.ejb.ee.webservices.allowedmethodstest.cm;

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

  private String role = "Administrator";

  private String info = "Hello";

  private Properties props = null;

  private Hashtable results = null;

  private boolean SKIP = false;

  private UserTransaction ut;

  private Hashtable table = new Hashtable();

  private String expected[] = { "true", "true", "true", "true", "true", "false",
      "false", "false", "false", "false", "false", "false", "true", "true",
      "true", "true", "true", "true", "true" };

  // These are the method tests
  private static final String tests[] = { "businessMethod" };

  // This is the results of the operation tests
  private static final Properties methodList[] = { new Properties() };

  public void ejbCreate() throws CreateException {
    TestUtil.logTrace("ejbCreate");
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

  public boolean businessMethod() {
    boolean pass = false;
    try {
      TestUtil.logTrace("businessMethod");
      doOperationTests("businessMethod");
      Hashtable results = getResults();
      pass = checkResults(results, "businessMethod", expected);
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      throw new EJBException(e.getMessage());
    }
    return pass;

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
    methodList[i].setProperty("Timer_Service_Methods", "true");
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
      Principal principal = sctx.getCallerPrincipal();
      if (principal != null) {
        TestUtil
            .logMsg("getCallerPrincipal() returned Principal: " + principal);
      } else {
        TestUtil.logErr("getCallerPrincipal() returned null reference");
        methodList[i].setProperty("getCallerPrincipal", "unexpected");
      }
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
      TestUtil.logMsg("Operations test: UserTransaction.begin"
          + " - not allowed (Unexpected Exception) -" + e);
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
      TestUtil.logMsg("Operations test: UserTransaction.commit "
          + " - not allowed (Unexpected Exception) -" + e);
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
      TestUtil.logMsg("Operations test: UserTransaction.getStatus "
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
          + "- not allowed (Unexpected Exception) - " + e);
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
      TestUtil.logMsg("Operations test: UserTransaction.setRollbackOnly "
          + "- not allowed (Unexpected Exception) - " + e);
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
      TestUtil.logMsg("Operations test: UserTransaction.setTransactionTimeout "
          + "- not allowed (Unexpected Exception) - " + e);
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
      TestUtil.logMsg("Operations test: getEJBLocalHome() "
          + "- not allowed (Unexpected Exception) - " + e);
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
      TestUtil.logMsg("Operations test: getEJBLocalObject() "
          + "not allowed (Unexpected Exception) - " + e);
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

    // Timer_Service_Method test
    try {
      javax.ejb.TimerService timesrv = sctx.getTimerService();
      javax.ejb.Timer tt = timesrv.createTimer((long) 10000, "info");
      javax.ejb.TimerHandle th = tt.getHandle();
      TestUtil.logTrace("getTimers:  " + th.getTimer());
      tt.cancel();
      TestUtil.logMsg("Operations test: Timer_Service_Methods() - allowed");
    } catch (IllegalStateException e) {
      methodList[i].setProperty("Timer_Service_Methods", "false");
      TestUtil.logMsg("Operations test: Timer_Service_Methods() - not allowed");
    } catch (Exception e) {
      TestUtil.printStackTrace(e);
      methodList[i].setProperty("Timer_Service_Methods", "unexpected");
      TestUtil.logMsg(
          "Operations test: Timer_Service_Methods() - not allowed (Unexpected Exception) - "
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

  private boolean checkResults(Hashtable results, String method, String r[]) {
    TestUtil.logTrace("checkResults");
    TestUtil.logMsg("-----------------------------------------------------");
    boolean pass = true;
    TestUtil.logMsg("Getting results for method: (" + method + ")");
    if (results == null) {
      TestUtil.logMsg("ERROR: Results object not found for method (" + method
          + ") ... Skipping");
      SKIP = true;
      return false;
    }
    Properties p = (Properties) results.get(method);
    if (p == null) {
      TestUtil.logMsg("ERROR: Property object not found for method (" + method
          + ") ... Skipping");
      SKIP = true;
      return false;
    }
    TestUtil.list(p);
    if (!p.getProperty("getEJBHome").equals(r[0])) {
      TestUtil.logErr("getEJBHome operations test failed");
      pass = false;
    }
    if (!p.getProperty("getCallerPrincipal").equals(r[1])) {
      TestUtil.logErr("getCallerPrincipal operations test failed");
      pass = false;
    }
    if (!p.getProperty("isCallerInRole").equals(r[2])) {
      TestUtil.logErr("isCallerInRole operations test failed");
      pass = false;
    }
    if (!p.getProperty("getEJBObject").equals(r[3])) {
      TestUtil.logErr("getEJBObject operations test failed");
      pass = false;
    }
    if (!p.getProperty("JNDI_Access").equals(r[4])) {
      TestUtil.logErr("JNDI_Access operations test failed");
      pass = false;
    }

    if (!p.getProperty("UserTransaction").equals(r[5])) {
      TestUtil.logErr("UserTransaction operations test failed");
      pass = false;
    }

    if (!p.getProperty("UserTransaction_Methods_Test1").equals(r[6])) {
      TestUtil.logErr("UserTransaction_Methods_Test1 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test2").equals(r[7])) {
      TestUtil.logErr("UserTransaction_Methods_Test2 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test3").equals(r[8])) {
      TestUtil.logErr("UserTransaction_Methods_Test3 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test4").equals(r[9])) {
      TestUtil.logErr("UserTransaction_Methods_Test4 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test5").equals(r[10])) {
      TestUtil.logErr("UserTransaction_Methods_Test5 operations test failed");
      pass = false;
    }
    if (!p.getProperty("UserTransaction_Methods_Test6").equals(r[11])) {
      TestUtil.logErr("UserTransaction_Methods_Test6 operations test failed");
      pass = false;
    }

    if (!p.getProperty("getEJBLocalHome").equals(r[12])) {
      TestUtil.logErr("getEJBLocalHome operations test failed");
      pass = false;
    }
    if (!p.getProperty("getEJBLocalObject").equals(r[13])) {
      TestUtil.logErr("getEJBLocalObject operations test failed");
      pass = false;
    }

    if (!p.getProperty("getTimerService").equals(r[14])) {
      TestUtil.logErr("getTimerService operations test failed");
      pass = false;
    }

    if (!p.getProperty("Timer_Service_Methods").equals(r[15])) {
      TestUtil.logErr("Timer_Service_Methods operations test failed");
      pass = false;
    }

    if (!p.getProperty("getMessageContext").equals(r[16])) {
      TestUtil.logErr("getMessageContext operations test failed");
      pass = false;
    }

    if (!p.getProperty("getRollbackOnly").equals(r[17])) {
      TestUtil.logErr("getRollbackOnly operations test failed");
      pass = false;
    }

    if (!p.getProperty("setRollbackOnly").equals(r[18])) {
      TestUtil.logErr("setRollbackOnly operations test failed");
      pass = false;
    }

    if (pass) {
      TestUtil.logMsg("All operation tests passed as expected ...");
    } else if (SKIP) {
      TestUtil.logMsg("ERROR: Unable to obtain test results");
      SKIP = false;
    } else {
      TestUtil.logErr("Not All operation tests passed - unexpected ...");
      TestUtil.logMsg("-----------------------------------------------------");
    }
    return pass;
  }

  // ===========================================================

}
