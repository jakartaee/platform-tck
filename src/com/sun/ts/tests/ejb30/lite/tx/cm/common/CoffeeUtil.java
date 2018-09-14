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

import java.util.logging.Level;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import com.sun.ts.tests.ejb30.common.helper.Helper;

public class CoffeeUtil {
  private CoffeeUtil() {
  }

  public static void deleteCoffeeInNewUserTransaction(int id, EntityManager em,
      UserTransaction ut) {
    try {
      ut.begin();
      findDelete(id, false, em);
      ut.commit();
    } catch (RuntimeException e) {
      throw e;
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static void findDelete(int id, boolean existingDataExpected,
      EntityManager em) {
    CoffeeEJBLite coffeeFound = em.find(CoffeeEJBLite.class, id);
    if (coffeeFound != null) {
      Helper.getLogger().logp(Level.FINE, "RWTestBeanBase0", "findDelete",
          "Current coffee bean already exists in db, need to delete it first: "
              + coffeeFound);
      em.remove(coffeeFound);
      em.flush();
    } else if (existingDataExpected) {
      throw new IllegalStateException(
          "Tried to find coffee but got null, id=" + id);
    } else {
      Helper.getLogger().logp(Level.FINE, "RWTestBeanBase0", "findDelete",
          "Current coffee bean not in db, no need to delete it. id=" + id);
    }
  }

  public static CoffeeEJBLite findDeletePersist(int id, String brandName,
      EntityManager em) {
    findDelete(id, false, em);
    CoffeeEJBLite c = new CoffeeEJBLite(id, brandName, id);
    em.persist(c);
    return c;
  }

  public static String verifyCoffee(int id, String brandName, EntityManager em,
      boolean expectingFound) {
    CoffeeEJBLite coffeeFound = em.find(CoffeeEJBLite.class, id);
    if (expectingFound) {
      return Helper.assertEquals("verify coffee", brandName,
          coffeeFound.getBrandName());
    }
    return Helper.assertEquals("verify coffee", null, coffeeFound);
  }
}
