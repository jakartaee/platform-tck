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
package com.sun.ts.tests.ejb30.lite.singleton.concurrency.common;

import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import javax.ejb.EJB;
import com.sun.ts.lib.util.TestUtil;

public class ClientBase extends EJBLiteClientBase {
  protected final int NUM_OF_THREADS = 100;

  protected final int NUM_OF_ADDITIONS = 10000;

  protected final int NUM_TO_ADD = 999;

  protected final long CORRECT_SUM = NUM_OF_THREADS * NUM_OF_ADDITIONS
      * NUM_TO_ADD;

  /**
   * Interceptor0 is the default interceptor for all bean. Interceptor3 is
   * declared as method-level interceptors for SingletonBean's business methods
   * with "FromInterceptor" in its name. See ejb-jar.xml. The around-invoke
   * method of both Interceptor0 and Interceptor3 are in their superclass. These
   * business methods pass a param interceptorName to indicate which interceptor
   * needs to take action. If this interceptorName match the current
   * interceptor's getSimpleName(), do the operation, return result and ignore
   * the rest of the interceptor chain. Otherwise, proceed to the next in chain.
   * For interceptor0, the next is Interceptor3. For Interceptor3, the next is
   * business method.
   */
  protected static final String[] INTERCEPTORS = { "Interceptor0",
      "Interceptor3" };

  protected ConcurrencyIF singleton; // injected in subclass

  protected ConcurrencyIF singleton2; // injected in subclass

  public void lockedSum1() {
    lockedSum(singleton);
  }

  public void lockedSum2() {
    lockedSum(singleton2);
  }

  public void lockedSumFromInterceptors1() {
    lockedSumFromInterceptors(singleton);
  }

  public void lockedSumFromInterceptors2() {
    lockedSumFromInterceptors(singleton2);
  }

  public void lockedLinkedList1() {
    lockedLinkedList(singleton);
  }

  public void lockedLinkedList2() {
    lockedLinkedList(singleton2);
  }

  protected void unlockedSum(ConcurrencyIF b) {
    concurrentWrites(b, "addUnlocked", null);
    long actual = b.getAndResetUnlockedSum();
    assertNotEquals(
        "Compare CORRECT_SUM " + CORRECT_SUM + ", and actual " + actual,
        CORRECT_SUM, actual);
  }

  protected void unlockedSumFromInterceptors(ConcurrencyIF b) {
    for (String inter : INTERCEPTORS) {
      concurrentWrites(b, "addUnlockedFromInterceptor", inter);
      long actual = b.getAndResetUnlockedSumFromInterceptor(inter);
      assertNotEquals(
          "Compare CORRECT_SUM " + CORRECT_SUM + ", and actual " + actual,
          CORRECT_SUM, actual);
    }
  }

  protected void lockedSum(ConcurrencyIF b) {
    concurrentWrites(b, "addLocked", null);
    assertEquals(null, CORRECT_SUM, b.getAndResetLockedSum());
  }

  protected void lockedSumFromInterceptors(ConcurrencyIF b) {
    for (String inter : INTERCEPTORS) {
      concurrentWrites(b, "addLockedFromInterceptor", inter);
      long actual = b.getAndResetLockedSumFromInterceptor(inter);
      assertEquals(null, CORRECT_SUM, actual);
    }
  }

  protected void lockedLinkedList(ConcurrencyIF b) {
    concurrentWrites(b, "addToLinkedList", null);
    assertEquals(null, NUM_OF_THREADS * NUM_OF_ADDITIONS,
        b.getLinkedListSizeAndClear());
  }

  protected void concurrentWrites(final ConcurrencyIF b,
      final String methodName, final String interceptorName) {
    Thread[] threads = new Thread[NUM_OF_THREADS];

    for (int i = 0; i < threads.length; i++) {
      threads[i] = new Thread(new Runnable() {
        public void run() {
          for (int i = 0; i < NUM_OF_ADDITIONS; i++) {
            if ("addLocked".equals(methodName)) {
              b.addLocked(NUM_TO_ADD);
            } else if ("addUnlocked".equals(methodName)) {
              b.addUnlocked(NUM_TO_ADD);
            } else if ("addToLinkedList".equals(methodName)) {
              b.addToLinkedList(NUM_TO_ADD);
            } else if ("addLockedFromInterceptor".equals(methodName)) {
              b.addLockedFromInterceptor(interceptorName, NUM_TO_ADD);
            } else if ("addUnlockedFromInterceptor".equals(methodName)) {
              b.addUnlockedFromInterceptor(interceptorName, NUM_TO_ADD);
            }
          }
        }
      });

      threads[i].start();
    } // end for

    for (int i = 0; i < threads.length; i++) {
      try {
        threads[i].join();
      } catch (InterruptedException e) {
        TestUtil.logErr("Exception caught during concurrentWrites", e);
      }
    }
  }
}
