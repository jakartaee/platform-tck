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
package com.sun.ts.tests.ejb30.lite.tx.cm.stateful.rw;

import javax.ejb.EJB;

import com.sun.ts.tests.ejb30.lite.tx.cm.common.RWClientBase;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.RWTestBeanBase;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.RWTestBeanBase0;

public class Client extends RWClientBase {
  private static final String STATEFUL_TEST_BEAN_NAME = "statefulTestBean";

  @EJB(beanName = "TestBean")
  private TestBean statefulTestBean;

  @SuppressWarnings("unused")
  @EJB(beanInterface = TestBean.class, name = STATEFUL_TEST_BEAN_NAME, beanName = "TestBean")
  private void setTestBean(RWTestBeanBase b) {
    testBean = b;
  }

  @Override
  protected RWTestBeanBase0 getTestBean() {
    return (RWTestBeanBase0) lookup(STATEFUL_TEST_BEAN_NAME, "TestBean",
        TestBean.class);
  }

  /*
   * @testName: mandatory
   * 
   * @test_Strategy:
   */
  /*
   * @testName: required
   * 
   * @test_Strategy:
   */
  /*
   * @testName: requiredNoExistingTransaction
   * 
   * @test_Strategy:
   */
  /*
   * @testName: supports
   * 
   * @test_Strategy:
   */
  /*
   * @testName: requiresNew
   * 
   * @test_Strategy:
   */

  /*
   * @testName: invokeAsBusinessMethod
   * 
   * @test_Strategy:
   */
  public void invokeAsBusinessMethod() {
    appendReason(statefulTestBean.invokeAsBusinessMethod());
  }

  /*
   * @testName: postConstructTransaction
   *
   * @test_Strategy: insert CoffeeEJBLite records in TestBean & TxBean's
   * postConstruct methods, respectively using BMT & CMT, then check the
   * existence of the records in test method postConstructTransaction
   */
}
