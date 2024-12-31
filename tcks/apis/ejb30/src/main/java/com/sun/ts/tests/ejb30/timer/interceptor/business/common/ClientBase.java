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
package com.sun.ts.tests.ejb30.timer.interceptor.business.common;

public class ClientBase extends com.sun.ts.tests.ejb30.timer.common.ClientBase {
  protected BusinessTimerBeanBase businessTimerBean;

  /*
   * testName: aroundInvokeMethods
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: create a timer in all interceptor methods. Verify they
   * expire as expected.
   */
  public void aroundInvokeMethods() {
    businessTimerBean.createMillisecondLaterTimer(getTestName());
    passIfTimeout("BusinessTimerBeanBase.aroundInvoke"); // expire once
    passIfTimeout("BusinessTimerBean.aroundInvoke"); // expire once
    passIfTimeout("Interceptor1.aroundInvoke"); // expire once
    passIfTimeout("Interceptor2.aroundInvoke"); // expire once
    passIfTimeout("Interceptor3.aroundInvoke"); // expire once
    passIfTimeout("InterceptorBase.aroundInvoke"); // expire 3 times
    passIfTimeout(); // expire once
  }
}
