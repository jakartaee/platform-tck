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

package com.sun.ts.tests.ejb32.lite.timer.basic.xa;

import java.util.Date;
import java.util.logging.Level;

import javax.ejb.Timer;
import javax.ejb.TimerConfig;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.CoffeeEJBLite;
import com.sun.ts.tests.ejb30.timer.common.TimerBeanBase;
import com.sun.ts.tests.ejb30.timer.common.TimerInfo;
import com.sun.ts.tests.ejb30.timer.common.TimerUtil;

abstract public class XATimerBeanBase extends TimerBeanBase {
  @PersistenceContext(unitName = "ejblite-pu")
  protected EntityManager em;

  abstract public void persistCoffee(int id, String brandName);

  public boolean persistCoffeeCreateTimerRollback(int id, String brandName,
      Date expiration, TimerInfo info) {
    boolean result = false;
    // Timer timer = timerService.createTimer(expiration, info);
    Timer timer = timerService.createSingleActionTimer(expiration,
        new TimerConfig(info, false));
    Helper.getLogger().logp(Level.INFO, "XATimerBeanBase",
        "persistCoffeeCreateTimerRollback",
        "Temporarily created timer: " + TimerUtil.toString(timer));
    CoffeeEJBLite coffeeFound = em.find(CoffeeEJBLite.class, id);
    if (coffeeFound == null) {
      throw new IllegalStateException("Expecting 1 coffee, but found none.");
    }
    Helper.getLogger().logp(Level.INFO, "XATimerBeanBase",
        "persistCoffeeCreateTimerRollback",
        "Found the newly-persisted coffee: " + coffeeFound);
    CoffeeEJBLite c = new CoffeeEJBLite(id, brandName, id);
    // try to persist a coffee that already exists. It will cause
    // EntityExistsException and tx rollback. The timer creation must also
    // rollback.
    try {
      em.persist(c);
      em.flush();
      Helper.getLogger().logp(Level.INFO, "XATimerBeanBase",
          "persistCoffeeCreateTimerRollback",
          "Expecting EntityExistsException or another PersistenceContext, but got none");
      result = false;
    } catch (PersistenceException e) {
      Helper.getLogger().logp(Level.INFO, "XATimerBeanBase",
          "persistCoffeeCreateTimerRollback", "Got the expected " + e);
      result = true;
    }
    return result;
  }
}
