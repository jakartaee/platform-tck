/*
 * Copyright (c) 2009, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.dual;

import jakarta.ejb.Schedule;
import jakarta.ejb.Singleton;
import jakarta.ejb.Timer;
import jakarta.interceptor.Interceptors;

import com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;

@Singleton
public class AroundTimeoutBean extends TimerBeanBaseWithoutTimeOutMethod {
  @Schedule(year = "9999", info = Interceptor1.asBusiness)
  @Interceptors(Interceptor1.class)
  public void asBusiness() {

  }

  @Interceptors(Interceptor1.class)
  @Schedule(hour = "*", minute = "*", second = "*", info = Interceptor1.asTimeout)
  public void asTimeout() {
    Timer timer = TimerUtil.findTimer(timerService, Interceptor1.asTimeout);
    statusSingleton.setStatus(Interceptor1.asTimeout, true);
    statusSingleton.addRecord(Interceptor1.asTimeout,
        "Invoking " + Interceptor1.asTimeout + ", current timer: "
            + TimerUtil.toString(timer));
  }
}
