/*
 * Copyright (c) 2013, 2018 Oracle and/or its affiliates. All rights reserved.
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

package com.sun.ts.tests.ejb30.lite.tx.cm.stateful.webrw;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;

import com.sun.ts.tests.ejb30.lite.tx.cm.common.RWTestBeanBase;
import com.sun.ts.tests.ejb30.lite.tx.cm.stateful.rw.TestBean;
import com.sun.ts.tests.ejb30.lite.tx.cm.stateful.rw.TxBean;

/**
 * The difference between webrw and rw test directories is, in webrw directory,
 * the test client (web components) set the em, ut and txBean values in
 * testBean. This should work since this web component client share the same
 * naming environment as the target ejb.
 */
public class Client
    extends com.sun.ts.tests.ejb30.lite.tx.cm.common.RWClientBase {
  @PersistenceContext(unitName = "ejblite-pu")
  private EntityManager em;

  @Resource
  private UserTransaction ut;

  @EJB
  private TxBean txBean;

  @SuppressWarnings("unused")
  @EJB(beanInterface = TestBean.class)
  private void setTestBean(RWTestBeanBase b) {
    testBean = b;
  }

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    testBean.setEm(em);
    testBean.setUt(ut);
    testBean.setTxBean(txBean);
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
   * @testName: postConstructTransaction
   * 
   * @test_Strategy: insert CoffeeEJBLite records in TestBean & TxBean's
   * postConstruct methods, respectively using BMT & CMT, then check the
   * existence of the records in test method postConstructTransaction
   */
}
