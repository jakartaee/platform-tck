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
package com.sun.ts.tests.ejb30.timer.interceptor.aroundtimeout.singleton.dual;

import javax.ejb.EJB;

public class Client extends com.sun.ts.tests.ejb30.timer.common.ClientBase {

  @EJB(beanName = "AroundTimeoutBean")
  protected AroundTimeoutBean aroundTimeoutBean;

  /*
   * @testName: asBusiness
   * 
   * @test_Strategy: timeout method can also be exposed as business methods.
   * When invoked as a business method, the around-invoke method in the
   * method-level interceptor is to be invoked, not the around-timeout method.
   */
  public void asBusiness() {
    // There should be no exception
    aroundTimeoutBean.asBusiness();
    appendReason("Invoked business method without errors.");
  }

  /*
   * @testName: asTimeout
   * 
   * @test_Strategy: When invoked as a timeout method, the around-timeout method
   * in the method-level interceptor is to be invoked, not the around-invoke
   * method.
   */
  public void asTimeout() {
    passIfRecurringTimeout();
  }
}
