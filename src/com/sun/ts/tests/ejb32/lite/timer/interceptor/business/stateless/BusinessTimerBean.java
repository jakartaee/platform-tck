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

package com.sun.ts.tests.ejb32.lite.timer.interceptor.business.stateless;

import javax.ejb.Stateless;
import javax.ejb.Timer;
import javax.interceptor.AroundInvoke;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;

import com.sun.ts.tests.ejb30.timer.common.TimerUtil;
import com.sun.ts.tests.ejb32.lite.timer.interceptor.business.common.BusinessTimerBeanBase;
import com.sun.ts.tests.ejb32.lite.timer.interceptor.business.common.Interceptor2;
import com.sun.ts.tests.ejb32.lite.timer.interceptor.business.common.Interceptor3;

@Stateless
@Interceptors({ Interceptor2.class })
public class BusinessTimerBean extends BusinessTimerBeanBase {
  @SuppressWarnings("unused")
  @AroundInvoke
  private Object aroundInvoke(InvocationContext inv) throws Exception {
    // TimerUtil.createMillisecondLaterTimer(timerService,
    // "BusinessTimerBean.aroundInvoke");
    TimerUtil.createMillisecondLaterTimer(timerService,
        "BusinessTimerBean.aroundInvoke", false);
    return inv.proceed();
  }

  @Interceptors(Interceptor3.class)
  @Override
  // So this method has 3 interceptors: 1, 2, 3
  // The superclass is shared among stateless, singleton and mdb tests.
  // The method-level interceptor doesn't apply in mdb. So we have to
  // duplicate this method in both singleton and stateless.
  public Timer createMillisecondLaterTimer(String name) {
    // return super.createMillisecondLaterTimer(name);
    return super.createMillisecondLaterTimer(name, false);
  }
}
