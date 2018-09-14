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

import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.rw.TxBean.AFTER_BEGIN;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.rw.TxBean.AFTER_BEGIN_COFFEE_ID;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.rw.TxBean.BEFORE_COMPLETION;
import static com.sun.ts.tests.ejb30.lite.tx.cm.stateful.rw.TxBean.BEFORE_COMPLETION_COFFEE_ID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Stateful;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.CoffeeEJBLite;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.CoffeeUtil;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.RWTestBeanBase;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.RWTxBeanBase;

@Stateful
@TransactionManagement(TransactionManagementType.BEAN)
public class TestBean extends RWTestBeanBase {

  @EJB
  private TxBean statefulTxBean;

  @EJB(beanInterface = TxBean.class)
  @Override
  public void setTxBean(RWTxBeanBase b) {
    txBean = b;
  }

  @Override
  public String mandatory(boolean flush) {
    deleteTestCoffees();

    // tx was rolled back in RWTestBeanBase0. Verify coffees persisted in
    // afterBegin and beforeCompletion are not present.
    String result = super.mandatory(flush) + TestUtil.NEW_LINE;
    result += CoffeeUtil.verifyCoffee(AFTER_BEGIN_COFFEE_ID, AFTER_BEGIN, em,
        false);
    result += TestUtil.NEW_LINE;
    result += CoffeeUtil.verifyCoffee(BEFORE_COMPLETION_COFFEE_ID,
        BEFORE_COMPLETION, em, false);

    return result;
  }

  @Override
  public String required(boolean flush) {
    deleteTestCoffees();
    String result = super.required(flush) + TestUtil.NEW_LINE;
    result += CoffeeUtil.verifyCoffee(AFTER_BEGIN_COFFEE_ID, AFTER_BEGIN, em,
        false);
    result += TestUtil.NEW_LINE;
    result += CoffeeUtil.verifyCoffee(BEFORE_COMPLETION_COFFEE_ID,
        BEFORE_COMPLETION, em, false);

    return result;
  }

  @Override
  public String requiredNoExistingTransaction(boolean flush) {
    deleteTestCoffees();
    String result = super.requiredNoExistingTransaction(flush)
        + TestUtil.NEW_LINE;
    result += CoffeeUtil.verifyCoffee(AFTER_BEGIN_COFFEE_ID, AFTER_BEGIN, em,
        true);
    result += TestUtil.NEW_LINE;
    result += CoffeeUtil.verifyCoffee(BEFORE_COMPLETION_COFFEE_ID,
        BEFORE_COMPLETION, em, true);

    return result;
  }

  @Override
  public String requiresNew(boolean flush) {
    deleteTestCoffees();
    String result = super.requiresNew(flush) + TestUtil.NEW_LINE;
    result += CoffeeUtil.verifyCoffee(AFTER_BEGIN_COFFEE_ID, AFTER_BEGIN, em,
        true);
    result += TestUtil.NEW_LINE;
    result += CoffeeUtil.verifyCoffee(BEFORE_COMPLETION_COFFEE_ID,
        BEFORE_COMPLETION, em, true);

    return result;
  }

  public String invokeAsBusinessMethod() {
    deleteTestCoffees();
    statefulTxBean.afterBegin();
    String result = CoffeeUtil.verifyCoffee(AFTER_BEGIN_COFFEE_ID, AFTER_BEGIN,
        em, true);
    result += TestUtil.NEW_LINE;
    statefulTxBean.afterCompletion(true);
    statefulTxBean.afterCompletion(false);
    return result;
  }

  private void deleteTestCoffees() {
    // first delete the test coffee in a different business method and different
    // tx
    CoffeeUtil.deleteCoffeeInNewUserTransaction(AFTER_BEGIN_COFFEE_ID, em, ut);
    CoffeeUtil.deleteCoffeeInNewUserTransaction(BEFORE_COMPLETION_COFFEE_ID, em,
        ut);
  }

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    int[] ids = { 90206, 75600 };
    String[] brandNames = { "postConstructCoffee1", "postConstructCoffee2" };

    try {
      ut.begin();
      for (int i = 0; i < ids.length; i++) {
        postConstructCoffees[i] = CoffeeUtil.findDeletePersist(ids[i],
            brandNames[i], em);
      }
      ut.commit();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

  @SuppressWarnings("unused")
  @PreDestroy
  private void preDestroy() {
    if ((System.getProperty("ejbembed", null) == null)) {
      // we are NOT in ejbembed
      em.getEntityManagerFactory().getCache().evictAll();
      for (CoffeeEJBLite postConstructCoffee : postConstructCoffees) {
        CoffeeUtil.deleteCoffeeInNewUserTransaction(postConstructCoffee.getId(),
            em, ut);
      }
    }
  }
}
