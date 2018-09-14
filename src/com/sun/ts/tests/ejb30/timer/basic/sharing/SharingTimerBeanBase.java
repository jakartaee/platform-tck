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
package com.sun.ts.tests.ejb30.timer.basic.sharing;

import java.util.Collection;

import javax.ejb.TimedObject;
import javax.ejb.Timer;

import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod;

abstract public class SharingTimerBeanBase
    extends TimerBeanBaseWithoutTimeOutMethod implements TimerIF, TimedObject {

  public void ejbTimeout(Timer timer) {
    timeout(timer);
  }

  public String accessTimers() throws TestFailedException {
    Collection<Timer> timers = timerService.getTimers();
    if (timers.size() == 1) {
      return "Timer in bean class: " + this + "\n";
    }
    throw new TestFailedException("Expecting 1 timer, but actual "
        + timers.size() + ", bean class: " + this);
  }
}
