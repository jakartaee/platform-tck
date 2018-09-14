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

package com.sun.ts.tests.ejb32.lite.timer.schedule.tx;

import java.util.logging.Level;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.transaction.NotSupportedException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import com.sun.ts.lib.util.TestUtil;
import com.sun.ts.tests.ejb30.common.helper.Helper;

@Stateless
@TransactionManagement(TransactionManagementType.BEAN)
public class ScheduleBMTBean extends ScheduleTxBeanBase {

  @Resource
  private UserTransaction ut;

  @Override
  protected void setRollbackOnly() {
    try {
      ut.setRollbackOnly();
    } catch (SystemException ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Override
  protected void beginTransaction() {
    try {
      ut.begin();
    } catch (NotSupportedException ex) {
      throw new IllegalStateException(ex);
    } catch (SystemException ex) {
      throw new IllegalStateException(ex);
    }
  }

  @Override
  protected void commitTransaction() {
    try {
      if (ut.getStatus() == Status.STATUS_ACTIVE) {
        Helper.getLogger().logp(Level.FINE, "ScheduleBMTBean",
            "commitTransaction",
            "About to commit UserTransaction, current status "
                + TestUtil.getTransactionStatus(ut.getStatus()));
        ut.commit();
      } else if (ut.getStatus() == Status.STATUS_MARKED_ROLLBACK) {
        Helper.getLogger().logp(Level.FINE, "ScheduleBMTBean",
            "commitTransaction",
            "About to roll back UserTransaction, current status "
                + TestUtil.getTransactionStatus(ut.getStatus()));
        ut.rollback();
      }
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }
}
