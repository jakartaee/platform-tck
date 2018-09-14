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

import java.util.logging.Level;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.AfterBegin;
import javax.ejb.AfterCompletion;
import javax.ejb.BeforeCompletion;
import javax.ejb.Stateful;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.CoffeeEJBLite;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.CoffeeUtil;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.RWTxBeanBase;

@Stateful
public class TxBean extends RWTxBeanBase {

  public static final String AFTER_BEGIN = "afterBegin";

  public static final String BEFORE_COMPLETION = "beforeCompletion";

  public static final String AFTER_COMPLETION = "afterCompletion";

  public static final int AFTER_BEGIN_COFFEE_ID = 1602;

  public static final int BEFORE_COMPLETION_COFFEE_ID = 2503;

  public static final int AFTER_COMPLETION_COFFEE_ID = 1603;

  @AfterBegin
  public void afterBegin() {
    CoffeeUtil.findDeletePersist(AFTER_BEGIN_COFFEE_ID, AFTER_BEGIN, em);
  }

  @BeforeCompletion
  public void beforeCompletion() {
    CoffeeUtil.verifyCoffee(AFTER_BEGIN_COFFEE_ID, AFTER_BEGIN, em, true);
    CoffeeUtil.findDeletePersist(BEFORE_COMPLETION_COFFEE_ID, BEFORE_COMPLETION,
        em);
    CoffeeUtil.verifyCoffee(BEFORE_COMPLETION_COFFEE_ID, BEFORE_COMPLETION, em,
        true);
  }

  @AfterCompletion
  public void afterCompletion(boolean arg0) {
  }

  @SuppressWarnings("unused")
  @PostConstruct
  @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
  private void postConstruct() {
    int id = 9582176;
    String brandName = "postConstruct";
    float price = id;

    CoffeeUtil.findDelete(id, false, em);
    postConstructCoffee = new CoffeeEJBLite(id, brandName, price);
    updatePersist(postConstructCoffee, false);
    Helper.getLogger().logp(Level.FINE, "TxBean", "postConstruct",
        "Updated and persisted coffee: " + postConstructCoffee);
  }

  @SuppressWarnings("unused")
  @PreDestroy
  @TransactionAttribute(value = TransactionAttributeType.REQUIRES_NEW)
  private void preDestroy() {
    if ((System.getProperty("ejbembed", null) == null)) {
      // we are NOT in ejbembed
      Helper.getLogger().logp(Level.FINE, "TxBean", "preDestroy",
          "About to merge and remove: " + postConstructCoffee);
      em.remove(em.merge(postConstructCoffee));
      em.getEntityManagerFactory().getCache().evictAll();
    }
  }
}
