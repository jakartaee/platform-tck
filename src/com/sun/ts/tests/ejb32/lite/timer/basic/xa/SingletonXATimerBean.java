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

import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.UserTransaction;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.lite.tx.cm.common.CoffeeUtil;
import com.sun.ts.tests.ejb30.timer.common.TimerInfo;

@Singleton
@TransactionManagement(TransactionManagementType.BEAN)
public class SingletonXATimerBean extends XATimerBeanBase {
  @Resource
  private UserTransaction ut;

  @Override
  public void persistCoffee(int id, String brandName) {
    try {
      ut.begin();
      CoffeeUtil.findDeletePersist(id, brandName, em);
      ut.commit();
    } catch (Exception e) {
      Helper.getLogger().log(Level.WARNING, "Failed in findDeletePersist: ", e);
      try {
        ut.rollback();
      } catch (Exception ee) {
      }
    }
  }

  @Override
  public boolean persistCoffeeCreateTimerRollback(int id, String brandName,
      Date expireation, TimerInfo info) {
    boolean result = false;
    try {
      ut.begin();
      result = super.persistCoffeeCreateTimerRollback(id, brandName,
          expireation, info);
      ut.commit();
    } catch (Exception e) {
      Helper.getLogger().log(Level.WARNING,
          "Failed in persistCoffeeCreateTimerRollback", e);
      try {
        ut.rollback();
      } catch (Exception ee) {
      }
    }
    return result;
  }
}
