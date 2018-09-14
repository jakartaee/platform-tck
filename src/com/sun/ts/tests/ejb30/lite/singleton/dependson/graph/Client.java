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
package com.sun.ts.tests.ejb30.lite.singleton.dependson.graph;

import com.sun.ts.tests.ejb30.common.lite.EJBLiteClientBase;
import com.sun.ts.tests.ejb30.lite.singleton.dependson.common.HistoryBean;
import java.util.List;
import javax.ejb.EJB;

public class Client extends EJBLiteClientBase {
  @EJB(beanName = "HistoryBean")
  private HistoryBean historyBean;

  /*
   * @testName: graph
   * 
   * @test_Strategy: http://en.wikipedia.org/wiki/Directed_acyclic_graph
   */
  public void graph() {
    long t7 = historyBean.getCreationTimeMillisByBeanName("SevenBean");
    long t5 = historyBean.getCreationTimeMillisByBeanName("FiveBean");
    long t3 = historyBean.getCreationTimeMillisByBeanName("ThreeBean");
    long t11 = historyBean.getCreationTimeMillisByBeanName("ElevenBean");
    long t8 = historyBean.getCreationTimeMillisByBeanName("EightBean");
    long t2 = historyBean.getCreationTimeMillisByBeanName("TwoBean");
    long t9 = historyBean.getCreationTimeMillisByBeanName("NineBean");
    long t10 = historyBean.getCreationTimeMillisByBeanName("TenBean");

    assertGreaterThan("7-time > 11-time?", t7, t11);
    assertGreaterThan("7-time > 8-time?", t7, t8);
    assertGreaterThan("5-time > 11-time?", t5, t11);
    assertGreaterThan("3-time > 8-time?", t3, t8);
    assertGreaterThan("3-time > 10-time?", t3, t10);
    assertGreaterThan("11-time > 2-time?", t11, t2);
    assertGreaterThan("11-time > 9-time?", t11, t9);
    assertGreaterThan("11-time > 10-time?", t11, t10);
    assertGreaterThan("8-time > 9-time?", t8, t9);
  }

  /*
   * @testName: numOfInstances
   * 
   * @test_Strategy: verify one instance per singelton bean, using HistoryBean
   * (a singleton bean) as a place to save each bean instance id.
   */
  public void numOfInstances() {
    String[] beanNames = { "SevenBean", "FiveBean", "ThreeBean", "ElevenBean",
        "EightBean", "TwoBean", "NineBean", "TenBean" };
    appendReason("one instance per singelton bean?");
    for (String beanName : beanNames) {
      List<Integer> ids = historyBean.getInstanceIdsByBeanName(beanName);
      appendReason("Instances for ", beanName, ids);
      assertEquals(null, 1, ids.size());
    }
  }
}
