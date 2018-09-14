/*
 * Copyright (c) 2008, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.inheritance;

import com.sun.ts.tests.ejb30.lite.singleton.concurrency.common.ConcurrencyIF;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;

/**
 * The purpose of this class is to verify a subclass can override the @Lock
 * metadata by the superclasses.
 */
@Singleton
public class InverseLockSingletonBean extends ReadLockBeanBase
    implements ConcurrencyIF {

  @Override
  public void addUnlocked(int num) {
    super.addUnlocked(num);
  }

  @Override
  public long getAndResetUnlockedSum() {
    return super.getAndResetUnlockedSum();
  }

  @Override
  @Lock(LockType.READ)
  public void addLocked(int num) {
    super.addLocked(num);
  }

  @Override
  @Lock(LockType.READ)
  public long getAndResetLockedSum() {
    return super.getAndResetLockedSum();
  }

}
