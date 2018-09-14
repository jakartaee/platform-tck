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

package com.sun.ts.tests.ejb30.common.allowed.stateful;

import com.sun.ts.tests.ejb30.common.allowed.CancelInterceptor;
import com.sun.ts.tests.ejb30.common.helper.TestFailedException;
import javax.ejb.SessionContext;
import javax.interceptor.AroundInvoke;

public class StatefulCancelInterceptor extends CancelInterceptor {
  private static StatefulCancelInterceptor instance = new StatefulCancelInterceptor();

  public StatefulCancelInterceptor() {
    super();
  }

  public static StatefulCancelInterceptor getInstance() {
    return instance;
  }

  @Override
  public void cancelTimers(SessionContext sctx) {
    TimerLocalIF timerBean = StatefulOperations.getInstance().getTimerBean();
    timerBean.findAndCancelTimer();
  }

  // @todo this interceptor method is not called since it overrides the one
  // in superclass. Make sure this is what we want.
  @Override
  @AroundInvoke
  public Object intercept(javax.interceptor.InvocationContext inv)
      throws Exception {

    Object retValue;

    retValue = super.intercept(inv);
    return retValue;
  }
}
