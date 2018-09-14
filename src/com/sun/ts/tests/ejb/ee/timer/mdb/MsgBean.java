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

package com.sun.ts.tests.ejb.ee.timer.mdb;

import com.sun.ts.tests.ejb.ee.timer.helper.FlagStoreHome;
import com.sun.ts.tests.ejb.ee.timer.helper.FlagStore;
import com.sun.ts.tests.ejb.ee.timer.common.*;
import com.sun.ts.lib.util.*;

import java.lang.reflect.Method;
import java.util.Properties;
import java.util.Enumeration;
import java.util.ArrayList;
import java.io.Serializable;
import javax.ejb.*;
import javax.jms.*;

public class MsgBean
    implements MessageDrivenBean, MessageListener, TimedObject {

  // JNDI names
  private static final String queueName = "java:comp/env/jms/ReplyQueue";

  private static final String queueFactoryName = "java:comp/env/jms/MyQueueConnectionFactory";

  private Queue replyQueue;

  private QueueConnectionFactory qcFactory;

  public TSNamingContext nctx;

  protected MessageDrivenContext mctx;

  private FlagStoreHome flagStoreHome;

  private FlagStore flagStoreRef;

  public void ejbCreate() {
    try {
      TestUtil.logTrace("timer/mdb - ejbCreate()");
      nctx = new TSNamingContext();
      TestUtil.logTrace("Looking up " + queueFactoryName);
      qcFactory = (QueueConnectionFactory) nctx.lookup(queueFactoryName);
      TestUtil.logTrace("Looking up " + queueName);
      replyQueue = (Queue) nctx.lookup(queueName);
    } catch (Exception e) {
      TimerImpl.handleException("Exception in ejbCreate()", e);
    }
  }

  public void setMessageDrivenContext(MessageDrivenContext mctx) {
    TestUtil.logTrace("timer/mdb - setMessageDrivenContext()");
    this.mctx = mctx;
  }

  public void ejbRemove() {
    TestUtil.logTrace("timer/mdb - ejbRemove()");
  }

  public void onMessage(Message msg) {
    String methodToRun = "Undefined";
    int timerType;
    Properties props = null;
    String propsList = "";

    try {
      props = getProperties(msg);
      TestUtil.init(props);
    } catch (JMSException je) {
      System.out.println("JMSException caught getting Properties");
    } catch (RemoteLoggingInitException e) {
      System.out
          .println("RemoteLoggingInitException caught initializing Props");
    }

    try {
      methodToRun = ((TextMessage) msg).getText();
      TestUtil.logTrace("Will run method: " + methodToRun + " at time "
          + System.currentTimeMillis());

      runMethod(methodToRun);
    } catch (Exception e) {
      TimerImpl.handleException("Exception in onMessage()", e);
      TimerImpl.sendMessage(replyQueue, qcFactory,
          "Exception in onMessage():" + e.toString());
    }
  }

  /**
   * Run corresponding test by invoking test method on the current instance.
   */
  private void runMethod(String methodToRunName) throws Exception {

    Class testDriverClass;
    Method methodToRun;
    Class params[] = null;
    Class params1[] = { Integer.class };
    Object args[] = null;

    int timerType = Integer.parseInt(TestUtil.getProperty("timer_type"));
    if (timerType != 0) {
      args = new Object[1];
      args[0] = new Integer(timerType);
      params = params1;
    }
    TestUtil.logTrace("run method '" + methodToRunName + "'");
    testDriverClass = this.getClass();
    methodToRun = testDriverClass.getMethod(methodToRunName, params);
    methodToRun.invoke(this, args);
  }

  public void ejbTimeout(Timer timer) {

    Serializable sz;
    int timeoutAction;
    String message;
    TestUtil.logTrace(
        "EJB_TIMEOUT: ejbTimeout called at " + System.currentTimeMillis());

    try {
      sz = timer.getInfo();

      // No message to be sent in this case
      if (!(sz instanceof Integer)) {
        TestUtil.logTrace("EJB_TIMEOUT: No action required");
        return;
      }

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
      message = TimerImpl.ACCESS_OK;
      TestUtil.logTrace("EJB_TIMEOUT: Access OK, sending JMS message");
      break;

    case TimerImpl.CHKMETH:
      if (TimerImpl.accessCheckedMethod(nctx))
        message = TimerImpl.CHKMETH_OK;
      else
        message = TimerImpl.CHKMETH_FAIL;
      TestUtil.logTrace("EJB_TIMEOUT: Sending results of attempt "
          + "to access checked method...");
      break;

    case TimerImpl.RETRY:
      try {
        TestUtil.logTrace("EJB_TIMEOUT: finding flag store bean");
        flagStoreHome = (FlagStoreHome) nctx.lookup(TimerImpl.FLAGSTORE_BEAN,
            FlagStoreHome.class);
        flagStoreRef = (FlagStore) flagStoreHome
            .findByPrimaryKey(new Integer(TimerImpl.FLAGSTORE_KEY));

        TestUtil.logTrace("EJB_TIMEOUT: checking flag in flag store bean");
        if ((flagStoreRef.getRequiresNewAccessFlag()) == false) {
          TestUtil.logTrace("EJB_TIMEOUT: flag is false - set it and rollback");
          flagStoreRef.setRequiresNewAccessFlag(true);
          mctx.setRollbackOnly();
          return;
        }

        TestUtil.logTrace("EJB_TIMEOUT: flag is true - send a message");
        TestUtil.logTrace("removing flag store bean");
        flagStoreRef.remove();
        message = TimerImpl.RETRY_OK;
      } catch (Exception e) {
        TimerImpl.handleException("ejbTimeout retry", e);
        TestUtil.logTrace("removing flag store bean");
        if (flagStoreRef != null)
          try {
            flagStoreRef.remove();
          } catch (Exception re) {
            TimerImpl.handleException("exception removing FlagStore bean", e);
          }
        message = TimerImpl.RETRY_FAIL;
      }
      break;

    case TimerImpl.ROLLBACK:
      try {
        TestUtil.logTrace("EJB_TIMEOUT: finding flag store bean");
        flagStoreHome = (FlagStoreHome) nctx.lookup(TimerImpl.FLAGSTORE_BEAN,
            FlagStoreHome.class);
        flagStoreRef = (FlagStore) flagStoreHome
            .findByPrimaryKey(new Integer(TimerImpl.FLAGSTORE_KEY));

        TestUtil.logTrace("EJB_TIMEOUT: checking flags in flag store bean");
        if ((flagStoreRef.getRequiresNewAccessFlag()) == false) {
          if ((flagStoreRef.getRequiredAccessFlag()) == true) {
            TestUtil.logErr("EJB_TIMEOUT: Unexpected value of Required "
                + "flag: true when RequiresNew flag is false");
            message = TimerImpl.ROLLBACK_FAIL;
            break;
          }
          TestUtil.logTrace(
              "EJB_TIMEOUT: both flags unset, " + "set them and rollback");
          flagStoreRef.setRequiresNewAccessFlag(true);
          flagStoreRef.setRequiredAccessFlag(true);
          mctx.setRollbackOnly();
          return;
        }

        TestUtil.logTrace("EJB_TIMEOUT: RequiresNew flag is true - "
            + "checking the Required flag");
        if ((flagStoreRef.getRequiredAccessFlag()) == true) {
          TestUtil.logErr("EJB_TIMEOUT: Unexpected value of Required "
              + "flag: true when RequiresNew flag is true");
          message = TimerImpl.ROLLBACK_FAIL;
          break;
        }

        TestUtil.logTrace("removing flag store bean");
        flagStoreRef.remove();
        message = TimerImpl.ROLLBACK_OK;
      } catch (Exception e) {
        TimerImpl.handleException("ejbTimeout rollback", e);
        TestUtil.logTrace("removing flag store bean");
        if (flagStoreRef != null)
          try {
            flagStoreRef.remove();
          } catch (Exception re) {
            TimerImpl.handleException("exception removing FlagStore bean", e);
          }
        message = TimerImpl.ROLLBACK_FAIL;
      }
      break;

    case TimerImpl.SERIALIZE:

      TestUtil.logTrace("EJB_TIMEOUT: Getting timer handle...");
      TimerHandle handle = TimerImpl.getTimerHandleFromEjbTimeout(
          mctx.getTimerService(), TimerImpl.SERIALIZE);

      if (handle == null) {
        TestUtil.logErr("EJB_TIMEOUT: Null handle received from " +

            "getTimerHandleFromEjbTimeout()");
        message = TimerImpl.SERIALIZE_FAIL;
        break;
      }

      TestUtil
          .logTrace("EJB_TIMEOUT: Verifying handle is " + "serializable...");
      if (!(TimerImpl.isSerializable(handle))) {

        TestUtil.logErr("EJB_TIMEOUT: Timer handle is not serializable");
        message = TimerImpl.SERIALIZE_FAIL;
        break;
      }

      TestUtil.logTrace("EJB_TIMEOUT: Getting deserialized handle...");
      TimerHandle deserializedHandle = TimerImpl.getDeserializedHandle(handle);

      TestUtil.logTrace("EJB_TIMEOUT: Verifying timers are " + "identical...");
      if (!(TimerImpl.timersAreIdentical(handle, deserializedHandle))) {
        TestUtil.logErr("EJB_TIMEOUT: Timers are not identical");
        message = TimerImpl.SERIALIZE_FAIL;
        break;
      }
      message = TimerImpl.SERIALIZE_OK;
      break;

    default:
      message = TimerImpl.INVALID_ACTION;
      break;
    }

    TestUtil.logTrace(
        "EJB_TIMEOUT: Sending message at " + System.currentTimeMillis());
    TimerImpl.sendMessage(replyQueue, qcFactory, message);
  }

  /**
   *
   * Test Methods
   *
   */

  public void getInfoStrAndCancel(Integer timerType) {

    String infoStr = TimerImpl.INFOSTRING;
    String returnStr;
    String message = null;
    TimerHandle handle;
    int timer_type;
    boolean pass = false;

    try {
      timer_type = timerType.intValue();
      TimerService ts = mctx.getTimerService();
      TestUtil.logTrace("Initializing timer at " + System.currentTimeMillis());
      handle = TimerImpl.createTimerHandle(timer_type, infoStr, ts);
      returnStr = (String) TimerImpl.getInfo(handle);

      if (returnStr.equals(infoStr))
        pass = true;
      else {
        message = "getInfo failed: input = " + infoStr + ", return value = "
            + returnStr;
        TestUtil.logErr(message);
      }

      if (pass) {
        TimerImpl.cancelTimer(handle);
        TestUtil.logTrace("Timer cancelled.");
        message = TestUtil.getProperty("testName");
      }
    } catch (Exception e) {
      TimerImpl.handleException("getInfoStrAndCancel", e);
      message = e.toString();
      TimerImpl.sendMessage(replyQueue, qcFactory, message);
    }
    TimerImpl.sendMessage(replyQueue, qcFactory, message);
  }

  public void getInfoClassAndCancel(Integer timerType) {

    TimerInfo infoClass, returnClass;
    String message = null;
    TimerHandle handle;
    int timer_type;
    boolean pass = false;

    try {
      timer_type = timerType.intValue();
      TimerService ts = mctx.getTimerService();
      TestUtil.logTrace("Initializing timer at " + System.currentTimeMillis());

      infoClass = new TimerInfo("string", 1, true, 3.1415926);
      handle = TimerImpl.createTimerHandle(timer_type, infoClass, ts);
      returnClass = (TimerInfo) TimerImpl.getInfo(handle);

      if (returnClass.equals(infoClass))
        pass = true;
      else {
        message = "getInfo failed: input = " + infoClass + ", return value = "
            + returnClass;
        TestUtil.logErr(message);
      }

      if (pass) {
        TimerImpl.cancelTimer(handle);
        TestUtil.logTrace("Timer cancelled.");
        message = TestUtil.getProperty("testName");
      }
    } catch (Exception e) {
      TimerImpl.handleException("getInfoClassAndCancel", e);
      message = e.toString();
      TimerImpl.sendMessage(replyQueue, qcFactory, message);
    }
    TimerImpl.sendMessage(replyQueue, qcFactory, message);
  }

  public void checkedMethodAccess(Integer timerType) {

    String message = null;
    int timer_type;
    boolean initialized = true;

    try {
      timer_type = timerType.intValue();
      TimerService ts = mctx.getTimerService();
      initialized = initializeTimer(timer_type, TimerImpl.CHKMETH);
    } catch (Exception e) {
      initialized = false;
      TimerImpl.handleException("checkedMethodAccess", e);
    }

    if (!initialized) {
      message = "timer initialization failure";
      TimerImpl.sendMessage(replyQueue, qcFactory, message);
    }
  }

  public void verifyTimeoutCall(Integer timerType) {

    String message = null;
    int timer_type;
    boolean initialized = true;

    try {
      timer_type = timerType.intValue();
      TimerService ts = mctx.getTimerService();
      initialized = initializeTimer(timer_type, TimerImpl.ACCESS);
    } catch (Exception e) {
      initialized = false;
      TimerImpl.handleException("verifyTimeoutCall", e);
    }

    if (!initialized) {
      message = "timer initialization failure";
      TimerImpl.sendMessage(replyQueue, qcFactory, message);
    }
  }

  public void createAndRollback(Integer timerType) {

    String message = null;
    int timer_type;
    Timer timer;

    // check and see if this method has been called before
    try {
      TestUtil.logTrace("finding flag store bean");
      flagStoreHome = (FlagStoreHome) nctx.lookup(TimerImpl.FLAGSTORE_BEAN,
          FlagStoreHome.class);
      flagStoreRef = (FlagStore) flagStoreHome
          .findByPrimaryKey(new Integer(TimerImpl.FLAGSTORE_KEY));

      TestUtil.logTrace("checking flag in flag store bean");
      if ((flagStoreRef.getRequiresNewAccessFlag()) == false) {

        // first time we've been called
        TestUtil.logTrace(
            "flag is false - initialize timer, set flag and rollback");
        timer_type = timerType.intValue();
        TimerService ts = mctx.getTimerService();
        TestUtil
            .logTrace("Initializing timer at " + System.currentTimeMillis());
        timer = TimerImpl.createTimer(timer_type, new Integer(TimerImpl.ACCESS),
            ts);

        flagStoreRef.setRequiresNewAccessFlag(true);

        TestUtil.logTrace("calling setRollbackOnly()");
        mctx.setRollbackOnly();
        return;
      }

      // second time we've been called - rollback must have worked
      TestUtil.logTrace("flag is true - send a message");
      flagStoreRef.remove();
      message = "Rollback occurred as expected";
    } catch (Exception e) {
      message = "exception in createAndRollback " + e.toString();
      if (flagStoreRef != null)
        try {
          flagStoreRef.remove();
        } catch (Exception re) {
          TimerImpl.handleException("exception removing FlagStore bean", e);
        }
      TimerImpl.handleException("createAndRollback", e);
    }
    TimerImpl.sendMessage(replyQueue, qcFactory, message);
  }

  public void ejbTimeoutRetry(Integer timerType) {

    String message = null;
    int timer_type;
    boolean initialized = true;

    try {
      timer_type = timerType.intValue();
      TimerService ts = mctx.getTimerService();
      initialized = createFlagStoreAndTimer(timer_type, TimerImpl.RETRY);
    } catch (Exception e) {
      initialized = false;
      TimerImpl.handleException("ejbTimeoutRetry", e);
    }

    if (!initialized) {
      message = "FlagStore bean or timer initialization failure";
      TimerImpl.sendMessage(replyQueue, qcFactory, message);
    }
  }

  public void rollbackTxInEjbTimeout(Integer timerType) {

    String message = null;
    int timer_type;
    boolean initialized = true;

    try {
      timer_type = timerType.intValue();
      TimerService ts = mctx.getTimerService();
      initialized = createFlagStoreAndTimer(timer_type, TimerImpl.ROLLBACK);
    } catch (Exception e) {
      initialized = false;
      TimerImpl.handleException("rollbackTxInEjbTimeout", e);
    }

    if (!initialized) {
      message = "FlagStore bean or timer initialization failure";
      TimerImpl.sendMessage(replyQueue, qcFactory, message);
    }
  }

  public void checkSerialization(Integer timerType) {

    String message = null;
    TimerHandle handle;
    int timer_type;

    try {
      timer_type = timerType.intValue();
      TimerService ts = mctx.getTimerService();
      TestUtil.logTrace("Initializing timer at " + System.currentTimeMillis());
      handle = TimerImpl.createTimerHandle(timer_type,
          new Integer(TimerImpl.NOMSG), ts);
      message = (TimerImpl.isSerializable(handle))
          ? TestUtil.getProperty("testName")
          : "checkSerialization failed";
    } catch (Exception e) {
      message = "exception in checkSerialization " + e.toString();
      TimerImpl.handleException("checkSerialization", e);
    }
    TimerImpl.sendMessage(replyQueue, qcFactory, message);
  }

  public void checkSerializationInEjbTimeout(Integer timerType) {

    String message = null;
    int timer_type;
    boolean initialized = true;

    try {
      timer_type = timerType.intValue();
      TimerService ts = mctx.getTimerService();
      initialized = initializeTimer(timer_type, TimerImpl.SERIALIZE);
    } catch (Exception e) {
      initialized = false;
      TimerImpl.handleException("checkSerializationInEjbTimeout", e);
    }

    if (!initialized) {
      message = "timer initialization failure";
      TimerImpl.sendMessage(replyQueue, qcFactory, message);
    }
  }

  public void initializeTimerAndNotify(Integer timerType) {

    String message = null;
    int timer_type;
    boolean initialized = true;

    try {
      timer_type = timerType.intValue();
      TimerService ts = mctx.getTimerService();
      initialized = initializeTimer(timer_type, TimerImpl.ACCESS);
    } catch (Exception e) {
      initialized = false;
      TimerImpl.handleException("initializeTimerAndNotify", e);
    }

    message = (initialized) ? TestUtil.getProperty("testName")
        : "timer initialization failure";
    TimerImpl.sendMessage(replyQueue, qcFactory, message);
  }

  public void initializeTimerAndFlagStoreBean(Integer timerType) {

    String message = null;
    int timer_type;
    boolean initialized = true;

    try {
      timer_type = timerType.intValue();
      TimerService ts = mctx.getTimerService();
      initialized = createFlagStoreAndTimer(timer_type, TimerImpl.ACCESS);
    } catch (Exception e) {
      initialized = false;
      TimerImpl.handleException("initializeTimerAndFlagStoreBean", e);
    }

    message = (initialized) ? TestUtil.getProperty("testName")
        : "timer or FlagStore bean initialization failure";
    TimerImpl.sendMessage(replyQueue, qcFactory, message);
  }

  public void cancelAndRollback() {

    Timer timer;
    String message = null;

    // check and see if this method has been called before
    try {
      TestUtil.logTrace("finding flag store bean");
      flagStoreHome = (FlagStoreHome) nctx.lookup(TimerImpl.FLAGSTORE_BEAN,
          FlagStoreHome.class);
      flagStoreRef = (FlagStore) flagStoreHome
          .findByPrimaryKey(new Integer(TimerImpl.FLAGSTORE_KEY));

      TestUtil.logTrace("checking flag in flag store bean");
      if ((flagStoreRef.getRequiresNewAccessFlag()) == false) {

        // first time we've been called
        TestUtil
            .logTrace("flag is false - cancel timer, set flag and rollback");
        timer = findTimer(TimerImpl.ACCESS);
        if (timer == null)
          message = "No timer found to cancel";
        else {
          TestUtil
              .logTrace("Cancelling timer at " + System.currentTimeMillis());
          timer.cancel();
          flagStoreRef.setRequiresNewAccessFlag(true);
          TestUtil.logTrace(
              "Rolling back transaction " + "in which timer was cancelled...");
          mctx.setRollbackOnly();
          return;
        }
      }
      // if flag has been set, we need only remove the FlagStore bean
      else {
        TestUtil.logTrace("flag is true - remove bean and return");
        if (flagStoreRef != null)
          try {
            flagStoreRef.remove();
            return;
          } catch (Exception re) {
            TimerImpl.handleException("exception removing FlagStore bean", re);
            message = "Exception removing FlagStore bean";
          }
      }
    } catch (Exception e) {
      TimerImpl.handleException("cancelAndRollback", e);
      message = "cancelAndRollback failure";
    }

    TimerImpl.sendMessage(replyQueue, qcFactory, message);
  }

  public void verifyTimerIsGone() {

    boolean noTimers;
    String message;

    try {
      TimerService ts = mctx.getTimerService();
      noTimers = TimerImpl.verifyNoTimers(ts);
      message = (noTimers) ? TimerImpl.NOTIMER_FOUND : TimerImpl.TIMER_EXISTS;
    } catch (Exception e) {
      TimerImpl.handleException("verifyTimerIsGone", e);
      message = "failure in verifyTimerIsGone";
    }
    TimerImpl.sendMessage(replyQueue, qcFactory, message);
  }

  public void initializeFlagStoreBean() {

    String message = "FlagStore bean not yet created";
    if (createFlagStoreBean() == true)
      message = "FlagStore bean successfully created";
    else
      message = "Failed to create FlagStore bean";
    TimerImpl.sendMessage(replyQueue, qcFactory, message);
  }

  /**
   *
   * Utility Methods
   *
   */

  public void cancelAllTimers() {

    try {
      TimerService ts = mctx.getTimerService();
      TestUtil.logTrace("Cancelling all timers...");
      TimerImpl.cancelAllTimers(ts);
    } catch (Exception e) {
      TimerImpl.handleException("cancelAllTimers", e);
    }
  }

  protected boolean initializeTimer(int timerType, int timerAction) {

    TimerHandle handle;

    try {
      TimerService ts = mctx.getTimerService();
      TestUtil.logTrace("Initializing timer at " + System.currentTimeMillis());
      handle = TimerImpl.createTimerHandle(timerType, new Integer(timerAction),
          ts);
      return (handle == null) ? false : true;
    } catch (Exception e) {
      TimerImpl.handleException("initializeTimer", e);
    }
    return false;
  }

  protected boolean createFlagStoreAndTimer(int timerType, int timerAction) {

    try {
      if (createFlagStoreBean() == true)
        return initializeTimer(timerType, timerAction);
    } catch (Exception e) {
      TimerImpl.handleException("createFlagStoreAndTimer", e);
    }
    return false;
  }

  protected boolean createFlagStoreBean() {

    try {
      TestUtil.logTrace("creating flag store bean");
      flagStoreHome = (FlagStoreHome) nctx.lookup(TimerImpl.FLAGSTORE_BEAN,
          FlagStoreHome.class);
      flagStoreRef = (FlagStore) flagStoreHome.create(TestUtil.getProperties(),
          TimerImpl.FLAGSTORE_KEY, "expresso", 10.5f, false, false);
      return true;
    } catch (Exception e) {
      TimerImpl.handleException("createFlagStoreBean", e);
      try {
        TestUtil.logTrace("removing flag store bean");
        if (flagStoreRef != null)
          flagStoreRef.remove();
      } catch (Exception e1) {
        TimerImpl.handleException("removal of FlagStore bean failed", e);
      }
    }
    return false;
  }

  protected Timer findTimer(int id) {

    try {
      TimerService ts = mctx.getTimerService();
      ArrayList al = new ArrayList(ts.getTimers());
      if (!al.isEmpty()) {

        int numTimers = al.size();
        TestUtil.logTrace("Number of timers is " + numTimers);
        for (int i = 0; i < numTimers; ++i) {
          Timer timer = (Timer) al.get(i);
          Serializable sz = timer.getInfo();
          if (sz instanceof Integer) {
            int info = ((Integer) sz).intValue();
            TestUtil.logTrace("Timer info is " + info);
            if (id == info) {
              TestUtil.logTrace("match found!");
              return timer;
            }
          }
        }
      }
    } catch (Exception e) {
      TimerImpl.handleException("Exception in findTimer", e);
    }
    return null;
  }

  /**
   * Construct a property object needed by TS harness for logging. We retrieve
   * the properties from the Message object passed into the MDB onMessage()
   * method
   */
  protected Properties getProperties(Message msg) throws JMSException {
    Properties props;
    String hostname = null;
    String traceflag = null;
    String logport = null;
    Enumeration propNames;

    props = new Properties();

    /*
     * Because a JMS property name cannot contain '.' the following properties
     * are a special case
     */

    hostname = msg.getStringProperty("harnesshost");
    props.put("harness.host", hostname);
    traceflag = msg.getStringProperty("harnesslogtraceflag");
    props.put("harness.log.traceflag", traceflag);
    logport = msg.getStringProperty("harnesslogport");
    props.put("harness.log.port", logport);

    /*
     * now pull out the rest of the properties from the message
     */
    propNames = msg.getPropertyNames();
    for (String name = null; propNames.hasMoreElements();) {
      name = (String) propNames.nextElement();
      props.put(name, msg.getStringProperty(name));
    }
    return props;
  }
}
