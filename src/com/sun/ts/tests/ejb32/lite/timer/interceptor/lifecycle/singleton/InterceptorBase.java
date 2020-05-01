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

package com.sun.ts.tests.ejb32.lite.timer.interceptor.lifecycle.singleton;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import jakarta.ejb.TimerService;
import jakarta.interceptor.InvocationContext;

import com.sun.ts.tests.ejb30.timer.common.TimerUtil;

public class InterceptorBase {
  @Resource
  protected TimerService timerService;

  @SuppressWarnings("unused")
  @PostConstruct
  @PreDestroy
  private void postConstruct(InvocationContext inv) {
    // TimerUtil.createMillisecondLaterTimer(timerService,
    // "InterceptorBase.postConstruct");
    TimerUtil.createMillisecondLaterTimer(timerService,
        "InterceptorBase.postConstruct", false);
    try {
      inv.proceed();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception ex) {
      throw new RuntimeException(ex);
    }
  }
}
