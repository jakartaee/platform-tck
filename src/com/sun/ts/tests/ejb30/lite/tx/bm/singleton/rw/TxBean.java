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
import javax.ejb.Singleton;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.CoffeeUtil;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.CoffeeEJBLite;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.RWTxBeanBase;

@Singleton
public class TxBean extends RWTxBeanBase {
  @SuppressWarnings("unused")
  @PostConstruct
  private void postConstruct() {
    int id = 0;
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
  private void preDestroy() {
    Helper.getLogger().logp(Level.FINE, "TxBean", "preDestroy",
        "About to merge and remove: " + postConstructCoffee);
    em.remove(em.merge(postConstructCoffee));
  }
}
