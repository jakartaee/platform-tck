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
package com.sun.ts.tests.ejb30.lite.stateful.concurrency.metadata.common;

import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.CONCURRENT_INVOCATION_TIMES;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.containerConcurrencyBeanLocal;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.containerConcurrencyBeanNoInterface;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.containerConcurrencyBeanRemote;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.defaultConcurrencyBeanLocal;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.defaultConcurrencyBeanNoInterface;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.defaultConcurrencyBeanRemote;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.notAllowedConcurrencyBeanLocal;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.notAllowedConcurrencyBeanNoInterface;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.notAllowedConcurrencyBeanRemote;

import javax.ejb.EJB;
import javax.ejb.EJBs;

import com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyClientBase;
import com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF;

@EJBs({
    @EJB(name = containerConcurrencyBeanLocal, beanName = "ContainerConcurrencyBean", beanInterface = StatefulConcurrencyIF.class),
    @EJB(name = defaultConcurrencyBeanLocal, beanName = "DefaultConcurrencyBean", beanInterface = StatefulConcurrencyIF.class),
    @EJB(name = notAllowedConcurrencyBeanLocal, beanName = "NotAllowedConcurrencyBean", beanInterface = StatefulConcurrencyIF.class) })
abstract public class ClientBase extends StatefulConcurrencyClientBase {

  // remote view tests are only available in JavaEE profile
  protected StatefulConcurrencyIF getContainerConcurrencyBeanLocal() {
    return (StatefulConcurrencyIF) lookup(containerConcurrencyBeanLocal, null,
        null);
  }

  protected StatefulConcurrencyIF getContainerConcurrencyBeanRemote() {
    return (StatefulConcurrencyIF) lookup(containerConcurrencyBeanRemote, null,
        null);
  }

  protected StatefulConcurrencyIF getContainerConcurrencyBeanNoInterface() {
    return (StatefulConcurrencyIF) lookup(containerConcurrencyBeanNoInterface,
        null, null);
  }

  protected StatefulConcurrencyIF getDefaultConcurrencyBeanLocal() {
    return (StatefulConcurrencyIF) lookup(defaultConcurrencyBeanLocal, null,
        null);
  }

  protected StatefulConcurrencyIF getDefaultConcurrencyBeanRemote() {
    return (StatefulConcurrencyIF) lookup(defaultConcurrencyBeanRemote, null,
        null);
  }

  protected StatefulConcurrencyIF getDefaultConcurrencyBeanNoInterface() {
    return (StatefulConcurrencyIF) lookup(defaultConcurrencyBeanNoInterface,
        null, null);
  }

  protected StatefulConcurrencyIF getNotAllowedConcurrencyBeanLocal() {
    return (StatefulConcurrencyIF) lookup(notAllowedConcurrencyBeanLocal, null,
        null);
  }

  protected StatefulConcurrencyIF getNotAllowedConcurrencyBeanRemote() {
    return (StatefulConcurrencyIF) lookup(notAllowedConcurrencyBeanRemote, null,
        null);
  }

  protected StatefulConcurrencyIF getNotAllowedConcurrencyBeanNoInterface() {
    return (StatefulConcurrencyIF) lookup(notAllowedConcurrencyBeanNoInterface,
        null, null);
  }

  /*
   * testName: notAllowed
   * 
   * @test_Strategy:
   */
  public void notAllowed() throws InterruptedException {
    StatefulConcurrencyIF[] bs = { getNotAllowedConcurrencyBeanLocal(),
        getNotAllowedConcurrencyBeanNoInterface() };
    for (StatefulConcurrencyIF b : bs) {
      checkConcurrentAccessResult(concurrentPing(b), 1, 1);
    }
  }

  /*
   * testName: containerConcurrent
   * 
   * @test_Strategy:
   */
  public void containerConcurrent() throws InterruptedException {
    containerConcurrent(getContainerConcurrencyBeanLocal(),
        getContainerConcurrencyBeanNoInterface());
  }

  /*
   * testName: defaultConcurrent
   * 
   * @test_Strategy:
   */
  public void defaultConcurrent() throws InterruptedException {
    containerConcurrent(getDefaultConcurrencyBeanLocal(),
        getDefaultConcurrencyBeanNoInterface());
  }

  protected void containerConcurrent(StatefulConcurrencyIF... bs)
      throws InterruptedException {
    for (StatefulConcurrencyIF b : bs) {
      checkConcurrentAccessResult(concurrentPing(b),
          CONCURRENT_INVOCATION_TIMES, 0);
    }
  }

}
