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

package com.sun.ts.tests.ejb.ee.timer.session.stateless.bm;

import com.sun.ts.tests.ejb.ee.timer.common.*;
import com.sun.ts.tests.common.ejb.wrappers.StatelessWrapper;
import com.sun.ts.lib.util.*;

import java.util.Properties;
import java.io.Serializable;
import javax.ejb.*;
import javax.transaction.*;
import javax.jms.QueueConnectionFactory;
import javax.jms.Queue;

public class TestBeanEJB extends StatelessWrapper implements TimedObject {

  private UserTransaction ut;

  // JNDI names
  private static final String queueName = "java:comp/env/jms/MyQueue";

  private static final String queueFactoryName = "java:comp/env/jms/MyQueueConnectionFactory";

  private static final String role = "Manager";

  // Expected results of allowed methods tests
  private static final String expected[] = { "true", // getEJBHome
      "true", // getCallerPrincipal
      "false", // getRollbackOnly
      "true", // isCallerInRole
      "true", // getEJBObject
      "true", // JNDI access
      "true", // getUserTransaction
      "true", // UserTransaction_Methods_Test1 -ut.begin()
      "true", // UserTransaction_Methods_Test2 -ut.commit()
      "true", // UserTransaction_Methods_Test3 -ut.getStatus()
      "true", // UserTransaction_Methods_Test4 -ut.rollback()
      "true", // UserTransaction_Methods_Test5 -ut.setRollbackOnly()
      "true", // UserTransaction_Methods_Test6 -ut.setTransactionTimeout()
      "false", // setRollbackOnly
      "true", // getEJBLocalHome
      "true", // getEJBLocalObject
      "false", // getPrimaryKey
      "true", // getTimerService
      "true" // Timer Service methods
  };

  // Allowed methods tests not tested
  private static final boolean skip[] = { false, // getEJBHome
      false, // getCallerPrincipal
      false, // getRollbackOnly
      false, // isCallerInRole
      false, // getEJBObject
      false, // JNDI access
      false, // getUserTransaction
      false, // UserTransaction_Methods_Test1 -ut.begin()
      false, // UserTransaction_Methods_Test2 -ut.commit()
      false, // UserTransaction_Methods_Test3 -ut.getStatus()
      true, // UserTransaction_Methods_Test4 -ut.rollback()
      true, // UserTransaction_Methods_Test5 -ut.setRollbackOnly()
      false, // UserTransaction_Methods_Test6 -ut.setTransactionTimeout()
      false, // setRollbackOnly
      false, // getEJBLocalHome
      false, // getEJBLocalObject
      true, // getPrimaryKey
      false, // getTimerService
      false // Timer Service methods
  };

  public TestBeanEJB() {
    TestUtil.logTrace("TestBeanEJB no arg constructor");
  }

  public void ejbTimeout(Timer timer) {

    Queue queue;
    QueueConnectionFactory qcFactory;
    Serializable sz;
    int timeoutAction;
    String msg = null;
    TestUtil.logTrace(
        "EJB_TIMEOUT: ejbTimeout called at " + System.currentTimeMillis());

    try {
      ut = sctx.getUserTransaction();
      ut.begin();
      sz = timer.getInfo();
      ut.commit();

      // No message to be sent in this case
      if (!(sz instanceof Integer)) {
        TestUtil.logTrace("EJB_TIMEOUT: No action required");
        return;
      }

      // Get the resources we need to send a message
      TestUtil.logTrace("EJB_TIMEOUT: finding jms resources...");
      queue = (Queue) nctx.lookup(queueName);
      qcFactory = (QueueConnectionFactory) nctx.lookup(queueFactoryName);

      timeoutAction = ((Integer) sz).intValue();
      TestUtil.logTrace("EJB_TIMEOUT: timeoutAction is " + timeoutAction
          + " at " + System.currentTimeMillis());

    } catch (Exception e) {
      TimerImpl.handleException("ejbTimeout initialization", e);
      return;
    }

    switch (timeoutAction) {

    case TimerImpl.NOMSG:
      TestUtil.logTrace("EJB_TIMEOUT: No message sent - return");
      return;

    case TimerImpl.ACCESS:
      msg = TimerImpl.ACCESS_OK;
      TestUtil.logTrace("EJB_TIMEOUT: Access OK, sending JMS message");
      break;

    case TimerImpl.CHKMETH:
      if (TimerImpl.accessCheckedMethod(nctx))
        msg = TimerImpl.CHKMETH_OK;
      else
        msg = TimerImpl.CHKMETH_FAIL;
      TestUtil.logTrace("EJB_TIMEOUT: Sending results of attempt "
          + "to access checked method...");
      break;

    case TimerImpl.SERIALIZE:

      TestUtil.logTrace("EJB_TIMEOUT: Getting timer handle...");
      try {
        ut.begin();
        TimerHandle handle = TimerImpl.getTimerHandleFromEjbTimeout(
            sctx.getTimerService(), TimerImpl.SERIALIZE);

        if (handle == null) {
          ut.commit();
          TestUtil.logErr("EJB_TIMEOUT: Null handle received from " +

              "getTimerHandleFromEjbTimeout()");
          msg = TimerImpl.SERIALIZE_FAIL;
          break;
        }

        TestUtil
            .logTrace("EJB_TIMEOUT: Verifying handle is " + "serializable...");
        if (!(TimerImpl.isSerializable(handle))) {
          ut.commit();
          TestUtil.logErr("EJB_TIMEOUT: Timer handle is not serializable");
          msg = TimerImpl.SERIALIZE_FAIL;
          break;
        }

        TestUtil.logTrace("EJB_TIMEOUT: Getting deserialized handle...");
        TimerHandle deserializedHandle = TimerImpl
            .getDeserializedHandle(handle);

        TestUtil
            .logTrace("EJB_TIMEOUT: Verifying timers are " + "identical...");
        if (!(TimerImpl.timersAreIdentical(handle, deserializedHandle))) {
          ut.commit();
          TestUtil.logErr("EJB_TIMEOUT: Timers are not identical");
          msg = TimerImpl.SERIALIZE_FAIL;
          break;
        }
        ut.commit();
        msg = TimerImpl.SERIALIZE_OK;
      } catch (Exception e) {
        TimerImpl.handleException("ejb_timeout serialization", e);
      }
      break;

    case TimerImpl.ALLOWED:
      Properties props = TimerImpl.doOperationTests(sctx, nctx, timer, skip,
          role, TimerImpl.SESSION);
      boolean results = TimerImpl.checkOperationsTestResults(props, expected,
          skip);
      msg = (results) ? TimerImpl.ALLOW_OK : TimerImpl.ALLOW_FAIL;
      break;

    default:
      msg = TimerImpl.INVALID_ACTION;
      break;
    }

    TestUtil.logTrace(
        "EJB_TIMEOUT: Sending message at " + System.currentTimeMillis());
    try {
      ut.begin();
      TimerImpl.sendMessage(queue, qcFactory, msg);
      ut.commit();
    } catch (Exception e) {
      TimerImpl.handleException("ejb_timeout message sending", e);
    }
  }

  /**
   *
   * Business Methods
   *
   */

  public boolean getInfoStrAndCancel(int timerType) {

    String infoStr = TimerImpl.INFOSTRING;
    String returnStr;
    TimerHandle handle;

    try {
      TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("Initializing timer at " + System.currentTimeMillis());
      ut = sctx.getUserTransaction();
      ut.begin();
      handle = TimerImpl.createTimerHandle(timerType, infoStr, ts);
      returnStr = (String) TimerImpl.getInfo(handle);
      ut.commit();

      if (!(returnStr.equals(infoStr))) {
        TestUtil.logErr("getInfo failed: input = " + infoStr
            + ", return value = " + returnStr);
        return false;
      }
      TestUtil.logTrace("Timer info is " + infoStr);

      ut.begin();
      TimerImpl.cancelTimer(handle);
      ut.commit();

      TestUtil.logTrace("Timer cancelled.");
      return true;

    } catch (Exception e) {
      TimerImpl.handleException("getInfoStrAndCancel", e);
    }
    return false;
  }

  public boolean getInfoClassAndCancel(int timerType) {

    TimerInfo infoClass, returnClass;
    TimerHandle handle;

    try {
      TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("Initializing timer at " + System.currentTimeMillis());
      infoClass = new TimerInfo("string", 1, true, 3.1415926);

      ut = sctx.getUserTransaction();
      ut.begin();
      handle = TimerImpl.createTimerHandle(timerType, infoClass, ts);
      returnClass = (TimerInfo) TimerImpl.getInfo(handle);
      ut.commit();

      if (!(returnClass.equals(infoClass))) {
        TestUtil.logErr("getInfo failed: input = " + infoClass.toString()
            + ", return value = " + returnClass.toString());
        return false;
      }
      TestUtil.logTrace("Timer info is " + infoClass.toString());

      ut.begin();
      TimerImpl.cancelTimer(handle);
      ut.commit();

      TestUtil.logTrace("Timer cancelled.");
      return true;

    } catch (Exception e) {
      TimerImpl.handleException("getInfoClassAndCancel", e);
    }
    return false;
  }

  public boolean initializeTimer(int timerType, int timerAction) {

    TimerHandle handle;

    try {
      TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("Initializing timer at " + System.currentTimeMillis());
      ut = sctx.getUserTransaction();
      ut.begin();
      handle = TimerImpl.createTimerHandle(timerType, new Integer(timerAction),
          ts);
      ut.commit();

      return (handle == null) ? false : true;

    } catch (Exception e) {
      TimerImpl.handleException("initializeTimer", e);
    }
    return false;
  }

  public TimerHandle initializeTimerHandle(int timerType, int timerAction) {

    TimerHandle handle = null;

    try {
      TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("Initializing timer at " + System.currentTimeMillis());

      ut = sctx.getUserTransaction();
      ut.begin();
      handle = TimerImpl.createTimerHandle(timerType, new Integer(timerAction),
          ts);
      ut.commit();

    } catch (Exception e) {
      TimerImpl.handleException("initializeTimer", e);
    }
    return handle;
  }

  public boolean createAndRollback(int timerType) {

    Timer timer;

    try {
      TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("Initializing timer at " + System.currentTimeMillis());

      ut = sctx.getUserTransaction();
      ut.begin();
      timer = TimerImpl.createTimer(timerType, new Integer(TimerImpl.NOMSG),
          ts);

      TestUtil
          .logTrace("Rolling back transaction in which timer was created...");
      ut.rollback();
      return true;

    } catch (Exception e) {
      TimerImpl.handleException("createAndRollback", e);
    }
    return false;
  }

  public boolean cancelAndRollback(TimerHandle handle) {

    Timer timer;

    try {
      TestUtil.logTrace("Getting timer...");

      ut = sctx.getUserTransaction();
      ut.begin();
      timer = handle.getTimer();
      TestUtil.logTrace("Cancelling timer at " + System.currentTimeMillis());
      timer.cancel();

      TestUtil
          .logTrace("Rolling back transaction in which timer was cancelled...");
      ut.rollback();
      return true;

    } catch (Exception e) {
      TimerImpl.handleException("cancelAndRollback", e);
    }
    return false;
  }

  public boolean cancelTimer(TimerHandle handle) {

    try {
      ut = sctx.getUserTransaction();
      ut.begin();
      TimerImpl.cancelTimer(handle);
      ut.commit();
      return true;

    } catch (Exception e) {
      TimerImpl.handleException("cancelTimer", e);
    }
    return false;
  }

  public boolean isSerializable(int timerType) {

    TimerHandle handle;

    try {
      TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("Initializing timer at " + System.currentTimeMillis());

      ut = sctx.getUserTransaction();
      ut.begin();
      handle = TimerImpl.createTimerHandle(timerType,
          new Integer(TimerImpl.NOMSG), ts);
      ut.commit();
      return TimerImpl.isSerializable(handle);

    } catch (Exception e) {
      TimerImpl.handleException("isSerializable", e);
    }
    return false;
  }

  public boolean verifyTimerIsGone() {

    try {
      TimerService ts = sctx.getTimerService();
      ut = sctx.getUserTransaction();
      ut.begin();
      boolean result = TimerImpl.verifyNoTimers(ts);
      ut.commit();
      return result;

    } catch (Exception e) {
      TimerImpl.handleException("verifyTimerIsGone", e);
    }
    return false;
  }

  public void cancelAllTimers() {

    try {
      TimerService ts = sctx.getTimerService();
      TestUtil.logTrace("Cancelling all timers...");

      ut = sctx.getUserTransaction();
      ut.begin();
      TimerImpl.cancelAllTimers(ts);
      ut.commit();

    } catch (Exception e) {
      TimerImpl.handleException("cancelAllTimers", e);
    }
  }
}
