/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb32.lite.timer.schedule.descriptor.stateless;

import javax.ejb.Stateless;
import javax.ejb.Timer;

import com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;
import com.sun.ts.tests.ejb30.timer.schedule.descriptor.common.TimeoutParamIF;

/**
 * timeout-method is only specified in ejb-jar.xml. The timeout-method (timeout)
 * is implemented in superclass TimerBeanBaseWithoutTimeOutMethod.
 * 
 * The timeout-method for both programmatic and auto timers are specified with
 * empty <method-params> element. Therefore, the container should look for
 * timeout methods without params. Overloaded methods should be ignored.
 */
@Stateless
public class EmptyParamTimeoutBean extends TimerBeanBaseWithoutTimeOutMethod
    implements TimeoutParamIF {

  public String getBeanName() {
    return EmptyParamTimeoutBean;
  }

  // to be ignored
  @SuppressWarnings("unused")
  private void auto(Timer timer) {
    throw new RuntimeException(
        "This is not a timeout method since the params do not match.");
  }

  // to be ignored
  @Override
  protected void timeout(Timer timer) {
    throw new RuntimeException(
        "This is not a timeout method since the params do not match.");
  }

  // timeout method for auto timer declared in ejb-jar.xml
  @SuppressWarnings("unused")
  private void auto() {
    timeoutAlias(TimerUtil.findTimer(timerService,
        EmptyParamTimeoutBean + AUTO_TIMER_SUFFIX));
  }

  // timeout method for programmatic timer declared in ejb-jar.xml
  protected void timeout() {
    timeoutAlias(TimerUtil.findTimer(timerService,
        EmptyParamTimeoutBean + PROGRAMMATIC_TIMER_SUFFIX));
  }
}
