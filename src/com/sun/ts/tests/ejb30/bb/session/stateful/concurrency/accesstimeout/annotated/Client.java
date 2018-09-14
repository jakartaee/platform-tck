/*
 * Copyright (c) 2009, 2018 Oracle and/or its affiliates. All rights reserved.
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
package com.sun.ts.tests.ejb30.bb.session.stateful.concurrency.accesstimeout.annotated;

import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.CONCURRENT_INVOCATION_TIMES;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ejb.ConcurrentAccessException;
import javax.ejb.ConcurrentAccessTimeoutException;
import javax.ejb.EJB;
import javax.ejb.EJBs;

import com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF;
import com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.ClientBase;

@EJBs({
    @EJB(name = AccessTimeoutIF.beanClassMethodLevelOverrideAccessTimeoutBeanRemote, beanName = "BeanClassMethodLevelOverrideAccessTimeoutBean", beanInterface = AccessTimeoutRemoteIF.class),
    @EJB(name = AccessTimeoutIF.beanClassMethodLevelAccessTimeoutBeanRemote, beanName = "BeanClassMethodLevelAccessTimeoutBean", beanInterface = AccessTimeoutRemoteIF.class),
    @EJB(name = AccessTimeoutIF.beanClassLevelAccessTimeoutBeanRemote, beanName = "BeanClassLevelAccessTimeoutBean", beanInterface = AccessTimeoutRemoteIF.class),
    @EJB(name = AccessTimeoutIF.annotatedSuperClassAccessTimeoutBeanRemote, beanName = "AnnotatedSuperClassAccessTimeoutBean", beanInterface = AccessTimeoutRemoteIF.class)

})
public class Client extends ClientBase {
  protected List<Exception> concurrentPing(AccessTimeoutIF b, String m)
      throws InterruptedException {
    List<Future<String>> futureList = new ArrayList<Future<String>>();
    List<Exception> exceptionList = new ArrayList<Exception>();
    for (int i = 0; i < CONCURRENT_INVOCATION_TIMES; i++) {
      Future<String> f = null;

      if (m.equals("beanClassLevel")) {
        f = b.beanClassLevel();
      } else if (m.equals("beanClassLevel2")) {
        f = b.beanClassLevel2();
      } else if (m.equals("beanSuperClassLevel")) {
        f = b.beanSuperClassLevel();
      } else if (m.equals("beanSuperClassMethodLevel")) {
        f = b.beanSuperClassMethodLevel();
      } else if (m.equals("beanSuperClassMethodLevelOverride")) {
        f = b.beanSuperClassMethodLevelOverride();
      } else if (m.equals("beanClassMethodLevel")) {
        f = b.beanClassMethodLevel();
      } else if (m.equals("beanClassMethodLevelOverride")) {
        f = b.beanClassMethodLevelOverride();
      } else {
        throw new RuntimeException("Bad method name: " + m);
      }

      futureList.add(f);
    }
    for (Future<String> f : futureList) {
      ConcurrentAccessException e = null;
      try {
        f.get();
      } catch (ExecutionException ee) {
        e = (ConcurrentAccessTimeoutException) ee.getCause();
      }
      exceptionList.add(e);
    }
    return exceptionList;
  }

  /*
   * @testName: beanClassLevel
   * 
   * @test_Strategy:
   */
  @Override
  public void beanClassLevel() throws InterruptedException {
    List<Exception> exceptionList = concurrentPing(
        getBeanClassLevelAccessTimeoutBeanRemote(), getTestName());
    checkConcurrentAccessTimeoutResult(exceptionList, 1, 1);
  }

  /*
   * @testName: beanClassLevel2
   * 
   * @test_Strategy:
   */
  @Override
  public void beanClassLevel2() throws InterruptedException {
    List<Exception> exceptionList = concurrentPing(
        getBeanClassLevelAccessTimeoutBeanRemote(), getTestName());
    checkConcurrentAccessTimeoutResult(exceptionList, 1, 1);
  }

  /*
   * @testName: beanSuperClassLevel
   * 
   * @test_Strategy:
   */
  @Override
  public void beanSuperClassLevel() throws InterruptedException {
    List<Exception> exceptionList = concurrentPing(
        getAnnotatedSuperClassAccessTimeoutBeanRemote(), getTestName());
    checkConcurrentAccessTimeoutResult(exceptionList, 1, 1);
  }

  /*
   * @testName: beanSuperClassMethodLevel
   * 
   * @test_Strategy:
   */
  @Override
  public void beanSuperClassMethodLevel() throws InterruptedException {
    List<Exception> exceptionList = concurrentPing(
        getAnnotatedSuperClassAccessTimeoutBeanRemote(), getTestName());
    checkConcurrentAccessTimeoutResult(exceptionList, 1, 1);
  }

  /*
   * @testName: beanSuperClassMethodLevelOverride
   * 
   * @test_Strategy:
   */
  @Override
  public void beanSuperClassMethodLevelOverride() throws InterruptedException {
    List<Exception> exceptionList = concurrentPing(
        getAnnotatedSuperClassAccessTimeoutBeanRemote(), getTestName());
    checkConcurrentAccessTimeoutResult(exceptionList,
        CONCURRENT_INVOCATION_TIMES, 0);
  }

  /*
   * @testName: beanClassMethodLevel
   * 
   * @test_Strategy:
   */
  @Override
  public void beanClassMethodLevel() throws InterruptedException {
    List<Exception> exceptionList = concurrentPing(
        getBeanClassMethodLevelAccessTimeoutBeanRemote(), getTestName());
    checkConcurrentAccessTimeoutResult(exceptionList, 1, 1);
  }

  /*
   * @testName: beanClassMethodLevelOverride
   * 
   * @test_Strategy:
   */
  @Override
  public void beanClassMethodLevelOverride() throws InterruptedException {
    List<Exception> exceptionList = concurrentPing(
        getBeanClassMethodLevelOverrideAccessTimeoutBeanRemote(),
        getTestName());
    checkConcurrentAccessTimeoutResult(exceptionList,
        CONCURRENT_INVOCATION_TIMES, 0);
  }
}
