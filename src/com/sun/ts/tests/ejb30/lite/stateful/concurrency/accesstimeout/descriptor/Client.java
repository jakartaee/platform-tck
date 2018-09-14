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
package com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.descriptor;

import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.CONCURRENT_INVOCATION_TIMES;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.EJBs;

import com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.AccessTimeoutIF;
import com.sun.ts.tests.ejb30.lite.stateful.concurrency.accesstimeout.common.ClientBase;

@EJBs({
    @EJB(name = AccessTimeoutIF.beanClassLevelAccessTimeoutBeanLocal, beanName = "BeanClassLevelAccessTimeoutBean", beanInterface = AccessTimeoutIF.class) })
public class Client extends ClientBase {
  /*
   * @testName: beanClassLevel
   * 
   * @test_Strategy: ejb-jar.xml declares <concurrent-method> and their
   * <access-timeout>
   */

  /*
   * @testName: beanClassLevel2
   * 
   * @test_Strategy: ejb-jar.xml declares <concurrent-method> and their
   * <access-timeout>
   */

  /*
   * @testName: pingMethodInBeanSuperClass
   * 
   * @test_Strategy: ejb-jar.xml declares <concurrent-method> and their
   * <access-timeout>
   */
  public void pingMethodInBeanSuperClass() throws Exception {
    final AccessTimeoutIF b = getBeanClassLevelAccessTimeoutBeanLocal();
    List<Exception> exceptionList = concurrentPing(new Runnable() {
      public void run() {
        b.ping();
      }
    });
    checkConcurrentAccessTimeoutResult(exceptionList, 1, 1);
  }

  /*
   * @testName: beanClassMethodLevel
   * 
   * @test_Strategy: beanClassMethodLevel is a concurrent method with default
   * access-timeout. It is not declared in ejb-jar.xml with <concurrent-method>
   */
  @Override
  public void beanClassMethodLevel() throws InterruptedException {
    final AccessTimeoutIF b = getBeanClassLevelAccessTimeoutBeanLocal();
    List<Exception> exceptionList = concurrentPing(new Runnable() {
      public void run() {
        b.beanClassMethodLevel();
      }
    });
    checkConcurrentAccessTimeoutResult(exceptionList,
        CONCURRENT_INVOCATION_TIMES, 0);
  }

}
