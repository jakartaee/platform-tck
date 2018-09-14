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

package com.sun.ts.tests.ejb32.lite.timer.schedule.tx;

import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod;
import com.sun.ts.tests.ejb30.timer.common.TimerInfo;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;

abstract public class ScheduleTxBeanBase
    extends TimerBeanBaseWithoutTimeOutMethod {
  // not a business method
  abstract protected void setRollbackOnly();

  abstract protected void beginTransaction();

  abstract protected void commitTransaction();

  @SuppressWarnings("unused")
  @Timeout
  private void ejbTimeout(Timer timer) {
    beginTransaction();
    super.timeout(timer);
    Object o = timer.getInfo();
    if (o != null) {
      TimerInfo info = (TimerInfo) o;
      String name = info.getTestName();
      if ("timeoutRollback".equals(name)) {
        Helper.getLogger()
            .fine("About to setRollbackOnly for test timeoutRollback.");
        setRollbackOnly();
      } else if ("timeoutSystemException".equals(name)
          || "timeoutSystemExceptionBMT".equals(name)) {
        throw new RuntimeException("For test " + name
            + ", transaction must fail in timeout method and retry at least once.");
      }
    }
    commitTransaction();
  }

  public String createRollback(TimerConfig timerConfig) {
    beginTransaction();
    Timer timer = TimerUtil.createSecondLaterTimer(timerService, timerConfig);
    Helper.busyWait(1500); // 1.5 seconds later, should timer expires? No
    setRollbackOnly();
    commitTransaction();
    return "Created a timer within tx: " + timer + ". Set the tx to rollback.";
  }

  public String cancelRollback(String name) {
    beginTransaction();
    Timer timer = TimerUtil.findTimer(timerService, name);
    timer.cancel();
    setRollbackOnly();
    commitTransaction();
    return "Cancelled a timer within tx: " + timer
        + ". Set the tx to rollback.";
  }

}
