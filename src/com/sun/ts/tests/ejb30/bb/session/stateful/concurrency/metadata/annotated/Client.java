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
package com.sun.ts.tests.ejb30.bb.session.stateful.concurrency.metadata.annotated;

import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.CONCURRENT_INVOCATION_TIMES;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.containerConcurrencyBeanNoInterface;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.containerConcurrencyBeanRemote;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.defaultConcurrencyBeanNoInterface;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.defaultConcurrencyBeanRemote;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.notAllowedConcurrencyBeanNoInterface;
import static com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF.notAllowedConcurrencyBeanRemote;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.ejb.ConcurrentAccessException;
import javax.ejb.EJB;
import javax.ejb.EJBs;

import com.sun.ts.tests.ejb30.common.helper.Helper;
import com.sun.ts.tests.ejb30.lite.stateful.concurrency.common.StatefulConcurrencyIF;
import com.sun.ts.tests.ejb30.lite.stateful.concurrency.metadata.common.ClientBase;

@EJBs({
    @EJB(name = containerConcurrencyBeanNoInterface, beanName = "ContainerConcurrencyBean", beanInterface = ContainerConcurrencyBean.class),
    @EJB(name = defaultConcurrencyBeanNoInterface, beanName = "DefaultConcurrencyBean", beanInterface = DefaultConcurrencyBean.class),
    @EJB(name = notAllowedConcurrencyBeanNoInterface, beanName = "NotAllowedConcurrencyBean", beanInterface = NotAllowedConcurrencyBean.class),

    @EJB(name = containerConcurrencyBeanRemote, beanName = "ContainerConcurrencyBean", beanInterface = StatefulConcurrencyRemoteIF.class),
    @EJB(name = defaultConcurrencyBeanRemote, beanName = "DefaultConcurrencyBean", beanInterface = StatefulConcurrencyRemoteIF.class),
    @EJB(name = notAllowedConcurrencyBeanRemote, beanName = "NotAllowedConcurrencyBean", beanInterface = StatefulConcurrencyRemoteIF.class) })
public class Client extends ClientBase {

  /*
   * @testName: notAllowed
   * 
   * @test_Strategy:
   */
  @Override
  public void notAllowed() throws InterruptedException {
    StatefulConcurrencyIF[] bs = { getNotAllowedConcurrencyBeanNoInterface(),
        getNotAllowedConcurrencyBeanRemote() };
    for (StatefulConcurrencyIF b : bs) {
      checkConcurrentAccessResult(concurrentPing(b), 1, 1);
    }
  }

  /*
   * @testName: containerConcurrent
   * 
   * @test_Strategy:
   */
  @Override
  public void containerConcurrent() throws InterruptedException {
    containerConcurrent(getContainerConcurrencyBeanLocal(),
        getContainerConcurrencyBeanNoInterface(),
        getContainerConcurrencyBeanRemote());
  }

  /*
   * @testName: defaultConcurrent
   * 
   * @test_Strategy:
   */
  @Override
  public void defaultConcurrent() throws InterruptedException {
    containerConcurrent(getDefaultConcurrencyBeanLocal(),
        getDefaultConcurrencyBeanNoInterface(),
        getDefaultConcurrencyBeanRemote());
  }

  @Override
  protected List<Exception> concurrentPing(StatefulConcurrencyIF b)
      throws InterruptedException {
    List<Future<String>> futureList = new ArrayList<Future<String>>();
    List<Exception> exceptionList = new ArrayList<Exception>();
    for (int i = 0; i < CONCURRENT_INVOCATION_TIMES; i++) {
      futureList.add(b.ping());
    }
    for (Future<String> f : futureList) {
      ConcurrentAccessException e = null;
      String targetBeanHash = null;
      try {
        targetBeanHash = f.get();
        appendReason("The identityHashCode of the target bean instance: "
            + targetBeanHash);
      } catch (ExecutionException ee) {
        e = (ConcurrentAccessException) ee.getCause();
      }
      exceptionList.add(e);
    }
    return exceptionList;
  }
}
