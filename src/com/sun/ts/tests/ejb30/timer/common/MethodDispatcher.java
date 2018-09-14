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

package com.sun.ts.tests.ejb30.timer.common;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.messaging.StatusReporter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.ejb.Timer;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.QueueConnectionFactory;

/**
 * A class that reflectively dispatches to the method corresponding to the test
 * method, and sends the reply message.
 */
abstract public class MethodDispatcher {
  public static final String TIMEOUT_PREFIX = "timeOut_";

  public static final String ONMESSAGE_PREFIX = "onMessage_";

  protected MethodDispatcher() {
    super();
  }

  public static void dispatchTimeOut(Timer timer, Object target,
      QueueConnectionFactory qfactory, Queue queue) {
    // test method example: public void test1(Timer timer, TimerInfo ti)
    TimerInfo ti = (TimerInfo) timer.getInfo();
    final String testname = ti.getTestName();
    if (testname == null) {
      throw new IllegalStateException("testname is null in timer info: " + ti);
    }
    String methodName = TIMEOUT_PREFIX + testname;
    Method method = null;
    Throwable exception = null;
    boolean testResult = false;
    String reason = null;
    try {
      method = target.getClass().getMethod(methodName, Timer.class,
          TimerInfo.class);
      method.invoke(target, timer, ti);
    } catch (SecurityException ex) {
      exception = ex;
    } catch (NoSuchMethodException ex) {
      exception = ex;
    } catch (IllegalAccessException ex) {
      exception = ex;
    } catch (InvocationTargetException ex) {
      exception = ex.getTargetException();
    }
    if (exception == null) {
      testResult = true;
      reason = "TimeOut in " + target;
    } else {
      testResult = false;
      reason = TestUtil.printStackTraceToString(exception);
    }
    StatusReporter.report(testname, testResult, reason, qfactory, queue);
  }

  public static void dispatchOnMessage(Message msg, Object target,
      final String testname) {
    if (testname == null) {
      throw new IllegalStateException(
          "testname is null when trying to dispatch to " + target);
    }
    String methodName = ONMESSAGE_PREFIX + testname;
    Method method = null;
    Throwable exception = null;
    try {
      method = target.getClass().getMethod(methodName, Message.class);
      method.invoke(target, msg);
    } catch (SecurityException ex) {
      exception = ex;
    } catch (NoSuchMethodException ex) {
      exception = ex;
    } catch (IllegalAccessException ex) {
      exception = ex;
    } catch (InvocationTargetException ex) {
      exception = ex.getTargetException();
    }
    if (exception != null) {
      throw new IllegalStateException(exception);
    }
  }
}
