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
package com.sun.ts.tests.ejb30.lite.singleton.concurrency.container.annotated;

import com.sun.ts.tests.ejb30.lite.singleton.concurrency.common.BeanBase;
import com.sun.ts.tests.ejb30.lite.singleton.concurrency.common.ConcurrencyIF;
import java.util.LinkedList;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;

/**
 * This class is similar to ../bm/SingletonBean. But the latter uses java
 * language primitives like volatile and synchronized.
 */
@Singleton
public class SingletonBean extends BeanBase implements ConcurrencyIF {
  private long unlockedSum;

  private long lockedSum;

  private LinkedList<Integer> data = new LinkedList<Integer>();

  @Lock(LockType.WRITE)
  public void addToLinkedList(Integer i) {
    data.add(i);
  }

  @Lock(LockType.WRITE)
  public int getLinkedListSizeAndClear() {
    int i = data.size();
    data.clear();
    return i;
  }

  // default WRITE lock
  public long getAndResetLockedSum() {
    long result = lockedSum;
    lockedSum = 0;
    return result;
  }

  // default WRITE lock
  public void addLocked(int num) {
    for (int i = 0; i < num; i++) {
      lockedSum++;
    }
  }

  @Lock(LockType.READ)
  public long getAndResetUnlockedSum() {
    long result = unlockedSum;
    unlockedSum = 0;
    return result;
  }

  @Lock(LockType.READ)
  public void addUnlocked(int num) {
    for (int i = 0; i < num; i++) {
      unlockedSum++;
    }
  }

  // TODO these overridings are not needed; method-level interceptors are
  // already specified in ejb-jar.xml, but somehow ignored.
  // Remove these method once issue 6948 is fixed.
  // If these methods are kept here, @Lock must also be set explicitly on
  // these methods.

  @Override
  // @Interceptors(Interceptor3.class)
  public void addLockedFromInterceptor(String interceptorName, int num) {
    super.addLockedFromInterceptor(interceptorName, num);
  }

  @Override
  // @Interceptors(Interceptor3.class)
  @Lock(LockType.READ)
  public void addUnlockedFromInterceptor(String interceptorName, int num) {
    super.addUnlockedFromInterceptor(interceptorName, num);
  }

  @Override
  // @Interceptors(Interceptor3.class)
  public long getAndResetLockedSumFromInterceptor(String interceptorName) {
    return super.getAndResetLockedSumFromInterceptor(interceptorName);
  }

  @Override
  // @Interceptors(Interceptor3.class)
  @Lock(LockType.READ)
  public long getAndResetUnlockedSumFromInterceptor(String interceptorName) {
    return super.getAndResetUnlockedSumFromInterceptor(interceptorName);
  }
}
