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

import javax.persistence.EntityTransaction;

final public class EntityTransactionWrapper implements EntityTransaction {
  private EntityTransaction delegate;

  public EntityTransactionWrapper() {
  }

  public EntityTransactionWrapper(EntityTransaction delegate) {
    this.delegate = delegate;
  }

  public void setDelegate(EntityTransaction delegate) {
    this.delegate = delegate;
  }

  public void rollback() {
    delegate.rollback();
  }

  public boolean isActive() {
    return delegate.isActive();
  }

  public void commit() {
    delegate.commit();
  }

  public void begin() {
    delegate.begin();
  }

  public void setRollbackOnly() {
    delegate.setRollbackOnly();
  }

  public boolean getRollbackOnly() {
    return delegate.getRollbackOnly();
  }
}
