/*
 * Copyright (c) 2007, 2020 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.timer.interceptor.business.mdb;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.timer.common.MessageSenderBean;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;
import com.sun.ts.tests.ejb30.timer.interceptor.business.common.BusinessTimerBeanBase;
import com.sun.ts.tests.ejb30.timer.interceptor.business.common.Interceptor2;
import com.sun.ts.tests.ejb30.timer.interceptor.business.common.Interceptor3;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.ejb.EJB;
import jakarta.ejb.MessageDriven;
import jakarta.ejb.Timer;
import jakarta.interceptor.AroundInvoke;
import jakarta.interceptor.Interceptors;
import jakarta.interceptor.InvocationContext;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;

@MessageDriven
@Interceptors({ Interceptor2.class })
public class BusinessTimerBean extends BusinessTimerBeanBase
    implements MessageListener {
  @EJB(beanInterface = TestBean.class, beanName = "TestBean")
  private TestBean testBean;

  @SuppressWarnings("unused")
  @AroundInvoke
  private Object aroundInvoke(InvocationContext inv) throws Exception {
    TimerUtil.createMillisecondLaterTimer(timerService,
        "BusinessTimerBean.aroundInvoke");
    return inv.proceed();
  }

  @Interceptors(Interceptor3.class)
  public void onMessage(Message msg) {
    Helper.getLogger().info("In onMessage method of " + this);
    String testName = MessageSenderBean.getTestName(msg);

    if ("messageFromSingletonBeanToMDB".equals(testName)) {
      testBean.setReplyFromMDB(testName);
      return;
    }
    super.createMillisecondLaterTimer(testName);
  }

  @Override
  public Timer createMillisecondLaterTimer(String name) {
    return null;
  }

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    Helper.getLogger().info("In PostConstruct of " + this);
  }

  @SuppressWarnings("unused")
  @PreDestroy
  private void preDestroy() {
    Helper.getLogger().info("In PreDestroy of " + this);
  }
}
