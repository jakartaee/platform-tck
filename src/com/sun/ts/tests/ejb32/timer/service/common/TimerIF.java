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

package com.sun.ts.tests.ejb32.timer.service.common;

import java.util.Collection;
import java.util.Date;

import javax.ejb.ScheduleExpression;
import javax.ejb.Timer;
import javax.ejb.TimerConfig;

public interface TimerIF {
  Collection<Timer> getAllTimers();

  Timer createTimer(Date expiration, TimerConfig timerConfig);

  Timer createTimer(Date initialExpiration, long intervalDuration,
      TimerConfig timerConfig);

  Timer createTimer(long duration, TimerConfig timerConfig);

  Timer createTimer(long InitialDuration, long intervalDuration,
      TimerConfig timerConfig);

  Timer createTimer(ScheduleExpression exp, TimerConfig timerConfig);
}
