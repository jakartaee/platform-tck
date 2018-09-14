/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.timer.basic.mdb;

import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.jms.Message;
import javax.jms.MessageListener;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.misc.getresource.common.GetResourceTest;
import com.sun.ts.tests.ejb30.timer.common.MethodDispatcher;
import com.sun.ts.tests.ejb30.timer.common.TimerInfo;
import com.sun.ts.tests.ejb30.timer.common.TimerMessageBeanBase;

abstract public class TimerBasicBeanBase2 extends TimerMessageBeanBase
    implements MessageListener {
  private static final String RESOURCE_NAME = "foo.txt";

  private static final String RESOURCE_CONTENT = "foo.txt";

  private static final String RESOURCE_NAME_ABSOLUTE = "com/sun/ts/tests/ejb30/timer/basic/mdb/foo.txt";

  private GetResourceTest tester = new GetResourceTest();

  protected TimerBasicBeanBase2() {
    super();
  }

  public void onMessage(Message message) throws RuntimeException {
    initTimerInfo(message);
    MethodDispatcher.dispatchOnMessage(message, this,
        getTimerInfo().getTestName());
  }

  @SuppressWarnings("unused")
  @Timeout
  private void timeOut(Timer timer) throws RuntimeException {
    MethodDispatcher.dispatchTimeOut(timer, this, getQFactory(),
        getReplyQueue());
  }

  //////////////////////////////////////////////////////////////////////
  // test methods
  //////////////////////////////////////////////////////////////////////
  public void onMessage_getResourceInTimeOut(Message msg) {
    getTimerService().createTimer(1, getTimerInfo());
  }

  public void onMessage_test1(Message msg) {
    getTimerService().createTimer(1, getTimerInfo());
  }

  public void timeOut_getResourceInTimeOut(Timer timer, TimerInfo ti)
      throws TestFailedException {
    tester.getResourceWithClass(getClass(), RESOURCE_NAME, RESOURCE_CONTENT);
    tester.getResourceWithClassLoader(getClass(), RESOURCE_NAME_ABSOLUTE,
        RESOURCE_CONTENT);
  }

  public void timeOut_test1(Timer timer, TimerInfo ti)
      throws TestFailedException {
    // just to verify TimeOut method is invoked and timer info can be accessed.
  }

}
