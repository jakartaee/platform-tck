/*
 * Copyright (c) 2013, 2018, 2020 Oracle and/or its affiliates. All rights reserved.
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

import com.sun.ts.tests.ejb30.timer.common.TimerBeanBase;

import jakarta.ejb.Schedule;
import jakarta.ejb.Schedules;

public class AutoTimerBeanBase extends TimerBeanBase implements TimerIF {

  @Schedule(hour = "1", dayOfMonth = "1", persistent = false, info = "a0")
  void singleAutoTimerCallbackNonPersistent() {

  }

  @Schedules({
      @Schedule(hour = "12", dayOfWeek = "Mon-Thu", persistent = false, info = "a1"),
      @Schedule(hour = "11", dayOfWeek = "Fri", persistent = false, info = "a2") })
  void doubleAutoTimersCallbackNonPersistent() {

  }

  @Schedule(hour = "1", dayOfMonth = "1", persistent = true, info = "a3")
  void singleAutoTimerCallback() {

  }

  @Schedules({
      @Schedule(hour = "12", dayOfWeek = "Mon-Thu", persistent = true, info = "a4"),
      @Schedule(hour = "11", dayOfWeek = "Fri", persistent = true, info = "a5") })
  void doubleAutoTimersCallback() {

  }

  // Since Jakarta Enterprise Beans 4.0, @Schedule is repeatable
  @Schedule(hour = "12", dayOfWeek = "Mon-Thu", persistent = true, info = "a6")
  @Schedule(hour = "11", dayOfWeek = "Fri", persistent = false, info = "a7")
  void doubleAutoTimersCallbackPersistentAndNot() {

  }

}
