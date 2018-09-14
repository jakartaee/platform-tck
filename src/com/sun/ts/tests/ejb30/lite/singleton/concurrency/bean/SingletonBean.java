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

package com.sun.ts.tests.ejb30.lite.singleton.concurrency.bean;

import com.sun.ts.tests.ejb30.lite.singleton.concurrency.common.ConcurrencyIF;
import java.util.LinkedList;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.interceptor.Interceptors;

/**
 * This class is similar to ../container/SingletonBean except that this one uses
 * java language primitives like volatile and synchronized.
 */
@ConcurrencyManagement(ConcurrencyManagementType.BEAN)
@Singleton
@Interceptors({ Interceptor0.class, Interceptor3.class })
public class SingletonBean implements ConcurrencyIF {
  private static final String msg = "Should not reach here. The interceptors should have returned "
      + "the result. Maybe the interceptors are not ignored?";

  private volatile long unlockedSum;

  private volatile long lockedSum;

  private volatile LinkedList<Integer> data = new LinkedList<Integer>();

  public void addToLinkedList(Integer i) {
    synchronized (data) {
      data.add(i);
    }
  }

  public int getLinkedListSizeAndClear() {
    synchronized (data) {
      int i = data.size();
      data.clear();
      return i;
    }
  }

  public synchronized long getAndResetLockedSum() {
    long result = lockedSum;
    lockedSum = 0;
    return result;
  }

  public synchronized void addLocked(int num) {
    for (int i = 0; i < num; i++) {
      lockedSum++;
    }
  }

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

  public void addLockedFromInterceptor(String interceptorName, int num) {
    throw new RuntimeException(msg);
  }

  public void addUnlockedFromInterceptor(String interceptorName, int num) {
    throw new RuntimeException(msg);
  }

  public long getAndResetLockedSumFromInterceptor(String interceptorName) {
    throw new RuntimeException(msg);
  }

  public long getAndResetUnlockedSumFromInterceptor(String interceptorName) {
    throw new RuntimeException(msg);
  }
}
