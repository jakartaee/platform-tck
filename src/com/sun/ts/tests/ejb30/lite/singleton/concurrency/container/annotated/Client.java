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

import com.sun.ts.tests.ejb30.lite.singleton.concurrency.common.ClientBase;
import com.sun.ts.tests.ejb30.lite.singleton.concurrency.common.ConcurrencyIF;
import javax.ejb.EJB;

public class Client extends ClientBase {

  @EJB(beanName = "SingletonBean")
  public void setSingleton(ConcurrencyIF singleton) {
    this.singleton = singleton;
  }

  @EJB(beanName = "ReadSingletonBean")
  public void setSingleton2(ConcurrencyIF singleton2) {
    this.singleton2 = singleton2;
  }

  /*
   * @testName: lockedSum1
   * 
   * @test_Strategy: spawn multiple threads, invoke synchronized methods of a
   * singleton with container-managed concurrency. Expecting correct sum result.
   */
  /*
   * @testName: lockedSumFromInterceptors1
   * 
   * @test_Strategy: spawn multiple threads, invoke synchronized methods of
   * interceptors with container-managed concurrency. Expecting correct sum
   * result.
   */

  /*
   * @testName: lockedLinkedList1
   * 
   * @test_Strategy: spawn multiple threads, invoke methods of a singleton with
   * container-managed concurrency. Expecting correct data in the LinkedList.
   */

  /*
   * @testName: lockedSum2
   * 
   * @test_Strategy: spawn multiple threads, invoke synchronized methods of a
   * singleton with container-managed concurrency. Expecting correct sum result.
   */
  /*
   * @testName: lockedSumFromInterceptors2
   * 
   * @test_Strategy: spawn multiple threads, invoke synchronized methods of
   * interceptors with container-managed concurrency. Expecting correct sum
   * result.
   */

  /*
   * @testName: lockedLinkedList2
   * 
   * @test_Strategy: spawn multiple threads, invoke methods of a singleton with
   * container-managed concurrency. Expecting correct data in the LinkedList.
   */

}
