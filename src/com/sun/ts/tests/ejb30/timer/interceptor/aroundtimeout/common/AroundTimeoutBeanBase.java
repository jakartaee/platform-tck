/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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

import java.util.Arrays;
import java.util.Map;

import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.interceptor.AroundTimeout;
import javax.interceptor.Interceptors;
import javax.interceptor.InvocationContext;

import com.sun.ts.tests.ejb30.timer.common.TimerBeanBaseWithoutTimeOutMethod;
import com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.annotated.InvocationContextMethodsBean;

public class AroundTimeoutBeanBase extends TimerBeanBaseWithoutTimeOutMethod
    implements AroundTimeoutIF {

  private static final String simpleName = "AroundTimeoutBeanBase";

  @Override
  @Timeout
  @Interceptors({ Interceptor6.class, Interceptor5.class })
  protected void timeout(Timer timer) {
    super.timeout(timer);
    InterceptorBase.addAroundInvokeRecord(timer, simpleName + ".timeout", this,
        "timeout");
  }

  @AroundTimeout
  protected Object aroundTimeoutInAroundTimeoutBeanBase(InvocationContext inv)
      throws Exception {
    return InterceptorBase.handleAroundTimeout(inv, simpleName, this,
        "aroundTimeoutInAroundTimeoutBeanBase");
  }

  protected Object invocationContextMethods(InvocationContext inv)
      throws Exception {
    String rec = null;
    String m = inv.getMethod().getName();
    Timer timer = (Timer) inv.getTimer();
    if (m.equals("timeout")) {
      rec = "getMethod";
    } else {
      rec = "Expecting method name timeout, but got " + m;
    }
    InterceptorBase.addAroundInvokeRecord(timer, rec, this,
        "invocationContextMethods");

    Object[] parameters = inv.getParameters();
    if (parameters.length == 1 && parameters[0] instanceof Timer) {
      rec = "getParameters";
    } else {
      rec = "Expecting 1 param of type Timer, but got "
          + Arrays.asList(parameters);
    }
    InterceptorBase.addAroundInvokeRecord(timer, rec, this,
        "invocationContextMethods");

    Object target = inv.getTarget();
    if (target instanceof InvocationContextMethodsBean) {
      rec = "getTarget";
    } else {
      rec = "Expecting target of type InvocationContextMethodsBean, but got "
          + target;
    }
    InterceptorBase.addAroundInvokeRecord(timer, rec, this,
        "invocationContextMethods");

    Map<String, Object> contextData = inv.getContextData();
    if (contextData.size() == 0) {
      rec = "getContextData";
    } else {
      rec = "Expecting empty contextData, but got " + contextData;
    }
    InterceptorBase.addAroundInvokeRecord(timer, rec, this,
        "invocationContextMethods");

    Object[] newParams = { parameters[0] };
    inv.setParameters(newParams);
    rec = "setParameters";
    InterceptorBase.addAroundInvokeRecord(timer, rec, this,
        "invocationContextMethods");

    return inv.proceed();
  }

}
