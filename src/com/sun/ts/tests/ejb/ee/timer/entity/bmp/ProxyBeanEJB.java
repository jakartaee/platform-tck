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

package com.sun.ts.tests.ejb.ee.timer.entity.bmp;

import com.sun.ts.tests.ejb.ee.timer.common.*;
import com.sun.ts.tests.common.ejb.wrappers.StatefulWrapper;
import com.sun.ts.lib.util.*;
import javax.ejb.*;
import java.io.Serializable;

public class ProxyBeanEJB extends StatefulWrapper {

  private static final String UserNameProp = "user";

  private static final String UserPasswordProp = "password";

  private static final String AuthUser = "authuser";

  private String authuser = "";

  private String username = "";

  private String password = "";

  private static final String testLookup = "java:comp/env/ejb/TestBean";

  private TestBeanLocalHome beanHome;

  private TestBeanLocal beanRef;

  private TimerHandle handle;

  public ProxyBeanEJB() {
    TestUtil.logTrace("ProxyBeanEJB no arg constructor");
  }

  public boolean setup() {

    try {
      authuser = props.getProperty(AuthUser);
      username = props.getProperty(UserNameProp);
      password = props.getProperty(UserPasswordProp);

      TestUtil
          .logTrace("Getting the EJB Local Home interface for " + testLookup);
      beanHome = (TestBeanLocalHome) nctx.lookup(testLookup,
          TestBeanLocalHome.class);

      TestUtil.logTrace("Creating EJB TestBeanLocal instance");
      beanRef = (TestBeanLocal) beanHome.create(props, TimerImpl.TESTBEAN_KEY,
          "expresso", 10.5f);

      TestUtil.logTrace("Initializing logging for TestBean instance");
      return true;
    } catch (Exception e) {
      TimerImpl.handleException("Proxy bean setup", e);
    }
    return false;
  }

  public void cleanup() {

    try {
      if (beanRef == null)
        beanRef = (TestBeanLocal) beanHome.create(props, TimerImpl.TESTBEAN_KEY,
            "expresso", 10.5f);
      beanRef.cancelAllTimers();
    } catch (Exception e) {
      TimerImpl.handleException("Exception in cancelAllTimers", e);
    }
    if (beanRef != null)
      try {
        beanRef.remove();
      } catch (Exception e) {
        TimerImpl.handleException("Exception while removing test bean", e);
      }
  }

  public boolean initializeTimer(int timerType, int timeoutAction) {

    try {
      TestUtil.logTrace("Execute TestBeanLocal:initializeTimer");
      handle = beanRef.initializeTimerHandle(timerType, timeoutAction);
      return (handle != null);
    } catch (Exception e) {
      TimerImpl.handleException("initializeTimer", e);
    }
    return false;
  }

  public boolean initializeAndCancelTimer(int timerType) {

    try {
      handle = beanRef.initializeTimerHandle(timerType, TimerImpl.ACCESS);

      if (handle == null) {
        TestUtil
            .logTrace("Null handle returned from" + " initializeTimerHandle");
        return false;
      }

      if (!(beanRef.cancelTimer(handle))) {
        TestUtil.logTrace("Unable to cancel timer");
        return false;
      }

      return timerDoesNotExist();
    } catch (Exception e) {
      TimerImpl.handleException("initializeAndCancelTimer", e);
    }

    return false;
  }

  public boolean cancelAndRollback(int timerType) {

    try {
      TestUtil.logTrace("Execute TestBeanLocal:initializeTimer");
      handle = beanRef.initializeTimerHandle(TimerImpl.TIMER_SINGLEEVENT,
          TimerImpl.ACCESS);
      if (handle == null)
        return false;

      TestUtil.logTrace("Cancelling timer and rolling back cancellation...");
      beanRef.cancelAndRollback(handle);
      return timerExists();

    } catch (Exception e) {
      TimerImpl.handleException("cancelAndRollback", e);
    }
    return false;
  }

  public boolean removeBeanWithTimers(int timerType) {

    int NUM_TIMERS = 4;
    int info;
    Serializable sz;
    TimerHandle[] handles = new TimerHandle[NUM_TIMERS];

    try {
      TestUtil.logTrace("Creating a set of " + NUM_TIMERS + " timers.");
      for (int i = 0; i < NUM_TIMERS; ++i)
        handles[i] = beanRef.initializeTimerHandle(timerType, TimerImpl.NOMSG);

      TestUtil.logTrace("Making sure we've got the right timers");
      for (int i = 0; i < NUM_TIMERS; ++i) {

        if (handles[i] == null) {
          TestUtil.logErr("Null handle returned for timer " + i);
          return false;
        }

        sz = handles[i].getTimer().getInfo();

        if (!(sz instanceof Integer)) {
          TestUtil
              .logErr("Non-integer info value returned for " + "timer " + i);
          return false;
        }

        if ((info = ((Integer) sz).intValue()) != TimerImpl.NOMSG) {
          TestUtil.logErr("Wrong info value received for timer " + i + ":\n"
              + "  Expected:" + TimerImpl.NOMSG + "  Received:" + info);
          return false;
        }
      }

      TestUtil.logTrace("Removing the timer bean");
      beanRef.remove();

      TestUtil.logTrace("Verifying the timers are gone with the bean");
      for (int i = 0; i < NUM_TIMERS; ++i) {
        try {
          handles[i].getTimer();
        } catch (NoSuchObjectLocalException nsole) {
          TestUtil.logTrace("Timer " + i + ": NoSuchObjectLocalException"
              + " caught as expected");
          continue;
        }
        TestUtil.logErr("Timer " + i + ": NoSuchObjectLocalException"
            + " not caught as expected");
        return false;
      }
      return true;

    } catch (Exception e) {
      TimerImpl.handleException("removeBeanWithTimers: Unexpected Exception",
          e);
    }
    return false;
  }

  public boolean timerExists() {

    TestUtil.logTrace("Checking that timer still exists...");

    try {
      handle.getTimer();
      return (handle != null);
    } catch (Exception e) {
      TimerImpl.handleException("timerExists: Exception caught accessing timer",
          e);
    }
    return false;
  }

  public boolean timerDoesNotExist() {

    TestUtil.logTrace("Checking that timer no longer exists...");

    try {
      handle.getTimer();
    } catch (NoSuchObjectLocalException nsole) {
      TestUtil.logTrace("NoSuchObjectLocalException caught as expected");
      return true;
    } catch (Exception e) {
      TimerImpl.handleException(
          "NoSuchObjectLocalException not caught " + "as expected", e);
    }
    return false;
  }
}
