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

import com.sun.ts.tests.ejb30.common.helper.Helper;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.CoffeeEJBLite;

public class RWTxBeanBase {
  protected CoffeeEJBLite postConstructCoffee;

  @PersistenceContext(unitName = "ejblite-pu")
  protected EntityManager em;

  @TransactionAttribute(TransactionAttributeType.SUPPORTS)
  public void supports(CoffeeEJBLite c, boolean flush) {
    updatePersist(c, flush);
  }

  @TransactionAttribute(TransactionAttributeType.MANDATORY)
  public void mandatory(CoffeeEJBLite c, boolean flush) {
    updatePersist(c, flush);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRED)
  public void required(CoffeeEJBLite c, boolean flush) {
    updatePersist(c, flush);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public void requiresNew(CoffeeEJBLite c, boolean flush) {
    updatePersist(c, flush);
  }

  // default REQUIRED
  public void requiredNoExistingTransaction(CoffeeEJBLite coffee,
      boolean flush) {
    updatePersist(coffee, flush);
  }

  @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
  public void postConstructTransaction(StringBuilder sb) {
    CoffeeEJBLite c = em.find(CoffeeEJBLite.class, postConstructCoffee.getId());
    int expected = postConstructCoffee.getId();
    int actual = c.getId();
    Helper.assertEquals(
        "Check the coffee persisted inside postConstruct method: ", expected,
        actual, sb);
  }

  protected void updatePersist(CoffeeEJBLite c, boolean flush) {
    c.setPrice(c.getPrice() + 100);
    em.persist(c);
    if (flush) {
      em.flush();
    }
  }

}
