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

/*
 * @(#)TimerImpl.java	1.3 02/08/21
 */

package com.sun.ts.tests.ejb.ee.timer.common;

import com.sun.ts.tests.ejb.ee.timer.helper.*;
import com.sun.ts.lib.util.*;

import java.io.Serializable;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Enumeration;
import java.rmi.RemoteException;
import javax.ejb.*;
import javax.naming.Context;
import javax.transaction.UserTransaction;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueConnection;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.QueueSession;
import javax.jms.QueueSender;
import javax.jms.QueueReceiver;
import javax.jms.QueueBrowser;
import javax.jms.Message;
import javax.jms.TextMessage;
import java.security.Principal;

public class TimerImpl {

  // used as parameter to create a timer with a string info value
  public static final String INFOSTRING = "info";

  // message sent when JMS receive call times out
  public static final String NOMSGRECEIVED = "no valid message received";

  public static final String NOTIMER_FOUND = "no timer found";

  public static final String TIMER_EXISTS = "at least one timer found";

  // messages from ejbTimeout
  public static final String ACCESS_OK = "hello from ejbTimeout";

  public static final String CHKMETH_OK = "checked method successfully accessed in ejbTimeout";

  public static final String CHKMETH_FAIL = "checked method access failed in ejbTimeout";

  public static final String RETRY_OK = "ejbTimeout has been successfully retried";

  public static final String RETRY_FAIL = "Retry of ejbTimeout resulted in an exception";

  public static final String ROLLBACK_OK = "Transaction has been successfully rolled back in ejbTimeout";

  public static final String ROLLBACK_FAIL = "Transaction rollback failed in ejbTimeout";

  public static final String SERIALIZE_OK = "Timer handle successfully serialized in ejbTimeout";

  public static final String SERIALIZE_FAIL = "Timer handle serialization failed in ejbTimeout";

  public static final String INVALID_ACTION = "Invalid action specified in ejbTimeout";

  public static final String ALLOW_OK = "allowed methods test successful in ejbTimeout";

  public static final String ALLOW_FAIL = "allowed methods test failed in ejbTimeout";

  // used to create date timers
  public static final long EXPIRATION_TIME = 1000000;

  // public static final interval time for interval timers
  public static final long INTERVAL_TIME = 10000;

  // these values determine the action taken by the ejbTimeout method
  public static final int NOMSG = -1;

  public static final int ACCESS = 1;

  public static final int CHKMETH = 2;

  public static final int RETRY = 3;

  public static final int ROLLBACK = 4;

  public static final int SERIALIZE = 5;

  public static final int ALLOWED = 6;

  // these values determine the type of timer to be created
  public static final int TIMER_SINGLEEVENT = 1;

  public static final int TIMER_INTERVAL = 2;

  public static final int TIMER_DATE = 3;

  public static final int TIMER_DATE_INTERVAL = 4;

  // keys for entity beans created
  public static final int TESTBEAN_KEY = 1;

  public static final int FLAGSTORE_KEY = 2;

  // bean flavors
  public static final int SESSION = 1;

  public static final int ENTITY = 2;

  // jndi names
  public static final String CHKMETH_BEAN = "java:comp/env/ejb/CheckedMethod";

  public static final String FLAGSTORE_BEAN = "java:comp/env/ejb/FlagStore";

  /*
   * Methods implementing the Timer interface
   *
   */

  protected static Timer createSingleEventTimer(long duration,
      Serializable info, TimerService ts) throws Exception {

    Timer t = ts.createTimer(duration, info);
    TestUtil.logTrace("created new single-event timer");
    return t;
  }

  protected static Timer createIntervalTimer(long duration, Serializable info,
      TimerService ts) throws Exception {

    Timer t = ts.createTimer(duration, INTERVAL_TIME, info);
    TestUtil.logTrace("created new interval timer");
    return t;
  }

  protected static Timer createDateTimer(Date date, Serializable info,
      TimerService ts) throws Exception {

    Timer t = ts.createTimer(date, info);
    TestUtil.logTrace("created new date timer");
    return t;
  }

  protected static Timer createDateIntervalTimer(Date date, long duration,
      Serializable info, TimerService ts) throws Exception {

    Timer t = ts.createTimer(date, duration, info);
    TestUtil.logTrace("created new date interval timer");
    return t;
  }

  protected static TimerHandle createSingleEventTimerHandle(long duration,
      Serializable info, TimerService ts) throws Exception {

    Timer t = ts.createTimer(duration, info);
    TestUtil.logTrace("created new single-event timer");
    return t.getHandle();
  }

  protected static TimerHandle createIntervalTimerHandle(long duration,
      Serializable info, TimerService ts) throws Exception {

    Timer t = ts.createTimer(duration, INTERVAL_TIME, info);
    TestUtil.logTrace("created new interval timer");
    return t.getHandle();
  }

  protected static TimerHandle createDateTimerHandle(Date date,
      Serializable info, TimerService ts) throws Exception {

    Timer t = ts.createTimer(date, info);
    TestUtil.logTrace("created new date timer");
    return t.getHandle();
  }

  protected static TimerHandle createDateIntervalTimerHandle(Date date,
      long duration, Serializable info, TimerService ts) throws Exception {

    Timer t = ts.createTimer(date, duration, info);
    TestUtil.logTrace("created new date interval timer");
    return t.getHandle();
  }

  public static Serializable getInfo(TimerHandle handle) throws Exception {
    Timer t = handle.getTimer();
    return t.getInfo();
  }

  public static void cancelTimer(TimerHandle timerHandle) throws Exception {
    Timer t = timerHandle.getTimer();
    t.cancel();
  }

  public static boolean verifyNoTimers(TimerService ts) throws Exception {
    ArrayList al = new ArrayList(ts.getTimers());
    return (al.isEmpty());
  }

  public static void cancelAllTimers(TimerService ts) throws Exception {

    try {
      ArrayList al = new ArrayList(ts.getTimers());
      if (!al.isEmpty()) {

        int numTimers = al.size();
        TestUtil.logTrace("Number of timers to be cancelled is " + numTimers);
        for (int i = 0; i < numTimers; ++i) {
          Timer timer = (Timer) al.get(i);
          TestUtil.logTrace("Timer info is " + timer.getInfo());
          timer.cancel();
        }
      } else
        TestUtil.logTrace("No timers to cancel");
    } catch (Exception e) {
      handleException("cancelAllTimers", e);
    }
  }

  /*
   * Methods Implementing Test Logic
   *
   */

  public static Timer createTimer(int timerType, Serializable info,
      TimerService ts) throws Exception {

    Timer timer;

    switch (timerType) {
    case TIMER_SINGLEEVENT:
      TestUtil.logTrace("calling createSingleEventTimer");
      timer = createSingleEventTimer(getTimeout(), info, ts);
      break;

    case TIMER_INTERVAL:
      TestUtil.logTrace("calling createIntervalTimer");
      timer = createIntervalTimer(getTimeout(), info, ts);
      break;

    case TIMER_DATE:
      TestUtil.logTrace("calling createDateTimer");
      timer = createDateTimer(getExpirationDate(), info, ts);
      break;

    case TIMER_DATE_INTERVAL:
      TestUtil.logTrace("calling createDateIntervalTimer");
      timer = createDateIntervalTimer(getExpirationDate(), getTimeout(), info,
          ts);
      break;

    default:
      throw new TimerImplException("invalid timer type specified");
    }
    return timer;
  }

  public static TimerHandle createTimerHandle(int timerType, Serializable info,
      TimerService ts) throws Exception {

    TimerHandle handle;

    switch (timerType) {
    case TIMER_SINGLEEVENT:
      TestUtil.logTrace("calling createSingleEventTimerHandle");
      handle = createSingleEventTimerHandle(getTimeout(), info, ts);
      break;

    case TIMER_INTERVAL:
      TestUtil.logTrace("calling createIntervalTimerHandle");
      handle = createIntervalTimerHandle(getTimeout(), info, ts);
      break;

    case TIMER_DATE:
      TestUtil.logTrace("calling createDateTimerHandle");
      handle = createDateTimerHandle(getExpirationDate(), info, ts);
      break;

    case TIMER_DATE_INTERVAL:
      TestUtil.logTrace("calling createDateIntervalTimerHandle");
      handle = createDateIntervalTimerHandle(getExpirationDate(), getTimeout(),
          info, ts);
      break;

    default:
      throw new TimerImplException("invalid timer type specified");
    }
    return handle;
  }

  public static boolean accessCheckedMethod(TSNamingContext nctx) {

    CheckedMethodHome beanHome;
    CheckedMethod beanRef = null;

    TestUtil.logTrace("Creating checked method bean...");
    try {
      beanHome = (CheckedMethodHome) nctx.lookup(CHKMETH_BEAN,
          CheckedMethodHome.class);
      beanRef = (CheckedMethod) beanHome.create();
      beanRef.initLogging(TestUtil.getProperties());

      TestUtil.logTrace("Attempting to access checked method...");
      return (beanRef.isAuthz()) ? true : false;
    } catch (Exception e) {
      handleException("accessCheckedMethod", e);
      return false;
    } finally {
      if (beanRef != null) {
        try {
          beanRef.remove();
        } catch (Exception e) {
          TimerImpl.handleException("Exception while removing bean", e);
        }
      }
    }
  }

  public static boolean isSerializable(TimerHandle handle) {

    if (handle == null) {
      TestUtil.logErr("Timer handle is null");
      return false;
    }
    if (!(handle instanceof Serializable)) {
      TestUtil.logErr("Timer handle is not serializable");
      return false;
    }

    return true;
  }

  public static TimerHandle getTimerHandleFromEjbTimeout(TimerService ts,
      int timeoutInfo) {

    TimerHandle handle;

    try {

      ArrayList al = new ArrayList(ts.getTimers());

      if (al.size() != 1) {
        TestUtil.logErr("Wrong number of timers found: " + al.size());
        return null;
      }

      Timer timer = (Timer) al.get(0);
      Serializable sz = timer.getInfo();
      if (!(sz instanceof Integer)) {
        TestUtil.logErr("Timer.getInfo() returns non-Integer");
        return null;
      }

      int timerInfo = ((Integer) sz).intValue();
      TestUtil.logTrace("Value of timer info is " + timerInfo);
      if (timerInfo != timeoutInfo) {
        TestUtil.logErr("Timer info received not equal to timer info "
            + "passed: " + timerInfo + " vs. " + timeoutInfo);
        return null;
      }

      handle = timer.getHandle();
    } catch (Exception e) {
      handleException("getTimerHandleFromEJBTimeout", e);
      return null;
    }
    return handle;
  }

  public static TimerHandle getDeserializedHandle(TimerHandle handle) {

    TimerHandle deserializedHandle;

    try {
      TestUtil.logTrace("write object output stream to byte array");
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream os = new ObjectOutputStream(baos);
      os.writeObject(handle);

      TestUtil.logTrace("read object output stream from byte array");
      byte[] b = baos.toByteArray();
      ByteArrayInputStream bais = new ByteArrayInputStream(b);
      ObjectInputStream is = new ObjectInputStream(bais);
      deserializedHandle = (TimerHandle) is.readObject();
    } catch (Exception e) {
      handleException("getDeserializedHandle", e);
      return null;
    }
    return deserializedHandle;
  }

  public static boolean timersAreIdentical(TimerHandle th1, TimerHandle th2) {
    Timer timer1, timer2;

    try {
      timer1 = th1.getTimer();
      if (timer1 == null) {
        TestUtil.logErr("First timer handle references null timer");
        return false;
      }

      timer2 = th2.getTimer();
      if (timer2 == null) {
        TestUtil.logErr("Second timer handle references null timer");
        return false;
      }

      return timer1.equals(timer2);
    } catch (Exception e) {
      handleException("timersAreIdentical", e);
    }
    return false;
  }

  private static Properties setTestList() {

    TestUtil.logTrace("setTestList");
    Properties props = new Properties();

    props.setProperty("JNDI_Access", "true");
    props.setProperty("getEJBHome", "true");
    props.setProperty("getCallerPrincipal", "true");
    props.setProperty("getRollbackOnly", "true");
    props.setProperty("isCallerInRole", "true");
    props.setProperty("setRollbackOnly", "true");
    props.setProperty("getEJBObject", "true");
    props.setProperty("UserTransaction", "true");
    props.setProperty("UserTransaction_Methods_Test1", "true");
    props.setProperty("UserTransaction_Methods_Test2", "true");
    props.setProperty("UserTransaction_Methods_Test3", "true");
    props.setProperty("UserTransaction_Methods_Test4", "true");
    props.setProperty("UserTransaction_Methods_Test5", "true");
    props.setProperty("UserTransaction_Methods_Test6", "true");
    props.setProperty("getEJBLocalHome", "true");
    props.setProperty("getEJBLocalObject", "true");
    props.setProperty("getPrimaryKey", "true");
    props.setProperty("getTimerService", "true");
    props.setProperty("Timer_Service_Methods", "true");

    return props;
  }

  public static Properties doOperationTests(EJBContext ejbctx,
      TSNamingContext nctx, Timer timer, boolean[] skip, String role,
      int beanType) {

    TestUtil.logTrace("doOperationTests");
    Properties props = setTestList();

    // getEJBHome test
    if (!skip[0]) {
      try {
        ejbctx.getEJBHome();
        TestUtil.logTrace("Operations test: getEJBHome() - allowed");
      } catch (IllegalStateException e) {
        props.setProperty("getEJBHome", "false");
        TestUtil.logTrace("Operations test: getEJBHome() - not allowed");
      } catch (Exception e) {
        props.setProperty("getEJBHome", "unexpected");
        TestUtil.logTrace(
            "Operations test: getEJBHome() - not allowed (Unexpected Exception) - "
                + e);
      }
    }

    // getCallerPrincipal test
    if (!skip[1]) {
      try {
        Principal principal = ejbctx.getCallerPrincipal();
        TestUtil.logTrace("Operations test: getCallerPrincipal() - allowed");
        if (principal != null) {
          TestUtil.logTrace("getCallerPrincipal() non-null value test passed");
          TestUtil.logTrace("returned Principal: " + principal);
          props.setProperty("getCallerPrincipal", "true");
        } else {
          TestUtil.logTrace("getCallerPrincipal() non-null value test failed");
          props.setProperty("getCallerPrincipal", "unexpected");
        }
      } catch (IllegalStateException e) {
        props.setProperty("getCallerPrincipal", "false");
        TestUtil
            .logTrace("Operations test: getCallerPrincipal() - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("getCallerPrincipal", "unexpected");
        TestUtil.logTrace(
            "Operations test: getCallerPrincipal() - not allowed (Unexpected Exception) - "
                + e);
      }
    }

    // getRollbackOnly test
    if (!skip[2]) {
      try {
        ejbctx.getRollbackOnly();
        TestUtil.logTrace("Operations test: getRollbackOnly() - allowed");
      } catch (IllegalStateException e) {
        props.setProperty("getRollbackOnly", "false");
        TestUtil.logTrace("Operations test: getRollbackOnly() - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("getRollbackOnly", "unexpected");
        TestUtil.logTrace(
            "Operations test: getRollbackOnly() - not allowed (Unexpected Exception) - "
                + e);
      }
    }

    // isCallerInRole test
    if (!skip[3]) {
      try {
        ejbctx.isCallerInRole(role);
        TestUtil.logTrace("Operations test: isCallerInRole() - allowed");
      } catch (IllegalStateException e) {
        props.setProperty("isCallerInRole", "false");
        TestUtil.logTrace("Operations test: isCallerInRole() - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("isCallerInRole", "unexpected");
        TestUtil.logTrace(
            "Operations test: isCallerInRole() - not allowed (Unexpected Exception) - "
                + e);
      }
    }

    // getEJBObject test
    if (!skip[4]) {
      try {
        EJBObject obj;
        if (beanType == SESSION)
          obj = ((SessionContext) ejbctx).getEJBObject();
        else if (beanType == ENTITY)
          obj = ((EntityContext) ejbctx).getEJBObject();
        TestUtil.logTrace("Operations test: getEJBObject() - allowed");
      } catch (IllegalStateException e) {
        props.setProperty("getEJBObject", "false");
        TestUtil.logTrace("Operations test: getEJBObject() - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("getEJBObject", "unexpected");
        TestUtil.logTrace(
            "Operations test: getEJBObject() - not allowed (Unexpected Exception) - "
                + e);
      }
    }

    // JNDI Access test
    if (!skip[5]) {
      try {
        Context ctx = (Context) nctx.lookup("java:comp/env");
        TestUtil.logTrace("Operations test: JNDI_Access - allowed");
      } catch (IllegalStateException e) {
        props.setProperty("JNDI_Access", "false");
        TestUtil.logTrace("Operations test: JNDI_Access - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("JNDI_Access", "unexpected");
        TestUtil.logTrace(
            "Operations test: JNDI_Access - not allowed (Unexpected Exception) - "
                + e);
      }
    }

    // UserTransaction Access test
    if (!skip[6]) {
      try {
        UserTransaction ut = ejbctx.getUserTransaction();
        TestUtil.logTrace("Operations test: UserTransaction - allowed");
      } catch (IllegalStateException e) {
        props.setProperty("UserTransaction", "false");
        TestUtil.logTrace("Operations test: UserTransaction - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("UserTransaction", "unexpected");
        TestUtil.logTrace(
            "Operations test: UserTransaction - not allowed (Unexpected Exception) - "
                + e);
      }
    }

    // UserTransaction Methods Test1
    if (!skip[7]) {
      try {
        ejbctx.getUserTransaction().begin();
        TestUtil.logTrace("Operations test: UserTransaction.begin - allowed");
      } catch (IllegalStateException e) {
        props.setProperty("UserTransaction_Methods_Test1", "false");
        TestUtil
            .logTrace("Operations test: UserTransaction.begin - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("UserTransaction_Methods_Test1", "unexpected");
        TestUtil.logTrace(
            "Operations test: UserTransaction.begin - not allowed (Unexpected Exception) - "
                + e);
      }
    }
    // UserTransaction Methods Test2
    if (!skip[8]) {
      try {
        ejbctx.getUserTransaction().commit();
        TestUtil
            .logTrace("Operations test: UserTransaction.commit() - allowed");
      } catch (IllegalStateException e) {
        props.setProperty("UserTransaction_Methods_Test2", "false");
        TestUtil.logTrace(
            "Operations test: UserTransaction.commit() - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("UserTransaction_Methods_Test2", "unexpected");
        TestUtil.logTrace(
            "Operations test: UserTransaction.commit - not allowed (Unexpected Exception) - "
                + e);
      }
    }
    // UserTransaction Methods Test3
    if (!skip[9]) {
      try {
        ejbctx.getUserTransaction().getStatus();
        TestUtil
            .logTrace("Operations test: UserTransaction.getStatus - allowed");
      } catch (IllegalStateException e) {
        props.setProperty("UserTransaction_Methods_Test3", "false");
        TestUtil.logTrace(
            "Operations test: UserTransaction.getStatus - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("UserTransaction_Methods_Test3", "unexpected");
        TestUtil.logTrace(
            "Operations test: UserTransaction.getStatus - not allowed (Unexpected Exception) - "
                + e);
      }
    }
    // UserTransaction Methods Test4
    if (!skip[10]) {
      try {
        ejbctx.getUserTransaction().rollback();
        TestUtil
            .logTrace("Operations test: UserTransaction.rollback - allowed");
      } catch (IllegalStateException e) {
        props.setProperty("UserTransaction_Methods_Test4", "false");
        TestUtil.logTrace(
            "Operations test: UserTransaction.rollback - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("UserTransaction_Methods_Test4", "unexpected");
        TestUtil.logTrace("Operations test: UserTransaction.rollback "
            + "- not allowed (Unexpected Exception) - " + e);
      }
    }
    // UserTransaction Access test
    if (!skip[11]) {
      try {
        ejbctx.getUserTransaction().setRollbackOnly();
        TestUtil.logTrace(
            "Operations test: UserTransaction.setRollbackOnly - allowed");
      } catch (IllegalStateException e) {
        props.setProperty("UserTransaction_Methods_Test5", "false");
        TestUtil.logTrace(
            "Operations test: UserTransaction.setRollbackOnly - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("UserTransaction_Methods_Test5", "unexpected");
        TestUtil.logTrace("Operations test: UserTransaction.setRollbackOnly "
            + "- not allowed (Unexpected Exception) - " + e);
      }
    }
    // UserTransaction Methods Test6
    if (!skip[12]) {
      try {
        ejbctx.getUserTransaction().setTransactionTimeout(5);
        TestUtil.logTrace(
            "Operations test: UserTransaction.setTransactionTimeout - allowed");
      } catch (IllegalStateException e) {
        props.setProperty("UserTransaction_Methods_Test6", "false");
        TestUtil.logTrace(
            "Operations test: UserTransaction.setTransactionTimeout - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("UserTransaction)_Methods_Test6", "unexpected");
        TestUtil
            .logTrace("Operations test: UserTransaction.setTransactionTimeout "
                + "- not allowed (Unexpected Exception) - " + e);
      }
    }
    // setRollbackOnly test
    if (!skip[13]) {
      try {
        ejbctx.setRollbackOnly();
        TestUtil.logTrace("Operations test: setRollbackOnly() - allowed");
      } catch (IllegalStateException e) {
        props.setProperty("setRollbackOnly", "false");
        TestUtil.logTrace("Operations test: setRollbackOnly() - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("setRollbackOnly", "unexpected");
        TestUtil.logTrace(
            "Operations test: setRollbackOnly() - not allowed (Unexpected Exception) - "
                + e);
      }
    }

    // getEJBLocalHome test
    if (!skip[14]) {
      try {
        ejbctx.getEJBLocalHome();
        TestUtil.logTrace("Operations test: getEJBLocalHome() - allowed");
      } catch (IllegalStateException e) {
        props.setProperty("getEJBLocalHome", "false");
        TestUtil.logTrace("Operations test: getEJBLocalHome() - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("getEJBLocalHome", "unexpected");
        TestUtil.logTrace(
            "Operations test: getEJBLocalHome() - not allowed (Unexpected Exception) - "
                + e);
      }
    }

    // getEJBLocalObject test
    if (!skip[15]) {
      try {
        EJBLocalObject obj;
        if (beanType == SESSION)
          obj = ((SessionContext) ejbctx).getEJBLocalObject();
        else if (beanType == ENTITY)
          obj = ((EntityContext) ejbctx).getEJBLocalObject();
        TestUtil
            .logTrace("Operations test: getEJBLocalLocalObject() - allowed");
      } catch (IllegalStateException e) {
        props.setProperty("getEJBLocalObject", "false");
        TestUtil.logTrace("Operations test: getEJBLocalObject() - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("getEJBLocalObject", "unexpected");
        TestUtil.logTrace(
            "Operations test: getEJBLocalObject() - not allowed (Unexpected Exception) - "
                + e);
      }
    }

    // getPrimaryKey test
    if (!skip[16]) {
      try {
        Object obj = ((EntityContext) ejbctx).getPrimaryKey();
        TestUtil.logTrace("Operations test: getPrimaryKey() - allowed");
      } catch (IllegalStateException e) {
        props.setProperty("getPrimaryKey", "false");
        TestUtil.logTrace("Operations test: getPrimaryKey() - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("getPrimaryKey", "unexpected");
        TestUtil.logTrace(
            "Operations test: getPrimaryKey() - not allowed (Unexpected Exception) - "
                + e);
      }
    }

    // getTimerService test
    if (!skip[17]) {
      try {
        ejbctx.getTimerService();
        TestUtil.logTrace("Operations test: getTimerService() - allowed");
      } catch (IllegalStateException e) {

        props.setProperty("getTimerService", "false");
        TestUtil.logTrace("Operations test: getTimerService() - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("getTimerService", "unexpected");
        TestUtil.logTrace(
            "Operations test: getTimerService() - not allowed (Unexpected Exception) - "
                + e);
      }
    }

    // Timer Service Methods test
    if (!skip[18]) {
      try {
        TimerHandle th = timer.getHandle();
        TestUtil.logTrace("Got timer handle");
        if (th != null) {
          Timer t = th.getTimer();
          TestUtil.logTrace("Got timer");
          long tRemaining = t.getTimeRemaining();
          TestUtil.logTrace("Time remaining is " + tRemaining);
          java.util.Date tNextTimeout = t.getNextTimeout();
          TestUtil.logTrace("Next timeout is " + tNextTimeout);
          Integer tInfo = (Integer) t.getInfo();
          TestUtil.logTrace("Timer info is " + tInfo.toString());
          TestUtil.logTrace("Operations test: Timer_Methods() - allowed");
        } else {
          TestUtil.logTrace("Could not obtain valid timer handle");
          props.setProperty("Timer_Methods", "false");
        }
      } catch (IllegalStateException e) {
        props.setProperty("Timer_Methods", "false");
        TestUtil.logTrace("Operations test: Timer_Methods() - not allowed");
      } catch (Exception e) {
        TestUtil.printStackTrace(e);
        props.setProperty("Timer_Methods", "unexpected");
        TestUtil.logTrace(
            "Operations test: Timer_Methods() - not allowed (Unexpected Exception) - "
                + e);
      }
    }

    return props;
  }

  public static boolean checkOperationsTestResults(Properties p,
      String expected[], boolean skip[]) {
    TestUtil.logTrace("checkOperationsTestResults");
    TestUtil.logTrace("-----------------------------------------------------");
    boolean pass = true;

    if (p == null) {
      TestUtil.logErr("ERROR: Property object not found");
      return false;
    }
    TestUtil.list(p);

    if (!skip[0] && !p.getProperty("getEJBHome").equals(expected[0])) {
      TestUtil.logErr("getEJBHome operations test failed");
      pass = false;
    }

    if (!skip[1] && !p.getProperty("getCallerPrincipal").equals(expected[1])) {
      TestUtil.logErr("getCallerPrincipal operations test failed");
      pass = false;
    }

    if (!skip[2] && !p.getProperty("getRollbackOnly").equals(expected[2])) {
      TestUtil.logErr("getRollbackOnly operations test failed");
      pass = false;
    }

    if (!skip[3] && !p.getProperty("isCallerInRole").equals(expected[3])) {
      TestUtil.logErr("isCallerInRole operations test failed");
      pass = false;
    }

    if (!skip[4] && !p.getProperty("getEJBObject").equals(expected[4])) {
      TestUtil.logErr("getEJBObject operations test failed");
      pass = false;
    }

    if (!skip[5] && !p.getProperty("JNDI_Access").equals(expected[5])) {
      TestUtil.logErr("JNDI_Access operations test failed");
      pass = false;
    }

    if (!skip[6] && !p.getProperty("UserTransaction").equals(expected[6])) {
      TestUtil.logErr("UserTransaction operations test failed");
      pass = false;
    }

    if (!skip[7] && !p.getProperty("UserTransaction_Methods_Test1")
        .equals(expected[7])) {
      TestUtil.logErr("UserTransaction_Methods_Test1 operations test failed");
      pass = false;
    }

    if (!skip[8] && !p.getProperty("UserTransaction_Methods_Test2")
        .equals(expected[8])) {
      TestUtil.logErr("UserTransaction_Methods_Test2 operations test failed");
      pass = false;
    }

    if (!skip[9] && !p.getProperty("UserTransaction_Methods_Test3")
        .equals(expected[9])) {
      TestUtil.logErr("UserTransaction_Methods_Test3 operations test failed");
      pass = false;
    }

    if (!skip[10] && !p.getProperty("UserTransaction_Methods_Test4")
        .equals(expected[10])) {
      TestUtil.logErr("UserTransaction_Methods_Test4 operations test failed");
      pass = false;
    }

    if (!skip[11] && !p.getProperty("UserTransaction_Methods_Test5")
        .equals(expected[11])) {
      TestUtil.logErr("UserTransaction_Methods_Test5 operations test failed");
      pass = false;
    }

    if (!skip[12] && !p.getProperty("UserTransaction_Methods_Test6")
        .equals(expected[12])) {
      TestUtil.logErr("UserTransaction_Methods_Test6 operations test failed");
      pass = false;
    }

    if (!skip[13] && !p.getProperty("setRollbackOnly").equals(expected[13])) {
      TestUtil.logErr("setRollbackOnly operations test failed");
      pass = false;
    }

    if (!skip[14] && !p.getProperty("getEJBLocalHome").equals(expected[14])) {
      TestUtil.logErr("getEJBLocalHome operations test failed");
      pass = false;
    }

    if (!skip[15] && !p.getProperty("getEJBLocalObject").equals(expected[15])) {
      TestUtil.logErr("getEJBLocalObject operations test failed");
      pass = false;
    }

    if (!skip[16] && !p.getProperty("getPrimaryKey").equals(expected[16])) {
      TestUtil.logErr("getPrimaryKey operations test failed");
      pass = false;
    }

    if (!skip[17] && !p.getProperty("getTimerService").equals(expected[17])) {
      TestUtil.logErr("getTimerService operations test failed");
      pass = false;
    }

    if (!skip[18]
        && !p.getProperty("Timer_Service_Methods").equals(expected[18])) {
      TestUtil.logErr("Timer_Service_Methods operations test failed");
      pass = false;
    }

    if (pass) {
      TestUtil.logTrace("All operation tests passed as expected ...");
    } else {
      TestUtil.logErr("Not All operation tests passed - unexpected ...");
      TestUtil
          .logTrace("-----------------------------------------------------");
    }
    return pass;
  }

  /*
   * Methods implementing JMS messaging
   *
   */

  public static void sendMessage(Queue queue, QueueConnectionFactory qcFactory,
      String msg) {

    QueueConnection connection = null;

    try {
      TestUtil.logTrace("getting connection");
      connection = qcFactory.createQueueConnection();
      QueueSession session = connection.createQueueSession(true, 0);
      TestUtil.logTrace("creating sender");
      QueueSender sender = session.createSender(queue);
      connection.start();
      TextMessage message = session.createTextMessage();
      message.setText(msg);
      message.setStringProperty("testName", TestUtil.getProperty("testName"));
      TestUtil.logTrace(
          "Sending message at " + System.currentTimeMillis() + ": " + msg);
      sender.send(message);
    } catch (Exception e) {
      handleException("Error sending message", e);
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (Exception e) {
          handleException("Error closing queue connection", e);
        }
      }
    }
  }

  public static String getMessage(Queue queue, QueueConnectionFactory qcFactory,
      long timeout) {

    QueueConnection connection = null;

    try {
      TestUtil.logTrace("getting connection");
      connection = qcFactory.createQueueConnection();
      QueueSession session = connection.createQueueSession(true, 0);
      TestUtil.logTrace("creating receiver");
      QueueReceiver receiver = session.createReceiver(queue);
      connection.start();
      return getMessage(receiver, timeout);
    } catch (Exception e) {
      handleException("Error receiving message", e);
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (Exception e) {
          handleException("Error closing queue connection", e);
        }
      }
    }
    return NOMSGRECEIVED;
  }

  public static String getMessage(QueueReceiver receiver, long timeout) {

    TextMessage msg;
    String message;
    int retries = 0;

    try {

      do {
        Message m = receiver.receive(timeout);

        if (m == null) {
          TestUtil.logTrace("no message received");
          return TimerImpl.NOMSGRECEIVED;
        } else {
          if (m instanceof TextMessage) {
            msg = (TextMessage) m;
            message = msg.getText();
            TestUtil.logTrace("Receiving message at "
                + System.currentTimeMillis() + ": " + message);
            String mTestName = msg.getStringProperty("testName");
            String testName = TestUtil.getProperty("testName");
            if (testName.equals(mTestName)) {
              TestUtil.logTrace(
                  "test name property of message is correct: accepting");
              return message;
            }
            TestUtil
                .logErr("message from " + mTestName + " received - ignoring");
            ++retries;
          } else {
            TestUtil.logErr("non-text message received!");
            ++retries;
          }
        }
      } while (retries < 5);
      TestUtil
          .logTrace("No valid message received after " + retries + " attempts");

    } catch (Exception e) {
      TimerImpl.handleException("Error receiving message", e);
    }
    return TimerImpl.NOMSGRECEIVED;
  }

  public static void flushQueue(Queue queue, QueueConnectionFactory qcFactory) {

    QueueConnection connection = null;
    TextMessage smessage;
    Message rmessage;
    String smsg = "end of queue";
    String rmsg = null;
    int priority = 0; // lowest priority
    int numMsgs = 0;
    int numMsgsFlushed = 0;

    try {
      TestUtil.logTrace("Flushing queue: creating connection, session");
      connection = qcFactory.createQueueConnection();
      QueueSession session = connection.createQueueSession(false,
          Session.AUTO_ACKNOWLEDGE);
      connection.start();

      TestUtil.logTrace("Browsing queue for unsent messages");
      QueueBrowser qBrowser = session.createBrowser(queue);
      Enumeration msgs = qBrowser.getEnumeration();

      while (msgs.hasMoreElements()) {
        msgs.nextElement();
        numMsgs++;
      }

      qBrowser.close();
      if (numMsgs == 0) {
        TestUtil.logTrace("No Messages to flush - returning");
        return;
      }

      QueueSender sender = session.createSender(queue);
      QueueReceiver receiver = session.createReceiver(queue);

      TextMessage message = session.createTextMessage();
      message.setText(smsg);
      message.setStringProperty("testName", TestUtil.getProperty("testName"));
      sender.send(message, javax.jms.Message.DEFAULT_DELIVERY_MODE, priority,
          javax.jms.Message.DEFAULT_TIME_TO_LIVE);

      TestUtil.logTrace("message sent: " + message.getText());

      do {
        rmessage = receiver.receive();
        if (rmessage instanceof TextMessage) {
          rmsg = ((TextMessage) rmessage).getText();
          TestUtil.logTrace("Message received is " + rmsg);
        } else
          TestUtil.logTrace("Non-text message received");
        numMsgsFlushed++;
        TestUtil.logTrace("numMsgsFlushed is " + numMsgsFlushed);
      } while (!smsg.equals(rmsg));

      TestUtil.logTrace("flushed " + numMsgsFlushed + " messages");
    } catch (Exception e) {
      handleException("Error cleaning message queue", e);
    } finally {
      if (connection != null) {
        try {
          connection.close();
        } catch (Exception e) {
          handleException("Error closing queue connection", e);
        }
      }
    }
  }

  /*
   * Utility methods
   *
   */

  public static void handleException(String msg, Exception e) {
    TestUtil.logErr(msg + ": " + e.getMessage());
    TestUtil.printStackTrace(e);
  }

  // Retrieves the timeout value set in the test properties
  protected static long getTimeout() throws TimerImplException {

    long timeout = 0;
    try {
      timeout = Long.parseLong(TestUtil.getProperty("ejb_timeout"));
    } catch (Exception e) {
      handleException("getTimeout", e);
    }
    if (timeout <= 0)
      throw new TimerImplException("Invalid timeout value: " + timeout);
    return timeout;
  }

  // Gets a date for expiration of date timers
  protected static Date getExpirationDate() {
    return new Date(System.currentTimeMillis() + EXPIRATION_TIME);
  }
}
