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
package com.sun.ts.tests.ejb30.timer.interceptor.business.singleton;

import com.sun.ts.tests.ejb30.timer.interceptor.business.common.BusinessTimerBeanBase;
import javax.ejb.EJB;

public class Client extends
    com.sun.ts.tests.ejb30.timer.interceptor.business.common.ClientBase {

  @EJB(beanInterface = BusinessTimerBean.class, beanName = "BusinessTimerBean")
  protected void setBusinessTimerBean(BusinessTimerBeanBase businessTimerBean) {
    this.businessTimerBean = businessTimerBean;
  }

  /*
   * @testName: aroundInvokeMethods
   * 
   * @assertion_ids:
   * 
   * @test_Strategy: create a timer in all interceptor methods. Verify they
   * expire as expected.
   */
}
