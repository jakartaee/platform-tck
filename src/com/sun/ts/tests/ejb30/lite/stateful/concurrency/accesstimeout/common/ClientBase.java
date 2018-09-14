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
package com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common;

import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.annotatedSuperClassAccessTimeoutBeanLocal;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.annotatedSuperClassAccessTimeoutBeanRemote;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.beanClassLevelAccessTimeoutBeanLocal;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.beanClassLevelAccessTimeoutBeanRemote;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.beanClassMethodLevelAccessTimeoutBeanLocal;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.beanClassMethodLevelAccessTimeoutBeanRemote;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.beanClassMethodLevelOverrideAccessTimeoutBeanLocal;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF.beanClassMethodLevelOverrideAccessTimeoutBeanRemote;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.CONCURRENT_INVOCATION_TIMES;

import java.util.List;

import javax.ejb.ConcurrentAccessTimeoutException;

import com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyClientBase;

abstract public class ClientBase extends StatefulConcurrencyClientBase {

  // remote view tests are only available in JavaEE profile
  protected AccessTimeoutIF getBeanClassMethodLevelOverrideAccessTimeoutBeanLocal() {
    return (AccessTimeoutIF) lookup(
        beanClassMethodLevelOverrideAccessTimeoutBeanLocal, null, null);
  }

  protected AccessTimeoutIF getBeanClassMethodLevelAccessTimeoutBeanLocal() {
    return (AccessTimeoutIF) lookup(beanClassMethodLevelAccessTimeoutBeanLocal,
        null, null);
  }

  protected AccessTimeoutIF getBeanClassLevelAccessTimeoutBeanLocal() {
    return (AccessTimeoutIF) lookup(beanClassLevelAccessTimeoutBeanLocal, null,
        null);
  }

  protected AccessTimeoutIF getAnnotatedSuperClassAccessTimeoutBeanLocal() {
    return (AccessTimeoutIF) lookup(annotatedSuperClassAccessTimeoutBeanLocal,
        null, null);
  }

  protected AccessTimeoutIF getBeanClassMethodLevelOverrideAccessTimeoutBeanRemote() {
    return (AccessTimeoutIF) lookup(
        beanClassMethodLevelOverrideAccessTimeoutBeanRemote, null, null);
  }

  protected AccessTimeoutIF getBeanClassMethodLevelAccessTimeoutBeanRemote() {
    return (AccessTimeoutIF) lookup(beanClassMethodLevelAccessTimeoutBeanRemote,
        null, null);
  }

  protected AccessTimeoutIF getBeanClassLevelAccessTimeoutBeanRemote() {
    return (AccessTimeoutIF) lookup(beanClassLevelAccessTimeoutBeanRemote, null,
        null);
  }

  protected AccessTimeoutIF getAnnotatedSuperClassAccessTimeoutBeanRemote() {
    return (AccessTimeoutIF) lookup(annotatedSuperClassAccessTimeoutBeanRemote,
        null, null);
  }

  protected void checkConcurrentAccessTimeoutResult(
      List<Exception> exceptionList, int nullCountExpected,
      int concurrentAccessTimeoutExceptionCountExpected) {
    int nullCount = 0;
    int concurrentAccessTimeoutExceptionCount = 0;
    for (Exception e : exceptionList) {
      if (e == null) {
        appendReason("Got no exception, which may be correct.");
        nullCount++;
      } else if (e instanceof ConcurrentAccessTimeoutException) {
        appendReason(
            "Got ConcurrentAccessTimeoutException, which may be correct: ", e);
        concurrentAccessTimeoutExceptionCount++;
      } else {
        throw new RuntimeException(
            "Expecting null or ConcurrentAccessTimeoutException, but got ", e);
      }
    }
    assertEquals("Check nullCount", nullCountExpected, nullCount);
    assertEquals("Check concurrentAccessExceptionCount",
        concurrentAccessTimeoutExceptionCountExpected,
        concurrentAccessTimeoutExceptionCount);
  }

  /*
   * testName: beanClassLevel
   * 
   * @test_Strategy:
   */
  public void beanClassLevel() throws InterruptedException {
    beanClassLevel(getBeanClassLevelAccessTimeoutBeanLocal());
  }

  protected void beanClassLevel(final AccessTimeoutIF b)
      throws InterruptedException {
    List<Exception> exceptionList = concurrentPing(new Runnable() {
      public void run() {
        b.beanClassLevel();
      }
    });
    checkConcurrentAccessTimeoutResult(exceptionList, 1, 1);
  }

  /*
   * testName: beanClassLevel2
   * 
   * @test_Strategy:
   */
  public void beanClassLevel2() throws InterruptedException {
    beanClassLevel2(getBeanClassLevelAccessTimeoutBeanLocal());
  }

  protected void beanClassLevel2(final AccessTimeoutIF b)
      throws InterruptedException {
    List<Exception> exceptionList = concurrentPing(new Runnable() {
      public void run() {
        b.beanClassLevel2();
      }
    });
    checkConcurrentAccessTimeoutResult(exceptionList, 1, 1);
  }

  /*
   * testName: beanSuperClassLevel
   * 
   * @test_Strategy:
   */
  public void beanSuperClassLevel() throws InterruptedException {
    beanSuperClassLevel(getAnnotatedSuperClassAccessTimeoutBeanLocal());
  }

  protected void beanSuperClassLevel(final AccessTimeoutIF b)
      throws InterruptedException {
    List<Exception> exceptionList = concurrentPing(new Runnable() {
      public void run() {
        b.beanSuperClassLevel();
      }
    });
    checkConcurrentAccessTimeoutResult(exceptionList, 1, 1);
  }

  /*
   * testName: beanSuperClassMethodLevel
   * 
   * @test_Strategy:
   */
  public void beanSuperClassMethodLevel() throws InterruptedException {
    beanSuperClassMethodLevel(getAnnotatedSuperClassAccessTimeoutBeanLocal());
  }

  protected void beanSuperClassMethodLevel(final AccessTimeoutIF b)
      throws InterruptedException {
    List<Exception> exceptionList = concurrentPing(new Runnable() {
      public void run() {
        b.beanSuperClassMethodLevel();
      }
    });
    checkConcurrentAccessTimeoutResult(exceptionList, 1, 1);
  }

  /*
   * testName: beanSuperClassMethodLevelOverride
   * 
   * @test_Strategy:
   */
  public void beanSuperClassMethodLevelOverride() throws InterruptedException {
    beanSuperClassMethodLevelOverride(
        getAnnotatedSuperClassAccessTimeoutBeanLocal());
  }

  protected void beanSuperClassMethodLevelOverride(final AccessTimeoutIF b)
      throws InterruptedException {
    List<Exception> exceptionList = concurrentPing(new Runnable() {
      public void run() {
        b.beanSuperClassMethodLevelOverride();
      }
    });
    checkConcurrentAccessTimeoutResult(exceptionList,
        CONCURRENT_INVOCATION_TIMES, 0);
  }

  /*
   * testName: beanClassMethodLevel
   * 
   * @test_Strategy:
   */
  public void beanClassMethodLevel() throws InterruptedException {
    beanClassMethodLevel(getBeanClassMethodLevelAccessTimeoutBeanLocal());
  }

  protected void beanClassMethodLevel(final AccessTimeoutIF b)
      throws InterruptedException {
    List<Exception> exceptionList = concurrentPing(new Runnable() {
      public void run() {
        b.beanClassMethodLevel();
      }
    });
    checkConcurrentAccessTimeoutResult(exceptionList, 1, 1);
  }

  /*
   * testName: beanClassMethodLevelOverride
   * 
   * @test_Strategy:
   */
  public void beanClassMethodLevelOverride() throws InterruptedException {
    beanClassMethodLevelOverride(
        getBeanClassMethodLevelOverrideAccessTimeoutBeanLocal());
  }

  protected void beanClassMethodLevelOverride(final AccessTimeoutIF b)
      throws InterruptedException {
    List<Exception> exceptionList = concurrentPing(new Runnable() {
      public void run() {
        b.beanClassMethodLevelOverride();
      }
    });
    checkConcurrentAccessTimeoutResult(exceptionList,
        CONCURRENT_INVOCATION_TIMES, 0);
  }

}
