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
package com.sun.ts.tests.ejb30.lite.tx.bm.singleton.rw;

import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.CoffeeUtil;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.CoffeeEJBLite;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.RWTestBeanBase;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.RWTxBeanBase;

@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class TestBean extends RWTestBeanBase {
  @EJB(beanInterface = TxBean.class)
  @Override
  public void setTxBean(RWTxBeanBase b) {
    txBean = b;
  }

  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    int[] ids = { 1000, 2000 };
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

    try {
      ut.begin();
      boolean existingDataExpected = true;
      for (int i = 0; i < ids.length; i++) {
        CoffeeUtil.findDelete(ids[i], existingDataExpected, em);
        Helper.getLogger().logp(Level.FINE, "TestBean", "postConstruct",
            "Deleted coffee but tx will be rolled back. id=" + ids[i]);
      }
      ut.rollback();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @SuppressWarnings("unused")
  @PreDestroy
  private void preDestroy() {
    for (CoffeeEJBLite postConstructCoffee : postConstructCoffees) {
      CoffeeUtil.deleteCoffeeInNewUserTransaction(postConstructCoffee.getId(),
          em, ut);
    }
  }
}
