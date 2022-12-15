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

/**
 * This class has no class-level @Lock, hence the default WRITE for all business
 * methods in this class.
 */
abstract public class DefaultLockBeanBase implements ConcurrencyIF {
  protected long unlockedSum;

  protected long lockedSum;

  public long getAndResetLockedSum() {
    long result = lockedSum;
    lockedSum = 0;
    return result;
  }

  public void addLocked(int num) {
    for (int i = 0; i < num; i++) {
      lockedSum++;
    }
  }

  // dummy impl for other methods in interface
  public void addLockedFromInterceptor(String interceptorName, int num) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void addToLinkedList(Integer i) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public void addUnlockedFromInterceptor(String interceptorName, int num) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public long getAndResetLockedSumFromInterceptor(String interceptorName) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public long getAndResetUnlockedSumFromInterceptor(String interceptorName) {
    throw new UnsupportedOperationException("Not supported yet.");
  }

  public int getLinkedListSizeAndClear() {
    throw new UnsupportedOperationException("Not supported yet.");
  }
}
