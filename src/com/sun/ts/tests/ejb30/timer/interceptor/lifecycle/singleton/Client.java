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
package com.sun.ts.tests.ejb30.timer.interceptor.lifecycle.singleton;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.timer.common.ClientBase;

public class Client extends ClientBase {
  @EJB(beanName = "LifecycleTimerBean")
  private LifecycleTimerBean bean;

  /*
   * @testName: postConstructInBeanClass
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: create a timer in singleton PostConstruct method in bean
   * class and its superclass. Verify they expire as expected.
   */
  public void postConstructInBeanClass() {
    bean.getTimers(); // the first request to activate the singleton
    passIfTimeout("LifecycleTimerBeanBase.postConstruct");
    passIfTimeout("LifecycleTimerBean.postConstruct");
  }

  /*
   * @testName: postConstructInInterceptorClasses
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: create a timer in PostConstruct methods of lifecycle
   * interceptor classes. Verify they all expire as expected.
   */
  public void postConstructInInterceptorClasses() {
    bean.getTimers(); // the first request to activate the singleton
    passIfTimeout("Interceptor1.postConstruct"); // expecting 2 timeout records
    passIfTimeout("Interceptor2.postConstruct"); // expecting 1 timeout records
    passIfRecurringTimeout("InterceptorBase.postConstruct"); // expecting 3
                                                             // timeout records
  }
}
