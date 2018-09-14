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

import static com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.common.AroundTimeoutIF.STATUS_SINGLETON_BEAN_NAME;

import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Timer;
import javax.interceptor.AroundTimeout;
import javax.interceptor.InvocationContext;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.common.helper.ServiceLocator;
import com.sun.ts.tests.ejb30.timer.common.TimeoutStatusBean;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;

//all EJBs need to declare at least one interceptor that extends InterceptorBase to make @EJB
//injection work in this class.
@EJB(name = STATUS_SINGLETON_BEAN_NAME, beanInterface = TimeoutStatusBean.class)
public class InterceptorBase {
  // we cannot use a getSimpleName() method, since it will always return the
  // simpleName of the subclass. So we have to declare a static simpleName
  // in each layer of the class hierarchy
  private static final String simpleName = "InterceptorBase";

  @SuppressWarnings("unused")
  @AroundTimeout
  private Object aroundTimeoutInInterceptorBase(InvocationContext inv)
      throws Exception {
    return handleAroundTimeout(inv, simpleName, this,
        "aroundTimeoutInInterceptorBase");
  }

  public static Object handleAroundTimeout(InvocationContext inv, String record,
      Object component, String methodName, TimeoutStatusBean... statusBean)
      throws Exception {
    addAroundInvokeRecord((Timer) inv.getTimer(), record, component, methodName,
        statusBean);
    return inv.proceed();
  }

  public static void addAroundInvokeRecord(Timer timer, String record,
      Object component, String methodName, TimeoutStatusBean... statusBean) {
    TimeoutStatusBean sta = (statusBean.length > 0) ? statusBean[0]
        : (TimeoutStatusBean) ServiceLocator
            .lookupByShortNameNoTry(STATUS_SINGLETON_BEAN_NAME);

    String recordKey = TimerUtil.getTimerName(timer)
        + AroundTimeoutIF.AROUND_TIMEOUT_RECORD_KEY_SUFFIX;

    Helper.getLogger().logp(Level.FINE, record, methodName,
        "Adding around-timeout record: " + recordKey + " : " + record
            + ", requested by : " + component);

    sta.addRecord(recordKey, record);
  }

}
