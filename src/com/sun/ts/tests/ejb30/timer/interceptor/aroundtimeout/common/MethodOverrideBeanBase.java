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
package com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common;

import com.sun.ts.tests.ejb30.timer.common.TimeoutStatusBean;
import com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod;

import jakarta.ejb.EJB;
import jakarta.ejb.Timeout;
import jakarta.ejb.Timer;
import jakarta.interceptor.AroundTimeout;
import jakarta.interceptor.ExcludeClassInterceptors;
import jakarta.interceptor.InvocationContext;

public class MethodOverrideBeanBase extends TimerBeanBaseWithoutTimeOutMethod
    implements AroundTimeoutIF {

  private static final String simpleName = "MethodOverrideBeanBase";

  @EJB
  protected TimeoutStatusBean timeoutStatusBean;

  @Override
  @Timeout
  @ExcludeClassInterceptors
  protected void timeout(Timer timer) {
    super.timeout(timer);
    InterceptorBase.addAroundInvokeRecord(timer, simpleName + ".timeout", this,
        "timeout", timeoutStatusBean);
  }

  @AroundTimeout
  protected Object aroundTimeoutInMethodOverrideBeanBase(InvocationContext inv)
      throws Exception {
    return InterceptorBase.handleAroundTimeout(inv, simpleName, this,
        "aroundTimeoutInMethodOverrideBeanBase");
  }

}
