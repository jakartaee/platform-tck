/*
 * Copyright (c) 2008, 2020 Oracle and/or its affiliates. All rights reserved.
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

import jakarta.ejb.Lock;
import jakarta.ejb.LockType;

/**
 * This class has READ locktype at class-level, which applies to all business
 * methods in this class.
 */
@Lock(LockType.READ)
abstract public class ReadLockBeanBase extends DefaultLockBeanBase {

  public long getAndResetUnlockedSum() {
    long result = unlockedSum;
    unlockedSum = 0;
    return result;
  }

  public void addUnlocked(int num) {
    for (int i = 0; i < num; i++) {
      unlockedSum++;
    }
  }
}
