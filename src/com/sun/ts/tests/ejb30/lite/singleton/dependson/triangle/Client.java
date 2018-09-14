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
package com.sun.ts.tests.ejb30.lite.singleton.dependson.triangle;

import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.lite.singleton.dependson.common.HistoryBean;
import javax.ejb.EJB;

public class Client extends EJBLiteClientBase {
  @EJB(beanName = "HistoryBean")
  private HistoryBean historyBean;

  @EJB(beanName = "StatelessBean")
  private StatelessBean stateless; // to force bean instance creation by this
                                   // ref

  // to force bean instance creation by this ref. X depends on Y Z, and this
  // will
  // also force Y Z to be instantiated.
  @EJB(beanName = "XSingletonBean")
  private XSingletonBean xSingleton;

  /*
   * @testName: triangleStartUp
   * 
   * @test_Strategy: A->C,B B->C expecting the exact order: C,B,A,S Although C
   * appears twice, there must be only 1 C, and BeanBase verifies all beans'
   * postConstruct method is only invoked once. A, B, C beans are annotated with
   * StartUp, and mixed concurrency management types.
   */
  public void triangleStartUp() {
    stateless.ping();
    long a = historyBean.getCreationTimeMillisByBeanName("ASingletonBean");
    long b = historyBean.getCreationTimeMillisByBeanName("BSingletonBean");
    long c = historyBean.getCreationTimeMillisByBeanName("CSingletonBean");
    long s = historyBean.getCreationTimeMillisByBeanName("StatelessBean");
    assertGreaterThan("S-time > A-time?", s, a);
    assertGreaterThan("A-time > B-time?", a, b);
    assertGreaterThan("B-time > C-time?", b, c);
  }

  /*
   * @testName: triangleNoStartUp
   * 
   * @test_Strategy: X->Z,Y Y->Z expecting the exact order: Z, Y, X Although Z
   * appears twice, there must be only 1 Z, and BeanBase verifies all beans'
   * postConstruct method is only invoked once. None is annotated with StartUp.
   * They all use default (container) concurrency management
   */
  public void triangleNoStartUp() {
    xSingleton.ping();
    long x = historyBean.getCreationTimeMillisByBeanName("XSingletonBean");
    long y = historyBean.getCreationTimeMillisByBeanName("YSingletonBean");
    long z = historyBean.getCreationTimeMillisByBeanName("ZSingletonBean");
    assertGreaterThan("X-time > Y-time?", x, y);
    assertGreaterThan("Y-time > Z-time?", y, z);
  }
}
