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
package com.sun.ts.tests.ejb30.lite.tx.cm.common;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.CoffeeEJBLite;

public class RWTestBeanBase0 {
  protected CoffeeEJBLite[] postConstructCoffees = new CoffeeEJBLite[2];

  // injected in subclass
  protected EntityManager em;

  protected UserTransaction ut;

  protected RWTxBeanBase txBean;

  // the following 3 setters are also exposed as business methods, called
  // by test client in */webrw/ directory to reset em, ut and txBean to the
  // values injected into test client that are web components.
  public void setEm(EntityManager em) {
    this.em = em;
  }

  public void setUt(UserTransaction ut) {
    this.ut = ut;
  }

  public void setTxBean(RWTxBeanBase b) {
    this.txBean = b;
  }

  public String mandatory(boolean flush) {
    int id = 1;
    String brandName = "mandatory";
    float price = id;
    float targetPrice = price + 100;
    CoffeeEJBLite coffee = new CoffeeEJBLite(id, brandName, price);
    CoffeeUtil.deleteCoffeeInNewUserTransaction(coffee.getId(), em, ut);
    String result = "";
    try {
      ut.begin();
      txBean.mandatory(coffee, flush);
      coffee = em.find(CoffeeEJBLite.class, id);
      result += Helper.assertEquals("Check coffee price ", targetPrice,
          coffee.getPrice());
      ut.rollback();
      CoffeeEJBLite coffeeFound = em.find(CoffeeEJBLite.class, id);
      result += Helper.assertEquals("Check coffee from em.find()", null,
          coffeeFound);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  public String required(boolean flush) {
    int id = 2;
    String brandName = "required";
    float price = id;
    float targetPrice = price + 100;
    CoffeeEJBLite coffee = new CoffeeEJBLite(id, brandName, price);
    CoffeeUtil.deleteCoffeeInNewUserTransaction(coffee.getId(), em, ut);
    String result = "";
    try {
      ut.begin();
      txBean.required(coffee, flush);
      coffee = em.find(CoffeeEJBLite.class, id);
      result += Helper.assertEquals("Check coffee price ", targetPrice,
          coffee.getPrice());
      ut.rollback();
      CoffeeEJBLite coffeeFound = em.find(CoffeeEJBLite.class, id);
      result += Helper.assertEquals("Check coffee from em.find()", null,
          coffeeFound);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  public String requiredNoExistingTransaction(boolean flush) {
    int id = 21;
    String brandName = "requiredNoExisting";
    float price = id;
    float targetPrice = price + 100;
    CoffeeEJBLite coffee = new CoffeeEJBLite(id, brandName, price);
    CoffeeUtil.deleteCoffeeInNewUserTransaction(coffee.getId(), em, ut);
    txBean.requiredNoExistingTransaction(coffee, flush);
    coffee = em.find(CoffeeEJBLite.class, id);
    return Helper.assertEquals("Check coffee price ", targetPrice,
        coffee.getPrice());
  }

  public String supports(boolean flush) {
    int id = 3;
    String brandName = "supports";
    float price = id;
    float targetPrice = price + 100;
    CoffeeEJBLite coffee = new CoffeeEJBLite(id, brandName, price);
    CoffeeUtil.deleteCoffeeInNewUserTransaction(coffee.getId(), em, ut);
    String result = "";
    try {
      ut.begin();
      txBean.supports(coffee, flush);
      coffee = em.find(CoffeeEJBLite.class, id);
      result += Helper.assertEquals("Check coffee price ", targetPrice,
          coffee.getPrice());
      ut.rollback();
      CoffeeEJBLite coffeeFound = em.find(CoffeeEJBLite.class, id);
      result += Helper.assertEquals("Check coffee from em.find()", null,
          coffeeFound);
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  public String requiresNew(boolean flush) {
    int id = 4;
    String brandName = "requiresNew";
    float price = id;
    float targetPrice = price + 100;
    CoffeeEJBLite coffee = new CoffeeEJBLite(id, brandName, price);
    CoffeeUtil.deleteCoffeeInNewUserTransaction(coffee.getId(), em, ut);
    String result = "";
    try {
      ut.begin();
      txBean.requiresNew(coffee, flush);
      coffee = em.find(CoffeeEJBLite.class, id);
      result += Helper.assertEquals("Check coffee price ", targetPrice,
          coffee.getPrice());
      ut.rollback();
      CoffeeEJBLite coffeeFound = em.find(CoffeeEJBLite.class, id);
      result += Helper.assertEquals("Check coffee id from em.find()", id,
          coffeeFound.getId());
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
    return result;
  }

  public void postConstructTransaction(StringBuilder sb) {
    for (CoffeeEJBLite postConstructCoffee : postConstructCoffees) {
      CoffeeEJBLite c = em.find(CoffeeEJBLite.class,
          postConstructCoffee.getId());
      int expected = postConstructCoffee.getId();
      int actual = c.getId();
      Helper.assertEquals("Finding postConstructCoffee ", expected, actual, sb);
    }
    txBean.postConstructTransaction(sb);
  }
}
