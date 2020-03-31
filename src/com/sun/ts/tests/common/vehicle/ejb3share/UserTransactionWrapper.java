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

package com.sun.ts.tests.common.vehicle.ejb3share;

import com.sun.ts.lib.util.TestUtil;

import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;
import javax.transaction.Status;

final public class UserTransactionWrapper implements EntityTransaction {
  private UserTransaction delegate;

  /**
   * These are the various status and values for a transaction STATUS_ACTIVE:0
   * STATUS_COMMITTED:3 STATUS_COMMITTING:8 STATUS_MARKED_ROLLBACK:1
   * STATUS_NO_TRANSACTION:6 STATUS_PREPARED:2 STATUS_PREPARING:7
   * STATUS_ROLLEDBACK:4 STATUS_ROLLING_BACK:9 STATUS_UNKNOWN:5 *
   */
  public UserTransactionWrapper() {
  }

  public UserTransactionWrapper(UserTransaction delegate) {
    this.delegate = delegate;
  }

  public void setDelegate(UserTransaction delegate) {
    this.delegate = delegate;
  }

  public void rollback() {
    TestUtil.logTrace("in UserTransactionWrapper.rollback()");
    if (!isActive()) {
      throw new IllegalStateException("Transaction is not active.");
    }
    try {
      delegate.rollback();
    } catch (SystemException e) {
      throw new PersistenceException(e);
    }
  }

  public boolean isActive() {
    boolean active = false;
    try {
      int txStatus = delegate.getStatus();
      TestUtil.logTrace(
          "UserTransactionWrapper.isActive().getStatus():" + txStatus);
      if ((txStatus == Status.STATUS_ACTIVE)
          || (txStatus == Status.STATUS_MARKED_ROLLBACK)) {
        active = true;
      }
    } catch (SystemException e) {
      throw new PersistenceException(e);
    }
    return active;
  }

  public void commit() {
    TestUtil.logTrace("in UserTransactionWrapper.commit()");

    if (!isActive()) {
      throw new IllegalStateException("Transaction is not active.");
    }
    try {
      delegate.commit();
    } catch (Exception e) {
      throw new javax.persistence.RollbackException(e);
    }
  }

  public void begin() {
    TestUtil.logTrace("in UserTransactionWrapper.begin()");
    if (isActive()) {
      throw new IllegalStateException("Transaction is already active.");
    }
    try {
      delegate.begin();
      TestUtil.logTrace(
          "UserTransactionWrapper.begin().getStatus():" + delegate.getStatus());
    } catch (SystemException e) {
      throw new PersistenceException(e);
    } catch (NotSupportedException e) {
      throw new PersistenceException(e);
    }
  }

  public void setRollbackOnly() {
    TestUtil.logTrace("in UserTransactionWrapper.setRollbackOnly()");
    if (!isActive()) {
      throw new IllegalStateException("Transaction is not active.");
    }
  }

  public boolean getRollbackOnly() {
    TestUtil.logTrace("in UserTransactionWrapper.getRollbackOnly()");
    if (!isActive()) {
      throw new IllegalStateException("Transaction is not active.");
    }
    try {
      int txStatus = delegate.getStatus();
      TestUtil.logTrace(
          "UserTransactionWrapper.getRollbackOnly().getStatus():" + txStatus);
      return txStatus == Status.STATUS_MARKED_ROLLBACK;
    } catch (SystemException e) {
      throw new PersistenceException(e);
    }
  }
}
